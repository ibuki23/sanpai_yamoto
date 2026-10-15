package aks.keiryo.gui;

import aks.industrialwaste.recycle.util.GoodsPriceResolver;
import aks.keiryo.status.Status;
import java.awt.Component;
import java.awt.KeyboardFocusManager;
import java.awt.KeyEventDispatcher;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
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

        asbestosCheck = new JCheckBox("アスベスト含有");
        asbestosCheck.setFont(FontUtility.getFont("メイリオ", java.awt.Font.PLAIN, 18));
        asbestosCheck.setOpaque(false);

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
            public void insertUpdate(DocumentEvent e) { scheduleApply(); }
            public void removeUpdate(DocumentEvent e) { scheduleApply(); }
            public void changedUpdate(DocumentEvent e) { scheduleApply(); }
        });
        carIdField.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) { scheduleLoadContext(); }
            public void removeUpdate(DocumentEvent e) { scheduleLoadContext(); }
            public void changedUpdate(DocumentEvent e) { scheduleLoadContext(); }
        });

        hookWeighButton("weight_btn_計量処理");
        hookWeighButton("weight_btn_登録処理");

        KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(
                 new KeyEventDispatcher() {
            @Override
            public boolean dispatchKeyEvent(KeyEvent e) {
                if (e.getID() == KeyEvent.KEY_PRESSED && e.getKeyCode() == KeyEvent.VK_ENTER) {
                    Component c = e.getComponent();
                    try {
                        if (c == field("weight_btn_計量処理").get(owner)
                                 || c == field("weight_btn_登録処理").get(owner)) {
                            prepareForWeigh();
                            SwingUtilities.invokeLater(new Runnable() {
                                @Override
                                public void run() { finishAfterWeigh(); }
                            });
                        }
                    } catch (Exception ex) {
                        System.out.println("WEIGH PRICE KEY ERROR:" + ex);
                    }
                }
                return false;
            }
        });

        panel.repaint();
        loadGateContext();
        applySelection(false);
    }

    /**
     * 既存の画面高さを変えずに料金項目を配置する。
     * 料金区分は取扱品目名(kind_jLab)を少し縮めた領域へ配置し、
     * アスベストチェックは契約内容表示の右側を一部利用する。
     * sub_message_ / txt契約内容表示 は実際に使用されているため削除しない。
     */
    private static void installControlsInExistingRows(JPanel panel) throws Exception {
        JPanel goodsPanel = (JPanel) field("jPanel1").get(owner);
        JLabel kindLabel = (JLabel) field("kind_jLab").get(owner);
        JLabel carryName = (JLabel) field("carry_name_").get(owner);
        JLabel contractText = (JLabel) field("txt契約内容表示").get(owner);

        panel.doLayout();
        goodsPanel.doLayout();

        // Free Design(GroupLayout) の現在位置を固定し、以後の実行時追加で
        // 既存行が押し下げられないようにする。
        panel.setLayout(null);

        Rectangle goodsPanelBounds = new Rectangle(goodsPanel.getBounds());
        int goodsPanelWidth = Math.max(goodsPanelBounds.width, 600);
        int rowHeight = Math.max(30, goodsPanelBounds.height);

        // jPanel1 内だけは品目名・料金区分・搬入搬出表示を横並びで再配置する。
        goodsPanel.setLayout(null);

        int carryWidth = carryName.getWidth();
        if (carryWidth <= 0) {
            carryWidth = 130;
        }
        carryWidth = Math.max(90, Math.min(carryWidth, 130));

        final int gap = 5;
        final int priceLabelWidth = 72;
        final int comboWidth = 110;

        int kindWidth = goodsPanelWidth - carryWidth - priceLabelWidth - comboWidth - (gap * 4);
        kindWidth = Math.max(250, kindWidth);

        JLabel priceLabel = new JLabel("料金区分");
        priceLabel.setFont(FontUtility.getFont("メイリオ", java.awt.Font.PLAIN, 16));
        priceLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

        int x = 0;
        kindLabel.setBounds(x, 0, kindWidth, rowHeight);
        x += kindWidth + gap;
        priceLabel.setBounds(x, 0, priceLabelWidth, rowHeight);
        x += priceLabelWidth + gap;
        priceCategoryCombo.setBounds(x, 0, comboWidth, rowHeight);
        x += comboWidth + gap;
        carryName.setBounds(x, 0, Math.max(70, goodsPanelWidth - x), rowHeight);

        goodsPanel.add(priceLabel);
        goodsPanel.add(priceCategoryCombo);
        goodsPanel.setComponentZOrder(priceLabel, 0);
        goodsPanel.setComponentZOrder(priceCategoryCombo, 0);

        // txt契約内容表示 は契約内容表示に使用されているため残し、右側だけを
        // アスベストチェック用に確保する。幅が不足する場合は sub_message_ 側を利用する。
        Rectangle contractBounds = new Rectangle(contractText.getBounds());
        int checkWidth = 175;
        int checkGap = 8;
        int minContractWidth = 260;

        if (contractBounds.width >= minContractWidth + checkWidth + checkGap) {
            int newContractWidth = contractBounds.width - checkWidth - checkGap;
            contractText.setBounds(contractBounds.x, contractBounds.y,
                    newContractWidth, contractBounds.height);
            asbestosCheck.setBounds(contractBounds.x + newContractWidth + checkGap,
                    contractBounds.y, checkWidth, contractBounds.height);
        } else {
            JLabel subMessage = (JLabel) field("sub_message_").get(owner);
            Rectangle subBounds = new Rectangle(subMessage.getBounds());
            int minSubWidth = 320;
            if (subBounds.width >= minSubWidth + checkWidth + checkGap) {
                int newSubWidth = subBounds.width - checkWidth - checkGap;
                subMessage.setBounds(subBounds.x, subBounds.y,
                        newSubWidth, subBounds.height);
                asbestosCheck.setBounds(subBounds.x + newSubWidth + checkGap,
                        subBounds.y, checkWidth, subBounds.height);
            } else {
                // 最終フォールバック。既存行を増やさず品目行の右端へ置く。
                Rectangle gp = goodsPanel.getBounds();
                asbestosCheck.setBounds(
                        Math.max(0, gp.x + gp.width - checkWidth),
                        gp.y, checkWidth, Math.max(30, gp.height));
            }
        }

        panel.add(asbestosCheck);
        panel.setComponentZOrder(asbestosCheck, 0);

        goodsPanel.repaint();
        panel.repaint();

        System.out.println(
                 "WEIGH PRICE UI INLINE goodsPanel=" + goodsPanelWidth + "x" + rowHeight
                 + " kindWidth=" + kindWidth
                 + " contract=" + contractBounds.width
                 + " asbestos=" + asbestosCheck.getBounds());
    }

    private static void hookWeighButton(String fieldName) throws Exception {
        final JButton button = (JButton) field(fieldName).get(owner);
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                prepareForWeigh();
            }
        });
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() { finishAfterWeigh(); }
                });
            }
        });
    }

    private static void scheduleApply() {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() { applySelection(false); }
        });
    }

    private static void scheduleLoadContext() {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                loadGateContext();
                applySelection(false);
            }
        });
    }

    private static void loadGateContext() {
        if (con == null || carIdField == null || priceCategoryCombo == null) {
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

    private static void applySelection(boolean showMessage) {
        if (MainPanel.mStatus == null || priceCategoryCombo == null || asbestosCheck == null) {
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
            unitValueField.setText("0");
            MainPanel.mStatus.SetData(Status.INDEX_UNIT_VALUE, "0");
            if (showMessage) {
                MainPanel.mStatus.SetData(Status.INDEX_MESSAGE,
                         "アスベスト含有時は新規・既存・その他の料金区分を選択してください。");
            }
            owner.UpdateView(MainPanel.mStatus.msRemote);
            return;
        }

        try {
            int price = GoodsPriceResolver.resolve(con, goodsId, category, asbestos);
            unitValueField.setText(String.valueOf(price));
            MainPanel.mStatus.SetData(Status.INDEX_UNIT_VALUE, String.valueOf(price));
            owner.UpdateView(MainPanel.mStatus.msRemote);
        } catch (Exception e) {
            System.out.println("WEIGH PRICE RESOLVE ERROR:" + e);
        }
    }

    private static void prepareForWeigh() {
        applySelection(true);
        beforeWeighSno = maxWeighSno();
        persistGateContext();
    }

    private static void finishAfterWeigh() {
        persistGateContext();
        int carId = MainPanel.mStatus == null ? 0 : MainPanel.mStatus.msRemote.GetInt("car_id");
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
        try (PreparedStatement ps = con.prepareStatement("select coalesce(max(sno),0) from weigh_data");
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
