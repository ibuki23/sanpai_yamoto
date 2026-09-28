package aks.industrialwaste.recycle.ui.master.dis_office;

import aks.swing.table.BooleanTableCellRenderer;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import javax.swing.table.DefaultTableColumnModel;
import javax.swing.table.TableColumn;
import jp.co.astax.swing.table.CalendarTableCellRenderer;
import jp.co.astax.swing.table.NumberTableCellRenderer;

public class Dis_officeTableColumnModel extends DefaultTableColumnModel {

    public Dis_officeTableColumnModel() {
        // ID
        TableColumn col = new TableColumn(Dis_officeTableModel.COLUMN_ENT_ID);
        col.setHeaderValue("コード");
        col.setPreferredWidth(60);
        col.setCellRenderer(
                 new NumberTableCellRenderer(
                          new DecimalFormat("######"),
                          NumberTableCellRenderer.RIGHT));
        addColumn(col);
        // 名称
        col = new TableColumn(Dis_officeTableModel.COLUMN_ENT_NAME);
        col.setHeaderValue("取引先名");
        col.setPreferredWidth(200);
        addColumn(col);
        // 所在地1
        col = new TableColumn(Dis_officeTableModel.COLUMN_ENT_ADDRESS1);
        col.setHeaderValue("取引先住所１");
        col.setPreferredWidth(150);
        addColumn(col);
        // ID
        col = new TableColumn(Dis_officeTableModel.COLUMN_ID);
        col.setHeaderValue("コード");
        col.setPreferredWidth(60);
        col.setCellRenderer(
                 new NumberTableCellRenderer(
                          new DecimalFormat("######"),
                          NumberTableCellRenderer.RIGHT));
        addColumn(col);
        // 名称
        col = new TableColumn(Dis_officeTableModel.COLUMN_NAME);
        col.setHeaderValue("現場名称");
        col.setPreferredWidth(200);
        addColumn(col);
        // 所在地1
        col = new TableColumn(Dis_officeTableModel.COLUMN_ADDRESS1);
        col.setHeaderValue("所在地1");
        col.setPreferredWidth(230);
        addColumn(col);

        // 現場終了
        col = new TableColumn(Dis_officeTableModel.COLUMN_FINISH);
        col.setHeaderValue("<html>現場<br>終了<html>");
        col.setPreferredWidth(50);
        col.setCellRenderer(new BooleanTableCellRenderer());
        addColumn(col);
        // 契約開始日
        col = new TableColumn(Dis_officeTableModel.COLUMN_START_DATE);
        col.setHeaderValue("契約開始日");
        col.setPreferredWidth(100);
        col.setCellRenderer(
                 new CalendarTableCellRenderer(
                          new SimpleDateFormat("yyyy/MM/dd"),
                          NumberTableCellRenderer.LEFT));
        addColumn(col);
        // 契約終了日
        col = new TableColumn(Dis_officeTableModel.COLUMN_END_DATE);
        col.setHeaderValue("契約終了日");
        col.setPreferredWidth(100);
        col.setCellRenderer(
                 new CalendarTableCellRenderer(
                          new SimpleDateFormat("yyyy/MM/dd"),
                          NumberTableCellRenderer.LEFT));
        addColumn(col);

        // 契約自動更新
        col = new TableColumn(Dis_officeTableModel.COLUMN_AUTO_UPDATE_FLAG);
        col.setHeaderValue("<html>自動<br>更新<html>");
        col.setPreferredWidth(50);
        col.setCellRenderer(new BooleanTableCellRenderer());
        addColumn(col);
    }
}
