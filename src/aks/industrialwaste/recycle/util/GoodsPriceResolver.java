package aks.industrialwaste.recycle.util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

/**
 * 品目マスタから料金区分・アスベスト有無に応じた単価を決定する。
 */
public final class GoodsPriceResolver {

    public static final int PRICE_CATEGORY_NORMAL = 0;
    public static final int PRICE_CATEGORY_NEW = 1;
    public static final int PRICE_CATEGORY_EXISTING = 2;
    public static final int PRICE_CATEGORY_OTHER = 3;

    private GoodsPriceResolver() {
    }

    public static int resolve(Map<String, Object> goods, int priceCategory, boolean asbestos) {
        if (goods == null) {
            return 0;
        }

        String column = resolveColumn(priceCategory, asbestos);
        Object value = goods.get(column);
        return value instanceof Number ? ((Number) value).intValue() : 0;
    }

    public static int resolve(Connection con, int goodsId, int priceCategory, boolean asbestos)
             throws SQLException {
        String sql = "select unit_value, unit_value_new, unit_value_existing, unit_value_other, "
                 + "asbestos_unit_value_new, asbestos_unit_value_existing, asbestos_unit_value_other "
                 + "from m_goods_kind where id = ? and delete_flag = false order by sno limit 1";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, goodsId);
            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) {
                    return 0;
                }
                return rs.getInt(resolveColumn(priceCategory, asbestos));
            }
        }
    }

    public static String resolveColumn(int priceCategory, boolean asbestos) {
        if (!asbestos) {
            switch (priceCategory) {
                case PRICE_CATEGORY_NEW:
                    return "unit_value_new";
                case PRICE_CATEGORY_EXISTING:
                    return "unit_value_existing";
                case PRICE_CATEGORY_OTHER:
                    return "unit_value_other";
                case PRICE_CATEGORY_NORMAL:
                default:
                    return "unit_value";
            }
        }

        switch (priceCategory) {
            case PRICE_CATEGORY_NEW:
                return "asbestos_unit_value_new";
            case PRICE_CATEGORY_EXISTING:
                return "asbestos_unit_value_existing";
            case PRICE_CATEGORY_OTHER:
                return "asbestos_unit_value_other";
            case PRICE_CATEGORY_NORMAL:
            default:
                // アスベスト有りで通常区分は業務上使用しない想定。
                // 未設定の区分で誤って通常単価へフォールバックすると過少請求に
                // つながるため、0を返せる存在しないキーを使用する。
                return "asbestos_unit_value_normal";
        }
    }
}
