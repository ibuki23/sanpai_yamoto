package aks.industrialwaste.recycle.ui.manifesto;

import aks.industrialwaste.recycle.ui.weighlist.WeighListTableModel;
import java.awt.Color;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import javax.swing.table.DefaultTableColumnModel;
import javax.swing.table.TableColumn;
import jp.co.astax.swing.table.CalendarTableCellRenderer;
import jp.co.astax.swing.table.NumberTableCellRenderer;
import javax.swing.table.DefaultTableCellRenderer;

public class ManifestoTableColumnModel extends DefaultTableColumnModel {

    public ManifestoTableColumnModel() {
        DefaultTableCellRenderer r = new DefaultTableCellRenderer();
        r.setBackground(new Color(255, 153, 153));
        // 計量回数
        TableColumn col = new TableColumn(WeighListTableModel.COLUMN_REPORT_NO);
        col.setHeaderValue("回数");
        col.setPreferredWidth(50);
        addColumn(col);

        // 計量日
        col = new TableColumn(WeighListTableModel.COLUMN_TERM_DATE);
        col.setHeaderValue("計量日");
        col.setPreferredWidth(100);
        col.setCellRenderer(
                 new CalendarTableCellRenderer(
                          new SimpleDateFormat("yyyy/MM/dd"),
                          NumberTableCellRenderer.LEFT));
        addColumn(col);

        // 建設系 交付年月日
        col = new TableColumn(WeighListTableModel.COLUMN_GRANT_DATE12);
        col.setHeaderValue("交付日");
        col.setPreferredWidth(100);
        col.setCellRenderer(
                 new CalendarTableCellRenderer(
                          new SimpleDateFormat("yyyy/MM/dd"),
                          NumberTableCellRenderer.LEFT));
        addColumn(col);
        // 交付番号
        col = new TableColumn(WeighListTableModel.COLUMN_MNIFESTO2);
        col.setHeaderValue("建設交付番号");
        col.setPreferredWidth(120);
        col.setCellRenderer(
                 new NumberTableCellRenderer(
                          new DecimalFormat("######"),
                          NumberTableCellRenderer.RIGHT));
        addColumn(col);
        // 交付番号
        col = new TableColumn(WeighListTableModel.COLUMN_MNIFESTO1);
        col.setHeaderValue("産業交付番号");
        col.setPreferredWidth(120);
        col.setCellRenderer(
                 new NumberTableCellRenderer(
                          new DecimalFormat("######"),
                          NumberTableCellRenderer.RIGHT));
        addColumn(col);
        // 排出事業者
        col = new TableColumn(WeighListTableModel.COLUMN_DIS_ENT);
        col.setHeaderValue("排出事業者");
        col.setPreferredWidth(170);
        addColumn(col);
        // 品目
        col = new TableColumn(WeighListTableModel.COLUMN_KIND);
        col.setHeaderValue("品目");
        col.setPreferredWidth(230);
        addColumn(col);
        // 処分業者　210513　マニフェスト一覧にて処分業者を表示
        col = new TableColumn(WeighListTableModel.COLUMN_CONTRACTORY);
        col.setHeaderValue("処分業者");
        col.setPreferredWidth(170);
        addColumn(col);
        // 処分種類
        col = new TableColumn(WeighListTableModel.COLUMN_OPTION);
        col.setHeaderValue("処分");
        col.setPreferredWidth(80);
        addColumn(col);
        // 処理施設
        col = new TableColumn(WeighListTableModel.COLUMN_CONT_OFFICE_SUB_NAME);
        col.setHeaderValue("処理施設");
        col.setPreferredWidth(150);
        addColumn(col);
        //  正味重量
        col = new TableColumn(WeighListTableModel.COLUMN_NET_WEIGHT);
        col.setHeaderValue("数量(kg)");
        col.setPreferredWidth(80);
        col.setCellRenderer(
                 new NumberTableCellRenderer(
                          new DecimalFormat("#,###,##0"),
                          NumberTableCellRenderer.RIGHT));
        addColumn(col);

        // 最終処分の場所
        col = new TableColumn(WeighListTableModel.COLUMN_最終処分の場所);
        col.setHeaderValue("最終処分の場所");
        col.setPreferredWidth(270);
        addColumn(col);

        // 2次マニフェスト交付番号
        col = new TableColumn(WeighListTableModel.COLUMN_2次交付番号);
        col.setHeaderValue("2次交付番号");
        col.setPreferredWidth(120);
        col.setCellRenderer(
                 new NumberTableCellRenderer(
                          new DecimalFormat("######"),
                          NumberTableCellRenderer.RIGHT));
        addColumn(col);
    }
}
