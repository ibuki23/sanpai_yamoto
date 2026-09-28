package aks.industrialwaste.recycle.ui.bill_report;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.sql.Connection;
import javax.swing.JPanel;
import aks.industrialwaste.ui.Panel;

public class BillReport extends Panel {

    public BillReport(Connection con) {
        configurePane(con);
    }

    @Override
    protected void initComponents() {
        super.initComponents();

        titleField_.setText("請求書締めメニュー");
    }

    private void configurePane(Connection con) {
        // 帳票印刷操作パネル
        // 帳票出力
        Panel p = new BillReportPanel(con);
        p.setMinimumSize(new Dimension(450, 400));
        p.setPreferredSize(new Dimension(450, 400));
        addContainer(
                 centerPane_, p,
                 0, 0, 1, 1, 0.0, 0.0,
                 GridBagConstraints.NORTHWEST, GridBagConstraints.NONE,
                 new Insets(2, 2, 2, 2), 0, 0);
        // 請求書締め処理
        p = new BundlePanel(con);
        p.setMinimumSize(new Dimension(400, 400));
        p.setPreferredSize(new Dimension(400, 400));
        addContainer(
                 centerPane_, p,
                 1, 0, 1, 1, 0.0, 0.0,
                 GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                 new Insets(2, 2, 2, 2), 0, 0);
        // 請求書締め処理
        p = new BillMaintePanel(con);
        p.setMinimumSize(new Dimension(600, 400));
        p.setPreferredSize(new Dimension(600, 400));
        // ダミー
        JPanel pane = new JPanel();
        pane = new JPanel();
        pane.setMinimumSize(new Dimension(250, 100));
        pane.setPreferredSize(new Dimension(250, 100));
        addContainer(
                 centerPane_, pane,
                 0, 5, 3, 1, 1.0, 1.0,
                 GridBagConstraints.WEST, GridBagConstraints.NONE,
                 new Insets(2, 2, 2, 2), 0, 0);
    }
}
