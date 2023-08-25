package com.biit.infographic.core.models.svg.components.text;

public enum TextAlign {
    RIGHT("text-align:end;text-anchor:end"),
    CENTER("text-align:center;text-anchor:middle"),
    LEFT(""),
    JUSTIFY("");

    private final String style;

    TextAlign(String style) {
        this.style = style;
    }

    public String getStyle() {
        return style;
    }
}
