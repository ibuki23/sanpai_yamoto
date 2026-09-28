/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package aks.keiryo.status;

/**
 *
 * @author tss
 */
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
