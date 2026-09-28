package aks.industrialwaste.recycle.ui.bill_report;

import aks.industrialwaste.database.DBManager;
import aks.industrialwaste.recycle.ui.MainFrame;
import aks.industrialwaste.recycle.util.BillSelector;
import aks.industrialwaste.ui.Panel;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.util.Calendar;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.border.TitledBorder;
import aks.swing.JTextField;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;
import jp.co.astax.awt.FontUtility;
import jp.co.astax.swing.DateTimeField;

/**
 * 請求締め処理パネル操作をするためのパネル.
 */
public class BillMaintePanel extends Panel {

    private Connection con_;
    private DateTimeField startField_;
    private DateTimeField endField_;
    private JButton printButton_;
    private JTextField idField_;
    private JTextField nameField_;
    public long mLastClickTime = System.currentTimeMillis();
    public int mClickCount = 0;

    boolean bo = false;

    public BillMaintePanel(Connection con) {
        con_ = con;
    }

    @Override
    protected void initComponents() {
        super.initComponents();

        Font font14 = FontUtility.getFont("メイリオ", Font.PLAIN, 14);
        Font font18 = FontUtility.getFont("メイリオ", Font.PLAIN, 18);
        // タイトルは非表示
        titleField_.setVisible(false);

        centerPane_.setBorder(
                 BorderFactory.createTitledBorder(
                          BorderFactory.createRaisedBevelBorder(),
                          "請求日修正",
                          TitledBorder.LEFT,
                          TitledBorder.DEFAULT_POSITION,
                          FontUtility.getFont("メイリオ", Font.PLAIN, 24)));

        // 集計日
        JLabel label = new JLabel("伝票発行日", JLabel.LEFT);
        label.setFont(font18);
        addContainer(
                 centerPane_, label,
                 0, 0, 1, 1, 0.5, 0.0,
                 GridBagConstraints.SOUTHWEST, GridBagConstraints.HORIZONTAL,
                 new Insets(2, 2, 2, 2), 0, 0);
        startField_ = new DateTimeField(DateTimeField.DateFormat.YMD);
        startField_.setFont(font18);
        startField_.setPreferredSize(new Dimension(250, 35));
        startField_.setMinimumSize(startField_.getPreferredSize());
        addContainer(
                 centerPane_, startField_,
                 0, 1, 2, 1, 0.0, 0.0,
                 GridBagConstraints.NORTHWEST, GridBagConstraints.NONE,
                 new Insets(2, 2, 2, 2), 0, 0);

        endField_ = new DateTimeField(DateTimeField.DateFormat.YMD);
        endField_.setFont(font18);
        endField_.setPreferredSize(new Dimension(250, 35));
        endField_.setMinimumSize(endField_.getPreferredSize());
        addContainer(
                 centerPane_, endField_,
                 2, 1, 2, 1, 0.0, 0.0,
                 GridBagConstraints.NORTHWEST, GridBagConstraints.NONE,
                 new Insets(2, 2, 2, 2), 0, 0);

        label = new JLabel("請求先指定", JLabel.LEFT);
        label.setFont(font18);
        addContainer(
                 centerPane_, label,
                 0, 3, 1, 1, 0.0, 0.0,
                 GridBagConstraints.SOUTHWEST, GridBagConstraints.HORIZONTAL,
                 new Insets(2, 2, 2, 2), 0, 0);

        idField_ = new JTextField();
        idField_.setFont(font18);
        idField_.setMargin(new Insets(0, 0, 0, 0));
        idField_.setMinimumSize(new Dimension(100, 35));
        idField_.setPreferredSize(new Dimension(100, 35));
        idField_.addActionListener(new SearchAction());
        idField_.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mousePressed(java.awt.event.MouseEvent evt) {
                showBillSelector();
            }
        });
        addContainer(
                 centerPane_, idField_,
                 1, 3, 1, 1, 0.0, 0.0,
                 GridBagConstraints.WEST, GridBagConstraints.NONE,
                 new Insets(2, 2, 2, 2), 0, 0);

        nameField_ = new JTextField();
        nameField_.setFont(font18);
        nameField_.setEditable(false);
        nameField_.setFocusable(false);
        nameField_.setMargin(new Insets(0, 0, 0, 0));
        nameField_.setMinimumSize(new Dimension(300, 35));
        nameField_.setPreferredSize(new Dimension(300, 35));
        addContainer(
                 centerPane_, nameField_,
                 2, 3, 2, 1, 0.0, 0.0,
                 GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
                 new Insets(2, 2, 2, 2), 0, 0);

        // 請求締め
        printButton_ = new JButton("請求日修正");
        printButton_.setFont(font14);
        printButton_.setMargin(new Insets(2, 2, 2, 2));
        printButton_.setPreferredSize(new Dimension(130, 35));
        printButton_.setMinimumSize(printButton_.getPreferredSize());
        printButton_.addActionListener(
                 new ActionListener() {

            public void actionPerformed(ActionEvent event) {
                doMainte();
            }
        });
        addContainer(
                 centerPane_, printButton_,
                 0, 4, 1, 1, 1.0, 1.0,
                 GridBagConstraints.SOUTHWEST, GridBagConstraints.NONE,
                 new Insets(2, 2, 2, 2), 0, 0);

        idField_.setText("0");
        nameField_.setText("");

        Calendar c = Calendar.getInstance();
        c.set(Calendar.DAY_OF_MONTH, 1);
        startField_.setValue(c);
        c.set(Calendar.DAY_OF_MONTH, c.getActualMaximum(Calendar.DATE));
        endField_.setValue(c);
    }

    private void doMainte() {
        Calendar c1 = startField_.getValue();
        Calendar c2 = endField_.getValue();

        int id = 0;

        try {
            id = Integer.valueOf(idField_.getText());
        } catch (Exception e) {
            JOptionPane.showMessageDialog(
                     this,
                     "得意先NOは数字で入力して下さい。",
                     "警告",
                     JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {

            String s = "update d_bill_detail set bill_date = check_end_date(term_date::date, ?), "
                     + " term_month = date_trunc('month', check_end_date(term_date::date, ?)) "
                     + " where bill_id = ? and term_date >= ? and term_date <= ? ;";
            PreparedStatement ps01 = MainFrame.con1_.prepareStatement(s);
            try {
                ps01.setObject(1, id);
                ps01.setObject(2, id);
                ps01.setObject(3, id);
                ps01.setObject(4, DBManager.convert(c1.getTime()));
                ps01.setObject(5, DBManager.convert(c2.getTime()));
                ps01.executeUpdate();

                s = "update d_bill set "
                         + " term_date = check_end_date(term_month::date, ?) "
                         + " where bill_id = ? and term_date >= ? and term_date <= ? ;";
                ps01 = MainFrame.con1_.prepareStatement(s);
                ps01.setObject(1, id);
                ps01.setObject(2, id);
                ps01.setObject(3, DBManager.convert(c1.getTime()));
                ps01.setObject(4, DBManager.convert(c2.getTime()));
                ps01.executeUpdate();

            } finally {
                ps01.close();
            }

            JOptionPane.showMessageDialog(
                     this,
                     "【請求日修正処理】正常終了しました。",
                     "確認",
                     JOptionPane.INFORMATION_MESSAGE);

        } catch (Exception e) {

            JOptionPane.showMessageDialog(
                     this,
                     "【請求日修正処理】異常が発生しました.",
                     "警告",
                     JOptionPane.WARNING_MESSAGE);
            logger_.warning(e.toString());
        }
    }

    private class SearchAction implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent event) {

            try {
                int id = Integer.valueOf(idField_.getText());
                String id_name = updateView(id);
                if (id_name != null) {
                    nameField_.setText(id_name);
                }
            } catch (Exception e) {
            }

        }
    }

    private String updateView(int id_) {
        String ret = "";

        //得意先検索
        try {

            String s = "select * from v0_bill "
                     + " where v0_bill.id = ?;";

            PreparedStatement ps = con_.prepareStatement(s);
            try {
                ps.setObject(1, id_);
                ResultSet rs = ps.executeQuery();

                if (rs.next()) {
                    ret = rs.getString("name");
                }

            } finally {
                ps.close();
            }
        } catch (SQLException e) {

        }

        return ret;
    }

    //請求先セレクター
    //ダイアログを開く
    private void showBillSelector() {

        long now_time = System.currentTimeMillis();
        long gap_time = now_time - mLastClickTime;
        if (gap_time < 500) {
            mClickCount++;
        } else {
            mClickCount = 1;
        }

        if (mClickCount >= 2) {
            BillSelector selector = new BillSelector(con_);
            switch (selector.showSelector(BillMaintePanel.this)) {
                case BillSelector.CLEARED:
                    idField_.setText("");
                    nameField_.setText("");
                    break;
                case BillSelector.SELECTED:
                    Map<String, Object> item = selector.selectedRow();
                    idField_.setText(String.valueOf((Integer) item.get("id")));
                    nameField_.setText((String) item.get("name"));
                    break;
                default:
            }
            updateView();
        }
        mLastClickTime = now_time;
    }
}
