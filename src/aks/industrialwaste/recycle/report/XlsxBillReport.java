package aks.industrialwaste.recycle.report;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;
import aks.industrialwaste.database.DBManager;
import static aks.industrialwaste.recycle.ui.SystemSetting.SystemSetting.税率;
import static aks.industrialwaste.recycle.ui.weighlist.WeighList.領収書用途;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.channels.FileChannel;
import java.sql.Date;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DataFormat;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.PrintSetup;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

public class XlsxBillReport extends OSCReport {

    public static final int BILL_REPORT1 = 1;//請求書
    public static final int BILL_REPORT2 = 2;//請求一覧表
    public static final int BILL_REPORT3 = 3;//請求明細表
    public static final int BILL_REPORT4 = 4;//得意先管理台帳
    public static final int BILL_REPORT5 = 5;//入金明細表
    public static final int BILL_REPORT6 = 6;//売掛残高一覧表
    public static final int BILL_REPORT7 = 7;//領収書
    public static final int BILL_REPORT8 = 8;//計量時領収書

    private Connection con_;

    private CellStyle style, style2, cstyle, dstyle, dstyle1, dstyle2, tstyle,
             dtstyle, rstyle1, rstyle2, rstyle3, rstyle4, rstyle5, 游ゴシックstyle;
    private Workbook wb1;
    private Font font;
    private DataFormat format;

    /**
     * 月報を印刷するMonthlyReportのインスタンスを生成する
     *
     */
    public XlsxBillReport() {

        this(BILL_REPORT1);
    }

    /**
     * 帳票を印刷するMonthlyReportのインスタンスを生成する.
     *
     * @param reportID 印刷する帳票のID
     */
    public XlsxBillReport(int reportID) {
        if ((BILL_REPORT1 != reportID)
                 && (BILL_REPORT2 != reportID)
                 && (BILL_REPORT3 != reportID)
                 && (BILL_REPORT4 != reportID)
                 && (BILL_REPORT5 != reportID)
                 && (BILL_REPORT6 != reportID)
                 && (BILL_REPORT7 != reportID)
                 && (BILL_REPORT8 != reportID)) {
            throw new IllegalArgumentException("'reportID' is not match.");
        }
    }

    private void initCom() {

        font = wb1.createFont();
        font.setFontName("ＭＳ 明朝");
        font.setFontHeightInPoints((short) 9);
        format = wb1.createDataFormat();

        Font font游ゴシック = wb1.createFont();
        font.setFontName("ＭＳ 明朝");
        font.setFontHeightInPoints((short) 6);
        format = wb1.createDataFormat();

        style = wb1.createCellStyle();
        style.setBorderLeft(CellStyle.BORDER_THIN);
        style.setBorderRight(CellStyle.BORDER_THIN);
        style.setBorderTop(CellStyle.BORDER_THIN);
        style.setBorderBottom(CellStyle.BORDER_THIN);
        style.setShrinkToFit(true);
        style.setFont(font);

        style2 = wb1.createCellStyle();
        style2.setBorderLeft(CellStyle.BORDER_THIN);
        style2.setBorderRight(CellStyle.BORDER_THIN);
        style2.setBorderTop(CellStyle.BORDER_THIN);
        style2.setBorderBottom(CellStyle.BORDER_THIN);
        style2.setAlignment(CellStyle.ALIGN_CENTER);
        style2.setShrinkToFit(true);
        style2.setFont(font);

        dstyle = wb1.createCellStyle();
        dstyle.setBorderLeft(CellStyle.BORDER_NONE);
        dstyle.setBorderRight(CellStyle.BORDER_NONE);
        dstyle.setBorderTop(CellStyle.BORDER_NONE);
        dstyle.setBorderBottom(CellStyle.BORDER_MEDIUM);
        dstyle.setAlignment(CellStyle.ALIGN_RIGHT);
        dstyle.setDataFormat(format.getFormat("yyyy年MM月"));
        dstyle.setFont(font);

        dstyle1 = wb1.createCellStyle();
        dstyle1.setBorderLeft(CellStyle.BORDER_NONE);
        dstyle1.setBorderRight(CellStyle.BORDER_NONE);
        dstyle1.setBorderTop(CellStyle.BORDER_NONE);
        dstyle1.setBorderBottom(CellStyle.BORDER_MEDIUM);
        dstyle1.setAlignment(CellStyle.ALIGN_LEFT);
        dstyle1.setDataFormat(format.getFormat("- yyyy年MM月"));
        dstyle1.setFont(font);

        tstyle = wb1.createCellStyle();
        tstyle.setDataFormat(format.getFormat("MM月dd日"));
        tstyle.setFont(font);
        tstyle.setBorderLeft(CellStyle.BORDER_THIN);
        tstyle.setBorderRight(CellStyle.BORDER_THIN);
        tstyle.setBorderTop(CellStyle.BORDER_THIN);
        tstyle.setBorderBottom(CellStyle.BORDER_THIN);
        tstyle.setAlignment(CellStyle.ALIGN_CENTER);

        dtstyle = wb1.createCellStyle();
        dtstyle.setDataFormat(format.getFormat("yy/MM/dd hh:mm"));
        dtstyle.setFont(font);
        dtstyle.setBorderLeft(CellStyle.BORDER_THIN);
        dtstyle.setBorderRight(CellStyle.BORDER_THIN);
        dtstyle.setBorderTop(CellStyle.BORDER_THIN);
        dtstyle.setBorderBottom(CellStyle.BORDER_THIN);
        dtstyle.setAlignment(CellStyle.ALIGN_CENTER);

        dstyle2 = wb1.createCellStyle();
        dstyle2.setDataFormat(format.getFormat("yy/MM/dd"));
        dstyle2.setFont(font);
        dstyle2.setBorderLeft(CellStyle.BORDER_THIN);
        dstyle2.setBorderRight(CellStyle.BORDER_THIN);
        dstyle2.setBorderTop(CellStyle.BORDER_THIN);
        dstyle2.setBorderBottom(CellStyle.BORDER_THIN);
        dstyle2.setAlignment(CellStyle.ALIGN_CENTER);

        cstyle = wb1.createCellStyle();
        cstyle.setBorderLeft(CellStyle.BORDER_THIN);
        cstyle.setBorderRight(CellStyle.BORDER_THIN);
        cstyle.setBorderTop(CellStyle.BORDER_THIN);
        cstyle.setBorderBottom(CellStyle.BORDER_THIN);
        cstyle.setAlignment(CellStyle.ALIGN_RIGHT);
        cstyle.setDataFormat(format.getFormat("#,##0"));
        cstyle.setFont(font);

        rstyle1 = wb1.createCellStyle();
        rstyle1.setBorderLeft(CellStyle.BORDER_THIN);
        rstyle1.setBorderRight(CellStyle.BORDER_THIN);
        rstyle1.setBorderTop(CellStyle.BORDER_THIN);
        rstyle1.setBorderBottom(CellStyle.BORDER_THIN);
        rstyle1.setAlignment(CellStyle.ALIGN_RIGHT);
        rstyle1.setDataFormat(format.getFormat("#,##0.00"));
        rstyle1.setFont(font);

        rstyle2 = wb1.createCellStyle();
        rstyle2.setBorderLeft(CellStyle.BORDER_NONE);
        rstyle2.setBorderRight(CellStyle.BORDER_THIN);
        rstyle2.setBorderTop(CellStyle.BORDER_THIN);
        rstyle2.setBorderBottom(CellStyle.BORDER_THIN);
        rstyle2.setAlignment(CellStyle.ALIGN_RIGHT);
        rstyle2.setDataFormat(format.getFormat("#,##0\"kg\""));
        rstyle2.setFont(font);

        rstyle3 = wb1.createCellStyle();
        rstyle3.setBorderLeft(CellStyle.BORDER_NONE);
        rstyle3.setBorderRight(CellStyle.BORDER_THIN);
        rstyle3.setBorderTop(CellStyle.BORDER_THIN);
        rstyle3.setBorderBottom(CellStyle.BORDER_THIN);
        rstyle3.setAlignment(CellStyle.ALIGN_RIGHT);
        rstyle3.setFont(font);

        rstyle4 = wb1.createCellStyle();
        rstyle4.setBorderLeft(CellStyle.BORDER_THIN);
        rstyle4.setBorderRight(CellStyle.BORDER_THIN);
        rstyle4.setBorderTop(CellStyle.BORDER_THIN);
        rstyle4.setBorderBottom(CellStyle.BORDER_THIN);
        rstyle4.setAlignment(CellStyle.ALIGN_LEFT);
        rstyle4.setShrinkToFit(true);
//      rstyle4.setWrapText(true);//テキストを折り返す
        rstyle4.setFont(font);

        rstyle5 = wb1.createCellStyle();
        rstyle5.setBorderLeft(CellStyle.BORDER_NONE);
        rstyle5.setBorderRight(CellStyle.BORDER_THIN);
        rstyle5.setBorderTop(CellStyle.BORDER_THIN);
        rstyle5.setBorderBottom(CellStyle.BORDER_THIN);
        rstyle5.setAlignment(CellStyle.ALIGN_LEFT);
        rstyle5.setFont(font);

        游ゴシックstyle = wb1.createCellStyle();
        游ゴシックstyle.setAlignment(CellStyle.ALIGN_CENTER);
        游ゴシックstyle.setShrinkToFit(true);
        游ゴシックstyle.setFont(font游ゴシック);
        游ゴシックstyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
    }

    /**
     * 指定条件で帳票を印刷する.
     *
     * @param con データベースとの接続
     * @param report_id 対象の報告
     * @param term_date 対象月
     * @param bill_id 請求先コード
     * @param eda_no1 開始枝番
     * @param eda_no2 終了枝番
     * @param print_no 印刷区分(0:すべて　1:表紙のみ　2:明細のみ)
     */
    public int print(
             Connection con,
             int report_id,
             Date term_date,
             int bill_id,
             int eda_no1,
             int eda_no2,
             int print_no,
             long bill_sno) {
        con_ = con;
        String template1 = "";
        String template2 = "";

        try {

            if (BILL_REPORT1 == report_id) {
                //請求書
                template1 = DBManager.PAYMENT_FLAG
                         ? "C:\\Excel2013\\請求書入金有.xlsx"
                         : "C:\\Excel2013\\請求書入金無.xlsx";
            } else if (BILL_REPORT2 == report_id) {
                //請求一覧表
                template1 = "C:\\Excel2013\\請求一覧表.xlsx";
            } else if (BILL_REPORT3 == report_id) {
                //請求明細表
                template1 = "C:\\Excel2013\\請求明細表.xlsx";
            } else if (BILL_REPORT4 == report_id) {
                //得意先管理台帳
                template1 = "C:\\Excel2013\\得意先管理台帳.xlsx";
            } else if (BILL_REPORT5 == report_id) {
                //入金明細表
                template1 = "C:\\Excel2013\\入金明細表.xlsx";
            } else if (BILL_REPORT6 == report_id) {
                //売掛残高一覧表
                template1 = "C:\\Excel2013\\売掛残高一覧表.xlsx";
            } else if (BILL_REPORT7 == report_id) {
                //領収書発行
                template1 = "C:\\yamakuni_exe\\template\\領収書.xlsx";
                template2 = "C:\\yamakuni_exe\\template\\領収書控.xlsx";
            } else if (BILL_REPORT8 == report_id) {
                //計量時領収書発行
                template1 = "C:\\yamakuni_exe\\template\\領収書.xlsx";
                template2 = "C:\\yamakuni_exe\\template\\領収書控.xlsx";
            }

            // テンプレートファイルをローカルにコピー
            File templateFile1 = new File(template1);
            File templateFile2 = new File(template2);
            File tmpFile1 = new File("tmp", templateFile1.getName());
            File tmpFile2 = new File("tmp", templateFile2.getName());

            // テンプレートファイルをローカルにコピー
            copyTransfer(templateFile1.getPath(), tmpFile1.getPath());
            copyTransfer(templateFile2.getPath(), tmpFile2.getPath());

            try {

                if (BILL_REPORT1 == report_id) {
                    //請求書
                    printReport1(tmpFile1.getPath(), tmpFile1.getAbsolutePath(), con_, term_date, bill_id, eda_no1, eda_no2, print_no, bill_sno);
                } else if (BILL_REPORT2 == report_id) {
                    //請求一覧表
                    printReport2(tmpFile1.getPath(), tmpFile1.getAbsolutePath(), con_, term_date, bill_id, eda_no1, eda_no2, print_no);
                } else if (BILL_REPORT3 == report_id) {
                    //請求明細表
                    printReport3(tmpFile1.getPath(), tmpFile1.getAbsolutePath(), con_, term_date, bill_id, eda_no1, eda_no2, print_no);
                } else if (BILL_REPORT4 == report_id) {
                    //得意先管理台帳
                    printReport4(tmpFile1.getPath(), tmpFile1.getAbsolutePath(), con_, term_date, bill_id, eda_no1, eda_no2, print_no);
                } else if (BILL_REPORT5 == report_id) {
                    //入金明細表
                    printReport5(tmpFile1.getPath(), tmpFile1.getAbsolutePath(), con_, term_date, bill_id, eda_no1, eda_no2, print_no);
                    ;
                } else if (BILL_REPORT6 == report_id) {
                    //売掛残高一覧表
                    printReport6(tmpFile1.getPath(), tmpFile1.getAbsolutePath(), con_, term_date, bill_id, eda_no1, eda_no2, print_no);

                } else if (BILL_REPORT7 == report_id) {
                    //領収書
                    print領収書(tmpFile1.getPath(), tmpFile1.getAbsolutePath(), con_, bill_id);
                    print領収書控え(tmpFile2.getPath(), tmpFile2.getAbsolutePath(), con_, bill_id);
                } else if (BILL_REPORT8 == report_id) {
                    //領収書
                    print領収書(tmpFile1.getPath(), tmpFile1.getAbsolutePath(), con_);
                    print領収書控え(tmpFile2.getPath(), tmpFile2.getAbsolutePath(), con_);
                }

            } finally {

            }

        } catch (Exception e) {
            logger_.warning(e.toString());
        }

        return 0;
    }

    /**
     * コピー元のパス[srcPath]から、コピー先のパス[destPath]へ ファイルのコピーを行います。
     * コピー処理にはFileChannel#transferToメソッドを利用します。 尚、コピー処理終了後、入力・出力のチャネルをクローズします。
     *
     * @param srcPath コピー元のパス
     * @param destPath コピー先のパス
     * @throws IOException 何らかの入出力処理例外が発生した場合
     */
    public static void copyTransfer(String srcPath, String destPath)
             throws IOException {

        FileChannel srcChannel = new FileInputStream(srcPath).getChannel();
        FileChannel destChannel = new FileOutputStream(destPath).getChannel();
        try {
            srcChannel.transferTo(0, srcChannel.size(), destChannel);
        } finally {
            srcChannel.close();
            destChannel.close();
        }

    }

    //プリンター名取得
    private String[] getPrinter() {

        String[] printer = new String[4];
        printer[0] = "";
        printer[1] = "";
        printer[2] = "";
        printer[3] = "";

        try {
            Statement st = con_.createStatement();
            try {
                String s = new StringBuilder()
                         .append("select weigh_printer, default_printer, manifesto_printer, 領収書_printer ")
                         .append("from m_system ")
                         .append("where sno = 1;")
                         .toString();
                ResultSet rs = st.executeQuery(s);
                if (rs.next()) {
                    printer[0] = rs.getString("weigh_printer");
                    printer[1] = rs.getString("default_printer");
                    printer[2] = rs.getString("manifesto_printer");
                    printer[3] = rs.getString("領収書_printer");
                }
            } finally {
                st.close();
            }

        } catch (SQLException e) {
        }

        return printer;

    }

    public static void doPrintXlsx(String filePath, String printer1, String printer2,
             int papers, boolean directPrint) {
        try {
            if (directPrint) {
                // 直接印刷：XlsPrint.ps1 側で printer1 を指定して印刷する
                run(
                         "powershell.exe",
                         "-NoProfile",
                         "-ExecutionPolicy", "Bypass",
                         "-File", "tmp\\XlsPrint.ps1",
                         "-FilePath", filePath,
                         "-PrinterName", printer1,
                         "-PrintCount", String.valueOf(papers)
                );
            } else {
                // プレビュー：Excelファイルを開くだけ
                run("cmd", "/c", "start", "", filePath);
            }

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.out.println("失敗(InterruptedException)");
        } catch (IOException e) {
            System.out.println("失敗(IOException): " + e.getMessage());
        }
    }

    private static void run(String... command) throws IOException, InterruptedException {
        Process p = new ProcessBuilder(command)
                 .redirectErrorStream(true)
                 .inheritIO()
                 .start();

        int exit = p.waitFor();
        if (exit != 0) {
            throw new IOException("Command failed (" + exit + "): " + Arrays.toString(command));
        }
    }

    //請求書
    private Boolean printReport1(
             String file_path,
             String storeFol,
             Connection con,
             Date term_date,
             int bill_id,
             int eda_no1,
             int eda_no2,
             int print_no,
             long bill_sno) {

        boolean ret = false;
        int cnt00 = 0;
        try {
            InputStream in1 = new FileInputStream(file_path);
            wb1 = WorkbookFactory.create(in1);
            initCom();

            Sheet sheet1 = wb1.getSheet("様式a");
            Sheet sheet02 = null;
            Sheet sheet03 = null;

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
            SimpleDateFormat sdf_dd = new SimpleDateFormat("yyyyMMdd");
            SimpleDateFormat sdf00 = new SimpleDateFormat("yyyy年MM月dd日～");
            SimpleDateFormat sdf01 = new SimpleDateFormat("yyyy年MM月dd日");
            SimpleDateFormat sdf02 = new SimpleDateFormat("yyyy/MM/dd HH:mm");

            FileOutputStream out1 = null;
            File out2 = null;
            File print_out = null;

            Row row, row1, row2;
            Cell cell;
            Object value = null;
            String[] printer = getPrinter();

            int v現在ページcnt = 0;
            int edaカウント = 0;
            boolean b直接印刷 = false;
            String m自社住所 = "";
            String m自社名称 = "";
            String m自社代表者名 = "";
            String m自社電話 = "";
            String m自社fax = "";
            String m振込銀行期間名1 = "";
            String m振込銀行期間名2 = "";
            String m振込銀行口座1 = "";
            String m振込銀行口座2 = "";

            //自社情報取得/////////////////////////////////////////////////////
            //印刷するかプレビューするか確認
            String s0 = new StringBuilder()
                     .append("select * ")
                     .append(" from m_manifesto where sno = 1; ")
                     .toString();

            PreparedStatement ps0 = con.prepareStatement(s0);

            try {
                ResultSet rs0 = ps0.executeQuery();

                if (rs0.next()) {
                    b直接印刷 = rs0.getBoolean("bill_print_flag");
                    m自社住所 = rs0.getString("trust_name1");
                    m自社名称 = rs0.getString("company_name");
                    m自社代表者名 = "代表取締役　" + rs0.getString("trust_name2");
                    m自社電話 = "TEL " + rs0.getString("trust_name3");
                    m自社fax = "FAX " + rs0.getString("trust_name4");
                    m振込銀行期間名1 = rs0.getString("trans_name1");
                    m振込銀行口座1 = rs0.getString("trans_name2");
                    m振込銀行期間名2 = rs0.getString("trans_name3");
                    m振込銀行口座2 = rs0.getString("trans_name4");
                }

            } finally {
                ps0.close();
            }

            //請求先情報取得/////////////////////////////////////////////////////////////
            String hs = String.format("select * from v0_bill where id = %s;", bill_id);
            String s請求郵便番号 = "";
            String s請求住所1 = "";
            String s請求住所2 = "";
            String s請求名称 = "";
            String s請求起算日 = "";
            String s請求書No = "";
            Date start_date = null;
            Date end_date = null;

            PreparedStatement hps = con.prepareStatement(hs);
            try {
                ResultSet hrs = hps.executeQuery();
                if (hrs.next()) {
                    s請求郵便番号 = hrs.getString("zip_code");
                    s請求住所1 = hrs.getString("address1");
                    s請求住所2 = hrs.getString("address2");
                    s請求名称 = hrs.getString("p_name") + "　" + hrs.getString("keisyo");
                }

                hs = String.format("select * from v1_d_bill where sno = %s;", bill_sno);
                hps = con.prepareStatement(hs);
                hrs = hps.executeQuery();
                if (hrs.next()) {
                    s請求書No = "No." + hrs.getString("bill_report_no");
                    s請求起算日 = "(" + sdf00.format(hrs.getDate("start_date")) + sdf01.format(hrs.getDate("end_date")) + ")";

                    start_date = hrs.getDate("start_date");
                    end_date = hrs.getDate("end_date");
                }
            } catch (SQLException e) {

            }

            String sh0 = new StringBuilder()
                     .append("select * ")
                     .append(" from v1_d_bill ")
                     .append(" where sno = ? ")
                     .append(" and total_value <> 0 ;")
                     .toString();

            PreparedStatement psh0 = con.prepareStatement(sh0);

            try {
                psh0.setObject(1, bill_sno);

                ResultSet rsh0 = psh0.executeQuery();

                if (rsh0.next()) {
                    row1 = sheet1.getRow(3);//4行目スタート
                    cell = row1.getCell(2);//C列
                    cell.setCellValue(s請求郵便番号);
                    row1 = sheet1.getRow(4);//5行目スタート
                    cell = row1.getCell(2);//C列
                    cell.setCellValue(s請求住所1);
                    row1 = sheet1.getRow(5);//6行目スタート
                    cell = row1.getCell(2);//C列
                    cell.setCellValue(s請求住所2);
                    row1 = sheet1.getRow(6);//7行目スタート
                    cell = row1.getCell(2);//C列
                    cell.setCellValue(s請求名称);
                    row1 = sheet1.getRow(3);//4行目スタート
                    cell = row1.getCell(6);//G列
                    cell.setCellValue(s請求起算日);
                    row1 = sheet1.getRow(1);//2行目スタート
                    cell = row1.getCell(7);//H列
                    cell.setCellValue(s請求書No);

                    row1 = sheet1.getRow(5);//6行目
                    cell = row1.getCell(6);//G列
                    cell.setCellValue(m自社住所);

                    row1 = sheet1.getRow(6);//7行目
                    cell = row1.getCell(6);//G列
                    cell.setCellValue(m自社名称);

                    row1 = sheet1.getRow(8);//9行目
                    cell = row1.getCell(6);//G列
                    cell.setCellValue(m自社代表者名);

                    row1 = sheet1.getRow(9);//10行目
                    cell = row1.getCell(6);//G列
                    cell.setCellValue(m自社電話 + "　" + m自社fax);

                    row1 = sheet1.getRow(10);//11行目
                    cell = row1.getCell(6);//G列
                    cell.setCellValue(m振込銀行期間名1 + " " + m振込銀行口座1);

                    row1 = sheet1.getRow(11);//12行目
                    cell = row1.getCell(6);//G列
                    cell.setCellValue(m振込銀行期間名2 + " " + m振込銀行口座2);

                    row1 = sheet1.getRow(14);//15行目スタート
                    cell = row1.getCell(1);//B列
                    value = rsh0.getInt("value_1");//前回請求額
                    cell.setCellValue((Integer) value);
                    cell = row1.getCell(3);//D列
                    value = rsh0.getInt("value_2");//今回入金額
                    cell.setCellValue((Integer) value);
                    cell = row1.getCell(4);//E列
                    value = rsh0.getInt("value_5");//今回売上額
                    cell.setCellValue((Integer) value);
                    cell = row1.getCell(5);//F列
                    value = rsh0.getInt("value_6");//消費税額
                    cell.setCellValue((Integer) value);
                    cell = row1.getCell(6);//G列
                    value = rsh0.getInt("value_9");//産廃税
                    cell.setCellValue((Integer) value);
                    cell = row1.getCell(7);//H列
                    value = rsh0.getInt("total_value");//合計金額
                    cell.setCellValue((Integer) value);
                }

            } finally {
                psh0.close();
            }
            //
            /////////////////////////////////////////////////////////////////////////////////
            String s = new StringBuilder()
                     .append("select ")
                     .append("max(bill_id)as bill_id, ")//請求先コード
                     .append("max(m_bill_name)as m_bill_name, ")//--請求先名
                     .append("max(dis_office_id)as max_office_id, ")//現場コード
                     .append("min(dis_office_id)as min_office_id, ")//現場コード
                     .append("max(eda_office_id)as eda_office_id, ")//--枝番
                     .append("max(office_name)as office_name, ")//--発地1
                     .append("max(office_name2)as office_name2, ")//--発地2
                     .append("max(cont_office_name)as cont_office_name, ")//--着地
                     .append("sum(bill_price)as bill_price, ")
                     .append("case when max(customer_tax_no) = 0 then ")
                     .append("   round((sum(bill_price)::double precision *  ")
                     .append("   max(tax_rate)::smallint)::double precision  ")
                     .append("    / 100::double precision)::integer ")
                     .append("when max(customer_tax_no) = 1 then ")
                     .append("   trunc((sum(bill_price)::double precision *  ")
                     .append("   max(tax_rate)::smallint)::double precision  ")
                     .append("    / 100::double precision)::integer ")
                     .append("when max(customer_tax_no) = 2 then ")
                     .append("   trunc((sum(bill_price)::double precision *  ")
                     .append("   max(tax_rate)::smallint)::double precision  ")
                     .append("    / 100::double precision + 0.99::double precision)::integer ")
                     .append("end as tax_price, ")
                     .append("sum(advances)as advances ")
                     .append("from v1_d_bill_detail ")
                     .append("where bill_id = ? and term_date >= ? and term_date <= ? ")
                     .append("and eda_office_id >= ? and eda_office_id <= ? ")
                     .append("and delete_flag = false ")
                     .append("group by eda_office_id ")
                     .append("order by eda_office_id limit 38; ")
                     .toString();
            PreparedStatement ps = con.prepareStatement(s);
            try {
                ps.setObject(1, bill_id);
                ps.setObject(2, start_date);
                ps.setObject(3, end_date);
                ps.setObject(4, eda_no1);
                ps.setObject(5, eda_no2);
                ResultSet rs = ps.executeQuery();

                int i現在行 = 17;
                int id現在行 = 17;
                int id現在レコード = 0;
                try {
                    while (rs.next()) {
                        edaカウント++;

                        row1 = sheet1.getRow(i現在行);//18行目スタート
                        cell = row1.getCell(1);//B列
                        value = rs.getInt("eda_office_id");//eda_office_id
                        cell.setCellValue((Integer) value);
                        cell = row1.getCell(2);//C列
                        value = rs.getInt("min_office_id") == rs.getInt("max_office_id")
                                 ? rs.getString("office_name2")
                                 : rs.getString("office_name2") + "　　　他";//現場名（発地）
                        cell.setCellValue((String) value);

                        cell = row1.getCell(5);//F列
                        value = rs.getInt("bill_price");//売上金額
                        cell.setCellValue((Integer) value);

                        cell = row1.getCell(6);//G列
                        value = rs.getInt("tax_price");//消費税
                        cell.setCellValue((Integer) value);

                        cell = row1.getCell(7);//H列
                        value = rs.getInt("advances");//産廃税
                        cell.setCellValue((Integer) value);

                        cell = row1.getCell(8);//I列
                        value = rs.getInt("bill_price") + rs.getInt("tax_price") + rs.getInt("advances");//合計金額
                        cell.setCellValue((Integer) value);

                        //枝番ごとの明細処理///////////////////////////////////////////
                        s = new StringBuilder()
                                 .append("select * ")
                                 .append("from v1_d_bill_detail ")
                                 .append("where bill_id = ? and term_date >= ? and term_date <= ? ")
                                 .append("and eda_office_id = ? ")
                                 .append("and (bill_price <> 0 or advances <> 0)")
                                 .append("and delete_flag = false ")
                                 .append("order by eda_office_id, dis_office_id, term_date, car_no, goods_id, bill_price desc; ")
                                 .toString();

                        PreparedStatement psd = con.prepareStatement(s);
                        psd.setObject(1, bill_id);
                        psd.setObject(2, start_date);
                        psd.setObject(3, end_date);
                        psd.setObject(4, rs.getInt("eda_office_id"));

                        ResultSet rsd = psd.executeQuery();

                        id現在行 = 17;
                        v現在ページcnt = 1;
                        id現在レコード = 1;
                        while (rsd.next()) {

                            if (id現在レコード <= 24) {
                                if (id現在レコード == 1 && v現在ページcnt == 1) {
                                    sheet02 = wb1.cloneSheet(1);
                                    wb1.setSheetOrder(sheet02.getSheetName(), wb1.getNumberOfSheets() - 1);
                                    if (id現在行 == 17) {
                                        row1 = sheet02.getRow(3);//4行目スタート
                                        cell = row1.getCell(2);//C列
                                        cell.setCellValue(s請求郵便番号);
                                        row1 = sheet02.getRow(4);//5行目スタート
                                        cell = row1.getCell(2);//C列
                                        cell.setCellValue(s請求住所1);
                                        row1 = sheet02.getRow(5);//6行目スタート
                                        cell = row1.getCell(2);//C列
                                        cell.setCellValue(s請求住所2);
                                        row1 = sheet02.getRow(6);//7行目スタート
                                        cell = row1.getCell(2);//C列
                                        cell.setCellValue(s請求名称);
                                        row1 = sheet02.getRow(3);//4行目スタート
                                        cell = row1.getCell(8);//I列
                                        cell.setCellValue(s請求起算日);
                                        row1 = sheet02.getRow(1);//2行目スタート
                                        cell = row1.getCell(11);//L列
                                        cell.setCellValue(s請求書No + "-" + String.format("%1$02d", rs.getInt("eda_office_id")));
                                        //印刷日
                                        row1 = sheet02.getRow(3);//4行目
                                        cell = row1.getCell(11);//L列
                                        value = sdf01.format(DBManager.convert((Calendar.getInstance()).getTime()));
                                        cell.setCellValue((String) value);

                                        row1 = sheet02.getRow(5);//6行目
                                        cell = row1.getCell(7);//H列
                                        cell.setCellValue(m自社住所);

                                        row1 = sheet02.getRow(6);//7行目
                                        cell = row1.getCell(7);//H列
                                        cell.setCellValue(m自社名称);

                                        row1 = sheet02.getRow(8);//9行目
                                        cell = row1.getCell(7);//H列
                                        cell.setCellValue(m自社代表者名);

                                        row1 = sheet02.getRow(9);//10行目
                                        cell = row1.getCell(7);//H列
                                        cell.setCellValue(m自社電話 + "　" + m自社fax);

                                        row1 = sheet02.getRow(10);//11行目
                                        cell = row1.getCell(8);//I列
                                        cell.setCellValue(m振込銀行期間名1 + " " + m振込銀行口座1);

                                        row1 = sheet02.getRow(11);//12行目
                                        cell = row1.getCell(8);//I列
                                        cell.setCellValue(m振込銀行期間名2 + " " + m振込銀行口座2);

                                        //現場ごとの金額セット
                                        row1 = sheet02.getRow(14);//15行目
                                        cell = row1.getCell(4);//G列
                                        value = rs.getInt("bill_price");//売上金額
                                        cell.setCellValue((Integer) value);

                                        cell = row1.getCell(5);//F列
                                        value = rs.getInt("tax_price");//消費税
                                        cell.setCellValue((Integer) value);

                                        cell = row1.getCell(7);//H列
                                        value = rs.getInt("advances");//立替
                                        cell.setCellValue((Integer) value);

                                        cell = row1.getCell(10);//K列
                                        value = rs.getInt("bill_price") + rs.getInt("tax_price") + rs.getInt("advances");//合計金額
                                        cell.setCellValue((Integer) value);

                                        //印刷範囲
                                        wb1.setPrintArea(wb1.getNumberOfSheets() - 1, "$A$1:$L$43");
                                        // 印刷用紙指定
                                        PrintSetup prints = sheet02.getPrintSetup();
                                        prints.setPaperSize(PrintSetup.A4_PAPERSIZE);
                                        prints.setLandscape(true);    //横向き
                                    }
                                }
                                row1 = sheet02.getRow(id現在行);//18行目スタート
                                cell = row1.getCell(1);//B列
                                value = rsd.getDate("term_date");//所属日
                                cell.setCellValue((Date) value);

                                cell = row1.getCell(2);//C列
                                value = rsd.getString("bill_name");//請求品目名
                                cell.setCellValue((String) value);

                                cell = row1.getCell(3);//D列
                                value = rsd.getString("office_name2");//現場名（発地）
                                cell.setCellValue((String) value);

                                cell = row1.getCell(4);//E列
                                value = rsd.getString("manifesto_no");//交付番号
                                cell.setCellValue((String) value);

                                //bill_value
                                cell = row1.getCell(5);//F列
                                value = rsd.getDouble("bill_value");//請求数量
                                cell.setCellValue((Double) value);
                                //bill_unit
                                cell = row1.getCell(7);//H列
                                value = rsd.getString("bill_unit");//単位
                                cell.setCellValue((String) value);

                                cell = row1.getCell(8);//I列
                                value = rsd.getInt("unit_price");//単価
                                cell.setCellValue((Integer) value);

                                //bill_price
                                cell = row1.getCell(9);//J列
                                value = rsd.getInt("bill_price");//金額
                                cell.setCellValue((Integer) value);

                                cell = row1.getCell(10);//K列
                                value = rsd.getInt("advances");//産廃税
                                cell.setCellValue((Integer) value);

                                cell = row1.getCell(11);//L列
                                value = rsd.getString("remark01");//単位
                                cell.setCellValue((String) value);
                            } else {

                                if (id現在レコード % 24 == 1) {
                                    sheet03 = wb1.cloneSheet(2);
                                    wb1.setSheetOrder(sheet03.getSheetName(), wb1.getNumberOfSheets() - 1);
                                    v現在ページcnt++;
                                    id現在行 = 17;
                                    if (id現在行 == 17) {
                                        row1 = sheet03.getRow(3);//4行目スタート
                                        cell = row1.getCell(2);//C列
                                        cell.setCellValue(s請求郵便番号);
                                        row1 = sheet03.getRow(4);//5行目スタート
                                        cell = row1.getCell(2);//C列
                                        cell.setCellValue(s請求住所1);
                                        row1 = sheet03.getRow(5);//6行目スタート
                                        cell = row1.getCell(2);//C列
                                        cell.setCellValue(s請求住所2);
                                        row1 = sheet03.getRow(6);//7行目スタート
                                        cell = row1.getCell(2);//C列
                                        cell.setCellValue(s請求名称);
                                        row1 = sheet03.getRow(3);//4行目スタート
                                        cell = row1.getCell(9);//J列
                                        cell.setCellValue(s請求起算日);
                                        row1 = sheet03.getRow(1);//2行目スタート
                                        cell = row1.getCell(11);//L列
                                        cell.setCellValue(s請求書No + "-" + String.format("%1$02d", rs.getInt("eda_office_id")));
                                        //印刷日
                                        row1 = sheet03.getRow(3);//4行目
                                        cell = row1.getCell(11);//M列
                                        value = sdf01.format(DBManager.convert((Calendar.getInstance()).getTime()));
                                        cell.setCellValue((String) value);

                                        row1 = sheet03.getRow(5);//6行目
                                        cell = row1.getCell(7);//I列
                                        cell.setCellValue(m自社住所);

                                        row1 = sheet03.getRow(6);//7行目
                                        cell = row1.getCell(7);//I列
                                        cell.setCellValue(m自社名称);

                                        row1 = sheet03.getRow(9);//10行目
                                        cell = row1.getCell(7);//I列
                                        cell.setCellValue(m自社電話 + "　" + m自社fax);

                                        row1 = sheet03.getRow(10);//11行目
                                        cell = row1.getCell(8);//J列
                                        cell.setCellValue(m振込銀行期間名1 + " " + m振込銀行口座1);

                                        row1 = sheet03.getRow(11);//12行目
                                        cell = row1.getCell(8);//J列
                                        cell.setCellValue(m振込銀行期間名2 + " " + m振込銀行口座2);

                                        //印刷範囲
                                        wb1.setPrintArea(wb1.getNumberOfSheets() - 1, "$A$1:$L$43");
                                        // 印刷用紙指定
                                        PrintSetup prints = sheet03.getPrintSetup();
                                        prints.setPaperSize(PrintSetup.A4_PAPERSIZE);
                                        prints.setLandscape(true);    //横向き

                                    }
                                }
                                row1 = sheet03.getRow(id現在行);//18行目スタート
                                cell = row1.getCell(1);//B列
                                value = rsd.getDate("term_date");//所属日
                                cell.setCellValue((Date) value);

                                cell = row1.getCell(2);//C列
                                value = rsd.getString("bill_name");//請求品目名
                                cell.setCellValue((String) value);

                                cell = row1.getCell(3);//D列
                                value = rsd.getString("office_name2");//現場名（発地）
                                cell.setCellValue((String) value);

                                cell = row1.getCell(4);//E列
                                value = rsd.getString("manifesto_no");//交付番号
                                cell.setCellValue((String) value);

                                //bill_value
                                cell = row1.getCell(5);//F列
                                value = rsd.getDouble("bill_value");//請求数量
                                cell.setCellValue((Double) value);
                                //bill_unit
                                cell = row1.getCell(7);//H列
                                value = rsd.getString("bill_unit");//単位
                                cell.setCellValue((String) value);

                                cell = row1.getCell(8);//I列
                                value = rsd.getInt("unit_price");//単価
                                cell.setCellValue((Integer) value);

                                //bill_price
                                cell = row1.getCell(9);//J列
                                value = rsd.getInt("bill_price");//金額
                                cell.setCellValue((Integer) value);

                                cell = row1.getCell(10);//K列
                                value = rsd.getInt("advances");//立替
                                cell.setCellValue((Integer) value);

                                cell = row1.getCell(11);//L列
                                value = rsd.getString("remark01");//単位
                                cell.setCellValue((String) value);
                            }
                            id現在行++;
                            id現在レコード++;
                        }

                        /////////////////////////////////////////////////////////////
                        i現在行++;

                    }

                } catch (Exception ie1) {
                    System.out.println(ie1.toString() + String.valueOf(i現在行));
                }

            } finally {
                ps.close();
            }

            //シート再計算
            sheet1.setForceFormulaRecalculation(true);
            sheet1.setSelected(true);

            if (print_no == 0) {
                //すべて印刷
                wb1.removeSheetAt(1);//様式bを非表示
                wb1.removeSheetAt(1);//様式cを非表示
            } else if (print_no == 1) {
                //表紙のみ印刷
                int i不要ページ = wb1.getNumberOfSheets();
                for (int i = 1; i < i不要ページ; i++) {
                    wb1.removeSheetAt(1);//様式cを非表示
                }
            } else if (print_no == 2) {
                //明細のみ印刷
                wb1.removeSheetAt(0);//様式aを非表示
                wb1.removeSheetAt(0);//様式bを非表示
                wb1.removeSheetAt(0);//様式cを非表示
            }

            for (int i = 0; i < wb1.getNumberOfSheets(); i++) {
                wb1.getSheetAt(i).setSelected(true);
            }

            try {
                //自身のtmpに保存
                out1 = new FileOutputStream(storeFol);
                wb1.write(out1);
                //スクリプトから印刷
                print_out = new File(storeFol);
                doPrintXlsx(print_out.getAbsolutePath(), printer[1], printer[1], 1, b直接印刷);

            } catch (IOException e) {
                System.out.println(e.toString());
            } finally {
                try {
                    out1.close();

                } catch (IOException e) {
                    System.out.println(e.toString());
                }
            }

        } catch (Exception ie1) {
            System.out.println(ie1.toString() + String.valueOf(cnt00));
        }

        return ret;
    }

    /*
   * term_date:所属月
   * bill_id:締め日
   * eda_no1:使用しない
   * eda_no2:使用しない
   * print_no:使用しない
     */
    //請求一覧表
    private Boolean printReport2(
             String file_path,
             String storeFol,
             Connection con,
             Date term_date,
             int bill_id,
             int eda_no1,
             int eda_no2,
             int print_no) {

        boolean ret = false;
        int cnt00 = 0;
        try {
            InputStream in1 = new FileInputStream(file_path);
            wb1 = WorkbookFactory.create(in1);
            initCom();
            Sheet sheet1 = wb1.getSheet("様式a");

            SimpleDateFormat sdf01 = new SimpleDateFormat("yyyy年MM月dd日");
            SimpleDateFormat sdf03 = new SimpleDateFormat("yyyy年MM月");
            SimpleDateFormat sdf04 = new SimpleDateFormat("MM月dd日");

            FileOutputStream out1 = null;
            File print_out = null;

            Row row1;
            Cell cell;
            Object value = null;
            String[] printer = getPrinter();

            boolean b直接印刷 = false;

            String s = new StringBuilder()
                     .append("select * from v1_d_bill ")
                     .append("where term_month = ? ")
                     .append("and bundle_day = ? ")
                     .append("order by bill_id; ")
                     .toString();
            PreparedStatement ps = con.prepareStatement(s);
            try {
                ps.setObject(1, term_date);
                ps.setObject(2, bill_id);
                ResultSet rs = ps.executeQuery();

                int i現在行 = 4;

                //印刷日
                row1 = sheet1.getRow(1);//2行目
                cell = row1.getCell(7);//H列
                value = "作成日 : " + sdf01.format(DBManager.convert((Calendar.getInstance()).getTime()));
                cell.setCellValue((String) value);
                //対象月
                row1 = sheet1.getRow(0);//1行目
                cell = row1.getCell(0);//A列
                value = sdf03.format(term_date) + String.format(" %s日締め分", bill_id == 0 ? "末" : String.valueOf(bill_id));
                cell.setCellValue((String) value);

                while (rs.next()) {

                    //ID
                    row1 = sheet1.createRow(i現在行);//5行目スタート
                    row1.setHeightInPoints(18);//行の高さを指定
                    cell = row1.createCell(0);//A列
                    value = rs.getInt("bill_id");
                    cell.setCellValue((Integer) value);
                    cell.setCellStyle(style);
                    //請求先名
                    cell = row1.createCell(1);//B列
                    value = rs.getString("bill_name");
                    cell.setCellValue((String) value);
                    cell.setCellStyle(style);
                    //請求日
                    cell = row1.createCell(2);//C列
                    value = rs.getDate("term_date");
                    cell.setCellValue(sdf04.format((Date) value));
                    cell.setCellStyle(tstyle);
                    //請求番号
                    cell = row1.createCell(3);//D列
                    value = rs.getString("bill_report_no");
                    cell.setCellValue((String) value);
                    cell.setCellStyle(style2);
                    //前回請求額
                    cell = row1.createCell(4);//E列
                    value = rs.getInt("value_1");
                    cell.setCellValue((Integer) value);
                    cell.setCellStyle(cstyle);
                    //入金額
                    cell = row1.createCell(5);//F列
                    value = rs.getInt("value_2");
                    cell.setCellValue((Integer) value);
                    cell.setCellStyle(cstyle);
                    //繰越額
                    cell = row1.createCell(6);//G列
                    value = rs.getInt("value_4");
                    cell.setCellValue((Integer) value);
                    cell.setCellStyle(cstyle);
                    //今回売上額
                    cell = row1.createCell(7);//H列
                    value = rs.getInt("value_5");
                    cell.setCellValue((Integer) value);
                    cell.setCellStyle(cstyle);
                    //消費税額
                    cell = row1.createCell(8);//I列
                    value = rs.getInt("value_6");
                    cell.setCellValue((Integer) value);
                    cell.setCellStyle(cstyle);
                    //調整額
                    cell = row1.createCell(9);//J列
                    value = rs.getInt("value_9");
                    cell.setCellValue((Integer) value);
                    cell.setCellStyle(cstyle);
                    //当月請求額
                    cell = row1.createCell(10);//K列
                    value = rs.getInt("total_value");
                    cell.setCellValue((Integer) value);
                    cell.setCellStyle(cstyle);

                    /////////////////////////////////////////////////////////////
                    i現在行++;

                }

            } finally {
                ps.close();
            }

            //シート再計算
            sheet1.setForceFormulaRecalculation(true);
            sheet1.setSelected(true);

            try {
                //自身のtmpに保存
                out1 = new FileOutputStream(storeFol);
                wb1.write(out1);
                //スクリプトから印刷
                print_out = new File(storeFol);
                doPrintXlsx(print_out.getAbsolutePath(), printer[1], printer[1], 1, b直接印刷);

            } catch (IOException e) {
                System.out.println(e.toString());
            } finally {
                try {
                    out1.close();

                } catch (IOException e) {
                    System.out.println(e.toString());
                }
            }

        } catch (Exception ie1) {
            System.out.println(ie1.toString() + String.valueOf(cnt00));
        }

        return ret;
    }

    /*
   * term_date:所属月
   * bill_id:得意先ID
   * eda_no1:締め日
   * eda_no2:使用しない
   * print_no:使用しない
     */
    //請求明細表
    private Boolean printReport3(
             String file_path,
             //    String file_name, 
             String storeFol,
             Connection con,
             Date term_date,
             int bill_id,
             int eda_no1,
             int eda_no2,
             int print_no) {

        boolean ret = false;
        int cnt00 = 0;
        try {
            InputStream in1 = new FileInputStream(file_path);
            wb1 = WorkbookFactory.create(in1);
            initCom();
            Sheet sheet1 = wb1.getSheet("様式a");

            SimpleDateFormat sdf01 = new SimpleDateFormat("yyyy年MM月dd日");
            SimpleDateFormat sdf03 = new SimpleDateFormat("yyyy年MM月");
            SimpleDateFormat sdf04 = new SimpleDateFormat("MM月dd日");
            SimpleDateFormat sdf5 = new SimpleDateFormat("dd");

            FileOutputStream out1 = null;
            File print_out = null;

            Row row1;
            Cell cell;
            Object value = null;
            String[] printer = getPrinter();

            boolean b直接印刷 = false;

            String s = new StringBuilder()
                     //                .append("select * from f1_得意先請求明細表(?::date, ?::integer);" )
                     .append("select * from v1_d_bill_detail ")
                     .append("where term_month = ?::date ")
                     .append("and ((? > 0 and bill_id = ?) or (? = 0 and bill_id > 0)) ")
                     .append("order by bill_id, dis_office_id, term_date, car_no, weight_sno, goods_id ; ")
                     .toString();
            PreparedStatement ps = con.prepareStatement(s);
            try {
                ps.setObject(1, term_date);
                ps.setObject(2, bill_id);
                ps.setObject(3, bill_id);
                ps.setObject(4, bill_id);
                ResultSet rs = ps.executeQuery();

                int i現在行 = 4;

                //印刷日
                row1 = sheet1.getRow(1);//2行目
                cell = row1.getCell(10);//K列
                value = "作成日 : " + sdf01.format(DBManager.convert((Calendar.getInstance()).getTime()));
                cell.setCellValue((String) value);
                //対象月
                row1 = sheet1.getRow(0);//1行目
                cell = row1.getCell(0);//A列
                value = sdf03.format(term_date) + "請求分";
                cell.setCellValue((String) value);

                while (rs.next()) {

                    //ID
                    row1 = sheet1.createRow(i現在行);//5行目スタート
                    row1.setHeightInPoints(18);//行の高さを指定
                    cell = row1.createCell(0);//A列
                    value = rs.getInt("bill_id");
                    cell.setCellValue((Integer) value);
                    cell.setCellStyle(style);
                    //請求先名
                    cell = row1.createCell(1);//B列
                    value = rs.getString("m_bill_name");
                    cell.setCellValue((String) value);
                    cell.setCellStyle(style);
                    //締日
                    cell = row1.createCell(2);//C列
                    value = rs.getDate("bill_date");
                    cell.setCellValue(sdf5.format((Date) value));
                    cell.setCellStyle(style);
                    //請求日
                    cell = row1.createCell(3);//D列
                    value = rs.getDate("term_date");
                    cell.setCellValue(sdf04.format((Date) value));
                    cell.setCellStyle(tstyle);
                    //品名
                    cell = row1.createCell(4);//E列
                    value = rs.getString("bill_name");
                    cell.setCellValue((String) value);
                    cell.setCellStyle(style);
                    //発地
                    cell = row1.createCell(5);//F列
                    value = rs.getString("office_name2");
                    cell.setCellValue((String) value);
                    cell.setCellStyle(style);
                    //交付番号
                    cell = row1.createCell(6);//G列
                    value = rs.getString("manifesto_no");
                    cell.setCellValue((String) value);
                    cell.setCellStyle(style);

                    //数量
                    cell = row1.createCell(7);//H列
                    value = rs.getDouble("bill_value");
                    cell.setCellValue((Double) value);
                    cell.setCellStyle(rstyle3);
                    //単位
                    cell = row1.createCell(8);//I列
                    value = rs.getString("bill_unit");
                    cell.setCellValue((String) value);
                    cell.setCellStyle(style);

                    //単価
                    cell = row1.createCell(9);//J列
                    value = rs.getInt("unit_price");
                    cell.setCellValue((Integer) value);
                    cell.setCellStyle(rstyle3);

                    //請求金額
                    cell = row1.createCell(10);//J列
                    value = rs.getInt("bill_price");
                    cell.setCellValue((Integer) value);
                    cell.setCellStyle(rstyle3);//advances

                    //産廃税
                    cell = row1.createCell(11);//J列
                    value = rs.getInt("advances");
                    cell.setCellValue((Integer) value);
                    cell.setCellStyle(rstyle3);//

                    //備考
                    cell = row1.createCell(12);//G列
                    value = rs.getString("remark01");
                    cell.setCellValue((String) value);
                    cell.setCellStyle(style);

                    i現在行++;
                }

            } finally {
                ps.close();
            }

            //シート再計算
            sheet1.setForceFormulaRecalculation(true);
            sheet1.setSelected(true);

            try {
                //自身のtmpに保存
                out1 = new FileOutputStream(storeFol);
                wb1.write(out1);
                //スクリプトから印刷
                print_out = new File(storeFol);
                doPrintXlsx(print_out.getAbsolutePath(), printer[1], printer[1], 1, b直接印刷);

            } catch (IOException e) {
                System.out.println(e.toString());
            } finally {
                try {
                    out1.close();

                } catch (IOException e) {
                    System.out.println(e.toString());
                }
            }

        } catch (Exception ie1) {
            System.out.println(ie1.toString() + String.valueOf(cnt00));
        }

        return ret;
    }

    /*
   * term_date:所属月（管理台帳のスタート月）
   * bill_id:請求先ID
   * eda_no1:使用しない
   * eda_no2:使用しない
   * print_no:使用しない
     */
    //得意先管理台帳
    private Boolean printReport4(
             String file_path,
             //    String file_name, 
             String storeFol,
             Connection con,
             Date term_date,
             int bill_id,
             int eda_no1,
             int eda_no2,
             int print_no) {

        boolean ret = false;
        int cnt00 = 0;
        try {
            InputStream in1 = new FileInputStream(file_path);
            wb1 = WorkbookFactory.create(in1);
            initCom();
            Sheet sheet1 = wb1.getSheet("様式a");
            SimpleDateFormat sdf00 = new SimpleDateFormat("yyyy年MM月dd日～");
            SimpleDateFormat sdf01 = new SimpleDateFormat("yyyy年MM月dd日");
            SimpleDateFormat sdf04 = new SimpleDateFormat("MM月dd日");

            FileOutputStream out1 = null;
            File print_out = null;

            Row row1;
            Cell cell;
            Object value = null;
            String[] printer = getPrinter();

            boolean b直接印刷 = false;
            double iページ数 = 1;

            String s0 = new StringBuilder()
                     .append("select count(*)as cnt, ")
                     .append(" max(m_bill_id)as m_bill_id, max(m_bill_name)as m_bill_name ")
                     .append(" from v1_d_bill_daityo ")
                     .append("where date_trunc('month'::text, term_date)::date >= ? ")
                     .append("and m_bill_id = ? ;")
                     .toString();

            PreparedStatement ps0 = con.prepareStatement(s0);

            try {
                ps0.setObject(1, term_date);
                ps0.setObject(2, bill_id);
                ResultSet rs0 = ps0.executeQuery();

                if (rs0.next()) {
                    iページ数 = Math.ceil(rs0.getInt("cnt") / 50d);
                    //得意先コード
                    row1 = sheet1.getRow(2);//3行目
                    cell = row1.getCell(0);//A列
                    value = rs0.getInt("m_bill_id");
                    cell.setCellValue((Integer) value);
                    //得意先名
                    row1 = sheet1.getRow(2);//3行目
                    cell = row1.getCell(1);//B列
                    value = rs0.getString("m_bill_name");
                    cell.setCellValue((String) value);

                }

            } finally {
                ps0.close();
            }

            //印刷日
            row1 = sheet1.getRow(1);//2行目
            cell = row1.getCell(10);//K列
            value = "作成日 : " + sdf01.format(DBManager.convert((Calendar.getInstance()).getTime()));
            cell.setCellValue((String) value);
            //対象月
            row1 = sheet1.getRow(1);//2行目
            cell = row1.getCell(0);//A列
            value = sdf00.format(term_date);
            cell.setCellValue((String) value);

            for (int i = 0; i < iページ数; i++) {
                //シートの複製
                wb1.cloneSheet(0);
            }

            String s = new StringBuilder()
                     .append("select * from v1_d_bill_daityo ")
                     .append("where date_trunc('month'::text, term_date)::date >= ? ")
                     .append("and m_bill_id = ? ")
                     .append("order by term_date, row_no, i借方貸方; ")//該当日ごとの貸方借方区分ごとのSNOごと
                     .toString();
            PreparedStatement ps = con.prepareStatement(s);
            try {
                ps.setObject(1, term_date);
                ps.setObject(2, bill_id);
                ResultSet rs = ps.executeQuery();

                int i現在行 = 4;
                int i現在レコード = 0;
                int i前回差引残高 = 0;
                int v現在ページcnt = 0;
                int i借方小計 = 0;
                int i貸方小計 = 0;
                sheet1 = wb1.getSheetAt(1 + v現在ページcnt);
                //印刷範囲
                wb1.setPrintArea(1 + v現在ページcnt, "$A$1:$L$29");
                // 印刷用紙指定
                PrintSetup prints = sheet1.getPrintSetup();
                prints.setPaperSize(PrintSetup.A4_PAPERSIZE);
                prints.setLandscape(true);    //横向き

                while (rs.next()) {

                    if (v現在ページcnt > 0 && v現在ページcnt % 2 == 0 && i現在行 == 4) {
                        sheet1 = wb1.getSheetAt(1 + (v現在ページcnt % 2));
                    }
                    if (i現在レコード == 0 || rs.getInt("row_no") == 0 || rs.getInt("row_no") == 1000) {
                        i前回差引残高 = rs.getInt("i借方貸方");
                    } else {
                        i前回差引残高 = i前回差引残高 + rs.getInt("i借方貸方");
                    }
                    if (rs.getInt("row_no") == 10) {
                        i借方小計 = i借方小計 + rs.getInt("i借方");
                        i貸方小計 = i貸方小計 + rs.getInt("i貸方");
                    }

                    if (i現在行 < 29 && v現在ページcnt % 2 == 0) {
                        row1 = sheet1.getRow(i現在行);//5行目スタート
                        //所属日
                        cell = row1.getCell(0);//A列
                        value = rs.getDate("term_date");
                        cell.setCellValue(value != null ? sdf04.format((Date) value) : "");

                        //摘要
                        cell = row1.getCell(2);//C列
                        value = rs.getString("摘要");
                        cell.setCellValue((String) value);

                        //請求額（借方）
                        cell = row1.getCell(3);//D列
                        value = rs.getInt("row_no") == 0 ? 0 : rs.getInt("row_no") == 1000 ? i借方小計 : rs.getInt("i借方");
                        cell.setCellValue((Integer) value);

                        //入金額（貸方）
                        cell = row1.getCell(4);//E列
                        value = rs.getInt("row_no") == 0 ? 0 : rs.getInt("row_no") == 1000 ? i貸方小計 : rs.getInt("i貸方");
                        cell.setCellValue((Integer) value);

                        //差引残高
                        cell = row1.getCell(5);//F列
                        value = i前回差引残高;
                        cell.setCellValue((Integer) value);
                    } else if (v現在ページcnt % 2 == 1) {
                        row1 = sheet1.getRow(i現在行);//5行目スタート
                        //所属日
                        cell = row1.getCell(6);//G列
                        value = rs.getDate("term_date");
                        cell.setCellValue(value != null ? sdf04.format((Date) value) : "");
                        //摘要
                        cell = row1.getCell(8);//I列
                        value = rs.getString("摘要");
                        cell.setCellValue((String) value);
                        //請求額（借方）
                        cell = row1.getCell(9);//J列
                        value = rs.getInt("row_no") == 0 ? 0 : rs.getInt("row_no") == 1000 ? i借方小計 : rs.getInt("i借方");
                        cell.setCellValue((Integer) value);
                        //入金額（貸方）
                        cell = row1.getCell(10);//K列
                        value = rs.getInt("row_no") == 0 ? 0 : rs.getInt("row_no") == 1000 ? i貸方小計 : rs.getInt("i貸方");
                        cell.setCellValue((Integer) value);
                        //差引残高
                        cell = row1.getCell(11);//L列
                        value = i前回差引残高;
                        cell.setCellValue((Integer) value);
                    }

                    if ((rs.getInt("row_no") == 1000)) {
                        i現在行++;
                        i借方小計 = 0;
                        i貸方小計 = 0;
                    }
                    i現在行++;
                    i現在レコード++;

                    if (i現在行 >= 29) {
                        i現在行 = 4;
                        v現在ページcnt++;
                    }

                }

            } finally {
                ps.close();
            }

            //テンプレートシート削除
            wb1.removeSheetAt(0);

            //シート再計算
            sheet1.setForceFormulaRecalculation(true);
            sheet1.setSelected(true);

            try {
                //自身のtmpに保存
                out1 = new FileOutputStream(storeFol);
                wb1.write(out1);
                //スクリプトから印刷
                print_out = new File(storeFol);
                doPrintXlsx(print_out.getAbsolutePath(), printer[1], printer[1], 1, b直接印刷);

            } catch (IOException e) {
                System.out.println(e.toString());
            } finally {
                try {
                    out1.close();

                } catch (IOException e) {
                    System.out.println(e.toString());
                }
            }

        } catch (Exception ie1) {
            System.out.println(ie1.toString() + String.valueOf(cnt00));
        }

        return ret;
    }

    //入金明細表
    private Boolean printReport5(
             String file_path,
             //    String file_name, 
             String storeFol,
             Connection con,
             Date term_date,
             int bill_id,
             int eda_no1,
             int eda_no2,
             int print_no) {

        boolean ret = false;
        int cnt00 = 0;
        try {
            InputStream in1 = new FileInputStream(file_path);
            wb1 = WorkbookFactory.create(in1);
            initCom();
            Sheet sheet1 = wb1.getSheet("様式a");
            SimpleDateFormat sdf01 = new SimpleDateFormat("yyyy年MM月dd日");
            SimpleDateFormat sdf03 = new SimpleDateFormat("yyyy年MM月");
            SimpleDateFormat sdf04 = new SimpleDateFormat("MM月dd日");

            FileOutputStream out1 = null;
            File print_out = null;

            Row row1;
            Cell cell;
            Object value = null;
            String[] printer = getPrinter();

            boolean b直接印刷 = false;

            String s = new StringBuilder()
                     .append("select * from v1_d_bill_payment_detail ")
                     .append("where date_trunc('month'::text, payment_date)::date  = ? ")
                     .append("and ((bill_id = ? and ? <> 0) or (bill_id > 0 and ? = 0)) ")
                     .append("order by bill_id, payment_date, p_class_id, payment_price; ")
                     .toString();
            PreparedStatement ps = con.prepareStatement(s);
            try {
                ps.setObject(1, term_date);
                ps.setObject(2, bill_id);
                ps.setObject(3, bill_id);
                ps.setObject(4, bill_id);
                ResultSet rs = ps.executeQuery();

                int i現在行 = 4;

                //印刷日
                row1 = sheet1.getRow(1);//2行目
                cell = row1.getCell(7);//H列
                value = "作成日 : " + sdf01.format(DBManager.convert((Calendar.getInstance()).getTime()));
                cell.setCellValue((String) value);
                //対象月
                row1 = sheet1.getRow(0);//1行目
                cell = row1.getCell(0);//A列
                value = sdf03.format(term_date) + "入金分";
                cell.setCellValue((String) value);

                while (rs.next()) {

                    //ID
                    row1 = sheet1.createRow(i現在行);//5行目スタート
                    row1.setHeightInPoints(18);//行の高さを指定
                    cell = row1.createCell(0);//A列
                    value = rs.getInt("bill_id");
                    cell.setCellValue((Integer) value);
                    cell.setCellStyle(style);
                    //請求先
                    cell = row1.createCell(1);//B列
                    value = rs.getString("m_bill_name");
                    cell.setCellValue((String) value);
                    cell.setCellStyle(style);
                    //入金日
                    cell = row1.createCell(2);//C列
                    value = rs.getDate("payment_date");
                    cell.setCellValue(sdf04.format((Date) value));
                    cell.setCellStyle(tstyle);
                    //取引区分ID
                    cell = row1.createCell(3);//D列
                    value = rs.getInt("p_class_id");
                    cell.setCellValue((Integer) value);
                    cell.setCellStyle(style);
                    //入金区分
                    cell = row1.createCell(4);//E列
                    value = rs.getString("p_class_name");
                    cell.setCellValue((String) value);
                    cell.setCellStyle(style);
                    //入金額
                    cell = row1.createCell(5);//F列
                    value = rs.getInt("payment_price");
                    cell.setCellValue((Integer) value);
                    cell.setCellStyle(cstyle);
                    //調整額
                    cell = row1.createCell(6);//G列
                    value = rs.getInt("commission");
                    cell.setCellValue((Integer) value);
                    cell.setCellStyle(cstyle);
                    //適要
                    cell = row1.createCell(7);//H列
                    value = rs.getString("remark01");
                    cell.setCellValue((String) value);
                    cell.setCellStyle(style);

                    i現在行++;

                }

            } finally {
                ps.close();
            }

            //シート再計算
            sheet1.setForceFormulaRecalculation(true);
            sheet1.setSelected(true);

            try {
                //自身のtmpに保存
                out1 = new FileOutputStream(storeFol);
                wb1.write(out1);
                //スクリプトから印刷
                print_out = new File(storeFol);
                doPrintXlsx(print_out.getAbsolutePath(), printer[1], printer[1], 1, b直接印刷);

            } catch (IOException e) {
                System.out.println(e.toString());
            } finally {
                try {
                    out1.close();

                } catch (IOException e) {
                    System.out.println(e.toString());
                }
            }

        } catch (Exception ie1) {
            System.out.println(ie1.toString() + String.valueOf(cnt00));
        }

        return ret;
    }

    /* 
   * term_date:所属月
   * bill_id:使用しない
   * eda_no1:使用しない
   * eda_no2:使用しない
   * print_no:使用しない
     */
    //売掛残高一覧表
    private Boolean printReport6(
             String file_path,
             String storeFol,
             Connection con,
             Date term_date,
             int bill_id,
             int eda_no1,
             int eda_no2,
             int print_no) {

        boolean ret = false;
        int cnt00 = 0;
        try {
            InputStream in1 = new FileInputStream(file_path);
            wb1 = WorkbookFactory.create(in1);
            initCom();
            Sheet sheet1 = wb1.getSheet("様式a");

            SimpleDateFormat sdf01 = new SimpleDateFormat("yyyy年MM月dd日");
            SimpleDateFormat sdf03 = new SimpleDateFormat("yyyy年MM月");

            FileOutputStream out1 = null;
            File print_out = null;

            Row row1;
            Cell cell;
            Object value = null;

            String[] printer = getPrinter();

            boolean b直接印刷 = false;

            try {
                Statement st0 = con.createStatement();
                st0.executeUpdate("begin;");
                st0.executeUpdate("commit;");

            } catch (SQLException e0) {
                System.out.print(e0.toString());
            }

            String s = new StringBuilder()
                     .append("select * from v1_d_bill_urikake ")
                     .append("where term_month = ? ")
                     .append("order by m_bill_id; ")
                     .toString();
            PreparedStatement ps = con.prepareStatement(s);
            try {
                ps.setObject(1, term_date);
                ResultSet rs = ps.executeQuery();

                int i現在行 = 4;

                //印刷日
                row1 = sheet1.getRow(1);//2行目
                cell = row1.getCell(7);//H列
                value = "作成日 : " + sdf01.format(DBManager.convert((Calendar.getInstance()).getTime()));
                cell.setCellValue((String) value);
                //対象月
                row1 = sheet1.getRow(0);//1行目
                cell = row1.getCell(0);//A列
                value = sdf03.format(term_date) + " 分";
                cell.setCellValue((String) value);

                while (rs.next()) {
                    //ID
                    row1 = sheet1.createRow(i現在行);//5行目スタート
                    row1.setHeightInPoints(18);//行の高さを指定
                    cell = row1.createCell(0);//A列
                    value = rs.getInt("m_bill_id");
                    cell.setCellValue((Integer) value);
                    cell.setCellStyle(style);
                    //請求先名
                    cell = row1.createCell(1);//B列
                    value = rs.getString("m_bill_name");
                    cell.setCellValue((String) value);
                    cell.setCellStyle(style);
                    //i前月繰越額
                    cell = row1.createCell(2);//C列
                    value = rs.getInt("i前月繰越額");
                    cell.setCellValue((Integer) value);
                    cell.setCellStyle(cstyle);
                    //i当月入金額
                    cell = row1.createCell(3);//D列
                    value = rs.getInt("i当月入金額");
                    cell.setCellValue((Integer) value);
                    cell.setCellStyle(cstyle);
                    //i前月残額
                    cell = row1.createCell(4);//E列
                    value = rs.getInt("i前月残額");
                    cell.setCellValue((Integer) value);
                    cell.setCellStyle(cstyle);
                    //i当月売上金額
                    cell = row1.createCell(5);//F列
                    value = rs.getInt("i当月売上金額");
                    cell.setCellValue((Integer) value);
                    cell.setCellStyle(cstyle);
                    //i当月繰越額
                    cell = row1.createCell(6);//G列
                    value = rs.getInt("i当月繰越額");
                    cell.setCellValue((Integer) value);
                    cell.setCellStyle(cstyle);
                    //摘要欄
                    cell = row1.createCell(7);//H列
                    value = "";
                    cell.setCellValue((String) value);
                    cell.setCellStyle(style);
                    /////////////////////////////////////////////////////////////

                    i現在行++;

                }

            } finally {
                ps.close();
            }

            //シート再計算
            sheet1.setForceFormulaRecalculation(true);
            sheet1.setSelected(true);

            try {
                //自身のtmpに保存
                out1 = new FileOutputStream(storeFol);
                wb1.write(out1);
                //スクリプトから印刷
                print_out = new File(storeFol);
                doPrintXlsx(print_out.getAbsolutePath(), printer[1], printer[1], 1, b直接印刷);

            } catch (IOException e) {
                System.out.println(e.toString());
            } finally {
                try {
                    out1.close();

                } catch (IOException e) {
                    System.out.println(e.toString());
                }
            }

        } catch (Exception ie1) {
            System.out.println(ie1.toString() + String.valueOf(cnt00));
        }

        return ret;
    }

    //領収書
    private Boolean print領収書(
             String file_path,
             String storeFol,
             Connection con,
             int sno
    ) {

        boolean ret = false;
        int cnt00 = 0;
        try {
            InputStream in1 = new FileInputStream(file_path);
            wb1 = WorkbookFactory.create(in1);
            initCom();
            Sheet sheet1 = wb1.getSheet("Sheet1");

            FileOutputStream out1 = null;
            File print_out = null;

            Row row1;
            Cell cell;
            Object value = null;

            String[] printer = getPrinter();

            boolean b直接印刷 = true;

            String s = new StringBuilder()
                     .append("select * from v1_weigh_data ")
                     .append("where sno = ?; ")
                     .toString();
            PreparedStatement ps = con.prepareStatement(s);
            try {
                ps.setObject(1, sno);
                ResultSet rs = ps.executeQuery();

                if (rs.next()) {

                    int 税抜き金額 = rs.getInt("charge_value");
                    int 消費税額 = 税抜き金額 * 税率 / 100;
                    int 税込み金額 = 税抜き金額 + 消費税額;
                    //日付
                    row1 = sheet1.getRow(7);//8行目
                    cell = row1.getCell(2);//C列
                    LocalDateTime nowDate = LocalDateTime.now();
                    DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy年MM月dd日");
                    cell.setCellValue(dtf.format(nowDate));

                    row1 = sheet1.getRow(0);
                    cell = row1.getCell(5);//F列
                    String 通し番号 = "No. ";
                    value = rs.getString("sno");
                    通し番号 += value;
                    cell.setCellValue((String) 通し番号);

                    //得意先名
                    row1 = sheet1.getRow(4);//5行目
                    cell = row1.getCell(1);//B列
                    value = rs.getString("entrepreneur_name");
                    cell.setCellValue((String) value);

                    //金額
                    row1 = sheet1.getRow(5);//6行目
                    cell = row1.getCell(2);//C列
                    String formatted金額 = String.format("%,d", 税込み金額);
                    String s税込み金額 = "￥" + formatted金額 + "-";
                    cell.setCellValue((String) s税込み金額);

                    //品名・但し書き
                    row1 = sheet1.getRow(6);//7行目
                    cell = row1.getCell(2);//C列
                    value = rs.getString("goods_name");
                    if (((String) value).length() >= 9) {
                        cell.setCellStyle(游ゴシックstyle);
                    }
                    cell.setCellValue((String) value);
                    cell = row1.getCell(5);//F列

                    if (領収書用途 == 0) {
                        cell.setCellValue((String) "処分代として");
                    } else if (領収書用途 == 1) {
                        cell.setCellValue((String) "販売代として");
                    }

                    //内訳
                    row1 = sheet1.getRow(11);//12行目
                    cell = row1.getCell(2);//C列
                    String formatted税抜き金額 = String.format("%,d", 税抜き金額);
                    String s税抜き金額 = "￥" + formatted税抜き金額 + "-";
                    cell.setCellValue((String) s税抜き金額);

                    //消費税額
                    row1 = sheet1.getRow(12);//13行目
                    cell = row1.getCell(2);//C列
                    String formatted消費税額 = String.format("%,d", 消費税額);
                    String s消費税額 = "￥" + formatted消費税額 + "-";
                    cell.setCellValue((String) s消費税額);
                }

            } finally {
                ps.close();
            }

            try {
                //自身のtmpに保存
                out1 = new FileOutputStream(storeFol);
                wb1.write(out1);
                //スクリプトから印刷
                print_out = new File(storeFol);
                doPrintXlsx(print_out.getAbsolutePath(), printer[3], printer[3], 1, b直接印刷);

            } catch (IOException e) {
                System.out.println(e.toString());
            } finally {
                try {
                    out1.close();

                } catch (IOException e) {
                    System.out.println(e.toString());
                }
            }
        } catch (Exception ie1) {
            System.out.println(ie1.toString() + String.valueOf(cnt00));
        }
        return ret;
    }

    //領収書
    private Boolean print領収書控え(
             String file_path,
             String storeFol,
             Connection con,
             int sno
    ) {

        boolean ret = false;
        int cnt00 = 0;
        try {
            InputStream in1 = new FileInputStream(file_path);
            wb1 = WorkbookFactory.create(in1);
            initCom();
            Sheet sheet1 = wb1.getSheet("Sheet1");

            FileOutputStream out1 = null;
            File print_out = null;

            Row row1;
            Cell cell;
            Object value = null;

            String[] printer = getPrinter();

            boolean b直接印刷 = true;

            String s = new StringBuilder()
                     .append("select * from v1_weigh_data ")
                     .append("where sno = ?; ")
                     .toString();
            PreparedStatement ps = con.prepareStatement(s);
            try {
                ps.setObject(1, sno);
                ResultSet rs = ps.executeQuery();

                if (rs.next()) {

                    int 税抜き金額 = rs.getInt("charge_value");
                    int 消費税額 = 税抜き金額 * 税率 / 100;
                    int 税込み金額 = 税抜き金額 + 消費税額;
                    //日付
                    row1 = sheet1.getRow(7);//8行目
                    cell = row1.getCell(2);//C列
                    LocalDateTime nowDate = LocalDateTime.now();
                    DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy年MM月dd日");
                    cell.setCellValue(dtf.format(nowDate));

                    row1 = sheet1.getRow(0);
                    cell = row1.getCell(5);//F列
                    String 通し番号 = "No. ";
                    value = rs.getString("sno");
                    通し番号 += value;
                    cell.setCellValue((String) 通し番号);

                    //得意先名
                    row1 = sheet1.getRow(4);//5行目
                    cell = row1.getCell(2);//C列
                    value = rs.getString("entrepreneur_name");
                    cell.setCellValue((String) value);

                    //金額
                    row1 = sheet1.getRow(5);//6行目
                    cell = row1.getCell(2);//C列
                    String formatted金額 = String.format("%,d", 税込み金額);
                    String s税込み金額 = "￥" + formatted金額 + "-";
                    cell.setCellValue((String) s税込み金額);

                    //品名・但し書き
                    row1 = sheet1.getRow(6);//7行目
                    cell = row1.getCell(2);//B列
                    value = rs.getString("goods_name");
                    if (((String) value).length() >= 9) {
                        cell.setCellStyle(游ゴシックstyle);
                    }
                    cell.setCellValue((String) value);
                    cell = row1.getCell(5);//F列

                    if (領収書用途 == 0) {
                        cell.setCellValue((String) "処分代として");
                    } else if (領収書用途 == 1) {
                        cell.setCellValue((String) "販売代として");
                    }

                    //内訳
                    row1 = sheet1.getRow(11);//12行目
                    cell = row1.getCell(2);//C列
                    cell.setCellValue((Integer) 税抜き金額);

                    //消費税額
                    row1 = sheet1.getRow(12);//13行目
                    cell = row1.getCell(2);//C列
                    cell.setCellValue((Integer) 消費税額);
                }

            } finally {
                ps.close();
            }

            try {
                //自身のtmpに保存
                out1 = new FileOutputStream(storeFol);
                wb1.write(out1);
                //スクリプトから印刷
                print_out = new File(storeFol);
                doPrintXlsx(print_out.getAbsolutePath(), printer[3], printer[3], 1, b直接印刷);

            } catch (IOException e) {
                System.out.println(e.toString());
            } finally {
                try {
                    out1.close();

                } catch (IOException e) {
                    System.out.println(e.toString());
                }
            }
        } catch (Exception ie1) {
            System.out.println(ie1.toString() + String.valueOf(cnt00));
        }
        return ret;
    }

    //領収書
    private Boolean print領収書(
             String file_path,
             String storeFol,
             Connection con
    ) {

        boolean ret = false;
        int cnt00 = 0;
        try {
            InputStream in1 = new FileInputStream(file_path);
            wb1 = WorkbookFactory.create(in1);
            initCom();
            Sheet sheet1 = wb1.getSheet("Sheet1");

            FileOutputStream out1 = null;
            File print_out = null;

            Row row1;
            Cell cell;
            Object value = null;

            String[] printer = getPrinter();

            boolean b直接印刷 = true;

            String s = new StringBuilder()
                     .append("select * from v1_weigh_data ")
                     .append("order by gate_out_date desc ")
                     .append("limit 1;")
                     .toString();
            PreparedStatement ps = con.prepareStatement(s);
            try {
                ResultSet rs = ps.executeQuery();

                if (rs.next()) {

                    int 税抜き金額 = rs.getInt("charge_value");
                    int 消費税額 = 税抜き金額 * 税率 / 100;
                    int 税込み金額 = 税抜き金額 + 消費税額;
                    //日付
                    row1 = sheet1.getRow(7);//8行目
                    cell = row1.getCell(2);//C列
                    LocalDateTime nowDate = LocalDateTime.now();
                    DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy年MM月dd日");
                    cell.setCellValue(dtf.format(nowDate));

                    row1 = sheet1.getRow(0);
                    cell = row1.getCell(5);//F列
                    String 通し番号 = "No. ";
                    value = rs.getString("sno");
                    通し番号 += value;
                    cell.setCellValue((String) 通し番号);

                    //得意先名
                    row1 = sheet1.getRow(4);//5行目
                    cell = row1.getCell(1);//B列
                    value = rs.getString("entrepreneur_name");
                    cell.setCellValue((String) value);

                    //金額
                    row1 = sheet1.getRow(5);//6行目
                    cell = row1.getCell(2);//C列
                    String formatted金額 = String.format("%,d", 税込み金額);
                    String s税込み金額 = "￥" + formatted金額 + "-";
                    cell.setCellValue((String) s税込み金額);

                    //品名・但し書き
                    row1 = sheet1.getRow(6);//7行目
                    cell = row1.getCell(2);//C列
                    value = rs.getString("goods_name");
                    if (((String) value).length() >= 9) {
                        cell.setCellStyle(游ゴシックstyle);
                    }
                    cell.setCellValue((String) value);
                    cell = row1.getCell(5);//F列

                    if (領収書用途 == 0) {
                        cell.setCellValue((String) "処分代として");
                    } else if (領収書用途 == 1) {
                        cell.setCellValue((String) "販売代として");
                    }

                    //内訳
                    row1 = sheet1.getRow(11);//12行目
                    cell = row1.getCell(2);//C列
                    String formatted税抜き金額 = String.format("%,d", 税抜き金額);
                    String s税抜き金額 = "￥" + formatted税抜き金額 + "-";
                    cell.setCellValue((String) s税抜き金額);

                    //消費税額
                    row1 = sheet1.getRow(12);//13行目
                    cell = row1.getCell(2);//C列
                    String formatted消費税額 = String.format("%,d", 消費税額);
                    String s消費税額 = "￥" + formatted消費税額 + "-";
                    cell.setCellValue((String) s消費税額);
                }

            } finally {
                ps.close();
            }

            try {
                //自身のtmpに保存
                out1 = new FileOutputStream(storeFol);
                wb1.write(out1);
                //スクリプトから印刷
                print_out = new File(storeFol);
                doPrintXlsx(print_out.getAbsolutePath(), printer[3], printer[3], 1, b直接印刷);

            } catch (IOException e) {
                System.out.println(e.toString());
            } finally {
                try {
                    out1.close();

                } catch (IOException e) {
                    System.out.println(e.toString());
                }
            }
        } catch (Exception ie1) {
            System.out.println(ie1.toString() + String.valueOf(cnt00));
        }
        return ret;
    }

    //領収書
    private Boolean print領収書控え(
             String file_path,
             String storeFol,
             Connection con
    ) {

        boolean ret = false;
        int cnt00 = 0;
        try {
            InputStream in1 = new FileInputStream(file_path);
            wb1 = WorkbookFactory.create(in1);
            initCom();
            Sheet sheet1 = wb1.getSheet("Sheet1");

            FileOutputStream out1 = null;
            File print_out = null;

            Row row1;
            Cell cell;
            Object value = null;

            String[] printer = getPrinter();

            boolean b直接印刷 = true;

            String s = new StringBuilder()
                     .append("select * from v1_weigh_data ")
                     .append("order by gate_out_date desc ")
                     .append("limit 1;")
                     .toString();
            PreparedStatement ps = con.prepareStatement(s);
            try {
                ResultSet rs = ps.executeQuery();

                if (rs.next()) {

                    int 税抜き金額 = rs.getInt("charge_value");
                    int 消費税額 = 税抜き金額 * 税率 / 100;
                    int 税込み金額 = 税抜き金額 + 消費税額;
                    //日付
                    row1 = sheet1.getRow(7);//8行目
                    cell = row1.getCell(2);//C列
                    LocalDateTime nowDate = LocalDateTime.now();
                    DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy年MM月dd日");
                    cell.setCellValue(dtf.format(nowDate));

                    row1 = sheet1.getRow(0);
                    cell = row1.getCell(5);//F列
                    String 通し番号 = "No. ";
                    value = rs.getString("sno");
                    通し番号 += value;
                    cell.setCellValue((String) 通し番号);

                    //得意先名
                    row1 = sheet1.getRow(4);//5行目
                    cell = row1.getCell(2);//C列
                    value = rs.getString("entrepreneur_name");
                    cell.setCellValue((String) value);

                    //金額
                    row1 = sheet1.getRow(5);//6行目
                    cell = row1.getCell(2);//C列
                    String formatted金額 = String.format("%,d", 税込み金額);
                    String s税込み金額 = "￥" + formatted金額 + "-";
                    cell.setCellValue((String) s税込み金額);

                    //品名・但し書き
                    row1 = sheet1.getRow(6);//7行目
                    cell = row1.getCell(2);//B列
                    value = rs.getString("goods_name");
                    if (((String) value).length() >= 9) {
                        cell.setCellStyle(游ゴシックstyle);
                    }
                    cell.setCellValue((String) value);
                    cell = row1.getCell(5);//F列

                    if (領収書用途 == 0) {
                        cell.setCellValue((String) "処分代として");
                    } else if (領収書用途 == 1) {
                        cell.setCellValue((String) "販売代として");
                    }

                    //内訳
                    row1 = sheet1.getRow(11);//12行目
                    cell = row1.getCell(2);//C列
                    cell.setCellValue((Integer) 税抜き金額);

                    //消費税額
                    row1 = sheet1.getRow(12);//13行目
                    cell = row1.getCell(2);//C列
                    cell.setCellValue((Integer) 消費税額);
                }

            } finally {
                ps.close();
            }

            try {
                //自身のtmpに保存
                out1 = new FileOutputStream(storeFol);
                wb1.write(out1);
                //スクリプトから印刷
                print_out = new File(storeFol);
                doPrintXlsx(print_out.getAbsolutePath(), printer[3], printer[3], 1, b直接印刷);

            } catch (IOException e) {
                System.out.println(e.toString());
            } finally {
                try {
                    out1.close();

                } catch (IOException e) {
                    System.out.println(e.toString());
                }
            }
        } catch (Exception ie1) {
            System.out.println(ie1.toString() + String.valueOf(cnt00));
        }
        return ret;
    }
}
