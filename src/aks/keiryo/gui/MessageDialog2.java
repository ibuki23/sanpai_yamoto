package aks.keiryo.gui;

import java.awt.event.KeyEvent;

public class MessageDialog2 extends javax.swing.JDialog {

    public boolean mResult;

    public MessageDialog2(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jBtn_Confirm = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTextArea1 = new javax.swing.JTextArea();
        jBtn_Cancel = new javax.swing.JButton();

        setTitle("メッセージ確認");
        setResizable(false);
        addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentShown(java.awt.event.ComponentEvent evt) {
                formComponentShown(evt);
            }
        });

        jBtn_Confirm.setFont(new java.awt.Font("メイリオ", 0, 18)); // NOI18N
        jBtn_Confirm.setText("実　行");
        jBtn_Confirm.setMargin(new java.awt.Insets(2, 2, 2, 2));
        jBtn_Confirm.setMaximumSize(new java.awt.Dimension(80, 30));
        jBtn_Confirm.setMinimumSize(new java.awt.Dimension(80, 30));
        jBtn_Confirm.setPreferredSize(new java.awt.Dimension(80, 30));
        jBtn_Confirm.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBtn_ConfirmActionPerformed(evt);
            }
        });
        jBtn_Confirm.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jBtn_ConfirmKeyPressed(evt);
            }
        });

        jScrollPane2.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        jScrollPane2.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);

        jTextArea1.setEditable(false);
        jTextArea1.setColumns(20);
        jTextArea1.setFont(new java.awt.Font("メイリオ", 0, 24)); // NOI18N
        jTextArea1.setLineWrap(true);
        jTextArea1.setRows(5);
        jTextArea1.setText("messaage text\n１２３４５７８９０１２３４５７８９０１２３４５７８９０１２３４５７８９０１２３４５７８９０");
        jTextArea1.setAutoscrolls(false);
        jTextArea1.setFocusable(false);
        jScrollPane2.setViewportView(jTextArea1);

        jBtn_Cancel.setFont(new java.awt.Font("メイリオ", 0, 18)); // NOI18N
        jBtn_Cancel.setText("取り消し");
        jBtn_Cancel.setMargin(new java.awt.Insets(2, 2, 2, 2));
        jBtn_Cancel.setMaximumSize(new java.awt.Dimension(80, 30));
        jBtn_Cancel.setMinimumSize(new java.awt.Dimension(80, 30));
        jBtn_Cancel.setPreferredSize(new java.awt.Dimension(80, 30));
        jBtn_Cancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBtn_CancelActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 598, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jBtn_Confirm, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jBtn_Cancel, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 194, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jBtn_Confirm, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jBtn_Cancel, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jBtn_ConfirmActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBtn_ConfirmActionPerformed
        // TODO add your handling code here:
        mResult = true;
        setVisible(false);
    }//GEN-LAST:event_jBtn_ConfirmActionPerformed

    private void jBtn_CancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBtn_CancelActionPerformed
        // TODO add your handling code here:
        mResult = false;
        setVisible(false);
    }//GEN-LAST:event_jBtn_CancelActionPerformed

    private void formComponentShown(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_formComponentShown
        // TODO add your handling code here:
        mResult = false;
    }//GEN-LAST:event_formComponentShown

    private void jBtn_ConfirmKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jBtn_ConfirmKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            mResult = true;
            setVisible(false);
        }
    }//GEN-LAST:event_jBtn_ConfirmKeyPressed
    //中央配置
    public void setCenter() {
        this.setLocationRelativeTo(null);
    }

    //メッセージセット
    public void setMessage(String msg, String btn_name, String btn_name2) {
        jTextArea1.setText(msg);
        jBtn_Confirm.setText(btn_name);
        jBtn_Cancel.setText(btn_name2);
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jBtn_Cancel;
    private javax.swing.JButton jBtn_Confirm;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTextArea jTextArea1;
    // End of variables declaration//GEN-END:variables

}
