package aks.industrialwaste.recycle.ui.master.goods_category;

import java.sql.Connection;
import java.util.Map;
import aks.industrialwaste.util.Journal;

/**
 * 市町村マスタ画面の操作を記録します.
 */
final class GoodsCategoryJournal {

    protected GoodsCategoryJournal() {
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
                          .append("[市町村マスタ], ")
                          .append("ID=").append(columns.get("id"))
                          .append(", 名称=").append(columns.get("name"))
                          .append(", 代表者名=").append(columns.get("chief_name"))
                          .toString();
        Journal.recordJournal(con, operation, contents);
    }
}
