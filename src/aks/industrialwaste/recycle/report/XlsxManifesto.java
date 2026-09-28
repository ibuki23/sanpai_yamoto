package aks.industrialwaste.recycle.report;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.channels.FileChannel;
import java.sql.Date;
import java.sql.Statement;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.*;

public class XlsxManifesto extends OSCReport {

    public static final int MANIFESTO_REPORT1 = 1;//建設系マニフェスト
    public static final int MANIFESTO_REPORT2 = 2;//直行型マニフェスト
    public static final int MANIFESTO_REPORT3 = 3;//建設系マニフェスト最終処分の場所印字
    public static final int MANIFESTO_REPORT4 = 4;//直行型マニフェスト最終処分の場所印字
    public static final int MANIFESTO_REPORT5 = 5;//処理委託契約書
    public static final int MANIFESTO_REPORT6 = 6;//委託契約ラベル_CODE39
    public static final int MANIFESTO_REPORT9 = 9;
    public static final int MANIFESTO_REPORT10 = 10;
    public static final int MANIFESTO_REPORT11 = 11;
    private Connection con_;
    private Workbook wb1;

    public XlsxManifesto() {

        this(MANIFESTO_REPORT1);
    }

    public XlsxManifesto(int reportID) {
        if ((MANIFESTO_REPORT1 != reportID)
                 && (MANIFESTO_REPORT2 != reportID)
                 && (MANIFESTO_REPORT3 != reportID)
                 && (MANIFESTO_REPORT4 != reportID)
                 && (MANIFESTO_REPORT5 != reportID)
                 && (MANIFESTO_REPORT6 != reportID)) {
            throw new IllegalArgumentException("'reportID' is not match.");
        }
    }

    /**
     * 指定条件で帳票を印刷する.
     *
     * @param con データベースとの接続
     * @param path 保存先ファイルのフルパス
     * @param report_sno 対象の報告
     * @param term_date 対象年
     */
    public int print(Connection con, int report_sno, Long sno) {
        con_ = con;
        String template = "";

        try {
            switch (report_sno) {
                case MANIFESTO_REPORT1:
                    //建設系マニフェスト
                    template = "template\\建設系廃棄物マニフェスト.xlsx";
                    break;
                case MANIFESTO_REPORT2:
                    //直行型マニフェスト
                    template = "template\\直行型廃棄物マニフェスト.xlsx";
                    break;
                case MANIFESTO_REPORT3:
                    //建設系マニフェスト
                    template = "template\\建設系廃棄物マニフェスト.xlsx";
                    break;
                case MANIFESTO_REPORT4:
                    //直行型マニフェスト
                    template = "template\\直行型廃棄物マニフェスト.xlsx";
                    break;
                case MANIFESTO_REPORT5:
                    //処理委託契約書
                    template = "template\\処理委託契約書.xlsx";
                    break;
                case MANIFESTO_REPORT6:
                    //委託契約ラベル_CODE39
                    template = "template\\委託契約ラベル_CODE39.xlsx";
                    break;
                default:
                    break;
            }

            // テンプレートファイルをローカルにコピー
            File templateFile = new File(template);
            File tmpFile = new File("tmp", templateFile.getName());

            // テンプレートファイルをローカルにコピー
            copyTransfer(templateFile.getPath(), tmpFile.getPath());

            try {
                if (MANIFESTO_REPORT1 == report_sno) {
                    //建設系マニフェスト
                    printManifestoReport1(templateFile.getPath(), tmpFile.getPath(), con_, sno);
                } else if (MANIFESTO_REPORT2 == report_sno) {
                    //直行型マニフェスト
                    printManifestoReport2(templateFile.getPath(), tmpFile.getPath(), con_, sno);
                } else if (MANIFESTO_REPORT3 == report_sno) {
                    //建設系マニフェスト最終処分の場所印字
                    printManifestoReport3(templateFile.getPath(), tmpFile.getPath(), con_, sno);
                } else if (MANIFESTO_REPORT4 == report_sno) {
                    //直行型マニフェスト最終処分の場所印字
                    printManifestoReport4(templateFile.getPath(), tmpFile.getPath(), con_, sno);
                } else if (MANIFESTO_REPORT5 == report_sno) {
                    //委託契約書印刷
                    printManifestoReport5(templateFile.getPath(), tmpFile.getPath(), con_, sno);
                } else if (MANIFESTO_REPORT6 == report_sno) {
                    //委託契約ラベル_CODE39
                    printManifestoReport6(templateFile.getPath(), tmpFile.getPath(), con_, sno);
                }

            } finally {
//        calcH.close();
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

    //印刷処理 マニフェスト印刷用
    //file_path:印刷に使用するプリンター
    //printer1:印刷に使用するプリンター
    //printer2:通常使用するプリンター
    //papers:印刷部数
    //preview:プレビューするかどうか
    public static void doPrintXlsx(String file_path, String printer1, String printer2, int papers, boolean b直接印刷) {
        try {
            if (b直接印刷) {
                // 直接印刷：XlsPrint.ps1 側で printer1 を指定して印刷する
                run(
                         "powershell.exe",
                         "-NoProfile",
                         "-ExecutionPolicy", "Bypass",
                         "-File", "tmp\\XlsPrint.ps1",
                         "-FilePath", file_path,
                         "-PrinterName", printer1,
                         "-PrintCount", String.valueOf(papers)
                );
            } else {
                // プレビュー：Excelファイルを開くだけ
                run("cmd", "/c", "start", "", file_path);
            }

        } catch (InterruptedException ev) {
            System.out.println("失敗(InterruptedException)");
            Thread.currentThread().interrupt();
        } catch (IOException ev) {
            System.out.println("失敗(IOException): " + ev.getMessage());
        }
    }

    //印刷処理 委託契約書印刷用
    //file_path:印刷に使用するプリンター
    //printer:通常使用するプリンター
    //papers:印刷部数
    //preview:プレビューするかどうか
// 印刷処理 委託契約書印刷用
    public static void doPrintXlsx(String file_path, String printer, int papers, boolean preview) {
        try {
            if (!preview) {
                // 直接印刷
                run(
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
                run("cmd", "/c", "start", "", file_path);
            }

        } catch (InterruptedException ev) {
            System.out.println("失敗(InterruptedException)");
            Thread.currentThread().interrupt();
        } catch (IOException ev) {
            System.out.println("失敗(IOException): " + ev.getMessage());
        }
    }

    public static void run(String... command) throws IOException, InterruptedException {
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

//建設系マニフェスト 
    private void printManifestoReport1(
             String file_path,
             String storeFol,
             Connection con,
             Long sno) {

        try {
            InputStream in1 = new FileInputStream(file_path);

            wb1 = WorkbookFactory.create(in1);
            XSSFSheet sheet1 = ((XSSFWorkbook) wb1).getSheet("print");

            FileOutputStream out1 = null;
            File print_out = null;

            Row row;
            Cell cell;
            Object value = null;

            String[] printer = getPrinter();

            ///////////////////////////////////////////////////////////////////////////////////
            String s = new StringBuilder().append("select * ")
                     .append(" from v1_weigh_data where sno = ?; ")
                     .toString();
            PreparedStatement ps = con.prepareStatement(s);
            try {
                ps.setObject(1, sno);
                ResultSet rs = ps.executeQuery();

                if (rs.next()) {
                    //セル初期化

                    //データ貼り付け 交付年月日　元号使用止め西暦表示、入力に変更　180918
                    if (rs.getDate("grant_date2") != null && !rs.getBoolean("grant2_print_f")) {
                        //一つのセルに年月日をまとめて貼り付け
                        row = sheet1.getRow(4);//5行目
                        cell = row.getCell(2);//C列
                        value = rs.getDate("grant_date2") != null ? rs.getDate("grant_date2") : " ";
                        cell.setCellValue((Date) value);
                    } else {
                        //（年月日）
                        row = sheet1.getRow(4);//5行目
                        cell = row.getCell(2);//C列
                        value = "　";
                        cell.setCellValue((String) value);
                    }
                    if (!rs.getBoolean("tantou_print_f")) {
                        //交付担当者所属
                        row = sheet1.getRow(4);//5行目
                        cell = row.getCell(56);//BE列
                        value = rs.getString("grant_name01");
                        cell.setCellValue((String) value);
                        //交付担当者氏名
                        row = sheet1.getRow(4);//5行目
                        cell = row.getCell(81);//CD列
                        value = rs.getString("grant_name");
                        cell.setCellValue((String) value);
                    } else {
                        //交付担当者所属
                        row = sheet1.getRow(4);//5行目
                        cell = row.getCell(56);//BE列
                        value = "　";
                        cell.setCellValue((String) value);
                        //交付担当者氏名
                        row = sheet1.getRow(4);//5行目
                        cell = row.getCell(81);//CD列
                        value = "　";
                        cell.setCellValue((String) value);
                    }
                    if (!rs.getBoolean("ent_print_flag")) {
                        //排出業者〒
                        row = sheet1.getRow(10);//11行目
                        cell = row.getCell(10);//K列
                        value = rs.getString("ent_zip_code");
                        cell.setCellValue((String) value);
                        //排出業者住所1
                        row = sheet1.getRow(12);//13行目
                        cell = row.getCell(5);//F列
                        value = rs.getString("ent_address1");
                        cell.setCellValue((String) value);
                        //排出業者正式名称
                        row = sheet1.getRow(14);//15行目
                        cell = row.getCell(16);//Q列
                        value = rs.getString("ent_p_name");
                        cell.setCellValue((String) value);
                        //排出業者電話
                        row = sheet1.getRow(16);//17行目
                        cell = row.getCell(16);//Q列
                        value = rs.getString("ent_tel");
                        cell.setCellValue((String) value);
                    } else {
                        //排出業者〒
                        row = sheet1.getRow(10);//11行目
                        cell = row.getCell(10);//K列
                        value = "　";
                        cell.setCellValue((String) value);
                        //排出業者住所1
                        row = sheet1.getRow(12);//13行目
                        cell = row.getCell(5);//F列
                        value = "　";
                        cell.setCellValue((String) value);
                        //排出業者正式名称
                        row = sheet1.getRow(14);//15行目
                        cell = row.getCell(16);//Q列
                        value = "　";
                        cell.setCellValue((String) value);
                        //排出業者電話
                        row = sheet1.getRow(16);//17行目
                        cell = row.getCell(16);//Q列
                        value = "　";
                        cell.setCellValue((String) value);
                    }
                    if (!rs.getBoolean("office_print_flag")) {
                        //排出業現場〒
                        row = sheet1.getRow(10);//11行目
                        cell = row.getCell(57);//BF列
                        value = rs.getString("office_zip_code");
                        cell.setCellValue((String) value);
                        //排出現場住所1
                        row = sheet1.getRow(12);//13行目
                        cell = row.getCell(51);//AZ列
                        value = rs.getString("office_address1");
                        cell.setCellValue((String) value);
                        //排出現場正式名称
                        row = sheet1.getRow(14);//15行目
                        cell = row.getCell(56);//BE列
                        value = rs.getString("office_p_name") + rs.getString("office_sub_name");

                        cell.setCellValue((String) value);
                        //排出現場電話
                        row = sheet1.getRow(16);//17行目
                        cell = row.getCell(60);//BI列
                        value = rs.getString("office_tel");
                        cell.setCellValue((String) value);
                    } else {
                        //排出業現場〒
                        row = sheet1.getRow(10);//11行目
                        cell = row.getCell(57);//BF列
                        value = "　";
                        cell.setCellValue((String) value);
                        //排出現場住所1
                        row = sheet1.getRow(12);//13行目
                        cell = row.getCell(51);//F列
                        value = "　";
                        cell.setCellValue((String) value);
                        //排出現場正式名称
                        row = sheet1.getRow(14);//15行目
                        cell = row.getCell(56);//BE列
                        value = "　";
                        cell.setCellValue((String) value);
                        //排出現場電話
                        row = sheet1.getRow(16);//17行目
                        cell = row.getCell(60);//BI列
                        value = "　";
                        cell.setCellValue((String) value);
                    }
                    //////////品目関係///////////////////////////////////////////////

                    if (!rs.getBoolean("tani_print_f")) {
                        //単位チェック ton
                        row = sheet1.getRow(19);//20行目
                        cell = row.getCell(29);//AD列
                        value = (rs.getBoolean("goods_flag01") ? "○" : "　");
                        cell.setCellValue((String) value);
                        //単位チェック kg
                        row = sheet1.getRow(19);//20行目
                        cell = row.getCell(32);//AG列
                        value = (rs.getBoolean("goods_flag02") ? "○" : "　");
                        cell.setCellValue((String) value);
                        //単位チェック m3
                        row = sheet1.getRow(19);//20行目
                        cell = row.getCell(35);//AJ列
                        value = (rs.getBoolean("goods_flag03") ? "○" : "　");
                        cell.setCellValue((String) value);
                        //単位チェック L
                        row = sheet1.getRow(19);//20行目
                        cell = row.getCell(38);//AM列
                        value = (rs.getBoolean("goods_flag04") ? "○" : "　");
                        cell.setCellValue((String) value);
                    } else {
                        //単位チェック ton
                        row = sheet1.getRow(19);//20行目
                        cell = row.getCell(29);//AD列
                        value = " ";
                        cell.setCellValue((String) value);
                        //単位チェック kg
                        row = sheet1.getRow(19);//20行目
                        cell = row.getCell(32);//AG列
                        value = " ";
                        cell.setCellValue((String) value);
                        //単位チェック m3
                        row = sheet1.getRow(19);//20行目
                        cell = row.getCell(35);//AJ列
                        value = " ";
                        cell.setCellValue((String) value);
                        //単位チェック L
                        row = sheet1.getRow(19);//20行目
                        cell = row.getCell(38);//AM列
                        value = " ";
                        cell.setCellValue((String) value);
                    }

                    //コンクリートがら//////////////////////////////////////////////
                    row = sheet1.getRow(23);//24行目
                    cell = row.getCell(1);//B列
                    value = (!rs.getBoolean("goods_flag40") && rs.getBoolean("goods_flag05") ? "○" : "　");
                    cell.setCellValue((String) value);
                    row = sheet1.getRow(23);//24行目
                    cell = row.getCell(19);//T列
                    value = (!rs.getBoolean("goods_flag40") && rs.getString("goods_weight01").length() > 0 ? rs.getString("goods_weight01") : "　");
                    cell.setCellValue((String) value);
                    //アスコンがら/////////////////////////////////////////////////
                    row = sheet1.getRow(24);//25行目
                    cell = row.getCell(1);//B列
                    value = (!rs.getBoolean("goods_flag40") && rs.getBoolean("goods_flag06") ? "○" : "　");
                    cell.setCellValue((String) value);
                    row = sheet1.getRow(24);//25行目
                    cell = row.getCell(19);//T列
                    value = (!rs.getBoolean("goods_flag40") && rs.getString("goods_weight02").length() > 0 ? rs.getString("goods_weight02") : "　");
                    cell.setCellValue((String) value);
                    //その他がれき/////////////////////////////////////////////////
                    row = sheet1.getRow(25);//26行目
                    cell = row.getCell(1);//B列
                    value = (!rs.getBoolean("goods_flag40") && rs.getBoolean("goods_flag07") ? "○" : "　");
                    cell.setCellValue((String) value);
                    row = sheet1.getRow(25);//26行目
                    cell = row.getCell(19);//T列
                    value = (!rs.getBoolean("goods_flag40") && rs.getString("goods_weight03").length() > 0 ? rs.getString("goods_weight03") : "　");
                    cell.setCellValue((String) value);
                    //ガラス陶磁器/////////////////////////////////////////////////
                    row = sheet1.getRow(26);//27行目
                    cell = row.getCell(1);//B列
                    value = (!rs.getBoolean("goods_flag40") && rs.getBoolean("goods_flag08") ? "○" : "　");
                    cell.setCellValue((String) value);
                    row = sheet1.getRow(26);//27行目
                    cell = row.getCell(19);//T列
                    value = (!rs.getBoolean("goods_flag40") && rs.getString("goods_weight04").length() > 0 ? rs.getString("goods_weight04") : "　");
                    cell.setCellValue((String) value);
                    //廃プラ/////////////////////////////////////////////////
                    row = sheet1.getRow(27);//28行目
                    cell = row.getCell(1);//B列
                    value = (!rs.getBoolean("goods_flag40") && rs.getBoolean("goods_flag09") ? "○" : "　");
                    cell.setCellValue((String) value);
                    row = sheet1.getRow(27);//28行目
                    cell = row.getCell(19);//T列
                    value = (!rs.getBoolean("goods_flag40") && rs.getString("goods_weight05").length() > 0 ? rs.getString("goods_weight05") : "　");
                    cell.setCellValue((String) value);
                    //金属くず/////////////////////////////////////////////////
                    row = sheet1.getRow(28);//29行目
                    cell = row.getCell(1);//B列
                    value = (!rs.getBoolean("goods_flag40") && rs.getBoolean("goods_flag10") ? "○" : "　");
                    cell.setCellValue((String) value);
                    row = sheet1.getRow(28);//29行目
                    cell = row.getCell(19);//T列
                    value = (!rs.getBoolean("goods_flag40") && rs.getString("goods_weight06").length() > 0 ? rs.getString("goods_weight06") : "　");
                    cell.setCellValue((String) value);

                    //混合/////////////////////////////////////////////////
                    row = sheet1.getRow(23);//24行目
                    cell = row.getCell(27);//AB列
                    value = (!rs.getBoolean("goods_flag40") && rs.getBoolean("goods_flag11") ? "○" : "　");
                    cell.setCellValue((String) value);
                    row = sheet1.getRow(23);//24行目
                    cell = row.getCell(43);//AR列
                    value = (!rs.getBoolean("goods_flag40") && rs.getString("goods_weight07").length() > 0 ? rs.getString("goods_weight07") : "　");
                    cell.setCellValue((String) value);
                    //石綿含有産業廃棄物/////////////////////////////////////////////////
                    row = sheet1.getRow(24);//25行目
                    cell = row.getCell(27);//AB列
                    value = (!rs.getBoolean("goods_flag40") && rs.getBoolean("goods_flag12") ? "○" : "　");
                    cell.setCellValue((String) value);
                    row = sheet1.getRow(24);//25行目
                    cell = row.getCell(43);//AR列
                    value = (!rs.getBoolean("goods_flag40") && rs.getString("goods_weight08").length() > 0 ? rs.getString("goods_weight08") : "　");
                    cell.setCellValue((String) value);

                    //建設汚泥/////////////////////////////////////////////////////////////////////////////////////////////////
                    row = sheet1.getRow(23);//24行目
                    cell = row.getCell(51);//AZ列
                    value = (!rs.getBoolean("goods_flag40") && rs.getBoolean("goods_flag17") ? "○" : "　");
                    cell.setCellValue((String) value);
                    row = sheet1.getRow(23);//24行目
                    cell = row.getCell(67);//BP列
                    value = (!rs.getBoolean("goods_flag40") && rs.getString("goods_weight09").length() > 0 ? rs.getString("goods_weight09") : "　");
                    cell.setCellValue((String) value);
                    //紙くず/////////////////////////////////////////////////
                    row = sheet1.getRow(24);//25行目
                    cell = row.getCell(51);//AZ列
                    value = (!rs.getBoolean("goods_flag40") && rs.getBoolean("goods_flag18") ? "○" : "　");
                    cell.setCellValue((String) value);
                    row = sheet1.getRow(24);//25行目
                    cell = row.getCell(67);//BP列
                    value = (!rs.getBoolean("goods_flag40") && rs.getString("goods_weight10").length() > 0 ? rs.getString("goods_weight10") : "　");
                    cell.setCellValue((String) value);
                    //木くず/////////////////////////////////////////////////
                    row = sheet1.getRow(25);//24行目
                    cell = row.getCell(51);//AZ列
                    value = (!rs.getBoolean("goods_flag40") && rs.getBoolean("goods_flag19") ? "○" : "　");
                    cell.setCellValue((String) value);
                    row = sheet1.getRow(25);//24行目
                    cell = row.getCell(67);//BP列
                    value = (!rs.getBoolean("goods_flag40") && rs.getString("goods_weight11").length() > 0 ? rs.getString("goods_weight11") : "　");
                    cell.setCellValue((String) value);
                    //繊維くず/////////////////////////////////////////////////
                    row = sheet1.getRow(26);//25行目
                    cell = row.getCell(51);//AZ列
                    value = (!rs.getBoolean("goods_flag40") && rs.getBoolean("goods_flag20") ? "○" : "　");
                    cell.setCellValue((String) value);
                    row = sheet1.getRow(26);//25行目
                    cell = row.getCell(67);//BP列
                    value = (!rs.getBoolean("goods_flag40") && rs.getString("goods_weight12").length() > 0 ? rs.getString("goods_weight12") : "　");
                    cell.setCellValue((String) value);
                    //廃石膏ボード/////////////////////////////////////////////////
                    row = sheet1.getRow(27);//24行目
                    cell = row.getCell(51);//AZ列
                    value = (!rs.getBoolean("goods_flag40") && rs.getBoolean("goods_flag21") ? "○" : "　");
                    cell.setCellValue((String) value);
                    row = sheet1.getRow(27);//24行目
                    cell = row.getCell(67);//BP列
                    value = (!rs.getBoolean("goods_flag40") && rs.getString("goods_weight13").length() > 0 ? rs.getString("goods_weight13") : "　");
                    cell.setCellValue((String) value);
                    //混合型/////////////////////////////////////////////////
                    row = sheet1.getRow(28);//25行目
                    cell = row.getCell(51);//AZ列
                    value = (!rs.getBoolean("goods_flag40") && rs.getBoolean("goods_flag22") ? "○" : "　");
                    cell.setCellValue((String) value);
                    row = sheet1.getRow(28);//25行目
                    cell = row.getCell(67);//BP列
                    value = (!rs.getBoolean("goods_flag40") && rs.getString("goods_weight14").length() > 0 ? rs.getString("goods_weight14") : "　");
                    cell.setCellValue((String) value);

                    //石綿含有産業廃棄物/////////////////////////////////////////////////////////////////////////////////////
                    row = sheet1.getRow(23);//24行目
                    cell = row.getCell(75);//BX列
                    value = (!rs.getBoolean("goods_flag40") && rs.getBoolean("goods_flag23") ? "○" : "　");
                    cell.setCellValue((String) value);
                    row = sheet1.getRow(23);//24行目
                    cell = row.getCell(91);//CN列
                    value = (!rs.getBoolean("goods_flag40") && rs.getString("goods_weight15").length() > 0 ? rs.getString("goods_weight15") : "　");
                    cell.setCellValue((String) value);
                    //水銀使用製品/////////////////////////////////////////////////////////////////////////////////////
                    row = sheet1.getRow(24);//25行目
                    cell = row.getCell(75);//BX列
                    value = (!rs.getBoolean("goods_flag40") && rs.getBoolean("goods_flag24") ? "○" : "　");
                    cell.setCellValue((String) value);
                    row = sheet1.getRow(24);//25行目
                    cell = row.getCell(91);//CN列
                    value = (!rs.getBoolean("goods_flag40") && rs.getString("goods_weight17").length() > 0 ? rs.getString("goods_weight17") : "　");
                    cell.setCellValue((String) value);
                    //廃石綿/////////////////////////////////////////////////
                    row = sheet1.getRow(23);//24行目
                    cell = row.getCell(98);//CU列
                    value = (!rs.getBoolean("goods_flag40") && rs.getBoolean("goods_flag27") ? "○" : "　");
                    cell.setCellValue((String) value);
                    row = sheet1.getRow(23);//24行目
                    cell = row.getCell(114);//DK列
                    value = (!rs.getBoolean("goods_flag40") && rs.getString("goods_weight16").length() > 0 ? rs.getString("goods_weight16") : "　");
                    cell.setCellValue((String) value);

                    //総重量/////////////////////////////////////////////////////////////////////////////////////////////////
                    row = sheet1.getRow(27);//28行目
                    cell = row.getCell(91);//CN列
                    value = (!rs.getBoolean("goods_flag40") && rs.getString("trans_net_weight").length() > 0 ? rs.getString("trans_net_weight") : "　");
                    cell.setCellValue((String) value);

                    //形状////////////////////////////////////////////////////////////////////////////////////////////////////
                    row = sheet1.getRow(22);//23行目
                    cell = row.getCell(124);//DU列
                    value = (!rs.getBoolean("keijyo_print_f") && rs.getBoolean("goods_flag33") ? "○" : "　");
                    cell.setCellValue((String) value);

                    row = sheet1.getRow(23);//24行目
                    cell = row.getCell(124);//DU列
                    value = (!rs.getBoolean("keijyo_print_f") && rs.getBoolean("goods_flag34") ? "○" : "　");
                    cell.setCellValue((String) value);

                    row = sheet1.getRow(24);//25行目
                    cell = row.getCell(124);//DU列
                    value = (!rs.getBoolean("keijyo_print_f") && rs.getBoolean("goods_flag35") ? "○" : "　");
                    cell.setCellValue((String) value);
                    //荷姿/////////////////////////////////////////////////
                    row = sheet1.getRow(22);//23行目
                    cell = row.getCell(133);//ED列
                    value = (!rs.getBoolean("nisugata_print_f") && rs.getBoolean("goods_flag36") ? "○" : "　");
                    cell.setCellValue((String) value);

                    row = sheet1.getRow(23);//24行目
                    cell = row.getCell(133);//ED列
                    value = (!rs.getBoolean("nisugata_print_f") && rs.getBoolean("goods_flag37") ? "○" : "　");
                    cell.setCellValue((String) value);

                    row = sheet1.getRow(24);//25行目
                    cell = row.getCell(133);//ED列
                    value = (!rs.getBoolean("nisugata_print_f") && rs.getBoolean("goods_flag38") ? "○" : "　");
                    cell.setCellValue((String) value);

                    row = sheet1.getRow(25);//26行目
                    cell = row.getCell(133);//ED列
                    value = (!rs.getBoolean("nisugata_print_f") && rs.getBoolean("goods_flag39") ? "○" : "　");
                    cell.setCellValue((String) value);

                    row = sheet1.getRow(26);//27行目
                    cell = row.getCell(133);//ED列
                    value = (!rs.getBoolean("nisugata_print_f") && rs.getBoolean("goods_flag41") ? "○" : "　");
                    cell.setCellValue((String) value);

                    ////////////////////////////////////////////////////////////////////////////////////////////////////////
                    //当欄記載のとおり　斜線
                    if (rs.getBoolean("slash_flag01")) {

                        XSSFDrawing drawing01 = sheet1.createDrawingPatriarch();
                        XSSFClientAnchor anchor01 = new XSSFClientAnchor(0, 0, 0, 0, 50, 30, 100, 31);
                        XSSFSimpleShape shape01 = drawing01.createSimpleShape(anchor01);
                        shape01.setShapeType(ShapeTypes.LINE);                        // 直線
                        shape01.setLineWidth(1);                                     // 太さ(pt)
                        shape01.setLineStyleColor(0, 0, 0);                           // 線の色(黒)

                        //当欄記載のとおりチェックを空欄に
                        row = sheet1.getRow(30);//31行目
                        cell = row.getCell(50);//AY列
                        value = "　";
                        cell.setCellValue((String) value);
                        //当欄記載のとおりチェックを空欄に
                        row = sheet1.getRow(30);//31行目
                        cell = row.getCell(74);//BW列
                        value = "　";
                        cell.setCellValue((String) value);

                    } else {//check_flag01 
                        //当欄記載のとおりチェックを空欄に
                        row = sheet1.getRow(30);//31行目
                        cell = row.getCell(50);//AY列
                        value = (rs.getBoolean("check_flag01") ? "○" : "　");
                        cell.setCellValue((String) value);
                        //当欄記載のとおりチェックを空欄に
                        row = sheet1.getRow(30);//31行目
                        cell = row.getCell(74);//BW列
                        value = (rs.getBoolean("check_flag02") ? "○" : "　");
                        cell.setCellValue((String) value);
                        //当欄記載のとおりチェックを空欄に
                        row = sheet1.getRow(30);//31行目
                        cell = row.getCell(91);//CN列
                        value = (rs.getBoolean("check_flag02") ? "　" : "　");
                        cell.setCellValue((String) value);
                    }

                    //当欄記載のとおりチェックを空欄に
                    row = sheet1.getRow(32);//33行目
                    cell = row.getCell(35);//AJ列
                    value = (rs.getBoolean("check_flag03") ? "○" : "　");
                    cell.setCellValue((String) value);
                    //当欄記載のとおりチェックを空欄に
                    row = sheet1.getRow(32);//33行目
                    cell = row.getCell(65);//BN列
                    value = (rs.getBoolean("check_flag04") ? "○" : "　");
                    cell.setCellValue((String) value);
                    row = sheet1.getRow(32);//33行目
                    cell = row.getCell(91);//CN列
                    value = (rs.getBoolean("check_flag04") ? rs.getString("cont_name02") + "  " + rs.getString("cont_no02") : "　");
                    cell.setCellValue((String) value);

                    //当欄記載のとおりチェックで「㈱スパット北上」印字
                    row = sheet1.getRow(32);//33行目
                    cell = row.getCell(91);//CN列
                    value = (rs.getBoolean("check_flag04") && !(rs.getBoolean("check_flag03")) ? "㈱スパット北上" : "　");
                    cell.setCellValue((String) value);

                    if (!rs.getBoolean("trans1_print_flag")) {//運搬業者印字なしフラグ
                        //運搬受託１〒
                        row = sheet1.getRow(35);//36行目
                        cell = row.getCell(7);//H列
                        value = rs.getString("trans1_zip_code");
                        cell.setCellValue((String) value);
                        row = sheet1.getRow(35);//36行目
                        cell = row.getCell(19);//T列
                        value = "　";
                        cell.setCellValue((String) value);
                        //運搬受託者1住所
                        row = sheet1.getRow(36);//37行目
                        cell = row.getCell(4);//E列
                        value = rs.getString("trans1_address1") != null ? rs.getString("trans1_address1") : " "
                                 + rs.getString("trans1_address2") != null ? rs.getString("trans1_address2") : " ";
                        cell.setCellValue((String) value);
                        //運搬受託者1氏名
                        row = sheet1.getRow(37);//38行目
                        cell = row.getCell(14);//O列
                        value = rs.getString("trans1_p_name");
                        cell.setCellValue((String) value);
                        //運搬受託者1電話番号
                        row = sheet1.getRow(38);//39行目
                        cell = row.getCell(14);//O列
                        value = rs.getString("trans1_tel");
                        cell.setCellValue((String) value);

                        //運搬受託者1積替有
                        row = sheet1.getRow(41);//42行目
                        cell = row.getCell(1);//B列
                        value = (rs.getBoolean("check_flag05") ? "○" : "　");
                        cell.setCellValue((String) value);
                        //運搬受託者1積替無
                        row = sheet1.getRow(41);//42行目
                        cell = row.getCell(6);//G列
                        value = (rs.getBoolean("check_flag06") ? "○" : "　");
                        cell.setCellValue((String) value);
                        //運搬受託者1車両番号
                        row = sheet1.getRow(41);//42行目
                        cell = row.getCell(14);//O列
                        value = (rs.getString("trans1_car_no").length() > 0 && rs.getInt("trans1_car_no") != 0 ? rs.getString("trans1_car_no") : "　");
                        cell.setCellValue((String) value);
                        //運搬受託者1車種
                        row = sheet1.getRow(41);//42行目
                        cell = row.getCell(32);//AG列
                        value = (rs.getString("trans1_car_no").length() > 0 ? rs.getString("trans1_car_kind") : "　");
                        cell.setCellValue((String) value);
                    } else {
                        //運搬受託１〒
                        row = sheet1.getRow(35);//36行目
                        cell = row.getCell(7);//H列
                        value = "　";
                        cell.setCellValue((String) value);
                        row = sheet1.getRow(35);//36行目
                        cell = row.getCell(19);//T列
                        value = "　";
                        cell.setCellValue((String) value);
                        //運搬受託者住所1
                        row = sheet1.getRow(36);//37行目
                        cell = row.getCell(4);//F列
                        value = "　";
                        cell.setCellValue((String) value);
                        //搬受託者正式名称
                        row = sheet1.getRow(37);//38行目
                        cell = row.getCell(14);//O列
                        value = "　";
                        cell.setCellValue((String) value);
                        //搬受託者電話
                        row = sheet1.getRow(38);//39行目
                        cell = row.getCell(14);//O列
                        value = "　";
                        cell.setCellValue((String) value);

                        //運搬受託者1積替有
                        row = sheet1.getRow(41);//42行目
                        cell = row.getCell(1);//B列
                        value = "　";
                        cell.setCellValue((String) value);
                        //運搬受託者1積替無
                        row = sheet1.getRow(41);//42行目
                        cell = row.getCell(6);//G列
                        value = "　";
                        cell.setCellValue((String) value);
                        //運搬受託者1車両番号
                        row = sheet1.getRow(41);//42行目
                        cell = row.getCell(14);//O列
                        value = "　";
                        cell.setCellValue((String) value);
                        //運搬受託者1車種
                        row = sheet1.getRow(41);//42行目
                        cell = row.getCell(32);//AG列
                        value = "　";
                        cell.setCellValue((String) value);

                    }
                    if (!rs.getBoolean("cont_print_flag")) {
                        //処分受託者郵便番号〒
                        row = sheet1.getRow(44);//45行目
                        cell = row.getCell(9);//J列
                        value = rs.getString("cont_zip_code");
                        cell.setCellValue((String) value);
                        //処分受託者郵便番号〒
                        row = sheet1.getRow(44);//45行目
                        cell = row.getCell(19);//T列
                        value = "　";
                        cell.setCellValue((String) value);
                        //処分受託者1住所
                        row = sheet1.getRow(45);//46行目
                        cell = row.getCell(4);//E列
                        value = rs.getString("cont_address1") != null ? rs.getString("cont_address1") : " "
                                 + rs.getString("cont_address2") != null ? rs.getString("cont_address2") : " ";
                        cell.setCellValue((String) value);
                        //処分受託者1氏名
                        row = sheet1.getRow(46);//47行目
                        cell = row.getCell(13);//N列
                        value = rs.getString("cont_p_name");
                        cell.setCellValue((String) value);
                        //処分受託者1電話番号
                        row = sheet1.getRow(47);//48行目
                        cell = row.getCell(13);//N列
                        value = rs.getString("cont_tel");
                        cell.setCellValue((String) value);
                    } else {
                        //処分受託者〒
                        row = sheet1.getRow(44);//45行目
                        cell = row.getCell(9);//J列
                        value = "　";
                        cell.setCellValue((String) value);
                        row = sheet1.getRow(44);//45行目
                        cell = row.getCell(19);//T列
                        value = "　";
                        cell.setCellValue((String) value);
                        //処分受託者住所1
                        row = sheet1.getRow(45);//46行目
                        cell = row.getCell(4);//F列
                        value = "　";
                        cell.setCellValue((String) value);
                        //処分受託者正式名称
                        row = sheet1.getRow(46);//47行目
                        cell = row.getCell(13);//N列
                        value = "　";
                        cell.setCellValue((String) value);
                        //処分受託者電話
                        row = sheet1.getRow(47);//48行目
                        cell = row.getCell(13);//N列
                        value = "　";
                        cell.setCellValue((String) value);
                    }

                    //運搬受託者（２）　斜線
                    if (rs.getBoolean("slash_flag02")) {

                        XSSFDrawing drawing02 = sheet1.createDrawingPatriarch();
                        XSSFClientAnchor anchor02 = new XSSFClientAnchor(0, 0, 0, 0, 49, 35, 95, 42);
                        XSSFSimpleShape shape02 = drawing02.createSimpleShape(anchor02);
                        shape02.setShapeType(ShapeTypes.LINE);                        // 直線
                        shape02.setLineWidth(1);                                     // 太さ(pt)
                        shape02.setLineStyleColor(0, 0, 0);
                        // 線の色(黒)
                        XSSFDrawing drawing03 = sheet1.createDrawingPatriarch();
                        XSSFClientAnchor anchor03 = new XSSFClientAnchor(0, 0, 0, 0, 49, 44, 95, 48);
                        XSSFSimpleShape shape03 = drawing03.createSimpleShape(anchor03);
                        shape03.setShapeType(ShapeTypes.LINE);                        // 直線
                        shape03.setLineWidth(1);                                     // 太さ(pt)
                        shape03.setLineStyleColor(0, 0, 0);
                        //運搬受託２〒
                        row = sheet1.getRow(35);//36行目
                        cell = row.getCell(55);//BD列
                        value = "　";
                        cell.setCellValue((String) value);
                        //搬受託者住所1
                        row = sheet1.getRow(36);//37行目
                        cell = row.getCell(52);//BA列
                        value = "　";
                        cell.setCellValue((String) value);
                        //搬受託者正式名称
                        row = sheet1.getRow(37);//38行目
                        cell = row.getCell(61);//BJ列
                        value = "　";
                        cell.setCellValue((String) value);
                        //搬受託者電話
                        row = sheet1.getRow(38);//39行目
                        cell = row.getCell(61);//BJ列
                        value = "　";
                        cell.setCellValue((String) value);

                        //運搬受託者２積替有
                        row = sheet1.getRow(41);//42行目
                        cell = row.getCell(49);//AX列
                        value = "　";
                        cell.setCellValue((String) value);
                        //運搬受託者２積替無
                        row = sheet1.getRow(41);//42行目
                        cell = row.getCell(54);//BC列
                        value = "　";
                        cell.setCellValue((String) value);
                        //運搬受託者２車両番号
                        row = sheet1.getRow(41);//42行目
                        cell = row.getCell(61);//BJ列
                        value = "　";
                        cell.setCellValue((String) value);
                        //運搬受託者２車種
                        row = sheet1.getRow(41);//42行目
                        cell = row.getCell(80);//CC列
                        value = "　";
                        cell.setCellValue((String) value);

                        //積替え保管場所〒
                        row = sheet1.getRow(44);//45行目
                        cell = row.getCell(57);//BF列
                        value = "　";
                        cell.setCellValue((String) value);
                        //積替え保管場所名称
                        row = sheet1.getRow(44);//45行目
                        cell = row.getCell(67);//BP列
                        value = "　";
                        cell.setCellValue((String) value);
                        //積替え保管場所住所
                        row = sheet1.getRow(45);//46行目
                        cell = row.getCell(52);//BA列
                        value = "　";
                        cell.setCellValue((String) value);
                        //積替え保管場所電話
                        row = sheet1.getRow(46);//47行目
                        cell = row.getCell(58);//BG列
                        value = "　";
                        cell.setCellValue((String) value);
                        //有価物収集有り
                        row = sheet1.getRow(47);//48行目
                        cell = row.getCell(60);//CC列
                        value = "　";
                        cell.setCellValue((String) value);
                        //有価物収集無し
                        row = sheet1.getRow(47);//48行目
                        cell = row.getCell(65);//CC列
                        value = "　";
                        cell.setCellValue((String) value);
                        //実績数量
                        row = sheet1.getRow(47);//48行目
                        cell = row.getCell(80);//CC列
                        value = "　";
                        cell.setCellValue((String) value);
                        //実績数量単位 ton
                        row = sheet1.getRow(47);//48行目
                        cell = row.getCell(89);//CL列
                        value = "　";
                        cell.setCellValue((String) value);
                        //実績数量単位 m3
                        row = sheet1.getRow(47);//42行目
                        cell = row.getCell(92);//CO列
                        value = "　";
                        cell.setCellValue((String) value);

                    } else {
                        //trans2_print_flag
                        if (!rs.getBoolean("trans2_print_flag")) {//印字なし
                            //運搬受託２〒 trans2_zip_code
                            row = sheet1.getRow(35);//36行目
                            cell = row.getCell(55);//BD列
                            value = rs.getString("trans2_zip_code");
                            cell.setCellValue((String) value);
                            //運搬受託者住所1
                            row = sheet1.getRow(36);//37行目
                            cell = row.getCell(52);//BA列
                            value = rs.getString("trans2_address1") != null ? rs.getString("trans2_address1") : " "
                                     + rs.getString("trans2_address2") != null ? rs.getString("trans2_address2") : " ";
                            cell.setCellValue((String) value);
                            //搬受託者正式名称
                            row = sheet1.getRow(37);//38行目
                            cell = row.getCell(61);//BJ列
                            value = rs.getString("trans2_p_name");
                            cell.setCellValue((String) value);
                            //搬受託者電話
                            row = sheet1.getRow(38);//39行目
                            cell = row.getCell(61);//BJ列
                            value = rs.getString("trans2_tel");
                            cell.setCellValue((String) value);
                            //運搬受託者２積替有
                            row = sheet1.getRow(41);//42行目
                            cell = row.getCell(49);//AX列
                            value = (rs.getBoolean("check_flag07") ? "○" : "　");
                            cell.setCellValue((String) value);
                            //運搬受託者２積替無
                            row = sheet1.getRow(41);//42行目
                            cell = row.getCell(54);//BC列
                            value = (rs.getBoolean("check_flag08") ? "○" : "　");
                            cell.setCellValue((String) value);
                            //運搬受託者２車両番号
                            row = sheet1.getRow(41);//42行目
                            cell = row.getCell(61);//BJ列
                            value = (rs.getString("trans2_car_no").length() > 0 && rs.getInt("trans2_car_no") != 0 ? rs.getString("trans2_car_no") : "　");
                            cell.setCellValue((String) value);
                            //運搬受託者２車種
                            row = sheet1.getRow(41);//42行目
                            cell = row.getCell(80);//CC列
                            value = rs.getString("trans2_car_kind");
                            cell.setCellValue((String) value);

                            //運搬受託者２積替有
                            row = sheet1.getRow(41);//42行目
                            cell = row.getCell(49);//AX列
                            value = (rs.getBoolean("check_flag07") ? "○" : "　");
                            cell.setCellValue((String) value);
                            //運搬受託者２積替無
                            row = sheet1.getRow(41);//42行目
                            cell = row.getCell(54);//BC列
                            value = (rs.getBoolean("check_flag08") ? "○" : "　");
                            cell.setCellValue((String) value);
                            //運搬受託者２車両番号
                            row = sheet1.getRow(41);//42行目
                            cell = row.getCell(61);//BJ列
                            value = (rs.getString("trans2_car_no").length() > 0 && rs.getInt("trans2_car_no") != 0 ? rs.getString("trans2_car_no") : "　");
                            cell.setCellValue((String) value);
                            //運搬受託者２車種
                            row = sheet1.getRow(41);//42行目
                            cell = row.getCell(80);//CC列
                            value = rs.getString("trans2_car_kind");
                            cell.setCellValue((String) value);

                        } else {
                            //運搬受託２〒
                            row = sheet1.getRow(35);//36行目
                            cell = row.getCell(55);//BD列
                            value = "　";
                            cell.setCellValue((String) value);
                            //搬受託者住所1
                            row = sheet1.getRow(36);//37行目
                            cell = row.getCell(52);//BA列
//                            cell = row.getCell(56);//BF列
                            value = "　";
                            cell.setCellValue((String) value);
                            //搬受託者正式名称
                            row = sheet1.getRow(37);//38行目
                            cell = row.getCell(61);//BJ列
                            value = "　";
                            cell.setCellValue((String) value);
                            //搬受託者電話
                            row = sheet1.getRow(38);//39行目
                            cell = row.getCell(61);//BJ列
                            value = "　";
                            cell.setCellValue((String) value);

                            //運搬受託者２積替有
                            row = sheet1.getRow(41);//42行目
                            cell = row.getCell(49);//AX列
                            value = "　";
                            cell.setCellValue((String) value);
                            //運搬受託者２積替無
                            row = sheet1.getRow(41);//42行目
                            cell = row.getCell(54);//BC列
                            value = "　";
                            cell.setCellValue((String) value);
                            //運搬受託者２車両番号
                            row = sheet1.getRow(41);//42行目
                            cell = row.getCell(61);//BJ列
                            value = "　";
                            cell.setCellValue((String) value);
                            //運搬受託者２車種
                            row = sheet1.getRow(41);//42行目
                            cell = row.getCell(80);//CC列
                            value = "　";
                            cell.setCellValue((String) value);

                        }

                        if (!rs.getBoolean("tsumikae_print_flag")) {
                            //積替え保管場所〒
                            row = sheet1.getRow(44);//45行目
                            cell = row.getCell(57);//BF列
                            value = rs.getString("last_cont_zip02");
                            cell.setCellValue((String) value);
                            //積替え保管場所名称
                            row = sheet1.getRow(44);//45行目
                            cell = row.getCell(67);//BP列
                            value = rs.getString("last_cont_name02");
                            cell.setCellValue((String) value);
                            //積替え保管場所住所
                            row = sheet1.getRow(45);//46行目
                            cell = row.getCell(52);//BA列
                            value = rs.getString("last_cont_address02");
                            cell.setCellValue((String) value);
                            //積替え保管場所電話
                            row = sheet1.getRow(46);//47行目
                            cell = row.getCell(58);//BG列
                            value = rs.getString("last_cont_tel02");
                            cell.setCellValue((String) value);
                            //有価物収集有り
                            row = sheet1.getRow(47);//48行目
                            cell = row.getCell(60);//CC列
                            value = (rs.getBoolean("check_flag15") ? "○" : "　");
                            cell.setCellValue((String) value);
                            //有価物収集無し
                            row = sheet1.getRow(47);//48行目
                            cell = row.getCell(65);//CC列
                            value = (rs.getBoolean("check_flag16") ? "○" : "　");
                            cell.setCellValue((String) value);
                            //実績数量
                            row = sheet1.getRow(47);//48行目
                            cell = row.getCell(80);//CC列
                            value = "　";
                            cell.setCellValue((String) value);
                            //実績数量単位 m3
                            row = sheet1.getRow(47);//42行目
                            cell = row.getCell(92);//CO列
                            value = (rs.getBoolean("check_flag17") ? "○" : "　");
                            cell.setCellValue((String) value);
                            //実績数量単位 ton
                            row = sheet1.getRow(47);//48行目
                            cell = row.getCell(89);//CL列
                            value = (rs.getBoolean("check_flag18") ? "○" : "　");
                            cell.setCellValue((String) value);

                        } else {

                            //積替え保管場所〒
                            row = sheet1.getRow(44);//45行目
                            cell = row.getCell(57);//BF列
                            value = "　";
                            cell.setCellValue((String) value);
                            //積替え保管場所名称
                            row = sheet1.getRow(44);//45行目
                            cell = row.getCell(67);//BP列
                            value = "　";
                            cell.setCellValue((String) value);
                            //積替え保管場所住所
                            row = sheet1.getRow(45);//46行目
                            cell = row.getCell(52);//BA列
                            value = "　";
                            cell.setCellValue((String) value);
                            //積替え保管場所電話
                            row = sheet1.getRow(46);//47行目
                            cell = row.getCell(58);//BG列
                            value = "　";
                            cell.setCellValue((String) value);
                            //有価物収集有り
                            row = sheet1.getRow(47);//48行目
                            cell = row.getCell(60);//CC列
                            value = "　";
                            cell.setCellValue((String) value);
                            //有価物収集無し
                            row = sheet1.getRow(47);//48行目
                            cell = row.getCell(65);//CC列
                            value = "　";
                            cell.setCellValue((String) value);
                            //実績数量
                            row = sheet1.getRow(47);//48行目
                            cell = row.getCell(80);//CC列
                            value = "　";
                            cell.setCellValue((String) value);
                            //実績数量単位 ton
                            row = sheet1.getRow(47);//48行目
                            cell = row.getCell(89);//CL列
                            value = "　";
                            cell.setCellValue((String) value);
                            //実績数量単位 m3
                            row = sheet1.getRow(47);//42行目
                            cell = row.getCell(92);//CO列
                            value = "　";
                            cell.setCellValue((String) value);
                        }
                    }

                    //運搬先の事業場（処分業者の処理施設）
                    //〒
                    row = sheet1.getRow(35);//36行目
                    cell = row.getCell(105);//DB列
                    value = rs.getString("cont_office_zip_code");
                    cell.setCellValue((String) value);
                    //運搬先の事業場
                    row = sheet1.getRow(35);//36行目
                    cell = row.getCell(115);//DL列
                    value = " ";
                    cell.setCellValue((String) value);
                    //住所
                    row = sheet1.getRow(36);//37行目
                    cell = row.getCell(98);//CU列
                    value = rs.getString("cont_office_address1") != null ? rs.getString("cont_office_address1") : " "
                             + rs.getString("cont_office_address2") != null ? rs.getString("cont_office_address2") : " ";
                    cell.setCellValue((String) value);
                    //名称
                    row = sheet1.getRow(37);//38行目
                    cell = row.getCell(106);//DC列
                    value = rs.getString("cont_office_name");
                    cell.setCellValue((String) value);
                    //電話
                    row = sheet1.getRow(38);//39行目
                    cell = row.getCell(106);//DC列
                    value = rs.getString("cont_office_tel");
                    cell.setCellValue((String) value);
                    //処分方法中間1
                    row = sheet1.getRow(39);//40行目
                    cell = row.getCell(108);//DE列
                    value = (rs.getBoolean("check_flag09") ? "○" : "　");
                    cell.setCellValue((String) value);
                    //処分方法中間2
                    row = sheet1.getRow(39);//40行目
                    cell = row.getCell(114);//DK列
                    value = (rs.getBoolean("check_flag10") ? "○" : "　");
                    cell.setCellValue((String) value);
                    //処分方法中間3
                    row = sheet1.getRow(39);//40行目
                    cell = row.getCell(120);//DQ列
                    value = (rs.getBoolean("check_flag11") ? "○" : "　");
                    cell.setCellValue((String) value);
                    //処分方法中間４
                    row = sheet1.getRow(39);//40行目
                    cell = row.getCell(126);//DW列
                    value = " ";
//                    value = (rs.getString("method_name").length() > 0 ? rs.getString("method_name") : "　");
                    cell.setCellValue((String) value);
                    //処分方法最終1
                    row = sheet1.getRow(41);//42行目
                    cell = row.getCell(108);//DE列
                    value = (rs.getBoolean("check_flag12") ? "○" : "　");
                    cell.setCellValue((String) value);
                    //処分方法最終2
                    row = sheet1.getRow(41);//42行目
                    cell = row.getCell(116);//DM列
                    value = (rs.getBoolean("check_flag13") ? "○" : "　");
                    cell.setCellValue((String) value);
                    //処分方法最終3
                    row = sheet1.getRow(41);//42行目
                    cell = row.getCell(123);//DT列
                    value = (rs.getBoolean("check_flag14") ? "○" : "　");
                    cell.setCellValue((String) value);

                    //追加記載事項
                    row = sheet1.getRow(44);//45行目
                    cell = row.getCell(98);//CU列
                    value = (rs.getString("remark_name").length() > 0 ? rs.getString("remark_name") : "　");
                    cell.setCellValue((String) value);

                    //trans_sub1_print_flag
                    if (!rs.getBoolean("trans_sub1_print_flag")) {
                        //運搬受託１
                        row = sheet1.getRow(51);//52行目
                        cell = row.getCell(2);//C列
                        value = rs.getString("trans_office_sub_name1");
                        cell.setCellValue((String) value);
                        //運搬受託１
                        row = sheet1.getRow(53);//54行目
                        cell = row.getCell(2);//C列
                        value = rs.getString("trans1_sub_name");
                        cell.setCellValue((String) value);
                        if (rs.getDate("trans1_date") != null) {
                            //一つのセルに年月日をまとめて貼り付け
                            row = sheet1.getRow(54);//55行目
                            cell = row.getCell(2);//C列
                            value = rs.getDate("trans1_date") != null ? rs.getDate("trans1_date") : " ";
                            cell.setCellValue((Date) value);
                        } else {
                            //運搬受託年月日（年月日）
                            row = sheet1.getRow(54);//55行目
                            cell = row.getCell(2);//C列
                            value = " ";
                            cell.setCellValue((String) value);
                        }

                    } else {
                        //運搬受託１
                        row = sheet1.getRow(51);//52行目
                        cell = row.getCell(2);//C列
                        value = " ";
                        cell.setCellValue((String) value);
                        //運搬受託１
                        row = sheet1.getRow(53);//54行目
                        cell = row.getCell(2);//C列
                        value = " ";
                        cell.setCellValue((String) value);
                        //運搬受託年月日（年）
                        row = sheet1.getRow(54);//55行目
                        cell = row.getCell(2);//C列
                        value = " ";
                        cell.setCellValue((String) value);
                    }

                    //運搬の受託２　斜線
                    if (rs.getBoolean("slash_flag03")) {
                        XSSFDrawing drawing04 = sheet1.createDrawingPatriarch();
                        XSSFClientAnchor anchor04 = new XSSFClientAnchor(0, 0, 0, 0, 19, 49, 36, 56);
                        XSSFSimpleShape shape04 = drawing04.createSimpleShape(anchor04);
                        shape04.setShapeType(ShapeTypes.LINE);                        // 直線
                        shape04.setLineWidth(1);                                     // 太さ(pt)
                        shape04.setLineStyleColor(0, 0, 0);
                        //運搬受託２
                        row = sheet1.getRow(51);//52行目
                        cell = row.getCell(19);//T列
                        value = " ";
                        cell.setCellValue((String) value);
                        //運搬受託２
                        row = sheet1.getRow(53);//54行目
                        cell = row.getCell(19);//T列
                        value = " ";
                        cell.setCellValue((String) value);
                        //運搬受託年月日（年）
                        row = sheet1.getRow(55);//56行目
                        cell = row.getCell(19);//T列
                        value = " ";
                        cell.setCellValue((String) value);
                        //運搬受託年月日（月）
                        row = sheet1.getRow(55);//56行目
                        cell = row.getCell(27);//AB列
                        value = " ";
                        cell.setCellValue((String) value);
                        //運搬受託年月日（日）
                        row = sheet1.getRow(55);//56行目
                        cell = row.getCell(31);//AF列
                        value = " ";
                        cell.setCellValue((String) value);

                    } else if (!rs.getBoolean("trans_sub2_print_flag")) {
                        //value = sdf_dd.format(rs.getDate("trans_office_sub_name2"));
                        //運搬受託２
                        row = sheet1.getRow(51);//52行目
                        cell = row.getCell(19);//T列
                        value = rs.getString("trans_office_sub_name2");
                        cell.setCellValue((String) value);
                        //運搬受託２
                        row = sheet1.getRow(53);//54行目
                        cell = row.getCell(19);//T列
                        value = rs.getString("trans2_sub_name");
                        cell.setCellValue((String) value);

                        if (rs.getDate("trans2_date") != null) {
                            //一つのセルに年月日をまとめて貼り付け
                            row = sheet1.getRow(55);//56行目
                            cell = row.getCell(19);//T列
                            value = rs.getDate("trans2_date") != null ? rs.getDate("trans2_date") : " ";
                            cell.setCellValue((Date) value);
                        } else {
                            //運搬受託年月日（年月日）
                            row = sheet1.getRow(55);//56行目
                            cell = row.getCell(19);//T列
                            value = " ";
                            cell.setCellValue((String) value);
                        }

                    } else {
                        //運搬受託２
                        row = sheet1.getRow(51);//52行目
                        cell = row.getCell(19);//T列
                        value = " ";
                        cell.setCellValue((String) value);
                        //運搬受託２
                        row = sheet1.getRow(53);//54行目
                        cell = row.getCell(19);//T列
                        value = " ";
                        cell.setCellValue((String) value);
                        //運搬受託年月日（年）
                        row = sheet1.getRow(55);//56行目
                        cell = row.getCell(19);//T列
                        value = " ";
                        cell.setCellValue((String) value);
                    }

                    if (!rs.getBoolean("cont_sub1_print_flag")) {
                        //処分受託（受領）
                        row = sheet1.getRow(51);//52行目
                        cell = row.getCell(38);//AM列
                        value = rs.getString("cont_office_sub_name1");
                        cell.setCellValue((String) value);
                        //処分の受託担当者1
                        row = sheet1.getRow(53);//54行目
                        cell = row.getCell(38);//AM列
                        value = rs.getString("cont_sub_name");
                        cell.setCellValue((String) value);
                        if (rs.getDate("cont_date1") != null) {
                            //一つのセルに年月日をまとめて貼り付け
                            row = sheet1.getRow(55);//56行目
                            cell = row.getCell(38);//AM列
                            value = rs.getDate("cont_date1") != null ? rs.getDate("cont_date1") : " ";
                            cell.setCellValue((Date) value);
                        } else {
                            //処分受領年（年月日）
                            row = sheet1.getRow(55);//56行目
                            cell = row.getCell(38);//AM列
                            value = " ";
                            cell.setCellValue((String) value);
                        }

                    } else {
                        //処分受託（受領）
                        row = sheet1.getRow(51);//52行目
                        cell = row.getCell(38);//AM列
                        value = " ";
                        cell.setCellValue((String) value);
                        //処分の受託担当者1
                        row = sheet1.getRow(53);//54行目
                        cell = row.getCell(38);//AM列
                        value = " ";
                        cell.setCellValue((String) value);
                        //処分受領年（年）
                        row = sheet1.getRow(55);//56行目
                        cell = row.getCell(38);//AM列
                        value = " ";
                        cell.setCellValue((String) value);
                    }

                    if (!rs.getBoolean("cont_sub2_print_flag")) {
                        //処分受託（処分）
                        row = sheet1.getRow(51);//52行目
                        cell = row.getCell(56);//BE列
                        value = rs.getString("cont_office_sub_name2");
                        cell.setCellValue((String) value);
                        //処分の受託担当者1
                        row = sheet1.getRow(53);//54行目
                        cell = row.getCell(56);//BE列
                        value = rs.getString("cont_sub_name2");
                        cell.setCellValue((String) value);

                        //一つのセルに年月日をまとめて貼り付け
                        if (rs.getDate("cont_date2") != null) {
                            row = sheet1.getRow(55);//56行目
                            cell = row.getCell(56);//BE列
                            value = rs.getDate("cont_date2") != null ? rs.getDate("cont_date2") : " ";
                            cell.setCellValue((Date) value);
                        } else {
                            //一つのセルに年月日をまとめて貼り付け
                            row = sheet1.getRow(55);//56行目
                            cell = row.getCell(56);//BE列
                            value = " ";
                            cell.setCellValue((String) value);
                        }
                    } else {
                        //処分受託（受領）
                        row = sheet1.getRow(51);//52行目
                        cell = row.getCell(56);//BE列
                        value = " ";
                        cell.setCellValue((String) value);
                        //処分の受託担当者1
                        row = sheet1.getRow(53);//54行目
                        cell = row.getCell(56);//BE列
                        value = " ";
                        cell.setCellValue((String) value);
                        //処分受領年（年）
                        row = sheet1.getRow(55);//56行目
                        cell = row.getCell(56);//BE列
                        value = " ";
                        cell.setCellValue((String) value);
                    }
                    //最終処分確認印字チェック追加 20151019 //////////////////////////////////////////////
                    if (!rs.getBoolean("last_tantou_print_flag")) {
                        //最終処分確認欄　社名
                        row = sheet1.getRow(52);//53行目
                        cell = row.getCell(76);//BY列
                        value = rs.getString("cont_office_sub_name2");
                        cell.setCellValue((String) value);
                        //最終処分の担当者
                        row = sheet1.getRow(54);//55行目
                        cell = row.getCell(76);//BY列
                        value = rs.getString("last_tantou");
                        cell.setCellValue((String) value);

                        //一つのセルに年月日をまとめて貼り付け
                        if (rs.getDate("disposal_date2") != null) {
                            row = sheet1.getRow(50);//51行目
                            cell = row.getCell(76);//BY列
                            value = rs.getDate("disposal_date2") != null ? rs.getDate("disposal_date2") : " ";
                            cell.setCellValue((Date) value);
                        } else {
                            row = sheet1.getRow(50);//51行目
                            cell = row.getCell(76);//BY列
                            value = " ";
                            cell.setCellValue((String) value);
                        }
                    } else {
                        //最終処分確認欄　社名
                        row = sheet1.getRow(52);//53行目
                        cell = row.getCell(76);//BY列
                        value = " ";
                        cell.setCellValue((String) value);
                        //最終処分の担当者
                        row = sheet1.getRow(54);//55行目
                        cell = row.getCell(76);//BY列
                        value = " ";
                        cell.setCellValue((String) value);
                        //処分受領年（年）
                        row = sheet1.getRow(50);//51行目
                        cell = row.getCell(76);//BY列
                        value = " ";
                        cell.setCellValue((String) value);
                    }

                    //最終処分の場所
                    row = sheet1.getRow(58);//59行目
                    cell = row.getCell(2);//C列
                    value = rs.getString("last_office_name").replaceAll("　", "\r\n");
                    cell.setCellValue((String) value);
                }
            } finally {
                ps.close();
            }

            wb1.getCreationHelper().createFormulaEvaluator().evaluateAll();
            //印刷シート選択
            sheet1.setSelected(true);

            //印刷するかプレビューするか確認
            String s0 = new StringBuilder().append("select * ").append(" from m_manifesto where sno = 1; ").toString();
            boolean b直接印刷 = false;
            PreparedStatement ps0 = con.prepareStatement(s0);

            try {
                ResultSet rs0 = ps0.executeQuery();

                if (rs0.next()) {
                    b直接印刷 = rs0.getBoolean("manifesto_print_flag");
                }

            } finally {
                ps0.close();
            }

            try {
                //自身のtmpに保存
                out1 = new FileOutputStream(storeFol);
                wb1.write(out1);
                //スクリプトから印刷
                print_out = new File(storeFol);
                doPrintXlsx(print_out.getAbsolutePath(), printer[2], printer[1], 1, b直接印刷);

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

    //直行型マニフェスト
    private void printManifestoReport2(
             String file_path,
             String storeFol,
             Connection con,
             Long sno) {

        try {
            InputStream in1 = new FileInputStream(file_path);
            wb1 = WorkbookFactory.create(in1);
            XSSFSheet sheet1 = ((XSSFWorkbook) wb1).getSheet("print");

            FileOutputStream out1 = null;
            File print_out = null;

            Row row;
            Cell cell;
            Object value = null;
            String[] printer = getPrinter();

            ///////////////////////////////////////////////////////////////////////////////////
            String s = new StringBuilder().append("select * ").append(" from v1_weigh_data where sno = ?; ").toString();
            PreparedStatement ps = con.prepareStatement(s);
            try {
                ps.setObject(1, sno);
                ResultSet rs = ps.executeQuery();

                if (rs.next()) {

                    //データ貼り付け
                    if (rs.getDate("grant_date1") != null && !rs.getBoolean("grant2_print_f")) {
                        row = sheet1.getRow(2);//3行目
                        cell = row.getCell(8);//I列
                        value = rs.getDate("grant_date1") != null ? rs.getDate("grant_date1") : " ";
                        cell.setCellValue((Date) value);
                    } else {
                        //交付年月日（年月日）
                        row = sheet1.getRow(2);//3行目
                        cell = row.getCell(8);//I列
                        value = "　";
                        cell.setCellValue((String) value);
                    }
                    if (!rs.getBoolean("tantou_print_f")) {
                        //交付担当者氏名
                        row = sheet1.getRow(2);//3行目
                        cell = row.getCell(85);//CH列
                        value = rs.getString("grant_name");
                        cell.setCellValue((String) value);
                    } else {
                        //交付担当者氏名
                        row = sheet1.getRow(2);//3行目
                        cell = row.getCell(85);//CH列
                        value = "　";
                        cell.setCellValue((String) value);
                    }
                    if (!rs.getBoolean("ent_print_flag")) {
                        //排出業者〒
                        row = sheet1.getRow(5);//6行目
                        cell = row.getCell(12);//M列
                        value = rs.getString("ent_zip_code");
                        cell.setCellValue((String) value);
                        //排出業者住所1
                        row = sheet1.getRow(6);//7行目
                        cell = row.getCell(5);//F列
                        value = rs.getString("ent_address1");
                        cell.setCellValue((String) value);
                        //排出業者住所2
                        row = sheet1.getRow(7);//8行目
                        cell = row.getCell(5);//F列
                        value = rs.getString("ent_address2");
                        cell.setCellValue((String) value);
                        //排出業者正式名称
                        row = sheet1.getRow(4);//5行目
                        cell = row.getCell(5);//F列
                        value = rs.getString("ent_p_name");
                        cell.setCellValue((String) value);
                        //排出業者電話
                        row = sheet1.getRow(5);//6行目
                        cell = row.getCell(32);//AG列
                        value = rs.getString("ent_tel");
                        cell.setCellValue((String) value);
                    } else {
                        //排出業者〒
                        row = sheet1.getRow(5);//6行目
                        cell = row.getCell(12);//M列
                        value = "　";
                        cell.setCellValue((String) value);
                        //排出業者住所1
                        row = sheet1.getRow(6);//7行目
                        cell = row.getCell(5);//F列
                        value = "　";
                        cell.setCellValue((String) value);
                        //排出業者住所2
                        row = sheet1.getRow(7);//8行目
                        cell = row.getCell(5);//F列
                        value = "　";
                        cell.setCellValue((String) value);
                        //排出業者正式名称
                        row = sheet1.getRow(4);//5行目
                        cell = row.getCell(5);//F列
                        value = "　";
                        cell.setCellValue((String) value);
                        //排出業者電話
                        row = sheet1.getRow(5);//6行目
                        cell = row.getCell(32);//AG列
                        value = "　";
                        cell.setCellValue((String) value);

                    }
                    if (!rs.getBoolean("office_print_flag")) {
                        //排出業現場〒
                        row = sheet1.getRow(5);//6行目
                        cell = row.getCell(73);//BV列
                        value = rs.getString("office_zip_code");
                        cell.setCellValue((String) value);
                        //排出現場住所1
                        row = sheet1.getRow(6);//7行目
                        cell = row.getCell(65);//BN列
                        value = rs.getString("office_address1");
                        cell.setCellValue((String) value);
                        //排出現場住所2
                        row = sheet1.getRow(7);//8行目
                        cell = row.getCell(65);//BN列
                        value = rs.getString("office_address2");
                        cell.setCellValue((String) value);
                        //排出現場正式名称
                        row = sheet1.getRow(4);//5行目
                        cell = row.getCell(65);//BN列
                        //現場印字をマスタpname + subnameに変更
                        value = rs.getString("office_p_name") + rs.getString("office_sub_name");
                        cell.setCellValue((String) value);

                        //排出現場電話
                        row = sheet1.getRow(5);//6行目
                        cell = row.getCell(93);//CP列
                        value = rs.getString("office_tel");
                        cell.setCellValue((String) value);
                    } else {
                        //排出業現場〒
                        row = sheet1.getRow(5);//6行目
                        cell = row.getCell(73);//BV列
                        value = "　";
                        cell.setCellValue((String) value);
                        //排出現場住所1
                        row = sheet1.getRow(6);//7行目
                        cell = row.getCell(65);//BN列
                        value = "　";
                        cell.setCellValue((String) value);
                        //排出現場住所2
                        row = sheet1.getRow(7);//8行目
                        cell = row.getCell(65);//BN列
                        value = "　";
                        cell.setCellValue((String) value);
                        //排出現場正式名称
                        row = sheet1.getRow(4);//5行目
                        cell = row.getCell(65);//BN列
                        value = "　";
                        cell.setCellValue((String) value);
                        //排出現場電話
                        row = sheet1.getRow(5);//6行目
                        cell = row.getCell(93);//CP列
                        value = "　";
                        cell.setCellValue((String) value);
                    }
                    //////////品目関係///////////////////////////////////////////////

                    if (!rs.getBoolean("tani_print_f")) {
                        //普通の産業廃棄物
                        row = sheet1.getRow(8);//9行目
                        cell = row.getCell(10);//K列
                        value = (rs.getBoolean("goods_flag01") ? "レ" : "　");
                        cell.setCellValue((String) value);
                        //特別管理産業廃棄物
                        row = sheet1.getRow(8);//9行目
                        cell = row.getCell(46);//AU列
                        value = (rs.getBoolean("goods_flag02") ? "レ" : "　");
                        cell.setCellValue((String) value);

                        //燃えがら///////////////////////////////////////////////////////////////////////////////////////////////
                        row = sheet1.getRow(9);//10行目
                        cell = row.getCell(4);//E列
                        value = (rs.getBoolean("goods_flag03") ? "レ" : "　");
                        cell.setCellValue((String) value);
                        //汚泥/////////////////////////////////////////////////
                        row = sheet1.getRow(10);//11行目
                        cell = row.getCell(4);//E列
                        value = (rs.getBoolean("goods_flag04") ? "レ" : "　");
                        cell.setCellValue((String) value);
                        //廃油/////////////////////////////////////////////////
                        row = sheet1.getRow(11);//12行目
                        cell = row.getCell(4);//E列
                        value = (rs.getBoolean("goods_flag05") ? "レ" : "　");
                        cell.setCellValue((String) value);
                        //廃酸/////////////////////////////////////////////////
                        row = sheet1.getRow(12);//13行目
                        cell = row.getCell(4);//E列
                        value = (rs.getBoolean("goods_flag06") ? "レ" : "　");
                        cell.setCellValue((String) value);
                        //廃アルカリ/////////////////////////////////////////////////
                        row = sheet1.getRow(13);//14行目
                        cell = row.getCell(4);//E列
                        value = (rs.getBoolean("goods_flag07") ? "レ" : "　");
                        cell.setCellValue((String) value);
                        //廃プラ/////////////////////////////////////////////////
                        row = sheet1.getRow(14);//15行目
                        cell = row.getCell(4);//E列
                        value = (rs.getBoolean("goods_flag08") ? "レ" : "　");
                        cell.setCellValue((String) value);
                        //紙くず/////////////////////////////////////////////////
                        row = sheet1.getRow(15);//16行目
                        cell = row.getCell(4);//E列
                        value = (rs.getBoolean("goods_flag09") ? "レ" : "　");
                        cell.setCellValue((String) value);
                        //木くず//////////////////////////////////////
                        row = sheet1.getRow(16);//17行目
                        cell = row.getCell(4);//E列
                        value = (rs.getBoolean("goods_flag10") ? "レ" : "　");
                        cell.setCellValue((String) value);
                        //繊維くず/////////////////////////////////////////////////
                        row = sheet1.getRow(17);//18行目
                        cell = row.getCell(4);//E列
                        value = (rs.getBoolean("goods_flag11") ? "レ" : "　");
                        cell.setCellValue((String) value);
                        //動物性残さ/////////////////////////////////////////////////
                        row = sheet1.getRow(18);//19行目
                        cell = row.getCell(4);//E列
                        value = (rs.getBoolean("goods_flag12") ? "レ" : "　");
                        cell.setCellValue((String) value);
                        //ゴムくず/////////////////////////////////////////////////
                        row = sheet1.getRow(19);//20行目
                        cell = row.getCell(4);//E列
                        value = (rs.getBoolean("goods_flag13") ? "レ" : "　");
                        cell.setCellValue((String) value);
                        //金属くず///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
                        row = sheet1.getRow(9);//10行目
                        cell = row.getCell(22);//W列
                        value = (rs.getBoolean("goods_flag14") ? "レ" : "　");
                        cell.setCellValue((String) value);
                        //ガラスコンクリート陶磁器くず/////////////////////////////
                        row = sheet1.getRow(10);//11行目
                        cell = row.getCell(22);//W列
                        value = (rs.getBoolean("goods_flag15") ? "レ" : "　");
                        cell.setCellValue((String) value);
                        //鉱さい/////////////////////////////////////////////////
                        row = sheet1.getRow(11);//12行目
                        cell = row.getCell(22);//W列
                        value = (rs.getBoolean("goods_flag16") ? "レ" : "　");
                        cell.setCellValue((String) value);
                        //がれき類/////////////////////////////////////////////////
                        row = sheet1.getRow(12);//13行目
                        cell = row.getCell(22);//W列
                        value = (rs.getBoolean("goods_flag17") ? "レ" : "　");
                        cell.setCellValue((String) value);
                        //家畜のふん尿/////////////////////////////////////////////
                        row = sheet1.getRow(13);//14行目
                        cell = row.getCell(22);//W列
                        value = (rs.getBoolean("goods_flag18") ? "レ" : "　");
                        cell.setCellValue((String) value);
                        //家畜の死体/////////////////////////////////////////////////
                        row = sheet1.getRow(14);//15行目
                        cell = row.getCell(22);//W列
                        value = (rs.getBoolean("goods_flag19") ? "レ" : "　");
                        cell.setCellValue((String) value);
                        //ばいじん/////////////////////////////////////////////////
                        row = sheet1.getRow(15);//16行目
                        cell = row.getCell(22);//W列
                        value = (rs.getBoolean("goods_flag20") ? "レ" : "　");
                        cell.setCellValue((String) value);
                        //13号廃棄物/////////////////////////////////////////////////
                        row = sheet1.getRow(16);//17行目
                        cell = row.getCell(22);//W列
                        value = (rs.getBoolean("goods_flag21") ? "レ" : "　");
                        cell.setCellValue((String) value);
                        //動物系固形不要物/////////////////////////////////////////////////
                        row = sheet1.getRow(17);//18行目
                        cell = row.getCell(22);//W列
                        value = (rs.getBoolean("goods_flag22") ? "レ" : "　");
                        cell.setCellValue((String) value);
                        //引火性廃油/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
                        row = sheet1.getRow(9);//10行目
                        cell = row.getCell(40);//AO列
                        value = (rs.getBoolean("goods_flag23") ? "レ" : "　");
                        cell.setCellValue((String) value);
                        ////引火性廃油有害//////////////////
                        row = sheet1.getRow(10);//11行目
                        cell = row.getCell(40);//AO列
                        value = (rs.getBoolean("goods_flag24") ? "レ" : "　");
                        cell.setCellValue((String) value);
                        ////強酸//////////////////
                        row = sheet1.getRow(11);//12行目
                        cell = row.getCell(40);//AO列
                        value = (rs.getBoolean("goods_flag25") ? "レ" : "　");
                        cell.setCellValue((String) value);
                        ////強酸有害//////////////////
                        row = sheet1.getRow(12);//13行目
                        cell = row.getCell(40);//AO列
                        value = (rs.getBoolean("goods_flag26") ? "レ" : "　");
                        cell.setCellValue((String) value);
                        ////強アルカリ//////////////////
                        row = sheet1.getRow(13);//14行目
                        cell = row.getCell(40);//AO列
                        value = (rs.getBoolean("goods_flag27") ? "レ" : "　");
                        cell.setCellValue((String) value);
                        ////強アルカリ有害//////////////////
                        row = sheet1.getRow(14);//15行目
                        cell = row.getCell(40);//AO列
                        value = (rs.getBoolean("goods_flag28") ? "レ" : "　");
                        cell.setCellValue((String) value);
                        ////感染性廃棄物//////////////////
                        row = sheet1.getRow(15);//16行目
                        cell = row.getCell(40);//AO列
                        value = (rs.getBoolean("goods_flag29") ? "レ" : "　");
                        cell.setCellValue((String) value);
                        ////PCB//////////////////
                        row = sheet1.getRow(16);//17行目
                        cell = row.getCell(40);//AO列
                        value = (rs.getBoolean("goods_flag30") ? "レ" : "　");
                        cell.setCellValue((String) value);
                        ////廃石綿等//////////////////
                        row = sheet1.getRow(17);//18行目
                        cell = row.getCell(40);//AO列
                        value = (rs.getBoolean("goods_flag31") ? "レ" : "　");
                        cell.setCellValue((String) value);
                        ////指定下水汚泥//////////////////
                        row = sheet1.getRow(18);//19行目
                        cell = row.getCell(40);//AO列
                        value = (rs.getBoolean("goods_flag32") ? "レ" : "　");
                        cell.setCellValue((String) value);
                        ////鉱さい有害//////////////////
                        row = sheet1.getRow(19);//20行目
                        cell = row.getCell(40);//AO列
                        value = (rs.getBoolean("goods_flag33") ? "レ" : "　");
                        cell.setCellValue((String) value);
                        //燃え殻有害/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
                        row = sheet1.getRow(9);//10行目
                        cell = row.getCell(58);//BG列
                        value = (rs.getBoolean("goods_flag34") ? "レ" : "　");
                        cell.setCellValue((String) value);
                        ////廃油有害//////////////////
                        row = sheet1.getRow(10);//11行目
                        cell = row.getCell(58);//BG列
                        value = (rs.getBoolean("goods_flag35") ? "レ" : "　");
                        cell.setCellValue((String) value);
                        ////汚泥有害//////////////////
                        row = sheet1.getRow(11);//12行目
                        cell = row.getCell(58);//BG列
                        value = (rs.getBoolean("goods_flag36") ? "レ" : "　");
                        cell.setCellValue((String) value);
                        ////廃酸有害//////////////////
                        row = sheet1.getRow(12);//13行目
                        cell = row.getCell(58);//BG列
                        value = (rs.getBoolean("goods_flag37") ? "レ" : "　");
                        cell.setCellValue((String) value);
                        ////強アルカリ有害//////////////////
                        row = sheet1.getRow(13);//14行目
                        cell = row.getCell(58);//BG列
                        value = (rs.getBoolean("goods_flag38") ? "レ" : "　");
                        cell.setCellValue((String) value);
                        ////ばいじん有害//////////////////
                        row = sheet1.getRow(14);//15行目
                        cell = row.getCell(58);//BG列
                        value = (rs.getBoolean("goods_flag39") ? "レ" : "　");
                        cell.setCellValue((String) value);
                        ////13号廃棄物//////////////////
                        row = sheet1.getRow(15);//16行目
                        cell = row.getCell(58);//BG列
                        value = (rs.getBoolean("goods_flag40") ? "レ" : "　");
                        cell.setCellValue((String) value);

                        ////廃水銀等//////////////////
                        row = sheet1.getRow(16);//17行目
                        cell = row.getCell(58);//BG列
                        value = (rs.getBoolean("goods_flag42") ? "レ" : "　");
                        cell.setCellValue((String) value);
                        ////廃水銀等//////////////////
                        row = sheet1.getRow(17);//18行目
                        cell = row.getCell(77);//BZ列
                        value = (rs.getBoolean("goods_flag43") ? "レ" : "　");
                        cell.setCellValue((String) value);
                        ////廃水銀等//////////////////
                        row = sheet1.getRow(18);//19行目
                        cell = row.getCell(77);//BZ列
                        value = (rs.getBoolean("goods_flag44") ? "レ" : "　");
                        cell.setCellValue((String) value);
                        ////廃水銀等//////////////////
                        row = sheet1.getRow(19);//20行目
                        cell = row.getCell(77);//BZ列
                        value = (rs.getBoolean("goods_flag45") ? "レ" : "　");
                        cell.setCellValue((String) value);
                        ////廃水銀等//////////////////
                        row = sheet1.getRow(20);//21行目
                        cell = row.getCell(77);//BZ列
                        value = (rs.getBoolean("goods_flag46") ? "レ" : "　");
                        cell.setCellValue((String) value);

                        //数量///////////////////////////////////////////////// 
                        row = sheet1.getRow(9);//10行目
                        cell = row.getCell(75);//BX列
                        value = (!rs.getBoolean("nisugata_print_f") && rs.getString("trans_net_weight").length() > 0 ? rs.getString("trans_net_weight") : "　");
                        cell.setCellValue((String) value);
                        //荷姿/////////////////////////////////////////////////
                        row = sheet1.getRow(9);//10行目
                        cell = row.getCell(95);//CR列
                        value = (!rs.getBoolean("nisugata_print_f") ? rs.getString("nisugata_name") : "　");
                        cell.setCellValue((String) value);
                        //廃棄物名/////////////////////////////////////////////////
                        row = sheet1.getRow(12);//13行目
                        cell = row.getCell(75);//BX列
                        value = (!rs.getBoolean("nisugata_print_f") ? rs.getString("trans3_sub_name") : "　");
                        cell.setCellValue((String) value);
                        //有害物質等/////////////////////////////////////////////////keijyo_print_f  yugai_name slash_flag03 
                        row = sheet1.getRow(14);//15行目
                        cell = row.getCell(75);//BX列
                        value = (!rs.getBoolean("keijyo_print_f") && !rs.getBoolean("slash_flag03") ? rs.getString("yugai_name") : "　");
                        cell.setCellValue((String) value);
                        if (rs.getBoolean("slash_flag03")) {
                            XSSFDrawing drawing01 = sheet1.createDrawingPatriarch();
                            XSSFClientAnchor anchor01 = new XSSFClientAnchor(0, 0, 0, 0, 75, 14, 95, 16);
                            XSSFSimpleShape shape01 = drawing01.createSimpleShape(anchor01);
                            shape01.setShapeType(ShapeTypes.LINE);                        // 直線
                            shape01.setLineWidth(1);                                     // 太さ(pt)
                            shape01.setLineStyleColor(0, 0, 0);
                        }
                        //処分方法/////////////////////////////////////////////////keijyo_print_f  method_name
                        row = sheet1.getRow(14);//15行目
                        cell = row.getCell(95);//CR列
                        value = (!rs.getBoolean("keijyo_print_f") ? rs.getString("method_name") : "　");
                        cell.setCellValue((String) value);
                        //備考欄/////////////////////////////////////////////////keijyo_print_f  method_name
                        row = sheet1.getRow(17);//18行目
                        cell = row.getCell(99);//CV列
                        value = (!rs.getBoolean("keijyo_print_f") ? rs.getString("remark_name") : "　");
                        cell.setCellValue((String) value);

                    } else {
                        //印字無し
                        //普通の産業廃棄物
                        row = sheet1.getRow(8);//9行目
                        cell = row.getCell(10);//K列
                        value = "";
                        cell.setCellValue((String) value);
                        //特別管理産業廃棄物
                        row = sheet1.getRow(8);//9行目
                        cell = row.getCell(46);//AU列
                        value = "";
                        cell.setCellValue((String) value);

                        //燃えがら///////////////////////////////////////////////////////////////////////////////////////////////
                        row = sheet1.getRow(9);//10行目
                        cell = row.getCell(4);//E列
                        value = "";
                        cell.setCellValue((String) value);
                        //汚泥/////////////////////////////////////////////////
                        row = sheet1.getRow(10);//11行目
                        cell = row.getCell(4);//E列
                        value = "";
                        cell.setCellValue((String) value);
                        //廃油/////////////////////////////////////////////////
                        row = sheet1.getRow(11);//12行目
                        cell = row.getCell(4);//E列
                        value = "";
                        cell.setCellValue((String) value);
                        //廃酸/////////////////////////////////////////////////
                        row = sheet1.getRow(12);//13行目
                        cell = row.getCell(4);//E列
                        value = "";
                        cell.setCellValue((String) value);
                        //廃アルカリ/////////////////////////////////////////////////
                        row = sheet1.getRow(13);//14行目
                        cell = row.getCell(4);//E列
                        value = "";
                        cell.setCellValue((String) value);
                        //廃プラ/////////////////////////////////////////////////
                        row = sheet1.getRow(14);//15行目
                        cell = row.getCell(4);//E列
                        value = "";
                        cell.setCellValue((String) value);
                        //紙くず/////////////////////////////////////////////////
                        row = sheet1.getRow(15);//16行目
                        cell = row.getCell(4);//E列
                        value = "";
                        cell.setCellValue((String) value);
                        //木くず//////////////////////////////////////
                        row = sheet1.getRow(16);//17行目
                        cell = row.getCell(4);//E列
                        value = "";
                        cell.setCellValue((String) value);
                        //繊維くず/////////////////////////////////////////////////
                        row = sheet1.getRow(17);//18行目
                        cell = row.getCell(4);//E列
                        value = "";
                        cell.setCellValue((String) value);
                        //動物性残さ/////////////////////////////////////////////////
                        row = sheet1.getRow(18);//19行目
                        cell = row.getCell(4);//E列
                        value = "";
                        cell.setCellValue((String) value);
                        //ゴムくず/////////////////////////////////////////////////
                        row = sheet1.getRow(19);//20行目
                        cell = row.getCell(4);//E列
                        value = "";
                        cell.setCellValue((String) value);
                        //金属くず/////////////////////////////////////////////////
                        row = sheet1.getRow(9);//10行目
                        cell = row.getCell(22);//W列
                        value = "";
                        cell.setCellValue((String) value);
                        //ガラスコンクリート陶磁器くず/////////////////////////////
                        row = sheet1.getRow(10);//11行目
                        cell = row.getCell(22);//W列
                        value = "";
                        cell.setCellValue((String) value);
                        //鉱さい/////////////////////////////////////////////////
                        row = sheet1.getRow(11);//12行目
                        cell = row.getCell(22);//W列
                        value = "";
                        cell.setCellValue((String) value);
                        //がれき類/////////////////////////////////////////////////
                        row = sheet1.getRow(12);//13行目
                        cell = row.getCell(22);//W列
                        value = "";
                        cell.setCellValue((String) value);
                        //家畜のふん尿/////////////////////////////////////////////
                        row = sheet1.getRow(13);//14行目
                        cell = row.getCell(22);//W列
                        value = "";
                        cell.setCellValue((String) value);
                        //家畜の死体/////////////////////////////////////////////////
                        row = sheet1.getRow(14);//15行目
                        cell = row.getCell(22);//W列
                        value = "";
                        cell.setCellValue((String) value);
                        //ばいじん/////////////////////////////////////////////////
                        row = sheet1.getRow(15);//16行目
                        cell = row.getCell(22);//W列
                        value = "";
                        cell.setCellValue((String) value);
                        //13号廃棄物/////////////////////////////////////////////////
                        row = sheet1.getRow(16);//17行目
                        cell = row.getCell(22);//W列
                        value = "";
                        cell.setCellValue((String) value);
                        //動物系固形不要物/////////////////////////////////////////////////
                        row = sheet1.getRow(17);//18行目
                        cell = row.getCell(22);//W列
                        value = "";
                        cell.setCellValue((String) value);
                        //引火性廃油///////////////////////////////////////////////////////
                        row = sheet1.getRow(9);//10行目
                        cell = row.getCell(40);//AO列
                        value = "";
                        cell.setCellValue((String) value);
                        ////引火性廃油有害//////////////////
                        row = sheet1.getRow(10);//11行目
                        cell = row.getCell(40);//AO列
                        value = "";
                        cell.setCellValue((String) value);
                        ////強酸//////////////////
                        row = sheet1.getRow(11);//12行目
                        cell = row.getCell(40);//AO列
                        value = "";
                        cell.setCellValue((String) value);
                        ////強酸有害//////////////////
                        row = sheet1.getRow(12);//13行目
                        cell = row.getCell(40);//AO列
                        value = "";
                        cell.setCellValue((String) value);
                        ////強アルカリ//////////////////
                        row = sheet1.getRow(13);//14行目
                        cell = row.getCell(40);//AO列
                        value = "";
                        cell.setCellValue((String) value);
                        ////強アルカリ有害//////////////////
                        row = sheet1.getRow(14);//15行目
                        cell = row.getCell(40);//AO列
                        value = "";
                        cell.setCellValue((String) value);
                        ////感染性廃棄物//////////////////
                        row = sheet1.getRow(15);//16行目
                        cell = row.getCell(40);//AO列
                        value = "";
                        cell.setCellValue((String) value);
                        ////PCB//////////////////
                        row = sheet1.getRow(16);//17行目
                        cell = row.getCell(40);//AO列
                        value = "";
                        cell.setCellValue((String) value);
                        ////廃石綿等//////////////////
                        row = sheet1.getRow(17);//18行目
                        cell = row.getCell(40);//AO列
                        value = "";
                        cell.setCellValue((String) value);
                        ////指定下水汚泥//////////////////
                        row = sheet1.getRow(18);//19行目
                        cell = row.getCell(40);//AO列
                        value = "";
                        cell.setCellValue((String) value);
                        ////鉱さい有害//////////////////
                        row = sheet1.getRow(19);//20行目
                        cell = row.getCell(40);//AO列
                        value = "";
                        cell.setCellValue((String) value);
                        //燃え殻有害//////////////////////////////////////////////////////
                        row = sheet1.getRow(9);//10行目
                        cell = row.getCell(58);//BG列
                        value = "";
                        cell.setCellValue((String) value);
                        ////廃油有害//////////////////
                        row = sheet1.getRow(10);//11行目
                        cell = row.getCell(58);//BG列
                        value = "";
                        cell.setCellValue((String) value);
                        ////汚泥有害//////////////////
                        row = sheet1.getRow(11);//12行目
                        cell = row.getCell(58);//BG列
                        value = "";
                        cell.setCellValue((String) value);
                        ////廃酸有害//////////////////
                        row = sheet1.getRow(12);//13行目
                        cell = row.getCell(58);//BG列
                        value = "";
                        cell.setCellValue((String) value);
                        ////強アルカリ有害//////////////////
                        row = sheet1.getRow(13);//14行目
                        cell = row.getCell(58);//BG列
                        value = "";
                        cell.setCellValue((String) value);
                        ////ばいじん有害//////////////////
                        row = sheet1.getRow(14);//15行目
                        cell = row.getCell(58);//BG列
                        value = "";
                        cell.setCellValue((String) value);
                        ////13号廃棄物//////////////////
                        row = sheet1.getRow(15);//16行目
                        cell = row.getCell(58);//BG列
                        value = "";
                        cell.setCellValue((String) value);

                        ////廃水銀等//////////////////
                        row = sheet1.getRow(16);//17行目
                        cell = row.getCell(58);//BG列
                        value = "";
                        cell.setCellValue((String) value);
                        ////廃水銀等//////////////////
                        row = sheet1.getRow(17);//18行目
                        cell = row.getCell(77);//BZ列
                        value = "";
                        cell.setCellValue((String) value);
                        ////廃水銀等//////////////////
                        row = sheet1.getRow(18);//19行目
                        cell = row.getCell(77);//BZ列
                        value = "";
                        cell.setCellValue((String) value);
                        ////廃水銀等//////////////////
                        row = sheet1.getRow(19);//20行目
                        cell = row.getCell(77);//BZ列
                        value = "";
                        cell.setCellValue((String) value);
                        ////廃水銀等//////////////////
                        row = sheet1.getRow(20);//21行目
                        cell = row.getCell(77);//BZ列
                        value = "";
                        cell.setCellValue((String) value);

                        //数量///////////////////////////////////////////////// 
                        row = sheet1.getRow(9);//10行目
                        cell = row.getCell(75);//BX列
                        value = (!rs.getBoolean("nisugata_print_f") && rs.getString("trans_net_weight").length() > 0 ? rs.getString("trans_net_weight") : "　");
                        cell.setCellValue((String) value);
                        //荷姿/////////////////////////////////////////////////
                        row = sheet1.getRow(9);//10行目
                        cell = row.getCell(95);//CR列
                        value = (!rs.getBoolean("nisugata_print_f") ? rs.getString("nisugata_name") : "　");
                        cell.setCellValue((String) value);
                        //廃棄物名/////////////////////////////////////////////////
                        row = sheet1.getRow(12);//13行目
                        cell = row.getCell(75);//BX列
                        value = (!rs.getBoolean("nisugata_print_f") ? rs.getString("trans3_sub_name") : "　");
                        cell.setCellValue((String) value);
                        //有害物質等/////////////////////////////////////////////////keijyo_print_f  yugai_name slash_flag03 
                        row = sheet1.getRow(14);//15行目
                        cell = row.getCell(75);//BX列
                        value = (!rs.getBoolean("keijyo_print_f") && !rs.getBoolean("slash_flag03") ? rs.getString("yugai_name") : "　");
                        cell.setCellValue((String) value);
                        if (rs.getBoolean("slash_flag03")) {
                            XSSFDrawing drawing01 = sheet1.createDrawingPatriarch();
                            XSSFClientAnchor anchor01 = new XSSFClientAnchor(0, 0, 0, 0, 75, 14, 95, 16);
                            XSSFSimpleShape shape01 = drawing01.createSimpleShape(anchor01);
                            shape01.setShapeType(ShapeTypes.LINE);                        // 直線
                            shape01.setLineWidth(1);                                     // 太さ(pt)
                            shape01.setLineStyleColor(0, 0, 0);
                        }
                        //処分方法/////////////////////////////////////////////////keijyo_print_f  method_name
                        row = sheet1.getRow(14);//15行目
                        cell = row.getCell(95);//CR列
                        value = (!rs.getBoolean("keijyo_print_f") ? rs.getString("method_name") : "　");
                        cell.setCellValue((String) value);
                        //備考欄/////////////////////////////////////////////////keijyo_print_f  method_name
                        row = sheet1.getRow(17);//18行目
                        cell = row.getCell(99);//CV列
                        value = (!rs.getBoolean("keijyo_print_f") ? rs.getString("remark_name") : "　");
                        cell.setCellValue((String) value);
                    }

                    ///中間処理/////////////////////////////////////////////////////
                    //当欄記載のとおり　斜線
                    if (rs.getBoolean("slash_flag01")) {

                        XSSFDrawing drawing01 = sheet1.createDrawingPatriarch();
                        XSSFClientAnchor anchor01 = new XSSFClientAnchor(0, 0, 0, 0, 31, 21, 77, 23);
                        XSSFSimpleShape shape01 = drawing01.createSimpleShape(anchor01);
                        shape01.setShapeType(ShapeTypes.LINE);                        // 直線
                        shape01.setLineWidth(1);                                     // 太さ(pt)
                        shape01.setLineStyleColor(0, 0, 0);                           // 線の色(黒)

                        //帳簿記載のとおりチェックを空欄に
                        row = sheet1.getRow(21);//22行目
                        cell = row.getCell(4);//E列
                        value = "　";
                        cell.setCellValue((String) value);
                        //当欄記載のとおりチェックを空欄に
                        row = sheet1.getRow(22);//23行目
                        cell = row.getCell(4);//E列
                        value = "　";
                        cell.setCellValue((String) value);
                        //帳簿記載のとおりを空欄に
                        row = sheet1.getRow(21);//22行目
                        cell = row.getCell(31);//AF列
                        value = "　";
                        cell.setCellValue((String) value);
                        //当欄記載のとおりを空欄に
                        row = sheet1.getRow(22);//23行目
                        cell = row.getCell(31);//AF列
                        value = "　";
                        cell.setCellValue((String) value);

                    } else {
                        //帳簿記載のとおりチェック
                        row = sheet1.getRow(21);//22行目
                        cell = row.getCell(4);//E列
                        value = (rs.getBoolean("check_flag01") ? "レ" : "　");
                        cell.setCellValue((String) value);
                        //当欄記載のとおりチェック
                        row = sheet1.getRow(22);//23行目
                        cell = row.getCell(4);//E列
                        value = (rs.getBoolean("check_flag02") ? "レ" : "　");
                        cell.setCellValue((String) value);
                        //帳簿記載のとおりを空欄に
                        row = sheet1.getRow(21);//22行目
                        cell = row.getCell(31);//AF列
                        value = "　";
                        cell.setCellValue((String) value);
                        //当欄記載のとおり
                        row = sheet1.getRow(22);//23行目
                        cell = row.getCell(31);//AF列
                        value = rs.getString("cont_name02") + "  " + rs.getString("cont_no02");
                        cell.setCellValue((String) value);
                    }
                    ///最終処分の場所/////////////////////////////////////////////////////

                    //委託契約書記載のとおりチェック
                    row = sheet1.getRow(24);//25行目
                    cell = row.getCell(4);//E列
                    value = (rs.getBoolean("check_flag03") ? "レ" : "　");
                    cell.setCellValue((String) value);
                    //当欄記載のとおりチェック
                    row = sheet1.getRow(25);//26行目
                    cell = row.getCell(4);//E列
                    value = (rs.getBoolean("check_flag04") ? "レ" : "　");
                    cell.setCellValue((String) value);
                    row = sheet1.getRow(23);//24行目
                    cell = row.getCell(31);//AF列
                    value = (rs.getBoolean("check_flag04") && !(rs.getBoolean("check_flag03")) ? "" : "　");
                    cell.setCellValue((String) value);

                    row = sheet1.getRow(24);//25行目　
                    cell = row.getCell(31);//AF列
                    value = (rs.getBoolean("check_flag04") && !(rs.getBoolean("check_flag03")) ? "㈱スパット北上" : "　");
                    cell.setCellValue((String) value);

                    //運搬受託者1氏名///////////////////////////////////////////////////////////////////////////
                    row = sheet1.getRow(27);//28行目
                    cell = row.getCell(5);//F列
                    value = !rs.getBoolean("trans1_print_flag") ? rs.getString("trans1_p_name") : " ";
                    cell.setCellValue((String) value);
                    //運搬受託１〒
                    row = sheet1.getRow(28);//29行目
                    cell = row.getCell(12);//M列
                    value = !rs.getBoolean("trans1_print_flag") ? rs.getString("trans1_zip_code") : " ";
                    cell.setCellValue((String) value);
                    //運搬受託電話
                    row = sheet1.getRow(28);//29行目
                    cell = row.getCell(32);//AG列
                    value = !rs.getBoolean("trans1_print_flag") ? rs.getString("trans1_tel") : " ";
                    cell.setCellValue((String) value);
                    //運搬受託者1住所1
                    row = sheet1.getRow(29);//30行目
                    cell = row.getCell(5);//F列
                    value = !rs.getBoolean("trans1_print_flag") ? rs.getString("trans1_address1") : " ";
                    cell.setCellValue((String) value);
                    //運搬受託者1住所2
                    row = sheet1.getRow(30);//31行目
                    cell = row.getCell(5);//F列
                    value = !rs.getBoolean("trans1_print_flag") ? rs.getString("trans1_address2") : " ";
                    cell.setCellValue((String) value);

                    //処分受託者1氏名/////////////////////////////////////////////////////////////////////
                    row = sheet1.getRow(32);//33行目
                    cell = row.getCell(5);//F列
                    value = !rs.getBoolean("cont_print_flag") ? rs.getString("cont_p_name") : " ";
                    cell.setCellValue((String) value);
                    //処分受託１〒
                    row = sheet1.getRow(33);//34行目
                    cell = row.getCell(12);//M列
                    value = !rs.getBoolean("cont_print_flag") ? rs.getString("cont_zip_code") : " ";
                    cell.setCellValue((String) value);
                    //処分受託電話
                    row = sheet1.getRow(33);//34行目
                    cell = row.getCell(32);//AG列
                    value = !rs.getBoolean("cont_print_flag") ? rs.getString("cont_tel") : " ";
                    cell.setCellValue((String) value);
                    //処分受託者1住所1
                    row = sheet1.getRow(34);//35行目
                    cell = row.getCell(5);//F列
                    value = !rs.getBoolean("cont_print_flag") ? rs.getString("cont_address1") : " ";
                    cell.setCellValue((String) value);
                    //処分受託者1住所2
                    row = sheet1.getRow(35);//36行目
                    cell = row.getCell(5);//F列
                    value = !rs.getBoolean("cont_print_flag") ? rs.getString("cont_address2") : " ";
                    cell.setCellValue((String) value);

                    //運搬先の事業場名称////////////////////////////////////////////////////////////////
                    row = sheet1.getRow(27);//28行目
                    cell = row.getCell(65);//BN列
                    value = rs.getString("cont_office_name") != null ? rs.getString("cont_office_name") : " ";
                    cell.setCellValue((String) value);
                    //運搬先の事業場１〒
                    row = sheet1.getRow(28);//29行目
                    cell = row.getCell(73);//BU列
                    value = rs.getString("cont_office_zip_code") != null ? rs.getString("cont_office_zip_code") : " ";
                    cell.setCellValue((String) value);
                    //運搬先の事業場電話
                    row = sheet1.getRow(28);//29行目
                    cell = row.getCell(93);//CP列
                    value = rs.getString("cont_office_tel") != null ? rs.getString("cont_office_tel") : " ";
                    cell.setCellValue((String) value);
                    //運搬先の事業場住所1
                    row = sheet1.getRow(29);//30行目
                    cell = row.getCell(65);//BN列
                    value = rs.getString("cont_office_address1") != null ? rs.getString("cont_office_address1") : " ";
                    cell.setCellValue((String) value);
                    //運搬先の事業場住所2
                    row = sheet1.getRow(30);//31行目
                    cell = row.getCell(65);//BN列
                    value = rs.getString("cont_office_address2") != null ? rs.getString("cont_office_address2") : " ";
                    cell.setCellValue((String) value);

                    //積替え保管場所名称///////////////////////////////////////////////////////////////////
                    row = sheet1.getRow(32);//33行目
                    cell = row.getCell(65);//BN列 tsumikae_print_flag 
                    value = !(rs.getBoolean("slash_flag02") || rs.getBoolean("tsumikae_print_flag")) && rs.getString("last_cont_name02").length() > 0 ? rs.getString("last_cont_name02") : " ";
                    cell.setCellValue((String) value);
                    //積替え保管場所〒
                    row = sheet1.getRow(33);//34行目
                    cell = row.getCell(73);//BV列
                    value = !(rs.getBoolean("slash_flag02") || rs.getBoolean("tsumikae_print_flag")) && rs.getString("last_cont_zip02").length() > 0 ? rs.getString("last_cont_zip02") : " ";
                    cell.setCellValue((String) value);
                    //積替え保管場所電話
                    row = sheet1.getRow(33);//34行目
                    cell = row.getCell(93);//CP列
                    value = !(rs.getBoolean("slash_flag02") || rs.getBoolean("tsumikae_print_flag")) && rs.getString("last_cont_tel02").length() > 0 ? rs.getString("last_cont_tel02") : " ";
                    cell.setCellValue((String) value);
                    //積替え保管場所住所1
                    row = sheet1.getRow(34);//35行目
                    cell = row.getCell(65);//BN列
                    value = !(rs.getBoolean("slash_flag02") || rs.getBoolean("tsumikae_print_flag")) && rs.getString("last_cont_address02").length() > 0 ? rs.getString("last_cont_address02") : " ";
                    cell.setCellValue((String) value);
                    //積替え保管場所住所2
                    row = sheet1.getRow(35);//36行目
                    cell = row.getCell(65);//BN列
                    value = " ";
                    cell.setCellValue((String) value);
                    if (rs.getBoolean("slash_flag02")) {
                        XSSFDrawing drawing02 = sheet1.createDrawingPatriarch();
                        XSSFClientAnchor anchor02 = new XSSFClientAnchor(0, 0, 0, 0, 65, 32, 113, 35);
                        XSSFSimpleShape shape02 = drawing02.createSimpleShape(anchor02);
                        shape02.setShapeType(ShapeTypes.LINE);                        // 直線
                        shape02.setLineWidth(1);                                     // 太さ(pt)
                        shape02.setLineStyleColor(0, 0, 0);
                    }

                    //運搬受託　名称
                    row = sheet1.getRow(36);//37行目
                    cell = row.getCell(18);//S列
                    value = !rs.getBoolean("trans_sub1_print_flag") ? rs.getString("trans_office_sub_name1") : " ";
                    cell.setCellValue((String) value);
                    //運搬受託　運搬担当者
                    row = sheet1.getRow(37);//38行目
                    cell = row.getCell(18);//S列
                    value = !rs.getBoolean("trans_sub2_print_flag") ? rs.getString("trans1_sub_name") : " ";
                    cell.setCellValue((String) value);

                    //処分受託　名称
                    row = sheet1.getRow(38);//39行目
                    cell = row.getCell(18);//S列
                    value = !rs.getBoolean("cont_sub1_print_flag") ? rs.getString("cont_office_sub_name1") : " ";
                    cell.setCellValue((String) value);
                    //処分受託　処分担当者
                    row = sheet1.getRow(39);//40行目
                    cell = row.getCell(18);//S列
                    value = !rs.getBoolean("cont_sub2_print_flag") ? rs.getString("cont_sub_name") : " ";
                    cell.setCellValue((String) value);

                    if (rs.getDate("trans1_date") != null) {
                        row = sheet1.getRow(37);//38行目
                        cell = row.getCell(79);//CB列
                        value = rs.getDate("trans1_date") != null ? rs.getDate("trans1_date") : " ";
                        cell.setCellValue((Date) value);

                    } else {
                        //運搬終了年月日（年月日）
                        row = sheet1.getRow(37);//38行目
                        cell = row.getCell(79);//CB列
                        value = " ";
                        cell.setCellValue((String) value);
                    }

                    //処分終了年月日（年） 　元号使用止め西暦表示、入力に変更　180918
                    if (rs.getDate("cont_date1") != null) {
                        row = sheet1.getRow(39);//40行目
                        cell = row.getCell(79);//CB列
                        value = rs.getDate("cont_date1") != null ? rs.getDate("cont_date1") : " ";
                        cell.setCellValue((Date) value);
                    } else {
                        //処分終了年月日（年月日）
                        row = sheet1.getRow(39);//40行目
                        cell = row.getCell(79);//CB列
                        value = " ";
                        cell.setCellValue((String) value);
                    }

                    //最終処分終了年月日（年） 　元号使用止め西暦表示、入力に変更　180918
                    if (rs.getDate("cont_date3") != null) {
                        row = sheet1.getRow(39);//40行目
                        cell = row.getCell(103);//CZ列
                        value = rs.getDate("cont_date3") != null ? rs.getDate("cont_date3") : " ";
                        cell.setCellValue((Date) value);
                    } else {
                        //最終処分終了年月日（年月日）
                        row = sheet1.getRow(39);//40行目
                        cell = row.getCell(103);//CZ列
                        value = " ";
                        cell.setCellValue((String) value);
                    }

                    //有価物拾集量
                    row = sheet1.getRow(37);//38行目
                    cell = row.getCell(102);//CY列
                    value = rs.getString("trans_weight");
                    cell.setCellValue((String) value);

                    //最終処分を行った場所
                    row = sheet1.getRow(42);//43行目
                    cell = row.getCell(6);//G列
                    value = rs.getString("last_office_name").replaceAll("　", "\r\n");
                    cell.setCellValue((String) value);
                }
            } finally {
                ps.close();
            }

            wb1.getCreationHelper().createFormulaEvaluator().evaluateAll();
            //印刷シート選択
            sheet1.setSelected(true);

            //印刷するかプレビューするか確認
            String s0 = new StringBuilder().append("select * ").append(" from m_manifesto where sno = 1; ").toString();
            boolean b直接印刷 = false;
            PreparedStatement ps0 = con.prepareStatement(s0);

            try {
                ResultSet rs0 = ps0.executeQuery();

                if (rs0.next()) {
                    b直接印刷 = rs0.getBoolean("manifesto_print_flag");
                }

            } finally {
                ps0.close();
            }

            try {
                //自身のtmpに保存
                out1 = new FileOutputStream(storeFol);
                wb1.write(out1);
                //スクリプトから印刷
                print_out = new File(storeFol);
                doPrintXlsx(print_out.getAbsolutePath(), printer[2], printer[1], 1, b直接印刷);

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

    //建設系マニフェスト最終処分の場所印字
    private void printManifestoReport3(
             String file_path,
             String storeFol,
             Connection con,
             Long sno) {

        try {
            InputStream in1 = new FileInputStream(file_path);
            wb1 = WorkbookFactory.create(in1);
            XSSFSheet sheet1 = ((XSSFWorkbook) wb1).getSheet("print");

            FileOutputStream out1 = null;
            File print_out = null;

            Row row;
            Cell cell;
            Object value = null;
            String[] printer = getPrinter();

            ///////////////////////////////////////////////////////////////////////////////////
            String s = new StringBuilder().append("select * ").append(" from v1_weigh_data where sno = ?; ").toString();
            PreparedStatement ps = con.prepareStatement(s);
            try {
                ps.setObject(1, sno);
                ResultSet rs = ps.executeQuery();

                if (rs.next()) {
                    //セル初期化

                    //データ貼り付け
                    //交付年月日（年）
                    row = sheet1.getRow(4);//5行目
                    cell = row.getCell(2);//C列
                    value = "　";
                    cell.setCellValue((String) value);
                    //交付年月日（月）
                    row = sheet1.getRow(4);//5行目
                    cell = row.getCell(10);//K列
                    value = "　";
                    cell.setCellValue((String) value);
                    //交付年月日（日）
                    row = sheet1.getRow(4);//5行目
                    cell = row.getCell(15);//P列
                    value = "　";
                    cell.setCellValue((String) value);
                    //交付担当者所属
                    row = sheet1.getRow(4);//5行目
                    cell = row.getCell(56);//BE列
                    value = "　";
                    cell.setCellValue((String) value);
                    //交付担当者氏名
                    row = sheet1.getRow(4);//5行目
                    cell = row.getCell(81);//CD列
                    value = "　";
                    cell.setCellValue((String) value);

                    //排出業者〒
                    row = sheet1.getRow(10);//11行目
                    cell = row.getCell(10);//K列
                    value = "　";
                    cell.setCellValue((String) value);
                    //排出業者住所1
                    row = sheet1.getRow(12);//13行目
                    cell = row.getCell(5);//F列
                    value = "　";
                    cell.setCellValue((String) value);
                    //排出業者正式名称
                    row = sheet1.getRow(14);//15行目
                    cell = row.getCell(16);//Q列
                    value = "　";
                    cell.setCellValue((String) value);
                    //排出業者電話
                    row = sheet1.getRow(16);//17行目
                    cell = row.getCell(16);//Q列
                    value = "　";
                    cell.setCellValue((String) value);

                    //排出業現場〒
                    row = sheet1.getRow(10);//11行目
                    cell = row.getCell(57);//BF列
                    value = "　";
                    cell.setCellValue((String) value);
                    //排出現場住所1
                    row = sheet1.getRow(12);//13行目
                    cell = row.getCell(51);//F列
                    value = "　";
                    cell.setCellValue((String) value);
                    //排出現場正式名称
                    row = sheet1.getRow(14);//15行目
                    cell = row.getCell(56);//BE列
                    value = "　";
                    cell.setCellValue((String) value);
                    //排出現場電話
                    row = sheet1.getRow(16);//17行目
                    cell = row.getCell(60);//BI列
                    value = "　";
                    cell.setCellValue((String) value);

                    //////////品目関係///////////////////////////////////////////////
                    //単位チェック ton
                    row = sheet1.getRow(19);//20行目
                    cell = row.getCell(29);//AD列
                    value = " ";
                    cell.setCellValue((String) value);
                    //単位チェック kg
                    row = sheet1.getRow(19);//20行目
                    cell = row.getCell(32);//AG列
                    value = " ";
                    cell.setCellValue((String) value);
                    //単位チェック m3
                    row = sheet1.getRow(19);//20行目
                    cell = row.getCell(35);//AJ列
                    value = " ";
                    cell.setCellValue((String) value);
                    //単位チェック L
                    row = sheet1.getRow(19);//20行目
                    cell = row.getCell(38);//AM列
                    value = " ";
                    cell.setCellValue((String) value);

                    //コンクリートがら//////////////////////////////////////////////
                    row = sheet1.getRow(23);//24行目
                    cell = row.getCell(1);//B列
                    value = " ";
                    cell.setCellValue((String) value);
                    row = sheet1.getRow(23);//24行目
                    cell = row.getCell(19);//T列
                    value = " ";
                    cell.setCellValue((String) value);
                    //アスコンがら/////////////////////////////////////////////////
                    row = sheet1.getRow(24);//25行目
                    cell = row.getCell(1);//B列
                    value = " ";
                    cell.setCellValue((String) value);
                    row = sheet1.getRow(24);//25行目
                    cell = row.getCell(19);//T列
                    value = " ";
                    cell.setCellValue((String) value);
                    //その他がれき/////////////////////////////////////////////////
                    row = sheet1.getRow(25);//26行目
                    cell = row.getCell(1);//B列
                    value = " ";
                    cell.setCellValue((String) value);
                    row = sheet1.getRow(25);//26行目
                    cell = row.getCell(19);//T列
                    value = " ";
                    cell.setCellValue((String) value);
                    //ガラス陶磁器/////////////////////////////////////////////////
                    row = sheet1.getRow(26);//27行目
                    cell = row.getCell(1);//B列
                    value = " ";
                    cell.setCellValue((String) value);
                    row = sheet1.getRow(26);//27行目
                    cell = row.getCell(19);//T列
                    value = " ";
                    cell.setCellValue((String) value);
                    //廃プラ/////////////////////////////////////////////////
                    row = sheet1.getRow(27);//28行目
                    cell = row.getCell(1);//B列
                    value = " ";
                    cell.setCellValue((String) value);
                    row = sheet1.getRow(27);//28行目
                    cell = row.getCell(19);//T列
                    value = " ";
                    cell.setCellValue((String) value);
                    //金属くず/////////////////////////////////////////////////
                    row = sheet1.getRow(28);//29行目
                    cell = row.getCell(1);//B列
                    value = " ";
                    cell.setCellValue((String) value);
                    row = sheet1.getRow(28);//29行目
                    cell = row.getCell(19);//T列
                    value = " ";
                    cell.setCellValue((String) value);

                    //混合/////////////////////////////////////////////////
                    row = sheet1.getRow(23);//24行目
                    cell = row.getCell(27);//AB列
                    value = " ";
                    cell.setCellValue((String) value);
                    row = sheet1.getRow(23);//24行目
                    cell = row.getCell(43);//AR列
                    value = " ";
                    cell.setCellValue((String) value);
                    //石綿含有産業廃棄物/////////////////////////////////////////////////
                    row = sheet1.getRow(24);//25行目
                    cell = row.getCell(27);//AB列
                    value = " ";
                    cell.setCellValue((String) value);
                    row = sheet1.getRow(24);//25行目
                    cell = row.getCell(43);//AR列
                    value = " ";
                    cell.setCellValue((String) value);

                    //建設汚泥/////////////////////////////////////////////////
                    row = sheet1.getRow(23);//24行目
                    cell = row.getCell(51);//AZ列
                    value = " ";
                    cell.setCellValue((String) value);
                    row = sheet1.getRow(23);//24行目
                    cell = row.getCell(67);//BP列
                    value = " ";
                    cell.setCellValue((String) value);
                    //紙くず/////////////////////////////////////////////////
                    row = sheet1.getRow(24);//25行目
                    cell = row.getCell(51);//AZ列
                    value = " ";
                    cell.setCellValue((String) value);
                    row = sheet1.getRow(24);//25行目
                    cell = row.getCell(67);//BP列
                    value = " ";
                    cell.setCellValue((String) value);
                    //木くず/////////////////////////////////////////////////
                    row = sheet1.getRow(25);//24行目
                    cell = row.getCell(51);//AZ列
                    value = " ";
                    cell.setCellValue((String) value);
                    row = sheet1.getRow(25);//24行目
                    cell = row.getCell(67);//BP列
                    value = " ";
                    cell.setCellValue((String) value);
                    //繊維くず/////////////////////////////////////////////////
                    row = sheet1.getRow(26);//25行目
                    cell = row.getCell(51);//AZ列
                    value = " ";
                    cell.setCellValue((String) value);
                    row = sheet1.getRow(26);//25行目
                    cell = row.getCell(67);//BP列
                    value = " ";
                    cell.setCellValue((String) value);
                    //廃石膏ボード/////////////////////////////////////////////////
                    row = sheet1.getRow(27);//24行目
                    cell = row.getCell(51);//AZ列
                    value = " ";
                    cell.setCellValue((String) value);
                    row = sheet1.getRow(27);//24行目
                    cell = row.getCell(67);//BP列
                    value = " ";
                    cell.setCellValue((String) value);
                    //混合型/////////////////////////////////////////////////
                    row = sheet1.getRow(28);//25行目
                    cell = row.getCell(51);//AZ列
                    value = " ";
                    cell.setCellValue((String) value);
                    row = sheet1.getRow(28);//25行目
                    cell = row.getCell(67);//BP列
                    value = " ";
                    cell.setCellValue((String) value);

                    //石綿含有産業廃棄物/////////////////////////////////////////////////
                    row = sheet1.getRow(23);//24行目
                    cell = row.getCell(75);//BX列
                    value = " ";
                    cell.setCellValue((String) value);
                    row = sheet1.getRow(23);//24行目
                    cell = row.getCell(91);//CN列
                    value = " ";
                    cell.setCellValue((String) value);
                    //廃石綿/////////////////////////////////////////////////
                    row = sheet1.getRow(23);//24行目
                    cell = row.getCell(98);//CU列
                    value = " ";
                    cell.setCellValue((String) value);
                    row = sheet1.getRow(23);//24行目
                    cell = row.getCell(114);//DK列
                    value = " ";
                    cell.setCellValue((String) value);

                    //総重量/////////////////////////////////////////////////
                    row = sheet1.getRow(27);//28行目
                    cell = row.getCell(91);//CN列
                    value = " ";
                    cell.setCellValue((String) value);

                    //形状/////////////////////////////////////////////////
                    row = sheet1.getRow(22);//23行目
                    cell = row.getCell(124);//DU列
                    value = " ";
                    cell.setCellValue((String) value);

                    row = sheet1.getRow(23);//24行目
                    cell = row.getCell(124);//DU列
                    value = " ";
                    cell.setCellValue((String) value);

                    row = sheet1.getRow(24);//25行目
                    cell = row.getCell(124);//DU列
                    value = " ";
                    cell.setCellValue((String) value);
                    //荷姿/////////////////////////////////////////////////
                    row = sheet1.getRow(22);//23行目
                    cell = row.getCell(133);//ED列
                    value = " ";
                    cell.setCellValue((String) value);

                    row = sheet1.getRow(23);//24行目
                    cell = row.getCell(133);//ED列
                    value = " ";
                    cell.setCellValue((String) value);

                    row = sheet1.getRow(24);//25行目
                    cell = row.getCell(133);//ED列
                    value = " ";
                    cell.setCellValue((String) value);

                    row = sheet1.getRow(25);//26行目
                    cell = row.getCell(133);//ED列
                    value = " ";
                    cell.setCellValue((String) value);

                    ////////////////////////////////////////////////////////
                    //当欄記載のとおり　斜線
                    // 線の色(黒)
                    //当欄記載のとおりチェックを空欄に
                    row = sheet1.getRow(30);//31行目
                    cell = row.getCell(50);//AY列
                    value = "　";
                    cell.setCellValue((String) value);
                    //当欄記載のとおりチェックを空欄に
                    row = sheet1.getRow(30);//31行目
                    cell = row.getCell(74);//BW列
                    value = "　";
                    cell.setCellValue((String) value);

                    //当欄記載のとおりチェックを空欄に
                    row = sheet1.getRow(32);//33行目
                    cell = row.getCell(35);//AJ列
                    value = "　";
                    cell.setCellValue((String) value);
                    //当欄記載のとおりチェックを空欄に
                    row = sheet1.getRow(32);//33行目
                    cell = row.getCell(65);//BN列
                    value = "　";
                    cell.setCellValue((String) value);
                    row = sheet1.getRow(32);//33行目
                    cell = row.getCell(91);//CN列
                    value = "　";
                    cell.setCellValue((String) value);

                    //運搬受託１〒
                    row = sheet1.getRow(35);//36行目
                    cell = row.getCell(7);//H列
                    value = "　";
                    cell.setCellValue((String) value);
                    row = sheet1.getRow(35);//36行目
                    cell = row.getCell(19);//T列
                    value = "　";
                    cell.setCellValue((String) value);
                    //搬受託者住所1
                    row = sheet1.getRow(36);//37行目
                    cell = row.getCell(4);//F列
                    value = "　";
                    cell.setCellValue((String) value);
                    //搬受託者正式名称
                    row = sheet1.getRow(37);//38行目
                    cell = row.getCell(14);//O列
                    value = "　";
                    cell.setCellValue((String) value);
                    //搬受託者電話
                    row = sheet1.getRow(38);//39行目
                    cell = row.getCell(14);//O列
                    value = "　";
                    cell.setCellValue((String) value);

                    //運搬受託者1積替有
                    row = sheet1.getRow(41);//42行目
                    cell = row.getCell(1);//B列
                    value = "　";
                    cell.setCellValue((String) value);
                    //運搬受託者1積替無
                    row = sheet1.getRow(41);//42行目
                    cell = row.getCell(6);//G列
                    value = "　";
                    cell.setCellValue((String) value);
                    //運搬受託者1車両番号
                    row = sheet1.getRow(41);//42行目
                    cell = row.getCell(14);//O列
                    value = "　";
                    cell.setCellValue((String) value);
                    //運搬受託者1車種
                    row = sheet1.getRow(41);//42行目
                    cell = row.getCell(32);//AG列
                    value = "　";
                    cell.setCellValue((String) value);

                    //処分受託者〒
                    row = sheet1.getRow(44);//45行目
                    cell = row.getCell(9);//J列
                    value = "　";
                    cell.setCellValue((String) value);
                    row = sheet1.getRow(44);//45行目
                    cell = row.getCell(19);//T列
                    value = "　";
                    cell.setCellValue((String) value);
                    //処分受託者住所1
                    row = sheet1.getRow(45);//46行目
                    cell = row.getCell(4);//F列
                    value = "　";
                    cell.setCellValue((String) value);
                    //処分受託者正式名称
                    row = sheet1.getRow(46);//47行目
                    cell = row.getCell(13);//N列
                    value = "　";
                    cell.setCellValue((String) value);
                    //処分受託者電話
                    row = sheet1.getRow(47);//48行目
                    cell = row.getCell(13);//N列
                    value = "　";
                    cell.setCellValue((String) value);

                    //運搬受託者（２）　斜線
                    //運搬受託２〒
                    row = sheet1.getRow(35);//36行目
                    cell = row.getCell(55);//BD列
                    value = "　";
                    cell.setCellValue((String) value);
                    row = sheet1.getRow(35);//36行目
                    cell = row.getCell(58);//BG列
                    value = "　";
                    cell.setCellValue((String) value);
                    //搬受託者住所1
                    row = sheet1.getRow(36);//37行目
                    cell = row.getCell(52);//BA列
                    value = "　";
                    cell.setCellValue((String) value);
                    //搬受託者正式名称
                    row = sheet1.getRow(37);//38行目
                    cell = row.getCell(61);//BJ列
                    value = "　";
                    cell.setCellValue((String) value);
                    //搬受託者電話
                    row = sheet1.getRow(38);//39行目
                    cell = row.getCell(61);//BJ列
                    value = "　";
                    cell.setCellValue((String) value);

                    //運搬受託者２積替有
                    row = sheet1.getRow(41);//42行目
                    cell = row.getCell(49);//AX列
                    value = "　";
                    cell.setCellValue((String) value);
                    //運搬受託者1積替無
                    row = sheet1.getRow(41);//42行目
                    cell = row.getCell(54);//BC列
                    value = "　";
                    cell.setCellValue((String) value);
                    //運搬受託者1車両番号
                    row = sheet1.getRow(41);//42行目
                    cell = row.getCell(61);//BJ列
                    value = "　";
                    cell.setCellValue((String) value);
                    //運搬受託者1車種
                    row = sheet1.getRow(41);//42行目
                    cell = row.getCell(80);//CC列
                    value = "　";
                    cell.setCellValue((String) value);

                    //積替え保管場所〒
                    row = sheet1.getRow(44);//45行目
                    cell = row.getCell(57);//BF列
                    value = "　";
                    cell.setCellValue((String) value);
                    //積替え保管場所名称
                    row = sheet1.getRow(44);//45行目
                    cell = row.getCell(67);//BP列
                    value = "　";
                    cell.setCellValue((String) value);
                    //積替え保管場所住所
                    row = sheet1.getRow(45);//46行目
                    cell = row.getCell(52);//BA列
                    value = "　";
                    cell.setCellValue((String) value);
                    //積替え保管場所電話
                    row = sheet1.getRow(46);//47行目
                    cell = row.getCell(58);//BG列
                    value = "　";
                    cell.setCellValue((String) value);
                    //有価物収集有り
                    row = sheet1.getRow(47);//48行目
                    cell = row.getCell(60);//CC列
                    value = "　";
                    cell.setCellValue((String) value);
                    //有価物収集無し
                    row = sheet1.getRow(47);//48行目
                    cell = row.getCell(65);//CC列
                    value = "　";
                    cell.setCellValue((String) value);
                    //実績数量
                    row = sheet1.getRow(47);//48行目
                    cell = row.getCell(80);//CC列
                    value = "　";
                    cell.setCellValue((String) value);
                    //実績数量単位 ton
                    row = sheet1.getRow(47);//48行目
                    cell = row.getCell(89);//CL列
                    value = "　";
                    cell.setCellValue((String) value);
                    //実績数量単位 m3
                    row = sheet1.getRow(47);//42行目
                    cell = row.getCell(92);//CO列
                    value = "　";
                    cell.setCellValue((String) value);

                    //運搬先の事業場（処分業者の処理施設）
                    //〒
                    row = sheet1.getRow(35);//36行目
                    cell = row.getCell(105);//DB列
                    value = " ";
                    cell.setCellValue((String) value);
                    //運搬先の事業場
                    row = sheet1.getRow(35);//36行目
                    cell = row.getCell(115);//DL列
                    value = " ";
                    cell.setCellValue((String) value);
                    //住所
                    row = sheet1.getRow(36);//37行目
                    cell = row.getCell(98);//CU列
                    value = " ";
                    cell.setCellValue((String) value);
                    //名称
                    row = sheet1.getRow(37);//38行目
                    cell = row.getCell(106);//DC列
                    value = " ";
                    cell.setCellValue((String) value);
                    //電話
                    row = sheet1.getRow(38);//39行目
                    cell = row.getCell(106);//DC列
                    value = " ";
                    cell.setCellValue((String) value);
                    //処分方法中間1
                    row = sheet1.getRow(39);//40行目
                    cell = row.getCell(108);//DE列
                    value = " ";
                    cell.setCellValue((String) value);
                    //処分方法中間2
                    row = sheet1.getRow(39);//40行目
                    cell = row.getCell(114);//DK列
                    value = " ";
                    cell.setCellValue((String) value);
                    //処分方法中間3
                    row = sheet1.getRow(39);//40行目
                    cell = row.getCell(120);//DQ列
                    value = " ";
                    cell.setCellValue((String) value);
                    //処分方法中間４
                    row = sheet1.getRow(39);//40行目
                    cell = row.getCell(126);//DW列
                    value = " ";
                    cell.setCellValue((String) value);
                    //処分方法最終1
                    row = sheet1.getRow(41);//42行目
                    cell = row.getCell(108);//DE列
                    value = " ";
                    cell.setCellValue((String) value);
                    //処分方法最終2
                    row = sheet1.getRow(41);//42行目
                    cell = row.getCell(116);//DM列
                    value = " ";
                    cell.setCellValue((String) value);
                    //処分方法最終3
                    row = sheet1.getRow(41);//42行目
                    cell = row.getCell(123);//DT列
                    value = " ";
                    cell.setCellValue((String) value);

                    //追加記載事項
                    row = sheet1.getRow(44);//45行目
                    cell = row.getCell(98);//CU列
                    value = " ";
                    cell.setCellValue((String) value);

                    //運搬受託１
                    row = sheet1.getRow(51);//52行目
                    cell = row.getCell(2);//C列
                    value = " ";
                    cell.setCellValue((String) value);
                    //運搬受託１
                    row = sheet1.getRow(53);//54行目
                    cell = row.getCell(2);//C列
                    value = " ";
                    cell.setCellValue((String) value);
                    //運搬受託年月日（年）
                    row = sheet1.getRow(54);//55行目
                    cell = row.getCell(2);//C列
                    value = " ";
                    cell.setCellValue((String) value);
                    //運搬受託年月日（月）
                    row = sheet1.getRow(54);//55行目
                    cell = row.getCell(8);//I列
                    value = " ";
                    cell.setCellValue((String) value);
                    //運搬受託年月日（日）
                    row = sheet1.getRow(54);//55行目
                    cell = row.getCell(13);//N列
                    value = " ";
                    cell.setCellValue((String) value);

                    //運搬の受託２　斜線
                    //運搬受託２
                    row = sheet1.getRow(51);//52行目
                    cell = row.getCell(19);//T列
                    value = " ";
                    cell.setCellValue((String) value);
                    //運搬受託２
                    row = sheet1.getRow(53);//54行目
                    cell = row.getCell(19);//T列
                    value = " ";
                    cell.setCellValue((String) value);
                    //運搬受託年月日（年）
                    row = sheet1.getRow(55);//56行目
                    cell = row.getCell(19);//T列
                    value = " ";
                    cell.setCellValue((String) value);
                    //運搬受託年月日（月）
                    row = sheet1.getRow(55);//56行目
                    cell = row.getCell(27);//AB列
                    value = " ";
                    cell.setCellValue((String) value);
                    //運搬受託年月日（日）
                    row = sheet1.getRow(55);//56行目
                    cell = row.getCell(31);//AF列
                    value = " ";
                    cell.setCellValue((String) value);

                    //処分受託（受領）
                    row = sheet1.getRow(51);//52行目
                    cell = row.getCell(38);//AM列
                    value = " ";
                    cell.setCellValue((String) value);
                    //処分の受託担当者1
                    row = sheet1.getRow(53);//54行目
                    cell = row.getCell(38);//AM列
                    value = " ";
                    cell.setCellValue((String) value);
                    //処分受領年（年）
                    row = sheet1.getRow(55);//56行目
                    cell = row.getCell(38);//AM列
                    value = " ";
                    cell.setCellValue((String) value);
                    //処分受領月（月）
                    row = sheet1.getRow(55);//56行目
                    cell = row.getCell(46);//AU列
                    value = " ";
                    cell.setCellValue((String) value);
                    //処分受領日（日）
                    row = sheet1.getRow(55);//56行目
                    cell = row.getCell(50);//AY列
                    value = " ";
                    cell.setCellValue((String) value);

                    //処分受託（受領）
                    row = sheet1.getRow(51);//52行目
                    cell = row.getCell(56);//BE列
                    value = " ";
                    cell.setCellValue((String) value);
                    //処分の受託担当者1
                    row = sheet1.getRow(53);//54行目
                    cell = row.getCell(56);//BE列
                    value = " ";
                    cell.setCellValue((String) value);
                    //処分受領年（年）
                    row = sheet1.getRow(55);//56行目
                    cell = row.getCell(56);//BE列
                    value = " ";
                    cell.setCellValue((String) value);
                    //処分受領月（月）
                    row = sheet1.getRow(55);//56行目
                    cell = row.getCell(64);//BM列
                    value = " ";
                    cell.setCellValue((String) value);
                    //処分受領日（日）
                    row = sheet1.getRow(55);//56行目
                    cell = row.getCell(68);//BQ列
                    value = " ";
                    cell.setCellValue((String) value);

                    if (rs.getDate("cont_date3") != null) {
                        //一つのセルに年月日をまとめて貼り付け
                        row = sheet1.getRow(50);//51行目
                        cell = row.getCell(76);//BY列
                        value = rs.getDate("cont_date3") != null ? rs.getDate("cont_date3") : " ";
                        cell.setCellValue((Date) value);
                    } else {
                        //最終処分確認（年）
                        row = sheet1.getRow(50);//51行目
                        cell = row.getCell(76);//BY列
                        value = " ";
                        cell.setCellValue((String) value);
                        //最終処分確認（月）
                        row = sheet1.getRow(50);//51行目
                        cell = row.getCell(86);//CI列
                        value = " ";
                        cell.setCellValue((String) value);
                        //最終処分確認（日）
                        row = sheet1.getRow(50);//51行目
                        cell = row.getCell(90);//CM列
                        value = " ";
                        cell.setCellValue((String) value);
                    }
                    //最終処分の場所
                    row = sheet1.getRow(58);//59行目
                    cell = row.getCell(2);//C列
                    value = rs.getString("last_office_name");
                    cell.setCellValue((String) value);

                }

            } finally {
                ps.close();
            }

            //シート再計算
            sheet1.setForceFormulaRecalculation(true);
            //印刷シート選択
            sheet1.setSelected(true);

            //印刷するかプレビューするか確認
            String s0 = new StringBuilder().append("select * ").append(" from m_manifesto where sno = 1; ").toString();
            boolean b直接印刷 = false;
            PreparedStatement ps0 = con.prepareStatement(s0);

            try {
                ResultSet rs0 = ps0.executeQuery();

                if (rs0.next()) {
                    b直接印刷 = rs0.getBoolean("manifesto_print_flag");

                }

            } finally {
                ps0.close();
            }

            try {
                //自身のtmpに保存
                out1 = new FileOutputStream(storeFol);
                wb1.write(out1);
                //スクリプトから印刷
                print_out = new File(storeFol);
                doPrintXlsx(print_out.getAbsolutePath(), printer[2], printer[1], 1, b直接印刷);

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

    //直行型マニフェスト最終処分印字
    private void printManifestoReport4(
             String file_path,
             String storeFol,
             Connection con,
             Long sno) {

        try {
            InputStream in1 = new FileInputStream(file_path);
            wb1 = WorkbookFactory.create(in1);
            XSSFSheet sheet1 = ((XSSFWorkbook) wb1).getSheet("print");

            FileOutputStream out1 = null;
            File print_out = null;

            Row row;
            Cell cell;
            Object value = null;
            String[] printer = getPrinter();

            ///////////////////////////////////////////////////////////////////////////////////
            String s = new StringBuilder().append("select * ").append(" from v1_weigh_data where sno = ?; ").toString();
            PreparedStatement ps = con.prepareStatement(s);
            try {
                ps.setObject(1, sno);
                ResultSet rs = ps.executeQuery();

                if (rs.next()) {

                    //データ貼り付け
                    //交付年月日（年）
                    row = sheet1.getRow(2);//3行目
                    cell = row.getCell(8);//I列
                    value = "　";
                    cell.setCellValue((String) value);
                    //交付年月日（月）
                    row = sheet1.getRow(2);//3行目
                    cell = row.getCell(14);//O列
                    value = "　";
                    cell.setCellValue((String) value);
                    //交付年月日（日）
                    row = sheet1.getRow(2);//3行目
                    cell = row.getCell(18);//S列
                    value = "　";
                    cell.setCellValue((String) value);

                    //交付担当者氏名
                    row = sheet1.getRow(2);//3行目
                    cell = row.getCell(85);//CH列
                    value = "　";
                    cell.setCellValue((String) value);

                    //排出業者〒
                    row = sheet1.getRow(5);//6行目
                    cell = row.getCell(12);//M列
                    value = "　";
                    cell.setCellValue((String) value);
                    //排出業者住所1
                    row = sheet1.getRow(6);//7行目
                    cell = row.getCell(5);//F列
                    value = "　";
                    cell.setCellValue((String) value);
                    //排出業者住所2
                    row = sheet1.getRow(7);//8行目
                    cell = row.getCell(5);//F列
                    value = "　";
                    cell.setCellValue((String) value);
                    //排出業者正式名称
                    row = sheet1.getRow(4);//5行目
                    cell = row.getCell(5);//F列
                    value = "　";
                    cell.setCellValue((String) value);
                    //排出業者電話
                    row = sheet1.getRow(5);//6行目
                    cell = row.getCell(32);//AG列
                    value = "　";
                    cell.setCellValue((String) value);

                    //排出業現場〒
                    row = sheet1.getRow(5);//6行目
                    cell = row.getCell(73);//BV列
                    value = "　";
                    cell.setCellValue((String) value);
                    //排出現場住所1
                    row = sheet1.getRow(6);//7行目
                    cell = row.getCell(65);//BN列
                    value = "　";
                    cell.setCellValue((String) value);
                    //排出現場住所1
                    row = sheet1.getRow(7);//8行目
                    cell = row.getCell(65);//BN列
                    value = "　";
                    cell.setCellValue((String) value);
                    //排出現場正式名称
                    row = sheet1.getRow(4);//5行目
                    cell = row.getCell(65);//BN列
                    value = "　";
                    cell.setCellValue((String) value);
                    //排出現場電話
                    row = sheet1.getRow(5);//6行目
                    cell = row.getCell(93);//CP列
                    value = "　";
                    cell.setCellValue((String) value);

                    //////////品目関係///////////////////////////////////////////////
                    //普通の産業廃棄物
                    row = sheet1.getRow(8);//9行目
                    cell = row.getCell(10);//K列
                    value = " ";
                    cell.setCellValue((String) value);
                    //特別管理産業廃棄物
                    row = sheet1.getRow(8);//9行目
                    cell = row.getCell(46);//AU列
                    value = " ";
                    cell.setCellValue((String) value);

                    //燃えがら///////////////////////////////////////////////////////////////////////////////////////////////
                    row = sheet1.getRow(9);//10行目
                    cell = row.getCell(4);//E列
                    value = " ";
                    cell.setCellValue((String) value);
                    //汚泥/////////////////////////////////////////////////
                    row = sheet1.getRow(10);//11行目
                    cell = row.getCell(4);//E列
                    value = " ";
                    cell.setCellValue((String) value);
                    //廃油/////////////////////////////////////////////////
                    row = sheet1.getRow(11);//12行目
                    cell = row.getCell(4);//E列
                    value = " ";
                    cell.setCellValue((String) value);
                    //廃酸/////////////////////////////////////////////////
                    row = sheet1.getRow(12);//13行目
                    cell = row.getCell(4);//E列
                    value = " ";
                    cell.setCellValue((String) value);
                    //廃アルカリ/////////////////////////////////////////////////
                    row = sheet1.getRow(13);//14行目
                    cell = row.getCell(4);//E列
                    value = " ";
                    cell.setCellValue((String) value);
                    //廃プラ/////////////////////////////////////////////////
                    row = sheet1.getRow(14);//15行目
                    cell = row.getCell(4);//E列
                    value = " ";
                    cell.setCellValue((String) value);
                    //紙くず/////////////////////////////////////////////////
                    row = sheet1.getRow(15);//16行目
                    cell = row.getCell(4);//E列
                    value = " ";
                    cell.setCellValue((String) value);
                    //木くず//////////////////////////////////////
                    row = sheet1.getRow(16);//17行目
                    cell = row.getCell(4);//E列
                    value = " ";
                    cell.setCellValue((String) value);
                    //繊維くず/////////////////////////////////////////////////
                    row = sheet1.getRow(17);//18行目
                    cell = row.getCell(4);//E列
                    value = " ";
                    cell.setCellValue((String) value);
                    //動物性残さ/////////////////////////////////////////////////
                    row = sheet1.getRow(18);//19行目
                    cell = row.getCell(4);//E列
                    value = " ";
                    cell.setCellValue((String) value);
                    //ゴムくず/////////////////////////////////////////////////
                    row = sheet1.getRow(19);//20行目
                    cell = row.getCell(4);//E列
                    value = " ";
                    cell.setCellValue((String) value);
                    //金属くず///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
                    row = sheet1.getRow(9);//10行目
                    cell = row.getCell(22);//W列
                    value = " ";
                    cell.setCellValue((String) value);
                    //ガラスコンクリート陶磁器くず/////////////////////////////
                    row = sheet1.getRow(10);//11行目
                    cell = row.getCell(22);//W列
                    value = " ";
                    cell.setCellValue((String) value);
                    //鉱さい/////////////////////////////////////////////////
                    row = sheet1.getRow(11);//12行目
                    cell = row.getCell(22);//W列
                    value = " ";
                    cell.setCellValue((String) value);
                    //がれき類/////////////////////////////////////////////////
                    row = sheet1.getRow(12);//13行目
                    cell = row.getCell(22);//W列
                    value = " ";
                    cell.setCellValue((String) value);
                    //家畜のふん尿/////////////////////////////////////////////
                    row = sheet1.getRow(13);//14行目
                    cell = row.getCell(22);//W列
                    value = " ";
                    cell.setCellValue((String) value);
                    //家畜の死体/////////////////////////////////////////////////
                    row = sheet1.getRow(14);//15行目
                    cell = row.getCell(22);//W列
                    value = " ";
                    cell.setCellValue((String) value);
                    //ばいじん/////////////////////////////////////////////////
                    row = sheet1.getRow(15);//16行目
                    cell = row.getCell(22);//W列
                    value = " ";
                    cell.setCellValue((String) value);
                    //13号廃棄物/////////////////////////////////////////////////
                    row = sheet1.getRow(16);//17行目
                    cell = row.getCell(22);//W列
                    value = " ";
                    cell.setCellValue((String) value);
                    //動物系固形不要物/////////////////////////////////////////////////
                    row = sheet1.getRow(17);//18行目
                    cell = row.getCell(22);//W列
                    value = " ";
                    cell.setCellValue((String) value);
                    //引火性廃油/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
                    row = sheet1.getRow(9);//10行目
                    cell = row.getCell(40);//AO列
                    value = " ";
                    cell.setCellValue((String) value);
                    ////引火性廃油有害//////////////////
                    row = sheet1.getRow(10);//11行目
                    cell = row.getCell(40);//AO列
                    value = " ";
                    cell.setCellValue((String) value);
                    ////強酸//////////////////
                    row = sheet1.getRow(11);//12行目
                    cell = row.getCell(40);//AO列
                    value = " ";
                    cell.setCellValue((String) value);
                    ////強酸有害//////////////////
                    row = sheet1.getRow(12);//13行目
                    cell = row.getCell(40);//AO列
                    value = " ";
                    cell.setCellValue((String) value);
                    ////強アルカリ//////////////////
                    row = sheet1.getRow(13);//14行目
                    cell = row.getCell(40);//AO列
                    value = " ";
                    cell.setCellValue((String) value);
                    ////強アルカリ有害//////////////////
                    row = sheet1.getRow(14);//15行目
                    cell = row.getCell(40);//AO列
                    value = " ";
                    cell.setCellValue((String) value);
                    ////感染性廃棄物//////////////////
                    row = sheet1.getRow(15);//16行目
                    cell = row.getCell(40);//AO列
                    value = " ";
                    cell.setCellValue((String) value);
                    ////PCB//////////////////
                    row = sheet1.getRow(16);//17行目
                    cell = row.getCell(40);//AO列
                    value = " ";
                    cell.setCellValue((String) value);
                    ////廃石綿等//////////////////
                    row = sheet1.getRow(17);//18行目
                    cell = row.getCell(40);//AO列
                    value = " ";
                    cell.setCellValue((String) value);
                    ////指定下水汚泥//////////////////
                    row = sheet1.getRow(18);//19行目
                    cell = row.getCell(40);//AO列
                    value = " ";
                    cell.setCellValue((String) value);
                    ////鉱さい有害//////////////////
                    row = sheet1.getRow(19);//20行目
                    cell = row.getCell(40);//AO列
                    value = " ";
                    cell.setCellValue((String) value);
                    //燃え殻有害/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
                    row = sheet1.getRow(9);//10行目
                    cell = row.getCell(58);//BG列
                    value = " ";
                    cell.setCellValue((String) value);
                    ////廃油有害//////////////////
                    row = sheet1.getRow(10);//11行目
                    cell = row.getCell(58);//BG列
                    value = " ";
                    cell.setCellValue((String) value);
                    ////汚泥有害//////////////////
                    row = sheet1.getRow(11);//12行目
                    cell = row.getCell(58);//BG列
                    value = " ";
                    cell.setCellValue((String) value);
                    ////廃酸有害//////////////////
                    row = sheet1.getRow(12);//13行目
                    cell = row.getCell(58);//BG列
                    value = " ";
                    cell.setCellValue((String) value);
                    ////強アルカリ有害//////////////////
                    row = sheet1.getRow(13);//14行目
                    cell = row.getCell(58);//BG列
                    value = " ";
                    cell.setCellValue((String) value);
                    ////ばいじん有害//////////////////
                    row = sheet1.getRow(14);//15行目
                    cell = row.getCell(58);//BG列
                    value = " ";
                    cell.setCellValue((String) value);
                    ////ばいじん有害//////////////////
                    row = sheet1.getRow(15);//16行目
                    cell = row.getCell(58);//BG列
                    value = " ";
                    cell.setCellValue((String) value);

                    //数量///////////////////////////////////////////////// 
                    row = sheet1.getRow(9);//10行目
                    cell = row.getCell(75);//BX列
                    value = " ";
                    cell.setCellValue((String) value);
                    //荷姿/////////////////////////////////////////////////
                    row = sheet1.getRow(9);//10行目
                    cell = row.getCell(95);//CR列
                    value = " ";
                    cell.setCellValue((String) value);
                    //廃棄物名/////////////////////////////////////////////////
                    row = sheet1.getRow(12);//13行目
                    cell = row.getCell(75);//BX列
                    value = " ";
                    cell.setCellValue((String) value);
                    //有害物質等/////////////////////////////////////////////////keijyo_print_f  yugai_name slash_flag03 
                    row = sheet1.getRow(14);//15行目
                    cell = row.getCell(75);//BX列
                    value = " ";
                    cell.setCellValue((String) value);
                    //処分方法/////////////////////////////////////////////////keijyo_print_f  method_name
                    row = sheet1.getRow(14);//15行目
                    cell = row.getCell(95);//CR列
                    value = " ";
                    cell.setCellValue((String) value);
                    //備考欄/////////////////////////////////////////////////keijyo_print_f  method_name
                    row = sheet1.getRow(17);//18行目
                    cell = row.getCell(77);//BZ列
                    value = " ";
                    cell.setCellValue((String) value);

                    ///中間処理/////////////////////////////////////////////////////
                    //当欄記載のとおり　斜線
                    //帳簿記載のとおりチェックを空欄に
                    row = sheet1.getRow(21);//22行目
                    cell = row.getCell(4);//E列
                    value = "　";
                    cell.setCellValue((String) value);
                    //当欄記載のとおりチェックを空欄に
                    row = sheet1.getRow(22);//23行目
                    cell = row.getCell(4);//E列
                    value = "　";
                    cell.setCellValue((String) value);
                    //帳簿記載のとおりを空欄に
                    row = sheet1.getRow(21);//22行目
                    cell = row.getCell(31);//AF列
                    value = "　";
                    cell.setCellValue((String) value);
                    //当欄記載のとおりを空欄に
                    row = sheet1.getRow(22);//23行目
                    cell = row.getCell(31);//AF列
                    value = "　";
                    cell.setCellValue((String) value);

                    ///最終処分の場所/////////////////////////////////////////////////////
                    //委託契約書記載のとおりチェック
                    row = sheet1.getRow(24);//25行目
                    cell = row.getCell(4);//E列
                    value = " ";
                    cell.setCellValue((String) value);
                    //当欄記載のとおりチェック
                    row = sheet1.getRow(25);//26行目
                    cell = row.getCell(4);//E列
                    value = " ";
                    cell.setCellValue((String) value);
                    //運搬受託者1氏名
                    row = sheet1.getRow(27);//28行目
                    cell = row.getCell(5);//F列
                    value = " ";
                    cell.setCellValue((String) value);
                    //運搬受託１〒
                    row = sheet1.getRow(28);//29行目
                    cell = row.getCell(12);//M列
                    value = " ";
                    cell.setCellValue((String) value);
                    //運搬受託電話
                    row = sheet1.getRow(28);//29行目
                    cell = row.getCell(32);//AG列
                    value = " ";
                    cell.setCellValue((String) value);
                    //運搬受託者1住所1
                    row = sheet1.getRow(29);//30行目
                    cell = row.getCell(5);//F列
                    value = " ";
                    cell.setCellValue((String) value);
                    //運搬受託者1住所2
                    row = sheet1.getRow(30);//31行目
                    cell = row.getCell(5);//F列
                    value = " ";
                    cell.setCellValue((String) value);

                    //処分受託者1氏名
                    row = sheet1.getRow(32);//33行目
                    cell = row.getCell(5);//F列
                    value = " ";
                    cell.setCellValue((String) value);
                    //処分受託１〒
                    row = sheet1.getRow(33);//34行目
                    cell = row.getCell(12);//M列
                    value = " ";
                    cell.setCellValue((String) value);
                    //処分受託電話
                    row = sheet1.getRow(33);//34行目
                    cell = row.getCell(32);//AG列
                    value = " ";
                    cell.setCellValue((String) value);
                    //処分受託者1住所1
                    row = sheet1.getRow(34);//35行目
                    cell = row.getCell(5);//F列
                    value = " ";
                    cell.setCellValue((String) value);
                    //処分受託者1住所2
                    row = sheet1.getRow(35);//36行目
                    cell = row.getCell(5);//F列
                    value = " ";
                    cell.setCellValue((String) value);

                    //運搬先の事業場名称
                    row = sheet1.getRow(27);//28行目
                    cell = row.getCell(65);//BN列
                    value = " ";
                    cell.setCellValue((String) value);
                    //運搬先の事業場１〒
                    row = sheet1.getRow(28);//29行目
                    cell = row.getCell(73);//BV列
                    value = " ";
                    cell.setCellValue((String) value);
                    //運搬先の事業場電話
                    row = sheet1.getRow(28);//29行目
                    cell = row.getCell(93);//CP列
                    value = " ";
                    cell.setCellValue((String) value);
                    //運搬先の事業場住所1
                    row = sheet1.getRow(29);//30行目
                    cell = row.getCell(65);//BN列
                    value = " ";
                    cell.setCellValue((String) value);
                    //運搬先の事業場住所2
                    row = sheet1.getRow(30);//31行目
                    cell = row.getCell(65);//BN列
                    value = " ";
                    cell.setCellValue((String) value);

                    //積替え保管場所名称
                    row = sheet1.getRow(32);//33行目
                    cell = row.getCell(65);//BN列 tsumikae_print_flag 
                    value = " ";
                    cell.setCellValue((String) value);
                    //積替え保管場所〒
                    row = sheet1.getRow(33);//34行目
                    cell = row.getCell(73);//BV列
                    value = " ";
                    cell.setCellValue((String) value);
                    //積替え保管場所電話
                    row = sheet1.getRow(33);//34行目
                    cell = row.getCell(93);//CP列
                    value = " ";
                    cell.setCellValue((String) value);
                    //積替え保管場所住所1
                    row = sheet1.getRow(34);//35行目
                    cell = row.getCell(65);//BN列
                    value = " ";
                    cell.setCellValue((String) value);
                    //積替え保管場所住所2
                    row = sheet1.getRow(35);//36行目
                    cell = row.getCell(65);//BN列
                    value = " ";
                    cell.setCellValue((String) value);

                    //運搬受託　氏名名称
                    row = sheet1.getRow(36);//37行目
                    cell = row.getCell(18);//S列
                    value = " ";
                    cell.setCellValue((String) value);
                    //運搬受託　運搬担当者
                    row = sheet1.getRow(37);//38行目
                    cell = row.getCell(18);//S列
                    value = " ";
                    cell.setCellValue((String) value);

                    //処分受託　氏名名称
                    row = sheet1.getRow(38);//39行目
                    cell = row.getCell(18);//S列
                    value = " ";
                    cell.setCellValue((String) value);
                    //処分受託　処分担当者
                    row = sheet1.getRow(39);//40行目
                    cell = row.getCell(18);//S列
                    value = " ";
                    cell.setCellValue((String) value);

                    //運搬終了年月日（年）
                    row = sheet1.getRow(37);//38行目
                    cell = row.getCell(80);//CC列
                    value = " ";
                    cell.setCellValue((String) value);
                    //運搬終了年月日（月）
                    row = sheet1.getRow(37);//38行目
                    cell = row.getCell(84);//CG列
                    value = " ";
                    cell.setCellValue((String) value);
                    //運搬終了年月日（日）
                    row = sheet1.getRow(37);//38行目
                    cell = row.getCell(87);//CJ列
                    value = " ";
                    cell.setCellValue((String) value);

                    //処分終了年月日（年）
                    row = sheet1.getRow(39);//40行目
                    cell = row.getCell(80);//CC列
                    value = " ";
                    cell.setCellValue((String) value);
                    //処分終了年月日（月）
                    row = sheet1.getRow(39);//40行目
                    cell = row.getCell(84);//CG列
                    value = " ";
                    cell.setCellValue((String) value);
                    //処分終了年月日（日）
                    row = sheet1.getRow(39);//40行目
                    cell = row.getCell(87);//CJ列
                    value = " ";
                    cell.setCellValue((String) value);

                    if (rs.getDate("cont_date3") != null) {

                        row = sheet1.getRow(39);//40行目
                        cell = row.getCell(103);//CZ列
                        value = rs.getDate("cont_date3") != null ? rs.getDate("cont_date3") : " ";
                        cell.setCellValue((Date) value);
                    } else {
                        //最終処分終了年月日（年）
                        row = sheet1.getRow(39);//40行目
                        cell = row.getCell(104);//DA列
                        value = " ";
                        cell.setCellValue((String) value);
                        //最終処分終了年月日（月）
                        row = sheet1.getRow(39);//40行目
                        cell = row.getCell(108);//DE列
                        value = " ";
                        cell.setCellValue((String) value);
                        //最終処分終了年月日（日）
                        row = sheet1.getRow(39);//40行目
                        cell = row.getCell(111);//DH列
                        value = " ";
                        cell.setCellValue((String) value);
                    }

                    //有価物拾集量
                    row = sheet1.getRow(37);//38行目
                    cell = row.getCell(102);//CY列
                    value = " ";
                    cell.setCellValue((String) value);

                    //最終処分を行った場所
                    row = sheet1.getRow(42);//43行目
                    cell = row.getCell(6);//G列
                    value = rs.getString("last_office_name");
                    cell.setCellValue((String) value);
                }

            } finally {
                ps.close();
            }

            //シート再計算
            sheet1.setForceFormulaRecalculation(true);
            //印刷シート選択
            sheet1.setSelected(true);

            //印刷するかプレビューするか確認
            String s0 = new StringBuilder().append("select * ").append(" from m_manifesto where sno = 1; ").toString();
            boolean b直接印刷 = false;
            PreparedStatement ps0 = con.prepareStatement(s0);

            try {
                ResultSet rs0 = ps0.executeQuery();

                if (rs0.next()) {
                    b直接印刷 = rs0.getBoolean("manifesto_print_flag");

                }

            } finally {
                ps0.close();
            }

            try {
                //自身のtmpに保存
                out1 = new FileOutputStream(storeFol);
                wb1.write(out1);
                //スクリプトから印刷
                print_out = new File(storeFol);
                doPrintXlsx(print_out.getAbsolutePath(), printer[2], printer[1], 1, b直接印刷);

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

    //委託契約書印刷
    private void printManifestoReport5(
             String file_path,
             String storeFol,
             Connection con,
             Long sno) {

        try {
            InputStream in1 = new FileInputStream(file_path);

            wb1 = WorkbookFactory.create(in1);
            XSSFSheet sheet1 = ((XSSFWorkbook) wb1).getSheet("委託契約1");
            XSSFSheet sheet2 = ((XSSFWorkbook) wb1).getSheet("委託契約2");
            XSSFSheet sheet3 = ((XSSFWorkbook) wb1).getSheet("委託契約3");
            XSSFSheet sheet4 = ((XSSFWorkbook) wb1).getSheet("委託契約4");
            XSSFSheet sheet5 = ((XSSFWorkbook) wb1).getSheet("印刷シート");

            FileOutputStream out1 = null;
            File print_out = null;

            Row row1;
            Cell cell;
            Object value = null;

            String[] printer = getPrinter();
            int i契約_直線 = 0; // 1:収集のみ　2：処分のみ　3：収集・処分

            /////////////////////////////////////////////////////////////////////////////////
            String s = new StringBuilder()
                     .append("SELECT * FROM v0_office ")
                     .append("WHERE sno = ? ; ")
                     .toString();

            PreparedStatement ps = con.prepareStatement(s);
            try {
                ps.setObject(1, sno);
                ResultSet rs = ps.executeQuery();

                int i現在行 = 4;
                try {
                    if (rs.next()) {

                        //１ページ目///////////////////////////////////////////
                        row1 = sheet1.getRow(16);//17行目
                        cell = row1.getCell(13);//N列
                        value = rs.getString("ent_address1") != null ? rs.getString("ent_address1") : " "
                                 + rs.getString("ent_address2") != null ? rs.getString("ent_address2") : " ";
                        cell.setCellValue((String) value);

                        row1 = sheet1.getRow(18);//19行目
                        cell = row1.getCell(13);//N列
                        //排出業者名称
                        value = rs.getString("ent_p_name");
                        cell.setCellValue((String) value);

                        row1 = sheet1.getRow(22);//23行目
                        cell = row1.getCell(13);//N列
                        //運搬業者住所
                        value = rs.getString("trans_address01");
                        cell.setCellValue((String) value);

                        row1 = sheet1.getRow(24);//25行目
                        cell = row1.getCell(13);//N列
                        //運搬業者名称
                        value = rs.getString("trans_name01");
                        cell.setCellValue((String) value);

                        row1 = sheet1.getRow(34);//35行目
                        cell = row1.getCell(13);//N列
                        value = rs.getString("con_address1_01") != null ? rs.getString("con_address1_01") : " "
                                 + rs.getString("con_address2_01") != null ? rs.getString("con_address2_01") : " ";
                        cell.setCellValue((String) value);

                        row1 = sheet1.getRow(36);//37行目
                        cell = row1.getCell(13);//N列
                        //処分業者名称
                        value = rs.getString("con_p_name01");
                        cell.setCellValue((String) value);

                        row1 = sheet1.getRow(38);//39行目
                        cell = row1.getCell(13);//N列
                        //処分業者許可番号
                        value = rs.getString("permission_no01");
                        cell.setCellValue((String) value);

                        //２ページ目///////////////////////////////////////////
                        row1 = sheet2.getRow(56);//57行目
                        cell = row1.getCell(9);//J列
                        //運搬業者住所
                        value = rs.getString("trans_address02");
                        cell.setCellValue((String) value);

                        cell = row1.getCell(1);//B列
                        //運搬業者名称
                        value = rs.getString("trans_name02");
                        cell.setCellValue((String) value);

                        row1 = sheet2.getRow(57);//57行目
                        cell = row1.getCell(9);//J列
                        //運搬業者住所
                        value = rs.getString("trans_address03");
                        cell.setCellValue((String) value);

                        cell = row1.getCell(1);//B列
                        //運搬業者名称
                        value = rs.getString("trans_name03");
                        cell.setCellValue((String) value);

                        row1 = sheet2.getRow(58);//57行目
                        cell = row1.getCell(9);//J列
                        //運搬業者住所
                        value = rs.getString("trans_address04");
                        cell.setCellValue((String) value);

                        cell = row1.getCell(1);//B列
                        //運搬業者名称
                        value = rs.getString("trans_name04");
                        cell.setCellValue((String) value);

                        //３ページ目///////////////////////////////////////////
                        row1 = sheet3.getRow(0);//1行目
                        cell = row1.getCell(14);//O列
                        //現場名称
                        value = rs.getString("p_name");
                        cell.setCellValue((String) value);

                        row1 = sheet3.getRow(2);//3行目
                        cell = row1.getCell(14);//O列
                        //現場住所
                        value = rs.getString("address1");
                        cell.setCellValue((String) value);

                        row1 = sheet3.getRow(4);//5行目
                        cell = row1.getCell(12);//M列
                        //開始日
                        value = rs.getDate("contract_start_date");
                        cell.setCellValue((Date) value);

                        row1 = sheet3.getRow(4);//5行目
                        cell = row1.getCell(25);//Z列
                        //終了日
                        value = rs.getDate("contract_end_date");
                        cell.setCellValue((Date) value);

                        if (rs.getInt("trans_id01") == 1 && rs.getInt("cont_id01") == 1) {
                            i契約_直線 = 3;
                        } else if (rs.getInt("trans_id01") == 1) {
                            i契約_直線 = 1;
                        } else if (rs.getInt("cont_id01") == 1) {
                            i契約_直線 = 2;
                        } else {
                            i契約_直線 = 0;
                        }

                    }

                } catch (Exception ie1) {
                    System.out.println(ie1.toString() + String.valueOf(i現在行));
                }

            } finally {
                ps.close();
            }

            XSSFDrawing drawing = sheet5.createDrawingPatriarch();
            XSSFClientAnchor anchor11 = new XSSFClientAnchor(0, 0, 0, 0, 81, 16, 81, 25);//収集運搬のみ
            XSSFClientAnchor anchor21 = new XSSFClientAnchor(0, 0, 0, 0, 86, 16, 86, 26);//処分のみ
            XSSFClientAnchor anchor22 = new XSSFClientAnchor(0, 0, 0, 0, 86, 32, 86, 37);//処分のみ
            XSSFClientAnchor anchor31 = new XSSFClientAnchor(0, 0, 0, 0, 91, 16, 91, 37);//収集運搬・処分
            //契約書に線を引く
            switch (i契約_直線) {
                case 1:
                    //収集運搬のみ
                    XSSFSimpleShape shape11 = drawing.createSimpleShape(anchor11);
                    shape11.setShapeType(ShapeTypes.LINE);  // 直線
                    shape11.setLineWidth(1.5);              // 太さ(pt)
                    shape11.setLineStyleColor(0, 0, 0);     // 線の色(黒)
                    break;
                case 2:
                    //処分のみ
                    XSSFSimpleShape shape21 = drawing.createSimpleShape(anchor21);
                    XSSFSimpleShape shape22 = drawing.createSimpleShape(anchor22);
                    shape21.setShapeType(ShapeTypes.LINE);  // 直線
                    shape21.setLineWidth(1.5);              // 太さ(pt)
                    shape21.setLineStyleColor(0, 0, 0);     // 線の色(黒)
                    shape22.setShapeType(ShapeTypes.LINE);  // 直線
                    shape22.setLineWidth(1.5);              // 太さ(pt)
                    shape22.setLineStyleColor(0, 0, 0);     // 線の色(黒)
                    break;
                case 3:
                    //収集運搬・処分
                    XSSFSimpleShape shape31 = drawing.createSimpleShape(anchor31);
                    shape31.setShapeType(ShapeTypes.LINE);  // 直線
                    shape31.setLineWidth(1.5);              // 太さ(pt)
                    shape31.setLineStyleColor(0, 0, 0);     // 線の色(黒)
                    break;
                default:
                    break;
            }

            //印刷シートをアクティブに
            wb1.setActiveSheet(4);
            //シート全選択
            sheet1.setSelected(false);
            sheet2.setSelected(false);
            sheet3.setSelected(false);
            sheet4.setSelected(false);
            sheet5.setSelected(true);
            //シート再計算
            sheet5.setForceFormulaRecalculation(true);

            try {
                //自身のtmpに保存
                out1 = new FileOutputStream(storeFol);
                wb1.write(out1);
                //スクリプトから印刷
                print_out = new File(storeFol);
                //ファイルパス・使用するプリンター・枚数・プレビューするかどうか
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

    //委託契約ラベル_CODE39
    private void printManifestoReport6(
             String file_path,
             String storeFol,
             Connection con,
             Long sno) {

        try {
            InputStream in1 = new FileInputStream(file_path);

            wb1 = WorkbookFactory.create(in1);
            XSSFSheet sheet1 = ((XSSFWorkbook) wb1).getSheet("print");

            FileOutputStream out1 = null;
            File print_out = null;

            Row row1;
            Cell cell;
            Object value = null;
            String[] printer = getPrinter();

            /////////////////////////////////////////////////////////////////////////////////
            String s = new StringBuilder()
                     .append("SELECT * FROM v0_office ")
                     .append("WHERE sno = ? ; ")
                     .toString();

            PreparedStatement ps = con.prepareStatement(s);
            try {
                ps.setObject(1, sno);
                ResultSet rs = ps.executeQuery();

                int i現在行 = 4;
                try {
                    if (rs.next()) {

                        //１ページ目///////////////////////////////////////////
                        row1 = sheet1.getRow(2);//3行目
                        cell = row1.getCell(2);//C列
                        //排出業者名称
                        value = rs.getString("ent_p_name");
                        cell.setCellValue((String) value);

                        row1 = sheet1.getRow(2);//3行目
                        cell = row1.getCell(3);//D列
                        //請求先名称
                        value = rs.getString("bill_name");
                        cell.setCellValue((String) value);

                        row1 = sheet1.getRow(4);//5行目
                        cell = row1.getCell(2);//C列
                        //現場名称
                        value = rs.getString("p_name");
                        cell.setCellValue((String) value);

                        row1 = sheet1.getRow(5);//6行目
                        cell = row1.getCell(2);//C列
                        //契約内容のバーコード（4ケタ+3ケタ）
                        value = "*" + String.format("%1$04d", rs.getInt("ent_id"))
                                 + "-"
                                 + String.format("%1$03d", rs.getInt("id"))
                                 + "*";
                        cell.setCellValue((String) value);

                        int r = 0;//縦
                        int c = 0;//横
                        int cnt = 0;//バーコードを貼り付けた件数
                        String[] trans = new String[2];
                        trans[0] = "";
                        trans[1] = "";
                        //契約運搬業者////////////////////////////////////////
                        for (int no = 1; (cnt < 4 && no < 5); no++) {
                            if (rs.getInt(String.format("trans_id%1$02d", no)) > 0) {
                                trans = getTransマスタ(con, rs.getInt(String.format("trans_id%1$02d", no)));
                                //IDをバーコード
                                if (r > 2 && c == 0) {
                                    r = 0;
                                    c = 1;
                                }
                                row1 = sheet1.getRow(8 + r);//9行目
                                cell = row1.getCell(2 + c);//I列
                                value = "*" + trans[0] + "*";
                                cell.setCellValue((String) value);
                                //下の行に名称を表示
                                row1 = sheet1.getRow(9 + r);//10行目
                                cell = row1.getCell(2 + c);//I列
                                value = trans[1];
                                cell.setCellValue((String) value);
                                r++;
                                r++;
                                cnt++;
                            }
                        }

                        //契約品目///////////////////////////////////////////
                        r = 0;//縦
                        c = 0;//横
                        cnt = 0;//バーコードを貼り付けた件数
                        String[] goods = new String[2];
                        goods[0] = "";
                        goods[1] = "";

                        if (rs.getInt("goods_unit01") > 0) {
                            goods = getGoodsマスタ(con, 1);
                            //IDをバーコード
                            row1 = sheet1.getRow(2 + r);//3行目
                            cell = row1.getCell(8 + c);//I列
                            value = "*" + goods[0] + "*";
                            cell.setCellValue((String) value);
                            //下の行に名称を表示
                            row1 = sheet1.getRow(3 + r);//4行目
                            cell = row1.getCell(8 + c);//I列
                            value = goods[1];
                            cell.setCellValue((String) value);
                            r++;
                            r++;
                            cnt++;
                        }
                        if (rs.getInt("goods_unit02") > 0) {
                            goods = getGoodsマスタ(con, 2);
                            //IDをバーコード
                            row1 = sheet1.getRow(2 + r);//3行目
                            cell = row1.getCell(8 + c);//I列
                            value = "*" + goods[0] + "*";
                            cell.setCellValue((String) value);
                            //下の行に名称を表示
                            row1 = sheet1.getRow(3 + r);//4行目
                            cell = row1.getCell(8 + c);//I列
                            value = goods[1];
                            cell.setCellValue((String) value);
                            r++;
                            r++;
                            cnt++;
                        }
                        if (rs.getInt("goods_unit03") > 0) {
                            goods = getGoodsマスタ(con, 3);
                            //IDをバーコード
                            row1 = sheet1.getRow(2 + r);//3行目
                            cell = row1.getCell(8 + c);//I列
                            value = "*" + goods[0] + "*";
                            cell.setCellValue((String) value);
                            //下の行に名称を表示
                            row1 = sheet1.getRow(3 + r);//4行目
                            cell = row1.getCell(8 + c);//I列
                            value = goods[1];
                            cell.setCellValue((String) value);
                            r++;
                            r++;
                            cnt++;
                        }
                        if (rs.getInt("goods_unit04") > 0) {
                            goods = getGoodsマスタ(con, 4);
                            //IDをバーコード
                            row1 = sheet1.getRow(2 + r);//3行目
                            cell = row1.getCell(8 + c);//I列
                            value = "*" + goods[0] + "*";
                            cell.setCellValue((String) value);
                            //下の行に名称を表示
                            row1 = sheet1.getRow(3 + r);//4行目
                            cell = row1.getCell(8 + c);//I列
                            value = goods[1];
                            cell.setCellValue((String) value);
                            r++;
                            r++;
                            cnt++;
                        }
                        if (rs.getInt("goods_unit05") > 0) {
                            goods = getGoodsマスタ(con, 5);
                            //IDをバーコード
                            row1 = sheet1.getRow(2 + r);//3行目
                            cell = row1.getCell(8 + c);//I列
                            value = "*" + goods[0] + "*";
                            cell.setCellValue((String) value);
                            //下の行に名称を表示
                            row1 = sheet1.getRow(3 + r);//4行目
                            cell = row1.getCell(8 + c);//I列
                            value = goods[1];
                            cell.setCellValue((String) value);
                            r++;
                            r++;
                            cnt++;
                        }
                        if (rs.getInt("goods_unit06") > 0) {
                            goods = getGoodsマスタ(con, 6);
                            //IDをバーコード
                            if (r > 8) {
                                r = 0;
                                c = 4;
                            }
                            row1 = sheet1.getRow(2 + r);//3行目
                            cell = row1.getCell(8 + c);//I列
                            value = "*" + goods[0] + "*";
                            cell.setCellValue((String) value);
                            //下の行に名称を表示
                            row1 = sheet1.getRow(3 + r);//4行目
                            cell = row1.getCell(8 + c);//I列
                            value = goods[1];
                            cell.setCellValue((String) value);
                            r++;
                            r++;
                            cnt++;
                        }
                        if (rs.getInt("goods_unit07") > 0) {
                            goods = getGoodsマスタ(con, 7);
                            //IDをバーコード
                            if (r > 8) {
                                r = 0;
                                c = 4;
                            }
                            row1 = sheet1.getRow(2 + r);//3行目
                            cell = row1.getCell(8 + c);//I列
                            value = "*" + goods[0] + "*";
                            cell.setCellValue((String) value);
                            //下の行に名称を表示
                            row1 = sheet1.getRow(3 + r);//4行目
                            cell = row1.getCell(8 + c);//I列
                            value = goods[1];
                            cell.setCellValue((String) value);
                            r++;
                            r++;
                            cnt++;
                        }
                        if (rs.getInt("goods_unit08") > 0) {
                            goods = getGoodsマスタ(con, 8);
                            //IDをバーコード
                            if (r > 8) {
                                r = 0;
                                c = 4;
                            }
                            row1 = sheet1.getRow(2 + r);//3行目
                            cell = row1.getCell(8 + c);//I列
                            value = "*" + goods[0] + "*";
                            cell.setCellValue((String) value);
                            //下の行に名称を表示
                            row1 = sheet1.getRow(3 + r);//4行目
                            cell = row1.getCell(8 + c);//I列
                            value = goods[1];
                            cell.setCellValue((String) value);
                            r++;
                            r++;
                            cnt++;
                        }
                        if (rs.getInt("goods_unit09") > 0) {
                            goods = getGoodsマスタ(con, 9);
                            //IDをバーコード
                            if (r > 8) {
                                r = 0;
                                c = 4;
                            }
                            row1 = sheet1.getRow(2 + r);//3行目
                            cell = row1.getCell(8 + c);//I列
                            value = "*" + goods[0] + "*";
                            cell.setCellValue((String) value);
                            //下の行に名称を表示
                            row1 = sheet1.getRow(3 + r);//4行目
                            cell = row1.getCell(8 + c);//I列
                            value = goods[1];
                            cell.setCellValue((String) value);
                            r++;
                            r++;
                            cnt++;
                        }
                        if (rs.getInt("goods_unit10") > 0) {
                            goods = getGoodsマスタ(con, 10);
                            //IDをバーコード
                            if (r > 8) {
                                r = 0;
                                c = 4;
                            }
                            row1 = sheet1.getRow(2 + r);//3行目
                            cell = row1.getCell(8 + c);//I列
                            value = "*" + goods[0] + "*";
                            cell.setCellValue((String) value);
                            //下の行に名称を表示
                            row1 = sheet1.getRow(3 + r);//4行目
                            cell = row1.getCell(8 + c);//I列
                            value = goods[1];
                            cell.setCellValue((String) value);
                            r++;
                            r++;
                            cnt++;
                        }

                        for (int no = 11; (cnt < 10 && no < 40); no++) {
                            if (rs.getInt(String.format("goods_unit%d", no)) > 0) {
                                goods = getGoodsマスタ(con, no);
                                //IDをバーコード
                                if (r > 8 && c == 0) {
                                    r = 0;
                                    c = 4;
                                }
                                row1 = sheet1.getRow(2 + r);//3行目
                                cell = row1.getCell(8 + c);//I列
                                value = "*" + goods[0] + "*";
                                cell.setCellValue((String) value);
                                //下の行に名称を表示
                                row1 = sheet1.getRow(3 + r);//4行目
                                cell = row1.getCell(8 + c);//I列
                                value = goods[1];
                                cell.setCellValue((String) value);
                                r++;
                                r++;
                                cnt++;
                            }
                        }

                    }

                } catch (Exception ie1) {
                    System.out.println(ie1.toString() + String.valueOf(i現在行));
                }

            } finally {
                ps.close();
            }

            //シート１をアクティブに
            wb1.setActiveSheet(0);
            //シート全選択
            sheet1.setSelected(true);

            try {
                //自身のtmpに保存
                out1 = new FileOutputStream(storeFol);
                wb1.write(out1);
                //スクリプトから印刷
                print_out = new File(storeFol);
                //ファイルパス・使用するプリンター・枚数・プレビューするかどうか
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

    private String[] getGoodsマスタ(Connection con, int sno) {

        String goods[] = new String[2];
        goods[0] = "";
        goods[1] = "";

        String s = new StringBuilder()
                 .append("select * ")
                 .append(" from v0_goods_kind where sno = ?; ")
                 .toString();
        try {
            PreparedStatement ps = con.prepareStatement(s);
            try {
                ps.setObject(1, sno);
                ResultSet rs = ps.executeQuery();

                if (rs.next()) {

                    goods[0] = "G" + String.format("%1$03d", rs.getInt("id"));
                    goods[1] = rs.getString("official_name");
                }
            } catch (SQLException e) {
            }
        } catch (Exception e) {
        }
        return goods;
    }

    private String[] getTransマスタ(Connection con, int id) {

        String goods[] = new String[2];
        goods[0] = "";
        goods[1] = "";

        String s = new StringBuilder()
                 .append("select * ")
                 .append(" from v0_trans where id = ?; ")
                 .toString();
        try {
            PreparedStatement ps = con.prepareStatement(s);
            try {
                ps.setObject(1, id);
                ResultSet rs = ps.executeQuery();

                if (rs.next()) {

                    goods[0] = "T" + String.format("%1$04d", rs.getInt("id"));
                    goods[1] = rs.getString("p_name");
                }
            } catch (SQLException e) {
            }
        } catch (Exception e) {
        }
        return goods;
    }
}
