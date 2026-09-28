package aks.industrialwaste.recycle.ui.master.goods;

import aks.industrialwaste.database.DBManager;
import aks.industrialwaste.recycle.ui.sql.SQLCommon;
import aks.industrialwaste.recycle.util.EntSelector;
import aks.swing.DateFieldText;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.Window;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.SwingUtilities;
import jp.co.astax.awt.FontUtility;

public class GoodsZaikoDetailPane0 extends javax.swing.JPanel {

    private List<GoodsZaikoDetailPane11> gzd01;
    private GoodsZaikoDetailPane11 gzd1;
    private GoodsZaikoDetailPane2[] gzd2;
    private int goods_id_ = 0;
    private Date term_date;
    private DateFieldText dft;
    public static int h_row = 0;//グリッドからの選択行
    public int h_max_row = 0;//ヘッダーの最大行

    private SQLCommon mSQLCommon;
    public static Connection con_;
    public static List<Object> o1;
    private Object[][] o2;
    private JPanel dmy1 = new JPanel();
    private JPanel dmy2 = new JPanel();

    public static Double h月搬入予定量, h月搬入実績;
    public static int d稼働日数, d処理予定設定数量, d搬入実績台数;
    public static Double d搬入予定設定数量, d搬入実績数量;

    private SimpleDateFormat sdf01, sdf02;
    private GoodsTableModel model_;
    private int row_;

    private Double d前月搬入数量, d前月平均搬入量, d前月末在庫数, d前月差分;
    private int i前月搬入台数;

    public GoodsZaikoDetailPane0(Connection con, GoodsTableModel model, int row) {

        initComponents();

        con_ = con;
        model_ = model;
        row_ = row;

        Calendar c = Calendar.getInstance();
        Font font14 = FontUtility.getFont("メイリオ", Font.PLAIN, 14);
        term_date = DBManager.convert(c.getTime());

        //所属日パネル
        dft = new DateFieldText(DateFieldText.YMD);
        dft.setSize(new Dimension(260, 31));

        dft.setValue(c);
        dft.setFont1(font14);
        p所属日.add(dft);

        goods_id_ = (Integer) model_.getValueAt(row_, "id");
        sdf01 = new SimpleDateFormat("yyyy年MM月の搬入計画");
        sdf02 = new SimpleDateFormat("yyyy年MM月の日別計画・実績");

        //ヘッダーの選択行
        h_row = -1;
        gzd01 = new ArrayList<GoodsZaikoDetailPane11>();

        mSQLCommon = new SQLCommon(DBManager.jdbc_, DBManager.getUser(), DBManager.getPassword());

        gzd1 = null;
        gzd2 = null;

        hScroll.getVerticalScrollBar().setUnitIncrement(5);
        dScroll.getVerticalScrollBar().setUnitIncrement(10);

        updateView();

    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        jScrollPane1 = new javax.swing.JScrollPane();
        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        p所属日 = new javax.swing.JPanel();
        jPanel15 = new javax.swing.JPanel();
        hScroll = new javax.swing.JScrollPane();
        jPanel6 = new javax.swing.JPanel();
        txt月搬入実績 = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        jLabel27 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel32 = new javax.swing.JLabel();
        jLabel28 = new javax.swing.JLabel();
        jLabel29 = new javax.swing.JLabel();
        jLabel30 = new javax.swing.JLabel();
        jLabel31 = new javax.swing.JLabel();
        txt月搬入予定量 = new javax.swing.JTextField();
        jLabel33 = new javax.swing.JLabel();
        btn追加 = new javax.swing.JButton();
        btn削除 = new javax.swing.JButton();
        jLabel61 = new javax.swing.JLabel();
        jButton8 = new javax.swing.JButton();
        jButton10 = new javax.swing.JButton();
        btn自動補正 = new javax.swing.JButton();
        jLabel11 = new javax.swing.JLabel();
        btn再表示 = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        jPanel7 = new javax.swing.JPanel();
        jLabel9 = new javax.swing.JLabel();
        txt達成率最大値 = new aks.swing.JTextFieldKeiryo();
        txt達成率最小値 = new aks.swing.JTextFieldKeiryo();
        jLabel16 = new javax.swing.JLabel();
        f達成率警報設定_ = new javax.swing.JCheckBox();
        jLabel58 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jPanel8 = new javax.swing.JPanel();
        f在庫警報設定_ = new javax.swing.JCheckBox();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        txt在庫範囲最大値 = new aks.swing.JTextFieldKeiryo();
        txt在庫範囲最小値 = new aks.swing.JTextFieldKeiryo();
        jLabel59 = new javax.swing.JLabel();
        jLabel60 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jPanel9 = new javax.swing.JPanel();
        txt前月平均搬入量 = new javax.swing.JTextField();
        txt前月搬入台数 = new javax.swing.JTextField();
        jLabel14 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        jLabel55 = new javax.swing.JLabel();
        jLabel56 = new javax.swing.JLabel();
        jLabel57 = new javax.swing.JLabel();
        jLabel63 = new javax.swing.JLabel();
        txt前月搬入数量 = new javax.swing.JTextField();
        jLabel64 = new javax.swing.JLabel();
        jPanel11 = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        jLabel18 = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        txt稼働日数 = new javax.swing.JTextField();
        btn再計算 = new javax.swing.JButton();
        jLabel20 = new javax.swing.JLabel();
        jLabel21 = new javax.swing.JLabel();
        jLabel22 = new javax.swing.JLabel();
        jLabel23 = new javax.swing.JLabel();
        jLabel24 = new javax.swing.JLabel();
        jLabel25 = new javax.swing.JLabel();
        txt搬入実績台数 = new javax.swing.JTextField();
        txt搬入実績数量 = new javax.swing.JTextField();
        txt前月末在庫数 = new javax.swing.JTextField();
        txt前月差分 = new javax.swing.JTextField();
        txt当月末在庫数 = new javax.swing.JTextField();
        jLabel26 = new javax.swing.JLabel();
        dScroll = new javax.swing.JScrollPane();
        jPanel13 = new javax.swing.JPanel();
        jLabel34 = new javax.swing.JLabel();
        jLabel35 = new javax.swing.JLabel();
        jLabel36 = new javax.swing.JLabel();
        jLabel37 = new javax.swing.JLabel();
        jLabel38 = new javax.swing.JLabel();
        jLabel39 = new javax.swing.JLabel();
        jLabel40 = new javax.swing.JLabel();
        jLabel41 = new javax.swing.JLabel();
        jLabel42 = new javax.swing.JLabel();
        jLabel43 = new javax.swing.JLabel();
        jLabel44 = new javax.swing.JLabel();
        jLabel45 = new javax.swing.JLabel();
        jLabel46 = new javax.swing.JLabel();
        txt搬入予定設定数量 = new javax.swing.JTextField();
        jLabel47 = new javax.swing.JLabel();
        jLabel48 = new javax.swing.JLabel();
        jLabel49 = new javax.swing.JLabel();
        jLabel50 = new javax.swing.JLabel();
        jLabel51 = new javax.swing.JLabel();
        jLabel52 = new javax.swing.JLabel();
        jLabel53 = new javax.swing.JLabel();
        txt処理予定設定数量 = new javax.swing.JTextField();
        jLabel54 = new javax.swing.JLabel();
        jLabel62 = new javax.swing.JLabel();
        jButton11 = new javax.swing.JButton();
        jButton12 = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jPanel5 = new javax.swing.JPanel();
        jButton5 = new javax.swing.JButton();
        jButton6 = new javax.swing.JButton();
        jButton7 = new javax.swing.JButton();
        jPanel14 = new javax.swing.JPanel();

        setLayout(new java.awt.GridBagLayout());

        jPanel1.setMaximumSize(new java.awt.Dimension(1200, 800));
        jPanel1.setMinimumSize(new java.awt.Dimension(1200, 800));
        jPanel1.setPreferredSize(new java.awt.Dimension(1200, 800));
        jPanel1.setLayout(new java.awt.GridBagLayout());

        jPanel2.setMaximumSize(new java.awt.Dimension(660, 380));
        jPanel2.setMinimumSize(new java.awt.Dimension(660, 380));
        jPanel2.setPreferredSize(new java.awt.Dimension(660, 380));
        jPanel2.setLayout(new java.awt.GridBagLayout());

        jLabel2.setBackground(new java.awt.Color(0, 51, 153));
        jLabel2.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel2.setText("品名");
        jLabel2.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        jLabel2.setMaximumSize(new java.awt.Dimension(100, 25));
        jLabel2.setMinimumSize(new java.awt.Dimension(100, 25));
        jLabel2.setOpaque(true);
        jLabel2.setPreferredSize(new java.awt.Dimension(100, 25));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(10, 0, 0, 0);
        jPanel2.add(jLabel2, gridBagConstraints);

        jTextField1.setEditable(false);
        jTextField1.setBackground(new java.awt.Color(204, 255, 255));
        jTextField1.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        jTextField1.setText("品名１");
        jTextField1.setMaximumSize(new java.awt.Dimension(280, 28));
        jTextField1.setMinimumSize(new java.awt.Dimension(280, 28));
        jTextField1.setPreferredSize(new java.awt.Dimension(280, 28));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(10, 2, 2, 2);
        jPanel2.add(jTextField1, gridBagConstraints);

        jLabel3.setBackground(new java.awt.Color(0, 51, 153));
        jLabel3.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel3.setText("年月日");
        jLabel3.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        jLabel3.setMaximumSize(new java.awt.Dimension(100, 25));
        jLabel3.setMinimumSize(new java.awt.Dimension(100, 25));
        jLabel3.setOpaque(true);
        jLabel3.setPreferredSize(new java.awt.Dimension(100, 25));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        jPanel2.add(jLabel3, gridBagConstraints);

        p所属日.setMaximumSize(new java.awt.Dimension(180, 31));
        p所属日.setMinimumSize(new java.awt.Dimension(180, 31));
        p所属日.setPreferredSize(new java.awt.Dimension(180, 31));
        p所属日.setLayout(new javax.swing.BoxLayout(p所属日, javax.swing.BoxLayout.LINE_AXIS));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 2, 0, 2);
        jPanel2.add(p所属日, gridBagConstraints);

        jPanel15.setBorder(javax.swing.BorderFactory.createEtchedBorder(new java.awt.Color(153, 153, 255), null));
        jPanel15.setMaximumSize(new java.awt.Dimension(660, 285));
        jPanel15.setMinimumSize(new java.awt.Dimension(660, 285));
        jPanel15.setPreferredSize(new java.awt.Dimension(660, 285));
        jPanel15.setLayout(new java.awt.GridBagLayout());

        hScroll.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        hScroll.setMaximumSize(new java.awt.Dimension(660, 200));
        hScroll.setMinimumSize(new java.awt.Dimension(660, 200));
        hScroll.setPreferredSize(new java.awt.Dimension(660, 200));

        jPanel6.setMaximumSize(new java.awt.Dimension(660, 400));
        jPanel6.setMinimumSize(new java.awt.Dimension(660, 400));
        jPanel6.setPreferredSize(new java.awt.Dimension(660, 400));
        jPanel6.setLayout(new java.awt.GridBagLayout());
        hScroll.setViewportView(jPanel6);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 7;
        gridBagConstraints.weighty = 1.0;
        jPanel15.add(hScroll, gridBagConstraints);

        txt月搬入実績.setEditable(false);
        txt月搬入実績.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        txt月搬入実績.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txt月搬入実績.setText("440.8");
        txt月搬入実績.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        txt月搬入実績.setMaximumSize(new java.awt.Dimension(100, 25));
        txt月搬入実績.setMinimumSize(new java.awt.Dimension(100, 25));
        txt月搬入実績.setPreferredSize(new java.awt.Dimension(100, 25));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 10, 0);
        jPanel15.add(txt月搬入実績, gridBagConstraints);

        jLabel6.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        jLabel6.setText(" ton");
        jLabel6.setMaximumSize(new java.awt.Dimension(70, 22));
        jLabel6.setMinimumSize(new java.awt.Dimension(70, 22));
        jLabel6.setPreferredSize(new java.awt.Dimension(70, 22));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 10, 0);
        jPanel15.add(jLabel6, gridBagConstraints);

        jLabel27.setBackground(new java.awt.Color(0, 51, 153));
        jLabel27.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        jLabel27.setForeground(new java.awt.Color(255, 255, 255));
        jLabel27.setText("2015年05月の搬入計画");
        jLabel27.setMaximumSize(new java.awt.Dimension(630, 25));
        jLabel27.setMinimumSize(new java.awt.Dimension(630, 25));
        jLabel27.setOpaque(true);
        jLabel27.setPreferredSize(new java.awt.Dimension(630, 25));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 7;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        jPanel15.add(jLabel27, gridBagConstraints);

        jLabel5.setBackground(new java.awt.Color(102, 102, 255));
        jLabel5.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(255, 255, 255));
        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel5.setText("月搬入予定量");
        jLabel5.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jLabel5.setMaximumSize(new java.awt.Dimension(103, 25));
        jLabel5.setMinimumSize(new java.awt.Dimension(103, 25));
        jLabel5.setOpaque(true);
        jLabel5.setPreferredSize(new java.awt.Dimension(103, 25));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        jPanel15.add(jLabel5, gridBagConstraints);

        jLabel32.setBackground(new java.awt.Color(102, 102, 255));
        jLabel32.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        jLabel32.setForeground(new java.awt.Color(255, 255, 255));
        jLabel32.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel32.setText("業　者");
        jLabel32.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jLabel32.setMaximumSize(new java.awt.Dimension(180, 25));
        jLabel32.setMinimumSize(new java.awt.Dimension(180, 25));
        jLabel32.setOpaque(true);
        jLabel32.setPreferredSize(new java.awt.Dimension(180, 25));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        jPanel15.add(jLabel32, gridBagConstraints);

        jLabel28.setBackground(new java.awt.Color(102, 102, 255));
        jLabel28.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        jLabel28.setForeground(new java.awt.Color(255, 255, 255));
        jLabel28.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel28.setText("基準日まで");
        jLabel28.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jLabel28.setMaximumSize(new java.awt.Dimension(80, 25));
        jLabel28.setMinimumSize(new java.awt.Dimension(80, 25));
        jLabel28.setOpaque(true);
        jLabel28.setPreferredSize(new java.awt.Dimension(80, 25));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        jPanel15.add(jLabel28, gridBagConstraints);

        jLabel29.setBackground(new java.awt.Color(102, 102, 255));
        jLabel29.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        jLabel29.setForeground(new java.awt.Color(255, 255, 255));
        jLabel29.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel29.setText("月搬入実績");
        jLabel29.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jLabel29.setMaximumSize(new java.awt.Dimension(100, 25));
        jLabel29.setMinimumSize(new java.awt.Dimension(100, 25));
        jLabel29.setOpaque(true);
        jLabel29.setPreferredSize(new java.awt.Dimension(100, 25));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        jPanel15.add(jLabel29, gridBagConstraints);

        jLabel30.setBackground(new java.awt.Color(102, 102, 255));
        jLabel30.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        jLabel30.setForeground(new java.awt.Color(255, 255, 255));
        jLabel30.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel30.setText("達成率");
        jLabel30.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jLabel30.setMaximumSize(new java.awt.Dimension(80, 25));
        jLabel30.setMinimumSize(new java.awt.Dimension(80, 25));
        jLabel30.setOpaque(true);
        jLabel30.setPreferredSize(new java.awt.Dimension(80, 25));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        jPanel15.add(jLabel30, gridBagConstraints);

        jLabel31.setBackground(new java.awt.Color(102, 102, 255));
        jLabel31.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        jLabel31.setForeground(new java.awt.Color(255, 255, 255));
        jLabel31.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel31.setText("標準偏差");
        jLabel31.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jLabel31.setMaximumSize(new java.awt.Dimension(70, 25));
        jLabel31.setMinimumSize(new java.awt.Dimension(70, 25));
        jLabel31.setOpaque(true);
        jLabel31.setPreferredSize(new java.awt.Dimension(70, 25));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 6;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        jPanel15.add(jLabel31, gridBagConstraints);

        txt月搬入予定量.setEditable(false);
        txt月搬入予定量.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        txt月搬入予定量.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txt月搬入予定量.setText("1700.0");
        txt月搬入予定量.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        txt月搬入予定量.setMaximumSize(new java.awt.Dimension(103, 25));
        txt月搬入予定量.setMinimumSize(new java.awt.Dimension(103, 25));
        txt月搬入予定量.setPreferredSize(new java.awt.Dimension(103, 25));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 10, 0);
        jPanel15.add(txt月搬入予定量, gridBagConstraints);

        jLabel33.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        jLabel33.setText(" ton");
        jLabel33.setMaximumSize(new java.awt.Dimension(80, 25));
        jLabel33.setMinimumSize(new java.awt.Dimension(80, 25));
        jLabel33.setPreferredSize(new java.awt.Dimension(80, 25));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 10, 0);
        jPanel15.add(jLabel33, gridBagConstraints);

        btn追加.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        btn追加.setText("追加");
        btn追加.setMargin(new java.awt.Insets(2, 2, 2, 2));
        btn追加.setMaximumSize(new java.awt.Dimension(100, 31));
        btn追加.setMinimumSize(new java.awt.Dimension(100, 31));
        btn追加.setPreferredSize(new java.awt.Dimension(100, 31));
        btn追加.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn追加ActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 2, 0);
        jPanel15.add(btn追加, gridBagConstraints);

        btn削除.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        btn削除.setText("削除");
        btn削除.setMargin(new java.awt.Insets(2, 2, 2, 2));
        btn削除.setMaximumSize(new java.awt.Dimension(100, 31));
        btn削除.setMinimumSize(new java.awt.Dimension(100, 31));
        btn削除.setPreferredSize(new java.awt.Dimension(100, 31));
        btn削除.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn削除ActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 2, 2, 0);
        jPanel15.add(btn削除, gridBagConstraints);

        jLabel61.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        jLabel61.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel61.setText("合計");
        jLabel61.setMaximumSize(new java.awt.Dimension(60, 25));
        jLabel61.setMinimumSize(new java.awt.Dimension(60, 25));
        jLabel61.setPreferredSize(new java.awt.Dimension(60, 25));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 10, 0);
        jPanel15.add(jLabel61, gridBagConstraints);

        jButton8.setFont(new java.awt.Font("ＭＳ Ｐ明朝", 0, 12)); // NOI18N
        jButton8.setLabel("↓");
        jButton8.setMargin(new java.awt.Insets(0, 0, 0, 0));
        jButton8.setMaximumSize(new java.awt.Dimension(40, 31));
        jButton8.setMinimumSize(new java.awt.Dimension(40, 31));
        jButton8.setPreferredSize(new java.awt.Dimension(40, 31));
        jButton8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton8ActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 6;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        jPanel15.add(jButton8, gridBagConstraints);

        jButton10.setFont(new java.awt.Font("ＭＳ Ｐ明朝", 0, 12)); // NOI18N
        jButton10.setText("↑");
        jButton10.setMargin(new java.awt.Insets(0, 0, 0, 0));
        jButton10.setMaximumSize(new java.awt.Dimension(40, 31));
        jButton10.setMinimumSize(new java.awt.Dimension(40, 31));
        jButton10.setPreferredSize(new java.awt.Dimension(40, 31));
        jButton10.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton10ActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 6;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        jPanel15.add(jButton10, gridBagConstraints);

        btn自動補正.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        btn自動補正.setForeground(new java.awt.Color(255, 0, 51));
        btn自動補正.setText("自動補正");
        btn自動補正.setFocusable(false);
        btn自動補正.setMargin(new java.awt.Insets(2, 2, 2, 2));
        btn自動補正.setMaximumSize(new java.awt.Dimension(100, 31));
        btn自動補正.setMinimumSize(new java.awt.Dimension(100, 31));
        btn自動補正.setPreferredSize(new java.awt.Dimension(100, 31));
        btn自動補正.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn自動補正ActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.SOUTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 2, 2, 0);
        jPanel15.add(btn自動補正, gridBagConstraints);

        jLabel11.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        jLabel11.setForeground(new java.awt.Color(255, 0, 51));
        jLabel11.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel11.setText("搬入計画合計を日別に振分をします。　");
        jLabel11.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);
        jLabel11.setMaximumSize(new java.awt.Dimension(180, 25));
        jLabel11.setMinimumSize(new java.awt.Dimension(180, 25));
        jLabel11.setPreferredSize(new java.awt.Dimension(180, 25));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.SOUTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 2, 0);
        jPanel15.add(jLabel11, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weighty = 1.0;
        jPanel2.add(jPanel15, gridBagConstraints);

        btn再表示.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        btn再表示.setText("再表示");
        btn再表示.setMargin(new java.awt.Insets(2, 2, 2, 2));
        btn再表示.setMaximumSize(new java.awt.Dimension(100, 31));
        btn再表示.setMinimumSize(new java.awt.Dimension(100, 31));
        btn再表示.setPreferredSize(new java.awt.Dimension(100, 31));
        btn再表示.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn再表示ActionPerformed(evt);
            }
        });
        btn再表示.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                btn再表示KeyPressed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        jPanel2.add(btn再表示, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 50, 0, 0);
        jPanel1.add(jPanel2, gridBagConstraints);

        jPanel3.setMaximumSize(new java.awt.Dimension(500, 365));
        jPanel3.setMinimumSize(new java.awt.Dimension(500, 365));
        jPanel3.setPreferredSize(new java.awt.Dimension(500, 365));
        jPanel3.setLayout(new java.awt.GridBagLayout());

        jPanel7.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel7.setMaximumSize(new java.awt.Dimension(400, 100));
        jPanel7.setMinimumSize(new java.awt.Dimension(400, 100));
        jPanel7.setPreferredSize(new java.awt.Dimension(400, 100));
        jPanel7.setLayout(new java.awt.GridBagLayout());

        jLabel9.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        jLabel9.setText("当月20日以降の達成率");
        jLabel9.setMaximumSize(new java.awt.Dimension(200, 22));
        jLabel9.setMinimumSize(new java.awt.Dimension(200, 22));
        jLabel9.setPreferredSize(new java.awt.Dimension(200, 22));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 15, 0, 0);
        jPanel7.add(jLabel9, gridBagConstraints);

        txt達成率最大値.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        txt達成率最大値.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txt達成率最大値.setText("150.0");
        txt達成率最大値.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        txt達成率最大値.setMaximumSize(new java.awt.Dimension(80, 25));
        txt達成率最大値.setMinimumSize(new java.awt.Dimension(80, 25));
        txt達成率最大値.setPreferredSize(new java.awt.Dimension(80, 25));
        txt達成率最大値.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txt達成率最大値KeyPressed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(10, 2, 2, 2);
        jPanel7.add(txt達成率最大値, gridBagConstraints);

        txt達成率最小値.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        txt達成率最小値.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txt達成率最小値.setText("65.0");
        txt達成率最小値.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        txt達成率最小値.setMaximumSize(new java.awt.Dimension(80, 25));
        txt達成率最小値.setMinimumSize(new java.awt.Dimension(80, 25));
        txt達成率最小値.setPreferredSize(new java.awt.Dimension(80, 25));
        txt達成率最小値.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txt達成率最小値KeyPressed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        jPanel7.add(txt達成率最小値, gridBagConstraints);

        jLabel16.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        jLabel16.setText("％以下");
        jLabel16.setMaximumSize(new java.awt.Dimension(60, 25));
        jLabel16.setMinimumSize(new java.awt.Dimension(60, 25));
        jLabel16.setPreferredSize(new java.awt.Dimension(60, 25));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        jPanel7.add(jLabel16, gridBagConstraints);

        f達成率警報設定_.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        f達成率警報設定_.setText("警報設定");
        f達成率警報設定_.setFocusable(false);
        f達成率警報設定_.setMaximumSize(new java.awt.Dimension(100, 22));
        f達成率警報設定_.setMinimumSize(new java.awt.Dimension(100, 22));
        f達成率警報設定_.setPreferredSize(new java.awt.Dimension(100, 22));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(10, 15, 0, 0);
        jPanel7.add(f達成率警報設定_, gridBagConstraints);

        jLabel58.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        jLabel58.setText("％以上");
        jLabel58.setMaximumSize(new java.awt.Dimension(60, 25));
        jLabel58.setMinimumSize(new java.awt.Dimension(60, 25));
        jLabel58.setPreferredSize(new java.awt.Dimension(60, 25));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(10, 2, 2, 2);
        jPanel7.add(jLabel58, gridBagConstraints);

        jLabel8.setBackground(new java.awt.Color(0, 51, 153));
        jLabel8.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(255, 255, 255));
        jLabel8.setText("　達成率警報設定");
        jLabel8.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        jLabel8.setMaximumSize(new java.awt.Dimension(150, 22));
        jLabel8.setMinimumSize(new java.awt.Dimension(145, 22));
        jLabel8.setOpaque(true);
        jLabel8.setPreferredSize(new java.awt.Dimension(145, 22));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        jPanel7.add(jLabel8, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 50);
        jPanel3.add(jPanel7, gridBagConstraints);

        jPanel8.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel8.setMaximumSize(new java.awt.Dimension(400, 100));
        jPanel8.setMinimumSize(new java.awt.Dimension(400, 100));
        jPanel8.setPreferredSize(new java.awt.Dimension(400, 100));
        jPanel8.setLayout(new java.awt.GridBagLayout());

        f在庫警報設定_.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        f在庫警報設定_.setText("警報設定");
        f在庫警報設定_.setFocusable(false);
        f在庫警報設定_.setMaximumSize(new java.awt.Dimension(100, 22));
        f在庫警報設定_.setMinimumSize(new java.awt.Dimension(100, 22));
        f在庫警報設定_.setPreferredSize(new java.awt.Dimension(100, 22));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(10, 15, 0, 0);
        jPanel8.add(f在庫警報設定_, gridBagConstraints);

        jLabel12.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        jLabel12.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel12.setText("可能最大在庫");
        jLabel12.setMaximumSize(new java.awt.Dimension(100, 22));
        jLabel12.setMinimumSize(new java.awt.Dimension(100, 22));
        jLabel12.setPreferredSize(new java.awt.Dimension(100, 22));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(10, 2, 2, 2);
        jPanel8.add(jLabel12, gridBagConstraints);

        jLabel13.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        jLabel13.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel13.setText("必要最小在庫");
        jLabel13.setMaximumSize(new java.awt.Dimension(100, 22));
        jLabel13.setMinimumSize(new java.awt.Dimension(100, 22));
        jLabel13.setPreferredSize(new java.awt.Dimension(100, 22));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        jPanel8.add(jLabel13, gridBagConstraints);

        txt在庫範囲最大値.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        txt在庫範囲最大値.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txt在庫範囲最大値.setText("190.0");
        txt在庫範囲最大値.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        txt在庫範囲最大値.setMaximumSize(new java.awt.Dimension(80, 25));
        txt在庫範囲最大値.setMinimumSize(new java.awt.Dimension(80, 25));
        txt在庫範囲最大値.setPreferredSize(new java.awt.Dimension(80, 25));
        txt在庫範囲最大値.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txt在庫範囲最大値KeyPressed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(10, 2, 2, 2);
        jPanel8.add(txt在庫範囲最大値, gridBagConstraints);

        txt在庫範囲最小値.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        txt在庫範囲最小値.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txt在庫範囲最小値.setText("150.0");
        txt在庫範囲最小値.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        txt在庫範囲最小値.setMaximumSize(new java.awt.Dimension(80, 25));
        txt在庫範囲最小値.setMinimumSize(new java.awt.Dimension(80, 25));
        txt在庫範囲最小値.setPreferredSize(new java.awt.Dimension(80, 25));
        txt在庫範囲最小値.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txt在庫範囲最小値KeyPressed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        jPanel8.add(txt在庫範囲最小値, gridBagConstraints);

        jLabel59.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        jLabel59.setText(" ton");
        jLabel59.setMaximumSize(new java.awt.Dimension(60, 25));
        jLabel59.setMinimumSize(new java.awt.Dimension(60, 25));
        jLabel59.setPreferredSize(new java.awt.Dimension(60, 25));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(10, 2, 2, 2);
        jPanel8.add(jLabel59, gridBagConstraints);

        jLabel60.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        jLabel60.setText(" ton");
        jLabel60.setMaximumSize(new java.awt.Dimension(60, 25));
        jLabel60.setMinimumSize(new java.awt.Dimension(60, 25));
        jLabel60.setPreferredSize(new java.awt.Dimension(60, 25));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        jPanel8.add(jLabel60, gridBagConstraints);

        jLabel7.setBackground(new java.awt.Color(0, 51, 153));
        jLabel7.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(255, 255, 255));
        jLabel7.setText("　在庫警報設定");
        jLabel7.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        jLabel7.setMaximumSize(new java.awt.Dimension(150, 22));
        jLabel7.setMinimumSize(new java.awt.Dimension(145, 22));
        jLabel7.setOpaque(true);
        jLabel7.setPreferredSize(new java.awt.Dimension(145, 22));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        jPanel8.add(jLabel7, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 50);
        jPanel3.add(jPanel8, gridBagConstraints);

        jPanel9.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel9.setMaximumSize(new java.awt.Dimension(400, 115));
        jPanel9.setMinimumSize(new java.awt.Dimension(400, 115));
        jPanel9.setPreferredSize(new java.awt.Dimension(400, 115));
        jPanel9.setLayout(new java.awt.GridBagLayout());

        txt前月平均搬入量.setEditable(false);
        txt前月平均搬入量.setBackground(new java.awt.Color(241, 241, 241));
        txt前月平均搬入量.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        txt前月平均搬入量.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txt前月平均搬入量.setText("150.0");
        txt前月平均搬入量.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        txt前月平均搬入量.setFocusable(false);
        txt前月平均搬入量.setMaximumSize(new java.awt.Dimension(80, 25));
        txt前月平均搬入量.setMinimumSize(new java.awt.Dimension(80, 25));
        txt前月平均搬入量.setPreferredSize(new java.awt.Dimension(80, 25));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.SOUTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 5, 2, 2);
        jPanel9.add(txt前月平均搬入量, gridBagConstraints);

        txt前月搬入台数.setEditable(false);
        txt前月搬入台数.setBackground(new java.awt.Color(241, 241, 241));
        txt前月搬入台数.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        txt前月搬入台数.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txt前月搬入台数.setText("194");
        txt前月搬入台数.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        txt前月搬入台数.setFocusable(false);
        txt前月搬入台数.setMaximumSize(new java.awt.Dimension(80, 25));
        txt前月搬入台数.setMinimumSize(new java.awt.Dimension(80, 25));
        txt前月搬入台数.setPreferredSize(new java.awt.Dimension(80, 25));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 5, 2, 2);
        jPanel9.add(txt前月搬入台数, gridBagConstraints);

        jLabel14.setBackground(new java.awt.Color(0, 51, 153));
        jLabel14.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        jLabel14.setForeground(new java.awt.Color(255, 255, 255));
        jLabel14.setText("　前月実績");
        jLabel14.setMaximumSize(new java.awt.Dimension(150, 22));
        jLabel14.setMinimumSize(new java.awt.Dimension(150, 22));
        jLabel14.setOpaque(true);
        jLabel14.setPreferredSize(new java.awt.Dimension(150, 22));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        jPanel9.add(jLabel14, gridBagConstraints);

        jLabel15.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        jLabel15.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel15.setText("前月搬入台数");
        jLabel15.setMaximumSize(new java.awt.Dimension(130, 22));
        jLabel15.setMinimumSize(new java.awt.Dimension(130, 22));
        jLabel15.setPreferredSize(new java.awt.Dimension(130, 22));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 5, 2, 2);
        jPanel9.add(jLabel15, gridBagConstraints);

        jLabel55.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        jLabel55.setText("台");
        jLabel55.setMaximumSize(new java.awt.Dimension(60, 25));
        jLabel55.setMinimumSize(new java.awt.Dimension(60, 25));
        jLabel55.setPreferredSize(new java.awt.Dimension(60, 25));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.SOUTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        jPanel9.add(jLabel55, gridBagConstraints);

        jLabel56.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        jLabel56.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel56.setText("前月平均搬入量");
        jLabel56.setMaximumSize(new java.awt.Dimension(130, 22));
        jLabel56.setMinimumSize(new java.awt.Dimension(130, 22));
        jLabel56.setPreferredSize(new java.awt.Dimension(130, 22));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.SOUTHEAST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(2, 5, 2, 2);
        jPanel9.add(jLabel56, gridBagConstraints);

        jLabel57.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        jLabel57.setText("ton/台");
        jLabel57.setMaximumSize(new java.awt.Dimension(60, 25));
        jLabel57.setMinimumSize(new java.awt.Dimension(60, 25));
        jLabel57.setPreferredSize(new java.awt.Dimension(60, 25));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.SOUTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        jPanel9.add(jLabel57, gridBagConstraints);

        jLabel63.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        jLabel63.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel63.setText("前月搬入数量");
        jLabel63.setMaximumSize(new java.awt.Dimension(130, 22));
        jLabel63.setMinimumSize(new java.awt.Dimension(130, 22));
        jLabel63.setPreferredSize(new java.awt.Dimension(130, 22));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.SOUTHEAST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(2, 5, 2, 2);
        jPanel9.add(jLabel63, gridBagConstraints);

        txt前月搬入数量.setEditable(false);
        txt前月搬入数量.setBackground(new java.awt.Color(241, 241, 241));
        txt前月搬入数量.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        txt前月搬入数量.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txt前月搬入数量.setText("150.0");
        txt前月搬入数量.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        txt前月搬入数量.setFocusable(false);
        txt前月搬入数量.setMaximumSize(new java.awt.Dimension(80, 25));
        txt前月搬入数量.setMinimumSize(new java.awt.Dimension(80, 25));
        txt前月搬入数量.setPreferredSize(new java.awt.Dimension(80, 25));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.SOUTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 5, 2, 2);
        jPanel9.add(txt前月搬入数量, gridBagConstraints);

        jLabel64.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        jLabel64.setText("ton");
        jLabel64.setMaximumSize(new java.awt.Dimension(60, 25));
        jLabel64.setMinimumSize(new java.awt.Dimension(60, 25));
        jLabel64.setPreferredSize(new java.awt.Dimension(60, 25));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.SOUTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        jPanel9.add(jLabel64, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 50);
        jPanel3.add(jPanel9, gridBagConstraints);

        jPanel11.setMaximumSize(new java.awt.Dimension(30, 100));
        jPanel11.setMinimumSize(new java.awt.Dimension(30, 100));
        jPanel11.setPreferredSize(new java.awt.Dimension(30, 100));

        javax.swing.GroupLayout jPanel11Layout = new javax.swing.GroupLayout(jPanel11);
        jPanel11.setLayout(jPanel11Layout);
        jPanel11Layout.setHorizontalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 490, Short.MAX_VALUE)
        );
        jPanel11Layout.setVerticalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        jPanel3.add(jPanel11, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(10, 0, 0, 0);
        jPanel1.add(jPanel3, gridBagConstraints);

        jPanel4.setBorder(javax.swing.BorderFactory.createEtchedBorder(new java.awt.Color(102, 102, 255), null));
        jPanel4.setMaximumSize(new java.awt.Dimension(1100, 400));
        jPanel4.setMinimumSize(new java.awt.Dimension(1100, 400));
        jPanel4.setPreferredSize(new java.awt.Dimension(1100, 400));
        jPanel4.setLayout(new java.awt.GridBagLayout());

        jLabel18.setBackground(new java.awt.Color(0, 51, 153));
        jLabel18.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        jLabel18.setForeground(new java.awt.Color(255, 255, 255));
        jLabel18.setText("2015年05月の日別計画・実績");
        jLabel18.setMaximumSize(new java.awt.Dimension(300, 25));
        jLabel18.setMinimumSize(new java.awt.Dimension(300, 25));
        jLabel18.setOpaque(true);
        jLabel18.setPreferredSize(new java.awt.Dimension(300, 25));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 13;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        jPanel4.add(jLabel18, gridBagConstraints);

        jLabel19.setBackground(new java.awt.Color(0, 51, 153));
        jLabel19.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        jLabel19.setForeground(new java.awt.Color(255, 255, 255));
        jLabel19.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel19.setText("当月末在庫数");
        jLabel19.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        jLabel19.setMaximumSize(new java.awt.Dimension(100, 25));
        jLabel19.setMinimumSize(new java.awt.Dimension(100, 25));
        jLabel19.setOpaque(true);
        jLabel19.setPreferredSize(new java.awt.Dimension(100, 25));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 7;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 5, 2);
        jPanel4.add(jLabel19, gridBagConstraints);

        txt稼働日数.setEditable(false);
        txt稼働日数.setBackground(new java.awt.Color(241, 241, 241));
        txt稼働日数.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        txt稼働日数.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txt稼働日数.setText("26");
        txt稼働日数.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        txt稼働日数.setMaximumSize(new java.awt.Dimension(30, 25));
        txt稼働日数.setMinimumSize(new java.awt.Dimension(30, 25));
        txt稼働日数.setPreferredSize(new java.awt.Dimension(30, 25));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        jPanel4.add(txt稼働日数, gridBagConstraints);

        btn再計算.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        btn再計算.setForeground(new java.awt.Color(255, 0, 51));
        btn再計算.setText("再計算");
        btn再計算.setMargin(new java.awt.Insets(2, 2, 2, 2));
        btn再計算.setMaximumSize(new java.awt.Dimension(100, 31));
        btn再計算.setMinimumSize(new java.awt.Dimension(100, 31));
        btn再計算.setPreferredSize(new java.awt.Dimension(100, 31));
        btn再計算.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn再計算ActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 2, 2, 0);
        jPanel4.add(btn再計算, gridBagConstraints);

        jLabel20.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        jLabel20.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel20.setText("計");
        jLabel20.setMaximumSize(new java.awt.Dimension(30, 25));
        jLabel20.setMinimumSize(new java.awt.Dimension(30, 25));
        jLabel20.setPreferredSize(new java.awt.Dimension(30, 25));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        jPanel4.add(jLabel20, gridBagConstraints);

        jLabel21.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        jLabel21.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel21.setText("日");
        jLabel21.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        jLabel21.setMaximumSize(new java.awt.Dimension(30, 25));
        jLabel21.setMinimumSize(new java.awt.Dimension(30, 25));
        jLabel21.setPreferredSize(new java.awt.Dimension(30, 25));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        jPanel4.add(jLabel21, gridBagConstraints);

        jLabel22.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        jLabel22.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel22.setText("ton");
        jLabel22.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        jLabel22.setMaximumSize(new java.awt.Dimension(30, 25));
        jLabel22.setMinimumSize(new java.awt.Dimension(30, 25));
        jLabel22.setPreferredSize(new java.awt.Dimension(30, 25));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        jPanel4.add(jLabel22, gridBagConstraints);

        jLabel23.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        jLabel23.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel23.setText("台");
        jLabel23.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        jLabel23.setMaximumSize(new java.awt.Dimension(30, 25));
        jLabel23.setMinimumSize(new java.awt.Dimension(30, 25));
        jLabel23.setPreferredSize(new java.awt.Dimension(30, 25));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        jPanel4.add(jLabel23, gridBagConstraints);

        jLabel24.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        jLabel24.setText("ton");
        jLabel24.setMaximumSize(new java.awt.Dimension(30, 25));
        jLabel24.setMinimumSize(new java.awt.Dimension(30, 25));
        jLabel24.setPreferredSize(new java.awt.Dimension(30, 25));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 9;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 5, 0);
        jPanel4.add(jLabel24, gridBagConstraints);

        jLabel25.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        jLabel25.setText("ton");
        jLabel25.setMaximumSize(new java.awt.Dimension(30, 25));
        jLabel25.setMinimumSize(new java.awt.Dimension(30, 25));
        jLabel25.setPreferredSize(new java.awt.Dimension(30, 25));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 9;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        jPanel4.add(jLabel25, gridBagConstraints);

        txt搬入実績台数.setEditable(false);
        txt搬入実績台数.setBackground(new java.awt.Color(241, 241, 241));
        txt搬入実績台数.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        txt搬入実績台数.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txt搬入実績台数.setText("68");
        txt搬入実績台数.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        txt搬入実績台数.setMaximumSize(new java.awt.Dimension(70, 25));
        txt搬入実績台数.setMinimumSize(new java.awt.Dimension(70, 25));
        txt搬入実績台数.setPreferredSize(new java.awt.Dimension(70, 25));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        jPanel4.add(txt搬入実績台数, gridBagConstraints);

        txt搬入実績数量.setEditable(false);
        txt搬入実績数量.setBackground(new java.awt.Color(241, 241, 241));
        txt搬入実績数量.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        txt搬入実績数量.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txt搬入実績数量.setText("440.8");
        txt搬入実績数量.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        txt搬入実績数量.setMaximumSize(new java.awt.Dimension(70, 25));
        txt搬入実績数量.setMinimumSize(new java.awt.Dimension(70, 25));
        txt搬入実績数量.setPreferredSize(new java.awt.Dimension(70, 25));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        jPanel4.add(txt搬入実績数量, gridBagConstraints);

        txt前月末在庫数.setEditable(false);
        txt前月末在庫数.setBackground(new java.awt.Color(241, 241, 241));
        txt前月末在庫数.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        txt前月末在庫数.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txt前月末在庫数.setText("150.0");
        txt前月末在庫数.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        txt前月末在庫数.setMaximumSize(new java.awt.Dimension(96, 25));
        txt前月末在庫数.setMinimumSize(new java.awt.Dimension(96, 25));
        txt前月末在庫数.setPreferredSize(new java.awt.Dimension(96, 25));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 8;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        jPanel4.add(txt前月末在庫数, gridBagConstraints);

        txt前月差分.setEditable(false);
        txt前月差分.setBackground(new java.awt.Color(241, 241, 241));
        txt前月差分.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        txt前月差分.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txt前月差分.setText("-2.6");
        txt前月差分.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        txt前月差分.setMaximumSize(new java.awt.Dimension(96, 25));
        txt前月差分.setMinimumSize(new java.awt.Dimension(96, 25));
        txt前月差分.setPreferredSize(new java.awt.Dimension(96, 25));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 8;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        jPanel4.add(txt前月差分, gridBagConstraints);

        txt当月末在庫数.setEditable(false);
        txt当月末在庫数.setBackground(new java.awt.Color(241, 241, 241));
        txt当月末在庫数.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        txt当月末在庫数.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txt当月末在庫数.setText("147.4");
        txt当月末在庫数.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        txt当月末在庫数.setMaximumSize(new java.awt.Dimension(96, 25));
        txt当月末在庫数.setMinimumSize(new java.awt.Dimension(96, 25));
        txt当月末在庫数.setPreferredSize(new java.awt.Dimension(96, 25));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 8;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 5, 2);
        jPanel4.add(txt当月末在庫数, gridBagConstraints);

        jLabel26.setBackground(new java.awt.Color(0, 51, 153));
        jLabel26.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        jLabel26.setForeground(new java.awt.Color(255, 255, 255));
        jLabel26.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel26.setText("前月差分");
        jLabel26.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        jLabel26.setMaximumSize(new java.awt.Dimension(100, 25));
        jLabel26.setMinimumSize(new java.awt.Dimension(100, 25));
        jLabel26.setOpaque(true);
        jLabel26.setPreferredSize(new java.awt.Dimension(100, 25));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 7;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        jPanel4.add(jLabel26, gridBagConstraints);

        dScroll.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        dScroll.setMaximumSize(new java.awt.Dimension(1075, 600));
        dScroll.setMinimumSize(new java.awt.Dimension(1075, 600));
        dScroll.setPreferredSize(new java.awt.Dimension(1075, 600));

        jPanel13.setMaximumSize(new java.awt.Dimension(1065, 775));
        jPanel13.setMinimumSize(new java.awt.Dimension(1065, 775));
        jPanel13.setPreferredSize(new java.awt.Dimension(1065, 775));
        jPanel13.setLayout(new java.awt.GridBagLayout());
        dScroll.setViewportView(jPanel13);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.gridwidth = 13;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weighty = 1.0;
        jPanel4.add(dScroll, gridBagConstraints);

        jLabel34.setBackground(new java.awt.Color(102, 102, 255));
        jLabel34.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        jLabel34.setForeground(new java.awt.Color(255, 255, 255));
        jLabel34.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel34.setText("数量");
        jLabel34.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jLabel34.setMaximumSize(new java.awt.Dimension(30, 25));
        jLabel34.setMinimumSize(new java.awt.Dimension(30, 25));
        jLabel34.setOpaque(true);
        jLabel34.setPreferredSize(new java.awt.Dimension(30, 25));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        jPanel4.add(jLabel34, gridBagConstraints);

        jLabel35.setBackground(new java.awt.Color(102, 102, 255));
        jLabel35.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        jLabel35.setForeground(new java.awt.Color(255, 255, 255));
        jLabel35.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel35.setText("処理実績");
        jLabel35.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jLabel35.setMaximumSize(new java.awt.Dimension(104, 25));
        jLabel35.setMinimumSize(new java.awt.Dimension(104, 25));
        jLabel35.setOpaque(true);
        jLabel35.setPreferredSize(new java.awt.Dimension(104, 25));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 7;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        jPanel4.add(jLabel35, gridBagConstraints);

        jLabel36.setBackground(new java.awt.Color(102, 102, 255));
        jLabel36.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        jLabel36.setForeground(new java.awt.Color(255, 255, 255));
        jLabel36.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel36.setText("搬入実績");
        jLabel36.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jLabel36.setMaximumSize(new java.awt.Dimension(280, 25));
        jLabel36.setMinimumSize(new java.awt.Dimension(280, 25));
        jLabel36.setOpaque(true);
        jLabel36.setPreferredSize(new java.awt.Dimension(280, 25));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        jPanel4.add(jLabel36, gridBagConstraints);

        jLabel37.setBackground(new java.awt.Color(102, 102, 255));
        jLabel37.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        jLabel37.setForeground(new java.awt.Color(255, 255, 255));
        jLabel37.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel37.setText("実数量");
        jLabel37.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jLabel37.setMaximumSize(new java.awt.Dimension(100, 25));
        jLabel37.setMinimumSize(new java.awt.Dimension(100, 25));
        jLabel37.setOpaque(true);
        jLabel37.setPreferredSize(new java.awt.Dimension(100, 25));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 9;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        jPanel4.add(jLabel37, gridBagConstraints);

        jLabel38.setBackground(new java.awt.Color(102, 102, 255));
        jLabel38.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        jLabel38.setForeground(new java.awt.Color(255, 255, 255));
        jLabel38.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel38.setText("累計数量");
        jLabel38.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jLabel38.setMaximumSize(new java.awt.Dimension(104, 25));
        jLabel38.setMinimumSize(new java.awt.Dimension(104, 25));
        jLabel38.setOpaque(true);
        jLabel38.setPreferredSize(new java.awt.Dimension(104, 25));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 7;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        jPanel4.add(jLabel38, gridBagConstraints);

        jLabel39.setBackground(new java.awt.Color(102, 102, 255));
        jLabel39.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        jLabel39.setForeground(new java.awt.Color(255, 255, 255));
        jLabel39.setText("　在庫");
        jLabel39.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jLabel39.setMaximumSize(new java.awt.Dimension(100, 25));
        jLabel39.setMinimumSize(new java.awt.Dimension(100, 25));
        jLabel39.setOpaque(true);
        jLabel39.setPreferredSize(new java.awt.Dimension(100, 25));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 8;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 5;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        jPanel4.add(jLabel39, gridBagConstraints);

        jLabel40.setBackground(new java.awt.Color(102, 102, 255));
        jLabel40.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        jLabel40.setForeground(new java.awt.Color(255, 255, 255));
        jLabel40.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel40.setText("日");
        jLabel40.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jLabel40.setMaximumSize(new java.awt.Dimension(30, 25));
        jLabel40.setMinimumSize(new java.awt.Dimension(30, 25));
        jLabel40.setOpaque(true);
        jLabel40.setPreferredSize(new java.awt.Dimension(30, 25));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        jPanel4.add(jLabel40, gridBagConstraints);

        jLabel41.setBackground(new java.awt.Color(102, 102, 255));
        jLabel41.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        jLabel41.setForeground(new java.awt.Color(255, 255, 255));
        jLabel41.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel41.setText("数量");
        jLabel41.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jLabel41.setMaximumSize(new java.awt.Dimension(100, 25));
        jLabel41.setMinimumSize(new java.awt.Dimension(100, 25));
        jLabel41.setOpaque(true);
        jLabel41.setPreferredSize(new java.awt.Dimension(100, 25));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        jPanel4.add(jLabel41, gridBagConstraints);

        jLabel42.setBackground(new java.awt.Color(102, 102, 255));
        jLabel42.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        jLabel42.setForeground(new java.awt.Color(255, 255, 255));
        jLabel42.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel42.setText("搬入予定設定");
        jLabel42.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jLabel42.setMaximumSize(new java.awt.Dimension(110, 25));
        jLabel42.setMinimumSize(new java.awt.Dimension(110, 25));
        jLabel42.setOpaque(true);
        jLabel42.setPreferredSize(new java.awt.Dimension(110, 25));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        jPanel4.add(jLabel42, gridBagConstraints);

        jLabel43.setBackground(new java.awt.Color(102, 102, 255));
        jLabel43.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        jLabel43.setForeground(new java.awt.Color(255, 255, 255));
        jLabel43.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel43.setText("数量");
        jLabel43.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jLabel43.setMaximumSize(new java.awt.Dimension(80, 25));
        jLabel43.setMinimumSize(new java.awt.Dimension(80, 25));
        jLabel43.setOpaque(true);
        jLabel43.setPreferredSize(new java.awt.Dimension(80, 25));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        jPanel4.add(jLabel43, gridBagConstraints);

        jLabel44.setBackground(new java.awt.Color(102, 102, 255));
        jLabel44.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        jLabel44.setForeground(new java.awt.Color(255, 255, 255));
        jLabel44.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel44.setText("台数");
        jLabel44.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jLabel44.setMaximumSize(new java.awt.Dimension(80, 25));
        jLabel44.setMinimumSize(new java.awt.Dimension(80, 25));
        jLabel44.setOpaque(true);
        jLabel44.setPreferredSize(new java.awt.Dimension(80, 25));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        jPanel4.add(jLabel44, gridBagConstraints);

        jLabel45.setBackground(new java.awt.Color(102, 102, 255));
        jLabel45.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        jLabel45.setForeground(new java.awt.Color(255, 255, 255));
        jLabel45.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel45.setText("累計数量");
        jLabel45.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jLabel45.setMaximumSize(new java.awt.Dimension(100, 25));
        jLabel45.setMinimumSize(new java.awt.Dimension(100, 25));
        jLabel45.setOpaque(true);
        jLabel45.setPreferredSize(new java.awt.Dimension(100, 25));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 6;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        jPanel4.add(jLabel45, gridBagConstraints);

        jLabel46.setBackground(new java.awt.Color(102, 102, 255));
        jLabel46.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        jLabel46.setForeground(new java.awt.Color(255, 255, 255));
        jLabel46.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel46.setText("有");
        jLabel46.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jLabel46.setMaximumSize(new java.awt.Dimension(30, 25));
        jLabel46.setMinimumSize(new java.awt.Dimension(30, 25));
        jLabel46.setOpaque(true);
        jLabel46.setPreferredSize(new java.awt.Dimension(30, 25));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        jPanel4.add(jLabel46, gridBagConstraints);

        txt搬入予定設定数量.setEditable(false);
        txt搬入予定設定数量.setBackground(new java.awt.Color(241, 241, 241));
        txt搬入予定設定数量.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        txt搬入予定設定数量.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txt搬入予定設定数量.setText("1700.0");
        txt搬入予定設定数量.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        txt搬入予定設定数量.setMaximumSize(new java.awt.Dimension(70, 25));
        txt搬入予定設定数量.setMinimumSize(new java.awt.Dimension(70, 25));
        txt搬入予定設定数量.setPreferredSize(new java.awt.Dimension(70, 25));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        jPanel4.add(txt搬入予定設定数量, gridBagConstraints);

        jLabel47.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        jLabel47.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel47.setText("ton");
        jLabel47.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        jLabel47.setMaximumSize(new java.awt.Dimension(30, 25));
        jLabel47.setMinimumSize(new java.awt.Dimension(30, 25));
        jLabel47.setPreferredSize(new java.awt.Dimension(30, 25));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        jPanel4.add(jLabel47, gridBagConstraints);

        jLabel48.setBackground(new java.awt.Color(102, 102, 255));
        jLabel48.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        jLabel48.setForeground(new java.awt.Color(255, 255, 255));
        jLabel48.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel48.setText("処理予定設定");
        jLabel48.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jLabel48.setMaximumSize(new java.awt.Dimension(100, 25));
        jLabel48.setMinimumSize(new java.awt.Dimension(100, 25));
        jLabel48.setOpaque(true);
        jLabel48.setPreferredSize(new java.awt.Dimension(100, 25));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        jPanel4.add(jLabel48, gridBagConstraints);

        jLabel49.setBackground(new java.awt.Color(0, 51, 153));
        jLabel49.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        jLabel49.setForeground(new java.awt.Color(255, 255, 255));
        jLabel49.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel49.setText("前月末在庫数");
        jLabel49.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        jLabel49.setMaximumSize(new java.awt.Dimension(100, 25));
        jLabel49.setMinimumSize(new java.awt.Dimension(100, 25));
        jLabel49.setOpaque(true);
        jLabel49.setPreferredSize(new java.awt.Dimension(100, 25));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 7;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        jPanel4.add(jLabel49, gridBagConstraints);

        jLabel50.setBackground(new java.awt.Color(102, 102, 255));
        jLabel50.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        jLabel50.setForeground(new java.awt.Color(255, 255, 255));
        jLabel50.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel50.setText("+平均誤差");
        jLabel50.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jLabel50.setMaximumSize(new java.awt.Dimension(80, 25));
        jLabel50.setMinimumSize(new java.awt.Dimension(80, 25));
        jLabel50.setOpaque(true);
        jLabel50.setPreferredSize(new java.awt.Dimension(80, 25));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 10;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        jPanel4.add(jLabel50, gridBagConstraints);

        jLabel51.setBackground(new java.awt.Color(102, 102, 255));
        jLabel51.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        jLabel51.setForeground(new java.awt.Color(255, 255, 255));
        jLabel51.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel51.setText("警報ランプ");
        jLabel51.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jLabel51.setMaximumSize(new java.awt.Dimension(80, 25));
        jLabel51.setMinimumSize(new java.awt.Dimension(80, 25));
        jLabel51.setOpaque(true);
        jLabel51.setPreferredSize(new java.awt.Dimension(80, 25));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 12;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        jPanel4.add(jLabel51, gridBagConstraints);

        jLabel52.setBackground(new java.awt.Color(102, 102, 255));
        jLabel52.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        jLabel52.setForeground(new java.awt.Color(255, 255, 255));
        jLabel52.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel52.setText("加減数");
        jLabel52.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jLabel52.setMaximumSize(new java.awt.Dimension(100, 25));
        jLabel52.setMinimumSize(new java.awt.Dimension(100, 25));
        jLabel52.setOpaque(true);
        jLabel52.setPreferredSize(new java.awt.Dimension(100, 25));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 8;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        jPanel4.add(jLabel52, gridBagConstraints);

        jLabel53.setBackground(new java.awt.Color(102, 102, 255));
        jLabel53.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        jLabel53.setForeground(new java.awt.Color(255, 255, 255));
        jLabel53.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel53.setText("在庫上限");
        jLabel53.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jLabel53.setMaximumSize(new java.awt.Dimension(100, 25));
        jLabel53.setMinimumSize(new java.awt.Dimension(100, 25));
        jLabel53.setOpaque(true);
        jLabel53.setPreferredSize(new java.awt.Dimension(100, 25));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 11;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        jPanel4.add(jLabel53, gridBagConstraints);

        txt処理予定設定数量.setEditable(false);
        txt処理予定設定数量.setBackground(new java.awt.Color(241, 241, 241));
        txt処理予定設定数量.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        txt処理予定設定数量.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txt処理予定設定数量.setText("1720");
        txt処理予定設定数量.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        txt処理予定設定数量.setMaximumSize(new java.awt.Dimension(70, 25));
        txt処理予定設定数量.setMinimumSize(new java.awt.Dimension(70, 25));
        txt処理予定設定数量.setPreferredSize(new java.awt.Dimension(70, 25));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        jPanel4.add(txt処理予定設定数量, gridBagConstraints);

        jLabel54.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        jLabel54.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel54.setText("ton");
        jLabel54.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        jLabel54.setMaximumSize(new java.awt.Dimension(30, 25));
        jLabel54.setMinimumSize(new java.awt.Dimension(30, 25));
        jLabel54.setPreferredSize(new java.awt.Dimension(30, 25));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        jPanel4.add(jLabel54, gridBagConstraints);

        jLabel62.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        jLabel62.setForeground(new java.awt.Color(255, 0, 51));
        jLabel62.setText("在庫情報を再計算します。");
        jLabel62.setMaximumSize(new java.awt.Dimension(180, 25));
        jLabel62.setMinimumSize(new java.awt.Dimension(180, 25));
        jLabel62.setPreferredSize(new java.awt.Dimension(180, 25));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.gridwidth = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 2, 0);
        jPanel4.add(jLabel62, gridBagConstraints);

        jButton11.setFont(new java.awt.Font("ＭＳ Ｐゴシック", 0, 12)); // NOI18N
        jButton11.setText("↑");
        jButton11.setMargin(new java.awt.Insets(0, 0, 0, 0));
        jButton11.setMaximumSize(new java.awt.Dimension(40, 31));
        jButton11.setMinimumSize(new java.awt.Dimension(40, 31));
        jButton11.setPreferredSize(new java.awt.Dimension(40, 31));
        jButton11.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton11ActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 12;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        jPanel4.add(jButton11, gridBagConstraints);

        jButton12.setFont(new java.awt.Font("ＭＳ Ｐゴシック", 0, 12)); // NOI18N
        jButton12.setLabel("↓");
        jButton12.setMargin(new java.awt.Insets(0, 0, 0, 0));
        jButton12.setMaximumSize(new java.awt.Dimension(40, 31));
        jButton12.setMinimumSize(new java.awt.Dimension(40, 31));
        jButton12.setPreferredSize(new java.awt.Dimension(40, 31));
        jButton12.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton12ActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 12;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        jPanel4.add(jButton12, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 50, 0, 0);
        jPanel1.add(jPanel4, gridBagConstraints);

        jScrollPane1.setViewportView(jPanel1);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        add(jScrollPane1, gridBagConstraints);

        jLabel1.setBackground(new java.awt.Color(51, 51, 255));
        jLabel1.setFont(new java.awt.Font("メイリオ", 0, 18)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("　在庫管理詳細画面");
        jLabel1.setMaximumSize(new java.awt.Dimension(1200, 35));
        jLabel1.setMinimumSize(new java.awt.Dimension(1200, 35));
        jLabel1.setOpaque(true);
        jLabel1.setPreferredSize(new java.awt.Dimension(1200, 35));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        add(jLabel1, gridBagConstraints);

        jPanel5.setMaximumSize(new java.awt.Dimension(1200, 50));
        jPanel5.setMinimumSize(new java.awt.Dimension(1200, 50));
        jPanel5.setPreferredSize(new java.awt.Dimension(1200, 50));
        jPanel5.setLayout(new java.awt.GridBagLayout());

        jButton5.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        jButton5.setText("登録");
        jButton5.setMargin(new java.awt.Insets(2, 2, 2, 2));
        jButton5.setMaximumSize(new java.awt.Dimension(120, 31));
        jButton5.setMinimumSize(new java.awt.Dimension(120, 31));
        jButton5.setPreferredSize(new java.awt.Dimension(120, 31));
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        jPanel5.add(jButton5, gridBagConstraints);

        jButton6.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        jButton6.setText("閉じる");
        jButton6.setMargin(new java.awt.Insets(2, 2, 2, 2));
        jButton6.setMaximumSize(new java.awt.Dimension(120, 31));
        jButton6.setMinimumSize(new java.awt.Dimension(120, 31));
        jButton6.setPreferredSize(new java.awt.Dimension(120, 31));
        jButton6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton6ActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 100);
        jPanel5.add(jButton6, gridBagConstraints);

        jButton7.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        jButton7.setForeground(new java.awt.Color(255, 0, 0));
        jButton7.setText("警報音停止");
        jButton7.setMargin(new java.awt.Insets(2, 2, 2, 2));
        jButton7.setMaximumSize(new java.awt.Dimension(120, 31));
        jButton7.setMinimumSize(new java.awt.Dimension(120, 31));
        jButton7.setPreferredSize(new java.awt.Dimension(120, 31));
        jButton7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton7ActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 60, 2, 2);
        jPanel5.add(jButton7, gridBagConstraints);

        jPanel14.setPreferredSize(new java.awt.Dimension(100, 20));

        javax.swing.GroupLayout jPanel14Layout = new javax.swing.GroupLayout(jPanel14);
        jPanel14.setLayout(jPanel14Layout);
        jPanel14Layout.setHorizontalGroup(
            jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 678, Short.MAX_VALUE)
        );
        jPanel14Layout.setVerticalGroup(
            jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 35, Short.MAX_VALUE)
        );

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        jPanel5.add(jPanel14, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        add(jPanel5, gridBagConstraints);
    }// </editor-fold>//GEN-END:initComponents

    private void btn再表示ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn再表示ActionPerformed
        // TODO add your handling code here:
        term_date = DBManager.convert((dft.getValue()).getTime());
        updateView();
    }//GEN-LAST:event_btn再表示ActionPerformed

    private void btn再計算ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn再計算ActionPerformed
        // TODO add your handling code here:
        term_date = DBManager.convert((dft.getValue()).getTime());
        //明細手入力分（処理予定数量）をDB登録
        d再計算02();
        //受入実績等の計算
        d再計算2();
        updateView();
    }//GEN-LAST:event_btn再計算ActionPerformed

    private void btn追加ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn追加ActionPerformed
        // TODO add your handling code here:
        //ヘッダー行追加
        copyColumns();
        gzd1 = new GoodsZaikoDetailPane11(o1.size() - 1, (Map) o1.get(o1.size() - 1));
        gzd01.add(o1.size() - 1, gzd1);
        jPanel6.add(
                 gzd01.get(o1.size() - 1),
                 new GridBagConstraints(
                          0, o1.size() - 1, 1, 1, 0.0, 0.0,
                          GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
                          new Insets(0, 0, 0, 0), 0, 0));

        //パネル内の再描画
        dmy1.validate();
        jPanel6.validate();
        hScroll.validate();

        for (int i = 0; i < o1.size(); i++) {
            gzd01.get(i).h_row = i;
        }
        h_max_row = o1.size();

    }//GEN-LAST:event_btn追加ActionPerformed

    private void btn削除ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn削除ActionPerformed
        // TODO add your handling code here:
        //ヘッダー行削除
        if (o1.size() > 0) {
            //計画データ削除
            gzd01.get(h_row).delete(term_date);
            //スクロールパネルから表示行パネル削除
            jPanel6.remove(gzd01.get(h_row));
            //表示行パネル削除
            gzd01.remove(h_row);
            //配列の削除
            o1.remove(h_row);

        }
        for (int i = 0; i < o1.size(); i++) {
            gzd01.get(i).h_row = i;
        }

        h小計計算();

        //パネル内の再描画
        dmy1.validate();
        jPanel6.validate();
        hScroll.validate();

    }//GEN-LAST:event_btn削除ActionPerformed

    private void btn自動補正ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn自動補正ActionPerformed
        // TODO add your handling code here:
        term_date = DBManager.convert((dft.getValue()).getTime());
        //グリッドに任意に行を追加しているため
        //初めに再計算（DBデータ登録）
        h再計算();
        //明細の再計算
        d再計算();
        updateView();

    }//GEN-LAST:event_btn自動補正ActionPerformed

    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton6ActionPerformed
        // TODO add your handling code here:
        doClose();
    }//GEN-LAST:event_jButton6ActionPerformed

    private void btn再表示KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_btn再表示KeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            term_date = DBManager.convert((dft.getValue()).getTime());
            h再計算();
            updateView();

        }

    }//GEN-LAST:event_btn再表示KeyPressed

    private void jButton7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton7ActionPerformed
        // TODO add your handling code here:

        int result = JOptionPane.showConfirmDialog(
                 GoodsZaikoDetailPane0.this,
                 "警報を停止します。よろしいですか？",
                 "確認",
                 JOptionPane.YES_NO_OPTION);
        if (JOptionPane.YES_OPTION == result) {
            警報停止();
        }
    }//GEN-LAST:event_jButton7ActionPerformed

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        // TODO add your handling code here:
        int result = JOptionPane.showConfirmDialog(
                 GoodsZaikoDetailPane0.this,
                 "設定値を登録します。よろしいですか？",
                 "確認",
                 JOptionPane.YES_NO_OPTION);
        if (JOptionPane.YES_OPTION == result) {
            設定値登録();
            doClose();
        }
    }//GEN-LAST:event_jButton5ActionPerformed

    private void txt在庫範囲最大値KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt在庫範囲最大値KeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            txt在庫範囲最大値.transferFocus();
            evt.consume();
        }
    }//GEN-LAST:event_txt在庫範囲最大値KeyPressed

    private void txt在庫範囲最小値KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt在庫範囲最小値KeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            txt在庫範囲最小値.transferFocus();
            evt.consume();
        }
    }//GEN-LAST:event_txt在庫範囲最小値KeyPressed

    private void txt達成率最大値KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt達成率最大値KeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            txt達成率最大値.transferFocus();
            evt.consume();
        }
    }//GEN-LAST:event_txt達成率最大値KeyPressed

    private void txt達成率最小値KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt達成率最小値KeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            txt達成率最小値.transferFocus();
            evt.consume();
        }
    }//GEN-LAST:event_txt達成率最小値KeyPressed

    private void jButton10ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton10ActionPerformed
        // TODO add your handling code here:
        //スクロールバー取得
        //エンターキーにてスクロールする
        JScrollBar vBar = GoodsZaikoDetailPane0.hScroll.getVerticalScrollBar();
        int vBarSize = vBar.getMinimum();
        vBar.setValue(vBarSize);
    }//GEN-LAST:event_jButton10ActionPerformed

    private void jButton8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton8ActionPerformed
        // TODO add your handling code here:
        JScrollBar vBar = GoodsZaikoDetailPane0.hScroll.getVerticalScrollBar();
        int vBarSize = vBar.getMaximum();
        vBar.setValue(vBarSize);
    }//GEN-LAST:event_jButton8ActionPerformed

    private void jButton12ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton12ActionPerformed
        // TODO add your handling code here:
        JScrollBar vBar = GoodsZaikoDetailPane0.dScroll.getVerticalScrollBar();
        int vBarSize = vBar.getMaximum();
        vBar.setValue(vBarSize);
    }//GEN-LAST:event_jButton12ActionPerformed

    private void jButton11ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton11ActionPerformed
        // TODO add your handling code here:
        //エンターキーにてスクロールする
        JScrollBar vBar = GoodsZaikoDetailPane0.dScroll.getVerticalScrollBar();
        int vBarSize = vBar.getMinimum();
        vBar.setValue(vBarSize);
    }//GEN-LAST:event_jButton11ActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btn再表示;
    private javax.swing.JButton btn再計算;
    private javax.swing.JButton btn削除;
    private javax.swing.JButton btn自動補正;
    private javax.swing.JButton btn追加;
    public static javax.swing.JScrollPane dScroll;
    private javax.swing.JCheckBox f在庫警報設定_;
    private javax.swing.JCheckBox f達成率警報設定_;
    public static javax.swing.JScrollPane hScroll;
    private javax.swing.JButton jButton10;
    private javax.swing.JButton jButton11;
    private javax.swing.JButton jButton12;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton6;
    private javax.swing.JButton jButton7;
    private javax.swing.JButton jButton8;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel29;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel30;
    private javax.swing.JLabel jLabel31;
    private javax.swing.JLabel jLabel32;
    private javax.swing.JLabel jLabel33;
    private javax.swing.JLabel jLabel34;
    private javax.swing.JLabel jLabel35;
    private javax.swing.JLabel jLabel36;
    private javax.swing.JLabel jLabel37;
    private javax.swing.JLabel jLabel38;
    private javax.swing.JLabel jLabel39;
    private javax.swing.JLabel jLabel40;
    private javax.swing.JLabel jLabel41;
    private javax.swing.JLabel jLabel42;
    private javax.swing.JLabel jLabel43;
    private javax.swing.JLabel jLabel44;
    private javax.swing.JLabel jLabel45;
    private javax.swing.JLabel jLabel46;
    private javax.swing.JLabel jLabel47;
    private javax.swing.JLabel jLabel48;
    private javax.swing.JLabel jLabel49;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel50;
    private javax.swing.JLabel jLabel51;
    private javax.swing.JLabel jLabel52;
    private javax.swing.JLabel jLabel53;
    private javax.swing.JLabel jLabel54;
    private javax.swing.JLabel jLabel55;
    private javax.swing.JLabel jLabel56;
    private javax.swing.JLabel jLabel57;
    private javax.swing.JLabel jLabel58;
    private javax.swing.JLabel jLabel59;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel60;
    private javax.swing.JLabel jLabel61;
    private javax.swing.JLabel jLabel62;
    private javax.swing.JLabel jLabel63;
    private javax.swing.JLabel jLabel64;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel13;
    private javax.swing.JPanel jPanel14;
    private javax.swing.JPanel jPanel15;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JPanel p所属日;
    private javax.swing.JTextField txt処理予定設定数量;
    private javax.swing.JTextField txt前月差分;
    private javax.swing.JTextField txt前月平均搬入量;
    private javax.swing.JTextField txt前月搬入台数;
    private javax.swing.JTextField txt前月搬入数量;
    private javax.swing.JTextField txt前月末在庫数;
    private javax.swing.JTextField txt在庫範囲最大値;
    private javax.swing.JTextField txt在庫範囲最小値;
    private javax.swing.JTextField txt当月末在庫数;
    private javax.swing.JTextField txt搬入予定設定数量;
    private javax.swing.JTextField txt搬入実績台数;
    private javax.swing.JTextField txt搬入実績数量;
    private static javax.swing.JTextField txt月搬入予定量;
    private static javax.swing.JTextField txt月搬入実績;
    private javax.swing.JTextField txt稼働日数;
    private javax.swing.JTextField txt達成率最大値;
    private javax.swing.JTextField txt達成率最小値;
    // End of variables declaration//GEN-END:variables

    private void updateView() {

        dmy1 = null;
        dmy2 = null;
        o1 = null;
        o2 = null;
        gzd1 = null;
        gzd2 = null;
        gzd01 = null;

        d前月搬入数量 = 0.;
        d前月平均搬入量 = 0.;
        d前月末在庫数 = 0.;
        d前月差分 = 0.;
        i前月搬入台数 = 0;

        //セットされている明細行を初期化
        jPanel6.removeAll();
        jPanel6.repaint();
        jPanel13.removeAll();
        jPanel13.repaint();
        //パネル内の再描画(ヘッダー)
        jPanel6.validate();
        hScroll.validate();
        //パネル内の再描画(明細)
        jPanel13.validate();
        dScroll.validate();
        gzd01 = new ArrayList<GoodsZaikoDetailPane11>();

        //処分計画データ参照
        d参照();
        ///在庫計画問い合わせ
        o1 = mSQLCommon.GetZaikoData1(term_date, goods_id_);
        o2 = mSQLCommon.GetZaikoData2(term_date, goods_id_);

        h_max_row = o1.size();

        gzd2 = new GoodsZaikoDetailPane2[o2.length];

        jTextField1.setText((String) o2[0][2]);
        jLabel27.setText(sdf01.format(term_date));
        jLabel18.setText(sdf02.format(term_date));

        dmy1 = new JPanel();
//        dmy1.setBackground(Color.yellow);
        dmy1.setMinimumSize(new Dimension(120, 400));
        dmy1.setPreferredSize(new Dimension(120, 400));
        dmy2 = new JPanel();

        for (int i = 0; i < o1.size(); i++) {
            gzd1 = new GoodsZaikoDetailPane11(i, (Map) o1.get(i));
            gzd01.add(i, gzd1);
            jPanel6.add(
                     gzd01.get(i),
                     new GridBagConstraints(
                              0, i, 1, 1, 0.0, 0.0,
                              GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
                              new Insets(0, 0, 0, 0), 0, 0));
        }

        jPanel6.add(
                 dmy1,
                 new GridBagConstraints(
                          0, 100, 1, 1, 0.0, 1.0,
                          GridBagConstraints.WEST, GridBagConstraints.BOTH,
                          new Insets(0, 0, 0, 0), 0, 0));

        for (int i = 0; i < gzd2.length; i++) {
            gzd2[i] = new GoodsZaikoDetailPane2(term_date, o2[i]);
            jPanel13.add(
                     gzd2[i],
                     new GridBagConstraints(
                              0, i, 1, 1, 0.0, 0.0,
                              GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
                              new Insets(0, 0, 0, 0), 0, 0));
        }

        jPanel13.add(
                 dmy2,
                 new GridBagConstraints(
                          0, 50, 1, 1, 0.0, 1.0,
                          GridBagConstraints.WEST, GridBagConstraints.BOTH,
                          new Insets(0, 0, 0, 0), 0, 0));

        get前月実績および前月末在庫数取得();
        h小計計算();
        d小計計算();

        //パネル内の再描画(ヘッダー)
        dmy1.validate();
        jPanel6.validate();
        hScroll.validate();
        //パネル内の再描画(明細)
        dmy2.validate();
        jPanel13.validate();
        dScroll.validate();

        txt在庫範囲最大値.setText(String.valueOf((Double) model_.getValueAt(row_, "d在庫範囲最大値")));
        txt在庫範囲最小値.setText(String.valueOf((Double) model_.getValueAt(row_, "d在庫範囲最小値")));
        txt達成率最大値.setText(String.valueOf((Double) model_.getValueAt(row_, "d達成率最大値")));
        txt達成率最小値.setText(String.valueOf((Double) model_.getValueAt(row_, "d達成率最小値")));
        f在庫警報設定_.setSelected((Boolean) model_.getValueAt(row_, "f在庫警報設定"));
        f達成率警報設定_.setSelected((Boolean) model_.getValueAt(row_, "f達成率警報設定"));

        txt前月搬入数量.setText(String.valueOf(d前月搬入数量));
        txt前月搬入台数.setText(String.valueOf(i前月搬入台数));
        txt前月平均搬入量.setText(String.valueOf(d前月平均搬入量));
        txt当月末在庫数.setText(String.valueOf(Double.valueOf((String) o2[o2.length - 1][11])));
        txt前月末在庫数.setText(String.valueOf(d前月末在庫数));
        d前月差分 = Double.valueOf((String) o2[o2.length - 1][11]) - d前月末在庫数;
        //浮動小数対策
        d前月差分 = (Math.round(d前月差分 * 1000d)) / 1000d;
        txt前月差分.setText(String.valueOf(d前月差分));

    }

    public static void h小計計算() {

        //計算に使用する変数初期化
        h月搬入予定量 = 0.;
        h月搬入実績 = 0.;

        for (int i = 0; i < o1.size(); i++) {
            if (o1.get(i) != null) {
                h月搬入予定量 += (Double) ((Map) o1.get(i)).get("月搬入予定数量");
                //浮動小数の処理のため四捨五入
                h月搬入予定量 = Math.round(h月搬入予定量 * 100d) / 100d;
                h月搬入実績 += (Double) ((Map) o1.get(i)).get("月搬入実績");
                //浮動小数の処理のため四捨五入
                h月搬入実績 = Math.round(h月搬入実績 * 100d) / 100d;
            }
        }

        //ヘッダー合計行の計算
        txt月搬入予定量.setText(String.valueOf(h月搬入予定量));
        txt月搬入実績.setText(String.valueOf(h月搬入実績));

    }

    private void d小計計算() {

        //計算に使用する変数初期化
        d稼働日数 = 0;
        d搬入予定設定数量 = 0.;
        d処理予定設定数量 = 0;
        d搬入実績数量 = 0.;
        d搬入実績台数 = 0;

        for (int i = 0; i < o2.length; i++) {
            if ((Boolean) o2[i][3]) {
                d稼働日数 += 1;
            }
            d搬入予定設定数量 += Math.round(Double.valueOf((String) o2[i][4]) * 100d) / 100d;
            //浮動小数の処理のため四捨五入
            d搬入予定設定数量 = Math.round(d搬入予定設定数量 * 100d) / 100d;
            d処理予定設定数量 += (Integer) o2[i][5];
            d搬入実績数量 += Double.valueOf((String) o2[i][6]);
            //浮動小数の処理のため四捨五入
            d搬入実績数量 = Math.round(d搬入実績数量 * 100d) / 100d;
            d搬入実績台数 += (Integer) o2[i][7];
        }

        //明細合計行の計算
        txt稼働日数.setText(String.valueOf(d稼働日数));
        txt搬入予定設定数量.setText(String.valueOf(d搬入予定設定数量));
        txt処理予定設定数量.setText(String.valueOf(d処理予定設定数量));
        txt搬入実績数量.setText(String.valueOf(d搬入実績数量));
        txt搬入実績台数.setText(String.valueOf(d搬入実績台数));

    }

    private void copyColumns() {

        Map<String, Object> columns_ = new HashMap<String, Object>();
        if (o1.size() > 0) {
            columns_.putAll((Map) o1.get(0));
        }

        EntSelector selector = new EntSelector(con_);
        switch (selector.showSelector(jPanel3)) {
            case EntSelector.CLEARED:
                columns_.put("業者コード", 0);
                columns_.put("業者名", "");
                break;
            case EntSelector.SELECTED:
                Map<String, Object> item = selector.selectedRow();
                columns_.put("業者コード", item.get("id"));
                columns_.put("業者名", (String) item.get("name"));
                break;
            default:
            // No operation.
            }
        columns_.put("所属月", dft.getValue());
        columns_.put("商品コード", goods_id_);
        columns_.put("月搬入予定数量", 0.);
        columns_.put("当日までの搬入予定", 0.);
        columns_.put("月搬入実績", 0.);
        columns_.put("達成率", 0.);
        columns_.put("標準偏差", 0.);

        o1.add(o1.size(), columns_);
    }

    private void h再計算() {

        //ヘッダーの再計算（合計算出）
        for (int i = 0; i < o1.size(); i++) {
            gzd01.get(i).update(term_date);
        }
    }

    public void d再計算() {

        try {
            Statement st = con_.createStatement();
            st.executeUpdate("begin;");
            try {
                String s = new StringBuilder()
                         .append("select zaiko.update_d_result(?::date, ?::integer);")
                         .toString();

                PreparedStatement ps = con_.prepareStatement(s);
                try {
                    ps.setObject(1, term_date);
                    ps.setObject(2, goods_id_);

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

    public void d再計算02() {

        for (int i = 0; i < o2.length; i++) {
            gzd2[i].update(term_date, goods_id_);
        }

    }

    public void d再計算2() {

        try {
            Statement st = con_.createStatement();
            st.executeUpdate("begin;");
            try {
                String s = new StringBuilder()
                         .append("select zaiko.update_d_result2(?::date, ?::integer);")
                         .toString();

                PreparedStatement ps = con_.prepareStatement(s);
                try {
                    ps.setObject(1, term_date);
                    ps.setObject(2, goods_id_);

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

    public void d参照() {

        try {
            Statement st = con_.createStatement();
            st.executeUpdate("begin;");
            try {
                String s = new StringBuilder()
                         .append("select zaiko.set_d_result(?::date);")
                         .toString();

                PreparedStatement ps = con_.prepareStatement(s);
                try {
                    ps.setObject(1, term_date);
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

    private void 警報停止() {

        try {
            Statement st = GoodsZaikoDetailPane0.con_.createStatement();
            st.executeUpdate("begin;");
            try {
                String s = new StringBuilder()
                         .append("update zaiko.d_alert set 警報確認f = true ")
                         .append("where date_trunc('month'::text, 所属日::date)::date = date_trunc('month'::text, ?::date)::date ")
                         .append("and 商品コード = ?;")
                         .toString();

                PreparedStatement ps = GoodsZaikoDetailPane0.con_.prepareStatement(s);
                try {

                    ps.setObject(1, term_date);
                    ps.setObject(2, goods_id_);

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

    private void 設定値登録() {

        try {
            Statement st = GoodsZaikoDetailPane0.con_.createStatement();
            st.executeUpdate("begin;");
            try {
                String s = new StringBuilder()
                         .append("update m_goods_kind set ")
                         .append("d在庫範囲最大値 = ?, ")
                         .append("d在庫範囲最小値 = ?, ")
                         .append("d達成率最大値 = ?, ")
                         .append("d達成率最小値 = ?, ")
                         .append("f在庫警報設定 = ?, ")
                         .append("f達成率警報設定 = ? ")
                         .append("where id = ?;")
                         .toString();

                PreparedStatement ps = GoodsZaikoDetailPane0.con_.prepareStatement(s);
                try {

                    ps.setObject(1, Double.valueOf(txt在庫範囲最大値.getText()));
                    ps.setObject(2, Double.valueOf(txt在庫範囲最小値.getText()));
                    ps.setObject(3, Double.valueOf(txt達成率最大値.getText()));
                    ps.setObject(4, Double.valueOf(txt達成率最小値.getText()));
                    ps.setObject(5, f在庫警報設定_.isSelected());
                    ps.setObject(6, f達成率警報設定_.isSelected());
                    ps.setObject(7, goods_id_);

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

    public void hスクロール制御() {
        //jScrollPane3
        JScrollBar vBar = hScroll.getVerticalScrollBar();
        int vBarMax = vBar.getMaximum();
        vBar.setValue(vBarMax);
    }

    public void dスクロール制御() {
        JScrollBar vBar = dScroll.getVerticalScrollBar();
        int vBarMax = vBar.getMaximum();
        vBar.setValue(vBarMax);
    }

    private void get前月実績および前月末在庫数取得() {
        try {
            PreparedStatement ps = con_.prepareStatement(
                     String.format("select count(sno) as cnt, sum(net_weight) as net_weight "
                              + "from v1_weigh_data "
                              + "where date_trunc('month', term_date) = (date_trunc('month', '%s'::date) + '-1 month'::interval)::date "
                              + "and goods_id = %d::integer; ", term_date, goods_id_)
            );
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
//private Double d前月搬入数量, d前月平均搬入量, d前月末在庫数, d前月差分;
//    private int i前月搬入台数;
                d前月搬入数量 = rs.getDouble("net_weight") / 1000d;
                i前月搬入台数 = rs.getInt("cnt");
                d前月平均搬入量 = (Math.round((rs.getDouble("net_weight") / 1000d) / rs.getDouble("cnt") * 1000d)) / 1000d;
            } else {
                d前月搬入数量 = 0.;
                d前月平均搬入量 = 0.;
            }

            ps = con_.prepareStatement(
                     String.format("select sum(在庫実数) as 在庫実数 "
                              + "from zaiko.d_result "
                              + "where 所属日::date = (date_trunc('month', '%s'::date) + '-1 day'::interval)::date "
                              + "and 商品コード = %d::integer; ", term_date, goods_id_)
            );

            rs = ps.executeQuery();
            if (rs.next()) {
                d前月末在庫数 = rs.getDouble("在庫実数");
            } else {
                d前月末在庫数 = 0.;
            }

        } catch (SQLException e) {

        }
    }

    protected void doClose() {
        Window w = SwingUtilities.getWindowAncestor(this);
        if (w instanceof Frame) {
            w.dispatchEvent(new WindowEvent(w, WindowEvent.WINDOW_CLOSING));
        } else {
            w.dispose();
        }

        //外観スタイル
        try {

            javax.swing.UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
            SwingUtilities.updateComponentTreeUI(this);

        } catch (Exception ex) {

        }
    }
}
