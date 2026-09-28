package aks.industrialwaste.database;

import java.io.Serializable;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

public class Table implements Serializable {

    private String title_;
    private String name_;
    private Map<String, Column> columns_;

    public Table() {
        setAttribute("", "", new Column[0]);
    }

    public Table(Table table) {
        setAttribute(table);
    }

    public Table(String title, String name, Column[] columns) {
        setAttribute(title, name, columns);
    }

    public Table getAttribute() {
        return new Table(getTitle(), getName(), getColumns());
    }

    public Column[] getColumns() {
        return columns_.values().toArray(new Column[0]);
    }

    public String[] getColumnNames() {
        return columns_.keySet().toArray(new String[0]);
    }

    public String getName() {
        return name_;
    }

    public String getTitle() {
        return title_;
    }

    public void setAttribute(Table table) {
        if (null == table) {
            setAttribute("", "", new Column[0]);
        } else {
            setAttribute(table.getTitle(), table.getName(), table.getColumns());
        }
    }

    public void setAttribute(String title, String name, Column[] columns) {
        title_ = (null == title) ? ("") : (title);
        name_ = (null == name) ? ("") : (name);

        if (null == columns_) {
            columns_
                     = Collections.synchronizedMap(new LinkedHashMap<String, Column>());
        }
        if (null != columns) {
            for (Column column : columns) {
                if (null != column) {
                    columns_.put(column.getName(), column);
                }
            }
        }
    }
}
