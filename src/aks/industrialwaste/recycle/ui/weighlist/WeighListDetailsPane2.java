package aks.industrialwaste.recycle.ui.weighlist;

import aks.industrialwaste.recycle.util.GoodsSelector1;
import java.awt.Color;
import java.awt.event.KeyEvent;
import java.sql.*;
import java.util.HashMap;
import java.util.Map;

public class WeighListDetailsPane2 extends javax.swing.JPanel {

    public long mLastClickTime = System.currentTimeMillis();
    public int mClickCount = 0;
    private Connection con_;
    private int i正味重量 = 0;
    private int[] i按分正品目id = new int[10];
    private String[] i按分正品目名 = new String[]{"", "", "", "", "", "", "", "", "", ""};
    private int[] i按分正味重量 = new int[10];
    private double[] i按分比重 = new double[10];
    private double[] i按分体積比率 = new double[10];
    private double i按分正味重量合計 = 0.00;
    private Map<String, Object> columns_;

    private WeighListDetailTableModel model_;
    private static Long weight_sno_ = 0l;

    public WeighListDetailsPane2(Connection con, WeighListDetailTableModel model, long weight_sno, int i正味重量_) {
        initComponents();
        con_ = con;
        model_ = model;
        weight_sno_ = weight_sno;
        i正味重量 = i正味重量_;

        columns_ = new HashMap<>();
        //按分に使用する変数の初期化//////////////////////////////////
        clearView();

        //ダブルクリックにてセレクター表示////////////////
        按分品目id_01.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mousePressed(java.awt.event.MouseEvent evt) {
                showSelector(1);
            }
        });
        按分品目id_02.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mousePressed(java.awt.event.MouseEvent evt) {
                showSelector(2);
            }
        });
        按分品目id_03.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mousePressed(java.awt.event.MouseEvent evt) {
                showSelector(3);
            }
        });
        按分品目id_04.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mousePressed(java.awt.event.MouseEvent evt) {
                showSelector(4);
            }
        });
        按分品目id_05.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mousePressed(java.awt.event.MouseEvent evt) {
                showSelector(5);
            }
        });
        按分品目id_06.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mousePressed(java.awt.event.MouseEvent evt) {
                showSelector(6);
            }
        });
        按分品目id_07.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mousePressed(java.awt.event.MouseEvent evt) {
                showSelector(7);
            }
        });
        按分品目id_08.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mousePressed(java.awt.event.MouseEvent evt) {
                showSelector(8);
            }
        });
        按分品目id_09.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mousePressed(java.awt.event.MouseEvent evt) {
                showSelector(9);
            }
        });
        按分品目id_10.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mousePressed(java.awt.event.MouseEvent evt) {
                showSelector(10);
            }
        });

        chk重量手入力.setSelected(false);
        fn重量手入力();

        updateView();

        fn重量手入力チェック();
    }

    private void clearView() {
        //按分に使用する変数の初期化
        for (int i = 0; i < 10; i++) {
            i按分正品目id[i] = 0;
            i按分正品目名[i] = "";
            i按分正味重量[i] = 0;
            i按分比重[i] = 0.;
            i按分体積比率[i] = 0.;
        }

        按分品目id_01.setText("0");
        按分品目名_01.setText("");
        按分比重_01.setText("0.00");
        按分品目id_02.setText("0");
        按分品目名_02.setText("");
        按分比重_02.setText("0.00");
        按分品目id_03.setText("0");
        按分品目名_03.setText("");
        按分比重_03.setText("0.00");
        按分品目id_04.setText("0");
        按分品目名_04.setText("");
        按分比重_04.setText("0.00");
        按分品目id_05.setText("0");
        按分品目名_05.setText("");
        按分比重_05.setText("0.00");
        按分品目id_06.setText("0");
        按分品目名_06.setText("");
        按分比重_06.setText("0.00");
        按分品目id_07.setText("0");
        按分品目名_07.setText("");
        按分比重_07.setText("0.00");
        按分品目id_08.setText("0");
        按分品目名_08.setText("");
        按分比重_08.setText("0.00");
        按分品目id_09.setText("0");
        按分品目名_09.setText("");
        按分比重_09.setText("0.00");
        按分品目id_10.setText("0");
        按分品目名_10.setText("");
        按分比重_10.setText("0.00");
    }

    public void updateView() {

//        BigDecimal b1 = new BigDecimal();
        //按分に使用する変数の初期化
        for (int i = 0; i < 10; i++) {
            i按分正品目id[i] = (Integer) model_.getValueAt(i, "按分品目id");
            i按分正品目名[i] = (String) model_.getValueAt(i, "按分品目名");
            i按分正味重量[i] = (Integer) model_.getValueAt(i, "按分正味重量");
            i按分比重[i] = (Double) model_.getValueAt(i, "按分比重");
            i按分体積比率[i] = (Double) model_.getValueAt(i, "按分m3");
        }

        按分品目id_01.setText(String.valueOf((Integer) model_.getValueAt(0, "按分品目id")));
        按分品目名_01.setText((String) model_.getValueAt(0, "按分品目名"));
        按分正味重量_01.setText(String.format("%s", (Integer) model_.getValueAt(0, "按分正味重量")));
        按分比重_01.setText(String.valueOf((Double) model_.getValueAt(0, "按分比重")));
        按分m3_01.setText(String.valueOf((Double) model_.getValueAt(0, "按分m3")));

        按分品目id_02.setText(String.valueOf((Integer) model_.getValueAt(1, "按分品目id")));
        按分品目名_02.setText((String) model_.getValueAt(1, "按分品目名"));
        按分正味重量_02.setText(String.format("%s", (Integer) model_.getValueAt(1, "按分正味重量")));
        按分比重_02.setText(String.valueOf((Double) model_.getValueAt(1, "按分比重")));
        按分m3_02.setText(String.valueOf((Double) model_.getValueAt(1, "按分m3")));

        按分品目id_03.setText(String.valueOf((Integer) model_.getValueAt(2, "按分品目id")));
        按分品目名_03.setText((String) model_.getValueAt(2, "按分品目名"));
        按分正味重量_03.setText(String.format("%s", (Integer) model_.getValueAt(2, "按分正味重量")));
        按分比重_03.setText(String.valueOf((Double) model_.getValueAt(2, "按分比重")));
        按分m3_03.setText(String.valueOf((Double) model_.getValueAt(2, "按分m3")));

        按分品目id_04.setText(String.valueOf((Integer) model_.getValueAt(3, "按分品目id")));
        按分品目名_04.setText((String) model_.getValueAt(3, "按分品目名"));
        按分正味重量_04.setText(String.format("%s", (Integer) model_.getValueAt(3, "按分正味重量")));
        按分比重_04.setText(String.valueOf((Double) model_.getValueAt(3, "按分比重")));
        按分m3_04.setText(String.valueOf((Double) model_.getValueAt(3, "按分m3")));

        按分品目id_05.setText(String.valueOf((Integer) model_.getValueAt(4, "按分品目id")));
        按分品目名_05.setText((String) model_.getValueAt(4, "按分品目名"));
        按分正味重量_05.setText(String.format("%s", (Integer) model_.getValueAt(4, "按分正味重量")));
        按分比重_05.setText(String.valueOf((Double) model_.getValueAt(4, "按分比重")));
        按分m3_05.setText(String.valueOf((Double) model_.getValueAt(4, "按分m3")));

        按分品目id_06.setText(String.valueOf((Integer) model_.getValueAt(5, "按分品目id")));
        按分品目名_06.setText((String) model_.getValueAt(5, "按分品目名"));
        按分正味重量_06.setText(String.format("%s", (Integer) model_.getValueAt(5, "按分正味重量")));
        按分比重_06.setText(String.valueOf((Double) model_.getValueAt(5, "按分比重")));
        按分m3_06.setText(String.valueOf((Double) model_.getValueAt(5, "按分m3")));

        按分品目id_07.setText(String.valueOf((Integer) model_.getValueAt(6, "按分品目id")));
        按分品目名_07.setText((String) model_.getValueAt(6, "按分品目名"));
        按分正味重量_07.setText(String.format("%s", (Integer) model_.getValueAt(6, "按分正味重量")));
        按分比重_07.setText(String.valueOf((Double) model_.getValueAt(6, "按分比重")));
        按分m3_07.setText(String.valueOf((Double) model_.getValueAt(6, "按分m3")));

        按分品目id_08.setText(String.valueOf((Integer) model_.getValueAt(7, "按分品目id")));
        按分品目名_08.setText((String) model_.getValueAt(7, "按分品目名"));
        按分正味重量_08.setText(String.format("%s", (Integer) model_.getValueAt(7, "按分正味重量")));
        按分比重_08.setText(String.valueOf((Double) model_.getValueAt(7, "按分比重")));
        按分m3_08.setText(String.valueOf((Double) model_.getValueAt(7, "按分m3")));

        按分品目id_09.setText(String.valueOf((Integer) model_.getValueAt(8, "按分品目id")));
        按分品目名_09.setText((String) model_.getValueAt(8, "按分品目名"));
        按分正味重量_09.setText(String.format("%s", (Integer) model_.getValueAt(8, "按分正味重量")));
        按分比重_09.setText(String.valueOf((Double) model_.getValueAt(8, "按分比重")));
        按分m3_09.setText(String.valueOf((Double) model_.getValueAt(8, "按分m3")));

        按分品目id_10.setText(String.valueOf((Integer) model_.getValueAt(9, "按分品目id")));
        按分品目名_10.setText((String) model_.getValueAt(9, "按分品目名"));
        按分正味重量_10.setText(String.format("%s", (Integer) model_.getValueAt(9, "按分正味重量")));
        按分比重_10.setText(String.valueOf((Double) model_.getValueAt(9, "按分比重")));
        按分m3_10.setText(String.valueOf((Double) model_.getValueAt(9, "按分m3")));
    }

    private void showSelector(int index) {

        long now_time = System.currentTimeMillis();
        long gap_time = now_time - mLastClickTime;
        if (gap_time < 500) {
            mClickCount++;
        } else {
            mClickCount = 1;
        }

        if (mClickCount >= 2) {
            GoodsSelector1 selector = new GoodsSelector1(con_);
            switch (selector.showSelector(WeighListDetailsPane2.this)) {
                case GoodsSelector1.CLEARED:
                    i按分正品目id[index - 1] = 0;
                    i按分正品目名[index - 1] = "";
                    i按分比重[index - 1] = 0.;
                    break;
                case GoodsSelector1.SELECTED:
                    Map<String, Object> item = selector.selectedRow();
                    i按分正品目id[index - 1] = (Integer) item.get("id");
                    i按分正品目名[index - 1] = (String) item.get("name");
                    i按分比重[index - 1] = (Double) item.get("ryube");
                    break;
                default:
            }

            if (index == 1) {
                按分品目id_01.setText(String.valueOf(i按分正品目id[0]));
                按分品目名_01.setText(i按分正品目名[0]);
                按分比重_01.setText(String.valueOf(i按分比重[0]));
            } else if (index == 2) {
                按分品目id_02.setText(String.valueOf(i按分正品目id[1]));
                按分品目名_02.setText(i按分正品目名[1]);
                按分比重_02.setText(String.valueOf(i按分比重[1]));
            } else if (index == 3) {
                按分品目id_03.setText(String.valueOf(i按分正品目id[2]));
                按分品目名_03.setText(i按分正品目名[2]);
                按分比重_03.setText(String.valueOf(i按分比重[2]));
            } else if (index == 4) {
                按分品目id_04.setText(String.valueOf(i按分正品目id[3]));
                按分品目名_04.setText(i按分正品目名[3]);
                按分比重_04.setText(String.valueOf(i按分比重[3]));
            } else if (index == 5) {
                按分品目id_05.setText(String.valueOf(i按分正品目id[4]));
                按分品目名_05.setText(i按分正品目名[4]);
                按分比重_05.setText(String.valueOf(i按分比重[4]));
            } else if (index == 6) {
                按分品目id_06.setText(String.valueOf(i按分正品目id[5]));
                按分品目名_06.setText(i按分正品目名[5]);
                按分比重_06.setText(String.valueOf(i按分比重[5]));
            } else if (index == 7) {
                按分品目id_07.setText(String.valueOf(i按分正品目id[6]));
                按分品目名_07.setText(i按分正品目名[6]);
                按分比重_07.setText(String.valueOf(i按分比重[6]));
            } else if (index == 8) {
                按分品目id_08.setText(String.valueOf(i按分正品目id[7]));
                按分品目名_08.setText(i按分正品目名[7]);
                按分比重_08.setText(String.valueOf(i按分比重[7]));
            } else if (index == 9) {
                按分品目id_09.setText(String.valueOf(i按分正品目id[8]));
                按分品目名_09.setText(i按分正品目名[8]);
                按分比重_09.setText(String.valueOf(i按分比重[8]));
            } else if (index == 10) {
                按分品目id_10.setText(String.valueOf(i按分正品目id[9]));
                按分品目名_10.setText(i按分正品目名[9]);
                按分比重_10.setText(String.valueOf(i按分比重[9]));
            } else {
                按分品目id_01.setText(String.valueOf(i按分正品目id[0]));
                按分品目名_01.setText(i按分正品目名[0]);
                按分比重_01.setText(String.valueOf(i按分比重[0]));
            }

        }
        mLastClickTime = now_time;

    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        按分品目id_01 = new aks.swing.JTextFieldHankaku();
        按分品目名_01 = new javax.swing.JTextField();
        按分正味重量_01 = new aks.swing.JTextFieldKeiryo();
        按分比重_01 = new javax.swing.JTextField();
        按分m3_01 = new javax.swing.JTextField();
        按分品目id_02 = new aks.swing.JTextFieldHankaku();
        按分品目名_02 = new javax.swing.JTextField();
        按分正味重量_02 = new aks.swing.JTextFieldKeiryo();
        按分比重_02 = new javax.swing.JTextField();
        按分m3_02 = new aks.swing.JTextFieldKeiryo();
        按分品目id_03 = new aks.swing.JTextFieldHankaku();
        按分品目名_03 = new javax.swing.JTextField();
        按分正味重量_03 = new aks.swing.JTextFieldKeiryo();
        按分比重_03 = new javax.swing.JTextField();
        按分m3_03 = new aks.swing.JTextFieldKeiryo();
        按分品目id_04 = new aks.swing.JTextFieldHankaku();
        按分品目名_04 = new javax.swing.JTextField();
        按分正味重量_04 = new aks.swing.JTextFieldKeiryo();
        按分比重_04 = new javax.swing.JTextField();
        按分m3_04 = new aks.swing.JTextFieldKeiryo();
        按分品目id_05 = new aks.swing.JTextFieldHankaku();
        按分品目名_05 = new javax.swing.JTextField();
        按分正味重量_05 = new aks.swing.JTextFieldKeiryo();
        按分比重_05 = new javax.swing.JTextField();
        按分m3_05 = new aks.swing.JTextFieldKeiryo();
        按分品目id_06 = new aks.swing.JTextFieldHankaku();
        按分品目名_06 = new javax.swing.JTextField();
        按分正味重量_06 = new aks.swing.JTextFieldKeiryo();
        按分比重_06 = new javax.swing.JTextField();
        按分m3_06 = new aks.swing.JTextFieldKeiryo();
        按分品目id_07 = new aks.swing.JTextFieldHankaku();
        按分品目名_07 = new javax.swing.JTextField();
        按分正味重量_07 = new aks.swing.JTextFieldKeiryo();
        按分比重_07 = new javax.swing.JTextField();
        按分m3_07 = new aks.swing.JTextFieldKeiryo();
        按分品目id_08 = new aks.swing.JTextFieldHankaku();
        按分品目名_08 = new javax.swing.JTextField();
        按分正味重量_08 = new aks.swing.JTextFieldKeiryo();
        按分比重_08 = new javax.swing.JTextField();
        按分m3_08 = new aks.swing.JTextFieldKeiryo();
        按分品目id_09 = new aks.swing.JTextFieldHankaku();
        按分品目名_09 = new javax.swing.JTextField();
        按分正味重量_09 = new aks.swing.JTextFieldKeiryo();
        按分比重_09 = new javax.swing.JTextField();
        按分m3_09 = new aks.swing.JTextFieldKeiryo();
        按分品目id_10 = new aks.swing.JTextFieldHankaku();
        按分品目名_10 = new javax.swing.JTextField();
        按分正味重量_10 = new aks.swing.JTextFieldKeiryo();
        按分比重_10 = new javax.swing.JTextField();
        按分m3_10 = new aks.swing.JTextFieldKeiryo();
        jPanel1 = new javax.swing.JPanel();
        jLabel8 = new javax.swing.JLabel();
        jLabel21 = new javax.swing.JLabel();
        chk重量手入力 = new javax.swing.JCheckBox();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        lbl合計重量値 = new javax.swing.JLabel();

        jLabel1.setText("jLabel1");

        setMaximumSize(new java.awt.Dimension(500, 600));
        setMinimumSize(new java.awt.Dimension(500, 600));
        setPreferredSize(new java.awt.Dimension(500, 600));
        setLayout(new java.awt.GridBagLayout());

        jLabel2.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        jLabel2.setText("混載按分登録");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        add(jLabel2, gridBagConstraints);

        jLabel3.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel3.setText("品目ID");
        jLabel3.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jLabel3.setMaximumSize(new java.awt.Dimension(60, 30));
        jLabel3.setMinimumSize(new java.awt.Dimension(60, 30));
        jLabel3.setPreferredSize(new java.awt.Dimension(60, 30));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        add(jLabel3, gridBagConstraints);

        jLabel4.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel4.setText("品　名");
        jLabel4.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jLabel4.setMaximumSize(new java.awt.Dimension(190, 30));
        jLabel4.setMinimumSize(new java.awt.Dimension(190, 30));
        jLabel4.setPreferredSize(new java.awt.Dimension(190, 30));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        add(jLabel4, gridBagConstraints);

        jLabel5.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel5.setText("按分正味重量");
        jLabel5.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jLabel5.setMaximumSize(new java.awt.Dimension(100, 30));
        jLabel5.setMinimumSize(new java.awt.Dimension(100, 30));
        jLabel5.setPreferredSize(new java.awt.Dimension(100, 30));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        add(jLabel5, gridBagConstraints);

        jLabel6.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        jLabel6.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel6.setText("比重");
        jLabel6.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jLabel6.setMaximumSize(new java.awt.Dimension(70, 30));
        jLabel6.setMinimumSize(new java.awt.Dimension(70, 30));
        jLabel6.setPreferredSize(new java.awt.Dimension(70, 30));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 1;
        add(jLabel6, gridBagConstraints);

        jLabel7.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        jLabel7.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel7.setText("%");
        jLabel7.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jLabel7.setMaximumSize(new java.awt.Dimension(70, 30));
        jLabel7.setMinimumSize(new java.awt.Dimension(70, 30));
        jLabel7.setPreferredSize(new java.awt.Dimension(70, 30));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 1;
        add(jLabel7, gridBagConstraints);

        按分品目id_01.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        按分品目id_01.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        按分品目id_01.setText("0");
        按分品目id_01.setMaximumSize(new java.awt.Dimension(60, 30));
        按分品目id_01.setMinimumSize(new java.awt.Dimension(60, 30));
        按分品目id_01.setPreferredSize(new java.awt.Dimension(60, 30));
        按分品目id_01.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                按分品目id_01KeyPressed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        add(按分品目id_01, gridBagConstraints);

        按分品目名_01.setEditable(false);
        按分品目名_01.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        按分品目名_01.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        按分品目名_01.setEnabled(false);
        按分品目名_01.setFocusable(false);
        按分品目名_01.setMaximumSize(new java.awt.Dimension(190, 30));
        按分品目名_01.setMinimumSize(new java.awt.Dimension(190, 30));
        按分品目名_01.setPreferredSize(new java.awt.Dimension(190, 30));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        add(按分品目名_01, gridBagConstraints);

        按分正味重量_01.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        按分正味重量_01.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        按分正味重量_01.setText("0");
        按分正味重量_01.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        按分正味重量_01.setMaximumSize(new java.awt.Dimension(100, 30));
        按分正味重量_01.setMinimumSize(new java.awt.Dimension(100, 30));
        按分正味重量_01.setPreferredSize(new java.awt.Dimension(100, 30));
        按分正味重量_01.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                按分正味重量_01FocusLost(evt);
            }
        });
        按分正味重量_01.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                按分正味重量_01KeyPressed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 2;
        add(按分正味重量_01, gridBagConstraints);

        按分比重_01.setEditable(false);
        按分比重_01.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        按分比重_01.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        按分比重_01.setText("0.00");
        按分比重_01.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        按分比重_01.setEnabled(false);
        按分比重_01.setFocusable(false);
        按分比重_01.setMaximumSize(new java.awt.Dimension(70, 30));
        按分比重_01.setMinimumSize(new java.awt.Dimension(70, 30));
        按分比重_01.setPreferredSize(new java.awt.Dimension(70, 30));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 2;
        add(按分比重_01, gridBagConstraints);

        按分m3_01.setEditable(false);
        按分m3_01.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        按分m3_01.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        按分m3_01.setText("0.00");
        按分m3_01.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        按分m3_01.setEnabled(false);
        按分m3_01.setFocusable(false);
        按分m3_01.setMaximumSize(new java.awt.Dimension(70, 30));
        按分m3_01.setMinimumSize(new java.awt.Dimension(70, 30));
        按分m3_01.setPreferredSize(new java.awt.Dimension(70, 30));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 2;
        add(按分m3_01, gridBagConstraints);

        按分品目id_02.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        按分品目id_02.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        按分品目id_02.setText("0");
        按分品目id_02.setMaximumSize(new java.awt.Dimension(60, 30));
        按分品目id_02.setMinimumSize(new java.awt.Dimension(60, 30));
        按分品目id_02.setPreferredSize(new java.awt.Dimension(60, 30));
        按分品目id_02.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                按分品目id_02KeyPressed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        add(按分品目id_02, gridBagConstraints);

        按分品目名_02.setEditable(false);
        按分品目名_02.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        按分品目名_02.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        按分品目名_02.setEnabled(false);
        按分品目名_02.setFocusable(false);
        按分品目名_02.setMaximumSize(new java.awt.Dimension(190, 30));
        按分品目名_02.setMinimumSize(new java.awt.Dimension(190, 30));
        按分品目名_02.setPreferredSize(new java.awt.Dimension(190, 30));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        add(按分品目名_02, gridBagConstraints);

        按分正味重量_02.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        按分正味重量_02.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        按分正味重量_02.setText("0");
        按分正味重量_02.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        按分正味重量_02.setMaximumSize(new java.awt.Dimension(100, 30));
        按分正味重量_02.setMinimumSize(new java.awt.Dimension(100, 30));
        按分正味重量_02.setPreferredSize(new java.awt.Dimension(100, 30));
        按分正味重量_02.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                按分正味重量_02FocusLost(evt);
            }
        });
        按分正味重量_02.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                按分正味重量_02KeyPressed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 3;
        add(按分正味重量_02, gridBagConstraints);

        按分比重_02.setEditable(false);
        按分比重_02.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        按分比重_02.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        按分比重_02.setText("0.00");
        按分比重_02.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        按分比重_02.setEnabled(false);
        按分比重_02.setFocusable(false);
        按分比重_02.setMaximumSize(new java.awt.Dimension(70, 30));
        按分比重_02.setMinimumSize(new java.awt.Dimension(70, 30));
        按分比重_02.setPreferredSize(new java.awt.Dimension(70, 30));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 3;
        add(按分比重_02, gridBagConstraints);

        按分m3_02.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        按分m3_02.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        按分m3_02.setText("0.00");
        按分m3_02.setMaximumSize(new java.awt.Dimension(70, 30));
        按分m3_02.setMinimumSize(new java.awt.Dimension(70, 30));
        按分m3_02.setPreferredSize(new java.awt.Dimension(70, 30));
        按分m3_02.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                按分m3_02KeyPressed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 3;
        add(按分m3_02, gridBagConstraints);

        按分品目id_03.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        按分品目id_03.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        按分品目id_03.setText("0");
        按分品目id_03.setMaximumSize(new java.awt.Dimension(60, 30));
        按分品目id_03.setMinimumSize(new java.awt.Dimension(60, 30));
        按分品目id_03.setPreferredSize(new java.awt.Dimension(60, 30));
        按分品目id_03.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                按分品目id_03KeyPressed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        add(按分品目id_03, gridBagConstraints);

        按分品目名_03.setEditable(false);
        按分品目名_03.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        按分品目名_03.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        按分品目名_03.setEnabled(false);
        按分品目名_03.setFocusable(false);
        按分品目名_03.setMaximumSize(new java.awt.Dimension(190, 30));
        按分品目名_03.setMinimumSize(new java.awt.Dimension(190, 30));
        按分品目名_03.setPreferredSize(new java.awt.Dimension(190, 30));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        add(按分品目名_03, gridBagConstraints);

        按分正味重量_03.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        按分正味重量_03.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        按分正味重量_03.setText("0");
        按分正味重量_03.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        按分正味重量_03.setMaximumSize(new java.awt.Dimension(100, 30));
        按分正味重量_03.setMinimumSize(new java.awt.Dimension(100, 30));
        按分正味重量_03.setPreferredSize(new java.awt.Dimension(100, 30));
        按分正味重量_03.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                按分正味重量_03FocusLost(evt);
            }
        });
        按分正味重量_03.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                按分正味重量_03KeyPressed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 4;
        add(按分正味重量_03, gridBagConstraints);

        按分比重_03.setEditable(false);
        按分比重_03.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        按分比重_03.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        按分比重_03.setText("0.00");
        按分比重_03.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        按分比重_03.setEnabled(false);
        按分比重_03.setFocusable(false);
        按分比重_03.setMaximumSize(new java.awt.Dimension(70, 30));
        按分比重_03.setMinimumSize(new java.awt.Dimension(70, 30));
        按分比重_03.setPreferredSize(new java.awt.Dimension(70, 30));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 4;
        add(按分比重_03, gridBagConstraints);

        按分m3_03.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        按分m3_03.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        按分m3_03.setText("0.00");
        按分m3_03.setMaximumSize(new java.awt.Dimension(70, 30));
        按分m3_03.setMinimumSize(new java.awt.Dimension(70, 30));
        按分m3_03.setPreferredSize(new java.awt.Dimension(70, 30));
        按分m3_03.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                按分m3_03KeyPressed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 4;
        add(按分m3_03, gridBagConstraints);

        按分品目id_04.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        按分品目id_04.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        按分品目id_04.setText("0");
        按分品目id_04.setMaximumSize(new java.awt.Dimension(60, 30));
        按分品目id_04.setMinimumSize(new java.awt.Dimension(60, 30));
        按分品目id_04.setPreferredSize(new java.awt.Dimension(60, 30));
        按分品目id_04.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                按分品目id_04KeyPressed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        add(按分品目id_04, gridBagConstraints);

        按分品目名_04.setEditable(false);
        按分品目名_04.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        按分品目名_04.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        按分品目名_04.setEnabled(false);
        按分品目名_04.setFocusable(false);
        按分品目名_04.setMaximumSize(new java.awt.Dimension(190, 30));
        按分品目名_04.setMinimumSize(new java.awt.Dimension(190, 30));
        按分品目名_04.setPreferredSize(new java.awt.Dimension(190, 30));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        add(按分品目名_04, gridBagConstraints);

        按分正味重量_04.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        按分正味重量_04.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        按分正味重量_04.setText("0");
        按分正味重量_04.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        按分正味重量_04.setMaximumSize(new java.awt.Dimension(100, 30));
        按分正味重量_04.setMinimumSize(new java.awt.Dimension(100, 30));
        按分正味重量_04.setPreferredSize(new java.awt.Dimension(100, 30));
        按分正味重量_04.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                按分正味重量_04FocusLost(evt);
            }
        });
        按分正味重量_04.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                按分正味重量_04KeyPressed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 5;
        add(按分正味重量_04, gridBagConstraints);

        按分比重_04.setEditable(false);
        按分比重_04.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        按分比重_04.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        按分比重_04.setText("0.00");
        按分比重_04.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        按分比重_04.setEnabled(false);
        按分比重_04.setFocusable(false);
        按分比重_04.setMaximumSize(new java.awt.Dimension(70, 30));
        按分比重_04.setMinimumSize(new java.awt.Dimension(70, 30));
        按分比重_04.setPreferredSize(new java.awt.Dimension(70, 30));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 5;
        add(按分比重_04, gridBagConstraints);

        按分m3_04.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        按分m3_04.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        按分m3_04.setText("0.00");
        按分m3_04.setMaximumSize(new java.awt.Dimension(70, 30));
        按分m3_04.setMinimumSize(new java.awt.Dimension(70, 30));
        按分m3_04.setPreferredSize(new java.awt.Dimension(70, 30));
        按分m3_04.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                按分m3_04KeyPressed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 5;
        add(按分m3_04, gridBagConstraints);

        按分品目id_05.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        按分品目id_05.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        按分品目id_05.setText("0");
        按分品目id_05.setMaximumSize(new java.awt.Dimension(60, 30));
        按分品目id_05.setMinimumSize(new java.awt.Dimension(60, 30));
        按分品目id_05.setPreferredSize(new java.awt.Dimension(60, 30));
        按分品目id_05.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                按分品目id_05KeyPressed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        add(按分品目id_05, gridBagConstraints);

        按分品目名_05.setEditable(false);
        按分品目名_05.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        按分品目名_05.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        按分品目名_05.setEnabled(false);
        按分品目名_05.setFocusable(false);
        按分品目名_05.setMaximumSize(new java.awt.Dimension(190, 30));
        按分品目名_05.setMinimumSize(new java.awt.Dimension(190, 30));
        按分品目名_05.setPreferredSize(new java.awt.Dimension(190, 30));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        add(按分品目名_05, gridBagConstraints);

        按分正味重量_05.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        按分正味重量_05.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        按分正味重量_05.setText("0");
        按分正味重量_05.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        按分正味重量_05.setMaximumSize(new java.awt.Dimension(100, 30));
        按分正味重量_05.setMinimumSize(new java.awt.Dimension(100, 30));
        按分正味重量_05.setPreferredSize(new java.awt.Dimension(100, 30));
        按分正味重量_05.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                按分正味重量_05FocusLost(evt);
            }
        });
        按分正味重量_05.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                按分正味重量_05KeyPressed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 6;
        add(按分正味重量_05, gridBagConstraints);

        按分比重_05.setEditable(false);
        按分比重_05.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        按分比重_05.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        按分比重_05.setText("0.00");
        按分比重_05.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        按分比重_05.setEnabled(false);
        按分比重_05.setFocusable(false);
        按分比重_05.setMaximumSize(new java.awt.Dimension(70, 30));
        按分比重_05.setMinimumSize(new java.awt.Dimension(70, 30));
        按分比重_05.setPreferredSize(new java.awt.Dimension(70, 30));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 6;
        add(按分比重_05, gridBagConstraints);

        按分m3_05.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        按分m3_05.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        按分m3_05.setText("0.00");
        按分m3_05.setMaximumSize(new java.awt.Dimension(70, 30));
        按分m3_05.setMinimumSize(new java.awt.Dimension(70, 30));
        按分m3_05.setPreferredSize(new java.awt.Dimension(70, 30));
        按分m3_05.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                按分m3_05KeyPressed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 6;
        add(按分m3_05, gridBagConstraints);

        按分品目id_06.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        按分品目id_06.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        按分品目id_06.setText("0");
        按分品目id_06.setMaximumSize(new java.awt.Dimension(60, 30));
        按分品目id_06.setMinimumSize(new java.awt.Dimension(60, 30));
        按分品目id_06.setPreferredSize(new java.awt.Dimension(60, 30));
        按分品目id_06.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                按分品目id_06KeyPressed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 7;
        add(按分品目id_06, gridBagConstraints);

        按分品目名_06.setEditable(false);
        按分品目名_06.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        按分品目名_06.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        按分品目名_06.setEnabled(false);
        按分品目名_06.setFocusable(false);
        按分品目名_06.setMaximumSize(new java.awt.Dimension(190, 30));
        按分品目名_06.setMinimumSize(new java.awt.Dimension(190, 30));
        按分品目名_06.setPreferredSize(new java.awt.Dimension(190, 30));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        add(按分品目名_06, gridBagConstraints);

        按分正味重量_06.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        按分正味重量_06.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        按分正味重量_06.setText("0");
        按分正味重量_06.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        按分正味重量_06.setMaximumSize(new java.awt.Dimension(100, 30));
        按分正味重量_06.setMinimumSize(new java.awt.Dimension(100, 30));
        按分正味重量_06.setPreferredSize(new java.awt.Dimension(100, 30));
        按分正味重量_06.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                按分正味重量_06FocusLost(evt);
            }
        });
        按分正味重量_06.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                按分正味重量_06KeyPressed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 7;
        add(按分正味重量_06, gridBagConstraints);

        按分比重_06.setEditable(false);
        按分比重_06.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        按分比重_06.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        按分比重_06.setText("0.00");
        按分比重_06.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        按分比重_06.setEnabled(false);
        按分比重_06.setFocusable(false);
        按分比重_06.setMaximumSize(new java.awt.Dimension(70, 30));
        按分比重_06.setMinimumSize(new java.awt.Dimension(70, 30));
        按分比重_06.setPreferredSize(new java.awt.Dimension(70, 30));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 7;
        add(按分比重_06, gridBagConstraints);

        按分m3_06.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        按分m3_06.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        按分m3_06.setText("0.00");
        按分m3_06.setMaximumSize(new java.awt.Dimension(70, 30));
        按分m3_06.setMinimumSize(new java.awt.Dimension(70, 30));
        按分m3_06.setPreferredSize(new java.awt.Dimension(70, 30));
        按分m3_06.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                按分m3_06KeyPressed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 7;
        add(按分m3_06, gridBagConstraints);

        按分品目id_07.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        按分品目id_07.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        按分品目id_07.setText("0");
        按分品目id_07.setMaximumSize(new java.awt.Dimension(60, 30));
        按分品目id_07.setMinimumSize(new java.awt.Dimension(60, 30));
        按分品目id_07.setPreferredSize(new java.awt.Dimension(60, 30));
        按分品目id_07.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                按分品目id_07KeyPressed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 8;
        add(按分品目id_07, gridBagConstraints);

        按分品目名_07.setEditable(false);
        按分品目名_07.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        按分品目名_07.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        按分品目名_07.setEnabled(false);
        按分品目名_07.setFocusable(false);
        按分品目名_07.setMaximumSize(new java.awt.Dimension(190, 30));
        按分品目名_07.setMinimumSize(new java.awt.Dimension(190, 30));
        按分品目名_07.setPreferredSize(new java.awt.Dimension(190, 30));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        add(按分品目名_07, gridBagConstraints);

        按分正味重量_07.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        按分正味重量_07.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        按分正味重量_07.setText("0");
        按分正味重量_07.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        按分正味重量_07.setMaximumSize(new java.awt.Dimension(100, 30));
        按分正味重量_07.setMinimumSize(new java.awt.Dimension(100, 30));
        按分正味重量_07.setPreferredSize(new java.awt.Dimension(100, 30));
        按分正味重量_07.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                按分正味重量_07FocusLost(evt);
            }
        });
        按分正味重量_07.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                按分正味重量_07KeyPressed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 8;
        add(按分正味重量_07, gridBagConstraints);

        按分比重_07.setEditable(false);
        按分比重_07.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        按分比重_07.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        按分比重_07.setText("0.00");
        按分比重_07.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        按分比重_07.setEnabled(false);
        按分比重_07.setFocusable(false);
        按分比重_07.setMaximumSize(new java.awt.Dimension(70, 30));
        按分比重_07.setMinimumSize(new java.awt.Dimension(70, 30));
        按分比重_07.setPreferredSize(new java.awt.Dimension(70, 30));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 8;
        add(按分比重_07, gridBagConstraints);

        按分m3_07.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        按分m3_07.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        按分m3_07.setText("0.00");
        按分m3_07.setMaximumSize(new java.awt.Dimension(70, 30));
        按分m3_07.setMinimumSize(new java.awt.Dimension(70, 30));
        按分m3_07.setPreferredSize(new java.awt.Dimension(70, 30));
        按分m3_07.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                按分m3_07KeyPressed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 8;
        add(按分m3_07, gridBagConstraints);

        按分品目id_08.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        按分品目id_08.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        按分品目id_08.setText("0");
        按分品目id_08.setMaximumSize(new java.awt.Dimension(60, 30));
        按分品目id_08.setMinimumSize(new java.awt.Dimension(60, 30));
        按分品目id_08.setPreferredSize(new java.awt.Dimension(60, 30));
        按分品目id_08.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                按分品目id_08KeyPressed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 9;
        add(按分品目id_08, gridBagConstraints);

        按分品目名_08.setEditable(false);
        按分品目名_08.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        按分品目名_08.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        按分品目名_08.setEnabled(false);
        按分品目名_08.setFocusable(false);
        按分品目名_08.setMaximumSize(new java.awt.Dimension(190, 30));
        按分品目名_08.setMinimumSize(new java.awt.Dimension(190, 30));
        按分品目名_08.setPreferredSize(new java.awt.Dimension(190, 30));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        add(按分品目名_08, gridBagConstraints);

        按分正味重量_08.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        按分正味重量_08.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        按分正味重量_08.setText("0");
        按分正味重量_08.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        按分正味重量_08.setMaximumSize(new java.awt.Dimension(100, 30));
        按分正味重量_08.setMinimumSize(new java.awt.Dimension(100, 30));
        按分正味重量_08.setPreferredSize(new java.awt.Dimension(100, 30));
        按分正味重量_08.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                按分正味重量_08FocusLost(evt);
            }
        });
        按分正味重量_08.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                按分正味重量_08KeyPressed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 9;
        add(按分正味重量_08, gridBagConstraints);

        按分比重_08.setEditable(false);
        按分比重_08.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        按分比重_08.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        按分比重_08.setText("0.00");
        按分比重_08.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        按分比重_08.setEnabled(false);
        按分比重_08.setFocusable(false);
        按分比重_08.setMaximumSize(new java.awt.Dimension(70, 30));
        按分比重_08.setMinimumSize(new java.awt.Dimension(70, 30));
        按分比重_08.setPreferredSize(new java.awt.Dimension(70, 30));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 9;
        add(按分比重_08, gridBagConstraints);

        按分m3_08.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        按分m3_08.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        按分m3_08.setText("0.00");
        按分m3_08.setMaximumSize(new java.awt.Dimension(70, 30));
        按分m3_08.setMinimumSize(new java.awt.Dimension(70, 30));
        按分m3_08.setPreferredSize(new java.awt.Dimension(70, 30));
        按分m3_08.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                按分m3_08KeyPressed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 9;
        add(按分m3_08, gridBagConstraints);

        按分品目id_09.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        按分品目id_09.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        按分品目id_09.setText("0");
        按分品目id_09.setMaximumSize(new java.awt.Dimension(60, 30));
        按分品目id_09.setMinimumSize(new java.awt.Dimension(60, 30));
        按分品目id_09.setPreferredSize(new java.awt.Dimension(60, 30));
        按分品目id_09.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                按分品目id_09KeyPressed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 10;
        add(按分品目id_09, gridBagConstraints);

        按分品目名_09.setEditable(false);
        按分品目名_09.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        按分品目名_09.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        按分品目名_09.setEnabled(false);
        按分品目名_09.setFocusable(false);
        按分品目名_09.setMaximumSize(new java.awt.Dimension(190, 30));
        按分品目名_09.setMinimumSize(new java.awt.Dimension(190, 30));
        按分品目名_09.setPreferredSize(new java.awt.Dimension(190, 30));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        add(按分品目名_09, gridBagConstraints);

        按分正味重量_09.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        按分正味重量_09.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        按分正味重量_09.setText("0");
        按分正味重量_09.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        按分正味重量_09.setMaximumSize(new java.awt.Dimension(100, 30));
        按分正味重量_09.setMinimumSize(new java.awt.Dimension(100, 30));
        按分正味重量_09.setPreferredSize(new java.awt.Dimension(100, 30));
        按分正味重量_09.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                按分正味重量_09FocusLost(evt);
            }
        });
        按分正味重量_09.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                按分正味重量_09KeyPressed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 10;
        add(按分正味重量_09, gridBagConstraints);

        按分比重_09.setEditable(false);
        按分比重_09.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        按分比重_09.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        按分比重_09.setText("0.00");
        按分比重_09.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        按分比重_09.setEnabled(false);
        按分比重_09.setFocusable(false);
        按分比重_09.setMaximumSize(new java.awt.Dimension(70, 30));
        按分比重_09.setMinimumSize(new java.awt.Dimension(70, 30));
        按分比重_09.setPreferredSize(new java.awt.Dimension(70, 30));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 10;
        add(按分比重_09, gridBagConstraints);

        按分m3_09.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        按分m3_09.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        按分m3_09.setText("0.00");
        按分m3_09.setMaximumSize(new java.awt.Dimension(70, 30));
        按分m3_09.setMinimumSize(new java.awt.Dimension(70, 30));
        按分m3_09.setPreferredSize(new java.awt.Dimension(70, 30));
        按分m3_09.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                按分m3_09KeyPressed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 10;
        add(按分m3_09, gridBagConstraints);

        按分品目id_10.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        按分品目id_10.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        按分品目id_10.setText("0");
        按分品目id_10.setMaximumSize(new java.awt.Dimension(60, 30));
        按分品目id_10.setMinimumSize(new java.awt.Dimension(60, 30));
        按分品目id_10.setPreferredSize(new java.awt.Dimension(60, 30));
        按分品目id_10.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                按分品目id_10KeyPressed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 11;
        add(按分品目id_10, gridBagConstraints);

        按分品目名_10.setEditable(false);
        按分品目名_10.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        按分品目名_10.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        按分品目名_10.setEnabled(false);
        按分品目名_10.setFocusable(false);
        按分品目名_10.setMaximumSize(new java.awt.Dimension(190, 30));
        按分品目名_10.setMinimumSize(new java.awt.Dimension(190, 30));
        按分品目名_10.setPreferredSize(new java.awt.Dimension(190, 30));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 11;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        add(按分品目名_10, gridBagConstraints);

        按分正味重量_10.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        按分正味重量_10.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        按分正味重量_10.setText("0");
        按分正味重量_10.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        按分正味重量_10.setMaximumSize(new java.awt.Dimension(100, 30));
        按分正味重量_10.setMinimumSize(new java.awt.Dimension(100, 30));
        按分正味重量_10.setPreferredSize(new java.awt.Dimension(100, 30));
        按分正味重量_10.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                按分正味重量_10FocusLost(evt);
            }
        });
        按分正味重量_10.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                按分正味重量_10KeyPressed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 11;
        add(按分正味重量_10, gridBagConstraints);

        按分比重_10.setEditable(false);
        按分比重_10.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        按分比重_10.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        按分比重_10.setText("0.00");
        按分比重_10.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        按分比重_10.setEnabled(false);
        按分比重_10.setFocusable(false);
        按分比重_10.setMaximumSize(new java.awt.Dimension(70, 30));
        按分比重_10.setMinimumSize(new java.awt.Dimension(70, 30));
        按分比重_10.setPreferredSize(new java.awt.Dimension(70, 30));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 11;
        add(按分比重_10, gridBagConstraints);

        按分m3_10.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        按分m3_10.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        按分m3_10.setText("0.00");
        按分m3_10.setMaximumSize(new java.awt.Dimension(70, 30));
        按分m3_10.setMinimumSize(new java.awt.Dimension(70, 30));
        按分m3_10.setPreferredSize(new java.awt.Dimension(70, 30));
        按分m3_10.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                按分m3_10KeyPressed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 11;
        add(按分m3_10, gridBagConstraints);

        jPanel1.setMaximumSize(new java.awt.Dimension(500, 239));
        jPanel1.setMinimumSize(new java.awt.Dimension(500, 239));

        jLabel8.setFont(new java.awt.Font("MS UI Gothic", 0, 16)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(255, 0, 0));
        jLabel8.setText("※品目の設定はID入力箇所をダブルクリックで選択画面表示します。");

        jLabel21.setFont(new java.awt.Font("MS UI Gothic", 0, 16)); // NOI18N
        jLabel21.setForeground(new java.awt.Color(255, 0, 0));
        jLabel21.setText("<html>※重量値を手入力した場合には、<br>\n　合計重量値が正味重量と同じであるか確認してください。</html>");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel8)
                    .addComponent(jLabel21, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(57, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel8)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel21, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(141, Short.MAX_VALUE))
        );

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 12;
        gridBagConstraints.gridwidth = 6;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weighty = 1.0;
        add(jPanel1, gridBagConstraints);

        chk重量手入力.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        chk重量手入力.setText("重量手入力");
        chk重量手入力.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                chk重量手入力ItemStateChanged(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 2;
        add(chk重量手入力, gridBagConstraints);

        jLabel9.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        jLabel9.setText("kg");
        jLabel9.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        add(jLabel9, gridBagConstraints);

        jLabel10.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        jLabel10.setText("kg");
        jLabel10.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        add(jLabel10, gridBagConstraints);

        jLabel11.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        jLabel11.setText("kg");
        jLabel11.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        add(jLabel11, gridBagConstraints);

        jLabel12.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        jLabel12.setText("kg");
        jLabel12.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        add(jLabel12, gridBagConstraints);

        jLabel13.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        jLabel13.setText("kg");
        jLabel13.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        add(jLabel13, gridBagConstraints);

        jLabel14.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        jLabel14.setText("kg");
        jLabel14.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        add(jLabel14, gridBagConstraints);

        jLabel15.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        jLabel15.setText("kg");
        jLabel15.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        add(jLabel15, gridBagConstraints);

        jLabel16.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        jLabel16.setText("kg");
        jLabel16.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        add(jLabel16, gridBagConstraints);

        jLabel17.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        jLabel17.setText("kg");
        jLabel17.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 11;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        add(jLabel17, gridBagConstraints);

        jLabel18.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        jLabel18.setText("kg");
        jLabel18.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        add(jLabel18, gridBagConstraints);

        jLabel19.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        jLabel19.setText("合計重量値");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        add(jLabel19, gridBagConstraints);

        lbl合計重量値.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        lbl合計重量値.setForeground(new java.awt.Color(255, 0, 51));
        lbl合計重量値.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lbl合計重量値.setText("0 kg");
        lbl合計重量値.setMaximumSize(new java.awt.Dimension(100, 30));
        lbl合計重量値.setMinimumSize(new java.awt.Dimension(100, 30));
        lbl合計重量値.setPreferredSize(new java.awt.Dimension(100, 30));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        add(lbl合計重量値, gridBagConstraints);
    }// </editor-fold>//GEN-END:initComponents

    private void 按分m3_02KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_按分m3_02KeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            try {
                i按分体積比率[1] = Double.valueOf(按分m3_02.getText());
                i按分比重[1] = Double.valueOf(按分比重_02.getText());
                按分m3_02.transferFocus();
            } catch (Exception e) {
            }
            m3計算体積比率();

        }
    }//GEN-LAST:event_按分m3_02KeyPressed

    private void 按分m3_03KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_按分m3_03KeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            try {
                i按分体積比率[2] = Double.valueOf(按分m3_03.getText());
                i按分比重[2] = Double.valueOf(按分比重_03.getText());
                按分m3_03.transferFocus();
            } catch (Exception e) {
            }
            m3計算体積比率();

        }
    }//GEN-LAST:event_按分m3_03KeyPressed

    private void 按分m3_04KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_按分m3_04KeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            try {
                i按分体積比率[3] = Double.valueOf(按分m3_04.getText());
                i按分比重[3] = Double.valueOf(按分比重_04.getText());
                按分m3_04.transferFocus();
            } catch (Exception e) {
            }
            m3計算体積比率();

        }
    }//GEN-LAST:event_按分m3_04KeyPressed

    private void 按分m3_05KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_按分m3_05KeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            try {
                i按分体積比率[4] = Double.valueOf(按分m3_05.getText());
                i按分比重[4] = Double.valueOf(按分比重_05.getText());
                按分m3_05.transferFocus();
            } catch (Exception e) {
            }
            m3計算体積比率();

        }
    }//GEN-LAST:event_按分m3_05KeyPressed

    private void 按分m3_06KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_按分m3_06KeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            try {
                i按分体積比率[5] = Double.valueOf(按分m3_06.getText());
                i按分比重[5] = Double.valueOf(按分比重_06.getText());
                按分m3_06.transferFocus();
            } catch (Exception e) {
            }
            m3計算体積比率();

        }
    }//GEN-LAST:event_按分m3_06KeyPressed

    private void 按分m3_07KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_按分m3_07KeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            try {
                i按分体積比率[6] = Double.valueOf(按分m3_07.getText());
                i按分比重[6] = Double.valueOf(按分比重_07.getText());
                按分m3_07.transferFocus();
            } catch (Exception e) {
            }
            m3計算体積比率();

        }
    }//GEN-LAST:event_按分m3_07KeyPressed

    private void 按分m3_08KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_按分m3_08KeyPressed

        // TODO add your handling code here:
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            try {
                i按分体積比率[7] = Double.valueOf(按分m3_08.getText());
                i按分比重[7] = Double.valueOf(按分比重_08.getText());
                按分m3_08.transferFocus();
            } catch (Exception e) {
            }
            m3計算体積比率();

        }
    }//GEN-LAST:event_按分m3_08KeyPressed

    private void 按分m3_09KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_按分m3_09KeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            try {
                i按分体積比率[8] = Double.valueOf(按分m3_09.getText());
                i按分比重[8] = Double.valueOf(按分比重_09.getText());
                按分m3_09.transferFocus();
            } catch (Exception e) {
            }
            m3計算体積比率();

        }
    }//GEN-LAST:event_按分m3_09KeyPressed

    private void 按分m3_10KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_按分m3_10KeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            try {
                i按分体積比率[9] = Double.valueOf(按分m3_10.getText());
                i按分比重[9] = Double.valueOf(按分比重_10.getText());
                按分m3_10.transferFocus();
            } catch (Exception e) {
            }
            m3計算体積比率();

        }
    }//GEN-LAST:event_按分m3_10KeyPressed

    private void 按分品目id_01KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_按分品目id_01KeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            按分品目id_01.transferFocus();

            if (!"".equals(按分品目id_01.getText())) {
                columns_.put("goods_id", Integer.valueOf(按分品目id_01.getText()));
                getMasterData_sub9("v0_goods_kind", (Integer) columns_.get("goods_id"));
                按分品目名_01.setText((String) columns_.get("goods_name"));
                按分比重_01.setText(String.valueOf(columns_.get("ryube")));

                i按分正品目id[0] = Integer.valueOf(按分品目id_01.getText());
                i按分正品目名[0] = 按分品目名_01.getText();
                i按分比重[0] = Double.valueOf(按分比重_01.getText());

            } else {
                按分品目id_01.setText(String.valueOf(0));
                按分品目名_01.setText("");
                按分m3_01.setText(String.valueOf(0));
            }
        }
    }//GEN-LAST:event_按分品目id_01KeyPressed

    private void 按分品目id_02KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_按分品目id_02KeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            按分品目id_02.transferFocus();

            if (!"".equals(按分品目id_02.getText())) {
                columns_.put("goods_id", Integer.valueOf(按分品目id_02.getText()));
                getMasterData_sub9("v0_goods_kind", (Integer) columns_.get("goods_id"));
                按分品目名_02.setText((String) columns_.get("goods_name"));
                按分比重_02.setText(String.valueOf(columns_.get("ryube")));

                i按分正品目id[1] = Integer.valueOf(按分品目id_02.getText());
                i按分正品目名[1] = 按分品目名_02.getText();
                i按分比重[1] = Double.valueOf(按分比重_02.getText());

            } else {
                按分品目id_02.setText(String.valueOf(0));
                按分品目名_02.setText("");
                按分m3_02.setText(String.valueOf(0));
            }
        }
    }//GEN-LAST:event_按分品目id_02KeyPressed

    private void 按分品目id_03KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_按分品目id_03KeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            按分品目id_03.transferFocus();

            if (!"".equals(按分品目id_03.getText())) {
                columns_.put("goods_id", Integer.valueOf(按分品目id_03.getText()));
                getMasterData_sub9("v0_goods_kind", (Integer) columns_.get("goods_id"));
                按分品目名_03.setText((String) columns_.get("goods_name"));
                按分比重_03.setText(String.valueOf(columns_.get("ryube")));

                i按分正品目id[2] = Integer.valueOf(按分品目id_03.getText());
                i按分正品目名[2] = 按分品目名_03.getText();
                i按分比重[2] = Double.valueOf(按分比重_03.getText());

            } else {
                按分品目id_03.setText(String.valueOf(0));
                按分品目名_03.setText("");
                按分m3_03.setText(String.valueOf(0));
            }
        }
    }//GEN-LAST:event_按分品目id_03KeyPressed

    private void 按分品目id_04KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_按分品目id_04KeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            按分品目id_04.transferFocus();

            if (!"".equals(按分品目id_04.getText())) {
                columns_.put("goods_id", Integer.valueOf(按分品目id_04.getText()));
                getMasterData_sub9("v0_goods_kind", (Integer) columns_.get("goods_id"));
                按分品目名_04.setText((String) columns_.get("goods_name"));
                按分比重_04.setText(String.valueOf(columns_.get("ryube")));

                i按分正品目id[3] = Integer.valueOf(按分品目id_04.getText());
                i按分正品目名[3] = 按分品目名_04.getText();
                i按分比重[3] = Double.valueOf(按分比重_04.getText());

            } else {
                按分品目id_04.setText(String.valueOf(0));
                按分品目名_04.setText("");
                按分m3_04.setText(String.valueOf(0));
            }
        }
    }//GEN-LAST:event_按分品目id_04KeyPressed

    private void 按分品目id_05KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_按分品目id_05KeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            按分品目id_05.transferFocus();

            if (!"".equals(按分品目id_05.getText())) {
                columns_.put("goods_id", Integer.valueOf(按分品目id_05.getText()));
                getMasterData_sub9("v0_goods_kind", (Integer) columns_.get("goods_id"));
                按分品目名_05.setText((String) columns_.get("goods_name"));
                按分比重_05.setText(String.valueOf(columns_.get("ryube")));

                i按分正品目id[4] = Integer.valueOf(按分品目id_05.getText());
                i按分正品目名[4] = 按分品目名_05.getText();
                i按分比重[4] = Double.valueOf(按分比重_05.getText());

            } else {
                按分品目id_05.setText(String.valueOf(0));
                按分品目名_05.setText("");
                按分m3_05.setText(String.valueOf(0));
            }
        }
    }//GEN-LAST:event_按分品目id_05KeyPressed

    private void 按分品目id_06KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_按分品目id_06KeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            按分品目id_06.transferFocus();

            if (!"".equals(按分品目id_06.getText())) {
                columns_.put("goods_id", Integer.valueOf(按分品目id_06.getText()));
                getMasterData_sub9("v0_goods_kind", (Integer) columns_.get("goods_id"));
                按分品目名_06.setText((String) columns_.get("goods_name"));
                按分比重_06.setText(String.valueOf(columns_.get("ryube")));

                i按分正品目id[5] = Integer.valueOf(按分品目id_06.getText());
                i按分正品目名[5] = 按分品目名_06.getText();
                i按分比重[5] = Double.valueOf(按分比重_06.getText());

            } else {
                按分品目id_06.setText(String.valueOf(0));
                按分品目名_06.setText("");
                按分m3_06.setText(String.valueOf(0));
            }
        }
    }//GEN-LAST:event_按分品目id_06KeyPressed

    private void 按分品目id_07KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_按分品目id_07KeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            按分品目id_07.transferFocus();

            if (!"".equals(按分品目id_07.getText())) {
                columns_.put("goods_id", Integer.valueOf(按分品目id_07.getText()));
                getMasterData_sub9("v0_goods_kind", (Integer) columns_.get("goods_id"));
                按分品目名_07.setText((String) columns_.get("goods_name"));
                按分比重_07.setText(String.valueOf(columns_.get("ryube")));

                i按分正品目id[6] = Integer.valueOf(按分品目id_07.getText());
                i按分正品目名[6] = 按分品目名_07.getText();
                i按分比重[6] = Double.valueOf(按分比重_07.getText());

            } else {
                按分品目id_07.setText(String.valueOf(0));
                按分品目名_07.setText("");
                按分m3_07.setText(String.valueOf(0));
            }
        }
    }//GEN-LAST:event_按分品目id_07KeyPressed

    private void 按分品目id_08KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_按分品目id_08KeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            按分品目id_08.transferFocus();

            if (!"".equals(按分品目id_08.getText())) {
                columns_.put("goods_id", Integer.valueOf(按分品目id_08.getText()));
                getMasterData_sub9("v0_goods_kind", (Integer) columns_.get("goods_id"));
                按分品目名_08.setText((String) columns_.get("goods_name"));
                按分比重_08.setText(String.valueOf(columns_.get("ryube")));

                i按分正品目id[7] = Integer.valueOf(按分品目id_08.getText());
                i按分正品目名[7] = 按分品目名_08.getText();
                i按分比重[7] = Double.valueOf(按分比重_08.getText());

            } else {
                按分品目id_08.setText(String.valueOf(0));
                按分品目名_08.setText("");
                按分m3_08.setText(String.valueOf(0));
            }
        }
    }//GEN-LAST:event_按分品目id_08KeyPressed

    private void 按分品目id_09KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_按分品目id_09KeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            按分品目id_09.transferFocus();

            if (!"".equals(按分品目id_09.getText())) {
                columns_.put("goods_id", Integer.valueOf(按分品目id_09.getText()));
                getMasterData_sub9("v0_goods_kind", (Integer) columns_.get("goods_id"));
                按分品目名_09.setText((String) columns_.get("goods_name"));
                按分比重_09.setText(String.valueOf(columns_.get("ryube")));

                i按分正品目id[8] = Integer.valueOf(按分品目id_09.getText());
                i按分正品目名[8] = 按分品目名_09.getText();
                i按分比重[8] = Double.valueOf(按分比重_09.getText());

            } else {
                按分品目id_09.setText(String.valueOf(0));
                按分品目名_09.setText("");
                按分m3_09.setText(String.valueOf(0));
            }
        }
    }//GEN-LAST:event_按分品目id_09KeyPressed

    private void 按分品目id_10KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_按分品目id_10KeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            按分品目id_10.transferFocus();

            if (!"".equals(按分品目id_10.getText())) {
                columns_.put("goods_id", Integer.valueOf(按分品目id_10.getText()));
                getMasterData_sub9("v0_goods_kind", (Integer) columns_.get("goods_id"));
                按分品目名_10.setText((String) columns_.get("goods_name"));
                按分比重_10.setText(String.valueOf(columns_.get("ryube")));

                i按分正品目id[9] = Integer.valueOf(按分品目id_10.getText());
                i按分正品目名[9] = 按分品目名_10.getText();
                i按分比重[9] = Double.valueOf(按分比重_10.getText());

            } else {
                按分品目id_10.setText(String.valueOf(0));
                按分品目名_10.setText("");
                按分m3_10.setText(String.valueOf(0));
            }
        }
    }//GEN-LAST:event_按分品目id_10KeyPressed

    private void chk重量手入力ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_chk重量手入力ItemStateChanged
        // TODO add your handling code here:
        fn重量手入力();
    }//GEN-LAST:event_chk重量手入力ItemStateChanged

    private void fn重量手入力チェック() {
        int i合計重量値 = (i按分正味重量[0]
                 + i按分正味重量[1] + i按分正味重量[2] + i按分正味重量[3]
                 + i按分正味重量[4] + i按分正味重量[5] + i按分正味重量[6]
                 + i按分正味重量[7] + i按分正味重量[8] + i按分正味重量[9]);
        lbl合計重量値.setText(String.format("%s kg", i合計重量値));
        if (i合計重量値 == i正味重量) {
            lbl合計重量値.setForeground(Color.BLACK);
        } else {
            lbl合計重量値.setForeground(new Color(255, 0, 51));
        }
    }

    private void 按分正味重量_01FocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_按分正味重量_01FocusLost
        // TODO add your handling code here:
        i按分正味重量[0] = Integer.valueOf(按分正味重量_01.getText());
        fn重量手入力チェック();
    }//GEN-LAST:event_按分正味重量_01FocusLost

    private void 按分正味重量_02FocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_按分正味重量_02FocusLost
        // TODO add your handling code here:
        i按分正味重量[1] = Integer.valueOf(按分正味重量_02.getText());
        fn重量手入力チェック();
    }//GEN-LAST:event_按分正味重量_02FocusLost

    private void 按分正味重量_03FocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_按分正味重量_03FocusLost
        // TODO add your handling code here:
        i按分正味重量[2] = Integer.valueOf(按分正味重量_03.getText());
        fn重量手入力チェック();
    }//GEN-LAST:event_按分正味重量_03FocusLost

    private void 按分正味重量_04FocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_按分正味重量_04FocusLost
        // TODO add your handling code here:
        i按分正味重量[3] = Integer.valueOf(按分正味重量_04.getText());
        fn重量手入力チェック();
    }//GEN-LAST:event_按分正味重量_04FocusLost

    private void 按分正味重量_05FocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_按分正味重量_05FocusLost
        // TODO add your handling code here:
        i按分正味重量[4] = Integer.valueOf(按分正味重量_05.getText());
        fn重量手入力チェック();
    }//GEN-LAST:event_按分正味重量_05FocusLost

    private void 按分正味重量_06FocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_按分正味重量_06FocusLost
        // TODO add your handling code here:
        i按分正味重量[5] = Integer.valueOf(按分正味重量_06.getText());
        fn重量手入力チェック();
    }//GEN-LAST:event_按分正味重量_06FocusLost

    private void 按分正味重量_07FocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_按分正味重量_07FocusLost
        // TODO add your handling code here:
        i按分正味重量[6] = Integer.valueOf(按分正味重量_07.getText());
        fn重量手入力チェック();
    }//GEN-LAST:event_按分正味重量_07FocusLost

    private void 按分正味重量_08FocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_按分正味重量_08FocusLost
        // TODO add your handling code here:
        i按分正味重量[7] = Integer.valueOf(按分正味重量_08.getText());
        fn重量手入力チェック();
    }//GEN-LAST:event_按分正味重量_08FocusLost

    private void 按分正味重量_09FocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_按分正味重量_09FocusLost
        // TODO add your handling code here:
        i按分正味重量[8] = Integer.valueOf(按分正味重量_09.getText());
        fn重量手入力チェック();
    }//GEN-LAST:event_按分正味重量_09FocusLost

    private void 按分正味重量_10FocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_按分正味重量_10FocusLost
        // TODO add your handling code here:
        i按分正味重量[9] = Integer.valueOf(按分正味重量_10.getText());
        fn重量手入力チェック();
    }//GEN-LAST:event_按分正味重量_10FocusLost

    private void 按分正味重量_01KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_按分正味重量_01KeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            按分正味重量_01.transferFocus();
        }
    }//GEN-LAST:event_按分正味重量_01KeyPressed

    private void 按分正味重量_02KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_按分正味重量_02KeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            按分正味重量_02.transferFocus();
        }
    }//GEN-LAST:event_按分正味重量_02KeyPressed

    private void 按分正味重量_03KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_按分正味重量_03KeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            按分正味重量_03.transferFocus();
        }
    }//GEN-LAST:event_按分正味重量_03KeyPressed

    private void 按分正味重量_04KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_按分正味重量_04KeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            按分正味重量_04.transferFocus();
        }
    }//GEN-LAST:event_按分正味重量_04KeyPressed

    private void 按分正味重量_05KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_按分正味重量_05KeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            按分正味重量_05.transferFocus();
        }
    }//GEN-LAST:event_按分正味重量_05KeyPressed

    private void 按分正味重量_06KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_按分正味重量_06KeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            按分正味重量_06.transferFocus();
        }
    }//GEN-LAST:event_按分正味重量_06KeyPressed

    private void 按分正味重量_07KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_按分正味重量_07KeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            按分正味重量_07.transferFocus();
        }
    }//GEN-LAST:event_按分正味重量_07KeyPressed

    private void 按分正味重量_08KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_按分正味重量_08KeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            按分正味重量_08.transferFocus();
        }
    }//GEN-LAST:event_按分正味重量_08KeyPressed

    private void 按分正味重量_09KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_按分正味重量_09KeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            按分正味重量_09.transferFocus();
        }
    }//GEN-LAST:event_按分正味重量_09KeyPressed

    private void 按分正味重量_10KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_按分正味重量_10KeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            按分正味重量_10.transferFocus();
        }
    }//GEN-LAST:event_按分正味重量_10KeyPressed

    private void fn重量手入力() {

        按分正味重量_01.setEnabled(chk重量手入力.isSelected());
        按分正味重量_02.setEnabled(chk重量手入力.isSelected());
        按分正味重量_03.setEnabled(chk重量手入力.isSelected());
        按分正味重量_04.setEnabled(chk重量手入力.isSelected());
        按分正味重量_05.setEnabled(chk重量手入力.isSelected());
        按分正味重量_06.setEnabled(chk重量手入力.isSelected());
        按分正味重量_07.setEnabled(chk重量手入力.isSelected());
        按分正味重量_08.setEnabled(chk重量手入力.isSelected());
        按分正味重量_09.setEnabled(chk重量手入力.isSelected());
        按分正味重量_10.setEnabled(chk重量手入力.isSelected());
        按分正味重量_01.setEditable(chk重量手入力.isSelected());
        按分正味重量_02.setEditable(chk重量手入力.isSelected());
        按分正味重量_03.setEditable(chk重量手入力.isSelected());
        按分正味重量_04.setEditable(chk重量手入力.isSelected());
        按分正味重量_05.setEditable(chk重量手入力.isSelected());
        按分正味重量_06.setEditable(chk重量手入力.isSelected());
        按分正味重量_07.setEditable(chk重量手入力.isSelected());
        按分正味重量_08.setEditable(chk重量手入力.isSelected());
        按分正味重量_09.setEditable(chk重量手入力.isSelected());
        按分正味重量_10.setEditable(chk重量手入力.isSelected());

        按分m3_02.setEnabled(!chk重量手入力.isSelected());
        按分m3_03.setEnabled(!chk重量手入力.isSelected());
        按分m3_04.setEnabled(!chk重量手入力.isSelected());
        按分m3_05.setEnabled(!chk重量手入力.isSelected());
        按分m3_06.setEnabled(!chk重量手入力.isSelected());
        按分m3_07.setEnabled(!chk重量手入力.isSelected());
        按分m3_08.setEnabled(!chk重量手入力.isSelected());
        按分m3_09.setEnabled(!chk重量手入力.isSelected());
        按分m3_10.setEnabled(!chk重量手入力.isSelected());
        按分m3_02.setEditable(!chk重量手入力.isSelected());
        按分m3_03.setEditable(!chk重量手入力.isSelected());
        按分m3_04.setEditable(!chk重量手入力.isSelected());
        按分m3_05.setEditable(!chk重量手入力.isSelected());
        按分m3_06.setEditable(!chk重量手入力.isSelected());
        按分m3_07.setEditable(!chk重量手入力.isSelected());
        按分m3_08.setEditable(!chk重量手入力.isSelected());
        按分m3_09.setEditable(!chk重量手入力.isSelected());
        按分m3_10.setEditable(!chk重量手入力.isSelected());

    }

    private void m3計算体積比率() {
        //１行目の按分体積比率を求める
        // 合計が100とする
        i按分体積比率[0] = 100d - i按分体積比率[1] - i按分体積比率[2] - i按分体積比率[3]
                 - i按分体積比率[4] - i按分体積比率[5] - i按分体積比率[6]
                 - i按分体積比率[7] - i按分体積比率[8] - i按分体積比率[9];

        i按分正味重量合計 = (i按分体積比率[0] * i按分比重[0])
                 + (i按分体積比率[1] * i按分比重[1])
                 + (i按分体積比率[2] * i按分比重[2])
                 + (i按分体積比率[3] * i按分比重[3])
                 + (i按分体積比率[4] * i按分比重[4])
                 + (i按分体積比率[5] * i按分比重[5])
                 + (i按分体積比率[6] * i按分比重[6])
                 + (i按分体積比率[7] * i按分比重[7])
                 + (i按分体積比率[8] * i按分比重[8])
                 + (i按分体積比率[9] * i按分比重[9]);

        i按分正味重量[1] = (int) aks_Math_round(((i按分体積比率[1] * i按分比重[1]) / i按分正味重量合計) * i正味重量, -1);
        i按分正味重量[2] = (int) aks_Math_round(((i按分体積比率[2] * i按分比重[2]) / i按分正味重量合計) * i正味重量, -1);
        i按分正味重量[3] = (int) aks_Math_round(((i按分体積比率[3] * i按分比重[3]) / i按分正味重量合計) * i正味重量, -1);
        i按分正味重量[4] = (int) aks_Math_round(((i按分体積比率[4] * i按分比重[4]) / i按分正味重量合計) * i正味重量, -1);
        i按分正味重量[5] = (int) aks_Math_round(((i按分体積比率[5] * i按分比重[5]) / i按分正味重量合計) * i正味重量, -1);
        i按分正味重量[6] = (int) aks_Math_round(((i按分体積比率[6] * i按分比重[6]) / i按分正味重量合計) * i正味重量, -1);
        i按分正味重量[7] = (int) aks_Math_round(((i按分体積比率[7] * i按分比重[7]) / i按分正味重量合計) * i正味重量, -1);
        i按分正味重量[8] = (int) aks_Math_round(((i按分体積比率[8] * i按分比重[8]) / i按分正味重量合計) * i正味重量, -1);
        i按分正味重量[9] = (int) aks_Math_round(((i按分体積比率[9] * i按分比重[9]) / i按分正味重量合計) * i正味重量, -1);

        i按分正味重量[0] = i正味重量 - (i按分正味重量[1] + i按分正味重量[2] + i按分正味重量[3]
                 + i按分正味重量[4] + i按分正味重量[5] + i按分正味重量[6]
                 + i按分正味重量[7] + i按分正味重量[8] + i按分正味重量[9]);

        按分m3_01.setText(String.valueOf(i按分体積比率[0]));
        按分正味重量_01.setText(String.format("%s", i按分正味重量[0]));
        按分正味重量_02.setText(String.format("%s", i按分正味重量[1]));
        按分正味重量_03.setText(String.format("%s", i按分正味重量[2]));
        按分正味重量_04.setText(String.format("%s", i按分正味重量[3]));
        按分正味重量_05.setText(String.format("%s", i按分正味重量[4]));
        按分正味重量_06.setText(String.format("%s", i按分正味重量[5]));
        按分正味重量_07.setText(String.format("%s", i按分正味重量[6]));
        按分正味重量_08.setText(String.format("%s", i按分正味重量[7]));
        按分正味重量_09.setText(String.format("%s", i按分正味重量[8]));
        按分正味重量_10.setText(String.format("%s", i按分正味重量[9]));

    }

    private int aks_Math_round(double d, int i) {
        int ret = 0;

        ret = (int) Math.round(d * Math.pow(10, i));
        ret = (int) Math.round(ret * Math.pow(10, i * -1));

        return ret;
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JCheckBox chk重量手入力;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JLabel lbl合計重量値;
    private javax.swing.JTextField 按分m3_01;
    private javax.swing.JTextField 按分m3_02;
    private javax.swing.JTextField 按分m3_03;
    private javax.swing.JTextField 按分m3_04;
    private javax.swing.JTextField 按分m3_05;
    private javax.swing.JTextField 按分m3_06;
    private javax.swing.JTextField 按分m3_07;
    private javax.swing.JTextField 按分m3_08;
    private javax.swing.JTextField 按分m3_09;
    private javax.swing.JTextField 按分m3_10;
    private javax.swing.JTextField 按分品目id_01;
    private javax.swing.JTextField 按分品目id_02;
    private javax.swing.JTextField 按分品目id_03;
    private javax.swing.JTextField 按分品目id_04;
    private javax.swing.JTextField 按分品目id_05;
    private javax.swing.JTextField 按分品目id_06;
    private javax.swing.JTextField 按分品目id_07;
    private javax.swing.JTextField 按分品目id_08;
    private javax.swing.JTextField 按分品目id_09;
    private javax.swing.JTextField 按分品目id_10;
    private javax.swing.JTextField 按分品目名_01;
    private javax.swing.JTextField 按分品目名_02;
    private javax.swing.JTextField 按分品目名_03;
    private javax.swing.JTextField 按分品目名_04;
    private javax.swing.JTextField 按分品目名_05;
    private javax.swing.JTextField 按分品目名_06;
    private javax.swing.JTextField 按分品目名_07;
    private javax.swing.JTextField 按分品目名_08;
    private javax.swing.JTextField 按分品目名_09;
    private javax.swing.JTextField 按分品目名_10;
    private javax.swing.JTextField 按分正味重量_01;
    private javax.swing.JTextField 按分正味重量_02;
    private javax.swing.JTextField 按分正味重量_03;
    private javax.swing.JTextField 按分正味重量_04;
    private javax.swing.JTextField 按分正味重量_05;
    private javax.swing.JTextField 按分正味重量_06;
    private javax.swing.JTextField 按分正味重量_07;
    private javax.swing.JTextField 按分正味重量_08;
    private javax.swing.JTextField 按分正味重量_09;
    private javax.swing.JTextField 按分正味重量_10;
    private javax.swing.JTextField 按分比重_01;
    private javax.swing.JTextField 按分比重_02;
    private javax.swing.JTextField 按分比重_03;
    private javax.swing.JTextField 按分比重_04;
    private javax.swing.JTextField 按分比重_05;
    private javax.swing.JTextField 按分比重_06;
    private javax.swing.JTextField 按分比重_07;
    private javax.swing.JTextField 按分比重_08;
    private javax.swing.JTextField 按分比重_09;
    private javax.swing.JTextField 按分比重_10;
    // End of variables declaration//GEN-END:variables

    public void update() {
        try {
            Statement st = con_.createStatement();
            st.executeUpdate("begin;");
            try {
                String s = new StringBuilder()
                         .append("update weigh_data_anbun ")
                         .append("set 按分品目id = ?, ")
                         .append("按分品目名 = ?, ")
                         .append("按分正味重量 = ?, ")
                         .append("按分比重 = ?, ")
                         .append("按分m3 = ? ")
                         .append("where weight_sno = ? and row_no = ?;")
                         .toString();
                PreparedStatement ps = con_.prepareStatement(s);
                try {
                    for (int i = 0; i < 10; i++) {
                        ps.setObject(1, i按分正品目id[i]);
                        ps.setObject(2, i按分正品目名[i]);
                        ps.setObject(3, i按分正味重量[i]);
                        ps.setObject(4, i按分比重[i]);
                        ps.setObject(5, i按分体積比率[i]);
                        ps.setObject(6, weight_sno_);
                        ps.setObject(7, i + 1);
                        ps.executeUpdate();
                    }
                } finally {
                    ps.close();
                }
                st.executeUpdate("commit;");
                java.awt.Toolkit.getDefaultToolkit().beep();
            } catch (SQLException e) {
                st.executeUpdate("rollback;");
                throw e;
            } finally {
                st.close();
            }
        } catch (Exception e) {
        }
    }

    private void getMasterData_sub9(String veiw_name, int id_) {
        try {

            //商品マスタの確認
            String s = "select * from "
                     + veiw_name
                     + " where id = "
                     + String.valueOf(id_);

            PreparedStatement ps = model_.getConnection().prepareStatement(s);
            try {
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    columns_.put("goods_name", rs.getString("name"));
                    columns_.put("goods_option_name", rs.getString("option1_name"));
                    columns_.put("method_id1", rs.getInt("method_id1"));
                    columns_.put("method_id2", rs.getInt("method_id2"));
                    columns_.put("unit_name", rs.getString("unit_name"));
                    columns_.put("unit_value", rs.getInt("unit_value"));
                    columns_.put("carry_name", rs.getString("carry_name"));
                    columns_.put("carry_class", rs.getInt("carry_class"));
                    columns_.put("unit_no", rs.getInt("unit_no"));
                    columns_.put("option1", rs.getInt("option1"));
                    columns_.put("ryube", rs.getDouble("ryube"));

                    columns_.put("unit", rs.getDouble("unit_value"));
                    columns_.put("f按分フラグ", rs.getBoolean("f按分フラグ"));
                }

            } finally {
                ps.close();
            }
        } catch (Exception e) {
            System.out.println(e.toString());
        }
    }
}
