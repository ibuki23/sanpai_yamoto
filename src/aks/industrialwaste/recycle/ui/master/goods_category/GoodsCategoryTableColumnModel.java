package aks.industrialwaste.recycle.ui.master.goods_category;

import java.text.DecimalFormat;
import javax.swing.table.DefaultTableColumnModel;
import javax.swing.table.TableColumn;
import jp.co.astax.swing.table.NumberTableCellRenderer;

public class GoodsCategoryTableColumnModel extends DefaultTableColumnModel {

    public GoodsCategoryTableColumnModel() {
        // ID
        TableColumn col = new TableColumn(GoodsCategoryTableModel.COLUMN_SNO);
        col.setHeaderValue("ID");
        col.setPreferredWidth(80);
        col.setCellRenderer(
                 new NumberTableCellRenderer(
                          new DecimalFormat("######"),
                          NumberTableCellRenderer.RIGHT));
        addColumn(col);
        // 名称
        col = new TableColumn(GoodsCategoryTableModel.COLUMN_NAME);
        col.setHeaderValue("名称");
        col.setPreferredWidth(180);
        addColumn(col);
    }
}
