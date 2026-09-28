package aks.industrialwaste.recycle.ui.master.transporter;

import java.sql.Connection;
import java.util.Map;
import aks.industrialwaste.util.Journal;

final class TransporterJournal {

    protected TransporterJournal() {
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
                          .append("[運搬業者マスタ]")
                          .append(", 運搬業者ID=").append(columns.get("id"))
                          .append(", 名称=").append(columns.get("name"))
                          .append(", 所在地1=").append(columns.get("address1"))
                          .append(", 所在地2=").append(columns.get("address2"))
                          .append(", TEL=").append(columns.get("tel"))
                          .toString();

        Journal.recordJournal(con, operation, contents);
    }
}
