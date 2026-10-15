package aks.keiryo.status;

public class KindStruct extends CommonStruct {

    public KindStruct() {
        Clear();
    }

    public void Clear() {
        mValue.clear();

        putEx("id", "", TYPE_STRING);
        putEx("name", "", TYPE_STRING);
    }
}
