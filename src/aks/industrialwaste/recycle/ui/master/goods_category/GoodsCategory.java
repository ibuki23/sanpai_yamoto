package aks.industrialwaste.recycle.ui.master.goods_category;

//import aks.industrialwaste.recycle.ui.master.gastype.*;
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
import aks.industrialwaste.recycle.ui.Infobar;
import aks.industrialwaste.database.Column;
import aks.industrialwaste.database.ColumnType;
import aks.industrialwaste.database.DBManager;
import aks.industrialwaste.database.SearchCondition;
import aks.industrialwaste.database.SortCondition;
import aks.industrialwaste.database.SortOrder;
import aks.industrialwaste.ui.DialogHelper;
import aks.industrialwaste.ui.ListPanel;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.print.PrinterException;
import jp.co.astax.awt.FontUtility;
import javax.swing.JFileChooser;
import java.util.Calendar;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.text.MessageFormat;
import java.util.HashMap;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.swing.JPopupMenu;
import javax.swing.JTable.PrintMode;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;
import javax.swing.table.*;

/**
 * 品目マスタ中分類マスタの一覧を表示する.
 */
public class GoodsCategory extends ListPanel {

    public static GoodsCategoryTableModel model_;
    private List<SearchCondition> searchCondition_;
    private List<SortCondition> sortCondition_;
    private JPopupMenu popup;

    public GoodsCategory(Connection con) {
        model_ = new GoodsCategoryTableModel(con);
        dataTable_.setModel(model_);
//    dataTable_.setAutoResizeMode(JTable.AUTO_RESIZE_LAST_COLUMN);
        dataTable_.setColumnModel(new GoodsCategoryTableColumnModel());
        searchCondition_
                 = Collections.synchronizedList(new ArrayList<SearchCondition>());
        sortCondition_
                 = Collections.synchronizedList(new ArrayList<SortCondition>());

    }

    public static GoodsCategoryTableModel getModel() {
        return model_;
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
//    Font font14 = FontUtility.getFont("メイリオ", Font.PLAIN, 14);

        popup = new JPopupMenu();// ポップアップメニューを生成
        // タイトル
        titleField_.setText("品目中分類マスタ");
        // データテーブル
        dataTable_.setFont(font14);
        dataTable_.setRowHeight(30);
        dataTable_.setColumnSelectionAllowed(false);
        dataTable_.setRowSelectionAllowed(true);
        dataTable_.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        dataTable_.getTableHeader().setReorderingAllowed(false);
        dataTable_.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

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
        b.setEnabled(DBManager.mode == 1 || DBManager.mode == 2 || DBManager.mode == 201);
        b.addActionListener(new AppendAction());

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
        b.setEnabled(DBManager.mode == 1 || DBManager.mode == 2 || DBManager.mode == 201);
        b.addActionListener(new DeleteAction());

        // 本部取込(既設工場)
        b = new JButton("マスタ取込");
        b.setFont(font14);
        b.setMargin(new Insets(2, 2, 2, 2));
        b.setMinimumSize(new Dimension(120, 30));
        b.setPreferredSize(new Dimension(120, 30));
        b.setEnabled(DBManager.mode == 11 || DBManager.mode == 202);
        b.setVisible(DBManager.mode == 11 || DBManager.mode == 202);
        b.addActionListener(new ImportAction1());
        addContainer(
                 southPane_, b,
                 3, 0, 1, 1, 0.5, 0.0,
                 GridBagConstraints.WEST, GridBagConstraints.NONE,
                 new Insets(2, 2, 2, 2), 0, 0);
        // 本部取込(新設工場)
        b = new JButton("マスタ取込");
        b.setFont(font14);
        b.setMargin(new Insets(2, 2, 2, 2));
        b.setMinimumSize(new Dimension(120, 30));
        b.setPreferredSize(new Dimension(120, 30));
        b.setEnabled(DBManager.mode == 2);
        b.setVisible(DBManager.mode == 2);
        b.addActionListener(new ImportAction2());

        // 本部取込(本社のみ)
        b = new JButton("マスタ出力");
        b.setFont(font14);
        b.setMargin(new Insets(2, 2, 2, 2));
        b.setMinimumSize(new Dimension(120, 30));
        b.setPreferredSize(new Dimension(120, 30));
        b.setEnabled(DBManager.mode == 1 || DBManager.mode == 2 || DBManager.mode == 201);
        b.setVisible(DBManager.mode == 1 || DBManager.mode == 2 || DBManager.mode == 201);
        b.addActionListener(new ExportAction1());

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

        // レポート機能
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
            SearchCondition[] search = new SearchCondition[0];
            if (0 < searchCondition_.size()) {
                search = searchCondition_.toArray(new SearchCondition[0]);
            }
            SortCondition[] sort
                     = new SortCondition[]{
                         new SortCondition(
                                  new Column("sno", "sno", ColumnType.INT8),
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
                         = st.executeQuery("select count(sno) from m_manifesto_2;");
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

    private class EditAction implements ActionListener {

        public void actionPerformed(ActionEvent event) {
            showEditDialog(0);
        }
    }

    private class DeleteAction implements ActionListener {

        public void actionPerformed(ActionEvent event) {
            int row = dataTable_.getSelectedRow();
            if (0 > row) {
                JOptionPane.showMessageDialog(GoodsCategory.this,
                         "一覧から項目を選択してください.",
                         "確認",
                         JOptionPane.WARNING_MESSAGE);

                return;
            }

            int result = JOptionPane.showConfirmDialog(GoodsCategory.this,
                     "選択した項目を削除します。よろしいですか？",
                     "確認",
                     JOptionPane.YES_NO_OPTION);
            if (JOptionPane.YES_OPTION == result) {
                GoodsCategoryJournal.recordJournal(
                         model_.getConnection(),
                         "市町村マスタ一覧 - 削除",
                         new HashMap<String, Object>(model_.getColumns(row)));
                if (model_.delete(row)) {
                    updateView();
                } else {
                    JOptionPane.showMessageDialog(GoodsCategory.this,
                             "削除に失敗しました。",
                             "警告",
                             JOptionPane.WARNING_MESSAGE);
                }
            }
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
                                  String.format("%1$tY/%1$tm/%1$td 品目中分類マスタ一覧", Calendar.getInstance()));
                MessageFormat footer = new MessageFormat("page {0}");
                PrintRequestAttributeSet a = new HashPrintRequestAttributeSet();
                dataTable_.print(PrintMode.FIT_WIDTH, header, footer, true, a, false);
            } catch (PrinterException e) {
                logger_.warning(e.toString());
                JOptionPane.showMessageDialog(GoodsCategory.this,
                         "印刷に失敗しました.",
                         "警告",
                         JOptionPane.WARNING_MESSAGE);
            }
        }
    }

    private class AppendAction implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent event) {
            JDialog dialog = DialogHelper.getNewInstance(GoodsCategory.this, true);
            dialog.add(new GoodsCategoryDetailsPane(model_, -1));
            //20111212 edit
            dialog.setModal(true);
            dialog.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
            dialog.setSize(aks.industrialwaste.ui.DialogHelper.getMaximumSize());
            dialog.setVisible(true);

            updateView();
        }
    }

    @Override
    protected void showEditDialog(int mode) {
        int row = dataTable_.getSelectedRow();
        if (0 > row) {
            JOptionPane.showMessageDialog(GoodsCategory.this,
                     "一覧から項目を選択してください。",
                     "警告",
                     JOptionPane.WARNING_MESSAGE);

            return;
        }
        if (0l == (Long) model_.getValueAt(row, "sno"))//検索後には空欄行はなくなるのでidで見る
        {
            JOptionPane.showMessageDialog(GoodsCategory.this,
                     "空欄マスタは変更できません。",
                     "警告",
                     JOptionPane.WARNING_MESSAGE);

            return;
        }

        JDialog dialog = DialogHelper.getNewInstance(GoodsCategory.this, true);
        dialog.add(new GoodsCategoryDetailsPane(model_, row));
        //20111212 edit
        dialog.setModal(true);
        dialog.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        dialog.setSize(aks.industrialwaste.ui.DialogHelper.getMaximumSize());
        dialog.setVisible(true);

        updateView();
    }

    private void ReportAction(java.awt.event.ActionEvent evt) {
        // 20111019 add 一覧表示されているデータをcsvファイルに落とす
        int confirm = JOptionPane.showConfirmDialog(
                 this, "現在一覧表示されているデータをファイルに保存します. よろしいですか?",
                 "確認", JOptionPane.OK_CANCEL_OPTION);

        if (confirm == JOptionPane.OK_OPTION) {
            Calendar c = Calendar.getInstance();
            JFileChooser fc = new JFileChooser();
            fc.setSelectedFile(new File(DBManager.pass_,
                     String.format("品目注分類マスタ_%1$tY%1$tm%1$td%1$tH%1$tM.csv", c)));
            if (JFileChooser.APPROVE_OPTION != fc.showSaveDialog(this)) {
                return;
            }
            File f = fc.getSelectedFile();
            f = new File(String.format("%s", f.getPath()));
            if (!f.getPath().matches(".+\\.[cC][sS][vV]$")) {
                model_.report(new File("C:\\spat_exe\\csv",
                         String.format("品目注分類マスタ_%1$tY%1$tm%1$td%1$tH%1$tM.csv", c)));

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
                e.printStackTrace();
                JOptionPane.showMessageDialog(
                         this,
                         "CSVファイルへの出力に失敗しました.",
                         "警告",
                         JOptionPane.WARNING_MESSAGE);
            }
        }
    }

    //既設工場マスタ取込
    private class ImportAction1 implements ActionListener {

        public void actionPerformed(ActionEvent event) {

            int result = JOptionPane.showConfirmDialog(GoodsCategory.this,
                     "本部マスタデータを取得します。よろしいですか？",
                     "確認",
                     JOptionPane.YES_NO_OPTION);
            if (JOptionPane.YES_OPTION == result) {

                int cnt = model_.import_master();
                if (cnt > 0) {
                    JOptionPane.showMessageDialog(GoodsCategory.this,
                             "マスタ取得が正常終了しました。",
                             "更新件数：" + cnt,
                             JOptionPane.INFORMATION_MESSAGE);

                    updateView();
                } else if (cnt == 0) {
                    JOptionPane.showMessageDialog(GoodsCategory.this,
                             "マスタ変更はありません。",
                             "更新件数：" + cnt,
                             JOptionPane.INFORMATION_MESSAGE);

                    updateView();
                } else {
                    JOptionPane.showMessageDialog(GoodsCategory.this,
                             "取得に失敗しました。",
                             "警告",
                             JOptionPane.WARNING_MESSAGE);
                }
            }
        }
    }

    private class ImportAction2 implements ActionListener {

        public void actionPerformed(ActionEvent event) {

            int result = JOptionPane.showConfirmDialog(GoodsCategory.this,
                     "本部マスタデータを取得します。よろしいですか？",
                     "確認",
                     JOptionPane.YES_NO_OPTION);
            if (JOptionPane.YES_OPTION == result) {

                if (model_.import_master2()) {
                    JOptionPane.showMessageDialog(GoodsCategory.this,
                             "マスタ取得が正常終了しました。",
                             "警告",
                             JOptionPane.INFORMATION_MESSAGE);
                    updateView();
                } else {
                    JOptionPane.showMessageDialog(GoodsCategory.this,
                             "取得に失敗しました。",
                             "警告",
                             JOptionPane.WARNING_MESSAGE);
                }
            }
        }
    }

    private class ExportAction1 implements ActionListener {

        public void actionPerformed(ActionEvent event) {

            int result = JOptionPane.showConfirmDialog(GoodsCategory.this,
                     "本部マスタデータをファイルに出力します。よろしいですか？",
                     "確認",
                     JOptionPane.YES_NO_OPTION);
            if ((JOptionPane.YES_OPTION == result) && (Infobar.master_index_ == 0)) {

                Infobar.message_ = "車種マスタファイル転送中・・・";
                Infobar.master_index_ = 10;

            }
        }
    }

    /**
     * コピー元のパス[srcPath]から、コピー先のパス[destPath]へ ファイルのコピーを行います。
     * コピー処理にはFileChannel#transferToメソッドを利用します。 尚、コピー処理終了後、入力・出力のチャネルをクローズします。
     *
     * @param srcPath コピー元のパス
     * @param destPath コピー先のパス
     * @throws IOException 何らかの入出力処理例外が発生した場合
     */
    public static void copyTransfer(String srcPath, String destPath)
             throws IOException {

        FileChannel srcChannel = new FileInputStream(srcPath).getChannel();
        FileChannel destChannel = new FileOutputStream(destPath).getChannel();
        try {
            srcChannel.transferTo(0, srcChannel.size(), destChannel);
        } finally {
            srcChannel.close();
            destChannel.close();
        }
    }
}
