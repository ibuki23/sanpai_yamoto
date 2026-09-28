package aks.industrialwaste.recycle.ui.master.agreement;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import aks.industrialwaste.recycle.util.BillSelector;
import aks.industrialwaste.recycle.util.ContSelector;
import aks.industrialwaste.recycle.util.EntSelector;
import aks.industrialwaste.recycle.util.GoodsSelector;
import aks.industrialwaste.recycle.util.TransSelector;
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
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import aks.swing.JTextField;
import aks.industrialwaste.database.Column;
import aks.industrialwaste.database.ColumnType;
import aks.industrialwaste.database.CombineOperator;
import aks.industrialwaste.database.CompareOperator;
import aks.industrialwaste.database.DBManager;
import aks.industrialwaste.database.SearchCondition;
import aks.industrialwaste.database.SortCondition;
import aks.industrialwaste.database.SortOrder;
import aks.industrialwaste.ui.DetailsPanel;
import aks.industrialwaste.recycle.ui.master.agreement.Agreement;
import aks.swing.JTextFieldHankaku;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import jp.co.astax.awt.FontUtility;
import java.awt.event.KeyEvent;
import java.sql.Date;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import jp.co.astax.swing.DateTimeField;

public class AgreementDetailsPane extends DetailsPanel {

    private AgreementTableModel model_;
    private AgreementUnitTableModel unit_model_;
    private JTable unitDataTable_;
    private int row_;
    private Map<String, Object> columns_, goods_columns_;
    public static JTextField idField_;
    private JTextField nameField_;
    private JTextField kanaField_;
    private JTextField address1Field_;
    private JTextField address2Field_;
    private JTextField p_nameField_;
    private JTextField zip_codeField_;
    private JTextField telField_;
    private JPanel pane;
    private JCheckBox finish_flagField_;
    private JTextField ent_nameField_;
    private JTextFieldHankaku[] goodsUnitField_;
    private JCheckBox auto_update_flag_;
    private DateTimeField contract_start_date_, contract_end_date_, contract_day_;
    private List<SearchCondition> searchCondition_;
    private List<SortCondition> sortCondition_;
    private JButton btn1, btn2;
    private JTextField transField01_, transField02_, transField03_, transField04_, transField05_;
    private JTextField contField01_, contField02_, contField03_, contField04_;
    private JTextField 備考Field_, 交付担当者Field_, 契約書ファイルパスField_, billField01_, 交付担当者所属Field_, 契約番号Field_;
    private int mode_;

    public AgreementDetailsPane(AgreementTableModel model, int row, int mode) {
        model_ = model;
        row_ = row;
        // 0:追加・訂正 1:複写
        mode_ = mode;
        unit_model_ = new AgreementUnitTableModel(model_.getConnection());
        unitDataTable_.setModel(unit_model_);
        unitDataTable_.setColumnModel(new AgreementUnitTableColumnModel());
        unitDataTable_.setRowHeight(30);
        unitDataTable_.setColumnSelectionAllowed(false);
        unitDataTable_.setRowSelectionAllowed(true);
        unitDataTable_.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        unitDataTable_.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        unitDataTable_.getTableHeader().setReorderingAllowed(false);

        columns_ = new HashMap<>();
        goods_columns_ = new HashMap<>();
        searchCondition_
                 = Collections.synchronizedList(new ArrayList<SearchCondition>());
        sortCondition_
                 = Collections.synchronizedList(new ArrayList<SortCondition>());

        copyColumns();
    }

    @Override
    protected void initComponents() {
        super.initComponents();
        Font font14 = FontUtility.getFont("メイリオ", Font.PLAIN, 14);
        titleField_.setText("契約マスタ詳細");
        // 詳細 - 左側
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createLoweredBevelBorder());
        panel.setMinimumSize(new Dimension(550, 750));
        panel.setPreferredSize(new Dimension(550, 750));

        // 排出業者名
        JLabel label = new JLabel("排出業者", JLabel.LEFT);
        label.setFont(font14);
        label.setForeground(Color.RED);
        addContainer(
                 panel, label,
                 0, 0, 1, 1, 0.1, 0.0,
                 GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
                 new Insets(2, 2, 0, 2), 0, 0);
        ent_nameField_ = new JTextField();
        ent_nameField_.setFont(font14);
        ent_nameField_.setEditable(false);
        ent_nameField_.setFocusable(false);
        ent_nameField_.addFocusListener(new ColumnUpdater());
        ent_nameField_.setMinimumSize(new Dimension(150, 30));
        ent_nameField_.setPreferredSize(new Dimension(150, 30));
        ent_nameField_.addKeyListener(new java.awt.event.KeyAdapter() {
            @Override
            public void keyPressed(java.awt.event.KeyEvent evt) {
                JTextField_KeyPressed(evt, ent_nameField_);
            }
        });
        addContainer(
                 panel, ent_nameField_,
                 1, 0, 1, 1, 0.9, 0.0,
                 GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
                 new Insets(2, 2, 0, 2), 0, 0);
        btn1 = new JButton("選択");
        btn1.setFont(font14);
        btn1.setFocusable(false);
        btn1.setMargin(new Insets(2, 2, 0, 2));
        btn1.setMinimumSize(new Dimension(100, 30));
        btn1.setPreferredSize(new Dimension(100, 30));
        btn1.addActionListener(new SelectEntAction());
        addContainer(
                 panel, btn1,
                 2, 0, 1, 1, 0.0, 0.0,
                 GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
                 new Insets(2, 2, 0, 2), 0, 0);

        // ID
        label = new JLabel("ID", JLabel.LEFT);
        label.setFont(font14);
        label.setForeground(Color.RED);
        addContainer(
                 panel, label,
                 0, 1, 1, 1, 0.1, 0.0,
                 GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
                 new Insets(2, 2, 0, 2), 0, 0);
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
                 1, 1, 1, 1, 0.9, 0.0,
                 GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
                 new Insets(2, 2, 0, 2), 0, 0);
        btn2 = new JButton("ID採番");
        btn2.setFont(font14);
        btn2.setFocusable(false);
        btn2.setMargin(new Insets(2, 0, 2, 0));
        btn2.setMinimumSize(new Dimension(100, 30));
        btn2.setPreferredSize(new Dimension(100, 30));
        btn2.addActionListener(new GetNextID());
        addContainer(
                 panel, btn2,
                 2, 1, 1, 1, 0.0, 0.0,
                 GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
                 new Insets(2, 2, 0, 2), 0, 0);

        // 名称
        label = new JLabel("現場名称", JLabel.LEFT);
        label.setFont(font14);
        label.setForeground(Color.RED);
        addContainer(
                 panel, label,
                 0, 2, 1, 1, 0.0, 0.0,
                 GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
                 new Insets(2, 2, 0, 2), 0, 0);
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
                 1, 2, 1, 1, 0.0, 0.0,
                 GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
                 new Insets(2, 2, 0, 2), 0, 0);

        // かな
        label = new JLabel("ｶﾅ", JLabel.LEFT);
        label.setFont(font14);
        addContainer(
                 panel, label,
                 0, 3, 1, 1, 0.0, 0.0,
                 GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
                 new Insets(2, 2, 0, 2), 0, 0);
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
                 1, 3, 1, 1, 0.0, 0.0,
                 GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
                 new Insets(2, 2, 0, 2), 0, 0);

        // 所在地1
        label = new JLabel("所在地", JLabel.LEFT);
        label.setFont(font14);
        addContainer(
                 panel, label,
                 0, 4, 1, 1, 0.0, 0.0,
                 GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
                 new Insets(2, 2, 0, 2), 0, 0);
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
                 1, 4, 1, 1, 0.0, 0.0,
                 GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
                 new Insets(2, 2, 0, 2), 0, 0);

//     所在地2
        label = new JLabel("所在地2", JLabel.LEFT);
        label.setFont(font14);
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
        // 正式名称
        label = new JLabel("正式名称", JLabel.LEFT);
        label.setFont(font14);
        addContainer(
                 panel, label,
                 0, 6, 1, 1, 0.0, 0.0,
                 GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
                 new Insets(2, 2, 0, 2), 0, 0);
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
                 1, 6, 1, 1, 0.0, 0.0,
                 GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
                 new Insets(2, 2, 0, 2), 0, 0);

        // 郵便番号
        JPanel zip_panel = new JPanel(new GridBagLayout());
        label = new JLabel("郵便番号", JLabel.LEFT);
        label.setFont(font14);
        addContainer(
                 panel, label,
                 0, 7, 1, 1, 0.0, 0.0,
                 GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
                 new Insets(2, 2, 0, 2), 0, 0);
        zip_codeField_ = new JTextField();
        zip_codeField_.setFont(font14);
        zip_codeField_.setMinimumSize(new Dimension(150, 30));
        zip_codeField_.setPreferredSize(new Dimension(150, 30));
        zip_codeField_.addFocusListener(new ColumnUpdater());
        zip_codeField_.addKeyListener(new java.awt.event.KeyAdapter() {
            @Override
            public void keyPressed(java.awt.event.KeyEvent evt) {
                JTextField_KeyPressed(evt, zip_codeField_);
            }
        });
        addContainer(
                 zip_panel, zip_codeField_,
                 1, 0, 1, 1, 0.0, 0.0,
                 GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
                 new Insets(2, 2, 0, 2), 0, 0);

        // TEL
        label = new JLabel("TEL", JLabel.LEFT);
        label.setFont(font14);
        addContainer(
                 zip_panel, label,
                 2, 0, 1, 1, 0.0, 0.0,
                 GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
                 new Insets(2, 2, 0, 2), 0, 0);
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
                 zip_panel, telField_,
                 3, 0, 1, 1, 1.0, 0.0,
                 GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
                 new Insets(2, 2, 0, 2), 0, 0);
        addContainer(
                 panel, zip_panel,
                 1, 7, 1, 1, 0.0, 0.0,
                 GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
                 new Insets(2, 2, 0, 2), 0, 0);
        // 備考
        label = new JLabel("備考", JLabel.LEFT);
        label.setFont(font14);
        addContainer(
                 panel, label,
                 0, 8, 1, 1, 0.0, 0.0,
                 GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
                 new Insets(2, 2, 0, 2), 0, 0);
        備考Field_ = new JTextField();
        備考Field_.setFont(font14);
        備考Field_.setMinimumSize(new Dimension(150, 30));
        備考Field_.setPreferredSize(new Dimension(150, 30));
        備考Field_.addFocusListener(new ColumnUpdater());
        備考Field_.addKeyListener(new java.awt.event.KeyAdapter() {
            @Override
            public void keyPressed(java.awt.event.KeyEvent evt) {
                JTextField_KeyPressed(evt, 備考Field_);
            }
        });
        addContainer(
                 panel, 備考Field_,
                 1, 8, 1, 1, 0.0, 0.0,
                 GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
                 new Insets(2, 2, 0, 2), 0, 0);

        // 現場終了チェック
        label = new JLabel("現場終了", JLabel.LEFT);
        label.setFont(font14);
        label.setForeground(Color.RED);
        addContainer(
                 panel, label,
                 0, 9, 1, 1, 0.0, 0.0,
                 GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
                 new Insets(2, 2, 0, 2), 0, 0);
        finish_flagField_ = new JCheckBox("※チェックを入れると検索リストに表示されません。");
        finish_flagField_.setFont(font14);
        finish_flagField_.setMinimumSize(new Dimension(150, 30));
        finish_flagField_.setPreferredSize(new Dimension(150, 30));
        finish_flagField_.addFocusListener(new ColumnUpdater());
        addContainer(
                 panel, finish_flagField_,
                 1, 9, 1, 1, 0.0, 0.0,
                 GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
                 new Insets(2, 2, 0, 2), 0, 0);

        // 交付担当者名
        label = new JLabel("交付担当者", JLabel.LEFT);
        label.setFont(font14);
        addContainer(
                 panel, label,
                 0, 10, 1, 1, 0.0, 0.0,
                 GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
                 new Insets(2, 2, 0, 2), 0, 0);
        //交付担当者Field_
        交付担当者Field_ = new JTextField();
        交付担当者Field_.setFont(font14);
        交付担当者Field_.setMinimumSize(new Dimension(150, 30));
        交付担当者Field_.setPreferredSize(new Dimension(150, 30));
        交付担当者Field_.addFocusListener(new ColumnUpdater());
        交付担当者Field_.addKeyListener(new java.awt.event.KeyAdapter() {
            @Override
            public void keyPressed(java.awt.event.KeyEvent evt) {
                JTextField_KeyPressed(evt, 交付担当者Field_);
            }
        });
        addContainer(
                 panel, 交付担当者Field_,
                 1, 10, 1, 1, 0.0, 0.0,
                 GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
                 new Insets(2, 2, 0, 2), 0, 0);

        // 交付担当者　所属　20170124 追加
        label = new JLabel("交付担当者　所属", JLabel.LEFT);
        label.setFont(font14);
        addContainer(
                 panel, label,
                 0, 11, 1, 1, 0.0, 0.0,
                 GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
                 new Insets(2, 2, 0, 2), 0, 0);
        //交付担当者所属
        交付担当者所属Field_ = new JTextField();
        交付担当者所属Field_.setFont(font14);
        交付担当者所属Field_.setMinimumSize(new Dimension(150, 30));
        交付担当者所属Field_.setPreferredSize(new Dimension(150, 30));
        交付担当者所属Field_.addFocusListener(new ColumnUpdater());
        交付担当者所属Field_.addKeyListener(new java.awt.event.KeyAdapter() {
            @Override
            public void keyPressed(java.awt.event.KeyEvent evt) {
                Last_Field_KeyPressed(evt);
            }
        });
        addContainer(
                 panel, 交付担当者所属Field_,
                 1, 11, 1, 1, 0.0, 0.0,
                 GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
                 new Insets(2, 2, 0, 2), 0, 0);

        label = new JLabel("※名称は全角１８文字以内で入力してください。計量表印刷時に正常に印字されません。", JLabel.LEFT);
        label.setFont(font14);
        label.setForeground(Color.RED);
        addContainer(
                 panel, label,
                 0, 12, 3, 1, 0.0, 0.0,
                 GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
                 new Insets(2, 2, 0, 2), 0, 0);

        // 契約日
        label = new JLabel("契約日", JLabel.LEFT);
        label.setFont(font14);
        addContainer(
                 panel, label,
                 0, 14, 1, 1, 0.0, 0.0,
                 GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
                 new Insets(2, 2, 0, 2), 0, 0);
        contract_day_ = new DateTimeField(
                 DateTimeField.DateFormat.YMD);
        contract_day_.setFont(font14);
        contract_day_.setMinimumSize(new Dimension(260, 35));
        contract_day_.setPreferredSize(new Dimension(260, 35));
        contract_day_.addFocusListener(new ColumnUpdater());
        addContainer(
                 panel, contract_day_,
                 1, 14, 1, 1, 0.0, 0.0,
                 GridBagConstraints.WEST, GridBagConstraints.NONE,
                 new Insets(2, 2, 0, 2), 0, 0);

        label = new JLabel("契約番号", JLabel.LEFT);
        label.setFont(font14);
        addContainer(
                 panel, label,
                 0, 15, 1, 1, 0.0, 0.0,
                 GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
                 new Insets(2, 2, 0, 2), 0, 0);
        //契約番号
        契約番号Field_ = new JTextField();
        契約番号Field_.setFont(font14);
        契約番号Field_.setMinimumSize(new Dimension(150, 30));
        契約番号Field_.setPreferredSize(new Dimension(150, 30));
        契約番号Field_.addFocusListener(new ColumnUpdater());
        契約番号Field_.addKeyListener(new java.awt.event.KeyAdapter() {
            @Override
            public void keyPressed(java.awt.event.KeyEvent evt) {
                Last_Field_KeyPressed(evt);
            }
        });
        addContainer(
                 panel, 契約番号Field_,
                 1, 15, 1, 1, 0.0, 0.0,
                 GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
                 new Insets(2, 2, 0, 2), 0, 0);

        // 契約自動更新チェック
        label = new JLabel("契約自動更新", JLabel.LEFT);
        label.setFont(font14);
        addContainer(
                 panel, label,
                 0, 16, 1, 1, 0.0, 0.0,
                 GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
                 new Insets(2, 2, 0, 2), 0, 0);
        auto_update_flag_ = new JCheckBox("契約期間の判定を無視します。");
        auto_update_flag_.setFont(font14);
        auto_update_flag_.setMinimumSize(new Dimension(150, 30));
        auto_update_flag_.setPreferredSize(new Dimension(150, 30));
        auto_update_flag_.addFocusListener(new ColumnUpdater());
        addContainer(
                 panel, auto_update_flag_,
                 1, 16, 1, 1, 0.0, 0.0,
                 GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
                 new Insets(2, 2, 0, 2), 0, 0);

        // 契約開始日
        label = new JLabel("契約開始日", JLabel.LEFT);
        label.setFont(font14);
        addContainer(
                 panel, label,
                 0, 17, 1, 1, 0.0, 0.0,
                 GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
                 new Insets(2, 2, 0, 2), 0, 0);
        contract_start_date_ = new DateTimeField(
                 DateTimeField.DateFormat.YMD);
        contract_start_date_.setFont(font14);
        contract_start_date_.setMinimumSize(new Dimension(260, 35));
        contract_start_date_.setPreferredSize(new Dimension(260, 35));
        contract_start_date_.addFocusListener(new ColumnUpdater());
        addContainer(
                 panel, contract_start_date_,
                 1, 17, 1, 1, 0.0, 0.0,
                 GridBagConstraints.WEST, GridBagConstraints.NONE,
                 new Insets(2, 2, 0, 2), 0, 0);

        // 契約終了日
        label = new JLabel("契約終了日", JLabel.LEFT);
        label.setFont(font14);
        addContainer(
                 panel, label,
                 0, 18, 1, 1, 0.0, 0.0,
                 GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
                 new Insets(2, 2, 0, 2), 0, 0);
        contract_end_date_ = new DateTimeField(
                 DateTimeField.DateFormat.YMD);
        contract_end_date_.setFont(font14);
        contract_end_date_.setMinimumSize(new Dimension(260, 35));
        contract_end_date_.setPreferredSize(new Dimension(260, 35));
        contract_end_date_.addFocusListener(new ColumnUpdater());
        addContainer(
                 panel, contract_end_date_,
                 1, 18, 1, 1, 0.0, 0.0,
                 GridBagConstraints.WEST, GridBagConstraints.NONE,
                 new Insets(2, 2, 0, 2), 0, 0);

        // 契約運搬業者１～５
        label = new JLabel("契約運搬業者", JLabel.LEFT);
        label.setFont(font14);
        addContainer(
                 panel, label,
                 0, 20, 1, 1, 0.0, 0.0,
                 GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
                 new Insets(2, 2, 0, 2), 0, 0);
        transField01_ = new JTextField();
        transField01_.setFont(font14);
        transField01_.setEditable(false);
        transField01_.setFocusable(false);
        transField01_.setMinimumSize(new Dimension(150, 30));
        transField01_.setPreferredSize(new Dimension(150, 30));
        addContainer(
                 panel, transField01_,
                 1, 20, 1, 1, 0.0, 0.0,
                 GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
                 new Insets(2, 2, 0, 2), 0, 0);

        JButton btn = new JButton("選択");
        btn.setFont(font14);
        btn.setFocusable(false);
        btn.setMargin(new Insets(2, 2, 0, 2));
        btn.setMinimumSize(new Dimension(70, 30));
        btn.setPreferredSize(new Dimension(70, 30));
        btn.addActionListener(new SelecTransAction(1));
        addContainer(
                 panel, btn,
                 2, 20, 1, 1, 0.0, 0.0,
                 GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
                 new Insets(2, 2, 0, 2), 0, 0);

        transField02_ = new JTextField();
        transField02_.setFont(font14);
        transField02_.setEditable(false);
        transField02_.setFocusable(false);
        transField02_.setMinimumSize(new Dimension(150, 30));
        transField02_.setPreferredSize(new Dimension(150, 30));
        addContainer(
                 panel, transField02_,
                 1, 21, 1, 1, 0.0, 0.0,
                 GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
                 new Insets(2, 2, 0, 2), 0, 0);

        btn = new JButton("選択");
        btn.setFont(font14);
        btn.setFocusable(false);
        btn.setMargin(new Insets(2, 2, 0, 2));
        btn.setMinimumSize(new Dimension(70, 30));
        btn.setPreferredSize(new Dimension(70, 30));
        btn.addActionListener(new SelecTransAction(2));
        addContainer(
                 panel, btn,
                 2, 21, 1, 1, 0.0, 0.0,
                 GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
                 new Insets(2, 2, 0, 2), 0, 0);

        transField03_ = new JTextField();
        transField03_.setFont(font14);
        transField03_.setEditable(false);
        transField03_.setFocusable(false);
        transField03_.setMinimumSize(new Dimension(150, 30));
        transField03_.setPreferredSize(new Dimension(150, 30));
        addContainer(
                 panel, transField03_,
                 1, 22, 1, 1, 0.0, 0.0,
                 GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
                 new Insets(2, 2, 0, 2), 0, 0);

        btn = new JButton("選択");
        btn.setFont(font14);
        btn.setFocusable(false);
        btn.setMargin(new Insets(2, 2, 0, 2));
        btn.setMinimumSize(new Dimension(70, 30));
        btn.setPreferredSize(new Dimension(70, 30));
        btn.addActionListener(new SelecTransAction(3));
        addContainer(
                 panel, btn,
                 2, 22, 1, 1, 0.0, 0.0,
                 GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
                 new Insets(2, 2, 0, 2), 0, 0);

        transField04_ = new JTextField();
        transField04_.setFont(font14);
        transField04_.setEditable(false);
        transField04_.setFocusable(false);
        transField04_.setMinimumSize(new Dimension(150, 30));
        transField04_.setPreferredSize(new Dimension(150, 30));

        btn = new JButton("選択");
        btn.setFont(font14);
        btn.setFocusable(false);
        btn.setMargin(new Insets(2, 2, 0, 2));
        btn.setMinimumSize(new Dimension(70, 30));
        btn.setPreferredSize(new Dimension(70, 30));
        btn.addActionListener(new SelecTransAction(4));

        transField05_ = new JTextField();
        transField05_.setFont(font14);
        transField05_.setEditable(false);
        transField05_.setFocusable(false);
        transField05_.setMinimumSize(new Dimension(150, 30));
        transField05_.setPreferredSize(new Dimension(150, 30));

        btn = new JButton("選択");
        btn.setFont(font14);
        btn.setFocusable(false);
        btn.setMargin(new Insets(2, 2, 0, 2));
        btn.setMinimumSize(new Dimension(70, 30));
        btn.setPreferredSize(new Dimension(70, 30));
        btn.addActionListener(new SelecTransAction(5));

        // 契約処分業者１～４
        label = new JLabel("契約処分業者", JLabel.LEFT);
        label.setFont(font14);
        addContainer(
                 panel, label,
                 0, 25, 1, 1, 0.0, 0.0,
                 GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
                 new Insets(2, 2, 0, 2), 0, 0);
        contField01_ = new JTextField();
        contField01_.setFont(font14);
        contField01_.setEditable(false);
        contField01_.setFocusable(false);
        contField01_.setMinimumSize(new Dimension(150, 30));
        contField01_.setPreferredSize(new Dimension(150, 30));
        addContainer(
                 panel, contField01_,
                 1, 25, 1, 1, 0.0, 0.0,
                 GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
                 new Insets(2, 2, 0, 2), 0, 0);

        btn = new JButton("選択");
        btn.setFont(font14);
        btn.setFocusable(false);
        btn.setMargin(new Insets(2, 2, 0, 2));
        btn.setMinimumSize(new Dimension(70, 30));
        btn.setPreferredSize(new Dimension(70, 30));
        btn.addActionListener(new SelecContAction(1));
        addContainer(
                 panel, btn,
                 2, 25, 1, 1, 0.0, 0.0,
                 GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
                 new Insets(2, 2, 0, 2), 0, 0);

        contField02_ = new JTextField();
        contField02_.setFont(font14);
        contField02_.setEditable(false);
        contField02_.setFocusable(false);
        contField02_.setMinimumSize(new Dimension(150, 30));
        contField02_.setPreferredSize(new Dimension(150, 30));
        addContainer(
                 panel, contField02_,
                 1, 26, 1, 1, 0.0, 0.0,
                 GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
                 new Insets(2, 2, 0, 2), 0, 0);

        btn = new JButton("選択");
        btn.setFont(font14);
        btn.setFocusable(false);
        btn.setMargin(new Insets(2, 2, 0, 2));
        btn.setMinimumSize(new Dimension(70, 30));
        btn.setPreferredSize(new Dimension(70, 30));
        btn.addActionListener(new SelecContAction(2));
        addContainer(
                 panel, btn,
                 2, 26, 1, 1, 0.0, 0.0,
                 GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
                 new Insets(2, 2, 0, 2), 0, 0);

        contField03_ = new JTextField();
        contField03_.setFont(font14);
        contField03_.setEditable(false);
        contField03_.setFocusable(false);
        contField03_.setMinimumSize(new Dimension(150, 30));
        contField03_.setPreferredSize(new Dimension(150, 30));
        addContainer(
                 panel, contField03_,
                 1, 27, 1, 1, 0.0, 0.0,
                 GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
                 new Insets(2, 2, 0, 2), 0, 0);

        btn = new JButton("選択");
        btn.setFont(font14);
        btn.setFocusable(false);
        btn.setMargin(new Insets(2, 2, 0, 2));
        btn.setMinimumSize(new Dimension(70, 30));
        btn.setPreferredSize(new Dimension(70, 30));
        btn.addActionListener(new SelecContAction(3));
        addContainer(
                 panel, btn,
                 2, 27, 1, 1, 0.0, 0.0,
                 GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
                 new Insets(2, 2, 0, 2), 0, 0);

        contField04_ = new JTextField();
        contField04_.setFont(font14);
        contField04_.setEditable(false);
        contField04_.setFocusable(false);
        contField04_.setMinimumSize(new Dimension(150, 30));
        contField04_.setPreferredSize(new Dimension(150, 30));

        btn = new JButton("選択");
        btn.setFont(font14);
        btn.setFocusable(false);
        btn.setMargin(new Insets(2, 2, 0, 2));
        btn.setMinimumSize(new Dimension(70, 30));
        btn.setPreferredSize(new Dimension(70, 30));
        btn.addActionListener(new SelecContAction(4));

        //billField01_
        //契約の請求先
        label = new JLabel("請求先", JLabel.LEFT);
        label.setFont(font14);
        addContainer(
                 panel, label,
                 0, 29, 1, 1, 0.0, 0.0,
                 GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
                 new Insets(2, 2, 0, 2), 0, 0);
        billField01_ = new JTextField();
        billField01_.setFont(font14);
        billField01_.setEditable(false);
        billField01_.setFocusable(false);
        billField01_.setMinimumSize(new Dimension(150, 30));
        billField01_.setPreferredSize(new Dimension(150, 30));
        addContainer(
                 panel, billField01_,
                 1, 29, 1, 1, 0.0, 0.0,
                 GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
                 new Insets(2, 2, 0, 2), 0, 0);

        btn = new JButton("選択");
        btn.setFont(font14);
        btn.setFocusable(false);
        btn.setMargin(new Insets(2, 2, 0, 2));
        btn.setMinimumSize(new Dimension(70, 30));
        btn.setPreferredSize(new Dimension(70, 30));
        btn.addActionListener(new SelectBillAction());
        addContainer(
                 panel, btn,
                 2, 29, 1, 1, 0.0, 0.0,
                 GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
                 new Insets(2, 2, 0, 2), 0, 0);

        //契約書ファイルパスField_
        label = new JLabel("委託契約書", JLabel.LEFT);
        label.setFont(font14);
        addContainer(
                 panel, label,
                 0, 30, 1, 1, 0.0, 0.0,
                 GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
                 new Insets(2, 2, 0, 2), 0, 0);
        契約書ファイルパスField_ = new JTextField();
        契約書ファイルパスField_.setFont(font14);
        契約書ファイルパスField_.setEditable(false);
        契約書ファイルパスField_.setFocusable(false);
        契約書ファイルパスField_.setMinimumSize(new Dimension(150, 30));
        契約書ファイルパスField_.setPreferredSize(new Dimension(150, 30));
        addContainer(
                 panel, 契約書ファイルパスField_,
                 1, 30, 1, 1, 0.0, 0.0,
                 GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
                 new Insets(2, 2, 0, 2), 0, 0);

        btn = new JButton("選択");
        btn.setFont(font14);
        btn.setFocusable(false);
        btn.setMargin(new Insets(2, 2, 0, 2));
        btn.setMinimumSize(new Dimension(70, 30));
        btn.setPreferredSize(new Dimension(70, 30));
        btn.addActionListener(new Select契約書Action());
        addContainer(
                 panel, btn,
                 2, 30, 1, 1, 0.0, 0.0,
                 GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
                 new Insets(2, 2, 0, 2), 0, 0);

        // ダミー
        addContainer(
                 panel, new JPanel(),
                 0, 35, 1, 1, 0.0, 1.0,
                 GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                 new Insets(2, 2, 0, 2), 0, 0);
        addContainer(
                 centerPane_, panel,
                 0, 0, 1, 1, 0.5, 1.0,
                 GridBagConstraints.WEST, GridBagConstraints.BOTH,
                 new Insets(2, 2, 0, 2), 0, 0);

        // 詳細 - 右側
        goodsUnitField_ = new JTextFieldHankaku[40];
        unitDataTable_ = new JTable();
        unitDataTable_.setFont(font14);
        unitDataTable_.getTableHeader().setFont(font14);

        pane = new JPanel(new GridBagLayout());
        pane.setBorder(BorderFactory.createLoweredBevelBorder());
        pane.setMinimumSize(new Dimension(650, 750));
        pane.setPreferredSize(new Dimension(650, 750));

        pane.add(
                 new JScrollPane(unitDataTable_),
                 new GridBagConstraints(
                          0, 0, 1, 1, 1.0, 1.0,
                          GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                          new Insets(5, 5, 5, 5), 0, 0));

        JLabel l10 = new JLabel("契約外の品目は単価設定をゼロにしてください。");
        l10.setFont(font14);
        l10.setForeground(Color.RED);

        JPanel btn_pane1 = new JPanel(new GridBagLayout());

        btn_pane1.setMinimumSize(new Dimension(600, 35));
        btn_pane1.setPreferredSize(new Dimension(600, 35));

        JButton b1 = new JButton("標準単価ｾｯﾄ");
        b1.setFont(font14);
        b1.setFocusable(false);
        b1.setMargin(new Insets(2, 2, 0, 2));
        b1.setMinimumSize(new Dimension(150, 30));
        b1.setPreferredSize(new Dimension(150, 30));
        b1.addActionListener(new SelectGoodsAction());

        addContainer(
                 btn_pane1, b1,
                 2, 0, 1, 1, 1.0, 1.0,
                 GridBagConstraints.EAST, GridBagConstraints.NONE,
                 new Insets(2, 2, 2, 2), 0, 0);

        b1 = new JButton("行追加");
        b1.setFont(font14);
        b1.setFocusable(false);
        b1.setMargin(new Insets(2, 2, 0, 2));
        b1.setMinimumSize(new Dimension(150, 30));
        b1.setPreferredSize(new Dimension(150, 30));
        b1.addActionListener(new AddGoodsAction());

        addContainer(
                 btn_pane1, b1,
                 0, 0, 1, 1, 0.0, 0.0,
                 GridBagConstraints.EAST, GridBagConstraints.NONE,
                 new Insets(2, 2, 2, 2), 0, 0);

        b1 = new JButton("行削除");
        b1.setFont(font14);
        b1.setFocusable(false);
        b1.setMargin(new Insets(2, 2, 0, 2));
        b1.setMinimumSize(new Dimension(150, 30));
        b1.setPreferredSize(new Dimension(150, 30));
        b1.addActionListener(new DeleteGoodsAction());

        addContainer(
                 btn_pane1, b1,
                 1, 0, 1, 1, 0.0, 0.0,
                 GridBagConstraints.EAST, GridBagConstraints.NONE,
                 new Insets(2, 2, 2, 2), 0, 0);
        addContainer(
                 pane, btn_pane1,
                 0, 2, 2, 1, 0.0, 0.0,
                 GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
                 new Insets(2, 2, 0, 2), 0, 0);

        addContainer(
                 centerPane_, pane,
                 1, 0, 1, 1, 0.5, 0.0,
                 GridBagConstraints.EAST, GridBagConstraints.BOTH,
                 new Insets(2, 2, 0, 2), 0, 0);

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

        try {
            //重複登録の確認
            String s = "select count(*) as cnt from v0_office "
                     + " where v0_office.id = ? and v0_office.ent_id = ? and v0_office.sno <> ?;";

            PreparedStatement ps = model_.getConnection().prepareStatement(s);
            try {
                ps.setObject(1, columns_.get("id"));
                ps.setObject(2, columns_.get("ent_id"));
                ps.setObject(3, columns_.get("sno"));

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

        //商品単価をコピー
        columns_.putAll(goods_columns_);

        if (0 > row_) {
            AgreementJournal.recordJournal(
                     model_.getConnection(),
                     "契約マスタ詳細 - 追加",
                     columns_);

            if (0 == Integer.valueOf(idField_.getText())) {
                JOptionPane.showMessageDialog(
                         null,
                         "ID 0は追加できません。",
                         "警告",
                         JOptionPane.WARNING_MESSAGE);
                return;
            }

            //mode_　0:追加・訂正　1:複写
            if (model_.insert(columns_)) {
                //単価テーブル追加
                unit_model_.delete((Integer) columns_.get("ent_id"), (Integer) columns_.get("id"));
                unit_model_.insert();

                doClose();

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
            if (mode_ == 0) {
                AgreementJournal.recordJournal(
                         model_.getConnection(),
                         "契約マスタ詳細 - 訂正",
                         columns_);
                if (model_.update(columns_)) {
                    //単価テーブル追加
                    unit_model_.delete((Integer) columns_.get("ent_id"), (Integer) columns_.get("id"));
                    unit_model_.insert();
                } else {
                    JOptionPane.showMessageDialog(
                             this,
                             "登録に失敗しました.",
                             "警告",
                             JOptionPane.WARNING_MESSAGE);
                    return;
                }
            } else {
                AgreementJournal.recordJournal(
                         model_.getConnection(),
                         "契約マスタ詳細 - 複写",
                         columns_);
                if (model_.insert(columns_)) {
                    //単価テーブル追加
                    unit_model_.delete((Integer) columns_.get("ent_id"), (Integer) columns_.get("id"));
                    unit_model_.insert();
                } else {
                    JOptionPane.showMessageDialog(
                             this,
                             "登録に失敗しました.",
                             "警告",
                             JOptionPane.WARNING_MESSAGE);
                    return;
                }
            }
        }
        doClose();

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
        if (mode_ == 1) {
            titleField_.setText("契約マスタ詳細 - 複写");
        } else {
            titleField_.setText("契約マスタ詳細");
        }

        //排出業者コードが入力されていれば押下可能とする
        btn2.setEnabled(((Integer) columns_.get("ent_id") > 0 && row_ < 0) || ((Integer) columns_.get("ent_id") > 0 && mode_ == 1));

        nameField_.setText((String) columns_.get("name"));
        kanaField_.setText((String) columns_.get("kana"));
        ent_nameField_.setText((String) columns_.get("ent_name"));
        address1Field_.setText((String) columns_.get("address1"));
        address2Field_.setText((String) columns_.get("address2"));
        p_nameField_.setText((String) columns_.get("p_name"));
        zip_codeField_.setText((String) columns_.get("zip_code"));
        telField_.setText((String) columns_.get("tel"));
        備考Field_.setText((String) columns_.get("remark"));
        交付担当者Field_.setText((String) columns_.get("tantou"));
        交付担当者所属Field_.setText((String) columns_.get("tantou_syozoku"));
        finish_flagField_.setSelected((Boolean) columns_.get("finish_flag"));

        Calendar c1 = Calendar.getInstance();
        Calendar c2 = Calendar.getInstance();
        Calendar c0 = Calendar.getInstance();
        if (columns_.get("contract_start_date") != null) {
            c1.setTime((Date) columns_.get("contract_start_date"));
            contract_start_date_.setValue(c1);
        }
        if (columns_.get("contract_end_date") != null) {
            c2.setTime((Date) columns_.get("contract_end_date"));
            contract_end_date_.setValue(c2);
        }
        if (columns_.get("contract_day") != null) {
            c0.setTime((Date) columns_.get("contract_day"));
            contract_day_.setValue(c0);
        }

        transField01_.setText((String) columns_.get("trans_name01"));
        transField02_.setText((String) columns_.get("trans_name02"));
        transField03_.setText((String) columns_.get("trans_name03"));
        transField04_.setText((String) columns_.get("trans_name04"));
        transField05_.setText((String) columns_.get("trans_name05"));

        contField01_.setText((String) columns_.get("cont_name01"));
        contField02_.setText((String) columns_.get("cont_name02"));
        contField03_.setText((String) columns_.get("cont_name03"));
        contField04_.setText((String) columns_.get("cont_name04"));

        billField01_.setText((String) columns_.get("bill_name"));

        契約書ファイルパスField_.setText((String) columns_.get("keiyakusyo_path"));
        契約番号Field_.setText((String) columns_.get("keiyaku_no"));

        //auto_update_flag
        o = columns_.get("auto_update_flag");
        if (o instanceof Boolean) {
            auto_update_flag_.setSelected((Boolean) o);
        }

        o = columns_.get("last_update");
        if (o instanceof Calendar) {
            updateTimeField_.setValue(((Calendar) o).getTime());
        }
    }

    private void copyColumns() {

        try {
            SearchCondition[] search
                     = new SearchCondition[]{
                         new SearchCondition(
                                  new Column("ent_id", "ent_id", ColumnType.INT4),
                                  CompareOperator.EQ,
                                  (Integer) model_.getValueAt(row_, "ent_id"),
                                  CombineOperator.AND),
                         new SearchCondition(
                                  new Column("office_id", "office_id", ColumnType.INT4),
                                  CompareOperator.EQ,
                                  (Integer) model_.getValueAt(row_, "id"),
                                  CombineOperator.NONE)};
            if (0 < searchCondition_.size()) {
                search = searchCondition_.toArray(new SearchCondition[0]);
            }
            SortCondition[] sort
                     = new SortCondition[]{
                         new SortCondition(
                                  new Column("goods_id", "goods_id", ColumnType.INT4),
                                  SortOrder.ASC)};
            if (0 < sortCondition_.size()) {
                sort = sortCondition_.toArray(new SortCondition[0]);
            }

            // リスト更新
            unit_model_.refresh(search, sort);
        } catch (Exception e) {
            logger_.warning(e.toString());
        }

        if (0 <= row_) {
            columns_.putAll(model_.getColumns(row_));
            Calendar c1 = Calendar.getInstance();
            Calendar c2 = Calendar.getInstance();
            if (columns_.get("contract_start_date") != null) {
                c1.setTime((Date) columns_.get("contract_start_date"));
                contract_start_date_.setValue(c1);
            }
            if (columns_.get("contract_end_date") != null) {
                c2.setTime((Date) columns_.get("contract_end_date"));
                contract_end_date_.setValue(c2);
            }

            if (mode_ == 1) {
                //複写の場合にはID==0とする
                columns_.put("id", 0);
                columns_.put("keiyakusyo_path", "");
            }
        } else {

            columns_.put("id", 0);
            columns_.put("name", "");
            columns_.put("address1", "");
            columns_.put("address2", "");
            columns_.put("tel", "");
            columns_.put("kana", "");
            columns_.put("finish_flag", false);
            columns_.put("ent_id", 0);
            columns_.put("contract_start_date", DBManager.convert((Calendar.getInstance()).getTime()));
            columns_.put("contract_end_date", DBManager.convert((Calendar.getInstance()).getTime()));
            columns_.put("municipality_no", 1);
            columns_.put("remark", "");
            columns_.put("bill_id", 0);
            //20210922 add
            columns_.put("contract_day", DBManager.convert((Calendar.getInstance()).getTime()));
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
        finish_flagField_.setSelected(false);
    }

    private boolean checkInputItems() {
        if ("".equals(nameField_.getText().trim())) {
            JOptionPane.showMessageDialog(
                     this,
                     "排出事業場名を入力してください.",
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
            } else if (source == idField_) {
                idField_.selectAll();
            } else if (source == 契約番号Field_) {
                契約番号Field_.selectAll();
            }
            //契約番号Field_
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
            if (source == p_nameField_) {
                columns_.put("p_name", p_nameField_.getText().trim());
            }
            if (source == zip_codeField_) {
                columns_.put("zip_code", zip_codeField_.getText().trim());
            }
            if (source == telField_) {
                columns_.put("tel", telField_.getText().trim());
            }
            if (source == idField_) {
                columns_.put("id", Integer.valueOf(idField_.getText()));
            }
            if (source == finish_flagField_) {
                columns_.put("finish_flag", finish_flagField_.isSelected());
            }
            if (source == auto_update_flag_) {
                columns_.put("auto_update_flag", auto_update_flag_.isSelected());
            }
            if (source == contract_start_date_) {
                columns_.put("contract_start_date", contract_start_date_.getValue() != null ? DBManager.convert(((Calendar) contract_start_date_.getValue()).getTime()) : null);
            }
            if (source == contract_end_date_) {
                columns_.put("contract_end_date", contract_end_date_.getValue() != null ? DBManager.convert(((Calendar) contract_end_date_.getValue()).getTime()) : null);
            }
            //備考Field_
            if (source == 備考Field_) {
                columns_.put("remark", 備考Field_.getText());
            }
            //交付担当者Field_
            if (source == 交付担当者Field_) {
                columns_.put("tantou", 交付担当者Field_.getText());
            }
            //交付担当者所属Field_ 20170124 追加
            if (source == 交付担当者所属Field_) {
                columns_.put("tantou_syozoku", 交付担当者所属Field_.getText());
            }
            if (source == 契約番号Field_) {
                columns_.put("keiyaku_no", 契約番号Field_.getText());
            }
            if (source == contract_day_) {
                columns_.put("contract_day", contract_day_.getValue() != null ? DBManager.convert(((Calendar) contract_day_.getValue()).getTime()) : null);
            }

        }
    }

    private void JTextField_KeyPressed(java.awt.event.KeyEvent evt, JTextField JTextField_) {
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            JTextField_.transferFocus();
            evt.consume();
        }
    }

    private void Last_Field_KeyPressed(java.awt.event.KeyEvent evt) {
        // Enterでフォーカスを移動する
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            registerButton_.requestFocus();
        }
    }

    // カタカナモードの設定
    private void setKatakanaMode() {
        Character.Subset[] subsets = {java.awt.im.InputSubset.HALFWIDTH_KATAKANA};
        kanaField_.getInputContext().setCompositionEnabled(true);
        kanaField_.getInputContext().setCharacterSubsets(subsets);
    }

    private class GetNextID implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent event) {
            if ((Integer) columns_.get("ent_id") > 0 && row_ < 0) {
                columns_.put("id", model_.getNextID((Integer) columns_.get("ent_id")));
                updateView();
            } else if ((Integer) columns_.get("ent_id") > 0 && mode_ == 1) {
                columns_.put("id", model_.getNextID((Integer) columns_.get("ent_id")));
                updateView();
            }
        }
    }

    private class SelectBillAction implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent event) {
            BillSelector selector = new BillSelector(model_.getConnection());
            switch (selector.showSelector(AgreementDetailsPane.this)) {
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
            }
            updateView();
        }
    }

    private class SelectEntAction implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent event) {
            EntSelector selector = new EntSelector(model_.getConnection());
            switch (selector.showSelector(AgreementDetailsPane.this)) {
                case EntSelector.CLEARED:
                    columns_.put("ent_id", 0);
                    columns_.put("ent_name", "");
                    columns_.put("ent_p_name", "");
                    break;
                case EntSelector.SELECTED:
                    Map<String, Object> item = selector.selectedRow();
                    columns_.put("ent_id", item.get("id"));
                    columns_.put("ent_name", (String) item.get("name"));
                    columns_.put("ent_p_name", (String) item.get("p_name"));

                    break;
                default:
            }
            updateView();
        }
    }

    private class SelectGoodsAction implements ActionListener {

        public SelectGoodsAction() {
        }

        @Override
        public void actionPerformed(ActionEvent event) {

            //品目マスタの単価参照
            pane.requestFocus();
            getGoodsUnit();
            updateView();
        }
    }

    private class AddGoodsAction implements ActionListener {

        public AddGoodsAction() {

        }

        @Override
        public void actionPerformed(ActionEvent event) {

            //品目マスタの単価参照
            GoodsSelector selector = new GoodsSelector(model_.getConnection());
            switch (selector.showSelector(AgreementDetailsPane.this)) {
                case GoodsSelector.SELECTED:
                    Map<String, Object> item = selector.selectedRow();
                    columns_.put("goods_id", item.get("id"));
                    columns_.put("goods_name", (String) item.get("name"));
                    columns_.put("goods_unit", item.get("unit_value"));
                    columns_.put("goods_unit_no", item.get("unit_no"));
                    columns_.put("goods_unit_no_name", item.get("unit_name"));
                    columns_.put("option1_name", item.get("option1_name"));
                    columns_.put("carry_name", item.get("carry_name"));
                    unit_model_.addRow(columns_);
                    break;
                default:
            }

            updateView();
        }
    }

    private class DeleteGoodsAction implements ActionListener {

        public DeleteGoodsAction() {

        }

        @Override
        public void actionPerformed(ActionEvent event) {

            //選択行削除
            if (unitDataTable_.getSelectedRow() >= 0) {
                unit_model_.removeRow(unitDataTable_.getSelectedRow());
            }
            updateView();
        }
    }

    private void getGoodsUnit() {

        String s = "";
        s += "select unit_value from v0_goods_kind ";
        s += " where id = ? ;";
        ResultSet rs;

        try {
            PreparedStatement ps = unit_model_.getConnection().prepareStatement(s);
            try {
                for (int i = 0; i < unit_model_.getRowCount(); i++) {
                    ps.setObject(1, unit_model_.getValueAt(i, "goods_id"));
                    rs = ps.executeQuery();
                    if (rs.next()) {
                        unit_model_.setValueAt(rs.getInt("unit_value"), i, AgreementUnitTableModel.COLUMN_GOODS_UNIT);
                    }
                }
                unit_model_.refreshRow();
            } finally {
                ps.close();
            }
        } catch (SQLException e) {
            System.out.print(e.toString() + ":getGoodsUnit()");
        }
    }

    private class SelecTransAction implements ActionListener {

        private int tindex_;

        public SelecTransAction(int index) {
            tindex_ = index;
        }

        @Override
        public void actionPerformed(ActionEvent event) {
            TransSelector selector = new TransSelector(model_.getConnection());
            switch (selector.showSelector(AgreementDetailsPane.this)) {
                case EntSelector.CLEARED:
                    columns_.put(String.format("trans_id%1$02d", tindex_), 0);
                    columns_.put(String.format("trans_name%1$02d", tindex_), "");
                    break;
                case EntSelector.SELECTED:
                    Map<String, Object> item = selector.selectedRow();
                    columns_.put(String.format("trans_id%1$02d", tindex_), item.get("id"));
                    columns_.put(String.format("trans_name%1$02d", tindex_), (String) item.get("name"));
                    break;
                default:
                // No operation.
            }
            updateView();
        }
    }

    private class SelecContAction implements ActionListener {

        private int cindex_;

        public SelecContAction(int index) {
            cindex_ = index;
        }

        @Override
        public void actionPerformed(ActionEvent event) {
            ContSelector selector = new ContSelector(model_.getConnection());
            switch (selector.showSelector(AgreementDetailsPane.this)) {
                case EntSelector.CLEARED:
                    columns_.put(String.format("cont_id%1$02d", cindex_), 0);
                    columns_.put(String.format("cont_name%1$02d", cindex_), "");
                    break;
                case EntSelector.SELECTED:
                    Map<String, Object> item = selector.selectedRow();
                    columns_.put(String.format("cont_id%1$02d", cindex_), item.get("id"));
                    columns_.put(String.format("cont_name%1$02d", cindex_), (String) item.get("name"));
                    break;
                default:
            }
            updateView();
        }
    }

    private class Select契約書Action implements ActionListener {

        public void actionPerformed(ActionEvent event) {
            JFileChooser fc = new JFileChooser(契約書ファイルパスField_.getText().trim());
            fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
            if (JFileChooser.APPROVE_OPTION
                     == fc.showOpenDialog(AgreementDetailsPane.this)) {
                columns_.put("keiyakusyo_path", fc.getSelectedFile().toString());
            }
            updateView();
        }
    }
}
