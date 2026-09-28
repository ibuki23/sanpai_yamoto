package aks.industrialwaste.recycle.ui.master.dis_entrepreneur;

import java.sql.Connection;
import java.util.Map;
import aks.industrialwaste.util.Journal;

final class Dis_entrepreneurJournal {

    protected Dis_entrepreneurJournal() {
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
                          .append("[排出業者マスタ], ")
                          .append("排出業者ID=").append(columns.get("id"))
                          .append(", 名称=").append(columns.get("name"))
                          .append(", 所在地1=").append(columns.get("address1"))
                          .append(", 所在地2=").append(columns.get("address2"))
                          .append(", 正式名称=").append(columns.get("p_name"))
                          .append(", TEL=").append(columns.get("tel"))
                          .append(", 計量表印字フラグ=").append(columns.get("print_flag"))
                          .toString();
        Journal.recordJournal(con, operation, contents);
    }
}
