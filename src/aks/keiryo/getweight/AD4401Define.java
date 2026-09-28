package aks.keiryo.getweight;

public interface AD4401Define {

    public final static byte[] CMD_STX = {0x02};
    public final static byte[] CMD_ETX = {0x03};
    public final static byte[] CMD_CR = {0x0D};
    public final static byte[] CMD_LF = {0x0A};
    public final static byte[] CMD_g = {0x67};
    public final static byte[] CMD_G = {0x47};
    public final static byte[] CMD_S = {0x53};
    public final static byte[] CMD_T = {0x54};
    public final static byte[] CMD_U = {0x55};
    public final static byte[] CMD_DELM = {'\r', '\n'};//デミリタコード

    public static int RECV_FLASH_TMOUT = 200;//フラッシュ時の受信タイムアウト(msec)
    public static int RECV_FLASH_WAIT = 200;//フラッシュ後ウェイト(msec)
    public static int RECV_TMOUT = 1000;//受信タイムアウト(msec)

    //**************************************************************************
    public final static int CMD_TYPE_ZERO = 0;//ゼロリセットコマンド
    public final static int CMD_TYPE_QUERY = 1;//問い合わせコマンド
    public final static int CMD_TYPE_STATUS = 2;//ステータス問い合わせコマンド
    public final static int RECV_CODE_00 = 0;//受信データ正常
    public final static int RECV_CODE_74 = 74;//受信データ正常
    public final static int RECV_CODE_75 = 75;//受信データ正常
    public final static int RECV_CODE_76 = 76;//受信データ正常
    public final static int RECV_ERR = -1;//受信データエラー
    public final static int SEND_ERR = -1;//送信データエラー

}
