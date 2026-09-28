package aks.industrialwaste.recycle.ui.master.contractory;

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
import aks.industrialwaste.database.DBManager;
import aks.industrialwaste.database.SearchCondition;
import aks.industrialwaste.database.SortCondition;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;

public class ContractoryTableModel extends AbstractTableModel {

    public static final int COLUMN_COUNT = 10;
    public static final int COLUMN_ID = 0;
    public static final int COLUMN_NAME = 1;
    public static final int COLUMN_ADDRESS1 = 2;
    public static final int COLUMN_ADDRESS2 = 3;
    public static final int COLUMN_CON_TEL = 4;
    public static final int COLUMN_CON_P_NAME = 5;
    public static final int COLUMN_OFFICE_NAME = 6;
    public static final int COLUMN_KANA = 7;
    public static final int COLUMN_PERMISSION_NO = 8;
    public static final int COLUMN_CONT_TANTOU = 9;
    public static final int COLUMN_LAST_TANTOU = 10;
    private static final String[] COLUMN_NAMES
             = {"id", "name", "con_address1", "con_address2", "con_tel", "con_p_name",
                 "office_name", "kana", "permission_no", "cont_tantou", "last_tantou"};

    protected static Logger logger_
             = Logger.getLogger(ContractoryTableModel.class.getName());
    private Connection con_;
    private List<Map<String, Object>> rows_
             = Collections.synchronizedList(new ArrayList<Map<String, Object>>());

    public ContractoryTableModel(Connection con) {
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

    public Map<String, Object> getColumns(int row) {
        return rows_.get(row);
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
                              "v0_cont",
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
                                columns.put(s, rs.getInt(i));
                                break;
                            case Types.DOUBLE:
                                columns.put(s, rs.getDouble(i));
                                break;
                            case Types.BOOLEAN:
                                columns.put(s, rs.getBoolean(i));
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

    public Connection getConnection() {
        return con_;
    }

    public boolean insert(Map<String, Object> newdata) {
        try {
            Statement st = con_.createStatement();
            st.executeUpdate("begin;");
            try {
                String s = new StringBuilder().append("insert into m_disposal_contractor")
                         .append("(id, name, con_address1, ")
                         .append("con_address2, con_p_name, con_zip_code, con_tel, ")
                         .append("office_address1, office_address2, ")
                         .append("office_p_name, office_zip_code, office_tel, kana, permission_no, cont_tantou, last_tantou) ")
                         .append("values(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);").toString();
                PreparedStatement ps = con_.prepareStatement(s);
                try {
                    ps.setObject(1, newdata.get("id"));
                    ps.setObject(2, newdata.get("name"));
                    ps.setObject(3, newdata.get("con_address1"));
                    ps.setObject(4, newdata.get("con_address2"));
                    ps.setObject(5, newdata.get("con_p_name"));
                    ps.setObject(6, newdata.get("con_zip_code"));
                    ps.setObject(7, newdata.get("con_tel"));
                    ps.setObject(8, newdata.get("office_address1"));
                    ps.setObject(9, newdata.get("office_address2"));
                    ps.setObject(10, newdata.get("office_p_name"));
                    ps.setObject(11, newdata.get("office_zip_code"));
                    ps.setObject(12, newdata.get("office_tel"));
                    ps.setObject(13, newdata.get("kana"));
                    ps.setObject(14, newdata.get("permission_no"));
                    ps.setObject(15, newdata.get("cont_tantou"));
                    ps.setObject(16, newdata.get("last_tantou"));
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

    public boolean update(Map<String, Object> updata) {
        try {
            Statement st = con_.createStatement();
            st.executeUpdate("begin;");
            try {
                String s = new StringBuilder()
                         .append("update m_disposal_contractor set ")
                         .append("name = ?, con_address1 = ?, ")
                         .append("con_address2 = ?, con_p_name = ?, ")
                         .append("con_zip_code = ?, con_tel = ?, office_address1 = ?, ")
                         .append("office_address2 = ?, office_p_name = ?, office_zip_code = ?, ")
                         .append("office_tel = ?, kana = ?, id = ? , permission_no = ?, cont_tantou = ?, last_tantou = ? ")
                         .append("where sno = ?;")
                         .toString();
                PreparedStatement ps = con_.prepareStatement(s);
                try {
                    ps.setObject(1, updata.get("name"));
                    ps.setObject(2, updata.get("con_address1"));
                    ps.setObject(3, updata.get("con_address2"));
                    ps.setObject(4, updata.get("con_p_name"));
                    ps.setObject(5, updata.get("con_zip_code"));
                    ps.setObject(6, updata.get("con_tel"));
                    ps.setObject(7, updata.get("office_address1"));
                    ps.setObject(8, updata.get("office_address2"));
                    ps.setObject(9, updata.get("office_p_name"));
                    ps.setObject(10, updata.get("office_zip_code"));
                    ps.setObject(11, updata.get("office_tel"));
                    ps.setObject(12, updata.get("kana"));
                    ps.setObject(13, updata.get("id"));
                    ps.setObject(14, updata.get("permission_no"));
                    ps.setObject(15, updata.get("cont_tantou"));
                    ps.setObject(16, updata.get("last_tantou"));
                    ps.setObject(17, updata.get("sno"));
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

    public boolean delete(int row) {
        try {
            Map<String, Object> columns = getColumns(row);

            Statement st = con_.createStatement();
            st.executeUpdate("begin;");
            try {
                String s = new StringBuilder()
                         .append("update m_disposal_contractor ")
                         .append("set delete_flag = true ")
                         .append("where sno = ?;")
                         .toString();
                PreparedStatement ps = con_.prepareStatement(s);
                try {
                    ps.setObject(1, columns.get("sno"));
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

    public void report(File f) {
        try {
            BufferedWriter out = new BufferedWriter(
                     new OutputStreamWriter(
                              new FileOutputStream(f), "MS932"));
            try {
                // タイトル
                out.write(
                         "処分業者コード,処分業者名称,業者所在地1,業者所在地2,業者正式名称,業者郵便番号,業者TEL,許可番号,処分担当者,最終処分担当者");
                out.newLine();
                synchronized (rows_) {
                    for (Map<String, Object> columns : rows_) {
                        // id
                        Integer i = (Integer) columns.get("id");
                        StringBuilder sb = new StringBuilder().append(i).append(",");
                        // 名称
                        String s = (String) columns.get("name");
                        sb = sb.append(s).append(",");
                        // 業者所在地1
                        s = (String) columns.get("con_address1");
                        sb = sb.append(s).append(",");
                        // 業者所在地2
                        s = (String) columns.get("con_address2");
                        sb = sb.append(s).append(",");
                        // 業者正式名称
                        s = (String) columns.get("con_p_name");
                        sb = sb.append(s).append(",");
                        // 業者郵便番号
                        s = (String) columns.get("con_zip_code");
                        sb = sb.append(s).append(",");
                        // 業者TEL
                        s = (String) columns.get("con_tel");
                        sb = sb.append(s).append(",");
                        // 許可番号
                        s = (String) columns.get("permission_no");
                        sb = sb.append(s).append(",");
                        // 処分担当者 
                        s = (String) columns.get("cont_tantou");
                        sb = sb.append(s).append(",");
                        // 最終処分担当者 20151019 追加
                        s = (String) columns.get("last_tantou");
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
                    ps.setObject(1, "m_disposal_contractor");
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

        try {
            Statement st = con_.createStatement();
            st.executeUpdate("begin;");
            st.executeUpdate("delete from m_disposal_contractor;");
            try {
                String s = new StringBuilder()
                         .append("SET CLIENT_ENCODING TO 'UTF8';\n COPY m_disposal_contractor from E'C:\\\\csv\\\\master_csv\\\\m_disposal_contractor.csv' CSV;").toString();
                PreparedStatement ps = con_.prepareStatement(s);
                try {
                    ps.executeUpdate();
                } finally {
                    ps.close();
                }

                st.executeUpdate("commit;");

                s = new StringBuilder()
                         .append("SELECT setval('m_disposal_contractor_sno_seq', (select max(sno) from m_disposal_contractor)::bigint); ").toString();
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

        try {
            Statement st = con_.createStatement();
            st.executeUpdate("begin;");
            st.executeUpdate("delete from m_disposal_contractor;");
            try {
                String s = new StringBuilder()
                         .append("SET CLIENT_ENCODING TO 'UTF8';\n COPY m_disposal_contractor from E'C:\\\\csv\\\\master_csv\\\\m_disposal_contractor.csv' CSV;").toString();
                PreparedStatement ps = con_.prepareStatement(s);
                try {
                    ps.executeUpdate();
                } finally {
                    ps.close();
                }

                st.executeUpdate("commit;");

                s = new StringBuilder()
                         .append("SELECT setval('m_disposal_contractor_sno_seq', (select max(sno) from m_disposal_contractor)::bigint); ").toString();
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
                         .append("SET CLIENT_ENCODING TO 'UTF8';\n COPY m_disposal_contractor to E'C:\\\\csv\\\\satou\\\\m_disposal_contractor.csv' CSV;")
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
}
