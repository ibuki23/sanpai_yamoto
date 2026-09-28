package aks.keiryo.status;

public class TransporterStruct extends CommonStruct {

    public TransporterStruct() {
        Clear();
    }

    public void Clear() {
        mValue.clear();

        putEx("id", "", TYPE_STRING);
        putEx("name", "", TYPE_STRING);
    }
}
