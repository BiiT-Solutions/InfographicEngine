package com.biit.infographic.core.models.svg.components.path;

import com.biit.infographic.core.models.svg.ElementType;
import com.biit.infographic.core.models.svg.serialization.HorizontalLineDeserializer;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@JsonDeserialize(using = HorizontalLineDeserializer.class)
@JsonRootName(value = "horizontalLine")
public class HorizontalLine extends PathElement {

    @JsonProperty("x")
    private Long x;

    public HorizontalLine() {
        super();
        setElementType(ElementType.HORIZONTAL_LINE);
    }

    public HorizontalLine(Number x) {
        this(x, false);
    }

    public HorizontalLine(Long x) {
        this(x, false);
    }

    public HorizontalLine(Number x, boolean relativeCoordinates) {
        this(x != null ? x.longValue() : null, relativeCoordinates);
    }

    public HorizontalLine(Long x, boolean relativeCoordinates) {
        this();
        this.x = x;
        setRelativeCoordinates(relativeCoordinates);
    }

    @Override
    public Long getX() {
        return x;
    }

    @Override
    public Long getY() {
        return null;
    }

    public void setX(Long x) {
        this.x = x;
    }

    @Override
    public String generateCoordinate(Long previousX, Long previousY) {
        return (isRelativeCoordinates() ? "h " : "H ") + getX() + " ";
    }
}
