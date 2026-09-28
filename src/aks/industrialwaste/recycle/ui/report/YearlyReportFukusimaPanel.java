package aks.industrialwaste.recycle.ui.report;

import java.sql.Connection;

public class YearlyReportFukusimaPanel extends YearlyReportPanel {

    public YearlyReportFukusimaPanel(Connection con) {
        super(con, "福島県", REPORT_TYPE_FUKUSHIMA);
    }
}
