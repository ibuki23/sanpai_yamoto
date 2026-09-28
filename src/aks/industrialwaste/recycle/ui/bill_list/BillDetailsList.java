package aks.industrialwaste.recycle.ui.bill_list;

import aks.industrialwaste.database.Column;
import aks.industrialwaste.database.ColumnType;
import aks.industrialwaste.database.CombineOperator;
import aks.industrialwaste.database.CompareOperator;
import aks.industrialwaste.database.SearchCondition;
import aks.industrialwaste.database.SortCondition;
import aks.industrialwaste.database.SortOrder;
import aks.industrialwaste.recycle.ui.MainFrame;
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
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JComboBox;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.SwingUtilities;
import javax.swing.table.JTableHeader;

public class BillDetailsList extends ListPanel //implements Runnable
{

    public static List<SearchCondition> searchCondition_;
    public static List<SortCondition> sortCondition_;
    public static BillDetailsListTableModel model_;
    private static JTextField searchWordField_;
    public static JTextField totalValueField_;
    public static JTextField sumValueField_;
    public static JTextField serviceValueField_;
    private Transliterator tr1_;
    private Transliterator tr2_;
    private static Connection con_;
    public static int bill_sno_;
    public static int bill_id_;
    public static Date term_month_, start_date_, end_date_;
    public static boolean bill_detail_view_ = false;
    private String[] tax_;
    private JComboBox tax_Field_;
    private JPopupMenu popup;

    public BillDetailsList(Connection con) {
        model_ = new BillDetailsListTableModel(con);
        con_ = con;
        dataTable_.setModel(model_);
        dataTable_.setColumnModel(new BillDetailsListTableColumnModel());
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

        tax_ = new String[]{"0", "5", "10", "15", "20"};

        popup = new JPopupMenu();// ポップアップメニューを生成

        Font font14 = FontUtility.getFont("メイリオ", Font.PLAIN, 14);
        Font font24 = FontUtility.getFont("メイリオ", Font.PLAIN, 24);
        // タイトル
        titleField_.setText("請求明細");
        titleField_.setFont(font24);
        countField_.setVisible(false);

        JPanel panel = new JPanel();
        panel.setLayout(new java.awt.GridBagLayout());

        //検索
        JLabel lbl = new JLabel("検索");
        lbl.setFont(font14);
        lbl.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        addContainer(
                 panel, lbl,
                 0, 0, 1, 1, 0.1, 0.0,
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
                 1, 0, 1, 1, 0.0, 0.0,
                 GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
                 new Insets(0, 0, 0, 0), 0, 0);

        lbl = new JLabel(" 小計");
        lbl.setFont(font14);
        lbl.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);

        totalValueField_ = new JTextField();
        totalValueField_.setFont(font14);
        totalValueField_.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        totalValueField_.setMargin(new Insets(0, 0, 0, 0));
        totalValueField_.setEditable(false);
        totalValueField_.setFocusable(false);
        totalValueField_.setMinimumSize(new Dimension(100, 35));
        totalValueField_.setPreferredSize(new Dimension(100, 35));
        totalValueField_.addActionListener(new SearchAction());

        // 値引率
        lbl = new JLabel(" 値引率", JLabel.LEFT);
        lbl.setFont(font14);
        tax_Field_ = new JComboBox();
        tax_Field_.setFont(font14);
        tax_Field_.setMinimumSize(new Dimension(80, 30));
        tax_Field_.setPreferredSize(new Dimension(80, 30));
        tax_Field_.setEditable(true);
        tax_Field_.addItemListener(new java.awt.event.ItemListener() {
            @Override
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                if (dataTable_.getRowCount() > 0) {
                    calcSumValue();
                }

            }
        });
        tax_Field_.setModel(new javax.swing.DefaultComboBoxModel(tax_));
        //合計欄
        lbl = new JLabel(" 値引額");
        lbl.setFont(font14);
        lbl.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);

        serviceValueField_ = new JTextField();
        serviceValueField_.setFont(font14);
        serviceValueField_.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        serviceValueField_.setMargin(new Insets(0, 0, 0, 0));
        serviceValueField_.setMinimumSize(new Dimension(100, 35));
        serviceValueField_.setPreferredSize(new Dimension(100, 35));
        serviceValueField_.addActionListener(new SearchAction());

        //合計欄
        lbl = new JLabel(" 合計");
        lbl.setFont(font14);
        lbl.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);

        sumValueField_ = new JTextField();
        sumValueField_.setFont(font14);
        sumValueField_.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        sumValueField_.setMargin(new Insets(0, 0, 0, 0));
        sumValueField_.setEditable(false);
        sumValueField_.setFocusable(false);
        sumValueField_.setMinimumSize(new Dimension(100, 35));
        sumValueField_.setPreferredSize(new Dimension(100, 35));
        sumValueField_.addActionListener(new SearchAction());

        // ボタン
        // 値引追加
        JButton cb = new JButton("値引き追加");
        cb.setFont(font14);
        cb.setMargin(new Insets(2, 2, 2, 2));
        cb.setMinimumSize(new Dimension(130, 30));
        cb.setPreferredSize(new Dimension(130, 30));
        cb.addActionListener(new AppendAction2());

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
        dataTable_.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        dataTable_.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mousePressed(java.awt.event.MouseEvent evt) {
                calcSumValue();
            }
        });
        JTableHeader jh = dataTable_.getTableHeader();
        jh.setFont(font14);
        //右クリック時のPOPアップメニュー作成///////////////////////////////////////
        JMenuItem item0 = new JMenuItem("値引き追加");
        JMenuItem item1 = new JMenuItem("明細追加", MainFrame.icon_add);
        JMenuItem item2 = new JMenuItem("明細訂正", MainFrame.icon_edit);
        JMenuItem item3 = new JMenuItem("明細削除", MainFrame.icon_delete);
        popup.add(item1);
        popup.add(item2);
        popup.addSeparator(); //セパレーター
        popup.add(item3);
        // 各メニュー選択時の処理
        item0.addActionListener(new AppendAction2());
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
        ///////////////////////////////////////////////右クリックメニュー実装終了///

        // ボタン
        // 追加
        JButton b = new JButton("明細追加");
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
        b = new JButton("明細訂正");
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
        b = new JButton("明細削除");
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
    }

    @Override
    protected void updateView() {

        String searchWordTemp;
        searchWordTemp = tr1_.transliterate(searchWordField_.getText().trim());
        searchWordTemp = tr2_.transliterate(searchWordTemp);

        // 検索条件, ソート条件を設定
        SearchCondition[] search;
        if (searchWordField_.getText().trim().equals("")) {
            search
                     = new SearchCondition[]{
                         new SearchCondition(
                                  new Column("請求先コード", "bill_id", ColumnType.INT4),
                                  CompareOperator.EQ,
                                  bill_id_,
                                  CombineOperator.AND),
                         new SearchCondition(
                                  new Column("起算開始日", "start_date", ColumnType.DATE),
                                  CompareOperator.GE,
                                  start_date_,
                                  CombineOperator.AND),
                         new SearchCondition(
                                  new Column("起算終了日", "end_date", ColumnType.DATE),
                                  CompareOperator.LE,
                                  end_date_,
                                  CombineOperator.NONE)};
        } else {
            search
                     = new SearchCondition[]{
                         new SearchCondition(
                                  new Column("請求先コード", "bill_id", ColumnType.INT4),
                                  CompareOperator.EQ,
                                  bill_id_,
                                  CombineOperator.AND),
                         new SearchCondition(
                                  new Column("起算開始日", "start_date", ColumnType.DATE),
                                  CompareOperator.GE,
                                  start_date_,
                                  CombineOperator.AND),
                         new SearchCondition(
                                  new Column("起算終了日", "end_date", ColumnType.DATE),
                                  CompareOperator.LE,
                                  end_date_,
                                  CombineOperator.AND),
                         new SearchCondition(
                                  new Column("請求品名", "bill_name", ColumnType.TEXT),
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
                              new Column("現場ID", "dis_office_id", ColumnType.INT4),
                              SortOrder.ASC),
                     new SortCondition(
                              new Column("所属日", "term_date", ColumnType.DATE),
                              SortOrder.ASC),
                     new SortCondition(
                              new Column("車番", "car_no", ColumnType.INT4),
                              SortOrder.ASC),
                     new SortCondition(
                              new Column("請求分類NO", "b_class_id", ColumnType.INT4),
                              SortOrder.ASC),
                     new SortCondition(
                              new Column("商品NO", "goods_id", ColumnType.INT4),
                              SortOrder.ASC),
                     new SortCondition(
                              new Column("請求金額", "bill_price", ColumnType.INT4),
                              SortOrder.DESC)};

        if (0 < sortCondition_.size()) {
            sort = sortCondition_.toArray(new SortCondition[0]);
        }

        // リスト更新
        model_.refresh(search, sort);
        // 件数表示
        countField_.setText(String.format("%d", model_.getRowCount()));

    }

    private class SearchAction implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent event) {
        }
    }

    private class AppendAction implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent event) {
            if (bill_sno_ > 0) {

                //明細表示フラグを立てる(明細のキーイベント用)
                int row = dataTable_.getSelectedRow();
                bill_detail_view_ = true;
                JDialog dialog = DialogHelper.getNewInstance(BillDetailsList.this, true);
                dialog.add(new BillDetailsListInput(true, con_, model_, row, bill_sno_));
                dialog.setSize(650, 420);
                dialog.setTitle("請求明細 - 追加");
                dialog.setLocation(250, 100);
                dialog.setVisible(true);

                //少し待つ
                try {
                    Thread.sleep(100);
                } catch (Exception e) {
                }

                bill_detail_view_ = false;

                model_reflesh(bill_sno_);
            }
        }
    }

    private class AppendAction2 implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent event) {
            int row = dataTable_.getSelectedRow();
            if (0 > row) {
                JOptionPane.showMessageDialog(
                         BillDetailsList.this,
                         "一覧から項目を選択してください.",
                         "確認",
                         JOptionPane.WARNING_MESSAGE);

                return;
            }

            int result = JOptionPane.showConfirmDialog(
                     BillDetailsList.this,
                     "値引き追加します。よろしいですか？",
                     "確認",
                     JOptionPane.YES_NO_OPTION);
            if (JOptionPane.YES_OPTION == result) {
                if (model_.insert2(model_.getColumns(row), -1 * Integer.valueOf(serviceValueField_.getText().trim().replace(",", "")))) {
                    //値引コンボリセット
                    tax_Field_.setSelectedIndex(0);
                    model_reflesh(bill_sno_);
                } else {
                    JOptionPane.showMessageDialog(
                             BillDetailsList.this,
                             "処理に失敗しました。",
                             "警告",
                             JOptionPane.WARNING_MESSAGE);
                }
            }
        }
    }

    private class EditAction implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent event) {
            if (bill_sno_ > 0) {
                showEditDialog(0);
            }
        }
    }

    private class DeleteAction implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent event) {
            int row = dataTable_.getSelectedRow();
            if (0 > row) {
                JOptionPane.showMessageDialog(
                         BillDetailsList.this,
                         "一覧から項目を選択してください.",
                         "確認",
                         JOptionPane.WARNING_MESSAGE);

                return;
            }

            int result = JOptionPane.showConfirmDialog(
                     BillDetailsList.this,
                     "選択した項目を削除します. よろしいですか?",
                     "確認",
                     JOptionPane.YES_NO_OPTION);
            if (JOptionPane.YES_OPTION == result) {
                if (model_.delete(row)) {
                    model_reflesh(bill_sno_);
                } else {
                    JOptionPane.showMessageDialog(
                             BillDetailsList.this,
                             "削除に失敗しました.",
                             "警告",
                             JOptionPane.WARNING_MESSAGE);
                }
            }
        }
    }

    private void calcSumValue() {
        //値引き後の合計金額表示
        totalValueField_.setText(String.format("%1$,3d", Math.round(getSum())));
        int row = dataTable_.getSelectedRow();

        if (row < 0) {
            row = 0;
        }

        //値引後の合計欄計算
        if ((Integer) model_.getValueAt(row, "tax_no") == 0) {
            //四捨五入
            sumValueField_.setText(String.format("%1$,3d", Math.round(getSum() * (1 - Double.valueOf(tax_Field_.getSelectedItem().toString()) / 100))));
        } else if ((Integer) model_.getValueAt(row, "tax_no") == 1) {
            //切り捨て
            sumValueField_.setText(String.format("%1$,3d", Math.round(Math.floor(getSum() * (1 - Double.valueOf(tax_Field_.getSelectedItem().toString()) / 100)))));
        } else if ((Integer) model_.getValueAt(row, "tax_no") == 2) {
            //切り上げ
            sumValueField_.setText(String.format("%1$,3d", Math.round(Math.ceil(getSum() * (1 - Double.valueOf(tax_Field_.getSelectedItem().toString()) / 100)))));
        } else {
            //四捨五入
            sumValueField_.setText(String.format("%1$,3d", Math.round(getSum() * (1 - Double.valueOf(tax_Field_.getSelectedItem().toString()) / 100))));
        }

        //値引額の計算
        int serviceValue = 0;
        serviceValue = Integer.valueOf(totalValueField_.getText().trim().replace(",", "")) - Integer.valueOf(sumValueField_.getText().trim().replace(",", ""));
        serviceValueField_.setText(String.format("%1$,3d", serviceValue));

    }

    public static void model_reflesh(int bill_id) {

        bill_sno_ = bill_id;
        // 検索条件, ソート条件を設定
        SearchCondition[] search;
        if (searchWordField_.getText().trim().equals("")) {
            search
                     = new SearchCondition[]{
                         new SearchCondition(
                                  new Column("請求先コード", "bill_id", ColumnType.INT4),
                                  CompareOperator.EQ,
                                  bill_id_,
                                  CombineOperator.AND),
                         new SearchCondition(
                                  new Column("起算開始日", "start_date", ColumnType.DATE),
                                  CompareOperator.GE,
                                  start_date_,
                                  CombineOperator.AND),
                         new SearchCondition(
                                  new Column("起算終了日", "end_date", ColumnType.DATE),
                                  CompareOperator.LE,
                                  end_date_,
                                  CombineOperator.NONE)};
        } else {
            search
                     = new SearchCondition[]{
                         new SearchCondition(
                                  new Column("請求先コード", "bill_id", ColumnType.INT4),
                                  CompareOperator.EQ,
                                  bill_id_,
                                  CombineOperator.AND),
                         new SearchCondition(
                                  new Column("起算開始日", "start_date", ColumnType.DATE),
                                  CompareOperator.GE,
                                  start_date_,
                                  CombineOperator.AND),
                         new SearchCondition(
                                  new Column("起算終了日", "end_date", ColumnType.DATE),
                                  CompareOperator.LE,
                                  end_date_,
                                  CombineOperator.AND),
                         new SearchCondition(
                                  new Column("請求品名", "bill_name", ColumnType.TEXT),
                                  CompareOperator.LIKE,
                                  searchWordField_.getText().trim(),
                                  CombineOperator.NONE)};
        }

        if (0 < searchCondition_.size()) {
            search = searchCondition_.toArray(new SearchCondition[0]);
        }

        SortCondition[] sort
                 = new SortCondition[]{
                     //20140624 所属日ごと現場ごとにソート
                     new SortCondition(
                              new Column("現場ID", "dis_office_id", ColumnType.INT4),
                              SortOrder.ASC),
                     new SortCondition(
                              new Column("所属日", "term_date", ColumnType.DATE),
                              SortOrder.ASC),
                     new SortCondition(
                              new Column("車番", "car_no", ColumnType.INT4),
                              SortOrder.ASC),
                     new SortCondition(
                              new Column("計量SNO", "weight_sno", ColumnType.INT8),
                              SortOrder.DESC),
                     new SortCondition(
                              new Column("商品NO", "goods_id", ColumnType.INT4),
                              SortOrder.ASC)};

        if (0 < sortCondition_.size()) {
            sort = sortCondition_.toArray(new SortCondition[0]);
        }

        // リスト更新
        model_.refresh(search, sort);
//    // 小計表示
        double total = 0;
        for (int i = 0; i < model_.getRowCount(); i++) {
            total += (Double) model_.getValueAt(i, "bill_price");
        }

        //3桁区切り
        totalValueField_.setText(String.format("%1$,3d", Math.round(total)));

    }

    @Override
    protected void showEditDialog(int mode) {
        int row = dataTable_.getSelectedRow();
        if (0 > row) {
            JOptionPane.showMessageDialog(
                     BillDetailsList.this,
                     "一覧から項目を選択してください.",
                     "警告",
                     JOptionPane.WARNING_MESSAGE);

            return;
        }

        //明細表示フラグを立てる(明細のキーイベント用)
        bill_detail_view_ = true;

        JDialog dialog = DialogHelper.getNewInstance(BillDetailsList.this, true);
        dialog.add(new BillDetailsListInput(false, con_, model_, row, bill_sno_));
        dialog.setSize(620, 380);
        dialog.setTitle("請求明細 - 訂正");
        dialog.setLocation(250, 100);
        dialog.setVisible(true);

        //少し待つ
        try {
            Thread.sleep(100);
        } catch (Exception e) {
        }

        bill_detail_view_ = false;

        model_reflesh(bill_sno_);
    }

    private int getSum() {

        int ret = 0;
        int row = dataTable_.getSelectedRow();
        if (row >= 0) {
            //出荷総数の表示更新
            long bill_sno = ((Integer) model_.getValueAt(row, "bill_sno"));
            try {

                String s = "select sum(bill_price)as total from v1_d_bill_detail "
                         + " where v1_d_bill_detail.bill_sno = ? ;";

                PreparedStatement ps = con_.prepareStatement(s);
                try {
                    ps.setObject(1, bill_sno);
                    ResultSet rs = ps.executeQuery();

                    if (rs.next()) {
                        ret = (Integer) rs.getInt("total");
                    }

                } finally {
                    ps.close();
                }
            } catch (SQLException e) {
                System.out.print(e.toString());
            }

        } else {
            ret = Integer.valueOf(totalValueField_.getText().trim().replace(",", ""));
        }
        return ret;
    }
}
