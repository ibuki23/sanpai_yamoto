package aks.industrialwaste.recycle.ui.bill_list;

import aks.industrialwaste.database.DBManager;
import aks.industrialwaste.database.SearchCondition;
import aks.industrialwaste.database.SortCondition;
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

public class BillListTableModel extends AbstractTableModel {

    public static final int COLUMN_COUNT = 27;
    public static final int COLUMN_SNO = 0;
    public static final int COLUMN_BILL_ID = 1;
    public static final int COLUMN_BILL_NAME = 2;
    public static final int COLUMN_TERM_DATE = 3;
    public static final int COLUMN_TERM_MONTH = 4;
    public static final int COLUMN_TOTAL = 5;
    public static final int COLUMN_VOLUE1 = 6;
    public static final int COLUMN_VOLUE2 = 7;
    public static final int COLUMN_VOLUE3 = 8;
    public static final int COLUMN_VOLUE4 = 9;
    public static final int COLUMN_VOLUE5 = 10;
    public static final int COLUMN_VOLUE6 = 11;
    public static final int COLUMN_VOLUE7 = 12;
    public static final int COLUMN_VOLUE8 = 13;
    public static final int COLUMN_REMARK1 = 14;
    public static final int COLUMN_REMARK2 = 15;
    public static final int COLUMN_REMARK3 = 16;
    public static final int COLUMN_REMARK4 = 17;
    public static final int COLUMN_REMARK5 = 18;
    public static final int COLUMN_REMARK6 = 19;
    public static final int COLUMN_REMARK7 = 20;
    public static final int COLUMN_REMARK8 = 21;
    public static final int COLUMN_REPORT_NO = 22;
    public static final int COLUMN_VOLUE9 = 23;
    public static final int COLUMN_REMARK9 = 24;
    public static final int COLUMN_START_DATE = 25;
    public static final int COLUMN_END_DATE = 26;
    private static final String[] COLUMN_NAMES = {
        "sno",
        "bill_id",
        "bill_name",
        "term_date",
        "term_month",
        "total_value",
        "value_1",
        "value_2",
        "value_3",
        "value_4",
        "value_5",
        "value_6",
        "value_7",
        "value_8",
        "remark_1",
        "remark_2",
        "remark_3",
        "remark_4",
        "remark_5",
        "remark_6",
        "remark_7",
        "remark_8",
        "bill_report_no",
        "value_9",
        "remark_9",
        "start_date",
        "end_date"};

    protected static Logger logger_
             = Logger.getLogger(BillListTableModel.class.getName());
    private Connection con_;
    private List<Map<String, Object>> rows_
             = Collections.synchronizedList(new ArrayList<Map<String, Object>>());

    public BillListTableModel(Connection con) {
        con_ = con;
    }

    @Override
    public int getRowCount() {
        return rows_.size();
    }

    @Override
    public int getColumnCount() {
        return COLUMN_COUNT;
    }

    @Override
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
                              "v1_d_bill",
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

    public int update(Map<String, Object> updata) {
        try {
            Statement st = con_.createStatement();
            st.executeUpdate("begin;");
            try {
                String s = new StringBuilder()
                         .append("update d_bill set ")
                         .append("total_value = ?, ")
                         .append("value_1 = ?, ")
                         .append("value_2 = ?, ")
                         .append("value_3 = ?, ")
                         .append("value_4 = ?, ")
                         .append("value_5 = ?, ")
                         .append("value_6 = ?, ")
                         .append("value_7 = ?, ")
                         .append("value_8 = ?, ")
                         .append("value_9 = ?, ")
                         .append("remark_1 = ?, ")
                         .append("remark_2 = ?, ")
                         .append("remark_3 = ?, ")
                         .append("remark_4 = ?, ")
                         .append("remark_5 = ?, ")
                         .append("remark_6 = ?, ")
                         .append("remark_7 = ?, ")
                         .append("remark_8 = ?, ")
                         .append("remark_9 = ?, ")
                         .append("bill_report_no = ?, ")
                         .append("term_date = ? ")
                         .append("where sno = ?;")
                         .toString();
                PreparedStatement ps = con_.prepareStatement(s);
                try {
                    ps.setObject(1, updata.get("total_value"));
                    ps.setObject(2, updata.get("value_1"));
                    ps.setObject(3, updata.get("value_2"));
                    ps.setObject(4, updata.get("value_3"));
                    ps.setObject(5, updata.get("value_4"));
                    ps.setObject(6, updata.get("value_5"));
                    ps.setObject(7, updata.get("value_6"));
                    ps.setObject(8, updata.get("value_7"));
                    ps.setObject(9, updata.get("value_8"));
                    ps.setObject(10, updata.get("value_9"));
                    ps.setObject(11, updata.get("remark_1"));
                    ps.setObject(12, updata.get("remark_2"));
                    ps.setObject(13, updata.get("remark_3"));
                    ps.setObject(14, updata.get("remark_4"));
                    ps.setObject(15, updata.get("remark_5"));
                    ps.setObject(16, updata.get("remark_6"));
                    ps.setObject(17, updata.get("remark_7"));
                    ps.setObject(18, updata.get("remark_8"));
                    ps.setObject(19, updata.get("remark_9"));
                    ps.setObject(20, updata.get("bill_report_no"));
                    ps.setObject(21, updata.get("term_date"));
                    ps.setObject(22, updata.get("sno"));
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
            return -1;
        }

        return 0;
    }

    public boolean delete(Long delete) {
        try {
            Statement st = con_.createStatement();
            st.executeUpdate("begin;");
            try {
                String s = new StringBuilder()
                         .append("delete from d_bill ")
                         .append("where sno = ?;")
                         .toString();
                PreparedStatement ps = con_.prepareStatement(s);
                try {
                    ps.setObject(1, delete);
                    ps.executeUpdate();

                    //明細削除
                    s = new StringBuilder()
                             .append("delete from d_bill_detail ")
                             .append("where bill_sno = ?;")
                             .toString();
                    ps = con_.prepareStatement(s);
                    ps.setObject(1, delete);
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

    public boolean bill_update(int row) {
        Map<String, Object> columns = getColumns(row);

        try {
            String s = "select update_bill_data02_in_date(?, ?, ?, ?, ?);";
            PreparedStatement ps = con_.prepareStatement(s);
            try {
                ps.setObject(1, (java.sql.Date) columns.get("term_month"));
                ps.setObject(2, Integer.valueOf(columns.get("bill_id").toString()));
                ps.setObject(3, Integer.valueOf(columns.get("sno").toString()));
                ps.setObject(4, (java.sql.Date) columns.get("start_date"));
                ps.setObject(5, (java.sql.Date) columns.get("end_date"));

                ps.executeQuery();
            } finally {
                ps.close();
            }

        } catch (Exception e) {
            logger_.warning(e.toString());
            return false;
        }

        return true;
    }
}
