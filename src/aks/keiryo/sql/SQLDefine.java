package aks.keiryo.sql;

public interface SQLDefine {

    public final static int SQL_OK = 0;
    public final static int SQL_ERR = -1;

    public final static String PG_JDBC_DRIVER = "org.postgresql.Driver";

    public final static String SCHEME_NAME = "";

    public final static String ENV_TABLE_NAME = "sys_system";
    public final static String PRINTER_TABLE_NAME = "sys_printer";
    public final static String ENT_TABLE_NAME = "v0_ent";
    public final static String TRANS_TABLE_NAME = "v0_trans";
    public final static String KIND_TABLE_NAME = "v0_goods_kind";
    public final static String OFFICE_TABLE_NAME = "v0_office";
    public final static String CONT_TABLE_NAME = "v0_cont";
    public final static String CAR_KIND_TABLE_NAME = "v0_car_kind";
    public final static String CAR_TABLE_NAME = "v0_car";
    public final static String BILL_TABLE_NAME = "v0_bill";
    public final static String GATE_IN_TABLE_NAME = "gate_in_data";
    public final static String SHIPMENT_TABLE_NAME = "weigh_data";
    public final static String GATE_IN_VTABLE_NAME = "v1_gate_in_data";
    public final static String SHIPMENT_VTABLE_NAME = "v1_weigh_data";
    public final static String JOURNAL_TABLE_NAME = "weigh_journal";
}
