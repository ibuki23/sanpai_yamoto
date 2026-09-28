package aks.industrialwaste.recycle.ui.master.manifesto3;

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
import aks.industrialwaste.database.DBManager;
import aks.industrialwaste.database.SearchCondition;
import aks.industrialwaste.database.SortCondition;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;

public class Manifesto3TableModel extends AbstractTableModel {

    public static final int COLUMN_COUNT = 8;
    public static final int COLUMN_SNO = 0;
    public static final int COLUMN_NAME = 1;
    public static final int COLUMN_ADDRESS1 = 2;
    public static final int COLUMN_ADDRESS2 = 3;
    public static final int COLUMN_ZIP = 4;
    public static final int COLUMN_TEL = 5;
    public static final int COLUMN_CONT_ID = 6;
    public static final int COLUMN_CONT_NAME = 7;
    public static final int COLUMN_ID = 8;
    private static final String[] COLUMN_NAMES = {
        "sno",
        "name",
        "address1",
        "address2",
        "zip_code",
        "tel",
        "cont_id",
        "cont_name",
        "id"

    };
    protected static Logger logger_
             = Logger.getLogger(Dis_officeTableModel.class.getName());
    private Connection con_;
    private List<Map<String, Object>> rows_
             = Collections.synchronizedList(new ArrayList<Map<String, Object>>());

    public Manifesto3TableModel(Connection con) {
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
                              "v0_manifesto_3",
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
                String s = new StringBuilder().append("insert into m_manifesto_3 ")
                         .append("(name, address1, address2, zip_code, tel, cont_id, id)")
                         .append("values(?, ?, ?, ?, ?, ?, ?);")
                         .toString();
                PreparedStatement ps = con_.prepareStatement(s);
                try {
                    ps.setObject(1, newdata.get("name"));
                    ps.setObject(2, newdata.get("address1"));
                    ps.setObject(3, newdata.get("address2"));
                    ps.setObject(4, newdata.get("zip_code"));
                    ps.setObject(5, newdata.get("tel"));
                    ps.setObject(6, newdata.get("cont_id"));
                    ps.setObject(7, newdata.get("id"));

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
            ////////////////////////
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
                String s = new StringBuilder().append("update m_manifesto_3 set ")
                         .append("name = ?, address1 = ?, ")
                         .append("address2 = ?, zip_code = ?, ")
                         .append("tel = ?, cont_id = ?, id = ? ")
                         .append("where sno = ?;")
                         .toString();
                PreparedStatement ps = con_.prepareStatement(s);
                try {
                    ps.setObject(1, updata.get("name"));
                    ps.setObject(2, updata.get("address1"));
                    ps.setObject(3, updata.get("address2"));
                    ps.setObject(4, updata.get("zip_code"));
                    ps.setObject(5, updata.get("tel"));
                    ps.setObject(6, updata.get("cont_id"));
                    ps.setObject(7, updata.get("id"));

                    ps.setObject(8, updata.get("sno"));
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
                         .append("update m_manifesto_3 ")
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
                         "処分場ＩＤ,処分場名称,所在地1,所在地2,郵便番号,TEL,処分業者ＩＤ,処分業者名称");
                out.newLine();
                synchronized (rows_) {
                    for (Map<String, Object> columns : rows_) {
                        // id
                        Integer i = (Integer) columns.get("id");
                        StringBuilder sb = new StringBuilder().append(i).append(",");
                        // 名称
                        String s = (String) columns.get("name");
                        sb = sb.append(s).append(",");
                        // 所在地1
                        s = (String) columns.get("address1");
                        sb = sb.append(s).append(",");
                        // 所在地2
                        s = (String) columns.get("address2");
                        sb = sb.append(s).append(",");
                        // TEL
                        s = (String) columns.get("zip_code");
                        sb = sb.append(s).append(",");
                        // TEL
                        s = (String) columns.get("tel");
                        sb = sb.append(s).append(",");

                        // 処分業者コード
                        i = (Integer) columns.get("cont_id");
                        sb = sb.append(i).append(",");
                        // 処分業者名称
                        s = (String) columns.get("cont_name");
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
                    ps.setObject(1, "m_manifesto_3");
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
            st.executeUpdate("delete from m_manifesto_3;");
            try {
                String s = new StringBuilder()
                         .append("SET CLIENT_ENCODING TO 'UTF8';\n COPY m_manifesto_3 from E'C:\\\\csv\\\\master_csv\\\\m_manifesto_3.csv' CSV;")
                         .toString();
                PreparedStatement ps = con_.prepareStatement(s);
                try {
                    ps.executeUpdate();
                } finally {
                    ps.close();
                }

                st.executeUpdate("commit;");

                s = new StringBuilder()
                         .append("SELECT setval('m_manifesto_3_sno_seq', (select max(sno) from m_manifesto_3)::bigint); ")
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

        try {
            Statement st = con_.createStatement();
            st.executeUpdate("begin;");
            st.executeUpdate("delete from m_manifesto_3;");
            try {
                String s = new StringBuilder()
                         .append("SET CLIENT_ENCODING TO 'UTF8';\n COPY m_manifesto_3 from E'C:\\\\csv\\\\master_csv\\\\m_manifesto_3.csv' CSV;")
                         .toString();
                PreparedStatement ps = con_.prepareStatement(s);
                try {
                    ps.executeUpdate();
                } finally {
                    ps.close();
                }

                st.executeUpdate("commit;");

                s = new StringBuilder()
                         .append("SELECT setval('m_manifesto_3_sno_seq', (select max(sno) from m_manifesto_3)::bigint); ")
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
                         .append("SET CLIENT_ENCODING TO 'UTF8';\n COPY m_manifesto_3 to E'C:\\\\csv\\\\satou\\\\m_manifesto_3.csv' CSV;")
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
