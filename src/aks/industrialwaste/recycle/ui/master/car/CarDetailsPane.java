package aks.industrialwaste.recycle.ui.master.car;

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
import jp.co.astax.awt.FontUtility;
import javax.swing.JButton;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import aks.industrialwaste.recycle.util.*;
import java.awt.EventQueue;
import java.awt.event.KeyEvent;
import aks.industrialwaste.database.Column;
import aks.industrialwaste.database.ColumnType;
import aks.industrialwaste.database.SortCondition;
import aks.industrialwaste.database.SortOrder;
import aks.industrialwaste.ui.DetailsPanel2;

/**
 * 車番マスタの詳細を表示する.
 *
 * @version 1.00
 * @author K.Kuroda
 */
public class CarDetailsPane extends DetailsPanel2 {

    private CarTableModel model_;
    private int row_;
    private Map<String, Object> columns_;
    private JTextField car_idField_;
    private JTextField car_noField_;
    private JTextField dis_entField_;
    private JTextField transporterField_;
    private JTextField kindField_;
    private JTextField dis_officeField_;
    private JTextField contractoryField_;
    private JTextField car_kindField_;
    private JTextField empty_weightField_;
    private JTextField max_weightField_;
    private JPanel pane;
//  private JTextField bill_idField_;
    private JTextField billField_;

    public CarDetailsPane(CarTableModel model, int row) {
        model_ = model;
        row_ = row;
        columns_ = new HashMap<>();
        copyColumns();
    }

    @Override
    protected void initComponents() {
        super.initComponents();

//    Font font14 = FontUtility.getFont("メイリオ", Font.PLAIN, 14);
        Font font14 = FontUtility.getFont("メイリオ", Font.PLAIN, 14);
        // タイトル
        titleField_.setText("管理（車両）マスタ詳細");

        // 詳細 - 左側
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createLoweredBevelBorder());
        panel.setMinimumSize(new Dimension(600, 600));
        panel.setPreferredSize(new Dimension(600, 600));

        // 車両ID
        JLabel label = new JLabel("管理ID", JLabel.LEFT);
        label.setFont(font14);
//    label.setForeground(Color.RED);  //車番
        addContainer(
                 panel, label,
                 0, 0, 1, 1, 0.1, 0.0,
                 GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
                 new Insets(2, 2, 2, 2), 0, 0);
        car_idField_ = new JTextField();
        car_idField_.setFont(font14);
//    car_idField_.setFocusable(false);
//    car_idField_.setEditable(false);
        car_idField_.addFocusListener(new ColumnUpdater());///car_idと車番を同一にする
        car_idField_.setMinimumSize(new Dimension(150, 30));
        car_idField_.setPreferredSize(new Dimension(150, 30));
        //最初のフォーカスを設定
        EventQueue.invokeLater(new Runnable() {

            @Override
            public void run() {
                car_idField_.requestFocusInWindow();
            }
        });
        addContainer(
                 panel, car_idField_,
                 1, 0, 1, 1, 0.9, 0.0,
                 GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
                 new Insets(2, 2, 2, 2), 0, 0);

        // 車番
        label = new JLabel("車番", JLabel.LEFT);
        label.setFont(font14);
//    label.setForeground(Color.RED);  //車番
        addContainer(
                 panel, label,
                 0, 1, 1, 1, 0.1, 0.0,
                 GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
                 new Insets(2, 2, 2, 2), 0, 0);
        car_noField_ = new JTextField();
        car_noField_.setFont(font14);
        car_noField_.setMinimumSize(new Dimension(150, 30));
        car_noField_.setPreferredSize(new Dimension(150, 30));
        car_noField_.addFocusListener(new ColumnUpdater());
        car_noField_.addKeyListener(new java.awt.event.KeyAdapter() {

            @Override
            //頭にカーソル表示
            public void keyPressed(java.awt.event.KeyEvent evt) {
                JTextField_KeyPressed(evt, car_noField_);
            }
        });
        //最初のフォーカスを設定
//      EventQueue.invokeLater(new Runnable() {
//
//          @Override
//          public void run() {
//              car_noField_.requestFocusInWindow();
//          }
//      });
        addContainer(
                 panel, car_noField_,
                 1, 1, 1, 1, 0.9, 0.0,
                 GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
                 new Insets(2, 2, 2, 2), 0, 0);

        // 請求先
        label = new JLabel("請求先", JLabel.LEFT);
        label.setFont(font14);
//    label.setForeground(Color.RED);
//    addContainer(
//      panel, label,
//      0, 2, 1, 1, 0.0, 0.0,
//      GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
//      new Insets(2, 2, 2, 2), 0, 0);
        billField_ = new JTextField();
        billField_.setFont(font14);
        billField_.setFocusable(false);
        billField_.setEditable(false);
        billField_.setMinimumSize(new Dimension(150, 30));
        billField_.setPreferredSize(new Dimension(150, 30));
        billField_.addFocusListener(new ColumnUpdater());
//    addContainer(
//      panel, billField_,
//      1, 2, 1, 1, 0.0, 0.0,
//      GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
//      new Insets(2, 2, 2, 2), 0, 0);
        JButton btn = new JButton("選択");
        btn.setFont(font14);
        btn.setFocusable(false);
        btn.setMargin(new Insets(2, 2, 2, 2));
        btn.setMinimumSize(new Dimension(70, 30));
        btn.setPreferredSize(new Dimension(70, 30));
        btn.addActionListener(new SelectBillAction());
//    addContainer(
//      panel, btn,
//      2, 2, 1, 1, 0.0, 0.0,
//      GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
//      new Insets(2, 2, 2, 2), 0, 0);

        // 排出事業者
        label = new JLabel("排出事業者", JLabel.LEFT);
        label.setFont(font14);
//    label.setForeground(Color.RED);
        addContainer(
                 panel, label,
                 0, 3, 1, 1, 0.0, 0.0,
                 GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
                 new Insets(2, 2, 2, 2), 0, 0);
        dis_entField_ = new JTextField();
        dis_entField_.setFont(font14);
        //選択させない///////////////////////
        dis_entField_.setFocusable(false);//
        dis_entField_.setEditable(false); //
        ////////////////////////////////////
        dis_entField_.setMinimumSize(new Dimension(150, 30));
        dis_entField_.setPreferredSize(new Dimension(150, 30));
        dis_entField_.addFocusListener(new ColumnUpdater());
        addContainer(
                 panel, dis_entField_,
                 1, 3, 1, 1, 0.0, 0.0,
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
                 2, 3, 1, 1, 0.0, 0.0,
                 GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
                 new Insets(2, 2, 2, 2), 0, 0);

        // 運搬業者
        label = new JLabel("運搬業者", JLabel.LEFT);
        label.setFont(font14);
//    label.setForeground(Color.RED);
        addContainer(
                 panel, label,
                 0, 4, 1, 1, 0.0, 0.0,
                 GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
                 new Insets(2, 2, 2, 2), 0, 0);
        transporterField_ = new JTextField();
        transporterField_.setFont(font14);
        transporterField_.setFocusable(false);
        transporterField_.setEditable(false);
        transporterField_.setMinimumSize(new Dimension(150, 30));
        transporterField_.setPreferredSize(new Dimension(150, 30));
        transporterField_.addFocusListener(new ColumnUpdater());
        addContainer(
                 panel, transporterField_,
                 1, 4, 1, 1, 0.0, 0.0,
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
                 2, 4, 1, 1, 0.0, 0.0,
                 GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
                 new Insets(2, 2, 2, 2), 0, 0);

        //品目
        label = new JLabel("品目", JLabel.LEFT);
        label.setFont(font14);
        addContainer(
                 panel, label,
                 0, 5, 1, 1, 0.0, 0.0,
                 GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
                 new Insets(2, 2, 2, 2), 0, 0);

        kindField_ = new JTextField();
        kindField_.setFont(font14);
        kindField_.setFocusable(false);
        kindField_.setEditable(false);
        kindField_.setMinimumSize(new Dimension(150, 30));
        kindField_.setPreferredSize(new Dimension(150, 30));
        kindField_.addFocusListener(new ColumnUpdater());
        addContainer(
                 panel, kindField_,
                 1, 5, 1, 1, 0.0, 0.0,
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
                 2, 5, 1, 1, 0.0, 0.0,
                 GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
                 new Insets(2, 2, 2, 2), 0, 0);

        // 排出事業場
        label = new JLabel("排出事業場", JLabel.LEFT);
        label.setFont(font14);
//    label.setForeground(Color.RED);
        addContainer(
                 panel, label,
                 0, 6, 1, 1, 0.0, 0.0,
                 GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
                 new Insets(2, 2, 2, 2), 0, 0);
        dis_officeField_ = new JTextField();
        dis_officeField_.setFont(font14);
        dis_officeField_.setFocusable(false);
        dis_officeField_.setEditable(false);
        dis_officeField_.setMinimumSize(new Dimension(150, 30));
        dis_officeField_.setPreferredSize(new Dimension(150, 30));
        dis_officeField_.addFocusListener(new ColumnUpdater());
        addContainer(
                 panel, dis_officeField_,
                 1, 6, 1, 1, 0.0, 0.0,
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
                 2, 6, 1, 1, 0.0, 0.0,
                 GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
                 new Insets(2, 2, 2, 2), 0, 0);

        // 処分業者
        label = new JLabel("処分業者", JLabel.LEFT);
        label.setFont(font14);
//    label.setForeground(Color.RED);
        addContainer(
                 panel, label,
                 0, 7, 1, 1, 0.0, 0.0,
                 GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
                 new Insets(2, 2, 2, 2), 0, 0);
        contractoryField_ = new JTextField();
        contractoryField_.setFont(font14);
        contractoryField_.setFocusable(false);
        contractoryField_.setEditable(false);
        contractoryField_.setMinimumSize(new Dimension(150, 30));
        contractoryField_.setPreferredSize(new Dimension(150, 30));
        contractoryField_.addFocusListener(new ColumnUpdater());
        addContainer(
                 panel, contractoryField_,
                 1, 7, 1, 1, 0.0, 0.0,
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
                 2, 7, 1, 1, 0.0, 0.0,
                 GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
                 new Insets(2, 2, 2, 2), 0, 0);

        // 車種
        label = new JLabel("車種", JLabel.LEFT);
        label.setFont(font14);
//    label.setForeground(Color.RED);
        addContainer(
                 panel, label,
                 0, 8, 1, 1, 0.0, 0.0,
                 GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
                 new Insets(2, 2, 2, 2), 0, 0);
        car_kindField_ = new JTextField();
        car_kindField_.setFont(font14);
        car_kindField_.setFocusable(false);
        car_kindField_.setEditable(false);
        car_kindField_.setMinimumSize(new Dimension(150, 30));
        car_kindField_.setPreferredSize(new Dimension(150, 30));
        car_kindField_.addFocusListener(new ColumnUpdater());
        addContainer(
                 panel, car_kindField_,
                 1, 8, 1, 1, 0.0, 0.0,
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
                 2, 8, 1, 1, 0.0, 0.0,
                 GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
                 new Insets(2, 2, 2, 2), 0, 0);

        // 風袋重量
        label = new JLabel("風袋重量", JLabel.LEFT);
        label.setFont(font14);
//    label.setForeground(Color.RED);
        addContainer(
                 panel, label,
                 0, 9, 1, 1, 0.0, 0.0,
                 GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
                 new Insets(2, 2, 2, 2), 0, 0);
        empty_weightField_ = new JTextField();
        empty_weightField_.setFont(font14);
//    empty_weightField_.setFocusable(false);
//    empty_weightField_.setEditable(false);
        empty_weightField_.setMinimumSize(new Dimension(150, 30));
        empty_weightField_.setPreferredSize(new Dimension(150, 30));
        empty_weightField_.addFocusListener(new ColumnUpdater());
//    empty_weightField_.addKeyListener(new java.awt.event.KeyAdapter() {
//            @Override
//            public void keyPressed(java.awt.event.KeyEvent evt) {
//                Last_Field_KeyPressed(evt);
//            }
//        });
        addContainer(
                 panel, empty_weightField_,
                 1, 9, 1, 1, 0.0, 0.0,
                 GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
                 new Insets(2, 2, 2, 2), 0, 0);
        //max_weightField_
        label = new JLabel("最大積載量", JLabel.LEFT);
        label.setFont(font14);
//    label.setForeground(Color.RED);
        addContainer(
                 panel, label,
                 0, 10, 1, 1, 0.0, 0.0,
                 GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
                 new Insets(2, 2, 2, 2), 0, 0);
        max_weightField_ = new JTextField();
        max_weightField_.setFont(font14);
//    empty_weightField_.setFocusable(false);
//    empty_weightField_.setEditable(false);
        max_weightField_.setMinimumSize(new Dimension(150, 30));
        max_weightField_.setPreferredSize(new Dimension(150, 30));
        max_weightField_.addFocusListener(new ColumnUpdater());
        max_weightField_.addKeyListener(new java.awt.event.KeyAdapter() {
            @Override
            public void keyPressed(java.awt.event.KeyEvent evt) {
                Last_Field_KeyPressed(evt);
            }
        });
        addContainer(
                 panel, max_weightField_,
                 1, 10, 1, 1, 0.0, 0.0,
                 GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
                 new Insets(2, 2, 2, 2), 0, 0);

        //
//    label = new JLabel("※車両ID = 車種ID + 車番によって5桁または6桁で自動作成されます。", JLabel.LEFT);
//    label.setFont(font14);
//    label.setForeground(Color.RED);
//    addContainer(
//      panel, label,
//      0, 10, 3, 1, 0.0, 0.0,
//      GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
//      new Insets(2, 2, 2, 2), 0, 0);
        // ダミー
        addContainer(
                 panel, new JPanel(),
                 0, 11, 1, 1, 0.0, 1.0,
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

//        try {
//            //重複登録の確認
//            String s = "select count(*)as cnt from v0_car "
//                    + " where v0_car.car_no = ?"
//                    + " and v0_car.dis_ent_id = ?"
//                    + " and v0_car.trans_id = ?"
//                    + " and v0_car.goods_id = ?"
//                    + " and v0_car.dis_office_id = ?"
//                    + " and v0_car.dis_cont_id = ?"
//                    + " and v0_car.car_kind_id = ?"
//                    + " and v0_car.empty_weight = ?;";
//
//            PreparedStatement ps = model_.getConnection().prepareStatement(s);
//            try {
//                ps.setObject(1, columns_.get("car_no"));
//                ps.setObject(2, columns_.get("dis_ent_id"));
//                ps.setObject(3, columns_.get("trans_id"));
//                ps.setObject(4, columns_.get("goods_id"));
//                ps.setObject(5, columns_.get("dis_office_id"));
//                ps.setObject(6, columns_.get("dis_cont_id"));
//                ps.setObject(7, columns_.get("car_kind_id"));
//                ps.setObject(8, columns_.get("empty_weight"));
//
//                ResultSet rs = ps.executeQuery();
//                while (rs.next()) {
//                    if (rs.getInt("cnt") > 0) {
//
//                        JOptionPane.showMessageDialog(
//                                this,
//                                "データが重複しています。",
//                                "警告",
//                                JOptionPane.WARNING_MESSAGE);
//                        return;
//                    }
//
//                }
//            } finally {
//                ps.close();
//            }
//        } catch (SQLException e) {
//            System.out.println(e.toString());
//        }
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
//      throw new UnsupportedOperationException("m_garbage: insert operation");
            CarJournal.recordJournal(
                     model_.getConnection(),
                     "車番マスタ詳細 - 追加",
                     columns_);
            if (model_.insert(columns_)) {
                doClose();

                //リストの再描画
                SortCondition[] sort
                         = new SortCondition[]{
                             new SortCondition(
                                      new Column("車番", "car_id", ColumnType.INT4),
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
            CarJournal.recordJournal(
                     model_.getConnection(),
                     "車番マスタ詳細 - 訂正",
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
                              new Column("車番", "car_id", ColumnType.INT4),
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
        Object o = columns_.get("car_no");
        if (o instanceof Number) {
            car_noField_.setText(String.valueOf(o));
        }

        o = columns_.get("car_id");
        if (o instanceof Number) {
            car_idField_.setText(String.valueOf(o));
        }

//    car_noField_.setText(String.valueOf(columns_.get("car_no")));
        dis_entField_.setText((String) columns_.get("entrepreneur_name"));
        transporterField_.setText((String) columns_.get("transporter_name"));
        kindField_.setText((String) columns_.get("goods_name"));
        dis_officeField_.setText((String) columns_.get("office_name"));
        contractoryField_.setText((String) columns_.get("contractor_name"));
        car_kindField_.setText((String) columns_.get("car_kind_name"));
        //20120604 請求先追加
        billField_.setText((String) columns_.get("bill_name"));

        o = columns_.get("empty_weight");
        if (o instanceof Number) {
            empty_weightField_.setText(String.valueOf(o));
        }
        //max_weightField_
        o = columns_.get("max_weight");
        if (o instanceof Number) {
            max_weightField_.setText(String.valueOf(o));
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
            columns_.put("dis_ent_id", 0);
            columns_.put("trans_id", 0);
            columns_.put("goods_id", 0);
            columns_.put("dis_office_id", 0);
            columns_.put("dis_cont_id", 0);
            columns_.put("car_kind_id", 0);
            columns_.put("empty_weight", 0);
            columns_.put("max_weight", 0);
            //20120604 請求先追加
            columns_.put("bill_id", 0);
            columns_.put("car_no", 0);
            columns_.put("car_id", 0);
        }
    }

    private void clearField() {
        car_noField_.setText("0");
        car_idField_.setText("0");
        dis_entField_.setText("");
        transporterField_.setText("");
        kindField_.setText("");
        dis_officeField_.setText("");
        contractoryField_.setText("");
        empty_weightField_.setText("0");
        //20120604 請求先追加
        billField_.setText("");

    }

    private boolean checkInputItems() {
        if ("".equals(car_noField_.getText().trim())) {
            JOptionPane.showMessageDialog(
                     this,
                     "車番を入力してください.",
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
            if (source == car_noField_) {
                car_noField_.selectAll();
            } else if (source == empty_weightField_) {
                empty_weightField_.selectAll();

            } else if (source == car_idField_) {
                car_idField_.selectAll();

            }
        }

        @Override
        public void focusLost(FocusEvent event) {
            Object source = event.getSource();
            if (source == car_noField_) {
                columns_.put("car_no", Integer.valueOf(car_noField_.getText().trim()));
            }
            if (source == dis_entField_) {
                columns_.put("dis_ent", dis_entField_.getText().trim());
            }
            if (source == transporterField_) {
                columns_.put("transporter", transporterField_.getText().trim());
            }
            if (source == kindField_) {
                columns_.put("goods_kind", kindField_.getText().trim());
            }
            if (source == dis_officeField_) {
                columns_.put("dis_office", dis_officeField_.getText().trim());
            }
            if (source == contractoryField_) {
                columns_.put("contractory", contractoryField_.getText().trim());
            }
            if (source == empty_weightField_) {
                columns_.put("empty_weight", Integer.valueOf((String) empty_weightField_.getText().trim()));
            }
            if (source == car_idField_) {
                columns_.put("car_id", Integer.valueOf(car_idField_.getText().trim()));
            }
            if (source == max_weightField_) {
                columns_.put("max_weight", Integer.valueOf((String) max_weightField_.getText().trim()));
            }

        }
    }

//20120604 請求先追加
    private class SelectBillAction implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent event) {
            BillSelector selector = new BillSelector(model_.getConnection());
            switch (selector.showSelector(CarDetailsPane.this)) {
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

    private class SelectEntAction implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent event) {
            EntSelector selector = new EntSelector(model_.getConnection());
            switch (selector.showSelector(CarDetailsPane.this)) {
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
                // No operation.
            }
            updateView();
        }
    }

    private class SelectTransporterAction implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent event) {
            TransSelector selector = new TransSelector(model_.getConnection());
            switch (selector.showSelector(CarDetailsPane.this)) {
                case TransSelector.CLEARED:
                    columns_.put("trans_id", 0);
                    columns_.put("transporter_name", "");
                    break;
                case TransSelector.SELECTED:
                    Map<String, Object> item = selector.selectedRow();
                    columns_.put("trans_id", item.get("id"));
                    columns_.put("transporter_name", (String) item.get("name"));
                    break;
                default:
                // No operation.
            }
            updateView();
        }
    }

    private class SelectGoodsAction implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent event) {
            GoodsSelector selector = new GoodsSelector(model_.getConnection());
            switch (selector.showSelector(CarDetailsPane.this)) {
                case GoodsSelector.CLEARED:
                    columns_.put("goods_id", 0);
                    columns_.put("goods_name", "");
                    break;
                case GoodsSelector.SELECTED:
                    Map<String, Object> item = selector.selectedRow();
                    columns_.put("goods_id", item.get("id"));
                    columns_.put("goods_name", (String) item.get("name"));
                    break;
                default:
                // No operation.
            }
            updateView();
        }
    }

    private class SelectOfficeAction implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent event) {
            OfficeSelector selector = new OfficeSelector(model_.getConnection(), (Integer) columns_.get("dis_ent_id"));
            switch (selector.showSelector(CarDetailsPane.this)) {
                case OfficeSelector.CLEARED:
                    columns_.put("dis_office_id", 0);
                    columns_.put("office_name", "");
                    break;
                case OfficeSelector.SELECTED:
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
            switch (selector.showSelector(CarDetailsPane.this)) {
                case ContSelector.CLEARED:
                    columns_.put("dis_cont_id", 0);
                    columns_.put("contractor_name", "");
                    break;
                case ContSelector.SELECTED:
                    Map<String, Object> item = selector.selectedRow();
                    columns_.put("dis_cont_id", item.get("id"));
                    columns_.put("contractor_name", (String) item.get("name"));
                    break;
                default:
                // No operation.
            }
            updateView();
        }
    }

    private class SelectCarKindAction implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent event) {
            CarKindSelector selector = new CarKindSelector(model_.getConnection());
            switch (selector.showSelector(CarDetailsPane.this)) {
                case ContSelector.CLEARED:
                    columns_.put("car_kind_id", 0);
                    columns_.put("car_kind_name", "");
                    break;
                case ContSelector.SELECTED:
                    Map<String, Object> item = selector.selectedRow();
                    columns_.put("car_kind_id", item.get("id"));
                    columns_.put("car_kind_name", (String) item.get("name"));
                    break;
                default:
                // No operation.
            }
            updateView();
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

}
