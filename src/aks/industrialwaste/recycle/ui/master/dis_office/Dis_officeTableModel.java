package aks.industrialwaste.recycle.ui.master.dis_office;

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

public class Dis_officeTableModel extends AbstractTableModel {

    public static final int COLUMN_COUNT = 13;
    public static final int COLUMN_ID = 0;
    public static final int COLUMN_NAME = 1;
    public static final int COLUMN_ADDRESS1 = 2;
    public static final int COLUMN_ADDRESS2 = 3;
    public static final int COLUMN_TEL = 4;
    public static final int COLUMN_KANA = 5;
    public static final int COLUMN_FINISH = 6;
    public static final int COLUMN_ENT_ID = 7;
    public static final int COLUMN_ENT_NAME = 8;
    public static final int COLUMN_ENT_ADDRESS1 = 9;
    public static final int COLUMN_START_DATE = 10;
    public static final int COLUMN_END_DATE = 11;
    public static final int COLUMN_AUTO_UPDATE_FLAG = 12;
    public static final int COLUMN_CONTRACT_DATE = 13;
    public static final int COLUMN_FILE_PATH = 14;
    //FILE_PATH
    private static final String[] COLUMN_NAMES = {
        "id",
        "name",
        "address1",
        "address2",
        "tel",
        "kana",
        "finish_flag",
        "ent_id",
        "ent_name",
        "ent_address1",
        "contract_start_date",
        "contract_end_date",
        "auto_update_flag",
        "contract_date",
        "keiyakusyo_path"

    };
    protected static Logger logger_
             = Logger.getLogger(Dis_officeTableModel.class.getName());
    private Connection con_;
    private List<Map<String, Object>> rows_
             = Collections.synchronizedList(new ArrayList<Map<String, Object>>());

    public Dis_officeTableModel(Connection con) {
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
                String s = new StringBuilder().append("insert into m_discharge_office")
                         .append("(id, name, address1, address2, p_name, ")
                         .append("zip_code, tel, kana, finish_flag)")
                         .append("values(?, ?, ?, ?, ?, ?, ?, ?, ?,")
                         .append("?, ?, ?, ?, ?, ?, ?, ?, ?);")
                         .toString();
                PreparedStatement ps = con_.prepareStatement(s);
                try {
                    ps.setObject(1, newdata.get("id"));
                    ps.setObject(2, newdata.get("name"));
                    ps.setObject(3, newdata.get("address1"));
                    ps.setObject(4, newdata.get("address2"));
                    ps.setObject(5, newdata.get("p_name"));
                    ps.setObject(6, newdata.get("zip_code"));
                    ps.setObject(7, newdata.get("tel"));
                    ps.setObject(8, newdata.get("kana"));
                    ps.setObject(9, newdata.get("finish_flag"));

                    ps.setObject(10, newdata.get("goods_id01"));
                    ps.setObject(11, newdata.get("goods_id02"));
                    ps.setObject(12, newdata.get("goods_id03"));
                    ps.setObject(13, newdata.get("goods_id04"));
                    ps.setObject(14, newdata.get("goods_id05"));
                    ps.setObject(15, newdata.get("goods_id06"));
                    ps.setObject(16, newdata.get("goods_id07"));
                    ps.setObject(17, newdata.get("goods_id08"));
                    ps.setObject(18, newdata.get("goods_id09"));
                    ps.setObject(19, newdata.get("goods_id10"));

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
                String s = new StringBuilder().append("update m_discharge_office set ")
                         .append("name = ?, address1 = ?, ")
                         .append("address2 = ?, p_name = ?, ")
                         .append("zip_code = ?, tel = ?, kana = ? ,")
                         .append("finish_flag = ?,  id = ?, ")
                         .append("goods_id01 = ?,  goods_id02 = ?, ")
                         .append("goods_id03 = ?,  goods_id04 = ?, ")
                         .append("goods_id05 = ?,  goods_id06 = ?, ")
                         .append("goods_id07 = ?,  goods_id08 = ?, ")
                         .append("goods_id09 = ?,  goods_id10 = ?  ")
                         .append("where sno = ?;")
                         .toString();
                PreparedStatement ps = con_.prepareStatement(s);
                try {
                    ps.setObject(1, updata.get("name"));
                    ps.setObject(2, updata.get("address1"));
                    ps.setObject(3, updata.get("address2"));
                    ps.setObject(4, updata.get("p_name"));
                    ps.setObject(5, updata.get("zip_code"));
                    ps.setObject(6, updata.get("tel"));
                    ps.setObject(7, updata.get("kana"));
                    ps.setObject(8, updata.get("finish_flag"));
                    ps.setObject(9, updata.get("id"));

                    ps.setObject(10, updata.get("goods_id01"));
                    ps.setObject(11, updata.get("goods_id02"));
                    ps.setObject(12, updata.get("goods_id03"));
                    ps.setObject(13, updata.get("goods_id04"));
                    ps.setObject(14, updata.get("goods_id05"));
                    ps.setObject(15, updata.get("goods_id06"));
                    ps.setObject(16, updata.get("goods_id07"));
                    ps.setObject(17, updata.get("goods_id08"));
                    ps.setObject(18, updata.get("goods_id09"));
                    ps.setObject(19, updata.get("goods_id10"));

                    ps.setObject(20, updata.get("sno"));
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
                         .append("update m_discharge_office ")
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
                         "ＩＤ, 名称, 正式名称, 所在地1, 所在地2, TEL, 現場終了フラグ");
                out.newLine();
                synchronized (rows_) {
                    for (Map<String, Object> columns : rows_) {
                        // id
                        Integer i = (Integer) columns.get("id");
                        StringBuilder sb = new StringBuilder().append(i).append(",");
                        // 名称
                        String s = (String) columns.get("name");
                        sb = sb.append(s).append(",");
                        // 正式名称
                        s = (String) columns.get("p_name");
                        sb = sb.append(s).append(",");
                        // 所在地1
                        s = (String) columns.get("address1");
                        sb = sb.append(s).append(",");
                        // 所在地2
                        s = (String) columns.get("address2");
                        sb = sb.append(s).append(",");
                        // TEL
                        s = (String) columns.get("tel");
                        sb = sb.append(s).append(",");
                        // 現場終了
                        if ((Boolean) columns.get("finish_flag")) {
                            s = "終了";
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
