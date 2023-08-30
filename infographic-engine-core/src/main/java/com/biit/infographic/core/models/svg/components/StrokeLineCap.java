package com.biit.infographic.core.models.svg.components;

public enum StrokeLineCap {
    ROUND,
    BUTT,
    SQUARE;

    public String value() {
        return name().toLowerCase();
    }

    public static StrokeLineCap get(String parameter) {
        if (parameter != null) {
            for (StrokeLineCap strokeLineCap : StrokeLineCap.values()) {
                if (strokeLineCap.name().equalsIgnoreCase(parameter)) {
                    return strokeLineCap;
                }
            }
        }
        return StrokeLineCap.BUTT;
    }
}
