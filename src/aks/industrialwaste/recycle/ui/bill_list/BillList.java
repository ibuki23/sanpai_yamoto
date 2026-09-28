package aks.industrialwaste.recycle.ui.bill_list;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.event.AncestorEvent;
import aks.industrialwaste.database.Column;
import aks.industrialwaste.database.ColumnType;
import aks.industrialwaste.database.CombineOperator;
import aks.industrialwaste.database.CompareOperator;
import aks.industrialwaste.database.DBManager;
import aks.industrialwaste.database.SearchCondition;
import aks.industrialwaste.database.SortCondition;
import aks.industrialwaste.database.SortOrder;
import aks.industrialwaste.recycle.ui.MainFrame;
import aks.industrialwaste.ui.DialogHelper;
import jp.co.astax.swing.DateTimeField;
import jp.co.astax.awt.FontUtility;
import java.sql.Connection;
import aks.swing.JTextField;
import com.ibm.icu.text.Transliterator;
import aks.industrialwaste.ui.ListPanel;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Date;
import javax.swing.JComboBox;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.SwingUtilities;
import javax.swing.table.JTableHeader;

/**
 * 請求データを一覧表示する.
 *
 * @version 1.00
 * @author AKS.Takisawa
 */
public class BillList extends ListPanel {

    private List<SearchCondition> searchCondition_;
    private List<SortCondition> sortCondition_;
    private BillListTableModel model_;
    private DateTimeField startDateField_;
    private DateTimeField endDateField_;
    private JTextField searchWordField_;
    private Transliterator tr1_;
    private Transliterator tr2_;
    private Connection con_;

    private JPopupMenu popup;

    private String[] bundle_;
    private JComboBox bundleField_;

    public BillList(Connection con) {
        con_ = con;
        model_ = new BillListTableModel(con_);
        dataTable_.setModel(model_);
        dataTable_.setColumnModel(new BillListTableColumnModel());
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
        Font font24 = FontUtility.getFont("メイリオ", Font.PLAIN, 24);
        popup = new JPopupMenu();// ポップアップメニューを生成

        bundle_ = new String[]{"末", "5", "10", "15", "20", "25"};

        // タイトル
        titleField_.setText("請求書一覧");
        titleField_.setFont(font24);

        //抽出日
        JPanel panel = new JPanel();
        panel.setLayout(new java.awt.GridBagLayout());
        JLabel lbl = new JLabel("請求月");
        lbl.setFont(font14);
        addContainer(
                 panel, lbl,
                 0, 0, 1, 1, 0.0, 0.0,
                 GridBagConstraints.WEST, GridBagConstraints.NONE,
                 new Insets(2, 2, 2, 0), 0, 0);
        startDateField_ = new DateTimeField(DateTimeField.DateFormat.YM);
        startDateField_.setFont(font14);
        startDateField_.setMinimumSize(new Dimension(180, 30));
        startDateField_.setPreferredSize(new Dimension(180, 30));
        addContainer(
                 panel, startDateField_,
                 1, 0, 1, 1, 0.0, 0.0,
                 GridBagConstraints.WEST, GridBagConstraints.NONE,
                 new Insets(2, 2, 2, 0), 0, 0);
        JLabel label = new JLabel(" - ", JLabel.LEFT);
        label.setFont(font14);
        addContainer(
                 panel, label,
                 2, 0, 1, 1, 0.0, 0.0,
                 GridBagConstraints.CENTER, GridBagConstraints.NONE,
                 new Insets(2, 2, 2, 2), 0, 0);
        //抽出日
        endDateField_ = new DateTimeField(DateTimeField.DateFormat.YM);
        endDateField_.setFont(font14);
        endDateField_.setMinimumSize(new Dimension(180, 30));
        endDateField_.setPreferredSize(new Dimension(180, 30));
        addContainer(
                 panel, endDateField_,
                 3, 0, 1, 1, 0.0, 0.0,
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
                 4, 0, 1, 1, 0.0, 0.0,
                 GridBagConstraints.WEST, GridBagConstraints.NONE,
                 new Insets(2, 20, 2, 2), 0, 0);

        lbl = new JLabel("締日");
        lbl.setFont(font14);
        addContainer(
                 panel, lbl,
                 5, 0, 1, 1, 0.0, 0.0,
                 GridBagConstraints.WEST, GridBagConstraints.NONE,
                 new Insets(2, 20, 2, 0), 0, 0);

        bundleField_ = new JComboBox();
        bundleField_.setFont(font14);
        bundleField_.setMinimumSize(new Dimension(100, 30));
        bundleField_.setPreferredSize(new Dimension(100, 30));
        bundleField_.setModel(new javax.swing.DefaultComboBoxModel(bundle_));
        addContainer(
                 panel, bundleField_,
                 6, 0, 1, 1, 0.0, 0.0,
                 GridBagConstraints.WEST, GridBagConstraints.NONE,
                 new Insets(2, 2, 2, 2), 0, 0);

        //検索
        lbl = new JLabel("請求先検索");
        lbl.setFont(font14);
        panel.add(lbl);
        addContainer(
                 panel, lbl,
                 7, 0, 2, 1, 0.1, 0.0,
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
        searchWordField_.setMinimumSize(new Dimension(120, 35));
        searchWordField_.setPreferredSize(new Dimension(120, 35));
        searchWordField_.addActionListener(new SearchAction());
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
        dataTable_.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        dataTable_.getTableHeader().setReorderingAllowed(false);
        dataTable_.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        dataTable_.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                getDetailsAction(evt);
            }
        });

        JTableHeader jh = dataTable_.getTableHeader();
        jh.setFont(font14);

        //右クリック時のPOPアップメニュー作成///////////////////////////////////////
        JMenuItem item00 = new JMenuItem("最新の情報に更新", MainFrame.icon_reload);
        JMenuItem item1 = new JMenuItem("請求金額更新");
        JMenuItem item2 = new JMenuItem("請求書印刷", MainFrame.icon_print);
        JMenuItem item3 = new JMenuItem("データ削除", MainFrame.icon_delete);
        popup.add(item00);
        popup.add(item3);
        popup.add(item1);
        popup.addSeparator(); //セパレーター
        popup.add(item2);

        // 各メニュー選択時の処理
        item00.addActionListener(new RefreshAction());
        item1.addActionListener(new UpdateAction());
        item2.addActionListener(new PrintAction());
        item3.addActionListener(new DeleteAction());
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
        // 訂正
        b = new JButton("金額訂正");
        b.setFont(font14);
        b.setMargin(new Insets(2, 2, 2, 2));
        b.setMinimumSize(new Dimension(130, 30));
        b.setPreferredSize(new Dimension(130, 30));
        b.addActionListener(new EditAction());
        // 訂正
        b = new JButton("データ削除");
        b.setFont(font14);
        b.setMargin(new Insets(2, 2, 2, 2));
        b.setMinimumSize(new Dimension(130, 30));
        b.setPreferredSize(new Dimension(130, 30));
        b.addActionListener(new DeleteAction());
        addContainer(
                 southPane_, b,
                 1, 0, 1, 1, 0.0, 0.0,
                 GridBagConstraints.WEST, GridBagConstraints.NONE,
                 new Insets(2, 2, 2, 2), 0, 0);

        // 請求書金額登録
        b = new JButton("請求金額更新");
        b.setFont(font14);
        b.setMargin(new Insets(2, 2, 2, 2));
        b.setMinimumSize(new Dimension(130, 30));
        b.setPreferredSize(new Dimension(130, 30));
        b.addActionListener(new UpdateAction());
        addContainer(
                 southPane_, b,
                 2, 0, 1, 1, 1.0, 0.0,
                 GridBagConstraints.EAST, GridBagConstraints.NONE,
                 new Insets(2, 2, 2, 2), 0, 0);

        // 請求書印刷
        b = new JButton("請求書印刷");
        b.setFont(font14);
        b.setMargin(new Insets(2, 2, 2, 2));
        b.setMinimumSize(new Dimension(130, 30));
        b.setPreferredSize(new Dimension(130, 30));
        b.addActionListener(new PrintAction());
        addContainer(
                 southPane_, b,
                 3, 0, 1, 1, 0.0, 0.0,
                 GridBagConstraints.EAST, GridBagConstraints.NONE,
                 new Insets(2, 2, 2, 2), 0, 0);
        bundleField_.setSelectedIndex(0);
    }

    @Override
    protected void updateView() {
        if (null == startDateField_.getValue()) {
            startDateField_.setValue(Calendar.getInstance());
        }
        if (null == endDateField_.getValue()) {
            endDateField_.setValue(Calendar.getInstance());
        }
        Calendar sc = startDateField_.getValue();
        sc.set(Calendar.HOUR_OF_DAY, 0);
        Calendar ec = endDateField_.getValue();
        ec.set(Calendar.HOUR_OF_DAY, 23);
        startDateField_.setValue(sc);
        endDateField_.setValue(ec);

        String searchWordTemp;
        searchWordTemp = tr1_.transliterate(searchWordField_.getText().trim());
        searchWordTemp = tr2_.transliterate(searchWordTemp);

        Date startDate = DBManager.convert(startDateField_.getValue().getTime());
        Date endTime = DBManager.convert(endDateField_.getValue().getTime());
        // 検索条件, ソート条件を設定
        SearchCondition[] search;
        //bundleField_
        int bundle_day_ = 0;
        if (bundleField_.getSelectedIndex() == 1) {
            bundle_day_ = 5;
        } else if (bundleField_.getSelectedIndex() == 2) {
            bundle_day_ = 10;
        } else if (bundleField_.getSelectedIndex() == 3) {
            bundle_day_ = 15;
        } else if (bundleField_.getSelectedIndex() == 4) {
            bundle_day_ = 20;
        } else if (bundleField_.getSelectedIndex() == 5) {
            bundle_day_ = 25;
        }
        if (searchWordField_.getText().trim().equals("")) {
            search
                     = new SearchCondition[]{
                         new SearchCondition(
                                  new Column("請求月", "term_month", ColumnType.DATE),
                                  CompareOperator.GE,
                                  startDate,//startDate
                                  CombineOperator.AND),
                         new SearchCondition(
                                  new Column("請求月", "term_month", ColumnType.DATE),
                                  CompareOperator.LE,
                                  endTime,//endTime
                                  CombineOperator.AND),
                         new SearchCondition(
                                  new Column("締日", "bundle_day", ColumnType.INT4),
                                  CompareOperator.EQ,
                                  bundle_day_,//締日
                                  CombineOperator.NONE)};
        } else {
            search
                     = new SearchCondition[]{
                         new SearchCondition(
                                  new Column("請求月", "term_month", ColumnType.DATE),
                                  CompareOperator.GE,
                                  startDate,//startDate
                                  CombineOperator.AND),
                         new SearchCondition(
                                  new Column("請求月", "term_month", ColumnType.DATE),
                                  CompareOperator.LE,
                                  endTime,//endTime
                                  CombineOperator.AND),
                         new SearchCondition(
                                  new Column("得意先名", "bill_name", ColumnType.TEXT),
                                  CompareOperator.LIKE,
                                  searchWordField_.getText().trim(),
                                  CombineOperator.NONE)};

        }

        if (0 < searchCondition_.size()) {
            search = searchCondition_.toArray(new SearchCondition[0]);
        }
        SortCondition[] sort
                 = new SortCondition[]{
                     new SortCondition(
                              new Column("得意先NO", "bill_id", ColumnType.INT4),
                              SortOrder.ASC),
                     new SortCondition(
                              new Column("請求日", "term_date", ColumnType.DATE),
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

        BillDetailsList.model_reflesh(0);

    }

    private class SearchAction implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent event) {
            updateView();
        }
    }

    private class EditAction implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent event) {

        }
    }

    private class DeleteAction implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent event) {
            int row = dataTable_.getSelectedRow();
            if (0 > row) {
                JOptionPane.showMessageDialog(
                         BillList.this,
                         "一覧から項目を選択してください.",
                         "確認",
                         JOptionPane.WARNING_MESSAGE);

                return;
            }

            int result = JOptionPane.showConfirmDialog(
                     BillList.this,
                     "選択した請求書データを削除します。よろしいですか？",
                     "確認",
                     JOptionPane.YES_NO_OPTION);
            if (JOptionPane.YES_OPTION == result) {
                if (model_.delete((Long) model_.getValueAt(row, "sno"))) {
                    updateView();
                } else {
                    JOptionPane.showMessageDialog(
                             BillList.this,
                             "請求金額更新に失敗しました.",
                             "警告",
                             JOptionPane.WARNING_MESSAGE);

                }
            }
        }
    }

    private class UpdateAction implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent event) {
            int row = dataTable_.getSelectedRow();
            if (0 > row) {
                JOptionPane.showMessageDialog(
                         BillList.this,
                         "一覧から項目を選択してください.",
                         "確認",
                         JOptionPane.WARNING_MESSAGE);

                return;
            }

            int result = JOptionPane.showConfirmDialog(
                     BillList.this,
                     "選択した項目の前回請求・当月請求金額を更新します。よろしいですか？",
                     "確認",
                     JOptionPane.YES_NO_OPTION);
            if (JOptionPane.YES_OPTION == result) {
                if (model_.bill_update(row)) {
                    updateView();
                } else {
                    JOptionPane.showMessageDialog(
                             BillList.this,
                             "請求金額更新に失敗しました.",
                             "警告",
                             JOptionPane.WARNING_MESSAGE);

                }
            }
        }
    }

    private class PrintAction implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent event) {
            int row = dataTable_.getSelectedRow();

            if (0 > row) {
                JOptionPane.showMessageDialog(
                         BillList.this,
                         "一覧から項目を選択してください.",
                         "警告",
                         JOptionPane.WARNING_MESSAGE);

                return;
            }

            JDialog dialog = DialogHelper.getNewInstance(BillList.this, false);
            dialog.add(new BillReportDialog(con_,
                     (Date) model_.getValueAt(row, "term_month"),
                     (Integer) model_.getValueAt(row, "bill_id"),
                     (String) model_.getValueAt(row, "bill_name"),
                     (long) model_.getValueAt(row, "sno"),
                     (Date) model_.getValueAt(row, "start_date"),
                     (Date) model_.getValueAt(row, "end_date"),
                     (Date) model_.getValueAt(row, "term_date")));

            dialog.setTitle("請求書印刷");
            dialog.setSize(aks.industrialwaste.ui.DialogHelper.getMaximumSize());
            dialog.setBounds(400, 300, 520, 300);
            dialog.setModal(true);
            dialog.setVisible(true);

            updateView();

        }
    }

    private class RefreshAction implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent event) {
            updateView();
        }
    }

    @Override
    protected void showEditDialog(int mode) {
    }

    private void getDetailsAction(java.awt.event.MouseEvent evt) {
        int row = dataTable_.getSelectedRow();
        getDetails(row);
    }

    private void getDetails(int row) {

        //請求明細の更新
        BillDetailsList.term_month_ = (Date) model_.getValueAt(row, "term_month");
        BillDetailsList.bill_id_ = (Integer) model_.getValueAt(row, "bill_id");
        BillDetailsList.start_date_ = (Date) model_.getValueAt(row, "start_date");
        BillDetailsList.end_date_ = (Date) model_.getValueAt(row, "end_date");
        BillDetailsList.model_reflesh((Integer) model_.getValueAt(row, "bill_id"));
    }
}
