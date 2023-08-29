package com.biit.infographic.core.models.svg;

public enum Unit {
    PERCENTAGE("%", "%"),
    PIXELS("px", "");

    private final String tag;

    private final String value;

    Unit(String tag, String value) {
        this.tag = tag;
        this.value = value;
    }

    public String getTag() {
        return tag;
    }

    public String getValue() {
        return value;
    }

    public static Long getValue(String parameter) {
        if (parameter == null) {
            return null;
        }
        return Long.parseLong(parameter.replaceAll("[^\\d.]", ""));
    }

    public static Unit getUnit(String parameter) {
        if (parameter != null) {
            for (Unit unit : Unit.values()) {
                if (parameter.endsWith(unit.getTag())) {
                    return unit;
                }
            }
        }
        return Unit.PIXELS;
    }
}
