package aks.keiryo.status;

public class OfficeStruct extends CommonStruct {

    public OfficeStruct() {
        Clear();
    }

    public void Clear() {
        mValue.clear();

        putEx("id", "", TYPE_STRING);
        putEx("name", "", TYPE_STRING);
    }
}
