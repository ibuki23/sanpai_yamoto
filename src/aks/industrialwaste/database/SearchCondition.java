package aks.industrialwaste.database;

import java.io.Serializable;

public class SearchCondition implements Serializable {

    private Column column_;
    private CompareOperator compareOperator_;
    private Object value_;
    private CombineOperator combineOperator_;

    public SearchCondition() {
        this(null, CompareOperator.EQ, null, CombineOperator.NONE);
    }

    public SearchCondition(
             Column column,
             CompareOperator compareOperator,
             Object value,
             CombineOperator combineOperator) {
        setColumn(column);
        setCompareOperator(compareOperator);
        setValue(value);
        setCombineOperator(combineOperator);
    }

    public Column getColumn() {
        return column_;
    }

    public CombineOperator getCombineOperator() {
        return combineOperator_;
    }

    public CompareOperator getCompareOperator() {
        return compareOperator_;
    }

    public Object getValue() {
        return value_;
    }

    public void setColumn(Column column) {
        column_ = (null == column) ? (new Column()) : (column);
    }

    public void setCombineOperator(CombineOperator operator) {
        combineOperator_ = (null == operator) ? (CombineOperator.NONE) : (operator);
    }

    public void setCompareOperator(CompareOperator operator) {
        compareOperator_ = (null == operator) ? (CompareOperator.EQ) : (operator);
    }

    public void setValue(Object o) {
        value_ = o;
    }
}
