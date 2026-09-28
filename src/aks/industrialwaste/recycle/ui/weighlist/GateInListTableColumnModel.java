package aks.industrialwaste.recycle.ui.weighlist;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import javax.swing.table.DefaultTableColumnModel;
import javax.swing.table.TableColumn;
import jp.co.astax.swing.table.CalendarTableCellRenderer;
import jp.co.astax.swing.table.NumberTableCellRenderer;

public class GateInListTableColumnModel extends DefaultTableColumnModel {

    public GateInListTableColumnModel() {
        // 時間
        TableColumn col = new TableColumn(GateInListTableModel.COLUMN_TIME);
        col.setHeaderValue("時間");
        col.setPreferredWidth(120);
        col.setCellRenderer(
                 new CalendarTableCellRenderer(
                          new SimpleDateFormat("yy/MM/dd HH:mm"),
                          CalendarTableCellRenderer.CENTER));
        addColumn(col);
        // 車番
        col = new TableColumn(GateInListTableModel.COLUMN_CAR_NO);
        col.setHeaderValue("車番");
        col.setPreferredWidth(50);
        col.setCellRenderer(
                 new NumberTableCellRenderer(
                          new DecimalFormat("######"),
                          NumberTableCellRenderer.RIGHT));
        addColumn(col);
        // 入構重量　　一時記憶
        col = new TableColumn(GateInListTableModel.COLUMN_REJISTER_WEIGHT);
        col.setHeaderValue("記憶重量");
        col.setPreferredWidth(70);
        col.setCellRenderer(
                 new NumberTableCellRenderer(
                          new DecimalFormat("#,###,##0"),
                          NumberTableCellRenderer.RIGHT));
        addColumn(col);
        // 請求先
        col = new TableColumn(GateInListTableModel.COLUMN_BILL);
        col.setHeaderValue("請求先");
        col.setPreferredWidth(150);
        addColumn(col);
        // 排出事業者
        col = new TableColumn(GateInListTableModel.COLUMN_DIS_ENT);
        col.setHeaderValue("排出事業者");
        col.setPreferredWidth(150);
        addColumn(col);
        // 運搬業者
        col = new TableColumn(GateInListTableModel.COLUMN_TRANSPORTER);
        col.setHeaderValue("運搬業者");
        col.setPreferredWidth(150);
        addColumn(col);
        // 品目
        col = new TableColumn(GateInListTableModel.COLUMN_KIND);
        col.setHeaderValue("品目");
        col.setPreferredWidth(150);
        addColumn(col);
        // 排出事業場
        col = new TableColumn(GateInListTableModel.COLUMN_DIS_OFFICE);
        col.setHeaderValue("排出事業場");
        col.setPreferredWidth(130);
        addColumn(col);
        // 処分業者
        col = new TableColumn(GateInListTableModel.COLUMN_CONTRACTORY);
        col.setHeaderValue("処分業者");
        col.setPreferredWidth(130);
        addColumn(col);
    }
}
