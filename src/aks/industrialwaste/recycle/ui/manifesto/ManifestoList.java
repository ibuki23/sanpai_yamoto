package aks.industrialwaste.recycle.ui.manifesto;

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
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.event.AncestorEvent;
import aks.industrialwaste.database.Column;
import aks.industrialwaste.database.ColumnType;
import aks.industrialwaste.database.CombineOperator;
import aks.industrialwaste.database.CompareOperator;
import aks.industrialwaste.database.DBManager;
import aks.industrialwaste.database.SearchCondition;
import aks.industrialwaste.database.SortCondition;
import aks.industrialwaste.database.SortOrder;
import aks.industrialwaste.database.Table;
import aks.industrialwaste.recycle.ui.MainFrame;
import aks.industrialwaste.recycle.ui.weighlist.WeighListDetailsPane;
import aks.industrialwaste.recycle.ui.weighlist.WeighListTableModel;
import aks.industrialwaste.ui.DialogHelper;
import aks.industrialwaste.ui.ListPanelMaster;
import aks.industrialwaste.ui.util.GeneralFunctionPane;
import jp.co.astax.swing.DateTimeField;
import aks.util.FontUtility;
import java.sql.Connection;
import aks.swing.JTextField;
import com.ibm.icu.text.Transliterator;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JMenuItem;
import javax.swing.table.*;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.WindowConstants;

public class ManifestoList extends ListPanelMaster implements Runnable {

    private List<SearchCondition> searchCondition_;
    private List<SortCondition> sortCondition_;
    private WeighListTableModel model_;
    private Thread thread_ = null;
    private DateTimeField startDateField_;
    private DateTimeField endDateField_;
    private JTextField searchWordField_;
    protected JLabel dealingsWeightField_LO_;
    protected JLabel dealingsWeightField_LN_;
    protected JLabel dealingsWeightField_LA_;
    private Transliterator tr1_;
    private Transliterator tr2_;
    private Connection con_;
    private JPopupMenu popup;
    private List<String> mani_no_;

    public ManifestoList(Connection con) {
        con_ = con;
        model_ = new WeighListTableModel(con_);
        dataTable_.setModel(model_);
        dataTable_.setColumnModel(new ManifestoTableColumnModel());

        searchCondition_
                 = Collections.synchronizedList(new ArrayList<SearchCondition>());
        sortCondition_
                 = Collections.synchronizedList(new ArrayList<SortCondition>());
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

        Font font14 = FontUtility.getFont("メイリオ", Font.PLAIN, 14);

        popup = new JPopupMenu();// ポップアップメニューを生成

        thread_ = new Thread(ManifestoList.this);
        thread_.start();

        mani_no_ = new ArrayList<>();

        // タイトル
        titleField_.setText("マニフェスト一覧");
        JPanel panel = new JPanel();
        panel.setLayout(new java.awt.GridBagLayout());
        startDateField_ = new DateTimeField(DateTimeField.DateFormat.YMD);
        startDateField_.setFont(font14);
        startDateField_.setMinimumSize(new Dimension(270, 30));
        startDateField_.setPreferredSize(new Dimension(270, 30));
        startDateField_.addFocusListener(new ColumnUpdater());
        addContainer(
                 panel, startDateField_,
                 0, 0, 1, 1, 0.0, 0.0,
                 GridBagConstraints.WEST, GridBagConstraints.NONE,
                 new Insets(2, 2, 2, 0), 0, 0);
        panel.add(startDateField_);
        JLabel label = new JLabel(" - ", JLabel.LEFT);
        label.setFont(font14);
        addContainer(
                 panel, label,
                 1, 0, 1, 1, 0.0, 0.0,
                 GridBagConstraints.CENTER, GridBagConstraints.NONE,
                 new Insets(2, 2, 2, 2), 0, 0);
        panel.add(label);
        //抽出日
        endDateField_ = new DateTimeField(DateTimeField.DateFormat.YMD);
        endDateField_.setFont(font14);
        endDateField_.setMinimumSize(new Dimension(270, 30));
        endDateField_.setPreferredSize(new Dimension(270, 30));
        addContainer(
                 panel, endDateField_,
                 2, 0, 1, 1, 0.0, 0.0,
                 GridBagConstraints.WEST, GridBagConstraints.NONE,
                 new Insets(2, 2, 2, 0), 0, 0);
        panel.add(endDateField_);
        // 再表示ボタン
        JButton b = new JButton("再表示");
        b.setFont(font14);
        b.setMargin(new Insets(2, 2, 2, 2));
        b.setMinimumSize(new Dimension(120, 30));
        b.setPreferredSize(new Dimension(120, 30));
        b.addActionListener(new RefreshAction());
        addContainer(
                 panel, b,
                 3, 0, 1, 1, 0.0, 0.0,
                 GridBagConstraints.WEST, GridBagConstraints.NONE,
                 new Insets(2, 20, 2, 2), 0, 0);

        //検索
        JLabel lbl = new JLabel("<html>交付番号・排出事業者・廃棄物名　検索<br>交付番号の複数検索をリセットする場合は空欄でEnter</html>");
        lbl.setFont(font14);
        panel.add(lbl);
        addContainer(
                 panel, lbl,
                 8, 0, 2, 1, 0.1, 0.0,
                 GridBagConstraints.EAST, GridBagConstraints.NONE,
                 new Insets(2, 2, 2, 2), 0, 0);
        addContainer(
                 northPane_, panel,
                 0, 1, 1, 1, 0.9, 0.0,
                 GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL,
                 new Insets(0, 0, 0, 0), 0, 0);

        searchWordField_ = new JTextField();
        searchWordField_.setFont(font14);
        searchWordField_.setMargin(new Insets(0, 0, 0, 0));
        searchWordField_.setMinimumSize(new Dimension(120, 30));
        searchWordField_.setPreferredSize(new Dimension(120, 30));
        searchWordField_.addActionListener(new SearchAction());
        searchWordField_.addFocusListener(new java.awt.event.FocusAdapter() {
            // フォーカスが移ったときはIMEをOFFにする
            @Override
            public void focusLost(java.awt.event.FocusEvent e) {
                searchWordField_.getInputContext().setCompositionEnabled(false);
                searchWordField_.getInputContext().setCharacterSubsets(null);
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

        // データテーブル
        dataTable_.setFont(font14);
        dataTable_.setRowHeight(30);
        dataTable_.setColumnSelectionAllowed(false);
        dataTable_.setRowSelectionAllowed(true);
        dataTable_.getTableHeader().setReorderingAllowed(false);
        dataTable_.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

        //右クリック時のPOPアップメニュー作成///////////////////////////////////////
        JMenuItem item00 = new JMenuItem("最新の情報に更新", MainFrame.icon_reload);
        JMenuItem item0 = new JMenuItem("計量データ詳細", MainFrame.icon_edit);
        JMenuItem item1 = new JMenuItem("マニフェスト印刷", MainFrame.icon_print);
        JMenuItem item3 = new JMenuItem("最終処分印刷");
        JMenuItem item2 = new JMenuItem("データ削除", MainFrame.icon_delete);
        popup.add(item00);
        popup.add(item0);
        popup.add(item1);
        popup.add(item3);

        popup.addSeparator(); //セパレーター
        popup.add(item2);
        // 各メニュー選択時の処理
        item00.addActionListener(new RefreshAction());
        item0.addActionListener(new EditAction());
        item1.addActionListener(new ManifestAction());
        item2.addActionListener(new DeleteAction());
        if (DBManager.mode == 4) {
            item3.addActionListener(new ManifestAction_final());
        }
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

        JTableHeader jh = dataTable_.getTableHeader();
        jh.setFont(font14);

        // ボタン
        // 計量記録データ詳細
        b = new JButton("計量データ詳細");
        b.setFont(font14);
        b.setMargin(new Insets(2, 2, 2, 2));
        b.setMinimumSize(new Dimension(150, 30));
        b.setPreferredSize(new Dimension(150, 30));
        b.addActionListener(new EditAction());
        addContainer(
                 southPane_, b,
                 0, 0, 1, 1, 0.0, 0.0,
                 GridBagConstraints.WEST, GridBagConstraints.NONE,
                 new Insets(2, 2, 2, 2), 0, 0);
        // マニフェスト
        b = new JButton("マニフェスト印刷");
        b.setFont(font14);
        b.setMargin(new Insets(0, 0, 0, 0));
        b.setMinimumSize(new Dimension(150, 30));
        b.setPreferredSize(new Dimension(150, 30));
        b.addActionListener(new ManifestAction());
        addContainer(
                 southPane_, b,
                 1, 0, 1, 1, 0.0, 0.0,
                 GridBagConstraints.WEST, GridBagConstraints.NONE,
                 new Insets(2, 2, 2, 2), 0, 0);

        // 最終処分印刷
        b = new JButton("最終処分印刷");
        b.setFont(FontUtility.getFont("メイリオ", Font.PLAIN, 14));
        b.setMargin(new Insets(0, 0, 0, 0));
        b.setMinimumSize(new Dimension(150, 30));
        b.setPreferredSize(new Dimension(150, 30));
        b.addActionListener(new ManifestAction_final());
        addContainer(
                 southPane_, b,
                 4, 0, 1, 1, 0.0, 0.0,
                 GridBagConstraints.WEST, GridBagConstraints.NONE,
                 new Insets(2, 2, 2, 2), 0, 0);
        // 複数行選択
        JLabel lbl1 = new JLabel("Ctrl + クリックで複数行選択");
        lbl1.setFont(FontUtility.getFont("メイリオ", Font.PLAIN, 14));
        lbl1.setMinimumSize(new Dimension(250, 35));
        lbl1.setPreferredSize(new Dimension(250, 35));
        addContainer(
                 southPane_, lbl1,
                 5, 0, 1, 1, 1.0, 0.0,
                 GridBagConstraints.WEST, GridBagConstraints.NONE,
                 new Insets(2, 2, 2, 2), 0, 0);

        // 汎用機能
        b = new JButton("簡易検索");
        b.setFont(font14);
        b.setMargin(new Insets(2, 2, 2, 2));
        b.setMinimumSize(new Dimension(150, 30));
        b.setPreferredSize(new Dimension(150, 30));
        b.addActionListener(new GeneralFunctionAction2());
//        addContainer(
//                southPane_, b,
//                6, 0, 1, 1, 0.5, 0.0,
//                GridBagConstraints.EAST, GridBagConstraints.NONE,
//                new Insets(2, 2, 2, 2), 0, 0);

        // 汎用機能
        b = new JButton("汎用機能");
        b.setFont(font14);
        b.setMargin(new Insets(2, 2, 2, 2));
        b.setMinimumSize(new Dimension(150, 30));
        b.setPreferredSize(new Dimension(150, 30));
        b.addActionListener(new GeneralFunctionAction());
        addContainer(
                 southPane_, b,
                 7, 0, 1, 1, 0.5, 0.0,
                 GridBagConstraints.EAST, GridBagConstraints.NONE,
                 new Insets(2, 2, 2, 2), 0, 0);

        //ダミー
        addContainer(
                 southPane_, new JPanel(),
                 10, 0, 1, 1, 1.0, 0.0,
                 GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
                 new Insets(2, 2, 2, 2), 0, 0);
        // 削除
        b = new JButton("データ削除");
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
            mani_no_.clear();
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
                                  new Column("マニフェスト1", "( manifesto_no1", ColumnType.TEXT),
                                  CompareOperator.NT,
                                  "",
                                  CombineOperator.OR),
                         new SearchCondition(
                                  new Column("マニフェスト2", "manifesto_no2", ColumnType.TEXT),
                                  CompareOperator.NT,
                                  "",
                                  CombineOperator.OR),
                         new SearchCondition(
                                  new Column("2次マニフェスト", "secod_manifesto_no", ColumnType.TEXT),
                                  CompareOperator.NT,
                                  "",
                                  CombineOperator.NONE)};
        } else {
            if (mani_no_.indexOf(searchWordField_.getText().trim()) < 0) {
                mani_no_.add(searchWordField_.getText().trim());
            }

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
                                  new Column("マニフェスト", "manifesto_no", ColumnType.TEXT),
                                  CompareOperator.NT,
                                  "",
                                  CombineOperator.AND),
                         new SearchCondition(
                                  new Column("排出事業者", "(entrepreneur_name", ColumnType.TEXT),
                                  CompareOperator.LIKE,
                                  searchWordField_.getText().trim(),
                                  CombineOperator.OR),
                         new SearchCondition(
                                  new Column("品目", "goods_name", ColumnType.TEXT),
                                  CompareOperator.LIKE,
                                  searchWordField_.getText().trim(),
                                  CombineOperator.OR),
                         new SearchCondition(
                                  new Column("マニフェスト1", "manifesto_no1", ColumnType.TEXT),
                                  CompareOperator.IN,
                                  mani_no_,
                                  CombineOperator.OR2),
                         new SearchCondition(
                                  new Column("マニフェスト2", "manifesto_no2", ColumnType.TEXT),
                                  CompareOperator.IN,
                                  mani_no_,
                                  CombineOperator.OR2),
                         new SearchCondition(
                                  new Column("2次マニフェスト", "secod_manifesto_no", ColumnType.TEXT),
                                  CompareOperator.IN,
                                  mani_no_,
                                  CombineOperator.NONE2)};
        }

        if (0 < searchCondition_.size()) {
            search = searchCondition_.toArray(new SearchCondition[0]);
        }
        SortCondition[] sort
                 = new SortCondition[]{
                     new SortCondition(
                              new Column("出構日時", "gate_out_date", ColumnType.DATE),
                              SortOrder.DESC),
                     new SortCondition(
                              new Column("SNO", "sno", ColumnType.INT4),
                              SortOrder.DESC)};
        if (0 < sortCondition_.size()) {
            sort = sortCondition_.toArray(new SortCondition[0]);
        }

        // リスト更新
        int row = dataTable_.getSelectedRow();
        model_.refresh(search, sort);

        //20141129マニフェスト番号が入力されたら該当のデータを選択する
        for (int i = 0; i < model_.getRowCount(); i++) {
            if (dataTable_.getValueAt(i, 3).equals(searchWordField_.getText().trim())
                     || dataTable_.getValueAt(i, 4).equals(searchWordField_.getText().trim())) {
                row = i;
                break;
            }
        }
        if ((0 <= row) && (model_.getRowCount() > row)) {
            dataTable_.setRowSelectionInterval(row, row);
        }

        // 件数表示
        countField_.setText(String.format("%d", model_.getRowCount()));
    }

    private class GeneralFunctionAction implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent event) {
            GeneralFunctionPane pane
                     = new GeneralFunctionPane(
                              model_.getConnection(),
                              new Table(
                                       "計量マニフェストデータ",
                                       "v1_weigh_data",
                                       new Column[]{
                                           new Column("排出業者ID", "dis_ent_id", ColumnType.INT4),
                                           new Column("排出業者名", "entrepreneur_name", ColumnType.TEXT),
                                           new Column("排出現場ID", "dis_office_id", ColumnType.INT4),
                                           new Column("排出現場名", "office_name", ColumnType.TEXT),
                                           new Column("品名ID", "goods_id", ColumnType.INT4),
                                           new Column("品名", "goods_name", ColumnType.TEXT),
                                           new Column("車番", "car_no", ColumnType.INT4),
                                           new Column("車種ID", "car_kind_id", ColumnType.INT4),
                                           new Column("車種名", "car_kind_name", ColumnType.TEXT),
                                           new Column("運搬業者ID", "trans_id", ColumnType.INT4),
                                           new Column("運搬業者名", "transporter_name", ColumnType.TEXT),
                                           new Column("処分業者ID", "dis_cont_id", ColumnType.INT4),
                                           new Column("処分業者名", "contractor_name", ColumnType.TEXT),
                                           new Column("処分場ID", "manifesto3_id1", ColumnType.INT4),
                                           new Column("処分場名", "cont_office_name", ColumnType.TEXT),
                                           new Column("所属日", "term_date", ColumnType.DATE),
                                           new Column("入構日時", "gate_in_date", ColumnType.TIMESTAMP),
                                           new Column("入構重量", "gate_in_weight", ColumnType.INT4),
                                           new Column("出構日時", "gate_out_date", ColumnType.TIMESTAMP),
                                           new Column("出構重量", "gate_out_weight", ColumnType.INT4),
                                           new Column("取引重量", "net_weight", ColumnType.INT4),
                                           new Column("計量票No", "report_no", ColumnType.TEXT),
                                           new Column("最終処分の場所", "last_office_name", ColumnType.TEXT),
                                           new Column("請求先ID", "bill_id", ColumnType.INT4),
                                           new Column("請求先名", "bill_name", ColumnType.TEXT),
                                           new Column("産廃交付番号", "manifesto_no1", ColumnType.TEXT),
                                           new Column("建設交付番号", "manifesto_no2", ColumnType.TEXT),
                                           new Column("交付日", "grant_date12", ColumnType.DATE),
                                           new Column("処分日", "cont_date12", ColumnType.DATE),
                                           new Column("最終処分日", "cont_date3", ColumnType.DATE)

                                       }),
                              searchCondition_,
                              sortCondition_);
            pane.setMinimumSize(new Dimension(1280, 1024));
            pane.setPreferredSize(new Dimension(1280, 1024));
            pane.setMaximumSize(new Dimension(1280, 1024));
            JDialog dialog = DialogHelper.getNewInstance(ManifestoList.this, true);
            dialog.add(pane);
            dialog.setSize(aks.industrialwaste.ui.DialogHelper.getMaximumSize());
            //20140918
            dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
            dialog.setVisible(true);

            updateView();
        }
    }

    private class GeneralFunctionAction2 implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent event) {
            ManifestoSerachDialog pane
                     = new ManifestoSerachDialog(
                              model_.getConnection(),
                              new Table(
                                       "計量マニフェストデータ",
                                       "v1_weigh_data",
                                       new Column[]{
                                           new Column("排出業者ID", "dis_ent_id", ColumnType.INT4),
                                           new Column("排出業者名", "entrepreneur_name", ColumnType.TEXT),
                                           new Column("排出現場ID", "dis_office_id", ColumnType.INT4),
                                           new Column("排出現場名", "office_name", ColumnType.TEXT),
                                           new Column("品名ID", "goods_id", ColumnType.INT4),
                                           new Column("品名", "goods_name", ColumnType.TEXT),
                                           new Column("車番", "car_no", ColumnType.INT4),
                                           new Column("車種ID", "car_kind_id", ColumnType.INT4),
                                           new Column("車種名", "car_kind_name", ColumnType.TEXT),
                                           new Column("運搬業者ID", "trans_id", ColumnType.INT4),
                                           new Column("運搬業者名", "transporter_name", ColumnType.TEXT),
                                           new Column("処分業者ID", "dis_cont_id", ColumnType.INT4),
                                           new Column("処分業者名", "contractor_name", ColumnType.TEXT),
                                           new Column("処分場ID", "manifesto3_id1", ColumnType.INT4),
                                           new Column("処分場名", "cont_office_name", ColumnType.TEXT),
                                           new Column("所属日", "term_date", ColumnType.DATE),
                                           new Column("入構日時", "gate_in_date", ColumnType.TIMESTAMP),
                                           new Column("入構重量", "gate_in_weight", ColumnType.INT4),
                                           new Column("出構日時", "gate_out_date", ColumnType.TIMESTAMP),
                                           new Column("出構重量", "gate_out_weight", ColumnType.INT4),
                                           new Column("取引重量", "net_weight", ColumnType.INT4),
                                           new Column("計量票No", "report_no", ColumnType.TEXT),
                                           new Column("最終処分の場所", "last_office_name", ColumnType.TEXT),
                                           new Column("請求先ID", "bill_id", ColumnType.INT4),
                                           new Column("請求先名", "bill_name", ColumnType.TEXT),
                                           new Column("産廃交付番号", "manifesto_no1", ColumnType.TEXT),
                                           new Column("建設交付番号", "manifesto_no2", ColumnType.TEXT),
                                           new Column("交付日", "grant_date12", ColumnType.DATE),
                                           new Column("処分日", "cont_date12", ColumnType.DATE),
                                           new Column("最終処分日", "cont_date3", ColumnType.DATE)
                                       }),
                              searchCondition_,
                              sortCondition_);
            pane.setMinimumSize(new Dimension(700, 250));
            pane.setPreferredSize(new Dimension(700, 250));
            pane.setMaximumSize(new Dimension(700, 250));
            JDialog dialog = DialogHelper.getNewInstance(ManifestoList.this, true);
            dialog.add(pane);
            dialog.setTitle("簡易検索");
            dialog.setBounds(100, 100, 800, 350);
            dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
            dialog.setVisible(true);
        }
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
                    }
                });
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

    private class ManifestAction_final implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent event) {
            showEditDialog_mani_final();
        }
    }

    protected void showEditDialog_mani_final() {

        int row = dataTable_.getSelectedRow();
        int[] multi_select;
        multi_select = dataTable_.getSelectedRows();

        if (0 > row) {
            JOptionPane.showMessageDialog(
                     ManifestoList.this,
                     "一覧からマニフェストデータを選択してください。",
                     "警告",
                     JOptionPane.WARNING_MESSAGE);

            return;
        }

        //ダイアログで表示
        JDialog dialog = DialogHelper.getNewInstance(this, false);
        if (!dataTable_.getValueAt(row, 3).toString().equals("")) {
            dialog.add(new KennsetuManifestoDialog(model_, row, con_, 1, multi_select));
            dialog.setTitle("建設系廃棄物マニフェスト - 最終処分登録");
            dialog.setSize(aks.industrialwaste.ui.DialogHelper.getMaximumSize());
            dialog.setBounds(300, 400, 700, 200);
            dialog.setModal(true);
            dialog.setVisible(true);
        } else if (!dataTable_.getValueAt(row, 4).toString().equals("")) {
            dialog.add(new SangyouManifestoDialog(model_, row, con_, 1, multi_select));
            dialog.setTitle("産業廃棄物マニフェスト - 最終処分登録");
            dialog.setSize(aks.industrialwaste.ui.DialogHelper.getMaximumSize());
            dialog.setBounds(300, 400, 700, 200);
            dialog.setModal(true);
            dialog.setVisible(true);
        }

        updateView();
    }

    private class EditAction implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent event) {
            showEditDialog();
        }
    }

    private class ManifestAction implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent event) {
            showEditDialog_mani();
        }
    }

    private class DeleteAction implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent event) {
            int row = dataTable_.getSelectedRow();
            if (0 > row) {
                JOptionPane.showMessageDialog(
                         ManifestoList.this,
                         "一覧から項目を選択してください.",
                         "確認",
                         JOptionPane.WARNING_MESSAGE);

                return;
            }

            int result = JOptionPane.showConfirmDialog(
                     ManifestoList.this,
                     "選択した項目を削除します. よろしいですか?",
                     "確認",
                     JOptionPane.YES_NO_OPTION);
            if (JOptionPane.YES_OPTION == result) {
                ManifestoListJournal.recordJournal(
                         model_.getConnection(),
                         "マニフェスト一覧 - 削除",
                         new HashMap<String, Object>(model_.getColumns(row)));
                //中央へ(シーケンサ)データ送信
                if (model_.delete(row)) {
                    updateView();
                } else {
                    JOptionPane.showMessageDialog(
                             ManifestoList.this,
                             "削除に失敗しました.",
                             "警告",
                             JOptionPane.WARNING_MESSAGE);
                }
            }
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
        if (0 > row) {
            JOptionPane.showMessageDialog(
                     ManifestoList.this,
                     "一覧から項目を選択してください.",
                     "警告",
                     JOptionPane.WARNING_MESSAGE);

            return;
        }

        //最小化ボタン無効にするため
        //ダイアログで表示
        JDialog dialog = DialogHelper.getNewInstance(this, false);
        if (!dataTable_.getValueAt(row, 3).toString().equals("")) {//選択した行の交付番号に空白がなければ
            dialog.add(new KennsetuManifestoPanel100(model_, row, con_, 1));
            dialog.setTitle("建設系廃棄物マニフェスト - 印刷");
        } else if (!dataTable_.getValueAt(row, 4).toString().equals("")) {
            dialog.add(new SanngyouManifestoPanel100(model_, row, con_, 1));
            dialog.setTitle("産業廃棄物マニフェスト - 印刷");
        }
        dialog.setModal(true);
        dialog.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        //液晶サイズワイド対応のため間延び防止サイズ指定
        dialog.setMaximumSize(new Dimension(1280, 985));
        dialog.setMinimumSize(new Dimension(1280, 985));
        dialog.setVisible(true);

        updateView();
    }

    @Override
    protected void showEditDialog()//訂正btnイベント
    {
        int row = dataTable_.getSelectedRow();
        if (0 > row) {
            JOptionPane.showMessageDialog(
                     ManifestoList.this,
                     "一覧から項目を選択してください.",
                     "警告",
                     JOptionPane.WARNING_MESSAGE);

            return;
        }

        JDialog dialog = DialogHelper.getNewInstance(this, true);
        dialog.add(new WeighListDetailsPane(model_, row, 0));
        dialog.setModal(true);
        dialog.setTitle("計量記録データ - 訂正");
        dialog.setSize(aks.industrialwaste.ui.DialogHelper.getMaximumSize());
        dialog.setVisible(true);
        updateView();
    }
}
