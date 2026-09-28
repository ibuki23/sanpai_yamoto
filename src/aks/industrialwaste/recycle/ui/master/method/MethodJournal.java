package aks.industrialwaste.recycle.ui.master.method;

import java.sql.Connection;
import java.util.Map;
import aks.industrialwaste.util.Journal;

final class MethodJournal {

    protected MethodJournal() {
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
                          .append("[処分方法マスタ], ")
                          .append("ID=").append(columns.get("id"))
                          .append(", 廃棄物名=").append(columns.get("name"))
                          .append(", 処分名称=").append(columns.get("method_name"))
                          .append(", 最終処分名称=").append(columns.get("final_method_name"))
                          .toString();
        Journal.recordJournal(con, operation, contents);
    }
}
