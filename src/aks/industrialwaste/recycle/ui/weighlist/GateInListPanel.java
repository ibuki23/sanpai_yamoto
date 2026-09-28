package aks.industrialwaste.recycle.ui.weighlist;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.sql.Connection;
import aks.industrialwaste.ui.Panel;

public class GateInListPanel extends Panel {

    public GateInListPanel(Connection con) {
        configurePane(con);
    }

    @Override
    protected void initComponents() {
        super.initComponents();

        titleField_.setVisible(false);
    }

    private void configurePane(Connection con) {

        // 一時記憶一覧
        GateInList gateInList = new GateInList(con);
        gateInList.setMinimumSize(new Dimension(1260, 200));
        gateInList.setPreferredSize(new Dimension(1260, 200));
        addContainer(
                 centerPane_, gateInList,
                 0, 1, 1, 1, 1.0, 1.0,
                 GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                 new Insets(2, 2, 2, 2), 0, 0);
    }
}
