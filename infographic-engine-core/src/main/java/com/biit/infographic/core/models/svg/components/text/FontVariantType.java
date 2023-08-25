package com.biit.infographic.core.models.svg.components.text;

public enum FontVariantType {
    NORMAL("normal"),

    NONE("none"),

    SMALL_CAPS("small-caps"),

    ALL_SMALL_CAPS("all-small-caps"),

    TITLING_CAPS("titling-caps"),

    PETITE_CAPS("petite-caps"),

    ALL_PETITE_CAPS("all-petite-caps"),

    UNICASE("unicase"),

    SLASHED_ZERO("slashed-zero");


    private final String tag;

    FontVariantType(String tag) {
        this.tag = tag;
    }

    public String getTag() {
        return tag;
    }
}
