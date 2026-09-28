package aks.keiryo.gui;

import java.sql.Connection;
import java.util.Calendar;
import aks.industrialwaste.util.Journal;

final class WeighPanelJournal {

    protected WeighPanelJournal() {
    }

    public static void recordWeightJournal(
             Connection con,
             String operation,
             int 記憶重量_) {
        if ((null == con) || (null == operation)) {
            throw new NullPointerException(
                     "'con', 'operation' and 'contents' cannot be null.");
        }

        StringBuilder sb
                 = new StringBuilder()
                          .append("[重量取り込み]")
                          .append(", 入構重量=").append(記憶重量_);
        Calendar o = Calendar.getInstance();
        if (o instanceof Calendar) {
            sb = sb.append(", 取込日時=").append(((Calendar) o).getTime());
        } else {
            sb = sb.append(", 取込日時=").append(o);
        }

        Journal.recordJournal(con, operation, sb.toString());
    }
}
