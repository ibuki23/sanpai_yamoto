package aks.swing.table;

import java.util.*;
import java.awt.*;
import javax.swing.*;
import javax.swing.table.*;

public class GroupableTableHeader extends JTableHeader {

    protected Vector columnGroups = null;

    public GroupableTableHeader(TableColumnModel model) {
        super(model);
        setReorderingAllowed(false);
    }

    public void setReorderingAllowed(boolean b) {
        reorderingAllowed = false;
    }

    public void addColumnGroup(ColumnGroup g) {
        if (columnGroups == null) {
            columnGroups = new Vector();
        }
        columnGroups.addElement(g);
    }

    public Enumeration getColumnGroups(TableColumn col) {
        if (columnGroups == null) {
            return null;
        }
        Enumeration en = columnGroups.elements();
        while (en.hasMoreElements()) {
            ColumnGroup cGroup = (ColumnGroup) en.nextElement();
            Vector v_ret = (Vector) cGroup.getColumnGroups(col, new Vector());
            if (v_ret != null) {
                return v_ret.elements();
            }
        }
        return null;
    }

    @Override
    public void updateUI() {
        setUI(new GroupableTableHeaderUI());

        TableCellRenderer tablecellrenderer = getDefaultRenderer();
        if (tablecellrenderer instanceof Component) {
            SwingUtilities.updateComponentTreeUI((Component) tablecellrenderer);
        }

    }

    public void setColumnMargin() {
        if (columnGroups == null) {
            return;
        }
        Enumeration en = columnGroups.elements();
        while (en.hasMoreElements()) {
            ColumnGroup cGroup = (ColumnGroup) en.nextElement();
            cGroup.setColumnMargin(0);
        }
    }

}
