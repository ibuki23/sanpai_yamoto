package aks.industrialwaste.recycle.ui.master.dis_office;

import aks.industrialwaste.database.Column;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import aks.industrialwaste.recycle.ui.master.goods.GoodsTableModel;
import aks.industrialwaste.recycle.util.GoodsSelector;
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
import aks.industrialwaste.database.ColumnType;
import aks.industrialwaste.database.CombineOperator;
import aks.industrialwaste.database.CompareOperator;
import aks.industrialwaste.database.SearchCondition;
import aks.industrialwaste.database.SortCondition;
import aks.industrialwaste.database.SortOrder;
import aks.industrialwaste.ui.DetailsPanel;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import jp.co.astax.awt.FontUtility;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.swing.JButton;

public class Dis_officeDetailsPane extends DetailsPanel {

    private Dis_officeTableModel model_;
    private GoodsTableModel goods_model_;
    private int row_;
    private Map<String, Object> columns_, goods_columns_;
    private JTextField idField_;
    private JTextField nameField_;
    private JTextField kanaField_;
    private JTextField address1Field_;
    private JTextField p_nameField_;
    private JTextField zip_codeField_;
    private JTextField telField_;
    private JPanel pane;
    private JCheckBox finish_flagField_;

    private JTextField ent_nameField_;
    private JTextField[] goodsField_, goodsUnitField_;
    private JLabel[] goodsLabel_;
    private JButton[] goodsBtn_;

    private List<SearchCondition> searchCondition_;
    private List<SortCondition> sortCondition_;

    public Dis_officeDetailsPane(Dis_officeTableModel model, int row) {
        model_ = model;
        row_ = row;
        goods_model_ = new GoodsTableModel(model_.getConnection());

        columns_ = new HashMap<String, Object>();
        goods_columns_ = new HashMap<String, Object>();
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
        // タイトル
        titleField_.setText("契約（現場）マスタ詳細");

        // 詳細 - 左側
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createLoweredBevelBorder());
        panel.setMinimumSize(new Dimension(600, 600));
        panel.setPreferredSize(new Dimension(600, 600));

        // 排出業者名
        JLabel label = new JLabel("排出業者", JLabel.LEFT);
        label.setFont(font14);
        label.setForeground(Color.RED);
        addContainer(
                 panel, label,
                 0, 0, 1, 1, 0.1, 0.0,
                 GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
                 new Insets(2, 2, 2, 2), 0, 0);
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
                 new Insets(2, 2, 2, 2), 0, 0);

        // ID
        label = new JLabel("ID", JLabel.LEFT);
        label.setFont(font14);
        label.setForeground(Color.RED);
        addContainer(
                 panel, label,
                 0, 1, 1, 1, 0.1, 0.0,
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
                 1, 1, 1, 1, 0.9, 0.0,
                 GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
                 new Insets(2, 2, 2, 2), 0, 0);
        // 名称
        label = new JLabel("名称", JLabel.LEFT);
        label.setFont(font14);
        label.setForeground(Color.RED);
        addContainer(
                 panel, label,
                 0, 2, 1, 1, 0.0, 0.0,
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
                 1, 2, 1, 1, 0.0, 0.0,
                 GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
                 new Insets(2, 2, 2, 2), 0, 0);

        // かな
        label = new JLabel("ｶﾅ", JLabel.LEFT);
        label.setFont(font14);
        addContainer(
                 panel, label,
                 0, 3, 1, 1, 0.0, 0.0,
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
                 1, 3, 1, 1, 0.0, 0.0,
                 GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
                 new Insets(2, 2, 2, 2), 0, 0);

        // 所在地1
        label = new JLabel("所在地1", JLabel.LEFT);
        label.setFont(font14);
        addContainer(
                 panel, label,
                 0, 4, 1, 1, 0.0, 0.0,
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
                 1, 4, 1, 1, 0.0, 0.0,
                 GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
                 new Insets(2, 2, 2, 2), 0, 0);

        // 正式名称
        label = new JLabel("正式名称", JLabel.LEFT);
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

        // 郵便番号
        label = new JLabel("郵便番号", JLabel.LEFT);
        label.setFont(font14);
        addContainer(
                 panel, label,
                 0, 6, 1, 1, 0.0, 0.0,
                 GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
                 new Insets(2, 2, 2, 2), 0, 0);
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
                 panel, zip_codeField_,
                 1, 6, 1, 1, 0.0, 0.0,
                 GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
                 new Insets(2, 2, 2, 2), 0, 0);

        // TEL
        label = new JLabel("TEL", JLabel.LEFT);
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
                Last_Field_KeyPressed(evt);
            }
        });
        addContainer(
                 panel, telField_,
                 1, 7, 1, 1, 0.0, 0.0,
                 GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
                 new Insets(2, 2, 2, 2), 0, 0);

        // 現場終了チェック
        label = new JLabel("現場終了", JLabel.LEFT);
        label.setFont(font14);
        addContainer(
                 panel, label,
                 0, 8, 1, 1, 0.0, 0.0,
                 GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
                 new Insets(2, 2, 2, 2), 0, 0);
        finish_flagField_ = new JCheckBox();
        finish_flagField_.setFont(font14);
        finish_flagField_.setMinimumSize(new Dimension(150, 30));
        finish_flagField_.setPreferredSize(new Dimension(150, 30));
        finish_flagField_.addFocusListener(new ColumnUpdater());
        addContainer(
                 panel, finish_flagField_,
                 1, 8, 1, 1, 0.0, 0.0,
                 GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
                 new Insets(2, 2, 2, 2), 0, 0);

        label = new JLabel("※チェックを入れると検索リストに表示されません。", JLabel.LEFT);
        label.setFont(font14);
        addContainer(
                 panel, label,
                 0, 9, 3, 1, 0.0, 0.0,
                 GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
                 new Insets(2, 2, 2, 2), 0, 0);
        label = new JLabel("※名称は全角１５文字以内で入力してください。", JLabel.LEFT);
        label.setFont(font14);
        label.setForeground(Color.RED);
        addContainer(
                 panel, label,
                 0, 10, 3, 1, 0.0, 0.0,
                 GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
                 new Insets(2, 2, 2, 2), 0, 0);
        label = new JLabel("（計量表印刷時に正常に印字されません。）", JLabel.LEFT);
        label.setFont(font14);
        label.setForeground(Color.RED);
        addContainer(
                 panel, label,
                 0, 11, 3, 1, 0.0, 0.0,
                 GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
                 new Insets(2, 2, 2, 2), 0, 0);

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
        goodsField_ = new JTextField[40];
        goodsUnitField_ = new JTextField[40];
        goodsLabel_ = new JLabel[40];
        goodsBtn_ = new JButton[40];

        pane = new JPanel(new GridBagLayout());
        pane.setBorder(BorderFactory.createLoweredBevelBorder());
        pane.setMinimumSize(new Dimension(600, 600));
        pane.setPreferredSize(new Dimension(600, 600));

        JPanel pane1 = new JPanel(new GridBagLayout());
        pane1.setMinimumSize(new Dimension(300, 600));
        pane1.setPreferredSize(new Dimension(300, 600));

        JPanel pane2 = new JPanel(new GridBagLayout());
        pane2.setMinimumSize(new Dimension(300, 600));
        pane2.setPreferredSize(new Dimension(300, 600));

        JLabel l1 = new JLabel("契約品目");
        JLabel l2 = new JLabel("契約単価");
        JLabel l3 = new JLabel("契約品目");
        JLabel l4 = new JLabel("契約単価");
        l1.setFont(font14);
        l2.setFont(font14);
        l3.setFont(font14);
        l4.setFont(font14);

        addContainer(
                 pane1, l1,
                 1, 0, 1, 1, 0.0, 0.0,
                 GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
                 new Insets(2, 2, 2, 2), 0, 0);
        addContainer(
                 pane1, l2,
                 2, 0, 1, 1, 0.0, 0.0,
                 GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
                 new Insets(2, 2, 2, 2), 0, 0);
        addContainer(
                 pane2, l3,
                 1, 0, 1, 1, 0.0, 0.0,
                 GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
                 new Insets(2, 2, 2, 2), 0, 0);
        addContainer(
                 pane2, l4,
                 2, 0, 1, 1, 0.0, 0.0,
                 GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
                 new Insets(2, 2, 2, 2), 0, 0);

        //左側前半部分２０個
        int cnt = 0;
        for (cnt = 0; cnt < 20; cnt++) {
            goodsLabel_[cnt] = new JLabel(String.valueOf(cnt + 1));
            goodsLabel_[cnt].setFont(font14);
            goodsLabel_[cnt].setHorizontalAlignment(JLabel.RIGHT);
            goodsLabel_[cnt].setMinimumSize(new Dimension(30, 30));
            goodsLabel_[cnt].setPreferredSize(new Dimension(30, 30));
            addContainer(
                     pane1, goodsLabel_[cnt],
                     0, cnt + 1, 1, 1, 0.05, 0.0,
                     GridBagConstraints.WEST, GridBagConstraints.NONE,
                     new Insets(2, 2, 2, 2), 0, 0);

            goodsField_[cnt] = new JTextField();
            goodsField_[cnt].setFont(font14);
            goodsField_[cnt].setEnabled(false);
            goodsField_[cnt].setFocusable(false);
            goodsField_[cnt].setMinimumSize(new Dimension(150, 30));
            goodsField_[cnt].setPreferredSize(new Dimension(150, 30));
            addContainer(
                     pane1, goodsField_[cnt],
                     1, cnt + 1, 1, 1, 0.35, 0.0,
                     GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
                     new Insets(2, 2, 2, 2), 0, 0);

            goodsUnitField_[cnt] = new JTextField();
            goodsUnitField_[cnt].setFont(font14);
            goodsUnitField_[cnt].setHorizontalAlignment(JLabel.RIGHT);
            goodsUnitField_[cnt].setMinimumSize(new Dimension(50, 30));
            goodsUnitField_[cnt].setPreferredSize(new Dimension(50, 30));
            addContainer(
                     pane1, goodsUnitField_[cnt],
                     2, cnt + 1, 1, 1, 0.15, 0.0,
                     GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
                     new Insets(2, 2, 2, 2), 0, 0);

            goodsBtn_[cnt] = new JButton("選択");
            goodsBtn_[cnt].setFont(font14);
            goodsBtn_[cnt].setFocusable(false);
            goodsBtn_[cnt].setMargin(new Insets(2, 2, 2, 2));
            goodsBtn_[cnt].setMinimumSize(new Dimension(70, 30));
            goodsBtn_[cnt].setPreferredSize(new Dimension(70, 30));
            goodsBtn_[cnt].addActionListener(new SelectGoodsAction(cnt));
        }
        //右側後半部分２０個
        for (cnt = 20; cnt < 40; cnt++) {
            goodsLabel_[cnt] = new JLabel(String.valueOf(cnt + 1));
            goodsLabel_[cnt].setFont(font14);
            goodsLabel_[cnt].setHorizontalAlignment(JLabel.RIGHT);
            goodsLabel_[cnt].setMinimumSize(new Dimension(30, 30));
            goodsLabel_[cnt].setPreferredSize(new Dimension(30, 30));
            addContainer(
                     pane2, goodsLabel_[cnt],
                     0, cnt - 20 + 1, 1, 1, 0.05, 0.0,
                     GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
                     new Insets(2, 2, 2, 2), 0, 0);

            goodsField_[cnt] = new JTextField();
            goodsField_[cnt].setFont(font14);
            goodsField_[cnt].setEnabled(false);
            goodsField_[cnt].setFocusable(false);
            goodsField_[cnt].setMinimumSize(new Dimension(150, 30));
            goodsField_[cnt].setPreferredSize(new Dimension(150, 30));
            addContainer(
                     pane2, goodsField_[cnt],
                     1, cnt - 20 + 1, 1, 1, 0.35, 0.0,
                     GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
                     new Insets(2, 2, 2, 2), 0, 0);

            goodsUnitField_[cnt] = new JTextField();
            goodsUnitField_[cnt].setFont(font14);
            goodsUnitField_[cnt].setHorizontalAlignment(JLabel.RIGHT);
            goodsUnitField_[cnt].setMinimumSize(new Dimension(50, 30));
            goodsUnitField_[cnt].setPreferredSize(new Dimension(50, 30));
            addContainer(
                     pane2, goodsUnitField_[cnt],
                     2, cnt - 20 + 1, 1, 1, 0.15, 0.0,
                     GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
                     new Insets(2, 2, 2, 2), 0, 0);

            goodsBtn_[cnt] = new JButton("選択");
            goodsBtn_[cnt].setFont(font14);
            goodsBtn_[cnt].setFocusable(false);
            goodsBtn_[cnt].setMargin(new Insets(2, 2, 2, 2));
            goodsBtn_[cnt].setMinimumSize(new Dimension(70, 30));
            goodsBtn_[cnt].setPreferredSize(new Dimension(70, 30));
            goodsBtn_[cnt].addActionListener(new SelectGoodsAction(cnt));

        }
        JLabel l10 = new JLabel("契約外の品目は単価設定を空欄にしてください。");
        l10.setFont(font14);
        l10.setForeground(Color.RED);
        addContainer(
                 pane, l10,
                 0, 1, 2, 1, 0.0, 0.0,
                 GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
                 new Insets(2, 2, 2, 2), 0, 0);

        JPanel btn_pane1 = new JPanel(new GridBagLayout());
        btn_pane1.setBorder(BorderFactory.createLoweredBevelBorder());
        btn_pane1.setMinimumSize(new Dimension(600, 35));
        btn_pane1.setPreferredSize(new Dimension(600, 35));

        JButton b1 = new JButton("標準単価");
        b1.setFont(font14);
        b1.setFocusable(false);
        b1.setMargin(new Insets(2, 2, 2, 2));
        b1.setMinimumSize(new Dimension(120, 30));
        b1.setPreferredSize(new Dimension(120, 30));
        b1.addActionListener(new SelectGoodsAction(0));

        addContainer(
                 btn_pane1, b1,
                 0, 0, 1, 1, 0.0, 0.0,
                 GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
                 new Insets(2, 2, 2, 2), 0, 0);

        JButton b2 = new JButton("A単価");
        b2.setFont(font14);
        b2.setFocusable(false);
        b2.setMargin(new Insets(2, 2, 2, 2));
        b2.setMinimumSize(new Dimension(120, 30));
        b2.setPreferredSize(new Dimension(120, 30));
        b2.addActionListener(new SelectGoodsAction(1));

        addContainer(
                 btn_pane1, b2,
                 1, 0, 1, 1, 0.0, 0.0,
                 GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
                 new Insets(2, 2, 2, 2), 0, 0);

        JButton b3 = new JButton("B単価");
        b3.setFont(font14);
        b3.setFocusable(false);
        b3.setMargin(new Insets(2, 2, 2, 2));
        b3.setMinimumSize(new Dimension(120, 30));
        b3.setPreferredSize(new Dimension(120, 30));
        b3.addActionListener(new SelectGoodsAction(2));

        addContainer(
                 btn_pane1, b3,
                 2, 0, 1, 1, 0.0, 0.0,
                 GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
                 new Insets(2, 2, 2, 2), 0, 0);

        JButton b4 = new JButton("C単価");
        b4.setFont(font14);
        b4.setFocusable(false);
        b4.setMargin(new Insets(2, 2, 2, 2));
        b4.setMinimumSize(new Dimension(120, 30));
        b4.setPreferredSize(new Dimension(120, 30));
        b4.addActionListener(new SelectGoodsAction(3));

        addContainer(
                 btn_pane1, b4,
                 3, 0, 1, 1, 0.0, 0.0,
                 GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
                 new Insets(2, 2, 2, 2), 0, 0);

        addContainer(
                 pane, btn_pane1,
                 0, 2, 2, 1, 0.0, 0.0,
                 GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
                 new Insets(2, 2, 2, 2), 0, 0);

        addContainer(
                 pane, pane1,
                 0, 0, 1, 1, 0.5, 1.0,
                 GridBagConstraints.EAST, GridBagConstraints.BOTH,
                 new Insets(2, 2, 2, 2), 0, 0);
        addContainer(
                 pane, pane2,
                 1, 0, 1, 1, 0.5, 1.0,
                 GridBagConstraints.EAST, GridBagConstraints.BOTH,
                 new Insets(2, 2, 2, 2), 0, 0);

        addContainer(
                 centerPane_, pane,
                 1, 0, 1, 1, 0.5, 0.0,
                 GridBagConstraints.EAST, GridBagConstraints.BOTH,
                 new Insets(2, 2, 2, 2), 0, 0);

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
                     + " where v0_office.id = ? and v0_office.sno <> ?;";

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
            Dis_officeJournal.recordJournal(
                     model_.getConnection(),
                     "排出事業場マスタ詳細 - 追加",
                     columns_);
            if (model_.insert(columns_)) {
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
            Dis_officeJournal.recordJournal(
                     model_.getConnection(),
                     "排出事業場マスタ詳細 - 訂正",
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
        kanaField_.setText((String) columns_.get("kana"));
        address1Field_.setText((String) columns_.get("address1"));
        p_nameField_.setText((String) columns_.get("p_name"));
        zip_codeField_.setText((String) columns_.get("zip_code"));
        telField_.setText((String) columns_.get("tel"));
        finish_flagField_.setSelected((Boolean) columns_.get("finish_flag"));

        for (int i = 0; i < 40; i++) {
            goodsField_[i].setText((String) goods_columns_.get(String.format("goods_name%1$02d", i + 1)));
            if (goods_columns_.get(String.format("goods_unit%1$02d", i + 1)) != null) {
                goodsUnitField_[i].setText(String.valueOf((Integer) goods_columns_.get(String.format("goods_unit%1$02d", i + 1))));
            } else {
                goodsUnitField_[i].setText("");
            }
        }

        o = columns_.get("last_update");
        if (o instanceof Calendar) {
            updateTimeField_.setValue(((Calendar) o).getTime());
        }
    }

    private void copyColumns() {

        try {
            // 検索条件, ソート条件を設定
            SearchCondition[] search
                     = new SearchCondition[]{
                         new SearchCondition(
                                  new Column("商品ID", "id", ColumnType.INT4),
                                  CompareOperator.GE,
                                  1,
                                  CombineOperator.NONE)};
            if (0 < searchCondition_.size()) {
                search = searchCondition_.toArray(new SearchCondition[0]);
            }
            SortCondition[] sort
                     = new SortCondition[]{
                         new SortCondition(
                                  new Column("商品ID", "sno", ColumnType.INT4),
                                  SortOrder.ASC)};
            if (0 < sortCondition_.size()) {
                sort = sortCondition_.toArray(new SortCondition[0]);
            }

            // リスト更新
            goods_model_.refresh(search, sort);
        } catch (Exception e) {
            logger_.warning(e.toString());
        }

        goods_model_.getRowCount();
        columns_.clear();
        for (int i = 0; i < goods_model_.getRowCount(); i++) {
            goods_columns_.put(String.format("goods_name%1$02d", (Integer) goods_model_.getValueAt(i, "id")), (String) goods_model_.getValueAt(i, "name"));
            goods_columns_.put(String.format("goods_unit%1$02d", i + 1), null);
        }

        if (0 <= row_) {
            columns_.putAll(model_.getColumns(row_));
            for (int i = 0; i < 40; i++) {
                goods_columns_.put(String.format("goods_unit%1$02d", i), columns_.get(String.format("goods_unit%1$02d", i)));
            }
        } else {
            columns_.put("id", 0);
            columns_.put("name", "");
            columns_.put("address1", "");
            columns_.put("address2", "");
            columns_.put("tel", "");
            columns_.put("kana", "");
            columns_.put("finish_flag", false);

        }

        for (int i = 0; i < goods_model_.getRowCount(); i++) {
            goods_columns_.put(String.format("goods_name%1$02d", (Integer) goods_model_.getValueAt(i, "id")), (String) goods_model_.getValueAt(i, "name"));
        }

    }

    private void clearField() {
        idField_.setText("0");
        nameField_.setText("");
        kanaField_.setText("");
        address1Field_.setText("");
        p_nameField_.setText("");
        zip_codeField_.setText("");
        telField_.setText("");
        finish_flagField_.setSelected(false);

        for (int i = 0; i < 40; i++) {
            goodsField_[i].setText("");
        }

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
            } else if (source == p_nameField_) {
                p_nameField_.selectAll();
            } else if (source == zip_codeField_) {
                zip_codeField_.selectAll();
            } else if (source == telField_) {
                telField_.selectAll();
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

        }
    }

    private void JTextField_KeyPressed(java.awt.event.KeyEvent evt, JTextField JTextField_) {

        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            JTextField_.transferFocus();
            evt.consume();
        }
    }

    private void Last_Field_KeyPressed(java.awt.event.KeyEvent evt) {
        // TODO add your handling code here:
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

    private class SelectGoodsAction implements ActionListener {

        private int index_;

        public SelectGoodsAction(int index) {
            index_ = index;
        }

        @Override
        public void actionPerformed(ActionEvent event) {
            GoodsSelector selector = new GoodsSelector(model_.getConnection());
            switch (selector.showSelector(Dis_officeDetailsPane.this)) {
                case GoodsSelector.CLEARED:
                    columns_.put(String.format("goods_id%1$02d", index_), 0);
                    columns_.put(String.format("goods_name%1$02d", index_), "");

                    break;
                case GoodsSelector.SELECTED:
                    Map<String, Object> item = selector.selectedRow();
                    columns_.put(String.format("goods_id%1$02d", index_), item.get("id"));
                    columns_.put(String.format("goods_name%1$02d", index_), (String) item.get("name"));

                    break;
                default:
            }
            updateView();
        }
    }
}
