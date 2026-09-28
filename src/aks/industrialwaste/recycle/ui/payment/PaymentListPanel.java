package aks.industrialwaste.recycle.ui.payment;

import aks.industrialwaste.ui.Panel;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.sql.Connection;
import javax.swing.JSplitPane;

public class PaymentListPanel extends Panel {

    private JSplitPane splitpane;

    public PaymentListPanel(Connection con) {
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
        PaymentList paymentList = new PaymentList(con);
        paymentList.setMinimumSize(new Dimension(1260, 300));
        paymentList.setPreferredSize(new Dimension(1260, 400));
        // 請求明細
        PaymentDetailsList paymentDetailList = new PaymentDetailsList(con);
        paymentDetailList.setMinimumSize(new Dimension(1260, 300));
        paymentDetailList.setPreferredSize(new Dimension(1260, 400));

        splitpane.setTopComponent(paymentList);
        splitpane.setBottomComponent(paymentDetailList);

        addContainer(
                 centerPane_, splitpane,
                 0, 0, 1, 1, 1.0, 1.0,
                 GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                 new Insets(2, 2, 2, 2), 0, 0);
    }
}
