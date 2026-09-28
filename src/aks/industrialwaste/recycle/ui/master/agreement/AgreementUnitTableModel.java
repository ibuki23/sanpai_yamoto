package aks.industrialwaste.recycle.ui.master.agreement;

import aks.industrialwaste.database.DBManager;
import aks.industrialwaste.database.SearchCondition;
import aks.industrialwaste.database.SortCondition;
import aks.industrialwaste.recycle.ui.master.dis_office.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import javax.swing.table.AbstractTableModel;
import aks.industrialwaste.recycle.ui.master.ZipDecompresser;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.zip.ZipException;

public class AgreementUnitTableModel extends AbstractTableModel {

    public static final int COLUMN_COUNT = 11;
    public static final int COLUMN_ENT_ID = 0;
    public static final int COLUMN_ENT_NAME = 1;
    public static final int COLUMN_OFFICE_ID = 2;
    public static final int COLUMN_OFFICE_NAME = 3;
    public static final int COLUMN_GOODS_ID = 4;
    public static final int COLUMN_GOODS_NAME = 5;
    public static final int COLUMN_GOODS_UNIT = 6;
    public static final int COLUMN_GOODS_UNIT_NO = 7;
    public static final int COLUMN_GOODS_UNIT_NO_NAME = 8;
    public static final int COLUMN_OPTION1_NAME = 9;
    public static final int COLUMN_CARRY_NAME = 10;

    private static final String[] COLUMN_NAMES = {
        "ent_id",
        "ent_name",
        "office_id",
        "office_name",
        "goods_id",
        "goods_name",
        "goods_unit",
        "goods_unit_no",
        "goods_unit_no_name",
        "option1_name",
        "carry_name"

    };
    protected static Logger logger_
             = Logger.getLogger(Dis_officeTableModel.class.getName());
    private Connection con_;
    private List<Map<String, Object>> rows_
             = Collections.synchronizedList(new ArrayList<Map<String, Object>>());

    public AgreementUnitTableModel(Connection con) {
        con_ = con;
    }

    public int getRowCount() {
        return rows_.size();
    }

    public int getColumnCount() {
        return COLUMN_COUNT;
    }

    public Object getValueAt(int row, int column) {
        return rows_.get(row).get(COLUMN_NAMES[column]);
    }

    public Object getValueAt(int row, String columnName) {
        return rows_.get(row).get(columnName);
    }

    @Override
    public boolean isCellEditable(int row, int column) {
        return (COLUMN_GOODS_UNIT == column);
    }

    @Override
    public void setValueAt(Object value, int row, int column) {
        Map<String, Object> columns = rows_.get(row);

        columns.put(COLUMN_NAMES[column], value);
    }

    public Map<String, Object> getColumns(int row) {
        return rows_.get(row);
    }

    public void addRow(Map<String, Object> columns) {
        Map<String, Object> s = new HashMap<>();

        s.put("ent_id", columns.get("ent_id"));
        s.put("ent_name", columns.get("ent_name"));
        s.put("office_id", columns.get("id"));
        s.put("office_name", columns.get("name"));
        s.put("goods_id", columns.get("goods_id"));
        s.put("goods_name", columns.get("goods_name"));
        s.put("goods_unit", columns.get("goods_unit"));
        s.put("goods_unit_no", columns.get("goods_unit_no"));
        s.put("goods_unit_no_name", columns.get("goods_unit_no_name"));
        s.put("option1_name", columns.get("option1_name"));
        s.put("carry_name", columns.get("carry_name"));

        rows_.add(s);

        fireTableDataChanged();
    }

    public void removeRow(int row) {
        rows_.remove(row);
        fireTableDataChanged();
    }

    public void refreshRow() {
        fireTableDataChanged();
    }

    public void clear() {
        rows_.clear();
    }

    public void refresh(SearchCondition[] search, SortCondition[] sort) {
        clear();
        try {
            PreparedStatement ps
                     = DBManager.prepareStatement(
                              con_,
                              "v0_office_unit",
                              "*",
                              (null == search) ? (new SearchCondition[0]) : (search),
                              (null == sort) ? (new SortCondition[0]) : (sort),
                              3000);
            try {
                ResultSet rs = ps.executeQuery();
                ResultSetMetaData md = rs.getMetaData();
                while (rs.next()) {
                    Map<String, Object> columns = new HashMap<>();
                    for (int i = 1; i <= md.getColumnCount(); i++) {
                        String s = md.getColumnName(i);
                        switch (md.getColumnType(i)) {
                            case Types.VARCHAR:
                                columns.put(s, rs.getString(i));
                                break;
                            case Types.INTEGER:
                                if (rs.getObject(i) != null) {
                                    columns.put(s, rs.getInt(i));
                                } else {
                                    columns.put(s, null);
                                }
                                break;
                            case Types.DOUBLE:
                                columns.put(s, rs.getDouble(i));
                                break;
                            case Types.BOOLEAN:
                                columns.put(s, rs.getBoolean(i));
                                break;
                            case Types.DATE:
                                columns.put(s, rs.getDate(i));
                                break;
                            case Types.TIMESTAMP:
                                columns.put(s, DBManager.convert(rs.getTimestamp(i)));
                                break;
                            default:
                                columns.put(s, rs.getObject(i));
                        }
                    }
                    rows_.add(columns);
                }
            } finally {
                ps.close();
            }
        } catch (SQLException e) {
            logger_.warning(e.toString());
        }
        fireTableDataChanged();
    }

    public void refresh_limit(SearchCondition[] search, SortCondition[] sort, int limit_1, int limit_2) {
        clear();
        try {
            PreparedStatement ps
                     = DBManager.prepareStatement(
                              con_,
                              "v0_office",
                              "*",
                              (null == search) ? (new SearchCondition[0]) : (search),
                              (null == sort) ? (new SortCondition[0]) : (sort),
                              0);
            try {
                ResultSet rs = ps.executeQuery();
                ResultSetMetaData md = rs.getMetaData();
                while (rs.next()) {
                    Map<String, Object> columns = new HashMap<>();
                    for (int i = 1; i <= md.getColumnCount(); i++) {
                        String s = md.getColumnName(i);
                        switch (md.getColumnType(i)) {
                            case Types.VARCHAR:
                                columns.put(s, rs.getString(i));
                                break;
                            case Types.INTEGER:
                                if (rs.getObject(i) != null) {
                                    columns.put(s, rs.getInt(i));
                                } else {
                                    columns.put(s, null);
                                }
                                break;
                            case Types.DOUBLE:
                                columns.put(s, rs.getDouble(i));
                                break;
                            case Types.BOOLEAN:
                                columns.put(s, rs.getBoolean(i));
                                break;
                            case Types.DATE:
                                columns.put(s, rs.getDate(i));
                                break;
                            case Types.TIMESTAMP:
                                columns.put(s, DBManager.convert(rs.getTimestamp(i)));
                                break;
                            default:
                                columns.put(s, rs.getObject(i));
                        }
                    }

                    rows_.add(columns);
                }
            } finally {
                ps.close();
            }
        } catch (SQLException e) {
            logger_.warning(e.toString());
        }

        fireTableDataChanged();
    }

    public int getNextID(int id) {
        int ret = 0;
        try {
            String s = "select max(id)as id from v0_office where ent_id = ? ;";
            PreparedStatement ps = con_.prepareStatement(s);
            try {
                ps.setObject(1, id);
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    ret = rs.getInt(1) + 1;
                }
            } finally {
                ps.close();
            }
        } catch (SQLException e) {
        }

        return ret;
    }

    public Connection getConnection() {
        return con_;
    }

    public boolean insert() {
        try {

            for (int i = 0; i < rows_.size(); i++) {
                Statement st = con_.createStatement();
                st.executeUpdate("begin;");
                try {
                    String s = new StringBuilder()
                             .append("insert into m_office_unit ")
                             .append("(ent_id, office_id, goods_id, goods_unit) ")
                             .append("values(")
                             .append("?, ?, ?, ?")
                             .append("); ")
                             .toString();
                    PreparedStatement ps = con_.prepareStatement(s);
                    try {
                        Map<String, Object> columns = getColumns(i);
                        ps.setObject(1, columns.get("ent_id"));
                        ps.setObject(2, columns.get("office_id"));
                        ps.setObject(3, columns.get("goods_id"));

                        //単価の入力があれば登録
                        Object o = columns.get("goods_unit");
                        if (o instanceof String) {
                            try {
                                columns.put("goods_unit", Integer.valueOf((String) columns.get("goods_unit")));
                            } catch (NumberFormatException ne) {
                                columns.put("goods_unit", 0);
                            }
                        }
                        ps.setObject(4, columns.get("goods_unit"));
                        if ((Integer) columns.get("goods_unit") > 0) {
                            try {
                                ps.executeUpdate();
                            } catch (SQLException se00) {
                                System.out.print("");
                            }
                        }
                    } finally {
                        ps.close();
                    }

                    st.executeUpdate("commit;");

                } catch (SQLException e) {
                    st.executeUpdate("rollback;");
                    throw e;
                } finally {
                    st.close();
                }
            }

        } catch (Exception e) {
            logger_.warning(e.toString());
            return false;
        }

        return true;
    }

    public boolean update(Map<String, Object> updata) {
        return true;
    }

    public boolean delete(int ent_id, int office_id) {
        try {
            Statement st = con_.createStatement();
            st.executeUpdate("begin;");
            try {
                String s = new StringBuilder()
                         .append("delete from m_office_unit ")
                         .append("where ent_id = ? and office_id = ?;")
                         .toString();
                PreparedStatement ps = con_.prepareStatement(s);
                try {
                    ps.setObject(1, ent_id);
                    ps.setObject(2, office_id);
                    ps.executeUpdate();
                } finally {
                    ps.close();
                }

                st.executeUpdate("commit;");

            } catch (SQLException e) {
                st.executeUpdate("rollback;");
                throw e;
            } finally {
                st.close();
            }

        } catch (Exception e) {
            logger_.warning(e.toString());
            return false;
        }

        return true;
    }

    public int import_master() {
        int ret = 0;
        try {

            Statement st = con_.createStatement();
            st.executeUpdate("begin;");
            try {
                String s = new StringBuilder()
                         .append("select get_master_data_changed(?::regclass, ?::text, ?::text, ?::text)as cnt;")
                         .toString();
                PreparedStatement ps = con_.prepareStatement(s);
                try {
                    ps.setObject(1, "m_discharge_office");
                    ps.setObject(2, DBManager.host1_);
                    ps.setObject(3, DBManager.dbname1_);
                    ps.setObject(4, DBManager.dbname2_);
                    ResultSet rs = ps.executeQuery();
                    if (rs.next()) {
                        ret = rs.getInt("cnt");
                    }
                } finally {
                    ps.close();
                }

                st.executeUpdate("commit;");
            } catch (SQLException e) {
                st.executeUpdate("rollback;");
                throw e;
            } finally {
                st.close();
            }
        } catch (Exception e) {
            logger_.warning(e.toString());
            return -1;
        }

        return ret;
    }

    //既設プラント契約マスタ取込
    public boolean import_master1() {

        ZipDecompresser za = new ZipDecompresser();
        try {
            File file = new File("H:\\master_csv\\m_discharge_office.zip");
            File file2 = new File("C:\\csv\\master_csv\\m_discharge_office.zip");
            za.unzip(file, file2);
        } catch (ZipException e) {
        } catch (IOException e) {
        }

        try {
            Statement st = con_.createStatement();
            st.executeUpdate("begin;");
            st.executeUpdate("delete from m_discharge_office;");
            try {
                String s = new StringBuilder()
                         .append("SET CLIENT_ENCODING TO 'UTF8';\n COPY m_discharge_office from E'C:\\\\csv\\\\master_csv\\\\m_discharge_office\\\\m_discharge_office.csv' CSV;")
                         .toString();
                PreparedStatement ps = con_.prepareStatement(s);
                try {
                    ps.executeUpdate();
                } finally {
                    ps.close();
                }

                st.executeUpdate("commit;");

                s = new StringBuilder()
                         .append("SELECT setval('m_discharge_office_sno_seq', (select max(sno) from m_discharge_office)::bigint); ")
                         .toString();
                PreparedStatement ps_cnt = con_.prepareStatement(s);
                try {
                    ps_cnt.executeQuery();
                } finally {
                    ps_cnt.close();
                }

            } catch (SQLException e) {
                st.executeUpdate("rollback;");
                throw e;
            } finally {
                st.close();
            }
        } catch (Exception e) {
            logger_.warning(e.toString());
            return false;
        }

        return true;
    }

    //新設プラント契約マスタ取込
    public boolean import_master2() {

        ZipDecompresser za = new ZipDecompresser();
        try {
            File file = new File("H:\\master_csv\\m_discharge_office.zip");
            File file2 = new File("C:\\csv\\master_csv\\m_discharge_office.zip");
            za.unzip(file, file2);
        } catch (ZipException e) {
        } catch (IOException e) {
        }

        try {
            Statement st = con_.createStatement();
            st.executeUpdate("begin;");
            st.executeUpdate("delete from m_discharge_office;");
            try {
                String s = new StringBuilder()
                         .append("SET CLIENT_ENCODING TO 'UTF8';\n COPY m_discharge_office from E'C:\\\\csv\\\\master_csv\\\\m_discharge_office\\\\m_discharge_office.csv' CSV;")
                         .toString();
                PreparedStatement ps = con_.prepareStatement(s);
                try {
                    ps.executeUpdate();
                } finally {
                    ps.close();
                }

                st.executeUpdate("commit;");

                s = new StringBuilder()
                         .append("SELECT setval('m_discharge_office_sno_seq', (select max(sno) from m_discharge_office)::bigint); ")
                         .toString();
                PreparedStatement ps_cnt = con_.prepareStatement(s);
                try {
                    ps_cnt.executeQuery();
                } finally {
                    ps_cnt.close();
                }

            } catch (SQLException e) {
                st.executeUpdate("rollback;");
                throw e;
            } finally {
                st.close();
            }
        } catch (Exception e) {
            logger_.warning(e.toString());
            return false;
        }

        return true;
    }

    //本社マスタ出力
    public boolean export_master1() {

        try {
            Statement st = con_.createStatement();
            try {
                //本社側のC:\csv\satouにファイル出力
                String s = new StringBuilder()
                         .append("SET CLIENT_ENCODING TO 'UTF8';\n COPY m_discharge_office to E'C:\\\\csv\\\\satou\\\\m_discharge_office.csv' CSV;")
                         .toString();
                PreparedStatement ps = con_.prepareStatement(s);
                try {
                    ps.executeUpdate();
                } finally {
                    ps.close();
                }
            } catch (SQLException e) {
                throw e;
            } finally {
                st.close();
            }
        } catch (Exception e) {
            logger_.warning(e.toString());
            return false;
        }

        return true;
    }

    public void report(File f) {
        try {
            BufferedWriter out = new BufferedWriter(
                     new OutputStreamWriter(
                              new FileOutputStream(f), "MS932"));
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
            try {
                // タイトル
                out.write(
                         "取引先ID,取引先名称,取引先住所,現場ID,現場名称,現場正式名称,現場所在地1,現場TEL,現場終了,"
                         + "自動更新,契約開始日,契約終了日,契約状況,請求先ID,請求先名,備考,運搬業者１,運搬業者２,運搬業者３");
                out.newLine();
                synchronized (rows_) {
                    for (Map<String, Object> columns : rows_) {
                        // id
                        Integer i = (Integer) columns.get("ent_id");
                        StringBuilder sb = new StringBuilder().append(i).append(",");
                        // 名称
                        String s = "";
                        if (columns.get("ent_name") != null) {
                            if (((String) columns.get("ent_name")).indexOf(",") > 0) {
                                s = "\"" + (String) columns.get("ent_name") + "\"";
                            } else {
                                s = (String) columns.get("ent_name");
                            }
                        } else {
                            s = "";
                        }
                        sb = sb.append(s).append(",");
                        if (columns.get("ent_address1") != null) {
                            s = (String) columns.get("ent_address1");
                        } else {
                            s = "";
                        }
                        sb = sb.append(s).append(",");
                        //現場コード
                        if (columns.get("id") != null) {
                            i = (Integer) columns.get("id");
                        } else {
                            i = 0;
                        }
                        sb = sb.append(i).append(",");
                        //現場名称
                        if (columns.get("name") != null) {
                            if (((String) columns.get("name")).indexOf(",") > 0) {
                                s = "\"" + (String) columns.get("name") + "\"";
                            } else {
                                s = (String) columns.get("name");
                            }
                        } else {
                            s = "";
                        }
                        sb = sb.append(s).append(",");
                        // 正式名称
                        if (columns.get("p_name") != null) {
                            if (((String) columns.get("p_name")).indexOf(",") > 0) {
                                s = "\"" + (String) columns.get("p_name") + "\"";
                            } else {
                                s = (String) columns.get("p_name");
                            }
                        } else {
                            s = "";
                        }
                        sb = sb.append(s).append(",");
                        // 所在地1
                        if (columns.get("address1") != null) {
                            s = (String) columns.get("address1");
                        } else {
                            s = "";
                        }
                        sb = sb.append(s).append(",");
                        // TEL
                        if (columns.get("tel") != null) {
                            s = (String) columns.get("tel");
                        } else {
                            s = "";
                        }
                        sb = sb.append(s).append(",");
                        //現場終了
                        if (columns.get("finish_flag") != null && (Boolean) columns.get("finish_flag")) {
                            s = "現場終了";
                        } else {
                            s = "";
                        }
                        sb = sb.append(s).append(",");
                        // 契約自動更新
                        if (columns.get("auto_update_flag") != null && (Boolean) columns.get("auto_update_flag")) {
                            s = "自動更新";
                        } else {
                            s = "";
                        }
                        sb = sb.append(s).append(",");
                        // 契約開始日
                        if (columns.get("contract_start_date") != null) {
                            s = (Date) columns.get("contract_start_date") != null ? sdf.format((Date) columns.get("contract_start_date")) : "";
                        } else {
                            s = "";
                        }
                        sb = sb.append(s).append(",");
                        // 契約終了日
                        if (columns.get("contract_end_date") != null) {
                            s = (Date) columns.get("contract_end_date") != null ? sdf.format((Date) columns.get("contract_end_date")) : "";
                        } else {
                            s = "";
                        }
                        sb = sb.append(s).append(",");
                        //契約状況
                        if (columns.get("contract_date") != null) {
                            s = (String) columns.get("contract_date");
                        } else {
                            s = "";
                        }
                        sb = sb.append(s).append(",");

                        //請求先ID
                        if (columns.get("bill_id") != null) {
                            i = (Integer) columns.get("bill_id");
                        } else {
                            i = 0;
                        }
                        sb = sb.append(i).append(",");
                        //請求先名
                        if (columns.get("bill_name") != null) {
                            if (((String) columns.get("bill_name")).indexOf(",") > 0) {
                                s = "\"" + (String) columns.get("bill_name") + "\"";
                            } else {
                                s = (String) columns.get("bill_name");
                            }
                        } else {
                            s = "";
                        }
                        sb = sb.append(s).append(",");
                        //備考
                        if (columns.get("remark") != null) {
                            if (((String) columns.get("remark")).indexOf(",") > 0) {
                                s = "\"" + (String) columns.get("remark") + "\"";
                            } else {
                                s = (String) columns.get("remark");
                            }
                        } else {
                            s = "";
                        }
                        sb = sb.append(s).append(",");

                        //trans_name01
                        if (columns.get("trans_name01") != null) {
                            s = (String) columns.get("trans_name01");
                        } else {
                            s = "";
                        }
                        sb = sb.append(s).append(",");
                        //trans_name01
                        if (columns.get("trans_name02") != null) {
                            s = (String) columns.get("trans_name02");
                        } else {
                            s = "";
                        }
                        sb = sb.append(s).append(",");
                        if (columns.get("trans_name03") != null) {
                            s = (String) columns.get("trans_name03");
                        } else {
                            s = "";
                        }
                        sb = sb.append(s);

                        out.write(sb.toString());
                        out.newLine();
                    }
                }
            } finally {
                out.close();
            }
        } catch (Exception e) {
            logger_.warning(e.toString());
        }
    }
}
