package com.biit.infographic.core.models.svg;

public enum StrokeAlign {
    CENTER,
    OUTSET,
    INSET;

    public static StrokeAlign get(String parameter) {
        if (parameter != null) {
            for (StrokeAlign strokeLineCap : StrokeAlign.values()) {
                if (strokeLineCap.name().equalsIgnoreCase(parameter)) {
                    return strokeLineCap;
                }
            }
        }
        return StrokeAlign.CENTER;
    }
}
