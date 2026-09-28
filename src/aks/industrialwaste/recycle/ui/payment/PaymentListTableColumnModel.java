package aks.industrialwaste.recycle.ui.payment;

import aks.industrialwaste.recycle.ui.bill_list.*;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import javax.swing.table.DefaultTableColumnModel;
import javax.swing.table.TableColumn;
import jp.co.astax.swing.table.CalendarTableCellRenderer;
import jp.co.astax.swing.table.NumberTableCellRenderer;

public class PaymentListTableColumnModel extends DefaultTableColumnModel {

    public PaymentListTableColumnModel() {
        // 得意先ID
        TableColumn col = new TableColumn(PaymentListTableModel.COLUMN_BILL_ID);
        col.setHeaderValue("ID");
        col.setPreferredWidth(60);
        addColumn(col);
        // 請求先名
        col = new TableColumn(PaymentListTableModel.COLUMN_BILL_NAME);
        col.setHeaderValue("請求先名");
        col.setPreferredWidth(180);
        addColumn(col);
        // 請求所属日時
        col = new TableColumn(PaymentListTableModel.COLUMN_TERM_DATE);
        col.setHeaderValue("請求日");
        col.setPreferredWidth(100);
        col.setCellRenderer(
                 new CalendarTableCellRenderer(
                          new SimpleDateFormat("yyyy/MM/dd"),
                          NumberTableCellRenderer.LEFT));
        addColumn(col);
        // 起算開始日
        col = new TableColumn(PaymentListTableModel.COLUMN_START_DATE);
        col.setHeaderValue("開始日");
        col.setPreferredWidth(80);
        col.setCellRenderer(
                 new CalendarTableCellRenderer(
                          new SimpleDateFormat("MM/dd～"),
                          NumberTableCellRenderer.RIGHT));
        addColumn(col);
        // 起算終了日
        col = new TableColumn(PaymentListTableModel.COLUMN_END_DATE);
        col.setHeaderValue("終了日");
        col.setPreferredWidth(70);
        col.setCellRenderer(
                 new CalendarTableCellRenderer(
                          new SimpleDateFormat("MM/dd"),
                          NumberTableCellRenderer.LEFT));
        addColumn(col);
        // 前回請求
        col = new TableColumn(PaymentListTableModel.COLUMN_VOLUE1);
        col.setHeaderValue("前回請求額");
        col.setPreferredWidth(90);
        col.setCellRenderer(
                 new NumberTableCellRenderer(
                          new DecimalFormat("#,###,##0"),
                          NumberTableCellRenderer.RIGHT));
        addColumn(col);
        // 入金額
        col = new TableColumn(PaymentListTableModel.COLUMN_VOLUE2);
        col.setHeaderValue("今回入金額");
        col.setPreferredWidth(90);
        col.setCellRenderer(
                 new NumberTableCellRenderer(
                          new DecimalFormat("#,###,##0"),
                          NumberTableCellRenderer.RIGHT));
        addColumn(col);
        // 調整額
        col = new TableColumn(PaymentListTableModel.COLUMN_VOLUE3);
        col.setHeaderValue("今回値引額");
        col.setPreferredWidth(90);
        col.setCellRenderer(
                 new NumberTableCellRenderer(
                          new DecimalFormat("#,###,##0"),
                          NumberTableCellRenderer.RIGHT));
        addColumn(col);
        // 繰越額
        col = new TableColumn(PaymentListTableModel.COLUMN_VOLUE4);
        col.setHeaderValue("繰越額");
        col.setPreferredWidth(90);
        col.setCellRenderer(
                 new NumberTableCellRenderer(
                          new DecimalFormat("#,###,##0"),
                          NumberTableCellRenderer.RIGHT));
        addColumn(col);
        // 売上額
        col = new TableColumn(PaymentListTableModel.COLUMN_VOLUE5);
        col.setHeaderValue("今回売上額");
        col.setPreferredWidth(90);
        col.setCellRenderer(
                 new NumberTableCellRenderer(
                          new DecimalFormat("#,###,##0"),
                          NumberTableCellRenderer.RIGHT));
        addColumn(col);
        // 消費税
        col = new TableColumn(BillListTableModel.COLUMN_VOLUE6);
        col.setHeaderValue("今回消費税");
        col.setPreferredWidth(90);
        col.setCellRenderer(
                 new NumberTableCellRenderer(
                          new DecimalFormat("#,###,##0"),
                          NumberTableCellRenderer.RIGHT));
        // 今回請求
        col = new TableColumn(PaymentListTableModel.COLUMN_TOTAL);
        col.setHeaderValue("今回請求額");
        col.setPreferredWidth(90);
        col.setCellRenderer(
                 new NumberTableCellRenderer(
                          new DecimalFormat("#,###,##0"),
                          NumberTableCellRenderer.RIGHT));
        addColumn(col);

        // 今回請求
        col = new TableColumn(PaymentListTableModel.COLUMN_RECOVERY_RATE);
        col.setHeaderValue("回収率");
        col.setPreferredWidth(70);
        col.setCellRenderer(
                 new NumberTableCellRenderer(
                          new DecimalFormat("##0.00"),
                          NumberTableCellRenderer.RIGHT));
        addColumn(col);
    }
}
