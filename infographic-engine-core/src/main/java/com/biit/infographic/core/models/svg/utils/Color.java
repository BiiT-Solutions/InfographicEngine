package com.biit.infographic.core.models.svg.utils;

public final class Color {
    private static final String BASIC_COLOR_VALIDATION = "^#[0-9A-F]{6}$";
    private static final String TRANSPARENT_COLOR_VALIDATION = "^#[0-9A-F]{6}[0-9a-f]{0,2}$";

    private Color() {

    }

    public static boolean isValidWithTransparency(String color) {
        return color.matches(TRANSPARENT_COLOR_VALIDATION);
    }

    public static boolean isValidWithoutTransparency(String color) {
        return color.matches(BASIC_COLOR_VALIDATION);
    }
}
