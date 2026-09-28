package aks.industrialwaste.recycle.ui.weighlist;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import aks.swing.JTextField;
import aks.industrialwaste.recycle.util.*;
import aks.industrialwaste.ui.DetailsPanel2;
import aks.industrialwaste.util.TermDate;
import java.awt.EventQueue;
import jp.co.astax.awt.FontUtility;
import jp.co.astax.swing.DateTimeField;
import java.awt.event.KeyEvent;
import aks.swing.JTextFieldHankaku;

/**
 * 一時記憶詳細を表示する.
 */
public class GateInListDetailsPane extends DetailsPanel2 {

    private GateInListTableModel model_;
    private int row_;
    private Map<String, Object> columns_;
    private DateTimeField gateInDateField_;
    private JTextField dis_entField_;
    private JTextField transField_;
    private JTextFieldHankaku car_no_Field_;
    private JTextField goodsField_;
    private JTextField dis_office_Field_;
    private JTextField dis_contField_;
    private JTextField car_kindField_;
    private JTextFieldHankaku gateInWeightField_;
    private JPanel pane;
    private JTextField office_sub_Field_;
    private JTextField billField_;

    private JTextFieldHankaku 建設系マニフェスト番号Field_;
    private JTextFieldHankaku 産廃マニフェスト番号Field_;

    public GateInListDetailsPane(GateInListTableModel model, int row) {
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
        titleField_.setText("一時記憶データの変更");

        // 詳細 - 左側(上)
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createLoweredBevelBorder());
        panel.setMinimumSize(new Dimension(600, 600));
        panel.setPreferredSize(new Dimension(600, 600));
        // 入構日時
        JLabel label = new JLabel("時間", JLabel.LEFT);
        label.setFont(font14);
        addContainer(
                 panel, label,
                 0, 0, 1, 1, 0.1, 0.0,
                 GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
                 new Insets(2, 2, 2, 2), 0, 0);
        gateInDateField_ = new DateTimeField(
                 DateTimeField.DateFormat.YMD,
                 DateTimeField.TimeFormat.HM);
        gateInDateField_.setFont(font14);
        gateInDateField_.setMinimumSize(new Dimension(150, 30));
        gateInDateField_.setPreferredSize(new Dimension(150, 30));
        gateInDateField_.addFocusListener(new ColumnUpdater());
        addContainer(
                 panel, gateInDateField_,
                 1, 0, 2, 1, 0.0, 0.0,
                 GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
                 new Insets(2, 2, 2, 2), 0, 0);

        // 車番
        label = new JLabel("車番", JLabel.LEFT);
        label.setFont(font14);
        addContainer(
                 panel, label,
                 0, 1, 1, 1, 0.1, 0.0,
                 GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
                 new Insets(2, 2, 2, 2), 0, 0);
        car_no_Field_ = new JTextFieldHankaku();
        car_no_Field_.setFont(font14);
        car_no_Field_.setMinimumSize(new Dimension(150, 30));
        car_no_Field_.setPreferredSize(new Dimension(150, 30));
        car_no_Field_.addFocusListener(new ColumnUpdater());
        car_no_Field_.addKeyListener(new java.awt.event.KeyAdapter() {

            @Override
            //
            public void keyPressed(java.awt.event.KeyEvent evt) {
                JTextField_KeyPressed(evt, car_no_Field_);
            }
        });
        //最初のフォーカスを設定
        EventQueue.invokeLater(new Runnable() {

            @Override
            public void run() {
                car_no_Field_.requestFocusInWindow();
            }
        });
        addContainer(
                 panel, car_no_Field_,
                 1, 1, 2, 1, 0.8, 0.0,
                 GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
                 new Insets(2, 2, 2, 2), 0, 0);
        JButton btn = new JButton("選択");
        btn.setFont(font14);
        btn.setFocusable(false);
        btn.setMargin(new Insets(2, 2, 2, 2));
        btn.setMinimumSize(new Dimension(70, 30));
        btn.setPreferredSize(new Dimension(70, 30));
        btn.addActionListener(new SelectCarAction());
        addContainer(
                 panel, btn,
                 3, 1, 1, 1, 0.1, 0.0,
                 GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
                 new Insets(2, 2, 2, 2), 0, 0);

        // 請求先
        label = new JLabel("請求先", JLabel.LEFT);
        label.setFont(font14);
        addContainer(
                 panel, label,
                 0, 2, 1, 1, 0.1, 0.0,
                 GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
                 new Insets(2, 2, 2, 2), 0, 0);

        billField_ = new JTextField();
        billField_.setFont(font14);
        billField_.setFocusable(false);
        billField_.setEditable(false);
        billField_.setMinimumSize(new Dimension(150, 30));
        billField_.setPreferredSize(new Dimension(150, 30));
        addContainer(
                 panel, billField_,
                 1, 2, 2, 1, 0.8, 0.0,
                 GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
                 new Insets(2, 2, 2, 2), 0, 0);
        btn = new JButton("選択");
        btn.setFont(font14);
        btn.setFocusable(false);
        btn.setMargin(new Insets(2, 2, 2, 2));
        btn.setMinimumSize(new Dimension(70, 30));
        btn.setPreferredSize(new Dimension(70, 30));
        btn.addActionListener(new SelectBillAction());
        addContainer(
                 panel, btn,
                 3, 2, 1, 1, 0.1, 0.0,
                 GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
                 new Insets(2, 2, 2, 2), 0, 0);

        // 　排出事業者
        label = new JLabel("排出事業者", JLabel.LEFT);
        label.setFont(font14);
        addContainer(
                 panel, label,
                 0, 3, 1, 1, 0.1, 0.0,
                 GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
                 new Insets(2, 2, 2, 2), 0, 0);
        dis_entField_ = new JTextField();
        dis_entField_.setFont(font14);
        dis_entField_.setFocusable(false);
        dis_entField_.setEditable(false);
        dis_entField_.setMinimumSize(new Dimension(150, 30));
        dis_entField_.setPreferredSize(new Dimension(150, 30));
        dis_entField_.addKeyListener(new java.awt.event.KeyAdapter() {
            @Override
            public void keyPressed(java.awt.event.KeyEvent evt) {
                JTextField_KeyPressed(evt, dis_entField_);
            }
        });
        EventQueue.invokeLater(new Runnable() {

            @Override
            public void run() {
                dis_entField_.requestFocusInWindow();
            }
        });
        addContainer(
                 panel, dis_entField_,
                 1, 3, 2, 1, 0.8, 0.0,
                 GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
                 new Insets(2, 2, 2, 2), 0, 0);
        btn = new JButton("選択");
        btn.setFont(font14);
        btn.setFocusable(false);
        btn.setMargin(new Insets(2, 2, 2, 2));
        btn.setMinimumSize(new Dimension(70, 30));
        btn.setPreferredSize(new Dimension(70, 30));
        btn.addActionListener(new SelectEntAction());
        addContainer(
                 panel, btn,
                 3, 3, 1, 1, 0.1, 0.0,
                 GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
                 new Insets(2, 2, 2, 2), 0, 0);

        // 事業所　運搬業者
        label = new JLabel("運搬業者", JLabel.LEFT);
        label.setFont(font14);
        addContainer(
                 panel, label,
                 0, 4, 1, 1, 0.1, 0.0,
                 GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
                 new Insets(2, 2, 2, 2), 0, 0);
        transField_ = new JTextField();
        transField_.setFont(font14);
        transField_.setFocusable(false);
        transField_.setEditable(false);
        transField_.setMinimumSize(new Dimension(150, 30));
        transField_.setPreferredSize(new Dimension(150, 30));
        addContainer(
                 panel, transField_,
                 1, 4, 2, 1, 0.8, 0.0,
                 GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
                 new Insets(2, 2, 2, 2), 0, 0);
        btn = new JButton("選択");
        btn.setFont(font14);
        btn.setFocusable(false);
        btn.setMargin(new Insets(2, 2, 2, 2));
        btn.setMinimumSize(new Dimension(70, 30));
        btn.setPreferredSize(new Dimension(70, 30));
        btn.addActionListener(new SelectTransporterAction());
        addContainer(
                 panel, btn,
                 3, 4, 1, 1, 0.1, 0.0,
                 GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
                 new Insets(2, 2, 2, 2), 0, 0);

        // 種類
        label = new JLabel("品目", JLabel.LEFT);
        label.setFont(font14);
        addContainer(
                 panel, label,
                 0, 5, 1, 1, 0.1, 0.0,
                 GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
                 new Insets(2, 2, 2, 2), 0, 0);
        goodsField_ = new JTextField();
        goodsField_.setFont(font14);
        goodsField_.setFocusable(false);
        goodsField_.setEditable(false);
        goodsField_.setMinimumSize(new Dimension(150, 30));
        goodsField_.setPreferredSize(new Dimension(150, 30));
        addContainer(
                 panel, goodsField_,
                 1, 5, 2, 1, 0.8, 0.0,
                 GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
                 new Insets(2, 2, 2, 2), 0, 0);
        btn = new JButton("選択");
        btn.setFont(font14);
        btn.setFocusable(false);
        btn.setMargin(new Insets(2, 2, 2, 2));
        btn.setMinimumSize(new Dimension(70, 30));
        btn.setPreferredSize(new Dimension(70, 30));
        btn.addActionListener(new SelectGoodsAction());
        addContainer(
                 panel, btn,
                 3, 5, 1, 1, 0.1, 0.0,
                 GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
                 new Insets(2, 2, 2, 2), 0, 0);

        // 排出事業場
        label = new JLabel("排出事業場", JLabel.LEFT);
        label.setFont(font14);
        addContainer(
                 panel, label,
                 0, 6, 1, 1, 0.1, 0.0,
                 GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
                 new Insets(2, 2, 2, 2), 0, 0);
        dis_office_Field_ = new JTextField();
        dis_office_Field_.setFont(font14);
        dis_office_Field_.setFocusable(false);
        dis_office_Field_.setEditable(false);
        dis_office_Field_.setMinimumSize(new Dimension(250, 30));
        dis_office_Field_.setPreferredSize(new Dimension(250, 30));
        addContainer(
                 panel, dis_office_Field_,
                 1, 6, 1, 1, 0.0, 0.0,
                 GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
                 new Insets(2, 2, 2, 2), 0, 0);
        office_sub_Field_ = new JTextField();
        office_sub_Field_.setFont(font14);
        office_sub_Field_.addFocusListener(new ColumnUpdater());
        office_sub_Field_.setMinimumSize(new Dimension(150, 30));
        office_sub_Field_.setPreferredSize(new Dimension(150, 30));
        addContainer(
                 panel, office_sub_Field_,
                 2, 6, 1, 1, 0.0, 0.0,
                 GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
                 new Insets(2, 2, 2, 2), 0, 0);
        btn = new JButton("選択");
        btn.setFont(font14);
        btn.setFocusable(false);
        btn.setMargin(new Insets(2, 2, 2, 2));
        btn.setMinimumSize(new Dimension(70, 30));
        btn.setPreferredSize(new Dimension(70, 30));
        btn.addActionListener(new SelectOfficeAction());
        addContainer(
                 panel, btn,
                 3, 6, 1, 1, 0.1, 0.0,
                 GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
                 new Insets(2, 2, 2, 2), 0, 0);

        // 処分業者
        label = new JLabel("処分業者", JLabel.LEFT);
        label.setFont(font14);
        addContainer(
                 panel, label,
                 0, 7, 1, 1, 0.1, 0.0,
                 GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
                 new Insets(2, 2, 2, 2), 0, 0);
        dis_contField_ = new JTextField();
        dis_contField_.setFont(font14);
        dis_contField_.setFocusable(false);
        dis_contField_.setEditable(false);
        dis_contField_.setMinimumSize(new Dimension(150, 30));
        dis_contField_.setPreferredSize(new Dimension(150, 30));
        addContainer(
                 panel, dis_contField_,
                 1, 7, 2, 1, 0.8, 0.0,
                 GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
                 new Insets(2, 2, 2, 2), 0, 0);
        btn = new JButton("選択");
        btn.setFont(font14);
        btn.setFocusable(false);
        btn.setMargin(new Insets(2, 2, 2, 2));
        btn.setMinimumSize(new Dimension(70, 30));
        btn.setPreferredSize(new Dimension(70, 30));
        btn.addActionListener(new SelectContAction());
        addContainer(
                 panel, btn,
                 3, 7, 1, 1, 0.1, 0.0,
                 GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
                 new Insets(2, 2, 2, 2), 0, 0);

        // 車種
        label = new JLabel("車種", JLabel.LEFT);
        label.setFont(font14);
        addContainer(
                 panel, label,
                 0, 8, 1, 1, 0.1, 0.0,
                 GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
                 new Insets(2, 2, 2, 2), 0, 0);
        car_kindField_ = new JTextField();
        car_kindField_.setFont(font14);
        car_kindField_.setFocusable(false);
        car_kindField_.setEditable(false);
        car_kindField_.setMinimumSize(new Dimension(150, 30));
        car_kindField_.setPreferredSize(new Dimension(150, 30));
        addContainer(
                 panel, car_kindField_,
                 1, 8, 2, 1, 0.8, 0.0,
                 GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
                 new Insets(2, 2, 2, 2), 0, 0);
        btn = new JButton("選択");
        btn.setFont(font14);
        btn.setFocusable(false);
        btn.setMargin(new Insets(2, 2, 2, 2));
        btn.setMinimumSize(new Dimension(70, 30));
        btn.setPreferredSize(new Dimension(70, 30));
        btn.addActionListener(new SelectCarKindAction());
        addContainer(
                 panel, btn,
                 3, 8, 1, 1, 0.1, 0.0,
                 GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
                 new Insets(2, 2, 2, 2), 0, 0);
        // 一時記録重量
        label = new JLabel("一時記録重量(kg)", JLabel.LEFT);
        label.setFont(font14);
        addContainer(
                 panel, label,
                 0, 9, 1, 1, 0.0, 0.0,
                 GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
                 new Insets(2, 2, 2, 2), 0, 0);
        gateInWeightField_ = new JTextFieldHankaku();
        gateInWeightField_.setFont(font14);
        gateInWeightField_.setMinimumSize(new Dimension(150, 30));
        gateInWeightField_.setPreferredSize(new Dimension(150, 30));
        gateInWeightField_.addFocusListener(new ColumnUpdater());
        addContainer(
                 panel, gateInWeightField_,
                 1, 9, 2, 1, 0.8, 0.0,
                 GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
                 new Insets(2, 2, 2, 2), 0, 0);

//    建設系マニフェスト番号Field_;
        label = new JLabel("建廃交付番号", JLabel.LEFT);
        label.setFont(font14);
        addContainer(
                 panel, label,
                 0, 10, 1, 1, 0.1, 0.0,
                 GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
                 new Insets(2, 2, 2, 2), 0, 0);
        建設系マニフェスト番号Field_ = new JTextFieldHankaku();
        建設系マニフェスト番号Field_.setFont(font14);
        建設系マニフェスト番号Field_.setMinimumSize(new Dimension(150, 30));
        建設系マニフェスト番号Field_.setPreferredSize(new Dimension(150, 30));
        建設系マニフェスト番号Field_.addFocusListener(new ColumnUpdater());
        addContainer(
                 panel, 建設系マニフェスト番号Field_,
                 1, 10, 2, 1, 0.8, 0.0,
                 GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
                 new Insets(2, 2, 2, 2), 0, 0);
//    産廃マニフェスト番号Field_;
        label = new JLabel("産廃交付番号", JLabel.LEFT);
        label.setFont(font14);
        addContainer(
                 panel, label,
                 0, 11, 1, 1, 0.1, 0.0,
                 GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
                 new Insets(2, 2, 2, 2), 0, 0);
        産廃マニフェスト番号Field_ = new JTextFieldHankaku();
        産廃マニフェスト番号Field_.setFont(font14);
        産廃マニフェスト番号Field_.setMinimumSize(new Dimension(150, 30));
        産廃マニフェスト番号Field_.setPreferredSize(new Dimension(150, 30));
        産廃マニフェスト番号Field_.addFocusListener(new ColumnUpdater());
        産廃マニフェスト番号Field_.addKeyListener(new java.awt.event.KeyAdapter() {
            @Override
            public void keyPressed(java.awt.event.KeyEvent evt) {
                Last_Field_KeyPressed(evt);
            }
        });
        addContainer(
                 panel, 産廃マニフェスト番号Field_,
                 1, 11, 2, 1, 0.8, 0.0,
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
    }

    @Override
    public void updateView() {
        clearField();

        // 入構重量
        Object o = columns_.get("register_weight");
        if (o instanceof Number) {
            gateInWeightField_.setText(((Integer) columns_.get("register_weight")).toString());
        }
        // 入構日時
        gateInDateField_.setValue((Calendar) columns_.get("register_date"));
        // 請求先
        billField_.setText((String) columns_.get("bill_name"));
        // 排出事業者
        dis_entField_.setText((String) columns_.get("entrepreneur_name"));
        // 運搬業者
        transField_.setText((String) columns_.get("transporter_name"));
        // 車番
        car_no_Field_.setText(columns_.get("car_no").toString());
        // 種類
        goodsField_.setText((String) columns_.get("goods_name"));
        // 排出事業場
        dis_office_Field_.setText((String) columns_.get("office_name"));
        //20120617 現場名補完分
        office_sub_Field_.setText((String) columns_.get("office_sub_name"));
        // 処分業者
        dis_contField_.setText((String) columns_.get("contractor_name"));
        // 車種
        car_kindField_.setText((String) columns_.get("car_kind_name"));
        建設系マニフェスト番号Field_.setText((String) columns_.get("manifesto_no2"));
        産廃マニフェスト番号Field_.setText((String) columns_.get("manifesto_no1"));
        operatorField_.setText((String) columns_.get("operator"));
        o = columns_.get("last_update");
        if (o instanceof Calendar) {
            updateTimeField_.setValue(((Calendar) o).getTime());
        }

    }

    @Override
    protected void doDelete() {
        int result = JOptionPane.showConfirmDialog(
                 this,
                 "この項目を削除します. よろしいですか?",
                 "確認",
                 JOptionPane.YES_NO_OPTION);
        if (JOptionPane.YES_OPTION == result) {
            GateInListJournal.recordJournal(
                     model_.getConnection(),
                     "一時記憶詳細 - 削除",
                     new HashMap<String, Object>(model_.getColumns(row_)));
            if (model_.delete(row_)) {
                doClose();
            } else {
                JOptionPane.showMessageDialog(
                         this,
                         "削除に失敗しました.",
                         "警告",
                         JOptionPane.WARNING_MESSAGE);
            }
        }
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
            GateInListJournal.recordJournal(
                     model_.getConnection(),
                     "一時記憶詳細 - 追加",
                     columns_);
            if (model_.insert(columns_)) {
                doClose();
                return;
            }
        } else {
            GateInListJournal.recordJournal(
                     model_.getConnection(),
                     "一時記憶詳細 - 訂正",
                     columns_);
            if (model_.update(columns_)) {
                doClose();
                return;
            }
        }

        JOptionPane.showMessageDialog(
                 this,
                 "登録に失敗しました.",
                 "警告",
                 JOptionPane.WARNING_MESSAGE);
    }

    @Override
    protected void doMovePrevious() {
        if (0 < row_) {
            row_--;
            copyColumns();
            updateView();
        }
    }

    @Override
    protected void doMoveNext() {
        if ((0 <= row_) && ((model_.getRowCount() - 1) > row_)) {
            row_++;
            copyColumns();
            updateView();
        }
    }

    private void copyColumns() {
        columns_.clear();
        if (0 <= row_) {
            columns_.putAll(model_.getColumns(row_));
        }
        Calendar c = Calendar.getInstance();
        if (0 > row_) {
            columns_.put("gate_in_date", c);
            columns_.put("gate_out_date", c);
            columns_.put("term_date", TermDate.getTerm(c).getTime());
            columns_.put("register_weight", 0);
            columns_.put("car_no", 0);
            columns_.put("bill_id", 0);
            columns_.put("dis_ent_id", 0);
            columns_.put("trans_id", 0);
            columns_.put("goods_id", 0);
            columns_.put("dis_office_id", 0);
            columns_.put("dis_cont_id", 0);
            columns_.put("car_kind_id", 0);

            // 請求先
            columns_.put("bill_name", "");
            // 排出事業者
            columns_.put("entrepreneur_name", "");
            // 運搬業者
            columns_.put("transporter_name", "");

            // 種類
            columns_.put("goods_name", "");
            // 排出事業場
            columns_.put("office_name", "");
            // 処分業者
            columns_.put("contractor_name", "");
            // 車種
            columns_.put("car_kind_name", "");
            //建設系マニフェスト番号
            columns_.put("manifesto_no2", "");
            //産廃マニフェスト番号
            columns_.put("manifesto_no1", "");

        }
    }

    private void clearField() {
        Calendar c = Calendar.getInstance();

        // 入構重量
        gateInWeightField_.setText("0");
        // 入構日時
        gateInDateField_.setValue(c);
        // 地区
        dis_entField_.setText("");
        // 業種
        dis_entField_.setText("");
        // 事業所
        transField_.setText("");
        // 車番
        car_no_Field_.setText("");
        // ごみ種
        goodsField_.setText("");
        // 排出事業場
        dis_office_Field_.setText("");
        // 処分業者
        dis_contField_.setText("");
        // 車種
        car_kindField_.setText("");

        建設系マニフェスト番号Field_.setText("");
        産廃マニフェスト番号Field_.setText("");

        if (0 > row_) {
            columns_.put("register_date", c);
        }
    }

    private boolean checkInputItems() {

        if (!"".equals(建設系マニフェスト番号Field_.getText().trim()) && !"".equals(産廃マニフェスト番号Field_.getText().trim())) {
            JOptionPane.showMessageDialog(
                     this,
                     "交付番号は片方に入力してください。",
                     "警告",
                     JOptionPane.WARNING_MESSAGE);
            return false;
        }
        if (建設系マニフェスト番号Field_.getText().length() > 0 && 建設系マニフェスト番号Field_.getText().length() != 11) {
            JOptionPane.showMessageDialog(
                     this,
                     "建廃交付番号の入力桁数を確認してください。",
                     "警告",
                     JOptionPane.WARNING_MESSAGE);
            return false;
        }
        if (産廃マニフェスト番号Field_.getText().length() > 0 && 産廃マニフェスト番号Field_.getText().length() != 11) {
            JOptionPane.showMessageDialog(
                     this,
                     "産廃交付番号の入力桁数を確認してください。",
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
            if (source == car_no_Field_) {
                car_no_Field_.selectAll();
            } else if (source == gateInWeightField_) {
                gateInWeightField_.selectAll();

            } else if (source == office_sub_Field_) {
                office_sub_Field_.selectAll();
            }
        }

        @Override
        public void focusLost(FocusEvent event) {
            Object source = event.getSource();
            if (source == gateInDateField_) {
                columns_.put("register_date", gateInDateField_.getValue());
            } else if (source == gateInWeightField_) {
                columns_.put("register_weight", Integer.valueOf(gateInWeightField_.getText()));
            } else if (source == office_sub_Field_) {
                columns_.put("office_sub_name", office_sub_Field_.getText());
            } else if (source == car_no_Field_) {
                columns_.put("car_no", Integer.valueOf(car_no_Field_.getText()));
            } else if (source == 建設系マニフェスト番号Field_) {
                columns_.put("manifesto_no2", 建設系マニフェスト番号Field_.getText());
            } else if (source == 産廃マニフェスト番号Field_) {
                columns_.put("manifesto_no1", 産廃マニフェスト番号Field_.getText());
            }
        }
    }

    private void Last_Field_KeyPressed(java.awt.event.KeyEvent evt) {
        // TODO add your handling code here:
        // Enterでフォーカスを移動する
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            registerButton_.requestFocus();
        }
    }

    private void JTextField_KeyPressed(java.awt.event.KeyEvent evt, JTextField JTextField_) {

        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            JTextField_.transferFocus();
            evt.consume();
        }
    }

    private void JTextField_KeyPressed(java.awt.event.KeyEvent evt, JTextFieldHankaku JTextField_) {

        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            JTextField_.transferFocus();
            evt.consume();
        }
    }

    private class SelectCarAction implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent event) {
            CarSelector selector = new CarSelector(model_.getConnection());
            switch (selector.showSelector(GateInListDetailsPane.this)) {
                case CarSelector.CLEARED:
                    columns_.put("car_no", 0);
                    columns_.put("bill_id", 0);
                    columns_.put("bill_name", "");
                    columns_.put("dis_ent_id", 0);
                    columns_.put("entrepreneur_name", "");
                    columns_.put("trans_id", 0);
                    columns_.put("transporter_name", "");
                    columns_.put("goods_id", 0);
                    columns_.put("goods_name", "");
                    columns_.put("goods_option_name", "");
                    columns_.put("dis_office_id", 0);
                    columns_.put("office_name", "");
                    columns_.put("dis_cont_id", 0);
                    columns_.put("contractor_name", "");
                    columns_.put("car_kind_id", 0);
                    columns_.put("car_kind_name", "");
                    columns_.put("method_id1", 0);
                    columns_.put("method_id2", 0);
                    columns_.put("carry_class", 0);
                    columns_.put("carry_name", "搬入");

                    break;
                case CarSelector.SELECTED:
                    Map<String, Object> item = selector.selectedRow();
                    columns_.put("car_no", item.get("car_no"));
                    columns_.put("bill_id", item.get("bill_id"));
                    columns_.put("bill_name", (String) item.get("bill_name"));
                    columns_.put("dis_ent_id", item.get("dis_ent_id"));
                    columns_.put("entrepreneur_name", (String) item.get("entrepreneur_name"));
                    columns_.put("trans_id", item.get("trans_id"));
                    columns_.put("transporter_name", (String) item.get("transporter_name"));
                    columns_.put("goods_id", item.get("goods_id"));
                    columns_.put("goods_name", (String) item.get("goods_name"));
                    columns_.put("goods_option_name", (String) item.get("option1_name"));
                    columns_.put("dis_office_id", item.get("dis_office_id"));
                    columns_.put("office_name", (String) item.get("office_name"));
                    columns_.put("dis_cont_id", item.get("dis_cont_id"));
                    columns_.put("contractor_name", (String) item.get("contractor_name"));
                    columns_.put("car_kind_id", item.get("car_kind_id"));
                    columns_.put("car_kind_name", (String) item.get("car_kind_name"));
                    columns_.put("method_id1", item.get("method_id1"));
                    columns_.put("method_id2", item.get("method_id2"));
                    columns_.put("carry_class", item.get("carry_class"));
                    columns_.put("carry_name", (String) item.get("carry_name"));

                    break;
                default:
            }
            updateView();
        }
    }

    //排出事業者セレクター
    //ダイアログを開く
    private class SelectBillAction implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent event) {
            BillSelector selector = new BillSelector(model_.getConnection());
            switch (selector.showSelector(GateInListDetailsPane.this)) {
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
                // No operation.
            }
            updateView();
        }
    }

    //排出事業者セレクター
    //ダイアログを開く
    private class SelectEntAction implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent event) {
            EntSelector selector = new EntSelector(model_.getConnection());
            switch (selector.showSelector(GateInListDetailsPane.this)) {
                case EntSelector.CLEARED:
                    columns_.put("dis_ent_id", 0);
                    columns_.put("entrepreneur_name", "");
                    break;
                case EntSelector.SELECTED:
                    Map<String, Object> item = selector.selectedRow();
                    columns_.put("dis_ent_id", item.get("id"));
                    columns_.put("entrepreneur_name", (String) item.get("name"));
                    break;
                default:
            }
            updateView();
        }
    }

    private class SelectTransporterAction implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent event) {
            TransSelector selector = new TransSelector(model_.getConnection());
            switch (selector.showSelector(GateInListDetailsPane.this)) {
                case EntSelector.CLEARED:
                    columns_.put("trans_id", 0);
                    columns_.put("transporter_name", "");
                    break;
                case EntSelector.SELECTED:
                    Map<String, Object> item = selector.selectedRow();
                    columns_.put("trans_id", item.get("id"));
                    columns_.put("transporter_name", (String) item.get("name"));
                    break;
                default:
            }
            updateView();
        }
    }

    private class SelectGoodsAction implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent event) {
            GoodsSelector selector = new GoodsSelector(model_.getConnection());
            switch (selector.showSelector(GateInListDetailsPane.this)) {
                case EntSelector.CLEARED:
                    columns_.put("goods_id", 0);
                    columns_.put("goods_name", "");
                    columns_.put("carry_class", 0);
                    break;
                case EntSelector.SELECTED:
                    Map<String, Object> item = selector.selectedRow();
                    columns_.put("goods_id", item.get("id"));
                    columns_.put("goods_name", (String) item.get("name"));
                    columns_.put("carry_class", item.get("carry_class"));
                    break;
                default:
            }
            updateView();
        }
    }

    private class SelectOfficeAction implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent event) {
            OfficeSelector selector = new OfficeSelector(model_.getConnection(), (Integer) columns_.get("dis_ent_id"));
            switch (selector.showSelector(GateInListDetailsPane.this)) {
                case EntSelector.CLEARED:
                    columns_.put("dis_office_id", 0);
                    columns_.put("office_name", "");
                    break;
                case EntSelector.SELECTED:
                    Map<String, Object> item = selector.selectedRow();
                    columns_.put("dis_office_id", item.get("id"));
                    columns_.put("office_name", (String) item.get("name"));
                    break;
                default:
                // No operation.
            }
            updateView();
        }
    }

    private class SelectContAction implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent event) {
            ContSelector selector = new ContSelector(model_.getConnection());
            switch (selector.showSelector(GateInListDetailsPane.this)) {
                case EntSelector.CLEARED:
                    columns_.put("dis_cont_id", 0);
                    columns_.put("contractor_name", "");
                    break;
                case EntSelector.SELECTED:
                    Map<String, Object> item = selector.selectedRow();
                    columns_.put("dis_cont_id", item.get("id"));
                    columns_.put("contractor_name", (String) item.get("name"));
                    break;
                default:
            }
            updateView();
        }
    }

    private class SelectCarKindAction implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent event) {
            CarKindSelector selector = new CarKindSelector(model_.getConnection());
            switch (selector.showSelector(GateInListDetailsPane.this)) {
                case EntSelector.CLEARED:
                    columns_.put("car_kind_id", 0);
                    columns_.put("car_kind_name", "");
                    break;
                case EntSelector.SELECTED:
                    Map<String, Object> item = selector.selectedRow();
                    columns_.put("car_kind_id", item.get("id"));
                    columns_.put("car_kind_name", (String) item.get("name"));
                    break;
                default:
            }
            updateView();
        }
    }
}
