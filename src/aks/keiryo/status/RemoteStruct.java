package aks.keiryo.status;

public class RemoteStruct extends CommonStruct {

    public RemoteStruct() {
        Clear();
    }

    public void Clear() {
        mValue.clear();
        mType.clear();

        putEx("dis_ent_id", "", TYPE_STRING);
        putEx("entrepreneur_name", "", TYPE_STRING);

        putEx("trans_id", "", TYPE_STRING);
        putEx("transporter_name", "", TYPE_STRING);

        putEx("goods_id", "", TYPE_STRING);
        putEx("goods_name", "", TYPE_STRING);

        putEx("car_no", "0", TYPE_STRING);
        //20121016 add
        putEx("car_id", "0", TYPE_STRING);
        putEx("sub_messsage", "", TYPE_STRING);
        putEx("carry_class", "", TYPE_STRING);
        putEx("carry_name", "", TYPE_STRING);

        putEx("dis_office_id", "", TYPE_STRING);
        putEx("office_name", "", TYPE_STRING);

        putEx("now_weight", "", TYPE_INTEGER);
        putEx("gate_in_weight", "", TYPE_INTEGER);
        putEx("gate_out_weight", "", TYPE_INTEGER);
        putEx("loading_weight", "", TYPE_INTEGER);
        putEx("empty_weight", "", TYPE_STRING);
        //max_weight
        putEx("max_weight", "", TYPE_STRING);

        putEx("dis_cont_id", "", TYPE_STRING);
        putEx("contractor_name", "", TYPE_STRING);

        putEx("car_kind_id", "", TYPE_STRING);
        putEx("car_kind_name", "", TYPE_STRING);

        putEx("manifesto_no1", "", TYPE_STRING);
        putEx("manifesto_no2", "", TYPE_STRING);
        putEx("manifesto_no3", "", TYPE_STRING);

        putEx("class", "", TYPE_INTEGER);

        putEx("bill_id", "", TYPE_STRING);
        putEx("bill_name", "", TYPE_STRING);

        putEx("office_sub_name", "", TYPE_STRING);

        putEx("m3", "", TYPE_INTEGER);
        putEx("unit_value", "", TYPE_INTEGER);
        putEx("charge_value", "", TYPE_INTEGER);
        //契約内容表示
        putEx("契約内容表示", "", TYPE_STRING);
        putEx("売上区分", "", TYPE_STRING);

    }
}
