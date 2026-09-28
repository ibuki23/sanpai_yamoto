package aks.industrialwaste.recycle.ui.master.bill;

import java.text.DecimalFormat;
import javax.swing.table.DefaultTableColumnModel;
import javax.swing.table.TableColumn;
import jp.co.astax.swing.table.NumberTableCellRenderer;

public class BillTableColumnModel extends DefaultTableColumnModel {

    public BillTableColumnModel() {
        // ID
        TableColumn col = new TableColumn(BillTableModel.COLUMN_ID);
        col.setHeaderValue("ID");
        col.setPreferredWidth(80);
        col.setCellRenderer(
                 new NumberTableCellRenderer(
                          new DecimalFormat("######"),
                          NumberTableCellRenderer.RIGHT));
        addColumn(col);
        // 名称
        col = new TableColumn(BillTableModel.COLUMN_NAME);
        col.setHeaderValue("名称");
        col.setPreferredWidth(300);
        addColumn(col);
        //正式名称
        col = new TableColumn(BillTableModel.COLUMN_OFFICE_P_NAME);
        col.setHeaderValue("正式名称");
        col.setPreferredWidth(400);
        addColumn(col);
        //敬称
        col = new TableColumn(BillTableModel.COLUMN_KEISYO);
        col.setHeaderValue("敬称");
        col.setPreferredWidth(60);
        addColumn(col);
        // 請求業者　所在地1
        col = new TableColumn(BillTableModel.COLUMN_ADDRESS1);
        col.setHeaderValue("所在地1");
        col.setPreferredWidth(300);
        addColumn(col);
        // 請求業者　所在地2
        col = new TableColumn(BillTableModel.COLUMN_ADDRESS2);
        col.setHeaderValue("所在地2");
        col.setPreferredWidth(180);
//    addColumn(col);
        // 請求業者　TEL
        col = new TableColumn(BillTableModel.COLUMN_CON_TEL);
        col.setHeaderValue("TEL");
        col.setPreferredWidth(120);
        addColumn(col);

        // 郵便番号
        col = new TableColumn(BillTableModel.COLUMN_ZIP);
        col.setHeaderValue("郵便番号");
        col.setPreferredWidth(90);
        addColumn(col);

        // 締日
        col = new TableColumn(BillTableModel.COLUMN_BUNDLE);
        col.setHeaderValue("締日");
        col.setPreferredWidth(90);
        addColumn(col);

        // 端数処理
        col = new TableColumn(BillTableModel.COLUMN_TAX_NO_NAME);
        col.setHeaderValue("端数処理");
        col.setPreferredWidth(90);
        addColumn(col);

        // 売上区分
        col = new TableColumn(BillTableModel.COLUMN_売上区分);
        col.setHeaderValue("売上区分");
        col.setPreferredWidth(70);
        addColumn(col);
    }
}
