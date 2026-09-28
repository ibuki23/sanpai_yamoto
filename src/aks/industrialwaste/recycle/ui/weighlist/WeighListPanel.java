package aks.industrialwaste.recycle.ui.weighlist;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.sql.Connection;
import aks.industrialwaste.ui.Panel;

public class WeighListPanel extends Panel {

    public WeighListPanel(Connection con) {
        configurePane(con);
    }

    @Override
    protected void initComponents() {
        super.initComponents();

        // タイトルは非表示
        titleField_.setVisible(false);
    }

    private void configurePane(Connection con) {
        // 計量記録一覧
        WeighList weighList = new WeighList(con);
        weighList.setMinimumSize(new Dimension(1260, 200));
        weighList.setPreferredSize(new Dimension(1260, 200));
        addContainer(
                 centerPane_, weighList,
                 0, 0, 1, 1, 1.0, 1.0,
                 GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                 new Insets(2, 2, 2, 2), 0, 0);
    }
}
