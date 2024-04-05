package com.biit.infographic.core.models.svg.components;

import com.biit.infographic.core.models.svg.serialization.ArcDeserializer;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@JsonDeserialize(using = ArcDeserializer.class)
@JsonRootName(value = "arc")
public class Arc implements PathElement {

    public static final String ELEMENT_NAME = "ARC";

    @JsonProperty("element")
    private final String element = ELEMENT_NAME;

    @JsonProperty("x")
    private Long x;

    @JsonProperty("y")
    private Long y;

    public Arc() {
        super();
    }

    public Arc(Number x, Number y) {
        this.x = x != null ? x.longValue() : null;
        this.y = y != null ? y.longValue() : null;
    }

    public Arc(Long x, Long y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public Long getX() {
        return x;
    }

    public void setX(Long x) {
        this.x = x;
    }

    @Override
    public Long getY() {
        return y;
    }

    public void setY(Long y) {
        this.y = y;
    }

    /**
     *
     * @param previousX
     * @param previousY
     * @return
     */
    @Override
    public String generateCoordinate(Long previousX, Long previousY) {
        return "A "
                + (previousX != null ? getX() - previousX : getX())
                + " "
                + (previousY != null ? getY() - previousY : getY())
                + " 0 0 0 "
                + getX()
                + " "
                + getY()
                + " ";
    }

    public String getElement() {
        return element;
    }
}
