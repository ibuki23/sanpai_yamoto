package aks.industrialwaste.recycle.ui.payment;

import aks.industrialwaste.database.DBManager;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Window;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import jp.co.astax.awt.FontUtility;
import jp.co.astax.swing.DateTimeField;

public class PaymentDetailsListInput extends javax.swing.JPanel {

    private DateTimeField termDateField_;
    private PaymentDetailsListTableModel model;
    private Connection con;
    private int row;
    private Map<String, Object> columns_;
    public String[][] pclassName2Array = new String[2][];
    //選択されている行の各idを取得する
    public int p_class_id_ = 0;
    //取得したidを要素とする配列のindexを取得する(表示のため)
    public int p_class_id_index = 0;
    private DecimalFormat df;

    public PaymentDetailsListInput(Connection con_, PaymentDetailsListTableModel model_, int row_) {
        initComponents();
        Font font14 = FontUtility.getFont("メイリオ", Font.PLAIN, 14);
        con = con_;
        model = model_;
        row = row_;
        columns_ = new HashMap<>();
        df = new DecimalFormat("#,##0");

        //陸中建設では不要のため適用欄は一つとする
        remark02_.setVisible(false);

        copyColumns();

        p_class_id_ = (Integer) columns_.get("p_class_id");

        termDateField_ = new DateTimeField(DateTimeField.DateFormat.YMD);
        termDateField_.setFont(font14);
        termDateField_.setMinimumSize(new Dimension(250, 25));
        termDateField_.setPreferredSize(new Dimension(250, 25));
        calendar_p1.add(termDateField_);

        //マスタ問い合わせ
        //請求分類
        pclassName2Array = GetMasterData("v0_m_p_class", pclassName2Array);
        String[] pclassName = new String[pclassName2Array[0].length];
        for (int i = 0; i <= pclassName2Array[0].length - 1; i++) {
            pclassName[i] = (String) pclassName2Array[1][i].toString();
            if (p_class_id_ == Integer.valueOf((String) pclassName2Array[0][i])) {
                p_class_id_index = i;
            }
        }
        p_class_cmb_.setModel(new javax.swing.DefaultComboBoxModel(pclassName));

        clearView();
        copyColumns();
        updateView();

        payment_price_.requestFocus();
    }

    private void copyColumns() {
        columns_.clear();

        if (row >= 0) {
            columns_.putAll(model.getColumns(row));
        } else {

            columns_.put("bill_id", 0);
            columns_.put("p_class_id", 0);

            columns_.put("payment_date", null);
            columns_.put("payment_price", 0);
            columns_.put("commission", 0);

            columns_.put("remark01", "");
            columns_.put("remark02", "");
        }
    }

    private void clearView() {

        bill_id_.setText("0");
        m_bill_name_.setText("");
        termDateField_.setValue(null);
        payment_price_.setText("0");
        commission_.setText("0");

        remark01_.setText("");
        remark02_.setText("");

    }

    private void updateView() {

        Object o = columns_.get("payment_date");
        Calendar c = Calendar.getInstance();
        if (o instanceof java.sql.Date) {
            c.setTime((java.sql.Date) o);
            termDateField_.setValue(c);
            columns_.put("payment_date", termDateField_.getValue());
        } else if (o instanceof Calendar) {
            termDateField_.setValue((Calendar) o);
            columns_.put("payment_date", (Calendar) o);
        } else {
            termDateField_.setValue(null);
            columns_.put("payment_date", termDateField_.getValue());
        }

        p_class_cmb_.setSelectedIndex(p_class_id_index);

        payment_price_.setText(df.format((Integer) columns_.get("payment_price")));
        commission_.setText(df.format((Integer) columns_.get("commission")));

        remark01_.setText((String) columns_.get("remark01"));
        remark02_.setText((String) columns_.get("remark02"));

        bill_id_.setText(String.valueOf((Integer) columns_.get("bill_id")));
        m_bill_name_.setText((String) columns_.get("m_bill_name"));

    }

    private String[][] GetMasterData(String table_name, String[][] ms) {
        String sql = "";

        try {
            int i = 0;
            int cnt = 0;
            String s = String.format("select count(*) as cnt from %s where delete_flag=false", table_name);
            PreparedStatement ps = con.prepareStatement(s);

            try {
                ResultSet rs0 = ps.executeQuery();

                if (rs0.next()) {
                    //マスタの登録件数を返す
                    cnt = rs0.getInt("cnt");
                }

            } finally {
                ps.close();
            }
            ms[0] = new String[cnt];//id
            ms[1] = new String[cnt];//name
            sql = String.format("select id, name from %s where delete_flag=false order by id;",
                     table_name);
            ps = con.prepareStatement(sql);

            try {
                ResultSet rs = ps.executeQuery();

                while (rs.next()) {
                    ms[0][i] = String.valueOf((Integer) rs.getInt("id"));
                    ms[1][i] = rs.getString("name");
                    i++;
                }

            } finally {
                ps.close();
            }

        } catch (Exception e) {
            System.out.println("SQLCommon-GetMasterData-exp");
        }

        return ms;
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        btnRegist_ = new javax.swing.JButton();
        btnCancel_ = new javax.swing.JButton();
        btnClose_ = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        jLabel8 = new javax.swing.JLabel();
        payment_price_ = new aks.swing.JTextFieldHankaku();
        remark01_ = new aks.swing.JTextField();
        jLabel12 = new javax.swing.JLabel();
        remark02_ = new aks.swing.JTextField();
        p_class_cmb_ = new javax.swing.JComboBox();
        jLabel14 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        calendar_p1 = new javax.swing.JPanel();
        commission_ = new aks.swing.JTextFieldHankaku();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jPanel5 = new javax.swing.JPanel();
        bill_id_ = new aks.swing.JTextFieldHankaku();
        m_bill_name_ = new javax.swing.JTextField();
        jPanel6 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();

        setMaximumSize(new java.awt.Dimension(500, 350));
        setMinimumSize(new java.awt.Dimension(500, 350));
        setPreferredSize(new java.awt.Dimension(500, 350));
        setLayout(new java.awt.GridBagLayout());

        jPanel1.setMaximumSize(new java.awt.Dimension(600, 400));
        jPanel1.setMinimumSize(new java.awt.Dimension(600, 400));
        jPanel1.setPreferredSize(new java.awt.Dimension(600, 400));
        jPanel1.setLayout(new java.awt.GridBagLayout());

        jPanel2.setMaximumSize(new java.awt.Dimension(600, 70));
        jPanel2.setMinimumSize(new java.awt.Dimension(600, 50));
        jPanel2.setPreferredSize(new java.awt.Dimension(600, 50));
        jPanel2.setLayout(new java.awt.GridBagLayout());

        jPanel4.setMinimumSize(new java.awt.Dimension(440, 50));
        jPanel4.setPreferredSize(new java.awt.Dimension(125, 50));
        jPanel4.setLayout(new java.awt.GridBagLayout());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        jPanel2.add(jPanel4, gridBagConstraints);

        btnRegist_.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        btnRegist_.setText("登録");
        btnRegist_.setMaximumSize(new java.awt.Dimension(120, 30));
        btnRegist_.setMinimumSize(new java.awt.Dimension(120, 30));
        btnRegist_.setPreferredSize(new java.awt.Dimension(120, 30));
        btnRegist_.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRegist_ActionPerformed(evt);
            }
        });
        btnRegist_.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                btnRegist_KeyPressed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(0, 2, 0, 2);
        jPanel2.add(btnRegist_, gridBagConstraints);

        btnCancel_.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        btnCancel_.setText("キャンセル");
        btnCancel_.setMaximumSize(new java.awt.Dimension(120, 30));
        btnCancel_.setMinimumSize(new java.awt.Dimension(120, 30));
        btnCancel_.setPreferredSize(new java.awt.Dimension(120, 30));
        btnCancel_.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancel_ActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(0, 2, 0, 2);
        jPanel2.add(btnCancel_, gridBagConstraints);

        btnClose_.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        btnClose_.setText("閉じる");
        btnClose_.setMaximumSize(new java.awt.Dimension(120, 30));
        btnClose_.setMinimumSize(new java.awt.Dimension(120, 30));
        btnClose_.setPreferredSize(new java.awt.Dimension(120, 30));
        btnClose_.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnClose_ActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(0, 2, 0, 15);
        jPanel2.add(btnClose_, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        jPanel1.add(jPanel2, gridBagConstraints);

        jPanel3.setMinimumSize(new java.awt.Dimension(600, 300));
        jPanel3.setPreferredSize(new java.awt.Dimension(600, 300));
        jPanel3.setLayout(new java.awt.GridBagLayout());

        jLabel8.setBackground(new java.awt.Color(0, 51, 153));
        jLabel8.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(255, 255, 255));
        jLabel8.setText("　取引区分");
        jLabel8.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        jLabel8.setMaximumSize(new java.awt.Dimension(100, 25));
        jLabel8.setMinimumSize(new java.awt.Dimension(100, 25));
        jLabel8.setOpaque(true);
        jLabel8.setPreferredSize(new java.awt.Dimension(100, 25));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        jPanel3.add(jLabel8, gridBagConstraints);

        payment_price_.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        payment_price_.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        payment_price_.setText("00000");
        payment_price_.setMaximumSize(new java.awt.Dimension(60, 30));
        payment_price_.setMinimumSize(new java.awt.Dimension(120, 30));
        payment_price_.setPreferredSize(new java.awt.Dimension(120, 30));
        payment_price_.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                payment_price_FocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                payment_price_FocusLost(evt);
            }
        });
        payment_price_.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                payment_price_KeyPressed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        jPanel3.add(payment_price_, gridBagConstraints);

        remark01_.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        remark01_.setMinimumSize(new java.awt.Dimension(150, 30));
        remark01_.setPreferredSize(new java.awt.Dimension(150, 30));
        remark01_.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                remark01_FocusGained(evt);
            }
        });
        remark01_.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                remark01_KeyPressed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 50);
        jPanel3.add(remark01_, gridBagConstraints);

        jLabel12.setBackground(new java.awt.Color(0, 51, 153));
        jLabel12.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        jLabel12.setForeground(new java.awt.Color(255, 255, 255));
        jLabel12.setText("　備考");
        jLabel12.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        jLabel12.setMaximumSize(new java.awt.Dimension(100, 25));
        jLabel12.setMinimumSize(new java.awt.Dimension(100, 25));
        jLabel12.setOpaque(true);
        jLabel12.setPreferredSize(new java.awt.Dimension(100, 25));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        jPanel3.add(jLabel12, gridBagConstraints);

        remark02_.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        remark02_.setMinimumSize(new java.awt.Dimension(120, 30));
        remark02_.setPreferredSize(new java.awt.Dimension(120, 30));
        remark02_.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                remark02_FocusGained(evt);
            }
        });
        remark02_.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                remark02_KeyPressed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 50);
        jPanel3.add(remark02_, gridBagConstraints);

        p_class_cmb_.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        p_class_cmb_.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        p_class_cmb_.setMinimumSize(new java.awt.Dimension(120, 30));
        p_class_cmb_.setPreferredSize(new java.awt.Dimension(120, 30));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        jPanel3.add(p_class_cmb_, gridBagConstraints);

        jLabel14.setBackground(new java.awt.Color(0, 51, 153));
        jLabel14.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        jLabel14.setForeground(new java.awt.Color(255, 255, 255));
        jLabel14.setText("　得意先名");
        jLabel14.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        jLabel14.setMaximumSize(new java.awt.Dimension(100, 25));
        jLabel14.setMinimumSize(new java.awt.Dimension(100, 25));
        jLabel14.setOpaque(true);
        jLabel14.setPreferredSize(new java.awt.Dimension(100, 25));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        jPanel3.add(jLabel14, gridBagConstraints);

        jLabel9.setBackground(new java.awt.Color(0, 51, 153));
        jLabel9.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(255, 255, 255));
        jLabel9.setText("　入金日");
        jLabel9.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        jLabel9.setMaximumSize(new java.awt.Dimension(100, 25));
        jLabel9.setMinimumSize(new java.awt.Dimension(100, 25));
        jLabel9.setOpaque(true);
        jLabel9.setPreferredSize(new java.awt.Dimension(100, 25));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        jPanel3.add(jLabel9, gridBagConstraints);

        calendar_p1.setMinimumSize(new java.awt.Dimension(200, 35));
        calendar_p1.setPreferredSize(new java.awt.Dimension(200, 35));
        calendar_p1.setLayout(new java.awt.GridLayout(1, 0));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 50);
        jPanel3.add(calendar_p1, gridBagConstraints);

        commission_.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        commission_.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        commission_.setText("00000");
        commission_.setMaximumSize(new java.awt.Dimension(60, 30));
        commission_.setMinimumSize(new java.awt.Dimension(120, 30));
        commission_.setPreferredSize(new java.awt.Dimension(120, 30));
        commission_.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                commission_FocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                commission_FocusLost(evt);
            }
        });
        commission_.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                commission_KeyPressed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        jPanel3.add(commission_, gridBagConstraints);

        jLabel10.setBackground(new java.awt.Color(0, 51, 153));
        jLabel10.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        jLabel10.setForeground(new java.awt.Color(255, 255, 255));
        jLabel10.setText("　手数料等");
        jLabel10.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        jLabel10.setMaximumSize(new java.awt.Dimension(100, 25));
        jLabel10.setMinimumSize(new java.awt.Dimension(100, 25));
        jLabel10.setOpaque(true);
        jLabel10.setPreferredSize(new java.awt.Dimension(100, 25));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        jPanel3.add(jLabel10, gridBagConstraints);

        jLabel11.setBackground(new java.awt.Color(0, 51, 153));
        jLabel11.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        jLabel11.setForeground(new java.awt.Color(255, 255, 255));
        jLabel11.setText("　入金額");
        jLabel11.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        jLabel11.setMaximumSize(new java.awt.Dimension(100, 25));
        jLabel11.setMinimumSize(new java.awt.Dimension(100, 25));
        jLabel11.setOpaque(true);
        jLabel11.setPreferredSize(new java.awt.Dimension(100, 25));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        jPanel3.add(jLabel11, gridBagConstraints);

        jPanel5.setMinimumSize(new java.awt.Dimension(100, 10));
        jPanel5.setLayout(new java.awt.GridBagLayout());

        bill_id_.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        bill_id_.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        bill_id_.setText("00000");
        bill_id_.setMaximumSize(new java.awt.Dimension(60, 30));
        bill_id_.setMinimumSize(new java.awt.Dimension(60, 30));
        bill_id_.setPreferredSize(new java.awt.Dimension(60, 30));
        bill_id_.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                bill_id_FocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                bill_id_FocusLost(evt);
            }
        });
        bill_id_.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                bill_id_KeyPressed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        jPanel5.add(bill_id_, gridBagConstraints);

        m_bill_name_.setEditable(false);
        m_bill_name_.setBackground(new java.awt.Color(204, 204, 255));
        m_bill_name_.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        m_bill_name_.setFocusable(false);
        m_bill_name_.setMinimumSize(new java.awt.Dimension(250, 30));
        m_bill_name_.setPreferredSize(new java.awt.Dimension(250, 30));
        m_bill_name_.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                m_bill_name_FocusGained(evt);
            }
        });
        m_bill_name_.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                m_bill_name_KeyPressed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        jPanel5.add(m_bill_name_, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 50);
        jPanel3.add(jPanel5, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridheight = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        jPanel3.add(jPanel6, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weighty = 1.0;
        jPanel1.add(jPanel3, gridBagConstraints);

        jLabel1.setBackground(new java.awt.Color(0, 0, 204));
        jLabel1.setFont(new java.awt.Font("SansSerif", 0, 24)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("入金データ入力");
        jLabel1.setMaximumSize(new java.awt.Dimension(5000, 35));
        jLabel1.setMinimumSize(new java.awt.Dimension(600, 35));
        jLabel1.setOpaque(true);
        jLabel1.setPreferredSize(new java.awt.Dimension(600, 35));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        jPanel1.add(jLabel1, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        add(jPanel1, gridBagConstraints);
    }// </editor-fold>//GEN-END:initComponents

    private void btnClose_ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnClose_ActionPerformed
        //閉じるボタン
        doClose();
    }//GEN-LAST:event_btnClose_ActionPerformed

    private void btnCancel_ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancel_ActionPerformed
        // キャンセルボタン
        updateView();
    }//GEN-LAST:event_btnCancel_ActionPerformed

    private void btnRegist_ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRegist_ActionPerformed
        //登録ボタン
        doRegist();
    }//GEN-LAST:event_btnRegist_ActionPerformed

    private void payment_price_KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_payment_price_KeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            payment_price_.transferFocus();
            evt.consume();
            commission_.requestFocus();
        }
    }//GEN-LAST:event_payment_price_KeyPressed

    private void payment_price_FocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_payment_price_FocusLost
        // TODO add your handling code here:
    }//GEN-LAST:event_payment_price_FocusLost

    private void remark01_KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_remark01_KeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            remark01_.transferFocus();
            evt.consume();
        }
    }//GEN-LAST:event_remark01_KeyPressed

    private void payment_price_FocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_payment_price_FocusGained
        // TODO add your handling code here:
        payment_price_.selectAll();
    }//GEN-LAST:event_payment_price_FocusGained

    private void remark02_KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_remark02_KeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            remark02_.transferFocus();
            evt.consume();
            btnRegist_.requestFocus();
        }
    }//GEN-LAST:event_remark02_KeyPressed

    private void remark01_FocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_remark01_FocusGained
        // TODO add your handling code here:
        remark01_.selectAll();
    }//GEN-LAST:event_remark01_FocusGained

    private void remark02_FocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_remark02_FocusGained
        // TODO add your handling code here:
        remark02_.selectAll();
    }//GEN-LAST:event_remark02_FocusGained

    private void btnRegist_KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_btnRegist_KeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            //登録ボタン
            doRegist();
        }
    }//GEN-LAST:event_btnRegist_KeyPressed

    private void m_bill_name_FocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_m_bill_name_FocusGained
        // TODO add your handling code here:
        m_bill_name_.selectAll();
    }//GEN-LAST:event_m_bill_name_FocusGained

    private void m_bill_name_KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_m_bill_name_KeyPressed

    }//GEN-LAST:event_m_bill_name_KeyPressed

    private void bill_id_FocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_bill_id_FocusGained
        // TODO add your handling code here:
        bill_id_.selectAll();
    }//GEN-LAST:event_bill_id_FocusGained

    private void bill_id_FocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_bill_id_FocusLost
        // TODO add your handling code here:
        try {
            int id = Integer.valueOf(bill_id_.getText());

            if (id >= 0) {
                m_bill_name_.setText(getBillName(id));
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(
                     PaymentDetailsListInput.this,
                     "コードは数字のみ入力出来ます。",
                     "確認",
                     JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_bill_id_FocusLost

    private void bill_id_KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_bill_id_KeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            bill_id_.transferFocus();
            evt.consume();
            payment_price_.requestFocus();
        }
    }//GEN-LAST:event_bill_id_KeyPressed

    private void commission_FocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_commission_FocusGained
        // TODO add your handling code here:
        commission_.selectAll();
    }//GEN-LAST:event_commission_FocusGained

    private void commission_FocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_commission_FocusLost
        // TODO add your handling code here:
    }//GEN-LAST:event_commission_FocusLost

    private void commission_KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_commission_KeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            payment_price_.transferFocus();
            evt.consume();
            remark01_.requestFocus();
        }
    }//GEN-LAST:event_commission_KeyPressed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField bill_id_;
    private javax.swing.JButton btnCancel_;
    private javax.swing.JButton btnClose_;
    private javax.swing.JButton btnRegist_;
    private javax.swing.JPanel calendar_p1;
    private javax.swing.JTextField commission_;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JTextField m_bill_name_;
    private javax.swing.JComboBox p_class_cmb_;
    private javax.swing.JTextField payment_price_;
    private javax.swing.JTextField remark01_;
    private javax.swing.JTextField remark02_;
    // End of variables declaration//GEN-END:variables

    private void doClose() {
        Window w = SwingUtilities.getWindowAncestor(this);
        if (w instanceof Frame) {
            w.dispatchEvent(new WindowEvent(w, WindowEvent.WINDOW_CLOSING));
        } else {
            w.dispose();
        }
    }

    private void updateColumns() {
        if (termDateField_.getValue() != null) {
            columns_.put("payment_date", DBManager.convert(((Calendar) termDateField_.getValue()).getTime()));
        }

        try {
            int i = Integer.valueOf(bill_id_.getText());
            columns_.put("bill_id", i);
        } catch (Exception e) {

        }

        try {
            int d = Integer.valueOf(payment_price_.getText());
            columns_.put("payment_price", d);
        } catch (Exception e) {

        }

        try {
            int d = Integer.valueOf(commission_.getText());
            columns_.put("commission", d);
        } catch (Exception e) {

        }

        columns_.put("remark01", remark01_.getText());
        columns_.put("remark02", remark02_.getText());

        columns_.put("p_class_id", Integer.valueOf(pclassName2Array[0][p_class_cmb_.getSelectedIndex()]));

    }

    private void doRegist() {

        // 画面の変更内容をカラムに反映
        updateColumns();

        if (row < 0) {
            if (model.insert(columns_)) {
                //少し待つ
                try {
                    Thread.sleep(100);
                } catch (Exception e) {
                }

                JOptionPane.showMessageDialog(
                         PaymentDetailsListInput.this,
                         "登録しました。",
                         "確認",
                         JOptionPane.INFORMATION_MESSAGE);

                doClose();

            } else {
                //少し待つ
                try {
                    Thread.sleep(100);
                } catch (Exception e) {
                }

                JOptionPane.showMessageDialog(
                         PaymentDetailsListInput.this,
                         "登録に失敗しました。",
                         "確認",
                         JOptionPane.ERROR_MESSAGE);
            }

        } else {
            if (model.update(columns_)) {
                //少し待つ
                try {
                    Thread.sleep(100);
                } catch (Exception e) {
                }

                JOptionPane.showMessageDialog(
                         PaymentDetailsListInput.this,
                         "登録しました。",
                         "確認",
                         JOptionPane.INFORMATION_MESSAGE);

                doClose();

            } else {
                //少し待つ
                try {
                    Thread.sleep(100);
                } catch (Exception e) {
                }
                JOptionPane.showMessageDialog(
                         PaymentDetailsListInput.this,
                         "登録に失敗しました。",
                         "確認",
                         JOptionPane.ERROR_MESSAGE);
            }
        }

    }

    private String getBillName(int id) {
        String goods_name = "";
        //品名検索
        try {

            String s = "select * from v0_bill "
                     + " where v0_bill.id = ?;";

            PreparedStatement ps = con.prepareStatement(s);
            try {
                ps.setObject(1, id);
                ResultSet rs = ps.executeQuery();

                if (rs.next()) {
                    goods_name = rs.getString("name");
                }

            } finally {
                ps.close();
            }
        } catch (SQLException e) {
        }
        return goods_name;
    }
}
