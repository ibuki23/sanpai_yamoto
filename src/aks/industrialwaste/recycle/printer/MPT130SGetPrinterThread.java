package aks.industrialwaste.recycle.printer;

import aks.industrialwaste.database.DBManager;
import aks.keiryo.gui.WeighPriceIntegration;
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
        WeighPriceIntegration.install(con);

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
        // NPort logs connection failures and recovery with retry suppression.
        transport.open();
    }

    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            try {
                shipmentSno = sqlCommon.GetShipmentPrintOrder();
                System.out.println("MPT130S PRINT CHECK shipmentSno=" + shipmentSno);

                if (shipmentSno > 0) {
                    Thread.sleep(WAIT_AFTER_SEND);
                    if (printoutWeight(shipmentSno)) {
                        Thread.sleep(WAIT_AFTER_SEND);
                        sqlCommon.SetPrintOrder(shipmentSno, false);
                        System.out.println("MPT130S PRINT COMPLETE sno=" + shipmentSno);
                    } else {
                        System.out.println("MPT130S PRINT FAILED sno=" + shipmentSno);
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
        System.out.println("MPT130S PRINT START sno=" + sno);

        ShipmentStruct shipment = new ShipmentStruct();
        int getShipmentResult = sqlCommon.GetShipmentData(sno, shipment);
        boolean printOrder = shipment.GetBoolean("printer_print_order");
        System.out.println(
                 "MPT130S SHIPMENT LOAD sno=" + sno
                 + " result=" + getShipmentResult
                 + " printer_print_order=" + printOrder
        );

        if (getShipmentResult < 0 || !printOrder) {
            System.out.println("MPT130S PRINT ABORT sno=" + sno);
            return false;
        }

        List<byte[]> commands = MPT130SPrintCommandBuilder.build(shipment);
        System.out.println("MPT130S COMMAND COUNT sno=" + sno + " count=" + commands.size());

        int commandIndex = 0;
        for (byte[] command : commands) {
            commandIndex++;
            System.out.println(
                     "MPT130S SEND sno=" + sno
                     + " command=" + commandIndex + "/" + commands.size()
                     + " bytes=" + (command == null ? -1 : command.length)
            );

            if (!transport.send(command)) {
                System.out.println(
                         "MPT130S printer send failed:" + transport.getDescription()
                         + " sno=" + sno
                         + " command=" + commandIndex
                );
                return false;
            }

            System.out.println(
                     "MPT130S SEND OK sno=" + sno
                     + " command=" + commandIndex + "/" + commands.size()
            );

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
