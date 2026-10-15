package aks.keiryo.status;

public class ContStruct extends CommonStruct {

    public ContStruct() {
        Clear();
    }

    public void Clear() {
        mValue.clear();

        putEx("id", "", TYPE_STRING);
        putEx("name", "", TYPE_STRING);
    }
}
