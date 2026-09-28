package aks.keiryo.status;

public class Car_KindStruct extends CommonStruct {

    public Car_KindStruct() {
        Clear();
    }

    public void Clear() {
        mValue.clear();

        putEx("id", "", TYPE_STRING);
        putEx("name", "", TYPE_STRING);
    }
}
