package com.biit.infographic.core.models.svg.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class Color {
    private static final String BASIC_COLOR_VALIDATION = "^#[0-9A-Fa-f]{6}$";
    private static final String TRANSPARENT_COLOR_VALIDATION = "^#[0-9A-Fa-f]{6}[0-9A-Fa-f]{0,2}$";

    public static final int COLOR_WITH_TRANSPARENCY_LENGTH = 8;
    private static final int HEX_VALUES = 16;
    private static final int RGB_VALUES = 256;

    private static final Pattern HEXADECIMAL_PATTERN = Pattern.compile("\\p{XDigit}+");

    private Color() {

    }

    public static boolean isValidWithTransparency(String color) {
        if (color != null) {
            return color.matches(TRANSPARENT_COLOR_VALIDATION);
        }
        return true;
    }

    public static boolean isValidWithoutTransparency(String color) {
        if (color != null) {
            return color.matches(BASIC_COLOR_VALIDATION);
        }
        return true;
    }

    private static boolean isHexadecimal(String input) {
        final Matcher matcher = HEXADECIMAL_PATTERN.matcher(input);
        return matcher.matches();
    }

    public static String checkColor(String color) {
        if (color != null && isHexadecimal(color) && !color.startsWith("#")) {
            color = "#" + color;
        }
        return color;
    }

    public static Double getOpacity(String color) {
        if (color.length() < COLOR_WITH_TRANSPARENCY_LENGTH) {
            return null;
        }
        return ((double) Integer.parseInt(color.substring(color.length() - 2), HEX_VALUES))
                / RGB_VALUES;
    }
}
