package aks.keiryo.gui;

import aks.industrialwaste.recycle.util.GoodsPriceResolver;
import aks.keiryo.status.Status;
import java.awt.Component;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import jp.co.astax.awt.FontUtility;

/**
 * 矢本クリーンセンター向け料金区分・アスベスト選択を既存計量画面へ接続する。
 * 追加行は作らず、取扱品目行と既存の空き領域を利用して画面高さを変えない。
 */
public final class WeighPriceIntegration {

    private static boolean installed;
    private static boolean syncing;
    private static boolean applyScheduled;
    private static boolean contextLoadScheduled;
    private static JComboBox<String> priceCategoryCombo;
    private static JCheckBox asbestosCheck;
    private static JTextField goodsIdField;
    private static JTextField carIdField;
    private static JTextField unitValueField;
    private static MainPanel owner;
    private static Connection con;
    private static long beforeWeighSno = -1L;

    private WeighPriceIntegration() {
    }

    public static void install(final Connection connection) {
        if (installed) {
            return;
        }
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                if (installed || MainPanel.mStatus == null || MainPanel.mStatus.mMainPanel == null) {
                    return;
                }
                try {
                    con = connection;
                    owner = MainPanel.mStatus.mMainPanel;
                    installUi();
                    installed = true;
                } catch (Exception e) {
                    System.out.println("WEIGH PRICE UI INSTALL ERROR:" + e);
                }
            }
        });
    }

    private static void installUi() throws Exception {
        JPanel panel = (JPanel) field("jPanel3").get(owner);
        goodsIdField = (JTextField) field("kind_no_tex").get(owner);
        carIdField = (JTextField) field("car_id_").get(owner);
        unitValueField = (JTextField) field("unit_value_tex").get(owner);

        priceCategoryCombo = new JComboBox<String>(new String[]{"通常", "新規", "既存", "その他"});
        priceCategoryCombo.setFont(FontUtility.getFont("メイリオ", java.awt.Font.PLAIN, 18));
        priceCategoryCombo.setToolTipText("料金区分");
        // 既存画面は Enter / transferFocus() で入力欄を順番に移動する。
        // 実行時追加したコンボがフォーカス順へ割り込まないようにする。
        priceCategoryCombo.setFocusable(false);

        asbestosCheck = new JCheckBox("アスベスト含有");
        asbestosCheck.setFont(FontUtility.getFont("メイリオ", java.awt.Font.PLAIN, 14));
        asbestosCheck.setOpaque(false);
        asbestosCheck.setFocusable(false);

        installControlsInExistingRows(panel);

        ActionListener priceChanged = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (syncing) {
                    return;
                }
                applySelection(true);
                persistGateContext();
            }
        };
        priceCategoryCombo.addActionListener(priceChanged);
        asbestosCheck.addActionListener(priceChanged);

        goodsIdField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                scheduleApply();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                scheduleApply();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                scheduleApply();
            }
        });

        carIdField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                scheduleLoadContext();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                scheduleLoadContext();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                scheduleLoadContext();
            }
        });

        // Enter キーも JButton の ActionListener が発火するため、
        // KeyboardFocusManager へグローバルな Enter フックを追加しない。
        hookWeighButton("weight_btn_計量処理");
        hookWeighButton("weight_btn_登録処理");

        panel.repaint();
        loadGateContext();
        applySelection(false);
    }

    /**
     * 既存の画面高さを変えずに料金項目を配置する。
     * 料金区分の文言は表示せず、料金区分コンボとアスベストチェックの左端を
     * 売上区分（掛け/現金）コンボの左端へ揃える。
     */
    private static void installControlsInExistingRows(JPanel panel) throws Exception {
        JPanel goodsPanel = (JPanel) field("jPanel1").get(owner);
        JLabel kindLabel = (JLabel) field("kind_jLab").get(owner);
        JLabel carryName = (JLabel) field("carry_name_").get(owner);
        JLabel contractText = (JLabel) field("txt契約内容表示").get(owner);
        Component officeLabel = (Component) field("office_jLab").get(owner);
        Component officeSubName = (Component) field("office_sub_name_").get(owner);
        JComboBox salesCategoryCombo = (JComboBox) field("cmb売上区分").get(owner);

        panel.doLayout();
        goodsPanel.doLayout();

        // Free Design(GroupLayout) の現在位置を固定し、実行時の追加で
        // 既存行が押し下げられないようにする。
        panel.setLayout(null);

        Rectangle goodsPanelBounds = new Rectangle(goodsPanel.getBounds());
        Rectangle salesComboBounds = new Rectangle(salesCategoryCombo.getBounds());
        int goodsPanelWidth = Math.max(goodsPanelBounds.width, 600);
        int rowHeight = Math.max(30, goodsPanelBounds.height);

        goodsPanel.setLayout(null);

        int carryWidth = carryName.getWidth();
        if (carryWidth <= 0) {
            carryWidth = 130;
        }
        carryWidth = Math.max(90, Math.min(carryWidth, 130));

        final int gap = 5;
        final int comboWidth = Math.max(95, salesComboBounds.width);

        // cmb売上区分 は jPanel3 直下、料金区分コンボは jPanel1 内なので座標を変換する。
        int comboX = salesComboBounds.x - goodsPanelBounds.x;
        comboX = Math.max(carryWidth + gap + 250,
                Math.min(comboX, goodsPanelWidth - comboWidth));

        // 搬入/搬出表示を料金区分コンボの左へ移動し、残りを品目名に使う。
        int carryX = comboX - gap - carryWidth;
        int kindWidth = Math.max(250, carryX - gap);

        kindLabel.setBounds(0, 0, kindWidth, rowHeight);
        carryName.setBounds(carryX, 0, carryWidth, rowHeight);
        priceCategoryCombo.setBounds(comboX, 0, comboWidth, rowHeight);

        goodsPanel.add(priceCategoryCombo);
        goodsPanel.setComponentZOrder(priceCategoryCombo, 0);

        // 排出現場名の右端を、取扱品目名(kind_jLab)の右端と揃える。
        int kindRightInPanel = goodsPanelBounds.x + kindWidth;
        Rectangle officeBounds = new Rectangle(officeLabel.getBounds());
        int officeWidth = Math.max(0, kindRightInPanel - officeBounds.x);
        officeLabel.setBounds(officeBounds.x, officeBounds.y,
                officeWidth, officeBounds.height);

        // office_sub_name_ は右端を維持したまま、office_jLab の新しい右端から始める。
        Rectangle officeSubBounds = new Rectangle(officeSubName.getBounds());
        int officeSubRight = officeSubBounds.x + officeSubBounds.width;
        int officeSubX = kindRightInPanel + gap;
        int officeSubWidth = Math.max(0, officeSubRight - officeSubX);
        officeSubName.setBounds(officeSubX, officeSubBounds.y,
                officeSubWidth, officeSubBounds.height);

        // アスベストチェックは「掛け/現金」コンボと同じX座標から開始する。
        // txt契約内容表示の右端をチェックボックス手前まで縮め、重ならないようにする。
        Rectangle contractBounds = new Rectangle(contractText.getBounds());
        int asbestosX = salesComboBounds.x;
        int asbestosWidth = Math.max(105, panel.getWidth() - asbestosX - 4);
        int contractRight = Math.max(contractBounds.x, asbestosX - gap);
        int contractWidth = Math.max(0, contractRight - contractBounds.x);
        contractText.setBounds(contractBounds.x, contractBounds.y,
                contractWidth, contractBounds.height);
        asbestosCheck.setBounds(asbestosX, contractBounds.y,
                asbestosWidth, Math.max(30, contractBounds.height));

        panel.add(asbestosCheck);
        panel.setComponentZOrder(asbestosCheck, 0);

        goodsPanel.repaint();
        panel.repaint();

        System.out.println(
                "WEIGH PRICE UI ALIGN salesX=" + salesComboBounds.x
                + " goodsPanelX=" + goodsPanelBounds.x
                + " comboX=" + comboX
                + " carryX=" + carryX
                + " kindWidth=" + kindWidth
                + " officeRight=" + (officeBounds.x + officeWidth)
                + " contractWidth=" + contractWidth
                + " asbestos=" + asbestosCheck.getBounds());
    }

    private static void hookWeighButton(String fieldName) throws Exception {
        final JButton button = (JButton) field(fieldName).get(owner);
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                prepareForWeigh();
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        finishAfterWeigh();
                    }
                });
            }
        });
    }

    private static void scheduleApply() {
        if (syncing || applyScheduled) {
            return;
        }
        applyScheduled = true;
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                applyScheduled = false;
                if (!syncing) {
                    applySelection(false);
                }
            }
        });
    }

    private static void scheduleLoadContext() {
        if (syncing || contextLoadScheduled) {
            return;
        }
        contextLoadScheduled = true;
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                contextLoadScheduled = false;
                if (syncing) {
                    return;
                }
                loadGateContext();
                applySelection(false);
            }
        });
    }

    private static void loadGateContext() {
        if (con == null || carIdField == null || priceCategoryCombo == null || syncing) {
            return;
        }

        int carId = intValue(carIdField.getText());
        syncing = true;
        try {
            if (carId <= 0) {
                priceCategoryCombo.setSelectedIndex(GoodsPriceResolver.PRICE_CATEGORY_NORMAL);
                asbestosCheck.setSelected(false);
                return;
            }

            String sql = "select price_category, asbestos_flag from gate_in_data "
                    + "where car_id=? and delete_flag<>true order by sno desc limit 1";
            try (PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setInt(1, carId);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        int category = rs.getInt("price_category");
                        if (category < 0 || category > 3) {
                            category = 0;
                        }
                        priceCategoryCombo.setSelectedIndex(category);
                        asbestosCheck.setSelected(rs.getBoolean("asbestos_flag"));
                    } else {
                        priceCategoryCombo.setSelectedIndex(GoodsPriceResolver.PRICE_CATEGORY_NORMAL);
                        asbestosCheck.setSelected(false);
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("WEIGH PRICE CONTEXT LOAD ERROR:" + e);
        } finally {
            syncing = false;
        }
    }

    /**
     * Apply the bill master default only when the bill ID field loses focus.
     * Loading an existing weighing record continues to use its saved context.
     */
    public static void loadBillPriceCategory(String billIdText) {
        if (!installed || syncing || con == null || priceCategoryCombo == null) {
            return;
        }

        int category = GoodsPriceResolver.PRICE_CATEGORY_NORMAL;
        int billId = intValue(billIdText);
        if (billId > 0) {
            String sql = "select price_category from m_bill where id=? and delete_flag<>true";
            try (PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setInt(1, billId);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        category = rs.getInt("price_category");
                    }
                }
            } catch (Exception e) {
                java.util.logging.Logger.getLogger(WeighPriceIntegration.class.getName())
                        .log(java.util.logging.Level.SEVERE, "WEIGH BILL PRICE CATEGORY LOAD ERROR", e);
                return;
            }
        }
        if (category < 0 || category > 3) {
            category = GoodsPriceResolver.PRICE_CATEGORY_NORMAL;
        }

        syncing = true;
        try {
            priceCategoryCombo.setSelectedIndex(category);
        } finally {
            syncing = false;
        }
        applySelection(true);
        persistGateContext();
    }
    private static void applySelection(boolean showMessage) {
        if (syncing || MainPanel.mStatus == null
                || priceCategoryCombo == null || asbestosCheck == null) {
            return;
        }

        int category = priceCategoryCombo.getSelectedIndex();
        boolean asbestos = asbestosCheck.isSelected();
        MainPanel.mStatus.msRemote.mValue.put("price_category", category);
        MainPanel.mStatus.msRemote.mValue.put("asbestos_flag", asbestos);

        int goodsId = intValue(goodsIdField == null ? null : goodsIdField.getText());
        if (goodsId <= 0) {
            return;
        }

        if (asbestos && category == GoodsPriceResolver.PRICE_CATEGORY_NORMAL) {
            setUnitValue(0);
            if (showMessage) {
                setMessage("アスベスト含有時は新規・既存・その他の料金区分を選択してください。");
            }
            return;
        }

        try {
            int price = GoodsPriceResolver.resolve(con, goodsId, category, asbestos);
            setUnitValue(price);
        } catch (Exception e) {
            System.out.println("WEIGH PRICE RESOLVE ERROR:" + e);
        }
    }

    /**
     * MainPanel.UpdateView() を呼ぶと管理ID・品目ID等が再設定され、DocumentListener が
     * 再発火してループするため、単価欄とステータス値だけを直接更新する。
     */
    private static void setUnitValue(int value) {
        String text = String.valueOf(value);
        if (unitValueField != null && !text.equals(unitValueField.getText())) {
            unitValueField.setText(text);
        }
        MainPanel.mStatus.SetData(Status.INDEX_UNIT_VALUE, text);
    }

    private static void setMessage(String message) {
        MainPanel.mStatus.SetData(Status.INDEX_MESSAGE, message);
        try {
            JLabel messageLabel = (JLabel) field("jLbl_message").get(owner);
            messageLabel.setText(message);
        } catch (Exception e) {
            System.out.println("WEIGH PRICE MESSAGE ERROR:" + e);
        }
    }

    private static void prepareForWeigh() {
        applySelection(true);
        beforeWeighSno = maxWeighSno();
        persistGateContext();
    }

    private static void finishAfterWeigh() {
        persistGateContext();
        int carId = MainPanel.mStatus == null
                ? 0 : MainPanel.mStatus.msRemote.GetInt("car_id");
        if (carId <= 0 || beforeWeighSno < 0) {
            return;
        }

        String sql = "update weigh_data set price_category=?, asbestos_flag=? "
                + "where sno > ? and car_id=?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, priceCategoryCombo.getSelectedIndex());
            ps.setBoolean(2, asbestosCheck.isSelected());
            ps.setLong(3, beforeWeighSno);
            ps.setInt(4, carId);
            ps.executeUpdate();
        } catch (Exception e) {
            System.out.println("WEIGH PRICE RESULT SAVE ERROR:" + e);
        }
    }

    private static void persistGateContext() {
        if (MainPanel.mStatus == null || con == null || priceCategoryCombo == null) {
            return;
        }

        int carId = MainPanel.mStatus.msRemote.GetInt("car_id");
        if (carId <= 0) {
            return;
        }

        String sql = "update gate_in_data set price_category=?, asbestos_flag=?, unit_value=? "
                + "where car_id=? and delete_flag<>true";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, priceCategoryCombo.getSelectedIndex());
            ps.setBoolean(2, asbestosCheck.isSelected());
            ps.setInt(3, intValue(unitValueField.getText()));
            ps.setInt(4, carId);
            ps.executeUpdate();
        } catch (Exception e) {
            System.out.println("WEIGH PRICE STAY SAVE ERROR:" + e);
        }
    }

    private static long maxWeighSno() {
        try (PreparedStatement ps = con.prepareStatement(
                "select coalesce(max(sno),0) from weigh_data");
             ResultSet rs = ps.executeQuery()) {
            return rs.next() ? rs.getLong(1) : 0L;
        } catch (Exception e) {
            System.out.println("WEIGH PRICE MAX SNO ERROR:" + e);
            return -1L;
        }
    }

    private static int intValue(String text) {
        if (text == null) {
            return 0;
        }
        try {
            return Integer.parseInt(text.trim().replace(",", ""));
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    private static Field field(String name) throws Exception {
        Field f = MainPanel.class.getDeclaredField(name);
        f.setAccessible(true);
        return f;
    }
}
