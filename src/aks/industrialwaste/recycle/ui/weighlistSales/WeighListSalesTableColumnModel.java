package aks.industrialwaste.recycle.ui.weighlistSales;

import aks.swing.table.BooleanIntegerTableCellRenderer;
import java.awt.Color;
import java.text.DecimalFormat;
import javax.swing.table.DefaultTableColumnModel;
import javax.swing.table.TableColumn;
import jp.co.astax.swing.table.NumberTableCellRenderer;
import javax.swing.table.DefaultTableCellRenderer;

public class WeighListSalesTableColumnModel extends DefaultTableColumnModel {

    public WeighListSalesTableColumnModel() {
        DefaultTableCellRenderer r = new DefaultTableCellRenderer();
        r.setBackground(new Color(255, 153, 153));

        // 請求先ID
        TableColumn col = new TableColumn(WeighListSalesTableModel.COLUMN_BILL_ID);
        col.setHeaderValue("ID");
        col.setPreferredWidth(60);
        col.setCellRenderer(
                 new NumberTableCellRenderer(
                          new DecimalFormat("####"),
                          NumberTableCellRenderer.LEFT));
        addColumn(col);

        // 請求先 モードで項目出力
        col = new TableColumn(WeighListSalesTableModel.COLUMN_BILL_NAME);
        col.setHeaderValue("得意先");
        col.setPreferredWidth(180);
        addColumn(col);

        // 請求No
        col = new TableColumn(WeighListSalesTableModel.COLUMN_BILL_NO);
        col.setHeaderValue("請求書No");
        col.setPreferredWidth(100);
        addColumn(col);

        //  料金
        col = new TableColumn(WeighListSalesTableModel.COLUMN_SUM_CHARGE_VALUE);
        col.setHeaderValue("合計金額");
        col.setPreferredWidth(100);
        col.setCellRenderer(
                 new NumberTableCellRenderer(
                          new DecimalFormat("#,###,##0"),
                          NumberTableCellRenderer.RIGHT));
        addColumn(col);

        col = new TableColumn(WeighListSalesTableModel.COLUMN_SUM_TAX_VALUE);
        col.setHeaderValue("消費税");
        col.setPreferredWidth(80);
        col.setCellRenderer(
                 new NumberTableCellRenderer(
                          new DecimalFormat("#,###,##0"),
                          NumberTableCellRenderer.RIGHT));
        addColumn(col);

        col = new TableColumn(WeighListSalesTableModel.COLUMN_SUM_CHARGE_TAX_VALUE);
        col.setHeaderValue("料金合計");
        col.setPreferredWidth(100);
        col.setCellRenderer(
                 new NumberTableCellRenderer(
                          new DecimalFormat("#,###,##0"),
                          NumberTableCellRenderer.RIGHT));
        addColumn(col);

        //現金受領済み
        col = new TableColumn(WeighListSalesTableModel.COLUMN_現金受領済み);
        col.setHeaderValue("現金");
        col.setPreferredWidth(70);
        col.setCellRenderer(new BooleanIntegerTableCellRenderer());

    }
}
