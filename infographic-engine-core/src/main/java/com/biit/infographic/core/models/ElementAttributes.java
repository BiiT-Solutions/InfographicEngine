package com.biit.infographic.core.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;

@JsonRootName(value = "attributes")
public class ElementAttributes {

    @JsonProperty("width")
    private long width;

    @JsonProperty("height")
    private long height;

    @JsonProperty("x")
    private long xCoordinate;

    @JsonProperty("y")
    private long yCoordinate;

    @JsonProperty("style")
    private String style;

    @JsonProperty("verticalAlign")
    private VerticalAlignment verticalAlignment;
}
