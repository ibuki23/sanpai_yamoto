package aks.industrialwaste.recycle.ui.weighlist;

import java.awt.event.FocusListener;
import java.awt.event.FocusEvent;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.event.AncestorEvent;
import aks.industrialwaste.recycle.report.XlsxBillReport;
import aks.industrialwaste.recycle.ui.MainFrame;
import aks.industrialwaste.database.Column;
import aks.industrialwaste.database.ColumnType;
import aks.industrialwaste.database.CombineOperator;
import aks.industrialwaste.database.CompareOperator;
import aks.industrialwaste.database.DBManager;
import aks.industrialwaste.database.SearchCondition;
import aks.industrialwaste.database.SortCondition;
import aks.industrialwaste.database.SortOrder;
import jp.co.astax.swing.DateTimeField;
import aks.util.FontUtility;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import aks.swing.JTextField;
import com.ibm.icu.text.Transliterator;
import javax.swing.table.*;
import javax.swing.JTabbedPane;
import aks.industrialwaste.recycle.ui.manifesto.KennsetuManifestoPanel100;
import aks.industrialwaste.recycle.ui.manifesto.SanngyouManifestoPanel100;
import aks.industrialwaste.ui.DialogHelper;
import aks.industrialwaste.ui.ListPanel;
import aks.industrialwaste.util.TermDate;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.WindowConstants;

/**
 * 計量実績データを一覧表示する.
 *
 * @version 1.00
 * @author AKS.Takisawa
 */
public class WeighList extends ListPanel implements Runnable {

    public static int 領収書用途;
    private List<SearchCondition> searchCondition_;
    private List<SortCondition> sortCondition_;
    private WeighListTableModel model_;
    private Thread thread_ = null;
    private DateTimeField startDateField_;
    private DateTimeField endDateField_;
    private JCheckBox autoUpdateSwitch_;
    private JCheckBox carryInSwitch_;
    private JTextField searchWordField_;
    protected JLabel dealingsWeightField_LO_;
    protected JLabel dealingsWeightField_LN_;
    protected JLabel dealingsWeightField_LA_;
    private Transliterator tr1_;
    private Transliterator tr2_;
    private Connection con_;
    private JTabbedPane tabbedPane_;
    private String[] cmb_list_;
    private JComboBox serchCmb_;
    private JPopupMenu popup;
    private JCheckBox unitDifferenceSwitch_;

    public WeighList(Connection con) {
        model_ = new WeighListTableModel(con);
        dataTable_.setModel(model_);
        if (DBManager.mode == 4 || DBManager.mode == 14) {
            dataTable_.setColumnModel(new WeighListTableColumnModelSeikyu());
        } else {
            dataTable_.setColumnModel(new WeighListTableColumnModel());
        }
        con_ = con;
        searchCondition_
                 = Collections.synchronizedList(new ArrayList<>());
        sortCondition_
                 = Collections.synchronizedList(new ArrayList<>());
        // 20110405 add
        thread_ = new Thread(WeighList.this);
        thread_.start();

    }

    @Override
    public void ancestorRemoved(AncestorEvent event) {
        model_.clear();

        super.ancestorRemoved(event);
    }

    @Override
    protected void initComponents() {
        super.initComponents();

        tr1_ = Transliterator.getInstance("Hiragana-Katakana");
        tr2_ = Transliterator.getInstance("Fullwidth-Halfwidth");

        popup = new JPopupMenu();// ポップアップメニューを生成

        Font font14 = FontUtility.getFont("メイリオ", Font.PLAIN, 14);

        // タイトル
        titleField_.setText("計量記録一覧");
        //抽出日
        JPanel panel = new JPanel();
        panel.setLayout(new java.awt.GridBagLayout());
        startDateField_ = new DateTimeField(DateTimeField.DateFormat.YMD);
        startDateField_.setFont(font14);
        startDateField_.setMinimumSize(new Dimension(250, 30));
        startDateField_.setPreferredSize(new Dimension(250, 30));
        startDateField_.addFocusListener(new ColumnUpdater());
        addContainer(
                 panel, startDateField_,
                 0, 0, 1, 1, 0.0, 0.0,
                 GridBagConstraints.WEST, GridBagConstraints.NONE,
                 new Insets(2, 2, 2, 0), 0, 0);
        JLabel label = new JLabel(" - ", JLabel.LEFT);
        label.setFont(font14);
        addContainer(
                 panel, label,
                 1, 0, 1, 1, 0.0, 0.0,
                 GridBagConstraints.WEST, GridBagConstraints.NONE,
                 new Insets(2, 2, 2, 0), 0, 0);

        //抽出日
        endDateField_ = new DateTimeField(DateTimeField.DateFormat.YMD);
        endDateField_.setFont(font14);
        endDateField_.setMinimumSize(new Dimension(250, 30));
        endDateField_.setPreferredSize(new Dimension(250, 30));
        endDateField_.addFocusListener(new ColumnUpdater());
        addContainer(
                 panel, endDateField_,
                 2, 0, 1, 1, 0.0, 0.0,
                 GridBagConstraints.WEST, GridBagConstraints.NONE,
                 new Insets(2, 2, 2, 0), 0, 0);

        // 再表示ボタン
        JButton b = new JButton("再表示");
        b.setFont(FontUtility.getFont("メイリオ", Font.PLAIN, 14));
        b.setMargin(new Insets(2, 2, 2, 2));
        b.setMinimumSize(new Dimension(120, 30));
        b.setPreferredSize(new Dimension(120, 30));
        b.addActionListener(new RefreshAction());
        addContainer(
                 panel, b,
                 3, 0, 1, 1, 0.0, 0.0,
                 GridBagConstraints.WEST, GridBagConstraints.NONE,
                 new Insets(2, 20, 2, 2), 0, 0);
        //自動更新
        autoUpdateSwitch_ = new JCheckBox("自動更新");
        autoUpdateSwitch_.setFont(font14);
        autoUpdateSwitch_.setMargin(new Insets(0, 0, 0, 0));
        autoUpdateSwitch_.setSelected(true);
        autoUpdateSwitch_.addActionListener(
                 new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent event) {
                if (autoUpdateSwitch_.isSelected()) {
                    startDateField_.setEnabled(false);
                    endDateField_.setEnabled(false);
                    thread_ = new Thread(WeighList.this);
                    thread_.start();
                } else {
                    thread_.interrupt();
                    thread_ = null;
                    startDateField_.setEnabled(true);
                    endDateField_.setEnabled(true);
                }
            }
        });
        addContainer(
                 panel, autoUpdateSwitch_,
                 4, 0, 1, 1, 0.0, 0.0,
                 GridBagConstraints.WEST, GridBagConstraints.NONE,
                 new Insets(2, 10, 2, 2), 0, 0);

        //搬入のみ
        carryInSwitch_ = new JCheckBox("搬入のみ");
        carryInSwitch_.setFont(font14);
        carryInSwitch_.setMargin(new Insets(0, 0, 0, 0));

        //単価違い
        unitDifferenceSwitch_ = new JCheckBox("単価違い");
        unitDifferenceSwitch_.setFont(font14);
        unitDifferenceSwitch_.setMargin(new Insets(0, 0, 0, 0));
        unitDifferenceSwitch_.setSelected(false);
        unitDifferenceSwitch_.addActionListener(
                 new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent event) {
                updateView();
            }
        });
        addContainer(
                 panel, unitDifferenceSwitch_,
                 6, 0, 1, 1, 0.0, 0.0,
                 GridBagConstraints.WEST, GridBagConstraints.NONE,
                 new Insets(2, 10, 2, 2), 0, 0);
        //検索
        JLabel lbl = new JLabel("検索");
        lbl.setFont(font14);
        panel.add(lbl);
        addContainer(
                 panel, lbl,
                 7, 0, 1, 1, 1.0, 0.0,
                 GridBagConstraints.EAST, GridBagConstraints.NONE,
                 new Insets(2, 2, 2, 2), 0, 0);

        cmb_list_ = new String[]{"指定無し", "車番", "請求先", "排出事業者", "運搬業者", "品目", "事業場", "処分業者", "ﾏﾆﾌｪｽﾄ無し", "料金設定無し", "伝票NO(ｾﾞﾛ埋め)"};
        serchCmb_ = new JComboBox();
        serchCmb_.setModel(new javax.swing.DefaultComboBoxModel(cmb_list_));
        serchCmb_.setFont(font14);
        serchCmb_.setMaximumRowCount(10);
        serchCmb_.setMinimumSize(new Dimension(150, 30));
        serchCmb_.setPreferredSize(new Dimension(150, 30));
        addContainer(
                 panel, serchCmb_,
                 8, 0, 1, 1, 0.1, 0.0,
                 GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL,
                 new Insets(0, 0, 0, 0), 0, 0);

        addContainer(
                 northPane_, panel,
                 0, 1, 1, 1, 0.0, 0.0,
                 GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL,
                 new Insets(0, 0, 0, 0), 0, 0);

        searchWordField_ = new JTextField();
        searchWordField_.setFont(font14);
        searchWordField_.setMargin(new Insets(0, 0, 0, 0));
        searchWordField_.setMinimumSize(new Dimension(120, 30));
        searchWordField_.setPreferredSize(new Dimension(120, 30));
        searchWordField_.addActionListener(new SearchAction());
        searchWordField_.addFocusListener(new java.awt.event.FocusAdapter() {

            @Override
            public void focusLost(java.awt.event.FocusEvent e) {
                updateView();
            }

            @Override
            public void focusGained(java.awt.event.FocusEvent evt) {
            }
        });

        addContainer(
                 northPane_, searchWordField_,
                 1, 1, 1, 1, 0.1, 0.0,
                 GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL,
                 new Insets(0, 0, 0, 0), 0, 0);

        //右クリック時のPOPアップメニュー作成///////////////////////////////////////
        JMenuItem item00 = new JMenuItem("最新の情報に更新", MainFrame.icon_reload);
        JMenuItem item0 = new JMenuItem("ﾃﾞｰﾀ訂正", MainFrame.icon_edit);
        JMenuItem item1 = new JMenuItem("計量票印刷");
        JMenuItem item2 = new JMenuItem("ﾏﾆﾌｪｽﾄ印刷", MainFrame.icon_print);
        JMenuItem item4 = new JMenuItem("ﾃﾞｰﾀ削除", MainFrame.icon_delete);
        JMenuItem item5 = new JMenuItem("ﾃﾞｰﾀ複写");
        JMenuItem item6 = new JMenuItem("ﾃﾞｰﾀ出力");
        popup.add(item00);
        popup.add(item0);
        popup.add(item5);
        popup.add(item1);
        popup.add(item2);
        popup.add(item6);
        popup.addSeparator(); //セパレーター

        popup.add(item4);

        // 各メニュー選択時の処理
        item00.addActionListener(new RefreshAction());
        item0.addActionListener(new EditAction());
        item1.addActionListener(new PrintAction());
        item2.addActionListener(new ManifestAction());
        item4.addActionListener(new DeleteAction());
        item5.addActionListener(new CopyAction());
        item6.addActionListener(new ReportAction());

        //右クリックメニュー対応
        dataTable_.addMouseListener(
                 new MouseAdapter() {

            @Override
            public void mouseClicked(final MouseEvent me) {
                if (javax.swing.SwingUtilities.isRightMouseButton(me)) {
                    // 右クリック時の処理
                    if (SwingUtilities.isRightMouseButton(me)) {
                        popup.show(me.getComponent(), me.getX(), me.getY());
                    }
                } else if (javax.swing.SwingUtilities.isMiddleMouseButton(me)) {
                    // 中ボタンクリック時の処理
                } else if (javax.swing.SwingUtilities.isLeftMouseButton(me)) {
                    // 左クリック時の処理
                }
            }
        });
        ///////////////////////////////////////////////右クリックメニュー実装終了///

        // データテーブル
        dataTable_.setFont(font14);
        dataTable_.setRowHeight(30);
        dataTable_.setColumnSelectionAllowed(false);
        dataTable_.setRowSelectionAllowed(true);
        dataTable_.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        dataTable_.getTableHeader().setReorderingAllowed(false);
        dataTable_.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

        JTableHeader jh = dataTable_.getTableHeader();
        jh.setFont(font14);
        // ボタン
        // 追加
        b = new JButton("ﾃﾞｰﾀ追加");
        b.setFont(FontUtility.getFont("メイリオ", Font.PLAIN, 14));
        b.setMargin(new Insets(2, 2, 2, 2));
        b.setMinimumSize(new Dimension(150, 30));
        b.setPreferredSize(new Dimension(150, 30));
        b.addActionListener(new AppendAction());
        addContainer(
                 southPane_, b,
                 0, 0, 1, 1, 0.0, 0.0,
                 GridBagConstraints.WEST, GridBagConstraints.NONE,
                 new Insets(2, 2, 2, 2), 0, 0);
        // 訂正
        b = new JButton("ﾃﾞｰﾀ訂正");
        b.setFont(FontUtility.getFont("メイリオ", Font.PLAIN, 14));
        b.setMargin(new Insets(2, 2, 2, 2));
        b.setMinimumSize(new Dimension(150, 30));
        b.setPreferredSize(new Dimension(150, 30));
        b.addActionListener(new EditAction());
        addContainer(
                 southPane_, b,
                 1, 0, 1, 1, 0.0, 0.0,
                 GridBagConstraints.WEST, GridBagConstraints.NONE,
                 new Insets(2, 2, 2, 2), 0, 0);

        // データ複写
        b = new JButton("ﾃﾞｰﾀ複写");
        b.setFont(FontUtility.getFont("メイリオ", Font.PLAIN, 14));
        b.setMargin(new Insets(2, 2, 2, 2));
        b.setMinimumSize(new Dimension(150, 30));
        b.setPreferredSize(new Dimension(150, 30));
        b.addActionListener(new CopyAction());
        addContainer(
                 southPane_, b,
                 2, 0, 1, 1, 0.0, 0.0,
                 GridBagConstraints.WEST, GridBagConstraints.NONE,
                 new Insets(2, 2, 2, 2), 0, 0);

        //   計量票印刷
        b = new JButton("計量票印刷");
        b.setFont(FontUtility.getFont("メイリオ", Font.PLAIN, 14));
        b.setMargin(new Insets(2, 2, 2, 2));
        b.setMinimumSize(new Dimension(150, 30));
        b.setPreferredSize(new Dimension(150, 30));
        b.addActionListener(new PrintAction());
        addContainer(
                 southPane_, b,
                 3, 0, 1, 1, 0.0, 0.0,
                 GridBagConstraints.WEST, GridBagConstraints.NONE,
                 new Insets(2, 2, 2, 2), 0, 0);

        // マニフェスト
        b = new JButton("ﾏﾆﾌｪｽﾄ印刷");
        b.setFont(FontUtility.getFont("メイリオ", Font.PLAIN, 14));
        b.setMargin(new Insets(0, 0, 0, 0));
        b.setMinimumSize(new Dimension(150, 30));
        b.setPreferredSize(new Dimension(150, 30));
        b.addActionListener(new ManifestAction());
        addContainer(
                 southPane_, b,
                 5, 0, 1, 1, 0.0, 0.0,
                 GridBagConstraints.WEST, GridBagConstraints.NONE,
                 new Insets(2, 2, 2, 2), 0, 0);

        // マニフェスト
        b = new JButton("ﾏﾆﾌｪｽﾄ空伝");
        b.setFont(FontUtility.getFont("メイリオ", Font.PLAIN, 14));
        b.setMargin(new Insets(0, 0, 0, 0));
        b.setMinimumSize(new Dimension(150, 30));
        b.setPreferredSize(new Dimension(150, 30));
        b.addActionListener(new ManifestAction_kara());

        // データ出力
        b = new JButton("ﾃﾞｰﾀ出力");
        b.setFont(FontUtility.getFont("メイリオ", Font.PLAIN, 14));
        b.setMargin(new Insets(0, 0, 0, 0));
        b.setMinimumSize(new Dimension(150, 30));
        b.setPreferredSize(new Dimension(150, 30));
        b.addActionListener(new ReportAction());
        addContainer(
                 southPane_, b,
                 6, 0, 1, 1, 0.0, 0.0,
                 GridBagConstraints.WEST, GridBagConstraints.NONE,
                 new Insets(2, 2, 2, 2), 0, 0);

        b = new JButton("領収書発行");
        b.setFont(FontUtility.getFont("メイリオ", Font.PLAIN, 14));
        b.setMargin(new Insets(2, 2, 2, 2));
        b.setMinimumSize(new Dimension(150, 30));
        b.setPreferredSize(new Dimension(150, 30));
        b.addActionListener(new 領収書発行());
        addContainer(
                 southPane_, b,
                 7, 0, 1, 1, 0.0, 0.0,
                 GridBagConstraints.WEST, GridBagConstraints.NONE,
                 new Insets(2, 2, 2, 2), 0, 0);

        //   全件送信（本社へ送信準備）計量モードのみ
        b = new JButton("電子ﾏﾆﾌｪｽﾄ区分");
        b.setFont(FontUtility.getFont("メイリオ", Font.PLAIN, 14));
        b.setMargin(new Insets(2, 2, 2, 2));
        b.setMinimumSize(new Dimension(150, 30));
        b.setPreferredSize(new Dimension(150, 30));
        b.addActionListener(new HideAction());
        addContainer(
                 southPane_, b,
                 8, 0, 1, 1, 0.0, 0.0,
                 GridBagConstraints.WEST, GridBagConstraints.NONE,
                 new Insets(2, 2, 2, 2), 0, 0);

        //ダミー
        addContainer(
                 southPane_, new JPanel(),
                 10, 0, 1, 1, 1.0, 0.0,
                 GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
                 new Insets(2, 2, 2, 2), 0, 0);

        // 削除
        b = new JButton("ﾃﾞｰﾀ削除");
        b.setFont(FontUtility.getFont("メイリオ", Font.PLAIN, 14));
        b.setMargin(new Insets(2, 2, 2, 2));
        b.setMinimumSize(new Dimension(150, 30));
        b.setPreferredSize(new Dimension(150, 30));
        b.addActionListener(new DeleteAction());
        addContainer(
                 southPane_, b,
                 20, 0, 1, 1, 0.0, 0.0,
                 GridBagConstraints.WEST, GridBagConstraints.NONE,
                 new Insets(2, 2, 2, 2), 0, 0);

    }

    @Override
    protected void updateView() {

        if (null == startDateField_.getValue()) {
            startDateField_.setValue(Calendar.getInstance());
        }
        if (null == endDateField_.getValue()) {
            endDateField_.setValue(Calendar.getInstance());
        }

        //20110603 add
        String searchWordTemp;
        searchWordTemp = tr1_.transliterate(searchWordField_.getText().trim());
        searchWordTemp = tr2_.transliterate(searchWordTemp);

        Date startDate = startDateField_.getValueAtDate();
        Date endTime = endDateField_.getValueAtDate();
        // 検索条件, ソート条件を設定
        SearchCondition[] search;
        if (searchWordField_.getText().equals("")) {
            if (!carryInSwitch_.isSelected() && serchCmb_.getSelectedIndex() == 8) {

                //NC
                //マニフェスト無の検索に電子を加えないようにする
                search
                         = new SearchCondition[]{
                             new SearchCondition(
                                      new Column("時間", "term_date", ColumnType.DATE),
                                      CompareOperator.GE,
                                      startDate,
                                      CombineOperator.AND),
                             new SearchCondition(
                                      new Column("時間", "term_date", ColumnType.DATE),
                                      CompareOperator.LE,
                                      endTime,
                                      CombineOperator.AND),
                             new SearchCondition(
                                      new Column("電子MF", "option1", ColumnType.INT4),
                                      CompareOperator.EQ,
                                      "0",
                                      CombineOperator.AND),
                             new SearchCondition(
                                      new Column("マニフェスト有り無し", "manifesto_no", ColumnType.TEXT),
                                      CompareOperator.EQ,
                                      "",
                                      CombineOperator.NONE)};
            } else if (carryInSwitch_.isSelected() && serchCmb_.getSelectedIndex() == 8) {
                search
                         = new SearchCondition[]{
                             new SearchCondition(
                                      new Column("時間", "term_date", ColumnType.DATE),
                                      CompareOperator.GE,
                                      startDate,
                                      CombineOperator.AND),
                             new SearchCondition(
                                      new Column("時間", "term_date", ColumnType.DATE),
                                      CompareOperator.LE,
                                      endTime,
                                      CombineOperator.AND),
                             new SearchCondition(
                                      new Column("マニフェスト有り無し", "manifesto_no", ColumnType.TEXT),
                                      CompareOperator.EQ,
                                      "",
                                      CombineOperator.AND),
                             new SearchCondition(
                                      new Column("搬入出", "carry_class", ColumnType.INT4),
                                      CompareOperator.EQ,
                                      0,
                                      CombineOperator.NONE)};
            } else if (!carryInSwitch_.isSelected() && serchCmb_.getSelectedIndex() == 9) {

                search
                         = new SearchCondition[]{
                             new SearchCondition(
                                      new Column("時間", "term_date", ColumnType.DATE),
                                      CompareOperator.GE,
                                      startDate,
                                      CombineOperator.AND),
                             new SearchCondition(
                                      new Column("時間", "term_date", ColumnType.DATE),
                                      CompareOperator.LE,
                                      endTime,
                                      CombineOperator.AND),
                             new SearchCondition(
                                      new Column("料金設定無し", "charge_value", ColumnType.INT4),
                                      CompareOperator.EQ,
                                      0,
                                      CombineOperator.NONE)};
            } else if (carryInSwitch_.isSelected() && serchCmb_.getSelectedIndex() == 9) {
                search
                         = new SearchCondition[]{
                             new SearchCondition(
                                      new Column("時間", "term_date", ColumnType.DATE),
                                      CompareOperator.GE,
                                      startDate,
                                      CombineOperator.AND),
                             new SearchCondition(
                                      new Column("時間", "term_date", ColumnType.DATE),
                                      CompareOperator.LE,
                                      endTime,
                                      CombineOperator.AND),
                             new SearchCondition(
                                      new Column("料金設定無し", "charge_value", ColumnType.INT4),
                                      CompareOperator.EQ,
                                      0,
                                      CombineOperator.AND),
                             new SearchCondition(
                                      new Column("搬入出", "carry_class", ColumnType.INT4),
                                      CompareOperator.EQ,
                                      0,
                                      CombineOperator.NONE)};
            } else if (carryInSwitch_.isSelected() && serchCmb_.getSelectedIndex() == 2) {
                search
                         = new SearchCondition[]{
                             new SearchCondition(
                                      new Column("時間", "term_date", ColumnType.DATE),
                                      CompareOperator.GE,
                                      startDate,
                                      CombineOperator.AND),
                             new SearchCondition(
                                      new Column("時間", "term_date", ColumnType.DATE),
                                      CompareOperator.LE,
                                      endTime,
                                      CombineOperator.AND),
                             new SearchCondition(
                                      new Column("請求先設定無し", "bill_id", ColumnType.INT4),
                                      CompareOperator.EQ,
                                      0,
                                      CombineOperator.AND),
                             new SearchCondition(
                                      new Column("搬入出", "carry_class", ColumnType.INT4),
                                      CompareOperator.EQ,
                                      0,
                                      CombineOperator.NONE)};
            } else if (!carryInSwitch_.isSelected() && serchCmb_.getSelectedIndex() == 2) {
                search
                         = new SearchCondition[]{
                             new SearchCondition(
                                      new Column("時間", "term_date", ColumnType.DATE),
                                      CompareOperator.GE,
                                      startDate,
                                      CombineOperator.AND),
                             new SearchCondition(
                                      new Column("時間", "term_date", ColumnType.DATE),
                                      CompareOperator.LE,
                                      endTime,
                                      CombineOperator.AND),
                             new SearchCondition(
                                      new Column("請求先設定無し", "bill_id", ColumnType.INT4),
                                      CompareOperator.EQ,
                                      0,
                                      CombineOperator.NONE)};
            } else if (!carryInSwitch_.isSelected() && unitDifferenceSwitch_.isSelected()) {
                search
                         = new SearchCondition[]{
                             new SearchCondition(
                                      new Column("時間", "term_date", ColumnType.DATE),
                                      CompareOperator.GE,
                                      startDate,
                                      CombineOperator.AND),
                             new SearchCondition(
                                      new Column("時間", "term_date", ColumnType.DATE),
                                      CompareOperator.LE,
                                      endTime,
                                      CombineOperator.AND),
                             new SearchCondition(
                                      new Column("単価違い", "unit_difference_flag", ColumnType.BOOLEAN),
                                      CompareOperator.EQ,
                                      true,
                                      CombineOperator.NONE)

                         };
            } else if (carryInSwitch_.isSelected() && unitDifferenceSwitch_.isSelected()) {
                search
                         = new SearchCondition[]{
                             new SearchCondition(
                                      new Column("時間", "term_date", ColumnType.DATE),
                                      CompareOperator.GE,
                                      startDate,
                                      CombineOperator.AND),
                             new SearchCondition(
                                      new Column("時間", "term_date", ColumnType.DATE),
                                      CompareOperator.LE,
                                      endTime,
                                      CombineOperator.AND),
                             new SearchCondition(
                                      new Column("搬入出", "carry_class", ColumnType.INT4),
                                      CompareOperator.EQ,
                                      0,
                                      CombineOperator.AND),
                             new SearchCondition(
                                      new Column("単価違い", "unit_difference_flag", ColumnType.BOOLEAN),
                                      CompareOperator.EQ,
                                      true,
                                      CombineOperator.NONE)

                         };
            } else if (!carryInSwitch_.isSelected()) {
                search
                         = new SearchCondition[]{
                             new SearchCondition(
                                      new Column("時間", "term_date", ColumnType.DATE),
                                      CompareOperator.GE,
                                      startDate,
                                      CombineOperator.AND),
                             new SearchCondition(
                                      new Column("時間", "term_date", ColumnType.DATE),
                                      CompareOperator.LE,
                                      endTime,
                                      CombineOperator.NONE)};
            } else {
                search
                         = new SearchCondition[]{
                             new SearchCondition(
                                      new Column("時間", "term_date", ColumnType.DATE),
                                      CompareOperator.GE,
                                      startDate,
                                      CombineOperator.AND),
                             new SearchCondition(
                                      new Column("時間", "term_date", ColumnType.DATE),
                                      CompareOperator.LE,
                                      endTime,
                                      CombineOperator.AND),
                             new SearchCondition(
                                      new Column("搬入出", "carry_class", ColumnType.INT4),
                                      CompareOperator.EQ,
                                      0,
                                      CombineOperator.NONE)};
            }
        } else {
            search = null;

            switchLabel01:
            switch (serchCmb_.getSelectedIndex()) {
                case 0:
                    if (!carryInSwitch_.isSelected()) {
                        search
                                 = new SearchCondition[]{
                                     new SearchCondition(
                                              new Column("時間", "term_date", ColumnType.DATE),
                                              CompareOperator.GE,
                                              startDate,
                                              CombineOperator.AND),
                                     new SearchCondition(
                                              new Column("時間", "term_date", ColumnType.DATE),
                                              CompareOperator.LE,
                                              endTime,
                                              CombineOperator.NONE)};
                    } else {
                        search
                                 = new SearchCondition[]{
                                     new SearchCondition(
                                              new Column("時間", "term_date", ColumnType.DATE),
                                              CompareOperator.GE,
                                              startDate,
                                              CombineOperator.AND),
                                     new SearchCondition(
                                              new Column("時間", "term_date", ColumnType.DATE),
                                              CompareOperator.LE,
                                              endTime,
                                              CombineOperator.AND),
                                     new SearchCondition(
                                              new Column("搬入出", "carry_class", ColumnType.INT4),
                                              CompareOperator.EQ,
                                              0,
                                              CombineOperator.NONE)};
                    }
                    break;

                case 1:
                    try {
                    Object o = Integer.parseInt(searchWordField_.getText().trim());
                    if (!carryInSwitch_.isSelected()) {
                        search
                                 = new SearchCondition[]{
                                     new SearchCondition(
                                              new Column("時間", "term_date", ColumnType.DATE),
                                              CompareOperator.GE,
                                              startDate,
                                              CombineOperator.AND),
                                     new SearchCondition(
                                              new Column("時間", "term_date", ColumnType.DATE),
                                              CompareOperator.LE,
                                              endTime,
                                              CombineOperator.AND),
                                     new SearchCondition(
                                              new Column("車番", "car_no", ColumnType.INT4),
                                              CompareOperator.EQ,
                                              o,
                                              CombineOperator.NONE)};
                    } else {
                        search
                                 = new SearchCondition[]{
                                     new SearchCondition(
                                              new Column("時間", "term_date", ColumnType.DATE),
                                              CompareOperator.GE,
                                              startDate,
                                              CombineOperator.AND),
                                     new SearchCondition(
                                              new Column("時間", "term_date", ColumnType.DATE),
                                              CompareOperator.LE,
                                              endTime,
                                              CombineOperator.AND),
                                     new SearchCondition(
                                              new Column("車番", "car_no", ColumnType.INT4),
                                              CompareOperator.EQ,
                                              o,
                                              CombineOperator.AND),
                                     new SearchCondition(
                                              new Column("搬入出", "carry_class", ColumnType.INT4),
                                              CompareOperator.EQ,
                                              0,
                                              CombineOperator.NONE)};
                    }
                } catch (Exception e) {
                    search
                             = new SearchCondition[]{
                                 new SearchCondition(
                                          new Column("時間", "term_date", ColumnType.DATE),
                                          CompareOperator.GE,
                                          startDate,
                                          CombineOperator.AND),
                                 new SearchCondition(
                                          new Column("時間", "term_date", ColumnType.DATE),
                                          CompareOperator.LE,
                                          endTime,
                                          CombineOperator.AND),
                                 new SearchCondition(
                                          new Column("搬入出", "carry_class", ColumnType.INT4),
                                          CompareOperator.EQ,
                                          0,
                                          CombineOperator.NONE)};
                }
                break;

                case 2:
                    if (!carryInSwitch_.isSelected()) {
                        search
                                 = new SearchCondition[]{
                                     new SearchCondition(
                                              new Column("時間", "term_date", ColumnType.DATE),
                                              CompareOperator.GE,
                                              startDate,
                                              CombineOperator.AND),
                                     new SearchCondition(
                                              new Column("時間", "term_date", ColumnType.DATE),
                                              CompareOperator.LE,
                                              endTime,
                                              CombineOperator.AND),
                                     new SearchCondition(
                                              new Column("請求先名", "bill_name", ColumnType.TEXT),
                                              CompareOperator.LIKE,
                                              searchWordField_.getText().trim(),
                                              CombineOperator.NONE)};
                    } else {
                        search
                                 = new SearchCondition[]{
                                     new SearchCondition(
                                              new Column("時間", "term_date", ColumnType.DATE),
                                              CompareOperator.GE,
                                              startDate,
                                              CombineOperator.AND),
                                     new SearchCondition(
                                              new Column("時間", "term_date", ColumnType.DATE),
                                              CompareOperator.LE,
                                              endTime,
                                              CombineOperator.AND),
                                     new SearchCondition(
                                              new Column("請求先名", "bill_name", ColumnType.TEXT),
                                              CompareOperator.LIKE,
                                              searchWordField_.getText().trim(),
                                              CombineOperator.AND),
                                     new SearchCondition(
                                              new Column("搬入出", "carry_class", ColumnType.INT4),
                                              CompareOperator.EQ,
                                              0,
                                              CombineOperator.NONE)};
                    }
                    break;

                case 3:
                    if (!carryInSwitch_.isSelected()) {
                        search
                                 = new SearchCondition[]{
                                     new SearchCondition(
                                              new Column("時間", "term_date", ColumnType.DATE),
                                              CompareOperator.GE,
                                              startDate,
                                              CombineOperator.AND),
                                     new SearchCondition(
                                              new Column("時間", "term_date", ColumnType.DATE),
                                              CompareOperator.LE,
                                              endTime,
                                              CombineOperator.AND),
                                     new SearchCondition(
                                              new Column("排出事業者", "entrepreneur_name", ColumnType.TEXT),
                                              CompareOperator.LIKE,
                                              searchWordField_.getText().trim(),
                                              CombineOperator.NONE)};
                    } else {
                        search
                                 = new SearchCondition[]{
                                     new SearchCondition(
                                              new Column("時間", "term_date", ColumnType.DATE),
                                              CompareOperator.GE,
                                              startDate,
                                              CombineOperator.AND),
                                     new SearchCondition(
                                              new Column("時間", "term_date", ColumnType.DATE),
                                              CompareOperator.LE,
                                              endTime,
                                              CombineOperator.AND),
                                     new SearchCondition(
                                              new Column("排出事業者", "entrepreneur_name", ColumnType.TEXT),
                                              CompareOperator.LIKE,
                                              searchWordField_.getText().trim(),
                                              CombineOperator.AND),
                                     new SearchCondition(
                                              new Column("搬入出", "carry_class", ColumnType.INT4),
                                              CompareOperator.EQ,
                                              0,
                                              CombineOperator.NONE)};
                    }
                    break;
                case 4:
                    if (!carryInSwitch_.isSelected()) {
                        search
                                 = new SearchCondition[]{
                                     new SearchCondition(
                                              new Column("時間", "term_date", ColumnType.DATE),
                                              CompareOperator.GE,
                                              startDate,
                                              CombineOperator.AND),
                                     new SearchCondition(
                                              new Column("時間", "term_date", ColumnType.DATE),
                                              CompareOperator.LE,
                                              endTime,
                                              CombineOperator.AND),
                                     new SearchCondition(
                                              new Column("運搬業者", "transporter_name", ColumnType.TEXT),
                                              CompareOperator.LIKE,
                                              searchWordField_.getText().trim(),
                                              CombineOperator.NONE)};
                    } else {
                        search
                                 = new SearchCondition[]{
                                     new SearchCondition(
                                              new Column("時間", "term_date", ColumnType.DATE),
                                              CompareOperator.GE,
                                              startDate,
                                              CombineOperator.AND),
                                     new SearchCondition(
                                              new Column("時間", "term_date", ColumnType.DATE),
                                              CompareOperator.LE,
                                              endTime,
                                              CombineOperator.AND),
                                     new SearchCondition(
                                              new Column("運搬業者", "transporter_name", ColumnType.TEXT),
                                              CompareOperator.LIKE,
                                              searchWordField_.getText().trim(),
                                              CombineOperator.AND),
                                     new SearchCondition(
                                              new Column("搬入出", "carry_class", ColumnType.INT4),
                                              CompareOperator.EQ,
                                              0,
                                              CombineOperator.NONE)};
                    }
                    break;

                case 5:
                    if (!carryInSwitch_.isSelected()) {
                        search
                                 = new SearchCondition[]{
                                     new SearchCondition(
                                              new Column("時間", "term_date", ColumnType.DATE),
                                              CompareOperator.GE,
                                              startDate,
                                              CombineOperator.AND),
                                     new SearchCondition(
                                              new Column("時間", "term_date", ColumnType.DATE),
                                              CompareOperator.LE,
                                              endTime,
                                              CombineOperator.AND),
                                     new SearchCondition(
                                              new Column("廃棄物種類", "goods_name", ColumnType.TEXT),
                                              CompareOperator.LIKE,
                                              searchWordField_.getText().trim(),
                                              CombineOperator.NONE)};
                    } else {
                        search
                                 = new SearchCondition[]{
                                     new SearchCondition(
                                              new Column("時間", "term_date", ColumnType.DATE),
                                              CompareOperator.GE,
                                              startDate,
                                              CombineOperator.AND),
                                     new SearchCondition(
                                              new Column("時間", "term_date", ColumnType.DATE),
                                              CompareOperator.LE,
                                              endTime,
                                              CombineOperator.AND),
                                     new SearchCondition(
                                              new Column("ごみ種類", "goods_name", ColumnType.TEXT),
                                              CompareOperator.LIKE,
                                              searchWordField_.getText().trim(),
                                              CombineOperator.AND),
                                     new SearchCondition(
                                              new Column("搬入出", "carry_class", ColumnType.INT4),
                                              CompareOperator.EQ,
                                              0,
                                              CombineOperator.NONE)};
                    }
                    break;

                case 6:
                    if (!carryInSwitch_.isSelected()) {
                        search
                                 = new SearchCondition[]{
                                     new SearchCondition(
                                              new Column("時間", "term_date", ColumnType.DATE),
                                              CompareOperator.GE,
                                              startDate,
                                              CombineOperator.AND),
                                     new SearchCondition(
                                              new Column("時間", "term_date", ColumnType.DATE),
                                              CompareOperator.LE,
                                              endTime,
                                              CombineOperator.AND),
                                     new SearchCondition(
                                              new Column("処分事業場", "office_name", ColumnType.TEXT),
                                              CompareOperator.LIKE,
                                              searchWordField_.getText().trim(),
                                              CombineOperator.NONE)};
                    } else {
                        search
                                 = new SearchCondition[]{
                                     new SearchCondition(
                                              new Column("時間", "term_date", ColumnType.DATE),
                                              CompareOperator.GE,
                                              startDate,
                                              CombineOperator.AND),
                                     new SearchCondition(
                                              new Column("時間", "term_date", ColumnType.DATE),
                                              CompareOperator.LE,
                                              endTime,
                                              CombineOperator.AND),
                                     new SearchCondition(
                                              new Column("処分事業場", "office_name", ColumnType.TEXT),
                                              CompareOperator.LIKE,
                                              searchWordField_.getText().trim(),
                                              CombineOperator.AND),
                                     new SearchCondition(
                                              new Column("搬入出", "carry_class", ColumnType.INT4),
                                              CompareOperator.EQ,
                                              0,
                                              CombineOperator.NONE)};
                    }
                    break;

                case 7:
                    if (!carryInSwitch_.isSelected()) {
                        search
                                 = new SearchCondition[]{
                                     new SearchCondition(
                                              new Column("時間", "term_date", ColumnType.DATE),
                                              CompareOperator.GE,
                                              startDate,
                                              CombineOperator.AND),
                                     new SearchCondition(
                                              new Column("時間", "term_date", ColumnType.DATE),
                                              CompareOperator.LE,
                                              endTime,
                                              CombineOperator.AND),
                                     new SearchCondition(
                                              new Column("処分業者", "contractor_name", ColumnType.TEXT),
                                              CompareOperator.LIKE,
                                              searchWordField_.getText().trim(),
                                              CombineOperator.NONE)};
                    } else {
                        search
                                 = new SearchCondition[]{
                                     new SearchCondition(
                                              new Column("時間", "term_date", ColumnType.DATE),
                                              CompareOperator.GE,
                                              startDate,
                                              CombineOperator.AND),
                                     new SearchCondition(
                                              new Column("時間", "term_date", ColumnType.DATE),
                                              CompareOperator.LE,
                                              endTime,
                                              CombineOperator.AND),
                                     new SearchCondition(
                                              new Column("処分業者", "contractor_name", ColumnType.TEXT),
                                              CompareOperator.LIKE,
                                              searchWordField_.getText().trim(),
                                              CombineOperator.AND),
                                     new SearchCondition(
                                              new Column("搬入出", "carry_class", ColumnType.INT4),
                                              CompareOperator.EQ,
                                              0,
                                              CombineOperator.NONE)};
                    }
                    break;

                case 8:
                    if (!carryInSwitch_.isSelected()) {
                        search
                                 = new SearchCondition[]{
                                     new SearchCondition(
                                              new Column("時間", "term_date", ColumnType.DATE),
                                              CompareOperator.GE,
                                              startDate,
                                              CombineOperator.AND),
                                     new SearchCondition(
                                              new Column("時間", "term_date", ColumnType.DATE),
                                              CompareOperator.LE,
                                              endTime,
                                              CombineOperator.AND),
                                     new SearchCondition(
                                              new Column("マニフェスト有り無し", "manifesto_no", ColumnType.TEXT),
                                              CompareOperator.EQ,
                                              "",
                                              CombineOperator.NONE)};
                    } else {
                        search
                                 = new SearchCondition[]{
                                     new SearchCondition(
                                              new Column("時間", "term_date", ColumnType.DATE),
                                              CompareOperator.GE,
                                              startDate,
                                              CombineOperator.AND),
                                     new SearchCondition(
                                              new Column("時間", "term_date", ColumnType.DATE),
                                              CompareOperator.LE,
                                              endTime,
                                              CombineOperator.AND),
                                     new SearchCondition(
                                              new Column("マニフェスト有り無し", "manifesto_no", ColumnType.TEXT),
                                              CompareOperator.EQ,
                                              "",
                                              CombineOperator.AND),
                                     new SearchCondition(
                                              new Column("搬入出", "carry_class", ColumnType.INT4),
                                              CompareOperator.EQ,
                                              0,
                                              CombineOperator.NONE)};
                    }
                    break;

                case 9:
                    if (!carryInSwitch_.isSelected()) {
                        search
                                 = new SearchCondition[]{
                                     new SearchCondition(
                                              new Column("時間", "term_date", ColumnType.DATE),
                                              CompareOperator.GE,
                                              startDate,
                                              CombineOperator.AND),
                                     new SearchCondition(
                                              new Column("時間", "term_date", ColumnType.DATE),
                                              CompareOperator.LE,
                                              endTime,
                                              CombineOperator.AND),
                                     new SearchCondition(
                                              new Column("料金設定無し", "charge_value", ColumnType.INT4),
                                              CompareOperator.EQ,
                                              0,
                                              CombineOperator.NONE)};
                    } else {
                        search
                                 = new SearchCondition[]{
                                     new SearchCondition(
                                              new Column("時間", "term_date", ColumnType.DATE),
                                              CompareOperator.GE,
                                              startDate,
                                              CombineOperator.AND),
                                     new SearchCondition(
                                              new Column("時間", "term_date", ColumnType.DATE),
                                              CompareOperator.LE,
                                              endTime,
                                              CombineOperator.AND),
                                     new SearchCondition(
                                              new Column("料金設定無し", "charge_value", ColumnType.INT4),
                                              CompareOperator.EQ,
                                              0,
                                              CombineOperator.AND),
                                     new SearchCondition(
                                              new Column("搬入出", "carry_class", ColumnType.INT4),
                                              CompareOperator.EQ,
                                              0,
                                              CombineOperator.NONE)};
                    }
                    break;

                case 10:
                    if (!carryInSwitch_.isSelected()) {
                        search
                                 = new SearchCondition[]{
                                     new SearchCondition(
                                              new Column("時間", "term_date", ColumnType.DATE),
                                              CompareOperator.GE,
                                              startDate,
                                              CombineOperator.AND),
                                     new SearchCondition(
                                              new Column("時間", "term_date", ColumnType.DATE),
                                              CompareOperator.LE,
                                              endTime,
                                              CombineOperator.AND),
                                     new SearchCondition(
                                              new Column("伝票NO", "report_no", ColumnType.TEXT),
                                              CompareOperator.EQ,
                                              searchWordField_.getText().trim(),
                                              CombineOperator.NONE)};
                    } else {
                        search
                                 = new SearchCondition[]{
                                     new SearchCondition(
                                              new Column("時間", "term_date", ColumnType.DATE),
                                              CompareOperator.GE,
                                              startDate,
                                              CombineOperator.AND),
                                     new SearchCondition(
                                              new Column("時間", "term_date", ColumnType.DATE),
                                              CompareOperator.LE,
                                              endTime,
                                              CombineOperator.AND),
                                     new SearchCondition(
                                              new Column("伝票NO", "report_no", ColumnType.TEXT),
                                              CompareOperator.EQ,
                                              searchWordField_.getText().trim(),
                                              CombineOperator.AND),
                                     new SearchCondition(
                                              new Column("搬入出", "carry_class", ColumnType.INT4),
                                              CompareOperator.EQ,
                                              0,
                                              CombineOperator.NONE)};
                    }
                    break;
            }
        }

        if (0 < searchCondition_.size()) {
            search = searchCondition_.toArray(new SearchCondition[0]);
        }
        //計量記録一覧並び　データ作成順　スパット北上仕様　20161201
        SortCondition[] sort
                 = new SortCondition[]{
                     new SortCondition(
                              new Column("SNO", "sno", ColumnType.INT8),
                              SortOrder.DESC),
                     new SortCondition(
                              new Column("計量NO", "report_no", ColumnType.INT4),
                              SortOrder.DESC)};

        if (0 < sortCondition_.size()) {
            sort = sortCondition_.toArray(new SortCondition[0]);
        }

        // リスト更新
        int row = dataTable_.getSelectedRow();
        model_.refresh(search, sort);

        if ((0 <= row) && (model_.getRowCount() > row)) {
            dataTable_.setRowSelectionInterval(row, row);
        }
        // 件数表示
        countField_.setText(String.format("%d", model_.getRowCount()));
    }

    private class ColumnUpdater implements FocusListener {

        @Override
        public void focusGained(FocusEvent event) {
        }

        @Override
        public void focusLost(FocusEvent event) {
            Object source = event.getSource();
            if (source == startDateField_) {
            }
        }
    }

    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            try {
                SwingUtilities.invokeLater(
                         new Runnable() {

                    @Override
                    public void run() {
                        Calendar c = TermDate.getTerm(Calendar.getInstance());
                        startDateField_.setValue(c);
                        endDateField_.setValue(c);
                        updateView();
                    }
                });
                //５秒で１回ループ(5000)
                if (DBManager.mode == 4) {
                    Thread.sleep(30000);
                } else {
                    Thread.sleep(5000);
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            } catch (Exception e) {
                logger_.warning(e.toString());
            }
        }
    }

    private class SearchAction implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent event) {
            updateView();
        }
    }

    private class AppendAction implements ActionListener//追加btnイベント
    {

        @Override
        public void actionPerformed(ActionEvent event) {
            JDialog dialog = DialogHelper.getNewInstance(WeighList.this, true);
            dialog.add(new WeighListDetailsPane(model_, -1, 0));
            dialog.setModal(true);
            dialog.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
            dialog.setTitle("計量記録データ - 追加");
            dialog.setSize(aks.industrialwaste.ui.DialogHelper.getMaximumSize());
            dialog.setVisible(true);
            updateView();
        }
    }

    private class EditAction implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent event) {
            showEditDialog(0);
        }
    }

    private class CopyAction implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent event) {
            showEditDialog(1);
        }
    }

    private class ManifestAction implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent event) {
            showEditDialog_mani();
        }
    }

    private class ManifestAction_kara implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent event) {
            showEditDialog_mani_kara();
        }
    }

    private class DeleteAction implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent event) {
            int row = dataTable_.getSelectedRow();
            if (0 > row) {
                JOptionPane.showMessageDialog(
                         WeighList.this,
                         "一覧から項目を選択してください.",
                         "確認",
                         JOptionPane.WARNING_MESSAGE);

                return;
            }

            int result = JOptionPane.showConfirmDialog(
                     WeighList.this,
                     "選択した項目を削除します. よろしいですか?",
                     "確認",
                     JOptionPane.YES_NO_OPTION);
            if (JOptionPane.YES_OPTION == result) {
                WeighListJournal.recordJournal(
                         model_.getConnection(),
                         "計量記録一覧 - 削除",
                         new HashMap<>(model_.getColumns(row)));
                if (model_.delete(row)) {
                    updateView();
                } else {
                    JOptionPane.showMessageDialog(
                             WeighList.this,
                             "削除に失敗しました.",
                             "警告",
                             JOptionPane.WARNING_MESSAGE);
                }
            }
        }
    }

    private class HideAction implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent event) {
            int row = dataTable_.getSelectedRow();
            if (0 > row) {
                JOptionPane.showMessageDialog(
                         WeighList.this,
                         "一覧から項目を選択してください.",
                         "確認",
                         JOptionPane.WARNING_MESSAGE);

                return;
            }

            WeighListJournal.recordJournal(
                     model_.getConnection(),
                     "計量記録一覧 - 電子マニフェスト区分変更",
                     new HashMap<>(model_.getColumns(row)));
            if (model_.電子マニフェスト区分変更(row)) {
                updateView();
            } else {
                JOptionPane.showMessageDialog(
                         WeighList.this,
                         "変更に失敗しました.",
                         "警告",
                         JOptionPane.WARNING_MESSAGE);
            }

        }
    }

    private class 領収書発行 implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent event) {
            int row = dataTable_.getSelectedRow();
            if (0 > row) {
                JOptionPane.showMessageDialog(
                         WeighList.this,
                         "一覧から項目を選択してください.",
                         "確認",
                         JOptionPane.WARNING_MESSAGE);

                return;
            }

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

            if (領収書用途 >= 0) {
                WeighListJournal.recordJournal(
                         model_.getConnection(),
                         "計量記録一覧 - 領収書発行",
                         new HashMap<>(model_.getColumns(row)));

                ////印刷処理
                if (row >= 0) {
                    long snol = (Long) model_.getValueAt(row, "sno");
                    new XlsxBillReport(XlsxBillReport.BILL_REPORT7).print(
                             con_,
                             XlsxBillReport.BILL_REPORT7,
                             null, (int) snol, 0, 0, 0, 0);
                }
            }
        }
    }

    private class ReportAction implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent event) {
            doReport();
        }
    }

    private void doReport() {

// 20111019 add 一覧表示されているデータをcsvファイルに落とす
        int confirm = JOptionPane.showConfirmDialog(
                 this, "現在一覧表示されているデータをファイルに保存します. よろしいですか?",
                 "確認", JOptionPane.OK_CANCEL_OPTION);

        if (confirm == JOptionPane.OK_OPTION) {
            Calendar c = startDateField_.getValue();
            JFileChooser fc = new JFileChooser();
            fc.setSelectedFile(new File("C:\\spat_exe\\csv",
                     String.format("計量実績_%1$tY%1$tm%1$td%1$tH%1$tM.csv", c)));
            if (JFileChooser.APPROVE_OPTION != fc.showSaveDialog(this)) {
                return;
            }
            File f = fc.getSelectedFile();
            f = new File(String.format("%s", f.getPath()));
            if (!f.getPath().matches(".+\\.[cC][sS][vV]$")) {
                model_.report(new File("C:\\satou_report\\csv",
                         String.format("計量実績_%1$tY%1$tm%1$td%1$tH%1$tM.csv", c)));

            }
            if (f.exists()) {
                confirm = JOptionPane.showConfirmDialog(
                         this,
                         String.format(
                                  "%s は存在します。\n上書きしますか?",
                                  f.getPath()),
                         "確認",
                         JOptionPane.OK_CANCEL_OPTION);
                if (JOptionPane.OK_OPTION != confirm) {

                    return;
                }
            }

            model_.report(new File(f.getPath()));

            try {
                JOptionPane.showMessageDialog(
                         this,
                         "CSVファイルへの出力を完了しました.",
                         "情報",
                         JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(
                         this,
                         "CSVファイルへの出力に失敗しました.",
                         "警告",
                         JOptionPane.WARNING_MESSAGE);
            }
        }
    }

    private class PrintAction implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent event) {
            int row = dataTable_.getSelectedRow();

            if (0 > row) {
                JOptionPane.showMessageDialog(
                         WeighList.this,
                         "一覧から項目を選択してください.",
                         "警告",
                         JOptionPane.WARNING_MESSAGE);

                return;
            }

            //印刷許可フラグを立てる
            if (DBManager.mode == 1 || DBManager.mode == 11
                     || DBManager.mode == 2 || DBManager.mode == 12
                     || DBManager.mode == 5 || DBManager.mode == 0 || DBManager.mode == 4 || DBManager.mode == 201 || DBManager.mode == 202) {
                updatePostPrint(model_.getConnection(), ((Number) model_.getValueAt(row, "sno")).longValue());
            } else if (DBManager.mode == 0 || DBManager.mode == 2 || DBManager.mode == 3) {
                doPrint(((Number) model_.getValueAt(row, "sno")).longValue());
                //BookOpen.exeの起動
                //20170210 VBAは使用しない
                doExcel();
            }

            try {
                Thread.sleep(200);
            } catch (Exception e) {
            }

        }
    }


    private void updatePostPrint(Connection con, long sno) {
        try {
            String s = new StringBuilder()
                     .append("update weigh_data ")
                     .append("set printer_print_order = true ")
                     .append("where sno = ?;")
                     .toString();
            PreparedStatement ps = con.prepareStatement(s);
            try {
                ps.setObject(1, sno);
                ps.executeUpdate();
            } finally {
                ps.close();
            }
        } catch (SQLException e) {
            logger_.warning(e.toString());
        }
    }

    private class RefreshAction implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent event) {
            updateView();
        }
    }

    protected void showEditDialog_mani() {
        int row = dataTable_.getSelectedRow();

        int index = 13;
        if (DBManager.mode == 4) {
            index = 12;
        }

        if (0 > row) {
            JOptionPane.showMessageDialog(
                     WeighList.this,
                     "一覧から項目を選択してください.",
                     "警告",
                     JOptionPane.WARNING_MESSAGE);

            return;
        }
        String btag = "";
        String atag = "";
        btag = "<html><table width='600'><tr><td align='center'>";
        atag = "</td></tr></table>";

        //最小化ボタン無効にするため
        //ダイアログで表示
        JDialog dialog = DialogHelper.getNewInstance(this, false);

        if (dataTable_.getValueAt(row, index).toString().equals("建廃")) {
            dialog.add(new KennsetuManifestoPanel100(model_, row, con_, 1));
            dialog.setTitle("建設系廃棄物マニフェスト - 印刷");
            dialog.setModal(true);
            dialog.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
            //液晶サイズワイド対応のため間延び防止サイズ指定
            dialog.setMaximumSize(new Dimension(1280, 985));
            dialog.setMinimumSize(new Dimension(1280, 985));
            dialog.setVisible(true);
        } else if (dataTable_.getValueAt(row, index).toString().equals("産廃")) {
            dialog.add(new SanngyouManifestoPanel100(model_, row, con_, 1));
            dialog.setTitle("産業廃棄物マニフェスト - 印刷");
            dialog.setModal(true);
            dialog.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
            //液晶サイズワイド対応のため間延び防止サイズ指定
            dialog.setMaximumSize(new Dimension(1280, 985));
            dialog.setMinimumSize(new Dimension(1280, 985));
            dialog.setVisible(true);
        }

        updateView();
    }

    protected void showEditDialog_mani_kara() {
        Font font14 = FontUtility.getFont("メイリオ", Font.PLAIN, 14);

        int row = dataTable_.getSelectedRow();
        if (0 > row) {
            JOptionPane.showMessageDialog(
                     WeighList.this,
                     "一覧からマニフェスト連続印刷用データを選択してください。",
                     "警告",
                     JOptionPane.WARNING_MESSAGE);

            return;
        }

        // タブの幅を調整する
        String btag = "　　　　　　　　　　　　　　";
        String atag = "　　　　　　　　　　　　　　";

        tabbedPane_ = new JTabbedPane();
        tabbedPane_.setFont(font14);
        tabbedPane_.add(new KennsetuManifestoPanel100(model_, row, con_, 0), btag + "建設系廃棄物マニフェスト" + atag);
        tabbedPane_.add(new SanngyouManifestoPanel100(model_, row, con_, 0), btag + "産業廃棄物マニフェスト" + atag);

//      //最小化ボタン無効にするため
//      //ダイアログで表示
        JDialog dialog = DialogHelper.getNewInstance(this, false);
        dialog.add(tabbedPane_);
        dialog.setModal(true);
        dialog.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        dialog.setSize(aks.industrialwaste.ui.DialogHelper.getMaximumSize());
        dialog.setModal(true);
        dialog.setVisible(true);

        updateView();
    }

    protected void showEditDialog_mani_final() {

        int row = dataTable_.getSelectedRow();
        if (0 > row) {
            JOptionPane.showMessageDialog(
                     WeighList.this,
                     "一覧からマニフェストデータを選択してください。",
                     "警告",
                     JOptionPane.WARNING_MESSAGE);

            return;
        }
        updateView();
    }

    protected void showEditDialog(int index)//訂正btnイベント
    {
        int row = dataTable_.getSelectedRow();
        if (0 > row) {
            JOptionPane.showMessageDialog(
                     WeighList.this,
                     "一覧から項目を選択してください.",
                     "警告",
                     JOptionPane.WARNING_MESSAGE);

            return;
        }

        JDialog dialog = DialogHelper.getNewInstance(this, true);
        dialog.add(new WeighListDetailsPane(model_, row, index));
        dialog.setModal(true);
        dialog.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        if (index == 0) {
            dialog.setTitle("計量記録データ - 訂正");
        } else {
            dialog.setTitle("計量記録データ - 複写");
        }
        dialog.setSize(aks.industrialwaste.ui.DialogHelper.getMaximumSize());
        dialog.setVisible(true);
        updateView();
    }

    private void doPrint(long sno) {
        try {
            Statement st = con_.createStatement();
            st.executeUpdate("begin;");

            try {
                String s = new StringBuilder()
                         .append("update m_report set ")
                         .append("yearly_num = ?, ")
                         .append("print_flag = 1 ")
                         .append("where sno = 30;")
                         .toString();
                PreparedStatement ps = con_.prepareStatement(s);
                try {
                    ps.setObject(1, sno);
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
        }

    }

    private void doExcel() {
        try {
            Runtime runtime = Runtime.getRuntime();
            Process proc = runtime.exec("C:\\Excel\\BookOpen.exe");
            proc.waitFor();

        } catch (InterruptedException ev) {
            System.out.println("失敗(InnterruptedException)");
        } catch (IOException ev) {
            System.out.println("失敗(IOException)");
        }
    }
}
