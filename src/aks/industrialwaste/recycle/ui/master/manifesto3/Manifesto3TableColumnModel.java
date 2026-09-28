package aks.industrialwaste.recycle.ui.master.manifesto3;

import java.text.DecimalFormat;
import javax.swing.table.DefaultTableColumnModel;
import javax.swing.table.TableColumn;
import jp.co.astax.swing.table.NumberTableCellRenderer;

public class Manifesto3TableColumnModel extends DefaultTableColumnModel {

    public Manifesto3TableColumnModel() {
        // ID
        TableColumn col = new TableColumn(Manifesto3TableModel.COLUMN_CONT_ID);
        col.setHeaderValue("業者コード");
        col.setPreferredWidth(80);
        col.setCellRenderer(
                 new NumberTableCellRenderer(
                          new DecimalFormat("######"),
                          NumberTableCellRenderer.RIGHT));
        addColumn(col);
        // 名称
        col = new TableColumn(Manifesto3TableModel.COLUMN_CONT_NAME);
        col.setHeaderValue("業者名称");
        col.setPreferredWidth(200);
        addColumn(col);
        //ID
        col = new TableColumn(Manifesto3TableModel.COLUMN_ID);
        col.setHeaderValue("ID");
        col.setPreferredWidth(60);
        col.setCellRenderer(
                 new NumberTableCellRenderer(
                          new DecimalFormat("######"),
                          NumberTableCellRenderer.RIGHT));
        addColumn(col);
        // 名称
        col = new TableColumn(Manifesto3TableModel.COLUMN_NAME);
        col.setHeaderValue("処分場名称");
        col.setPreferredWidth(250);
        addColumn(col);
        // 所在地1
        col = new TableColumn(Manifesto3TableModel.COLUMN_ADDRESS1);
        col.setHeaderValue("住所１");
        col.setPreferredWidth(400);
        addColumn(col);
        // 所在地1
        col = new TableColumn(Manifesto3TableModel.COLUMN_ADDRESS2);
        col.setHeaderValue("住所２");
        col.setPreferredWidth(100);
//    addColumn(col);
        // 郵便番号
        col = new TableColumn(Manifesto3TableModel.COLUMN_ZIP);
        col.setHeaderValue("郵便番号");
        col.setPreferredWidth(100);
        addColumn(col);
        // TEL
        col = new TableColumn(Manifesto3TableModel.COLUMN_TEL);
        col.setHeaderValue("TEL");
        col.setPreferredWidth(120);
        addColumn(col);
    }
}
