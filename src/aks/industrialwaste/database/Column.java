package aks.industrialwaste.database;

import java.io.Serializable;

public class Column implements Serializable {

    private String title_;
    private String name_;
    private ColumnType type_;

    public Column() {
        setAttribute("", "", ColumnType.TEXT);
    }

    public Column(Column column) {
        setAttribute(column);
    }

    public Column(String title, String name, ColumnType type) {
        setAttribute(title, name, type);
    }

    public Column getAttribute() {
        return new Column(getTitle(), getName(), getType());
    }

    public String getName() {
        return name_;
    }

    public String getTitle() {
        return title_;
    }

    public ColumnType getType() {
        return type_;
    }

    public void setAttribute(Column column) {
        if (null == column) {
            setAttribute("", "", ColumnType.TEXT);
        } else {
            setAttribute(column.getTitle(), column.getName(), column.getType());
        }
    }

    public void setAttribute(String title, String name, ColumnType type) {
        title_ = (null == title) ? ("") : (title);
        name_ = (null == name) ? ("") : (name);
        type_ = (null == type) ? (ColumnType.TEXT) : (type);
    }
}
