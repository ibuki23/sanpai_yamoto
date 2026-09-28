package aks.industrialwaste.database;

public enum CombineOperator {
    NONE(""),
    NONE2("] "),
    AND("and"),
    OR("or"),
    OR2("]) or");

    private String operator_;

    CombineOperator(String s) {
        operator_ = s;
    }

    public String getOperator() {
        return operator_;
    }
}
