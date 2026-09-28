package aks.industrialwaste.recycle.ui.sql;

public interface SQLDefine {

    public final static int SQL_OK = 0;
    public final static int SQL_ERR = -1;
    public final static int STAY_STAT_ON = 0;
    public final static int STAY_STAT_OFF = -1;

    public final static String PG_JDBC_DRIVER = "org.postgresql.Driver";

    public final static String SCHEME_NAME = "";

    public final static String ENV_TABLE_NAME = "sys_system";
    public final static String RFID_ENV_TABLE_NAME = "sys_rfid";
    public final static String DLC_ENV_TABLE_NAME = "sys_dlc";
    public final static String USER_TABLE_NAME = "m_user";
    public final static String USER_CUSTOMER_TABLE_NAME = "m_customer_list_each_user";
    public final static String REMOTE_TABLE_NAME = "sys_remote_control";
    public final static String DIVISION_TABLE_NAME = "m_division";
    public final static String CAR_VIEW_NAME = "v1_transporter";
    public final static String TRAILER_TABLE_NAME = "m_trailer_head";
    public final static String BUSINESS_TABLE_NAME = "m_business_type";
    public final static String GARBAGE_TABLE_NAME = "m_garbage_type";
    public final static String CARD_TABLE_NAME = "v1_card";
    public final static String CARRIER_TABLE_NAME = "m_carrier";

    public final static String ENT_TABLE_NAME = "v0_ent";
    public final static String TRANS_TABLE_NAME = "v0_trans";
    public final static String KIND_TABLE_NAME = "v0_goods_kind";
    public final static String OFFICE_TABLE_NAME = "v0_office";
    public final static String CONT_TABLE_NAME = "v0_cont";
    public final static String CAR_KIND_TABLE_NAME = "v0_car_kind";
    public final static String CAR_TABLE_NAME = "v0_car";

    public final static String PURPOSE_TABLE_NAME = "m_weigh_purpose";
    public final static String CHARGE_GATE_TABLE_NAME = "m_charge_gate";
    public final static String CHARGE_GATE_VTABLE_NAME = "v1_charge_gate";

    public final static String SHIPMENT_TABLE_NAME = "weigh_data";
    public final static String ANALYSIS_TABLE_NAME = "analysis_data";

    public final static String GATE_IN_VTABLE_NAME = "v1_gate_in_data";
    public final static String SHIPMENT_VTABLE_NAME = "v1_weigh_data";
    public final static String ANALYSIS_VTABLE_NAME = "v1_analysis_data";

    public final static String WEIGH_REPORT_TABLE_NAME = "v1_weigh_report";
    public final static String ANALYSIS_REPORT_TABLE_NAME = "v1_analysis_report";

    public final static String MESSAGE_TABLE_NAME = "message_list";
    public final static String JOURNAL_TABLE_NAME = "weigh_journal";

}
