package aks.industrialwaste.recycle.ui.master.goods;

import java.awt.Color;
import java.awt.event.KeyEvent;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import javax.swing.JScrollBar;

public class GoodsZaikoDetailPane2 extends javax.swing.JPanel {

    private SimpleDateFormat sdf;
    private Date term_date_;

    private Object[] o_;

    public GoodsZaikoDetailPane2(Date term_date, Object[] o) {
        initComponents();

        sdf = new SimpleDateFormat("dd");
        term_date_ = term_date;
        o_ = o;
        updateView(o_);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        v0 = new aks.swing.JTextFieldHankaku1();
        v1 = new javax.swing.JCheckBox();
        v2 = new aks.swing.JTextFieldHankaku1();
        v3 = new aks.swing.JTextFieldKeiryo();
        v4 = new aks.swing.JTextFieldHankaku1();
        v5 = new aks.swing.JTextFieldHankaku1();
        v6 = new aks.swing.JTextFieldHankaku1();
        v7 = new aks.swing.JTextFieldHankaku1();
        v8 = new aks.swing.JTextFieldHankaku1();
        v9 = new aks.swing.JTextFieldHankaku1();
        v10 = new aks.swing.JTextFieldHankaku1();
        v11 = new aks.swing.JTextFieldHankaku1();
        v12 = new javax.swing.JLabel();

        setFocusable(false);
        setMaximumSize(new java.awt.Dimension(1100, 25));
        setMinimumSize(new java.awt.Dimension(1100, 25));
        setPreferredSize(new java.awt.Dimension(1100, 25));
        setLayout(new javax.swing.BoxLayout(this, javax.swing.BoxLayout.LINE_AXIS));

        v0.setEditable(false);
        v0.setBackground(new java.awt.Color(220, 220, 220));
        v0.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        v0.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        v0.setText("31");
        v0.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        v0.setFocusable(false);
        v0.setMaximumSize(new java.awt.Dimension(30, 25));
        v0.setMinimumSize(new java.awt.Dimension(30, 25));
        v0.setPreferredSize(new java.awt.Dimension(30, 25));
        add(v0);

        v1.setFocusable(false);
        v1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        v1.setMargin(new java.awt.Insets(0, 0, 0, 0));
        v1.setMaximumSize(new java.awt.Dimension(27, 21));
        v1.setMinimumSize(new java.awt.Dimension(27, 21));
        v1.setPreferredSize(new java.awt.Dimension(27, 21));
        v1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                v1KeyReleased(evt);
            }
        });
        add(v1);

        v2.setEditable(false);
        v2.setBackground(new java.awt.Color(220, 220, 220));
        v2.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        v2.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        v2.setText("999");
        v2.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        v2.setFocusable(false);
        v2.setMaximumSize(new java.awt.Dimension(80, 25));
        v2.setMinimumSize(new java.awt.Dimension(80, 25));
        v2.setPreferredSize(new java.awt.Dimension(80, 25));
        add(v2);

        v3.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        v3.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        v3.setText("999");
        v3.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED, new java.awt.Color(0, 51, 153), null));
        v3.setMaximumSize(new java.awt.Dimension(100, 25));
        v3.setMinimumSize(new java.awt.Dimension(100, 25));
        v3.setPreferredSize(new java.awt.Dimension(100, 25));
        v3.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                v3KeyPressed(evt);
            }
        });
        add(v3);

        v4.setEditable(false);
        v4.setBackground(new java.awt.Color(220, 220, 220));
        v4.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        v4.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        v4.setText("999");
        v4.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        v4.setFocusable(false);
        v4.setMaximumSize(new java.awt.Dimension(100, 25));
        v4.setMinimumSize(new java.awt.Dimension(100, 25));
        v4.setPreferredSize(new java.awt.Dimension(100, 25));
        add(v4);

        v5.setEditable(false);
        v5.setBackground(new java.awt.Color(220, 220, 220));
        v5.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        v5.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        v5.setText("999");
        v5.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        v5.setFocusable(false);
        v5.setMaximumSize(new java.awt.Dimension(80, 25));
        v5.setMinimumSize(new java.awt.Dimension(80, 25));
        v5.setPreferredSize(new java.awt.Dimension(80, 25));
        add(v5);

        v6.setEditable(false);
        v6.setBackground(new java.awt.Color(220, 220, 220));
        v6.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        v6.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        v6.setText("999");
        v6.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        v6.setFocusable(false);
        v6.setMaximumSize(new java.awt.Dimension(100, 25));
        v6.setMinimumSize(new java.awt.Dimension(100, 25));
        v6.setPreferredSize(new java.awt.Dimension(100, 25));
        add(v6);

        v7.setEditable(false);
        v7.setBackground(new java.awt.Color(220, 220, 220));
        v7.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        v7.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        v7.setText("999");
        v7.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        v7.setFocusable(false);
        v7.setMaximumSize(new java.awt.Dimension(104, 25));
        v7.setMinimumSize(new java.awt.Dimension(104, 25));
        v7.setPreferredSize(new java.awt.Dimension(104, 25));
        add(v7);

        v8.setEditable(false);
        v8.setBackground(new java.awt.Color(204, 255, 204));
        v8.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        v8.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        v8.setText("999");
        v8.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        v8.setFocusable(false);
        v8.setMaximumSize(new java.awt.Dimension(100, 25));
        v8.setMinimumSize(new java.awt.Dimension(100, 25));
        v8.setPreferredSize(new java.awt.Dimension(100, 25));
        add(v8);

        v9.setEditable(false);
        v9.setBackground(new java.awt.Color(204, 255, 204));
        v9.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        v9.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        v9.setText("999");
        v9.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        v9.setFocusable(false);
        v9.setMaximumSize(new java.awt.Dimension(100, 25));
        v9.setMinimumSize(new java.awt.Dimension(100, 25));
        v9.setPreferredSize(new java.awt.Dimension(100, 25));
        add(v9);

        v10.setEditable(false);
        v10.setBackground(new java.awt.Color(204, 255, 204));
        v10.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        v10.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        v10.setText("999");
        v10.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        v10.setFocusable(false);
        v10.setMaximumSize(new java.awt.Dimension(80, 25));
        v10.setMinimumSize(new java.awt.Dimension(80, 25));
        v10.setPreferredSize(new java.awt.Dimension(80, 25));
        add(v10);

        v11.setEditable(false);
        v11.setBackground(new java.awt.Color(204, 255, 204));
        v11.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        v11.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        v11.setText("+99");
        v11.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        v11.setFocusable(false);
        v11.setMaximumSize(new java.awt.Dimension(100, 25));
        v11.setMinimumSize(new java.awt.Dimension(100, 25));
        v11.setPreferredSize(new java.awt.Dimension(100, 25));
        add(v11);

        v12.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aks/autoanalysis/recycle/ui/master/goods/RedLampButton50.png"))); // NOI18N
        v12.setFocusable(false);
        v12.setMaximumSize(new java.awt.Dimension(120, 25));
        v12.setMinimumSize(new java.awt.Dimension(120, 25));
        v12.setPreferredSize(new java.awt.Dimension(120, 25));
        add(v12);
    }// </editor-fold>//GEN-END:initComponents

    private void v1KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_v1KeyReleased

    }//GEN-LAST:event_v1KeyReleased

    private void v3KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_v3KeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {

            v3.transferFocus();
            evt.consume();

            //スクロールバー取得
            //エンターキーにてスクロールする
            JScrollBar vBar = GoodsZaikoDetailPane0.dScroll.getVerticalScrollBar();
            int vBarSize = vBar.getValue();
            int vBarSizeDay = Integer.valueOf(sdf.format((Date) o_[0])) * 25;
            vBar.setValue(vBarSize > vBarSizeDay ? vBarSizeDay : vBarSize + 25);

        }
    }//GEN-LAST:event_v3KeyPressed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField v0;
    private javax.swing.JCheckBox v1;
    private javax.swing.JTextField v10;
    private javax.swing.JTextField v11;
    private javax.swing.JLabel v12;
    private javax.swing.JTextField v2;
    private javax.swing.JTextField v3;
    private javax.swing.JTextField v4;
    private javax.swing.JTextField v5;
    private javax.swing.JTextField v6;
    private javax.swing.JTextField v7;
    private javax.swing.JTextField v8;
    private javax.swing.JTextField v9;
    // End of variables declaration//GEN-END:variables

    public void updateView(Object[] o) {

        v0.setText(sdf.format((Date) o[0]));
        v1.setSelected((Boolean) o[3]);
        v2.setText(o[4].toString());
        v3.setText(o[5].toString());
        v4.setText(o[6].toString());
        v5.setText(o[7].toString());
        v6.setText(o[8].toString());
        v7.setText(o[9].toString());
        v8.setText(o[10].toString());
        v9.setText(o[11].toString());
        v10.setText(o[12].toString());
        v11.setText(o[13].toString());
        v12.setEnabled((Boolean) o[14]);
        //当日以降は赤文字表示
        v6.setForeground(Integer.valueOf(sdf.format(term_date_)) == Integer.valueOf(sdf.format((Date) o[0])) ? Color.BLUE : Integer.valueOf(sdf.format(term_date_)) <= Integer.valueOf(sdf.format((Date) o[0])) ? Color.RED : Color.BLACK);
        v8.setForeground(Integer.valueOf(sdf.format(term_date_)) == Integer.valueOf(sdf.format((Date) o[0])) ? Color.BLUE : Integer.valueOf(sdf.format(term_date_)) <= Integer.valueOf(sdf.format((Date) o[0])) ? Color.RED : Color.BLACK);
        v9.setForeground(Integer.valueOf(sdf.format(term_date_)) == Integer.valueOf(sdf.format((Date) o[0])) ? Color.BLUE : Integer.valueOf(sdf.format(term_date_)) <= Integer.valueOf(sdf.format((Date) o[0])) ? Color.RED : Color.BLACK);
        v10.setForeground(Integer.valueOf(sdf.format(term_date_)) == Integer.valueOf(sdf.format((Date) o[0])) ? Color.BLUE : Integer.valueOf(sdf.format(term_date_)) <= Integer.valueOf(sdf.format((Date) o[0])) ? Color.RED : Color.BLACK);
        v11.setForeground(Integer.valueOf(sdf.format(term_date_)) == Integer.valueOf(sdf.format((Date) o[0])) ? Color.BLUE : Integer.valueOf(sdf.format(term_date_)) <= Integer.valueOf(sdf.format((Date) o[0])) ? Color.RED : Color.BLACK);
    }

    public void update(Date d, int goods_id) {

        try {
            Statement st = GoodsZaikoDetailPane0.con_.createStatement();
            st.executeUpdate("begin;");
            try {
                String s = new StringBuilder()
                         .append("update zaiko.d_result set ")
                         .append("処理予定数量 = ?, 搬入作業有f = ? ")
                         .append("where 所属日 = ? and 商品コード = ?;")
                         .toString();

                PreparedStatement ps = GoodsZaikoDetailPane0.con_.prepareStatement(s);
                try {

                    ps.setObject(1, Double.valueOf(v3.getText()));
                    ps.setObject(2, v1.isSelected());
                    ps.setObject(3, (Date) o_[0]);
                    ps.setObject(4, goods_id);

                    ps.execute();

                } finally {
                    ps.close();
                }

                st.executeUpdate("commit;");

            } catch (SQLException e) {
                st.executeUpdate("rollback;");
                throw e;
            } finally {
                st.close();
            }

        } catch (Exception e) {

        }

    }
}
