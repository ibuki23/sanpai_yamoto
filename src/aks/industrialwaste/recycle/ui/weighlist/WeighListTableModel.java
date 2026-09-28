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
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import javax.swing.table.AbstractTableModel;
import aks.industrialwaste.database.DBManager;
import aks.industrialwaste.database.SearchCondition;
import aks.industrialwaste.database.SortCondition;
import aks.industrialwaste.util.TermDate;
import aks.keiryo.util.Str2Int;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;

public class WeighListTableModel extends AbstractTableModel {

    public static final int COLUMN_COUNT = 36;
    public static final int COLUMN_REPORT_NO = 0;
    public static final int COLUMN_TIME = 1;
    public static final int COLUMN_CAR_NO = 2;
    public static final int COLUMN_NET_WEIGHT = 3;
    public static final int COLUMN_DIS_ENT = 4;
    public static final int COLUMN_TRANSPORTER = 5;
    public static final int COLUMN_KIND = 6;
    public static final int COLUMN_DIS_OFFICE = 7;
    public static final int COLUMN_CONTRACTORY = 8;
    public static final int COLUMN_OPTION = 9;
    public static final int COLUMN_MNIFEST = 10;
    public static final int COLUMN_MNIFESTO1 = 11;
    public static final int COLUMN_MNIFESTO2 = 12;
    public static final int COLUMN_GATE_IN_DATE = 13;
    public static final int COLUMN_GATE_IN_WEIGHT = 14;
    public static final int COLUMN_GATE_OUT_DATE = 15;
    public static final int COLUMN_GATE_OUT_WEIGHT = 16;
    public static final int COLUMN_GATE_IN_SNO = 17;
    public static final int COLUMN_TERM_DATE = 18;
    public static final int COLUMN_SNO = 19;
    public static final int COLUMN_GRANT_DATE1 = 20;
    public static final int COLUMN_GRANT_DATE2 = 21;
    public static final int COLUMN_CONT_DATE1 = 22;
    public static final int COLUMN_CONT_DATE2 = 23;
    public static final int COLUMN_TRANS_ID = 24;
    public static final int COLUMN_GOODS_ID = 25;
    public static final int COLUMN_OFFICE_ID = 26;
    public static final int COLUMN_CONT_ID = 27;
    public static final int COLUMN_BILL_ID = 28;
    public static final int COLUMN_BILL_NAME = 29;
    public static final int COLUMN_OFFICE_SUB_NAME = 30;
    public static final int COLUMN_M3 = 31;
    public static final int COLUMN_CONT_OFFICE_SUB_NAME = 32;
    public static final int COLUMN_CARRY_NAME = 33;
    public static final int COLUMN_RYUBE = 34;
    public static final int COLUMN_料金 = 35;
    public static final int COLUMN_GRANT_DATE12 = 36;
    public static final int COLUMN_単価 = 37;
    public static final int COLUMN_標準単価 = 38;
    public static final int COLUMN_最終処分の場所 = 40;
    public static final int COLUMN_2次交付番号 = 56;
    public static final int COLUMN_電子マニフェスト区分 = 57;
    public static final int COLUMN_単位 = 58;
    public static final int COLUMN_売上区分 = 60;
    public static final int COLUMN_REMARK = 61;

    private static final String[] COLUMN_NAMES = {
        "report_no",
        "gate_out_date",
        "car_no",
        "net_weight",
        "entrepreneur_name",
        "transporter_name",//5
        "goods_name",
        "office_name",
        "contractor_name",
        "goods_option_name",
        "manifesto_no",//10
        "manifesto_no1",
        "manifesto_no2",
        "gate_in_date",
        "gate_in_weight",
        "gate_out_date",
        "gate_out_weight",//15
        "gate_in_sno",
        "term_date",
        "sno",
        "grant_date1",//20
        "grant_date2",
        "cont_date1",
        "cont_date2",
        "trans_id",
        "goods_id",
        "dis_office_id",
        "dis_cont_id",//27
        "bill_id",
        "bill_name",
        "office_sub_name",//30
        "m3",
        "cont_office_name",
        "carry_name",
        "ryube",
        "charge_value",//35
        "grant_date12",
        "unit",
        "unit_value",
        "carry_no",
        "last_office_name",//40
        "empty_weight",
        "car_kind_name",
        "method_id1",
        "trans1_sub_name",
        "trans1_date",
        "trans1_weight",
        "cont_sub_name",
        "dis_office_id",
        "dis_cont_id",
        "ent_print_flag",//50
        "trans1_print_flag",
        "office_print_flag",
        "cont_print_flag",
        "manifesto_sno",
        "class",
        "secod_manifesto_no",
        "option1",
        "unit_name",
        "carry_class",
        "売上区分",
        "remark"
    };
    protected static Logger logger_
             = Logger.getLogger(WeighListTableModel.class.getName());
    private Connection con_;
    private List<Map<String, Object>> rows_
             = Collections.synchronizedList(new ArrayList<>());

    public WeighListTableModel(Connection con) {
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

    public void addRow() {
        Map<String, Object> s = new HashMap<>();
        Calendar c = Calendar.getInstance();
        s.put("total_weight", 0);
        s.put("empty_weight", 0);
        s.put("net_weight", 0);
        s.put("gate_in_date", c);
        s.put("gate_out_date", c);
        s.put("term_date", TermDate.getTerm(c).getTime());

        s.put("car_no", 0);
        s.put("unit", 0.);
        s.put("charge_value", 0);
        s.put("class", 0);

        s.put("bill_id", 0);
        s.put("dis_ent_id", 0);
        s.put("trans_id", 0);
        s.put("goods_id", 0);
        s.put("dis_office_id", 0);
        s.put("dis_cont_id", 0);
        s.put("car_kind_id", 0);

        s.put("method_id1", 0);
        s.put("method_id2", 0);

        s.put("unit_value", 0);
        s.put("unit_name", "");
        // 中間最終
        s.put("goods_option_name", "中間");
        //20120617 現場補完分
        s.put("office_sub_name", "");
        //20120914 リューベ追加
        s.put("m3", 0.);
        //20121017 add 搬入出区分追加
        s.put("carry_class", 0);
        s.put("carry_name", "搬入");
        //20121022 add 請求単価・運搬単価追加
        s.put("unit_no", 0);
        s.put("unit_name", "");

        s.put("trans_unit_value", 0);
        s.put("ryube", 0.);
        s.put("売上区分", "");
        rows_.add(s);

        fireTableDataChanged();
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
                              "v1_weigh_data",
                              "*",
                              (null == search) ? (new SearchCondition[0]) : (search),
                              (null == sort) ? (new SortCondition[0]) : (sort),
                              3000);
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

            // 計量NO取得
            st.executeUpdate("begin;");
            String str = "select next_weigh_report_no('"
                     + DBManager.convert((Date) newdata.get("term_date"))
                     + "') as report_no_;";
            ResultSet rs = st.executeQuery(str);
            rs.next();

            try {
                String s = new StringBuilder()
                         .append("insert into weigh_data")
                         .append("(gate_in_date, ")
                         .append("gate_in_weight, ")
                         .append("gate_out_date, ")
                         .append("gate_out_weight, ")
                         .append("gate_in_sno, ")
                         .append("term_date, ")
                         .append("report_no, ")
                         .append("manifesto_no1, ")
                         .append("manifesto_no2, ")
                         .append("manifesto_no3, ")
                         .append("car_no, ")
                         .append("dis_ent_id, ")
                         .append("trans_id, ")
                         .append("goods_id, ")
                         .append("dis_office_id, ")
                         .append("dis_cont_id, ")
                         .append("carry_no, ")
                         .append("car_kind_id, ")
                         .append("change_flag, ")
                         .append("unit, ")
                         .append("charge_value, ")
                         .append("cont_date2, ")
                         .append("class, ")
                         .append("method_id, ")
                         .append("bill_id, office_sub_name, m3, carry_class, ryube, printer_print_order, car_kind_name, 売上区分) ")
                         .append("values(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ")
                         .append("?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ")
                         .append("?, ?, ?, ?, ?, ?, ?, ?, ?, false, ?, ?);")
                         .toString();
                PreparedStatement ps = con_.prepareStatement(s);
                try {
                    ps.setObject(1, DBManager.convert((Calendar) newdata.get("gate_in_date")));
                    ps.setObject(2, newdata.get("total_weight"));
                    ps.setObject(3, DBManager.convert((Calendar) newdata.get("gate_out_date")));
                    ps.setObject(4, newdata.get("empty_weight"));
                    ps.setObject(5, 0);
                    ps.setObject(6, DBManager.convert((Date) newdata.get("term_date")));
                    ps.setObject(7, rs.getString("report_no_"));
                    ps.setObject(8, newdata.get("manifesto_no1"));
                    ps.setObject(9, newdata.get("manifesto_no2"));
                    ps.setObject(10, newdata.get("manifesto_no3"));
                    ps.setObject(11, newdata.get("car_no"));
                    ps.setObject(12, newdata.get("dis_ent_id"));
                    ps.setObject(13, newdata.get("trans_id"));
                    ps.setObject(14, newdata.get("goods_id"));
                    ps.setObject(15, newdata.get("dis_office_id"));
                    ps.setObject(16, newdata.get("dis_cont_id"));
                    ps.setObject(17, 1);
                    ps.setObject(18, newdata.get("car_kind_id"));
                    ps.setObject(19, true);
                    ps.setObject(20, newdata.get("unit"));
                    ps.setObject(21, newdata.get("charge_value"));
                    if (newdata.get("cont_date2") != null) {
                        ps.setObject(22, DBManager.convert(DBManager.convert(String.format("%1$tY-%1$tm-%1$td", ((Calendar) newdata.get("cont_date2")).getTime()))));
                    } else {
                        ps.setObject(22, null);
                    }
                    ps.setObject(23, newdata.get("class"));

                    if (newdata.get("manifesto_no1") == null ? "" != null : !newdata.get("manifesto_no1").equals("")) {
                        ps.setObject(24, newdata.get("method_id2"));
                    } else if (newdata.get("manifesto_no2") == null ? "" != null : !newdata.get("manifesto_no2").equals("")) {
                        ps.setObject(24, newdata.get("method_id1"));
                    } else {
                        ps.setObject(24, 0);
                    }

                    //請求先追加
                    ps.setObject(25, newdata.get("bill_id"));
                    //20120617 現場補完分
                    ps.setObject(26, newdata.get("office_sub_name"));
                    //20120914 リューベ
                    ps.setObject(27, newdata.get("m3"));
                    //20121017 add
                    ps.setObject(28, newdata.get("carry_class"));
                    //20121225 比重係数
                    ps.setObject(29, newdata.get("ryube"));
                    ps.setObject(30, newdata.get("car_kind_name"));
                    ps.setObject(31, newdata.get("売上区分"));
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

    //マニフェストデータ作成
    public boolean insert2(Map<String, Object> newdata) {
        try {
            Statement st = con_.createStatement();

            st.executeUpdate("begin;");
            String str = "select sno, gate_in_sno, "
                     + " term_date, report_no "
                     + " from v1_weigh_data order by last_update desc limit 1;";
            ResultSet rs = st.executeQuery(str);
            rs.next();

            try {
                String s = new StringBuilder()
                         .append("insert into manifesto_data")
                         .append("(weight_sno, ")
                         .append("gate_in_sno, ")
                         .append("term_date, ")
                         .append("report_no, ")
                         .append("manifesto_no1, ")
                         .append("manifesto_no2, ")
                         .append("manifesto_no3, ")
                         .append("dis_ent_id, ")
                         .append("trans1_id, ")
                         .append("goods_id, ")
                         .append("dis_office_id, ")
                         .append("dis_cont_id )")
                         .append("values(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ")
                         .append("?);")
                         .toString();
                PreparedStatement ps = con_.prepareStatement(s);
                try {
                    ps.setObject(1, rs.getInt("sno"));
                    ps.setObject(2, rs.getInt("gate_in_sno"));
                    ps.setObject(3, rs.getDate("term_date"));
                    ps.setObject(4, newdata.get("report_no"));
                    ps.setObject(5, newdata.get("manifesto_no1"));
                    ps.setObject(6, newdata.get("manifesto_no2"));
                    ps.setObject(7, newdata.get("manifesto_no3"));
                    ps.setObject(8, newdata.get("dis_ent_id"));
                    ps.setObject(9, newdata.get("trans_id"));
                    ps.setObject(10, newdata.get("goods_id"));
                    ps.setObject(11, newdata.get("dis_office_id"));
                    ps.setObject(12, newdata.get("dis_cont_id"));
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
                int total_weight_ = (Integer) updata.get("total_weight");
                int empty_weight_ = (Integer) updata.get("empty_weight");
                String s = new StringBuilder()
                         .append("update weigh_data ")
                         .append("set gate_in_date = ?, ")
                         .append("gate_in_weight = ?, ")
                         .append("gate_out_date = ?, ")
                         .append("gate_out_weight = ?, ")
                         .append("gate_in_sno = ?, ")
                         .append("term_date = ?, ")
                         .append("report_no = ?, ")
                         .append("manifesto_no1 = ?, ")
                         .append("manifesto_no2 = ?, ")
                         .append("manifesto_no3 = ?, ")
                         .append("car_no = ?, ")
                         .append("dis_ent_id = ?, ")
                         .append("trans_id = ?, ")
                         .append("goods_id = ?, ")
                         .append("dis_office_id = ?, ")
                         .append("dis_cont_id = ?, ")
                         .append("carry_no = ?, ")
                         .append("car_kind_id = ?, ")
                         .append("change_flag = ? ,")
                         .append("unit = ? ,")
                         .append("charge_value = ?, ")
                         .append("cont_date2 = ?, ")
                         .append("class = ?, ")
                         .append("method_id = ?, ")
                         .append("bill_id = ? , office_sub_name = ?, m3 = ?, carry_class = ? , ryube = ?, printer_print_order = false, ")
                         .append("car_kind_name = ?, 売上区分 = ? ")
                         .append("where sno = ?;")
                         .toString();
                PreparedStatement ps = con_.prepareStatement(s);
                try {
                    ps.setObject(1, DBManager.convert((Calendar) updata.get("gate_in_date")));
                    if ((Integer) updata.get("carry_class") == 0) {
                        ps.setObject(2, total_weight_);
                    } else {
                        ps.setObject(2, empty_weight_);
                    }
                    ps.setObject(3, DBManager.convert((Calendar) updata.get("gate_out_date")));
                    if ((Integer) updata.get("carry_class") == 0) {
                        ps.setObject(4, empty_weight_);
                    } else {
                        ps.setObject(4, total_weight_);
                    }
                    ps.setObject(5, updata.get("gate_in_sno"));
                    ps.setObject(6, DBManager.convert((Date) updata.get("term_date")));
                    ps.setObject(7, updata.get("report_no"));
                    ps.setObject(8, updata.get("manifesto_no1"));
                    ps.setObject(9, updata.get("manifesto_no2"));
                    ps.setObject(10, updata.get("manifesto_no3"));
                    ps.setObject(11, updata.get("car_no"));
                    ps.setObject(12, updata.get("dis_ent_id"));
                    ps.setObject(13, updata.get("trans_id"));
                    ps.setObject(14, updata.get("goods_id"));
                    ps.setObject(15, updata.get("dis_office_id"));
                    ps.setObject(16, updata.get("dis_cont_id"));
                    ps.setObject(17, updata.get("carry_no"));
                    ps.setObject(18, updata.get("car_kind_id"));
                    ps.setObject(19, true);
                    ps.setObject(20, updata.get("unit"));
                    ps.setObject(21, updata.get("charge_value"));
                    if (updata.get("cont_date2") != null) {
                        ps.setObject(22, DBManager.convert(DBManager.convert(String.format("%1$tY-%1$tm-%1$td", ((Calendar) updata.get("cont_date2")).getTime()))));
                    } else {
                        ps.setObject(22, null);
                    }
                    ps.setObject(23, updata.get("class"));

//                    if (updata.get("manifesto_no1") == null ? "" != null : !updata.get("manifesto_no1").equals("")) {
//                        ps.setObject(24, updata.get("method_id1"));
//                    } else if (updata.get("manifesto_no2") == null ? "" != null : !updata.get("manifesto_no2").equals("")) {
//                        ps.setObject(24, updata.get("method_id2"));
//                    } else {
//                        ps.setObject(24, 0);
//                    }
                    if (updata.get("manifesto_no1") == null ? "" != null : !updata.get("manifesto_no1").equals("")) {
                        ps.setObject(24, updata.get("method_id2"));
                    } else if (updata.get("manifesto_no2") == null ? "" != null : !updata.get("manifesto_no2").equals("")) {
                        ps.setObject(24, updata.get("method_id1"));
                    } else {
                        ps.setObject(24, 0);
                    }

                    //請求先追加
                    ps.setObject(25, updata.get("bill_id"));

                    ps.setObject(26, updata.get("office_sub_name"));
                    ps.setObject(27, updata.get("m3"));
                    //20121017 add
                    ps.setObject(28, updata.get("carry_class"));
                    //20121225
                    ps.setObject(29, updata.get("ryube"));
                    ps.setObject(30, updata.get("car_kind_name"));
                    ps.setObject(31, updata.get("売上区分"));
                    ps.setObject(32, updata.get("sno"));

                    ps.executeUpdate();

                    //20140529 add
                    //マニフェストデータの現場名も更新する
                    String sm = new StringBuilder()
                             .append("update manifesto_data ")
                             .append("set office_zip = ?, ")
                             .append("office_tel = ?, ")
                             .append("office_address = ? ")
                             .append("where sno = ?;")
                             .toString();
                    try (PreparedStatement psm = con_.prepareStatement(sm)) {
                        psm.setObject(1, updata.get("office_zip"));
                        psm.setObject(2, updata.get("office_tel"));
                        psm.setObject(3, updata.get("office_address1"));
                        psm.setObject(4, updata.get("manifesto_sno"));

                        psm.executeUpdate();
                    }

                } finally {
                    ps.close();

                }

                st.executeUpdate("commit;");

                java.awt.Toolkit.getDefaultToolkit().beep();
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

    //マニフェストデータ作成
    //rs:現在状態
    //ret:0以上成功 0未満異常
    public long SetManifestoData(String data) {

        long ret = -1;

        try {
            Statement st = con_.createStatement();
            st.executeUpdate("begin;");
            try {
                String s = String.format("select add_manifesto_data01(%s); ",
                         Str2Int.get(data));
                PreparedStatement ps = con_.prepareStatement(s);
                try {
                    ps.executeQuery();
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

    public boolean delete(int row) {
        try {
            Map<String, Object> columns = getColumns(row);

            Statement st = con_.createStatement();
            st.executeUpdate("begin;");
            try {
                String s = new StringBuilder()
                         .append("update weigh_data ")
                         .append("set change_flag = true, ")
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

    public boolean 電子マニフェスト区分変更(int row) {
        try {
            Map<String, Object> columns = getColumns(row);

            Statement st = con_.createStatement();
            st.executeUpdate("begin;");
            try {
                String s = new StringBuilder()
                         .append("update weigh_data ")
                         .append("set option1 = (case when option1 = 0 then 1 else 0 end)::integer ")
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
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
            try {
                // タイトル 現場名の右側に備考欄追加　200601
                out.write(
                         "マニフェストNo,伝票No,月日,排出事業者ID,排出事業者,種類,現場ID,現場,備考,"
                         + "品目ID,品目,数量(kg),運搬受託ID,運搬受託,車輌No.,"
                         + "車種,処分受託者ID,処分受託者,処分日,単価,金額,請求先ID,請求先,売上区分,手入力");//2017/09/30 CSVに請求先追加
                out.newLine();
                synchronized (rows_) {
                    for (Map<String, Object> columns : rows_) {

                        String s = "";
                        // マニフェストNO
                        if (((String) columns.get("manifesto_no1")).length() > 0) {
                            s = (String) columns.get("manifesto_no1");
                        } else if (((String) columns.get("manifesto_no2")).length() > 0) {
                            s = (String) columns.get("manifesto_no2");
                        } else if (((String) columns.get("manifesto_no3")).length() > 0) {
                            s = (String) columns.get("manifesto_no3");
                        } else {
                            s = "";
                        }
                        StringBuilder sb = new StringBuilder().append(s).append(",");

                        // No
                        s = (String) columns.get("report_no");
                        sb = sb.append(s).append(",");
                        // 計量日時
                        s = sdf.format((Date) columns.get("term_date"));
                        sb = sb.append(s).append(",");
                        // 排出業者ID
                        Integer i = (Integer) columns.get("dis_ent_id");
                        sb = sb.append(i).append(",");
                        // 排出事業者
                        s = (String) columns.get("entrepreneur_name");
                        sb = sb.append(s).append(",");
                        // 種類（中間・最終・販売等）
                        s = (String) columns.get("goods_option_name");
                        sb = sb.append(s).append(",");
                        //現場ID
                        i = (Integer) columns.get("dis_office_id");
                        sb = sb.append(i).append(",");
                        // 現場
                        s = (String) columns.get("office_name");
                        sb = sb.append(s).append(",");

                        // 備考 200601
                        // null対応
                        if (columns.get("remark") != null) {
                            s = (String) columns.get("remark");
                        } else {
                            s = "";
                        }
                        sb = sb.append(s).append(",");

                        //品目ID
                        i = (Integer) columns.get("goods_id");
                        sb = sb.append(i).append(",");
                        // 品目（廃棄物名）
                        s = (String) columns.get("goods_name");
                        sb = sb.append(s).append(",");

                        i = (Integer) columns.get("net_weight");
                        sb = sb.append(i).append(",");

//                        Double d = (Double) columns.get("net_weight_ton");
//                        if (columns.get("net_weight_ton") != null) {
//                            sb = sb.append(d).append(",");
//                        } else {
//                            sb = sb.append("").append(",");
//                        }
                        //運搬業者ID
                        i = (Integer) columns.get("trans_id");
                        sb = sb.append(i).append(",");
                        // 運搬業者
                        s = (String) columns.get("transporter_name");
                        sb = sb.append(s).append(",");
                        //車番
                        i = (Integer) columns.get("car_no");
                        sb = sb.append(i).append(",");

                        // 車種名
                        s = (String) columns.get("car_kind_name");
                        sb = sb.append(s).append(",");
                        //処分業者ID
                        i = (Integer) columns.get("dis_cont_id");
                        sb = sb.append(i).append(",");
                        // 処分業者
                        s = (String) columns.get("contractor_name");
                        sb = sb.append(s).append(",");
                        // 処分日
                        s = (Date) columns.get("cont_date12") != null ? sdf.format((Date) columns.get("cont_date12")) : "";
                        sb = sb.append(s).append(",");
                        // 単価
                        Double d = (Double) columns.get("unit");
                        sb = sb.append(d).append(",");
                        // 金額
                        i = (Integer) columns.get("charge_value");
                        sb = sb.append(i).append(",");

                        // 請求先ID
                        i = (Integer) columns.get("bill_id");
                        sb = sb.append(i).append(",");
                        // 請求先
                        s = (String) columns.get("bill_name");
                        sb = sb.append(s).append(",");
                        // 売上区分
                        s = (String) columns.get("売上区分");
                        sb = sb.append(s).append(",");
                        // 現場手入力
                        s = (String) columns.get("office_sub_name");
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
