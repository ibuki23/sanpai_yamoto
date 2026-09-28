package aks.industrialwaste.recycle.ui.master.car;

import aks.industrialwaste.database.Column;
import aks.industrialwaste.database.ColumnType;
import aks.industrialwaste.database.CombineOperator;
import aks.industrialwaste.database.CompareOperator;
import aks.industrialwaste.database.DBManager;
import aks.industrialwaste.database.SearchCondition;
import aks.industrialwaste.database.SortCondition;
import aks.industrialwaste.database.SortOrder;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.event.AncestorEvent;
import aks.industrialwaste.recycle.ui.MainFrame;
import aks.industrialwaste.ui.DialogHelper;
import aks.industrialwaste.ui.ListPanelMaster;
import aks.swing.JTextField;
import jp.co.astax.awt.FontUtility;
import javax.swing.JFileChooser;
import java.util.Calendar;
import java.io.File;
import java.util.HashMap;
import javax.swing.JPanel;
import javax.swing.table.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.print.PrinterException;
import java.text.MessageFormat;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JTable.PrintMode;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

public class Car extends ListPanelMaster {

    private CarTableModel model_;
    private List<SearchCondition> searchCondition_;
    private List<SortCondition> sortCondition_;
    private JTextField searchWordField_;
    private JPopupMenu popup;

    public Car(Connection con) {
        model_ = new CarTableModel(con);
        dataTable_.setModel(model_);
        dataTable_.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        dataTable_.setColumnModel(new CarTableColumnModel());
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

        Font font14 = FontUtility.getFont("メイリオ", Font.PLAIN, 14);

        popup = new JPopupMenu();// ポップアップメニューを生成
        // タイトル
        titleField_.setText("管理（車両）マスタ");

        //検索
        JPanel p = new JPanel();
        p.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));
        p.setAlignmentX(JPanel.CENTER_ALIGNMENT);

        searchWordField_ = new JTextField();
        searchWordField_.setFont(font14);
        searchWordField_.setMargin(new Insets(0, 2, 0, 2));
        searchWordField_.setMinimumSize(new Dimension(120, 35));
        searchWordField_.setPreferredSize(new Dimension(120, 35));
        searchWordField_.addActionListener(new RefreshAction());
        p.add(searchWordField_);
        addContainer(
                 northPane_, p,
                 0, 1, 1, 1, 0.0, 0.0,
                 GridBagConstraints.CENTER,
                 GridBagConstraints.HORIZONTAL,
                 new Insets(0, 0, 0, 0), 0, 0);

        JLabel s_lbl = new JLabel("※車番検索");
        s_lbl.setFont(font14);
        p.add(s_lbl);
        addContainer(
                 p, s_lbl,
                 0, 1, 2, 1, 0.0, 0.0,
                 GridBagConstraints.EAST,
                 GridBagConstraints.NONE,
                 new Insets(2, 2, 2, 2), 0, 0);
        addContainer(
                 northPane_, p,
                 0, 1, 1, 1, 0.9, 0.0,
                 GridBagConstraints.NORTHWEST,
                 GridBagConstraints.HORIZONTAL,
                 new Insets(0, 0, 0, 0), 0, 0);

        // データテーブル
        dataTable_.setFont(font14);
        dataTable_.setRowHeight(30);
        dataTable_.setColumnSelectionAllowed(false);
        dataTable_.setRowSelectionAllowed(true);
        dataTable_.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        dataTable_.getTableHeader().setReorderingAllowed(false);
        dataTable_.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

        //右クリック時のPOPアップメニュー作成///////////////////////////////////////
        JMenuItem item0 = new JMenuItem("最新の情報に更新", MainFrame.icon_reload);
        JMenuItem item1 = new JMenuItem("追加", MainFrame.icon_add);
        JMenuItem item2 = new JMenuItem("訂正", MainFrame.icon_edit);
        JMenuItem item3 = new JMenuItem("削除", MainFrame.icon_delete);
        JMenuItem item4 = new JMenuItem("一覧印刷", MainFrame.icon_print);
        popup.add(item0);
        popup.add(item4);
        if (DBManager.mode == 4 || DBManager.mode == 0) {
            popup.add(item1);
        }
        popup.add(item2);

        // 各メニュー選択時の処理
        item0.addActionListener(new RefreshAction());
        item1.addActionListener(new AppendAction());
        item2.addActionListener(new EditAction());
        item3.addActionListener(new DeleteAction());
        item4.addActionListener(new PrintAction());

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
        // 追加
        JButton b = new JButton("追加");
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
        b = new JButton("訂正");
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
        b = new JButton("削除");
        b.setFont(font14);
        b.setMargin(new Insets(2, 2, 2, 2));
        b.setMinimumSize(new Dimension(120, 30));
        b.setPreferredSize(new Dimension(120, 30));
        b.addActionListener(new DeleteAction());
        // 風袋登録
        b = new JButton("風袋登録");
        b.setFont(font14);
        b.setMargin(new Insets(2, 2, 2, 2));
        b.setMinimumSize(new Dimension(120, 30));
        b.setPreferredSize(new Dimension(120, 30));
        b.addActionListener(new WeighAction());
        addContainer(
                 southPane_, b,
                 3, 0, 1, 1, 0.0, 0.0,
                 GridBagConstraints.WEST, GridBagConstraints.NONE,
                 new Insets(2, 2, 2, 2), 0, 0);
        // 最大登録
        b = new JButton("最大積載量");
        b.setFont(font14);
        b.setMargin(new Insets(2, 2, 2, 2));
        b.setMinimumSize(new Dimension(120, 30));
        b.setPreferredSize(new Dimension(120, 30));
        b.addActionListener(new MaxWeighAction());
        addContainer(
                 southPane_, b,
                 4, 0, 1, 1, 0.5, 0.0,
                 GridBagConstraints.WEST, GridBagConstraints.NONE,
                 new Insets(2, 2, 2, 2), 0, 0);

        // 一覧印刷
        b = new JButton("一覧印刷");
        b.setFont(font14);
        b.setMargin(new Insets(2, 2, 2, 2));
        b.setMinimumSize(new Dimension(120, 30));
        b.setPreferredSize(new Dimension(120, 30));
        b.addActionListener(new PrintAction());
        addContainer(
                 southPane_, b,
                 8, 0, 1, 1, 0.5, 0.0,
                 GridBagConstraints.EAST, GridBagConstraints.NONE,
                 new Insets(2, 2, 2, 2), 0, 0);

        // CSV機能
        b = new JButton("CSV");
        b.setFont(font14);
        b.setMargin(new Insets(2, 2, 2, 2));
        b.setMinimumSize(new Dimension(120, 30));
        b.setPreferredSize(new Dimension(120, 30));
        b.addActionListener(new java.awt.event.ActionListener() {

            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ReportAction(evt);
            }
        });
        p = new JPanel();
        addContainer(
                 southPane_, b,
                 10, 0, 1, 1, 0.0, 0.0,
                 GridBagConstraints.EAST, GridBagConstraints.NONE,
                 new Insets(2, 2, 2, 2), 0, 0);
    }

    @Override
    protected void updateView() {
        try {
            // 検索条件, ソート条件を設定
            SearchCondition[] search;
            if (!searchWordField_.getText().equals("")) {
                search
                         = new SearchCondition[]{
                             new SearchCondition(
                                      new Column("車番", "serch_car_no", ColumnType.TEXT),
                                      CompareOperator.LIKE,
                                      searchWordField_.getText().trim(),
                                      CombineOperator.OR),
                             new SearchCondition(
                                      new Column("車番", "car_no", ColumnType.INT4),
                                      CompareOperator.EQ,
                                      Integer.valueOf(searchWordField_.getText().trim()),
                                      CombineOperator.OR),
                             new SearchCondition(
                                      new Column("車番", "car_id", ColumnType.INT4),
                                      CompareOperator.EQ,
                                      Integer.valueOf(searchWordField_.getText().trim()),
                                      CombineOperator.NONE)};

            } else {
                search = null;
            }
            if (0 < searchCondition_.size()) {
                search = searchCondition_.toArray(new SearchCondition[0]);
            }
            SortCondition[] sort
                     = new SortCondition[]{
                         new SortCondition(
                                  new Column("車両ID", "car_id", ColumnType.INT4),
                                  SortOrder.ASC)};
            if (0 < sortCondition_.size()) {
                sort = sortCondition_.toArray(new SortCondition[0]);
            }

            // リスト更新
            model_.refresh(search, sort);
            // 件数表示
            Statement st = model_.getConnection().createStatement();
            try {
                ResultSet rs
                         = st.executeQuery("select count(sno) from m_car_no;");
                if (rs.next()) {
                    countField_.setText(
                             String.format("%d / %d", model_.getRowCount(), rs.getInt(1)));
                }
            } finally {
                st.close();
            }
        } catch (Exception e) {
            logger_.warning(e.toString());
        }
    }

    private class RefreshAction implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent event) {
            updateView();
        }
    }

    private class EditAction implements ActionListener {

        public void actionPerformed(ActionEvent event) {
            showEditDialog();
        }
    }

    private class PrintAction implements ActionListener//追加btnイベント
    {

        @Override
        public void actionPerformed(ActionEvent event) {
            try {
                // ヘッダー, フッター, 印刷属性を設定
                MessageFormat header
                         = new MessageFormat(
                                  String.format("%1$tY/%1$tm/%1$td 管理(車両)マスタ一覧", Calendar.getInstance()));
                MessageFormat footer = new MessageFormat("page {0}");
                PrintRequestAttributeSet a = new HashPrintRequestAttributeSet();
                dataTable_.print(PrintMode.FIT_WIDTH, header, footer, true, a, false);
            } catch (PrinterException e) {
                logger_.warning(e.toString());
                JOptionPane.showMessageDialog(
                         Car.this,
                         "印刷に失敗しました.",
                         "警告",
                         JOptionPane.WARNING_MESSAGE);
            }
        }
    }

    private class AppendAction implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent event) {
            JDialog dialog = DialogHelper.getNewInstance(Car.this, true);
            dialog.add(new CarDetailsPane(model_, -1));
            dialog.setModal(true);
            dialog.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
            dialog.setSize(aks.industrialwaste.ui.DialogHelper.getMaximumSize());
            dialog.setVisible(true);
            updateView();
        }
    }

    @Override
    protected void showEditDialog() {
        int row = dataTable_.getSelectedRow();
        if (0 > row) {
            JOptionPane.showMessageDialog(
                     Car.this,
                     "一覧から項目を選択してください.",
                     "警告",
                     JOptionPane.WARNING_MESSAGE);

            return;
        }

        JDialog dialog = DialogHelper.getNewInstance(Car.this, true);
        dialog.add(new CarDetailsPane(model_, row));
        dialog.setModal(true);
        dialog.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        dialog.setSize(aks.industrialwaste.ui.DialogHelper.getMaximumSize());
        dialog.setVisible(true);

        updateView();
    }

    private class DeleteAction implements ActionListener {

        public void actionPerformed(ActionEvent event) {

            int row = dataTable_.getSelectedRow();
            if (0 > row) {
                JOptionPane.showMessageDialog(
                         Car.this,
                         "一覧から項目を選択してください.",
                         "確認",
                         JOptionPane.WARNING_MESSAGE);

                return;
            }

            int result = JOptionPane.showConfirmDialog(
                     Car.this,
                     "選択した項目を削除します。よろしいですか？",
                     "確認",
                     JOptionPane.YES_NO_OPTION);
            if (JOptionPane.YES_OPTION == result) {
                CarJournal.recordJournal(
                         model_.getConnection(),
                         "車両マスタ一覧 - 削除",
                         new HashMap<>(model_.getColumns(row)));
                if (model_.delete(row)) {
                    updateView();
                } else {
                    JOptionPane.showMessageDialog(
                             Car.this,
                             "削除に失敗しました。",
                             "警告",
                             JOptionPane.WARNING_MESSAGE);
                }
            }
        }
    }

    private class WeighAction implements ActionListener {

        public void actionPerformed(ActionEvent event) {
            int row = dataTable_.getSelectedRow();
            if (0 > row) {
                JOptionPane.showMessageDialog(
                         Car.this,
                         "一覧から項目を選択してください.",
                         "確認",
                         JOptionPane.WARNING_MESSAGE);

                return;
            }

            String result = JOptionPane.showInputDialog(
                     Car.this,
                     "風袋重量を入力してください。",
                     "風袋重量入力",
                     JOptionPane.INFORMATION_MESSAGE);
            if (result != null && result.length() > 0 && Integer.valueOf(result) >= 0) {
                if (model_.weight(row, Integer.valueOf(result))) {
                    updateView();
                } else {
                    JOptionPane.showMessageDialog(
                             Car.this,
                             "登録に失敗しました。",
                             "警告",
                             JOptionPane.WARNING_MESSAGE);
                }
            }
        }
    }

    private class MaxWeighAction implements ActionListener {

        public void actionPerformed(ActionEvent event) {
            int row = dataTable_.getSelectedRow();
            if (0 > row) {
                JOptionPane.showMessageDialog(
                         Car.this,
                         "一覧から項目を選択してください.",
                         "確認",
                         JOptionPane.WARNING_MESSAGE);

                return;
            }

            String result = JOptionPane.showInputDialog(
                     Car.this,
                     "最大積載重量を入力してください。",
                     "最大積載重量入力",
                     JOptionPane.INFORMATION_MESSAGE);
            if (result != null && result.length() > 0 && Integer.valueOf(result) >= 0) {
                if (model_.max_weight(row, Integer.valueOf(result))) {
                    updateView();
                } else {
                    JOptionPane.showMessageDialog(
                             Car.this,
                             "登録に失敗しました。",
                             "警告",
                             JOptionPane.WARNING_MESSAGE);
                }
            }

        }
    }

    private void ReportAction(java.awt.event.ActionEvent evt) {
        // 20111019 add 一覧表示されているデータをcsvファイルに落とす
        int confirm = JOptionPane.showConfirmDialog(
                 this, "現在一覧表示されているデータをファイルに保存します. よろしいですか?",
                 "確認", JOptionPane.OK_CANCEL_OPTION);

        if (confirm == JOptionPane.OK_OPTION) {
            Calendar c = Calendar.getInstance();
            JFileChooser fc = new JFileChooser();
            fc.setSelectedFile(new File("C:\\spat_exe\\csv",
                     String.format("車番マスタ_%1$tY%1$tm%1$td%1$tH%1$tM.csv", c)));
            if (JFileChooser.APPROVE_OPTION != fc.showSaveDialog(this)) {
                return;
            }
            File f = fc.getSelectedFile();
            f = new File(String.format("%s", f.getPath()));
            if (!f.getPath().matches(".+\\.[cC][sS][vV]$")) {
                model_.report(new File("C:\\spat_exe\\csv",
                         String.format("車番マスタ_%1$tY%1$tm%1$td%1$tH%1$tM.csv", c)));

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
}
