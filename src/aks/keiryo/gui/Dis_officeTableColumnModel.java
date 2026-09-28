package aks.keiryo.gui;

import java.text.DecimalFormat;
import javax.swing.table.DefaultTableColumnModel;
import javax.swing.table.TableColumn;
import jp.co.astax.swing.table.NumberTableCellRenderer;

public class Dis_officeTableColumnModel extends DefaultTableColumnModel {

    public Dis_officeTableColumnModel() {
        // ID
        TableColumn col = new TableColumn(Dis_officeTableModel.COLUMN_ENT_ID);
        col.setHeaderValue("コード");
        col.setPreferredWidth(60);
        col.setCellRenderer(
                 new NumberTableCellRenderer(
                          new DecimalFormat("######"),
                          NumberTableCellRenderer.RIGHT));
        addColumn(col);
        // 名称
        col = new TableColumn(Dis_officeTableModel.COLUMN_ENT_NAME);
        col.setHeaderValue("契約先名称");
        col.setPreferredWidth(150);
        addColumn(col);
        // 所在地1
        col = new TableColumn(Dis_officeTableModel.COLUMN_ENT_ADDRESS1);
        col.setHeaderValue("契約先住所");
        col.setPreferredWidth(180);
        addColumn(col);
        // ID
        col = new TableColumn(Dis_officeTableModel.COLUMN_ID);
        col.setHeaderValue("コード");
        col.setPreferredWidth(60);
        col.setCellRenderer(
                 new NumberTableCellRenderer(
                          new DecimalFormat("######"),
                          NumberTableCellRenderer.RIGHT));
        addColumn(col);
        // 名称
        col = new TableColumn(Dis_officeTableModel.COLUMN_NAME);
        col.setHeaderValue("現場名称");
        col.setPreferredWidth(280);
        addColumn(col);
        // 所在地1
        col = new TableColumn(Dis_officeTableModel.COLUMN_ADDRESS1);
        col.setHeaderValue("現場　所在地");
        col.setPreferredWidth(200);
        addColumn(col);
    }
}
