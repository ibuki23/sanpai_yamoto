package aks.industrialwaste.recycle.ui.manifesto;

import java.sql.Connection;
import java.util.Calendar;
import java.util.Map;
import aks.industrialwaste.util.Journal;
import java.text.SimpleDateFormat;

final class ManifestoListJournal {

    protected ManifestoListJournal() {
    }

    public static void recordJournal(
             Connection con,
             String operation,
             Map<String, Object> columns) {
        if ((null == con) || (null == operation) || (null == columns)) {
            throw new NullPointerException(
                     "'con', 'operation' and 'contents' cannot be null.");
        }

        StringBuilder sb
                 = new StringBuilder()
                          .append("[計量]")
                          .append(", 計量票No=").append(columns.get("report_no"))
                          .append(", 所属日=").append(columns.get("term_date")) //所属？
                          .append(", 入構重量=").append(columns.get("gate_in_weight"));
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Object o = columns.get("gate_in_date");
        if (o instanceof Calendar) {
            sb = sb.append(", 入構日時=").append(sf.format(((Calendar) o).getTime()));
        } else {
            sb = sb.append(", 入構日時=").append(o);
        }
        sb = sb.append(", 出構重量=").append(columns.get("gate_out_weight"));
        o = columns.get("gate_out_date");
        if (o instanceof Calendar) {
            sb = sb.append(", 出構日時=").append(sf.format(((Calendar) o).getTime()));
        } else {
            sb = sb.append(", 出構日時=").append(o);
        }
        sb = sb.append(", 車番=").append(columns.get("car_no"))
                 .append(", 排出事業者=").append(columns.get("entrepreneur_name"))
                 .append(", 運搬業者=").append(columns.get("transporter_name"))
                 .append(", 種類=").append(columns.get("goods_name"))
                 .append(", 領収済=").append(columns.get("collected"));

        Journal.recordJournal(con, operation, sb.toString());
    }
}
