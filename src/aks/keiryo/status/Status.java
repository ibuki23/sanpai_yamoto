package aks.keiryo.status;

import aks.industrialwaste.database.DBManager;
import java.sql.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.*;
import aks.keiryo.gui.*;
import aks.keiryo.sql.*;
import aks.keiryo.util.*;
import aks.keiryo.message.*;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;

public class Status implements StatusDefine {

    public boolean mRemote;//リモート状態=true 自身制御状態=false
    public int mMyRemoteType;//自身制御番号 0=計量室 1=管理室
    public DataStruct[] msData;//計量,ドライバー,車両,トレーラー情報状態
    public RemoteStruct msRemote;
    public ProgressStruct msProgress;
    public StayStruct msStay;
    public ShipmentStruct msShipment;
    public OfficeStruct msEnv;
    public String mSQL_Drv;
    public String mSQL_Url;
    public String mSQL_User;
    public String mSQL_Pwd;
    public SQLID mSQLID;
    public SQLCommon mSQLCommon;
    public SoundMessage mSound;
    public MainPanel mMainPanel;

    public long mLastConfirmTime;//直前の確認押下時間

    public static boolean WeightReady_ = false;

    public boolean f重量オーバー = false;

    private SimpleDateFormat sdf;

    //コンストラクタ
    public Status() {
        msData = new DataStruct[21];
        for (int i = 0; i < msData.length; i++) {
            msData[i] = new DataStruct();
        }

        msRemote = new RemoteStruct();
        msProgress = new ProgressStruct();
        msStay = new StayStruct();
        msShipment = new ShipmentStruct();
        msEnv = new OfficeStruct();

        mSound = new SoundMessage();
    }

    //セットアップ
    public void Setup(MainPanel owner) {

        //設定ファイルの読み込み
        loadProperties(new File("db.properties"));

        mMainPanel = owner;

        mSQLID = new SQLID(mSQL_Url, mSQL_User, mSQL_Pwd);
        mSQLCommon = new SQLCommon(mSQL_Url, mSQL_User, mSQL_Pwd);

        mRemote = false;
        //設定初期化
        SetCancel();

        //環境変数読み込み
        sdf = new SimpleDateFormat("dd日　HH時mm分");
    }

    //DBの設定値を取得
    public void loadProperties(File propertyFile) {
        try {
            InputStream in = new FileInputStream(propertyFile);
            Properties prop = new Properties();
            prop.load(in);

            mSQL_Url = DBManager.url_;
            mSQL_User = prop.getProperty("user", "").trim();
            mSQL_Pwd = prop.getProperty("passwd", "").trim();
        } catch (Exception e) {
            System.out.println("Status-loadProperties-exp");
        }
    }

    //データのセット２
    //index:セットするデータ種別 INDEX_EX_*
    //data:データ
    public void SetDataEx(int index, String data) {
        switch (index) {
            case INDEX_EX_WEIGH_INTERVAL://最小計量可能間隔の更新
                msEnv.mValue.put("weigh_interval", data);
                break;
            case INDEX_EX_MIN_WEIGHT://最小計量可能重量の更新
                msEnv.mValue.put("min_weight", data);
                break;
        }
    }

    private Integer toIntOrNull(Object v) {
        if (v == null) {
            return null;
        }
        String s = v.toString().trim();
        if (s.isEmpty()) {
            return null;
        }
        try {
            return Integer.valueOf(s);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    //データのセット
    //index:セットするデータ種別 INDEX_*
    //data:データ
    public void SetData(int index, String data) {

        switch (index) {
            case INDEX_WEIGHT://計量

                msRemote.mValue.put("now_weight", data);

                msData[INDEX_WEIGHT].mData = data;
                //画面の計量更新
                try {
                    MainPanel.count = Integer.valueOf(GetReportNoTEXT(MainPanel.con_, MainPanel.term_date_).replaceAll("A", "").replaceAll("B", ""));
                } catch (Exception e) {
                }

                //重量確認OKの場合、表示させる
                if (WeightReady_) {
                    msProgress.mFinalWeight = Str2Int.get(data);//確定重量の取り込み

                } else {
                    MainPanel.now_weight_1 = Integer.valueOf((String) msRemote.mValue.get("now_weight"));
                    MainPanel.total_weight_ = 0;
                    MainPanel.empty_weight_ = 0;
                    MainPanel.net_weight_ = 0;
                }

                break;

            case INDEX_NET_WEIGHT:
                //トラックスケール重量取込ボタン
                if (MainPanel.選択計量回数_ == 1) {
                    //1回計量
                    //現在重量が重いはず
                    MainPanel.total_weight_ = MainPanel.now_weight_1;
                    //マスタ風袋重量が軽いはず
                    MainPanel.empty_weight_ = MainPanel.loading_weight_;
                    MainPanel.net_weight_ = MainPanel.total_weight_ - MainPanel.empty_weight_;
                } else if (MainPanel.選択計量回数_ == 2) {
                    //2回計量1回目
                    //常に記憶重量に上書き
                    MainPanel.loading_weight_ = MainPanel.now_weight_1;
                } else if (MainPanel.選択計量回数_ == 3) {
                    //2回計量2回目 carry_class_==0 :搬入　carry_class_==1 :搬出
                    //搬入なら現在重量が軽いはず
                    MainPanel.total_weight_ = MainPanel.carry_class_ == 0 ? MainPanel.loading_weight_ : MainPanel.now_weight_1;
                    //マスタ風袋重量が軽いはず
                    MainPanel.empty_weight_ = MainPanel.carry_class_ == 0 ? MainPanel.now_weight_1 : MainPanel.loading_weight_;

                    MainPanel.net_weight_ = MainPanel.total_weight_ - MainPanel.empty_weight_;
                }
                //表示更新
                mMainPanel.UpdateView(msRemote);
                break;

            case INDEX_CAR_NO:
                msRemote.mValue.put("car_no", data);
                //表示更新
                mMainPanel.UpdateView(msRemote);
                break;

            case INDEX_KANRI_ID:
                msRemote.mValue.put("car_id", data);

                break;
            case INDEX_CAR_ID:
                msRemote.mValue.put("car_id", data);
                CarNoStruct cns = new CarNoStruct();
                if (mSQLID.GetCarData(data, (CarNoStruct) cns) == SQLDefine.SQL_OK) {
                    msData[index].mExist = true;
                    msData[INDEX_TRANS].mExist = true;
                    msData[INDEX_ENT].mExist = true;
                    msData[INDEX_OFFICE].mExist = true;
                    msData[INDEX_CAR_ID].mExist = true;
                    msData[INDEX_CAR_KIND].mExist = true;
                    msData[INDEX_KIND].mExist = true;
                    //INDEX_BILL
                    msData[INDEX_BILL].mExist = true;
                    msData[index].mData = data;

                    //リモートデータに入れる
                    for (int i = 0; i < cns.mValue.size(); i++) {
                        msRemote.mValue.put(cns.GetKey(i), cns.GetString(cns.GetKey(i)));
                    }
                    MainPanel.carry_class_ = Integer.valueOf((String) msRemote.mValue.get("carry_class"));

                    if (Integer.valueOf((String) msRemote.mValue.get("goods_id")) <= 40
                             && Integer.valueOf((String) msRemote.mValue.get("goods_id")) > 0) {
                        getOfficeUnit("v0_goods_kind", Integer.valueOf((String) msRemote.mValue.get("goods_id")));
                    }

                    OfficeStruct os = new OfficeStruct();
                    if (mSQLID.GetOfficeList((String) msRemote.mValue.get("dis_office_id"),
                             (String) msRemote.mValue.get("dis_ent_id"), (OfficeStruct) os) == SQLDefine.SQL_OK) {
                        msData[index].mAccept = false;
                        msData[index].mExist = true;
                        msData[index].mData = data;

                        //リモートデータに入れる
                        for (int i = 0; i < os.mValue.size(); i++) {
                            msRemote.mValue.put(os.GetKey(i), os.GetString(os.GetKey(i)));
                        }
                    }
                    //契約内容表示
                    msRemote.mValue.put("契約内容表示",
                             (Object) msRemote.mValue.get("auto_update_flag") != null
                             && ((String) msRemote.mValue.get("auto_update_flag")).equals("true")
                             ? "『自動更新』" + "　『備考』" + (String) msRemote.mValue.get("remark")
                             : msRemote.mValue.get("remark") != null
                             ? "『備考』" + (String) msRemote.mValue.get("remark")
                             : ""
                    );

                    if (Integer.valueOf((String) msRemote.mValue.get("empty_weight")) > 0) {
                        MainPanel.選択計量回数_ = 1;
                    } else {
                        MainPanel.選択計量回数_ = 2;
                    }
                    //最大積載量 + 風袋重量より現在重量が重ければ
                    if ((((String) msRemote.mValue.get("max_weight").toString()).length() > 0) && (((String) msRemote.mValue.get("empty_weight").toString()).length() > 0)) {
                        if (Integer.valueOf((String) msRemote.mValue.get("max_weight")) != 0 && MainPanel.now_weight_1 > Integer.valueOf((String) msRemote.mValue.get("max_weight")) + Integer.valueOf((String) msRemote.mValue.get("empty_weight"))) {
                            msRemote.mValue.put("message", "重量オーバーの可能性があります。確認してください。(登録最大積載重量：" + (String) msRemote.mValue.get("max_weight") + "kg)");
                            mSound.Play("/true.wav");
                            f重量オーバー = true;
                        }
                    }
                    msRemote.mValue.put("sub_message", "");

                    //滞留確認
                    //無ければ出構重量をゼロに
                    if (mSQLCommon.GetStayDataRaw3(msRemote, msStay, data) < 0) {

                        MainPanel.total_weight_ = 0;
                        MainPanel.empty_weight_ = 0;
                        MainPanel.net_weight_ = 0;
                        MainPanel.loading_weight_ = Integer.valueOf((String) msRemote.mValue.get("empty_weight"));

                    } else {
                        for (int i = 0; i < msStay.mValue.size(); i++) {
                            msRemote.mValue.put(msStay.GetKey(i), msStay.GetString(msStay.GetKey(i)));
                        }
                        MainPanel.選択計量回数_ = 3;
                        //滞留データがあれば
                        //2回目の計量
                        msRemote.mValue.put("loading_weight", msStay.mValue.get("register_weight"));
                        MainPanel.loading_weight_ = (Integer) msRemote.mValue.get("loading_weight");
                    }

                    WeightReady_ = true;
                    MainPanel.OpaqueFlag_ = true;

                    //　ジャーナル記録
                    mSQLCommon.SetJournal("車番選択：" + (String) msRemote.mValue.get("car_no"),
                             msRemote, (mMyRemoteType != REMOTE_KEIRYO));

                    //表示更新
                    mMainPanel.UpdateView(msRemote);

                }

                break;

            case INDEX_TRANS://運搬業者
                TransporterStruct ts = new TransporterStruct();
                if (mSQLID.GetTransporterList(data, (TransporterStruct) ts) == SQLDefine.SQL_OK) {
                    msData[index].mAccept = false;
                    msData[index].mExist = true;
                    msData[index].mData = data;

                    //リモートデータに入れる
                    for (int i = 0; i < ts.mValue.size(); i++) {
                        msRemote.mValue.put(ts.GetKey(i), ts.GetString(ts.GetKey(i)));
                    }
                    //　ジャーナル記録
                    mSQLCommon.SetJournal("運搬業者選択：" + (String) msRemote.mValue.get("transporter_name"),
                             msRemote, (mMyRemoteType != REMOTE_KEIRYO));

                    //表示更新
                    mMainPanel.UpdateView(msRemote);

                } else {
                    msRemote.mValue.put("trans_id", data);
                    msRemote.mValue.put("transporter_name", "");
                }
                break;

            case INDEX_ENT://排出事業者
                EntrepreneurStruct es = new EntrepreneurStruct();
                if (mSQLID.GetEntrepreneurList(data, (EntrepreneurStruct) es) == SQLDefine.SQL_OK) {
                    msData[index].mAccept = false;
                    msData[index].mExist = true;
                    msData[index].mData = data;

                    //リモートデータに入れる
                    for (int i = 0; i < es.mValue.size(); i++) {
                        msRemote.mValue.put(es.GetKey(i), es.GetString(es.GetKey(i)));
                    }

                    OfficeStruct os = new OfficeStruct();
                    if (mSQLID.GetOfficeList((String) msRemote.mValue.get("dis_office_id"),
                             (String) msRemote.mValue.get("dis_ent_id"), (OfficeStruct) os) == SQLDefine.SQL_OK) {
                        msData[index].mAccept = false;
                        msData[index].mExist = true;
                        msData[index].mData = data;

                        //リモートデータに入れる
                        for (int i = 0; i < os.mValue.size(); i++) {
                            msRemote.mValue.put(os.GetKey(i), os.GetString(os.GetKey(i)));
                        }
                    }
                    //契約ごとの単価確認
                    Integer entId = toIntOrNull(msRemote.mValue.get("dis_ent_id"));
                    Integer officeId = toIntOrNull(msRemote.mValue.get("dis_office_id"));
                    Integer goodsId = toIntOrNull(msRemote.mValue.get("goods_id"));

                    if (entId != null && officeId != null && goodsId != null) {
                        getAgreementUnit(entId, officeId, goodsId);
                    } else {
//                        msRemote.mValue.put("sub_message", "契約単価取得に必要な情報が未入力です
                    }

                    //契約内容表示
                    msRemote.mValue.put("契約内容表示",
                             (Object) msRemote.mValue.get("auto_update_flag") != null
                             && ((String) msRemote.mValue.get("auto_update_flag")).equals("true")
                             ? "『自動更新』" + "　『備考』" + (String) msRemote.mValue.get("remark")
                             : msRemote.mValue.get("remark") != null
                             ? "『備考』" + (String) msRemote.mValue.get("remark")
                             : ""
                    );
                    mSQLCommon.SetJournal("排出事業者選択：" + (String) msRemote.mValue.get("entrepreneur_name"),
                             msRemote, (mMyRemoteType != REMOTE_KEIRYO));

                    //表示更新
                    mMainPanel.UpdateView(msRemote);

                } else {
                    msRemote.mValue.put("dis_ent_id", data);
                    msRemote.mValue.put("entrepreneur_name", "");
                }
                break;

            case INDEX_OFFICE://排出事業場
                OfficeStruct os = new OfficeStruct();
                if (mSQLID.GetOfficeList(data, (String) msRemote.mValue.get("dis_ent_id"), (OfficeStruct) os) == SQLDefine.SQL_OK) {
                    msData[index].mAccept = false;
                    msData[index].mExist = true;
                    msData[index].mData = data;

                    //リモートデータに入れる
                    for (int i = 0; i < os.mValue.size(); i++) {
                        msRemote.mValue.put(os.GetKey(i), os.GetString(os.GetKey(i)));
                    }
                    Integer entId = toIntOrNull(msRemote.mValue.get("dis_ent_id"));
                    Integer officeId = toIntOrNull(msRemote.mValue.get("dis_office_id"));
                    Integer goodsId = toIntOrNull(msRemote.mValue.get("goods_id"));

                    if (entId != null && officeId != null && goodsId != null) {
                        getAgreementUnit(entId, officeId, goodsId);
                    } else {
//                        msRemote.mValue.put("sub_message", "契約単価取得に必要な情報が未入力です");
                    }

                    //契約内容表示
                    msRemote.mValue.put("契約内容表示",
                             (Object) msRemote.mValue.get("auto_update_flag") != null
                             && ((String) msRemote.mValue.get("auto_update_flag")).equals("true")
                             ? "『自動更新』" + "　『備考』" + (String) msRemote.mValue.get("remark")
                             : msRemote.mValue.get("remark") != null
                             ? "『備考』" + (String) msRemote.mValue.get("remark")
                             : ""
                    );

                    mSQLCommon.SetJournal("排出事業場選択：" + (String) msRemote.mValue.get("office_name"),
                             msRemote, (mMyRemoteType != REMOTE_KEIRYO));

                    //表示更新
                    mMainPanel.UpdateView(msRemote);

                } else {
                    msRemote.mValue.put("dis_office_id", data);
                    msRemote.mValue.put("office_name", "");
                }
                break;

            case INDEX_KIND://種類
                KindStruct k = new KindStruct();
                if (mSQLID.GetKindList(data, (KindStruct) k, (String) msRemote.mValue.get("goods_id")) == SQLDefine.SQL_OK) {
                    msData[index].mAccept = false;
                    msData[index].mExist = true;
                    msData[index].mData = data;

                    //リモートデータに入れる
                    for (int i = 0; i < k.mValue.size(); i++) {
                        msRemote.mValue.put(k.GetKey(i), k.GetString(k.GetKey(i)));
                    }

                    Integer entId = toIntOrNull(msRemote.mValue.get("dis_ent_id"));
                    Integer officeId = toIntOrNull(msRemote.mValue.get("dis_office_id"));
                    Integer goodsId = toIntOrNull(msRemote.mValue.get("goods_id"));

                    if (entId != null && officeId != null && goodsId != null) {
                        getAgreementUnit(entId, officeId, goodsId);
                    } else {
//                        msRemote.mValue.put("sub_message", "契約単価取得に必要な情報が未入力です");
                    }

                    MainPanel.carry_class_ = Integer.valueOf((String) msRemote.mValue.get("carry_class"));//搬入出の値の入れ直し2018/01/11

                    //　ジャーナル記録
                    mSQLCommon.SetJournal("種類選択：" + (String) msRemote.mValue.get("goods_name"),
                             msRemote, (mMyRemoteType != REMOTE_KEIRYO));

                    //表示更新
                    mMainPanel.UpdateView(msRemote);

                } else {
                    msRemote.mValue.put("goods_id", data);
                    msRemote.mValue.put("goods_name", "");
                    //該当無ければゼロ
                    msRemote.mValue.put("unit", 0);
                    msRemote.mValue.put("unit_value", 0);
                }
                break;

            case INDEX_CONT://処分業者
                ContStruct cs = new ContStruct();
                if (mSQLID.GetContList(data, (ContStruct) cs) == SQLDefine.SQL_OK) {
                    msData[index].mAccept = false;
                    msData[index].mExist = true;
                    msData[index].mData = data;

                    //リモートデータに入れる
                    for (int i = 0; i < cs.mValue.size(); i++) {
                        msRemote.mValue.put(cs.GetKey(i), cs.GetString(cs.GetKey(i)));
                    }
                    //　ジャーナル記録
                    mSQLCommon.SetJournal("処分業者選択：" + (String) msRemote.mValue.get("contractor_name"),
                             msRemote, (mMyRemoteType != REMOTE_KEIRYO));

                    //表示更新
                    mMainPanel.UpdateView(msRemote);

                } else {
                    msRemote.mValue.put("dis_cont_id", data);
                    msRemote.mValue.put("contractor_name", "");
                }
                break;

            case INDEX_CAR_KIND://車種
                Car_KindStruct ck = new Car_KindStruct();
                if (mSQLID.GetCar_KindList(data, (Car_KindStruct) ck) == SQLDefine.SQL_OK) {
                    msData[index].mAccept = false;
                    msData[index].mExist = true;
                    msData[index].mData = data;

                    //リモートデータに入れる
                    for (int i = 0; i < ck.mValue.size(); i++) {
                        msRemote.mValue.put(ck.GetKey(i), ck.GetString(ck.GetKey(i)));
                    }
                    //　ジャーナル記録
                    mSQLCommon.SetJournal("車種選択：" + (String) msRemote.mValue.get("car_kind_name"),
                             msRemote, (mMyRemoteType != REMOTE_KEIRYO));

                    //表示更新
                    mMainPanel.UpdateView(msRemote);

                } else {
                    msRemote.mValue.put("car_kind_id", data);
                    msRemote.mValue.put("car_kind_name", "");
                }
                break;

            case INDEX_BILL://請求先
                BillStruct bs = new BillStruct();
                if (mSQLID.GetBillList(data, (BillStruct) bs) == SQLDefine.SQL_OK) {
                    msData[index].mAccept = false;
                    msData[index].mExist = true;
                    msData[index].mData = data;

                    //リモートデータに入れる
                    for (int i = 0; i < bs.mValue.size(); i++) {
                        msRemote.mValue.put(bs.GetKey(i), bs.GetString(bs.GetKey(i)));
                    }

                    //　ジャーナル記録
                    mSQLCommon.SetJournal("請求先選択：" + (String) msRemote.mValue.get("bill_name"),
                             msRemote, (mMyRemoteType != REMOTE_KEIRYO));

                    //表示更新
                    mMainPanel.UpdateView(msRemote);

                } else {
                    msRemote.mValue.put("bill_id", data);
                    msRemote.mValue.put("bill_name", "");
                }
                break;

            case INDEX_INPUT_CONFIRM://確認

                if (MainPanel.OpaqueFlag_) {

                    msProgress.mWeightReady = false;//重量確定
                    msProgress.mConfirmed = true;//確認押下済

                    MainPanel.OpaqueFlag_ = false;

                    long stay_sno, shipment_sno;

                    //印刷フラグ data=="1":マスタ設定に従う（計量ボタン）
                    //印刷フラグ data=="2":印刷しない（登録ボタン）
                    msRemote.mValue.put("print_flag", data);

                    while (true) {

                        //入出構別処理
                        if (MainPanel.net_weight_ == 0 && MainPanel.total_weight_ == 0 && MainPanel.empty_weight_ == 0) {

                            //滞留データ作成
                            if (mSQLCommon.SetStayData(msRemote) >= 0) {
                                //メッセージ
                                mSound.Play("/03.wav");
                                msRemote.mValue.put("message", "重量を記録しました。");
                            } else {
                                msRemote.mValue.put("message", "登録内容を確認してください。");
                            }

                            //計量完了後に計量ボタンを押せなくする
                            MainPanel.OpaqueFlag_ = false;
                            MainPanel.WeightFlag_ = true;

                            //ジャーナル
                            mSQLCommon.SetJournal(String.format("確認"), msRemote, (mMyRemoteType != REMOTE_KEIRYO));

                        } else {

                            //滞留SNO取得
                            stay_sno = mSQLCommon.GetStayDataRaw(msRemote, msStay);

                            //計量実績作成処理
                            //　１回計量なら滞留データが無いため計量実績
                            //　データを作成するための滞留データ作成
                            if ((stay_sno = mSQLCommon.GetStayDataRaw(msRemote, msStay)) < 0) {
                                //滞留データ作成
                                if (!MainPanel.weight_edit_) {
                                    msRemote.mValue.put("total_weight", MainPanel.total_weight_);
                                    mSQLCommon.SetStayData1(msRemote);

                                } else if (MainPanel.weight_edit_) {
                                    mSQLCommon.SetStayData_edit(msRemote, MainPanel.empty_weight_);
                                } else if (MainPanel.weight_add_) {
                                    mSQLCommon.SetStayData_edit(msRemote, MainPanel.total_weight_);
                                }

                                mSQLCommon.SetStayData2(msRemote);

                                try {
                                    Thread.sleep(20);
                                } catch (Exception e) {
                                }
                            } else {
                                //1回目にマニフェストNOが入力されていなく
                                //2回目に入力された場合
                                mSQLCommon.SetStayData2(msRemote);
                                //滞留データ作成
                                if (!MainPanel.weight_edit_) {
                                } else if (MainPanel.weight_edit_) {
                                    mSQLCommon.SetStayData_edit2(msRemote, MainPanel.empty_weight_);
                                } else if (MainPanel.weight_add_) {
                                    mSQLCommon.SetStayData_edit2(msRemote, MainPanel.total_weight_);
                                }

                            }

                            int weight = 0;
                            if (msRemote.GetInt("carry_class") == 0) {
                                weight = MainPanel.empty_weight_;
                            } else {
                                weight = MainPanel.total_weight_;
                            }

                            if ((shipment_sno = SetShipmentData(msRemote, String.valueOf(weight))) > 0) {

                                msRemote.mValue.put("計量日時", sdf.format(mSQLCommon.Get計量日時(shipment_sno)));

                                try {
                                    Thread.sleep(20);
                                } catch (Exception e) {
                                }
                                try {
                                    Thread.sleep(1000);
                                } catch (Exception e) {
                                }
                                //計量実績作成成功時
                                ShipmentStruct ss = new ShipmentStruct();

                                mSound.Play("/04.wav");
                                msRemote.mValue.put("message", "計量を完了しました。");
                                msData[INDEX_INPUT_CANCEL].mAccept = false;
                                MainPanel.weight_edit_ = false;

                                //画面の計量更新
                                try {
                                    MainPanel.count = Integer.valueOf(GetReportNoTEXT(MainPanel.con_, MainPanel.term_date_).replaceAll("A", "").replaceAll("B", ""));
                                } catch (Exception e) {
                                }

                                //計量完了後に計量ボタンを押せなくする
                                MainPanel.OpaqueFlag_ = false;
                                MainPanel.WeightFlag_ = true;

                                //ジャーナル
                                mSQLCommon.SetJournal(String.format("確認%s:計量SNO%s", data, shipment_sno), msRemote, (mMyRemoteType != REMOTE_KEIRYO));

                            }
                        }
                        break;
                    }

                    //表示更新
                    mMainPanel.UpdateView(msRemote);

                    //押した時間と車両IDを記録
                    mLastConfirmTime = System.currentTimeMillis();
                }

                break;

            case INDEX_INPUT_CANCEL://キャンセル
                //ジャーナル
                if (data != null && data.length() > 0) {
                    mSQLCommon.SetJournal(String.format("キャンセル"), msRemote, (mMyRemoteType != REMOTE_KEIRYO));
                }
                SetCancel();

                break;

            case INDEX_INPUT_RESET://キャンセル

                SetCancel();

                //ジャーナル
                mSQLCommon.SetJournal(String.format("キャンセル"), msRemote, (mMyRemoteType != REMOTE_KEIRYO));

                break;

            case INDEX_WINDOW_OPEN:
                //ジャーナル
                mSQLCommon.SetJournal(String.format("%sPC - 起動", (mMyRemoteType == REMOTE_KEIRYO ? "計量室" : "管理室")), msRemote, (mMyRemoteType != REMOTE_KEIRYO));
                break;
            case INDEX_WINDOW_CLOSE:
                //ジャーナル
                mSQLCommon.SetJournal(String.format("%sPC - 停止", (mMyRemoteType == REMOTE_KEIRYO ? "計量室" : "管理室")), msRemote, (mMyRemoteType != REMOTE_KEIRYO));
                break;
            case INDEX_SANGYO:
                //産業マニフェスト番号
                msRemote.mValue.put("manifesto_no1", data);
                //ジャーナル
                mSQLCommon.SetJournal("産業マニフェスト番号：" + (String) msRemote.mValue.get("manifesto_no1"),
                         msRemote, (mMyRemoteType != REMOTE_KEIRYO));
                break;
            case INDEX_KENSETSU:
                //建設マニフェスト番号
                msRemote.mValue.put("manifesto_no2", data);
                //ジャーナル
                mSQLCommon.SetJournal("建設マニフェスト番号：" + (String) msRemote.mValue.get("manifesto_no2"),
                         msRemote, (mMyRemoteType != REMOTE_KEIRYO));
                break;
            case INDEX_SANGYO2:
                //産業マニフェスト2番号
                msRemote.mValue.put("manifesto_no3", data);
                //ジャーナル
                mSQLCommon.SetJournal("産業マニフェスト2番号：" + (String) msRemote.mValue.get("manifesto_no3"),
                         msRemote, (mMyRemoteType != REMOTE_KEIRYO));
                break;
            case INDEX_OFFICE_SUB:
                //現場名追記対応
                //20120617
                msRemote.mValue.put("office_sub_name", data);
                //　ジャーナル記録
                mSQLCommon.SetJournal("排出事業場２選択：" + (String) msRemote.mValue.get("office_sub_name"),
                         msRemote, (mMyRemoteType != REMOTE_KEIRYO));
                break;
            //20120914 add
            case INDEX_M3:
                msRemote.mValue.put("m3", data);
                break;

            case INDEX_G:
                msRemote.mValue.put("ryube", data);
                break;

            case INDEX_UNIT_VALUE:
                //料金単価
                //20130422
                msRemote.mValue.put("unit_value", data);
                //　ジャーナル記録
                mSQLCommon.SetJournal("料金単価選択：" + (String) msRemote.mValue.get("unit_value"),
                         msRemote, (mMyRemoteType != REMOTE_KEIRYO));
                break;

            case INDEX_CHARGE_VALUE:
                msRemote.mValue.put("charge_value", data);
                break;

            case INDEX_CLASS:
                //管理自社区分
                msRemote.mValue.put("class", Integer.valueOf(data));
                //ジャーナル
                mSQLCommon.SetJournal("管理自社区分：" + data,
                         msRemote, (mMyRemoteType != REMOTE_KEIRYO));
                break;
            case INDEX_EMPTY:
                //風袋手入力時
                MainPanel.weight_edit_ = true;
                MainPanel.empty_weight_ = Integer.valueOf(data);

                if ((MainPanel.now_weight_1 > MainPanel.empty_weight_) && (MainPanel.empty_weight_ > 0)) {
                    MainPanel.total_weight_ = MainPanel.now_weight_1;
                    MainPanel.net_weight_ = MainPanel.total_weight_ - MainPanel.empty_weight_;
                    msRemote.mValue.put("total_weight", MainPanel.total_weight_);
                    msRemote.mValue.put("empty_weight", MainPanel.empty_weight_);
                    msRemote.mValue.put("net_weight", MainPanel.net_weight_);
                    msRemote.mValue.put("now_weight", MainPanel.now_weight_1);
                } else {
                    msRemote.mValue.put("empty_weight", MainPanel.empty_weight_);
                    MainPanel.total_weight_ = 0;
                    MainPanel.net_weight_ = 0;
                    msRemote.mValue.put("total_weight", MainPanel.total_weight_);
                    msRemote.mValue.put("empty_weight", MainPanel.empty_weight_);
                    msRemote.mValue.put("net_weight", MainPanel.net_weight_);
                    msRemote.mValue.put("now_weight", MainPanel.now_weight_1);
                }

                //ジャーナル
                mSQLCommon.SetJournal("風袋手入力：" + data,
                         msRemote, (mMyRemoteType != REMOTE_KEIRYO));
                break;
            case INDEX_MESSAGE:
                //メッセージ
                msRemote.mValue.put("message", data);
                //表示更新
                mMainPanel.UpdateView(msRemote);
                break;
        }

    }

    private void getOfficeUnit(String veiw_name, int id_) {

        try {

            //商品マスタの確認
            String s = "select * from "
                     + veiw_name
                     + " where id = "
                     + String.valueOf(id_);

            PreparedStatement ps = MainPanel.con_.prepareStatement(s);
            try {
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                }

                s = "select * from v0_office "
                         + " where ent_id = "
                         + String.valueOf(msRemote.mValue.get("dis_ent_id"))
                         + " and id = "
                         + String.valueOf(msRemote.mValue.get("dis_office_id"));
                ps = MainPanel.con_.prepareStatement(s);
                try {
                    ResultSet rs01 = ps.executeQuery();
                    if (rs01.next()) {
                        msRemote.mValue.put("unit", rs01.getInt(String.format("goods_unit%1$02d", rs.getInt("sno"))));
                        msRemote.mValue.put("unit_value", rs01.getInt(String.format("goods_unit%1$02d", rs.getInt("sno"))));
                    }
                } catch (SQLException e) {
                    System.out.println(e.toString());

                }

            } finally {
                ps.close();
            }
        } catch (Exception e) {
            System.out.println(e.toString());

        }

    }

    private void getAgreementUnit(int ent_id_, int office_id_, int goods_id_) {

        try {

            //商品マスタの確認
            String s = "select * from v0_office_unit"
                     + " where ent_id = ? "
                     + " and office_id = ? "
                     + " and goods_id = ?; ";

            PreparedStatement ps = MainPanel.con_.prepareStatement(s);
            try {
                ps.setObject(1, ent_id_);
                ps.setObject(2, office_id_);
                ps.setObject(3, goods_id_);
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    msRemote.mValue.put("goods_name", rs.getString("goods_name"));
                    msRemote.mValue.put("goods_unit", rs.getInt("goods_unit"));
                    msRemote.mValue.put("unit_value", rs.getInt("goods_unit"));
                    msRemote.mValue.put("goods_unit_no", rs.getInt("goods_unit_no"));
                    msRemote.mValue.put("goods_unit_no_name", rs.getString("goods_unit_no_name"));
                } else {
                    msRemote.mValue.put("goods_name", "");
                    msRemote.mValue.put("goods_unit", 0);
                    msRemote.mValue.put("goods_unit_no", 0);
                    msRemote.mValue.put("goods_unit_no_name", "");

                    s = "select * from v0_goods_kind"
                             + " where id = ?; ";
                    ps = MainPanel.con_.prepareStatement(s);
                    ps.setObject(1, goods_id_);
                    rs = ps.executeQuery();
                    if (rs.next()) {
                        msRemote.mValue.put("goods_name", rs.getString("name"));
                    }
                }
            } finally {
                ps.close();
            }
        } catch (Exception e) {
            System.out.println(e.toString());
        }
    }

    //データのセット
    //index:セットするデータ種別 INDEX_*
    //data:データ
    public void SetData2(int index, String data) {

        switchLabel01:
        switch (index) {
            case INDEX_CAR_ID://

                msRemote.mValue.put("car_id", data);
                if (mSQLCommon.GetStayDataRaw2(msRemote, msStay) < 0) {
                    //リモートデータに入れる
                    MainPanel.選択計量回数_ = 2;
                    msRemote.mValue.put("car_no", data);
                    if (!MainPanel.契約取込flag) {
                        msRemote.mValue.put("dis_ent_id", "0");
                        msRemote.mValue.put("entrepreneur_name", "");
                        msRemote.mValue.put("trans_id", "0");
                        msRemote.mValue.put("transporter_name", "");
                        msRemote.mValue.put("dis_office_id", "0");
                        msRemote.mValue.put("office_name", "");
                        msRemote.mValue.put("goods_id", "0");
                        msRemote.mValue.put("goods_name", "");
                    }
                    msRemote.mValue.put("car_kind_id", "0");
                    msRemote.mValue.put("car_kind_name", "");
                    msRemote.mValue.put("empty_weight", "0");
                    msRemote.mValue.put("bill_id", "0");
                    msRemote.mValue.put("bill_name", "");
                    msData[index].mExist = true;
                    msData[index].mData = data;

                    if (Integer.valueOf((String) msRemote.mValue.get("empty_weight")) > 0) {
                        msRemote.mValue.put("loading_weight", (Integer) msStay.mValue.get("empty_weight"));

                    } else {
                        msRemote.mValue.put("sub_message", "【２回計量】マスター該当無し");
                        msRemote.mValue.put("loading_weight", 0);
                    }
                } else {
                    //滞留データがあれば
                    //リモートデータに入れる
                    MainPanel.選択計量回数_ = 3;
                    msRemote.mValue.put("sno", (String) msStay.mValue.get("sno"));
                    msRemote.mValue.put("bill_id", (String) msStay.mValue.get("bill_id"));
                    msRemote.mValue.put("bill_name", (String) msStay.mValue.get("bill_name"));
                    msRemote.mValue.put("dis_ent_id", (String) msStay.mValue.get("dis_ent_id"));
                    msRemote.mValue.put("entrepreneur_name", (String) msStay.mValue.get("entrepreneur_name"));
                    msRemote.mValue.put("trans_id", (String) msStay.mValue.get("trans_id"));
                    msRemote.mValue.put("transporter_name", (String) msStay.mValue.get("transporter_name"));
                    msRemote.mValue.put("dis_office_id", (String) msStay.mValue.get("dis_office_id"));
                    msRemote.mValue.put("office_name", (String) msStay.mValue.get("office_name"));
                    msRemote.mValue.put("goods_id", (String) msStay.mValue.get("goods_id"));
                    msRemote.mValue.put("goods_name", (String) msStay.mValue.get("goods_name"));
                    msRemote.mValue.put("dis_cont_id", (String) msStay.mValue.get("dis_cont_id"));
                    msRemote.mValue.put("contractor_name", (String) msStay.mValue.get("contractor_name"));
                    msRemote.mValue.put("car_kind_id", (String) msStay.mValue.get("car_kind_id"));
                    msRemote.mValue.put("car_kind_name", (String) msStay.mValue.get("car_kind_name"));

                    msRemote.mValue.put("loading_weight", (String) msStay.mValue.get("register_weight"));
                    msRemote.mValue.put("manifesto_no1", (String) msStay.mValue.get("manifesto_no1"));
                    msRemote.mValue.put("manifesto_no2", (String) msStay.mValue.get("manifesto_no2"));
                    msRemote.mValue.put("manifesto_no3", (String) msStay.mValue.get("manifesto_no3"));

                    msRemote.mValue.put("office_sub_name", (String) msStay.mValue.get("office_sub_name"));

                    msRemote.mValue.put("car_no", (String) msStay.mValue.get("car_no"));
                    msRemote.mValue.put("carry_class", (String) msStay.mValue.get("carry_class"));
                    msRemote.mValue.put("carry_name", (String) msStay.mValue.get("carry_name"));
                    msRemote.mValue.put("sub_message", "【２回計量】一時記憶データ該当有り");
                    msRemote.mValue.put("m3", (String) msStay.mValue.get("m3"));
                    msRemote.mValue.put("ryube", (String) msStay.mValue.get("ryube"));
                    msRemote.mValue.put("unit_value", (String) msStay.mValue.get("unit_value"));
                    msRemote.mValue.put("unit_no", (String) msStay.mValue.get("unit_no"));
                    msRemote.mValue.put("charge_value", (int) Math.round(Double.valueOf((String) msStay.mValue.get("charge_value"))));

                    msData[index].mExist = true;
                    msData[index].mData = data;
                    MainPanel.loading_weight_ = Integer.valueOf((String) msRemote.mValue.get("loading_weight"));
                    MainPanel.carry_class_ = Integer.valueOf((String) msRemote.mValue.get("carry_class"));
                }

                WeightReady_ = true;
                MainPanel.OpaqueFlag_ = true;

                mSQLCommon.SetJournal("車番選択：" + data,
                         msRemote, (mMyRemoteType != REMOTE_KEIRYO));

                //表示更新
                mMainPanel.UpdateView(msRemote);

                break;

            case INDEX_TRANS://運搬業者
                TransporterStruct ts = new TransporterStruct();
                if (mSQLID.GetTransporterList(data, (TransporterStruct) ts) == SQLDefine.SQL_OK) {
                    msData[index].mAccept = false;
                    msData[index].mExist = true;
                    msData[index].mData = data;

                    //リモートデータに入れる
                    for (int i = 0; i < ts.mValue.size(); i++) {
                        msRemote.mValue.put(ts.GetKey(i), ts.GetString(ts.GetKey(i)));
                    }
                    //現在状態構築
                    BuildStatus();
                    ReloadStatus();

                    //表示更新
                    mMainPanel.UpdateView(msRemote);
                }
                break;

            case INDEX_ENT://排出事業者
                ContStruct cs = new ContStruct();
                if (mSQLID.GetContList(data, (ContStruct) cs) == SQLDefine.SQL_OK) {
                    msData[index].mAccept = false;
                    msData[index].mExist = true;
                    msData[index].mData = data;

                    //リモートデータに入れる
                    for (int i = 0; i < cs.mValue.size(); i++) {
                        msRemote.mValue.put(cs.GetKey(i), cs.GetString(cs.GetKey(i)));
                    }
                    //現在状態構築
                    BuildStatus();
                    ReloadStatus();

                    //表示更新
                    mMainPanel.UpdateView(msRemote);
                }
                break;

            case INDEX_OFFICE://排出事業場
                OfficeStruct os = new OfficeStruct();
                if (mSQLID.GetOfficeList(data, (String) msRemote.mValue.get("dis_ent_id"), (OfficeStruct) os) == SQLDefine.SQL_OK) {
                    msData[index].mAccept = false;
                    msData[index].mExist = true;
                    msData[index].mData = data;

                    //リモートデータに入れる
                    for (int i = 0; i < os.mValue.size(); i++) {
                        msRemote.mValue.put(os.GetKey(i), os.GetString(os.GetKey(i)));
                    }
                    //現在状態構築
                    BuildStatus();
                    ReloadStatus();

                    //表示更新
                    mMainPanel.UpdateView(msRemote);
                }
                break;
        }
    }

    private String GetReportNoTEXT(Connection con, java.util.Date term_date_)
             throws SQLException, UnknownHostException {
        String s = "select report_no from v1_weigh_data where term_date = ? order by sno desc limit 1;";
        PreparedStatement ps2 = con.prepareStatement(s);
        try {
            ps2.setObject(1, term_date_);
            ResultSet rs2 = ps2.executeQuery();
            if (rs2.next()) {

                return rs2.getString("report_no");

            } else {
                return "1";
            }
        } finally {
            ps2.close();
//      return printer_print_order;
        }
    }

    //現在状態構築
    public void BuildStatus() {
        msData[INDEX_INPUT_SHIPMENT].mAccept = false;//出荷
        msData[INDEX_INPUT_ARRAIVAL].mAccept = false;//入荷

        msData[INDEX_INPUT_CONFIRM].mAccept = false;//確認

        msProgress.mWeightReady = false;
        msProgress.mGateDirection = UNKNOWN_STATE;

        if (msData[INDEX_TRANS].mExist == true
                 && msData[INDEX_ENT].mExist == true
                 && msData[INDEX_OFFICE].mExist == true
                 && msData[INDEX_CAR_ID].mExist == true
                 && msData[INDEX_CONT].mExist == true
                 && msData[INDEX_KIND].mExist == true) {

            //IDが全て入力されている場合
            msData[INDEX_TRANS].mAccept = false;
            msData[INDEX_ENT].mAccept = false;
            msData[INDEX_OFFICE].mAccept = false;
            msData[INDEX_CAR_ID].mAccept = false;
            msData[INDEX_CONT].mAccept = false;
            msData[INDEX_KIND].mAccept = false;

            WeightReady_ = true;
            msRemote.mValue.put("message", "<html>内容を確認し、間違いなければ計量ボタンを押してください。"
                     + "<br>処理を取り消す場合はキャンセルボタンを押してください。</html>");
        }
    }

    //状態データの再読み込み
    public void ReloadStatus() {
        SQLCommon sql = mSQLCommon;
        OfficeStruct es = new OfficeStruct();

        //環境変数の取込
        if (sql.GetEnv(es, "sno") != SQLCommon.SQL_ERR) {
            //最小計量間隔の更新
            SetDataEx(INDEX_EX_WEIGH_INTERVAL, es.GetString("weigh_interval"));

            //最小計量可能重量の更新
            SetDataEx(INDEX_EX_MIN_WEIGHT, es.GetString("min_weight"));

        }

    }

    //計量実績データ作成
    public long SetShipmentData(RemoteStruct rs, String data) {
        long shipment_stat = mSQLCommon.SetShipmentData(rs, data);
        if (shipment_stat == -1) {
            //計量実績作成失敗
            //メッセージ
            MainPanel.PopupMessage("計量実績の作成に失敗しました。\n内容を確認してください。");
            msRemote.mValue.put("message", "計量実績の作成に失敗しました。");
            msData[INDEX_INPUT_CANCEL].mAccept = true;

        } else if (shipment_stat < 0) {
            //計量実績作成失敗
            //メッセージ
            MainPanel.PopupMessage("計量実績の作成に失敗しました。\n内容を確認してください。");
            msRemote.mValue.put("message", "計量実績の作成に失敗しました。");
            msData[INDEX_INPUT_CANCEL].mAccept = true;

        }

        return shipment_stat;
    }

    //データのクリア
    public void SetCancel() {
        for (int i = 1; i < msData.length; i++) {//計量以外はクリアする
            msData[i].Clear();
        }

        msData[INDEX_INPUT_CANCEL].mAccept = true;//キャンセルボタンは有効にする

        msRemote.Clear();
        msProgress.Clear();
        msStay.Clear();
        msShipment.Clear();
        MainPanel.net_weight_ = 0;
        MainPanel.total_weight_ = 0;
        MainPanel.empty_weight_ = 0;
        MainPanel.now_weight_1 = 0;
        MainPanel.loading_weight_ = 0;
        MainPanel.weight_add_ = false;
        MainPanel.weight_edit_ = false;

        //表示更新
        mMainPanel.UpdateView(msRemote);

        //重量オーバーフラグ初期化
        f重量オーバー = false;
    }
}
