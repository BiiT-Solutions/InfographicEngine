package com.biit.infographic.core.models.svg.components;

import com.biit.infographic.core.models.svg.serialization.PointDeserializer;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@JsonDeserialize(using = PointDeserializer.class)
@JsonRootName(value = "point")
public class Point implements PathElement {
    public static final String ELEMENT_NAME = "POINT";

    @JsonProperty("element")
    private final String element = ELEMENT_NAME;

    @JsonProperty("x")
    private Long x;

    @JsonProperty("y")
    private Long y;

    public Point() {
        super();
    }

    public Point(Number x, Number y) {
        this(x != null ? x.longValue() : null, y != null ? y.longValue() : null);
    }

    public Point(Long x, Long y) {
        this();
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

    @Override
    public String generateCoordinate(Long previousX, Long previousY) {
        return "L "
                + getX()
                + " "
                + getY()
                + " ";
    }

    public String getElement() {
        return element;
    }
}
