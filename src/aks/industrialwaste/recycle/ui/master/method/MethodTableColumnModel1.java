package aks.industrialwaste.recycle.ui.master.method;

import java.text.DecimalFormat;
import javax.swing.table.DefaultTableColumnModel;
import javax.swing.table.TableColumn;
import jp.co.astax.swing.table.NumberTableCellRenderer;

public class MethodTableColumnModel1 extends DefaultTableColumnModel {

    public MethodTableColumnModel1() {
        // ID
        TableColumn col = new TableColumn(MethodTableModel.COLUMN_ID);
        col.setHeaderValue("ID");
        col.setPreferredWidth(80);
        col.setCellRenderer(
                 new NumberTableCellRenderer(
                          new DecimalFormat("######"),
                          NumberTableCellRenderer.RIGHT));
        addColumn(col);
        // 処分方法
        col = new TableColumn(MethodTableModel.COLUMN_NAME);
        col.setHeaderValue("処分方法");
        col.setPreferredWidth(180);
        addColumn(col);

    }
}
