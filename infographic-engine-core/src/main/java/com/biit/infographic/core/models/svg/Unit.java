package com.biit.infographic.core.models.svg;

public enum Unit {
    PERCENTAGE("%"),
    PIXELS("");

    private final String tag;

    Unit(String tag) {
        this.tag = tag;
    }

    public String getTag() {
        return tag;
    }
}
