package aks.industrialwaste.recycle.ui.master.car_kind;

import java.text.DecimalFormat;
import javax.swing.table.DefaultTableColumnModel;
import javax.swing.table.TableColumn;
import jp.co.astax.swing.table.NumberTableCellRenderer;

public class Car_kindTableColumnModel extends DefaultTableColumnModel {

    public Car_kindTableColumnModel() {
        // ID
        TableColumn col = new TableColumn(Car_kindTableModel.COLUMN_ID);
        col.setHeaderValue("ID");
        col.setPreferredWidth(80);
        col.setCellRenderer(
                 new NumberTableCellRenderer(
                          new DecimalFormat("######"),
                          NumberTableCellRenderer.RIGHT));
        addColumn(col);
        // 名称
        col = new TableColumn(Car_kindTableModel.COLUMN_NAME);
        col.setHeaderValue("名称");
        col.setPreferredWidth(180);
        addColumn(col);
    }
}
