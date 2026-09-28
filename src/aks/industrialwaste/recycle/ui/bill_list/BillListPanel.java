package aks.industrialwaste.recycle.ui.bill_list;

import aks.industrialwaste.ui.Panel;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.sql.Connection;
import javax.swing.JSplitPane;

public class BillListPanel extends Panel {

    private JSplitPane splitpane;

    public BillListPanel(Connection con) {
        configurePane(con);
    }

    @Override
    protected void initComponents() {
        super.initComponents();

        // タイトルは非表示
        titleField_.setVisible(false);
    }

    private void configurePane(Connection con) {

        //移動可能区画をセット
        splitpane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
        // 請求一覧
        BillList billList = new BillList(con);
        billList.setMinimumSize(new Dimension(1260, 300));
        billList.setPreferredSize(new Dimension(1260, 400));
        // 請求明細
        BillDetailsList billDetailList = new BillDetailsList(con);
        billDetailList.setMinimumSize(new Dimension(1260, 300));
        billDetailList.setPreferredSize(new Dimension(1260, 400));
        splitpane.setTopComponent(billList);
        splitpane.setBottomComponent(billDetailList);

        addContainer(
                 centerPane_, splitpane,
                 0, 0, 1, 1, 1.0, 1.0,
                 GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                 new Insets(2, 2, 2, 2), 0, 0);
    }
}
