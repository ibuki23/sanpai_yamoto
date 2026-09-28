package aks.industrialwaste.recycle.ui.master.bill;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import javax.swing.table.AbstractTableModel;

public class GoodsListTableModel extends AbstractTableModel {

    public static final int COLUMNS = 3;
    public static final int COLUMN_ID = 0;
    public static final int COLUMN_NAME = 1;
    public static final int COLUMN_UNIT_VALUE = 2;
    private static final String[] COLUMN_NAMES = {"id", "name", "unit_value"};

    protected static Logger logger_
             = Logger.getLogger(GoodsListTableModel.class.getName());
    private Connection con_;
    private int userSNo_;
    private List<Map<String, Object>> rows_
             = Collections.synchronizedList(new ArrayList<Map<String, Object>>());

    public GoodsListTableModel(Connection con, int userSNo) {
        con_ = con;
        userSNo_ = Math.max(0, userSNo);
    }

    @Override
    public int getRowCount() {
        return rows_.size();
    }

    public int getColumnCount() {
        return COLUMNS;
    }

    @Override
    public boolean isCellEditable(int row, int column) {
        return (COLUMN_UNIT_VALUE == column);
    }

    @Override
    public Object getValueAt(int row, int column) {
        Map<String, Object> columns = rows_.get(row);

        return columns.get(COLUMN_NAMES[column]);
    }

    @Override
    public void setValueAt(Object value, int row, int column) {
        Map<String, Object> columns = rows_.get(row);
        columns.put(COLUMN_NAMES[column], value);
    }

    public void clear() {
        rows_.clear();
    }
}
