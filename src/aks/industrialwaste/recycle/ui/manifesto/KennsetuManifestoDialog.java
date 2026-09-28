package aks.industrialwaste.recycle.ui.manifesto;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.*;
import javax.swing.*;
import aks.industrialwaste.recycle.ui.sql.SQLCommon;
import aks.industrialwaste.recycle.ui.weighlist.*;
import java.awt.Window;
import java.awt.Frame;
import java.text.DateFormat;
import java.sql.SQLException;
import aks.industrialwaste.database.DBManager;
import aks.industrialwaste.recycle.report.XlsxManifesto;
import java.awt.event.KeyEvent;

public class KennsetuManifestoDialog extends javax.swing.JPanel {

    private SQLCommon mSQLCommon;
    private Connection con_;

    //0:id 1:name 2:zip_code 3:tel 4:address
    public String[][] entName2Array = new String[7][];
    public String[][] officeName2Array = new String[7][];
    public String[][] transName2Array = new String[7][];
    public String[][] contName2Array = new String[13][];
    public String[][] methotName2Array = new String[6][];
    public String[][] contOfficeName2Array = new String[6][];
    public String[][] remarkName2Array = new String[2][];
    public String[][] grantName2Array = new String[2][];
    public String[][] maniName2Array = new String[10][];
    public String[][] carKindName2Array = new String[2][];
    //選択されている行の各idを取得する
    public int ent_id_ = 0;
    public int goods_id_ = 0;
    public int office_id_ = 0;
    public int trans1_id_ = 0;
    public int trans2_id_ = 0;
    public int cont_office_id_ = 0;
    public int cont_id_ = 0;
    public int car_kind1_id_ = 0;
    public int car_kind2_id_ = 0;
    public int methot_id_ = 0;
    //取得したidを要素とする配列のindexを取得する(表示のため)
    public int ent_id_index = 0;
    public int office_id_index = 0;
    public int trans1_id_index = 0;
    public int trans2_id_index = 0;
    public int cont_id_index = 4;
    public int methot_id_index = 0;
    public int cont_office_id_index = 0;
    public int car_kind1_id_index = 0;
    public int car_kind2_id_index = 0;
    public int row_ = -1;
    private WeighListTableModel model_;
    private int mode_;
    private int[] multi_select_;

    private Long sno_;

    public KennsetuManifestoDialog(WeighListTableModel model, int row, Connection con, int mode, int[] multi_select) {
        initComponents();
        model_ = model;
        con_ = con;
        row_ = row;
        mode_ = mode;// 0:空伝 1:通常

        multi_select_ = multi_select;

        if (mode_ == 0) {
            jLabel4.setVisible(true);
            pring_num_.setVisible(true);
            pring_num_.setValue(0);
            rejist_btn_kensetu.setVisible(false);
        } else {
            jLabel4.setVisible(false);
            pring_num_.setVisible(false);
            pring_num_.setValue(1);
            rejist_btn_kensetu.setVisible(true);
        }

        if (mode_ == 0) {
            sno_ = (Long) 0l;
        } else {
            sno_ = (Long) model_.getColumns(row_).get("sno");
        }

        mSQLCommon = new SQLCommon(DBManager.jdbc_, DBManager.getUser(), DBManager.getPassword());

        //最終処分を行った場所
        last_cont_cmb.setEditable(true);

        //運搬先の事業場
        mSQLCommon.GetMasterData4("v0_manifesto_3", contOfficeName2Array);
        String[] LastContName = new String[contOfficeName2Array[0].length];
        for (int i = 0; i <= contOfficeName2Array[0].length - 1; i++) {
            LastContName[i] = (String) contOfficeName2Array[1][i].toString() + "　"
                     + (String) contOfficeName2Array[2][i].toString() + (String) contOfficeName2Array[3][i].toString();
            if (((String) contOfficeName2Array[1][i].toString() + "　"
                     + (String) contOfficeName2Array[2][i].toString() + (String) contOfficeName2Array[3][i].toString())
                     .equals((String) model.getColumns(row_).get("last_office_name"))) {
                cont_office_id_index = i;
            }
        }
        last_cont_cmb.setModel(new javax.swing.DefaultComboBoxModel(LastContName));

        //最終処分終了日()
        if ((Date) model.getColumns(row_).get("disposal_date2") != null) {
            cont_date2_year.setText(String.format("%tY", (Date) model.getColumns(row_).get("disposal_date2")));
            cont_date2_month.setText(String.format("%tm", (Date) model.getColumns(row_).get("disposal_date2")));
            cont_date2_day.setText(String.format("%td", (Date) model.getColumns(row_).get("disposal_date2")));
        } else {
            cont_date2_year.setText("");
            cont_date2_month.setText("");
            cont_date2_day.setText("");
        }
        //空欄を選択しておく last_office_name
        if (!"".equals((String) model.getColumns(row_).get("last_office_name"))) {
            last_cont_cmb.setSelectedItem((String) model.getColumns(row_).get("last_office_name"));
        } else {
            last_cont_cmb.setSelectedIndex(0);
        }
        //２次マニフェスト情報
        if (!"".equals((String) model.getColumns(row_).get("secod_manifesto_no"))) {
            secod_manifesto_no_.setText((String) model.getColumns(row_).get("secod_manifesto_no"));
        } else {
            secod_manifesto_no_.setText("");
        }

        id_search_.setText(String.valueOf((Integer) model.getColumns(row_).get("manifesto3_id1")));
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        jPanel21 = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        last_cont_cmb = new javax.swing.JComboBox();
        print_btn_kensetu = new javax.swing.JButton();
        quit_btn_kensetu = new javax.swing.JButton();
        rejist_btn_kensetu = new javax.swing.JButton();
        jLabel4 = new javax.swing.JLabel();
        pring_num_ = new javax.swing.JSpinner();
        id_search_ = new aks.swing.JTextFieldHankaku();
        jLabel79 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jLabel77 = new javax.swing.JLabel();
        cont_date2_year = new aks.swing.JTextFieldHankaku();
        jLabel87 = new javax.swing.JLabel();
        cont_date2_month = new aks.swing.JTextFieldHankaku();
        jLabel97 = new javax.swing.JLabel();
        cont_date2_day = new aks.swing.JTextFieldHankaku();
        jLabel98 = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        jLabel128 = new javax.swing.JLabel();
        jLabel80 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        jLabel78 = new javax.swing.JLabel();
        secod_manifesto_no_ = new aks.swing.JTextFieldHankaku();

        setMaximumSize(new java.awt.Dimension(650, 135));
        setMinimumSize(new java.awt.Dimension(650, 135));
        setName(""); // NOI18N
        setPreferredSize(new java.awt.Dimension(650, 135));
        setLayout(new java.awt.GridBagLayout());

        jPanel21.setMaximumSize(new java.awt.Dimension(600, 170));
        jPanel21.setMinimumSize(new java.awt.Dimension(600, 170));
        jPanel21.setPreferredSize(new java.awt.Dimension(600, 170));
        jPanel21.setLayout(new java.awt.GridBagLayout());

        jPanel1.setMaximumSize(new java.awt.Dimension(600, 130));
        jPanel1.setMinimumSize(new java.awt.Dimension(600, 130));
        jPanel1.setPreferredSize(new java.awt.Dimension(600, 130));
        jPanel1.setLayout(new java.awt.GridBagLayout());

        last_cont_cmb.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        last_cont_cmb.setMinimumSize(new java.awt.Dimension(550, 27));
        last_cont_cmb.setPreferredSize(new java.awt.Dimension(650, 27));
        last_cont_cmb.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                last_cont_cmbItemStateChanged(evt);
            }
        });
        last_cont_cmb.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                last_cont_cmbActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 5;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 10);
        jPanel1.add(last_cont_cmb, gridBagConstraints);

        print_btn_kensetu.setFont(new java.awt.Font("メイリオ", 0, 18)); // NOI18N
        print_btn_kensetu.setText("印　字");
        print_btn_kensetu.setMinimumSize(new java.awt.Dimension(110, 30));
        print_btn_kensetu.setPreferredSize(new java.awt.Dimension(110, 30));
        print_btn_kensetu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                print_btn_kensetuActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        jPanel1.add(print_btn_kensetu, gridBagConstraints);

        quit_btn_kensetu.setFont(new java.awt.Font("メイリオ", 0, 18)); // NOI18N
        quit_btn_kensetu.setText("閉じる");
        quit_btn_kensetu.setMinimumSize(new java.awt.Dimension(110, 30));
        quit_btn_kensetu.setPreferredSize(new java.awt.Dimension(110, 30));
        quit_btn_kensetu.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                quit_btn_kensetuMousePressed(evt);
            }
        });
        quit_btn_kensetu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                quit_btn_kensetuActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 10);
        jPanel1.add(quit_btn_kensetu, gridBagConstraints);

        rejist_btn_kensetu.setFont(new java.awt.Font("メイリオ", 0, 18)); // NOI18N
        rejist_btn_kensetu.setText("登　録");
        rejist_btn_kensetu.setMinimumSize(new java.awt.Dimension(110, 30));
        rejist_btn_kensetu.setPreferredSize(new java.awt.Dimension(110, 30));
        rejist_btn_kensetu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rejist_btn_kensetuActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        jPanel1.add(rejist_btn_kensetu, gridBagConstraints);

        jLabel4.setFont(new java.awt.Font("メイリオ", 0, 18)); // NOI18N
        jLabel4.setText("印刷部数");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 20, 0, 2);
        jPanel1.add(jLabel4, gridBagConstraints);

        pring_num_.setFont(new java.awt.Font("メイリオ", 0, 18)); // NOI18N
        pring_num_.setMinimumSize(new java.awt.Dimension(60, 28));
        pring_num_.setPreferredSize(new java.awt.Dimension(60, 28));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        jPanel1.add(pring_num_, gridBagConstraints);

        id_search_.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        id_search_.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        id_search_.setMaximumSize(new java.awt.Dimension(60, 25));
        id_search_.setMinimumSize(new java.awt.Dimension(60, 25));
        id_search_.setPreferredSize(new java.awt.Dimension(60, 25));
        id_search_.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                id_search_FocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                id_search_FocusLost(evt);
            }
        });
        id_search_.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                id_search_KeyPressed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        jPanel1.add(id_search_, gridBagConstraints);

        jLabel79.setFont(new java.awt.Font("メイリオ", 1, 14)); // NOI18N
        jLabel79.setText("   最終処分を行った場所 ");
        jLabel79.setMaximumSize(new java.awt.Dimension(170, 30));
        jLabel79.setMinimumSize(new java.awt.Dimension(170, 25));
        jLabel79.setOpaque(true);
        jLabel79.setPreferredSize(new java.awt.Dimension(170, 25));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        jPanel1.add(jLabel79, gridBagConstraints);

        jPanel2.setMaximumSize(new java.awt.Dimension(250, 30));
        jPanel2.setMinimumSize(new java.awt.Dimension(250, 30));
        jPanel2.setPreferredSize(new java.awt.Dimension(250, 30));
        jPanel2.setLayout(new java.awt.GridBagLayout());

        jLabel77.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        jLabel77.setText("（西暦）");
        jLabel77.setMaximumSize(new java.awt.Dimension(60, 25));
        jLabel77.setMinimumSize(new java.awt.Dimension(60, 25));
        jLabel77.setPreferredSize(new java.awt.Dimension(60, 25));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        jPanel2.add(jLabel77, gridBagConstraints);

        cont_date2_year.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        cont_date2_year.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        cont_date2_year.setMinimumSize(new java.awt.Dimension(60, 25));
        cont_date2_year.setPreferredSize(new java.awt.Dimension(60, 25));
        cont_date2_year.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                cont_date2_yearKeyPressed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        jPanel2.add(cont_date2_year, gridBagConstraints);

        jLabel87.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        jLabel87.setText("年");
        jLabel87.setMinimumSize(new java.awt.Dimension(15, 20));
        jLabel87.setPreferredSize(new java.awt.Dimension(15, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        jPanel2.add(jLabel87, gridBagConstraints);

        cont_date2_month.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        cont_date2_month.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        cont_date2_month.setMinimumSize(new java.awt.Dimension(25, 25));
        cont_date2_month.setPreferredSize(new java.awt.Dimension(30, 25));
        cont_date2_month.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                cont_date2_monthKeyPressed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        jPanel2.add(cont_date2_month, gridBagConstraints);

        jLabel97.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        jLabel97.setText("月");
        jLabel97.setMinimumSize(new java.awt.Dimension(15, 20));
        jLabel97.setPreferredSize(new java.awt.Dimension(15, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        jPanel2.add(jLabel97, gridBagConstraints);

        cont_date2_day.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        cont_date2_day.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        cont_date2_day.setMinimumSize(new java.awt.Dimension(30, 25));
        cont_date2_day.setPreferredSize(new java.awt.Dimension(30, 25));
        cont_date2_day.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                cont_date2_dayKeyPressed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        jPanel2.add(cont_date2_day, gridBagConstraints);

        jLabel98.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        jLabel98.setText("日");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 6;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        jPanel2.add(jLabel98, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        jPanel1.add(jPanel2, gridBagConstraints);

        jPanel4.setMaximumSize(new java.awt.Dimension(100, 10));
        jPanel4.setMinimumSize(new java.awt.Dimension(100, 10));

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 10, Short.MAX_VALUE)
        );

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.gridwidth = 5;
        gridBagConstraints.weighty = 1.0;
        jPanel1.add(jPanel4, gridBagConstraints);

        jLabel128.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        jLabel128.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel128.setText("ｺｰﾄﾞ");
        jLabel128.setMaximumSize(new java.awt.Dimension(30, 20));
        jLabel128.setMinimumSize(new java.awt.Dimension(30, 20));
        jLabel128.setPreferredSize(new java.awt.Dimension(30, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 15, 0, 2);
        jPanel1.add(jLabel128, gridBagConstraints);

        jLabel80.setFont(new java.awt.Font("メイリオ", 1, 14)); // NOI18N
        jLabel80.setText("   最終処分日");
        jLabel80.setMaximumSize(new java.awt.Dimension(110, 30));
        jLabel80.setMinimumSize(new java.awt.Dimension(110, 30));
        jLabel80.setOpaque(true);
        jLabel80.setPreferredSize(new java.awt.Dimension(110, 30));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        jPanel1.add(jLabel80, gridBagConstraints);

        jPanel3.setLayout(new javax.swing.BoxLayout(jPanel3, javax.swing.BoxLayout.LINE_AXIS));

        jLabel78.setFont(new java.awt.Font("メイリオ", 1, 14)); // NOI18N
        jLabel78.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel78.setText("2次マニフェストNO");
        jLabel78.setMaximumSize(new java.awt.Dimension(150, 25));
        jLabel78.setMinimumSize(new java.awt.Dimension(150, 25));
        jLabel78.setOpaque(true);
        jLabel78.setPreferredSize(new java.awt.Dimension(150, 25));
        jPanel3.add(jLabel78);

        secod_manifesto_no_.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        secod_manifesto_no_.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        secod_manifesto_no_.setText("012345678901");
        secod_manifesto_no_.setMaximumSize(new java.awt.Dimension(150, 25));
        secod_manifesto_no_.setMinimumSize(new java.awt.Dimension(150, 25));
        secod_manifesto_no_.setPreferredSize(new java.awt.Dimension(150, 25));
        secod_manifesto_no_.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                secod_manifesto_no_FocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                secod_manifesto_no_FocusLost(evt);
            }
        });
        secod_manifesto_no_.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                secod_manifesto_no_KeyPressed(evt);
            }
        });
        jPanel3.add(secod_manifesto_no_);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        jPanel1.add(jPanel3, gridBagConstraints);

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
    }// </editor-fold>//GEN-END:initComponents

    private void quit_btn_kensetuMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_quit_btn_kensetuMousePressed
        // TODO add your handling code here:
    }//GEN-LAST:event_quit_btn_kensetuMousePressed

    private void print_btn_kensetuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_print_btn_kensetuActionPerformed
        // TODO add your handling code here:
        for (int cnt = 0; cnt < (Integer) pring_num_.getValue(); cnt++) {
            //変更内容の登録処理
            update();
            //印刷処理
            new XlsxManifesto(XlsxManifesto.MANIFESTO_REPORT3).print(con_, XlsxManifesto.MANIFESTO_REPORT3, sno_);

        }

    }//GEN-LAST:event_print_btn_kensetuActionPerformed

    private void rejist_btn_kensetuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rejist_btn_kensetuActionPerformed
        // TODO add your handling code here:
        //登録処理
        if (update()) {
            //正常終了
            doClose();
        }
    }//GEN-LAST:event_rejist_btn_kensetuActionPerformed

    private void quit_btn_kensetuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_quit_btn_kensetuActionPerformed
        // TODO add your handling code here:
        doClose();
    }//GEN-LAST:event_quit_btn_kensetuActionPerformed

    private void id_search_FocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_id_search_FocusGained
        // TODO add your handling code here:
        id_search_.selectAll();
    }//GEN-LAST:event_id_search_FocusGained

    private void id_search_FocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_id_search_FocusLost
        // TODO add your handling code here:
        private_id_search();
    }//GEN-LAST:event_id_search_FocusLost

    private void private_id_search() {
        try {
            int search_id = Integer.valueOf(id_search_.getText());
            for (int i = 0; i < contOfficeName2Array[0].length; i++) {
                if (search_id == Integer.valueOf((String) contOfficeName2Array[0][i])) {
                    last_cont_cmb.setSelectedItem(
                             (String) contOfficeName2Array[1][i].toString() + "　"
                             + (String) contOfficeName2Array[2][i].toString() + (String) contOfficeName2Array[3][i].toString());
                    break;
                }
            }
        } catch (Exception e) {
            id_search_.setText("0");
        }
    }

    private void id_search_KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_id_search_KeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            cont_date2_year.requestFocus();
        }
    }//GEN-LAST:event_id_search_KeyPressed

    private void cont_date2_yearKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cont_date2_yearKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            cont_date2_year.transferFocus();
            evt.consume();
        }
    }//GEN-LAST:event_cont_date2_yearKeyPressed

    private void cont_date2_monthKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cont_date2_monthKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            cont_date2_month.transferFocus();
            evt.consume();
        }
    }//GEN-LAST:event_cont_date2_monthKeyPressed

    private void cont_date2_dayKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cont_date2_dayKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            cont_date2_day.transferFocus();
            evt.consume();
        }
    }//GEN-LAST:event_cont_date2_dayKeyPressed

    private void last_cont_cmbItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_last_cont_cmbItemStateChanged

    }//GEN-LAST:event_last_cont_cmbItemStateChanged

    private void secod_manifesto_no_FocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_secod_manifesto_no_FocusGained
        // TODO add your handling code here:
    }//GEN-LAST:event_secod_manifesto_no_FocusGained

    private void secod_manifesto_no_FocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_secod_manifesto_no_FocusLost
        // TODO add your handling code here:
    }//GEN-LAST:event_secod_manifesto_no_FocusLost

    private void secod_manifesto_no_KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_secod_manifesto_no_KeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_secod_manifesto_no_KeyPressed

    private void last_cont_cmbActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_last_cont_cmbActionPerformed
        // TODO add your handling code here:
        id_search_.setText((String) contOfficeName2Array[0][last_cont_cmb.getSelectedIndex()]);
    }//GEN-LAST:event_last_cont_cmbActionPerformed

    public boolean update() {

        try {
            Statement st = con_.createStatement();
            st.executeUpdate("begin;");
            Date disposal_date2_ = null;

            try {
                String s = new StringBuilder()
                         .append("update manifesto_data set ")
                         .append("last_office_name = ?, ")//最終処分を行った場所（マニフェストマスタ参照）
                         .append("disposal_date2 = ?, ")//最終処分年月日
                         .append("secod_manifesto_no = ? ")//2次マニフェストNO
                         .append("where weight_sno = ?;").toString();//95
                PreparedStatement ps = con_.prepareStatement(s);
                try {
                    for (int i = 0; i < multi_select_.length; i++) {
                        //複数選択時
                        ps.setObject(1, last_cont_cmb.getSelectedItem().toString());
                        if (!cont_date2_year.getText().equals("") && !cont_date2_month.getText().equals("") && !cont_date2_day.getText().equals("")) {
                            disposal_date2_ = DateFormat.getDateInstance()
                                     .parse(String.valueOf(Integer.valueOf(cont_date2_year.getText()))//+1988
                                              + "/" + cont_date2_month.getText() + "/" + cont_date2_day.getText());
                        } else {
                            disposal_date2_ = null;
                        }

                        ps.setObject(2, DBManager.convert(disposal_date2_));
                        ps.setObject(3, secod_manifesto_no_.getText());
                        ps.setObject(4, (Long) model_.getColumns(multi_select_[i]).get("sno"));

                        ps.executeUpdate();

                    }
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

            return false;
        }

        return true;
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField cont_date2_day;
    private javax.swing.JTextField cont_date2_month;
    private javax.swing.JTextField cont_date2_year;
    private javax.swing.JTextField id_search_;
    private javax.swing.JLabel jLabel128;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel77;
    private javax.swing.JLabel jLabel78;
    private javax.swing.JLabel jLabel79;
    private javax.swing.JLabel jLabel80;
    private javax.swing.JLabel jLabel87;
    private javax.swing.JLabel jLabel97;
    private javax.swing.JLabel jLabel98;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel21;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JComboBox last_cont_cmb;
    private javax.swing.JSpinner pring_num_;
    private javax.swing.JButton print_btn_kensetu;
    private javax.swing.JButton quit_btn_kensetu;
    private javax.swing.JButton rejist_btn_kensetu;
    private javax.swing.JTextField secod_manifesto_no_;
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
