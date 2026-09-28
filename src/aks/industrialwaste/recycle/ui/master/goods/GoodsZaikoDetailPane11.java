package aks.industrialwaste.recycle.ui.master.goods;

import java.awt.Color;
import java.awt.event.KeyEvent;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Map;
import javax.swing.JScrollBar;

public class GoodsZaikoDetailPane11 extends javax.swing.JPanel {

    public int h_row;

    public Map colums;

    public GoodsZaikoDetailPane11(int index, Map o) {
        initComponents();
        h_row = index;
        colums = o;
        updateView(colums);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        v得意先名 = new javax.swing.JTextField();
        v月搬入予定量 = new aks.swing.JTextFieldKeiryo();
        v当日分 = new javax.swing.JTextField();
        v月搬入実績 = new javax.swing.JTextField();
        v達成率 = new javax.swing.JTextField();
        v標準偏差 = new javax.swing.JTextField();
        jPanel1 = new javax.swing.JPanel();

        setMaximumSize(new java.awt.Dimension(675, 25));
        setMinimumSize(new java.awt.Dimension(675, 25));
        setName(""); // NOI18N
        setPreferredSize(new java.awt.Dimension(675, 25));
        setLayout(new javax.swing.BoxLayout(this, javax.swing.BoxLayout.LINE_AXIS));

        v得意先名.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        v得意先名.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        v得意先名.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        v得意先名.setEnabled(false);
        v得意先名.setFocusable(false);
        v得意先名.setMaximumSize(new java.awt.Dimension(200, 25));
        v得意先名.setMinimumSize(new java.awt.Dimension(200, 25));
        v得意先名.setPreferredSize(new java.awt.Dimension(200, 25));
        v得意先名.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                v得意先名FocusGained(evt);
            }
        });
        add(v得意先名);

        v月搬入予定量.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        v月搬入予定量.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        v月搬入予定量.setText("0");
        v月搬入予定量.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED, new java.awt.Color(0, 51, 153), null));
        v月搬入予定量.setMaximumSize(new java.awt.Dimension(102, 25));
        v月搬入予定量.setMinimumSize(new java.awt.Dimension(102, 25));
        v月搬入予定量.setPreferredSize(new java.awt.Dimension(102, 25));
        v月搬入予定量.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                v月搬入予定量FocusGained(evt);
            }
        });
        v月搬入予定量.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                v月搬入予定量KeyPressed(evt);
            }
        });
        add(v月搬入予定量);

        v当日分.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        v当日分.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        v当日分.setText("0");
        v当日分.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        v当日分.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        v当日分.setEnabled(false);
        v当日分.setFocusable(false);
        v当日分.setMaximumSize(new java.awt.Dimension(80, 25));
        v当日分.setMinimumSize(new java.awt.Dimension(80, 25));
        v当日分.setPreferredSize(new java.awt.Dimension(80, 25));
        v当日分.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                v当日分FocusGained(evt);
            }
        });
        add(v当日分);

        v月搬入実績.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        v月搬入実績.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        v月搬入実績.setText("0");
        v月搬入実績.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        v月搬入実績.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        v月搬入実績.setEnabled(false);
        v月搬入実績.setFocusable(false);
        v月搬入実績.setMaximumSize(new java.awt.Dimension(100, 25));
        v月搬入実績.setMinimumSize(new java.awt.Dimension(100, 25));
        v月搬入実績.setPreferredSize(new java.awt.Dimension(100, 25));
        v月搬入実績.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                v月搬入実績FocusGained(evt);
            }
        });
        add(v月搬入実績);

        v達成率.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        v達成率.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        v達成率.setText("0");
        v達成率.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        v達成率.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        v達成率.setEnabled(false);
        v達成率.setFocusable(false);
        v達成率.setMaximumSize(new java.awt.Dimension(80, 25));
        v達成率.setMinimumSize(new java.awt.Dimension(80, 25));
        v達成率.setPreferredSize(new java.awt.Dimension(80, 25));
        v達成率.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                v達成率FocusGained(evt);
            }
        });
        add(v達成率);

        v標準偏差.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        v標準偏差.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        v標準偏差.setText("0");
        v標準偏差.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        v標準偏差.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        v標準偏差.setEnabled(false);
        v標準偏差.setFocusable(false);
        v標準偏差.setMaximumSize(new java.awt.Dimension(70, 25));
        v標準偏差.setMinimumSize(new java.awt.Dimension(70, 25));
        v標準偏差.setPreferredSize(new java.awt.Dimension(70, 25));
        v標準偏差.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                v標準偏差FocusGained(evt);
            }
        });
        add(v標準偏差);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 42, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 25, Short.MAX_VALUE)
        );

        add(jPanel1);
    }// </editor-fold>//GEN-END:initComponents

    private void v月搬入予定量FocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_v月搬入予定量FocusGained

        // TODO add your handling code here:
        GoodsZaikoDetailPane0.h_row = h_row;
    }//GEN-LAST:event_v月搬入予定量FocusGained

    private void v当日分FocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_v当日分FocusGained
        // TODO add your handling code here:
        GoodsZaikoDetailPane0.h_row = h_row;
    }//GEN-LAST:event_v当日分FocusGained

    private void v月搬入実績FocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_v月搬入実績FocusGained
        // TODO add your handling code here:
        GoodsZaikoDetailPane0.h_row = h_row;
    }//GEN-LAST:event_v月搬入実績FocusGained

    private void v達成率FocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_v達成率FocusGained
        // TODO add your handling code here:
        GoodsZaikoDetailPane0.h_row = h_row;
    }//GEN-LAST:event_v達成率FocusGained

    private void v標準偏差FocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_v標準偏差FocusGained
        // TODO add your handling code here:
        GoodsZaikoDetailPane0.h_row = h_row;
    }//GEN-LAST:event_v標準偏差FocusGained

    private void v得意先名FocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_v得意先名FocusGained
        // TODO add your handling code here:
        GoodsZaikoDetailPane0.h_row = h_row;
    }//GEN-LAST:event_v得意先名FocusGained

    private void v月搬入予定量KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_v月搬入予定量KeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            try {
                colums.put("月搬入予定数量", Double.valueOf(v月搬入予定量.getText().replaceAll(",", "")));
                v月搬入予定量.transferFocus();
                evt.consume();

                //スクロールバー取得
                //エンターキーにてスクロールする
                JScrollBar vBar = GoodsZaikoDetailPane0.hScroll.getVerticalScrollBar();
                int vBarSize = vBar.getValue();
                int vBarSizeDay = h_row * 25;
                vBar.setValue(vBarSize > vBarSizeDay ? vBarSizeDay : vBarSize + 25);

            } catch (NumberFormatException ne) {
                colums.put("月搬入予定数量", 0.);
            }
            updateView(colums);
        }
    }//GEN-LAST:event_v月搬入予定量KeyPressed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel jPanel1;
    private javax.swing.JTextField v当日分;
    private javax.swing.JTextField v得意先名;
    public javax.swing.JTextField v月搬入予定量;
    private javax.swing.JTextField v月搬入実績;
    private javax.swing.JTextField v標準偏差;
    private javax.swing.JTextField v達成率;
    // End of variables declaration//GEN-END:variables

    public void updateView(Map o) {

        if (o != null) {
            try {
                v得意先名.setText((String) o.get("業者名"));
                v月搬入予定量.setText(String.valueOf((Double) o.get("月搬入予定数量")));
                v当日分.setText(String.valueOf((Double) o.get("当日までの搬入予定")));
                v月搬入実績.setText(String.valueOf((Double) o.get("月搬入実績")));
                v達成率.setText(String.valueOf((Double) o.get("達成率")));
                v標準偏差.setText(String.valueOf((Double) o.get("標準偏差")));
            } catch (Exception e) {

            }

            GoodsZaikoDetailPane0.o1.set(h_row, o);
            GoodsZaikoDetailPane0.h小計計算();

            if ((Boolean) o.get("警報ランプf")) {
                v達成率.setDisabledTextColor(Color.RED);
            } else {
                v達成率.setDisabledTextColor(Color.BLACK);
            }

        }
    }

    public void update(Date d) {

        try {
            Statement st = GoodsZaikoDetailPane0.con_.createStatement();
            st.executeUpdate("begin;");
            try {
                String s = new StringBuilder()
                         .append("select zaiko.set_d_plan(?::date, ?::integer, ?::integer, ?::integer);")
                         .toString();

                PreparedStatement ps = GoodsZaikoDetailPane0.con_.prepareStatement(s);
                try {
                    ps.setObject(1, d);
                    ps.setObject(2, colums.get("業者コード"));
                    ps.setObject(3, colums.get("商品コード"));
                    ps.setObject(4, Double.valueOf(v月搬入予定量.getText()));

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

    public void delete(Date d) {

        try {
            Statement st = GoodsZaikoDetailPane0.con_.createStatement();
            st.executeUpdate("begin;");
            try {
                String s = new StringBuilder()
                         .append("delete from zaiko.d_plan where 所属月 = date_trunc('month'::text, ?::date) and 業者コード = ? and 商品コード = ?;")
                         .toString();

                PreparedStatement ps = GoodsZaikoDetailPane0.con_.prepareStatement(s);
                try {
                    ps.setObject(1, d);
                    ps.setObject(2, colums.get("業者コード"));
                    ps.setObject(3, colums.get("商品コード"));

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
