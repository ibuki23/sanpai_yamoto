package aks.industrialwaste.recycle.ui.master.bill;

import aks.industrialwaste.database.Column;
import aks.industrialwaste.database.ColumnType;
import aks.industrialwaste.database.SortCondition;
import aks.industrialwaste.database.SortOrder;
import aks.industrialwaste.ui.DetailsPanel;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import jp.co.astax.awt.FontUtility;
import java.awt.event.KeyEvent;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;

public class BillDetailsPane extends DetailsPanel {

    private BillTableModel model_;
    private int row_;
    private Map<String, Object> columns_;
    private JTextField idField_;
    private JTextField nameField_;
    private JTextField kanaField_;
    private JTextField address1Field_;
    private JTextField address2Field_;
    private JTextField p_nameField_;
    private JTextField zip_codeField_;
    private JTextField telField_;
    private JTextField bundle_dayField_;

    private JComboBox pay_dayField_;
    private GoodsListTableModel goodsListModel_;
    private JPanel pane;
    private JComboBox tax_no_Field_;
    private JTextField id_serachField_;
    private JTextField keisyoField_;
    private JComboBox 売上区分Field_;

    private JCheckBox fixed_flag_;//定額請求フラグ
    private JTextField fixed_value_;//定額請求額

    public BillDetailsPane(BillTableModel model, int row) {
        model_ = model;
        row_ = row;
        columns_ = new HashMap<>();
        copyColumns();
    }

    @Override
    protected void initComponents() {
        super.initComponents();
        Font font14 = FontUtility.getFont("メイリオ", Font.PLAIN, 14);
        // タイトル
        titleField_.setText("請求先マスタ詳細");
        String[] tax_no_ = new String[]{"四捨五入", "切り捨て", "切り上げ"};
        String[] 売上区分 = new String[]{"現金", "掛け"};
        String[] pay_day_ = new String[]{"翌月", "翌々月", "翌々々月"};

        // 詳細 - 左側
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createLoweredBevelBorder());

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

        // 名称kanaField_
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

        // かな
        label = new JLabel("ｶﾅ", JLabel.LEFT);
        label.setFont(font14);
        addContainer(
                 panel, label,
                 0, 2, 1, 1, 0.0, 0.0,
                 GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
                 new Insets(2, 2, 2, 2), 0, 0);
        kanaField_ = new JTextField();
        kanaField_.setFont(font14);
        kanaField_.setMinimumSize(new Dimension(150, 30));
        kanaField_.setPreferredSize(new Dimension(150, 30));
        kanaField_.addFocusListener(new ColumnUpdater());
        kanaField_.addFocusListener(new java.awt.event.FocusAdapter() {
            // フォーカスが移ったときはIMEをOFFにする
            @Override
            public void focusLost(java.awt.event.FocusEvent e) {
                kanaField_.getInputContext().setCompositionEnabled(false);
                kanaField_.getInputContext().setCharacterSubsets(null);
            }

            @Override
            public void focusGained(java.awt.event.FocusEvent evt) {
                // カタカナモード
                setKatakanaMode();
            }
        });
        kanaField_.addKeyListener(new java.awt.event.KeyAdapter() {
            @Override
            public void keyPressed(java.awt.event.KeyEvent evt) {
                JTextField_KeyPressed(evt, kanaField_);
            }
        });
        addContainer(
                 panel, kanaField_,
                 1, 2, 1, 1, 0.0, 0.0,
                 GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
                 new Insets(2, 2, 2, 2), 0, 0);

// 処分業者所在地1
        label = new JLabel("業者 所在地1", JLabel.LEFT);
        label.setFont(font14);
        addContainer(
                 panel, label,
                 0, 3, 1, 1, 0.0, 0.0,
                 GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
                 new Insets(2, 2, 2, 2), 0, 0);
        address1Field_ = new JTextField();
        address1Field_.setFont(font14);
        address1Field_.setMinimumSize(new Dimension(150, 30));
        address1Field_.setPreferredSize(new Dimension(150, 30));
        address1Field_.addFocusListener(new ColumnUpdater());
        address1Field_.addKeyListener(new java.awt.event.KeyAdapter() {

            @Override
            public void keyPressed(java.awt.event.KeyEvent evt) {
                JTextField_KeyPressed(evt, address1Field_);
            }
        });
        addContainer(
                 panel, address1Field_,
                 1, 3, 1, 1, 0.0, 0.0,
                 GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
                 new Insets(2, 2, 2, 2), 0, 0);

        // 処分業者所在地2
        label = new JLabel("業者 所在地2", JLabel.LEFT);
        label.setFont(font14);
        addContainer(
                 panel, label,
                 0, 4, 1, 1, 0.0, 0.0,
                 GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
                 new Insets(2, 2, 2, 2), 0, 0);
        address2Field_ = new JTextField();
        address2Field_.setFont(font14);
        address2Field_.setMinimumSize(new Dimension(150, 30));
        address2Field_.setPreferredSize(new Dimension(150, 30));
        address2Field_.addFocusListener(new ColumnUpdater());
        address2Field_.addKeyListener(new java.awt.event.KeyAdapter() {
            @Override
            public void keyPressed(java.awt.event.KeyEvent evt) {
                JTextField_KeyPressed(evt, address2Field_);
            }
        });
        addContainer(
                 panel, address2Field_,
                 1, 4, 1, 1, 0.0, 0.0,
                 GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
                 new Insets(2, 2, 2, 2), 0, 0);

        // 敬称
        label = new JLabel("敬称", JLabel.LEFT);
        label.setFont(font14);
        addContainer(
                 panel, label,
                 2, 4, 1, 1, 0.0, 0.0,
                 GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
                 new Insets(2, 2, 2, 2), 0, 0);
        keisyoField_ = new JTextField();
        keisyoField_.setFont(font14);
        keisyoField_.setMinimumSize(new Dimension(70, 30));
        keisyoField_.setPreferredSize(new Dimension(70, 30));
        keisyoField_.addFocusListener(new ColumnUpdater());
        keisyoField_.addKeyListener(new java.awt.event.KeyAdapter() {

            @Override
            public void keyPressed(java.awt.event.KeyEvent evt) {
                JTextField_KeyPressed(evt, keisyoField_);
            }
        });
        addContainer(
                 panel, keisyoField_,
                 2, 5, 1, 1, 0.0, 0.0,
                 GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
                 new Insets(2, 2, 2, 2), 0, 0);

        //業者 正式名称
        label = new JLabel("業者 正式名称", JLabel.LEFT);
        label.setFont(font14);
        addContainer(
                 panel, label,
                 0, 5, 1, 1, 0.0, 0.0,
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
                JTextField_KeyPressed(evt, p_nameField_);
            }
        });
        addContainer(
                 panel, p_nameField_,
                 1, 5, 1, 1, 0.0, 0.0,
                 GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
                 new Insets(2, 2, 2, 2), 0, 0);

        //業者郵便番号
        label = new JLabel("業者 郵便番号", JLabel.LEFT);
        label.setFont(font14);
        addContainer(
                 panel, label,
                 0, 6, 1, 1, 0.0, 0.0,
                 GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
                 new Insets(2, 2, 2, 2), 0, 0);
        zip_codeField_ = new JTextField();
        zip_codeField_.setFont(font14);
        zip_codeField_.setMinimumSize(new Dimension(60, 30));
        zip_codeField_.setPreferredSize(new Dimension(60, 30));
        zip_codeField_.addFocusListener(new ColumnUpdater());
        zip_codeField_.addKeyListener(new java.awt.event.KeyAdapter() {
            @Override
            public void keyPressed(java.awt.event.KeyEvent evt) {
                JTextField_KeyPressed(evt, zip_codeField_);
            }
        });
        addContainer(
                 panel, zip_codeField_,
                 1, 6, 1, 1, 0.0, 0.0,
                 GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
                 new Insets(2, 2, 2, 2), 0, 0);

        //業者　TEL
        label = new JLabel("業者 TEL", JLabel.LEFT);
        label.setFont(font14);
        addContainer(
                 panel, label,
                 0, 7, 1, 1, 0.0, 0.0,
                 GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
                 new Insets(2, 2, 2, 2), 0, 0);
        telField_ = new JTextField();
        telField_.setFont(font14);
        telField_.setMinimumSize(new Dimension(150, 30));
        telField_.setPreferredSize(new Dimension(150, 30));
        telField_.addFocusListener(new ColumnUpdater());
        telField_.addKeyListener(new java.awt.event.KeyAdapter() {
            @Override
            public void keyPressed(java.awt.event.KeyEvent evt) {
                JTextField_KeyPressed(evt, telField_);
            }
        });
        addContainer(
                 panel, telField_,
                 1, 7, 1, 1, 0.0, 0.0,
                 GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
                 new Insets(2, 2, 2, 2), 0, 0);

        // 締め日
        label = new JLabel("締め日", JLabel.LEFT);
        label.setFont(font14);
        addContainer(
                 panel, label,
                 0, 8, 1, 1, 0.0, 0.0,
                 GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
                 new Insets(2, 2, 2, 2), 0, 0);
        bundle_dayField_ = new JTextField();
        bundle_dayField_.setFont(font14);
        bundle_dayField_.setMinimumSize(new Dimension(150, 30));
        bundle_dayField_.setPreferredSize(new Dimension(150, 30));
        bundle_dayField_.addFocusListener(new ColumnUpdater());
        bundle_dayField_.addKeyListener(new java.awt.event.KeyAdapter() {
            @Override
            public void keyPressed(java.awt.event.KeyEvent evt) {
                JTextField_KeyPressed(evt, bundle_dayField_);
            }
        });
        addContainer(
                 panel, bundle_dayField_,
                 1, 8, 1, 1, 0.0, 0.0,
                 GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
                 new Insets(2, 2, 2, 2), 0, 0);

        // 支払日
        label = new JLabel("入金予定区分", JLabel.LEFT);
        label.setFont(font14);
        addContainer(
                 panel, label,
                 0, 9, 1, 1, 0.0, 0.0,
                 GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
                 new Insets(2, 2, 2, 2), 0, 0);
        pay_dayField_ = new JComboBox();
        pay_dayField_.setFont(font14);
        pay_dayField_.setMinimumSize(new Dimension(150, 30));
        pay_dayField_.setPreferredSize(new Dimension(150, 30));
        pay_dayField_.addFocusListener(new ColumnUpdater());
        pay_dayField_.setModel(new javax.swing.DefaultComboBoxModel(pay_day_));
        addContainer(
                 panel, pay_dayField_,
                 1, 9, 1, 1, 0.0, 0.0,
                 GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
                 new Insets(2, 2, 2, 2), 0, 0);

        // 端数処理
        label = new JLabel("端数処理", JLabel.LEFT);
        label.setFont(font14);
        addContainer(
                 panel, label,
                 0, 10, 1, 1, 0.0, 0.0,
                 GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
                 new Insets(2, 2, 2, 2), 0, 0);
        tax_no_Field_ = new JComboBox();
        tax_no_Field_.setFont(font14);
        tax_no_Field_.setMinimumSize(new Dimension(150, 30));
        tax_no_Field_.setPreferredSize(new Dimension(150, 30));
        tax_no_Field_.addFocusListener(new ColumnUpdater());
        tax_no_Field_.setModel(new javax.swing.DefaultComboBoxModel(tax_no_));
        addContainer(
                 panel, tax_no_Field_,
                 1, 10, 1, 1, 0.0, 0.0,
                 GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
                 new Insets(2, 2, 2, 2), 0, 0);

        // 売上区分
        label = new JLabel("売上区分", JLabel.LEFT);
        label.setFont(font14);
        addContainer(
                 panel, label,
                 0, 11, 1, 1, 0.0, 0.0,
                 GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
                 new Insets(2, 2, 2, 2), 0, 0);
        売上区分Field_ = new JComboBox();
        売上区分Field_.setFont(font14);
        売上区分Field_.setMinimumSize(new Dimension(150, 30));
        売上区分Field_.setPreferredSize(new Dimension(150, 30));
        売上区分Field_.addFocusListener(new ColumnUpdater());
        売上区分Field_.setModel(new javax.swing.DefaultComboBoxModel(売上区分));
        addContainer(
                 panel, 売上区分Field_,
                 1, 11, 1, 1, 0.0, 0.0,
                 GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
                 new Insets(2, 2, 2, 2), 0, 0);

        fixed_flag_ = new JCheckBox("処分料の定額請求をする場合にはチェックしてください。");
        fixed_flag_.setFont(font14);
        fixed_flag_.addFocusListener(new ColumnUpdater());

        label = new JLabel("定額請求額", JLabel.LEFT);
        label.setFont(font14);
        fixed_value_ = new JTextField();
        fixed_value_.setFont(font14);
        fixed_value_.setMinimumSize(new Dimension(150, 30));
        fixed_value_.setPreferredSize(new Dimension(150, 30));
        fixed_value_.addFocusListener(new ColumnUpdater());
        fixed_value_.addKeyListener(new java.awt.event.KeyAdapter() {
            @Override
            public void keyPressed(java.awt.event.KeyEvent evt) {
                JTextField_KeyPressed(evt, fixed_value_);
            }
        });

        label = new JLabel("※名称は全角１５文字以内で入力してください。", JLabel.LEFT);
        label.setFont(font14);
        label.setForeground(Color.RED);
        addContainer(
                 panel, label,
                 0, 16, 3, 1, 0.0, 0.0,
                 GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
                 new Insets(2, 2, 2, 2), 0, 0);

        label = new JLabel("※締め日は5日単位での設定です。 0入力は月末とします。", JLabel.LEFT);
        label.setFont(font14);
        label.setForeground(Color.RED);
        addContainer(
                 panel, label,
                 0, 17, 3, 1, 0.0, 0.0,
                 GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
                 new Insets(2, 2, 2, 2), 0, 0);
        label = new JLabel("(例) 5、10、15、20、25、0", JLabel.LEFT);
        label.setFont(font14);
        label.setForeground(Color.RED);
        addContainer(
                 panel, label,
                 0, 18, 3, 1, 0.0, 0.0,
                 GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
                 new Insets(2, 2, 2, 2), 0, 0);

        // ダミー
        JPanel p = new JPanel();
        addContainer(
                 panel, p,
                 0, 20, 1, 1, 0.0, 1.0,
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
        pane.setMinimumSize(new Dimension(300, 600));
        pane.setPreferredSize(new Dimension(300, 600));

        label = new JLabel("空きコード検索", JLabel.LEFT);
        label.setFont(font14);
        addContainer(
                 pane, label,
                 0, 0, 1, 1, 0.0, 0.0,
                 GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
                 new Insets(2, 2, 2, 2), 0, 0);

        id_serachField_ = new JTextField("0");
        id_serachField_.setFont(font14);
        id_serachField_.setMinimumSize(new Dimension(100, 30));
        id_serachField_.setPreferredSize(new Dimension(100, 30));
        id_serachField_.addKeyListener(new java.awt.event.KeyAdapter() {
            @Override
            public void keyPressed(java.awt.event.KeyEvent evt) {
                JTextField_KeyPressed(evt, id_serachField_);
            }
        });
        addContainer(
                 pane, id_serachField_,
                 1, 0, 1, 1, 0.0, 0.0,
                 GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
                 new Insets(2, 2, 2, 2), 0, 0);

        JButton btn1 = new JButton("ID採番");
        btn1.setFont(font14);
        btn1.setFocusable(false);
        btn1.setMargin(new Insets(2, 2, 2, 2));
        btn1.setMinimumSize(new Dimension(100, 30));
        btn1.setPreferredSize(new Dimension(100, 30));
        btn1.addActionListener(new GetNextID());
        addContainer(
                 pane, btn1,
                 2, 0, 1, 1, 0.0, 0.0,
                 GridBagConstraints.WEST, GridBagConstraints.NONE,
                 new Insets(2, 2, 2, 2), 0, 0);

        label = new JLabel("(例) 2000番までで採番する場合は1999と入力してください。", JLabel.LEFT);
        label.setFont(font14);
        addContainer(
                 pane, label,
                 0, 1, 3, 1, 0.0, 0.0,
                 GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
                 new Insets(2, 2, 2, 2), 0, 0);
        label = new JLabel("　", JLabel.LEFT);
        addContainer(
                 pane, label,
                 0, 2, 3, 1, 0.0, 0.0,
                 GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
                 new Insets(2, 2, 2, 2), 0, 0);

        label = new JLabel("排出業者コード検索", JLabel.LEFT);
        label.setFont(font14);
        addContainer(
                 pane, label,
                 0, 3, 1, 1, 0.0, 0.0,
                 GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
                 new Insets(2, 2, 2, 2), 0, 0);

        JButton btn2 = new JButton("ID採番");
        btn2.setFont(font14);
        btn2.setFocusable(false);
        btn2.setMargin(new Insets(2, 2, 2, 2));
        btn2.setMinimumSize(new Dimension(100, 30));
        btn2.setPreferredSize(new Dimension(100, 30));
        btn2.addActionListener(new GetEntID());
        addContainer(
                 pane, btn2,
                 2, 3, 1, 1, 0.0, 0.0,
                 GridBagConstraints.WEST, GridBagConstraints.NONE,
                 new Insets(2, 2, 2, 2), 0, 0);

        label = new JLabel("<html>排出業者マスタの『名称』を請求先マスタの『名称』に<p>コピー・貼り付けした後で検索してください。<p>※右クリックメニューから操作可能です。</html>", JLabel.LEFT);
        label.setFont(font14);
        addContainer(
                 pane, label,
                 0, 4, 3, 1, 0.0, 0.0,
                 GridBagConstraints.NORTHEAST, GridBagConstraints.NONE,
                 new Insets(2, 2, 2, 2), 0, 0);

        addContainer(
                 pane, new JPanel(),
                 0, 5, 3, 1, 0.0, 1.0,
                 GridBagConstraints.NORTHEAST, GridBagConstraints.BOTH,
                 new Insets(2, 2, 2, 2), 0, 0);

        addContainer(
                 centerPane_, pane,
                 1, 0, 1, 1, 0.5, 0.0,
                 GridBagConstraints.EAST, GridBagConstraints.BOTH,
                 new Insets(2, 2, 2, 2), 0, 0);
        deleteButton_.setVisible(false);
    }

    @Override
    protected void doCancel() {
        copyColumns();

        super.doCancel();
    }

    @Override
    protected void doRegister() {

        try {
            //重複登録の確認
            String s = "select count(*) as cnt from v0_bill "
                     + " where v0_bill.id = ? and v0_bill.sno <> ?;";

            PreparedStatement ps = model_.getConnection().prepareStatement(s);
            try {
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
            } finally {
                ps.close();
            }
        } catch (SQLException e) {
            System.out.println(e.toString());
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
            BillJournal.recordJournal(
                     model_.getConnection(),
                     "請求先マスタ詳細 - 追加",
                     columns_);
            if (model_.insert(columns_, goodsListModel_)) {
                doClose();

                //リストの再描画
                SortCondition[] sort
                         = new SortCondition[]{
                             new SortCondition(
                                      new Column("id", "id", ColumnType.TEXT),
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
            BillJournal.recordJournal(
                     model_.getConnection(),
                     "請求先マスタ詳細 - 訂正",
                     columns_);
            if (!model_.update(columns_, goodsListModel_)) {
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
                              new Column("id", "id", ColumnType.TEXT),
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
        Object o = columns_.get("id");
        if (o instanceof Number) {
            idField_.setText(String.valueOf(o));
        }
        nameField_.setText((String) columns_.get("name"));
        keisyoField_.setText((String) columns_.get("keisyo"));
        kanaField_.setText((String) columns_.get("kana"));
        address1Field_.setText((String) columns_.get("address1"));
        address2Field_.setText((String) columns_.get("address2"));
        p_nameField_.setText((String) columns_.get("p_name"));
        zip_codeField_.setText((String) columns_.get("zip_code"));
        telField_.setText((String) columns_.get("tel"));
        o = columns_.get("bundle_day");
        if (o instanceof Number) {
            bundle_dayField_.setText(String.valueOf(o));
        }

        o = columns_.get("pay_class");
        if (o instanceof Number) {
            pay_dayField_.setSelectedIndex((Integer) o);
        }

        o = columns_.get("sno");
        if (o instanceof Number) {
            goodsListModel_
                     = new GoodsListTableModel(model_.getConnection(), ((Number) o).intValue());
        } else {
            goodsListModel_
                     = new GoodsListTableModel(model_.getConnection(), 0);
        }

        fixed_flag_.setSelected((Boolean) columns_.get("fixed_flag"));
        fixed_value_.setText(String.valueOf((Integer) columns_.get("fixed_value")));

        o = columns_.get("tax_no");
        if (o instanceof Number) {
            tax_no_Field_.setSelectedIndex((Integer) o);
        }

        o = columns_.get("売上区分");
        if (o instanceof String) {
            売上区分Field_.setSelectedItem(o);
        }

        o = columns_.get("last_update");
        if (o instanceof Calendar) {
            updateTimeField_.setValue(((Calendar) o).getTime());
        }
    }

    private void copyColumns() {
        columns_.clear();
        if (0 <= row_) {
            columns_.putAll(model_.getColumns(row_));
        } else {
            columns_.put("id", 0);
            columns_.put("name", "");
            columns_.put("address1", "");
            columns_.put("address2", "");
            columns_.put("tel", "");
            columns_.put("p_name", "");
            columns_.put("zip_code", "");
            columns_.put("kana", "");
            columns_.put("bundle_day", 0);
            columns_.put("pay_day", 0);
            columns_.put("tax_no", 0);
            //20141102
            columns_.put("keisyo", "");
            columns_.put("fixed_flag", false);
            columns_.put("fixed_value", 0);

        }
    }

    private void clearField() {
        idField_.setText("0");
        nameField_.setText("");
        kanaField_.setText("");
        address1Field_.setText("");
        address2Field_.setText("");
        p_nameField_.setText("");
        zip_codeField_.setText("");
        telField_.setText("");
        bundle_dayField_.setText("0");
        pay_dayField_.setSelectedIndex(0);
        tax_no_Field_.setSelectedIndex(0);
        売上区分Field_.setSelectedIndex(0);
        keisyoField_.setText("");
        fixed_flag_.setSelected(false);
        fixed_value_.setText("0");

    }

    private class GetNextID implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent event) {
            int id = Integer.valueOf(id_serachField_.getText());
            if (id > 0 && row_ < 0) {
                columns_.put("id", getNextID(id));
                updateView();
            }
        }
    }

    public int getNextID(int id) {
        int ret = 0;
        try {
            String s = "select max(id)as id from v0_bill where id <= ? ;";
            try (PreparedStatement ps = model_.getConnection().prepareStatement(s)) {
                ps.setObject(1, id);
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    if (rs.getInt(1) == id) {
                        ret = 0;
                    } else {
                        ret = rs.getInt(1) + 1;
                    }
                }
            }
        } catch (SQLException e) {

        }

        return ret;
    }

    private class GetEntID implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent event) {
            columns_.put("id", getEntID(nameField_.getText()));
            updateView();
        }
    }

    public int getEntID(String name) {
        int ret = 0;
        try {
            String s = String.format("select * from v0_ent where name like '%%%s%%' ;", name);
            try (PreparedStatement ps = model_.getConnection().prepareStatement(s)) {
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    ret = rs.getInt("id");
                    columns_.put("name", rs.getString("name"));
                    columns_.put("kana", rs.getString("kana"));
                    columns_.put("address1", rs.getString("address1"));
                    columns_.put("address2", rs.getString("address2"));
                    columns_.put("tel", rs.getString("tel"));
                    columns_.put("zip_code", rs.getString("zip_code"));
                    columns_.put("p_name", rs.getString("p_name"));
                }
            }
        } catch (SQLException e) {
            System.err.println(e.toString());
        }

        return ret;
    }

    private boolean checkInputItems() {
        if ("".equals(nameField_.getText().trim())) {
            JOptionPane.showMessageDialog(
                     this,
                     "請求先名を入力してください.",
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
            } else if (source == kanaField_) {
                kanaField_.selectAll();
            } else if (source == address1Field_) {
                address1Field_.selectAll();
            } else if (source == address2Field_) {
                address2Field_.selectAll();
            } else if (source == p_nameField_) {
                p_nameField_.selectAll();
            } else if (source == zip_codeField_) {
                zip_codeField_.selectAll();
            } else if (source == telField_) {
                telField_.selectAll();
            } else if (source == bundle_dayField_) {
                bundle_dayField_.selectAll();
            } else if (source == keisyoField_) {
                keisyoField_.selectAll();
            } else if (source == idField_) {
                idField_.selectAll();
            }
        }

        @Override
        public void focusLost(FocusEvent event) {
            Object source = event.getSource();
            if (source == nameField_) {
                columns_.put("name", nameField_.getText().trim());
            }
            if (source == kanaField_) {
                columns_.put("kana", kanaField_.getText().trim());
            }
            if (source == address1Field_) {
                columns_.put("address1", address1Field_.getText().trim());
            }
            if (source == address2Field_) {
                columns_.put("address2", address2Field_.getText().trim());
            }
            //keisyoField_
            if (source == keisyoField_) {
                columns_.put("keisyo", keisyoField_.getText().trim());
            }
            if (source == p_nameField_) {
                columns_.put("p_name", p_nameField_.getText().trim());
            }
            if (source == zip_codeField_) {
                columns_.put("zip_code", zip_codeField_.getText().trim());
            }
            if (source == telField_) {
                columns_.put("tel", telField_.getText().trim());
            }
            if (source == bundle_dayField_) {
                columns_.put("bundle_day", Integer.valueOf(bundle_dayField_.getText().trim()));
            }
            if (source == pay_dayField_) {
                columns_.put("pay_class", pay_dayField_.getSelectedIndex());
            }
            if (source == tax_no_Field_) {
                columns_.put("tax_no", tax_no_Field_.getSelectedIndex());
            }
            if (source == 売上区分Field_) {
                columns_.put("売上区分", 売上区分Field_.getSelectedItem());
            }
            if (source == idField_) {
                columns_.put("id", Integer.valueOf(idField_.getText()));
            }
            if (source == fixed_flag_) {
                columns_.put("fixed_flag", fixed_flag_.isSelected());
            }
            if (source == fixed_value_) {
                columns_.put("fixed_value", Integer.valueOf(fixed_value_.getText()));
            }
        }
    }

    private void JTextField_KeyPressed(java.awt.event.KeyEvent evt, JTextField JTextField_) {

        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            JTextField_.transferFocus();
            evt.consume();
        }
    }

    // カタカナモードの設定
    private void setKatakanaMode() {
        Character.Subset[] subsets = {java.awt.im.InputSubset.HALFWIDTH_KATAKANA};
        kanaField_.getInputContext().setCompositionEnabled(true);
        kanaField_.getInputContext().setCharacterSubsets(subsets);
    }
}
