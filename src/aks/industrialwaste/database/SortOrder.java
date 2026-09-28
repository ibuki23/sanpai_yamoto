package aks.industrialwaste.database;

public enum SortOrder {
    ASC("asc"),
    DESC("desc"),
    NULLS_FIRST("is not null desc"),
    NULLS_LAST("is null desc");

    private String order_;

    SortOrder(String s) {
        order_ = s;
    }

    public String getOrder() {
        return order_;
    }
}
