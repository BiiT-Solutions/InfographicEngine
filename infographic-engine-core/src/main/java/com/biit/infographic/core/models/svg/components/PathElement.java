package com.biit.infographic.core.models.svg.components;

public interface PathElement {
    Long getX();

    Long getY();

    String generateCoordinate(Long previousX, Long previousY);
}
