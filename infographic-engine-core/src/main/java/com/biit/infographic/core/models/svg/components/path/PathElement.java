package com.biit.infographic.core.models.svg.components.path;

import com.biit.infographic.core.models.svg.ElementType;
import com.fasterxml.jackson.annotation.JsonProperty;

public abstract class PathElement {

    @JsonProperty("elementType")
    private ElementType elementType;

    @JsonProperty("relativeCoordinates")
    private boolean relativeCoordinates;

    protected PathElement() {
        this.relativeCoordinates = false;
    }

    public ElementType getElementType() {
        return elementType;
    }

    public void setElementType(ElementType elementType) {
        this.elementType = elementType;
    }

    public boolean isRelativeCoordinates() {
        return relativeCoordinates;
    }

    public void setRelativeCoordinates(boolean relativeCoordinates) {
        this.relativeCoordinates = relativeCoordinates;
    }

    public abstract Long getX();

    public abstract Long getY();

    public abstract String generateCoordinate(Long previousX, Long previousY);
}
