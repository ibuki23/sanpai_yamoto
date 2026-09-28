package aks.industrialwaste.recycle.ui.master.transporter;

import java.text.DecimalFormat;
import javax.swing.table.DefaultTableColumnModel;
import javax.swing.table.TableColumn;
import jp.co.astax.swing.table.NumberTableCellRenderer;

public class TransporterTableColumnModel extends DefaultTableColumnModel {

    public TransporterTableColumnModel() {
        // ID
        TableColumn col = new TableColumn(TransporterTableModel.COLUMN_ID);
        col.setHeaderValue("ID");
        col.setPreferredWidth(70);
        col.setCellRenderer(
                 new NumberTableCellRenderer(
                          new DecimalFormat("######"),
                          NumberTableCellRenderer.RIGHT));
        addColumn(col);
        // 名称
        col = new TableColumn(TransporterTableModel.COLUMN_NAME);
        col.setHeaderValue("名称");
        col.setPreferredWidth(180);
        addColumn(col);
        // 名称
        col = new TableColumn(TransporterTableModel.COLUMN_P_NAME);
        col.setHeaderValue("正式名称");
        col.setPreferredWidth(250);
        addColumn(col);
        // 所在地1
        col = new TableColumn(TransporterTableModel.COLUMN_ADDRESS1);
        col.setHeaderValue("所在地");
        col.setPreferredWidth(300);
        addColumn(col);

        // TEL
        col = new TableColumn(TransporterTableModel.COLUMN_TEL);
        col.setHeaderValue("TEL");
        col.setPreferredWidth(150);
        addColumn(col);
        // 行政最
        col = new TableColumn(TransporterTableModel.COLUMN_TAX_KIND_NAME);
        col.setHeaderValue("行政報告");
        col.setPreferredWidth(40);
        // 所在地2
        col = new TableColumn(TransporterTableModel.COLUMN_PERMISSION_NO);
        col.setHeaderValue("許可番号");
        col.setPreferredWidth(150);
        addColumn(col);
    }
}
