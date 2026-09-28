package aks.industrialwaste.recycle.ui.master.goods;

import java.text.DecimalFormat;
import javax.swing.table.DefaultTableColumnModel;
import javax.swing.table.TableColumn;
import jp.co.astax.swing.table.NumberTableCellRenderer;

public class GoodsTableColumnModel extends DefaultTableColumnModel {

    public GoodsTableColumnModel() {
        // SNO　200903　必要ないため隠す
        TableColumn col = new TableColumn(GoodsTableModel.COLUMN_SNO);
        col.setHeaderValue("SNO");
        col.setPreferredWidth(50);
        // ID
        col = new TableColumn(GoodsTableModel.COLUMN_ID);
        col.setHeaderValue("ID");
        col.setPreferredWidth(50);
        col.setCellRenderer(
                 new NumberTableCellRenderer(
                          new DecimalFormat("######"),
                          NumberTableCellRenderer.RIGHT));
        addColumn(col);
        // 名前
        col = new TableColumn(GoodsTableModel.COLUMN_NAME);
        col.setHeaderValue("名称");
        col.setPreferredWidth(200);
        addColumn(col);
        // 正式名称
        col = new TableColumn(GoodsTableModel.COLUMN_OFFICIAL_NAME);
        col.setHeaderValue("正式名称");
        col.setPreferredWidth(200);
        addColumn(col);
        // 種類
        col = new TableColumn(GoodsTableModel.COLUMN_OPTION_NAME);
        col.setHeaderValue("処分");
        col.setPreferredWidth(80);
        addColumn(col);
        // 搬入搬出
        col = new TableColumn(GoodsTableModel.COLUMN_CARRY_NAME);
        col.setHeaderValue("搬入出");
        col.setPreferredWidth(60);
        addColumn(col);

        // 処分方法 建廃
        col = new TableColumn(GoodsTableModel.COLUMN_METHOD2);
        col.setHeaderValue("大分類(建廃)");
        col.setPreferredWidth(200);
        addColumn(col);
        // 処分方法 産廃
        col = new TableColumn(GoodsTableModel.COLUMN_METHOD1);
        col.setHeaderValue("大分類(産廃)");
        col.setPreferredWidth(200);
        addColumn(col);

        // 単位
        col = new TableColumn(GoodsTableModel.COLUMN_UNIT_NAME);
        col.setHeaderValue("単位");
        col.setPreferredWidth(80);
        addColumn(col);
        // 単価
        col = new TableColumn(GoodsTableModel.COLUMN_UNIT_VALUE);
        col.setHeaderValue("標準単価");
        col.setPreferredWidth(100);
        col.setCellRenderer(
                 new NumberTableCellRenderer(
                          new DecimalFormat("#,###,##0"),
                          NumberTableCellRenderer.RIGHT));
        addColumn(col);
        // 行政中間
        col = new TableColumn(GoodsTableModel.COLUMN_MIDDLE_GOODS_NAME);
        col.setHeaderValue("行政報告分類名");
        col.setPreferredWidth(200);
        addColumn(col);
        // 一般・産廃
        col = new TableColumn(GoodsTableModel.COLUMN_FINAL_GOODS_NAME);
        col.setHeaderValue("一般・産廃");
        col.setPreferredWidth(200);
        addColumn(col);
        // 行政最終
        col = new TableColumn(GoodsTableModel.COLUMN_FINAL_GOODS_NAME);
        col.setHeaderValue("<html>行政<br>最終</html>");
        col.setPreferredWidth(60);

        // 比重係数
        col = new TableColumn(GoodsTableModel.COLUMN_RYUBE);
        col.setHeaderValue("比重");
        col.setPreferredWidth(100);
        col.setCellRenderer(
                 new NumberTableCellRenderer(
                          new DecimalFormat("##0.00"),
                          NumberTableCellRenderer.RIGHT));
        addColumn(col);

    }
}
