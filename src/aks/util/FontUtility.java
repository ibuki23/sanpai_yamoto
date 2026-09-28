package aks.util;

import java.awt.Font;

public class FontUtility {

    private final static String DEFAULT_FAMILY = "scanserif";
    private final static int DEFAULT_STYLE = Font.PLAIN;
    private final static int DEFAULT_SIZE = 14;

    public static Font getDefaultFont() {
        return new Font(DEFAULT_FAMILY, DEFAULT_STYLE, DEFAULT_SIZE);
    }

    public static Font getFont(String kind, int style, int size) {
        return new Font(kind, style, size);
    }

    public static String getDefaultFamily() {
        return DEFAULT_FAMILY;
    }

    public static int getDefaultStyle() {
        return DEFAULT_STYLE;
    }

    public static int getDefaultSize() {
        return DEFAULT_SIZE;
    }
}
