package com.biit.infographic.core.models.svg.clip;

public enum ClipType {
    RECTANGLE(SvgRectangleClipPath.class);

    private final Class<? extends SvgClipPath> type;

    ClipType(Class<? extends SvgClipPath> type) {
        this.type = type;
    }

    public Class<? extends SvgClipPath> getType() {
        return type;
    }

    public static ClipType fromString(String value) {
        for (ClipType type : values()) {
            if (type.name().equalsIgnoreCase(value)) {
                return type;
            }
        }
        return null;
    }
}
