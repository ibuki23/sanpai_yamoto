package aks.industrialwaste.recycle.ui;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.logging.Logger;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.event.AncestorEvent;
import aks.industrialwaste.ui.Panel;
import jp.co.astax.awt.FontUtility;
import aks.industrialwaste.database.DBManager;
import java.awt.Color;
import javax.swing.JButton;

public class Infobar extends Panel {

    protected static Logger logger_ = Logger.getLogger(Infobar.class.getName());
    private Connection con_;
    private Thread thread_ = null;
    private Thread alert_thread_ = null;
    protected JLabel timeField_;
    protected JList messageField_;
    protected JLabel messageLabel_;
    public static String message_;
    public static String mfc_message_;
    public static int master_index_;
    public Thread mThreadMasterTransferThread;
    private String s企業名 = "";
    private String s起動モード = "";
    private JLabel l企業名;
    public static int i消費税率 = 0;
    private JButton authBtn, mfcBtn;

    public Infobar(Connection con) {
        con_ = con;
        setup();
    }

    @Override
    public void ancestorAdded(AncestorEvent event) {
        if (null == thread_) {
            thread_
                     = new Thread(
                              new Runnable() {
                         public void run() {
                             while (!Thread.currentThread().isInterrupted()) {
                                 long tm = System.currentTimeMillis();
                                 try {
                                     SwingUtilities.invokeLater(
                                              new Runnable() {
                                         public void run() {
                                             updateView();
                                         }
                                     });

                                     tm = System.currentTimeMillis() - tm;
                                     tm = Math.max(10, 1000 - tm);
                                     Thread.sleep(tm);
                                 } catch (InterruptedException e) {
                                     Thread.currentThread().interrupt();
                                 } catch (Exception e) {
                                     logger_.warning(e.toString());
                                 }
                             }
                         }
                     });
            thread_.start();
        }

        if (null == alert_thread_) {
            alert_thread_
                     = new Thread(
                              new Runnable() {
                         public void run() {
                             while (!Thread.currentThread().isInterrupted()) {
                                 try {
                                     SwingUtilities.invokeLater(
                                              new Runnable() {
                                         public void run() {
                                             checkSoundEvent();
                                         }
                                     });

                                     Thread.sleep(2000);
                                 } catch (InterruptedException e) {
                                     Thread.currentThread().interrupt();
                                 } catch (Exception e) {
                                     logger_.warning(e.toString());
                                 }
                             }
                         }
                     });
            alert_thread_.start();
        }

    }

    @Override
    public void ancestorRemoved(AncestorEvent event) {
        if (null != thread_) {
            thread_.interrupt();
            thread_ = null;
        }
    }

    @Override
    protected void initComponents() {
        super.initComponents();

        setMinimumSize(new Dimension(1260, 35));
        setPreferredSize(new Dimension(1260, 35));

        // centerPane_以外は非表示
        northPane_.setVisible(false);
        southPane_.setVisible(false);
        westPane_.setVisible(false);
        eastPane_.setVisible(false);

        Font font14 = FontUtility.getFont("メイリオ", Font.PLAIN, 14);

        l企業名 = new JLabel();
        l企業名.setFont(font14);
        l企業名.setMinimumSize(new Dimension(500, 30));
        l企業名.setPreferredSize(new Dimension(500, 30));
        addContainer(
                 centerPane_, l企業名,
                 0, 0, 1, 1, 0.1, 1.0,
                 GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL,
                 new Insets(2, 2, 2, 2), 0, 0);

        //メッセージ
        messageLabel_ = new JLabel("メッセージ", JLabel.CENTER);
        messageLabel_.setFont(font14);
        messageLabel_.setBackground(Color.WHITE);
        messageLabel_.setMinimumSize(new Dimension(150, 30));
        messageLabel_.setPreferredSize(new Dimension(150, 30));
        addContainer(
                 centerPane_, messageLabel_,
                 3, 0, 1, 1, 0.0, 0.0,
                 GridBagConstraints.EAST, GridBagConstraints.NONE,
                 new Insets(2, 2, 2, 2), 0, 0);
        // 日時
        timeField_ = new JLabel("2012/01/01 12:00:00", JLabel.CENTER);
        timeField_.setFont(font14);
        timeField_.setBorder(BorderFactory.createEtchedBorder());
        timeField_.setMinimumSize(new Dimension(200, 30));
        timeField_.setPreferredSize(new Dimension(200, 30));
        addContainer(
                 centerPane_, timeField_,
                 4, 0, 1, 1, 0.0, 1.0,
                 GridBagConstraints.EAST, GridBagConstraints.NONE,
                 new Insets(2, 2, 2, 2), 0, 0);

        // ダミー
        JPanel panel = new JPanel(new GridBagLayout());
        addContainer(
                 centerPane_, panel,
                 1, 0, 1, 1, 0.8, 1.0,
                 GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                 new Insets(2, 2, 2, 2), 0, 0);
    }

    @Override
    protected void updateView() {
        // 時計表示の更新
        String s = String.format(
                 "%1$tY/%1$tm/%1$td %1$tH:%1$tM:%1$tS",
                 Calendar.getInstance());
        timeField_.setText(s);
        messageLabel_.setText(mfc_message_);
    }
    private java.applet.AudioClip ac_;

    private void checkSoundEvent() {

        try {
            String sql = "SELECT * FROM zaiko.d_alert WHERE 警報発報f = true AND 警報確認f = false AND delete_flag = false ORDER BY 所属日, 商品コード  LIMIT 1";
            PreparedStatement ps = con_.prepareStatement(sql);
            try {
                ResultSet rs = ps.executeQuery();

                if (rs.next()) {

                    //音を鳴らす
                    try {
                        if (ac_ != null) {
                            ac_.stop();
                        }
                        ac_ = java.applet.Applet.newAudioClip(getClass().getResource("/gatein.wav"));
                        ac_.play();

                        message_ = "ID " + rs.getString("商品コード") + "　"
                                 + rs.getString("商品名") + "　　"
                                 + rs.getString("警報内容");
                        messageLabel_.setForeground(Color.RED);

                    } catch (Exception e) {
                    }
                } else {
                    message_ = "";
                }

            } finally {
                ps.close();
            }
        } catch (SQLException e) {
            logger_.warning(e.toString());
        }
    }

    private void setup() {

        try {
            String sql = "SELECT * FROM m_system WHERE sno = 1; ";
            PreparedStatement ps = con_.prepareStatement(sql);
            try {
                ResultSet rs = ps.executeQuery();

                if (rs.next()) {

                    //会社名取得
                    s企業名 = rs.getString("company_name");
                    //初期消費税率取得
                    i消費税率 = rs.getInt("tax");

                    if (DBManager.mode == 1) {
                        s起動モード = "計量モード(1)";
                    } else if (DBManager.mode == 11) {
                        s起動モード = "計量モード(11)";
                    } else if (DBManager.mode == 2) {
                        s起動モード = "計量モード(2)";
                    } else if (DBManager.mode == 12) {
                        s起動モード = "計量モード(12)";
                    } else if (DBManager.mode == 4) {
                        s起動モード = "管理本部モード";
                    } else if (DBManager.mode == 14) {
                        s起動モード = "請求書モード";
                    } else if (DBManager.mode == 5) {
                        s起動モード = "計量テストモード(5)";
                    } else if (DBManager.mode == 10) {
                        s起動モード = "計量モード(10)";
                    } else if (DBManager.mode == 20) {
                        s起動モード = "マスタ転送モード(20)";
                    } else if (DBManager.mode == 0) {
                        s起動モード = "計量モード(0)";
                    } else if (DBManager.mode == 200) {
                        s起動モード = "契約マスタ仮登録モード";
                    } else if (DBManager.mode == 201) {
                        s起動モード = "クライアントモード(1)";
                    } else if (DBManager.mode == 202) {
                        s起動モード = "クライアントモード(2)";
                    } else {
                        s起動モード = "";
                    }

                    l企業名.setText(s企業名 + " - " + s起動モード);

                }

            } finally {
                ps.close();
            }
        } catch (SQLException e) {
            logger_.warning(e.toString());
        }
    }
}
