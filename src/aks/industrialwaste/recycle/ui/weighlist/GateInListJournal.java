package aks.industrialwaste.recycle.ui.weighlist;

import java.sql.Connection;
import java.util.Calendar;
import java.util.Map;
import aks.industrialwaste.util.Journal;

final class GateInListJournal {

    protected GateInListJournal() {
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
                          .append("[滞留車]")
                          .append(", 入構重量=").append(columns.get("register_weight"));
        Object o = columns.get("register_date");
        if (o instanceof Calendar) {
            sb = sb.append(", 入構日時=").append(((Calendar) o).getTime());
        } else {
            sb = sb.append(", 入構日時=").append(o);
        }

        sb = sb.append(", 排出事業者=").append(columns.get("entrepreneur_name"))
                 .append(", 運搬業者=").append(columns.get("transporter_name"))
                 .append(", 車番=").append(columns.get("car_no"))
                 .append(", 種類=").append(columns.get("goods_name"))
                 .append(", 排出現場１=").append(columns.get("office_name"))
                 .append(", 排出現場２=").append(columns.get("office_sub_name"))
                 .append(", 建設マニフェスト=").append(columns.get("manifesto_no2"))
                 .append(", 産廃マニフェスト=").append(columns.get("manifesto_no1"));

        Journal.recordJournal(con, operation, sb.toString());
    }
}
