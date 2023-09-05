package com.biit.infographic.core.models.svg;

public enum LayoutType {
    GRID_LAYOUT("gridLayout"),
    FREE_LAYOUT("freeLayout");

    private final String tag;

    LayoutType(String tag) {
        this.tag = tag;
    }

    public String getTag() {
        return tag;
    }

    public static LayoutType getType(String parameter) {
        if (parameter != null) {
            for (LayoutType type : LayoutType.values()) {
                if (type.name().equalsIgnoreCase(parameter) || type.getTag().equalsIgnoreCase(parameter)) {
                    return type;
                }
            }
        }
        return LayoutType.GRID_LAYOUT;
    }
}
