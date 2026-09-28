package aks.industrialwaste.recycle.ui.master.car;

//import aks.industrialwaste.recycle.ui.master.gastype.*;
import java.sql.Connection;
import java.util.Map;
import aks.industrialwaste.util.Journal;

/**
 * 車番マスタ画面の操作を記録します.
 */
final class CarJournal {

    protected CarJournal() {
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
                          .append("[車番マスタ], ")
                          .append("車番=").append(columns.get("car_no"))
                          .append(", 排出事業者=").append(columns.get("dis_ent"))
                          .append(", 運搬業者=").append(columns.get("transporter"))
                          .append(", 種類=").append(columns.get("kind"))
                          .append(", 排出事業場=").append(columns.get("dis_office"))
                          .append(", 処分業者=").append(columns.get("contractory"))
                          .append(", 風袋重量=").append(columns.get("tare_weight"))
                          //        .append(", S換算係数=").append(columns.get("s_coefficient"))
                          //        .append(", N換算係数=").append(columns.get("n_coefficient"))
                          //        .append(", 移動監視者資格=").append(columns.get("qualification"))
                          .toString();
        Journal.recordJournal(con, operation, contents);
    }
}
