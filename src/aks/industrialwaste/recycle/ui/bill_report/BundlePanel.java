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
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Map;
import javax.swing.ButtonGroup;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import jp.co.astax.awt.FontUtility;
import jp.co.astax.swing.DateTimeField;

/**
 * 請求締め処理パネル操作をするためのパネル.
 */
public class BundlePanel extends Panel {

    private Connection con_;
    private DateTimeField timeField_, start_timeField_, end_timeField_;
    private JRadioButton report1Switch_;
    private JRadioButton report2Switch_;
    private JButton printButton_;
    private ButtonGroup bg;
    private JTextField idField_;
    private JTextField nameField_;
    private String[] bundle_;
    private JComboBox bundleField_;
    public long mLastClickTime = System.currentTimeMillis();
    public int mClickCount = 0;

    boolean bo = false;

    public BundlePanel(Connection con) {
        con_ = con;
    }

    @Override
    protected void initComponents() {
        super.initComponents();

        Font font14 = FontUtility.getFont("メイリオ", Font.PLAIN, 14);
        Font font18 = FontUtility.getFont("メイリオ", Font.PLAIN, 18);
        // タイトルは非表示
        titleField_.setVisible(false);
        bg = new ButtonGroup();
        bundle_ = new String[]{"末", "5", "10", "15", "20", "25"};

        centerPane_.setBorder(
                 BorderFactory.createTitledBorder(
                          BorderFactory.createRaisedBevelBorder(),
                          //                "請求締め及び請求書印刷",
                          "請求締め",
                          TitledBorder.LEFT,
                          TitledBorder.DEFAULT_POSITION,
                          FontUtility.getFont("メイリオ", Font.PLAIN, 24)));

        // 集計日
        JLabel label = new JLabel("集計月", JLabel.LEFT);
        label.setFont(font18);
        addContainer(
                 centerPane_, label,
                 0, 0, 1, 1, 0.5, 0.0,
                 GridBagConstraints.SOUTHWEST, GridBagConstraints.HORIZONTAL,
                 new Insets(2, 2, 2, 2), 0, 0);
        timeField_ = new DateTimeField(DateTimeField.DateFormat.YM);
        timeField_.setFont(font18);
        timeField_.setMinimumSize(new Dimension(200, 35));
        timeField_.setPreferredSize(new Dimension(200, 35));
        timeField_.setMinimumSize(timeField_.getPreferredSize());

        addContainer(
                 centerPane_, timeField_,
                 0, 1, 1, 1, 0.0, 0.0,
                 GridBagConstraints.NORTHWEST, GridBagConstraints.NONE,
                 new Insets(2, 2, 2, 2), 0, 0);
        label = new JLabel("起算日指定", JLabel.LEFT);
        label.setFont(font18);
        addContainer(
                 centerPane_, label,
                 0, 2, 1, 1, 0.5, 0.0,
                 GridBagConstraints.SOUTHWEST, GridBagConstraints.HORIZONTAL,
                 new Insets(2, 2, 2, 2), 0, 0);
        start_timeField_ = new DateTimeField(DateTimeField.DateFormat.YMD);
        start_timeField_.setFont(font18);
        start_timeField_.setMinimumSize(new Dimension(200, 35));
        start_timeField_.setPreferredSize(new Dimension(200, 35));
        start_timeField_.setMinimumSize(start_timeField_.getPreferredSize());
        addContainer(
                 centerPane_, start_timeField_,
                 0, 3, 2, 1, 0.0, 0.0,
                 GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL,
                 new Insets(2, 2, 2, 2), 0, 0);
        label = new JLabel("起算開始日", JLabel.LEFT);
        label.setFont(font18);
        addContainer(
                 centerPane_, label,
                 2, 3, 1, 1, 0.0, 0.0,
                 GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
                 new Insets(2, 2, 2, 2), 0, 0);
        end_timeField_ = new DateTimeField(DateTimeField.DateFormat.YMD);
        end_timeField_.setFont(font18);
        end_timeField_.setMinimumSize(new Dimension(200, 35));
        end_timeField_.setPreferredSize(new Dimension(200, 35));
        end_timeField_.setMinimumSize(end_timeField_.getPreferredSize());
        addContainer(
                 centerPane_, end_timeField_,
                 0, 4, 2, 1, 0.0, 0.0,
                 GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL,
                 new Insets(2, 2, 2, 2), 0, 0);
        label = new JLabel("起算終了日", JLabel.LEFT);
        label.setFont(font18);
        addContainer(
                 centerPane_, label,
                 2, 4, 1, 1, 0.0, 0.0,
                 GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
                 new Insets(2, 2, 2, 2), 0, 0);

        // 帳票選択
        report1Switch_ = new JRadioButton("全請求先");
        report1Switch_.setFont(font18);
        addContainer(
                 centerPane_, report1Switch_,
                 0, 7, 2, 1, 1.0, 0.0,
                 GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
                 new Insets(2, 2, 2, 2), 0, 0);
        label = new JLabel("締め日", JLabel.RIGHT);
        label.setFont(font18);
        addContainer(
                 centerPane_, label,
                 1, 1, 1, 1, 0.0, 0.0,
                 GridBagConstraints.EAST, GridBagConstraints.NONE,
                 new Insets(2, 2, 2, 2), 0, 0);
        bundleField_ = new JComboBox();
        bundleField_.setFont(font18);
        bundleField_.setMinimumSize(new Dimension(100, 35));
        bundleField_.setPreferredSize(new Dimension(100, 35));
        bundleField_.setModel(new javax.swing.DefaultComboBoxModel(bundle_));
        bundleField_.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                change_bundleField_();
            }
        });
        addContainer(
                 centerPane_, bundleField_,
                 2, 1, 1, 1, 0.0, 0.0,
                 GridBagConstraints.EAST, GridBagConstraints.NONE,
                 new Insets(2, 2, 2, 2), 0, 0);

        report2Switch_ = new JRadioButton("請求先指定");
        report2Switch_.setFont(font18);
        addContainer(
                 centerPane_, report2Switch_,
                 0, 5, 1, 1, 0.0, 0.0,
                 GridBagConstraints.WEST, GridBagConstraints.NONE,
                 new Insets(2, 2, 2, 2), 0, 0);

        idField_ = new JTextField();
        idField_.setFont(font18);
        idField_.setMargin(new Insets(0, 0, 0, 0));
        idField_.setMinimumSize(new Dimension(80, 35));
        idField_.setPreferredSize(new Dimension(80, 35));
        idField_.addActionListener(new SearchAction());
        idField_.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mousePressed(java.awt.event.MouseEvent evt) {
                showBillSelector();
            }
        });
        addContainer(
                 centerPane_, idField_,
                 0, 5, 1, 1, 0.0, 0.0,
                 GridBagConstraints.EAST, GridBagConstraints.NONE,
                 new Insets(2, 2, 2, 2), 0, 0);

        // ダミー
        addContainer(
                 centerPane_, new JPanel(),
                 1, 5, 1, 1, 1.0, 0.0,
                 GridBagConstraints.CENTER, GridBagConstraints.BOTH,
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
                 0, 6, 3, 1, 0.0, 0.0,
                 GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
                 new Insets(2, 2, 2, 2), 0, 0);

        // 請求締め
        printButton_ = new JButton("請求締め");
        printButton_.setFont(font14);
        printButton_.setMargin(new Insets(2, 2, 2, 2));
        printButton_.setPreferredSize(new Dimension(130, 35));
        printButton_.setMinimumSize(printButton_.getPreferredSize());
        printButton_.addActionListener(
                 new ActionListener() {

            public void actionPerformed(ActionEvent event) {
                doBundle();
            }
        });
        addContainer(
                 centerPane_, printButton_,
                 0, 10, 1, 1, 0.0, 1.0,
                 GridBagConstraints.SOUTHWEST, GridBagConstraints.NONE,
                 new Insets(2, 2, 2, 2), 0, 0);

        // 合計請求書ボタン
        printButton_ = new JButton("合計請求書");
        printButton_.setFont(font14);
        printButton_.setMargin(new Insets(2, 2, 2, 2));
        printButton_.setPreferredSize(new Dimension(130, 35));
        printButton_.setMinimumSize(printButton_.getPreferredSize());
        printButton_.addActionListener(
                 new ActionListener() {

            public void actionPerformed(ActionEvent event) {
                bo = true;
                doPrint();
            }
        });
        // 請求書ボタン
        printButton_ = new JButton("請求書");
        printButton_.setFont(font14);
        printButton_.setMargin(new Insets(2, 2, 2, 2));
        printButton_.setPreferredSize(new Dimension(130, 35));
        printButton_.setMinimumSize(printButton_.getPreferredSize());
        printButton_.addActionListener(
                 new ActionListener() {

            public void actionPerformed(ActionEvent event) {
                doPrint();
            }
        });

        bg.add(report1Switch_);
        bg.add(report2Switch_);
        report2Switch_.setSelected(true);

        idField_.setText("0");
        nameField_.setText("");

        timeField_.setValue(Calendar.getInstance());
        Calendar c1 = Calendar.getInstance();
        Calendar c2 = Calendar.getInstance();
        c1.set(Calendar.DAY_OF_MONTH, 1);
        start_timeField_.setValue(c1);

        c2.set(Calendar.DAY_OF_MONTH, 1);
        c2.add(Calendar.MONTH, 1);
        c2.add(Calendar.DATE, -1);
        end_timeField_.setValue(c2);

    }

    private void change_bundleField_() {

        //締日のコンボボックスを変更した際に
        //起算日を変更する
        Calendar c0 = timeField_.getValue();
        Calendar c1 = (Calendar) c0.clone();
        Calendar c2 = (Calendar) c0.clone();
        if (bundleField_.getSelectedIndex() == 0) {
            //末締め
            c1.set(Calendar.DAY_OF_MONTH, 1);
            c2.set(Calendar.DAY_OF_MONTH, 1);
            c2.add(Calendar.MONTH, 1);
            c2.add(Calendar.DATE, -1);
        } else if (bundleField_.getSelectedIndex() == 1) {
            //5
            c1.set(Calendar.DAY_OF_MONTH, 1);
            c1.add(Calendar.MONTH, -1);
            c1.add(Calendar.DATE, +5);
            c2.set(Calendar.DAY_OF_MONTH, 5);
        } else if (bundleField_.getSelectedIndex() == 2) {
            //10
            c1.set(Calendar.DAY_OF_MONTH, 1);
            c1.add(Calendar.MONTH, -1);
            c1.add(Calendar.DATE, +10);
            c2.set(Calendar.DAY_OF_MONTH, 10);
        } else if (bundleField_.getSelectedIndex() == 3) {
            //15
            c1.set(Calendar.DAY_OF_MONTH, 1);
            c1.add(Calendar.MONTH, -1);
            c1.add(Calendar.DATE, +15);
            c2.set(Calendar.DAY_OF_MONTH, 15);
        } else if (bundleField_.getSelectedIndex() == 4) {
            //20
            c1.set(Calendar.DAY_OF_MONTH, 1);
            c1.add(Calendar.MONTH, -1);
            c1.add(Calendar.DATE, +20);
            c2.set(Calendar.DAY_OF_MONTH, 20);
        } else if (bundleField_.getSelectedIndex() == 5) {
            //25
            c1.set(Calendar.DAY_OF_MONTH, 1);
            c1.add(Calendar.MONTH, -1);
            c1.add(Calendar.DATE, +25);
            c2.set(Calendar.DAY_OF_MONTH, 25);
        }

        start_timeField_.setValue(c1);
        end_timeField_.setValue(c2);
    }

    private void doPrint() {
        Calendar c = timeField_.getValue();
        boolean b1 = report1Switch_.isSelected();
        boolean b2 = report2Switch_.isSelected();

        if (null == c) {
            JOptionPane.showMessageDialog(
                     this,
                     "締め日を入力してください。",
                     "警告",
                     JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (!b1 && !b2) {
            JOptionPane.showMessageDialog(
                     this,
                     "締め処理区分を選択してください。",
                     "警告",
                     JOptionPane.WARNING_MESSAGE);
            return;
        }

        int id = 0;

        if (b2) {
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
        }

        try {

            //印刷指示
            doPrint(id);
            //BookOpen.exeの起動
            doExcel();

        } catch (Exception e) {

            JOptionPane.showMessageDialog(
                     this,
                     "締め処理中に異常が発生しました.",
                     "警告",
                     JOptionPane.WARNING_MESSAGE);
            logger_.warning(e.toString());
        }
    }

    private void doBundle() {
        Calendar c = timeField_.getValue();
        boolean b1 = report1Switch_.isSelected();
        boolean b2 = report2Switch_.isSelected();
        if (null == c) {
            JOptionPane.showMessageDialog(
                     this,
                     "締め日を入力してください。",
                     "警告",
                     JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (!b1 && !b2) {
            JOptionPane.showMessageDialog(
                     this,
                     "締め処理区分を選択してください。",
                     "警告",
                     JOptionPane.WARNING_MESSAGE);
            return;
        }

        int id = 0;

        if (b2) {
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
        }

        Calendar c2 = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd hh:mm:ss");
        Calendar c3 = (Calendar) c.clone();

        try {

            Calendar c1 = Calendar.getInstance();

            String s = "select * from weigh_data where date_trunc('month', term_date) = ? and bill_id = ? ;";
            PreparedStatement ps01 = MainFrame.con1_.prepareStatement(s);
            try {
                ps01.setObject(1, DBManager.convert(c.getTime()));
                if (report1Switch_.isSelected()) {
                    ps01.setObject(2, 0);
                } else {
                    ps01.setObject(2, id);
                }
                ResultSet rs = ps01.executeQuery();

                while (rs.next()) {
                    s = "insert into weigh_data() ;";
                }

            } finally {
                ps01.close();
            }

            // 請求書の締め処理
            s = "select set_bill_data_in_date(?::date, ?::integer, ?::integer, ?::date, ?::date);";
            PreparedStatement ps = MainFrame.con1_.prepareStatement(s);
            try {
                ps.setObject(1, DBManager.convert(c.getTime()));
                if (report1Switch_.isSelected()) {
                    ps.setObject(2, 0);
                } else {
                    ps.setObject(2, id);
                }
                if (bundleField_.getSelectedIndex() == 0) {
                    ps.setObject(3, 0);
                } else if (bundleField_.getSelectedIndex() == 1) {
                    ps.setObject(3, 5);
                } else if (bundleField_.getSelectedIndex() == 2) {
                    ps.setObject(3, 10);
                } else if (bundleField_.getSelectedIndex() == 3) {
                    ps.setObject(3, 15);
                } else if (bundleField_.getSelectedIndex() == 4) {
                    ps.setObject(3, 20);
                } else if (bundleField_.getSelectedIndex() == 5) {
                    ps.setObject(3, 25);
                }

                ps.setObject(4, DBManager.convert(start_timeField_.getValue()));
                ps.setObject(5, DBManager.convert(end_timeField_.getValue()));
                ps.executeQuery();
            } finally {
                ps.close();
            }

            //少し待つ
            try {
                Thread.sleep(100);
            } catch (Exception e) {
            }

            if (bundleField_.getSelectedIndex() == 1) {
                c3.set(Calendar.DAY_OF_MONTH, 5);
            } else if (bundleField_.getSelectedIndex() == 2) {
                c3.set(Calendar.DAY_OF_MONTH, 10);
            } else if (bundleField_.getSelectedIndex() == 3) {
                c3.set(Calendar.DAY_OF_MONTH, 15);
            } else if (bundleField_.getSelectedIndex() == 4) {
                c3.set(Calendar.DAY_OF_MONTH, 20);
            } else if (bundleField_.getSelectedIndex() == 5) {
                c3.set(Calendar.DAY_OF_MONTH, 25);
            }

            BillReportJournal.recordJournal(con_, "締め処理",
                     sdf.format(DBManager.convert(c2)),//実行日
                     DBManager.convert(c3.getTime()).toString(),//締日
                     report1Switch_.isSelected(),
                     idField_.getText(),
                     nameField_.getText());

            JOptionPane.showMessageDialog(this,
                     "締め処理正常終了しました。"
                     + "\n開始時刻：" + sdf.format(DBManager.convert(c1))
                     + "\n終了時刻：" + sdf.format(DBManager.convert(c2)),
                     "確認",
                     JOptionPane.INFORMATION_MESSAGE);

        } catch (Exception e) {

            BillReportJournal.recordJournal(con_, "締め処理",
                     sdf.format(DBManager.convert(c2)),//実行日
                     DBManager.convert(c3.getTime()).toString(),//締日
                     report1Switch_.isSelected(),
                     idField_.getText(),
                     nameField_.getText());

            JOptionPane.showMessageDialog(
                     this,
                     "締め処理中に異常が発生しました.",
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

    private void doPrint(long sno) {
        try {
            Statement st = con_.createStatement();
            st.executeUpdate("begin;");
            if (bo) {
                try {
                    String s = new StringBuilder()
                             .append("update m_report set ")
                             .append("yearly_num = ?, ")
                             .append("print_flag = 1 ")
                             .append("where sno = 29; ")
                             .toString();

                    PreparedStatement ps = con_.prepareStatement(s);
                    try {
                        ps.setObject(1, sno);
                        ps.executeUpdate();
                    } finally {
                        ps.close();
                    }

                    st.executeUpdate("commit;");
                    bo = false;
                } catch (SQLException e) {
                    st.executeUpdate("rollback;");
                    throw e;
                } finally {
                    st.close();
                }
            } else {
                try {
                    String s = new StringBuilder()
                             .append("update m_report set ")
                             .append("yearly_num = ?, ")
                             .append("print_flag = 1 ")
                             .append("where sno = 31 ")
                             .toString();

                    PreparedStatement ps = con_.prepareStatement(s);
                    try {
                        ps.setObject(1, sno);
                        ps.executeUpdate();
                    } finally {
                        ps.close();
                    }

                    st.executeUpdate("commit;");
                } catch (SQLException e) {
                    st.executeUpdate("rollback;");
                    throw e;
                } finally {
                    st.close();
                }
            }
        } catch (Exception e) {
        }

    }

    private void doExcel() {
        try {
            Runtime runtime = Runtime.getRuntime();
            Process proc = runtime.exec("C:\\Excel\\BookOpen.exe");
            proc.waitFor();

        } catch (InterruptedException ev) {
            System.out.println("失敗(InnterruptedException)");
        } catch (IOException ev) {
            System.out.println("失敗(IOException)");
        }
    }

    private void showBillSelector() {

        long now_time = System.currentTimeMillis();
        long gap_time = now_time - mLastClickTime;
        if (gap_time < 500) {
            mClickCount++;
        } else {
            mClickCount = 1;
        }

        if (mClickCount >= 2) {
            int bundle_day_ = 0;
            if (bundleField_.getSelectedIndex() == 0) {
                //末締め
                bundle_day_ = 0;
            } else if (bundleField_.getSelectedIndex() == 1) {
                //5
                bundle_day_ = 5;
            } else if (bundleField_.getSelectedIndex() == 2) {
                //10
                bundle_day_ = 10;
            } else if (bundleField_.getSelectedIndex() == 3) {
                //15
                bundle_day_ = 15;
            } else if (bundleField_.getSelectedIndex() == 4) {
                //20
                bundle_day_ = 20;
            } else if (bundleField_.getSelectedIndex() == 5) {
                //25
                bundle_day_ = 25;
            }

            BillSelector selector = new BillSelector(con_, bundle_day_);
            switch (selector.showSelector(BundlePanel.this)) {
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
