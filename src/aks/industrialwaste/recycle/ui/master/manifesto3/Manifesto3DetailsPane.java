package aks.industrialwaste.recycle.ui.master.manifesto3;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import aks.industrialwaste.recycle.util.ContSelector;
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
import aks.industrialwaste.ui.DetailsPanel;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import jp.co.astax.awt.FontUtility;
import java.awt.event.KeyEvent;
import javax.swing.JButton;

public class Manifesto3DetailsPane extends DetailsPanel {

    private Manifesto3TableModel model_;
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
    private JPanel pane;

    private JTextField ent_nameField_;
    private JTextField cont_nameField_;

    public Manifesto3DetailsPane(Manifesto3TableModel model, int row) {
        model_ = model;
        row_ = row;

        columns_ = new HashMap<String, Object>();
        copyColumns();
    }

    @Override
    protected void initComponents() {
        super.initComponents();
        Font font14 = FontUtility.getFont("メイリオ", Font.PLAIN, 14);
        // タイトル
        titleField_.setText("処分場マスタ詳細");

        // 詳細 - 左側
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createLoweredBevelBorder());
        panel.setMinimumSize(new Dimension(600, 600));
        panel.setPreferredSize(new Dimension(600, 600));

        // 排出業者名
        JLabel label = new JLabel("処分場名称", JLabel.LEFT);
        label.setFont(font14);
        label.setForeground(Color.RED);

        ent_nameField_ = new JTextField();
        ent_nameField_.setFont(font14);
        ent_nameField_.setEditable(false);
        ent_nameField_.setFocusable(false);
        ent_nameField_.addFocusListener(new ColumnUpdater());
        ent_nameField_.setMinimumSize(new Dimension(400, 30));
        ent_nameField_.setPreferredSize(new Dimension(400, 30));
        ent_nameField_.addKeyListener(new java.awt.event.KeyAdapter() {
            @Override
            public void keyPressed(java.awt.event.KeyEvent evt) {
                JTextField_KeyPressed(evt, ent_nameField_);
            }
        });
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
        label = new JLabel("処分場名称", JLabel.LEFT);
        label.setFont(font14);
        label.setForeground(Color.RED);
        addContainer(
                 panel, label,
                 0, 2, 1, 1, 0.0, 0.0,
                 GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
                 new Insets(2, 2, 2, 2), 0, 0);
        nameField_ = new JTextField();
        nameField_.setFont(font14);
        nameField_.setMinimumSize(new Dimension(400, 30));
        nameField_.setPreferredSize(new Dimension(400, 30));
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
                 1, 2, 1, 1, 1.0, 0.0,
                 GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
                 new Insets(2, 2, 2, 2), 0, 0);

        // かな
        label = new JLabel("ｶﾅ", JLabel.LEFT);
        label.setFont(font14);
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
            public void keyPressed(java.awt.event.KeyEvent evt) {
                JTextField_KeyPressed(evt, address1Field_);
            }
        });
        addContainer(
                 panel, address1Field_,
                 1, 3, 1, 1, 0.0, 0.0,
                 GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
                 new Insets(2, 2, 2, 2), 0, 0);

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

        // 処分業者
        label = new JLabel("処分業者", JLabel.LEFT);
        label.setFont(font14);
        label.setForeground(Color.RED);
        addContainer(
                 panel, label,
                 0, 0, 1, 1, 0.1, 0.0,
                 GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
                 new Insets(2, 2, 2, 2), 0, 0);
        cont_nameField_ = new JTextField();
        cont_nameField_.setFont(font14);
        cont_nameField_.setEditable(false);
        cont_nameField_.setFocusable(false);
        cont_nameField_.addFocusListener(new ColumnUpdater());
        cont_nameField_.setMinimumSize(new Dimension(150, 30));
        cont_nameField_.setPreferredSize(new Dimension(150, 30));
        cont_nameField_.addKeyListener(new java.awt.event.KeyAdapter() {
            @Override
            public void keyPressed(java.awt.event.KeyEvent evt) {
                JTextField_KeyPressed(evt, cont_nameField_);
            }
        });
        addContainer(
                 panel, cont_nameField_,
                 1, 0, 1, 1, 0.9, 0.0,
                 GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
                 new Insets(2, 2, 2, 2), 0, 0);
        JButton btn1 = new JButton("選択");
        btn1.setFont(font14);
        btn1.setFocusable(false);
        btn1.setMargin(new Insets(2, 2, 2, 2));
        btn1.setMinimumSize(new Dimension(100, 30));
        btn1.setPreferredSize(new Dimension(100, 30));
        btn1.addActionListener(new SelecContAction());
        addContainer(
                 panel, btn1,
                 2, 0, 1, 1, 0.0, 0.0,
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
            String s = "";
            s = "select count(*) as cnt from v0_manifesto_3 "
                     + " where v0_manifesto_3.id = ? "
                     + " and v0_manifesto_3.cont_id = ? "
                     + (row_ == -1 ? "" : " and v0_manifesto_3.sno <> ? ")
                     + " ;";

            PreparedStatement ps = model_.getConnection().prepareStatement(s);
            try {
                ps.setObject(1, columns_.get("id"));
                ps.setObject(2, columns_.get("cont_id"));
                if (row_ > -1) {
                    ps.setObject(3, columns_.get("sno"));
                }

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
            Manifesto3Journal.recordJournal(
                     model_.getConnection(),
                     "最終処分場マスタ詳細 - 追加",
                     columns_);
            if (model_.insert(columns_)) {
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
            Manifesto3Journal.recordJournal(
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
        Object o = columns_.get("sno");
        if (o instanceof Number) {
        }
        idField_.setText(String.valueOf(columns_.get("id")));
        nameField_.setText((String) columns_.get("name"));
        address1Field_.setText((String) columns_.get("address1"));
        address2Field_.setText((String) columns_.get("address2"));
        zip_codeField_.setText((String) columns_.get("zip_code"));
        telField_.setText((String) columns_.get("tel"));
        cont_nameField_.setText((String) columns_.get("cont_name"));

        o = columns_.get("last_update");
        if (o instanceof Calendar) {
            updateTimeField_.setValue(((Calendar) o).getTime());
        }
    }

    private void copyColumns() {

        if (0 <= row_) {
            columns_.putAll(model_.getColumns(row_));
        } else {
            columns_.put("id", 0);
            columns_.put("name", "");
            columns_.put("address1", "");
            columns_.put("address2", "");
            columns_.put("tel", "");
            columns_.put("zip_code", "");

        }

    }

    private void clearField() {
        idField_.setText("0");
        nameField_.setText("");
        kanaField_.setText("");
        address1Field_.setText("");
        address2Field_.setText("");
        zip_codeField_.setText("");
        telField_.setText("");

        cont_nameField_.setText("");
    }

    private boolean checkInputItems() {
        if ("".equals(nameField_.getText().trim())) {
            JOptionPane.showMessageDialog(
                     this,
                     "最終処分場名称を入力してください。",
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
            if (source == idField_) {
                columns_.put("id", Integer.valueOf(idField_.getText()));
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

    private class SelecContAction implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent event) {
            ContSelector selector = new ContSelector(model_.getConnection());
            switch (selector.showSelector(Manifesto3DetailsPane.this)) {
                case ContSelector.CLEARED:
                    columns_.put("cont_id", 0);
                    columns_.put("cont_name", "");
                    break;
                case ContSelector.SELECTED:
                    Map<String, Object> item = selector.selectedRow();
                    columns_.put("cont_id", item.get("id"));
                    columns_.put("cont_name", (String) item.get("name"));
                    break;
                default:
            }
            updateView();
        }
    }
}
