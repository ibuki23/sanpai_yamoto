package aks.industrialwaste.recycle.ui.report;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.sql.Connection;
import javax.swing.JPanel;
import aks.industrialwaste.ui.Panel;

public class ReportPanel extends Panel {

    // 今回は行政報告を利用しないため非表示。関連機能は削除せず残す。
    private static final boolean SHOW_ADMINISTRATIVE_REPORT_PANEL = false;

    public ReportPanel(Connection con) {
        configurePane(con);
    }

    @Override
    protected void initComponents() {
        super.initComponents();

        // タイトル
        titleField_.setText("帳票");
    }

    private void configurePane(Connection con) {
        // 帳票印刷操作パネル
        // 日報
        Panel p = new DailyReportPanel(con);
        p.setMinimumSize(new Dimension(450, 500));
        p.setPreferredSize(new Dimension(450, 500));
        addContainer(
                 centerPane_, p,
                 0, 0, 1, 1, 0.0, 0.0,
                 GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL,
                 new Insets(2, 2, 2, 2), 0, 0);

        if (SHOW_ADMINISTRATIVE_REPORT_PANEL) {
            // 年報（行政報告）
            p = new YearlyReportPanel(con);
            p.setMinimumSize(new Dimension(450, 500));
            p.setPreferredSize(new Dimension(450, 500));
            addContainer(
                     centerPane_, p,
                     2, 0, 1, 1, 0.0, 0.0,
                     GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL,
                     new Insets(2, 2, 2, 2), 0, 0);

            // 年報 山形県
            p = new YearlyReportYamagataPanel(con);
            p.setMinimumSize(new Dimension(450, 500));
            p.setPreferredSize(new Dimension(450, 500));
//      発注が出ていないためコメント中
//        addContainer(
//                 centerPane_, p,
//                 3, 0, 1, 1, 0.0, 0.0,
//                 GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL,
//                 new Insets(2, 2, 2, 2), 0, 0);

            // 年報 福島県
            p = new YearlyReportFukusimaPanel(con);
            p.setMinimumSize(new Dimension(450, 500));
            p.setPreferredSize(new Dimension(450, 500));
//      発注が出ていないためコメント中
//        addContainer(
//                 centerPane_, p,
//                 4, 0, 1, 1, 0.0, 0.0,
//                 GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL,
//                 new Insets(2, 2, 2, 2), 0, 0);
        }

        // ダミー
        JPanel pane = new JPanel();
        pane = new JPanel();
        pane.setMinimumSize(new Dimension(250, 100));
        pane.setPreferredSize(new Dimension(250, 100));
        addContainer(
                 centerPane_, pane,
                 5, 0, 1, 2, 1.0, 1.0,
                 GridBagConstraints.WEST, GridBagConstraints.BOTH,
                 new Insets(2, 2, 2, 2), 0, 0);
    }
}
