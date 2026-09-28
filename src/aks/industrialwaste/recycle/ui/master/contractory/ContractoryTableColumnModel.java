package aks.industrialwaste.recycle.ui.master.contractory;

import java.text.DecimalFormat;
import javax.swing.table.DefaultTableColumnModel;
import javax.swing.table.TableColumn;
import jp.co.astax.swing.table.NumberTableCellRenderer;

public class ContractoryTableColumnModel extends DefaultTableColumnModel {

    public ContractoryTableColumnModel() {
        // ID
        TableColumn col = new TableColumn(ContractoryTableModel.COLUMN_ID);
        col.setHeaderValue("ID");
        col.setPreferredWidth(80);
        col.setCellRenderer(
                 new NumberTableCellRenderer(
                          new DecimalFormat("######"),
                          NumberTableCellRenderer.RIGHT));
        addColumn(col);
        // 名称
        col = new TableColumn(ContractoryTableModel.COLUMN_NAME);
        col.setHeaderValue("名称");
        col.setPreferredWidth(200);
        addColumn(col);
        // 処分業者　所在地1
        col = new TableColumn(ContractoryTableModel.COLUMN_ADDRESS1);
        col.setHeaderValue("所在地1");
        col.setPreferredWidth(300);
        addColumn(col);
        // 処分業者　所在地2
        col = new TableColumn(ContractoryTableModel.COLUMN_ADDRESS2);
        col.setHeaderValue("所在地2");
        col.setPreferredWidth(150);
//    addColumn(col);
        // 処分業者　TEL
        col = new TableColumn(ContractoryTableModel.COLUMN_CON_TEL);
        col.setHeaderValue("TEL");
        col.setPreferredWidth(150);
        addColumn(col);
        // 許可番号
        col = new TableColumn(ContractoryTableModel.COLUMN_PERMISSION_NO);
        col.setHeaderValue("許可番号");
        col.setPreferredWidth(150);
        addColumn(col);
        // 処分担当者
        col = new TableColumn(ContractoryTableModel.COLUMN_CONT_TANTOU);
        col.setHeaderValue("処分担当者");
        col.setPreferredWidth(150);
        addColumn(col);
        // 最終処分担当者
        col = new TableColumn(ContractoryTableModel.COLUMN_LAST_TANTOU);
        col.setHeaderValue("最終処分担当者");
        col.setPreferredWidth(150);
        addColumn(col);

    }
}
