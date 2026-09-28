package aks.industrialwaste.recycle.ui.report;

import java.sql.Connection;

public class YearlyReportYamagataPanel extends YearlyReportPanel {

    public YearlyReportYamagataPanel(Connection con) {
        super(con, "山形県", REPORT_TYPE_YAMAGATA);
    }
}
