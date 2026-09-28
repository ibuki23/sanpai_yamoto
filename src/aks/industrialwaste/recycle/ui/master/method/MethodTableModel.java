package aks.industrialwaste.recycle.ui.master.method;

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

public class MethodTableModel extends AbstractTableModel {

    public static final int COLUMN_COUNT = 6;
    public static final int COLUMN_ID = 0;
    public static final int COLUMN_NAME = 1;
    public static final int COLUMN_METHOD_NAME = 2;
    public static final int COLUMN_FINAL_METHOD_NAME = 3;
    public static final int COLUMN_GOODS_ID = 4;
    public static final int COLUMN_GOODS_NAME = 5;
    private static final String[] COLUMN_NAMES
             = {"id", "name", "method_name", "final_method_name", "goods_id", "goods_name"};

    protected static Logger logger_
             = Logger.getLogger(MethodTableModel.class.getName());
    private Connection con_;
    private List<Map<String, Object>> rows_
             = Collections.synchronizedList(new ArrayList<Map<String, Object>>());

    public MethodTableModel(Connection con) {
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
                              "v0_method",
                              "*",
                              (null == search) ? (new SearchCondition[0]) : (search),
                              (null == sort) ? (new SortCondition[0]) : (sort),
                              0);
            try {
                ResultSet rs = ps.executeQuery();
                ResultSetMetaData md = rs.getMetaData();
                while (rs.next()) {
                    Map<String, Object> columns = new HashMap<String, Object>();
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
                String s = new StringBuilder()
                         .append("insert into m_method")
                         .append("(id, name, method_name, final_method_name, goods_id) ")
                         .append("values(?, ?, ?, ?, ?);")
                         .toString();
                PreparedStatement ps = con_.prepareStatement(s);
                try {
                    ps.setObject(1, newdata.get("id"));
                    ps.setObject(2, newdata.get("name"));
                    ps.setObject(3, newdata.get("method_name"));
                    ps.setObject(4, newdata.get("final_method_name"));
                    ps.setObject(5, newdata.get("goods_id"));

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
                         .append("update m_method set ")
                         .append("id = ?, ")
                         .append("name = ?, ")
                         .append("method_name = ?, ")
                         .append("final_method_name = ?, ")
                         .append("goods_id = ? ")
                         .append("where sno = ?;")
                         .toString();
                PreparedStatement ps = con_.prepareStatement(s);
                try {
                    ps.setObject(1, updata.get("id"));
                    ps.setObject(2, updata.get("name"));
                    ps.setObject(3, updata.get("method_name"));
                    ps.setObject(4, updata.get("final_method_name"));
                    ps.setObject(5, updata.get("goods_id"));
                    ps.setObject(6, updata.get("sno"));
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
                         .append("update m_method ")
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
                         "ID,名称,正式名称");
                out.newLine();
                synchronized (rows_) {
                    for (Map<String, Object> columns : rows_) {
                        // id
                        String s = (String) columns.get("id");
                        StringBuilder sb = new StringBuilder().append(s).append(",");
                        // 名称
                        s = (String) columns.get("name");
                        sb = sb.append(s).append(",");
                        // 正式名称
                        s = (String) columns.get("official_name");
                        sb = sb.append(s);

                        // 搬入搬出区分
                        if ((Integer) columns.get("option1") == 0) {
                            s = "中間";
                            sb = sb.append(s);
                        } else {
                            s = "最終";
                            sb = sb.append(s);
                        }

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
