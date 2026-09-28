package aks.industrialwaste.recycle.ui.weighlistSales;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
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

public class WeighListSalesDetailTableModel extends AbstractTableModel {

    public static final int COLUMN_COUNT = 67;
    public static final int COLUMN_TERM_DATE = 0;
    public static final int COLUMN_MANIFESTO_NO = 1;
    public static final int COLUMN_CAR_NO = 2;
    public static final int COLUMN_ENT_NAMRE = 3;
    public static final int COLUMN_TRANS_NAME = 4;
    public static final int COLUMN_GOOS_NAME = 5;
    public static final int COLUMN_OFFICE_NAME = 6;
    public static final int COLUMN_CONT_NAME = 7;
    public static final int COLUMN_CAR_KIND_NAME = 8;
    public static final int COLUMN_NET_WEIGHT = 9;
    public static final int COLUMN_UNIT = 10;
    public static final int COLUMN_CHARGE_VALUE = 11;
    public static final int COLUMN_UNIT_NAME = 12;
    public static final int COLUMN_M3 = 13;
    public static final int COLUMN_GATE_OUT_DATE = 14;
    public static final int COLUMN_現金受領済み = 15;

    private static final String[] COLUMN_NAMES = {
        "term_date",
        "manifesto_no",
        "car_no",
        "entrepreneur_name",
        "transporter_name",
        "goods_bill_name",//5
        "office_name",
        "contractor_name",
        "car_kind_name",
        "net_weight",
        "unit",//10
        "charge_value",
        "unit_name",
        "m3",
        "gate_out_date",
        "option2"
    };
    protected static Logger logger_
             = Logger.getLogger(WeighListSalesDetailTableModel.class.getName());
    private Connection con_;
    private List<Map<String, Object>> rows_
             = Collections.synchronizedList(new ArrayList<>());

    public WeighListSalesDetailTableModel(Connection con) {
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

    public void refresh(Date d1, Date d2, Integer id, Integer option2, SortCondition[] sort) {
        clear();
        try {
            PreparedStatement ps
                     = DBManager.prepareStatement(
                              con_,
                              "f1_得意先売上明細表(?, ?, ?) where option2 = ? and dis_ent_id > 0 ",
                              "*",
                              (new SearchCondition[0]),
                              (null == sort) ? (new SortCondition[0]) : (sort),
                              0);
            try {
                ps.setObject(1, d1);
                ps.setObject(2, d2);
                ps.setObject(3, id);
                ps.setObject(4, option2);
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

}
