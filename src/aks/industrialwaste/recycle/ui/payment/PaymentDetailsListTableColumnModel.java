package aks.industrialwaste.recycle.ui.payment;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import javax.swing.table.DefaultTableColumnModel;
import javax.swing.table.TableColumn;
import jp.co.astax.swing.table.CalendarTableCellRenderer;
import jp.co.astax.swing.table.NumberTableCellRenderer;

public class PaymentDetailsListTableColumnModel extends DefaultTableColumnModel {

    public PaymentDetailsListTableColumnModel() {

        // 得意先ID
        TableColumn col = new TableColumn(PaymentDetailsListTableModel.COLUMN_BILL_ID);
        col.setHeaderValue("ID");
        col.setPreferredWidth(60);
        addColumn(col);
        // 得意先名
        col = new TableColumn(PaymentDetailsListTableModel.COLUMN_M_BILL_NAME);
        col.setHeaderValue("請求先名");
        col.setPreferredWidth(180);
        addColumn(col);
        // 入金日
        col = new TableColumn(PaymentDetailsListTableModel.COLUMN_PAYMENT_DATE);
        col.setHeaderValue("入金日");
        col.setPreferredWidth(100);
        col.setCellRenderer(
                 new CalendarTableCellRenderer(
                          new SimpleDateFormat("yyyy/MM/dd"),
                          NumberTableCellRenderer.LEFT));
        addColumn(col);
        // 取引ID
        col = new TableColumn(PaymentDetailsListTableModel.COLUMN_P_CLASS_ID);
        col.setHeaderValue("ID");
        col.setPreferredWidth(50);
        addColumn(col);
        // 取引区分
        col = new TableColumn(PaymentDetailsListTableModel.COLUMN_P_CLASS_NAME);
        col.setHeaderValue("取引区分");
        col.setPreferredWidth(130);
        addColumn(col);
        // 入金額
        col = new TableColumn(PaymentDetailsListTableModel.COLUMN_PAYMENT_PRICE);
        col.setHeaderValue("入金額");
        col.setPreferredWidth(100);
        col.setCellRenderer(
                 new NumberTableCellRenderer(
                          new DecimalFormat("###,###,##0"),
                          NumberTableCellRenderer.RIGHT));
        addColumn(col);
        // 入金額
        col = new TableColumn(PaymentDetailsListTableModel.COLUMN_COMMISSION);
        col.setHeaderValue("手数料等");
        col.setPreferredWidth(80);
        col.setCellRenderer(
                 new NumberTableCellRenderer(
                          new DecimalFormat("###,###,##0"),
                          NumberTableCellRenderer.RIGHT));
        addColumn(col);
        // 摘要１
        col = new TableColumn(PaymentDetailsListTableModel.COLUMN_REMARK01);
        col.setHeaderValue("摘要");
        col.setPreferredWidth(150);
        addColumn(col);
    }
}
