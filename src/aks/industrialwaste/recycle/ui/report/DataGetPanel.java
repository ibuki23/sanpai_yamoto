package aks.industrialwaste.recycle.ui.report;

import aks.industrialwaste.database.DBManager;
import aks.industrialwaste.recycle.ui.MainFrame;
import java.awt.Dimension;
import java.awt.Font;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import javax.swing.JOptionPane;
import jp.co.astax.awt.FontUtility;
import jp.co.astax.swing.DateTimeField;

public class DataGetPanel extends javax.swing.JPanel {

    private DateTimeField dateField1_;
    private DateTimeField dateField2_;

    public DataGetPanel() {
        initComponents();

        Font font18 = FontUtility.getFont("メイリオ", Font.PLAIN, 18);

        dateField1_ = new DateTimeField(DateTimeField.DateFormat.YMD);
        dateField1_.setFont(font18);
        dateField1_.setPreferredSize(new Dimension(200, 35));
        dateField1_.setMinimumSize(dateField1_.getPreferredSize());
        取込日パネル_.add(dateField1_);

        dateField2_ = new DateTimeField(DateTimeField.DateFormat.YM);
        dateField2_.setFont(font18);
        dateField2_.setPreferredSize(new Dimension(200, 35));
        dateField2_.setMinimumSize(dateField1_.getPreferredSize());
        取込月パネル_.add(dateField2_);
        Calendar c1 = Calendar.getInstance();
        dateField1_.setValue(c1);
        Calendar c2 = (Calendar) c1.clone();
        c2.set(Calendar.DAY_OF_MONTH, 1);
        dateField2_.setValue(c2);
        計量データ取込panel.setVisible(false);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        計量データ取込panel = new javax.swing.JPanel();
        取込日パネル_ = new javax.swing.JPanel();
        取込月パネル_ = new javax.swing.JPanel();
        cmb指定月取込 = new javax.swing.JComboBox();
        jButton1 = new javax.swing.JButton();
        cmb指定日取込 = new javax.swing.JComboBox();
        jButton2 = new javax.swing.JButton();

        setMaximumSize(new java.awt.Dimension(450, 250));
        setMinimumSize(new java.awt.Dimension(450, 250));
        setPreferredSize(new java.awt.Dimension(450, 250));
        setLayout(new javax.swing.BoxLayout(this, javax.swing.BoxLayout.LINE_AXIS));

        計量データ取込panel.setBorder(javax.swing.BorderFactory.createTitledBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED), "計量データ取込", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("メイリオ", 0, 24))); // NOI18N
        計量データ取込panel.setMaximumSize(new java.awt.Dimension(450, 250));
        計量データ取込panel.setMinimumSize(new java.awt.Dimension(450, 250));
        計量データ取込panel.setPreferredSize(new java.awt.Dimension(450, 250));
        計量データ取込panel.setLayout(new java.awt.GridBagLayout());

        取込日パネル_.setMaximumSize(new java.awt.Dimension(270, 35));
        取込日パネル_.setMinimumSize(new java.awt.Dimension(270, 35));
        取込日パネル_.setPreferredSize(new java.awt.Dimension(270, 35));
        取込日パネル_.setLayout(new javax.swing.BoxLayout(取込日パネル_, javax.swing.BoxLayout.LINE_AXIS));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(5, 0, 5, 0);
        計量データ取込panel.add(取込日パネル_, gridBagConstraints);

        取込月パネル_.setMaximumSize(new java.awt.Dimension(270, 35));
        取込月パネル_.setMinimumSize(new java.awt.Dimension(270, 35));
        取込月パネル_.setPreferredSize(new java.awt.Dimension(270, 35));
        取込月パネル_.setLayout(new javax.swing.BoxLayout(取込月パネル_, javax.swing.BoxLayout.LINE_AXIS));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(10, 0, 5, 0);
        計量データ取込panel.add(取込月パネル_, gridBagConstraints);

        cmb指定月取込.setFont(new java.awt.Font("メイリオ", 0, 18)); // NOI18N
        cmb指定月取込.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "プラント（稲瀬町）", "　" }));
        cmb指定月取込.setMaximumSize(new java.awt.Dimension(270, 30));
        cmb指定月取込.setMinimumSize(new java.awt.Dimension(270, 30));
        cmb指定月取込.setPreferredSize(new java.awt.Dimension(270, 30));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        計量データ取込panel.add(cmb指定月取込, gridBagConstraints);

        jButton1.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        jButton1.setText("指定月取込");
        jButton1.setMargin(new java.awt.Insets(2, 2, 2, 2));
        jButton1.setMaximumSize(new java.awt.Dimension(120, 30));
        jButton1.setMinimumSize(new java.awt.Dimension(120, 30));
        jButton1.setPreferredSize(new java.awt.Dimension(120, 30));
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        計量データ取込panel.add(jButton1, gridBagConstraints);

        cmb指定日取込.setFont(new java.awt.Font("メイリオ", 0, 18)); // NOI18N
        cmb指定日取込.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "プラント（稲瀬町）", "　" }));
        cmb指定日取込.setMaximumSize(new java.awt.Dimension(270, 30));
        cmb指定日取込.setMinimumSize(new java.awt.Dimension(270, 30));
        cmb指定日取込.setPreferredSize(new java.awt.Dimension(270, 30));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        計量データ取込panel.add(cmb指定日取込, gridBagConstraints);

        jButton2.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        jButton2.setText("指定日取込");
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
        計量データ取込panel.add(jButton2, gridBagConstraints);

        add(計量データ取込panel);
    }// </editor-fold>//GEN-END:initComponents

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // TODO add your handling code here:
        int result = JOptionPane.showConfirmDialog(
                 DataGetPanel.this,
                 "選択した日の計量データを取り込みます。よろしいですか？",
                 "確認",
                 JOptionPane.YES_NO_OPTION);

        if (JOptionPane.YES_OPTION == result) {
            Calendar c2 = Calendar.getInstance();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd hh:mm:ss");

            if (指定日取込(cmb指定日取込.getSelectedIndex(), dateField1_.getValue())) {

                ReportJournal.recordJournal(MainFrame.con1_, "日時データ取込処理",
                         sdf.format(DBManager.convert(c2)),//実行日
                         DBManager.convert(dateField1_.getValue().getTime()).toString(),//取込対象日時
                         cmb指定日取込.getSelectedIndex());
                JOptionPane.showMessageDialog(
                         this,
                         "データ取込正常終了しました。",
                         "警告",
                         JOptionPane.INFORMATION_MESSAGE);
            } else {

                ReportJournal.recordJournal(MainFrame.con1_, "日時データ取込処理(失敗)",
                         sdf.format(DBManager.convert(c2)),//実行日
                         DBManager.convert(dateField1_.getValue().getTime()).toString(),//取込対象日時
                         cmb指定日取込.getSelectedIndex());

                JOptionPane.showMessageDialog(
                         this,
                         "データ取込中に異常が発生しました。",
                         "警告",
                         JOptionPane.WARNING_MESSAGE);
            }
        }
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        int result = JOptionPane.showConfirmDialog(
                 DataGetPanel.this,
                 "選択した月の計量データを取り込みます。よろしいですか？",
                 "確認",
                 JOptionPane.YES_NO_OPTION);
        if (JOptionPane.YES_OPTION == result) {
            if (指定月取込(cmb指定月取込.getSelectedIndex(), dateField2_.getValue())) {
                JOptionPane.showMessageDialog(
                         this,
                         "データ取込正常終了しました。",
                         "警告",
                         JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(
                         this,
                         "データ取込中に異常が発生しました。",
                         "警告",
                         JOptionPane.WARNING_MESSAGE);
            }
        }

    }//GEN-LAST:event_jButton1ActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox cmb指定日取込;
    private javax.swing.JComboBox cmb指定月取込;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JPanel 取込日パネル_;
    private javax.swing.JPanel 取込月パネル_;
    public static javax.swing.JPanel 計量データ取込panel;
    // End of variables declaration//GEN-END:variables

    private boolean 指定日取込(int index, Calendar c) {
        boolean ret = false;
        try {
            String s;
            Statement st = MainFrame.con1_.createStatement();
            st.executeUpdate("begin;");

            try {
                if (index == 0 && MainFrame.con1_ != null) {
                    s = new StringBuilder()
                             .append("select get_data01_day(?::text, ?::date, ?::integer); ")
                             .toString();
                } else if (index == 1 && MainFrame.con1_ != null) {
                    s = new StringBuilder()
                             .append("select get_data02_day(?::text, ?::date, ?::integer); ")
                             .toString();
                } else {
                    s = "";
                }

                if (s.length() > 0) {
                    PreparedStatement ps = MainFrame.con1_.prepareStatement(s);
                    try {
                        ps.setObject(1, DBManager.host1_);
                        ps.setObject(2, DBManager.convert(c));
                        ps.setObject(3, 10000);
                        ps.executeQuery();
                    } finally {
                        ps.close();
                    }
                }

                st.executeUpdate("commit;");
                ret = true;
            } catch (SQLException e) {
                st.executeUpdate("rollback;");
                ret = false;
                throw e;
            } finally {
                st.close();
            }

        } catch (Exception e) {
        }

        return ret;
    }

    private boolean 指定月取込(int index, Calendar c) {
        boolean ret = false;
        try {
            String s;
            Statement st = MainFrame.con1_.createStatement();
            st.executeUpdate("begin;");

            try {
                if (index == 0 && MainFrame.con1_ != null) {
                    s = new StringBuilder()
                             .append("select get_data01_month(?::text, date_trunc('month', ?::date)::date, ?::integer); ")
                             .toString();
                } else if (index == 1 && MainFrame.con1_ != null) {
                    s = new StringBuilder()
                             .append("select get_data02_month(?::text, date_trunc('month', ?::date)::date, ?::integer); ")
                             .toString();
                } else {
                    s = "";
                }

                if (s.length() > 0) {
                    PreparedStatement ps = MainFrame.con1_.prepareStatement(s);
                    try {
                        ps.setObject(1, DBManager.host1_);
                        ps.setObject(2, DBManager.convert(c));
                        ps.setObject(3, 10000);
                        ps.executeQuery();
                    } finally {
                        ps.close();
                    }
                }

                st.executeUpdate("commit;");
                ret = true;
            } catch (SQLException e) {
                st.executeUpdate("rollback;");
                ret = false;
                throw e;
            } finally {
                st.close();
            }

        } catch (Exception e) {
        }

        return ret;
    }
}
