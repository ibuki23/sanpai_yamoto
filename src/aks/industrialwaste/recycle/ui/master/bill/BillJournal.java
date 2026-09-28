package aks.industrialwaste.recycle.ui.master.bill;

import java.sql.Connection;
import java.util.Map;
import aks.industrialwaste.util.Journal;

final class BillJournal {

    protected BillJournal() {
    }

    public static void recordJournal(
             Connection con,
             String operation,
             Map<String, Object> columns) {
        if ((null == con) || (null == operation) || (null == columns)) {
            throw new NullPointerException(
                     "'con', 'operation' and 'contents' cannot be null.");
        }

        String contents
                 = new StringBuilder()
                          .append("[請求先マスタ], ")
                          .append("請求先ID=").append(columns.get("id"))
                          .append(", 請求先名称=").append(columns.get("name"))
                          .append(", 所在地1=").append(columns.get("address1"))
                          .append(", 所在地2=").append(columns.get("address2"))
                          .append(", 正式名称=").append(columns.get("p_name"))
                          .toString();
        Journal.recordJournal(con, operation, contents);
    }
}
