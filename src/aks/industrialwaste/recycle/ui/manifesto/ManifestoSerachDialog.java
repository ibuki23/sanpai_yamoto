package aks.industrialwaste.recycle.ui.manifesto;

import aks.industrialwaste.database.Column;
import aks.industrialwaste.database.ColumnType;
import aks.industrialwaste.database.CombineOperator;
import aks.industrialwaste.database.CompareOperator;
import java.sql.Connection;
import java.text.SimpleDateFormat;
import java.util.*;
import javax.swing.*;
import aks.industrialwaste.recycle.ui.sql.SQLCommon;
import java.awt.Window;
import java.awt.Frame;
import java.io.IOException;
import aks.industrialwaste.database.DBManager;
import aks.industrialwaste.database.SearchCondition;
import aks.industrialwaste.database.SortCondition;
import aks.industrialwaste.database.Table;
import aks.industrialwaste.ui.util.CSVFile;
import aks.util.FontUtility;
import java.awt.Dimension;
import java.awt.Font;
import java.io.File;
import jp.co.astax.swing.DateTimeField;

public class ManifestoSerachDialog extends javax.swing.JPanel {

    private SQLCommon mSQLCommon;
    private Connection con_;

    public String[][] entName2Array = new String[7][];
    //選択されている行の各idを取得する
    public int ent_id_ = 0;
    public int goods_id_ = 0;
    public int office_id_ = 0;
    public int trans1_id_ = 0;
    public int trans2_id_ = 0;
    public int cont_office_id_ = 0;
    public int cont_id_ = 0;
    public int car_kind1_id_ = 0;
    public int car_kind2_id_ = 0;
    public int methot_id_ = 0;
    //取得したidを要素とする配列のindexを取得する(表示のため)
    public int ent_id_index = 0;
    public int office_id_index = 0;
    public int trans1_id_index = 0;
    public int trans2_id_index = 0;
    public int cont_id_index = 4;
    public int methot_id_index = 0;
    public int cont_office_id_index = 0;
    public int car_kind1_id_index = 0;
    public int car_kind2_id_index = 0;
    public int row_ = -1;

    private Table table_;
    private DateTimeField startDateField_;
    private DateTimeField endDateField_;

    public ManifestoSerachDialog(Connection con, Table table, List<SearchCondition> searchlist, List<SortCondition> sortlist) {
        initComponents();
        con_ = con;
        table_ = table;

        Font font14 = FontUtility.getFont("メイリオ", Font.PLAIN, 14);
        Calendar c1 = Calendar.getInstance();
        Calendar c2 = Calendar.getInstance();

        startDateField_ = new DateTimeField(DateTimeField.DateFormat.YMD);
        startDateField_.setFont(font14);
        startDateField_.setMinimumSize(new Dimension(180, 30));
        startDateField_.setPreferredSize(new Dimension(180, 30));
        date1.add(startDateField_);
        startDateField_.setValue(c1);

        endDateField_ = new DateTimeField(DateTimeField.DateFormat.YMD);
        endDateField_.setFont(font14);
        endDateField_.setMinimumSize(new Dimension(180, 30));
        endDateField_.setPreferredSize(new Dimension(180, 30));
        date2.add(endDateField_);
        endDateField_.setValue(c2);

        //数量単位のボタングループ
        ButtonGroup bg1 = new ButtonGroup();
        bg1.add(jRadioButton1);
        bg1.add(jRadioButton2);
        jCheckBox1.setSelected(true);
        jRadioButton1.setSelected(true);
        clearView();

        mSQLCommon = new SQLCommon(DBManager.jdbc_, DBManager.getUser(), DBManager.getPassword());

        //排出業者リスト取得
        mSQLCommon.GetMasterData4("v0_ent", entName2Array);
        String[] EntName = new String[entName2Array[0].length];
        for (int i = 0; i <= entName2Array[0].length - 1; i++) {
            EntName[i] = (String) entName2Array[1][i].toString();
        }
        ent_cmb.setModel(new javax.swing.DefaultComboBoxModel(EntName));
        id_search_.setText(String.valueOf(0));
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        jPanel21 = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        ent_cmb = new javax.swing.JComboBox();
        id_search_ = new aks.swing.JTextFieldHankaku();
        jLabel79 = new javax.swing.JLabel();
        jLabel128 = new javax.swing.JLabel();
        jCheckBox1 = new javax.swing.JCheckBox();
        jRadioButton1 = new javax.swing.JRadioButton();
        jRadioButton2 = new javax.swing.JRadioButton();
        jPanel2 = new javax.swing.JPanel();
        date1 = new javax.swing.JPanel();
        date2 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        rejist_btn_kensetu = new javax.swing.JButton();
        quit_btn_kensetu = new javax.swing.JButton();

        setMaximumSize(new java.awt.Dimension(650, 135));
        setMinimumSize(new java.awt.Dimension(650, 135));
        setName(""); // NOI18N
        setPreferredSize(new java.awt.Dimension(650, 135));
        setLayout(new java.awt.GridBagLayout());

        jPanel21.setMaximumSize(new java.awt.Dimension(600, 170));
        jPanel21.setMinimumSize(new java.awt.Dimension(600, 170));
        jPanel21.setPreferredSize(new java.awt.Dimension(600, 170));
        jPanel21.setLayout(new java.awt.GridBagLayout());

        jPanel1.setMaximumSize(new java.awt.Dimension(600, 250));
        jPanel1.setMinimumSize(new java.awt.Dimension(600, 250));
        jPanel1.setPreferredSize(new java.awt.Dimension(600, 250));
        jPanel1.setLayout(new java.awt.GridBagLayout());

        ent_cmb.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        ent_cmb.setMaximumSize(new java.awt.Dimension(400, 27));
        ent_cmb.setMinimumSize(new java.awt.Dimension(400, 27));
        ent_cmb.setPreferredSize(new java.awt.Dimension(400, 27));
        ent_cmb.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                ent_cmbItemStateChanged(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 10);
        jPanel1.add(ent_cmb, gridBagConstraints);

        id_search_.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        id_search_.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        id_search_.setMaximumSize(new java.awt.Dimension(60, 25));
        id_search_.setMinimumSize(new java.awt.Dimension(60, 25));
        id_search_.setPreferredSize(new java.awt.Dimension(60, 25));
        id_search_.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                id_search_FocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                id_search_FocusLost(evt);
            }
        });
        id_search_.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                id_search_KeyPressed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        jPanel1.add(id_search_, gridBagConstraints);

        jLabel79.setFont(new java.awt.Font("メイリオ", 1, 14)); // NOI18N
        jLabel79.setText("　排出業者指定");
        jLabel79.setMaximumSize(new java.awt.Dimension(120, 25));
        jLabel79.setMinimumSize(new java.awt.Dimension(120, 25));
        jLabel79.setOpaque(true);
        jLabel79.setPreferredSize(new java.awt.Dimension(120, 25));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        jPanel1.add(jLabel79, gridBagConstraints);

        jLabel128.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        jLabel128.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel128.setText("排出業者ｺｰﾄﾞ");
        jLabel128.setMaximumSize(new java.awt.Dimension(100, 20));
        jLabel128.setMinimumSize(new java.awt.Dimension(100, 20));
        jLabel128.setPreferredSize(new java.awt.Dimension(100, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 15, 0, 2);
        jPanel1.add(jLabel128, gridBagConstraints);

        jCheckBox1.setFont(new java.awt.Font("メイリオ", 1, 14)); // NOI18N
        jCheckBox1.setText("排出業者指定無し");
        jCheckBox1.setMaximumSize(new java.awt.Dimension(250, 31));
        jCheckBox1.setMinimumSize(new java.awt.Dimension(250, 31));
        jCheckBox1.setPreferredSize(new java.awt.Dimension(250, 31));
        jCheckBox1.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jCheckBox1ItemStateChanged(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        jPanel1.add(jCheckBox1, gridBagConstraints);

        jRadioButton1.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        jRadioButton1.setText("交付日指定　");
        jRadioButton1.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jRadioButton1.setMaximumSize(new java.awt.Dimension(120, 25));
        jRadioButton1.setMinimumSize(new java.awt.Dimension(120, 25));
        jRadioButton1.setPreferredSize(new java.awt.Dimension(120, 25));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        jPanel1.add(jRadioButton1, gridBagConstraints);

        jRadioButton2.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        jRadioButton2.setText("処分日指定　");
        jRadioButton2.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jRadioButton2.setMaximumSize(new java.awt.Dimension(120, 25));
        jRadioButton2.setMinimumSize(new java.awt.Dimension(120, 25));
        jRadioButton2.setPreferredSize(new java.awt.Dimension(120, 25));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        jPanel1.add(jRadioButton2, gridBagConstraints);

        jPanel2.setMaximumSize(new java.awt.Dimension(20, 20));
        jPanel2.setMinimumSize(new java.awt.Dimension(20, 20));

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 20, Short.MAX_VALUE)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 20, Short.MAX_VALUE)
        );

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        jPanel1.add(jPanel2, gridBagConstraints);

        date1.setMaximumSize(new java.awt.Dimension(250, 30));
        date1.setMinimumSize(new java.awt.Dimension(250, 30));
        date1.setPreferredSize(new java.awt.Dimension(250, 30));
        date1.setLayout(new javax.swing.BoxLayout(date1, javax.swing.BoxLayout.LINE_AXIS));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        jPanel1.add(date1, gridBagConstraints);

        date2.setMaximumSize(new java.awt.Dimension(250, 30));
        date2.setMinimumSize(new java.awt.Dimension(250, 30));
        date2.setPreferredSize(new java.awt.Dimension(250, 30));
        date2.setLayout(new javax.swing.BoxLayout(date2, javax.swing.BoxLayout.LINE_AXIS));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        jPanel1.add(date2, gridBagConstraints);

        jLabel1.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        jLabel1.setText("～");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridheight = 2;
        jPanel1.add(jLabel1, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        jPanel21.add(jPanel1, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        add(jPanel21, gridBagConstraints);

        rejist_btn_kensetu.setFont(new java.awt.Font("メイリオ", 0, 18)); // NOI18N
        rejist_btn_kensetu.setText("出　力");
        rejist_btn_kensetu.setMinimumSize(new java.awt.Dimension(110, 30));
        rejist_btn_kensetu.setPreferredSize(new java.awt.Dimension(110, 30));
        rejist_btn_kensetu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rejist_btn_kensetuActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        add(rejist_btn_kensetu, gridBagConstraints);

        quit_btn_kensetu.setFont(new java.awt.Font("メイリオ", 0, 18)); // NOI18N
        quit_btn_kensetu.setText("閉じる");
        quit_btn_kensetu.setMinimumSize(new java.awt.Dimension(110, 30));
        quit_btn_kensetu.setPreferredSize(new java.awt.Dimension(110, 30));
        quit_btn_kensetu.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                quit_btn_kensetuMousePressed(evt);
            }
        });
        quit_btn_kensetu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                quit_btn_kensetuActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 10);
        add(quit_btn_kensetu, gridBagConstraints);
    }// </editor-fold>//GEN-END:initComponents

    private void quit_btn_kensetuMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_quit_btn_kensetuMousePressed

    }//GEN-LAST:event_quit_btn_kensetuMousePressed

    private void rejist_btn_kensetuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rejist_btn_kensetuActionPerformed
        // TODO add your handling code here:

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

            // 検索条件, ソート条件を設定
            Date startDate = startDateField_.getValueAtDate();
            Date endTime = endDateField_.getValueAtDate();
            SearchCondition[] search;
            if (jCheckBox1.isSelected() && jRadioButton1.isSelected()) {
                search
                         = new SearchCondition[]{
                             new SearchCondition(
                                      new Column("時間", "grant_date12", ColumnType.DATE),
                                      CompareOperator.GE,
                                      startDate,
                                      CombineOperator.AND),
                             new SearchCondition(
                                      new Column("時間", "grant_date12", ColumnType.DATE),
                                      CompareOperator.LE,
                                      endTime,
                                      CombineOperator.NONE)};
            } else if (jCheckBox1.isSelected() && jRadioButton2.isSelected()) {
                search
                         = new SearchCondition[]{
                             new SearchCondition(
                                      new Column("時間", "cont_date12", ColumnType.DATE),
                                      CompareOperator.GE,
                                      startDate,
                                      CombineOperator.AND),
                             new SearchCondition(
                                      new Column("時間", "cont_date12", ColumnType.DATE),
                                      CompareOperator.LE,
                                      endTime,
                                      CombineOperator.NONE)};
            } else if (!jCheckBox1.isSelected() && jRadioButton1.isSelected()) {
                search
                         = new SearchCondition[]{
                             new SearchCondition(
                                      new Column("排出業者ID", "dis_ent_id", ColumnType.INT4),
                                      CompareOperator.EQ,
                                      Integer.valueOf(id_search_.getText().replaceAll(",", "")),
                                      CombineOperator.AND),
                             new SearchCondition(
                                      new Column("時間", "grant_date12", ColumnType.DATE),
                                      CompareOperator.GE,
                                      startDate,
                                      CombineOperator.AND),
                             new SearchCondition(
                                      new Column("時間", "grant_date12", ColumnType.DATE),
                                      CompareOperator.LE,
                                      endTime,
                                      CombineOperator.NONE)};
            } else if (!jCheckBox1.isSelected() && jRadioButton2.isSelected()) {
                search
                         = new SearchCondition[]{
                             new SearchCondition(
                                      new Column("排出業者ID", "dis_ent_id", ColumnType.INT4),
                                      CompareOperator.EQ,
                                      Integer.valueOf(id_search_.getText().replaceAll(",", "")),
                                      CombineOperator.AND),
                             new SearchCondition(
                                      new Column("時間", "cont_date12", ColumnType.DATE),
                                      CompareOperator.GE,
                                      startDate,
                                      CombineOperator.AND),
                             new SearchCondition(
                                      new Column("時間", "cont_date12", ColumnType.DATE),
                                      CompareOperator.LE,
                                      endTime,
                                      CombineOperator.NONE)};
            } else if (jRadioButton1.isSelected()) {
                search
                         = new SearchCondition[]{
                             new SearchCondition(
                                      new Column("時間", "grant_date12", ColumnType.DATE),
                                      CompareOperator.GE,
                                      startDate,
                                      CombineOperator.AND),
                             new SearchCondition(
                                      new Column("時間", "grant_date12", ColumnType.DATE),
                                      CompareOperator.LE,
                                      endTime,
                                      CombineOperator.NONE)};
            } else if (jRadioButton2.isSelected()) {
                search
                         = new SearchCondition[]{
                             new SearchCondition(
                                      new Column("時間", "cont_date12", ColumnType.DATE),
                                      CompareOperator.GE,
                                      startDate,
                                      CombineOperator.AND),
                             new SearchCondition(
                                      new Column("時間", "cont_date12", ColumnType.DATE),
                                      CompareOperator.LE,
                                      endTime,
                                      CombineOperator.NONE)};
            } else {
                search
                         = new SearchCondition[]{
                             new SearchCondition(
                                      new Column("時間", "grant_date12", ColumnType.DATE),
                                      CompareOperator.GE,
                                      startDate,
                                      CombineOperator.AND),
                             new SearchCondition(
                                      new Column("時間", "grant_date12", ColumnType.DATE),
                                      CompareOperator.LE,
                                      endTime,
                                      CombineOperator.NONE)};
            }

            SortCondition[] sort
                     = new SortCondition[]{
                         new SortCondition(
                                  new Column("排出業者ID", "dis_ent_id", ColumnType.INT4),
                                  aks.industrialwaste.database.SortOrder.ASC),
                         new SortCondition(
                                  new Column("商品ID", "goods_id", ColumnType.INT4),
                                  aks.industrialwaste.database.SortOrder.ASC),
                         new SortCondition(
                                  new Column("交付年月日", "grant_date12", ColumnType.DATE),
                                  aks.industrialwaste.database.SortOrder.ASC)};

            try {
                new CSVFile(f).write(con_, table_, search, sort);
                doExcel(f.getAbsolutePath());
            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(
                         this,
                         "CSVファイルへの出力に失敗しました.",
                         "警告",
                         JOptionPane.WARNING_MESSAGE);
            }
        }
    }//GEN-LAST:event_rejist_btn_kensetuActionPerformed

    private void quit_btn_kensetuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_quit_btn_kensetuActionPerformed
        // TODO add your handling code here:
        doClose();
    }//GEN-LAST:event_quit_btn_kensetuActionPerformed

    private void id_search_FocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_id_search_FocusGained
        // TODO add your handling code here:
        id_search_.selectAll();
    }//GEN-LAST:event_id_search_FocusGained

    private void id_search_FocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_id_search_FocusLost
        // TODO add your handling code here:
        private_id_search();
    }//GEN-LAST:event_id_search_FocusLost

    private void private_id_search() {
        try {
            int search_id = Integer.valueOf(id_search_.getText());
            String name = mSQLCommon.GetMasterDataName("v0_ent", search_id);
            ent_cmb.setSelectedItem(name);

        } catch (Exception e) {
            id_search_.setText("0");
        }
    }

    private void id_search_KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_id_search_KeyPressed

    }//GEN-LAST:event_id_search_KeyPressed

    private void ent_cmbItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_ent_cmbItemStateChanged
        // TODO add your handling code here:

        try {

            String name = (String) ent_cmb.getSelectedItem();
            int search_id = mSQLCommon.GetMasterDataID("v0_ent", name);
            id_search_.setText(String.valueOf(search_id));

        } catch (Exception e) {
            id_search_.setText("0");
        }
    }//GEN-LAST:event_ent_cmbItemStateChanged

    private void jCheckBox1ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jCheckBox1ItemStateChanged
        // TODO add your handling code here:

        id_search_.setEnabled(!jCheckBox1.isSelected());
        id_search_.setEditable(!jCheckBox1.isSelected());
        ent_cmb.setEnabled(!jCheckBox1.isSelected());
    }//GEN-LAST:event_jCheckBox1ItemStateChanged

    private void clearView() {

    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel date1;
    private javax.swing.JPanel date2;
    private javax.swing.JComboBox ent_cmb;
    private javax.swing.JTextField id_search_;
    private javax.swing.JCheckBox jCheckBox1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel128;
    private javax.swing.JLabel jLabel79;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel21;
    private javax.swing.JRadioButton jRadioButton1;
    private javax.swing.JRadioButton jRadioButton2;
    private javax.swing.JButton quit_btn_kensetu;
    private javax.swing.JButton rejist_btn_kensetu;
    // End of variables declaration//GEN-END:variables

    protected void doClose() {
        Window w = SwingUtilities.getWindowAncestor(this);
        if (w instanceof Frame) {
            w.dispose();

        } else {
            w.dispose();
        }
    }

    private void doExcel(String file_path) {
        try {
            Runtime runtime = Runtime.getRuntime();
            Process proc;
            proc = runtime.exec("cmd /c " + file_path);
            proc.waitFor();

        } catch (InterruptedException ev) {
            System.out.println("失敗(InnterruptedException)");
        } catch (IOException ev) {
            System.out.println("失敗(IOException)");
        }
    }
}
