package aks.industrialwaste.database;

public enum CompareOperator {
    LT("<"),
    LE("<="),
    EQ("="),
    GE(">="),
    GT(">"),
    NT("<>"),
    LIKE("like"),
    IN("= ANY (ARRAY[");

    private String operator_;

    CompareOperator(String s) {
        operator_ = s;
    }

    public String getOperator() {
        return operator_;
    }
}
