package aks.industrialwaste.recycle.report;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.Date;
import aks.industrialwaste.database.DBManager;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.channels.FileChannel;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.PrintSetup;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

public class XlsxYearlyReportMiyagi extends OSCReport {

    //宮城県産業廃棄物処分実績報告書（中間）
    public static final int YEARLY_REPORT1 = 1;
    //宮城県産業廃棄物処分実績報告書（最終）
    public static final int YEARLY_REPORT2 = 2;
    //宮城県産業廃棄物 運搬実績報告書
    public static final int YEARLY_REPORT3 = 3;
    //宮城県産業廃棄物管理表交付状況報告書
    public static final int YEARLY_REPORT4 = 4;
    //宮城県産業廃棄物処理実績報告書
    public static final int YEARLY_REPORT5 = 5;

    private Connection con_;
    private CellStyle style3, style4;
    private Workbook wb1;
    private Font font;
    private String m市区町村 = "";
    private String m自社住所 = "";
    private String m自社名称 = "";
    private String m自社代表者名 = "";
    private String m自社電話 = "";
    private String m許可番号1処分 = "";
    private String m許可番号1運搬 = "";
    private String 許可年月日1処分 = "";
    private String 許可年月日1運搬 = "";

    private static final DateTimeFormatter DATE_FORMATTER
             = DateTimeFormatter.ofPattern("yyyy年M月d日");

    private static String 現在年月日() {
        return LocalDate.now().format(DATE_FORMATTER);
    }

    private String 処分場住所(Connection con) {
        String sql = new StringBuilder()
                 .append("select address1 ")
                 .append("from m_manifesto_3 ")
                 .append("where cont_id = ? ")
                 .append("and id = ? ")
                 .append("and delete_flag = false ")
                 .toString();

        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, 1); // cont_id
            ps.setInt(2, 1); // id

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    String address1 = rs.getString("address1");
                    return address1 == null ? "" : address1;
                }
            }

        } catch (SQLException e) {
            logger.log(Level.SEVERE, "getManifesto3Address1 failed", e);
        }

        return "";
    }

    private static final Logger logger
             = Logger.getLogger(XlsxYearlyReportFukushima.class.getName());

    public XlsxYearlyReportMiyagi() {

        this(YEARLY_REPORT1);
    }

    /**
     * 帳票を印刷するMonthlyReportのインスタンスを生成する.
     *
     * @param reportID 印刷する帳票のID
     */
    public XlsxYearlyReportMiyagi(int reportID) {
        if ((YEARLY_REPORT1 != reportID)
                 && (YEARLY_REPORT2 != reportID)
                 && (YEARLY_REPORT3 != reportID)
                 && (YEARLY_REPORT4 != reportID)
                 && (YEARLY_REPORT5 != reportID)) {
            throw new IllegalArgumentException("'reportID' is not match.");
        }
    }

    private void initCom() {

        font = wb1.createFont();
        font.setFontName("ＭＳ 明朝");
        font.setFontHeightInPoints((short) 9);

        style3 = wb1.createCellStyle();
        style3.setBorderLeft(CellStyle.BORDER_NONE);
        style3.setBorderRight(CellStyle.BORDER_NONE);
        style3.setBorderTop(CellStyle.BORDER_THIN);
        style3.setBorderBottom(CellStyle.BORDER_THIN);
        style3.setAlignment(CellStyle.ALIGN_LEFT);
        style3.setFont(font);

        style4 = wb1.createCellStyle();
        style4.setBorderLeft(CellStyle.BORDER_NONE);
        style4.setBorderRight(CellStyle.BORDER_NONE);
        style4.setBorderTop(CellStyle.BORDER_THIN);
        style4.setBorderBottom(CellStyle.BORDER_THIN);
        style4.setAlignment(CellStyle.ALIGN_RIGHT);
        style4.setFont(font);
    }

    /**
     * 指定条件で帳票を印刷する.
     *
     * @param con データベースとの接続
     * @param path 保存先ファイルのフルパス
     * @param report_sno 対象の報告
     * @param term_date 対象年
     */
    public int print(Connection con, String path, int report_sno, Integer term_date, int ent_id) {
        con_ = con;
        String template = "";

        try {
            switch (report_sno) {
                case YEARLY_REPORT1:
                    //宮城県産業廃棄物処分実績報告書（中間）
                    template = "template\\行政報告書\\宮城県_処分実績報告書.xlsx";
                    break;
                case YEARLY_REPORT2:
                    //宮城県産業廃棄物処分実績報告書（最終）
                    template = "template\\行政報告書\\仙台市_行政報告書最終処分.xlsx";
                    break;
                case YEARLY_REPORT3:
                    //宮城県産業廃棄物　運搬実績報告書
                    template = "template\\行政報告書\\宮城県_運搬実績報告書.xlsx";
                    break;
                case YEARLY_REPORT4:
                    //管理表交付状況報告書
                    template = "template\\行政報告書\\宮城県_交付等状況報告書.xlsx";
                    break;
                case YEARLY_REPORT5:
                    //宮城県産業廃棄物処理実績報告書
                    template = "template\\行政報告書\\宮城県_処理実績報告書.xlsx";
                    break;
                default:
                    break;
            }

            // テンプレートファイルをローカルにコピー
            File templateFile = new File(template);
            File tmpFile = new File("tmp", templateFile.getName());

            // テンプレートファイルをローカルにコピー
            copyTransfer(templateFile.getPath(), tmpFile.getPath());

            //報告者情報取得////////////////////////////////
            m自社住所 = "";
            m自社名称 = "";
            m自社代表者名 = "";
            m自社電話 = "";
            m許可番号1処分 = "";
            m許可番号1運搬 = "";
            許可年月日1処分 = "";
            許可年月日1運搬 = "";
            String s0 = new StringBuilder()
                     .append("select * ")
                     .append("from m_manifesto where sno = 1; ")
                     .toString();

            PreparedStatement ps0 = con.prepareStatement(s0);

            try {
                ResultSet rs0 = ps0.executeQuery();

                if (rs0.next()) {
                    m市区町村 = "仙台市";
                    m自社住所 = rs0.getString("trust_name1");
                    m自社名称 = rs0.getString("company_name");
                    m自社代表者名 = "    代表取締役　" + rs0.getString("trust_name2");
                    m自社電話 = "  " + rs0.getString("trust_name3");
                    m許可番号1処分 = "  " + rs0.getString("permission_no");
                    m許可番号1運搬 = "  " + rs0.getString("permission_no_trans");

                    Date permissionDate = rs0.getDate("permission_date");
                    Date permissionDate2 = rs0.getDate("permission_date2");

                    許可年月日1処分 = "  " + formatDateJp(permissionDate);
                    許可年月日1運搬 = "  " + formatDateJp(permissionDate2);
                }

            } finally {
                ps0.close();
            }
            /////////////////////////////////////////////////////////////////

            try {
                switch (report_sno) {
                    case YEARLY_REPORT1:
                        //産業廃棄物処分実績報告書（中間）
                        printYearlyReport1(tmpFile.getPath(), path, con_, term_date);
                        break;
                    case YEARLY_REPORT2:
                        //産業廃棄物処分実績報告書（最終）
                        printYearlyReport2(tmpFile.getPath(), path, con_, term_date);
                        break;
                    case YEARLY_REPORT3:
                        //産業廃棄物 運搬実績報告書
                        printYearlyReport3(tmpFile.getPath(), path, con_, term_date);
                        break;
                    case YEARLY_REPORT4:
                        //産業廃棄物管理表交付状況報告書
                        printYearlyReport4(tmpFile.getPath(), path, con_, term_date, ent_id);
                        break;
                    case YEARLY_REPORT5:
                        //産業廃棄物処理実績報告書
                        printYearlyReport5(tmpFile.getPath(), path, con_, term_date);
                        break;
                    default:
                        break;
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

        String[] printer = new String[3];
        printer[0] = "";
        printer[1] = "";
        printer[2] = "";

        try {
            Statement st = con_.createStatement();
            try {
                String s = new StringBuilder()
                         .append("select weigh_printer, default_printer, manifesto_printer ")
                         .append("from m_system ")
                         .append("where sno = 1;")
                         .toString();
                ResultSet rs = st.executeQuery(s);
                if (rs.next()) {
                    printer[0] = rs.getString("weigh_printer");
                    printer[1] = rs.getString("default_printer");
                    printer[2] = rs.getString("manifesto_printer");
                }
            } finally {
                st.close();
            }

        } catch (SQLException e) {
        }
        return printer;
    }

// 印刷処理
    public static void doPrintXlsx(String file_path, String printer1, String printer2, int papers, boolean b直接印刷) {
        try {
            System.out.println("ps1 exists=" + new java.io.File("tmp\\XlsPrint.ps1").exists());
            System.out.println("file exists=" + new java.io.File(file_path).exists());

            if (b直接印刷) {
                System.out.println("XlsPrint 開始");

                runCmd(
                         "powershell.exe",
                         "-NoProfile",
                         "-ExecutionPolicy", "Bypass",
                         "-File", "tmp\\XlsPrint.ps1",
                         "-FilePath", file_path,
                         "-PrinterName", printer1,
                         "-PrintCount", String.valueOf(papers)
                );

                System.out.println("XlsPrint 終了");
            } else {
                System.out.println("Excel表示 開始");
                runCmd("cmd", "/c", "start", "", file_path);
                System.out.println("Excel表示 終了");
            }

        } catch (IOException | InterruptedException e) {
            System.out.println("失敗: " + e);
        }
    }

    private static void runCmd(String... args) throws IOException, InterruptedException {
        ProcessBuilder pb = new ProcessBuilder(args);
        pb.redirectErrorStream(true);
        Process p = pb.start();
        int code = p.waitFor();
        if (code != 0) {
            throw new IOException("Command failed. exitCode=" + code);
        }
    }

    //産業廃棄物処分実績報告書（中間）テンプレート貼り付けVer.
    private void printYearlyReport1(
             String file_path,
             String storeFol,
             Connection con,
             Integer term_date) {

        try {
            InputStream in1 = new FileInputStream(file_path);
            wb1 = WorkbookFactory.create(in1);
            initCom();
            Sheet sheet1 = wb1.getSheet("27sys");

            FileOutputStream out1 = null;
            File print_out = null;

            Row row1;
            Cell cell;
            Object value = null;
            String[] printer = getPrinter();

            int v現在明細行 = 0;

            //自社処理分のみ品目・排出業者・排出現場・運搬業者・処分方法ごとに集計//////////
            String s = new StringBuilder()
                     .append("select *, ")
                     .append("public.\"宮城_現場区域判定関数\"(office_address1) as office_area_name ")
                     .append("from v1_yearly_data_00_sendai where term_year = ? ")
                     .append("and cont_id in (1) ")
                     .append("and final_goods_kind in (1) ")
                     .append("and goods_option = 0 ")
                     .append("and base_goods_id <> 0 ")
                     .append("order by base_goods_id, ent_id, method_name, last_office_name ; ")
                     .toString();
            PreparedStatement ps = con.prepareStatement(s);
            try {

                String 選択年度 = String.valueOf(term_date) + "年度";

                ps.setObject(1, term_date);
                ResultSet rs = ps.executeQuery();

                row1 = sheet1.getRow(3);//4行目
                cell = row1.getCell(0);//A列
                value = "産業廃棄物・特別管理産業廃棄物の処分実績報告書 (" + 選択年度 + ")";
                cell.setCellValue((String) value);

                row1 = sheet1.getRow(6);//7行目
                cell = row1.getCell(20);//P列
                value = 現在年月日();
                cell.setCellValue((String) value);

                row1 = sheet1.getRow(11);//12行目
                cell = row1.getCell(15);//P列
                value = m自社住所;
                cell.setCellValue((String) value);

                row1 = sheet1.getRow(13);//14行目
                cell = row1.getCell(15);//P列
                value = m自社名称 + "　" + m自社代表者名;
                cell.setCellValue((String) value);

                row1 = sheet1.getRow(16);//17行目
                cell = row1.getCell(15);//P列
                value = m自社電話;
                cell.setCellValue((String) value);

                row1 = sheet1.getRow(19);//20行目
                cell = row1.getCell(0);//A列
                value = 選択年度 + "の産業廃棄物の処理の実績について、廃棄物の処理委及び清掃に関する施行細則"
                         + "第6条の２第３項の規定により、次の通り報告します。";
                cell.setCellValue((String) value);

                row1 = sheet1.getRow(22);//23行目
                cell = row1.getCell(9);//J列
                value = m許可番号1処分;//m許可番号1
                cell.setCellValue((String) value);

                row1 = sheet1.getRow(22);//23行目
                cell = row1.getCell(16);//Q列
                value = 許可年月日1処分;
                cell.setCellValue((String) value);

                while (rs.next()) {
                    boolean 仙台市内flag = rs.getBoolean("仙台市内flag");
                    String officeAddress1 = rs.getString("office_address1");
                    String officeAreaName = rs.getString("office_area_name");

                    if (officeAreaName == null || "".equals(officeAreaName)) {
                        officeAreaName = "宮城県内一円(仙台市を除く)";
                    }
                    try {

                        row1 = sheet1.getRow(25 + v現在明細行);//26行目スタート
                        cell = row1.getCell(0);//A列
                        value = rs.getString("base_goods_name");//大分類名
                        cell.setCellValue((String) value);

                        cell = row1.getCell(2);//C列
                        value = rs.getString("ent_name");//排出業者名
                        cell.setCellValue((String) value);

                        // 現場住所が空白だった場合には住所欄に「住所登録なし」を入力
                        if (!"".equals(officeAddress1)) {

                            cell = row1.getCell(3); // D列
                            value = officeAreaName;
                            cell.setCellValue((String) value);

                            cell = row1.getCell(4); // E列
                            value = officeAddress1;
                            cell.setCellValue((String) value);

                        } else {

                            cell = row1.getCell(4); // E列
                            value = "住所登録なし";
                            cell.setCellValue((String) value);
                        }

                        cell = row1.getCell(5);//F列
                        value = rs.getDouble("net_weight_ton");//受託量
                        cell.setCellValue((Double) value);

                        cell = row1.getCell(6);//G列
                        value = "t";//単位
                        cell.setCellValue((String) value);

                        cell = row1.getCell(7);//H列
                        value = "自社処理";//処理区分
                        cell.setCellValue((String) value);

                        //処分方法　method_name
//                        cell = row1.getCell(8);//I列
//                        value = rs.getString("method_name");//処分方法
//                        cell.setCellValue((String) value);
                        cell = row1.getCell(8);//I列
                        value = "破砕";//処分方法
                        cell.setCellValue((String) value);

                        cell = row1.getCell(9);//J列
                        value = rs.getDouble("net_weight_ton");//処分量
                        cell.setCellValue((Double) value);

                        cell = row1.getCell(10);//K列
                        value = "t";//単位
                        cell.setCellValue((String) value);

                        cell = row1.getCell(11);//K列
                        value = rs.getDouble("net_weight_ton");//処分後量
                        cell.setCellValue((Double) value);

                        cell = row1.getCell(12);//L列
                        value = "t";//単位
                        cell.setCellValue((String) value);

                        cell = row1.getCell(13);//N列
                        value = "川崎町";//処分場所(市区町村)
                        cell.setCellValue((String) value);

                        cell = row1.getCell(14);//O列
                        value = rs.getString("last_office_name");//処分場所
                        cell.setCellValue((String) value);

                        v現在明細行++;

                    } catch (Exception e) {
                        System.out.printf(e.toString());
                    }
                }

            } finally {
                ps.close();
            }

            //シート再計算
            sheet1.setForceFormulaRecalculation(true);
            //印刷シート選択
            sheet1.setSelected(true);
            //印刷範囲の設定　シート番号、列開始、列終了、行開始、行終了の順
            wb1.setPrintArea(0, 0, 22, 0, 24 + v現在明細行);

            try {
                //自身のtmpに保存
                out1 = new FileOutputStream(storeFol);
                wb1.write(out1);
                //スクリプトから印刷
                print_out = new File(storeFol);
                doPrintXlsx(print_out.getAbsolutePath(), printer[1], printer[1], 1, false);
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
            System.out.println(ie1.toString());
        }
    }

    //宮城県産業廃棄物処分実績報告書（最終）
    private void printYearlyReport2(
             String file_path,
             String storeFol,
             Connection con,
             Integer term_date) {

        try {
            InputStream in1 = new FileInputStream(file_path);
            wb1 = WorkbookFactory.create(in1);
            initCom();
            Sheet sheet1 = wb1.getSheet("行政報告集計");

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");

            FileOutputStream out1 = null;
            File out2 = null;
            File print_out = null;

            Row row1;
            Cell cell;
            Object value = null;
            String[] printer = getPrinter();

            int old_base_goods_id = 0;
            int v現在明細行 = 0;
            double net_weight_ton_小計 = 0.0;
            String old品名 = "";

            //自社処理分のみ品目・排出業者・排出現場・運搬業者・処分方法ごとに集計//////////
            String s = new StringBuilder()
                     .append("select * ")
                     .append(" from v1_yearly_data_00_sendai where term_year = ? ")
                     .append(" and cont_id in (1) ")
                     .append(" and final_goods_kind in (1) ")//産廃区分 1:通常産業廃棄物　2:特別管理産業廃棄物
                     .append(" and goods_option = 1 ")//最終処分のみ
                     .append(" order by base_goods_id, ent_id, cont_id, last_office_name, method_name ; ")
                     .toString();
            PreparedStatement ps = con.prepareStatement(s);
            try {
                ps.setObject(1, term_date);
                ResultSet rs = ps.executeQuery();

                row1 = sheet1.getRow(2);//3行目
                cell = row1.getCell(1);//B列
                value = m自社名称;//m許可番号1
                cell.setCellValue((String) value);

                row1 = sheet1.getRow(3);//4行目
                cell = row1.getCell(1);//B列
                value = m許可番号1処分;//m許可番号1
                cell.setCellValue((String) value);

                row1 = sheet1.getRow(1);//2行目
                cell = row1.getCell(6);//G列
                value = "出力日：" + sdf.format(DBManager.convert(Calendar.getInstance()));//m許可番号1
                cell.setCellValue((String) value);

                while (rs.next()) {
                    try {
                        if (old_base_goods_id != 0 && old_base_goods_id != rs.getInt("base_goods_id")) {
                            row1 = sheet1.createRow(6 + v現在明細行);
                            cell = row1.createCell(0);//A列
                            value = old品名 + "　小計";//大分類名
                            cell.setCellValue((String) value);
                            cell.setCellStyle(style3);
                            cell = row1.createCell(1);//B列
                            cell.setCellStyle(style3);
                            cell = row1.createCell(2);//C列
                            cell.setCellStyle(style3);
                            cell = row1.createCell(3);//D列
                            cell.setCellStyle(style3);
                            cell = row1.createCell(4);//E列
                            cell.setCellStyle(style3);
                            cell = row1.createCell(5);//F列
                            value = net_weight_ton_小計;//受託量
                            cell.setCellValue(Math.round((Double) value * 100d) / 100d + "t");
                            cell.setCellStyle(style4);

                            cell = row1.createCell(6);//E列
                            cell.setCellStyle(style3);
                            cell = row1.createCell(7);//F列
                            value = net_weight_ton_小計;//受託量
                            cell.setCellValue(Math.round((Double) value * 100d) / 100d + "t");
                            cell.setCellStyle(style4);
                            cell = row1.createCell(8);//G列
                            cell.setCellStyle(style3);

                            v現在明細行++;
                            v現在明細行++;
                            v現在明細行++;
                            net_weight_ton_小計 = 0.;
                        }
                        row1 = sheet1.createRow(6 + v現在明細行);//7行目スタート
                        cell = row1.createCell(0);//A列
                        value = rs.getString("base_goods_name");//大分類名
                        cell.setCellValue((String) value);
                        cell.setCellStyle(style3);

                        cell = row1.createCell(1);//B列
                        value = rs.getString("ent_name");//排出業者名
                        cell.setCellValue((String) value);
                        cell.setCellStyle(style3);

                        cell = row1.createCell(2);//C列
                        value = rs.getString("ent_address1") + rs.getString("ent_address2");//排出業者名
                        cell.setCellValue((String) value);
                        cell.setCellStyle(style3);

                        cell = row1.createCell(3);//D列
                        value = rs.getString("office_p_name");//排出現場名
                        cell.setCellValue((String) value);
                        cell.setCellStyle(style3);

                        cell = row1.createCell(4);//C列
                        value = rs.getString("office_address1") + rs.getString("office_address2");//排出現場住所
                        cell.setCellValue((String) value);
                        cell.setCellStyle(style3);

                        cell = row1.createCell(5);//D列
                        value = rs.getDouble("net_weight_ton");//受託量
                        net_weight_ton_小計 += (Double) value;
                        cell.setCellValue((Double) value + "t");
                        cell.setCellStyle(style4);

                        //処分方法　method_name
                        cell = row1.createCell(6);//E列
                        value = rs.getString("method_name");//処分方法
                        cell.setCellValue((String) value);
                        cell.setCellStyle(style3);

                        cell = row1.createCell(7);//F列
                        value = rs.getDouble("net_weight_ton");//処分量
                        cell.setCellValue((Double) value + "t");
                        cell.setCellStyle(style4);

                        //cont_office_addres
                        cell = row1.createCell(8);//G列
                        value = rs.getString("cont_office_address");//処分場所
                        cell.setCellValue((String) value);
                        cell.setCellStyle(style3);

                        v現在明細行++;
                        old_base_goods_id = rs.getInt("base_goods_id");
                        old品名 = rs.getString("base_goods_name");

                    } catch (Exception e) {
                        System.out.printf(e.toString());
                    }
                }

                row1 = sheet1.createRow(6 + v現在明細行);
                cell = row1.createCell(0);//A列
                value = old品名 + "　小計";//大分類名
                cell.setCellValue((String) value);
                cell.setCellStyle(style3);
                cell = row1.createCell(1);//B列
                cell.setCellStyle(style3);
                cell = row1.createCell(2);//C列
                cell.setCellStyle(style3);
                cell = row1.createCell(3);//D列
                cell.setCellStyle(style3);
                cell = row1.createCell(4);//E列
                cell.setCellStyle(style3);
                cell = row1.createCell(5);//F列
                value = net_weight_ton_小計;//受託量
                cell.setCellValue(Math.round((Double) value * 100d) / 100d + "t");
                cell.setCellStyle(style4);

                cell = row1.createCell(6);//E列
                cell.setCellStyle(style3);
                cell = row1.createCell(7);//F列
                value = net_weight_ton_小計;//受託量
                cell.setCellValue(Math.round((Double) value * 100d) / 100d + "t");
                cell.setCellStyle(style4);
                cell = row1.createCell(8);//G列
                cell.setCellStyle(style3);

            } finally {
                ps.close();
            }

            //シート再計算
            sheet1.setForceFormulaRecalculation(true);
            //印刷シート選択
            sheet1.setSelected(true);

            try {
                //自身のtmpに保存
                out1 = new FileOutputStream(storeFol);
                wb1.write(out1);
                //スクリプトから印刷
                print_out = new File(storeFol);
                doPrintXlsx(print_out.getAbsolutePath(), printer[1], printer[1], 1, false);
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
            System.out.println(ie1.toString());
        }
    }

// 宮城県産業廃棄物　収集運搬実績報告書　テンプレート貼り付けVer.
    private void printYearlyReport3(
             String file_path,
             String storeFol,
             Connection con,
             Integer term_date) {

        int v現在明細行 = 0;

        String s = new StringBuilder()
                 .append("select ")
                 .append("v.*, ")
                 .append("public.\"宮城_現場区域判定関数\"(v.office_address1) as office_area_name ")
                 .append("from v1_yearly_data_01_sendai v ")
                 .append("where v.term_year = ? ")
                 .append("and v.trans_id in (1000) ") //自社運搬
                 .append("order by v.base_goods_id, v.ent_id, v.cont_id, v.last_office_name, v.method_name ; ")
                 .toString();

        try (InputStream in1 = new FileInputStream(file_path)) {

            wb1 = WorkbookFactory.create(in1);
            initCom();

            Sheet sheet1 = wb1.getSheet("26sys");

            String[] printer = getPrinter();

            // ===== 事前調査ログ：対象年度データが存在するか =====
            String countAllSql = new StringBuilder()
                     .append("select count(*) as cnt ")
                     .append("from v1_yearly_data_01_sendai ")
                     .append("where term_year = ? ")
                     .toString();

            try (PreparedStatement cps = con.prepareStatement(countAllSql)) {
                cps.setObject(1, term_date);
                try (ResultSet crs = cps.executeQuery()) {
                    if (crs.next()) {
                        logger.info("printYearlyReport3 count term_year only"
                                 + " term_year=" + term_date
                                 + ", count=" + crs.getInt("cnt"));
                    }
                }
            }
            String 選択年度 = String.valueOf(term_date) + "年度";
            // ===== ヘッダー出力 =====
            Row row1;
            Cell cell;
            Object value;

            row1 = sheet1.getRow(2); // 3行目
            if (row1 == null) {
                row1 = sheet1.createRow(5);
            }
            cell = row1.getCell(0); // A列
            if (cell == null) {
                cell = row1.createCell(0);
            }

            value = "産業廃棄物・特別管理産業廃棄物の運搬実績報告書 (" + 選択年度 + ")";
            cell.setCellValue((String) value);

            row1 = sheet1.getRow(5); // 6行目
            if (row1 == null) {
                row1 = sheet1.createRow(5);
            }
            cell = row1.getCell(10); // K列
            if (cell == null) {
                cell = row1.createCell(10);
            }

            value = 現在年月日();
            cell.setCellValue((String) value);

            row1 = sheet1.getRow(10); // 11行目
            if (row1 == null) {
                row1 = sheet1.createRow(10);
            }
            cell = row1.getCell(8); // I列
            if (cell == null) {
                cell = row1.createCell(8);
            }
            value = m自社住所;
            cell.setCellValue((String) value);

            row1 = sheet1.getRow(12); // 13行目
            if (row1 == null) {
                logger.warning("printYearlyReport3 row 12 is null. create row.");
                row1 = sheet1.createRow(12);
            }
            cell = row1.getCell(8); // I列
            if (cell == null) {
                cell = row1.createCell(8);
            }
            value = m自社名称 + "　" + m自社代表者名;
            cell.setCellValue((String) value);

            row1 = sheet1.getRow(15); // 16行目
            if (row1 == null) {
                logger.warning("printYearlyReport3 row 15 is null. create row.");
                row1 = sheet1.createRow(15);
            }
            cell = row1.getCell(8); // I列
            if (cell == null) {
                cell = row1.createCell(8);
            }
            value = m自社電話;
            cell.setCellValue((String) value);

            row1 = sheet1.getRow(18); // 19行目
            if (row1 == null) {
                row1 = sheet1.createRow(20);
            }

            cell = row1.getCell(0); // A列
            if (cell == null) {
                cell = row1.createCell(0);
            }
            value = 選択年度 + "の産業廃棄物の処理の実績について、廃棄物の処理"
                     + "及び清掃に関する法律施行細則第6条の２第２項の規定により、次の通り報告します。";
            cell.setCellValue((String) value);

            row1 = sheet1.getRow(20); // 21行目
            if (row1 == null) {
                row1 = sheet1.createRow(20);
            }

            cell = row1.getCell(2); // C列
            if (cell == null) {
                cell = row1.createCell(2);
            }
            value = m許可番号1処分;
            cell.setCellValue((String) value);

            cell = row1.getCell(7); // H列
            if (cell == null) {
                cell = row1.createCell(7);
            }
            value = 許可年月日1処分;
            cell.setCellValue((String) value);

            try (PreparedStatement ps = con.prepareStatement(s)) {

                ps.setObject(1, term_date);

                try (ResultSet rs = ps.executeQuery()) {

                    while (rs.next()) {
                        int excelRowIndex = 23 + v現在明細行;

                        String baseGoodsName = rs.getString("base_goods_name");
                        String entName = rs.getString("ent_name");
                        String officeAddress1 = rs.getString("office_address1");
                        String officeAreaName = rs.getString("office_area_name");

                        if (officeAddress1 == null) {
                            officeAddress1 = "";
                        }

                        if (officeAreaName == null || "".equals(officeAreaName)) {
                            officeAreaName = "宮城県内一円(仙台市を除く)";
                        }
                        String permissionNo = rs.getString("permission_no");
                        String 処分場住所 = rs.getString("ent_address1");//処分場住所
                        String contOfficeAddress = rs.getString("cont_office_address");
                        double netWeightTon = rs.getDouble("net_weight_ton");

                        try {
                            row1 = sheet1.getRow(excelRowIndex);
                            if (row1 == null) {
                                row1 = sheet1.createRow(excelRowIndex);
                            }

                            cell = row1.getCell(0); // A列
                            if (cell == null) {
                                cell = row1.createCell(0);
                            }
                            value = baseGoodsName;
                            cell.setCellValue((String) value);

                            cell = row1.getCell(2); // C列
                            if (cell == null) {
                                cell = row1.createCell(2);
                            }
                            value = entName;
                            cell.setCellValue((String) value);

                            // 現場住所が空白だった場合には住所欄に「住所登録なし」を入力
                            // 現場住所が空白だった場合には住所欄に「住所登録なし」を入力
                            if (!"".equals(officeAddress1)) {
                                cell = row1.getCell(3); // D列
                                if (cell == null) {
                                    cell = row1.createCell(3);
                                }

                                value = officeAreaName;
                                cell.setCellValue((String) value);

                                cell = row1.getCell(4); // E列
                                if (cell == null) {
                                    cell = row1.createCell(4);
                                }

                                value = officeAddress1;
                                cell.setCellValue((String) value);

                            } else {
                                cell = row1.getCell(3); // D列
                                if (cell == null) {
                                    cell = row1.createCell(3);
                                }
                                value = "住所登録なし";
                                cell.setCellValue((String) value);

                                cell = row1.getCell(4); // E列
                                if (cell == null) {
                                    cell = row1.createCell(4);
                                }
                                value = "住所登録なし";
                                cell.setCellValue((String) value);
                            }

                            cell = row1.getCell(5); // F列
                            if (cell == null) {
                                cell = row1.createCell(5);
                            }
                            value = netWeightTon;
                            cell.setCellValue((Double) value);

                            cell = row1.getCell(6); // G列
                            if (cell == null) {
                                cell = row1.createCell(6);
                            }
                            value = "t";
                            cell.setCellValue((String) value);

                            cell = row1.getCell(7); // H列
                            if (cell == null) {
                                cell = row1.createCell(7);
                            }
                            value = permissionNo;
                            cell.setCellValue((String) value);

                            cell = row1.getCell(8); // I列
                            if (cell == null) {
                                cell = row1.createCell(8);
                            }
                            value = m自社名称;
                            cell.setCellValue((String) value);

                            cell = row1.getCell(9); // J列
                            if (cell == null) {
                                cell = row1.createCell(9);
                            }
                            value = "川崎町";
                            cell.setCellValue((String) value);

                            cell = row1.getCell(10); // K列
                            if (cell == null) {
                                cell = row1.createCell(10);
                            }
                            value = 処分場住所(con);
                            cell.setCellValue((String) value);

                            cell = row1.getCell(11); // L列
                            if (cell == null) {
                                cell = row1.createCell(11);
                            }
                            value = netWeightTon;
                            cell.setCellValue((Double) value);

                            cell = row1.getCell(12); // M列
                            if (cell == null) {
                                cell = row1.createCell(12);
                            }
                            value = "t";
                            cell.setCellValue((String) value);

                            v現在明細行++;

                        } catch (Exception e) {
                            logger.log(Level.SEVERE,
                                     "printYearlyReport3 detail row output error"
                                     + " detailNo=" + (v現在明細行 + 1)
                                     + ", excelRowIndex=" + excelRowIndex
                                     + ", base_goods_name=" + baseGoodsName
                                     + ", ent_name=" + entName,
                                     e);
                        }
                    }
                }
            }

            if (v現在明細行 == 0) {
                logger.warning("printYearlyReport3 no output data."
                         + " term_date=" + term_date
                         + ", condition=term_year = " + term_date + " and trans_id in (1000)");

                logger.warning("printYearlyReport3 possible causes:"
                         + " 1) v1_yearly_data_01_sendai に対象年度データがない"
                         + " 2) trans_id=1000 のデータがない"
                         + " 3) term_year の値が想定年度と違う"
                         + " 4) ビュー v1_yearly_data_01_sendai の条件や元テーブルに対象データがない");
            }

            // シート再計算
            sheet1.setForceFormulaRecalculation(true);

            // 印刷シート選択
            sheet1.setSelected(true);

            // 印刷範囲の設定
            int printEndRow = 22 + v現在明細行;
            wb1.setPrintArea(0, 0, 12, 0, printEndRow);

            try (FileOutputStream out1 = new FileOutputStream(storeFol)) {
                wb1.write(out1);
            }

            File print_out = new File(storeFol);

            doPrintXlsx(print_out.getAbsolutePath(), printer[1], printer[1], 1, false);

        } catch (Exception e) {
            logger.log(Level.SEVERE,
                     "printYearlyReport3 failed"
                     + " file_path=" + file_path
                     + ", storeFol=" + storeFol
                     + ", term_date=" + term_date
                     + ", detailCount=" + v現在明細行,
                     e);
        } finally {
            logger.info("printYearlyReport3 end detailCount=" + v現在明細行);
        }
    }

// 宮城県産業廃棄物管理表交付状況報告書
    private void printYearlyReport4(
             String file_path,
             String storeFol,
             Connection con,
             Integer term_date,
             int ent_id) {

        try (InputStream in1 = new FileInputStream(file_path)) {

            wb1 = WorkbookFactory.create(in1);
            initCom();

            System.out.println("printYearlyReport4 template sheet count=" + wb1.getNumberOfSheets());
            for (int i = 0; i < wb1.getNumberOfSheets(); i++) {
                System.out.println("printYearlyReport4 sheet[" + i + "]=" + wb1.getSheetName(i));
            }

            // テンプレート1枚前提
            if (wb1.getNumberOfSheets() < 1) {
                System.out.println("printYearlyReport4 テンプレートシートが存在しません。sheetCount="
                         + wb1.getNumberOfSheets());
                return;
            }

            Row row;
            Row row1;
            Cell cell;
            Object value;

            int v現在ページcnt = 0;
            int v現在レコードcnt = 0;
            int v現在明細行 = 4;

            boolean hasData = false;

            String[] printer = getPrinter();
            if (printer == null || printer.length <= 1 || printer[1] == null) {
                System.out.println("printYearlyReport4 printer[1] is null or invalid");
                return;
            }

            String s = new StringBuilder()
                     .append("select ")
                     .append("max(office_name) as genba_office_name, ")
                     .append("max(office_name_neo) as office_name, ")
                     .append("max(office_address1) as office_address1, ")
                     .append("max(office_address2) as office_address2 ")
                     .append("from v1_yearly_data_04_sendai where term_year = ? ")
                     .append("and ent_id in (?) ")
                     .append("group by office_name_neo ")
                     .append("order by office_name_neo; ")
                     .toString();

            try (PreparedStatement ps = con.prepareStatement(s)) {

                ps.setObject(1, term_date);
                ps.setObject(2, ent_id);

                try (ResultSet rs = ps.executeQuery()) {

                    while (rs.next()) {
                        String 選択年度 = String.valueOf(term_date) + "年度";
                        hasData = true;
                        // テンプレート0番シートを複製して、出力用シートとして使う
                        wb1.cloneSheet(0);
                        Sheet sheet2 = wb1.getSheetAt(wb1.getNumberOfSheets() - 1);

                        // 排出業者情報取得
                        if (ent_id != 1) {
                            String s_ent = "select * from v0_ent where id = ? ;";

                            try (PreparedStatement ps_ent = con.prepareStatement(s_ent)) {
                                ps_ent.setObject(1, ent_id);

                                try (ResultSet rs_ent = ps_ent.executeQuery()) {
                                    if (rs_ent.next()) {

                                        row = getOrCreateRow(sheet2, 2);
                                        cell = getOrCreateCell(row, 0);
                                        value = "産業廃棄物管理票交付状況報告書　(" + 選択年度 + ")";
                                        cell.setCellValue((String) value);

                                        row = getOrCreateRow(sheet2, 7);
                                        cell = getOrCreateCell(row, 9);
                                        value = m自社住所;
                                        cell.setCellValue((String) value);

                                        row = getOrCreateRow(sheet2, 8);
                                        cell = getOrCreateCell(row, 9);
                                        value = m自社名称 + m自社代表者名;
                                        cell.setCellValue((String) value);

                                        row = getOrCreateRow(sheet2, 11);
                                        cell = getOrCreateCell(row, 9);
                                        value = m自社電話;
                                        cell.setCellValue((String) value);

                                        row = getOrCreateRow(sheet2, 10);
                                        cell = getOrCreateCell(row, 8);
                                        value = "　";
                                        cell.setCellValue((String) value);

                                        row = getOrCreateRow(sheet2, 15);
                                        cell = getOrCreateCell(row, 9);
                                        value = "　" + rs_ent.getString("tel");
                                        cell.setCellValue((String) value);
                                    }
                                }
                            }

                        } else {
                            row = getOrCreateRow(sheet2, 8);
                            cell = getOrCreateCell(row, 8);
                            value = "　" + m自社住所;
                            cell.setCellValue((String) value);

                            row = getOrCreateRow(sheet2, 9);
                            cell = getOrCreateCell(row, 8);
                            value = "　" + m自社名称;
                            cell.setCellValue((String) value);

                            row = getOrCreateRow(sheet2, 10);
                            cell = getOrCreateCell(row, 8);
                            value = "　" + m自社代表者名;
                            cell.setCellValue((String) value);

                            row = getOrCreateRow(sheet2, 11);
                            cell = getOrCreateCell(row, 8);
                            value = "　" + m自社電話;
                            cell.setCellValue((String) value);
                        }

                        // タイトル
                        row = getOrCreateRow(sheet2, 2);
                        cell = getOrCreateCell(row, 5);
                        value = String.format("産業廃棄物管理票交付等状況報告書（ %s ）", 選択年度);
                        cell.setCellValue((String) value);

                        row = getOrCreateRow(sheet2, 13);
                        cell = getOrCreateCell(row, 1);
                        value = String.format(
                                 "廃棄物の処理及び清掃に関する法律第12条の3第7項の規定に基づき、%sの産業廃棄物管理票に関する報告書を提出します。",
                                 選択年度);
                        cell.setCellValue((String) value);

                        row = getOrCreateRow(sheet2, 3);
                        cell = getOrCreateCell(row, 11);
                        value = 現在年月日();
                        cell.setCellValue((String) value);

                        // 現場名
                        row = getOrCreateRow(sheet2, 14);
                        cell = getOrCreateCell(row, 4);
                        value = rs.getString("office_name");
                        cell.setCellValue((String) value);

                        // 現場住所
                        row = getOrCreateRow(sheet2, 15);
                        cell = getOrCreateCell(row, 4);
                        value = rs.getString("genba_office_name");
//                        value = rs.getString("office_address1") + rs.getString("office_address2");
                        cell.setCellValue((String) value);

                        row = getOrCreateRow(sheet2, 4);
                        cell = getOrCreateCell(row, 1);
                        value = "宮城県知事　　　　　　　　　　様";
                        cell.setCellValue((String) value);

                        String ds = new StringBuilder()
                                 .append("select * ")
                                 .append("from v1_yearly_data_04_sendai where term_year = ? ")
                                 .append("and ent_id in (?) ")
                                 .append("and office_name_neo like ? ")
                                 .append("order by base_goods_id, trans_id, cont_id ; ")
                                 .toString();

                        try (PreparedStatement dps = con.prepareStatement(ds)) {

                            dps.setObject(1, term_date);
                            dps.setObject(2, ent_id);
                            dps.setObject(3, rs.getString("office_name"));

                            int rs_cnt = 0;
                            try (ResultSet drsCount = dps.executeQuery()) {
                                while (drsCount.next()) {
                                    rs_cnt++;
                                }
                            }

                            double page_cnt = rs_cnt <= 4 ? 1 : Math.ceil((rs_cnt - 4) / 10d);

                            try (ResultSet drs = dps.executeQuery()) {

                                v現在レコードcnt = 0;
                                v現在明細行 = 4;
                                Sheet sheet4 = null;

                                while (drs.next()) {
                                    String officeAddress1 = drs.getString("last_office_name");
                                    if (v現在レコードcnt < 4) {
                                        // 4件目までは表紙側に出力
                                        int sheetIndex = wb1.getSheetIndex(sheet2);
                                        if (sheetIndex >= 0) {
                                            wb1.setPrintArea(sheetIndex, "$A$1:$M$33");
                                        }

                                        PrintSetup prints = sheet2.getPrintSetup();
                                        prints.setPaperSize(PrintSetup.A4_PAPERSIZE);
                                        prints.setLandscape(true);

                                        row1 = getOrCreateRow(sheet2, 17 + v現在レコードcnt);

                                        cell = getOrCreateCell(row1, 1);
                                        value = v現在レコードcnt + 1;
                                        cell.setCellValue((Integer) value);

                                        cell = getOrCreateCell(row1, 2);
                                        value = drs.getString("base_goods_name");
                                        cell.setCellValue((String) value);

                                        cell = getOrCreateCell(row1, 3);
                                        value = drs.getDouble("net_weight_ton");
                                        cell.setCellValue(Math.round((Double) value * 100d) / 100d + "t");

                                        cell = getOrCreateCell(row1, 5);
                                        value = drs.getString("cnt") + "枚";
                                        cell.setCellValue((String) value);

                                        cell = getOrCreateCell(row1, 6);
                                        value = drs.getString("trans_permission_no");
                                        cell.setCellValue((String) value);

                                        cell = getOrCreateCell(row1, 7);
                                        value = drs.getString("trans_p_name");
                                        cell.setCellValue((String) value);

                                        cell = getOrCreateCell(row1, 8);
                                        value = 処分場住所(con);
                                        cell.setCellValue((String) value);

                                        cell = getOrCreateCell(row1, 9);
                                        value = drs.getString("permission_no");
                                        cell.setCellValue((String) value);

                                        cell = getOrCreateCell(row1, 10);
                                        value = drs.getString("cont_p_name");
                                        cell.setCellValue((String) value);

                                        cell = getOrCreateCell(row1, 11);
                                        value = 処分場住所(con);
                                        cell.setCellValue((String) value);

                                    } else {
                                        // 5件目以降は、テンプレート0番シートを複製して別紙として使う
                                        if (v現在明細行 == 4) {
                                            wb1.cloneSheet(0);
                                            sheet4 = wb1.getSheetAt(wb1.getNumberOfSheets() - 1);

                                            int sheet4Index = wb1.getSheetIndex(sheet4);
                                            if (sheet4Index >= 0) {
                                                wb1.setPrintArea(sheet4Index, "$A$1:$L$20");
                                            }

                                            PrintSetup prints = sheet4.getPrintSetup();
                                            prints.setPaperSize(PrintSetup.A4_PAPERSIZE);
                                            prints.setLandscape(true);

                                            row1 = getOrCreateRow(sheet4, 1);
                                            cell = getOrCreateCell(row1, 10);

                                            value = String.format("（別紙　%s　　／　%s　ページ　）",
                                                     v現在レコードcnt + 1 == 5 ? "1"
                                                              : v現在レコードcnt + 1 == 15 ? "2"
                                                                       : v現在レコードcnt + 1 == 20 ? "3"
                                                                                : v現在レコードcnt + 1 == 25 ? "4"
                                                                                         : v現在レコードcnt + 1 == 30 ? "5"
                                                                                                  : v現在レコードcnt + 1 == 35 ? "6"
                                                                                                           : v現在レコードcnt + 1 == 40 ? "7"
                                                                                                                    : v現在レコードcnt + 1 == 45 ? "8"
                                                                                                                             : v現在レコードcnt + 1 == 50 ? "9" : "10",
                                                     (int) page_cnt);
                                            cell.setCellValue((String) value);

                                            // 現場名
                                            row = getOrCreateRow(sheet4, 2);
                                            cell = getOrCreateCell(row, 3);
                                            value = rs.getString("office_name");
                                            cell.setCellValue((String) value);
                                        }

                                        if (sheet4 == null) {
                                            System.out.println("printYearlyReport4 sheet4 is null");
                                            return;
                                        }

                                        row1 = getOrCreateRow(sheet4, v現在明細行);

                                        cell = getOrCreateCell(row1, 1);
                                        value = v現在レコードcnt + 1;
                                        cell.setCellValue((Integer) value);

                                        cell = getOrCreateCell(row1, 2);
                                        value = drs.getString("base_goods_name");
                                        cell.setCellValue((String) value);

                                        cell = getOrCreateCell(row1, 3);
                                        value = drs.getDouble("net_weight_ton");
                                        cell.setCellValue(Math.round((Double) value * 100d) / 100d + "t");

                                        cell = getOrCreateCell(row1, 5);
                                        value = drs.getString("cnt") + "枚";
                                        cell.setCellValue((String) value);

                                        cell = getOrCreateCell(row1, 6);
                                        value = m許可番号1運搬;
                                        cell.setCellValue((String) value);

                                        cell = getOrCreateCell(row1, 7);
                                        value = drs.getString("trans_name");
                                        cell.setCellValue((String) value);

                                        cell = getOrCreateCell(row1, 8);
                                        value = drs.getString("cont_office_address");
                                        cell.setCellValue((String) value);

                                        cell = getOrCreateCell(row1, 9);
                                        value = drs.getString("permission_no");
                                        cell.setCellValue((String) value);

                                        cell = getOrCreateCell(row1, 10);
                                        value = drs.getString("cont_p_name");
                                        cell.setCellValue((String) value);

                                        cell = getOrCreateCell(row1, 11);
                                        String contOfficeAddress = drs.getString("cont_office_address");
                                        String contAddress = drs.getString("cont_address1") + drs.getString("cont_address2");

                                        if (contOfficeAddress != null && contOfficeAddress.equals(contAddress)) {
                                            value = "運搬先に同じ";
                                        } else {
                                            value = contAddress;
                                        }
                                        cell.setCellValue((String) value);

                                        if (v現在明細行 == 13) {
                                            v現在ページcnt++;
                                            v現在明細行 = 4;
                                            sheet4 = null;
                                        } else {
                                            v現在明細行++;
                                        }
                                    }

                                    v現在レコードcnt++;
                                }
                            }
                        }
                    }
                }
            }

            if (!hasData) {
                System.out.println("printYearlyReport4 出力対象データがありません。"
                         + " term_date=" + term_date
                         + ", ent_id=" + ent_id);
                return;
            }

            // 全シート再計算
            for (int i = 0; i < wb1.getNumberOfSheets(); i++) {
                wb1.getSheetAt(i).setForceFormulaRecalculation(true);
            }

            System.out.println("printYearlyReport4 remove前シート数=" + wb1.getNumberOfSheets());

            // テンプレート1枚 + 出力シート1枚以上が必要
            if (wb1.getNumberOfSheets() <= 1) {
                System.out.println("printYearlyReport4 出力シートが作成されていないため保存を中止します。sheetCount="
                         + wb1.getNumberOfSheets());
                return;
            }

            // 0番目のテンプレートシートだけ削除
            wb1.removeSheetAt(0);

            if (wb1.getNumberOfSheets() == 0) {
                System.out.println("printYearlyReport4 テンプレート削除後にシートが0枚になったため保存を中止します。");
                return;
            }

            try (FileOutputStream out1 = new FileOutputStream(storeFol)) {
                wb1.write(out1);
            }

            File print_out = new File(storeFol);
            doPrintXlsx(print_out.getAbsolutePath(), printer[1], printer[1], 1, false);

        } catch (Exception ie1) {
            System.out.println(ie1.toString());
            ie1.printStackTrace();
        }
    }

    private Row getOrCreateRow(Sheet sheet, int rowIndex) {
        Row row = sheet.getRow(rowIndex);
        if (row == null) {
            row = sheet.createRow(rowIndex);
        }
        return row;
    }

    private Cell getOrCreateCell(Row row, int columnIndex) {
        Cell cell = row.getCell(columnIndex);
        if (cell == null) {
            cell = row.createCell(columnIndex);
        }
        return cell;
    }

    //宮城県産業廃棄物処理実績報告書　テンプレート貼り付けVer.
    private void printYearlyReport5(
             String file_path,
             String storeFol,
             Connection con,
             Integer term_date) {

        logger.info("printYearlyReport5 start"
                 + " file_path=" + file_path
                 + ", storeFol=" + storeFol
                 + ", term_date=" + term_date
                 + ", con=" + con);

        int v現在明細行 = 0;

        String s = new StringBuilder()
                 .append("select * ")
                 .append("from v1_yearly_data_03_sendai where term_year = ? ")
                 .append("and final_goods_kind in (1) ")
                 .append("and goods_option = 0 ")
                 .append("and base_goods_id <> 0 ")
                 .append("order by base_goods_id, ent_id, cont_id, last_office_name ; ")
                 .toString();

        logger.info("printYearlyReport5 sql=" + s);

        try (InputStream in1 = new FileInputStream(file_path)) {

            wb1 = WorkbookFactory.create(in1);
            initCom();

            // 表面シートへヘッダー情報を出力
            Sheet sheetFront = wb1.getSheet("25sys_表面");
            if (sheetFront == null) {
                logger.warning("printYearlyReport5 sheet not found: 25sys_表面");
                return;
            }

            Calendar now = Calendar.getInstance();

            String 選択年度 = String.valueOf(term_date) + "年度";

            SimpleDateFormat sdf提出日 = new SimpleDateFormat("yyyy年MM月dd日");
            String todayText = sdf提出日.format(now.getTime());

// 4行目A列: 産業廃棄物処理実績報告書(現在の西暦)
            Row frontRow = sheetFront.getRow(3); // 4行目
            if (frontRow == null) {
                frontRow = sheetFront.createRow(3);
            }
            Cell frontCell = frontRow.getCell(0); // A列
            if (frontCell == null) {
                frontCell = frontRow.createCell(0);
            }
            frontCell.setCellValue("産業廃棄物処理実績報告書" + 選択年度);

// 5行目H列: 今日の日付
            frontRow = sheetFront.getRow(5); // 6行目
            if (frontRow == null) {
                frontRow = sheetFront.createRow(5);
            }
            frontCell = frontRow.getCell(7); // H列
            if (frontCell == null) {
                frontCell = frontRow.createCell(7);
            }
            frontCell.setCellValue(todayText);

// 12行目F列: 住所
            frontRow = sheetFront.getRow(11); // 12行目
            if (frontRow == null) {
                frontRow = sheetFront.createRow(11);
            }
            frontCell = frontRow.getCell(5); // F列
            if (frontCell == null) {
                frontCell = frontRow.createCell(5);
            }
            frontCell.setCellValue(m自社住所);

// 14行目F列: 代表者氏名
            frontRow = sheetFront.getRow(13); // 14行目
            if (frontRow == null) {
                frontRow = sheetFront.createRow(13);
            }
            frontCell = frontRow.getCell(5); // F列
            if (frontCell == null) {
                frontCell = frontRow.createCell(5);
            }
            frontCell.setCellValue(m自社代表者名);

// 17行目F列: 電話番号
            frontRow = sheetFront.getRow(16); // 17行目
            if (frontRow == null) {
                frontRow = sheetFront.createRow(16);
            }
            frontCell = frontRow.getCell(5); // F列
            if (frontCell == null) {
                frontCell = frontRow.createCell(5);
            }
            frontCell.setCellValue(m自社電話);

            // 4行目A列: 産業廃棄物処理実績報告書(現在の西暦)
            frontRow = sheetFront.getRow(19); // 4行目
            if (frontRow == null) {
                frontRow = sheetFront.createRow(19);
            }
            frontCell = frontRow.getCell(0); // A列
            if (frontCell == null) {
                frontCell = frontRow.createCell(0);
            }
            frontCell.setCellValue(選択年度 + "の産業廃棄物の処理の実績について、廃棄物の処理及び"
                     + "清掃に関する法律施行細則第6条の２第１項の規定により、別紙のとおり報告します。");

            Sheet sheet1 = wb1.getSheet("25sys_裏面1");
            if (sheet1 == null) {
                logger.warning("printYearlyReport5 sheet not found: 25sys_裏面1");
                return;
            }

            String[] printer = getPrinter();
            logger.info("printYearlyReport5 printer="
                     + " printer[0]=" + (printer != null && printer.length > 0 ? printer[0] : "null")
                     + ", printer[1]=" + (printer != null && printer.length > 1 ? printer[1] : "null"));

            if (printer == null || printer.length <= 1 || printer[1] == null) {
                logger.warning("printYearlyReport5 printer[1] is null or invalid");
                return;
            }

            logger.info("printYearlyReport5 header values"
                     + " m市区町村=" + m市区町村
                     + ", m自社住所=" + m自社住所
                     + ", m自社名称=" + m自社名称);

            // ヘッダー出力
            Row row1 = sheet1.getRow(1);
            if (row1 == null) {
                logger.warning("printYearlyReport5 row 1 is null");
                return;
            }

            Cell cell = row1.getCell(1);
            if (cell == null) {
                cell = row1.createCell(1);
            }
            cell.setCellValue(m市区町村);

            cell = row1.getCell(2);
            if (cell == null) {
                cell = row1.createCell(2);
            }

            String addressAfterCity = "";
            if (m自社住所 != null && m市区町村 != null && m自社住所.contains(m市区町村)) {
                addressAfterCity = m自社住所.substring(m自社住所.indexOf(m市区町村) + m市区町村.length());
            } else {
                logger.warning("printYearlyReport5 address parse warning"
                         + " m自社住所=" + m自社住所
                         + ", m市区町村=" + m市区町村);
                addressAfterCity = m自社住所;
            }
            cell.setCellValue(addressAfterCity);

            row1 = sheet1.getRow(2);
            if (row1 == null) {
                logger.warning("printYearlyReport5 row 2 is null");
                return;
            }

            cell = row1.getCell(1);
            if (cell == null) {
                cell = row1.createCell(1);
            }
            cell.setCellValue(m自社名称);

            try (PreparedStatement ps = con.prepareStatement(s)) {

                ps.setObject(1, term_date);
                logger.info("printYearlyReport5 executeQuery term_date=" + term_date);

                try (ResultSet rs = ps.executeQuery()) {

                    while (rs.next()) {
                        int excelRowIndex = 7 + v現在明細行;

                        String lastOfficeName = rs.getString("last_office_name");
                        String baseGoodsName = rs.getString("base_goods_name");
                        double netWeightTon = rs.getDouble("net_weight_ton");

                        logger.info("printYearlyReport5 row data"
                                 + " detailNo=" + (v現在明細行 + 1)
                                 + ", excelRowIndex=" + excelRowIndex
                                 + ", last_office_name=" + lastOfficeName
                                 + ", base_goods_name=" + baseGoodsName
                                 + ", net_weight_ton=" + netWeightTon);

                        try {
                            row1 = sheet1.getRow(excelRowIndex);
                            if (row1 == null) {
                                logger.warning("printYearlyReport5 excel row is null. create row."
                                         + " excelRowIndex=" + excelRowIndex);
                                row1 = sheet1.createRow(excelRowIndex);
                            }

                            cell = row1.getCell(0);
                            if (cell == null) {
                                cell = row1.createCell(0);
                            }
                            cell.setCellValue("木くず又はがれき類の破砕施設");

                            cell = row1.getCell(1);
                            if (cell == null) {
                                cell = row1.createCell(1);
                            }
                            cell.setCellValue(baseGoodsName);

                            cell = row1.getCell(2);
                            if (cell == null) {
                                cell = row1.createCell(2);
                            }
                            cell.setCellValue(netWeightTon);

                            cell = row1.getCell(3);
                            if (cell == null) {
                                cell = row1.createCell(3);
                            }
                            cell.setCellValue("t");

//                            cell = row1.getCell(6);
//                            if (cell == null) {
//                                cell = row1.createCell(6);
//                            }
//                            cell.setCellValue("t");
//                            cell = row1.getCell(7);
//                            if (cell == null) {
//                                cell = row1.createCell(7);
//                            }
//                            cell.setCellValue("破砕");
                            v現在明細行++;

                        } catch (Exception e) {
                            logger.log(Level.SEVERE,
                                     "printYearlyReport5 detail row error"
                                     + " detailNo=" + (v現在明細行 + 1)
                                     + ", excelRowIndex=" + excelRowIndex
                                     + ", last_office_name=" + lastOfficeName
                                     + ", base_goods_name=" + baseGoodsName
                                     + ", net_weight_ton=" + netWeightTon,
                                     e);
                        }
                    }
                }
            }

            logger.info("printYearlyReport5 query finished. detailCount=" + v現在明細行);

            // シート再計算
            sheet1.setForceFormulaRecalculation(true);

            // 印刷シート選択
            sheet1.setSelected(true);

            // 印刷範囲の設定
            int printEndRow = 6 + v現在明細行;
            wb1.setPrintArea(1, 0, 9, 0, printEndRow);

            logger.info("printYearlyReport5 print area set"
                     + " sheetIndex=1"
                     + ", startCol=0"
                     + ", endCol=9"
                     + ", startRow=0"
                     + ", endRow=" + printEndRow);

            // 自身のtmpに保存
            logger.info("printYearlyReport5 write workbook start storeFol=" + storeFol);

            try (FileOutputStream out1 = new FileOutputStream(storeFol)) {
                wb1.write(out1);
            }

            logger.info("printYearlyReport5 write workbook end storeFol=" + storeFol);

            File print_out = new File(storeFol);

            logger.info("printYearlyReport5 doPrintXlsx start"
                     + " file=" + print_out.getAbsolutePath()
                     + ", printer=" + printer[1]);

            doPrintXlsx(print_out.getAbsolutePath(), printer[1], printer[1], 1, false);

            logger.info("printYearlyReport5 doPrintXlsx end");

        } catch (Exception e) {
            logger.log(Level.SEVERE,
                     "printYearlyReport5 failed"
                     + " file_path=" + file_path
                     + ", storeFol=" + storeFol
                     + ", term_date=" + term_date
                     + ", detailCount=" + v現在明細行,
                     e);
        } finally {
            logger.info("printYearlyReport5 end detailCount=" + v現在明細行);
        }
    }

    private String formatDateJp(Date date) {
        if (date == null) {
            return "";
        }
        return new SimpleDateFormat("yyyy年M月d日").format(date);
    }
}
