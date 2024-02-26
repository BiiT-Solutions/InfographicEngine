package com.biit.infographic.core.models.svg.components.text;

public enum FontStyle {
    NORMAL("normal", "font-style:normal;"),
    ITALIC("italic", "font-style:italic;");

    private final String name;
    private final String style;

    FontStyle(String name, String style) {
        this.name = name;
        this.style = style;
    }

    public String getName() {
        return name;
    }

    public String getStyle() {
        return style;
    }

    public static FontStyle getStyle(String parameter) {
        if (parameter != null) {
            for (FontStyle fontStyle : FontStyle.values()) {
                if (fontStyle.name().equalsIgnoreCase(parameter)) {
                    return fontStyle;
                }
            }
        }
        return FontStyle.NORMAL;
    }
}
