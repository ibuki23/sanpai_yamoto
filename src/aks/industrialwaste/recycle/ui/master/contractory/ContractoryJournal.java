package aks.industrialwaste.recycle.ui.master.contractory;

import java.sql.Connection;
import java.util.Map;
import aks.industrialwaste.util.Journal;

final class ContractoryJournal {

    protected ContractoryJournal() {
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
                          .append("[処分業者マスタ], ")
                          .append("処分業者ID=").append(columns.get("id"))
                          .append(", 名称=").append(columns.get("name"))
                          .append(", 処分業者　所在地1=").append(columns.get("con_address1"))
                          .append(", 処分業者　所在地2=").append(columns.get("con_address2"))
                          .append(", 処分業者　正式名称=").append(columns.get("con_p_name"))
                          .append(", 処分業者　TEL=").append(columns.get("con_tel"))
                          .append(", 処分事業場　所在地1=").append(columns.get("office_address1"))
                          .append(", 処分事業場　所在地2=").append(columns.get("office_address2"))
                          .append(", 処分事業場　正式名称=").append(columns.get("qualification"))
                          .append(", 処分事業場　TEL=").append(columns.get("office_tel"))
                          .toString();
        Journal.recordJournal(con, operation, contents);
    }
}
