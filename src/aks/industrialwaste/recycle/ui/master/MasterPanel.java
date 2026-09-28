package aks.industrialwaste.recycle.ui.master;

import aks.industrialwaste.recycle.ui.SystemSetting.SystemSetting;
import aks.industrialwaste.recycle.ui.master.agreement.Agreement;
import aks.industrialwaste.recycle.ui.master.bill.Bill;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.sql.Connection;
import javax.swing.JTabbedPane;
import aks.industrialwaste.recycle.ui.master.car.Car;
import aks.industrialwaste.recycle.ui.master.dis_entrepreneur.Dis_entrepreneur;
import aks.industrialwaste.recycle.ui.master.goods.Goods;
import aks.industrialwaste.recycle.ui.master.contractory.Contractory;
import aks.industrialwaste.recycle.ui.master.transporter.Transporter;
import aks.industrialwaste.recycle.ui.master.car_kind.Car_kind;
import aks.industrialwaste.recycle.ui.master.manifesto3.Manifesto3;
import aks.industrialwaste.recycle.ui.master.method.Method1;
import aks.industrialwaste.database.DBManager;
import aks.industrialwaste.ui.Panel;
import java.awt.Font;
import jp.co.astax.awt.FontUtility;

public class MasterPanel extends Panel {

    private JTabbedPane tabbedPane_;

    public MasterPanel(Connection con) {
        createMasterPanel(con);
    }

    @Override
    protected void initComponents() {
        super.initComponents();
        Font font18 = FontUtility.getFont("メイリオ", Font.PLAIN, 18);
        setMinimumSize(new Dimension(1280, 900));
        setPreferredSize(new Dimension(1280, 900));
        // タイトル
        titleField_.setText("マスタ設定");
        titleField_.setFont(font18);
        // マスタ
        tabbedPane_ = new JTabbedPane();
        tabbedPane_.setFont(font18);
        addContainer(
                 centerPane_, tabbedPane_,
                 0, 0, 1, 1, 1.0, 1.0,
                 GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                 new Insets(2, 2, 2, 2), 0, 0);
    }

    private void createMasterPanel(Connection con) {
        String btag2 = "";
        String atag = "";

        tabbedPane_.add(new Car(con), btag2 + "管理（車両）" + atag);
        tabbedPane_.add(new Agreement(con), btag2 + "契約" + atag);
        tabbedPane_.add(new Bill(con), btag2 + "請求先" + atag);
        tabbedPane_.add(new Dis_entrepreneur(con), btag2 + "排出事業者" + atag);
        tabbedPane_.add(new Transporter(con), btag2 + "運搬業者" + atag);
//        tabbedPane_.add(new TransTantou(con), btag2 + "運搬担当者" + atag);
//        tabbedPane_.add(new GoodsCategory(con), btag2 + "品目中分類" + atag);
        tabbedPane_.add(new Goods(con), btag2 + "品目" + atag);
        tabbedPane_.add(new Contractory(con), btag2 + "処分業者" + atag);
        tabbedPane_.add(new Manifesto3(con), btag2 + "処分場" + atag);
//        tabbedPane_.add(new Tsumikae(con), btag2 + "保管場所" + atag);
        tabbedPane_.add(new Car_kind(con), btag2 + "車種" + atag);
        tabbedPane_.add(new Method1(con), btag2 + "処分方法" + atag);
        if (DBManager.PAYMENT_FLAG) {
        }
        tabbedPane_.add(new SystemSetting(con), btag2 + "システム設定" + atag);
    }
}
