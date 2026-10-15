package aks.industrialwaste.ui.util;

import java.io.File;
import java.sql.*;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;
import aks.industrialwaste.database.*;
import aks.util.FontUtility;
import java.awt.Font;

public class GeneralFunctionPane extends javax.swing.JPanel {

    private Connection conn_;
    private Table table_;
    private List<SearchCondition> baseSearchList_;
    private List<SortCondition> baseSortList_;

    private List<SearchCondition> tmpSearchList_;
    private List<SortCondition> tmpSortList_;

    private DefaultTableModel searchModel_;
    private DefaultTableModel sortModel_;

    private boolean btnStatus_ = false;

    public GeneralFunctionPane(Connection con, Table table, List<SearchCondition> searchlist, List<SortCondition> sortlist) {
        initComponents();

        conn_ = con;
        table_ = table;
        baseSearchList_ = searchlist;
        baseSortList_ = sortlist;

        setInitGeneralFunction();
    }

    private Object convertValue(Column column, String value) {
        Object o = null;
        switch (column.getType()) {
            case INT4:
            case INT8:
            case FLOAT4:
            case FLOAT8:
                try {
                o = new DecimalFormat().parse(value);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(
                         this,
                         "検索値に数値を入力してください.",
                         "警告",
                         JOptionPane.WARNING_MESSAGE);
            }
            break;
            case BOOLEAN:
                value = (null == value) ? (value) : (value.toUpperCase());
                if ("0".equals(value)
                         || "FALSE".equals(value)
                         || "無し".equals(value)
                         || "なし".equals(value)) {
                    o = false;
                }
                if ("1".equals(value)
                         || "TRUE".equals(value)
                         || "有り".equals(value)
                         || "あり".equals(value)) {
                    o = true;
                } else {
                    JOptionPane.showMessageDialog(
                             this,
                             "検索値に0, 1, true, false, 有り, 無し, "
                             + "あり, なしのいずれかを入力してください.",
                             "警告",
                             JOptionPane.WARNING_MESSAGE);
                }
                break;
            case DATE:
                try {
                o = new SimpleDateFormat("yyyy/MM/dd").parse(value);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(
                         this,
                         "検索値に日付(YYYY/MM/DD)を入力してください.",
                         "警告",
                         JOptionPane.WARNING_MESSAGE);
            }
            break;
            case TIME:
                try {
                o = new SimpleDateFormat("HH:mm").parse(value);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(
                         this,
                         "検索値に時間(HH:mm)を入力してください.",
                         "警告",
                         JOptionPane.WARNING_MESSAGE);
            }
            break;
            case TIMESTAMP:
                try {
                o = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").parse(value);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(
                         this,
                         "検索値に日時(YYYY/MM/DD hh:mm:ss)を入力してください.",
                         "警告",
                         JOptionPane.WARNING_MESSAGE);
            }
            break;
            default:
                if (null == value) {
                    o = "";
                } else {
                    o = value.toString();
                }
        }

        return o;
    }

    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        Font font14 = FontUtility.getFont("メイリオ", Font.PLAIN, 14);

        searchPane = new javax.swing.JPanel();
        searchColListPane = new javax.swing.JScrollPane();
        searchColList = new javax.swing.JList();
        lbl1 = new javax.swing.JLabel();
        compareComboBox = new javax.swing.JComboBox();
        setValText = new javax.swing.JTextField();
        searchButtonsPane = new javax.swing.JPanel();
        searchGoLeftBtn = new javax.swing.JButton();
        searchGoRightBtn = new javax.swing.JButton();
        searchGoUpperBtn = new javax.swing.JButton();
        searchGoDownBtn = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        combineComboBox = new javax.swing.JComboBox();
        searchSetTablePane = new javax.swing.JScrollPane();
        searchSetTable = new javax.swing.JTable();
        sortPane = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        sortColListPane = new javax.swing.JScrollPane();
        sortColList = new javax.swing.JList();
        jLabel7 = new javax.swing.JLabel();
        sortComboBox = new javax.swing.JComboBox();
        sortButtonsPane = new javax.swing.JPanel();
        sortGoRightBtn = new javax.swing.JButton();
        sortGoLeftBtn = new javax.swing.JButton();
        sortGoUpperBtn = new javax.swing.JButton();
        sortGoDownBtn = new javax.swing.JButton();
        sortSetTablePane = new javax.swing.JScrollPane();
        sortSetTable = new javax.swing.JTable();
        controllPane = new javax.swing.JPanel();
        printBtn = new javax.swing.JButton();
        okBtn = new javax.swing.JButton();
        cancelBtn = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();

        setLayout(new java.awt.GridBagLayout());

        setMaximumSize(new java.awt.Dimension(620, 560));
        setMinimumSize(new java.awt.Dimension(620, 560));
        setPreferredSize(new java.awt.Dimension(620, 560));
        searchPane.setLayout(new java.awt.GridBagLayout());

        searchPane.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        searchPane.setPreferredSize(new java.awt.Dimension(600, 280));
        searchPane.setRequestFocusEnabled(false);
        searchColListPane.setPreferredSize(new java.awt.Dimension(150, 110));
        searchColList.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        searchColListPane.setViewportView(searchColList);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 10, 0, 0);
        searchPane.add(searchColListPane, gridBagConstraints);

        lbl1.setText("\u30ab\u30e9\u30e0\u4e00\u89a7");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 10, 0, 0);
        searchPane.add(lbl1, gridBagConstraints);

        compareComboBox.setModel(new javax.swing.DefaultComboBoxModel(new String[]{"like", "<", "<=", "=", ">=", ">", "<>"}));
        compareComboBox.setMinimumSize(new java.awt.Dimension(150, 25));
        compareComboBox.setPreferredSize(new java.awt.Dimension(150, 25));
        compareComboBox.setFont(font14);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 10, 0, 0);
        searchPane.add(compareComboBox, gridBagConstraints);

        setValText.setMinimumSize(new java.awt.Dimension(150, 25));
        setValText.setPreferredSize(new java.awt.Dimension(150, 25));
        setValText.setFont(font14);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 10, 0, 0);
        searchPane.add(setValText, gridBagConstraints);

        searchButtonsPane.setLayout(new java.awt.GridBagLayout());

        searchButtonsPane.setPreferredSize(new java.awt.Dimension(110, 160));
        searchButtonsPane.setRequestFocusEnabled(false);
        searchGoLeftBtn.setText("\u524a\u9664");
        searchGoLeftBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent event) {
                searchGoLeftBtnActionPerformed(event);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 10, 10);
        searchButtonsPane.add(searchGoLeftBtn, gridBagConstraints);

        searchGoRightBtn.setText("\u8ffd\u52a0");
        searchGoRightBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent event) {
                searchGoRightBtnActionPerformed(event);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 10, 10);
        searchButtonsPane.add(searchGoRightBtn, gridBagConstraints);

        searchGoUpperBtn.setText("\u4e0a\u3078");
        searchGoUpperBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent event) {
                searchGoUpperBtnActionPerformed(event);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 10, 10);
        searchButtonsPane.add(searchGoUpperBtn, gridBagConstraints);

        searchGoDownBtn.setText("\u4e0b\u3078");
        searchGoDownBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent event) {
                searchGoDownBtnActionPerformed(event);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 10, 10);
        searchButtonsPane.add(searchGoDownBtn, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridheight = 7;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 10, 10);
        searchPane.add(searchButtonsPane, gridBagConstraints);

        jLabel3.setText("\u6bd4\u8f03\u6f14\u7b97\u5b50");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 10, 0, 0);
        searchPane.add(jLabel3, gridBagConstraints);

        jLabel4.setText("\u691c\u7d22\u5024");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 10, 0, 0);
        searchPane.add(jLabel4, gridBagConstraints);

        jLabel5.setText("\u7d50\u5408\u5024");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 10, 0, 0);
        searchPane.add(jLabel5, gridBagConstraints);

        combineComboBox.setModel(new javax.swing.DefaultComboBoxModel(new String[]{"and", "or"}));
        combineComboBox.setPreferredSize(new java.awt.Dimension(39, 25));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 10, 5, 0);
        searchPane.add(combineComboBox, gridBagConstraints);

        String[] columnNames = {"カラム名", "比較演算子", "検索値", "結合値"};
        searchModel_ = new DefaultTableModel(columnNames, 0);
        searchSetTable.setModel(new javax.swing.table.DefaultTableModel(
                 new Object[][]{},
                 new String[]{}
        ));
        searchSetTable.setModel(searchModel_);
        searchSetTable.setFont(font14);
        searchSetTable.setRowHeight(30);
        searchSetTablePane.setViewportView(searchSetTable);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridheight = 8;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        searchPane.add(searchSetTablePane, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weighty = 0.5;
        gridBagConstraints.insets = new java.awt.Insets(5, 10, 10, 10);
        add(searchPane, gridBagConstraints);

        sortPane.setLayout(new java.awt.GridBagLayout());

        sortPane.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        sortPane.setPreferredSize(new java.awt.Dimension(600, 280));
        jLabel6.setText("\u30ab\u30e9\u30e0\u4e00\u89a7");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(5, 10, 0, 0);
        sortPane.add(jLabel6, gridBagConstraints);

        sortColListPane.setMinimumSize(new java.awt.Dimension(150, 100));
        sortColListPane.setPreferredSize(new java.awt.Dimension(150, 100));
        sortColList.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        sortColListPane.setViewportView(sortColList);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 10, 0, 0);
        sortPane.add(sortColListPane, gridBagConstraints);

        jLabel7.setText("\u6607\u9806\u30fb\u964d\u9806");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(5, 10, 0, 0);
        sortPane.add(jLabel7, gridBagConstraints);

        sortComboBox.setModel(new javax.swing.DefaultComboBoxModel(new String[]{"\u6607\u9806", "\u964d\u9806"}));
        sortComboBox.setMaximumSize(new java.awt.Dimension(150, 21));
        sortComboBox.setMinimumSize(new java.awt.Dimension(150, 19));
        sortComboBox.setPreferredSize(new java.awt.Dimension(150, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(5, 10, 5, 0);
        sortPane.add(sortComboBox, gridBagConstraints);

        sortButtonsPane.setLayout(new java.awt.GridBagLayout());

        sortButtonsPane.setMinimumSize(new java.awt.Dimension(79, 172));
        sortButtonsPane.setPreferredSize(new java.awt.Dimension(110, 160));
        sortGoRightBtn.setText("\u8ffd\u52a0");
        sortGoRightBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent event) {
                sortGoRightBtnActionPerformed(event);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 10, 10);
        sortButtonsPane.add(sortGoRightBtn, gridBagConstraints);

        sortGoLeftBtn.setText("\u524a\u9664");
        sortGoLeftBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent event) {
                sortGoLeftBtnActionPerformed(event);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 10, 10);
        sortButtonsPane.add(sortGoLeftBtn, gridBagConstraints);

        sortGoUpperBtn.setText("\u4e0a\u3078");
        sortGoUpperBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent event) {
                sortGoUpperBtnActionPerformed(event);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 10, 10);
        sortButtonsPane.add(sortGoUpperBtn, gridBagConstraints);

        sortGoDownBtn.setText("\u4e0b\u3078");
        sortGoDownBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent event) {
                sortGoDownBtnActionPerformed(event);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 10, 10);
        sortButtonsPane.add(sortGoDownBtn, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridheight = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 10, 10);
        sortPane.add(sortButtonsPane, gridBagConstraints);

        String[] sortColumnNames = {"カラム名", "ソート"};
        sortModel_ = new DefaultTableModel(sortColumnNames, 0);
        sortSetTable.setModel(new javax.swing.table.DefaultTableModel(
                 new Object[][]{},
                 new String[]{}
        ));
        sortSetTable.setModel(sortModel_);
        sortSetTablePane.setViewportView(sortSetTable);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridheight = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        sortPane.add(sortSetTablePane, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weighty = 0.5;
        gridBagConstraints.insets = new java.awt.Insets(5, 10, 10, 10);
        add(sortPane, gridBagConstraints);

        controllPane.setLayout(new java.awt.GridBagLayout());

        controllPane.setPreferredSize(new java.awt.Dimension(600, 27));
        printBtn.setText("\u30d5\u30a1\u30a4\u30eb\u51fa\u529b");
        printBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent event) {
                printBtnActionPerformed(event);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        controllPane.add(printBtn, gridBagConstraints);

        okBtn.setText("OK");
        okBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent event) {
                okBtnActionPerformed(event);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        controllPane.add(okBtn, gridBagConstraints);

        cancelBtn.setText("\u30ad\u30e3\u30f3\u30bb\u30eb");
        cancelBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent event) {
                cancelBtnActionPerformed(event);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 0, 0);
        controllPane.add(cancelBtn, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(10, 0, 0, 0);
        add(controllPane, gridBagConstraints);

        jLabel2.setText("\u691c\u7d22\u6761\u4ef6");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 10, 0, 0);
        add(jLabel2, gridBagConstraints);

        jLabel1.setText("\u30bd\u30fc\u30c8\u6761\u4ef6");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 10, 0, 0);
        add(jLabel1, gridBagConstraints);

    }

    private void printBtnActionPerformed(java.awt.event.ActionEvent evt) {
        int confirm = JOptionPane.showConfirmDialog(
                 this,
                 "CSVファイルに出力します。よろしいですか？",
                 "確認",
                 JOptionPane.OK_CANCEL_OPTION);
        if (confirm == JOptionPane.OK_OPTION) {
            JFileChooser fc = new JFileChooser();
            if (JFileChooser.APPROVE_OPTION != fc.showSaveDialog(this)) {
                return;
            }
            File f = fc.getSelectedFile();
            if (!f.getPath().matches(".+\\.[cC][sS][vV]$")) {
                f = new File(String.format("%s.csv", f.getPath()));
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

            SearchCondition[] search = tmpSearchList_.toArray(new SearchCondition[0]);
            SortCondition[] sort = tmpSortList_.toArray(new SortCondition[0]);

            int i = search.length;
            if (i != 0) {
                search[i - 1].setCombineOperator(CombineOperator.NONE);
            }

            try {
                new CSVFile(f).write(conn_, table_, search, sort);
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

    private void cancelBtnActionPerformed(java.awt.event.ActionEvent evt) {
        javax.swing.SwingUtilities.getWindowAncestor(this).dispose();
    }

    private void okBtnActionPerformed(java.awt.event.ActionEvent evt) {
        //OKボタンを押されたので、編集したListを受け取ったListへ上書き
        baseSearchList_.clear();
        baseSearchList_.addAll(tmpSearchList_);

        baseSortList_.clear();
        baseSortList_.addAll(tmpSortList_);

        //検索Listの最後の行の結合値をNoneにする
        int i = baseSearchList_.size();
        if (i != 0) {
            baseSearchList_.get(i - 1).setCombineOperator(CombineOperator.NONE);
        }
        btnStatus_ = true;
        javax.swing.SwingUtilities.getWindowAncestor(this).dispose();
    }

    private void sortGoDownBtnActionPerformed(java.awt.event.ActionEvent evt) {
        int i = sortSetTable.getSelectedRow();
        int j = sortSetTable.getRowCount();
        if (i == -1 || i == j - 1) {
            return;
        }

        moveTmpSortList(i, i + 1);
        moveRow2Model(i, i + 1, sortModel_, sortSetTable);
    }

    private void sortGoUpperBtnActionPerformed(java.awt.event.ActionEvent evt) {
        int i = sortSetTable.getSelectedRow();
        if (i == -1 || i == 0) {
            return;
        }

        moveTmpSortList(i, i - 1);
        moveRow2Model(i, i - 1, sortModel_, sortSetTable);
    }

    private void sortGoLeftBtnActionPerformed(java.awt.event.ActionEvent evt) {
        int i = sortSetTable.getSelectedRow();
        if (i == -1) {
            return;
        }

        tmpSortList_.remove(i);
        removeRow2Model(i, sortModel_, sortSetTable);
    }

    private void sortGoRightBtnActionPerformed(java.awt.event.ActionEvent evt) {
        int i = sortColList.getSelectedIndex();
        if (i == -1) {
            return;
        }

        Column[] columns = table_.getColumns();
        if (checkRegistedData(columns[i].getTitle(), sortSetTable)) {

            addTmpSortList(
                     columns[i],
                     getSortOrder(sortComboBox.getSelectedIndex()));
            String[] vals = {columns[i].getTitle(), (String) sortComboBox.getSelectedItem()};
            addRow2Model(vals, sortModel_, sortSetTable);
        } else {
            JOptionPane.showMessageDialog(this, "既にソート条件に登録されています", "二重登録警告", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void searchGoDownBtnActionPerformed(java.awt.event.ActionEvent evt) {
        int i = searchSetTable.getSelectedRow();
        int j = searchSetTable.getRowCount();
        if (i == -1 || i == j - 1) {
            return;
        }

        //移動する行の結合値がNoneだった場合、AndかOrを選択させる    
        if (!checkCombine(1, i + 1)) {
            return;
        }

        moveTmpSearchList(i, i + 1);
        moveRow2Model(i, i + 1, searchModel_, searchSetTable);
    }

    private void searchGoUpperBtnActionPerformed(java.awt.event.ActionEvent evt) {
        int i = searchSetTable.getSelectedRow();
        if (i == -1 || i == 0) {
            return;
        }

        //移動する行の結合値がNoneだった場合、AndかOrを選択させる    
        if (!checkCombine(2, i)) {
            return;
        }

        moveTmpSearchList(i, i - 1);
        moveRow2Model(i, i - 1, searchModel_, searchSetTable);
    }

    private void searchGoLeftBtnActionPerformed(java.awt.event.ActionEvent evt) {
        int i = searchSetTable.getSelectedRow();
        if (i == -1) {
            return;
        }

        tmpSearchList_.remove(i);
        removeRow2Model(i, searchModel_, searchSetTable);
    }

    private void searchGoRightBtnActionPerformed(java.awt.event.ActionEvent evt) {
        int i = searchColList.getSelectedIndex();
        if (i == -1) {
            return;
        }

        //追加する全行の結合値がNoneだった場合、AndかOrを選択させる
        int size = searchSetTable.getRowCount();
        if (size != 0) {
            if (!checkCombine(0, size - 1)) {
                return;
            }
        }

        Column[] columns = table_.getColumns();
        Object o = convertValue(columns[i], setValText.getText());
        if (null == o) {
            return;
        }
        addTmpSearchList(
                 columns[i],
                 getCompareOperator((String) compareComboBox.getSelectedItem()),
                 getCombineOperator((String) combineComboBox.getSelectedItem()),
                 o);

        String[] vals = {columns[i].getTitle(), (String) compareComboBox.getSelectedItem(), setValText.getText(), (String) combineComboBox.getSelectedItem()};
        addRow2Model(vals, searchModel_, searchSetTable);
    }

    private javax.swing.JButton cancelBtn;
    private javax.swing.JComboBox combineComboBox;
    private javax.swing.JComboBox compareComboBox;
    private javax.swing.JPanel controllPane;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel lbl1;
    private javax.swing.JButton okBtn;
    private javax.swing.JButton printBtn;
    private javax.swing.JPanel searchButtonsPane;
    private javax.swing.JList searchColList;
    private javax.swing.JScrollPane searchColListPane;
    private javax.swing.JButton searchGoDownBtn;
    private javax.swing.JButton searchGoLeftBtn;
    private javax.swing.JButton searchGoRightBtn;
    private javax.swing.JButton searchGoUpperBtn;
    private javax.swing.JPanel searchPane;
    private javax.swing.JTable searchSetTable;
    private javax.swing.JScrollPane searchSetTablePane;
    private javax.swing.JTextField setValText;
    private javax.swing.JPanel sortButtonsPane;
    private javax.swing.JList sortColList;
    private javax.swing.JScrollPane sortColListPane;
    private javax.swing.JComboBox sortComboBox;
    private javax.swing.JButton sortGoDownBtn;
    private javax.swing.JButton sortGoLeftBtn;
    private javax.swing.JButton sortGoRightBtn;
    private javax.swing.JButton sortGoUpperBtn;
    private javax.swing.JPanel sortPane;
    private javax.swing.JTable sortSetTable;
    private javax.swing.JScrollPane sortSetTablePane;

    private void setInitGeneralFunction() {
        Column[] columns = table_.getColumns();
        String[] col = new String[columns.length];
        for (int i = 0; i < columns.length; i++) {
            col[i] = columns[i].getTitle();
        }
        searchColList.setListData(col);
        sortColList.setListData(col);

        searchSetTable.setDefaultEditor(Object.class, null);
        searchSetTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        sortSetTable.setDefaultEditor(Object.class, null);
        sortSetTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        //tmpListを作る    
        tmpSearchList_ = new ArrayList<SearchCondition>(baseSearchList_);
        tmpSortList_ = new ArrayList<SortCondition>(baseSortList_);

        //検索Listをテーブルにセットする
        for (SearchCondition condition : tmpSearchList_) {
            String[] vals = {condition.getColumn().getTitle(),
                condition.getCompareOperator().getOperator(),
                valueToString(
                condition.getColumn().getType(),
                condition.getValue()),
                condition.getCombineOperator().getOperator()};
            addRow2Model(vals, searchModel_, searchSetTable);
        }

        //ソートListをテーブルにセットする
        for (SortCondition condition : tmpSortList_) {
            String s
                     = (SortOrder.DESC.equals(condition.getOrder())) ? ("降順") : ("昇順");
            String[] vals = {condition.getColumn().getTitle(), s};
            addRow2Model(vals, sortModel_, sortSetTable);
        }
    }

    private void addRow2Model(String[] vals, DefaultTableModel model, JTable table) {
        model.addRow(vals);
        int i = table.getRowCount();
        table.changeSelection(i - 1, 0, false, false);
    }

    private void removeRow2Model(int row, DefaultTableModel model, JTable table) {
        model.removeRow(row);
        if (row == 0) {
            table.changeSelection(0, 0, false, false);
        } else {
            table.changeSelection(row - 1, 0, false, false);
        }
    }

    private void moveRow2Model(int start, int end, DefaultTableModel model, JTable table) {
        model.moveRow(start, start, end);
        table.changeSelection(end, 0, false, false);
    }

    private boolean checkRegistedData(String col, JTable table) {
        int row = table.getRowCount();

        for (int i = 0; i < row; i++) {
            if (col.equals(table.getValueAt(i, 0).toString())) {
                return false;
            }
        }
        return true;
    }

    private void addTmpSearchList(
             Column column,
             CompareOperator compare,
             CombineOperator combine,
             Object value) {
        SearchCondition condition = new SearchCondition();
        condition.setColumn(column);
        condition.setCompareOperator(compare);
        condition.setCombineOperator(combine);
        condition.setValue(value);

        tmpSearchList_.add(condition);
    }

    private void addTmpSortList(Column column, SortOrder order) {
        SortCondition condition = new SortCondition();
        condition.setColumn(column);
        condition.setOrder(order);

        tmpSortList_.add(condition);
    }

    private void moveTmpSearchList(int start, int end) {
        SearchCondition condition = tmpSearchList_.get(start);
        tmpSearchList_.remove(start);

        tmpSearchList_.add(end, condition);
    }

    private void moveTmpSortList(int start, int end) {
        SortCondition condition = tmpSortList_.get(start);
        tmpSortList_.remove(start);

        tmpSortList_.add(end, condition);
    }

    //指定した行の連結値がNoneならAndかOrから選択させる
    private boolean checkCombine(int mine, int tar) {

        if (tmpSearchList_.get(tar).getCombineOperator().equals(CombineOperator.NONE)) {
            String[] combines = {"and", "or"};
            String combine = (String) JOptionPane.showInputDialog(
                     this, "結合値が空白の箇所があります。\r\n結合値を選択してください", "結合値選択",
                     JOptionPane.INFORMATION_MESSAGE, null, combines, combines[0]);

            if (combine != null) {
                tmpSearchList_.get(tar).setCombineOperator(getCombineOperator(combine));
                searchModel_.setValueAt(combine, tar, 3);

                return true;
            } else {
                return false;
            }
        }

        return true;
    }

    //受け取った文字列と対応したCompareOperatorを返す
    private CompareOperator getCompareOperator(String c) {
        if (c.equals("=")) {
            return CompareOperator.EQ;
        } else if (c.equals(">=")) {
            return CompareOperator.GE;
        } else if (c.equals("<=")) {
            return CompareOperator.LE;
        } else if (c.equals("<>")) {
            return CompareOperator.NT;
        } else if (c.equals(">")) {
            return CompareOperator.GT;
        } else if (c.equals("<")) {
            return CompareOperator.LT;
        } else if (c.equals("like")) {
            return CompareOperator.LIKE;
        }
        return null;
    }
    //受け取った文字列と対応したCombineOperatorを返す

    private CombineOperator getCombineOperator(String c) {
        if (c.equals("and")) {
            return CombineOperator.AND;
        } else if (c.equals("or")) {
            return CombineOperator.OR;
        } else {
            return CombineOperator.NONE;
        }
    }
    //受け取った数値と対応したSortOrderを返す

    private SortOrder getSortOrder(int i) {
        switch (i) {
            case 1:
                return SortOrder.DESC;
            default:
                return SortOrder.ASC;
        }
    }

    private String valueToString(ColumnType type, Object value) {
        switch (type) {
            case TIMESTAMP:
                if (value instanceof Calendar) {
                    return String.format("%1$tY/%1$tm/%1$td %1$tH:%1$tM", (Calendar) value);
                }
                return "";
            case DATE:
                if (value instanceof Calendar) {
                    return String.format("%1$tY/%1$tm/%1$td", (Calendar) value);
                } else if (value instanceof Date) {
                    return String.format("%1$tY/%1$tm/%1$td", (Date) value);
                } else {
                    return "";
                }
            case TIME:
                if (value instanceof Calendar) {
                    return String.format("%1$tH:%1$tM", (Calendar) value);
                }
                if (value instanceof Date) {
                    return String.format("%1$tH:%1$tM", (Date) value);
                } else {
                    return "";
                }
            case BOOLEAN:
                return (value instanceof Boolean) ? (((Boolean) value).toString()) : ("false");
            default:
                return (null == value) ? ("") : (value.toString());
        }
    }
}
