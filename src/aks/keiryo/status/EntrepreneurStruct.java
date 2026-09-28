package aks.keiryo.status;

public class EntrepreneurStruct extends CommonStruct {

    public EntrepreneurStruct() {
        Clear();
    }

    public void Clear() {
        mValue.clear();

        putEx("id", "", TYPE_STRING);
        putEx("name", "", TYPE_STRING);
    }
}
