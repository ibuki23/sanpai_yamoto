package aks.industrialwaste.recycle.ui.master.agreement;

import java.sql.Connection;
import java.util.Map;
import aks.industrialwaste.util.Journal;

final class AgreementJournal {

    protected AgreementJournal() {
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
                          .append("[契約マスタ], ")
                          .append("排出事業場ID=").append(columns.get("id"))
                          .append(", 名称=").append(columns.get("name"))
                          .append(", 正式名称=").append(columns.get("p_name"))
                          .append(", 所在地1=").append(columns.get("address1"))
                          .append(", 所在地2=").append(columns.get("address2"))
                          .append(", TEL=").append(columns.get("tel"))
                          .append(", 排出業者ID=").append(columns.get("ent_id"))
                          .append(", 排出業者名=").append(columns.get("ent_name"))
                          .append(", 契約開始日=").append(columns.get("contract_start_date"))
                          .append(", 契約終了日=").append(columns.get("contract_end_date"))
                          .append(", 契約自動更新=").append(columns.get("auto_update_flag"))
                          .append(", 交付担当者=").append(columns.get("tantou"))
                          .append(", 交付担当者　所属=").append(columns.get("tantou_syozoku"))//tantou_syozoku　追加
                          .append(", 運搬業者ID_01=").append(columns.get("trans_id01"))
                          .append(", 運搬業者名_01=").append(columns.get("trans_name01"))
                          .append(", 運搬業者ID_02=").append(columns.get("trans_id02"))
                          .append(", 運搬業者名_02=").append(columns.get("trans_name02"))
                          .append(", 運搬業者ID_03=").append(columns.get("trans_id03"))
                          .append(", 運搬業者名_03=").append(columns.get("trans_name03"))
                          .append(", 運搬業者ID_04=").append(columns.get("trans_id04"))
                          .append(", 運搬業者名_04=").append(columns.get("trans_name04"))
                          .append(", 運搬業者ID_05=").append(columns.get("trans_id05"))
                          .append(", 運搬業者名_05=").append(columns.get("trans_name05"))
                          .append(", 処分業者ID_01=").append(columns.get("cont_id01"))
                          .append(", 処分業者名_01=").append(columns.get("cont_name01"))
                          .append(", 処分業者ID_02=").append(columns.get("cont_id02"))
                          .append(", 処分業者名_02=").append(columns.get("cont_name02"))
                          .append(", 処分業者ID_03=").append(columns.get("cont_id03"))
                          .append(", 処分業者名_03=").append(columns.get("cont_name03"))
                          .append(", 処分業者ID_04=").append(columns.get("cont_id04"))
                          .append(", 処分業者名_04=").append(columns.get("cont_name04"))
                          .toString();
        Journal.recordJournal(con, operation, contents);
    }
}
