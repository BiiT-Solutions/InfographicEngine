package com.biit.infographic.core.models.svg.components.text;

public enum FontWeight {
    NORMAL("font-weight:normal;"),
    BOLD("font-weight:bold;");

    private final String style;

    FontWeight(String style) {
        this.style = style;
    }

    public String getStyle() {
        return style;
    }

    public static FontWeight getWeight(String parameter) {
        if (parameter != null) {
            for (FontWeight fontWeight : FontWeight.values()) {
                if (fontWeight.name().equalsIgnoreCase(parameter)) {
                    return fontWeight;
                }
            }
        }
        return FontWeight.NORMAL;
    }
}
