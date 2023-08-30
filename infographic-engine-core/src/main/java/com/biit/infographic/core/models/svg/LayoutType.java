package com.biit.infographic.core.models.svg;

public enum LayoutType {
    GRID_LAYOUT;

    public static LayoutType getType(String parameter) {
        if (parameter != null) {
            for (LayoutType type : LayoutType.values()) {
                if (type.name().equalsIgnoreCase(parameter)) {
                    return type;
                }
            }
        }
        return LayoutType.GRID_LAYOUT;
    }
}
