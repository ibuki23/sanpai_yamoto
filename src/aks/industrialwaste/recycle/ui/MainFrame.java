package aks.industrialwaste.recycle.ui;

import aks.industrialwaste.database.Column;
import aks.industrialwaste.database.ColumnType;
import aks.industrialwaste.database.CombineOperator;
import aks.industrialwaste.database.CompareOperator;
import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.Window;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTabbedPane;
import javax.swing.SwingUtilities;
import javax.swing.event.AncestorEvent;
import aks.industrialwaste.database.DBManager;
import aks.industrialwaste.database.SearchCondition;
import aks.industrialwaste.recycle.ui.bill_list.BillListPanel;
import aks.industrialwaste.recycle.ui.bill_report.BillReport;
import aks.industrialwaste.recycle.ui.manifesto.KennsetuManifestoPanel100;
import aks.industrialwaste.ui.Panel;
import aks.industrialwaste.util.TermDate;
import aks.industrialwaste.recycle.ui.manifesto.ManifestoList;
import aks.industrialwaste.recycle.ui.manifesto.SanngyouManifestoPanel100;
import aks.industrialwaste.recycle.ui.master.MasterPanel;
import aks.industrialwaste.recycle.ui.master.agreement.Agreement;
import aks.industrialwaste.recycle.ui.payment.PaymentListPanel;
import aks.industrialwaste.recycle.ui.report.ReportPanel;
import aks.industrialwaste.recycle.ui.weighlist.*;
import aks.industrialwaste.recycle.ui.weighlistSales.WeighListSalesPanel;
import jp.co.astax.awt.FontUtility;
import aks.keiryo.gui.MainPanel;
import java.awt.Color;
import java.io.FileOutputStream;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.sql.Connection;
import javax.swing.ImageIcon;

public class MainFrame extends Panel {

    protected static Logger logger01_ = Logger.getLogger(MainFrame.class.getName());
    private DBManager dbm1_;
    public static JFrame f;
    public static Connection con1_;
    public static Connection con2_;
    public static ImageIcon icon_add, icon_edit, icon_delete, icon_reload, icon_print;

    // 今回はマニフェスト画面を利用しないため、タブだけを非表示にする。
    private static final boolean SHOW_MANIFESTO_TAB = false;

    // ソース修正後はバージョンを上げること
    /*
     * ver 4.4.8  直行型最終処分入力の際に複数選択行　multi_select_ が null になっている不具合修正
     *            処分場マスタ登録（追加）のパラメーターの数が不具合のため修正
     *            直行型マニフェスト印刷時にチェックボックスのフラグがスルーしてしまう不具合修正
     * ver 4.4.9  22日締め追加
     *            計量票印刷スレッドに計量snoを渡した詩にジャーナル記録
     * ver 4.5.0  マスタ転送を各クライアントから実行出来るようにHドライブを使用するように変更
     *            契約・請求先・排出業者・運搬業者のマスタはファイル容量が大きいため、圧縮・転送・解凍・取込に変更
     *            請求明細の訂正時に持越しフラグを立てる（追加のみだったため）
     * ver 4.5.1  請求一覧表および請求明細表の出力の際に、請求先コード指定を可能にする
     * ver 4.5.2  計量ボタン・登録ボタンのジャーナル修正（計量票印刷の挙動調査のため）
     * ver 4.5.3  マスタファイル転送に不具合（ファイルが大きいところ以外もzipファイルの扱いになっていたため）
     * ver 4.5.4  計量契約確認画面において契約マスタに40番目の品目登録時にエラーがあったため修正
     * ver 4.5.5  計量クライアント側にStatusに印刷フラグを立てる処理があったため修正
     * ver 4.5.6  帳票印刷中にエラーが発生した場合の印刷フラグを初期化可能にする（帳票タブにリセットボタン追加）
     * ver 4.5.7  請求明細データの訂正時に品目コードが変更されない不具合修正
     * ver 4.5.8  請求明細データの運搬費等追加時の並び替え条件変更
     * ver 4.5.9  計量ジャーナルのキャンセルの箇所にて画面のリセットがされない不具合変更　2014/07/29
     * ver 4.6.0  排出業者の名称　最大数変更 15 -> 18　2014/07/29
     *            運搬業者の名称　最大数変更 15 -> 18　2014/07/29
     *            処分業者の名称　最大数変更 15 -> 18　2014/07/29
     * ver 4.6.1  請求明細一覧のタブ追加（請求先変更等の参照されなくなったデータを復旧するため）　2014/07/30
     * ver 4.6.2  マニフェスト一覧の検索条件変更　交付番号複数検索可能にする　2014/08/06
     * ver 4.6.3  建設系マニフェスト積み替え保管がある場合に運搬業者（２）の車番を計量車番とする　2014/08/22
     *            ※運搬業者（１）の車番は積み替え場所までの車両であるため
     * ver 4.6.4  締め処理ジャーナル追加
     *            計量データ取込ジャーナル追加
     *            帳票　日報・月報　管理簿CSV項目追加
     * ver 4.6.5  管理簿CSV項目追加 日付の次に交付年月日追加
     * ver 4.6.6  マニフェスト印刷　排出業者手入力対応
     * ver 4.6.7  マニフェスト印刷　排出業者手入力対応（直行型が登録できないバグ（排出業者コードが初期値空欄になっていたため））
     *            マニフェスト印刷　直行型荷姿の登録内容を再表示できない不具合修正
     * ver 4.6.8  マニフェスト印刷　直行型荷姿初期表示をバラ備考欄に車番をセット
     *            直行型の運搬の日付を最初の登録時に画面上処分日と同じにセットする
     * 
     * ver 4.7.1  新計量票対応
     * 
     * ver 5.0.1  新プラント対応
     * ver 5.0.2　マニフェスト実績一覧に汎用検索および簡易検索機能追加
     * ver 5.0.3　行政報告機能追加　新プラントマスタ転送機能追加
     * ver 5.0.4　2015/05/07 建設系マニフェスト　処分方法コンボが選択されているときに破砕にチェックが入っていたため
     * ver 5.0.5　2015/05/09 商品マスタがDB3か所に更新するようになっていたため自身のみに変更 
     * ver 5.0.6　2015/05/30 マニフェスト一覧より2次マニフェスト検索を可能にする
     * ver 5.0.7　2015/06/03 行政報告修正ベータ版 
     * ver 6.0.0  2015/07/01 産廃デモ用スタート
     * ver 6.0.1  2015/08/11 中央公害開発スタート
     * ver 6.0.2  2015/09/24 中央公害　契約確認、更新画面の検索修正
     * ver 6.0.3  2015/09/28 中央公害　入金明細一覧の検索修正、請求一覧,入金一覧詳細画面の半角,全角の文字指定修正
     * ver 6.1.3  2015/10/09 中央公害　一時記憶insert修正, カラムサイズ調整
     * ver 6.1.4  2015/10/14 中央公害　マニフェスト画面デザインの調整
     * ver 6.1.5  2015/10/15 中央公害　マニフェスト管理台帳の交付者名を排出事業者の出力に変更
     * ver 6.1.6  2015/10/19 中央公害　建設系マニフェストの最終処分担当者の欄に「中央公害清掃㈱」、担当者名を入力、印字する
     * ver 6.1.7  2015/10/21 中央公害　業者別受入集計表（廃棄物名別）の項目　処分業者から処分場へ変更（printReport4 問い合わせ修正）
     * ver 6.1.9  2015/10/29 中央公害　按分処理をm3から％での入力設定に変更、直行型マニフェストの按分品目を備考欄に最大６種印字できるように変更
     * ver 6.2.0  2015/11/02 中央公害　マニフェスト印字位置調整
     * ver 6.2.1  2015/11/05 中央公害　マニフェスト画面での総重量表示をKg等
     * ver 6.2.2  2015/11/12 中央公害　本日付の修正リストに記載
     * ver 7.0.2  2015/12/25 鎌田建設　開発開始
     * ver 7.2.3  2016/02/26 鎌田建設　建設系マニフェスト最終処分印刷時に排出業者郵便番号等が初期化できていないため
     * ver 7.5.0  2016/04/18 丸本建設　開発開始（計量操作画面にバーコード読み込み機能追加）
     * ver 7.5.2  2016/06/02 丸本建設　計量空伝時刻表示
     * ver 7.5.3  2016/06/02 丸本建設　請求データ追加からの印刷データ反映、請求データ並び替えの修正
     * ver 7.6.3  2016/08/20 新工住建を元に陸中建設更新プログラムスタート
     * ver 7.6.4  2016/09/20 rikutyu2コンパイル
     * ver 7.6.5  2016/12/01 スパット北上機能変更
     *                        マニフェスト画面初期設定
     *                        風袋手入力機能追加
     *                        各帳表の正味重量を「ｔ」表示
     *                        計量記録一覧のデータ並び変更
     * ver 7.6.6  2016/12/01 処分場マスタデータ並び変更
     *                        行政報告貼り付け位置修正（処分）
     * ver 7.6.7  2017/06/05 行政報告grupe_byからoffce_name削除
     * ver 7.6.8  2017/09/30 計量記録一覧CSVに請求先追加
     * ver 7.6.9  2017/06/14 行政報告の集計の修正
     * ver 7.6.9  2018/12/25 交付担当者　契約マスタの登録内容が反映しない為変更　181225　抽出内容変更
     * ver 7.7.0  2019/03/15 契約マスタでの新規追加時に現場id が『0』でも登録できてしまう為ダイアログ出力とした
     * ver 7.7.1  2019/04/18 マニフェスト画面からの日付印字を西暦とする
     * ver 7.7.2  2019/06/08 行政報告集計の調整　収集運搬 
     * ver 7.7.3  2019/06/22 行政報告集計の調整　中間処分
     * ver 7.7.4  2020/06/09 行政報告集計の調整　収集運搬　中間処分　自社の区分をID1,10
     * ver 7.7.5  2020/06/10 計量管理記録簿CSV出力に契約マスタ備考欄を設ける　契約マスタ備考欄登録改修
     * ver 7.7.6  2020/08/04 契約マスタ備考欄の自動工番を修正
     * ver 7.7.7  2020/08/04 契約マスタ追加時の登録処理を修正
     * ver 7.7.8  2020/09/03 品目マスタ一覧　列調整 
     * ver 7.7.9  2021/04/25 稲瀬プラント指示計「KS-C7200」へ交換による変更 
     * ver 7.8.0  2021/05/06 行政報告改修　中間処分テンプレート変更含む  重量値取得社内テストを行うこと
     * ver 7.8.1  2021/05/13 直行型マニフェスト運搬日のデフォルト日付を無しとする v1_weigh_dataの修正　対応リスト参照
     * ver 7.8.1  2021/05/13 マニフェスト一覧にて処分業者を表示
     * ver 7.8.2  2021/06/10 行政報告がれき類にくくる処理へ変更　品目マスタ行政報告区分１に水銀追加
     * ver 15.0.0  2024/06/11 スパットむらた用　開発開始(PostgreSQL15)
     * ver 15.0.0  2025/03/01 村山コンポスト用　開発開始(PostgreSQL15)
     * ver 15.0.1  2025/04/14 運搬担当者Update失敗の不具合修正
     * ver 15.0.2  2025/04/16 納品当日の対応
     * ver 15.0.3  2025/04/17 ゼロリセット対応＋計量画面の品目・現場の順番変更
     * ver 15.0.5  2026/09/04 産廃税設定追加、行政報告パネル・マニフェストタブ非表示
     * ver 15.0.6  2026/09/07 専用の日計表・CSV・集計表・マニフェスト管理表を削除
     * ver 15.0.7  2026/09/14 新規請求システム用の請求ヘッダー・明細・取込ビューを追加
     */
    private static String weight_title = "産廃システム - ver 15.0.9";

    public MainFrame() throws IOException {
        dbm1_ = new DBManager(new File("db.properties"));

        //計量モードなら通信バッティングを防ぐため二重起動をチェック
        if (DBManager.mode == 0 || DBManager.mode == 1 || DBManager.mode == 2) {
            //起動チェック
            final FileOutputStream fos = new FileOutputStream(new File("lock"));
            final FileChannel fc = fos.getChannel();
            final FileLock lock = fc.tryLock();
            if (lock == null) {
                //既に起動されているので終了する
                JOptionPane.showMessageDialog(this,
                         "すでに起動しています。",
                         "確認",
                         JOptionPane.INFORMATION_MESSAGE);
                System.exit(-1);
            }

            //ロック開放処理を登録
            Runtime.getRuntime().addShutdownHook(
                     new Thread() {
                @Override
                public void run() {
                    if (lock != null && lock.isValid()) {
                        try {
                            lock.release();
                        } catch (IOException e1) {
                        }
                    }

                    try {
                        fc.close();
                        fos.close();
                    } catch (IOException e1) {
                    }
                }
            });
        }

        //システムアイコン
        ImageIcon icon;
        switch (DBManager.mode) {
            case 0://産廃システム
            case 1://産廃システム
            case 4://産廃システム
                icon = new ImageIcon(getClass().getResource("/01.png"));
                break;
            case 20://買取システム
            case 21://買取システム
            case 22://買取システム
                icon = new ImageIcon(getClass().getResource("/20.png"));
                break;
            case 99:
                icon = new ImageIcon(getClass().getResource("/04.png"));
                break;
            default:
                icon = new ImageIcon(getClass().getResource("/100.png"));
                break;
        }

        f.setIconImage(icon.getImage());

    }

    public static void main(String[] args) {
        FontUtility.setDefaultFont("メイリオ", Font.PLAIN, 18);
        TermDate.setStartHour(0);

        //外観スタイル
        try {

            javax.swing.UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");

        } catch (Exception ex) {
        }

        try {

            f = new JFrame();
            f.setTitle(weight_title);
            f.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
            f.setLayout(new BorderLayout());
            f.add(new MainFrame());
            f.setSize(1920, 1040);
            f.setVisible(true);

        } catch (IOException e) {
            JOptionPane.showMessageDialog(
                     null,
                     e.getMessage() + "起動できません.",
                     "エラー",
                     JOptionPane.ERROR_MESSAGE);
            System.exit(-1);
        }
    }

    @Override
    public void ancestorAdded(AncestorEvent event) {
        Window w = SwingUtilities.getWindowAncestor(this);
        if (null == w) {
            System.exit(-1);
        }

        // 終了時の処理を登録
        w.addWindowListener(
                 new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent event) {
                if (JOptionPane.showConfirmDialog(MainFrame.this,
                         "終了します", "確認",
                         JOptionPane.OK_CANCEL_OPTION) == 2) {
                    return;
                }
                try {
                } catch (Exception e) {
                    logger_.warning(e.toString());
                } finally {
                    try {
                        dbm1_.close();

                    } catch (SQLException e) {
                    }
                }

                System.exit(0);
            }
        });

        configureFrame();
    }

    @Override
    protected void initComponents() {
        super.initComponents();
        // タイトルは非表示
        titleField_.setVisible(false);

    }
    private JTabbedPane tabpanel;

    private void configureFrame() {

        try {

            Font font14 = FontUtility.getFont("メイリオ", Font.PLAIN, 14);

            //右クリックメニューのアイコン生成
            icon_add = new ImageIcon(getClass().getResource("/add.png"));
            icon_edit = new ImageIcon(getClass().getResource("/edit.png"));
            icon_delete = new ImageIcon(getClass().getResource("/delete.png"));
            icon_reload = new ImageIcon(getClass().getResource("/reload.png"));
            icon_print = new ImageIcon(getClass().getResource("/print.png"));

            if (dbm1_.url_.length() > 0) {
                con1_ = dbm1_.getConnection();
            }

            if (dbm1_.url_.length() > 0) {
                addContainer(
                         northPane_, new Infobar(con1_),
                         0, 1, 2, 1, 1.0, 0.0,
                         GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                         new Insets(1, 1, 1, 1), 0, 0);
            }

            // 管理機能
            tabpanel = new JTabbedPane();
            tabpanel.setDoubleBuffered(true);
            tabpanel.setFont(font14);

            String btag = "";
            String atag = "";

            // 110122 add aks.takisawa
            // タブのあるパネルの背景色の指定
            centerPane_.setBackground(new Color(214, 217, 223));
            switch (DBManager.mode) {
                case 4:
                    tabpanel.add(new WeighListPanel(con1_), btag + "計量記録一覧" + atag);
                    tabpanel.setFocusable(false);
                    tabpanel.add(new GateInListPanel(con1_), btag + "一時記憶一覧" + atag);
                    tabpanel.setFocusable(false);
                    addManifestoTab(btag, atag);
                    tabpanel.add(new WeighListSalesPanel(con1_), btag + "　売上高管理　" + atag);
                    tabpanel.setFocusable(false);
                    tabpanel.add(new MasterPanel(con1_), btag + "マスタ設定" + atag);
                    tabpanel.setFocusable(false);
                    tabpanel.add(new ReportPanel(con1_), btag + "　帳　票　" + atag);
                    tabpanel.setFocusable(false);

                    break;
                case 14:
                    tabpanel.add(new WeighListPanel(con1_), btag + "計量記録一覧" + atag);
                    tabpanel.setFocusable(false);
                    addManifestoTab(btag, atag);
                    tabpanel.add(new ReportPanel(con1_), btag + "帳票" + atag);
                    tabpanel.setFocusable(false);
                    tabpanel.add(new BillListPanel(con1_), btag + "請求一覧" + atag);
                    tabpanel.setFocusable(false);
                    if (DBManager.PAYMENT_FLAG) {
                        tabpanel.add(new PaymentListPanel(con1_), btag + "入金一覧" + atag);
                        tabpanel.setFocusable(false);
                    }
                    tabpanel.add(new BillReport(con1_), btag + "請求書締め" + atag);
                    tabpanel.setFocusable(false);
                    tabpanel.add(new MasterPanel(con1_), btag + "マスタ設定" + atag);
                    tabpanel.setFocusable(false);
                    break;
                case 1:
                case 2:
                case 11:
                    tabpanel.add(new MainPanel(null, con1_), btag + "計量メニュー" + atag);
                    tabpanel.setFocusable(false);
                    tabpanel.add(new WeighListPanel(con1_), btag + "計量記録一覧" + atag);
                    tabpanel.setFocusable(false);
                    tabpanel.add(new GateInListPanel(con1_), btag + "一時記憶一覧" + atag);
                    tabpanel.setFocusable(false);
                    addManifestoTab(btag, atag);
                    tabpanel.add(new WeighListSalesPanel(con1_), btag + "　売上高管理　" + atag);
                    tabpanel.setFocusable(false);
                    tabpanel.add(new MasterPanel(con1_), btag + "マスタ設定" + atag);
                    tabpanel.setFocusable(false);
                    tabpanel.add(new ReportPanel(con1_), btag + "　帳　票　" + atag);
                    tabpanel.setFocusable(false);
                    break;
                case 201:
                case 202:
                    tabpanel.add(new WeighListPanel(con1_), btag + "計量記録一覧" + atag);
                    tabpanel.setFocusable(false);
                    tabpanel.add(new GateInListPanel(con1_), btag + "一時記憶一覧" + atag);
                    tabpanel.setFocusable(false);
                    addManifestoTab(btag, atag);
                    tabpanel.add(new ReportPanel(con1_), btag + "帳票" + atag);
                    tabpanel.setFocusable(false);
                    tabpanel.add(new MasterPanel(con1_), btag + "マスタ設定" + atag);
                    tabpanel.setFocusable(false);
                    break;
                case 0:
                    tabpanel.add(new MainPanel(null, con1_), btag + "計量メニュー" + atag);
                    tabpanel.setFocusable(false);
                    tabpanel.add(new WeighListPanel(con1_), btag + "計量一覧" + atag);
                    tabpanel.setFocusable(false);
                    tabpanel.add(new GateInListPanel(con1_), btag + "一時記憶" + atag);
                    tabpanel.setFocusable(false);
                    addManifestoTab(btag, atag);
                    tabpanel.add(new MasterPanel(con1_), btag + "マスタ設定" + atag);
                    tabpanel.setFocusable(false);
                    tabpanel.add(new ReportPanel(con1_), btag + "帳票" + atag);
                    tabpanel.setFocusable(false);
                    break;
                case 100:
                    WeighListTableModel model_ = new WeighListTableModel(con1_);
                    // 検索条件, ソート条件を設定
                    SearchCondition[] search
                             = new SearchCondition[]{
                                 new SearchCondition(
                                          new Column("sno", "sno", ColumnType.INT4),
                                          CompareOperator.EQ,
                                          0,
                                          CombineOperator.NONE)};
                    model_.refresh(search, null);
                    if (SHOW_MANIFESTO_TAB) {
                        tabpanel.add(new KennsetuManifestoPanel100(model_, 0, con1_, 0), btag + "建設系廃棄物マニフェスト" + atag);
                        tabpanel.add(new SanngyouManifestoPanel100(model_, 0, con1_, 0), btag + "産業廃棄物マニフェスト" + atag);
                    }
                    f.setBounds(0, 0, 1280, 1024);
                    northPane_.setVisible(false);
                    break;
                case 200:
                    //本社契約マスタ仮登録モード
                    tabpanel.add(new Agreement(con1_), "契約マスタ");
                    break;
                default:
                    break;
            }

            addContainer(
                     centerPane_, tabpanel,
                     0, 0, 1, 1, 1.0, 1.0,
                     GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                     new Insets(1, 1, 1, 1), 0, 0);
        } catch (SQLException e) {
            logger_.warning(e.toString());
        }
    }

    private void addManifestoTab(String btag, String atag) {
        if (SHOW_MANIFESTO_TAB) {
            tabpanel.add(new ManifestoList(con1_), btag + "マニフェスト" + atag);
            tabpanel.setFocusable(false);
        }
    }
}
