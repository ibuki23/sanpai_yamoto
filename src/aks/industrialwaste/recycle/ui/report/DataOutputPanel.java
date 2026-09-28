package aks.industrialwaste.recycle.ui.report;

import aks.industrialwaste.database.Column;
import aks.industrialwaste.database.ColumnType;
import aks.industrialwaste.database.CombineOperator;
import aks.industrialwaste.database.CompareOperator;
import aks.industrialwaste.database.DBManager;
import aks.industrialwaste.database.SearchCondition;
import aks.industrialwaste.database.SortCondition;
import aks.industrialwaste.database.SortOrder;
import aks.industrialwaste.recycle.ui.weighlist.WeighListTableModel;
import java.awt.Dimension;
import java.awt.Font;
import java.io.File;
import java.sql.Connection;
import java.util.Calendar;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import jp.co.astax.awt.FontUtility;
import jp.co.astax.swing.DateTimeField;

public class DataOutputPanel extends javax.swing.JPanel {

    private DateTimeField dateField1_;
    private Connection con_;

    public DataOutputPanel(Connection con) {
        initComponents();
        con_ = con;

        Font font18 = FontUtility.getFont("メイリオ", Font.PLAIN, 18);

        dateField1_ = new DateTimeField(DateTimeField.DateFormat.YMD);
        dateField1_.setFont(font18);
        dateField1_.setPreferredSize(new Dimension(200, 35));
        dateField1_.setMinimumSize(dateField1_.getPreferredSize());
        取込日パネル_.add(dateField1_);

        Calendar c1 = Calendar.getInstance();
        dateField1_.setValue(c1);

    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        jPanel1 = new javax.swing.JPanel();
        取込日パネル_ = new javax.swing.JPanel();
        cmb出力区分 = new javax.swing.JComboBox();
        jButton2 = new javax.swing.JButton();

        setMaximumSize(new java.awt.Dimension(450, 250));
        setMinimumSize(new java.awt.Dimension(450, 250));
        setPreferredSize(new java.awt.Dimension(450, 250));
        setLayout(new javax.swing.BoxLayout(this, javax.swing.BoxLayout.LINE_AXIS));

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED), "計量記録管理簿csv出力", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("メイリオ", 0, 24), new java.awt.Color(0, 0, 0))); // NOI18N
        jPanel1.setMaximumSize(new java.awt.Dimension(450, 250));
        jPanel1.setMinimumSize(new java.awt.Dimension(450, 250));
        jPanel1.setPreferredSize(new java.awt.Dimension(450, 250));
        jPanel1.setLayout(new java.awt.GridBagLayout());

        取込日パネル_.setMaximumSize(new java.awt.Dimension(200, 35));
        取込日パネル_.setMinimumSize(new java.awt.Dimension(200, 35));
        取込日パネル_.setPreferredSize(new java.awt.Dimension(200, 35));
        取込日パネル_.setLayout(new javax.swing.BoxLayout(取込日パネル_, javax.swing.BoxLayout.LINE_AXIS));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        jPanel1.add(取込日パネル_, gridBagConstraints);

        cmb出力区分.setFont(new java.awt.Font("メイリオ", 0, 18)); // NOI18N
        cmb出力区分.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "自社（請求先ID：１）", "管理（請求先ID：１以外）" }));
        cmb出力区分.setMaximumSize(new java.awt.Dimension(280, 30));
        cmb出力区分.setMinimumSize(new java.awt.Dimension(280, 30));
        cmb出力区分.setPreferredSize(new java.awt.Dimension(280, 30));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        jPanel1.add(cmb出力区分, gridBagConstraints);

        jButton2.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        jButton2.setText("指定日出力");
        jButton2.setMargin(new java.awt.Insets(2, 2, 2, 2));
        jButton2.setMaximumSize(new java.awt.Dimension(120, 30));
        jButton2.setMinimumSize(new java.awt.Dimension(120, 30));
        jButton2.setPreferredSize(new java.awt.Dimension(120, 30));
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        jPanel1.add(jButton2, gridBagConstraints);

        add(jPanel1);
    }// </editor-fold>//GEN-END:initComponents

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // TODO add your handling code here:
        doReport();
    }//GEN-LAST:event_jButton2ActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox cmb出力区分;
    private javax.swing.JButton jButton2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel 取込日パネル_;
    // End of variables declaration//GEN-END:variables

    private void doReport() {

// 20111019 add 一覧表示されているデータをcsvファイルに落とす
        int confirm = JOptionPane.showConfirmDialog(
                 this, "選択した日の計量データをファイルに保存します. よろしいですか?",
                 "確認", JOptionPane.OK_CANCEL_OPTION);

        if (confirm == JOptionPane.OK_OPTION) {
            Calendar c = dateField1_.getValue();
            JFileChooser fc = new JFileChooser();
            fc.setSelectedFile(new File("C:\\spat_exe\\csv",
                     String.format("%1$tY%1$tm%1$td.csv", c)));
            if (JFileChooser.APPROVE_OPTION != fc.showSaveDialog(this)) {
                return;
            }
            File f = fc.getSelectedFile();
            f = new File(String.format("%s", f.getPath()));

            WeighListTableModel model_ = new WeighListTableModel(con_);

            SearchCondition[] search;
            if (cmb出力区分.getSelectedIndex() == 0) {
                search
                         = new SearchCondition[]{
                             new SearchCondition(
                                      new Column("時間", "term_date", ColumnType.DATE),
                                      CompareOperator.EQ,
                                      DBManager.convert(dateField1_.getValue()),
                                      CombineOperator.AND),
                             new SearchCondition(
                                      new Column("請求先ID", "bill_id", ColumnType.INT4),
                                      CompareOperator.EQ,
                                      1,
                                      CombineOperator.NONE)};
            } else {
                search
                         = new SearchCondition[]{
                             new SearchCondition(
                                      new Column("時間", "term_date", ColumnType.DATE),
                                      CompareOperator.EQ,
                                      DBManager.convert(dateField1_.getValue()),
                                      CombineOperator.AND),
                             new SearchCondition(
                                      new Column("請求先ID", "bill_id", ColumnType.INT4),
                                      CompareOperator.NT,
                                      cmb出力区分.getSelectedIndex(),
                                      CombineOperator.NONE)};
            }
            SortCondition[] sort
                     = new SortCondition[]{
                         new SortCondition(
                                  new Column("排出業者ｶﾅ", "ent_kana", ColumnType.TEXT),
                                  SortOrder.ASC),
                         new SortCondition(
                                  new Column("計量SNO", "sno", ColumnType.INT8),
                                  SortOrder.ASC)};
            model_.refresh(search, sort);

            if (!f.getPath().matches(".+\\.[cC][sS][vV]$")) {
                model_.report(new File("C:\\spat_exe\\csv",
                         String.format("%1$tY%1$tm%1$td%1$tH%1$tM.csv", c)));

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

            //CSV出力
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
