package aks.keiryo.gui;

import aks.industrialwaste.recycle.util.GoodsPriceResolver;
import aks.keiryo.status.Status;
import java.awt.Color;
import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.KeyboardFocusManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyEventDispatcher;
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
 * 既存NetBeans生成画面を大きく崩さないため、空いている6行目へ動的に追加する。
 */
public final class WeighPriceIntegration {

    private static boolean installed;
    private static JComboBox<String> priceCategoryCombo;
    private static JCheckBox asbestosCheck;
    private static JTextField goodsIdField;
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
        unitValueField = (JTextField) field("unit_value_tex").get(owner);

        JLabel label = new JLabel("　料金区分");
        label.setFont(FontUtility.getFont("メイリオ", java.awt.Font.PLAIN, 18));
        label.setForeground(Color.WHITE);
        label.setBackground(new Color(0, 51, 153));
        label.setOpaque(true);
        label.setBorder(javax.swing.BorderFactory.createEtchedBorder(
                 javax.swing.border.EtchedBorder.RAISED));
        label.setMinimumSize(new java.awt.Dimension(150, 30));
        label.setPreferredSize(new java.awt.Dimension(150, 30));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.insets = new Insets(0, 0, 0, 5);
        panel.add(label, gbc);

        priceCategoryCombo = new JComboBox<String>(new String[]{"通常", "新規", "既存", "その他"});
        priceCategoryCombo.setFont(FontUtility.getFont("メイリオ", java.awt.Font.PLAIN, 18));
        priceCategoryCombo.setMinimumSize(new java.awt.Dimension(130, 35));
        priceCategoryCombo.setPreferredSize(new java.awt.Dimension(130, 35));
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 6;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(2, 0, 2, 2);
        panel.add(priceCategoryCombo, gbc);

        asbestosCheck = new JCheckBox("アスベスト含有");
        asbestosCheck.setFont(FontUtility.getFont("メイリオ", java.awt.Font.PLAIN, 18));
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 6;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(2, 2, 2, 5);
        panel.add(asbestosCheck, gbc);

        ActionListener priceChanged = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
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

        panel.revalidate();
        panel.repaint();
        applySelection(false);
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
        if (MainPanel.mStatus == null || con == null) {
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
