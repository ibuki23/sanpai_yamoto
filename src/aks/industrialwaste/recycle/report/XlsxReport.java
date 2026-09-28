package aks.industrialwaste.recycle.report;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import aks.industrialwaste.database.DBManager;
import static aks.industrialwaste.recycle.report.OSCReport.logger_;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.channels.FileChannel;
import java.sql.Statement;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DataFormat;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.PrintSetup;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class XlsxReport extends OSCReport {

    public static final int REPORT1 = 1;//計量記録一覧
    public static final int REPORT2 = 2;//排出業者別種類別
    public static final int REPORT3 = 3;//運搬業者別種類別
    public static final int REPORT4 = 4;//処分方法別種類別
    public static final int REPORT5 = 5;//マニフェスト管理台帳
    public static final int REPORT6 = 6;//得意先別種類別

    private Connection con_;

    private CellStyle style, style2, cstyle, dstyle, rstyle1, rstyle4, rstyle6,
             style_blank0, style_blank1, style_blank2;
    private Workbook wb1;
    private Font font, font_blank;
    private DataFormat format;

    public XlsxReport() {

        this(REPORT1);
    }

    /**
     * 帳票を印刷するMonthlyReportのインスタンスを生成する.
     *
     * @param reportID 印刷する帳票のID
     */
    public XlsxReport(int reportID) {
        if ((REPORT1 != reportID)
                 && (REPORT2 != reportID)
                 && (REPORT3 != reportID)
                 && (REPORT4 != reportID)
                 && (REPORT5 != reportID)
                 && (REPORT6 != reportID)) {
            throw new IllegalArgumentException("'reportID' is not match.");
        }
    }

    private void initCom() {

        font = wb1.createFont();
        font.setFontName("ＭＳ 明朝");
        font.setFontHeightInPoints((short) 9);

        font_blank = wb1.createFont();
        font_blank.setFontName("ＭＳ 明朝");
        font_blank.setFontHeightInPoints((short) 9);
        font_blank.setColor(IndexedColors.WHITE.getIndex());

        format = wb1.createDataFormat();

        style = wb1.createCellStyle();
        style.setBorderLeft(CellStyle.BORDER_NONE);
        style.setBorderRight(CellStyle.BORDER_NONE);
        style.setBorderTop(CellStyle.BORDER_THIN);
        style.setBorderBottom(CellStyle.BORDER_THIN);
        style.setShrinkToFit(true);
        style.setFont(font);

        //フォントを白にするための１行目スタイル
        //フォント黒 罫線　左右のみ
        style_blank0 = wb1.createCellStyle();
        style_blank0.setBorderLeft(CellStyle.BORDER_NONE);
        style_blank0.setBorderRight(CellStyle.BORDER_NONE);
        style_blank0.setBorderTop(CellStyle.BORDER_THIN);
        style_blank0.setBorderBottom(CellStyle.BORDER_NONE);
        style_blank0.setWrapText(true);//テキストを折り返す
        style_blank0.setFont(font);
        //フォントを白にするための２行目スタイル
        //フォント白 罫線　左右のみ
        style_blank1 = wb1.createCellStyle();
        style_blank1.setBorderLeft(CellStyle.BORDER_NONE);
        style_blank1.setBorderRight(CellStyle.BORDER_NONE);
        style_blank1.setBorderTop(CellStyle.BORDER_NONE);
        style_blank1.setBorderBottom(CellStyle.BORDER_NONE);
        style_blank1.setWrapText(true);//テキストを折り返す
        style_blank1.setFont(font_blank);
        //フォントを白にするための最終行スタイル
        //フォント白 罫線　左右のみ
        style_blank2 = wb1.createCellStyle();
        style_blank2.setBorderLeft(CellStyle.BORDER_NONE);
        style_blank2.setBorderRight(CellStyle.BORDER_NONE);
        style_blank2.setBorderTop(CellStyle.BORDER_THIN);
        style_blank2.setBorderBottom(CellStyle.BORDER_NONE);
        style_blank2.setWrapText(true);//テキストを折り返す
        style_blank2.setFont(font_blank);

        style2 = wb1.createCellStyle();
        style2.setBorderLeft(CellStyle.BORDER_NONE);
        style2.setBorderRight(CellStyle.BORDER_NONE);
        style2.setBorderTop(CellStyle.BORDER_THIN);
        style2.setBorderBottom(CellStyle.BORDER_THIN);
        style2.setAlignment(CellStyle.ALIGN_CENTER);
        //文字の自動縮小
        style.setShrinkToFit(true);
        style2.setFont(font);

        cstyle = wb1.createCellStyle();
        cstyle.setBorderLeft(CellStyle.BORDER_NONE);
        cstyle.setBorderRight(CellStyle.BORDER_NONE);
        cstyle.setBorderTop(CellStyle.BORDER_THIN);
        cstyle.setBorderBottom(CellStyle.BORDER_THIN);
        cstyle.setAlignment(CellStyle.ALIGN_RIGHT);
        cstyle.setDataFormat(format.getFormat("#,##0"));
        cstyle.setFont(font);

        dstyle = wb1.createCellStyle();
        dstyle.setBorderLeft(CellStyle.BORDER_NONE);
        dstyle.setBorderRight(CellStyle.BORDER_NONE);
        dstyle.setBorderTop(CellStyle.BORDER_NONE);
        dstyle.setBorderBottom(CellStyle.BORDER_MEDIUM);
        dstyle.setAlignment(CellStyle.ALIGN_RIGHT);
        dstyle.setDataFormat(format.getFormat("yyyy年MM月"));
        dstyle.setFont(font);

        rstyle1 = wb1.createCellStyle();
        rstyle1.setBorderLeft(CellStyle.BORDER_NONE);
        rstyle1.setBorderRight(CellStyle.BORDER_NONE);
        rstyle1.setBorderTop(CellStyle.BORDER_THIN);
        rstyle1.setBorderBottom(CellStyle.BORDER_THIN);
        rstyle1.setAlignment(CellStyle.ALIGN_RIGHT);
        rstyle1.setDataFormat(format.getFormat("#,##0.00"));
        rstyle1.setFont(font);

        rstyle4 = wb1.createCellStyle();
        rstyle4.setBorderLeft(CellStyle.BORDER_NONE);
        rstyle4.setBorderRight(CellStyle.BORDER_NONE);
        rstyle4.setBorderTop(CellStyle.BORDER_THIN);
        rstyle4.setBorderBottom(CellStyle.BORDER_THIN);
        rstyle4.setAlignment(CellStyle.ALIGN_LEFT);
        rstyle4.setShrinkToFit(true);
        rstyle4.setFont(font);

        rstyle6 = wb1.createCellStyle();
        rstyle6.setBorderLeft(CellStyle.BORDER_NONE);
        rstyle6.setBorderRight(CellStyle.BORDER_NONE);
        rstyle6.setBorderTop(CellStyle.BORDER_THIN);
        rstyle6.setBorderBottom(CellStyle.BORDER_THIN);
        rstyle6.setAlignment(CellStyle.ALIGN_LEFT);
        rstyle6.setWrapText(true);//テキストを折り返す
        rstyle6.setFont(font);

    }

    /**
     * 指定条件で帳票を印刷する.
     *
     * @param con データベースとの接続
     * @param path 保存先ファイルのフルパス
     * @param report_sno 対象の報告
     * @param term_date 対象年
     */
    public int print(Connection con, int report_id, String path, Calendar term_date, Calendar term_date1, int ent_id) {
        con_ = con;
        String template = "";

        try {

            switch (report_id) {
                case REPORT1:
                    //計量記録一覧
                    template = "template\\計量記録一覧.xlsx";
                    break;
                case REPORT2:
                    //業者集計表
                    template = "template\\業者集計表1.xlsx";
                    break;
                case REPORT3:
                    //運搬業者別種類別
                    template = "template\\業者集計表2.xlsx";
                    break;
                case REPORT4:
                    //処分方法別種類別
                    template = "template\\業者集計表3.xlsx";
                    break;
                case REPORT5:
                    //マニフェスト管理台帳
                    template = "template\\マニフェスト管理台帳.xlsx";
                    break;
                case REPORT6:
                    //業者別搬出品目集計表
                    template = "template\\業者集計表4.xlsx";
                    break;
                default:
                    break;
            }

            // テンプレートファイルをローカルにコピー
            File templateFile = new File(template);
            File tmpFile = new File("tmp", templateFile.getName());

            // テンプレートファイルをローカルにコピー
            copyTransfer(templateFile.getPath(), tmpFile.getPath());

            // データ出力
            try {

                switch (report_id) {
                    case REPORT1:
                        //計量記録一覧
                        printReport1(tmpFile.getPath(), path, con_, term_date, term_date1);
                        break;
                    case REPORT2:
                        //業者集計表　排出業者別種類別
                        printReport2(tmpFile.getPath(), path, con_, term_date, term_date1);
                        break;
                    case REPORT3:
                        //業者集計表　運搬業者別種類別
                        printReport3(tmpFile.getPath(), path, con_, term_date, term_date1);
                        break;
                    case REPORT4:
                        //業者集計表　処分場別種類別処分方法別
                        printReport4(tmpFile.getPath(), path, con_, term_date, term_date1);
                        break;
                    case REPORT5:
                        //マニフェスト管理台帳
                        printReport5(tmpFile.getPath(), path, con_, term_date, term_date1, ent_id);
                        break;
                    case REPORT6:
                        //業者別搬出品目集計表
                        printReport6(tmpFile.getPath(), path, con_, term_date, term_date1);
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

// 印刷処理
    public static void doPrintXlsx(String file_path, String printer, int papers, boolean preview) {
        try {
            if (!preview) {
                // 直接印刷：XlsPrint.ps1 に任せる
                runCmd(
                         "powershell.exe",
                         "-NoProfile",
                         "-ExecutionPolicy", "Bypass",
                         "-File", "tmp\\XlsPrint.ps1",
                         "-FilePath", file_path,
                         "-PrinterName", printer,
                         "-PrintCount", String.valueOf(papers)
                );

            } else {
                // プレビュー：Excelで開くだけ
                runCmd(
                         "cmd",
                         "/c",
                         "start",
                         "",
                         file_path
                );
            }

        } catch (InterruptedException ev) {
            System.out.println("失敗(InterruptedException)");
            Thread.currentThread().interrupt();

        } catch (IOException ev) {
            System.out.println("失敗(IOException): " + ev.getMessage());
        }
    }

    public static void runCmd(String... command) throws IOException, InterruptedException {
        ProcessBuilder pb = new ProcessBuilder(command);
        pb.redirectErrorStream(true);

        System.out.println("実行コマンド:");
        for (String s : command) {
            System.out.println("  " + s);
        }

        Process p = pb.start();

        try (java.io.BufferedReader br = new java.io.BufferedReader(
                 new java.io.InputStreamReader(
                          p.getInputStream(),
                          java.nio.charset.Charset.forName("MS932")
                 ))) {

            String line;
            while ((line = br.readLine()) != null) {
                System.out.println(line);
            }
        }

        int exitCode = p.waitFor();

        if (exitCode != 0) {
            throw new IOException("Command failed. exitCode=" + exitCode);
        }
    }

    //計量記録一覧
    private void printReport1(
             String file_path,
             String storeFol,
             Connection con,
             Calendar term_date,
             Calendar term_date1) {

        try {
            InputStream in1 = new FileInputStream(file_path);
            wb1 = WorkbookFactory.create(in1);
            initCom();
            Sheet sheet1 = wb1.getSheet("print");

            SimpleDateFormat sdf00 = new SimpleDateFormat("yyyy年MM月dd日～");
            SimpleDateFormat sdf01 = new SimpleDateFormat("yyyy年MM月dd日");
            SimpleDateFormat sdf02 = new SimpleDateFormat("yyyy/MM/dd HH:mm");

            FileOutputStream out1 = null;
            File print_out = null;

            Row row, row1;
            Cell cell;
            Object value = null;
            String[] printer = getPrinter();

            /////////////////////////////////////////////////////////////////////////////////
            String s = new StringBuilder()
                     .append("SELECT a1.gate_out_date, a1.report_no, a1.entrepreneur_name, a1.car_no, ")
                     .append("a1.goods_name, a1.net_weight_ton, a1.transporter_name, a1.office_name, a1.contractor_name, ")
                     .append("a1.manifesto_no1, a1.manifesto_no2, a1.m3, ")
                     .append("a1.office_sub_name, ")
                     .append("a1.f按分フラグ, COALESCE(anbun.按分品目id, 0) AS 按分品目id, anbun.按分品目名, anbun.按分正味重量, anbun.按分比重, anbun.按分m3 ")
                     .append("FROM (SELECT * FROM v1_weigh_data ")
                     .append("WHERE term_date >= ? AND term_date <= ? ")
                     .append("ORDER BY gate_out_date) a1 ")
                     .append("LEFT JOIN  (SELECT ")
                     .append("v1_weigh_data_anbun.weight_sno, v1_weigh_data_anbun.按分品目id, ")
                     .append("v1_weigh_data_anbun.按分品目名, v1_weigh_data_anbun.按分正味重量, ")
                     .append("v1_weigh_data_anbun.按分比重, v1_weigh_data_anbun.按分m3 ")
                     .append("FROM v1_weigh_data_anbun ")
                     .append("WHERE v1_weigh_data_anbun.delete_flag <> TRUE ")
                     .append("AND v1_weigh_data_anbun.按分品目id > 0) anbun ON a1.sno = anbun.weight_sno ")
                     .append("ORDER BY term_date, gate_out_date, report_no;")
                     .toString();

            PreparedStatement ps = con.prepareStatement(s);
            try {
                ps.setObject(1, DBManager.convert(term_date));
                ps.setObject(2, DBManager.convert(term_date1));
                ResultSet rs = ps.executeQuery();

                //対象日付
                row = sheet1.getRow(1);//2行目
                cell = row.getCell(0);//A列
                value = (sdf00.format(DBManager.convert(term_date)).equals(sdf00.format(DBManager.convert(term_date1)))
                         ? sdf01.format(DBManager.convert(term_date1))
                         : sdf00.format(DBManager.convert(term_date)) + sdf01.format(DBManager.convert(term_date1)));
                cell.setCellValue((String) value);

                //印刷日
                row = sheet1.getRow(1);//2行目
                cell = row.getCell(7);//8列
                value = "印刷日時：" + sdf02.format(DBManager.convert((Calendar.getInstance()).getTime()));
                cell.setCellValue((String) value);

                int i現在行 = 4;
                try {
                    while (rs.next()) {

                        row1 = sheet1.createRow(i現在行);//5行目スタート
                        row1.setHeightInPoints(15);//行幅を15に指定
                        cell = row1.createCell(0);//1列
                        value = sdf02.format(rs.getTimestamp("gate_out_date"));//計量日時
                        cell.setCellValue((String) value);
                        cell.setCellStyle(style2);

                        cell = row1.createCell(1);//2列
                        value = rs.getString("report_no");//伝票NO
                        cell.setCellValue((String) value);
                        cell.setCellStyle(style2);

                        cell = row1.createCell(2);//3列
                        value = rs.getString("entrepreneur_name");//排出業者
                        cell.setCellValue((String) value);
                        cell.setCellStyle(style);

                        cell = row1.createCell(3);//4列
                        value = rs.getString("car_no");//車番
                        cell.setCellValue((String) value);
                        cell.setCellStyle(style2);

                        if (rs.getBoolean("f按分フラグ") && Integer.valueOf(rs.getString("按分品目id")) > 0) {
                            cell = row1.createCell(4);//5列
                            value = rs.getString("按分品目名");//品名
                            cell.setCellValue((String) value);
                            cell.setCellStyle(style);
                        } else {
                            cell = row1.createCell(4);//5列
                            value = rs.getString("goods_name");//品名
                            cell.setCellValue((String) value);
                            cell.setCellStyle(style);
                        }

                        //正味
                        if (rs.getBoolean("f按分フラグ") && Integer.valueOf(rs.getString("按分品目id")) > 0) {
                            cell = row1.createCell(5);//6列
                            value = rs.getDouble("按分正味重量") / 1000d;//按分正味重量 ｔ　スパット北上
                            cell.setCellValue((Double) value);
                            cell.setCellStyle(rstyle1);
                        } else {
                            cell = row1.createCell(5);//6列
                            value = rs.getDouble("net_weight_ton");//受託量(t) 20161131 スパット北上
                            cell.setCellValue((Double) value);
                            cell.setCellStyle(rstyle1);
                        }

                        if (rs.getDouble("m3") > 0.) {
                            cell = row1.createCell(6);//7列
                            value = rs.getDouble("m3");//m3
                            cell.setCellValue((Double) value);
                            cell.setCellStyle(rstyle1);
                        } else {
                            //書式のみ
                            cell = row1.createCell(6);//7列
                            cell.setCellStyle(rstyle1);
                        }

                        //運搬業者
                        cell = row1.createCell(7);//8列
                        value = rs.getString("transporter_name");//運搬業者
                        cell.setCellValue((String) value);
                        cell.setCellStyle(style);

                        //排出現場
                        cell = row1.createCell(8);//9列
                        value = rs.getString("office_sub_name").length() > 0
                                 ? rs.getString("office_sub_name")
                                 : rs.getString("office_name");//現場名　
                        cell.setCellValue((String) value);
                        cell.setCellStyle(style);

                        //交付番号に変更
                        //処分業者
                        cell = row1.createCell(9);//10列
                        value = rs.getString("manifesto_no1").length() == 11
                                 ? rs.getString("manifesto_no1")
                                 : rs.getString("manifesto_no2").length() == 11
                                 ? rs.getString("manifesto_no2")
                                 : "";//交付番号
                        cell.setCellValue((String) value);
                        cell.setCellStyle(style);

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

            try {
                //自身のtmpに保存
                out1 = new FileOutputStream(storeFol);
                wb1.write(out1);
                //スクリプトから印刷
                print_out = new File(storeFol);
                doPrintXlsx(print_out.getAbsolutePath(), printer[1], 1, true);

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

    //業者別受入集計表　排出事業者別
    private void printReport2(
             String file_path,
             String storeFol,
             Connection con,
             Calendar term_date,
             Calendar term_date1) {

        try {
            InputStream in1 = new FileInputStream(file_path);
            wb1 = WorkbookFactory.create(in1);
            initCom();
            Sheet sheet1 = wb1.getSheet("print");

            SimpleDateFormat sdf00 = new SimpleDateFormat("yyyy年MM月dd日～");
            SimpleDateFormat sdf01 = new SimpleDateFormat("yyyy年MM月dd日");
            SimpleDateFormat sdf02 = new SimpleDateFormat("yyyy/MM/dd HH:mm");

            FileOutputStream out1 = null;
            File print_out = null;

            Row row, row1, row2, row3;
            Cell cell, cell2, cell3;
            Object value = null;
            String[] printer = getPrinter();

            /////////////////////////////////////////////////////////////////////////////////
            String s = new StringBuilder()
                     .append("SELECT count(a1.dis_ent_id)AS cnt, max(a1.dis_ent_id)AS dis_ent_id, max(a1.entrepreneur_name)AS entrepreneur_name, ")
                     .append("max(a1.goods_id)AS goods_id, max(a1.goods_name)AS goods_name, ")
                     .append("max(a1.method_name1)AS method_name, max(a1.goods_option_name)AS goods_option_name, ")
                     .append("sum(a1.net_weight_ton)AS net_weight_ton, sum(a1.m3)AS m3, bool_or(a1.f按分フラグ) AS f按分フラグ, ")
                     .append("COALESCE(anbun.按分品目id, 0) AS 按分品目id, max(anbun.按分品目名)AS 按分品目名, sum(anbun.按分正味重量)AS 按分正味重量, ")
                     .append("sum(anbun.按分比重)AS 按分比重, sum(anbun.按分m3)AS 按分m3 ")
                     .append("FROM(SELECT *, ")
                     .append("CASE WHEN v1_weigh_data.method_name <> ''::text AND v1_weigh_data.method_name IS NOT NULL THEN v1_weigh_data.method_name ")
                     .append("WHEN v1_weigh_data.check_flag09 = true THEN '脱水'::text ")
                     .append("WHEN v1_weigh_data.check_flag10 = true THEN '焼却'::text ")
                     .append("WHEN v1_weigh_data.check_flag11 = true THEN '破砕'::text ")
                     .append("WHEN v1_weigh_data.check_flag12 = true THEN '安定型'::text ")
                     .append("WHEN v1_weigh_data.check_flag13 = true THEN '管理型'::text ")
                     .append("WHEN v1_weigh_data.check_flag13 = true THEN '遮断型'::text ")
                     .append("ELSE ''::text END AS method_name1 ")
                     .append("FROM v1_weigh_data ")
                     .append("WHERE term_date >= ? AND term_date <= ? AND carry_class = 0) a1 ")
                     .append("LEFT JOIN (SELECT ")
                     .append("v1_weigh_data_anbun.weight_sno, v1_weigh_data_anbun.按分品目id, ")
                     .append("v1_weigh_data_anbun.按分品目名, v1_weigh_data_anbun.按分正味重量, ")
                     .append("v1_weigh_data_anbun.按分比重, v1_weigh_data_anbun.按分m3 ")
                     .append("FROM v1_weigh_data_anbun ")
                     .append("WHERE v1_weigh_data_anbun.delete_flag <> true ")
                     .append("AND v1_weigh_data_anbun.按分品目id > 0) anbun ON a1.sno = anbun.weight_sno ")
                     .append("GROUP BY a1.dis_ent_id, a1.goods_id, a1.method_name1, anbun.按分品目id ")
                     .append("ORDER BY a1.dis_ent_id, a1.goods_id, a1.method_name1, anbun.按分品目id ;")
                     .toString();
            PreparedStatement ps = con.prepareStatement(s);
            try {
                ps.setObject(1, DBManager.convert(term_date));
                ps.setObject(2, DBManager.convert(term_date1));
                ResultSet rs = ps.executeQuery();

                //対象日付
                row = sheet1.getRow(1);//2行目
                cell = row.getCell(0);//A列
                value = (sdf00.format(DBManager.convert(term_date)).equals(sdf00.format(DBManager.convert(term_date1)))
                         ? sdf01.format(DBManager.convert(term_date1))
                         : sdf00.format(DBManager.convert(term_date)) + sdf01.format(DBManager.convert(term_date1)));
                cell.setCellValue((String) value);

                //印刷日
                row = sheet1.getRow(1);//2行目
                cell = row.getCell(6);//7列
                value = "印刷日時：" + sdf02.format(DBManager.convert((Calendar.getInstance()).getTime()));
                cell.setCellValue((String) value);

                int i現在行 = 4;
                try {
                    while (rs.next()) {

                        row1 = sheet1.createRow(i現在行);//5行目スタート
                        row1.setHeightInPoints(15);//行幅を15に指定
                        cell = row1.createCell(0);//1列
                        value = rs.getString("dis_ent_id");//業者コード
                        cell.setCellValue((String) value);

                        row2 = sheet1.getRow(i現在行);
                        row3 = sheet1.getRow(i現在行 - 1);
                        cell2 = row2.getCell(0);//1列
                        cell3 = row3.getCell(0);//1列

                        if ((cell2.getStringCellValue()).equals(cell3.getStringCellValue())) {
                            //前回レコードと同じならフォントカラーを白にする
                            cell.setCellStyle(style_blank1);
                        } else {
                            cell.setCellStyle(style_blank0);
                        }

                        cell = row1.createCell(1);//2列
                        value = rs.getString("entrepreneur_name");//業者名
                        cell.setCellValue((String) value);

                        cell2 = row2.getCell(1);//1列
                        cell3 = row3.getCell(1);//1列

                        if ((cell2.getStringCellValue()).equals(cell3.getStringCellValue())) {
                            //前回レコードと同じならフォントカラーを白にする
                            cell.setCellStyle(style_blank1);
                        } else {
                            cell.setCellStyle(style_blank0);
                        }

                        if (rs.getBoolean("f按分フラグ") && Integer.valueOf(rs.getString("按分品目id")) > 0) {
                            cell = row1.createCell(2);//3列
                            value = rs.getString("按分品目id");//按分品目id　　
                            cell.setCellValue((String) value);
                            cell.setCellStyle(style2);

                            cell = row1.createCell(3);//4列
                            value = rs.getString("按分品目名");//按分品目名
                            cell.setCellValue((String) value);
                            cell.setCellStyle(style);
                        } else {
                            cell = row1.createCell(2);//3列
                            value = rs.getString("goods_id");//商品コード　　
                            cell.setCellValue((String) value);
                            cell.setCellStyle(style2);

                            cell = row1.createCell(3);//4列
                            value = rs.getString("goods_name");//商品名
                            cell.setCellValue((String) value);
                            cell.setCellStyle(style);
                        }

                        cell = row1.createCell(4);//5列
                        value = rs.getString("goods_option_name");//取扱区分
                        cell.setCellValue((String) value);
                        cell.setCellStyle(style);

                        cell = row1.createCell(5);//6列
//                        value = rs.getString("method_name");//処分方法
                        value = rs.getInt("cnt");//件数
                        cell.setCellValue((Integer) value);
                        cell.setCellStyle(cstyle);

                        //正味
                        if (rs.getBoolean("f按分フラグ") && Integer.valueOf(rs.getString("按分品目id")) > 0) {
                            cell = row1.createCell(6);//7列
                            value = rs.getDouble("按分正味重量") / 1000d;//按分正味重量 ｔ　スパット北上
                            cell.setCellValue((Double) value);
                            cell.setCellStyle(rstyle1);

                            //容積
                            cell = row1.createCell(7);//8列
                            value = rs.getDouble("按分m3");//按分m3
                            if ((Double) value > 0.) {
                                cell.setCellValue((Double) value);
                            }
                            cell.setCellStyle(rstyle1);

                        } else {
                            cell = row1.createCell(6);//7列
                            value = rs.getDouble("net_weight_ton");//正味重量(t) 20161131 スパット北上
                            cell.setCellValue((Double) value);
                            cell.setCellStyle(rstyle1);

                            //容積
                            cell = row1.createCell(7);//8列
                            value = rs.getDouble("m3");//受託量 m3
                            if ((Double) value > 0.) {
                                cell.setCellValue((Double) value);
                            }
                            cell.setCellStyle(rstyle1);

                        }

                        i現在行++;
                    }

                    row1 = sheet1.createRow(i現在行);//5行目スタート
                    cell = row1.createCell(0);//1列
                    cell.setCellStyle(style_blank2);
                    cell = row1.createCell(1);//2列
                    cell.setCellStyle(style_blank2);

                } catch (Exception ie1) {
                    System.out.println(ie1.toString() + String.valueOf(i現在行));
                }

                i現在行++;
                ////品目ごとの合計行を出力する////////////////////////////////////////
                s = new StringBuilder()
                         .append("SELECT count(a1.goods_id)AS cnt,  ")
                         .append("max(a1.goods_id)AS goods_id, max(a1.goods_name)AS goods_name,  ")
                         .append("max(a1.method_name1)AS method_name, max(a1.goods_option_name)AS goods_option_name,  ")
                         .append("sum(a1.net_weight_ton)AS net_weight_ton, sum(a1.m3)AS m3 ")
                         .append("FROM(SELECT *, ")
                         .append("CASE WHEN v1_weigh_data.method_name <> ''::text AND v1_weigh_data.method_name IS NOT NULL THEN v1_weigh_data.method_name ")
                         .append("WHEN v1_weigh_data.check_flag09 = true THEN '脱水'::text ")
                         .append("WHEN v1_weigh_data.check_flag10 = true THEN '焼却'::text ")
                         .append("WHEN v1_weigh_data.check_flag11 = true THEN '破砕'::text ")
                         .append("WHEN v1_weigh_data.check_flag12 = true THEN '安定型'::text ")
                         .append("WHEN v1_weigh_data.check_flag13 = true THEN '管理型'::text ")
                         .append("WHEN v1_weigh_data.check_flag13 = true THEN '遮断型'::text ")
                         .append("ELSE ''::text END AS method_name1 ")
                         .append("FROM v1_weigh_data ")
                         .append("WHERE term_date >= ? AND term_date <= ? AND carry_class = 0) a1 ")
                         .append("GROUP BY a1.goods_id ")
                         .append("ORDER BY a1.goods_id; ")
                         .toString();

                ps = con.prepareStatement(s);

                ps.setObject(1, DBManager.convert(term_date));
                ps.setObject(2, DBManager.convert(term_date1));
                ResultSet rs_sum = ps.executeQuery();

                while (rs_sum.next()) {

                    i現在行++;
                    row1 = sheet1.createRow(i現在行);//5行目スタート
                    row1.setHeightInPoints(15);//行幅を15に指定
                    cell = row1.createCell(2);//3列
                    value = rs_sum.getString("goods_id");//商品コード　　
                    cell.setCellValue((String) value);
                    cell.setCellStyle(style2);

                    cell = row1.createCell(3);//4列
                    value = rs_sum.getString("goods_name") + "合計";//商品名
                    cell.setCellValue((String) value);
                    cell.setCellStyle(style);

                    cell = row1.createCell(4);//5列
                    cell.setCellStyle(style);

                    cell = row1.createCell(5);//6列
                    value = rs_sum.getInt("cnt");//件数
                    cell.setCellValue((Integer) value);
                    cell.setCellStyle(cstyle);

                    cell = row1.createCell(6);//7列
                    value = rs_sum.getDouble("net_weight_ton");//正味重量(t) 20161131 スパット北上
                    cell.setCellValue((Double) value);
                    cell.setCellStyle(rstyle1);

                    //容積
                    cell = row1.createCell(7);//8列
                    value = rs_sum.getDouble("m3");//受託量 m3
                    if ((Double) value > 0.) {
                        cell.setCellValue((Double) value);
                    }
                    cell.setCellStyle(rstyle1);

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
                doPrintXlsx(print_out.getAbsolutePath(), printer[1], 1, true);

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

    //業者別受入集計表　運搬業者別
    private void printReport3(
             String file_path,
             String storeFol,
             Connection con,
             Calendar term_date,
             Calendar term_date1) {

        try {
            InputStream in1 = new FileInputStream(file_path);
            wb1 = WorkbookFactory.create(in1);
            initCom();
            Sheet sheet1 = wb1.getSheet("print");

            SimpleDateFormat sdf00 = new SimpleDateFormat("yyyy年MM月dd日～");
            SimpleDateFormat sdf01 = new SimpleDateFormat("yyyy年MM月dd日");
            SimpleDateFormat sdf02 = new SimpleDateFormat("yyyy/MM/dd HH:mm");

            FileOutputStream out1 = null;
            File print_out = null;

            Row row, row1, row2, row3;
            Cell cell, cell2, cell3;
            Object value = null;
            String[] printer = getPrinter();
            /////////////////////////////////////////////////////////////////////////////////
            String s = new StringBuilder()
                     .append("SELECT count(a1.trans_id)AS cnt, max(a1.trans_id)AS trans_id, max(a1.transporter_name)AS transporter_name, ")
                     .append("max(a1.goods_id)AS goods_id, max(a1.goods_name)AS goods_name, max(a1.dis_ent_id)AS dis_ent_id, ")
                     .append("max(a1.entrepreneur_name)AS entrepreneur_name, sum(a1.net_weight_ton)AS net_weight_ton, sum(a1.m3)AS m3, ")
                     .append("bool_or(a1.f按分フラグ) AS f按分フラグ, COALESCE(anbun.按分品目id, 0) as 按分品目id, ")
                     .append("max(anbun.按分品目名)AS 按分品目名, sum(anbun.按分正味重量)AS 按分正味重量, ")
                     .append("sum(anbun.按分比重)AS 按分比重, sum(anbun.按分m3)AS 按分m3 ")
                     .append("FROM (SELECT * ")
                     .append("FROM v1_weigh_data ")
                     .append("WHERE term_date >= ? AND term_date <= ? AND carry_class = 0 )a1 ")
                     .append("LEFT JOIN  (SELECT ")
                     .append("v1_weigh_data_anbun.weight_sno, v1_weigh_data_anbun.按分品目id, ")
                     .append("v1_weigh_data_anbun.按分品目名, v1_weigh_data_anbun.按分正味重量, ")
                     .append("v1_weigh_data_anbun.按分比重, v1_weigh_data_anbun.按分m3 ")
                     .append("FROM v1_weigh_data_anbun ")
                     .append("WHERE v1_weigh_data_anbun.delete_flag <> TRUE ")
                     .append("AND v1_weigh_data_anbun.按分品目id > 0) anbun ON a1.sno = anbun.weight_sno ")
                     .append("GROUP BY a1.trans_id, a1.dis_ent_id, a1.goods_id, anbun.按分品目id ")
                     .append("ORDER BY a1.trans_id, a1.dis_ent_id, a1.goods_id, anbun.按分品目id ;")
                     .toString();
            PreparedStatement ps = con.prepareStatement(s);

            try {
                ps.setObject(1, DBManager.convert(term_date));
                ps.setObject(2, DBManager.convert(term_date1));
                ResultSet rs = ps.executeQuery();

                //対象日付
                row = sheet1.getRow(1);//2行目
                cell = row.getCell(0);//A列
                value = (sdf00.format(DBManager.convert(term_date)).equals(sdf00.format(DBManager.convert(term_date1)))
                         ? sdf01.format(DBManager.convert(term_date1))
                         : sdf00.format(DBManager.convert(term_date)) + sdf01.format(DBManager.convert(term_date1)));
                cell.setCellValue((String) value);

                //印刷日
                row = sheet1.getRow(1);//2行目
                cell = row.getCell(6);//7列
                value = "印刷日時：" + sdf02.format(DBManager.convert((Calendar.getInstance()).getTime()));
                cell.setCellValue((String) value);

                int i現在行 = 4;
                boolean b前回運搬業者 = false;
                try {
                    while (rs.next()) {

                        row1 = sheet1.createRow(i現在行);//5行目スタート
                        row1.setHeightInPoints(15);//行幅を15に指定

                        cell = row1.createCell(0);//1列
                        value = rs.getString("trans_id");//業者コード
                        cell.setCellValue((String) value);
                        row2 = sheet1.getRow(i現在行);
                        row3 = sheet1.getRow(i現在行 - 1);
                        cell2 = row2.getCell(0);//1列
                        cell3 = row3.getCell(0);//1列

                        if ((cell2.getStringCellValue()).equals(cell3.getStringCellValue())) {
                            //前回レコードと同じならフォントカラーを白にする
                            cell.setCellStyle(style_blank1);
                            b前回運搬業者 = false;
                        } else {
                            cell.setCellStyle(style_blank0);
                            b前回運搬業者 = true;
                        }

                        cell = row1.createCell(1);//2列
                        value = rs.getString("transporter_name");//業者名
                        cell.setCellValue((String) value);
                        cell2 = row2.getCell(1);//1列
                        cell3 = row3.getCell(1);//1列

                        if ((cell2.getStringCellValue()).equals(cell3.getStringCellValue()) && !b前回運搬業者) {
                            //前回レコードと同じならフォントカラーを白にする
                            cell.setCellStyle(style_blank1);
                        } else {
                            cell.setCellStyle(style_blank0);
                        }

                        cell = row1.createCell(2);//3列
                        value = rs.getString("dis_ent_id");//排出業者コード　　　　
                        cell.setCellValue((String) value);
                        cell2 = row2.getCell(2);//1列
                        cell3 = row3.getCell(2);//1列

                        if ((cell2.getStringCellValue()).equals(cell3.getStringCellValue())) {
                            //前回レコードと同じならフォントカラーを白にする
                            cell.setCellStyle(style_blank1);
                        } else {
                            cell.setCellStyle(style_blank0);
                        }

                        cell = row1.createCell(3);//4列
                        value = rs.getString("entrepreneur_name");//排出業者名
                        cell.setCellValue((String) value);
                        cell2 = row2.getCell(3);//1列
                        cell3 = row3.getCell(3);//1列

                        if ((cell2.getStringCellValue()).equals(cell3.getStringCellValue())) {
                            //前回レコードと同じならフォントカラーを白にする
                            cell.setCellStyle(style_blank1);
                        } else {
                            cell.setCellStyle(style_blank0);
                        }

                        if (rs.getBoolean("f按分フラグ") && Integer.valueOf(rs.getString("按分品目id")) > 0) {
                            cell = row1.createCell(4);//5列
                            value = rs.getString("按分品目id");//按分品目id　　
                            cell.setCellValue((String) value);
                            cell.setCellStyle(style2);

                            cell = row1.createCell(5);//6列
                            value = rs.getString("按分品目名");//按分品目名
                            cell.setCellValue((String) value);
                            cell.setCellStyle(style);

                        } else {
                            cell = row1.createCell(4);//5列
                            value = rs.getString("goods_id");//商品コード　　
                            cell.setCellValue((String) value);
                            cell.setCellStyle(style2);

                            cell = row1.createCell(5);//6列
                            value = rs.getString("goods_name");//商品名
                            cell.setCellValue((String) value);
                            cell.setCellStyle(style);

                        }

                        //正味 容積
                        if (rs.getBoolean("f按分フラグ") && Integer.valueOf(rs.getString("按分品目id")) > 0) {
                            cell = row1.createCell(6);//7列
                            value = rs.getDouble("按分正味重量") / 1000d;//按分正味重量 ｔ　スパット北上
                            cell.setCellValue((Double) value);
                            cell.setCellStyle(rstyle1);

                            cell = row1.createCell(7);//8列
                            value = rs.getDouble("按分m3");//按分m3
                            if ((Double) value > 0.) {
                                cell.setCellValue((Double) value);
                            }
                            cell.setCellStyle(rstyle1);

                        } else {
                            cell = row1.createCell(6);//7列
                            value = rs.getDouble("net_weight_ton");//正味重量(t) 20161131 スパット北上
                            cell.setCellValue((Double) value);
                            cell.setCellStyle(rstyle1);

                            cell = row1.createCell(7);//8列
                            value = rs.getDouble("m3");//受託量 m3
                            if ((Double) value > 0.) {
                                cell.setCellValue((Double) value);
                            }
                            cell.setCellStyle(rstyle1);

                        }

                        i現在行++;
                    }

                    row1 = sheet1.createRow(i現在行);//5行目スタート
                    cell = row1.createCell(0);//1列
                    cell.setCellStyle(style_blank2);
                    cell = row1.createCell(1);//2列
                    cell.setCellStyle(style_blank2);
                    cell = row1.createCell(2);//3列
                    cell.setCellStyle(style_blank2);
                    cell = row1.createCell(3);//4列
                    cell.setCellStyle(style_blank2);

                } catch (Exception ie1) {
                    System.out.println(ie1.toString() + String.valueOf(i現在行));
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
                doPrintXlsx(print_out.getAbsolutePath(), printer[1], 1, true);

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

    //業者別受入集計表　廃棄物名別
    private void printReport4(
             String file_path,
             String storeFol,
             Connection con,
             Calendar term_date,
             Calendar term_date1) {

        try {
            InputStream in1 = new FileInputStream(file_path);
            wb1 = WorkbookFactory.create(in1);
            initCom();
            Sheet sheet1 = wb1.getSheet("print");
            SimpleDateFormat sdf00 = new SimpleDateFormat("yyyy年MM月dd日～");
            SimpleDateFormat sdf01 = new SimpleDateFormat("yyyy年MM月dd日");
            SimpleDateFormat sdf02 = new SimpleDateFormat("yyyy/MM/dd HH:mm");

            FileOutputStream out1 = null;
            File print_out = null;

            Row row, row1;
            Cell cell;
            Object value = null;
            String[] printer = getPrinter();

            /////////////////////////////////////////////////////////////////////////////////
            String s = new StringBuilder()
                     .append("SELECT count(a1.*)AS cnt, max(a1.goods_id)AS goods_id, max(a1.goods_name)AS goods_name, ")
                     .append("max(a1.dis_cont_id)AS dis_cont_id, max(a1.contractor_name)AS contractor_name, ")
                     .append("max(a1.method_name1)AS method_name, max(a1.goods_option_name)AS goods_option_name, ")
                     .append("max(a1.manifesto3_id1)AS manifesto3_id1, max(a1.cont_office_name)AS cont_office_name, ")
                     .append("sum(a1.net_weight_ton)AS net_weight_ton, sum(a1.m3)AS m3, max(a1.unit_no) as unit_no, bool_or(a1.f按分フラグ) AS f按分フラグ, ")
                     .append("COALESCE(anbun.按分品目id, 0) AS 按分品目id, max(anbun.按分品目名)AS 按分品目名, sum(anbun.按分正味重量)AS 按分正味重量, ")
                     .append("sum(anbun.按分比重)AS 按分比重, sum(anbun.按分m3)AS 按分m3 ")
                     .append("FROM(SELECT *, ")
                     .append("CASE WHEN v1_weigh_data.method_name <> ''::text AND v1_weigh_data.method_name IS NOT NULL THEN v1_weigh_data.method_name ")
                     .append("WHEN v1_weigh_data.check_flag09 = true THEN '脱水'::text ")
                     .append("WHEN v1_weigh_data.check_flag10 = true THEN '焼却'::text ")
                     .append("WHEN v1_weigh_data.check_flag11 = true THEN '破砕'::text ")
                     .append("WHEN v1_weigh_data.check_flag12 = true THEN '安定型'::text ")
                     .append("WHEN v1_weigh_data.check_flag13 = true THEN '管理型'::text ")
                     .append("WHEN v1_weigh_data.check_flag13 = true THEN '遮断型'::text ")
                     .append("ELSE ''::text END AS method_name1 ")
                     .append("FROM v1_weigh_data ")
                     .append("WHERE term_date >= ? AND term_date <= ? AND carry_class = 0 ")
                     .append(")a1 ")
                     .append("LEFT JOIN  (SELECT ")
                     .append("v1_weigh_data_anbun.weight_sno, v1_weigh_data_anbun.按分品目id, ")
                     .append("v1_weigh_data_anbun.按分品目名, v1_weigh_data_anbun.按分正味重量, ")
                     .append("v1_weigh_data_anbun.按分比重, v1_weigh_data_anbun.按分m3 ")
                     .append("FROM v1_weigh_data_anbun ")
                     .append("WHERE v1_weigh_data_anbun.delete_flag <> TRUE ")
                     .append("AND v1_weigh_data_anbun.按分品目id > 0) anbun ON a1.sno = anbun.weight_sno ")
                     .append("GROUP BY a1.goods_id, a1.method_name1, a1.manifesto3_id1, anbun.按分品目id ") //処分業者IDを処分場IDへ変更
                     .append("ORDER BY a1.goods_id, a1.method_name1, a1.manifesto3_id1, anbun.按分品目id ;")
                     .toString();
            PreparedStatement ps = con.prepareStatement(s);
            try {
                ps.setObject(1, DBManager.convert(term_date));
                ps.setObject(2, DBManager.convert(term_date1));
                ResultSet rs = ps.executeQuery();

                //対象日付
                row = sheet1.getRow(1);//2行目
                cell = row.getCell(0);//A列
                value = (sdf00.format(DBManager.convert(term_date)).equals(sdf00.format(DBManager.convert(term_date1)))
                         ? sdf01.format(DBManager.convert(term_date1))
                         : sdf00.format(DBManager.convert(term_date)) + sdf01.format(DBManager.convert(term_date1)));
                cell.setCellValue((String) value);

                //印刷日
                row = sheet1.getRow(1);//2行目
                cell = row.getCell(6);//7列
                value = "印刷日時：" + sdf02.format(DBManager.convert((Calendar.getInstance()).getTime()));
                cell.setCellValue((String) value);

                int i現在行 = 4;
                try {
                    while (rs.next()) {

                        row1 = sheet1.createRow(i現在行);//5行目スタート
                        row1.setHeightInPoints(15);//行幅を15に指定

                        if (rs.getBoolean("f按分フラグ") && Integer.valueOf(rs.getString("按分品目id")) > 0) {
                            cell = row1.createCell(0);//1列
                            value = rs.getString("按分品目id");//按分品目id　　
                            cell.setCellValue((String) value);
                            cell.setCellStyle(style2);

                            cell = row1.createCell(1);//2列
                            value = rs.getString("按分品目名");//按分品目名
                            cell.setCellValue((String) value);
                            cell.setCellStyle(style);

                        } else {
                            cell = row1.createCell(0);//1列
                            value = rs.getString("goods_id");//商品コード　　　
                            cell.setCellValue((String) value);
                            cell.setCellStyle(style2);

                            cell = row1.createCell(1);//2列
                            value = rs.getString("goods_name");//商品名
                            cell.setCellValue((String) value);
                            cell.setCellStyle(style);

                        }

                        cell = row1.createCell(2);//3列
                        value = rs.getString("goods_option_name");//取扱区分
                        cell.setCellValue((String) value);
                        cell.setCellStyle(style);

                        cell = row1.createCell(3);//4列
                        value = rs.getString("method_name");//処分方法
                        cell.setCellValue((String) value);
                        cell.setCellStyle(style);

                        cell = row1.createCell(4);//5列
                        value = rs.getString("manifesto3_id1");//処分場コード
                        cell.setCellValue((String) value);
                        cell.setCellStyle(style2);

                        cell = row1.createCell(5);//6列
                        value = rs.getString("cont_office_name");//処分場
                        cell.setCellValue((String) value);
                        cell.setCellStyle(style);

                        //正味 容積
                        if (rs.getBoolean("f按分フラグ") && Integer.valueOf(rs.getString("按分品目id")) > 0) {
                            cell = row1.createCell(6);//7列
                            value = rs.getDouble("按分正味重量") / 1000d;//按分正味重量 ｔ　スパット北上
                            cell.setCellValue((Double) value);
                            cell.setCellStyle(rstyle1);

                            cell = row1.createCell(7);//8列
                            value = rs.getDouble("按分m3");//按分m3
                            if ((Double) value > 0.) {
                                cell.setCellValue((Double) value);
                            }
                            cell.setCellStyle(rstyle1);

                        } else {
                            cell = row1.createCell(6);//7列
                            value = rs.getDouble("net_weight_ton");//正味重量(t) 20161131 スパット北上
                            cell.setCellValue((Double) value);
                            cell.setCellStyle(rstyle1);

                            cell = row1.createCell(7);//8列
                            value = rs.getDouble("m3");//受託量 m3
                            if ((Double) value > 0.) {
                                cell.setCellValue((Double) value);
                            }
                            cell.setCellStyle(rstyle1);

                        }

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

            try {
                //自身のtmpに保存
                out1 = new FileOutputStream(storeFol);
                wb1.write(out1);
                //スクリプトから印刷
                print_out = new File(storeFol);
                doPrintXlsx(print_out.getAbsolutePath(), printer[1], 1, true);

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

    //マニフェスト管理台帳
    private void printReport5(
             String file_path,
             String storeFol,
             Connection con,
             Calendar term_date,
             Calendar term_date1,
             int ent_id) {

        try {
            InputStream in1 = new FileInputStream(file_path);
            wb1 = WorkbookFactory.create(in1);
            initCom();

            Sheet sheet1;
            Sheet sheet2;

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
            SimpleDateFormat sdf00 = new SimpleDateFormat("yyyy年MM月dd日～");
            SimpleDateFormat sdf01 = new SimpleDateFormat("yyyy年MM月dd日");
            SimpleDateFormat sdf02 = new SimpleDateFormat("yyyy/MM/dd HH:mm");

            FileOutputStream out1 = null;
            File print_out = null;

            Row row, row1, row2, row3;
            Cell cell, cell2, cell3;
            Object value = null;
            String[] printer = getPrinter();

            ////////////////////////////////////////////////////////////////////////////////
            ///品目ごとにループ（処理開始）///////////////////////////////////////////////
            String s0 = new StringBuilder()
                     .append("SELECT max(a3.base_goods_id)as base_goods_id, max(a3.base_goods_name)as base_goods_name ")
                     .append("from( SELECT ")
                     .append("v0_goods.middle_goods_kind AS base_goods_id, ")
                     .append("v0_goods.middle_goods_kind_name AS base_goods_name ")
                     .append("from( ")
                     .append("SELECT a1.term_date, ")
                     .append("case when a1.f按分フラグ = true then COALESCE(anbun.按分品目id, 0) ")
                     .append("else a1.goods_id end as goods_id, ")
                     .append("case when a1.f按分フラグ = true then COALESCE(anbun.按分品目名, '') ")
                     .append("else a1.goods_name end as goods_name, ")
                     .append("a1.dis_ent_id, a1.entrepreneur_name, ")
                     .append("a1.grant_date1, a1.grant_date2, ")
                     .append("a1.manifesto_no1, a1.manifesto_no2, a1.dis_cont_id, a1.contractor_name, a1.net_weight, a1.method_name1, ")
                     .append("a1.office_name, a1.office_sub_name, a1.car_no, a1.unit_no, a1.unit_name, ")
                     .append("a1.cont_date2, a1.trans1_date, a1.manifesto3_id1, ")
                     .append("a1.cont_office_name, a1.car_kind_id, a1.car_kind_name, ")
                     .append("COALESCE(a1.secod_manifesto_no, '')as secod_manifesto_no, ")
                     .append("a1.f按分フラグ, COALESCE(anbun.按分品目id, 0) AS 按分品目id, anbun.按分品目名, anbun.按分正味重量, anbun.按分比重, anbun.按分m3 ")
                     .append("FROM(SELECT *, ")
                     .append("CASE WHEN v1_weigh_data.method_name <> ''::text AND v1_weigh_data.method_name IS NOT NULL THEN v1_weigh_data.method_name ")
                     .append("WHEN v1_weigh_data.check_flag09 = true THEN '脱水'::text ")
                     .append("WHEN v1_weigh_data.check_flag10 = true THEN '焼却'::text ")
                     .append("WHEN v1_weigh_data.check_flag11 = true THEN '破砕'::text ")
                     .append("WHEN v1_weigh_data.check_flag12 = true THEN '安定型'::text ")
                     .append("WHEN v1_weigh_data.check_flag13 = true THEN '管理型'::text ")
                     .append("WHEN v1_weigh_data.check_flag13 = true THEN '遮断型'::text ")
                     .append("ELSE ''::text END AS method_name1 ")
                     .append("FROM v1_weigh_data ")
                     .append("WHERE term_date >= ? and term_date <= ? and carry_class = 0 and goods_option_name in ('中間処分', '最終処分') ")
                     .append("AND ((? > 0 and dis_ent_id = ?) or (? = 0 and dis_ent_id > 0))) a1 ")
                     .append("LEFT JOIN  (SELECT ")
                     .append("v1_weigh_data_anbun.weight_sno, v1_weigh_data_anbun.按分品目id, ")
                     .append("v1_weigh_data_anbun.按分品目名, v1_weigh_data_anbun.按分正味重量, ")
                     .append("v1_weigh_data_anbun.按分比重, v1_weigh_data_anbun.按分m3 ")
                     .append("FROM v1_weigh_data_anbun ")
                     .append("WHERE v1_weigh_data_anbun.delete_flag <> TRUE ")
                     .append("AND v1_weigh_data_anbun.按分品目id > 0) anbun ON a1.sno = anbun.weight_sno)a2 ")
                     .append("LEFT JOIN v0_goods_kind v0_goods ON a2.goods_id = v0_goods.id)a3 ")
                     .append("GROUP BY a3.base_goods_id ORDER BY a3.base_goods_id;")
                     .toString();
            PreparedStatement ps0 = con.prepareStatement(s0);
            try {
                ps0.setObject(1, DBManager.convert(term_date));
                ps0.setObject(2, DBManager.convert(term_date1));

                ps0.setObject(3, ent_id);
                ps0.setObject(4, ent_id);
                ps0.setObject(5, ent_id);

                ResultSet rs0 = ps0.executeQuery();
                while (rs0.next()) {
                    ////////////////////////////////////////////////////////////////////////////////
                    sheet1 = wb1.cloneSheet(0);
                    wb1.setSheetName(wb1.getNumberOfSheets() - 1, rs0.getString("base_goods_name") + "(処分)");
                    // 印刷用紙指定
                    PrintSetup prints1 = sheet1.getPrintSetup();
                    prints1.setPaperSize(PrintSetup.A4_PAPERSIZE);
                    prints1.setLandscape(true);    //横向き
                    sheet2 = wb1.cloneSheet(1);
                    wb1.setSheetName(wb1.getNumberOfSheets() - 1, rs0.getString("base_goods_name") + "(運搬)");
                    // 印刷用紙指定
                    PrintSetup prints2 = sheet2.getPrintSetup();
                    prints2.setPaperSize(PrintSetup.A4_PAPERSIZE);
                    prints2.setLandscape(true);    //横向き

                    ///////処分業/////////////////////////////////////////////////////////////////
                    String s = new StringBuilder()
                             .append("SELECT a3.* FROM (SELECT a2.*, ")
                             .append("v0_goods.middle_goods_kind AS base_goods_id, ")
                             .append("v0_goods.middle_goods_kind_name AS base_goods_name ")
                             .append("from( ")
                             .append("SELECT a1.term_date, a1.report_no, ")
                             .append("case when a1.f按分フラグ = true then COALESCE(anbun.按分品目id, 0) ")
                             .append("else a1.goods_id end as goods_id, ")
                             .append("case when a1.f按分フラグ = true then COALESCE(anbun.按分品目名, '') ")
                             .append("else a1.goods_name end as goods_name, ")
                             .append("a1.dis_ent_id, a1.entrepreneur_name, ")
                             .append("a1.grant_date1, a1.grant_date2, ")
                             .append("a1.manifesto_no1, a1.manifesto_no2, a1.dis_cont_id, a1.contractor_name, a1.net_weight, a1.method_name1, ")
                             .append("a1.office_name, a1.office_sub_name, a1.car_no, a1.unit_no, a1.unit_name, ")
                             .append("a1.cont_date2, a1.trans1_date, a1.manifesto3_id1, ")
                             .append("a1.cont_office_name, a1.car_kind_id, a1.car_kind_name, ")
                             .append("COALESCE(a1.secod_manifesto_no, '')as secod_manifesto_no, ")
                             .append("a1.f按分フラグ, COALESCE(anbun.按分品目id, 0) AS 按分品目id, anbun.按分品目名, anbun.按分正味重量, anbun.按分比重, anbun.按分m3 ")
                             .append("FROM(SELECT *, ")
                             .append("CASE WHEN v1_weigh_data.method_name <> ''::text AND v1_weigh_data.method_name IS NOT NULL THEN v1_weigh_data.method_name ")
                             .append("WHEN v1_weigh_data.check_flag09 = true THEN '脱水'::text ")
                             .append("WHEN v1_weigh_data.check_flag10 = true THEN '焼却'::text ")
                             .append("WHEN v1_weigh_data.check_flag11 = true THEN '破砕'::text ")
                             .append("WHEN v1_weigh_data.check_flag12 = true THEN '安定型'::text ")
                             .append("WHEN v1_weigh_data.check_flag13 = true THEN '管理型'::text ")
                             .append("WHEN v1_weigh_data.check_flag13 = true THEN '遮断型'::text ")
                             .append("ELSE ''::text END AS method_name1 ")
                             .append("FROM v1_weigh_data ")
                             .append("WHERE term_date >= ? and term_date <= ? and carry_class = 0 and goods_option_name in ('中間処分', '最終処分') ")
                             .append("AND ((? > 0 and dis_ent_id = ?) or (? = 0 and dis_ent_id > 0))) a1 ")
                             .append("LEFT JOIN  (SELECT ")
                             .append("v1_weigh_data_anbun.weight_sno, v1_weigh_data_anbun.按分品目id, ")
                             .append("v1_weigh_data_anbun.按分品目名, v1_weigh_data_anbun.按分正味重量, ")
                             .append("v1_weigh_data_anbun.按分比重, v1_weigh_data_anbun.按分m3 ")
                             .append("FROM v1_weigh_data_anbun ")
                             .append("WHERE v1_weigh_data_anbun.delete_flag <> TRUE ")
                             .append("AND v1_weigh_data_anbun.按分品目id > 0) anbun ON a1.sno = anbun.weight_sno)a2 ")
                             .append("LEFT JOIN v0_goods_kind v0_goods ON a2.goods_id = v0_goods.id)a3 ")
                             .append("WHERE a3.base_goods_id = ? ")
                             .append("ORDER BY a3.dis_ent_id, a3.term_date, a3.report_no, a3.goods_id, a3.按分品目id; ")
                             .toString();
                    PreparedStatement ps = con.prepareStatement(s);
                    try {
                        ps.setObject(1, DBManager.convert(term_date));
                        ps.setObject(2, DBManager.convert(term_date1));

                        ps.setObject(3, ent_id);
                        ps.setObject(4, ent_id);
                        ps.setObject(5, ent_id);

                        ps.setObject(6, rs0.getInt("base_goods_id"));

                        ResultSet rs = ps.executeQuery();

                        //集計品目
                        row = sheet1.getRow(1);//2行目
                        cell = row.getCell(1);//B列
                        value = rs0.getString("base_goods_name");
                        cell.setCellValue((String) value);
                        //対象日付
                        cell = row.getCell(3);//D列
                        value = (sdf00.format(DBManager.convert(term_date)).equals(sdf00.format(DBManager.convert(term_date1)))
                                 ? sdf01.format(DBManager.convert(term_date1))
                                 : sdf00.format(DBManager.convert(term_date)) + sdf01.format(DBManager.convert(term_date1)));
                        cell.setCellValue((String) value);

                        //印刷日
                        row = sheet1.getRow(1);//2行目
                        cell = row.getCell(8);//9列
                        value = "印刷日時：" + sdf02.format(DBManager.convert((Calendar.getInstance()).getTime()));
                        cell.setCellValue((String) value);

                        int i現在行 = 5;

                        try {
                            while (rs.next()) {

                                row1 = sheet1.createRow(i現在行);//5行目スタート
                                row1.setHeightInPoints((short) 24);
                                cell = row1.createCell(0);//1列
                                value = sdf.format(rs.getTimestamp("term_date"));//受入日（所属日）
                                cell.setCellValue((String) value);
                                cell.setCellStyle(style2);

                                if (rs.getBoolean("f按分フラグ") && Integer.valueOf(rs.getString("按分品目id")) > 0) {

                                    cell = row1.createCell(1);//2列
                                    value = rs.getString("按分品目名");//按分品目名
                                    cell.setCellValue((String) value);
                                    cell.setCellStyle(style);

                                } else {
                                    cell = row1.createCell(1);//2列
                                    value = rs.getString("goods_name");
                                    cell.setCellValue((String) value);
                                    cell.setCellStyle(rstyle4);

                                }

                                //車番
                                cell = row1.createCell(2);//3列
                                value = rs.getString("car_no");//車番
                                cell.setCellValue((String) value);
                                cell.setCellStyle(rstyle4);

                                //交付者氏名
                                cell = row1.createCell(3);//4列
                                value = rs.getString("entrepreneur_name");
                                cell.setCellValue((String) value);
                                cell.setCellStyle(rstyle6);

                                //交付日
                                cell = row1.createCell(4);//5列
                                value = rs.getString("grant_date1") != null && rs.getString("grant_date1").length() > 0
                                         ? sdf.format(rs.getTimestamp("grant_date1"))
                                         : rs.getString("grant_date2") != null && rs.getString("grant_date2").length() > 0
                                         ? sdf.format(rs.getTimestamp("grant_date2")) : "";
                                cell.setCellValue((String) value);
                                cell.setCellStyle(style2);

                                //交付番号
                                cell = row1.createCell(5);//6列
                                value = rs.getString("manifesto_no1").length() > 0
                                         ? rs.getString("manifesto_no1")
                                         : rs.getString("manifesto_no2").length() > 0
                                         ? rs.getString("manifesto_no2") : "";
                                cell.setCellValue((String) value);

                                //受入先
                                cell = row1.createCell(6);//7列
                                value = rs.getString("office_sub_name").length() > 0
                                         ? rs.getString("office_sub_name")
                                         : rs.getString("office_name");//現場名　
                                cell.setCellValue((String) value);

                                row2 = sheet1.getRow(i現在行);
                                row3 = sheet1.getRow(i現在行 - 1);
                                cell2 = row2.getCell(5);//1列
                                cell3 = row3.getCell(5);//1列

                                if ((cell2.getStringCellValue()).equals(cell3.getStringCellValue())
                                         && (cell2.getStringCellValue()).length() > 0) {
                                    //前回レコードと同じならフォントカラーを白にする
                                    cell.setCellStyle(style_blank1);
                                    cell = row1.getCell(0);//1列
                                    cell.setCellStyle(style_blank1);
                                    cell = row1.getCell(3);//4列
                                    cell.setCellStyle(style_blank1);
                                    cell = row1.getCell(4);//5列
                                    cell.setCellStyle(style_blank1);
                                    cell = row1.getCell(5);//6列
                                    cell.setCellStyle(style_blank1);
                                    cell = row1.getCell(6);//7列
                                    cell.setCellStyle(style_blank1);
                                } else {
                                    cell.setCellStyle(style_blank0);
                                    cell = row1.getCell(0);//1列
                                    cell.setCellStyle(style_blank0);
                                    cell = row1.getCell(3);//4列
                                    cell.setCellStyle(style_blank0);
                                    cell = row1.getCell(4);//5列
                                    cell.setCellStyle(style_blank0);
                                    cell = row1.getCell(5);//6列
                                    cell.setCellStyle(style_blank0);
                                    cell = row1.getCell(6);//7列
                                    cell.setCellStyle(style_blank0);
                                }

                                //受入量
                                if (rs.getBoolean("f按分フラグ") && Integer.valueOf(rs.getString("按分品目id")) > 0) {
                                    cell = row1.createCell(7);//8列
                                    if (rs.getInt("unit_no") == 1) {
                                        value = rs.getDouble("按分正味重量") / 1000d;
                                        cell.setCellValue((Double) value);
                                        cell.setCellStyle(rstyle1);
                                    } else {
                                        value = rs.getInt("按分正味重量");
                                        cell.setCellValue((Integer) value);
                                        cell.setCellStyle(cstyle);
                                    }
                                } else {
                                    cell = row1.createCell(7);//8列
                                    if (rs.getInt("unit_no") == 1) {
                                        value = rs.getDouble("net_weight") / 1000d;
                                        cell.setCellValue((Double) value);
                                        cell.setCellStyle(rstyle1);
                                    } else if (rs.getInt("unit_no") == 2) {
                                        value = rs.getInt("net_weight");
                                        cell.setCellValue((Integer) value);
                                        cell.setCellStyle(cstyle);
                                    } else {
                                        value = rs.getInt("m3");
                                        cell.setCellValue((Integer) value);
                                        cell.setCellStyle(rstyle1);
                                    }
                                }

                                //処分終了日
                                cell = row1.createCell(8);//9列
                                value = rs.getString("cont_date2") != null && rs.getString("cont_date2").length() > 0
                                         ? sdf.format(rs.getTimestamp("cont_date2"))
                                         : "";//処分終了日
                                cell.setCellValue((String) value);
                                cell.setCellStyle(style2);

                                //処分方法
                                cell = row1.createCell(9);//10列
                                value = rs.getString("method_name1");//処分方法
                                cell.setCellValue((String) value);
                                cell.setCellStyle(rstyle4);

                                //処分量
                                if (rs.getBoolean("f按分フラグ") && Integer.valueOf(rs.getString("按分品目id")) > 0) {
                                    cell = row1.createCell(10);//11列
                                    if (rs.getInt("unit_no") == 1) {
                                        value = rs.getDouble("按分正味重量") / 1000d;
                                        cell.setCellValue((Double) value);
                                        cell.setCellStyle(rstyle1);
                                    } else {
                                        value = rs.getInt("按分正味重量");
                                        cell.setCellValue((Integer) value);
                                        cell.setCellStyle(cstyle);
                                    }

                                } else {
                                    cell = row1.createCell(10);//11列
                                    if (rs.getInt("unit_no") == 1) {
                                        value = rs.getDouble("net_weight") / 1000d;
                                        cell.setCellValue((Double) value);
                                        cell.setCellStyle(rstyle1);
                                    } else if (rs.getInt("unit_no") == 2) {
                                        value = rs.getInt("net_weight");
                                        cell.setCellValue((Integer) value);
                                        cell.setCellStyle(cstyle);
                                    } else {
                                        value = rs.getInt("m3");
                                        cell.setCellValue((Integer) value);
                                        cell.setCellStyle(rstyle1);
                                    }
                                }

                                //備考
                                cell = row1.createCell(11);//12列
                                value = rs.getString("unit_name");
                                cell.setCellValue((String) value);
                                cell.setCellStyle(rstyle4);

                                i現在行++;
                            }

                            row1 = sheet1.createRow(i現在行);//5行目スタート
                            row1.setHeightInPoints((short) 24);
                            cell = row1.createCell(0);//1列
                            cell.setCellStyle(style_blank2);
                            cell = row1.createCell(3);//4列
                            cell.setCellStyle(style_blank2);
                            cell = row1.createCell(4);//5列
                            cell.setCellStyle(style_blank2);
                            cell = row1.createCell(5);//6列
                            cell.setCellStyle(style_blank2);
                            cell = row1.createCell(6);//7列
                            cell.setCellStyle(style_blank2);

                        } catch (Exception ie1) {
                            System.out.println(ie1.toString() + String.valueOf(i現在行));
                        }

                    } finally {
                        ps.close();
                    }

                    /////収集運搬業/////////////////////////////////////////////////////////////////
                    s = new StringBuilder()
                             .append("SELECT a3.* FROM (SELECT a2.*, ")
                             .append("v0_goods.middle_goods_kind AS base_goods_id, ")
                             .append("v0_goods.middle_goods_kind_name AS base_goods_name ")
                             .append("from( ")
                             .append("SELECT a1.term_date, a1.report_no, ")
                             .append("case when a1.f按分フラグ = true then COALESCE(anbun.按分品目id, 0) ")
                             .append("else a1.goods_id end as goods_id, ")
                             .append("case when a1.f按分フラグ = true then COALESCE(anbun.按分品目名, '') ")
                             .append("else a1.goods_name end as goods_name, ")
                             .append("a1.dis_ent_id, a1.entrepreneur_name, a1.grant_date1, a1.grant_date2, ")
                             .append("a1.manifesto_no1, a1.manifesto_no2, a1.dis_cont_id, a1.contractor_name, a1.net_weight, a1.method_name1, ")
                             .append("a1.office_name, a1.office_sub_name, a1.car_no, a1.unit_no, a1.unit_name, ")
                             .append("a1.cont_date2, a1.trans1_date, a1.manifesto3_id1, a1.cont_office_name, a1.car_kind_id, a1.car_kind_name, a1.secod_manifesto_no, ")
                             .append("a1.f按分フラグ, COALESCE(anbun.按分品目id, 0) AS 按分品目id, anbun.按分品目名, anbun.按分正味重量, anbun.按分比重, anbun.按分m3 ")
                             .append("FROM(SELECT *, ")
                             .append("CASE WHEN v1_weigh_data.method_name <> ''::text AND v1_weigh_data.method_name IS NOT NULL THEN v1_weigh_data.method_name ")
                             .append("WHEN v1_weigh_data.check_flag09 = true THEN '脱水'::text ")
                             .append("WHEN v1_weigh_data.check_flag10 = true THEN '焼却'::text ")
                             .append("WHEN v1_weigh_data.check_flag11 = true THEN '破砕'::text ")
                             .append("WHEN v1_weigh_data.check_flag12 = true THEN '安定型'::text ")
                             .append("WHEN v1_weigh_data.check_flag13 = true THEN '管理型'::text ")
                             .append("WHEN v1_weigh_data.check_flag13 = true THEN '遮断型'::text ")
                             .append("ELSE ''::text END AS method_name1 ")
                             .append("FROM v1_weigh_data ")
                             .append("WHERE term_date >= ? and term_date <= ? and carry_class = 0  and goods_option_name in ('中間処分', '最終処分') ")
                             .append("AND ((? > 0 and dis_ent_id = ?) or (? = 0 and dis_ent_id > 0))) a1 ")
                             .append("LEFT JOIN  (SELECT ")
                             .append("v1_weigh_data_anbun.weight_sno, v1_weigh_data_anbun.按分品目id, ")
                             .append("v1_weigh_data_anbun.按分品目名, v1_weigh_data_anbun.按分正味重量, ")
                             .append("v1_weigh_data_anbun.按分比重, v1_weigh_data_anbun.按分m3 ")
                             .append("FROM v1_weigh_data_anbun ")
                             .append("WHERE v1_weigh_data_anbun.delete_flag <> TRUE ")
                             .append("AND v1_weigh_data_anbun.按分品目id > 0) anbun ON a1.sno = anbun.weight_sno)a2 ")
                             .append("LEFT JOIN v0_goods_kind v0_goods ON a2.goods_id = v0_goods.id)a3 ")
                             .append("WHERE a3.base_goods_id = ? ")
                             .append("ORDER BY a3.dis_ent_id, a3.term_date, a3.report_no, a3.goods_id, a3.按分品目id; ")
                             .toString();
                    ps = con.prepareStatement(s);
                    try {
                        ps.setObject(1, DBManager.convert(term_date));
                        ps.setObject(2, DBManager.convert(term_date1));

                        ps.setObject(3, ent_id);
                        ps.setObject(4, ent_id);
                        ps.setObject(5, ent_id);

                        ps.setObject(6, rs0.getInt("base_goods_id"));

                        ResultSet rs = ps.executeQuery();

                        //集計品目
                        row = sheet2.getRow(1);//2行目
                        cell = row.getCell(1);//B列
                        value = rs0.getString("base_goods_name");
                        cell.setCellValue((String) value);
                        //対象日付
                        cell = row.getCell(3);//D列
                        value = (sdf00.format(DBManager.convert(term_date)).equals(sdf00.format(DBManager.convert(term_date1)))
                                 ? sdf01.format(DBManager.convert(term_date1))
                                 : sdf00.format(DBManager.convert(term_date)) + sdf01.format(DBManager.convert(term_date1)));
                        cell.setCellValue((String) value);

                        //印刷日
                        row = sheet2.getRow(1);//2行目
                        cell = row.getCell(8);//9列
                        value = "印刷日時：" + sdf02.format(DBManager.convert((Calendar.getInstance()).getTime()));
                        cell.setCellValue((String) value);

                        int i現在行 = 5;

                        try {
                            while (rs.next()) {

                                row1 = sheet2.getRow(i現在行);//5行目スタート
                                row1.setHeightInPoints((short) 24);

                                //収集運搬終了日
                                if (rs.getTimestamp("trans1_date") != null) {
                                    cell = row1.createCell(0);//1列
                                    value = sdf.format(rs.getTimestamp("trans1_date"));
                                    cell.setCellValue((String) value);
                                }

                                //品目
                                if (rs.getBoolean("f按分フラグ") && Integer.valueOf(rs.getString("按分品目id")) > 0) {

                                    cell = row1.createCell(1);//2列
                                    value = rs.getString("按分品目名");//按分品目名
                                    cell.setCellValue((String) value);
                                    cell.setCellStyle(style);

                                } else {
                                    cell = row1.createCell(1);//2列
                                    value = rs.getString("goods_name");
                                    cell.setCellValue((String) value);
                                    cell.setCellStyle(rstyle4);

                                }

                                //車番
                                cell = row1.createCell(2);//3列
                                value = rs.getString("car_no");//車番
                                cell.setCellValue((String) value);
                                cell.setCellStyle(rstyle4);

                                //排出事業者名
                                cell = row1.createCell(3);//4列
                                value = rs.getString("entrepreneur_name");
                                cell.setCellValue((String) value);

                                //交付日（収集運搬日とする）
                                cell = row1.createCell(4);//5列
                                value = rs.getString("trans1_date") != null && rs.getString("trans1_date").length() > 0
                                         ? sdf.format(rs.getTimestamp("trans1_date"))
                                         : "";
                                cell.setCellValue((String) value);

                                //交付番号
                                cell = row1.createCell(5);//6列
                                value = rs.getString("manifesto_no1").length() > 0
                                         ? rs.getString("manifesto_no1")
                                         : rs.getString("manifesto_no2").length() > 0
                                         ? rs.getString("manifesto_no2") : "";
                                cell.setCellValue((String) value);

                                //受入先
                                cell = row1.createCell(6);//7列
                                value = rs.getString("office_sub_name").length() > 0
                                         ? rs.getString("office_sub_name")
                                         : rs.getString("office_name");//現場名　
                                cell.setCellValue((String) value);
                                row2 = sheet1.getRow(i現在行);
                                row3 = sheet1.getRow(i現在行 - 1);
                                cell2 = row2.getCell(5);//1列
                                cell3 = row3.getCell(5);//1列

                                if ((cell2.getStringCellValue()).equals(cell3.getStringCellValue())
                                         && (cell2.getStringCellValue()).length() > 0) {
                                    //前回レコードと同じならフォントカラーを白にする
                                    cell.setCellStyle(style_blank1);
                                    cell = row1.getCell(0);//1列
                                    cell.setCellStyle(style_blank1);
                                    cell = row1.getCell(3);//3列
                                    cell.setCellStyle(style_blank1);
                                    cell = row1.getCell(4);//4列
                                    cell.setCellStyle(style_blank1);
                                    cell = row1.getCell(5);//5列
                                    cell.setCellStyle(style_blank1);
                                    cell = row1.getCell(6);//6列
                                    cell.setCellStyle(style_blank1);
                                } else {
                                    cell.setCellStyle(style_blank0);
                                    cell = row1.getCell(0);//1列
                                    cell.setCellStyle(style_blank0);
                                    cell = row1.getCell(3);//3列
                                    cell.setCellStyle(style_blank0);
                                    cell = row1.getCell(4);//4列
                                    cell.setCellStyle(style_blank0);
                                    cell = row1.getCell(5);//5列
                                    cell.setCellStyle(style_blank0);
                                    cell = row1.getCell(6);//6列
                                    cell.setCellStyle(style_blank0);
                                }

                                //受入量
                                if (rs.getBoolean("f按分フラグ") && Integer.valueOf(rs.getString("按分品目id")) > 0) {
                                    cell = row1.createCell(7);//8列
                                    if (rs.getInt("unit_no") == 1) {
                                        value = rs.getDouble("按分正味重量") / 1000d;
                                        cell.setCellValue((Double) value);
                                        cell.setCellStyle(rstyle1);
                                    } else {
                                        value = rs.getInt("按分正味重量");
                                        cell.setCellValue((Integer) value);
                                        cell.setCellStyle(cstyle);
                                    }

                                } else {
                                    cell = row1.createCell(7);//8列
                                    if (rs.getInt("unit_no") == 1) {
                                        value = rs.getDouble("net_weight") / 1000d;
                                        cell.setCellValue((Double) value);
                                        cell.setCellStyle(rstyle1);
                                    } else if (rs.getInt("unit_no") == 2) {
                                        value = rs.getInt("net_weight");
                                        cell.setCellValue((Integer) value);
                                        cell.setCellStyle(cstyle);
                                    } else {
                                        value = rs.getInt("m3");
                                        cell.setCellValue((Integer) value);
                                        cell.setCellStyle(rstyle1);
                                    }
                                }

                                //運搬先
                                cell = row1.createCell(8);//9列
                                value = rs.getString("cont_office_name");
                                cell.setCellValue((String) value);
                                cell.setCellStyle(rstyle6);

                                //運搬方法
                                cell = row1.createCell(9);//10列
                                value = rs.getString("car_kind_name");
                                cell.setCellValue((String) value);
                                cell.setCellStyle(rstyle4);

                                //運搬量
                                if (rs.getBoolean("f按分フラグ") && Integer.valueOf(rs.getString("按分品目id")) > 0) {
                                    cell = row1.createCell(10);//11列
                                    if (rs.getInt("unit_no") == 1) {
                                        value = rs.getDouble("按分正味重量") / 1000d;
                                        cell.setCellValue((Double) value);
                                        cell.setCellStyle(rstyle1);
                                    } else {
                                        value = rs.getInt("按分正味重量");
                                        cell.setCellValue((Integer) value);
                                        cell.setCellStyle(cstyle);
                                    }

                                } else {
                                    cell = row1.createCell(10);//11列
                                    if (rs.getInt("unit_no") == 1) {
                                        value = rs.getDouble("net_weight") / 1000d;
                                        cell.setCellValue((Double) value);
                                        cell.setCellStyle(rstyle1);
                                    } else if (rs.getInt("unit_no") == 2) {
                                        value = rs.getInt("net_weight");
                                        cell.setCellValue((Integer) value);
                                        cell.setCellStyle(cstyle);
                                    } else {
                                        value = rs.getInt("m3");
                                        cell.setCellValue((Integer) value);
                                        cell.setCellStyle(rstyle1);
                                    }
                                }

                                //備考
                                cell = row1.createCell(11);//12列
                                value = rs.getString("unit_name");
                                cell.setCellValue((String) value);
                                cell.setCellStyle(rstyle4);

                                i現在行++;

                            }

                            row1 = sheet2.createRow(i現在行);//5行目スタート
                            row1.setHeightInPoints((short) 24);
                            cell = row1.createCell(0);//1列
                            cell.setCellStyle(style_blank2);
                            cell = row1.createCell(3);//4列
                            cell.setCellStyle(style_blank2);
                            cell = row1.createCell(4);//5列
                            cell.setCellStyle(style_blank2);
                            cell = row1.createCell(5);//6列
                            cell.setCellStyle(style_blank2);
                            cell = row1.createCell(6);//7列
                            cell.setCellStyle(style_blank2);

                        } catch (Exception ie1) {
                            System.out.println(ie1.toString() + String.valueOf(i現在行));
                        }

                    } finally {
                        ps.close();
                    }

                }
            } finally {
                ps0.close();
            }

            wb1.getSheetAt(2).setSelected(true);
            wb1.getSheetAt(2).setForceFormulaRecalculation(true);

            //テンプレートシート削除
            wb1.removeSheetAt(wb1.getSheetIndex("print1"));
            wb1.removeSheetAt(wb1.getSheetIndex("print2"));
            //すべてのシートを選択
            for (int i = 0; i < wb1.getNumberOfSheets(); i++) {
                wb1.getSheetAt(i).setSelected(true);
            }

            try {
                //自身のtmpに保存
                out1 = new FileOutputStream(storeFol);
                wb1.write(out1);
                //スクリプトから印刷
                print_out = new File(storeFol);
                doPrintXlsx(print_out.getAbsolutePath(), printer[1], 1, true);

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

    //業者別搬出品目集計表
    private void printReport6(
             String file_path,
             String storeFol,
             Connection con,
             Calendar term_date,
             Calendar term_date1) {

        try {
            InputStream in1 = new FileInputStream(file_path);
            wb1 = WorkbookFactory.create(in1);
            initCom();
            Sheet sheet1 = wb1.getSheet("print");

            SimpleDateFormat sdf00 = new SimpleDateFormat("yyyy年MM月dd日～");
            SimpleDateFormat sdf01 = new SimpleDateFormat("yyyy年MM月dd日");
            SimpleDateFormat sdf02 = new SimpleDateFormat("yyyy/MM/dd HH:mm");

            FileOutputStream out1 = null;
            File print_out = null;

            Row row, row1, row2, row3;
            Cell cell, cell2, cell3;
            Object value = null;

            String[] printer = getPrinter();

            /////////////////////////////////////////////////////////////////////////////////
            String s = new StringBuilder()
                     .append("SELECT count(*) AS cnt, max(dis_ent_id) AS dis_ent_id, ")
                     .append("max(entrepreneur_name) AS entrepreneur_name, ")
                     .append("max(dis_office_id) AS dis_office_id, COALESCE(max(office_name), '') AS office_name, max(office_sub_name) AS office_sub_name, ")
                     .append("max(car_no) AS car_no, ")
                     .append("max(goods_id) AS goods_id, ")
                     .append("max(goods_name) AS goods_name, max(goods_option_name) AS goods_op_name, max(trans_id) AS trans_id, ")
                     .append("sum(net_weight_ton) AS net_weight_ton, sum(m3) AS m3, bool_or(a1.f按分フラグ) AS f按分フラグ, COALESCE(anbun.按分品目id, 0) AS 按分品目id, ")
                     .append("max(anbun.按分品目名) AS 按分品目名, sum(anbun.按分正味重量) AS 按分正味重量, sum(anbun.按分m3) AS 按分m3 ")
                     .append("FROM (SELECT * FROM v1_weigh_data ")
                     .append("WHERE term_date >= ? and term_date <= ? and carry_class = 1 ) a1 ")
                     .append("LEFT JOIN (SELECT ")
                     .append("v1_weigh_data_anbun.weight_sno, v1_weigh_data_anbun.按分品目id, ")
                     .append("v1_weigh_data_anbun.按分品目名, v1_weigh_data_anbun.按分正味重量, ")
                     .append("v1_weigh_data_anbun.按分比重, v1_weigh_data_anbun.按分m3 ")
                     .append("FROM v1_weigh_data_anbun ")
                     .append("WHERE v1_weigh_data_anbun.delete_flag <> TRUE ")
                     .append("AND v1_weigh_data_anbun.按分品目id > 0) anbun ON a1.sno = anbun.weight_sno ")
                     .append("GROUP BY dis_ent_id, dis_office_id, office_sub_name, car_no, goods_id, trans_id, goods_option_name, 按分品目id ")
                     .append("ORDER BY dis_ent_id, dis_office_id, office_sub_name, car_no, goods_id, trans_id, goods_option_name, 按分品目id ;")
                     .toString();
            PreparedStatement ps = con.prepareStatement(s);
            try {
                ps.setObject(1, DBManager.convert(term_date));
                ps.setObject(2, DBManager.convert(term_date1));
                ResultSet rs = ps.executeQuery();

                //対象日付
                row = sheet1.getRow(1);//2行目
                cell = row.getCell(0);//A列
                value = (sdf00.format(DBManager.convert(term_date)).equals(sdf00.format(DBManager.convert(term_date1)))
                         ? sdf01.format(DBManager.convert(term_date1))
                         : sdf00.format(DBManager.convert(term_date)) + sdf01.format(DBManager.convert(term_date1)));
                cell.setCellValue((String) value);

                //印刷日
                row = sheet1.getRow(1);//2行目
                cell = row.getCell(9);//10列
                value = "印刷日時：" + sdf02.format(DBManager.convert((Calendar.getInstance()).getTime()));
                cell.setCellValue((String) value);

                int i現在行 = 4;
                boolean b前回運搬業者 = false;

                try {
                    while (rs.next()) {

                        row1 = sheet1.createRow(i現在行);//5行目スタート
                        row1.setHeightInPoints(15);//行幅を15に指定

                        cell = row1.createCell(0);//1列
                        value = rs.getString("dis_ent_id");//業者コード
                        cell.setCellValue((String) value);
                        row2 = sheet1.getRow(i現在行);
                        row3 = sheet1.getRow(i現在行 - 1);
                        cell2 = row2.getCell(0);//1列
                        cell3 = row3.getCell(0);//1列

                        if ((cell2.getStringCellValue()).equals(cell3.getStringCellValue())) {
                            //前回レコードと同じならフォントカラーを白にする
                            cell.setCellStyle(style_blank1);
                            b前回運搬業者 = false;
                        } else {
                            cell.setCellStyle(style_blank0);
                            b前回運搬業者 = true;
                        }

                        cell = row1.createCell(1);//2列
                        value = rs.getString("entrepreneur_name");//業者名
                        cell.setCellValue((String) value);
                        cell2 = row2.getCell(1);//1列
                        cell3 = row3.getCell(1);//1列

                        if ((cell2.getStringCellValue()).equals(cell3.getStringCellValue()) && !b前回運搬業者) {
                            //前回レコードと同じならフォントカラーを白にする
                            cell.setCellStyle(style_blank1);
                        } else {
                            cell.setCellStyle(style_blank0);
                        }

                        //品目
                        if (rs.getBoolean("f按分フラグ") && Integer.valueOf(rs.getString("按分品目id")) > 0) {

                            cell = row1.createCell(2);//3列
                            value = rs.getString("按分品目id");//按分品目id　　
                            cell.setCellValue((String) value);
                            cell2 = row2.getCell(2);//1列
                            cell3 = row3.getCell(2);//1列

                            if ((cell2.getStringCellValue()).equals(cell3.getStringCellValue()) && !b前回運搬業者) {
                                //前回レコードと同じならフォントカラーを白にする
                                cell.setCellStyle(style_blank1);
                            } else {
                                cell.setCellStyle(style_blank0);
                            }

                            cell = row1.createCell(3);//4列
                            value = rs.getString("按分品目名");//按分品目名
                            cell.setCellValue((String) value);
                            cell2 = row2.getCell(3);//1列
                            cell3 = row3.getCell(3);//1列

                            if ((cell2.getStringCellValue()).equals(cell3.getStringCellValue()) && !b前回運搬業者) {
                                //前回レコードと同じならフォントカラーを白にする
                                cell.setCellStyle(style_blank1);
                            } else {
                                cell.setCellStyle(style_blank0);
                            }

                        } else {
                            cell = row1.createCell(2);//3列
                            value = rs.getString("goods_id");//商品コード　　
                            cell.setCellValue((String) value);
                            cell2 = row2.getCell(2);//1列
                            cell3 = row3.getCell(2);//1列

                            if ((cell2.getStringCellValue()).equals(cell3.getStringCellValue()) && !b前回運搬業者) {
                                //前回レコードと同じならフォントカラーを白にする
                                cell.setCellStyle(style_blank1);
                            } else {
                                cell.setCellStyle(style_blank0);
                            }

                            cell = row1.createCell(3);//4列
                            value = rs.getString("goods_name");//商品名
                            cell.setCellValue((String) value);
                            cell2 = row2.getCell(3);//1列
                            cell3 = row3.getCell(3);//1列

                            if ((cell2.getStringCellValue()).equals(cell3.getStringCellValue()) && !b前回運搬業者) {
                                //前回レコードと同じならフォントカラーを白にする
                                cell.setCellStyle(style_blank1);
                            } else {
                                cell.setCellStyle(style_blank0);
                            }

                        }

                        cell = row1.createCell(4);//5列
                        value = rs.getString("dis_office_id");//現場コード　　
                        cell.setCellValue((String) value);
                        cell.setCellStyle(style2);

                        cell = row1.createCell(5);//6列
                        value = rs.getString("office_sub_name").length() > 0
                                 ? rs.getString("office_sub_name")
                                 : rs.getString("office_name");//現場名　
                        cell.setCellValue((String) value);
                        cell.setCellStyle(style);

                        cell = row1.createCell(6);//7列
                        value = rs.getString("car_no");//車番　　
                        cell.setCellValue((String) value);
                        cell.setCellStyle(style2);

                        cell = row1.createCell(7);//8列
                        //運搬業者コード=1 なら　持込　その他業者なら引取　
                        value = (rs.getInt("trans_id") == 1 ? "持込" : "引取");
                        cell.setCellValue((String) value);
                        cell.setCellStyle(style2);

                        //搬出区分
                        cell = row1.createCell(8);//9列
                        value = rs.getString("goods_op_name");
                        cell.setCellValue((String) value);
                        cell.setCellStyle(style2);

                        //件数
                        cell = row1.createCell(9);//10列
                        value = rs.getInt("cnt");
                        cell.setCellValue((Integer) value);
                        cell.setCellStyle(cstyle);

                        //正味
                        if (rs.getBoolean("f按分フラグ") && Integer.valueOf(rs.getString("按分品目id")) > 0) {

                            cell = row1.createCell(10);//11列
                            value = rs.getDouble("按分正味重量") / 1000d;//按分正味重量 ｔ　スパット北上
                            cell.setCellValue((Double) value);
                            cell.setCellStyle(rstyle1);
                            //m3
                            cell = row1.createCell(11);//12列
                            value = rs.getDouble("按分m3");// m3
                            if ((Double) value > 0.) {
                                cell.setCellValue((Double) value);
                            }
                            cell.setCellStyle(rstyle1);

                        } else {
                            cell = row1.createCell(10);//11列
                            value = rs.getDouble("net_weight_ton");//正味重量(t) 20161131 スパット北上
                            cell.setCellValue((Double) value);
                            cell.setCellStyle(rstyle1);
                            //m3
                            cell = row1.createCell(11);//12列
                            value = rs.getDouble("m3");//受託量 m3
                            if ((Double) value > 0.) {
                                cell.setCellValue((Double) value);
                            }
                            cell.setCellStyle(rstyle1);
                        }

                        i現在行++;
                    }

                    row1 = sheet1.createRow(i現在行);//5行目スタート
                    cell = row1.createCell(0);//1列
                    cell.setCellStyle(style_blank2);
                    cell = row1.createCell(1);//2列
                    cell.setCellStyle(style_blank2);
                    cell = row1.createCell(2);//3列
                    cell.setCellStyle(style_blank2);
                    cell = row1.createCell(3);//4列
                    cell.setCellStyle(style_blank2);

                } catch (Exception ie1) {
                    System.out.println(ie1.toString() + String.valueOf(i現在行));
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
                doPrintXlsx(print_out.getAbsolutePath(), printer[1], 1, true);

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
