package aks.industrialwaste.recycle.ui.master.goods;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import aks.swing.JTextField;
import javax.swing.JComboBox;
import jp.co.astax.awt.FontUtility;
import javax.swing.JButton;
import aks.industrialwaste.recycle.util.MethodSelector;
import aks.industrialwaste.database.Column;
import aks.industrialwaste.database.ColumnType;
import aks.industrialwaste.database.SortCondition;
import aks.industrialwaste.database.SortOrder;
import aks.industrialwaste.ui.DetailsPanel;
import java.awt.EventQueue;
import java.awt.event.KeyEvent;
import java.sql.Connection;
import java.text.DecimalFormat;
import javax.swing.JCheckBox;

/**
 * 品目マスタの詳細を表示する.
 *
 * @version 1.00
 * @author K.Kuroda
 */
public class GoodsDetailsPane extends DetailsPanel {

    public static final int CLEARED = -1;
    public static final int CANCELED = 0;
    public static final int SELECTED = 1;

    private GoodsTableModel model_;
    private int row_;
    private Map<String, Object> columns_;
    private JTextField idField_;
    private JTextField nameField_;
    private JTextField p_nameField_;
    private JComboBox carrySwitch_;
    private JComboBox carrySwitch2_;
    private Object[] carryString = new String[2];
    private JPanel pane;
    private JTextField methodField1_;
    private JTextField methodField2_;
    private Object[] unitString = new String[22];
    private JComboBox unitSwitch_;
    private JTextField gravity_Field_;
    private JTextField unitField_, unit_value_a_, unit_value_b_, unit_value_c_;
    private JTextField transUnitField_;
    private GoodsPriceEditorPanel priceEditor_;

    //20120827
    private String[] final_goods_kind_;
    private String[][] middle_goods_kind_name = new String[2][];
    public int middle_goods_kind = 0;
    public int middle_goods_kind_index = 0;
    private String[] carry_name_ = new String[]{"搬入", "搬出"};
    private JComboBox goods_kind_cmb1;
    private JComboBox goods_kind_cmb2;
    private JComboBox tax_kind_cmb;

    //20121024 add
    private JTextField bill_nameField_;
    private JTextField bill_name2Field_;

    public String[][] bclassName2Array = new String[2][];
    //選択されている行の各idを取得する
    public int b_class_id_ = 0;
    //取得したidを要素とする配列のindexを取得する(表示のため)
    public int b_class_id_index = 0;
    private JComboBox b_class_cmb_;
    private Connection con_;

    private JCheckBox f按分フラグ_;
    private JCheckBox f在庫管理フラグ_;

    public GoodsDetailsPane(Connection con, GoodsTableModel model, int row) {
        con_ = con;
        model_ = model;
        row_ = row;
        columns_ = new HashMap<>();
        copyColumns();

        b_class_id_ = (Integer) columns_.get("b_class_id");
        middle_goods_kind = (Integer) columns_.get("middle_goods_kind");
    }

    @Override
    protected void initComponents() {
        super.initComponents();
        carryString = new String[]{"中間処分", "最終処分", "販売", "請求関係", "その他"};
        unitString = new String[]{"", "ton", "kg", "m3", "ℓ", "台", "枚", "個", "日", "回", "本", "箱", "着", "冊", "ﾀﾞｰｽ", "ｾｯﾄ", "ｹｰｽ", "g", "cm", "式", "足", "袋"};
        final_goods_kind_ = new String[]{"", "産業廃棄物", "特別管理産業廃棄物", "一般廃棄物"};

        //20121016 add
        carry_name_ = new String[]{"搬入", "搬出"};

        Font font14 = FontUtility.getFont("メイリオ", Font.PLAIN, 14);
        // タイトル
        titleField_.setText("品目マスタ詳細");

        // 詳細 - 左側
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createLoweredBevelBorder());
        panel.setMinimumSize(new Dimension(600, 700));
        panel.setPreferredSize(new Dimension(600, 700));
        // ID
        JLabel label = new JLabel("ID", JLabel.LEFT);
        label.setFont(font14);
        label.setForeground(Color.RED);
        addContainer(
                 panel, label,
                 0, 0, 1, 1, 0.1, 0.0,
                 GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
                 new Insets(2, 2, 2, 2), 0, 0);
        idField_ = new JTextField();
        idField_.setFont(font14);
        idField_.addFocusListener(new ColumnUpdater());
        idField_.setMinimumSize(new Dimension(150, 30));
        idField_.setPreferredSize(new Dimension(150, 30));
        idField_.addKeyListener(new java.awt.event.KeyAdapter() {
            @Override
            public void keyPressed(java.awt.event.KeyEvent evt) {
                JTextField_KeyPressed(evt, idField_);
            }
        });
        addContainer(
                 panel, idField_,
                 1, 0, 1, 1, 0.9, 0.0,
                 GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
                 new Insets(2, 2, 2, 2), 0, 0);
        // 名称
        label = new JLabel("名称", JLabel.LEFT);
        label.setFont(font14);
        label.setForeground(Color.RED);
        addContainer(
                 panel, label,
                 0, 1, 1, 1, 0.0, 0.0,
                 GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
                 new Insets(2, 2, 2, 2), 0, 0);
        nameField_ = new JTextField();
        nameField_.setFont(font14);
        nameField_.setMinimumSize(new Dimension(150, 30));
        nameField_.setPreferredSize(new Dimension(150, 30));
        nameField_.addFocusListener(new ColumnUpdater());
        nameField_.addKeyListener(new java.awt.event.KeyAdapter() {
            @Override
            public void keyPressed(java.awt.event.KeyEvent evt) {
                JTextField_KeyPressed(evt, nameField_);
            }
        });
        EventQueue.invokeLater(new Runnable() {

            @Override
            public void run() {
                nameField_.requestFocusInWindow();
            }
        });
        addContainer(
                 panel, nameField_,
                 1, 1, 1, 1, 0.0, 0.0,
                 GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
                 new Insets(2, 2, 2, 2), 0, 0);

        // 正式名称
        label = new JLabel("正式名称", JLabel.LEFT);
        label.setFont(font14);
        addContainer(
                 panel, label,
                 0, 2, 1, 1, 0.0, 0.0,
                 GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
                 new Insets(2, 2, 2, 2), 0, 0);
        p_nameField_ = new JTextField();
        p_nameField_.setFont(font14);
        p_nameField_.setMinimumSize(new Dimension(150, 30));
        p_nameField_.setPreferredSize(new Dimension(150, 30));
        p_nameField_.addFocusListener(new ColumnUpdater());
        p_nameField_.addKeyListener(new java.awt.event.KeyAdapter() {
            @Override
            public void keyPressed(java.awt.event.KeyEvent evt) {
                Last_Field_KeyPressed(evt);
            }
        });
        addContainer(
                 panel, p_nameField_,
                 1, 2, 1, 1, 0.0, 0.0,
                 GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
                 new Insets(2, 2, 2, 2), 0, 0);

        //取扱区分　中間/最終
        label = new JLabel("取扱区分", JLabel.LEFT);
        label.setFont(font14);
        addContainer(
                 panel, label,
                 0, 3, 1, 1, 0.0, 0.0,
                 GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
                 new Insets(2, 2, 2, 2), 0, 0);
        carrySwitch_ = new JComboBox(carryString);
        carrySwitch_.setFont(font14);
        carrySwitch_.setMinimumSize(new Dimension(150, 30));
        carrySwitch_.setPreferredSize(new Dimension(150, 30));
        carrySwitch_.addFocusListener(new ColumnUpdater());

        addContainer(
                 panel, carrySwitch_,
                 1, 3, 1, 1, 0.0, 0.0,
                 GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
                 new Insets(2, 2, 2, 2), 0, 0);

        //搬入・搬出
        label = new JLabel("搬入/搬出", JLabel.LEFT);
        label.setFont(font14);
        addContainer(
                 panel, label,
                 0, 4, 1, 1, 0.0, 0.0,
                 GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
                 new Insets(2, 2, 2, 2), 0, 0);
        carrySwitch2_ = new JComboBox(carry_name_);
        carrySwitch2_.setFont(font14);
        carrySwitch2_.setMinimumSize(new Dimension(150, 30));
        carrySwitch2_.setPreferredSize(new Dimension(150, 30));
        carrySwitch2_.addFocusListener(new ColumnUpdater());

        addContainer(
                 panel, carrySwitch2_,
                 1, 4, 1, 1, 0.0, 0.0,
                 GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
                 new Insets(2, 2, 2, 2), 0, 0);

        // 処分方法 建廃
        label = new JLabel("マニフェスト(建廃)", JLabel.LEFT);
        label.setFont(font14);
        addContainer(
                 panel, label,
                 0, 5, 1, 1, 0.0, 0.0,
                 GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
                 new Insets(2, 2, 2, 2), 0, 0);
        methodField2_ = new JTextField();
        methodField2_.setFont(font14);
        methodField2_.setFocusable(false);
        methodField2_.setMinimumSize(new Dimension(150, 30));
        methodField2_.setPreferredSize(new Dimension(150, 30));
        methodField2_.addFocusListener(new ColumnUpdater());
        addContainer(
                 panel, methodField2_,
                 1, 5, 1, 1, 0.0, 0.0,
                 GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
                 new Insets(2, 2, 2, 2), 0, 0);
        // 選択
        JButton b = new JButton("選択");
        b.setFont(font14);
        b.setFocusable(false);
        b.setMargin(new Insets(0, 0, 0, 0));
        b.setMinimumSize(new Dimension(70, 30));
        b.setPreferredSize(new Dimension(70, 30));
        b.addActionListener(new SelectMethodAction2());
        addContainer(
                 panel, b,
                 2, 5, 1, 1, 0.0, 0.0,
                 GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
                 new Insets(2, 2, 2, 2), 0, 0);
        // 処分方法 産廃
        label = new JLabel("マニフェスト(産廃)", JLabel.LEFT);
        label.setFont(font14);
        addContainer(
                 panel, label,
                 0, 6, 1, 1, 0.0, 0.0,
                 GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
                 new Insets(2, 2, 2, 2), 0, 0);
        methodField1_ = new JTextField();
        methodField1_.setFont(font14);
        methodField1_.setFocusable(false);
        methodField1_.setMinimumSize(new Dimension(150, 30));
        methodField1_.setPreferredSize(new Dimension(150, 30));
        methodField1_.addFocusListener(new ColumnUpdater());

        addContainer(
                 panel, methodField1_,
                 1, 6, 1, 1, 0.0, 0.0,
                 GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
                 new Insets(2, 2, 2, 2), 0, 0);
        // 選択
        b = new JButton("選択");
        b.setFont(font14);
        b.setFocusable(false);
        b.setMargin(new Insets(0, 0, 0, 0));
        b.setMinimumSize(new Dimension(70, 30));
        b.setPreferredSize(new Dimension(70, 30));
        b.addActionListener(new SelectMethodAction());
        addContainer(
                 panel, b,
                 2, 6, 1, 1, 0.0, 0.0,
                 GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
                 new Insets(2, 2, 2, 2), 0, 0);

        //単位
        label = new JLabel("単位", JLabel.LEFT);
        label.setFont(font14);
        addContainer(
                 panel, label,
                 0, 7, 1, 1, 0.0, 0.0,
                 GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
                 new Insets(2, 2, 2, 2), 0, 0);
        unitSwitch_ = new JComboBox(unitString);
        unitSwitch_.setFont(font14);
        unitSwitch_.setMinimumSize(new Dimension(150, 30));
        unitSwitch_.setPreferredSize(new Dimension(150, 30));
        unitSwitch_.addFocusListener(new ColumnUpdater());
        addContainer(
                 panel, unitSwitch_,
                 1, 7, 1, 1, 0.0, 0.0,
                 GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
                 new Insets(2, 2, 2, 2), 0, 0);

        // 行政報告区分
        label = new JLabel("行政報告区分1", JLabel.LEFT);
        label.setFont(font14);
        addContainer(
                 panel, label,
                 0, 8, 1, 1, 0.0, 0.0,
                 GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
                 new Insets(2, 2, 2, 2), 0, 0);
        goods_kind_cmb1 = new JComboBox(final_goods_kind_);
        goods_kind_cmb1.setFont(font14);
        goods_kind_cmb1.setMinimumSize(new Dimension(150, 30));
        goods_kind_cmb1.setPreferredSize(new Dimension(150, 30));
        goods_kind_cmb1.addFocusListener(new ColumnUpdater());

        addContainer(
                 panel, goods_kind_cmb1,
                 1, 8, 1, 1, 0.0, 0.0,
                 GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
                 new Insets(2, 2, 2, 2), 0, 0);

        // 行政報告区分
        label = new JLabel("行政報告区分2", JLabel.LEFT);
        label.setFont(font14);
        addContainer(
                 panel, label,
                 0, 9, 1, 1, 0.0, 0.0,
                 GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
                 new Insets(2, 2, 2, 2), 0, 0);
        goods_kind_cmb2 = new JComboBox(final_goods_kind_);
        goods_kind_cmb2.setFont(font14);
        goods_kind_cmb2.setMinimumSize(new Dimension(150, 30));
        goods_kind_cmb2.setPreferredSize(new Dimension(150, 30));
        goods_kind_cmb2.addFocusListener(new ColumnUpdater());

        addContainer(
                 panel, goods_kind_cmb2,
                 1, 9, 1, 1, 0.0, 0.0,
                 GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
                 new Insets(2, 2, 2, 2), 0, 0);
        addContainer(
                 panel, new JPanel(),
                 1, 10, 1, 1, 0.0, 0.0,
                 GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
                 new Insets(2, 2, 2, 2), 0, 0);

        // 請求分類
        label = new JLabel("請求分類", JLabel.LEFT);
        label.setFont(font14);
        label.setForeground(Color.RED);
        addContainer(
                 panel, label,
                 0, 11, 1, 1, 0.0, 0.0,
                 GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
                 new Insets(2, 2, 2, 2), 0, 0);
        b_class_cmb_ = new JComboBox(final_goods_kind_);
        b_class_cmb_.setFont(font14);
        b_class_cmb_.setMinimumSize(new Dimension(150, 30));
        b_class_cmb_.setPreferredSize(new Dimension(150, 30));
        b_class_cmb_.addFocusListener(new ColumnUpdater());

        addContainer(
                 panel, b_class_cmb_,
                 1, 11, 1, 1, 0.0, 0.0,
                 GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
                 new Insets(2, 2, 2, 2), 0, 0);

        //単位
        label = new JLabel("請求書品名１", JLabel.LEFT);
        label.setFont(font14);
        label.setForeground(Color.RED);
        addContainer(
                 panel, label,
                 0, 12, 1, 1, 0.0, 0.0,
                 GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
                 new Insets(2, 2, 2, 2), 0, 0);
        bill_nameField_ = new JTextField();
        bill_nameField_.setFont(font14);
        bill_nameField_.setMinimumSize(new Dimension(150, 30));
        bill_nameField_.setPreferredSize(new Dimension(150, 30));
        bill_nameField_.addFocusListener(new ColumnUpdater());
        addContainer(
                 panel, bill_nameField_,
                 1, 12, 1, 1, 0.0, 0.0,
                 GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
                 new Insets(2, 2, 2, 2), 0, 0);

        //単位
        label = new JLabel("請求書品名２", JLabel.LEFT);
        label.setFont(font14);
        bill_name2Field_ = new JTextField();
        bill_name2Field_.setFont(font14);
        bill_name2Field_.setMinimumSize(new Dimension(150, 30));
        bill_name2Field_.setPreferredSize(new Dimension(150, 30));
        bill_name2Field_.addFocusListener(new ColumnUpdater());

        // 単価
        label = new JLabel("請求単価(標準)整数のみ", JLabel.LEFT);
        label.setFont(font14);
        addContainer(
                 panel, label,
                 0, 13, 1, 1, 0.0, 0.0,
                 GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
                 new Insets(2, 2, 2, 2), 0, 0);
        unitField_ = new JTextField();
        unitField_.setFont(font14);
        unitField_.setMinimumSize(new Dimension(150, 30));
        unitField_.setPreferredSize(new Dimension(150, 30));
        unitField_.addFocusListener(new ColumnUpdater());
        addContainer(
                 panel, unitField_,
                 1, 13, 1, 1, 0.0, 0.0,
                 GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
                 new Insets(2, 2, 2, 2), 0, 0);

        // 旧A/B/C欄は互換用データ保持のため残す（画面には表示しない）
        unit_value_a_ = new JTextField();
        unit_value_b_ = new JTextField();
        unit_value_c_ = new JTextField();

        // 運搬単価
        label = new JLabel("運搬単価", JLabel.LEFT);
        label.setFont(font14);
        transUnitField_ = new JTextField();
        transUnitField_.setFont(font14);
        transUnitField_.setMinimumSize(new Dimension(150, 30));
        transUnitField_.setPreferredSize(new Dimension(150, 30));
        transUnitField_.addFocusListener(new ColumnUpdater());
        //比重係数
        label = new JLabel("比重係数", JLabel.LEFT);
        label.setFont(font14);
        addContainer(
                 panel, label,
                 0, 18, 1, 1, 0.0, 0.0,
                 GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
                 new Insets(2, 2, 2, 2), 0, 0);
        gravity_Field_ = new JTextField();
        gravity_Field_.setFont(font14);
        gravity_Field_.setMinimumSize(new Dimension(150, 30));
        gravity_Field_.setPreferredSize(new Dimension(150, 30));
        gravity_Field_.addFocusListener(new ColumnUpdater());
        gravity_Field_.addKeyListener(new java.awt.event.KeyAdapter() {
            @Override
            public void keyPressed(java.awt.event.KeyEvent evt) {
                Last_Field_KeyPressed(evt);
            }
        });
        addContainer(
                 panel, gravity_Field_,
                 1, 18, 1, 1, 0.0, 0.0,
                 GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
                 new Insets(2, 2, 2, 2), 0, 0);

        f按分フラグ_ = new JCheckBox("混載便の場合に按分する場合にはチェックしてください。");
        f按分フラグ_.setFont(font14);
        f按分フラグ_.addFocusListener(new ColumnUpdater());
        addContainer(
                 panel, f按分フラグ_,
                 0, 20, 3, 1, 0.0, 0.0,
                 GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
                 new Insets(2, 2, 2, 2), 0, 0);

        label = new JLabel("※名称は全角１５文字以内で入力してください。", JLabel.LEFT);
        label.setFont(font14);
        label.setForeground(Color.RED);
        addContainer(
                 panel, label,
                 0, 21, 3, 1, 0.0, 0.0,
                 GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
                 new Insets(2, 2, 2, 2), 0, 0);

        label = new JLabel("（計量表印刷時に正常に印字されません。）", JLabel.LEFT);
        label.setFont(font14);
        label.setForeground(Color.RED);
        addContainer(
                 panel, label,
                 0, 22, 3, 1, 0.0, 0.0,
                 GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
                 new Insets(2, 2, 2, 2), 0, 0);

        label = new JLabel("※契約確認機能を使用する場合は、請求単価を設定してください。", JLabel.LEFT);
        label.setFont(font14);
        label.setForeground(Color.RED);
        addContainer(
                 panel, label,
                 0, 23, 3, 1, 0.0, 0.0,
                 GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
                 new Insets(2, 2, 2, 2), 0, 0);

        f在庫管理フラグ_ = new JCheckBox("在庫確認機能を使用する場合にはチェックしてください。");
        f在庫管理フラグ_.setFont(font14);
        f在庫管理フラグ_.addFocusListener(new ColumnUpdater());

        // ダミー
        addContainer(
                 panel, new JPanel(),
                 0, 30, 1, 1, 0.0, 1.0,
                 GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                 new Insets(2, 2, 2, 2), 0, 0);
        addContainer(
                 centerPane_, panel,
                 0, 0, 1, 1, 0.5, 1.0,
                 GridBagConstraints.WEST, GridBagConstraints.BOTH,
                 new Insets(2, 2, 2, 2), 0, 0);

        // 詳細 - 右側
        pane = new JPanel(new GridBagLayout());
        pane.setBorder(BorderFactory.createLoweredBevelBorder());
        pane.setMinimumSize(new Dimension(600, 700));
        pane.setPreferredSize(new Dimension(600, 700));
        addContainer(
                 centerPane_, pane,
                 1, 0, 1, 1, 0.5, 0.0,
                 GridBagConstraints.EAST, GridBagConstraints.BOTH,
                 new Insets(2, 2, 2, 2), 0, 0);

        priceEditor_ = new GoodsPriceEditorPanel();
        addContainer(
                 pane, priceEditor_,
                 0, 0, 1, 1, 1.0, 0.0,
                 GridBagConstraints.NORTH, GridBagConstraints.HORIZONTAL,
                 new Insets(8, 8, 8, 8), 0, 0);

        // 削除ボタンは使用しない.
        deleteButton_.setVisible(false);
    }

    @Override
    protected void doCancel() {
        copyColumns();

        super.doCancel();
    }

    @Override
    protected void doRegister() {

        // 料金区分別単価は登録ボタン押下時に確実にMapへ反映する。
        priceEditor_.saveTo(columns_);

        try {
            //重複登録の確認
            String s = "select count(*) as cnt from v0_goods_kind "
                     + " where v0_goods_kind.id = ? and v0_goods_kind.sno <> ?;";
            try (PreparedStatement ps = model_.getConnection().prepareStatement(s)) {
                ps.setObject(1, columns_.get("id"));
                ps.setObject(2, columns_.get("sno"));

                ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                    if (rs.getInt("cnt") > 0) {

                        JOptionPane.showMessageDialog(
                                 this,
                                 "IDが重複しています。",
                                 "警告",
                                 JOptionPane.WARNING_MESSAGE);
                        return;
                    }

                }
            }
        } catch (SQLException e) {
            System.out.println(e.toString());
        }

        //nameの文字数を確認する(全角15文字まで)
        if (columns_.get("name").toString().length() > 15) {
            JOptionPane.showMessageDialog(
                     this,
                     "名称は全角15文字まで入力できます。",
                     "確認",
                     JOptionPane.WARNING_MESSAGE);
            return;
        }

        int result = JOptionPane.showConfirmDialog(
                 this,
                 "このデータを登録します. よろしいですか?",
                 "確認",
                 JOptionPane.YES_NO_OPTION);
        if (JOptionPane.YES_OPTION != result) {
            return;
        }
        if (!checkInputItems()) {
            return;
        }

        if (0 > row_) {
            GoodsJournal.recordJournal(
                     model_.getConnection(),
                     "種類マスタ詳細 - 追加",
                     columns_);
            if (model_.insert(columns_)) {
                doClose();

                //リストの再描画
                SortCondition[] sort
                         = new SortCondition[]{
                             new SortCondition(
                                      new Column("sno", "sno", ColumnType.INT4),
                                      SortOrder.ASC)};
                model_.refresh(null, sort);

                return;

            } else {
                JOptionPane.showMessageDialog(
                         this,
                         "登録に失敗しました.",
                         "警告",
                         JOptionPane.WARNING_MESSAGE);
                return;
            }
        } else {
            GoodsJournal.recordJournal(
                     model_.getConnection(),
                     "種類マスタ詳細 - 訂正",
                     columns_);
            if (!model_.update(columns_)) {
                JOptionPane.showMessageDialog(
                         this,
                         "登録に失敗しました.",
                         "警告",
                         JOptionPane.WARNING_MESSAGE);
                return;
            }
        }
        doClose();

        //リストの再描画
        SortCondition[] sort
                 = new SortCondition[]{
                     new SortCondition(
                              new Column("sno", "sno", ColumnType.INT4),
                              SortOrder.ASC)};
        model_.refresh(null, sort);

    }

    @Override
    protected void doMovePrevious() {
        if ((null != model_) && (0 < row_)) {
            row_--;
            copyColumns();
            updateView();
        }
    }

    @Override
    protected void doMoveNext() {
        if ((null != model_) && ((model_.getRowCount() - 1) > row_)) {
            row_++;
            copyColumns();
            updateView();
        }
    }

    @Override
    public void updateView() {
        clearField();
        DecimalFormat exFormat1 = new DecimalFormat("###,###.##");

        // ID
        Object o = columns_.get("id");
        if (o instanceof Number) {
            idField_.setText(String.valueOf(o));
        }
        nameField_.setText((String) columns_.get("name"));
        p_nameField_.setText((String) columns_.get("official_name"));
        methodField1_.setText((String) columns_.get("method_name1"));
        methodField2_.setText((String) columns_.get("method_name2"));

        carrySwitch_.setSelectedIndex((Integer) columns_.get("option1"));
        carrySwitch2_.setSelectedIndex((Integer) columns_.get("carry_class"));
        unitSwitch_.setSelectedIndex((Integer) columns_.get("unit_no"));

        o = columns_.get("unit_value");
        if (o instanceof Number) {
            unitField_.setText(String.valueOf(o));
        }
        o = columns_.get("unit_value_a");
        if (o instanceof Number) {
            unit_value_a_.setText(String.valueOf(o));
        }
        o = columns_.get("unit_value_b");
        if (o instanceof Number) {
            unit_value_b_.setText(String.valueOf(o));
        }
        o = columns_.get("unit_value_c");
        if (o instanceof Number) {
            unit_value_c_.setText(String.valueOf(o));
        }

        priceEditor_.loadFrom(columns_);

        o = columns_.get("trans_unit_value");
        if (o instanceof Number) {
            transUnitField_.setText(String.valueOf(o));
        }

        o = columns_.get("last_update");
        if (o instanceof Calendar) {
            updateTimeField_.setValue(((Calendar) o).getTime());
        }

        //20120827
        goods_kind_cmb1.setSelectedIndex((Integer) columns_.get("middle_goods_kind"));
        goods_kind_cmb2.setSelectedIndex((Integer) columns_.get("final_goods_kind"));

        //20121024 add
        b_class_cmb_.setSelectedIndex((Integer) columns_.get("b_class_id"));
        bill_nameField_.setText((String) columns_.get("bill_name"));
        bill_name2Field_.setText((String) columns_.get("bill_name2"));
        gravity_Field_.setText(exFormat1.format((Double) columns_.get("ryube")));

        o = columns_.get("f按分フラグ");
        if (o instanceof Boolean) {
            f按分フラグ_.setSelected((Boolean) o);
        }
        //f有効
        o = columns_.get("f有効");
        if (o instanceof Boolean) {
            f在庫管理フラグ_.setSelected((Boolean) o);
        }

    }

    private void copyColumns() {
        columns_.clear();
        if (0 <= row_) {
            columns_.putAll(model_.getColumns(row_));
        } else {
            columns_.put("id", 0);
            columns_.put("option1", 0);
            columns_.put("name", "");
            columns_.put("official_name", "");
            columns_.put("method_id1", 0);
            columns_.put("method_id2", 0);

            columns_.put("unit_no", 0);
            columns_.put("unit_value", 0);
            columns_.put("unit_value_new", 0);
            columns_.put("unit_value_existing", 0);
            columns_.put("unit_value_other", 0);
            columns_.put("asbestos_unit_value_new", 0);
            columns_.put("asbestos_unit_value_existing", 0);
            columns_.put("asbestos_unit_value_other", 0);
            columns_.put("unit_value_a", 0);
            columns_.put("unit_value_b", 0);
            columns_.put("unit_value_c", 0);
            columns_.put("trans_unit_value", 0);

            //20120822 add
            columns_.put("middle_goods_kind", 0);
            columns_.put("final_goods_kind", 0);
            columns_.put("tax_kind", 0);
            columns_.put("carry_class", 0);

            //20121024 add
            columns_.put("b_class_id", 0);
            columns_.put("bill_name", "");
            columns_.put("bill_name2", "");
            columns_.put("ryube", 0.0);

            //f按分フラグ
            columns_.put("f按分フラグ", false);
            columns_.put("f有効", false);
        }
    }

    private void clearField() {
        idField_.setText("0");
        nameField_.setText("");
        p_nameField_.setText("");
        carrySwitch_.setSelectedIndex(0);
        unitSwitch_.setSelectedIndex(0);
        unitField_.setText("0");
        unit_value_a_.setText("0");
        unit_value_b_.setText("0");
        unit_value_c_.setText("0");
        priceEditor_.clear();
        transUnitField_.setText("0");
        //20121024 add
        bill_nameField_.setText("");
        bill_name2Field_.setText("");
        gravity_Field_.setText("0");

        middle_goods_kind_name = GetMasterData2("v0_manifesto_2", middle_goods_kind_name);
        String[] middle_goods_kind_indexName = new String[middle_goods_kind_name[0].length];
        for (int i = 0; i <= middle_goods_kind_name[0].length - 1; i++) {
            middle_goods_kind_indexName[i] = (String) middle_goods_kind_name[1][i];
            if (middle_goods_kind == Integer.valueOf((String) middle_goods_kind_name[0][i])) {
                middle_goods_kind_index = i;
            }
        }
        goods_kind_cmb1.setModel(new javax.swing.DefaultComboBoxModel(middle_goods_kind_indexName));

        //マスタ問い合わせ
        //請求分類
        bclassName2Array = GetMasterData("v0_m_b_class", bclassName2Array);
        String[] bclassName = new String[bclassName2Array[0].length];
        for (int i = 0; i <= bclassName2Array[0].length - 1; i++) {
            bclassName[i] = (String) bclassName2Array[1][i];
            if (b_class_id_ == Integer.valueOf((String) bclassName2Array[0][i])) {
                b_class_id_index = i;
            }
        }
        b_class_cmb_.setModel(new javax.swing.DefaultComboBoxModel(bclassName));

        f按分フラグ_.setSelected(false);
        f在庫管理フラグ_.setSelected(false);

    }

    private boolean checkInputItems() {
        if ("".equals(nameField_.getText().trim())) {
            JOptionPane.showMessageDialog(
                     this,
                     "種類名を入力してください.",
                     "警告",
                     JOptionPane.WARNING_MESSAGE);
            return false;
        }

        return true;
    }

    private class ColumnUpdater implements FocusListener {

        @Override
        public void focusGained(FocusEvent event) {
            Object source = event.getSource();
            if (source == nameField_) {
                nameField_.selectAll();
            } else if (source == p_nameField_) {
                p_nameField_.selectAll();
            } else if (source == idField_) {
                idField_.selectAll();
            } else if (source == unitField_) {
                unitField_.selectAll();
            } else if (source == bill_nameField_) {
                bill_name2Field_.selectAll();
            } else if (source == bill_name2Field_) {
                bill_name2Field_.selectAll();
            } else if (source == transUnitField_) {
                transUnitField_.selectAll();
            } else if (source == gravity_Field_) {
                gravity_Field_.selectAll();
            } else if (source == unit_value_a_) {
                unit_value_a_.selectAll();
            } else if (source == unit_value_b_) {
                unit_value_b_.selectAll();
            } else if (source == unit_value_c_) {
                unit_value_c_.selectAll();
            }

        }

        @Override
        public void focusLost(FocusEvent event) {
            Object source = event.getSource();
            if (source == nameField_) {
                columns_.put("name", nameField_.getText().trim());
            }
            if (source == p_nameField_) {
                columns_.put("official_name", p_nameField_.getText().trim());
            }
            if (source == carrySwitch_) {
                columns_.put("option1", carrySwitch_.getSelectedIndex());
            }
            if (source == idField_) {
                columns_.put("id", Integer.valueOf(idField_.getText()));
            }
            if (source == unitSwitch_) {
                columns_.put("unit_no", unitSwitch_.getSelectedIndex());
            }
            if (source == unitField_ && !"".equals(unitField_.getText())) {
                columns_.put("unit_value", Integer.valueOf(unitField_.getText()));
            }
            if (source == transUnitField_ && !"".equals(transUnitField_.getText())) {
                columns_.put("trans_unit_value", Integer.valueOf(transUnitField_.getText()));
            }
            //20120827 add
            if (source == goods_kind_cmb1) {
                columns_.put("middle_goods_kind", goods_kind_cmb1.getSelectedIndex());
            }
            if (source == goods_kind_cmb2) {
                columns_.put("final_goods_kind", goods_kind_cmb2.getSelectedIndex());
            }
            if (source == tax_kind_cmb) {
                columns_.put("tax_kind", tax_kind_cmb.getSelectedIndex());
            }
            if (source == carrySwitch2_) {
                columns_.put("carry_class", carrySwitch2_.getSelectedIndex());
            }
            if (source == b_class_cmb_) {
                columns_.put("b_class_id", b_class_cmb_.getSelectedIndex());
            }
            if (source == bill_nameField_) {
                columns_.put("bill_name", bill_nameField_.getText());
            }
            if (source == bill_name2Field_) {
                columns_.put("bill_name2", bill_name2Field_.getText());
            }
            if (source == gravity_Field_ && !"".equals(gravity_Field_.getText())) {
                columns_.put("ryube", Math.round(Double.valueOf(gravity_Field_.getText()) * 100D) / 100D);
            }
            if (source == unit_value_a_ && !"".equals(unit_value_a_.getText())) {
                columns_.put("unit_value_a", Integer.valueOf(unit_value_a_.getText()));
            }
            if (source == unit_value_b_ && !"".equals(unit_value_b_.getText())) {
                columns_.put("unit_value_b", Integer.valueOf(unit_value_b_.getText()));
            }
            if (source == unit_value_c_ && !"".equals(unit_value_c_.getText())) {
                columns_.put("unit_value_c", Integer.valueOf(unit_value_c_.getText()));
            }
            if (source == f按分フラグ_) {
                columns_.put("f按分フラグ", f按分フラグ_.isSelected());
            }
            if (source == f在庫管理フラグ_) {
                columns_.put("f有効", f在庫管理フラグ_.isSelected());
            }
        }
    }

    private class SelectMethodAction implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent event) {
            MethodSelector selector = new MethodSelector(model_.getConnection());
            switch (selector.showSelector(GoodsDetailsPane.this)) {
                case MethodSelector.CLEARED:
                    columns_.put("method_id1", 0);
                    columns_.put("method_name1", "");
                    break;
                case MethodSelector.SELECTED:
                    Map<String, Object> item = selector.selectedRow();
                    columns_.put("method_id1", item.get("id"));
                    columns_.put("method_name1", (String) item.get("name"));
                    break;
                default:
                // No operation.
            }
            updateView();
        }
    }

    private class SelectMethodAction2 implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent event) {
            MethodSelector selector = new MethodSelector(model_.getConnection());
            switch (selector.showSelector(GoodsDetailsPane.this)) {
                case MethodSelector.CLEARED:
                    columns_.put("method_id2", 0);
                    columns_.put("method_name2", "");
                    break;
                case MethodSelector.SELECTED:
                    Map<String, Object> item = selector.selectedRow();
                    columns_.put("method_id2", item.get("id"));
                    columns_.put("method_name2", (String) item.get("name"));
                    break;
                default:
                // No operation.
            }
            updateView();
        }
    }

    private void JTextField_KeyPressed(java.awt.event.KeyEvent evt, JTextField JTextField_) {

        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            JTextField_.transferFocus();
            evt.consume();
        }
    }

    private void Last_Field_KeyPressed(java.awt.event.KeyEvent evt) {
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            registerButton_.requestFocus();
        }
    }

    private String[][] GetMasterData(String table_name, String[][] ms) {
        String sql = "";

        try {
            int i = 0;
            int cnt = 0;
            String s = String.format("select count(*) as cnt from %s where delete_flag=false", table_name);
            PreparedStatement ps = con_.prepareStatement(s);

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
            ps = model_.getConnection().prepareStatement(sql);

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

    private String[][] GetMasterData2(String table_name, String[][] ms) {
        String sql = "";

        try {
            int i = 0;
            int cnt = 0;
            String s = String.format("select count(*) as cnt from %s where delete_flag=false", table_name);
            PreparedStatement ps = con_.prepareStatement(s);

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
            sql = String.format("select sno, remark_name from %s where delete_flag=false order by sno;",
                     table_name);
            ps = model_.getConnection().prepareStatement(sql);

            try {
                ResultSet rs = ps.executeQuery();

                while (rs.next()) {
                    ms[0][i] = String.valueOf((Integer) rs.getInt("sno"));
                    ms[1][i] = rs.getString("remark_name");
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
}
