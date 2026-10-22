package aks.industrialwaste.recycle.printer;

import aks.keiryo.nport.NPort;
import aks.keiryo.util.ByteStr;

/**
 * NPort等のTCPシリアル変換器を経由してMPT-130Sへ送信する。
 */
class MPT130STcpTransport implements MPT130STransport {

    private final String address;
    private final int port;
    private final int timeout;
    private NPort nport;

    MPT130STcpTransport(String address, int port, int timeout) {
        this.address = address;
        this.port = port;
        this.timeout = timeout;
    }

    @Override
    public boolean open() {
        close();
        nport = new NPort(address, port, timeout);
        boolean opened = nport.Open() == NPort.NPORT_OK;
        return opened;
    }

    @Override
    public boolean send(byte[] data) {
        if (data == null) {
            return false;
        }
        if (nport == null && !open()) {
            return false;
        }
        return nport.Send(data, ByteStr.length(data)) == NPort.NPORT_OK;
    }

    @Override
    public void close() {
        if (nport != null) {
            nport.Close();
            nport = null;
        }
    }

    @Override
    public String getDescription() {
        return address + ":" + port;
    }
}
