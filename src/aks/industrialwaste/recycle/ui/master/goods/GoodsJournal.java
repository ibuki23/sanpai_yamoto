package aks.industrialwaste.recycle.ui.master.goods;

import java.sql.Connection;
import java.util.Map;
import aks.industrialwaste.util.Journal;

final class GoodsJournal {

    protected GoodsJournal() {
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
                          .append("[品目マスタ], ")
                          .append("種類ID=").append(columns.get("id"))
                          .append(", 名称=").append(columns.get("name"))
                          .append(", 正式名称=").append(columns.get("p_name"))
                          .toString();
        Journal.recordJournal(con, operation, contents);
    }
}
