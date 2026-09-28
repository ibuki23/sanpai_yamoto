package aks.industrialwaste.recycle.ui.manifesto;

import aks.industrialwaste.database.DBManager;
import aks.industrialwaste.recycle.report.XlsxManifesto;
import java.awt.Window;
import java.awt.Frame;
import java.util.*;
import javax.swing.*;
import aks.industrialwaste.recycle.ui.sql.*;
import aks.industrialwaste.recycle.ui.weighlist.*;
import aks.industrialwaste.recycle.util.ContSelector;
import aks.industrialwaste.recycle.util.EntSelector;
import aks.industrialwaste.recycle.util.TransSelector;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Locale;
import java.sql.Connection;
import aks.swing.MyLineSlash1;
import java.awt.GridBagLayout;
import java.awt.event.KeyEvent;

public class SanngyouManifestoPanel100 extends javax.swing.JPanel {

    private SQLCommon mSQLCommon;
    public String[][] entName2Array = new String[7][];
    public String[][] officeName2Array = new String[9][];
    public String[][] transName2Array = new String[7][];
    public String[][] contName2Array = new String[15][];
    public String[][] methotName2Array = new String[6][];
    public String[][] contOfficeName2Array = new String[6][];
    public String[][] remarkName2Array = new String[2][];
    public String[][] grantName2Array = new String[2][];
    public String[][] maniName2Array = new String[10][];
    public String[][] transTantouArray1 = new String[5][];
    //選択されている行の各idを取得する
    public int ent_id_ = 0;
    public int office_id_ = 0;
    public int trans_id_ = 0;
    public int cont_id_ = 0;
    public int methot_id_ = 0;
    public int cont_office_id_ = 0;
    //取得したidを要素とする配列のindexを取得する(表示のため)
    public int ent_id_index = 0;
    public int office_id_index = 0;
    public int trans_id_index = 0;
    public int cont_id_index = 0;
    public int methot_id_index = 0;
    public int cont_office_id_index = 0;
    public int row_ = -1;
    private Object[] nisugataString = new String[4];
    private Connection con_;
    private WeighListTableModel model_;
    private MyLineSlash1 MyLine1, MyLine2, MyLine3;
    public String[][] 処分方法Array = new String[2][];
    private int mode_;
    private String[] 単位_ = new String[4];
    public String[][] 保管場所Array = new String[6][];
    private Long sno_;
    public String[][] 按分品目Array = new String[6][];
    private String s備考欄 = "";

    public SanngyouManifestoPanel100(WeighListTableModel model, int row, Connection con, int mode) {
        initComponents();
        model_ = model;
        con_ = con;
        mode_ = mode;
        row_ = row;

        if (mode_ == 0) {
            jLabel20.setVisible(true);
            pring_num_.setVisible(true);
            pring_num_.setValue(0);
            regist_btn.setVisible(false);
            画面初期化_btn_.setVisible(true);
            //マニフェストNO
            manifesto_no1.setText("");
        } else {
            jLabel20.setVisible(false);
            pring_num_.setVisible(false);
            pring_num_.setValue(1);
            regist_btn.setVisible(true);
            画面初期化_btn_.setVisible(false);
            //マニフェストNO
            manifesto_no1.setText((String) model.getColumns(row_).get("manifesto_no1"));
        }
        // 0:空伝 1:通常
        mode_ = mode;
        if (mode_ == 0) {
            sno_ = (Long) 0l;
        } else {
            sno_ = (Long) model_.getColumns(row_).get("sno");
        }

        row_ = row;

        mSQLCommon = new SQLCommon(DBManager.getURL(), DBManager.getUser(), DBManager.getPassword());

        単位_ = new String[]{"kg", "t", "m3", "L"};
        単位_cmb.setModel(new javax.swing.DefaultComboBoxModel(単位_));

        nisugataString = new String[]{"", "バラ", "コンテナ", "ドラム缶", "袋", "専用容器",
            !((String) model.getColumns(row_).get("nisugata_name")).equals("バラ")
            && !((String) model.getColumns(row_).get("nisugata_name")).equals("コンテナ")
            && !((String) model.getColumns(row_).get("nisugata_name")).equals("ドラム缶")
            && !((String) model.getColumns(row_).get("nisugata_name")).equals("袋")
            && !((String) model.getColumns(row_).get("nisugata_name")).equals("専用容器")
            ? (String) model.getColumns(row_).get("nisugata_name") : ""

        };
        mSQLCommon.GetMasterData8_1("v0_manifesto_5", 処分方法Array);
        String[] 処分方法Name = new String[処分方法Array[0].length];
        for (int i = 0; i <= 処分方法Array[0].length - 1; i++) {
            処分方法Name[i] = (String) 処分方法Array[1][i];
            if (((String) model.getColumns(row_).get("method_name")).equals(String.valueOf((String) 処分方法Array[1][i]))) {
            }
        }
        method_cmb.setModel(new javax.swing.DefaultComboBoxModel(処分方法Name));
//        if (((String) model.getColumns(row_).get("method_name")).length() > 0) {
        method_cmb.setSelectedItem("破砕");
//        }
        nisugata_cmb.setModel(new javax.swing.DefaultComboBoxModel(nisugataString));
        if (((String) model.getColumns(row_).get("nisugata_name")).length() > 0) {
            nisugata_cmb.setSelectedItem((String) model.getColumns(row_).get("nisugata_name"));
        } else {
            nisugata_cmb.setSelectedItem("バラ");
        }

        //選択されている行の各idを取得する
        ent_id_ = (Integer) model.getColumns(row_).get("dis_ent_id");
        office_id_ = (Integer) model.getColumns(row_).get("dis_office_id");
        trans_id_ = (Integer) model.getColumns(row_).get("trans_id");
        cont_id_ = (Integer) model.getColumns(row_).get("dis_cont_id");
        methot_id_ = (Integer) model.getColumns(row_).get("method_id");
        cont_office_id_ = (Integer) model.getColumns(row_).get("manifesto3_id1");

        //ごみの種類によってチェックボックスを選択する
        if ((Boolean) model.getColumns(row_).get("f按分フラグ") && sno_ > 0) {
            //按分有の場合の数量表示
            mSQLCommon.Get按分Data(sno_, 按分品目Array);
            check_goods_flag(true);
        } else {
            check_goods_flag(false);
        }

        //コンボを編集可能にする
        tantou_cmb.setEditable(true);
        last_cont_cmb.setEditable(true);
        nisugata_cmb.setEditable(true);

        //交付年月日()　元号使用止め西暦表示、入力に変更　180918
        if ((Date) model.getColumns(row_).get("grant_date1") != null) {
            grant_year.setText(String.format("%tY", (Date) model.getColumns(row_).get("grant_date1")));
            grant_month.setText(String.format("%tm", (Date) model.getColumns(row_).get("grant_date1")));
            grant_day.setText(String.format("%td", (Date) model.getColumns(row_).get("grant_date1")));
        } else {
            grant_year.setText(String.format("%tY", (Date) model.getColumns(row_).get("term_date")));
            grant_month.setText(String.format("%tm", (Date) model.getColumns(row_).get("term_date")));
            grant_day.setText(String.format("%td", (Date) model.getColumns(row_).get("term_date")));
        }

        //マニフェストNO
        manifesto_no1.setText((String) model.getColumns(row_).get("manifesto_no1"));

        //マスタ問い合わせ
        //排出事業者
        mSQLCommon.GetMasterData("v0_ent", entName2Array);
        String[] entName = new String[entName2Array[0].length];
        for (int i = 0; i <= entName2Array[0].length - 1; i++) {
            entName[i] = (String) entName2Array[1][i];
            if (ent_id_ == Integer.valueOf((String) entName2Array[0][i])) {
                ent_id_index = i;
            }
        }
        //排出事業場
        mSQLCommon.GetMasterDataOffice("v0_office", officeName2Array, ent_id_);
        String[] OfficeId = new String[officeName2Array[0].length];
        for (int i = 0; i < officeName2Array[0].length; i++) {
            if ((String) officeName2Array[0][i] != null) {
                OfficeId[i] = (String) officeName2Array[0][i];

                if (office_id_ == Integer.valueOf((String) officeName2Array[0][i])) {
                    office_id_index = i;
                }
            } else {
                OfficeId[i] = "0";
            }
        }
        office_id01.setModel(new javax.swing.DefaultComboBoxModel(OfficeId));

        //運搬業者
        mSQLCommon.GetMasterData("v0_trans", transName2Array);
        String[] TransName = new String[transName2Array[0].length];
        for (int i = 0; i <= transName2Array[0].length - 1; i++) {
            TransName[i] = (String) transName2Array[1][i];
            if (trans_id_ == Integer.valueOf((String) transName2Array[0][i])) {
                trans_id_index = i;
            }
        }
        trans_cmb01.setModel(new javax.swing.DefaultComboBoxModel(TransName));

        //処分業者
        mSQLCommon.GetMasterData2("v0_cont", contName2Array);
        String[] ContName = new String[contName2Array[0].length];
        for (int i = 0; i <= contName2Array[0].length - 1; i++) {
            ContName[i] = (String) contName2Array[1][i];
            if (cont_id_ == Integer.valueOf((String) contName2Array[0][i])) {
                cont_id_index = i;
            }
        }
        cont_cmb01.setModel(new javax.swing.DefaultComboBoxModel(ContName));
        //運搬先の事業場
        mSQLCommon.GetMasterData4_1("v0_manifesto_3", contOfficeName2Array, cont_id_);
        String[] ContOfficeName = new String[contOfficeName2Array[0].length];
        String[] LastContName = new String[contOfficeName2Array[0].length];
        for (int i = 0; i <= contOfficeName2Array[0].length - 1; i++) {
            ContOfficeName[i] = (String) contOfficeName2Array[1][i];
            LastContName[i] = (String) contOfficeName2Array[1][i] + "　"
                     + (String) contOfficeName2Array[2][i] + (String) contOfficeName2Array[3][i];
            if (cont_office_id_ == Integer.valueOf((String) contOfficeName2Array[0][i])) {
                cont_office_id_index = i;
            }

        }
        cont_office_name01.setModel(new javax.swing.DefaultComboBoxModel(ContOfficeName));
        last_cont_cmb.setModel(new javax.swing.DefaultComboBoxModel(LastContName));

        //備考
        txt備考.setText(((String) model.getColumns(row_).get("remark_name")).length() > 0
                 ? (String) model.getColumns(row_).get("remark_name")
                 : s備考欄);

        //担当者
        mSQLCommon.GetMasterData6("v0_manifesto_1", grantName2Array);
        String[] GrantName = new String[grantName2Array[0].length];
        for (int i = 0; i <= grantName2Array[0].length - 1; i++) {
            GrantName[i] = (String) grantName2Array[1][i];
            if (((String) model.getColumns(row_).get("grant_name")).equals(String.valueOf((String) grantName2Array[1][i]))) {
            }
        }
        tantou_cmb.setModel(new javax.swing.DefaultComboBoxModel(GrantName));
        tantou_cmb.setSelectedItem((String) model.getColumns(row_).get("grant_name"));

        //担当者所属
        mSQLCommon.GetMasterData7("v0_manifesto", maniName2Array);
        String[] GrantName01 = new String[maniName2Array[0].length + 1];

        GrantName01[0] = "";
        GrantName01[1] = (String) maniName2Array[1][0];
        for (int i = 0; i <= GrantName01.length - 1; i++) {
            if (((String) model.getColumns(row_).get("grant_name01")).equals(GrantName01[i])) {
            }

        }

        /////////////////////////////////////////////////////////////////////
        mSQLCommon.GetMasterDataTransTantou("v0_trans_tantou", transTantouArray1, trans_id_);
        if (transTantouArray1[0].length > 0) {
            String[] TransTantouID1 = new String[transTantouArray1[0].length];
            for (int i = 0; i < transTantouArray1[0].length; i++) {
                TransTantouID1[i] = (String) transTantouArray1[3][i];
            }
            trans_tantou_id_cmb1.setModel(new javax.swing.DefaultComboBoxModel(TransTantouID1));
        }

        ent_search_.setText((String) entName2Array[0][ent_id_index]);
        ent_name.setText((String) entName2Array[6][ent_id_index]);
        ent_zip01.setText((String) entName2Array[2][ent_id_index]);
        ent_tel01.setText((String) entName2Array[3][ent_id_index]);
        ent_address01.setText((String) entName2Array[4][ent_id_index] + (String) entName2Array[5][ent_id_index]);

        office_id01.setSelectedIndex(office_id_index);
        office_zip01.setText((String) officeName2Array[2][office_id_index]);
        office_tel01.setText((String) officeName2Array[3][office_id_index]);
        office_address01.setText((String) officeName2Array[4][office_id_index]
                 + (String) officeName2Array[5][office_id_index]);
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
        last_cont_cmb.setSelectedItem((String) model.getColumns(0).get("last_office_name"));
//        last_cont_cmb.setSelectedItem((String) model.getColumns(row_).get("last_office_name"));

        trans_cmb01.setSelectedIndex(trans_id_index);
        trans_zip01.setText((String) transName2Array[2][trans_id_index]);
        trans_tel01.setText((String) transName2Array[3][trans_id_index]);
        trans_address01.setText((String) transName2Array[4][trans_id_index]
                 + (String) transName2Array[5][trans_id_index]);

//        cont_cmb01.setSelectedIndex(cont_id_index);
        cont_cmb01.setSelectedIndex(1);
        cont_zip01.setText((String) contName2Array[2][cont_id_index]);
        cont_tel01.setText((String) contName2Array[3][cont_id_index]);
        cont_address01.setText((String) contName2Array[4][cont_id_index]
                 + (String) contName2Array[5][cont_id_index]);

//        cont_office_name01.setSelectedIndex(cont_office_id_index);
        cont_office_name01.setSelectedIndex(1);
        cont_office_zip01.setText((String) contOfficeName2Array[4][cont_office_id_index]);
        cont_office_tel01.setText((String) contOfficeName2Array[5][cont_office_id_index]);
        cont_office_address01.setText((String) contOfficeName2Array[2][cont_office_id_index]
                 + (String) contOfficeName2Array[3][cont_office_id_index]);

        単位_cmb.setSelectedIndex(1);
//        //3桁区切り正味重量
        if (((String) model.getColumns(row_).get("trans_net_weight")).indexOf("kg") > 0) {
            単位_cmb.setSelectedIndex(0);
        } else if (((String) model.getColumns(row_).get("trans_net_weight")).indexOf("t") > 0) {
            単位_cmb.setSelectedIndex(1);
        } else if (((String) model.getColumns(row_).get("trans_net_weight")).indexOf("m3") > 0) {
            単位_cmb.setSelectedIndex(2);
        } else if (((String) model.getColumns(row_).get("trans_net_weight")).indexOf("L") > 0) {
            単位_cmb.setSelectedIndex(3);
        } else {
//            単位_cmb.setSelectedIndex(1);
            単位_cmb.setSelectedIndex(0);
        }
        if (単位_cmb.getSelectedIndex() == 1) {
            int net = (Integer) model_.getColumns(row_).get("net_weight");
            net_weight_field.setText(String.valueOf((double) net / 1000));
        } else {
            net_weight_field.setText(String.format("%,d", (Integer) model.getColumns(row_).get("net_weight")));
        }

        //ごみ種名称//goods_name
        if (((String) model.getColumns(row_).get("trans3_sub_name")).length() > 0) {
            goods_name_field.setText((String) model.getColumns(row_).get("trans3_sub_name"));//印刷用廃棄物名に代用
        } else {
            goods_name_field.setText((String) model.getColumns(row_).get("goods_name"));//印刷用廃棄物名に代用    
        }

        //有害物質等初期値　
        yugai_field.setText("なし");

        //選択済みとする
        slash_f1.setSelected(true);
        slash_f2.setSelected(false);
        //当欄記載の通りにチェックで『中央公害清掃㈱松山事業所』を表示する
        check_f3.setSelected((Boolean) model.getColumns(row_).get("check_flag03"));
        check_f4.setSelected((Boolean) model.getColumns(row_).get("check_flag04"));

        if (check_f4.isSelected() && !(check_f3.isSelected())) {
            jlbl_final_name_.setVisible(check_f4.isSelected());
        } else {
            jlbl_final_name_.setVisible(false);
        }

        if (((String) model.getColumns(row_).get("trans_office_sub_name1")).equals("")) {
            trans1_sub_name.setText(trans_cmb01.getSelectedItem().toString());
        } else {
            trans1_sub_name.setText((String) model.getColumns(row_).get("trans_office_sub_name1"));
        }
        if (((String) model.getColumns(row_).get("trans1_sub_name")).equals("")) {
        } else {
            trans1_sub_name1.setText((String) model.getColumns(row_).get("trans1_sub_name"));
        }

        if (((String) model.getColumns(row_).get("cont_office_sub_name1")).equals("")) {
            //初回入力が無ければ処分担当者を表示
            cont_sub_name.setText(cont_cmb01.getSelectedItem().toString());
        } else {
            cont_sub_name.setText((String) model.getColumns(row_).get("cont_office_sub_name1"));
        }
        if (((String) model.getColumns(row_).get("cont_sub_name")).equals("")) {
        } else {
            cont_sub_name1.setText((String) model.getColumns(row_).get("cont_sub_name"));
        }

        //　元号使用止め西暦表示、入力に変更　180918
//        if ((Date) model.getColumns(row_).get("trans1_date") != null) {
//            trans1_date_year.setText(String.format("%tY", (Date) model.getColumns(row_).get("trans1_date")));
//            trans1_date_month.setText(String.format("%tm", (Date) model.getColumns(row_).get("trans1_date")));
//            trans1_date_day.setText(String.format("%td", (Date) model.getColumns(row_).get("trans1_date")));
//        } else if ((Date) model.getColumns(row_).get("cont_date2") != null) {
//            trans1_date_year.setText(String.format("%tY", (Date) model.getColumns(row_).get("cont_date2")));
//            trans1_date_month.setText(String.format("%tm", (Date) model.getColumns(row_).get("cont_date2")));
//            trans1_date_day.setText(String.format("%td", (Date) model.getColumns(row_).get("cont_date2")));
//        } else if ((Date) model.getColumns(row_).get("grant_date1") != null) {
//            trans1_date_year.setText(String.format("%tY", (Date) model.getColumns(row_).get("grant_date1")));
//            trans1_date_month.setText(String.format("%tm", (Date) model.getColumns(row_).get("grant_date1")));
//            trans1_date_day.setText(String.format("%td", (Date) model.getColumns(row_).get("grant_date1")));
//        } else {
//            trans1_date_year.setText("");
//            trans1_date_month.setText("");
//            trans1_date_day.setText("");
//        }
        Calendar now = Calendar.getInstance();

        String cont_年1 = String.valueOf(now.get(Calendar.YEAR));
        String cont_月1 = String.valueOf(now.get(Calendar.MONTH) + 1); // 月は0始まりなので+1
        String cont_日1 = String.valueOf(now.get(Calendar.DAY_OF_MONTH));
//        trans1_date_year.setText(cont_年1);
//        trans1_date_month.setText(cont_月1);
//        trans1_date_day.setText(cont_日1);

        trans1_date_year.setEnabled(false);
        trans1_date_month.setEnabled(false);
        trans1_date_day.setEnabled(false);
        cont_date1_year.setText(cont_年1);
        cont_date1_month.setText(cont_月1);
        cont_date1_day.setText(cont_日1);
        cont_date2_year.setText(cont_年1);
        cont_date2_month.setText(cont_月1);
        cont_date2_day.setText(cont_日1);

        if ((Date) model.getColumns(row_).get("cont_date2") != null) {
//            cont_date1_year.setText(String.format("%tY", (Date) model.getColumns(row_).get("cont_date2")));
//            cont_date1_month.setText(String.format("%tm", (Date) model.getColumns(row_).get("cont_date2")));
//            cont_date1_day.setText(String.format("%td", (Date) model.getColumns(row_).get("cont_date2")));
        } else if ((Date) model.getColumns(row_).get("grant_date1") != null) {
//            cont_date1_year.setText(String.format("%tY", (Date) model.getColumns(row_).get("grant_date1")));
//            cont_date1_month.setText(String.format("%tm", (Date) model.getColumns(row_).get("grant_date1")));
//            cont_date1_day.setText(String.format("%td", (Date) model.getColumns(row_).get("grant_date1")));
        } else {

        }
        //最終処分年月日  元号使用止め西暦表示、入力に変更　180918
        if ((Date) model.getColumns(row_).get("disposal_date2") != null) {
//            cont_date2_year.setText(String.format("%tY", (Date) model.getColumns(row_).get("disposal_date2")));
//            cont_date2_month.setText(String.format("%tm", (Date) model.getColumns(row_).get("disposal_date2")));
//            cont_date2_day.setText(String.format("%td", (Date) model.getColumns(row_).get("disposal_date2")));
        } else {

        }
        //印字フラグ
        Object o = model.getColumns(row_).get("ent_print_flag");
        if ((Boolean) o) {
            排出事業者_print_f.setSelected((Boolean) o);
        } else {
            排出事業者_print_f.setSelected(false);
        }
        o = model.getColumns(row_).get("trans1_print_flag");
        if ((Boolean) o) {
            運搬受託者_print_f.setSelected((Boolean) o);
        } else {
            運搬受託者_print_f.setSelected(false);
        }
        o = model.getColumns(row_).get("office_print_flag");
        if ((Boolean) o) {
            排出事業場_print_f.setSelected((Boolean) o);
        } else {
            排出事業場_print_f.setSelected(false);
        }
        o = model.getColumns(row_).get("cont_print_flag");
        if ((Boolean) o) {
            処分受託者_print_f.setSelected((Boolean) o);
        } else {
            処分受託者_print_f.setSelected(false);
        }

        //交付年月日チェック印字フラグ　true
        交付年月日_print_f_.setSelected((Boolean) model.getColumns(row_).get("grant2_print_f"));
        //交付担当者チェック印字フラグ　true
        交付担当者_print_f_.setSelected((Boolean) model.getColumns(row_).get("tantou_print_f"));
        //品目チェック印字フラグ　true
        単位_print_f_.setSelected((Boolean) model.getColumns(row_).get("tani_print_f"));
        //数量荷姿名称印刷フラグ　true
        形状_print_f_.setSelected((Boolean) model.getColumns(row_).get("keijyo_print_f"));
        //処分方法備考印刷フラグ　true
        荷姿_print_f_.setSelected((Boolean) model.getColumns(row_).get("nisugata_print_f"));

        //運搬担当者会社名チェック印字フラグ　true
        運搬担当者_print_3.setSelected((Boolean) model.getColumns(row_).get("trans_sub1_print_flag"));
        //運搬担当者チェック印字フラグ　true
        運搬担当者_print_f4.setSelected((Boolean) model.getColumns(row_).get("trans_sub2_print_flag"));
        //処分担当者会社名チェック印字フラグ　true
        処分担当者_print_f2.setSelected((Boolean) model.getColumns(row_).get("cont_sub1_print_flag"));
        //処分担当者チェック印字フラグ　true
        処分担当者_print_f3.setSelected((Boolean) model.getColumns(row_).get("cont_sub2_print_flag"));

        MyLine1 = new MyLineSlash1();
        MyLine1.setLayout(new GridBagLayout());
        MyLine1.setMinimumSize(new java.awt.Dimension(520, 95));
        MyLine1.setPreferredSize(new java.awt.Dimension(520, 95));

        javax.swing.GroupLayout MyLine1Layout = new javax.swing.GroupLayout(MyLine1);
        MyLine1.setLayout(MyLine1Layout);
        MyLine1Layout.setHorizontalGroup(
                 MyLine1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                          .addGap(0, 90, Short.MAX_VALUE));
        MyLine1Layout.setVerticalGroup(
                 MyLine1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                          .addGap(0, 36, Short.MAX_VALUE));
        java.awt.GridBagConstraints gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.gridheight = 1;
        gridBagConstraints.gridwidth = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.CENTER;
        jPanel34.add(MyLine1, gridBagConstraints);

        MyLine2 = new MyLineSlash1();
        MyLine2.setLayout(new GridBagLayout());
        MyLine2.setMinimumSize(new java.awt.Dimension(230, 25));
        MyLine2.setPreferredSize(new java.awt.Dimension(230, 25));

        javax.swing.GroupLayout MyLine2Layout = new javax.swing.GroupLayout(MyLine2);
        MyLine2.setLayout(MyLine2Layout);
        MyLine2Layout.setHorizontalGroup(
                 MyLine2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                          .addGap(0, 90, Short.MAX_VALUE));
        MyLine2Layout.setVerticalGroup(
                 MyLine1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                          .addGap(0, 36, Short.MAX_VALUE));
        java.awt.GridBagConstraints gridBagConstraints2 = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.gridheight = 1;
        gridBagConstraints.gridwidth = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.CENTER;
        jPanel7.add(MyLine2, gridBagConstraints2);
        MyLine2.setVisible(false);

        積み替え_f.setSelected(true);

        last_cont_cmb.setSelectedItem((String) model.getColumns(row_).get("last_office_name"));
        //積み替え保管場所
        mSQLCommon.GetMasterData4("v0_tsumikae", 保管場所Array);
        String[] 保管場所Name = new String[保管場所Array[0].length];
        int 保管場所_index = 0;
        for (int i = 0; i <= 保管場所Array[0].length - 1; i++) {
            保管場所Name[i] = (String) 保管場所Array[1][i].toString();
            if (((String) model.getColumns(row_).get("last_cont_name02")).equals(String.valueOf((String) 保管場所Array[1][i]))) {
                保管場所_index = i;
            }
        }
        保管場所_cmb.setModel(new javax.swing.DefaultComboBoxModel(保管場所Name));
        保管場所_cmb.setSelectedIndex(保管場所_index);

        trans1_sub_name1.setVisible(false);
        trans1_sub_name2.setVisible(false);

        if (((String) model.getColumns(row_).get("trans1_sub_name")).equals("")) {
            trans1_sub_name1.setText("");
        } else {
            trans1_sub_name1.setText((String) model.getColumns(row_).get("trans1_sub_name"));
            trans_tantou_id_cmb1.setSelectedItem((String) model.getColumns(row_).get("trans1_sub_name"));
        }

        印字初期値設定();
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        menuBar1 = new java.awt.MenuBar();
        menu1 = new java.awt.Menu();
        menu2 = new java.awt.Menu();
        menuBar2 = new java.awt.MenuBar();
        menu3 = new java.awt.Menu();
        menu4 = new java.awt.Menu();
        label1 = new java.awt.Label();
        jCheckBox78 = new javax.swing.JCheckBox();
        jScrollPane2 = new javax.swing.JScrollPane();
        jPanel34 = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        cont_date2_year = new aks.swing.JTextFieldHankaku();
        jLabel68 = new javax.swing.JLabel();
        cont_date2_month = new aks.swing.JTextFieldHankaku();
        jLabel69 = new javax.swing.JLabel();
        cont_date2_day = new aks.swing.JTextFieldHankaku();
        jLabel70 = new javax.swing.JLabel();
        jLabel78 = new javax.swing.JLabel();
        cont_date1_year = new aks.swing.JTextFieldHankaku();
        jLabel89 = new javax.swing.JLabel();
        cont_date1_month = new aks.swing.JTextFieldHankaku();
        jLabel90 = new javax.swing.JLabel();
        cont_date1_day = new aks.swing.JTextFieldHankaku();
        jLabel95 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        jCheckBox58 = new javax.swing.JCheckBox();
        jCheckBox59 = new javax.swing.JCheckBox();
        jCheckBox60 = new javax.swing.JCheckBox();
        goods_f42 = new javax.swing.JCheckBox();
        jCheckBox62 = new javax.swing.JCheckBox();
        jCheckBox63 = new javax.swing.JCheckBox();
        goods_f22 = new javax.swing.JCheckBox();
        goods_f40 = new javax.swing.JCheckBox();
        goods_f39 = new javax.swing.JCheckBox();
        goods_f38 = new javax.swing.JCheckBox();
        goods_f37 = new javax.swing.JCheckBox();
        goods_f35 = new javax.swing.JCheckBox();
        goods_f36 = new javax.swing.JCheckBox();
        goods_f27 = new javax.swing.JCheckBox();
        goods_f26 = new javax.swing.JCheckBox();
        goods_f25 = new javax.swing.JCheckBox();
        goods_f30 = new javax.swing.JCheckBox();
        goods_f31 = new javax.swing.JCheckBox();
        goods_f28 = new javax.swing.JCheckBox();
        goods_f29 = new javax.swing.JCheckBox();
        goods_f34 = new javax.swing.JCheckBox();
        goods_f32 = new javax.swing.JCheckBox();
        goods_f33 = new javax.swing.JCheckBox();
        goods_f10 = new javax.swing.JCheckBox();
        goods_f09 = new javax.swing.JCheckBox();
        goods_f12 = new javax.swing.JCheckBox();
        goods_f11 = new javax.swing.JCheckBox();
        goods_f13 = new javax.swing.JCheckBox();
        goods_f19 = new javax.swing.JCheckBox();
        goods_f18 = new javax.swing.JCheckBox();
        goods_f20 = new javax.swing.JCheckBox();
        goods_f21 = new javax.swing.JCheckBox();
        goods_f23 = new javax.swing.JCheckBox();
        goods_f24 = new javax.swing.JCheckBox();
        goods_f14 = new javax.swing.JCheckBox();
        goods_f15 = new javax.swing.JCheckBox();
        goods_f16 = new javax.swing.JCheckBox();
        goods_f07 = new javax.swing.JCheckBox();
        goods_f08 = new javax.swing.JCheckBox();
        goods_f05 = new javax.swing.JCheckBox();
        goods_f06 = new javax.swing.JCheckBox();
        goods_f01 = new javax.swing.JCheckBox();
        goods_f04 = new javax.swing.JCheckBox();
        goods_f17 = new javax.swing.JCheckBox();
        goods_f02 = new javax.swing.JCheckBox();
        goods_f03 = new javax.swing.JCheckBox();
        jPanel37 = new javax.swing.JPanel();
        形状_print_f_ = new javax.swing.JCheckBox();
        jLabel4 = new javax.swing.JLabel();
        net_weight_field = new aks.swing.JTextField();
        jLabel16 = new javax.swing.JLabel();
        荷姿_print_f_ = new javax.swing.JCheckBox();
        単位_cmb = new javax.swing.JComboBox();
        jLabel21 = new javax.swing.JLabel();
        method_cmb = new javax.swing.JComboBox();
        jLabel19 = new javax.swing.JLabel();
        slash_f3 = new javax.swing.JCheckBox();
        jLabel18 = new javax.swing.JLabel();
        goods_name_field = new aks.swing.JTextField();
        nisugata_cmb = new aks.swing.JComboBox();
        jPanel7 = new javax.swing.JPanel();
        yugai_field = new aks.swing.JTextField();
        jPanel42 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        txt備考 = new javax.swing.JTextArea();
        jLabel17 = new javax.swing.JLabel();
        slash_f4 = new javax.swing.JCheckBox();
        goods_f43 = new javax.swing.JCheckBox();
        goods_f44 = new javax.swing.JCheckBox();
        goods_f46 = new javax.swing.JCheckBox();
        goods_f45 = new javax.swing.JCheckBox();
        単位_print_f_ = new javax.swing.JCheckBox();
        jPanel2 = new javax.swing.JPanel();
        grant_year = new aks.swing.JTextFieldHankaku();
        grant_month = new aks.swing.JTextFieldHankaku();
        jLabel80 = new javax.swing.JLabel();
        jLabel81 = new javax.swing.JLabel();
        grant_day = new aks.swing.JTextFieldHankaku();
        jLabel82 = new javax.swing.JLabel();
        jLabel39 = new javax.swing.JLabel();
        manifesto_no1 = new javax.swing.JTextField();
        tantou_cmb = new aks.swing.JComboBox();
        交付年月日_print_f_ = new javax.swing.JCheckBox();
        交付担当者_print_f_ = new javax.swing.JCheckBox();
        jLabel40 = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        jLabel52 = new javax.swing.JLabel();
        last_cont_adress = new aks.swing.JTextField();
        jLabel99 = new javax.swing.JLabel();
        last_cont_tel = new aks.swing.JTextFieldHankaku();
        jLabel56 = new javax.swing.JLabel();
        last_cont_zip = new aks.swing.JTextFieldHankaku();
        last_cont_name = new aks.swing.JTextField();
        jLabel25 = new javax.swing.JLabel();
        jPanel33 = new javax.swing.JPanel();
        jlbl_final_name_ = new javax.swing.JLabel();
        jPanel35 = new javax.swing.JPanel();
        jLabel46 = new javax.swing.JLabel();
        check_f3 = new javax.swing.JCheckBox();
        check_f4 = new javax.swing.JCheckBox();
        jPanel41 = new javax.swing.JPanel();
        jPanel6 = new javax.swing.JPanel();
        jLabel64 = new javax.swing.JLabel();
        jLabel71 = new javax.swing.JLabel();
        jPanel10 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        cont_office_zip01 = new aks.swing.JTextFieldHankaku();
        cont_office_tel01 = new aks.swing.JTextFieldHankaku();
        cont_office_address01 = new aks.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        cont_office_name01 = new javax.swing.JComboBox();
        jLabel33 = new javax.swing.JLabel();
        jPanel11 = new javax.swing.JPanel();
        jLabel44 = new javax.swing.JLabel();
        jLabel93 = new javax.swing.JLabel();
        jLabel96 = new javax.swing.JLabel();
        jLabel97 = new javax.swing.JLabel();
        jLabel98 = new javax.swing.JLabel();
        jPanel12 = new javax.swing.JPanel();
        jLabel122 = new javax.swing.JLabel();
        jLabel123 = new javax.swing.JLabel();
        jPanel13 = new javax.swing.JPanel();
        jLabel91 = new javax.swing.JLabel();
        check_f2 = new javax.swing.JCheckBox();
        check_f1 = new javax.swing.JCheckBox();
        slash_f1 = new javax.swing.JCheckBox();
        jLabel92 = new javax.swing.JLabel();
        cont_name = new aks.swing.JTextField();
        jLabel104 = new javax.swing.JLabel();
        cont_no = new aks.swing.JTextFieldHankaku();
        jPanel32 = new javax.swing.JPanel();
        jPanel14 = new javax.swing.JPanel();
        trans1_date_year = new aks.swing.JTextFieldHankaku();
        jLabel75 = new javax.swing.JLabel();
        trans1_date_month = new aks.swing.JTextFieldHankaku();
        jLabel76 = new javax.swing.JLabel();
        trans1_date_day = new aks.swing.JTextFieldHankaku();
        jLabel77 = new javax.swing.JLabel();
        jLabel54 = new javax.swing.JLabel();
        trans1_weight = new aks.swing.JTextFieldHankaku();
        jLabel86 = new javax.swing.JLabel();
        jPanel16 = new javax.swing.JPanel();
        排出事業場_print_f = new javax.swing.JCheckBox();
        jLabel10 = new javax.swing.JLabel();
        office_id01 = new javax.swing.JComboBox();
        jLabel11 = new javax.swing.JLabel();
        office_zip01 = new aks.swing.JTextFieldHankaku();
        office_tel01 = new aks.swing.JTextFieldHankaku();
        office_address01 = new aks.swing.JTextField();
        jLabel22 = new javax.swing.JLabel();
        office_sub_name_ = new aks.swing.JTextField();
        jLabel94 = new javax.swing.JLabel();
        office_search_ = new aks.swing.JTextFieldHankaku();
        office_name_ = new aks.swing.JTextField();
        jPanel17 = new javax.swing.JPanel();
        jLabel35 = new javax.swing.JLabel();
        jPanel18 = new javax.swing.JPanel();
        jLabel36 = new javax.swing.JLabel();
        jLabel42 = new javax.swing.JLabel();
        jPanel19 = new javax.swing.JPanel();
        jLabel83 = new javax.swing.JLabel();
        jLabel109 = new javax.swing.JLabel();
        jPanel20 = new javax.swing.JPanel();
        jLabel125 = new javax.swing.JLabel();
        jPanel21 = new javax.swing.JPanel();
        trans1_sub_name = new aks.swing.JTextField();
        trans1_sub_name1 = new aks.swing.JTextField();
        運搬担当者_print_3 = new javax.swing.JCheckBox();
        運搬担当者_print_f4 = new javax.swing.JCheckBox();
        trans_tantou_search_1 = new aks.swing.JTextFieldHankaku();
        trans_tantou_id_cmb1 = new javax.swing.JComboBox();
        jPanel22 = new javax.swing.JPanel();
        処分受託者_print_f = new javax.swing.JCheckBox();
        jLabel106 = new javax.swing.JLabel();
        cont_cmb01 = new javax.swing.JComboBox();
        jLabel107 = new javax.swing.JLabel();
        cont_zip01 = new aks.swing.JTextFieldHankaku();
        cont_tel01 = new aks.swing.JTextFieldHankaku();
        cont_address01 = new aks.swing.JTextField();
        jLabel108 = new javax.swing.JLabel();
        jLabel127 = new javax.swing.JLabel();
        cont_search_ = new aks.swing.JTextFieldHankaku();
        jButton4 = new javax.swing.JButton();
        jLabel32 = new javax.swing.JLabel();
        jPanel23 = new javax.swing.JPanel();
        jLabel84 = new javax.swing.JLabel();
        jLabel85 = new javax.swing.JLabel();
        last_cont_cmb = new aks.swing.JComboBox();
        jPanel38 = new javax.swing.JPanel();
        画面初期化_btn_ = new javax.swing.JButton();
        jPanel40 = new javax.swing.JPanel();
        jPanel15 = new javax.swing.JPanel();
        jLabel47 = new javax.swing.JLabel();
        jLabel117 = new javax.swing.JLabel();
        slash_f2 = new javax.swing.JCheckBox();
        jPanel24 = new javax.swing.JPanel();
        jLabel59 = new javax.swing.JLabel();
        jPanel25 = new javax.swing.JPanel();
        積み替え_f = new javax.swing.JCheckBox();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        cont_office_zip2 = new aks.swing.JTextFieldHankaku();
        cont_office_tel2 = new aks.swing.JTextFieldHankaku();
        cont_office_address2 = new aks.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jLabel128 = new javax.swing.JLabel();
        cont_search_1 = new aks.swing.JTextFieldHankaku();
        jButton5 = new javax.swing.JButton();
        保管場所_cmb = new javax.swing.JComboBox();
        jPanel26 = new javax.swing.JPanel();
        運搬受託者_print_f = new javax.swing.JCheckBox();
        jLabel62 = new javax.swing.JLabel();
        trans_cmb01 = new javax.swing.JComboBox();
        jLabel63 = new javax.swing.JLabel();
        trans_zip01 = new aks.swing.JTextFieldHankaku();
        trans_tel01 = new aks.swing.JTextFieldHankaku();
        trans_address01 = new aks.swing.JTextField();
        jLabel72 = new javax.swing.JLabel();
        jLabel87 = new javax.swing.JLabel();
        trans_search_ = new aks.swing.JTextFieldHankaku();
        jButton2 = new javax.swing.JButton();
        jLabel27 = new javax.swing.JLabel();
        jPanel27 = new javax.swing.JPanel();
        cont_sub_name = new aks.swing.JTextField();
        cont_sub_name1 = new aks.swing.JTextField();
        処分担当者_print_f2 = new javax.swing.JCheckBox();
        処分担当者_print_f3 = new javax.swing.JCheckBox();
        trans1_sub_name2 = new aks.swing.JTextField();
        jPanel28 = new javax.swing.JPanel();
        jLabel57 = new javax.swing.JLabel();
        jPanel29 = new javax.swing.JPanel();
        print_btn = new javax.swing.JButton();
        quit_btn = new javax.swing.JButton();
        regist_btn = new javax.swing.JButton();
        jLabel20 = new javax.swing.JLabel();
        pring_num_ = new javax.swing.JSpinner();
        産廃パターン1 = new javax.swing.JButton();
        jPanel9 = new javax.swing.JPanel();
        jLabel26 = new javax.swing.JLabel();
        jLabel28 = new javax.swing.JLabel();
        jLabel29 = new javax.swing.JLabel();
        jLabel30 = new javax.swing.JLabel();
        jLabel31 = new javax.swing.JLabel();
        jLabel34 = new javax.swing.JLabel();
        jLabel37 = new javax.swing.JLabel();
        jLabel58 = new javax.swing.JLabel();
        jPanel8 = new javax.swing.JPanel();
        jLabel119 = new javax.swing.JLabel();
        jLabel121 = new javax.swing.JLabel();
        jLabel129 = new javax.swing.JLabel();
        jLabel130 = new javax.swing.JLabel();
        jLabel131 = new javax.swing.JLabel();
        jLabel132 = new javax.swing.JLabel();
        jPanel5 = new javax.swing.JPanel();
        jLabel7 = new javax.swing.JLabel();
        jPanel30 = new javax.swing.JPanel();
        jLabel13 = new javax.swing.JLabel();
        jPanel31 = new javax.swing.JPanel();
        jLabel12 = new javax.swing.JLabel();
        jPanel36 = new javax.swing.JPanel();
        排出事業者_print_f = new javax.swing.JCheckBox();
        jLabel14 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        ent_zip01 = new aks.swing.JTextFieldHankaku();
        ent_tel01 = new aks.swing.JTextFieldHankaku();
        ent_address01 = new aks.swing.JTextField();
        jLabel24 = new javax.swing.JLabel();
        jLabel65 = new javax.swing.JLabel();
        ent_search_ = new aks.swing.JTextFieldHankaku();
        jButton1 = new javax.swing.JButton();
        ent_name = new javax.swing.JTextField();

        menu1.setLabel("File");
        menuBar1.add(menu1);

        menu2.setLabel("Edit");
        menuBar1.add(menu2);

        menu3.setLabel("File");
        menuBar2.add(menu3);

        menu4.setLabel("Edit");
        menuBar2.add(menu4);

        label1.setText("label1");

        jCheckBox78.setFont(new java.awt.Font("MS UI Gothic", 1, 10)); // NOI18N
        jCheckBox78.setText("種類(特別管理産業廃棄物）");
        jCheckBox78.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

        setFont(new java.awt.Font("MS UI Gothic", 1, 12)); // NOI18N
        setMaximumSize(new java.awt.Dimension(1280, 950));
        setMinimumSize(new java.awt.Dimension(1280, 900));
        setPreferredSize(new java.awt.Dimension(1280, 900));
        setLayout(new java.awt.GridBagLayout());

        jScrollPane2.setMaximumSize(new java.awt.Dimension(1280, 950));
        jScrollPane2.setMinimumSize(new java.awt.Dimension(1280, 700));
        jScrollPane2.setPreferredSize(new java.awt.Dimension(1280, 900));

        jPanel34.setMaximumSize(new java.awt.Dimension(1220, 910));
        jPanel34.setMinimumSize(new java.awt.Dimension(1220, 900));
        jPanel34.setPreferredSize(new java.awt.Dimension(1220, 900));
        jPanel34.setLayout(new java.awt.GridBagLayout());

        jPanel1.setBorder(javax.swing.BorderFactory.createEtchedBorder(new java.awt.Color(0, 102, 153), null));
        jPanel1.setMinimumSize(new java.awt.Dimension(484, 35));
        jPanel1.setPreferredSize(new java.awt.Dimension(424, 35));
        jPanel1.setLayout(new java.awt.GridBagLayout());

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
        gridBagConstraints.gridx = 9;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 0, 0);
        jPanel1.add(cont_date2_year, gridBagConstraints);

        jLabel68.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        jLabel68.setText("年");
        jLabel68.setMinimumSize(new java.awt.Dimension(20, 20));
        jLabel68.setPreferredSize(new java.awt.Dimension(20, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 10;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        jPanel1.add(jLabel68, gridBagConstraints);

        cont_date2_month.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        cont_date2_month.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        cont_date2_month.setMaximumSize(new java.awt.Dimension(35, 25));
        cont_date2_month.setMinimumSize(new java.awt.Dimension(35, 25));
        cont_date2_month.setPreferredSize(new java.awt.Dimension(35, 25));
        cont_date2_month.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                cont_date2_monthKeyPressed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 11;
        gridBagConstraints.gridy = 0;
        jPanel1.add(cont_date2_month, gridBagConstraints);

        jLabel69.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        jLabel69.setText("月");
        jLabel69.setMinimumSize(new java.awt.Dimension(20, 20));
        jLabel69.setPreferredSize(new java.awt.Dimension(20, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 12;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        jPanel1.add(jLabel69, gridBagConstraints);

        cont_date2_day.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        cont_date2_day.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        cont_date2_day.setMaximumSize(new java.awt.Dimension(35, 25));
        cont_date2_day.setMinimumSize(new java.awt.Dimension(35, 25));
        cont_date2_day.setPreferredSize(new java.awt.Dimension(35, 25));
        cont_date2_day.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                cont_date2_dayKeyPressed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 13;
        gridBagConstraints.gridy = 0;
        jPanel1.add(cont_date2_day, gridBagConstraints);

        jLabel70.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        jLabel70.setText("日");
        jLabel70.setMaximumSize(new java.awt.Dimension(30, 20));
        jLabel70.setMinimumSize(new java.awt.Dimension(30, 20));
        jLabel70.setPreferredSize(new java.awt.Dimension(30, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 14;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        jPanel1.add(jLabel70, gridBagConstraints);

        jLabel78.setBackground(new java.awt.Color(0, 153, 204));
        jLabel78.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        jLabel78.setForeground(new java.awt.Color(255, 255, 255));
        jLabel78.setText(" 最終処分年月日 ");
        jLabel78.setBorder(javax.swing.BorderFactory.createEtchedBorder(new java.awt.Color(0, 102, 153), null));
        jLabel78.setOpaque(true);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 7;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weighty = 1.0;
        jPanel1.add(jLabel78, gridBagConstraints);

        cont_date1_year.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        cont_date1_year.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        cont_date1_year.setMinimumSize(new java.awt.Dimension(60, 25));
        cont_date1_year.setPreferredSize(new java.awt.Dimension(60, 25));
        cont_date1_year.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                cont_date1_yearKeyPressed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 0, 0);
        jPanel1.add(cont_date1_year, gridBagConstraints);

        jLabel89.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        jLabel89.setText("年");
        jLabel89.setMinimumSize(new java.awt.Dimension(20, 20));
        jLabel89.setPreferredSize(new java.awt.Dimension(20, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        jPanel1.add(jLabel89, gridBagConstraints);

        cont_date1_month.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        cont_date1_month.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        cont_date1_month.setMinimumSize(new java.awt.Dimension(35, 25));
        cont_date1_month.setPreferredSize(new java.awt.Dimension(35, 25));
        cont_date1_month.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                cont_date1_monthKeyPressed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        jPanel1.add(cont_date1_month, gridBagConstraints);

        jLabel90.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        jLabel90.setText("月");
        jLabel90.setMinimumSize(new java.awt.Dimension(20, 20));
        jLabel90.setPreferredSize(new java.awt.Dimension(20, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        jPanel1.add(jLabel90, gridBagConstraints);

        cont_date1_day.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        cont_date1_day.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        cont_date1_day.setMinimumSize(new java.awt.Dimension(35, 25));
        cont_date1_day.setPreferredSize(new java.awt.Dimension(35, 25));
        cont_date1_day.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                cont_date1_dayKeyPressed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 0;
        jPanel1.add(cont_date1_day, gridBagConstraints);

        jLabel95.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        jLabel95.setText("日");
        jLabel95.setMaximumSize(new java.awt.Dimension(30, 20));
        jLabel95.setMinimumSize(new java.awt.Dimension(30, 20));
        jLabel95.setPreferredSize(new java.awt.Dimension(30, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 6;
        gridBagConstraints.gridy = 0;
        jPanel1.add(jLabel95, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        jPanel34.add(jPanel1, gridBagConstraints);

        jPanel3.setBorder(javax.swing.BorderFactory.createEtchedBorder(new java.awt.Color(0, 102, 153), null));
        jPanel3.setMaximumSize(new java.awt.Dimension(1130, 255));
        jPanel3.setMinimumSize(new java.awt.Dimension(1130, 255));
        jPanel3.setPreferredSize(new java.awt.Dimension(1130, 255));
        jPanel3.setLayout(new java.awt.GridBagLayout());

        jCheckBox58.setEnabled(false);
        jCheckBox58.setFocusable(false);
        jCheckBox58.setMaximumSize(new java.awt.Dimension(180, 20));
        jCheckBox58.setMinimumSize(new java.awt.Dimension(180, 20));
        jCheckBox58.setPreferredSize(new java.awt.Dimension(180, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        jPanel3.add(jCheckBox58, gridBagConstraints);

        jCheckBox59.setEnabled(false);
        jCheckBox59.setFocusable(false);
        jCheckBox59.setMaximumSize(new java.awt.Dimension(180, 20));
        jCheckBox59.setMinimumSize(new java.awt.Dimension(180, 20));
        jCheckBox59.setPreferredSize(new java.awt.Dimension(180, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 11;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        jPanel3.add(jCheckBox59, gridBagConstraints);

        jCheckBox60.setEnabled(false);
        jCheckBox60.setFocusable(false);
        jCheckBox60.setMaximumSize(new java.awt.Dimension(180, 20));
        jCheckBox60.setMinimumSize(new java.awt.Dimension(180, 20));
        jCheckBox60.setPreferredSize(new java.awt.Dimension(180, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        jPanel3.add(jCheckBox60, gridBagConstraints);

        goods_f42.setFont(new java.awt.Font("メイリオ", 0, 12)); // NOI18N
        goods_f42.setText("7440　廃水銀等");
        goods_f42.setFocusable(false);
        goods_f42.setMaximumSize(new java.awt.Dimension(180, 20));
        goods_f42.setMinimumSize(new java.awt.Dimension(180, 20));
        goods_f42.setPreferredSize(new java.awt.Dimension(180, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        jPanel3.add(goods_f42, gridBagConstraints);

        jCheckBox62.setEnabled(false);
        jCheckBox62.setFocusable(false);
        jCheckBox62.setMaximumSize(new java.awt.Dimension(180, 20));
        jCheckBox62.setMinimumSize(new java.awt.Dimension(180, 20));
        jCheckBox62.setPreferredSize(new java.awt.Dimension(180, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 11;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        jPanel3.add(jCheckBox62, gridBagConstraints);

        jCheckBox63.setEnabled(false);
        jCheckBox63.setFocusable(false);
        jCheckBox63.setMaximumSize(new java.awt.Dimension(180, 20));
        jCheckBox63.setMinimumSize(new java.awt.Dimension(180, 20));
        jCheckBox63.setPreferredSize(new java.awt.Dimension(180, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        jPanel3.add(jCheckBox63, gridBagConstraints);

        goods_f22.setFont(new java.awt.Font("メイリオ", 0, 12)); // NOI18N
        goods_f22.setText("4000　動物系固形不要物");
        goods_f22.setMaximumSize(new java.awt.Dimension(180, 20));
        goods_f22.setMinimumSize(new java.awt.Dimension(180, 20));
        goods_f22.setPreferredSize(new java.awt.Dimension(180, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        jPanel3.add(goods_f22, gridBagConstraints);

        goods_f40.setFont(new java.awt.Font("メイリオ", 0, 12)); // NOI18N
        goods_f40.setText("7430　13号廃棄物(有害)");
        goods_f40.setMaximumSize(new java.awt.Dimension(180, 20));
        goods_f40.setMinimumSize(new java.awt.Dimension(180, 20));
        goods_f40.setPreferredSize(new java.awt.Dimension(180, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        jPanel3.add(goods_f40, gridBagConstraints);

        goods_f39.setFont(new java.awt.Font("メイリオ", 0, 12)); // NOI18N
        goods_f39.setText("7429　ばいじん(有害)");
        goods_f39.setMaximumSize(new java.awt.Dimension(180, 20));
        goods_f39.setMinimumSize(new java.awt.Dimension(180, 20));
        goods_f39.setPreferredSize(new java.awt.Dimension(180, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        jPanel3.add(goods_f39, gridBagConstraints);

        goods_f38.setFont(new java.awt.Font("メイリオ", 0, 12)); // NOI18N
        goods_f38.setText("7428　廃アルカリ(有害)");
        goods_f38.setMaximumSize(new java.awt.Dimension(180, 20));
        goods_f38.setMinimumSize(new java.awt.Dimension(180, 20));
        goods_f38.setPreferredSize(new java.awt.Dimension(180, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        jPanel3.add(goods_f38, gridBagConstraints);

        goods_f37.setFont(new java.awt.Font("メイリオ", 0, 12)); // NOI18N
        goods_f37.setText("7427　廃酸(有害)");
        goods_f37.setMaximumSize(new java.awt.Dimension(180, 20));
        goods_f37.setMinimumSize(new java.awt.Dimension(180, 20));
        goods_f37.setPreferredSize(new java.awt.Dimension(180, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        jPanel3.add(goods_f37, gridBagConstraints);

        goods_f35.setFont(new java.awt.Font("メイリオ", 0, 12)); // NOI18N
        goods_f35.setText("7425　廃油(有害)");
        goods_f35.setMaximumSize(new java.awt.Dimension(180, 20));
        goods_f35.setMinimumSize(new java.awt.Dimension(180, 20));
        goods_f35.setPreferredSize(new java.awt.Dimension(180, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        jPanel3.add(goods_f35, gridBagConstraints);

        goods_f36.setFont(new java.awt.Font("メイリオ", 0, 12)); // NOI18N
        goods_f36.setText("7426　汚泥(有害)");
        goods_f36.setMaximumSize(new java.awt.Dimension(180, 20));
        goods_f36.setMinimumSize(new java.awt.Dimension(180, 20));
        goods_f36.setPreferredSize(new java.awt.Dimension(180, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        jPanel3.add(goods_f36, gridBagConstraints);

        goods_f27.setFont(new java.awt.Font("メイリオ", 0, 12)); // NOI18N
        goods_f27.setText("7200　強アルカリ");
        goods_f27.setMaximumSize(new java.awt.Dimension(180, 20));
        goods_f27.setMinimumSize(new java.awt.Dimension(180, 20));
        goods_f27.setPreferredSize(new java.awt.Dimension(180, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        jPanel3.add(goods_f27, gridBagConstraints);

        goods_f26.setFont(new java.awt.Font("メイリオ", 0, 12)); // NOI18N
        goods_f26.setText("7110　強酸(有害)");
        goods_f26.setMaximumSize(new java.awt.Dimension(180, 20));
        goods_f26.setMinimumSize(new java.awt.Dimension(180, 20));
        goods_f26.setPreferredSize(new java.awt.Dimension(180, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        jPanel3.add(goods_f26, gridBagConstraints);

        goods_f25.setFont(new java.awt.Font("メイリオ", 0, 12)); // NOI18N
        goods_f25.setText("7100　強酸");
        goods_f25.setMaximumSize(new java.awt.Dimension(180, 20));
        goods_f25.setMinimumSize(new java.awt.Dimension(180, 20));
        goods_f25.setPreferredSize(new java.awt.Dimension(180, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        jPanel3.add(goods_f25, gridBagConstraints);

        goods_f30.setFont(new java.awt.Font("メイリオ", 0, 12)); // NOI18N
        goods_f30.setText("7410　PCB等");
        goods_f30.setMaximumSize(new java.awt.Dimension(180, 20));
        goods_f30.setMinimumSize(new java.awt.Dimension(180, 20));
        goods_f30.setPreferredSize(new java.awt.Dimension(180, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        jPanel3.add(goods_f30, gridBagConstraints);

        goods_f31.setFont(new java.awt.Font("メイリオ", 0, 12)); // NOI18N
        goods_f31.setText("7421　廃石綿等");
        goods_f31.setMaximumSize(new java.awt.Dimension(180, 20));
        goods_f31.setMinimumSize(new java.awt.Dimension(180, 20));
        goods_f31.setPreferredSize(new java.awt.Dimension(180, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        jPanel3.add(goods_f31, gridBagConstraints);

        goods_f28.setFont(new java.awt.Font("メイリオ", 0, 12)); // NOI18N
        goods_f28.setText("7210　強アルカリ(有害)");
        goods_f28.setMaximumSize(new java.awt.Dimension(180, 20));
        goods_f28.setMinimumSize(new java.awt.Dimension(180, 20));
        goods_f28.setPreferredSize(new java.awt.Dimension(180, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        jPanel3.add(goods_f28, gridBagConstraints);

        goods_f29.setFont(new java.awt.Font("メイリオ", 0, 12)); // NOI18N
        goods_f29.setText("7300　感染性廃棄物");
        goods_f29.setMaximumSize(new java.awt.Dimension(180, 20));
        goods_f29.setMinimumSize(new java.awt.Dimension(180, 20));
        goods_f29.setPreferredSize(new java.awt.Dimension(180, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        jPanel3.add(goods_f29, gridBagConstraints);

        goods_f34.setFont(new java.awt.Font("メイリオ", 0, 12)); // NOI18N
        goods_f34.setText("7424　燃えがら(有害)");
        goods_f34.setMaximumSize(new java.awt.Dimension(180, 20));
        goods_f34.setMinimumSize(new java.awt.Dimension(180, 20));
        goods_f34.setPreferredSize(new java.awt.Dimension(180, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        jPanel3.add(goods_f34, gridBagConstraints);

        goods_f32.setFont(new java.awt.Font("メイリオ", 0, 12)); // NOI18N
        goods_f32.setText("7422　指定下水汚泥");
        goods_f32.setMaximumSize(new java.awt.Dimension(180, 20));
        goods_f32.setMinimumSize(new java.awt.Dimension(180, 20));
        goods_f32.setPreferredSize(new java.awt.Dimension(180, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        jPanel3.add(goods_f32, gridBagConstraints);

        goods_f33.setFont(new java.awt.Font("メイリオ", 0, 12)); // NOI18N
        goods_f33.setText("7423　鉱さい(有害)　");
        goods_f33.setMaximumSize(new java.awt.Dimension(180, 20));
        goods_f33.setMinimumSize(new java.awt.Dimension(180, 20));
        goods_f33.setPreferredSize(new java.awt.Dimension(180, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 11;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        jPanel3.add(goods_f33, gridBagConstraints);

        goods_f10.setFont(new java.awt.Font("メイリオ", 0, 12)); // NOI18N
        goods_f10.setText("0800　木くず");
        goods_f10.setMaximumSize(new java.awt.Dimension(180, 20));
        goods_f10.setMinimumSize(new java.awt.Dimension(180, 20));
        goods_f10.setPreferredSize(new java.awt.Dimension(180, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        jPanel3.add(goods_f10, gridBagConstraints);

        goods_f09.setFont(new java.awt.Font("メイリオ", 0, 12)); // NOI18N
        goods_f09.setText("0700　紙くず");
        goods_f09.setMaximumSize(new java.awt.Dimension(180, 20));
        goods_f09.setMinimumSize(new java.awt.Dimension(180, 20));
        goods_f09.setPreferredSize(new java.awt.Dimension(180, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        jPanel3.add(goods_f09, gridBagConstraints);

        goods_f12.setFont(new java.awt.Font("メイリオ", 0, 12)); // NOI18N
        goods_f12.setText("1000　動植物性残さ");
        goods_f12.setMaximumSize(new java.awt.Dimension(180, 20));
        goods_f12.setMinimumSize(new java.awt.Dimension(180, 20));
        goods_f12.setPreferredSize(new java.awt.Dimension(180, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        jPanel3.add(goods_f12, gridBagConstraints);

        goods_f11.setFont(new java.awt.Font("メイリオ", 0, 12)); // NOI18N
        goods_f11.setText("0900　繊維くず");
        goods_f11.setMaximumSize(new java.awt.Dimension(180, 20));
        goods_f11.setMinimumSize(new java.awt.Dimension(180, 20));
        goods_f11.setPreferredSize(new java.awt.Dimension(180, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        jPanel3.add(goods_f11, gridBagConstraints);

        goods_f13.setFont(new java.awt.Font("メイリオ", 0, 12)); // NOI18N
        goods_f13.setText("1100　ゴムくず");
        goods_f13.setMaximumSize(new java.awt.Dimension(180, 20));
        goods_f13.setMinimumSize(new java.awt.Dimension(180, 20));
        goods_f13.setPreferredSize(new java.awt.Dimension(180, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 11;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        jPanel3.add(goods_f13, gridBagConstraints);

        goods_f19.setFont(new java.awt.Font("メイリオ", 0, 12)); // NOI18N
        goods_f19.setText("1700　家畜の死体");
        goods_f19.setMaximumSize(new java.awt.Dimension(180, 20));
        goods_f19.setMinimumSize(new java.awt.Dimension(180, 20));
        goods_f19.setPreferredSize(new java.awt.Dimension(180, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        jPanel3.add(goods_f19, gridBagConstraints);

        goods_f18.setFont(new java.awt.Font("メイリオ", 0, 12)); // NOI18N
        goods_f18.setText("1600　家畜のふん尿");
        goods_f18.setMaximumSize(new java.awt.Dimension(180, 20));
        goods_f18.setMinimumSize(new java.awt.Dimension(180, 20));
        goods_f18.setPreferredSize(new java.awt.Dimension(180, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        jPanel3.add(goods_f18, gridBagConstraints);

        goods_f20.setFont(new java.awt.Font("メイリオ", 0, 12)); // NOI18N
        goods_f20.setText("1800　ばいじん");
        goods_f20.setMaximumSize(new java.awt.Dimension(180, 20));
        goods_f20.setMinimumSize(new java.awt.Dimension(180, 20));
        goods_f20.setPreferredSize(new java.awt.Dimension(180, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        jPanel3.add(goods_f20, gridBagConstraints);

        goods_f21.setFont(new java.awt.Font("メイリオ", 0, 12)); // NOI18N
        goods_f21.setText("1900　13号廃棄物");
        goods_f21.setMaximumSize(new java.awt.Dimension(180, 20));
        goods_f21.setMinimumSize(new java.awt.Dimension(180, 20));
        goods_f21.setPreferredSize(new java.awt.Dimension(180, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        jPanel3.add(goods_f21, gridBagConstraints);

        goods_f23.setFont(new java.awt.Font("メイリオ", 0, 12)); // NOI18N
        goods_f23.setText("7000　引火性廃油");
        goods_f23.setMaximumSize(new java.awt.Dimension(180, 20));
        goods_f23.setMinimumSize(new java.awt.Dimension(180, 20));
        goods_f23.setPreferredSize(new java.awt.Dimension(180, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        jPanel3.add(goods_f23, gridBagConstraints);

        goods_f24.setFont(new java.awt.Font("メイリオ", 0, 12)); // NOI18N
        goods_f24.setText("7010　引火性廃油(有害)");
        goods_f24.setMaximumSize(new java.awt.Dimension(180, 20));
        goods_f24.setMinimumSize(new java.awt.Dimension(180, 20));
        goods_f24.setPreferredSize(new java.awt.Dimension(180, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        jPanel3.add(goods_f24, gridBagConstraints);

        goods_f14.setFont(new java.awt.Font("メイリオ", 0, 12)); // NOI18N
        goods_f14.setText("1200　金属くず");
        goods_f14.setMaximumSize(new java.awt.Dimension(180, 20));
        goods_f14.setMinimumSize(new java.awt.Dimension(180, 20));
        goods_f14.setPreferredSize(new java.awt.Dimension(180, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        jPanel3.add(goods_f14, gridBagConstraints);

        goods_f15.setFont(new java.awt.Font("メイリオ", 0, 12)); // NOI18N
        goods_f15.setText("1300　ガラス・陶磁器くず");
        goods_f15.setMaximumSize(new java.awt.Dimension(180, 20));
        goods_f15.setMinimumSize(new java.awt.Dimension(180, 20));
        goods_f15.setPreferredSize(new java.awt.Dimension(180, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        jPanel3.add(goods_f15, gridBagConstraints);

        goods_f16.setFont(new java.awt.Font("メイリオ", 0, 12)); // NOI18N
        goods_f16.setText("1400　鉱さい");
        goods_f16.setMaximumSize(new java.awt.Dimension(180, 20));
        goods_f16.setMinimumSize(new java.awt.Dimension(180, 20));
        goods_f16.setPreferredSize(new java.awt.Dimension(180, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        jPanel3.add(goods_f16, gridBagConstraints);

        goods_f07.setFont(new java.awt.Font("メイリオ", 0, 12)); // NOI18N
        goods_f07.setText("0500　廃アルカリ");
        goods_f07.setMaximumSize(new java.awt.Dimension(180, 20));
        goods_f07.setMinimumSize(new java.awt.Dimension(180, 20));
        goods_f07.setPreferredSize(new java.awt.Dimension(180, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        jPanel3.add(goods_f07, gridBagConstraints);

        goods_f08.setFont(new java.awt.Font("メイリオ", 0, 12)); // NOI18N
        goods_f08.setText("0600　廃プラスチック類");
        goods_f08.setMaximumSize(new java.awt.Dimension(180, 20));
        goods_f08.setMinimumSize(new java.awt.Dimension(180, 20));
        goods_f08.setPreferredSize(new java.awt.Dimension(180, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        jPanel3.add(goods_f08, gridBagConstraints);

        goods_f05.setFont(new java.awt.Font("メイリオ", 0, 12)); // NOI18N
        goods_f05.setText("0300　廃油");
        goods_f05.setMaximumSize(new java.awt.Dimension(180, 20));
        goods_f05.setMinimumSize(new java.awt.Dimension(180, 20));
        goods_f05.setPreferredSize(new java.awt.Dimension(180, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        jPanel3.add(goods_f05, gridBagConstraints);

        goods_f06.setFont(new java.awt.Font("メイリオ", 0, 12)); // NOI18N
        goods_f06.setText("0400　廃酸");
        goods_f06.setMaximumSize(new java.awt.Dimension(180, 20));
        goods_f06.setMinimumSize(new java.awt.Dimension(180, 20));
        goods_f06.setPreferredSize(new java.awt.Dimension(180, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        jPanel3.add(goods_f06, gridBagConstraints);

        goods_f01.setFont(new java.awt.Font("メイリオ", 1, 14)); // NOI18N
        goods_f01.setText("種類(普通の産業廃棄物)");
        goods_f01.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        goods_f01.setMaximumSize(new java.awt.Dimension(180, 25));
        goods_f01.setMinimumSize(new java.awt.Dimension(180, 25));
        goods_f01.setPreferredSize(new java.awt.Dimension(180, 25));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 2;
        jPanel3.add(goods_f01, gridBagConstraints);

        goods_f04.setFont(new java.awt.Font("メイリオ", 0, 12)); // NOI18N
        goods_f04.setText("0200　汚泥");
        goods_f04.setMaximumSize(new java.awt.Dimension(180, 20));
        goods_f04.setMinimumSize(new java.awt.Dimension(180, 20));
        goods_f04.setPreferredSize(new java.awt.Dimension(180, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        jPanel3.add(goods_f04, gridBagConstraints);

        goods_f17.setFont(new java.awt.Font("メイリオ", 0, 12)); // NOI18N
        goods_f17.setText("1500　がれき類");
        goods_f17.setMaximumSize(new java.awt.Dimension(180, 20));
        goods_f17.setMinimumSize(new java.awt.Dimension(180, 20));
        goods_f17.setPreferredSize(new java.awt.Dimension(180, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        jPanel3.add(goods_f17, gridBagConstraints);

        goods_f02.setFont(new java.awt.Font("メイリオ", 1, 14)); // NOI18N
        goods_f02.setText("種類(特別管理産業廃棄物）");
        goods_f02.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        goods_f02.setMaximumSize(new java.awt.Dimension(330, 25));
        goods_f02.setMinimumSize(new java.awt.Dimension(330, 25));
        goods_f02.setPreferredSize(new java.awt.Dimension(330, 25));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        jPanel3.add(goods_f02, gridBagConstraints);

        goods_f03.setFont(new java.awt.Font("メイリオ", 0, 12)); // NOI18N
        goods_f03.setText("0100  燃えがら");
        goods_f03.setMaximumSize(new java.awt.Dimension(180, 20));
        goods_f03.setMinimumSize(new java.awt.Dimension(180, 20));
        goods_f03.setPreferredSize(new java.awt.Dimension(180, 20));
        goods_f03.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                goods_f03ActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        jPanel3.add(goods_f03, gridBagConstraints);

        jPanel37.setMaximumSize(new java.awt.Dimension(440, 250));
        jPanel37.setMinimumSize(new java.awt.Dimension(440, 250));
        jPanel37.setPreferredSize(new java.awt.Dimension(440, 250));
        jPanel37.setLayout(new java.awt.GridBagLayout());

        形状_print_f_.setFont(new java.awt.Font("メイリオ", 0, 12)); // NOI18N
        形状_print_f_.setText("<html>印字なし</html>");
        形状_print_f_.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        形状_print_f_.setMaximumSize(new java.awt.Dimension(100, 17));
        形状_print_f_.setMinimumSize(new java.awt.Dimension(100, 17));
        形状_print_f_.setPreferredSize(new java.awt.Dimension(100, 17));
        形状_print_f_.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                形状_print_f_ItemStateChanged(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 20);
        jPanel37.add(形状_print_f_, gridBagConstraints);

        jLabel4.setFont(new java.awt.Font("メイリオ", 0, 12)); // NOI18N
        jLabel4.setText("数量(及び単位)");
        jLabel4.setMaximumSize(new java.awt.Dimension(110, 22));
        jLabel4.setMinimumSize(new java.awt.Dimension(110, 22));
        jLabel4.setPreferredSize(new java.awt.Dimension(110, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        jPanel37.add(jLabel4, gridBagConstraints);

        net_weight_field.setFont(new java.awt.Font("メイリオ", 0, 12)); // NOI18N
        net_weight_field.setMaximumSize(new java.awt.Dimension(150, 25));
        net_weight_field.setMinimumSize(new java.awt.Dimension(150, 25));
        net_weight_field.setPreferredSize(new java.awt.Dimension(150, 25));
        net_weight_field.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                net_weight_fieldFocusGained(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.insets = new java.awt.Insets(2, 0, 2, 2);
        jPanel37.add(net_weight_field, gridBagConstraints);

        jLabel16.setFont(new java.awt.Font("メイリオ", 0, 12)); // NOI18N
        jLabel16.setText("荷姿");
        jLabel16.setMaximumSize(new java.awt.Dimension(30, 22));
        jLabel16.setMinimumSize(new java.awt.Dimension(30, 22));
        jLabel16.setPreferredSize(new java.awt.Dimension(30, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        jPanel37.add(jLabel16, gridBagConstraints);

        荷姿_print_f_.setFont(new java.awt.Font("メイリオ", 0, 12)); // NOI18N
        荷姿_print_f_.setText("<html>印字なし</html>");
        荷姿_print_f_.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        荷姿_print_f_.setMaximumSize(new java.awt.Dimension(100, 20));
        荷姿_print_f_.setMinimumSize(new java.awt.Dimension(100, 20));
        荷姿_print_f_.setPreferredSize(new java.awt.Dimension(100, 20));
        荷姿_print_f_.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                荷姿_print_f_ItemStateChanged(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 20);
        jPanel37.add(荷姿_print_f_, gridBagConstraints);

        単位_cmb.setFont(new java.awt.Font("メイリオ", 0, 12)); // NOI18N
        単位_cmb.setMaximumSize(new java.awt.Dimension(60, 27));
        単位_cmb.setMinimumSize(new java.awt.Dimension(60, 27));
        単位_cmb.setPreferredSize(new java.awt.Dimension(60, 27));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 20);
        jPanel37.add(単位_cmb, gridBagConstraints);

        jLabel21.setFont(new java.awt.Font("メイリオ", 0, 12)); // NOI18N
        jLabel21.setText("有害物質等");
        jLabel21.setMaximumSize(new java.awt.Dimension(80, 17));
        jLabel21.setMinimumSize(new java.awt.Dimension(80, 17));
        jLabel21.setPreferredSize(new java.awt.Dimension(80, 17));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        jPanel37.add(jLabel21, gridBagConstraints);

        method_cmb.setFont(new java.awt.Font("メイリオ", 0, 12)); // NOI18N
        method_cmb.setMinimumSize(new java.awt.Dimension(140, 27));
        method_cmb.setPreferredSize(new java.awt.Dimension(140, 27));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 20);
        jPanel37.add(method_cmb, gridBagConstraints);

        jLabel19.setFont(new java.awt.Font("メイリオ", 0, 12)); // NOI18N
        jLabel19.setText("処分方法");
        jLabel19.setMaximumSize(new java.awt.Dimension(60, 17));
        jLabel19.setMinimumSize(new java.awt.Dimension(60, 17));
        jLabel19.setPreferredSize(new java.awt.Dimension(60, 17));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        jPanel37.add(jLabel19, gridBagConstraints);

        slash_f3.setFont(new java.awt.Font("メイリオ", 0, 12)); // NOI18N
        slash_f3.setText("斜線有り");
        slash_f3.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        slash_f3.setMaximumSize(new java.awt.Dimension(80, 17));
        slash_f3.setMinimumSize(new java.awt.Dimension(80, 17));
        slash_f3.setPreferredSize(new java.awt.Dimension(80, 17));
        slash_f3.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                slash_f3ItemStateChanged(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        jPanel37.add(slash_f3, gridBagConstraints);

        jLabel18.setFont(new java.awt.Font("メイリオ", 0, 12)); // NOI18N
        jLabel18.setText("産業廃棄物の名称");
        jLabel18.setMaximumSize(new java.awt.Dimension(110, 17));
        jLabel18.setMinimumSize(new java.awt.Dimension(110, 17));
        jLabel18.setPreferredSize(new java.awt.Dimension(110, 17));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        jPanel37.add(jLabel18, gridBagConstraints);

        goods_name_field.setFont(new java.awt.Font("メイリオ", 0, 12)); // NOI18N
        goods_name_field.setMaximumSize(new java.awt.Dimension(400, 27));
        goods_name_field.setMinimumSize(new java.awt.Dimension(400, 27));
        goods_name_field.setPreferredSize(new java.awt.Dimension(400, 27));
        goods_name_field.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                goods_name_fieldFocusGained(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(2, 0, 2, 20);
        jPanel37.add(goods_name_field, gridBagConstraints);

        nisugata_cmb.setFont(new java.awt.Font("メイリオ", 0, 12)); // NOI18N
        nisugata_cmb.setMinimumSize(new java.awt.Dimension(140, 27));
        nisugata_cmb.setPreferredSize(new java.awt.Dimension(140, 27));
        nisugata_cmb.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                nisugata_cmbItemStateChanged(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 20);
        jPanel37.add(nisugata_cmb, gridBagConstraints);

        jPanel7.setLayout(new javax.swing.BoxLayout(jPanel7, javax.swing.BoxLayout.LINE_AXIS));

        yugai_field.setFont(new java.awt.Font("メイリオ", 0, 12)); // NOI18N
        yugai_field.setMaximumSize(new java.awt.Dimension(230, 25));
        yugai_field.setMinimumSize(new java.awt.Dimension(230, 25));
        yugai_field.setPreferredSize(new java.awt.Dimension(230, 25));
        yugai_field.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                yugai_fieldFocusGained(evt);
            }
        });
        jPanel7.add(yugai_field);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        jPanel37.add(jPanel7, gridBagConstraints);

        jPanel42.setMaximumSize(new java.awt.Dimension(400, 105));
        jPanel42.setMinimumSize(new java.awt.Dimension(400, 105));
        jPanel42.setPreferredSize(new java.awt.Dimension(400, 105));
        jPanel42.setLayout(new java.awt.GridBagLayout());

        jScrollPane1.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        jScrollPane1.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
        jScrollPane1.setMaximumSize(new java.awt.Dimension(200, 80));
        jScrollPane1.setMinimumSize(new java.awt.Dimension(200, 80));
        jScrollPane1.setPreferredSize(new java.awt.Dimension(200, 80));

        txt備考.setColumns(20);
        txt備考.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        txt備考.setRows(5);
        txt備考.setText("あああああああ\nいいいいいいい\nううううううう");
        txt備考.setMaximumSize(new java.awt.Dimension(100, 80));
        txt備考.setMinimumSize(new java.awt.Dimension(100, 80));
        txt備考.setPreferredSize(new java.awt.Dimension(100, 80));
        jScrollPane1.setViewportView(txt備考);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.gridheight = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        jPanel42.add(jScrollPane1, gridBagConstraints);

        jLabel17.setFont(new java.awt.Font("メイリオ", 0, 12)); // NOI18N
        jLabel17.setText("備考・通信欄");
        jLabel17.setMaximumSize(new java.awt.Dimension(80, 22));
        jLabel17.setMinimumSize(new java.awt.Dimension(154, 17));
        jLabel17.setPreferredSize(new java.awt.Dimension(154, 17));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        jPanel42.add(jLabel17, gridBagConstraints);

        slash_f4.setFont(new java.awt.Font("メイリオ", 0, 12)); // NOI18N
        slash_f4.setText("斜線有り");
        slash_f4.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        slash_f4.setMaximumSize(new java.awt.Dimension(80, 17));
        slash_f4.setMinimumSize(new java.awt.Dimension(80, 17));
        slash_f4.setPreferredSize(new java.awt.Dimension(80, 17));
        slash_f4.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                slash_f4ItemStateChanged(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        jPanel42.add(slash_f4, gridBagConstraints);

        goods_f43.setFont(new java.awt.Font("メイリオ", 0, 12)); // NOI18N
        goods_f43.setText("水銀使用製品産業廃棄物");
        goods_f43.setFocusable(false);
        goods_f43.setMaximumSize(new java.awt.Dimension(180, 20));
        goods_f43.setMinimumSize(new java.awt.Dimension(180, 20));
        goods_f43.setPreferredSize(new java.awt.Dimension(180, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        jPanel42.add(goods_f43, gridBagConstraints);

        goods_f44.setFont(new java.awt.Font("メイリオ", 0, 12)); // NOI18N
        goods_f44.setText("水銀含有ばいじん等");
        goods_f44.setFocusable(false);
        goods_f44.setMaximumSize(new java.awt.Dimension(180, 20));
        goods_f44.setMinimumSize(new java.awt.Dimension(180, 20));
        goods_f44.setPreferredSize(new java.awt.Dimension(180, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        jPanel42.add(goods_f44, gridBagConstraints);

        goods_f46.setFont(new java.awt.Font("メイリオ", 0, 12)); // NOI18N
        goods_f46.setText("特定産業廃棄物");
        goods_f46.setFocusable(false);
        goods_f46.setMaximumSize(new java.awt.Dimension(180, 20));
        goods_f46.setMinimumSize(new java.awt.Dimension(180, 20));
        goods_f46.setPreferredSize(new java.awt.Dimension(180, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        jPanel42.add(goods_f46, gridBagConstraints);

        goods_f45.setFont(new java.awt.Font("メイリオ", 0, 12)); // NOI18N
        goods_f45.setText("石綿含有産業廃棄物");
        goods_f45.setFocusable(false);
        goods_f45.setMaximumSize(new java.awt.Dimension(180, 20));
        goods_f45.setMinimumSize(new java.awt.Dimension(180, 20));
        goods_f45.setPreferredSize(new java.awt.Dimension(180, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        jPanel42.add(goods_f45, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        jPanel37.add(jPanel42, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridheight = 12;
        gridBagConstraints.weightx = 1.0;
        jPanel3.add(jPanel37, gridBagConstraints);
        jPanel37.getAccessibleContext().setAccessibleDescription("");

        単位_print_f_.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        単位_print_f_.setText("印字なし");
        単位_print_f_.setMaximumSize(new java.awt.Dimension(90, 25));
        単位_print_f_.setMinimumSize(new java.awt.Dimension(90, 25));
        単位_print_f_.setPreferredSize(new java.awt.Dimension(90, 25));
        単位_print_f_.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                単位_print_f_ItemStateChanged(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        jPanel3.add(単位_print_f_, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        jPanel34.add(jPanel3, gridBagConstraints);

        jPanel2.setBorder(javax.swing.BorderFactory.createEtchedBorder(new java.awt.Color(0, 102, 153), null));
        jPanel2.setMinimumSize(new java.awt.Dimension(1130, 35));
        jPanel2.setPreferredSize(new java.awt.Dimension(1130, 35));
        jPanel2.setLayout(new java.awt.GridBagLayout());

        grant_year.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        grant_year.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        grant_year.setMaximumSize(new java.awt.Dimension(60, 25));
        grant_year.setMinimumSize(new java.awt.Dimension(60, 25));
        grant_year.setPreferredSize(new java.awt.Dimension(60, 25));
        grant_year.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                grant_yearKeyPressed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        jPanel2.add(grant_year, gridBagConstraints);

        grant_month.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        grant_month.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        grant_month.setMaximumSize(new java.awt.Dimension(40, 25));
        grant_month.setMinimumSize(new java.awt.Dimension(40, 25));
        grant_month.setPreferredSize(new java.awt.Dimension(40, 25));
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
        jPanel2.add(grant_month, gridBagConstraints);

        jLabel80.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        jLabel80.setText("年");
        jLabel80.setMinimumSize(new java.awt.Dimension(20, 30));
        jLabel80.setPreferredSize(new java.awt.Dimension(20, 30));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        jPanel2.add(jLabel80, gridBagConstraints);

        jLabel81.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        jLabel81.setText("月");
        jLabel81.setMinimumSize(new java.awt.Dimension(20, 30));
        jLabel81.setPreferredSize(new java.awt.Dimension(20, 30));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 0;
        jPanel2.add(jLabel81, gridBagConstraints);

        grant_day.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        grant_day.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        grant_day.setMaximumSize(new java.awt.Dimension(40, 25));
        grant_day.setMinimumSize(new java.awt.Dimension(40, 25));
        grant_day.setPreferredSize(new java.awt.Dimension(40, 25));
        grant_day.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                grant_dayActionPerformed(evt);
            }
        });
        grant_day.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                grant_dayKeyPressed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 6;
        gridBagConstraints.gridy = 0;
        jPanel2.add(grant_day, gridBagConstraints);

        jLabel82.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        jLabel82.setText("日");
        jLabel82.setMinimumSize(new java.awt.Dimension(25, 30));
        jLabel82.setPreferredSize(new java.awt.Dimension(25, 30));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 7;
        gridBagConstraints.gridy = 0;
        jPanel2.add(jLabel82, gridBagConstraints);

        jLabel39.setBackground(new java.awt.Color(0, 102, 153));
        jLabel39.setFont(new java.awt.Font("メイリオ", 1, 14)); // NOI18N
        jLabel39.setForeground(new java.awt.Color(255, 255, 255));
        jLabel39.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel39.setText("交付担当者");
        jLabel39.setBorder(javax.swing.BorderFactory.createEtchedBorder(new java.awt.Color(0, 102, 153), null));
        jLabel39.setMaximumSize(new java.awt.Dimension(80, 30));
        jLabel39.setMinimumSize(new java.awt.Dimension(80, 30));
        jLabel39.setOpaque(true);
        jLabel39.setPreferredSize(new java.awt.Dimension(80, 30));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 12;
        gridBagConstraints.gridy = 0;
        jPanel2.add(jLabel39, gridBagConstraints);

        manifesto_no1.setEditable(false);
        manifesto_no1.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        manifesto_no1.setEnabled(false);
        manifesto_no1.setFocusable(false);
        manifesto_no1.setMinimumSize(new java.awt.Dimension(150, 27));
        manifesto_no1.setPreferredSize(new java.awt.Dimension(150, 25));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 9;
        gridBagConstraints.gridy = 0;
        jPanel2.add(manifesto_no1, gridBagConstraints);

        tantou_cmb.setEditable(true);
        tantou_cmb.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        tantou_cmb.setMaximumSize(new java.awt.Dimension(200, 27));
        tantou_cmb.setMinimumSize(new java.awt.Dimension(200, 27));
        tantou_cmb.setPreferredSize(new java.awt.Dimension(200, 27));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 13;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(2, 0, 2, 20);
        jPanel2.add(tantou_cmb, gridBagConstraints);

        交付年月日_print_f_.setFont(new java.awt.Font("メイリオ", 0, 12)); // NOI18N
        交付年月日_print_f_.setText("印字なし");
        交付年月日_print_f_.setMaximumSize(new java.awt.Dimension(90, 22));
        交付年月日_print_f_.setMinimumSize(new java.awt.Dimension(90, 22));
        交付年月日_print_f_.setPreferredSize(new java.awt.Dimension(90, 22));
        交付年月日_print_f_.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                交付年月日_print_f_ItemStateChanged(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        jPanel2.add(交付年月日_print_f_, gridBagConstraints);

        交付担当者_print_f_.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        交付担当者_print_f_.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        交付担当者_print_f_.setLabel("印字なし");
        交付担当者_print_f_.setMaximumSize(new java.awt.Dimension(85, 25));
        交付担当者_print_f_.setMinimumSize(new java.awt.Dimension(85, 25));
        交付担当者_print_f_.setPreferredSize(new java.awt.Dimension(85, 25));
        交付担当者_print_f_.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                交付担当者_print_f_ItemStateChanged(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 11;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        jPanel2.add(交付担当者_print_f_, gridBagConstraints);

        jLabel40.setBackground(new java.awt.Color(0, 102, 153));
        jLabel40.setFont(new java.awt.Font("メイリオ", 1, 14)); // NOI18N
        jLabel40.setForeground(new java.awt.Color(255, 255, 255));
        jLabel40.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel40.setText("交付番号");
        jLabel40.setMinimumSize(new java.awt.Dimension(70, 30));
        jLabel40.setOpaque(true);
        jLabel40.setPreferredSize(new java.awt.Dimension(70, 30));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 8;
        gridBagConstraints.gridy = 0;
        jPanel2.add(jLabel40, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        jPanel34.add(jPanel2, gridBagConstraints);

        jPanel4.setBorder(javax.swing.BorderFactory.createEtchedBorder(new java.awt.Color(0, 102, 153), null));
        jPanel4.setMaximumSize(new java.awt.Dimension(1130, 80));
        jPanel4.setMinimumSize(new java.awt.Dimension(1130, 80));
        jPanel4.setPreferredSize(new java.awt.Dimension(1130, 80));
        jPanel4.setLayout(new java.awt.GridBagLayout());

        jLabel52.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        jLabel52.setText("　名称");
        jLabel52.setMaximumSize(new java.awt.Dimension(50, 25));
        jLabel52.setMinimumSize(new java.awt.Dimension(50, 25));
        jLabel52.setPreferredSize(new java.awt.Dimension(50, 25));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        jPanel4.add(jLabel52, gridBagConstraints);

        last_cont_adress.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        last_cont_adress.setMaximumSize(new java.awt.Dimension(430, 25));
        last_cont_adress.setMinimumSize(new java.awt.Dimension(430, 25));
        last_cont_adress.setPreferredSize(new java.awt.Dimension(430, 25));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 0, 2, 0);
        jPanel4.add(last_cont_adress, gridBagConstraints);

        jLabel99.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        jLabel99.setText("　電話番号");
        jLabel99.setMaximumSize(new java.awt.Dimension(80, 25));
        jLabel99.setMinimumSize(new java.awt.Dimension(80, 25));
        jLabel99.setPreferredSize(new java.awt.Dimension(80, 25));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 1;
        jPanel4.add(jLabel99, gridBagConstraints);

        last_cont_tel.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        last_cont_tel.setMaximumSize(new java.awt.Dimension(200, 25));
        last_cont_tel.setMinimumSize(new java.awt.Dimension(200, 25));
        last_cont_tel.setPreferredSize(new java.awt.Dimension(200, 25));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        jPanel4.add(last_cont_tel, gridBagConstraints);

        jLabel56.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        jLabel56.setText("  住所");
        jLabel56.setMaximumSize(new java.awt.Dimension(50, 25));
        jLabel56.setMinimumSize(new java.awt.Dimension(50, 25));
        jLabel56.setPreferredSize(new java.awt.Dimension(50, 25));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 0, 2, 0);
        jPanel4.add(jLabel56, gridBagConstraints);

        last_cont_zip.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        last_cont_zip.setMaximumSize(new java.awt.Dimension(100, 25));
        last_cont_zip.setMinimumSize(new java.awt.Dimension(100, 25));
        last_cont_zip.setPreferredSize(new java.awt.Dimension(100, 25));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 0, 2, 0);
        jPanel4.add(last_cont_zip, gridBagConstraints);

        last_cont_name.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        last_cont_name.setMaximumSize(new java.awt.Dimension(250, 25));
        last_cont_name.setMinimumSize(new java.awt.Dimension(250, 25));
        last_cont_name.setPreferredSize(new java.awt.Dimension(250, 25));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        jPanel4.add(last_cont_name, gridBagConstraints);

        jLabel25.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        jLabel25.setText("〒");
        jLabel25.setMaximumSize(new java.awt.Dimension(14, 25));
        jLabel25.setMinimumSize(new java.awt.Dimension(14, 25));
        jLabel25.setPreferredSize(new java.awt.Dimension(14, 25));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.insets = new java.awt.Insets(2, 0, 2, 0);
        jPanel4.add(jLabel25, gridBagConstraints);

        jPanel33.setMaximumSize(new java.awt.Dimension(260, 75));
        jPanel33.setMinimumSize(new java.awt.Dimension(260, 75));
        jPanel33.setPreferredSize(new java.awt.Dimension(260, 75));
        jPanel33.setLayout(new java.awt.GridBagLayout());

        jlbl_final_name_.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        jlbl_final_name_.setText("<html>株式会社スパット北上<br> ");
        jlbl_final_name_.setMaximumSize(new java.awt.Dimension(260, 50));
        jlbl_final_name_.setMinimumSize(new java.awt.Dimension(260, 50));
        jlbl_final_name_.setPreferredSize(new java.awt.Dimension(260, 50));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        jPanel33.add(jlbl_final_name_, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 7;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridheight = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 15, 0, 0);
        jPanel4.add(jPanel33, gridBagConstraints);

        jPanel35.setMaximumSize(new java.awt.Dimension(180, 75));
        jPanel35.setMinimumSize(new java.awt.Dimension(180, 75));
        jPanel35.setPreferredSize(new java.awt.Dimension(180, 75));
        jPanel35.setLayout(new java.awt.GridBagLayout());

        jLabel46.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        jLabel46.setText("  名称/所在地/電話番号");
        jLabel46.setMaximumSize(new java.awt.Dimension(180, 22));
        jLabel46.setMinimumSize(new java.awt.Dimension(180, 22));
        jLabel46.setPreferredSize(new java.awt.Dimension(180, 22));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        jPanel35.add(jLabel46, gridBagConstraints);

        check_f3.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        check_f3.setText("委託契約書記載のとおり");
        check_f3.setMaximumSize(new java.awt.Dimension(180, 22));
        check_f3.setMinimumSize(new java.awt.Dimension(180, 22));
        check_f3.setPreferredSize(new java.awt.Dimension(180, 22));
        check_f3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                check_f3ActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        jPanel35.add(check_f3, gridBagConstraints);

        check_f4.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        check_f4.setText("当欄記載のとおり");
        check_f4.setMaximumSize(new java.awt.Dimension(180, 22));
        check_f4.setMinimumSize(new java.awt.Dimension(180, 22));
        check_f4.setPreferredSize(new java.awt.Dimension(180, 22));
        check_f4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                check_f4ActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        jPanel35.add(check_f4, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridheight = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        jPanel4.add(jPanel35, gridBagConstraints);

        jPanel41.setMinimumSize(new java.awt.Dimension(700, 20));
        jPanel41.setPreferredSize(new java.awt.Dimension(700, 20));

        javax.swing.GroupLayout jPanel41Layout = new javax.swing.GroupLayout(jPanel41);
        jPanel41.setLayout(jPanel41Layout);
        jPanel41Layout.setHorizontalGroup(
            jPanel41Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 700, Short.MAX_VALUE)
        );
        jPanel41Layout.setVerticalGroup(
            jPanel41Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 20, Short.MAX_VALUE)
        );

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 5;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 2, 0);
        jPanel4.add(jPanel41, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        jPanel34.add(jPanel4, gridBagConstraints);

        jPanel6.setBackground(new java.awt.Color(0, 153, 204));
        jPanel6.setBorder(javax.swing.BorderFactory.createEtchedBorder(new java.awt.Color(0, 102, 153), null));
        jPanel6.setMaximumSize(new java.awt.Dimension(100, 95));
        jPanel6.setMinimumSize(new java.awt.Dimension(100, 95));
        jPanel6.setPreferredSize(new java.awt.Dimension(100, 95));
        jPanel6.setLayout(new java.awt.GridBagLayout());

        jLabel64.setFont(new java.awt.Font("メイリオ", 0, 12)); // NOI18N
        jLabel64.setForeground(new java.awt.Color(255, 255, 255));
        jLabel64.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel64.setText("<html><body><center>(処分事業場)</center></body></html>");
        jLabel64.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        jLabel64.setMaximumSize(new java.awt.Dimension(80, 25));
        jLabel64.setMinimumSize(new java.awt.Dimension(80, 25));
        jLabel64.setPreferredSize(new java.awt.Dimension(80, 25));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        jPanel6.add(jLabel64, gridBagConstraints);

        jLabel71.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        jLabel71.setForeground(new java.awt.Color(255, 255, 255));
        jLabel71.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel71.setText("<html><body><center>運搬先の<br>事業場</center></body></html>");
        jLabel71.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);
        jLabel71.setMaximumSize(new java.awt.Dimension(80, 50));
        jLabel71.setMinimumSize(new java.awt.Dimension(80, 50));
        jLabel71.setPreferredSize(new java.awt.Dimension(80, 50));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        jPanel6.add(jLabel71, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        jPanel34.add(jPanel6, gridBagConstraints);

        jPanel10.setBorder(javax.swing.BorderFactory.createEtchedBorder(new java.awt.Color(0, 102, 153), null));
        jPanel10.setFont(new java.awt.Font("MS UI Gothic", 1, 24)); // NOI18N
        jPanel10.setMaximumSize(new java.awt.Dimension(480, 95));
        jPanel10.setMinimumSize(new java.awt.Dimension(480, 95));
        jPanel10.setPreferredSize(new java.awt.Dimension(480, 95));
        jPanel10.setLayout(new java.awt.GridBagLayout());

        jLabel2.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel2.setText("名称　");
        jLabel2.setMaximumSize(new java.awt.Dimension(80, 25));
        jLabel2.setMinimumSize(new java.awt.Dimension(80, 25));
        jLabel2.setPreferredSize(new java.awt.Dimension(80, 25));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        jPanel10.add(jLabel2, gridBagConstraints);

        jLabel5.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel5.setText("電話番号");
        jLabel5.setMinimumSize(new java.awt.Dimension(80, 25));
        jLabel5.setPreferredSize(new java.awt.Dimension(80, 25));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        jPanel10.add(jLabel5, gridBagConstraints);

        cont_office_zip01.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        cont_office_zip01.setMinimumSize(new java.awt.Dimension(100, 25));
        cont_office_zip01.setPreferredSize(new java.awt.Dimension(100, 25));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 0, 2, 0);
        jPanel10.add(cont_office_zip01, gridBagConstraints);

        cont_office_tel01.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        cont_office_tel01.setMinimumSize(new java.awt.Dimension(150, 25));
        cont_office_tel01.setPreferredSize(new java.awt.Dimension(150, 25));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 0, 2, 0);
        jPanel10.add(cont_office_tel01, gridBagConstraints);

        cont_office_address01.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        cont_office_address01.setMaximumSize(new java.awt.Dimension(400, 25));
        cont_office_address01.setMinimumSize(new java.awt.Dimension(400, 25));
        cont_office_address01.setPreferredSize(new java.awt.Dimension(400, 25));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 0, 2, 0);
        jPanel10.add(cont_office_address01, gridBagConstraints);

        jLabel6.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        jLabel6.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel6.setText("所在地　");
        jLabel6.setMaximumSize(new java.awt.Dimension(80, 25));
        jLabel6.setMinimumSize(new java.awt.Dimension(80, 25));
        jLabel6.setPreferredSize(new java.awt.Dimension(80, 25));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        jPanel10.add(jLabel6, gridBagConstraints);

        cont_office_name01.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        cont_office_name01.setMinimumSize(new java.awt.Dimension(130, 27));
        cont_office_name01.setPreferredSize(new java.awt.Dimension(130, 27));
        cont_office_name01.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cont_office_name01ItemStateChanged(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(2, 0, 2, 0);
        jPanel10.add(cont_office_name01, gridBagConstraints);

        jLabel33.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        jLabel33.setText("〒");
        jLabel33.setMaximumSize(new java.awt.Dimension(14, 25));
        jLabel33.setMinimumSize(new java.awt.Dimension(14, 25));
        jLabel33.setPreferredSize(new java.awt.Dimension(14, 25));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 0, 2, 0);
        jPanel10.add(jLabel33, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        jPanel34.add(jPanel10, gridBagConstraints);

        jPanel11.setBackground(new java.awt.Color(0, 102, 153));
        jPanel11.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel11.setMinimumSize(new java.awt.Dimension(70, 255));
        jPanel11.setPreferredSize(new java.awt.Dimension(70, 255));
        jPanel11.setLayout(new java.awt.GridBagLayout());

        jLabel44.setFont(new java.awt.Font("メイリオ", 1, 14)); // NOI18N
        jLabel44.setForeground(new java.awt.Color(255, 255, 255));
        jLabel44.setText("物");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(26, 25, 30, 25);
        jPanel11.add(jLabel44, gridBagConstraints);

        jLabel93.setFont(new java.awt.Font("メイリオ", 1, 14)); // NOI18N
        jLabel93.setForeground(new java.awt.Color(255, 255, 255));
        jLabel93.setText("棄");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(26, 25, 0, 25);
        jPanel11.add(jLabel93, gridBagConstraints);

        jLabel96.setFont(new java.awt.Font("メイリオ", 1, 14)); // NOI18N
        jLabel96.setForeground(new java.awt.Color(255, 255, 255));
        jLabel96.setText("産");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(34, 25, 0, 25);
        jPanel11.add(jLabel96, gridBagConstraints);

        jLabel97.setFont(new java.awt.Font("メイリオ", 1, 14)); // NOI18N
        jLabel97.setForeground(new java.awt.Color(255, 255, 255));
        jLabel97.setText("業");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(27, 25, 0, 25);
        jPanel11.add(jLabel97, gridBagConstraints);

        jLabel98.setFont(new java.awt.Font("メイリオ", 1, 14)); // NOI18N
        jLabel98.setForeground(new java.awt.Color(255, 255, 255));
        jLabel98.setText("廃");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(26, 25, 0, 25);
        jPanel11.add(jLabel98, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        jPanel34.add(jPanel11, gridBagConstraints);

        jPanel12.setBackground(new java.awt.Color(0, 102, 153));
        jPanel12.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel12.setMaximumSize(new java.awt.Dimension(70, 70));
        jPanel12.setMinimumSize(new java.awt.Dimension(70, 70));
        jPanel12.setPreferredSize(new java.awt.Dimension(70, 70));
        jPanel12.setLayout(new java.awt.GridBagLayout());

        jLabel122.setFont(new java.awt.Font("メイリオ", 1, 12)); // NOI18N
        jLabel122.setForeground(new java.awt.Color(255, 255, 255));
        jLabel122.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel122.setText("中間処理");
        jLabel122.setMinimumSize(new java.awt.Dimension(60, 20));
        jLabel122.setPreferredSize(new java.awt.Dimension(60, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        jPanel12.add(jLabel122, gridBagConstraints);

        jLabel123.setFont(new java.awt.Font("メイリオ", 1, 12)); // NOI18N
        jLabel123.setForeground(new java.awt.Color(255, 255, 255));
        jLabel123.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel123.setText("産業廃棄物");
        jLabel123.setMinimumSize(new java.awt.Dimension(60, 20));
        jLabel123.setPreferredSize(new java.awt.Dimension(60, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.SOUTH;
        jPanel12.add(jLabel123, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        jPanel34.add(jPanel12, gridBagConstraints);

        jPanel13.setBorder(javax.swing.BorderFactory.createEtchedBorder(new java.awt.Color(0, 102, 153), null));
        jPanel13.setMaximumSize(new java.awt.Dimension(1130, 80));
        jPanel13.setMinimumSize(new java.awt.Dimension(1130, 80));
        jPanel13.setPreferredSize(new java.awt.Dimension(1130, 80));
        jPanel13.setLayout(new java.awt.GridBagLayout());

        jLabel91.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        jLabel91.setText("  管理票交付者(処分委託者)の氏名又は名称及び管理表の交付番号(登録番号)");
        jLabel91.setMaximumSize(new java.awt.Dimension(450, 13));
        jLabel91.setMinimumSize(new java.awt.Dimension(429, 20));
        jLabel91.setPreferredSize(new java.awt.Dimension(450, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 6;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        jPanel13.add(jLabel91, gridBagConstraints);

        check_f2.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        check_f2.setText("当欄記載のとおり");
        check_f2.setMinimumSize(new java.awt.Dimension(150, 25));
        check_f2.setPreferredSize(new java.awt.Dimension(150, 25));
        check_f2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                check_f2ActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        jPanel13.add(check_f2, gridBagConstraints);

        check_f1.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        check_f1.setText("帳簿記載のとおり");
        check_f1.setMinimumSize(new java.awt.Dimension(150, 21));
        check_f1.setPreferredSize(new java.awt.Dimension(150, 21));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        jPanel13.add(check_f1, gridBagConstraints);

        slash_f1.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        slash_f1.setText("斜線有り");
        slash_f1.setMinimumSize(new java.awt.Dimension(100, 25));
        slash_f1.setPreferredSize(new java.awt.Dimension(100, 25));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        jPanel13.add(slash_f1, gridBagConstraints);

        jLabel92.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        jLabel92.setText("　名称");
        jLabel92.setMinimumSize(new java.awt.Dimension(50, 25));
        jLabel92.setPreferredSize(new java.awt.Dimension(50, 25));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        jPanel13.add(jLabel92, gridBagConstraints);

        cont_name.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        cont_name.setMinimumSize(new java.awt.Dimension(300, 25));
        cont_name.setPreferredSize(new java.awt.Dimension(300, 25));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 2;
        jPanel13.add(cont_name, gridBagConstraints);

        jLabel104.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        jLabel104.setText("　交付番号");
        jLabel104.setMinimumSize(new java.awt.Dimension(80, 25));
        jLabel104.setPreferredSize(new java.awt.Dimension(80, 25));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        jPanel13.add(jLabel104, gridBagConstraints);

        cont_no.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        cont_no.setMinimumSize(new java.awt.Dimension(150, 25));
        cont_no.setPreferredSize(new java.awt.Dimension(150, 25));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 2;
        jPanel13.add(cont_no, gridBagConstraints);

        jPanel32.setMaximumSize(new java.awt.Dimension(300, 60));
        jPanel32.setMinimumSize(new java.awt.Dimension(300, 60));
        jPanel32.setPreferredSize(new java.awt.Dimension(300, 60));
        jPanel32.setLayout(new java.awt.GridBagLayout());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 6;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridheight = 3;
        gridBagConstraints.weightx = 1.0;
        jPanel13.add(jPanel32, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        jPanel34.add(jPanel13, gridBagConstraints);

        jPanel14.setBorder(javax.swing.BorderFactory.createEtchedBorder(new java.awt.Color(0, 102, 153), null));
        jPanel14.setMinimumSize(new java.awt.Dimension(484, 35));
        jPanel14.setPreferredSize(new java.awt.Dimension(407, 35));
        jPanel14.setLayout(new java.awt.GridBagLayout());

        trans1_date_year.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        trans1_date_year.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        trans1_date_year.setMinimumSize(new java.awt.Dimension(60, 25));
        trans1_date_year.setPreferredSize(new java.awt.Dimension(60, 25));
        trans1_date_year.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                trans1_date_yearActionPerformed(evt);
            }
        });
        trans1_date_year.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                trans1_date_yearKeyPressed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 0, 0);
        jPanel14.add(trans1_date_year, gridBagConstraints);

        jLabel75.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        jLabel75.setText("年");
        jLabel75.setMinimumSize(new java.awt.Dimension(20, 20));
        jLabel75.setPreferredSize(new java.awt.Dimension(20, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        jPanel14.add(jLabel75, gridBagConstraints);

        trans1_date_month.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        trans1_date_month.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        trans1_date_month.setMinimumSize(new java.awt.Dimension(35, 25));
        trans1_date_month.setPreferredSize(new java.awt.Dimension(35, 25));
        trans1_date_month.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                trans1_date_monthKeyPressed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        jPanel14.add(trans1_date_month, gridBagConstraints);

        jLabel76.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        jLabel76.setText("月");
        jLabel76.setMinimumSize(new java.awt.Dimension(20, 20));
        jLabel76.setPreferredSize(new java.awt.Dimension(20, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 0;
        jPanel14.add(jLabel76, gridBagConstraints);

        trans1_date_day.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        trans1_date_day.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        trans1_date_day.setMinimumSize(new java.awt.Dimension(35, 25));
        trans1_date_day.setPreferredSize(new java.awt.Dimension(35, 25));
        trans1_date_day.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                trans1_date_dayKeyPressed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 0;
        jPanel14.add(trans1_date_day, gridBagConstraints);

        jLabel77.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        jLabel77.setText("日　");
        jLabel77.setMaximumSize(new java.awt.Dimension(30, 20));
        jLabel77.setMinimumSize(new java.awt.Dimension(30, 20));
        jLabel77.setPreferredSize(new java.awt.Dimension(30, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 6;
        gridBagConstraints.gridy = 0;
        jPanel14.add(jLabel77, gridBagConstraints);

        jLabel54.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        jLabel54.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel54.setText(" 数量（及び単位）");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 8;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        jPanel14.add(jLabel54, gridBagConstraints);

        trans1_weight.setFont(new java.awt.Font("メイリオ", 0, 12)); // NOI18N
        trans1_weight.setMinimumSize(new java.awt.Dimension(90, 25));
        trans1_weight.setPreferredSize(new java.awt.Dimension(90, 25));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 9;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.weightx = 1.0;
        jPanel14.add(trans1_weight, gridBagConstraints);

        jLabel86.setBackground(new java.awt.Color(0, 153, 204));
        jLabel86.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        jLabel86.setForeground(new java.awt.Color(255, 255, 255));
        jLabel86.setText("有価物拾集量");
        jLabel86.setBorder(javax.swing.BorderFactory.createEtchedBorder(new java.awt.Color(0, 102, 153), null));
        jLabel86.setOpaque(true);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 7;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.weighty = 1.0;
        jPanel14.add(jLabel86, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        jPanel34.add(jPanel14, gridBagConstraints);

        jPanel16.setBorder(javax.swing.BorderFactory.createEtchedBorder(new java.awt.Color(0, 102, 153), null));
        jPanel16.setMinimumSize(new java.awt.Dimension(520, 95));
        jPanel16.setPreferredSize(new java.awt.Dimension(520, 95));
        jPanel16.setLayout(new java.awt.GridBagLayout());

        排出事業場_print_f.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        排出事業場_print_f.setText("印字なし");
        排出事業場_print_f.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        排出事業場_print_f.setMinimumSize(new java.awt.Dimension(100, 25));
        排出事業場_print_f.setPreferredSize(new java.awt.Dimension(100, 25));
        排出事業場_print_f.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                排出事業場_print_fItemStateChanged(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        jPanel16.add(排出事業場_print_f, gridBagConstraints);

        jLabel10.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        jLabel10.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel10.setText("名称");
        jLabel10.setMinimumSize(new java.awt.Dimension(40, 25));
        jLabel10.setPreferredSize(new java.awt.Dimension(40, 25));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 2);
        jPanel16.add(jLabel10, gridBagConstraints);

        office_id01.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        office_id01.setMaximumSize(new java.awt.Dimension(80, 25));
        office_id01.setMinimumSize(new java.awt.Dimension(80, 25));
        office_id01.setPreferredSize(new java.awt.Dimension(80, 25));
        office_id01.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                office_id01ItemStateChanged(evt);
            }
        });
        office_id01.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                office_id01ActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.ipady = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 2);
        jPanel16.add(office_id01, gridBagConstraints);

        jLabel11.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        jLabel11.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel11.setText("電話番号");
        jLabel11.setMinimumSize(new java.awt.Dimension(80, 25));
        jLabel11.setPreferredSize(new java.awt.Dimension(80, 25));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        jPanel16.add(jLabel11, gridBagConstraints);

        office_zip01.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        office_zip01.setMinimumSize(new java.awt.Dimension(85, 30));
        office_zip01.setPreferredSize(new java.awt.Dimension(85, 25));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(2, 0, 2, 0);
        jPanel16.add(office_zip01, gridBagConstraints);

        office_tel01.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        office_tel01.setMinimumSize(new java.awt.Dimension(120, 30));
        office_tel01.setPreferredSize(new java.awt.Dimension(120, 25));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 0, 2, 20);
        jPanel16.add(office_tel01, gridBagConstraints);

        office_address01.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        office_address01.setMinimumSize(new java.awt.Dimension(300, 30));
        office_address01.setPreferredSize(new java.awt.Dimension(300, 25));
        office_address01.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                office_address01ActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 0, 2, 20);
        jPanel16.add(office_address01, gridBagConstraints);

        jLabel22.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        jLabel22.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel22.setText("所在地");
        jLabel22.setMinimumSize(new java.awt.Dimension(60, 25));
        jLabel22.setPreferredSize(new java.awt.Dimension(60, 25));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 2);
        jPanel16.add(jLabel22, gridBagConstraints);

        office_sub_name_.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        office_sub_name_.setMinimumSize(new java.awt.Dimension(150, 27));
        office_sub_name_.setPreferredSize(new java.awt.Dimension(150, 27));
        office_sub_name_.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                office_sub_name_ActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 20);
        jPanel16.add(office_sub_name_, gridBagConstraints);

        jLabel94.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        jLabel94.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel94.setText("ｺｰﾄﾞ");
        jLabel94.setMaximumSize(new java.awt.Dimension(35, 20));
        jLabel94.setMinimumSize(new java.awt.Dimension(35, 20));
        jLabel94.setPreferredSize(new java.awt.Dimension(35, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 2);
        jPanel16.add(jLabel94, gridBagConstraints);

        office_search_.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        office_search_.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        office_search_.setMaximumSize(new java.awt.Dimension(60, 25));
        office_search_.setMinimumSize(new java.awt.Dimension(60, 25));
        office_search_.setPreferredSize(new java.awt.Dimension(60, 25));
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
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        jPanel16.add(office_search_, gridBagConstraints);

        office_name_.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        office_name_.setMinimumSize(new java.awt.Dimension(85, 30));
        office_name_.setPreferredSize(new java.awt.Dimension(85, 25));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(2, 0, 2, 0);
        jPanel16.add(office_name_, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        jPanel34.add(jPanel16, gridBagConstraints);

        jPanel17.setBackground(new java.awt.Color(0, 102, 153));
        jPanel17.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel17.setForeground(new java.awt.Color(255, 255, 255));
        jPanel17.setMinimumSize(new java.awt.Dimension(70, 35));
        jPanel17.setPreferredSize(new java.awt.Dimension(70, 35));
        jPanel17.setLayout(new java.awt.GridBagLayout());

        jLabel35.setBackground(new java.awt.Color(153, 153, 255));
        jLabel35.setFont(new java.awt.Font("メイリオ", 1, 12)); // NOI18N
        jLabel35.setForeground(new java.awt.Color(255, 255, 255));
        jLabel35.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel35.setText("運搬担当者");
        jLabel35.setMinimumSize(new java.awt.Dimension(65, 25));
        jLabel35.setPreferredSize(new java.awt.Dimension(65, 25));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        jPanel17.add(jLabel35, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        jPanel34.add(jPanel17, gridBagConstraints);

        jPanel18.setBackground(new java.awt.Color(0, 102, 153));
        jPanel18.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel18.setForeground(new java.awt.Color(255, 255, 255));
        jPanel18.setMinimumSize(new java.awt.Dimension(70, 85));
        jPanel18.setPreferredSize(new java.awt.Dimension(70, 70));
        jPanel18.setLayout(new java.awt.GridBagLayout());

        jLabel36.setFont(new java.awt.Font("メイリオ", 1, 12)); // NOI18N
        jLabel36.setForeground(new java.awt.Color(255, 255, 255));
        jLabel36.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel36.setText("の 場 所");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        jPanel18.add(jLabel36, gridBagConstraints);

        jLabel42.setFont(new java.awt.Font("メイリオ", 1, 12)); // NOI18N
        jLabel42.setForeground(new java.awt.Color(255, 255, 255));
        jLabel42.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel42.setText("最終処分");
        jLabel42.setMaximumSize(new java.awt.Dimension(52, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        jPanel18.add(jLabel42, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        jPanel34.add(jPanel18, gridBagConstraints);

        jPanel19.setBackground(new java.awt.Color(0, 102, 153));
        jPanel19.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel19.setForeground(new java.awt.Color(255, 255, 255));
        jPanel19.setMinimumSize(new java.awt.Dimension(70, 55));
        jPanel19.setPreferredSize(new java.awt.Dimension(70, 55));
        jPanel19.setLayout(new java.awt.GridBagLayout());

        jLabel83.setFont(new java.awt.Font("メイリオ", 1, 12)); // NOI18N
        jLabel83.setForeground(new java.awt.Color(255, 255, 255));
        jLabel83.setText("最終処分を");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        jPanel19.add(jLabel83, gridBagConstraints);

        jLabel109.setFont(new java.awt.Font("メイリオ", 1, 12)); // NOI18N
        jLabel109.setForeground(new java.awt.Color(255, 255, 255));
        jLabel109.setText("行った場所");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        jPanel19.add(jLabel109, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        jPanel34.add(jPanel19, gridBagConstraints);

        jPanel20.setBackground(new java.awt.Color(0, 102, 153));
        jPanel20.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel20.setForeground(new java.awt.Color(255, 255, 255));
        jPanel20.setMinimumSize(new java.awt.Dimension(70, 95));
        jPanel20.setPreferredSize(new java.awt.Dimension(70, 95));
        jPanel20.setLayout(new java.awt.GridBagLayout());

        jLabel125.setFont(new java.awt.Font("メイリオ", 1, 14)); // NOI18N
        jLabel125.setForeground(new java.awt.Color(255, 255, 255));
        jLabel125.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel125.setText("<html><body><center>処 分<br>受託者</center></body></html>");
        jLabel125.setMaximumSize(new java.awt.Dimension(60, 60));
        jLabel125.setMinimumSize(new java.awt.Dimension(60, 60));
        jLabel125.setPreferredSize(new java.awt.Dimension(60, 60));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        jPanel20.add(jLabel125, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        jPanel34.add(jPanel20, gridBagConstraints);

        jPanel21.setBorder(javax.swing.BorderFactory.createEtchedBorder(new java.awt.Color(0, 102, 153), null));
        jPanel21.setMinimumSize(new java.awt.Dimension(404, 35));
        jPanel21.setPreferredSize(new java.awt.Dimension(424, 35));
        jPanel21.setLayout(new java.awt.GridBagLayout());

        trans1_sub_name.setFont(new java.awt.Font("メイリオ", 0, 12)); // NOI18N
        trans1_sub_name.setMaximumSize(new java.awt.Dimension(150, 25));
        trans1_sub_name.setMinimumSize(new java.awt.Dimension(150, 25));
        trans1_sub_name.setPreferredSize(new java.awt.Dimension(150, 25));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        jPanel21.add(trans1_sub_name, gridBagConstraints);

        trans1_sub_name1.setFont(new java.awt.Font("メイリオ", 0, 12)); // NOI18N
        trans1_sub_name1.setMaximumSize(new java.awt.Dimension(10, 25));
        trans1_sub_name1.setMinimumSize(new java.awt.Dimension(10, 25));
        trans1_sub_name1.setPreferredSize(new java.awt.Dimension(10, 25));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 6;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        jPanel21.add(trans1_sub_name1, gridBagConstraints);

        運搬担当者_print_3.setFont(new java.awt.Font("メイリオ", 0, 10)); // NOI18N
        運搬担当者_print_3.setText("印字無 会社名");
        運搬担当者_print_3.setActionCommand("印字なし\n");
        運搬担当者_print_3.setMaximumSize(new java.awt.Dimension(90, 23));
        運搬担当者_print_3.setMinimumSize(new java.awt.Dimension(90, 23));
        運搬担当者_print_3.setPreferredSize(new java.awt.Dimension(90, 23));
        運搬担当者_print_3.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                運搬担当者_print_3ItemStateChanged(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        jPanel21.add(運搬担当者_print_3, gridBagConstraints);

        運搬担当者_print_f4.setFont(new java.awt.Font("メイリオ", 0, 10)); // NOI18N
        運搬担当者_print_f4.setText("印字無 氏名");
        運搬担当者_print_f4.setActionCommand("印字なし\n");
        運搬担当者_print_f4.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        運搬担当者_print_f4.setMaximumSize(new java.awt.Dimension(80, 23));
        運搬担当者_print_f4.setMinimumSize(new java.awt.Dimension(80, 23));
        運搬担当者_print_f4.setPreferredSize(new java.awt.Dimension(80, 23));
        運搬担当者_print_f4.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                運搬担当者_print_f4ItemStateChanged(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        jPanel21.add(運搬担当者_print_f4, gridBagConstraints);

        trans_tantou_search_1.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        trans_tantou_search_1.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        trans_tantou_search_1.setMaximumSize(new java.awt.Dimension(40, 25));
        trans_tantou_search_1.setMinimumSize(new java.awt.Dimension(40, 25));
        trans_tantou_search_1.setPreferredSize(new java.awt.Dimension(40, 25));
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
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 0, 2);
        jPanel21.add(trans_tantou_search_1, gridBagConstraints);

        trans_tantou_id_cmb1.setEditable(true);
        trans_tantou_id_cmb1.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        trans_tantou_id_cmb1.setMaximumSize(new java.awt.Dimension(130, 27));
        trans_tantou_id_cmb1.setMinimumSize(new java.awt.Dimension(130, 27));
        trans_tantou_id_cmb1.setPreferredSize(new java.awt.Dimension(130, 27));
        trans_tantou_id_cmb1.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                trans_tantou_id_cmb1ItemStateChanged(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(2, 0, 2, 2);
        jPanel21.add(trans_tantou_id_cmb1, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        jPanel34.add(jPanel21, gridBagConstraints);

        jPanel22.setBorder(javax.swing.BorderFactory.createEtchedBorder(new java.awt.Color(0, 102, 153), null));
        jPanel22.setMinimumSize(new java.awt.Dimension(520, 95));
        jPanel22.setPreferredSize(new java.awt.Dimension(520, 95));
        jPanel22.setLayout(new java.awt.GridBagLayout());

        処分受託者_print_f.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        処分受託者_print_f.setText("印字なし");
        処分受託者_print_f.setMaximumSize(new java.awt.Dimension(100, 31));
        処分受託者_print_f.setMinimumSize(new java.awt.Dimension(100, 31));
        処分受託者_print_f.setPreferredSize(new java.awt.Dimension(100, 31));
        処分受託者_print_f.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                処分受託者_print_fItemStateChanged(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        jPanel22.add(処分受託者_print_f, gridBagConstraints);

        jLabel106.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        jLabel106.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel106.setText("氏名又は名称");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 2);
        jPanel22.add(jLabel106, gridBagConstraints);

        cont_cmb01.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        cont_cmb01.setMinimumSize(new java.awt.Dimension(50, 27));
        cont_cmb01.setPreferredSize(new java.awt.Dimension(50, 27));
        cont_cmb01.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cont_cmb01ItemStateChanged(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        jPanel22.add(cont_cmb01, gridBagConstraints);

        jLabel107.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        jLabel107.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel107.setText("電話番号");
        jLabel107.setMinimumSize(new java.awt.Dimension(80, 25));
        jLabel107.setPreferredSize(new java.awt.Dimension(80, 25));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 1;
        jPanel22.add(jLabel107, gridBagConstraints);

        cont_zip01.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        cont_zip01.setMinimumSize(new java.awt.Dimension(100, 25));
        cont_zip01.setPreferredSize(new java.awt.Dimension(100, 25));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        jPanel22.add(cont_zip01, gridBagConstraints);

        cont_tel01.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        cont_tel01.setMinimumSize(new java.awt.Dimension(170, 25));
        cont_tel01.setPreferredSize(new java.awt.Dimension(170, 25));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        jPanel22.add(cont_tel01, gridBagConstraints);

        cont_address01.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        cont_address01.setMinimumSize(new java.awt.Dimension(340, 25));
        cont_address01.setPreferredSize(new java.awt.Dimension(340, 25));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        jPanel22.add(cont_address01, gridBagConstraints);

        jLabel108.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        jLabel108.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel108.setText("住所");
        jLabel108.setMinimumSize(new java.awt.Dimension(40, 25));
        jLabel108.setPreferredSize(new java.awt.Dimension(40, 25));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 2);
        jPanel22.add(jLabel108, gridBagConstraints);

        jLabel127.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        jLabel127.setText("ｺｰﾄﾞ");
        jLabel127.setMaximumSize(new java.awt.Dimension(30, 20));
        jLabel127.setMinimumSize(new java.awt.Dimension(30, 20));
        jLabel127.setPreferredSize(new java.awt.Dimension(30, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 2, 0, 2);
        jPanel22.add(jLabel127, gridBagConstraints);

        cont_search_.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        cont_search_.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        cont_search_.setMaximumSize(new java.awt.Dimension(68, 25));
        cont_search_.setMinimumSize(new java.awt.Dimension(68, 25));
        cont_search_.setPreferredSize(new java.awt.Dimension(68, 25));
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
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 2);
        jPanel22.add(cont_search_, gridBagConstraints);

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
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 0, 2, 2);
        jPanel22.add(jButton4, gridBagConstraints);

        jLabel32.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        jLabel32.setText("〒");
        jLabel32.setMaximumSize(new java.awt.Dimension(14, 25));
        jLabel32.setMinimumSize(new java.awt.Dimension(14, 25));
        jLabel32.setPreferredSize(new java.awt.Dimension(14, 25));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 0, 2, 0);
        jPanel22.add(jLabel32, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        jPanel34.add(jPanel22, gridBagConstraints);

        jPanel23.setBorder(javax.swing.BorderFactory.createEtchedBorder(new java.awt.Color(0, 102, 153), null));
        jPanel23.setMinimumSize(new java.awt.Dimension(695, 60));
        jPanel23.setPreferredSize(new java.awt.Dimension(695, 60));
        jPanel23.setLayout(new java.awt.GridBagLayout());

        jLabel84.setFont(new java.awt.Font("メイリオ", 0, 12)); // NOI18N
        jLabel84.setText("名称/所在地/電話番号");
        jLabel84.setMinimumSize(new java.awt.Dimension(150, 20));
        jLabel84.setPreferredSize(new java.awt.Dimension(150, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 2, 0, 0);
        jPanel23.add(jLabel84, gridBagConstraints);

        jLabel85.setFont(new java.awt.Font("メイリオ", 0, 12)); // NOI18N
        jLabel85.setText("（委託契約書記載の場所にあっては委託契約書記載の番号）");
        jLabel85.setMaximumSize(new java.awt.Dimension(350, 13));
        jLabel85.setMinimumSize(new java.awt.Dimension(350, 20));
        jLabel85.setPreferredSize(new java.awt.Dimension(350, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        jPanel23.add(jLabel85, gridBagConstraints);

        last_cont_cmb.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        last_cont_cmb.setMinimumSize(new java.awt.Dimension(520, 27));
        last_cont_cmb.setPreferredSize(new java.awt.Dimension(520, 27));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.ipadx = 191;
        gridBagConstraints.ipady = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 2, 0, 0);
        jPanel23.add(last_cont_cmb, gridBagConstraints);

        jPanel38.setMinimumSize(new java.awt.Dimension(350, 40));
        jPanel38.setPreferredSize(new java.awt.Dimension(350, 40));
        jPanel38.setLayout(new java.awt.GridBagLayout());

        画面初期化_btn_.setFont(new java.awt.Font("メイリオ", 0, 18)); // NOI18N
        画面初期化_btn_.setText("クリア");
        画面初期化_btn_.setMaximumSize(new java.awt.Dimension(110, 30));
        画面初期化_btn_.setMinimumSize(new java.awt.Dimension(110, 30));
        画面初期化_btn_.setPreferredSize(new java.awt.Dimension(110, 30));
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
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.SOUTHEAST;
        gridBagConstraints.insets = new java.awt.Insets(20, 2, 0, 2);
        jPanel38.add(画面初期化_btn_, gridBagConstraints);

        jPanel40.setMaximumSize(new java.awt.Dimension(20, 20));
        jPanel40.setMinimumSize(new java.awt.Dimension(20, 20));

        javax.swing.GroupLayout jPanel40Layout = new javax.swing.GroupLayout(jPanel40);
        jPanel40.setLayout(jPanel40Layout);
        jPanel40Layout.setHorizontalGroup(
            jPanel40Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 360, Short.MAX_VALUE)
        );
        jPanel40Layout.setVerticalGroup(
            jPanel40Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 50, Short.MAX_VALUE)
        );

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        jPanel38.add(jPanel40, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        jPanel23.add(jPanel38, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        jPanel34.add(jPanel23, gridBagConstraints);

        jPanel15.setBackground(new java.awt.Color(0, 153, 204));
        jPanel15.setBorder(javax.swing.BorderFactory.createEtchedBorder(new java.awt.Color(0, 102, 153), null));
        jPanel15.setMinimumSize(new java.awt.Dimension(50, 95));
        jPanel15.setPreferredSize(new java.awt.Dimension(50, 95));
        jPanel15.setLayout(new java.awt.GridBagLayout());

        jLabel47.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        jLabel47.setForeground(new java.awt.Color(255, 255, 255));
        jLabel47.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel47.setText("積み替え");
        jLabel47.setMaximumSize(new java.awt.Dimension(54, 20));
        jLabel47.setMinimumSize(new java.awt.Dimension(54, 20));
        jLabel47.setPreferredSize(new java.awt.Dimension(54, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.SOUTH;
        gridBagConstraints.weightx = 1.0;
        jPanel15.add(jLabel47, gridBagConstraints);

        jLabel117.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        jLabel117.setForeground(new java.awt.Color(255, 255, 255));
        jLabel117.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel117.setText("又は保管");
        jLabel117.setMinimumSize(new java.awt.Dimension(60, 20));
        jLabel117.setPreferredSize(new java.awt.Dimension(60, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        jPanel15.add(jLabel117, gridBagConstraints);

        slash_f2.setBackground(new java.awt.Color(0, 153, 204));
        slash_f2.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        slash_f2.setForeground(new java.awt.Color(255, 255, 255));
        slash_f2.setText("斜線有り");
        slash_f2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        slash_f2.setMinimumSize(new java.awt.Dimension(80, 25));
        slash_f2.setPreferredSize(new java.awt.Dimension(80, 25));
        slash_f2.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                slash_f2ItemStateChanged(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        jPanel15.add(slash_f2, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        jPanel34.add(jPanel15, gridBagConstraints);

        jPanel24.setBackground(new java.awt.Color(0, 102, 153));
        jPanel24.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel24.setForeground(new java.awt.Color(255, 255, 255));
        jPanel24.setMinimumSize(new java.awt.Dimension(70, 95));
        jPanel24.setPreferredSize(new java.awt.Dimension(70, 95));
        jPanel24.setLayout(new java.awt.GridBagLayout());

        jLabel59.setFont(new java.awt.Font("メイリオ", 1, 14)); // NOI18N
        jLabel59.setForeground(new java.awt.Color(255, 255, 255));
        jLabel59.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel59.setText("<html><body><center>運 搬<br>受託者</center></body></html>");
        jLabel59.setMaximumSize(new java.awt.Dimension(60, 60));
        jLabel59.setMinimumSize(new java.awt.Dimension(60, 60));
        jLabel59.setPreferredSize(new java.awt.Dimension(60, 60));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        jPanel24.add(jLabel59, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        jPanel34.add(jPanel24, gridBagConstraints);

        jPanel25.setBorder(javax.swing.BorderFactory.createEtchedBorder(new java.awt.Color(0, 102, 153), null));
        jPanel25.setMinimumSize(new java.awt.Dimension(520, 95));
        jPanel25.setPreferredSize(new java.awt.Dimension(520, 95));
        jPanel25.setLayout(new java.awt.GridBagLayout());

        積み替え_f.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        積み替え_f.setText("印字なし");
        積み替え_f.setMaximumSize(new java.awt.Dimension(100, 23));
        積み替え_f.setMinimumSize(new java.awt.Dimension(100, 23));
        積み替え_f.setPreferredSize(new java.awt.Dimension(100, 23));
        積み替え_f.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                積み替え_fItemStateChanged(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 0, 2, 0);
        jPanel25.add(積み替え_f, gridBagConstraints);

        jLabel8.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        jLabel8.setText("　電話番号");
        jLabel8.setMinimumSize(new java.awt.Dimension(70, 20));
        jLabel8.setPreferredSize(new java.awt.Dimension(70, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        jPanel25.add(jLabel8, gridBagConstraints);

        jLabel9.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        jLabel9.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel9.setText("所在地");
        jLabel9.setMinimumSize(new java.awt.Dimension(55, 20));
        jLabel9.setPreferredSize(new java.awt.Dimension(55, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 2);
        jPanel25.add(jLabel9, gridBagConstraints);

        cont_office_zip2.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        cont_office_zip2.setMinimumSize(new java.awt.Dimension(100, 25));
        cont_office_zip2.setPreferredSize(new java.awt.Dimension(100, 25));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 0, 2, 0);
        jPanel25.add(cont_office_zip2, gridBagConstraints);

        cont_office_tel2.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        cont_office_tel2.setMinimumSize(new java.awt.Dimension(150, 25));
        cont_office_tel2.setPreferredSize(new java.awt.Dimension(150, 25));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 0, 2, 0);
        jPanel25.add(cont_office_tel2, gridBagConstraints);

        cont_office_address2.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        cont_office_address2.setMaximumSize(new java.awt.Dimension(350, 25));
        cont_office_address2.setMinimumSize(new java.awt.Dimension(350, 25));
        cont_office_address2.setPreferredSize(new java.awt.Dimension(350, 25));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 0, 2, 0);
        jPanel25.add(cont_office_address2, gridBagConstraints);

        jLabel3.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel3.setText("名称");
        jLabel3.setMinimumSize(new java.awt.Dimension(40, 25));
        jLabel3.setPreferredSize(new java.awt.Dimension(40, 25));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 2);
        jPanel25.add(jLabel3, gridBagConstraints);

        jLabel128.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        jLabel128.setText("ｺｰﾄﾞ");
        jLabel128.setMaximumSize(new java.awt.Dimension(30, 20));
        jLabel128.setMinimumSize(new java.awt.Dimension(30, 20));
        jLabel128.setPreferredSize(new java.awt.Dimension(30, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 2, 0, 2);
        jPanel25.add(jLabel128, gridBagConstraints);

        cont_search_1.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        cont_search_1.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        cont_search_1.setMaximumSize(new java.awt.Dimension(68, 25));
        cont_search_1.setMinimumSize(new java.awt.Dimension(68, 25));
        cont_search_1.setPreferredSize(new java.awt.Dimension(68, 25));
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
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 2);
        jPanel25.add(cont_search_1, gridBagConstraints);

        jButton5.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        jButton5.setText("ｶﾅ検索");
        jButton5.setFocusable(false);
        jButton5.setMargin(new java.awt.Insets(0, 0, 0, 0));
        jButton5.setMaximumSize(new java.awt.Dimension(70, 25));
        jButton5.setMinimumSize(new java.awt.Dimension(70, 25));
        jButton5.setPreferredSize(new java.awt.Dimension(70, 25));
        jButton5.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jButton5MouseClicked(evt);
            }
        });
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 0, 2, 2);
        jPanel25.add(jButton5, gridBagConstraints);

        保管場所_cmb.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        保管場所_cmb.setMinimumSize(new java.awt.Dimension(50, 27));
        保管場所_cmb.setPreferredSize(new java.awt.Dimension(50, 27));
        保管場所_cmb.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                保管場所_cmbItemStateChanged(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        jPanel25.add(保管場所_cmb, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        jPanel34.add(jPanel25, gridBagConstraints);

        jPanel26.setBorder(javax.swing.BorderFactory.createEtchedBorder(new java.awt.Color(0, 102, 153), null));
        jPanel26.setMinimumSize(new java.awt.Dimension(520, 95));
        jPanel26.setPreferredSize(new java.awt.Dimension(520, 95));
        jPanel26.setLayout(new java.awt.GridBagLayout());

        運搬受託者_print_f.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        運搬受託者_print_f.setText("印字なし");
        運搬受託者_print_f.setMaximumSize(new java.awt.Dimension(100, 31));
        運搬受託者_print_f.setMinimumSize(new java.awt.Dimension(100, 31));
        運搬受託者_print_f.setPreferredSize(new java.awt.Dimension(100, 31));
        運搬受託者_print_f.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                運搬受託者_print_fItemStateChanged(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        jPanel26.add(運搬受託者_print_f, gridBagConstraints);

        jLabel62.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        jLabel62.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel62.setText("氏名又は名称");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 0, 2, 2);
        jPanel26.add(jLabel62, gridBagConstraints);

        trans_cmb01.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        trans_cmb01.setMinimumSize(new java.awt.Dimension(50, 27));
        trans_cmb01.setPreferredSize(new java.awt.Dimension(50, 27));
        trans_cmb01.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                trans_cmb01ItemStateChanged(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.ipadx = 191;
        gridBagConstraints.ipady = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 0, 2, 0);
        jPanel26.add(trans_cmb01, gridBagConstraints);

        jLabel63.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        jLabel63.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel63.setText("電話番号");
        jLabel63.setMinimumSize(new java.awt.Dimension(80, 25));
        jLabel63.setPreferredSize(new java.awt.Dimension(80, 25));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 1;
        jPanel26.add(jLabel63, gridBagConstraints);

        trans_zip01.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        trans_zip01.setMinimumSize(new java.awt.Dimension(100, 25));
        trans_zip01.setPreferredSize(new java.awt.Dimension(100, 25));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(2, 0, 2, 0);
        jPanel26.add(trans_zip01, gridBagConstraints);

        trans_tel01.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        trans_tel01.setMinimumSize(new java.awt.Dimension(170, 25));
        trans_tel01.setPreferredSize(new java.awt.Dimension(170, 25));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 0, 2, 0);
        jPanel26.add(trans_tel01, gridBagConstraints);

        trans_address01.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        trans_address01.setMinimumSize(new java.awt.Dimension(340, 25));
        trans_address01.setPreferredSize(new java.awt.Dimension(340, 25));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 0, 2, 0);
        jPanel26.add(trans_address01, gridBagConstraints);

        jLabel72.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        jLabel72.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel72.setText("住所");
        jLabel72.setMinimumSize(new java.awt.Dimension(40, 25));
        jLabel72.setPreferredSize(new java.awt.Dimension(40, 25));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 2);
        jPanel26.add(jLabel72, gridBagConstraints);

        jLabel87.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        jLabel87.setText("ｺｰﾄﾞ");
        jLabel87.setMaximumSize(new java.awt.Dimension(30, 20));
        jLabel87.setMinimumSize(new java.awt.Dimension(30, 20));
        jLabel87.setPreferredSize(new java.awt.Dimension(30, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 2, 0, 2);
        jPanel26.add(jLabel87, gridBagConstraints);

        trans_search_.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        trans_search_.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        trans_search_.setMaximumSize(new java.awt.Dimension(68, 25));
        trans_search_.setMinimumSize(new java.awt.Dimension(68, 25));
        trans_search_.setPreferredSize(new java.awt.Dimension(68, 25));
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
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 2);
        jPanel26.add(trans_search_, gridBagConstraints);

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
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 0, 2, 2);
        jPanel26.add(jButton2, gridBagConstraints);

        jLabel27.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        jLabel27.setText("〒");
        jLabel27.setMaximumSize(new java.awt.Dimension(14, 25));
        jLabel27.setMinimumSize(new java.awt.Dimension(14, 25));
        jLabel27.setPreferredSize(new java.awt.Dimension(14, 25));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 0, 2, 0);
        jPanel26.add(jLabel27, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        jPanel34.add(jPanel26, gridBagConstraints);

        jPanel27.setBorder(javax.swing.BorderFactory.createEtchedBorder(new java.awt.Color(0, 102, 153), null));
        jPanel27.setMinimumSize(new java.awt.Dimension(404, 35));
        jPanel27.setPreferredSize(new java.awt.Dimension(424, 35));
        jPanel27.setLayout(new java.awt.GridBagLayout());

        cont_sub_name.setFont(new java.awt.Font("メイリオ", 0, 12)); // NOI18N
        cont_sub_name.setMaximumSize(new java.awt.Dimension(150, 25));
        cont_sub_name.setMinimumSize(new java.awt.Dimension(150, 25));
        cont_sub_name.setPreferredSize(new java.awt.Dimension(150, 25));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        jPanel27.add(cont_sub_name, gridBagConstraints);

        cont_sub_name1.setFont(new java.awt.Font("メイリオ", 0, 12)); // NOI18N
        cont_sub_name1.setMaximumSize(new java.awt.Dimension(130, 25));
        cont_sub_name1.setMinimumSize(new java.awt.Dimension(130, 25));
        cont_sub_name1.setPreferredSize(new java.awt.Dimension(130, 25));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        jPanel27.add(cont_sub_name1, gridBagConstraints);

        処分担当者_print_f2.setFont(new java.awt.Font("メイリオ", 0, 10)); // NOI18N
        処分担当者_print_f2.setText("印字無 会社名");
        処分担当者_print_f2.setActionCommand("印字なし\n");
        処分担当者_print_f2.setMaximumSize(new java.awt.Dimension(90, 23));
        処分担当者_print_f2.setMinimumSize(new java.awt.Dimension(90, 23));
        処分担当者_print_f2.setPreferredSize(new java.awt.Dimension(90, 23));
        処分担当者_print_f2.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                処分担当者_print_f2ItemStateChanged(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        jPanel27.add(処分担当者_print_f2, gridBagConstraints);

        処分担当者_print_f3.setFont(new java.awt.Font("メイリオ", 0, 12)); // NOI18N
        処分担当者_print_f3.setText("印字無 氏名");
        処分担当者_print_f3.setActionCommand("印字なし\n");
        処分担当者_print_f3.setMaximumSize(new java.awt.Dimension(130, 23));
        処分担当者_print_f3.setMinimumSize(new java.awt.Dimension(130, 23));
        処分担当者_print_f3.setPreferredSize(new java.awt.Dimension(130, 23));
        処分担当者_print_f3.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                処分担当者_print_f3ItemStateChanged(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        jPanel27.add(処分担当者_print_f3, gridBagConstraints);

        trans1_sub_name2.setFont(new java.awt.Font("メイリオ", 0, 12)); // NOI18N
        trans1_sub_name2.setMaximumSize(new java.awt.Dimension(10, 25));
        trans1_sub_name2.setMinimumSize(new java.awt.Dimension(10, 25));
        trans1_sub_name2.setPreferredSize(new java.awt.Dimension(10, 25));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 6;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        jPanel27.add(trans1_sub_name2, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        jPanel34.add(jPanel27, gridBagConstraints);

        jPanel28.setBackground(new java.awt.Color(0, 102, 153));
        jPanel28.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel28.setForeground(new java.awt.Color(255, 255, 255));
        jPanel28.setMinimumSize(new java.awt.Dimension(70, 35));
        jPanel28.setPreferredSize(new java.awt.Dimension(70, 35));
        jPanel28.setLayout(new java.awt.GridBagLayout());

        jLabel57.setFont(new java.awt.Font("メイリオ", 1, 12)); // NOI18N
        jLabel57.setForeground(new java.awt.Color(255, 255, 255));
        jLabel57.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel57.setText("処分担当者");
        jLabel57.setMinimumSize(new java.awt.Dimension(65, 25));
        jLabel57.setPreferredSize(new java.awt.Dimension(65, 25));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridheight = java.awt.GridBagConstraints.RELATIVE;
        jPanel28.add(jLabel57, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        jPanel34.add(jPanel28, gridBagConstraints);

        jPanel29.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel29.setMinimumSize(new java.awt.Dimension(500, 35));
        jPanel29.setPreferredSize(new java.awt.Dimension(500, 35));
        jPanel29.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        print_btn.setFont(new java.awt.Font("メイリオ", 0, 18)); // NOI18N
        print_btn.setText("印　字");
        print_btn.setMargin(new java.awt.Insets(2, 2, 2, 2));
        print_btn.setMinimumSize(new java.awt.Dimension(110, 30));
        print_btn.setPreferredSize(new java.awt.Dimension(110, 30));
        print_btn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                print_btnActionPerformed(evt);
            }
        });
        jPanel29.add(print_btn, new org.netbeans.lib.awtextra.AbsoluteConstraints(926, 3, -1, -1));

        quit_btn.setFont(new java.awt.Font("メイリオ", 0, 18)); // NOI18N
        quit_btn.setText("閉じる");
        quit_btn.setMargin(new java.awt.Insets(2, 2, 2, 2));
        quit_btn.setMinimumSize(new java.awt.Dimension(110, 30));
        quit_btn.setPreferredSize(new java.awt.Dimension(110, 30));
        quit_btn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                quit_btnMousePressed(evt);
            }
        });
        quit_btn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                quit_btnActionPerformed(evt);
            }
        });
        quit_btn.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                quit_btnKeyPressed(evt);
            }
        });
        jPanel29.add(quit_btn, new org.netbeans.lib.awtextra.AbsoluteConstraints(1154, 3, -1, -1));

        regist_btn.setFont(new java.awt.Font("メイリオ", 0, 18)); // NOI18N
        regist_btn.setText("登　録");
        regist_btn.setMargin(new java.awt.Insets(2, 2, 2, 2));
        regist_btn.setMinimumSize(new java.awt.Dimension(110, 30));
        regist_btn.setPreferredSize(new java.awt.Dimension(110, 30));
        regist_btn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                regist_btnActionPerformed(evt);
            }
        });
        jPanel29.add(regist_btn, new org.netbeans.lib.awtextra.AbsoluteConstraints(1040, 3, -1, -1));

        jLabel20.setFont(new java.awt.Font("メイリオ", 0, 18)); // NOI18N
        jLabel20.setText("印刷部数");
        jPanel29.add(jLabel20, new org.netbeans.lib.awtextra.AbsoluteConstraints(786, 4, -1, -1));

        pring_num_.setFont(new java.awt.Font("メイリオ", 0, 18)); // NOI18N
        pring_num_.setMinimumSize(new java.awt.Dimension(60, 28));
        pring_num_.setPreferredSize(new java.awt.Dimension(60, 28));
        jPanel29.add(pring_num_, new org.netbeans.lib.awtextra.AbsoluteConstraints(862, 4, -1, -1));

        産廃パターン1.setFont(new java.awt.Font("メイリオ", 0, 18)); // NOI18N
        産廃パターン1.setText("産廃パターン①");
        産廃パターン1.setMargin(new java.awt.Insets(2, 2, 2, 2));
        産廃パターン1.setMinimumSize(new java.awt.Dimension(160, 30));
        産廃パターン1.setPreferredSize(new java.awt.Dimension(160, 30));
        産廃パターン1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                産廃パターン1ActionPerformed(evt);
            }
        });
        jPanel29.add(産廃パターン1, new org.netbeans.lib.awtextra.AbsoluteConstraints(2, 3, -1, -1));

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.gridwidth = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        jPanel34.add(jPanel29, gridBagConstraints);

        jPanel9.setBackground(new java.awt.Color(0, 153, 204));
        jPanel9.setBorder(javax.swing.BorderFactory.createEtchedBorder(new java.awt.Color(0, 102, 153), null));
        jPanel9.setMaximumSize(new java.awt.Dimension(70, 95));
        jPanel9.setMinimumSize(new java.awt.Dimension(70, 95));
        jPanel9.setPreferredSize(new java.awt.Dimension(70, 95));
        jPanel9.setLayout(new java.awt.GridBagLayout());

        jLabel26.setFont(new java.awt.Font("メイリオ", 1, 14)); // NOI18N
        jLabel26.setForeground(new java.awt.Color(255, 255, 255));
        jLabel26.setText("業");
        jLabel26.setMinimumSize(new java.awt.Dimension(25, 20));
        jLabel26.setPreferredSize(new java.awt.Dimension(25, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        jPanel9.add(jLabel26, gridBagConstraints);

        jLabel28.setFont(new java.awt.Font("メイリオ", 1, 14)); // NOI18N
        jLabel28.setForeground(new java.awt.Color(255, 255, 255));
        jLabel28.setText("場");
        jLabel28.setMinimumSize(new java.awt.Dimension(25, 24));
        jLabel28.setPreferredSize(new java.awt.Dimension(25, 24));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridheight = 2;
        jPanel9.add(jLabel28, gridBagConstraints);

        jLabel29.setFont(new java.awt.Font("メイリオ", 1, 14)); // NOI18N
        jLabel29.setForeground(new java.awt.Color(255, 255, 255));
        jLabel29.setText("事");
        jLabel29.setMinimumSize(new java.awt.Dimension(25, 24));
        jLabel29.setPreferredSize(new java.awt.Dimension(25, 24));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridheight = 2;
        jPanel9.add(jLabel29, gridBagConstraints);

        jLabel30.setFont(new java.awt.Font("メイリオ", 1, 12)); // NOI18N
        jLabel30.setForeground(new java.awt.Color(255, 255, 255));
        jLabel30.setText("業");
        jLabel30.setMinimumSize(new java.awt.Dimension(12, 12));
        jLabel30.setPreferredSize(new java.awt.Dimension(12, 15));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        jPanel9.add(jLabel30, gridBagConstraints);

        jLabel31.setFont(new java.awt.Font("メイリオ", 1, 12)); // NOI18N
        jLabel31.setForeground(new java.awt.Color(255, 255, 255));
        jLabel31.setText("事");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        jPanel9.add(jLabel31, gridBagConstraints);

        jLabel34.setFont(new java.awt.Font("メイリオ", 1, 12)); // NOI18N
        jLabel34.setForeground(new java.awt.Color(255, 255, 255));
        jLabel34.setText("出");
        jLabel34.setMinimumSize(new java.awt.Dimension(12, 12));
        jLabel34.setPreferredSize(new java.awt.Dimension(12, 15));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        jPanel9.add(jLabel34, gridBagConstraints);

        jLabel37.setFont(new java.awt.Font("メイリオ", 1, 12)); // NOI18N
        jLabel37.setForeground(new java.awt.Color(255, 255, 255));
        jLabel37.setText("排");
        jLabel37.setMinimumSize(new java.awt.Dimension(12, 12));
        jLabel37.setPreferredSize(new java.awt.Dimension(12, 15));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        jPanel9.add(jLabel37, gridBagConstraints);

        jLabel58.setFont(new java.awt.Font("メイリオ", 1, 12)); // NOI18N
        jLabel58.setForeground(new java.awt.Color(255, 255, 255));
        jLabel58.setText("場");
        jLabel58.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);
        jLabel58.setMinimumSize(new java.awt.Dimension(12, 15));
        jLabel58.setPreferredSize(new java.awt.Dimension(12, 15));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        jPanel9.add(jLabel58, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        jPanel34.add(jPanel9, gridBagConstraints);

        jPanel8.setBackground(new java.awt.Color(0, 102, 153));
        jPanel8.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel8.setMinimumSize(new java.awt.Dimension(70, 95));
        jPanel8.setPreferredSize(new java.awt.Dimension(70, 95));
        jPanel8.setLayout(new java.awt.GridBagLayout());

        jLabel119.setFont(new java.awt.Font("メイリオ", 1, 14)); // NOI18N
        jLabel119.setForeground(new java.awt.Color(255, 255, 255));
        jLabel119.setText("事");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 3, 0);
        jPanel8.add(jLabel119, gridBagConstraints);

        jLabel121.setFont(new java.awt.Font("メイリオ", 1, 14)); // NOI18N
        jLabel121.setForeground(new java.awt.Color(255, 255, 255));
        jLabel121.setText("排");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 0);
        jPanel8.add(jLabel121, gridBagConstraints);

        jLabel129.setFont(new java.awt.Font("メイリオ", 1, 14)); // NOI18N
        jLabel129.setForeground(new java.awt.Color(255, 255, 255));
        jLabel129.setText("者");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 5, 0);
        jPanel8.add(jLabel129, gridBagConstraints);

        jLabel130.setFont(new java.awt.Font("メイリオ", 1, 14)); // NOI18N
        jLabel130.setForeground(new java.awt.Color(255, 255, 255));
        jLabel130.setText("者");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 0, 0, 0);
        jPanel8.add(jLabel130, gridBagConstraints);

        jLabel131.setFont(new java.awt.Font("メイリオ", 1, 14)); // NOI18N
        jLabel131.setForeground(new java.awt.Color(255, 255, 255));
        jLabel131.setText("業");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        jPanel8.add(jLabel131, gridBagConstraints);

        jLabel132.setFont(new java.awt.Font("メイリオ", 1, 14)); // NOI18N
        jLabel132.setForeground(new java.awt.Color(255, 255, 255));
        jLabel132.setText("出");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 0, 0);
        jPanel8.add(jLabel132, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        jPanel34.add(jPanel8, gridBagConstraints);

        jPanel5.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel5.setMinimumSize(new java.awt.Dimension(70, 30));
        jPanel5.setPreferredSize(new java.awt.Dimension(70, 30));
        jPanel5.setLayout(new java.awt.GridBagLayout());

        jLabel7.setBackground(new java.awt.Color(0, 102, 153));
        jLabel7.setFont(new java.awt.Font("メイリオ", 1, 12)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(255, 255, 255));
        jLabel7.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel7.setText("交付年月日");
        jLabel7.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jLabel7.setMinimumSize(new java.awt.Dimension(70, 35));
        jLabel7.setOpaque(true);
        jLabel7.setPreferredSize(new java.awt.Dimension(70, 35));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        jPanel5.add(jLabel7, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        jPanel34.add(jPanel5, gridBagConstraints);

        jPanel30.setBackground(new java.awt.Color(0, 153, 204));
        jPanel30.setBorder(javax.swing.BorderFactory.createEtchedBorder(new java.awt.Color(0, 102, 153), null));
        jPanel30.setForeground(new java.awt.Color(255, 255, 255));
        jPanel30.setMinimumSize(new java.awt.Dimension(80, 28));
        jPanel30.setPreferredSize(new java.awt.Dimension(80, 20));
        jPanel30.setLayout(new java.awt.GridBagLayout());

        jLabel13.setFont(new java.awt.Font("メイリオ", 0, 12)); // NOI18N
        jLabel13.setForeground(new java.awt.Color(255, 255, 255));
        jLabel13.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel13.setText("<html>処分終了年月日</html>");
        jLabel13.setMinimumSize(new java.awt.Dimension(90, 35));
        jLabel13.setPreferredSize(new java.awt.Dimension(90, 35));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        jPanel30.add(jLabel13, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        jPanel34.add(jPanel30, gridBagConstraints);

        jPanel31.setBackground(new java.awt.Color(0, 153, 204));
        jPanel31.setBorder(javax.swing.BorderFactory.createEtchedBorder(new java.awt.Color(0, 102, 153), null));
        jPanel31.setForeground(new java.awt.Color(255, 255, 255));
        jPanel31.setMinimumSize(new java.awt.Dimension(80, 28));
        jPanel31.setPreferredSize(new java.awt.Dimension(80, 20));
        jPanel31.setLayout(new java.awt.GridBagLayout());

        jLabel12.setFont(new java.awt.Font("メイリオ", 0, 12)); // NOI18N
        jLabel12.setForeground(new java.awt.Color(255, 255, 255));
        jLabel12.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel12.setText("<html>運搬終了年月日</html>");
        jLabel12.setMinimumSize(new java.awt.Dimension(90, 35));
        jLabel12.setPreferredSize(new java.awt.Dimension(80, 35));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        jPanel31.add(jLabel12, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        jPanel34.add(jPanel31, gridBagConstraints);

        jPanel36.setBorder(javax.swing.BorderFactory.createEtchedBorder(new java.awt.Color(0, 102, 153), null));
        jPanel36.setMaximumSize(new java.awt.Dimension(530, 95));
        jPanel36.setMinimumSize(new java.awt.Dimension(530, 95));
        jPanel36.setPreferredSize(new java.awt.Dimension(530, 95));
        jPanel36.setLayout(new java.awt.GridBagLayout());

        排出事業者_print_f.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        排出事業者_print_f.setText("印字なし");
        排出事業者_print_f.setMaximumSize(new java.awt.Dimension(81, 23));
        排出事業者_print_f.setMinimumSize(new java.awt.Dimension(100, 23));
        排出事業者_print_f.setPreferredSize(new java.awt.Dimension(100, 23));
        排出事業者_print_f.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                排出事業者_print_fItemStateChanged(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        jPanel36.add(排出事業者_print_f, gridBagConstraints);

        jLabel14.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        jLabel14.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel14.setText("氏名又は名称");
        jLabel14.setMinimumSize(new java.awt.Dimension(88, 23));
        jLabel14.setPreferredSize(new java.awt.Dimension(88, 23));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 5);
        jPanel36.add(jLabel14, gridBagConstraints);

        jLabel15.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        jLabel15.setText("　電話番号");
        jLabel15.setMinimumSize(new java.awt.Dimension(80, 20));
        jLabel15.setPreferredSize(new java.awt.Dimension(80, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        jPanel36.add(jLabel15, gridBagConstraints);

        ent_zip01.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        ent_zip01.setMinimumSize(new java.awt.Dimension(85, 30));
        ent_zip01.setPreferredSize(new java.awt.Dimension(85, 25));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.insets = new java.awt.Insets(2, 0, 2, 0);
        jPanel36.add(ent_zip01, gridBagConstraints);

        ent_tel01.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        ent_tel01.setMinimumSize(new java.awt.Dimension(210, 30));
        ent_tel01.setPreferredSize(new java.awt.Dimension(210, 25));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 0, 2, 0);
        jPanel36.add(ent_tel01, gridBagConstraints);

        ent_address01.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        ent_address01.setMinimumSize(new java.awt.Dimension(400, 30));
        ent_address01.setPreferredSize(new java.awt.Dimension(400, 25));
        ent_address01.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ent_address01ActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 0, 2, 0);
        jPanel36.add(ent_address01, gridBagConstraints);

        jLabel24.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        jLabel24.setText("住所");
        jLabel24.setMinimumSize(new java.awt.Dimension(30, 20));
        jLabel24.setPreferredSize(new java.awt.Dimension(30, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        jPanel36.add(jLabel24, gridBagConstraints);

        jLabel65.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        jLabel65.setText("ｺｰﾄﾞ");
        jLabel65.setMaximumSize(new java.awt.Dimension(35, 20));
        jLabel65.setMinimumSize(new java.awt.Dimension(35, 20));
        jLabel65.setPreferredSize(new java.awt.Dimension(35, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 2);
        jPanel36.add(jLabel65, gridBagConstraints);

        ent_search_.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        ent_search_.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        ent_search_.setMaximumSize(new java.awt.Dimension(65, 25));
        ent_search_.setMinimumSize(new java.awt.Dimension(65, 25));
        ent_search_.setPreferredSize(new java.awt.Dimension(65, 25));
        ent_search_.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                ent_search_FocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                ent_search_FocusLost(evt);
            }
        });
        ent_search_.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                ent_search_KeyPressed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 5);
        jPanel36.add(ent_search_, gridBagConstraints);

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
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 0, 2, 2);
        jPanel36.add(jButton1, gridBagConstraints);

        ent_name.setFont(new java.awt.Font("メイリオ", 0, 14)); // NOI18N
        ent_name.setMaximumSize(new java.awt.Dimension(250, 25));
        ent_name.setMinimumSize(new java.awt.Dimension(250, 25));
        ent_name.setPreferredSize(new java.awt.Dimension(250, 25));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(2, 0, 2, 0);
        jPanel36.add(ent_name, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        jPanel34.add(jPanel36, gridBagConstraints);

        jScrollPane2.setViewportView(jPanel34);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        add(jScrollPane2, gridBagConstraints);
    }// </editor-fold>//GEN-END:initComponents

    private void grant_monthActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_grant_monthActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_grant_monthActionPerformed

    private void grant_dayActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_grant_dayActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_grant_dayActionPerformed

    private void office_address01ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_office_address01ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_office_address01ActionPerformed

    private void ent_address01ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ent_address01ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_ent_address01ActionPerformed

    private void trans1_date_yearActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_trans1_date_yearActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_trans1_date_yearActionPerformed

    private void quit_btnMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_quit_btnMousePressed
        // TODO add your handling code here:
//        doClose();
    }//GEN-LAST:event_quit_btnMousePressed

    private void quit_btnKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_quit_btnKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_quit_btnKeyPressed

    private void office_id01ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_office_id01ItemStateChanged
        // TODO add your handling code here:
    }//GEN-LAST:event_office_id01ItemStateChanged

    private void trans_cmb01ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_trans_cmb01ItemStateChanged
        // TODO add your handling code here:
        //運搬業者名選択
        trans_zip01.setText((String) transName2Array[2][trans_cmb01.getSelectedIndex()]);
        trans_tel01.setText((String) transName2Array[3][trans_cmb01.getSelectedIndex()]);
        trans_address01.setText((String) transName2Array[4][trans_cmb01.getSelectedIndex()]
                 + (String) transName2Array[5][trans_cmb01.getSelectedIndex()]);
        trans1_sub_name.setText(trans_cmb01.getSelectedItem().toString());
        trans_search_.setText((String) transName2Array[0][trans_cmb01.getSelectedIndex()]);
        int search_id = Integer.valueOf(trans_search_.getText());
        mSQLCommon.GetMasterDataTransTantou("v0_trans_tantou", transTantouArray1, search_id);
        if (transTantouArray1[0].length > 0) {
            String[] TransTantouID1 = new String[transTantouArray1[0].length];
            for (int i = 0; i < transTantouArray1[0].length; i++) {
                TransTantouID1[i] = (String) transTantouArray1[3][i].toString();
            }
            trans_tantou_id_cmb1.setModel(new javax.swing.DefaultComboBoxModel(TransTantouID1));
        }

    }//GEN-LAST:event_trans_cmb01ItemStateChanged

    private void cont_cmb01ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cont_cmb01ItemStateChanged
        cont_zip01.setText((String) contName2Array[2][cont_cmb01.getSelectedIndex()]);
        cont_tel01.setText((String) contName2Array[3][cont_cmb01.getSelectedIndex()]);
        cont_address01.setText((String) contName2Array[4][cont_cmb01.getSelectedIndex()]
                 + (String) contName2Array[5][cont_cmb01.getSelectedIndex()]);
        cont_sub_name.setText(cont_cmb01.getSelectedItem().toString());
        //20151028 add 処分担当者名表示
        cont_sub_name1.setText((String) contName2Array[13][cont_cmb01.getSelectedIndex()]);
        cont_search_.setText((String) contName2Array[0][cont_cmb01.getSelectedIndex()]);
        cont_id_ = Integer.valueOf((String) contName2Array[0][cont_cmb01.getSelectedIndex()]);
        mSQLCommon.GetMasterData4_1("v0_manifesto_3", contOfficeName2Array, cont_id_);
        String[] ContOfficeName = new String[contOfficeName2Array[0].length];
        String[] LastContName = new String[contOfficeName2Array[0].length];
        for (int i = 0; i <= contOfficeName2Array[0].length - 1; i++) {
            ContOfficeName[i] = (String) contOfficeName2Array[1][i];
            LastContName[i] = (String) contOfficeName2Array[1][i] + "　"
                     + (String) contOfficeName2Array[2][i] + (String) contOfficeName2Array[3][i];
            if (cont_office_id_ == Integer.valueOf((String) contOfficeName2Array[0][i])) {
                cont_office_id_index = i;
            }
        }
        cont_office_name01.setModel(new javax.swing.DefaultComboBoxModel(ContOfficeName));
        last_cont_cmb.setModel(new javax.swing.DefaultComboBoxModel(LastContName));
        //運搬先の事業場情報をリセット
        cont_office_zip01.setText("");
        cont_office_tel01.setText("");
        cont_office_address01.setText("");
        if (contOfficeName2Array[0].length > 1 && cont_office_id_ == 0) {
            cont_office_id_ = 1;
        }
        cont_sub_name.setText(cont_cmb01.getSelectedItem().toString());

    }//GEN-LAST:event_cont_cmb01ItemStateChanged

    private void print_btnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_print_btnActionPerformed
        // TODO add your handling code here:
        //印刷ボタン
        //変更内容の登録処理
        update();
        //指定ページ数分ループ
        for (int cnt = 0; cnt < (Integer) pring_num_.getValue(); cnt++) {
            //印刷処理
            new XlsxManifesto(XlsxManifesto.MANIFESTO_REPORT2).print(con_, XlsxManifesto.MANIFESTO_REPORT2, sno_);
        }
    }//GEN-LAST:event_print_btnActionPerformed

    private void nisugata_cmbItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_nisugata_cmbItemStateChanged
        // TODO add your handling code here:
    }//GEN-LAST:event_nisugata_cmbItemStateChanged

    private void 排出事業者_print_fItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_排出事業者_print_fItemStateChanged
        // TODO add your handling code here:
        //排出業者印字F
        ent_name.setEditable(!排出事業者_print_f.isSelected());
        ent_zip01.setEditable(!排出事業者_print_f.isSelected());
        ent_tel01.setEditable(!排出事業者_print_f.isSelected());
        ent_address01.setEditable(!排出事業者_print_f.isSelected());
        ent_name.setEnabled(!排出事業者_print_f.isSelected());
        ent_zip01.setEnabled(!排出事業者_print_f.isSelected());
        ent_tel01.setEnabled(!排出事業者_print_f.isSelected());
        ent_address01.setEnabled(!排出事業者_print_f.isSelected());

    }//GEN-LAST:event_排出事業者_print_fItemStateChanged

    private void 排出事業場_print_fItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_排出事業場_print_fItemStateChanged
        office_name_.setEditable(!排出事業場_print_f.isSelected());
        office_zip01.setEditable(!排出事業場_print_f.isSelected());
        office_tel01.setEditable(!排出事業場_print_f.isSelected());
        office_address01.setEditable(!排出事業場_print_f.isSelected());
        office_sub_name_.setEditable(!排出事業場_print_f.isSelected());
        office_name_.setEnabled(!排出事業場_print_f.isSelected());
        office_zip01.setEnabled(!排出事業場_print_f.isSelected());
        office_tel01.setEnabled(!排出事業場_print_f.isSelected());
        office_address01.setEnabled(!排出事業場_print_f.isSelected());
        office_sub_name_.setEnabled(!排出事業場_print_f.isSelected());

    }//GEN-LAST:event_排出事業場_print_fItemStateChanged

    private void 運搬受託者_print_fItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_運搬受託者_print_fItemStateChanged
        trans_cmb01.setEditable(!運搬受託者_print_f.isSelected());
        trans_zip01.setEditable(!運搬受託者_print_f.isSelected());
        trans_tel01.setEditable(!運搬受託者_print_f.isSelected());
        trans_address01.setEditable(!運搬受託者_print_f.isSelected());
        trans_cmb01.setEnabled(!運搬受託者_print_f.isSelected());
        trans_zip01.setEnabled(!運搬受託者_print_f.isSelected());
        trans_tel01.setEnabled(!運搬受託者_print_f.isSelected());
        trans_address01.setEnabled(!運搬受託者_print_f.isSelected());
    }//GEN-LAST:event_運搬受託者_print_fItemStateChanged

    private void 処分受託者_print_fItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_処分受託者_print_fItemStateChanged
        cont_cmb01.setEditable(!処分受託者_print_f.isSelected());
        cont_zip01.setEditable(!処分受託者_print_f.isSelected());
        cont_tel01.setEditable(!処分受託者_print_f.isSelected());
        cont_address01.setEditable(!処分受託者_print_f.isSelected());
        cont_cmb01.setEnabled(!処分受託者_print_f.isSelected());
        cont_zip01.setEnabled(!処分受託者_print_f.isSelected());
        cont_tel01.setEnabled(!処分受託者_print_f.isSelected());
        cont_address01.setEnabled(!処分受託者_print_f.isSelected());
    }//GEN-LAST:event_処分受託者_print_fItemStateChanged

    private void 積み替え_fItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_積み替え_fItemStateChanged
        cont_office_zip2.setEditable(!積み替え_f.isSelected());
        cont_office_tel2.setEditable(!積み替え_f.isSelected());
        cont_office_address2.setEditable(!積み替え_f.isSelected());
        cont_office_zip2.setEnabled(!積み替え_f.isSelected());
        cont_office_tel2.setEnabled(!積み替え_f.isSelected());
        cont_office_address2.setEnabled(!積み替え_f.isSelected());
    }//GEN-LAST:event_積み替え_fItemStateChanged

    private void regist_btnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_regist_btnActionPerformed
        //登録処理
        if (update()) {
            doClose();
        }
    }//GEN-LAST:event_regist_btnActionPerformed

    private void quit_btnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_quit_btnActionPerformed
        // TODO add your handling code here:
        doClose();
    }//GEN-LAST:event_quit_btnActionPerformed

    private void cont_office_name01ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cont_office_name01ItemStateChanged
        cont_office_zip01.setText((String) contOfficeName2Array[4][cont_office_name01.getSelectedIndex()]);
        cont_office_tel01.setText((String) contOfficeName2Array[5][cont_office_name01.getSelectedIndex()]);
        cont_office_address01.setText((String) contOfficeName2Array[2][cont_office_name01.getSelectedIndex()]
                 + (String) contOfficeName2Array[3][cont_office_name01.getSelectedIndex()]);
    }//GEN-LAST:event_cont_office_name01ItemStateChanged

    private void slash_f2ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_slash_f2ItemStateChanged
        // TODO add your handling code here:
        if (slash_f2.isSelected()) {
            jPanel25.setVisible(false);
            MyLine1.setVisible(true);

        } else {
            jPanel25.setVisible(true);
            MyLine1.setVisible(false);
        }
    }//GEN-LAST:event_slash_f2ItemStateChanged

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

    private void trans1_date_yearKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_trans1_date_yearKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            trans1_date_year.transferFocus();
            evt.consume();
        }
    }//GEN-LAST:event_trans1_date_yearKeyPressed

    private void trans1_date_monthKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_trans1_date_monthKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            trans1_date_month.transferFocus();
            evt.consume();
        }
    }//GEN-LAST:event_trans1_date_monthKeyPressed

    private void trans1_date_dayKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_trans1_date_dayKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            trans1_date_day.transferFocus();
            evt.consume();
        }
    }//GEN-LAST:event_trans1_date_dayKeyPressed

    private void cont_date1_yearKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cont_date1_yearKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            cont_date1_year.transferFocus();
            evt.consume();
        }
    }//GEN-LAST:event_cont_date1_yearKeyPressed

    private void cont_date1_monthKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cont_date1_monthKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            cont_date1_month.transferFocus();
            evt.consume();
        }
    }//GEN-LAST:event_cont_date1_monthKeyPressed

    private void cont_date1_dayKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cont_date1_dayKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            cont_date1_day.transferFocus();
            evt.consume();
        }
    }//GEN-LAST:event_cont_date1_dayKeyPressed

    private void cont_date2_yearKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cont_date2_yearKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            cont_date2_year.transferFocus();
            evt.consume();
        }
    }//GEN-LAST:event_cont_date2_yearKeyPressed

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

    private void office_sub_name_ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_office_sub_name_ActionPerformed
        // TODO add your handling code here:
}//GEN-LAST:event_office_sub_name_ActionPerformed

    private void ent_search_FocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_ent_search_FocusGained
        // TODO add your handling code here:
        ent_search_.selectAll();
    }//GEN-LAST:event_ent_search_FocusGained

    private void ent_search_FocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_ent_search_FocusLost

    }//GEN-LAST:event_ent_search_FocusLost

    private void private_ent_search() {
        try {
            int search_id = Integer.valueOf(ent_search_.getText());
            for (int i = 0; i < entName2Array[0].length - 1; i++) {
                if (search_id == Integer.valueOf((String) entName2Array[0][i])) {
                    ent_name.setText((String) entName2Array[6][i]);
                    ent_zip01.setText((String) entName2Array[2][i]);
                    ent_tel01.setText((String) entName2Array[3][i]);

                    if ((String) entName2Array[5][i] != null) {
                        ent_address01.setText((String) entName2Array[4][i] + (String) entName2Array[5][i]);
                    } else {
                        ent_address01.setText((String) entName2Array[4][i]);
                    }
                    break;
                }
            }
        } catch (Exception e) {
            ent_search_.setText((String) entName2Array[0][ent_id_index]);
        }
    }

    private void office_search() {
        Integer search_id = office_id_;
        Integer array_search_id;
        ent_id_ = Integer.valueOf(ent_search_.getText());
        mSQLCommon.GetMasterDataOffice("v0_office", officeName2Array, ent_id_);
        String[] OfficeID;

        if (officeName2Array[0].length > 0) {
            OfficeID = new String[officeName2Array[0].length];
            for (int i = 0; i < officeName2Array[0].length; i++) {
                if (officeName2Array[0][i] != null) {
                    array_search_id = Integer.valueOf((String) officeName2Array[0][i]);
                    OfficeID[i] = (String) officeName2Array[0][i].toString();
                    if (search_id.equals(array_search_id)) {
                        office_id_index = i;
                    }
                }
            }
            office_id01.setModel(new javax.swing.DefaultComboBoxModel(OfficeID));
            try {
                office_id01.setSelectedIndex(office_id_index);
            } catch (IllegalArgumentException e) {
                office_id01.setSelectedIndex(0);
            }
        } else {
            OfficeID = new String[1];
            OfficeID[0] = "0";
            office_id01.setModel(new javax.swing.DefaultComboBoxModel(OfficeID));
            office_id01.setSelectedIndex(0);
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

    private void jButton1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton1MouseClicked

    }//GEN-LAST:event_jButton1MouseClicked

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        EntSelector selector = new EntSelector(con_);
        switch (selector.showSelector(jPanel37)) {
            case EntSelector.CLEARED:
                ent_search_.setText("0");
                ent_search_.requestFocus();
                break;
            case EntSelector.SELECTED:
                Map<String, Object> item = selector.selectedRow();
                ent_search_.setText(String.valueOf((Integer) item.get("id")));
                private_ent_search();
                ent_search_.requestFocus();
                break;
            default:
            // No operation.
        }
    }//GEN-LAST:event_jButton1ActionPerformed

    private void 交付年月日_print_f_ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_交付年月日_print_f_ItemStateChanged
        // TODO add your handling code here:
        grant1_print_check();
    }//GEN-LAST:event_交付年月日_print_f_ItemStateChanged
    private void grant1_print_check() {
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
        tantou_cmb.setEnabled(!交付担当者_print_f_.isSelected());
    }
    private void office_search_FocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_office_search_FocusGained
        // TODO add your handling code here:
        office_search_.selectAll();
    }//GEN-LAST:event_office_search_FocusGained

    private void office_search_KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_office_search_KeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            trans_search_.requestFocus();
            try {
                int search_id = Integer.valueOf(office_search_.getText());
                for (int i = 0; i < officeName2Array[0].length; i++) {
                    if (search_id == Integer.valueOf((String) officeName2Array[0][i])) {
                        office_id01.setSelectedIndex(i);
                        //20140827 add
                        office_name_.setText((String) officeName2Array[1][i]);
                        office_name_.setToolTipText((String) officeName2Array[1][i]);
                        //////////////////////////////////////////////////////////////
                        office_zip01.setText((String) officeName2Array[2][i]);
                        office_tel01.setText((String) officeName2Array[3][i]);
                        office_address01.setText((String) officeName2Array[4][i] + (String) officeName2Array[5][i]);
                        office_sub_name_.setText("");
                        break;
                    }
                }
            } catch (Exception e) {
                office_search_.setText((String) officeName2Array[0][office_id_index]);
            }
        }
    }//GEN-LAST:event_office_search_KeyPressed

    private void goods_f03ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_goods_f03ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_goods_f03ActionPerformed

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
            for (int i = 0; i < transName2Array[0].length - 1; i++) {
                if (search_id == Integer.valueOf((String) transName2Array[0][i])) {
                    trans_cmb01.setSelectedIndex(i);
                    trans_zip01.setText((String) transName2Array[2][i]);
                    trans_tel01.setText((String) transName2Array[3][i]);
                    trans_address01.setText((String) transName2Array[4][i] + (String) transName2Array[5][i]);
                    break;
                }
            }
        } catch (Exception e) {
            trans_search_.setText((String) transName2Array[0][trans_id_index]);
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
        switch (selector.showSelector(jPanel37)) {
            case TransSelector.CLEARED:
                trans_search_.setText("0");
                trans_search_.requestFocus();
                break;
            case TransSelector.SELECTED:
                Map<String, Object> item = selector.selectedRow();
                trans_search_.setText(String.valueOf((Integer) item.get("id")));
                private_trans_search();
                trans_search_.requestFocus();
                break;
            default:
            // No operation.
        }
    }//GEN-LAST:event_jButton2ActionPerformed

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
            for (int i = 0; i < contName2Array[0].length - 1; i++) {
                if (search_id == Integer.valueOf((String) contName2Array[0][i])) {
                    cont_cmb01.setSelectedIndex(i);
                    cont_zip01.setText((String) contName2Array[2][i]);
                    cont_tel01.setText((String) contName2Array[3][i]);
                    cont_address01.setText((String) contName2Array[4][i] + (String) contName2Array[5][i]);
                    break;
                }
            }
        } catch (Exception e) {
            cont_search_.setText((String) contName2Array[0][cont_id_index]);
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
        switch (selector.showSelector(jPanel37)) {
            case ContSelector.CLEARED:
                cont_search_.setText("0");
                cont_search_.requestFocus();
                break;
            case ContSelector.SELECTED:
                Map<String, Object> item = selector.selectedRow();
                cont_search_.setText(String.valueOf((Integer) item.get("id")));
                private_trans_search();
                cont_search_.requestFocus();
                break;
            default:
            // No operation.
        }
    }//GEN-LAST:event_jButton4ActionPerformed

    private void 画面初期化_btn_MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_画面初期化_btn_MousePressed
        // TODO add your handling code here:
    }//GEN-LAST:event_画面初期化_btn_MousePressed

    private void 画面初期化_btn_ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_画面初期化_btn_ActionPerformed
        // TODO add your handling code here:
        clearView();
    }//GEN-LAST:event_画面初期化_btn_ActionPerformed

    private void 荷姿_print_f_ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_荷姿_print_f_ItemStateChanged
        // TODO add your handling code here:
        nisugata_print_check();
    }//GEN-LAST:event_荷姿_print_f_ItemStateChanged

    private void 形状_print_f_ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_形状_print_f_ItemStateChanged
        // TODO add your handling code here:
        keijyo_print_check();
    }//GEN-LAST:event_形状_print_f_ItemStateChanged

    private void 運搬担当者_print_3ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_運搬担当者_print_3ItemStateChanged
        // TODO add your handling code here:
        trans_print_f3();
    }//GEN-LAST:event_運搬担当者_print_3ItemStateChanged

    private void trans_print_f3() {
        trans1_sub_name.setEnabled(!運搬担当者_print_3.isSelected());
    }

    private void 運搬担当者_print_f4ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_運搬担当者_print_f4ItemStateChanged
        // TODO add your handling code here:
        trans_print_f4();
    }//GEN-LAST:event_運搬担当者_print_f4ItemStateChanged
    private void trans_print_f4() {
        trans_tantou_id_cmb1.setEnabled(!運搬担当者_print_f4.isSelected());
        trans_tantou_search_1.setEnabled(!運搬担当者_print_f4.isSelected());
        trans1_sub_name1.setEnabled(!運搬担当者_print_f4.isSelected());
    }
    private void 処分担当者_print_f2ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_処分担当者_print_f2ItemStateChanged
        cont_print_f2();

    }//GEN-LAST:event_処分担当者_print_f2ItemStateChanged
    private void cont_print_f2() {
        cont_sub_name.setEnabled(!処分担当者_print_f2.isSelected());
    }
    private void 処分担当者_print_f3ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_処分担当者_print_f3ItemStateChanged
        // TODO add your handling code here:
        cont_print_f3();
    }//GEN-LAST:event_処分担当者_print_f3ItemStateChanged

    private void cont_print_f3() {
        cont_sub_name1.setEnabled(!処分担当者_print_f3.isSelected());
    }

    private void net_weight_fieldFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_net_weight_fieldFocusGained
        // TODO add your handling code here:
        net_weight_field.selectAll();
    }//GEN-LAST:event_net_weight_fieldFocusGained

    private void goods_name_fieldFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_goods_name_fieldFocusGained
        // TODO add your handling code here:
        goods_name_field.selectAll();
    }//GEN-LAST:event_goods_name_fieldFocusGained

    private void yugai_fieldFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_yugai_fieldFocusGained
        // TODO add your handling code here:
        yugai_field.selectAll();
    }//GEN-LAST:event_yugai_fieldFocusGained

    private void 単位_print_f_ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_単位_print_f_ItemStateChanged
        // TODO add your handling code here:
        tani_print_check();
    }//GEN-LAST:event_単位_print_f_ItemStateChanged

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
                    cont_office_zip2.setText((String) 保管場所Array[4][i]);
                    cont_office_tel2.setText((String) 保管場所Array[5][i]);
                    cont_office_address2.setText((String) 保管場所Array[2][i] + (String) 保管場所Array[3][i]);
                    break;
                }
            }
        } catch (Exception e) {
            cont_search_.setText((String) contName2Array[0][cont_id_index]);
        }
    }

    private void cont_search_1KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cont_search_1KeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_cont_search_1KeyPressed

    private void jButton5MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton5MouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton5MouseClicked

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton5ActionPerformed

    private void 保管場所_cmbItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_保管場所_cmbItemStateChanged
        // TODO add your handling code here:
        //保管場所選択
        cont_office_zip2.setText((String) 保管場所Array[4][保管場所_cmb.getSelectedIndex()]);
        cont_office_tel2.setText((String) 保管場所Array[5][保管場所_cmb.getSelectedIndex()]);
        cont_office_address2.setText((String) 保管場所Array[2][保管場所_cmb.getSelectedIndex()]
                 + (String) 保管場所Array[3][保管場所_cmb.getSelectedIndex()]);
        cont_search_1.setText((String) 保管場所Array[0][保管場所_cmb.getSelectedIndex()]);
    }//GEN-LAST:event_保管場所_cmbItemStateChanged

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
            for (int i = 0; i < transTantouArray1[2].length; i++) {
                if (((String) transTantouArray1[2][i]).length() > 0) {
                    array_search_id = Integer.valueOf((String) transTantouArray1[2][i]);
                } else {
                    array_search_id = 0;
                }
                if (search_id == array_search_id) {
                    trans_tantou_id_cmb1.setSelectedItem((String) transTantouArray1[3][i]);
                    break;
                }
            }
        } catch (Exception e) {
        }
    }

    private void trans_tantou_search_1KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_trans_tantou_search_1KeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_trans_tantou_search_1KeyPressed

    private void trans_tantou_id_cmb1ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_trans_tantou_id_cmb1ItemStateChanged
        // TODO add your handling code here:
        trans1_sub_name1.setText(trans_tantou_id_cmb1.getSelectedItem().toString());
        if (trans_tantou_id_cmb1.getSelectedIndex() > 0) {
            trans_tantou_search_1.setText((String) transTantouArray1[2][trans_tantou_id_cmb1.getSelectedIndex()]);
        }
    }//GEN-LAST:event_trans_tantou_id_cmb1ItemStateChanged

    private void slash_f3ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_slash_f3ItemStateChanged
        // TODO add your handling code here:
        if (slash_f3.isSelected()) {
            yugai_field.setVisible(false);
            MyLine2.setVisible(true);
        } else {
            yugai_field.setVisible(true);
            MyLine2.setVisible(false);
        }
    }//GEN-LAST:event_slash_f3ItemStateChanged

    private void check_f2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_check_f2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_check_f2ActionPerformed

    private void check_f4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_check_f4ActionPerformed
        // TODO add your handling code here:
        //当欄記載の通りにチェックで『中央公害清掃㈱松山事業所』を表示する
        if (check_f4.isSelected() && !(check_f3.isSelected())) {
            jlbl_final_name_.setVisible(check_f4.isSelected());
        } else {
            jlbl_final_name_.setVisible(false);
        }
    }//GEN-LAST:event_check_f4ActionPerformed

    private void check_f3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_check_f3ActionPerformed
        // TODO add your handling code here:
        //当欄記載の通りにチェックで『中央公害清掃㈱松山事業所』を表示する
        if (check_f4.isSelected() && !(check_f3.isSelected())) {
            jlbl_final_name_.setVisible(check_f4.isSelected());
        } else {
            jlbl_final_name_.setVisible(false);
        }
    }//GEN-LAST:event_check_f3ActionPerformed

    private void office_id01ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_office_id01ActionPerformed

        // TODO add your handling code here:
        //排出事業場名選択
        try {
            Integer search_id = Integer.valueOf(office_id01.getSelectedItem().toString());
            Integer array_search_id;
            for (int i = 0; i < officeName2Array[0].length; i++) {
                array_search_id = Integer.valueOf((String) officeName2Array[0][i]);
                if (search_id.equals(array_search_id)) {
                    office_search_.setText((String) officeName2Array[0][i]);
                    office_name_.setText((String) officeName2Array[1][i]);
                    office_name_.setToolTipText((String) officeName2Array[1][i]);
                    office_zip01.setText((String) officeName2Array[2][i]);
                    office_tel01.setText((String) officeName2Array[3][i]);
                    office_address01.setText((String) officeName2Array[4][i] + (String) officeName2Array[5][i]);
                    office_sub_name_.setText("");

                    //交付担当者
                    if (((String) model_.getColumns(row_).get("grant_name")).length() > 0) {
                        tantou_cmb.setSelectedItem((String) officeName2Array[7][i]);
                    } else if (((String) officeName2Array[7][i]) == null) {
                        tantou_cmb.setSelectedIndex(0);
                    } else if (((String) officeName2Array[7][i]).length() > 0) {
                        tantou_cmb.setSelectedItem((String) officeName2Array[7][i]);
                    } else {
                        tantou_cmb.setSelectedIndex(0);
                    }
                    break;
                }
            }

        } catch (Exception e) {
            tantou_cmb.setSelectedIndex(0);
            office_search_.setText((String) officeName2Array[0][office_id_index]);
        }
    }//GEN-LAST:event_office_id01ActionPerformed

    private void slash_f4ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_slash_f4ItemStateChanged
        // TODO add your handling code here:
        //備考斜線
        if (slash_f3.isSelected()) {
            jScrollPane1.setVisible(false);
            MyLine3.setVisible(true);
        } else {
            jScrollPane1.setVisible(true);
            MyLine3.setVisible(false);
        }
    }//GEN-LAST:event_slash_f4ItemStateChanged

    private void 産廃パターン1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_産廃パターン1ActionPerformed
        // TODO add your handling code here:
        set産廃パターン1();
    }//GEN-LAST:event_産廃パターン1ActionPerformed

    private void tani_print_check() {
        goods_f01.setEnabled(!単位_print_f_.isSelected());
        goods_f02.setEnabled(!単位_print_f_.isSelected());
        goods_f03.setEnabled(!単位_print_f_.isSelected());
        goods_f04.setEnabled(!単位_print_f_.isSelected());
        goods_f05.setEnabled(!単位_print_f_.isSelected());
        goods_f06.setEnabled(!単位_print_f_.isSelected());
        goods_f07.setEnabled(!単位_print_f_.isSelected());
        goods_f08.setEnabled(!単位_print_f_.isSelected());
        goods_f09.setEnabled(!単位_print_f_.isSelected());
        goods_f10.setEnabled(!単位_print_f_.isSelected());
        goods_f11.setEnabled(!単位_print_f_.isSelected());
        goods_f12.setEnabled(!単位_print_f_.isSelected());
        goods_f13.setEnabled(!単位_print_f_.isSelected());
        goods_f14.setEnabled(!単位_print_f_.isSelected());
        goods_f15.setEnabled(!単位_print_f_.isSelected());
        goods_f16.setEnabled(!単位_print_f_.isSelected());
        goods_f17.setEnabled(!単位_print_f_.isSelected());
        goods_f18.setEnabled(!単位_print_f_.isSelected());
        goods_f19.setEnabled(!単位_print_f_.isSelected());
        goods_f20.setEnabled(!単位_print_f_.isSelected());
        goods_f21.setEnabled(!単位_print_f_.isSelected());
        goods_f22.setEnabled(!単位_print_f_.isSelected());
        goods_f23.setEnabled(!単位_print_f_.isSelected());
        goods_f24.setEnabled(!単位_print_f_.isSelected());
        goods_f25.setEnabled(!単位_print_f_.isSelected());
        goods_f26.setEnabled(!単位_print_f_.isSelected());
        goods_f27.setEnabled(!単位_print_f_.isSelected());
        goods_f28.setEnabled(!単位_print_f_.isSelected());
        goods_f29.setEnabled(!単位_print_f_.isSelected());
        goods_f30.setEnabled(!単位_print_f_.isSelected());
        goods_f31.setEnabled(!単位_print_f_.isSelected());
        goods_f32.setEnabled(!単位_print_f_.isSelected());
        goods_f33.setEnabled(!単位_print_f_.isSelected());
        goods_f34.setEnabled(!単位_print_f_.isSelected());
        goods_f35.setEnabled(!単位_print_f_.isSelected());
        goods_f36.setEnabled(!単位_print_f_.isSelected());
        goods_f37.setEnabled(!単位_print_f_.isSelected());
        goods_f38.setEnabled(!単位_print_f_.isSelected());
        goods_f39.setEnabled(!単位_print_f_.isSelected());
        goods_f40.setEnabled(!単位_print_f_.isSelected());

        goods_f42.setEnabled(!単位_print_f_.isSelected());
        goods_f43.setEnabled(!単位_print_f_.isSelected());
        goods_f44.setEnabled(!単位_print_f_.isSelected());
        goods_f45.setEnabled(!単位_print_f_.isSelected());
        goods_f46.setEnabled(!単位_print_f_.isSelected());

    }

    private void keijyo_print_check() {
        yugai_field.setEnabled(!形状_print_f_.isSelected());
        method_cmb.setEnabled(!形状_print_f_.isSelected());
        txt備考.setEnabled(!形状_print_f_.isSelected());
    }

    private void nisugata_print_check() {
        net_weight_field.setEnabled(!荷姿_print_f_.isSelected());
        nisugata_cmb.setEnabled(!荷姿_print_f_.isSelected());
        goods_name_field.setEnabled(!荷姿_print_f_.isSelected());
        単位_cmb.setEnabled(!荷姿_print_f_.isSelected());
    }

    public boolean update() {
        try {
            Statement st = con_.createStatement();
            st.executeUpdate("begin;");
            Locale locale = new Locale("ja", "JP", "JP");
            Calendar calendar = Calendar.getInstance(locale);
            String dateStr = "";
            calendar.setLenient(false);

            try {
                String s = new StringBuilder()
                         .append("update manifesto_data set ")
                         .append("manifesto_no1 = ?, ")
                         .append("grant_date1 = ?, ")
                         .append("ent_print_flag = ?, ")
                         .append("trans1_print_flag = ?, ")
                         .append("trans2_print_flag = ?, ")
                         .append("trans3_print_flag = ?, ")
                         .append("office_print_flag = ?, ")
                         .append("cont_print_flag = ?, ")
                         .append("goods_flag01 = ?, ")
                         .append("goods_flag02 = ?, ")//10
                         .append("goods_flag03 = ?, ")
                         .append("goods_flag04 = ?, ")
                         .append("goods_flag05 = ?, ")
                         .append("goods_flag06 = ?, ")
                         .append("goods_flag07 = ?, ")
                         .append("goods_flag08 = ?, ")
                         .append("goods_flag09 = ?, ")
                         .append("goods_flag10 = ?, ")
                         .append("goods_flag11 = ?, ")
                         .append("goods_flag12 = ?, ")//20
                         .append("goods_flag13 = ?, ")
                         .append("goods_flag14 = ?, ")
                         .append("goods_flag15 = ?, ")
                         .append("goods_flag16 = ?, ")
                         .append("goods_flag17 = ?, ")
                         .append("goods_flag18 = ?, ")
                         .append("goods_flag19 = ?, ")
                         .append("goods_flag20 = ?, ")
                         .append("goods_flag21 = ?, ")
                         .append("goods_flag22 = ?, ")//30
                         .append("goods_flag23 = ?, ")
                         .append("goods_flag24 = ?, ")
                         .append("goods_flag25 = ?, ")
                         .append("goods_flag26 = ?, ")
                         .append("goods_flag27 = ?, ")
                         .append("goods_flag28 = ?, ")
                         .append("goods_flag29 = ?, ")
                         .append("goods_flag30 = ?, ")
                         .append("goods_flag31 = ?, ")
                         .append("goods_flag32 = ?, ")//40
                         .append("goods_flag33 = ?, ")
                         .append("goods_flag34 = ?, ")
                         .append("goods_flag35 = ?, ")
                         .append("goods_flag36 = ?, ")
                         .append("goods_flag37 = ?, ")
                         .append("goods_flag38 = ?, ")
                         .append("goods_flag39 = ?, ")
                         .append("goods_flag40 = ?, ")
                         .append("slash_flag01 = ?, ")//斜線有り(中間処理)
                         .append("slash_flag02 = ?, ")//50斜線有り(運搬受託２)
                         .append("slash_flag03 = ?, ")//斜線有り(運搬受託SUB)
                         .append("check_flag01 = ?, ")//中間：１
                         .append("check_flag02 = ?, ")//中間：２
                         .append("check_flag03 = ?, ")//最終：１
                         .append("check_flag04 = ?, ")//最終：２
                         .append("check_flag05 = ?, ")//運搬受託１積み替え有り
                         .append("check_flag06 = ?, ")//積替え無し
                         .append("check_flag07 = ?, ")//運搬受託２積み替え有り
                         .append("check_flag08 = ?, ")//積替え無し
                         .append("check_flag09 = ?, ")//60//運搬先の処分方法：１（脱水）
                         .append("check_flag10 = ?, ")//運搬先の処分方法：１（焼却）
                         .append("check_flag11 = ?, ")//運搬先の処分方法：１（破砕）
                         .append("check_flag12 = ?, ")//運搬先の処分方法：１（安定型）
                         .append("check_flag13 = ?, ")//運搬先の処分方法：１（管理型）
                         .append("check_flag14 = ?, ")//運搬先の処分方法：１（遮断型）
                         .append("check_flag15 = ?, ")//有価物収集：有り
                         .append("check_flag16 = ?, ")//有価物収集：無し
                         .append("check_flag17 = ?, ")//実績数量：t
                         .append("check_flag18 = ?, ")//実績数量：m3
                         .append("trans1_sub_name = ?, ")//70　運搬の受託　担当者
                         .append("trans1_date = ?, ")
                         .append("trans1_weight = ?, ")
                         .append("trans2_sub_name = ?, ")
                         .append("trans2_date = ?, ")
                         .append("trans2_weight = ?, ")
                         .append("trans3_sub_name = ?, ")//印刷する廃棄物名に代用
                         .append("trans3_date = ?, ")
                         .append("trans3_weight = ?, ")
                         .append("cont_sub_name1 = ?, ")//処分の受託　担当者
                         .append("cont_date2 = ?, ")//80
                         .append("disposal_date2 = ?, ")
                         .append("dis_ent_id = ?, ")
                         .append("trans1_id = ?, ")
                         .append("trans2_id = ?, ")
                         .append("trans3_id = ?, ")
                         .append("dis_office_id = ?, ")
                         .append("dis_cont_id = ?, ")
                         .append("manifesto3_id = ?, ")
                         .append("grant_name = ?, ")//交付担当者（マニフェストマスタ参照）
                         .append("remark_name = ?, ")//90//備考欄（マニフェストマスタ参照）
                         .append("cont_office_sub_name1 = ?, ")//処分受託１会社名
                         .append("last_office_name = ?, ")//最終処分を行った場所（マニフェストマスタ参照）
                         //yugai_field
                         .append("yugai_name = ?, ")
                         .append("method_name = ?, ")
                         .append("nisugata_name = ?, ")
                         .append("printer_print_order = ?, ")
                         .append("cont_name02 = ?, ")
                         .append("cont_no02 = ?, ")
                         .append("last_cont_name02 = ?, ")
                         .append("last_cont_tel02 = ?, ")//100
                         .append("last_cont_zip02 = ?, ")
                         .append("last_cont_address02 = ?, ")
                         .append("cont_date1 = ?, trans_weight = ?, trans_net_weight = ?, ")
                         .append("slash_flag04 = ?, ")
                         .append("office_zip = ?, ")//現場郵便番号
                         .append("office_tel = ?, ")//現場電話
                         .append("office_address = ?, ")//現場住所
                         .append("grant2_print_f = ?, ")//110
                         .append("tantou_print_f = ?, ")
                         .append("tani_print_f = ?, ")
                         .append("keijyo_print_f = ?, ")
                         .append("nisugata_print_f = ?, ")
                         .append("trans_sub1_print_flag = ?, ")//運搬受託１印字フラグ
                         .append("trans_sub2_print_flag = ?, ")//運搬受託２印字フラグ
                         .append("cont_sub1_print_flag = ?, ")//処分受託１印字フラグ
                         .append("cont_sub2_print_flag = ?, ")//処分受託２印字フラグ
                         .append("trans_office_sub_name1 = ?, ")//運搬受託１会社名
                         //                        .append("cont_office_sub_name = ? ")//処分受託１会社名
                         .append("edit_ent_zip = ?, ")//120
                         .append("edit_ent_name = ?, ")
                         .append("edit_ent_address = ?, ")
                         .append("edit_ent_tel = ?, ")
                         //tsumikae_print_flag 
                         .append("tsumikae_print_flag = ?, ")
                         //                        .append("goods_flag41 = ?, ")//直行型は使用せず
                         .append("goods_flag42 = ?, ")
                         .append("goods_flag43 = ?, ")
                         .append("goods_flag44 = ?, ")
                         .append("goods_flag45 = ?, ")
                         .append("goods_flag46 = ?  ")
                         .append("where weight_sno = ?;").toString();//130
                PreparedStatement ps = con_.prepareStatement(s);
                try {
                    ps.setObject(1, manifesto_no1.getText());
                    //交付日チェック 元号使用止め西暦表示、入力に変更　180918
                    if (!grant_year.getText().equals("") && !grant_month.getText().equals("") && !grant_day.getText().equals("")) {
                        dateStr = grant_year.getText() + "/" + grant_month.getText() + "/" + grant_day.getText();
                    } else {
                        dateStr = null;
                    }
                    ps.setObject(2, DBManager.convert(DBManager.convert(dateStr)));

                    ps.setObject(3, 排出事業者_print_f.isSelected());
                    ps.setObject(4, 運搬受託者_print_f.isSelected());
                    ps.setObject(5, false);
                    ps.setObject(6, false);
                    ps.setObject(7, 排出事業場_print_f.isSelected());
                    ps.setObject(8, 処分受託者_print_f.isSelected());

                    ps.setObject(9, goods_f01.isSelected());
                    ps.setObject(10, goods_f02.isSelected());
                    ps.setObject(11, goods_f03.isSelected());
                    ps.setObject(12, goods_f04.isSelected());
                    ps.setObject(13, goods_f05.isSelected());
                    ps.setObject(14, goods_f06.isSelected());
                    ps.setObject(15, goods_f07.isSelected());
                    ps.setObject(16, goods_f08.isSelected());
                    ps.setObject(17, goods_f09.isSelected());
                    ps.setObject(18, goods_f10.isSelected());
                    ps.setObject(19, goods_f11.isSelected());
                    ps.setObject(20, goods_f12.isSelected());
                    ps.setObject(21, goods_f13.isSelected());
                    ps.setObject(22, goods_f14.isSelected());
                    ps.setObject(23, goods_f15.isSelected());
                    ps.setObject(24, goods_f16.isSelected());
                    ps.setObject(25, goods_f17.isSelected());
                    ps.setObject(26, goods_f18.isSelected());
                    ps.setObject(27, goods_f19.isSelected());
                    ps.setObject(28, goods_f20.isSelected());
                    ps.setObject(29, goods_f21.isSelected());
                    ps.setObject(30, goods_f22.isSelected());
                    ps.setObject(31, goods_f23.isSelected());
                    ps.setObject(32, goods_f24.isSelected());
                    ps.setObject(33, goods_f25.isSelected());
                    ps.setObject(34, goods_f26.isSelected());
                    ps.setObject(35, goods_f27.isSelected());
                    ps.setObject(36, goods_f28.isSelected());
                    ps.setObject(37, goods_f29.isSelected());
                    ps.setObject(38, goods_f30.isSelected());
                    ps.setObject(39, goods_f31.isSelected());
                    ps.setObject(40, goods_f32.isSelected());
                    ps.setObject(41, goods_f33.isSelected());
                    ps.setObject(42, goods_f34.isSelected());
                    ps.setObject(43, goods_f35.isSelected());
                    ps.setObject(44, goods_f36.isSelected());
                    ps.setObject(45, goods_f37.isSelected());
                    ps.setObject(46, goods_f38.isSelected());
                    ps.setObject(47, goods_f39.isSelected());
                    ps.setObject(48, goods_f40.isSelected());

                    ///////////////////////////////////////////////////////////////
                    ps.setObject(49, slash_f1.isSelected());
                    ps.setObject(50, slash_f2.isSelected());
                    //20120629 有害物質等斜線有り
                    ps.setObject(51, slash_f3.isSelected());
                    ps.setObject(52, check_f1.isSelected());
                    ps.setObject(53, check_f2.isSelected());
                    ps.setObject(54, check_f3.isSelected());
                    ps.setObject(55, check_f4.isSelected());
                    ps.setObject(56, false);
                    ps.setObject(57, false);
                    ps.setObject(58, false);
                    ps.setObject(59, false);
                    ps.setObject(60, false);
                    ps.setObject(61, false);
                    ps.setObject(62, false);
                    ps.setObject(63, false);
                    ps.setObject(64, false);
                    ps.setObject(65, false);
                    ps.setObject(66, false);
                    ps.setObject(67, false);
                    ps.setObject(68, false);
                    ps.setObject(69, false);

                    ///////////////////////////////////////////////////////////////
                    ps.setObject(70, trans1_sub_name1.getText());
                    //運搬終了チェック 元号使用止め西暦表示、入力に変更　180918
                    if (!trans1_date_year.getText().equals("") && !trans1_date_month.getText().equals("") && !trans1_date_day.getText().equals("")) {
                        dateStr = trans1_date_year.getText() + "/" + trans1_date_month.getText() + "/" + trans1_date_day.getText();
                    } else {
                        dateStr = null;
                    }
                    ps.setObject(71, DBManager.convert(DBManager.convert(dateStr)));

                    Object o = trans1_weight.getText().replace("kg", "").replace("t", "").replace("ton", "");
                    if (o instanceof Number) {
                        ps.setObject(72, Integer.valueOf(trans1_weight.getText().replace("kg", "").replace("t", "").replace("ton", "")));
                    } else {
                        ps.setObject(72, 0);
                    }
                    ps.setObject(73, "");
                    ps.setObject(74, null);
                    ps.setObject(75, 0);
                    ps.setObject(76, goods_name_field.getText());//印刷する廃棄物名に代用
                    ps.setObject(77, null);
                    ps.setObject(78, 0);
                    ps.setObject(79, cont_sub_name1.getText());

                    //運搬終了チェック  元号使用止め西暦表示、入力に変更　180918
                    if (!cont_date1_year.getText().equals("") && !cont_date1_month.getText().equals("") && !cont_date1_day.getText().equals("")) {
                        dateStr = cont_date1_year.getText() + "/" + cont_date1_month.getText() + "/" + cont_date1_day.getText();
                    } else {
                        dateStr = null;
                    }
                    ps.setObject(80, DBManager.convert(DBManager.convert(dateStr)));

                    //運搬終了チェック  元号使用止め西暦表示、入力に変更　180918
                    if (!cont_date2_year.getText().equals("") && !cont_date2_month.getText().equals("") && !cont_date2_day.getText().equals("")) {
                        dateStr = cont_date2_year.getText() + "/" + cont_date2_month.getText() + "/" + cont_date2_day.getText();
                    } else {
                        dateStr = null;
                    }
                    ps.setObject(81, DBManager.convert(DBManager.convert(dateStr)));
                    ps.setObject(82, Integer.valueOf((String) ent_search_.getText()));
                    ps.setObject(83, Integer.valueOf((String) transName2Array[0][trans_cmb01.getSelectedIndex()]));
                    ps.setObject(84, 0);
                    ps.setObject(85, 0);
                    if (officeName2Array[0].length > 0) {
                        ps.setObject(86, Integer.valueOf((String) officeName2Array[0][office_id01.getSelectedIndex()]));
                    } else {
                        ps.setObject(86, 0);
                    }
                    ps.setObject(87, Integer.valueOf((String) contName2Array[0][cont_cmb01.getSelectedIndex()]));
                    ps.setObject(88, Integer.valueOf((String) contOfficeName2Array[0][cont_office_name01.getSelectedIndex()]));
                    ps.setObject(89, tantou_cmb.getSelectedItem().toString());

                    //bikou_cmb
                    ps.setObject(90, txt備考.getText());
                    ps.setObject(91, cont_sub_name.getText());
                    ps.setObject(92, last_cont_cmb.getSelectedItem().toString());
                    ps.setObject(93, yugai_field.getText());
                    ps.setObject(94, method_cmb.getSelectedItem().toString());
                    ps.setObject(95, nisugata_cmb.getSelectedItem().toString());
                    ps.setObject(96, 0);
                    //cont_name
                    ps.setObject(97, cont_name.getText());
                    //cont_no
                    ps.setObject(98, cont_no.getText());
                    //保管場所_cmb
                    ps.setObject(99, 保管場所_cmb.getSelectedItem().toString());
                    //cont_office_tel2
                    ps.setObject(100, cont_office_tel2.getText());
                    //cont_office_zip2
                    ps.setObject(101, cont_office_zip2.getText());
                    //last_cont_adress
                    ps.setObject(102, cont_office_address2.getText());

                    //処分チェック  元号使用止め西暦表示、入力に変更　180918
                    if (!cont_date1_year.getText().equals("") && !cont_date1_month.getText().equals("") && !cont_date1_day.getText().equals("")) {
                        dateStr = cont_date1_year.getText() + "/" + cont_date1_month.getText() + "/" + cont_date1_day.getText();
                    } else {
                        dateStr = null;
                    }
                    ps.setObject(103, DBManager.convert(DBManager.convert(dateStr)));

                    ps.setObject(104, trans1_weight.getText());
                    ps.setObject(105, net_weight_field.getText() + 単位_cmb.getSelectedItem());
//                    //最終処分当欄記載チェックで斜線を引く
                    ps.setObject(106, check_f4.isSelected());
                    ps.setObject(107, office_zip01.getText());
                    ps.setObject(108, office_tel01.getText());
                    ps.setObject(109, office_address01.getText());
                    ps.setObject(110, 交付年月日_print_f_.isSelected());
                    ps.setObject(111, 交付担当者_print_f_.isSelected());
                    ps.setObject(112, 単位_print_f_.isSelected());
                    ps.setObject(113, 形状_print_f_.isSelected());
                    ps.setObject(114, 荷姿_print_f_.isSelected());
                    ps.setObject(115, 運搬担当者_print_3.isSelected());
                    ps.setObject(116, 運搬担当者_print_f4.isSelected());
                    ps.setObject(117, 処分担当者_print_f2.isSelected());
                    ps.setObject(118, 処分担当者_print_f3.isSelected());
                    ps.setObject(119, trans1_sub_name.getText());
                    ps.setObject(120, ent_zip01.getText());
                    ps.setObject(121, ent_name.getText());
                    ps.setObject(122, ent_address01.getText());
                    ps.setObject(123, ent_tel01.getText());
                    ps.setObject(124, 積み替え_f.isSelected());
                    ps.setObject(125, goods_f42.isSelected());
                    ps.setObject(126, goods_f43.isSelected());
                    ps.setObject(127, goods_f44.isSelected());
                    ps.setObject(128, goods_f45.isSelected());
                    ps.setObject(129, goods_f46.isSelected());
                    ps.setObject(130, sno_);
                    ps.executeUpdate();
                } finally {
                    ps.close();
                }

                s = new StringBuilder()
                         .append("update weigh_data set ")
                         .append("dis_ent_id = ?, ")//排出業者ID
                         .append("trans_id = ?, ")//運搬業者ID
                         .append("dis_office_id = ?, ")//排出事業場ID
                         .append("dis_cont_id = ?, ")//処分業者ID
                         .append("entrepreneur_name = ?, ")//排出業者
                         .append("transporter_name = ?, ")//運搬業者
                         .append("office_name = ?, ")//排出事業場
                         .append("contractor_name = ?, ")//処分業者
                         .append("office_sub_name = ? ")//現場名補完分
                         .append("where sno = ?;").toString();//10
                ps = con_.prepareStatement(s);
                try {
                    ps.setObject(1, Integer.valueOf((String) ent_search_.getText()));
                    ps.setObject(2, Integer.valueOf(transName2Array[0][trans_cmb01.getSelectedIndex()]));
                    ps.setObject(3, Integer.valueOf(officeName2Array[0][office_id01.getSelectedIndex()]));
                    ps.setObject(4, Integer.valueOf(contName2Array[0][cont_cmb01.getSelectedIndex()]));
                    ps.setObject(5, ent_name.getText());
                    ps.setObject(6, trans_cmb01.getSelectedItem().toString());
                    ps.setObject(7, office_name_.getText());
                    ps.setObject(8, cont_cmb01.getSelectedItem().toString());
                    ps.setObject(9, office_sub_name_.getText());
                    ps.setObject(10, sno_);

                    ps.executeUpdate();
                } finally {
                    ps.close();
                }

                st.executeUpdate("commit;");
            } catch (SQLException e) {
                st.executeUpdate("rollback;");
                throw e;
            } finally {
                st.close();
            }
        } catch (Exception e) {

            return false;
        }

        return true;
    }

    private void clearView() {

        ent_name.setText("");
        ent_search_.setText("0");
        ent_zip01.setText("");
        ent_tel01.setText("");
        ent_address01.setText("");
        office_id01.setSelectedIndex(0);
        office_name_.setText("");
        office_search_.setText("0");
        office_zip01.setText("");
        office_tel01.setText("");
        office_address01.setText("");
        office_sub_name_.setText("");
        trans_cmb01.setSelectedIndex(0);
        trans_search_.setText("0");
        trans_zip01.setText("");
        trans_tel01.setText("");
        trans_address01.setText("");
        cont_cmb01.setSelectedIndex(0);
        cont_search_.setText("0");
        cont_zip01.setText("");
        cont_tel01.setText("");
        cont_address01.setText("");
        cont_office_name01.setSelectedIndex(0);
        cont_office_zip01.setText("");
        cont_office_tel01.setText("");
        cont_office_address01.setText("");
        method_cmb.setSelectedIndex(0);
        cont_name.setText("");
        cont_no.setText("");
        last_cont_name.setText("");
        last_cont_tel.setText("");
        last_cont_zip.setText("");
        last_cont_adress.setText("");
        運搬担当者_print_3.setSelected(false);//運搬担当者　社名
        trans1_sub_name.setText("");
        処分担当者_print_f2.setSelected(false);//処分担当者　社名
        cont_sub_name.setText("");
        運搬担当者_print_f4.setSelected(false);//運搬担当者
        trans_tantou_search_1.setText("0");
        trans_tantou_id_cmb1.setSelectedIndex(0);
        trans1_sub_name1.setText("");
        処分担当者_print_f3.setSelected(false);//処分担当者
        cont_sub_name1.setText("");
        保管場所_cmb.setSelectedIndex(0);
        grant_year.setText("");
        grant_month.setText("");
        grant_day.setText("");
        tantou_cmb.setSelectedIndex(0);
        net_weight_field.setText("");
        goods_name_field.setText("");
        yugai_field.setText("");
        txt備考.setText("");
        tantou_cmb.setSelectedIndex(0);
        nisugata_cmb.setSelectedIndex(0);
        trans1_date_year.setText("");
        trans1_date_month.setText("");
        trans1_date_day.setText("");
        cont_date1_year.setText("");
        cont_date1_month.setText("");
        cont_date1_day.setText("");
        cont_date2_year.setText("");
        cont_date2_month.setText("");
        cont_date2_day.setText("");
        trans_tantou_search_1.setText("0");
        check_f3.setSelected(false);
        check_f4.setSelected(false);
    }

    private void 印字初期値設定() {
        交付年月日_print_f_.setSelected(true);
        交付担当者_print_f_.setSelected(true);
        排出事業者_print_f.setSelected(true);
        排出事業場_print_f.setSelected(true);
        単位_print_f_.setSelected(false);
        荷姿_print_f_.setSelected(false);
        形状_print_f_.setSelected(false);
        運搬受託者_print_f.setSelected(true);
        処分受託者_print_f.setSelected(false);
        積み替え_f.setSelected(false);
        運搬担当者_print_3.setSelected(true);
        運搬担当者_print_f4.setSelected(true);
        処分担当者_print_f2.setSelected(false);
        処分担当者_print_f3.setSelected(false);
        slash_f1.setSelected(true);
        slash_f2.setSelected(true);
    }

//    private void 印字初期値設定() 
//        交付年月日_print_f_.setSelected(false);
//        交付担当者_print_f_.setSelected(false);
//        排出事業者_print_f.setSelected(false);
//        排出事業場_print_f.setSelected(false);
//        単位_print_f_.setSelected(false);
//        荷姿_print_f_.setSelected(false);
//        形状_print_f_.setSelected(false);
//        運搬受託者_print_f.setSelected(false);
//        処分受託者_print_f.setSelected(false);
//        積み替え_f.setSelected(false);
//        運搬担当者_print_3.setSelected(false);
//        運搬担当者_print_f4.setSelected(false);
//        処分担当者_print_f2.setSelected(false);
//        処分担当者_print_f3.setSelected(false);
//        slash_f1.setSelected(false);
//    }
    private void set産廃パターン1() {
        cont_date1_year.setEnabled(false);
        cont_date1_year.setText("");
        cont_date1_month.setEnabled(false);
        cont_date1_day.setText("");
        cont_date1_day.setEnabled(false);
        cont_date1_month.setText("");
        cont_date2_year.setEnabled(false);
        cont_date2_year.setText("");
        cont_date2_month.setEnabled(false);
        cont_date2_month.setText("");
        cont_date2_day.setEnabled(false);
        cont_date2_day.setText("");
        処分受託者_print_f.setSelected(true);
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JCheckBox check_f1;
    private javax.swing.JCheckBox check_f2;
    private javax.swing.JCheckBox check_f3;
    private javax.swing.JCheckBox check_f4;
    private javax.swing.JTextField cont_address01;
    private javax.swing.JComboBox cont_cmb01;
    private javax.swing.JTextField cont_date1_day;
    private javax.swing.JTextField cont_date1_month;
    private javax.swing.JTextField cont_date1_year;
    private javax.swing.JTextField cont_date2_day;
    private javax.swing.JTextField cont_date2_month;
    private javax.swing.JTextField cont_date2_year;
    private javax.swing.JTextField cont_name;
    private javax.swing.JTextField cont_no;
    private javax.swing.JTextField cont_office_address01;
    private javax.swing.JTextField cont_office_address2;
    private javax.swing.JComboBox cont_office_name01;
    private javax.swing.JTextField cont_office_tel01;
    private javax.swing.JTextField cont_office_tel2;
    private javax.swing.JTextField cont_office_zip01;
    private javax.swing.JTextField cont_office_zip2;
    private javax.swing.JTextField cont_search_;
    private javax.swing.JTextField cont_search_1;
    private javax.swing.JTextField cont_sub_name;
    private javax.swing.JTextField cont_sub_name1;
    private javax.swing.JTextField cont_tel01;
    private javax.swing.JTextField cont_zip01;
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
    private javax.swing.JCheckBox goods_f42;
    private javax.swing.JCheckBox goods_f43;
    private javax.swing.JCheckBox goods_f44;
    private javax.swing.JCheckBox goods_f45;
    private javax.swing.JCheckBox goods_f46;
    private javax.swing.JTextField goods_name_field;
    private javax.swing.JTextField grant_day;
    private javax.swing.JTextField grant_month;
    private javax.swing.JTextField grant_year;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JCheckBox jCheckBox58;
    private javax.swing.JCheckBox jCheckBox59;
    private javax.swing.JCheckBox jCheckBox60;
    private javax.swing.JCheckBox jCheckBox62;
    private javax.swing.JCheckBox jCheckBox63;
    private javax.swing.JCheckBox jCheckBox78;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel104;
    private javax.swing.JLabel jLabel106;
    private javax.swing.JLabel jLabel107;
    private javax.swing.JLabel jLabel108;
    private javax.swing.JLabel jLabel109;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel117;
    private javax.swing.JLabel jLabel119;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel121;
    private javax.swing.JLabel jLabel122;
    private javax.swing.JLabel jLabel123;
    private javax.swing.JLabel jLabel125;
    private javax.swing.JLabel jLabel127;
    private javax.swing.JLabel jLabel128;
    private javax.swing.JLabel jLabel129;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel130;
    private javax.swing.JLabel jLabel131;
    private javax.swing.JLabel jLabel132;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel29;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel30;
    private javax.swing.JLabel jLabel31;
    private javax.swing.JLabel jLabel32;
    private javax.swing.JLabel jLabel33;
    private javax.swing.JLabel jLabel34;
    private javax.swing.JLabel jLabel35;
    private javax.swing.JLabel jLabel36;
    private javax.swing.JLabel jLabel37;
    private javax.swing.JLabel jLabel39;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel40;
    private javax.swing.JLabel jLabel42;
    private javax.swing.JLabel jLabel44;
    private javax.swing.JLabel jLabel46;
    private javax.swing.JLabel jLabel47;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel52;
    private javax.swing.JLabel jLabel54;
    private javax.swing.JLabel jLabel56;
    private javax.swing.JLabel jLabel57;
    private javax.swing.JLabel jLabel58;
    private javax.swing.JLabel jLabel59;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel62;
    private javax.swing.JLabel jLabel63;
    private javax.swing.JLabel jLabel64;
    private javax.swing.JLabel jLabel65;
    private javax.swing.JLabel jLabel68;
    private javax.swing.JLabel jLabel69;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel70;
    private javax.swing.JLabel jLabel71;
    private javax.swing.JLabel jLabel72;
    private javax.swing.JLabel jLabel75;
    private javax.swing.JLabel jLabel76;
    private javax.swing.JLabel jLabel77;
    private javax.swing.JLabel jLabel78;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel80;
    private javax.swing.JLabel jLabel81;
    private javax.swing.JLabel jLabel82;
    private javax.swing.JLabel jLabel83;
    private javax.swing.JLabel jLabel84;
    private javax.swing.JLabel jLabel85;
    private javax.swing.JLabel jLabel86;
    private javax.swing.JLabel jLabel87;
    private javax.swing.JLabel jLabel89;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JLabel jLabel90;
    private javax.swing.JLabel jLabel91;
    private javax.swing.JLabel jLabel92;
    private javax.swing.JLabel jLabel93;
    private javax.swing.JLabel jLabel94;
    private javax.swing.JLabel jLabel95;
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
    private javax.swing.JPanel jPanel32;
    private javax.swing.JPanel jPanel33;
    private javax.swing.JPanel jPanel34;
    private javax.swing.JPanel jPanel35;
    private javax.swing.JPanel jPanel36;
    private javax.swing.JPanel jPanel37;
    private javax.swing.JPanel jPanel38;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel40;
    private javax.swing.JPanel jPanel41;
    private javax.swing.JPanel jPanel42;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JLabel jlbl_final_name_;
    private java.awt.Label label1;
    private javax.swing.JTextField last_cont_adress;
    private javax.swing.JComboBox last_cont_cmb;
    private javax.swing.JTextField last_cont_name;
    private javax.swing.JTextField last_cont_tel;
    private javax.swing.JTextField last_cont_zip;
    private javax.swing.JTextField manifesto_no1;
    private java.awt.Menu menu1;
    private java.awt.Menu menu2;
    private java.awt.Menu menu3;
    private java.awt.Menu menu4;
    private java.awt.MenuBar menuBar1;
    private java.awt.MenuBar menuBar2;
    private javax.swing.JComboBox method_cmb;
    private javax.swing.JTextField net_weight_field;
    private javax.swing.JComboBox nisugata_cmb;
    private javax.swing.JTextField office_address01;
    private javax.swing.JComboBox office_id01;
    private javax.swing.JTextField office_name_;
    private javax.swing.JTextField office_search_;
    private javax.swing.JTextField office_sub_name_;
    private javax.swing.JTextField office_tel01;
    private javax.swing.JTextField office_zip01;
    private javax.swing.JSpinner pring_num_;
    private javax.swing.JButton print_btn;
    private javax.swing.JButton quit_btn;
    private javax.swing.JButton regist_btn;
    private javax.swing.JCheckBox slash_f1;
    private javax.swing.JCheckBox slash_f2;
    private javax.swing.JCheckBox slash_f3;
    private javax.swing.JCheckBox slash_f4;
    private javax.swing.JComboBox tantou_cmb;
    private javax.swing.JTextField trans1_date_day;
    private javax.swing.JTextField trans1_date_month;
    private javax.swing.JTextField trans1_date_year;
    private javax.swing.JTextField trans1_sub_name;
    private javax.swing.JTextField trans1_sub_name1;
    private javax.swing.JTextField trans1_sub_name2;
    private javax.swing.JTextField trans1_weight;
    private javax.swing.JTextField trans_address01;
    private javax.swing.JComboBox trans_cmb01;
    private javax.swing.JTextField trans_search_;
    private javax.swing.JComboBox trans_tantou_id_cmb1;
    private javax.swing.JTextField trans_tantou_search_1;
    private javax.swing.JTextField trans_tel01;
    private javax.swing.JTextField trans_zip01;
    private javax.swing.JTextArea txt備考;
    private javax.swing.JTextField yugai_field;
    private javax.swing.JCheckBox 交付年月日_print_f_;
    private javax.swing.JCheckBox 交付担当者_print_f_;
    private javax.swing.JComboBox 保管場所_cmb;
    private javax.swing.JCheckBox 処分受託者_print_f;
    private javax.swing.JCheckBox 処分担当者_print_f2;
    private javax.swing.JCheckBox 処分担当者_print_f3;
    private javax.swing.JComboBox 単位_cmb;
    private javax.swing.JCheckBox 単位_print_f_;
    private javax.swing.JCheckBox 形状_print_f_;
    private javax.swing.JCheckBox 排出事業場_print_f;
    private javax.swing.JCheckBox 排出事業者_print_f;
    private javax.swing.JButton 産廃パターン1;
    private javax.swing.JButton 画面初期化_btn_;
    private javax.swing.JCheckBox 積み替え_f;
    private javax.swing.JCheckBox 荷姿_print_f_;
    private javax.swing.JCheckBox 運搬受託者_print_f;
    private javax.swing.JCheckBox 運搬担当者_print_3;
    private javax.swing.JCheckBox 運搬担当者_print_f4;
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

    private void check_goods_flag(Boolean f按分フラグ) {
        if (!f按分フラグ) {
            //ごみの種類によってチェックボックスを選択する
            switchLabel01:
            switch (methot_id_) {
                case 100:
                    goods_f03.setSelected(true);
                    goods_f01.setSelected(true);
                    break;
                case 200:
                    goods_f04.setSelected(true);
                    goods_f01.setSelected(true);
                    break;
                case 300:
                    goods_f05.setSelected(true);
                    goods_f01.setSelected(true);
                    break;
                case 400:
                    goods_f06.setSelected(true);
                    goods_f01.setSelected(true);
                    break;
                case 500:
                    goods_f07.setSelected(true);
                    goods_f01.setSelected(true);
                    break;
                case 600:
                    goods_f08.setSelected(true);
                    goods_f01.setSelected(true);
                    break;
                case 700:
                    goods_f09.setSelected(true);
                    goods_f01.setSelected(true);
                    break;
                case 800:
                    goods_f10.setSelected(true);
                    goods_f01.setSelected(true);
                    break;
                case 900:
                    goods_f11.setSelected(true);
                    goods_f01.setSelected(true);
                    break;
                case 1000:
                    goods_f12.setSelected(true);
                    goods_f01.setSelected(true);
                    break;
                case 1100:
                    goods_f13.setSelected(true);
                    goods_f01.setSelected(true);
                    break;
                case 1200:
                    goods_f14.setSelected(true);
                    goods_f01.setSelected(true);
                    break;
                case 1300:
                    goods_f15.setSelected(true);
                    goods_f01.setSelected(true);
                    break;
                case 1400:
                    goods_f16.setSelected(true);
                    goods_f01.setSelected(true);
                    break;
                case 1500:
                    goods_f17.setSelected(true);
                    goods_f01.setSelected(true);
                    break;
                case 1600:
                    goods_f18.setSelected(true);
                    goods_f01.setSelected(true);
                    break;
                case 1700:
                    goods_f19.setSelected(true);
                    goods_f01.setSelected(true);
                    break;
                case 1800:
                    goods_f20.setSelected(true);
                    goods_f01.setSelected(true);
                    break;
                case 1900:
                    goods_f21.setSelected(true);
                    goods_f01.setSelected(true);
                    break;
                case 4000:
                    goods_f22.setSelected(true);
                    goods_f01.setSelected(true);
                    break;
                case 7000:
                    goods_f23.setSelected(true);
                    goods_f02.setSelected(true);
                    break;
                case 7010:
                    goods_f24.setSelected(true);
                    goods_f02.setSelected(true);
                    break;
                case 7100:
                    goods_f25.setSelected(true);
                    goods_f02.setSelected(true);
                    break;
                case 7110:
                    goods_f26.setSelected(true);
                    goods_f02.setSelected(true);
                    break;
                case 7200:
                    goods_f27.setSelected(true);
                    goods_f02.setSelected(true);
                    break;
                case 7210:
                    goods_f28.setSelected(true);
                    goods_f02.setSelected(true);
                    break;
                case 7300:
                    goods_f29.setSelected(true);
                    goods_f02.setSelected(true);
                    break;
                case 7410:
                    goods_f30.setSelected(true);
                    goods_f02.setSelected(true);
                    break;
                case 7421:
                    goods_f31.setSelected(true);
                    goods_f02.setSelected(true);
                    break;
                case 7422:
                    goods_f32.setSelected(true);
                    goods_f02.setSelected(true);
                    break;
                case 7423:
                    goods_f33.setSelected(true);
                    goods_f02.setSelected(true);
                    break;
                case 7424:
                    goods_f34.setSelected(true);
                    goods_f02.setSelected(true);
                    break;
                case 7425:
                    goods_f35.setSelected(true);
                    goods_f02.setSelected(true);
                    break;
                case 7426:
                    goods_f36.setSelected(true);
                    goods_f02.setSelected(true);
                    break;
                case 7427:
                    goods_f37.setSelected(true);
                    goods_f02.setSelected(true);
                    break;
                case 7428:
                    goods_f38.setSelected(true);
                    goods_f02.setSelected(true);
                    break;
                case 7429:
                    goods_f39.setSelected(true);
                    goods_f02.setSelected(true);
                    break;
                case 7430:
                    goods_f40.setSelected(true);
                    goods_f02.setSelected(true);
                    break;
            }
        } else {
            //ごみの種類によってチェックボックスを選択する
            s備考欄 = "";
            int cnt = 1;
            for (int i = 0; i < 10; i++) {
                switchLabel01:
                switch (Integer.valueOf((String) 按分品目Array[4][i])) {
                    //按分数量は計量データを正とする
                    case 100:
                        goods_f03.setSelected(true);
                        goods_f01.setSelected(true);
                        if (cnt < 7) {
                            s備考欄 += (cnt % 2 == 1 ? "" : "　　")
                                     + (String) 按分品目Array[1][i]
                                     + " "
                                     + (String) 按分品目Array[2][i] + "kg" + (cnt != 6 && cnt % 2 == 0 ? "\r\n" : "");
                            cnt++;
                        }
                        break;
                    case 200:
                        goods_f04.setSelected(true);
                        goods_f01.setSelected(true);
                        if (cnt < 7) {
                            s備考欄 += (cnt % 2 == 1 ? "" : "　　")
                                     + (String) 按分品目Array[1][i]
                                     + " "
                                     + (String) 按分品目Array[2][i] + "kg" + (cnt != 6 && cnt % 2 == 0 ? "\r\n" : "");
                            cnt++;
                        }
                        break;
                    case 300:
                        goods_f05.setSelected(true);
                        goods_f01.setSelected(true);
                        if (cnt < 7) {
                            s備考欄 += (cnt % 2 == 1 ? "" : "　　")
                                     + (String) 按分品目Array[1][i]
                                     + " "
                                     + (String) 按分品目Array[2][i] + "kg" + (cnt != 6 && cnt % 2 == 0 ? "\r\n" : "");
                            cnt++;
                        }
                        break;
                    case 400:
                        goods_f06.setSelected(true);
                        goods_f01.setSelected(true);
                        if (cnt < 7) {
                            s備考欄 += (cnt % 2 == 1 ? "" : "　　")
                                     + (String) 按分品目Array[1][i]
                                     + " "
                                     + (String) 按分品目Array[2][i] + "kg" + (cnt != 6 && cnt % 2 == 0 ? "\r\n" : "");
                            cnt++;
                        }
                        break;
                    case 500:
                        goods_f07.setSelected(true);
                        goods_f01.setSelected(true);
                        if (cnt < 7) {
                            s備考欄 += (cnt % 2 == 1 ? "" : "　　")
                                     + (String) 按分品目Array[1][i]
                                     + " "
                                     + (String) 按分品目Array[2][i] + "kg" + (cnt != 6 && cnt % 2 == 0 ? "\r\n" : "");
                            cnt++;
                        }
                        break;
                    case 600:
                        goods_f08.setSelected(true);
                        goods_f01.setSelected(true);
                        if (cnt < 7) {
                            s備考欄 += (cnt % 2 == 1 ? "" : "　　")
                                     + (String) 按分品目Array[1][i]
                                     + " "
                                     + (String) 按分品目Array[2][i] + "kg" + (cnt != 6 && cnt % 2 == 0 ? "\r\n" : "");
                            cnt++;
                        }
                        break;
                    case 700:
                        goods_f09.setSelected(true);
                        goods_f01.setSelected(true);
                        if (cnt < 7) {
                            s備考欄 += (cnt % 2 == 1 ? "" : "　　")
                                     + (String) 按分品目Array[1][i]
                                     + " "
                                     + (String) 按分品目Array[2][i] + "kg" + (cnt != 6 && cnt % 2 == 0 ? "\r\n" : "");
                            cnt++;
                        }
                        break;
                    case 800:
                        goods_f10.setSelected(true);
                        goods_f01.setSelected(true);
                        if (cnt < 7) {
                            s備考欄 += (cnt % 2 == 1 ? "" : "　　")
                                     + (String) 按分品目Array[1][i]
                                     + " "
                                     + (String) 按分品目Array[2][i] + "kg" + (cnt != 6 && cnt % 2 == 0 ? "\r\n" : "");
                            cnt++;
                        }
                        break;
                    case 900:
                        goods_f11.setSelected(true);
                        goods_f01.setSelected(true);
                        if (cnt < 7) {
                            s備考欄 += (cnt % 2 == 1 ? "" : "　　")
                                     + (String) 按分品目Array[1][i]
                                     + " "
                                     + (String) 按分品目Array[2][i] + "kg" + (cnt != 6 && cnt % 2 == 0 ? "\r\n" : "");
                            cnt++;
                        }
                        break;
                    case 1000:
                        goods_f12.setSelected(true);
                        goods_f01.setSelected(true);
                        if (cnt < 7) {
                            s備考欄 += (cnt % 2 == 1 ? "" : "　　")
                                     + (String) 按分品目Array[1][i]
                                     + " "
                                     + (String) 按分品目Array[2][i] + "kg" + (cnt != 6 && cnt % 2 == 0 ? "\r\n" : "");
                            cnt++;
                        }
                        break;
                    case 1100:
                        goods_f13.setSelected(true);
                        goods_f01.setSelected(true);
                        if (cnt < 7) {
                            s備考欄 += (cnt % 2 == 1 ? "" : "　　")
                                     + (String) 按分品目Array[1][i]
                                     + " "
                                     + (String) 按分品目Array[2][i] + "kg" + (cnt != 6 && cnt % 2 == 0 ? "\r\n" : "");
                            cnt++;
                        }
                        break;
                    case 1200:
                        goods_f14.setSelected(true);
                        goods_f01.setSelected(true);
                        if (cnt < 7) {
                            s備考欄 += (cnt % 2 == 1 ? "" : "　　")
                                     + (String) 按分品目Array[1][i]
                                     + " "
                                     + (String) 按分品目Array[2][i] + "kg" + (cnt != 6 && cnt % 2 == 0 ? "\r\n" : "");
                            cnt++;
                        }
                        break;
                    case 1300:
                        goods_f15.setSelected(true);
                        goods_f01.setSelected(true);
                        if (cnt < 7) {
                            s備考欄 += (cnt % 2 == 1 ? "" : "　　")
                                     + (String) 按分品目Array[1][i]
                                     + " "
                                     + (String) 按分品目Array[2][i] + "kg" + (cnt != 6 && cnt % 2 == 0 ? "\r\n" : "");
                            cnt++;
                        }
                        break;
                    case 1400:
                        goods_f16.setSelected(true);
                        goods_f01.setSelected(true);
                        if (cnt < 7) {
                            s備考欄 += (cnt % 2 == 1 ? "" : "　　")
                                     + (String) 按分品目Array[1][i]
                                     + " "
                                     + (String) 按分品目Array[2][i] + "kg" + (cnt != 6 && cnt % 2 == 0 ? "\r\n" : "");
                            cnt++;
                        }
                        break;
                    case 1500:
                        goods_f17.setSelected(true);
                        goods_f01.setSelected(true);
                        if (cnt < 7) {
                            s備考欄 += (cnt % 2 == 1 ? "" : "　　")
                                     + (String) 按分品目Array[1][i]
                                     + " "
                                     + (String) 按分品目Array[2][i] + "kg" + (cnt != 6 && cnt % 2 == 0 ? "\r\n" : "");
                            cnt++;
                        }
                        break;
                    case 1600:
                        goods_f18.setSelected(true);
                        goods_f01.setSelected(true);
                        if (cnt < 7) {
                            s備考欄 += (cnt % 2 == 1 ? "" : "　　")
                                     + (String) 按分品目Array[1][i]
                                     + " "
                                     + (String) 按分品目Array[2][i] + "kg" + (cnt != 6 && cnt % 2 == 0 ? "\r\n" : "");
                            cnt++;
                        }
                        break;
                    case 1700:
                        goods_f19.setSelected(true);
                        goods_f01.setSelected(true);
                        if (cnt < 7) {
                            s備考欄 += (cnt % 2 == 1 ? "" : "　　")
                                     + (String) 按分品目Array[1][i]
                                     + " "
                                     + (String) 按分品目Array[2][i] + "kg" + (cnt != 6 && cnt % 2 == 0 ? "\r\n" : "");
                            cnt++;
                        }
                        break;
                    case 1800:
                        goods_f20.setSelected(true);
                        goods_f01.setSelected(true);
                        if (cnt < 7) {
                            s備考欄 += (cnt % 2 == 1 ? "" : "　　")
                                     + (String) 按分品目Array[1][i]
                                     + " "
                                     + (String) 按分品目Array[2][i] + "kg" + (cnt != 6 && cnt % 2 == 0 ? "\r\n" : "");
                            cnt++;
                        }
                        break;
                    case 1900:
                        goods_f21.setSelected(true);
                        goods_f01.setSelected(true);
                        if (cnt < 7) {
                            s備考欄 += (cnt % 2 == 1 ? "" : "　　")
                                     + (String) 按分品目Array[1][i]
                                     + " "
                                     + (String) 按分品目Array[2][i] + "kg" + (cnt != 6 && cnt % 2 == 0 ? "\r\n" : "");
                            cnt++;
                        }
                        break;
                    case 4000:
                        goods_f22.setSelected(true);
                        goods_f01.setSelected(true);
                        if (cnt < 7) {
                            s備考欄 += (cnt % 2 == 1 ? "" : "　　")
                                     + (String) 按分品目Array[1][i]
                                     + " "
                                     + (String) 按分品目Array[2][i] + "kg" + (cnt != 6 && cnt % 2 == 0 ? "\r\n" : "");
                            cnt++;
                        }
                        break;
                    case 7000:
                        goods_f23.setSelected(true);
                        goods_f02.setSelected(true);
                        if (cnt < 7) {
                            s備考欄 += (cnt % 2 == 1 ? "" : "　　")
                                     + (String) 按分品目Array[1][i]
                                     + " "
                                     + (String) 按分品目Array[2][i] + "kg" + (cnt != 6 && cnt % 2 == 0 ? "\r\n" : "");
                            cnt++;
                        }
                        break;
                    case 7010:
                        goods_f24.setSelected(true);
                        goods_f02.setSelected(true);
                        if (cnt < 7) {
                            s備考欄 += (cnt % 2 == 1 ? "" : "　　")
                                     + (String) 按分品目Array[1][i]
                                     + " "
                                     + (String) 按分品目Array[2][i] + "kg" + (cnt != 6 && cnt % 2 == 0 ? "\r\n" : "");
                            cnt++;
                        }
                        break;
                    case 7100:
                        goods_f25.setSelected(true);
                        goods_f02.setSelected(true);
                        if (cnt < 7) {
                            s備考欄 += (cnt % 2 == 1 ? "" : "　　")
                                     + (String) 按分品目Array[1][i]
                                     + " "
                                     + (String) 按分品目Array[2][i] + "kg" + (cnt != 6 && cnt % 2 == 0 ? "\r\n" : "");
                            cnt++;
                        }
                        break;
                    case 7110:
                        goods_f26.setSelected(true);
                        goods_f02.setSelected(true);
                        if (cnt < 7) {
                            s備考欄 += (cnt % 2 == 1 ? "" : "　　")
                                     + (String) 按分品目Array[1][i]
                                     + " "
                                     + (String) 按分品目Array[2][i] + "kg" + (cnt != 6 && cnt % 2 == 0 ? "\r\n" : "");
                            cnt++;
                        }
                        break;
                    case 7200:
                        goods_f27.setSelected(true);
                        goods_f02.setSelected(true);
                        if (cnt < 7) {
                            s備考欄 += (cnt % 2 == 1 ? "" : "　　")
                                     + (String) 按分品目Array[1][i]
                                     + " "
                                     + (String) 按分品目Array[2][i] + "kg" + (cnt != 6 && cnt % 2 == 0 ? "\r\n" : "");
                            cnt++;
                        }
                        break;
                    case 7210:
                        goods_f28.setSelected(true);
                        goods_f02.setSelected(true);
                        if (cnt < 7) {
                            s備考欄 += (cnt % 2 == 1 ? "" : "　　")
                                     + (String) 按分品目Array[1][i]
                                     + " "
                                     + (String) 按分品目Array[2][i] + "kg" + (cnt != 6 && cnt % 2 == 0 ? "\r\n" : "");
                            cnt++;
                        }
                        break;
                    case 7300:
                        goods_f29.setSelected(true);
                        goods_f02.setSelected(true);
                        if (cnt < 7) {
                            s備考欄 += (cnt % 2 == 1 ? "" : "　　")
                                     + (String) 按分品目Array[1][i]
                                     + " "
                                     + (String) 按分品目Array[2][i] + "kg" + (cnt != 6 && cnt % 2 == 0 ? "\r\n" : "");
                            cnt++;
                        }
                        break;
                    case 7410:
                        goods_f30.setSelected(true);
                        goods_f02.setSelected(true);
                        if (cnt < 7) {
                            s備考欄 += (cnt % 2 == 1 ? "" : "　　")
                                     + (String) 按分品目Array[1][i]
                                     + " "
                                     + (String) 按分品目Array[2][i] + "kg" + (cnt != 6 && cnt % 2 == 0 ? "\r\n" : "");
                            cnt++;
                        }
                        break;
                    case 7421:
                        goods_f31.setSelected(true);
                        goods_f02.setSelected(true);
                        if (cnt < 7) {
                            s備考欄 += (cnt % 2 == 1 ? "" : "　　")
                                     + (String) 按分品目Array[1][i]
                                     + " "
                                     + (String) 按分品目Array[2][i] + "kg" + (cnt != 6 && cnt % 2 == 0 ? "\r\n" : "");
                            cnt++;
                        }
                        break;
                    case 7422:
                        goods_f32.setSelected(true);
                        goods_f02.setSelected(true);
                        if (cnt < 7) {
                            s備考欄 += (cnt % 2 == 1 ? "" : "　　")
                                     + (String) 按分品目Array[1][i]
                                     + " "
                                     + (String) 按分品目Array[2][i] + "kg" + (cnt != 6 && cnt % 2 == 0 ? "\r\n" : "");
                            cnt++;
                        }
                        break;
                    case 7423:
                        goods_f33.setSelected(true);
                        goods_f02.setSelected(true);
                        if (cnt < 7) {
                            s備考欄 += (cnt % 2 == 1 ? "" : "　　")
                                     + (String) 按分品目Array[1][i]
                                     + " "
                                     + (String) 按分品目Array[2][i] + "kg" + (cnt != 6 && cnt % 2 == 0 ? "\r\n" : "");
                            cnt++;
                        }
                        break;
                    case 7424:
                        goods_f34.setSelected(true);
                        goods_f02.setSelected(true);
                        if (cnt < 7) {
                            s備考欄 += (cnt % 2 == 1 ? "" : "　　")
                                     + (String) 按分品目Array[1][i]
                                     + " "
                                     + (String) 按分品目Array[2][i] + "kg" + (cnt != 6 && cnt % 2 == 0 ? "\r\n" : "");
                            cnt++;
                        }
                        break;
                    case 7425:
                        goods_f35.setSelected(true);
                        goods_f02.setSelected(true);
                        if (cnt < 7) {
                            s備考欄 += (cnt % 2 == 1 ? "" : "　　")
                                     + (String) 按分品目Array[1][i]
                                     + " "
                                     + (String) 按分品目Array[2][i] + "kg" + (cnt != 6 && cnt % 2 == 0 ? "\r\n" : "");
                            cnt++;
                        }
                        break;
                    case 7426:
                        goods_f36.setSelected(true);
                        goods_f02.setSelected(true);
                        if (cnt < 7) {
                            s備考欄 += (cnt % 2 == 1 ? "" : "　　")
                                     + (String) 按分品目Array[1][i]
                                     + " "
                                     + (String) 按分品目Array[2][i] + "kg" + (cnt != 6 && cnt % 2 == 0 ? "\r\n" : "");
                            cnt++;
                        }
                        break;
                    case 7427:
                        goods_f37.setSelected(true);
                        goods_f02.setSelected(true);
                        if (cnt < 7) {
                            s備考欄 += (cnt % 2 == 1 ? "" : "　　")
                                     + (String) 按分品目Array[1][i]
                                     + " "
                                     + (String) 按分品目Array[2][i] + "kg" + (cnt != 6 && cnt % 2 == 0 ? "\r\n" : "");
                            cnt++;
                        }
                        break;
                    case 7428:
                        goods_f38.setSelected(true);
                        goods_f02.setSelected(true);
                        if (cnt < 7) {
                            s備考欄 += (cnt % 2 == 1 ? "" : "　　")
                                     + (String) 按分品目Array[1][i]
                                     + " "
                                     + (String) 按分品目Array[2][i] + "kg" + (cnt != 6 && cnt % 2 == 0 ? "\r\n" : "");
                            cnt++;
                        }
                        break;
                    case 7429:
                        goods_f39.setSelected(true);
                        goods_f02.setSelected(true);
                        if (cnt < 7) {
                            s備考欄 += (cnt % 2 == 1 ? "" : "　　")
                                     + (String) 按分品目Array[1][i]
                                     + " "
                                     + (String) 按分品目Array[2][i] + "kg" + (cnt != 6 && cnt % 2 == 0 ? "\r\n" : "");
                            cnt++;
                        }
                        break;
                    case 7430:
                        goods_f40.setSelected(true);
                        goods_f02.setSelected(true);
                        if (cnt < 7) {
                            s備考欄 += (cnt % 2 == 1 ? "" : "　　")
                                     + (String) 按分品目Array[1][i]
                                     + " "
                                     + (String) 按分品目Array[2][i] + "kg" + (cnt != 6 && cnt % 2 == 0 ? "\r\n" : "");
                            cnt++;
                        }
                        break;
                }
            }
        }

        //水銀分追加
        if (model_.getColumns(row_).get("goods_flag42") != null) {
            goods_f42.setSelected((Boolean) model_.getColumns(row_).get("goods_flag42"));
        }
        if (model_.getColumns(row_).get("goods_flag43") != null) {
            goods_f43.setSelected((Boolean) model_.getColumns(row_).get("goods_flag43"));
        }
        if (model_.getColumns(row_).get("goods_flag44") != null) {
            goods_f44.setSelected((Boolean) model_.getColumns(row_).get("goods_flag44"));
        }
        if (model_.getColumns(row_).get("goods_flag45") != null) {
            goods_f45.setSelected((Boolean) model_.getColumns(row_).get("goods_flag45"));
        }
        if (model_.getColumns(row_).get("goods_flag46") != null) {
            goods_f46.setSelected((Boolean) model_.getColumns(row_).get("goods_flag46"));
        }
    }
}
