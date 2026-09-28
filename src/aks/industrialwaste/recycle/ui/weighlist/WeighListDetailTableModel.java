package aks.industrialwaste.recycle.ui.weighlist;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import javax.swing.table.AbstractTableModel;
import aks.industrialwaste.database.DBManager;

public class WeighListDetailTableModel extends AbstractTableModel {

    public static final int COLUMN_COUNT = 36;
    public static final int COLUMN_SNO = 0;
    public static final int COLUMN_WEIGHT_SNO = 1;
    public static final int COLUMN_ROW_NO = 2;
    public static final int COLUMN_按分品目ID = 3;
    public static final int COLUMN_按分品目名 = 4;
    public static final int COLUMN_按分正味重量 = 5;
    public static final int COLUMN_按分比重 = 6;
    public static final int COLUMN_按分m3 = 7;

    private static final String[] COLUMN_NAMES = {
        "sno",
        "weight_sno",
        "row_no",
        "按分品目id",
        "按分品目名",
        "按分正味重量",//5
        "按分比重",
        "按分m3"
    };

    protected static Logger logger_
             = Logger.getLogger(WeighListDetailTableModel.class.getName());
    private Connection con_;
    private List<Map<String, Object>> rows_
             = Collections.synchronizedList(new ArrayList<Map<String, Object>>());

    public WeighListDetailTableModel(Connection con) {
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

    public void refresh(String s0) {
        clear();
        try {
            PreparedStatement ps = con_.prepareStatement(s0);
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
}
