package aks.industrialwaste.recycle.ui.master.trans_unit;

import java.text.DecimalFormat;
import javax.swing.table.DefaultTableColumnModel;
import javax.swing.table.TableColumn;
import jp.co.astax.swing.table.NumberTableCellRenderer;

public class TransUnitTableColumnModel extends DefaultTableColumnModel {

    public TransUnitTableColumnModel() {
        // ID
        TableColumn col = new TableColumn(TransUnitTableModel.COLUMN_ID);
        col.setHeaderValue("ID");
        col.setPreferredWidth(80);
        col.setCellRenderer(
                 new NumberTableCellRenderer(
                          new DecimalFormat("######"),
                          NumberTableCellRenderer.RIGHT));
        addColumn(col);
        // 名称
        col = new TableColumn(TransUnitTableModel.COLUMN_UNIT);
        col.setHeaderValue("輸送単価");
        col.setPreferredWidth(180);
        addColumn(col);

    }
}
