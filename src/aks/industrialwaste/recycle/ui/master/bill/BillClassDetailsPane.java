package aks.industrialwaste.recycle.ui.master.bill;

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
import javax.swing.JComboBox;

public class BillClassDetailsPane extends DetailsPanel {

    private BillClassTableModel model_;
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
    private JTextField pay_dayField_;
    private JPanel pane;
    private JComboBox tax_no_Field_;

    public BillClassDetailsPane(BillClassTableModel model, int row) {
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
        titleField_.setText("請求分類マスタ詳細");

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

        // ダミー
        JPanel p = new JPanel();
        addContainer(
                 panel, p,
                 0, 14, 1, 1, 0.0, 1.0,
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
            String s = "select count(*) as cnt from v0_m_b_class "
                     + " where v0_m_b_class.id = ? and v0_m_b_class.sno <> ?;";

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
            BillJournal.recordJournal(
                     model_.getConnection(),
                     "請求先マスタ詳細 - 追加",
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
            BillJournal.recordJournal(
                     model_.getConnection(),
                     "請求先マスタ詳細 - 訂正",
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
        }
    }

    private void clearField() {
        idField_.setText("0");
        nameField_.setText("");
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
            } else if (source == pay_dayField_) {
                pay_dayField_.selectAll();
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
            if (source == bundle_dayField_) {
                columns_.put("bundle_day", Integer.valueOf(bundle_dayField_.getText().trim()));
            }
            if (source == pay_dayField_) {
                columns_.put("pay_day", Integer.valueOf(pay_dayField_.getText().trim()));
            }
            if (source == tax_no_Field_) {
                columns_.put("tax_no", tax_no_Field_.getSelectedIndex());
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
}
