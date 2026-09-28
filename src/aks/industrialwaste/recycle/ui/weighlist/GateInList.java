package aks.industrialwaste.recycle.ui.weighlist;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.util.HashMap;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.event.AncestorEvent;
import aks.industrialwaste.recycle.ui.MainFrame;
import aks.industrialwaste.database.Column;
import aks.industrialwaste.database.ColumnType;
import aks.industrialwaste.database.SearchCondition;
import aks.industrialwaste.database.SortCondition;
import aks.industrialwaste.database.SortOrder;
import aks.industrialwaste.ui.DialogHelper;
import aks.industrialwaste.ui.ListPanelMaster;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JMenuItem;
import jp.co.astax.awt.FontUtility;
import javax.swing.table.*;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;

public class GateInList extends ListPanelMaster implements Runnable {

    private GateInListTableModel model_;
    private Thread thread2_ = null;
    private JPopupMenu popup;

    public GateInList(Connection con) {
        this.setDoubleBuffered(true);
        model_ = new GateInListTableModel(con);
        dataTable_.setModel(model_);
        dataTable_.setColumnModel(new GateInListTableColumnModel());
    }

    @Override
    public void ancestorAdded(AncestorEvent event) {
        super.ancestorAdded(event);

        if (null == thread2_) {
            thread2_ = new Thread(this);
            thread2_.start();
        }
    }

    @Override
    public void ancestorRemoved(AncestorEvent event) {
        if (null != thread2_) {
            thread2_.interrupt();
            thread2_ = null;
        }
        model_.clear();

        super.ancestorRemoved(event);
    }

    @Override
    protected void initComponents() {
        super.initComponents();

        Font font18 = FontUtility.getFont("メイリオ", Font.PLAIN, 14);

        popup = new JPopupMenu();// ポップアップメニューを生成

        // タイトル
        titleField_.setText("一時記憶一覧");
        // データテーブル
        dataTable_.setFont(font18);
        dataTable_.setRowHeight(30);
        dataTable_.setColumnSelectionAllowed(false);
        dataTable_.setRowSelectionAllowed(true);
        dataTable_.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        dataTable_.getTableHeader().setReorderingAllowed(false);
        dataTable_.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);

        //右クリック時のPOPアップメニュー作成///////////////////////////////////////
        JMenuItem item0 = new JMenuItem("データ訂正", MainFrame.icon_edit);
        JMenuItem item1 = new JMenuItem("データ削除", MainFrame.icon_delete);
        popup.add(item0);
        popup.addSeparator(); //セパレーター
        popup.add(item1);
        // 各メニュー選択時の処理
        item0.addActionListener(new EditAction());
        item1.addActionListener(new DeleteAction());

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
        jh.setFont(font18);
        // ボタン
        // 追加
        JButton b = new JButton("データ追加");
        b.setFont(font18);
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
        b = new JButton("データ訂正");
        b.setFont(font18);
        b.setMargin(new Insets(2, 2, 2, 2));
        b.setMinimumSize(new Dimension(150, 30));
        b.setPreferredSize(new Dimension(150, 30));
        b.addActionListener(new EditAction());
        addContainer(
                 southPane_, b,
                 1, 0, 1, 1, 0.0, 0.0,
                 GridBagConstraints.WEST, GridBagConstraints.NONE,
                 new Insets(2, 2, 2, 2), 0, 0);
        //ダミー
        addContainer(
                 southPane_, new JPanel(),
                 2, 0, 1, 1, 1.0, 0.0,
                 GridBagConstraints.WEST, GridBagConstraints.NONE,
                 new Insets(2, 2, 2, 2), 0, 0);
        // 削除
        b = new JButton("データ削除");
        b.setFont(font18);
        b.setMargin(new Insets(2, 2, 2, 2));
        b.setMinimumSize(new Dimension(150, 30));
        b.setPreferredSize(new Dimension(150, 30));
        b.addActionListener(new DeleteAction());
        addContainer(
                 southPane_, b,
                 6, 0, 1, 1, 0.0, 0.0,
                 GridBagConstraints.WEST, GridBagConstraints.NONE,
                 new Insets(2, 2, 2, 2), 0, 0);

    }

    @Override
    protected void updateView() {
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
        // 件数表示
        countField_.setText(String.format("%d", model_.getRowCount()));

    }

    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            try {
                SwingUtilities.invokeLater(
                         new Runnable() {
                    public void run() {
                        updateView();
                    }
                });
                //５秒で１回更新
                //Thread.sleep(5000);
                //１秒で１回更新
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            } catch (Exception e) {
                logger_.warning(e.toString());
            }
        }
    }

    private class AppendAction implements ActionListener//追加btnイベント
    {

        public void actionPerformed(ActionEvent event) {
            JDialog dialog = DialogHelper.getNewInstance(GateInList.this, false);
            dialog.add(new GateInListDetailsPane(model_, -1));
            dialog.setTitle("一時記憶データ - 追加");
            dialog.setSize(1280, 990);
            dialog.setVisible(true);

            updateView();
        }
    }

    private class DeleteAction implements ActionListener {

        public void actionPerformed(ActionEvent event) {
            int row = dataTable_.getSelectedRow();
            if (0 > row) {
                JOptionPane.showMessageDialog(
                         GateInList.this,
                         "一覧から項目を選択してください.",
                         "確認",
                         JOptionPane.WARNING_MESSAGE);

                return;
            }

            int result = JOptionPane.showConfirmDialog(
                     GateInList.this,
                     "選択した項目を削除します. よろしいですか?",
                     "確認",
                     JOptionPane.YES_NO_OPTION);
            if (JOptionPane.YES_OPTION == result) {
                GateInListJournal.recordJournal(
                         model_.getConnection(),
                         "一時記憶一覧 - 削除",
                         new HashMap<String, Object>(model_.getColumns(row)));
                if (model_.delete(row)) {
                    updateView();
                } else {
                    JOptionPane.showMessageDialog(
                             GateInList.this,
                             "削除に失敗しました.",
                             "警告",
                             JOptionPane.WARNING_MESSAGE);
                }
            }
        }
    }

    private class EditAction implements ActionListener {

        public void actionPerformed(ActionEvent event) {
            showEditDialog();
        }
    }

    @Override
    protected void showEditDialog()//訂正btnイベント
    {
        int row = dataTable_.getSelectedRow();
        if (0 > row) {
            JOptionPane.showMessageDialog(
                     GateInList.this,
                     "一覧から項目を選択してください.",
                     "警告",
                     JOptionPane.WARNING_MESSAGE);

            return;
        }

        JDialog dialog = DialogHelper.getNewInstance(GateInList.this, true);
        dialog.add(new GateInListDetailsPane(model_, row));
        //20111212 edit
        dialog.setModal(false);
        dialog.setTitle("一時記憶データ - 訂正");
        dialog.setSize(aks.industrialwaste.ui.DialogHelper.getMaximumSize());
        dialog.setVisible(true);
        updateView();
    }
}
