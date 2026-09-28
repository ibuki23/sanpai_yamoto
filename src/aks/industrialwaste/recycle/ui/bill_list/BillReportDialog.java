package aks.industrialwaste.recycle.ui.bill_list;

import aks.industrialwaste.recycle.report.XlsxBillReport;
import java.sql.Connection;
import java.text.SimpleDateFormat;
import javax.swing.*;
import java.awt.Window;
import java.awt.Frame;
import java.awt.event.KeyEvent;
import java.sql.Date;

public class BillReportDialog extends javax.swing.JPanel {

    private Connection con_;
    private Date term_month_, start_date_, end_date_, term_date_;
    private Integer bill_id_;
    private long bill_sno_;

    public BillReportDialog(Connection con, Date term_month, Integer bill_id, String bill_name,
             long bill_sno, Date start_date, Date end_date, Date term_date) {
        initComponents();
        con_ = con;
        term_month_ = term_month;
        start_date_ = start_date;
        end_date_ = end_date;
        term_date_ = term_date;
        bill_id_ = bill_id;
        bill_sno_ = bill_sno;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月");
        SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy年MM月dd日");

        //印刷種類のボタングループ
        ButtonGroup bg1 = new ButtonGroup();
        bg1.add(rb00);
        bg1.add(rb01);
        bg1.add(rb02);
        rb00.setSelected(true);

        clearView();
        請求先コード_.setText(String.valueOf(bill_id));
        請求先名_.setText(bill_name);
        請求月_.setText(sdf.format(term_month_));
        請求日_.setText(sdf2.format(term_date_));
        起算開始日_.setText(sdf2.format(start_date_));
        起算終了日_.setText(sdf2.format(end_date_));

    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        jPanel21 = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        print_btn_kousan = new javax.swing.JButton();
        quit_btn = new javax.swing.JButton();
        rb02 = new javax.swing.JRadioButton();
        rb00 = new javax.swing.JRadioButton();
        rb01 = new javax.swing.JRadioButton();
        jPanel2 = new javax.swing.JPanel();
        jLabel80 = new javax.swing.JLabel();
        開始枝番_ = new aks.swing.JTextField();
        jLabel78 = new javax.swing.JLabel();
        終了枝番_ = new aks.swing.JTextField();
        jPanel3 = new javax.swing.JPanel();
        jLabel82 = new javax.swing.JLabel();
        jLabel83 = new javax.swing.JLabel();
        jLabel84 = new javax.swing.JLabel();
        jLabel85 = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        請求先コード_ = new aks.swing.JTextField();
        請求先名_ = new aks.swing.JTextField();
        jPanel6 = new javax.swing.JPanel();
        請求月_ = new aks.swing.JTextField();
        jLabel86 = new javax.swing.JLabel();
        jLabel87 = new javax.swing.JLabel();
        jPanel7 = new javax.swing.JPanel();
        請求日_ = new aks.swing.JTextField();
        jPanel8 = new javax.swing.JPanel();
        起算開始日_ = new aks.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        起算終了日_ = new aks.swing.JTextField();
        jLabel81 = new javax.swing.JLabel();

        setMaximumSize(new java.awt.Dimension(500, 260));
        setMinimumSize(new java.awt.Dimension(500, 260));
        setName(""); // NOI18N
        setPreferredSize(new java.awt.Dimension(500, 260));
        setLayout(new java.awt.GridBagLayout());

        jPanel21.setMaximumSize(new java.awt.Dimension(500, 260));
        jPanel21.setMinimumSize(new java.awt.Dimension(500, 260));
        jPanel21.setPreferredSize(new java.awt.Dimension(500, 260));
        jPanel21.setLayout(new java.awt.GridBagLayout());

        jPanel1.setMaximumSize(new java.awt.Dimension(500, 260));
        jPanel1.setMinimumSize(new java.awt.Dimension(500, 260));
        jPanel1.setPreferredSize(new java.awt.Dimension(500, 260));
        jPanel1.setLayout(new java.awt.GridBagLayout());

        print_btn_kousan.setFont(new java.awt.Font("メイリオ", 0, 18)); // NOI18N
        print_btn_kousan.setText("出　力");
        print_btn_kousan.setMargin(new java.awt.Insets(2, 2, 2, 2));
        print_btn_kousan.setMaximumSize(new java.awt.Dimension(120, 30));
        print_btn_kousan.setMinimumSize(new java.awt.Dimension(120, 30));
        print_btn_kousan.setPreferredSize(new java.awt.Dimension(120, 30));
        print_btn_kousan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                print_btn_kousanActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        jPanel1.add(print_btn_kousan, gridBagConstraints);

        quit_btn.setFont(new java.awt.Font("メイリオ", 0, 18)); // NOI18N
        quit_btn.setText("閉じる");
        quit_btn.setMargin(new java.awt.Insets(2, 2, 2, 2));
        quit_btn.setMaximumSize(new java.awt.Dimension(120, 30));
        quit_btn.setMinimumSize(new java.awt.Dimension(120, 30));
        quit_btn.setPreferredSize(new java.awt.Dimension(120, 30));
        quit_btn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                quit_btnMousePressed(evt);
            }
        });
        quit_btn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                quit_btnActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 10);
        jPanel1.add(quit_btn, gridBagConstraints);

        rb02.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        rb02.setText("明細のみ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 5;
        jPanel1.add(rb02, gridBagConstraints);

        rb00.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        rb00.setText("すべて");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 5;
        jPanel1.add(rb00, gridBagConstraints);

        rb01.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        rb01.setText("表紙のみ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 5;
        jPanel1.add(rb01, gridBagConstraints);

        jPanel2.setMaximumSize(new java.awt.Dimension(100, 100));
        jPanel2.setMinimumSize(new java.awt.Dimension(100, 100));
        jPanel2.setLayout(new javax.swing.BoxLayout(jPanel2, javax.swing.BoxLayout.LINE_AXIS));

        jLabel80.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        jLabel80.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel80.setText("開始枝番");
        jLabel80.setMaximumSize(new java.awt.Dimension(70, 30));
        jLabel80.setMinimumSize(new java.awt.Dimension(70, 30));
        jLabel80.setOpaque(true);
        jLabel80.setPreferredSize(new java.awt.Dimension(70, 30));
        jPanel2.add(jLabel80);

        開始枝番_.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        開始枝番_.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        開始枝番_.setText("0");
        開始枝番_.setMaximumSize(new java.awt.Dimension(50, 30));
        開始枝番_.setMinimumSize(new java.awt.Dimension(50, 30));
        開始枝番_.setPreferredSize(new java.awt.Dimension(50, 30));
        開始枝番_.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                開始枝番_FocusGained(evt);
            }
        });
        開始枝番_.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                開始枝番_KeyPressed(evt);
            }
        });
        jPanel2.add(開始枝番_);

        jLabel78.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        jLabel78.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel78.setText("終了枝番");
        jLabel78.setMaximumSize(new java.awt.Dimension(70, 30));
        jLabel78.setMinimumSize(new java.awt.Dimension(70, 30));
        jLabel78.setOpaque(true);
        jLabel78.setPreferredSize(new java.awt.Dimension(70, 30));
        jPanel2.add(jLabel78);

        終了枝番_.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        終了枝番_.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        終了枝番_.setText("999");
        終了枝番_.setMaximumSize(new java.awt.Dimension(50, 30));
        終了枝番_.setMinimumSize(new java.awt.Dimension(50, 30));
        終了枝番_.setPreferredSize(new java.awt.Dimension(50, 30));
        終了枝番_.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                終了枝番_FocusGained(evt);
            }
        });
        終了枝番_.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                終了枝番_KeyPressed(evt);
            }
        });
        jPanel2.add(終了枝番_);

        jPanel3.setMaximumSize(new java.awt.Dimension(110, 30));
        jPanel3.setMinimumSize(new java.awt.Dimension(110, 30));
        jPanel3.setPreferredSize(new java.awt.Dimension(110, 30));

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 110, Short.MAX_VALUE)
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 30, Short.MAX_VALUE)
        );

        jPanel2.add(jPanel3);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.gridwidth = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        jPanel1.add(jPanel2, gridBagConstraints);

        jLabel82.setBackground(new java.awt.Color(0, 51, 153));
        jLabel82.setFont(new java.awt.Font("メイリオ", 1, 14)); // NOI18N
        jLabel82.setForeground(new java.awt.Color(255, 255, 255));
        jLabel82.setText("　印刷種類");
        jLabel82.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        jLabel82.setMaximumSize(new java.awt.Dimension(100, 30));
        jLabel82.setMinimumSize(new java.awt.Dimension(100, 30));
        jLabel82.setOpaque(true);
        jLabel82.setPreferredSize(new java.awt.Dimension(100, 30));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        jPanel1.add(jLabel82, gridBagConstraints);

        jLabel83.setBackground(new java.awt.Color(0, 51, 153));
        jLabel83.setFont(new java.awt.Font("メイリオ", 1, 14)); // NOI18N
        jLabel83.setForeground(new java.awt.Color(255, 255, 255));
        jLabel83.setText("　得意先");
        jLabel83.setToolTipText("");
        jLabel83.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        jLabel83.setMaximumSize(new java.awt.Dimension(100, 30));
        jLabel83.setMinimumSize(new java.awt.Dimension(100, 30));
        jLabel83.setOpaque(true);
        jLabel83.setPreferredSize(new java.awt.Dimension(100, 30));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        jPanel1.add(jLabel83, gridBagConstraints);

        jLabel84.setBackground(new java.awt.Color(0, 51, 153));
        jLabel84.setFont(new java.awt.Font("メイリオ", 1, 14)); // NOI18N
        jLabel84.setForeground(new java.awt.Color(255, 255, 255));
        jLabel84.setText("　　請求日");
        jLabel84.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        jLabel84.setMaximumSize(new java.awt.Dimension(100, 30));
        jLabel84.setMinimumSize(new java.awt.Dimension(100, 30));
        jLabel84.setOpaque(true);
        jLabel84.setPreferredSize(new java.awt.Dimension(100, 30));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        jPanel1.add(jLabel84, gridBagConstraints);

        jLabel85.setBackground(new java.awt.Color(0, 51, 153));
        jLabel85.setFont(new java.awt.Font("メイリオ", 1, 14)); // NOI18N
        jLabel85.setForeground(new java.awt.Color(255, 255, 255));
        jLabel85.setText("　枝番");
        jLabel85.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        jLabel85.setMaximumSize(new java.awt.Dimension(100, 30));
        jLabel85.setMinimumSize(new java.awt.Dimension(100, 30));
        jLabel85.setOpaque(true);
        jLabel85.setPreferredSize(new java.awt.Dimension(100, 30));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        jPanel1.add(jLabel85, gridBagConstraints);

        jPanel4.setMaximumSize(new java.awt.Dimension(100, 100));
        jPanel4.setMinimumSize(new java.awt.Dimension(100, 100));
        jPanel4.setLayout(new java.awt.GridBagLayout());

        請求先コード_.setEditable(false);
        請求先コード_.setBackground(new java.awt.Color(204, 255, 204));
        請求先コード_.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        請求先コード_.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        請求先コード_.setText("12345");
        請求先コード_.setFocusable(false);
        請求先コード_.setMaximumSize(new java.awt.Dimension(60, 30));
        請求先コード_.setMinimumSize(new java.awt.Dimension(60, 30));
        請求先コード_.setPreferredSize(new java.awt.Dimension(60, 30));
        請求先コード_.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                請求先コード_FocusGained(evt);
            }
        });
        請求先コード_.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                請求先コード_KeyPressed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        jPanel4.add(請求先コード_, gridBagConstraints);

        請求先名_.setEditable(false);
        請求先名_.setBackground(new java.awt.Color(204, 255, 204));
        請求先名_.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        請求先名_.setText("あああああああ");
        請求先名_.setFocusable(false);
        請求先名_.setMaximumSize(new java.awt.Dimension(250, 30));
        請求先名_.setMinimumSize(new java.awt.Dimension(250, 30));
        請求先名_.setPreferredSize(new java.awt.Dimension(250, 30));
        請求先名_.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                請求先名_FocusGained(evt);
            }
        });
        請求先名_.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                請求先名_KeyPressed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 10);
        jPanel4.add(請求先名_, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        jPanel1.add(jPanel4, gridBagConstraints);

        jPanel6.setMaximumSize(new java.awt.Dimension(100, 100));
        jPanel6.setMinimumSize(new java.awt.Dimension(100, 100));
        jPanel6.setLayout(new javax.swing.BoxLayout(jPanel6, javax.swing.BoxLayout.LINE_AXIS));

        請求月_.setEditable(false);
        請求月_.setBackground(new java.awt.Color(204, 255, 204));
        請求月_.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        請求月_.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        請求月_.setText("2014年05月");
        請求月_.setFocusable(false);
        請求月_.setMaximumSize(new java.awt.Dimension(120, 30));
        請求月_.setMinimumSize(new java.awt.Dimension(120, 30));
        請求月_.setPreferredSize(new java.awt.Dimension(120, 30));
        請求月_.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                請求月_FocusGained(evt);
            }
        });
        請求月_.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                請求月_KeyPressed(evt);
            }
        });
        jPanel6.add(請求月_);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        jPanel1.add(jPanel6, gridBagConstraints);

        jLabel86.setBackground(new java.awt.Color(0, 51, 153));
        jLabel86.setFont(new java.awt.Font("メイリオ", 1, 14)); // NOI18N
        jLabel86.setForeground(new java.awt.Color(255, 255, 255));
        jLabel86.setText("　　　起算日");
        jLabel86.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        jLabel86.setMaximumSize(new java.awt.Dimension(100, 30));
        jLabel86.setMinimumSize(new java.awt.Dimension(100, 30));
        jLabel86.setOpaque(true);
        jLabel86.setPreferredSize(new java.awt.Dimension(100, 30));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        jPanel1.add(jLabel86, gridBagConstraints);

        jLabel87.setBackground(new java.awt.Color(0, 51, 153));
        jLabel87.setFont(new java.awt.Font("メイリオ", 1, 14)); // NOI18N
        jLabel87.setForeground(new java.awt.Color(255, 255, 255));
        jLabel87.setText("　請求月");
        jLabel87.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        jLabel87.setMaximumSize(new java.awt.Dimension(100, 30));
        jLabel87.setMinimumSize(new java.awt.Dimension(100, 30));
        jLabel87.setOpaque(true);
        jLabel87.setPreferredSize(new java.awt.Dimension(100, 30));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        jPanel1.add(jLabel87, gridBagConstraints);

        jPanel7.setMaximumSize(new java.awt.Dimension(100, 100));
        jPanel7.setMinimumSize(new java.awt.Dimension(100, 100));
        jPanel7.setLayout(new javax.swing.BoxLayout(jPanel7, javax.swing.BoxLayout.LINE_AXIS));

        請求日_.setEditable(false);
        請求日_.setBackground(new java.awt.Color(204, 255, 204));
        請求日_.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        請求日_.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        請求日_.setText("2014年05月");
        請求日_.setFocusable(false);
        請求日_.setMaximumSize(new java.awt.Dimension(150, 30));
        請求日_.setMinimumSize(new java.awt.Dimension(150, 30));
        請求日_.setPreferredSize(new java.awt.Dimension(150, 30));
        請求日_.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                請求日_FocusGained(evt);
            }
        });
        請求日_.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                請求日_KeyPressed(evt);
            }
        });
        jPanel7.add(請求日_);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        jPanel1.add(jPanel7, gridBagConstraints);

        jPanel8.setMaximumSize(new java.awt.Dimension(100, 100));
        jPanel8.setMinimumSize(new java.awt.Dimension(100, 100));
        jPanel8.setLayout(new javax.swing.BoxLayout(jPanel8, javax.swing.BoxLayout.LINE_AXIS));

        起算開始日_.setEditable(false);
        起算開始日_.setBackground(new java.awt.Color(204, 255, 204));
        起算開始日_.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        起算開始日_.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        起算開始日_.setText("2014年05月");
        起算開始日_.setFocusable(false);
        起算開始日_.setMaximumSize(new java.awt.Dimension(150, 30));
        起算開始日_.setMinimumSize(new java.awt.Dimension(150, 30));
        起算開始日_.setPreferredSize(new java.awt.Dimension(150, 30));
        起算開始日_.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                起算開始日_FocusGained(evt);
            }
        });
        起算開始日_.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                起算開始日_KeyPressed(evt);
            }
        });
        jPanel8.add(起算開始日_);

        jLabel1.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("～");
        jLabel1.setMaximumSize(new java.awt.Dimension(30, 22));
        jLabel1.setMinimumSize(new java.awt.Dimension(30, 22));
        jLabel1.setPreferredSize(new java.awt.Dimension(30, 22));
        jPanel8.add(jLabel1);

        起算終了日_.setEditable(false);
        起算終了日_.setBackground(new java.awt.Color(204, 255, 204));
        起算終了日_.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        起算終了日_.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        起算終了日_.setText("2014年05月");
        起算終了日_.setFocusable(false);
        起算終了日_.setMaximumSize(new java.awt.Dimension(150, 30));
        起算終了日_.setMinimumSize(new java.awt.Dimension(150, 30));
        起算終了日_.setPreferredSize(new java.awt.Dimension(150, 30));
        起算終了日_.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                起算終了日_FocusGained(evt);
            }
        });
        起算終了日_.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                起算終了日_KeyPressed(evt);
            }
        });
        jPanel8.add(起算終了日_);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        jPanel1.add(jPanel8, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        jPanel21.add(jPanel1, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        add(jPanel21, gridBagConstraints);

        jLabel81.setFont(new java.awt.Font("メイリオ", 1, 14)); // NOI18N
        jLabel81.setText("   開始枝番");
        jLabel81.setMaximumSize(new java.awt.Dimension(80, 30));
        jLabel81.setMinimumSize(new java.awt.Dimension(80, 30));
        jLabel81.setOpaque(true);
        jLabel81.setPreferredSize(new java.awt.Dimension(80, 30));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        add(jLabel81, gridBagConstraints);
    }// </editor-fold>//GEN-END:initComponents

    private void quit_btnMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_quit_btnMousePressed
        // TODO add your handling code here:
//        doClose();
    }//GEN-LAST:event_quit_btnMousePressed

    private void print_btn_kousanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_print_btn_kousanActionPerformed
        // TODO add your handling code here:
        //印刷指示
        doPrint(term_date_, bill_id_, bill_sno_);

    }//GEN-LAST:event_print_btn_kousanActionPerformed

    private void quit_btnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_quit_btnActionPerformed
        // TODO add your handling code here:
        doClose();
    }//GEN-LAST:event_quit_btnActionPerformed

    private void 開始枝番_KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_開始枝番_KeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            開始枝番_.transferFocus();
            evt.consume();
        }
    }//GEN-LAST:event_開始枝番_KeyPressed

    private void 終了枝番_KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_終了枝番_KeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            終了枝番_.transferFocus();
            evt.consume();
        }
    }//GEN-LAST:event_終了枝番_KeyPressed

    private void 開始枝番_FocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_開始枝番_FocusGained
        // TODO add your handling code here:
        開始枝番_.selectAll();
    }//GEN-LAST:event_開始枝番_FocusGained

    private void 終了枝番_FocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_終了枝番_FocusGained
        // TODO add your handling code here:
        終了枝番_.selectAll();
    }//GEN-LAST:event_終了枝番_FocusGained

    private void 請求先コード_FocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_請求先コード_FocusGained
        // TODO add your handling code here:
    }//GEN-LAST:event_請求先コード_FocusGained

    private void 請求先コード_KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_請求先コード_KeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_請求先コード_KeyPressed

    private void 請求先名_FocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_請求先名_FocusGained
        // TODO add your handling code here:
    }//GEN-LAST:event_請求先名_FocusGained

    private void 請求先名_KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_請求先名_KeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_請求先名_KeyPressed

    private void 請求月_FocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_請求月_FocusGained
        // TODO add your handling code here:
    }//GEN-LAST:event_請求月_FocusGained

    private void 請求月_KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_請求月_KeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_請求月_KeyPressed

    private void 請求日_FocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_請求日_FocusGained
        // TODO add your handling code here:
    }//GEN-LAST:event_請求日_FocusGained

    private void 請求日_KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_請求日_KeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_請求日_KeyPressed

    private void 起算開始日_FocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_起算開始日_FocusGained
        // TODO add your handling code here:
    }//GEN-LAST:event_起算開始日_FocusGained

    private void 起算開始日_KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_起算開始日_KeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_起算開始日_KeyPressed

    private void 起算終了日_FocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_起算終了日_FocusGained
        // TODO add your handling code here:
    }//GEN-LAST:event_起算終了日_FocusGained

    private void 起算終了日_KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_起算終了日_KeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_起算終了日_KeyPressed

    private void clearView() {
        開始枝番_.setText("0");
        終了枝番_.setText("999");
        rb00.setSelected(true);
    }

    private void doPrint(java.sql.Date term_date_, int ent_id, long bill_sno) {
        try {

            int start_eda_ = 0;
            if (開始枝番_.getText().length() > 0) {
                start_eda_ = Integer.valueOf(開始枝番_.getText());
            }
            int end_eda_ = 999;
            if (終了枝番_.getText().length() > 0) {
                end_eda_ = Integer.valueOf(終了枝番_.getText());
            }

            int print_kind_ = 0;
            if (rb00.isSelected()) {
                print_kind_ = 0;
            } else if (rb01.isSelected()) {
                print_kind_ = 1;
            } else if (rb02.isSelected()) {
                print_kind_ = 2;
            }

            new XlsxBillReport(XlsxBillReport.BILL_REPORT1).print(con_, XlsxBillReport.BILL_REPORT1,
                     term_date_,
                     ent_id,
                     start_eda_,
                     end_eda_,
                     print_kind_,
                     bill_sno);

        } catch (Exception e) {

        }

    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel78;
    private javax.swing.JLabel jLabel80;
    private javax.swing.JLabel jLabel81;
    private javax.swing.JLabel jLabel82;
    private javax.swing.JLabel jLabel83;
    private javax.swing.JLabel jLabel84;
    private javax.swing.JLabel jLabel85;
    private javax.swing.JLabel jLabel86;
    private javax.swing.JLabel jLabel87;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel21;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JButton print_btn_kousan;
    private javax.swing.JButton quit_btn;
    private javax.swing.JRadioButton rb00;
    private javax.swing.JRadioButton rb01;
    private javax.swing.JRadioButton rb02;
    private javax.swing.JTextField 終了枝番_;
    private javax.swing.JTextField 請求先コード_;
    private javax.swing.JTextField 請求先名_;
    private javax.swing.JTextField 請求日_;
    private javax.swing.JTextField 請求月_;
    private javax.swing.JTextField 起算終了日_;
    private javax.swing.JTextField 起算開始日_;
    private javax.swing.JTextField 開始枝番_;
    // End of variables declaration//GEN-END:variables

    protected void doClose() {
        Window w = SwingUtilities.getWindowAncestor(this);
        if (w instanceof Frame) {
            w.dispose();

        } else {
            w.dispose();
        }
    }
}
