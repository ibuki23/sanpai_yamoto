package aks.industrialwaste.recycle.ui.master.contractory;

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
import aks.industrialwaste.database.Column;
import aks.industrialwaste.database.ColumnType;
import aks.industrialwaste.database.SortCondition;
import aks.industrialwaste.database.SortOrder;
import aks.industrialwaste.ui.DetailsPanel;
import java.awt.EventQueue;
import jp.co.astax.awt.FontUtility;
import java.awt.event.KeyEvent;

public class ContractoryDetailsPane extends DetailsPanel {

    private ContractoryTableModel model_;
    private int row_;
    private Map<String, Object> columns_;
    private JTextField idField_;
    private JTextField nameField_;
    private JTextField kanaField_;
    private JTextField con_address1Field_;
    private JTextField con_address2Field_;
    private JTextField con_p_nameField_;
    private JTextField con_zip_codeField_;
    private JTextField con_telField_;
    private JTextField office_address1Field_;
    private JTextField office_address2Field_;
    private JTextField office_p_nameField_;
    private JTextField office_zip_codeField_;
    private JTextField office_telField_;
    private JTextField tantou_Field_;
    private JTextField last_tantou_Field_;
    private JPanel pane;

    private JTextField permission_Field_;

    public ContractoryDetailsPane(ContractoryTableModel model, int row) {
        model_ = model;
        row_ = row;
        columns_ = new HashMap<>();
        copyColumns();
    }

    @Override
    protected void initComponents() {
        super.initComponents();
        Font font18 = FontUtility.getFont("メイリオ", Font.PLAIN, 14);
        // タイトル
        titleField_.setText("処分業者マスタ詳細");

        // 詳細 - 左側
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createLoweredBevelBorder());

        // ID
        JLabel label = new JLabel("ID", JLabel.LEFT);
        label.setFont(font18);
        label.setForeground(Color.RED);
        addContainer(
                 panel, label,
                 0, 0, 1, 1, 0.1, 0.0,
                 GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
                 new Insets(2, 2, 2, 2), 0, 0);
        idField_ = new JTextField();
        idField_.setFont(font18);
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
        label.setFont(font18);
        label.setForeground(Color.RED);
        addContainer(
                 panel, label,
                 0, 1, 1, 1, 0.0, 0.0,
                 GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
                 new Insets(2, 2, 2, 2), 0, 0);
        nameField_ = new JTextField();
        nameField_.setFont(font18);
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

        // ｶﾅ
        label = new JLabel("ｶﾅ", JLabel.LEFT);
        label.setFont(font18);
        addContainer(
                 panel, label,
                 0, 2, 1, 1, 0.0, 0.0,
                 GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
                 new Insets(2, 2, 2, 2), 0, 0);
        kanaField_ = new JTextField();
        kanaField_.setFont(font18);
        kanaField_.setMinimumSize(new Dimension(150, 30));
        kanaField_.setPreferredSize(new Dimension(150, 30));
        kanaField_.addFocusListener(new ColumnUpdater());
        kanaField_.addFocusListener(new java.awt.event.FocusAdapter() {
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
        label.setFont(font18);
        addContainer(
                 panel, label,
                 0, 3, 1, 1, 0.0, 0.0,
                 GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
                 new Insets(2, 2, 2, 2), 0, 0);
        con_address1Field_ = new JTextField();
        con_address1Field_.setFont(font18);
        con_address1Field_.setMinimumSize(new Dimension(150, 30));
        con_address1Field_.setPreferredSize(new Dimension(150, 30));
        con_address1Field_.addFocusListener(new ColumnUpdater());
        con_address1Field_.addKeyListener(new java.awt.event.KeyAdapter() {

            @Override
            public void keyPressed(java.awt.event.KeyEvent evt) {
                JTextField_KeyPressed(evt, con_address1Field_);
            }
        });
        addContainer(
                 panel, con_address1Field_,
                 1, 3, 1, 1, 0.0, 0.0,
                 GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
                 new Insets(2, 2, 2, 2), 0, 0);

        // 処分業者所在地2
        label = new JLabel("業者 所在地2", JLabel.LEFT);
        label.setFont(font18);
        addContainer(
                 panel, label,
                 0, 4, 1, 1, 0.0, 0.0,
                 GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
                 new Insets(2, 2, 2, 2), 0, 0);
        con_address2Field_ = new JTextField();
        con_address2Field_.setFont(font18);
        con_address2Field_.setMinimumSize(new Dimension(150, 30));
        con_address2Field_.setPreferredSize(new Dimension(150, 30));
        con_address2Field_.addFocusListener(new ColumnUpdater());
        con_address2Field_.addKeyListener(new java.awt.event.KeyAdapter() {
            @Override
            public void keyPressed(java.awt.event.KeyEvent evt) {
                JTextField_KeyPressed(evt, con_address2Field_);
            }
        });
        addContainer(
                 panel, con_address2Field_,
                 1, 4, 1, 1, 0.0, 0.0,
                 GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
                 new Insets(2, 2, 2, 2), 0, 0);

        // 処分業者正式名称
        label = new JLabel("業者 正式名称", JLabel.LEFT);
        label.setFont(font18);
        addContainer(
                 panel, label,
                 0, 5, 1, 1, 0.0, 0.0,
                 GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
                 new Insets(2, 2, 2, 2), 0, 0);
        con_p_nameField_ = new JTextField();
        con_p_nameField_.setFont(font18);
        con_p_nameField_.setMinimumSize(new Dimension(150, 30));
        con_p_nameField_.setPreferredSize(new Dimension(150, 30));
        con_p_nameField_.addFocusListener(new ColumnUpdater());
        con_p_nameField_.addKeyListener(new java.awt.event.KeyAdapter() {
            @Override
            public void keyPressed(java.awt.event.KeyEvent evt) {
                JTextField_KeyPressed(evt, con_p_nameField_);
            }
        });
        addContainer(
                 panel, con_p_nameField_,
                 1, 5, 1, 1, 0.0, 0.0,
                 GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
                 new Insets(2, 2, 2, 2), 0, 0);

        // 処分業者郵便番号
        label = new JLabel("業者 郵便番号", JLabel.LEFT);
        label.setFont(font18);
        addContainer(
                 panel, label,
                 0, 6, 1, 1, 0.0, 0.0,
                 GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
                 new Insets(2, 2, 2, 2), 0, 0);
        con_zip_codeField_ = new JTextField();
        con_zip_codeField_.setFont(font18);
        con_zip_codeField_.setMinimumSize(new Dimension(60, 30));
        con_zip_codeField_.setPreferredSize(new Dimension(60, 30));
        con_zip_codeField_.addFocusListener(new ColumnUpdater());
        con_zip_codeField_.addKeyListener(new java.awt.event.KeyAdapter() {
            @Override
            public void keyPressed(java.awt.event.KeyEvent evt) {
                JTextField_KeyPressed(evt, con_zip_codeField_);
            }
        });
        addContainer(
                 panel, con_zip_codeField_,
                 1, 6, 1, 1, 0.0, 0.0,
                 GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
                 new Insets(2, 2, 2, 2), 0, 0);

        // 処分業者TEL
        label = new JLabel("業者 TEL", JLabel.LEFT);
        label.setFont(font18);
        addContainer(
                 panel, label,
                 0, 7, 1, 1, 0.0, 0.0,
                 GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
                 new Insets(2, 2, 2, 2), 0, 0);
        con_telField_ = new JTextField();
        con_telField_.setFont(font18);
        con_telField_.setMinimumSize(new Dimension(150, 30));
        con_telField_.setPreferredSize(new Dimension(150, 30));
        con_telField_.addFocusListener(new ColumnUpdater());
        con_telField_.addKeyListener(new java.awt.event.KeyAdapter() {
            @Override
            public void keyPressed(java.awt.event.KeyEvent evt) {
                JTextField_KeyPressed(evt, con_telField_);
            }
        });
        addContainer(
                 panel, con_telField_,
                 1, 7, 1, 1, 0.0, 0.0,
                 GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
                 new Insets(2, 2, 2, 2), 0, 0);

        // 処分事業場　所在地1
        label = new JLabel("事業場 所在地1", JLabel.LEFT);
        label.setFont(font18);
        office_address1Field_ = new JTextField();
        office_address1Field_.setFont(font18);
        office_address1Field_.setMinimumSize(new Dimension(150, 30));
        office_address1Field_.setPreferredSize(new Dimension(150, 30));
        office_address1Field_.addFocusListener(new ColumnUpdater());
        office_address1Field_.addKeyListener(new java.awt.event.KeyAdapter() {
            @Override
            public void keyPressed(java.awt.event.KeyEvent evt) {
                JTextField_KeyPressed(evt, office_address1Field_);
            }
        });

        // 処分事業場　所在地2
        label = new JLabel("事業場 所在地2", JLabel.LEFT);
        label.setFont(font18);
        office_address2Field_ = new JTextField();
        office_address2Field_.setFont(font18);
        office_address2Field_.setMinimumSize(new Dimension(150, 30));
        office_address2Field_.setPreferredSize(new Dimension(150, 30));
        office_address2Field_.addFocusListener(new ColumnUpdater());
        office_address2Field_.addKeyListener(new java.awt.event.KeyAdapter() {
            @Override
            public void keyPressed(java.awt.event.KeyEvent evt) {
                JTextField_KeyPressed(evt, office_address2Field_);
            }
        });

        // 処分事業場　正式名称
        label = new JLabel("事業場 正式名称", JLabel.LEFT);
        label.setFont(font18);
        office_p_nameField_ = new JTextField();
        office_p_nameField_.setFont(font18);
        office_p_nameField_.setMinimumSize(new Dimension(150, 30));
        office_p_nameField_.setPreferredSize(new Dimension(150, 30));
        office_p_nameField_.addFocusListener(new ColumnUpdater());
        office_p_nameField_.addKeyListener(new java.awt.event.KeyAdapter() {
            @Override
            public void keyPressed(java.awt.event.KeyEvent evt) {
                JTextField_KeyPressed(evt, office_p_nameField_);
            }
        });

        // 処分事業場　郵便番号
        label = new JLabel("事業場 郵便番号", JLabel.LEFT);
        label.setFont(font18);
        office_zip_codeField_ = new JTextField();
        office_zip_codeField_.setFont(font18);
        office_zip_codeField_.setMinimumSize(new Dimension(150, 30));
        office_zip_codeField_.setPreferredSize(new Dimension(150, 30));
        office_zip_codeField_.addFocusListener(new ColumnUpdater());
        office_zip_codeField_.addKeyListener(new java.awt.event.KeyAdapter() {
            @Override
            public void keyPressed(java.awt.event.KeyEvent evt) {
                JTextField_KeyPressed(evt, office_zip_codeField_);
            }
        });

        // 処分事業場　TEL
        label = new JLabel("事業場 TEL", JLabel.LEFT);
        label.setFont(font18);
        office_telField_ = new JTextField();
        office_telField_.setFont(font18);
        office_telField_.setMinimumSize(new Dimension(150, 30));
        office_telField_.setPreferredSize(new Dimension(150, 30));
        office_telField_.addFocusListener(new ColumnUpdater());
        office_telField_.addKeyListener(new java.awt.event.KeyAdapter() {
            @Override
            public void keyPressed(java.awt.event.KeyEvent evt) {
                JTextField_KeyPressed(evt, office_telField_);
            }
        });

        // 許可番号
        label = new JLabel("許可番号", JLabel.LEFT);
        label.setFont(font18);
        addContainer(
                 panel, label,
                 0, 13, 1, 1, 0.0, 0.0,
                 GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
                 new Insets(2, 2, 2, 2), 0, 0);
        permission_Field_ = new JTextField();
        permission_Field_.setFont(font18);
        permission_Field_.setMinimumSize(new Dimension(150, 30));
        permission_Field_.setPreferredSize(new Dimension(150, 30));
        permission_Field_.addFocusListener(new ColumnUpdater());
        permission_Field_.addKeyListener(new java.awt.event.KeyAdapter() {
            @Override
            public void keyPressed(java.awt.event.KeyEvent evt) {
                JTextField_KeyPressed(evt, permission_Field_);
            }
        });
        addContainer(
                 panel, permission_Field_,
                 1, 13, 1, 1, 0.0, 0.0,
                 GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
                 new Insets(2, 2, 2, 2), 0, 0);

        // 処分担当者
        label = new JLabel("処分担当者", JLabel.LEFT);
        label.setFont(font18);
        addContainer(
                 panel, label,
                 0, 14, 1, 1, 0.0, 0.0,
                 GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
                 new Insets(2, 2, 2, 2), 0, 0);
        tantou_Field_ = new JTextField();
        tantou_Field_.setFont(font18);
        tantou_Field_.setMinimumSize(new Dimension(150, 30));
        tantou_Field_.setPreferredSize(new Dimension(150, 30));
        tantou_Field_.addFocusListener(new ColumnUpdater());
        tantou_Field_.addKeyListener(new java.awt.event.KeyAdapter() {
            @Override
            public void keyPressed(java.awt.event.KeyEvent evt) {
                JTextField_KeyPressed(evt, tantou_Field_);
            }
        });
        addContainer(
                 panel, tantou_Field_,
                 1, 14, 1, 1, 0.0, 0.0,
                 GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
                 new Insets(2, 2, 2, 2), 0, 0);

        // 最終処分担当者
        label = new JLabel("最終処分担当者", JLabel.LEFT);
        label.setFont(font18);
        addContainer(
                 panel, label,
                 0, 15, 1, 1, 0.0, 0.0,
                 GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
                 new Insets(2, 2, 2, 2), 0, 0);
        last_tantou_Field_ = new JTextField();
        last_tantou_Field_.setFont(font18);
        last_tantou_Field_.setMinimumSize(new Dimension(150, 30));
        last_tantou_Field_.setPreferredSize(new Dimension(150, 30));
        last_tantou_Field_.addFocusListener(new ColumnUpdater());
        last_tantou_Field_.addKeyListener(new java.awt.event.KeyAdapter() {

            @Override
            public void keyPressed(java.awt.event.KeyEvent evt) {
                Last_Field_KeyPressed(evt);
            }
        });
        addContainer(
                 panel, last_tantou_Field_,
                 1, 15, 1, 1, 0.0, 0.0,
                 GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
                 new Insets(2, 2, 2, 2), 0, 0);

        label = new JLabel("※名称は全角１８文字以内で入力してください。", JLabel.LEFT);
        label.setFont(font18);
        label.setForeground(Color.RED);
        addContainer(
                 panel, label,
                 0, 16, 3, 1, 0.0, 0.0,
                 GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
                 new Insets(2, 2, 2, 2), 0, 0);
        label = new JLabel("（計量表印刷時に正常に印字されません。）", JLabel.LEFT);
        label.setFont(font18);
        label.setForeground(Color.RED);
        addContainer(
                 panel, label,
                 0, 17, 3, 1, 0.0, 0.0,
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
            String s = "select count(*) as cnt from v0_cont "
                     + " where v0_cont.id = ? and v0_cont.sno <> ?;";

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

        //nameの文字数を確認する(全角18文字まで)
        if (columns_.get("name").toString().length() > 18) {
            JOptionPane.showMessageDialog(
                     this,
                     "名称は全角18文字まで入力できます。",
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
            ContractoryJournal.recordJournal(
                     model_.getConnection(),
                     "処分業者マスタ詳細 - 追加",
                     columns_);
            if (model_.insert(columns_)) {
                doClose();

                //リストの再描画
                SortCondition[] sort
                         = new SortCondition[]{
                             new SortCondition(
                                      new Column("ID", "id", ColumnType.INT4),
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
            ContractoryJournal.recordJournal(
                     model_.getConnection(),
                     "処分業者マスタ詳細 - 訂正",
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
                              new Column("ID", "id", ColumnType.INT4),
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
        con_address1Field_.setText((String) columns_.get("con_address1"));
        con_address2Field_.setText((String) columns_.get("con_address2"));
        con_p_nameField_.setText((String) columns_.get("con_p_name"));
        con_zip_codeField_.setText((String) columns_.get("con_zip_code"));
        con_telField_.setText((String) columns_.get("con_tel"));
        office_address1Field_.setText((String) columns_.get("office_address1"));
        office_address2Field_.setText((String) columns_.get("office_address2"));
        office_p_nameField_.setText((String) columns_.get("office_p_name"));
        office_zip_codeField_.setText((String) columns_.get("office_zip_code"));
        office_telField_.setText((String) columns_.get("office_tel"));
        permission_Field_.setText((String) columns_.get("permission_no"));
        tantou_Field_.setText((String) columns_.get("cont_tantou"));
        last_tantou_Field_.setText((String) columns_.get("last_tantou"));

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
            columns_.put("con_address1", "");
            columns_.put("con_address2", "");
            columns_.put("con_tel", "");
            columns_.put("office_p_name", "");
            columns_.put("office_tel", "");
            columns_.put("kana", "");
            columns_.put("permission_no", "");
            columns_.put("cont_tantou", "");
            columns_.put("last_tantou", "");
        }
    }

    private void clearField() {
        idField_.setText("0");
        nameField_.setText("");
        kanaField_.setText("");
        con_address1Field_.setText("");
        con_address2Field_.setText("");
        con_p_nameField_.setText("");
        con_zip_codeField_.setText("");
        con_telField_.setText("");
        office_address1Field_.setText("");
        office_address2Field_.setText("");
        office_p_nameField_.setText("");
        office_zip_codeField_.setText("");
        office_telField_.setText("");
        permission_Field_.setText("");
        tantou_Field_.setText("");
        last_tantou_Field_.setText("");
    }

    private boolean checkInputItems() {
        if ("".equals(nameField_.getText().trim())) {
            JOptionPane.showMessageDialog(
                     this,
                     "処分業者名を入力してください.",
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
            } else if (source == con_address1Field_) {
                con_address1Field_.selectAll();
            } else if (source == con_address2Field_) {
                con_address2Field_.selectAll();
            } else if (source == con_p_nameField_) {
                con_p_nameField_.selectAll();
            } else if (source == con_zip_codeField_) {
                con_zip_codeField_.selectAll();
            } else if (source == con_telField_) {
                con_telField_.selectAll();
            } else if (source == office_address1Field_) {
                office_address1Field_.selectAll();
            } else if (source == office_address2Field_) {
                office_address2Field_.selectAll();
            } else if (source == office_p_nameField_) {
                office_p_nameField_.selectAll();
            } else if (source == office_zip_codeField_) {
                office_zip_codeField_.selectAll();
            } else if (source == office_telField_) {
                office_telField_.selectAll();
            } else if (source == idField_) {
                idField_.selectAll();
            } else if (source == permission_Field_) {
                permission_Field_.selectAll();
            } else if (source == tantou_Field_) {
                tantou_Field_.selectAll();
            } else if (source == last_tantou_Field_) {
                last_tantou_Field_.selectAll();
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
            if (source == con_address1Field_) {
                columns_.put("con_address1", con_address1Field_.getText().trim());
            }
            if (source == con_address2Field_) {
                columns_.put("con_address2", con_address2Field_.getText().trim());
            }
            if (source == con_p_nameField_) {
                columns_.put("con_p_name", con_p_nameField_.getText().trim());
            }
            if (source == con_zip_codeField_) {
                columns_.put("con_zip_code", con_zip_codeField_.getText().trim());
            }
            if (source == con_telField_) {
                columns_.put("con_tel", con_telField_.getText().trim());
            }
            if (source == office_address1Field_) {
                columns_.put("office_address1", office_address1Field_.getText().trim());
            }
            if (source == office_address2Field_) {
                columns_.put("office_address2", office_address2Field_.getText().trim());
            }
            if (source == office_p_nameField_) {
                columns_.put("office_p_name", office_p_nameField_.getText().trim());
            }
            if (source == office_zip_codeField_) {
                columns_.put("office_zip_code", office_zip_codeField_.getText().trim());
            }
            if (source == office_telField_) {
                columns_.put("office_tel", office_telField_.getText().trim());
            }
            if (source == idField_) {
                columns_.put("id", Integer.valueOf(idField_.getText()));
            }
            if (source == permission_Field_) {
                columns_.put("permission_no", permission_Field_.getText());
            }
            if (source == tantou_Field_) {
                columns_.put("cont_tantou", tantou_Field_.getText());
            }
            if (source == last_tantou_Field_) {
                columns_.put("last_tantou", last_tantou_Field_.getText());
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
}
