package com.biit.infographic.core.models.svg.clip;

public enum ClipDirection {
    LEFT_TO_RIGHT,
    RIGHT_TO_LEFT,
    TOP_TO_BOTTOM,
    BOTTOM_TO_TOP;

    public static ClipDirection fromString(String value) {
        for (ClipDirection direction : values()) {
            if (direction.name().equalsIgnoreCase(value)) {
                return direction;
            }
        }
        return null;
    }
}
