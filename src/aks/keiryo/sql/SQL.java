package aks.keiryo.sql;

import java.sql.*;

public class SQL implements SQLDefine {

    public String mUrl;//アクセスするURL
    public String mUser;//ユーザー名
    public String mPwd;//パスワード

    public Connection mConnection = null;
    public Statement mStatement = null;
    public ResultSet mResultSet = null;
    public ResultSetMetaData mMetaData = null;
    public Connection mConnection2 = null;
    public Statement mStatement2 = null;
    public Connection mConnection3 = null;
    public Statement mStatement3 = null;
    public ResultSet mResultSet2 = null;
    public ResultSetMetaData mMetaData2 = null;

    //コンストラクタ
    //url:DBへのURL
    //user:ログインユーザー名
    //pwd:ログインパスワード
    public SQL(String url, String user, String pwd) {
        mUrl = url;
        mUser = user;
        mPwd = pwd;
        try {
            Class.forName(PG_JDBC_DRIVER);
        } catch (ClassNotFoundException e) {
            System.err.println("JDBCドライバ  " + PG_JDBC_DRIVER + " がみつかりません.");
            System.exit(-1);
        }
        Open();
    }

    //オープン処理
    public int Open() {
        int ret = SQL_ERR;
        Close();//一旦終了

        try {
            mConnection = DriverManager.getConnection(mUrl, mUser, mPwd);
            mStatement = mConnection.createStatement();
            ret = SQL_OK;
        } catch (Exception e) {
            System.out.println("SQL-Open-exp:aks.keiryo.sql");
        }

        return ret;
    }

    //オープン処理
    public int Open2() {
        int ret = SQL_ERR;
        Close();//一旦終了

        try {
            mConnection2 = DriverManager.getConnection(mUrl, mUser, mPwd);
            mStatement2 = mConnection2.createStatement();
            ret = SQL_OK;
        } catch (Exception e) {
            System.out.println("SQL-Open2-exp");
        }

        return ret;
    }

    //オープン処理
    public int Open3() {
        int ret = SQL_ERR;
        Close();//一旦終了

        try {
            mConnection3 = DriverManager.getConnection(mUrl, mUser, mPwd);
            mStatement3 = mConnection3.createStatement();
            ret = SQL_OK;
        } catch (Exception e) {
            System.out.println("SQL-Open3-exp");
        }

        return ret;
    }

    //クローズ処理
    public void Close() {
        try {
            if (mStatement != null /*&& !mStatement.isClosed()*/) {
                mStatement.close();
            }

            if (mConnection != null && !mConnection.isClosed()) {
                mConnection.close();
            }
        } catch (Exception e) {
            System.out.println("SQL-Close-exp");
        }
    }

    //クエリ発行
    //ret:失敗時=-1
    public int Query(String sql) {
        int ret = SQL_ERR;

        try {
            if (mStatement == null || mStatement.isClosed()) {
                Open();
            }

            mResultSet = mStatement.executeQuery(sql);
            mMetaData = mResultSet.getMetaData();
            ret = SQL_OK;
        } catch (Exception e) {
            System.out.println("SQL-Query-exp:" + sql);
            Open();//自動再オープン
            ret = SQL_ERR;
        }
        return ret;
    }

    public int Select(String sql) {
        int ret = SQL_ERR;

        try {
            if (mStatement2 == null) {
                Open2();
            }

            Statement st = mConnection2.createStatement();

            mResultSet2 = st.executeQuery(sql);
            mMetaData2 = mResultSet2.getMetaData();

            ret = SQL_OK;
        } catch (Exception e) {
            System.out.println("SQL-Select-exp");
            Open2();//自動再オープン
            ret = SQL_ERR;
        }
        return ret;
    }

    //UPDATE SQL発行
    //ret:失敗時=-1
    public int Update(String sql) {
        int ret = SQL_ERR;

        try {
            if (mStatement3 == null || mStatement3.isClosed()) {
                Open3();
            }

            mStatement3.executeUpdate("begin;");
            ret = mStatement3.executeUpdate(sql);
            mStatement3.executeUpdate("commit;");
        } catch (Exception e) {
            System.out.println("SQL-Update-exp");
            System.out.println("SQL = " + sql);      // ←これ
            e.printStackTrace();                     // ←これ
            return SQL_ERR;
        }
        return ret;
    }
}
