package aks.industrialwaste.recycle.ui.report;

import aks.industrialwaste.database.DBManager;
import aks.industrialwaste.recycle.report.*;
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
import aks.industrialwaste.ui.Panel;
import java.io.File;
import javax.swing.JFileChooser;
import javax.swing.JPanel;
import jp.co.astax.awt.FontUtility;
import jp.co.astax.swing.DateTimeField;

public class YearlyReportPanel extends Panel {

    private Connection con_;
    private DateTimeField timeField_;
    private JLabel report4Switch_;
    private JLabel report5Switch_;
    private JLabel report7Switch_;
    private JLabel report9Switch_;
    private int reportx_;
    private JButton b4;
    private JButton b5;
    private JButton b7;
    private JButton b9;
    private String areaName_ = "";
    private JLabel areaLabel_;

    protected static final int REPORT_TYPE_YAMAGATA = 5;
    protected static final int REPORT_TYPE_FUKUSHIMA = 6;
    protected static final int REPORT_TYPE_SENDAI = 14;

    public YearlyReportPanel(Connection con) {
        this(con, "宮城県", REPORT_TYPE_SENDAI);
    }

    public YearlyReportPanel(Connection con, String areaName, int reportType) {
        con_ = con;
        areaName_ = areaName;
        reportx_ = reportType;
        updateAreaName();
        updateReportVisibility();
    }

    protected void initComponents() {
        super.initComponents();

        b4 = new JButton("出力");
        b5 = new JButton("出力");
        b7 = new JButton("出力");
        b9 = new JButton("出力");
        int height = 30;
        Font font14 = FontUtility.getFont("メイリオ", Font.PLAIN, 14);
        Font font18 = FontUtility.getFont("メイリオ", Font.PLAIN, 18);

        // タイトルは非表示
        titleField_.setVisible(false);

        centerPane_.setBorder(
                 BorderFactory.createTitledBorder(
                          BorderFactory.createRaisedBevelBorder(),
                          "年間報告",
                          TitledBorder.LEFT,
                          TitledBorder.DEFAULT_POSITION,
                          FontUtility.getFont("メイリオ", Font.PLAIN, 24)));
        // 集計年
        JLabel label = new JLabel("集計年", JLabel.LEFT);
        label.setFont(font18);
        addContainer(
                 centerPane_, label,
                 0, 0, 1, 1, 0.0, 0.0,
                 GridBagConstraints.SOUTHWEST, GridBagConstraints.HORIZONTAL,
                 new Insets(2, 2, 2, 2), 0, 0);

        timeField_ = new DateTimeField(DateTimeField.DateFormat.Y);
        timeField_.setFont(font18);
        timeField_.setMinimumSize(new Dimension(120, 35));
        timeField_.setPreferredSize(new Dimension(120, 35));
        timeField_.setMinimumSize(timeField_.getPreferredSize());
        addContainer(
                 centerPane_, timeField_,
                 0, 1, 1, 1, 0.0, 0.0,
                 GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL,
                 new Insets(2, 2, 2, 2), 0, 0);

        report4Switch_ = new JLabel("1.収集運搬実績報告書");
        report4Switch_.setFont(font18);
        report4Switch_.setMinimumSize(new Dimension(280, height));
        report4Switch_.setPreferredSize(new Dimension(280, height));
        addContainer(
                 centerPane_, report4Switch_,
                 0, 5, 2, 1, 0.0, 0.0,
                 GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
                 new Insets(2, 2, 2, 2), 0, 0);

        // レポート機能
        b4.setFont(font14);
        b4.setMargin(new Insets(2, 2, 2, 2));
        b4.setMinimumSize(new Dimension(80, height));
        b4.setPreferredSize(new Dimension(80, height));
        b4.addActionListener(
                 new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                ReportAction(3, 0);
            }
        });
        addContainer(
                 centerPane_, b4,
                 2, 5, 1, 1, 0.0, 0.0,
                 GridBagConstraints.EAST, GridBagConstraints.NONE,
                 new Insets(2, 2, 2, 2), 0, 0);

        if (!isFukushima()) {
            report5Switch_ = new JLabel("2.処分実績報告書中間");
            report5Switch_.setFont(font18);
            report5Switch_.setMinimumSize(new Dimension(280, height));
            report5Switch_.setPreferredSize(new Dimension(280, height));

            addContainer(
                     centerPane_, report5Switch_,
                     0, 6, 2, 1, 0.0, 0.0,
                     GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
                     new Insets(2, 2, 2, 2), 0, 0);

            b5.setFont(font14);
            b5.setMargin(new Insets(2, 2, 2, 2));
            b5.setMinimumSize(new Dimension(80, height));
            b5.setPreferredSize(new Dimension(80, height));
            b5.addActionListener(
                     new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent event) {
                    ReportAction(1, 0);
                }
            });
            addContainer(
                     centerPane_, b5,
                     2, 6, 1, 1, 0.0, 0.0,
                     GridBagConstraints.EAST, GridBagConstraints.NONE,
                     new Insets(2, 2, 2, 2), 0, 0);
        }

        report7Switch_ = new JLabel("3.交付状況報告書");
        report7Switch_.setFont(font18);
        report7Switch_.setMinimumSize(new Dimension(240, height));
        report7Switch_.setPreferredSize(new Dimension(240, height));
        addContainer(
                 centerPane_, report7Switch_,
                 0, 8, 2, 1, 0.0, 0.0,
                 GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
                 new Insets(2, 2, 2, 2), 0, 0);

        b7.setFont(font14);
        b7.setMargin(new Insets(2, 2, 2, 2));
        b7.setMinimumSize(new Dimension(80, height));
        b7.setPreferredSize(new Dimension(80, height));
        b7.addActionListener(
                 new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {

                String ent_id_ = JOptionPane.showInputDialog(
                         YearlyReportPanel.this,
                         "出力する排出業者コードを入力してください。",
                         "確認",
                         JOptionPane.INFORMATION_MESSAGE);
                if (ent_id_ != null && ent_id_.length() > 0) {
                    ReportAction(4, Integer.valueOf(ent_id_));
                }
            }
        });
        addContainer(
                 centerPane_, b7,
                 2, 8, 1, 1, 0.0, 0.0,
                 GridBagConstraints.EAST, GridBagConstraints.NONE,
                 new Insets(2, 2, 2, 2), 0, 0);

        if (!isFukushima()) {
            report9Switch_ = new JLabel("4.処理実績報告書");
            report9Switch_.setFont(font18);
            report9Switch_.setMinimumSize(new Dimension(280, height));
            report9Switch_.setPreferredSize(new Dimension(280, height));
            addContainer(
                     centerPane_, report9Switch_,
                     0, 9, 2, 1, 0.0, 0.0,
                     GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL,
                     new Insets(2, 2, 2, 2), 0, 0);

            b9.setFont(font14);
            b9.setMargin(new Insets(2, 2, 2, 2));
            b9.setMinimumSize(new Dimension(80, height));
            b9.setPreferredSize(new Dimension(80, height));
            b9.addActionListener(
                     new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent event) {
                    ReportAction(5, 0);
                }
            });
            addContainer(
                     centerPane_, b9,
                     2, 9, 1, 1, 0.0, 0.0,
                     GridBagConstraints.EAST, GridBagConstraints.NONE,
                     new Insets(2, 2, 2, 2), 0, 0);
        }

        label = new JLabel("※行政報告は年度集計にて出力します", JLabel.LEFT);
        label.setFont(font18);
        addContainer(
                 centerPane_, label,
                 0, 15, 3, 1, 0.0, 0.0,
                 GridBagConstraints.NORTHWEST, GridBagConstraints.NONE,
                 new Insets(2, 2, 2, 2), 0, 0);

        addContainer(
                 centerPane_, new JPanel(),
                 0, 20, 3, 1, 1.0, 1.0,
                 GridBagConstraints.EAST, GridBagConstraints.NONE,
                 new Insets(2, 2, 2, 2), 0, 0);

        timeField_.setValue(Calendar.getInstance());
    }

    private void ReportAction(int report_id, int ent_id) {
        // 20111219 add データをcsvファイルに落とす

        // 1: 処分実績報告書中間
        // 2: 処分実績報告書最終
        // 3: 収集運搬報告書
        // 4: 交付状況報告書
        // 5: 特別管理処分報告書
        // 6: 処理実績報告書
        try {
            Calendar c = timeField_.getValue();
            JFileChooser fc = new JFileChooser();
            fc.setSelectedFile(new File(DBManager.pass_,
                     String.format("%1$tY_" + (report_id == 1 ? "処分実績報告書中間"
                              : report_id == 2 ? "処分実績報告書最終"
                                       : report_id == 3 ? "収集運搬報告書"
                                                : report_id == 4 ? "交付状況報告書" + ent_id
                                                         : report_id == 5 ? "処理実績報告書" : "") + ".xlsx", c)));
            if (JFileChooser.APPROVE_OPTION != fc.showSaveDialog(this)) {
                return;
            }
            File f = fc.getSelectedFile();
            String path = f.getPath();

            if (!path.matches(".+\\.[xX][lL][sS][xX]$")) {
                path = path + ".xlsx";
            }

            excel_report(path, report_id, c, ent_id);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(
                     this,
                     "EXCELファイルへの出力に失敗しました.",
                     "警告",
                     JOptionPane.WARNING_MESSAGE);
        }
    }

    private boolean isFukushima() {
        return reportx_ == REPORT_TYPE_FUKUSHIMA;
    }

    private void updateAreaName() {
        if (areaLabel_ != null) {
            areaLabel_.setText(areaName_);
        }

        if (centerPane_ != null) {
            centerPane_.setBorder(
                     BorderFactory.createTitledBorder(
                              BorderFactory.createRaisedBevelBorder(),
                              "年間報告  " + areaName_,
                              TitledBorder.LEFT,
                              TitledBorder.DEFAULT_POSITION,
                              FontUtility.getFont("メイリオ", Font.PLAIN, 24)));
        }
    }

    private void updateReportVisibility() {
        if (isFukushima()) {
            if (report5Switch_ != null) {
                report5Switch_.setVisible(false);
            }
            if (b5 != null) {
                b5.setVisible(false);
            }

            if (report9Switch_ != null) {
                report9Switch_.setVisible(false);
            }
            if (b9 != null) {
                b9.setVisible(false);
            }

            if (centerPane_ != null) {
                centerPane_.revalidate();
                centerPane_.repaint();
            }
        }
    }

    private void excel_report(String path, int report_sno, Calendar term_date, int ent_id) {

        switch (reportx_) {

            case 5:
                //山形
                switch (report_sno) {
                    case XlsxYearlyReportYamagata.YEARLY_REPORT1:
                        new XlsxYearlyReportYamagata(XlsxYearlyReportYamagata.YEARLY_REPORT1).print(con_, path, report_sno, term_date.get(Calendar.YEAR), 0);
                        break;
                    case XlsxYearlyReportYamagata.YEARLY_REPORT2:
                        new XlsxYearlyReportYamagata(XlsxYearlyReportYamagata.YEARLY_REPORT2).print(con_, path, report_sno, term_date.get(Calendar.YEAR), 0);
                        break;
                    case XlsxYearlyReportYamagata.YEARLY_REPORT3:
                        new XlsxYearlyReportYamagata(XlsxYearlyReportYamagata.YEARLY_REPORT3).print(con_, path, report_sno, term_date.get(Calendar.YEAR), 0);
                        break;
                    case XlsxYearlyReportYamagata.YEARLY_REPORT4:
                        new XlsxYearlyReportYamagata(XlsxYearlyReportYamagata.YEARLY_REPORT4).print(con_, path, report_sno, term_date.get(Calendar.YEAR), ent_id);
                        break;
                    default:
                        break;
                }
                break;
            case 6:
                //福島
                switch (report_sno) {
                    case XlsxYearlyReportFukushima.YEARLY_REPORT1:
                        new XlsxYearlyReportFukushima(XlsxYearlyReportFukushima.YEARLY_REPORT1).print(con_, path, report_sno, term_date.get(Calendar.YEAR), 0);
                        break;
                    case XlsxYearlyReportFukushima.YEARLY_REPORT2:
                        new XlsxYearlyReportFukushima(XlsxYearlyReportFukushima.YEARLY_REPORT2).print(con_, path, report_sno, term_date.get(Calendar.YEAR), 0);
                        break;
                    case XlsxYearlyReportFukushima.YEARLY_REPORT3:
                        new XlsxYearlyReportFukushima(XlsxYearlyReportFukushima.YEARLY_REPORT3).print(con_, path, report_sno, term_date.get(Calendar.YEAR), 0);
                        break;
                    case XlsxYearlyReportFukushima.YEARLY_REPORT4:
                        new XlsxYearlyReportFukushima(XlsxYearlyReportFukushima.YEARLY_REPORT4).print(con_, path, report_sno, term_date.get(Calendar.YEAR), ent_id);
                        break;
                    default:
                        break;
                }
                break;
            case 14:
                // 仙台
                switch (report_sno) {
                    case XlsxYearlyReportMiyagi.YEARLY_REPORT1:
                        new XlsxYearlyReportMiyagi(XlsxYearlyReportMiyagi.YEARLY_REPORT1).print(con_, path, report_sno, term_date.get(Calendar.YEAR), 0);
                        break;
                    case XlsxYearlyReportMiyagi.YEARLY_REPORT2:
                        new XlsxYearlyReportMiyagi(XlsxYearlyReportMiyagi.YEARLY_REPORT2).print(con_, path, report_sno, term_date.get(Calendar.YEAR), 0);
                        break;
                    case XlsxYearlyReportMiyagi.YEARLY_REPORT3:
                        new XlsxYearlyReportMiyagi(XlsxYearlyReportMiyagi.YEARLY_REPORT3).print(con_, path, report_sno, term_date.get(Calendar.YEAR), 0);
                        break;
                    case XlsxYearlyReportMiyagi.YEARLY_REPORT4:
                        new XlsxYearlyReportMiyagi(XlsxYearlyReportMiyagi.YEARLY_REPORT4).print(con_, path, report_sno, term_date.get(Calendar.YEAR), ent_id);
                        break;
                    case XlsxYearlyReportMiyagi.YEARLY_REPORT5:
                        new XlsxYearlyReportMiyagi(XlsxYearlyReportMiyagi.YEARLY_REPORT5).print(con_, path, report_sno, term_date.get(Calendar.YEAR), 0);
                        break;
                }
                break;
            default:
                break;
        }
    }
}
