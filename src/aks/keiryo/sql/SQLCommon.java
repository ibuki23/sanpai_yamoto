package aks.keiryo.sql;

import aks.keiryo.gui.MainPanel;
import aks.keiryo.util.*;

import aks.keiryo.status.*;
import static aks.keiryo.gui.MainPanel.cmb売上区分;

public class SQLCommon implements SQLDefine {

    private SQL mSql;

    //コンストラクタ
    public SQLCommon(String url, String user, String pwd) {
        mSql = new SQL(url, user, pwd);
    }

    //環境変数読み込み
    //es:環境変数格納先
    //sortkey:ソートキー
    //ret:失敗時=SQL_ERR
    public int GetEnv(OfficeStruct es, String sortkey) {
        int ret = SQL_ERR;
        String sql = "";
        String key = "";

        es.Clear();

        try {
            sql = String.format("select * from %s%s where delete_flag<>true order by %s;",
                     SCHEME_NAME,
                     ENV_TABLE_NAME,
                     sortkey);
            if ((ret = mSql.Query(sql)) != SQL_ERR) {
                if (mSql.mResultSet.next()) {
                    for (int i = 1; i <= mSql.mMetaData.getColumnCount(); i++) {
                        key = mSql.mMetaData.getColumnName(i);
                        es.mValue.put(key, mSql.mResultSet.getObject(key));
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("SQLCommon-GetEnv-exp");
            ret = SQL_ERR;
        }

        return ret;
    }

    //キャリアID＆計量値の通信設定読み込み
    public int GetIDEnv(IDEnvStruct ies, String tablename, String key) {
        int ret = SQL_ERR;
        String sql = "";

        ies.Clear();

        try {
            sql = String.format("select * from %s%s where sno=%s and delete_flag<>true;",
                     SCHEME_NAME,
                     tablename,
                     key);
            if ((ret = mSql.Query(sql)) != SQL_ERR) {
                if (mSql.mResultSet.next()) {
                    ies.mAddress = mSql.mResultSet.getString("ip_address");
                    ies.mPort = mSql.mResultSet.getInt("port_no");
                    ies.mTimeout = mSql.mResultSet.getInt("timeout");
                }
            }
        } catch (Exception e) {
            System.out.println("SQLCommon-GetIDEnv-exp");
            ret = SQL_ERR;
        }

        return ret;
    }

    //滞留データ取得
    //rs:現在状態
    //ss:滞留データ
    //ret:滞留データSNO 無時=SQL_ERR
    public long GetStayData(RemoteStruct rs, StayStruct ss) {
        return GetStayData_(GATE_IN_VTABLE_NAME, rs, ss);
    }

    public long GetStayDataRaw(RemoteStruct rs, StayStruct ss) {
        return GetStayData_(GATE_IN_TABLE_NAME, rs, ss);
    }

    public long GetStayDataRaw2(RemoteStruct rs, StayStruct ss) {
        return GetStayData2_(GATE_IN_TABLE_NAME, rs, ss);
    }

    public long GetStayDataRaw3(RemoteStruct rs, StayStruct ss, String data) {
        return GetStayData3_(GATE_IN_TABLE_NAME, rs, ss, data);
    }

    public long GetStayData_(String table_name, RemoteStruct rs, StayStruct ss) {
        long ret = SQL_ERR;
        String sql = "";
        String key;

        ss.Clear();

        try {
            sql = String.format("select * from %s%s where car_id = %s "
                     + " and delete_flag <> true;",
                     SCHEME_NAME,
                     "v1_gate_in_data",
                     rs.GetString("car_id"));
            if (mSql.Query(sql) != SQL_ERR) {
                if (mSql.mResultSet.next()) {
                    for (int i = 1; i <= mSql.mMetaData.getColumnCount(); i++) {
                        key = mSql.mMetaData.getColumnName(i);
                        ss.mValue.put(key, mSql.mResultSet.getObject(key));
                    }
                    ret = Long.parseLong(mSql.mResultSet.getObject("sno").toString());//戻り値に滞留SNO
                }
            }
        } catch (Exception e) {
            System.out.println("SQLCommon-GetStayData-exp");
            ret = SQL_ERR;
        }
        return ret;
    }

    public long GetStayData2_(String table_name, RemoteStruct rs, StayStruct ss) {
        long ret = SQL_ERR;
        String sql = "";
        String key;

        ss.Clear();

        try {
            sql = String.format("select * from %s%s where car_id = %s "
                     + " and delete_flag<>true;",
                     SCHEME_NAME,
                     "v1_gate_in_data",
                     rs.GetString("car_id"));
            if (mSql.Query(sql) != SQL_ERR) {
                if (mSql.mResultSet.next()) {
                    for (int i = 1; i <= mSql.mMetaData.getColumnCount(); i++) {
                        key = mSql.mMetaData.getColumnName(i);
                        ss.mValue.put(key, mSql.mResultSet.getString(key));
                    }
                    ret = Long.parseLong(mSql.mResultSet.getObject("sno").toString());//戻り値に滞留SNO
                }
            }
        } catch (Exception e) {
            System.out.println("SQLCommon-GetStayData-exp");
            ret = SQL_ERR;
        }
        return ret;
    }

    public long GetStayData3_(String table_name, RemoteStruct rs, StayStruct ss, String data) {
        long ret = SQL_ERR;
        String sql = "";
        String key;

        ss.Clear();

        try {
            sql = String.format("select * from %s%s where car_id = %s "
                     + " and delete_flag<>true;",
                     SCHEME_NAME,
                     "v1_gate_in_data",
                     data);
            if (mSql.Query(sql) != SQL_ERR) {
                if (mSql.mResultSet.next()) {
                    for (int i = 1; i <= mSql.mMetaData.getColumnCount(); i++) {
                        key = mSql.mMetaData.getColumnName(i);
                        ss.mValue.put(key, mSql.mResultSet.getObject(key));
                    }
                    ret = Long.parseLong(mSql.mResultSet.getObject("sno").toString());//戻り値に滞留SNO
                }
            }
        } catch (Exception e) {
            System.out.println("SQLCommon-GetStayData-exp");
            ret = SQL_ERR;
        }
        return ret;
    }

    //滞留データ作成
    //rs:現在状態
    //ret:失敗時 SQL_ERR
    public int SetStayData(RemoteStruct rs) {
        String sql = "";

        // 入構データを作成
        sql = String.format("insert into %s%s"
                 + "(register_date, "
                 + " register_weight, "
                 + " dis_ent_id, "
                 + " trans_id, "
                 + " goods_id, "
                 + " dis_office_id, "
                 + " dis_cont_id, "
                 + " car_kind_id, "
                 + " car_no, "
                 + " manifesto_no1, "
                 + " manifesto_no2, "
                 + " manifesto_no3, "
                 + " class, "
                 + " bill_id, "
                 + " office_sub_name, "
                 + " m3, "
                 + " car_id, "
                 + " carry_class, "
                 + " ryube, "
                 + " unit_value, "
                 + " unit_no,"
                 + " 売上区分) "
                 + "values(now(), %s, %s, %s, %s, %s, %s, %s, %s, '%s', '%s', '%s', %s, %s, '%s', %s, '%s', %s, %s, %s, %s, '%s');",
                 SCHEME_NAME, GATE_IN_TABLE_NAME,
                 MainPanel.loading_weight_,
                 rs.GetString("dis_ent_id"),
                 rs.GetString("trans_id"),
                 rs.GetString("goods_id"),
                 rs.GetString("dis_office_id"),
                 rs.GetString("dis_cont_id"),
                 rs.GetString("car_kind_id"),
                 rs.GetString("car_no"),
                 rs.GetString("manifesto_no1"),
                 rs.GetString("manifesto_no2"),
                 rs.GetString("manifesto_no3"),
                 0,
                 rs.GetString("bill_id"),
                 rs.GetString("office_sub_name"),
                 0.0,
                 rs.GetString("car_id"),
                 rs.GetString("carry_class"),
                 rs.GetString("ryube"),
                 rs.GetString("unit_value"),
                 rs.GetString("unit_no"),
                 String.valueOf(cmb売上区分.getSelectedItem()));

        return mSql.Update(sql);
    }

    //滞留データ作成
    //rs:現在状態
    //ret:失敗時 SQL_ERR
    public int SetStayData1(RemoteStruct rs) {
        String sql = "";

        int weight = 0;
        try {
            sql = String.format("select * from v0_car where car_id =%s and delete_flag<>true;",
                     rs.GetString("car_id"));

            mSql.Select(sql);
            mSql.mResultSet2.next();

            weight = mSql.mResultSet2.getInt("empty_weight");

        } catch (Exception e) {

        }

        //搬入時は総重量が１回目
        if (rs.GetInt("carry_class") == 0) {
            weight = rs.GetInt("total_weight");
        }
        // 入構データを作成
        sql = String.format(
                 "insert into %s%s"
                 + "(register_date, "
                 + " register_weight, "
                 + " dis_ent_id, "
                 + " trans_id, "
                 + " goods_id, "
                 + " dis_office_id, "
                 + " dis_cont_id, "
                 + " car_kind_id, "
                 + " car_no, manifesto_no1, "
                 + "manifesto_no2, "
                 + "manifesto_no3, "
                 + "class, "
                 + "bill_id, "
                 + "m3, "
                 + "car_id, "
                 + "carry_class, "
                 + "ryube, "
                 + "unit_value, unit_no) "
                 + "values(now(), %s, %s, %s, %s, %s, %s, %s, %s, '%s', '%s','%s', %s, %s, %s, '%s', %s, %s, %s, %s );",
                 SCHEME_NAME, GATE_IN_TABLE_NAME,
                 weight,
                 rs.GetString("dis_ent_id"),
                 rs.GetString("trans_id"),
                 rs.GetString("goods_id"),
                 rs.GetString("dis_office_id"),
                 rs.GetString("dis_cont_id"),
                 rs.GetString("car_kind_id"),
                 rs.GetString("car_no"),
                 rs.GetString("manifesto_no1"),
                 rs.GetString("manifesto_no2"),
                 rs.GetString("manifesto_no3"),
                 0,
                 rs.GetString("bill_id"),
                 0.0,
                 rs.GetString("car_id"),
                 rs.GetString("carry_class"),
                 rs.GetString("ryube"),
                 rs.GetString("unit_value"),
                 rs.GetString("unit_no"));

        return mSql.Update(sql);
    }

    //滞留データのマニフェストNO更新
    //rs:現在状態
    //ret:失敗時 SQL_ERR
    public int SetStayData2(RemoteStruct rs) {
        int ret = SQL_ERR;
        String sql = "";

        try {
            sql = String.format("update gate_in_data set "
                     + "car_no = %s,"
                     + "dis_ent_id = %s,"
                     + "trans_id = %s,"
                     + "goods_id = %s,"
                     + "dis_office_id = %s,"
                     + "dis_cont_id = %s,"
                     + "car_kind_id = %s,"
                     + "bill_id = %s,"
                     + "office_sub_name = '%s', "
                     + "manifesto_no1 = '%s', "
                     + "manifesto_no2 = '%s', "
                     + "manifesto_no3 = '%s', "
                     + "m3 ='%s', "
                     + "carry_class =%s, "
                     + "ryube =%s, "
                     + "unit_value =%s "
                     + "where car_id = %s and delete_flag <> true;",
                     rs.GetString("car_no"),
                     rs.GetString("dis_ent_id"),
                     rs.GetString("trans_id"),
                     rs.GetString("goods_id"),
                     rs.GetString("dis_office_id"),
                     rs.GetString("dis_cont_id"),
                     rs.GetString("car_kind_id"),
                     rs.GetString("bill_id"),
                     rs.GetString("office_sub_name"),
                     rs.GetString("manifesto_no1"),
                     rs.GetString("manifesto_no2"),
                     rs.GetString("manifesto_no3"),
                     0.0,
                     rs.GetString("carry_class"),
                     rs.GetString("ryube"),
                     rs.GetString("unit_value"),
                     rs.GetString("car_id"));

            if (mSql.Update(sql) != SQL_ERR) {
                ret = 1;
                mSql.Close();
            }
        } catch (Exception e) {

        }
        return ret;
    }

    //滞留データ作成
    //rs:現在状態
    //ret:失敗時 SQL_ERR
    public int SetStayData_edit(RemoteStruct rs, int weight) {
        String sql = "";

        //搬入時は総重量が１回目
        if (rs.GetInt("carry_class") == 0) {
            weight = rs.GetInt("total_weight");
        }

        // 入構データを作成
        sql = String.format(
                 "insert into %s%s"
                 + "(register_date, "
                 + " register_weight, "
                 + " dis_ent_id, "
                 + " trans_id, "
                 + " goods_id, "
                 + " dis_office_id, "
                 + " dis_cont_id, "
                 + " car_kind_id, "
                 + " car_no, manifesto_no1, manifesto_no2, manifesto_no3, class, "
                 + "bill_id, m3, car_id, carry_class, ryube, unit_value, unit_no, charge_value ) "
                 + "values(now(), %s, %s, %s, %s, %s, %s, %s, %s, '%s', '%s','%s', %s, %s, %s,'%s', %s, %s, %s, %s, %s );",
                 SCHEME_NAME, GATE_IN_TABLE_NAME,
                 weight,
                 //rs.GetString("total_weight"),
                 rs.GetString("dis_ent_id"),
                 rs.GetString("trans_id"),
                 rs.GetString("goods_id"),
                 rs.GetString("dis_office_id"),
                 rs.GetString("dis_cont_id"),
                 rs.GetString("car_kind_id"),
                 rs.GetString("car_no"),
                 rs.GetString("manifesto_no1"),
                 rs.GetString("manifesto_no2"),
                 rs.GetString("manifesto_no3"),
                 0,
                 rs.GetString("bill_id"),
                 rs.GetString("m3"),
                 rs.GetString("car_id"),
                 rs.GetString("carry_class"),
                 rs.GetString("ryube"),
                 rs.GetString("unit_value"),
                 rs.GetString("unit_no"),
                 rs.GetString("charge_value"));

        return mSql.Update(sql);
    }

    public int SetStayData_edit2(RemoteStruct rs, int weight) {
        String sql = "";

        // 入構データを修正
        sql = String.format(
                 "update %s%s set "
                 + " register_weight = %s "
                 + "where sno=%d and delete_flag<>true;",
                 SCHEME_NAME, GATE_IN_TABLE_NAME,
                 weight,
                 rs.GetLong("sno"));

        return mSql.Update(sql);
    }

    //計量データ取得
    //shipment_sno:滞留SNO
    //ss:計量データ格納先
    //ret:失敗時=SQL_ERR
    public int GetShipmentData(long shipment_sno, ShipmentStruct ss) {
        int ret = SQL_ERR;
        String sql = "";
        String key = "";

        ss.Clear();

        try {
            sql = String.format("select * from %s where sno=%d and delete_flag<>true;",
                     SHIPMENT_VTABLE_NAME,
                     shipment_sno);

            mSql.Select(sql);
            mSql.mResultSet2.next();

            for (int i = 1; i <= mSql.mMetaData2.getColumnCount(); i++) {
                key = mSql.mMetaData2.getColumnName(i);
                ss.mValue.put(key, mSql.mResultSet2.getObject(key));
            }
            ret = SQL_OK;

        } catch (Exception e) {
            System.out.println("SQLCommon-GetShipmentData-exp");
            ret = SQL_ERR;
        }

        return ret;
    }

    //計量印刷要求
    //shipment_sno:計量SNO
    //ss:計量データ格納先
    //ret:失敗時=SQL_ERR
    public int SetPrintOrder(long shipment_sno, boolean printer_print) {
        int ret = SQL_ERR;
        String sql = "";

        try {
            sql = String.format("update %s set "
                     + "printer_print_order = %b "
                     + "where sno=%d and delete_flag<>true;",
                     SHIPMENT_TABLE_NAME,
                     printer_print,
                     shipment_sno);
            if ((ret = mSql.Update(sql)) != SQL_ERR) {
                ret = SQL_OK;
            }
            mSql.Close();
        } catch (Exception e) {
            System.out.println("SQLCommon-GetShipmentData-exp");
            ret = SQL_ERR;
        }

        return ret;
    }

    //計量票印刷要求の確認
    //ret:失敗時=SQL_ERR
    public int GetShipmentPrintOrder() {
        int ret = SQL_ERR;
        String sql = "";

        try {
            sql = "select sno from "
                     + "v1_weigh_data "
                     + "where printer_print_order =true "
                     + "limit 1;";

            mSql.Select(sql);
            if (mSql.mResultSet2.next()) {

                //ポスト印刷フラグ:trueの計量データのSNOを返す
                ret = mSql.mResultSet2.getInt("sno");
            }

        } catch (Exception e) {
            System.out.println("SQLCommon-GetShipmentPrintOrder-exp");
            ret = SQL_ERR;
        }

        return ret;
    }

    //計量データ作成
    //rs:現在状態
    //ret:0以上成功 0未満異常
    public long SetShipmentData(RemoteStruct rs, String data) {
        long ret = -1;
        String sql = "", func = "";

        try {
            if (rs.GetString("print_flag").equals("1")) {
                func = "01";
            } else {
                func = "02";
            }

            String uriageKubun = String.valueOf(cmb売上区分.getSelectedItem());
            if (uriageKubun == null) {
                uriageKubun = "掛け";
            }

            sql = String.format(
                     "select %sadd_weigh_data%s(%s,%s,'%s');",
                     SCHEME_NAME,
                     func,
                     rs.GetString("car_id"),
                     Str2Int.get(data),
                     uriageKubun
            );

            if (mSql.Query(sql) != SQL_ERR) {
                if (mSql.mResultSet.next()) {
                    ret = Long.parseLong(mSql.mResultSet.getString(1));
                    mSql.Close();
                }
            }
        } catch (Exception e) {
            System.out.println("SQLCommon-SetShipmentData-exp");
        }

        return ret;
    }

    //ジャーナルデータ書き込み
    //data:書き込みデータ
    //ss:リモートデータ
    //remote_flag:リモートフラグ
    //ret:失敗時=SQL_ERR
    public int SetJournal(String data, RemoteStruct ss, boolean remote_flag) {
        String sql = "";

        sql = String.format("insert into %s%s(register_date,operation,tanker_id,driver_name,remote_flag) values (now(),'%s','%s','%s',%s);",
                 SCHEME_NAME,
                 JOURNAL_TABLE_NAME,
                 data,
                 "読取時重量：" + ss.GetString("now_weight")
                 + "　車番：" + ss.GetString("car_no")
                 + "　請求先名：" + ss.GetString("bill_name")
                 + "　排出事業者名：" + ss.GetString("entrepreneur_name")
                 + "　運搬業者名：" + ss.GetString("transporter_name")
                 + "　排出事業場名：" + ss.GetString("office_name")
                 + "　ごみ種名：" + ss.GetString("goods_name")
                 + "　処分業者名：" + ss.GetString("contractor_name")
                 + "　車種：" + ss.GetString("car_kind_name")
                 + "　総重量：" + ss.GetString("total_weight")
                 + "　空車重量：" + ss.GetString("empty_weight")
                 + "　正味重量：" + ss.GetString("net_weight"),
                 "排出現場２：" + ss.GetString("office_sub_name"),
                 (remote_flag ? "true" : "false"));

        return mSql.Update(sql);
    }

    //現在DB日時を取得
    public Object Get計量日時(Long sno) {
        Object ret = null;

        try {
            if (mSql.Query(String.format("select gate_out_date from v1_weigh_data where sno = %s;", sno)) != SQL_ERR) {//現在日付取得
                if (mSql.mResultSet.next()) {
                    ret = mSql.mResultSet.getObject("gate_out_date");
                }
            }
        } catch (Exception e) {
            System.out.println("SQLCommon-Get計量日時-exp");
            ret = null;
        }
        return ret;
    }

}
