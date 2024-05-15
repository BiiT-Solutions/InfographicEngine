package com.biit.infographic.core.models.svg.components.path;

import com.biit.infographic.core.models.svg.ElementType;
import com.biit.infographic.core.models.svg.serialization.VerticalLineDeserializer;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@JsonDeserialize(using = VerticalLineDeserializer.class)
@JsonRootName(value = "verticalLine")
public class VerticalLine extends PathElement {

    @JsonProperty("y")
    private Long y;

    public VerticalLine() {
        super();
        setElementType(ElementType.VERTICAL_LINE);
    }

    public VerticalLine(Number y) {
        this(y, false);
    }

    public VerticalLine(Long y) {
        this(y, false);
    }

    public VerticalLine(Number y, boolean relativeCoordinates) {
        this(y != null ? y.longValue() : null, relativeCoordinates);
    }

    public VerticalLine(Long y, boolean relativeCoordinates) {
        this();
        this.y = y;
        setRelativeCoordinates(relativeCoordinates);
    }

    @Override
    public Long getX() {
        return null;
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
        return (isRelativeCoordinates() ? "v " : "V ") + getY() + " ";
    }

}
