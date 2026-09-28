package aks.industrialwaste.recycle.ui.payment;

import aks.industrialwaste.recycle.ui.MainFrame;
import aks.industrialwaste.database.Column;
import aks.industrialwaste.database.ColumnType;
import aks.industrialwaste.database.CombineOperator;
import aks.industrialwaste.database.CompareOperator;
import aks.industrialwaste.database.SearchCondition;
import aks.industrialwaste.database.SortCondition;
import aks.industrialwaste.database.SortOrder;
import aks.industrialwaste.ui.DialogHelper;
import aks.industrialwaste.ui.ListPanel;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
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
import jp.co.astax.awt.FontUtility;
import java.sql.Connection;
import aks.swing.JTextField;
import com.ibm.icu.text.Transliterator;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Calendar;
import java.util.Date;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.SwingUtilities;
import javax.swing.table.JTableHeader;
import jp.co.astax.swing.DateTimeField;

public class PaymentDetailsList extends ListPanel {

    public static List<SearchCondition> searchCondition_;
    public static List<SortCondition> sortCondition_;
    public static PaymentDetailsListTableModel model_;
    private static JTextField searchWordField_;
    private DateTimeField startDateField_;
    private DateTimeField endDateField_;
    private Transliterator tr1_;
    private Transliterator tr2_;
    private static Connection con_;
    public static long bill_sno_;
    public static boolean bill_detail_view_ = false;
    private JPopupMenu popup;

    public PaymentDetailsList(Connection con) {
        model_ = new PaymentDetailsListTableModel(con);
        con_ = con;
        dataTable_.setModel(model_);
        dataTable_.setColumnModel(new PaymentDetailsListTableColumnModel());
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

        popup = new JPopupMenu();// ポップアップメニューを生成

        Font font14 = FontUtility.getFont("メイリオ", Font.PLAIN, 14);
        Font font24 = FontUtility.getFont("メイリオ", Font.PLAIN, 24);
        // タイトル
        titleField_.setText("入金明細");
        titleField_.setFont(font24);
        countField_.setVisible(false);

        //抽出日
        JPanel panel = new JPanel();
        panel.setLayout(new java.awt.GridBagLayout());
        JLabel lbl = new JLabel("入金日");
        lbl.setFont(font14);
        addContainer(
                 panel, lbl,
                 0, 0, 1, 1, 0.0, 0.0,
                 GridBagConstraints.WEST, GridBagConstraints.NONE,
                 new Insets(2, 2, 2, 0), 0, 0);
        startDateField_ = new DateTimeField(DateTimeField.DateFormat.YMD);
        startDateField_.setFont(font14);
        startDateField_.setMinimumSize(new Dimension(250, 30));
        startDateField_.setPreferredSize(new Dimension(250, 30));
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
        endDateField_ = new DateTimeField(DateTimeField.DateFormat.YMD);
        endDateField_.setFont(font14);
        endDateField_.setMinimumSize(new Dimension(250, 30));
        endDateField_.setPreferredSize(new Dimension(250, 30));
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
                 4, 0, 1, 1, 1.0, 0.0,
                 GridBagConstraints.WEST, GridBagConstraints.NONE,
                 new Insets(2, 20, 2, 2), 0, 0);

        //検索
        lbl = new JLabel("請求先検索");
        lbl.setFont(font14);
        lbl.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        addContainer(
                 panel, lbl,
                 5, 0, 1, 1, 0.0, 0.0,
                 GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
                 new Insets(2, 2, 2, 2), 0, 0);

        searchWordField_ = new JTextField();
        searchWordField_.setFont(font14);
        searchWordField_.setMargin(new Insets(0, 0, 0, 0));
        searchWordField_.setMinimumSize(new Dimension(150, 35));
        searchWordField_.setPreferredSize(new Dimension(150, 35));
        searchWordField_.addActionListener(new SearchAction());
        addContainer(
                 panel, searchWordField_,
                 6, 0, 1, 1, 0.0, 0.0,
                 GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
                 new Insets(0, 0, 0, 0), 0, 0);

        addContainer(
                 northPane_, panel,
                 0, 1, 1, 1, 1.0, 0.0,
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
        JTableHeader jh = dataTable_.getTableHeader();
        jh.setFont(font14);
        //右クリック時のPOPアップメニュー作成///////////////////////////////////////
        JMenuItem item0 = new JMenuItem("最新の情報に更新", MainFrame.icon_reload);
        JMenuItem item1 = new JMenuItem("入金追加", MainFrame.icon_add);
        JMenuItem item2 = new JMenuItem("入金訂正", MainFrame.icon_edit);
        JMenuItem item3 = new JMenuItem("入金削除", MainFrame.icon_delete);
        popup.add(item0);
        popup.add(item1);
        popup.add(item2);
        popup.addSeparator(); //セパレーター
        popup.add(item3);
        // 各メニュー選択時の処理
        item0.addActionListener(new RefreshAction());
        item1.addActionListener(new AppendAction());
        item2.addActionListener(new EditAction());
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

        // ボタン
        // 追加
        b = new JButton("入金追加");
        b.setFont(font14);
        b.setMargin(new Insets(2, 2, 2, 2));
        b.setMinimumSize(new Dimension(120, 30));
        b.setPreferredSize(new Dimension(120, 30));
        b.addActionListener(new AppendAction());
        addContainer(
                 southPane_, b,
                 0, 0, 1, 1, 0.0, 0.0,
                 GridBagConstraints.WEST, GridBagConstraints.NONE,
                 new Insets(2, 2, 2, 2), 0, 0);
        // 訂正
        b = new JButton("入金訂正");
        b.setFont(font14);
        b.setMargin(new Insets(2, 2, 2, 2));
        b.setMinimumSize(new Dimension(120, 30));
        b.setPreferredSize(new Dimension(120, 30));
        b.addActionListener(new EditAction());
        addContainer(
                 southPane_, b,
                 1, 0, 1, 1, 0.0, 0.0,
                 GridBagConstraints.WEST, GridBagConstraints.NONE,
                 new Insets(2, 2, 2, 2), 0, 0);
        // 削除
        b = new JButton("入金削除");
        b.setFont(font14);
        b.setMargin(new Insets(2, 2, 2, 2));
        b.setMinimumSize(new Dimension(120, 30));
        b.setPreferredSize(new Dimension(120, 30));
        b.addActionListener(new DeleteAction());
        addContainer(
                 southPane_, b,
                 2, 0, 1, 1, 0.5, 0.0,
                 GridBagConstraints.WEST, GridBagConstraints.NONE,
                 new Insets(2, 2, 2, 2), 0, 0);

        Calendar c = Calendar.getInstance();
        c.set(Calendar.DAY_OF_MONTH, 1);
        startDateField_.setValue(c);

    }

    private class RefreshAction implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent event) {
            updateView();
        }
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

        //20110603 add
        String searchWordTemp;
        searchWordTemp = tr1_.transliterate(searchWordField_.getText().trim());
        searchWordTemp = tr2_.transliterate(searchWordTemp);

        Date startDate = startDateField_.getValueAtDate();
        Date endTime = endDateField_.getValueAtDate();
        // 検索条件, ソート条件を設定
        SearchCondition[] search;
        if (searchWordField_.getText().trim().equals("")) {
            search
                     = new SearchCondition[]{
                         new SearchCondition(
                                  new Column("入金日", "payment_date", ColumnType.DATE),
                                  CompareOperator.GE,
                                  startDate,
                                  CombineOperator.AND),
                         new SearchCondition(
                                  new Column("入金日", "payment_date", ColumnType.DATE),
                                  CompareOperator.LE,
                                  endTime,
                                  CombineOperator.NONE)};
        } else {
            search
                     = new SearchCondition[]{
                         new SearchCondition(
                                  new Column("入金日", "payment_date", ColumnType.DATE),
                                  CompareOperator.GE,
                                  startDate,
                                  CombineOperator.AND),
                         new SearchCondition(
                                  new Column("入金日", "payment_date", ColumnType.DATE),
                                  CompareOperator.LE,
                                  endTime,
                                  CombineOperator.AND),
                         new SearchCondition(
                                  new Column("請求先名", "m_bill_name", ColumnType.TEXT),
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
                              new Column("請求先ID", "bill_id", ColumnType.INT4),
                              SortOrder.ASC),
                     new SortCondition(
                              new Column("開始日", "payment_date", ColumnType.DATE),
                              SortOrder.ASC),
                     new SortCondition(
                              new Column("取引区分ID", "p_class_id", ColumnType.INT4),
                              SortOrder.ASC)};
        if (0 < sortCondition_.size()) {
            sort = sortCondition_.toArray(new SortCondition[0]);
        }

        model_.refresh(search, sort);
        // 件数表示
        countField_.setText(String.format("%d", model_.getRowCount()));

    }

    private class SearchAction implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent event) {
            model_reflesh(bill_sno_);
        }
    }

    private class AppendAction implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent event) {
            JDialog dialog = DialogHelper.getNewInstance(PaymentDetailsList.this, true);
            dialog.add(new PaymentDetailsListInput(con_, model_, -1));
            dialog.setSize(500, 400);
            dialog.setTitle("入金明細 - 追加");
            dialog.setLocation(250, 100);
            dialog.setVisible(true);

            //少し待つ
            try {
                Thread.sleep(100);
            } catch (Exception e) {
            }

            updateView();
        }
    }

    private class EditAction implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent event) {

            showEditDialog(0);

        }
    }

    private class DeleteAction implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent event) {
            int row = dataTable_.getSelectedRow();
            if (0 > row) {
                JOptionPane.showMessageDialog(
                         PaymentDetailsList.this,
                         "一覧から項目を選択してください.",
                         "確認",
                         JOptionPane.WARNING_MESSAGE);

                return;
            }

            int result = JOptionPane.showConfirmDialog(
                     PaymentDetailsList.this,
                     "選択した項目を削除します. よろしいですか?",
                     "確認",
                     JOptionPane.YES_NO_OPTION);
            if (JOptionPane.YES_OPTION == result) {
                if (model_.delete(row)) {
                    model_reflesh(bill_sno_);
                } else {
                    JOptionPane.showMessageDialog(
                             PaymentDetailsList.this,
                             "削除に失敗しました.",
                             "警告",
                             JOptionPane.WARNING_MESSAGE);
                }
            }
        }
    }

    public static void model_reflesh(long bill_sno) {

        bill_sno_ = bill_sno;
        // 検索条件, ソート条件を設定
        SearchCondition[] search;
        if (searchWordField_.getText().trim().equals("")) {
            search = null;
        } else {
            search
                     = new SearchCondition[]{
                         new SearchCondition(
                                  new Column("請求先名", "m_bill_name", ColumnType.TEXT),
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
                              new Column("入金日", "payment_date", ColumnType.DATE),
                              SortOrder.DESC),
                     new SortCondition(
                              new Column("取引区分ID", "p_class_id", ColumnType.INT4),
                              SortOrder.DESC)};

        if (0 < sortCondition_.size()) {
            sort = sortCondition_.toArray(new SortCondition[0]);
        }

        // リスト更新
        model_.refresh(search, sort);
    }

    @Override
    protected void showEditDialog(int mode) {
        int row = dataTable_.getSelectedRow();
        if (0 > row) {
            JOptionPane.showMessageDialog(
                     PaymentDetailsList.this,
                     "一覧から項目を選択してください.",
                     "警告",
                     JOptionPane.WARNING_MESSAGE);

            return;
        }

        //明細表示フラグを立てる(明細のキーイベント用)
        bill_detail_view_ = true;

        JDialog dialog = DialogHelper.getNewInstance(PaymentDetailsList.this, true);
        dialog.add(new PaymentDetailsListInput(con_, model_, row));
        dialog.setSize(500, 400);
        dialog.setTitle("入金明細 - 訂正");
        dialog.setLocation(250, 100);
        dialog.setVisible(true);

        //少し待つ
        try {
            Thread.sleep(100);
        } catch (Exception e) {
        }

        updateView();
    }

}
