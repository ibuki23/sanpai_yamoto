package aks.keiryo.status;

public class CarNoStruct extends CommonStruct {

    public CarNoStruct() {
        Clear();
    }

    public void Clear() {

        putEx("car_no_id", "", TYPE_STRING);
        putEx("car_no", "", TYPE_STRING);

    }
}
