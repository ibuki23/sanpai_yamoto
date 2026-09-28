package aks.industrialwaste.recycle.util;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableColumnModel;
import javax.swing.table.TableColumn;
import aks.industrialwaste.database.Column;
import aks.industrialwaste.database.ColumnType;
import aks.industrialwaste.database.CombineOperator;
import aks.industrialwaste.database.CompareOperator;
import aks.industrialwaste.database.DBManager;
import aks.industrialwaste.database.SearchCondition;
import aks.industrialwaste.database.SortCondition;
import aks.industrialwaste.database.SortOrder;
import aks.industrialwaste.ui.DialogHelper;
import java.awt.event.KeyEvent;
import jp.co.astax.awt.FontUtility;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JTextField;

/**
 * 種類セレクター
 */
public class GoodsSelector extends JComponent {

    public static final int CLEARED = -1;
    public static final int CANCELED = 0;
    public static final int SELECTED = 1;

    protected static Logger logger_
             = Logger.getLogger(GoodsSelector.class.getName());
    private ListTableModel model_;
    private int result_;
    private int selectedRow_;
    private JTable listTable_;
    private JTextField tf;

    public GoodsSelector(Connection con) {
        this(con, 0);
    }

    public GoodsSelector(Connection con, int garbageSNo) {
        if (null == con) {
            throw new NullPointerException("'con' cannot be null.");
        }

        model_ = new ListTableModel(con);
        result_ = CANCELED;
        selectedRow_ = -1;
        initComponents();
    }

    public Map<String, Object> selectedRow() {
        if (0 > selectedRow_) {
            return null;
        } else {
            return model_.getColumns(selectedRow_);
        }
    }

    public int showSelector(Component c) {
        SearchCondition[] search = new SearchCondition[0];
        SortCondition[] sort
                 = new SortCondition[]{
                     new SortCondition(
                              new Column("ID", "id", ColumnType.TEXT),
                              SortOrder.ASC)};
        model_.refresh(search, sort);

//ダイアログで表示
        JDialog dialog = DialogHelper.getNewInstance(this, true);
        dialog.setLayout(new BorderLayout());
        dialog.add(this);
        dialog.setModal(true);
        dialog.setSize(400, 500);
        dialog.setLocation(650, 200);
        dialog.setVisible(true);

        return result_;
    }

    private void initComponents() {
        setLayout(new BorderLayout());

        Font font18 = FontUtility.getFont("メイリオ", Font.PLAIN, 14);
        // タイトル
        JLabel label = new JLabel("品名選択", JLabel.LEFT);
        label.setFont(font18);
        label.setBorder(BorderFactory.createLoweredBevelBorder());
        label.setBackground(Color.BLUE);
        label.setForeground(Color.WHITE);
        label.setOpaque(true);
        label.setMinimumSize(new Dimension(250, 25));
        label.setPreferredSize(new Dimension(250, 25));
        add(label, BorderLayout.NORTH);

        // 検索リスト
        JPanel pane = new JPanel(new GridBagLayout());
        // 検索Box
        tf = new JTextField("");
        tf.setFont(font18);
        tf.setMinimumSize(new Dimension(200, 30));
        tf.setPreferredSize(new Dimension(200, 30));
        tf.addActionListener(new SearchListAction());
        tf.addFocusListener(new java.awt.event.FocusAdapter() {
            // フォーカスが移ったときはIMEをOFFにする
            @Override
            public void focusLost(java.awt.event.FocusEvent e) {
                tf.getInputContext().setCompositionEnabled(false);
                tf.getInputContext().setCharacterSubsets(null);
            }

            @Override
            public void focusGained(java.awt.event.FocusEvent evt) {
                // カタカナモード
                setKatakanaMode();
            }
        });
        pane.add(
                 tf,
                 new GridBagConstraints(
                          0, 0, 1, 1, 0.0, 0.0,
                          GridBagConstraints.EAST, GridBagConstraints.NONE,
                          new Insets(2, 2, 2, 2), 0, 0));
        // 検索リスト
        listTable_ = new JTable(model_, new ListTableColumnModel());
        listTable_.setRowHeight(30);
        listTable_.setFont(font18);
        listTable_.setColumnSelectionAllowed(false);
        listTable_.setRowSelectionAllowed(true);
        listTable_.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        listTable_.getTableHeader().setReorderingAllowed(false);

        listTable_.addKeyListener(new java.awt.event.KeyAdapter() {

            @Override
            //頭にカーソル表示
            public void keyPressed(java.awt.event.KeyEvent evt) {
                if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
                    doSelectAction();
                }
            }
        });

        listTable_.addMouseListener(
                 new MouseAdapter() {
            public void mouseClicked(final MouseEvent me) {
                if (me.getClickCount() == 2 && listTable_.getSelectedRow() >= 0) {
                    doSelectAction();
                }
            }
        });
        pane.add(
                 new JScrollPane(listTable_),
                 new GridBagConstraints(
                          0, 1, 1, 1, 1.0, 1.0,
                          GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                          new Insets(2, 2, 2, 2), 0, 0));
        add(pane);

        // ボタン
        pane = new JPanel(new GridBagLayout());
        // クリア
        JButton b = new JButton("削除");
        b.setFont(font18);
        b.setMargin(new Insets(0, 0, 0, 0));
        b.setMinimumSize(new Dimension(120, 60));
        b.setPreferredSize(new Dimension(120, 60));
        b.addActionListener(new ClearAction());
        pane.add(
                 b,
                 new GridBagConstraints(
                          0, 0, 1, 1, 0.5, 0.0,
                          GridBagConstraints.WEST, GridBagConstraints.NONE,
                          new Insets(2, 2, 2, 2), 0, 0));
        // 選択
        b = new JButton("選択");
        b.setFont(font18);
        b.setMargin(new Insets(0, 0, 0, 0));
        b.setMinimumSize(new Dimension(120, 60));
        b.setPreferredSize(new Dimension(120, 60));
        b.addActionListener(new SelectAction());
        pane.add(
                 b,
                 new GridBagConstraints(
                          1, 0, 1, 1, 0.5, 0.0,
                          GridBagConstraints.EAST, GridBagConstraints.NONE,
                          new Insets(2, 2, 2, 2), 0, 0));
        // キャンセル
        b = new JButton("キャンセル");
        b.setFont(font18);
        b.setMargin(new Insets(0, 0, 0, 0));
        b.setMinimumSize(new Dimension(120, 60));
        b.setPreferredSize(new Dimension(120, 60));
        b.addActionListener(new CancelAction());
        pane.add(
                 b,
                 new GridBagConstraints(
                          2, 0, 1, 1, 0.0, 0.0,
                          GridBagConstraints.WEST, GridBagConstraints.NONE,
                          new Insets(2, 2, 2, 2), 0, 0));
        add(pane, BorderLayout.SOUTH);
    }

    private void closeWindow() {
        Window w = SwingUtilities.getWindowAncestor(this);
        if (null != w) {
            w.dispatchEvent(new WindowEvent(w, WindowEvent.WINDOW_CLOSING));
        }
    }

    private class ClearAction implements ActionListener {

        public void actionPerformed(ActionEvent event) {
            selectedRow_ = -1;
            result_ = CLEARED;
            closeWindow();
        }
    }

    public void doSelectAction() {
        selectedRow_ = listTable_.getSelectedRow();
        if (0 > selectedRow_) {
            JOptionPane.showMessageDialog(
                     GoodsSelector.this,
                     "項目を選択してください.",
                     "警告",
                     JOptionPane.WARNING_MESSAGE);
        } else {
            result_ = SELECTED;
            closeWindow();
        }
    }

    private class SelectAction implements ActionListener {

        public void actionPerformed(ActionEvent event) {
            doSelectAction();
        }
    }

    private class CancelAction implements ActionListener {

        public void actionPerformed(ActionEvent event) {
            selectedRow_ = -1;
            result_ = CANCELED;
            closeWindow();
        }
    }

    private class ListTableModel extends AbstractTableModel {

        private Connection con_;
        private List<Map<String, Object>> rows_
                 = Collections.synchronizedList(new ArrayList<Map<String, Object>>());

        public ListTableModel(Connection con) {
            con_ = con;
        }

        public int getRowCount() {
            return rows_.size();
        }

        public int getColumnCount() {
            return 2;
        }

        public Object getValueAt(int row, int column) {
            Map<String, Object> columns = rows_.get(row);
            switch (column) {
                case 0:
                    return columns.get("id");
                case 1:
                    return columns.get("name");
                case 2:
                    return columns.get("option1_name");
                case 3:
                    return columns.get("carry_name");
                default:
                    return null;
            }
        }

        public void clear() {
            rows_.clear();
        }

        public void refresh(SearchCondition[] search, SortCondition[] sort) {
            clear();
            try {
                PreparedStatement ps
                         = DBManager.prepareStatement(
                                  con_,
                                  "v0_goods_kind",
                                  "*",
                                  (null == search) ? (new SearchCondition[0]) : (search),
                                  (null == sort) ? (new SortCondition[0]) : (sort),
                                  0);
                try {
                    ResultSet rs = ps.executeQuery();
                    ResultSetMetaData md = rs.getMetaData();
                    while (rs.next()) {
                        Map<String, Object> columns = new HashMap<>();
                        for (int i = 1; i <= md.getColumnCount(); i++) {
                            String name = md.getColumnName(i);
                            switch (md.getColumnType(i)) {
                                case Types.VARCHAR:
                                    columns.put(name, rs.getString(name));
                                    break;
                                case Types.TIMESTAMP:
                                    columns.put(name, DBManager.convert(rs.getTimestamp(name)));
                                    break;
                                case Types.DATE:
                                    columns.put(name, DBManager.convert(rs.getDate(name)));
                                    break;
                                case Types.INTEGER:
                                    columns.put(name, rs.getInt(name));
                                    break;
                                case Types.DOUBLE:
                                    columns.put(name, rs.getDouble(name));
                                    break;
                                case Types.BOOLEAN:
                                    columns.put(name, rs.getBoolean(name));
                                    break;
                                default:
                                    columns.put(name, rs.getObject(name));
                            }
                        }
                        rows_.add(columns);
                    }
                } finally {
                    ps.close();
                }
            } catch (SQLException e) {
                logger_.warning(e.toString());
            }
            fireTableDataChanged();
        }

        public Map<String, Object> getColumns(int row) {
            return rows_.get(row);
        }
    }

    private class ListTableColumnModel extends DefaultTableColumnModel {

        public ListTableColumnModel() {
            // ID
            TableColumn col = new TableColumn(0);
            col.setHeaderValue("ID");
            col.setPreferredWidth(60);
            addColumn(col);
            // 名称
            col = new TableColumn(1);
            col.setHeaderValue("名称");
            col.setPreferredWidth(200);
            addColumn(col);
            // 処分
            col = new TableColumn(2);
            col.setHeaderValue("処分");
            col.setPreferredWidth(60);
            addColumn(col);
            // 搬入出
            col = new TableColumn(3);
            col.setHeaderValue("搬入出");
            col.setPreferredWidth(60);
            addColumn(col);
        }
    }

    private class SearchListAction implements ActionListener {

        public void actionPerformed(ActionEvent event) {
            Object source = event.getSource();
            if (source instanceof JTextField) {
                String s = ((JTextField) source).getText().trim();
                SearchCondition[] search = new SearchCondition[0];
                if (!"".equals(s)) {
                    search
                             = new SearchCondition[]{
                                 new SearchCondition(
                                          new Column("種類", "kana", ColumnType.TEXT),
                                          CompareOperator.LIKE,
                                          s,
                                          CombineOperator.OR),
                                 new SearchCondition(
                                          new Column("種類", "name", ColumnType.TEXT),
                                          CompareOperator.LIKE,
                                          s,
                                          CombineOperator.NONE)};
                }
                SortCondition[] sort
                         = new SortCondition[]{
                             new SortCondition(
                                      new Column("ID", "id", ColumnType.TEXT),
                                      SortOrder.ASC)};
                model_.refresh(search, sort);

                if (model_.getRowCount() > 0) {
                    //絞り込み検索後に一番上を選択
                    listTable_.setRowSelectionInterval(0, 0);
                    listTable_.requestFocus();
                }
            }
        }
    }
    // カタカナモードの設定

    private void setKatakanaMode() {
        Character.Subset[] subsets = {java.awt.im.InputSubset.HALFWIDTH_KATAKANA};
        tf.getInputContext().setCompositionEnabled(true);
        tf.getInputContext().setCharacterSubsets(subsets);
    }
}
