package aks.industrialwaste.recycle.ui.report;

import java.sql.Connection;
import aks.industrialwaste.util.Journal;

final class ReportJournal {

    public static void recordJournal(
             Connection con,
             String operation,
             String s1,//実行日
             String s2,//取込対象日時
             int s3)//0:既設プラント　1:新設工場
    {
        if ((null == con) || (null == operation)) {
            throw new NullPointerException(
                     "'con', 'operation' and 'contents' cannot be null.");
        }

        String contents
                 = new StringBuilder()
                          .append("[日時データ取込処理] ")
                          .append("取込対象日時=").append(s2)
                          .append(", 取込先区分(0:既設プラント　1:新設工場)=").append(s3)
                          .append(", 実行日=").append(s1)
                          .toString();
        Journal.recordJournal(con, operation, contents);
    }
}
