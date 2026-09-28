package aks.industrialwaste.recycle.ui.master.dis_entrepreneur;

import java.text.DecimalFormat;
import javax.swing.table.DefaultTableColumnModel;
import javax.swing.table.TableColumn;
import jp.co.astax.swing.table.NumberTableCellRenderer;
import aks.swing.table.BooleanTableCellRenderer;

public class Dis_entrepreneurTableColumnModel extends DefaultTableColumnModel {

    public Dis_entrepreneurTableColumnModel() {
        // ID
        TableColumn col = new TableColumn(Dis_entrepreneurTableModel.COLUMN_ID);
        col.setHeaderValue("ID");
        col.setPreferredWidth(80);
        col.setCellRenderer(
                 new NumberTableCellRenderer(
                          new DecimalFormat("######"),
                          NumberTableCellRenderer.RIGHT));
        addColumn(col);
        // 名前
        col = new TableColumn(Dis_entrepreneurTableModel.COLUMN_NAME);
        col.setHeaderValue("名称");
        col.setPreferredWidth(300);
        addColumn(col);
        // 名前
        col = new TableColumn(Dis_entrepreneurTableModel.COLUMN_P_NAME);
        col.setHeaderValue("正式名称");
        col.setPreferredWidth(400);
        addColumn(col);
        // 所在地1
        col = new TableColumn(Dis_entrepreneurTableModel.COLUMN_ADDRESS1);
        col.setHeaderValue("所在地1");
        col.setPreferredWidth(300);
        addColumn(col);
        // 所在地2
        col = new TableColumn(Dis_entrepreneurTableModel.COLUMN_ADDRESS2);
        col.setHeaderValue("所在地2");
        col.setPreferredWidth(200);
        // TEL
        col = new TableColumn(Dis_entrepreneurTableModel.COLUMN_TEL);
        col.setHeaderValue("TEL");
        col.setPreferredWidth(120);
        addColumn(col);

        // 計量票発行有無
        col = new TableColumn(Dis_entrepreneurTableModel.COLUMN_P_FLAG);
        col.setHeaderValue("計量票");
        col.setPreferredWidth(60);
        col.setCellRenderer(new BooleanTableCellRenderer());
        addColumn(col);

        // 行政中
        col = new TableColumn(Dis_entrepreneurTableModel.COLUMN_MIDDLE_GOODS_NAME);
        col.setHeaderValue("行政中間");
        col.setPreferredWidth(40);

        // 行政最
        col = new TableColumn(Dis_entrepreneurTableModel.COLUMN_FINAL_GOODS_NAME);
        col.setHeaderValue("行政最終");
        col.setPreferredWidth(40);

    }
}
