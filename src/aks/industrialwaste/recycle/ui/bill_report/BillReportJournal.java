package aks.industrialwaste.recycle.ui.bill_report;

import java.sql.Connection;
import aks.industrialwaste.util.Journal;

final class BillReportJournal {

    protected BillReportJournal() {
    }

    public static void recordJournal(
             Connection con,
             String operation,
             String s1,//実行日
             String s2,//締日
             boolean s3,//true:全請求先　false:個別
             String s4,//id
             String s5)//name
    {
        if ((null == con) || (null == operation)) {
            throw new NullPointerException(
                     "'con', 'operation' and 'contents' cannot be null.");
        }

        String contents
                 = new StringBuilder()
                          .append("[請求締め] ")
                          .append("請求日=").append(s2)
                          .append(", 全請求先=").append(s3)
                          .append(", 請求先ID=").append(s4)
                          .append(", 請求先名=").append(s5)
                          .append(", 実行日=").append(s1)
                          .toString();
        Journal.recordJournal(con, operation, contents);
    }
}
