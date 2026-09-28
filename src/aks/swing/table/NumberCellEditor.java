package aks.swing.table;

import java.awt.Color;
import java.awt.Component;
import java.awt.im.InputSubset;
import java.text.DecimalFormat;
import java.util.EventObject;
import javax.swing.AbstractCellEditor;
import javax.swing.BorderFactory;
import javax.swing.JComboBox;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.TableCellEditor;

public class NumberCellEditor extends AbstractCellEditor implements TableCellEditor {

    private static final long serialVersionUID = 1L;

    private JTextField text;
    private Object value;

    private Component editor;
    private Color oColor = new Color(255, 255, 255);
    private Color dColor = new Color(255, 204, 204);

    public NumberCellEditor(DecimalFormat df) {
        text = new JTextField();

        oColor = text.getBackground();
        text.setBorder(BorderFactory.createEmptyBorder());
        text.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                //フォーカスで全選択、背景変更
                setAsciiMode(text);
                text.selectAll();
                text.setBackground(dColor);
            }

            public void focusLost(java.awt.event.FocusEvent evt) {

                text.setBackground(oColor);
            }
        });
    }

    @Override
    public Object getCellEditorValue() {
        return value;
    }

    @Override
    public boolean isCellEditable(EventObject e) {
        text.setBackground(dColor);
        return true;
    }

    @Override
    public boolean stopCellEditing() {
        value = "";
        if (editor instanceof JComboBox) {
            value = ((JComboBox) editor).getSelectedItem();
        } else if (editor instanceof JTextField) {
            value = ((JTextField) editor).getText();
        }

        return super.stopCellEditing();
    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value,
             boolean isSelected, int row, int column) {
        text.setText(value.toString());
        text.selectAll();//コメントすると入力値を上書きできないので注意
        editor = text;

        return editor;
    }

    // 半角英数字モードの設定
    private void setAsciiMode(javax.swing.JTextField jt) {
        Character.Subset[] subsets = {InputSubset.LATIN};
        jt.getInputContext().setCompositionEnabled(false);
        jt.getInputContext().setCharacterSubsets(subsets);
    }
}
