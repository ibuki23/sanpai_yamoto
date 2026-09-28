package aks.industrialwaste.database;

import java.io.Serializable;

public class SortCondition implements Serializable {

    private Column column_;
    private SortOrder order_;

    public SortCondition() {
        this(null, SortOrder.ASC);
    }

    public SortCondition(Column column, SortOrder order) {
        setColumn(column);
        setOrder(order);
    }

    public Column getColumn() {
        return column_;
    }

    public SortOrder getOrder() {
        return order_;
    }

    public void setColumn(Column column) {
        column_ = (null == column) ? (new Column()) : (column);
    }

    public void setOrder(SortOrder order) {
        order_ = (null == order) ? (SortOrder.ASC) : (order);
    }
}
