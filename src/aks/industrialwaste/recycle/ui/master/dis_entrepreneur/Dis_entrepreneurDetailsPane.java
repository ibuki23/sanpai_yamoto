package aks.industrialwaste.recycle.ui.master.dis_entrepreneur;

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
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import aks.swing.JTextField;
import javax.swing.JComboBox;
import java.awt.EventQueue;
import jp.co.astax.awt.FontUtility;
import java.awt.event.KeyEvent;

public class Dis_entrepreneurDetailsPane extends DetailsPanel {

    private Dis_entrepreneurTableModel model_;
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
    private JCheckBox print_flagField_;
    private JPanel pane;
    //20120827
    private String[] tax_kind_ = new String[]{"しない", "1.報告する"};
    private JComboBox goods_kind_cmb1;
    private JComboBox goods_kind_cmb2;
    private JComboBox tax_kind_cmb;

    public Dis_entrepreneurDetailsPane(Dis_entrepreneurTableModel model, int row) {
        model_ = model;
        row_ = row;
        columns_ = new HashMap<>();
        copyColumns();
    }

    @Override
    protected void initComponents() {
        super.initComponents();
        Font font14 = FontUtility.getFont("メイリオ", Font.PLAIN, 14);
        tax_kind_ = new String[]{"しない", "1.報告する"};

        // タイトル
        titleField_.setText("排出事業者マスタ詳細");

        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createLoweredBevelBorder());
        panel.setMinimumSize(new Dimension(600, 600));
        panel.setPreferredSize(new Dimension(600, 600));

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
            //Enterでタブ移動
            public void keyPressed(java.awt.event.KeyEvent evt) {
                JTextField_KeyPressed(evt, nameField_);
            }
        });
        //最初のフォーカスを設定
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

        // 所在地1
        label = new JLabel("所在地1", JLabel.LEFT);
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
            //Enterでタブ移動
            public void keyPressed(java.awt.event.KeyEvent evt) {
                JTextField_KeyPressed(evt, address1Field_);
            }
        });
        addContainer(
                 panel, address1Field_,
                 1, 3, 1, 1, 0.0, 0.0,
                 GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
                 new Insets(2, 2, 2, 2), 0, 0);

        // 所在地2
        label = new JLabel("所在地2", JLabel.LEFT);
        label.setFont(font14);
        address2Field_ = new JTextField();
        address2Field_.setFont(font14);
        address2Field_.setMinimumSize(new Dimension(150, 30));
        address2Field_.setPreferredSize(new Dimension(150, 30));
        address2Field_.addFocusListener(new ColumnUpdater());
        address2Field_.addKeyListener(new java.awt.event.KeyAdapter() {
            @Override
            //Enterでタブ移動
            public void keyPressed(java.awt.event.KeyEvent evt) {
                JTextField_KeyPressed(evt, address2Field_);
            }
        });
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
            //Enterでタブ移動
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
            //Enterでタブ移動
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
            //Enterでタブ移動
            public void keyPressed(java.awt.event.KeyEvent evt) {
                JTextField_KeyPressed(evt, telField_);
            }
        });
        addContainer(
                 panel, telField_,
                 1, 7, 1, 1, 0.0, 0.0,
                 GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
                 new Insets(2, 2, 2, 2), 0, 0);

        // 計量票発行有無
        label = new JLabel("計量票発行有無", JLabel.LEFT);
        label.setFont(font14);
        addContainer(
                 panel, label,
                 0, 8, 1, 1, 0.0, 0.0,
                 GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
                 new Insets(2, 2, 2, 2), 0, 0);
        print_flagField_ = new JCheckBox();
        print_flagField_.setFont(font14);
        print_flagField_.setMinimumSize(new Dimension(150, 30));
        print_flagField_.setPreferredSize(new Dimension(150, 30));
        print_flagField_.addFocusListener(new ColumnUpdater());
        addContainer(
                 panel, print_flagField_,
                 1, 8, 1, 1, 0.0, 0.0,
                 GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
                 new Insets(2, 2, 2, 2), 0, 0);

        label = new JLabel("行政報告区分(中間)", JLabel.LEFT);
        label.setFont(font14);
        goods_kind_cmb1 = new JComboBox(tax_kind_);
        goods_kind_cmb1.setFont(font14);
        goods_kind_cmb1.setMinimumSize(new Dimension(150, 30));
        goods_kind_cmb1.setPreferredSize(new Dimension(150, 30));
        goods_kind_cmb1.addFocusListener(new ColumnUpdater());

        label = new JLabel("行政報告区分(最終)", JLabel.LEFT);
        label.setFont(font14);
        goods_kind_cmb2 = new JComboBox(tax_kind_);
        goods_kind_cmb2.setFont(font14);
        goods_kind_cmb2.setMinimumSize(new Dimension(150, 30));
        goods_kind_cmb2.setPreferredSize(new Dimension(150, 30));
        goods_kind_cmb2.addFocusListener(new ColumnUpdater());

        label = new JLabel("※名称は全角１８文字以内で入力してください。", JLabel.LEFT);
        label.setFont(font14);
        label.setForeground(Color.RED);
        addContainer(
                 panel, label,
                 0, 11, 3, 1, 0.0, 0.0,
                 GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
                 new Insets(2, 2, 2, 2), 0, 0);
        label = new JLabel("（計量表印刷時に正常に印字されません。）", JLabel.LEFT);
        label.setFont(font14);
        label.setForeground(Color.RED);
        addContainer(
                 panel, label,
                 0, 12, 3, 1, 0.0, 0.0,
                 GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
                 new Insets(2, 2, 2, 2), 0, 0);
        label = new JLabel("　", JLabel.LEFT);
        label.setFont(font14);
        addContainer(
                 panel, label,
                 0, 13, 3, 1, 0.0, 0.0,
                 GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
                 new Insets(2, 2, 2, 2), 0, 0);
        label = new JLabel("市町村合併や移転により使用しなくなった業者には、", JLabel.LEFT);
        label.setFont(font14);
        addContainer(
                 panel, label,
                 0, 14, 3, 1, 0.0, 0.0,
                 GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
                 new Insets(2, 2, 2, 2), 0, 0);
        label = new JLabel("名称の後ろに（旧）や全角スペースを付加してください。", JLabel.LEFT);
        label.setFont(font14);
        addContainer(
                 panel, label,
                 0, 15, 3, 1, 0.0, 0.0,
                 GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
                 new Insets(2, 2, 2, 2), 0, 0);
        label = new JLabel("※マニフェスト情報登録時にエラーとなります。", JLabel.LEFT);
        label.setFont(font14);
        addContainer(
                 panel, label,
                 0, 16, 3, 1, 0.0, 0.0,
                 GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
                 new Insets(2, 2, 2, 2), 0, 0);
        // ダミー
        addContainer(
                 panel, new JPanel(),
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
        pane.setMinimumSize(new Dimension(600, 600));
        pane.setPreferredSize(new Dimension(600, 600));
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
            String s = "select count(*) as cnt from v0_ent "
                     + " where v0_ent.id = ? and v0_ent.sno <> ?;";

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
        //20140729 edit nameの文字数を確認する(全角18文字まで)
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
            Dis_entrepreneurJournal.recordJournal(
                     model_.getConnection(),
                     "排出事業者マスタ詳細 - 追加",
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
            Dis_entrepreneurJournal.recordJournal(
                     model_.getConnection(),
                     "排出事業者マスタ詳細 - 訂正",
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
        address1Field_.setText((String) columns_.get("address1"));
        address2Field_.setText((String) columns_.get("address2"));
        p_nameField_.setText((String) columns_.get("p_name"));
        zip_codeField_.setText((String) columns_.get("zip_code"));
        telField_.setText((String) columns_.get("tel"));
        // 受領済み
        o = columns_.get("print_flag");
        if (o instanceof Boolean) {
            print_flagField_.setSelected((Boolean) o);
        }

        o = columns_.get("last_update");
        if (o instanceof Calendar) {
            updateTimeField_.setValue(((Calendar) o).getTime());
        }

        //20120827
        goods_kind_cmb1.setSelectedIndex((Integer) columns_.get("middle_goods_kind"));
        goods_kind_cmb2.setSelectedIndex((Integer) columns_.get("final_goods_kind"));

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
            columns_.put("print_flag", true);
            columns_.put("kana", "");

            columns_.put("middle_goods_kind", 0);
            columns_.put("final_goods_kind", 0);
            columns_.put("tax_kind", 0);

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
        print_flagField_.setSelected(false);

        // 追加:-1 訂正:行番号
        if (0 > row_) {
            columns_.put("print_flag", false);
        }
    }

    private boolean checkInputItems() {
        if ("".equals(nameField_.getText().trim())) {
            JOptionPane.showMessageDialog(
                     this,
                     "排出事業者名を入力してください.",
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
            if (source == p_nameField_) {
                columns_.put("p_name", p_nameField_.getText().trim());
            }
            if (source == zip_codeField_) {
                columns_.put("zip_code", zip_codeField_.getText().trim());
            }
            if (source == telField_) {
                columns_.put("tel", telField_.getText().trim());
            }
            if (source == print_flagField_) {
                columns_.put("print_flag", print_flagField_.isSelected());
            }
            if (source == idField_) {
                columns_.put("id", Integer.valueOf(idField_.getText()));
            }

            //20120822 add
            if (source == goods_kind_cmb1) {
                columns_.put("middle_goods_kind", goods_kind_cmb1.getSelectedIndex());
            }
            if (source == goods_kind_cmb2) {
                columns_.put("final_goods_kind", goods_kind_cmb2.getSelectedIndex());
            }
            if (source == tax_kind_cmb) {
                columns_.put("tax_kind", tax_kind_cmb.getSelectedIndex());
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
