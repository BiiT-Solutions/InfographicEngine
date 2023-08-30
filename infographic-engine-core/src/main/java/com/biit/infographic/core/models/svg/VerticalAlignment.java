package com.biit.infographic.core.models.svg;

public enum VerticalAlignment {
    MIDDLE;

    public static VerticalAlignment get(String parameter) {
        if (parameter != null) {
            for (VerticalAlignment alignment : VerticalAlignment.values()) {
                if (alignment.name().equalsIgnoreCase(parameter)) {
                    return alignment;
                }
            }
        }
        return VerticalAlignment.MIDDLE;
    }
}
