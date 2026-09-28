package aks.industrialwaste.recycle.ui.SystemSetting;

import aks.industrialwaste.database.DBManager;
import java.awt.Dimension;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.awt.Font;
import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import javax.swing.JOptionPane;
import jp.co.astax.awt.FontUtility;
import jp.co.astax.swing.DateTimeField;

public class SystemSetting extends javax.swing.JPanel {

    private Connection con_;

    private DateTimeField permission_date1_, permission_date2_, permission_date3_, permission_date4_;

    private Object[] printerName_ = new String[10];

    public static int 税率;
    public static BigDecimal 産廃税額 = BigDecimal.ONE;

    private javax.swing.JTextField wasteTaxPerKg_;

    public SystemSetting(Connection con) {
        con_ = con;
        getprinter();
        initComponents();
        initWasteTaxSetting();

        Font font14 = FontUtility.getFont("メイリオ", Font.PLAIN, 14);

        permission_date1_ = new DateTimeField(DateTimeField.DateFormat.YMD);
        permission_date1_.setFont(font14);
        permission_date1_.setMinimumSize(new Dimension(130, 25));
        permission_date1_.setPreferredSize(new Dimension(130, 25));
        permission_date1_.setMinimumSize(permission_date1_.getPreferredSize());
        permission_date_pane1.add(permission_date1_);

        permission_date2_ = new DateTimeField(DateTimeField.DateFormat.YMD);
        permission_date2_.setFont(font14);
        permission_date2_.setMinimumSize(new Dimension(130, 25));
        permission_date2_.setPreferredSize(new Dimension(130, 25));
        permission_date2_.setMinimumSize(permission_date2_.getPreferredSize());
        permission_date_pane2.add(permission_date2_);

        permission_date3_ = new DateTimeField(DateTimeField.DateFormat.YMD);
        permission_date3_.setFont(font14);
        permission_date3_.setMinimumSize(new Dimension(130, 25));
        permission_date3_.setPreferredSize(new Dimension(130, 25));
        permission_date3_.setMinimumSize(permission_date3_.getPreferredSize());
        permission_date_pane3.add(permission_date3_);

        permission_date4_ = new DateTimeField(DateTimeField.DateFormat.YMD);
        permission_date4_.setFont(font14);
        permission_date4_.setMinimumSize(new Dimension(130, 25));
        permission_date4_.setPreferredSize(new Dimension(130, 25));
        permission_date4_.setMinimumSize(permission_date4_.getPreferredSize());
        permission_date_pane4.add(permission_date4_);

        updateView();

    }

    /**
     * 消費税率の下に、1kg当たりの産廃税額入力欄を追加します。
     * NetBeansの生成領域外で構築し、既存フォームへの影響を抑えています。
     */
    private void initWasteTaxSetting() {
        Font font14 = FontUtility.getFont("メイリオ", Font.PLAIN, 14);

        jPanel12.setBorder(javax.swing.BorderFactory.createTitledBorder(
                 null,
                 "【税設定】",
                 javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION,
                 javax.swing.border.TitledBorder.DEFAULT_POSITION,
                 FontUtility.getFont("メイリオ", Font.PLAIN, 18)));
        jPanel12.setMaximumSize(new Dimension(300, 130));
        jPanel12.setMinimumSize(new Dimension(300, 130));
        jPanel12.setPreferredSize(new Dimension(300, 130));
        jLabel12.setText("消費税率");

        javax.swing.JLabel label = new javax.swing.JLabel("1kg当たりの産廃税額");
        label.setFont(font14);
        java.awt.GridBagConstraints constraints = new java.awt.GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridy = 1;
        constraints.anchor = java.awt.GridBagConstraints.EAST;
        constraints.insets = new java.awt.Insets(8, 2, 0, 2);
        jPanel12.add(label, constraints);

        wasteTaxPerKg_ = new javax.swing.JTextField("1");
        wasteTaxPerKg_.setFont(font14);
        wasteTaxPerKg_.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        wasteTaxPerKg_.setMaximumSize(new Dimension(70, 28));
        wasteTaxPerKg_.setMinimumSize(new Dimension(70, 28));
        wasteTaxPerKg_.setPreferredSize(new Dimension(70, 28));
        constraints = new java.awt.GridBagConstraints();
        constraints.gridx = 1;
        constraints.gridy = 1;
        constraints.anchor = java.awt.GridBagConstraints.WEST;
        constraints.insets = new java.awt.Insets(8, 0, 0, 0);
        jPanel12.add(wasteTaxPerKg_, constraints);

        javax.swing.JLabel unitLabel = new javax.swing.JLabel("円/kg");
        unitLabel.setFont(font14);
        constraints = new java.awt.GridBagConstraints();
        constraints.gridx = 2;
        constraints.gridy = 1;
        constraints.anchor = java.awt.GridBagConstraints.WEST;
        constraints.insets = new java.awt.Insets(8, 2, 0, 2);
        jPanel12.add(unitLabel, constraints);
    }

    private boolean validateTaxSettings() {
        try {
            int taxRate = Integer.parseInt(tax_.getText().trim());
            if (taxRate < 0 || taxRate > 100) {
                throw new NumberFormatException();
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(
                     this,
                     "消費税率は0～100の半角整数で入力してください。",
                     "入力エラー",
                     JOptionPane.WARNING_MESSAGE);
            tax_.requestFocusInWindow();
            return false;
        }

        try {
            BigDecimal wasteTax = new BigDecimal(wasteTaxPerKg_.getText().trim());
            if (wasteTax.compareTo(BigDecimal.ZERO) < 0
                     || wasteTax.compareTo(new BigDecimal("99999999.99")) > 0
                     || wasteTax.scale() > 2) {
                throw new NumberFormatException();
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(
                     this,
                     "産廃税額は0以上の半角数字で入力してください。\n小数は2桁まで入力できます。",
                     "入力エラー",
                     JOptionPane.WARNING_MESSAGE);
            wasteTaxPerKg_.requestFocusInWindow();
            return false;
        }
        return true;
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        jButton1 = new javax.swing.JButton();
        jLabel_Title = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jPanel1 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        company_name2 = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        company_name1 = new javax.swing.JTextField();
        jLabel21 = new javax.swing.JLabel();
        company_name4 = new javax.swing.JTextField();
        jLabel22 = new javax.swing.JLabel();
        jLabel23 = new javax.swing.JLabel();
        company_name3 = new javax.swing.JTextField();
        company_name5 = new javax.swing.JTextField();
        trans_name3 = new javax.swing.JTextField();
        trans_name2 = new javax.swing.JTextField();
        trans_name4 = new javax.swing.JTextField();
        jLabel24 = new javax.swing.JLabel();
        jLabel25 = new javax.swing.JLabel();
        trans_name1 = new javax.swing.JTextField();
        jPanel2 = new javax.swing.JPanel();
        jPanel16 = new javax.swing.JPanel();
        jPanel7 = new javax.swing.JPanel();
        permission_no_ = new javax.swing.JTextField();
        permission_no_trans1_ = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        permission_date_pane2 = new javax.swing.JPanel();
        jLabel7 = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        permission_no_trans2_ = new javax.swing.JTextField();
        permission_no2_ = new javax.swing.JTextField();
        jLabel20 = new javax.swing.JLabel();
        permission_date_pane1 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        permission_date_pane3 = new javax.swing.JPanel();
        jLabel8 = new javax.swing.JLabel();
        permission_date_pane4 = new javax.swing.JPanel();
        jPanel12 = new javax.swing.JPanel();
        tax_ = new javax.swing.JTextField();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        regist_btn = new javax.swing.JButton();
        jLabel14 = new javax.swing.JLabel();
        jPanel14 = new javax.swing.JPanel();
        jPanel9 = new javax.swing.JPanel();
        bill_print_flag_ = new javax.swing.JCheckBox();
        manifesto_print_flag_ = new javax.swing.JCheckBox();
        jPanel10 = new javax.swing.JPanel();
        printerSelect1 = new javax.swing.JComboBox(printerName_);
        printerSelect2 = new javax.swing.JComboBox(printerName_);
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        printerSelect3 = new javax.swing.JComboBox(printerName_);
        jLabel15 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        printerSelect4 = new javax.swing.JComboBox(printerName_);

        jButton1.setFont(new java.awt.Font("SansSerif", 1, 14)); // NOI18N
        jButton1.setText("更新");
        jButton1.setMaximumSize(new java.awt.Dimension(100, 30));
        jButton1.setMinimumSize(new java.awt.Dimension(100, 30));
        jButton1.setPreferredSize(new java.awt.Dimension(100, 30));
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        setBorder(javax.swing.BorderFactory.createEtchedBorder());
        setMaximumSize(new java.awt.Dimension(1200, 800));
        setMinimumSize(new java.awt.Dimension(1200, 800));
        setPreferredSize(new java.awt.Dimension(1200, 800));
        setLayout(new java.awt.GridBagLayout());

        jLabel_Title.setBackground(java.awt.Color.blue);
        jLabel_Title.setFont(new java.awt.Font("MS UI Gothic", 1, 18)); // NOI18N
        jLabel_Title.setForeground(new java.awt.Color(255, 255, 255));
        jLabel_Title.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel_Title.setText("システム設定");
        jLabel_Title.setFocusable(false);
        jLabel_Title.setMaximumSize(new java.awt.Dimension(1250, 50));
        jLabel_Title.setMinimumSize(new java.awt.Dimension(1250, 40));
        jLabel_Title.setOpaque(true);
        jLabel_Title.setPreferredSize(new java.awt.Dimension(1250, 40));
        jLabel_Title.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jLabel_TitleMousePressed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        add(jLabel_Title, gridBagConstraints);

        jScrollPane1.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        jScrollPane1.setMaximumSize(new java.awt.Dimension(1250, 900));
        jScrollPane1.setMinimumSize(new java.awt.Dimension(1250, 600));
        jScrollPane1.setPreferredSize(new java.awt.Dimension(1250, 750));

        jPanel1.setMaximumSize(new java.awt.Dimension(1200, 750));
        jPanel1.setMinimumSize(new java.awt.Dimension(1200, 750));
        jPanel1.setPreferredSize(new java.awt.Dimension(1200, 750));
        jPanel1.setLayout(new java.awt.GridBagLayout());

        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "【企業情報】", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("メイリオ", 0, 18))); // NOI18N
        jPanel3.setFont(new java.awt.Font("MS UI Gothic", 1, 18)); // NOI18N
        jPanel3.setMaximumSize(new java.awt.Dimension(450, 300));
        jPanel3.setMinimumSize(new java.awt.Dimension(450, 300));
        jPanel3.setPreferredSize(new java.awt.Dimension(450, 300));
        jPanel3.setLayout(new java.awt.GridBagLayout());

        company_name2.setBackground(new java.awt.Color(255, 255, 204));
        company_name2.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        company_name2.setMaximumSize(new java.awt.Dimension(355, 30));
        company_name2.setMinimumSize(new java.awt.Dimension(355, 30));
        company_name2.setPreferredSize(new java.awt.Dimension(355, 30));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        jPanel3.add(company_name2, gridBagConstraints);

        jLabel5.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel5.setText("会社名");
        jLabel5.setMaximumSize(new java.awt.Dimension(80, 20));
        jLabel5.setMinimumSize(new java.awt.Dimension(80, 20));
        jLabel5.setPreferredSize(new java.awt.Dimension(80, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        jPanel3.add(jLabel5, gridBagConstraints);

        company_name1.setBackground(new java.awt.Color(255, 255, 204));
        company_name1.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        company_name1.setMaximumSize(new java.awt.Dimension(355, 30));
        company_name1.setMinimumSize(new java.awt.Dimension(355, 30));
        company_name1.setPreferredSize(new java.awt.Dimension(355, 30));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        jPanel3.add(company_name1, gridBagConstraints);

        jLabel21.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        jLabel21.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel21.setText("振込銀行");
        jLabel21.setMaximumSize(new java.awt.Dimension(80, 20));
        jLabel21.setMinimumSize(new java.awt.Dimension(80, 20));
        jLabel21.setPreferredSize(new java.awt.Dimension(80, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        jPanel3.add(jLabel21, gridBagConstraints);

        company_name4.setBackground(new java.awt.Color(255, 255, 204));
        company_name4.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        company_name4.setMaximumSize(new java.awt.Dimension(355, 30));
        company_name4.setMinimumSize(new java.awt.Dimension(355, 30));
        company_name4.setPreferredSize(new java.awt.Dimension(355, 30));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        jPanel3.add(company_name4, gridBagConstraints);

        jLabel22.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        jLabel22.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel22.setText("所在地");
        jLabel22.setMaximumSize(new java.awt.Dimension(80, 20));
        jLabel22.setMinimumSize(new java.awt.Dimension(80, 20));
        jLabel22.setPreferredSize(new java.awt.Dimension(80, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        jPanel3.add(jLabel22, gridBagConstraints);

        jLabel23.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        jLabel23.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel23.setText("代表者");
        jLabel23.setMaximumSize(new java.awt.Dimension(80, 20));
        jLabel23.setMinimumSize(new java.awt.Dimension(80, 20));
        jLabel23.setPreferredSize(new java.awt.Dimension(80, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        jPanel3.add(jLabel23, gridBagConstraints);

        company_name3.setBackground(new java.awt.Color(255, 255, 204));
        company_name3.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        company_name3.setMaximumSize(new java.awt.Dimension(355, 30));
        company_name3.setMinimumSize(new java.awt.Dimension(355, 30));
        company_name3.setPreferredSize(new java.awt.Dimension(355, 30));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        jPanel3.add(company_name3, gridBagConstraints);

        company_name5.setBackground(new java.awt.Color(255, 255, 204));
        company_name5.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        company_name5.setMaximumSize(new java.awt.Dimension(355, 30));
        company_name5.setMinimumSize(new java.awt.Dimension(355, 30));
        company_name5.setPreferredSize(new java.awt.Dimension(355, 30));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        jPanel3.add(company_name5, gridBagConstraints);

        trans_name3.setBackground(new java.awt.Color(255, 255, 204));
        trans_name3.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        trans_name3.setMaximumSize(new java.awt.Dimension(200, 30));
        trans_name3.setMinimumSize(new java.awt.Dimension(200, 30));
        trans_name3.setPreferredSize(new java.awt.Dimension(200, 30));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        jPanel3.add(trans_name3, gridBagConstraints);

        trans_name2.setBackground(new java.awt.Color(255, 255, 204));
        trans_name2.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        trans_name2.setMaximumSize(new java.awt.Dimension(150, 30));
        trans_name2.setMinimumSize(new java.awt.Dimension(150, 30));
        trans_name2.setPreferredSize(new java.awt.Dimension(150, 30));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        jPanel3.add(trans_name2, gridBagConstraints);

        trans_name4.setBackground(new java.awt.Color(255, 255, 204));
        trans_name4.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        trans_name4.setMaximumSize(new java.awt.Dimension(150, 30));
        trans_name4.setMinimumSize(new java.awt.Dimension(150, 30));
        trans_name4.setPreferredSize(new java.awt.Dimension(150, 30));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        jPanel3.add(trans_name4, gridBagConstraints);

        jLabel24.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        jLabel24.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel24.setText("電話番号");
        jLabel24.setMaximumSize(new java.awt.Dimension(80, 20));
        jLabel24.setMinimumSize(new java.awt.Dimension(80, 20));
        jLabel24.setPreferredSize(new java.awt.Dimension(80, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        jPanel3.add(jLabel24, gridBagConstraints);

        jLabel25.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        jLabel25.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel25.setText("FAX番号");
        jLabel25.setMaximumSize(new java.awt.Dimension(80, 20));
        jLabel25.setMinimumSize(new java.awt.Dimension(80, 20));
        jLabel25.setPreferredSize(new java.awt.Dimension(80, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        jPanel3.add(jLabel25, gridBagConstraints);

        trans_name1.setBackground(new java.awt.Color(255, 255, 204));
        trans_name1.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        trans_name1.setMaximumSize(new java.awt.Dimension(200, 30));
        trans_name1.setMinimumSize(new java.awt.Dimension(200, 30));
        trans_name1.setPreferredSize(new java.awt.Dimension(200, 30));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        jPanel3.add(trans_name1, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weighty = 1.0;
        jPanel3.add(jPanel2, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 30, 2, 2);
        jPanel1.add(jPanel3, gridBagConstraints);

        jPanel16.setMaximumSize(new java.awt.Dimension(550, 300));
        jPanel16.setMinimumSize(new java.awt.Dimension(550, 300));
        jPanel16.setPreferredSize(new java.awt.Dimension(550, 300));
        jPanel16.setLayout(new java.awt.GridBagLayout());

        jPanel7.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "【許可情報】", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("メイリオ", 0, 18))); // NOI18N
        jPanel7.setFont(new java.awt.Font("MS UI Gothic", 1, 18)); // NOI18N
        jPanel7.setMaximumSize(new java.awt.Dimension(600, 450));
        jPanel7.setMinimumSize(new java.awt.Dimension(600, 450));
        jPanel7.setPreferredSize(new java.awt.Dimension(600, 450));
        jPanel7.setLayout(new java.awt.GridBagLayout());

        permission_no_.setBackground(new java.awt.Color(255, 255, 204));
        permission_no_.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        permission_no_.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        permission_no_.setMaximumSize(new java.awt.Dimension(130, 30));
        permission_no_.setMinimumSize(new java.awt.Dimension(130, 30));
        permission_no_.setPreferredSize(new java.awt.Dimension(130, 30));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 10, 2, 2);
        jPanel7.add(permission_no_, gridBagConstraints);

        permission_no_trans1_.setBackground(new java.awt.Color(255, 255, 204));
        permission_no_trans1_.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        permission_no_trans1_.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        permission_no_trans1_.setText("01234567890");
        permission_no_trans1_.setMaximumSize(new java.awt.Dimension(130, 30));
        permission_no_trans1_.setMinimumSize(new java.awt.Dimension(130, 30));
        permission_no_trans1_.setPreferredSize(new java.awt.Dimension(130, 30));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 10, 2, 2);
        jPanel7.add(permission_no_trans1_, gridBagConstraints);

        jLabel1.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel1.setText("許可年月日3");
        jLabel1.setMaximumSize(new java.awt.Dimension(80, 22));
        jLabel1.setMinimumSize(new java.awt.Dimension(80, 22));
        jLabel1.setPreferredSize(new java.awt.Dimension(80, 22));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.insets = new java.awt.Insets(2, 10, 2, 2);
        jPanel7.add(jLabel1, gridBagConstraints);

        jLabel2.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel2.setText("<html>許可番号<br>(処分2)</html>");
        jLabel2.setMaximumSize(new java.awt.Dimension(80, 44));
        jLabel2.setMinimumSize(new java.awt.Dimension(80, 44));
        jLabel2.setPreferredSize(new java.awt.Dimension(80, 44));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.insets = new java.awt.Insets(2, 10, 2, 2);
        jPanel7.add(jLabel2, gridBagConstraints);

        jLabel3.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel3.setText("<html>許可番号<br>(運搬2)</html>");
        jLabel3.setMaximumSize(new java.awt.Dimension(80, 44));
        jLabel3.setMinimumSize(new java.awt.Dimension(80, 44));
        jLabel3.setPreferredSize(new java.awt.Dimension(80, 44));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.insets = new java.awt.Insets(5, 10, 2, 2);
        jPanel7.add(jLabel3, gridBagConstraints);

        permission_date_pane2.setMaximumSize(new java.awt.Dimension(270, 30));
        permission_date_pane2.setMinimumSize(new java.awt.Dimension(270, 30));
        permission_date_pane2.setPreferredSize(new java.awt.Dimension(270, 30));
        permission_date_pane2.setLayout(new javax.swing.BoxLayout(permission_date_pane2, javax.swing.BoxLayout.LINE_AXIS));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        jPanel7.add(permission_date_pane2, gridBagConstraints);

        jLabel7.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        jLabel7.setText("許可年月日・許可番号の設定をします");
        jLabel7.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        jLabel7.setMaximumSize(new java.awt.Dimension(300, 60));
        jLabel7.setMinimumSize(new java.awt.Dimension(150, 20));
        jLabel7.setPreferredSize(new java.awt.Dimension(150, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.gridwidth = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 10, 2);
        jPanel7.add(jLabel7, gridBagConstraints);

        jLabel19.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        jLabel19.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel19.setText("<html>許可番号<br>(運搬1)</html>");
        jLabel19.setMaximumSize(new java.awt.Dimension(80, 44));
        jLabel19.setMinimumSize(new java.awt.Dimension(80, 44));
        jLabel19.setPreferredSize(new java.awt.Dimension(80, 44));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.insets = new java.awt.Insets(5, 10, 2, 2);
        jPanel7.add(jLabel19, gridBagConstraints);

        permission_no_trans2_.setBackground(new java.awt.Color(255, 255, 204));
        permission_no_trans2_.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        permission_no_trans2_.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        permission_no_trans2_.setText("01234567890");
        permission_no_trans2_.setMaximumSize(new java.awt.Dimension(130, 30));
        permission_no_trans2_.setMinimumSize(new java.awt.Dimension(130, 30));
        permission_no_trans2_.setPreferredSize(new java.awt.Dimension(130, 30));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 10, 2, 2);
        jPanel7.add(permission_no_trans2_, gridBagConstraints);

        permission_no2_.setBackground(new java.awt.Color(255, 255, 204));
        permission_no2_.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        permission_no2_.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        permission_no2_.setMaximumSize(new java.awt.Dimension(130, 30));
        permission_no2_.setMinimumSize(new java.awt.Dimension(130, 30));
        permission_no2_.setPreferredSize(new java.awt.Dimension(130, 30));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 10, 2, 2);
        jPanel7.add(permission_no2_, gridBagConstraints);

        jLabel20.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        jLabel20.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel20.setText("<html>許可番号<br>(処分1)</html>");
        jLabel20.setMaximumSize(new java.awt.Dimension(80, 44));
        jLabel20.setMinimumSize(new java.awt.Dimension(80, 44));
        jLabel20.setPreferredSize(new java.awt.Dimension(80, 44));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.insets = new java.awt.Insets(2, 10, 2, 2);
        jPanel7.add(jLabel20, gridBagConstraints);

        permission_date_pane1.setMaximumSize(new java.awt.Dimension(270, 30));
        permission_date_pane1.setMinimumSize(new java.awt.Dimension(270, 30));
        permission_date_pane1.setPreferredSize(new java.awt.Dimension(270, 30));
        permission_date_pane1.setLayout(new javax.swing.BoxLayout(permission_date_pane1, javax.swing.BoxLayout.LINE_AXIS));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        jPanel7.add(permission_date_pane1, gridBagConstraints);

        jLabel4.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel4.setText("許可年月日1");
        jLabel4.setMaximumSize(new java.awt.Dimension(80, 22));
        jLabel4.setMinimumSize(new java.awt.Dimension(80, 22));
        jLabel4.setPreferredSize(new java.awt.Dimension(80, 22));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(2, 10, 2, 2);
        jPanel7.add(jLabel4, gridBagConstraints);

        jLabel6.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        jLabel6.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel6.setText("許可年月日4");
        jLabel6.setMaximumSize(new java.awt.Dimension(80, 22));
        jLabel6.setMinimumSize(new java.awt.Dimension(80, 22));
        jLabel6.setPreferredSize(new java.awt.Dimension(80, 22));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.insets = new java.awt.Insets(2, 10, 2, 2);
        jPanel7.add(jLabel6, gridBagConstraints);

        permission_date_pane3.setMaximumSize(new java.awt.Dimension(270, 30));
        permission_date_pane3.setMinimumSize(new java.awt.Dimension(270, 30));
        permission_date_pane3.setPreferredSize(new java.awt.Dimension(270, 30));
        permission_date_pane3.setLayout(new javax.swing.BoxLayout(permission_date_pane3, javax.swing.BoxLayout.LINE_AXIS));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        jPanel7.add(permission_date_pane3, gridBagConstraints);

        jLabel8.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        jLabel8.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel8.setText("許可年月日2");
        jLabel8.setMaximumSize(new java.awt.Dimension(80, 22));
        jLabel8.setMinimumSize(new java.awt.Dimension(80, 22));
        jLabel8.setPreferredSize(new java.awt.Dimension(80, 22));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.insets = new java.awt.Insets(2, 10, 2, 2);
        jPanel7.add(jLabel8, gridBagConstraints);

        permission_date_pane4.setMaximumSize(new java.awt.Dimension(270, 30));
        permission_date_pane4.setMinimumSize(new java.awt.Dimension(270, 30));
        permission_date_pane4.setPreferredSize(new java.awt.Dimension(270, 30));
        permission_date_pane4.setLayout(new javax.swing.BoxLayout(permission_date_pane4, javax.swing.BoxLayout.LINE_AXIS));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        jPanel7.add(permission_date_pane4, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 2, 2, 20);
        jPanel16.add(jPanel7, gridBagConstraints);

        jPanel12.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "【消費税設定】", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("メイリオ", 0, 18))); // NOI18N
        jPanel12.setFont(new java.awt.Font("MS UI Gothic", 1, 18)); // NOI18N
        jPanel12.setMaximumSize(new java.awt.Dimension(200, 120));
        jPanel12.setMinimumSize(new java.awt.Dimension(200, 120));
        jPanel12.setPreferredSize(new java.awt.Dimension(200, 130));
        jPanel12.setLayout(new java.awt.GridBagLayout());

        tax_.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        tax_.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        tax_.setText("10");
        tax_.setMaximumSize(new java.awt.Dimension(50, 28));
        tax_.setMinimumSize(new java.awt.Dimension(50, 28));
        tax_.setPreferredSize(new java.awt.Dimension(50, 28));
        tax_.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tax_ActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        jPanel12.add(tax_, gridBagConstraints);

        jLabel12.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        jLabel12.setText("税率設定");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        jPanel12.add(jLabel12, gridBagConstraints);

        jLabel13.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        jLabel13.setText("％");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        jPanel12.add(jLabel13, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 2, 2, 2);
        jPanel16.add(jPanel12, gridBagConstraints);

        regist_btn.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        regist_btn.setText("登　録");
        regist_btn.setMaximumSize(new java.awt.Dimension(100, 30));
        regist_btn.setMinimumSize(new java.awt.Dimension(150, 40));
        regist_btn.setPreferredSize(new java.awt.Dimension(150, 40));
        regist_btn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                regist_btnActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 30);
        jPanel16.add(regist_btn, gridBagConstraints);

        jLabel14.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        jLabel14.setText("設定変更時は、システムの再起動を行ってください。");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.SOUTHEAST;
        gridBagConstraints.insets = new java.awt.Insets(50, 2, 2, 30);
        jPanel16.add(jLabel14, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.gridheight = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        jPanel1.add(jPanel16, gridBagConstraints);

        jPanel14.setMaximumSize(new java.awt.Dimension(120, 170));
        jPanel14.setMinimumSize(new java.awt.Dimension(120, 170));
        jPanel14.setPreferredSize(new java.awt.Dimension(120, 170));
        jPanel14.setLayout(new java.awt.GridBagLayout());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 0.3;
        jPanel1.add(jPanel14, gridBagConstraints);

        jPanel9.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "【印刷設定】", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("メイリオ", 0, 18))); // NOI18N
        jPanel9.setFont(new java.awt.Font("MS UI Gothic", 1, 18)); // NOI18N
        jPanel9.setMaximumSize(new java.awt.Dimension(450, 150));
        jPanel9.setMinimumSize(new java.awt.Dimension(450, 120));
        jPanel9.setPreferredSize(new java.awt.Dimension(450, 120));
        jPanel9.setLayout(new java.awt.GridBagLayout());

        bill_print_flag_.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        bill_print_flag_.setText("請求書印刷時に直接印字する");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 5, 0);
        jPanel9.add(bill_print_flag_, gridBagConstraints);

        manifesto_print_flag_.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        manifesto_print_flag_.setText("マニフェスト印刷時に直接印字する");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 5, 0);
        jPanel9.add(manifesto_print_flag_, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(5, 30, 2, 2);
        jPanel1.add(jPanel9, gridBagConstraints);

        jPanel10.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "【プリンター設定】", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("メイリオ", 0, 18))); // NOI18N
        jPanel10.setFont(new java.awt.Font("MS UI Gothic", 1, 18)); // NOI18N
        jPanel10.setMaximumSize(new java.awt.Dimension(500, 200));
        jPanel10.setMinimumSize(new java.awt.Dimension(500, 200));
        jPanel10.setPreferredSize(new java.awt.Dimension(500, 200));
        jPanel10.setLayout(new java.awt.GridBagLayout());

        printerSelect1.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        printerSelect1.setMaximumSize(new java.awt.Dimension(250, 28));
        printerSelect1.setMinimumSize(new java.awt.Dimension(250, 28));
        printerSelect1.setPreferredSize(new java.awt.Dimension(250, 28));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        jPanel10.add(printerSelect1, gridBagConstraints);

        printerSelect2.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        printerSelect2.setMaximumSize(new java.awt.Dimension(250, 28));
        printerSelect2.setMinimumSize(new java.awt.Dimension(250, 28));
        printerSelect2.setPreferredSize(new java.awt.Dimension(250, 28));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        jPanel10.add(printerSelect2, gridBagConstraints);

        jLabel9.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        jLabel9.setText("計量票プリンター");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        jPanel10.add(jLabel9, gridBagConstraints);

        jLabel10.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        jLabel10.setText("マニフェスト用プリンター");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        jPanel10.add(jLabel10, gridBagConstraints);

        jLabel11.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        jLabel11.setText("使用するプリンターはスペースを含まないようにしてください");
        jLabel11.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        jLabel11.setMaximumSize(new java.awt.Dimension(450, 30));
        jLabel11.setMinimumSize(new java.awt.Dimension(450, 30));
        jLabel11.setPreferredSize(new java.awt.Dimension(450, 30));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 10, 2);
        jPanel10.add(jLabel11, gridBagConstraints);

        printerSelect3.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        printerSelect3.setMaximumSize(new java.awt.Dimension(250, 28));
        printerSelect3.setMinimumSize(new java.awt.Dimension(250, 28));
        printerSelect3.setPreferredSize(new java.awt.Dimension(250, 28));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        jPanel10.add(printerSelect3, gridBagConstraints);

        jLabel15.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        jLabel15.setText("帳票用プリンター");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        jPanel10.add(jLabel15, gridBagConstraints);

        jLabel16.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        jLabel16.setText("領収書用プリンター");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        jPanel10.add(jLabel16, gridBagConstraints);

        printerSelect4.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        printerSelect4.setMaximumSize(new java.awt.Dimension(250, 28));
        printerSelect4.setMinimumSize(new java.awt.Dimension(250, 28));
        printerSelect4.setPreferredSize(new java.awt.Dimension(250, 28));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        jPanel10.add(printerSelect4, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 30, 2, 2);
        jPanel1.add(jPanel10, gridBagConstraints);

        jScrollPane1.setViewportView(jPanel1);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        add(jScrollPane1, gridBagConstraints);
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:

    }//GEN-LAST:event_jButton1ActionPerformed

    private void regist_btnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_regist_btnActionPerformed
        // TODO add your handling code here:
        if (!validateTaxSettings()) {
            return;
        }
        if (update() == 0) {
            //マニフェストマスタ登録成功
            JOptionPane.showMessageDialog(
                     this,
                     "登録しました。",
                     "情報",
                     JOptionPane.INFORMATION_MESSAGE);
        } else {
            //マニフェストマスタ登録失敗
            JOptionPane.showMessageDialog(
                     this,
                     "<html>登録失敗しました。<br>内容を確認してください。</html>",
                     "情報",
                     JOptionPane.INFORMATION_MESSAGE);
        }
    }//GEN-LAST:event_regist_btnActionPerformed

    private void jLabel_TitleMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel_TitleMousePressed

}//GEN-LAST:event_jLabel_TitleMousePressed

    private void tax_ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tax_ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_tax_ActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JCheckBox bill_print_flag_;
    private javax.swing.JTextField company_name1;
    private javax.swing.JTextField company_name2;
    private javax.swing.JTextField company_name3;
    private javax.swing.JTextField company_name4;
    private javax.swing.JTextField company_name5;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JLabel jLabel_Title;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel14;
    private javax.swing.JPanel jPanel16;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JCheckBox manifesto_print_flag_;
    private javax.swing.JPanel permission_date_pane1;
    private javax.swing.JPanel permission_date_pane2;
    private javax.swing.JPanel permission_date_pane3;
    private javax.swing.JPanel permission_date_pane4;
    private javax.swing.JTextField permission_no2_;
    private javax.swing.JTextField permission_no_;
    private javax.swing.JTextField permission_no_trans1_;
    private javax.swing.JTextField permission_no_trans2_;
    private javax.swing.JComboBox printerSelect1;
    private javax.swing.JComboBox printerSelect2;
    private javax.swing.JComboBox printerSelect3;
    private javax.swing.JComboBox printerSelect4;
    private javax.swing.JButton regist_btn;
    private javax.swing.JTextField tax_;
    private javax.swing.JTextField trans_name1;
    private javax.swing.JTextField trans_name2;
    private javax.swing.JTextField trans_name3;
    private javax.swing.JTextField trans_name4;
    // End of variables declaration//GEN-END:variables

    private void getprinter() {

        PrintService ps[] = PrintServiceLookup.lookupPrintServices(null, null);
        int i = 0;
        printerName_ = new Object[ps.length];
        while (i < ps.length) {
            printerName_[i] = ps[i].getName();
            i++;
        }
    }

    public void updateView() {

        //会社名称他
        try {

            String s = "select * from m_manifesto "
                     + " where m_manifesto.sno = 1;";

            PreparedStatement ps = con_.prepareStatement(s);
            try {
                ResultSet rs = ps.executeQuery();
                // 初期化
                company_name1.setText("");
                company_name2.setText("");
                company_name3.setText("");
                company_name4.setText("");
                company_name5.setText("");
                permission_no_.setText("");
                permission_no2_.setText("");
                permission_no_trans1_.setText("");
                permission_no_trans2_.setText("");

                trans_name1.setText("");
                trans_name2.setText("");
                trans_name3.setText("");
                trans_name4.setText("");

                if (rs.next()) {

                    // 会社名称および処分の受託（受領）
                    company_name1.setText(rs.getString("company_name"));
                    company_name2.setText(rs.getString("trust_name1"));
                    company_name3.setText(rs.getString("trust_name2"));
                    company_name4.setText(rs.getString("trust_name3"));
                    company_name5.setText(rs.getString("trust_name4"));
                    //マニフェスト印刷時設定
                    manifesto_print_flag_.setSelected(rs.getBoolean("manifesto_print_flag"));
                    //bill_print_flag_
                    bill_print_flag_.setSelected(rs.getBoolean("bill_print_flag"));
                    permission_no_.setText(rs.getString("permission_no"));
                    permission_no2_.setText(rs.getString("permission_no2"));
                    permission_no_trans1_.setText(rs.getString("permission_no_trans"));
                    permission_no_trans2_.setText(rs.getString("permission_no_trans2"));

                    permission_date1_.setValue(DBManager.convert(rs.getTimestamp("permission_date")));
                    permission_date2_.setValue(DBManager.convert(rs.getTimestamp("permission_date2")));
                    permission_date3_.setValue(DBManager.convert(rs.getTimestamp("permission_date3")));
                    permission_date4_.setValue(DBManager.convert(rs.getTimestamp("permission_date4")));

                    trans_name1.setText(rs.getString("trans_name1"));
                    trans_name2.setText(rs.getString("trans_name2"));
                    trans_name3.setText(rs.getString("trans_name3"));
                    trans_name4.setText(rs.getString("trans_name4"));
                }
                s = "select * from m_system "
                         + " where m_system.sno = 1;";

                ps = con_.prepareStatement(s);
                rs = ps.executeQuery();
                if (rs.next()) {
                    //帳票用
                    printerSelect1.setSelectedItem(rs.getString("default_printer"));
                    //マニフェスト用
                    printerSelect2.setSelectedItem(rs.getString("manifesto_printer"));
                    //計量票用
                    printerSelect3.setSelectedItem(rs.getString("weigh_printer"));
                    //領収書用
                    printerSelect4.setSelectedItem(rs.getString("領収書_printer"));
                    //消費税率
                    tax_.setText(rs.getString("tax"));
                    //1kg当たり産廃税額
                    BigDecimal wasteTax = rs.getBigDecimal("waste_tax_per_kg");
                    if (wasteTax == null) {
                        wasteTax = BigDecimal.ONE;
                    }
                    wasteTaxPerKg_.setText(wasteTax.stripTrailingZeros().toPlainString());
                    税率 = rs.getInt("tax");
                    産廃税額 = wasteTax;

                }

            } finally {
                ps.close();
            }
        } catch (SQLException e) {
            System.out.println(e.toString());
        }

    }

    public int update() {
        // データ更新
        int ret = 0;
        try {
            Statement st = con_.createStatement();
            st.executeUpdate("begin;");
            try {
                String s = "update m_manifesto set "
                         + "company_name = ?, trust_name1 = ?, trust_name2 = ?, trust_name3 = ?, "
                         + "permission_date = ?::date, permission_date2 = ?::date, "
                         + "permission_no = ? , permission_no_trans = ?, "
                         + "permission_no2 = ? , permission_no_trans2 = ?, "
                         + "manifesto_print_flag = ?, "
                         + "trust_name4 = ?, "
                         + "trans_name1 = ?, "
                         + "trans_name2 = ?, "
                         + "trans_name3 = ?, "
                         + "trans_name4 = ?, "
                         + "bill_print_flag = ?, "
                         + "permission_date3 = ?, "
                         + "permission_date4 = ? "
                         + "where m_manifesto.sno = 1 ; ";

                PreparedStatement ps = con_.prepareStatement(s);
                try {

                    //システム設定会社名他登録
                    ps.setObject(1, company_name1.getText());
                    ps.setObject(2, company_name2.getText());
                    ps.setObject(3, company_name3.getText());
                    ps.setObject(4, company_name4.getText());
                    ps.setObject(5, DBManager.convert(permission_date1_.getValue()));
                    ps.setObject(6, DBManager.convert(permission_date2_.getValue()));
                    ps.setObject(7, permission_no_.getText());
                    ps.setObject(8, permission_no_trans1_.getText());
                    ps.setObject(9, permission_no2_.getText());
                    ps.setObject(10, permission_no_trans2_.getText());
                    ps.setObject(11, manifesto_print_flag_.isSelected());
                    ps.setObject(12, company_name5.getText());
                    ps.setObject(13, trans_name1.getText());
                    ps.setObject(14, trans_name2.getText());
                    ps.setObject(15, trans_name3.getText());
                    ps.setObject(16, trans_name4.getText());
                    ps.setObject(17, bill_print_flag_.isSelected());
                    //permission_date3_
                    ps.setObject(18, DBManager.convert(permission_date3_.getValue()));
                    ps.setObject(19, DBManager.convert(permission_date4_.getValue()));

                    ps.executeUpdate();

                    //システム設定プリンター名登録
                    s = "update m_system set "
                             + "default_printer = ?, manifesto_printer = ?, weigh_printer = ?,領収書_printer = ?, "
                             + "tax = ?, waste_tax_per_kg = ? "
                             + "where m_system.sno = 1 ; ";

                    ps = con_.prepareStatement(s);
                    ps.setObject(1, printerSelect1.getSelectedItem().toString());
                    ps.setObject(2, printerSelect2.getSelectedItem().toString());
                    ps.setObject(3, printerSelect3.getSelectedItem().toString());
                    ps.setObject(4, printerSelect4.getSelectedItem().toString());
                    ps.setObject(5, Integer.valueOf(tax_.getText()));
                    ps.setObject(6, new BigDecimal(wasteTaxPerKg_.getText().trim()));

                    ps.executeUpdate();

                    税率 = Integer.valueOf(tax_.getText());
                    産廃税額 = new BigDecimal(wasteTaxPerKg_.getText().trim());

                } finally {
                    ps.close();
                }
                st.executeUpdate("commit;");

            } catch (SQLException e) {
                st.executeUpdate("rollback;");
                ret += 1;
                throw e;
            } finally {
                st.close();
            }
        } catch (Exception e) {
            System.out.print(e);
            ret += 1;
        }
        return ret;
    }
}
