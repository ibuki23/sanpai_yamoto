package aks.keiryo.nport;

import java.net.*;

public class NPort {

    public final static int FLASH_TMOUT = 200;//フラッシュ時の受信タイムアウト(msec)
    public final static int FLASH_WAIT = 200;//フラッシュ後ウェイト(msec)
    public final static int NPORT_OK = 0;
    public final static int NPORT_ERR = -1;

    private String mHostName;
    private int mHostPort;
    private int mTimeOut;
    private Socket mSock = null;

    //コンストラクタ
    //hostname:接続先アドレス
    //hostport:接続先ポート
    //tmout:接続タイムアウト時間(msec)
    public NPort(String hostname, int hostport, int tmout) {
        Setup(hostname, hostport, tmout);
    }

    ;

    //ポート初期化
    //ret:異常時=NPORT_ERR
    public int Open() {
        int ret = NPORT_ERR;

        Close();//一旦終了

        System.out.println(
                "NPORT OPEN TRY " + mHostName + ":" + mHostPort
                + " timeout=" + mTimeOut
                + " thread=" + Thread.currentThread().getName());

        try {

            mSock = new Socket();
            mSock.connect(new InetSocketAddress(InetAddress.getByName(mHostName), mHostPort), mTimeOut);
            ret = NPORT_OK;
            System.out.println(
                    "NPORT OPEN OK " + mHostName + ":" + mHostPort
                    + " thread=" + Thread.currentThread().getName());
        } catch (Exception e) {
            System.out.println(
                    "nport-open-err/" + e + ":" + mHostName + ":" + mHostPort
                    + " thread=" + Thread.currentThread().getName());
        }

        return ret;
    }

    //ポート終了
    public void Close() {
        try {
            if (mSock != null && !mSock.isClosed()) {
                mSock.close();
            }
        } catch (Exception e) {
            System.out.println("nport-close-err/" + e + ":" + mHostName + ":" + mHostPort);
        }
    }

    //再セットアップ
    public void Setup(String hostname, int hostport, int tmout) {
        Close();//一旦終了
        mHostName = hostname;
        mHostPort = hostport;
        mTimeOut = tmout;
    }

    //送信
    //data:送信バイト配列
    //len:送信バイト数
    //ret:異常時=NPORT_ERR
    public int Send(byte[] data, int len) {
        int ret = NPORT_ERR;

        try {
            if (!mSock.isConnected() || mSock.isClosed()) {
                Open();
            }

            mSock.getOutputStream().write(data, 0, len);
            ret = NPORT_OK;
        } catch (Exception e) {
            System.out.println(
                    "nport-send-err/" + e + ":" + mHostName + ":" + mHostPort
                    + " thread=" + Thread.currentThread().getName());
            Open();
        }

        return ret;
    }

    //受信
    //data:受信バイト配列
    //len:受信最大バイト数
    //tmout:受信タイムアウト時間(msec)
    //ret:>=0(受信バイト数) 異常時=NPORT_ERR
    public int Read(byte[] data, int len, int tmout) {
        int ret = NPORT_ERR;

        try {
            if (!mSock.isConnected() || mSock.isClosed()) {
                Open();
            }

            mSock.setSoTimeout(tmout);//タイムアウト設定

            int size = mSock.getInputStream().read(data, 0, len - 1);//データ受信
            if (size < 0) {//ストリーム終端に達した場合 0バイト受信
                data[0] = 0;
                ret = 0;//受信バイト数=0
            } else {
                data[size] = 0;
                ret = size;//受信バイト数セット
            }
        } catch (Exception e) {
        }
        return ret;
    }
}
