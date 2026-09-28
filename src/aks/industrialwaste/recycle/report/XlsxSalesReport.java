package aks.industrialwaste.recycle.report;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;
import aks.industrialwaste.database.DBManager;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.channels.FileChannel;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import javax.swing.JOptionPane;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DataFormat;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

public class XlsxSalesReport extends OSCReport {

    public static final int SALES_REPORT1 = 1;//売上高実績一覧
    public static final int SALES_REPORT2 = 2;//得意先別買掛計算書
    public static final int SALES_REPORT3 = 3;//入金明細表
    public static final int SALES_REPORT4 = 4;//得意先別実績
    public static final int SALES_REPORT5 = 5;//排出業者別実績
    public static final int SALES_REPORT6 = 6;//運搬業者別実績

    private Connection con_;

    private CellStyle style, cstyle, rstyle2, rstyle1, dstyle2, //下線無し
             style_, cstyle_, rstyle2_, rstyle1_, dstyle2_ // 下線あり 
             ;
    private Workbook wb1;
    private Font font, font_blank;
    private DataFormat format;

    public XlsxSalesReport() {

        this(SALES_REPORT1);
    }

    public XlsxSalesReport(int reportID) {
        if ((SALES_REPORT1 != reportID)
                 && (SALES_REPORT2 != reportID)
                 && (SALES_REPORT3 != reportID)
                 && (SALES_REPORT4 != reportID)
                 && (SALES_REPORT5 != reportID)
                 && (SALES_REPORT6 != reportID)) {
            throw new IllegalArgumentException("'reportID' is not match.");
        }
    }

    private void initCom() {
        font = wb1.createFont();
        font.setFontName("メイリオ");
        font.setFontHeightInPoints((short) 9);

        font_blank = wb1.createFont();
        font_blank.setFontName("メイリオ");
        font_blank.setFontHeightInPoints((short) 9);
        font_blank.setColor(IndexedColors.WHITE.getIndex());

        format = wb1.createDataFormat();

        style = wb1.createCellStyle();
        style.setBorderLeft(CellStyle.BORDER_NONE);
        style.setBorderRight(CellStyle.BORDER_NONE);
        style.setBorderTop(CellStyle.BORDER_NONE);
        style.setBorderBottom(CellStyle.BORDER_NONE);
        style.setShrinkToFit(true);
        style.setFont(font);

        style_ = wb1.createCellStyle();
        style_.setBorderLeft(CellStyle.BORDER_NONE);
        style_.setBorderRight(CellStyle.BORDER_NONE);
        style_.setBorderTop(CellStyle.BORDER_NONE);
        style_.setBorderBottom(CellStyle.BORDER_THIN);
        style_.setShrinkToFit(true);
        style_.setFont(font);

        dstyle2 = wb1.createCellStyle();
        dstyle2.setDataFormat(format.getFormat("MM/dd"));
        dstyle2.setFont(font);
        dstyle2.setBorderLeft(CellStyle.BORDER_NONE);
        dstyle2.setBorderRight(CellStyle.BORDER_NONE);
        dstyle2.setBorderTop(CellStyle.BORDER_NONE);
        dstyle2.setBorderBottom(CellStyle.BORDER_NONE);
        dstyle2.setAlignment(CellStyle.ALIGN_CENTER);

        dstyle2_ = wb1.createCellStyle();
        dstyle2_.setDataFormat(format.getFormat("yy/MM/dd"));
        dstyle2_.setFont(font);
        dstyle2_.setBorderLeft(CellStyle.BORDER_NONE);
        dstyle2_.setBorderRight(CellStyle.BORDER_NONE);
        dstyle2_.setBorderTop(CellStyle.BORDER_NONE);
        dstyle2_.setBorderBottom(CellStyle.BORDER_THIN);
        dstyle2_.setAlignment(CellStyle.ALIGN_CENTER);

        cstyle = wb1.createCellStyle();
        cstyle.setBorderLeft(CellStyle.BORDER_NONE);
        cstyle.setBorderRight(CellStyle.BORDER_NONE);
        cstyle.setBorderTop(CellStyle.BORDER_NONE);
        cstyle.setBorderBottom(CellStyle.BORDER_NONE);
        cstyle.setAlignment(CellStyle.ALIGN_RIGHT);
        cstyle.setDataFormat(format.getFormat("#,##0"));
        cstyle.setFont(font);

        cstyle_ = wb1.createCellStyle();
        cstyle_.setBorderLeft(CellStyle.BORDER_NONE);
        cstyle_.setBorderRight(CellStyle.BORDER_NONE);
        cstyle_.setBorderTop(CellStyle.BORDER_NONE);
        cstyle_.setBorderBottom(CellStyle.BORDER_THIN);
        cstyle_.setAlignment(CellStyle.ALIGN_RIGHT);
        cstyle_.setDataFormat(format.getFormat("#,##0"));
        cstyle_.setFont(font);

        rstyle2 = wb1.createCellStyle();
        rstyle2.setBorderLeft(CellStyle.BORDER_NONE);
        rstyle2.setBorderRight(CellStyle.BORDER_NONE);
        rstyle2.setBorderTop(CellStyle.BORDER_NONE);
        rstyle2.setBorderBottom(CellStyle.BORDER_NONE);
        rstyle2.setAlignment(CellStyle.ALIGN_RIGHT);
        rstyle2.setDataFormat(format.getFormat("###0"));
        rstyle2.setFont(font);

        rstyle2_ = wb1.createCellStyle();
        rstyle2_.setBorderLeft(CellStyle.BORDER_NONE);
        rstyle2_.setBorderRight(CellStyle.BORDER_NONE);
        rstyle2_.setBorderTop(CellStyle.BORDER_NONE);
        rstyle2_.setBorderBottom(CellStyle.BORDER_THIN);
        rstyle2_.setAlignment(CellStyle.ALIGN_RIGHT);
        rstyle2_.setDataFormat(format.getFormat("###0"));
        rstyle2_.setFont(font);

        rstyle1 = wb1.createCellStyle();
        rstyle1.setBorderLeft(CellStyle.BORDER_NONE);
        rstyle1.setBorderRight(CellStyle.BORDER_NONE);
        rstyle1.setBorderTop(CellStyle.BORDER_NONE);
        rstyle1.setBorderBottom(CellStyle.BORDER_NONE);
        rstyle1.setAlignment(CellStyle.ALIGN_RIGHT);
        rstyle1.setDataFormat(format.getFormat("###0.00"));
        rstyle1.setFont(font);

        rstyle1_ = wb1.createCellStyle();
        rstyle1_.setBorderLeft(CellStyle.BORDER_NONE);
        rstyle1_.setBorderRight(CellStyle.BORDER_NONE);
        rstyle1_.setBorderTop(CellStyle.BORDER_NONE);
        rstyle1_.setBorderBottom(CellStyle.BORDER_THIN);
        rstyle1_.setAlignment(CellStyle.ALIGN_RIGHT);
        rstyle1_.setDataFormat(format.getFormat("###0.00"));
        rstyle1_.setFont(font);

    }

    /**
     * 指定条件で帳票を印刷する.
     *
     * @param con データベースとの接続
     * @param path 保存先ファイルのフルパス
     * @param report_sno 対象の報告
     * @param term_date 対象年
     */
    public int print(Connection con, int report_id, String path, Calendar term_date1, Calendar term_date2, int tax_, int 締め日_) {
        con_ = con;
        String template = "";

        try {

            if (SALES_REPORT1 == report_id) {
                //売上高実績一覧
                template = "template\\売上高実績一覧表.xls";
            } else if (SALES_REPORT2 == report_id) {
                //売上明細表
                template = "template\\売上高明細表.xlsx";
            } else if (SALES_REPORT3 == report_id) {
                //入金明細表
                template = "template\\入金明細表.xlsx";
            } else if (SALES_REPORT4 == report_id) {
                //売上高実績一覧
                template = "template\\売上高実績一覧表.xls";
            } else if (SALES_REPORT5 == report_id) {
                //売上高実績一覧
                template = "template\\売上高実績一覧表.xls";
            } else if (SALES_REPORT6 == report_id) {
                //売上高実績一覧
                template = "template\\売上高実績一覧表.xls";
            }

            // テンプレートファイルをローカルにコピー
            File templateFile = new File(template);
            File tmpFile = new File("tmp", templateFile.getName());

            // テンプレートファイルをローカルにコピー
            try {
                copyTransfer(templateFile.getPath(), tmpFile.getPath());
            } catch (FileNotFoundException fe) {
                fe.printStackTrace();
                JOptionPane.showMessageDialog(
                         null,
                         "ファイルが既に開かれています。",
                         "確認",
                         JOptionPane.WARNING_MESSAGE);
                return -1;
            }

            // データ出力
            try {

                switch (report_id) {
                    case SALES_REPORT1:
                        //売上高実績一覧 bill_id->taxとして利用する
                        printReport1(tmpFile.getPath(), tmpFile.getName(), path, con_, term_date1, term_date2, tax_);
                        break;
                    case SALES_REPORT2:
                        //売上明細表
                        printReport2(tmpFile.getPath(), tmpFile.getName(), path, con_, term_date1, term_date2, tax_, 締め日_);
                        break;

                    case SALES_REPORT3:
                        //入金明細表
                        printReport3(tmpFile.getPath(), tmpFile.getName(), path, con_, term_date1, term_date2, tax_);
                        break;

                    case SALES_REPORT4:
                        //売上高実績一覧 bill_id->taxとして利用する
                        printReport1new(tmpFile.getPath(), tmpFile.getName(), path, con_, term_date1, term_date2, tax_, 1);
                        break;

                    case SALES_REPORT5:
                        //売上高実績一覧 bill_id->taxとして利用する
                        printReport1new(tmpFile.getPath(), tmpFile.getName(), path, con_, term_date1, term_date2, tax_, 2);
                        break;

                    case SALES_REPORT6:
                        //売上高実績一覧 bill_id->taxとして利用する
                        printReport1new(tmpFile.getPath(), tmpFile.getName(), path, con_, term_date1, term_date2, tax_, 3);
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

    //印刷処理
    public static void doPrintXlsx(String file_path, String printer, int papers, boolean preview) {
        try {
            if (!preview) {
                // 直接印刷
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
                // プレビュー：Excelファイルを開くだけ
                runCmd("cmd", "/c", "start", "", file_path);
            }

        } catch (IOException | InterruptedException ev) {
            System.out.println("失敗: " + ev);
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

    //売上高実績一覧
    private Boolean printReport1(
             String file_path,
             String file_name,
             String storeFol,
             Connection con,
             Calendar term_date1,
             Calendar term_date2,
             int tax) {

        boolean ret = false;
        int cnt00 = 0;
        try {
            InputStream in1 = new FileInputStream(file_path);
            wb1 = WorkbookFactory.create(in1);
            initCom();
            Sheet sheet1 = wb1.getSheet("print");

            SimpleDateFormat sdf00 = new SimpleDateFormat("yyyy年MM月dd日～");
            SimpleDateFormat sdf01 = new SimpleDateFormat("yyyy年MM月dd日 分");
            SimpleDateFormat sdf02 = new SimpleDateFormat("yyyy/MM/dd HH:mm");

            FileOutputStream out1 = null;
            File print_out = null;

            Row row, row1;
            Cell cell;
            Object value = null;
            String[] printer = getPrinter();
            int i合計金額 = 0;
            int i消費税合計 = 0;
            int i料金合計 = 0;

            /////////////////////////////////////////////////////////////////////////////////
            String s = new StringBuilder()
                     .append("SELECT * FROM f1_得意先売上高一覧(?, ?, ?) ")
                     .append("ORDER BY bill_id;")
                     .toString();

            PreparedStatement ps = con.prepareStatement(s);
            try {
                ps.setObject(1, DBManager.convert(term_date1));
                ps.setObject(2, DBManager.convert(term_date2));
                ps.setObject(3, tax);
                ResultSet rs = ps.executeQuery();

                //対象日付
                row = sheet1.getRow(1);//2行目
                cell = row.getCell(0);//A列
                value = (sdf00.format(DBManager.convert(term_date1)).equals(sdf00.format(DBManager.convert(term_date2)))
                         ? sdf01.format(DBManager.convert(term_date1))
                         : sdf00.format(DBManager.convert(term_date1)) + sdf01.format(DBManager.convert(term_date2)));
                cell.setCellValue((String) value);

                //印刷日
                row = sheet1.getRow(1);//2行目
                cell = row.getCell(4);//E列
                value = "印刷日時：" + sdf02.format(DBManager.convert((Calendar.getInstance()).getTime()));
                cell.setCellValue((String) value);

                //消費税率
                row = sheet1.getRow(2);//3行目
                cell = row.getCell(5);//F列
                value = tax;
                cell.setCellValue((Integer) value);

                int i現在行 = 4;
                try {
                    while (rs.next()) {

                        row1 = sheet1.createRow(i現在行);//5行目スタート
                        row1.setHeightInPoints(15);//行幅を15に指定
                        cell = row1.createCell(0);//A列
                        value = rs.getInt("bill_id");//bill_id
                        cell.setCellValue((Integer) value);
                        //3桁カンマ無し
                        cell.setCellStyle((i現在行 - 3) % 5 != 0 ? rstyle2 : rstyle2_);

                        cell = row1.createCell(1);//B列
                        cell.setCellStyle((i現在行 - 3) % 5 != 0 ? style : style_);//左寄せテキスト

                        cell = row1.createCell(2);//C列
                        value = rs.getString("bill_name");//排出業者
                        cell.setCellValue((String) value);
                        cell.setCellStyle((i現在行 - 3) % 5 != 0 ? style : style_);//左寄せテキスト

                        cell = row1.createCell(3);//D列
                        cell.setCellStyle((i現在行 - 3) % 5 != 0 ? style : style_);//左寄せテキスト

                        cell = row1.createCell(4);//E列
                        value = rs.getInt("sum_charge_value");//合計金額
                        cell.setCellValue((Integer) value);
                        cell.setCellStyle((i現在行 - 3) % 5 != 0 ? cstyle : cstyle_);//3桁カンマあり
                        i合計金額 += (Integer) value;

                        cell = row1.createCell(5);//E列
                        value = rs.getInt("sum_tax_value");//消費税
                        cell.setCellValue((Integer) value);
                        cell.setCellStyle((i現在行 - 3) % 5 != 0 ? cstyle : cstyle_);//3桁カンマあり
                        i消費税合計 += (Integer) value;

                        cell = row1.createCell(6);//E列
                        value = rs.getInt("sum_charge_tax_value");//料金合計
                        cell.setCellValue((Integer) value);
                        cell.setCellStyle((i現在行 - 3) % 5 != 0 ? cstyle : cstyle_);//3桁カンマあり
                        i料金合計 += (Integer) value;

                        i現在行++;
                    }
                    //最終行に合計金額
                    i現在行++;
                    row1 = sheet1.createRow(i現在行);//5行目スタート
                    row1.setHeightInPoints(15);//行幅を15に指定
                    cell = row1.createCell(3);//D列
                    value = "合計";
                    cell.setCellValue((String) value);
                    cell.setCellStyle(style_);//左寄せテキスト
                    cell = row1.createCell(4);//E列
                    cell.setCellValue(i合計金額);
                    cell.setCellStyle(cstyle_);//3桁カンマあり
                    cell = row1.createCell(5);//F列
                    cell.setCellValue(i消費税合計);
                    cell.setCellStyle(cstyle_);//3桁カンマあり
                    cell = row1.createCell(6);//G列
                    cell.setCellValue(i料金合計);
                    cell.setCellStyle(cstyle_);//3桁カンマあり

                } catch (Exception ie1) {
                    System.out.println(ie1.toString() + String.valueOf(i現在行));
                }

            } finally {
                ps.close();
            }

            wb1.getCreationHelper().createFormulaEvaluator().evaluateAll();
            sheet1.setSelected(true);

            try {
                //自身のtmpに保存
                out1 = new FileOutputStream(storeFol);
                wb1.write(out1);
                //スクリプトから印刷
                print_out = new File(storeFol);
                doPrintXlsx(print_out.getAbsolutePath(), printer[1], 1, true);

            } catch (FileNotFoundException fe) {
                JOptionPane.showMessageDialog(
                         null,
                         "ファイルが既に開かれています。",
                         "確認",
                         JOptionPane.WARNING_MESSAGE);
            } catch (IOException e) {
                System.out.println(e.toString());
            } finally {
                try {
                    if (out1 != null) {
                        out1.close();
                    }

                } catch (IOException e) {
                    System.out.println(e.toString());
                }
            }

        } catch (Exception ie1) {
            System.out.println(ie1.toString() + String.valueOf(cnt00));
        }

        return ret;
    }

    //業者別受入集計表　得意先別
    private Boolean printReport2(
             String file_path,
             String file_name,
             String storeFol,
             Connection con,
             Calendar term_date1,
             Calendar term_date2,
             int tax_,
             int 締め日_) {

        boolean ret = false;
        int cnt00 = 0;
        try {
            InputStream in1 = new FileInputStream(file_path);
            wb1 = WorkbookFactory.create(in1);
            initCom();
            Sheet sheet1;

            SimpleDateFormat sdf = new SimpleDateFormat("MM/dd");
            SimpleDateFormat sdf00 = new SimpleDateFormat("yyyy年MM月dd日～");
            SimpleDateFormat sdf01 = new SimpleDateFormat("yyyy年MM月dd日 分");

            FileOutputStream out1 = null;
            File print_out = null;

            Row row, row1;
            Cell cell;
            Object value = null;
            String[] printer = getPrinter();

            int v現在ページcnt = 1;

            /////////////////////////////////////////////////////////////////////////////////
            String s = new StringBuilder()
                     .append("SELECT * FROM f1_得意先売上高一覧(?, ?, ?, ?) where option2 = 0 ")
                     .append("ORDER BY bill_id;")
                     .toString();

            PreparedStatement ps = con.prepareStatement(s);
            try {
                ps.setObject(1, DBManager.convert(term_date1));
                ps.setObject(2, DBManager.convert(term_date2));
                ps.setObject(3, tax_);
                ps.setObject(4, 締め日_);
                ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                    System.out.print("印刷ページ:" + v現在ページcnt + "\r\n");
                    wb1.cloneSheet(0);
                    sheet1 = wb1.getSheetAt(0 + v現在ページcnt);

                    //対象日付
                    row = sheet1.getRow(1);//2行目
                    cell = row.getCell(7);//H列
                    value = (sdf00.format(DBManager.convert(term_date1)).equals(sdf00.format(DBManager.convert(term_date2)))
                             ? sdf01.format(DBManager.convert(term_date1))
                             : sdf00.format(DBManager.convert(term_date1)) + sdf01.format(DBManager.convert(term_date2)));
                    cell.setCellValue((String) value);

                    row = sheet1.getRow(1);//2行目
                    cell = row.getCell(0);//A列
                    value = rs.getString("p_name") + "　　" + rs.getString("keisyo");//
                    cell.setCellValue((String) value);

                    //シート名の設定
                    wb1.setSheetName(0 + v現在ページcnt, String.valueOf(rs.getInt("bill_id")));

                    String ss = new StringBuilder()
                             .append("SELECT * FROM f1_得意先売上明細表(?, ?, ?) where option2 = 0 ")
                             .append("ORDER BY dis_ent_id, dis_office_id, term_date, gate_out_date ;")
                             .toString();

                    PreparedStatement psd = con.prepareStatement(ss);

                    try {

                        psd.setObject(1, DBManager.convert(term_date1));
                        psd.setObject(2, DBManager.convert(term_date2));
                        psd.setObject(3, rs.getInt("bill_id"));
                        ResultSet rsd = psd.executeQuery();
                        int i現在行 = 4;
                        while (rsd.next()) {

                            row1 = sheet1.createRow(i現在行);//5行目スタート
                            row1.setHeightInPoints(15);//行幅を15に指定

                            cell = row1.createCell(0);//A列
                            value = sdf.format(rsd.getDate("term_date"));//計量日
                            cell.setCellValue((String) value);
                            cell.setCellStyle((i現在行 - 5) % 5 != 0 ? dstyle2 : dstyle2);

                            cell = row1.createCell(1);//B列
                            value = rsd.getInt("car_no");//
                            cell.setCellValue((Integer) value);
                            cell.setCellStyle((i現在行 - 7) % 5 != 0 ? rstyle2 : rstyle2);

                            cell = row1.createCell(2);//C列
                            value = rsd.getString("office_name");//
                            cell.setCellValue((String) value);
                            cell.setCellStyle((i現在行 - 5) % 5 != 0 ? style : style);

                            cell = row1.createCell(3);//D列
                            value = rsd.getString("goods_name");//
                            cell.setCellValue((String) value);
                            cell.setCellStyle((i現在行 - 5) % 5 != 0 ? style : style);

                            cell = row1.createCell(4);//E列
                            value = rsd.getInt("net_weight");//net_weight
                            cell.setCellValue((Integer) value);
//                            cell.setCellStyle(cstyle);//3桁カンマあり
                            cell.setCellStyle((i現在行 - 5) % 5 != 0 ? cstyle : cstyle);

                            cell = row1.createCell(5);//F列
                            value = rsd.getDouble("m3");//数量
                            if ((Double) value != 0.) {
                                cell.setCellValue((Double) value);
                            }
                            cell.setCellStyle((i現在行 - 7) % 5 != 0 ? rstyle1 : rstyle1);

                            cell = row1.createCell(6);//G列
                            value = rsd.getString("unit_name");//
                            cell.setCellValue((String) value);
                            cell.setCellStyle((i現在行 - 5) % 5 != 0 ? style : style);

                            cell = row1.createCell(7);//H列
                            value = rsd.getInt("unit");//unit
                            if ((Integer) value != 0) {
                                cell.setCellValue((Integer) value);
                            }
                            cell.setCellStyle((i現在行 - 5) % 5 != 0 ? cstyle : cstyle);

                            cell = row1.createCell(8);//I列
                            value = rsd.getInt("charge_value");//charge_value
                            if ((Integer) value != 0) {
                                cell.setCellValue((Integer) value);
                            }
                            cell.setCellStyle((i現在行 - 5) % 5 != 0 ? cstyle : cstyle);

                            cell = row1.createCell(9);//J列
                            value = rsd.getString("manifesto_no");//
                            cell.setCellValue((String) value);
                            cell.setCellStyle((i現在行 - 7) % 5 != 0 ? rstyle2 : rstyle2);

                            i現在行++;
                        }
                    } finally {
                        psd.close();
                    }
                    v現在ページcnt++;
                }
            } finally {
                ps.close();
            }

            wb1.getCreationHelper().createFormulaEvaluator().evaluateAll();
            //テンプレートシート削除
            wb1.removeSheetAt(0);
            try {
                //自身のtmpに保存
                out1 = new FileOutputStream(storeFol);
                wb1.write(out1);
                //スクリプトから印刷
                print_out = new File(storeFol);
                doPrintXlsx(print_out.getAbsolutePath(), printer[1], 1, true);

            } catch (FileNotFoundException fe) {
                JOptionPane.showMessageDialog(
                         null,
                         "ファイルが既に開かれています。",
                         "確認",
                         JOptionPane.WARNING_MESSAGE);
            } catch (IOException e) {
                System.out.println(e.toString());
            } finally {
                try {
                    if (out1 != null) {
                        out1.close();
                    }

                } catch (IOException e) {
                    System.out.println(e.toString());
                }
            }

        } catch (Exception ie1) {
            System.out.println(ie1.toString() + String.valueOf(cnt00));
        }

        return ret;
    }

    //入金明細出力
    private Boolean printReport3(
             String file_path,
             String file_name,
             String storeFol,
             Connection con,
             Calendar term_date,
             Calendar term_date1,
             int tax_) {

        boolean ret = false;
        int cnt00 = 0;
        try {
            InputStream in1 = new FileInputStream(file_path);
            wb1 = WorkbookFactory.create(in1);
            initCom();
            Sheet sheet1 = wb1.getSheet("print");

            SimpleDateFormat sdf00 = new SimpleDateFormat("yyyy年MM月dd日～");
            SimpleDateFormat sdf01 = new SimpleDateFormat("yyyy年MM月dd日");
            SimpleDateFormat sdf02 = new SimpleDateFormat("yyyy/MM/dd HH:mm");
            SimpleDateFormat sdf03 = new SimpleDateFormat("yy/MM/dd HH:mm");

            FileOutputStream out1 = null;
            File print_out = null;

            Row row, row1;
            Cell cell;
            Object value = null;
            String[] printer = getPrinter();
            int i合計金額 = 0;

            /////////////////////////////////////////////////////////////////////////////////
            String s = new StringBuilder()
                     .append("SELECT a1.gate_out_date, a1.report_no, a1.entrepreneur_name, a1.car_no, ")
                     .append("a1.goods_name, a1.net_weight, a1.transporter_name, a1.office_name, a1.contractor_name, ")
                     .append("a1.manifesto_no1, a1.manifesto_no2, a1.m3, a1.bill_name, a1.unit, a1.charge_value, ")
                     .append("case when a1.tax_no = 0 then round((? * (a1.charge_value)::integer)/100::numeric)::integer ")
                     .append(" when a1.tax_no = 1 then trunc((? * (a1.charge_value)::integer)/100::integer)::integer ")
                     .append(" when a1.tax_no = 2 then trunc(((? * (a1.charge_value)::integer)/100::numeric)+0.9)::integer ")
                     .append(" end as tax_value, ")
                     .append("case when a1.tax_no = 0 then a1.charge_value + round((? * (a1.charge_value)::integer)/100::numeric)::integer ")
                     .append(" when a1.tax_no = 1 then a1.charge_value + trunc((? * (a1.charge_value)::integer)/100::integer)::integer ")
                     .append(" when a1.tax_no = 2 then a1.charge_value + trunc(((? * (a1.charge_value)::integer)/100::numeric)+0.9)::integer ")
                     .append(" end as charge_tax_value, ")
                     .append("a1.office_sub_name, a1.option2, ")
                     .append("a1.f按分フラグ, COALESCE(anbun.按分品目id, 0) AS 按分品目id, anbun.按分品目名, anbun.按分正味重量, anbun.按分比重, anbun.按分m3 ")
                     .append("FROM (SELECT v1_weigh_data.*, m_bill.tax_no FROM v1_weigh_data LEFT JOIN m_bill ON v1_weigh_data.bill_id = m_bill.id ")
                     .append("WHERE term_date >= ? AND term_date <= ? and option2 = 1 ")
                     .append("ORDER BY gate_out_date) a1 ")
                     .append("LEFT JOIN  (SELECT ")
                     .append("v1_weigh_data_anbun.weight_sno, v1_weigh_data_anbun.按分品目id, ")
                     .append("v1_weigh_data_anbun.按分品目名, v1_weigh_data_anbun.按分正味重量, ")
                     .append("v1_weigh_data_anbun.按分比重, v1_weigh_data_anbun.按分m3 ")
                     .append("FROM v1_weigh_data_anbun ")
                     .append("WHERE v1_weigh_data_anbun.delete_flag <> TRUE ")
                     .append("AND v1_weigh_data_anbun.按分品目id > 0) anbun ON a1.sno = anbun.weight_sno ")
                     .append("ORDER BY term_date, report_no;")
                     .toString();

            PreparedStatement ps = con.prepareStatement(s);
            try {
                ps.setObject(1, tax_);
                ps.setObject(2, tax_);
                ps.setObject(3, tax_);
                ps.setObject(4, tax_);
                ps.setObject(5, tax_);
                ps.setObject(6, tax_);
                ps.setObject(7, DBManager.convert(term_date));
                ps.setObject(8, DBManager.convert(term_date1));
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
                        value = sdf03.format(rs.getTimestamp("gate_out_date"));//計量日時
                        cell.setCellValue((String) value);
                        cell.setCellStyle(dstyle2_);

                        cell = row1.createCell(1);//2列
                        value = rs.getString("report_no");//伝票NO
                        cell.setCellValue((String) value);
                        cell.setCellStyle(style_);

                        cell = row1.createCell(2);//3列
                        value = rs.getString("bill_name");//請求業者
                        cell.setCellValue((String) value);
                        cell.setCellStyle(style_);

                        cell = row1.createCell(3);//3列
                        value = rs.getString("entrepreneur_name");//排出業者
                        cell.setCellValue((String) value);
                        cell.setCellStyle(style_);

                        cell = row1.createCell(4);//4列
                        value = rs.getString("car_no");//車番
                        cell.setCellValue((String) value);
                        cell.setCellStyle(style_);

                        if (rs.getBoolean("f按分フラグ") && Integer.valueOf(rs.getString("按分品目id")) > 0) {
                            cell = row1.createCell(5);//5列
                            value = rs.getString("按分品目名");//品名
                            cell.setCellValue((String) value);
                            cell.setCellStyle(style_);
                        } else {
                            cell = row1.createCell(5);//5列
                            value = rs.getString("goods_name");//品名
                            cell.setCellValue((String) value);
                            cell.setCellStyle(style_);
                        }

                        //運搬業者
                        cell = row1.createCell(6);//8列
                        value = rs.getString("transporter_name");//運搬業者
                        cell.setCellValue((String) value);
                        cell.setCellStyle(style_);

                        //排出現場
                        cell = row1.createCell(7);//9列
                        value = rs.getString("office_sub_name").length() > 0
                                 ? rs.getString("office_sub_name")
                                 : rs.getString("office_name");//現場名　
                        cell.setCellValue((String) value);
                        cell.setCellStyle(style_);

                        cell = row1.createCell(8);//6列
                        value = rs.getInt("charge_value");//金額
                        cell.setCellValue((Integer) value);
                        cell.setCellStyle(cstyle_);

                        cell = row1.createCell(9);//6列
                        value = rs.getInt("tax_value");//消費税
                        cell.setCellValue((Integer) value);
                        cell.setCellStyle(cstyle_);

                        cell = row1.createCell(10);//6列
                        value = rs.getInt("charge_tax_value");//合計
                        cell.setCellValue((Integer) value);
                        i合計金額 += (Integer) value;
                        cell.setCellStyle(cstyle_);
                        i現在行++;
                    }

                    i現在行++;
                    row1 = sheet1.createRow(i現在行);//5行目スタート
                    row1.setHeightInPoints(15);//行幅を15に指定
                    cell = row1.createCell(9);//6列
                    cell.setCellValue("合計");
                    cell.setCellStyle(style_);
                    cell = row1.createCell(10);//6列
                    cell.setCellValue(i合計金額);
                    cell.setCellStyle(cstyle_);

                } catch (Exception ie1) {
                    System.out.println(ie1.toString() + String.valueOf(i現在行));
                }

            } finally {
                ps.close();
            }
            wb1.getCreationHelper().createFormulaEvaluator().evaluateAll();
            sheet1.setSelected(true);

            try {
                //自身のtmpに保存
                out1 = new FileOutputStream(storeFol);
                wb1.write(out1);
                //スクリプトから印刷
                print_out = new File(storeFol);
                doPrintXlsx(print_out.getAbsolutePath(), printer[1], 1, true);

            } catch (FileNotFoundException fe) {
                JOptionPane.showMessageDialog(
                         null,
                         "ファイルが既に開かれています。",
                         "確認",
                         JOptionPane.WARNING_MESSAGE);
                return ret;
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

    //売上高実績一覧
    private Boolean printReport1new(
             String file_path,
             String file_name,
             String storeFol,
             Connection con,
             Calendar term_date1,
             Calendar term_date2,
             int tax,
             int index) {

        boolean ret = false;
        int cnt00 = 0;
        try {
            InputStream in1 = new FileInputStream(file_path);
            wb1 = WorkbookFactory.create(in1);
            initCom();
            Sheet sheet1 = wb1.getSheet("print");

            SimpleDateFormat sdf00 = new SimpleDateFormat("yyyy年MM月dd日～");
            SimpleDateFormat sdf01 = new SimpleDateFormat("yyyy年MM月dd日 分");
            SimpleDateFormat sdf02 = new SimpleDateFormat("yyyy/MM/dd HH:mm");

            FileOutputStream out1 = null;
            File print_out = null;

            Row row, row1;
            Cell cell;
            Object value = null;
            String[] printer = getPrinter();
            int i合計金額 = 0;
            int i消費税合計 = 0;
            int i料金合計 = 0;

            /////////////////////////////////////////////////////////////////////////////////
            String s = new StringBuilder()
                     .append("SELECT * FROM f1_得意先売上高一覧new(?, ?, ?, ?) ")
                     .append("ORDER BY bill_id;")
                     .toString();

            PreparedStatement ps = con.prepareStatement(s);
            try {
                ps.setObject(1, DBManager.convert(term_date1));
                ps.setObject(2, DBManager.convert(term_date2));
                ps.setObject(3, tax);
                ps.setObject(4, index);
                ResultSet rs = ps.executeQuery();

                //帳票タイトル
                row = sheet1.getRow(0);//1行目
                cell = row.getCell(0);//A列
                value = index == 1 ? "□□□　得意先別実績一覧表　□□□"
                         : index == 2 ? "□□□　排出業者別実績一覧表　□□□"
                                  : "□□□　運搬業者別実績一覧表　□□□";
                cell.setCellValue((String) value);

                //列タイトル
                row = sheet1.getRow(3);//4行目
                cell = row.getCell(2);//C列
                value = index == 1 ? "得意先名"
                         : index == 2 ? "排出業者名"
                                  : "運搬業者名";
                cell.setCellValue((String) value);

                //対象日付
                row = sheet1.getRow(1);//2行目
                cell = row.getCell(0);//A列
                value = (sdf00.format(DBManager.convert(term_date1)).equals(sdf00.format(DBManager.convert(term_date2)))
                         ? sdf01.format(DBManager.convert(term_date1))
                         : sdf00.format(DBManager.convert(term_date1)) + sdf01.format(DBManager.convert(term_date2)));
                cell.setCellValue((String) value);

                //印刷日
                row = sheet1.getRow(1);//2行目
                cell = row.getCell(4);//E列
                value = "印刷日時：" + sdf02.format(DBManager.convert((Calendar.getInstance()).getTime()));
                cell.setCellValue((String) value);

                //消費税率
                row = sheet1.getRow(2);//3行目
                cell = row.getCell(5);//F列
                value = tax;
                cell.setCellValue((Integer) value);

                int i現在行 = 4;
                try {
                    while (rs.next()) {

                        row1 = sheet1.createRow(i現在行);//5行目スタート
                        row1.setHeightInPoints(15);//行幅を15に指定
                        cell = row1.createCell(0);//A列
                        value = rs.getInt("bill_id");//bill_id
                        cell.setCellValue((Integer) value);
                        //3桁カンマ無し
                        cell.setCellStyle((i現在行 - 3) % 5 != 0 ? rstyle2 : rstyle2_);

                        cell = row1.createCell(1);//B列
                        cell.setCellStyle((i現在行 - 3) % 5 != 0 ? style : style_);//左寄せテキスト

                        cell = row1.createCell(2);//C列
                        value = rs.getString("bill_name");//排出業者
                        cell.setCellValue((String) value);
                        cell.setCellStyle((i現在行 - 3) % 5 != 0 ? style : style_);//左寄せテキスト

                        cell = row1.createCell(3);//D列
                        cell.setCellStyle((i現在行 - 3) % 5 != 0 ? style : style_);//左寄せテキスト

                        cell = row1.createCell(4);//E列
                        value = rs.getInt("sum_charge_value");//合計金額
                        cell.setCellValue((Integer) value);
                        cell.setCellStyle((i現在行 - 3) % 5 != 0 ? cstyle : cstyle_);//3桁カンマあり
                        i合計金額 += (Integer) value;

                        cell = row1.createCell(5);//E列
                        value = rs.getInt("sum_tax_value");//消費税
                        cell.setCellValue((Integer) value);
                        cell.setCellStyle((i現在行 - 3) % 5 != 0 ? cstyle : cstyle_);//3桁カンマあり
                        i消費税合計 += (Integer) value;

                        cell = row1.createCell(6);//E列
                        value = rs.getInt("sum_charge_tax_value");//料金合計
                        cell.setCellValue((Integer) value);
                        cell.setCellStyle((i現在行 - 3) % 5 != 0 ? cstyle : cstyle_);//3桁カンマあり
                        i料金合計 += (Integer) value;

                        i現在行++;
                    }
                    //最終行に合計金額
                    i現在行++;
                    row1 = sheet1.createRow(i現在行);//5行目スタート
                    row1.setHeightInPoints(15);//行幅を15に指定
                    cell = row1.createCell(3);//D列
                    value = "合計";
                    cell.setCellValue((String) value);
                    cell.setCellStyle(style_);//左寄せテキスト
                    cell = row1.createCell(4);//E列
                    cell.setCellValue(i合計金額);
                    cell.setCellStyle(cstyle_);//3桁カンマあり
                    cell = row1.createCell(5);//F列
                    cell.setCellValue(i消費税合計);
                    cell.setCellStyle(cstyle_);//3桁カンマあり
                    cell = row1.createCell(6);//G列
                    cell.setCellValue(i料金合計);
                    cell.setCellStyle(cstyle_);//3桁カンマあり

                } catch (Exception ie1) {
                    System.out.println(ie1.toString() + String.valueOf(i現在行));
                }

            } finally {
                ps.close();
            }

            wb1.getCreationHelper().createFormulaEvaluator().evaluateAll();
            sheet1.setSelected(true);

            try {
                //自身のtmpに保存
                out1 = new FileOutputStream(storeFol);
                wb1.write(out1);
                //スクリプトから印刷
                print_out = new File(storeFol);
                doPrintXlsx(print_out.getAbsolutePath(), printer[1], 1, true);

            } catch (FileNotFoundException fe) {
                JOptionPane.showMessageDialog(
                         null,
                         "ファイルが既に開かれています。",
                         "確認",
                         JOptionPane.WARNING_MESSAGE);
            } catch (IOException e) {
                System.out.println(e.toString());
            } finally {
                try {
                    if (out1 != null) {
                        out1.close();
                    }

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
