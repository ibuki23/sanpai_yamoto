package aks.industrialwaste.recycle.ui.master.trans_unit;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import javax.swing.table.AbstractTableModel;

public class TransUnitTableModel extends AbstractTableModel {

    public static final int COLUMN_COUNT = 2;
    public static final int COLUMN_ID = 0;
    public static final int COLUMN_UNIT = 1;
    private static final String[] COLUMN_NAMES
             = {"id", "unit"};

    protected static Logger logger_
             = Logger.getLogger(TransUnitTableModel.class.getName());
    private Connection con_;
    private List<Map<String, Object>> rows_
             = Collections.synchronizedList(new ArrayList<Map<String, Object>>());

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
}
