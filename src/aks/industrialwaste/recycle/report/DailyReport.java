package aks.industrialwaste.recycle.report;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Calendar;
import aks.industrialwaste.database.DBManager;
import java.sql.Statement;

public class DailyReport extends OSCReport {

    public static final int DAILY_REPORT1 = 1;//計量記録一覧
    public static final int DAILY_REPORT2 = 2;//種類別日報
    public static final int DAILY_REPORT3 = 3;//排出事業者別種類別日報
    public static final int DAILY_REPORT4 = 4;//運搬業者別種類別日報
    public static final int DAILY_REPORT5 = 5;//処分業者別種類別日報
    public static final int DAILY_REPORT6 = 6;//産業廃棄物管理帳票記録簿
    public static final int DAILY_REPORT7 = 7;//産業廃棄物自社分
    public static final int DAILY_REPORT8 = 24;//請求先別種類別日報
    public static final int DAILY_REPORT9 = 46;//排出事業場別種類別日報

    public static final int BILL_REPORT1 = 33;//請求一覧表
    public static final int BILL_REPORT2 = 35;//請求明細表
    public static final int BILL_REPORT3 = 37;//入金予定表
    public static final int BILL_REPORT4 = 38;//入金明細表
    public static final int CONT_REPORT5 = 34;//種類別売上表

    private Connection con_;
    private int reportID_;

    public DailyReport() {
        this(DAILY_REPORT1);
    }

    public DailyReport(int reportID) {
        if ((DAILY_REPORT1 != reportID)
                 && (DAILY_REPORT2 != reportID)
                 && (DAILY_REPORT3 != reportID)
                 && (DAILY_REPORT4 != reportID)
                 && (DAILY_REPORT5 != reportID)
                 && (DAILY_REPORT6 != reportID)
                 && (DAILY_REPORT7 != reportID)
                 && (DAILY_REPORT8 != reportID)
                 && (DAILY_REPORT9 != reportID)
                 && (BILL_REPORT1 != reportID)
                 && (BILL_REPORT2 != reportID)
                 && (BILL_REPORT3 != reportID)
                 && (BILL_REPORT4 != reportID)
                 && (CONT_REPORT5 != reportID)) {
            throw new IllegalArgumentException("'reportID' is not match.");
        }

        reportID_ = reportID;

    }

    /**
     * 指定条件で帳票を印刷する.
     *
     * @param con データベースとの接続
     * @param c 集計日
     * @param print_flag_ 印刷F 1:印刷　2:プレビュー
     */
    public void doPrint(Connection con, Calendar c, int print_flag_, int bill_id_) {
        con_ = con;

        try {
            //請求一覧表
            //請求明細表
            //入金予定表
            //入金明細表
            //売上一覧表
            printDailyReport(c, reportID_, print_flag_, bill_id_);

        } catch (Exception e) {
            logger_.warning(e.toString());
        }
    }

    public void doCSV(Connection con, Calendar c, int print_flag_, int bill_id_) {
        con_ = con;

        try {
            //計量記録一覧
            //種類別日報
            //排出事業者別種類別日報
            //運搬業者別種類別日報
            //処分業者別種類別日報
            //排出事業場別種類別日報
            //産業廃棄物管理帳票記録簿
            //産業廃棄物自社分
            //請求先別日報
            printDailyReport(c, reportID_, print_flag_, bill_id_);

        } catch (Exception e) {
            logger_.warning(e.toString());
        }
    }

    //日報印刷・プレビュー
    private Boolean printDailyReport(
             Calendar c,
             int report_sno,
             int print_num_,
             int bill_id_) {
        Boolean ret = false;

        try {
            Statement st = con_.createStatement();
            st.executeUpdate("begin;");
            try {
                String s = new StringBuilder()
                         .append("update m_report ")
                         .append("set term_date = ?, print_flag = 1, print_num = ?, yearly_num = ? ")
                         .append("where sno = ?;").toString();
                PreparedStatement ps = con_.prepareStatement(s);
                try {
                    ps.setObject(1, DBManager.convert(c.getTime()));
                    ps.setObject(2, print_num_);
                    ps.setObject(3, bill_id_);
                    ps.setObject(4, report_sno);
                    ps.executeUpdate();
                } finally {
                    ps.close();
                }

                st.executeUpdate("commit;");
            } catch (SQLException e) {
                st.executeUpdate("rollback;");
                throw e;
            } finally {
                st.close();
            }
        } catch (Exception e) {
            logger_.warning(e.toString());
            return false;
        }
        return ret;
    }
}
