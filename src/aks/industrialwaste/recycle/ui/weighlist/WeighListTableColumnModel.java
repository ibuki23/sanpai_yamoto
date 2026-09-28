package aks.industrialwaste.recycle.ui.weighlist;

import aks.swing.table.BooleanIntegerTableCellRenderer;
import java.awt.Color;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import javax.swing.table.DefaultTableColumnModel;
import javax.swing.table.TableColumn;
import jp.co.astax.swing.table.CalendarTableCellRenderer;
import jp.co.astax.swing.table.NumberTableCellRenderer;
import javax.swing.table.DefaultTableCellRenderer;

public class WeighListTableColumnModel extends DefaultTableColumnModel {

    public WeighListTableColumnModel() {
        DefaultTableCellRenderer r = new DefaultTableCellRenderer();
        r.setBackground(new Color(255, 153, 153));

        // 計量票No
        TableColumn col = new TableColumn(WeighListTableModel.COLUMN_REPORT_NO);
        col.setHeaderValue("NO");
        col.setPreferredWidth(50);
        col.setCellRenderer(
                 new NumberTableCellRenderer(
                          new DecimalFormat("####"),
                          NumberTableCellRenderer.LEFT));
        addColumn(col);
        // 計量日時 時間
        col = new TableColumn(WeighListTableModel.COLUMN_TIME);
        col.setHeaderValue("計量日時");
        col.setPreferredWidth(150);
        col.setCellRenderer(
                 new CalendarTableCellRenderer(
                          new SimpleDateFormat("yy/MM/dd HH:mm"),
                          NumberTableCellRenderer.LEFT));
        addColumn(col);
        // 車番
        col = new TableColumn(WeighListTableModel.COLUMN_CAR_NO);
        col.setHeaderValue("車番");
        col.setPreferredWidth(80);
        col.setCellRenderer(
                 new NumberTableCellRenderer(
                          new DecimalFormat("######"),
                          NumberTableCellRenderer.RIGHT));
        addColumn(col);
        //  正味重量
        col = new TableColumn(WeighListTableModel.COLUMN_NET_WEIGHT);
        col.setHeaderValue("正味重量");
        col.setPreferredWidth(100);
        col.setCellRenderer(
                 new NumberTableCellRenderer(
                          new DecimalFormat("#,###,##0"),
                          NumberTableCellRenderer.RIGHT));
        addColumn(col);
        // 排出事業者
        col = new TableColumn(WeighListTableModel.COLUMN_DIS_ENT);
        col.setHeaderValue("排出事業者");
        col.setPreferredWidth(180);
        addColumn(col);

        // 品目
        col = new TableColumn(WeighListTableModel.COLUMN_KIND);
        col.setHeaderValue("品目");
        col.setPreferredWidth(180);
        addColumn(col);
        // 単位
        col = new TableColumn(WeighListTableModel.COLUMN_単位);
        col.setHeaderValue("単位");
        col.setPreferredWidth(80);
        addColumn(col);
        // 処分種類
        col = new TableColumn(WeighListTableModel.COLUMN_OPTION);
        col.setHeaderValue("取扱区分");
        col.setPreferredWidth(80);
        addColumn(col);
        // 処分種類
        col = new TableColumn(WeighListTableModel.COLUMN_CARRY_NAME);
        col.setHeaderValue("搬入出");
        col.setPreferredWidth(80);
        addColumn(col);
        // 排出事業場
        col = new TableColumn(WeighListTableModel.COLUMN_DIS_OFFICE);
        col.setHeaderValue("排出事業場");
        col.setPreferredWidth(180);
        addColumn(col);
        // 運搬業者
        col = new TableColumn(WeighListTableModel.COLUMN_TRANSPORTER);
        col.setHeaderValue("運搬業者");
        col.setPreferredWidth(180);
        addColumn(col);
        // 備考
        col = new TableColumn(WeighListTableModel.COLUMN_REMARK);
        col.setHeaderValue("備考");
        col.setPreferredWidth(80);
        addColumn(col);
        // 処分業者
        col = new TableColumn(WeighListTableModel.COLUMN_CONTRACTORY);
        col.setHeaderValue("処分業者");
        col.setPreferredWidth(180);
        addColumn(col);
        // マニフェスト有
        col = new TableColumn(WeighListTableModel.COLUMN_MNIFEST);
        col.setHeaderValue("管理票");
        col.setPreferredWidth(80);
        addColumn(col);
        // 電子マニフェスト区分
        col = new TableColumn(WeighListTableModel.COLUMN_電子マニフェスト区分);
        col.setHeaderValue("電子MF");
        col.setPreferredWidth(80);
        col.setCellRenderer(new BooleanIntegerTableCellRenderer());
        addColumn(col);
        // 売上区分
        col = new TableColumn(WeighListTableModel.COLUMN_売上区分);
        col.setHeaderValue("売上区分");
        col.setPreferredWidth(80);
        addColumn(col);
    }
}
