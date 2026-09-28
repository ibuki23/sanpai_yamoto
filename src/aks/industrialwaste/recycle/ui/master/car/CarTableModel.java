package aks.industrialwaste.recycle.ui.master.car;

//import aks.industrialwaste.recycle.ui.master.gastype.*;
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

public class CarTableModel extends AbstractTableModel {

    public static final int COLUMN_COUNT = 19;
    public static final int COLUMN_CAR_NO = 0;
    public static final int COLUMN_DIS_ENT = 1;
    public static final int COLUMN_TRANSPORTER = 2;
    public static final int COLUMN_KIND = 3;
    public static final int COLUMN_DIS_OFFICE = 4;
    public static final int COLUMN_CONTRACTORY = 5;
    public static final int COLUMN_EMPTY_WEIGHT = 6;
    public static final int COLUMN_BILL = 7;
    public static final int COLUMN_BILL_NAME = 8;
    public static final int COLUMN_CAR_ID = 9;
    public static final int COLUMN_ENT_ID = 10;
    public static final int COLUMN_TRANS_ID = 11;
    public static final int COLUMN_GOODS_ID = 12;
    public static final int COLUMN_OFFICE_ID = 13;
    public static final int COLUMN_CONT_ID = 14;
    public static final int COLUMN_CAR_KIND_ID = 15;
    public static final int COLUMN_CAR_KIND_NAME = 16;
    public static final int COLUMN_MAX_WEIGHT = 17;
    public static final int COLUMN_OFFICE_SUB_NAME = 18;
    private static final String[] COLUMN_NAMES = {"car_no",
        "entrepreneur_name",
        "transporter_name",
        "goods_name",
        "office_name",
        "contractor_name",
        "empty_weight",
        "bill_id",
        "bill_name",
        "car_id",
        "dis_ent_id",
        "trans_id",
        "goods_id",
        "dis_office_id",
        "dis_cont_id",
        "car_kind_id",
        "car_kind_name",
        "max_weight",
        "office_sub_name"};
    protected static Logger logger_
             = Logger.getLogger(CarTableModel.class.getName());
    private Connection con_;
    private List<Map<String, Object>> rows_
             = Collections.synchronizedList(new ArrayList<Map<String, Object>>());

    public CarTableModel(Connection con) {
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
                              "v0_car",
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
//
                String s = new StringBuilder()
                         .append("insert into m_car_no")
                         .append("(car_no, dis_ent_id,")
                         .append("trans_id, goods_id, dis_office_id,")
                         .append(" dis_cont_id, kind_id, empty_weight, bill_id, car_id, max_weight)")
                         .append("values(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);")
                         .toString();
                PreparedStatement ps = con_.prepareStatement(s);
                try {
                    ps.setObject(1, newdata.get("car_no"));
                    ps.setObject(2, newdata.get("dis_ent_id"));
                    ps.setObject(3, newdata.get("trans_id"));
                    ps.setObject(4, newdata.get("goods_id"));
                    ps.setObject(5, newdata.get("dis_office_id"));
                    ps.setObject(6, newdata.get("dis_cont_id"));
                    ps.setObject(7, newdata.get("car_kind_id"));
                    ps.setObject(8, newdata.get("empty_weight"));
                    ps.setObject(9, newdata.get("bill_id"));
                    ps.setObject(10, newdata.get("car_id"));
                    ps.setObject(11, newdata.get("max_weight"));
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
//      int err = 0;
            Statement st = con_.createStatement();
            st.executeUpdate("begin;");
            try {
                String s = new StringBuilder()
                         .append("update m_car_no set ")
                         .append("car_no = ?, dis_ent_id = ?, ")
                         .append("trans_id = ?, goods_id = ?, ")
                         .append("dis_office_id = ?, ")
                         .append("dis_cont_id = ?, kind_id = ?, empty_weight = ?, ")
                         .append("bill_id = ?, car_id = ?, max_weight = ? ")
                         .append("where sno = ?;")
                         .toString();
                PreparedStatement ps = con_.prepareStatement(s);
                try {
                    ps.setObject(1, updata.get("car_no"));
                    ps.setObject(2, updata.get("dis_ent_id"));
                    ps.setObject(3, updata.get("trans_id"));
                    ps.setObject(4, updata.get("goods_id"));
                    ps.setObject(5, updata.get("dis_office_id"));
                    ps.setObject(6, updata.get("dis_cont_id"));
                    ps.setObject(7, updata.get("car_kind_id"));
                    ps.setObject(8, updata.get("empty_weight"));
                    ps.setObject(9, updata.get("bill_id"));
                    ps.setObject(10, updata.get("car_id"));
                    ps.setObject(11, updata.get("max_weight"));
                    ps.setObject(12, updata.get("sno"));
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
                         .append("update m_car_no ")
                         .append("set delete_flag = true ")
                         .append("where sno = ?;")
                         .toString();
                PreparedStatement ps = con_.prepareStatement(s);
                try {
//          ps.setObject(1, LoginHelper.getManager(con_));
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

    //風袋重量登録ファンクション
    public boolean weight(int row, int weigh) {
        try {
            Map<String, Object> columns = getColumns(row);

            Statement st = con_.createStatement();
            st.executeUpdate("begin;");
            try {
                String s = new StringBuilder()
                         .append("update m_car_no ")
                         .append("set empty_weight = ? ")
                         .append("where sno = ?;")
                         .toString();
                PreparedStatement ps = con_.prepareStatement(s);
                try {
//          ps.setObject(1, LoginHelper.getManager(con_));
                    ps.setObject(1, weigh);
                    ps.setObject(2, columns.get("sno"));
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

    //最大積載量登録ファンクション
    public boolean max_weight(int row, int weigh) {
        try {
            Map<String, Object> columns = getColumns(row);

            Statement st = con_.createStatement();
            st.executeUpdate("begin;");
            try {
                String s = new StringBuilder()
                         .append("update m_car_no ")
                         .append("set max_weight = ? ")
                         .append("where sno = ?;")
                         .toString();
                PreparedStatement ps = con_.prepareStatement(s);
                try {
//          ps.setObject(1, LoginHelper.getManager(con_));
                    ps.setObject(1, weigh);
                    ps.setObject(2, columns.get("sno"));
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
                         "管理ＩＤ,車番, 排出事業者, 運搬業者, 品目, 排出事業場, 処分業者, 車種, 風袋重量");
                out.newLine();
                synchronized (rows_) {
                    for (Map<String, Object> columns : rows_) {

                        // 管理ＩＤ
                        Integer i = (Integer) columns.get("car_id");
                        StringBuilder sb = new StringBuilder().append(i).append(",");
                        // 車番
                        i = (Integer) columns.get("car_no");
                        sb = sb.append(i).append(",");
//                        // 請求先
//                        String s = (String) columns.get("bill_name");
//                        sb = sb.append(s).append(",");
                        // 排出事業者
                        String s = (String) columns.get("entrepreneur_name");
                        sb = sb.append(s).append(",");
                        // 運搬業者
                        s = (String) columns.get("transporter_name");
                        sb = sb.append(s).append(",");
                        // 品目
                        s = (String) columns.get("goods_name");
                        sb = sb.append(s).append(",");
                        // 排出事業場
                        s = (String) columns.get("office_name");
                        sb = sb.append(s).append(",");
                        // 処分業者
                        s = (String) columns.get("contractor_name");
                        sb = sb.append(s).append(",");
                        // 車種
                        s = (String) columns.get("car_kind_name");
                        sb = sb.append(s).append(",");
                        // 風袋重量
                        i = (Integer) columns.get("empty_weight");
                        sb = sb.append(i).append(",");
                        // 最大重量
                        i = (Integer) columns.get("max_weight");
                        sb = sb.append(i);

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
