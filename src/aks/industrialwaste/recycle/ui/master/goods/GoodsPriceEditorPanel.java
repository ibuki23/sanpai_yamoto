package aks.industrialwaste.recycle.ui.master.goods;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.Map;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import aks.swing.JTextField;
import jp.co.astax.awt.FontUtility;

/**
 * 矢本クリーンセンター向けの料金区分別単価入力欄。
 */
class GoodsPriceEditorPanel extends JPanel {

    private final JTextField unitValueNewField_ = new JTextField();
    private final JTextField unitValueExistingField_ = new JTextField();
    private final JTextField unitValueOtherField_ = new JTextField();
    private final JTextField asbestosUnitValueNewField_ = new JTextField();
    private final JTextField asbestosUnitValueExistingField_ = new JTextField();
    private final JTextField asbestosUnitValueOtherField_ = new JTextField();

    private final JPanel formPanel_ = new JPanel(new GridBagLayout());

    GoodsPriceEditorPanel() {
        super(new BorderLayout());

        // 親側がGridBagLayoutで右ペイン中央に寄せるため、外側パネルを
        // 右ペイン相当の高さにして、その上端へ実際の入力パネルを配置する。
        // 外側には枠を表示しないので、空き領域は見た目上の余白になる。
        setOpaque(false);
        setMinimumSize(new Dimension(560, 300));
        setPreferredSize(new Dimension(560, 680));

        formPanel_.setBorder(BorderFactory.createTitledBorder("料金区分別単価"));

        Font font14 = FontUtility.getFont("メイリオ", Font.PLAIN, 14);
        addPriceRow("新規単価", unitValueNewField_, 0, font14);
        addPriceRow("既存単価", unitValueExistingField_, 1, font14);
        addPriceRow("その他単価", unitValueOtherField_, 2, font14);
        addPriceRow("アスベスト含有 新規単価", asbestosUnitValueNewField_, 3, font14);
        addPriceRow("アスベスト含有 既存単価", asbestosUnitValueExistingField_, 4, font14);
        addPriceRow("アスベスト含有 その他単価", asbestosUnitValueOtherField_, 5, font14);

        JLabel note = new JLabel("※標準単価は左側の「請求単価(標準)」を使用します。");
        note.setFont(font14);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.gridwidth = 2;
        gbc.weightx = 1.0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(8, 6, 6, 6);
        formPanel_.add(note, gbc);

        // 実際に見える料金入力パネルは常に上端へ配置する。
        add(formPanel_, BorderLayout.NORTH);
    }

    private void addPriceRow(String labelText, JTextField field, int row, Font font) {
        JLabel label = new JLabel(labelText);
        label.setFont(font);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.weightx = 0.55;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(4, 6, 4, 6);
        formPanel_.add(label, gbc);

        field.setFont(font);
        field.setMinimumSize(new Dimension(150, 30));
        field.setPreferredSize(new Dimension(150, 30));
        field.setHorizontalAlignment(JTextField.RIGHT);
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = row;
        gbc.weightx = 0.45;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(4, 6, 4, 6);
        formPanel_.add(field, gbc);
    }

    void clear() {
        unitValueNewField_.setText("0");
        unitValueExistingField_.setText("0");
        unitValueOtherField_.setText("0");
        asbestosUnitValueNewField_.setText("0");
        asbestosUnitValueExistingField_.setText("0");
        asbestosUnitValueOtherField_.setText("0");
    }

    void loadFrom(Map<String, Object> columns) {
        unitValueNewField_.setText(value(columns, "unit_value_new"));
        unitValueExistingField_.setText(value(columns, "unit_value_existing"));
        unitValueOtherField_.setText(value(columns, "unit_value_other"));
        asbestosUnitValueNewField_.setText(value(columns, "asbestos_unit_value_new"));
        asbestosUnitValueExistingField_.setText(value(columns, "asbestos_unit_value_existing"));
        asbestosUnitValueOtherField_.setText(value(columns, "asbestos_unit_value_other"));
    }

    void saveTo(Map<String, Object> columns) {
        int newValue = intValue(unitValueNewField_);
        int existingValue = intValue(unitValueExistingField_);
        int otherValue = intValue(unitValueOtherField_);

        columns.put("unit_value_new", newValue);
        columns.put("unit_value_existing", existingValue);
        columns.put("unit_value_other", otherValue);
        columns.put("asbestos_unit_value_new", intValue(asbestosUnitValueNewField_));
        columns.put("asbestos_unit_value_existing", intValue(asbestosUnitValueExistingField_));
        columns.put("asbestos_unit_value_other", intValue(asbestosUnitValueOtherField_));

        // 旧A/B/Cにも同期して、既存コードとの互換性を維持する。
        columns.put("unit_value_a", newValue);
        columns.put("unit_value_b", existingValue);
        columns.put("unit_value_c", otherValue);
    }

    private String value(Map<String, Object> columns, String key) {
        Object value = columns.get(key);
        return value instanceof Number ? String.valueOf(((Number) value).intValue()) : "0";
    }

    private int intValue(JTextField field) {
        String text = field.getText() == null ? "" : field.getText().trim().replace(",", "");
        if (text.length() == 0) {
            return 0;
        }
        return Integer.valueOf(text);
    }
}
