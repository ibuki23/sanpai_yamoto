package aks.industrialwaste.recycle.printer;

import aks.industrialwaste.database.DBManager;
import aks.keiryo.sql.SQLCommon;
import aks.keiryo.sql.SQLDefine;
import aks.keiryo.status.IDEnvStruct;
import aks.keiryo.status.ShipmentStruct;
import java.sql.Connection;
import java.util.List;

/**
 * NADA MPT-130S用の計量票印字スレッド。
 */
public class MPT130SGetPrinterThread implements Runnable, MPT130SDefine {

    public static final int WAIT_AFTER_SEND = 500;
    public static final int LOOP_WAIT = 1000;

    private final SQLCommon sqlCommon;
    private final MPT130STransport transport;
    private long shipmentSno = -1;

    public MPT130SGetPrinterThread(Connection con) {
        sqlCommon = new SQLCommon(
                 DBManager.jdbc_, DBManager.getUser(), DBManager.getPassword());

        IDEnvStruct setting = new IDEnvStruct();
        sqlCommon.GetIDEnv(setting, SQLDefine.PRINTER_TABLE_NAME, "100");
        System.out.println(
                 "MPT130S CONFIG address=" + setting.mAddress
                 + " port=" + setting.mPort
                 + " timeout=" + setting.mTimeout
                 + " table=" + SQLDefine.PRINTER_TABLE_NAME
        );
        transport = new MPT130STcpTransport(
                 setting.mAddress, setting.mPort, setting.mTimeout);
        setup();
    }

    public final void setup() {
        if (!transport.open()) {
            System.out.println("MPT130S printer open failed:" + transport.getDescription());
        }
    }

    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            try {
                shipmentSno = sqlCommon.GetShipmentPrintOrder();
                if (shipmentSno > 0) {
                    Thread.sleep(WAIT_AFTER_SEND);
                    if (printoutWeight(shipmentSno)) {
                        Thread.sleep(WAIT_AFTER_SEND);
                        sqlCommon.SetPrintOrder(shipmentSno, false);
                    } else {
                        setup();
                    }
                    shipmentSno = -1;
                }
                Thread.sleep(LOOP_WAIT);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            } catch (Exception e) {
                System.out.println("MPT130SGetPrinterThread-run-exp:" + e);
            }
        }
        transport.close();
    }

    boolean printoutWeight(long sno) {
        ShipmentStruct shipment = new ShipmentStruct();
        if (sqlCommon.GetShipmentData(sno, shipment) < 0
                 || !shipment.GetBoolean("printer_print_order")) {
            return false;
        }

        List<byte[]> commands = MPT130SPrintCommandBuilder.build(shipment);
        for (byte[] command : commands) {
            if (!transport.send(command)) {
                System.out.println("MPT130S printer send failed:" + transport.getDescription());
                return false;
            }
            try {
                Thread.sleep(WAIT_AFTER_SEND);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return false;
            }
        }
        return true;
    }

}
