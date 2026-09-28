package aks.swing;

import java.awt.Font;
import java.util.Calendar;

public class DateField1 extends javax.swing.JPanel {

    public final static int YMDHMS = 0;
    public final static int YMDHM = 1;
    public final static int YMDH = 2;
    public final static int YMD = 3;
    public final static int YM = 4;
    public final static int Y = 5;
    public int MODE = 0;

    public DateField1(int mode) {
        initComponents();
        MODE = mode;
        initComponents2();

        Calendar cal = Calendar.getInstance();
        setValue(cal);
    }

    private void initComponents2() {

        cmbYear.removeAllItems();
        Calendar now = Calendar.getInstance();
        now.add(Calendar.YEAR, -10);
        for (int i = 0; i < 20; i++) {
            now.add(Calendar.YEAR, 1);
            cmbYear.addItem(now.get(Calendar.YEAR));
        }

        cmbMonth.removeAllItems();
        cmbMonth.addItem(null);
        for (int i = 1; i < 13; i++) {
            cmbMonth.addItem(i);
        }

        cmbDay.removeAllItems();
        cmbDay.addItem(null);
        for (int i = 1; i < 32; i++) {
            cmbDay.addItem(i);
        }

        cmbHour.removeAllItems();
        cmbHour.addItem(null);
        for (int i = 1; i < 32; i++) {
            cmbHour.addItem(i);
        }

        cmbMinute.removeAllItems();
        cmbMinute.addItem(null);
        for (int i = 0; i < 59; i++) {
            cmbMinute.addItem(i);
        }

        if (YMDHM == MODE) {

        } else if (YMDH == MODE) {
            cmbMinute.setVisible(false);
        } else if (YMD == MODE) {
            cmbHour.setVisible(false);
            cmbMinute.setVisible(false);
            jLabel3.setVisible(false);
        } else if (YM == MODE) {
            cmbDay.setVisible(false);
            cmbHour.setVisible(false);
            cmbMinute.setVisible(false);
//        jLabel2.setVisible(true);
            jLabel4.setVisible(false);
            jLabel3.setVisible(false);
        } else if (Y == MODE) {
            cmbMonth.setVisible(false);
            cmbDay.setVisible(false);
            cmbHour.setVisible(false);
            cmbMinute.setVisible(false);
            jLabel2.setVisible(false);
            jLabel4.setVisible(false);
            jLabel3.setVisible(false);
        }

    }

    public void setValue(Calendar cal_) {

        cmbYear.setSelectedItem(cal_.get(Calendar.YEAR));
        cmbMonth.setSelectedItem(cal_.get(Calendar.MONTH) + 1);
        cmbDay.setSelectedItem(cal_.get(Calendar.DAY_OF_MONTH));
        cmbHour.setSelectedItem(cal_.get(Calendar.HOUR_OF_DAY));
        cmbMinute.setSelectedItem(cal_.get(Calendar.MINUTE));
    }

    public Calendar getValue() {

        Calendar ret = Calendar.getInstance();

        ret.set(
                 (Integer) cmbYear.getSelectedItem(),
                 (Integer) cmbMonth.getSelectedItem() - 1,
                 (Integer) cmbDay.getSelectedItem(),
                 (Integer) cmbHour.getSelectedItem(),
                 (Integer) cmbMinute.getSelectedItem()
        );

        return ret;
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        cmbYear = new javax.swing.JComboBox();
        cmbMonth = new javax.swing.JComboBox();
        cmbHour = new javax.swing.JComboBox();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        cmbMinute = new javax.swing.JComboBox();
        cmbDay = new javax.swing.JComboBox();
        jLabel4 = new javax.swing.JLabel();

        setMaximumSize(new java.awt.Dimension(420, 25));
        setMinimumSize(new java.awt.Dimension(420, 25));
        setPreferredSize(new java.awt.Dimension(420, 25));
        setLayout(new java.awt.GridBagLayout());

        cmbYear.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        cmbYear.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "2000", " " }));
        cmbYear.setMaximumSize(new java.awt.Dimension(80, 25));
        cmbYear.setMinimumSize(new java.awt.Dimension(80, 25));
        cmbYear.setPreferredSize(new java.awt.Dimension(80, 25));
        cmbYear.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                cmbYearKeyPressed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        add(cmbYear, gridBagConstraints);

        cmbMonth.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        cmbMonth.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12" }));
        cmbMonth.setMaximumSize(new java.awt.Dimension(55, 25));
        cmbMonth.setMinimumSize(new java.awt.Dimension(55, 25));
        cmbMonth.setPreferredSize(new java.awt.Dimension(55, 25));
        cmbMonth.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                cmbMonthKeyPressed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        add(cmbMonth, gridBagConstraints);

        cmbHour.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        cmbHour.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23" }));
        cmbHour.setMaximumSize(new java.awt.Dimension(55, 25));
        cmbHour.setMinimumSize(new java.awt.Dimension(55, 25));
        cmbHour.setPreferredSize(new java.awt.Dimension(55, 25));
        cmbHour.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                cmbHourKeyPressed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 7;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        add(cmbHour, gridBagConstraints);

        jLabel1.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        jLabel1.setText("年");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 0, 5);
        add(jLabel1, gridBagConstraints);

        jLabel2.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        jLabel2.setText("月");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 0, 5);
        add(jLabel2, gridBagConstraints);

        jLabel3.setFont(new java.awt.Font("MS UI Gothic", 1, 14)); // NOI18N
        jLabel3.setText("：");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 8;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 0, 5);
        add(jLabel3, gridBagConstraints);

        cmbMinute.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        cmbMinute.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30", "31", "32", "33", "34", "35", "36", "37", "38", "39", "40", "41", "42", "43", "44", "45", "46", "47", "48", "49", "50", "51", "52", "53", "54", "55", "56", "57", "58", "59" }));
        cmbMinute.setMaximumSize(new java.awt.Dimension(55, 25));
        cmbMinute.setMinimumSize(new java.awt.Dimension(55, 25));
        cmbMinute.setPreferredSize(new java.awt.Dimension(55, 25));
        cmbMinute.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                cmbMinuteKeyPressed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 9;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        add(cmbMinute, gridBagConstraints);

        cmbDay.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        cmbDay.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30", "31" }));
        cmbDay.setMaximumSize(new java.awt.Dimension(55, 25));
        cmbDay.setMinimumSize(new java.awt.Dimension(55, 25));
        cmbDay.setPreferredSize(new java.awt.Dimension(55, 25));
        cmbDay.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                cmbDayKeyPressed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        add(cmbDay, gridBagConstraints);

        jLabel4.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        jLabel4.setText("日");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 0, 5);
        add(jLabel4, gridBagConstraints);
    }// </editor-fold>//GEN-END:initComponents

    private void cmbYearKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cmbYearKeyPressed

    }//GEN-LAST:event_cmbYearKeyPressed

    private void cmbMonthKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cmbMonthKeyPressed

    }//GEN-LAST:event_cmbMonthKeyPressed

    private void cmbDayKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cmbDayKeyPressed

    }//GEN-LAST:event_cmbDayKeyPressed

    private void cmbHourKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cmbHourKeyPressed

    }//GEN-LAST:event_cmbHourKeyPressed

    private void cmbMinuteKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cmbMinuteKeyPressed

    }//GEN-LAST:event_cmbMinuteKeyPressed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox cmbDay;
    private javax.swing.JComboBox cmbHour;
    private javax.swing.JComboBox cmbMinute;
    private javax.swing.JComboBox cmbMonth;
    private javax.swing.JComboBox cmbYear;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    // End of variables declaration//GEN-END:variables

    public void setFont1(Font f) {
        cmbYear.setFont(f);
        cmbMonth.setFont(f);
        cmbDay.setFont(f);
        cmbHour.setFont(f);
        cmbMinute.setFont(f);
    }
}
