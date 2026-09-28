package aks.industrialwaste.recycle.ui.weighlistSales;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
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
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;

public class WeighListSalesTableModel extends AbstractTableModel {

    public static final int COLUMN_COUNT = 8;
    public static final int COLUMN_BILL_ID = 0;
    public static final int COLUMN_BILL_NAME = 1;
    public static final int COLUMN_SUM_CHARGE_VALUE = 2;
    public static final int COLUMN_SUM_TAX_VALUE = 3;
    public static final int COLUMN_SUM_CHARGE_TAX_VALUE = 4;
    public static final int COLUMN_現金受領済み = 5;
    public static final int COLUMN_BILL_NO = 6;
    public static final int COLUMN_DUE_DATE = 7;

    private static final String[] COLUMN_NAMES = {
        "bill_id",
        "bill_name",
        "sum_charge_value",
        "sum_tax_value",
        "sum_charge_tax_value",
        "option2",
        "billing_no",
        "due_date"
    };
    protected static Logger logger_
             = Logger.getLogger(WeighListSalesTableModel.class.getName());
    private Connection con_;
    private List<Map<String, Object>> rows_
             = Collections.synchronizedList(new ArrayList<>());

    public WeighListSalesTableModel(Connection con) {
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

    public void refresh(Date d1, Date d2, Integer tax, Integer 締め日, SearchCondition[] search, SortCondition[] sort) {
        clear();
        try {
            PreparedStatement ps
                     = DBManager.prepareStatement(
                              con_,
                              "f1_得意先売上高一覧(?, ?, ?, ?) ",
                              "*",
                              (null == search) ? (new SearchCondition[0]) : (search),
                              (null == sort) ? (new SortCondition[0]) : (sort),
                              0);
            try {
                ps.setObject(1, d1);
                ps.setObject(2, d2);
                ps.setObject(3, tax);
                ps.setObject(4, 締め日);
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

    public void report(File f) {
        try {
            BufferedWriter out = new BufferedWriter(
                     new OutputStreamWriter(
                              new FileOutputStream(f), "MS932"));
            try {
                // タイトル
                out.write(
                         "回数,計量日時,車番,正味重量,請求先,排出事業者,運搬業者,品目,単位,取扱区分,搬入出,排出事業場,排出事業場2,処分業者,マニフェストNo");
                out.newLine();
                synchronized (rows_) {
                    for (Map<String, Object> columns : rows_) {

                        // 車番
                        String s = (String) columns.get("report_no");
                        StringBuilder sb = new StringBuilder().append(s).append(",");
                        // 計量日時
                        s = DBManager.convert((Calendar) columns.get("gate_out_date")).toString();
                        sb = sb.append(s).append(",");
                        // 車番
                        Integer i = (Integer) columns.get("car_no");
                        sb = sb.append(i).append(",");
                        // 正味重量
                        i = (Integer) columns.get("net_weight");
                        sb = sb.append(i).append(",");
                        // 請求先
                        s = (String) columns.get("bill_name");
                        sb = sb.append(s).append(",");
                        // 排出事業者
                        s = (String) columns.get("entrepreneur_name");
                        sb = sb.append(s).append(",");
                        // 運搬業者
                        s = (String) columns.get("transporter_name");
                        sb = sb.append(s).append(",");
                        // 品目
                        s = (String) columns.get("goods_name");
                        sb = sb.append(s).append(",");
                        //単位
                        s = (String) columns.get("unit_name");
                        sb = sb.append(s).append(",");
                        //取扱区分
                        s = (String) columns.get("goods_option_name");
                        sb = sb.append(s).append(",");
                        //carry_name
                        s = (String) columns.get("carry_name");
                        sb = sb.append(s).append(",");
                        // 排出事業場
                        s = columns.get("office_name") != null ? (String) columns.get("office_name") : "";
                        sb = sb.append(s).append(",");
                        s = columns.get("office_sub_name") != null ? (String) columns.get("office_sub_name") : "";
                        sb = sb.append(s).append(",");
                        // 処分業者
                        s = (String) columns.get("contractor_name");
                        sb = sb.append(s).append(",");
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
