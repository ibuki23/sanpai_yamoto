package aks.industrialwaste.recycle.ui.manifesto;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.*;
import javax.swing.*;
import aks.industrialwaste.recycle.ui.sql.SQLCommon;
import aks.industrialwaste.recycle.ui.weighlist.*;
import java.awt.Window;
import java.awt.Frame;
import java.sql.SQLException;
import aks.industrialwaste.database.DBManager;
import aks.industrialwaste.recycle.report.XlsxManifesto;
import aks.industrialwaste.recycle.util.ContSelector;
import aks.industrialwaste.recycle.util.EntSelector;
import aks.industrialwaste.recycle.util.TransSelector;
import aks.swing.MyLineSlash1;
import java.awt.Color;
import java.awt.GridBagLayout;
import java.awt.event.KeyEvent;

public class KennsetuManifestoPanel100 extends javax.swing.JPanel {

    private SQLCommon mSQLCommon;
    private Connection con_;
    public String[][] 排出事業者Array = new String[7][];
    public String[][] 排出事業場Array = new String[9][];
    public String[][] 運搬業者名2Array = new String[7][];
    public String[][] 処分業者名Array = new String[15][];
    public String[][] 運搬事業場Array = new String[6][];
    public String[][] 交付担当者Array = new String[2][];
    public String[][] 車種Array = new String[2][];
    public String[][] 処分方法Array = new String[2][];
    public String[][] 保管場所Array = new String[6][];
    public String[][] 運搬担当者Array1 = new String[5][];
    public String[][] 運搬担当者Array2 = new String[5][];
    public String[][] 按分品目Array = new String[6][];
    //選択されている行の各idを取得する
    public int ent_id_ = 0;
    public int goods_id_ = 0;
    public int office_id_ = 0;
    public int trans1_id_ = 0;
    public int trans2_id_ = 0;
    public int cont_office_id_ = 0;
    public int cont_id_ = 0;
    public int car_kind1_id_ = 0;
    public int car_kind2_id_ = 0;
    public int methot_id_ = 0;
    //取得したidを要素とする配列のindexを取得する(表示のため)
    public int ent_id_index = 0;
    public int office_id_index = 0;
    public int trans1_id_index = 0;
    public int trans2_id_index = 0;
    public int cont_id_index = 4;
    public int methot_id_index = 0;
    public int cont_office_id_index = 1;
    public int car_kind1_id_index = 0;
    public int car_kind2_id_index = 0;
    public int row_ = -1;
    private final MyLineSlash1 MyLine1;
    private final MyLineSlash1 MyLine2;
    private final MyLineSlash1 MyLine3;
    private MyLineSlash1 MyLine4;
    private MyLineSlash1 MyLine5;
    private WeighListTableModel model_;
    private int mode_;
    private Long sno_;
    //20260317 add
    private ButtonGroup bg;

    public KennsetuManifestoPanel100(WeighListTableModel model, int row, Connection con, int mode) {
        initComponents();
        model_ = model;
        con_ = con;
        row_ = row;

        // 0:空伝 1:通常
        mode_ = mode;
        if (mode_ == 0) {
            sno_ = (Long) 0l;
        } else {
            sno_ = (Long) model_.getColumns(row_).get("sno");
        }

        if (mode_ == 0) {
            jLabel4.setVisible(true);
            pring_num_.setVisible(true);
            pring_num_.setValue(0);
            rejist_btn_kensetu.setVisible(false);
            画面初期化_btn_.setVisible(true);
            //マニフェストNO
            manifesto_no2.setText("");
        } else {
            jLabel4.setVisible(false);
            pring_num_.setVisible(false);
            pring_num_.setValue(1);
            rejist_btn_kensetu.setVisible(true);
            画面初期化_btn_.setVisible(false);
            //マニフェストNO
            manifesto_no2.setText((String) model.getColumns(row_).get("manifesto_no2"));
        }

        mSQLCommon = new SQLCommon(DBManager.getURL(), DBManager.getUser(), DBManager.getPassword());

        //初期値
        //印字フラグの設定
        運搬業者_print_f2.setSelected((Boolean) model.getColumns(row_).get("trans2_print_flag"));
        処分業者_print_f1.setSelected((Boolean) model.getColumns(row_).get("cont_print_flag"));
        運搬の受託_print_f3.setSelected((Boolean) model.getColumns(row_).get("trans_sub1_print_flag"));
        運搬の受託_print_f4.setSelected((Boolean) model.getColumns(row_).get("trans_sub2_print_flag"));

        //選択されている行の各idを取得する
        ent_id_ = (Integer) model.getColumns(row_).get("dis_ent_id");
        office_id_ = (Integer) model.getColumns(row_).get("dis_office_id");
        trans1_id_ = (Integer) model.getColumns(row_).get("trans1_id");
        trans2_id_ = (Integer) model.getColumns(row_).get("trans2_id");
        cont_id_ = (Integer) model.getColumns(row_).get("dis_cont_id");
        methot_id_ = (Integer) model.getColumns(row_).get("method_id");
        car_kind1_id_ = (Integer) model.getColumns(row_).get("car_kind_id");
        cont_office_id_ = (Integer) model.getColumns(row_).get("manifesto3_id1");

        check_goods_flag(false);

        //コンボを編集可能にする
        tantou_cmb.setEditable(true);
        tantou_cmb1.setEditable(true);
        last_cont_cmb.setEditable(true);
        trans_car_kind01.setEditable(true);
        trans_car_kind02.setEditable(true);

        //交付年月日()　元号使用止め西暦表示、入力に変更　180918
        if ((Date) model.getColumns(row_).get("grant_date2") != null) {
            grant_year.setText(String.format("%tY", (Date) model.getColumns(row_).get("grant_date2")));
            grant_month.setText(String.format("%tm", (Date) model.getColumns(row_).get("grant_date2")));
            grant_day.setText(String.format("%td", (Date) model.getColumns(row_).get("grant_date2")));
        } else {
            grant_year.setText(String.format("%tY", (Date) model.getColumns(row_).get("term_date")));
            grant_month.setText(String.format("%tm", (Date) model.getColumns(row_).get("term_date")));
            grant_day.setText(String.format("%td", (Date) model.getColumns(row_).get("term_date")));
            model.getColumns(row_).put("grant_date2", (Date) model.getColumns(row_).get("term_date"));
        }

        //交付年月日印刷フラグ　true
        交付年月日_print_f_.setSelected((Boolean) model.getColumns(row_).get("grant2_print_f"));

        if (!(Boolean) model.getColumns(row_).get("goods_flag01")
                 && !(Boolean) model.getColumns(row_).get("goods_flag02")
                 && !(Boolean) model.getColumns(row_).get("goods_flag03")
                 && !(Boolean) model.getColumns(row_).get("goods_flag04")
                 && !(Boolean) model.getColumns(row_).get("f按分フラグ")) {
            //初回画面展開時の按分フラグ以外はkg選択
            goods_f02.setSelected(true);
        } else if (!(Boolean) model.getColumns(row_).get("goods_flag01")
                 && !(Boolean) model.getColumns(row_).get("goods_flag02")
                 && !(Boolean) model.getColumns(row_).get("goods_flag03")
                 && !(Boolean) model.getColumns(row_).get("goods_flag04")
                 && (Boolean) model.getColumns(row_).get("f按分フラグ")) {
            //初回画面展開時の按分フラグ品目はm3選択
            goods_f02.setSelected(true);//kgにチェック
        } else {
            goods_f02.setSelected((Boolean) model.getColumns(row_).get("goods_flag02"));
        }

        //単位印刷フラグ　true
        単位_print_f_.setSelected((Boolean) model.getColumns(row_).get("tani_print_f"));

        //形状印刷フラグ　true
        形状_print_f_.setSelected((Boolean) model.getColumns(row_).get("keijyo_print_f"));
        if (!(Boolean) model.getColumns(row_).get("goods_flag33")
                 && !(Boolean) model.getColumns(row_).get("goods_flag34")
                 && !(Boolean) model.getColumns(row_).get("goods_flag35")) {
            goods_f33.setSelected(true);
            goods_f33.setForeground(Color.RED);
        } else {
            goods_f33.setSelected((Boolean) model.getColumns(row_).get("goods_flag33"));
        }

        //荷姿印刷フラグ　true
        荷姿_print_f_.setSelected((Boolean) model.getColumns(row_).get("nisugata_print_f"));
        if (!(Boolean) model.getColumns(row_).get("goods_flag36")
                 && !(Boolean) model.getColumns(row_).get("goods_flag37")
                 && !(Boolean) model.getColumns(row_).get("goods_flag38")
                 && !(Boolean) model.getColumns(row_).get("goods_flag39")) {
            goods_f36.setSelected(true);
            goods_f36.setForeground(Color.RED);
        } else {
            goods_f36.setSelected((Boolean) model.getColumns(row_).get("goods_flag36"));
        }

        //マスタ問い合わせ
        //排出事業者
        mSQLCommon.GetMasterData("v0_ent", 排出事業者Array);
        String[] entName = new String[排出事業者Array[0].length];
        for (int i = 0; i <= 排出事業者Array[0].length - 1; i++) {
            entName[i] = (String) 排出事業者Array[1][i];
            if (ent_id_ == Integer.valueOf((String) 排出事業者Array[0][i])) {
                ent_id_index = i;
            }
        }

        //排出事業場
        mSQLCommon.GetMasterDataOffice("v0_office", 排出事業場Array, ent_id_);
        String[] OfficeId = new String[排出事業場Array[0].length];
        for (int i = 0; i < 排出事業場Array[0].length; i++) {
            if ((String) 排出事業場Array[0][i] != null) {
                OfficeId[i] = (String) 排出事業場Array[0][i];

                if (office_id_ == Integer.valueOf((String) 排出事業場Array[0][i])) {
                    office_id_index = i;
                }
            } else {
                OfficeId[i] = "0";
            }
        }
        office_id_cmb.setModel(new javax.swing.DefaultComboBoxModel(OfficeId));

        //運搬業者
        mSQLCommon.GetMasterData("v0_trans", 運搬業者名2Array);
        String[] TransName = new String[運搬業者名2Array[0].length];
        for (int i = 0; i <= 運搬業者名2Array[0].length - 1; i++) {
            TransName[i] = (String) 運搬業者名2Array[1][i];
            if (trans1_id_ == Integer.valueOf((String) 運搬業者名2Array[0][i])) {
                trans1_id_index = i;
            }
            if (trans2_id_ == Integer.valueOf((String) 運搬業者名2Array[0][i])) {
                trans2_id_index = i;
            }
        }
        trans_cmb01.setModel(new javax.swing.DefaultComboBoxModel(TransName));
        trans_cmb02.setModel(new javax.swing.DefaultComboBoxModel(TransName));

        /////////////////////////////////////////////////////////////////////
        //運搬担当１
        mSQLCommon.GetMasterDataTransTantou("v0_trans_tantou", 運搬担当者Array1, trans1_id_);
        if (運搬担当者Array1[0].length > 0) {
            String[] TransTantouID1 = new String[運搬担当者Array1[0].length];
            for (int i = 0; i < 運搬担当者Array1[0].length; i++) {
                TransTantouID1[i] = (String) 運搬担当者Array1[3][i];
            }
            trans_tantou_id_cmb1.setModel(new javax.swing.DefaultComboBoxModel(TransTantouID1));
        }

        /////////////////////////////////////////////////////////////////////
        //運搬担当２
        mSQLCommon.GetMasterDataTransTantou("v0_trans_tantou", 運搬担当者Array2, trans2_id_);
        if (運搬担当者Array2[0].length > 0) {
            String[] TransTantouID2 = new String[運搬担当者Array2[0].length];
            for (int i = 0; i < 運搬担当者Array2[0].length; i++) {
                TransTantouID2[i] = (String) 運搬担当者Array2[3][i];
            }
            trans_tantou_id_cmb2.setModel(new javax.swing.DefaultComboBoxModel(TransTantouID2));
        }

        //車種
        mSQLCommon.GetMasterData8("v0_car_kind", 車種Array);
        String[] carKindName = new String[車種Array[0].length];
        for (int i = 0; i <= 車種Array[0].length - 1; i++) {
            carKindName[i] = (String) 車種Array[1][i];
            if (car_kind1_id_ == Integer.valueOf((String) 車種Array[0][i])) {
                car_kind1_id_index = i;
            }
        }
        trans_car_kind01.setModel(new javax.swing.DefaultComboBoxModel(carKindName));
        if (((String) model.getColumns(row_).get("trans1_car_kind")).length() > 0) {
            trans_car_kind01.setSelectedItem((String) model.getColumns(row_).get("trans1_car_kind"));
        } else {
            trans_car_kind01.setSelectedItem((String) model.getColumns(row_).get("car_kind_name"));
        }
        trans_car_kind02.setModel(new javax.swing.DefaultComboBoxModel(carKindName));
        if (((String) model.getColumns(row_).get("trans2_car_kind")).length() > 0) {
            trans_car_kind02.setSelectedItem((String) model.getColumns(row_).get("trans2_car_kind"));
        } else {
            trans_car_kind02.setSelectedItem("");
        }

        //処分業者
        mSQLCommon.GetMasterData2("v0_cont", 処分業者名Array);
        String[] ContName = new String[処分業者名Array[0].length];
        for (int i = 0; i <= 処分業者名Array[0].length - 1; i++) {
            ContName[i] = (String) 処分業者名Array[1][i];
            if (cont_id_ == Integer.valueOf((String) 処分業者名Array[0][i])) {
                cont_id_index = i;
            }
        }
        cont_cmb01.setModel(new javax.swing.DefaultComboBoxModel(ContName));

        //運搬先の事業場
        mSQLCommon.GetMasterData4_1("v0_manifesto_3", 運搬事業場Array, cont_id_);
        String[] ContOfficeName = new String[運搬事業場Array[0].length];
        String[] LastContName = new String[運搬事業場Array[0].length];
        for (int i = 0; i <= 運搬事業場Array[0].length - 1; i++) {
            ContOfficeName[i] = (String) 運搬事業場Array[1][i];
            LastContName[i] = (String) 運搬事業場Array[1][i] + "　"
                     + (String) 運搬事業場Array[2][i] + (String) 運搬事業場Array[3][i];
            if (cont_office_id_ == Integer.valueOf((String) 運搬事業場Array[0][i])) {
                cont_office_id_index = i;
            }
        }

        cont_office_name01.setModel(new javax.swing.DefaultComboBoxModel(ContOfficeName));
        //最終処分を行った場所
        last_cont_cmb.setModel(new javax.swing.DefaultComboBoxModel(LastContName));

        //備考
        txt備考.setText((String) model.getColumns(row_).get("remark_name"));

        //交付担当者
        mSQLCommon.GetMasterData6("v0_manifesto_1", 交付担当者Array);
        String[] GrantName = new String[交付担当者Array[0].length];
        int grant_index = 0;
        for (int i = 0; i <= 交付担当者Array[0].length - 1; i++) {
            GrantName[i] = (String) 交付担当者Array[1][i];
            if (((String) model.getColumns(row_).get("grant_name")).equals(String.valueOf((String) 交付担当者Array[1][i]))) {
                grant_index = i;
            }
        }
        tantou_cmb.setModel(new javax.swing.DefaultComboBoxModel(GrantName));
        tantou_cmb.setSelectedItem((String) model.getColumns(row_).get("grant_name"));
        tantou_cmb1.setModel(new javax.swing.DefaultComboBoxModel(GrantName));
        tantou_cmb1.setSelectedItem((String) model.getColumns(row_).get("grant_name01"));

        //20260317 add///////ここから/////////////////////////////////////////////////////////////////////////////////////////
        //ボタングループ追加
        bg = new ButtonGroup();
        bg.add(goods_f01);
        bg.add(goods_f02);
        bg.add(goods_f03);
        bg.add(goods_f04);

        //各チェックの設定 20260317 edit
//        goods_f01.setSelected((Boolean) model.getColumns(row_).get("goods_flag01"));
//        goods_f02.setSelected((Boolean) model.getColumns(row_).get("goods_flag02"));
//        goods_f03.setSelected((Boolean) model.getColumns(row_).get("goods_flag03"));
//        goods_f04.setSelected((Boolean) model.getColumns(row_).get("goods_flag04"));
        if (!(Boolean) model.getColumns(row_).get("goods_flag01")
                 && !(Boolean) model.getColumns(row_).get("goods_flag02")
                 && !(Boolean) model.getColumns(row_).get("goods_flag03")
                 && !(Boolean) model.getColumns(row_).get("goods_flag04")) {
            //いずれもfalseなら初回の画面展開時
            if ((Integer) model.getColumns(row_).get("unit_no") == 1) {
                goods_f01.setSelected(true);
                int net = (Integer) model_.getColumns(row_).get("net_weight");
                net_weight_field.setText(String.valueOf((double) net / 1000));
            } else if ((Integer) model.getColumns(row_).get("unit_no") == 2) {
                goods_f02.setSelected(true);
                net_weight_field.setText(String.format("%,d", (Integer) model.getColumns(row_).get("net_weight")));
            }
        } else {
            //１回でも登録されていれば
            goods_f01.setSelected((Boolean) model.getColumns(row_).get("goods_flag01"));
            goods_f02.setSelected((Boolean) model.getColumns(row_).get("goods_flag02"));
            goods_f03.setSelected((Boolean) model.getColumns(row_).get("goods_flag03"));
            goods_f04.setSelected((Boolean) model.getColumns(row_).get("goods_flag04"));
        }
        ////////////////////////ここまで///////////////////////////////////////////////////////////////////////////////////////

        goods_f34.setSelected((Boolean) model.getColumns(row_).get("goods_flag34"));
        goods_f35.setSelected((Boolean) model.getColumns(row_).get("goods_flag35"));
        goods_f37.setSelected((Boolean) model.getColumns(row_).get("goods_flag37"));
        goods_f38.setSelected((Boolean) model.getColumns(row_).get("goods_flag38"));
        goods_f39.setSelected((Boolean) model.getColumns(row_).get("goods_flag39"));
        goods_f40.setSelected((Boolean) model.getColumns(row_).get("goods_flag40"));
        goods_f41.setSelected((Boolean) model.getColumns(row_).get("goods_flag41"));//専用容器
        weight_print_check();

        check_f1.setSelected((Boolean) model.getColumns(row_).get("check_flag01"));
        check_f2.setSelected((Boolean) model.getColumns(row_).get("check_flag02"));
        check_f3.setSelected((Boolean) model.getColumns(row_).get("check_flag03"));
        check_f4.setSelected((Boolean) model.getColumns(row_).get("check_flag04"));

        if (!(Boolean) model.getColumns(row_).get("check_flag05") && !(Boolean) model.getColumns(row_).get("check_flag06")) {
            check_f5.setSelected(false);
            check_f6.setSelected(true);
        } else {
            check_f5.setSelected((Boolean) model.getColumns(row_).get("check_flag05"));
            check_f6.setSelected((Boolean) model.getColumns(row_).get("check_flag06"));
        }
        check_f7.setSelected((Boolean) model.getColumns(row_).get("check_flag07"));
        check_f8.setSelected((Boolean) model.getColumns(row_).get("check_flag08"));
        check_f9.setSelected((Boolean) model.getColumns(row_).get("check_flag09"));
        check_f10.setSelected((Boolean) model.getColumns(row_).get("check_flag10"));
////        check_f11.setSelected((Boolean) model.getColumns(row_).get("check_flag11"));
        check_f11.setSelected(true);
        check_f12.setSelected((Boolean) model.getColumns(row_).get("check_flag12"));
        check_f13.setSelected((Boolean) model.getColumns(row_).get("check_flag13"));
        check_f14.setSelected((Boolean) model.getColumns(row_).get("check_flag14"));
        check_f15.setSelected((Boolean) model.getColumns(row_).get("check_flag15"));
        check_f16.setSelected((Boolean) model.getColumns(row_).get("check_flag16"));
        check_f17.setSelected((Boolean) model.getColumns(row_).get("check_flag17"));
        check_f18.setSelected((Boolean) model.getColumns(row_).get("check_flag18"));

        運搬の受託_print_f3.setSelected((Boolean) model.getColumns(row_).get("trans_sub1_print_flag"));
        運搬の受託_print_f4.setSelected((Boolean) model.getColumns(row_).get("trans_sub2_print_flag"));
//        処分の受託_print_f2.setSelected((Boolean) model.getColumns(row_).get("cont_sub1_print_flag"));
//        処分の受託_print_f3.setSelected(true);
//        処分の受託_print_f3.setSelected((Boolean) model.getColumns(row_).get("cont_sub2_print_flag"));
//        最終処分確認_print_f.setSelected(true);
//        最終処分確認_print_f.setSelected((Boolean) model.getColumns(row_).get("last_tantou_print_flag"));

        ent_name.setText((String) model.getColumns(row_).get("ent_p_name"));
        ent_zip01.setText((String) model.getColumns(row_).get("ent_zip_code"));
        ent_tel01.setText((String) model.getColumns(row_).get("ent_tel"));
        ent_address01.setText((String) model.getColumns(row_).get("ent_address1"));
        ent_search_.setText(String.valueOf((Integer) model.getColumns(row_).get("dis_ent_id")));

        office_name_.setText((String) 排出事業場Array[1][office_id_index]);
        office_name_.setToolTipText((String) 排出事業場Array[1][office_id_index]);
        office_zip01.setText((String) 排出事業場Array[2][office_id_index]);
        office_tel01.setText((String) 排出事業場Array[3][office_id_index]);
        office_address01.setText((String) 排出事業場Array[4][office_id_index] + (String) 排出事業場Array[5][office_id_index]);
        office_search_.setText((String) 排出事業場Array[0][office_id_index]);

        trans_cmb01.setSelectedIndex(trans1_id_index);
        trans_zip01.setText((String) 運搬業者名2Array[2][trans1_id_index]);
        trans_tel01.setText((String) 運搬業者名2Array[3][trans1_id_index]);
        trans_address01.setText((String) 運搬業者名2Array[4][trans1_id_index] + (String) 運搬業者名2Array[5][trans1_id_index]);

        if (((String) model.getColumns(row_).get("trans1_car_no")).length() > 0) {
            trans_car_no01.setText((String) model.getColumns(row_).get("trans1_car_no"));
        } else {
            trans_car_no01.setText(((Integer) model.getColumns(row_).get("car_no")).toString());
        }

        trans_cmb02.setSelectedIndex(trans2_id_index);
        trans_zip02.setText((String) 運搬業者名2Array[2][trans2_id_index]);
        trans_tel02.setText((String) 運搬業者名2Array[3][trans2_id_index]);
        trans_address02.setText((String) 運搬業者名2Array[4][trans2_id_index] + (String) 運搬業者名2Array[5][trans2_id_index]);

        if (((String) model.getColumns(row_).get("trans2_car_no")).length() > 0) {
            trans_car_no02.setText((String) model.getColumns(row_).get("trans2_car_no"));
        } else {
            trans_car_no02.setText("");
        }

//        cont_cmb01.setSelectedIndex(cont_id_index);
        cont_cmb01.setSelectedIndex(1);
//        cont_zip01.setText((String) 処分業者名Array[2][cont_id_index]);
        cont_zip01.setText((String) 処分業者名Array[2][1]);
        cont_tel01.setText((String) 処分業者名Array[3][1]);
//        cont_tel01.setText((String) 処分業者名Array[3][cont_id_index]);
//        cont_address01.setText((String) 処分業者名Array[4][cont_id_index] + (String) 処分業者名Array[5][cont_id_index]);
        cont_address01.setText((String) 処分業者名Array[4][1] + (String) 処分業者名Array[5][1]);
        cont_name1.setText((String) 処分業者名Array[13][1]);//cont_tantou 20151010 担当者追加
//        cont_name1.setText((String) 処分業者名Array[13][cont_id_index]);//cont_tantou 20151010 担当者追加
//        cont_name2.setText((String) 処分業者名Array[13][cont_id_index]);

        最終処分担当者.setText((String) 処分業者名Array[14][cont_id_index]);//last_tantou 20151010 最終処分担当者追加
//        最終処分担当者.setText((String) 処分業者名Array[14][1]);//last_tantou 20151010 最終処分担当者追加
        cont_office_name01.setSelectedIndex(cont_office_id_index);
        cont_office_zip01.setText((String) 運搬事業場Array[4][cont_office_id_index]);
        cont_office_tel01.setText((String) 運搬事業場Array[5][cont_office_id_index]);
        cont_office_address01.setText((String) 運搬事業場Array[2][cont_office_id_index] + (String) 運搬事業場Array[3][cont_office_id_index]);

        office_sub_name_.setText((String) model.getColumns(row_).get("office_sub_name"));
        //手入力があれば
        if (model_.getColumns(row_).get("office_zip_code") != null && ((String) model_.getColumns(row_).get("office_zip_code")).length() > 0) {
            office_zip01.setText((String) model_.getColumns(row_).get("office_zip_code"));
        }
        if (model_.getColumns(row_).get("office_tel") != null && ((String) model_.getColumns(row_).get("office_tel")).length() > 0) {
            office_tel01.setText((String) model_.getColumns(row_).get("office_tel"));
        }
        if (model_.getColumns(row_).get("office_address1") != null && ((String) model_.getColumns(row_).get("office_address1")).length() > 0) {
            office_address01.setText((String) model_.getColumns(row_).get("office_address1"));
        }

//        last_cont_cmb.setSelectedItem((String) model.getColumns(row_).get("last_office_name"));
        //固定でプラント名を表示
        last_cont_cmb.setSelectedItem((String) model.getColumns(0).get("last_office_name"));

        //計量データ作成時のみマニフェスト画面を開いた際に初期値でｔ表示にする　リサイクル江刺　DB初期値goods_flag01　true
        //20260318 edit
        //品目マスタの単位を参照するためコメントアウト
//        if (goods_f01.isSelected()) {
//            int net = (Integer) model_.getColumns(row_).get("net_weight");
//            net_weight_field.setText(String.valueOf((double) net / 1000));
//        } else {
//            net_weight_field.setText(String.format("%,d", (Integer) model.getColumns(row_).get("net_weight")));
//        }
        MyLine1 = new MyLineSlash1();
        MyLine1.setLayout(new GridBagLayout());
        MyLine1.setMinimumSize(new java.awt.Dimension(360, 35));
        MyLine1.setPreferredSize(new java.awt.Dimension(360, 35));

        MyLine2 = new MyLineSlash1();
        MyLine2.setLayout(new GridBagLayout());
        MyLine2.setMinimumSize(new java.awt.Dimension(395, 135));
        MyLine2.setPreferredSize(new java.awt.Dimension(395, 135));

        MyLine3 = new MyLineSlash1();
        MyLine3.setLayout(new GridBagLayout());
        MyLine3.setMinimumSize(new java.awt.Dimension(400, 95));
        MyLine3.setPreferredSize(new java.awt.Dimension(400, 95));

        MyLine4 = new MyLineSlash1();
        MyLine4.setLayout(new GridBagLayout());
        MyLine4.setMinimumSize(new java.awt.Dimension(400, 95));
        MyLine4.setPreferredSize(new java.awt.Dimension(400, 95));
        MyLine4.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        MyLine5 = new MyLineSlash1();
        MyLine5.setLayout(new GridBagLayout());
        MyLine5.setMinimumSize(new java.awt.Dimension(230, 100));
        MyLine5.setPreferredSize(new java.awt.Dimension(230, 100));

        javax.swing.GroupLayout MyLine1Layout = new javax.swing.GroupLayout(MyLine1);
        MyLine1.setLayout(MyLine1Layout);
        MyLine1Layout.setHorizontalGroup(
                 MyLine1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                          .addGap(0, 90, Short.MAX_VALUE));
        MyLine1Layout.setVerticalGroup(
                 MyLine1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                          .addGap(0, 36, Short.MAX_VALUE));
        java.awt.GridBagConstraints gridBagConstraints = new java.awt.GridBagConstraints();

        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        jPanel13.add(MyLine1, gridBagConstraints);

        javax.swing.GroupLayout MyLine2Layout = new javax.swing.GroupLayout(MyLine2);
        MyLine2.setLayout(MyLine2Layout);
        MyLine2Layout.setHorizontalGroup(
                 MyLine2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                          .addGap(0, 90, Short.MAX_VALUE));
        MyLine2Layout.setVerticalGroup(
                 MyLine2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                          .addGap(0, 36, Short.MAX_VALUE));
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridheight = 1;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.NONE;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        jPanel53.add(MyLine2, gridBagConstraints);

        javax.swing.GroupLayout MyLine3Layout = new javax.swing.GroupLayout(MyLine3);
        MyLine3.setLayout(MyLine3Layout);
        MyLine3Layout.setHorizontalGroup(
                 MyLine3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                          .addGap(0, 90, Short.MAX_VALUE));
        MyLine3Layout.setVerticalGroup(
                 MyLine3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                          .addGap(0, 36, Short.MAX_VALUE));
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridheight = 1;
        gridBagConstraints.gridwidth = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        jPanel10.add(MyLine3, gridBagConstraints);

        javax.swing.GroupLayout MyLine4Layout = new javax.swing.GroupLayout(MyLine4);
        MyLine4.setLayout(MyLine4Layout);
        MyLine4Layout.setHorizontalGroup(
                 MyLine4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                          .addGap(0, 90, Short.MAX_VALUE));
        MyLine4Layout.setVerticalGroup(
                 MyLine4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                          .addGap(0, 36, Short.MAX_VALUE));
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridheight = 1;
        gridBagConstraints.gridwidth = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        jPanel10.add(MyLine4, gridBagConstraints);

        javax.swing.GroupLayout MyLine5Layout = new javax.swing.GroupLayout(MyLine5);
        MyLine5.setLayout(MyLine5Layout);
        MyLine5Layout.setHorizontalGroup(
                 MyLine5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                          .addGap(0, 90, Short.MAX_VALUE));
        MyLine5Layout.setVerticalGroup(
                 MyLine5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                          .addGap(0, 36, Short.MAX_VALUE));
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridheight = 1;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        jPanel57.add(MyLine5, gridBagConstraints);

        slash_f2.setSelected((Boolean) model.getColumns(row_).get("slash_flag02"));
        積み替え_f5.setSelected(true);

        排出事業者_print_f.setSelected(true);
        事業場_print_f.setSelected(true);
        goods_f40.setSelected(true);
        //当欄記載の通りにチェックで『社名』を表示する
        if (check_f4.isSelected() && !(check_f3.isSelected())) {
            jlbl_final_name_.setVisible(check_f4.isSelected());
        } else {
            jlbl_final_name_.setVisible(false);
        }

        if (((String) model.getColumns(row_).get("cont_office_sub_name1")).equals("")) {
            //初回入力が無ければ処分受託者を表示
            cont_office1.setText(cont_cmb01.getSelectedItem().toString());
            最終処分社名.setText(cont_cmb01.getSelectedItem().toString());
        } else {
            cont_office1.setText((String) model.getColumns(row_).get("cont_office_sub_name1"));
            最終処分社名.setText((String) model.getColumns(row_).get("cont_office_sub_name1"));
        }
        //交付担当者印刷フラグ　true
        交付担当者_print_f_.setSelected((Boolean) model.getColumns(row_).get("tantou_print_f"));

        //処分の受託１・２
        if (((String) model.getColumns(row_).get("cont_office_sub_name1")).equals("")) {
            //初回入力が無ければ運搬受託者を表示
            cont_office1.setText(cont_cmb01.getSelectedItem().toString());
            最終処分社名.setText(cont_cmb01.getSelectedItem().toString());
        } else {
            cont_office1.setText((String) model.getColumns(row_).get("cont_office_sub_name1"));
            最終処分社名.setText((String) model.getColumns(row_).get("cont_office_sub_name1"));
        }

        if (((String) model.getColumns(row_).get("cont_sub_name")).equals("")) {
        } else {
            cont_name1.setText((String) model.getColumns(row_).get("cont_sub_name"));
        }
        if (((String) model.getColumns(row_).get("cont_office_sub_name2")).equals("")) {
            //初回入力が無ければ運搬受託者を表示
            cont_office2.setText(cont_cmb01.getSelectedItem().toString());
        } else {
            cont_office2.setText((String) model.getColumns(row_).get("cont_office_sub_name2"));
        }

        if (((String) model.getColumns(row_).get("cont_sub_name2")).equals("")) {
        } else {
            cont_name2.setText((String) model.getColumns(row_).get("cont_sub_name2"));
        }
        //運搬の受託１・２
        if (((String) model.getColumns(row_).get("trans_office_sub_name1")).equals("")) {
            //初回入力が無ければ運搬受託者を表示
            trans_office1.setText(trans_cmb01.getSelectedItem().toString());
        } else {
            trans_office1.setText((String) model.getColumns(row_).get("trans_office_sub_name1"));
        }
        if (((String) model.getColumns(row_).get("trans1_sub_name")).equals("")) {
            trans_name1.setText("");
        } else {
            trans_name1.setText((String) model.getColumns(row_).get("trans1_sub_name"));
            trans_tantou_id_cmb1.setSelectedItem((String) model.getColumns(row_).get("trans1_sub_name"));
        }
        if (((String) model.getColumns(row_).get("trans_office_sub_name2")).equals("")) {
            trans_office2.setText(trans_cmb02.getSelectedItem().toString());
        } else {
            trans_office2.setText((String) model.getColumns(row_).get("trans_office_sub_name2"));
        }
        if (((String) model.getColumns(row_).get("trans2_sub_name")).equals("")) {
            trans_name2.setText("");
        } else {
            trans_name2.setText((String) model.getColumns(row_).get("trans2_sub_name"));
            trans_tantou_id_cmb2.setSelectedItem((String) model.getColumns(row_).get("trans2_sub_name"));
        }

        //備考
        mSQLCommon.GetMasterData8_1("v0_manifesto_5", 処分方法Array);
        String[] 処分方法Name = new String[処分方法Array[0].length];
        int 処分方法_index = 0;
        for (int i = 0; i <= 処分方法Array[0].length - 1; i++) {
            処分方法Name[i] = (String) 処分方法Array[1][i];
            if (((String) model.getColumns(row_).get("method_name")).equals(String.valueOf((String) 処分方法Array[1][i]))) {
                処分方法_index = i;
            }
        }
        cmb中間処理.setModel(new javax.swing.DefaultComboBoxModel(処分方法Name));
        cmb中間処理.setSelectedIndex(処分方法_index);
        if (cont_id_ == 100) {
            if (!check_f12.isSelected() && !check_f13.isSelected() && !check_f14.isSelected()) {
                check_f12.setSelected(true);
                cmb中間処理.setSelectedItem("埋立");
            }
        }

        if ((Date) model.getColumns(row_).get("cont_date1") != null) {
            cont_year1.setText(String.format("%tY", (Date) model.getColumns(row_).get("cont_date1")));
            cont_month1.setText(String.format("%tm", (Date) model.getColumns(row_).get("cont_date1")));
            cont_day1.setText(String.format("%td", (Date) model.getColumns(row_).get("cont_date1")));
        } else if ((Date) model.getColumns(row_).get("grant_date2") != null) {
            cont_year1.setText(String.format("%tY", (Date) model.getColumns(row_).get("grant_date2")));
            cont_month1.setText(String.format("%tm", (Date) model.getColumns(row_).get("grant_date2")));
            cont_day1.setText(String.format("%td", (Date) model.getColumns(row_).get("grant_date2")));
            cont_year1.setForeground(Color.RED);
            cont_month1.setForeground(Color.RED);
            cont_day1.setForeground(Color.RED);
            cont_year1.setDisabledTextColor(Color.RED);
            cont_month1.setDisabledTextColor(Color.RED);
            cont_day1.setDisabledTextColor(Color.RED);
        } else {
            cont_year1.setText("");
            cont_month1.setText("");
            cont_day1.setText("");
        }//受領日()　元号使用止め西暦表示、入力に変更　180918
//        if ((Date) model.getColumns(row_).get("cont_date1") != null) {
//            cont_year1.setText(String.format("%tY", (Date) model.getColumns(row_).get("cont_date1")));
//            cont_month1.setText(String.format("%tm", (Date) model.getColumns(row_).get("cont_date1")));
//            cont_day1.setText(String.format("%td", (Date) model.getColumns(row_).get("cont_date1")));
//        } else if ((Date) model.getColumns(row_).get("grant_date2") != null) {
//            cont_year1.setText(String.format("%tY", (Date) model.getColumns(row_).get("grant_date2")));
//            cont_month1.setText(String.format("%tm", (Date) model.getColumns(row_).get("grant_date2")));
//            cont_day1.setText(String.format("%td", (Date) model.getColumns(row_).get("grant_date2")));
//            cont_year1.setForeground(Color.RED);
//            cont_month1.setForeground(Color.RED);
//            cont_day1.setForeground(Color.RED);
//            cont_year1.setDisabledTextColor(Color.RED);
//            cont_month1.setDisabledTextColor(Color.RED);
//            cont_day1.setDisabledTextColor(Color.RED);
//        } else {
//            cont_year1.setText("");
//            cont_month1.setText("");
//            cont_day1.setText("");
//        }

        Calendar now = Calendar.getInstance();

        String cont_年1 = String.valueOf(now.get(Calendar.YEAR));
        String cont_月1 = String.valueOf(now.get(Calendar.MONTH) + 1); // 月は0始まりなので+1
        String cont_日1 = String.valueOf(now.get(Calendar.DAY_OF_MONTH));
        cont_year1.setText(cont_年1);
        cont_month1.setText(cont_月1);
        cont_day1.setText(cont_日1);
        cont_year2.setText(cont_年1);
        cont_month2.setText(cont_月1);
        cont_day2.setText(cont_日1);
        cont_date2_year.setText(cont_年1);
        cont_date2_month.setText(cont_月1);
        cont_date2_day.setText(cont_日1);

        //処分終了日()　元号使用止め西暦表示、入力に変更　180918
        if ((Date) model.getColumns(row_).get("cont_date2") != null) {
//            cont_year2.setText(String.format("%tY", (Date) model.getColumns(row_).get("cont_date2")));
//            cont_month2.setText(String.format("%tm", (Date) model.getColumns(row_).get("cont_date2")));
//            cont_day2.setText(String.format("%td", (Date) model.getColumns(row_).get("cont_date2")));
            //20140423 初期値は空欄に
        } else if ((Date) model.getColumns(row_).get("grant_date2") != null) {
//            cont_year2.setText(String.format("%tY", (Date) model.getColumns(row_).get("grant_date2")));
//            cont_month2.setText(String.format("%tm", (Date) model.getColumns(row_).get("grant_date2")));
//            cont_day2.setText(String.format("%td", (Date) model.getColumns(row_).get("grant_date2")));
//            cont_year2.setForeground(Color.RED);
//            cont_month2.setForeground(Color.RED);
//            cont_day2.setForeground(Color.RED);
//            cont_year2.setDisabledTextColor(Color.RED);
//            cont_month2.setDisabledTextColor(Color.RED);
//            cont_day2.setDisabledTextColor(Color.RED);
        } else {
//            cont_year2.setText("");
//            cont_month2.setText("");
//            cont_day2.setText("");
        }

        //最終処分終了日()　元号使用止め西暦表示、入力に変更　180918
        if ((Date) model.getColumns(row_).get("disposal_date2") != null) {
//            cont_date2_year.setText(String.format("%tY", (Date) model.getColumns(row_).get("disposal_date2")));
//            cont_date2_month.setText(String.format("%tm", (Date) model.getColumns(row_).get("disposal_date2")));
//            cont_date2_day.setText(String.format("%td", (Date) model.getColumns(row_).get("disposal_date2")));
        } else {
//            cont_date2_year.setText("");
//            cont_date2_month.setText("");
//            cont_date2_day.setText("");
        }

        //運搬受託日１　元号使用止め西暦表示、入力に変更　180918　
        if ((Date) model.getColumns(row_).get("trans1_date") != null) {
            trans_year1.setText(String.format("%tY", (Date) model.getColumns(row_).get("trans1_date")));//西暦四桁表示
            trans_month1.setText(String.format("%tm", (Date) model.getColumns(row_).get("trans1_date")));
            trans_day1.setText(String.format("%td", (Date) model.getColumns(row_).get("trans1_date")));

            //運搬受託日１の登録がなければ交付日を初期値とする　　　初期値で運搬日の日付を登録させないようにする　210513　
        } else if ((Date) model.getColumns(row_).get("grant_date2") != null) {

            trans_year1.setForeground(Color.RED);
            trans_month1.setForeground(Color.RED);
            trans_day1.setForeground(Color.RED);
            trans_year1.setDisabledTextColor(Color.RED);
            trans_month1.setDisabledTextColor(Color.RED);
            trans_day1.setDisabledTextColor(Color.RED);
        } else {
            trans_year1.setText("");
            trans_month1.setText("");
            trans_day1.setText("");
        }

        //運搬受託日２
        if ((Date) model.getColumns(row_).get("trans2_date") != null) {
            trans_year2.setText(String.format("%tY", (Date) model.getColumns(row_).get("trans2_date")));
            trans_month2.setText(String.format("%tm", (Date) model.getColumns(row_).get("trans2_date")));
            trans_day2.setText(String.format("%td", (Date) model.getColumns(row_).get("trans2_date")));
        } else {
            trans_year2.setText("");
            trans_month2.setText("");
            trans_day2.setText("");
        }

        //画面表示時にデフォルトで斜線有りチェック
        slash_f1.setSelected(true);

        check_f1.setSelected(false);
        check_f2.setSelected(false);
        運搬業者_print_f1.setSelected(true);
        運搬業者_print_f2.setSelected(true);

        if (!(Boolean) model.getColumns(row_).get("ent_print_flag")) {
            排出事業者_print_f.setSelected((Boolean) model.getColumns(row_).get("ent_print_flag"));
        }
        if (!(Boolean) model.getColumns(row_).get("office_print_flag")) {
            事業場_print_f.setSelected((Boolean) model.getColumns(row_).get("office_print_flag"));
        }
        if (!(Boolean) model.getColumns(row_).get("trans1_print_flag")) {
            運搬業者_print_f1.setSelected((Boolean) model.getColumns(row_).get("trans1_print_flag"));
        }
        if (!(Boolean) model.getColumns(row_).get("trans2_print_flag")) {
            運搬業者_print_f2.setSelected((Boolean) model.getColumns(row_).get("trans2_print_flag"));
        }

        //各チェック項目反映
        ent_print_f();
        office_print_f();
        trans_print_f1();
        trans_print_f2();
        cont_print_f1();
        tsumikae_f5();
        cont_print_f3();
        cont_print_f2();
        trans_print_f3();
        trans_print_f4();
        最終処分確認_print_f();//20151019 追加

        if (slash_f1.isSelected()) {
            check_f1.setVisible(false);
            check_f2.setVisible(false);
            MyLine1.setVisible(true);

        } else {
            check_f1.setVisible(true);
            check_f2.setVisible(true);
            MyLine1.setVisible(false);
        }

        mSQLCommon.GetMasterData4("v0_tsumikae", 保管場所Array);
        String[] 保管場所Name = new String[保管場所Array[0].length];
        int 保管場所_index = 0;
        for (int i = 0; i <= 保管場所Array[0].length - 1; i++) {
            保管場所Name[i] = (String) 保管場所Array[1][i];
            if (((String) model.getColumns(row_).get("last_cont_name02")).equals(String.valueOf((String) 保管場所Array[1][i]))) {
                保管場所_index = i;
            }
        }
        保管場所_cmb.setModel(new javax.swing.DefaultComboBoxModel(保管場所Name));
        保管場所_cmb.setSelectedIndex(保管場所_index);

        //積み替えまたは保管　住所
        if (((String) model.getColumns(row_).get("cont_address02")).length() > 0) {
            cont_address02.setText((String) model.getColumns(row_).get("cont_address02"));
        }
        //積み替えまたは保管　郵便番号
        if (((String) model.getColumns(row_).get("cont_zip02")).length() > 0) {
            cont_zip02.setText((String) model.getColumns(row_).get("cont_zip02"));
        }
        //積み替えまたは保管　電話番号
        if (((String) model.getColumns(row_).get("cont_tel02")).length() > 0) {
            cont_tel02.setText((String) model.getColumns(row_).get("cont_tel02"));
        }

        積み替え_f5.setSelected((Boolean) model.getColumns(row_).get("tsumikae_print_flag"));

        office_id_cmb.setSelectedItem("");
        office_id_cmb.setSelectedItem(office_search_.getText());

        office_sub_name_.setText((String) model.getColumns(row_).get("office_sub_name"));
        //手入力があれば
        if (model_.getColumns(row_).get("office_zip_code") != null && ((String) model_.getColumns(row_).get("office_zip_code")).length() > 0) {
            office_zip01.setText((String) model_.getColumns(row_).get("office_zip_code"));
        }
        if (model_.getColumns(row_).get("office_tel") != null && ((String) model_.getColumns(row_).get("office_tel")).length() > 0) {
            office_tel01.setText((String) model_.getColumns(row_).get("office_tel"));
        }
        if (model_.getColumns(row_).get("office_address1") != null && ((String) model_.getColumns(row_).get("office_address1")).length() > 0) {
            office_address01.setText((String) model_.getColumns(row_).get("office_address1"));
        }

        trans_name1.setVisible(false);
        trans_name2.setVisible(false);

        //建設系マニフェスト　総重量窓枠の数量を品目ごとの小窓枠に再取得で空白にしている
        if (model.getColumns(row_).get("goods_weight01") != null) {
            value_tex1.setText((String) model.getColumns(row_).get("goods_weight01"));
        }

        if (model.getColumns(row_).get("goods_weight02") != null) {
            value_tex2.setText((String) model.getColumns(row_).get("goods_weight02"));
        }
        if (model.getColumns(row_).get("goods_weight03") != null) {
            value_tex3.setText((String) model.getColumns(row_).get("goods_weight03"));
        }
        if (model.getColumns(row_).get("goods_weight04") != null) {
            value_tex4.setText((String) model.getColumns(row_).get("goods_weight04"));
        }
        if (model.getColumns(row_).get("goods_weight05") != null) {
            value_tex5.setText((String) model.getColumns(row_).get("goods_weight05"));
        }
        if (model.getColumns(row_).get("goods_weight06") != null) {
            value_tex6.setText((String) model.getColumns(row_).get("goods_weight06"));
        }
        if (model.getColumns(row_).get("goods_weight07") != null) {
            value_tex7.setText((String) model.getColumns(row_).get("goods_weight07"));
        }
        if (model.getColumns(row_).get("goods_weight08") != null) {
            value_tex8.setText((String) model.getColumns(row_).get("goods_weight08"));
        }
        if (model.getColumns(row_).get("goods_weight09") != null) {
            value_tex13.setText((String) model.getColumns(row_).get("goods_weight09"));
        }
        if (model.getColumns(row_).get("goods_weight10") != null) {
            value_tex14.setText((String) model.getColumns(row_).get("goods_weight10"));
        }
        if (model.getColumns(row_).get("goods_weight11") != null) {
            value_tex15.setText((String) model.getColumns(row_).get("goods_weight11"));
        }
        if (model.getColumns(row_).get("goods_weight12") != null) {
            value_tex16.setText((String) model.getColumns(row_).get("goods_weight12"));
        }
        if (model.getColumns(row_).get("goods_weight13") != null) {
            value_tex17.setText((String) model.getColumns(row_).get("goods_weight13"));
        }
        if (model.getColumns(row_).get("goods_weight14") != null) {
            value_tex18.setText((String) model.getColumns(row_).get("goods_weight14"));
        }
        if (model.getColumns(row_).get("goods_weight15") != null) {
            value_tex19.setText((String) model.getColumns(row_).get("goods_weight15"));

        }
        if (model.getColumns(row_).get("goods_weight16") != null) {
            value_tex23.setText((String) model.getColumns(row_).get("goods_weight16"));
        }
        if (model.getColumns(row_).get("goods_weight17") != null) {//水銀使用製品産業廃棄物
            value_tex20.setText((String) model.getColumns(row_).get("goods_weight17"));
        }

        if ((Boolean) model.getColumns(row_).get("f按分フラグ") && sno_ > 0) {
            //按分有の場合の数量表示
            mSQLCommon.Get按分Data(sno_, 按分品目Array);
            check_goods_flag(true);
        }

        印字初期値設定();

    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        jScrollPane2 = new javax.swing.JScrollPane();
        jPanel21 = new javax.swing.JPanel();
        jPanel28 = new javax.swing.JPanel();
        goods_f36 = new javax.swing.JCheckBox();
        goods_f37 = new javax.swing.JCheckBox();
        goods_f38 = new javax.swing.JCheckBox();
        goods_f39 = new javax.swing.JCheckBox();
        jPanel12 = new javax.swing.JPanel();
        jLabel9 = new javax.swing.JLabel();
        荷姿_print_f_ = new javax.swing.JCheckBox();
        goods_f41 = new javax.swing.JCheckBox();
        jPanel19 = new javax.swing.JPanel();
        goods_f33 = new javax.swing.JCheckBox();
        goods_f34 = new javax.swing.JCheckBox();
        goods_f35 = new javax.swing.JCheckBox();
        jPanel6 = new javax.swing.JPanel();
        jLabel8 = new javax.swing.JLabel();
        形状_print_f_ = new javax.swing.JCheckBox();
        jPanel17 = new javax.swing.JPanel();
        goods_f01 = new javax.swing.JCheckBox();
        goods_f02 = new javax.swing.JCheckBox();
        goods_f03 = new javax.swing.JCheckBox();
        goods_f04 = new javax.swing.JCheckBox();
        jLabel14 = new javax.swing.JLabel();
        jPanel11 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        単位_print_f_ = new javax.swing.JCheckBox();
        jPanel10 = new javax.swing.JPanel();
        jPanel25 = new javax.swing.JPanel();
        cont_cmb01 = new javax.swing.JComboBox();
        jLabel56 = new javax.swing.JLabel();
        cont_zip01 = new aks.swing.JTextFieldHankaku();
        jLabel57 = new javax.swing.JLabel();
        cont_tel01 = new aks.swing.JTextFieldHankaku();
        cont_address01 = new aks.swing.JTextField();
        jLabel127 = new javax.swing.JLabel();
        cont_search_ = new aks.swing.JTextFieldHankaku();
        jButton4 = new javax.swing.JButton();
        jPanel27 = new javax.swing.JPanel();
        jLabel84 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        txt備考 = new javax.swing.JTextArea();
        jPanel55 = new javax.swing.JPanel();
        jLabel113 = new javax.swing.JLabel();
        cont_zip02 = new aks.swing.JTextFieldHankaku();
        jLabel114 = new javax.swing.JLabel();
        cont_tel02 = new aks.swing.JTextFieldHankaku();
        cont_address02 = new aks.swing.JTextField();
        jLabel128 = new javax.swing.JLabel();
        cont_search_1 = new aks.swing.JTextFieldHankaku();
        保管場所_cmb = new javax.swing.JComboBox();
        jLabel21 = new javax.swing.JLabel();
        check_f15 = new javax.swing.JCheckBox();
        check_f16 = new javax.swing.JCheckBox();
        trans1_weight = new aks.swing.JTextField();
        check_f18 = new javax.swing.JCheckBox();
        check_f17 = new javax.swing.JCheckBox();
        jPanel4 = new javax.swing.JPanel();
        jLabel46 = new javax.swing.JLabel();
        check_f3 = new javax.swing.JCheckBox();
        check_f4 = new javax.swing.JCheckBox();
        jLabel99 = new javax.swing.JLabel();
        jPanel33 = new javax.swing.JPanel();
        jPanel46 = new javax.swing.JPanel();
        jlbl_final_name_ = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        jLabel78 = new javax.swing.JLabel();
        last_cont_cmb = new aks.swing.JComboBox();
        print_btn_kensetu = new javax.swing.JButton();
        quit_btn_kensetu = new javax.swing.JButton();
        rejist_btn_kensetu = new javax.swing.JButton();
        jLabel4 = new javax.swing.JLabel();
        pring_num_ = new javax.swing.JSpinner();
        画面初期化_btn_ = new javax.swing.JButton();
        jPanel9 = new javax.swing.JPanel();
        jPanel14 = new javax.swing.JPanel();
        jPanel41 = new javax.swing.JPanel();
        jLabel120 = new javax.swing.JLabel();
        jLabel122 = new javax.swing.JLabel();
        jLabel133 = new javax.swing.JLabel();
        jLabel134 = new javax.swing.JLabel();
        jLabel135 = new javax.swing.JLabel();
        jPanel42 = new javax.swing.JPanel();
        排出事業者_print_f = new javax.swing.JCheckBox();
        jLabel54 = new javax.swing.JLabel();
        jLabel63 = new javax.swing.JLabel();
        ent_zip01 = new aks.swing.JTextFieldHankaku();
        ent_tel01 = new aks.swing.JTextFieldHankaku();
        ent_address01 = new aks.swing.JTextField();
        jLabel65 = new javax.swing.JLabel();
        jLabel74 = new javax.swing.JLabel();
        ent_search_ = new aks.swing.JTextFieldHankaku();
        jLabel110 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        ent_name = new javax.swing.JTextField();
        jPanel43 = new javax.swing.JPanel();
        jLabel66 = new javax.swing.JLabel();
        jLabel67 = new javax.swing.JLabel();
        jLabel68 = new javax.swing.JLabel();
        jLabel69 = new javax.swing.JLabel();
        jLabel70 = new javax.swing.JLabel();
        jLabel88 = new javax.swing.JLabel();
        jPanel44 = new javax.swing.JPanel();
        事業場_print_f = new javax.swing.JCheckBox();
        jLabel2 = new javax.swing.JLabel();
        jLabel89 = new javax.swing.JLabel();
        office_zip01 = new aks.swing.JTextFieldHankaku();
        office_tel01 = new aks.swing.JTextFieldHankaku();
        office_address01 = new aks.swing.JTextField();
        jLabel90 = new javax.swing.JLabel();
        office_sub_name_ = new aks.swing.JTextField();
        jLabel75 = new javax.swing.JLabel();
        office_search_ = new aks.swing.JTextFieldHankaku();
        jLabel112 = new javax.swing.JLabel();
        office_id_cmb = new javax.swing.JComboBox();
        office_name_ = new aks.swing.JTextField();
        jPanel7 = new javax.swing.JPanel();
        jLabel10 = new javax.swing.JLabel();
        jPanel8 = new javax.swing.JPanel();
        grant_year = new aks.swing.JTextFieldHankaku();
        grant_month = new aks.swing.JTextFieldHankaku();
        jLabel107 = new javax.swing.JLabel();
        jLabel108 = new javax.swing.JLabel();
        grant_day = new aks.swing.JTextFieldHankaku();
        jLabel109 = new javax.swing.JLabel();
        jLabel58 = new javax.swing.JLabel();
        manifesto_no2 = new javax.swing.JTextField();
        jLabel111 = new javax.swing.JLabel();
        tantou_cmb = new aks.swing.JComboBox();
        jLabel125 = new javax.swing.JLabel();
        tantou_cmb1 = new aks.swing.JComboBox();
        交付年月日_print_f_ = new javax.swing.JCheckBox();
        交付担当者_print_f_ = new javax.swing.JCheckBox();
        jPanel31 = new javax.swing.JPanel();
        jPanel30 = new javax.swing.JPanel();
        jLabel61 = new javax.swing.JLabel();
        処分業者_print_f1 = new javax.swing.JCheckBox();
        jPanel35 = new javax.swing.JPanel();
        jLabel62 = new javax.swing.JLabel();
        積み替え_f5 = new javax.swing.JCheckBox();
        jLabel5 = new javax.swing.JLabel();
        jPanel13 = new javax.swing.JPanel();
        check_f2 = new javax.swing.JCheckBox();
        check_f1 = new javax.swing.JCheckBox();
        jLabel92 = new javax.swing.JLabel();
        jLabel104 = new javax.swing.JLabel();
        jLabel105 = new javax.swing.JLabel();
        jLabel106 = new javax.swing.JLabel();
        jPanel34 = new javax.swing.JPanel();
        jPanel47 = new javax.swing.JPanel();
        slash_f1 = new javax.swing.JCheckBox();
        jPanel29 = new javax.swing.JPanel();
        jPanel38 = new javax.swing.JPanel();
        jLabel76 = new javax.swing.JLabel();
        処分の受託_print_f2 = new javax.swing.JCheckBox();
        cont_office1 = new aks.swing.JTextField();
        cont_name1 = new aks.swing.JTextField();
        cont_day1 = new aks.swing.JTextFieldHankaku();
        jLabel93 = new javax.swing.JLabel();
        cont_month1 = new aks.swing.JTextFieldHankaku();
        jLabel94 = new javax.swing.JLabel();
        cont_year1 = new aks.swing.JTextFieldHankaku();
        jLabel96 = new javax.swing.JLabel();
        jPanel20 = new javax.swing.JPanel();
        jPanel40 = new javax.swing.JPanel();
        jLabel86 = new javax.swing.JLabel();
        jLabel87 = new javax.swing.JLabel();
        cont_date2_month = new aks.swing.JTextFieldHankaku();
        jLabel97 = new javax.swing.JLabel();
        cont_date2_day = new aks.swing.JTextFieldHankaku();
        jLabel98 = new javax.swing.JLabel();
        cont_date2_year = new aks.swing.JTextFieldHankaku();
        最終処分社名 = new aks.swing.JTextField();
        最終処分担当者 = new aks.swing.JTextField();
        最終処分確認_print_f = new javax.swing.JCheckBox();
        jPanel39 = new javax.swing.JPanel();
        jLabel83 = new javax.swing.JLabel();
        処分の受託_print_f3 = new javax.swing.JCheckBox();
        cont_office2 = new aks.swing.JTextField();
        cont_name2 = new aks.swing.JTextField();
        cont_day2 = new aks.swing.JTextFieldHankaku();
        jLabel116 = new javax.swing.JLabel();
        cont_month2 = new aks.swing.JTextFieldHankaku();
        jLabel117 = new javax.swing.JLabel();
        cont_year2 = new aks.swing.JTextFieldHankaku();
        jLabel118 = new javax.swing.JLabel();
        jPanel5 = new javax.swing.JPanel();
        jPanel56 = new javax.swing.JPanel();
        jLabel85 = new javax.swing.JLabel();
        運搬の受託_print_f3 = new javax.swing.JCheckBox();
        jLabel73 = new javax.swing.JLabel();
        jPanel18 = new javax.swing.JPanel();
        trans_office1 = new aks.swing.JTextField();
        trans_tantou_search_1 = new aks.swing.JTextFieldHankaku();
        trans_tantou_id_cmb1 = new javax.swing.JComboBox();
        trans_year1 = new aks.swing.JTextFieldHankaku();
        jLabel121 = new javax.swing.JLabel();
        trans_month1 = new aks.swing.JTextFieldHankaku();
        jLabel123 = new javax.swing.JLabel();
        trans_day1 = new aks.swing.JTextFieldHankaku();
        jLabel124 = new javax.swing.JLabel();
        trans_name1 = new aks.swing.JTextField();
        jPanel57 = new javax.swing.JPanel();
        jPanel58 = new javax.swing.JPanel();
        trans_office2 = new aks.swing.JTextField();
        trans_name2 = new aks.swing.JTextField();
        trans_tantou_id_cmb2 = new javax.swing.JComboBox();
        trans_tantou_search_2 = new aks.swing.JTextFieldHankaku();
        trans_year2 = new aks.swing.JTextFieldHankaku();
        jLabel136 = new javax.swing.JLabel();
        trans_month2 = new aks.swing.JTextFieldHankaku();
        jLabel137 = new javax.swing.JLabel();
        trans_day2 = new aks.swing.JTextFieldHankaku();
        jLabel138 = new javax.swing.JLabel();
        jLabel131 = new javax.swing.JLabel();
        運搬の受託_print_f4 = new javax.swing.JCheckBox();
        slash_f3 = new javax.swing.JCheckBox();
        jPanel3 = new javax.swing.JPanel();
        goods_f16 = new javax.swing.JCheckBox();
        goods_f23 = new javax.swing.JCheckBox();
        goods_f24 = new javax.swing.JCheckBox();
        goods_f25 = new javax.swing.JCheckBox();
        goods_f22 = new javax.swing.JCheckBox();
        goods_f21 = new javax.swing.JCheckBox();
        goods_f32 = new javax.swing.JCheckBox();
        goods_f29 = new javax.swing.JCheckBox();
        goods_f28 = new javax.swing.JCheckBox();
        goods_f17 = new javax.swing.JCheckBox();
        goods_f31 = new javax.swing.JCheckBox();
        goods_f19 = new javax.swing.JCheckBox();
        goods_f18 = new javax.swing.JCheckBox();
        goods_f20 = new javax.swing.JCheckBox();
        goods_f15 = new javax.swing.JCheckBox();
        goods_f14 = new javax.swing.JCheckBox();
        goods_f11 = new javax.swing.JCheckBox();
        goods_f26 = new javax.swing.JCheckBox();
        goods_f09 = new javax.swing.JCheckBox();
        goods_f10 = new javax.swing.JCheckBox();
        goods_f08 = new javax.swing.JCheckBox();
        goods_f12 = new javax.swing.JCheckBox();
        goods_f07 = new javax.swing.JCheckBox();
        goods_f06 = new javax.swing.JCheckBox();
        goods_f13 = new javax.swing.JCheckBox();
        goods_f27 = new javax.swing.JCheckBox();
        goods_f05 = new javax.swing.JCheckBox();
        jLabel100 = new javax.swing.JLabel();
        value_tex1 = new aks.swing.JTextField();
        goods_f30 = new javax.swing.JCheckBox();
        value_tex3 = new aks.swing.JTextField();
        value_tex2 = new aks.swing.JTextField();
        jLabel102 = new javax.swing.JLabel();
        jLabel103 = new javax.swing.JLabel();
        jLabel101 = new javax.swing.JLabel();
        jLabel25 = new javax.swing.JLabel();
        jLabel27 = new javax.swing.JLabel();
        value_tex4 = new aks.swing.JTextField();
        value_tex5 = new aks.swing.JTextField();
        value_tex6 = new aks.swing.JTextField();
        value_tex8 = new aks.swing.JTextField();
        value_tex9 = new aks.swing.JTextField();
        value_tex10 = new aks.swing.JTextField();
        value_tex11 = new aks.swing.JTextField();
        value_tex13 = new aks.swing.JTextField();
        value_tex7 = new aks.swing.JTextField();
        value_tex14 = new aks.swing.JTextField();
        value_tex15 = new aks.swing.JTextField();
        value_tex16 = new aks.swing.JTextField();
        value_tex17 = new aks.swing.JTextField();
        value_tex19 = new aks.swing.JTextField();
        value_tex24 = new aks.swing.JTextField();
        value_tex12 = new aks.swing.JTextField();
        jLabel15 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        value_tex20 = new aks.swing.JTextField();
        value_tex21 = new aks.swing.JTextField();
        value_tex22 = new aks.swing.JTextField();
        value_tex18 = new aks.swing.JTextField();
        jLabel18 = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        value_tex25 = new aks.swing.JTextField();
        value_tex26 = new aks.swing.JTextField();
        value_tex23 = new aks.swing.JTextField();
        value_tex28 = new aks.swing.JTextField();
        value_tex27 = new aks.swing.JTextField();
        jPanel15 = new javax.swing.JPanel();
        jLabel16 = new javax.swing.JLabel();
        net_weight_field = new aks.swing.JTextField();
        goods_f40 = new javax.swing.JCheckBox();
        jPanel26 = new javax.swing.JPanel();
        jPanel22 = new javax.swing.JPanel();
        jLabel38 = new javax.swing.JLabel();
        check_f5 = new javax.swing.JCheckBox();
        trans_cmb01 = new javax.swing.JComboBox();
        jLabel42 = new javax.swing.JLabel();
        trans_zip01 = new aks.swing.JTextFieldHankaku();
        jLabel43 = new javax.swing.JLabel();
        trans_tel01 = new aks.swing.JTextFieldHankaku();
        trans_address01 = new aks.swing.JTextField();
        check_f6 = new javax.swing.JCheckBox();
        jLabel36 = new javax.swing.JLabel();
        jLabel37 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        trans_car_no01 = new aks.swing.JTextFieldHankaku();
        trans_car_kind01 = new aks.swing.JComboBox();
        運搬業者_print_f1 = new javax.swing.JCheckBox();
        jLabel82 = new javax.swing.JLabel();
        trans_search_ = new aks.swing.JTextFieldHankaku();
        jButton2 = new javax.swing.JButton();
        jPanel24 = new javax.swing.JPanel();
        jLabel44 = new javax.swing.JLabel();
        jLabel45 = new javax.swing.JLabel();
        cont_office_name01 = new javax.swing.JComboBox();
        jLabel47 = new javax.swing.JLabel();
        cont_office_zip01 = new aks.swing.JTextFieldHankaku();
        jLabel49 = new javax.swing.JLabel();
        cont_office_tel01 = new aks.swing.JTextFieldHankaku();
        cont_office_address01 = new aks.swing.JTextField();
        jPanel16 = new javax.swing.JPanel();
        jLabel50 = new javax.swing.JLabel();
        jLabel51 = new javax.swing.JLabel();
        check_f12 = new javax.swing.JCheckBox();
        check_f9 = new javax.swing.JCheckBox();
        check_f10 = new javax.swing.JCheckBox();
        check_f13 = new javax.swing.JCheckBox();
        check_f11 = new javax.swing.JCheckBox();
        check_f14 = new javax.swing.JCheckBox();
        cmb中間処理 = new javax.swing.JComboBox();
        jPanel53 = new javax.swing.JPanel();
        jLabel39 = new javax.swing.JLabel();
        slash_f2 = new javax.swing.JCheckBox();
        jPanel52 = new javax.swing.JPanel();
        check_f7 = new javax.swing.JCheckBox();
        trans_cmb02 = new javax.swing.JComboBox();
        jLabel71 = new javax.swing.JLabel();
        trans_zip02 = new aks.swing.JTextFieldHankaku();
        jLabel79 = new javax.swing.JLabel();
        trans_tel02 = new aks.swing.JTextFieldHankaku();
        trans_address02 = new aks.swing.JTextField();
        check_f8 = new javax.swing.JCheckBox();
        jLabel80 = new javax.swing.JLabel();
        jLabel81 = new javax.swing.JLabel();
        jLabel22 = new javax.swing.JLabel();
        trans_car_no02 = new aks.swing.JTextFieldHankaku();
        trans_car_kind02 = new javax.swing.JComboBox();
        jLabel126 = new javax.swing.JLabel();
        trans_search_1 = new aks.swing.JTextFieldHankaku();
        jButton3 = new javax.swing.JButton();
        運搬業者_print_f2 = new javax.swing.JCheckBox();
        jPanel2 = new javax.swing.JPanel();
        jLabel91 = new javax.swing.JLabel();
        btn建設パターン2 = new javax.swing.JButton();
        btn建設パターン1 = new javax.swing.JButton();
        jPanel23 = new javax.swing.JPanel();

        setMaximumSize(new java.awt.Dimension(1250, 950));
        setMinimumSize(new java.awt.Dimension(1250, 900));
        setPreferredSize(new java.awt.Dimension(1250, 900));
        setLayout(new java.awt.GridBagLayout());

        jScrollPane2.setMaximumSize(new java.awt.Dimension(1280, 950));
        jScrollPane2.setMinimumSize(new java.awt.Dimension(1280, 700));
        jScrollPane2.setPreferredSize(new java.awt.Dimension(1280, 900));

        jPanel21.setMaximumSize(new java.awt.Dimension(1220, 910));
        jPanel21.setMinimumSize(new java.awt.Dimension(1220, 900));
        jPanel21.setPreferredSize(new java.awt.Dimension(1220, 900));
        jPanel21.setLayout(new java.awt.GridBagLayout());

        jPanel28.setBorder(javax.swing.BorderFactory.createEtchedBorder(new java.awt.Color(0, 102, 153), null));
        jPanel28.setMinimumSize(new java.awt.Dimension(90, 210));
        jPanel28.setPreferredSize(new java.awt.Dimension(90, 210));
        jPanel28.setLayout(new java.awt.GridBagLayout());

        goods_f36.setFont(new java.awt.Font("メイリオ", 0, 12)); // NOI18N
        goods_f36.setText("バラ");
        goods_f36.setMargin(new java.awt.Insets(2, 2, 2, 0));
        goods_f36.setMaximumSize(new java.awt.Dimension(80, 25));
        goods_f36.setMinimumSize(new java.awt.Dimension(80, 25));
        goods_f36.setPreferredSize(new java.awt.Dimension(80, 25));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        jPanel28.add(goods_f36, gridBagConstraints);

        goods_f37.setFont(new java.awt.Font("メイリオ", 0, 12)); // NOI18N
        goods_f37.setText("コンテナ");
        goods_f37.setMargin(new java.awt.Insets(2, 2, 2, 0));
        goods_f37.setMaximumSize(new java.awt.Dimension(80, 25));
        goods_f37.setMinimumSize(new java.awt.Dimension(80, 25));
        goods_f37.setPreferredSize(new java.awt.Dimension(80, 25));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        jPanel28.add(goods_f37, gridBagConstraints);

        goods_f38.setFont(new java.awt.Font("メイリオ", 0, 12)); // NOI18N
        goods_f38.setText("ドラム缶");
        goods_f38.setMargin(new java.awt.Insets(2, 2, 2, 0));
        goods_f38.setMaximumSize(new java.awt.Dimension(80, 25));
        goods_f38.setMinimumSize(new java.awt.Dimension(80, 25));
        goods_f38.setPreferredSize(new java.awt.Dimension(80, 25));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        jPanel28.add(goods_f38, gridBagConstraints);

        goods_f39.setFont(new java.awt.Font("メイリオ", 0, 12)); // NOI18N
        goods_f39.setText("袋");
        goods_f39.setMargin(new java.awt.Insets(2, 2, 2, 0));
        goods_f39.setMaximumSize(new java.awt.Dimension(80, 25));
        goods_f39.setMinimumSize(new java.awt.Dimension(80, 25));
        goods_f39.setPreferredSize(new java.awt.Dimension(80, 25));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        jPanel28.add(goods_f39, gridBagConstraints);

        jPanel12.setMaximumSize(new java.awt.Dimension(10, 10));
        jPanel12.setRequestFocusEnabled(false);

        javax.swing.GroupLayout jPanel12Layout = new javax.swing.GroupLayout(jPanel12);
        jPanel12.setLayout(jPanel12Layout);
        jPanel12Layout.setHorizontalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        jPanel12Layout.setVerticalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.weighty = 1.0;
        jPanel28.add(jPanel12, gridBagConstraints);

        jLabel9.setBackground(new java.awt.Color(0, 102, 153));
        jLabel9.setFont(new java.awt.Font("メイリオ", 1, 14)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(255, 255, 255));
        jLabel9.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel9.setText("荷　姿");
        jLabel9.setMaximumSize(new java.awt.Dimension(65, 30));
        jLabel9.setMinimumSize(new java.awt.Dimension(65, 30));
        jLabel9.setOpaque(true);
        jLabel9.setPreferredSize(new java.awt.Dimension(65, 30));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        jPanel28.add(jLabel9, gridBagConstraints);

        荷姿_print_f_.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        荷姿_print_f_.setText("<html>印字<br>なし</html>");
        荷姿_print_f_.setMaximumSize(new java.awt.Dimension(65, 45));
        荷姿_print_f_.setMinimumSize(new java.awt.Dimension(65, 45));
        荷姿_print_f_.setPreferredSize(new java.awt.Dimension(65, 45));
        荷姿_print_f_.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                荷姿_print_f_ItemStateChanged(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        jPanel28.add(荷姿_print_f_, gridBagConstraints);

        goods_f41.setFont(new java.awt.Font("メイリオ", 0, 12)); // NOI18N
        goods_f41.setText("専用容器");
        goods_f41.setMargin(new java.awt.Insets(2, 2, 2, 0));
        goods_f41.setMaximumSize(new java.awt.Dimension(80, 25));
        goods_f41.setMinimumSize(new java.awt.Dimension(80, 25));
        goods_f41.setPreferredSize(new java.awt.Dimension(80, 25));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        jPanel28.add(goods_f41, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        jPanel21.add(jPanel28, gridBagConstraints);

        jPanel19.setBorder(javax.swing.BorderFactory.createEtchedBorder(new java.awt.Color(0, 102, 153), null));
        jPanel19.setMinimumSize(new java.awt.Dimension(70, 210));
        jPanel19.setPreferredSize(new java.awt.Dimension(70, 210));
        jPanel19.setLayout(new java.awt.GridBagLayout());

        goods_f33.setFont(new java.awt.Font("メイリオ", 0, 12)); // NOI18N
        goods_f33.setText("固形状");
        goods_f33.setMargin(new java.awt.Insets(2, 2, 2, 0));
        goods_f33.setMinimumSize(new java.awt.Dimension(60, 25));
        goods_f33.setPreferredSize(new java.awt.Dimension(60, 25));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 2, 0, 0);
        jPanel19.add(goods_f33, gridBagConstraints);

        goods_f34.setFont(new java.awt.Font("メイリオ", 0, 12)); // NOI18N
        goods_f34.setText("泥状");
        goods_f34.setMargin(new java.awt.Insets(2, 2, 2, 0));
        goods_f34.setMinimumSize(new java.awt.Dimension(60, 25));
        goods_f34.setPreferredSize(new java.awt.Dimension(49, 25));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 2, 0, 0);
        jPanel19.add(goods_f34, gridBagConstraints);

        goods_f35.setFont(new java.awt.Font("メイリオ", 0, 12)); // NOI18N
        goods_f35.setText("液状");
        goods_f35.setMargin(new java.awt.Insets(2, 2, 2, 0));
        goods_f35.setMinimumSize(new java.awt.Dimension(60, 25));
        goods_f35.setPreferredSize(new java.awt.Dimension(60, 25));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 2, 0, 0);
        jPanel19.add(goods_f35, gridBagConstraints);

        jPanel6.setMaximumSize(new java.awt.Dimension(20, 30));
        jPanel6.setMinimumSize(new java.awt.Dimension(20, 30));
        jPanel6.setPreferredSize(new java.awt.Dimension(20, 30));

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 20, Short.MAX_VALUE)
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 30, Short.MAX_VALUE)
        );

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.weighty = 1.0;
        jPanel19.add(jPanel6, gridBagConstraints);

        jLabel8.setBackground(new java.awt.Color(0, 102, 153));
        jLabel8.setFont(new java.awt.Font("メイリオ", 1, 14)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(255, 255, 255));
        jLabel8.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel8.setText("形　状");
        jLabel8.setMaximumSize(new java.awt.Dimension(65, 30));
        jLabel8.setMinimumSize(new java.awt.Dimension(65, 30));
        jLabel8.setOpaque(true);
        jLabel8.setPreferredSize(new java.awt.Dimension(65, 30));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        jPanel19.add(jLabel8, gridBagConstraints);

        形状_print_f_.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        形状_print_f_.setText("<html>印字<br>なし</html>");
        形状_print_f_.setMaximumSize(new java.awt.Dimension(65, 45));
        形状_print_f_.setMinimumSize(new java.awt.Dimension(65, 45));
        形状_print_f_.setPreferredSize(new java.awt.Dimension(65, 45));
        形状_print_f_.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                形状_print_f_ItemStateChanged(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        jPanel19.add(形状_print_f_, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        jPanel21.add(jPanel19, gridBagConstraints);

        jPanel17.setBackground(new java.awt.Color(0, 102, 153));
        jPanel17.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel17.setMinimumSize(new java.awt.Dimension(1040, 35));
        jPanel17.setPreferredSize(new java.awt.Dimension(1040, 35));
        jPanel17.setLayout(new java.awt.GridBagLayout());

        goods_f01.setBackground(new java.awt.Color(0, 102, 153));
        goods_f01.setFont(new java.awt.Font("メイリオ", 0, 12)); // NOI18N
        goods_f01.setForeground(new java.awt.Color(255, 255, 255));
        goods_f01.setText("t");
        goods_f01.setMaximumSize(new java.awt.Dimension(45, 25));
        goods_f01.setMinimumSize(new java.awt.Dimension(45, 25));
        goods_f01.setPreferredSize(new java.awt.Dimension(45, 25));
        goods_f01.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                goods_f01ItemStateChanged(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        jPanel17.add(goods_f01, gridBagConstraints);

        goods_f02.setBackground(new java.awt.Color(0, 102, 153));
        goods_f02.setFont(new java.awt.Font("メイリオ", 0, 12)); // NOI18N
        goods_f02.setForeground(new java.awt.Color(255, 255, 255));
        goods_f02.setText("kg");
        goods_f02.setMaximumSize(new java.awt.Dimension(45, 25));
        goods_f02.setMinimumSize(new java.awt.Dimension(45, 25));
        goods_f02.setPreferredSize(new java.awt.Dimension(45, 25));
        goods_f02.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                goods_f02ItemStateChanged(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        jPanel17.add(goods_f02, gridBagConstraints);

        goods_f03.setBackground(new java.awt.Color(0, 102, 153));
        goods_f03.setFont(new java.awt.Font("メイリオ", 0, 12)); // NOI18N
        goods_f03.setForeground(new java.awt.Color(255, 255, 255));
        goods_f03.setText("m3");
        goods_f03.setMaximumSize(new java.awt.Dimension(45, 25));
        goods_f03.setMinimumSize(new java.awt.Dimension(45, 25));
        goods_f03.setPreferredSize(new java.awt.Dimension(45, 25));
        goods_f03.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                goods_f03ItemStateChanged(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        jPanel17.add(goods_f03, gridBagConstraints);

        goods_f04.setBackground(new java.awt.Color(0, 102, 153));
        goods_f04.setFont(new java.awt.Font("メイリオ", 0, 12)); // NOI18N
        goods_f04.setForeground(new java.awt.Color(255, 255, 255));
        goods_f04.setText("ℓ");
        goods_f04.setMaximumSize(new java.awt.Dimension(45, 25));
        goods_f04.setMinimumSize(new java.awt.Dimension(45, 25));
        goods_f04.setPreferredSize(new java.awt.Dimension(45, 25));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        jPanel17.add(goods_f04, gridBagConstraints);

        jLabel14.setFont(new java.awt.Font("メイリオ", 0, 12)); // NOI18N
        jLabel14.setForeground(new java.awt.Color(255, 255, 255));
        jLabel14.setText("）");
        jLabel14.setMinimumSize(new java.awt.Dimension(20, 25));
        jLabel14.setPreferredSize(new java.awt.Dimension(20, 25));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        jPanel17.add(jLabel14, gridBagConstraints);

        jPanel11.setBackground(new java.awt.Color(0, 102, 153));
        jPanel11.setMaximumSize(new java.awt.Dimension(50, 25));
        jPanel11.setMinimumSize(new java.awt.Dimension(50, 25));
        jPanel11.setPreferredSize(new java.awt.Dimension(50, 25));

        javax.swing.GroupLayout jPanel11Layout = new javax.swing.GroupLayout(jPanel11);
        jPanel11.setLayout(jPanel11Layout);
        jPanel11Layout.setHorizontalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 50, Short.MAX_VALUE)
        );
        jPanel11Layout.setVerticalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 25, Short.MAX_VALUE)
        );

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 7;
        gridBagConstraints.gridy = 0;
        jPanel17.add(jPanel11, gridBagConstraints);

        jLabel1.setBackground(new java.awt.Color(0, 102, 153));
        jLabel1.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("産業廃棄物の種類　(単位：");
        jLabel1.setMaximumSize(new java.awt.Dimension(180, 25));
        jLabel1.setMinimumSize(new java.awt.Dimension(180, 25));
        jLabel1.setPreferredSize(new java.awt.Dimension(180, 25));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(0, 20, 0, 0);
        jPanel17.add(jLabel1, gridBagConstraints);

        単位_print_f_.setBackground(new java.awt.Color(0, 102, 153));
        単位_print_f_.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        単位_print_f_.setForeground(new java.awt.Color(255, 255, 255));
        単位_print_f_.setText("印字なし　単位切替時には重量を計算し、各項目に手入力してください。");
        単位_print_f_.setMaximumSize(new java.awt.Dimension(200, 30));
        単位_print_f_.setMinimumSize(new java.awt.Dimension(200, 30));
        単位_print_f_.setPreferredSize(new java.awt.Dimension(200, 30));
        単位_print_f_.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                単位_print_f_ItemStateChanged(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 6;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        jPanel17.add(単位_print_f_, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        jPanel21.add(jPanel17, gridBagConstraints);

        jPanel10.setBorder(javax.swing.BorderFactory.createEtchedBorder(new java.awt.Color(153, 153, 255), null));
        jPanel10.setMaximumSize(new java.awt.Dimension(1200, 130));
        jPanel10.setMinimumSize(new java.awt.Dimension(1200, 130));
        jPanel10.setPreferredSize(new java.awt.Dimension(1200, 130));
        jPanel10.setLayout(new java.awt.GridBagLayout());

        jPanel25.setBorder(javax.swing.BorderFactory.createEtchedBorder(new java.awt.Color(0, 102, 153), null));
        jPanel25.setFont(new java.awt.Font("MS UI Gothic", 1, 12)); // NOI18N
        jPanel25.setMaximumSize(new java.awt.Dimension(400, 140));
        jPanel25.setMinimumSize(new java.awt.Dimension(400, 140));
        jPanel25.setPreferredSize(new java.awt.Dimension(400, 140));
        jPanel25.setLayout(new java.awt.GridBagLayout());

        cont_cmb01.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        cont_cmb01.setMinimumSize(new java.awt.Dimension(170, 27));
        cont_cmb01.setPreferredSize(new java.awt.Dimension(170, 27));
        cont_cmb01.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cont_cmb01ItemStateChanged(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 0, 2, 5);
        jPanel25.add(cont_cmb01, gridBagConstraints);

        jLabel56.setFont(new java.awt.Font("メイリオ", 0, 12)); // NOI18N
        jLabel56.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel56.setText("所在地");
        jLabel56.setMaximumSize(new java.awt.Dimension(90, 20));
        jLabel56.setMinimumSize(new java.awt.Dimension(90, 20));
        jLabel56.setPreferredSize(new java.awt.Dimension(90, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 5);
        jPanel25.add(jLabel56, gridBagConstraints);

        cont_zip01.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        cont_zip01.setMaximumSize(new java.awt.Dimension(90, 25));
        cont_zip01.setMinimumSize(new java.awt.Dimension(90, 25));
        cont_zip01.setPreferredSize(new java.awt.Dimension(90, 25));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        jPanel25.add(cont_zip01, gridBagConstraints);

        jLabel57.setFont(new java.awt.Font("メイリオ", 0, 12)); // NOI18N
        jLabel57.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel57.setText("電話番号");
        jLabel57.setMaximumSize(new java.awt.Dimension(85, 20));
        jLabel57.setMinimumSize(new java.awt.Dimension(90, 20));
        jLabel57.setPreferredSize(new java.awt.Dimension(90, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        jPanel25.add(jLabel57, gridBagConstraints);

        cont_tel01.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        cont_tel01.setMaximumSize(new java.awt.Dimension(140, 25));
        cont_tel01.setMinimumSize(new java.awt.Dimension(140, 25));
        cont_tel01.setPreferredSize(new java.awt.Dimension(140, 25));
        cont_tel01.setRequestFocusEnabled(false);
        cont_tel01.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cont_tel01ActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 0.25;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 5);
        jPanel25.add(cont_tel01, gridBagConstraints);

        cont_address01.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        cont_address01.setMinimumSize(new java.awt.Dimension(380, 25));
        cont_address01.setPreferredSize(new java.awt.Dimension(380, 25));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(2, 5, 2, 5);
        jPanel25.add(cont_address01, gridBagConstraints);

        jLabel127.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        jLabel127.setText("ｺｰﾄﾞ");
        jLabel127.setMaximumSize(new java.awt.Dimension(30, 20));
        jLabel127.setMinimumSize(new java.awt.Dimension(30, 20));
        jLabel127.setPreferredSize(new java.awt.Dimension(30, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 2, 0, 2);
        jPanel25.add(jLabel127, gridBagConstraints);

        cont_search_.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        cont_search_.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        cont_search_.setMaximumSize(new java.awt.Dimension(60, 25));
        cont_search_.setMinimumSize(new java.awt.Dimension(60, 25));
        cont_search_.setPreferredSize(new java.awt.Dimension(60, 25));
        cont_search_.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                cont_search_FocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                cont_search_FocusLost(evt);
            }
        });
        cont_search_.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                cont_search_KeyPressed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        jPanel25.add(cont_search_, gridBagConstraints);

        jButton4.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        jButton4.setText("ｶﾅ検索");
        jButton4.setFocusable(false);
        jButton4.setMargin(new java.awt.Insets(0, 0, 0, 0));
        jButton4.setMaximumSize(new java.awt.Dimension(70, 25));
        jButton4.setMinimumSize(new java.awt.Dimension(70, 25));
        jButton4.setPreferredSize(new java.awt.Dimension(70, 25));
        jButton4.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jButton4MouseClicked(evt);
            }
        });
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 0, 2, 2);
        jPanel25.add(jButton4, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        jPanel10.add(jPanel25, gridBagConstraints);

        jPanel27.setBorder(javax.swing.BorderFactory.createEtchedBorder(new java.awt.Color(0, 102, 153), null));
        jPanel27.setMaximumSize(new java.awt.Dimension(400, 95));
        jPanel27.setMinimumSize(new java.awt.Dimension(400, 95));
        jPanel27.setPreferredSize(new java.awt.Dimension(400, 95));
        jPanel27.setLayout(new java.awt.GridBagLayout());

        jLabel84.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        jLabel84.setText("追加記載事項");
        jLabel84.setMaximumSize(new java.awt.Dimension(100, 25));
        jLabel84.setMinimumSize(new java.awt.Dimension(100, 25));
        jLabel84.setPreferredSize(new java.awt.Dimension(100, 25));
        jLabel84.setRequestFocusEnabled(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 0);
        jPanel27.add(jLabel84, gridBagConstraints);

        jScrollPane1.setMaximumSize(new java.awt.Dimension(250, 95));
        jScrollPane1.setMinimumSize(new java.awt.Dimension(250, 95));
        jScrollPane1.setPreferredSize(new java.awt.Dimension(250, 95));

        txt備考.setColumns(20);
        txt備考.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        txt備考.setRows(5);
        txt備考.setMaximumSize(new java.awt.Dimension(150, 50));
        txt備考.setMinimumSize(new java.awt.Dimension(150, 50));
        jScrollPane1.setViewportView(txt備考);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 2, 2, 5);
        jPanel27.add(jScrollPane1, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        jPanel10.add(jPanel27, gridBagConstraints);

        jPanel55.setBorder(javax.swing.BorderFactory.createEtchedBorder(new java.awt.Color(0, 102, 153), null));
        jPanel55.setFont(new java.awt.Font("MS UI Gothic", 1, 12)); // NOI18N
        jPanel55.setMaximumSize(new java.awt.Dimension(400, 140));
        jPanel55.setMinimumSize(new java.awt.Dimension(400, 140));
        jPanel55.setPreferredSize(new java.awt.Dimension(400, 140));
        jPanel55.setLayout(new java.awt.GridBagLayout());

        jLabel113.setFont(new java.awt.Font("メイリオ", 0, 12)); // NOI18N
        jLabel113.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel113.setText("所在地");
        jLabel113.setMinimumSize(new java.awt.Dimension(90, 20));
        jLabel113.setPreferredSize(new java.awt.Dimension(90, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        jPanel55.add(jLabel113, gridBagConstraints);

        cont_zip02.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        cont_zip02.setMinimumSize(new java.awt.Dimension(90, 25));
        cont_zip02.setPreferredSize(new java.awt.Dimension(90, 25));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.insets = new java.awt.Insets(2, 0, 2, 0);
        jPanel55.add(cont_zip02, gridBagConstraints);

        jLabel114.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        jLabel114.setText(" 　電話番号");
        jLabel114.setMinimumSize(new java.awt.Dimension(90, 20));
        jLabel114.setPreferredSize(new java.awt.Dimension(90, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        jPanel55.add(jLabel114, gridBagConstraints);

        cont_tel02.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        cont_tel02.setMinimumSize(new java.awt.Dimension(35, 25));
        cont_tel02.setPreferredSize(new java.awt.Dimension(35, 25));
        cont_tel02.setRequestFocusEnabled(false);
        cont_tel02.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cont_tel02ActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 0, 2, 5);
        jPanel55.add(cont_tel02, gridBagConstraints);

        cont_address02.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        cont_address02.setMinimumSize(new java.awt.Dimension(380, 25));
        cont_address02.setPreferredSize(new java.awt.Dimension(380, 25));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(2, 0, 2, 5);
        jPanel55.add(cont_address02, gridBagConstraints);

        jLabel128.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        jLabel128.setText("ｺｰﾄﾞ");
        jLabel128.setMaximumSize(new java.awt.Dimension(30, 20));
        jLabel128.setMinimumSize(new java.awt.Dimension(30, 20));
        jLabel128.setPreferredSize(new java.awt.Dimension(30, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 2, 0, 2);
        jPanel55.add(jLabel128, gridBagConstraints);

        cont_search_1.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        cont_search_1.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        cont_search_1.setMaximumSize(new java.awt.Dimension(60, 25));
        cont_search_1.setMinimumSize(new java.awt.Dimension(60, 25));
        cont_search_1.setPreferredSize(new java.awt.Dimension(60, 25));
        cont_search_1.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                cont_search_1FocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                cont_search_1FocusLost(evt);
            }
        });
        cont_search_1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                cont_search_1KeyPressed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        jPanel55.add(cont_search_1, gridBagConstraints);

        保管場所_cmb.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        保管場所_cmb.setMinimumSize(new java.awt.Dimension(170, 27));
        保管場所_cmb.setPreferredSize(new java.awt.Dimension(170, 27));
        保管場所_cmb.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                保管場所_cmbItemStateChanged(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 5);
        jPanel55.add(保管場所_cmb, gridBagConstraints);

        jLabel21.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        jLabel21.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel21.setText("有価物拾集　");
        jLabel21.setMinimumSize(new java.awt.Dimension(100, 25));
        jLabel21.setPreferredSize(new java.awt.Dimension(100, 25));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        jPanel55.add(jLabel21, gridBagConstraints);

        check_f15.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        check_f15.setText("有");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        jPanel55.add(check_f15, gridBagConstraints);

        check_f16.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        check_f16.setText("無");
        check_f16.setMinimumSize(new java.awt.Dimension(40, 25));
        check_f16.setPreferredSize(new java.awt.Dimension(40, 25));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        jPanel55.add(check_f16, gridBagConstraints);

        trans1_weight.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        trans1_weight.setMaximumSize(new java.awt.Dimension(95, 25));
        trans1_weight.setMinimumSize(new java.awt.Dimension(95, 25));
        trans1_weight.setPreferredSize(new java.awt.Dimension(95, 25));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 3;
        jPanel55.add(trans1_weight, gridBagConstraints);

        check_f18.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        check_f18.setText("ｔ");
        check_f18.setMinimumSize(new java.awt.Dimension(40, 25));
        check_f18.setPreferredSize(new java.awt.Dimension(40, 25));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        jPanel55.add(check_f18, gridBagConstraints);

        check_f17.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        check_f17.setText("m3");
        check_f17.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        check_f17.setMaximumSize(new java.awt.Dimension(55, 25));
        check_f17.setMinimumSize(new java.awt.Dimension(55, 25));
        check_f17.setPreferredSize(new java.awt.Dimension(55, 25));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 3;
        jPanel55.add(check_f17, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        jPanel10.add(jPanel55, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        jPanel21.add(jPanel10, gridBagConstraints);

        jPanel4.setBorder(javax.swing.BorderFactory.createEtchedBorder(new java.awt.Color(0, 102, 153), null));
        jPanel4.setMinimumSize(new java.awt.Dimension(1200, 40));
        jPanel4.setPreferredSize(new java.awt.Dimension(1200, 40));
        jPanel4.setLayout(new java.awt.GridBagLayout());

        jLabel46.setBackground(new java.awt.Color(0, 102, 153));
        jLabel46.setFont(new java.awt.Font("メイリオ", 1, 14)); // NOI18N
        jLabel46.setForeground(new java.awt.Color(255, 255, 255));
        jLabel46.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel46.setText("   最終処分〔埋立処分、再生等〕の場所〔予定〕");
        jLabel46.setMaximumSize(new java.awt.Dimension(350, 18));
        jLabel46.setMinimumSize(new java.awt.Dimension(350, 18));
        jLabel46.setOpaque(true);
        jLabel46.setPreferredSize(new java.awt.Dimension(350, 22));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        jPanel4.add(jLabel46, gridBagConstraints);

        check_f3.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        check_f3.setText("委託契約書記載のとおり  \n");
        check_f3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                check_f3ActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        jPanel4.add(check_f3, gridBagConstraints);

        check_f4.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        check_f4.setText("当欄記載のとおり");
        check_f4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                check_f4ActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        jPanel4.add(check_f4, gridBagConstraints);

        jLabel99.setBackground(new java.awt.Color(0, 102, 153));
        jLabel99.setFont(new java.awt.Font("メイリオ", 1, 14)); // NOI18N
        jLabel99.setForeground(new java.awt.Color(255, 255, 255));
        jLabel99.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel99.setText("所在地/名称");
        jLabel99.setMinimumSize(new java.awt.Dimension(90, 18));
        jLabel99.setOpaque(true);
        jLabel99.setPreferredSize(new java.awt.Dimension(90, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        jPanel4.add(jLabel99, gridBagConstraints);

        jPanel33.setMaximumSize(new java.awt.Dimension(20, 10));
        jPanel33.setMinimumSize(new java.awt.Dimension(20, 10));
        jPanel33.setPreferredSize(new java.awt.Dimension(20, 10));

        javax.swing.GroupLayout jPanel33Layout = new javax.swing.GroupLayout(jPanel33);
        jPanel33.setLayout(jPanel33Layout);
        jPanel33Layout.setHorizontalGroup(
            jPanel33Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 20, Short.MAX_VALUE)
        );
        jPanel33Layout.setVerticalGroup(
            jPanel33Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 10, Short.MAX_VALUE)
        );

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        jPanel4.add(jPanel33, gridBagConstraints);

        jPanel46.setMaximumSize(new java.awt.Dimension(20, 20));
        jPanel46.setMinimumSize(new java.awt.Dimension(20, 20));
        jPanel46.setPreferredSize(new java.awt.Dimension(20, 20));

        javax.swing.GroupLayout jPanel46Layout = new javax.swing.GroupLayout(jPanel46);
        jPanel46.setLayout(jPanel46Layout);
        jPanel46Layout.setHorizontalGroup(
            jPanel46Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 20, Short.MAX_VALUE)
        );
        jPanel46Layout.setVerticalGroup(
            jPanel46Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 20, Short.MAX_VALUE)
        );

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        jPanel4.add(jPanel46, gridBagConstraints);

        jlbl_final_name_.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        jlbl_final_name_.setText("株式会社○○");
        jlbl_final_name_.setMaximumSize(new java.awt.Dimension(350, 20));
        jlbl_final_name_.setMinimumSize(new java.awt.Dimension(350, 20));
        jlbl_final_name_.setPreferredSize(new java.awt.Dimension(350, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.weightx = 1.0;
        jPanel4.add(jlbl_final_name_, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        jPanel21.add(jPanel4, gridBagConstraints);

        jPanel1.setBorder(javax.swing.BorderFactory.createEtchedBorder(new java.awt.Color(0, 102, 153), null));
        jPanel1.setMinimumSize(new java.awt.Dimension(1200, 35));
        jPanel1.setPreferredSize(new java.awt.Dimension(1200, 35));
        jPanel1.setLayout(new java.awt.GridBagLayout());

        jLabel78.setBackground(new java.awt.Color(0, 102, 153));
        jLabel78.setFont(new java.awt.Font("メイリオ", 1, 14)); // NOI18N
        jLabel78.setForeground(new java.awt.Color(255, 255, 255));
        jLabel78.setText(" 最終処分を行った場所 ");
        jLabel78.setMaximumSize(new java.awt.Dimension(150, 30));
        jLabel78.setMinimumSize(new java.awt.Dimension(150, 30));
        jLabel78.setOpaque(true);
        jLabel78.setPreferredSize(new java.awt.Dimension(150, 30));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        jPanel1.add(jLabel78, gridBagConstraints);

        last_cont_cmb.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        last_cont_cmb.setMaximumSize(new java.awt.Dimension(550, 27));
        last_cont_cmb.setMinimumSize(new java.awt.Dimension(550, 27));
        last_cont_cmb.setName(""); // NOI18N
        last_cont_cmb.setPreferredSize(new java.awt.Dimension(550, 27));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 2, 0, 2);
        jPanel1.add(last_cont_cmb, gridBagConstraints);

        print_btn_kensetu.setFont(new java.awt.Font("メイリオ", 0, 18)); // NOI18N
        print_btn_kensetu.setText("印　字");
        print_btn_kensetu.setMaximumSize(new java.awt.Dimension(100, 30));
        print_btn_kensetu.setMinimumSize(new java.awt.Dimension(100, 30));
        print_btn_kensetu.setPreferredSize(new java.awt.Dimension(100, 30));
        print_btn_kensetu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                print_btn_kensetuActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 8;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        jPanel1.add(print_btn_kensetu, gridBagConstraints);

        quit_btn_kensetu.setFont(new java.awt.Font("メイリオ", 0, 18)); // NOI18N
        quit_btn_kensetu.setText("閉じる");
        quit_btn_kensetu.setMaximumSize(new java.awt.Dimension(100, 30));
        quit_btn_kensetu.setMinimumSize(new java.awt.Dimension(100, 30));
        quit_btn_kensetu.setPreferredSize(new java.awt.Dimension(100, 30));
        quit_btn_kensetu.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                quit_btn_kensetuMousePressed(evt);
            }
        });
        quit_btn_kensetu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                quit_btn_kensetuActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 11;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        jPanel1.add(quit_btn_kensetu, gridBagConstraints);

        rejist_btn_kensetu.setFont(new java.awt.Font("メイリオ", 0, 18)); // NOI18N
        rejist_btn_kensetu.setText("登　録");
        rejist_btn_kensetu.setMaximumSize(new java.awt.Dimension(100, 30));
        rejist_btn_kensetu.setMinimumSize(new java.awt.Dimension(100, 30));
        rejist_btn_kensetu.setPreferredSize(new java.awt.Dimension(100, 30));
        rejist_btn_kensetu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rejist_btn_kensetuActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 10;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        jPanel1.add(rejist_btn_kensetu, gridBagConstraints);

        jLabel4.setFont(new java.awt.Font("メイリオ", 0, 18)); // NOI18N
        jLabel4.setText("部数");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 2, 0, 2);
        jPanel1.add(jLabel4, gridBagConstraints);

        pring_num_.setFont(new java.awt.Font("メイリオ", 0, 18)); // NOI18N
        pring_num_.setMaximumSize(new java.awt.Dimension(50, 28));
        pring_num_.setMinimumSize(new java.awt.Dimension(50, 28));
        pring_num_.setPreferredSize(new java.awt.Dimension(50, 28));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 2, 0, 2);
        jPanel1.add(pring_num_, gridBagConstraints);

        画面初期化_btn_.setFont(new java.awt.Font("メイリオ", 0, 18)); // NOI18N
        画面初期化_btn_.setText("クリア");
        画面初期化_btn_.setMaximumSize(new java.awt.Dimension(100, 30));
        画面初期化_btn_.setMinimumSize(new java.awt.Dimension(100, 30));
        画面初期化_btn_.setPreferredSize(new java.awt.Dimension(100, 30));
        画面初期化_btn_.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                画面初期化_btn_MousePressed(evt);
            }
        });
        画面初期化_btn_.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                画面初期化_btn_ActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 6;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        jPanel1.add(画面初期化_btn_, gridBagConstraints);

        jPanel9.setPreferredSize(new java.awt.Dimension(5, 5));

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 7;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.weightx = 1.0;
        jPanel1.add(jPanel9, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        jPanel21.add(jPanel1, gridBagConstraints);

        jPanel14.setMinimumSize(new java.awt.Dimension(1200, 125));
        jPanel14.setPreferredSize(new java.awt.Dimension(1200, 125));
        jPanel14.setLayout(new java.awt.GridBagLayout());

        jPanel41.setBackground(new java.awt.Color(0, 102, 153));
        jPanel41.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel41.setMinimumSize(new java.awt.Dimension(69, 95));
        jPanel41.setPreferredSize(new java.awt.Dimension(69, 95));
        jPanel41.setLayout(new java.awt.GridBagLayout());

        jLabel120.setFont(new java.awt.Font("メイリオ", 1, 12)); // NOI18N
        jLabel120.setForeground(new java.awt.Color(255, 255, 255));
        jLabel120.setText("事");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        jPanel41.add(jLabel120, gridBagConstraints);

        jLabel122.setFont(new java.awt.Font("メイリオ", 1, 12)); // NOI18N
        jLabel122.setForeground(new java.awt.Color(255, 255, 255));
        jLabel122.setText("排");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        jPanel41.add(jLabel122, gridBagConstraints);

        jLabel133.setFont(new java.awt.Font("メイリオ", 1, 12)); // NOI18N
        jLabel133.setForeground(new java.awt.Color(255, 255, 255));
        jLabel133.setText("者");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        jPanel41.add(jLabel133, gridBagConstraints);

        jLabel134.setFont(new java.awt.Font("メイリオ", 1, 12)); // NOI18N
        jLabel134.setForeground(new java.awt.Color(255, 255, 255));
        jLabel134.setText("業");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        jPanel41.add(jLabel134, gridBagConstraints);

        jLabel135.setFont(new java.awt.Font("メイリオ", 1, 12)); // NOI18N
        jLabel135.setForeground(new java.awt.Color(255, 255, 255));
        jLabel135.setText("出");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        jPanel41.add(jLabel135, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        jPanel14.add(jPanel41, gridBagConstraints);

        jPanel42.setBorder(javax.swing.BorderFactory.createEtchedBorder(new java.awt.Color(0, 102, 153), null));
        jPanel42.setMinimumSize(new java.awt.Dimension(520, 95));
        jPanel42.setPreferredSize(new java.awt.Dimension(520, 95));
        jPanel42.setLayout(new java.awt.GridBagLayout());

        排出事業者_print_f.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        排出事業者_print_f.setText("印字なし　");
        排出事業者_print_f.setMaximumSize(new java.awt.Dimension(130, 25));
        排出事業者_print_f.setMinimumSize(new java.awt.Dimension(130, 25));
        排出事業者_print_f.setPreferredSize(new java.awt.Dimension(130, 25));
        排出事業者_print_f.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                排出事業者_print_fItemStateChanged(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        jPanel42.add(排出事業者_print_f, gridBagConstraints);

        jLabel54.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        jLabel54.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel54.setText("名称");
        jLabel54.setMaximumSize(new java.awt.Dimension(35, 20));
        jLabel54.setMinimumSize(new java.awt.Dimension(35, 20));
        jLabel54.setPreferredSize(new java.awt.Dimension(35, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 0, 2);
        jPanel42.add(jLabel54, gridBagConstraints);

        jLabel63.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        jLabel63.setText(" 電話番号 ");
        jLabel63.setMinimumSize(new java.awt.Dimension(80, 20));
        jLabel63.setPreferredSize(new java.awt.Dimension(80, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        jPanel42.add(jLabel63, gridBagConstraints);

        ent_zip01.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        ent_zip01.setText("123－4567");
        ent_zip01.setMaximumSize(new java.awt.Dimension(85, 25));
        ent_zip01.setMinimumSize(new java.awt.Dimension(85, 25));
        ent_zip01.setPreferredSize(new java.awt.Dimension(85, 25));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 0;
        jPanel42.add(ent_zip01, gridBagConstraints);

        ent_tel01.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        ent_tel01.setText("0178－99－9999");
        ent_tel01.setMinimumSize(new java.awt.Dimension(150, 25));
        ent_tel01.setPreferredSize(new java.awt.Dimension(150, 25));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 6;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(2, 0, 2, 0);
        jPanel42.add(ent_tel01, gridBagConstraints);

        ent_address01.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        ent_address01.setText("青森県八戸市売市3丁目7-26 ");
        ent_address01.setMaximumSize(new java.awt.Dimension(345, 27));
        ent_address01.setMinimumSize(new java.awt.Dimension(345, 27));
        ent_address01.setPreferredSize(new java.awt.Dimension(345, 27));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        jPanel42.add(ent_address01, gridBagConstraints);

        jLabel65.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        jLabel65.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel65.setText("ｺｰﾄﾞ");
        jLabel65.setMaximumSize(new java.awt.Dimension(35, 20));
        jLabel65.setMinimumSize(new java.awt.Dimension(35, 20));
        jLabel65.setPreferredSize(new java.awt.Dimension(35, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 2);
        jPanel42.add(jLabel65, gridBagConstraints);

        jLabel74.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        jLabel74.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel74.setText("〒");
        jLabel74.setMinimumSize(new java.awt.Dimension(30, 20));
        jLabel74.setPreferredSize(new java.awt.Dimension(30, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        jPanel42.add(jLabel74, gridBagConstraints);

        ent_search_.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        ent_search_.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        ent_search_.setMaximumSize(new java.awt.Dimension(70, 25));
        ent_search_.setMinimumSize(new java.awt.Dimension(70, 25));
        ent_search_.setPreferredSize(new java.awt.Dimension(70, 25));
        ent_search_.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                ent_search_FocusGained(evt);
            }
        });
        ent_search_.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                ent_search_KeyPressed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        jPanel42.add(ent_search_, gridBagConstraints);

        jLabel110.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        jLabel110.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel110.setText("住所");
        jLabel110.setMaximumSize(new java.awt.Dimension(35, 20));
        jLabel110.setMinimumSize(new java.awt.Dimension(35, 20));
        jLabel110.setPreferredSize(new java.awt.Dimension(35, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 2);
        jPanel42.add(jLabel110, gridBagConstraints);

        jButton1.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        jButton1.setText("ｶﾅ検索");
        jButton1.setFocusable(false);
        jButton1.setMargin(new java.awt.Insets(0, 0, 0, 0));
        jButton1.setMaximumSize(new java.awt.Dimension(70, 25));
        jButton1.setMinimumSize(new java.awt.Dimension(70, 25));
        jButton1.setPreferredSize(new java.awt.Dimension(70, 25));
        jButton1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jButton1MouseClicked(evt);
            }
        });
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 0, 2, 2);
        jPanel42.add(jButton1, gridBagConstraints);

        ent_name.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        ent_name.setMaximumSize(new java.awt.Dimension(345, 27));
        ent_name.setMinimumSize(new java.awt.Dimension(345, 27));
        ent_name.setPreferredSize(new java.awt.Dimension(345, 237));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 4;
        jPanel42.add(ent_name, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        jPanel14.add(jPanel42, gridBagConstraints);

        jPanel43.setBorder(javax.swing.BorderFactory.createEtchedBorder(new java.awt.Color(0, 102, 153), null));
        jPanel43.setMaximumSize(new java.awt.Dimension(60, 95));
        jPanel43.setMinimumSize(new java.awt.Dimension(60, 95));
        jPanel43.setPreferredSize(new java.awt.Dimension(60, 95));
        jPanel43.setLayout(new java.awt.GridBagLayout());

        jLabel66.setFont(new java.awt.Font("メイリオ", 0, 12)); // NOI18N
        jLabel66.setText("業");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 0, 5, 0);
        jPanel43.add(jLabel66, gridBagConstraints);

        jLabel67.setFont(new java.awt.Font("メイリオ", 0, 12)); // NOI18N
        jLabel67.setText("場");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        jPanel43.add(jLabel67, gridBagConstraints);

        jLabel68.setFont(new java.awt.Font("メイリオ", 0, 12)); // NOI18N
        jLabel68.setText("事");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        jPanel43.add(jLabel68, gridBagConstraints);

        jLabel69.setFont(new java.awt.Font("メイリオ", 0, 12)); // NOI18N
        jLabel69.setText("業");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 0, 0);
        jPanel43.add(jLabel69, gridBagConstraints);

        jLabel70.setFont(new java.awt.Font("メイリオ", 0, 12)); // NOI18N
        jLabel70.setText("作");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 0);
        jPanel43.add(jLabel70, gridBagConstraints);

        jLabel88.setFont(new java.awt.Font("メイリオ", 0, 12)); // NOI18N
        jLabel88.setText("場");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 5, 0);
        jPanel43.add(jLabel88, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        jPanel14.add(jPanel43, gridBagConstraints);

        jPanel44.setBorder(javax.swing.BorderFactory.createEtchedBorder(new java.awt.Color(0, 102, 153), null));
        jPanel44.setMaximumSize(new java.awt.Dimension(550, 95));
        jPanel44.setMinimumSize(new java.awt.Dimension(550, 95));
        jPanel44.setPreferredSize(new java.awt.Dimension(550, 95));
        jPanel44.setLayout(new java.awt.GridBagLayout());

        事業場_print_f.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        事業場_print_f.setText("印字なし");
        事業場_print_f.setMaximumSize(new java.awt.Dimension(80, 20));
        事業場_print_f.setMinimumSize(new java.awt.Dimension(80, 20));
        事業場_print_f.setPreferredSize(new java.awt.Dimension(80, 20));
        事業場_print_f.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                事業場_print_fItemStateChanged(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        jPanel44.add(事業場_print_f, gridBagConstraints);

        jLabel2.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel2.setText("名称");
        jLabel2.setMaximumSize(new java.awt.Dimension(50, 20));
        jLabel2.setMinimumSize(new java.awt.Dimension(50, 20));
        jLabel2.setPreferredSize(new java.awt.Dimension(50, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 2);
        jPanel44.add(jLabel2, gridBagConstraints);

        jLabel89.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        jLabel89.setText(" 電話番号 ");
        jLabel89.setMinimumSize(new java.awt.Dimension(80, 20));
        jLabel89.setPreferredSize(new java.awt.Dimension(80, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        jPanel44.add(jLabel89, gridBagConstraints);

        office_zip01.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        office_zip01.setMaximumSize(new java.awt.Dimension(85, 25));
        office_zip01.setMinimumSize(new java.awt.Dimension(85, 25));
        office_zip01.setPreferredSize(new java.awt.Dimension(85, 25));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(1, 0, 2, 0);
        jPanel44.add(office_zip01, gridBagConstraints);

        office_tel01.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        office_tel01.setMaximumSize(new java.awt.Dimension(130, 25));
        office_tel01.setMinimumSize(new java.awt.Dimension(130, 25));
        office_tel01.setPreferredSize(new java.awt.Dimension(130, 25));
        office_tel01.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                office_tel01ActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 6;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 0, 2, 0);
        jPanel44.add(office_tel01, gridBagConstraints);

        office_address01.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        office_address01.setMinimumSize(new java.awt.Dimension(300, 27));
        office_address01.setPreferredSize(new java.awt.Dimension(300, 25));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 0, 2, 0);
        jPanel44.add(office_address01, gridBagConstraints);

        jLabel90.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        jLabel90.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel90.setText("ｺｰﾄﾞ");
        jLabel90.setMaximumSize(new java.awt.Dimension(35, 20));
        jLabel90.setMinimumSize(new java.awt.Dimension(35, 20));
        jLabel90.setPreferredSize(new java.awt.Dimension(35, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 2);
        jPanel44.add(jLabel90, gridBagConstraints);

        office_sub_name_.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        office_sub_name_.setMaximumSize(new java.awt.Dimension(180, 27));
        office_sub_name_.setMinimumSize(new java.awt.Dimension(180, 27));
        office_sub_name_.setPreferredSize(new java.awt.Dimension(180, 27));
        office_sub_name_.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                office_sub_name_ActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 0);
        jPanel44.add(office_sub_name_, gridBagConstraints);

        jLabel75.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        jLabel75.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel75.setText("所在地〒");
        jLabel75.setMaximumSize(new java.awt.Dimension(60, 20));
        jLabel75.setMinimumSize(new java.awt.Dimension(60, 20));
        jLabel75.setPreferredSize(new java.awt.Dimension(60, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        jPanel44.add(jLabel75, gridBagConstraints);

        office_search_.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        office_search_.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        office_search_.setMaximumSize(new java.awt.Dimension(50, 25));
        office_search_.setMinimumSize(new java.awt.Dimension(50, 25));
        office_search_.setPreferredSize(new java.awt.Dimension(50, 25));
        office_search_.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                office_search_FocusGained(evt);
            }
        });
        office_search_.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                office_search_KeyPressed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        jPanel44.add(office_search_, gridBagConstraints);

        jLabel112.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        jLabel112.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel112.setText("所在地");
        jLabel112.setMaximumSize(new java.awt.Dimension(50, 20));
        jLabel112.setMinimumSize(new java.awt.Dimension(50, 20));
        jLabel112.setPreferredSize(new java.awt.Dimension(50, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 2);
        jPanel44.add(jLabel112, gridBagConstraints);

        office_id_cmb.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        office_id_cmb.setMaximumSize(new java.awt.Dimension(80, 27));
        office_id_cmb.setMinimumSize(new java.awt.Dimension(80, 27));
        office_id_cmb.setPreferredSize(new java.awt.Dimension(80, 27));
        office_id_cmb.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                office_id_cmbActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 0, 2, 0);
        jPanel44.add(office_id_cmb, gridBagConstraints);

        office_name_.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        office_name_.setMaximumSize(new java.awt.Dimension(180, 27));
        office_name_.setMinimumSize(new java.awt.Dimension(180, 27));
        office_name_.setPreferredSize(new java.awt.Dimension(180, 27));
        office_name_.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                office_name_ActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 0, 2, 0);
        jPanel44.add(office_name_, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 1;
        jPanel14.add(jPanel44, gridBagConstraints);

        jPanel7.setBorder(javax.swing.BorderFactory.createEtchedBorder(new java.awt.Color(0, 102, 153), null));
        jPanel7.setMinimumSize(new java.awt.Dimension(69, 30));
        jPanel7.setPreferredSize(new java.awt.Dimension(69, 30));
        jPanel7.setLayout(new java.awt.GridBagLayout());

        jLabel10.setFont(new java.awt.Font("メイリオ", 0, 12)); // NOI18N
        jLabel10.setText("交付年月日");
        jLabel10.setMinimumSize(new java.awt.Dimension(65, 25));
        jLabel10.setPreferredSize(new java.awt.Dimension(65, 25));
        jLabel10.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jLabel10MousePressed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        jPanel7.add(jLabel10, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        jPanel14.add(jPanel7, gridBagConstraints);

        jPanel8.setBorder(javax.swing.BorderFactory.createEtchedBorder(new java.awt.Color(0, 102, 153), null));
        jPanel8.setMinimumSize(new java.awt.Dimension(1130, 30));
        jPanel8.setPreferredSize(new java.awt.Dimension(1130, 30));
        jPanel8.setLayout(new java.awt.GridBagLayout());

        grant_year.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        grant_year.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        grant_year.setText("23");
        grant_year.setMaximumSize(new java.awt.Dimension(60, 25));
        grant_year.setMinimumSize(new java.awt.Dimension(60, 25));
        grant_year.setPreferredSize(new java.awt.Dimension(60, 25));
        grant_year.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                grant_yearActionPerformed(evt);
            }
        });
        grant_year.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                grant_yearKeyPressed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        jPanel8.add(grant_year, gridBagConstraints);

        grant_month.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        grant_month.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        grant_month.setText("10");
        grant_month.setMaximumSize(new java.awt.Dimension(35, 25));
        grant_month.setMinimumSize(new java.awt.Dimension(35, 25));
        grant_month.setPreferredSize(new java.awt.Dimension(35, 25));
        grant_month.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                grant_monthActionPerformed(evt);
            }
        });
        grant_month.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                grant_monthKeyPressed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 0;
        jPanel8.add(grant_month, gridBagConstraints);

        jLabel107.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        jLabel107.setText("年");
        jLabel107.setMinimumSize(new java.awt.Dimension(20, 20));
        jLabel107.setPreferredSize(new java.awt.Dimension(20, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        jPanel8.add(jLabel107, gridBagConstraints);

        jLabel108.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        jLabel108.setText("月");
        jLabel108.setMinimumSize(new java.awt.Dimension(20, 20));
        jLabel108.setPreferredSize(new java.awt.Dimension(20, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 0;
        jPanel8.add(jLabel108, gridBagConstraints);

        grant_day.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        grant_day.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        grant_day.setText("31");
        grant_day.setMaximumSize(new java.awt.Dimension(35, 25));
        grant_day.setMinimumSize(new java.awt.Dimension(35, 25));
        grant_day.setPreferredSize(new java.awt.Dimension(35, 25));
        grant_day.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                grant_dayKeyPressed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 6;
        gridBagConstraints.gridy = 0;
        jPanel8.add(grant_day, gridBagConstraints);

        jLabel109.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        jLabel109.setText("日　");
        jLabel109.setMaximumSize(new java.awt.Dimension(30, 20));
        jLabel109.setMinimumSize(new java.awt.Dimension(30, 20));
        jLabel109.setPreferredSize(new java.awt.Dimension(30, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 7;
        gridBagConstraints.gridy = 0;
        jPanel8.add(jLabel109, gridBagConstraints);

        jLabel58.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        jLabel58.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel58.setText("交付番号");
        jLabel58.setMaximumSize(new java.awt.Dimension(65, 20));
        jLabel58.setMinimumSize(new java.awt.Dimension(65, 20));
        jLabel58.setPreferredSize(new java.awt.Dimension(65, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 8;
        gridBagConstraints.gridy = 0;
        jPanel8.add(jLabel58, gridBagConstraints);

        manifesto_no2.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        manifesto_no2.setText("0123456789");
        manifesto_no2.setEnabled(false);
        manifesto_no2.setFocusable(false);
        manifesto_no2.setMaximumSize(new java.awt.Dimension(120, 25));
        manifesto_no2.setMinimumSize(new java.awt.Dimension(120, 25));
        manifesto_no2.setPreferredSize(new java.awt.Dimension(120, 25));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 9;
        gridBagConstraints.gridy = 0;
        jPanel8.add(manifesto_no2, gridBagConstraints);

        jLabel111.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        jLabel111.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel111.setText("氏名");
        jLabel111.setMaximumSize(new java.awt.Dimension(36, 20));
        jLabel111.setMinimumSize(new java.awt.Dimension(40, 20));
        jLabel111.setPreferredSize(new java.awt.Dimension(40, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 14;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        jPanel8.add(jLabel111, gridBagConstraints);

        tantou_cmb.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        tantou_cmb.setMinimumSize(new java.awt.Dimension(180, 25));
        tantou_cmb.setPreferredSize(new java.awt.Dimension(180, 25));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 15;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 5);
        jPanel8.add(tantou_cmb, gridBagConstraints);

        jLabel125.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        jLabel125.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel125.setText("所属");
        jLabel125.setMaximumSize(new java.awt.Dimension(36, 20));
        jLabel125.setMinimumSize(new java.awt.Dimension(40, 20));
        jLabel125.setPreferredSize(new java.awt.Dimension(40, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 12;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        jPanel8.add(jLabel125, gridBagConstraints);

        tantou_cmb1.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        tantou_cmb1.setMinimumSize(new java.awt.Dimension(200, 25));
        tantou_cmb1.setPreferredSize(new java.awt.Dimension(200, 25));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 13;
        gridBagConstraints.gridy = 0;
        jPanel8.add(tantou_cmb1, gridBagConstraints);

        交付年月日_print_f_.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        交付年月日_print_f_.setText("印字なし");
        交付年月日_print_f_.setMaximumSize(new java.awt.Dimension(90, 25));
        交付年月日_print_f_.setMinimumSize(new java.awt.Dimension(90, 22));
        交付年月日_print_f_.setPreferredSize(new java.awt.Dimension(85, 22));
        交付年月日_print_f_.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                交付年月日_print_f_ItemStateChanged(evt);
            }
        });
        交付年月日_print_f_.addAncestorListener(new javax.swing.event.AncestorListener() {
            public void ancestorAdded(javax.swing.event.AncestorEvent evt) {
                交付年月日_print_f_AncestorAdded(evt);
            }
            public void ancestorMoved(javax.swing.event.AncestorEvent evt) {
            }
            public void ancestorRemoved(javax.swing.event.AncestorEvent evt) {
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        jPanel8.add(交付年月日_print_f_, gridBagConstraints);

        交付担当者_print_f_.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        交付担当者_print_f_.setText("印字なし　交付担当者");
        交付担当者_print_f_.setMaximumSize(new java.awt.Dimension(165, 22));
        交付担当者_print_f_.setMinimumSize(new java.awt.Dimension(165, 22));
        交付担当者_print_f_.setPreferredSize(new java.awt.Dimension(165, 22));
        交付担当者_print_f_.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                交付担当者_print_f_ItemStateChanged(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 10;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        jPanel8.add(交付担当者_print_f_, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        jPanel14.add(jPanel8, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        jPanel21.add(jPanel14, gridBagConstraints);

        jPanel31.setMinimumSize(new java.awt.Dimension(1200, 35));
        jPanel31.setPreferredSize(new java.awt.Dimension(1200, 35));
        jPanel31.setLayout(new java.awt.GridBagLayout());

        jPanel30.setBorder(javax.swing.BorderFactory.createEtchedBorder(new java.awt.Color(0, 102, 153), null));
        jPanel30.setMinimumSize(new java.awt.Dimension(400, 35));
        jPanel30.setPreferredSize(new java.awt.Dimension(400, 35));
        jPanel30.setLayout(new java.awt.GridBagLayout());

        jLabel61.setBackground(new java.awt.Color(0, 102, 153));
        jLabel61.setFont(new java.awt.Font("メイリオ", 1, 14)); // NOI18N
        jLabel61.setForeground(new java.awt.Color(255, 255, 255));
        jLabel61.setText("  処分受託者  （処分業者） ");
        jLabel61.setMinimumSize(new java.awt.Dimension(250, 30));
        jLabel61.setOpaque(true);
        jLabel61.setPreferredSize(new java.awt.Dimension(250, 30));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        jPanel30.add(jLabel61, gridBagConstraints);

        処分業者_print_f1.setFont(new java.awt.Font("メイリオ", 0, 12)); // NOI18N
        処分業者_print_f1.setText("印字なし");
        処分業者_print_f1.setMaximumSize(new java.awt.Dimension(110, 20));
        処分業者_print_f1.setMinimumSize(new java.awt.Dimension(90, 20));
        処分業者_print_f1.setPreferredSize(new java.awt.Dimension(110, 20));
        処分業者_print_f1.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                処分業者_print_f1ItemStateChanged(evt);
            }
        });
        jPanel30.add(処分業者_print_f1, new java.awt.GridBagConstraints());

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        jPanel31.add(jPanel30, gridBagConstraints);

        jPanel35.setBorder(javax.swing.BorderFactory.createEtchedBorder(new java.awt.Color(0, 102, 153), null));
        jPanel35.setMinimumSize(new java.awt.Dimension(800, 35));
        jPanel35.setPreferredSize(new java.awt.Dimension(800, 35));
        jPanel35.setLayout(new java.awt.GridBagLayout());

        jLabel62.setBackground(new java.awt.Color(0, 102, 153));
        jLabel62.setFont(new java.awt.Font("メイリオ", 1, 14)); // NOI18N
        jLabel62.setForeground(new java.awt.Color(255, 255, 255));
        jLabel62.setText("  積替え又は保管");
        jLabel62.setMinimumSize(new java.awt.Dimension(88, 30));
        jLabel62.setOpaque(true);
        jLabel62.setPreferredSize(new java.awt.Dimension(88, 30));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.ipadx = 51;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        jPanel35.add(jLabel62, gridBagConstraints);

        積み替え_f5.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        積み替え_f5.setText("印字なし　");
        積み替え_f5.setMaximumSize(new java.awt.Dimension(110, 20));
        積み替え_f5.setMinimumSize(new java.awt.Dimension(110, 20));
        積み替え_f5.setPreferredSize(new java.awt.Dimension(110, 20));
        積み替え_f5.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                積み替え_f5ItemStateChanged(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        jPanel35.add(積み替え_f5, gridBagConstraints);

        jLabel5.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        jLabel5.setText("↑積替え保管有りの場合にはこちらが計量データの運搬業者・車番となります");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        jPanel35.add(jLabel5, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        jPanel31.add(jPanel35, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        jPanel21.add(jPanel31, gridBagConstraints);

        jPanel13.setBorder(javax.swing.BorderFactory.createEtchedBorder(new java.awt.Color(0, 102, 153), null));
        jPanel13.setMinimumSize(new java.awt.Dimension(1200, 40));
        jPanel13.setPreferredSize(new java.awt.Dimension(1200, 40));
        jPanel13.setLayout(new java.awt.GridBagLayout());

        check_f2.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        check_f2.setText("当欄記載のとおり");
        check_f2.setMaximumSize(new java.awt.Dimension(200, 23));
        check_f2.setMinimumSize(new java.awt.Dimension(200, 23));
        check_f2.setPreferredSize(new java.awt.Dimension(200, 23));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 6;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        jPanel13.add(check_f2, gridBagConstraints);

        check_f1.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        check_f1.setText("帳簿記載のとおり  ");
        check_f1.setMaximumSize(new java.awt.Dimension(200, 23));
        check_f1.setMinimumSize(new java.awt.Dimension(200, 23));
        check_f1.setPreferredSize(new java.awt.Dimension(200, 23));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        jPanel13.add(check_f1, gridBagConstraints);

        jLabel92.setBackground(new java.awt.Color(0, 102, 153));
        jLabel92.setFont(new java.awt.Font("メイリオ", 1, 14)); // NOI18N
        jLabel92.setForeground(new java.awt.Color(255, 255, 255));
        jLabel92.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel92.setText("中間処理");
        jLabel92.setMinimumSize(new java.awt.Dimension(75, 18));
        jLabel92.setOpaque(true);
        jLabel92.setPreferredSize(new java.awt.Dimension(75, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        jPanel13.add(jLabel92, gridBagConstraints);

        jLabel104.setBackground(new java.awt.Color(0, 102, 153));
        jLabel104.setFont(new java.awt.Font("メイリオ", 1, 14)); // NOI18N
        jLabel104.setForeground(new java.awt.Color(255, 255, 255));
        jLabel104.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel104.setText("産業廃棄物");
        jLabel104.setMinimumSize(new java.awt.Dimension(90, 18));
        jLabel104.setOpaque(true);
        jLabel104.setPreferredSize(new java.awt.Dimension(90, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        jPanel13.add(jLabel104, gridBagConstraints);

        jLabel105.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        jLabel105.setText("及び管理表の交付番号(登録番号)");
        jLabel105.setMinimumSize(new java.awt.Dimension(216, 18));
        jLabel105.setPreferredSize(new java.awt.Dimension(216, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        jPanel13.add(jLabel105, gridBagConstraints);

        jLabel106.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        jLabel106.setText("管理表交付者(処分委託者)の氏名又は名称");
        jLabel106.setMinimumSize(new java.awt.Dimension(276, 18));
        jLabel106.setPreferredSize(new java.awt.Dimension(276, 18));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        jPanel13.add(jLabel106, gridBagConstraints);

        jPanel34.setMinimumSize(new java.awt.Dimension(40, 35));
        jPanel34.setPreferredSize(new java.awt.Dimension(40, 35));

        javax.swing.GroupLayout jPanel34Layout = new javax.swing.GroupLayout(jPanel34);
        jPanel34.setLayout(jPanel34Layout);
        jPanel34Layout.setHorizontalGroup(
            jPanel34Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 40, Short.MAX_VALUE)
        );
        jPanel34Layout.setVerticalGroup(
            jPanel34Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 36, Short.MAX_VALUE)
        );

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        jPanel13.add(jPanel34, gridBagConstraints);

        jPanel47.setMinimumSize(new java.awt.Dimension(30, 40));
        jPanel47.setPreferredSize(new java.awt.Dimension(30, 30));

        javax.swing.GroupLayout jPanel47Layout = new javax.swing.GroupLayout(jPanel47);
        jPanel47.setLayout(jPanel47Layout);
        jPanel47Layout.setHorizontalGroup(
            jPanel47Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 30, Short.MAX_VALUE)
        );
        jPanel47Layout.setVerticalGroup(
            jPanel47Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 40, Short.MAX_VALUE)
        );

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        jPanel13.add(jPanel47, gridBagConstraints);

        slash_f1.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        slash_f1.setText("斜線有り");
        slash_f1.setMaximumSize(new java.awt.Dimension(300, 23));
        slash_f1.setMinimumSize(new java.awt.Dimension(300, 23));
        slash_f1.setPreferredSize(new java.awt.Dimension(300, 23));
        slash_f1.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                slash_f1ItemStateChanged(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 8;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.weightx = 1.0;
        jPanel13.add(slash_f1, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        jPanel21.add(jPanel13, gridBagConstraints);

        jPanel29.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel29.setMinimumSize(new java.awt.Dimension(1200, 130));
        jPanel29.setPreferredSize(new java.awt.Dimension(1200, 115));
        jPanel29.setLayout(new java.awt.GridBagLayout());

        jPanel38.setBorder(javax.swing.BorderFactory.createEtchedBorder(new java.awt.Color(0, 102, 153), null));
        jPanel38.setMinimumSize(new java.awt.Dimension(240, 130));
        jPanel38.setPreferredSize(new java.awt.Dimension(240, 115));
        jPanel38.setLayout(new java.awt.GridBagLayout());

        jLabel76.setBackground(new java.awt.Color(0, 102, 153));
        jLabel76.setFont(new java.awt.Font("メイリオ", 1, 14)); // NOI18N
        jLabel76.setForeground(new java.awt.Color(255, 255, 255));
        jLabel76.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel76.setText("処分の受託（受領）");
        jLabel76.setMaximumSize(new java.awt.Dimension(160, 25));
        jLabel76.setMinimumSize(new java.awt.Dimension(160, 25));
        jLabel76.setOpaque(true);
        jLabel76.setPreferredSize(new java.awt.Dimension(160, 25));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        jPanel38.add(jLabel76, gridBagConstraints);

        処分の受託_print_f2.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        処分の受託_print_f2.setText("印字無");
        処分の受託_print_f2.setActionCommand("印字なし\n");
        処分の受託_print_f2.setMaximumSize(new java.awt.Dimension(75, 23));
        処分の受託_print_f2.setMinimumSize(new java.awt.Dimension(75, 23));
        処分の受託_print_f2.setPreferredSize(new java.awt.Dimension(75, 23));
        処分の受託_print_f2.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                処分の受託_print_f2ItemStateChanged(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        jPanel38.add(処分の受託_print_f2, gridBagConstraints);

        cont_office1.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        cont_office1.setMaximumSize(new java.awt.Dimension(220, 30));
        cont_office1.setMinimumSize(new java.awt.Dimension(220, 30));
        cont_office1.setPreferredSize(new java.awt.Dimension(220, 30));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 6;
        jPanel38.add(cont_office1, gridBagConstraints);

        cont_name1.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        cont_name1.setMaximumSize(new java.awt.Dimension(220, 30));
        cont_name1.setMinimumSize(new java.awt.Dimension(220, 30));
        cont_name1.setPreferredSize(new java.awt.Dimension(220, 30));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 6;
        jPanel38.add(cont_name1, gridBagConstraints);

        cont_day1.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        cont_day1.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        cont_day1.setText("31");
        cont_day1.setMaximumSize(new java.awt.Dimension(40, 25));
        cont_day1.setMinimumSize(new java.awt.Dimension(40, 25));
        cont_day1.setPreferredSize(new java.awt.Dimension(40, 25));
        cont_day1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                cont_day1KeyPressed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        jPanel38.add(cont_day1, gridBagConstraints);

        jLabel93.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        jLabel93.setText("年");
        jLabel93.setMinimumSize(new java.awt.Dimension(15, 25));
        jLabel93.setPreferredSize(new java.awt.Dimension(15, 25));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        jPanel38.add(jLabel93, gridBagConstraints);

        cont_month1.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        cont_month1.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        cont_month1.setText("1");
        cont_month1.setMaximumSize(new java.awt.Dimension(40, 25));
        cont_month1.setMinimumSize(new java.awt.Dimension(40, 25));
        cont_month1.setPreferredSize(new java.awt.Dimension(40, 25));
        cont_month1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                cont_month1KeyPressed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 0, 0);
        jPanel38.add(cont_month1, gridBagConstraints);

        jLabel94.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        jLabel94.setText("月");
        jLabel94.setMaximumSize(new java.awt.Dimension(15, 20));
        jLabel94.setMinimumSize(new java.awt.Dimension(15, 20));
        jLabel94.setPreferredSize(new java.awt.Dimension(15, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        jPanel38.add(jLabel94, gridBagConstraints);

        cont_year1.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        cont_year1.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        cont_year1.setText("23");
        cont_year1.setMaximumSize(new java.awt.Dimension(60, 25));
        cont_year1.setMinimumSize(new java.awt.Dimension(60, 25));
        cont_year1.setPreferredSize(new java.awt.Dimension(60, 25));
        cont_year1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                cont_year1KeyPressed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 12, 0, 0);
        jPanel38.add(cont_year1, gridBagConstraints);

        jLabel96.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        jLabel96.setText("日");
        jLabel96.setMinimumSize(new java.awt.Dimension(40, 20));
        jLabel96.setPreferredSize(new java.awt.Dimension(40, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 3;
        jPanel38.add(jLabel96, gridBagConstraints);

        jPanel20.setMaximumSize(new java.awt.Dimension(100, 10));
        jPanel20.setMinimumSize(new java.awt.Dimension(100, 10));

        javax.swing.GroupLayout jPanel20Layout = new javax.swing.GroupLayout(jPanel20);
        jPanel20.setLayout(jPanel20Layout);
        jPanel20Layout.setHorizontalGroup(
            jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 240, Short.MAX_VALUE)
        );
        jPanel20Layout.setVerticalGroup(
            jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 10, Short.MAX_VALUE)
        );

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.gridwidth = 6;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weighty = 1.0;
        jPanel38.add(jPanel20, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        jPanel29.add(jPanel38, gridBagConstraints);

        jPanel40.setBorder(javax.swing.BorderFactory.createEtchedBorder(new java.awt.Color(0, 102, 153), null));
        jPanel40.setMaximumSize(new java.awt.Dimension(220, 130));
        jPanel40.setMinimumSize(new java.awt.Dimension(220, 130));
        jPanel40.setPreferredSize(new java.awt.Dimension(220, 115));
        jPanel40.setLayout(new java.awt.GridBagLayout());

        jLabel86.setBackground(new java.awt.Color(0, 102, 153));
        jLabel86.setFont(new java.awt.Font("メイリオ", 1, 14)); // NOI18N
        jLabel86.setForeground(new java.awt.Color(255, 255, 255));
        jLabel86.setText("最終処分終了年月日");
        jLabel86.setMaximumSize(new java.awt.Dimension(135, 28));
        jLabel86.setMinimumSize(new java.awt.Dimension(135, 28));
        jLabel86.setOpaque(true);
        jLabel86.setPreferredSize(new java.awt.Dimension(135, 28));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 5;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 2, 0);
        jPanel40.add(jLabel86, gridBagConstraints);

        jLabel87.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        jLabel87.setText("年");
        jLabel87.setMinimumSize(new java.awt.Dimension(15, 20));
        jLabel87.setPreferredSize(new java.awt.Dimension(15, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        jPanel40.add(jLabel87, gridBagConstraints);

        cont_date2_month.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        cont_date2_month.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        cont_date2_month.setMaximumSize(new java.awt.Dimension(40, 25));
        cont_date2_month.setMinimumSize(new java.awt.Dimension(40, 25));
        cont_date2_month.setPreferredSize(new java.awt.Dimension(40, 25));
        cont_date2_month.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                cont_date2_monthKeyPressed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 0, 0);
        jPanel40.add(cont_date2_month, gridBagConstraints);

        jLabel97.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        jLabel97.setText("月");
        jLabel97.setMinimumSize(new java.awt.Dimension(15, 20));
        jLabel97.setPreferredSize(new java.awt.Dimension(15, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 1;
        jPanel40.add(jLabel97, gridBagConstraints);

        cont_date2_day.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        cont_date2_day.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        cont_date2_day.setMaximumSize(new java.awt.Dimension(40, 25));
        cont_date2_day.setMinimumSize(new java.awt.Dimension(40, 25));
        cont_date2_day.setPreferredSize(new java.awt.Dimension(40, 25));
        cont_date2_day.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                cont_date2_dayKeyPressed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 6;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 0, 0);
        jPanel40.add(cont_date2_day, gridBagConstraints);

        jLabel98.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        jLabel98.setText("日");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 7;
        gridBagConstraints.gridy = 1;
        jPanel40.add(jLabel98, gridBagConstraints);

        cont_date2_year.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        cont_date2_year.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        cont_date2_year.setMaximumSize(new java.awt.Dimension(60, 25));
        cont_date2_year.setMinimumSize(new java.awt.Dimension(60, 25));
        cont_date2_year.setPreferredSize(new java.awt.Dimension(60, 25));
        cont_date2_year.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                cont_date2_yearKeyPressed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 0, 0);
        jPanel40.add(cont_date2_year, gridBagConstraints);

        最終処分社名.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        最終処分社名.setMaximumSize(new java.awt.Dimension(210, 30));
        最終処分社名.setMinimumSize(new java.awt.Dimension(210, 30));
        最終処分社名.setPreferredSize(new java.awt.Dimension(210, 30));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 8;
        jPanel40.add(最終処分社名, gridBagConstraints);

        最終処分担当者.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        最終処分担当者.setMaximumSize(new java.awt.Dimension(210, 30));
        最終処分担当者.setMinimumSize(new java.awt.Dimension(210, 30));
        最終処分担当者.setPreferredSize(new java.awt.Dimension(210, 30));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.weighty = 1.0;
        jPanel40.add(最終処分担当者, gridBagConstraints);

        最終処分確認_print_f.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        最終処分確認_print_f.setText("印字無");
        最終処分確認_print_f.setActionCommand("印字なし\n");
        最終処分確認_print_f.setMaximumSize(new java.awt.Dimension(75, 28));
        最終処分確認_print_f.setMinimumSize(new java.awt.Dimension(75, 28));
        最終処分確認_print_f.setPreferredSize(new java.awt.Dimension(75, 28));
        最終処分確認_print_f.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                最終処分確認_print_fItemStateChanged(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        jPanel40.add(最終処分確認_print_f, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        jPanel29.add(jPanel40, gridBagConstraints);

        jPanel39.setBorder(javax.swing.BorderFactory.createEtchedBorder(new java.awt.Color(0, 102, 153), null));
        jPanel39.setMinimumSize(new java.awt.Dimension(240, 130));
        jPanel39.setPreferredSize(new java.awt.Dimension(240, 115));
        jPanel39.setLayout(new java.awt.GridBagLayout());

        jLabel83.setBackground(new java.awt.Color(0, 102, 153));
        jLabel83.setFont(new java.awt.Font("メイリオ", 1, 14)); // NOI18N
        jLabel83.setForeground(new java.awt.Color(255, 255, 255));
        jLabel83.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel83.setText("処分の受託（処分）");
        jLabel83.setMaximumSize(new java.awt.Dimension(160, 25));
        jLabel83.setMinimumSize(new java.awt.Dimension(160, 25));
        jLabel83.setOpaque(true);
        jLabel83.setPreferredSize(new java.awt.Dimension(160, 25));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        jPanel39.add(jLabel83, gridBagConstraints);

        処分の受託_print_f3.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        処分の受託_print_f3.setText("印字無");
        処分の受託_print_f3.setActionCommand("印字なし\n");
        処分の受託_print_f3.setMaximumSize(new java.awt.Dimension(75, 23));
        処分の受託_print_f3.setMinimumSize(new java.awt.Dimension(75, 23));
        処分の受託_print_f3.setPreferredSize(new java.awt.Dimension(75, 23));
        処分の受託_print_f3.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                処分の受託_print_f3ItemStateChanged(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        jPanel39.add(処分の受託_print_f3, gridBagConstraints);

        cont_office2.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        cont_office2.setMaximumSize(new java.awt.Dimension(220, 30));
        cont_office2.setMinimumSize(new java.awt.Dimension(220, 30));
        cont_office2.setPreferredSize(new java.awt.Dimension(220, 30));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 6;
        jPanel39.add(cont_office2, gridBagConstraints);

        cont_name2.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        cont_name2.setMaximumSize(new java.awt.Dimension(220, 30));
        cont_name2.setMinimumSize(new java.awt.Dimension(220, 30));
        cont_name2.setPreferredSize(new java.awt.Dimension(220, 30));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 6;
        jPanel39.add(cont_name2, gridBagConstraints);

        cont_day2.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        cont_day2.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        cont_day2.setMaximumSize(new java.awt.Dimension(40, 25));
        cont_day2.setMinimumSize(new java.awt.Dimension(40, 25));
        cont_day2.setPreferredSize(new java.awt.Dimension(40, 25));
        cont_day2.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                cont_day2KeyPressed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        jPanel39.add(cont_day2, gridBagConstraints);

        jLabel116.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        jLabel116.setText("年");
        jLabel116.setMinimumSize(new java.awt.Dimension(15, 25));
        jLabel116.setPreferredSize(new java.awt.Dimension(15, 25));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        jPanel39.add(jLabel116, gridBagConstraints);

        cont_month2.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        cont_month2.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        cont_month2.setMaximumSize(new java.awt.Dimension(40, 25));
        cont_month2.setMinimumSize(new java.awt.Dimension(40, 25));
        cont_month2.setPreferredSize(new java.awt.Dimension(40, 25));
        cont_month2.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                cont_month2KeyPressed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 0, 0);
        jPanel39.add(cont_month2, gridBagConstraints);

        jLabel117.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        jLabel117.setText("月");
        jLabel117.setMinimumSize(new java.awt.Dimension(15, 20));
        jLabel117.setPreferredSize(new java.awt.Dimension(15, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        jPanel39.add(jLabel117, gridBagConstraints);

        cont_year2.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        cont_year2.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        cont_year2.setMaximumSize(new java.awt.Dimension(60, 25));
        cont_year2.setMinimumSize(new java.awt.Dimension(60, 25));
        cont_year2.setPreferredSize(new java.awt.Dimension(60, 25));
        cont_year2.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                cont_year2KeyPressed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 12, 0, 0);
        jPanel39.add(cont_year2, gridBagConstraints);

        jLabel118.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        jLabel118.setText("日");
        jLabel118.setMinimumSize(new java.awt.Dimension(40, 20));
        jLabel118.setPreferredSize(new java.awt.Dimension(40, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 3;
        jPanel39.add(jLabel118, gridBagConstraints);

        jPanel5.setMaximumSize(new java.awt.Dimension(100, 10));
        jPanel5.setMinimumSize(new java.awt.Dimension(100, 10));
        jPanel5.setPreferredSize(new java.awt.Dimension(100, 10));

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 240, Short.MAX_VALUE)
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 10, Short.MAX_VALUE)
        );

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.gridwidth = 6;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weighty = 1.0;
        jPanel39.add(jPanel5, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        jPanel29.add(jPanel39, gridBagConstraints);

        jPanel56.setBorder(javax.swing.BorderFactory.createEtchedBorder(new java.awt.Color(0, 102, 153), null));
        jPanel56.setMinimumSize(new java.awt.Dimension(240, 115));
        jPanel56.setPreferredSize(new java.awt.Dimension(240, 115));
        jPanel56.setLayout(new java.awt.GridBagLayout());

        jLabel85.setBackground(new java.awt.Color(0, 102, 153));
        jLabel85.setFont(new java.awt.Font("メイリオ", 1, 14)); // NOI18N
        jLabel85.setForeground(new java.awt.Color(255, 255, 255));
        jLabel85.setText("　運搬の受託（1）");
        jLabel85.setMaximumSize(new java.awt.Dimension(150, 35));
        jLabel85.setMinimumSize(new java.awt.Dimension(150, 35));
        jLabel85.setOpaque(true);
        jLabel85.setPreferredSize(new java.awt.Dimension(150, 35));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        jPanel56.add(jLabel85, gridBagConstraints);

        運搬の受託_print_f3.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        運搬の受託_print_f3.setText("印字無");
        運搬の受託_print_f3.setActionCommand("印字なし\n");
        運搬の受託_print_f3.setMaximumSize(new java.awt.Dimension(80, 23));
        運搬の受託_print_f3.setMinimumSize(new java.awt.Dimension(80, 23));
        運搬の受託_print_f3.setPreferredSize(new java.awt.Dimension(80, 23));
        運搬の受託_print_f3.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                運搬の受託_print_f3ItemStateChanged(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.SOUTHWEST;
        jPanel56.add(運搬の受託_print_f3, gridBagConstraints);

        jLabel73.setMaximumSize(new java.awt.Dimension(50, 13));
        jLabel73.setMinimumSize(new java.awt.Dimension(40, 13));
        jLabel73.setPreferredSize(new java.awt.Dimension(40, 13));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        jPanel56.add(jLabel73, gridBagConstraints);

        jPanel18.setMaximumSize(new java.awt.Dimension(230, 90));
        jPanel18.setMinimumSize(new java.awt.Dimension(230, 90));
        jPanel18.setPreferredSize(new java.awt.Dimension(230, 90));
        jPanel18.setLayout(new java.awt.GridBagLayout());

        trans_office1.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        trans_office1.setMaximumSize(new java.awt.Dimension(210, 30));
        trans_office1.setMinimumSize(new java.awt.Dimension(210, 30));
        trans_office1.setPreferredSize(new java.awt.Dimension(210, 30));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 7;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 15, 0, 15);
        jPanel18.add(trans_office1, gridBagConstraints);

        trans_tantou_search_1.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        trans_tantou_search_1.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        trans_tantou_search_1.setMaximumSize(new java.awt.Dimension(60, 25));
        trans_tantou_search_1.setMinimumSize(new java.awt.Dimension(60, 25));
        trans_tantou_search_1.setPreferredSize(new java.awt.Dimension(60, 25));
        trans_tantou_search_1.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                trans_tantou_search_1FocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                trans_tantou_search_1FocusLost(evt);
            }
        });
        trans_tantou_search_1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                trans_tantou_search_1KeyPressed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 15, 0, 0);
        jPanel18.add(trans_tantou_search_1, gridBagConstraints);

        trans_tantou_id_cmb1.setEditable(true);
        trans_tantou_id_cmb1.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        trans_tantou_id_cmb1.setMaximumSize(new java.awt.Dimension(150, 27));
        trans_tantou_id_cmb1.setMinimumSize(new java.awt.Dimension(150, 27));
        trans_tantou_id_cmb1.setPreferredSize(new java.awt.Dimension(150, 27));
        trans_tantou_id_cmb1.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                trans_tantou_id_cmb1ItemStateChanged(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 6;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 15);
        jPanel18.add(trans_tantou_id_cmb1, gridBagConstraints);

        trans_year1.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        trans_year1.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        trans_year1.setMaximumSize(new java.awt.Dimension(60, 25));
        trans_year1.setMinimumSize(new java.awt.Dimension(60, 25));
        trans_year1.setPreferredSize(new java.awt.Dimension(60, 25));
        trans_year1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                trans_year1KeyPressed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        jPanel18.add(trans_year1, gridBagConstraints);

        jLabel121.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        jLabel121.setText("年");
        jLabel121.setMinimumSize(new java.awt.Dimension(15, 25));
        jLabel121.setPreferredSize(new java.awt.Dimension(15, 25));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 3;
        jPanel18.add(jLabel121, gridBagConstraints);

        trans_month1.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        trans_month1.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        trans_month1.setMaximumSize(new java.awt.Dimension(40, 25));
        trans_month1.setMinimumSize(new java.awt.Dimension(40, 25));
        trans_month1.setPreferredSize(new java.awt.Dimension(40, 25));
        trans_month1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                trans_month1KeyPressed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 0, 0);
        jPanel18.add(trans_month1, gridBagConstraints);

        jLabel123.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        jLabel123.setText("月");
        jLabel123.setMinimumSize(new java.awt.Dimension(15, 20));
        jLabel123.setPreferredSize(new java.awt.Dimension(15, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 3;
        jPanel18.add(jLabel123, gridBagConstraints);

        trans_day1.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        trans_day1.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        trans_day1.setMaximumSize(new java.awt.Dimension(40, 25));
        trans_day1.setMinimumSize(new java.awt.Dimension(40, 25));
        trans_day1.setPreferredSize(new java.awt.Dimension(40, 25));
        trans_day1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                trans_day1KeyPressed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 0, 0);
        jPanel18.add(trans_day1, gridBagConstraints);

        jLabel124.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        jLabel124.setText("日");
        jLabel124.setMaximumSize(new java.awt.Dimension(20, 20));
        jLabel124.setMinimumSize(new java.awt.Dimension(20, 20));
        jLabel124.setPreferredSize(new java.awt.Dimension(20, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 6;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 15);
        jPanel18.add(jLabel124, gridBagConstraints);

        trans_name1.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        trans_name1.setMaximumSize(new java.awt.Dimension(155, 1));
        trans_name1.setMinimumSize(new java.awt.Dimension(155, 1));
        trans_name1.setPreferredSize(new java.awt.Dimension(155, 1));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.gridwidth = 7;
        gridBagConstraints.weighty = 1.0;
        jPanel18.add(trans_name1, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        jPanel56.add(jPanel18, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        jPanel29.add(jPanel56, gridBagConstraints);

        jPanel57.setBorder(javax.swing.BorderFactory.createEtchedBorder(new java.awt.Color(0, 102, 153), null));
        jPanel57.setMaximumSize(new java.awt.Dimension(260, 130));
        jPanel57.setMinimumSize(new java.awt.Dimension(260, 115));
        jPanel57.setPreferredSize(new java.awt.Dimension(260, 115));
        jPanel57.setLayout(new java.awt.GridBagLayout());

        jPanel58.setMaximumSize(new java.awt.Dimension(230, 90));
        jPanel58.setMinimumSize(new java.awt.Dimension(230, 90));
        jPanel58.setPreferredSize(new java.awt.Dimension(230, 90));
        jPanel58.setLayout(new java.awt.GridBagLayout());

        trans_office2.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        trans_office2.setMaximumSize(new java.awt.Dimension(220, 30));
        trans_office2.setMinimumSize(new java.awt.Dimension(220, 30));
        trans_office2.setPreferredSize(new java.awt.Dimension(220, 30));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 7;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 15, 0, 15);
        jPanel58.add(trans_office2, gridBagConstraints);

        trans_name2.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        trans_name2.setMaximumSize(new java.awt.Dimension(155, 1));
        trans_name2.setMinimumSize(new java.awt.Dimension(155, 1));
        trans_name2.setPreferredSize(new java.awt.Dimension(155, 1));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 0, 2, 5);
        jPanel58.add(trans_name2, gridBagConstraints);

        trans_tantou_id_cmb2.setEditable(true);
        trans_tantou_id_cmb2.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        trans_tantou_id_cmb2.setMaximumSize(new java.awt.Dimension(150, 27));
        trans_tantou_id_cmb2.setMinimumSize(new java.awt.Dimension(150, 27));
        trans_tantou_id_cmb2.setPreferredSize(new java.awt.Dimension(150, 27));
        trans_tantou_id_cmb2.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                trans_tantou_id_cmb2ItemStateChanged(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 6;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 15);
        jPanel58.add(trans_tantou_id_cmb2, gridBagConstraints);

        trans_tantou_search_2.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        trans_tantou_search_2.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        trans_tantou_search_2.setMaximumSize(new java.awt.Dimension(60, 25));
        trans_tantou_search_2.setMinimumSize(new java.awt.Dimension(60, 25));
        trans_tantou_search_2.setPreferredSize(new java.awt.Dimension(60, 25));
        trans_tantou_search_2.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                trans_tantou_search_2FocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                trans_tantou_search_2FocusLost(evt);
            }
        });
        trans_tantou_search_2.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                trans_tantou_search_2KeyPressed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 15, 0, 0);
        jPanel58.add(trans_tantou_search_2, gridBagConstraints);

        trans_year2.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        trans_year2.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        trans_year2.setMaximumSize(new java.awt.Dimension(60, 25));
        trans_year2.setMinimumSize(new java.awt.Dimension(60, 25));
        trans_year2.setPreferredSize(new java.awt.Dimension(60, 25));
        trans_year2.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                trans_year2KeyPressed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        jPanel58.add(trans_year2, gridBagConstraints);

        jLabel136.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        jLabel136.setText("年");
        jLabel136.setMinimumSize(new java.awt.Dimension(15, 25));
        jLabel136.setPreferredSize(new java.awt.Dimension(15, 25));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 2;
        jPanel58.add(jLabel136, gridBagConstraints);

        trans_month2.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        trans_month2.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        trans_month2.setMaximumSize(new java.awt.Dimension(40, 25));
        trans_month2.setMinimumSize(new java.awt.Dimension(40, 25));
        trans_month2.setPreferredSize(new java.awt.Dimension(40, 25));
        trans_month2.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                trans_month2KeyPressed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 0, 0);
        jPanel58.add(trans_month2, gridBagConstraints);

        jLabel137.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        jLabel137.setText("月");
        jLabel137.setMinimumSize(new java.awt.Dimension(15, 20));
        jLabel137.setPreferredSize(new java.awt.Dimension(15, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 2;
        jPanel58.add(jLabel137, gridBagConstraints);

        trans_day2.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        trans_day2.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        trans_day2.setMaximumSize(new java.awt.Dimension(40, 25));
        trans_day2.setMinimumSize(new java.awt.Dimension(40, 25));
        trans_day2.setPreferredSize(new java.awt.Dimension(40, 25));
        trans_day2.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                trans_day2KeyPressed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 0, 0);
        jPanel58.add(trans_day2, gridBagConstraints);

        jLabel138.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        jLabel138.setText("日");
        jLabel138.setMinimumSize(new java.awt.Dimension(40, 20));
        jLabel138.setPreferredSize(new java.awt.Dimension(40, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 6;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        jPanel58.add(jLabel138, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weighty = 1.0;
        jPanel57.add(jPanel58, gridBagConstraints);

        jLabel131.setBackground(new java.awt.Color(0, 102, 153));
        jLabel131.setFont(new java.awt.Font("メイリオ", 1, 14)); // NOI18N
        jLabel131.setForeground(new java.awt.Color(255, 255, 255));
        jLabel131.setText("　運搬の受託（2）");
        jLabel131.setMaximumSize(new java.awt.Dimension(125, 27));
        jLabel131.setMinimumSize(new java.awt.Dimension(125, 27));
        jLabel131.setOpaque(true);
        jLabel131.setPreferredSize(new java.awt.Dimension(125, 27));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        jPanel57.add(jLabel131, gridBagConstraints);

        運搬の受託_print_f4.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        運搬の受託_print_f4.setText("印字無");
        運搬の受託_print_f4.setActionCommand("印字なし\n");
        運搬の受託_print_f4.setMaximumSize(new java.awt.Dimension(77, 20));
        運搬の受託_print_f4.setMinimumSize(new java.awt.Dimension(70, 23));
        運搬の受託_print_f4.setPreferredSize(new java.awt.Dimension(70, 23));
        運搬の受託_print_f4.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                運搬の受託_print_f4ItemStateChanged(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        jPanel57.add(運搬の受託_print_f4, gridBagConstraints);

        slash_f3.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        slash_f3.setText("斜線");
        slash_f3.setActionCommand("印字なし\n");
        slash_f3.setMaximumSize(new java.awt.Dimension(53, 25));
        slash_f3.setMinimumSize(new java.awt.Dimension(53, 25));
        slash_f3.setPreferredSize(new java.awt.Dimension(53, 25));
        slash_f3.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                slash_f3ItemStateChanged(evt);
            }
        });
        slash_f3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                slash_f3ActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        jPanel57.add(slash_f3, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        jPanel29.add(jPanel57, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        jPanel21.add(jPanel29, gridBagConstraints);

        jPanel3.setBorder(javax.swing.BorderFactory.createEtchedBorder(new java.awt.Color(0, 102, 153), null));
        jPanel3.setMinimumSize(new java.awt.Dimension(1040, 170));
        jPanel3.setPreferredSize(new java.awt.Dimension(1040, 170));
        jPanel3.setLayout(new java.awt.GridBagLayout());

        goods_f16.setFont(new java.awt.Font("MS UI Gothic", 1, 12)); // NOI18N
        goods_f16.setMargin(new java.awt.Insets(0, 0, 0, 0));
        goods_f16.setMaximumSize(new java.awt.Dimension(165, 25));
        goods_f16.setMinimumSize(new java.awt.Dimension(165, 25));
        goods_f16.setPreferredSize(new java.awt.Dimension(165, 25));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        jPanel3.add(goods_f16, gridBagConstraints);

        goods_f23.setFont(new java.awt.Font("メイリオ", 0, 12)); // NOI18N
        goods_f23.setText("17 石綿含有産業廃棄物");
        goods_f23.setMargin(new java.awt.Insets(0, 0, 0, 0));
        goods_f23.setMaximumSize(new java.awt.Dimension(140, 25));
        goods_f23.setMinimumSize(new java.awt.Dimension(140, 25));
        goods_f23.setPreferredSize(new java.awt.Dimension(140, 25));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 6;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        jPanel3.add(goods_f23, gridBagConstraints);

        goods_f24.setFont(new java.awt.Font("メイリオ", 0, 12)); // NOI18N
        goods_f24.setText("水銀使用製品");
        goods_f24.setMargin(new java.awt.Insets(0, 0, 0, 0));
        goods_f24.setMaximumSize(new java.awt.Dimension(140, 25));
        goods_f24.setMinimumSize(new java.awt.Dimension(140, 25));
        goods_f24.setPreferredSize(new java.awt.Dimension(140, 25));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 6;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        jPanel3.add(goods_f24, gridBagConstraints);

        goods_f25.setFont(new java.awt.Font("メイリオ", 0, 12)); // NOI18N
        goods_f25.setText("　　　　　産業廃棄物");
        goods_f25.setMargin(new java.awt.Insets(0, 0, 0, 0));
        goods_f25.setMaximumSize(new java.awt.Dimension(140, 25));
        goods_f25.setMinimumSize(new java.awt.Dimension(140, 25));
        goods_f25.setPreferredSize(new java.awt.Dimension(140, 25));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 6;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        jPanel3.add(goods_f25, gridBagConstraints);

        goods_f22.setFont(new java.awt.Font("メイリオ", 0, 12)); // NOI18N
        goods_f22.setText("16 安定(管理型含む)");
        goods_f22.setMargin(new java.awt.Insets(0, 0, 0, 0));
        goods_f22.setMaximumSize(new java.awt.Dimension(140, 25));
        goods_f22.setMinimumSize(new java.awt.Dimension(140, 25));
        goods_f22.setPreferredSize(new java.awt.Dimension(140, 25));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        jPanel3.add(goods_f22, gridBagConstraints);

        goods_f21.setFont(new java.awt.Font("メイリオ", 0, 12)); // NOI18N
        goods_f21.setText("15 廃石膏ボード");
        goods_f21.setMargin(new java.awt.Insets(0, 0, 0, 0));
        goods_f21.setMaximumSize(new java.awt.Dimension(140, 25));
        goods_f21.setMinimumSize(new java.awt.Dimension(140, 25));
        goods_f21.setPreferredSize(new java.awt.Dimension(140, 25));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        jPanel3.add(goods_f21, gridBagConstraints);

        goods_f32.setFont(new java.awt.Font("MS UI Gothic", 1, 12)); // NOI18N
        goods_f32.setMargin(new java.awt.Insets(0, 0, 0, 0));
        goods_f32.setMaximumSize(new java.awt.Dimension(120, 25));
        goods_f32.setMinimumSize(new java.awt.Dimension(120, 25));
        goods_f32.setPreferredSize(new java.awt.Dimension(120, 25));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 8;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        jPanel3.add(goods_f32, gridBagConstraints);

        goods_f29.setFont(new java.awt.Font("MS UI Gothic", 1, 12)); // NOI18N
        goods_f29.setMargin(new java.awt.Insets(0, 0, 0, 0));
        goods_f29.setMaximumSize(new java.awt.Dimension(120, 25));
        goods_f29.setMinimumSize(new java.awt.Dimension(120, 25));
        goods_f29.setPreferredSize(new java.awt.Dimension(120, 25));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 8;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        jPanel3.add(goods_f29, gridBagConstraints);

        goods_f28.setFont(new java.awt.Font("MS UI Gothic", 1, 12)); // NOI18N
        goods_f28.setMargin(new java.awt.Insets(0, 0, 0, 0));
        goods_f28.setMaximumSize(new java.awt.Dimension(120, 25));
        goods_f28.setMinimumSize(new java.awt.Dimension(120, 25));
        goods_f28.setPreferredSize(new java.awt.Dimension(120, 25));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 8;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        jPanel3.add(goods_f28, gridBagConstraints);

        goods_f17.setFont(new java.awt.Font("メイリオ", 0, 12)); // NOI18N
        goods_f17.setText("11 建設汚泥");
        goods_f17.setMargin(new java.awt.Insets(0, 0, 0, 0));
        goods_f17.setMaximumSize(new java.awt.Dimension(140, 25));
        goods_f17.setMinimumSize(new java.awt.Dimension(140, 25));
        goods_f17.setPreferredSize(new java.awt.Dimension(140, 25));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        jPanel3.add(goods_f17, gridBagConstraints);

        goods_f31.setFont(new java.awt.Font("MS UI Gothic", 1, 12)); // NOI18N
        goods_f31.setMargin(new java.awt.Insets(0, 0, 0, 0));
        goods_f31.setMaximumSize(new java.awt.Dimension(120, 25));
        goods_f31.setMinimumSize(new java.awt.Dimension(120, 25));
        goods_f31.setPreferredSize(new java.awt.Dimension(120, 25));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 8;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        jPanel3.add(goods_f31, gridBagConstraints);

        goods_f19.setFont(new java.awt.Font("メイリオ", 0, 12)); // NOI18N
        goods_f19.setText("13 木くず");
        goods_f19.setMargin(new java.awt.Insets(0, 0, 0, 0));
        goods_f19.setMaximumSize(new java.awt.Dimension(140, 25));
        goods_f19.setMinimumSize(new java.awt.Dimension(140, 25));
        goods_f19.setPreferredSize(new java.awt.Dimension(140, 25));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        jPanel3.add(goods_f19, gridBagConstraints);

        goods_f18.setFont(new java.awt.Font("メイリオ", 0, 12)); // NOI18N
        goods_f18.setText("12 紙くず");
        goods_f18.setMargin(new java.awt.Insets(0, 0, 0, 0));
        goods_f18.setMaximumSize(new java.awt.Dimension(140, 25));
        goods_f18.setMinimumSize(new java.awt.Dimension(140, 25));
        goods_f18.setPreferredSize(new java.awt.Dimension(140, 25));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        jPanel3.add(goods_f18, gridBagConstraints);

        goods_f20.setFont(new java.awt.Font("メイリオ", 0, 12)); // NOI18N
        goods_f20.setText("14 繊維くず");
        goods_f20.setMargin(new java.awt.Insets(0, 0, 0, 0));
        goods_f20.setMaximumSize(new java.awt.Dimension(140, 25));
        goods_f20.setMinimumSize(new java.awt.Dimension(140, 25));
        goods_f20.setPreferredSize(new java.awt.Dimension(140, 25));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        jPanel3.add(goods_f20, gridBagConstraints);

        goods_f15.setFont(new java.awt.Font("MS UI Gothic", 1, 12)); // NOI18N
        goods_f15.setMargin(new java.awt.Insets(0, 0, 0, 0));
        goods_f15.setMaximumSize(new java.awt.Dimension(165, 25));
        goods_f15.setMinimumSize(new java.awt.Dimension(165, 25));
        goods_f15.setPreferredSize(new java.awt.Dimension(165, 25));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        jPanel3.add(goods_f15, gridBagConstraints);

        goods_f14.setFont(new java.awt.Font("MS UI Gothic", 1, 12)); // NOI18N
        goods_f14.setMargin(new java.awt.Insets(0, 0, 0, 0));
        goods_f14.setMaximumSize(new java.awt.Dimension(165, 25));
        goods_f14.setMinimumSize(new java.awt.Dimension(165, 25));
        goods_f14.setPreferredSize(new java.awt.Dimension(165, 25));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        jPanel3.add(goods_f14, gridBagConstraints);

        goods_f11.setFont(new java.awt.Font("メイリオ", 0, 12)); // NOI18N
        goods_f11.setText("07 混合(安定型)");
        goods_f11.setMargin(new java.awt.Insets(0, 0, 0, 0));
        goods_f11.setMaximumSize(new java.awt.Dimension(165, 25));
        goods_f11.setMinimumSize(new java.awt.Dimension(165, 25));
        goods_f11.setPreferredSize(new java.awt.Dimension(165, 25));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        jPanel3.add(goods_f11, gridBagConstraints);

        goods_f26.setFont(new java.awt.Font("MS UI Gothic", 1, 12)); // NOI18N
        goods_f26.setMargin(new java.awt.Insets(0, 0, 0, 0));
        goods_f26.setMaximumSize(new java.awt.Dimension(140, 25));
        goods_f26.setMinimumSize(new java.awt.Dimension(140, 25));
        goods_f26.setPreferredSize(new java.awt.Dimension(140, 25));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 6;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        jPanel3.add(goods_f26, gridBagConstraints);

        goods_f09.setFont(new java.awt.Font("メイリオ", 0, 12)); // NOI18N
        goods_f09.setText("05 廃ﾌﾟﾗｽﾁｯｸ類");
        goods_f09.setMargin(new java.awt.Insets(0, 0, 0, 0));
        goods_f09.setMaximumSize(new java.awt.Dimension(165, 25));
        goods_f09.setMinimumSize(new java.awt.Dimension(165, 25));
        goods_f09.setPreferredSize(new java.awt.Dimension(165, 25));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        jPanel3.add(goods_f09, gridBagConstraints);

        goods_f10.setFont(new java.awt.Font("メイリオ", 0, 12)); // NOI18N
        goods_f10.setText("06 金属くず");
        goods_f10.setMargin(new java.awt.Insets(0, 0, 0, 0));
        goods_f10.setMaximumSize(new java.awt.Dimension(165, 25));
        goods_f10.setMinimumSize(new java.awt.Dimension(165, 25));
        goods_f10.setPreferredSize(new java.awt.Dimension(165, 25));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        jPanel3.add(goods_f10, gridBagConstraints);

        goods_f08.setFont(new java.awt.Font("メイリオ", 0, 12)); // NOI18N
        goods_f08.setText("04 ｶﾞﾗｽ・陶磁器くず");
        goods_f08.setMargin(new java.awt.Insets(0, 0, 0, 0));
        goods_f08.setMaximumSize(new java.awt.Dimension(165, 25));
        goods_f08.setMinimumSize(new java.awt.Dimension(165, 25));
        goods_f08.setPreferredSize(new java.awt.Dimension(165, 25));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        jPanel3.add(goods_f08, gridBagConstraints);

        goods_f12.setFont(new java.awt.Font("メイリオ", 0, 12)); // NOI18N
        goods_f12.setText("08 石綿含有産業廃棄物");
        goods_f12.setMargin(new java.awt.Insets(0, 0, 0, 0));
        goods_f12.setMaximumSize(new java.awt.Dimension(165, 25));
        goods_f12.setMinimumSize(new java.awt.Dimension(165, 25));
        goods_f12.setPreferredSize(new java.awt.Dimension(165, 25));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        jPanel3.add(goods_f12, gridBagConstraints);

        goods_f07.setFont(new java.awt.Font("メイリオ", 0, 12)); // NOI18N
        goods_f07.setText("03 その他がれき類");
        goods_f07.setMargin(new java.awt.Insets(0, 0, 0, 0));
        goods_f07.setMaximumSize(new java.awt.Dimension(165, 25));
        goods_f07.setMinimumSize(new java.awt.Dimension(165, 25));
        goods_f07.setPreferredSize(new java.awt.Dimension(165, 25));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        jPanel3.add(goods_f07, gridBagConstraints);

        goods_f06.setFont(new java.awt.Font("メイリオ", 0, 12)); // NOI18N
        goods_f06.setText("02 アスコンがら");
        goods_f06.setMargin(new java.awt.Insets(0, 0, 0, 0));
        goods_f06.setMaximumSize(new java.awt.Dimension(165, 25));
        goods_f06.setMinimumSize(new java.awt.Dimension(165, 25));
        goods_f06.setPreferredSize(new java.awt.Dimension(165, 25));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        jPanel3.add(goods_f06, gridBagConstraints);

        goods_f13.setFont(new java.awt.Font("MS UI Gothic", 1, 12)); // NOI18N
        goods_f13.setMargin(new java.awt.Insets(0, 0, 0, 0));
        goods_f13.setMaximumSize(new java.awt.Dimension(165, 25));
        goods_f13.setMinimumSize(new java.awt.Dimension(165, 25));
        goods_f13.setPreferredSize(new java.awt.Dimension(165, 25));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        jPanel3.add(goods_f13, gridBagConstraints);

        goods_f27.setFont(new java.awt.Font("メイリオ", 0, 12)); // NOI18N
        goods_f27.setText("21 廃石綿等");
        goods_f27.setMargin(new java.awt.Insets(0, 0, 0, 0));
        goods_f27.setMaximumSize(new java.awt.Dimension(120, 25));
        goods_f27.setMinimumSize(new java.awt.Dimension(120, 25));
        goods_f27.setPreferredSize(new java.awt.Dimension(120, 25));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 8;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        jPanel3.add(goods_f27, gridBagConstraints);

        goods_f05.setFont(new java.awt.Font("メイリオ", 0, 12)); // NOI18N
        goods_f05.setText("01 コンクリートがら");
        goods_f05.setMargin(new java.awt.Insets(0, 0, 0, 0));
        goods_f05.setMaximumSize(new java.awt.Dimension(165, 25));
        goods_f05.setMinimumSize(new java.awt.Dimension(165, 25));
        goods_f05.setPreferredSize(new java.awt.Dimension(165, 25));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        jPanel3.add(goods_f05, gridBagConstraints);

        jLabel100.setBackground(new java.awt.Color(0, 102, 153));
        jLabel100.setFont(new java.awt.Font("メイリオ", 1, 14)); // NOI18N
        jLabel100.setForeground(new java.awt.Color(255, 255, 255));
        jLabel100.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel100.setText("数量");
        jLabel100.setMinimumSize(new java.awt.Dimension(55, 20));
        jLabel100.setOpaque(true);
        jLabel100.setPreferredSize(new java.awt.Dimension(55, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        jPanel3.add(jLabel100, gridBagConstraints);

        value_tex1.setFont(new java.awt.Font("メイリオ", 0, 12)); // NOI18N
        value_tex1.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        value_tex1.setMaximumSize(new java.awt.Dimension(55, 25));
        value_tex1.setMinimumSize(new java.awt.Dimension(55, 25));
        value_tex1.setPreferredSize(new java.awt.Dimension(55, 25));
        value_tex1.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                value_tex1FocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        jPanel3.add(value_tex1, gridBagConstraints);

        goods_f30.setFont(new java.awt.Font("MS UI Gothic", 1, 12)); // NOI18N
        goods_f30.setMargin(new java.awt.Insets(0, 0, 0, 0));
        goods_f30.setMaximumSize(new java.awt.Dimension(120, 25));
        goods_f30.setMinimumSize(new java.awt.Dimension(120, 25));
        goods_f30.setPreferredSize(new java.awt.Dimension(120, 25));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 8;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        jPanel3.add(goods_f30, gridBagConstraints);

        value_tex3.setFont(new java.awt.Font("メイリオ", 0, 12)); // NOI18N
        value_tex3.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        value_tex3.setMaximumSize(new java.awt.Dimension(55, 25));
        value_tex3.setMinimumSize(new java.awt.Dimension(55, 25));
        value_tex3.setPreferredSize(new java.awt.Dimension(55, 25));
        value_tex3.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                value_tex3FocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        jPanel3.add(value_tex3, gridBagConstraints);

        value_tex2.setFont(new java.awt.Font("メイリオ", 0, 12)); // NOI18N
        value_tex2.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        value_tex2.setMaximumSize(new java.awt.Dimension(55, 25));
        value_tex2.setMinimumSize(new java.awt.Dimension(55, 25));
        value_tex2.setPreferredSize(new java.awt.Dimension(55, 25));
        value_tex2.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                value_tex2FocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        jPanel3.add(value_tex2, gridBagConstraints);

        jLabel102.setBackground(new java.awt.Color(0, 102, 153));
        jLabel102.setFont(new java.awt.Font("メイリオ", 1, 14)); // NOI18N
        jLabel102.setForeground(new java.awt.Color(255, 255, 255));
        jLabel102.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel102.setText("数量");
        jLabel102.setMinimumSize(new java.awt.Dimension(60, 20));
        jLabel102.setOpaque(true);
        jLabel102.setPreferredSize(new java.awt.Dimension(60, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        jPanel3.add(jLabel102, gridBagConstraints);

        jLabel103.setBackground(new java.awt.Color(0, 102, 153));
        jLabel103.setFont(new java.awt.Font("メイリオ", 1, 14)); // NOI18N
        jLabel103.setForeground(new java.awt.Color(255, 255, 255));
        jLabel103.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel103.setText(" 安定型品目");
        jLabel103.setMaximumSize(new java.awt.Dimension(170, 20));
        jLabel103.setMinimumSize(new java.awt.Dimension(160, 20));
        jLabel103.setOpaque(true);
        jLabel103.setPreferredSize(new java.awt.Dimension(170, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        jPanel3.add(jLabel103, gridBagConstraints);

        jLabel101.setBackground(new java.awt.Color(0, 102, 153));
        jLabel101.setFont(new java.awt.Font("メイリオ", 1, 14)); // NOI18N
        jLabel101.setForeground(new java.awt.Color(255, 255, 255));
        jLabel101.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel101.setText(" 管理型品目");
        jLabel101.setMinimumSize(new java.awt.Dimension(140, 20));
        jLabel101.setOpaque(true);
        jLabel101.setPreferredSize(new java.awt.Dimension(140, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        jPanel3.add(jLabel101, gridBagConstraints);

        jLabel25.setBackground(new java.awt.Color(0, 102, 153));
        jLabel25.setFont(new java.awt.Font("メイリオ", 1, 14)); // NOI18N
        jLabel25.setForeground(new java.awt.Color(255, 255, 255));
        jLabel25.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel25.setText(" 安定型品目");
        jLabel25.setMinimumSize(new java.awt.Dimension(150, 20));
        jLabel25.setOpaque(true);
        jLabel25.setPreferredSize(new java.awt.Dimension(150, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        jPanel3.add(jLabel25, gridBagConstraints);

        jLabel27.setBackground(new java.awt.Color(0, 102, 153));
        jLabel27.setFont(new java.awt.Font("メイリオ", 1, 14)); // NOI18N
        jLabel27.setForeground(new java.awt.Color(255, 255, 255));
        jLabel27.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel27.setText("数量");
        jLabel27.setMinimumSize(new java.awt.Dimension(55, 20));
        jLabel27.setOpaque(true);
        jLabel27.setPreferredSize(new java.awt.Dimension(55, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        jPanel3.add(jLabel27, gridBagConstraints);

        value_tex4.setFont(new java.awt.Font("メイリオ", 0, 12)); // NOI18N
        value_tex4.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        value_tex4.setMaximumSize(new java.awt.Dimension(55, 25));
        value_tex4.setMinimumSize(new java.awt.Dimension(55, 25));
        value_tex4.setPreferredSize(new java.awt.Dimension(55, 25));
        value_tex4.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                value_tex4FocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        jPanel3.add(value_tex4, gridBagConstraints);

        value_tex5.setFont(new java.awt.Font("メイリオ", 0, 12)); // NOI18N
        value_tex5.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        value_tex5.setMaximumSize(new java.awt.Dimension(55, 25));
        value_tex5.setMinimumSize(new java.awt.Dimension(55, 25));
        value_tex5.setPreferredSize(new java.awt.Dimension(55, 25));
        value_tex5.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                value_tex5FocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        jPanel3.add(value_tex5, gridBagConstraints);

        value_tex6.setFont(new java.awt.Font("メイリオ", 0, 12)); // NOI18N
        value_tex6.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        value_tex6.setMaximumSize(new java.awt.Dimension(55, 25));
        value_tex6.setMinimumSize(new java.awt.Dimension(55, 25));
        value_tex6.setPreferredSize(new java.awt.Dimension(55, 25));
        value_tex6.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                value_tex6FocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 6;
        jPanel3.add(value_tex6, gridBagConstraints);

        value_tex8.setFont(new java.awt.Font("メイリオ", 0, 12)); // NOI18N
        value_tex8.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        value_tex8.setMinimumSize(new java.awt.Dimension(55, 25));
        value_tex8.setPreferredSize(new java.awt.Dimension(55, 25));
        value_tex8.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                value_tex8FocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 2;
        jPanel3.add(value_tex8, gridBagConstraints);

        value_tex9.setFont(new java.awt.Font("メイリオ", 0, 12)); // NOI18N
        value_tex9.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        value_tex9.setMinimumSize(new java.awt.Dimension(55, 25));
        value_tex9.setPreferredSize(new java.awt.Dimension(55, 25));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 3;
        jPanel3.add(value_tex9, gridBagConstraints);

        value_tex10.setFont(new java.awt.Font("メイリオ", 0, 12)); // NOI18N
        value_tex10.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        value_tex10.setMinimumSize(new java.awt.Dimension(55, 25));
        value_tex10.setPreferredSize(new java.awt.Dimension(55, 25));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 4;
        jPanel3.add(value_tex10, gridBagConstraints);

        value_tex11.setFont(new java.awt.Font("メイリオ", 0, 12)); // NOI18N
        value_tex11.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        value_tex11.setMinimumSize(new java.awt.Dimension(55, 25));
        value_tex11.setPreferredSize(new java.awt.Dimension(55, 25));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 5;
        jPanel3.add(value_tex11, gridBagConstraints);

        value_tex13.setFont(new java.awt.Font("メイリオ", 0, 12)); // NOI18N
        value_tex13.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        value_tex13.setMinimumSize(new java.awt.Dimension(60, 25));
        value_tex13.setPreferredSize(new java.awt.Dimension(60, 25));
        value_tex13.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                value_tex13FocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        jPanel3.add(value_tex13, gridBagConstraints);

        value_tex7.setFont(new java.awt.Font("メイリオ", 0, 12)); // NOI18N
        value_tex7.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        value_tex7.setMinimumSize(new java.awt.Dimension(55, 25));
        value_tex7.setPreferredSize(new java.awt.Dimension(55, 25));
        value_tex7.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                value_tex7FocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 1;
        jPanel3.add(value_tex7, gridBagConstraints);

        value_tex14.setFont(new java.awt.Font("メイリオ", 0, 12)); // NOI18N
        value_tex14.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        value_tex14.setMinimumSize(new java.awt.Dimension(60, 25));
        value_tex14.setPreferredSize(new java.awt.Dimension(60, 25));
        value_tex14.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                value_tex14FocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        jPanel3.add(value_tex14, gridBagConstraints);

        value_tex15.setFont(new java.awt.Font("メイリオ", 0, 12)); // NOI18N
        value_tex15.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        value_tex15.setMinimumSize(new java.awt.Dimension(60, 25));
        value_tex15.setPreferredSize(new java.awt.Dimension(60, 25));
        value_tex15.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                value_tex15FocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        jPanel3.add(value_tex15, gridBagConstraints);

        value_tex16.setFont(new java.awt.Font("メイリオ", 0, 12)); // NOI18N
        value_tex16.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        value_tex16.setMinimumSize(new java.awt.Dimension(60, 25));
        value_tex16.setPreferredSize(new java.awt.Dimension(60, 25));
        value_tex16.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                value_tex16FocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        jPanel3.add(value_tex16, gridBagConstraints);

        value_tex17.setFont(new java.awt.Font("メイリオ", 0, 12)); // NOI18N
        value_tex17.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        value_tex17.setMinimumSize(new java.awt.Dimension(60, 25));
        value_tex17.setPreferredSize(new java.awt.Dimension(60, 25));
        value_tex17.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                value_tex17FocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        jPanel3.add(value_tex17, gridBagConstraints);

        value_tex19.setFont(new java.awt.Font("メイリオ", 0, 12)); // NOI18N
        value_tex19.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        value_tex19.setMinimumSize(new java.awt.Dimension(55, 25));
        value_tex19.setPreferredSize(new java.awt.Dimension(55, 25));
        value_tex19.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                value_tex19FocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 7;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        jPanel3.add(value_tex19, gridBagConstraints);

        value_tex24.setFont(new java.awt.Font("メイリオ", 0, 12)); // NOI18N
        value_tex24.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        value_tex24.setMinimumSize(new java.awt.Dimension(55, 25));
        value_tex24.setPreferredSize(new java.awt.Dimension(55, 25));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 9;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        jPanel3.add(value_tex24, gridBagConstraints);

        value_tex12.setFont(new java.awt.Font("メイリオ", 0, 12)); // NOI18N
        value_tex12.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        value_tex12.setMinimumSize(new java.awt.Dimension(55, 25));
        value_tex12.setPreferredSize(new java.awt.Dimension(55, 25));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 6;
        jPanel3.add(value_tex12, gridBagConstraints);

        jLabel15.setBackground(new java.awt.Color(0, 102, 153));
        jLabel15.setFont(new java.awt.Font("メイリオ", 1, 14)); // NOI18N
        jLabel15.setForeground(new java.awt.Color(255, 255, 255));
        jLabel15.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel15.setText(" 管理型品目");
        jLabel15.setMaximumSize(new java.awt.Dimension(170, 20));
        jLabel15.setMinimumSize(new java.awt.Dimension(170, 20));
        jLabel15.setOpaque(true);
        jLabel15.setPreferredSize(new java.awt.Dimension(170, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 6;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        jPanel3.add(jLabel15, gridBagConstraints);

        jLabel17.setBackground(new java.awt.Color(0, 102, 153));
        jLabel17.setFont(new java.awt.Font("メイリオ", 1, 14)); // NOI18N
        jLabel17.setForeground(new java.awt.Color(255, 255, 255));
        jLabel17.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel17.setText("数量");
        jLabel17.setMinimumSize(new java.awt.Dimension(55, 20));
        jLabel17.setOpaque(true);
        jLabel17.setPreferredSize(new java.awt.Dimension(55, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 7;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        jPanel3.add(jLabel17, gridBagConstraints);

        value_tex20.setFont(new java.awt.Font("メイリオ", 0, 12)); // NOI18N
        value_tex20.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        value_tex20.setMinimumSize(new java.awt.Dimension(55, 25));
        value_tex20.setPreferredSize(new java.awt.Dimension(55, 25));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 7;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        jPanel3.add(value_tex20, gridBagConstraints);

        value_tex21.setFont(new java.awt.Font("メイリオ", 0, 12)); // NOI18N
        value_tex21.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        value_tex21.setMinimumSize(new java.awt.Dimension(55, 25));
        value_tex21.setPreferredSize(new java.awt.Dimension(55, 25));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 7;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        jPanel3.add(value_tex21, gridBagConstraints);

        value_tex22.setFont(new java.awt.Font("メイリオ", 0, 12)); // NOI18N
        value_tex22.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        value_tex22.setMinimumSize(new java.awt.Dimension(55, 25));
        value_tex22.setPreferredSize(new java.awt.Dimension(55, 25));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 7;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        jPanel3.add(value_tex22, gridBagConstraints);

        value_tex18.setFont(new java.awt.Font("メイリオ", 0, 12)); // NOI18N
        value_tex18.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        value_tex18.setMinimumSize(new java.awt.Dimension(60, 25));
        value_tex18.setPreferredSize(new java.awt.Dimension(60, 25));
        value_tex18.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                value_tex18FocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        jPanel3.add(value_tex18, gridBagConstraints);

        jLabel18.setBackground(new java.awt.Color(0, 102, 153));
        jLabel18.setFont(new java.awt.Font("メイリオ", 1, 14)); // NOI18N
        jLabel18.setForeground(new java.awt.Color(255, 255, 255));
        jLabel18.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel18.setText(" 特別管理産廃");
        jLabel18.setMinimumSize(new java.awt.Dimension(125, 20));
        jLabel18.setOpaque(true);
        jLabel18.setPreferredSize(new java.awt.Dimension(125, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 8;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        jPanel3.add(jLabel18, gridBagConstraints);

        jLabel19.setBackground(new java.awt.Color(0, 102, 153));
        jLabel19.setFont(new java.awt.Font("メイリオ", 1, 14)); // NOI18N
        jLabel19.setForeground(new java.awt.Color(255, 255, 255));
        jLabel19.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel19.setText("数量");
        jLabel19.setMinimumSize(new java.awt.Dimension(55, 20));
        jLabel19.setOpaque(true);
        jLabel19.setPreferredSize(new java.awt.Dimension(55, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 9;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        jPanel3.add(jLabel19, gridBagConstraints);

        value_tex25.setFont(new java.awt.Font("メイリオ", 0, 12)); // NOI18N
        value_tex25.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        value_tex25.setMinimumSize(new java.awt.Dimension(55, 25));
        value_tex25.setPreferredSize(new java.awt.Dimension(55, 25));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 9;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        jPanel3.add(value_tex25, gridBagConstraints);

        value_tex26.setFont(new java.awt.Font("メイリオ", 0, 12)); // NOI18N
        value_tex26.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        value_tex26.setMinimumSize(new java.awt.Dimension(55, 25));
        value_tex26.setPreferredSize(new java.awt.Dimension(55, 25));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 9;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        jPanel3.add(value_tex26, gridBagConstraints);

        value_tex23.setFont(new java.awt.Font("メイリオ", 0, 12)); // NOI18N
        value_tex23.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        value_tex23.setMinimumSize(new java.awt.Dimension(55, 25));
        value_tex23.setPreferredSize(new java.awt.Dimension(55, 25));
        value_tex23.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                value_tex23FocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 9;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        jPanel3.add(value_tex23, gridBagConstraints);

        value_tex28.setFont(new java.awt.Font("メイリオ", 0, 12)); // NOI18N
        value_tex28.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        value_tex28.setMinimumSize(new java.awt.Dimension(55, 25));
        value_tex28.setPreferredSize(new java.awt.Dimension(55, 25));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 9;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        jPanel3.add(value_tex28, gridBagConstraints);

        value_tex27.setFont(new java.awt.Font("メイリオ", 0, 12)); // NOI18N
        value_tex27.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        value_tex27.setMinimumSize(new java.awt.Dimension(55, 25));
        value_tex27.setPreferredSize(new java.awt.Dimension(55, 25));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 9;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        jPanel3.add(value_tex27, gridBagConstraints);

        jPanel15.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(153, 153, 255)));
        jPanel15.setMaximumSize(new java.awt.Dimension(217, 50));
        jPanel15.setMinimumSize(new java.awt.Dimension(217, 50));
        jPanel15.setPreferredSize(new java.awt.Dimension(217, 50));
        jPanel15.setLayout(new java.awt.GridBagLayout());

        jLabel16.setFont(new java.awt.Font("メイリオ", 0, 12)); // NOI18N
        jLabel16.setText(" 総重量又は総容量");
        jLabel16.setMaximumSize(new java.awt.Dimension(135, 22));
        jLabel16.setMinimumSize(new java.awt.Dimension(135, 22));
        jLabel16.setPreferredSize(new java.awt.Dimension(135, 22));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        jPanel15.add(jLabel16, gridBagConstraints);

        net_weight_field.setFont(new java.awt.Font("メイリオ", 0, 12)); // NOI18N
        net_weight_field.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        net_weight_field.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        net_weight_field.setMinimumSize(new java.awt.Dimension(80, 25));
        net_weight_field.setPreferredSize(new java.awt.Dimension(80, 25));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.SOUTHEAST;
        jPanel15.add(net_weight_field, gridBagConstraints);

        goods_f40.setFont(new java.awt.Font("メイリオ", 1, 12)); // NOI18N
        goods_f40.setText("重量値印字なし");
        goods_f40.setMaximumSize(new java.awt.Dimension(109, 22));
        goods_f40.setMinimumSize(new java.awt.Dimension(109, 22));
        goods_f40.setPreferredSize(new java.awt.Dimension(109, 22));
        goods_f40.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                goods_f40ItemStateChanged(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        jPanel15.add(goods_f40, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 6;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        jPanel3.add(jPanel15, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        jPanel21.add(jPanel3, gridBagConstraints);

        jPanel26.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel26.setMinimumSize(new java.awt.Dimension(1200, 160));
        jPanel26.setPreferredSize(new java.awt.Dimension(1200, 160));
        jPanel26.setLayout(new java.awt.GridBagLayout());

        jPanel22.setBorder(javax.swing.BorderFactory.createEtchedBorder(new java.awt.Color(0, 102, 153), null));
        jPanel22.setFont(new java.awt.Font("MS UI Gothic", 1, 12)); // NOI18N
        jPanel22.setMinimumSize(new java.awt.Dimension(400, 160));
        jPanel22.setPreferredSize(new java.awt.Dimension(400, 160));
        jPanel22.setRequestFocusEnabled(false);
        jPanel22.setLayout(new java.awt.GridBagLayout());

        jLabel38.setBackground(new java.awt.Color(0, 102, 153));
        jLabel38.setFont(new java.awt.Font("メイリオ", 1, 14)); // NOI18N
        jLabel38.setForeground(new java.awt.Color(255, 255, 255));
        jLabel38.setText("　運搬受託者(収集運搬業者)(1)");
        jLabel38.setMaximumSize(new java.awt.Dimension(110, 25));
        jLabel38.setMinimumSize(new java.awt.Dimension(110, 25));
        jLabel38.setOpaque(true);
        jLabel38.setPreferredSize(new java.awt.Dimension(110, 25));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        jPanel22.add(jLabel38, gridBagConstraints);

        check_f5.setFont(new java.awt.Font("メイリオ", 0, 12)); // NOI18N
        check_f5.setText("1.有");
        check_f5.setMaximumSize(new java.awt.Dimension(50, 20));
        check_f5.setMinimumSize(new java.awt.Dimension(50, 20));
        check_f5.setPreferredSize(new java.awt.Dimension(50, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        jPanel22.add(check_f5, gridBagConstraints);

        trans_cmb01.setFont(new java.awt.Font("メイリオ", 0, 12)); // NOI18N
        trans_cmb01.setMinimumSize(new java.awt.Dimension(120, 27));
        trans_cmb01.setPreferredSize(new java.awt.Dimension(120, 27));
        trans_cmb01.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                trans_cmb01ItemStateChanged(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 0, 2, 5);
        jPanel22.add(trans_cmb01, gridBagConstraints);

        jLabel42.setFont(new java.awt.Font("メイリオ", 0, 12)); // NOI18N
        jLabel42.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel42.setText("所在地");
        jLabel42.setMaximumSize(new java.awt.Dimension(85, 20));
        jLabel42.setMinimumSize(new java.awt.Dimension(85, 20));
        jLabel42.setPreferredSize(new java.awt.Dimension(85, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 5);
        jPanel22.add(jLabel42, gridBagConstraints);

        trans_zip01.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        trans_zip01.setText("999-9999");
        trans_zip01.setMaximumSize(new java.awt.Dimension(90, 25));
        trans_zip01.setMinimumSize(new java.awt.Dimension(90, 25));
        trans_zip01.setPreferredSize(new java.awt.Dimension(90, 25));
        trans_zip01.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                trans_zip01ActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        jPanel22.add(trans_zip01, gridBagConstraints);

        jLabel43.setFont(new java.awt.Font("メイリオ", 0, 12)); // NOI18N
        jLabel43.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel43.setText("電話番号");
        jLabel43.setMinimumSize(new java.awt.Dimension(70, 20));
        jLabel43.setPreferredSize(new java.awt.Dimension(70, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        jPanel22.add(jLabel43, gridBagConstraints);

        trans_tel01.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        trans_tel01.setMaximumSize(new java.awt.Dimension(140, 25));
        trans_tel01.setMinimumSize(new java.awt.Dimension(140, 25));
        trans_tel01.setPreferredSize(new java.awt.Dimension(140, 25));
        trans_tel01.setRequestFocusEnabled(false);
        trans_tel01.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                trans_tel01ActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.weightx = 0.25;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 5);
        jPanel22.add(trans_tel01, gridBagConstraints);

        trans_address01.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        trans_address01.setMinimumSize(new java.awt.Dimension(380, 25));
        trans_address01.setPreferredSize(new java.awt.Dimension(380, 25));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 0, 2, 5);
        jPanel22.add(trans_address01, gridBagConstraints);

        check_f6.setFont(new java.awt.Font("メイリオ", 0, 12)); // NOI18N
        check_f6.setText("2.無");
        check_f6.setMaximumSize(new java.awt.Dimension(70, 20));
        check_f6.setMinimumSize(new java.awt.Dimension(70, 20));
        check_f6.setPreferredSize(new java.awt.Dimension(70, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        jPanel22.add(check_f6, gridBagConstraints);

        jLabel36.setFont(new java.awt.Font("メイリオ", 0, 12)); // NOI18N
        jLabel36.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel36.setText("車種");
        jLabel36.setMinimumSize(new java.awt.Dimension(50, 20));
        jLabel36.setOpaque(true);
        jLabel36.setPreferredSize(new java.awt.Dimension(50, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        jPanel22.add(jLabel36, gridBagConstraints);

        jLabel37.setFont(new java.awt.Font("メイリオ", 0, 12)); // NOI18N
        jLabel37.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel37.setText("積替え･保管");
        jLabel37.setMaximumSize(new java.awt.Dimension(150, 20));
        jLabel37.setMinimumSize(new java.awt.Dimension(150, 20));
        jLabel37.setOpaque(true);
        jLabel37.setPreferredSize(new java.awt.Dimension(150, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        jPanel22.add(jLabel37, gridBagConstraints);

        jLabel12.setFont(new java.awt.Font("メイリオ", 0, 12)); // NOI18N
        jLabel12.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel12.setText("収集運搬車両番号");
        jLabel12.setMaximumSize(new java.awt.Dimension(100, 20));
        jLabel12.setMinimumSize(new java.awt.Dimension(100, 20));
        jLabel12.setOpaque(true);
        jLabel12.setPreferredSize(new java.awt.Dimension(100, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        jPanel22.add(jLabel12, gridBagConstraints);

        trans_car_no01.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        trans_car_no01.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        trans_car_no01.setMinimumSize(new java.awt.Dimension(95, 25));
        trans_car_no01.setPreferredSize(new java.awt.Dimension(95, 25));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.PAGE_START;
        jPanel22.add(trans_car_no01, gridBagConstraints);

        trans_car_kind01.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        trans_car_kind01.setMinimumSize(new java.awt.Dimension(120, 25));
        trans_car_kind01.setPreferredSize(new java.awt.Dimension(120, 25));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        jPanel22.add(trans_car_kind01, gridBagConstraints);

        運搬業者_print_f1.setFont(new java.awt.Font("メイリオ", 0, 12)); // NOI18N
        運搬業者_print_f1.setText("印字なし　");
        運搬業者_print_f1.setMinimumSize(new java.awt.Dimension(100, 20));
        運搬業者_print_f1.setPreferredSize(new java.awt.Dimension(100, 20));
        運搬業者_print_f1.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                運搬業者_print_f1ItemStateChanged(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        jPanel22.add(運搬業者_print_f1, gridBagConstraints);

        jLabel82.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        jLabel82.setText("ｺｰﾄﾞ");
        jLabel82.setMaximumSize(new java.awt.Dimension(30, 20));
        jLabel82.setMinimumSize(new java.awt.Dimension(30, 20));
        jLabel82.setPreferredSize(new java.awt.Dimension(30, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 2, 0, 2);
        jPanel22.add(jLabel82, gridBagConstraints);

        trans_search_.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        trans_search_.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        trans_search_.setMaximumSize(new java.awt.Dimension(60, 25));
        trans_search_.setMinimumSize(new java.awt.Dimension(60, 25));
        trans_search_.setPreferredSize(new java.awt.Dimension(60, 25));
        trans_search_.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                trans_search_FocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                trans_search_FocusLost(evt);
            }
        });
        trans_search_.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                trans_search_KeyPressed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        jPanel22.add(trans_search_, gridBagConstraints);

        jButton2.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        jButton2.setText("ｶﾅ検索");
        jButton2.setFocusable(false);
        jButton2.setMargin(new java.awt.Insets(0, 0, 0, 0));
        jButton2.setMaximumSize(new java.awt.Dimension(70, 25));
        jButton2.setMinimumSize(new java.awt.Dimension(70, 25));
        jButton2.setPreferredSize(new java.awt.Dimension(70, 25));
        jButton2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jButton2MouseClicked(evt);
            }
        });
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 0, 2, 2);
        jPanel22.add(jButton2, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        jPanel26.add(jPanel22, gridBagConstraints);

        jPanel24.setBorder(javax.swing.BorderFactory.createEtchedBorder(new java.awt.Color(0, 102, 153), null));
        jPanel24.setMinimumSize(new java.awt.Dimension(400, 160));
        jPanel24.setPreferredSize(new java.awt.Dimension(400, 160));
        jPanel24.setLayout(new java.awt.GridBagLayout());

        jLabel44.setBackground(new java.awt.Color(0, 102, 153));
        jLabel44.setFont(new java.awt.Font("メイリオ", 1, 14)); // NOI18N
        jLabel44.setForeground(new java.awt.Color(255, 255, 255));
        jLabel44.setText("　運搬先の事業所〔処分者の処理施設）");
        jLabel44.setMinimumSize(new java.awt.Dimension(330, 20));
        jLabel44.setOpaque(true);
        jLabel44.setPreferredSize(new java.awt.Dimension(330, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 5;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        jPanel24.add(jLabel44, gridBagConstraints);

        jLabel45.setFont(new java.awt.Font("メイリオ", 0, 12)); // NOI18N
        jLabel45.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel45.setText("名称");
        jLabel45.setMaximumSize(new java.awt.Dimension(60, 20));
        jLabel45.setMinimumSize(new java.awt.Dimension(60, 20));
        jLabel45.setPreferredSize(new java.awt.Dimension(60, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        jPanel24.add(jLabel45, gridBagConstraints);

        cont_office_name01.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        cont_office_name01.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "青森カシマ　ソフトウエア" }));
        cont_office_name01.setMaximumSize(new java.awt.Dimension(220, 27));
        cont_office_name01.setMinimumSize(new java.awt.Dimension(220, 27));
        cont_office_name01.setPreferredSize(new java.awt.Dimension(220, 27));
        cont_office_name01.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cont_office_name01ItemStateChanged(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(2, 0, 2, 15);
        jPanel24.add(cont_office_name01, gridBagConstraints);

        jLabel47.setFont(new java.awt.Font("メイリオ", 0, 12)); // NOI18N
        jLabel47.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel47.setText("所在地");
        jLabel47.setMinimumSize(new java.awt.Dimension(60, 20));
        jLabel47.setPreferredSize(new java.awt.Dimension(60, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        jPanel24.add(jLabel47, gridBagConstraints);

        cont_office_zip01.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        cont_office_zip01.setMinimumSize(new java.awt.Dimension(100, 25));
        cont_office_zip01.setPreferredSize(new java.awt.Dimension(100, 25));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 0, 2, 0);
        jPanel24.add(cont_office_zip01, gridBagConstraints);

        jLabel49.setFont(new java.awt.Font("メイリオ", 0, 12)); // NOI18N
        jLabel49.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel49.setText("電話番号");
        jLabel49.setMinimumSize(new java.awt.Dimension(70, 20));
        jLabel49.setPreferredSize(new java.awt.Dimension(70, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        jPanel24.add(jLabel49, gridBagConstraints);

        cont_office_tel01.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        cont_office_tel01.setMinimumSize(new java.awt.Dimension(125, 25));
        cont_office_tel01.setPreferredSize(new java.awt.Dimension(120, 25));
        cont_office_tel01.setRequestFocusEnabled(false);
        cont_office_tel01.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cont_office_tel01ActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 15);
        jPanel24.add(cont_office_tel01, gridBagConstraints);

        cont_office_address01.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        cont_office_address01.setMaximumSize(new java.awt.Dimension(340, 25));
        cont_office_address01.setMinimumSize(new java.awt.Dimension(340, 25));
        cont_office_address01.setPreferredSize(new java.awt.Dimension(340, 25));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = 5;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 15, 2, 15);
        jPanel24.add(cont_office_address01, gridBagConstraints);

        jPanel16.setMaximumSize(new java.awt.Dimension(100, 50));
        jPanel16.setMinimumSize(new java.awt.Dimension(100, 50));
        jPanel16.setPreferredSize(new java.awt.Dimension(100, 50));
        jPanel16.setLayout(new java.awt.GridBagLayout());

        jLabel50.setFont(new java.awt.Font("メイリオ", 0, 12)); // NOI18N
        jLabel50.setText("中間処理");
        jLabel50.setMaximumSize(new java.awt.Dimension(60, 20));
        jLabel50.setMinimumSize(new java.awt.Dimension(60, 20));
        jLabel50.setPreferredSize(new java.awt.Dimension(60, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        jPanel16.add(jLabel50, gridBagConstraints);

        jLabel51.setFont(new java.awt.Font("メイリオ", 0, 12)); // NOI18N
        jLabel51.setText("最終処分");
        jLabel51.setMaximumSize(new java.awt.Dimension(60, 20));
        jLabel51.setMinimumSize(new java.awt.Dimension(60, 20));
        jLabel51.setPreferredSize(new java.awt.Dimension(60, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        jPanel16.add(jLabel51, gridBagConstraints);

        check_f12.setFont(new java.awt.Font("メイリオ", 0, 12)); // NOI18N
        check_f12.setText("安定型");
        check_f12.setMaximumSize(new java.awt.Dimension(65, 20));
        check_f12.setMinimumSize(new java.awt.Dimension(65, 20));
        check_f12.setPreferredSize(new java.awt.Dimension(65, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        jPanel16.add(check_f12, gridBagConstraints);

        check_f9.setFont(new java.awt.Font("メイリオ", 0, 12)); // NOI18N
        check_f9.setText("脱水");
        check_f9.setMaximumSize(new java.awt.Dimension(65, 20));
        check_f9.setMinimumSize(new java.awt.Dimension(65, 20));
        check_f9.setPreferredSize(new java.awt.Dimension(65, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        jPanel16.add(check_f9, gridBagConstraints);

        check_f10.setFont(new java.awt.Font("メイリオ", 0, 12)); // NOI18N
        check_f10.setText("焼却");
        check_f10.setMaximumSize(new java.awt.Dimension(65, 20));
        check_f10.setMinimumSize(new java.awt.Dimension(65, 20));
        check_f10.setPreferredSize(new java.awt.Dimension(65, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        jPanel16.add(check_f10, gridBagConstraints);

        check_f13.setFont(new java.awt.Font("メイリオ", 0, 12)); // NOI18N
        check_f13.setText("管理型");
        check_f13.setMaximumSize(new java.awt.Dimension(65, 20));
        check_f13.setMinimumSize(new java.awt.Dimension(65, 20));
        check_f13.setPreferredSize(new java.awt.Dimension(65, 20));
        check_f13.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                check_f13ActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        jPanel16.add(check_f13, gridBagConstraints);

        check_f11.setFont(new java.awt.Font("メイリオ", 0, 12)); // NOI18N
        check_f11.setText("破砕");
        check_f11.setMaximumSize(new java.awt.Dimension(65, 20));
        check_f11.setMinimumSize(new java.awt.Dimension(65, 20));
        check_f11.setPreferredSize(new java.awt.Dimension(65, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        jPanel16.add(check_f11, gridBagConstraints);

        check_f14.setFont(new java.awt.Font("メイリオ", 0, 12)); // NOI18N
        check_f14.setText("遮断型");
        check_f14.setMaximumSize(new java.awt.Dimension(65, 20));
        check_f14.setMinimumSize(new java.awt.Dimension(65, 20));
        check_f14.setPreferredSize(new java.awt.Dimension(65, 20));
        check_f14.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                check_f14ActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        jPanel16.add(check_f14, gridBagConstraints);

        cmb中間処理.setMaximumSize(new java.awt.Dimension(100, 25));
        cmb中間処理.setMinimumSize(new java.awt.Dimension(100, 25));
        cmb中間処理.setPreferredSize(new java.awt.Dimension(100, 25));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 15);
        jPanel16.add(cmb中間処理, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.gridwidth = 5;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        jPanel24.add(jPanel16, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        jPanel26.add(jPanel24, gridBagConstraints);

        jPanel53.setBorder(javax.swing.BorderFactory.createEtchedBorder(new java.awt.Color(0, 102, 153), null));
        jPanel53.setFont(new java.awt.Font("MS UI Gothic", 1, 12)); // NOI18N
        jPanel53.setMinimumSize(new java.awt.Dimension(400, 160));
        jPanel53.setPreferredSize(new java.awt.Dimension(400, 160));
        jPanel53.setRequestFocusEnabled(false);
        jPanel53.setLayout(new java.awt.GridBagLayout());

        jLabel39.setBackground(new java.awt.Color(0, 102, 153));
        jLabel39.setFont(new java.awt.Font("メイリオ", 1, 14)); // NOI18N
        jLabel39.setForeground(new java.awt.Color(255, 255, 255));
        jLabel39.setText("　運搬受託者(収集運搬業者)(2)");
        jLabel39.setMaximumSize(new java.awt.Dimension(200, 20));
        jLabel39.setMinimumSize(new java.awt.Dimension(200, 20));
        jLabel39.setOpaque(true);
        jLabel39.setPreferredSize(new java.awt.Dimension(200, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        jPanel53.add(jLabel39, gridBagConstraints);

        slash_f2.setFont(new java.awt.Font("メイリオ", 0, 12)); // NOI18N
        slash_f2.setText("斜線有り");
        slash_f2.setMaximumSize(new java.awt.Dimension(80, 20));
        slash_f2.setMinimumSize(new java.awt.Dimension(80, 20));
        slash_f2.setPreferredSize(new java.awt.Dimension(80, 20));
        slash_f2.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                slash_f2ItemStateChanged(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(2, 0, 0, 0);
        jPanel53.add(slash_f2, gridBagConstraints);

        jPanel52.setFont(new java.awt.Font("MS UI Gothic", 1, 12)); // NOI18N
        jPanel52.setMinimumSize(new java.awt.Dimension(395, 135));
        jPanel52.setPreferredSize(new java.awt.Dimension(395, 135));
        jPanel52.setRequestFocusEnabled(false);
        jPanel52.setLayout(new java.awt.GridBagLayout());

        check_f7.setFont(new java.awt.Font("メイリオ", 0, 12)); // NOI18N
        check_f7.setText("1.有");
        check_f7.setMaximumSize(new java.awt.Dimension(50, 20));
        check_f7.setMinimumSize(new java.awt.Dimension(50, 20));
        check_f7.setPreferredSize(new java.awt.Dimension(50, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        gridBagConstraints.insets = new java.awt.Insets(0, 2, 0, 0);
        jPanel52.add(check_f7, gridBagConstraints);

        trans_cmb02.setFont(new java.awt.Font("メイリオ", 0, 12)); // NOI18N
        trans_cmb02.setMinimumSize(new java.awt.Dimension(110, 27));
        trans_cmb02.setPreferredSize(new java.awt.Dimension(110, 27));
        trans_cmb02.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                trans_cmb02ItemStateChanged(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 2, 2);
        jPanel52.add(trans_cmb02, gridBagConstraints);

        jLabel71.setFont(new java.awt.Font("メイリオ", 0, 12)); // NOI18N
        jLabel71.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel71.setText("　所在地");
        jLabel71.setMaximumSize(new java.awt.Dimension(80, 20));
        jLabel71.setMinimumSize(new java.awt.Dimension(80, 20));
        jLabel71.setPreferredSize(new java.awt.Dimension(80, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 5);
        jPanel52.add(jLabel71, gridBagConstraints);

        trans_zip02.setFont(new java.awt.Font("メイリオ", 0, 12)); // NOI18N
        trans_zip02.setMaximumSize(new java.awt.Dimension(85, 25));
        trans_zip02.setMinimumSize(new java.awt.Dimension(85, 25));
        trans_zip02.setPreferredSize(new java.awt.Dimension(85, 25));
        trans_zip02.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                trans_zip02ActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        jPanel52.add(trans_zip02, gridBagConstraints);

        jLabel79.setFont(new java.awt.Font("メイリオ", 0, 12)); // NOI18N
        jLabel79.setText("電話番号");
        jLabel79.setMinimumSize(new java.awt.Dimension(70, 20));
        jLabel79.setPreferredSize(new java.awt.Dimension(70, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        jPanel52.add(jLabel79, gridBagConstraints);

        trans_tel02.setFont(new java.awt.Font("メイリオ", 0, 12)); // NOI18N
        trans_tel02.setMinimumSize(new java.awt.Dimension(120, 25));
        trans_tel02.setPreferredSize(new java.awt.Dimension(120, 25));
        trans_tel02.setRequestFocusEnabled(false);
        trans_tel02.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                trans_tel02ActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.SOUTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 2);
        jPanel52.add(trans_tel02, gridBagConstraints);

        trans_address02.setFont(new java.awt.Font("メイリオ", 0, 12)); // NOI18N
        trans_address02.setMinimumSize(new java.awt.Dimension(380, 25));
        trans_address02.setPreferredSize(new java.awt.Dimension(380, 25));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 0, 2, 2);
        jPanel52.add(trans_address02, gridBagConstraints);

        check_f8.setFont(new java.awt.Font("メイリオ", 0, 12)); // NOI18N
        check_f8.setText("2.無");
        check_f8.setMaximumSize(new java.awt.Dimension(49, 20));
        check_f8.setMinimumSize(new java.awt.Dimension(55, 20));
        check_f8.setPreferredSize(new java.awt.Dimension(55, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        jPanel52.add(check_f8, gridBagConstraints);

        jLabel80.setFont(new java.awt.Font("メイリオ", 0, 12)); // NOI18N
        jLabel80.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel80.setText("車種");
        jLabel80.setMinimumSize(new java.awt.Dimension(50, 20));
        jLabel80.setOpaque(true);
        jLabel80.setPreferredSize(new java.awt.Dimension(50, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        jPanel52.add(jLabel80, gridBagConstraints);

        jLabel81.setFont(new java.awt.Font("メイリオ", 0, 12)); // NOI18N
        jLabel81.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel81.setText("積替え･保管");
        jLabel81.setMaximumSize(new java.awt.Dimension(150, 20));
        jLabel81.setMinimumSize(new java.awt.Dimension(150, 20));
        jLabel81.setOpaque(true);
        jLabel81.setPreferredSize(new java.awt.Dimension(150, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        jPanel52.add(jLabel81, gridBagConstraints);

        jLabel22.setFont(new java.awt.Font("メイリオ", 0, 12)); // NOI18N
        jLabel22.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel22.setText("収集運搬車両番号");
        jLabel22.setMaximumSize(new java.awt.Dimension(100, 20));
        jLabel22.setMinimumSize(new java.awt.Dimension(100, 20));
        jLabel22.setOpaque(true);
        jLabel22.setPreferredSize(new java.awt.Dimension(100, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        jPanel52.add(jLabel22, gridBagConstraints);

        trans_car_no02.setFont(new java.awt.Font("メイリオ", 0, 12)); // NOI18N
        trans_car_no02.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        trans_car_no02.setMinimumSize(new java.awt.Dimension(95, 25));
        trans_car_no02.setPreferredSize(new java.awt.Dimension(95, 25));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.PAGE_START;
        jPanel52.add(trans_car_no02, gridBagConstraints);

        trans_car_kind02.setFont(new java.awt.Font("メイリオ", 0, 12)); // NOI18N
        trans_car_kind02.setMinimumSize(new java.awt.Dimension(120, 25));
        trans_car_kind02.setPreferredSize(new java.awt.Dimension(120, 25));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        jPanel52.add(trans_car_kind02, gridBagConstraints);

        jLabel126.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        jLabel126.setText("ｺｰﾄﾞ");
        jLabel126.setMaximumSize(new java.awt.Dimension(30, 20));
        jLabel126.setMinimumSize(new java.awt.Dimension(30, 20));
        jLabel126.setPreferredSize(new java.awt.Dimension(30, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 2);
        jPanel52.add(jLabel126, gridBagConstraints);

        trans_search_1.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        trans_search_1.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        trans_search_1.setMaximumSize(new java.awt.Dimension(60, 25));
        trans_search_1.setMinimumSize(new java.awt.Dimension(60, 25));
        trans_search_1.setPreferredSize(new java.awt.Dimension(60, 25));
        trans_search_1.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                trans_search_1FocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                trans_search_1FocusLost(evt);
            }
        });
        trans_search_1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                trans_search_1KeyPressed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 75);
        jPanel52.add(trans_search_1, gridBagConstraints);

        jButton3.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        jButton3.setText("ｶﾅ検索");
        jButton3.setFocusable(false);
        jButton3.setMargin(new java.awt.Insets(0, 0, 0, 0));
        jButton3.setMaximumSize(new java.awt.Dimension(70, 25));
        jButton3.setMinimumSize(new java.awt.Dimension(70, 25));
        jButton3.setPreferredSize(new java.awt.Dimension(70, 25));
        jButton3.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jButton3MouseClicked(evt);
            }
        });
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 0, 2, 2);
        jPanel52.add(jButton3, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        jPanel53.add(jPanel52, gridBagConstraints);

        運搬業者_print_f2.setFont(new java.awt.Font("メイリオ", 0, 12)); // NOI18N
        運搬業者_print_f2.setLabel("印字なし");
        運搬業者_print_f2.setMaximumSize(new java.awt.Dimension(80, 20));
        運搬業者_print_f2.setMinimumSize(new java.awt.Dimension(80, 20));
        運搬業者_print_f2.setPreferredSize(new java.awt.Dimension(80, 20));
        運搬業者_print_f2.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                運搬業者_print_f2ItemStateChanged(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        jPanel53.add(運搬業者_print_f2, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        jPanel26.add(jPanel53, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        jPanel21.add(jPanel26, gridBagConstraints);

        jPanel2.setBorder(javax.swing.BorderFactory.createEtchedBorder(new java.awt.Color(0, 102, 153), null));
        jPanel2.setMinimumSize(new java.awt.Dimension(1200, 35));
        jPanel2.setPreferredSize(new java.awt.Dimension(1200, 35));
        jPanel2.setLayout(new java.awt.GridBagLayout());

        jLabel91.setBackground(new java.awt.Color(0, 102, 153));
        jLabel91.setFont(new java.awt.Font("メイリオ", 1, 14)); // NOI18N
        jLabel91.setForeground(new java.awt.Color(255, 255, 255));
        jLabel91.setMaximumSize(new java.awt.Dimension(150, 30));
        jLabel91.setMinimumSize(new java.awt.Dimension(150, 30));
        jLabel91.setOpaque(true);
        jLabel91.setPreferredSize(new java.awt.Dimension(150, 30));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        jPanel2.add(jLabel91, gridBagConstraints);

        btn建設パターン2.setFont(new java.awt.Font("メイリオ", 0, 18)); // NOI18N
        btn建設パターン2.setText("建設パターン②");
        btn建設パターン2.setMaximumSize(new java.awt.Dimension(100, 30));
        btn建設パターン2.setMinimumSize(new java.awt.Dimension(100, 30));
        btn建設パターン2.setPreferredSize(new java.awt.Dimension(160, 30));
        btn建設パターン2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn建設パターン2ActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        jPanel2.add(btn建設パターン2, gridBagConstraints);

        btn建設パターン1.setFont(new java.awt.Font("メイリオ", 0, 18)); // NOI18N
        btn建設パターン1.setText("建設パターン①");
        btn建設パターン1.setMaximumSize(new java.awt.Dimension(160, 30));
        btn建設パターン1.setMinimumSize(new java.awt.Dimension(160, 30));
        btn建設パターン1.setPreferredSize(new java.awt.Dimension(160, 30));
        btn建設パターン1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                btn建設パターン1MousePressed(evt);
            }
        });
        btn建設パターン1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn建設パターン1ActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        jPanel2.add(btn建設パターン1, gridBagConstraints);

        jPanel23.setPreferredSize(new java.awt.Dimension(5, 5));

        javax.swing.GroupLayout jPanel23Layout = new javax.swing.GroupLayout(jPanel23);
        jPanel23.setLayout(jPanel23Layout);
        jPanel23Layout.setHorizontalGroup(
            jPanel23Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        jPanel23Layout.setVerticalGroup(
            jPanel23Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 7;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.weightx = 1.0;
        jPanel2.add(jPanel23, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 11;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        jPanel21.add(jPanel2, gridBagConstraints);

        jScrollPane2.setViewportView(jPanel21);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        add(jScrollPane2, gridBagConstraints);
    }// </editor-fold>//GEN-END:initComponents

    private void trans_tel01ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_trans_tel01ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_trans_tel01ActionPerformed

    private void cont_office_tel01ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cont_office_tel01ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cont_office_tel01ActionPerformed

    private void check_f13ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_check_f13ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_check_f13ActionPerformed

    private void cont_tel01ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cont_tel01ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cont_tel01ActionPerformed

    private void grant_yearActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_grant_yearActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_grant_yearActionPerformed

    private void grant_monthActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_grant_monthActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_grant_monthActionPerformed

    private void check_f14ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_check_f14ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_check_f14ActionPerformed

    private void quit_btn_kensetuMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_quit_btn_kensetuMousePressed
        // TODO add your handling code here:
//        doClose();
    }//GEN-LAST:event_quit_btn_kensetuMousePressed

    private void print_btn_kensetuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_print_btn_kensetuActionPerformed
        update();
        for (int cnt = 0; cnt < (Integer) pring_num_.getValue(); cnt++) {
            //印刷処理
            new XlsxManifesto(XlsxManifesto.MANIFESTO_REPORT1).print(con_, XlsxManifesto.MANIFESTO_REPORT1, sno_);

        }
    }//GEN-LAST:event_print_btn_kensetuActionPerformed

    private void rejist_btn_kensetuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rejist_btn_kensetuActionPerformed
        if (update()) {
            //正常終了
            doClose();
        }
    }//GEN-LAST:event_rejist_btn_kensetuActionPerformed

    private void office_tel01ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_office_tel01ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_office_tel01ActionPerformed

    private void trans_zip01ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_trans_zip01ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_trans_zip01ActionPerformed

    private void trans_zip02ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_trans_zip02ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_trans_zip02ActionPerformed

    private void trans_tel02ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_trans_tel02ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_trans_tel02ActionPerformed

    private void cont_tel02ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cont_tel02ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cont_tel02ActionPerformed

    private void ent_print_f() {
        //排出業者印字F
        ent_name.setEditable(!排出事業者_print_f.isSelected());
        ent_zip01.setEditable(!排出事業者_print_f.isSelected());
        ent_tel01.setEditable(!排出事業者_print_f.isSelected());
        ent_address01.setEditable(!排出事業者_print_f.isSelected());
        ent_name.setEnabled(!排出事業者_print_f.isSelected());
        ent_zip01.setEnabled(!排出事業者_print_f.isSelected());
        ent_tel01.setEnabled(!排出事業者_print_f.isSelected());
        ent_address01.setEnabled(!排出事業者_print_f.isSelected());
    }

    private void 排出事業者_print_fItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_排出事業者_print_fItemStateChanged
        // TODO add your handling code here:
        ent_print_f();
    }//GEN-LAST:event_排出事業者_print_fItemStateChanged

    private void 事業場_print_fItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_事業場_print_fItemStateChanged
        // TODO add your handling code here:
        office_print_f();
    }//GEN-LAST:event_事業場_print_fItemStateChanged

    private void office_print_f() {
        //排出事業場印字F
        office_name_.setEditable(!事業場_print_f.isSelected());
        office_zip01.setEditable(!事業場_print_f.isSelected());
        office_tel01.setEditable(!事業場_print_f.isSelected());
        office_address01.setEditable(!事業場_print_f.isSelected());
        office_sub_name_.setEditable(!事業場_print_f.isSelected());
        office_name_.setEnabled(!事業場_print_f.isSelected());
        office_zip01.setEnabled(!事業場_print_f.isSelected());
        office_tel01.setEnabled(!事業場_print_f.isSelected());
        office_address01.setEnabled(!事業場_print_f.isSelected());
        office_sub_name_.setEnabled(!事業場_print_f.isSelected());

    }
    private void 運搬業者_print_f1ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_運搬業者_print_f1ItemStateChanged
        // TODO add your handling code here:
        trans_print_f1();
    }//GEN-LAST:event_運搬業者_print_f1ItemStateChanged

    private void trans_print_f1() {
        //運搬業者印字F
        trans_zip01.setEditable(!運搬業者_print_f1.isSelected());
        trans_tel01.setEditable(!運搬業者_print_f1.isSelected());
        trans_address01.setEditable(!運搬業者_print_f1.isSelected());
        trans_car_no01.setEditable(!運搬業者_print_f1.isSelected());
        trans_cmb01.setEnabled(!運搬業者_print_f1.isSelected());
        trans_zip01.setEnabled(!運搬業者_print_f1.isSelected());
        trans_tel01.setEnabled(!運搬業者_print_f1.isSelected());
        trans_address01.setEnabled(!運搬業者_print_f1.isSelected());
        check_f5.setEnabled(!運搬業者_print_f1.isSelected());
        check_f6.setEnabled(!運搬業者_print_f1.isSelected());
        trans_car_no01.setEnabled(!運搬業者_print_f1.isSelected());
        trans_car_kind01.setEnabled(!運搬業者_print_f1.isSelected());
    }

    private void trans_print_f2() {
        //運搬業者印字F
        trans_zip02.setEditable(!運搬業者_print_f2.isSelected());
        trans_tel02.setEditable(!運搬業者_print_f2.isSelected());
        trans_address02.setEditable(!運搬業者_print_f2.isSelected());
        trans_car_no02.setEditable(!運搬業者_print_f2.isSelected());
        trans_cmb02.setEnabled(!運搬業者_print_f2.isSelected());
        trans_zip02.setEnabled(!運搬業者_print_f2.isSelected());
        trans_tel02.setEnabled(!運搬業者_print_f2.isSelected());
        trans_address02.setEnabled(!運搬業者_print_f2.isSelected());
        check_f7.setEnabled(!運搬業者_print_f2.isSelected());
        check_f8.setEnabled(!運搬業者_print_f2.isSelected());
        trans_car_no02.setEnabled(!運搬業者_print_f2.isSelected());
        trans_car_kind02.setEnabled(!運搬業者_print_f2.isSelected());
    }

    private void 運搬業者_print_f2ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_運搬業者_print_f2ItemStateChanged
        // TODO add your handling code here:
        trans_print_f2();
    }//GEN-LAST:event_運搬業者_print_f2ItemStateChanged

    private void 処分業者_print_f1ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_処分業者_print_f1ItemStateChanged
        // TODO add your handling code here:
        cont_print_f1();
    }//GEN-LAST:event_処分業者_print_f1ItemStateChanged

    private void cont_print_f1() {
        //処分業者名選択（処分場）印字F
        cont_zip01.setEditable(!処分業者_print_f1.isSelected());
        cont_tel01.setEditable(!処分業者_print_f1.isSelected());
        cont_address01.setEditable(!処分業者_print_f1.isSelected());
        cont_cmb01.setEnabled(!処分業者_print_f1.isSelected());
        cont_zip01.setEnabled(!処分業者_print_f1.isSelected());
        cont_tel01.setEnabled(!処分業者_print_f1.isSelected());
        cont_address01.setEnabled(!処分業者_print_f1.isSelected());
    }

    private void tsumikae_f5() {
        //積み替え印字F
        cont_zip02.setEditable(!積み替え_f5.isSelected());
        cont_tel02.setEditable(!積み替え_f5.isSelected());
        cont_address02.setEditable(!積み替え_f5.isSelected());
        trans1_weight.setEditable(!積み替え_f5.isSelected());
        cont_zip02.setEnabled(!積み替え_f5.isSelected());
        cont_tel02.setEnabled(!積み替え_f5.isSelected());
        cont_address02.setEnabled(!積み替え_f5.isSelected());
        check_f15.setEnabled(!積み替え_f5.isSelected());
        check_f16.setEnabled(!積み替え_f5.isSelected());
        check_f17.setEnabled(!積み替え_f5.isSelected());
        check_f18.setEnabled(!積み替え_f5.isSelected());
        trans1_weight.setEnabled(!積み替え_f5.isSelected());
        保管場所_cmb.setEnabled(!積み替え_f5.isSelected());
    }

    private void 積み替え_f5ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_積み替え_f5ItemStateChanged
        // TODO add your handling code here:
        //積み替え印字F
        tsumikae_f5();

    }//GEN-LAST:event_積み替え_f5ItemStateChanged

    private void trans_cmb01ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_trans_cmb01ItemStateChanged
        // TODO add your handling code here:
        //運搬業者名選択
        trans_zip01.setText((String) 運搬業者名2Array[2][trans_cmb01.getSelectedIndex()]);
        trans_tel01.setText((String) 運搬業者名2Array[3][trans_cmb01.getSelectedIndex()]);
        trans_address01.setText((String) 運搬業者名2Array[4][trans_cmb01.getSelectedIndex()]
                 + (String) 運搬業者名2Array[5][trans_cmb01.getSelectedIndex()]);
        trans_office1.setText(trans_cmb01.getSelectedItem().toString());
        trans_search_.setText((String) 運搬業者名2Array[0][trans_cmb01.getSelectedIndex()]);
        /////////////////////////////////////////////////////////////////////
        int search_id = Integer.valueOf(trans_search_.getText());
        mSQLCommon.GetMasterDataTransTantou("v0_trans_tantou", 運搬担当者Array1, search_id);
        if (運搬担当者Array1[0].length > 0) {
            String[] TransTantouID1 = new String[運搬担当者Array1[0].length];
            for (int i = 0; i < 運搬担当者Array1[0].length; i++) {
                TransTantouID1[i] = (String) 運搬担当者Array1[3][i].toString();
            }
            trans_tantou_id_cmb1.setModel(new javax.swing.DefaultComboBoxModel(TransTantouID1));
        }
    }//GEN-LAST:event_trans_cmb01ItemStateChanged

    private void trans_cmb02ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_trans_cmb02ItemStateChanged
        // TODO add your handling code here:
        //運搬業者名選択
        trans_zip02.setText((String) 運搬業者名2Array[2][trans_cmb02.getSelectedIndex()]);
        trans_tel02.setText((String) 運搬業者名2Array[3][trans_cmb02.getSelectedIndex()]);
        trans_address02.setText((String) 運搬業者名2Array[4][trans_cmb02.getSelectedIndex()]
                 + (String) 運搬業者名2Array[5][trans_cmb02.getSelectedIndex()]);
        trans_office2.setText(trans_cmb02.getSelectedItem().toString());
        trans_search_1.setText((String) 運搬業者名2Array[0][trans_cmb02.getSelectedIndex()]);
        int search_id = Integer.valueOf(trans_search_1.getText());
        mSQLCommon.GetMasterDataTransTantou("v0_trans_tantou", 運搬担当者Array2, search_id);
        if (運搬担当者Array2[0].length > 0) {
            String[] TransTantouID2 = new String[運搬担当者Array2[0].length];
            for (int i = 0; i < 運搬担当者Array2[0].length; i++) {
                TransTantouID2[i] = (String) 運搬担当者Array2[3][i].toString();
            }
            trans_tantou_id_cmb2.setModel(new javax.swing.DefaultComboBoxModel(TransTantouID2));
        }
    }//GEN-LAST:event_trans_cmb02ItemStateChanged

    private void cont_cmb01ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cont_cmb01ItemStateChanged
        // TODO add your handling code here:
        //処分業者名選択（処分場）
        cont_zip01.setText((String) 処分業者名Array[2][cont_cmb01.getSelectedIndex()]);
        cont_tel01.setText((String) 処分業者名Array[3][cont_cmb01.getSelectedIndex()]);
        cont_address01.setText((String) 処分業者名Array[4][cont_cmb01.getSelectedIndex()]
                 + (String) 処分業者名Array[5][cont_cmb01.getSelectedIndex()]);
        cont_name1.setText((String) 処分業者名Array[13][cont_cmb01.getSelectedIndex()]);//cont_tantou 20151010 担当者追加　処分担当者セット
        cont_name2.setText((String) 処分業者名Array[13][cont_cmb01.getSelectedIndex()]);
        最終処分担当者.setText((String) 処分業者名Array[14][cont_cmb01.getSelectedIndex()]);//last_tantou 20151019 最終処分担当者追加　最終処分担当者セット

        cont_search_.setText((String) 処分業者名Array[0][cont_cmb01.getSelectedIndex()]);
        //処分場
        cont_id_ = Integer.valueOf((String) 処分業者名Array[0][cont_cmb01.getSelectedIndex()]);
        //運搬先の事業場コンボ内をリフレッシュ
        mSQLCommon.GetMasterData4_1("v0_manifesto_3", 運搬事業場Array, cont_id_);
        String[] ContOfficeName = new String[運搬事業場Array[0].length];
        String[] LastContName = new String[運搬事業場Array[0].length];
        for (int i = 0; i <= 運搬事業場Array[0].length - 1; i++) {
            ContOfficeName[i] = (String) 運搬事業場Array[1][i];
            LastContName[i] = (String) 運搬事業場Array[1][i] + "　"
                     + (String) 運搬事業場Array[2][i] + (String) 運搬事業場Array[3][i];
            if (cont_office_id_ == Integer.valueOf((String) 運搬事業場Array[0][i])) {
                cont_office_id_index = i;
            }
        }
        cont_office_name01.setModel(new javax.swing.DefaultComboBoxModel(ContOfficeName));
        last_cont_cmb.setModel(new javax.swing.DefaultComboBoxModel(LastContName));
        //運搬先の事業場情報をリセット
        cont_office_zip01.setText("");
        cont_office_tel01.setText("");
        cont_office_address01.setText("");
        if (運搬事業場Array[0].length > 1 && cont_office_id_ == 0) {
            cont_office_id_ = 1;
        }

        cont_office1.setText(cont_cmb01.getSelectedItem().toString());
        cont_office2.setText(cont_cmb01.getSelectedItem().toString());
        最終処分社名.setText(cont_cmb01.getSelectedItem().toString());
    }//GEN-LAST:event_cont_cmb01ItemStateChanged

    private void cont_office_name01ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cont_office_name01ItemStateChanged
        cont_office_zip01.setText((String) 運搬事業場Array[4][cont_office_name01.getSelectedIndex()]);
        cont_office_tel01.setText((String) 運搬事業場Array[5][cont_office_name01.getSelectedIndex()]);
        cont_office_address01.setText((String) 運搬事業場Array[2][cont_office_name01.getSelectedIndex()]
                 + (String) 運搬事業場Array[3][cont_office_name01.getSelectedIndex()]);
    }//GEN-LAST:event_cont_office_name01ItemStateChanged

    private void 運搬の受託_print_f3ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_運搬の受託_print_f3ItemStateChanged
        // TODO add your handling code here:
        trans_print_f3();

    }//GEN-LAST:event_運搬の受託_print_f3ItemStateChanged

    private void trans_print_f3() {

        trans_office1.setEnabled(!運搬の受託_print_f3.isSelected());
        trans_name1.setEnabled(!運搬の受託_print_f3.isSelected());
        trans_year1.setEnabled(!運搬の受託_print_f3.isSelected());
        trans_month1.setEnabled(!運搬の受託_print_f3.isSelected());
        trans_day1.setEnabled(!運搬の受託_print_f3.isSelected());
        trans_tantou_id_cmb1.setEnabled(!運搬の受託_print_f3.isSelected());
        trans_tantou_search_1.setEnabled(!運搬の受託_print_f3.isSelected());
    }

    private void 処分の受託_print_f2ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_処分の受託_print_f2ItemStateChanged
        cont_print_f2();

    }//GEN-LAST:event_処分の受託_print_f2ItemStateChanged

    private void cont_print_f2() {

        cont_office1.setEditable(!処分の受託_print_f2.isSelected());
        cont_name1.setEditable(!処分の受託_print_f2.isSelected());
        cont_year1.setEditable(!処分の受託_print_f2.isSelected());
        cont_month1.setEditable(!処分の受託_print_f2.isSelected());
        cont_day1.setEditable(!処分の受託_print_f2.isSelected());
        cont_office1.setEnabled(!処分の受託_print_f2.isSelected());
        cont_name1.setEnabled(!処分の受託_print_f2.isSelected());
        cont_year1.setEnabled(!処分の受託_print_f2.isSelected());
        cont_month1.setEnabled(!処分の受託_print_f2.isSelected());
        cont_day1.setEnabled(!処分の受託_print_f2.isSelected());
    }
    private void 処分の受託_print_f3ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_処分の受託_print_f3ItemStateChanged
        // TODO add your handling code here:
        cont_print_f3();
    }//GEN-LAST:event_処分の受託_print_f3ItemStateChanged

    private void cont_print_f3() {

        cont_office2.setEditable(!処分の受託_print_f3.isSelected());
        cont_name2.setEditable(!処分の受託_print_f3.isSelected());
        cont_year2.setEditable(!処分の受託_print_f3.isSelected());
        cont_month2.setEditable(!処分の受託_print_f3.isSelected());
        cont_day2.setEditable(!処分の受託_print_f3.isSelected());
        cont_office2.setEnabled(!処分の受託_print_f3.isSelected());
        cont_name2.setEnabled(!処分の受託_print_f3.isSelected());
        cont_year2.setEnabled(!処分の受託_print_f3.isSelected());
        cont_month2.setEnabled(!処分の受託_print_f3.isSelected());
        cont_day2.setEnabled(!処分の受託_print_f3.isSelected());
    }
    private void quit_btn_kensetuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_quit_btn_kensetuActionPerformed
        // TODO add your handling code here:
        doClose();
    }//GEN-LAST:event_quit_btn_kensetuActionPerformed

    private void slash_f1ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_slash_f1ItemStateChanged
        // TODO add your handling code here:
        if (slash_f1.isSelected()) {
            check_f1.setVisible(false);
            check_f2.setVisible(false);
            MyLine1.setVisible(true);

        } else {
            check_f1.setVisible(true);
            check_f2.setVisible(true);
            MyLine1.setVisible(false);
        }
    }//GEN-LAST:event_slash_f1ItemStateChanged

    private void slash_f2ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_slash_f2ItemStateChanged
        // TODO add your handling code here:
        if (slash_f2.isSelected()) {
            jPanel52.setVisible(false);
            MyLine2.setVisible(true);
            jPanel55.setVisible(false);
            MyLine3.setVisible(true);

        } else {
            jPanel52.setVisible(true);
            MyLine2.setVisible(false);
            jPanel55.setVisible(true);
            MyLine3.setVisible(false);
        }
    }//GEN-LAST:event_slash_f2ItemStateChanged

    private void trans_print_f4() {
        trans_office2.setEnabled(!運搬の受託_print_f4.isSelected());
        trans_name2.setEnabled(!運搬の受託_print_f4.isSelected());
        trans_year2.setEnabled(!運搬の受託_print_f4.isSelected());
        trans_month2.setEnabled(!運搬の受託_print_f4.isSelected());
        trans_day2.setEnabled(!運搬の受託_print_f4.isSelected());
        trans_tantou_id_cmb2.setEnabled(!運搬の受託_print_f4.isSelected());
        trans_tantou_search_2.setEnabled(!運搬の受託_print_f4.isSelected());
    }
    private void 運搬の受託_print_f4ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_運搬の受託_print_f4ItemStateChanged
        // TODO add your handling code here:
        trans_print_f4();
    }//GEN-LAST:event_運搬の受託_print_f4ItemStateChanged

    private void slash_f3ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_slash_f3ItemStateChanged
        // TODO add your handling code here:
        if (slash_f3.isSelected()) {
            jPanel58.setVisible(false);
            MyLine5.setVisible(true);

        } else {
            jPanel58.setVisible(true);
            MyLine5.setVisible(false);
        }
    }//GEN-LAST:event_slash_f3ItemStateChanged

    private void grant_yearKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_grant_yearKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            grant_year.transferFocus();
            evt.consume();
        }
    }//GEN-LAST:event_grant_yearKeyPressed

    private void grant_monthKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_grant_monthKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            grant_month.transferFocus();
            evt.consume();
        }
    }//GEN-LAST:event_grant_monthKeyPressed

    private void grant_dayKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_grant_dayKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            grant_day.transferFocus();
            evt.consume();
        }
    }//GEN-LAST:event_grant_dayKeyPressed

    private void cont_year1KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cont_year1KeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            cont_year1.transferFocus();
            evt.consume();
        }
    }//GEN-LAST:event_cont_year1KeyPressed

    private void cont_month1KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cont_month1KeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            cont_month1.transferFocus();
            evt.consume();
        }
    }//GEN-LAST:event_cont_month1KeyPressed

    private void cont_day1KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cont_day1KeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            cont_day1.transferFocus();
            evt.consume();
        }
    }//GEN-LAST:event_cont_day1KeyPressed

    private void trans_year1KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_trans_year1KeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            trans_year1.transferFocus();
            evt.consume();
        }
    }//GEN-LAST:event_trans_year1KeyPressed

    private void trans_month1KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_trans_month1KeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            trans_month1.transferFocus();
            evt.consume();
        }
    }//GEN-LAST:event_trans_month1KeyPressed

    private void trans_day1KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_trans_day1KeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            trans_day1.transferFocus();
            evt.consume();
        }
    }//GEN-LAST:event_trans_day1KeyPressed

    private void trans_year2KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_trans_year2KeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            trans_year2.transferFocus();
            evt.consume();
        }
    }//GEN-LAST:event_trans_year2KeyPressed

    private void trans_month2KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_trans_month2KeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            trans_month2.transferFocus();
            evt.consume();
        }
    }//GEN-LAST:event_trans_month2KeyPressed

    private void trans_day2KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_trans_day2KeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            trans_day2.transferFocus();
            evt.consume();
        }
    }//GEN-LAST:event_trans_day2KeyPressed

    private void cont_year2KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cont_year2KeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            cont_year2.transferFocus();
            evt.consume();
        }
    }//GEN-LAST:event_cont_year2KeyPressed

    private void cont_month2KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cont_month2KeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            cont_month2.transferFocus();
            evt.consume();
        }
    }//GEN-LAST:event_cont_month2KeyPressed

    private void cont_day2KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cont_day2KeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            cont_day2.transferFocus();
            evt.consume();
        }
    }//GEN-LAST:event_cont_day2KeyPressed

    private void cont_date2_monthKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cont_date2_monthKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            cont_date2_month.transferFocus();
            evt.consume();
        }
    }//GEN-LAST:event_cont_date2_monthKeyPressed

    private void cont_date2_dayKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cont_date2_dayKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            cont_date2_day.transferFocus();
            evt.consume();
        }
    }//GEN-LAST:event_cont_date2_dayKeyPressed

    private void goods_f40ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_goods_f40ItemStateChanged
        // TODO add your handling code here:
        weight_print_check();
    }//GEN-LAST:event_goods_f40ItemStateChanged

    private void office_sub_name_ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_office_sub_name_ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_office_sub_name_ActionPerformed

    private void check_f4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_check_f4ActionPerformed
        // TODO add your handling code here:
        //当欄記載の通りにチェックで『中央公害清掃㈱松山事業所』を表示する
        if (check_f4.isSelected() && !(check_f3.isSelected())) {
            jlbl_final_name_.setVisible(check_f4.isSelected());
        } else {
            jlbl_final_name_.setVisible(false);
        }
    }//GEN-LAST:event_check_f4ActionPerformed

    private void goods_f03ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_goods_f03ItemStateChanged

    }//GEN-LAST:event_goods_f03ItemStateChanged

    private void goods_f01ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_goods_f01ItemStateChanged
        //20260317 add
        if (goods_f01.isSelected()) {
            int net = (Integer) model_.getColumns(row_).get("net_weight");
            net_weight_field.setText(String.valueOf((double) net / 1000));
        }
    }//GEN-LAST:event_goods_f01ItemStateChanged

    private void cont_date2_yearKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cont_date2_yearKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            cont_date2_year.transferFocus();
            evt.consume();
        }
    }//GEN-LAST:event_cont_date2_yearKeyPressed

    private void 画面初期化_btn_MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_画面初期化_btn_MousePressed
        // TODO add your handling code here:
    }//GEN-LAST:event_画面初期化_btn_MousePressed

    private void 画面初期化_btn_ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_画面初期化_btn_ActionPerformed
        // TODO add your handling code here:
        clearView();
        update();
    }//GEN-LAST:event_画面初期化_btn_ActionPerformed

    private void 交付年月日_print_f_ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_交付年月日_print_f_ItemStateChanged
        // TODO add your handling code here:
        grant2_print_check();
    }//GEN-LAST:event_交付年月日_print_f_ItemStateChanged

    private void grant2_print_check() {
        grant_year.setEditable(!交付年月日_print_f_.isSelected());
        grant_month.setEditable(!交付年月日_print_f_.isSelected());
        grant_day.setEditable(!交付年月日_print_f_.isSelected());
        grant_year.setEnabled(!交付年月日_print_f_.isSelected());
        grant_month.setEnabled(!交付年月日_print_f_.isSelected());
        grant_day.setEnabled(!交付年月日_print_f_.isSelected());
    }

    private void 交付担当者_print_f_ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_交付担当者_print_f_ItemStateChanged
        // TODO add your handling code here:
        tantou_print_check();
    }//GEN-LAST:event_交付担当者_print_f_ItemStateChanged
    private void tantou_print_check() {
        tantou_cmb1.setEnabled(!交付担当者_print_f_.isSelected());
        tantou_cmb.setEnabled(!交付担当者_print_f_.isSelected());
    }
    private void 単位_print_f_ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_単位_print_f_ItemStateChanged
        // TODO add your handling code here:
        tani_print_check();
    }//GEN-LAST:event_単位_print_f_ItemStateChanged

    private void tani_print_check() {
        goods_f01.setEnabled(!単位_print_f_.isSelected());
        goods_f02.setEnabled(!単位_print_f_.isSelected());
        goods_f03.setEnabled(!単位_print_f_.isSelected());
        goods_f04.setEnabled(!単位_print_f_.isSelected());
    }

    private void 形状_print_f_ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_形状_print_f_ItemStateChanged
        // TODO add your handling code here:
        keijyo_print_check();
    }//GEN-LAST:event_形状_print_f_ItemStateChanged

    private void keijyo_print_check() {
        goods_f33.setEnabled(!形状_print_f_.isSelected());
        goods_f34.setEnabled(!形状_print_f_.isSelected());
        goods_f35.setEnabled(!形状_print_f_.isSelected());
    }

    private void 荷姿_print_f_ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_荷姿_print_f_ItemStateChanged
        // TODO add your handling code here:
        nisugata_print_check();
    }//GEN-LAST:event_荷姿_print_f_ItemStateChanged

    private void ent_search_FocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_ent_search_FocusGained
        // TODO add your handling code here:
        ent_search_.selectAll();
    }//GEN-LAST:event_ent_search_FocusGained

    private void office_search_FocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_office_search_FocusGained
        // TODO add your handling code here:
        office_search_.selectAll();
    }//GEN-LAST:event_office_search_FocusGained
    private void private_ent_search() {
        try {
            int search_id = Integer.valueOf(ent_search_.getText());
            for (int i = 0; i < 排出事業者Array[0].length; i++) {
                if (search_id == Integer.valueOf((String) 排出事業者Array[0][i])) {
                    ent_name.setText((String) 排出事業者Array[6][i]);
                    ent_zip01.setText((String) 排出事業者Array[2][i]);
                    ent_tel01.setText((String) 排出事業者Array[3][i]);
                    ent_address01.setText((String) 排出事業者Array[4][i] + (String) 排出事業者Array[5][i]);

                    break;
                }
            }

        } catch (Exception e) {
            ent_search_.setText((String) 排出事業者Array[0][ent_id_index]);
        }
    }

    private void office_search() {
        //排出事業場
        Integer search_id = office_id_;
        Integer array_search_id;
        ent_id_ = Integer.valueOf(ent_search_.getText());
        mSQLCommon.GetMasterDataOffice("v0_office", 排出事業場Array, ent_id_);
        String[] OfficeID;

        if (排出事業場Array[0].length > 0) {
            OfficeID = new String[排出事業場Array[0].length];
            for (int i = 0; i < 排出事業場Array[0].length; i++) {
                if (排出事業場Array[0][i] != null) {
                    array_search_id = Integer.valueOf((String) 排出事業場Array[0][i]);
                    OfficeID[i] = (String) 排出事業場Array[0][i];
                    if (search_id.equals(array_search_id)) {
                        office_id_index = i;
                    }
                }
            }
            office_id_cmb.setModel(new javax.swing.DefaultComboBoxModel(OfficeID));
            try {
                office_id_cmb.setSelectedIndex(office_id_index);
            } catch (IllegalArgumentException e) {
                office_id_cmb.setSelectedIndex(0);
            }
        } else {
            OfficeID = new String[1];
            OfficeID[0] = "0";
            office_id_cmb.setModel(new javax.swing.DefaultComboBoxModel(OfficeID));
            office_id_cmb.setSelectedIndex(0);
        }
    }

    private void ent_search_KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_ent_search_KeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            office_search_.requestFocus();
            private_ent_search();
            office_search();
        }
    }//GEN-LAST:event_ent_search_KeyPressed

    private void office_search_KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_office_search_KeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            trans_search_.requestFocus();
            try {
                int search_id = Integer.valueOf(office_search_.getText());
                for (int i = 0; i < 排出事業場Array[0].length; i++) {
                    if (search_id == Integer.valueOf((String) 排出事業場Array[0][i])) {
                        office_id_cmb.setSelectedIndex(i);
                        office_name_.setText((String) 排出事業場Array[1][i]);
                        office_name_.setToolTipText((String) 排出事業場Array[1][i]);
                        office_zip01.setText((String) 排出事業場Array[2][i]);
                        office_tel01.setText((String) 排出事業場Array[3][i]);
                        office_address01.setText((String) 排出事業場Array[4][i] + (String) 排出事業場Array[5][i]);
                        office_sub_name_.setText("");
                        break;
                    }
                }
            } catch (Exception e) {
                office_search_.setText((String) 排出事業場Array[0][office_id_index]);
            }
        }
    }//GEN-LAST:event_office_search_KeyPressed

    private void jButton1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton1MouseClicked

    }//GEN-LAST:event_jButton1MouseClicked

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        EntSelector selector = new EntSelector(con_);
        switch (selector.showSelector(jPanel44)) {
            case EntSelector.CLEARED:
                ent_search_.setText("0");
                ent_search_.requestFocus();
                break;
            case EntSelector.SELECTED:
                Map<String, Object> item = selector.selectedRow();
                ent_search_.setText(String.valueOf((Integer) item.get("id")));
                private_ent_search();
                office_search();
                ent_search_.requestFocus();
                break;
            default:
            // No operation.
            }
    }//GEN-LAST:event_jButton1ActionPerformed

    private void trans_search_FocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_trans_search_FocusGained
        // TODO add your handling code here:
        trans_search_.selectAll();
    }//GEN-LAST:event_trans_search_FocusGained

    private void trans_search_FocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_trans_search_FocusLost
        // TODO add your handling code here:
        private_trans_search();
    }//GEN-LAST:event_trans_search_FocusLost

    private void private_trans_search() {
        try {
            int search_id = Integer.valueOf(trans_search_.getText());
            for (int i = 0; i < 運搬業者名2Array[0].length; i++) {
                if (search_id == Integer.valueOf((String) 運搬業者名2Array[0][i])) {
                    trans_cmb01.setSelectedIndex(i);
                    trans_zip01.setText((String) 運搬業者名2Array[2][i]);
                    trans_tel01.setText((String) 運搬業者名2Array[3][i]);
                    trans_address01.setText((String) 運搬業者名2Array[4][i] + (String) 運搬業者名2Array[5][i]);
                    break;
                }
            }
            /////////////////////////////////////////////////////////////////////
            mSQLCommon.GetMasterDataTransTantou("v0_trans_tantou", 運搬担当者Array1, search_id);
            if (運搬担当者Array1[0].length > 0) {
                String[] TransTantouID1 = new String[運搬担当者Array1[0].length];
                for (int i = 0; i < 運搬担当者Array1[0].length; i++) {
                    TransTantouID1[i] = (String) 運搬担当者Array1[3][i];
                }
                trans_tantou_id_cmb1.setModel(new javax.swing.DefaultComboBoxModel(TransTantouID1));
            }
        } catch (Exception e) {
            trans_search_.setText((String) 運搬業者名2Array[0][trans1_id_index]);
        }
    }

    private void private_trans_search1() {
        try {
            int search_id = Integer.valueOf(trans_search_1.getText());
            for (int i = 0; i < 運搬業者名2Array[0].length; i++) {
                if (search_id == Integer.valueOf((String) 運搬業者名2Array[0][i])) {
                    trans_cmb02.setSelectedIndex(i);
                    trans_zip02.setText((String) 運搬業者名2Array[2][i]);
                    trans_tel02.setText((String) 運搬業者名2Array[3][i]);
                    trans_address02.setText((String) 運搬業者名2Array[4][i] + (String) 運搬業者名2Array[5][i]);
                    break;
                }
            }
            /////////////////////////////////////////////////////////////////////
            mSQLCommon.GetMasterDataTransTantou("v0_trans_tantou", 運搬担当者Array1, search_id);
            if (運搬担当者Array1[0].length > 0) {
                String[] TransTantouID2 = new String[運搬担当者Array1[0].length];
                for (int i = 0; i < 運搬担当者Array1[0].length; i++) {
                    TransTantouID2[i] = (String) 運搬担当者Array1[3][i];
                }
                trans_tantou_id_cmb2.setModel(new javax.swing.DefaultComboBoxModel(TransTantouID2));
            }
        } catch (Exception e) {
            trans_search_1.setText((String) 運搬業者名2Array[0][trans2_id_index]);
        }
    }

    private void trans_search_KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_trans_search_KeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            cont_search_.requestFocus();
        }
    }//GEN-LAST:event_trans_search_KeyPressed

    private void jButton2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton2MouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton2MouseClicked

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // TODO add your handling code here:
        TransSelector selector = new TransSelector(con_);
        switch (selector.showSelector(jPanel44)) {
            case EntSelector.CLEARED:
                trans_search_.setText("0");
                trans_search_.requestFocus();
                break;
            case EntSelector.SELECTED:
                Map<String, Object> item = selector.selectedRow();
                trans_search_.setText(String.valueOf((Integer) item.get("id")));
                private_trans_search();
                trans_search_.requestFocus();
                break;
            default:
            // No operation.
            }

    }//GEN-LAST:event_jButton2ActionPerformed

    private void trans_search_1FocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_trans_search_1FocusGained
        // TODO add your handling code here:
        trans_search_1.selectAll();
    }//GEN-LAST:event_trans_search_1FocusGained

    private void trans_search_1FocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_trans_search_1FocusLost
        // TODO add your handling code here:
        private_trans_search1();
    }//GEN-LAST:event_trans_search_1FocusLost

    private void trans_search_1KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_trans_search_1KeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            ent_search_.requestFocus();
        }
    }//GEN-LAST:event_trans_search_1KeyPressed

    private void jButton3MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton3MouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton3MouseClicked

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        // TODO add your handling code here:
        TransSelector selector = new TransSelector(con_);
        switch (selector.showSelector(jPanel44)) {
            case EntSelector.CLEARED:
                trans_search_1.setText("0");
                trans_search_1.requestFocus();
                break;
            case EntSelector.SELECTED:
                Map<String, Object> item = selector.selectedRow();
                trans_search_1.setText(String.valueOf((Integer) item.get("id")));
                private_trans_search1();
                trans_search_1.requestFocus();
                break;
            default:
            // No operation.
            }
    }//GEN-LAST:event_jButton3ActionPerformed

    private void cont_search_FocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_cont_search_FocusGained
        // TODO add your handling code here:
        cont_search_.selectAll();
    }//GEN-LAST:event_cont_search_FocusGained

    private void cont_search_FocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_cont_search_FocusLost
        // TODO add your handling code here:
        private_cont_search();
    }//GEN-LAST:event_cont_search_FocusLost

    private void private_cont_search() {
        try {
            int search_id = Integer.valueOf(cont_search_.getText());
            for (int i = 0; i < 処分業者名Array[0].length; i++) {
                if (search_id == Integer.valueOf((String) 処分業者名Array[0][i])) {
                    cont_cmb01.setSelectedIndex(i);

                    cont_zip01.setText((String) 処分業者名Array[2][i]);
                    cont_tel01.setText((String) 処分業者名Array[3][i]);
                    cont_address01.setText((String) 処分業者名Array[4][i] + (String) 処分業者名Array[5][i]);
                    cont_name1.setText((String) 処分業者名Array[13][i]);//cont_tantou 20151010 担当者追加　処分担当者セット
                    cont_name2.setText((String) 処分業者名Array[13][cont_cmb01.getSelectedIndex()]);
                    最終処分担当者.setText((String) 処分業者名Array[14][i]);//last_tantou 20151019 最終処分担当者追加　最終処分担当者セット

                    break;
                }
            }
        } catch (Exception e) {
            cont_search_.setText((String) 処分業者名Array[0][cont_id_index]);
        }
    }
    private void cont_search_KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cont_search_KeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            ent_search_.requestFocus();
        }
    }//GEN-LAST:event_cont_search_KeyPressed

    private void jButton4MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton4MouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton4MouseClicked

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        // TODO add your handling code here:
        ContSelector selector = new ContSelector(con_);
        switch (selector.showSelector(jPanel44)) {
            case EntSelector.CLEARED:
                cont_search_.setText("0");
                cont_search_.requestFocus();
                break;
            case EntSelector.SELECTED:
                Map<String, Object> item = selector.selectedRow();
                cont_search_.setText(String.valueOf((Integer) item.get("id")));
                private_trans_search();
                cont_search_.requestFocus();
                break;
            default:
            // No operation.
            }
    }//GEN-LAST:event_jButton4ActionPerformed

    private void cont_search_1FocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_cont_search_1FocusGained
        // TODO add your handling code here:
        cont_search_1.selectAll();
    }//GEN-LAST:event_cont_search_1FocusGained

    private void cont_search_1FocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_cont_search_1FocusLost
        // TODO add your handling code here:
        private_cont_search1();
    }//GEN-LAST:event_cont_search_1FocusLost

    private void private_cont_search1() {
        try {
            int search_id = Integer.valueOf(cont_search_1.getText());
            for (int i = 0; i < 保管場所Array[0].length - 1; i++) {
                if (search_id == Integer.valueOf((String) 保管場所Array[0][i])) {
                    保管場所_cmb.setSelectedIndex(i);
                    cont_zip02.setText((String) 保管場所Array[4][i]);
                    cont_tel02.setText((String) 保管場所Array[5][i]);
                    cont_address02.setText((String) 保管場所Array[2][i] + (String) 保管場所Array[3][i]);
                    break;
                }
            }
        } catch (Exception e) {
            cont_search_.setText((String) 処分業者名Array[0][cont_id_index]);
        }
    }

    private void cont_search_1KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cont_search_1KeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_cont_search_1KeyPressed

    private void 保管場所_cmbItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_保管場所_cmbItemStateChanged
        // TODO add your handling code here:
        //保管場所選択
        cont_zip02.setText((String) 保管場所Array[4][保管場所_cmb.getSelectedIndex()]);
        cont_tel02.setText((String) 保管場所Array[5][保管場所_cmb.getSelectedIndex()]);
        cont_address02.setText((String) 保管場所Array[2][保管場所_cmb.getSelectedIndex()]
                 + (String) 保管場所Array[3][保管場所_cmb.getSelectedIndex()]);
        cont_search_1.setText((String) 保管場所Array[0][保管場所_cmb.getSelectedIndex()]);
    }//GEN-LAST:event_保管場所_cmbItemStateChanged

    private void office_name_ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_office_name_ActionPerformed

    }//GEN-LAST:event_office_name_ActionPerformed

    private void trans_tantou_id_cmb1ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_trans_tantou_id_cmb1ItemStateChanged
        // TODO add your handling code here:
        trans_name1.setText(trans_tantou_id_cmb1.getSelectedItem().toString());
        if (trans_tantou_id_cmb1.getSelectedIndex() > 0) {
            trans_tantou_search_1.setText((String) 運搬担当者Array1[2][trans_tantou_id_cmb1.getSelectedIndex()]);
        }
    }//GEN-LAST:event_trans_tantou_id_cmb1ItemStateChanged

    private void trans_tantou_id_cmb2ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_trans_tantou_id_cmb2ItemStateChanged
        // TODO add your handling code here:
        trans_name2.setText(trans_tantou_id_cmb2.getSelectedItem().toString());
        if (trans_tantou_id_cmb2.getSelectedIndex() > 0) {
            trans_tantou_search_2.setText((String) 運搬担当者Array2[2][trans_tantou_id_cmb2.getSelectedIndex()]);
        }
    }//GEN-LAST:event_trans_tantou_id_cmb2ItemStateChanged

    private void trans_tantou_search_1FocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_trans_tantou_search_1FocusGained
        // TODO add your handling code here:
    }//GEN-LAST:event_trans_tantou_search_1FocusGained

    private void trans_tantou_search_1FocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_trans_tantou_search_1FocusLost
        // TODO add your handling code here:
        private_trans_tantou_search1();
    }//GEN-LAST:event_trans_tantou_search_1FocusLost

    private void private_trans_tantou_search1() {
        try {
            int search_id = Integer.valueOf(trans_tantou_search_1.getText());
            int array_search_id = 0;
            for (int i = 0; i < 運搬担当者Array1[2].length; i++) {
                if (((String) 運搬担当者Array1[2][i]).length() > 0) {
                    array_search_id = Integer.valueOf((String) 運搬担当者Array1[2][i]);
                } else {
                    array_search_id = 0;
                }
                if (search_id == array_search_id) {
                    trans_tantou_id_cmb1.setSelectedItem((String) 運搬担当者Array1[3][i]);
                    break;
                }
            }
        } catch (Exception e) {
        }
    }

    private void trans_tantou_search_1KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_trans_tantou_search_1KeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_trans_tantou_search_1KeyPressed

    private void trans_tantou_search_2FocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_trans_tantou_search_2FocusGained
        // TODO add your handling code here:
    }//GEN-LAST:event_trans_tantou_search_2FocusGained

    private void trans_tantou_search_2FocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_trans_tantou_search_2FocusLost
        // TODO add your handling code here:
        private_trans_tantou_search2();
    }//GEN-LAST:event_trans_tantou_search_2FocusLost

    private void private_trans_tantou_search2() {
        try {
            int search_id = Integer.valueOf(trans_tantou_search_2.getText());
            int array_search_id = 0;
            for (int i = 0; i < 運搬担当者Array2[2].length; i++) {
                if (((String) 運搬担当者Array2[2][i]).length() > 0) {
                    array_search_id = Integer.valueOf((String) 運搬担当者Array2[2][i]);
                } else {
                    array_search_id = 0;
                }
                if (search_id == array_search_id) {
                    trans_tantou_id_cmb2.setSelectedItem((String) 運搬担当者Array2[3][i]);
                    break;
                }
            }
        } catch (Exception e) {
        }
    }

    private void trans_tantou_search_2KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_trans_tantou_search_2KeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_trans_tantou_search_2KeyPressed

    private void value_tex1FocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_value_tex1FocusLost

    }//GEN-LAST:event_value_tex1FocusLost

    private void value_tex2FocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_value_tex2FocusLost

    }//GEN-LAST:event_value_tex2FocusLost

    private void value_tex3FocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_value_tex3FocusLost

    }//GEN-LAST:event_value_tex3FocusLost

    private void value_tex4FocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_value_tex4FocusLost

    }//GEN-LAST:event_value_tex4FocusLost

    private void value_tex5FocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_value_tex5FocusLost

    }//GEN-LAST:event_value_tex5FocusLost

    private void value_tex6FocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_value_tex6FocusLost

    }//GEN-LAST:event_value_tex6FocusLost

    private void value_tex7FocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_value_tex7FocusLost

    }//GEN-LAST:event_value_tex7FocusLost

    private void value_tex8FocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_value_tex8FocusLost

    }//GEN-LAST:event_value_tex8FocusLost

    private void value_tex13FocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_value_tex13FocusLost

    }//GEN-LAST:event_value_tex13FocusLost

    private void value_tex14FocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_value_tex14FocusLost

    }//GEN-LAST:event_value_tex14FocusLost

    private void value_tex15FocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_value_tex15FocusLost

    }//GEN-LAST:event_value_tex15FocusLost

    private void value_tex16FocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_value_tex16FocusLost

    }//GEN-LAST:event_value_tex16FocusLost

    private void value_tex17FocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_value_tex17FocusLost

    }//GEN-LAST:event_value_tex17FocusLost

    private void value_tex18FocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_value_tex18FocusLost

    }//GEN-LAST:event_value_tex18FocusLost

    private void value_tex19FocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_value_tex19FocusLost

    }//GEN-LAST:event_value_tex19FocusLost

    private void value_tex23FocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_value_tex23FocusLost

    }//GEN-LAST:event_value_tex23FocusLost

    private void slash_f3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_slash_f3ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_slash_f3ActionPerformed

    private void check_f3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_check_f3ActionPerformed
        // TODO add your handling code here:
        //当欄記載の通りにチェックで『中央公害清掃㈱松山事業所』を表示する
        if (check_f4.isSelected() && !(check_f3.isSelected())) {
            jlbl_final_name_.setVisible(check_f4.isSelected());
        } else {
            jlbl_final_name_.setVisible(false);
        }
    }//GEN-LAST:event_check_f3ActionPerformed

    private void 最終処分確認_print_fItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_最終処分確認_print_fItemStateChanged
        // TODO add your handling code here:
        //20151019 最終処分確認社名、担当者追加
        最終処分確認_print_f();
    }//GEN-LAST:event_最終処分確認_print_fItemStateChanged

    private void office_id_cmbActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_office_id_cmbActionPerformed
        // TODO add your handling code here:
        try {
            Integer search_id = Integer.valueOf(office_id_cmb.getSelectedItem().toString());
            Integer array_search_id;
            for (int i = 0; i < 排出事業場Array[0].length; i++) {
                array_search_id = Integer.valueOf((String) 排出事業場Array[0][i]);
                if (search_id.equals(array_search_id)) {
                    office_search_.setText((String) 排出事業場Array[0][i]);
                    office_name_.setText((String) 排出事業場Array[1][i]);
                    office_name_.setToolTipText((String) 排出事業場Array[1][i]);
                    office_zip01.setText((String) 排出事業場Array[2][i]);
                    office_tel01.setText((String) 排出事業場Array[3][i]);
                    office_address01.setText((String) 排出事業場Array[4][i] + (String) 排出事業場Array[5][i]);
                    office_sub_name_.setText("");

                    //交付担当者 181225　抽出内容変更
                    if (((String) model_.getColumns(row_).get("grant_name")).length() > 0) {
                        tantou_cmb.setSelectedItem((String) 排出事業場Array[7][i]);
                    } else if (((String) 排出事業場Array[7][i]) == null) {
                        tantou_cmb.setSelectedIndex(0);
                    } else if (((String) 排出事業場Array[7][i]).length() > 0) {
                        tantou_cmb.setSelectedItem((String) 排出事業場Array[7][i]);
                    } else {
                        tantou_cmb.setSelectedIndex(0);
                    }
                    break;
                }
            }

        } catch (Exception e) {
            tantou_cmb.setSelectedIndex(0);
            office_search_.setText((String) 排出事業場Array[0][office_id_index]);
        }
    }//GEN-LAST:event_office_id_cmbActionPerformed

    private void jLabel10MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel10MousePressed

    }//GEN-LAST:event_jLabel10MousePressed

    private void btn建設パターン2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn建設パターン2ActionPerformed
        // TODO add your handling code here:
        set建設パターン2();
    }//GEN-LAST:event_btn建設パターン2ActionPerformed

    private void btn建設パターン1MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn建設パターン1MousePressed
        // TODO add your handling code here:
    }//GEN-LAST:event_btn建設パターン1MousePressed

    private void btn建設パターン1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn建設パターン1ActionPerformed
        // TODO add your handling code here:
        set建設パターン1();
    }//GEN-LAST:event_btn建設パターン1ActionPerformed

    private void 交付年月日_print_f_AncestorAdded(javax.swing.event.AncestorEvent evt) {//GEN-FIRST:event_交付年月日_print_f_AncestorAdded
        // TODO add your handling code here:
    }//GEN-LAST:event_交付年月日_print_f_AncestorAdded

    private void goods_f02ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_goods_f02ItemStateChanged
        // TODO add your handling code here:
        //20260317 add
        if (goods_f02.isSelected()) {
            net_weight_field.setText(String.format("%,d", (Integer) model_.getColumns(row_).get("net_weight")));
        }
    }//GEN-LAST:event_goods_f02ItemStateChanged

    private void 最終処分確認_print_f() {

        最終処分社名.setEditable(!最終処分確認_print_f.isSelected());
        最終処分担当者.setEditable(!最終処分確認_print_f.isSelected());
        cont_date2_year.setEditable(!最終処分確認_print_f.isSelected());
        cont_date2_month.setEditable(!最終処分確認_print_f.isSelected());
        cont_date2_day.setEditable(!最終処分確認_print_f.isSelected());
        最終処分社名.setEnabled(!最終処分確認_print_f.isSelected());
        最終処分担当者.setEnabled(!最終処分確認_print_f.isSelected());
        cont_date2_year.setEnabled(!最終処分確認_print_f.isSelected());
        cont_date2_month.setEnabled(!最終処分確認_print_f.isSelected());
        cont_date2_day.setEnabled(!最終処分確認_print_f.isSelected());
    }

    private void nisugata_print_check() {
        goods_f36.setEnabled(!荷姿_print_f_.isSelected());
        goods_f37.setEnabled(!荷姿_print_f_.isSelected());
        goods_f38.setEnabled(!荷姿_print_f_.isSelected());
        goods_f39.setEnabled(!荷姿_print_f_.isSelected());
        goods_f41.setEnabled(!荷姿_print_f_.isSelected());
    }

    private void weight_print_check() {
        //重量値の印字フラグ
        net_weight_field.setEnabled(!goods_f40.isSelected());
        net_weight_field.setEditable(!goods_f40.isSelected());
        value_tex1.setEnabled(!goods_f40.isSelected());
        value_tex1.setEditable(!goods_f40.isSelected());
        value_tex2.setEnabled(!goods_f40.isSelected());
        value_tex2.setEditable(!goods_f40.isSelected());
        value_tex3.setEnabled(!goods_f40.isSelected());
        value_tex3.setEditable(!goods_f40.isSelected());
        value_tex4.setEnabled(!goods_f40.isSelected());
        value_tex4.setEditable(!goods_f40.isSelected());
        value_tex5.setEnabled(!goods_f40.isSelected());
        value_tex5.setEditable(!goods_f40.isSelected());
        value_tex6.setEnabled(!goods_f40.isSelected());
        value_tex6.setEditable(!goods_f40.isSelected());
        value_tex7.setEnabled(!goods_f40.isSelected());
        value_tex7.setEditable(!goods_f40.isSelected());
        value_tex8.setEnabled(!goods_f40.isSelected());
        value_tex8.setEditable(!goods_f40.isSelected());
        value_tex9.setEnabled(!goods_f40.isSelected());
        value_tex9.setEditable(!goods_f40.isSelected());
        value_tex10.setEnabled(!goods_f40.isSelected());
        value_tex10.setEditable(!goods_f40.isSelected());
        value_tex11.setEnabled(!goods_f40.isSelected());
        value_tex11.setEditable(!goods_f40.isSelected());
        value_tex12.setEnabled(!goods_f40.isSelected());
        value_tex12.setEditable(!goods_f40.isSelected());
        value_tex13.setEnabled(!goods_f40.isSelected());
        value_tex13.setEditable(!goods_f40.isSelected());
        value_tex14.setEnabled(!goods_f40.isSelected());
        value_tex14.setEditable(!goods_f40.isSelected());
        value_tex15.setEnabled(!goods_f40.isSelected());
        value_tex15.setEditable(!goods_f40.isSelected());
        value_tex16.setEnabled(!goods_f40.isSelected());
        value_tex16.setEditable(!goods_f40.isSelected());
        value_tex17.setEnabled(!goods_f40.isSelected());
        value_tex17.setEditable(!goods_f40.isSelected());
        value_tex18.setEnabled(!goods_f40.isSelected());
        value_tex18.setEditable(!goods_f40.isSelected());
        value_tex19.setEnabled(!goods_f40.isSelected());
        value_tex19.setEditable(!goods_f40.isSelected());
        value_tex20.setEnabled(!goods_f40.isSelected());
        value_tex20.setEditable(!goods_f40.isSelected());
        value_tex21.setEnabled(!goods_f40.isSelected());
        value_tex21.setEditable(!goods_f40.isSelected());
        value_tex22.setEnabled(!goods_f40.isSelected());
        value_tex22.setEditable(!goods_f40.isSelected());
        value_tex23.setEnabled(!goods_f40.isSelected());
        value_tex23.setEditable(!goods_f40.isSelected());
        value_tex24.setEnabled(!goods_f40.isSelected());
        value_tex24.setEditable(!goods_f40.isSelected());
        value_tex25.setEnabled(!goods_f40.isSelected());
        value_tex25.setEditable(!goods_f40.isSelected());
        value_tex26.setEnabled(!goods_f40.isSelected());
        value_tex26.setEditable(!goods_f40.isSelected());
        value_tex27.setEnabled(!goods_f40.isSelected());
        value_tex27.setEditable(!goods_f40.isSelected());
        value_tex28.setEnabled(!goods_f40.isSelected());
        value_tex28.setEditable(!goods_f40.isSelected());
    }

    public boolean update() {
        boolean ret = false;
        Locale locale = new Locale("ja", "JP", "JP");
        Calendar calendar = Calendar.getInstance(locale);
        String dateStr = "";
        calendar.setLenient(false);

        //収集運搬車両番号に「0」をいれるための判定をチェックする
        if (trans_car_no01.getText().equals("")) {
            trans_car_no01.setText("0");
        }

        try {
            Statement st = con_.createStatement();
            st.executeUpdate("begin;");

            try {
                String s = new StringBuilder()
                         .append("update manifesto_data set ")
                         .append("manifesto_no2 = ?, ")//マニフェスト交付番号
                         .append("grant_date2 = ?, ")//交付年月日
                         .append("grant_name = ?, ")//交付担当者（マニフェストマスタ参照）
                         .append("ent_print_flag = ?, ")//排出事業者印字フラグ
                         .append("office_print_flag = ?, ")//排出事業場印字フラグ
                         .append("trans1_print_flag = ?, ")//運搬業者１印字フラグ
                         .append("trans2_print_flag = ?, ")//運搬業者２印字フラグ
                         .append("cont_print_flag = ?, ")//処分受託者印字フラグ
                         .append("tsumikae_print_flag = ?, ")//積み替え印字フラグ
                         .append("goods_flag01 = ?, ")//10 単位：t
                         .append("goods_flag02 = ?, ")//単位：kg
                         .append("goods_flag03 = ?, ")//単位：m3
                         .append("goods_flag04 = ?, ")//単位：リットル
                         .append("goods_flag05 = ?, ")//01コンクリートがら
                         .append("goods_flag06 = ?, ")//02アスコンがら
                         .append("goods_flag07 = ?, ")//03その他がれき類
                         .append("goods_flag08 = ?, ")//04ガラス・陶磁器くず
                         .append("goods_flag09 = ?, ")//05廃ぷら
                         .append("goods_flag10 = ?, ")//06金属くず
                         .append("goods_flag11 = ?, ")//20//07混合
                         .append("goods_flag12 = ?, ")//08石綿含有産業廃棄物
                         .append("goods_flag13 = ?, ")
                         .append("goods_flag14 = ?, ")
                         .append("goods_flag15 = ?, ")
                         .append("goods_flag16 = ?, ")
                         .append("goods_flag17 = ?, ")//11建設汚泥
                         .append("goods_flag18 = ?, ")//12紙くず
                         .append("goods_flag19 = ?, ")//13木くず
                         .append("goods_flag20 = ?, ")//14繊維くず
                         .append("goods_flag21 = ?, ")//30//15廃石膏ボード
                         .append("goods_flag22 = ?, ")//16混合
                         .append("goods_flag23 = ?, ")//17石綿含有産業廃棄物
                         .append("goods_flag24 = ?, ")//水銀使用製品　追加
                         .append("goods_flag25 = ?, ")
                         .append("goods_flag26 = ?, ")
                         .append("goods_flag27 = ?, ")//21廃石綿等
                         .append("goods_flag28 = ?, ")
                         .append("goods_flag29 = ?, ")
                         .append("goods_flag30 = ?, ")
                         .append("goods_flag31 = ?, ")//40
                         .append("goods_flag32 = ?, ")//水銀使用製品　追加
                         .append("goods_flag33 = ?, ")//形状：固形状
                         .append("goods_flag34 = ?, ")//形状：泥状
                         .append("goods_flag35 = ?, ")//形状：液状
                         .append("goods_flag36 = ?, ")//荷姿：バラ
                         .append("goods_flag37 = ?, ")//荷姿：コンテナ
                         .append("goods_flag38 = ?, ")//荷姿：ドラム缶
                         .append("goods_flag39 = ?, ")//荷姿：袋
                         .append("goods_flag40 = ?, ")//重量値印字無しフラグ
                         .append("slash_flag01 = ?, ")//50//斜線有り(中間処理)
                         .append("slash_flag02 = ?, ")//斜線有り(運搬受託２)
                         .append("slash_flag03 = ?, ")//斜線有り(運搬受託SUB)
                         .append("check_flag01 = ?, ")//中間：１
                         .append("check_flag02 = ?, ")//中間：２
                         .append("check_flag03 = ?, ")//最終：１
                         .append("check_flag04 = ?, ")//最終：２
                         .append("check_flag05 = ?, ")//運搬受託１積み替え有り
                         .append("check_flag06 = ?, ")//積替え無し
                         .append("check_flag07 = ?, ")//運搬受託２積み替え有り
                         .append("check_flag08 = ?, ")//60//積替え無し
                         .append("check_flag09 = ?, ")//運搬先の処分方法：１（脱水）
                         .append("check_flag10 = ?, ")//運搬先の処分方法：１（焼却）
                         .append("check_flag11 = ?, ")//運搬先の処分方法：１（破砕）
                         .append("check_flag12 = ?, ")//運搬先の処分方法：１（安定型）
                         .append("check_flag13 = ?, ")//運搬先の処分方法：１（管理型）
                         .append("check_flag14 = ?, ")//運搬先の処分方法：１（遮断型）
                         .append("check_flag15 = ?, ")//有価物収集：有り
                         .append("check_flag16 = ?, ")//有価物収集：無し
                         .append("check_flag17 = ?, ")//実績数量：t
                         .append("check_flag18 = ?, ")//70//実績数量：m3
                         .append("trans_office_sub_name1 = ?, ")//運搬受託１会社名
                         .append("trans1_sub_name = ?, ")//運搬受託１担当者名
                         .append("trans1_date = ?, ")//運搬受託日１
                         .append("trans1_car_no = ?, ")
                         .append("trans1_car_kind = ?, ")
                         .append("trans_office_sub_name2 = ?, ")//運搬受託２会社名
                         .append("trans2_sub_name = ?, ")//運搬受託２担当者名
                         .append("trans2_date = ?, ")//運搬受託日２
                         .append("trans2_car_no = ?, ")
                         .append("trans2_car_kind = ?, ")//80
                         .append("cont_office_sub_name1 = ?, ")//処分受託１会社名
                         .append("cont_sub_name1 = ?, ")//処分受託１担当者名
                         .append("cont_date1 = ?, ")//受領日
                         .append("cont_office_sub_name2 = ?, ")//処分受託２会社名
                         .append("cont_sub_name2 = ?, ")//処分受託２担当者名
                         .append("cont_date2 = ?, ")//処分終了日
                         .append("disposal_date2 = ?, ")//最終処分年月日
                         .append("remark_name = ?, ")//備考欄（マニフェストマスタ参照）
                         .append("last_office_name = ?, ")//最終処分を行った場所（マニフェストマスタ参照）
                         .append("dis_ent_id = ?, ")//90//排出事業者
                         .append("trans1_id = ?, ")//運搬業者１
                         .append("trans2_id = ?, ")//運搬業者２
                         .append("dis_office_id = ?, ")//運搬先の事業場（マニフェストマスタ参照）//
                         .append("manifesto3_id = ?, ")//運搬先の事業場（マニフェストマスタ参照）//
                         .append("dis_cont_id = ?, ")//処分受託者
                         .append("trans_sub1_print_flag = ?, ")//運搬受託１印字フラグ
                         .append("trans_sub2_print_flag = ?, ")//運搬受託２印字フラグ
                         .append("cont_sub1_print_flag = ?, ")//処分受託１印字フラグ
                         .append("cont_sub2_print_flag = ?, ")//処分受託２印字フラグ
                         .append("trans_net_weight = ?, ")//100　総重量
                         .append("grant_name01 = ?, ")//交付担当者　所属
                         .append("last_cont_zip02 = ?, ")//積み替えまたは保管　郵便番号
                         .append("last_cont_tel02 = ?, ")//積み替えまたは保管　電話番号
                         .append("last_cont_address02 = ?, ")//積み替えまたは保管　住所
                         .append("last_cont_name02 = ?, ")//積み替えまたは保管　名称
                         //cont_zip02
                         //処分方法４対応
                         .append("method_name = ?, ")//処分方法
                         .append("office_zip = ?, ")//現場郵便番号
                         .append("office_tel = ?, ")//現場電話
                         .append("office_address = ?, ")//現場住所
                         .append("grant2_print_f = ?, ")//110
                         .append("tantou_print_f = ?, ")
                         .append("tani_print_f = ?, ")
                         .append("keijyo_print_f = ?, ")
                         .append("nisugata_print_f = ?, ")
                         //goods_weight01
                         //20140426 add
                         .append("goods_weight01 = ?, ")//115
                         .append("goods_weight02 = ?, ")
                         .append("goods_weight03 = ?, ")
                         .append("goods_weight04 = ?, ")
                         .append("goods_weight05 = ?, ")
                         .append("goods_weight06 = ?, ")//120
                         .append("goods_weight07 = ?, ")
                         .append("goods_weight08 = ?, ")
                         .append("goods_weight09 = ?, ")
                         .append("goods_weight10 = ?, ")
                         .append("goods_weight11 = ?, ")
                         .append("goods_weight12 = ?, ")
                         .append("goods_weight13 = ?, ")
                         .append("goods_weight14 = ?, ")
                         .append("goods_weight15 = ?, ")
                         .append("goods_weight16 = ?, ")
                         .append("edit_ent_zip = ?, ")
                         .append("edit_ent_name = ?, ")
                         .append("edit_ent_address = ?, ")
                         .append("edit_ent_tel = ?, ")
                         .append("last_tantou = ?, ")//最終処分担当者
                         .append("last_tantou_print_flag = ?, ")//最終処分確認印字フラグ
                         .append("goods_flag41 = ?, ")//荷姿：専用容器
                         .append("goods_weight17 = ? ")//value_tex20　水銀使用製品
                         .append("where weight_sno = ?;").toString();//131
                PreparedStatement ps = con_.prepareStatement(s);
                try {
                    ps.setObject(1, manifesto_no2.getText());

                    //元号使用止め西暦表示、入力に変更　180918
                    if (!grant_year.getText().equals("") && !grant_month.getText().equals("") && !grant_day.getText().equals("")) {
                        dateStr = grant_year.getText() + "/" + grant_month.getText() + "/" + grant_day.getText();
                    } else {
                        dateStr = null;
                    }
                    ps.setObject(2, DBManager.convert(DBManager.convert(dateStr)));
                    ps.setObject(3, tantou_cmb.getSelectedItem().toString());
                    ps.setObject(4, 排出事業者_print_f.isSelected());
                    ps.setObject(5, 事業場_print_f.isSelected());
                    ps.setObject(6, 運搬業者_print_f1.isSelected());
                    ps.setObject(7, 運搬業者_print_f2.isSelected());
                    ps.setObject(8, 処分業者_print_f1.isSelected());
                    ps.setObject(9, 積み替え_f5.isSelected());
                    ps.setObject(10, goods_f01.isSelected());
                    ps.setObject(11, goods_f02.isSelected());
                    ps.setObject(12, goods_f03.isSelected());
                    ps.setObject(13, goods_f04.isSelected());
                    ps.setObject(14, goods_f05.isSelected());
                    ps.setObject(15, goods_f06.isSelected());
                    ps.setObject(16, goods_f07.isSelected());
                    ps.setObject(17, goods_f08.isSelected());
                    ps.setObject(18, goods_f09.isSelected());
                    ps.setObject(19, goods_f10.isSelected());
                    ps.setObject(20, goods_f11.isSelected());
                    ps.setObject(21, goods_f12.isSelected());
                    ps.setObject(22, goods_f13.isSelected());
                    ps.setObject(23, goods_f14.isSelected());
                    ps.setObject(24, goods_f15.isSelected());
                    ps.setObject(25, goods_f16.isSelected());
                    ps.setObject(26, goods_f17.isSelected());
                    ps.setObject(27, goods_f18.isSelected());
                    ps.setObject(28, goods_f19.isSelected());
                    ps.setObject(29, goods_f20.isSelected());
                    ps.setObject(30, goods_f21.isSelected());
                    ps.setObject(31, goods_f22.isSelected());
                    ps.setObject(32, goods_f23.isSelected());
                    ps.setObject(33, goods_f24.isSelected());
                    ps.setObject(34, goods_f25.isSelected());
                    ps.setObject(35, goods_f26.isSelected());
                    ps.setObject(36, goods_f27.isSelected());
                    ps.setObject(37, goods_f28.isSelected());
                    ps.setObject(38, goods_f29.isSelected());
                    ps.setObject(39, goods_f30.isSelected());
                    ps.setObject(40, goods_f31.isSelected());
                    ps.setObject(41, goods_f32.isSelected());
                    ps.setObject(42, goods_f33.isSelected());
                    ps.setObject(43, goods_f34.isSelected());
                    ps.setObject(44, goods_f35.isSelected());
                    ps.setObject(45, goods_f36.isSelected());
                    ps.setObject(46, goods_f37.isSelected());
                    ps.setObject(47, goods_f38.isSelected());
                    ps.setObject(48, goods_f39.isSelected());
                    ps.setObject(49, goods_f40.isSelected());
                    ps.setObject(50, slash_f1.isSelected());
                    ps.setObject(51, slash_f2.isSelected());
                    ps.setObject(52, slash_f3.isSelected());
                    ps.setObject(53, check_f1.isSelected());
                    ps.setObject(54, check_f2.isSelected());
                    ps.setObject(55, check_f3.isSelected());
                    ps.setObject(56, check_f4.isSelected());
                    ps.setObject(57, check_f5.isSelected());
                    ps.setObject(58, check_f6.isSelected());
                    ps.setObject(59, check_f7.isSelected());
                    ps.setObject(60, check_f8.isSelected());
                    ps.setObject(61, check_f9.isSelected());
                    ps.setObject(62, check_f10.isSelected());
                    ps.setObject(63, check_f11.isSelected());
                    ps.setObject(64, check_f12.isSelected());
                    ps.setObject(65, check_f13.isSelected());
                    ps.setObject(66, check_f14.isSelected());
                    ps.setObject(67, check_f15.isSelected());
                    ps.setObject(68, check_f16.isSelected());
                    ps.setObject(69, check_f17.isSelected());
                    ps.setObject(70, check_f18.isSelected());
                    ps.setObject(71, trans_office1.getText());
                    ps.setObject(72, trans_name1.getText());
                    if (!trans_year1.getText().equals("") && !trans_month1.getText().equals("") && !trans_day1.getText().equals("")) {
                        dateStr = trans_year1.getText() + "/" + trans_month1.getText() + "/" + trans_day1.getText();
                    } else {
                        dateStr = null;
                    }
                    ps.setObject(73, DBManager.convert(DBManager.convert(dateStr)));
                    ps.setObject(74, trans_car_no01.getText());
                    ps.setObject(75, trans_car_kind01.getSelectedItem().toString());
                    ps.setObject(76, trans_office2.getText());
                    ps.setObject(77, trans_name2.getText());
                    if (!trans_year2.getText().equals("") && !trans_month2.getText().equals("") && !trans_day2.getText().equals("")) {
                        dateStr = trans_year2.getText() + "/" + trans_month2.getText() + "/" + trans_day2.getText();
                    } else {
                        dateStr = null;
                    }
                    ps.setObject(78, DBManager.convert(DBManager.convert(dateStr)));
                    ps.setObject(79, trans_car_no02.getText());
                    ps.setObject(80, trans_car_kind02.getSelectedItem().toString());
                    ps.setObject(81, cont_office1.getText());
                    ps.setObject(82, cont_name1.getText());

                    if (!cont_year1.getText().equals("") && !cont_month1.getText().equals("") && !cont_day1.getText().equals("")) {
                        dateStr = cont_year1.getText() + "/" + cont_month1.getText() + "/" + cont_day1.getText();
                    } else {
                        dateStr = null;
                    }
                    ps.setObject(83, DBManager.convert(DBManager.convert(dateStr)));

                    ps.setObject(84, cont_office2.getText());
                    ps.setObject(85, cont_name2.getText());

                    if (!cont_year2.getText().equals("") && !cont_month2.getText().equals("") && !cont_day2.getText().equals("")) {
                        dateStr = cont_year2.getText() + "/" + cont_month2.getText() + "/" + cont_day2.getText();
                    } else {
                        dateStr = null;
                    }
                    ps.setObject(86, DBManager.convert(DBManager.convert(dateStr)));

                    if (!cont_date2_year.getText().equals("") && !cont_date2_month.getText().equals("") && !cont_date2_day.getText().equals("")) {
                        dateStr = cont_date2_year.getText() + "/" + cont_date2_month.getText() + "/" + cont_date2_day.getText();
                    } else {
                        dateStr = null;
                    }
                    ps.setObject(87, DBManager.convert(DBManager.convert(dateStr)));

                    ps.setObject(88, txt備考.getText());
                    ps.setObject(89, last_cont_cmb.getSelectedItem().toString());
                    ps.setObject(90, Integer.valueOf((String) ent_search_.getText()));
                    ps.setObject(91, Integer.valueOf((String) 運搬業者名2Array[0][trans_cmb01.getSelectedIndex()]));
                    ps.setObject(92, Integer.valueOf((String) 運搬業者名2Array[0][trans_cmb02.getSelectedIndex()]));
                    if (排出事業場Array[0].length > 0) {
                        ps.setObject(93, Integer.valueOf(office_id_cmb.getSelectedItem().toString()));
                    } else {
                        ps.setObject(93, 0);
                    }
                    ps.setObject(94, Integer.valueOf((String) 運搬事業場Array[0][cont_office_name01.getSelectedIndex()]));
                    ps.setObject(95, Integer.valueOf((String) 処分業者名Array[0][cont_cmb01.getSelectedIndex()]));
                    ps.setObject(96, 運搬の受託_print_f3.isSelected());
                    ps.setObject(97, 運搬の受託_print_f4.isSelected());
                    ps.setObject(98, 処分の受託_print_f2.isSelected());
                    ps.setObject(99, 処分の受託_print_f3.isSelected());
                    ps.setObject(100, net_weight_field.getText());
                    //null値でエラー出ていたため　20170317
                    if (tantou_cmb1.getSelectedItem().toString() == null) {
                        ps.setObject(101, "");
                    } else {
                        ps.setObject(101, tantou_cmb1.getSelectedItem().toString());
                    }
                    ps.setObject(102, cont_zip02.getText());
                    ps.setObject(103, cont_tel02.getText());
                    ps.setObject(104, cont_address02.getText());
                    //保管場所_cmb
                    ps.setObject(105, 保管場所_cmb.getSelectedItem().toString());
                    ps.setObject(106, cmb中間処理.getSelectedItem().toString());
                    ps.setObject(107, office_zip01.getText());
                    ps.setObject(108, office_tel01.getText());
                    ps.setObject(109, office_address01.getText());
                    ps.setObject(110, 交付年月日_print_f_.isSelected());
                    ps.setObject(111, 交付担当者_print_f_.isSelected());
                    ps.setObject(112, 単位_print_f_.isSelected());
                    ps.setObject(113, 形状_print_f_.isSelected());
                    ps.setObject(114, 荷姿_print_f_.isSelected());
                    ps.setObject(115, value_tex1.getText());
                    ps.setObject(116, value_tex2.getText());
                    ps.setObject(117, value_tex3.getText());
                    ps.setObject(118, value_tex4.getText());
                    ps.setObject(119, value_tex5.getText());
                    ps.setObject(120, value_tex6.getText());
                    ps.setObject(121, value_tex7.getText());
                    ps.setObject(122, value_tex8.getText());
                    ps.setObject(123, value_tex13.getText());
                    ps.setObject(124, value_tex14.getText());
                    ps.setObject(125, value_tex15.getText());
                    ps.setObject(126, value_tex16.getText());
                    ps.setObject(127, value_tex17.getText());
                    ps.setObject(128, value_tex18.getText());
                    ps.setObject(129, value_tex19.getText());
                    ps.setObject(130, value_tex23.getText());
                    ps.setObject(131, ent_zip01.getText());
                    ps.setObject(132, ent_name.getText());
                    ps.setObject(133, ent_address01.getText());
                    ps.setObject(134, ent_tel01.getText());
                    ps.setObject(135, 最終処分担当者.getText());
                    ps.setObject(136, 最終処分確認_print_f.isSelected());//20151019 印字なしチェック追加
                    ps.setObject(137, goods_f41.isSelected());
                    ps.setObject(138, value_tex20.getText());//水銀使用製品
                    ps.setObject(139, sno_);
                    ps.executeUpdate();
                } finally {
                    ps.close();
                }

                s = new StringBuilder()
                         .append("update weigh_data set ")
                         .append("car_no = ?, ")//車番
                         .append("dis_ent_id = ?, ")//排出業者ID
                         .append("trans_id = ?, ")//運搬業者ID
                         .append("dis_office_id = ?, ")//排出事業場ID
                         .append("dis_cont_id = ?, ")//処分業者ID
                         .append("entrepreneur_name = ?, ")//排出業者
                         .append("transporter_name = ?, ")//運搬業者
                         .append("office_name = ?, ")//排出事業場
                         .append("contractor_name = ?, ")//処分業者
                         .append("office_sub_name = ?, ")//現場名補完分
                         .append("car_kind_name = ? ")//車種
                         .append("where sno = ?;").toString();//12
                ps = con_.prepareStatement(s);
                try {
                    if (trans_car_no02.getText().length() > 0 && Integer.valueOf(trans_car_no02.getText()) > 0) {
                        ps.setObject(1, Integer.valueOf(trans_car_no02.getText()));
                    } else {
                        ps.setObject(1, Integer.valueOf(trans_car_no01.getText()));
                    }
                    ps.setObject(2, Integer.valueOf(ent_search_.getText()));

                    if (trans_search_1.getText().length() > 0 && Integer.valueOf(trans_search_1.getText()) > 0) {
                        ps.setObject(3, Integer.valueOf(trans_search_1.getText()));
                    } else {
                        ps.setObject(3, Integer.valueOf(運搬業者名2Array[0][trans_cmb01.getSelectedIndex()]));
                    }

                    if (排出事業場Array[0].length > 0) {
                        ps.setObject(4, Integer.valueOf(office_id_cmb.getSelectedItem().toString()));
                    } else {
                        ps.setObject(4, 0);
                    }
                    ps.setObject(5, Integer.valueOf(処分業者名Array[0][cont_cmb01.getSelectedIndex()]));
                    ps.setObject(6, ent_name.getText());
                    ps.setObject(7, trans_cmb01.getSelectedItem().toString());
                    ps.setObject(8, office_name_.getText());
                    ps.setObject(9, cont_cmb01.getSelectedItem().toString());
                    ps.setObject(10, office_sub_name_.getText());
                    ps.setObject(11, trans_car_kind01.getSelectedItem().toString());
                    ps.setObject(12, sno_);
                    ps.executeUpdate();

                } finally {
                    ps.close();
                }

                st.executeUpdate("commit;");
                ret = true;
            } catch (SQLException e) {
                st.executeUpdate("rollback;");
                throw e;
            } finally {
                st.close();
            }

        } catch (Exception e) {

            return ret;
        }

        return ret;
    }

    private void clearView() {
        ent_id_ = 0;
        office_id_ = 0;
        trans1_id_ = 0;
        trans2_id_ = 0;
        cont_id_ = 0;
        methot_id_ = 0;
        car_kind1_id_ = 0;
        cont_office_id_ = 0;
        ent_name.setText("");
        ent_zip01.setText("");
        ent_tel01.setText("");
        ent_address01.setText("");
        ent_search_.setText("0");
        office_name_.setText("");
        office_name_.setToolTipText(null);
        office_zip01.setText("");
        office_tel01.setText("");
        office_address01.setText("");
        office_sub_name_.setText("");
        office_search_.setText("0");
        net_weight_field.setText("");
        trans_cmb01.setSelectedIndex(0);
        trans_zip01.setText("");
        trans_tel01.setText("");
        trans_address01.setText("");
        trans_car_no01.setText("");
        trans_cmb02.setSelectedIndex(0);
        trans_zip02.setText("");
        trans_tel02.setText("");
        trans_address02.setText("");
        cont_cmb01.setSelectedIndex(0);
        cont_zip01.setText("");
        cont_tel01.setText("");
        cont_address01.setText("");
        tantou_cmb1.setSelectedIndex(0);
        tantou_cmb.setSelectedIndex(0);
        grant_year.setText("");
        grant_month.setText("");
        grant_day.setText("");
        trans_year1.setText("");
        trans_month1.setText("");
        trans_day1.setText("");
        trans_year2.setText("");
        trans_month2.setText("");
        trans_day2.setText("");
        cont_year1.setText("");
        cont_month1.setText("");
        cont_day1.setText("");
        cont_year2.setText("");
        cont_month2.setText("");
        cont_day2.setText("");
        cont_date2_year.setText("");
        cont_date2_month.setText("");
        cont_date2_day.setText("");
        trans_office1.setText("");
        trans_name1.setText("");
        trans_office2.setText("");
        trans_name2.setText("");
        cont_office1.setText("");
        cont_name1.setText("");
        cont_office2.setText("");
        cont_name2.setText("");
        cont_name1.setText("");
        最終処分担当者.setText("");
        最終処分社名.setText("");
        cmb中間処理.setSelectedIndex(0);
        txt備考.setText("");
        trans_car_kind01.setSelectedIndex(0);

        //各チェックの設定
        goods_f01.setSelected(false);
        goods_f02.setSelected(false);
        goods_f03.setSelected(false);
        goods_f04.setSelected(false);
        goods_f05.setSelected(false);
        goods_f06.setSelected(false);
        goods_f07.setSelected(false);
        goods_f08.setSelected(false);
        goods_f09.setSelected(false);
        goods_f10.setSelected(false);
        goods_f11.setSelected(false);
        goods_f12.setSelected(false);
        goods_f13.setSelected(false);
        goods_f14.setSelected(false);
        goods_f15.setSelected(false);
        goods_f16.setSelected(false);
        goods_f17.setSelected(false);
        goods_f18.setSelected(false);
        goods_f19.setSelected(false);
        goods_f20.setSelected(false);
        goods_f21.setSelected(false);
        goods_f22.setSelected(false);
        goods_f23.setSelected(false);
        goods_f24.setSelected(false);
        goods_f25.setSelected(false);
        goods_f26.setSelected(false);
        goods_f27.setSelected(false);
        goods_f28.setSelected(false);
        goods_f29.setSelected(false);
        goods_f30.setSelected(false);
        goods_f31.setSelected(false);
        goods_f32.setSelected(false);
        goods_f33.setSelected(false);
        goods_f34.setSelected(false);
        goods_f35.setSelected(false);
        goods_f36.setSelected(false);
        goods_f37.setSelected(false);
        goods_f38.setSelected(false);
        goods_f39.setSelected(false);
        goods_f40.setSelected(false);
        goods_f41.setSelected(false);
        weight_print_check();
        check_f1.setSelected(false);
        check_f2.setSelected(false);
        check_f3.setSelected(false);
        check_f4.setSelected(false);
        check_f5.setSelected(false);
        check_f6.setSelected(false);
        check_f7.setSelected(false);
        check_f8.setSelected(false);
        check_f9.setSelected(false);
        check_f10.setSelected(false);
        check_f11.setSelected(false);
        check_f12.setSelected(false);
        check_f13.setSelected(false);
        check_f14.setSelected(false);
        check_f15.setSelected(false);
        check_f16.setSelected(false);
        check_f17.setSelected(false);
        check_f18.setSelected(false);
        運搬の受託_print_f3.setSelected(false);
        運搬の受託_print_f4.setSelected(false);
        処分の受託_print_f2.setSelected(false);
        処分の受託_print_f3.setSelected(false);
        最終処分確認_print_f.setSelected(false);
    }

    private void check_goods_flag(Boolean f按分フラグ) {
        if (!f按分フラグ) {
            //ごみの種類によってチェックボックスを選択する
            switchLabel01:
            switch (methot_id_) {
                case 1:
                    goods_f05.setSelected(true);
                    //3桁区切り正味重量
                    if (((String) model_.getColumns(row_).get("trans_net_weight")).length() > 0) {
                        value_tex1.setText((String) model_.getColumns(row_).get("trans_net_weight"));//手入力反映
                    } else {
                        value_tex1.setText(String.format("%,d", (Integer) model_.getColumns(row_).get("net_weight")));//正味重量反映
                    }
                    break;
                case 2:
                    goods_f06.setSelected(true);
                    //3桁区切り正味重量
                    if (((String) model_.getColumns(row_).get("trans_net_weight")).length() > 0) {
                        value_tex2.setText((String) model_.getColumns(row_).get("trans_net_weight"));
                    } else {
                        value_tex2.setText(String.format("%,d", (Integer) model_.getColumns(row_).get("net_weight")));
                    }
                    break;
                case 3:
                    goods_f07.setSelected(true);
                    //3桁区切り正味重量
                    if (((String) model_.getColumns(row_).get("trans_net_weight")).length() > 0) {
                        value_tex3.setText((String) model_.getColumns(row_).get("trans_net_weight"));
                    } else {
                        value_tex3.setText(String.format("%,d", (Integer) model_.getColumns(row_).get("net_weight")));
                    }
                    break;
                case 4:
                    goods_f08.setSelected(true);
                    //3桁区切り正味重量
                    if (((String) model_.getColumns(row_).get("trans_net_weight")).length() > 0) {
                        value_tex4.setText((String) model_.getColumns(row_).get("trans_net_weight"));
                    } else {
                        value_tex4.setText(String.format("%,d", (Integer) model_.getColumns(row_).get("net_weight")));
                    }
                    break;
                case 5:
                    goods_f09.setSelected(true);
                    //3桁区切り正味重量
                    if (((String) model_.getColumns(row_).get("trans_net_weight")).length() > 0) {
                        value_tex5.setText((String) model_.getColumns(row_).get("trans_net_weight"));
                    } else {
                        value_tex5.setText(String.format("%,d", (Integer) model_.getColumns(row_).get("net_weight")));
                    }
                    break;
                case 6:
                    goods_f10.setSelected(true);
                    //3桁区切り正味重量
                    if (((String) model_.getColumns(row_).get("trans_net_weight")).length() > 0) {
                        value_tex6.setText((String) model_.getColumns(row_).get("trans_net_weight"));
                    } else {
                        value_tex6.setText(String.format("%,d", (Integer) model_.getColumns(row_).get("net_weight")));
                    }
                    break;
                case 7:
                    goods_f11.setSelected(true);
                    //3桁区切り正味重量
                    if (((String) model_.getColumns(row_).get("trans_net_weight")).length() > 0) {
                        value_tex7.setText((String) model_.getColumns(row_).get("trans_net_weight"));
                    } else {
                        value_tex7.setText(String.format("%,d", (Integer) model_.getColumns(row_).get("net_weight")));
                    }
                    break;
                case 8:
                    goods_f12.setSelected(true);
                    //3桁区切り正味重量
                    if (((String) model_.getColumns(row_).get("trans_net_weight")).length() > 0) {
                        value_tex8.setText((String) model_.getColumns(row_).get("trans_net_weight"));
                    } else {
                        value_tex8.setText(String.format("%,d", (Integer) model_.getColumns(row_).get("net_weight")));
                    }
                    break;
                case 11:
                    goods_f17.setSelected(true);
                    //3桁区切り正味重量
                    if (((String) model_.getColumns(row_).get("trans_net_weight")).length() > 0) {
                        value_tex13.setText((String) model_.getColumns(row_).get("trans_net_weight"));
                    } else {
                        value_tex13.setText(String.format("%,d", (Integer) model_.getColumns(row_).get("net_weight")));
                    }
                    break;
                case 12:
                    goods_f18.setSelected(true);
                    //3桁区切り正味重量
                    if (((String) model_.getColumns(row_).get("trans_net_weight")).length() > 0) {
                        value_tex14.setText((String) model_.getColumns(row_).get("trans_net_weight"));
                    } else {
                        value_tex14.setText(String.format("%,d", (Integer) model_.getColumns(row_).get("net_weight")));
                    }
                    break;
                case 13:
                    goods_f19.setSelected(true);
                    //3桁区切り正味重量
                    if (((String) model_.getColumns(row_).get("trans_net_weight")).length() > 0) {
                        value_tex15.setText((String) model_.getColumns(row_).get("trans_net_weight"));
                    } else {
                        value_tex15.setText(String.format("%,d", (Integer) model_.getColumns(row_).get("net_weight")));
                    }
                    break;
                case 14:
                    goods_f20.setSelected(true);
                    //3桁区切り正味重量
                    if (((String) model_.getColumns(row_).get("trans_net_weight")).length() > 0) {
                        value_tex16.setText((String) model_.getColumns(row_).get("trans_net_weight"));
                    } else {
                        value_tex16.setText(String.format("%,d", (Integer) model_.getColumns(row_).get("net_weight")));
                    }
                    break;
                case 15:
                    goods_f21.setSelected(true);
                    //3桁区切り正味重量
                    if (((String) model_.getColumns(row_).get("trans_net_weight")).length() > 0) {
                        value_tex17.setText((String) model_.getColumns(row_).get("trans_net_weight"));
                    } else {
                        value_tex17.setText(String.format("%,d", (Integer) model_.getColumns(row_).get("net_weight")));
                    }
                    break;
                case 16:
                    goods_f22.setSelected(true);
                    //3桁区切り正味重量
                    if (((String) model_.getColumns(row_).get("trans_net_weight")).length() > 0) {
                        value_tex18.setText((String) model_.getColumns(row_).get("trans_net_weight"));
                    } else {
                        value_tex18.setText(String.format("%,d", (Integer) model_.getColumns(row_).get("net_weight")));
                    }
                    break;
                case 17:
                    goods_f23.setSelected(true);
                    //3桁区切り正味重量
                    if (((String) model_.getColumns(row_).get("trans_net_weight")).length() > 0) {
                        value_tex19.setText((String) model_.getColumns(row_).get("trans_net_weight"));
                    } else {
                        value_tex19.setText(String.format("%,d", (Integer) model_.getColumns(row_).get("net_weight")));
                    }
                    break;
                case 21:
                    goods_f27.setSelected(true);
                    //3桁区切り正味重量
                    if (((String) model_.getColumns(row_).get("trans_net_weight")).length() > 0) {
                        value_tex23.setText((String) model_.getColumns(row_).get("trans_net_weight"));
                    } else {
                        value_tex23.setText(String.format("%,d", (Integer) model_.getColumns(row_).get("net_weight")));
                    }
                    break;
            }
        } else {
            for (int i = 0; i < 10; i++) {
                switchLabel01:
                switch (Integer.valueOf((String) 按分品目Array[5][i])) {
                    //按分数量は計量データを正とする
                    case 1:
                        goods_f05.setSelected(true);
                        value_tex1.setText((String) 按分品目Array[2][i]);
                        break;
                    case 2:
                        goods_f06.setSelected(true);
                        value_tex2.setText((String) 按分品目Array[2][i]);
                        break;
                    case 3:
                        goods_f07.setSelected(true);
                        value_tex3.setText((String) 按分品目Array[2][i]);
                        break;
                    case 4:
                        goods_f08.setSelected(true);
                        value_tex4.setText((String) 按分品目Array[2][i]);
                        break;
                    case 5:
                        goods_f09.setSelected(true);
                        value_tex5.setText((String) 按分品目Array[2][i]);
                        break;
                    case 6:
                        goods_f10.setSelected(true);
                        //3桁区切り正味重量
                        value_tex6.setText((String) 按分品目Array[2][i]);
                        break;
                    case 7:
                        goods_f11.setSelected(true);
                        //3桁区切り正味重量
                        value_tex7.setText((String) 按分品目Array[2][i]);
                        break;
                    case 8:
                        goods_f12.setSelected(true);
                        value_tex8.setText((String) 按分品目Array[2][i]);
                        break;
                    case 11:
                        goods_f17.setSelected(true);
                        value_tex13.setText((String) 按分品目Array[2][i]);
                        break;
                    case 12:
                        goods_f18.setSelected(true);
                        value_tex14.setText((String) 按分品目Array[2][i]);
                        break;
                    case 13:
                        goods_f19.setSelected(true);
                        value_tex15.setText((String) 按分品目Array[2][i]);
                        break;
                    case 14:
                        goods_f20.setSelected(true);
                        value_tex16.setText((String) 按分品目Array[2][i]);
                        break;
                    case 15:
                        goods_f21.setSelected(true);
                        value_tex17.setText((String) 按分品目Array[2][i]);
                        break;
                    case 16:
                        goods_f22.setSelected(true);
                        value_tex18.setText((String) 按分品目Array[2][i]);
                        break;
                    case 17:
                        goods_f23.setSelected(true);
                        value_tex19.setText((String) 按分品目Array[2][i]);
                        break;
                    case 21:
                        goods_f27.setSelected(true);
                        value_tex23.setText((String) 按分品目Array[2][i]);
                        break;
                }
            }
        }
    }

    private void 印字初期値設定() {
        交付年月日_print_f_.setSelected(true);
        交付担当者_print_f_.setSelected(true);
        排出事業者_print_f.setSelected(true);
        事業場_print_f.setSelected(true);
        単位_print_f_.setSelected(false);
        goods_f40.setSelected(false);
        形状_print_f_.setSelected(false);
        荷姿_print_f_.setSelected(false);
        slash_f1.setSelected(true);
        slash_f2.setSelected(true);
        slash_f3.setSelected(true);
        運搬業者_print_f1.setSelected(true);
        運搬業者_print_f2.setSelected(false);
        処分業者_print_f1.setSelected(false);
        積み替え_f5.setSelected(false);
        運搬の受託_print_f3.setSelected(true);
        運搬の受託_print_f4.setSelected(false);
        処分の受託_print_f2.setSelected(false);
//        処分の受託_print_f3.setSelected(false);
//        最終処分確認_print_f.setSelected(false);
    }

//    private void 印字初期値設定() {
//        交付年月日_print_f_.setSelected(false);
//        交付担当者_print_f_.setSelected(false);
//        排出事業者_print_f.setSelected(false);
//        事業場_print_f.setSelected(false);
//        単位_print_f_.setSelected(false);
//        goods_f40.setSelected(false);
//        形状_print_f_.setSelected(false);
//        荷姿_print_f_.setSelected(false);
//        slash_f1.setSelected(false);
//        slash_f2.setSelected(false);
//        運搬業者_print_f1.setSelected(false);
//        運搬業者_print_f2.setSelected(false);
//        処分業者_print_f1.setSelected(false);
//        積み替え_f5.setSelected(false);
//        運搬の受託_print_f3.setSelected(false);
//        運搬の受託_print_f4.setSelected(false);
//        処分の受託_print_f2.setSelected(false);
////        処分の受託_print_f3.setSelected(false);
////        最終処分確認_print_f.setSelected(false);
//    }
    private void set建設パターン1() {
        処分の受託_print_f2.setSelected(false);
        処分の受託_print_f3.setSelected(true);
        最終処分確認_print_f.setSelected(true);
        cont_office_name01.setEnabled(true);
        cont_office_zip01.setEnabled(true);
        cont_office_tel01.setEnabled(true);
        cont_office_address01.setEnabled(true);
        check_f9.setEnabled(true);
        check_f10.setEnabled(true);
        check_f11.setEnabled(true);
        check_f12.setEnabled(true);
        check_f13.setEnabled(true);
        check_f14.setEnabled(true);
        cmb中間処理.setEnabled(true);
    }

    private void set建設パターン2() {
        cont_office_name01.setEnabled(false);
        cont_office_name01.setSelectedIndex(0);
        cont_office_zip01.setEnabled(false);
        cont_office_zip01.setText("");
        cont_office_tel01.setEnabled(false);
        cont_office_tel01.setText("");
        cont_office_address01.setEnabled(false);
        cont_office_address01.setText("");
        check_f9.setEnabled(false);
        check_f9.setSelected(false);
        check_f10.setEnabled(false);
        check_f10.setSelected(false);
        check_f11.setEnabled(false);
        check_f11.setSelected(false);
        check_f12.setEnabled(false);
        check_f12.setSelected(false);
        check_f13.setEnabled(false);
        check_f13.setSelected(false);
        check_f14.setEnabled(false);
        check_f14.setSelected(false);
        cmb中間処理.setEnabled(false);
        cmb中間処理.setSelectedIndex(0);
        処分業者_print_f1.setSelected(true);
        処分の受託_print_f2.setSelected(true);
        処分の受託_print_f3.setSelected(true);
        最終処分確認_print_f.setSelected(true);
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btn建設パターン1;
    private javax.swing.JButton btn建設パターン2;
    private javax.swing.JCheckBox check_f1;
    private javax.swing.JCheckBox check_f10;
    private javax.swing.JCheckBox check_f11;
    private javax.swing.JCheckBox check_f12;
    private javax.swing.JCheckBox check_f13;
    private javax.swing.JCheckBox check_f14;
    private javax.swing.JCheckBox check_f15;
    private javax.swing.JCheckBox check_f16;
    private javax.swing.JCheckBox check_f17;
    private javax.swing.JCheckBox check_f18;
    private javax.swing.JCheckBox check_f2;
    private javax.swing.JCheckBox check_f3;
    private javax.swing.JCheckBox check_f4;
    private javax.swing.JCheckBox check_f5;
    private javax.swing.JCheckBox check_f6;
    private javax.swing.JCheckBox check_f7;
    private javax.swing.JCheckBox check_f8;
    private javax.swing.JCheckBox check_f9;
    private javax.swing.JComboBox cmb中間処理;
    private javax.swing.JTextField cont_address01;
    private javax.swing.JTextField cont_address02;
    private javax.swing.JComboBox cont_cmb01;
    private javax.swing.JTextField cont_date2_day;
    private javax.swing.JTextField cont_date2_month;
    private javax.swing.JTextField cont_date2_year;
    private javax.swing.JTextField cont_day1;
    private javax.swing.JTextField cont_day2;
    private javax.swing.JTextField cont_month1;
    private javax.swing.JTextField cont_month2;
    private javax.swing.JTextField cont_name1;
    private javax.swing.JTextField cont_name2;
    private javax.swing.JTextField cont_office1;
    private javax.swing.JTextField cont_office2;
    private javax.swing.JTextField cont_office_address01;
    private javax.swing.JComboBox cont_office_name01;
    private javax.swing.JTextField cont_office_tel01;
    private javax.swing.JTextField cont_office_zip01;
    private javax.swing.JTextField cont_search_;
    private javax.swing.JTextField cont_search_1;
    private javax.swing.JTextField cont_tel01;
    private javax.swing.JTextField cont_tel02;
    private javax.swing.JTextField cont_year1;
    private javax.swing.JTextField cont_year2;
    private javax.swing.JTextField cont_zip01;
    private javax.swing.JTextField cont_zip02;
    private javax.swing.JTextField ent_address01;
    private javax.swing.JTextField ent_name;
    private javax.swing.JTextField ent_search_;
    private javax.swing.JTextField ent_tel01;
    private javax.swing.JTextField ent_zip01;
    private javax.swing.JCheckBox goods_f01;
    private javax.swing.JCheckBox goods_f02;
    private javax.swing.JCheckBox goods_f03;
    private javax.swing.JCheckBox goods_f04;
    private javax.swing.JCheckBox goods_f05;
    private javax.swing.JCheckBox goods_f06;
    private javax.swing.JCheckBox goods_f07;
    private javax.swing.JCheckBox goods_f08;
    private javax.swing.JCheckBox goods_f09;
    private javax.swing.JCheckBox goods_f10;
    private javax.swing.JCheckBox goods_f11;
    private javax.swing.JCheckBox goods_f12;
    private javax.swing.JCheckBox goods_f13;
    private javax.swing.JCheckBox goods_f14;
    private javax.swing.JCheckBox goods_f15;
    private javax.swing.JCheckBox goods_f16;
    private javax.swing.JCheckBox goods_f17;
    private javax.swing.JCheckBox goods_f18;
    private javax.swing.JCheckBox goods_f19;
    private javax.swing.JCheckBox goods_f20;
    private javax.swing.JCheckBox goods_f21;
    private javax.swing.JCheckBox goods_f22;
    private javax.swing.JCheckBox goods_f23;
    private javax.swing.JCheckBox goods_f24;
    private javax.swing.JCheckBox goods_f25;
    private javax.swing.JCheckBox goods_f26;
    private javax.swing.JCheckBox goods_f27;
    private javax.swing.JCheckBox goods_f28;
    private javax.swing.JCheckBox goods_f29;
    private javax.swing.JCheckBox goods_f30;
    private javax.swing.JCheckBox goods_f31;
    private javax.swing.JCheckBox goods_f32;
    private javax.swing.JCheckBox goods_f33;
    private javax.swing.JCheckBox goods_f34;
    private javax.swing.JCheckBox goods_f35;
    private javax.swing.JCheckBox goods_f36;
    private javax.swing.JCheckBox goods_f37;
    private javax.swing.JCheckBox goods_f38;
    private javax.swing.JCheckBox goods_f39;
    private javax.swing.JCheckBox goods_f40;
    private javax.swing.JCheckBox goods_f41;
    private javax.swing.JTextField grant_day;
    private javax.swing.JTextField grant_month;
    private javax.swing.JTextField grant_year;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel100;
    private javax.swing.JLabel jLabel101;
    private javax.swing.JLabel jLabel102;
    private javax.swing.JLabel jLabel103;
    private javax.swing.JLabel jLabel104;
    private javax.swing.JLabel jLabel105;
    private javax.swing.JLabel jLabel106;
    private javax.swing.JLabel jLabel107;
    private javax.swing.JLabel jLabel108;
    private javax.swing.JLabel jLabel109;
    private javax.swing.JLabel jLabel110;
    private javax.swing.JLabel jLabel111;
    private javax.swing.JLabel jLabel112;
    private javax.swing.JLabel jLabel113;
    private javax.swing.JLabel jLabel114;
    private javax.swing.JLabel jLabel116;
    private javax.swing.JLabel jLabel117;
    private javax.swing.JLabel jLabel118;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel120;
    private javax.swing.JLabel jLabel121;
    private javax.swing.JLabel jLabel122;
    private javax.swing.JLabel jLabel123;
    private javax.swing.JLabel jLabel124;
    private javax.swing.JLabel jLabel125;
    private javax.swing.JLabel jLabel126;
    private javax.swing.JLabel jLabel127;
    private javax.swing.JLabel jLabel128;
    private javax.swing.JLabel jLabel131;
    private javax.swing.JLabel jLabel133;
    private javax.swing.JLabel jLabel134;
    private javax.swing.JLabel jLabel135;
    private javax.swing.JLabel jLabel136;
    private javax.swing.JLabel jLabel137;
    private javax.swing.JLabel jLabel138;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel36;
    private javax.swing.JLabel jLabel37;
    private javax.swing.JLabel jLabel38;
    private javax.swing.JLabel jLabel39;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel42;
    private javax.swing.JLabel jLabel43;
    private javax.swing.JLabel jLabel44;
    private javax.swing.JLabel jLabel45;
    private javax.swing.JLabel jLabel46;
    private javax.swing.JLabel jLabel47;
    private javax.swing.JLabel jLabel49;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel50;
    private javax.swing.JLabel jLabel51;
    private javax.swing.JLabel jLabel54;
    private javax.swing.JLabel jLabel56;
    private javax.swing.JLabel jLabel57;
    private javax.swing.JLabel jLabel58;
    private javax.swing.JLabel jLabel61;
    private javax.swing.JLabel jLabel62;
    private javax.swing.JLabel jLabel63;
    private javax.swing.JLabel jLabel65;
    private javax.swing.JLabel jLabel66;
    private javax.swing.JLabel jLabel67;
    private javax.swing.JLabel jLabel68;
    private javax.swing.JLabel jLabel69;
    private javax.swing.JLabel jLabel70;
    private javax.swing.JLabel jLabel71;
    private javax.swing.JLabel jLabel73;
    private javax.swing.JLabel jLabel74;
    private javax.swing.JLabel jLabel75;
    private javax.swing.JLabel jLabel76;
    private javax.swing.JLabel jLabel78;
    private javax.swing.JLabel jLabel79;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel80;
    private javax.swing.JLabel jLabel81;
    private javax.swing.JLabel jLabel82;
    private javax.swing.JLabel jLabel83;
    private javax.swing.JLabel jLabel84;
    private javax.swing.JLabel jLabel85;
    private javax.swing.JLabel jLabel86;
    private javax.swing.JLabel jLabel87;
    private javax.swing.JLabel jLabel88;
    private javax.swing.JLabel jLabel89;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JLabel jLabel90;
    private javax.swing.JLabel jLabel91;
    private javax.swing.JLabel jLabel92;
    private javax.swing.JLabel jLabel93;
    private javax.swing.JLabel jLabel94;
    private javax.swing.JLabel jLabel96;
    private javax.swing.JLabel jLabel97;
    private javax.swing.JLabel jLabel98;
    private javax.swing.JLabel jLabel99;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel13;
    private javax.swing.JPanel jPanel14;
    private javax.swing.JPanel jPanel15;
    private javax.swing.JPanel jPanel16;
    private javax.swing.JPanel jPanel17;
    private javax.swing.JPanel jPanel18;
    private javax.swing.JPanel jPanel19;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel20;
    private javax.swing.JPanel jPanel21;
    private javax.swing.JPanel jPanel22;
    private javax.swing.JPanel jPanel23;
    private javax.swing.JPanel jPanel24;
    private javax.swing.JPanel jPanel25;
    private javax.swing.JPanel jPanel26;
    private javax.swing.JPanel jPanel27;
    private javax.swing.JPanel jPanel28;
    private javax.swing.JPanel jPanel29;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel30;
    private javax.swing.JPanel jPanel31;
    private javax.swing.JPanel jPanel33;
    private javax.swing.JPanel jPanel34;
    private javax.swing.JPanel jPanel35;
    private javax.swing.JPanel jPanel38;
    private javax.swing.JPanel jPanel39;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel40;
    private javax.swing.JPanel jPanel41;
    private javax.swing.JPanel jPanel42;
    private javax.swing.JPanel jPanel43;
    private javax.swing.JPanel jPanel44;
    private javax.swing.JPanel jPanel46;
    private javax.swing.JPanel jPanel47;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel52;
    private javax.swing.JPanel jPanel53;
    private javax.swing.JPanel jPanel55;
    private javax.swing.JPanel jPanel56;
    private javax.swing.JPanel jPanel57;
    private javax.swing.JPanel jPanel58;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JLabel jlbl_final_name_;
    private javax.swing.JComboBox last_cont_cmb;
    private javax.swing.JTextField manifesto_no2;
    private javax.swing.JTextField net_weight_field;
    private javax.swing.JTextField office_address01;
    private javax.swing.JComboBox office_id_cmb;
    private javax.swing.JTextField office_name_;
    private javax.swing.JTextField office_search_;
    private javax.swing.JTextField office_sub_name_;
    private javax.swing.JTextField office_tel01;
    private javax.swing.JTextField office_zip01;
    private javax.swing.JSpinner pring_num_;
    private javax.swing.JButton print_btn_kensetu;
    private javax.swing.JButton quit_btn_kensetu;
    private javax.swing.JButton rejist_btn_kensetu;
    private javax.swing.JCheckBox slash_f1;
    private javax.swing.JCheckBox slash_f2;
    private javax.swing.JCheckBox slash_f3;
    private javax.swing.JComboBox tantou_cmb;
    private javax.swing.JComboBox tantou_cmb1;
    private javax.swing.JTextField trans1_weight;
    private javax.swing.JTextField trans_address01;
    private javax.swing.JTextField trans_address02;
    private javax.swing.JComboBox trans_car_kind01;
    private javax.swing.JComboBox trans_car_kind02;
    private javax.swing.JTextField trans_car_no01;
    private javax.swing.JTextField trans_car_no02;
    private javax.swing.JComboBox trans_cmb01;
    private javax.swing.JComboBox trans_cmb02;
    private javax.swing.JTextField trans_day1;
    private javax.swing.JTextField trans_day2;
    private javax.swing.JTextField trans_month1;
    private javax.swing.JTextField trans_month2;
    private javax.swing.JTextField trans_name1;
    private javax.swing.JTextField trans_name2;
    private javax.swing.JTextField trans_office1;
    private javax.swing.JTextField trans_office2;
    private javax.swing.JTextField trans_search_;
    private javax.swing.JTextField trans_search_1;
    private javax.swing.JComboBox trans_tantou_id_cmb1;
    private javax.swing.JComboBox trans_tantou_id_cmb2;
    private javax.swing.JTextField trans_tantou_search_1;
    private javax.swing.JTextField trans_tantou_search_2;
    private javax.swing.JTextField trans_tel01;
    private javax.swing.JTextField trans_tel02;
    private javax.swing.JTextField trans_year1;
    private javax.swing.JTextField trans_year2;
    private javax.swing.JTextField trans_zip01;
    private javax.swing.JTextField trans_zip02;
    private javax.swing.JTextArea txt備考;
    private javax.swing.JTextField value_tex1;
    private javax.swing.JTextField value_tex10;
    private javax.swing.JTextField value_tex11;
    private javax.swing.JTextField value_tex12;
    private javax.swing.JTextField value_tex13;
    private javax.swing.JTextField value_tex14;
    private javax.swing.JTextField value_tex15;
    private javax.swing.JTextField value_tex16;
    private javax.swing.JTextField value_tex17;
    private javax.swing.JTextField value_tex18;
    private javax.swing.JTextField value_tex19;
    private javax.swing.JTextField value_tex2;
    private javax.swing.JTextField value_tex20;
    private javax.swing.JTextField value_tex21;
    private javax.swing.JTextField value_tex22;
    private javax.swing.JTextField value_tex23;
    private javax.swing.JTextField value_tex24;
    private javax.swing.JTextField value_tex25;
    private javax.swing.JTextField value_tex26;
    private javax.swing.JTextField value_tex27;
    private javax.swing.JTextField value_tex28;
    private javax.swing.JTextField value_tex3;
    private javax.swing.JTextField value_tex4;
    private javax.swing.JTextField value_tex5;
    private javax.swing.JTextField value_tex6;
    private javax.swing.JTextField value_tex7;
    private javax.swing.JTextField value_tex8;
    private javax.swing.JTextField value_tex9;
    private javax.swing.JCheckBox 事業場_print_f;
    private javax.swing.JCheckBox 交付年月日_print_f_;
    private javax.swing.JCheckBox 交付担当者_print_f_;
    private javax.swing.JComboBox 保管場所_cmb;
    private javax.swing.JCheckBox 処分の受託_print_f2;
    private javax.swing.JCheckBox 処分の受託_print_f3;
    private javax.swing.JCheckBox 処分業者_print_f1;
    private javax.swing.JCheckBox 単位_print_f_;
    private javax.swing.JCheckBox 形状_print_f_;
    private javax.swing.JCheckBox 排出事業者_print_f;
    private javax.swing.JTextField 最終処分担当者;
    private javax.swing.JCheckBox 最終処分確認_print_f;
    private javax.swing.JTextField 最終処分社名;
    private javax.swing.JButton 画面初期化_btn_;
    private javax.swing.JCheckBox 積み替え_f5;
    private javax.swing.JCheckBox 荷姿_print_f_;
    private javax.swing.JCheckBox 運搬の受託_print_f3;
    private javax.swing.JCheckBox 運搬の受託_print_f4;
    private javax.swing.JCheckBox 運搬業者_print_f1;
    private javax.swing.JCheckBox 運搬業者_print_f2;
    // End of variables declaration//GEN-END:variables

    protected void doClose() {
        Window w = SwingUtilities.getWindowAncestor(this);
        if (w instanceof Frame) {
            w.dispose();

        } else {
            w.dispose();
        }
        if (DBManager.mode == 100) {
            System.exit(-1);
        }
    }
}
