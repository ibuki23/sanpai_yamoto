package aks.keiryo.gui;

import java.awt.*;
import aks.keiryo.status.*;
import aks.keiryo.getweight.*;
import aks.keiryo.message.*;
import aks.industrialwaste.database.DBManager;
import aks.industrialwaste.database.Column;
import aks.industrialwaste.database.ColumnType;
import aks.industrialwaste.database.CombineOperator;
import aks.industrialwaste.database.CompareOperator;
import aks.industrialwaste.database.SearchCondition;
import aks.industrialwaste.database.SortCondition;
import aks.industrialwaste.database.SortOrder;
import aks.keiryo.status.Status;
import java.sql.Connection;
import java.sql.ResultSet;
import aks.industrialwaste.recycle.printer.*;
import aks.industrialwaste.recycle.report.XlsxBillReport;
import aks.industrialwaste.recycle.ui.master.goods.GoodsTableModel;
import aks.industrialwaste.recycle.ui.weighlist.GateInListTableModel;
import static aks.industrialwaste.recycle.ui.weighlist.WeighList.領収書用途;
import java.awt.event.KeyEvent;
import javax.swing.table.*;
import jp.co.astax.awt.FontUtility;
import aks.industrialwaste.recycle.util.*;
import aks.swing.JTextFieldKeiryo;
import java.awt.event.InputEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.im.InputSubset;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.Map;
import javax.swing.ButtonGroup;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;

public class MainPanel extends javax.swing.JPanel {

    public Frame mOwnerFrame;
    public static Connection connect_;
    public static MessageDialog mMessageDialog;
    public static MessageDialog2 mMessageDialog2;
    public static Status mStatus;

    public MPT130SGetPrinterThread MPT130SmGetPrinterThread;
    public AD4401GetWeightThread AD4401mGetWeightThread;

    public Thread AD4401mThreadGetWeightThread;
    public Thread mThreadGetPrinterThread200;
    public Thread MPT130SmThreadGetPrinterThread;
    public Thread mThreadWeightUpdateView;

    public long mLastTitleClickTime = System.currentTimeMillis();
    public int mTitleClickCount = 0;
    public static String weight_err = "";
    public SoundMessage mSound;
    public static Connection con_;
    public static Boolean OpaqueFlag_ = false;
    public static Boolean WeightFlag_ = false;
    public static int now_weight_1 = 0;
    public static int now_weight_2 = 1000;
    public static int total_weight_ = 0;
    public static int empty_weight_ = 0;
    public static int net_weight_ = 0;
    public static int loading_weight_ = 0;
    private String[] goods_name = new String[40];
    private int[] goods_id = new int[40];
    private int[] office_unit = new int[40];
    private int[] trans_id = new int[5];
    private int[] cont_id = new int[4];

    public static int 選択現在重量_ = 0;
    public static int 選択計量回数_ = 0;//1:1回計量　2:2回計量1回目　3:2回計量2回目
    public static boolean 契約取込flag = false;

    private RemoteStruct rs;
    private Font font14 = FontUtility.getFont("メイリオ", Font.PLAIN, 14);
    public static Date term_date_;
    public static int count = 0;
    public static boolean weight_edit_ = false;
    public static boolean weight_add_ = false;
    private GateInListTableModel model_;
    private Dis_officeTableModel model_1, model_2;
    private GoodsTableModel gmodel_;
    private java.util.List<SearchCondition> searchCondition_;
    private java.util.List<SortCondition> sortCondition_;
    private JTableHeader th1, th2;
    private Boolean[] 安定flag;

    public static int carry_class_ = 0;

    public long mLastClickTime = System.currentTimeMillis();
    public int mClickCount = 0;
    private Object mObject = null;

    private boolean f重量検知音声再生 = false;

    public MainPanel(Frame owner, Connection con) {
        initComponents();
        connect_ = con;
        con_ = connect_;

        searchCondition_
                 = Collections.synchronizedList(new ArrayList<>());
        sortCondition_
                 = Collections.synchronizedList(new ArrayList<>());

        // データテーブル
        model_ = new GateInListTableModel(con);
        dataTable_.setModel(model_);
        dataTable_.setColumnModel(new GateInListTableColumnModel());
        dataTable_.setFont(font14);
        dataTable_.setRowHeight(30);
        dataTable_.setColumnSelectionAllowed(false);
        dataTable_.setRowSelectionAllowed(true);
        dataTable_.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        dataTable_.getTableHeader().setReorderingAllowed(false);
        dataTable_.setAutoResizeMode(JTable.AUTO_RESIZE_LAST_COLUMN);
        dataTable_.addMouseListener(
                 new MouseAdapter() {
            @Override
            public void mouseClicked(final MouseEvent me) {
                if (me.getClickCount() == 2 && dataTable_.getSelectedRow() >= 0) {
                    car_id_.setText(String.valueOf(dataTable_.getValueAt(dataTable_.getSelectedRow(), 1)));
                    getCarID();
                    weight_btn_重量取込.requestFocus();
                }
            }
        });

        JTableHeader jh = dataTable_.getTableHeader();
        jh.setFont(font14);

        //品目マスタ登録リスト
        gmodel_ = new GoodsTableModel(con);

        //契約内容
        model_1 = new Dis_officeTableModel(con);
        dataTable_1.setModel(model_1);
        dataTable_1.setColumnModel(new Dis_officeTableColumnModel());
        dataTable_1.setFont(font14);
        dataTable_1.setRowHeight(30);
        dataTable_1.setColumnSelectionAllowed(false);
        dataTable_1.setRowSelectionAllowed(true);
        dataTable_1.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        dataTable_1.getTableHeader().setReorderingAllowed(false);
        dataTable_1.setAutoResizeMode(JTable.AUTO_RESIZE_LAST_COLUMN);

        //契約内容
        model_2 = new Dis_officeTableModel(con);
        dataTable_2.setModel(model_2);
        dataTable_2.setColumnModel(new Dis_officeTableColumnModel2());
        dataTable_2.setFont(font14);
        dataTable_2.setRowHeight(30);
        dataTable_2.setColumnSelectionAllowed(false);
        dataTable_2.setRowSelectionAllowed(true);
        dataTable_2.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        dataTable_2.getTableHeader().setReorderingAllowed(false);
        dataTable_2.setAutoResizeMode(JTable.AUTO_RESIZE_LAST_COLUMN);
        dataTable_2.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {

            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                if (((String) table.getValueAt(row, 9)).equals("") && !isSelected) {
                    // 契約期間内なら
                    setBackground(table.getBackground());
                    setForeground(table.getForeground());

                } else if (((String) table.getValueAt(row, 9)).equals("契約前") && !isSelected) {
                    // 契約期間前なら
                    if (column == 9) {
                        //黄色
                        setBackground(new Color(255, 204, 204));
                        setForeground(table.getForeground());
                    } else {
                        setBackground(table.getBackground());
                        setForeground(table.getForeground());
                    }

                } else if (((String) table.getValueAt(row, 9)).equals("期間切れ") && !isSelected) {
                    // 契約期間後なら
                    if (column == 9) {
                        //赤
                        setBackground(new Color(255, 255, 204));
                        setForeground(table.getForeground());
                    } else {
                        setBackground(table.getBackground());
                        setForeground(table.getForeground());
                    }

                } else if (isSelected) {
                    // 紺色
                    setBackground(table.getSelectionBackground());
                    setForeground(table.getSelectionForeground());

                } else {
                    setBackground(table.getBackground());
                    setForeground(table.getForeground());
                }
                return this;
            }
        });

        th1 = dataTable_1.getTableHeader();
        th1.setFont(font14);
        Dimension d1 = th1.getPreferredSize();
        d1.height = 50;
        th1.setPreferredSize(d1);
        th2 = dataTable_2.getTableHeader();
        th2.setFont(font14);
        Dimension d2 = th2.getPreferredSize();
        d2.height = 50;
        th2.setPreferredSize(d2);

        ButtonGroup bg = new ButtonGroup();
        ButtonGroup bgt = new ButtonGroup();
        bg.add(rb1_);
        bg.add(rb2_);
        bg.add(rb3_);
        bg.add(rb4_);
        bg.add(rb5_);
        bg.add(rb6_);
        bg.add(rb7_);
        bg.add(rb8_);
        bg.add(rb9_);
        bg.add(rb10_);
        bg.add(rb11_);
        bg.add(rb12_);
        bg.add(rb13_);
        bg.add(rb14_);
        bg.add(rb15_);
        bg.add(rb16_);
        bg.add(rb17_);
        bg.add(rb18_);
        bg.add(rb19_);
        bg.add(rb20_);
        bg.add(rb21_);
        bg.add(rb22_);
        bg.add(rb23_);
        bg.add(rb24_);
        bg.add(rb25_);
        bg.add(rb26_);
        bg.add(rb27_);
        bg.add(rb28_);
        bg.add(rb29_);
        bg.add(rb30_);
        bg.add(rb31_);
        bg.add(rb32_);
        bg.add(rb33_);
        bg.add(rb34_);
        bg.add(rb35_);
        bg.add(rb36_);
        bg.add(rb37_);
        bg.add(rb38_);
        bg.add(rb39_);
        bg.add(rb40_);
        bgt.add(trb1_);
        bgt.add(trb2_);
        bgt.add(trb3_);
        bgt.add(trb4_);
        bgt.add(trb5_);

        mOwnerFrame = owner;//親フレームの保持

        mStatus = new Status();//ステータスクラスの生成

        安定flag = new Boolean[2];
        安定flag[0] = false;
        安定flag[1] = false;

        mStatus.mMyRemoteType = (owner == null ? Status.REMOTE_KANRI : Status.REMOTE_KEIRYO);
        mSound = new SoundMessage();

        jButton9.setVisible(false);
        empty_input.setVisible(false);
        weight_btn_重量初期化.setVisible(false);

        Setup();
    }

    public void Setup() {
        mStatus.mMainPanel = this;
        mStatus.Setup(this);
        //使用ダイアログの生成
        mMessageDialog = new MessageDialog(mOwnerFrame, true);
        mMessageDialog2 = new MessageDialog2(mOwnerFrame, true);

        //トラックスケール重量読み込みスレッド起動
        if (DBManager.mode == 1 || DBManager.mode == 11) {

            AD4401mGetWeightThread = new AD4401GetWeightThread(con_);
            AD4401mThreadGetWeightThread = new Thread(
                     AD4401mGetWeightThread, "AD4401-WeightReceiver");
            AD4401mThreadGetWeightThread.start();
            try {
                Thread.sleep(100);
            } catch (Exception e) {

            }

            //NADA MPT-130S
            //計量室プリンターコマンド送信スレッド起動
            MPT130SmGetPrinterThread = new MPT130SGetPrinterThread(con_);
            MPT130SmThreadGetPrinterThread = new Thread(
                     MPT130SmGetPrinterThread, "MPT130S-Printer");
            MPT130SmThreadGetPrinterThread.start();

            try {
                Thread.sleep(100);
            } catch (Exception e) {

            }

        }

        //重量値表示スレッド
        mThreadWeightUpdateView
                 = new Thread(
                          new Runnable() {

                     @Override
                     @SuppressWarnings("SleepWhileInLoop")
                     public void run() {
                         while (!Thread.currentThread().isInterrupted()) {
                             long tm = System.currentTimeMillis();
                             try {
                                 SwingUtilities.invokeLater(
                                          new Runnable() {

                                     @Override
                                     public void run() {
                                         getWeight();
                                         updateWeight();
                                     }
                                 });

                                 tm = System.currentTimeMillis() - tm;
                                 tm = Math.max(10, 300 - tm);
                                 Thread.sleep(tm);
                             } catch (InterruptedException e) {
                                 Thread.currentThread().interrupt();
                             } catch (Exception e) {
                             }
                         }
                     }
                 });
        mThreadWeightUpdateView.start();

        //最初のフォーカスを設定
        EventQueue.invokeLater(new Runnable() {

            @Override
            public void run() {
                car_id_.requestFocusInWindow();
            }
        });
        // タイトルの表示
        jLabel_Title.setText("計量メニュー");

        ClearView();

        //選択のダイアログセットする
        car_id_.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mousePressed(java.awt.event.MouseEvent evt) {
                showSelector(evt, 0);
            }
        });

        bill_no_tex.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mousePressed(java.awt.event.MouseEvent evt) {
                showSelector(evt, 1);
            }
        });
        ent_no_tex.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mousePressed(java.awt.event.MouseEvent evt) {
                showSelector(evt, 2);
            }
        });
        trans_no_tex.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mousePressed(java.awt.event.MouseEvent evt) {
                showSelector(evt, 3);
            }
        });
        kind_no_tex.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mousePressed(java.awt.event.MouseEvent evt) {
                showSelector(evt, 4);
            }
        });
        office_no_tex.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mousePressed(java.awt.event.MouseEvent evt) {
                showSelector(evt, 5);
            }
        });
        cont_no_tex.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mousePressed(java.awt.event.MouseEvent evt) {
                showSelector(evt, 6);
            }
        });
        car_kind_no_tex.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mousePressed(java.awt.event.MouseEvent evt) {
                showSelector(evt, 7);
            }
        });
        office_sub_name_.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mousePressed(java.awt.event.MouseEvent evt) {
                showSelector(evt, 8);
            }
        });

        //管理自社区分の初期値
        mStatus.SetData(Status.INDEX_CLASS, "0");

        //ジャーナル
        mStatus.SetData(Status.INDEX_WINDOW_OPEN, "");

        //処分業者初期表示
        mStatus.SetData(Status.INDEX_CONT, "1");

    }

    private void showSelector(MouseEvent evt, int index) {

        long now_time = System.currentTimeMillis();
        long gap_time = now_time - mLastClickTime;

        Object o = evt.getSource();

        if (gap_time < 500) {
            mClickCount++;
        } else {
            mClickCount = 1;
            mObject = o;
        }

        if (mClickCount >= 2 && !WeightFlag_ && mObject == o) {

            switch (index) {
                case 0:
                    SelectCarAction();
                    break;
                case 1:
                    SelectBillAction();
                    break;
                case 2:
                    SelectEntAction();
                    break;
                case 3:
                    SelectTransporterAction();
                    break;
                case 4:
                    SelectGoodsAction();
                    break;
                case 5:
                    SelectOfficeAction();
                    break;
                case 6:
                    SelectContAction();
                    break;
                case 7:
                    SelectCarKindAction();
                    break;
                case 8:
                    SelectOfficeSubAction();
                    break;
                default:
            }
        }
        mLastClickTime = now_time;
    }

    private Integer getNowWeight(int index) {
        //データベースへ重量値の問い合わせ
        String str = "";
        if (index == 1) {
            str = String.format("select last_value, last_data from sys_dlc where sno = %s;", 100);
        } else {
            str = String.format("select last_value, last_data from sys_dlc where sno = %s;", 101);
        }
        return Select(str, index);
    }

    private void getWeight() {
        now_weight_1 = getNowWeight(1);
        if ((now_weight_1 >= 200 || now_weight_2 >= 10) && !WeightFlag_) {
            MainPanel.OpaqueFlag_ = true;
        } else if ((now_weight_1 >= 200 || now_weight_2 >= 10) && WeightFlag_) {
            MainPanel.OpaqueFlag_ = false;
        } else if (!MainPanel.WeightFlag_) {
            MainPanel.OpaqueFlag_ = false;
        } else if (now_weight_1 < 10 || now_weight_2 < 10) {
            MainPanel.WeightFlag_ = false;
            MainPanel.OpaqueFlag_ = false;
            Status.WeightReady_ = false;

        }
    }

    public Integer Select(String sql, int index) {
        Integer ret = 0;

        try {
            PreparedStatement ps = con_.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                ret = rs.getInt("last_value");
                if (rs.getString("last_data") != null
                         && (rs.getString("last_data").equals("ST")
                         || rs.getString("last_data").equals(" "))) {
                    安定flag[index - 1] = true;
                } else {
                    安定flag[index - 1] = false;
                }
            }
        } catch (Exception e) {
            System.out.println("SQL-Select-exp:" + sql);
            安定flag[index - 1] = false;
        }
        return ret;
    }

    public void set風袋重量(String car_id, int empty_weight) {

        try {
            PreparedStatement ps = con_.prepareStatement(
                     String.format("update m_car_no set empty_weight = %s where car_id = %s;", empty_weight, car_id));
            ps.execute();

        } catch (Exception e) {
            System.out.println("SQL-set風袋重量-exp:" + e);
        }

    }

    //重量値の更新
    private void updateWeight() {
        Calendar c = Calendar.getInstance();
        term_date_ = DBManager.convert(c);
        count_jLab.setText(String.valueOf(count));
        //重量値の更新
        jLbl_now_weight1.setText(String.valueOf(now_weight_1).replaceAll("1", " 1") + " ");
        jLbl_loading_weight.setText(decimalformat(String.valueOf(loading_weight_)));
        jLbl_total_weight.setText(decimalformat(String.valueOf(total_weight_)));
        jLbl_empty_weight.setText(decimalformat(String.valueOf(empty_weight_)));
        jLbl_net_weight.setText(decimalformat(String.valueOf(net_weight_)));
        if (安定flag[0]) {
            //安定時 色の設定
            jLbl_now_weight1.setBackground(new Color(153, 255, 153));
        } else {
            //不安定時 色の設定
            jLbl_now_weight1.setBackground(new Color(255, 153, 153));
        }

        if (!f重量検知音声再生 && now_weight_1 >= 200) {
            f重量検知音声再生 = true;
            mSound.Play("/gatein.wav");
        }
        if (now_weight_1 < 200) {
            f重量検知音声再生 = false;
        }

        if (!OpaqueFlag_ && now_weight_1 < 100) {
            //計量完了または計量まえに重量１００ｋｇ以下なら画面初期化
            doCancel_log();
        }

        //計量完了したらコード入力させない
        car_id_.setEnabled(OpaqueFlag_);
        car_no_.setEnabled(OpaqueFlag_);
        bill_no_tex.setEnabled(OpaqueFlag_);
        ent_no_tex.setEnabled(OpaqueFlag_);
        trans_no_tex.setEnabled(OpaqueFlag_);
        kind_no_tex.setEnabled(OpaqueFlag_);
        office_no_tex.setEnabled(OpaqueFlag_);
        office_sub_name_.setEnabled(OpaqueFlag_);
        cont_no_tex.setEnabled(OpaqueFlag_);
        car_kind_no_tex.setEnabled(OpaqueFlag_);
        sanpai_tex.setEnabled(OpaqueFlag_);
        kensetu_tex.setEnabled(OpaqueFlag_);
        txt請求数量.setEnabled(OpaqueFlag_);

        weight_btn_計量処理.setEnabled(OpaqueFlag_);
        weight_btn_登録処理.setEnabled(OpaqueFlag_);
        weight_btn_重量取込.setEnabled(OpaqueFlag_);
        weight_btn_2回計量.setEnabled(OpaqueFlag_);
        weight_btn_風袋登録.setEnabled(OpaqueFlag_);
        empty_input.setEnabled(OpaqueFlag_);//風袋手入力ボタン追加　20161130

        // 検索条件, ソート条件を設定
        SearchCondition[] search = new SearchCondition[0];
        SortCondition[] sort
                 = new SortCondition[]{
                     new SortCondition(
                              new Column("時間", "register_date", ColumnType.DATE),
                              SortOrder.DESC)};

        // リスト更新
        int row = dataTable_.getSelectedRow();
        model_.refresh(search, sort);
        if ((0 <= row) && (model_.getRowCount() > row)) {
            dataTable_.setRowSelectionInterval(row, row);
        }
    }

    private void updateTable2() {
        //契約内容のテーブルモデル
        // 検索条件, ソート条件を設定
        SearchCondition[] search;
        SortCondition[] sort;
        Calendar c = Calendar.getInstance();
        // 検索条件, ソート条件を設定
        if (!searchWordField1_.getText().equals("")) {

            if (searchWordField2_.getText().equals("")) {
                search
                         = new SearchCondition[]{
                             new SearchCondition(
                                      new Column("契約自動更新", "auto_update_flag", ColumnType.BOOLEAN),
                                      CompareOperator.EQ,
                                      true,
                                      CombineOperator.AND),
                             new SearchCondition(
                                      new Column("排出業者", "ent_kana", ColumnType.TEXT),
                                      CompareOperator.LIKE,
                                      searchWordField1_.getText().trim(),
                                      CombineOperator.OR),
                             new SearchCondition(
                                      new Column("契約開始", "contract_start_date", ColumnType.DATE),
                                      CompareOperator.LE,
                                      DBManager.convert(c),
                                      CombineOperator.AND),
                             new SearchCondition(
                                      new Column("契約終了", "contract_end_date", ColumnType.DATE),
                                      CompareOperator.GE,
                                      DBManager.convert(c),
                                      CombineOperator.AND),
                             new SearchCondition(
                                      new Column("排出業者", "ent_kana", ColumnType.TEXT),
                                      CompareOperator.LIKE,
                                      searchWordField1_.getText().trim(),
                                      CombineOperator.NONE)};
            } else {
                search
                         = new SearchCondition[]{
                             new SearchCondition(
                                      new Column("契約自動更新", "auto_update_flag", ColumnType.BOOLEAN),
                                      CompareOperator.EQ,
                                      true,
                                      CombineOperator.AND),
                             new SearchCondition(
                                      new Column("排出業者", "ent_kana", ColumnType.TEXT),
                                      CompareOperator.LIKE,
                                      searchWordField1_.getText().trim(),
                                      CombineOperator.AND),
                             new SearchCondition(
                                      new Column("排出現場", "kana", ColumnType.TEXT),
                                      CompareOperator.LIKE,
                                      searchWordField2_.getText().trim(),
                                      CombineOperator.OR),
                             new SearchCondition(
                                      new Column("契約開始", "contract_start_date", ColumnType.DATE),
                                      CompareOperator.LE,
                                      DBManager.convert(c),
                                      CombineOperator.AND),
                             new SearchCondition(
                                      new Column("契約終了", "contract_end_date", ColumnType.DATE),
                                      CompareOperator.GE,
                                      DBManager.convert(c),
                                      CombineOperator.AND),
                             new SearchCondition(
                                      new Column("排出業者", "ent_kana", ColumnType.TEXT),
                                      CompareOperator.LIKE,
                                      searchWordField1_.getText().trim(),
                                      CombineOperator.AND),
                             new SearchCondition(
                                      new Column("排出現場", "kana", ColumnType.TEXT),
                                      CompareOperator.LIKE,
                                      searchWordField2_.getText().trim(),
                                      CombineOperator.NONE)};
            }

            if (0 < searchCondition_.size()) {
                search = searchCondition_.toArray(new SearchCondition[0]);
            }
            sort
                     = new SortCondition[]{
                         new SortCondition(
                                  new Column("排出業者ID", "ent_id", ColumnType.INT4),
                                  SortOrder.ASC),
                         new SortCondition(
                                  new Column("排出事業場ID", "id", ColumnType.INT4),
                                  SortOrder.ASC)};
            if (0 < sortCondition_.size()) {
                sort = sortCondition_.toArray(new SortCondition[0]);
            }

            int row2 = dataTable_1.getSelectedRow();
            model_1.refresh(search, sort);

            search
                     = new SearchCondition[]{
                         new SearchCondition(
                                  new Column("廃棄物sno", "sno", ColumnType.INT4),
                                  CompareOperator.GT,
                                  0,
                                  CombineOperator.AND),
                         new SearchCondition(
                                  new Column("廃棄物sno", "sno", ColumnType.INT4),
                                  CompareOperator.LE,
                                  40,
                                  CombineOperator.NONE)};

            sort
                     = new SortCondition[]{
                         new SortCondition(
                                  new Column("廃棄物sno", "sno", ColumnType.INT4),
                                  SortOrder.ASC)};
            gmodel_.refresh(search, sort);

            if ((0 <= row2) && (model_1.getRowCount() > row2)) {
                dataTable_1.setRowSelectionInterval(row2, row2);
            }

        } else {
            //検索フィールド初期化
            clearView();
            model_1.cleared();
        }
    }

    private void updateTable3() {
        //契約期間のテーブルモデル
        // 検索条件, ソート条件を設定
        SearchCondition[] search;
        SortCondition[] sort;
        // 検索条件, ソート条件を設定
        if (!searchWordField1_1.getText().equals("")) {

            if (searchWordField2_1.getText().equals("")) {
                search
                         = new SearchCondition[]{
                             new SearchCondition(
                                      new Column("排出業者", "ent_kana", ColumnType.TEXT),
                                      CompareOperator.LIKE,
                                      searchWordField1_1.getText().trim(),
                                      CombineOperator.NONE)};

            } else {
                search
                         = new SearchCondition[]{
                             new SearchCondition(
                                      new Column("排出業者", "ent_kana", ColumnType.TEXT),
                                      CompareOperator.LIKE,
                                      searchWordField1_1.getText().trim(),
                                      CombineOperator.AND),
                             new SearchCondition(
                                      new Column("排出現場", "kana", ColumnType.TEXT),
                                      CompareOperator.LIKE,
                                      searchWordField2_1.getText().trim(),
                                      CombineOperator.NONE)};
            }

            if (0 < searchCondition_.size()) {
                search = searchCondition_.toArray(new SearchCondition[0]);
            }
            sort
                     = new SortCondition[]{
                         new SortCondition(
                                  new Column("排出業者ID", "ent_id", ColumnType.INT4),
                                  SortOrder.ASC),
                         new SortCondition(
                                  new Column("排出事業場ID", "id", ColumnType.INT4),
                                  SortOrder.ASC)};
            if (0 < sortCondition_.size()) {
                sort = sortCondition_.toArray(new SortCondition[0]);
            }

            int row2 = dataTable_2.getSelectedRow();
            model_2.refresh(search, sort);

            if ((0 <= row2) && (model_2.getRowCount() > row2)) {
                dataTable_2.setRowSelectionInterval(row2, row2);
            }

        } else {
            //検索フィールド初期化
            clearView();
            model_2.cleared();
        }
    }

    private void clearView() {
        //検索フィールド初期化
        searchWordField2_.setText("");
        searchWordField2_1.setText("");
        model_1.cleared();
        model_2.cleared();
        goods_name01_.setText("");
        goods_name02_.setText("");
        goods_name03_.setText("");
        goods_name04_.setText("");
        goods_name05_.setText("");
        goods_name06_.setText("");
        goods_name07_.setText("");
        goods_name08_.setText("");
        goods_name09_.setText("");
        goods_name10_.setText("");
        goods_name11_.setText("");
        goods_name12_.setText("");
        goods_name13_.setText("");
        goods_name14_.setText("");
        goods_name15_.setText("");
        goods_name16_.setText("");
        goods_name17_.setText("");
        goods_name18_.setText("");
        goods_name19_.setText("");
        goods_name20_.setText("");
        goods_name21_.setText("");
        goods_name22_.setText("");
        goods_name23_.setText("");
        goods_name24_.setText("");
        goods_name25_.setText("");
        goods_name26_.setText("");
        goods_name27_.setText("");
        goods_name28_.setText("");
        goods_name29_.setText("");
        goods_name30_.setText("");
        goods_name31_.setText("");
        goods_name32_.setText("");
        goods_name33_.setText("");
        goods_name34_.setText("");
        goods_name35_.setText("");
        goods_name36_.setText("");
        goods_name37_.setText("");
        goods_name38_.setText("");
        goods_name39_.setText("");
        goods_name40_.setText("");
        rb1_.setSelected(false);
        rb2_.setSelected(false);
        rb3_.setSelected(false);
        rb4_.setSelected(false);
        rb5_.setSelected(false);
        rb6_.setSelected(false);
        rb7_.setSelected(false);
        rb8_.setSelected(false);
        rb9_.setSelected(false);
        rb10_.setSelected(false);
        rb11_.setSelected(false);
        rb12_.setSelected(false);
        rb13_.setSelected(false);
        rb14_.setSelected(false);
        rb15_.setSelected(false);
        rb16_.setSelected(false);
        rb17_.setSelected(false);
        rb18_.setSelected(false);
        rb19_.setSelected(false);
        rb20_.setSelected(false);
        rb21_.setSelected(false);
        rb22_.setSelected(false);
        rb23_.setSelected(false);
        rb24_.setSelected(false);
        rb25_.setSelected(false);
        rb26_.setSelected(false);
        rb27_.setSelected(false);
        rb28_.setSelected(false);
        rb29_.setSelected(false);
        rb30_.setSelected(false);
        rb31_.setSelected(false);
        rb32_.setSelected(false);
        rb33_.setSelected(false);
        rb34_.setSelected(false);
        rb35_.setSelected(false);
        rb36_.setSelected(false);
        rb37_.setSelected(false);
        rb38_.setSelected(false);
        rb39_.setSelected(false);
        rb40_.setSelected(false);
        trans_name01_.setText("");
        trans_name02_.setText("");
        trans_name03_.setText("");
        trans_name04_.setText("");
        trans_name05_.setText("");
        trb1_.setSelected(false);
        trb2_.setSelected(false);
        trb3_.setSelected(false);
        trb4_.setSelected(false);
        trb5_.setSelected(false);
        cont_name01_.setText("");
        cont_name01_.setBackground(new Color(255, 255, 255));
        cont_name02_.setText("");
        cont_name02_.setBackground(new Color(234, 234, 255));
        cont_name03_.setText("");
        cont_name03_.setBackground(new Color(255, 255, 255));
        cont_name04_.setText("");
        cont_name04_.setBackground(new Color(234, 234, 255));

    }

    //表示の更新
    public void UpdateView(RemoteStruct rs_) {

        rs = rs_;

        sub_message_.setText(String.valueOf(選択計量回数_) + ":" + sub_message_.getText());
        car_id_.setText(rs.GetString("car_id"));
        //20121016 add
        car_no_.setText(rs.GetString("car_no"));
        sub_message_.setText(rs.GetString("sub_message"));
        carry_name_.setText(rs.GetString("carry_name"));

        switch (carry_name_.getText()) {
            case "搬入":
                carry_name_.setForeground(new Color(0, 0, 255));//搬入なら青
                carry_name_.setBackground(Color.BLUE);
                break;
            case "搬出":
                carry_name_.setForeground(new Color(255, 0, 0));//搬出なら赤
                carry_name_.setBackground(Color.RED);
                break;
        }
        jLbl_計量日時.setText(rs.GetString("計量日時"));
        bill_no_tex.setText(rs.GetString("bill_id").length() == 0 ? "0" : rs.GetString("bill_id"));
        bill_jLab.setText(rs.GetString("bill_name"));
        ent_no_tex.setText(rs.GetString("dis_ent_id").length() == 0 ? "0" : rs.GetString("dis_ent_id"));
        ent_jLab.setText(rs.GetString("entrepreneur_name"));
        trans_no_tex.setText(rs.GetString("trans_id").length() == 0 ? "0" : rs.GetString("trans_id"));
        trans_jLab.setText(rs.GetString("transporter_name"));
        kind_no_tex.setText(rs.GetString("goods_id").length() == 0 ? "0" : rs.GetString("goods_id"));
        kind_jLab.setText(rs.GetString("goods_name"));
        office_no_tex.setText(rs.GetString("dis_office_id").length() == 0 ? "0" : rs.GetString("dis_office_id"));
        office_jLab.setText(rs.GetString("office_name"));
        cont_no_tex.setText(rs.GetString("dis_cont_id").length() == 0 ? "1" : rs.GetString("dis_cont_id"));
        cont_jLab.setText(rs.GetString("contractor_name"));
        car_kind_no_tex.setText(rs.GetString("car_kind_id").length() == 0 ? "0" : rs.GetString("car_kind_id"));
        car_kind_jLab.setText(rs.GetString("car_kind_name"));

        sanpai_tex.setText(rs.GetString("manifesto_no1"));
        kensetu_tex.setText(rs.GetString("manifesto_no2"));
        txt契約内容表示.setText(rs.GetString("契約内容表示"));
        office_sub_name_.setText(rs.GetString("office_sub_name"));
        jLbl_message.setText(rs.GetString("message"));
        if (!weight_err.equals("")) {
            // 20110416 add 重量スレッドからエラー受信時
            jLbl_message.setText(weight_err);
        }

        //20130422
        unit_value_tex.setText(rs.GetString("unit_value"));
        int unit_value_ = 0;
        try {
            unit_value_ = Integer.valueOf(rs.GetString("unit_value"));
        } catch (NumberFormatException ne) {
            unit_value_ = 0;
            unit_value_tex.setText("0");
        }

        mStatus.msRemote.mValue.put("ryube", rs.GetString("ryube"));
        double m3_ = 0.0;
        try {

            m3_ = Math.round((net_weight_ * 1.) / Double.valueOf((String) mStatus.msRemote.mValue.get("ryube")) / 1000 * 100) / 100d;
            if (Double.valueOf((String) mStatus.msRemote.mValue.get("ryube")) == 0.0) {
                m3_ = 0.0;
            }
        } catch (NumberFormatException ne) {
            m3_ = 0.0;
        }

        mStatus.SetData(Status.INDEX_M3, String.valueOf(m3_));

        String charge_value_ = 料金算出();
        mStatus.SetData(Status.INDEX_CHARGE_VALUE, charge_value_);
        charge_tex.setText(decimalformat(String.valueOf(charge_value_)));

    }

    //料金算出ファンクション
    private String 料金算出() {
        String ret = "0";
        //先に正味重量を計算させる
        if (net_weight_ > 0) {

            double billValue = 0.;
            int unit_no_ = 0;
            int unit_value_ = 0;
            double m3_ = 0.;
            try {
                unit_no_ = mStatus.msRemote.mValue.get("unit_no") != null ? (Integer) mStatus.msRemote.mValue.get("unit_no") : 0;
            } catch (ClassCastException ce) {
                unit_no_ = Integer.valueOf((String) mStatus.msRemote.mValue.get("unit_no"));
            }
            try {
                unit_value_ = (Integer) mStatus.msRemote.mValue.get("unit_value");
            } catch (ClassCastException ce) {
                unit_value_ = Integer.valueOf((String) mStatus.msRemote.mValue.get("unit_value"));
            }
            try {
                m3_ = (Double) mStatus.msRemote.mValue.get("m3");
            } catch (ClassCastException ce) {
                m3_ = Double.valueOf((String) mStatus.msRemote.mValue.get("m3"));
            }
            if (unit_no_ == 0) {
                billValue = 0;
                jLabel21.setText("数量");
                txt請求数量.setText("0");
            } else if (unit_no_ == 1) {
                billValue = (net_weight_ / 1000D) * unit_value_;//ton
                txt請求数量.setText(String.valueOf((net_weight_ / 1000D)));
                jLabel21.setText("数量(t)");
            } else if (unit_no_ == 2) {
                billValue = net_weight_ * unit_value_;//kg
                txt請求数量.setText(String.valueOf(net_weight_));
                jLabel21.setText("数量(kg)");
            } else if (unit_no_ == 3) {
                billValue = m3_ * unit_value_;//m3
                txt請求数量.setText(String.valueOf(m3_));
                jLabel21.setText("数量(㎥)");
            } else if (unit_no_ == 4) {
                billValue = 0;//リットル
                jLabel21.setText("数量(㍑)");
            } else if (unit_no_ == 5) {
                txt請求数量.setText("1");//台
                billValue = 1 * unit_value_;
                jLabel21.setText("数量(台)");
            } else if (unit_no_ == 6) {
                billValue = 0;//枚
                jLabel21.setText("数量(枚)");
            } else if (unit_no_ == 7) {
                billValue = 0;//個
                jLabel21.setText("数量(個)");
            } else if (unit_no_ == 8) {
                billValue = 0;//日
                jLabel21.setText("数量(日)");
            } else if (unit_no_ == 9) {
                txt請求数量.setText("1");//回
                billValue = 1 * unit_value_;
                jLabel21.setText("数量(回)");
            } else {
                billValue = 0;
            }

            ret = String.valueOf((int) Math.floor(billValue));
//            ret = String.valueOf(Math.round(billValue));
        }

        return ret;
    }

    //表示の初期化
    public void ClearView() {

        car_id_.setText("0");
        car_no_.setText("0");
        sub_message_.setText("");
        carry_name_.setText("");
        carry_name_.setBackground(new Color(240, 240, 240));
        count_jLab.setText("");
        bill_no_tex.setText("0");
        bill_jLab.setText("");
        ent_no_tex.setText("0");
        ent_jLab.setText("");
        trans_no_tex.setText("0");
        trans_jLab.setText("");
        kind_no_tex.setText("0");
        kind_jLab.setText("");
        office_no_tex.setText("0");
        office_jLab.setText("");
        cont_no_tex.setText("1");
        cont_jLab.setText("");
        car_kind_no_tex.setText("0");
        car_kind_jLab.setText("");

        jLbl_message.setText("");

        sanpai_tex.setText("");
        kensetu_tex.setText("");
        txt契約内容表示.setText("");
        jLbl_total_weight.setText("0 ");
        jLbl_empty_weight.setText("0 ");
        jLbl_net_weight.setText("0 ");
        total_weight_ = 0;
        empty_weight_ = 0;
        net_weight_ = 0;
        loading_weight_ = 0;
        now_weight_1 = 0;
        now_weight_2 = 0;
        unit_value_tex.setText("0");
        txt請求数量.setText("0");
        charge_tex.setText("0");

    }

    //画面下メッセージに表示
    public void StaticMessage(String msg) {
        jLbl_message.setText(msg);
    }

    //ポップアップメッセージを表示
    public static void PopupMessage(String msg) {
        mMessageDialog.setMessage(msg);
        mMessageDialog.setCenter();
        mMessageDialog.setVisible(true);
    }

    //ポップアップメッセージ２を表示
    public static boolean PopupMessage2(String msg, String btn_name, String btn_name2) {
        mMessageDialog2.setMessage(msg, btn_name, btn_name2);
        mMessageDialog2.setCenter();
        mMessageDialog2.setVisible(true);
        return mMessageDialog2.mResult;
    }

    public String decimalformat(String str) {
        java.text.DecimalFormat df = new java.text.DecimalFormat("#,###,##0 ");
        return df.format(Long.parseLong(str));
    }

    public String maniformat(String str) {
        if (str.length() > 11) {
            return str.substring(0, 11);
        } else {
            return str;
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        jPanel5 = new javax.swing.JPanel();
        jPanel_Top = new javax.swing.JPanel();
        jLabel_Title = new javax.swing.JLabel();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel6 = new javax.swing.JPanel();
        jPanel_Center = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        ent_no_tex = new aks.swing.JTextFieldKeiryo();
        kind_no_tex = new aks.swing.JTextFieldKeiryo();
        office_no_tex = new aks.swing.JTextFieldKeiryo();
        cont_no_tex = new aks.swing.JTextFieldKeiryo();
        jLabel13 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        car_kind_no_tex = new aks.swing.JTextFieldKeiryo();
        jLabel20 = new javax.swing.JLabel();
        sanpai_tex = new aks.swing.JTextFieldKeiryo();
        kensetu_tex = new aks.swing.JTextFieldKeiryo();
        count_jLab = new javax.swing.JLabel();
        ent_jLab = new javax.swing.JLabel();
        trans_jLab = new javax.swing.JLabel();
        office_jLab = new javax.swing.JLabel();
        cont_jLab = new javax.swing.JLabel();
        car_kind_jLab = new javax.swing.JLabel();
        jLabel22 = new javax.swing.JLabel();
        jLabel23 = new javax.swing.JLabel();
        bill_jLab = new javax.swing.JLabel();
        office_sub_name_ = new aks.swing.JTextFieldKeiryo();
        jPanel4 = new javax.swing.JPanel();
        unit_value_tex = new aks.swing.JTextFieldKeiryo();
        jLabel21 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        charge_tex = new javax.swing.JTextField();
        txt請求数量 = new aks.swing.JTextFieldKeiryo();
        jLabel29 = new javax.swing.JLabel();
        jLbl_計量日時 = new javax.swing.JLabel();
        jLabel_loading1 = new javax.swing.JLabel();
        empty_input = new javax.swing.JButton();
        jLabel24 = new javax.swing.JLabel();
        car_no_ = new aks.swing.JTextFieldKeiryo();
        sub_message_ = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        kind_jLab = new javax.swing.JLabel();
        carry_name_ = new javax.swing.JLabel();
        jButton9 = new javax.swing.JButton();
        txt契約内容表示 = new javax.swing.JLabel();
        cmb売上区分 = new javax.swing.JComboBox<>();
        jPanel_Bottom = new javax.swing.JPanel();
        jLbl_message = new javax.swing.JLabel();
        jPanel_Bottom1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        dataTable_ = new javax.swing.JTable();
        jPanel_Right = new javax.swing.JPanel();
        jLabel_gate_in = new javax.swing.JLabel();
        jLbl_total_weight = new javax.swing.JLabel();
        jLabel_gate_out = new javax.swing.JLabel();
        jLbl_empty_weight = new javax.swing.JLabel();
        jLabel_loading = new javax.swing.JLabel();
        jLbl_net_weight = new javax.swing.JLabel();
        jLbl_juuten1 = new javax.swing.JLabel();
        jLbl_juuten4 = new javax.swing.JLabel();
        jLbl_now_weight1 = new javax.swing.JLabel();
        jLbl_loading_weight = new javax.swing.JLabel();
        jPanel_Bottom10 = new javax.swing.JPanel();
        weight_btn_計量処理 = new javax.swing.JButton();
        weight_btn_登録処理 = new javax.swing.JButton();
        cancel_btn = new javax.swing.JButton();
        weight_btn_2回計量 = new javax.swing.JButton();
        weight_btn3 = new javax.swing.JButton();
        weight_btn_重量取込 = new javax.swing.JButton();
        weight_btn_風袋登録 = new javax.swing.JButton();
        weight_btn_重量初期化 = new javax.swing.JButton();
        jPanel7 = new javax.swing.JPanel();
        jPanel_Bottom3 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        dataTable_1 = new javax.swing.JTable();
        jLabel17 = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        searchWordField1_ = new aks.swing.JTextField();
        searchWordField2_ = new aks.swing.JTextField();
        btn絞込み = new javax.swing.JButton();
        jPanel8 = new javax.swing.JPanel();
        jScrollPane4 = new javax.swing.JScrollPane();
        jPanel13 = new javax.swing.JPanel();
        goods_name01_ = new javax.swing.JTextField();
        goods_name02_ = new javax.swing.JTextField();
        goods_name03_ = new javax.swing.JTextField();
        goods_name04_ = new javax.swing.JTextField();
        goods_name05_ = new javax.swing.JTextField();
        goods_name06_ = new javax.swing.JTextField();
        goods_name07_ = new javax.swing.JTextField();
        goods_name08_ = new javax.swing.JTextField();
        goods_name09_ = new javax.swing.JTextField();
        goods_name10_ = new javax.swing.JTextField();
        goods_name11_ = new javax.swing.JTextField();
        goods_name12_ = new javax.swing.JTextField();
        goods_name13_ = new javax.swing.JTextField();
        goods_name14_ = new javax.swing.JTextField();
        goods_name15_ = new javax.swing.JTextField();
        goods_name16_ = new javax.swing.JTextField();
        goods_name17_ = new javax.swing.JTextField();
        goods_name18_ = new javax.swing.JTextField();
        goods_name19_ = new javax.swing.JTextField();
        goods_name20_ = new javax.swing.JTextField();
        goods_name21_ = new javax.swing.JTextField();
        goods_name22_ = new javax.swing.JTextField();
        goods_name23_ = new javax.swing.JTextField();
        goods_name24_ = new javax.swing.JTextField();
        goods_name25_ = new javax.swing.JTextField();
        goods_name26_ = new javax.swing.JTextField();
        goods_name27_ = new javax.swing.JTextField();
        goods_name28_ = new javax.swing.JTextField();
        goods_name29_ = new javax.swing.JTextField();
        goods_name30_ = new javax.swing.JTextField();
        goods_name31_ = new javax.swing.JTextField();
        goods_name32_ = new javax.swing.JTextField();
        goods_name33_ = new javax.swing.JTextField();
        goods_name34_ = new javax.swing.JTextField();
        goods_name35_ = new javax.swing.JTextField();
        goods_name36_ = new javax.swing.JTextField();
        goods_name37_ = new javax.swing.JTextField();
        goods_name38_ = new javax.swing.JTextField();
        goods_name39_ = new javax.swing.JTextField();
        goods_name40_ = new javax.swing.JTextField();
        rb1_ = new javax.swing.JRadioButton();
        rb2_ = new javax.swing.JRadioButton();
        rb3_ = new javax.swing.JRadioButton();
        rb4_ = new javax.swing.JRadioButton();
        rb5_ = new javax.swing.JRadioButton();
        rb6_ = new javax.swing.JRadioButton();
        rb7_ = new javax.swing.JRadioButton();
        rb8_ = new javax.swing.JRadioButton();
        rb9_ = new javax.swing.JRadioButton();
        rb10_ = new javax.swing.JRadioButton();
        rb11_ = new javax.swing.JRadioButton();
        rb12_ = new javax.swing.JRadioButton();
        rb13_ = new javax.swing.JRadioButton();
        rb14_ = new javax.swing.JRadioButton();
        rb15_ = new javax.swing.JRadioButton();
        rb16_ = new javax.swing.JRadioButton();
        rb17_ = new javax.swing.JRadioButton();
        rb18_ = new javax.swing.JRadioButton();
        rb19_ = new javax.swing.JRadioButton();
        rb20_ = new javax.swing.JRadioButton();
        rb21_ = new javax.swing.JRadioButton();
        rb22_ = new javax.swing.JRadioButton();
        rb23_ = new javax.swing.JRadioButton();
        rb24_ = new javax.swing.JRadioButton();
        rb25_ = new javax.swing.JRadioButton();
        rb26_ = new javax.swing.JRadioButton();
        rb27_ = new javax.swing.JRadioButton();
        rb28_ = new javax.swing.JRadioButton();
        rb29_ = new javax.swing.JRadioButton();
        rb30_ = new javax.swing.JRadioButton();
        rb31_ = new javax.swing.JRadioButton();
        rb32_ = new javax.swing.JRadioButton();
        rb33_ = new javax.swing.JRadioButton();
        rb34_ = new javax.swing.JRadioButton();
        rb35_ = new javax.swing.JRadioButton();
        rb36_ = new javax.swing.JRadioButton();
        rb37_ = new javax.swing.JRadioButton();
        rb38_ = new javax.swing.JRadioButton();
        rb39_ = new javax.swing.JRadioButton();
        rb40_ = new javax.swing.JRadioButton();
        jScrollPane5 = new javax.swing.JScrollPane();
        jPanel14 = new javax.swing.JPanel();
        trans_name01_ = new javax.swing.JTextField();
        trans_name02_ = new javax.swing.JTextField();
        trans_name03_ = new javax.swing.JTextField();
        trans_name04_ = new javax.swing.JTextField();
        trans_name05_ = new javax.swing.JTextField();
        trb1_ = new javax.swing.JRadioButton();
        trb2_ = new javax.swing.JRadioButton();
        trb3_ = new javax.swing.JRadioButton();
        trb4_ = new javax.swing.JRadioButton();
        trb5_ = new javax.swing.JRadioButton();
        jScrollPane6 = new javax.swing.JScrollPane();
        jPanel15 = new javax.swing.JPanel();
        cont_name01_ = new javax.swing.JTextField();
        cont_name02_ = new javax.swing.JTextField();
        cont_name03_ = new javax.swing.JTextField();
        cont_name04_ = new javax.swing.JTextField();
        jButton8 = new javax.swing.JButton();
        jLabel27 = new javax.swing.JLabel();
        jLabel30 = new javax.swing.JLabel();
        jPanel12 = new javax.swing.JPanel();
        jPanel_Bottom4 = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        dataTable_2 = new javax.swing.JTable();
        searchWordField1_1 = new aks.swing.JTextField();
        searchWordField2_1 = new aks.swing.JTextField();
        btn絞込み1 = new javax.swing.JButton();
        jPanel9 = new javax.swing.JPanel();
        jLabel31 = new javax.swing.JLabel();
        jLabel32 = new javax.swing.JLabel();
        jLabel33 = new javax.swing.JLabel();

        setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        setFont(new java.awt.Font("ＭＳ ゴシック", 0, 12)); // NOI18N
        setMaximumSize(new java.awt.Dimension(1250, 900));
        setMinimumSize(new java.awt.Dimension(1250, 900));
        setName(""); // NOI18N
        setPreferredSize(new java.awt.Dimension(1250, 900));
        setLayout(new javax.swing.BoxLayout(this, javax.swing.BoxLayout.LINE_AXIS));

        jPanel5.setMaximumSize(new java.awt.Dimension(1250, 900));
        jPanel5.setMinimumSize(new java.awt.Dimension(1250, 900));
        jPanel5.setPreferredSize(new java.awt.Dimension(1250, 900));
        jPanel5.setLayout(new java.awt.GridBagLayout());

        jPanel_Top.setMaximumSize(new java.awt.Dimension(1280, 47));
        jPanel_Top.setMinimumSize(new java.awt.Dimension(1280, 47));
        jPanel_Top.setPreferredSize(new java.awt.Dimension(1280, 47));
        jPanel_Top.setLayout(new javax.swing.BoxLayout(jPanel_Top, javax.swing.BoxLayout.LINE_AXIS));

        jLabel_Title.setBackground(java.awt.Color.blue);
        jLabel_Title.setFont(new java.awt.Font("メイリオ", 0, 18)); // NOI18N
        jLabel_Title.setForeground(new java.awt.Color(255, 255, 255));
        jLabel_Title.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel_Title.setText("計　量");
        jLabel_Title.setFocusable(false);
        jLabel_Title.setMaximumSize(new java.awt.Dimension(1280, 40));
        jLabel_Title.setMinimumSize(new java.awt.Dimension(1280, 40));
        jLabel_Title.setOpaque(true);
        jLabel_Title.setPreferredSize(new java.awt.Dimension(1280, 40));
        jLabel_Title.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jLabel_TitleMousePressed(evt);
            }
        });
        jPanel_Top.add(jLabel_Title);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        jPanel5.add(jPanel_Top, gridBagConstraints);

        jTabbedPane1.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        jTabbedPane1.setMaximumSize(new java.awt.Dimension(1920, 860));
        jTabbedPane1.setMinimumSize(new java.awt.Dimension(1920, 860));
        jTabbedPane1.setOpaque(true);
        jTabbedPane1.setPreferredSize(new java.awt.Dimension(1920, 860));
        jTabbedPane1.setRequestFocusEnabled(false);
        jTabbedPane1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTabbedPane1MouseClicked(evt);
            }
        });

        jPanel6.setMaximumSize(new java.awt.Dimension(1280, 800));
        jPanel6.setMinimumSize(new java.awt.Dimension(1280, 800));
        jPanel6.setPreferredSize(new java.awt.Dimension(1280, 800));
        jPanel6.setLayout(new java.awt.GridBagLayout());

        jPanel_Center.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel_Center.setMinimumSize(new java.awt.Dimension(800, 450));
        jPanel_Center.setPreferredSize(new java.awt.Dimension(800, 450));
        jPanel_Center.setLayout(new java.awt.GridBagLayout());

        jPanel3.setFont(new java.awt.Font("MS UI Gothic", 1, 24)); // NOI18N
        jPanel3.setMaximumSize(new java.awt.Dimension(800, 420));
        jPanel3.setMinimumSize(new java.awt.Dimension(800, 420));
        jPanel3.setPreferredSize(new java.awt.Dimension(800, 420));

        jLabel10.setBackground(new java.awt.Color(0, 51, 153));
        jLabel10.setFont(new java.awt.Font("メイリオ", 0, 18)); // NOI18N
        jLabel10.setForeground(new java.awt.Color(255, 255, 255));
        jLabel10.setText("　取扱品目");
        jLabel10.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        jLabel10.setFocusable(false);
        jLabel10.setMaximumSize(new java.awt.Dimension(150, 30));
        jLabel10.setMinimumSize(new java.awt.Dimension(150, 30));
        jLabel10.setOpaque(true);
        jLabel10.setPreferredSize(new java.awt.Dimension(150, 30));

        jLabel11.setBackground(new java.awt.Color(0, 51, 153));
        jLabel11.setFont(new java.awt.Font("メイリオ", 0, 18)); // NOI18N
        jLabel11.setForeground(new java.awt.Color(255, 255, 255));
        jLabel11.setText("　請求先");
        jLabel11.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        jLabel11.setFocusable(false);
        jLabel11.setMaximumSize(new java.awt.Dimension(150, 30));
        jLabel11.setMinimumSize(new java.awt.Dimension(150, 30));
        jLabel11.setOpaque(true);
        jLabel11.setPreferredSize(new java.awt.Dimension(150, 30));

        jLabel12.setBackground(new java.awt.Color(0, 51, 153));
        jLabel12.setFont(new java.awt.Font("メイリオ", 0, 18)); // NOI18N
        jLabel12.setForeground(new java.awt.Color(255, 255, 255));
        jLabel12.setText("　車種");
        jLabel12.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        jLabel12.setFocusable(false);
        jLabel12.setMaximumSize(new java.awt.Dimension(150, 30));
        jLabel12.setMinimumSize(new java.awt.Dimension(150, 30));
        jLabel12.setOpaque(true);
        jLabel12.setPreferredSize(new java.awt.Dimension(150, 30));

        car_id_.setFont(new java.awt.Font("メイリオ", 0, 18)); // NOI18N
        car_id_.setHorizontalAlignment(aks.swing.JTextField.RIGHT);
        car_id_.setText("123456");
        car_id_.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        car_id_.setMaximumSize(new java.awt.Dimension(80, 35));
        car_id_.setMinimumSize(new java.awt.Dimension(80, 35));
        car_id_.setPreferredSize(new java.awt.Dimension(80, 35));
        car_id_.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                car_id_FocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                car_id_FocusLost(evt);
            }
        });
        car_id_.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                car_id_KeyPressed(evt);
            }
        });

        ent_no_tex.setFont(new java.awt.Font("メイリオ", 0, 18)); // NOI18N
        ent_no_tex.setHorizontalAlignment(aks.swing.JTextField.RIGHT);
        ent_no_tex.setText("123456");
        ent_no_tex.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED, new java.awt.Color(255, 102, 102), null));
        ent_no_tex.setMaximumSize(new java.awt.Dimension(80, 35));
        ent_no_tex.setMinimumSize(new java.awt.Dimension(80, 35));
        ent_no_tex.setPreferredSize(new java.awt.Dimension(80, 35));
        ent_no_tex.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                ent_no_texFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                ent_no_texFocusLost(evt);
            }
        });
        ent_no_tex.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                ent_no_texKeyPressed(evt);
            }
        });

        trans_no_tex.setFont(new java.awt.Font("メイリオ", 0, 18)); // NOI18N
        trans_no_tex.setHorizontalAlignment(aks.swing.JTextField.RIGHT);
        trans_no_tex.setText("123456");
        trans_no_tex.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED, new java.awt.Color(255, 102, 102), null));
        trans_no_tex.setMaximumSize(new java.awt.Dimension(80, 35));
        trans_no_tex.setMinimumSize(new java.awt.Dimension(80, 35));
        trans_no_tex.setPreferredSize(new java.awt.Dimension(80, 35));
        trans_no_tex.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                trans_no_texFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                trans_no_texFocusLost(evt);
            }
        });
        trans_no_tex.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                trans_no_texKeyPressed(evt);
            }
        });

        kind_no_tex.setFont(new java.awt.Font("メイリオ", 0, 18)); // NOI18N
        kind_no_tex.setHorizontalAlignment(aks.swing.JTextField.RIGHT);
        kind_no_tex.setText("123456");
        kind_no_tex.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED, new java.awt.Color(255, 102, 102), null));
        kind_no_tex.setMaximumSize(new java.awt.Dimension(80, 35));
        kind_no_tex.setMinimumSize(new java.awt.Dimension(80, 35));
        kind_no_tex.setPreferredSize(new java.awt.Dimension(80, 35));
        kind_no_tex.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                kind_no_texFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                kind_no_texFocusLost(evt);
            }
        });
        kind_no_tex.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                kind_no_texKeyPressed(evt);
            }
        });

        office_no_tex.setFont(new java.awt.Font("メイリオ", 0, 18)); // NOI18N
        office_no_tex.setHorizontalAlignment(aks.swing.JTextField.RIGHT);
        office_no_tex.setText("123456");
        office_no_tex.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED, new java.awt.Color(255, 102, 102), null));
        office_no_tex.setMaximumSize(new java.awt.Dimension(80, 35));
        office_no_tex.setMinimumSize(new java.awt.Dimension(80, 35));
        office_no_tex.setPreferredSize(new java.awt.Dimension(80, 35));
        office_no_tex.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                office_no_texFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                office_no_texFocusLost(evt);
            }
        });
        office_no_tex.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                office_no_texKeyPressed(evt);
            }
        });

        cont_no_tex.setFont(new java.awt.Font("メイリオ", 0, 18)); // NOI18N
        cont_no_tex.setHorizontalAlignment(aks.swing.JTextField.RIGHT);
        cont_no_tex.setText("123456");
        cont_no_tex.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED, new java.awt.Color(255, 102, 102), null));
        cont_no_tex.setMaximumSize(new java.awt.Dimension(80, 35));
        cont_no_tex.setMinimumSize(new java.awt.Dimension(80, 35));
        cont_no_tex.setPreferredSize(new java.awt.Dimension(80, 35));
        cont_no_tex.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                cont_no_texFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                cont_no_texFocusLost(evt);
            }
        });
        cont_no_tex.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                cont_no_texKeyPressed(evt);
            }
        });

        jLabel13.setBackground(new java.awt.Color(0, 51, 153));
        jLabel13.setFont(new java.awt.Font("メイリオ", 0, 18)); // NOI18N
        jLabel13.setForeground(new java.awt.Color(255, 255, 255));
        jLabel13.setText("　排出業者");
        jLabel13.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        jLabel13.setFocusable(false);
        jLabel13.setMaximumSize(new java.awt.Dimension(150, 30));
        jLabel13.setMinimumSize(new java.awt.Dimension(150, 30));
        jLabel13.setOpaque(true);
        jLabel13.setPreferredSize(new java.awt.Dimension(150, 30));

        jLabel14.setBackground(new java.awt.Color(0, 51, 153));
        jLabel14.setFont(new java.awt.Font("メイリオ", 0, 18)); // NOI18N
        jLabel14.setForeground(new java.awt.Color(255, 255, 255));
        jLabel14.setText("　排出現場");
        jLabel14.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        jLabel14.setFocusable(false);
        jLabel14.setMaximumSize(new java.awt.Dimension(150, 30));
        jLabel14.setMinimumSize(new java.awt.Dimension(150, 30));
        jLabel14.setOpaque(true);
        jLabel14.setPreferredSize(new java.awt.Dimension(150, 30));

        jLabel15.setBackground(new java.awt.Color(0, 51, 153));
        jLabel15.setFont(new java.awt.Font("メイリオ", 0, 18)); // NOI18N
        jLabel15.setForeground(new java.awt.Color(255, 255, 255));
        jLabel15.setText("　運搬業者");
        jLabel15.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        jLabel15.setFocusable(false);
        jLabel15.setMaximumSize(new java.awt.Dimension(150, 30));
        jLabel15.setMinimumSize(new java.awt.Dimension(150, 30));
        jLabel15.setOpaque(true);
        jLabel15.setPreferredSize(new java.awt.Dimension(150, 30));

        jLabel16.setBackground(new java.awt.Color(0, 51, 153));
        jLabel16.setFont(new java.awt.Font("メイリオ", 0, 18)); // NOI18N
        jLabel16.setForeground(new java.awt.Color(255, 255, 255));
        jLabel16.setText("　処分業者");
        jLabel16.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        jLabel16.setFocusable(false);
        jLabel16.setMaximumSize(new java.awt.Dimension(150, 30));
        jLabel16.setMinimumSize(new java.awt.Dimension(150, 30));
        jLabel16.setOpaque(true);
        jLabel16.setPreferredSize(new java.awt.Dimension(150, 30));

        jLabel18.setBackground(new java.awt.Color(0, 51, 153));
        jLabel18.setFont(new java.awt.Font("メイリオ", 0, 18)); // NOI18N
        jLabel18.setForeground(new java.awt.Color(255, 255, 255));
        jLabel18.setText("　計量回数");
        jLabel18.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        jLabel18.setFocusable(false);
        jLabel18.setMaximumSize(new java.awt.Dimension(150, 30));
        jLabel18.setMinimumSize(new java.awt.Dimension(150, 30));
        jLabel18.setOpaque(true);
        jLabel18.setPreferredSize(new java.awt.Dimension(150, 30));

        car_kind_no_tex.setFont(new java.awt.Font("メイリオ", 0, 18)); // NOI18N
        car_kind_no_tex.setHorizontalAlignment(aks.swing.JTextField.RIGHT);
        car_kind_no_tex.setText("123456");
        car_kind_no_tex.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED, new java.awt.Color(255, 102, 102), null));
        car_kind_no_tex.setMaximumSize(new java.awt.Dimension(80, 35));
        car_kind_no_tex.setMinimumSize(new java.awt.Dimension(80, 35));
        car_kind_no_tex.setPreferredSize(new java.awt.Dimension(80, 35));
        car_kind_no_tex.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                car_kind_no_texFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                car_kind_no_texFocusLost(evt);
            }
        });
        car_kind_no_tex.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                car_kind_no_texKeyPressed(evt);
            }
        });

        jLabel20.setBackground(new java.awt.Color(0, 51, 153));
        jLabel20.setFont(new java.awt.Font("メイリオ", 0, 18)); // NOI18N
        jLabel20.setForeground(new java.awt.Color(255, 255, 255));
        jLabel20.setText("　建廃交付番号");
        jLabel20.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        jLabel20.setMaximumSize(new java.awt.Dimension(150, 30));
        jLabel20.setMinimumSize(new java.awt.Dimension(150, 30));
        jLabel20.setOpaque(true);
        jLabel20.setPreferredSize(new java.awt.Dimension(150, 30));

        sanpai_tex.setFont(new java.awt.Font("メイリオ", 0, 18)); // NOI18N
        sanpai_tex.setHorizontalAlignment(aks.swing.JTextField.RIGHT);
        sanpai_tex.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED, new java.awt.Color(255, 102, 102), null));
        sanpai_tex.setMaximumSize(new java.awt.Dimension(170, 35));
        sanpai_tex.setMinimumSize(new java.awt.Dimension(170, 35));
        sanpai_tex.setPreferredSize(new java.awt.Dimension(170, 35));
        sanpai_tex.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                sanpai_texFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                sanpai_texFocusLost(evt);
            }
        });
        sanpai_tex.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                sanpai_texKeyPressed(evt);
            }
        });

        kensetu_tex.setFont(new java.awt.Font("メイリオ", 0, 18)); // NOI18N
        kensetu_tex.setHorizontalAlignment(aks.swing.JTextField.RIGHT);
        kensetu_tex.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED, new java.awt.Color(255, 102, 102), null));
        kensetu_tex.setMaximumSize(new java.awt.Dimension(155, 35));
        kensetu_tex.setMinimumSize(new java.awt.Dimension(170, 35));
        kensetu_tex.setPreferredSize(new java.awt.Dimension(170, 35));
        kensetu_tex.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                kensetu_texFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                kensetu_texFocusLost(evt);
            }
        });
        kensetu_tex.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                kensetu_texKeyPressed(evt);
            }
        });

        count_jLab.setBackground(new java.awt.Color(204, 255, 255));
        count_jLab.setFont(new java.awt.Font("メイリオ", 0, 18)); // NOI18N
        count_jLab.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        count_jLab.setText("999");
        count_jLab.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        count_jLab.setFocusable(false);
        count_jLab.setMaximumSize(new java.awt.Dimension(80, 35));
        count_jLab.setMinimumSize(new java.awt.Dimension(80, 35));
        count_jLab.setOpaque(true);
        count_jLab.setPreferredSize(new java.awt.Dimension(80, 35));

        ent_jLab.setBackground(new java.awt.Color(204, 255, 255));
        ent_jLab.setFont(new java.awt.Font("メイリオ", 0, 18)); // NOI18N
        ent_jLab.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        ent_jLab.setFocusable(false);
        ent_jLab.setMaximumSize(new java.awt.Dimension(150, 35));
        ent_jLab.setMinimumSize(new java.awt.Dimension(150, 35));
        ent_jLab.setOpaque(true);
        ent_jLab.setPreferredSize(new java.awt.Dimension(150, 35));

        trans_jLab.setBackground(new java.awt.Color(204, 255, 255));
        trans_jLab.setFont(new java.awt.Font("メイリオ", 0, 18)); // NOI18N
        trans_jLab.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        trans_jLab.setFocusable(false);
        trans_jLab.setMaximumSize(new java.awt.Dimension(150, 35));
        trans_jLab.setMinimumSize(new java.awt.Dimension(150, 35));
        trans_jLab.setOpaque(true);
        trans_jLab.setPreferredSize(new java.awt.Dimension(150, 35));

        office_jLab.setBackground(new java.awt.Color(204, 255, 255));
        office_jLab.setFont(new java.awt.Font("メイリオ", 0, 18)); // NOI18N
        office_jLab.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        office_jLab.setFocusable(false);
        office_jLab.setMaximumSize(new java.awt.Dimension(400, 35));
        office_jLab.setMinimumSize(new java.awt.Dimension(400, 35));
        office_jLab.setOpaque(true);
        office_jLab.setPreferredSize(new java.awt.Dimension(400, 35));

        cont_jLab.setBackground(new java.awt.Color(204, 255, 255));
        cont_jLab.setFont(new java.awt.Font("メイリオ", 0, 18)); // NOI18N
        cont_jLab.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        cont_jLab.setMaximumSize(new java.awt.Dimension(150, 35));
        cont_jLab.setMinimumSize(new java.awt.Dimension(150, 35));
        cont_jLab.setOpaque(true);
        cont_jLab.setPreferredSize(new java.awt.Dimension(150, 35));

        car_kind_jLab.setBackground(new java.awt.Color(204, 255, 255));
        car_kind_jLab.setFont(new java.awt.Font("メイリオ", 0, 18)); // NOI18N
        car_kind_jLab.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        car_kind_jLab.setMaximumSize(new java.awt.Dimension(150, 35));
        car_kind_jLab.setMinimumSize(new java.awt.Dimension(150, 35));
        car_kind_jLab.setOpaque(true);
        car_kind_jLab.setPreferredSize(new java.awt.Dimension(150, 35));

        jLabel22.setBackground(new java.awt.Color(0, 51, 153));
        jLabel22.setFont(new java.awt.Font("メイリオ", 0, 18)); // NOI18N
        jLabel22.setForeground(new java.awt.Color(255, 255, 255));
        jLabel22.setText("　産廃交付番号");
        jLabel22.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        jLabel22.setMaximumSize(new java.awt.Dimension(150, 30));
        jLabel22.setMinimumSize(new java.awt.Dimension(150, 30));
        jLabel22.setOpaque(true);
        jLabel22.setPreferredSize(new java.awt.Dimension(150, 30));

        jLabel23.setBackground(new java.awt.Color(0, 51, 153));
        jLabel23.setFont(new java.awt.Font("メイリオ", 0, 18)); // NOI18N
        jLabel23.setForeground(new java.awt.Color(255, 255, 255));
        jLabel23.setText("　車番");
        jLabel23.setFocusable(false);
        jLabel23.setMaximumSize(new java.awt.Dimension(70, 30));
        jLabel23.setMinimumSize(new java.awt.Dimension(70, 30));
        jLabel23.setOpaque(true);
        jLabel23.setPreferredSize(new java.awt.Dimension(70, 30));

        bill_no_tex.setFont(new java.awt.Font("メイリオ", 0, 18)); // NOI18N
        bill_no_tex.setHorizontalAlignment(aks.swing.JTextField.RIGHT);
        bill_no_tex.setText("123456");
        bill_no_tex.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED, new java.awt.Color(255, 102, 102), null));
        bill_no_tex.setMaximumSize(new java.awt.Dimension(80, 35));
        bill_no_tex.setMinimumSize(new java.awt.Dimension(80, 35));
        bill_no_tex.setPreferredSize(new java.awt.Dimension(80, 35));
        bill_no_tex.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                bill_no_texFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                bill_no_texFocusLost(evt);
            }
        });
        bill_no_tex.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                bill_no_texKeyPressed(evt);
            }
        });

        bill_jLab.setBackground(new java.awt.Color(204, 255, 255));
        bill_jLab.setFont(new java.awt.Font("メイリオ", 0, 18)); // NOI18N
        bill_jLab.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        bill_jLab.setFocusable(false);
        bill_jLab.setMaximumSize(new java.awt.Dimension(590, 35));
        bill_jLab.setMinimumSize(new java.awt.Dimension(590, 35));
        bill_jLab.setOpaque(true);
        bill_jLab.setPreferredSize(new java.awt.Dimension(590, 35));

        office_sub_name_.setFont(new java.awt.Font("メイリオ", 0, 18)); // NOI18N
        office_sub_name_.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        office_sub_name_.setMaximumSize(new java.awt.Dimension(350, 35));
        office_sub_name_.setMinimumSize(new java.awt.Dimension(350, 35));
        office_sub_name_.setPreferredSize(new java.awt.Dimension(350, 35));
        office_sub_name_.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                office_sub_name_FocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                office_sub_name_FocusLost(evt);
            }
        });
        office_sub_name_.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                office_sub_name_KeyPressed(evt);
            }
        });

        jPanel4.setMaximumSize(new java.awt.Dimension(300, 86));
        jPanel4.setMinimumSize(new java.awt.Dimension(300, 86));
        jPanel4.setPreferredSize(new java.awt.Dimension(300, 86));
        jPanel4.setLayout(new java.awt.GridBagLayout());

        unit_value_tex.setEditable(false);
        unit_value_tex.setFont(new java.awt.Font("メイリオ", 0, 18)); // NOI18N
        unit_value_tex.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        unit_value_tex.setText("12345");
        unit_value_tex.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        unit_value_tex.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        unit_value_tex.setEnabled(false);
        unit_value_tex.setMaximumSize(new java.awt.Dimension(90, 35));
        unit_value_tex.setMinimumSize(new java.awt.Dimension(90, 35));
        unit_value_tex.setPreferredSize(new java.awt.Dimension(90, 35));
        unit_value_tex.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                unit_value_texFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                unit_value_texFocusLost(evt);
            }
        });
        unit_value_tex.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                unit_value_texKeyPressed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.insets = new java.awt.Insets(0, 2, 2, 2);
        jPanel4.add(unit_value_tex, gridBagConstraints);

        jLabel21.setBackground(new java.awt.Color(0, 51, 153));
        jLabel21.setFont(new java.awt.Font("メイリオ", 0, 18)); // NOI18N
        jLabel21.setForeground(new java.awt.Color(255, 255, 255));
        jLabel21.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel21.setText("数量");
        jLabel21.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        jLabel21.setFocusable(false);
        jLabel21.setMaximumSize(new java.awt.Dimension(90, 30));
        jLabel21.setMinimumSize(new java.awt.Dimension(90, 30));
        jLabel21.setOpaque(true);
        jLabel21.setPreferredSize(new java.awt.Dimension(90, 30));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(5, 2, 0, 2);
        jPanel4.add(jLabel21, gridBagConstraints);

        jLabel2.setBackground(new java.awt.Color(0, 51, 153));
        jLabel2.setFont(new java.awt.Font("メイリオ", 0, 18)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel2.setText("料金(円)");
        jLabel2.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        jLabel2.setMaximumSize(new java.awt.Dimension(100, 30));
        jLabel2.setMinimumSize(new java.awt.Dimension(100, 30));
        jLabel2.setOpaque(true);
        jLabel2.setPreferredSize(new java.awt.Dimension(100, 30));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(5, 2, 0, 2);
        jPanel4.add(jLabel2, gridBagConstraints);

        charge_tex.setBackground(new java.awt.Color(204, 255, 255));
        charge_tex.setFont(new java.awt.Font("メイリオ", 0, 18)); // NOI18N
        charge_tex.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        charge_tex.setText("1234567");
        charge_tex.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        charge_tex.setFocusable(false);
        charge_tex.setMaximumSize(new java.awt.Dimension(100, 35));
        charge_tex.setMinimumSize(new java.awt.Dimension(100, 35));
        charge_tex.setPreferredSize(new java.awt.Dimension(100, 35));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.insets = new java.awt.Insets(0, 2, 2, 2);
        jPanel4.add(charge_tex, gridBagConstraints);

        txt請求数量.setBackground(new java.awt.Color(204, 255, 255));
        txt請求数量.setFont(new java.awt.Font("メイリオ", 0, 18)); // NOI18N
        txt請求数量.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txt請求数量.setText("12345");
        txt請求数量.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        txt請求数量.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        txt請求数量.setEnabled(false);
        txt請求数量.setFocusable(false);
        txt請求数量.setMaximumSize(new java.awt.Dimension(90, 35));
        txt請求数量.setMinimumSize(new java.awt.Dimension(90, 35));
        txt請求数量.setPreferredSize(new java.awt.Dimension(90, 35));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.insets = new java.awt.Insets(0, 2, 2, 2);
        jPanel4.add(txt請求数量, gridBagConstraints);

        jLabel29.setBackground(new java.awt.Color(0, 51, 153));
        jLabel29.setFont(new java.awt.Font("メイリオ", 0, 18)); // NOI18N
        jLabel29.setForeground(new java.awt.Color(255, 255, 255));
        jLabel29.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel29.setText("単価(円)");
        jLabel29.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        jLabel29.setFocusable(false);
        jLabel29.setMaximumSize(new java.awt.Dimension(90, 30));
        jLabel29.setMinimumSize(new java.awt.Dimension(90, 30));
        jLabel29.setOpaque(true);
        jLabel29.setPreferredSize(new java.awt.Dimension(90, 30));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(5, 2, 0, 2);
        jPanel4.add(jLabel29, gridBagConstraints);

        jLbl_計量日時.setBackground(new java.awt.Color(204, 255, 255));
        jLbl_計量日時.setFont(new java.awt.Font("メイリオ", 0, 24)); // NOI18N
        jLbl_計量日時.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLbl_計量日時.setText("01日 10時20分");
        jLbl_計量日時.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 3));
        jLbl_計量日時.setFocusable(false);
        jLbl_計量日時.setMaximumSize(new java.awt.Dimension(250, 40));
        jLbl_計量日時.setMinimumSize(new java.awt.Dimension(250, 40));
        jLbl_計量日時.setOpaque(true);
        jLbl_計量日時.setPreferredSize(new java.awt.Dimension(250, 40));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        jPanel4.add(jLbl_計量日時, gridBagConstraints);

        jLabel_loading1.setBackground(new java.awt.Color(0, 51, 153));
        jLabel_loading1.setFont(new java.awt.Font("メイリオ", 0, 18)); // NOI18N
        jLabel_loading1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel_loading1.setText("　計量日時");
        jLabel_loading1.setMaximumSize(new java.awt.Dimension(200, 30));
        jLabel_loading1.setMinimumSize(new java.awt.Dimension(200, 30));
        jLabel_loading1.setOpaque(true);
        jLabel_loading1.setPreferredSize(new java.awt.Dimension(200, 30));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 0, 0, 0);
        jPanel4.add(jLabel_loading1, gridBagConstraints);

        empty_input.setBackground(new java.awt.Color(255, 153, 153));
        empty_input.setFont(new java.awt.Font("メイリオ", 0, 18)); // NOI18N
        empty_input.setText("<html>風袋<br> 手入力</html>");
        empty_input.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        empty_input.setMaximumSize(new java.awt.Dimension(90, 70));
        empty_input.setMinimumSize(new java.awt.Dimension(90, 70));
        empty_input.setPreferredSize(new java.awt.Dimension(107, 70));
        empty_input.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                empty_inputFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                empty_inputFocusLost(evt);
            }
        });
        empty_input.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                empty_inputActionPerformed(evt);
            }
        });
        empty_input.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                empty_inputKeyPressed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.insets = new java.awt.Insets(5, 2, 0, 2);
        jPanel4.add(empty_input, gridBagConstraints);

        jLabel24.setBackground(new java.awt.Color(0, 51, 153));
        jLabel24.setFont(new java.awt.Font("メイリオ", 0, 18)); // NOI18N
        jLabel24.setForeground(new java.awt.Color(255, 255, 255));
        jLabel24.setText("　管理ID");
        jLabel24.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        jLabel24.setFocusable(false);
        jLabel24.setMaximumSize(new java.awt.Dimension(150, 30));
        jLabel24.setMinimumSize(new java.awt.Dimension(150, 30));
        jLabel24.setOpaque(true);
        jLabel24.setPreferredSize(new java.awt.Dimension(150, 30));

        car_no_.setFont(new java.awt.Font("メイリオ", 0, 18)); // NOI18N
        car_no_.setHorizontalAlignment(aks.swing.JTextField.RIGHT);
        car_no_.setText("1234");
        car_no_.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        car_no_.setMinimumSize(new java.awt.Dimension(100, 35));
        car_no_.setPreferredSize(new java.awt.Dimension(100, 35));
        car_no_.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                car_no_FocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                car_no_FocusLost(evt);
            }
        });
        car_no_.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                car_no_KeyPressed(evt);
            }
        });

        sub_message_.setFont(new java.awt.Font("メイリオ", 0, 24)); // NOI18N
        sub_message_.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        sub_message_.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        sub_message_.setMaximumSize(new java.awt.Dimension(295, 35));
        sub_message_.setMinimumSize(new java.awt.Dimension(295, 35));
        sub_message_.setOpaque(true);
        sub_message_.setPreferredSize(new java.awt.Dimension(600, 35));

        jPanel1.setMaximumSize(new java.awt.Dimension(100, 35));
        jPanel1.setMinimumSize(new java.awt.Dimension(100, 35));
        jPanel1.setPreferredSize(new java.awt.Dimension(100, 35));
        jPanel1.setLayout(new java.awt.GridBagLayout());

        kind_jLab.setBackground(new java.awt.Color(204, 255, 255));
        kind_jLab.setFont(new java.awt.Font("メイリオ", 0, 18)); // NOI18N
        kind_jLab.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        kind_jLab.setFocusable(false);
        kind_jLab.setMaximumSize(new java.awt.Dimension(500, 35));
        kind_jLab.setMinimumSize(new java.awt.Dimension(500, 35));
        kind_jLab.setOpaque(true);
        kind_jLab.setPreferredSize(new java.awt.Dimension(500, 35));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 10);
        jPanel1.add(kind_jLab, gridBagConstraints);

        carry_name_.setFont(new java.awt.Font("メイリオ", 0, 24)); // NOI18N
        carry_name_.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        carry_name_.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        carry_name_.setMaximumSize(new java.awt.Dimension(130, 35));
        carry_name_.setMinimumSize(new java.awt.Dimension(130, 35));
        carry_name_.setPreferredSize(new java.awt.Dimension(130, 35));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        jPanel1.add(carry_name_, gridBagConstraints);

        jButton9.setFont(new java.awt.Font("メイリオ", 0, 18)); // NOI18N
        jButton9.setText("ID検索");
        jButton9.setFocusable(false);
        jButton9.setMargin(new java.awt.Insets(0, 0, 0, 0));
        jButton9.setMaximumSize(new java.awt.Dimension(85, 30));
        jButton9.setMinimumSize(new java.awt.Dimension(85, 30));
        jButton9.setPreferredSize(new java.awt.Dimension(85, 30));
        jButton9.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jButton9MouseClicked(evt);
            }
        });
        jButton9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton9ActionPerformed(evt);
            }
        });

        txt契約内容表示.setFont(new java.awt.Font("メイリオ", 0, 24)); // NOI18N
        txt契約内容表示.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        txt契約内容表示.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        txt契約内容表示.setMaximumSize(new java.awt.Dimension(530, 35));
        txt契約内容表示.setMinimumSize(new java.awt.Dimension(530, 35));
        txt契約内容表示.setOpaque(true);
        txt契約内容表示.setPreferredSize(new java.awt.Dimension(530, 35));

        cmb売上区分.setFont(new java.awt.Font("メイリオ", 0, 18)); // NOI18N
        cmb売上区分.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "掛け", "現金" }));
        cmb売上区分.setMaximumSize(new java.awt.Dimension(130, 35));
        cmb売上区分.setMinimumSize(new java.awt.Dimension(130, 35));
        cmb売上区分.setOpaque(true);
        cmb売上区分.setPreferredSize(new java.awt.Dimension(130, 35));

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addComponent(jLabel18, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(5, 5, 5)
                .addComponent(count_jLab, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(5, 5, 5)
                .addComponent(jButton9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(7, 7, 7)
                .addComponent(sub_message_, javax.swing.GroupLayout.PREFERRED_SIZE, 639, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addComponent(jLabel24, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(5, 5, 5)
                .addComponent(car_id_, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(4, 4, 4)
                .addComponent(jLabel23, javax.swing.GroupLayout.PREFERRED_SIZE, 89, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(4, 4, 4)
                .addComponent(car_no_, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(9, 9, 9)
                .addComponent(txt契約内容表示, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(5, 5, 5)
                .addComponent(bill_no_tex, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(4, 4, 4)
                .addComponent(bill_jLab, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(12, 12, 12)
                .addComponent(cmb売上区分, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(5, 5, 5)
                .addComponent(ent_no_tex, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(4, 4, 4)
                .addComponent(ent_jLab, javax.swing.GroupLayout.PREFERRED_SIZE, 732, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addComponent(jLabel14, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(5, 5, 5)
                .addComponent(office_no_tex, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(4, 4, 4)
                .addComponent(office_jLab, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(7, 7, 7)
                .addComponent(office_sub_name_, javax.swing.GroupLayout.PREFERRED_SIZE, 325, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(5, 5, 5)
                .addComponent(kind_no_tex, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(4, 4, 4)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 732, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addComponent(jLabel15, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(5, 5, 5)
                .addComponent(trans_no_tex, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(4, 4, 4)
                .addComponent(trans_jLab, javax.swing.GroupLayout.PREFERRED_SIZE, 732, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addComponent(jLabel16, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(5, 5, 5)
                .addComponent(cont_no_tex, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(4, 4, 4)
                .addComponent(cont_jLab, javax.swing.GroupLayout.PREFERRED_SIZE, 732, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(5, 5, 5)
                .addComponent(car_kind_no_tex, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(4, 4, 4)
                .addComponent(car_kind_jLab, javax.swing.GroupLayout.PREFERRED_SIZE, 732, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel20, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel22, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(5, 5, 5)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(kensetu_tex, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(sanpai_tex, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(5, 5, 5)
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, 646, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(4, 4, 4)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(count_jLab, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(sub_message_, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(2, 2, 2)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel18, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGap(4, 4, 4)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(car_id_, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(car_no_, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txt契約内容表示, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(2, 2, 2)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel24, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel23, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGap(4, 4, 4)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(2, 2, 2)
                        .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(bill_no_tex, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(bill_jLab, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cmb売上区分, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(4, 4, 4)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(2, 2, 2)
                        .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(ent_no_tex, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(ent_jLab, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(4, 4, 4)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(2, 2, 2)
                        .addComponent(jLabel14, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(office_no_tex, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(office_jLab, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(office_sub_name_, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(4, 4, 4)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(2, 2, 2)
                        .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(kind_no_tex, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(4, 4, 4)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(2, 2, 2)
                        .addComponent(jLabel15, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(trans_no_tex, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(trans_jLab, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(4, 4, 4)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(2, 2, 2)
                        .addComponent(jLabel16, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(cont_no_tex, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cont_jLab, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(4, 4, 4)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(2, 2, 2)
                        .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(car_kind_no_tex, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(car_kind_jLab, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(2, 2, 2)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(4, 4, 4)
                        .addComponent(jLabel20, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(8, 8, 8)
                        .addComponent(jLabel22, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(2, 2, 2)
                        .addComponent(kensetu_tex, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(4, 4, 4)
                        .addComponent(sanpai_tex, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, 91, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 0.8;
        gridBagConstraints.weighty = 1.0;
        jPanel_Center.add(jPanel3, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.weightx = 0.8;
        gridBagConstraints.insets = new java.awt.Insets(0, 20, 0, 0);
        jPanel6.add(jPanel_Center, gridBagConstraints);

        jPanel_Bottom.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)), "メッセージ", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("メイリオ", 0, 18))); // NOI18N
        jPanel_Bottom.setFont(new java.awt.Font("メイリオ", 0, 24)); // NOI18N
        jPanel_Bottom.setMaximumSize(new java.awt.Dimension(800, 100));
        jPanel_Bottom.setMinimumSize(new java.awt.Dimension(800, 100));
        jPanel_Bottom.setPreferredSize(new java.awt.Dimension(800, 100));
        jPanel_Bottom.setLayout(new java.awt.GridBagLayout());

        jLbl_message.setBackground(new java.awt.Color(255, 255, 255));
        jLbl_message.setFont(new java.awt.Font("メイリオ", 0, 18)); // NOI18N
        jLbl_message.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jLbl_message.setFocusable(false);
        jLbl_message.setMaximumSize(new java.awt.Dimension(1150, 70));
        jLbl_message.setMinimumSize(new java.awt.Dimension(1150, 70));
        jLbl_message.setOpaque(true);
        jLbl_message.setPreferredSize(new java.awt.Dimension(1150, 70));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        jPanel_Bottom.add(jLbl_message, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weighty = 0.5;
        gridBagConstraints.insets = new java.awt.Insets(0, 20, 2, 20);
        jPanel6.add(jPanel_Bottom, gridBagConstraints);

        jPanel_Bottom1.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)), "一時記憶一覧", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("メイリオ", 0, 18))); // NOI18N
        jPanel_Bottom1.setFont(new java.awt.Font("メイリオ", 0, 24)); // NOI18N
        jPanel_Bottom1.setMaximumSize(new java.awt.Dimension(800, 180));
        jPanel_Bottom1.setMinimumSize(new java.awt.Dimension(800, 180));
        jPanel_Bottom1.setPreferredSize(new java.awt.Dimension(800, 180));
        jPanel_Bottom1.setLayout(new java.awt.GridBagLayout());

        dataTable_.setFont(new java.awt.Font("メイリオ", 0, 18)); // NOI18N
        dataTable_.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        jScrollPane1.setViewportView(dataTable_);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        jPanel_Bottom1.add(jScrollPane1, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 20, 0, 20);
        jPanel6.add(jPanel_Bottom1, gridBagConstraints);

        jPanel_Right.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel_Right.setMaximumSize(new java.awt.Dimension(230, 170));
        jPanel_Right.setMinimumSize(new java.awt.Dimension(230, 170));
        jPanel_Right.setPreferredSize(new java.awt.Dimension(230, 170));
        jPanel_Right.setLayout(new java.awt.GridBagLayout());

        jLabel_gate_in.setBackground(new java.awt.Color(0, 51, 153));
        jLabel_gate_in.setFont(new java.awt.Font("メイリオ", 0, 18)); // NOI18N
        jLabel_gate_in.setForeground(new java.awt.Color(255, 255, 255));
        jLabel_gate_in.setText("　総重量(kg)");
        jLabel_gate_in.setMaximumSize(new java.awt.Dimension(200, 30));
        jLabel_gate_in.setMinimumSize(new java.awt.Dimension(200, 30));
        jLabel_gate_in.setOpaque(true);
        jLabel_gate_in.setPreferredSize(new java.awt.Dimension(200, 30));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        jPanel_Right.add(jLabel_gate_in, gridBagConstraints);

        jLbl_total_weight.setFont(new java.awt.Font("メイリオ", 0, 36)); // NOI18N
        jLbl_total_weight.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLbl_total_weight.setText("12345");
        jLbl_total_weight.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 3));
        jLbl_total_weight.setFocusable(false);
        jLbl_total_weight.setMaximumSize(new java.awt.Dimension(160, 45));
        jLbl_total_weight.setMinimumSize(new java.awt.Dimension(160, 45));
        jLbl_total_weight.setOpaque(true);
        jLbl_total_weight.setPreferredSize(new java.awt.Dimension(160, 45));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 5, 0);
        jPanel_Right.add(jLbl_total_weight, gridBagConstraints);

        jLabel_gate_out.setBackground(new java.awt.Color(0, 51, 153));
        jLabel_gate_out.setFont(new java.awt.Font("メイリオ", 0, 18)); // NOI18N
        jLabel_gate_out.setForeground(new java.awt.Color(255, 255, 255));
        jLabel_gate_out.setText("　風袋重量(㎏)");
        jLabel_gate_out.setMaximumSize(new java.awt.Dimension(200, 30));
        jLabel_gate_out.setMinimumSize(new java.awt.Dimension(200, 30));
        jLabel_gate_out.setOpaque(true);
        jLabel_gate_out.setPreferredSize(new java.awt.Dimension(200, 30));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        jPanel_Right.add(jLabel_gate_out, gridBagConstraints);

        jLbl_empty_weight.setFont(new java.awt.Font("メイリオ", 0, 36)); // NOI18N
        jLbl_empty_weight.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLbl_empty_weight.setText("12345");
        jLbl_empty_weight.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 3));
        jLbl_empty_weight.setFocusable(false);
        jLbl_empty_weight.setMaximumSize(new java.awt.Dimension(160, 45));
        jLbl_empty_weight.setMinimumSize(new java.awt.Dimension(160, 45));
        jLbl_empty_weight.setOpaque(true);
        jLbl_empty_weight.setPreferredSize(new java.awt.Dimension(160, 45));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 5, 0);
        jPanel_Right.add(jLbl_empty_weight, gridBagConstraints);

        jLabel_loading.setBackground(new java.awt.Color(0, 51, 153));
        jLabel_loading.setFont(new java.awt.Font("メイリオ", 0, 18)); // NOI18N
        jLabel_loading.setForeground(new java.awt.Color(255, 255, 255));
        jLabel_loading.setText("　正味重量(kg)");
        jLabel_loading.setMaximumSize(new java.awt.Dimension(200, 30));
        jLabel_loading.setMinimumSize(new java.awt.Dimension(200, 30));
        jLabel_loading.setOpaque(true);
        jLabel_loading.setPreferredSize(new java.awt.Dimension(200, 30));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.SOUTHWEST;
        jPanel_Right.add(jLabel_loading, gridBagConstraints);

        jLbl_net_weight.setBackground(new java.awt.Color(204, 255, 255));
        jLbl_net_weight.setFont(new java.awt.Font("メイリオ", 0, 36)); // NOI18N
        jLbl_net_weight.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLbl_net_weight.setText("12345");
        jLbl_net_weight.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 3));
        jLbl_net_weight.setFocusable(false);
        jLbl_net_weight.setMaximumSize(new java.awt.Dimension(160, 45));
        jLbl_net_weight.setMinimumSize(new java.awt.Dimension(160, 45));
        jLbl_net_weight.setOpaque(true);
        jLbl_net_weight.setPreferredSize(new java.awt.Dimension(160, 45));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 5, 0);
        jPanel_Right.add(jLbl_net_weight, gridBagConstraints);

        jLbl_juuten1.setBackground(new java.awt.Color(0, 51, 153));
        jLbl_juuten1.setFont(new java.awt.Font("メイリオ", 0, 18)); // NOI18N
        jLbl_juuten1.setForeground(new java.awt.Color(255, 255, 255));
        jLbl_juuten1.setText("　記憶重量(kg)");
        jLbl_juuten1.setToolTipText("");
        jLbl_juuten1.setMaximumSize(new java.awt.Dimension(200, 30));
        jLbl_juuten1.setMinimumSize(new java.awt.Dimension(200, 30));
        jLbl_juuten1.setOpaque(true);
        jLbl_juuten1.setPreferredSize(new java.awt.Dimension(200, 30));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.SOUTHWEST;
        jPanel_Right.add(jLbl_juuten1, gridBagConstraints);

        jLbl_juuten4.setBackground(new java.awt.Color(0, 51, 153));
        jLbl_juuten4.setFont(new java.awt.Font("メイリオ", 0, 18)); // NOI18N
        jLbl_juuten4.setForeground(new java.awt.Color(255, 255, 255));
        jLbl_juuten4.setText("　現在重量値(kg)");
        jLbl_juuten4.setMaximumSize(new java.awt.Dimension(200, 30));
        jLbl_juuten4.setMinimumSize(new java.awt.Dimension(200, 30));
        jLbl_juuten4.setOpaque(true);
        jLbl_juuten4.setPreferredSize(new java.awt.Dimension(200, 30));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        jPanel_Right.add(jLbl_juuten4, gridBagConstraints);

        jLbl_now_weight1.setBackground(new java.awt.Color(0, 255, 0));
        jLbl_now_weight1.setFont(new java.awt.Font("Let's go Digital", 0, 60)); // NOI18N
        jLbl_now_weight1.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLbl_now_weight1.setText("12345");
        jLbl_now_weight1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 3));
        jLbl_now_weight1.setFocusable(false);
        jLbl_now_weight1.setMaximumSize(new java.awt.Dimension(160, 65));
        jLbl_now_weight1.setMinimumSize(new java.awt.Dimension(160, 65));
        jLbl_now_weight1.setOpaque(true);
        jLbl_now_weight1.setPreferredSize(new java.awt.Dimension(160, 65));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 25, 0);
        jPanel_Right.add(jLbl_now_weight1, gridBagConstraints);

        jLbl_loading_weight.setFont(new java.awt.Font("メイリオ", 0, 36)); // NOI18N
        jLbl_loading_weight.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLbl_loading_weight.setText("123,456");
        jLbl_loading_weight.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 3));
        jLbl_loading_weight.setFocusable(false);
        jLbl_loading_weight.setMaximumSize(new java.awt.Dimension(160, 45));
        jLbl_loading_weight.setMinimumSize(new java.awt.Dimension(160, 45));
        jLbl_loading_weight.setOpaque(true);
        jLbl_loading_weight.setPreferredSize(new java.awt.Dimension(160, 45));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 5, 0);
        jPanel_Right.add(jLbl_loading_weight, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 20);
        jPanel6.add(jPanel_Right, gridBagConstraints);

        jPanel_Bottom10.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel_Bottom10.setMaximumSize(new java.awt.Dimension(800, 55));
        jPanel_Bottom10.setMinimumSize(new java.awt.Dimension(800, 55));
        jPanel_Bottom10.setPreferredSize(new java.awt.Dimension(800, 55));
        jPanel_Bottom10.setLayout(new java.awt.GridBagLayout());

        weight_btn_計量処理.setFont(new java.awt.Font("メイリオ", 0, 18)); // NOI18N
        weight_btn_計量処理.setText("計量処理");
        weight_btn_計量処理.setMargin(new java.awt.Insets(2, 2, 2, 2));
        weight_btn_計量処理.setMaximumSize(new java.awt.Dimension(145, 40));
        weight_btn_計量処理.setMinimumSize(new java.awt.Dimension(145, 55));
        weight_btn_計量処理.setPreferredSize(new java.awt.Dimension(145, 40));
        weight_btn_計量処理.setRequestFocusEnabled(false);
        weight_btn_計量処理.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                weight_btn_計量処理FocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                weight_btn_計量処理FocusLost(evt);
            }
        });
        weight_btn_計量処理.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                weight_btn_計量処理ActionPerformed(evt);
            }
        });
        weight_btn_計量処理.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                weight_btn_計量処理KeyPressed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        jPanel_Bottom10.add(weight_btn_計量処理, gridBagConstraints);

        weight_btn_登録処理.setFont(new java.awt.Font("メイリオ", 0, 18)); // NOI18N
        weight_btn_登録処理.setText("登録処理");
        weight_btn_登録処理.setMargin(new java.awt.Insets(2, 2, 2, 2));
        weight_btn_登録処理.setMaximumSize(new java.awt.Dimension(145, 40));
        weight_btn_登録処理.setMinimumSize(new java.awt.Dimension(145, 55));
        weight_btn_登録処理.setPreferredSize(new java.awt.Dimension(145, 40));
        weight_btn_登録処理.setRequestFocusEnabled(false);
        weight_btn_登録処理.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                weight_btn_登録処理ActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 6;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        jPanel_Bottom10.add(weight_btn_登録処理, gridBagConstraints);

        cancel_btn.setFont(new java.awt.Font("メイリオ", 0, 18)); // NOI18N
        cancel_btn.setText("キャンセル");
        cancel_btn.setFocusable(false);
        cancel_btn.setMaximumSize(new java.awt.Dimension(145, 40));
        cancel_btn.setMinimumSize(new java.awt.Dimension(145, 55));
        cancel_btn.setPreferredSize(new java.awt.Dimension(145, 40));
        cancel_btn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancel_btnActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 7;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        jPanel_Bottom10.add(cancel_btn, gridBagConstraints);

        weight_btn_2回計量.setFont(new java.awt.Font("メイリオ", 0, 18)); // NOI18N
        weight_btn_2回計量.setText("２回計量");
        weight_btn_2回計量.setMargin(new java.awt.Insets(2, 2, 2, 2));
        weight_btn_2回計量.setMaximumSize(new java.awt.Dimension(145, 40));
        weight_btn_2回計量.setMinimumSize(new java.awt.Dimension(145, 55));
        weight_btn_2回計量.setPreferredSize(new java.awt.Dimension(145, 40));
        weight_btn_2回計量.setRequestFocusEnabled(false);
        weight_btn_2回計量.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                weight_btn_2回計量ActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        jPanel_Bottom10.add(weight_btn_2回計量, gridBagConstraints);

        weight_btn3.setFont(new java.awt.Font("メイリオ", 0, 18)); // NOI18N
        weight_btn3.setEnabled(false);
        weight_btn3.setMargin(new java.awt.Insets(2, 2, 2, 2));
        weight_btn3.setMaximumSize(new java.awt.Dimension(145, 40));
        weight_btn3.setMinimumSize(new java.awt.Dimension(145, 55));
        weight_btn3.setPreferredSize(new java.awt.Dimension(145, 40));
        weight_btn3.setRequestFocusEnabled(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        jPanel_Bottom10.add(weight_btn3, gridBagConstraints);

        weight_btn_重量取込.setFont(new java.awt.Font("メイリオ", 0, 18)); // NOI18N
        weight_btn_重量取込.setText("重量取込");
        weight_btn_重量取込.setMargin(new java.awt.Insets(2, 2, 2, 2));
        weight_btn_重量取込.setMaximumSize(new java.awt.Dimension(145, 40));
        weight_btn_重量取込.setMinimumSize(new java.awt.Dimension(145, 55));
        weight_btn_重量取込.setPreferredSize(new java.awt.Dimension(145, 40));
        weight_btn_重量取込.setRequestFocusEnabled(false);
        weight_btn_重量取込.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                weight_btn_重量取込FocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                weight_btn_重量取込FocusLost(evt);
            }
        });
        weight_btn_重量取込.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                weight_btn_重量取込ActionPerformed(evt);
            }
        });
        weight_btn_重量取込.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                weight_btn_重量取込KeyPressed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        jPanel_Bottom10.add(weight_btn_重量取込, gridBagConstraints);

        weight_btn_風袋登録.setFont(new java.awt.Font("メイリオ", 0, 18)); // NOI18N
        weight_btn_風袋登録.setText("風袋登録");
        weight_btn_風袋登録.setMargin(new java.awt.Insets(2, 2, 2, 2));
        weight_btn_風袋登録.setMaximumSize(new java.awt.Dimension(145, 40));
        weight_btn_風袋登録.setMinimumSize(new java.awt.Dimension(145, 55));
        weight_btn_風袋登録.setPreferredSize(new java.awt.Dimension(145, 40));
        weight_btn_風袋登録.setRequestFocusEnabled(false);
        weight_btn_風袋登録.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                weight_btn_風袋登録ActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        jPanel_Bottom10.add(weight_btn_風袋登録, gridBagConstraints);

        weight_btn_重量初期化.setFont(new java.awt.Font("メイリオ", 0, 18)); // NOI18N
        weight_btn_重量初期化.setText("ゼロリセット");
        weight_btn_重量初期化.setMargin(new java.awt.Insets(2, 0, 2, 0));
        weight_btn_重量初期化.setMaximumSize(new java.awt.Dimension(145, 40));
        weight_btn_重量初期化.setMinimumSize(new java.awt.Dimension(145, 55));
        weight_btn_重量初期化.setPreferredSize(new java.awt.Dimension(145, 40));
        weight_btn_重量初期化.setRequestFocusEnabled(false);
        weight_btn_重量初期化.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                weight_btn_重量初期化ActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        jPanel_Bottom10.add(weight_btn_重量初期化, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(2, 20, 2, 20);
        jPanel6.add(jPanel_Bottom10, gridBagConstraints);

        jTabbedPane1.addTab("　計　　量　　画　　面　", jPanel6);

        jPanel7.setMaximumSize(new java.awt.Dimension(400, 100));
        jPanel7.setMinimumSize(new java.awt.Dimension(400, 100));
        jPanel7.setPreferredSize(new java.awt.Dimension(400, 100));
        jPanel7.setLayout(new java.awt.GridBagLayout());

        jPanel_Bottom3.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)), "契約確認", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("メイリオ", 0, 18))); // NOI18N
        jPanel_Bottom3.setFont(new java.awt.Font("MS UI Gothic", 0, 36)); // NOI18N
        jPanel_Bottom3.setMaximumSize(new java.awt.Dimension(300, 180));
        jPanel_Bottom3.setMinimumSize(new java.awt.Dimension(300, 180));
        jPanel_Bottom3.setPreferredSize(new java.awt.Dimension(300, 180));
        jPanel_Bottom3.setLayout(new java.awt.GridBagLayout());

        jScrollPane2.setMaximumSize(new java.awt.Dimension(300, 180));
        jScrollPane2.setMinimumSize(new java.awt.Dimension(300, 180));
        jScrollPane2.setPreferredSize(new java.awt.Dimension(300, 180));

        dataTable_1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        dataTable_1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                dataTable_1MouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(dataTable_1);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        jPanel_Bottom3.add(jScrollPane2, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 6;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(10, 20, 20, 0);
        jPanel7.add(jPanel_Bottom3, gridBagConstraints);

        jLabel17.setBackground(new java.awt.Color(0, 51, 153));
        jLabel17.setFont(new java.awt.Font("メイリオ", 0, 18)); // NOI18N
        jLabel17.setForeground(new java.awt.Color(255, 255, 255));
        jLabel17.setText("　契約先名称");
        jLabel17.setFocusable(false);
        jLabel17.setMaximumSize(new java.awt.Dimension(150, 30));
        jLabel17.setMinimumSize(new java.awt.Dimension(150, 30));
        jLabel17.setOpaque(true);
        jLabel17.setPreferredSize(new java.awt.Dimension(150, 30));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 5);
        jPanel7.add(jLabel17, gridBagConstraints);

        jLabel19.setFont(new java.awt.Font("メイリオ", 0, 18)); // NOI18N
        jLabel19.setText("半角ｶﾅ検索");
        jLabel19.setFocusable(false);
        jLabel19.setMaximumSize(new java.awt.Dimension(250, 30));
        jLabel19.setMinimumSize(new java.awt.Dimension(250, 30));
        jLabel19.setPreferredSize(new java.awt.Dimension(250, 30));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 5);
        jPanel7.add(jLabel19, gridBagConstraints);

        searchWordField1_.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        searchWordField1_.setMaximumSize(new java.awt.Dimension(150, 30));
        searchWordField1_.setMinimumSize(new java.awt.Dimension(150, 30));
        searchWordField1_.setPreferredSize(new java.awt.Dimension(150, 30));
        searchWordField1_.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                searchWordField1_FocusGained(evt);
            }
        });
        searchWordField1_.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                searchWordField1_KeyPressed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        jPanel7.add(searchWordField1_, gridBagConstraints);

        searchWordField2_.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        searchWordField2_.setMaximumSize(new java.awt.Dimension(150, 30));
        searchWordField2_.setMinimumSize(new java.awt.Dimension(150, 30));
        searchWordField2_.setPreferredSize(new java.awt.Dimension(150, 30));
        searchWordField2_.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                searchWordField2_FocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                searchWordField2_FocusLost(evt);
            }
        });
        searchWordField2_.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                searchWordField2_KeyPressed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        jPanel7.add(searchWordField2_, gridBagConstraints);

        btn絞込み.setFont(new java.awt.Font("メイリオ", 0, 18)); // NOI18N
        btn絞込み.setText("絞込み");
        btn絞込み.setMargin(new java.awt.Insets(2, 2, 2, 2));
        btn絞込み.setMaximumSize(new java.awt.Dimension(120, 35));
        btn絞込み.setMinimumSize(new java.awt.Dimension(120, 35));
        btn絞込み.setPreferredSize(new java.awt.Dimension(120, 35));
        btn絞込み.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn絞込みActionPerformed(evt);
            }
        });
        btn絞込み.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                btn絞込みKeyPressed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 6;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        jPanel7.add(btn絞込み, gridBagConstraints);

        jPanel8.setMaximumSize(new java.awt.Dimension(250, 35));
        jPanel8.setMinimumSize(new java.awt.Dimension(250, 35));
        jPanel8.setPreferredSize(new java.awt.Dimension(250, 35));
        jPanel8.setLayout(new java.awt.GridBagLayout());

        jScrollPane4.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)), "契約　品目", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("メイリオ", 0, 18))); // NOI18N
        jScrollPane4.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        jScrollPane4.setMaximumSize(new java.awt.Dimension(245, 500));
        jScrollPane4.setMinimumSize(new java.awt.Dimension(245, 500));
        jScrollPane4.setPreferredSize(new java.awt.Dimension(245, 500));

        jPanel13.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel13.setMaximumSize(new java.awt.Dimension(245, 1200));
        jPanel13.setMinimumSize(new java.awt.Dimension(245, 1200));
        jPanel13.setPreferredSize(new java.awt.Dimension(245, 1200));
        jPanel13.setLayout(new java.awt.GridBagLayout());

        goods_name01_.setEditable(false);
        goods_name01_.setBackground(new java.awt.Color(255, 255, 255));
        goods_name01_.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        goods_name01_.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        goods_name01_.setFocusable(false);
        goods_name01_.setMaximumSize(new java.awt.Dimension(230, 30));
        goods_name01_.setMinimumSize(new java.awt.Dimension(230, 30));
        goods_name01_.setPreferredSize(new java.awt.Dimension(230, 30));
        goods_name01_.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                goods_name01_MouseClicked(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.weightx = 0.9;
        jPanel13.add(goods_name01_, gridBagConstraints);

        goods_name02_.setEditable(false);
        goods_name02_.setBackground(new java.awt.Color(234, 234, 255));
        goods_name02_.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        goods_name02_.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        goods_name02_.setFocusable(false);
        goods_name02_.setMaximumSize(new java.awt.Dimension(230, 30));
        goods_name02_.setMinimumSize(new java.awt.Dimension(230, 30));
        goods_name02_.setPreferredSize(new java.awt.Dimension(230, 30));
        goods_name02_.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                goods_name02_MouseClicked(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.weightx = 0.9;
        jPanel13.add(goods_name02_, gridBagConstraints);

        goods_name03_.setEditable(false);
        goods_name03_.setBackground(new java.awt.Color(255, 255, 255));
        goods_name03_.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        goods_name03_.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        goods_name03_.setFocusable(false);
        goods_name03_.setMaximumSize(new java.awt.Dimension(230, 30));
        goods_name03_.setMinimumSize(new java.awt.Dimension(230, 30));
        goods_name03_.setPreferredSize(new java.awt.Dimension(230, 30));
        goods_name03_.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                goods_name03_MouseClicked(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.weightx = 0.9;
        jPanel13.add(goods_name03_, gridBagConstraints);

        goods_name04_.setEditable(false);
        goods_name04_.setBackground(new java.awt.Color(234, 234, 255));
        goods_name04_.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        goods_name04_.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        goods_name04_.setFocusable(false);
        goods_name04_.setMaximumSize(new java.awt.Dimension(230, 30));
        goods_name04_.setMinimumSize(new java.awt.Dimension(230, 30));
        goods_name04_.setPreferredSize(new java.awt.Dimension(230, 30));
        goods_name04_.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                goods_name04_MouseClicked(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.weightx = 0.9;
        jPanel13.add(goods_name04_, gridBagConstraints);

        goods_name05_.setEditable(false);
        goods_name05_.setBackground(new java.awt.Color(255, 255, 255));
        goods_name05_.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        goods_name05_.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        goods_name05_.setFocusable(false);
        goods_name05_.setMaximumSize(new java.awt.Dimension(230, 30));
        goods_name05_.setMinimumSize(new java.awt.Dimension(230, 30));
        goods_name05_.setPreferredSize(new java.awt.Dimension(230, 30));
        goods_name05_.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                goods_name05_MouseClicked(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.weightx = 0.9;
        jPanel13.add(goods_name05_, gridBagConstraints);

        goods_name06_.setEditable(false);
        goods_name06_.setBackground(new java.awt.Color(234, 234, 255));
        goods_name06_.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        goods_name06_.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        goods_name06_.setFocusable(false);
        goods_name06_.setMaximumSize(new java.awt.Dimension(230, 30));
        goods_name06_.setMinimumSize(new java.awt.Dimension(230, 30));
        goods_name06_.setPreferredSize(new java.awt.Dimension(230, 30));
        goods_name06_.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                goods_name06_MouseClicked(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.weightx = 0.9;
        jPanel13.add(goods_name06_, gridBagConstraints);

        goods_name07_.setEditable(false);
        goods_name07_.setBackground(new java.awt.Color(255, 255, 255));
        goods_name07_.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        goods_name07_.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        goods_name07_.setFocusable(false);
        goods_name07_.setMaximumSize(new java.awt.Dimension(230, 30));
        goods_name07_.setMinimumSize(new java.awt.Dimension(230, 30));
        goods_name07_.setPreferredSize(new java.awt.Dimension(230, 30));
        goods_name07_.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                goods_name07_MouseClicked(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.weightx = 0.9;
        jPanel13.add(goods_name07_, gridBagConstraints);

        goods_name08_.setEditable(false);
        goods_name08_.setBackground(new java.awt.Color(234, 234, 255));
        goods_name08_.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        goods_name08_.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        goods_name08_.setFocusable(false);
        goods_name08_.setMaximumSize(new java.awt.Dimension(230, 30));
        goods_name08_.setMinimumSize(new java.awt.Dimension(230, 30));
        goods_name08_.setPreferredSize(new java.awt.Dimension(230, 30));
        goods_name08_.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                goods_name08_MouseClicked(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.weightx = 0.9;
        jPanel13.add(goods_name08_, gridBagConstraints);

        goods_name09_.setEditable(false);
        goods_name09_.setBackground(new java.awt.Color(255, 255, 255));
        goods_name09_.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        goods_name09_.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        goods_name09_.setFocusable(false);
        goods_name09_.setMaximumSize(new java.awt.Dimension(230, 30));
        goods_name09_.setMinimumSize(new java.awt.Dimension(230, 30));
        goods_name09_.setPreferredSize(new java.awt.Dimension(230, 30));
        goods_name09_.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                goods_name09_MouseClicked(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.weightx = 0.9;
        jPanel13.add(goods_name09_, gridBagConstraints);

        goods_name10_.setEditable(false);
        goods_name10_.setBackground(new java.awt.Color(234, 234, 255));
        goods_name10_.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        goods_name10_.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        goods_name10_.setFocusable(false);
        goods_name10_.setMaximumSize(new java.awt.Dimension(230, 30));
        goods_name10_.setMinimumSize(new java.awt.Dimension(230, 30));
        goods_name10_.setPreferredSize(new java.awt.Dimension(230, 30));
        goods_name10_.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                goods_name10_MouseClicked(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.weightx = 0.9;
        jPanel13.add(goods_name10_, gridBagConstraints);

        goods_name11_.setEditable(false);
        goods_name11_.setBackground(new java.awt.Color(255, 255, 255));
        goods_name11_.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        goods_name11_.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        goods_name11_.setFocusable(false);
        goods_name11_.setMaximumSize(new java.awt.Dimension(230, 30));
        goods_name11_.setMinimumSize(new java.awt.Dimension(230, 30));
        goods_name11_.setPreferredSize(new java.awt.Dimension(230, 30));
        goods_name11_.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                goods_name11_MouseClicked(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.weightx = 0.9;
        jPanel13.add(goods_name11_, gridBagConstraints);

        goods_name12_.setEditable(false);
        goods_name12_.setBackground(new java.awt.Color(234, 234, 255));
        goods_name12_.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        goods_name12_.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        goods_name12_.setFocusable(false);
        goods_name12_.setMaximumSize(new java.awt.Dimension(230, 30));
        goods_name12_.setMinimumSize(new java.awt.Dimension(230, 30));
        goods_name12_.setPreferredSize(new java.awt.Dimension(230, 30));
        goods_name12_.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                goods_name12_MouseClicked(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 11;
        gridBagConstraints.weightx = 0.9;
        jPanel13.add(goods_name12_, gridBagConstraints);

        goods_name13_.setEditable(false);
        goods_name13_.setBackground(new java.awt.Color(255, 255, 255));
        goods_name13_.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        goods_name13_.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        goods_name13_.setFocusable(false);
        goods_name13_.setMaximumSize(new java.awt.Dimension(230, 30));
        goods_name13_.setMinimumSize(new java.awt.Dimension(230, 30));
        goods_name13_.setPreferredSize(new java.awt.Dimension(230, 30));
        goods_name13_.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                goods_name13_MouseClicked(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 12;
        gridBagConstraints.weightx = 0.9;
        jPanel13.add(goods_name13_, gridBagConstraints);

        goods_name14_.setEditable(false);
        goods_name14_.setBackground(new java.awt.Color(234, 234, 255));
        goods_name14_.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        goods_name14_.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        goods_name14_.setFocusable(false);
        goods_name14_.setMaximumSize(new java.awt.Dimension(230, 30));
        goods_name14_.setMinimumSize(new java.awt.Dimension(230, 30));
        goods_name14_.setPreferredSize(new java.awt.Dimension(230, 30));
        goods_name14_.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                goods_name14_MouseClicked(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 13;
        gridBagConstraints.weightx = 0.9;
        jPanel13.add(goods_name14_, gridBagConstraints);

        goods_name15_.setEditable(false);
        goods_name15_.setBackground(new java.awt.Color(255, 255, 255));
        goods_name15_.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        goods_name15_.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        goods_name15_.setFocusable(false);
        goods_name15_.setMaximumSize(new java.awt.Dimension(230, 30));
        goods_name15_.setMinimumSize(new java.awt.Dimension(230, 30));
        goods_name15_.setPreferredSize(new java.awt.Dimension(230, 30));
        goods_name15_.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                goods_name15_MouseClicked(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 14;
        gridBagConstraints.weightx = 0.9;
        jPanel13.add(goods_name15_, gridBagConstraints);

        goods_name16_.setEditable(false);
        goods_name16_.setBackground(new java.awt.Color(234, 234, 255));
        goods_name16_.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        goods_name16_.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        goods_name16_.setFocusable(false);
        goods_name16_.setMaximumSize(new java.awt.Dimension(230, 30));
        goods_name16_.setMinimumSize(new java.awt.Dimension(230, 30));
        goods_name16_.setPreferredSize(new java.awt.Dimension(230, 30));
        goods_name16_.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                goods_name16_MouseClicked(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 15;
        gridBagConstraints.weightx = 0.9;
        jPanel13.add(goods_name16_, gridBagConstraints);

        goods_name17_.setEditable(false);
        goods_name17_.setBackground(new java.awt.Color(255, 255, 255));
        goods_name17_.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        goods_name17_.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        goods_name17_.setFocusable(false);
        goods_name17_.setMaximumSize(new java.awt.Dimension(230, 30));
        goods_name17_.setMinimumSize(new java.awt.Dimension(230, 30));
        goods_name17_.setPreferredSize(new java.awt.Dimension(230, 30));
        goods_name17_.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                goods_name17_MouseClicked(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 16;
        gridBagConstraints.weightx = 0.9;
        jPanel13.add(goods_name17_, gridBagConstraints);

        goods_name18_.setEditable(false);
        goods_name18_.setBackground(new java.awt.Color(234, 234, 255));
        goods_name18_.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        goods_name18_.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        goods_name18_.setFocusable(false);
        goods_name18_.setMaximumSize(new java.awt.Dimension(230, 30));
        goods_name18_.setMinimumSize(new java.awt.Dimension(230, 30));
        goods_name18_.setPreferredSize(new java.awt.Dimension(230, 30));
        goods_name18_.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                goods_name18_MouseClicked(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 17;
        gridBagConstraints.weightx = 0.9;
        jPanel13.add(goods_name18_, gridBagConstraints);

        goods_name19_.setEditable(false);
        goods_name19_.setBackground(new java.awt.Color(255, 255, 255));
        goods_name19_.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        goods_name19_.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        goods_name19_.setFocusable(false);
        goods_name19_.setMaximumSize(new java.awt.Dimension(230, 30));
        goods_name19_.setMinimumSize(new java.awt.Dimension(230, 30));
        goods_name19_.setPreferredSize(new java.awt.Dimension(230, 30));
        goods_name19_.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                goods_name19_MouseClicked(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 18;
        gridBagConstraints.weightx = 0.9;
        jPanel13.add(goods_name19_, gridBagConstraints);

        goods_name20_.setEditable(false);
        goods_name20_.setBackground(new java.awt.Color(234, 234, 255));
        goods_name20_.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        goods_name20_.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        goods_name20_.setFocusable(false);
        goods_name20_.setMaximumSize(new java.awt.Dimension(230, 30));
        goods_name20_.setMinimumSize(new java.awt.Dimension(230, 30));
        goods_name20_.setPreferredSize(new java.awt.Dimension(230, 30));
        goods_name20_.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                goods_name20_MouseClicked(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 19;
        gridBagConstraints.weightx = 0.9;
        jPanel13.add(goods_name20_, gridBagConstraints);

        goods_name21_.setEditable(false);
        goods_name21_.setBackground(new java.awt.Color(255, 255, 255));
        goods_name21_.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        goods_name21_.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        goods_name21_.setFocusable(false);
        goods_name21_.setMaximumSize(new java.awt.Dimension(230, 30));
        goods_name21_.setMinimumSize(new java.awt.Dimension(230, 30));
        goods_name21_.setPreferredSize(new java.awt.Dimension(230, 30));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 20;
        gridBagConstraints.weightx = 0.9;
        jPanel13.add(goods_name21_, gridBagConstraints);

        goods_name22_.setEditable(false);
        goods_name22_.setBackground(new java.awt.Color(234, 234, 255));
        goods_name22_.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        goods_name22_.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        goods_name22_.setFocusable(false);
        goods_name22_.setMaximumSize(new java.awt.Dimension(230, 30));
        goods_name22_.setMinimumSize(new java.awt.Dimension(230, 30));
        goods_name22_.setPreferredSize(new java.awt.Dimension(230, 30));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 21;
        gridBagConstraints.weightx = 0.9;
        jPanel13.add(goods_name22_, gridBagConstraints);

        goods_name23_.setEditable(false);
        goods_name23_.setBackground(new java.awt.Color(255, 255, 255));
        goods_name23_.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        goods_name23_.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        goods_name23_.setFocusable(false);
        goods_name23_.setMaximumSize(new java.awt.Dimension(230, 30));
        goods_name23_.setMinimumSize(new java.awt.Dimension(230, 30));
        goods_name23_.setPreferredSize(new java.awt.Dimension(230, 30));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 22;
        gridBagConstraints.weightx = 0.9;
        jPanel13.add(goods_name23_, gridBagConstraints);

        goods_name24_.setEditable(false);
        goods_name24_.setBackground(new java.awt.Color(234, 234, 255));
        goods_name24_.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        goods_name24_.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        goods_name24_.setFocusable(false);
        goods_name24_.setMaximumSize(new java.awt.Dimension(230, 30));
        goods_name24_.setMinimumSize(new java.awt.Dimension(230, 30));
        goods_name24_.setPreferredSize(new java.awt.Dimension(230, 30));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 23;
        gridBagConstraints.weightx = 0.9;
        jPanel13.add(goods_name24_, gridBagConstraints);

        goods_name25_.setEditable(false);
        goods_name25_.setBackground(new java.awt.Color(255, 255, 255));
        goods_name25_.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        goods_name25_.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        goods_name25_.setFocusable(false);
        goods_name25_.setMaximumSize(new java.awt.Dimension(230, 30));
        goods_name25_.setMinimumSize(new java.awt.Dimension(230, 30));
        goods_name25_.setPreferredSize(new java.awt.Dimension(230, 30));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 24;
        gridBagConstraints.weightx = 0.9;
        jPanel13.add(goods_name25_, gridBagConstraints);

        goods_name26_.setEditable(false);
        goods_name26_.setBackground(new java.awt.Color(234, 234, 255));
        goods_name26_.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        goods_name26_.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        goods_name26_.setFocusable(false);
        goods_name26_.setMaximumSize(new java.awt.Dimension(230, 30));
        goods_name26_.setMinimumSize(new java.awt.Dimension(230, 30));
        goods_name26_.setPreferredSize(new java.awt.Dimension(230, 30));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 25;
        gridBagConstraints.weightx = 0.9;
        jPanel13.add(goods_name26_, gridBagConstraints);

        goods_name27_.setEditable(false);
        goods_name27_.setBackground(new java.awt.Color(255, 255, 255));
        goods_name27_.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        goods_name27_.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        goods_name27_.setFocusable(false);
        goods_name27_.setMaximumSize(new java.awt.Dimension(230, 30));
        goods_name27_.setMinimumSize(new java.awt.Dimension(230, 30));
        goods_name27_.setPreferredSize(new java.awt.Dimension(230, 30));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 26;
        gridBagConstraints.weightx = 0.9;
        jPanel13.add(goods_name27_, gridBagConstraints);

        goods_name28_.setEditable(false);
        goods_name28_.setBackground(new java.awt.Color(234, 234, 255));
        goods_name28_.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        goods_name28_.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        goods_name28_.setFocusable(false);
        goods_name28_.setMaximumSize(new java.awt.Dimension(230, 30));
        goods_name28_.setMinimumSize(new java.awt.Dimension(230, 30));
        goods_name28_.setPreferredSize(new java.awt.Dimension(230, 30));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 27;
        gridBagConstraints.weightx = 0.9;
        jPanel13.add(goods_name28_, gridBagConstraints);

        goods_name29_.setEditable(false);
        goods_name29_.setBackground(new java.awt.Color(255, 255, 255));
        goods_name29_.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        goods_name29_.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        goods_name29_.setFocusable(false);
        goods_name29_.setMaximumSize(new java.awt.Dimension(230, 30));
        goods_name29_.setMinimumSize(new java.awt.Dimension(230, 30));
        goods_name29_.setPreferredSize(new java.awt.Dimension(230, 30));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 28;
        gridBagConstraints.weightx = 0.9;
        jPanel13.add(goods_name29_, gridBagConstraints);

        goods_name30_.setEditable(false);
        goods_name30_.setBackground(new java.awt.Color(234, 234, 255));
        goods_name30_.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        goods_name30_.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        goods_name30_.setFocusable(false);
        goods_name30_.setMaximumSize(new java.awt.Dimension(230, 30));
        goods_name30_.setMinimumSize(new java.awt.Dimension(230, 30));
        goods_name30_.setPreferredSize(new java.awt.Dimension(230, 30));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 29;
        gridBagConstraints.weightx = 0.9;
        jPanel13.add(goods_name30_, gridBagConstraints);

        goods_name31_.setEditable(false);
        goods_name31_.setBackground(new java.awt.Color(255, 255, 255));
        goods_name31_.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        goods_name31_.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        goods_name31_.setFocusable(false);
        goods_name31_.setMaximumSize(new java.awt.Dimension(230, 30));
        goods_name31_.setMinimumSize(new java.awt.Dimension(230, 30));
        goods_name31_.setPreferredSize(new java.awt.Dimension(230, 30));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 30;
        gridBagConstraints.weightx = 0.9;
        jPanel13.add(goods_name31_, gridBagConstraints);

        goods_name32_.setEditable(false);
        goods_name32_.setBackground(new java.awt.Color(234, 234, 255));
        goods_name32_.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        goods_name32_.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        goods_name32_.setFocusable(false);
        goods_name32_.setMaximumSize(new java.awt.Dimension(230, 30));
        goods_name32_.setMinimumSize(new java.awt.Dimension(230, 30));
        goods_name32_.setPreferredSize(new java.awt.Dimension(230, 30));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 31;
        gridBagConstraints.weightx = 0.9;
        jPanel13.add(goods_name32_, gridBagConstraints);

        goods_name33_.setEditable(false);
        goods_name33_.setBackground(new java.awt.Color(255, 255, 255));
        goods_name33_.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        goods_name33_.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        goods_name33_.setFocusable(false);
        goods_name33_.setMaximumSize(new java.awt.Dimension(230, 30));
        goods_name33_.setMinimumSize(new java.awt.Dimension(230, 30));
        goods_name33_.setPreferredSize(new java.awt.Dimension(230, 30));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 32;
        gridBagConstraints.weightx = 0.9;
        jPanel13.add(goods_name33_, gridBagConstraints);

        goods_name34_.setEditable(false);
        goods_name34_.setBackground(new java.awt.Color(234, 234, 255));
        goods_name34_.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        goods_name34_.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        goods_name34_.setFocusable(false);
        goods_name34_.setMaximumSize(new java.awt.Dimension(230, 30));
        goods_name34_.setMinimumSize(new java.awt.Dimension(230, 30));
        goods_name34_.setPreferredSize(new java.awt.Dimension(230, 30));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 33;
        gridBagConstraints.weightx = 0.9;
        jPanel13.add(goods_name34_, gridBagConstraints);

        goods_name35_.setEditable(false);
        goods_name35_.setBackground(new java.awt.Color(255, 255, 255));
        goods_name35_.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        goods_name35_.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        goods_name35_.setFocusable(false);
        goods_name35_.setMaximumSize(new java.awt.Dimension(230, 30));
        goods_name35_.setMinimumSize(new java.awt.Dimension(230, 30));
        goods_name35_.setPreferredSize(new java.awt.Dimension(230, 30));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 34;
        gridBagConstraints.weightx = 0.9;
        jPanel13.add(goods_name35_, gridBagConstraints);

        goods_name36_.setEditable(false);
        goods_name36_.setBackground(new java.awt.Color(234, 234, 255));
        goods_name36_.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        goods_name36_.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        goods_name36_.setFocusable(false);
        goods_name36_.setMaximumSize(new java.awt.Dimension(230, 30));
        goods_name36_.setMinimumSize(new java.awt.Dimension(230, 30));
        goods_name36_.setPreferredSize(new java.awt.Dimension(230, 30));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 35;
        gridBagConstraints.weightx = 0.9;
        jPanel13.add(goods_name36_, gridBagConstraints);

        goods_name37_.setEditable(false);
        goods_name37_.setBackground(new java.awt.Color(255, 255, 255));
        goods_name37_.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        goods_name37_.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        goods_name37_.setFocusable(false);
        goods_name37_.setMaximumSize(new java.awt.Dimension(230, 30));
        goods_name37_.setMinimumSize(new java.awt.Dimension(230, 30));
        goods_name37_.setPreferredSize(new java.awt.Dimension(230, 30));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 36;
        gridBagConstraints.weightx = 0.9;
        jPanel13.add(goods_name37_, gridBagConstraints);

        goods_name38_.setEditable(false);
        goods_name38_.setBackground(new java.awt.Color(234, 234, 255));
        goods_name38_.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        goods_name38_.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        goods_name38_.setFocusable(false);
        goods_name38_.setMaximumSize(new java.awt.Dimension(230, 30));
        goods_name38_.setMinimumSize(new java.awt.Dimension(230, 30));
        goods_name38_.setPreferredSize(new java.awt.Dimension(230, 30));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 37;
        gridBagConstraints.weightx = 0.9;
        jPanel13.add(goods_name38_, gridBagConstraints);

        goods_name39_.setEditable(false);
        goods_name39_.setBackground(new java.awt.Color(255, 255, 255));
        goods_name39_.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        goods_name39_.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        goods_name39_.setFocusable(false);
        goods_name39_.setMaximumSize(new java.awt.Dimension(230, 30));
        goods_name39_.setMinimumSize(new java.awt.Dimension(230, 30));
        goods_name39_.setPreferredSize(new java.awt.Dimension(230, 30));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 38;
        gridBagConstraints.weightx = 0.9;
        jPanel13.add(goods_name39_, gridBagConstraints);

        goods_name40_.setEditable(false);
        goods_name40_.setBackground(new java.awt.Color(234, 234, 255));
        goods_name40_.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        goods_name40_.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        goods_name40_.setFocusable(false);
        goods_name40_.setMaximumSize(new java.awt.Dimension(230, 30));
        goods_name40_.setMinimumSize(new java.awt.Dimension(230, 30));
        goods_name40_.setPreferredSize(new java.awt.Dimension(230, 30));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 39;
        gridBagConstraints.weightx = 0.9;
        jPanel13.add(goods_name40_, gridBagConstraints);

        rb1_.setMaximumSize(new java.awt.Dimension(20, 20));
        rb1_.setMinimumSize(new java.awt.Dimension(20, 20));
        rb1_.setPreferredSize(new java.awt.Dimension(20, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        jPanel13.add(rb1_, gridBagConstraints);

        rb2_.setMaximumSize(new java.awt.Dimension(20, 20));
        rb2_.setMinimumSize(new java.awt.Dimension(20, 20));
        rb2_.setPreferredSize(new java.awt.Dimension(20, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        jPanel13.add(rb2_, gridBagConstraints);

        rb3_.setMaximumSize(new java.awt.Dimension(20, 20));
        rb3_.setMinimumSize(new java.awt.Dimension(20, 20));
        rb3_.setPreferredSize(new java.awt.Dimension(20, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        jPanel13.add(rb3_, gridBagConstraints);

        rb4_.setMaximumSize(new java.awt.Dimension(20, 20));
        rb4_.setMinimumSize(new java.awt.Dimension(20, 20));
        rb4_.setPreferredSize(new java.awt.Dimension(20, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        jPanel13.add(rb4_, gridBagConstraints);

        rb5_.setMaximumSize(new java.awt.Dimension(20, 20));
        rb5_.setMinimumSize(new java.awt.Dimension(20, 20));
        rb5_.setPreferredSize(new java.awt.Dimension(20, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        jPanel13.add(rb5_, gridBagConstraints);

        rb6_.setMaximumSize(new java.awt.Dimension(20, 20));
        rb6_.setMinimumSize(new java.awt.Dimension(20, 20));
        rb6_.setPreferredSize(new java.awt.Dimension(20, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        jPanel13.add(rb6_, gridBagConstraints);

        rb7_.setMaximumSize(new java.awt.Dimension(20, 20));
        rb7_.setMinimumSize(new java.awt.Dimension(20, 20));
        rb7_.setPreferredSize(new java.awt.Dimension(20, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        jPanel13.add(rb7_, gridBagConstraints);

        rb8_.setMaximumSize(new java.awt.Dimension(20, 20));
        rb8_.setMinimumSize(new java.awt.Dimension(20, 20));
        rb8_.setPreferredSize(new java.awt.Dimension(20, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        jPanel13.add(rb8_, gridBagConstraints);

        rb9_.setMaximumSize(new java.awt.Dimension(20, 20));
        rb9_.setMinimumSize(new java.awt.Dimension(20, 20));
        rb9_.setPreferredSize(new java.awt.Dimension(20, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        jPanel13.add(rb9_, gridBagConstraints);

        rb10_.setMaximumSize(new java.awt.Dimension(20, 20));
        rb10_.setMinimumSize(new java.awt.Dimension(20, 20));
        rb10_.setPreferredSize(new java.awt.Dimension(20, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        jPanel13.add(rb10_, gridBagConstraints);

        rb11_.setMaximumSize(new java.awt.Dimension(20, 20));
        rb11_.setMinimumSize(new java.awt.Dimension(20, 20));
        rb11_.setPreferredSize(new java.awt.Dimension(20, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        jPanel13.add(rb11_, gridBagConstraints);

        rb12_.setMaximumSize(new java.awt.Dimension(20, 20));
        rb12_.setMinimumSize(new java.awt.Dimension(20, 20));
        rb12_.setPreferredSize(new java.awt.Dimension(20, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 11;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        jPanel13.add(rb12_, gridBagConstraints);

        rb13_.setMaximumSize(new java.awt.Dimension(20, 20));
        rb13_.setMinimumSize(new java.awt.Dimension(20, 20));
        rb13_.setPreferredSize(new java.awt.Dimension(20, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 12;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        jPanel13.add(rb13_, gridBagConstraints);

        rb14_.setMaximumSize(new java.awt.Dimension(20, 20));
        rb14_.setMinimumSize(new java.awt.Dimension(20, 20));
        rb14_.setPreferredSize(new java.awt.Dimension(20, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 13;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        jPanel13.add(rb14_, gridBagConstraints);

        rb15_.setMaximumSize(new java.awt.Dimension(20, 20));
        rb15_.setMinimumSize(new java.awt.Dimension(20, 20));
        rb15_.setPreferredSize(new java.awt.Dimension(20, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 14;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        jPanel13.add(rb15_, gridBagConstraints);

        rb16_.setMaximumSize(new java.awt.Dimension(20, 20));
        rb16_.setMinimumSize(new java.awt.Dimension(20, 20));
        rb16_.setPreferredSize(new java.awt.Dimension(20, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 15;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        jPanel13.add(rb16_, gridBagConstraints);

        rb17_.setMaximumSize(new java.awt.Dimension(20, 20));
        rb17_.setMinimumSize(new java.awt.Dimension(20, 20));
        rb17_.setPreferredSize(new java.awt.Dimension(20, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 16;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        jPanel13.add(rb17_, gridBagConstraints);

        rb18_.setMaximumSize(new java.awt.Dimension(20, 20));
        rb18_.setMinimumSize(new java.awt.Dimension(20, 20));
        rb18_.setPreferredSize(new java.awt.Dimension(20, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 17;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        jPanel13.add(rb18_, gridBagConstraints);

        rb19_.setMaximumSize(new java.awt.Dimension(20, 20));
        rb19_.setMinimumSize(new java.awt.Dimension(20, 20));
        rb19_.setPreferredSize(new java.awt.Dimension(20, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 18;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        jPanel13.add(rb19_, gridBagConstraints);

        rb20_.setMaximumSize(new java.awt.Dimension(20, 20));
        rb20_.setMinimumSize(new java.awt.Dimension(20, 20));
        rb20_.setPreferredSize(new java.awt.Dimension(20, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 19;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        jPanel13.add(rb20_, gridBagConstraints);

        rb21_.setMaximumSize(new java.awt.Dimension(20, 20));
        rb21_.setMinimumSize(new java.awt.Dimension(20, 20));
        rb21_.setPreferredSize(new java.awt.Dimension(20, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 20;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        jPanel13.add(rb21_, gridBagConstraints);

        rb22_.setMaximumSize(new java.awt.Dimension(20, 20));
        rb22_.setMinimumSize(new java.awt.Dimension(20, 20));
        rb22_.setPreferredSize(new java.awt.Dimension(20, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 21;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        jPanel13.add(rb22_, gridBagConstraints);

        rb23_.setMaximumSize(new java.awt.Dimension(20, 20));
        rb23_.setMinimumSize(new java.awt.Dimension(20, 20));
        rb23_.setPreferredSize(new java.awt.Dimension(20, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 22;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        jPanel13.add(rb23_, gridBagConstraints);

        rb24_.setMaximumSize(new java.awt.Dimension(20, 20));
        rb24_.setMinimumSize(new java.awt.Dimension(20, 20));
        rb24_.setPreferredSize(new java.awt.Dimension(20, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 23;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        jPanel13.add(rb24_, gridBagConstraints);

        rb25_.setMaximumSize(new java.awt.Dimension(20, 20));
        rb25_.setMinimumSize(new java.awt.Dimension(20, 20));
        rb25_.setPreferredSize(new java.awt.Dimension(20, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 24;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        jPanel13.add(rb25_, gridBagConstraints);

        rb26_.setMaximumSize(new java.awt.Dimension(20, 20));
        rb26_.setMinimumSize(new java.awt.Dimension(20, 20));
        rb26_.setPreferredSize(new java.awt.Dimension(20, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 25;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        jPanel13.add(rb26_, gridBagConstraints);

        rb27_.setMaximumSize(new java.awt.Dimension(20, 20));
        rb27_.setMinimumSize(new java.awt.Dimension(20, 20));
        rb27_.setPreferredSize(new java.awt.Dimension(20, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 26;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        jPanel13.add(rb27_, gridBagConstraints);

        rb28_.setMaximumSize(new java.awt.Dimension(20, 20));
        rb28_.setMinimumSize(new java.awt.Dimension(20, 20));
        rb28_.setPreferredSize(new java.awt.Dimension(20, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 27;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        jPanel13.add(rb28_, gridBagConstraints);

        rb29_.setMaximumSize(new java.awt.Dimension(20, 20));
        rb29_.setMinimumSize(new java.awt.Dimension(20, 20));
        rb29_.setPreferredSize(new java.awt.Dimension(20, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 28;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        jPanel13.add(rb29_, gridBagConstraints);

        rb30_.setMaximumSize(new java.awt.Dimension(20, 20));
        rb30_.setMinimumSize(new java.awt.Dimension(20, 20));
        rb30_.setPreferredSize(new java.awt.Dimension(20, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 29;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        jPanel13.add(rb30_, gridBagConstraints);

        rb31_.setMaximumSize(new java.awt.Dimension(20, 20));
        rb31_.setMinimumSize(new java.awt.Dimension(20, 20));
        rb31_.setPreferredSize(new java.awt.Dimension(20, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 30;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        jPanel13.add(rb31_, gridBagConstraints);

        rb32_.setMaximumSize(new java.awt.Dimension(20, 20));
        rb32_.setMinimumSize(new java.awt.Dimension(20, 20));
        rb32_.setPreferredSize(new java.awt.Dimension(20, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 31;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        jPanel13.add(rb32_, gridBagConstraints);

        rb33_.setMaximumSize(new java.awt.Dimension(20, 20));
        rb33_.setMinimumSize(new java.awt.Dimension(20, 20));
        rb33_.setPreferredSize(new java.awt.Dimension(20, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 32;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        jPanel13.add(rb33_, gridBagConstraints);

        rb34_.setMaximumSize(new java.awt.Dimension(20, 20));
        rb34_.setMinimumSize(new java.awt.Dimension(20, 20));
        rb34_.setPreferredSize(new java.awt.Dimension(20, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 33;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        jPanel13.add(rb34_, gridBagConstraints);

        rb35_.setMaximumSize(new java.awt.Dimension(20, 20));
        rb35_.setMinimumSize(new java.awt.Dimension(20, 20));
        rb35_.setPreferredSize(new java.awt.Dimension(20, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 34;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        jPanel13.add(rb35_, gridBagConstraints);

        rb36_.setMaximumSize(new java.awt.Dimension(20, 20));
        rb36_.setMinimumSize(new java.awt.Dimension(20, 20));
        rb36_.setPreferredSize(new java.awt.Dimension(20, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 35;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        jPanel13.add(rb36_, gridBagConstraints);

        rb37_.setMaximumSize(new java.awt.Dimension(20, 20));
        rb37_.setMinimumSize(new java.awt.Dimension(20, 20));
        rb37_.setPreferredSize(new java.awt.Dimension(20, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 36;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        jPanel13.add(rb37_, gridBagConstraints);

        rb38_.setMaximumSize(new java.awt.Dimension(20, 20));
        rb38_.setMinimumSize(new java.awt.Dimension(20, 20));
        rb38_.setPreferredSize(new java.awt.Dimension(20, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 37;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        jPanel13.add(rb38_, gridBagConstraints);

        rb39_.setMaximumSize(new java.awt.Dimension(20, 20));
        rb39_.setMinimumSize(new java.awt.Dimension(20, 20));
        rb39_.setPreferredSize(new java.awt.Dimension(20, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 38;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        jPanel13.add(rb39_, gridBagConstraints);

        rb40_.setMaximumSize(new java.awt.Dimension(20, 20));
        rb40_.setMinimumSize(new java.awt.Dimension(20, 20));
        rb40_.setPreferredSize(new java.awt.Dimension(20, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 39;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        jPanel13.add(rb40_, gridBagConstraints);

        jScrollPane4.setViewportView(jPanel13);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(10, 0, 0, 0);
        jPanel8.add(jScrollPane4, gridBagConstraints);

        jScrollPane5.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)), "契約　運搬業者", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("メイリオ", 0, 18))); // NOI18N
        jScrollPane5.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        jScrollPane5.setMaximumSize(new java.awt.Dimension(245, 100));
        jScrollPane5.setMinimumSize(new java.awt.Dimension(245, 100));
        jScrollPane5.setPreferredSize(new java.awt.Dimension(245, 100));

        jPanel14.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel14.setMaximumSize(new java.awt.Dimension(240, 145));
        jPanel14.setMinimumSize(new java.awt.Dimension(240, 145));
        jPanel14.setPreferredSize(new java.awt.Dimension(240, 145));
        jPanel14.setLayout(new java.awt.GridBagLayout());

        trans_name01_.setEditable(false);
        trans_name01_.setBackground(new java.awt.Color(255, 255, 255));
        trans_name01_.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        trans_name01_.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        trans_name01_.setFocusable(false);
        trans_name01_.setMaximumSize(new java.awt.Dimension(200, 30));
        trans_name01_.setMinimumSize(new java.awt.Dimension(200, 30));
        trans_name01_.setPreferredSize(new java.awt.Dimension(200, 30));
        trans_name01_.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                trans_name01_MouseClicked(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.weightx = 0.9;
        jPanel14.add(trans_name01_, gridBagConstraints);

        trans_name02_.setEditable(false);
        trans_name02_.setBackground(new java.awt.Color(234, 234, 255));
        trans_name02_.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        trans_name02_.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        trans_name02_.setFocusable(false);
        trans_name02_.setMaximumSize(new java.awt.Dimension(200, 30));
        trans_name02_.setMinimumSize(new java.awt.Dimension(200, 30));
        trans_name02_.setPreferredSize(new java.awt.Dimension(200, 30));
        trans_name02_.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                trans_name02_MouseClicked(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.weightx = 0.9;
        jPanel14.add(trans_name02_, gridBagConstraints);

        trans_name03_.setEditable(false);
        trans_name03_.setBackground(new java.awt.Color(255, 255, 255));
        trans_name03_.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        trans_name03_.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        trans_name03_.setFocusable(false);
        trans_name03_.setMaximumSize(new java.awt.Dimension(200, 30));
        trans_name03_.setMinimumSize(new java.awt.Dimension(200, 30));
        trans_name03_.setPreferredSize(new java.awt.Dimension(200, 30));
        trans_name03_.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                trans_name03_MouseClicked(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.weightx = 0.9;
        jPanel14.add(trans_name03_, gridBagConstraints);

        trans_name04_.setEditable(false);
        trans_name04_.setBackground(new java.awt.Color(234, 234, 255));
        trans_name04_.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        trans_name04_.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        trans_name04_.setFocusable(false);
        trans_name04_.setMaximumSize(new java.awt.Dimension(200, 30));
        trans_name04_.setMinimumSize(new java.awt.Dimension(200, 30));
        trans_name04_.setPreferredSize(new java.awt.Dimension(200, 30));
        trans_name04_.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                trans_name04_MouseClicked(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.weightx = 0.9;
        jPanel14.add(trans_name04_, gridBagConstraints);

        trans_name05_.setEditable(false);
        trans_name05_.setBackground(new java.awt.Color(255, 255, 255));
        trans_name05_.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        trans_name05_.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        trans_name05_.setFocusable(false);
        trans_name05_.setMaximumSize(new java.awt.Dimension(200, 30));
        trans_name05_.setMinimumSize(new java.awt.Dimension(200, 30));
        trans_name05_.setPreferredSize(new java.awt.Dimension(200, 30));
        trans_name05_.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                trans_name05_MouseClicked(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.weightx = 0.9;
        jPanel14.add(trans_name05_, gridBagConstraints);

        trb1_.setMaximumSize(new java.awt.Dimension(20, 20));
        trb1_.setMinimumSize(new java.awt.Dimension(20, 20));
        trb1_.setPreferredSize(new java.awt.Dimension(20, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 0, 0);
        jPanel14.add(trb1_, gridBagConstraints);

        trb2_.setMaximumSize(new java.awt.Dimension(20, 20));
        trb2_.setMinimumSize(new java.awt.Dimension(20, 20));
        trb2_.setPreferredSize(new java.awt.Dimension(20, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 0, 0);
        jPanel14.add(trb2_, gridBagConstraints);

        trb3_.setMaximumSize(new java.awt.Dimension(20, 20));
        trb3_.setMinimumSize(new java.awt.Dimension(20, 20));
        trb3_.setPreferredSize(new java.awt.Dimension(20, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 0, 0);
        jPanel14.add(trb3_, gridBagConstraints);

        trb4_.setMaximumSize(new java.awt.Dimension(20, 20));
        trb4_.setMinimumSize(new java.awt.Dimension(20, 20));
        trb4_.setPreferredSize(new java.awt.Dimension(20, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 0, 0);
        jPanel14.add(trb4_, gridBagConstraints);

        trb5_.setMaximumSize(new java.awt.Dimension(20, 20));
        trb5_.setMinimumSize(new java.awt.Dimension(20, 20));
        trb5_.setPreferredSize(new java.awt.Dimension(20, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 0, 0);
        jPanel14.add(trb5_, gridBagConstraints);

        jScrollPane5.setViewportView(jPanel14);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 0, 0, 0);
        jPanel8.add(jScrollPane5, gridBagConstraints);

        jScrollPane6.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)), "契約　処分業者（確認用）", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("メイリオ", 0, 18))); // NOI18N
        jScrollPane6.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        jScrollPane6.setMaximumSize(new java.awt.Dimension(245, 100));
        jScrollPane6.setMinimumSize(new java.awt.Dimension(245, 100));
        jScrollPane6.setPreferredSize(new java.awt.Dimension(245, 100));

        jPanel15.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel15.setMaximumSize(new java.awt.Dimension(240, 145));
        jPanel15.setMinimumSize(new java.awt.Dimension(240, 145));
        jPanel15.setPreferredSize(new java.awt.Dimension(240, 145));
        jPanel15.setLayout(new java.awt.GridBagLayout());

        cont_name01_.setEditable(false);
        cont_name01_.setBackground(new java.awt.Color(255, 255, 255));
        cont_name01_.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        cont_name01_.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        cont_name01_.setFocusable(false);
        cont_name01_.setMaximumSize(new java.awt.Dimension(200, 30));
        cont_name01_.setMinimumSize(new java.awt.Dimension(200, 30));
        cont_name01_.setPreferredSize(new java.awt.Dimension(200, 30));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.weightx = 0.9;
        jPanel15.add(cont_name01_, gridBagConstraints);

        cont_name02_.setEditable(false);
        cont_name02_.setBackground(new java.awt.Color(234, 234, 255));
        cont_name02_.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        cont_name02_.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        cont_name02_.setFocusable(false);
        cont_name02_.setMaximumSize(new java.awt.Dimension(200, 30));
        cont_name02_.setMinimumSize(new java.awt.Dimension(200, 30));
        cont_name02_.setPreferredSize(new java.awt.Dimension(200, 30));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.weightx = 0.9;
        jPanel15.add(cont_name02_, gridBagConstraints);

        cont_name03_.setEditable(false);
        cont_name03_.setBackground(new java.awt.Color(255, 255, 255));
        cont_name03_.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        cont_name03_.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        cont_name03_.setFocusable(false);
        cont_name03_.setMaximumSize(new java.awt.Dimension(200, 30));
        cont_name03_.setMinimumSize(new java.awt.Dimension(200, 30));
        cont_name03_.setPreferredSize(new java.awt.Dimension(200, 30));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.weightx = 0.9;
        jPanel15.add(cont_name03_, gridBagConstraints);

        cont_name04_.setEditable(false);
        cont_name04_.setBackground(new java.awt.Color(234, 234, 255));
        cont_name04_.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        cont_name04_.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        cont_name04_.setFocusable(false);
        cont_name04_.setMaximumSize(new java.awt.Dimension(200, 30));
        cont_name04_.setMinimumSize(new java.awt.Dimension(200, 30));
        cont_name04_.setPreferredSize(new java.awt.Dimension(200, 30));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.weightx = 0.9;
        jPanel15.add(cont_name04_, gridBagConstraints);

        jScrollPane6.setViewportView(jPanel15);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 0, 20, 0);
        jPanel8.add(jScrollPane6, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 6;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 20);
        jPanel7.add(jPanel8, gridBagConstraints);

        jButton8.setFont(new java.awt.Font("メイリオ", 0, 18)); // NOI18N
        jButton8.setText("計量画面");
        jButton8.setMargin(new java.awt.Insets(2, 2, 2, 2));
        jButton8.setMaximumSize(new java.awt.Dimension(120, 35));
        jButton8.setMinimumSize(new java.awt.Dimension(120, 35));
        jButton8.setPreferredSize(new java.awt.Dimension(120, 35));
        jButton8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton8ActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 7;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        jPanel7.add(jButton8, gridBagConstraints);

        jLabel27.setBackground(new java.awt.Color(0, 51, 153));
        jLabel27.setFont(new java.awt.Font("メイリオ", 0, 18)); // NOI18N
        jLabel27.setForeground(new java.awt.Color(255, 255, 255));
        jLabel27.setText("　現場名称");
        jLabel27.setFocusable(false);
        jLabel27.setMaximumSize(new java.awt.Dimension(150, 30));
        jLabel27.setMinimumSize(new java.awt.Dimension(150, 30));
        jLabel27.setOpaque(true);
        jLabel27.setPreferredSize(new java.awt.Dimension(150, 30));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 5);
        jPanel7.add(jLabel27, gridBagConstraints);

        jLabel30.setFont(new java.awt.Font("メイリオ", 0, 18)); // NOI18N
        jLabel30.setText("半角ｶﾅ検索");
        jLabel30.setFocusable(false);
        jLabel30.setMaximumSize(new java.awt.Dimension(150, 30));
        jLabel30.setMinimumSize(new java.awt.Dimension(150, 30));
        jLabel30.setPreferredSize(new java.awt.Dimension(150, 30));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 5);
        jPanel7.add(jLabel30, gridBagConstraints);

        jTabbedPane1.addTab("　契　約　内　容　確　認　", jPanel7);

        jPanel12.setMaximumSize(new java.awt.Dimension(400, 100));
        jPanel12.setMinimumSize(new java.awt.Dimension(400, 100));
        jPanel12.setPreferredSize(new java.awt.Dimension(400, 100));
        jPanel12.setLayout(new java.awt.GridBagLayout());

        jPanel_Bottom4.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)), "契約状況確認", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("メイリオ", 0, 18))); // NOI18N
        jPanel_Bottom4.setFont(new java.awt.Font("MS UI Gothic", 0, 36)); // NOI18N
        jPanel_Bottom4.setMaximumSize(new java.awt.Dimension(300, 180));
        jPanel_Bottom4.setMinimumSize(new java.awt.Dimension(300, 180));
        jPanel_Bottom4.setPreferredSize(new java.awt.Dimension(300, 180));
        jPanel_Bottom4.setLayout(new java.awt.GridBagLayout());

        jScrollPane3.setMaximumSize(new java.awt.Dimension(300, 180));
        jScrollPane3.setMinimumSize(new java.awt.Dimension(300, 180));
        jScrollPane3.setPreferredSize(new java.awt.Dimension(300, 180));

        dataTable_2.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        dataTable_2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                dataTable_2MouseClicked(evt);
            }
        });
        jScrollPane3.setViewportView(dataTable_2);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        jPanel_Bottom4.add(jScrollPane3, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 7;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(10, 20, 20, 20);
        jPanel12.add(jPanel_Bottom4, gridBagConstraints);

        searchWordField1_1.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        searchWordField1_1.setMaximumSize(new java.awt.Dimension(150, 30));
        searchWordField1_1.setMinimumSize(new java.awt.Dimension(150, 30));
        searchWordField1_1.setPreferredSize(new java.awt.Dimension(150, 30));
        searchWordField1_1.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                searchWordField1_1FocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                searchWordField1_1FocusLost(evt);
            }
        });
        searchWordField1_1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                searchWordField1_1KeyPressed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        jPanel12.add(searchWordField1_1, gridBagConstraints);

        searchWordField2_1.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        searchWordField2_1.setMaximumSize(new java.awt.Dimension(150, 30));
        searchWordField2_1.setMinimumSize(new java.awt.Dimension(150, 30));
        searchWordField2_1.setPreferredSize(new java.awt.Dimension(150, 30));
        searchWordField2_1.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                searchWordField2_1FocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                searchWordField2_1FocusLost(evt);
            }
        });
        searchWordField2_1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                searchWordField2_1KeyPressed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        jPanel12.add(searchWordField2_1, gridBagConstraints);

        btn絞込み1.setFont(new java.awt.Font("メイリオ", 0, 18)); // NOI18N
        btn絞込み1.setText("絞込み");
        btn絞込み1.setMaximumSize(new java.awt.Dimension(120, 35));
        btn絞込み1.setMinimumSize(new java.awt.Dimension(120, 35));
        btn絞込み1.setPreferredSize(new java.awt.Dimension(120, 35));
        btn絞込み1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn絞込み1ActionPerformed(evt);
            }
        });
        btn絞込み1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                btn絞込み1KeyPressed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 6;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        jPanel12.add(btn絞込み1, gridBagConstraints);

        jPanel9.setMinimumSize(new java.awt.Dimension(50, 25));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 6;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        jPanel12.add(jPanel9, gridBagConstraints);

        jLabel31.setBackground(new java.awt.Color(0, 51, 153));
        jLabel31.setFont(new java.awt.Font("メイリオ", 0, 18)); // NOI18N
        jLabel31.setForeground(new java.awt.Color(255, 255, 255));
        jLabel31.setText("　契約先名称");
        jLabel31.setFocusable(false);
        jLabel31.setMaximumSize(new java.awt.Dimension(150, 30));
        jLabel31.setMinimumSize(new java.awt.Dimension(150, 30));
        jLabel31.setOpaque(true);
        jLabel31.setPreferredSize(new java.awt.Dimension(150, 30));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 5);
        jPanel12.add(jLabel31, gridBagConstraints);

        jLabel32.setFont(new java.awt.Font("メイリオ", 0, 18)); // NOI18N
        jLabel32.setText("半角ｶﾅ検索");
        jLabel32.setFocusable(false);
        jLabel32.setMaximumSize(new java.awt.Dimension(150, 30));
        jLabel32.setMinimumSize(new java.awt.Dimension(150, 30));
        jLabel32.setPreferredSize(new java.awt.Dimension(150, 30));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 5);
        jPanel12.add(jLabel32, gridBagConstraints);

        jLabel33.setBackground(new java.awt.Color(0, 51, 153));
        jLabel33.setFont(new java.awt.Font("メイリオ", 0, 18)); // NOI18N
        jLabel33.setForeground(new java.awt.Color(255, 255, 255));
        jLabel33.setText("　現場名称");
        jLabel33.setFocusable(false);
        jLabel33.setMaximumSize(new java.awt.Dimension(150, 30));
        jLabel33.setMinimumSize(new java.awt.Dimension(150, 30));
        jLabel33.setOpaque(true);
        jLabel33.setPreferredSize(new java.awt.Dimension(150, 30));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 5);
        jPanel12.add(jLabel33, gridBagConstraints);

        jTabbedPane1.addTab("　契　約　期　間　確　認　", jPanel12);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        jPanel5.add(jTabbedPane1, gridBagConstraints);

        add(jPanel5);
    }// </editor-fold>//GEN-END:initComponents

    public void stretch(javax.swing.JLabel lbl, String txt) {
        String dmy = txt;
        //フォントメトリックスを取得
        FontMetrics fm = lbl.getFontMetrics(lbl.getFont());
        //ラベルの幅を取得
        int lbl_w = lbl.getWidth();
        //文字数を取得
        int len = txt.length();

        //文字幅がラベル幅を超える場合繰り返し
        while (fm.stringWidth(dmy) >= lbl_w && len > 0) {
            //文字数を1文字減らす
            dmy = txt.substring(0, len--);
            //ラベルからはみ出した場合、末尾に...を付ける
            dmy += "...";
        }
        lbl.setText(dmy);
    }

    private void jLabel_TitleMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel_TitleMousePressed
        // TODO add your handling code here:
        long now_time = System.currentTimeMillis();
        long gap_time = now_time - mLastTitleClickTime;
        if (gap_time < 500) {
            mTitleClickCount++;
        } else {
            mTitleClickCount = 1;
        }

        if (mTitleClickCount >= 5) {
            String NowValue_ = JOptionPane.showInputDialog(this, "重量値を入力してください。", null);
            if (NowValue_ != null) {
                if (Integer.valueOf(NowValue_) > 200) {
                    Status.WeightReady_ = false;
                    OpaqueFlag_ = true;
                } else {
                    Status.WeightReady_ = true;
                    OpaqueFlag_ = false;
                }
                mStatus.SetData(Status.INDEX_WEIGHT, NowValue_);
            }
        }
        mLastTitleClickTime = now_time;
    }//GEN-LAST:event_jLabel_TitleMousePressed

    private void car_id_KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_car_id_KeyPressed
        // TODO add your handling code here:
        // Enterでフォーカスを移動する
        int mod = evt.getModifiersEx();
        if (evt.getKeyCode() == KeyEvent.VK_ENTER & ((mod & InputEvent.SHIFT_DOWN_MASK) != 0)) {
            evt.consume();
            unit_value_tex.requestFocus();
        } else if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            int len = (((JTextFieldKeiryo) evt.getSource()).getText()).length();
            if (len > 0) {
                String s0 = ((JTextFieldKeiryo) evt.getSource()).getText().substring(0, 1);
                if (!((JTextFieldKeiryo) evt.getSource()).getText().equals("0")
                         && len < 7 && (!s0.equals("T") && !s0.equals("G"))) {
                    try {
                        getCarID();
                        car_no_.requestFocus();
                        evt.consume();
                    } catch (NumberFormatException ne) {
                        evt.consume();
                        car_id_.setText("0");
                        car_id_.requestFocus();
                        car_id_.selectAll();
                    }

                } else if (len == 8 || s0.equals("T") || s0.equals("G")) {
                    ((JTextFieldKeiryo) evt.getSource()).setText("0");
                    ((JTextFieldKeiryo) evt.getSource()).selectAll();
                    ((JTextFieldKeiryo) evt.getSource()).requestFocus();

                }
            } else {
                car_id_.setText("0");
                car_id_.requestFocus();
                car_id_.selectAll();
            }
        } else if (evt.getKeyCode() == KeyEvent.VK_F1) {
            //検索
            SelectCarAction();

        }
    }//GEN-LAST:event_car_id_KeyPressed
    private void ent_no_texKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_ent_no_texKeyPressed
        // TODO add your handling code here:
        // Enterでフォーカスを移動する
        int mod = evt.getModifiersEx();
        if (evt.getKeyCode() == KeyEvent.VK_ENTER & ((mod & InputEvent.SHIFT_DOWN_MASK) != 0)) {
            ent_no_tex.transferFocus();
            evt.consume();
            bill_no_tex.requestFocus();
        } else if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            int len = (((JTextFieldKeiryo) evt.getSource()).getText()).length();
            if (len > 0) {
                String s0 = ((JTextFieldKeiryo) evt.getSource()).getText().substring(0, 1);
                if (len == 8) {
                    //0000-000
                    //排出業者コード4桁 + "-" + 排出現場コード3桁
                    String i排出業者コード = ((JTextFieldKeiryo) evt.getSource()).getText().substring(0, 4);
                    String i排出現場コード = ((JTextFieldKeiryo) evt.getSource()).getText().substring(5, 8);
                    mStatus.SetData(Status.INDEX_ENT, i排出業者コード);
                    mStatus.SetData(Status.INDEX_OFFICE, i排出現場コード);
                } else if (len == 11) {
                    mStatus.SetData(Status.INDEX_SANGYO, ((JTextFieldKeiryo) evt.getSource()).getText());
                } else if (s0.equals("T")) {
                    mStatus.SetData(Status.INDEX_TRANS, ((JTextFieldKeiryo) evt.getSource()).getText().substring(1));
                } else if (s0.equals("G")) {
                    mStatus.SetData(Status.INDEX_KIND, ((JTextFieldKeiryo) evt.getSource()).getText().substring(1));
                }
            } else {
                ((JTextFieldKeiryo) evt.getSource()).setText("0");
            }
            ent_no_tex.transferFocus();
            evt.consume();
        } else if (evt.getKeyCode() == KeyEvent.VK_F1) {
            //検索
            SelectEntAction();
        }
    }//GEN-LAST:event_ent_no_texKeyPressed

    private void trans_no_texKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_trans_no_texKeyPressed
        // TODO add your handling code here:
        // Enterでフォーカスを移動する
        int mod = evt.getModifiersEx();
        if (evt.getKeyCode() == KeyEvent.VK_ENTER & ((mod & InputEvent.SHIFT_DOWN_MASK) != 0)) {
            trans_no_tex.transferFocus();
            evt.consume();
            ent_no_tex.requestFocus();
        } else if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            int len = (((JTextFieldKeiryo) evt.getSource()).getText()).length();
            if (len > 0) {
                String s0 = ((JTextFieldKeiryo) evt.getSource()).getText().substring(0, 1);
                if (len == 8) {
                    //排出業者コード4桁 + "-" + 排出現場コード3桁
                    String i排出業者コード = ((JTextFieldKeiryo) evt.getSource()).getText().substring(0, 4);
                    String i排出現場コード = ((JTextFieldKeiryo) evt.getSource()).getText().substring(5, 8);
                    mStatus.SetData(Status.INDEX_ENT, i排出業者コード);
                    mStatus.SetData(Status.INDEX_OFFICE, i排出現場コード);
                } else if (len == 11) {
                    mStatus.SetData(Status.INDEX_SANGYO, ((JTextFieldKeiryo) evt.getSource()).getText());
                } else if (s0.equals("T")) {
                    mStatus.SetData(Status.INDEX_TRANS, ((JTextFieldKeiryo) evt.getSource()).getText().substring(1));
                } else if (s0.equals("G")) {
                    mStatus.SetData(Status.INDEX_KIND, ((JTextFieldKeiryo) evt.getSource()).getText().substring(1));
                }
            } else {
                ((JTextFieldKeiryo) evt.getSource()).setText("0");
            }
            trans_no_tex.transferFocus();
            evt.consume();
        } else if (evt.getKeyCode() == KeyEvent.VK_F1) {
            //検索
            SelectTransporterAction();
        }
    }//GEN-LAST:event_trans_no_texKeyPressed

    private void kind_no_texKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_kind_no_texKeyPressed
        // TODO add your handling code here:
        // Enterでフォーカスを移動する
        int mod = evt.getModifiersEx();
        if (evt.getKeyCode() == KeyEvent.VK_ENTER & ((mod & InputEvent.SHIFT_DOWN_MASK) != 0)) {
            kind_no_tex.transferFocus();
            evt.consume();
            trans_no_tex.requestFocus();
        } else if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            int len = (((JTextFieldKeiryo) evt.getSource()).getText()).length();
            if (len > 0) {
                String s0 = ((JTextFieldKeiryo) evt.getSource()).getText().substring(0, 1);
                if (len == 8) {
                    //排出業者コード4桁 + "-" + 排出現場コード3桁
                    String i排出業者コード = ((JTextFieldKeiryo) evt.getSource()).getText().substring(0, 4);
                    String i排出現場コード = ((JTextFieldKeiryo) evt.getSource()).getText().substring(5, 8);
                    mStatus.SetData(Status.INDEX_ENT, i排出業者コード);
                    mStatus.SetData(Status.INDEX_OFFICE, i排出現場コード);
                } else if (len == 11) {
                    mStatus.SetData(Status.INDEX_SANGYO, ((JTextFieldKeiryo) evt.getSource()).getText());
                } else if (s0.equals("T")) {
                    mStatus.SetData(Status.INDEX_TRANS, ((JTextFieldKeiryo) evt.getSource()).getText().substring(1));
                } else if (s0.equals("G")) {
                    mStatus.SetData(Status.INDEX_KIND, ((JTextFieldKeiryo) evt.getSource()).getText().substring(1));
                }
            } else {
                ((JTextFieldKeiryo) evt.getSource()).setText("0");
            }
            kind_no_tex.transferFocus();
            evt.consume();
        } else if (evt.getKeyCode() == KeyEvent.VK_F1) {
            //検索
            SelectGoodsAction();
        }
    }//GEN-LAST:event_kind_no_texKeyPressed

    private void office_no_texKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_office_no_texKeyPressed
        // TODO add your handling code here:
        // Enterでフォーカスを移動する
        int mod = evt.getModifiersEx();
        if (evt.getKeyCode() == KeyEvent.VK_ENTER & ((mod & InputEvent.SHIFT_DOWN_MASK) != 0)) {
            office_no_tex.transferFocus();
            evt.consume();
            kind_no_tex.requestFocus();
        } else if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            int len = (((JTextFieldKeiryo) evt.getSource()).getText()).length();
            if (len > 0) {
                String s0 = ((JTextFieldKeiryo) evt.getSource()).getText().substring(0, 1);
                if (len == 8) {
                    //排出業者コード4桁 + "-" + 排出現場コード3桁
                    String i排出業者コード = ((JTextFieldKeiryo) evt.getSource()).getText().substring(0, 4);
                    String i排出現場コード = ((JTextFieldKeiryo) evt.getSource()).getText().substring(5, 8);
                    mStatus.SetData(Status.INDEX_ENT, i排出業者コード);
                    mStatus.SetData(Status.INDEX_OFFICE, i排出現場コード);
                } else if (len == 11) {
                    mStatus.SetData(Status.INDEX_SANGYO, ((JTextFieldKeiryo) evt.getSource()).getText());
                } else if (s0.equals("T")) {
                    mStatus.SetData(Status.INDEX_TRANS, ((JTextFieldKeiryo) evt.getSource()).getText().substring(1));
                } else if (s0.equals("G")) {
                    mStatus.SetData(Status.INDEX_KIND, ((JTextFieldKeiryo) evt.getSource()).getText().substring(1));
                }
            } else {
                ((JTextFieldKeiryo) evt.getSource()).setText("0");
            }
            office_no_tex.transferFocus();
            evt.consume();
        } else if (evt.getKeyCode() == KeyEvent.VK_F1) {
            //検索
            SelectOfficeAction();
        }
    }//GEN-LAST:event_office_no_texKeyPressed

    private void cont_no_texKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cont_no_texKeyPressed
        // TODO add your handling code here:
        // Enterでフォーカスを移動する
        int mod = evt.getModifiersEx();
        if (evt.getKeyCode() == KeyEvent.VK_ENTER & ((mod & InputEvent.SHIFT_DOWN_MASK) != 0)) {
            cont_no_tex.transferFocus();
            evt.consume();
            office_no_tex.requestFocus();
        } else if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            int len = (((JTextFieldKeiryo) evt.getSource()).getText()).length();
            if (len > 0) {
                String s0 = ((JTextFieldKeiryo) evt.getSource()).getText().substring(0, 1);
                if (len == 8) {
                    //排出業者コード4桁 + "-" + 排出現場コード3桁
                    String i排出業者コード = ((JTextFieldKeiryo) evt.getSource()).getText().substring(0, 4);
                    String i排出現場コード = ((JTextFieldKeiryo) evt.getSource()).getText().substring(5, 8);
                    mStatus.SetData(Status.INDEX_ENT, i排出業者コード);
                    mStatus.SetData(Status.INDEX_OFFICE, i排出現場コード);
                } else if (len == 11) {
                    mStatus.SetData(Status.INDEX_SANGYO, ((JTextFieldKeiryo) evt.getSource()).getText());
                } else if (s0.equals("T")) {
                    mStatus.SetData(Status.INDEX_TRANS, ((JTextFieldKeiryo) evt.getSource()).getText().substring(1));
                } else if (s0.equals("G")) {
                    mStatus.SetData(Status.INDEX_KIND, ((JTextFieldKeiryo) evt.getSource()).getText().substring(1));
                }
            } else {
                ((JTextFieldKeiryo) evt.getSource()).setText("0");
            }
            cont_no_tex.transferFocus();
            evt.consume();
        } else if (evt.getKeyCode() == KeyEvent.VK_F1) {
            //検索
            SelectContAction();
        }
    }//GEN-LAST:event_cont_no_texKeyPressed

    private void car_kind_no_texKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_car_kind_no_texKeyPressed
        // TODO add your handling code here:
        // Enterでフォーカスを移動する
        int mod = evt.getModifiersEx();
        if (evt.getKeyCode() == KeyEvent.VK_ENTER & ((mod & InputEvent.SHIFT_DOWN_MASK) != 0)) {
            car_kind_no_tex.transferFocus();
            evt.consume();
            cont_no_tex.requestFocus();
        } else if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            int len = (((JTextFieldKeiryo) evt.getSource()).getText()).length();
            if (len > 0) {
                String s0 = ((JTextFieldKeiryo) evt.getSource()).getText().substring(0, 1);
                if (len == 8) {
                    //排出業者コード4桁 + "-" + 排出現場コード3桁
                    String i排出業者コード = ((JTextFieldKeiryo) evt.getSource()).getText().substring(0, 4);
                    String i排出現場コード = ((JTextFieldKeiryo) evt.getSource()).getText().substring(5, 8);
                    mStatus.SetData(Status.INDEX_ENT, i排出業者コード);
                    mStatus.SetData(Status.INDEX_OFFICE, i排出現場コード);
                } else if (len == 11) {
                    mStatus.SetData(Status.INDEX_SANGYO, ((JTextFieldKeiryo) evt.getSource()).getText());
                } else if (s0.equals("T")) {
                    mStatus.SetData(Status.INDEX_TRANS, ((JTextFieldKeiryo) evt.getSource()).getText().substring(1));
                } else if (s0.equals("G")) {
                    mStatus.SetData(Status.INDEX_KIND, ((JTextFieldKeiryo) evt.getSource()).getText().substring(1));
                }
            } else {
                ((JTextFieldKeiryo) evt.getSource()).setText("0");
            }
            car_kind_no_tex.transferFocus();
            evt.consume();
        } else if (evt.getKeyCode() == KeyEvent.VK_F1) {
            //検索
            SelectCarKindAction();
        }
    }//GEN-LAST:event_car_kind_no_texKeyPressed

    private void ent_no_texFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_ent_no_texFocusLost
        if (ent_no_tex.getText() == null ? "" != null : !ent_no_tex.getText().equals("")) {
            String name = mStatus.mSQLID.GetEntrepreneur(ent_no_tex.getText());
            if (name == null ? "" != null : !name.equals("")) {
                ent_jLab.setText(name);
                mStatus.SetData(Status.INDEX_ENT, ent_no_tex.getText());
            } else {
                ent_no_tex.setText("0");
                ent_jLab.setText("");
                mStatus.SetData(Status.INDEX_ENT, ent_no_tex.getText());
            }
        } else {
            ent_no_tex.setText("0");
            ent_jLab.setText("");
        }
    }//GEN-LAST:event_ent_no_texFocusLost

    private void trans_no_texFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_trans_no_texFocusLost
        if (trans_no_tex.getText() == null ? "" != null : !trans_no_tex.getText().equals("")) {
            String name = mStatus.mSQLID.GetTransporter(trans_no_tex.getText());
            if (name == null ? "" != null : !name.equals("")) {
                trans_jLab.setText(name);
                mStatus.SetData(Status.INDEX_TRANS, trans_no_tex.getText());
            } else {
                trans_no_tex.setText("0");
                trans_jLab.setText("");
                mStatus.SetData(Status.INDEX_TRANS, trans_no_tex.getText());
            }
        } else {
            trans_no_tex.setText("0");
            trans_jLab.setText("");
        }
    }//GEN-LAST:event_trans_no_texFocusLost

    private void kind_no_texFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_kind_no_texFocusLost
        // 廃棄物
        if (kind_no_tex.getText() == null ? "" != null : !kind_no_tex.getText().equals("")) {
            String name = mStatus.mSQLID.GetKind(kind_no_tex.getText());

            if (name == null ? "" != null : !name.equals("")) {
                kind_jLab.setText(name);
                mStatus.SetData(Status.INDEX_KIND, kind_no_tex.getText());

            } else {
                kind_no_tex.setText("0");
                kind_jLab.setText("");
                mStatus.SetData(Status.INDEX_KIND, kind_no_tex.getText());
                unit_value_tex.setText("0");
            }
        } else {
            kind_no_tex.setText("0");
            kind_jLab.setText("");
            unit_value_tex.setText("0");
        }
    }//GEN-LAST:event_kind_no_texFocusLost

    private void office_no_texFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_office_no_texFocusLost
        // 排出事業場
        if (office_no_tex.getText() == null ? "" != null : !office_no_tex.getText().equals("")) {
            String name = mStatus.mSQLID.GetOffice(office_no_tex.getText(), ent_no_tex.getText());
            if (name == null ? "" != null : !name.equals("")) {
                office_jLab.setText(name);
                mStatus.SetData(Status.INDEX_OFFICE, office_no_tex.getText());
            } else {
                office_no_tex.setText("0");
                office_jLab.setText("");
                mStatus.SetData(Status.INDEX_OFFICE, office_no_tex.getText());
            }
        } else {
            office_no_tex.setText("0");
            office_jLab.setText("");
        }
    }//GEN-LAST:event_office_no_texFocusLost

    private void cont_no_texFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_cont_no_texFocusLost
        // 処分業者
        if (cont_no_tex.getText() == null ? "" != null : !cont_no_tex.getText().equals("")) {
            String name = mStatus.mSQLID.GetCont(cont_no_tex.getText());
            if (name == null ? "" != null : !name.equals("")) {
                cont_jLab.setText(name);
                mStatus.SetData(Status.INDEX_CONT, cont_no_tex.getText());
            } else {
                cont_jLab.setText("");
                cont_no_tex.setText("1");
                mStatus.SetData(Status.INDEX_CONT, cont_no_tex.getText());
            }
        } else {
            cont_no_tex.setText("1");
            cont_jLab.setText("");
        }
    }//GEN-LAST:event_cont_no_texFocusLost

    private void car_kind_no_texFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_car_kind_no_texFocusLost
        // 車種
        if (car_kind_no_tex.getText() == null ? "" != null : !car_kind_no_tex.getText().equals("")) {
            String name = mStatus.mSQLID.GetCar_Kind(car_kind_no_tex.getText());
            if (name == null ? "" != null : !name.equals("")) {
                car_kind_jLab.setText(name);
                mStatus.SetData(Status.INDEX_CAR_KIND, car_kind_no_tex.getText());
            } else {
                car_kind_no_tex.setText("0");
                car_kind_jLab.setText("");
                mStatus.SetData(Status.INDEX_CAR_KIND, car_kind_no_tex.getText());
            }
        } else {
            car_kind_no_tex.setText("0");
            car_kind_jLab.setText("");
        }
    }//GEN-LAST:event_car_kind_no_texFocusLost

    private void car_id_FocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_car_id_FocusLost
        // 管理ID
        if (car_id_.getText() == null ? "" != null : !car_id_.getText().equals("")
                 && car_id_.getText().length() <= 6) {
            try {
            } catch (NumberFormatException ne) {
                car_id_.setText("0");
                car_id_.requestFocus();
                car_id_.selectAll();
            }

        } else {
            car_id_.setText("0");
            car_id_.requestFocus();
            car_id_.selectAll();
        }

        mStatus.SetData(Status.INDEX_KANRI_ID, car_id_.getText());
    }//GEN-LAST:event_car_id_FocusLost

    private void weight_btn_計量処理KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_weight_btn_計量処理KeyPressed

        int mod = evt.getModifiersEx();
        if (evt.getKeyCode() == KeyEvent.VK_ENTER & ((mod & InputEvent.SHIFT_DOWN_MASK) != 0)) {
            unit_value_tex.requestFocus();
        } else if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            if (checkWeightError()) {
                mStatus.SetData(Status.INDEX_MESSAGE, "重量値エラーです。内容を確認してください。");
            } else {
                if (checkManifestoNumber() && checkCarNumber()) {
                    //現場名が入力された際に、反映しないタイミングがあるため
                    mStatus.SetData(Status.INDEX_OFFICE_SUB, office_sub_name_.getText());
                    mStatus.SetData(Status.INDEX_INPUT_CONFIRM, "1");
                    cancel_btn.requestFocus();
                    clearList();
                }
            }
        } else if (evt.getKeyCode() == KeyEvent.VK_F9) {
        } else if (evt.getKeyCode() == KeyEvent.VK_F10) {
        } else if (evt.getKeyCode() == KeyEvent.VK_F7) {
        }
        if ("現金".equals(cmb売上区分.getSelectedItem())
                 && charge_tex.getText() != "" && net_weight_ > 0) {
            計量時領収書発行();
        }

    }//GEN-LAST:event_weight_btn_計量処理KeyPressed

    private void weight_btn_計量処理ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_weight_btn_計量処理ActionPerformed
        // TODO add your handling code here:
        setData();
        if (checkWeightError()) {
            mStatus.SetData(Status.INDEX_MESSAGE, "重量値エラーです。内容を確認してください。");
        } else {
            if (checkManifestoNumber() && checkCarNumber()) {  //マニフェスト番号と車番のチェック
                if (unit_value_tex.getText().length() > 0 && Integer.valueOf(unit_value_tex.getText().replaceAll(",", "")) >= 0) {
                    try {
                        mStatus.SetData(Status.INDEX_UNIT_VALUE, unit_value_tex.getText());
                    } catch (Exception e) {

                    }
                }
                //現場名が入力された際に、反映しないタイミングがあるため
                mStatus.SetData(Status.INDEX_OFFICE_SUB, office_sub_name_.getText());
                mStatus.SetData(Status.INDEX_INPUT_CONFIRM, "1");
                cancel_btn.requestFocus();
                clearList();
            }
        }

        //計量処理終了後領収書の印字を開始する
        if ("現金".equals(cmb売上区分.getSelectedItem())
                 && charge_tex.getText() != "" && net_weight_ > 0) {
            計量時領収書発行();
        }
    }//GEN-LAST:event_weight_btn_計量処理ActionPerformed

    private void cancel_btnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancel_btnActionPerformed
        // TODO add your handling code here:
        doCancel();
    }//GEN-LAST:event_cancel_btnActionPerformed

    private void sanpai_texFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_sanpai_texFocusLost
        // TODO add your handling code here:
        //フォーカスを失ったときボーダーカラーを戻す
        if (sanpai_tex.getText().length() > 0 && sanpai_tex.getText().length() == 11) {
            sanpai_tex.setText(maniformat(sanpai_tex.getText()));
            mStatus.SetData(Status.INDEX_SANGYO, sanpai_tex.getText());
        } else if (sanpai_tex.getText().length() == 0) {
            mStatus.SetData(Status.INDEX_SANGYO, "");
        }

    }//GEN-LAST:event_sanpai_texFocusLost

    @SuppressWarnings({"null", "ConstantConditions"})
    private void kensetu_texFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_kensetu_texFocusLost
        // TODO add your handling code here:
        //フォーカスを失ったときボーダーカラーを戻す
        if (kensetu_tex.getText().length() > 0 && kensetu_tex.getText().length() == 11) {
            kensetu_tex.setText(maniformat(kensetu_tex.getText()));
            mStatus.SetData(Status.INDEX_KENSETSU, kensetu_tex.getText());
        } else if (kensetu_tex.getText().length() == 0) {
            mStatus.SetData(Status.INDEX_KENSETSU, "");
        }
    }//GEN-LAST:event_kensetu_texFocusLost

    private void sanpai_texKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_sanpai_texKeyPressed
        // TODO add your handling code here:
        // Enterでフォーカスを移動する
        int mod = evt.getModifiersEx();
        if (evt.getKeyCode() == KeyEvent.VK_ENTER & ((mod & InputEvent.SHIFT_DOWN_MASK) != 0)) {
            sanpai_tex.transferFocus();
            evt.consume();
            kensetu_tex.requestFocus();
        } else if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            int len = (((JTextFieldKeiryo) evt.getSource()).getText()).length();
            if (len > 0) {
                String s0 = ((JTextFieldKeiryo) evt.getSource()).getText().substring(0, 1);
                if (len == 11) {
                    //直行型マニフェスト
                    evt.consume();
                } else if (len == 8) {
                    String i排出業者コード = ((JTextFieldKeiryo) evt.getSource()).getText().substring(0, 4);
                    String i排出現場コード = ((JTextFieldKeiryo) evt.getSource()).getText().substring(5, 8);
                    mStatus.SetData(Status.INDEX_ENT, i排出業者コード);
                    mStatus.SetData(Status.INDEX_OFFICE, i排出現場コード);
                } else if (s0.equals("T")) {
                    mStatus.SetData(Status.INDEX_TRANS, ((JTextFieldKeiryo) evt.getSource()).getText().substring(1));
                } else if (s0.equals("G")) {
                    mStatus.SetData(Status.INDEX_KIND, ((JTextFieldKeiryo) evt.getSource()).getText().substring(1));
                } else {
                    ((JTextFieldKeiryo) evt.getSource()).setText("");
                }
            }
            weight_btn_重量取込.requestFocus();
            evt.consume();

        } else if (evt.getKeyCode() == KeyEvent.VK_F9) {
        } else if (evt.getKeyCode() == KeyEvent.VK_F10) {
        } else if (evt.getKeyCode() == KeyEvent.VK_F7) {
        }
    }//GEN-LAST:event_sanpai_texKeyPressed

    private void kensetu_texKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_kensetu_texKeyPressed
        // TODO add your handling code here:
        // Enterでフォーカスを移動する
        int mod = evt.getModifiersEx();
        if (evt.getKeyCode() == KeyEvent.VK_ENTER & ((mod & InputEvent.SHIFT_DOWN_MASK) != 0)) {
            kensetu_tex.transferFocus();
            evt.consume();
            car_kind_no_tex.requestFocus();
        } else if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            int len = (((JTextFieldKeiryo) evt.getSource()).getText()).length();
            if (len > 0) {
                String s0 = ((JTextFieldKeiryo) evt.getSource()).getText().substring(0, 1);
                if (len == 11) {
                    //建設系マニフェスト
                    evt.consume();
                } else if (len == 8) {
                    //排出業者コード4桁 + "-" + 排出現場コード3桁
                    String i排出業者コード = ((JTextFieldKeiryo) evt.getSource()).getText().substring(0, 4);
                    String i排出現場コード = ((JTextFieldKeiryo) evt.getSource()).getText().substring(5, 8);
                    mStatus.SetData(Status.INDEX_ENT, i排出業者コード);
                    mStatus.SetData(Status.INDEX_OFFICE, i排出現場コード);
                } else if (s0.equals("T")) {
                    mStatus.SetData(Status.INDEX_TRANS, ((JTextFieldKeiryo) evt.getSource()).getText().substring(1));
                } else if (s0.equals("G")) {
                    mStatus.SetData(Status.INDEX_KIND, ((JTextFieldKeiryo) evt.getSource()).getText().substring(1));
                } else {
                    ((JTextFieldKeiryo) evt.getSource()).setText("");
                }
            }
            sanpai_tex.requestFocus();

        } else if (evt.getKeyCode() == KeyEvent.VK_F9) {
        } else if (evt.getKeyCode() == KeyEvent.VK_F10) {
        } else if (evt.getKeyCode() == KeyEvent.VK_F7) {
        }
    }//GEN-LAST:event_kensetu_texKeyPressed

    private void car_id_FocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_car_id_FocusGained
        // TODO add your handling code here:
        //フォーカス取得時
        mStatus.SetData(Status.INDEX_MESSAGE, "<html>管理IDを入力してください。ダブルクリックにて検索可能です。</html>");
        car_id_.selectAll();
        setAsciiMode(car_id_);
    }//GEN-LAST:event_car_id_FocusGained

    private void ent_no_texFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_ent_no_texFocusGained
        // TODO add your handling code here:
        //フォーカス取得時
        mStatus.SetData(Status.INDEX_MESSAGE, "<html>排出業者IDを入力してください。ダブルクリックにて検索可能です。<br>IDを入力しない場合にはゼロを入力してください。</html>");
        ent_no_tex.selectAll();
        setAsciiMode(car_id_);
    }//GEN-LAST:event_ent_no_texFocusGained

    private void trans_no_texFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_trans_no_texFocusGained
        // TODO add your handling code here:
        //フォーカス取得時
        mStatus.SetData(Status.INDEX_MESSAGE, "<html>運搬業者IDを入力してください。ダブルクリックにて検索可能です。<br>IDを入力しない場合にはゼロを入力してください。</html>");
        trans_no_tex.selectAll();
        setAsciiMode(car_id_);
    }//GEN-LAST:event_trans_no_texFocusGained

    private void kind_no_texFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_kind_no_texFocusGained
        // TODO add your handling code here:
        //フォーカス取得時
        mStatus.SetData(Status.INDEX_MESSAGE, "<html>品目IDを入力してください。ダブルクリックにて検索可能です。<br>IDを入力しない場合にはゼロを入力してください。</html>");
        kind_no_tex.selectAll();
        setAsciiMode(car_id_);
    }//GEN-LAST:event_kind_no_texFocusGained

    private void office_no_texFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_office_no_texFocusGained
        // TODO add your handling code here:
        //フォーカス取得時
        mStatus.SetData(Status.INDEX_MESSAGE, "<html>契約IDを入力してください。ダブルクリックにて検索可能です。<br>IDを入力しない場合にはゼロを入力してください。</html>");
        office_no_tex.selectAll();
        setAsciiMode(car_id_);
    }//GEN-LAST:event_office_no_texFocusGained

    private void cont_no_texFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_cont_no_texFocusGained
        // TODO add your handling code here:
        //フォーカス取得時
        mStatus.SetData(Status.INDEX_MESSAGE, "<html>処分業者IDを入力してください。ダブルクリックにて検索可能です。<br>IDを入力しない場合にはゼロを入力してください。</html>");
        cont_no_tex.selectAll();
        setAsciiMode(car_id_);
    }//GEN-LAST:event_cont_no_texFocusGained

    private void car_kind_no_texFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_car_kind_no_texFocusGained
        // TODO add your handling code here:
        //フォーカス取得時
        mStatus.SetData(Status.INDEX_MESSAGE, "<html>車種IDを入力してください。ダブルクリックにて検索可能です。<br>IDを入力しない場合にはゼロを入力してください。</html>");
        car_kind_no_tex.selectAll();
        setAsciiMode(car_id_);
    }//GEN-LAST:event_car_kind_no_texFocusGained

    private void sanpai_texFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_sanpai_texFocusGained
        // TODO add your handling code here:
        //フォーカス取得時
        mStatus.SetData(Status.INDEX_MESSAGE, "直行型交付番号を入力してください。");
        sanpai_tex.selectAll();
        setAsciiMode(car_id_);
    }//GEN-LAST:event_sanpai_texFocusGained

    private void kensetu_texFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_kensetu_texFocusGained
        // TODO add your handling code here:
        //フォーカス取得時
        mStatus.SetData(Status.INDEX_MESSAGE, "建設系交付番号を入力してください。");
        kensetu_tex.selectAll();
        setAsciiMode(car_id_);
    }//GEN-LAST:event_kensetu_texFocusGained

    private void weight_btn_計量処理FocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_weight_btn_計量処理FocusGained
        // TODO add your handling code here:
        //フォーカス取得時
        mStatus.SetData(Status.INDEX_MESSAGE, "内容を確認し計量ボタンを押してください。");
        weight_btn_計量処理.setBackground(new Color(255, 51, 51));

    }//GEN-LAST:event_weight_btn_計量処理FocusGained

    private void weight_btn_計量処理FocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_weight_btn_計量処理FocusLost
        // TODO add your handling code here:
        //フォーカス失ったら
        weight_btn_計量処理.setBackground(new Color(240, 240, 240));
    }//GEN-LAST:event_weight_btn_計量処理FocusLost

    private void bill_no_texFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_bill_no_texFocusGained
        // TODO add your handling code here:
        //フォーカス取得時
        mStatus.SetData(Status.INDEX_MESSAGE, "<html>請求先IDを入力してください。ダブルクリックにて検索可能です。<br>IDを入力しない場合にはゼロを入力してください。</html>");
        bill_no_tex.selectAll();
        setAsciiMode(car_id_);
    }//GEN-LAST:event_bill_no_texFocusGained

    private void bill_no_texFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_bill_no_texFocusLost
        // TODO add your handling code here:
        //フォーカスを失ったときボーダーカラーを戻す
        // 請求先
        if (bill_no_tex.getText() == null ? "" != null : !bill_no_tex.getText().equals("")) {
            String name = mStatus.mSQLID.GetBill(bill_no_tex.getText());
            if (name == null ? "" != null : !name.equals("")) {
                bill_jLab.setText(name);
                mStatus.SetData(Status.INDEX_BILL, bill_no_tex.getText());
            } else {
                bill_jLab.setText("");
                mStatus.SetData(Status.INDEX_BILL, bill_no_tex.getText());
            }
        } else {
            bill_jLab.setText("");
        }

        //現金区分の自動入力の処理を加える 
        cmb売上区分.setSelectedItem(get売上区分());

    }//GEN-LAST:event_bill_no_texFocusLost

    private void bill_no_texKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_bill_no_texKeyPressed
        // TODO add your handling code here:
        // Enterでフォーカスを移動する
        int mod = evt.getModifiersEx();
        if (evt.getKeyCode() == KeyEvent.VK_ENTER & ((mod & InputEvent.SHIFT_DOWN_MASK) != 0)) {
            bill_no_tex.transferFocus();
            evt.consume();
            car_no_.requestFocus();
        } else if (evt.getKeyCode() == KeyEvent.VK_ENTER) {

            int len = (((JTextFieldKeiryo) evt.getSource()).getText()).length();
            if (len > 0) {
                String s0 = ((JTextFieldKeiryo) evt.getSource()).getText().substring(0, 1);
                if (len == 8) {
                    String i排出業者コード = ((JTextFieldKeiryo) evt.getSource()).getText().substring(0, 4);
                    String i排出現場コード = ((JTextFieldKeiryo) evt.getSource()).getText().substring(5, 8);
                    mStatus.SetData(Status.INDEX_ENT, i排出業者コード);
                    mStatus.SetData(Status.INDEX_OFFICE, i排出現場コード);
                } else if (len == 11) {
                    mStatus.SetData(Status.INDEX_SANGYO, ((JTextFieldKeiryo) evt.getSource()).getText());
                } else if (s0.equals("T")) {
                    mStatus.SetData(Status.INDEX_TRANS, ((JTextFieldKeiryo) evt.getSource()).getText().substring(1));
                } else if (s0.equals("G")) {
                    mStatus.SetData(Status.INDEX_KIND, ((JTextFieldKeiryo) evt.getSource()).getText().substring(1));
                }
            } else {
                ((JTextFieldKeiryo) evt.getSource()).setText("0");
            }
            ent_no_tex.requestFocus();
            evt.consume();
        } else if (evt.getKeyCode() == KeyEvent.VK_F1) {
            //検索
            SelectBillAction();

        }
    }//GEN-LAST:event_bill_no_texKeyPressed

    private void office_sub_name_KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_office_sub_name_KeyPressed
        // TODO add your handling code here:
        // Enterでフォーカスを移動する
        int mod = evt.getModifiersEx();
        if (evt.getKeyCode() == KeyEvent.VK_ENTER & ((mod & InputEvent.SHIFT_DOWN_MASK) != 0)) {
            office_sub_name_.transferFocus();
            evt.consume();
            office_no_tex.requestFocus();
        } else if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            office_sub_name_.transferFocus();
            evt.consume();
        } else if (evt.getKeyCode() == KeyEvent.VK_F1) {
            //検索
            SelectOfficeSubAction();
        }
    }//GEN-LAST:event_office_sub_name_KeyPressed

    private void office_sub_name_FocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_office_sub_name_FocusGained
        // TODO add your handling code here:
        //フォーカス取得時
        mStatus.SetData(Status.INDEX_MESSAGE, "現場名の入力ができます。");
        office_sub_name_.selectAll();
        setKanjiMode(office_sub_name_);
    }//GEN-LAST:event_office_sub_name_FocusGained

    private void office_sub_name_FocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_office_sub_name_FocusLost
        // TODO add your handling code here:
        //20120617 現場名の補完
        mStatus.SetData(Status.INDEX_OFFICE_SUB, office_sub_name_.getText());
        setAsciiMode(office_sub_name_);
    }//GEN-LAST:event_office_sub_name_FocusLost

    private void unit_value_texFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_unit_value_texFocusGained
        // TODO add your handling code here:
        //フォーカス取得時
        mStatus.SetData(Status.INDEX_MESSAGE, "<html>単価を入力してください。<br>入力しない場合にはゼロを入力してください。</html>");
        unit_value_tex.selectAll();
        setAsciiMode(car_id_);
    }//GEN-LAST:event_unit_value_texFocusGained

    private void unit_value_texFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_unit_value_texFocusLost
        // TODO add your handling code here:
        if (unit_value_tex.getText() == null ? "" != null : !unit_value_tex.getText().equals("")) {
            try {
                mStatus.SetData(Status.INDEX_UNIT_VALUE, unit_value_tex.getText());
            } catch (Exception e) {

            }

        } else {
            unit_value_tex.setText("0");
            charge_tex.setText("0");
        }
    }//GEN-LAST:event_unit_value_texFocusLost

    private void unit_value_texKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_unit_value_texKeyPressed
        // TODO add your handling code here:
        // Enterでフォーカスを移動する
        int mod = evt.getModifiersEx();
        if (evt.getKeyCode() == KeyEvent.VK_ENTER & ((mod & InputEvent.SHIFT_DOWN_MASK) != 0)) {
            unit_value_tex.transferFocus();
            evt.consume();
            sanpai_tex.requestFocus();
        } else if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            int len = (((JTextFieldKeiryo) evt.getSource()).getText()).length();
            if (len == 8) {
                ((JTextFieldKeiryo) evt.getSource()).setText("0");
                ((JTextFieldKeiryo) evt.getSource()).selectAll();
                ((JTextFieldKeiryo) evt.getSource()).requestFocus();
            } else {
                unit_value_tex.transferFocus();
                evt.consume();
                weight_btn_重量取込.requestFocus();
            }
        } else if (evt.getKeyCode() == KeyEvent.VK_F9) {
        } else if (evt.getKeyCode() == KeyEvent.VK_F10) {
        } else if (evt.getKeyCode() == KeyEvent.VK_F7) {
        }
    }//GEN-LAST:event_unit_value_texKeyPressed

    private void car_no_FocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_car_no_FocusGained
        // TODO add your handling code here:
        //フォーカス取得時
        if (!mStatus.f重量オーバー) {
            //重量オーバーの可能性が無いのならメッセージを表示
            mStatus.SetData(Status.INDEX_MESSAGE, "車番を入力してください。(半角数字４桁)");
        }
        car_no_.selectAll();
    }//GEN-LAST:event_car_no_FocusGained

    private void car_no_FocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_car_no_FocusLost
        // TODO add your handling code here:
        // 車番
        if (car_no_.getText() == null ? "" != null : !car_no_.getText().equals("")) {

            try {
            } catch (NumberFormatException ne) {
                car_no_.setText("0");
                car_no_.requestFocus();
                car_no_.selectAll();
            }
        } else {
            car_no_.setText("0");
            car_no_.requestFocus();
            car_no_.selectAll();
        }
        mStatus.SetData(Status.INDEX_CAR_NO, car_no_.getText());
    }//GEN-LAST:event_car_no_FocusLost

    private void car_no_KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_car_no_KeyPressed
        // TODO add your handling code here:
        // Enterでフォーカスを移動する
        int mod = evt.getModifiersEx();
        if (evt.getKeyCode() == KeyEvent.VK_ENTER & ((mod & InputEvent.SHIFT_DOWN_MASK) != 0)) {
            evt.consume();
            car_id_.requestFocus();
        } else if (evt.getKeyCode() == KeyEvent.VK_ENTER) {

            int len = (((JTextFieldKeiryo) evt.getSource()).getText()).length();
            if (len > 0) {
                if (!((JTextFieldKeiryo) evt.getSource()).getText().equals("0")
                         && len < 5) {
                    try {
                        car_no_.transferFocus();
                        evt.consume();
                    } catch (NumberFormatException ne) {
                        car_no_.setText("0");
                        car_no_.requestFocus();
                        car_no_.selectAll();
                    }
                } else if (len == 8) {
                    ((JTextFieldKeiryo) evt.getSource()).setText("0");
                    ((JTextFieldKeiryo) evt.getSource()).selectAll();
                    ((JTextFieldKeiryo) evt.getSource()).requestFocus();
                    updateWeight();
                }
            } else {
                car_no_.setText("0");
                car_no_.requestFocus();
                car_no_.selectAll();
            }
        } else if (evt.getKeyCode() == KeyEvent.VK_F9) {
        } else if (evt.getKeyCode() == KeyEvent.VK_F10) {
        } else if (evt.getKeyCode() == KeyEvent.VK_F7) {
        }
    }//GEN-LAST:event_car_no_KeyPressed

    private void weight_btn_登録処理ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_weight_btn_登録処理ActionPerformed
        // TODO add your handling code here:
        setData();
        if (checkWeightError()) {
            mStatus.SetData(Status.INDEX_MESSAGE, "重量値エラーです。内容を確認してください。");
        } else {
            if (checkManifestoNumber() && checkCarNumber()) {
                if (unit_value_tex.getText().length() > 0 && Integer.valueOf(unit_value_tex.getText().replaceAll(",", "")) >= 0) {
                    try {
                        mStatus.SetData(Status.INDEX_UNIT_VALUE, unit_value_tex.getText());
                    } catch (Exception e) {

                    }
                }
                mStatus.SetData(Status.INDEX_INPUT_CONFIRM, "2");
                cancel_btn.requestFocus();
                clearList();
            }
        }
    }//GEN-LAST:event_weight_btn_登録処理ActionPerformed

    private void searchWordField1_KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_searchWordField1_KeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            //契約内容テーブル更新
            updateTable2();
            searchWordField2_.requestFocus();
            searchWordField1_1.setText(searchWordField1_.getText());
            //契約内容テーブル更新
            updateTable3();
        }
    }//GEN-LAST:event_searchWordField1_KeyPressed

    private void searchWordField2_KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_searchWordField2_KeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            //契約内容テーブル更新
            updateTable2();
            btn絞込み.requestFocus();
            searchWordField2_1.setText(searchWordField2_.getText());
        }
    }//GEN-LAST:event_searchWordField2_KeyPressed

    private void btn絞込みActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn絞込みActionPerformed
        // TODO add your handling code here:
        //契約内容テーブル更新
        updateTable2();
    }//GEN-LAST:event_btn絞込みActionPerformed

    private void btn絞込みKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_btn絞込みKeyPressed
        // TODO add your handling code here:

        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            //契約内容テーブル更新
            updateTable2();
            searchWordField1_.requestFocus();
        }
    }//GEN-LAST:event_btn絞込みKeyPressed

    private void dataTable_1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_dataTable_1MouseClicked
        // TODO add your handling code here:
        int row = dataTable_1.getSelectedRow();
        if (row >= 0) {
            searchWordField1_1.setText(searchWordField1_.getText());
            searchWordField2_.setText((String) model_1.getValueAt(row, "kana"));
            searchWordField2_1.setText(searchWordField2_.getText());
            //契約内容テーブル更新
            updateTable3();

            //契約品目の表示/////////////////////////////////////////////////////
            int cnt = 0;
            for (int i = 0; i < 40; i++) {
                if ((Integer) model_1.getValueAt(row, String.format("goods_unit%1$02d", i + 1)) > 0
                         && (Long) gmodel_.getValueAt(i, "sno") == i + 1) {
                    goods_id[cnt] = (Integer) gmodel_.getValueAt(i, "id");
                    office_unit[cnt] = (Integer) model_1.getValueAt(row, String.format("goods_unit%1$02d", i + 1));
                    goods_name[cnt] = (String) gmodel_.getValueAt(i, "name");
                    cnt++;
                }
            }
            for (int ii = cnt; ii < 40; ii++) {
                goods_name[ii] = "";
            }
            int inx = 0;
            goods_name01_.setText(goods_name[inx]);
            inx++;
            goods_name02_.setText(goods_name[inx]);
            inx++;
            goods_name03_.setText(goods_name[inx]);
            inx++;
            goods_name04_.setText(goods_name[inx]);
            inx++;
            goods_name05_.setText(goods_name[inx]);
            inx++;
            goods_name06_.setText(goods_name[inx]);
            inx++;
            goods_name07_.setText(goods_name[inx]);
            inx++;
            goods_name08_.setText(goods_name[inx]);
            inx++;
            goods_name09_.setText(goods_name[inx]);
            inx++;
            goods_name10_.setText(goods_name[inx]);
            inx++;
            goods_name11_.setText(goods_name[inx]);
            inx++;
            goods_name12_.setText(goods_name[inx]);
            inx++;
            goods_name13_.setText(goods_name[inx]);
            inx++;
            goods_name14_.setText(goods_name[inx]);
            inx++;
            goods_name15_.setText(goods_name[inx]);
            inx++;
            goods_name16_.setText(goods_name[inx]);
            inx++;
            goods_name17_.setText(goods_name[inx]);
            inx++;
            goods_name18_.setText(goods_name[inx]);
            inx++;
            goods_name19_.setText(goods_name[inx]);
            inx++;
            goods_name20_.setText(goods_name[inx]);
            inx++;
            goods_name21_.setText(goods_name[inx]);
            inx++;
            goods_name22_.setText(goods_name[inx]);
            inx++;
            goods_name23_.setText(goods_name[inx]);
            inx++;
            goods_name24_.setText(goods_name[inx]);
            inx++;
            goods_name25_.setText(goods_name[inx]);
            inx++;
            goods_name26_.setText(goods_name[inx]);
            inx++;
            goods_name27_.setText(goods_name[inx]);
            inx++;
            goods_name28_.setText(goods_name[inx]);
            inx++;
            goods_name29_.setText(goods_name[inx]);
            inx++;
            goods_name30_.setText(goods_name[inx]);
            inx++;
            goods_name31_.setText(goods_name[inx]);
            inx++;
            goods_name32_.setText(goods_name[inx]);
            inx++;
            goods_name33_.setText(goods_name[inx]);
            inx++;
            goods_name34_.setText(goods_name[inx]);
            inx++;
            goods_name35_.setText(goods_name[inx]);
            inx++;
            goods_name36_.setText(goods_name[inx]);
            inx++;
            goods_name37_.setText(goods_name[inx]);
            inx++;
            goods_name38_.setText(goods_name[inx]);
            inx++;
            goods_name39_.setText(goods_name[inx]);
            inx++;
            goods_name40_.setText(goods_name[inx]);
            inx++;
        }
        //////////////////////////////////////////////////////////////////////

        //契約運搬業者の表示///////////////////////////////////////////////////
        trans_id[0] = (Integer) model_1.getValueAt(row, "trans_id01");
        trans_name01_.setText((String) model_1.getValueAt(row, "trans_name01"));
        trans_id[1] = (Integer) model_1.getValueAt(row, "trans_id02");
        trans_name02_.setText((String) model_1.getValueAt(row, "trans_name02"));
        trans_id[2] = (Integer) model_1.getValueAt(row, "trans_id03");
        trans_name03_.setText((String) model_1.getValueAt(row, "trans_name03"));
        trans_id[3] = (Integer) model_1.getValueAt(row, "trans_id04");
        trans_name04_.setText((String) model_1.getValueAt(row, "trans_name04"));
        trans_id[4] = (Integer) model_1.getValueAt(row, "trans_id05");
        trans_name05_.setText((String) model_1.getValueAt(row, "trans_name05"));

        ///////////////////////////////////////////////////////////////////////
        //契約処分業者の表示///////////////////////////////////////////////////
        cont_id[0] = (Integer) model_1.getValueAt(row, "cont_id01");
        cont_name01_.setText((String) model_1.getValueAt(row, "cont_name01"));
        if (cont_id[0] > 1) {
            cont_name01_.setBackground(Color.pink);
        } else {
            //[255,255,255]
            cont_name01_.setBackground(new Color(255, 255, 255));
        }
        cont_id[1] = (Integer) model_1.getValueAt(row, "cont_id02");
        cont_name02_.setText((String) model_1.getValueAt(row, "cont_name02"));
        if (cont_id[1] > 1) {
            cont_name02_.setBackground(Color.pink);
        } else {
            //[234,234,255]
            cont_name02_.setBackground(new Color(234, 234, 255));
        }
        cont_id[2] = (Integer) model_1.getValueAt(row, "cont_id03");
        cont_name03_.setText((String) model_1.getValueAt(row, "cont_name03"));
        if (cont_id[2] > 1) {
            cont_name03_.setBackground(Color.pink);
        } else {
            //[255,255,255]
            cont_name03_.setBackground(new Color(255, 255, 255));
        }
        cont_id[3] = (Integer) model_1.getValueAt(row, "cont_id04");
        cont_name04_.setText((String) model_1.getValueAt(row, "cont_name04"));
        if (cont_id[3] > 1) {
            cont_name04_.setBackground(Color.pink);
        } else {
            //[234,234,255]
            cont_name04_.setBackground(new Color(234, 234, 255));
        }
        ///////////////////////////////////////////////////////////////////////

    }//GEN-LAST:event_dataTable_1MouseClicked

    private void jButton8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton8ActionPerformed
        // TODO add your handling code here:
        //表示内容を計量画面へ反映
        int row = dataTable_1.getSelectedRow();
        if (row >= 0) {
            // 排出業者 /////////////////////////////////////////////////////////////////////////
            契約取込flag = true;
            ent_no_tex.setText(String.valueOf((Integer) model_1.getValueAt(row, "ent_id")));
            ent_jLab.setText((String) model_1.getValueAt(row, "ent_name"));
            mStatus.SetData(Status.INDEX_ENT, ent_no_tex.getText());

            if (rb1_.isSelected() && String.valueOf(goods_id[0]).length() > 0) {
                kind_no_tex.setText(String.valueOf(goods_id[0]));
            } else if (rb2_.isSelected() && String.valueOf(goods_id[0]).length() > 0) {
                kind_no_tex.setText(String.valueOf(goods_id[1]));
            } else if (rb3_.isSelected() && String.valueOf(goods_id[0]).length() > 0) {
                kind_no_tex.setText(String.valueOf(goods_id[2]));
            } else if (rb4_.isSelected() && String.valueOf(goods_id[0]).length() > 0) {
                kind_no_tex.setText(String.valueOf(goods_id[3]));
            } else if (rb5_.isSelected() && String.valueOf(goods_id[0]).length() > 0) {
                kind_no_tex.setText(String.valueOf(goods_id[4]));
            } else if (rb6_.isSelected() && String.valueOf(goods_id[0]).length() > 0) {
                kind_no_tex.setText(String.valueOf(goods_id[5]));
            } else if (rb7_.isSelected() && String.valueOf(goods_id[0]).length() > 0) {
                kind_no_tex.setText(String.valueOf(goods_id[6]));
            } else if (rb8_.isSelected() && String.valueOf(goods_id[0]).length() > 0) {
                kind_no_tex.setText(String.valueOf(goods_id[7]));
            } else if (rb9_.isSelected() && String.valueOf(goods_id[0]).length() > 0) {
                kind_no_tex.setText(String.valueOf(goods_id[8]));
            } else if (rb10_.isSelected() && String.valueOf(goods_id[0]).length() > 0) {
                kind_no_tex.setText(String.valueOf(goods_id[9]));
            } else if (rb11_.isSelected() && String.valueOf(goods_id[0]).length() > 0) {
                kind_no_tex.setText(String.valueOf(goods_id[10]));
            } else if (rb12_.isSelected() && String.valueOf(goods_id[0]).length() > 0) {
                kind_no_tex.setText(String.valueOf(goods_id[11]));
            } else if (rb13_.isSelected() && String.valueOf(goods_id[0]).length() > 0) {
                kind_no_tex.setText(String.valueOf(goods_id[12]));
            } else if (rb14_.isSelected() && String.valueOf(goods_id[0]).length() > 0) {
                kind_no_tex.setText(String.valueOf(goods_id[13]));
            } else if (rb15_.isSelected() && String.valueOf(goods_id[0]).length() > 0) {
                kind_no_tex.setText(String.valueOf(goods_id[14]));
            } else if (rb16_.isSelected() && String.valueOf(goods_id[0]).length() > 0) {
                kind_no_tex.setText(String.valueOf(goods_id[15]));
            } else if (rb17_.isSelected() && String.valueOf(goods_id[0]).length() > 0) {
                kind_no_tex.setText(String.valueOf(goods_id[16]));
            } else if (rb18_.isSelected() && String.valueOf(goods_id[0]).length() > 0) {
                kind_no_tex.setText(String.valueOf(goods_id[17]));
            } else if (rb19_.isSelected() && String.valueOf(goods_id[0]).length() > 0) {
                kind_no_tex.setText(String.valueOf(goods_id[18]));
            } else if (rb20_.isSelected() && String.valueOf(goods_id[0]).length() > 0) {
                kind_no_tex.setText(String.valueOf(goods_id[19]));
            } else if (rb21_.isSelected() && String.valueOf(goods_id[0]).length() > 0) {
                kind_no_tex.setText(String.valueOf(goods_id[20]));
            } else if (rb22_.isSelected() && String.valueOf(goods_id[0]).length() > 0) {
                kind_no_tex.setText(String.valueOf(goods_id[21]));
            } else if (rb23_.isSelected() && String.valueOf(goods_id[0]).length() > 0) {
                kind_no_tex.setText(String.valueOf(goods_id[22]));
            } else if (rb24_.isSelected() && String.valueOf(goods_id[0]).length() > 0) {
                kind_no_tex.setText(String.valueOf(goods_id[23]));
            } else if (rb25_.isSelected() && String.valueOf(goods_id[0]).length() > 0) {
                kind_no_tex.setText(String.valueOf(goods_id[24]));
            } else if (rb26_.isSelected() && String.valueOf(goods_id[0]).length() > 0) {
                kind_no_tex.setText(String.valueOf(goods_id[25]));
            } else if (rb27_.isSelected() && String.valueOf(goods_id[0]).length() > 0) {
                kind_no_tex.setText(String.valueOf(goods_id[26]));
            } else if (rb28_.isSelected() && String.valueOf(goods_id[0]).length() > 0) {
                kind_no_tex.setText(String.valueOf(goods_id[27]));
            } else if (rb29_.isSelected() && String.valueOf(goods_id[0]).length() > 0) {
                kind_no_tex.setText(String.valueOf(goods_id[28]));
            } else if (rb30_.isSelected() && String.valueOf(goods_id[0]).length() > 0) {
                kind_no_tex.setText(String.valueOf(goods_id[29]));
            } else if (rb31_.isSelected() && String.valueOf(goods_id[0]).length() > 0) {
                kind_no_tex.setText(String.valueOf(goods_id[30]));
            } else if (rb32_.isSelected() && String.valueOf(goods_id[0]).length() > 0) {
                kind_no_tex.setText(String.valueOf(goods_id[31]));
            } else if (rb33_.isSelected() && String.valueOf(goods_id[0]).length() > 0) {
                kind_no_tex.setText(String.valueOf(goods_id[32]));
            } else if (rb34_.isSelected() && String.valueOf(goods_id[0]).length() > 0) {
                kind_no_tex.setText(String.valueOf(goods_id[33]));
            } else if (rb35_.isSelected() && String.valueOf(goods_id[0]).length() > 0) {
                kind_no_tex.setText(String.valueOf(goods_id[34]));
            } else if (rb36_.isSelected() && String.valueOf(goods_id[0]).length() > 0) {
                kind_no_tex.setText(String.valueOf(goods_id[35]));
            } else if (rb37_.isSelected() && String.valueOf(goods_id[0]).length() > 0) {
                kind_no_tex.setText(String.valueOf(goods_id[36]));
            } else if (rb38_.isSelected() && String.valueOf(goods_id[0]).length() > 0) {
                kind_no_tex.setText(String.valueOf(goods_id[37]));
            } else if (rb39_.isSelected() && String.valueOf(goods_id[0]).length() > 0) {
                kind_no_tex.setText(String.valueOf(goods_id[38]));
            } else if (rb40_.isSelected() && String.valueOf(goods_id[0]).length() > 0) {
                kind_no_tex.setText(String.valueOf(goods_id[39]));
            }

            // 廃棄物 /////////////////////////////////////////////////////////////////////////
            if (kind_no_tex.getText() == null ? "" != null : !kind_no_tex.getText().equals("")) {
                String name = mStatus.mSQLID.GetKind(kind_no_tex.getText());

                if (name == null ? "" != null : !name.equals("")) {
                    kind_jLab.setText(name);
                    mStatus.SetData(Status.INDEX_KIND, kind_no_tex.getText());

                } else {
                    kind_no_tex.setText("0");
                    kind_jLab.setText("");
                    mStatus.SetData(Status.INDEX_KIND, kind_no_tex.getText());
                    unit_value_tex.setText("0");
                }
            } else {
                kind_no_tex.setText("");
                kind_jLab.setText("");
                unit_value_tex.setText("0");
            }

            // 単価 ///////////////////////////////////////////////////////////////////////////////////
            if (rb1_.isSelected()) {
                unit_value_tex.setText(String.valueOf(office_unit[0]));
            } else if (rb2_.isSelected()) {
                unit_value_tex.setText(String.valueOf(office_unit[1]));
            } else if (rb3_.isSelected()) {
                unit_value_tex.setText(String.valueOf(office_unit[2]));
            } else if (rb4_.isSelected()) {
                unit_value_tex.setText(String.valueOf(office_unit[3]));
            } else if (rb5_.isSelected()) {
                unit_value_tex.setText(String.valueOf(office_unit[4]));
            } else if (rb6_.isSelected()) {
                unit_value_tex.setText(String.valueOf(office_unit[5]));
            } else if (rb7_.isSelected()) {
                unit_value_tex.setText(String.valueOf(office_unit[6]));
            } else if (rb8_.isSelected()) {
                unit_value_tex.setText(String.valueOf(office_unit[7]));
            } else if (rb9_.isSelected()) {
                unit_value_tex.setText(String.valueOf(office_unit[8]));
            } else if (rb10_.isSelected()) {
                unit_value_tex.setText(String.valueOf(office_unit[9]));
            } else if (rb11_.isSelected()) {
                unit_value_tex.setText(String.valueOf(office_unit[10]));
            } else if (rb12_.isSelected()) {
                unit_value_tex.setText(String.valueOf(office_unit[11]));
            } else if (rb13_.isSelected()) {
                unit_value_tex.setText(String.valueOf(office_unit[12]));
            } else if (rb14_.isSelected()) {
                unit_value_tex.setText(String.valueOf(office_unit[13]));
            } else if (rb15_.isSelected()) {
                unit_value_tex.setText(String.valueOf(office_unit[14]));
            } else if (rb16_.isSelected()) {
                unit_value_tex.setText(String.valueOf(office_unit[15]));
            } else if (rb17_.isSelected()) {
                unit_value_tex.setText(String.valueOf(office_unit[16]));
            } else if (rb18_.isSelected()) {
                unit_value_tex.setText(String.valueOf(office_unit[17]));
            } else if (rb19_.isSelected()) {
                unit_value_tex.setText(String.valueOf(office_unit[18]));
            } else if (rb20_.isSelected()) {
                unit_value_tex.setText(String.valueOf(office_unit[19]));

            } else if (rb21_.isSelected()) {
                unit_value_tex.setText(String.valueOf(office_unit[20]));
            } else if (rb22_.isSelected()) {
                unit_value_tex.setText(String.valueOf(office_unit[21]));
            } else if (rb23_.isSelected()) {
                unit_value_tex.setText(String.valueOf(office_unit[22]));
            } else if (rb24_.isSelected()) {
                unit_value_tex.setText(String.valueOf(office_unit[23]));
            } else if (rb25_.isSelected()) {
                unit_value_tex.setText(String.valueOf(office_unit[24]));
            } else if (rb26_.isSelected()) {
                unit_value_tex.setText(String.valueOf(office_unit[25]));
            } else if (rb27_.isSelected()) {
                unit_value_tex.setText(String.valueOf(office_unit[26]));
            } else if (rb28_.isSelected()) {
                unit_value_tex.setText(String.valueOf(office_unit[27]));
            } else if (rb29_.isSelected()) {
                unit_value_tex.setText(String.valueOf(office_unit[28]));
            } else if (rb30_.isSelected()) {
                unit_value_tex.setText(String.valueOf(office_unit[29]));
            } else if (rb31_.isSelected()) {
                unit_value_tex.setText(String.valueOf(office_unit[30]));
            } else if (rb32_.isSelected()) {
                unit_value_tex.setText(String.valueOf(office_unit[31]));
            } else if (rb33_.isSelected()) {
                unit_value_tex.setText(String.valueOf(office_unit[32]));
            } else if (rb34_.isSelected()) {
                unit_value_tex.setText(String.valueOf(office_unit[33]));
            } else if (rb35_.isSelected()) {
                unit_value_tex.setText(String.valueOf(office_unit[34]));
            } else if (rb36_.isSelected()) {
                unit_value_tex.setText(String.valueOf(office_unit[35]));
            } else if (rb37_.isSelected()) {
                unit_value_tex.setText(String.valueOf(office_unit[36]));
            } else if (rb38_.isSelected()) {
                unit_value_tex.setText(String.valueOf(office_unit[37]));
            } else if (rb39_.isSelected()) {
                unit_value_tex.setText(String.valueOf(office_unit[38]));
            } else if (rb40_.isSelected()) {
                unit_value_tex.setText(String.valueOf(office_unit[39]));
            }
            mStatus.SetData(Status.INDEX_UNIT_VALUE, unit_value_tex.getText());

            // 現場 /////////////////////////////////////////////////////////////////////////
            office_no_tex.setText(String.valueOf((Integer) model_1.getValueAt(row, "id")));
            office_jLab.setText((String) model_1.getValueAt(row, "name"));
            mStatus.SetData(Status.INDEX_OFFICE, office_no_tex.getText());

            if (trb1_.isSelected()) {
                trans_no_tex.setText(String.valueOf(trans_id[0]));
            } else if (trb2_.isSelected()) {
                trans_no_tex.setText(String.valueOf(trans_id[1]));
            } else if (trb3_.isSelected()) {
                trans_no_tex.setText(String.valueOf(trans_id[2]));
            } else if (trb4_.isSelected()) {
                trans_no_tex.setText(String.valueOf(trans_id[3]));
            } else if (trb5_.isSelected()) {
                trans_no_tex.setText(String.valueOf(trans_id[4]));
            }
            mStatus.SetData(Status.INDEX_TRANS, trans_no_tex.getText());
            /////////////////////////////////////////////////////////////////////////
            jTabbedPane1.setSelectedIndex(0);
        }

    }//GEN-LAST:event_jButton8ActionPerformed

    private void jButton9MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton9MouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton9MouseClicked

    private void jButton9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton9ActionPerformed
        // TODO add your handling code here:
        getCarID();
        car_no_.requestFocus();
    }//GEN-LAST:event_jButton9ActionPerformed

    private void dataTable_2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_dataTable_2MouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_dataTable_2MouseClicked

    private void searchWordField1_1KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_searchWordField1_1KeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            //契約内容テーブル更新
            searchWordField2_1.requestFocus();
            searchWordField1_.setText(searchWordField1_1.getText());
            //契約内容テーブル更新
            updateTable3();
        }
    }//GEN-LAST:event_searchWordField1_1KeyPressed

    private void searchWordField2_1KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_searchWordField2_1KeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            btn絞込み1.requestFocus();
            searchWordField2_.setText(searchWordField2_1.getText());
            //契約期間テーブル更新
            updateTable3();
        }
    }//GEN-LAST:event_searchWordField2_1KeyPressed

    private void btn絞込み1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn絞込み1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btn絞込み1ActionPerformed

    private void btn絞込み1KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_btn絞込み1KeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_btn絞込み1KeyPressed

    private void searchWordField1_1FocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_searchWordField1_1FocusLost

    }//GEN-LAST:event_searchWordField1_1FocusLost

    private void searchWordField2_1FocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_searchWordField2_1FocusLost
        // TODO add your handling code here:
        setAsciiMode(searchWordField2_1);
//        //契約内容テーブル更新
        updateTable3();
    }//GEN-LAST:event_searchWordField2_1FocusLost

    private void weight_btn_2回計量ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_weight_btn_2回計量ActionPerformed
        // TODO add your handling code here:
        loading_weight_ = now_weight_1;
        total_weight_ = 0;
        empty_weight_ = 0;
        net_weight_ = 0;
        選択現在重量_ = now_weight_1;
        txt請求数量.setText("0");
        選択計量回数_ = 2;
    }//GEN-LAST:event_weight_btn_2回計量ActionPerformed

    private void weight_btn_重量取込ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_weight_btn_重量取込ActionPerformed
        // TODO add your handling code here:
        push重量取込();
        weight_btn_計量処理.requestFocus();
    }//GEN-LAST:event_weight_btn_重量取込ActionPerformed

    private void push重量取込() {
        setData();
        選択現在重量_ = now_weight_1;
        mStatus.SetData(Status.INDEX_NET_WEIGHT, String.valueOf(選択現在重量_));
        WeighPanelJournal.recordWeightJournal(con_, "", 選択現在重量_);
    }

    private void weight_btn_重量取込FocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_weight_btn_重量取込FocusGained

        // TODO add your handling code here:
        mStatus.SetData(Status.INDEX_MESSAGE, "重量取り込みボタン押してください。");
        weight_btn_重量取込.setBackground(new Color(255, 51, 51));
    }//GEN-LAST:event_weight_btn_重量取込FocusGained

    private void weight_btn_重量取込FocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_weight_btn_重量取込FocusLost
        // TODO add your handling code here:
        weight_btn_重量取込.setBackground(new Color(240, 240, 240));
    }//GEN-LAST:event_weight_btn_重量取込FocusLost

    private void weight_btn_重量取込KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_weight_btn_重量取込KeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            push重量取込();
            weight_btn_計量処理.requestFocus();
        }
    }//GEN-LAST:event_weight_btn_重量取込KeyPressed

    private void weight_btn_風袋登録ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_weight_btn_風袋登録ActionPerformed
        // TODO add your handling code here:
        if (!rs.GetString("car_id").equals("0")) {
            if (PopupMessage2("入力された車番の風袋を登録します。"
                     + "\r\nよろしいですか？"
                     + "\r\n管理ID:" + rs.GetString("car_id") + "　車番:" + rs.GetString("car_no")
                     + "\r\n風袋重量(kg):" + loading_weight_
                     + "　->　"
                     + now_weight_1, "登録", "キャンセル")) {
                //風袋登録および記憶重量値差し替え
                set風袋重量(rs.GetString("car_id"), now_weight_1);
                loading_weight_ = now_weight_1;
            }
        }

    }//GEN-LAST:event_weight_btn_風袋登録ActionPerformed

    private void jTabbedPane1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTabbedPane1MouseClicked
        // TODO add your handling code here:
        if (jTabbedPane1.getSelectedIndex() == 1) {
            searchWordField1_.requestFocus();
        } else if (jTabbedPane1.getSelectedIndex() == 0) {
            car_id_.requestFocus();
        }
    }//GEN-LAST:event_jTabbedPane1MouseClicked

    private void goods_name01_MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_goods_name01_MouseClicked
        // TODO add your handling code here:
        rb1_.setSelected(true);
    }//GEN-LAST:event_goods_name01_MouseClicked

    private void goods_name02_MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_goods_name02_MouseClicked
        // TODO add your handling code here:
        rb2_.setSelected(true);
    }//GEN-LAST:event_goods_name02_MouseClicked

    private void goods_name03_MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_goods_name03_MouseClicked
        // TODO add your handling code here:
        rb3_.setSelected(true);
    }//GEN-LAST:event_goods_name03_MouseClicked

    private void goods_name04_MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_goods_name04_MouseClicked
        // TODO add your handling code here:
        rb4_.setSelected(true);
    }//GEN-LAST:event_goods_name04_MouseClicked

    private void goods_name05_MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_goods_name05_MouseClicked
        // TODO add your handling code here:
        rb5_.setSelected(true);
    }//GEN-LAST:event_goods_name05_MouseClicked

    private void goods_name06_MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_goods_name06_MouseClicked
        // TODO add your handling code here:
        rb6_.setSelected(true);
    }//GEN-LAST:event_goods_name06_MouseClicked

    private void goods_name08_MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_goods_name08_MouseClicked
        // TODO add your handling code here:
        rb8_.setSelected(true);
    }//GEN-LAST:event_goods_name08_MouseClicked

    private void goods_name07_MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_goods_name07_MouseClicked
        // TODO add your handling code here:
        rb7_.setSelected(true);
    }//GEN-LAST:event_goods_name07_MouseClicked

    private void goods_name10_MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_goods_name10_MouseClicked
        // TODO add your handling code here:
        rb10_.setSelected(true);
    }//GEN-LAST:event_goods_name10_MouseClicked

    private void goods_name11_MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_goods_name11_MouseClicked
        // TODO add your handling code here:
        rb11_.setSelected(true);
    }//GEN-LAST:event_goods_name11_MouseClicked

    private void goods_name12_MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_goods_name12_MouseClicked
        // TODO add your handling code here:
        rb12_.setSelected(true);
    }//GEN-LAST:event_goods_name12_MouseClicked

    private void goods_name13_MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_goods_name13_MouseClicked
        // TODO add your handling code here:
        rb13_.setSelected(true);
    }//GEN-LAST:event_goods_name13_MouseClicked

    private void goods_name14_MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_goods_name14_MouseClicked
        // TODO add your handling code here:
        rb14_.setSelected(true);
    }//GEN-LAST:event_goods_name14_MouseClicked

    private void goods_name15_MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_goods_name15_MouseClicked
        // TODO add your handling code here:
        rb15_.setSelected(true);
    }//GEN-LAST:event_goods_name15_MouseClicked

    private void goods_name16_MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_goods_name16_MouseClicked
        // TODO add your handling code here:
        rb16_.setSelected(true);
    }//GEN-LAST:event_goods_name16_MouseClicked

    private void goods_name17_MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_goods_name17_MouseClicked
        // TODO add your handling code here:
        rb17_.setSelected(true);
    }//GEN-LAST:event_goods_name17_MouseClicked

    private void goods_name18_MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_goods_name18_MouseClicked
        // TODO add your handling code here:
        rb18_.setSelected(true);
    }//GEN-LAST:event_goods_name18_MouseClicked

    private void goods_name20_MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_goods_name20_MouseClicked
        // TODO add your handling code here:
        rb20_.setSelected(true);
    }//GEN-LAST:event_goods_name20_MouseClicked

    private void goods_name19_MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_goods_name19_MouseClicked
        // TODO add your handling code here:
        rb19_.setSelected(true);
    }//GEN-LAST:event_goods_name19_MouseClicked

    private void trans_name01_MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_trans_name01_MouseClicked
        // TODO add your handling code here:
        trb1_.setSelected(true);
    }//GEN-LAST:event_trans_name01_MouseClicked

    private void trans_name02_MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_trans_name02_MouseClicked
        // TODO add your handling code here:
        trb2_.setSelected(true);
    }//GEN-LAST:event_trans_name02_MouseClicked

    private void trans_name03_MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_trans_name03_MouseClicked
        // TODO add your handling code here:
        trb3_.setSelected(true);
    }//GEN-LAST:event_trans_name03_MouseClicked

    private void trans_name04_MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_trans_name04_MouseClicked
        // TODO add your handling code here:
        trb4_.setSelected(true);
    }//GEN-LAST:event_trans_name04_MouseClicked

    private void trans_name05_MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_trans_name05_MouseClicked
        // TODO add your handling code here:
        trb5_.setSelected(true);
    }//GEN-LAST:event_trans_name05_MouseClicked

    private void searchWordField1_FocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_searchWordField1_FocusGained
        // TODO add your handling code here:
        setKatakanaMode(searchWordField1_);
    }//GEN-LAST:event_searchWordField1_FocusGained

    private void goods_name09_MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_goods_name09_MouseClicked
        // TODO add your handling code here:
        rb9_.setSelected(true);
    }//GEN-LAST:event_goods_name09_MouseClicked

    private void searchWordField2_FocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_searchWordField2_FocusLost
        // TODO add your handling code here:
        setAsciiMode(searchWordField2_);
    }//GEN-LAST:event_searchWordField2_FocusLost

    private void searchWordField1_1FocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_searchWordField1_1FocusGained
        // TODO add your handling code here:
        setKatakanaMode(searchWordField1_1);
    }//GEN-LAST:event_searchWordField1_1FocusGained

    private void searchWordField2_FocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_searchWordField2_FocusGained
        // TODO add your handling code here:
        setKatakanaMode(searchWordField2_);
    }//GEN-LAST:event_searchWordField2_FocusGained

    private void searchWordField2_1FocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_searchWordField2_1FocusGained
        // TODO add your handling code here:
        setKatakanaMode(searchWordField2_1);
    }//GEN-LAST:event_searchWordField2_1FocusGained

    private void empty_inputActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_empty_inputActionPerformed
        // TODO add your handling code here:
        //20161130 スパット北上　風袋手入力
        String emptyValue_ = JOptionPane.showInputDialog(this, "重量値を入力してください。", null);
        if (check(emptyValue_)) {
            if (checkWeightError2(Integer.parseInt(emptyValue_))) {
                mStatus.SetData(Status.INDEX_MESSAGE, "重量値エラーです。内容を確認してください。");
            } else if (now_weight_1 > Integer.parseInt(emptyValue_)) {
                if (emptyValue_ != null) {
                    weight_edit_ = true;
                    mStatus.SetData(Status.INDEX_EMPTY, emptyValue_);
                    mStatus.SetData(Status.INDEX_MESSAGE, "");
                    weight_btn_計量処理.requestFocus();
                }

            } else {
                jLbl_message.setText("入力された重量値は現在重量値を超えています。");
            }
        }
        empty_input.setBackground(new Color(255, 153, 153));
    }//GEN-LAST:event_empty_inputActionPerformed

    private void empty_inputFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_empty_inputFocusGained
        // TODO add your handling code here:
        mStatus.SetData(Status.INDEX_MESSAGE, "風袋重量を入力してください。");
    }//GEN-LAST:event_empty_inputFocusGained

    private void empty_inputFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_empty_inputFocusLost
        // TODO add your handling code here:
        empty_input.setBackground(new Color(255, 153, 153));
    }//GEN-LAST:event_empty_inputFocusLost

    private void empty_inputKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_empty_inputKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            weight_btn_計量処理.requestFocus();
            empty_input.setBackground(new Color(255, 153, 153));
        }
    }//GEN-LAST:event_empty_inputKeyPressed

    private void weight_btn_重量初期化ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_weight_btn_重量初期化ActionPerformed
        // TODO add your handling code here:
//        mGetWeightThread101.setZeroReset(true);
    }//GEN-LAST:event_weight_btn_重量初期化ActionPerformed

    private void setData() {
        //建設系マニフェストの交付番号セット
        if (kensetu_tex.getText() == null ? "" != null : !kensetu_tex.getText().equals("")) {
            if (kensetu_tex.getText().length() > 0 && kensetu_tex.getText().length() == 11) {
                kensetu_tex.setText(maniformat(kensetu_tex.getText()));
                mStatus.SetData(Status.INDEX_KENSETSU, kensetu_tex.getText());
            }
        }
        //直行型マニフェストの交付番号セット
        if (sanpai_tex.getText() == null ? "" != null : !sanpai_tex.getText().equals("")) {
            if (sanpai_tex.getText().length() > 0 && sanpai_tex.getText().length() == 11) {
                sanpai_tex.setText(maniformat(sanpai_tex.getText()));
                mStatus.SetData(Status.INDEX_SANGYO, sanpai_tex.getText());
            }
        }
        //単価セット
        if (unit_value_tex.getText() == null ? "" != null : !unit_value_tex.getText().equals("")) {
            try {
                mStatus.SetData(Status.INDEX_UNIT_VALUE, unit_value_tex.getText());
            } catch (Exception e) {

            }

        } else {
            unit_value_tex.setText("0");
            charge_tex.setText("0");
        }
    }

    public boolean check(String str) {
        try {
            Integer.parseInt(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private boolean checkManifestoNumber() {

        if ((sanpai_tex.getText() == null ? "" != null : !sanpai_tex.getText().equals(""))
                 && (kensetu_tex.getText() == null ? "" != null : !kensetu_tex.getText().equals(""))) {
            //両方入力されているのでエラーメッセージを出す
            StaticMessage("マニフェスト番号は片方にのみ入力してください。");
            return false;
        } else if (sanpai_tex.getText() == null ? "" != null : !sanpai_tex.getText().equals("")) {

            if (sanpai_tex.getText().length() > 0 && sanpai_tex.getText().length() == 11) {
                mStatus.SetData(Status.INDEX_SANGYO, sanpai_tex.getText());
                mStatus.SetData(Status.INDEX_KENSETSU, "");
                mStatus.SetData(Status.INDEX_SANGYO2, "");
                return true;
            } else if (sanpai_tex.getText().length() > 0) {
                JOptionPane.showMessageDialog(
                         this,
                         "産廃交付番号の入力桁数を確認してください。",
                         "警告",
                         JOptionPane.WARNING_MESSAGE);
                return false;

            }
            return true;
        } else if (kensetu_tex.getText() == null ? "" != null : !kensetu_tex.getText().equals("")) {
            if (kensetu_tex.getText().length() > 0 && kensetu_tex.getText().length() == 11) {
                mStatus.SetData(Status.INDEX_KENSETSU, kensetu_tex.getText());
                mStatus.SetData(Status.INDEX_SANGYO, "");
                mStatus.SetData(Status.INDEX_SANGYO2, "");
                return true;
            } else if (kensetu_tex.getText().length() > 0) {
                JOptionPane.showMessageDialog(
                         this,
                         "建設交付番号の入力桁数を確認してください。",
                         "警告",
                         JOptionPane.WARNING_MESSAGE);
                return false;

            }
            return true;
        }
        mStatus.SetData(Status.INDEX_SANGYO, "");
        mStatus.SetData(Status.INDEX_KENSETSU, "");
        return true;
    }

    private boolean checkCarNumber() {

        if (car_id_.getText().equals("")) {
            //最低限車番の入力がないと計量させない
            return false;
        }
        if (bill_no_tex.getText().equals("")) {
            //請求先が空欄なら
//            bill_no_tex.setText("0");
            mStatus.SetData(Status.INDEX_BILL, bill_no_tex.getText());
        }
        if (car_kind_no_tex.getText().equals("")) {
            //車種が空欄なら
            car_kind_no_tex.setText("0");
            mStatus.SetData(Status.INDEX_CAR_KIND, car_kind_no_tex.getText());
        }
        return true;
    }

    private boolean checkWeightError() {

        carry_class_ = mStatus.msRemote.GetInt("carry_class");
        //目的ごとに重量値チェック
        if (選択現在重量_ > loading_weight_ && empty_weight_ > 0 && carry_class_ == 0 && 選択計量回数_ == 3) {
            //２回計量２回目の搬入なのに１回目が軽いとき
            return true;
        } else if (選択現在重量_ < loading_weight_ && empty_weight_ > 0 && carry_class_ == 1 && 選択計量回数_ == 3) {
            //２回計量の搬出なのに２回目が軽いとき
            return true;

        } else if (選択現在重量_ > loading_weight_ && empty_weight_ > 0 && carry_class_ == 0 && 選択計量回数_ == 2) {
            //風袋重量より現在重量が軽いとき 選択現在重量_ = now_weight_2;
            //２回計量１回目の搬入なのに１回目が軽いとき
            return true;
        } else if (選択現在重量_ < loading_weight_ && empty_weight_ > 0 && carry_class_ == 1 && 選択計量回数_ == 2) {
            //風袋重量より現在重量が軽いとき 選択現在重量_ = now_weight_2;
            //２回計量の搬出なのに２回目が軽いとき
            return true;
        } else if (選択現在重量_ < loading_weight_ && empty_weight_ > 0 && carry_class_ == 0 && 選択計量回数_ == 1) {
            //１回計量なのに風袋重量が重い
            return true;
        } else if (選択現在重量_ < loading_weight_ && empty_weight_ > 0 && carry_class_ == 1 && 選択計量回数_ == 1) {
            //１回計量なのに風袋重量が重い
            return true;
        }

        if (net_weight_ == 0 && empty_weight_ > 0) {
            if (!PopupMessage2("正味重量0kgです。計量しますか？", "はい", "いいえ")) {
                return true;
            }
        }
        return false;
    }

    private boolean checkWeightError2(int input_weight) {

        //目的ごとに重量値チェック
        if (0 != empty_weight_) {
            if (input_weight > now_weight_1 && carry_name_.getText().equals("搬入")) {
                return true;
            } else if (input_weight > total_weight_ && carry_name_.getText().equals("搬出")) {
                return true;

            }
        }
        return false;
    }

    private void doCancel() {
        //画面の初期化
        ClearView();
        mStatus.SetData(Status.INDEX_INPUT_CANCEL, "in");

        //処分業者にID=1を初期値としてセットする
        mStatus.SetData(Status.INDEX_CONT, "1");

        //20120117 add takisawa////////
        WeightFlag_ = false;
        OpaqueFlag_ = false;
        Status.WeightReady_ = false;
        //////////////////////////////

        //20121004 add takisawa //////
        weight_edit_ = false;
        /////////////////////////////
        契約取込flag = false;
        選択計量回数_ = 0;

        car_id_.requestFocus();
        car_id_.selectAll();
    }

    private void doCancel_log() {
        //画面の初期化
        ClearView();
        mStatus.SetData(Status.INDEX_INPUT_CANCEL, null);

        //20120117 add takisawa////////
        WeightFlag_ = false;
        OpaqueFlag_ = false;
        Status.WeightReady_ = false;
        //////////////////////////////

        //20121004 add takisawa //////
        weight_edit_ = false;
        /////////////////////////////
        契約取込flag = false;
    }

    private void getCarID() {
        //計量完了前なら
        if (!WeightFlag_ && OpaqueFlag_) {
            // 車番
            if (car_id_.getText() == null ? "" != null : !car_id_.getText().equals("")) {
                Object[] o = mStatus.mSQLID.GetCar(car_id_.getText());
                //マスタ参照
                if (o[0] != null) {
                    car_no_.setText(o[0].toString());

                    if (o[1].toString() == null ? "" != null : !o[1].toString().equals("") && !契約取込flag) {
                        ent_no_tex.setText(o[1].toString());
                    }
                    if (o[2].toString() == null ? "" != null : !o[2].toString().equals("") && !契約取込flag) {
                        ent_jLab.setText(o[2].toString());
                    }
                    if (o[3].toString() == null ? "" != null : !o[3].toString().equals("")) {
                        trans_no_tex.setText(o[3].toString());
                    }
                    if (o[4].toString() == null ? "" != null : !o[4].toString().equals("")) {
                        trans_jLab.setText(o[4].toString());
                    }
                    if (o[5].toString() == null ? "" != null : !o[5].toString().equals("") && !契約取込flag) {
                        kind_no_tex.setText(o[5].toString());
                    }
                    if (o[6].toString() == null ? "" != null : !o[6].toString().equals("") && !契約取込flag) {
                        kind_jLab.setText(o[6].toString());
                    }
                    if (o[7].toString() == null ? "" != null : !o[7].toString().equals("") && !契約取込flag) {
                        office_no_tex.setText(o[7].toString());
                    }
                    if (o[8].toString() == null ? "" != null : !o[8].toString().equals("") && !契約取込flag) {
                        office_jLab.setText(o[8].toString());
                    }
                    if (o[9].toString() == null ? "" != null : !o[9].toString().equals("") && !契約取込flag) {
                        cont_no_tex.setText(o[9].toString());
                    }
                    if (o[10].toString() == null ? "" != null : !o[10].toString().equals("") && !契約取込flag) {
                        cont_jLab.setText(o[10].toString());
                    }
                    if (o[11].toString() == null ? "" != null : !o[11].toString().equals("")) {
                        car_kind_no_tex.setText(o[11].toString());
                    }
                    if (o[12].toString() == null ? "" != null : !o[12].toString().equals("")) {
                        car_kind_jLab.setText(o[12].toString());
                    }
                    if (o[13].toString() == null ? "" != null : !o[13].toString().equals("")) {
                        empty_weight_ = Integer.valueOf(o[13].toString());
                        if (empty_weight_ > 0) {
                            選択計量回数_ = 1;
                        } else {
                            選択計量回数_ = 2;
                        }
                    }
                    if (o[14].toString() == null ? "" != null : !o[14].toString().equals("")) {
                        bill_no_tex.setText(o[14].toString());
                    }
                    if (o[15].toString() == null ? "" != null : !o[15].toString().equals("")) {
                        bill_jLab.setText(o[15].toString());
                    }

                    if (o[22].toString() == null ? "" != null : !o[22].toString().equals("")) {
                        car_id_.setText(o[22].toString());
                    }
                    mStatus.SetData(Status.INDEX_CAR_ID, car_id_.getText());

                } else {
                    選択計量回数_ = 2;
                    //記憶重量を初期化
                    loading_weight_ = 0;
                    mStatus.SetData2(Status.INDEX_CAR_ID, car_id_.getText());

                }
                cmb売上区分.setSelectedItem(get売上区分());
            }
        }
    }

    private void SelectCarAction() {

        CarSelector selector = new CarSelector(connect_);
        switch (selector.showSelector(MainPanel.this)) {
            case CarSelector.CLEARED:
                car_id_.setText("");
                car_id_.requestFocus();
                break;
            case CarSelector.SELECTED:
                Map<String, Object> item = selector.selectedRow();
                car_id_.setText(String.valueOf((Integer) item.get("car_id")));

                mStatus.SetData(Status.INDEX_CAR_ID, car_id_.getText());
                getCarID();
                car_no_.requestFocus();
                break;
            default:

        }
    }

    private void SelectEntAction() {

        EntSelector selector = new EntSelector(connect_);
        switch (selector.showSelector(MainPanel.this)) {
            case EntSelector.CLEARED:
                ent_no_tex.setText("");
                ent_jLab.setText("");
                ent_no_tex.requestFocus();
                break;
            case EntSelector.SELECTED:
                Map<String, Object> item = selector.selectedRow();
                ent_no_tex.setText(String.valueOf((Integer) item.get("id")));
                ent_jLab.setText((String) item.get("name"));
                mStatus.SetData(Status.INDEX_ENT, ent_no_tex.getText());
                ent_no_tex.requestFocus();
                break;
            default:

        }
    }

    private void SelectTransporterAction() {

        TransSelector selector = new TransSelector(connect_);
        switch (selector.showSelector(MainPanel.this)) {
            case TransSelector.CLEARED:
                trans_no_tex.setText("");
                trans_jLab.setText("");
                trans_no_tex.requestFocus();
                break;
            case TransSelector.SELECTED:
                Map<String, Object> item = selector.selectedRow();
                trans_no_tex.setText(String.valueOf((Integer) item.get("id")));
                trans_jLab.setText((String) item.get("name"));
                mStatus.SetData(Status.INDEX_TRANS, trans_no_tex.getText());
                trans_no_tex.requestFocus();
                break;
            default:

        }
    }

    private void SelectGoodsAction() {

        GoodsSelector selector = new GoodsSelector(connect_);
        switch (selector.showSelector(MainPanel.this)) {
            case GoodsSelector.CLEARED:
                kind_no_tex.setText("");
                kind_jLab.setText("");
                kind_no_tex.requestFocus();
                break;
            case GoodsSelector.SELECTED:
                Map<String, Object> item = selector.selectedRow();
                kind_no_tex.setText(String.valueOf((Integer) item.get("id")));
                kind_jLab.setText((String) item.get("name"));
                mStatus.SetData(Status.INDEX_KIND, kind_no_tex.getText());
                kind_no_tex.requestFocus();
                break;
            default:

        }
    }

    private void SelectOfficeAction() {

        if (mStatus.msRemote.GetInt("dis_ent_id") > 0) {
            OfficeSelector selector = new OfficeSelector(connect_, mStatus.msRemote.GetInt("dis_ent_id"));
            switch (selector.showSelector(MainPanel.this)) {
                case OfficeSelector.CLEARED:
                    office_sub_name_.setText("");
                    office_no_tex.requestFocus();
                    break;
                case OfficeSelector.SELECTED:
                    Map<String, Object> item = selector.selectedRow();
                    mStatus.SetData(Status.INDEX_OFFICE, String.valueOf(item.get("id")));
                    office_no_tex.requestFocus();
                    break;
                default:
            }
        } else {
            mStatus.SetData(Status.INDEX_MESSAGE, "排出業者を選択してください。");
            ent_no_tex.requestFocus();
        }

    }

    private void SelectOfficeSubAction() {

        if (mStatus.msRemote.GetInt("dis_ent_id") > 0) {
            OfficeSubNameSelector selector = new OfficeSubNameSelector(
                     connect_,
                     mStatus.msRemote.GetInt("dis_ent_id"),
                     mStatus.msRemote.GetInt("dis_office_id"));

            switch (selector.showSelector(MainPanel.this)) {
                case OfficeSelector.CLEARED:
                    office_sub_name_.setText("");
                    office_no_tex.requestFocus();
                    break;
                case OfficeSelector.SELECTED:
                    Map<String, Object> item = selector.selectedRow();
                    office_sub_name_.setText((String) item.get("sub_name"));
                    mStatus.SetData(Status.INDEX_OFFICE_SUB, office_sub_name_.getText());
                    office_no_tex.requestFocus();
                    break;
                default:
                // No operation.
                }
        } else {
            mStatus.SetData(Status.INDEX_MESSAGE, "排出業者を選択してください。");
            ent_no_tex.requestFocus();
        }

    }

    private void SelectContAction() {

        ContSelector selector = new ContSelector(connect_);
        switch (selector.showSelector(MainPanel.this)) {
            case ContSelector.CLEARED:
                cont_no_tex.setText("");
                cont_jLab.setText("");
                cont_no_tex.requestFocus();
                break;
            case ContSelector.SELECTED:
                Map<String, Object> item = selector.selectedRow();
                cont_no_tex.setText(String.valueOf((Integer) item.get("id")));
                cont_jLab.setText((String) item.get("name"));
                mStatus.SetData(Status.INDEX_CONT, cont_no_tex.getText());
                cont_no_tex.requestFocus();
                break;
            default:

        }
    }

    private void SelectCarKindAction() {

        CarKindSelector selector = new CarKindSelector(connect_);
        switch (selector.showSelector(MainPanel.this)) {
            case CarKindSelector.CLEARED:
                car_kind_no_tex.setText("");
                car_kind_jLab.setText("");
                car_kind_no_tex.requestFocus();
                break;
            case CarKindSelector.SELECTED:
                Map<String, Object> item = selector.selectedRow();
                car_kind_no_tex.setText(String.valueOf((Integer) item.get("id")));
                car_kind_jLab.setText((String) item.get("name"));
                mStatus.SetData(Status.INDEX_CAR_KIND, car_kind_no_tex.getText());
                car_kind_no_tex.requestFocus();
                break;
            default:

        }
    }

    private void SelectBillAction() {

        BillSelector selector = new BillSelector(connect_);
        switch (selector.showSelector(MainPanel.this)) {
            case BillSelector.CLEARED:
                bill_no_tex.setText("");
                bill_jLab.setText("");
                bill_no_tex.requestFocus();
                break;
            case BillSelector.SELECTED:
                Map<String, Object> item = selector.selectedRow();
                bill_no_tex.setText(String.valueOf((Integer) item.get("id")));
                bill_jLab.setText((String) item.get("name"));
                mStatus.SetData(Status.INDEX_BILL, bill_no_tex.getText());
                bill_no_tex.requestFocus();
                cmb売上区分.setSelectedItem(get売上区分());
                break;
            default:

        }
    }

    private String get売上区分() {

        int 請求先id;
        String 売上区分 = "";

        try {
            請求先id = Integer.parseInt(bill_no_tex.getText());
        } catch (NumberFormatException e) {
            return ""; // 不正入力時は空で返す
        }

        String sql
                 = "SELECT \"売上区分\" "
                 + "FROM m_bill "
                 + "WHERE id = ?";

        try (PreparedStatement ps = con_.prepareStatement(sql)) {

            ps.setInt(1, 請求先id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    売上区分 = rs.getString("売上区分");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 売上区分;
    }

    private void 計量時領収書発行() {

        int row = 1;
        if (row < 0) {
            return;
        }
        if (net_weight_ > 0) {
            領収書用途 = JOptionPane.showOptionDialog(
                     null,
                     "用途を選択してください",
                     "用途選択",
                     JOptionPane.DEFAULT_OPTION,
                     JOptionPane.QUESTION_MESSAGE,
                     null,
                     new Object[]{"産廃処理用", "販売用"},
                     "産廃処理用"
            );
        }
        if (領収書用途 < 0) {
            return;
        }

        new XlsxBillReport(XlsxBillReport.BILL_REPORT8).print(
                 con_,
                 XlsxBillReport.BILL_REPORT8,
                 null,
                 0,
                 0, 0, 0, 0
        );
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel bill_jLab;
    private final javax.swing.JTextField bill_no_tex = new aks.swing.JTextFieldKeiryo();
    private javax.swing.JButton btn絞込み;
    private javax.swing.JButton btn絞込み1;
    private javax.swing.JButton cancel_btn;
    public static final javax.swing.JTextField car_id_ = new aks.swing.JTextFieldKeiryo();
    private javax.swing.JLabel car_kind_jLab;
    private javax.swing.JTextField car_kind_no_tex;
    private javax.swing.JTextField car_no_;
    private javax.swing.JLabel carry_name_;
    private javax.swing.JTextField charge_tex;
    public static javax.swing.JComboBox<String> cmb売上区分;
    private javax.swing.JLabel cont_jLab;
    private javax.swing.JTextField cont_name01_;
    private javax.swing.JTextField cont_name02_;
    private javax.swing.JTextField cont_name03_;
    private javax.swing.JTextField cont_name04_;
    private javax.swing.JTextField cont_no_tex;
    private javax.swing.JLabel count_jLab;
    private javax.swing.JTable dataTable_;
    private javax.swing.JTable dataTable_1;
    private javax.swing.JTable dataTable_2;
    private javax.swing.JButton empty_input;
    private javax.swing.JLabel ent_jLab;
    private javax.swing.JTextField ent_no_tex;
    private javax.swing.JTextField goods_name01_;
    private javax.swing.JTextField goods_name02_;
    private javax.swing.JTextField goods_name03_;
    private javax.swing.JTextField goods_name04_;
    private javax.swing.JTextField goods_name05_;
    private javax.swing.JTextField goods_name06_;
    private javax.swing.JTextField goods_name07_;
    private javax.swing.JTextField goods_name08_;
    private javax.swing.JTextField goods_name09_;
    private javax.swing.JTextField goods_name10_;
    private javax.swing.JTextField goods_name11_;
    private javax.swing.JTextField goods_name12_;
    private javax.swing.JTextField goods_name13_;
    private javax.swing.JTextField goods_name14_;
    private javax.swing.JTextField goods_name15_;
    private javax.swing.JTextField goods_name16_;
    private javax.swing.JTextField goods_name17_;
    private javax.swing.JTextField goods_name18_;
    private javax.swing.JTextField goods_name19_;
    private javax.swing.JTextField goods_name20_;
    private javax.swing.JTextField goods_name21_;
    private javax.swing.JTextField goods_name22_;
    private javax.swing.JTextField goods_name23_;
    private javax.swing.JTextField goods_name24_;
    private javax.swing.JTextField goods_name25_;
    private javax.swing.JTextField goods_name26_;
    private javax.swing.JTextField goods_name27_;
    private javax.swing.JTextField goods_name28_;
    private javax.swing.JTextField goods_name29_;
    private javax.swing.JTextField goods_name30_;
    private javax.swing.JTextField goods_name31_;
    private javax.swing.JTextField goods_name32_;
    private javax.swing.JTextField goods_name33_;
    private javax.swing.JTextField goods_name34_;
    private javax.swing.JTextField goods_name35_;
    private javax.swing.JTextField goods_name36_;
    private javax.swing.JTextField goods_name37_;
    private javax.swing.JTextField goods_name38_;
    private javax.swing.JTextField goods_name39_;
    private javax.swing.JTextField goods_name40_;
    private javax.swing.JButton jButton8;
    private javax.swing.JButton jButton9;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel29;
    private javax.swing.JLabel jLabel30;
    private javax.swing.JLabel jLabel31;
    private javax.swing.JLabel jLabel32;
    private javax.swing.JLabel jLabel33;
    private javax.swing.JLabel jLabel_Title;
    private javax.swing.JLabel jLabel_gate_in;
    private javax.swing.JLabel jLabel_gate_out;
    private javax.swing.JLabel jLabel_loading;
    private javax.swing.JLabel jLabel_loading1;
    private javax.swing.JLabel jLbl_empty_weight;
    private javax.swing.JLabel jLbl_juuten1;
    private javax.swing.JLabel jLbl_juuten4;
    private javax.swing.JLabel jLbl_loading_weight;
    private javax.swing.JLabel jLbl_message;
    private javax.swing.JLabel jLbl_net_weight;
    private javax.swing.JLabel jLbl_now_weight1;
    private javax.swing.JLabel jLbl_total_weight;
    private javax.swing.JLabel jLbl_計量日時;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel13;
    private javax.swing.JPanel jPanel14;
    private javax.swing.JPanel jPanel15;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JPanel jPanel_Bottom;
    private javax.swing.JPanel jPanel_Bottom1;
    private javax.swing.JPanel jPanel_Bottom10;
    private javax.swing.JPanel jPanel_Bottom3;
    private javax.swing.JPanel jPanel_Bottom4;
    private javax.swing.JPanel jPanel_Center;
    private javax.swing.JPanel jPanel_Right;
    private javax.swing.JPanel jPanel_Top;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTextField kensetu_tex;
    private javax.swing.JLabel kind_jLab;
    private javax.swing.JTextField kind_no_tex;
    private javax.swing.JLabel office_jLab;
    private javax.swing.JTextField office_no_tex;
    private javax.swing.JTextField office_sub_name_;
    private javax.swing.JRadioButton rb10_;
    private javax.swing.JRadioButton rb11_;
    private javax.swing.JRadioButton rb12_;
    private javax.swing.JRadioButton rb13_;
    private javax.swing.JRadioButton rb14_;
    private javax.swing.JRadioButton rb15_;
    private javax.swing.JRadioButton rb16_;
    private javax.swing.JRadioButton rb17_;
    private javax.swing.JRadioButton rb18_;
    private javax.swing.JRadioButton rb19_;
    private javax.swing.JRadioButton rb1_;
    private javax.swing.JRadioButton rb20_;
    private javax.swing.JRadioButton rb21_;
    private javax.swing.JRadioButton rb22_;
    private javax.swing.JRadioButton rb23_;
    private javax.swing.JRadioButton rb24_;
    private javax.swing.JRadioButton rb25_;
    private javax.swing.JRadioButton rb26_;
    private javax.swing.JRadioButton rb27_;
    private javax.swing.JRadioButton rb28_;
    private javax.swing.JRadioButton rb29_;
    private javax.swing.JRadioButton rb2_;
    private javax.swing.JRadioButton rb30_;
    private javax.swing.JRadioButton rb31_;
    private javax.swing.JRadioButton rb32_;
    private javax.swing.JRadioButton rb33_;
    private javax.swing.JRadioButton rb34_;
    private javax.swing.JRadioButton rb35_;
    private javax.swing.JRadioButton rb36_;
    private javax.swing.JRadioButton rb37_;
    private javax.swing.JRadioButton rb38_;
    private javax.swing.JRadioButton rb39_;
    private javax.swing.JRadioButton rb3_;
    private javax.swing.JRadioButton rb40_;
    private javax.swing.JRadioButton rb4_;
    private javax.swing.JRadioButton rb5_;
    private javax.swing.JRadioButton rb6_;
    private javax.swing.JRadioButton rb7_;
    private javax.swing.JRadioButton rb8_;
    private javax.swing.JRadioButton rb9_;
    private javax.swing.JTextField sanpai_tex;
    private javax.swing.JTextField searchWordField1_;
    private javax.swing.JTextField searchWordField1_1;
    private javax.swing.JTextField searchWordField2_;
    private javax.swing.JTextField searchWordField2_1;
    private javax.swing.JLabel sub_message_;
    private javax.swing.JLabel trans_jLab;
    private javax.swing.JTextField trans_name01_;
    private javax.swing.JTextField trans_name02_;
    private javax.swing.JTextField trans_name03_;
    private javax.swing.JTextField trans_name04_;
    private javax.swing.JTextField trans_name05_;
    private final javax.swing.JTextField trans_no_tex = new aks.swing.JTextFieldKeiryo();
    private javax.swing.JRadioButton trb1_;
    private javax.swing.JRadioButton trb2_;
    private javax.swing.JRadioButton trb3_;
    private javax.swing.JRadioButton trb4_;
    private javax.swing.JRadioButton trb5_;
    private javax.swing.JLabel txt契約内容表示;
    private javax.swing.JTextField txt請求数量;
    private javax.swing.JTextField unit_value_tex;
    private javax.swing.JButton weight_btn3;
    private javax.swing.JButton weight_btn_2回計量;
    private javax.swing.JButton weight_btn_登録処理;
    private javax.swing.JButton weight_btn_計量処理;
    private javax.swing.JButton weight_btn_重量初期化;
    private javax.swing.JButton weight_btn_重量取込;
    private javax.swing.JButton weight_btn_風袋登録;
    // End of variables declaration//GEN-END:variables

    private void clearList() {
        for (int i = 0; i < 5; i++) {
            trans_id[i] = 0;
        }
        for (int i = 0; i < 40; i++) {
            goods_id[i] = 0;
            goods_name[i] = "";
        }
        searchWordField1_.setText("");
        searchWordField1_1.setText("");
        searchWordField2_.setText("");
        searchWordField2_1.setText("");
    }

    // 漢字モードの設定
    private void setKanjiMode(javax.swing.JTextField jt) {
        Character.Subset[] subsets = {InputSubset.KANJI};
        jt.getInputContext().setCompositionEnabled(true);
        jt.getInputContext().setCharacterSubsets(subsets);
    }

    // 半角英数字モードの設定
    private void setAsciiMode(javax.swing.JTextField jt) {
        Character.Subset[] subsets = {InputSubset.LATIN};
        jt.getInputContext().setCompositionEnabled(false);
        jt.getInputContext().setCharacterSubsets(subsets);
    }

    // カタカナモードの設定
    private void setKatakanaMode(javax.swing.JTextField jt) {
        Character.Subset[] subsets = {java.awt.im.InputSubset.HALFWIDTH_KATAKANA};
        jt.getInputContext().setCompositionEnabled(true);
        jt.getInputContext().setCharacterSubsets(subsets);
    }
}
