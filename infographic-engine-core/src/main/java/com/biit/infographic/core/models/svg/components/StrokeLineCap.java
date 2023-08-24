package com.biit.infographic.core.models.svg.components;

public enum StrokeLineCap {
    ROUND,
    BUTT,
    SQUARE;

    public String value() {
        return name().toLowerCase();
    }
}
