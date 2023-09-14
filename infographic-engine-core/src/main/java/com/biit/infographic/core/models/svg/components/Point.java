package com.biit.infographic.core.models.svg.components;

import com.biit.infographic.core.models.svg.serialization.PointDeserializer;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@JsonDeserialize(using = PointDeserializer.class)
@JsonRootName(value = "point")
public class Point {

    @JsonProperty("x")
    private Long x;

    @JsonProperty("y")
    private Long y;

    public Point() {
        super();
    }

    public Point(Long x, Long y) {
        this();
        this.x = x;
        this.y = y;
    }

    public Long getX() {
        return x;
    }

    public void setX(Long x) {
        this.x = x;
    }

    public Long getY() {
        return y;
    }

    public void setY(Long y) {
        this.y = y;
    }
}
