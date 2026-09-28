package aks.keiryo.status;

public class BillStruct extends CommonStruct {

    public BillStruct() {
        Clear();
    }

    public void Clear() {
        mValue.clear();

        putEx("id", "", TYPE_STRING);
        putEx("name", "", TYPE_STRING);
    }
}
