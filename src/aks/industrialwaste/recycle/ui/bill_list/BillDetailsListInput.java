package aks.industrialwaste.recycle.ui.bill_list;

import aks.industrialwaste.database.DBManager;
import aks.industrialwaste.recycle.util.GoodsSelector;
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
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import jp.co.astax.awt.FontUtility;
import jp.co.astax.swing.DateTimeField;

public class BillDetailsListInput extends javax.swing.JPanel {

    private DateTimeField termDateField1_, termDateField2_;
    private BillDetailsListTableModel model;
    private Connection con;
    private int row;
    private Map<String, Object> columns_;
    private long bill_sno;
    public String[][] bclassName2Array = new String[2][];
    //選択されている行の各idを取得する
    public int b_class_id_ = 0;
    //取得したidを要素とする配列のindexを取得する(表示のため)
    public int b_class_id_index = 0;
    private boolean add_flag = false;

    public BillDetailsListInput(boolean add_flag_, Connection con_, BillDetailsListTableModel model_, int row_, long bill_sno_) {
        initComponents();
        Font font14 = FontUtility.getFont("メイリオ", Font.PLAIN, 14);
        con = con_;
        model = model_;
        row = row_;
        columns_ = new HashMap<String, Object>();
        bill_sno = bill_sno_;

        add_flag = add_flag_;
        copyColumns();

        b_class_id_ = (Integer) columns_.get("b_class_id");

        termDateField1_ = new DateTimeField(DateTimeField.DateFormat.YMD);
        termDateField1_.setFont(font14);
        termDateField1_.setMinimumSize(new Dimension(250, 25));
        termDateField1_.setPreferredSize(new Dimension(250, 25));
        calendar_p1.add(termDateField1_);

        termDateField2_ = new DateTimeField(DateTimeField.DateFormat.YM);
        termDateField2_.setFont(font14);
        termDateField2_.setMinimumSize(new Dimension(250, 25));
        termDateField2_.setPreferredSize(new Dimension(250, 25));
        calendar_p2.add(termDateField2_);

        //マスタ問い合わせ
        //請求分類
        bclassName2Array = GetMasterData("v0_m_b_class", bclassName2Array);
        String[] bclassName = new String[bclassName2Array[0].length];
        for (int i = 0; i <= bclassName2Array[0].length - 1; i++) {
            bclassName[i] = (String) bclassName2Array[1][i].toString();
            if (b_class_id_ == Integer.valueOf((String) bclassName2Array[0][i])) {
                b_class_id_index = i;
            }
        }
        b_class_cmb_.setModel(new javax.swing.DefaultComboBoxModel(bclassName));

        clearView();
        copyColumns();
        updateView();

        unit_price_.requestFocus();
    }

    private void copyColumns() {
        columns_.clear();

        if (!add_flag) {
            columns_.putAll(model.getColumns(row));
        } else {

            if (row < 0) {
                row = 0;
            }
            columns_.putAll(model.getColumns(row));

            columns_.put("weight_sno", (Long) model.getValueAt(row, "weight_sno"));
            columns_.put("bill_sno", bill_sno);
            columns_.put("car_no", (Integer) model.getValueAt(row, "car_no"));
            columns_.put("goods_id", 0);

            columns_.put("bill_name", "");
            columns_.put("bill_name2", "");
            columns_.put("bill_value", 0);
            columns_.put("bill_unit", "");
            columns_.put("unit_price", 0.);
            columns_.put("bill_price", 0.);

            columns_.put("remark01", "");
            columns_.put("remark02", "");

            columns_.put("b_class_id", 0);
            columns_.put("b_class_name", "");

            columns_.put("eda_office_id", 0);
            columns_.put("advances", 0);
        }
    }

    private void clearView() {

        termDateField1_.setValue(null);
        termDateField2_.setValue(null);
        bill_name_.setText("");
        bill_value_.setText("0.");
        bill_unit_.setText("");
        unit_price_.setText("0.");
        advances_.setText("0.");
        bill_price_.setText("0.");

        remark01_.setText("");

        dis_office_id_.setText("0");
        office_name2_.setText("");
        eda_office_id_.setText("0");

    }

    private void updateView() {

        Object o = columns_.get("term_date");
        Calendar c = Calendar.getInstance();
        if (o instanceof java.sql.Date) {
            c.setTime((java.sql.Date) o);
            termDateField1_.setValue(c);
            columns_.put("term_date", termDateField1_.getValue());
        } else if (o instanceof Calendar) {
            termDateField1_.setValue((Calendar) o);
            columns_.put("term_date", (Calendar) o);
        } else {
            termDateField1_.setValue(null);
            columns_.put("term_date", termDateField1_.getValue());
        }

        //20140519 edit
        //所属月が所属日だったため
        o = columns_.get("term_month");
        if (o instanceof java.sql.Date) {
            c.setTime((java.sql.Date) o);
            termDateField2_.setValue(c);
            columns_.put("term_month", termDateField2_.getValue());
        } else if (o instanceof Calendar) {
            termDateField2_.setValue((Calendar) o);
            columns_.put("term_month", (Calendar) o);
        } else {
            termDateField2_.setValue(null);
            columns_.put("term_month", termDateField2_.getValue());
        }

        goods_id_.setText(String.valueOf((Integer) columns_.get("goods_id")));
        b_class_cmb_.setSelectedIndex(b_class_id_index);

        bill_name_.setText((String) columns_.get("bill_name"));
        bill_value_.setText(String.valueOf(columns_.get("bill_value")));
        bill_unit_.setText((String) columns_.get("bill_unit"));
        unit_price_.setText(String.valueOf(columns_.get("unit_price")));
        advances_.setText(String.valueOf(columns_.get("advances")));
        bill_price_.setText(String.valueOf(Math.round((Double) columns_.get("bill_price"))));

        remark01_.setText((String) columns_.get("remark01"));
        dis_office_id_.setText(String.valueOf((Integer) columns_.get("dis_office_id")));
        office_name2_.setText((String) columns_.get("office_name2"));

        eda_office_id_.setText(String.valueOf(columns_.get("eda_office_id")));

    }

    private void getBillDetailData(Long bill_sno) {

        //新規時の所属月・得意先・現場検索
        try {

            String s = String.format("select * from v1_d_bill_detail "
                     + " where bill_sno = ? order by b_class_id;");

            PreparedStatement ps = con.prepareStatement(s);
            try {
                ps.setObject(1, bill_sno);
                ResultSet rs = ps.executeQuery();

                if (rs.next()) {
                    columns_.put("bill_id", rs.getInt("bill_id"));
                    columns_.put("dis_office_id", rs.getInt("dis_office_id"));
                    columns_.put("term_month", rs.getDate("term_month"));
                    columns_.put("bill_date", rs.getDate("bill_date"));
                }

            } finally {
                ps.close();
            }
        } catch (SQLException e) {

        }

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
        btnCalc_ = new javax.swing.JButton();
        jPanel5 = new javax.swing.JPanel();
        btnRegist_ = new javax.swing.JButton();
        btnCancel_ = new javax.swing.JButton();
        btnClose_ = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        jLabel11 = new javax.swing.JLabel();
        goods_id_ = new aks.swing.JTextFieldHankaku();
        bill_name_ = new aks.swing.JTextField();
        b_class_cmb_ = new javax.swing.JComboBox();
        jLabel14 = new javax.swing.JLabel();
        office_name2_ = new aks.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        calendar_p1 = new javax.swing.JPanel();
        dis_office_id_ = new aks.swing.JTextFieldHankaku();
        jLabel15 = new javax.swing.JLabel();
        calendar_p2 = new javax.swing.JPanel();
        jLabel16 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        eda_office_id_ = new aks.swing.JTextFieldHankaku();
        jPanel6 = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        bill_unit_ = new aks.swing.JTextField();
        unit_price_ = new aks.swing.JTextFieldHankaku();
        jLabel2 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        bill_price_ = new aks.swing.JTextFieldHankaku();
        advances_ = new aks.swing.JTextFieldHankaku();
        remark01_ = new aks.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        bill_value_ = new aks.swing.JTextFieldHankaku();
        jLabel12 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();

        setMaximumSize(new java.awt.Dimension(650, 330));
        setMinimumSize(new java.awt.Dimension(650, 330));
        setPreferredSize(new java.awt.Dimension(650, 330));
        setLayout(new java.awt.GridBagLayout());

        jPanel1.setMaximumSize(new java.awt.Dimension(650, 330));
        jPanel1.setMinimumSize(new java.awt.Dimension(650, 330));
        jPanel1.setPreferredSize(new java.awt.Dimension(650, 330));
        jPanel1.setLayout(new java.awt.GridBagLayout());

        jPanel2.setMaximumSize(new java.awt.Dimension(600, 70));
        jPanel2.setMinimumSize(new java.awt.Dimension(600, 50));
        jPanel2.setPreferredSize(new java.awt.Dimension(600, 50));
        jPanel2.setLayout(new java.awt.GridBagLayout());

        jPanel4.setMinimumSize(new java.awt.Dimension(440, 50));
        jPanel4.setPreferredSize(new java.awt.Dimension(125, 50));
        jPanel4.setLayout(new java.awt.GridBagLayout());

        btnCalc_.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        btnCalc_.setLabel("金額再計算");
        btnCalc_.setMargin(new java.awt.Insets(2, 2, 2, 2));
        btnCalc_.setMaximumSize(new java.awt.Dimension(120, 30));
        btnCalc_.setMinimumSize(new java.awt.Dimension(120, 30));
        btnCalc_.setPreferredSize(new java.awt.Dimension(120, 30));
        btnCalc_.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCalc_ActionPerformed(evt);
            }
        });
        btnCalc_.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                btnCalc_KeyPressed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 0, 2);
        jPanel4.add(btnCalc_, gridBagConstraints);

        jPanel5.setMaximumSize(new java.awt.Dimension(50, 10));
        jPanel5.setMinimumSize(new java.awt.Dimension(50, 10));
        jPanel5.setPreferredSize(new java.awt.Dimension(50, 10));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        jPanel4.add(jPanel5, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
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

        jPanel3.setMaximumSize(new java.awt.Dimension(600, 330));
        jPanel3.setMinimumSize(new java.awt.Dimension(600, 330));
        jPanel3.setPreferredSize(new java.awt.Dimension(600, 330));
        jPanel3.setLayout(new java.awt.GridBagLayout());

        jLabel11.setBackground(new java.awt.Color(0, 51, 153));
        jLabel11.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        jLabel11.setForeground(new java.awt.Color(255, 255, 255));
        jLabel11.setText("　請求品名");
        jLabel11.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        jLabel11.setMaximumSize(new java.awt.Dimension(120, 25));
        jLabel11.setMinimumSize(new java.awt.Dimension(120, 25));
        jLabel11.setOpaque(true);
        jLabel11.setPreferredSize(new java.awt.Dimension(120, 25));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        jPanel3.add(jLabel11, gridBagConstraints);

        goods_id_.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        goods_id_.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        goods_id_.setText("00000");
        goods_id_.setMaximumSize(new java.awt.Dimension(60, 30));
        goods_id_.setMinimumSize(new java.awt.Dimension(60, 30));
        goods_id_.setPreferredSize(new java.awt.Dimension(60, 30));
        goods_id_.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                goods_id_MouseClicked(evt);
            }
        });
        goods_id_.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                goods_id_FocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                goods_id_FocusLost(evt);
            }
        });
        goods_id_.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                goods_id_KeyPressed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        jPanel3.add(goods_id_, gridBagConstraints);

        bill_name_.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        bill_name_.setText("aiueo");
        bill_name_.setMaximumSize(new java.awt.Dimension(200, 30));
        bill_name_.setMinimumSize(new java.awt.Dimension(200, 30));
        bill_name_.setPreferredSize(new java.awt.Dimension(200, 30));
        bill_name_.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                bill_name_KeyPressed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.ipadx = 61;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        jPanel3.add(bill_name_, gridBagConstraints);

        b_class_cmb_.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        b_class_cmb_.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        b_class_cmb_.setMaximumSize(new java.awt.Dimension(150, 30));
        b_class_cmb_.setMinimumSize(new java.awt.Dimension(150, 30));
        b_class_cmb_.setPreferredSize(new java.awt.Dimension(150, 30));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        jPanel3.add(b_class_cmb_, gridBagConstraints);

        jLabel14.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        jLabel14.setText("※枝番は任意");
        jLabel14.setMaximumSize(new java.awt.Dimension(100, 30));
        jLabel14.setMinimumSize(new java.awt.Dimension(100, 30));
        jLabel14.setPreferredSize(new java.awt.Dimension(100, 30));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        jPanel3.add(jLabel14, gridBagConstraints);

        office_name2_.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        office_name2_.setMaximumSize(new java.awt.Dimension(365, 30));
        office_name2_.setMinimumSize(new java.awt.Dimension(365, 30));
        office_name2_.setPreferredSize(new java.awt.Dimension(365, 30));
        office_name2_.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                office_name2_FocusGained(evt);
            }
        });
        office_name2_.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                office_name2_KeyPressed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        jPanel3.add(office_name2_, gridBagConstraints);

        jLabel9.setBackground(new java.awt.Color(0, 51, 153));
        jLabel9.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(255, 255, 255));
        jLabel9.setText("　請求起算日");
        jLabel9.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        jLabel9.setMaximumSize(new java.awt.Dimension(120, 25));
        jLabel9.setMinimumSize(new java.awt.Dimension(120, 25));
        jLabel9.setOpaque(true);
        jLabel9.setPreferredSize(new java.awt.Dimension(120, 25));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        jPanel3.add(jLabel9, gridBagConstraints);

        calendar_p1.setMaximumSize(new java.awt.Dimension(250, 32));
        calendar_p1.setMinimumSize(new java.awt.Dimension(250, 32));
        calendar_p1.setPreferredSize(new java.awt.Dimension(250, 32));
        calendar_p1.setLayout(new java.awt.GridLayout(1, 0));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        jPanel3.add(calendar_p1, gridBagConstraints);

        dis_office_id_.setEditable(false);
        dis_office_id_.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        dis_office_id_.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        dis_office_id_.setText("00000");
        dis_office_id_.setEnabled(false);
        dis_office_id_.setFocusable(false);
        dis_office_id_.setMaximumSize(new java.awt.Dimension(60, 30));
        dis_office_id_.setMinimumSize(new java.awt.Dimension(60, 30));
        dis_office_id_.setPreferredSize(new java.awt.Dimension(60, 30));
        dis_office_id_.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                dis_office_id_FocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                dis_office_id_FocusLost(evt);
            }
        });
        dis_office_id_.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                dis_office_id_KeyPressed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        jPanel3.add(dis_office_id_, gridBagConstraints);

        jLabel15.setBackground(new java.awt.Color(0, 51, 153));
        jLabel15.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        jLabel15.setForeground(new java.awt.Color(255, 255, 255));
        jLabel15.setText("　受入日");
        jLabel15.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        jLabel15.setMaximumSize(new java.awt.Dimension(120, 25));
        jLabel15.setMinimumSize(new java.awt.Dimension(120, 25));
        jLabel15.setOpaque(true);
        jLabel15.setPreferredSize(new java.awt.Dimension(120, 25));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        jPanel3.add(jLabel15, gridBagConstraints);

        calendar_p2.setMaximumSize(new java.awt.Dimension(200, 32));
        calendar_p2.setMinimumSize(new java.awt.Dimension(200, 32));
        calendar_p2.setPreferredSize(new java.awt.Dimension(200, 32));
        calendar_p2.setLayout(new java.awt.GridLayout(1, 0));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        jPanel3.add(calendar_p2, gridBagConstraints);

        jLabel16.setBackground(new java.awt.Color(0, 51, 153));
        jLabel16.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        jLabel16.setForeground(new java.awt.Color(255, 255, 255));
        jLabel16.setText("　請求現場名");
        jLabel16.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        jLabel16.setMaximumSize(new java.awt.Dimension(120, 25));
        jLabel16.setMinimumSize(new java.awt.Dimension(120, 25));
        jLabel16.setOpaque(true);
        jLabel16.setPreferredSize(new java.awt.Dimension(120, 25));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        jPanel3.add(jLabel16, gridBagConstraints);

        jLabel17.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        jLabel17.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel17.setText("請求枝番");
        jLabel17.setMaximumSize(new java.awt.Dimension(80, 19));
        jLabel17.setMinimumSize(new java.awt.Dimension(80, 19));
        jLabel17.setPreferredSize(new java.awt.Dimension(80, 19));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        jPanel3.add(jLabel17, gridBagConstraints);

        eda_office_id_.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        eda_office_id_.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        eda_office_id_.setMaximumSize(new java.awt.Dimension(100, 30));
        eda_office_id_.setMinimumSize(new java.awt.Dimension(100, 30));
        eda_office_id_.setPreferredSize(new java.awt.Dimension(100, 30));
        eda_office_id_.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                eda_office_id_FocusGained(evt);
            }
        });
        eda_office_id_.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                eda_office_id_KeyPressed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        jPanel3.add(eda_office_id_, gridBagConstraints);

        jPanel6.setMaximumSize(new java.awt.Dimension(100, 100));
        jPanel6.setMinimumSize(new java.awt.Dimension(100, 100));
        jPanel6.setLayout(new java.awt.GridBagLayout());

        jLabel6.setBackground(new java.awt.Color(0, 51, 153));
        jLabel6.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(255, 255, 255));
        jLabel6.setText("　単位");
        jLabel6.setMaximumSize(new java.awt.Dimension(80, 25));
        jLabel6.setMinimumSize(new java.awt.Dimension(80, 25));
        jLabel6.setOpaque(true);
        jLabel6.setPreferredSize(new java.awt.Dimension(80, 25));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 0, 2);
        jPanel6.add(jLabel6, gridBagConstraints);

        bill_unit_.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        bill_unit_.setMaximumSize(new java.awt.Dimension(80, 30));
        bill_unit_.setMinimumSize(new java.awt.Dimension(80, 30));
        bill_unit_.setPreferredSize(new java.awt.Dimension(80, 30));
        bill_unit_.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                bill_unit_FocusGained(evt);
            }
        });
        bill_unit_.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                bill_unit_KeyPressed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 2, 2, 2);
        jPanel6.add(bill_unit_, gridBagConstraints);

        unit_price_.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        unit_price_.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        unit_price_.setText("0");
        unit_price_.setMaximumSize(new java.awt.Dimension(110, 30));
        unit_price_.setMinimumSize(new java.awt.Dimension(110, 30));
        unit_price_.setPreferredSize(new java.awt.Dimension(110, 30));
        unit_price_.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                unit_price_FocusGained(evt);
            }
        });
        unit_price_.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                unit_price_KeyPressed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 2, 2, 2);
        jPanel6.add(unit_price_, gridBagConstraints);

        jLabel2.setBackground(new java.awt.Color(0, 51, 153));
        jLabel2.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("　商品単価");
        jLabel2.setMaximumSize(new java.awt.Dimension(110, 25));
        jLabel2.setMinimumSize(new java.awt.Dimension(110, 25));
        jLabel2.setOpaque(true);
        jLabel2.setPreferredSize(new java.awt.Dimension(110, 25));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 0, 2);
        jPanel6.add(jLabel2, gridBagConstraints);

        jLabel8.setBackground(new java.awt.Color(0, 51, 153));
        jLabel8.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(255, 255, 255));
        jLabel8.setText("　請求金額");
        jLabel8.setMaximumSize(new java.awt.Dimension(125, 25));
        jLabel8.setMinimumSize(new java.awt.Dimension(125, 25));
        jLabel8.setOpaque(true);
        jLabel8.setPreferredSize(new java.awt.Dimension(125, 25));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 0, 2);
        jPanel6.add(jLabel8, gridBagConstraints);

        jLabel13.setBackground(new java.awt.Color(0, 51, 153));
        jLabel13.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        jLabel13.setForeground(new java.awt.Color(255, 255, 255));
        jLabel13.setText("　産廃税");
        jLabel13.setMaximumSize(new java.awt.Dimension(100, 25));
        jLabel13.setMinimumSize(new java.awt.Dimension(100, 25));
        jLabel13.setOpaque(true);
        jLabel13.setPreferredSize(new java.awt.Dimension(100, 25));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 0, 2);
        jPanel6.add(jLabel13, gridBagConstraints);

        bill_price_.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        bill_price_.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        bill_price_.setText("00000");
        bill_price_.setMaximumSize(new java.awt.Dimension(125, 30));
        bill_price_.setMinimumSize(new java.awt.Dimension(125, 30));
        bill_price_.setPreferredSize(new java.awt.Dimension(125, 30));
        bill_price_.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                bill_price_FocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                bill_price_FocusLost(evt);
            }
        });
        bill_price_.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                bill_price_KeyPressed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 2, 2, 2);
        jPanel6.add(bill_price_, gridBagConstraints);

        advances_.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        advances_.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        advances_.setText("0");
        advances_.setMaximumSize(new java.awt.Dimension(100, 30));
        advances_.setMinimumSize(new java.awt.Dimension(100, 30));
        advances_.setPreferredSize(new java.awt.Dimension(100, 30));
        advances_.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                advances_FocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                advances_FocusLost(evt);
            }
        });
        advances_.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                advances_KeyPressed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 2, 2, 2);
        jPanel6.add(advances_, gridBagConstraints);

        remark01_.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        remark01_.setMaximumSize(new java.awt.Dimension(405, 30));
        remark01_.setMinimumSize(new java.awt.Dimension(405, 30));
        remark01_.setPreferredSize(new java.awt.Dimension(405, 30));
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
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        jPanel6.add(remark01_, gridBagConstraints);

        jLabel5.setBackground(new java.awt.Color(0, 51, 153));
        jLabel5.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(255, 255, 255));
        jLabel5.setText("　数量");
        jLabel5.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        jLabel5.setMaximumSize(new java.awt.Dimension(120, 25));
        jLabel5.setMinimumSize(new java.awt.Dimension(120, 25));
        jLabel5.setOpaque(true);
        jLabel5.setPreferredSize(new java.awt.Dimension(120, 25));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 0, 2);
        jPanel6.add(jLabel5, gridBagConstraints);

        bill_value_.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        bill_value_.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        bill_value_.setText("0");
        bill_value_.setMaximumSize(new java.awt.Dimension(120, 30));
        bill_value_.setMinimumSize(new java.awt.Dimension(120, 30));
        bill_value_.setPreferredSize(new java.awt.Dimension(120, 30));
        bill_value_.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                bill_value_FocusGained(evt);
            }
        });
        bill_value_.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                bill_value_KeyPressed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.ipadx = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 2, 2, 2);
        jPanel6.add(bill_value_, gridBagConstraints);

        jLabel12.setBackground(new java.awt.Color(0, 51, 153));
        jLabel12.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        jLabel12.setForeground(new java.awt.Color(255, 255, 255));
        jLabel12.setText("　備考");
        jLabel12.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        jLabel12.setMaximumSize(new java.awt.Dimension(120, 25));
        jLabel12.setMinimumSize(new java.awt.Dimension(120, 25));
        jLabel12.setOpaque(true);
        jLabel12.setPreferredSize(new java.awt.Dimension(120, 25));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        jPanel6.add(jLabel12, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.gridwidth = 4;
        gridBagConstraints.gridheight = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        jPanel3.add(jPanel6, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weighty = 1.0;
        jPanel1.add(jPanel3, gridBagConstraints);

        jLabel1.setBackground(new java.awt.Color(0, 0, 204));
        jLabel1.setFont(new java.awt.Font("メイリオ", 0, 24)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("請求明細入力");
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

    private void unit_price_KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_unit_price_KeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            unit_price_.transferFocus();
            evt.consume();
        }
    }//GEN-LAST:event_unit_price_KeyPressed

    private void bill_value_KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_bill_value_KeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            bill_value_.transferFocus();
            evt.consume();
        }
    }//GEN-LAST:event_bill_value_KeyPressed

    private void bill_unit_KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_bill_unit_KeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            bill_unit_.transferFocus();
            evt.consume();
        }
    }//GEN-LAST:event_bill_unit_KeyPressed

    private void advances_KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_advances_KeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            advances_.transferFocus();
            evt.consume();
        }
    }//GEN-LAST:event_advances_KeyPressed

    private void bill_price_KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_bill_price_KeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            bill_price_.transferFocus();
            evt.consume();
        }
    }//GEN-LAST:event_bill_price_KeyPressed

    private void bill_price_FocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_bill_price_FocusLost
        // TODO add your handling code here:
    }//GEN-LAST:event_bill_price_FocusLost

    private void remark01_KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_remark01_KeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            remark01_.transferFocus();
            evt.consume();
        }
    }//GEN-LAST:event_remark01_KeyPressed

    private void bill_price_FocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_bill_price_FocusGained
        // TODO add your handling code here:
        bill_price_.selectAll();
    }//GEN-LAST:event_bill_price_FocusGained

    private void unit_price_FocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_unit_price_FocusGained
        // TODO add your handling code here:
        unit_price_.selectAll();
    }//GEN-LAST:event_unit_price_FocusGained

    private void bill_value_FocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_bill_value_FocusGained
        // TODO add your handling code here:
        bill_value_.selectAll();
    }//GEN-LAST:event_bill_value_FocusGained

    private void bill_unit_FocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_bill_unit_FocusGained
        // TODO add your handling code here:
        bill_unit_.selectAll();
    }//GEN-LAST:event_bill_unit_FocusGained

    private void advances_FocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_advances_FocusGained
        // TODO add your handling code here:
        advances_.selectAll();
    }//GEN-LAST:event_advances_FocusGained

    private void remark01_FocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_remark01_FocusGained
        // TODO add your handling code here:
        remark01_.selectAll();
    }//GEN-LAST:event_remark01_FocusGained

    private void btnCalc_ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCalc_ActionPerformed
        // TODO add your handling code here:
        //金額再計算
        doCalc();
    }//GEN-LAST:event_btnCalc_ActionPerformed

    private void btnRegist_KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_btnRegist_KeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            //登録ボタン
            doRegist();
        }
    }//GEN-LAST:event_btnRegist_KeyPressed

    private void goods_id_FocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_goods_id_FocusGained
        // TODO add your handling code here:
        goods_id_.selectAll();
}//GEN-LAST:event_goods_id_FocusGained

    private void goods_id_FocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_goods_id_FocusLost
        // TODO add your handling code here:
        try {
            int id = Integer.valueOf(goods_id_.getText());

            if (id >= 0) {

                bill_name_.setText((String) (getGoodsName(id)[1]));
                b_class_cmb_.setSelectedIndex((Integer) (getGoodsName(id)[3]));
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(
                     BillDetailsListInput.this,
                     "コードは数字のみ入力出来ます。",
                     "確認",
                     JOptionPane.ERROR_MESSAGE);
        }
}//GEN-LAST:event_goods_id_FocusLost

    private void goods_id_KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_goods_id_KeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            bill_name_.requestFocus();
        }
}//GEN-LAST:event_goods_id_KeyPressed

    private void bill_name_KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_bill_name_KeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            bill_value_.requestFocus();
        }
}//GEN-LAST:event_bill_name_KeyPressed

    private void office_name2_FocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_office_name2_FocusGained
        // TODO add your handling code here:
        office_name2_.selectAll();
    }//GEN-LAST:event_office_name2_FocusGained

    private void office_name2_KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_office_name2_KeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            office_name2_.transferFocus();
            evt.consume();
            eda_office_id_.requestFocus();
        }
    }//GEN-LAST:event_office_name2_KeyPressed

    private void dis_office_id_FocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_dis_office_id_FocusGained
        // TODO add your handling code here:
        dis_office_id_.selectAll();
    }//GEN-LAST:event_dis_office_id_FocusGained

    private void dis_office_id_FocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_dis_office_id_FocusLost
        // TODO add your handling code here:
        try {
            int id = Integer.valueOf(dis_office_id_.getText());

            if (id >= 0) {
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(
                     BillDetailsListInput.this,
                     "コードは数字のみ入力出来ます。",
                     "確認",
                     JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_dis_office_id_FocusLost

    private void dis_office_id_KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_dis_office_id_KeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            dis_office_id_.transferFocus();
            evt.consume();
        }
    }//GEN-LAST:event_dis_office_id_KeyPressed

    private void advances_FocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_advances_FocusLost
        // TODO add your handling code here:
    }//GEN-LAST:event_advances_FocusLost

    private void goods_id_MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_goods_id_MouseClicked
        if (evt.getClickCount() == 2) {
            showGoodsSelector();
        }

    }//GEN-LAST:event_goods_id_MouseClicked

    private void eda_office_id_FocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_eda_office_id_FocusGained
        // TODO add your handling code here:
    }//GEN-LAST:event_eda_office_id_FocusGained

    private void eda_office_id_KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_eda_office_id_KeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            goods_id_.requestFocus();
        }
    }//GEN-LAST:event_eda_office_id_KeyPressed

    private void btnCalc_KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_btnCalc_KeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            //金額再計算
            doCalc();
            btnCalc_.transferFocus();
            evt.consume();
        }
    }//GEN-LAST:event_btnCalc_KeyPressed

    private void showGoodsSelector() {
        GoodsSelector selector = new GoodsSelector(con);
        switch (selector.showSelector(btnClose_)) {
            case GoodsSelector.SELECTED:
                Map<String, Object> item = selector.selectedRow();
                goods_id_.setText(String.valueOf((Integer) item.get("id")));
                bill_name_.setText((String) item.get("bill_name"));
                break;
            default:
        }
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField advances_;
    private javax.swing.JComboBox b_class_cmb_;
    private javax.swing.JTextField bill_name_;
    private javax.swing.JTextField bill_price_;
    private javax.swing.JTextField bill_unit_;
    private javax.swing.JTextField bill_value_;
    private javax.swing.JButton btnCalc_;
    private javax.swing.JButton btnCancel_;
    private javax.swing.JButton btnClose_;
    private javax.swing.JButton btnRegist_;
    private javax.swing.JPanel calendar_p1;
    private javax.swing.JPanel calendar_p2;
    private javax.swing.JTextField dis_office_id_;
    private javax.swing.JTextField eda_office_id_;
    private javax.swing.JTextField goods_id_;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JTextField office_name2_;
    private javax.swing.JTextField remark01_;
    private javax.swing.JTextField unit_price_;
    // End of variables declaration//GEN-END:variables

    private void doCalc() {

        double bill_value = Double.valueOf(bill_value_.getText());
        double unit_price = Double.valueOf(unit_price_.getText());
        Long bill_price = Math.round(bill_value * (unit_price));

        bill_price_.setText(String.valueOf(bill_price));
    }

    private void doClose() {
        Window w = SwingUtilities.getWindowAncestor(this);
        if (w instanceof Frame) {
            w.dispatchEvent(new WindowEvent(w, WindowEvent.WINDOW_CLOSING));
        } else {
            w.dispose();
        }
    }

    private void updateColumns() {
        if (termDateField1_.getValue() != null) {
            columns_.put("term_date", DBManager.convert(termDateField1_.getValue().getTime()));
        }
        if (termDateField2_.getValue() != null) {
            columns_.put("term_month", DBManager.convert(termDateField2_.getValue().getTime()));
        }

        try {
            int i = Integer.valueOf(unit_price_.getText());
            columns_.put("unit_price", i);
        } catch (Exception e) {
        }

        columns_.put("bill_name", bill_name_.getText());

        try {
            double d = Double.valueOf(bill_value_.getText());
            columns_.put("bill_value", d);
        } catch (Exception e) {

        }

        columns_.put("bill_unit", bill_unit_.getText());

        try {
            double d = Double.valueOf(unit_price_.getText());
            columns_.put("unit_price", d);
        } catch (Exception e) {

        }

        try {
            double d = Double.valueOf(advances_.getText());
            columns_.put("advances", d);
        } catch (Exception e) {

        }

        try {
            int i = Integer.valueOf(bill_price_.getText());
            columns_.put("bill_price", i);
        } catch (Exception e) {

        }

        columns_.put("office_name2", office_name2_.getText());
        columns_.put("remark01", remark01_.getText());

        try {
            int i = Integer.valueOf(eda_office_id_.getText());
            columns_.put("eda_office_id", i);
        } catch (Exception e) {

        }

        if (0 > row) {
            //新規なら
            getBillDetailData(bill_sno);
        }
        columns_.put("goods_id", Integer.valueOf(goods_id_.getText()));

        columns_.put("b_class_id", Integer.valueOf(bclassName2Array[0][b_class_cmb_.getSelectedIndex()]));
        columns_.put("b_class_name", bclassName2Array[1][b_class_cmb_.getSelectedIndex()]);

        columns_.put("dis_office_id", Integer.valueOf(dis_office_id_.getText()));
        columns_.put("office_name2", office_name2_.getText());

        columns_.put("start_date", BillDetailsList.start_date_);
        columns_.put("end_date", BillDetailsList.end_date_);

    }

    private void doRegist() {

        // 画面の変更内容をカラムに反映
        updateColumns();

        if (add_flag) {
            if (model.insert(columns_)) {
                //少し待つ
                try {
                    Thread.sleep(100);
                } catch (Exception e) {
                }

                doClose();

            } else {
                //少し待つ
                try {
                    Thread.sleep(100);
                } catch (Exception e) {
                }

                JOptionPane.showMessageDialog(
                         BillDetailsListInput.this,
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

                doClose();

            } else {
                //少し待つ
                try {
                    Thread.sleep(100);
                } catch (Exception e) {
                }
                JOptionPane.showMessageDialog(
                         BillDetailsListInput.this,
                         "登録に失敗しました。",
                         "確認",
                         JOptionPane.ERROR_MESSAGE);
            }
        }

    }

    private Object[] getGoodsName(int id) {
        Object[] goods_ = new Object[7];
        //品名検索
        try {

            String s = "select * from v0_goods_kind "
                     + " where v0_goods_kind.id = ?;";

            PreparedStatement ps = con.prepareStatement(s);
            try {
                ps.setObject(1, id);
                ResultSet rs = ps.executeQuery();

                if (rs.next()) {
                    goods_[0] = rs.getInt("id");
                    goods_[1] = rs.getString("bill_name");
                    goods_[2] = rs.getString("bill_name2");
                    goods_[3] = rs.getInt("b_class_id");
                    goods_[4] = rs.getString("unit_name");
                    goods_[5] = rs.getInt("unit_value");
                    goods_[6] = rs.getInt("trans_unit_value");
                } else {
                    goods_[0] = 0;
                    goods_[1] = "";
                    goods_[2] = "";
                    goods_[3] = 0;
                    goods_[4] = "";
                    goods_[5] = 0;
                    goods_[6] = 0;
                }

            } finally {
                ps.close();
            }
        } catch (SQLException e) {

        }
        return goods_;
    }
}
