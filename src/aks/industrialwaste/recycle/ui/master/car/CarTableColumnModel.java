package aks.industrialwaste.recycle.ui.master.car;

//import aks.industrialwaste.recycle.ui.master.gastype.*;
import java.text.DecimalFormat;
import javax.swing.table.DefaultTableColumnModel;
import javax.swing.table.TableColumn;
//import jp.co.astax.swing.table.BooleanTableCellRenderer;
//import aks.swing.table.BooleanTableCellRenderer;
import jp.co.astax.swing.table.NumberTableCellRenderer;

public class CarTableColumnModel extends DefaultTableColumnModel {

    public CarTableColumnModel() {
        // 車両ID
        TableColumn col = new TableColumn(CarTableModel.COLUMN_CAR_ID);
        col.setHeaderValue("管理ID");
        col.setPreferredWidth(80);
        col.setCellRenderer(
                 new NumberTableCellRenderer(
                          new DecimalFormat("######"),
                          NumberTableCellRenderer.LEFT));
        addColumn(col);

        // 車番
        col = new TableColumn(CarTableModel.COLUMN_CAR_NO);
        col.setHeaderValue("車番");
        col.setPreferredWidth(60);
        col.setCellRenderer(
                 new NumberTableCellRenderer(
                          new DecimalFormat("######"),
                          NumberTableCellRenderer.RIGHT));
        addColumn(col);
        // 車種
        col = new TableColumn(CarTableModel.COLUMN_CAR_KIND_NAME);
        col.setHeaderValue("車種");
        col.setPreferredWidth(100);
//    col.setCellRenderer(
//      new NumberTableCellRenderer(
//        new DecimalFormat("######"),
//        NumberTableCellRenderer.RIGHT));
        addColumn(col);
        //風袋重量
        col = new TableColumn(CarTableModel.COLUMN_EMPTY_WEIGHT);
        col.setHeaderValue("風袋重量");
        col.setPreferredWidth(100);
        col.setCellRenderer(
                 new NumberTableCellRenderer(
                          new DecimalFormat("#,###,##0"),
                          NumberTableCellRenderer.RIGHT));
        addColumn(col);
        //最大重量
        col = new TableColumn(CarTableModel.COLUMN_MAX_WEIGHT);
        col.setHeaderValue("最大積載量");
        col.setPreferredWidth(100);
        col.setCellRenderer(
                 new NumberTableCellRenderer(
                          new DecimalFormat("#,###,##0"),
                          NumberTableCellRenderer.RIGHT));
        addColumn(col);
        // 排出業者
        col = new TableColumn(CarTableModel.COLUMN_DIS_ENT);
        col.setHeaderValue("排出業者");
        col.setPreferredWidth(200);
        addColumn(col);
        // 運搬業者
        col = new TableColumn(CarTableModel.COLUMN_TRANSPORTER);
        col.setHeaderValue("運搬業者");
        col.setPreferredWidth(200);
        addColumn(col);
        //品目
        col = new TableColumn(CarTableModel.COLUMN_KIND);
        col.setHeaderValue("品目");
        col.setPreferredWidth(200);
        addColumn(col);
        //排出事業場
        col = new TableColumn(CarTableModel.COLUMN_DIS_OFFICE);
        col.setHeaderValue("排出事業場");
        col.setPreferredWidth(200);
        addColumn(col);
        //処分業者
        col = new TableColumn(CarTableModel.COLUMN_CONTRACTORY);
        col.setHeaderValue("処分業者");
        col.setPreferredWidth(200);
        addColumn(col);
//    //車種
//    col = new TableColumn(CarTableModel.COLUMN_CAR_KIND_NAME);
//    col.setHeaderValue("車種");
//    col.setPreferredWidth(80);
//    col.setCellRenderer(
//      new NumberTableCellRenderer(
//        new DecimalFormat("#,###,##0"),
//        NumberTableCellRenderer.RIGHT));
//    addColumn(col);
//    //風袋重量
//    col = new TableColumn(CarTableModel.COLUMN_EMPTY_WEIGHT);
//    col.setHeaderValue("風袋重量");
//    col.setPreferredWidth(100);
//    col.setCellRenderer(
//      new NumberTableCellRenderer(
//        new DecimalFormat("#,###,##0"),
//        NumberTableCellRenderer.RIGHT));
//    addColumn(col);
//    //最大重量
//    col = new TableColumn(CarTableModel.COLUMN_MAX_WEIGHT);
//    col.setHeaderValue("最大重量");
//    col.setPreferredWidth(100);
//    col.setCellRenderer(
//      new NumberTableCellRenderer(
//        new DecimalFormat("#,###,##0"),
//        NumberTableCellRenderer.RIGHT));
//    addColumn(col);

    }
}
