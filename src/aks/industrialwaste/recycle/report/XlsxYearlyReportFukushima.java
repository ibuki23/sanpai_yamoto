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
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.channels.FileChannel;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.PrintSetup;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

public class XlsxYearlyReportFukushima extends OSCReport {

    //福島県産業廃棄物処分実績報告書（中間）
    public static final int YEARLY_REPORT1 = 1;
    //福島県産業廃棄物処分実績報告書（最終）
    public static final int YEARLY_REPORT2 = 2;
    //福島県産業廃棄物運搬実績報告書
    public static final int YEARLY_REPORT3 = 3;
    //福島県産業廃棄物管理表交付状況報告書
    public static final int YEARLY_REPORT4 = 4;
    //福島県産業廃棄物処分実績報告書
    public static final int YEARLY_REPORT5 = 5;
    private Connection con_;
    private CellStyle style3, style4;
    private Workbook wb1;
    private Font font;
    private String m自社住所 = "";
    private String m自社名称 = "";
    private String m自社代表者名 = "";
    private String m自社電話 = "";
    private String m許可番号1 = "";
    private String m許可番号1運搬 = "";
    private String m許可番号2運搬 = "";

    /**
     * 月報を印刷するMonthlyReportのインスタンスを生成する
     *
     */
    public XlsxYearlyReportFukushima() {

        this(YEARLY_REPORT1);
    }

    /**
     * 帳票を印刷するMonthlyReportのインスタンスを生成する.
     *
     * @param reportID 印刷する帳票のID
     */
    public XlsxYearlyReportFukushima(int reportID) {
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

    //実績があるかどうか
    public Boolean isExistData(Connection con, Calendar c) {
        boolean ret = false;

        String s = "";
        s += "select count(*) as cn from tanker.v1_weigh_data";
        s += " where date_trunc('month', term_date) = ? ";
        s += " and purpose_sno in (1);";
        try {
            PreparedStatement ps = con.prepareStatement(s);
            try {
                ps.setObject(1, DBManager.convert(c.getTime()));
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    int i = 0;
                    i = rs.getInt("cn");
                    if (0 < i) {
                        ret = true;
                    }
                }
            } finally {
                ps.close();
            }
        } catch (SQLException e) {
        }

        return ret;
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
                    template = "template\\行政報告書\\福島県_行政報告書_中間.xlsx";//福島県_行政報告書_中間.xlsx
                    break;
                case YEARLY_REPORT2:
                    template = "template\\行政報告書\\福島県_行政報告書最終処分.xlsx";
                    break;
                case YEARLY_REPORT3:
                    //産業廃棄物　運搬実績報告書
                    template = "template\\行政報告書\\福島県_処分実績報告書.xlsx";
                    break;
                case YEARLY_REPORT4:
                    //管理表交付状況報告書
                    template = "template\\行政報告書\\福島県_管理票交付等状況報告書.xlsx";
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
            m許可番号1 = "";
            m許可番号1運搬 = "";
            m許可番号2運搬 = "";
            String s0 = new StringBuilder()
                     .append("select * ")
                     .append(" from m_manifesto where sno = 1; ")
                     .toString();

            PreparedStatement ps0 = con.prepareStatement(s0);

            try {
                ResultSet rs0 = ps0.executeQuery();

                if (rs0.next()) {
                    m自社住所 = "  " + rs0.getString("trust_name1");
                    m自社名称 = "  " + rs0.getString("company_name");
                    m自社代表者名 = "      代表取締役　" + rs0.getString("trust_name2");
                    m自社電話 = "    " + rs0.getString("trust_name3");
                    m許可番号1 = "  " + rs0.getString("permission_no");
                    m許可番号1運搬 = "  " + rs0.getString("permission_no_trans");
                    m許可番号2運搬 = "  " + rs0.getString("permission_no_trans2");
                }

            } finally {
                ps0.close();
            }
            /////////////////////////////////////////////////////////////////

            if (YEARLY_REPORT1 == report_sno) {
                //福島県産業廃棄物処分実績報告書（中間）
                printYearlyReport1(tmpFile.getPath(), tmpFile.getName(), path, con_, term_date);
            } else if (YEARLY_REPORT2 == report_sno) {
                //福島県産業廃棄物処分実績報告書（最終）
                printYearlyReport2(tmpFile.getPath(), tmpFile.getName(), path, con_, term_date);
            } else if (YEARLY_REPORT3 == report_sno) {
                //福島県産業廃棄物 運搬実績報告書
                printYearlyReport3_1(tmpFile.getPath(), tmpFile.getName(), path, con_, term_date, 1, 2);
            } else if (YEARLY_REPORT4 == report_sno) {
                //福島県産業廃棄物管理表交付状況報告書
                printYearlyReport4(tmpFile.getPath(), tmpFile.getName(), path, con_, term_date, ent_id);
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
//      logger_.warning(e.toString());
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

    //産業廃棄物処分実績報告書（中間）
    private Boolean printYearlyReport1(
             String file_path,
             String file_name,
             String storeFol,
             Connection con,
             Integer term_date) {

        boolean ret = false;
        int cnt00 = 0;
        try {
            InputStream in1 = new FileInputStream(file_path);
            wb1 = WorkbookFactory.create(in1);
            initCom();
            Sheet sheet1 = wb1.getSheet("中間処分");

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");

            FileOutputStream out1 = null;
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
                     .append("from v1_yearly_data_00_fukushima where term_year = ? ")
                     .append("and cont_id in (1) ")
                     .append("and final_goods_kind in (1) ")//産廃区分 1:通常産業廃棄物　2:特別管理産業廃棄物
                     .append("and goods_option = 0 ")//中間処分のみ 
                     .append("and base_goods_name <> '' ")//品目大分類が登録されているもの
                     .append("order by base_goods_id, ent_id, cont_id, method_name; ")//, last_office_name
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
                value = m許可番号1;//m許可番号1
                cell.setCellValue((String) value);

                row1 = sheet1.getRow(1);//2行目
                cell = row1.getCell(8);//I列
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
                            cell.setCellValue(Math.round((Double) value * 100d) / 100d);// + "t"
                            cell.setCellStyle(style4);

                            cell = row1.createCell(6);//E列
                            cell.setCellStyle(style3);
                            cell = row1.createCell(7);//F列
                            cell.setCellStyle(style3);
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
                        value = rs.getString("ent_address1");//排出業者住所
                        cell.setCellValue((String) value);
                        cell.setCellStyle(style3);

                        cell = row1.createCell(3);//D列
                        value = rs.getString("office_name");//排出現場名
                        cell.setCellValue((String) value);
                        cell.setCellStyle(style3);

                        cell = row1.createCell(4);//E列
                        value = rs.getString("office_address1");//排出現場住所
                        cell.setCellValue((String) value);
                        cell.setCellStyle(style3);

                        cell = row1.createCell(5);//F列
                        value = rs.getDouble("net_weight_ton");//受託量
                        net_weight_ton_小計 += (Double) value;
                        cell.setCellValue((Double) value);// + "t"
                        cell.setCellStyle(style4);

                        //処分方法　method_name
                        cell = row1.createCell(6);//G列
                        value = rs.getString("method_name");//処分方法
                        cell.setCellValue((String) value);
                        cell.setCellStyle(style3);

                        //運搬先の事業場
                        cell = row1.createCell(7);//H列
                        value = rs.getString("cont_office_name");//
                        cell.setCellValue((String) value);
                        cell.setCellStyle(style3);

                        //cont_office_addres
                        cell = row1.createCell(8);//I列
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
                cell.setCellValue(Math.round((Double) value * 100d) / 100d);// + "t"
                cell.setCellStyle(style4);

                cell = row1.createCell(6);//E列
                cell.setCellStyle(style3);
                cell = row1.createCell(7);//F列
                cell.setCellStyle(style3);
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
            System.out.println(ie1.toString() + String.valueOf(cnt00));
        }

        return ret;
    }

    //産業廃棄物処分実績報告書（最終）
    private Boolean printYearlyReport2(
             String file_path,
             String file_name,
             String storeFol,
             Connection con,
             Integer term_date) {

        boolean ret = false;
        int cnt00 = 0;
        try {
            InputStream in1 = new FileInputStream(file_path);
            wb1 = WorkbookFactory.create(in1);
            initCom();
            Sheet sheet1 = wb1.getSheet("中間処分");

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");

            FileOutputStream out1 = null;
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
                     .append("from v1_yearly_data_00_fukushima where term_year = ? ")
                     .append("and cont_id in (1) ")
                     .append("and final_goods_kind in (1) ")//産廃区分 1:通常産業廃棄物　2:特別管理産業廃棄物
                     .append("and goods_option = 1 ")//最終処分のみ 
                     .append("and base_goods_name <> '' ")//品目大分類が登録されているもの
                     .append("order by base_goods_id, ent_id, cont_id, method_name; ")//, last_office_name
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
                value = m許可番号1;//m許可番号1
                cell.setCellValue((String) value);

                row1 = sheet1.getRow(1);//2行目
                cell = row1.getCell(8);//I列
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
                            cell.setCellValue(Math.round((Double) value * 100d) / 100d);// + "t"
                            cell.setCellStyle(style4);

                            cell = row1.createCell(6);//E列
                            cell.setCellStyle(style3);
                            cell = row1.createCell(7);//F列
                            cell.setCellStyle(style3);
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
                        value = rs.getString("ent_address1");//排出業者住所
                        cell.setCellValue((String) value);
                        cell.setCellStyle(style3);

                        cell = row1.createCell(3);//D列
                        value = rs.getString("office_name");//排出現場名
                        cell.setCellValue((String) value);
                        cell.setCellStyle(style3);

                        cell = row1.createCell(4);//E列
                        value = rs.getString("office_address1");//排出現場住所
                        cell.setCellValue((String) value);
                        cell.setCellStyle(style3);

                        cell = row1.createCell(5);//F列
                        value = rs.getDouble("net_weight_ton");//受託量
                        net_weight_ton_小計 += (Double) value;
                        cell.setCellValue((Double) value);// + "t"
                        cell.setCellStyle(style4);

                        //処分方法　method_name
                        cell = row1.createCell(6);//G列
                        value = rs.getString("method_name");//処分方法
                        cell.setCellValue((String) value);
                        cell.setCellStyle(style3);

                        //運搬先の事業場
                        cell = row1.createCell(7);//H列
                        value = rs.getString("cont_office_name");//
                        cell.setCellValue((String) value);
                        cell.setCellStyle(style3);

                        //cont_office_addres
                        cell = row1.createCell(8);//I列
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
                cell.setCellValue(Math.round((Double) value * 100d) / 100d);// + "t"
                cell.setCellStyle(style4);

                cell = row1.createCell(6);//E列
                cell.setCellStyle(style3);
                cell = row1.createCell(7);//F列
                cell.setCellStyle(style3);
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
            System.out.println(ie1.toString() + String.valueOf(cnt00));
        }

        return ret;
    }

    private Boolean printYearlyReport3_1(
             String file_path,
             String file_name,
             String storeFol,
             Connection con,
             Integer term_date,
             int trans_id,
             int pref_id) {
        boolean ret = false;

        try {
            InputStream in1 = new FileInputStream(file_path);
            wb1 = WorkbookFactory.create(in1);
            initCom();
            Sheet sheet1 = wb1.getSheet("様式-業A");

            FileOutputStream out1 = null;
            File print_out = null;

            String[] printer = getPrinter();

            // 様式-業A
            boolean resultGyouA = youshikiGyouA(sheet1, term_date);
            if (resultGyouA == false) {
                System.out.println("失敗　様式-業A");
            }

            // 様式-業C
            boolean resultGyouC = youshikiGyouC(wb1, con, term_date);
            if (resultGyouC == false) {
                System.out.println("失敗　様式-業C");
            }

            // 様式-業D
            boolean resultGyouD = youshikiGyouD(wb1, con, term_date);
            if (resultGyouD == false) {
                System.out.println("失敗　様式-業D");
            }

            // 様式-業E
            Sheet sheet2 = wb1.getSheet("様式-業E");
            boolean youshikiGyouE = youshikiGyouE(wb1, sheet2, con, term_date);
            if (youshikiGyouE == false) {
                System.out.println("失敗 様式-業E");
            }

            try {
                // 自身のtmpに保存
                out1 = new FileOutputStream(storeFol);
                wb1.write(out1);
                // スクリプトから印刷
                print_out = new File(storeFol);
                doPrintXlsx(print_out.getAbsolutePath(), printer[1], printer[1], 1, false);
            } catch (IOException ex) {
                Logger.getLogger(XlsxYearlyReportFukushima.class.getName()).log(Level.SEVERE, null, ex);
            } finally {
                try {
                    out1.close();
                } catch (IOException ex) {
                    Logger.getLogger(XlsxYearlyReportFukushima.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

            ret = true;
        } catch (FileNotFoundException ex) {
            Logger.getLogger(XlsxYearlyReportFukushima.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(XlsxYearlyReportFukushima.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InvalidFormatException ex) {
            Logger.getLogger(XlsxYearlyReportFukushima.class.getName()).log(Level.SEVERE, null, ex);
        }

        return ret;
    }

    // 福島県の産業廃棄物・特別管理産業廃棄物等の収集運搬、処理・処分実勢k報告書の様式-業Aに貼り付ける処理
    private Boolean youshikiGyouA(
             Sheet sheetGyouA,
             Integer term_date) {
        boolean ret = false;

        Locale locale = new Locale("ja", "JP", "JP");
        DateFormat japaneseFormat = new SimpleDateFormat("GGGGy年M月d日", locale);
        DateFormat japaneseFormat2 = new SimpleDateFormat("GGGGy年", locale);
        Date today = new Date();
        Calendar cale = Calendar.getInstance();
        cale.set(term_date, 1, 1);

        Row row1;
        Cell cell1;

        // 日時
        row1 = sheetGyouA.getRow(4);
        cell1 = row1.getCell(6);
        cell1.setCellValue(japaneseFormat.format(today));

        // 住所
        row1 = sheetGyouA.getRow(6);
        cell1 = row1.getCell(6);
        cell1.setCellValue(m自社住所.trim());

        // 会社名、代表者名
        row1 = sheetGyouA.getRow(7);
        cell1 = row1.getCell(6);
        cell1.setCellValue(m自社名称.trim());

        row1 = sheetGyouA.getRow(8);
        cell1 = row1.getCell(6);
        cell1.setCellValue(m自社代表者名.trim());

        // 電話番号
        row1 = sheetGyouA.getRow(9);
        cell1 = row1.getCell(6);
        cell1.setCellValue(m自社電話.trim());

        // 文言
        row1 = sheetGyouA.getRow(11);
        cell1 = row1.getCell(1);
        String tmpStr10 = String.format("　%s度の産業廃棄物・特別管理産業廃棄物等の処理・処分実績（運搬実績）について、次のとおり報告します。",
                 japaneseFormat2.format(cale.getTime()));
        cell1.setCellValue(tmpStr10);

        // 事業概要の表
        // 業者名枠の氏名または名称
        row1 = sheetGyouA.getRow(14);
        cell1 = row1.getCell(4);
        cell1.setCellValue(m自社名称.trim());

        // 業者名枠の住所
        row1 = sheetGyouA.getRow(15);
        cell1 = row1.getCell(4);
        cell1.setCellValue(m自社住所.trim());

        // 業者名枠の代表者氏名
        row1 = sheetGyouA.getRow(16);
        cell1 = row1.getCell(4);
        cell1.setCellValue(m自社代表者名.trim());

        // 業者名枠の電話番号
        row1 = sheetGyouA.getRow(17);
        cell1 = row1.getCell(4);
        cell1.setCellValue(m自社電話.trim());

        // 事業所枠の名称
        row1 = sheetGyouA.getRow(18);
        cell1 = row1.getCell(4);
        cell1.setCellValue(m自社名称.trim());

        // 事業所枠の所在地
        row1 = sheetGyouA.getRow(19);
        cell1 = row1.getCell(4);
        cell1.setCellValue(m自社住所.trim());

        // 事業所枠の電話番号
        row1 = sheetGyouA.getRow(20);
        cell1 = row1.getCell(4);
        cell1.setCellValue(m自社電話.trim());

        // 事業所枠の事業内容、テンプレート側に書く予定
        // 事業概要の表（収集運搬業用）
        // 福島県枠の、有り、産業廃棄物の許可番号、特別管理産業廃棄物の許可番号、収集運搬実績の有無、県外への搬出
        // 入力規則のコンボボックスは恐らく触れないので、テンプレート側に書く予定
        row1 = sheetGyouA.getRow(41);
        cell1 = row1.getCell(4);
        cell1.setCellValue("第" + m許可番号1運搬.trim() + "号");

        cell1 = row1.getCell(5);
        cell1.setCellValue("第" + m許可番号2運搬.trim() + "号");

        // 記入者部課・氏名
        // 産廃システム上にデータは無いので、手入力頂く。
        // 報告書チェック欄
        // テンプレート側に書く予定。
        ret = true;
        return ret;
    }

    private Boolean youshikiGyouC(
             Workbook wb,
             Connection con,
             Integer term_date
    ) {
        boolean ret = false;
        Row row1;
        Cell cell1;

        Locale locale = new Locale("ja", "JP", "JP");
        DateFormat japaneseFormat2 = new SimpleDateFormat("GGGGy年", locale);
        Calendar cale = Calendar.getInstance();
        cale.set(term_date, 1, 1);

        int startDetailLine = 10;
        int maxDetailLines = 5;
        int maxShisetsu = 5;
        int countShisetsu = 0;
        int countDetail = 0;
        int countPage = 1;

        Sheet sheet = wb.cloneSheet(wb.getSheetIndex("様式-業C"));
        wb.setSheetName(wb.getSheetIndex(sheet), "様式-業C_" + countPage);

        // 年度実績
        row1 = sheet.getRow(4);
        cell1 = row1.getCell(1);
        String tmpStr10 = String.format("%s度実績", japaneseFormat2.format(cale.getTime()));
        cell1.setCellValue(tmpStr10);

        // 業者名
        cell1 = row1.getCell(18);
        cell1.setCellValue("業者名　" + m自社名称);

        // 処理施設の種類数はDBに無い。
        // 大分類毎、品目毎に集計を掛けて、貼り付けるしかない？
        try {
            String sql = new StringBuilder()
                     .append("SELECT ")
                     .append("base_goods_id ")
                     .append("FROM v1_yearly_data_00_fukushima ")
                     .append("WHERE term_year = ? ")
                     .append("AND cont_id IN (1) ")
                     .append("AND final_goods_kind IN (1, 2) ") // 通常廃棄物と特別管理
                     .append("AND goods_option IN(0, 1) ") // 中間処分と最終処理
                     .append("AND base_goods_name <> '' ")
                     .append("GROUP BY base_goods_id ")
                     .append("ORDER BY base_goods_id; ")
                     .toString();
            PreparedStatement ps = con.prepareStatement(sql);

            try {
                ps.setObject(1, term_date);
                ResultSet rs = ps.executeQuery();
                // 大分類のループ
                while (rs.next()) {
                    String sql2 = new StringBuilder()
                             .append("SELECT ")
                             .append("goods_id, ")
                             .append("MAX(base_goods_name) AS base_goods_name, ")
                             .append("MAX(goods_name) AS goods_name, ")
                             .append("MAX(net_weight_ton) AS net_weight_ton ")
                             .append("FROM v1_yearly_data_00_fukushima ")
                             .append("WHERE term_year = ? ")
                             .append("AND base_goods_id = ? ")
                             .append("AND cont_id IN (1) ")
                             .append("AND final_goods_kind IN (1, 2) ") // 通常廃棄物と特別管理
                             .append("AND goods_option IN(0, 1) ") // 中間処分と最終処理
                             .append("AND base_goods_name <> '' ")
                             .append("GROUP BY goods_id ")
                             .append("ORDER BY goods_id; ")
                             .toString();
                    PreparedStatement ps2 = con.prepareStatement(sql2);
                    ps2.setObject(1, term_date);
                    ps2.setObject(2, rs.getInt("base_goods_id"));
                    ResultSet rs2 = ps2.executeQuery();
                    // 品目のループ
                    while (rs2.next()) {
                        row1 = sheet.getRow(startDetailLine + (countShisetsu * maxDetailLines) + countDetail);
                        if (countDetail == 0) {
                            cell1 = row1.getCell(2);
                            String tmpStr1 = "" + rs2.getString("base_goods_name");
                            tmpStr1 += "の処理施設";
                            cell1.setCellValue(tmpStr1);
                        }
                        // 処理した種類
                        cell1 = row1.getCell(6);
                        cell1.setCellValue(rs2.getString("goods_name"));
                        // 処理した処理量
                        cell1 = row1.getCell(8);
                        cell1.setCellValue(rs2.getDouble("net_weight_ton"));
                        // 処理した単位
                        cell1 = row1.getCell(9);
                        cell1.setCellValue("t");

                        countDetail++;
                        if (countDetail >= maxDetailLines) {
                            // １大分類に５品目は無い想定。
                            Logger.getLogger(XlsxYearlyReportFukushima.class.getName()).log(Level.SEVERE, "5品目以上ある");
                            break;
                        }
                    }
                    countDetail = 0;

                    countShisetsu++;
                    if (countShisetsu >= maxShisetsu) {
                        countPage++;
                        sheet = wb.cloneSheet(wb.getSheetIndex("様式-業C"));
                        wb.setSheetName(wb.getSheetIndex(sheet), "様式-業C_" + countPage);

                        countShisetsu = 0;
                    }
                }
            } catch (SQLException ex) {
                Logger.getLogger(XlsxYearlyReportFukushima.class.getName()).log(Level.SEVERE, null, ex);
            } finally {
                ps.close();
            }

            wb.removeSheetAt(wb.getSheetIndex("様式-業C"));
            ret = true;
        } catch (SQLException ex) {
            Logger.getLogger(XlsxYearlyReportFukushima.class.getName()).log(Level.SEVERE, null, ex);
        }

        return ret;
    }

    private Boolean youshikiGyouD(
             Workbook wb,
             Connection con,
             Integer term_date
    ) {
        boolean ret = false;

        Row row1;
        Cell cell1;

        Locale locale = new Locale("ja", "JP", "JP");
        DateFormat japaneseFormat2 = new SimpleDateFormat("GGGGy年", locale);
        Calendar cale = Calendar.getInstance();
        cale.set(term_date, 1, 1);

        int startDetailLine = 9;
        int maxDetailLines = 15;
        int countDetail = 0;
        int countPage = 1;

        Sheet sheet = wb.cloneSheet(wb.getSheetIndex("様式-業D"));
        wb.setSheetName(wb.getSheetIndex(sheet), "様式-業D_" + countPage);

        // 年度実績
        row1 = sheet.getRow(4);
        cell1 = row1.getCell(1);
        String tmpStr10 = String.format("%s度実績", japaneseFormat2.format(cale.getTime()));
        cell1.setCellValue(tmpStr10);

        // 業者名
        cell1 = row1.getCell(12);
        cell1.setCellValue("業者名　" + m自社名称);

        try {
            String sql = new StringBuilder()
                     .append("SELECT * ")
                     .append("FROM v1_yearly_data_00_fukushima ")
                     .append("WHERE term_year = ? ")
                     .append("AND cont_id IN (1) ")
                     .append("AND final_goods_kind IN (1, 2) ") // 通常廃棄物と特別管理
                     .append("AND goods_option IN (0, 1) ") // 中間処分と最終処理
                     .append("AND base_goods_name <> '' ")
                     .append("ORDER BY base_goods_id, ent_id, cont_id, method_name; ")
                     .toString();

            PreparedStatement ps = con.prepareStatement(sql);

            try {
                ps.setObject(1, term_date);
                ResultSet rs = ps.executeQuery();

                while (rs.next()) {
                    try {
                        row1 = sheet.getRow(startDetailLine + countDetail);

                        // 廃棄物の種類
                        cell1 = row1.getCell(2);
                        cell1.setCellValue(rs.getString("base_goods_name"));

                        // 委託者の区分は手入力
                        // 備考は手入力
                        // 排出事業者
                        cell1 = row1.getCell(6);
                        cell1.setCellValue(rs.getString("ent_name"));
                        // 排出事業者住所
                        cell1 = row1.getCell(7);
                        String tmpStr1 = "" + rs.getString("ent_address1");
                        cell1.setCellValue(tmpStr1);
                        // 受託量
                        cell1 = row1.getCell(8);
                        cell1.setCellValue(rs.getDouble("net_weight_ton"));
                        // 単位
                        cell1 = row1.getCell(9);
                        cell1.setCellValue("t");

                        // 処分方法
                        cell1 = row1.getCell(10);
                        String tmpStr3 = "" + rs.getString("goods_option_name");
                        cell1.setCellValue(tmpStr3);

                        // 施設番号は手入力
                        // 処分場所
                        cell1 = row1.getCell(12);
                        cell1.setCellValue(rs.getString("cont_office_address"));

                        // 処分により生じた廃棄物の他社等での処分は手入力
                        countDetail++;
                        if (countDetail >= maxDetailLines) {
                            countPage++;
                            sheet = wb.cloneSheet(wb.getSheetIndex("様式-業D"));
                            wb.setSheetName(wb.getSheetIndex(sheet), "様式-業D_" + countPage);
                            countDetail = 0;
                        }

                    } catch (Exception ex) {
                        Logger.getLogger(XlsxYearlyReportFukushima.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }

                wb.removeSheetAt(wb.getSheetIndex("様式-業D"));
                ret = true;
            } catch (Exception ex) {
                Logger.getLogger(XlsxYearlyReportFukushima.class.getName()).log(Level.SEVERE, null, ex);
            } finally {
                ps.close();
            }
        } catch (SQLException ex) {
            Logger.getLogger(XlsxYearlyReportFukushima.class.getName()).log(Level.SEVERE, null, ex);
        }

        return ret;
    }

    // 福島県の産業廃棄物・特別管理産業廃棄物等の収集運搬、処理・処分実勢k報告書の様式-業Eに貼り付ける処理
    private Boolean youshikiGyouE(
             Workbook wb,
             Sheet sheetGyouE,
             Connection con,
             Integer term_date) {
        boolean ret = false;

        Locale locale = new Locale("ja", "JP", "JP");
        DateFormat japaneseFormat2 = new SimpleDateFormat("GGGGy年", locale);
        Calendar cale = Calendar.getInstance();
        cale.set(term_date, 1, 1);

        Row row1;
        Cell cell1;
        int startDetailLine = 9;
        int maxDetailLines = 15;
        int countDetail = 0;
        int countPage = 1;

        Sheet sheet = wb.cloneSheet(wb.getSheetIndex("様式-業E"));
        wb.setSheetName(wb.getSheetIndex(sheet), "様式-業E_" + countPage);

        // 年度実績
        row1 = sheet.getRow(4);
        cell1 = row1.getCell(1);
        String tmpStr10 = String.format("%s度実績", japaneseFormat2.format(cale.getTime()));
        cell1.setCellValue(tmpStr10);

        // 業者名
        cell1 = row1.getCell(11);
        cell1.setCellValue("業者名　" + m自社名称);

        try {
            // 集計sql
            String sql = new StringBuilder()
                     .append("SELECT * ")
                     .append("FROM v1_yearly_data_01_fukushima ")
                     .append("WHERE term_year = ? ")
                     .append("AND trans_id in (1) ")
                     .append("AND base_goods_name <> '' ")
                     .append("ORDER BY base_goods_id, ent_id, cont_id; ")
                     .toString();
            PreparedStatement ps = con.prepareStatement(sql);

            try {
                ps.setObject(1, term_date);
                ResultSet rs = ps.executeQuery();

                while (rs.next()) {
                    try {
                        row1 = sheet.getRow(startDetailLine + countDetail);

                        // 廃棄物の種類
                        cell1 = row1.getCell(2);
                        cell1.setCellValue(rs.getString("base_goods_name"));
                        // 排出事業者
                        cell1 = row1.getCell(6);
                        cell1.setCellValue(rs.getString("ent_name"));
                        // 排出事業者住所
                        cell1 = row1.getCell(7);
                        String tmpStr1 = "" + rs.getString("ent_address1");
                        tmpStr1 += rs.getString("ent_address2");
                        cell1.setCellValue(tmpStr1);
                        // 受託量
                        cell1 = row1.getCell(8);
                        cell1.setCellValue(rs.getDouble("net_weight_ton"));
                        // 単位
                        cell1 = row1.getCell(9);
                        cell1.setCellValue("t");

                        // 運搬先の許可番号
                        cell1 = row1.getCell(10);
                        cell1.setCellValue(rs.getString("permission_no"));
                        // 運搬先の名称
                        cell1 = row1.getCell(11);
                        cell1.setCellValue(rs.getString("cont_name"));
                        // 運搬先の住所
                        cell1 = row1.getCell(12);
                        String tmpStr2 = "" + rs.getString("con_address1");
                        cell1.setCellValue(tmpStr2);
                        // 運搬先の処分方法
                        cell1 = row1.getCell(13);
                        String tmpStr3 = "" + rs.getString("goods_option_name");
                        String tmpStr4 = "" + rs.getString("method_name");
                        if (tmpStr4.isEmpty() == false) {
                            tmpStr3 += "(" + tmpStr4 + ")";
                        }
                        cell1.setCellValue(tmpStr3);

                        countDetail++;
                        if (countDetail >= maxDetailLines) {
                            countPage++;
                            sheet = wb.cloneSheet(wb.getSheetIndex("様式-業E"));
                            wb.setSheetName(wb.getSheetIndex(sheet), "様式-業E_" + countPage);
                            countDetail = 0;
                        }

                    } catch (Exception ex) {
                        Logger.getLogger(XlsxYearlyReportFukushima.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            } catch (Exception ex) {
                Logger.getLogger(XlsxYearlyReportFukushima.class.getName()).log(Level.SEVERE, null, ex);
            } finally {
                ps.close();
            }

        } catch (SQLException ex) {
            Logger.getLogger(XlsxYearlyReportFukushima.class.getName()).log(Level.SEVERE, null, ex);
        }

        wb.removeSheetAt(wb.getSheetIndex("様式-業E"));

        System.out.println("aaaaaaaaaaaaa");
        ret = true;
        return ret;
    }

    //産業廃棄物管理表交付状況報告書
    private Boolean printYearlyReport4(
             String file_path,
             String file_name,
             String storeFol,
             Connection con,
             Integer term_date,
             int ent_id) {

        boolean ret = false;
        int cnt00 = 0;

        // ログ用の現在位置
        String currentStep = "start";
        String currentOfficeName = "";
        String currentOfficeAddress1 = "";
        int currentRecordNo = 0;
        int currentSheetIndex = -1;
        int currentRowIndex = -1;
        int currentCellIndex = -1;

        logger_.info(String.format(
                 "printYearlyReport4 start file_path=%s, file_name=%s, storeFol=%s, term_date=%s, ent_id=%s",
                 file_path, file_name, storeFol, term_date, ent_id));

        try {
            currentStep = "open workbook";
            InputStream in1 = new FileInputStream(file_path);
            wb1 = WorkbookFactory.create(in1);
            initCom();

            currentStep = "get sheet 様式";
            Sheet sheet2 = wb1.getSheet("様式");
            if (sheet2 == null) {
                throw new IllegalStateException("テンプレートにシート「様式」が存在しません。file_path=" + file_path);
            }

            FileOutputStream out1 = null;
            File print_out = null;

            Row row, row1;
            Cell cell;
            Object value = null;

            int v現在ページcnt = 0;
            int v現在レコードcnt = 0;
            int v現在明細行 = 4;

            currentStep = "get printer";
            String[] printer = getPrinter();
            logger_.info(String.format(
                     "printer weigh=%s, default=%s, manifesto=%s",
                     printer[0], printer[1], printer[2]));

            String s = new StringBuilder()
                     .append("select ")
                     .append(" max(office_name) as office_name, ")
                     .append(" max(office_address1)as office_address1, ")
                     .append(" max(office_address2)as office_address2 ")
                     .append(" from v1_yearly_data_01_fukushima where term_year = ? ")
                     .append(" and ent_id in (?) ")
                     .append(" group by office_name, office_address1 ")
                     .append(" order by office_name, office_address1; ")
                     .toString();

            logger_.info("printYearlyReport4 office sql=" + s);
            logger_.info(String.format("office sql params term_date=%s, ent_id=%s", term_date, ent_id));

            currentStep = "prepare office sql";
            PreparedStatement ps = con.prepareStatement(s);
            try {
                ps.setObject(1, term_date);
                ps.setObject(2, ent_id);

                currentStep = "execute office sql";
                ResultSet rs = ps.executeQuery();

                int officeCount = 0;

                while (rs.next()) {
                    officeCount++;
                    currentStep = "office loop";
                    currentOfficeName = rs.getString("office_name");
                    currentOfficeAddress1 = rs.getString("office_address1");
                    currentRecordNo = 0;

                    logger_.info(String.format(
                             "office loop start officeCount=%d, office_name=%s, office_address1=%s, office_address2=%s",
                             officeCount,
                             currentOfficeName,
                             currentOfficeAddress1,
                             rs.getString("office_address2")));

                    currentStep = "clone main sheet";
                    wb1.cloneSheet(0);
                    sheet2 = wb1.getSheetAt(wb1.getNumberOfSheets() - 1);
                    currentSheetIndex = wb1.getNumberOfSheets() - 1;

                    if (ent_id != 1) {
                        currentStep = "get ent info";
                        String s_ent = new StringBuilder()
                                 .append("select * from v0_ent where id = ? ;")
                                 .toString();

                        logger_.info("ent sql=" + s_ent);
                        logger_.info("ent sql params ent_id=" + ent_id);

                        PreparedStatement ps_ent = con.prepareStatement(s_ent);
                        try {
                            ps_ent.setObject(1, ent_id);
                            ResultSet rs_ent = ps_ent.executeQuery();

                            if (rs_ent.next()) {
                                currentStep = "set ent info cells";

                                row = sheet2.getRow(6);
                                currentRowIndex = 6;
                                cell = row.getCell(9);
                                currentCellIndex = 9;
                                value = "　" + rs_ent.getString("address1") + rs_ent.getString("address2");
                                cell.setCellValue((String) value);

                                row = sheet2.getRow(7);
                                currentRowIndex = 7;
                                cell = row.getCell(9);
                                currentCellIndex = 9;
                                value = "　" + rs_ent.getString("p_name");
                                cell.setCellValue((String) value);

                                row = sheet2.getRow(13);
                                currentRowIndex = 13;
                                cell = row.getCell(9);
                                currentCellIndex = 9;
                                value = "　" + rs_ent.getString("tel");
                                cell.setCellValue((String) value);
                            } else {
                                logger_.warning("v0_ent に該当データがありません。ent_id=" + ent_id);
                            }
                        } finally {
                            ps_ent.close();
                        }
                    } else {
                        currentStep = "set own company info cells";

                        row = sheet2.getRow(6);
                        currentRowIndex = 6;
                        cell = row.getCell(9);
                        currentCellIndex = 9;
                        value = "　" + m自社住所;
                        cell.setCellValue((String) value);

                        row = sheet2.getRow(7);
                        currentRowIndex = 7;
                        cell = row.getCell(9);
                        currentCellIndex = 9;
                        value = "　" + m自社名称 + " " + m自社代表者名;
                        cell.setCellValue((String) value);

                        row = sheet2.getRow(11);
                        currentRowIndex = 11;
                        cell = row.getCell(9);
                        currentCellIndex = 9;
                        value = "　" + m自社電話;
                        cell.setCellValue((String) value);
                    }

                    currentStep = "set title cells";

                    row = sheet2.getRow(2);
                    currentRowIndex = 2;
                    cell = row.getCell(2);
                    currentCellIndex = 2;
                    value = String.format("産業廃棄物管理票交付等状況報告書（　%s　年度）", term_date);
                    cell.setCellValue((String) value);

                    row = sheet2.getRow(10);
                    currentRowIndex = 10;
                    cell = row.getCell(2);
                    currentCellIndex = 2;
                    value = String.format("廃棄物の処理及び清掃に関する法律第12条の3第7項の規定に基づき、%s年度の産業廃棄物管理票に関する報告書を提出します。", term_date);
                    cell.setCellValue((String) value);

                    row = sheet2.getRow(3);
                    currentRowIndex = 3;
                    cell = row.getCell(10);
                    currentCellIndex = 10;
                    value = DBManager.convert((Calendar.getInstance()).getTime());
                    cell.setCellValue((Date) value);

                    currentStep = "set office cells";

                    row = sheet2.getRow(12);
                    currentRowIndex = 12;
                    cell = row.getCell(4);
                    currentCellIndex = 4;
                    value = currentOfficeName;
                    cell.setCellValue((String) value);

                    row = sheet2.getRow(13);
                    currentRowIndex = 13;
                    cell = row.getCell(4);
                    currentCellIndex = 4;

                    String tmpAddr = "";
                    if (rs.getString("office_address1") != null) {
                        tmpAddr = rs.getString("office_address1");
                    }
                    if (rs.getString("office_address2") != null) {
                        tmpAddr += rs.getString("office_address2");
                    }
                    value = tmpAddr;
                    cell.setCellValue((String) value);

                    int rs_cnt = 0;

                    String ds = new StringBuilder()
                             .append("select * ")
                             // 福島クラスなので、本来はこちらでは？
                             // .append(" from v1_yearly_data_01_fukushima where term_year = ? ")
                             .append(" from v1_yearly_data_01_iwate where term_year = ? ")
                             .append(" and ent_id in (?) ")
                             .append(" and office_name like ? ")
                             .append(" and office_address1 like ? ")
                             .append(" order by base_goods_id, trans_id, cont_id ; ")
                             .toString();

                    logger_.info("printYearlyReport4 detail sql=" + ds);
                    logger_.info(String.format(
                             "detail sql params term_date=%s, ent_id=%s, office_name=%s, office_address1=%s",
                             term_date, ent_id, currentOfficeName, currentOfficeAddress1));

                    currentStep = "prepare detail sql";
                    PreparedStatement dps = con.prepareStatement(ds);
                    try {
                        dps.setObject(1, term_date);
                        dps.setObject(2, ent_id);
                        dps.setObject(3, currentOfficeName);
                        dps.setObject(4, currentOfficeAddress1);

                        currentStep = "execute detail sql count";
                        ResultSet drs = dps.executeQuery();

                        while (drs.next()) {
                            rs_cnt++;
                        }

                        logger_.info(String.format(
                                 "detail count office_name=%s, office_address1=%s, count=%d",
                                 currentOfficeName, currentOfficeAddress1, rs_cnt));

                        double page_cnt = rs_cnt < 4 ? 1 : Math.ceil((rs_cnt - 4) / 10d);

                        currentStep = "execute detail sql output";
                        drs = dps.executeQuery();

                        v現在レコードcnt = 0;
                        v現在明細行 = 4;
                        Sheet sheet4 = null;

                        while (drs.next()) {
                            currentRecordNo = v現在レコードcnt + 1;

                            logger_.fine(String.format(
                                     "detail row recordNo=%d, base_goods_name=%s, trans_name=%s, net_weight_ton=%s",
                                     currentRecordNo,
                                     drs.getString("base_goods_name"),
                                     drs.getString("trans_name"),
                                     drs.getString("net_weight_ton")));

                            if (v現在レコードcnt < 4) {
                                currentStep = "set main detail row";

                                wb1.setPrintArea(1 + v現在ページcnt, "$A$1:$M$33");

                                PrintSetup prints = sheet2.getPrintSetup();
                                prints.setPaperSize(PrintSetup.A4_PAPERSIZE);
                                prints.setLandscape(true);

                                row1 = sheet2.getRow(15 + v現在レコードcnt);
                                currentRowIndex = 15 + v現在レコードcnt;

                                cell = row1.getCell(2);
                                currentCellIndex = 2;
                                value = currentRecordNo;
                                cell.setCellValue((Integer) value);

                                cell = row1.getCell(3);
                                currentCellIndex = 3;
                                value = drs.getString("base_goods_name");
                                cell.setCellValue((String) value);

                                cell = row1.getCell(4);
                                currentCellIndex = 4;
                                value = drs.getDouble("net_weight_ton");
                                cell.setCellValue(Math.round((Double) value * 100d) / 100d + "t");

                                cell = row1.getCell(5);
                                currentCellIndex = 5;
                                value = drs.getString("cnt") + "枚";
                                cell.setCellValue((String) value);

                                cell = row1.getCell(6);
                                currentCellIndex = 6;
                                value = m許可番号1運搬;
                                cell.setCellValue((String) value);

                                cell = row1.getCell(7);
                                currentCellIndex = 7;
                                value = drs.getString("trans_name");
                                cell.setCellValue((String) value);

                                cell = row1.getCell(8);
                                currentCellIndex = 8;
                                value = drs.getString("cont_office_address");
                                cell.setCellValue((String) value);

                                cell = row1.getCell(9);
                                currentCellIndex = 9;
                                value = drs.getString("permission_no");
                                cell.setCellValue((String) value);

                                cell = row1.getCell(10);
                                currentCellIndex = 10;
                                value = drs.getString("cont_p_name");
                                cell.setCellValue((String) value);

                            } else {
                                currentStep = "set extra detail row";

                                sheet4 = null;
                                if (v現在明細行 == 4) {
                                    wb1.cloneSheet(1);
                                }
                                sheet4 = wb1.getSheetAt(wb1.getNumberOfSheets() - 1);
                                currentSheetIndex = wb1.getNumberOfSheets() - 1;

                                if (v現在明細行 == 4) {
                                    wb1.setPrintArea(wb1.getNumberOfSheets() - 1, "$A$1:$L$20");

                                    PrintSetup prints = sheet4.getPrintSetup();
                                    prints.setPaperSize(PrintSetup.A4_PAPERSIZE);
                                    prints.setLandscape(true);

                                    row1 = sheet4.getRow(2);
                                    currentRowIndex = 2;
                                    cell = row1.getCell(11);
                                    currentCellIndex = 11;
                                    value = String.format(" %s ／　%s",
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

                                    row = sheet4.getRow(2);
                                    cell = row.getCell(7);
                                    currentCellIndex = 7;
                                    value = currentOfficeName;
                                    cell.setCellValue((String) value);
                                }

                                row1 = sheet4.getRow(v現在明細行);
                                currentRowIndex = v現在明細行;

                                cell = row1.getCell(2);
                                currentCellIndex = 2;
                                value = currentRecordNo;
                                cell.setCellValue((Integer) value);

                                cell = row1.getCell(3);
                                currentCellIndex = 3;
                                value = drs.getString("base_goods_name");
                                cell.setCellValue((String) value);

                                cell = row1.getCell(4);
                                currentCellIndex = 4;
                                value = drs.getDouble("net_weight_ton");
                                cell.setCellValue(Math.round((Double) value * 100d) / 100d + "t");

                                cell = row1.getCell(5);
                                currentCellIndex = 5;
                                value = drs.getString("cnt") + "枚";
                                cell.setCellValue((String) value);

                                cell = row1.getCell(6);
                                currentCellIndex = 6;
                                value = m許可番号1運搬;
                                cell.setCellValue((String) value);

                                cell = row1.getCell(7);
                                currentCellIndex = 7;
                                value = drs.getString("trans_name");
                                cell.setCellValue((String) value);

                                cell = row1.getCell(8);
                                currentCellIndex = 8;
                                value = drs.getString("cont_office_address");
                                cell.setCellValue((String) value);

                                cell = row1.getCell(9);
                                currentCellIndex = 9;
                                value = drs.getString("permission_no");
                                cell.setCellValue((String) value);

                                cell = row1.getCell(10);
                                currentCellIndex = 10;
                                value = drs.getString("cont_p_name");
                                cell.setCellValue((String) value);

                                if (v現在明細行 == 13) {
                                    v現在ページcnt++;
                                    v現在明細行 = 4;
                                } else {
                                    v現在明細行 = v現在明細行 + 1;
                                }
                            }

                            v現在レコードcnt++;
                        }
                    } finally {
                        dps.close();
                    }

                    logger_.info(String.format(
                             "office loop end officeCount=%d, office_name=%s, detailRecords=%d",
                             officeCount, currentOfficeName, v現在レコードcnt));
                }

            } finally {
                ps.close();
            }

            currentStep = "force formula recalculation";
            sheet2.setForceFormulaRecalculation(true);

            currentStep = "remove template sheets";
            for (int i = 0; i < 2; i++) {
                wb1.removeSheetAt(0);
            }

            try {
                currentStep = "write workbook";
                out1 = new FileOutputStream(storeFol);
                wb1.write(out1);

                currentStep = "print xlsx";
                print_out = new File(storeFol);
                doPrintXlsx(print_out.getAbsolutePath(), printer[1], printer[1], 1, false);

            } catch (IOException e) {
                logger_.log(Level.SEVERE, "Excel出力または印刷起動に失敗しました。storeFol=" + storeFol, e);
            } finally {
                try {
                    if (out1 != null) {
                        out1.close();
                    }
                } catch (IOException e) {
                    logger_.log(Level.WARNING, "FileOutputStream close に失敗しました。storeFol=" + storeFol, e);
                }
            }

            ret = true;
            logger_.info("printYearlyReport4 end ret=true");

        } catch (Exception ie1) {
            logger_.log(Level.SEVERE,
                     String.format(
                              "printYearlyReport4 error step=%s, term_date=%s, ent_id=%s, office_name=%s, office_address1=%s, recordNo=%d, sheetIndex=%d, row=%d, cell=%d, cnt00=%d",
                              currentStep,
                              term_date,
                              ent_id,
                              currentOfficeName,
                              currentOfficeAddress1,
                              currentRecordNo,
                              currentSheetIndex,
                              currentRowIndex,
                              currentCellIndex,
                              cnt00),
                     ie1);
        }

        return ret;
    }
}
