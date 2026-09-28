package aks.industrialwaste.recycle.ui.master.agreement;

import aks.swing.table.BooleanStringTableCellRenderer;
import aks.swing.table.BooleanTableCellRenderer;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import javax.swing.table.DefaultTableColumnModel;
import javax.swing.table.TableColumn;
import jp.co.astax.swing.table.CalendarTableCellRenderer;
import jp.co.astax.swing.table.NumberTableCellRenderer;

public class AgreementTableColumnModel extends DefaultTableColumnModel {

    public AgreementTableColumnModel() {
        // ID
        TableColumn col = new TableColumn(AgreementTableModel.COLUMN_ENT_ID);
        col.setHeaderValue("<html><center>取引先<br>ID</center></html>");
        col.setPreferredWidth(60);
        col.setCellRenderer(
                 new NumberTableCellRenderer(
                          new DecimalFormat("######"),
                          NumberTableCellRenderer.RIGHT));
        addColumn(col);
        // 名称
        col = new TableColumn(AgreementTableModel.COLUMN_ENT_NAME);
        col.setHeaderValue("取引先名(排出業者)");
        col.setPreferredWidth(200);
        addColumn(col);
        // 所在地1
        col = new TableColumn(AgreementTableModel.COLUMN_ENT_ADDRESS1);
        col.setHeaderValue("取引先(排出業者)住所");
        col.setPreferredWidth(300);
        addColumn(col);
        // ID
        col = new TableColumn(AgreementTableModel.COLUMN_ID);
        col.setHeaderValue("<html><center>現場<br>ID</center></html>");
        col.setPreferredWidth(60);
        col.setCellRenderer(
                 new NumberTableCellRenderer(
                          new DecimalFormat("######"),
                          NumberTableCellRenderer.RIGHT));
        addColumn(col);
        // 名称
        col = new TableColumn(AgreementTableModel.COLUMN_NAME);
        col.setHeaderValue("現場名称");
        col.setPreferredWidth(240);
        addColumn(col);
        // 所在地1
        col = new TableColumn(AgreementTableModel.COLUMN_ADDRESS1);
        col.setHeaderValue("所在地1");
        col.setPreferredWidth(400);
        addColumn(col);
        // 現場終了
        col = new TableColumn(AgreementTableModel.COLUMN_FINISH);
        col.setHeaderValue("<html>現場<br>終了<html>");
        col.setPreferredWidth(50);
        col.setCellRenderer(new BooleanTableCellRenderer());
        addColumn(col);

        // 契約自動更新
        col = new TableColumn(AgreementTableModel.COLUMN_AUTO_UPDATE_FLAG);
        col.setHeaderValue("<html>自動<br>更新</html>");
        col.setPreferredWidth(50);
        col.setCellRenderer(new BooleanTableCellRenderer());
        addColumn(col);

        // 契約開始日
        col = new TableColumn(AgreementTableModel.COLUMN_START_DATE);
        col.setHeaderValue("契約開始日");
        col.setPreferredWidth(100);
        col.setCellRenderer(
                 new CalendarTableCellRenderer(
                          new SimpleDateFormat("yyyy/MM/dd"),
                          NumberTableCellRenderer.LEFT));
        addColumn(col);

        // 契約終了日
        col = new TableColumn(AgreementTableModel.COLUMN_END_DATE);
        col.setHeaderValue("契約終了日");
        col.setPreferredWidth(100);
        col.setCellRenderer(
                 new CalendarTableCellRenderer(
                          new SimpleDateFormat("yyyy/MM/dd"),
                          NumberTableCellRenderer.LEFT));
        addColumn(col);

        // 契約状況
        col = new TableColumn(AgreementTableModel.COLUMN_CONTRACT_DATE);
        col.setHeaderValue("契約状況");
        col.setPreferredWidth(80);
        addColumn(col);

        col = new TableColumn(AgreementTableModel.COLUMN_FILE_PATH);
        col.setHeaderValue("契約書");
        col.setPreferredWidth(60);
        col.setCellRenderer(
                 new BooleanStringTableCellRenderer());
        addColumn(col);
        // 請求先
        col = new TableColumn(AgreementTableModel.COLUMN_BILL_NAME);
        col.setHeaderValue("請求先");
        col.setPreferredWidth(200);
        addColumn(col);
        // 備考欄
        col = new TableColumn(AgreementTableModel.COLUMN_REMARK);
        col.setHeaderValue("備考");
        col.setPreferredWidth(300);
        addColumn(col);

        // 運搬業者１
        col = new TableColumn(AgreementTableModel.COLUMN_TRANS_NAME1);
        col.setHeaderValue("運搬業者１");
        col.setPreferredWidth(200);
        addColumn(col);
        // 運搬業者２
        col = new TableColumn(AgreementTableModel.COLUMN_TRANS_NAME2);
        col.setHeaderValue("運搬業者２");
        col.setPreferredWidth(200);
        addColumn(col);
        // 運搬業者３
        col = new TableColumn(AgreementTableModel.COLUMN_TRANS_NAME3);
        col.setHeaderValue("運搬業者３");
        col.setPreferredWidth(200);
        addColumn(col);
    }
}
