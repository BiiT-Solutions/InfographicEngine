package com.biit.infographic.core.models.svg.components.text;

public enum FontWeight {
    NORMAL("font-weight:normal;", "font-weight: 'normal';"),
    BOLD("font-weight:bold;", "font-weight: 'bold';");

    private final String style;
    private final String fontDefinition;

    FontWeight(String style, String fontDefinition) {
        this.style = style;
        this.fontDefinition = fontDefinition;
    }

    public String getStyle() {
        return style;
    }

    public String getFontDefinition() {
        return fontDefinition;
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
