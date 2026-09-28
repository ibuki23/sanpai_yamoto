package aks.industrialwaste.recycle.ui.payment;

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
import java.util.List;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.event.AncestorEvent;
import aks.industrialwaste.recycle.ui.MainFrame;
import jp.co.astax.swing.DateTimeField;
import jp.co.astax.awt.FontUtility;
import java.sql.Connection;
import aks.swing.JTextField;
import com.ibm.icu.text.Transliterator;
import aks.industrialwaste.database.Column;
import aks.industrialwaste.database.ColumnType;
import aks.industrialwaste.database.CombineOperator;
import aks.industrialwaste.database.CompareOperator;
import aks.industrialwaste.database.SearchCondition;
import aks.industrialwaste.database.SortCondition;
import aks.industrialwaste.database.SortOrder;
import aks.industrialwaste.ui.ListPanel;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.SwingUtilities;
import javax.swing.table.JTableHeader;

public class PaymentList extends ListPanel //implements Runnable
{

    private List<SearchCondition> searchCondition_;
    private List<SortCondition> sortCondition_;
    private PaymentListTableModel model_;
    private DateTimeField startDateField_;
    private DateTimeField endDateField_;
    private JTextField searchWordField_;
    private Transliterator tr1_;
    private Transliterator tr2_;
    private Connection con_;

    private JPopupMenu popup;

    public PaymentList(Connection con) {
        con_ = con;
        model_ = new PaymentListTableModel(con_);
        dataTable_.setModel(model_);
        dataTable_.setColumnModel(new PaymentListTableColumnModel());
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

        // タイトル
        titleField_.setText("入金予定一覧");
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

        //検索
        lbl = new JLabel("請求先検索");
        lbl.setFont(font14);
        panel.add(lbl);
        addContainer(
                 panel, lbl,
                 6, 0, 2, 1, 0.1, 0.0,
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
        JTableHeader jh = dataTable_.getTableHeader();
        jh.setFont(font14);
        //右クリック時のPOPアップメニュー作成///////////////////////////////////////
        JMenuItem item0 = new JMenuItem("最新の情報に更新", MainFrame.icon_reload);
        popup.add(item0);
        popup.addSeparator();
        // 各メニュー選択時の処理
        item0.addActionListener(new RefreshAction());
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

        Date startDate = startDateField_.getValueAtDate();
        Date endTime = endDateField_.getValueAtDate();
        // 検索条件, ソート条件を設定
        SearchCondition[] search;
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

    }

    private class SearchAction implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent event) {
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

}
