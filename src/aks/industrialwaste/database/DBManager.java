package aks.industrialwaste.database;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Array;
import java.sql.Blob;
import java.sql.CallableStatement;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.NClob;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLClientInfoException;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.SQLXML;
import java.sql.Savepoint;
import java.sql.Statement;
import java.sql.Struct;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Logger;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Executor;

public class DBManager {

    protected static Logger logger_ = Logger.getLogger(DBManager.class.getName());
    protected String driver_ = "";
    public static String url_ = "";
    public static String jdbc_ = "";
    protected static String user_ = "";
    protected static String passwd_ = "";
    public static String pass_ = "";
    protected Connection con_ = null;
    public String mode_ = "";
    public static int mode = 0;
    public static boolean p_code = true;
    public static String company_name_ = "";
    public static boolean PAYMENT_FLAG = true;
    //他方のVPNのIPアドレスをdb.propertiesに登録する
    public static String host1_ = "";
    public static String dbname1_ = "";
    public static String dbname2_ = "";

    public static String client_id_ = "";
    public static String client_secret_ = "";

    public DBManager(File propertyFile) throws IOException {
        loadProperties(propertyFile);
        try {
            Class.forName(driver_);
        } catch (Exception e) {
            logger_.warning(e.toString());
        }
    }

    public static String getURL() {
        return url_;
    }

    public static String getUser() {
        return user_;
    }

    public static String getPassword() {
        return passwd_;
    }

    public synchronized Connection getConnection() throws SQLException {
        if (null == con_) {
            con_ = DriverManager.getConnection(url_, user_, passwd_);
        }

        return (Connection) new ConnectionWrapper(con_);
    }

    public synchronized void close() throws SQLException {
        if (null != con_) {
            try {
                con_.close();
            } finally {
                con_ = null;
            }
        }
    }

    @SuppressWarnings("empty-statement")
    protected void loadProperties(File propertyFile) throws IOException {
        InputStream in = new FileInputStream(propertyFile);
        try {
            Properties prop = new Properties();
            prop.load(in);

            driver_ = prop.getProperty("driver", "").trim();
            url_ = prop.getProperty("url1", "").trim();
            jdbc_ = prop.getProperty("url1", "").trim();
            user_ = prop.getProperty("user", "").trim();
            passwd_ = prop.getProperty("passwd", "").trim();
            pass_ = prop.getProperty("pass", "").trim();
            mode_ = prop.getProperty("mode", "").trim();
            if (mode_.equals("keiryo1")) {
                //既設工場
                //計量可能
                //請求機能無し
                //マスタ更新可能
                mode = 1;
            } else if (mode_.equals("keiryo2")) {
                //新設工場
                //計量可能
                //請求機能無し
                //マスタ更新可能
                mode = 2;
            } else if (mode_.equals("keiryo3")) {
                mode = 3;
            } else if (mode_.equals("seikyu")) {
                //本社（マスタ管理）
                mode = 4;
            } else if (mode_.equals("seikyu14")) {
                //本社（請求書のみ）
                mode = 14;
            } else if (mode_.equals("keiryo5")) {
                mode = 5;
            } else if (mode_.equals("keiryo_test")) {
                mode = 10;
            } else if (mode_.equals("keiryo11")) {
                //既設工場（クライアント）
                //計量は出来るがマスタ更新不可
                mode = 11;
            } else if (mode_.equals("keiryo12")) {
                //新設工場（クライアント）
                //計量は出来るがマスタ更新不可
                mode = 12;
            } else if (mode_.equals("keiryo")) {
                //計量システム単体モード
                mode = 0;
            } else if (mode_.equals("manifest")) {
                //マニフェスト空伝
                mode = 100;
            } else if (mode_.equals("keiyaku")) {
                //本社契約マスタ仮登録モード
                mode = 200;
            } else if (mode_.equals("client1")) {
                //マスタ登録可能モード
                //計量不可
                //請求機能無し
                mode = 201;
            } else if (mode_.equals("client2")) {
                //マスタ登録不可モード
                //計量不可
                //請求機能無し
                mode = 202;
            } else {
                mode = 99;
            }
            p_code = false;

            PAYMENT_FLAG = prop.getProperty("payment_flag", "").trim().equals("true");

            if ("".equals(driver_) || "".equals(url_) || "".equals(user_)) {
                // ログ
            }
        } finally {
            in.close();
        }
    }

    public static java.sql.Date convert(java.util.Date date) {
        if (null == date) {
            return null;
        }
        return new java.sql.Date(date.getTime());
    }

    public static java.util.Date convert(java.sql.Date date) {
        if (null == date) {
            return null;
        }
        return new java.util.Date(date.getTime());
    }

    public static Date convert(java.lang.String date) {
        if (null == date) {
            return null;
        }

        DateFormat foramt = new SimpleDateFormat("yyyy-MM-dd");
        Date d = null;
        try {
            d = foramt.parse(date.replaceAll("/", "-"));
        } catch (Exception e) {
        }
        return d;
    }

    public static Calendar convert(java.sql.Timestamp t) {
        if (null == t) {
            return null;
        }

        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(t.getTime());

        return c;
    }

    public static java.sql.Timestamp convert(Calendar c) {
        if (null == c) {
            return null;
        }

        return new java.sql.Timestamp(c.getTime().getTime());
    }

    public static PreparedStatement prepareStatement(
             Connection con,
             String table,
             String selectList,
             SearchCondition[] searchCondition,
             SortCondition[] sortCondition,
             int limit)
             throws SQLException {
        if (null == con) {
            throw new NullPointerException("'con' cannot be null.");
        }

        String s = String.format("select %s from %s ", selectList, table);
        List<Object> values = new ArrayList<>();
        String str = "";
        // 検索条件
        if ((null != searchCondition) && (0 < searchCondition.length)) {
            String cond = "";
            for (SearchCondition sc : searchCondition) {
                if (null == sc) {
                    continue;
                }
                cond = String.format(
                         "%s %s %s ? %s ",
                         cond,
                         sc.getColumn().getName(),
                         sc.getCompareOperator().getOperator(),
                         sc.getCombineOperator().getOperator());
                Object o = sc.getValue();
                if (o instanceof java.util.Calendar) {
                    o = convert((java.util.Calendar) o);
                } else if (o instanceof java.util.Date) {
                    o = convert((java.util.Date) o);
                } else {
                }
                if (CompareOperator.LIKE == sc.getCompareOperator()) {
                    values.add(String.format("%%%s%%", o.toString()));
                } else if (CompareOperator.IN == sc.getCompareOperator()) {
                    List<String> list = (List) o;
                    str = "";

                    for (int i = 0; i < list.size(); i++) {
                        str += String.format("%s,", (list.get(i)));

                    }
                    str = str.substring(0, str.length() - 1);
                    values.add(str);

                } else {
                    values.add(o);
                }
            }

            if (!"".equals(cond.trim())) {
                if (table.equals("v1_weigh_data") || table.equals("v1_manifesto_data")) {
                    if (cond.indexOf("(") > 0) {
                        if (cond.indexOf("ANY (ARRAY") > 0) {
                            cond = cond + ")";
                        }

                        s = String.format("%s where %s ", s, cond + ")");
                    } else {
                        s = String.format("%s where %s ", s, cond);
                    }
                } else {
                    s = String.format("%s where %s ", s, cond);
                }
            }
        }
        // ソート条件
        if ((null != sortCondition) && (0 < sortCondition.length)) {
            String cond = "";
            for (SortCondition so : sortCondition) {
                if (null == so) {
                    continue;
                }
                cond = String.format(
                         "%s %s %s, ",
                         cond,
                         so.getColumn().getName(),
                         so.getOrder().getOrder());
            }
            if (!"".equals(cond.trim())) {
                s = String.format(
                         "%s order by %s ",
                         s,
                         cond.trim().replaceAll(",$", "").trim());
            }
        }
        // 取得レコード数
        if (0 < limit) {
            s = String.format("%s limit %d", s, limit);
        }

        PreparedStatement ps = con.prepareStatement(
                 s,
                 ResultSet.TYPE_SCROLL_INSENSITIVE,
                 ResultSet.CONCUR_READ_ONLY);
        for (int i = 0; i < values.size(); i++) {
            Object o = values.get(i);
            if (o instanceof Calendar) {
                ps.setObject(i + 1, convert((Calendar) o));
            } else if (o instanceof java.util.Date) {
                ps.setObject(i + 1, convert((java.util.Date) o));
            } else if (o instanceof Number) {
                ps.setObject(i + 1, (Number) o);
            } else if (o instanceof String) {
                if (o.toString().indexOf(",") > 0) {
                    ps.setString(i + 1, o.toString().replaceAll(",", "','"));
                } else {
                    ps.setObject(i + 1, o);
                }
            } else {

                ps.setObject(i + 1, o);

            }
        }
        PreparedStatement ps0 = con.prepareStatement(ps.toString().replaceAll("'',''", "','"));
        return ps0;
    }

    private class ConnectionWrapper implements Connection {

        private Connection con_;

        public ConnectionWrapper(Connection con) {
            if (null == con) {
                throw new NullPointerException("'con' cannot be null.");
            }
            con_ = con;
        }

        public void clearWarnings() throws SQLException {
            con_.clearWarnings();
        }

        public void close() {
        }

        public void commit() throws SQLException {
            con_.commit();
        }

        public Statement createStatement() throws SQLException {
            return con_.createStatement();
        }

        public Statement createStatement(
                 int resultSetType,
                 int resultSetConcurrency)
                 throws SQLException {
            return con_.createStatement(resultSetType, resultSetConcurrency);
        }

        public Statement createStatement(
                 int resultSetType,
                 int resultSetConcurrency,
                 int resultSetHoldability)
                 throws SQLException {
            return con_.createStatement(
                     resultSetType,
                     resultSetConcurrency,
                     resultSetHoldability);
        }

        public boolean getAutoCommit() throws SQLException {
            return con_.getAutoCommit();
        }

        public String getCatalog() throws SQLException {
            return con_.getCatalog();
        }

        public int getHoldability() throws SQLException {
            return con_.getHoldability();
        }

        public DatabaseMetaData getMetaData() throws SQLException {
            return con_.getMetaData();
        }

        public int getTransactionIsolation() throws SQLException {
            return con_.getTransactionIsolation();
        }

        public Map<String, Class<?>> getTypeMap() throws SQLException {
            return con_.getTypeMap();
        }

        public SQLWarning getWarnings() throws SQLException {
            return con_.getWarnings();
        }

        public boolean isClosed() throws SQLException {
            return con_.isClosed();
        }

        public boolean isReadOnly() throws SQLException {
            return con_.isReadOnly();
        }

        public String nativeSQL(String sql) throws SQLException {
            return con_.nativeSQL(sql);
        }

        public CallableStatement prepareCall(String sql) throws SQLException {
            return con_.prepareCall(sql);
        }

        public CallableStatement prepareCall(
                 String sql,
                 int resultSetType,
                 int resultSetConcurrency)
                 throws SQLException {
            return con_.prepareCall(sql, resultSetType, resultSetConcurrency);
        }

        public CallableStatement prepareCall(
                 String sql,
                 int resultSetType,
                 int resultSetConcurrency,
                 int resultSetHoldability)
                 throws SQLException {
            return con_.prepareCall(
                     sql,
                     resultSetType,
                     resultSetConcurrency,
                     resultSetHoldability);
        }

        @Override
        public PreparedStatement prepareStatement(String sql) throws SQLException {
            return con_.prepareStatement(sql);
        }

        public PreparedStatement prepareStatement(String sql, int autoGeneratedKeys)
                 throws SQLException {
            return con_.prepareStatement(sql);
        }

        public PreparedStatement prepareStatement(String sql, int[] columnIndexes)
                 throws SQLException {
            return con_.prepareStatement(sql, columnIndexes);
        }

        public PreparedStatement prepareStatement(
                 String sql,
                 int resultSetType,
                 int resultSetConcurrency)
                 throws SQLException {
            return con_.prepareStatement(sql, resultSetType, resultSetConcurrency);
        }

        public PreparedStatement prepareStatement(
                 String sql,
                 int resultSetType,
                 int resultSetConcurrency,
                 int resultSetHolability)
                 throws SQLException {
            return con_.prepareStatement(
                     sql,
                     resultSetType,
                     resultSetConcurrency,
                     resultSetHolability);
        }

        public PreparedStatement prepareStatement(String sql, String[] columnNames)
                 throws SQLException {
            return con_.prepareStatement(sql, columnNames);
        }

        public void releaseSavepoint(Savepoint savepoint) throws SQLException {
            con_.releaseSavepoint(savepoint);
        }

        public void rollback() throws SQLException {
            con_.rollback();
        }

        public void rollback(Savepoint savepoint) throws SQLException {
            con_.rollback(savepoint);
        }

        public void setAutoCommit(boolean autoCommit) throws SQLException {
            con_.setAutoCommit(autoCommit);
        }

        public void setCatalog(String catalog) throws SQLException {
            con_.setCatalog(catalog);
        }

        public void setHoldability(int holdability) throws SQLException {
            con_.setHoldability(holdability);
        }

        public void setReadOnly(boolean readOnly) throws SQLException {
            con_.setReadOnly(readOnly);
        }

        public Savepoint setSavepoint() throws SQLException {
            return con_.setSavepoint();
        }

        public Savepoint setSavepoint(String name) throws SQLException {
            return con_.setSavepoint(name);
        }

        public void setTransactionIsolation(int level) throws SQLException {
            con_.setTransactionIsolation(level);
        }

        public void setTypeMap(Map<String, Class<?>> map) throws SQLException {
            con_.setTypeMap(map);
        }

        public Clob createClob() throws SQLException {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        public Blob createBlob() throws SQLException {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        public NClob createNClob() throws SQLException {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        public SQLXML createSQLXML() throws SQLException {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        public boolean isValid(int timeout) throws SQLException {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        public void setClientInfo(String name, String value) throws SQLClientInfoException {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        public void setClientInfo(Properties properties) throws SQLClientInfoException {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        public String getClientInfo(String name) throws SQLException {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        public Properties getClientInfo() throws SQLException {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        public Array createArrayOf(String typeName, Object[] elements) throws SQLException {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        public Struct createStruct(String typeName, Object[] attributes) throws SQLException {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        public <T> T unwrap(Class<T> iface) throws SQLException {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        public boolean isWrapperFor(Class<?> iface) throws SQLException {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public void setSchema(String schema) throws SQLException {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public String getSchema() throws SQLException {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public void abort(Executor executor) throws SQLException {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public void setNetworkTimeout(Executor executor, int milliseconds) throws SQLException {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public int getNetworkTimeout() throws SQLException {
            throw new UnsupportedOperationException("Not supported yet.");
        }
    }
}
