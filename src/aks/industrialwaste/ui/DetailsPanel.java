package aks.industrialwaste.ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.text.SimpleDateFormat;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import jp.co.astax.awt.FontUtility;
import java.awt.event.KeyEvent;

public class DetailsPanel extends Panel {

    private boolean insertMode_ = false;
    protected JButton movePreviousButton_;
    protected JButton moveNextButton_;
    protected JPanel detailsPane_;
    protected JTextField operatorField_;
    protected JFormattedTextField updateTimeField_;
    protected JButton deleteButton_;
    protected JButton registerButton_;
    protected JButton cancelButton_;
    protected JButton closeButton_;
    protected JButton minimumButton_;
    protected JTextField messageField_;
    protected boolean mini_flag_ = false;
    protected String message_ = "";
    protected boolean first_regist_flag_ = false;

    public DetailsPanel() {
        super();
    }

    public boolean isInsertMode() {
        return insertMode_;
    }

    public void setInsertMode(boolean b) {
        insertMode_ = b;
    }

    protected void doClose() {
        Window w = SwingUtilities.getWindowAncestor(this);
        if (w instanceof Frame) {
            w.dispatchEvent(new WindowEvent(w, WindowEvent.WINDOW_CLOSING));
        } else {
            w.dispose();
        }
    }

    protected void doCancel() {
        updateView();
    }

    protected void doDelete() {
    }

    protected void doMoveNext() {
    }

    protected void doMovePrevious() {
    }

    protected void doRegister() {
    }

    protected void doMinimum() {
        if (mini_flag_) {
            Window w = SwingUtilities.getWindowAncestor(this);
            w.setSize(1280, 1000);
            w.setLocationRelativeTo(null);
            mini_flag_ = false;

        } else {

            Window w = SwingUtilities.getWindowAncestor(this);
            w.setSize(480, 125);
            w.setLocation(20, 820);
            mini_flag_ = true;

        }
    }

    protected void doMessage() {
        operatorField_.setText(message_);
    }

    protected void initComponents() {
        super.initComponents();

        Font font14 = FontUtility.getFont("メイリオ", Font.PLAIN, 14);

        // 前ボタン, 次ボタンをタイトル横に追加する.
        // 前ボタン
        movePreviousButton_ = new JButton("<");
        movePreviousButton_.setFont(font14);
        movePreviousButton_.setMargin(new Insets(0, 0, 0, 0));
        movePreviousButton_.setMinimumSize(new Dimension(45, 40));
        movePreviousButton_.setPreferredSize(new Dimension(45, 40));
        movePreviousButton_.addActionListener(
                 new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                doMovePrevious();

            }
        });
        northPane_.add(
                 movePreviousButton_,
                 new GridBagConstraints(
                          1, 0, 1, 1, 0.0, 0.0,
                          GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                          new Insets(2, 2, 2, 2), 0, 0));
        // 次ボタン
        moveNextButton_ = new JButton(">");
        moveNextButton_.setFont(font14);
        moveNextButton_.setMargin(new Insets(0, 0, 0, 0));
        moveNextButton_.setMinimumSize(new Dimension(45, 40));
        moveNextButton_.setPreferredSize(new Dimension(45, 40));
        moveNextButton_.addActionListener(
                 new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                doMoveNext();

            }
        });
        northPane_.add(
                 moveNextButton_,
                 new GridBagConstraints(
                          2, 0, 1, 1, 0.0, 0.0,
                          GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                          new Insets(2, 2, 2, 2), 0, 0));

        detailsPane_ = new JPanel(new GridBagLayout());

        JPanel panel = new JPanel(new GridBagLayout());
        southPane_.add(
                 panel,
                 new GridBagConstraints(
                          0, 0, 1, 1, 1.0, 0.0,
                          GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
                          new Insets(2, 2, 2, 2), 0, 0));

        // 変更者, 変更日時を画面下部に追加する.
        // 変更者
        panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEtchedBorder());
        JLabel label = new JLabel("メッセージ", JLabel.RIGHT);
        label.setFont(font14);
        panel.add(
                 label,
                 new GridBagConstraints(
                          0, 0, 1, 1, 0.1, 0.0,
                          GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL,
                          new Insets(2, 2, 2, 2), 0, 0));
        operatorField_ = new JTextField("");
        operatorField_.setFont(font14);
        operatorField_.setForeground(new Color(255, 100, 0));
        operatorField_.setEditable(false);
        operatorField_.setFocusable(false);
        operatorField_.setMinimumSize(new Dimension(200, 30));
        operatorField_.setPreferredSize(new Dimension(200, 30));
        panel.add(
                 operatorField_,
                 new GridBagConstraints(
                          1, 0, 1, 1, 0.9, 0.0,
                          GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
                          new Insets(2, 2, 2, 2), 0, 0));
        // 変更日時
        label = new JLabel("変更日時", JLabel.RIGHT);
        label.setFont(font14);
        panel.add(
                 label,
                 new GridBagConstraints(
                          0, 1, 1, 1, 0.0, 0.0,
                          GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL,
                          new Insets(2, 2, 2, 2), 0, 0));
        updateTimeField_
                 = new JFormattedTextField(new SimpleDateFormat("yyyy/MM/dd HH:mm:ss"));
        updateTimeField_.setFont(font14);
        updateTimeField_.setEditable(false);
        updateTimeField_.setFocusable(false);
        updateTimeField_.setMinimumSize(new Dimension(200, 30));
        updateTimeField_.setPreferredSize(new Dimension(200, 30));
        panel.add(
                 updateTimeField_,
                 new GridBagConstraints(
                          1, 1, 1, 1, 0.0, 0.0,
                          GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
                          new Insets(2, 2, 2, 2), 0, 0));
        southPane_.add(
                 panel,
                 new GridBagConstraints(
                          1, 0, 1, 1, 0.0, 0.0,
                          GridBagConstraints.EAST, GridBagConstraints.NONE,
                          new Insets(2, 2, 2, 2), 0, 0));

        // ボタンを画面下部に追加する.
        panel = new JPanel(new GridBagLayout());
        // 削除ボタン
        deleteButton_ = new JButton("削除");
        deleteButton_.setFont(font14);
        deleteButton_.setMargin(new Insets(1, 1, 1, 1));
        deleteButton_.setMinimumSize(new Dimension(150, 30));
        deleteButton_.setPreferredSize(new Dimension(150, 30));
        deleteButton_.addActionListener(
                 new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                doDelete();
            }
        });
        panel.add(
                 deleteButton_,
                 new GridBagConstraints(
                          0, 0, 1, 1, 0.5, 0.0,
                          GridBagConstraints.WEST, GridBagConstraints.NONE,
                          new Insets(2, 2, 2, 2), 0, 0));

        // 登録ボタン
        registerButton_ = new JButton("登録");
        registerButton_.setFont(font14);
        registerButton_.setMargin(new Insets(1, 1, 1, 1));
        registerButton_.setMinimumSize(new Dimension(150, 30));
        registerButton_.setPreferredSize(new Dimension(150, 30));
        registerButton_.addActionListener(
                 new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                doRegister();
            }
        });
        registerButton_.addKeyListener(new java.awt.event.KeyAdapter() {
            @Override
            public void keyPressed(java.awt.event.KeyEvent evt) {
                registerButton_KeyPressed(evt);
            }
        });
        panel.add(
                 registerButton_,
                 new GridBagConstraints(
                          1, 0, 1, 1, 0.5, 0.0,
                          GridBagConstraints.EAST, GridBagConstraints.NONE,
                          new Insets(2, 2, 2, 2), 0, 0));
        // キャンセルボタン
        cancelButton_ = new JButton("キャンセル");
        cancelButton_.setFont(font14);
        cancelButton_.setMargin(new Insets(1, 1, 1, 1));
        cancelButton_.setMinimumSize(new Dimension(150, 30));
        cancelButton_.setPreferredSize(new Dimension(150, 30));
        cancelButton_.addActionListener(
                 new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                doCancel();
                first_regist_flag_ = false;
            }
        });
        panel.add(
                 cancelButton_,
                 new GridBagConstraints(
                          2, 0, 1, 1, 0.0, 0.0,
                          GridBagConstraints.EAST, GridBagConstraints.NONE,
                          new Insets(2, 2, 2, 2), 0, 0));
        // 閉じるボタン
        closeButton_ = new JButton("閉じる");
        closeButton_.setFont(font14);
        closeButton_.setMargin(new Insets(1, 1, 1, 1));
        closeButton_.setMinimumSize(new Dimension(150, 30));
        closeButton_.setPreferredSize(new Dimension(150, 30));
        closeButton_.addActionListener(
                 new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                doClose();
            }
        });
        panel.add(
                 closeButton_,
                 new GridBagConstraints(
                          3, 0, 1, 1, 0.0, 0.0,
                          GridBagConstraints.EAST, GridBagConstraints.NONE,
                          new Insets(2, 2, 2, 2), 0, 0));
        //最小化ボタン
        minimumButton_ = new JButton("最大最小化");
        minimumButton_.setFont(font14);
        minimumButton_.setMargin(new Insets(1, 1, 1, 1));
        minimumButton_.setMinimumSize(new Dimension(150, 30));
        minimumButton_.setPreferredSize(new Dimension(150, 30));
        minimumButton_.addActionListener(
                 new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                doMinimum();
            }
        });
        southPane_.add(
                 panel,
                 new GridBagConstraints(
                          0, 1, 2, 1, 1.0, 0.0,
                          GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
                          new Insets(2, 2, 2, 2), 0, 0));

    }

    private void registerButton_KeyPressed(java.awt.event.KeyEvent evt) {

        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            doRegister();

        }
    }
}
