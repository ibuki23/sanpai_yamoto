package aks.industrialwaste.recycle.ui.bill_report;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Calendar;
import java.util.logging.Logger;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.border.TitledBorder;
import aks.industrialwaste.database.DBManager;
import aks.industrialwaste.ui.Panel;
import aks.industrialwaste.recycle.report.DailyReport;
import aks.industrialwaste.recycle.report.XlsxBillReport;
import aks.industrialwaste.recycle.util.BillSelector;
import aks.swing.JTextField;
import java.awt.Color;
import jp.co.astax.awt.FontUtility;
import jp.co.astax.swing.DateTimeField;
import javax.swing.JPanel;
import javax.swing.JOptionPane;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;
import javax.swing.JComboBox;

/**
 * 請求書関係の帳票の印刷操作をするためのパネル.
 */
public class BillReportPanel extends Panel {

    protected static Logger logger_
             = Logger.getLogger(DailyReport.class.getName());
    private Connection con_;
    private DateTimeField timeField_;
    private JLabel report1Switch_;//請求一覧表(得意先別)：請求ヘッダー
    private JLabel report2Switch_;//請求明細表(得意先別・計量明細)：請求明細
    private JLabel report5Switch_;//得意先管理台帳：（貸方借方台帳）
    private JLabel report3Switch_;//入金明細表(得意先別・入金日別)：入金明細
    private JLabel report4Switch_;////売掛残高　
    private String[] bundle_;
    private JComboBox bundleField_;

    private JTextField idField_;
    private JTextField nameField_;

    public long mLastClickTime = System.currentTimeMillis();
    public int mClickCount = 0;

    public BillReportPanel(Connection con) {
        con_ = con;
    }

    @Override
    protected void initComponents() {
        super.initComponents();
        int height = 30;
        Font font14 = FontUtility.getFont("メイリオ", Font.PLAIN, 14);
        Font font18 = FontUtility.getFont("メイリオ", Font.PLAIN, 18);
        // タイトルは非表示
        titleField_.setVisible(false);

        bundle_ = new String[]{"末", "5", "10", "15", "20", "25"};

        centerPane_.setBorder(
                 BorderFactory.createTitledBorder(
                          BorderFactory.createRaisedBevelBorder(),
                          "一覧表",
                          TitledBorder.LEFT,
                          TitledBorder.DEFAULT_POSITION,
                          FontUtility.getFont("メイリオ", Font.PLAIN, 24)));
        // 集計日
        JLabel label = new JLabel("集計月", JLabel.LEFT);
        label.setFont(font18);
        addContainer(
                 centerPane_, label,
                 0, 0, 1, 1, 0.0, 0.0,
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

        label = new JLabel("締め日", JLabel.RIGHT);
        label.setFont(font18);
        label.setOpaque(true);
        addContainer(
                 centerPane_, label,
                 1, 1, 1, 1, 1.0, 0.0,
                 GridBagConstraints.EAST, GridBagConstraints.NONE,
                 new Insets(2, 2, 2, 2), 0, 0);
        bundleField_ = new JComboBox();
        bundleField_.setFont(font18);
        bundleField_.setMinimumSize(new Dimension(100, 35));
        bundleField_.setPreferredSize(new Dimension(100, 35));
        bundleField_.setModel(new javax.swing.DefaultComboBoxModel(bundle_));
        addContainer(
                 centerPane_, bundleField_,
                 2, 1, 1, 1, 0.0, 0.0,
                 GridBagConstraints.EAST, GridBagConstraints.NONE,
                 new Insets(2, 2, 2, 2), 0, 0);

        label = new JLabel("請求先指定", JLabel.LEFT);
        label.setFont(font18);
        addContainer(
                 centerPane_, label,
                 0, 3, 1, 1, 0.0, 0.0,
                 GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
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
                 0, 3, 1, 1, 0.0, 0.0,
                 GridBagConstraints.EAST, GridBagConstraints.NONE,
                 new Insets(2, 2, 2, 2), 0, 0);

        label = new JLabel("ダブルクリックにて検索可", JLabel.LEFT);
        label.setFont(font14);
        label.setForeground(new Color(0, 51, 153));
        addContainer(
                 centerPane_, label,
                 1, 3, 2, 1, 0.0, 0.0,
                 GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
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
                 0, 4, 4, 1, 0.0, 0.0,
                 GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
                 new Insets(2, 2, 2, 2), 0, 0);

        // 帳票選択
        report1Switch_ = new JLabel("1.請求一覧表");
        report1Switch_.setFont(font18);
        report1Switch_.setMinimumSize(new Dimension(320, height));
        report1Switch_.setPreferredSize(new Dimension(320, height));
        addContainer(
                 centerPane_, report1Switch_,
                 0, 5, 2, 1, 1.0, 0.0,
                 GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
                 new Insets(2, 2, 2, 2), 0, 0);
        // レポート機能
        JButton b = new JButton("出力");
        b.setFont(font14);
        b.setMargin(new Insets(2, 2, 2, 2));
        b.setMinimumSize(new Dimension(70, height));
        b.setPreferredSize(new Dimension(70, height));
        b.addActionListener(
                 new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                doPrint(2);
            }
        });
        addContainer(
                 centerPane_, b,
                 2, 5, 1, 1, 0.0, 0.0,
                 GridBagConstraints.EAST, GridBagConstraints.NONE,
                 new Insets(2, 2, 2, 2), 0, 0);

        report2Switch_ = new JLabel("2.請求明細表(請求先ID指定可)");
        report2Switch_.setFont(font18);
        report2Switch_.setMinimumSize(new Dimension(320, height));
        report2Switch_.setPreferredSize(new Dimension(320, height));
        addContainer(
                 centerPane_, report2Switch_,
                 0, 16, 2, 1, 0.0, 0.0,
                 GridBagConstraints.WEST, GridBagConstraints.NONE,
                 new Insets(2, 2, 2, 2), 0, 0);

        b = new JButton("出力");
        b.setFont(font14);
        b.setMargin(new Insets(2, 2, 2, 2));
        b.setMinimumSize(new Dimension(70, height));
        b.setPreferredSize(new Dimension(70, height));
        b.addActionListener(
                 new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                doPrint(3);
            }
        });
        addContainer(
                 centerPane_, b,
                 2, 16, 1, 1, 0.0, 0.0,
                 GridBagConstraints.EAST, GridBagConstraints.NONE,
                 new Insets(2, 2, 2, 2), 0, 0);

        report3Switch_ = new JLabel("3.入金明細表(請求先ID指定可)");
        report3Switch_.setFont(font18);
        report3Switch_.setMinimumSize(new Dimension(320, height));
        report3Switch_.setPreferredSize(new Dimension(320, height));
        if (DBManager.PAYMENT_FLAG) {
            addContainer(
                     centerPane_, report3Switch_,
                     0, 17, 2, 1, 0.0, 0.0,
                     GridBagConstraints.WEST, GridBagConstraints.NONE,
                     new Insets(2, 2, 2, 2), 0, 0);
        }

        b = new JButton("出力");
        b.setFont(font14);
        b.setMargin(new Insets(2, 2, 2, 2));
        b.setMinimumSize(new Dimension(70, height));
        b.setPreferredSize(new Dimension(70, height));
        b.addActionListener(
                 new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                doPrint(5);
            }
        });
        if (DBManager.PAYMENT_FLAG) {
            addContainer(
                     centerPane_, b,
                     2, 17, 1, 1, 0.0, 0.0,
                     GridBagConstraints.EAST, GridBagConstraints.NONE,
                     new Insets(2, 2, 2, 2), 0, 0);
        }

        report4Switch_ = new JLabel("4.売掛残高一覧表");
        report4Switch_.setFont(font18);
        report4Switch_.setMinimumSize(new Dimension(320, height));
        report4Switch_.setPreferredSize(new Dimension(320, height));
        if (DBManager.PAYMENT_FLAG) {
            addContainer(
                     centerPane_, report4Switch_,
                     0, 18, 2, 1, 0.0, 0.0,
                     GridBagConstraints.WEST, GridBagConstraints.NONE,
                     new Insets(2, 2, 2, 2), 0, 0);
        }

        b = new JButton("出力");
        b.setFont(font14);
        b.setMargin(new Insets(2, 2, 2, 2));
        b.setMinimumSize(new Dimension(70, height));
        b.setPreferredSize(new Dimension(70, height));
        b.addActionListener(
                 new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                doPrint(6);
            }
        });
        if (DBManager.PAYMENT_FLAG) {
            addContainer(
                     centerPane_, b,
                     2, 18, 1, 1, 0.0, 0.0,
                     GridBagConstraints.EAST, GridBagConstraints.NONE,
                     new Insets(2, 2, 2, 2), 0, 0);
        }

        report5Switch_ = new JLabel("5.得意先管理台帳(請求先別)");
        report5Switch_.setFont(font18);
        report5Switch_.setMinimumSize(new Dimension(320, height));
        report5Switch_.setPreferredSize(new Dimension(320, height));
        if (DBManager.PAYMENT_FLAG) {
            addContainer(
                     centerPane_, report5Switch_,
                     0, 19, 2, 1, 0.0, 0.0,
                     GridBagConstraints.WEST, GridBagConstraints.NONE,
                     new Insets(2, 2, 2, 2), 0, 0);
        }

        b = new JButton("出力");
        b.setFont(font14);
        b.setMargin(new Insets(2, 2, 2, 2));
        b.setMinimumSize(new Dimension(70, height));
        b.setPreferredSize(new Dimension(70, height));
        b.addActionListener(
                 new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                doPrint(4);
            }
        });
        if (DBManager.PAYMENT_FLAG) {
            addContainer(
                     centerPane_, b,
                     2, 19, 1, 1, 0.0, 0.0,
                     GridBagConstraints.EAST, GridBagConstraints.NONE,
                     new Insets(2, 2, 2, 2), 0, 0);
        }

        // ダミー
        addContainer(
                 centerPane_, new JPanel(),
                 0, 20, 3, 1, 0.0, 1.0,
                 GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                 new Insets(2, 2, 2, 2), 0, 0);

        idField_.setText("0");
        nameField_.setText("");

        timeField_.setValue(Calendar.getInstance());
    }

    private void doPrint(int report_id) {
        Calendar c = timeField_.getValue();
        if (null == c) {
            JOptionPane.showMessageDialog(
                     this,
                     "集計月を入力してください。",
                     "警告",
                     JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {

            int bill_id = 0;
            if (idField_.getText().length() > 0) {
                bill_id = Integer.valueOf(idField_.getText());
            }
            int bundle_ = 0;
            if (bundleField_.getSelectedIndex() == 0) {
                bundle_ = 0;
            } else if (bundleField_.getSelectedIndex() == 1) {
                bundle_ = 5;
            } else if (bundleField_.getSelectedIndex() == 2) {
                bundle_ = 10;
            } else if (bundleField_.getSelectedIndex() == 3) {
                bundle_ = 15;
            } else if (bundleField_.getSelectedIndex() == 4) {
                bundle_ = 20;
            } else if (bundleField_.getSelectedIndex() == 5) {
                bundle_ = 25;
            }

            if (report_id == 2) {
                //請求一覧表
                new XlsxBillReport(XlsxBillReport.BILL_REPORT2).print(con_,
                         XlsxBillReport.BILL_REPORT2,
                         DBManager.convert(c.getTime()),
                         bundle_,
                         0,
                         999,
                         0,
                         0);
            } else if (report_id == 3) {
                //請求明細
                new XlsxBillReport(XlsxBillReport.BILL_REPORT3).print(con_,
                         XlsxBillReport.BILL_REPORT3,
                         DBManager.convert(c.getTime()),
                         bill_id,
                         bundle_,
                         0,
                         0,
                         0);
            } else if (report_id == 4) {
                //得意先管理台帳
                if (bill_id > 0) {
                    new XlsxBillReport(XlsxBillReport.BILL_REPORT4).print(con_,
                             XlsxBillReport.BILL_REPORT4,
                             DBManager.convert(c.getTime()),
                             bill_id,
                             0, //使用せず
                             0, //使用せず
                             0,
                             0);
                }
            } else if (report_id == 5) {
                //入金明細表
                new XlsxBillReport(XlsxBillReport.BILL_REPORT5).print(con_,
                         XlsxBillReport.BILL_REPORT5,
                         DBManager.convert(c.getTime()),
                         bill_id,
                         0, //使用せず
                         0, //使用せず
                         0,
                         0);
            } else if (report_id == 6) {
                //売掛残高一覧
                new XlsxBillReport(XlsxBillReport.BILL_REPORT6).print(con_,
                         XlsxBillReport.BILL_REPORT6,
                         DBManager.convert(c.getTime()),
                         0, //使用せず
                         0, //使用せず
                         0, //使用せず
                         0,
                         0);
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(
                     this,
                     "印刷中に異常が発生しました.",
                     "警告",
                     JOptionPane.WARNING_MESSAGE);
            logger_.warning(e.toString());
        }
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
            switch (selector.showSelector(BillReportPanel.this)) {
                case BillSelector.CLEARED:
                    idField_.setText("");
                    nameField_.setText("");
                    bundleField_.setSelectedIndex(0);
                    break;
                case BillSelector.SELECTED:
                    Map<String, Object> item = selector.selectedRow();
                    idField_.setText(String.valueOf((Integer) item.get("id")));
                    nameField_.setText((String) item.get("name"));
                    if ((Integer) item.get("bundle_day") == 0) {
                        bundleField_.setSelectedIndex(0);
                    } else if ((Integer) item.get("bundle_day") == 5) {
                        bundleField_.setSelectedIndex(1);
                    } else if ((Integer) item.get("bundle_day") == 10) {
                        bundleField_.setSelectedIndex(2);
                    } else if ((Integer) item.get("bundle_day") == 15) {
                        bundleField_.setSelectedIndex(3);
                    } else if ((Integer) item.get("bundle_day") == 20) {
                        bundleField_.setSelectedIndex(4);
                    } else if ((Integer) item.get("bundle_day") == 25) {
                        bundleField_.setSelectedIndex(5);
                    }
                    break;
                default:
                // No operation.
            }
            updateView();
        }
        mLastClickTime = now_time;

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
                    if ((Integer) rs.getInt("bundle_day") == 0) {
                        bundleField_.setSelectedIndex(0);
                    } else if ((Integer) rs.getInt("bundle_day") == 5) {
                        bundleField_.setSelectedIndex(1);
                    } else if ((Integer) rs.getInt("bundle_day") == 10) {
                        bundleField_.setSelectedIndex(2);
                    } else if ((Integer) rs.getInt("bundle_day") == 15) {
                        bundleField_.setSelectedIndex(3);
                    } else if ((Integer) rs.getInt("bundle_day") == 20) {
                        bundleField_.setSelectedIndex(4);
                    } else if ((Integer) rs.getInt("bundle_day") == 25) {
                        bundleField_.setSelectedIndex(5);
                    }
                }
            } finally {
                ps.close();
            }
        } catch (SQLException e) {

        }
        return ret;
    }
}
