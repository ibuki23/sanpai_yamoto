package aks.industrialwaste.recycle.ui.payment;

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

public class PaymentDetailsListTableModel extends AbstractTableModel {

    public static final int COLUMN_COUNT = 12;
    public static final int COLUMN_SNO = 0;
    public static final int COLUMN_BILL_SNO = 1;
    public static final int COLUMN_BILL_ID = 2;
    public static final int COLUMN_M_BILL_NAME = 3;
    public static final int COLUMN_TERM_MONTH = 4;
    public static final int COLUMN_BILL_DATE = 5;
    public static final int COLUMN_PAYMENT_DATE = 6;
    public static final int COLUMN_P_CLASS_ID = 7;
    public static final int COLUMN_P_CLASS_NAME = 8;
    public static final int COLUMN_PAYMENT_PRICE = 9;
    public static final int COLUMN_COMMISSION = 10;
    public static final int COLUMN_REMARK01 = 11;
    public static final int COLUMN_REMARK02 = 12;

    private static final String[] COLUMN_NAMES = {
        "sno",
        "bill_sno",
        "bill_id",
        "m_bill_name",
        "term_month",
        "bill_date",//5
        "payment_date",
        "p_class_id",
        "p_class_name",
        "payment_price",
        "commission",
        "remark01",//10
        "remark02"};

    protected static Logger logger_
             = Logger.getLogger(PaymentDetailsListTableModel.class.getName());
    private Connection con_;
    private List<Map<String, Object>> rows_
             = Collections.synchronizedList(new ArrayList<Map<String, Object>>());

    public PaymentDetailsListTableModel(Connection con) {
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
                              "v1_d_bill_payment_detail",
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
                String s = new StringBuilder()
                         .append("insert into d_bill_payment_detail ")
                         .append("(bill_id, ")
                         .append("payment_date, ")
                         .append("p_class_id, ")
                         .append("payment_price, ")
                         .append("commission, ")
                         .append("remark01, ")
                         .append("remark02) ")
                         .append("values(?, ?, ?, ?, ?, ?, ?); ")
                         .toString();
                PreparedStatement ps = con_.prepareStatement(s);
                try {

                    ps.setObject(1, newdata.get("bill_id"));
                    ps.setObject(2, newdata.get("payment_date"));
                    ps.setObject(3, newdata.get("p_class_id"));
                    ps.setObject(4, newdata.get("payment_price"));
                    ps.setObject(5, newdata.get("commission"));
                    ps.setObject(6, newdata.get("remark01"));
                    ps.setObject(7, newdata.get("remark02"));

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
                         .append("update d_bill_payment_detail set ")
                         .append("bill_id = ?, ")
                         .append("payment_date = ?, ")
                         .append("p_class_id = ?, ")
                         .append("payment_price = ?, ")
                         .append("commission = ?, ")
                         .append("remark01 = ?, ")
                         .append("remark02 = ? ")
                         .append("where sno = ?;")
                         .toString();

                PreparedStatement ps = con_.prepareStatement(s);
                try {
                    ps.setObject(1, updata.get("bill_id"));
                    ps.setObject(2, updata.get("payment_date"));

                    ps.setObject(3, updata.get("p_class_id"));
                    ps.setObject(4, updata.get("payment_price"));
                    ps.setObject(5, updata.get("commission"));
                    ps.setObject(6, updata.get("remark01"));
                    ps.setObject(7, updata.get("remark02"));
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
                         .append("update d_bill_payment_detail set ")
                         .append("delete_flag = true ")
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
}
