package aks.industrialwaste.recycle.ui.master.car_kind;

import java.sql.Connection;
import java.util.Map;
import aks.industrialwaste.util.Journal;

final class Car_kindJournal {

    protected Car_kindJournal() {
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
                          .append("[車種マスタ], ")
                          .append("車種ID=").append(columns.get("id"))
                          .append(", 名称=").append(columns.get("name"))
                          .toString();
        Journal.recordJournal(con, operation, contents);
    }
}
