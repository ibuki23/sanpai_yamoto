package aks.swing.table;

import java.awt.Component;
import javax.swing.JCheckBox;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

public class BooleanStringTableCellRenderer extends DefaultTableCellRenderer {

    public BooleanStringTableCellRenderer() {

        super();
    }

    @Override
    public Component getTableCellRendererComponent(
             JTable table,
             Object value,
             boolean isSelected,
             boolean hasFocus,
             int row,
             int column) {
        if (value == null) {
            JCheckBox select = new JCheckBox();
            select.setHorizontalAlignment(javax.swing.JCheckBox.CENTER);
            return (select);
        }

        Component col = null;

        JCheckBox select = new JCheckBox();
        Boolean val = ((String) value).length() > 0;
        select.setSelected(val.booleanValue());
        select.setHorizontalAlignment(javax.swing.JCheckBox.CENTER);

        col = select;

        return (col);
    }
}
