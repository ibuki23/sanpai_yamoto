package aks.keiryo.status;

public class DataStruct {

    public boolean mAccept;//受領要求フラグ
    public boolean mExist;//データ有無フラグ
    public String mData;//ステータスデータ

    //コンストラクタ
    public DataStruct() {
        Clear();
    }

    //ステータスクリア
    public void Clear() {
        mAccept = false;
        mExist = false;
        mData = "";
    }
}
