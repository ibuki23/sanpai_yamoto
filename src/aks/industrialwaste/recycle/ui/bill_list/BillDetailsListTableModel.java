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

public class BillDetailsListTableModel extends AbstractTableModel {

    public static final int COLUMN_COUNT = 27;
    public static final int COLUMN_SNO = 0;
    public static final int COLUMN_BILL_SNO = 1;
    public static final int COLUMN_BILL_ID = 2;
    public static final int COLUMN_M_BILL_NAME = 3;
    public static final int COLUMN_GOODS_ID = 4;
    public static final int COLUMN_GOODS_NAME = 5;
    public static final int COLUMN_TERM_MONTH = 6;
    public static final int COLUMN_TERM_DATE = 7;
    public static final int COLUMN_BILL_DATE = 8;
    public static final int COLUMN_BILL_VALUE = 9;
    public static final int COLUMN_UNIT_PRICE = 10;
    public static final int COLUMN_TRANS_UNIT_VALUE = 11;
    public static final int COLUMN_BILL_PRICE = 12;
    public static final int COLUMN_REMARK01 = 13;
    public static final int COLUMN_REMARK02 = 14;
    public static final int COLUMN_BILL_NAME = 15;
    public static final int COLUMN_BILL_NAME2 = 16;
    public static final int COLUMN_BILL_UNIT = 17;
    public static final int COLUMN_B_CLASS_ID = 18;
    public static final int COLUMN_B_CLASS_NAME = 19;
    public static final int COLUMN_OFFICE_ID = 20;
    public static final int COLUMN_OFFICE_NAME = 21;
    public static final int COLUMN_OFFICE_NAME2 = 22;
    public static final int COLUMN_CAR_NO = 23;
    public static final int COLUMN_CONT_OFFICE_NAME = 24;
    public static final int COLUMN_ADVANCES = 25;
    public static final int COLUMN_BILL_FLAG = 26;
    public static final int COLUMN_EDA = 27;
    private static final String[] COLUMN_NAMES = {
        "sno",
        "bill_sno",
        "bill_id",
        "m_bill_name",
        "goods_id",
        "goods_name",//5
        "term_month",
        "term_date",
        "bill_date",
        "bill_value",
        "unit_price",//10
        "trans_unit_value",
        "bill_price",
        "remark01",
        "remark02",
        "bill_name",//15
        "bill_name2",
        "bill_unit",
        "b_class_id",
        "b_class_name",
        "dis_office_id",//20
        "office_name",
        "office_name2",
        "car_no",
        "cont_office_name",
        "advances",
        "bill_flag",
        "eda_office_id"};

    protected static Logger logger_
             = Logger.getLogger(BillDetailsListTableModel.class.getName());
    private Connection con_;
    private List<Map<String, Object>> rows_
             = Collections.synchronizedList(new ArrayList<Map<String, Object>>());

    public BillDetailsListTableModel(Connection con) {
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
                              "v1_d_bill_detail",
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
                         .append("insert into d_bill_detail ")
                         .append("(bill_sno, ")
                         .append("bill_id, ")
                         .append("goods_id, ")
                         .append("term_month, ")
                         .append("term_date, ")
                         .append("bill_date, ")
                         .append("bill_value, ")
                         .append("unit_price, ")
                         .append("advances, ")
                         .append("bill_price, ")
                         .append("remark01, ")
                         .append("remark02, ")
                         .append("bill_name, ")
                         .append("bill_unit, ")
                         .append("b_class_id, ")
                         .append("dis_office_id, ")
                         .append("office_name, ")
                         .append("office_name2, carry_over_flag, car_no, eda_office_id, weight_sno, ")
                         .append("start_date, end_date) ")
                         .append("values(?, ?, ?, date_trunc('month', check_end_date(?, ?)), ")
                         .append("?, check_end_date(?, ?), ?, ?, ?, ?, ?, ")
                         .append("?, ?, ?, ?, ?, ?, ?, true, ?, ?, ?, ?, ?);")
                         .toString();
                PreparedStatement ps = con_.prepareStatement(s);
                try {

                    ps.setObject(1, newdata.get("bill_sno"));
                    ps.setObject(2, newdata.get("bill_id"));
                    ps.setObject(3, newdata.get("goods_id"));
                    ps.setObject(4, newdata.get("term_month"));
                    ps.setObject(5, newdata.get("bill_id"));
                    ps.setObject(6, newdata.get("term_date"));
                    ps.setObject(7, newdata.get("term_date"));
                    ps.setObject(8, newdata.get("bill_id"));
                    ps.setObject(9, newdata.get("bill_value"));
                    ps.setObject(10, newdata.get("unit_price"));
                    ps.setObject(11, newdata.get("advances"));
                    ps.setObject(12, newdata.get("bill_price"));
                    ps.setObject(13, newdata.get("remark01"));
                    ps.setObject(14, newdata.get("remark02"));
                    ps.setObject(15, newdata.get("bill_name"));
                    ps.setObject(16, newdata.get("bill_unit"));
                    ps.setObject(17, newdata.get("b_class_id"));
                    ps.setObject(18, newdata.get("dis_office_id"));
                    ps.setObject(19, newdata.get("office_name"));
                    ps.setObject(20, newdata.get("office_name2"));
                    ps.setObject(21, newdata.get("car_no"));
                    ps.setObject(22, newdata.get("eda_office_id"));
                    ps.setObject(23, newdata.get("weight_sno"));
                    ps.setObject(24, newdata.get("start_date"));
                    ps.setObject(25, newdata.get("end_date"));

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
                         .append("update d_bill_detail set ")
                         .append("term_month = ?, ")
                         .append("goods_id = ?, ")
                         .append("bill_value = ?, ")
                         .append("unit_price = ?, ")
                         .append("bill_price = ?, ")
                         .append("remark01 = ?, ")
                         .append("remark02 = ?, ")
                         .append("bill_name = ?, ")
                         .append("bill_unit = ?, ")
                         .append("b_class_id = ?, ")
                         .append("dis_office_id = ?, ")
                         .append("office_name = ?, ")
                         .append("office_name2 = ?, ")
                         .append("advances = ?, ")
                         .append("car_no = ?, ")
                         .append("eda_office_id = ?, ")
                         .append("carry_over_flag = true ")
                         .append("where sno = ?;")
                         .toString();

                PreparedStatement ps = con_.prepareStatement(s);
                try {
                    ps.setObject(1, updata.get("term_month"));
                    ps.setObject(2, updata.get("goods_id"));
                    ps.setObject(3, updata.get("bill_value"));
                    ps.setObject(4, updata.get("unit_price"));
                    ps.setObject(5, updata.get("bill_price"));
                    ps.setObject(6, updata.get("remark01"));
                    ps.setObject(7, updata.get("remark02"));
                    ps.setObject(8, updata.get("bill_name"));
                    ps.setObject(9, updata.get("bill_unit"));
                    ps.setObject(10, updata.get("b_class_id"));
                    ps.setObject(11, updata.get("dis_office_id"));
                    ps.setObject(12, updata.get("office_name"));
                    ps.setObject(13, updata.get("office_name2"));
                    ps.setObject(14, updata.get("advances"));
                    ps.setObject(15, updata.get("car_no"));
                    ps.setObject(16, updata.get("eda_office_id"));
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
                         .append("update d_bill_detail set ")
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

    public boolean switching(Map<String, Object> switching) {
        try {
            Statement st = con_.createStatement();
            st.executeUpdate("begin;");
            try {
                String s = new StringBuilder()
                         .append("update d_bill_detail set ")
                         .append("bill_flag = ? ")
                         .append("where sno = ?;")
                         .toString();

                PreparedStatement ps = con_.prepareStatement(s);
                try {
                    if (switching.get("bill_flag") != null) {
                        ps.setObject(1, !(Boolean) switching.get("bill_flag"));
                    } else {
                        ps.setObject(1, true);
                    }
                    ps.setObject(2, switching.get("sno"));
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

    public boolean insert2(Map<String, Object> newdata2, int value) {
        try {
            Statement st = con_.createStatement();
            st.executeUpdate("begin;");
            try {
                String s = new StringBuilder()
                         .append("insert into d_bill_detail ")
                         .append("(bill_sno, ")
                         .append("bill_id, ")
                         .append("goods_id, ")
                         .append("term_month, ")
                         .append("term_date, ")
                         .append("bill_date, ")
                         .append("bill_value, ")
                         .append("unit_price, ")
                         .append("trans_unit_value, ")
                         .append("bill_price, ")
                         .append("remark01, ")
                         .append("remark02, ")
                         .append("bill_name, ")
                         .append("bill_unit, ")
                         .append("b_class_id, ")
                         .append("dis_office_id, ")
                         .append("office_name, ")
                         .append("office_name2) ")
                         .append("values(?, ?, ?, date_trunc('month', check_end_date(?, ?)), ")
                         .append("?, check_end_date(?, ?), ?, ?, ?, ?, ?, ")
                         .append("?, ?, ?, ?, ?, ?, ?);")
                         .toString();
                PreparedStatement ps = con_.prepareStatement(s);
                try {

                    ps.setObject(1, newdata2.get("bill_sno"));
                    ps.setObject(2, newdata2.get("bill_id"));
                    ps.setObject(3, 9999);
                    ps.setObject(4, newdata2.get("term_date"));
                    ps.setObject(5, newdata2.get("bill_id"));
                    ps.setObject(6, newdata2.get("term_date"));
                    ps.setObject(7, newdata2.get("term_date"));
                    ps.setObject(8, newdata2.get("bill_id"));
                    ps.setObject(9, 1);
                    ps.setObject(10, 0);
                    ps.setObject(11, 0);
                    ps.setObject(12, value);
                    ps.setObject(13, "");
                    ps.setObject(14, "");
                    ps.setObject(15, "値引き");
                    ps.setObject(16, "式");
                    ps.setObject(17, 10);
                    ps.setObject(18, newdata2.get("dis_office_id"));
                    ps.setObject(19, newdata2.get("office_name"));
                    ps.setObject(20, newdata2.get("office_name2"));

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
