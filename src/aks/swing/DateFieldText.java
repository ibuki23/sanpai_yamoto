package aks.swing;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.*;
import java.util.Calendar;

public class DateFieldText extends javax.swing.JPanel {

    public final static int YMDHMS = 0;
    public final static int YMDHM = 1;
    public final static int YMDH = 2;
    public final static int YMD = 3;
    public final static int YM = 4;
    public final static int Y = 5;
    public int MODE = 0;

    public DateFieldText(int mode) {
        initComponents();
        MODE = mode;

        initComponents2();

        Calendar cal = Calendar.getInstance();
        setValue(cal);
    }

    private void initComponents2() {

        if (YMDHM == MODE) {

        } else if (YMDH == MODE) {
            txt分.setVisible(false);
        } else if (YMD == MODE) {
            txt時.setVisible(false);
            txt分.setVisible(false);
            jLabel3.setVisible(false);
        } else if (YM == MODE) {
            txt日.setVisible(false);
            txt時.setVisible(false);
            txt分.setVisible(false);
            //        jLabel2.setVisible(true);
            jLabel4.setVisible(false);
            jLabel3.setVisible(false);
        } else if (Y == MODE) {
            txt月.setVisible(false);
            txt日.setVisible(false);
            txt時.setVisible(false);
            txt分.setVisible(false);
            jLabel2.setVisible(false);
            jLabel4.setVisible(false);
            jLabel3.setVisible(false);
        }

    }

    public void setValue(Calendar cal_) {

        if (cal_ != null) {
            txt年.setText(String.valueOf(cal_.get(Calendar.YEAR)));
            txt月.setText(String.valueOf(cal_.get(Calendar.MONTH) + 1));
            txt日.setText(String.valueOf(cal_.get(Calendar.DAY_OF_MONTH)));
            txt時.setText(String.valueOf(cal_.get(Calendar.HOUR_OF_DAY)));
            txt分.setText(String.valueOf(cal_.get(Calendar.MINUTE)));
        } else {
            txt年.setText("");
            txt月.setText("");
            txt日.setText("");
            txt時.setText("");
            txt分.setText("");
        }
    }

    public void RequestFocus(int mode) {

        if (Y == mode) {
            txt年.requestFocus();
        } else if (YM == mode) {
            txt月.requestFocus();
        } else if (YMD == mode) {
            txt日.requestFocus();
        }

    }

    public void setFocus_txt年() {

        txt年.requestFocus();
    }

    public void setFocus_txt日() {

        txt日.requestFocus();
    }

    public void setFocus_txt分() {

        txt分.requestFocus();
    }

    public Calendar getValue() {

        Calendar ret = Calendar.getInstance();

        ret.set(
                 Integer.valueOf(txt年.getText()),
                 Integer.valueOf(txt月.getText()) - 1,
                 Integer.valueOf(txt日.getText()),
                 Integer.valueOf(txt時.getText()),
                 Integer.valueOf(txt分.getText())
        );

        return ret;
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        txt年 = new aks.swing.JTextFieldKeiryo();
        jLabel1 = new javax.swing.JLabel();
        txt月 = new aks.swing.JTextFieldKeiryo();
        jLabel2 = new javax.swing.JLabel();
        txt日 = new aks.swing.JTextFieldKeiryo();
        jLabel4 = new javax.swing.JLabel();
        txt時 = new aks.swing.JTextFieldKeiryo();
        jLabel3 = new javax.swing.JLabel();
        txt分 = new aks.swing.JTextFieldKeiryo();
        jPanel1 = new javax.swing.JPanel();

        setFocusable(false);
        setMaximumSize(new java.awt.Dimension(400, 30));
        setMinimumSize(new java.awt.Dimension(400, 30));
        setPreferredSize(new java.awt.Dimension(400, 30));
        setLayout(new java.awt.GridBagLayout());

        txt年.setFont(new java.awt.Font("メイリオ", 0, 24)); // NOI18N
        txt年.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txt年.setText("2000");
        txt年.setMaximumSize(new java.awt.Dimension(80, 40));
        txt年.setMinimumSize(new java.awt.Dimension(80, 40));
        txt年.setPreferredSize(new java.awt.Dimension(80, 40));
        txt年.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txt年KeyPressed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        add(txt年, gridBagConstraints);

        jLabel1.setFont(new java.awt.Font("メイリオ", 0, 18)); // NOI18N
        jLabel1.setText("年");
        jLabel1.setFocusable(false);
        jLabel1.setMaximumSize(new java.awt.Dimension(20, 35));
        jLabel1.setMinimumSize(new java.awt.Dimension(20, 35));
        jLabel1.setPreferredSize(new java.awt.Dimension(20, 35));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        add(jLabel1, gridBagConstraints);

        txt月.setFont(new java.awt.Font("メイリオ", 0, 18)); // NOI18N
        txt月.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txt月.setText("11");
        txt月.setMaximumSize(new java.awt.Dimension(35, 30));
        txt月.setMinimumSize(new java.awt.Dimension(35, 30));
        txt月.setPreferredSize(new java.awt.Dimension(35, 30));
        txt月.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txt月KeyPressed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        add(txt月, gridBagConstraints);

        jLabel2.setFont(new java.awt.Font("メイリオ", 0, 18)); // NOI18N
        jLabel2.setText("月");
        jLabel2.setFocusable(false);
        jLabel2.setMaximumSize(new java.awt.Dimension(20, 35));
        jLabel2.setMinimumSize(new java.awt.Dimension(20, 35));
        jLabel2.setPreferredSize(new java.awt.Dimension(20, 35));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        add(jLabel2, gridBagConstraints);

        txt日.setFont(new java.awt.Font("メイリオ", 0, 18)); // NOI18N
        txt日.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txt日.setText("12");
        txt日.setMaximumSize(new java.awt.Dimension(35, 30));
        txt日.setMinimumSize(new java.awt.Dimension(35, 30));
        txt日.setPreferredSize(new java.awt.Dimension(35, 30));
        txt日.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txt日KeyPressed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        add(txt日, gridBagConstraints);

        jLabel4.setFont(new java.awt.Font("メイリオ", 0, 18)); // NOI18N
        jLabel4.setText("日");
        jLabel4.setFocusable(false);
        jLabel4.setMaximumSize(new java.awt.Dimension(20, 35));
        jLabel4.setMinimumSize(new java.awt.Dimension(20, 35));
        jLabel4.setPreferredSize(new java.awt.Dimension(20, 35));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 5);
        add(jLabel4, gridBagConstraints);

        txt時.setFont(new java.awt.Font("メイリオ", 0, 18)); // NOI18N
        txt時.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txt時.setText("11");
        txt時.setMaximumSize(new java.awt.Dimension(35, 30));
        txt時.setMinimumSize(new java.awt.Dimension(35, 30));
        txt時.setPreferredSize(new java.awt.Dimension(35, 30));
        txt時.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txt時KeyPressed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 6;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        add(txt時, gridBagConstraints);

        jLabel3.setFont(new java.awt.Font("メイリオ", 0, 18)); // NOI18N
        jLabel3.setText("：");
        jLabel3.setFocusable(false);
        jLabel3.setMaximumSize(new java.awt.Dimension(20, 35));
        jLabel3.setMinimumSize(new java.awt.Dimension(20, 35));
        jLabel3.setPreferredSize(new java.awt.Dimension(20, 35));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 7;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        add(jLabel3, gridBagConstraints);

        txt分.setFont(new java.awt.Font("メイリオ", 0, 18)); // NOI18N
        txt分.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txt分.setText("33");
        txt分.setMaximumSize(new java.awt.Dimension(35, 30));
        txt分.setMinimumSize(new java.awt.Dimension(35, 30));
        txt分.setPreferredSize(new java.awt.Dimension(35, 30));
        txt分.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txt分KeyPressed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 8;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        add(txt分, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        add(jPanel1, gridBagConstraints);
    }// </editor-fold>//GEN-END:initComponents

    private void txt年KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt年KeyPressed
        // TODO add your handling code here:

        if (evt.getKeyCode() == KeyEvent.VK_ENTER || evt.getKeyCode() == KeyEvent.VK_DOWN) {
            if (txt月.isVisible()) {
                txt月.requestFocus();
            } else {
                txt年.transferFocus();
            }
        } else if (evt.getKeyCode() == KeyEvent.VK_UP) {
            transferFocusBackward();
        }

    }//GEN-LAST:event_txt年KeyPressed

    private void txt月KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt月KeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == KeyEvent.VK_ENTER || evt.getKeyCode() == KeyEvent.VK_DOWN) {
            if (txt日.isVisible()) {
                txt日.requestFocus();
            } else {
                txt月.transferFocus();
            }
        } else if (evt.getKeyCode() == KeyEvent.VK_UP) {
            txt年.requestFocus();
        }

    }//GEN-LAST:event_txt月KeyPressed

    private void txt日KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt日KeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == KeyEvent.VK_ENTER || evt.getKeyCode() == KeyEvent.VK_DOWN) {
            if (txt時.isVisible()) {
                txt時.requestFocus();
            } else {
                txt日.transferFocus();
            }
        } else if (evt.getKeyCode() == KeyEvent.VK_UP) {
            txt月.requestFocus();
        }

    }//GEN-LAST:event_txt日KeyPressed

    private void txt時KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt時KeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == KeyEvent.VK_ENTER || evt.getKeyCode() == KeyEvent.VK_DOWN) {
            if (txt分.isVisible()) {
                txt分.requestFocus();
            } else {
                txt時.transferFocus();
            }
        } else if (evt.getKeyCode() == KeyEvent.VK_UP) {
            txt日.requestFocus();
        }

    }//GEN-LAST:event_txt時KeyPressed

    private void txt分KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt分KeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == KeyEvent.VK_ENTER || evt.getKeyCode() == KeyEvent.VK_DOWN) {
            txt分.transferFocus();
        } else if (evt.getKeyCode() == KeyEvent.VK_UP) {
            txt時.requestFocus();
        }

    }//GEN-LAST:event_txt分KeyPressed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JTextField txt分;
    private javax.swing.JTextField txt年;
    private javax.swing.JTextField txt日;
    private javax.swing.JTextField txt時;
    private javax.swing.JTextField txt月;
    // End of variables declaration//GEN-END:variables

    public void setFont1(Font f) {
        txt年.setFont(f);
        txt月.setFont(f);
        txt日.setFont(f);
        txt時.setFont(f);
        txt分.setFont(f);

        jLabel1.setFont(f);
        jLabel2.setFont(f);
        jLabel3.setFont(f);
        jLabel4.setFont(f);

        if (f.getSize() == 14) {
            txt年.setMinimumSize(new Dimension(50, 35));
            txt年.setPreferredSize(new Dimension(50, 35));
            txt月.setMinimumSize(new Dimension(35, 35));
            txt月.setPreferredSize(new Dimension(35, 35));
            txt日.setMinimumSize(new Dimension(35, 35));
            txt日.setPreferredSize(new Dimension(35, 35));
            txt時.setMinimumSize(new Dimension(35, 35));
            txt時.setPreferredSize(new Dimension(35, 35));
            txt分.setMinimumSize(new Dimension(35, 35));
            txt分.setPreferredSize(new Dimension(35, 35));
            jLabel1.setMinimumSize(new Dimension(15, 35));
            jLabel1.setPreferredSize(new Dimension(15, 35));
            jLabel2.setMinimumSize(new Dimension(15, 35));
            jLabel2.setPreferredSize(new Dimension(15, 35));
            jLabel3.setMinimumSize(new Dimension(15, 35));
            jLabel3.setPreferredSize(new Dimension(15, 35));
            jLabel4.setMinimumSize(new Dimension(15, 35));
            jLabel4.setPreferredSize(new Dimension(15, 35));
        } else if (f.getSize() == 18) {
            txt年.setMinimumSize(new Dimension(65, 35));
            txt年.setPreferredSize(new Dimension(65, 35));
            txt月.setMinimumSize(new Dimension(50, 35));
            txt月.setPreferredSize(new Dimension(50, 35));
            txt日.setMinimumSize(new Dimension(50, 35));
            txt日.setPreferredSize(new Dimension(50, 35));
            txt時.setMinimumSize(new Dimension(50, 35));
            txt時.setPreferredSize(new Dimension(50, 35));
            txt分.setMinimumSize(new Dimension(50, 35));
            txt分.setPreferredSize(new Dimension(50, 35));
        } else if (f.getSize() == 24) {
            txt年.setMinimumSize(new Dimension(80, 40));
            txt年.setPreferredSize(new Dimension(80, 40));
            txt月.setMinimumSize(new Dimension(50, 40));
            txt月.setPreferredSize(new Dimension(50, 40));
            txt日.setMinimumSize(new Dimension(50, 40));
            txt日.setPreferredSize(new Dimension(50, 40));
            txt時.setMinimumSize(new Dimension(50, 40));
            txt時.setPreferredSize(new Dimension(50, 40));
            txt分.setMinimumSize(new Dimension(50, 40));
            txt分.setPreferredSize(new Dimension(50, 40));
            jLabel1.setMinimumSize(new Dimension(30, 30));
            jLabel1.setPreferredSize(new Dimension(30, 30));
            jLabel2.setMinimumSize(new Dimension(30, 30));
            jLabel2.setPreferredSize(new Dimension(30, 30));
            jLabel3.setMinimumSize(new Dimension(30, 30));
            jLabel3.setPreferredSize(new Dimension(30, 30));
            jLabel4.setMinimumSize(new Dimension(30, 30));
            jLabel4.setPreferredSize(new Dimension(30, 30));
        }
    }

    @Override
    public void setSize(Dimension d) {
        setMinimumSize(d);
        setPreferredSize(d);
    }
}
