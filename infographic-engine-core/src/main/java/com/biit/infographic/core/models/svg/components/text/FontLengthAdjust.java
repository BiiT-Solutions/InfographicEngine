package com.biit.infographic.core.models.svg.components.text;

public enum FontLengthAdjust {
    SPACING("spacing"),
    SPACING_AND_GLYPHS("spacingAndGlyphs");

    private final String tag;

    FontLengthAdjust(String tag) {
        this.tag = tag;
    }

    public String getTag() {
        return tag;
    }

    public static FontLengthAdjust getLengthAdjust(String parameter) {
        if (parameter != null) {
            for (FontLengthAdjust lengthAdjust : FontLengthAdjust.values()) {
                if (lengthAdjust.name().equalsIgnoreCase(parameter)) {
                    return lengthAdjust;
                }
            }
        }
        return FontLengthAdjust.SPACING;
    }

}
