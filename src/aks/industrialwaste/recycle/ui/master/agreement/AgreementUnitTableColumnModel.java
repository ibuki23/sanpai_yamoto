package aks.industrialwaste.recycle.ui.master.agreement;

import java.text.DecimalFormat;
import javax.swing.table.DefaultTableColumnModel;
import javax.swing.table.TableColumn;
import aks.swing.table.NumberCellEditor;
import jp.co.astax.swing.table.NumberTableCellRenderer;

public class AgreementUnitTableColumnModel extends DefaultTableColumnModel {

    public AgreementUnitTableColumnModel() {

        // ID
        TableColumn col = new TableColumn(AgreementUnitTableModel.COLUMN_GOODS_ID);
        col.setHeaderValue("品目ID");
        col.setPreferredWidth(80);
        col.setCellRenderer(
                 new NumberTableCellRenderer(
                          new DecimalFormat("######"),
                          NumberTableCellRenderer.RIGHT));
        addColumn(col);
        // 名称
        col = new TableColumn(AgreementUnitTableModel.COLUMN_GOODS_NAME);
        col.setHeaderValue("品名");
        col.setPreferredWidth(300);
        addColumn(col);
        // 契約単価
        col = new TableColumn(AgreementUnitTableModel.COLUMN_GOODS_UNIT);
        col.setHeaderValue("契約単価");
        col.setPreferredWidth(80);
        col.setCellEditor(
                 new NumberCellEditor(new DecimalFormat("####0")));
        addColumn(col);
        // 単位区分名
        col = new TableColumn(AgreementUnitTableModel.COLUMN_GOODS_UNIT_NO_NAME);
        col.setHeaderValue("単位区分名");
        col.setPreferredWidth(80);
        addColumn(col);

        col = new TableColumn(AgreementUnitTableModel.COLUMN_OPTION1_NAME);
        col.setHeaderValue("取引区分");
        col.setPreferredWidth(80);
        addColumn(col);

        col = new TableColumn(AgreementUnitTableModel.COLUMN_CARRY_NAME);
        col.setHeaderValue("搬入出");
        col.setPreferredWidth(80);
        addColumn(col);
    }
}
