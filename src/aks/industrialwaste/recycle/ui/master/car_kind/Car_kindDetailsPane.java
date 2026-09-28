package aks.industrialwaste.recycle.ui.master.car_kind;

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
import java.awt.Color;
import aks.swing.JTextField;
import java.awt.EventQueue;
import jp.co.astax.awt.FontUtility;
import java.awt.event.KeyEvent;
import aks.industrialwaste.database.Column;
import aks.industrialwaste.database.ColumnType;
import aks.industrialwaste.database.SortCondition;
import aks.industrialwaste.database.SortOrder;
import aks.industrialwaste.ui.DetailsPanel;

public class Car_kindDetailsPane extends DetailsPanel {

    private Car_kindTableModel model_;
    private int row_;
    private Map<String, Object> columns_;
    private JTextField idField_;
    private JTextField nameField_;
    private JPanel pane;

    public Car_kindDetailsPane(Car_kindTableModel model, int row) {
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
        titleField_.setText("車種マスタ詳細");

        // 詳細 - 左側
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
            public void keyPressed(java.awt.event.KeyEvent evt) {
                Last_Field_KeyPressed(evt);
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

        label = new JLabel("※同じ名称は登録できません。", JLabel.LEFT);
        label.setFont(font14);
        label.setForeground(Color.RED);
        addContainer(
                 panel, label,
                 0, 2, 3, 1, 0.0, 0.0,
                 GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
                 new Insets(2, 2, 2, 2), 0, 0);

        // ダミー
        addContainer(
                 panel, new JPanel(),
                 0, 3, 1, 1, 0.0, 1.0,
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

            Car_kindJournal.recordJournal(
                     model_.getConnection(),
                     "車種マスタ詳細 - 追加",
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
            Car_kindJournal.recordJournal(
                     model_.getConnection(),
                     "車種マスタ詳細 - 訂正",
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
                     "車種名を入力してください.",
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
}
