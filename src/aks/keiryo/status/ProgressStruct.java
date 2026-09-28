package aks.keiryo.status;

public class ProgressStruct implements StatusDefine {

    public boolean mConfirmed;//確認済フラグ
    public boolean mWeightReady;//重量表示準備フラグ

    public int mGateDirection;//ゲート通過方向
    public int mMeasureDirection;//計量測定値方向
    public boolean mStayDataExist;//滞留データ有無
    public boolean mShipmentDataExist;//計量データ有無
    public int mGateInPurpose;//入構時目的ID
    public int mGateOutPurpose;//出構時目的ID
    public int mFinalWeight;//最終確定の重量値

    public ProgressStruct() {
        Clear();
    }

    public void Clear() {
        mConfirmed = false;
        mWeightReady = false;

        mGateDirection = UNKNOWN_STATE;
        mMeasureDirection = UNKNOWN_STATE;
        mStayDataExist = false;
        mShipmentDataExist = false;
        mGateInPurpose = UNKNOWN_STATE;
        mGateOutPurpose = UNKNOWN_STATE;
        mFinalWeight = 0;
    }

}
