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
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.PrintSetup;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.ss.util.CellRangeAddress;

public class XlsxYearlyReportYamagata extends OSCReport {

    //新庄市産業廃棄物処分実績報告書（中間）
    public static final int YEARLY_REPORT1 = 1;
    //新庄市産業廃棄物処分実績報告書（最終）
    public static final int YEARLY_REPORT2 = 2;
    //新庄市産業廃棄物 運搬実績報告書 
    public static final int YEARLY_REPORT3 = 3;
    //新庄市産業廃棄物管理表交付状況報告書
    public static final int YEARLY_REPORT4 = 4;

    private Connection con_;

    private CellStyle border_style;
    private Workbook wb1;
    private Font gfont;

    public XlsxYearlyReportYamagata() {

        this(YEARLY_REPORT1);
    }

    public XlsxYearlyReportYamagata(int reportID) {
        if ((YEARLY_REPORT1 != reportID)
                 && (YEARLY_REPORT2 != reportID)
                 && (YEARLY_REPORT3 != reportID)
                 && (YEARLY_REPORT4 != reportID)) {
            throw new IllegalArgumentException("'reportID' is not match.");
        }
    }

    private void initCom() {
        gfont = wb1.createFont();
        gfont.setFontName("ＭＳ ゴシック");
        gfont.setFontHeightInPoints((short) 14);

        //四隅のボーダーのみ設定
        border_style = wb1.createCellStyle();
        border_style.setBorderLeft(CellStyle.BORDER_THIN);
        border_style.setBorderRight(CellStyle.BORDER_THIN);
        border_style.setBorderTop(CellStyle.BORDER_THIN);
        border_style.setBorderBottom(CellStyle.BORDER_THIN);
        border_style.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
        border_style.setWrapText(true);
        border_style.setFont(gfont);
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

            if (YEARLY_REPORT1 == report_sno) {
                //山形市産業廃棄物処分実績報告書（中間）
                template = "template\\行政報告書\\山形県_行政報告書_中間.xlsx";
            } else if (YEARLY_REPORT2 == report_sno) {
//          //山形市産業廃棄物処分実績報告書（最終）
                template = "template\\行政報告書\\山形県_行政報告書_最終.xlsx";
            } else if (YEARLY_REPORT3 == report_sno) {
//          //山形市産業廃棄物　運搬実績報告書
                template = "template\\行政報告書\\山形県_行政報告書_運搬.xlsx";
            } else if (YEARLY_REPORT4 == report_sno) {
//          //管理表交付状況報告書
                template = "template\\行政報告書\\山形県_行政報告書_交付.xlsx";
            }

            // テンプレートファイルをローカルにコピー
            File templateFile = new File(template);
            File tmpFile = new File("tmp", templateFile.getName());

            // テンプレートファイルをローカルにコピー
            copyTransfer(templateFile.getPath(), tmpFile.getPath());

            try {

                if (YEARLY_REPORT1 == report_sno) {
                    //山形県産業廃棄物処分実績報告書（中間/最終）
                    printYearlyReport1(tmpFile.getPath(), path, con_, term_date);
                } else if (YEARLY_REPORT2 == report_sno) {
//          //山形市産業廃棄物処分実績報告書（最終）
                    printYearlyReport2(tmpFile.getPath(), path, con_, term_date);
                } else if (YEARLY_REPORT3 == report_sno) {
//          //山形市産業廃棄物 運搬実績報告書 (）
                    printYearlyReport3(tmpFile.getPath(), path, con_, term_date);
                } else if (YEARLY_REPORT4 == report_sno) {
                    //山形市産業廃棄物管理表交付状況報告書
                    printYearlyReport4(tmpFile.getPath(), path, con_, term_date, ent_id);
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

    //山形県産業廃棄物処分実績報告書（中間/最終）
    private void printYearlyReport1(
             String file_path,
             String storeFol,
             Connection con,
             Integer term_date) {

        try {
            InputStream in1 = new FileInputStream(file_path);
            wb1 = WorkbookFactory.create(in1);
            initCom();
            Sheet sheet1 = wb1.getSheet("様式b");
            Sheet sheet2 = wb1.getSheet("様式a");

            SimpleDateFormat sdf_MMdd = new SimpleDateFormat("MM月dd日");

            FileOutputStream out1 = null;
            File print_out = null;
            Row row, row1;
            Cell cell;
            Object value = null;
            String[] printer = getPrinter();
            String sec_ = "";
            PreparedStatement ps_sec;
            ResultSet rs_sec;

            //出力日 
            row = sheet2.getRow(2);//3行目
            cell = row.getCell(21);//V列
            value = String.format("%s年",
                     (Calendar.getInstance().get(Calendar.YEAR)))
                     + sdf_MMdd.format(Calendar.getInstance().getTime());
            cell.setCellValue((String) value);

            //出力日 
            row = sheet1.getRow(1);//2行目
            cell = row.getCell(13);//N列
            value = String.format("%s年",
                     (Calendar.getInstance().get(Calendar.YEAR)))
                     + sdf_MMdd.format(Calendar.getInstance().getTime());
            cell.setCellValue((String) value);

            String s0 = new StringBuilder()
                     .append("select * ")
                     .append(" from m_manifesto where sno = 1; ")
                     .toString();

            PreparedStatement ps0 = con.prepareStatement(s0);

            try {
            } finally {
                ps0.close();
            }

            //（ %s 年度）
            row = sheet2.getRow(9);//10行目
            cell = row.getCell(6);//G列
            value = String.format("（ %s 年度）", (Calendar.getInstance().get(Calendar.YEAR)));
            cell.setCellValue((String) value);

            //（ %s 年度）
            row = sheet1.getRow(8);//9行目
            cell = row.getCell(0);//A列
            value = String.format("　　　産業廃棄物の中間処理施設における処分実績報告書（ %s 年度）", (Calendar.getInstance().get(Calendar.YEAR)));
            cell.setCellValue((String) value);

            //自社排出以外は別紙（様式a）に /////////////////////////////////////////////////////////////////////////////////
            String s = new StringBuilder()
                     .append("select * ")
                     .append(" from v1_yearly_data_00_yamagata where term_year = ? ")
                     .append(" and base_goods_id > 0 ")
                     .append(" order by base_goods_id, ent_id, office_id, cont_id, method_name; ")
                     .toString();

            PreparedStatement ps = con.prepareStatement(s);
            ps.setObject(1, term_date);
            try {
                ResultSet rs = ps.executeQuery();

                int row_no = 23;
                int old品名コード = 0;
                String old品名 = "";
                double net_weight_ton_小計 = 0.;

                //データ数分まわす
                while (rs.next()) {
                    if (row_no < 38) {
                        if (old品名コード != 0 && old品名コード != rs.getInt("base_goods_id")) {
                            row1 = sheet2.getRow(row_no) != null ? sheet2.getRow(row_no) : sheet2.createRow(row_no);
                            cell = row1.getCell(1) != null ? row1.getCell(1) : row1.createCell(1);//B列
                            value = String.format("合計(%s)", old品名);//大分類合計
                            cell.setCellValue((String) value);
                            cell = row1.getCell(8) != null ? row1.getCell(8) : row1.createCell(8);//I列
                            value = net_weight_ton_小計;//受託量
                            cell.setCellValue(Math.round((Double) value * 100d) / 100d);
                            //単位
                            cell = row1.getCell(9) != null ? row1.getCell(9) : row1.createCell(9);//J列
                            cell.setCellValue("t");
                            //変数初期化
                            net_weight_ton_小計 = 0.;
                            row_no++;
                        }
                        row1 = sheet2.getRow(row_no) != null ? sheet2.getRow(row_no) : sheet2.createRow(row_no);//24行目スタート
                        cell = row1.getCell(1) != null ? row1.getCell(1) : row1.createCell(1);//B列
                        value = rs.getString("base_goods_name");//大分類名
                        cell.setCellValue((String) value);
                        cell = row1.getCell(4) != null ? row1.getCell(4) : row1.createCell(4);//E列
                        value = rs.getString("ent_name");//排出業者名
                        cell.setCellValue((String) value);
                        cell = row1.getCell(5) != null ? row1.getCell(5) : row1.createCell(5);//F列
                        value = rs.getString("ent_zip_code");//排出業者郵便番号
                        cell.setCellValue((String) value);
                        cell = row1.getCell(6) != null ? row1.getCell(6) : row1.createCell(6);//G列
                        value = rs.getString("ent_address1");//排出業者住所
                        cell.setCellValue((String) value);
                        //受託量
                        cell = row1.getCell(8) != null ? row1.getCell(8) : row1.createCell(8);//I列
                        value = rs.getDouble("net_weight_ton");//受託量
                        net_weight_ton_小計 += rs.getDouble("net_weight_ton");
                        cell.setCellValue((Double) value);
                        //単位
                        cell = row1.getCell(9) != null ? row1.getCell(9) : row1.createCell(9);//J列
                        cell.setCellValue("t");
                        //処分方法　method_name
                        cell = row1.getCell(10) != null ? row1.getCell(10) : row1.createCell(10);//K列
                        value = rs.getString("method_name");//処分方法
                        cell.setCellValue((String) value);
                        //処分量
                        cell = row1.getCell(12) != null ? row1.getCell(12) : row1.createCell(12);//M列
                        value = rs.getDouble("net_weight_ton");//処分量
                        cell.setCellValue((Double) value);
                        //単位
                        cell = row1.getCell(13) != null ? row1.getCell(13) : row1.createCell(13);//N列
                        cell.setCellValue("t");
                        //処分後量
                        cell = row1.getCell(14) != null ? row1.getCell(14) : row1.createCell(14);//O列
                        value = rs.getDouble("net_weight_ton");//処分後量
                        cell.setCellValue((Double) value);
                        //単位
                        cell = row1.getCell(15) != null ? row1.getCell(15) : row1.createCell(15);//P列
                        cell.setCellValue("t");
                        //cont_office_addres
                        cell = row1.getCell(16) != null ? row1.getCell(16) : row1.createCell(16);//Q列
                        value = rs.getString("cont_office_address");//処分場所
                        cell.setCellValue((String) value);

                        //二次マニフェストがあれば
                        if (rs.getString("secod_manifesto_no").length() > 0) {
                            sec_ = new StringBuilder()
                                     .append("select v1_weigh_data.*, ")
                                     .append(" CASE ")
                                     .append(" WHEN v1_weigh_data.method_name <> ''::text AND v1_weigh_data.method_name IS NOT NULL THEN v1_weigh_data.method_name ")
                                     .append(" WHEN v1_weigh_data.check_flag09 = true THEN '脱水'::text ")
                                     .append(" WHEN v1_weigh_data.check_flag10 = true THEN '焼却'::text ")
                                     .append(" WHEN v1_weigh_data.check_flag11 = true THEN '破砕'::text ")
                                     .append(" WHEN v1_weigh_data.check_flag12 = true THEN '安定型'::text ")
                                     .append(" WHEN v1_weigh_data.check_flag13 = true THEN '管理型'::text ")
                                     .append(" WHEN v1_weigh_data.check_flag14 = true THEN '遮断型'::text ")
                                     .append(" ELSE ''::text ")
                                     .append(" END AS sec_method_name, ")
                                     .append(" m_disposal_contractor.permission_no AS sec_permission_no ")
                                     .append(" from v1_weigh_data ")
                                     .append(" LEFT JOIN m_disposal_contractor ON v1_weigh_data.dis_cont_id = m_disposal_contractor.id ")
                                     .append(" where manifesto_no1 = ? ")
                                     .append(" or manifesto_no2 = ? ")
                                     .append(" or manifesto_no3 = ? ;")
                                     .toString();

                            ps_sec = con.prepareStatement(sec_);
                            ps_sec.setObject(1, rs.getString("secod_manifesto_no"));
                            ps_sec.setObject(2, rs.getString("secod_manifesto_no"));
                            ps_sec.setObject(3, rs.getString("secod_manifesto_no"));
                            rs_sec = ps_sec.executeQuery();
                            if (rs_sec.next()) {
                                //処分受託許可番号　permission_no
                                cell = row1.getCell(18) != null ? row1.getCell(18) : row1.createCell(18);//S列
                                value = rs_sec.getString("sec_permission_no");
                                cell.setCellValue((String) value);
                                //処分業者名
                                cell = row1.getCell(19) != null ? row1.getCell(19) : row1.createCell(19);//T列
                                value = rs_sec.getString("cont_p_name");
                                cell.setCellValue((String) value);
                                //処分業者名郵便番号
                                cell = row1.getCell(20) != null ? row1.getCell(20) : row1.createCell(20);//U列
                                value = rs_sec.getString("cont_zip_code");
                                cell.setCellValue((String) value);
                                //処分業者名住所
                                cell = row1.getCell(21) != null ? row1.getCell(21) : row1.createCell(21);//V列
                                value = rs_sec.getString("cont_address1") + rs_sec.getString("cont_address2");
                                cell.setCellValue((String) value);
                                //処分方法　method_name
                                cell = row1.getCell(23) != null ? row1.getCell(23) : row1.createCell(23);//X列
                                value = rs_sec.getString("sec_method_name");//処分方法
                                cell.setCellValue((String) value);
                                //処分量
                                cell = row1.getCell(25) != null ? row1.getCell(25) : row1.createCell(25);//Z列
                                value = rs.getDouble("net_weight_ton");//受託量
                                cell.setCellValue((Double) value + "t");
                                //単位
                                cell = row1.getCell(26) != null ? row1.getCell(26) : row1.createCell(26);//AA列
                                cell.setCellValue("t");

                            }

                        }
                    } else {
                        if (old品名コード != 0 && old品名コード != rs.getInt("base_goods_id")) {
                            row1 = sheet2.createRow(row_no);
                            row1.setHeightInPoints(60);
                            sheet2.addMergedRegion(new CellRangeAddress(row_no, row_no, 1, 2));
                            sheet2.addMergedRegion(new CellRangeAddress(row_no, row_no, 6, 7));
                            sheet2.addMergedRegion(new CellRangeAddress(row_no, row_no, 10, 11));
                            sheet2.addMergedRegion(new CellRangeAddress(row_no, row_no, 16, 17));
                            sheet2.addMergedRegion(new CellRangeAddress(row_no, row_no, 21, 22));
                            sheet2.addMergedRegion(new CellRangeAddress(row_no, row_no, 23, 24));
                            for (int i = 1; i <= 27; i++) {
                                cell = row1.createCell(i);//B列～AB列まで線を引く
                                cell.setCellStyle(border_style);
                            }

                            cell = row1.createCell(1);//B列
                            value = String.format("合計(%s)", old品名);//大分類合計
                            cell.setCellValue((String) value);
                            cell.setCellStyle(border_style);
                            cell = row1.createCell(8);//I列
                            value = net_weight_ton_小計;//受託量
                            cell.setCellValue(Math.round((Double) value * 100d) / 100d);
                            cell.setCellStyle(border_style);
                            //単位
                            cell = row1.createCell(9);//J列
                            cell.setCellValue("t");
                            cell.setCellStyle(border_style);

                            //変数初期化
                            net_weight_ton_小計 = 0.;
                            row_no++;
                        }
                        row1 = sheet2.createRow(row_no);
                        row1.setHeightInPoints(60);
                        sheet2.addMergedRegion(new CellRangeAddress(row_no, row_no, 1, 2));
                        sheet2.addMergedRegion(new CellRangeAddress(row_no, row_no, 6, 7));
                        sheet2.addMergedRegion(new CellRangeAddress(row_no, row_no, 10, 11));
                        sheet2.addMergedRegion(new CellRangeAddress(row_no, row_no, 16, 17));
                        sheet2.addMergedRegion(new CellRangeAddress(row_no, row_no, 21, 22));
                        sheet2.addMergedRegion(new CellRangeAddress(row_no, row_no, 23, 24));
                        for (int i = 1; i <= 27; i++) {
                            cell = row1.createCell(i);//B列～AB列まで線を引く
                            cell.setCellStyle(border_style);
                        }

                        cell = row1.createCell(1);//B列
                        value = rs.getString("base_goods_name");//大分類名
                        cell.setCellValue((String) value);
                        cell.setCellStyle(border_style);
                        cell = row1.createCell(4);//E列
                        value = rs.getString("ent_name");//排出業者名
                        cell.setCellValue((String) value);
                        cell.setCellStyle(border_style);
                        cell = row1.createCell(5);//F列
                        value = rs.getString("ent_zip_code");//排出業者郵便番号
                        cell.setCellValue((String) value);
                        cell.setCellStyle(border_style);
                        cell = row1.createCell(6);//G列
                        value = rs.getString("ent_address1");//排出業者住所
                        cell.setCellValue((String) value);
                        cell.setCellStyle(border_style);
                        //受託量
                        cell = row1.createCell(8);//I列
                        value = rs.getDouble("net_weight_ton");//受託量
                        net_weight_ton_小計 += rs.getDouble("net_weight_ton");
                        cell.setCellValue((Double) value);
                        cell.setCellStyle(border_style);
                        //単位
                        cell = row1.createCell(9);//J列
                        cell.setCellValue("t");
                        cell.setCellStyle(border_style);
                        //処分方法　method_name
                        cell = row1.createCell(10);//K列
                        value = rs.getString("method_name");//処分方法
                        cell.setCellValue((String) value);
                        cell.setCellStyle(border_style);
                        //処分量
                        cell = row1.createCell(12);//M列
                        value = rs.getDouble("net_weight_ton");//処分量
                        cell.setCellValue((Double) value);
                        cell.setCellStyle(border_style);
                        //単位
                        cell = row1.createCell(13);//N列
                        cell.setCellValue("t");
                        cell.setCellStyle(border_style);
                        //処分後量
                        cell = row1.createCell(14);//O列
                        value = rs.getDouble("net_weight_ton");//処分後量
                        cell.setCellValue((Double) value);
                        cell.setCellStyle(border_style);
                        //単位
                        cell = row1.createCell(15);//P列
                        cell.setCellValue("t");
                        cell.setCellStyle(border_style);
                        //cont_office_addres
                        cell = row1.createCell(16);//Q列
                        value = rs.getString("cont_office_address");//処分場所
                        cell.setCellValue((String) value);
                        cell.setCellStyle(border_style);

                        //二次マニフェストがあれば
                        if (rs.getString("secod_manifesto_no").length() > 0) {
                            sec_ = new StringBuilder()
                                     .append("select v1_weigh_data.*, ")
                                     .append(" CASE ")
                                     .append(" WHEN v1_weigh_data.method_name <> ''::text AND v1_weigh_data.method_name IS NOT NULL THEN v1_weigh_data.method_name ")
                                     .append(" WHEN v1_weigh_data.check_flag09 = true THEN '脱水'::text ")
                                     .append(" WHEN v1_weigh_data.check_flag10 = true THEN '焼却'::text ")
                                     .append(" WHEN v1_weigh_data.check_flag11 = true THEN '破砕'::text ")
                                     .append(" WHEN v1_weigh_data.check_flag12 = true THEN '安定型'::text ")
                                     .append(" WHEN v1_weigh_data.check_flag13 = true THEN '管理型'::text ")
                                     .append(" WHEN v1_weigh_data.check_flag14 = true THEN '遮断型'::text ")
                                     .append(" ELSE ''::text ")
                                     .append(" END AS sec_method_name, ")
                                     .append(" m_disposal_contractor.permission_no AS sec_permission_no ")
                                     .append(" from v1_weigh_data ")
                                     .append(" LEFT JOIN m_disposal_contractor ON v1_weigh_data.dis_cont_id = m_disposal_contractor.id ")
                                     .append(" where manifesto_no1 = ? ")
                                     .append(" or manifesto_no2 = ? ")
                                     .append(" or manifesto_no3 = ? ;")
                                     .toString();

                            ps_sec = con.prepareStatement(sec_);
                            ps_sec.setObject(1, rs.getString("secod_manifesto_no"));
                            ps_sec.setObject(2, rs.getString("secod_manifesto_no"));
                            ps_sec.setObject(3, rs.getString("secod_manifesto_no"));
                            rs_sec = ps_sec.executeQuery();
                            if (rs_sec.next()) {
                                //処分受託許可番号　permission_no
                                cell = row1.createCell(18);//S列
                                value = rs_sec.getString("sec_permission_no");
                                cell.setCellValue((String) value);
                                cell.setCellStyle(border_style);
                                //処分業者名
                                cell = row1.createCell(19);//T列
                                value = rs_sec.getString("cont_p_name");
                                cell.setCellValue((String) value);
                                cell.setCellStyle(border_style);
                                //処分業者名郵便番号
                                cell = row1.createCell(20);//U列
                                value = rs_sec.getString("cont_zip_code");
                                cell.setCellValue((String) value);
                                cell.setCellStyle(border_style);
                                //処分業者名住所
                                cell = row1.createCell(21);//V列
                                value = rs_sec.getString("cont_address1") + rs_sec.getString("cont_address2");
                                cell.setCellValue((String) value);
                                cell.setCellStyle(border_style);
                                //処分方法　method_name
                                cell = row1.createCell(23);//X列
                                value = rs_sec.getString("sec_method_name");//処分方法
                                cell.setCellValue((String) value);
                                cell.setCellStyle(border_style);
                                //処分量
                                cell = row1.createCell(25);//Z列
                                value = rs.getDouble("net_weight_ton");//受託量
                                cell.setCellValue((Double) value + "t");
                                cell.setCellStyle(border_style);
                                //単位
                                cell = row1.createCell(26);//AA列
                                cell.setCellValue("t");
                                cell.setCellStyle(border_style);

                            }
                        }
                    }

                    // 品名ごとに小計行を入れる
                    old品名コード = rs.getInt("base_goods_id");
                    old品名 = rs.getString("base_goods_name");
                    row_no++;

                }

                //最終品目の小計
                row1 = sheet2.createRow(row_no);
                row1.setHeightInPoints(60);
                if (row_no > 37) {
                    sheet2.addMergedRegion(new CellRangeAddress(row_no, row_no, 1, 2));
                    sheet2.addMergedRegion(new CellRangeAddress(row_no, row_no, 6, 7));
                    sheet2.addMergedRegion(new CellRangeAddress(row_no, row_no, 10, 11));
                    sheet2.addMergedRegion(new CellRangeAddress(row_no, row_no, 16, 17));
                    sheet2.addMergedRegion(new CellRangeAddress(row_no, row_no, 21, 22));
                    sheet2.addMergedRegion(new CellRangeAddress(row_no, row_no, 23, 24));
                }
                for (int i = 1; i <= 27; i++) {
                    cell = row1.createCell(i);//B列～AB列まで線を引く
                    cell.setCellStyle(border_style);
                }
                cell = row1.createCell(1);//B列
                cell.setCellStyle(border_style);

                value = String.format("合計(%s)", old品名);//大分類合計
                cell.setCellValue((String) value);
                cell = row1.createCell(8);//I列
                value = net_weight_ton_小計;//受託量
                cell.setCellValue(Math.round((Double) value * 100d) / 100d);
                cell.setCellStyle(border_style);
                //単位
                cell = row1.createCell(9);//J列
                cell.setCellValue("t");
                cell.setCellStyle(border_style);

                //処理施設報告(中間)////////////////////////////////////////////////////////
                s = new StringBuilder()
                         .append(" select max(term_year)as term_year, ")
                         .append(" max(ent_id)as ent_id, max(ent_name)as ent_name, ")
                         .append(" max(base_goods_id)as base_goods_id, max(base_goods_name)as base_goods_name, ")
                         .append(" sum(net_weight_ton)as net_weight_ton ")
                         .append(" from(select term_year, ")
                         .append(" case when ent_id = 1 then 1 else 0 end as ent_id,")
                         .append(" case when ent_id = 1 then '自社' else '業' end as ent_name,")
                         .append(" base_goods_id,base_goods_name, net_weight_ton ")
                         .append(" from v1_yearly_data_00_yamagata where term_year = ? ")
                         .append(" and cont_id in (1)  and base_goods_id > 0 and goods_option = 0)a ")
                         .append(" group by a.term_year, a.base_goods_id, a.ent_id ")
                         .append(" order by a.term_year, a.base_goods_id, a.ent_id; ")
                         .toString();

                ps = con.prepareStatement(s);
                ps.setObject(1, term_date);
                rs = ps.executeQuery();
                /*
                1	"燃え殻"4
                2	"汚泥"6
                3	"廃油"8
                4	"廃酸"9
                5	"廃アルカリ"10
                6	"廃プラスチック"11
                7	"紙くず"12
                8	"木くず"13
                9	"繊維くず"14
                10	"動物性残さ"15
                11	"動物系固形不要物"16
                12	"ゴムくず"17
                13	"金属くず"18
                14	"ガラスくず等"19
                15	"鉱さい"20
                16	"がれき類"21
                17	"動物のふん尿"22×
                18	"動物の死体"23×
                19	"ばいじん"24
                20	"第１３号廃棄物"×
                21	"自動車等破砕物"
                22	"石綿含有産業廃棄物"26～29
                23	"水銀使用製品産業廃棄物"30～33
                24	"水銀含有ばいじん等"34～39
                 */
                int cell_no = 4;
                //データ数分まわす
                while (rs.next()) {//1:燃えがら～25:水銀使用
                    if (rs.getInt("ent_id") == 0) {
                        row1 = sheet1.getRow(16);//17行目
                    } else {
                        row1 = sheet1.getRow(17);//18行目
                    }
                    switch (rs.getInt("base_goods_id")) {
                        case 1:
                            cell_no = 4;
                            break;
                        case 2:
                            cell_no = 6;
                            break;
                        case 3:
                            cell_no = 8;
                            break;
                        default:
                            cell_no = rs.getInt("base_goods_id") + 5;
                            break;
                    }
                    cell = row1.getCell(cell_no);//列
                    value = rs.getDouble("net_weight_ton");//受託量 
                    cell.setCellValue((Double) value);

                }

                //処理施設報告(最終)////////////////////////////////////////////////////////
                s = new StringBuilder()
                         .append(" select max(term_year)as term_year, ")
                         .append(" max(ent_id)as ent_id, max(ent_name)as ent_name, ")
                         .append(" max(base_goods_id)as base_goods_id, max(base_goods_name)as base_goods_name, ")
                         .append(" sum(net_weight_ton)as net_weight_ton ")
                         .append(" from(select term_year, ")
                         .append(" case when ent_id = 1 then 1 else 0 end as ent_id,")
                         .append(" case when ent_id = 1 then '自社' else '業' end as ent_name,")
                         .append(" base_goods_id,base_goods_name, net_weight_ton ")
                         .append(" from v1_yearly_data_00_yamagata where term_year = ? ")
                         .append(" and cont_id in (1)  and base_goods_id > 0 and goods_option = 1)a ")
                         .append(" group by a.term_year, a.base_goods_id, a.ent_id ")
                         .append(" order by a.term_year, a.base_goods_id, a.ent_id; ")
                         .toString();

                ps = con.prepareStatement(s);
                ps.setObject(1, term_date);
                rs = ps.executeQuery();
                //データ数分まわす
                while (rs.next()) {
                    if (rs.getInt("base_goods_id") == 1 && rs.getInt("ent_id") == 0) {
                        row1 = sheet1.getRow(16);//17行目
                        cell = row1.getCell(14);//O列
                        value = rs.getDouble("net_weight_ton");//受託量 
                        cell.setCellValue((Double) value);
                    } else if (rs.getInt("base_goods_id") == 1 && rs.getInt("ent_id") == 1) {
                        row1 = sheet1.getRow(17);//18行目
                        cell = row1.getCell(14);//O列
                        value = rs.getDouble("net_weight_ton");//受託量
                        cell.setCellValue((Double) value);
                    } else if (rs.getInt("base_goods_id") == 2 && rs.getInt("ent_id") == 0) {
                        row1 = sheet1.getRow(18);//19行目
                        cell = row1.getCell(21);//V列
                        value = rs.getDouble("net_weight_ton");//受託量
                        cell.setCellValue((Double) value);
                    } else if (rs.getInt("base_goods_id") == 2 && rs.getInt("ent_id") == 1) {
                        row1 = sheet1.getRow(19);//20行目
                        cell = row1.getCell(21);//V列
                        value = rs.getDouble("net_weight_ton");//受託量
                        cell.setCellValue((Double) value);
                    } else if (rs.getInt("base_goods_id") == 3 && rs.getInt("ent_id") == 0) {
                        row1 = sheet1.getRow(20);//21行目
                        cell = row1.getCell(23);//W列
                        value = rs.getDouble("net_weight_ton");//受託量
                        cell.setCellValue((Double) value);
                    } else if (rs.getInt("base_goods_id") == 3 && rs.getInt("ent_id") == 1) {
                        row1 = sheet1.getRow(21);//22行目
                        cell = row1.getCell(23);//W列
                        value = rs.getDouble("net_weight_ton");//受託量
                        cell.setCellValue((Double) value);
                    } else if (rs.getInt("base_goods_id") == 4 && rs.getInt("ent_id") == 0) {
                        row1 = sheet1.getRow(22);//21行目
                        cell = row1.getCell(23);//W列
                        value = rs.getDouble("net_weight_ton");//受託量
                        cell.setCellValue((Double) value);
                    } else if (rs.getInt("base_goods_id") == 4 && rs.getInt("ent_id") == 1) {
                        row1 = sheet1.getRow(23);//22行目
                        cell = row1.getCell(23);//W列
                        value = rs.getDouble("net_weight_ton");//受託量
                        cell.setCellValue((Double) value);
                    }
                }

            } finally {
                ps.close();
            }

            sheet2.setForceFormulaRecalculation(true);
            //印刷シート選択
            sheet2.setSelected(true);

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

    //山形市産業廃棄物処分実績報告書（最終）
    private void printYearlyReport2(
             String file_path,
             String storeFol,
             Connection con,
             Integer term_date) {

        try {
            InputStream in1 = new FileInputStream(file_path);
            wb1 = WorkbookFactory.create(in1);
            initCom();
            Sheet sheet1 = wb1.getSheet("様式b");
            Sheet sheet2 = wb1.getSheet("様式a");

            SimpleDateFormat sdf_MMdd = new SimpleDateFormat("MM月dd日");

            FileOutputStream out1 = null;
            File print_out = null;
            String s報告者情報1 = "";
            String s報告者情報2 = "";

            Row row, row1;
            Cell cell;
            Object value = null;
            String[] printer = getPrinter();
            String sec_ = "";
            PreparedStatement ps_sec;
            ResultSet rs_sec;

            //出力日 
            row = sheet2.getRow(2);//3行目
            cell = row.getCell(21);//V列
            value = String.format("平成%s年",
                     (Calendar.getInstance().get(Calendar.YEAR)) - 1988)
                     + sdf_MMdd.format(Calendar.getInstance().getTime());
            cell.setCellValue((String) value);

            //出力日 
            row = sheet1.getRow(1);//2行目
            cell = row.getCell(13);//N列
            value = String.format("平成%s年",
                     (Calendar.getInstance().get(Calendar.YEAR)) - 1988)
                     + sdf_MMdd.format(Calendar.getInstance().getTime());
            cell.setCellValue((String) value);

            String s0 = new StringBuilder()
                     .append("select * ")
                     .append(" from m_manifesto where sno = 1; ")
                     .toString();

            PreparedStatement ps0 = con.prepareStatement(s0);

            try {
                ResultSet rs0 = ps0.executeQuery();

                if (rs0.next()) {
                    s報告者情報1 = "　　住所　　　　　　" + rs0.getString("trust_name1") + "\r\n"
                             + "　　氏名又は名称　　" + rs0.getString("company_name") + "\r\n"
                             + "　　代表者氏名　　　" + rs0.getString("trust_name2") + "\r\n"
                             + "　　電話番号　　　　" + rs0.getString("trust_name3");
                    s報告者情報2 = "　　許可の種類　　　産業廃棄物　処分業\r\n"
                             + "　　許可の年月日　　" + String.format("平成%s年", DBManager.convert(rs0.getTimestamp("permission_date")).get(Calendar.YEAR) - 1988) + sdf_MMdd.format(rs0.getDate("permission_date")) + "\r\n"
                             + "　　許可番号　　　　" + rs0.getString("permission_no");

                }

            } finally {
                ps0.close();
            }

            //報告者
            row = sheet2.getRow(5);//6行目
            cell = row.getCell(20);//U列
            value = s報告者情報1;
            cell.setCellValue((String) value);

            //許可番号など
            row = sheet2.getRow(11);//12行目
            cell = row.getCell(20);//U列
            value = s報告者情報2;
            cell.setCellValue((String) value);

            //報告者
            row = sheet1.getRow(3);//4行目
            cell = row.getCell(11);//L列
            value = s報告者情報1;
            cell.setCellValue((String) value);

            //（平成 %s 年度）
            row = sheet2.getRow(9);//10行目
            cell = row.getCell(6);//G列
            value = String.format("（平成 %s 年度）", (Calendar.getInstance().get(Calendar.YEAR)) - 1988);
            cell.setCellValue((String) value);

            //（平成 %s 年度）
            row = sheet1.getRow(9);//10行目
            cell = row.getCell(6);//G列
            value = String.format("（平成 %s 年度）", (Calendar.getInstance().get(Calendar.YEAR)) - 1988);
            cell.setCellValue((String) value);

            //自社排出以外は別紙（様式a）に /////////////////////////////////////////////////////////////////////////////////
            String s = new StringBuilder()
                     .append("select * ")
                     .append(" from v1_yearly_data_00_yamagata where term_year = ? ")
                     .append(" and ent_id not in (1) ")//自社排出以外->自社処理のみ
                     .append(" and cont_id in (1) ")
                     .append(" and base_goods_id > 0 ")
                     .append(" and goods_option = 1 ")//最終処分のみ

                     .append(" order by base_goods_id, ent_id, office_id, cont_id, method_name; ")
                     .toString();

            PreparedStatement ps = con.prepareStatement(s);
            ps.setObject(1, term_date);
            try {
                ResultSet rs = ps.executeQuery();

                int row_no = 23;
                int old品名コード = 0;
                String old品名 = "";
                double net_weight_ton_小計 = 0.;

                //データ数分まわす
                while (rs.next()) {
                    if (row_no < 38) {
                        if (old品名コード != 0 && old品名コード != rs.getInt("base_goods_id")) {
                            row1 = sheet2.getRow(row_no) != null ? sheet2.getRow(row_no) : sheet2.createRow(row_no);
                            cell = row1.getCell(1) != null ? row1.getCell(1) : row1.createCell(1);//B列
                            value = String.format("合計(%s)", old品名);//大分類合計
                            cell.setCellValue((String) value);
                            cell = row1.getCell(8) != null ? row1.getCell(8) : row1.createCell(8);//I列
                            value = net_weight_ton_小計;//受託量
                            cell.setCellValue(Math.round((Double) value * 100d) / 100d);
                            //単位
                            cell = row1.getCell(9) != null ? row1.getCell(9) : row1.createCell(9);//J列
                            cell.setCellValue("t");
                            //変数初期化
                            net_weight_ton_小計 = 0.;
                            row_no++;
                        }
                        row1 = sheet2.getRow(row_no) != null ? sheet2.getRow(row_no) : sheet2.createRow(row_no);//24行目スタート
                        cell = row1.getCell(1) != null ? row1.getCell(1) : row1.createCell(1);//B列
                        value = rs.getString("base_goods_name");//大分類名
                        cell.setCellValue((String) value);
                        cell = row1.getCell(4) != null ? row1.getCell(4) : row1.createCell(4);//E列
                        value = rs.getString("ent_name");//排出業者名
                        cell.setCellValue((String) value);
                        cell = row1.getCell(5) != null ? row1.getCell(5) : row1.createCell(5);//F列
                        value = rs.getString("office_zip_code");//現場郵便番号
                        cell.setCellValue((String) value);
                        cell = row1.getCell(6) != null ? row1.getCell(6) : row1.createCell(6);//G列
                        value = rs.getString("office_address1") + rs.getString("office_address2");//現場住所
                        cell.setCellValue((String) value);
                        //受託量
                        cell = row1.getCell(8) != null ? row1.getCell(8) : row1.createCell(8);//I列
                        value = rs.getDouble("net_weight_ton");//受託量
                        net_weight_ton_小計 += rs.getDouble("net_weight_ton");
                        cell.setCellValue((Double) value);
                        //単位
                        cell = row1.getCell(9) != null ? row1.getCell(9) : row1.createCell(9);//J列
                        cell.setCellValue("t");
                        //処分方法　method_name
                        cell = row1.getCell(10) != null ? row1.getCell(10) : row1.createCell(10);//K列
                        value = rs.getString("method_name");//処分方法
                        cell.setCellValue((String) value);
                        //処分量
                        cell = row1.getCell(12) != null ? row1.getCell(12) : row1.createCell(12);//M列
                        value = rs.getDouble("net_weight_ton");//処分量
                        cell.setCellValue((Double) value);
                        //単位
                        cell = row1.getCell(13) != null ? row1.getCell(13) : row1.createCell(13);//N列
                        cell.setCellValue("t");
                        //処分後量
                        cell = row1.getCell(14) != null ? row1.getCell(14) : row1.createCell(14);//O列
                        value = rs.getDouble("net_weight_ton");//処分後量
                        cell.setCellValue((Double) value);
                        //単位
                        cell = row1.getCell(15) != null ? row1.getCell(15) : row1.createCell(15);//P列
                        cell.setCellValue("t");
                        //cont_office_addres
                        cell = row1.getCell(16) != null ? row1.getCell(16) : row1.createCell(16);//Q列
                        value = rs.getString("cont_office_address");//処分場所
                        cell.setCellValue((String) value);

                        //二次マニフェストがあれば
                        if (rs.getString("secod_manifesto_no").length() > 0) {
                            sec_ = new StringBuilder()
                                     .append("select v1_weigh_data.*, ")
                                     .append(" CASE ")
                                     .append(" WHEN v1_weigh_data.method_name <> ''::text AND v1_weigh_data.method_name IS NOT NULL THEN v1_weigh_data.method_name ")
                                     .append(" WHEN v1_weigh_data.check_flag09 = true THEN '脱水'::text ")
                                     .append(" WHEN v1_weigh_data.check_flag10 = true THEN '焼却'::text ")
                                     .append(" WHEN v1_weigh_data.check_flag11 = true THEN '破砕'::text ")
                                     .append(" WHEN v1_weigh_data.check_flag12 = true THEN '安定型'::text ")
                                     .append(" WHEN v1_weigh_data.check_flag13 = true THEN '管理型'::text ")
                                     .append(" WHEN v1_weigh_data.check_flag14 = true THEN '遮断型'::text ")
                                     .append(" ELSE ''::text ")
                                     .append(" END AS sec_method_name, ")
                                     .append(" m_disposal_contractor.permission_no AS sec_permission_no ")
                                     .append(" from v1_weigh_data ")
                                     .append(" LEFT JOIN m_disposal_contractor ON v1_weigh_data.dis_cont_id = m_disposal_contractor.id ")
                                     .append(" where manifesto_no1 = ? ")
                                     .append(" or manifesto_no2 = ? ")
                                     .append(" or manifesto_no3 = ? ;")
                                     .toString();

                            ps_sec = con.prepareStatement(sec_);
                            ps_sec.setObject(1, rs.getString("secod_manifesto_no"));
                            ps_sec.setObject(2, rs.getString("secod_manifesto_no"));
                            ps_sec.setObject(3, rs.getString("secod_manifesto_no"));
                            rs_sec = ps_sec.executeQuery();
                            if (rs_sec.next()) {
                                //処分受託許可番号　permission_no
                                cell = row1.getCell(18) != null ? row1.getCell(18) : row1.createCell(18);//S列
                                value = rs_sec.getString("sec_permission_no");
                                cell.setCellValue((String) value);
                                //処分業者名
                                cell = row1.getCell(19) != null ? row1.getCell(19) : row1.createCell(19);//T列
                                value = rs_sec.getString("cont_p_name");
                                cell.setCellValue((String) value);
                                //処分業者名郵便番号
                                cell = row1.getCell(20) != null ? row1.getCell(20) : row1.createCell(20);//U列
                                value = rs_sec.getString("cont_zip_code");
                                cell.setCellValue((String) value);
                                //処分業者名住所
                                cell = row1.getCell(21) != null ? row1.getCell(21) : row1.createCell(21);//V列
                                value = rs_sec.getString("cont_address1") + rs_sec.getString("cont_address2");
                                cell.setCellValue((String) value);
                                //処分方法　method_name
                                cell = row1.getCell(23) != null ? row1.getCell(23) : row1.createCell(23);//X列
                                value = rs_sec.getString("sec_method_name");//処分方法
                                cell.setCellValue((String) value);
                                //処分量
                                cell = row1.getCell(25) != null ? row1.getCell(25) : row1.createCell(25);//Z列
                                value = rs.getDouble("net_weight_ton");//受託量
                                cell.setCellValue((Double) value + "t");
                                //単位
                                cell = row1.getCell(26) != null ? row1.getCell(26) : row1.createCell(26);//AA列
                                cell.setCellValue("t");

                            }

                        }
                    } else {
                        if (old品名コード != 0 && old品名コード != rs.getInt("base_goods_id")) {
                            row1 = sheet2.createRow(row_no);
                            row1.setHeightInPoints(60);
                            sheet2.addMergedRegion(new CellRangeAddress(row_no, row_no, 1, 2));
                            sheet2.addMergedRegion(new CellRangeAddress(row_no, row_no, 6, 7));
                            sheet2.addMergedRegion(new CellRangeAddress(row_no, row_no, 10, 11));
                            sheet2.addMergedRegion(new CellRangeAddress(row_no, row_no, 16, 17));
                            sheet2.addMergedRegion(new CellRangeAddress(row_no, row_no, 21, 22));
                            sheet2.addMergedRegion(new CellRangeAddress(row_no, row_no, 23, 24));
                            for (int i = 1; i <= 27; i++) {
                                cell = row1.createCell(i);//B列～AB列まで線を引く
                                cell.setCellStyle(border_style);
                            }

                            cell = row1.createCell(1);//B列
                            value = String.format("合計(%s)", old品名);//大分類合計
                            cell.setCellValue((String) value);
                            cell.setCellStyle(border_style);
                            cell = row1.createCell(8);//I列
                            value = net_weight_ton_小計;//受託量
                            cell.setCellValue(Math.round((Double) value * 100d) / 100d);
                            cell.setCellStyle(border_style);
                            //単位
                            cell = row1.createCell(9);//J列
                            cell.setCellValue("t");
                            cell.setCellStyle(border_style);

                            //変数初期化
                            net_weight_ton_小計 = 0.;
                            row_no++;
                        }
                        row1 = sheet2.createRow(row_no);
                        row1.setHeightInPoints(60);
                        sheet2.addMergedRegion(new CellRangeAddress(row_no, row_no, 1, 2));
                        sheet2.addMergedRegion(new CellRangeAddress(row_no, row_no, 6, 7));
                        sheet2.addMergedRegion(new CellRangeAddress(row_no, row_no, 10, 11));
                        sheet2.addMergedRegion(new CellRangeAddress(row_no, row_no, 16, 17));
                        sheet2.addMergedRegion(new CellRangeAddress(row_no, row_no, 21, 22));
                        sheet2.addMergedRegion(new CellRangeAddress(row_no, row_no, 23, 24));
                        for (int i = 1; i <= 27; i++) {
                            cell = row1.createCell(i);//B列～AB列まで線を引く
                            cell.setCellStyle(border_style);
                        }

                        cell = row1.createCell(1);//B列
                        value = rs.getString("base_goods_name");//大分類名
                        cell.setCellValue((String) value);
                        cell.setCellStyle(border_style);
                        cell = row1.createCell(4);//E列
                        value = rs.getString("ent_name");//排出業者名
                        cell.setCellValue((String) value);
                        cell.setCellStyle(border_style);
                        cell = row1.createCell(5);//F列
                        value = rs.getString("office_zip_code");//現場郵便番号
                        cell.setCellValue((String) value);
                        cell.setCellStyle(border_style);
                        cell = row1.createCell(6);//G列
                        value = rs.getString("office_address1") + rs.getString("office_address2");//現場住所
                        cell.setCellValue((String) value);
                        cell.setCellStyle(border_style);
                        //受託量
                        cell = row1.createCell(8);//I列
                        value = rs.getDouble("net_weight_ton");//受託量
                        net_weight_ton_小計 += rs.getDouble("net_weight_ton");
                        cell.setCellValue((Double) value);
                        cell.setCellStyle(border_style);
                        //単位
                        cell = row1.createCell(9);//J列
                        cell.setCellValue("t");
                        cell.setCellStyle(border_style);
                        //処分方法　method_name
                        cell = row1.createCell(10);//K列
                        value = rs.getString("method_name");//処分方法
                        cell.setCellValue((String) value);
                        cell.setCellStyle(border_style);
                        //処分量
                        cell = row1.createCell(12);//M列
                        value = rs.getDouble("net_weight_ton");//処分量
                        cell.setCellValue((Double) value);
                        cell.setCellStyle(border_style);
                        //単位
                        cell = row1.createCell(13);//N列
                        cell.setCellValue("t");
                        cell.setCellStyle(border_style);
                        //処分後量
                        cell = row1.createCell(14);//O列
                        value = rs.getDouble("net_weight_ton");//処分後量
                        cell.setCellValue((Double) value);
                        cell.setCellStyle(border_style);
                        //単位
                        cell = row1.createCell(15);//P列
                        cell.setCellValue("t");
                        cell.setCellStyle(border_style);
                        //cont_office_addres
                        cell = row1.createCell(16);//Q列
                        value = rs.getString("cont_office_address");//処分場所
                        cell.setCellValue((String) value);
                        cell.setCellStyle(border_style);

                        //二次マニフェストがあれば
                        if (rs.getString("secod_manifesto_no").length() > 0) {
                            sec_ = new StringBuilder()
                                     .append("select v1_weigh_data.*, ")
                                     .append(" CASE ")
                                     .append(" WHEN v1_weigh_data.method_name <> ''::text AND v1_weigh_data.method_name IS NOT NULL THEN v1_weigh_data.method_name ")
                                     .append(" WHEN v1_weigh_data.check_flag09 = true THEN '脱水'::text ")
                                     .append(" WHEN v1_weigh_data.check_flag10 = true THEN '焼却'::text ")
                                     .append(" WHEN v1_weigh_data.check_flag11 = true THEN '破砕'::text ")
                                     .append(" WHEN v1_weigh_data.check_flag12 = true THEN '安定型'::text ")
                                     .append(" WHEN v1_weigh_data.check_flag13 = true THEN '管理型'::text ")
                                     .append(" WHEN v1_weigh_data.check_flag14 = true THEN '遮断型'::text ")
                                     .append(" ELSE ''::text ")
                                     .append(" END AS sec_method_name, ")
                                     .append(" m_disposal_contractor.permission_no AS sec_permission_no ")
                                     .append(" from v1_weigh_data ")
                                     .append(" LEFT JOIN m_disposal_contractor ON v1_weigh_data.dis_cont_id = m_disposal_contractor.id ")
                                     .append(" where manifesto_no1 = ? ")
                                     .append(" or manifesto_no2 = ? ")
                                     .append(" or manifesto_no3 = ? ;")
                                     .toString();

                            ps_sec = con.prepareStatement(sec_);
                            ps_sec.setObject(1, rs.getString("secod_manifesto_no"));
                            ps_sec.setObject(2, rs.getString("secod_manifesto_no"));
                            ps_sec.setObject(3, rs.getString("secod_manifesto_no"));
                            rs_sec = ps_sec.executeQuery();
                            if (rs_sec.next()) {
                                //処分受託許可番号　permission_no
                                cell = row1.createCell(18);//S列
                                value = rs_sec.getString("sec_permission_no");
                                cell.setCellValue((String) value);
                                cell.setCellStyle(border_style);
                                //処分業者名
                                cell = row1.createCell(19);//T列
                                value = rs_sec.getString("cont_p_name");
                                cell.setCellValue((String) value);
                                cell.setCellStyle(border_style);
                                //処分業者名郵便番号
                                cell = row1.createCell(20);//U列
                                value = rs_sec.getString("cont_zip_code");
                                cell.setCellValue((String) value);
                                cell.setCellStyle(border_style);
                                //処分業者名住所
                                cell = row1.createCell(21);//V列
                                value = rs_sec.getString("cont_address1") + rs_sec.getString("cont_address2");
                                cell.setCellValue((String) value);
                                cell.setCellStyle(border_style);
                                //処分方法　method_name
                                cell = row1.createCell(23);//X列
                                value = rs_sec.getString("sec_method_name");//処分方法
                                cell.setCellValue((String) value);
                                cell.setCellStyle(border_style);
                                //処分量
                                cell = row1.createCell(25);//Z列
                                value = rs.getDouble("net_weight_ton");//受託量
                                cell.setCellValue((Double) value + "t");
                                cell.setCellStyle(border_style);
                                //単位
                                cell = row1.createCell(26);//AA列
                                cell.setCellValue("t");
                                cell.setCellStyle(border_style);

                            }
                        }
                    }

                    // 品名ごとに小計行を入れる
                    old品名コード = rs.getInt("base_goods_id");
                    old品名 = rs.getString("base_goods_name");
                    row_no++;
                }

                //最終品目の小計
                row1 = sheet2.createRow(row_no);
                row1.setHeightInPoints(60);
                sheet2.addMergedRegion(new CellRangeAddress(row_no, row_no, 1, 2));
                sheet2.addMergedRegion(new CellRangeAddress(row_no, row_no, 6, 7));
                sheet2.addMergedRegion(new CellRangeAddress(row_no, row_no, 10, 11));
                sheet2.addMergedRegion(new CellRangeAddress(row_no, row_no, 16, 17));
                sheet2.addMergedRegion(new CellRangeAddress(row_no, row_no, 21, 22));
                sheet2.addMergedRegion(new CellRangeAddress(row_no, row_no, 23, 24));
                for (int i = 1; i <= 27; i++) {
                    cell = row1.createCell(i);//B列～AB列まで線を引く
                    cell.setCellStyle(border_style);
                }
                cell = row1.createCell(1);//B列
                cell.setCellStyle(border_style);

                value = String.format("合計(%s)", old品名);//大分類合計
                cell.setCellValue((String) value);
                cell = row1.createCell(8);//I列
                value = net_weight_ton_小計;//受託量
                cell.setCellValue(Math.round((Double) value * 100d) / 100d);
                cell.setCellStyle(border_style);
                //単位
                cell = row1.createCell(9);//J列
                cell.setCellValue("t");
                cell.setCellStyle(border_style);

                //処理施設報告
                s = new StringBuilder()
                         .append(" select max(term_year)as term_year, ")
                         .append(" max(ent_id)as ent_id, max(ent_name)as ent_name, ")
                         .append(" max(base_goods_id)as base_goods_id, max(base_goods_name)as base_goods_name, ")
                         .append(" sum(net_weight_ton)as net_weight_ton ")
                         .append(" from(select term_year, ")
                         .append(" case when ent_id = 1 then 1 else 0 end as ent_id,")
                         .append(" case when ent_id = 1 then '自社' else '業' end as ent_name,")
                         .append(" base_goods_id,base_goods_name, net_weight_ton ")
                         .append(" from v1_yearly_data_00_yamagata where term_year = ? ")
                         .append(" and cont_id in (1)  and base_goods_id > 0 and goods_option = 1 )a ")
                         .append(" group by a.term_year, a.base_goods_id, a.ent_id ")
                         .append(" order by a.term_year, a.base_goods_id, a.ent_id; ")
                         .toString();

                ps = con.prepareStatement(s);
                ps.setObject(1, term_date);
                rs = ps.executeQuery();
                //データ数分まわす
                while (rs.next()) {
                    if (rs.getInt("base_goods_id") == 1 && rs.getInt("ent_id") == 0) {
                        row1 = sheet1.getRow(16);//17行目
                        cell = row1.getCell(14);//O列
                        value = rs.getDouble("net_weight_ton");//受託量 
                        cell.setCellValue((Double) value);
                    } else if (rs.getInt("base_goods_id") == 1 && rs.getInt("ent_id") == 1) {
                        row1 = sheet1.getRow(17);//18行目
                        cell = row1.getCell(14);//O列
                        value = rs.getDouble("net_weight_ton");//受託量
                        cell.setCellValue((Double) value);
                    } else if (rs.getInt("base_goods_id") == 2 && rs.getInt("ent_id") == 0) {
                        row1 = sheet1.getRow(18);//19行目
                        cell = row1.getCell(21);//V列
                        value = rs.getDouble("net_weight_ton");//受託量
                        cell.setCellValue((Double) value);
                    } else if (rs.getInt("base_goods_id") == 2 && rs.getInt("ent_id") == 1) {
                        row1 = sheet1.getRow(19);//20行目
                        cell = row1.getCell(21);//V列
                        value = rs.getDouble("net_weight_ton");//受託量
                        cell.setCellValue((Double) value);
                    } else if (rs.getInt("base_goods_id") == 3 && rs.getInt("ent_id") == 0) {
                        row1 = sheet1.getRow(20);//21行目
                        cell = row1.getCell(23);//W列
                        value = rs.getDouble("net_weight_ton");//受託量
                        cell.setCellValue((Double) value);
                    } else if (rs.getInt("base_goods_id") == 3 && rs.getInt("ent_id") == 1) {
                        row1 = sheet1.getRow(21);//22行目
                        cell = row1.getCell(23);//W列
                        value = rs.getDouble("net_weight_ton");//受託量
                        cell.setCellValue((Double) value);
                    }
                }

            } finally {
                ps.close();
            }

            sheet2.setForceFormulaRecalculation(true);
            //印刷シート選択
            sheet2.setSelected(true);

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

    //山形市産業廃棄物 運搬実績報告書
    private void printYearlyReport3(
             String file_path,
             String storeFol,
             Connection con,
             Integer term_date) {

        try {
            InputStream in1 = new FileInputStream(file_path);
            wb1 = WorkbookFactory.create(in1);
            initCom();
            Sheet sheet2 = wb1.getSheet("様式a");

            SimpleDateFormat sdf_MMdd = new SimpleDateFormat("MM月dd日");

            FileOutputStream out1 = null;
            File print_out = null;
            Row row, row1;
            Cell cell;
            Object value = null;
            String[] printer = getPrinter();

            String s報告者情報1 = "";
            String s報告者情報2 = "";

            //出力日 
            row = sheet2.getRow(2);//3行目
            cell = row.getCell(17);//R列
            value = String.format("平成%s年",
                     (Calendar.getInstance().get(Calendar.YEAR)) - 1988)
                     + sdf_MMdd.format(Calendar.getInstance().getTime());
            cell.setCellValue((String) value);

            String s0 = new StringBuilder()
                     .append("select * ")
                     .append(" from m_manifesto where sno = 1; ")
                     .toString();

            PreparedStatement ps0 = con.prepareStatement(s0);

            try {
                ResultSet rs0 = ps0.executeQuery();

                if (rs0.next()) {
                    s報告者情報1 = "　　住所　　　　　　" + rs0.getString("trust_name1") + "\r\n"
                             + "　　氏名又は名称　　" + rs0.getString("company_name") + "\r\n"
                             + "　　代表者氏名　　　" + rs0.getString("trust_name2") + "\r\n"
                             + "　　電話番号　　　　" + rs0.getString("trust_name3");
                    s報告者情報2 = "　　許可の種類　　　産業廃棄物　収集運搬業\r\n"
                             + "　　許可の年月日　　" + String.format("平成%s年", DBManager.convert(rs0.getTimestamp("permission_date")).get(Calendar.YEAR) - 1988) + sdf_MMdd.format(rs0.getDate("permission_date")) + "\r\n"
                             + "　　許可番号　　　　" + rs0.getString("permission_no");

                }

            } finally {
                ps0.close();
            }

            //報告者
            row = sheet2.getRow(5);//6行目
            cell = row.getCell(16);//Q列
            value = s報告者情報1;
            cell.setCellValue((String) value);

            //許可番号など
            row = sheet2.getRow(10);//12行目
            cell = row.getCell(16);//Q列
            value = s報告者情報2;
            cell.setCellValue((String) value);

            row = sheet2.getRow(9);//10行目
            cell = row.getCell(7);//H列
            value = String.format("（平成 %s 年度）", (Calendar.getInstance().get(Calendar.YEAR)) - 1988);
            cell.setCellValue((String) value);

            //自社排出以外は別紙（様式a）に /////////////////////////////////////////////////////////////////////////////////
            String s = new StringBuilder()
                     .append("select * ")
                     .append(" from v1_yearly_data_00_yamagata where term_year = ? ")
                     .append(" and ent_id not in (1) ")//自社排出以外->自社処理のみ
                     .append(" and trans_id in (1) ")
                     .append(" and base_goods_id > 0 ")
                     .append(" order by base_goods_id, ent_id, office_id, cont_id, method_name; ")
                     .toString();

            PreparedStatement ps = con.prepareStatement(s);
            ps.setObject(1, term_date);
            try {
                ResultSet rs = ps.executeQuery();

                int row_no = 23;
                int old品名コード = 0;
                String old品名 = "";
                double net_weight_ton_小計 = 0.;

                //データ数分まわす
                while (rs.next()) {
                    if (row_no < 36) {
                        if (old品名コード != 0 && old品名コード != rs.getInt("base_goods_id")) {
                            row1 = sheet2.getRow(row_no) != null ? sheet2.getRow(row_no) : sheet2.createRow(row_no);
                            row1.setHeightInPoints(60);
                            //セル結合////////////////////////////////////////////////////////////////
                            sheet2.addMergedRegion(new CellRangeAddress(row_no, row_no, 1, 2));//B-C
                            sheet2.addMergedRegion(new CellRangeAddress(row_no, row_no, 6, 7));//G-H
                            sheet2.addMergedRegion(new CellRangeAddress(row_no, row_no, 12, 13));//M-N
                            sheet2.addMergedRegion(new CellRangeAddress(row_no, row_no, 19, 20));//T-U
                            for (int i = 1; i <= 23; i++) {
                                cell = row1.createCell(i);//B列～X列まで線を引く
                                cell.setCellStyle(border_style);
                            }
                            cell = row1.getCell(1) != null ? row1.getCell(1) : row1.createCell(1);//B列
                            value = String.format("合計(%s)", old品名);//大分類合計
                            cell.setCellValue((String) value);
                            cell.setCellStyle(border_style);
                            cell = row1.getCell(8) != null ? row1.getCell(8) : row1.createCell(8);//I列
                            value = net_weight_ton_小計;//受託量
                            cell.setCellValue(Math.round((Double) value * 100d) / 100d);
                            cell.setCellStyle(border_style);
                            //単位
                            cell = row1.getCell(9) != null ? row1.getCell(9) : row1.createCell(9);//J列
                            cell.setCellValue("t");
                            cell.setCellStyle(border_style);
                            //変数初期化
                            net_weight_ton_小計 = 0.;
                            row_no++;
                        }
                        row1 = sheet2.getRow(row_no) != null ? sheet2.getRow(row_no) : sheet2.createRow(row_no);
                        row1.setHeightInPoints(60);
                        //セル結合////////////////////////////////////////////////////////////////
                        sheet2.addMergedRegion(new CellRangeAddress(row_no, row_no, 1, 2));//B-C
                        sheet2.addMergedRegion(new CellRangeAddress(row_no, row_no, 6, 7));//G-H
                        sheet2.addMergedRegion(new CellRangeAddress(row_no, row_no, 12, 13));//M-N
                        sheet2.addMergedRegion(new CellRangeAddress(row_no, row_no, 19, 20));//T-U
                        for (int i = 1; i <= 23; i++) {
                            cell = row1.createCell(i);//B列～X列まで線を引く
                            cell.setCellStyle(border_style);
                        }
                        cell = row1.getCell(1) != null ? row1.getCell(1) : row1.createCell(1);//B列
                        value = rs.getString("base_goods_name");//大分類名
                        cell.setCellValue((String) value);
                        cell.setCellStyle(border_style);
                        cell = row1.getCell(4) != null ? row1.getCell(4) : row1.createCell(4);//E列
                        value = rs.getString("ent_name");//排出業者名
                        cell.setCellValue((String) value);
                        cell.setCellStyle(border_style);
                        cell = row1.getCell(5) != null ? row1.getCell(5) : row1.createCell(5);//F列
                        value = rs.getString("office_zip_code");//現場郵便番号
                        cell.setCellValue((String) value);
                        cell.setCellStyle(border_style);
                        cell = row1.getCell(6) != null ? row1.getCell(6) : row1.createCell(6);//G列
                        value = rs.getString("office_address1") + rs.getString("office_address2");//現場住所
                        cell.setCellValue((String) value);
                        cell.setCellStyle(border_style);
                        //受託量
                        cell = row1.getCell(8) != null ? row1.getCell(8) : row1.createCell(8);//I列
                        value = rs.getDouble("net_weight_ton");//受託量
                        net_weight_ton_小計 += rs.getDouble("net_weight_ton");
                        cell.setCellValue((Double) value);
                        cell.setCellStyle(border_style);
                        //単位
                        cell = row1.getCell(9) != null ? row1.getCell(9) : row1.createCell(9);//J列
                        cell.setCellValue("t");
                        cell.setCellStyle(border_style);
                        //運搬先（処分場名称）
                        cell = row1.getCell(10) != null ? row1.getCell(10) : row1.createCell(10);//K列
                        value = rs.getString("cont_office_name");//運搬先（処分場名称）
                        cell.setCellValue((String) value);
                        cell.setCellStyle(border_style);
                        //運搬先（郵便番号）
                        cell = row1.getCell(11) != null ? row1.getCell(11) : row1.createCell(11);//L列
                        value = rs.getString("cont_office_zip_code");//運搬先（郵便番号）
                        cell.setCellValue((String) value);
                        cell.setCellStyle(border_style);
                        //処分場所
                        cell = row1.getCell(12) != null ? row1.getCell(12) : row1.createCell(12);//M列
                        value = rs.getString("cont_office_address");//処分場所
                        cell.setCellValue((String) value);
                        cell.setCellStyle(border_style);
                        //処分後量
                        cell = row1.getCell(14) != null ? row1.getCell(14) : row1.createCell(14);//O列
                        value = rs.getDouble("net_weight_ton");//処分後量
                        cell.setCellValue((Double) value);
                        cell.setCellStyle(border_style);
                        //単位
                        cell = row1.getCell(15) != null ? row1.getCell(15) : row1.createCell(15);//P列
                        cell.setCellValue("t");
                        cell.setCellStyle(border_style);
                        //許可番号
                        cell = row1.getCell(16) != null ? row1.getCell(16) : row1.createCell(16);//Q列
                        value = rs.getString("permission_no");//許可番号
                        cell.setCellValue((String) value);
                        cell.setCellStyle(border_style);
                        //処分業者名
                        cell = row1.getCell(17) != null ? row1.getCell(17) : row1.createCell(17);//R列
                        value = rs.getString("cont_p_name");
                        cell.setCellValue((String) value);
                        cell.setCellStyle(border_style);
                        //処分業者名郵便番号
                        cell = row1.getCell(18) != null ? row1.getCell(18) : row1.createCell(18);//S列
                        value = rs.getString("cont_zip_code");
                        cell.setCellValue((String) value);
                        cell.setCellStyle(border_style);
                        //処分業者名住所
                        cell = row1.getCell(19) != null ? row1.getCell(19) : row1.createCell(19);//T列
                        value = rs.getString("cont_address1") + rs.getString("cont_address2");
                        cell.setCellValue((String) value);
                        cell.setCellStyle(border_style);
                        //処分量
                        cell = row1.getCell(21) != null ? row1.getCell(21) : row1.createCell(21);//V列
                        value = rs.getDouble("net_weight_ton");//受託量
                        cell.setCellValue((Double) value + "t");
                        cell.setCellStyle(border_style);
                        //単位
                        cell = row1.getCell(22) != null ? row1.getCell(22) : row1.createCell(22);//W列
                        cell.setCellValue("t");
                        cell.setCellStyle(border_style);

                    } else {
                        if (old品名コード != 0 && old品名コード != rs.getInt("base_goods_id")) {
                            row1 = sheet2.createRow(row_no);
                            row1.setHeightInPoints(60);
                            //セル結合////////////////////////////////////////////////////////////////
                            sheet2.addMergedRegion(new CellRangeAddress(row_no, row_no, 1, 2));//B-C
                            sheet2.addMergedRegion(new CellRangeAddress(row_no, row_no, 6, 7));//G-H
                            sheet2.addMergedRegion(new CellRangeAddress(row_no, row_no, 12, 13));//M-N
                            sheet2.addMergedRegion(new CellRangeAddress(row_no, row_no, 19, 20));//T-U
                            for (int i = 1; i <= 23; i++) {
                                cell = row1.createCell(i);//B列～X列まで線を引く
                                cell.setCellStyle(border_style);
                            }

                            cell = row1.createCell(1);//B列
                            value = String.format("合計(%s)", old品名);//大分類合計
                            cell.setCellValue((String) value);
                            cell.setCellStyle(border_style);
                            cell = row1.createCell(8);//I列
                            value = net_weight_ton_小計;//受託量
                            cell.setCellValue(Math.round((Double) value * 100d) / 100d);
                            cell.setCellStyle(border_style);
                            //単位
                            cell = row1.createCell(9);//J列
                            cell.setCellValue("t");
                            cell.setCellStyle(border_style);

                            //変数初期化
                            net_weight_ton_小計 = 0.;
                            row_no++;
                        }
                        row1 = sheet2.createRow(row_no);
                        row1.setHeightInPoints(60);
                        //セル結合////////////////////////////////////////////////////////////////
                        sheet2.addMergedRegion(new CellRangeAddress(row_no, row_no, 1, 2));//B-C
                        sheet2.addMergedRegion(new CellRangeAddress(row_no, row_no, 6, 7));//G-H
                        sheet2.addMergedRegion(new CellRangeAddress(row_no, row_no, 12, 13));//M-N
                        sheet2.addMergedRegion(new CellRangeAddress(row_no, row_no, 19, 20));//T-U
                        for (int i = 1; i <= 23; i++) {
                            cell = row1.createCell(i);//B列～X列まで線を引く
                            cell.setCellStyle(border_style);
                        }

                        row1 = sheet2.getRow(row_no);//24行目スタート
                        cell = row1.getCell(1);//B列
                        value = rs.getString("base_goods_name");//大分類名
                        cell.setCellValue((String) value);
                        cell.setCellStyle(border_style);
                        cell = row1.getCell(4);//E列
                        value = rs.getString("ent_name");//排出業者名
                        cell.setCellValue((String) value);
                        cell.setCellStyle(border_style);
                        cell = row1.getCell(5);//F列
                        value = rs.getString("office_zip_code");//現場郵便番号
                        cell.setCellValue((String) value);
                        cell.setCellStyle(border_style);
                        cell = row1.getCell(6);//G列
                        value = rs.getString("office_address1") + rs.getString("office_address2");//現場住所
                        cell.setCellValue((String) value);
                        cell.setCellStyle(border_style);
                        //受託量
                        cell = row1.getCell(8);//I列
                        value = rs.getDouble("net_weight_ton");//受託量
                        net_weight_ton_小計 += rs.getDouble("net_weight_ton");
                        cell.setCellValue((Double) value);
                        cell.setCellStyle(border_style);
                        //単位
                        cell = row1.getCell(9);//J列
                        cell.setCellValue("t");
                        cell.setCellStyle(border_style);
                        //運搬先（処分場名称）
                        cell = row1.getCell(10);//K列
                        value = rs.getString("cont_office_name");//運搬先（処分場名称）
                        cell.setCellValue((String) value);
                        cell.setCellStyle(border_style);
                        //運搬先（郵便番号）
                        cell = row1.getCell(11);//L列
                        value = rs.getString("cont_office_zip_code");//運搬先（郵便番号）
                        cell.setCellValue((String) value);
                        cell.setCellStyle(border_style);
                        //処分場所
                        cell = row1.getCell(12);//M列
                        value = rs.getString("cont_office_address");//処分場所
                        cell.setCellValue((String) value);
                        cell.setCellStyle(border_style);
                        //処分後量
                        cell = row1.getCell(14);//O列
                        value = rs.getDouble("net_weight_ton");//処分後量
                        cell.setCellValue((Double) value);
                        cell.setCellStyle(border_style);
                        //単位
                        cell = row1.getCell(15);//P列
                        cell.setCellValue("t");
                        cell.setCellStyle(border_style);
                        //許可番号
                        cell = row1.getCell(16);//Q列
                        value = rs.getString("permission_no");//許可番号
                        cell.setCellValue((String) value);
                        cell.setCellStyle(border_style);
                        //処分業者名
                        cell = row1.getCell(17);//R列
                        value = rs.getString("cont_p_name");
                        cell.setCellValue((String) value);
                        cell.setCellStyle(border_style);
                        //処分業者名郵便番号
                        cell = row1.getCell(18);//S列
                        value = rs.getString("cont_zip_code");
                        cell.setCellValue((String) value);
                        cell.setCellStyle(border_style);
                        //処分業者名住所
                        cell = row1.getCell(19);//T列
                        value = rs.getString("cont_address1") + rs.getString("cont_address2");
                        cell.setCellValue((String) value);
                        cell.setCellStyle(border_style);
                        //処分量
                        cell = row1.getCell(21);//V列
                        value = rs.getDouble("net_weight_ton");//受託量
                        cell.setCellValue((Double) value + "t");
                        cell.setCellStyle(border_style);
                        //単位
                        cell = row1.getCell(22);//W列
                        cell.setCellValue("t");
                        cell.setCellStyle(border_style);

                    }

                    // 品名ごとに小計行を入れる
                    old品名コード = rs.getInt("base_goods_id");
                    old品名 = rs.getString("base_goods_name");
                    row_no++;

                }

                //最終品目の小計
                row1 = sheet2.createRow(row_no);
                row1.setHeightInPoints(60);
                //セル結合////////////////////////////////////////////////////////////////
                sheet2.addMergedRegion(new CellRangeAddress(row_no, row_no, 1, 2));//B-C
                sheet2.addMergedRegion(new CellRangeAddress(row_no, row_no, 6, 7));//G-H
                sheet2.addMergedRegion(new CellRangeAddress(row_no, row_no, 12, 13));//M-N
                sheet2.addMergedRegion(new CellRangeAddress(row_no, row_no, 19, 20));//T-U
                for (int i = 1; i <= 23; i++) {
                    cell = row1.createCell(i);//B列～X列まで線を引く
                    cell.setCellStyle(border_style);
                }
                cell = row1.createCell(1);//B列
                cell.setCellStyle(border_style);

                value = String.format("合計(%s)", old品名);//大分類合計
                cell.setCellValue((String) value);
                cell = row1.createCell(8);//I列
                value = net_weight_ton_小計;//受託量
                cell.setCellValue(Math.round((Double) value * 100d) / 100d);
                cell.setCellStyle(border_style);
                //単位
                cell = row1.createCell(9);//J列
                cell.setCellValue("t");
                cell.setCellStyle(border_style);

            } finally {
                ps.close();
            }

            sheet2.setForceFormulaRecalculation(true);
            //印刷シート選択
            sheet2.setSelected(true);

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

    //新庄市産業廃棄物管理表交付状況報告書
    private void printYearlyReport4(
             String file_path,
             String storeFol,
             Connection con,
             Integer term_date,
             int ent_id) {

        try {
            InputStream in1 = new FileInputStream(file_path);
            wb1 = WorkbookFactory.create(in1);
            initCom();
            Sheet sheet2 = wb1.getSheet("様式a");

            FileOutputStream out1 = null;
            File print_out = null;

            Row row, row1;
            Cell cell;
            Object value = null;

            int v現在ページcnt = 0;
            int v現在レコードcnt = 0;
            int v現在明細行 = 4;
            String[] printer = getPrinter();

            String s報告者住所 = "";
            String s氏名又は名称 = "";
            String s報告者代表者氏名 = "";
            String s報告者電話番号 = "";

            String s0 = new StringBuilder()
                     .append("select * ")
                     .append(" from m_manifesto where sno = 1; ")
                     .toString();

            PreparedStatement ps0 = con.prepareStatement(s0);

            try {
                ResultSet rs0 = ps0.executeQuery();

                if (rs0.next()) {
                    s報告者住所 = rs0.getString("trust_name1");
                    s氏名又は名称 = rs0.getString("company_name");
                    s報告者代表者氏名 = rs0.getString("trust_name2");
                    s報告者電話番号 = rs0.getString("trust_name3");
                }

            } finally {
                ps0.close();
            }

            String s = new StringBuilder()
                     .append("select ")
                     .append(" max(office_name) as office_name, ")
                     .append(" max(office_address1)as office_address1, ")
                     .append(" max(office_address2)as office_address2 ")
                     .append(" from v1_yearly_data_01_yamagata where term_year = ? ")
                     .append(" and ent_id in (?) ")
                     .append(" group by office_name, office_address1 ")
                     .append(" order by office_name, office_address1; ")
                     .toString();
            PreparedStatement ps = con.prepareStatement(s);
            try {
                ps.setObject(1, term_date);
                //排出業者指定　ent_id
                ps.setObject(2, ent_id);
                ResultSet rs = ps.executeQuery();

                while (rs.next()) {
                    wb1.cloneSheet(0);
                    sheet2 = wb1.getSheetAt(wb1.getNumberOfSheets() - 1);
                    //排出業者情報取得/(他者排出分)///////////////////////////////////////////////
                    if (ent_id != 1) {
                        String s_ent = new StringBuilder()
                                 .append("select * from v0_ent where id = ? ;")
                                 .toString();
                        PreparedStatement ps_ent = con.prepareStatement(s_ent);
                        ps_ent.setObject(1, ent_id);
                        ResultSet rs_ent = ps_ent.executeQuery();
                        if (rs_ent.next()) {
                            row = sheet2.getRow(8);//9行目
                            cell = row.getCell(8);//I列
                            value = "　" + rs_ent.getString("address1") + rs_ent.getString("address2");
                            cell.setCellValue((String) value);
                            row = sheet2.getRow(9);//10行目
                            cell = row.getCell(8);//I列
                            value = "　" + rs_ent.getString("p_name");
                            cell.setCellValue((String) value);
                            row = sheet2.getRow(10);//11行目
                            cell = row.getCell(8);//I列
                            value = "　";
                            cell.setCellValue((String) value);
                            row = sheet2.getRow(11);//12行目
                            cell = row.getCell(8);//I列
                            value = "　" + rs_ent.getString("tel");
                            cell.setCellValue((String) value);
                        }
                    }
                    ////////////////////////////////////////////////////////////////////////////////////////

                    //タイトル
                    row = sheet2.getRow(2);//3行目
                    cell = row.getCell(5);//F列
                    value = String.format("産業廃棄物管理票交付等状況報告書（平成 %s 年度）", term_date - 1988);
                    cell.setCellValue((String) value);

                    row = sheet2.getRow(12);//13行目
                    cell = row.getCell(1);//B列
                    value = String.format("廃棄物の処理及び清掃に関する法律第12条の3第7項の規定に基づき、平成%s年度の産業廃棄物管理票に関する報告書を提出します。", term_date - 1988);
                    cell.setCellValue((String) value);

                    row = sheet2.getRow(3);//4行目
                    cell = row.getCell(10);//K列
                    value = DBManager.convert((Calendar.getInstance()).getTime());
                    cell.setCellValue((Date) value);

                    //現場名
                    row = sheet2.getRow(14);//15行目
                    cell = row.getCell(4);//E列
                    value = rs.getString("office_name");
                    cell.setCellValue((String) value);
                    //現場住所
                    row = sheet2.getRow(15);//16行目
                    cell = row.getCell(4);//E列
                    value = rs.getString("office_address1") + rs.getString("office_address2");
                    cell.setCellValue((String) value);

                    row = sheet2.getRow(4);//5行目
                    cell = row.getCell(1);//B列
                    value = "山形県知事　　○○　○○　　　　様";
                    cell.setCellValue((String) value);
                    row = sheet2.getRow(8);//9行目
                    cell = row.getCell(8);//I列
                    value = "　" + s報告者住所;
                    cell.setCellValue((String) value);
                    row = sheet2.getRow(9);//10行目
                    cell = row.getCell(8);//I列
                    value = "　" + s氏名又は名称;
                    cell.setCellValue((String) value);
                    row = sheet2.getRow(10);//11行目
                    cell = row.getCell(8);//I列
                    value = "　　代表取締役　" + s報告者代表者氏名;
                    cell.setCellValue((String) value);
                    row = sheet2.getRow(11);//12行目
                    cell = row.getCell(8);//I列
                    value = "　" + s報告者電話番号;
                    cell.setCellValue((String) value);

                    int rs_cnt = 0;

                    //明細ページ
                    String ds = new StringBuilder()
                             .append("select * ")
                             .append(" from v1_yearly_data_01_yamagata where term_year = ? ")
                             .append(" and ent_id in (?) ")
                             .append(" and office_name like ? ")
                             .append(" and office_address1 like ? ")
                             //office_sub_name, office_address1
                             .append(" order by base_goods_id, trans_id, cont_id ; ")
                             .toString();

                    PreparedStatement dps = con.prepareStatement(ds);
                    dps.setObject(1, term_date);
                    //排出業者指定　ent_id
                    dps.setObject(2, ent_id);
                    dps.setObject(3, rs.getString("office_name"));
                    dps.setObject(4, rs.getString("office_address1"));
                    ResultSet drs = dps.executeQuery();

                    while (drs.next()) {
                        //現場ごとの品目別件数を取得
                        rs_cnt++;

                    }
                    //明細ページ数取得
                    double page_cnt = rs_cnt < 4 ? 1 : Math.ceil((rs_cnt - 4) / 10d);

                    drs = dps.executeQuery();
                    v現在レコードcnt = 0;
                    v現在明細行 = 4;
                    while (drs.next()) {
                        //5件目以降は明細ページへ
                        if (v現在レコードcnt < 4) {
                            //印刷範囲
                            wb1.setPrintArea(1 + v現在ページcnt, "$A$1:$M$33");
                            // 印刷用紙指定
                            PrintSetup prints = sheet2.getPrintSetup();
                            prints.setPaperSize(PrintSetup.A4_PAPERSIZE);
                            prints.setLandscape(true);    //横向き

                            row1 = sheet2.getRow(17 + v現在レコードcnt);//18行目スタート
                            cell = row1.getCell(1);//B列
                            value = v現在レコードcnt + 1;//データ件数
                            cell.setCellValue((Integer) value);

                            cell = row1.getCell(2);//C列
                            value = drs.getString("base_goods_name");//大分類名
                            cell.setCellValue((String) value);
                            cell = row1.getCell(3);//D列
                            value = drs.getDouble("net_weight_ton");//排出量
                            cell.setCellValue(Math.round((Double) value * 100d) / 100d + "t");
                            cell = row1.getCell(5);//F列
                            value = drs.getString("cnt") + "枚";//交付枚数
                            cell.setCellValue((String) value);
                            //運搬受託者の許可番号
                            cell = row1.getCell(6);//G列
                            value = drs.getString("trans_permission_no");
                            cell.setCellValue((String) value);
                            //運搬受託者名
                            cell = row1.getCell(7);//H列
                            value = drs.getString("trans_name");//運搬受託者名
                            cell.setCellValue((String) value);
                            //運搬先の住所（処分場住所）
                            cell = row1.getCell(8);//I列
                            value = drs.getString("cont_office_address");//運搬先の住所（処分場住所）
                            cell.setCellValue((String) value);
                            //処分受託者の許可番号
                            cell = row1.getCell(9);//J列
                            value = drs.getString("permission_no");
                            cell.setCellValue((String) value);
                            //処分受託者の名称
                            cell = row1.getCell(10);//K列
                            value = drs.getString("cont_p_name");//処分受託者の名称
                            cell.setCellValue((String) value);

                        } else {
                            Sheet sheet4 = null;
                            if (v現在明細行 == 4) {
                                wb1.cloneSheet(1);
                            }
                            sheet4 = wb1.getSheetAt(wb1.getNumberOfSheets() - 1);

                            if (v現在明細行 == 4) {
                                //印刷範囲を設定するシートのインデックス
                                //印刷範囲
                                wb1.setPrintArea(wb1.getNumberOfSheets() - 1, "$A$1:$L$20");
                                // 印刷用紙指定
                                PrintSetup prints = sheet4.getPrintSetup();
                                prints.setPaperSize(PrintSetup.A4_PAPERSIZE);
                                prints.setLandscape(true);    //横向き
                                //（別紙　　　／　　ページ　※）
                                //v現在レコードcnt
                                row1 = sheet4.getRow(1);//2行目スタート
                                cell = row1.getCell(10);//K列
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

                                //現場名
                                row = sheet4.getRow(2);//3行目
                                cell = row.getCell(3);//D列
                                value = rs.getString("office_name");
                                cell.setCellValue((String) value);
                            }

                            //v現在レコードcnt
                            row1 = sheet4.getRow(v現在明細行);//5行目スタート
                            cell = row1.getCell(1);//B列
                            value = v現在レコードcnt + 1;//データ件数
                            cell.setCellValue((Integer) value);

                            cell = row1.getCell(2);//C列
                            value = drs.getString("base_goods_name");//大分類名
                            cell.setCellValue((String) value);
                            cell = row1.getCell(3);//D列
                            value = drs.getDouble("net_weight_ton");//排出量
                            cell.setCellValue(Math.round((Double) value * 100d) / 100d + "t");
                            cell = row1.getCell(5);//F列
                            value = drs.getString("cnt") + "枚";//交付枚数
                            cell.setCellValue((String) value);

                            //運搬受託者の許可番号
                            cell = row1.getCell(6);//G列
                            value = drs.getString("trans_permission_no");
                            cell.setCellValue((String) value);
                            //運搬受託者名
                            cell = row1.getCell(7);//H列
                            value = drs.getString("trans_name");//運搬受託者名
                            cell.setCellValue((String) value);
                            //運搬先の住所（処分場住所）
                            cell = row1.getCell(8);//I列
                            value = drs.getString("cont_office_address");//運搬先の住所（処分場住所）
                            cell.setCellValue((String) value);
                            //処分受託者の許可番号
                            cell = row1.getCell(9);//J列
                            value = drs.getString("permission_no");
                            cell.setCellValue((String) value);
                            //処分受託者の名称
                            cell = row1.getCell(10);//K列
                            value = drs.getString("cont_p_name");//処分受託者の名称
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
                }

            } finally {
                ps.close();
            }

            sheet2.setForceFormulaRecalculation(true);
            wb1.removeSheetAt(0);
            wb1.removeSheetAt(0);
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
}
