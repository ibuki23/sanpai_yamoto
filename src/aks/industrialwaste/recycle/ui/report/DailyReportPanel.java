package aks.industrialwaste.recycle.ui.report;

import aks.industrialwaste.database.DBManager;
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
import aks.industrialwaste.ui.Panel;
import aks.industrialwaste.recycle.report.DailyReport;
import aks.industrialwaste.recycle.report.XlsxReport;
import jp.co.astax.awt.FontUtility;
import jp.co.astax.swing.DateTimeField;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import java.io.File;
import java.sql.Connection;
import java.text.SimpleDateFormat;
import javax.swing.JPanel;

public class DailyReportPanel extends Panel {

    protected static Logger logger_
             = Logger.getLogger(DailyReport.class.getName());
    private Connection con_;
    private DateTimeField timeField_, timeField2_;
    private JLabel report1Switch_;
    private JLabel report2Switch_;
    private JLabel report3Switch_;
    private JLabel report4Switch_;
    private JLabel report5Switch_;
    private JLabel report6Switch_;

    public DailyReportPanel(Connection con) {
        con_ = con;
    }

    @Override
    protected void initComponents() {
        super.initComponents();
        int height = 30;
        Font font14 = FontUtility.getFont("メイリオ", Font.PLAIN, 14);
        Font font18 = FontUtility.getFont("メイリオ", Font.PLAIN, 18);

        centerPane_.doLayout();                 // 念のため
        centerPane_.validate();

        Dimension pref = centerPane_.getPreferredSize();

// 枠線・余白込みで少し足す（タイトル付きBorderの分も吸収）
        int padW = 40;
        int padH = 40;

// 画面に対してデカすぎないように上限も設ける（必要なら調整）
        Dimension screen = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
        int maxW = (int) (screen.width * 0.60);   // 画面幅の60%まで
        int maxH = (int) (screen.height * 0.80);  // 画面高の80%まで

        int w = Math.min(pref.width + padW, maxW);
        int h = Math.min(pref.height + padH, maxH);

        Dimension best = new Dimension(w, h);
        centerPane_.setPreferredSize(best);
        centerPane_.setMinimumSize(best);

        // タイトルは非表示
        titleField_.setVisible(false);

        centerPane_.setBorder(
                 BorderFactory.createTitledBorder(
                          BorderFactory.createRaisedBevelBorder(),
                          "帳票出力",
                          TitledBorder.LEFT,
                          TitledBorder.DEFAULT_POSITION,
                          FontUtility.getFont("メイリオ", Font.PLAIN, 24)));
        // 集計日
        JLabel label = new JLabel("集計日(開始日)", JLabel.LEFT);
        label.setFont(font18);
        addContainer(
                 centerPane_, label,
                 0, 0, 1, 1, 0.0, 0.0,
                 GridBagConstraints.SOUTHWEST, GridBagConstraints.HORIZONTAL,
                 new Insets(2, 2, 2, 2), 0, 0);
        timeField_ = new DateTimeField(DateTimeField.DateFormat.YMD);
        timeField_.setFont(font18);
        timeField_.setMinimumSize(new Dimension(170, 35));
        timeField_.setPreferredSize(new Dimension(170, 35));
        timeField_.setMinimumSize(timeField_.getPreferredSize());
        addContainer(
                 centerPane_, timeField_,
                 0, 1, 3, 1, 0.0, 0.0,
                 GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL,
                 new Insets(2, 2, 2, 2), 0, 0);
        // 集計日
        label = new JLabel("集計日(終了日)", JLabel.LEFT);
        label.setFont(font18);
        addContainer(
                 centerPane_, label,
                 0, 2, 1, 1, 0.0, 0.0,
                 GridBagConstraints.SOUTHWEST, GridBagConstraints.HORIZONTAL,
                 new Insets(2, 2, 2, 2), 0, 0);
        timeField2_ = new DateTimeField(DateTimeField.DateFormat.YMD);
        timeField2_.setFont(font18);
        timeField2_.setMinimumSize(new Dimension(170, 35));
        timeField2_.setPreferredSize(new Dimension(170, 35));
        timeField2_.setMinimumSize(timeField2_.getPreferredSize());
        addContainer(
                 centerPane_, timeField2_,
                 0, 3, 3, 1, 0.0, 0.0,
                 GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL,
                 new Insets(2, 2, 2, 2), 0, 0);

        // 帳票選択
        report1Switch_ = new JLabel("1.計量記録一覧");
        report1Switch_.setFont(font18);
        report1Switch_.setMinimumSize(new Dimension(300, height));
        report1Switch_.setPreferredSize(new Dimension(300, height));
        addContainer(
                 centerPane_, report1Switch_,
                 0, 4, 3, 1, 1.0, 0.0,
                 GridBagConstraints.WEST, GridBagConstraints.NONE,
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
                ReportAction(1, 0);
            }
        });
        addContainer(
                 centerPane_, b,
                 3, 4, 1, 1, 0.0, 0.0,
                 GridBagConstraints.EAST, GridBagConstraints.NONE,
                 new Insets(2, 2, 2, 2), 0, 0);

        report2Switch_ = new JLabel("2.業者別受入集計表（排出業者別）");
        report2Switch_.setFont(font18);
        report2Switch_.setMinimumSize(new Dimension(300, height));
        report2Switch_.setPreferredSize(new Dimension(300, height));
        addContainer(
                 centerPane_, report2Switch_,
                 0, 5, 3, 1, 0.0, 0.0,
                 GridBagConstraints.WEST, GridBagConstraints.NONE,
                 new Insets(2, 2, 2, 2), 0, 0);
        // レポート機能
        b = new JButton("出力");
        b.setFont(font14);
        b.setMargin(new Insets(2, 2, 2, 2));
        b.setMinimumSize(new Dimension(70, height));
        b.setPreferredSize(new Dimension(70, height));
        b.addActionListener(
                 new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                ReportAction(2, 0);
            }
        });
        addContainer(
                 centerPane_, b,
                 3, 5, 1, 1, 0.0, 0.0,
                 GridBagConstraints.EAST, GridBagConstraints.NONE,
                 new Insets(2, 2, 2, 2), 0, 0);

        report3Switch_ = new JLabel("3.業者別受入集計表（運搬業者別）");
        report3Switch_.setFont(font18);
        report3Switch_.setMinimumSize(new Dimension(300, height));
        report3Switch_.setPreferredSize(new Dimension(300, height));
        addContainer(
                 centerPane_, report3Switch_,
                 0, 6, 3, 1, 0.0, 0.0,
                 GridBagConstraints.WEST, GridBagConstraints.NONE,
                 new Insets(2, 2, 2, 2), 0, 0);
        // レポート機能
        b = new JButton("出力");
        b.setFont(font14);
        b.setMargin(new Insets(2, 2, 2, 2));
        b.setMinimumSize(new Dimension(70, height));
        b.setPreferredSize(new Dimension(70, height));
        b.addActionListener(
                 new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                ReportAction(3, 0);
            }
        });
        addContainer(
                 centerPane_, b,
                 3, 6, 1, 1, 0.0, 0.0,
                 GridBagConstraints.EAST, GridBagConstraints.NONE,
                 new Insets(2, 2, 2, 2), 0, 0);

        report4Switch_ = new JLabel("4.業者別受入集計表（品目別）");
        report4Switch_.setFont(font18);
        report4Switch_.setMinimumSize(new Dimension(300, height));
        report4Switch_.setPreferredSize(new Dimension(300, height));
        addContainer(
                 centerPane_, report4Switch_,
                 0, 7, 3, 1, 0.0, 0.0,
                 GridBagConstraints.WEST, GridBagConstraints.NONE,
                 new Insets(2, 2, 2, 2), 0, 0);
        // レポート機能
        b = new JButton("出力");
        b.setFont(font14);
        b.setMargin(new Insets(2, 2, 2, 2));
        b.setMinimumSize(new Dimension(70, height));
        b.setPreferredSize(new Dimension(70, height));
        b.addActionListener(
                 new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                ReportAction(4, 0);
            }
        });
        addContainer(
                 centerPane_, b,
                 3, 7, 1, 1, 0.0, 0.0,
                 GridBagConstraints.EAST, GridBagConstraints.NONE,
                 new Insets(2, 2, 2, 2), 0, 0);

        report5Switch_ = new JLabel("5.産業廃棄物実績簿");
        report5Switch_.setFont(font18);
        report5Switch_.setMinimumSize(new Dimension(300, height));
        report5Switch_.setPreferredSize(new Dimension(300, height));
        addContainer(
                 centerPane_, report5Switch_,
                 0, 8, 3, 1, 0.0, 0.0,
                 GridBagConstraints.WEST, GridBagConstraints.NONE,
                 new Insets(2, 2, 2, 2), 0, 0);
        // レポート機能
        b = new JButton("出力");
        b.setFont(font14);
        b.setMargin(new Insets(2, 2, 2, 2));
        b.setMinimumSize(new Dimension(70, height));
        b.setPreferredSize(new Dimension(70, height));
        b.addActionListener(
                 new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                String ent_id_ = JOptionPane.showInputDialog(
                         DailyReportPanel.this,
                         "出力する排出業者コードを入力してください。",
                         "確認",
                         JOptionPane.INFORMATION_MESSAGE);
                if (ent_id_ != null && ent_id_.length() > 0) {
                    ReportAction(5, Integer.valueOf(ent_id_));
                }
            }
        });
        addContainer(
                 centerPane_, b,
                 3, 8, 1, 1, 0.0, 0.0,
                 GridBagConstraints.EAST, GridBagConstraints.NONE,
                 new Insets(2, 2, 2, 2), 0, 0);

        report6Switch_ = new JLabel("6.業者別搬出品目集計表（品目別）");
        report6Switch_.setFont(font18);
        report6Switch_.setMinimumSize(new Dimension(300, height));
        report6Switch_.setPreferredSize(new Dimension(300, height));
        addContainer(
                 centerPane_, report6Switch_,
                 0, 9, 3, 1, 0.0, 0.0,
                 GridBagConstraints.WEST, GridBagConstraints.NONE,
                 new Insets(2, 2, 2, 2), 0, 0);
        // レポート機能
        b = new JButton("出力");
        b.setFont(font14);
        b.setMargin(new Insets(2, 2, 2, 2));
        b.setMinimumSize(new Dimension(70, height));
        b.setPreferredSize(new Dimension(70, height));
        b.addActionListener(
                 new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                ReportAction(6, 0);
            }
        });
        addContainer(
                 centerPane_, b,
                 3, 9, 1, 1, 0.0, 0.0,
                 GridBagConstraints.EAST, GridBagConstraints.NONE,
                 new Insets(2, 2, 2, 2), 0, 0);


        addContainer(
                 centerPane_, new JPanel(),
                 0, 15, 3, 1, 1.0, 1.0,
                 GridBagConstraints.EAST, GridBagConstraints.NONE,
                 new Insets(2, 2, 2, 2), 0, 0);

        Calendar fromCal = Calendar.getInstance();
        fromCal.set(Calendar.HOUR_OF_DAY, 0);
        fromCal.set(Calendar.MINUTE, 0);
        fromCal.set(Calendar.SECOND, 0);
        fromCal.set(Calendar.MILLISECOND, 0);

        Calendar toCal = Calendar.getInstance();
        toCal.set(Calendar.HOUR_OF_DAY, 23);
        toCal.set(Calendar.MINUTE, 59);
        toCal.set(Calendar.SECOND, 59);
        toCal.set(Calendar.MILLISECOND, 999);

        timeField_.setValue(fromCal);
        timeField2_.setValue(toCal);

//        timeField_.setValue(Calendar.getInstance());
//        timeField2_.setValue(Calendar.getInstance());
    }

    private void ReportAction(int report_id, int ent_id) {

        try {
            Calendar c = timeField_.getValue();
            Calendar c1 = timeField2_.getValue();
            SimpleDateFormat sdf_dd = new SimpleDateFormat("yyyyMMdd_");
            SimpleDateFormat sdf_ = new SimpleDateFormat("yyyyMM_");
            JFileChooser fc = new JFileChooser();
            if (report_id != 8) {
                fc.setSelectedFile(new File(DBManager.pass_,
                         (sdf_dd.format(DBManager.convert(c)).equals(sdf_dd.format(DBManager.convert(c1)))
                         ? sdf_dd.format(DBManager.convert(c))
                         : sdf_dd.format(DBManager.convert(c)) + sdf_dd.format(DBManager.convert(c1)))
                         + (report_id == 1 ? "計量記録一覧"
                                  : report_id == 2 ? "業者集計表1"
                                           : report_id == 3 ? "業者集計表2"
                                                    : report_id == 4 ? "業者集計表3"
                                                             : report_id == 5 ? "マニフェスト管理台帳"
                                                                      : report_id == 6 ? "業者集計表4"
                                                                               : report_id == 7 ? "計量記録一覧"
                                                                                                                   : "") + ".xlsx"));
            } else {
                fc.setSelectedFile(new File(DBManager.pass_,
                         (sdf_.format(DBManager.convert(c)) + "業者集計表5.xlsx")));
            }
            if (JFileChooser.APPROVE_OPTION != fc.showSaveDialog(this)) {
                return;
            }
            File f = fc.getSelectedFile();
            f = new File(String.format("%s", f.getPath()));

            if (XlsxReport.REPORT1 == report_id) {
                new XlsxReport(XlsxReport.REPORT1).print(con_, XlsxReport.REPORT1, f.getPath(), timeField_.getValue(), timeField2_.getValue(), 0);
            } else if (XlsxReport.REPORT2 == report_id) {
                new XlsxReport(XlsxReport.REPORT2).print(con_, XlsxReport.REPORT2, f.getPath(), timeField_.getValue(), timeField2_.getValue(), 0);
            } else if (XlsxReport.REPORT3 == report_id) {
                new XlsxReport(XlsxReport.REPORT3).print(con_, XlsxReport.REPORT3, f.getPath(), timeField_.getValue(), timeField2_.getValue(), 0);
            } else if (XlsxReport.REPORT4 == report_id) {
                new XlsxReport(XlsxReport.REPORT4).print(con_, XlsxReport.REPORT4, f.getPath(), timeField_.getValue(), timeField2_.getValue(), 0);
            } else if (XlsxReport.REPORT5 == report_id) {
                new XlsxReport(XlsxReport.REPORT5).print(con_, XlsxReport.REPORT5, f.getPath(), timeField_.getValue(), timeField2_.getValue(), ent_id);
            } else if (XlsxReport.REPORT6 == report_id) {
                new XlsxReport(XlsxReport.REPORT6).print(con_, XlsxReport.REPORT6, f.getPath(), timeField_.getValue(), timeField2_.getValue(), 0);
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(
                     this,
                     "EXCELファイルへの出力に失敗しました.",
                     "警告",
                     JOptionPane.WARNING_MESSAGE);
            System.out.println(e);
        }

    }

}
