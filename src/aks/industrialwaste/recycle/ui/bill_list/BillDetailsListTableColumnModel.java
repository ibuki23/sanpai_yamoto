package aks.industrialwaste.recycle.ui.bill_list;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import javax.swing.table.DefaultTableColumnModel;
import javax.swing.table.TableColumn;
import jp.co.astax.swing.table.CalendarTableCellRenderer;
import jp.co.astax.swing.table.NumberTableCellRenderer;

public class BillDetailsListTableColumnModel extends DefaultTableColumnModel {

    public BillDetailsListTableColumnModel() {
        // 現場ID
        TableColumn col = new TableColumn(BillDetailsListTableModel.COLUMN_OFFICE_ID);
        col.setHeaderValue("ID");
        col.setPreferredWidth(50);

        // 売上日
        col = new TableColumn(BillDetailsListTableModel.COLUMN_TERM_DATE);
        col.setHeaderValue("売上日");
        col.setPreferredWidth(60);
        col.setCellRenderer(
                 new CalendarTableCellRenderer(
                          new SimpleDateFormat("MM/dd"),
                          NumberTableCellRenderer.LEFT));
        addColumn(col);

        // 品目
        col = new TableColumn(BillDetailsListTableModel.COLUMN_BILL_NAME);
        col.setHeaderValue("品名");
        col.setPreferredWidth(180);
        addColumn(col);

        // 発地（現場）
        col = new TableColumn(BillDetailsListTableModel.COLUMN_OFFICE_NAME2);
        col.setHeaderValue("発地（現場名）");
        col.setPreferredWidth(200);
        addColumn(col);
        // 着地（処分業者）
        col = new TableColumn(BillDetailsListTableModel.COLUMN_OFFICE_NAME);
        col.setHeaderValue("着地（処分業者）");
        col.setPreferredWidth(180);

        // 車番
        col = new TableColumn(BillDetailsListTableModel.COLUMN_CAR_NO);
        col.setHeaderValue("車番");
        col.setPreferredWidth(60);
        col.setCellRenderer(
                 new NumberTableCellRenderer(
                          new DecimalFormat("####"),
                          NumberTableCellRenderer.RIGHT));
        // 数量
        col = new TableColumn(BillDetailsListTableModel.COLUMN_BILL_VALUE);
        col.setHeaderValue("数量");
        col.setPreferredWidth(80);
        col.setCellRenderer(
                 new NumberTableCellRenderer(
                          new DecimalFormat("#,###,##0.####"),
                          NumberTableCellRenderer.RIGHT));
        addColumn(col);
        // 単位
        col = new TableColumn(BillDetailsListTableModel.COLUMN_BILL_UNIT);
        col.setHeaderValue("単位");
        col.setPreferredWidth(40);
        addColumn(col);

        // 商品単価
        col = new TableColumn(BillDetailsListTableModel.COLUMN_UNIT_PRICE);
        col.setHeaderValue("単価");
        col.setPreferredWidth(80);
        col.setCellRenderer(
                 new NumberTableCellRenderer(
                          new DecimalFormat("#,###,##0.####"),
                          NumberTableCellRenderer.RIGHT));
        addColumn(col);
        // 金額
        col = new TableColumn(BillDetailsListTableModel.COLUMN_BILL_PRICE);
        col.setHeaderValue("請求金額");
        col.setPreferredWidth(100);
        col.setCellRenderer(
                 new NumberTableCellRenderer(
                          new DecimalFormat("#,###,##0"),
                          NumberTableCellRenderer.RIGHT));
        addColumn(col);
        // 産廃税
        col = new TableColumn(BillDetailsListTableModel.COLUMN_ADVANCES);
        col.setHeaderValue("産廃税");
        col.setPreferredWidth(100);
        col.setCellRenderer(
                 new NumberTableCellRenderer(
                          new DecimalFormat("#,###,##0"),
                          NumberTableCellRenderer.RIGHT));
        addColumn(col);

        // 備考１
        col = new TableColumn(BillDetailsListTableModel.COLUMN_REMARK01);
        col.setHeaderValue("備考");
        col.setPreferredWidth(150);
        addColumn(col);
        // 枝番
        col = new TableColumn(BillDetailsListTableModel.COLUMN_EDA);
        col.setHeaderValue("枝番");
        col.setPreferredWidth(70);
        col.setCellRenderer(
                 new NumberTableCellRenderer(
                          new DecimalFormat("##0"),
                          NumberTableCellRenderer.RIGHT));
        addColumn(col);
    }
}
