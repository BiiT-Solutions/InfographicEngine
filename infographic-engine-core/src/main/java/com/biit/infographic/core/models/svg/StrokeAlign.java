package com.biit.infographic.core.models.svg;

public enum StrokeAlign {
    /**
     * Default.
     */
    CENTER,
    /**
     * Only working with circle and rectangle.
     */
    OUTSET,
    /**
     * No implemented yet.
     */
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
