package aks.keiryo.getweight;

import aks.industrialwaste.recycle.ui.MainFrame;
import aks.keiryo.gui.MainPanel;
import aks.keiryo.nport.NPort;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 * A&amp;D AD-4401用の重量取得スレッド。
 */
public class AD4401GetWeightThread implements Runnable, AD4401Define {

    public static final int LOOP_WAIT = 500;
    private static final int COMMUNICATION_ERROR_LIMIT = 100;
    private static final int COMMUNICATION_ERROR_WEIGHT = 99999;
    private static final int DATA_ERROR_WEIGHT = 99998;
    private static final int DLC_SNO = 100;

    private final Connection initialConnection;
    private NPort nport;
    private String address = "";
    private int port = 0;
    private int timeout = RECV_TMOUT;

    public AD4401GetWeightThread(Connection con) {
        initialConnection = con;
        loadDlcSetting();
        nport = new NPort(address, port, timeout);
        nport.Open();
    }

    public void Setup() {
        try {
            nport.Open();
        } catch (Exception e) {
            System.out.println("AD4401GetWeightThread-Setup-exp:" + e);
        }
    }

    @Override
    public void run() {
        Setup();

        int oldWeight = Integer.MIN_VALUE;
        String oldStatus = "";
        int errorCount = 0;

        while (!Thread.currentThread().isInterrupted()) {
            try {
                if (AD4401Recv.Recv(nport, timeout) == RECV_ERR) {
                    errorCount++;
                    if (errorCount > COMMUNICATION_ERROR_LIMIT
                             && (oldWeight != COMMUNICATION_ERROR_WEIGHT
                             || !"US".equals(oldStatus))) {
                        updateDlc(COMMUNICATION_ERROR_WEIGHT, "US");
                        oldWeight = COMMUNICATION_ERROR_WEIGHT;
                        oldStatus = "US";
                        Setup();
                    }
                } else if (!AD4401Recv.mDataWeightError) {
                    errorCount = 0;
                    MainPanel.weight_err = "";

                    int weight = AD4401Recv.mDataWeight;
                    String status = "ST".equals(AD4401Recv.mDataStat) ? "ST" : "US";
                    if (oldWeight != weight || !oldStatus.equals(status)) {
                        updateDlc(weight, status);
                        oldWeight = weight;
                        oldStatus = status;
                    }
                } else if (oldWeight != DATA_ERROR_WEIGHT || !"US".equals(oldStatus)) {
                    updateDlc(DATA_ERROR_WEIGHT, "US");
                    oldWeight = DATA_ERROR_WEIGHT;
                    oldStatus = "US";
                }

                Thread.sleep(LOOP_WAIT);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            } catch (Exception e) {
                System.out.println("AD4401GetWeightThread-run-exp:" + e);
            }
        }

        nport.Close();
    }

    private void updateDlc(int weight, String status) {
        Connection con = getConnection();
        if (con == null) {
            return;
        }

        String sql = "update sys_dlc set last_value = ?, last_data = ? where sno = ?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, weight);
            ps.setString(2, status);
            ps.setInt(3, DLC_SNO);
            ps.executeUpdate();
        } catch (Exception e) {
            System.out.println("AD4401GetWeightThread-updateDlc-exp:" + e);
        }
    }

    private void loadDlcSetting() {
        Connection con = getConnection();
        if (con == null) {
            return;
        }

        String sql = "select ip_address, port_no, timeout from sys_dlc where sno = ?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, DLC_SNO);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    address = rs.getString("ip_address");
                    port = rs.getInt("port_no");
                    timeout = rs.getInt("timeout");
                }
            }
        } catch (Exception e) {
            System.out.println("AD4401GetWeightThread-loadDlcSetting-exp:" + e);
        }
    }

    private Connection getConnection() {
        if (initialConnection != null) {
            return initialConnection;
        }
        return MainFrame.con1_;
    }
}
