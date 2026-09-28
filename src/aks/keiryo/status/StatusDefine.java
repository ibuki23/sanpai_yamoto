package aks.keiryo.status;

public interface StatusDefine {

    //イベントインデックス定義
    public final static int INDEX_WEIGHT = 0;//計量

    public final static int INDEX_ENT = 1;//排出事業者
    public final static int INDEX_TRANS = 2;//運搬業者
    public final static int INDEX_KIND = 3;//種類
    public final static int INDEX_CAR_ID = 4;//車両ID(車種+車番の5～6桁)
    public final static int INDEX_OFFICE = 5;//排出事業場

    public final static int INDEX_INPUT_SHIPMENT = 6;//出荷
    public final static int INDEX_INPUT_ARRAIVAL = 7;//入荷

    public final static int INDEX_CONT = 8;//処分業者
    public final static int INDEX_BILL = 9;//請求先
    public final static int INDEX_CAR_KIND = 10;//車種

    public final static int INDEX_CLASS = 11;
    public final static int INDEX_INPUT_CONFIRM = 12;//確認
    public final static int INDEX_INPUT_CANCEL = 13;//キャンセル
    public final static int INDEX_INPUT_RESET = 14;//キャンセル2

    public final static int INDEX_SANGYO = 15;
    public final static int INDEX_KENSETSU = 16;
    public final static int INDEX_SANGYO2 = 17;

    public final static int INDEX_EMPTY = 18;
    public final static int INDEX_OFFICE_SUB = 19;
    public final static int INDEX_INPUT_CONFIRM2 = 20;//登録
    //20120914 add
    public final static int INDEX_M3 = 21;//リューベ
    public final static int INDEX_G = 23;//比重係数

    public final static int INDEX_CAR_NO = 22;//車番
    //20120422
    public final static int INDEX_UNIT_VALUE = 24;//料金単価
    public final static int INDEX_CHARGE_VALUE = 25;//料金
    //INDEX_NET_WEIGHt
    public final static int INDEX_NET_WEIGHT = 26;//重量計算
    public final static int INDEX_MESSAGE = 27;//メッセージ表示

    public final static int INDEX_KANRI_ID = 30;//管理ID

    public final static int INDEX_INPUT_REMOTEON = 900;//遠隔設定
    public final static int INDEX_INPUT_REMOTEOFF = 901;//遠隔解除
    public final static int INDEX_WINDOW_OPEN = 910;//ウィンドウ表示
    public final static int INDEX_WINDOW_CLOSE = 911;//ウィンドウクローズ

    //イベントインデックス定義２
    public final static int INDEX_EX_WEIGH_INTERVAL = 0;//最小計量可能間隔更新
    public final static int INDEX_EX_MIN_WEIGHT = 1;//最小計量重量更新

    //計量目的ID定義
    public final static String PURPOSE_ID_SHIPMENT = "000001";
    public final static String PURPOSE_ID_ARRAIVAL = "000002";
    public final static String PURPOSE_ID_RECOVERY = "000007";
    public final static String PURPOSE_ID_ANALYSIS = "000003";
    public final static String PURPOSE_ID_DELIVERY = "000004";
    public final static String PURPOSE_ID_ALL_WEIGHT = "000005";
    public final static String PURPOSE_ID_GROSS_WEIGHT = "000006";

    //ゲート通過方向定義
    public final static int GATE_IN = 0;//入構
    public final static int GATE_OUT = 1;//出構

    public final static int UNKNOWN_STATE = -1;//状態不明フラグ

    //リモート制御フラグ定義
    public final static int REMOTE_KEIRYO = 0;//計量室側操作
    public final static int REMOTE_KANRI = -1;//管理室側操作
}
