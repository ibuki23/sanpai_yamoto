package aks.industrialwaste.recycle.ui.weighlist;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import javax.swing.table.AbstractTableModel;
import aks.industrialwaste.database.DBManager;
import aks.industrialwaste.database.SearchCondition;
import aks.industrialwaste.database.SortCondition;

public class GateInListTableModel extends AbstractTableModel {

    public static final int COLUMN_COUNT = 11;
    public static final int COLUMN_SNO = 0;
    public static final int COLUMN_CAR_NO = 1;
    public static final int COLUMN_REJISTER_WEIGHT = 2;
    public static final int COLUMN_TIME = 3;
    public static final int COLUMN_DIS_ENT = 4;
    public static final int COLUMN_TRANSPORTER = 5;
    public static final int COLUMN_KIND = 6;
    public static final int COLUMN_DIS_OFFICE = 7;
    public static final int COLUMN_CONTRACTORY = 8;
    public static final int COLUMN_BILL = 9;
    public static final int COLUMN_CAR_ID = 10;
    private static final String[] COLUMN_NAMES = {
        "sno",
        "car_no",
        "register_weight",
        "register_date",
        "entrepreneur_name",
        "transporter_name",
        "goods_name",
        "office_name",
        "contractor_name",
        "bill_name",
        "car_id",
        "office_sub_name",
        "dis_ent_id",
        "trans_id",
        "goods_id",
        "dis_office_id",
        "dis_cont_id",
        "car_kind_name",
        "bill_id",
        "unit_value",
        "charge_value"

    };

    protected static Logger logger_
             = Logger.getLogger(GateInListTableModel.class.getName());
    private Connection con_;
    private List<Map<String, Object>> rows_
             = Collections.synchronizedList(new ArrayList<Map<String, Object>>());

    public GateInListTableModel(Connection con) {
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
                              "v1_gate_in_data",
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
                         .append("insert into gate_in_data")
                         .append("(register_date, ")
                         .append("register_weight, ")
                         .append("car_no, ")
                         .append("dis_ent_id, ")
                         .append("trans_id, ")
                         .append("goods_id, ")
                         .append("dis_office_id, ")
                         .append("dis_cont_id, ")
                         .append("bill_id, ")
                         .append("car_kind_id, office_sub_name, car_id, manifesto_no1, manifesto_no2, carry_class) ")
                         .append("values(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);")
                         .toString();
                PreparedStatement ps = con_.prepareStatement(s);
                try {
                    ps.setObject(1, DBManager.convert((Calendar) newdata.get("register_date")));
                    ps.setObject(2, newdata.get("register_weight"));
                    ps.setObject(3, newdata.get("car_no"));
                    ps.setObject(4, newdata.get("dis_ent_id"));
                    ps.setObject(5, newdata.get("trans_id"));
                    ps.setObject(6, newdata.get("goods_id"));
                    ps.setObject(7, newdata.get("dis_office_id"));
                    ps.setObject(8, newdata.get("dis_cont_id"));
                    ps.setObject(9, newdata.get("bill_id"));
                    ps.setObject(10, newdata.get("car_kind_id"));
                    ps.setObject(11, newdata.get("office_sub_name"));
                    ps.setObject(12, newdata.get("car_no"));
                    ps.setObject(13, newdata.get("manifesto_no1"));
                    ps.setObject(14, newdata.get("manifesto_no2"));
                    ps.setObject(15, newdata.get("carry_class"));

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
                         .append("update gate_in_data ")
                         .append("set register_date = ?, ")
                         .append("register_weight = ?, ")
                         .append("car_no = ?, ")
                         .append("dis_ent_id = ?, ")
                         .append("trans_id = ?, ")
                         .append("goods_id = ?, ")
                         .append("dis_office_id = ?, ")
                         .append("dis_cont_id = ?, ")
                         .append("bill_id = ?, ")
                         .append("car_kind_id = ?, office_sub_name = ?, ")
                         .append("manifesto_no1 = ?, manifesto_no2 = ?, carry_class = ? ")
                         .append("where sno = ?;")
                         .toString();
                PreparedStatement ps = con_.prepareStatement(s);
                try {
                    ps.setObject(1, DBManager.convert((Calendar) updata.get("register_date")));
                    ps.setObject(2, updata.get("register_weight"));
                    ps.setObject(3, updata.get("car_no"));
                    ps.setObject(4, updata.get("dis_ent_id"));
                    ps.setObject(5, updata.get("trans_id"));
                    ps.setObject(6, updata.get("goods_id"));
                    ps.setObject(7, updata.get("dis_office_id"));
                    ps.setObject(8, updata.get("dis_cont_id"));
                    ps.setObject(9, updata.get("bill_id"));
                    ps.setObject(10, updata.get("car_kind_id"));
                    ps.setObject(11, updata.get("office_sub_name"));
                    ps.setObject(12, updata.get("manifesto_no1"));
                    ps.setObject(13, updata.get("manifesto_no2"));
                    ps.setObject(14, updata.get("carry_class"));
                    ps.setObject(15, updata.get("sno"));

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
                         .append("update gate_in_data ")
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
}
