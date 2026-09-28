package aks.industrialwaste.recycle.ui.bill_list;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import javax.swing.table.DefaultTableColumnModel;
import javax.swing.table.TableColumn;
import jp.co.astax.swing.table.CalendarTableCellRenderer;
import jp.co.astax.swing.table.NumberTableCellRenderer;

public class BillListTableColumnModel extends DefaultTableColumnModel {

    public BillListTableColumnModel() {
        // 得意先ID
        TableColumn col = new TableColumn(BillListTableModel.COLUMN_BILL_ID);
        col.setHeaderValue("ID");
        col.setPreferredWidth(60);
        addColumn(col);
        // 請求先名
        col = new TableColumn(BillListTableModel.COLUMN_BILL_NAME);
        col.setHeaderValue("請求先名");
        col.setPreferredWidth(180);
        addColumn(col);
        // 請求所属日時
        col = new TableColumn(BillListTableModel.COLUMN_TERM_DATE);
        col.setHeaderValue("請求日");
        col.setPreferredWidth(100);
        col.setCellRenderer(
                 new CalendarTableCellRenderer(
                          new SimpleDateFormat("yyyy/MM/dd"),
                          NumberTableCellRenderer.LEFT));
        addColumn(col);
        // 起算開始日
        col = new TableColumn(BillListTableModel.COLUMN_START_DATE);
        col.setHeaderValue("開始日");
        col.setPreferredWidth(80);
        col.setCellRenderer(
                 new CalendarTableCellRenderer(
                          new SimpleDateFormat("MM/dd～"),
                          NumberTableCellRenderer.RIGHT));
        addColumn(col);
        // 起算終了日
        col = new TableColumn(BillListTableModel.COLUMN_END_DATE);
        col.setHeaderValue("終了日");
        col.setPreferredWidth(70);
        col.setCellRenderer(
                 new CalendarTableCellRenderer(
                          new SimpleDateFormat("MM/dd"),
                          NumberTableCellRenderer.LEFT));
        addColumn(col);
        // 請求番号
        col = new TableColumn(BillListTableModel.COLUMN_REPORT_NO);
        col.setHeaderValue("請求番号");
        col.setPreferredWidth(120);
        addColumn(col);

        // 前月請求額
        col = new TableColumn(BillListTableModel.COLUMN_VOLUE1);
        col.setHeaderValue("前回請求額");
        col.setPreferredWidth(100);
        col.setCellRenderer(
                 new NumberTableCellRenderer(
                          new DecimalFormat("#,###,##0"),
                          NumberTableCellRenderer.RIGHT));
        addColumn(col);
        // 入金額
        col = new TableColumn(BillListTableModel.COLUMN_VOLUE2);
        col.setHeaderValue("入金額");
        col.setPreferredWidth(100);
        col.setCellRenderer(
                 new NumberTableCellRenderer(
                          new DecimalFormat("#,###,##0"),
                          NumberTableCellRenderer.RIGHT));
        addColumn(col);

        // 繰越金額
        col = new TableColumn(BillListTableModel.COLUMN_VOLUE4);
        col.setHeaderValue("繰越金額");
        col.setPreferredWidth(100);
        col.setCellRenderer(
                 new NumberTableCellRenderer(
                          new DecimalFormat("#,###,##0"),
                          NumberTableCellRenderer.RIGHT));
        addColumn(col);
        // 売上額
        col = new TableColumn(BillListTableModel.COLUMN_VOLUE5);
        col.setHeaderValue("今回売上額");
        col.setPreferredWidth(120);
        col.setCellRenderer(
                 new NumberTableCellRenderer(
                          new DecimalFormat("#,###,##0"),
                          NumberTableCellRenderer.RIGHT));
        addColumn(col);
        // 消費税額
        col = new TableColumn(BillListTableModel.COLUMN_VOLUE6);
        col.setHeaderValue("消費税額");
        col.setPreferredWidth(100);
        col.setCellRenderer(
                 new NumberTableCellRenderer(
                          new DecimalFormat("#,###,##0"),
                          NumberTableCellRenderer.RIGHT));
        addColumn(col);
        // 産業廃棄物税
        col = new TableColumn(BillListTableModel.COLUMN_VOLUE9);
        col.setHeaderValue("産廃税");
        col.setPreferredWidth(100);
        col.setCellRenderer(
                 new NumberTableCellRenderer(
                          new DecimalFormat("#,###,##0"),
                          NumberTableCellRenderer.RIGHT));
        addColumn(col);
        // 当月請求額
        col = new TableColumn(BillListTableModel.COLUMN_TOTAL);
        col.setHeaderValue("御請求額");
        col.setPreferredWidth(120);
        col.setCellRenderer(
                 new NumberTableCellRenderer(
                          new DecimalFormat("#,###,##0"),
                          NumberTableCellRenderer.RIGHT));
        //font18
        addColumn(col);
    }
}
