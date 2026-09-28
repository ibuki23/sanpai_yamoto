package aks.industrialwaste.recycle.ui.weighlist;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import aks.swing.JTextField;
import javax.swing.JToggleButton;
import aks.industrialwaste.util.TermDate;
import aks.industrialwaste.recycle.util.*;
import aks.industrialwaste.ui.DetailsPanel2;
import aks.swing.JTextFieldHankaku;
import java.awt.*;
import jp.co.astax.awt.FontUtility;
import jp.co.astax.swing.DateTimeField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.sql.Date;
import java.text.DecimalFormat;
import javax.swing.JComboBox;

public class WeighListDetailsPane extends DetailsPanel2 {

    private WeighListTableModel model_;
    private int row_;
    private Map<String, Object> columns_;
    private JTextFieldHankaku ent_id_Field_;
    private JTextFieldHankaku trans_id_Field_;
    private JTextFieldHankaku goods_id_Field_;
    private JTextFieldHankaku office_id_Field_;
    private JTextFieldHankaku cont_id_Field_;
    private JTextFieldHankaku car_kind_id_Field_;
    private JTextField dis_entField_;
    private JTextField transField_;
    private JTextFieldHankaku car_no_Field_;
    private JTextField goodsField_;
    private JTextField dis_office_Field_;
    private JTextField office_sub_Field_;
    private JTextField dis_contField_;
    private JTextField car_kindField_;
    private JFormattedTextField gateInWeightField_;
    private DateTimeField gateInDateField_;
    private DateTimeField cont1_Field_;
    private DateTimeField cont2_Field_;
    private DateTimeField cont3_Field_;
    private JFormattedTextField gateOutWeightField_;
    private DateTimeField gateOutDateField_;
    private JTextFieldHankaku empty_weightField_;
    private static JTextFieldHankaku net_weightField_;
    private JTextFieldHankaku total_weightField_;
    private JTextFieldHankaku manifesto1_Field_;
    private JTextFieldHankaku manifesto2_Field_;
    private JTextFieldHankaku bill_id_Field_;
    private JTextField billField_;
    private JPanel pane;
    private JTextField reportNoField_;
    private JPanel ToggleField_;
    private JTextFieldHankaku unit_Field_;//料金単価
    private JTextFieldHankaku charge_Field_;//料金
    private JToggleButton jtbtn0;
    private JToggleButton jtbtn1;
    private JLabel goods_option_name;
    private JLabel unit_name;
    private JTextFieldHankaku unit_value_Field_;//参考単価
    private static JTextFieldHankaku m3_Field_; //リューベ
    private JTextFieldHankaku gravity_Field_;//比重係数
    private int mode_;
    private boolean f按分フラグ = false;
    private JPanel dpane;
    private Connection con_;
    private WeighListDetailsPane2 dWieghPane2;
    private static JLabel lblm3;
    private static final String[] 売上区分 = {"掛け", "現金"};
    private JComboBox<String> 売上区分cmb;

    public WeighListDetailsPane(WeighListTableModel model, int row, int mode) {

        super.initComponents();

        model_ = model;
        row_ = row;
        mode_ = mode;
        con_ = model_.getConnection();

        if (row != -1) {
            f按分フラグ = (Boolean) model_.getValueAt(row, "f按分フラグ");
        } else {
            f按分フラグ = false;
        }

        if (f按分フラグ) {
            WeighListDetailTableModel dmodel = new WeighListDetailTableModel(con_);
            dmodel.refresh(
                     String.format(
                              "select * from weigh_data_anbun where weight_sno = %d order by row_no;",
                              (Long) model_.getValueAt(row, "sno")));

            dWieghPane2 = new WeighListDetailsPane2(con_, dmodel,
                     (Long) model_.getValueAt(row, "sno"),
                     (Integer) model_.getValueAt(row, "net_weight"));
            addContainer(
                     dpane, dWieghPane2,
                     1, 0, 1, 1, 0.5, 0.0,
                     GridBagConstraints.EAST, GridBagConstraints.BOTH,
                     new Insets(2, 2, 2, 2), 0, 0);
        }

        dpane.setVisible(f按分フラグ);

        columns_ = new HashMap<>();
        copyColumns();
    }

    @Override
    protected void initComponents() {
        super.initComponents();
        row_ = -1;

        Font font18 = FontUtility.getFont("メイリオ", Font.PLAIN, 14);
        titleField_.setText("計量記録データの変更");

        // 詳細 - 左側(上)
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createLoweredBevelBorder());
        panel.setMinimumSize(new Dimension(680, 850));
        panel.setPreferredSize(new Dimension(680, 850));
        // 区分
        JLabel label = new JLabel("区分", JLabel.LEFT);
        label.setFont(font18);
        ToggleField_ = new JPanel(new GridBagLayout());
        ToggleField_.setMinimumSize(new Dimension(150, 35));
        ToggleField_.setPreferredSize(new Dimension(150, 35));

        jtbtn0 = new JToggleButton("産廃(管理)");
        jtbtn0.setFont(font18);
        jtbtn0.setMargin(new Insets(2, 2, 2, 2));
        jtbtn0.setMinimumSize(new Dimension(70, 30));
        jtbtn0.setPreferredSize(new Dimension(70, 30));
        jtbtn0.addActionListener(new java.awt.event.ActionListener() {

            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jtbtn0MousePressed(evt);
            }
        });
        addContainer(
                 ToggleField_, jtbtn0,
                 0, 0, 1, 1, 0.5, 0.0,
                 GridBagConstraints.WEST, GridBagConstraints.BOTH,
                 new Insets(2, 2, 2, 2), 0, 0);

        jtbtn1 = new JToggleButton("産廃(自社)");
        jtbtn1.setFont(font18);
        jtbtn1.setMargin(new Insets(2, 2, 2, 2));
        jtbtn1.setMinimumSize(new Dimension(70, 30));
        jtbtn1.setPreferredSize(new Dimension(70, 30));
        jtbtn1.addActionListener(new java.awt.event.ActionListener() {

            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jtbtn1MousePressed(evt);
            }
        });
        addContainer(
                 ToggleField_, jtbtn1,
                 1, 0, 1, 1, 0.5, 0.0,
                 GridBagConstraints.WEST, GridBagConstraints.BOTH,
                 new Insets(2, 2, 2, 2), 0, 0);

        // 入構日時
        label = new JLabel("入構日時", JLabel.LEFT);
        label.setFont(font18);
        addContainer(
                 panel, label,
                 0, 1, 1, 1, 0.1, 0.0,
                 GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
                 new Insets(2, 2, 2, 2), 0, 0);
        gateInDateField_ = new DateTimeField(
                 DateTimeField.DateFormat.YMD,
                 DateTimeField.TimeFormat.HM);
        gateInDateField_.setFont(font18);
        gateInDateField_.setMinimumSize(new Dimension(150, 30));
        gateInDateField_.setPreferredSize(new Dimension(150, 30));
        gateInDateField_.addFocusListener(new ColumnUpdater());
        addContainer(
                 panel, gateInDateField_,
                 1, 1, 4, 1, 0.0, 0.0,
                 GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
                 new Insets(2, 2, 2, 2), 0, 0);
        // 出構日時
        label = new JLabel("出構日時", JLabel.LEFT);
        label.setFont(font18);
        addContainer(
                 panel, label,
                 0, 2, 1, 1, 0.0, 0.0,
                 GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
                 new Insets(2, 2, 2, 2), 0, 0);
        gateOutDateField_ = new DateTimeField(
                 DateTimeField.DateFormat.YMD,
                 DateTimeField.TimeFormat.HM);
        gateOutDateField_.setFont(font18);
        gateOutDateField_.setMinimumSize(new Dimension(150, 30));
        gateOutDateField_.setPreferredSize(new Dimension(150, 30));
        gateOutDateField_.addFocusListener(new ColumnUpdater());
        addContainer(
                 panel, gateOutDateField_,
                 1, 2, 4, 1, 0.0, 0.0,
                 GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
                 new Insets(2, 2, 2, 2), 0, 0);
        // 計量回数
        label = new JLabel("計量NO", JLabel.LEFT);
        label.setFont(font18);
        addContainer(
                 panel, label,
                 0, 3, 1, 1, 0.1, 0.0,
                 GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
                 new Insets(2, 2, 2, 2), 0, 0);
        reportNoField_ = new JTextField();
        reportNoField_.setFont(font18);
        reportNoField_.setFocusable(false);
        reportNoField_.setEditable(false);
        reportNoField_.setOpaque(false);
        reportNoField_.setHorizontalAlignment(JFormattedTextField.RIGHT);
        reportNoField_.setMinimumSize(new Dimension(70, 30));
        reportNoField_.setPreferredSize(new Dimension(70, 30));
        addContainer(
                 panel, reportNoField_,
                 1, 3, 1, 1, 0.0, 0.0,
                 GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
                 new Insets(2, 2, 2, 2), 0, 0);

        // 車番
        label = new JLabel("車番", JLabel.LEFT);
        label.setFont(font18);
        addContainer(
                 panel, label,
                 0, 4, 1, 1, 0.1, 0.0,
                 GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
                 new Insets(2, 2, 2, 2), 0, 0);
        car_no_Field_ = new JTextFieldHankaku();
        car_no_Field_.setFont(font18);
        car_no_Field_.setMinimumSize(new Dimension(70, 30));
        car_no_Field_.setPreferredSize(new Dimension(70, 30));
        car_no_Field_.addFocusListener(new ColumnUpdater());
        car_no_Field_.setHorizontalAlignment(JFormattedTextField.RIGHT);
        car_no_Field_.addKeyListener(new java.awt.event.KeyAdapter() {

            @Override
            //頭にカーソル表示
            public void keyPressed(java.awt.event.KeyEvent evt) {
                JTextField_KeyPressed(evt, car_no_Field_);
            }
        });
        //最初のフォーカスを設定
        EventQueue.invokeLater(new Runnable() {

            @Override
            public void run() {
                //初期カーソル
//                car_no_Field_.requestFocusInWindow();
                manifesto2_Field_.requestFocusInWindow();
            }
        });
        addContainer(
                 panel, car_no_Field_,
                 1, 4, 1, 1, 0.0, 0.0,
                 GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
                 new Insets(2, 2, 2, 2), 0, 0);
        JButton btn = new JButton("選択");
        btn.setFont(font18);
        btn.setFocusable(false);
        btn.setMargin(new Insets(2, 2, 2, 2));
        btn.setMinimumSize(new Dimension(70, 30));
        btn.setPreferredSize(new Dimension(70, 30));
        btn.addActionListener(new SelectCarAction());
        addContainer(
                 panel, btn,
                 4, 4, 1, 1, 0.0, 0.0,
                 GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
                 new Insets(2, 2, 2, 2), 0, 0);

        // 請求先
        label = new JLabel("請求先", JLabel.LEFT);
        label.setFont(font18);
        addContainer(
                 panel, label,
                 0, 5, 1, 1, 0.1, 0.0,
                 GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
                 new Insets(2, 2, 2, 2), 0, 0);

        bill_id_Field_ = new JTextFieldHankaku();
        bill_id_Field_.setFont(font18);
        bill_id_Field_.setMinimumSize(new Dimension(70, 30));
        bill_id_Field_.setPreferredSize(new Dimension(70, 30));
        bill_id_Field_.addFocusListener(new ColumnUpdater());
        bill_id_Field_.setHorizontalAlignment(JFormattedTextField.RIGHT);
        bill_id_Field_.addKeyListener(new java.awt.event.KeyAdapter() {

            @Override
            public void keyPressed(java.awt.event.KeyEvent evt) {
                JTextField_KeyPressed(evt, bill_id_Field_);
            }
        });
        addContainer(
                 panel, bill_id_Field_,
                 1, 5, 1, 1, 0.0, 0.0,
                 GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
                 new Insets(2, 2, 2, 2), 0, 0);

        billField_ = new JTextField();
        billField_.setFont(font18);
        billField_.setFocusable(false);
        billField_.setEditable(false);
        billField_.setMinimumSize(new Dimension(150, 30));
        billField_.setPreferredSize(new Dimension(150, 30));
        addContainer(
                 panel, billField_,
                 2, 5, 2, 1, 0.9, 0.0,
                 GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
                 new Insets(2, 2, 2, 2), 0, 0);
        btn = new JButton("選択");
        btn.setFont(font18);
        btn.setFocusable(false);
        btn.setMargin(new Insets(2, 2, 2, 2));
        btn.setMinimumSize(new Dimension(70, 30));
        btn.setPreferredSize(new Dimension(70, 30));
        btn.addActionListener(new SelectBillAction());
        addContainer(
                 panel, btn,
                 4, 5, 1, 1, 0.0, 0.0,
                 GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
                 new Insets(2, 2, 2, 2), 0, 0);

        // 排出事業者
        label = new JLabel("排出事業者", JLabel.LEFT);
        label.setFont(font18);
        addContainer(
                 panel, label,
                 0, 6, 1, 1, 0.1, 0.0,
                 GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
                 new Insets(2, 2, 2, 2), 0, 0);

        ent_id_Field_ = new JTextFieldHankaku();
        ent_id_Field_.setFont(font18);
        ent_id_Field_.setMinimumSize(new Dimension(70, 30));
        ent_id_Field_.setPreferredSize(new Dimension(70, 30));
        ent_id_Field_.addFocusListener(new ColumnUpdater());
        ent_id_Field_.setHorizontalAlignment(JFormattedTextField.RIGHT);
        ent_id_Field_.addKeyListener(new java.awt.event.KeyAdapter() {

            @Override
            public void keyPressed(java.awt.event.KeyEvent evt) {
                JTextField_KeyPressed(evt, ent_id_Field_);
            }
        });
        addContainer(
                 panel, ent_id_Field_,
                 1, 6, 1, 1, 0.0, 0.0,
                 GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
                 new Insets(2, 2, 2, 2), 0, 0);

        dis_entField_ = new JTextField();
        dis_entField_.setFont(font18);
        dis_entField_.setFocusable(false);
        dis_entField_.setEditable(false);
        dis_entField_.setMinimumSize(new Dimension(150, 30));
        dis_entField_.setPreferredSize(new Dimension(150, 30));
        addContainer(
                 panel, dis_entField_,
                 2, 6, 2, 1, 0.9, 0.0,
                 GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
                 new Insets(2, 2, 2, 2), 0, 0);
        btn = new JButton("選択");
        btn.setFont(font18);
        btn.setFocusable(false);
        btn.setMargin(new Insets(2, 2, 2, 2));
        btn.setMinimumSize(new Dimension(70, 30));
        btn.setPreferredSize(new Dimension(70, 30));
        btn.addActionListener(new SelectEntAction());
        addContainer(
                 panel, btn,
                 4, 6, 1, 1, 0.0, 0.0,
                 GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
                 new Insets(2, 2, 2, 2), 0, 0);

        // 　種類
        label = new JLabel("品目", JLabel.LEFT);
        label.setFont(font18);
        addContainer(
                 panel, label,
                 0, 7, 1, 1, 0.1, 0.0,
                 GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
                 new Insets(2, 2, 2, 2), 0, 0);
        goods_id_Field_ = new JTextFieldHankaku();
        goods_id_Field_.setFont(font18);
        goods_id_Field_.setMinimumSize(new Dimension(70, 30));
        goods_id_Field_.setPreferredSize(new Dimension(70, 30));
        goods_id_Field_.addFocusListener(new ColumnUpdater());
        goods_id_Field_.setHorizontalAlignment(JFormattedTextField.RIGHT);
        goods_id_Field_.addKeyListener(new java.awt.event.KeyAdapter() {

            @Override
            public void keyPressed(java.awt.event.KeyEvent evt) {
                JTextField_KeyPressed(evt, goods_id_Field_);

            }
        });
        addContainer(
                 panel, goods_id_Field_,
                 1, 7, 1, 1, 0.0, 0.0,
                 GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
                 new Insets(2, 2, 2, 2), 0, 0);
        goodsField_ = new JTextField();
        goodsField_.setFont(font18);
        goodsField_.setFocusable(false);
        goodsField_.setEditable(false);
        goodsField_.setMinimumSize(new Dimension(170, 30));
        goodsField_.setPreferredSize(new Dimension(170, 30));
        addContainer(
                 panel, goodsField_,
                 2, 7, 1, 1, 0.8, 0.0,
                 GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
                 new Insets(2, 2, 2, 2), 0, 0);
        goods_option_name = new JLabel("　　　搬入", JLabel.LEFT);
        goods_option_name.setFont(font18);
        addContainer(
                 panel, goods_option_name,
                 3, 7, 1, 1, 0.1, 0.0,
                 GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
                 new Insets(2, 2, 2, 2), 0, 0);
        btn = new JButton("選択");
        btn.setFont(font18);
        btn.setFocusable(false);
        btn.setMargin(new Insets(2, 2, 2, 2));
        btn.setMinimumSize(new Dimension(70, 30));
        btn.setPreferredSize(new Dimension(70, 30));
        btn.addActionListener(new SelectGoodsAction());
        addContainer(
                 panel, btn,
                 4, 7, 1, 1, 0.0, 0.0,
                 GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
                 new Insets(2, 2, 2, 2), 0, 0);

        // 排出事業場
        label = new JLabel("排出事業場", JLabel.LEFT);
        label.setFont(font18);
        addContainer(
                 panel, label,
                 0, 8, 1, 1, 0.1, 0.0,
                 GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
                 new Insets(2, 2, 2, 2), 0, 0);

        office_id_Field_ = new JTextFieldHankaku();
        office_id_Field_.setFont(font18);
        office_id_Field_.setMinimumSize(new Dimension(70, 30));
        office_id_Field_.setPreferredSize(new Dimension(70, 30));
        office_id_Field_.addFocusListener(new ColumnUpdater());
        office_id_Field_.setHorizontalAlignment(JFormattedTextField.RIGHT);
        office_id_Field_.addKeyListener(new java.awt.event.KeyAdapter() {

            @Override
            public void keyPressed(java.awt.event.KeyEvent evt) {
                JTextField_KeyPressed(evt, office_id_Field_);
            }
        });
        addContainer(
                 panel, office_id_Field_,
                 1, 8, 1, 1, 0.0, 0.0,
                 GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
                 new Insets(2, 2, 2, 2), 0, 0);
        dis_office_Field_ = new JTextField();
        dis_office_Field_.setFont(font18);
        dis_office_Field_.setFocusable(false);
        dis_office_Field_.setEditable(false);
        dis_office_Field_.setMinimumSize(new Dimension(250, 30));
        dis_office_Field_.setPreferredSize(new Dimension(250, 30));
        addContainer(
                 panel, dis_office_Field_,
                 2, 8, 1, 1, 0.0, 0.0,
                 GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
                 new Insets(2, 2, 2, 2), 0, 0);
        office_sub_Field_ = new JTextField();
        office_sub_Field_.setFont(font18);
        office_sub_Field_.addFocusListener(new ColumnUpdater());
        office_sub_Field_.setMinimumSize(new Dimension(200, 30));
        office_sub_Field_.setPreferredSize(new Dimension(200, 30));
        office_sub_Field_.addKeyListener(new java.awt.event.KeyAdapter() {
            @Override
            public void keyPressed(java.awt.event.KeyEvent evt) {
                JTextField_KeyPressed(evt, office_sub_Field_);
            }
        });
        addContainer(
                 panel, office_sub_Field_,
                 3, 8, 1, 1, 0.0, 0.0,
                 GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
                 new Insets(2, 2, 2, 2), 0, 0);

        btn = new JButton("選択");
        btn.setFont(font18);
        btn.setFocusable(false);
        btn.setMargin(new Insets(2, 2, 2, 2));
        btn.setMinimumSize(new Dimension(70, 30));
        btn.setPreferredSize(new Dimension(70, 30));
        btn.addActionListener(new SelectOfficeAction());
        addContainer(
                 panel, btn,
                 4, 8, 1, 1, 0.0, 0.0,
                 GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
                 new Insets(2, 2, 2, 2), 0, 0);

        // 運搬業者
        label = new JLabel("運搬業者", JLabel.LEFT);
        label.setFont(font18);
        addContainer(
                 panel, label,
                 0, 9, 1, 1, 0.1, 0.0,
                 GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
                 new Insets(2, 2, 2, 2), 0, 0);
        trans_id_Field_ = new JTextFieldHankaku();
        trans_id_Field_.setFont(font18);
        trans_id_Field_.setMinimumSize(new Dimension(70, 30));
        trans_id_Field_.setPreferredSize(new Dimension(70, 30));
        trans_id_Field_.addFocusListener(new ColumnUpdater());
        trans_id_Field_.setHorizontalAlignment(JFormattedTextField.RIGHT);
        trans_id_Field_.addKeyListener(new java.awt.event.KeyAdapter() {

            @Override
            public void keyPressed(java.awt.event.KeyEvent evt) {
                JTextField_KeyPressed(evt, trans_id_Field_);
            }
        });
        addContainer(
                 panel, trans_id_Field_,
                 1, 9, 1, 1, 0.0, 0.0,
                 GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
                 new Insets(2, 2, 2, 2), 0, 0);
        transField_ = new JTextField();
        transField_.setFont(font18);
        transField_.setFocusable(false);
        transField_.setEditable(false);
        transField_.setMinimumSize(new Dimension(150, 30));
        transField_.setPreferredSize(new Dimension(150, 30));
        addContainer(
                 panel, transField_,
                 2, 9, 2, 1, 0.9, 0.0,
                 GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
                 new Insets(2, 2, 2, 2), 0, 0);
        btn = new JButton("選択");
        btn.setFont(font18);
        btn.setFocusable(false);
        btn.setMargin(new Insets(2, 2, 2, 2));
        btn.setMinimumSize(new Dimension(70, 30));
        btn.setPreferredSize(new Dimension(70, 30));
        btn.addActionListener(new SelectTransporterAction());
        addContainer(
                 panel, btn,
                 4, 9, 1, 1, 0.0, 0.0,
                 GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
                 new Insets(2, 2, 2, 2), 0, 0);
        // 処分業者
        label = new JLabel("処分業者", JLabel.LEFT);
        label.setFont(font18);
        addContainer(
                 panel, label,
                 0, 10, 1, 1, 0.1, 0.0,
                 GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
                 new Insets(2, 2, 2, 2), 0, 0);

        cont_id_Field_ = new JTextFieldHankaku();
        cont_id_Field_.setFont(font18);
        cont_id_Field_.setMinimumSize(new Dimension(70, 30));
        cont_id_Field_.setPreferredSize(new Dimension(70, 30));
        cont_id_Field_.addFocusListener(new ColumnUpdater());
        cont_id_Field_.setHorizontalAlignment(JFormattedTextField.RIGHT);
        cont_id_Field_.addKeyListener(new java.awt.event.KeyAdapter() {

            @Override
            public void keyPressed(java.awt.event.KeyEvent evt) {
                JTextField_KeyPressed(evt, cont_id_Field_);
            }
        });
        addContainer(
                 panel, cont_id_Field_,
                 1, 10, 1, 1, 0.0, 0.0,
                 GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
                 new Insets(2, 2, 2, 2), 0, 0);
        dis_contField_ = new JTextField();
        dis_contField_.setFont(font18);
        dis_contField_.setFocusable(false);
        dis_contField_.setEditable(false);
        dis_contField_.setMinimumSize(new Dimension(150, 30));
        dis_contField_.setPreferredSize(new Dimension(150, 30));
        addContainer(
                 panel, dis_contField_,
                 2, 10, 2, 1, 0.9, 0.0,
                 GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
                 new Insets(2, 2, 2, 2), 0, 0);
        btn = new JButton("選択");
        btn.setFont(font18);
        btn.setFocusable(false);
        btn.setMargin(new Insets(2, 2, 2, 2));
        btn.setMinimumSize(new Dimension(70, 30));
        btn.setPreferredSize(new Dimension(70, 30));
        btn.addActionListener(new SelectContAction());
        addContainer(
                 panel, btn,
                 4, 10, 1, 1, 0.0, 0.0,
                 GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
                 new Insets(2, 2, 2, 2), 0, 0);

        // 車種
        label = new JLabel("車種", JLabel.LEFT);
        label.setFont(font18);
        addContainer(
                 panel, label,
                 0, 11, 1, 1, 0.1, 0.0,
                 GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
                 new Insets(2, 2, 2, 2), 0, 0);
        //  private JTextField car_kind_id_Field_;
        car_kind_id_Field_ = new JTextFieldHankaku();
        car_kind_id_Field_.setFont(font18);
        car_kind_id_Field_.setMinimumSize(new Dimension(70, 30));
        car_kind_id_Field_.setPreferredSize(new Dimension(70, 30));
        car_kind_id_Field_.addFocusListener(new ColumnUpdater());
        car_kind_id_Field_.setHorizontalAlignment(JFormattedTextField.RIGHT);
        car_kind_id_Field_.addKeyListener(new java.awt.event.KeyAdapter() {

            @Override
            public void keyPressed(java.awt.event.KeyEvent evt) {
                JTextField_KeyPressed(evt, car_kind_id_Field_);
            }
        });
        addContainer(
                 panel, car_kind_id_Field_,
                 1, 11, 1, 1, 0.0, 0.0,
                 GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
                 new Insets(2, 2, 2, 2), 0, 0);
        car_kindField_ = new JTextField();
        car_kindField_.setFont(font18);
        car_kindField_.setFocusable(false);
        car_kindField_.setEditable(false);
        car_kindField_.setMinimumSize(new Dimension(150, 30));
        car_kindField_.setPreferredSize(new Dimension(150, 30));
        addContainer(
                 panel, car_kindField_,
                 2, 11, 2, 1, 0.9, 0.0,
                 GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
                 new Insets(2, 2, 2, 2), 0, 0);
        btn = new JButton("選択");
        btn.setFont(font18);
        btn.setFocusable(false);
        btn.setMargin(new Insets(2, 2, 2, 2));
        btn.setMinimumSize(new Dimension(70, 30));
        btn.setPreferredSize(new Dimension(70, 30));
        btn.addActionListener(new SelectCarKindAction());
        addContainer(
                 panel, btn,
                 4, 11, 1, 1, 0.0, 0.0,
                 GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
                 new Insets(2, 2, 2, 2), 0, 0);
        // 総重量
        label = new JLabel("総重量(kg)", JLabel.LEFT);
        label.setFont(font18);
        addContainer(
                 panel, label,
                 0, 12, 2, 1, 0.0, 0.0,
                 GridBagConstraints.WEST, GridBagConstraints.NONE,
                 new Insets(2, 2, 2, 2), 0, 0);

        total_weightField_ = new JTextFieldHankaku();
        total_weightField_.setFont(font18);
        total_weightField_.setHorizontalAlignment(JFormattedTextField.RIGHT);
        total_weightField_.setMinimumSize(new Dimension(120, 30));
        total_weightField_.setPreferredSize(new Dimension(120, 30));
        total_weightField_.addFocusListener(new ColumnUpdater());
        total_weightField_.addKeyListener(new java.awt.event.KeyAdapter() {

            @Override
            public void keyPressed(java.awt.event.KeyEvent evt) {
                JTextField_KeyPressed(evt, total_weightField_);
            }
        });
        addContainer(
                 panel, total_weightField_,
                 1, 12, 2, 1, 0.0, 0.0,
                 GridBagConstraints.WEST, GridBagConstraints.NONE,
                 new Insets(2, 2, 2, 2), 0, 0);

        // 風袋重量
        label = new JLabel("風袋重量(kg)", JLabel.LEFT);
        label.setFont(font18);
        addContainer(
                 panel, label,
                 0, 13, 1, 1, 0.0, 0.0,
                 GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
                 new Insets(2, 2, 2, 2), 0, 0);

        empty_weightField_ = new JTextFieldHankaku("");
        empty_weightField_.setFont(font18);
        empty_weightField_.setHorizontalAlignment(JFormattedTextField.RIGHT);
        empty_weightField_.setMinimumSize(new Dimension(120, 30));
        empty_weightField_.setPreferredSize(new Dimension(120, 30));
        empty_weightField_.addFocusListener(new ColumnUpdater());
        empty_weightField_.addKeyListener(new java.awt.event.KeyAdapter() {

            @Override
            public void keyPressed(java.awt.event.KeyEvent evt) {
                JTextField_KeyPressed(evt, empty_weightField_);
            }
        });
        addContainer(
                 panel, empty_weightField_,
                 1, 13, 2, 1, 0.0, 0.0,
                 GridBagConstraints.WEST, GridBagConstraints.NONE,
                 new Insets(2, 2, 2, 2), 0, 0);

        // 正味重量
        label = new JLabel("正味重量(kg)", JLabel.LEFT);
        label.setFont(font18);
        addContainer(
                 panel, label,
                 0, 14, 1, 1, 0.0, 0.0,
                 GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
                 new Insets(2, 2, 2, 2), 0, 0);
        net_weightField_ = new JTextFieldHankaku("");
        net_weightField_.setFont(font18);
        net_weightField_.setFocusable(false);
        net_weightField_.setEditable(false);
        net_weightField_.setHorizontalAlignment(JFormattedTextField.RIGHT);
        net_weightField_.setMinimumSize(new Dimension(120, 30));
        net_weightField_.setPreferredSize(new Dimension(120, 30));
        net_weightField_.addFocusListener(new ColumnUpdater());
        net_weightField_.addKeyListener(new java.awt.event.KeyAdapter() {

            @Override
            public void keyPressed(java.awt.event.KeyEvent evt) {
                JTextField_KeyPressed(evt, net_weightField_);
            }

        });
        addContainer(
                 panel, net_weightField_,
                 1, 14, 2, 1, 0.0, 0.0,
                 GridBagConstraints.WEST, GridBagConstraints.NONE,
                 new Insets(2, 2, 2, 2), 0, 0);

        // 比重
        label = new JLabel("比重(係数)", JLabel.LEFT);
        label.setFont(font18);
        addContainer(
                 panel, label,
                 0, 15, 1, 1, 0.0, 0.0,
                 GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
                 new Insets(2, 2, 2, 2), 0, 0);
        gravity_Field_ = new JTextFieldHankaku("");
        gravity_Field_.setFont(font18);
        gravity_Field_.setHorizontalAlignment(JFormattedTextField.RIGHT);
        gravity_Field_.setMinimumSize(new Dimension(120, 30));
        gravity_Field_.setPreferredSize(new Dimension(120, 30));
        gravity_Field_.addFocusListener(new ColumnUpdater());
        gravity_Field_.addKeyListener(new java.awt.event.KeyAdapter() {

            @Override
            public void keyPressed(java.awt.event.KeyEvent evt) {
                JTextField_KeyPressed(evt, gravity_Field_);
            }
        });
        addContainer(
                 panel, gravity_Field_,
                 1, 15, 2, 1, 0.0, 0.0,
                 GridBagConstraints.WEST, GridBagConstraints.NONE,
                 new Insets(2, 2, 2, 2), 0, 0);

        lblm3 = new JLabel("数量(m3)", JLabel.LEFT);
        lblm3.setFont(font18);
        addContainer(
                 panel, lblm3,
                 0, 16, 1, 1, 0.0, 0.0,
                 GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
                 new Insets(2, 2, 2, 2), 0, 0);
        m3_Field_ = new JTextFieldHankaku("");
        m3_Field_.setFont(font18);
        m3_Field_.setHorizontalAlignment(JFormattedTextField.RIGHT);
        m3_Field_.setMinimumSize(new Dimension(120, 30));
        m3_Field_.setPreferredSize(new Dimension(120, 30));
        m3_Field_.addFocusListener(new ColumnUpdater());
        m3_Field_.addKeyListener(new java.awt.event.KeyAdapter() {

            @Override
            public void keyPressed(java.awt.event.KeyEvent evt) {
                JTextField_KeyPressed(evt, m3_Field_);
            }
        });
        addContainer(
                 panel, m3_Field_,
                 1, 16, 2, 1, 0.0, 0.0,
                 GridBagConstraints.WEST, GridBagConstraints.NONE,
                 new Insets(2, 2, 2, 2), 0, 0);

        // 料金単価
        label = new JLabel("料金単価(￥)", JLabel.LEFT);
        label.setFont(font18);
        addContainer(
                 panel, label,
                 0, 17, 1, 1, 0.1, 0.0,
                 GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
                 new Insets(2, 2, 2, 2), 0, 0);
        unit_Field_ = new JTextFieldHankaku("");
        unit_Field_.setFont(font18);
        unit_Field_.setHorizontalAlignment(JFormattedTextField.RIGHT);
        unit_Field_.setMinimumSize(new Dimension(120, 30));
        unit_Field_.setPreferredSize(new Dimension(120, 30));
        unit_Field_.addFocusListener(new ColumnUpdater());
        unit_Field_.addKeyListener(new java.awt.event.KeyAdapter() {

            @Override
            public void keyPressed(java.awt.event.KeyEvent evt) {
                JTextField_KeyPressed(evt, unit_Field_);
            }
        });
        addContainer(
                 panel, unit_Field_,
                 1, 17, 2, 1, 0.0, 0.0,
                 GridBagConstraints.WEST, GridBagConstraints.NONE,
                 new Insets(2, 2, 2, 2), 0, 0);

        // 参考単価
        label = new JLabel("参考単価", JLabel.LEFT);
        label.setFont(font18);
        addContainer(
                 panel, label,
                 0, 18, 1, 1, 0.1, 0.0,
                 GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
                 new Insets(2, 2, 2, 2), 0, 0);
        unit_value_Field_ = new JTextFieldHankaku();
        unit_value_Field_.setFont(font18);
        unit_value_Field_.setFocusable(false);
        unit_value_Field_.setEditable(false);
        unit_value_Field_.setOpaque(false);
        unit_value_Field_.setBackground(new Color(204, 255, 255));//触れないため色をつける
        unit_value_Field_.setHorizontalAlignment(JFormattedTextField.RIGHT);
        unit_value_Field_.setMinimumSize(new Dimension(120, 30));
        unit_value_Field_.setPreferredSize(new Dimension(120, 30));
        addContainer(
                 panel, unit_value_Field_,
                 1, 18, 2, 1, 0.0, 0.0,
                 GridBagConstraints.WEST, GridBagConstraints.NONE,
                 new Insets(2, 2, 2, 2), 0, 0);
        // 単位
        unit_name = new JLabel("単位", JLabel.LEFT);
        unit_name.setFont(font18);
        addContainer(
                 panel, unit_name,
                 2, 18, 1, 1, 0.1, 0.0,
                 GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
                 new Insets(2, 2, 2, 2), 0, 0);

        // 料金
        label = new JLabel("料金", JLabel.LEFT);
        label.setFont(font18);
        addContainer(
                 panel, label,
                 0, 19, 1, 1, 0.1, 0.0,
                 GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
                 new Insets(2, 2, 2, 2), 0, 0);
        charge_Field_ = new JTextFieldHankaku("");
        charge_Field_.setFont(font18);
        charge_Field_.setHorizontalAlignment(JFormattedTextField.RIGHT);
        charge_Field_.setMinimumSize(new Dimension(120, 30));
        charge_Field_.setPreferredSize(new Dimension(120, 30));
        charge_Field_.addFocusListener(new ColumnUpdater());
        charge_Field_.addKeyListener(new java.awt.event.KeyAdapter() {

            @Override
            public void keyPressed(java.awt.event.KeyEvent evt) {
                JTextField_KeyPressed(evt, charge_Field_);
            }
        });
        addContainer(
                 panel, charge_Field_,
                 1, 19, 2, 1, 0.0, 0.0,
                 GridBagConstraints.WEST, GridBagConstraints.NONE,
                 new Insets(2, 2, 2, 2), 0, 0);

        // 建廃マニフェスト
        label = new JLabel("建廃交付番号", JLabel.LEFT);
        label.setFont(font18);
        addContainer(
                 panel, label,
                 0, 20, 1, 1, 0.1, 0.0,
                 GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
                 new Insets(2, 2, 2, 2), 0, 0);
        manifesto2_Field_ = new JTextFieldHankaku();
        manifesto2_Field_.setFont(font18);
        manifesto2_Field_.setMinimumSize(new Dimension(120, 30));
        manifesto2_Field_.setPreferredSize(new Dimension(120, 30));
        manifesto2_Field_.addFocusListener(new ColumnUpdater());
        manifesto2_Field_.addKeyListener(new java.awt.event.KeyAdapter() {

            @Override
            public void keyPressed(java.awt.event.KeyEvent evt) {
                JTextField_KeyPressed(evt, manifesto2_Field_);
            }
        });
        addContainer(
                 panel, manifesto2_Field_,
                 1, 20, 3, 1, 0.0, 0.0,
                 GridBagConstraints.WEST, GridBagConstraints.NONE,
                 new Insets(2, 2, 2, 2), 0, 0);

        // 産廃マニフェスト1
        label = new JLabel("産廃交付番号", JLabel.LEFT);
        label.setFont(font18);
        addContainer(
                 panel, label,
                 0, 21, 2, 1, 0.1, 0.0,
                 GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
                 new Insets(2, 2, 2, 2), 0, 0);
        manifesto1_Field_ = new JTextFieldHankaku();
        manifesto1_Field_.setFont(font18);
        manifesto1_Field_.setMinimumSize(new Dimension(120, 30));
        manifesto1_Field_.setPreferredSize(new Dimension(120, 30));
        manifesto1_Field_.addFocusListener(new ColumnUpdater());
        manifesto1_Field_.addKeyListener(new java.awt.event.KeyAdapter() {

            @Override
            public void keyPressed(java.awt.event.KeyEvent evt) {
                JTextField_KeyPressed(evt, manifesto1_Field_);
            }
        });
        addContainer(
                 panel, manifesto1_Field_,
                 1, 21, 2, 1, 0.0, 0.0,
                 GridBagConstraints.WEST, GridBagConstraints.NONE,
                 new Insets(2, 2, 2, 2), 0, 0);

        // 売上区分
        label = new JLabel("売上区分", JLabel.LEFT);
        label.setFont(font18);
        addContainer(
                 panel, label,
                 0, 22, 1, 1, 0.1, 0.0,
                 GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
                 new Insets(2, 2, 2, 2), 0, 0);
        売上区分cmb = new JComboBox(売上区分);
        売上区分cmb.setFont(font18);
        売上区分cmb.setMinimumSize(new Dimension(120, 30));
        売上区分cmb.setPreferredSize(new Dimension(120, 30));
        売上区分cmb.addFocusListener(new ColumnUpdater());

        addContainer(
                 panel, 売上区分cmb,
                 1, 22, 1, 1, 0.0, 0.0,
                 GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
                 new Insets(2, 2, 2, 2), 0, 0);

        // 建廃処分日
        label = new JLabel("処分日", JLabel.LEFT);
        label.setFont(font18);
        addContainer(
                 panel, label,
                 0, 23, 1, 1, 0.1, 0.0,
                 GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
                 new Insets(2, 2, 2, 2), 0, 0);
        cont1_Field_ = new DateTimeField(
                 DateTimeField.DateFormat.YMD);
        cont1_Field_.setFont(font18);
        cont1_Field_.setMinimumSize(new Dimension(250, 30));
        cont1_Field_.setPreferredSize(new Dimension(250, 30));
        cont1_Field_.addKeyListener(new java.awt.event.KeyAdapter() {

            @Override
            public void keyPressed(java.awt.event.KeyEvent evt) {
                DateTimeField_KeyPressed(evt, cont1_Field_);
            }
        });
        addContainer(
                 panel, cont1_Field_,
                 1, 23, 3, 1, 0.0, 0.0,
                 GridBagConstraints.WEST, GridBagConstraints.NONE,
                 new Insets(2, 2, 2, 2), 0, 0);

        btn = new JButton("処分日設定");
        btn.setFont(font18);
        btn.setFocusable(false);
        btn.setMargin(new Insets(2, 2, 2, 2));
        btn.setMinimumSize(new Dimension(120, 30));
        btn.setPreferredSize(new Dimension(120, 30));
        btn.addActionListener(new Set処分日Action());
        addContainer(
                 panel, btn,
                 3, 24, 1, 1, 0.0, 0.0,
                 GridBagConstraints.WEST, GridBagConstraints.NONE,
                 new Insets(2, 2, 2, 2), 0, 0);

        btn = new JButton("処分日消去");
        btn.setFont(font18);
        btn.setFocusable(false);
        btn.setMargin(new Insets(2, 2, 2, 2));
        btn.setMinimumSize(new Dimension(120, 30));
        btn.setPreferredSize(new Dimension(120, 30));
        btn.addActionListener(new Clear処分日Action());
        addContainer(
                 panel, btn,
                 3, 24, 2, 1, 0.0, 0.0,
                 GridBagConstraints.EAST, GridBagConstraints.NONE,
                 new Insets(2, 2, 2, 20), 0, 0);

        label = new JLabel("※総重量からカーソルキーの移動で計算されます。", JLabel.LEFT);
        label.setFont(font18);
        label.setForeground(Color.RED);
        addContainer(
                 panel, label,
                 0, 25, 4, 1, 0.1, 0.0,
                 GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
                 new Insets(2, 2, 2, 2), 0, 0);

        // 産廃処分日1
        label = new JLabel("産廃処分日1", JLabel.LEFT);
        label.setFont(font18);
        cont2_Field_ = new DateTimeField(
                 DateTimeField.DateFormat.YMD);
        cont2_Field_.setFont(font18);
        cont2_Field_.setFocusable(false);
        cont2_Field_.setMinimumSize(new Dimension(150, 30));
        cont2_Field_.setPreferredSize(new Dimension(150, 30));
        cont2_Field_.addKeyListener(new java.awt.event.KeyAdapter() {

            @Override
            public void keyPressed(java.awt.event.KeyEvent evt) {
                DateTimeField_KeyPressed(evt, cont2_Field_);
            }
        });
        // 産廃処分日2
        label = new JLabel("産廃処分日2", JLabel.LEFT);
        label.setFont(font18);
        cont3_Field_ = new DateTimeField(
                 DateTimeField.DateFormat.YMD);
        cont3_Field_.setFont(font18);
        cont3_Field_.setFocusable(false);
        cont3_Field_.setMinimumSize(new Dimension(150, 30));
        cont3_Field_.setPreferredSize(new Dimension(150, 30));
        cont3_Field_.addKeyListener(new java.awt.event.KeyAdapter() {

            @Override
            public void keyPressed(java.awt.event.KeyEvent evt) {
                DateTimeField_KeyPressed(evt, cont3_Field_);
            }
        });

        // ダミー
        addContainer(
                 panel, new JPanel(),
                 0, 26, 1, 1, 0.0, 1.0,
                 GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                 new Insets(2, 2, 2, 2), 0, 0);
        addContainer(
                 centerPane_, panel,
                 0, 0, 1, 1, 0.5, 1.0,
                 GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                 new Insets(2, 2, 2, 2), 0, 0);

        // 詳細 - 右側
        pane = new JPanel(new GridBagLayout());
        pane.setBorder(BorderFactory.createLoweredBevelBorder());
        pane.setMinimumSize(new Dimension(520, 600));
        pane.setPreferredSize(new Dimension(520, 600));
        addContainer(
                 centerPane_, pane,
                 1, 0, 1, 1, 0.5, 0.0,
                 GridBagConstraints.EAST, GridBagConstraints.BOTH,
                 new Insets(2, 2, 2, 2), 0, 0);

        dpane = new JPanel(new GridBagLayout());
        addContainer(
                 pane, dpane,
                 1, 0, 1, 1, 0.5, 0.0,
                 GridBagConstraints.EAST, GridBagConstraints.BOTH,
                 new Insets(2, 2, 2, 2), 0, 0);

    }

    @Override
    protected void updateView() {

        if (mode_ == 1) {
            titleField_.setText("計量記録データの複写");
            titleField_.setBackground(Color.GREEN.darker());
        }
        // 総重量
        Object o = columns_.get("total_weight");
        if (o instanceof Number) {
            total_weightField_.setText(String.format("%,d", (Integer) columns_.get("total_weight")));
        }
        // 風袋重量
        o = columns_.get("empty_weight");
        if (o instanceof Number) {
            empty_weightField_.setText(String.format("%,d", (Integer) columns_.get("empty_weight")));
        }
        // 正味重量
        o = columns_.get("net_weight");
        if (o instanceof Number) {
            net_weightField_.setText(String.format("%,d", (Integer) columns_.get("net_weight")));
        }
        //20120914 add
        // 比重係数
        DecimalFormat exFormat1 = new DecimalFormat("###,###.##");
        o = columns_.get("ryube");
        if (o instanceof Number) {
            gravity_Field_.setText(exFormat1.format((Double) columns_.get("ryube")));
        }
        //リューベ
        o = columns_.get("m3");
        if (o instanceof Number) {
            m3_Field_.setText(exFormat1.format((Double) columns_.get("m3")));
        }
        o = columns_.get("class");
        if (o instanceof Number) {
            if ((Integer) o == 0) {
                jtbtn0.setSelected(true);
                jtbtn1.setSelected(false);
            } else if ((Integer) o == 1) {
                jtbtn0.setSelected(false);
                jtbtn1.setSelected(true);
            }
        }
        if (null == (Integer) columns_.get("unit_no")) {
            lblm3.setText("数量()");
        } else //数量ラベル
        {
            switch ((Integer) columns_.get("unit_no")) {
                case 1:
                    lblm3.setText("数量(ton)");
                    break;
                case 3:
                    lblm3.setText("数量(m3)");
                    break;
                case 4:
                    lblm3.setText("数量(ℓ)");
                    break;
                case 5:
                    lblm3.setText("数量(台)");
                    break;
                case 6:
                    lblm3.setText("数量(枚)");
                    break;
                case 7:
                    lblm3.setText("数量(個)");
                    break;
                default:
                    lblm3.setText("数量()");
                    break;
            }
        }

        //伝票NO
        reportNoField_.setText((String) columns_.get("report_no"));
        // 入構日時
        gateInDateField_.setValue((Calendar) columns_.get("gate_in_date"));
        // 出構日時
        gateOutDateField_.setValue((Calendar) columns_.get("gate_out_date"));
        // 請求先
        bill_id_Field_.setText(String.valueOf((Integer) columns_.get("bill_id")));
        billField_.setText((String) columns_.get("bill_name"));
        // 排出事業者
        ent_id_Field_.setText(String.valueOf((Integer) columns_.get("dis_ent_id")));
        dis_entField_.setText((String) columns_.get("entrepreneur_name"));
        // 運搬業者名
        trans_id_Field_.setText(String.valueOf((Integer) columns_.get("trans_id")));
        transField_.setText((String) columns_.get("transporter_name"));
        // 車番
        car_no_Field_.setText(columns_.get("car_no").toString());
        // 種類
        goods_id_Field_.setText(String.valueOf((Integer) columns_.get("goods_id")));
        goodsField_.setText((String) columns_.get("goods_name"));
        // 中間最終
        goods_option_name.setText((String) columns_.get("goods_option_name") + "　" + (String) columns_.get("carry_name"));
        // 排出事業場
        office_id_Field_.setText(String.valueOf((Integer) columns_.get("dis_office_id")));
        dis_office_Field_.setText((String) columns_.get("office_name"));
        office_sub_Field_.setText((String) columns_.get("office_sub_name"));
        // 処分業者
        cont_id_Field_.setText(String.valueOf((Integer) columns_.get("dis_cont_id")));
        dis_contField_.setText((String) columns_.get("contractor_name"));
        // 車種
        car_kind_id_Field_.setText(String.valueOf((Integer) columns_.get("car_kind_id")));
        car_kindField_.setText((String) columns_.get("car_kind_name"));
        // 料金単価
        DecimalFormat exFormat2 = new DecimalFormat("###,###");
        o = columns_.get("unit");
        if (o instanceof Number) {
            unit_Field_.setText(exFormat2.format((Double) columns_.get("unit")));
            //正味重量、料金算出
            Charge_value();
        }
        // 料金
        o = columns_.get("charge_value");
        if (o instanceof Number) {
            charge_Field_.setText(String.format("%,d", (Integer) columns_.get("charge_value")));
        }
        // 建廃マニフェスト
        manifesto2_Field_.setText((String) columns_.get("manifesto_no2"));///////////////////////nullになってて新規登録できないもよう、クリア””するとどうか？
        // 産廃マニフェスト1
        manifesto1_Field_.setText((String) columns_.get("manifesto_no1"));

        // 建廃処分日
        if (columns_.get("cont_date2") != null) {
            o = columns_.get("cont_date2");
            if (o instanceof Calendar) {
                if ((String) columns_.get("manifesto_no2") != null) {
                    cont1_Field_.setValue((Calendar) o);
                } else if ((String) columns_.get("manifesto_no1") != null) {
                    // 産廃処分日
                    cont2_Field_.setValue((Calendar) o);
                } else if ((String) columns_.get("manifesto_no3") != null) {
                    // 産廃処分日
                    cont3_Field_.setValue((Calendar) o);
                } else {
                    cont1_Field_.setValue(null);
                    cont2_Field_.setValue(null);
                    cont3_Field_.setValue(null);
                }
            } else if (o instanceof Date) {
                if ((String) columns_.get("manifesto_no2") != null) {
                    Calendar cal = Calendar.getInstance();
                    int yy = Integer.valueOf(String.format("%1$tY", (Date) o));
                    int mm = Integer.valueOf(String.format("%1$tm", (Date) o));
                    int dd = Integer.valueOf(String.format("%1$td", (Date) o));
                    cal.set(Calendar.YEAR, yy);
                    cal.set(Calendar.MONTH, mm - 1);
                    cal.set(Calendar.DAY_OF_MONTH, dd);
                    cont1_Field_.setValue((Calendar) cal);
                } else if ((String) columns_.get("manifesto_no1") != null) {
                    // 産廃処分日
                    Calendar cal = Calendar.getInstance();
                    int yy = Integer.valueOf(String.format("%1$tY", (Date) o));
                    int mm = Integer.valueOf(String.format("%1$tm", (Date) o));
                    int dd = Integer.valueOf(String.format("%1$td", (Date) o));
                    cal.set(Calendar.YEAR, yy);
                    cal.set(Calendar.MONTH, mm - 1);
                    cal.set(Calendar.DAY_OF_MONTH, dd);
                    cont2_Field_.setValue((Calendar) cal);
                } else if ((String) columns_.get("manifesto_no3") != null) {
                    // 産廃処分日
                    Calendar cal = Calendar.getInstance();
                    int yy = Integer.valueOf(String.format("%1$tY", (Date) o));
                    int mm = Integer.valueOf(String.format("%1$tm", (Date) o));
                    int dd = Integer.valueOf(String.format("%1$td", (Date) o));
                    cal.set(Calendar.YEAR, yy);
                    cal.set(Calendar.MONTH, mm - 1);
                    cal.set(Calendar.DAY_OF_MONTH, dd);
                    cont3_Field_.setValue((Calendar) cal);
                } else {
                    cont1_Field_.setValue(null);
                    cont2_Field_.setValue(null);
                    cont3_Field_.setValue(null);
                }
            }
        } else {
            o = (Calendar) columns_.get("gate_out_date");
            if ((String) columns_.get("manifesto_no2") != null) {
            } else if ((String) columns_.get("manifesto_no1") != null) {
            } else if ((String) columns_.get("manifesto_no3") != null) {
            } else {
                cont1_Field_.setValue(null);
                cont2_Field_.setValue(null);
                cont3_Field_.setValue(null);
            }
            //20260317 edit//////////////////////////////////////////////////////////////////
            //ここだとイベントが起きないケースがあるのできちんと表示されない
//            //売上区分
//            売上区分cmb.setSelectedItem((columns_.get("売上区分")));
        }

        //20260317 add ///if分の外で///////////////////////////////////////
        //売上区分
        売上区分cmb.setSelectedItem((columns_.get("売上区分")));
        //////////////////////////////////////////////////////////////////

        unit_name.setText(" (" + (String) columns_.get("unit_name") + ")");

        o = columns_.get("unit_value");//参考単価
        if (o instanceof Number) {
            unit_value_Field_.setText(String.format("%,d", (Integer) columns_.get("unit_value")));
        }
        operatorField_.setText((String) columns_.get("operator"));
        o = columns_.get("last_update");
        if (o instanceof Calendar) {
            updateTimeField_.setValue(((Calendar) o).getTime());
        }
    }

    @Override
    protected void doDelete() {
        int result = JOptionPane.showConfirmDialog(
                 this,
                 "この項目を削除します. よろしいですか?",
                 "確認",
                 JOptionPane.YES_NO_OPTION);
        if (JOptionPane.YES_OPTION == result) {
            WeighListJournal.recordJournal(
                     model_.getConnection(),
                     "計量一覧 - 削除",
                     new HashMap<>(model_.getColumns(row_)));

            //送信後スリープ
            try {
                Thread.sleep(200);
            } catch (Exception e) {
                System.out.println("GetProductDThread-run-exp");
            }

            if (model_.delete(row_)) {
                doClose();
            } else {
                JOptionPane.showMessageDialog(
                         this,
                         "削除に失敗しました.",
                         "警告",
                         JOptionPane.WARNING_MESSAGE);
            }
        }
    }

    @Override
    protected void doCancel() {
        copyColumns();

        super.doCancel();
    }

    @Override
    protected void doRegister() {
        if (first_regist_flag_) {
            return;
        }
        if (!checkInputItems()) {
            return;
        }

        if (0 > row_) {
            WeighListJournal.recordJournal(
                     model_.getConnection(),
                     "計量一覧 - 追加",
                     columns_);

            Object o = cont1_Field_.getValue();
            if (o instanceof Calendar) {
                columns_.put("cont_date2", (Calendar) o);
            }

            if (model_.insert(columns_)) {
                //マニフェストデータ作成
                model_.insert2(columns_);
                message_ = "追加しました。";
                first_regist_flag_ = true;

                doMessage();

                doClose();
                return;
            }
        } else if (mode_ == 0) {
            WeighListJournal.recordJournal(
                     model_.getConnection(),
                     "計量一覧 - 訂正",
                     columns_);

            //送信後スリープ
            try {
                Thread.sleep(200);
            } catch (Exception e) {
                System.out.println("GetProductDThread-run-exp");
            }

            // 建廃処分日
            Object o = cont1_Field_.getValue();
            if (o instanceof Calendar) {
                columns_.put("cont_date2", cont1_Field_.getValue());
            } else {
                columns_.put("cont_date2", null);
            }
            o = cont2_Field_.getValue();
            if (o instanceof Calendar) {
                columns_.put("cont_date1", cont2_Field_.getValue());
            } else {
                columns_.put("cont_date1", null);
            }
            o = cont3_Field_.getValue();
            if (o instanceof Calendar) {
                columns_.put("cont_date3", cont3_Field_.getValue());
            } else {
                columns_.put("cont_date3", null);
            }

            if (model_.update(columns_)) {
                if (dWieghPane2 != null && (Boolean) columns_.get("f按分フラグ")) {
                    dWieghPane2.update();
                }

                //
                if (manifesto1_Field_.getText() == null ? "" != null : !manifesto1_Field_.getText().equals("")) {
                    if (model_.SetManifestoData(String.valueOf((Long) columns_.get("sno"))) > 0) {
                    }

                }
                if (manifesto2_Field_.getText() == null ? "" != null : !manifesto2_Field_.getText().equals("")) {
                    if (model_.SetManifestoData(String.valueOf((Long) columns_.get("sno"))) > 0) {
                    }
                }

                super.message_ = "登録しました。";
                doMessage();
                doClose();
                return;

            }
        } else {
            WeighListJournal.recordJournal(
                     model_.getConnection(),
                     "計量一覧 - 複写",
                     columns_);

            Object o = cont1_Field_.getValue();
            if (o instanceof Calendar) {
                columns_.put("cont_date2", (Calendar) o);
            }

            if (model_.insert(columns_)) {
                //マニフェストデータ作成
                model_.insert2(columns_);
                message_ = "複写しました。";
                first_regist_flag_ = true;
                doMessage();

                doClose();
                return;
            }
        }

        JOptionPane.showMessageDialog(
                 this,
                 "登録に失敗しました.",
                 "警告",
                 JOptionPane.WARNING_MESSAGE);
    }

    public void doCollect(java.awt.event.ActionEvent evt) {
    }

    @Override
    protected void doMinimum() {
        super.doMinimum();
    }

    @Override
    protected void doMovePrevious() {
        if (0 < row_) {
            row_--;
            copyColumns();
            updateView();
        }
    }

    @Override
    protected void doMoveNext() {
        if ((0 <= row_) && ((model_.getRowCount() - 1) > row_)) {
            row_++;
            copyColumns();
            updateView();
        }
    }

    private void copyColumns() {
        columns_.clear();
        if (0 <= row_) {
            columns_.putAll(model_.getColumns(row_));
            if (mode_ == 1) {
                columns_.put("report_no", "");
            }
        }
        Calendar c = Calendar.getInstance();
        if (0 > row_) {
            columns_.put("total_weight", 0);
            columns_.put("empty_weight", 0);
            columns_.put("net_weight", 0);
            columns_.put("gate_in_date", c);
            columns_.put("gate_out_date", c);
            columns_.put("term_date", TermDate.getTerm(c).getTime());

            columns_.put("car_no", 0);
            columns_.put("unit", 0.);
            columns_.put("charge_value", 0);
            columns_.put("class", 0);

            columns_.put("bill_id", 0);
            columns_.put("dis_ent_id", 0);
            columns_.put("trans_id", 0);
            columns_.put("goods_id", 0);
            columns_.put("dis_office_id", 0);
            columns_.put("dis_cont_id", 0);
            columns_.put("car_kind_id", 0);

            columns_.put("method_id1", 0);
            columns_.put("method_id2", 0);

            columns_.put("unit_value", 0);
            columns_.put("unit_name", "");
            // 中間最終
            columns_.put("goods_option_name", "中間");
            //20120617 現場補完分
            columns_.put("office_sub_name", "");
            //20120914 リューベ追加
            columns_.put("m3", 0.);
            //20121017 add 搬入出区分追加
            columns_.put("carry_class", 0);
            columns_.put("carry_name", "搬入");
            //20121022 add 請求単価・運搬単価追加
            columns_.put("unit_no", 0);
            columns_.put("unit_name", "");

            columns_.put("trans_unit_value", 0);
            columns_.put("ryube", 0.);
            columns_.put("売上区分", "掛け");

        }
    }

    private class Set処分日Action implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent event) {

            Object o = (Calendar) columns_.get("gate_out_date");
            if ((String) columns_.get("manifesto_no2") != null) {
                cont1_Field_.setValue((Calendar) o);
                columns_.put("cont_date2", cont1_Field_.getValue());
            } else if ((String) columns_.get("manifesto_no1") != null) {
                // 産廃処分日
                cont2_Field_.setValue((Calendar) o);
                columns_.put("cont_date1", cont2_Field_.getValue());

            } else if ((String) columns_.get("manifesto_no3") != null) {
                // 産廃処分日
                cont3_Field_.setValue((Calendar) o);
                columns_.put("cont_date3", cont3_Field_.getValue());
            } else {
                cont1_Field_.setValue((Calendar) o);
                cont2_Field_.setValue((Calendar) o);
                cont3_Field_.setValue((Calendar) o);
            }
        }
    }

    private class Clear処分日Action implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent event) {
            //20140424 add
//    //ボタンひとつで処分日を消去する
            cont1_Field_.setValue(null);
            cont2_Field_.setValue(null);
            cont3_Field_.setValue(null);
            columns_.put("cont_date1", null);
            columns_.put("cont_date2", null);
            columns_.put("cont_date3", null);

        }
    }

    private boolean checkInputItems() {

        if (manifesto2_Field_.getText().length() > 0 && manifesto2_Field_.getText().length() != 11) {
            JOptionPane.showMessageDialog(
                     this,
                     "建廃交付番号の入力桁数を確認してください。",
                     "警告",
                     JOptionPane.WARNING_MESSAGE);
            return false;
        }
        if (manifesto1_Field_.getText().length() > 0 && manifesto1_Field_.getText().length() != 11) {
            JOptionPane.showMessageDialog(
                     this,
                     "産廃交付番号の入力桁数を確認してください。",
                     "警告",
                     JOptionPane.WARNING_MESSAGE);
            return false;
        }

        return true;
    }

    private void JTextField_KeyPressed(java.awt.event.KeyEvent evt, JTextField JTextField_) {

        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            JTextField_.transferFocus();
            evt.consume();

        }
    }

    private void JTextField_KeyPressed(java.awt.event.KeyEvent evt, JTextFieldHankaku JTextField_) {

        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            JTextField_.transferFocus();
            evt.consume();
        }
    }
    //

    private void DateTimeField_KeyPressed(java.awt.event.KeyEvent evt, DateTimeField DateTimeField_) {

        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            DateTimeField_.transferFocus();
            evt.consume();
        }
    }

    private class ColumnUpdater implements FocusListener {

        @Override
        public void focusGained(FocusEvent event) {
            Object source = event.getSource();
            if (source == car_no_Field_) {
                car_no_Field_.selectAll();
            } else if (source == total_weightField_) {
                total_weightField_.selectAll();
            } else if (source == empty_weightField_) {
                empty_weightField_.selectAll();
            } else if (source == net_weightField_) {
                net_weightField_.selectAll();
            } else if (source == unit_Field_) {
                unit_Field_.selectAll();
            } else if (source == charge_Field_) {
                charge_Field_.selectAll();
            } else if (source == manifesto1_Field_) {
                manifesto1_Field_.selectAll();
            } else if (source == manifesto2_Field_) {
                manifesto2_Field_.selectAll();
            } else if (source == ent_id_Field_) {
                ent_id_Field_.selectAll();
            } else if (source == trans_id_Field_) {
                trans_id_Field_.selectAll();
            } else if (source == goods_id_Field_) {
                goods_id_Field_.selectAll();
            } else if (source == office_id_Field_) {
                office_id_Field_.selectAll();
            } else if (source == cont_id_Field_) {
                cont_id_Field_.selectAll();
            } else if (source == car_kind_id_Field_) {
                car_kind_id_Field_.selectAll();
            } else if (source == bill_id_Field_) {
                bill_id_Field_.selectAll();
            } else if (source == office_sub_Field_) {
                office_sub_Field_.selectAll();
            } else if (source == gravity_Field_) {
                gravity_Field_.selectAll();
            } else if (source == m3_Field_) {
                m3_Field_.selectAll();
            }

        }

        @Override
        public void focusLost(FocusEvent event) {
            Object source = event.getSource();
            if (source == gateInDateField_) {
                columns_.put("gate_in_date", gateInDateField_.getValue());
            } else if (source == gateInWeightField_) {
                columns_.put("gate_in_weight", Integer.valueOf(gateInWeightField_.getText().replace(",", "")));

                if (Integer.valueOf(gateInWeightField_.getText().replace(",", ""))
                         >= Integer.valueOf(gateOutWeightField_.getText().replace(",", ""))) {
                    columns_.put("net_weight", Integer.valueOf(gateInWeightField_.getText().replace(",", ""))
                             - Integer.valueOf(gateOutWeightField_.getText().replace(",", "")));
                } else {
                    columns_.put("net_weight", Integer.valueOf(gateOutWeightField_.getText().replace(",", ""))
                             - Integer.valueOf(gateInWeightField_.getText().replace(",", "")));
                }
            } else if (source == gateOutDateField_) {
                columns_.put("gate_out_date", gateOutDateField_.getValue());
                columns_.put(
                         "term_date",
                         TermDate.getTerm(gateOutDateField_.getValue()).getTime());
            } else if (source == gateOutWeightField_) {
                columns_.put("gate_out_weight", Integer.valueOf(gateOutWeightField_.getText().replace(",", "")));
                if (Integer.valueOf(gateInWeightField_.getText().replace(",", ""))
                         >= Integer.valueOf(gateOutWeightField_.getText().replace(",", ""))) {
                    columns_.put("net_weight", Integer.valueOf(gateInWeightField_.getText().replace(",", ""))
                             - Integer.valueOf(gateOutWeightField_.getText().replace(",", "")));
                } else {
                    columns_.put("net_weight", Integer.valueOf(gateOutWeightField_.getText().replace(",", ""))
                             - Integer.valueOf(gateInWeightField_.getText().replace(",", "")));
                }

            } else if (source == car_no_Field_) {
                columns_.put("car_no", Integer.valueOf(car_no_Field_.getText()));
                //請求先追加
            } else if (source == bill_id_Field_) {
                columns_.put("bill_id", Integer.valueOf(bill_id_Field_.getText()));
                columns_.put("bill_name", getMasterData("v0_bill", (Integer) columns_.get("bill_id")));

            } else if (source == ent_id_Field_) {
                columns_.put("dis_ent_id", Integer.valueOf(ent_id_Field_.getText()));
                columns_.put("entrepreneur_name", getMasterData("v0_ent", (Integer) columns_.get("dis_ent_id")));
                //契約ごとの単価取得
                getAgreementUnit((Integer) columns_.get("dis_ent_id"),
                         (Integer) columns_.get("dis_office_id"),
                         (Integer) columns_.get("goods_id"));

            } else if (source == trans_id_Field_) {
                columns_.put("trans_id", Integer.valueOf(trans_id_Field_.getText()));
                columns_.put("transporter_name", getMasterData("v0_trans", (Integer) columns_.get("trans_id")));

            } else if (source == goods_id_Field_) {
                columns_.put("goods_id", Integer.valueOf(goods_id_Field_.getText()));
                getMasterData_sub9("v0_goods_kind", (Integer) columns_.get("goods_id"));
                //契約ごとの単価取得
                getAgreementUnit((Integer) columns_.get("dis_ent_id"),
                         (Integer) columns_.get("dis_office_id"),
                         (Integer) columns_.get("goods_id"));

                if (Double.valueOf(gravity_Field_.getText().replace(",", "")) == 0.) { // 比重係数が0なら
                    columns_.put("ryube", getMasterData_sub7("v0_goods_kind", (Integer) columns_.get("goods_id")));
                }

                // 中間最終
                goods_option_name.setText((String) columns_.get("goods_option_name") + "　" + (String) columns_.get("carry_name"));
            } else if (source == office_id_Field_) {
                columns_.put("dis_office_id", Integer.valueOf(office_id_Field_.getText()));
                getMasterData_sub9("v0_goods_kind", (Integer) columns_.get("goods_id"));
                columns_.put("office_name", getMasterData_office("v0_office", (Integer) columns_.get("dis_office_id")));
                //契約ごとの単価取得
                getAgreementUnit((Integer) columns_.get("dis_ent_id"),
                         (Integer) columns_.get("dis_office_id"),
                         (Integer) columns_.get("goods_id"));

            } else if (source == cont_id_Field_) {
                columns_.put("dis_cont_id", Integer.valueOf(cont_id_Field_.getText()));
                columns_.put("contractor_name", getMasterData("v0_cont", (Integer) columns_.get("dis_cont_id")));
            } else if (source == car_kind_id_Field_) {
                columns_.put("car_kind_id", Integer.valueOf(car_kind_id_Field_.getText()));
                columns_.put("car_kind_name", getMasterData("v0_car_kind", (Integer) columns_.get("car_kind_id")));

                // 料金単価
            } else if (source == unit_Field_) {
                columns_.put("unit", Double.valueOf(unit_Field_.getText().replace(",", "")));
                double billValue = 0.;

                if ((Integer) columns_.get("unit_no") == 0) {
                    billValue = (Integer) columns_.get("net_weight");
                } else if ((Integer) columns_.get("unit_no") == 1) {
                    billValue = ((Integer) columns_.get("net_weight") / 1000D);//ton
                } else if ((Integer) columns_.get("unit_no") == 2) {
                    billValue = (Integer) columns_.get("net_weight");//kg
                } else if ((Integer) columns_.get("unit_no") == 3) {
                    billValue = (Double) columns_.get("m3");//m3
                } else if ((Integer) columns_.get("unit_no") == 4) {
                    billValue = 1;//リットル
                } else if ((Integer) columns_.get("unit_no") == 5) {
                    billValue = 1;//台
                } else if ((Integer) columns_.get("unit_no") == 6) {
                    billValue = 1;//枚
                } else if ((Integer) columns_.get("unit_no") == 7) {
                    billValue = 1;//個
                } else if ((Integer) columns_.get("unit_no") == 8) {
                    billValue = 1;//日
                } else if ((Integer) columns_.get("unit_no") == 9) {
                    billValue = 1;//回
                } else {
                    billValue = 0;
                }

                double unit_ = Double.valueOf(unit_Field_.getText().replace(",", ""));
                columns_.put("charge_value", (int) Math.round(billValue * unit_));

                // 料金
            } else if (source == charge_Field_) {
                columns_.put("charge_value", Integer.valueOf(charge_Field_.getText().replace(",", "")));

            } else if (source == manifesto1_Field_) {
                columns_.put("manifesto_no1", manifesto1_Field_.getText());
            } else if (source == manifesto2_Field_) {
                columns_.put("manifesto_no2", manifesto2_Field_.getText());

            } else if (source == total_weightField_) { // 総重量
                double d = Double.valueOf(gravity_Field_.getText());
                columns_.put("total_weight", Integer.valueOf(total_weightField_.getText().replace(",", "")));

                //正味重量、料金算出
                Charge_value();

                if (d != 0.) {
                    try {
                        double g_ = Double.valueOf(gravity_Field_.getText().replace(",", ""));

                        // 正味重量 / 係数　での小数第三を四捨五入
                        columns_.put("m3", Math.round((Integer.valueOf((Integer) columns_.get("net_weight")) / g_) / 10) / 100D);

                    } catch (Exception e) {
                        m3_Field_.setText("0.0");
                        columns_.put("m3", 0.);
                        gravity_Field_.setText("0.0");
                        columns_.put("ryube", 0.);
                    }

                } else {
                    columns_.put("total_weight", Integer.valueOf(total_weightField_.getText().replace(",", "")));
                    m3_Field_.setText("0.0");
                    columns_.put("m3", 0.);
                    gravity_Field_.setText("0.0");
                    columns_.put("ryube", 0.);
                }

            } else if (source == empty_weightField_) { //風袋重量
                double d = Double.valueOf(gravity_Field_.getText());
                columns_.put("empty_weight", Integer.valueOf(empty_weightField_.getText().replace(",", "")));

                //正味重量、料金算出
                Charge_value();

                if (d != 0.) { //比重係数が0以外なら計算させる
                    columns_.put("empty_weight", Integer.valueOf(empty_weightField_.getText().replace(",", "")));

                } else {
                    columns_.put("empty_weight", Integer.valueOf(empty_weightField_.getText().replace(",", "")));
                    m3_Field_.setText("0.0");
                    columns_.put("m3", 0.);
                    gravity_Field_.setText("0.0");
                    columns_.put("ryube", 0.);
                }

            } else if (source == office_sub_Field_) {
                columns_.put("office_sub_name", office_sub_Field_.getText());

                //比重係数
            } else if (source == gravity_Field_) {
                double d = Double.valueOf(gravity_Field_.getText());
                if (d != 0.) {
                    columns_.put("ryube", Math.round(Double.valueOf(gravity_Field_.getText()) * 100) / 100D);

                    try {

                        double g_ = (Double) (columns_.get("ryube"));
                        //正味重量 / 係数　での小数第三を四捨五入
                        columns_.put("m3", Math.round((Integer.valueOf((Integer) columns_.get("net_weight")) / g_) / 10) / 100D);

                    } catch (Exception e) {
                        m3_Field_.setText("0.0");
                        columns_.put("m3", 0.);
                        gravity_Field_.setText("0.0");
                        columns_.put("ryube", 0.);
                    }
                } else {
                    m3_Field_.setText("0.0");
                    columns_.put("m3", 0.);
                    gravity_Field_.setText("0.0");
                    columns_.put("ryube", 0.);
                }

                if (Integer.valueOf(net_weightField_.getText().replace(",", ""))
                         >= Double.valueOf(gravity_Field_.getText().replace(",", ""))) {
                    columns_.put("net_weight", Integer.valueOf(total_weightField_.getText().replace(",", ""))
                             - Integer.valueOf(empty_weightField_.getText().replace(",", "")));

                    double billValue = 0.;
                    if ((Integer) columns_.get("unit_no") == 0) {
                        billValue = (Integer) columns_.get("net_weight");
                    } else if ((Integer) columns_.get("unit_no") == 1) {
                        billValue = ((Integer) columns_.get("net_weight") / 1000D);//ton
                    } else if ((Integer) columns_.get("unit_no") == 2) {
                        billValue = (Integer) columns_.get("net_weight");//kg
                    } else if ((Integer) columns_.get("unit_no") == 3) {
                        billValue = (Double) columns_.get("m3");//m3
                    } else if ((Integer) columns_.get("unit_no") == 4) {
                        billValue = 0;//リットル
                    } else if ((Integer) columns_.get("unit_no") == 5) {
                        billValue = 1;//台
                    } else if ((Integer) columns_.get("unit_no") == 6) {
                        billValue = 1;//枚
                    } else if ((Integer) columns_.get("unit_no") == 7) {
                        billValue = 0;//個
                    } else if ((Integer) columns_.get("unit_no") == 8) {
                        billValue = 0;//日
                    } else if ((Integer) columns_.get("unit_no") == 9) {
                        billValue = 1;//回
                    } else {
                        billValue = 0;
                    }

                    double unit_ = Double.valueOf(unit_Field_.getText().replace(",", ""));
                    columns_.put("charge_value", (int) Math.round(billValue * unit_));

                }

                //リューベ
            } else if (source == m3_Field_) {
                try {
                    columns_.put("m3", Math.round(Double.valueOf(m3_Field_.getText()) * 100) / 100D);
                } catch (Exception e) {
                    m3_Field_.setText("0.0");
                    columns_.put("m3", 0.);
                }
            } else if (source == 売上区分cmb) {
                columns_.put("売上区分", 売上区分cmb.getSelectedItem());
            }
            get単位(Integer.valueOf(goods_id_Field_.getText()));
            // 数量(m3/ton/ℓ/台/枚/個)は利用者が入力した値を保持する。
            // 以前はフォーカス移動のたびに ton 数量を正味重量から再計算していたため、
            // 手入力した数量が上書きされていた。
            updateView();
        }
    }

    //料金算出ファンクション
    private void Charge_value() {
        //先に正味重量を計算させる
        if (Integer.valueOf(total_weightField_.getText().replace(",", ""))
                 >= Integer.valueOf(empty_weightField_.getText().replace(",", ""))) {
            columns_.put("net_weight", Integer.valueOf(total_weightField_.getText().replace(",", ""))
                     - Integer.valueOf(empty_weightField_.getText().replace(",", "")));

            double billValue = 0.;
            if ((Integer) columns_.get("unit_no") == 0) {
                billValue = (Integer) columns_.get("net_weight");
            } else if ((Integer) columns_.get("unit_no") == 1) {
                billValue = ((Integer) columns_.get("net_weight") / 1000D);//ton
            } else if ((Integer) columns_.get("unit_no") == 2) {
                billValue = (Integer) columns_.get("net_weight");//kg
            } else if ((Integer) columns_.get("unit_no") == 3) {
                billValue = (Double) columns_.get("m3");//m3
            } else if ((Integer) columns_.get("unit_no") == 4) {
                billValue = 0;//リットル
            } else if ((Integer) columns_.get("unit_no") == 5) {
                billValue = 1;//台
            } else if ((Integer) columns_.get("unit_no") == 6) {
                //畳の場合数量の値を得るようにする
                billValue = Integer.valueOf(m3_Field_.getText());//枚
            } else if ((Integer) columns_.get("unit_no") == 7) {
                billValue = 1;//個
            } else if ((Integer) columns_.get("unit_no") == 8) {
                billValue = 0;//日
            } else if ((Integer) columns_.get("unit_no") == 9) {
                billValue = 1;//回
            } else {
                billValue = 0;
            }

            double unit_ = Double.valueOf(unit_Field_.getText().replace(",", ""));
            columns_.put("charge_value", (int) Math.round(billValue * unit_));

        }
    }

    private void getAgreementUnit(int ent_id_, int office_id_, int goods_id_) {

        try {
            //商品マスタの確認
            String s = "select * from v0_office_unit"
                     + " where ent_id = ? "
                     + " and office_id = ? "
                     + " and goods_id = ?; ";

            PreparedStatement ps = con_.prepareStatement(s);
            try {
                ps.setObject(1, ent_id_);
                ps.setObject(2, office_id_);
                ps.setObject(3, goods_id_);
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    columns_.put("unit", rs.getDouble("goods_unit"));
                    columns_.put("unit_no", rs.getInt("goods_unit_no"));
                } else {
                    columns_.put("unit", 0.);
                    columns_.put("unit_no", 0);
                }

            } finally {
                ps.close();
            }
        } catch (Exception e) {
            System.out.println(e.toString());
        }
    }

    private void get単位(int goods_id_) {

        try {
            //商品マスタの確認
            String s = "select * from v0_goods_kind"
                     + " where id = ?; ";

            PreparedStatement ps = con_.prepareStatement(s);
            try {
                ps.setObject(1, goods_id_);
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    columns_.put("unit_no", rs.getInt("unit_no"));
                } else {
                    columns_.put("unit_no", 0);
                }

            } finally {
                ps.close();
            }
        } catch (Exception e) {
            System.out.println(e.toString());

        }
    }

    private String getMasterData(String veiw_name, int id_) {
        String master_name = "";
        try {

            //重複登録の確認
            String s = "select name from "
                     + veiw_name
                     + " where id = "
                     + String.valueOf(id_);

            PreparedStatement ps = model_.getConnection().prepareStatement(s);
            try {
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    master_name = rs.getString("name");
                } else {
                    master_name = "";
                }
            } finally {
                ps.close();
            }
        } catch (SQLException e) {
            System.out.println(e.toString());
        }

        return master_name;
    }

    private String getMasterData_office(String veiw_name, int id_) {
        String master_name = "";
        try {

            //重複登録の確認
            String s = "select * from "
                     + veiw_name
                     + " where id = "
                     + String.valueOf(id_)
                     + " and ent_id = "
                     + String.valueOf(columns_.get("dis_ent_id"))
                     + " and finish_flag = false "
                     + " and delete_flag = false;";

            PreparedStatement ps = model_.getConnection().prepareStatement(s);
            try {
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    master_name = rs.getString("name");
                    columns_.put("office_tel", rs.getString("tel"));
                    columns_.put("office_zip", rs.getString("zip_code"));
                    columns_.put("office_address", rs.getString("address1"));

                } else {
                    master_name = "";
                }
            } finally {
                ps.close();
            }
        } catch (SQLException e) {
            System.out.println(e.toString());
        }

        return master_name;
    }

    private double getMasterData_sub7(String veiw_name, int id_) {
        double keisu = 0.;
        try {

            //登録単価の確認
            String s = "select ryube from "
                     + veiw_name
                     + " where id = "
                     + String.valueOf(id_);

            PreparedStatement ps = model_.getConnection().prepareStatement(s);
            try {
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    keisu = rs.getDouble("ryube");
                } else {
                    keisu = 0.;
                }
            } finally {
                ps.close();
            }
        } catch (SQLException e) {
            System.out.println(e.toString());
        }

        return keisu;
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

    private class SelectCarAction implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent event) {
            CarSelector selector = new CarSelector(model_.getConnection());
            switch (selector.showSelector(WeighListDetailsPane.this)) {
                case CarSelector.CLEARED:
                    columns_.put("car_no", 0);
                    columns_.put("bill_id", 0);
                    columns_.put("bill_name", "");
                    columns_.put("dis_ent_id", 0);
                    columns_.put("entrepreneur_name", "");
                    columns_.put("trans_id", 0);
                    columns_.put("transporter_name", "");
                    columns_.put("goods_id", 0);
                    columns_.put("goods_name", "");
                    columns_.put("goods_option_name", "");
                    columns_.put("dis_office_id", 0);
                    columns_.put("office_name", "");
                    columns_.put("dis_cont_id", 0);
                    columns_.put("contractor_name", "");
                    columns_.put("car_kind_id", 0);
                    columns_.put("car_kind_name", "");
                    columns_.put("method_id1", 0);
                    columns_.put("method_id2", 0);
                    columns_.put("carry_class", 0);
                    columns_.put("carry_name", "搬入");
                    //20121022 add 請求単価・運搬単価追加
                    columns_.put("unit_no", 0);
                    columns_.put("unit_name", "");
                    columns_.put("unit_value", 0);
                    columns_.put("charge_value", 0);
                    columns_.put("trans_unit_value", 0);
                    columns_.put("ryube", 0.);

                    break;
                case CarSelector.SELECTED:
                    Map<String, Object> item = selector.selectedRow();
                    columns_.put("car_no", item.get("car_no"));
                    columns_.put("bill_id", item.get("bill_id"));
                    columns_.put("bill_name", (String) item.get("bill_name"));
                    columns_.put("dis_ent_id", item.get("dis_ent_id"));
                    columns_.put("entrepreneur_name", (String) item.get("entrepreneur_name"));
                    columns_.put("trans_id", item.get("trans_id"));
                    columns_.put("transporter_name", (String) item.get("transporter_name"));
                    columns_.put("goods_id", item.get("goods_id"));
                    // 20140423
                    //先に商品マスタ参照　その後車両マスタの商品単価設定するため
                    getMasterData_sub9("v0_goods_kind", (Integer) columns_.get("goods_id"));
                    columns_.put("goods_name", (String) item.get("goods_name"));
                    columns_.put("goods_option_name", (String) item.get("goods_option_name"));
                    columns_.put("dis_office_id", item.get("dis_office_id"));
                    columns_.put("office_name", (String) item.get("office_name"));
                    columns_.put("dis_cont_id", item.get("dis_cont_id"));
                    columns_.put("contractor_name", (String) item.get("contractor_name"));
                    columns_.put("car_kind_id", item.get("car_kind_id"));
                    columns_.put("car_kind_name", (String) item.get("car_kind_name"));
                    columns_.put("method_id1", item.get("method_id1"));
                    columns_.put("method_id2", item.get("method_id2"));
                    columns_.put("carry_class", item.get("carry_class"));
                    columns_.put("carry_name", (String) item.get("carry_name"));
                    //20121022 add 請求単価・運搬単価追加
                    columns_.put("unit_no", item.get("unit_no"));
                    columns_.put("unit_name", (String) item.get("unit_name"));
                    columns_.put("unit_value", item.get("unit_value"));
                    columns_.put("charge_value", item.get("charge_value"));
                    columns_.put("trans_unit_value", item.get("trans_unit_value"));
                    columns_.put("ryube", item.get("ryube"));
                    //20140423
                    columns_.put("empty_weight", item.get("empty_weight"));
                    bill_id_Field_.requestFocus();

                    break;
                default:
                // No operation.
            }
            updateView();
        }
    }

    //請求先セレクター
    //ダイアログを開く
    private class SelectBillAction implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent event) {
            BillSelector selector = new BillSelector(model_.getConnection());
            switch (selector.showSelector(WeighListDetailsPane.this)) {
                case EntSelector.CLEARED:
                    columns_.put("bill_id", 0);
                    columns_.put("bill_name", "");
                    break;
                case EntSelector.SELECTED:
                    Map<String, Object> item = selector.selectedRow();
                    columns_.put("bill_id", item.get("id"));
                    columns_.put("bill_name", (String) item.get("name"));
                    break;
                default:
                // No operation.
            }
            updateView();
        }
    }

    //排出事業者セレクター
    //ダイアログを開く
    private class SelectEntAction implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent event) {
            EntSelector selector = new EntSelector(model_.getConnection());
            switch (selector.showSelector(WeighListDetailsPane.this)) {
                case EntSelector.CLEARED:
                    columns_.put("dis_ent_id", 0);
                    columns_.put("entrepreneur_name", "");
                    break;
                case EntSelector.SELECTED:
                    Map<String, Object> item = selector.selectedRow();
                    columns_.put("dis_ent_id", item.get("id"));
                    columns_.put("entrepreneur_name", (String) item.get("name"));
                    break;
                default:
                // No operation.
            }
            updateView();
        }
    }

    private class SelectTransporterAction implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent event) {
            TransSelector selector = new TransSelector(model_.getConnection());
            switch (selector.showSelector(WeighListDetailsPane.this)) {
                case TransSelector.CLEARED:
                    columns_.put("trans_id", 0);
                    columns_.put("transporter_name", "");
                    break;
                case TransSelector.SELECTED:
                    Map<String, Object> item = selector.selectedRow();
                    columns_.put("trans_id", item.get("id"));
                    columns_.put("transporter_name", (String) item.get("name"));
                    break;
                default:
                // No operation.
            }
            updateView();
        }
    }

    private class SelectGoodsAction implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent event) {
            GoodsSelector selector = new GoodsSelector(model_.getConnection());
            switch (selector.showSelector(WeighListDetailsPane.this)) {
                case GoodsSelector.CLEARED:
                    columns_.put("goods_id", 0);
                    columns_.put("goods_name", "");
                    columns_.put("goods_option_name", "");
                    columns_.put("method_id1", 0);
                    columns_.put("method_id2", 0);
                    columns_.put("unit_no", 0);
                    columns_.put("unit_name", "");
                    columns_.put("carry_class", 0);
                    columns_.put("carry_name", "搬入");
                    columns_.put("unit_value", 0);
                    columns_.put("option1", 0);
                    columns_.put("ryube", 0.);

                    break;
                case GoodsSelector.SELECTED:
                    Map<String, Object> item = selector.selectedRow();
                    columns_.put("goods_id", item.get("id"));
                    columns_.put("goods_name", (String) item.get("name"));
                    columns_.put("goods_option_name", (String) item.get("option1_name"));
                    columns_.put("method_id1", item.get("method_id1"));
                    columns_.put("method_id2", item.get("method_id2"));
                    columns_.put("unit_no", item.get("unit_no"));
                    columns_.put("unit_name", item.get("unit_name"));
                    columns_.put("carry_class", item.get("carry_class"));
                    columns_.put("carry_name", item.get("carry_name"));
                    columns_.put("unit_value", item.get("unit_value"));
                    columns_.put("option1", item.get("option1"));
                    columns_.put("ryube", item.get("ryube"));

                    break;
                default:
                // No operation.
            }
            updateView();
        }
    }

    private class SelectOfficeAction implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent event) {
            OfficeSelector selector = new OfficeSelector(model_.getConnection(), (Integer) columns_.get("dis_ent_id"));
            switch (selector.showSelector(WeighListDetailsPane.this)) {
                case OfficeSelector.CLEARED:
                    columns_.put("dis_office_id", 0);
                    columns_.put("office_name", "");
                    break;
                case OfficeSelector.SELECTED:
                    Map<String, Object> item = selector.selectedRow();
                    columns_.put("dis_office_id", item.get("id"));
                    columns_.put("office_name", (String) item.get("name"));
                    break;
                default:
                // No operation.
            }
            updateView();
        }
    }

    private class SelectContAction implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent event) {
            ContSelector selector = new ContSelector(model_.getConnection());
            switch (selector.showSelector(WeighListDetailsPane.this)) {
                case ContSelector.CLEARED:
                    columns_.put("dis_cont_id", 0);
                    columns_.put("contractor_name", "");
                    break;
                case ContSelector.SELECTED:
                    Map<String, Object> item = selector.selectedRow();
                    columns_.put("dis_cont_id", item.get("id"));
                    columns_.put("contractor_name", (String) item.get("name"));
                    break;
                default:
                // No operation.
            }
            updateView();
        }
    }

    private class SelectCarKindAction implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent event) {
            CarKindSelector selector = new CarKindSelector(model_.getConnection());
            switch (selector.showSelector(WeighListDetailsPane.this)) {
                case CarKindSelector.CLEARED:
                    columns_.put("car_kind_id", 0);
                    columns_.put("car_kind_name", "");
                    break;
                case CarKindSelector.SELECTED:
                    Map<String, Object> item = selector.selectedRow();
                    columns_.put("car_kind_id", item.get("id"));
                    columns_.put("car_kind_name", (String) item.get("name"));
                    break;
                default:
                // No operation.
            }
            updateView();
        }
    }

    private void jtbtn0MousePressed(java.awt.event.ActionEvent evt) {
        if (jtbtn0.isSelected()) {

            jtbtn1.setSelected(false);
        } else {
            jtbtn0.setSelected(true);
        }
        columns_.put("class", 0);
    }

    private void jtbtn1MousePressed(java.awt.event.ActionEvent evt) {
        if (jtbtn1.isSelected()) {
            //選択されていれば黒
            jtbtn0.setSelected(false);
        } else {
            jtbtn1.setSelected(true);
        }
        columns_.put("class", 1);
    }

}
