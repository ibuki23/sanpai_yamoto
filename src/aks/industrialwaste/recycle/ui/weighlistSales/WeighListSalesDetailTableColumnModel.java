package aks.industrialwaste.recycle.ui.weighlistSales;

import aks.swing.table.BooleanIntegerTableCellRenderer;
import java.awt.Color;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import javax.swing.table.DefaultTableColumnModel;
import javax.swing.table.TableColumn;
import jp.co.astax.swing.table.CalendarTableCellRenderer;
import jp.co.astax.swing.table.NumberTableCellRenderer;
import javax.swing.table.DefaultTableCellRenderer;

public class WeighListSalesDetailTableColumnModel extends DefaultTableColumnModel {

    public WeighListSalesDetailTableColumnModel() {
        DefaultTableCellRenderer r = new DefaultTableCellRenderer();
        r.setBackground(new Color(255, 153, 153));

        // 計量日時 時間
        TableColumn col = new TableColumn(WeighListSalesDetailTableModel.COLUMN_GATE_OUT_DATE);
        col.setHeaderValue("計量日時");
        col.setPreferredWidth(130);
        col.setCellRenderer(
                 new CalendarTableCellRenderer(
                          new SimpleDateFormat("yy/MM/dd HH:mm"),
                          NumberTableCellRenderer.LEFT));
        addColumn(col);

        // 排出事業者
        col = new TableColumn(WeighListSalesDetailTableModel.COLUMN_ENT_NAMRE);
        col.setHeaderValue("排出事業者");
        col.setPreferredWidth(150);
        addColumn(col);
        // 現場
        col = new TableColumn(WeighListSalesDetailTableModel.COLUMN_OFFICE_NAME);
        col.setHeaderValue("現場");
        col.setPreferredWidth(150);
        addColumn(col);
        // 運搬業者
        col = new TableColumn(WeighListSalesDetailTableModel.COLUMN_TRANS_NAME);
        col.setHeaderValue("運搬業者");
        col.setPreferredWidth(100);
        addColumn(col);
        // 品目
        col = new TableColumn(WeighListSalesDetailTableModel.COLUMN_GOOS_NAME);
        col.setHeaderValue("品目");
        col.setPreferredWidth(140);
        addColumn(col);
        // 車番
        col = new TableColumn(WeighListSalesDetailTableModel.COLUMN_CAR_NO);
        col.setHeaderValue("車番");
        col.setPreferredWidth(60);
        col.setCellRenderer(
                 new NumberTableCellRenderer(
                          new DecimalFormat("######"),
                          NumberTableCellRenderer.RIGHT));
        addColumn(col);
        //  正味重量
        col = new TableColumn(WeighListSalesDetailTableModel.COLUMN_NET_WEIGHT);
        col.setHeaderValue("正味重量");
        col.setPreferredWidth(80);
        col.setCellRenderer(
                 new NumberTableCellRenderer(
                          new DecimalFormat("#,###,##0"),
                          NumberTableCellRenderer.RIGHT));
        addColumn(col);
        // 数量ｍ3
        col = new TableColumn(WeighListSalesDetailTableModel.COLUMN_M3);
        col.setHeaderValue("数量");
        col.setPreferredWidth(60);
        col.setCellRenderer(
                 new NumberTableCellRenderer(
                          new DecimalFormat("###,##0.##"),
                          NumberTableCellRenderer.RIGHT));
        addColumn(col);
        // 単位
        col = new TableColumn(WeighListSalesDetailTableModel.COLUMN_UNIT_NAME);
        col.setHeaderValue("単位");
        col.setPreferredWidth(40);
        addColumn(col);
        //  単価
        col = new TableColumn(WeighListSalesDetailTableModel.COLUMN_UNIT);
        col.setHeaderValue("単価");
        col.setPreferredWidth(70);
        col.setCellRenderer(
                 new NumberTableCellRenderer(
                          new DecimalFormat("#,###,##0.##"),
                          NumberTableCellRenderer.RIGHT));
        addColumn(col);
        //  金額
        col = new TableColumn(WeighListSalesDetailTableModel.COLUMN_CHARGE_VALUE);
        col.setHeaderValue("金額");
        col.setPreferredWidth(80);
        col.setCellRenderer(
                 new NumberTableCellRenderer(
                          new DecimalFormat("#,###,##0"),
                          NumberTableCellRenderer.RIGHT));
        addColumn(col);
        //現金受領済み
        col = new TableColumn(WeighListSalesDetailTableModel.COLUMN_現金受領済み);
        col.setHeaderValue("現金済");
        col.setPreferredWidth(70);
        col.setCellRenderer(new BooleanIntegerTableCellRenderer());
        // マニフェスト有
        col = new TableColumn(WeighListSalesDetailTableModel.COLUMN_MANIFESTO_NO);
        col.setHeaderValue("管理票");
        col.setPreferredWidth(120);
        addColumn(col);
    }
}
