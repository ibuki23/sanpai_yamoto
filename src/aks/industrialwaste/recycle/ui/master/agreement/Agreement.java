package aks.industrialwaste.recycle.ui.master.agreement;

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
import aks.industrialwaste.recycle.report.XlsxManifesto;
import aks.industrialwaste.recycle.ui.Infobar;
import aks.industrialwaste.recycle.ui.MainFrame;
import aks.industrialwaste.database.Column;
import aks.industrialwaste.database.ColumnType;
import aks.industrialwaste.database.CombineOperator;
import aks.industrialwaste.database.CompareOperator;
import aks.industrialwaste.database.DBManager;
import aks.industrialwaste.database.SearchCondition;
import aks.industrialwaste.database.SortCondition;
import aks.industrialwaste.database.SortOrder;
import aks.industrialwaste.ui.DialogHelper;
import aks.industrialwaste.ui.ListPanel;
import aks.swing.JTextField;
import com.ibm.icu.text.Transliterator;
import java.awt.Color;
import java.awt.Component;
import java.awt.Desktop;
import java.awt.GridBagLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.im.InputSubset;
import java.awt.print.PrinterException;
import jp.co.astax.awt.FontUtility;
import javax.swing.JFileChooser;
import java.util.Calendar;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.sql.PreparedStatement;
import java.text.MessageFormat;
import java.util.HashMap;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JTable.PrintMode;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;
import javax.swing.table.*;

public class Agreement extends ListPanel {

    private AgreementTableModel model_;
    private List<SearchCondition> searchCondition_;
    private List<SortCondition> sortCondition_;
    private JTextField searchWordField_;
    private JPopupMenu popup;

    private Transliterator tr1_;
    private Transliterator tr2_;
    private int 現在ページcnt;
    private Connection con_;

    private static class GoodsItem {

        int id;
        String name;

        GoodsItem(int id, String name) {
            this.id = id;
            this.name = name;
        }

        @Override
        public String toString() {
            return id + " : " + name;
        }
    }

    public Agreement(Connection con) {
        con_ = con;
        model_ = new AgreementTableModel(con_);
        dataTable_.setModel(model_);
        dataTable_.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        dataTable_.setColumnModel(new AgreementTableColumnModel());
        searchCondition_
                 = Collections.synchronizedList(new ArrayList<>());
        sortCondition_
                 = Collections.synchronizedList(new ArrayList<>());

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
        // タイトル
        titleField_.setText("契約マスタ");
        //検索
        JPanel p = new JPanel();
        p.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));
        p.setAlignmentX(JPanel.CENTER_ALIGNMENT);

        searchWordField_ = new JTextField();
        searchWordField_.setFont(font14);
        searchWordField_.setMargin(new Insets(0, 2, 0, 2));
        searchWordField_.setMinimumSize(new Dimension(200, 35));
        searchWordField_.setPreferredSize(new Dimension(200, 35));
        searchWordField_.addActionListener(new SearchAction());
        searchWordField_.addFocusListener(new java.awt.event.FocusAdapter() {
            // フォーカスが移ったときはIMEをOFFにする

            @Override
            public void focusLost(java.awt.event.FocusEvent e) {
                searchWordField_.getInputContext().setCompositionEnabled(false);
                searchWordField_.getInputContext().setCharacterSubsets(null);
                setAsciiMode();
            }

            @Override
            public void focusGained(java.awt.event.FocusEvent evt) {
                // カタカナモード
                setKanjiMode();
            }
        });
        p.add(searchWordField_);
        addContainer(
                 northPane_, p,
                 0, 1, 1, 1, 0.0, 0.0,
                 GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                 new Insets(0, 0, 0, 0), 0, 0);

        JLabel s_lbl = new JLabel("※得意先名 得意先ｶﾅ 現場名 現場ｶﾅ 検索");
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
        dataTable_.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        dataTable_.getTableHeader().setReorderingAllowed(false);
        dataTable_.setAutoResizeMode(JTable.AUTO_RESIZE_LAST_COLUMN);
        dataTable_.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                if (((String) table.getValueAt(row, 10)).equals("") && !isSelected) {
                    // 契約期間内なら
                    setBackground(table.getBackground());
                    setForeground(table.getForeground());

                } else if (((String) table.getValueAt(row, 10)).equals("契約前") && !isSelected) {
                    // 契約期間前なら
                    if (column == 10) {
                        //黄色
                        setBackground(new Color(255, 204, 204));
                        setForeground(table.getForeground());
                    } else {
                        setBackground(table.getBackground());
                        setForeground(table.getForeground());
                    }

                } else if (((String) table.getValueAt(row, 10)).equals("期間切れ") && !isSelected) {
                    // 契約期間後なら
                    if (column == 10) {
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
                //              }
                return this;
            }
        });

        //右クリック時のPOPアップメニュー作成///////////////////////////////////////
        JMenuItem item0 = new JMenuItem("最新の情報に更新", MainFrame.icon_reload);
        JMenuItem item1 = new JMenuItem("追加", MainFrame.icon_add);
        JMenuItem item2 = new JMenuItem("訂正", MainFrame.icon_edit);
        JMenuItem item4 = new JMenuItem("一覧印刷", MainFrame.icon_print);
        JMenuItem item5 = new JMenuItem("契約書確認");
        popup.add(item0);
        popup.add(item4);
        popup.add(item5);
        popup.add(item1);
        popup.add(item2);
        popup.addSeparator();

        // 各メニュー選択時の処理
        item0.addActionListener(new RefreshAction());
        item1.addActionListener(new AppendAction());
        item2.addActionListener(new EditAction());
        item4.addActionListener(new PrintAction());
        item5.addActionListener(new Get契約書Action());

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
        // 複写
        b = new JButton("複写");
        b.setFont(font14);
        b.setMargin(new Insets(2, 2, 2, 2));
        b.setMinimumSize(new Dimension(120, 30));
        b.setPreferredSize(new Dimension(120, 30));
        b.addActionListener(new CopyAction());
        addContainer(
                 southPane_, b,
                 2, 0, 1, 1, 0.0, 0.0,
                 GridBagConstraints.WEST, GridBagConstraints.NONE,
                 new Insets(2, 2, 2, 2), 0, 0);
        // 削除
        b = new JButton("削除");
        b.setFont(font14);
        b.setMargin(new Insets(2, 2, 2, 2));
        b.setMinimumSize(new Dimension(120, 30));
        b.setPreferredSize(new Dimension(120, 30));
        b.addActionListener(new DeleteAction());

        // 本部取込(既設工場)
        b = new JButton("マスタ取込");
        b.setFont(font14);
        b.setMargin(new Insets(2, 2, 2, 2));
        b.setMinimumSize(new Dimension(120, 30));
        b.setPreferredSize(new Dimension(120, 30));
        b.addActionListener(new ImportAction1());
        // 本部取込(新設工場)
        b = new JButton("マスタ取込");
        b.setFont(font14);
        b.setMargin(new Insets(2, 2, 2, 2));
        b.setMinimumSize(new Dimension(120, 30));
        b.setPreferredSize(new Dimension(120, 30));
        b.setEnabled(DBManager.mode == 2);
        b.setVisible(DBManager.mode == 2);
        b.setVisible(false);
        b.addActionListener(new ImportAction2());

        // 本部取込(本社のみ)
        b = new JButton("マスタ出力");
        b.setFont(font14);
        b.setMargin(new Insets(2, 2, 2, 2));
        b.setMinimumSize(new Dimension(120, 30));
        b.setPreferredSize(new Dimension(120, 30));
        b.setEnabled(DBManager.mode == 4 || DBManager.mode == 20 || DBManager.mode == 0 || DBManager.mode == 200);
        b.setVisible(DBManager.mode == 4 || DBManager.mode == 20 || DBManager.mode == 0 || DBManager.mode == 200);
        b.addActionListener(new ExportAction1());

        // 契約書確認
        b = new JButton("契約書確認");
        b.setFont(font14);
        b.setMargin(new Insets(2, 2, 2, 2));
        b.setMinimumSize(new Dimension(120, 30));
        b.setPreferredSize(new Dimension(120, 30));
        b.addActionListener(new Get契約書Action());
        addContainer(
                 southPane_, b,
                 7, 0, 1, 1, 0.0, 0.0,
                 GridBagConstraints.WEST, GridBagConstraints.NONE,
                 new Insets(2, 2, 2, 2), 0, 0);

        // 契約書印刷
        b = new JButton("契約書Ａ印刷");
        b.setFont(font14);
        b.setMargin(new Insets(2, 2, 2, 2));
        b.setMinimumSize(new Dimension(120, 30));
        b.setPreferredSize(new Dimension(120, 30));
        b.addActionListener(new Print契約書Action(1));

        // 契約書印刷
        b = new JButton("契約書Ｂ印刷");
        b.setFont(font14);
        b.setMargin(new Insets(2, 2, 2, 2));
        b.setMinimumSize(new Dimension(120, 30));
        b.setPreferredSize(new Dimension(120, 30));
        b.addActionListener(new Print契約書Action(2));

        // 契約書印刷
        b = new JButton("契約書印刷");
        b.setFont(font14);
        b.setMargin(new Insets(2, 2, 2, 2));
        b.setMinimumSize(new Dimension(120, 30));
        b.setPreferredSize(new Dimension(120, 30));
        b.addActionListener(new Print契約書Action(0));
        addContainer(
                 southPane_, b,
                 10, 0, 1, 1, 0.5, 0.0,
                 GridBagConstraints.WEST, GridBagConstraints.NONE,
                 new Insets(2, 2, 2, 2), 0, 0);

        b = new JButton("品目単価一括変更");
        b.setFont(font14);
        b.setMargin(new Insets(2, 2, 2, 2));
        b.setMinimumSize(new Dimension(150, 30));
        b.setPreferredSize(new Dimension(150, 30));
        b.addActionListener(new 品目単価変更());
        
        addContainer(
                 southPane_, b,
                 3, 0, 1, 1, 0.0, 0.0,
                 GridBagConstraints.WEST, GridBagConstraints.NONE,
                 new Insets(2, 2, 2, 2), 0, 0);

        // 汎用機能
        b = new JButton("ﾊﾞｰｺｰﾄﾞ発行");
        b.setFont(font14);
        b.setMargin(new Insets(2, 2, 2, 2));
        b.setMinimumSize(new Dimension(120, 30));
        b.setPreferredSize(new Dimension(120, 30));
        b.addActionListener(new PrintバーコードAction());

        // 一覧印刷
        b = new JButton("一覧印刷");
        b.setFont(font14);
        b.setMargin(new Insets(2, 2, 2, 2));
        b.setMinimumSize(new Dimension(120, 30));
        b.setPreferredSize(new Dimension(120, 30));
        b.addActionListener(new PrintAction());
        addContainer(
                 southPane_, b,
                 10, 0, 1, 1, 0.0, 0.0,
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
                 11, 0, 1, 1, 0.0, 0.0,
                 GridBagConstraints.EAST, GridBagConstraints.NONE,
                 new Insets(2, 2, 2, 2), 0, 0);
    }

    private class RefreshAction implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent event) {
            updateView();
        }
    }

    private class SearchAction implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent event) {
            updateView();
        }
    }

    @Override
    protected void updateView() {
        try {
            // 検索条件, ソート条件の設定
            String searchWordTemp;
            searchWordTemp = tr1_.transliterate(searchWordField_.getText().trim());
            searchWordTemp = tr2_.transliterate(searchWordTemp);

            SearchCondition[] search;
            search
                     = new SearchCondition[]{
                         new SearchCondition(
                                  new Column("現場名称", "name", ColumnType.TEXT),
                                  CompareOperator.LIKE,
                                  searchWordField_.getText().trim(),
                                  CombineOperator.OR),
                         new SearchCondition(
                                  new Column("kana", "kana", ColumnType.TEXT),
                                  CompareOperator.LIKE,
                                  searchWordField_.getText().trim(),
                                  CombineOperator.OR),
                         new SearchCondition(
                                  new Column("ent_kana", "ent_kana", ColumnType.TEXT),
                                  CompareOperator.LIKE,
                                  searchWordField_.getText().trim(),
                                  CombineOperator.OR),
                         new SearchCondition(
                                  new Column("排出業者名", "ent_name", ColumnType.TEXT),
                                  CompareOperator.LIKE,
                                  searchWordField_.getText().trim(),
                                  CombineOperator.NONE)};
            if (0 < searchCondition_.size()) {
                search = searchCondition_.toArray(new SearchCondition[0]);
            }
            SortCondition[] sort
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

            // リスト更新
            int row = dataTable_.getSelectedRow();
            model_.refresh_limit(search, sort, 3000 * 現在ページcnt - 3000, 3000 * 現在ページcnt);
            if ((0 <= row) && (model_.getRowCount() > row)) {
                dataTable_.setRowSelectionInterval(row, row);
            }

            // 件数表示
            Statement st = model_.getConnection().createStatement();
            try {
                ResultSet rs
                         = st.executeQuery("select count(sno) from m_discharge_office;");
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

    private class CopyAction implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent event) {
            showEditDialog(1);
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
                                  String.format("%1$tY/%1$tm/%1$td 処分契約マスタ一覧", Calendar.getInstance()));
                MessageFormat footer = new MessageFormat("page {0}");
                PrintRequestAttributeSet a = new HashPrintRequestAttributeSet();
                dataTable_.print(PrintMode.FIT_WIDTH, header, footer, true, a, false);
            } catch (PrinterException e) {
                logger_.warning(e.toString());
                JOptionPane.showMessageDialog(
                         Agreement.this,
                         "印刷に失敗しました.",
                         "警告",
                         JOptionPane.WARNING_MESSAGE);
            }
        }
    }

    private class AppendAction implements ActionListener//追加btnイベント
    {

        @Override
        public void actionPerformed(ActionEvent event) {
            JDialog dialog = DialogHelper.getNewInstance(Agreement.this, true);
            dialog.add(new AgreementDetailsPane(model_, -1, 0));
            dialog.setModal(true);
            dialog.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
            dialog.setSize(aks.industrialwaste.ui.DialogHelper.getMaximumSize());
            dialog.setVisible(true);

            updateView();
        }

    }

    @Override
    protected void showEditDialog(int mode)//訂正btnイベント
    {
        int row = dataTable_.getSelectedRow();
        if (0 > row) {
            JOptionPane.showMessageDialog(
                     Agreement.this,
                     "一覧から項目を選択してください。",
                     "警告",
                     JOptionPane.WARNING_MESSAGE);

            return;
        }
        if (0 == (Integer) model_.getValueAt(row, "ent_id"))//検索後には空欄行はなくなるのでidで見る
        {
            JOptionPane.showMessageDialog(
                     Agreement.this,
                     "空欄マスタは変更できません。",
                     "警告",
                     JOptionPane.WARNING_MESSAGE);

            return;
        }

        JDialog dialog = DialogHelper.getNewInstance(Agreement.this, true);
        dialog.add(new AgreementDetailsPane(model_, row, mode));
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
                         Agreement.this,
                         "一覧から項目を選択してください.",
                         "確認",
                         JOptionPane.WARNING_MESSAGE);

                return;
            }

            int result = JOptionPane.showConfirmDialog(
                     Agreement.this,
                     "選択した項目を削除します。よろしいですか？",
                     "確認",
                     JOptionPane.YES_NO_OPTION);
            if (JOptionPane.YES_OPTION == result) {
                AgreementJournal.recordJournal(
                         model_.getConnection(),
                         "現場マスタ一覧 - 削除",
                         new HashMap<>(model_.getColumns(row)));
                if (model_.delete(row)) {
                    updateView();
                } else {
                    JOptionPane.showMessageDialog(
                             Agreement.this,
                             "削除に失敗しました。",
                             "警告",
                             JOptionPane.WARNING_MESSAGE);
                }
            }
        }
    }

    private class 品目単価変更ダイアログ extends JDialog {

        private JComboBox<GoodsItem> goodsCombo_;
        private JTextField unitField_;
        private boolean updated_ = false;

        private Connection con_;
        private int[] rows_;
        private AgreementTableModel model_;

        public 品目単価変更ダイアログ(Component parent, Connection con, int[] rows, AgreementTableModel model) {
            super(SwingUtilities.getWindowAncestor(parent), "品目単価一括変更");
            con_ = con;
            rows_ = rows;
            model_ = model;

            init();
        }

        private void init() {
            Font font14 = FontUtility.getFont("メイリオ", Font.PLAIN, 14);

            JPanel panel = new JPanel(new GridBagLayout());

            JLabel goodsLabel = new JLabel("品目");
            goodsLabel.setFont(font14);

            goodsCombo_ = new JComboBox<>();
            goodsCombo_.setFont(font14);
            loadGoods();

            JLabel unitLabel = new JLabel("単価");
            unitLabel.setFont(font14);

            unitField_ = new JTextField();
            unitField_.setFont(font14);
            unitField_.setPreferredSize(new Dimension(150, 30));

            JButton okButton = new JButton("登録");
            okButton.setFont(font14);
            okButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    updateUnit();
                }
            });

            JButton cancelButton = new JButton("閉じる");
            cancelButton.setFont(font14);
            cancelButton.addActionListener(e -> dispose());

            addContainer(panel, goodsLabel, 0, 0, 1, 1, 0.0, 0.0,
                     GridBagConstraints.WEST, GridBagConstraints.NONE,
                     new Insets(5, 5, 5, 5), 0, 0);

            addContainer(panel, goodsCombo_, 1, 0, 1, 1, 1.0, 0.0,
                     GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
                     new Insets(5, 5, 5, 5), 0, 0);

            addContainer(panel, unitLabel, 0, 1, 1, 1, 0.0, 0.0,
                     GridBagConstraints.WEST, GridBagConstraints.NONE,
                     new Insets(5, 5, 5, 5), 0, 0);

            addContainer(panel, unitField_, 1, 1, 1, 1, 1.0, 0.0,
                     GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
                     new Insets(5, 5, 5, 5), 0, 0);

            addContainer(panel, okButton, 0, 2, 1, 1, 0.0, 0.0,
                     GridBagConstraints.WEST, GridBagConstraints.NONE,
                     new Insets(10, 5, 5, 5), 0, 0);

            addContainer(panel, cancelButton, 1, 2, 1, 1, 0.0, 0.0,
                     GridBagConstraints.EAST, GridBagConstraints.NONE,
                     new Insets(10, 5, 5, 5), 0, 0);

            add(panel);
            setSize(400, 180);
            setLocationRelativeTo(getParent());
        }

        private void loadGoods() {
            String sql
                     = "select id, name from m_goods_kind "
                     + "where coalesce(delete_flag, false) = false "
                     + "order by id";

            try (PreparedStatement ps = con_.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {

                while (rs.next()) {
                    goodsCombo_.addItem(new GoodsItem(
                             rs.getInt("id"),
                             rs.getString("name")
                    ));
                }

            } catch (Exception e) {
                logger_.warning(e.toString());
                JOptionPane.showMessageDialog(
                         this,
                         "品目の取得に失敗しました。",
                         "警告",
                         JOptionPane.WARNING_MESSAGE);
            }
        }

        private void updateUnit() {
            GoodsItem goods = (GoodsItem) goodsCombo_.getSelectedItem();

            if (goods == null) {
                JOptionPane.showMessageDialog(this, "品目を選択してください。");
                return;
            }

            int goodsUnit;
            try {
                goodsUnit = Integer.parseInt(unitField_.getText().trim());
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "単価は数値で入力してください。");
                return;
            }

            int result = JOptionPane.showConfirmDialog(
                     this,
                     rows_.length + "件の契約に対して、選択品目の単価を変更します。よろしいですか？",
                     "確認",
                     JOptionPane.YES_NO_OPTION);

            if (result != JOptionPane.YES_OPTION) {
                return;
            }

            String sql
                     = "update m_office_unit "
                     + "set goods_unit = ? "
                     + "where ent_id = ? "
                     + "and office_id = ? "
                     + "and goods_id = ?";

            try (PreparedStatement ps = con_.prepareStatement(sql)) {
                for (int row : rows_) {
                    int modelRow = dataTable_.convertRowIndexToModel(row);

                    Object entId = model_.getValueAt(modelRow, "ent_id");
                    Object officeId = model_.getValueAt(modelRow, "id");

                    ps.setInt(1, goodsUnit);
                    ps.setObject(2, entId);
                    ps.setObject(3, officeId);
                    ps.setInt(4, goods.id);

                    ps.addBatch();
                }

                int[] counts = ps.executeBatch();

                int updateCount = 0;
                for (int count : counts) {
                    if (count > 0) {
                        updateCount += count;
                    }
                }

                if (updateCount < rows_.length) {
                    JOptionPane.showMessageDialog(
                             this,
                             "一括変更が完了しました。\n"
                             + "更新件数：" + updateCount + "件\n"
                             + "未更新：" + (rows_.length - updateCount) + "件\n\n"
                             + "未更新の契約には、指定した品目が登録されていない可能性があります。",
                             "確認",
                             JOptionPane.WARNING_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(
                             this,
                             "一括変更が完了しました。\n更新件数：" + updateCount + "件");
                }

            } catch (Exception e) {
                try {
                    con_.rollback();
                } catch (Exception ignore) {
                }

                logger_.warning(e.toString());
                JOptionPane.showMessageDialog(
                         this,
                         "一括変更に失敗しました。\n" + e.toString(),
                         "警告",
                         JOptionPane.WARNING_MESSAGE);

            } finally {
                try {
                    con_.setAutoCommit(true);
                } catch (Exception ignore) {
                }
            }
        }

        public boolean isUpdated() {
            return updated_;
        }
    }

    private class 品目単価変更 implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent event) {
            int[] rows = dataTable_.getSelectedRows();

            if (rows.length == 0) {
                JOptionPane.showMessageDialog(
                         Agreement.this,
                         "一覧から契約を選択してください。",
                         "警告",
                         JOptionPane.WARNING_MESSAGE);
                return;
            }

            品目単価変更ダイアログ dialog
                     = new 品目単価変更ダイアログ(Agreement.this, model_.getConnection(), rows, model_);

            dialog.setModal(true);
            dialog.setVisible(true);

            if (dialog.isUpdated()) {
                updateView();
            }
        }
    }

    private class ImportAction1 implements ActionListener {

        public void actionPerformed(ActionEvent event) {

            int result = JOptionPane.showConfirmDialog(
                     Agreement.this,
                     "マスタデータを更新します。よろしいですか？",
                     "確認",
                     JOptionPane.YES_NO_OPTION);
            if (JOptionPane.YES_OPTION == result) {

                int cnt = model_.import_master();
                if (cnt > 0) {
                    JOptionPane.showMessageDialog(
                             Agreement.this,
                             "マスタ取得が正常終了しました。",
                             "更新件数：" + cnt,
                             JOptionPane.INFORMATION_MESSAGE);

                    updateView();
                } else if (cnt == 0) {
                    JOptionPane.showMessageDialog(
                             Agreement.this,
                             "マスタ変更はありません。",
                             "更新件数：" + cnt,
                             JOptionPane.INFORMATION_MESSAGE);

                    updateView();
                } else {
                    JOptionPane.showMessageDialog(
                             Agreement.this,
                             "取得に失敗しました。",
                             "警告",
                             JOptionPane.WARNING_MESSAGE);
                }
            }
        }
    }

    private class ImportAction2 implements ActionListener {

        public void actionPerformed(ActionEvent event) {

            int result = JOptionPane.showConfirmDialog(
                     Agreement.this,
                     "マスタデータを更新します。よろしいですか？",
                     "確認",
                     JOptionPane.YES_NO_OPTION);
            if (JOptionPane.YES_OPTION == result) {

                if (model_.import_master2()) {
                    JOptionPane.showMessageDialog(
                             Agreement.this,
                             "マスタ取得が正常終了しました。",
                             "警告",
                             JOptionPane.INFORMATION_MESSAGE);

                    updateView();
                } else {
                    JOptionPane.showMessageDialog(
                             Agreement.this,
                             "取得に失敗しました。",
                             "警告",
                             JOptionPane.WARNING_MESSAGE);
                }
            }
        }
    }

    public class ExportAction1 implements ActionListener {

        public void actionPerformed(ActionEvent event) {

            int result = JOptionPane.showConfirmDialog(
                     Agreement.this,
                     "本部マスタデータをファイルに出力します。よろしいですか？",
                     "確認",
                     JOptionPane.YES_NO_OPTION);
            if ((JOptionPane.YES_OPTION == result) && (Infobar.master_index_ == 0)) {

                Infobar.message_ = "契約マスタファイル転送中・・・";
                Infobar.master_index_ = 1;
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

    private void ReportAction(java.awt.event.ActionEvent evt) {
        // 20111019 add 一覧表示されているデータをcsvファイルに落とす
        int confirm = JOptionPane.showConfirmDialog(
                 this, "現在一覧表示されているデータをファイルに保存します. よろしいですか?",
                 "確認", JOptionPane.OK_CANCEL_OPTION);

        if (confirm == JOptionPane.OK_OPTION) {
            Calendar c = Calendar.getInstance();
            JFileChooser fc = new JFileChooser();
            fc.setSelectedFile(new File("C:\\計量システム\\マスタCSV",
                     String.format("契約マスタ_%1$tY%1$tm%1$td%1$tH%1$tM.csv", c)));
            if (JFileChooser.APPROVE_OPTION != fc.showSaveDialog(this)) {
                return;
            }
            File f = fc.getSelectedFile();
            f = new File(String.format("%s", f.getPath()));
            if (!f.getPath().matches(".+\\.[cC][sS][vV]$")) {
                model_.report(new File("C:\\計量システム\\マスタCSV",
                         String.format("契約マスタ_%1$tY%1$tm%1$td%1$tH%1$tM.csv", c)));

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

    // 漢字モードの設定
    private void setKanjiMode() {
        Character.Subset[] subsets = {InputSubset.KANJI};
        searchWordField_.getInputContext().setCompositionEnabled(true);
        searchWordField_.getInputContext().setCharacterSubsets(subsets);
    }

    // 半角英数字モードの設定
    private void setAsciiMode() {
        Character.Subset[] subsets = {InputSubset.LATIN};
        searchWordField_.getInputContext().setCompositionEnabled(false);
        searchWordField_.getInputContext().setCharacterSubsets(subsets);
    }

    private void pdfOpen() {
        //委託契約書がPDF化されておりシステムにファイルパスが登録されていること
        int row = dataTable_.getSelectedRow();
        try {
            if (row >= 0 && ((String) model_.getValueAt(row, "keiyakusyo_path")).length() > 0) {
                String FilePath = (String) model_.getValueAt(row, "keiyakusyo_path");
                try {
                    if (FilePath.length() > 0) {
                        Desktop desktop = Desktop.getDesktop();
                        // 関連付けられたアプリケーションを起動してファイルを開きます。
                        desktop.open(new File(FilePath));
                    }

                } catch (Exception ev) {
                    System.out.println("失敗(pdfOpen)");
                }
            }
        } catch (Exception ev) {
            System.out.println("契約書は設定されていません。");
        }
    }

    private class Get契約書Action implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent event) {
            pdfOpen();
        }
    }

    private class Print契約書Action implements ActionListener {

        private int index_;

        public Print契約書Action(int index) {
            index_ = index;
        }

        @Override
        public void actionPerformed(ActionEvent event) {
            ////印刷処理
            int row = dataTable_.getSelectedRow();
            if (row >= 0) {
                if (index_ == 1) {
                    new XlsxManifesto(XlsxManifesto.MANIFESTO_REPORT9).print(
                             con_,
                             XlsxManifesto.MANIFESTO_REPORT9,
                             (Long) model_.getValueAt(row, "sno"));
                } else if (index_ == 2) {
                    new XlsxManifesto(XlsxManifesto.MANIFESTO_REPORT10).print(
                             con_,
                             XlsxManifesto.MANIFESTO_REPORT10,
                             (Long) model_.getValueAt(row, "sno"));
                } else if (index_ == 3) {
                    new XlsxManifesto(XlsxManifesto.MANIFESTO_REPORT11).print(
                             con_,
                             XlsxManifesto.MANIFESTO_REPORT11,
                             (Long) model_.getValueAt(row, "sno"));
                } else if (index_ == 0) {
                    new XlsxManifesto(XlsxManifesto.MANIFESTO_REPORT5).print(
                             con_,
                             XlsxManifesto.MANIFESTO_REPORT5,
                             (Long) model_.getValueAt(row, "sno"));
                }
            }
        }
    }

    private class PrintバーコードAction implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent event) {
            ////印刷処理
            int row = dataTable_.getSelectedRow();
            if (row >= 0) {
                new XlsxManifesto(XlsxManifesto.MANIFESTO_REPORT6).print(
                         con_,
                         XlsxManifesto.MANIFESTO_REPORT6,
                         (Long) model_.getValueAt(row, "sno"));
            }
        }
    }
}
