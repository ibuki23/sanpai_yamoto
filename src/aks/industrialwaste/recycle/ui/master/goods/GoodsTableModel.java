package aks.industrialwaste.recycle.ui.master.goods;

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

public class GoodsTableModel extends AbstractTableModel {

    public static final int COLUMN_COUNT = 27;
    public static final int COLUMN_ID = 0;
    public static final int COLUMN_NAME = 1;
    public static final int COLUMN_OFFICIAL_NAME = 2;
    public static final int COLUMN_OPTION1 = 3;
    public static final int COLUMN_OPTION_NAME = 4;
    public static final int COLUMN_METHOD1 = 5;
    public static final int COLUMN_METHOD2 = 6;
    public static final int COLUMN_UNIT_NO = 7;
    public static final int COLUMN_UNIT_NAME = 8;
    public static final int COLUMN_UNIT_VALUE = 9;

    //20120822 add
    public static final int COLUMN_MIDDLE_GOODS = 12;
    public static final int COLUMN_FINAL_GOODS = 13;
    public static final int COLUMN_TAX_KIND = 14;
    public static final int COLUMN_MIDDLE_GOODS_NAME = 15;
    public static final int COLUMN_FINAL_GOODS_NAME = 16;
    public static final int COLUMN_TAX_KIND_NAME = 17;

    //20121016 add
    public static final int COLUMN_CARRY_CLASS = 18;
    public static final int COLUMN_CARRY_NAME = 19;

    //20121024 add
    public static final int COLUMN_BILL_NAME = 20;
    public static final int COLUMN_BILL_NAME2 = 21;

    public static final int COLUMN_RYUBE = 22;

    public static final int COLUMN_UNIT_VALUE_A = 23;
    public static final int COLUMN_UNIT_VALUE_B = 24;
    public static final int COLUMN_UNIT_VALUE_C = 25;

    public static final int COLUMN_有効 = 26;
    public static final int COLUMN_警報 = 27;
    //COLUMN_有効 COLUMN_警報 COLUMN_在庫量
    public static final int COLUMN_SNO = 28;

    private static final String[] COLUMN_NAMES = {
        "id", "name", "official_name", "option1", "option1_name", "method_name1",
        "method_name2", "unit_no", "unit_name", "unit_value",
        "method_id1", "method_id2",
        //20120822 add 行政報告用
        "middle_goods_kind",
        "final_goods_kind",
        "tax_kind",
        "middle_goods_kind_name",
        "final_goods_kind_name",
        "tax_kind_name",
        //20121016 add
        "carry_class",
        "carry_name",
        "bill_name",//20
        "bill_name2",
        "ryube",
        "unit_value_a",
        "unit_value_b",
        "unit_value_c",
        "f有効",
        "f警報",
        //        "d在庫量",
        "sno"

    };

    protected static Logger logger_
             = Logger.getLogger(GoodsTableModel.class.getName());
    private Connection con_;
    private List<Map<String, Object>> rows_
             = Collections.synchronizedList(new ArrayList<>());

    public GoodsTableModel(Connection con) {
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
                              "v0_goods_kind",
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
                         .append("insert into m_goods_kind")
                         .append("(id, name, official_name, option1, method_id1, method_id2, ")
                         .append("unit_no, unit_value, ")
                         .append("middle_goods_kind, final_goods_kind, tax_kind, carry_class, ")
                         .append("b_class_id, bill_name, bill_name2, ryube, ")
                         .append("unit_value_a, unit_value_b, unit_value_c, f按分フラグ, f有効) ")
                         .append("values(?, ?, ?, ?, ?,")
                         .append("?, ?, ?, ?, ?,")
                         .append("?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);")
                         .toString();

                PreparedStatement ps = con_.prepareStatement(s);
                try {
                    ps.setObject(1, newdata.get("id"));
                    ps.setObject(2, newdata.get("name"));
                    ps.setObject(3, newdata.get("official_name"));
                    ps.setObject(4, newdata.get("option1"));
                    ps.setObject(5, newdata.get("method_id1"));
                    ps.setObject(6, newdata.get("method_id2"));
                    ps.setObject(7, newdata.get("unit_no"));
                    ps.setObject(8, newdata.get("unit_value"));
                    //20120822 add
                    ps.setObject(9, newdata.get("middle_goods_kind"));
                    ps.setObject(10, newdata.get("final_goods_kind"));
                    ps.setObject(11, newdata.get("tax_kind"));
                    ps.setObject(12, newdata.get("carry_class"));
                    //20121024 add
                    ps.setObject(13, newdata.get("b_class_id"));
                    ps.setObject(14, newdata.get("bill_name"));
                    ps.setObject(15, newdata.get("bill_name2"));
                    ps.setObject(16, newdata.get("ryube"));

                    ps.setObject(17, newdata.get("unit_value_a"));
                    ps.setObject(18, newdata.get("unit_value_b"));
                    ps.setObject(19, newdata.get("unit_value_c"));
                    ps.setObject(20, newdata.get("f按分フラグ"));
                    //f有効
                    ps.setObject(21, newdata.get("f有効"));

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
                         .append("update m_goods_kind set ")
                         .append("name = ?, official_name = ?, ")
                         .append("option1 = ?, method_id1= ?, method_id2 = ?, ")
                         .append("unit_no = ?, unit_value = ?, id = ?, ")
                         .append("middle_goods_kind = ?, final_goods_kind= ?, tax_kind = ?, ") //20121016 add
                         .append("carry_class = ?, ") //20121024 add
                         .append("b_class_id = ?, bill_name = ?, bill_name2 = ?, ")
                         .append("ryube = ?, unit_value_a = ?, unit_value_b = ?, unit_value_c = ?, f按分フラグ = ?, f有効 = ? ")
                         .append("where sno = ?;")
                         .toString();
                PreparedStatement ps = con_.prepareStatement(s);
                try {
                    ps.setObject(1, updata.get("name"));
                    ps.setObject(2, updata.get("official_name"));
                    ps.setObject(3, updata.get("option1"));
                    ps.setObject(4, updata.get("method_id1"));
                    ps.setObject(5, updata.get("method_id2"));
                    ps.setObject(6, updata.get("unit_no"));
                    ps.setObject(7, updata.get("unit_value"));
                    ps.setObject(8, updata.get("id"));
                    //20120822 add
                    ps.setObject(9, updata.get("middle_goods_kind"));
                    ps.setObject(10, updata.get("final_goods_kind"));
                    ps.setObject(11, updata.get("tax_kind"));

                    //20121015 add
                    ps.setObject(12, updata.get("carry_class"));
                    //20121015 add
                    ps.setObject(13, updata.get("b_class_id"));
                    ps.setObject(14, updata.get("bill_name"));
                    ps.setObject(15, updata.get("bill_name2"));
                    ps.setObject(16, updata.get("ryube"));
                    ps.setObject(17, updata.get("unit_value_a"));
                    ps.setObject(18, updata.get("unit_value_b"));
                    ps.setObject(19, updata.get("unit_value_c"));
                    ps.setObject(20, updata.get("f按分フラグ"));
                    ps.setObject(21, updata.get("f有効"));
                    //f有効
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
                         .append("update m_goods_kind ")
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
                         "ＩＤ,名称,正式名称,中間最終区分,搬入搬出区分,単位,行政報告区分１,行政報告区分２,請求分類,請求名称,標準単価,比重");
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
                        s = (String) columns.get("official_name");
                        sb = sb.append(s).append(",");

                        //中間最終区分
                        s = (String) columns.get("option1_name");
                        sb = sb.append(s).append(",");
                        // 搬入搬出区分　carry_name
                        s = (String) columns.get("carry_name");
                        sb = sb.append(s).append(",");
                        //単位
                        s = (String) columns.get("unit_name");
                        sb = sb.append(s).append(",");
                        //行政報告大分類１
                        s = (String) columns.get("middle_goods_kind_name");
                        sb = sb.append(s).append(",");
                        //final_goods_kind_name
                        //行政報告大分類２
                        s = (String) columns.get("final_goods_kind_name");
                        sb = sb.append(s).append(",");

                        //請求分類
                        s = (String) columns.get("b_class_name");
                        sb = sb.append(s).append(",");

                        //請求名称
                        s = (String) columns.get("bill_name");
                        sb = sb.append(s).append(",");

                        //標準単価
                        i = (Integer) columns.get("unit_value");
                        sb = sb.append(i).append(",");

                        //ryube 比重
                        Double d = (Double) columns.get("ryube");
                        sb = sb.append(d);

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
                    ps.setObject(1, "m_goods_kind");
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
            st.executeUpdate("delete from m_goods_kind;");
            try {
                String s = new StringBuilder()
                         .append("SET CLIENT_ENCODING TO 'UTF8';\n COPY m_goods_kind from E'C:\\\\csv\\\\master_csv\\\\m_goods_kind.csv' CSV;")
                         .toString();
                PreparedStatement ps = con_.prepareStatement(s);
                try {
                    ps.executeUpdate();
                } finally {
                    ps.close();
                }

                st.executeUpdate("commit;");

                s = new StringBuilder()
                         .append("SELECT setval('m_goods_kind_sno_seq', (select max(sno) from m_goods_kind)::bigint); ")
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
            st.executeUpdate("delete from m_goods_kind;");
            try {
                String s = new StringBuilder()
                         .append("SET CLIENT_ENCODING TO 'UTF8';\n COPY m_goods_kind from E'C:\\\\csv\\\\master_csv\\\\m_goods_kind.csv' CSV;")
                         .toString();
                PreparedStatement ps = con_.prepareStatement(s);
                try {
                    ps.executeUpdate();
                } finally {
                    ps.close();
                }

                st.executeUpdate("commit;");

                s = new StringBuilder()
                         .append("SELECT setval('m_goods_kind_sno_seq', (select max(sno) from m_goods_kind)::bigint); ")
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
}
