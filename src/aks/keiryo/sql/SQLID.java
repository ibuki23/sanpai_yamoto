package aks.keiryo.sql;

import aks.keiryo.gui.MainPanel;
import java.util.*;
import aks.keiryo.status.*;

public class SQLID implements SQLDefine {

    public SQL mSql;

    //コンストラクタ
    public SQLID(String url, String user, String pwd) {
        mSql = new SQL(url, user, pwd);
    }

    //車両データ取得
    //key:車両ID
    //list:取得データ
    //ret:成功=SQL_OK 失敗=SQL_ERR
    public int GetCarData(String key, CarNoStruct list) {
        int ret = SQL_ERR;
        String colname;

        try {
            while (true) {
                if (mSql.Query(String.format("select * from %s%s where car_id = %s and delete_flag<>true ;",
                         SCHEME_NAME,
                         CAR_TABLE_NAME,
                         key)) != SQL_ERR) {
                    if (mSql.mResultSet.next()) {
                        colname = "car_no";
                        list.mValue.put(colname, mSql.mResultSet.getObject(colname));
                        if (!MainPanel.契約取込flag) {
                            colname = "dis_ent_id";
                            list.mValue.put(colname, mSql.mResultSet.getObject(colname));
                            colname = "entrepreneur_name";//
                            list.mValue.put(colname, mSql.mResultSet.getObject(colname));
                            colname = "trans_id";//
                            list.mValue.put(colname, mSql.mResultSet.getObject(colname));
                            colname = "transporter_name";//
                            list.mValue.put(colname, mSql.mResultSet.getObject(colname));
                            colname = "goods_id";//
                            list.mValue.put(colname, mSql.mResultSet.getObject(colname));
                            colname = "goods_name";//
                            list.mValue.put(colname, mSql.mResultSet.getObject(colname));
                            colname = "dis_office_id";
                            list.mValue.put(colname, mSql.mResultSet.getObject(colname));
                            colname = "office_name";//
                            list.mValue.put(colname, mSql.mResultSet.getObject(colname));
                            colname = "dis_cont_id";//
                            list.mValue.put(colname, mSql.mResultSet.getObject(colname));
                            colname = "contractor_name";//
                            list.mValue.put(colname, mSql.mResultSet.getObject(colname));
                        }
                        colname = "car_kind_id";//
                        list.mValue.put(colname, mSql.mResultSet.getObject(colname));
                        colname = "car_kind_name";//
                        list.mValue.put(colname, mSql.mResultSet.getObject(colname));
                        colname = "empty_weight";//
                        list.mValue.put(colname, mSql.mResultSet.getObject(colname));

                        //請求先追加
                        colname = "bill_id";
                        list.mValue.put(colname, mSql.mResultSet.getObject(colname));
                        colname = "bill_name";
                        list.mValue.put(colname, mSql.mResultSet.getObject(colname));

                        colname = "max_weight";
                        list.mValue.put(colname, mSql.mResultSet.getObject(colname));
                        colname = "office_sub_name";

                        list.mValue.put(colname, mSql.mResultSet.getObject(colname));
                        //リューベ初期化
                        colname = "m3";
                        list.mValue.put(colname, mSql.mResultSet.getObject(colname));
                        //比重係数
                        colname = "ryube";
                        list.mValue.put(colname, mSql.mResultSet.getObject(colname));
                        //料金区分
                        colname = "unit_no";
                        list.mValue.put(colname, mSql.mResultSet.getObject(colname));

                        //20140423 ID検索時に料金が設定されないため
                        //料金単価
                        colname = "unit_value";
                        if (list.mValue.get(colname) == null || (Integer) list.mValue.get(colname) == 0) {
                            list.mValue.put(colname, mSql.mResultSet.getObject(colname));
                        }

                        //0:搬入 1:搬出
                        colname = "carry_class";
                        list.mValue.put(colname, mSql.mResultSet.getObject(colname));
                        //『搬入』 『搬出』
                        colname = "carry_name";
                        list.mValue.put(colname, mSql.mResultSet.getObject(colname));
                    }
                }

                ret = SQL_OK;

                break;
            }
        } catch (Exception e) {
            System.out.println("SQLID-GetCarData-exp");
            ret = SQL_ERR;
        }
        return ret;
    }

//##################################################################################
//排出事業者データ取得
    //key:ID
    //list:取得データ
    //ret:成功=SQL_OK 失敗=SQL_ERR
    public int GetEntrepreneurList(String key, EntrepreneurStruct list) {
        int ret = SQL_ERR;

        try {
            while (true) {
                list.Clear();
                if (mSql.Query(String.format("select * from %s%s where id=%s and delete_flag<>true;",
                         SCHEME_NAME,
                         ENT_TABLE_NAME,
                         key)) != SQL_ERR) {
                    if (mSql.mResultSet.next()) {
                        list.mValue.put("dis_ent_id", mSql.mResultSet.getObject("id"));
                        list.mValue.put("entrepreneur_name", mSql.mResultSet.getObject("name"));

                        ret = SQL_OK;
                    }
                    break;
                } else {
                    break;
                }
            }
        } catch (Exception e) {
            System.out.println("SQLID-GetEntData-exp");
            ret = SQL_ERR;
        }
        return ret;
    }

    //請求先取得
    //idlist:IDリスト格納
    //namelist:名前リスト格納
    //ret:成功=SQL_OK 失敗=SQL_ERR
    public String GetBill(String sno) {

        try {
            if (mSql.Query(String.format("select * from %s%s where id = %s and delete_flag<>true ;",
                     SCHEME_NAME,
                     BILL_TABLE_NAME,
                     sno)) != SQL_ERR) {
                while (mSql.mResultSet.next()) {
                    return mSql.mResultSet.getString("name");
                }
            }
        } catch (Exception e) {
            System.out.println("SQLID-GetBill-exp");
            return "";
        }
        return "";
    }

    //排出事業者取得
    //idlist:IDリスト格納
    //namelist:名前リスト格納
    //ret:成功=SQL_OK 失敗=SQL_ERR
    public String GetEntrepreneur(String sno) {

        try {
            if (mSql.Query(String.format("select * from %s%s where id = %s and delete_flag<>true ;",
                     SCHEME_NAME,
                     ENT_TABLE_NAME,
                     sno)) != SQL_ERR) {
                while (mSql.mResultSet.next()) {
                    return mSql.mResultSet.getString("name");
                }
            }
        } catch (Exception e) {
            System.out.println("SQLID-GetEntrepreneur-exp");
            return "";
        }
        return "";
    }

//運搬業者データ取得
    //key:ID
    //list:取得データ
    //ret:成功=SQL_OK 失敗=SQL_ERR
    public int GetTransporterList(String key, TransporterStruct list) {
        int ret = SQL_ERR;

        try {
            while (true) {
                list.Clear();
                if (mSql.Query(String.format("select * from %s%s where id=%s and delete_flag<>true;",
                         SCHEME_NAME,
                         TRANS_TABLE_NAME,
                         key)) != SQL_ERR) {
                    if (mSql.mResultSet.next()) {
                        list.mValue.put("trans_id", mSql.mResultSet.getObject("id"));
                        list.mValue.put("transporter_name", mSql.mResultSet.getObject("name"));

                        ret = SQL_OK;
                    }
                    break;
                } else {
                    break;
                }
            }
        } catch (Exception e) {
            System.out.println("SQLID-GetTransporterData-exp");
            ret = SQL_ERR;
        }

        return ret;
    }

//運搬業者取得
    //idlist:IDリスト格納
    //namelist:名前リスト格納
    //ret:成功=SQL_OK 失敗=SQL_ERR
    public String GetTransporter(String sno) {
        try {
            if (mSql.Query(String.format("select * from %s%s where id = %s and delete_flag<>true ;",
                     SCHEME_NAME,
                     TRANS_TABLE_NAME,
                     sno)) != SQL_ERR) {
                while (mSql.mResultSet.next()) {
                    return mSql.mResultSet.getString("name");
                }
            }
        } catch (Exception e) {
            System.out.println("SQLID-GetTransporter-exp");
            return "";
        }
        return "";
    }

    //種類データ取得
    //key:ID
    //list:取得データ
    //ret:成功=SQL_OK 失敗=SQL_ERR
    public int GetKindList(String key, KindStruct list, String old_id) {
        int ret = SQL_ERR;

        try {
            while (true) {
                list.Clear();
                if (mSql.Query(String.format("select * from %s%s where id=%s and delete_flag<>true;",
                         SCHEME_NAME,
                         KIND_TABLE_NAME,
                         key)) != SQL_ERR) {
                    if (mSql.mResultSet.next()) {
                        list.mValue.put("goods_id", mSql.mResultSet.getObject("id"));
                        list.mValue.put("goods_name", mSql.mResultSet.getObject("name"));

                        // 20121016 add
                        list.mValue.put("carry_class", mSql.mResultSet.getObject("carry_class"));
                        list.mValue.put("carry_name", mSql.mResultSet.getObject("carry_name"));
                        // 20121225
                        list.mValue.put("ryube", mSql.mResultSet.getObject("ryube"));
                        // 20130422
                        list.mValue.put("unit_no", mSql.mResultSet.getObject("unit_no"));
                        if (old_id.length() > 0) {
                            int old_ = Integer.valueOf(old_id);
                            int new_ = Integer.valueOf(key);
                            if (old_ != new_) {
                                list.mValue.put("unit_value", mSql.mResultSet.getObject("unit_value"));
                            }
                        } else {
                            list.mValue.put("unit_value", mSql.mResultSet.getObject("unit_value"));
                        }

                        ret = SQL_OK;
                    }
                    break;
                } else {
                    break;
                }
            }
        } catch (Exception e) {
            System.out.println("SQLID-GetKindData-exp");
            ret = SQL_ERR;
        }
        return ret;
    }

//種類取得
    //idlist:IDリスト格納
    //namelist:名前リスト格納
    //ret:成功=SQL_OK 失敗=SQL_ERR
    public String GetKind(String sno) {

        try {
            if (mSql.Query(String.format("select * from %s%s where id = %s and delete_flag<>true ;",
                     SCHEME_NAME,
                     KIND_TABLE_NAME,
                     sno)) != SQL_ERR) {
                while (mSql.mResultSet.next()) {
                    return mSql.mResultSet.getString("name");

                }
            }
        } catch (Exception e) {
            System.out.println("SQLID-GetKind-exp");
            return "";
        }
        return "";
    }

    //排出事業場データ取得
    //key:ID
    //list:取得データ
    //ret:成功=SQL_OK 失敗=SQL_ERR
    public int GetOfficeList(String key, String ent_id, OfficeStruct list) {
        int ret = SQL_ERR;

        try {
            while (true) {
                list.Clear();
                if (mSql.Query(String.format("select * from %s%s where id=%s and ent_id = %s and delete_flag<>true;",
                         SCHEME_NAME,
                         OFFICE_TABLE_NAME,
                         key,
                         ent_id)) != SQL_ERR) {
                    if (mSql.mResultSet.next()) {
                        list.mValue.put("dis_office_id", mSql.mResultSet.getObject("id"));
                        list.mValue.put("office_name", mSql.mResultSet.getObject("name"));
                        list.mValue.put("auto_update_flag", mSql.mResultSet.getObject("auto_update_flag"));
                        list.mValue.put("contract_start_date", mSql.mResultSet.getObject("contract_start_date"));
                        list.mValue.put("contract_end_date", mSql.mResultSet.getObject("contract_end_date"));
                        list.mValue.put("remark", mSql.mResultSet.getObject("remark"));
                        //20260317 edit
//                        list.mValue.put("bill_id", mSql.mResultSet.getObject("bill_id"));
//                        list.mValue.put("bill_name", mSql.mResultSet.getObject("bill_name"));

                        ret = SQL_OK;
                    }
                    break;
                } else {
                    break;
                }
            }
        } catch (Exception e) {
            System.out.println("SQLID-GetOfficeData-exp");
            ret = SQL_ERR;
        }
        return ret;
    }

    //排出事業場取得
    //idlist:IDリスト格納
    //namelist:名前リスト格納
    //ret:成功=SQL_OK 失敗=SQL_ERR
    public String GetOffice(String id, String ent_id) {

        try {
            if (mSql.Query(String.format("select * from %s%s where id = %s and ent_id = %s and delete_flag <> true and finish_flag <> true ;",
                     SCHEME_NAME,
                     OFFICE_TABLE_NAME,
                     id, ent_id)) != SQL_ERR) {
                while (mSql.mResultSet.next()) {
                    return mSql.mResultSet.getString("name");
                }
            }
        } catch (Exception e) {
            System.out.println("SQLID-GetOffice-exp");
            return "";
        }
        return "";
    }

//    //処分業者データ取得
//    //key:ID
//    //list:取得データ
//    //ret:成功=SQL_OK 失敗=SQL_ERR
    public int GetContList(String key, ContStruct list) {
        int ret = SQL_ERR;

        try {
            while (true) {
                list.Clear();
                if (mSql.Query(String.format("select * from %s%s where id=%s and delete_flag<>true;",
                         SCHEME_NAME,
                         CONT_TABLE_NAME,
                         key)) != SQL_ERR) {
                    if (mSql.mResultSet.next()) {
                        list.mValue.put("dis_cont_id", mSql.mResultSet.getObject("id"));
                        list.mValue.put("contractor_name", mSql.mResultSet.getObject("name"));

                        ret = SQL_OK;
                    }
                    break;
                } else {
                    break;
                }
            }
        } catch (Exception e) {
            System.out.println("SQLID-GetContData-exp");
            ret = SQL_ERR;
        }
        return ret;
    }

//    //処分業者
//    //idlist:IDリスト格納
//    //namelist:名前リスト格納
//    //ret:成功=SQL_OK 失敗=SQL_ERR
    public String GetCont(String sno) {

        try {
            if (mSql.Query(String.format("select * from %s%s where id = %s and delete_flag<>true ;",
                     SCHEME_NAME,
                     CONT_TABLE_NAME,
                     sno)) != SQL_ERR) {
                while (mSql.mResultSet.next()) {
                    return mSql.mResultSet.getString("name");
                }
            }
        } catch (Exception e) {
            System.out.println("SQLID-GetCont-exp");
            return "";
        }
        return "";
    }

//    //車種データ取得
//    //key:ID
//    //list:取得データ
//    //ret:成功=SQL_OK 失敗=SQL_ERR
    public int GetCar_KindList(String key, Car_KindStruct list) {
        int ret = SQL_ERR;

        try {
            while (true) {
                list.Clear();
                if (mSql.Query(String.format("select * from %s%s where id=%s and delete_flag<>true;",
                         SCHEME_NAME,
                         CAR_KIND_TABLE_NAME,
                         key)) != SQL_ERR) {
                    if (mSql.mResultSet.next()) {
                        list.mValue.put("car_kind_id", mSql.mResultSet.getObject("id"));
                        list.mValue.put("car_kind_name", mSql.mResultSet.getObject("name"));

                        ret = SQL_OK;
                    }
                    break;
                } else {
                    break;
                }
            }
        } catch (Exception e) {
            System.out.println("SQLID-GetCar_KindData-exp");
            ret = SQL_ERR;
        }
        return ret;
    }

    //車種取得
    //idlist:IDリスト格納
    //namelist:名前リスト格納
    //ret:成功=SQL_OK 失敗=SQL_ERR
    public String GetCar_Kind(String sno) {

        try {
            if (mSql.Query(String.format("select * from %s%s where id = %s and delete_flag<>true ;",
                     SCHEME_NAME,
                     CAR_KIND_TABLE_NAME,
                     sno)) != SQL_ERR) {
                while (mSql.mResultSet.next()) {
                    return mSql.mResultSet.getString("name");
                }
            }
        } catch (Exception e) {
            System.out.println("SQLID-GetCar_Kind-exp");
            return "";
        }
        return "";
    }

    //車番取得
    //idlist:IDリスト格納
    //namelist:名前リスト格納
    //ret:成功=SQL_OK 失敗=SQL_ERR
    public Object[] GetCar(String car_no_) {
        //請求先追加のため
        Object[] list = new Object[23];

        try {
            while (true) {
                if (mSql.Query(String.format("select * from %s where car_id = %s and delete_flag<>true ;",
                         CAR_TABLE_NAME,
                         car_no_)) != SQL_ERR) {
                    if (mSql.mResultSet.next()) {
                        list[0] = mSql.mResultSet.getInt("car_no");
                        list[1] = mSql.mResultSet.getInt("dis_ent_id");
                        list[2] = mSql.mResultSet.getString("entrepreneur_name");
                        list[3] = mSql.mResultSet.getInt("trans_id");
                        list[4] = mSql.mResultSet.getString("transporter_name");
                        list[5] = mSql.mResultSet.getInt("goods_id");
                        list[6] = mSql.mResultSet.getString("goods_name");
                        list[7] = mSql.mResultSet.getInt("dis_office_id");
                        list[8] = mSql.mResultSet.getString("office_name");
                        list[9] = mSql.mResultSet.getInt("dis_cont_id");
                        list[10] = mSql.mResultSet.getString("contractor_name");
                        list[11] = mSql.mResultSet.getInt("car_kind_id");
                        list[12] = mSql.mResultSet.getString("car_kind_name");
                        list[13] = mSql.mResultSet.getInt("empty_weight");
                        list[14] = mSql.mResultSet.getInt("bill_id");
                        list[15] = mSql.mResultSet.getString("bill_name");
                        list[16] = mSql.mResultSet.getInt("carry_class");
                        list[17] = mSql.mResultSet.getString("carry_name");
                        list[18] = mSql.mResultSet.getDouble("m3");
                        list[19] = mSql.mResultSet.getDouble("ryube");
                        list[20] = mSql.mResultSet.getInt("unit_value");
                        list[21] = mSql.mResultSet.getInt("unit_no");
                        list[22] = mSql.mResultSet.getInt("car_id");

                        return list;
                    } else {
                    }
                    break;
                } else {
                    break;
                }
            }
        } catch (Exception e) {
            System.out.println("SQLID-GetCar-exp");
            return list;
        }
        return list;
    }

    //    //車種データ取得
//    //key:ID
//    //list:取得データ
//    //ret:成功=SQL_OK 失敗=SQL_ERR
    public int GetBillList(String key, BillStruct list) {
        int ret = SQL_ERR;

        try {
            while (true) {
                list.Clear();
                if (mSql.Query(String.format("select * from %s%s where id=%s and delete_flag<>true;",
                         SCHEME_NAME,
                         BILL_TABLE_NAME,
                         key)) != SQL_ERR) {
                    if (mSql.mResultSet.next()) {
                        list.mValue.put("bill_id", mSql.mResultSet.getObject("id"));
                        list.mValue.put("bill_name", mSql.mResultSet.getObject("name"));

                        ret = SQL_OK;
                    }
                    break;
                } else {
                    break;
                }
            }
        } catch (Exception e) {
            System.out.println("SQLID-GetBillData-exp");
            ret = SQL_ERR;
        }
        return ret;
    }

    //##################################################################################
    //運搬業者取得
    //idlist:IDリスト格納
    //namelist:名前リスト格納
    //ret:成功=SQL_OK 失敗=SQL_ERR
    public int GetTransList(List idlist, List namelist) {
        int ret = SQL_ERR;

        idlist.clear();
        namelist.clear();

        try {
            if (mSql.Query(String.format("select * from %s%s where delete_flag<>true order by sno;",
                     SCHEME_NAME,
                     TRANS_TABLE_NAME)) != SQL_ERR) {
                while (mSql.mResultSet.next()) {
                    idlist.add(mSql.mResultSet.getString("id"));
                    namelist.add(mSql.mResultSet.getString("name"));
                }
            }
        } catch (Exception e) {
            System.out.println("SQLID-GetGetTransporterList-exp");
            ret = SQL_ERR;
        }
        return ret;
    }

    //車番取得
    //idlist:IDリスト格納
    //namelist:名前リスト格納
    //ret:成功=SQL_OK 失敗=SQL_ERR
    public int GetCarList(List idlist, List namelist) {
        int ret = SQL_ERR;

        idlist.clear();
        namelist.clear();

        try {
            if (mSql.Query(String.format("select * from %s%s where delete_flag<>true order by sno;",
                     SCHEME_NAME,
                     //                    CAR_VIEW_NAME))!=SQL_ERR){
                     CAR_TABLE_NAME)) != SQL_ERR) {
                while (mSql.mResultSet.next()) {
                    idlist.add(mSql.mResultSet.getString("car_no"));
                    namelist.add(mSql.mResultSet.getString("transporter_name"));
                }
            }
        } catch (Exception e) {
            System.out.println("SQLID-GetCarList-exp");
            ret = SQL_ERR;
        }
        return ret;
    }

    //排出事業場取得
    //idlist:IDリスト格納
    //namelist:名前リスト格納
    //ret:成功=SQL_OK 失敗=SQL_ERR
    public int GetOfficeList(List idlist, List namelist) {
        int ret = SQL_ERR;

        idlist.clear();
        namelist.clear();

        try {
            if (mSql.Query(String.format("select * from %s%s where delete_flag<>true order by sno;",
                     SCHEME_NAME,
                     OFFICE_TABLE_NAME)) != SQL_ERR) {
                while (mSql.mResultSet.next()) {
                    idlist.add(mSql.mResultSet.getString("id"));
                    namelist.add(mSql.mResultSet.getString("name"));
                }
            }
        } catch (Exception e) {
            System.out.println("SQLID-GetOfficeList-exp");
            ret = SQL_ERR;
        }
        return ret;
    }

    //種類一覧取得
    //idlist:IDリスト格納
    //namelist:名前リスト格納
    //ret:成功=SQL_OK 失敗=SQL_ERR
    public int GetGoods_KindList(List idlist, List namelist) {
        int ret = SQL_ERR;

        idlist.clear();
        namelist.clear();

        try {
            if (mSql.Query(String.format("select * from %s%s where delete_flag<>true order by sno;",
                     SCHEME_NAME,
                     KIND_TABLE_NAME)) != SQL_ERR) {
                while (mSql.mResultSet.next()) {
                    idlist.add(mSql.mResultSet.getString("id"));
                    namelist.add(mSql.mResultSet.getString("name"));
                }
            }
        } catch (Exception e) {
            System.out.println("SQLID-GetKindList-exp");
            ret = SQL_ERR;
        }
        return ret;
    }

    //車種一覧取得
    //idlist:IDリスト格納
    //namelist:名前リスト格納
    //ret:成功=SQL_OK 失敗=SQL_ERR
    public int GetCar_KindList(List idlist, List namelist) {
        int ret = SQL_ERR;

        idlist.clear();
        namelist.clear();

        try {
            if (mSql.Query(String.format("select * from %s%s where delete_flag<>true order by sno;",
                     SCHEME_NAME,
                     CAR_KIND_TABLE_NAME)) != SQL_ERR) {
                while (mSql.mResultSet.next()) {
                    idlist.add(mSql.mResultSet.getString("id"));
                    namelist.add(mSql.mResultSet.getString("name"));
                }
            }
        } catch (Exception e) {
            System.out.println("SQLID-GetCar_KindList-exp");
            ret = SQL_ERR;
        }
        return ret;
    }

    //排出事業者取得
    //idlist:IDリスト格納
    //namelist:名前リスト格納
    //ret:成功=SQL_OK 失敗=SQL_ERR
    public int GetEntList(List idlist, List namelist) {
        int ret = SQL_ERR;

        idlist.clear();
        namelist.clear();

        try {
            if (mSql.Query(String.format("select * from %s%s where delete_flag<>true order by sno;",
                     SCHEME_NAME,
                     ENT_TABLE_NAME)) != SQL_ERR) {
                while (mSql.mResultSet.next()) {
                    idlist.add(mSql.mResultSet.getString("id"));
                    namelist.add(mSql.mResultSet.getString("name"));
                }
            }
        } catch (Exception e) {
            System.out.println("SQLID-GetEntrepreneur-exp");
            ret = SQL_ERR;
        }
        return ret;
    }

    //    //処分業者取得
//    //key:ID
//    //list:取得データ
//    //ret:成功=SQL_OK 失敗=SQL_ERR
    public int GetContList(List idlist, List namelist) {
        int ret = SQL_ERR;

        idlist.clear();
        namelist.clear();

        try {
            while (true) {
                if (mSql.Query(String.format("select * from %s%s where delete_flag<>true order by sno;",
                         SCHEME_NAME,
                         CONT_TABLE_NAME)) != SQL_ERR) {
                    while (mSql.mResultSet.next()) {
                        idlist.add(mSql.mResultSet.getString("id"));
                        namelist.add(mSql.mResultSet.getString("name"));
                        ret = SQL_OK;
                    }
                    break;
                } else {
                    break;
                }
            }
        } catch (Exception e) {
            System.out.println("SQLID-GetContData-exp");
            ret = SQL_ERR;
        }
        return ret;
    }
}
