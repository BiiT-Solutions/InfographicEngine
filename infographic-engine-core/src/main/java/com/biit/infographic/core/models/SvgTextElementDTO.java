package com.biit.infographic.core.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonRootName;

@JsonRootName(value = "element")
@JsonPropertyOrder(alphabetic = true)
@JsonIgnoreProperties(ignoreUnknown = true)
public class SvgTextElementDTO extends SvgElementDTO {

    @JsonProperty("contentText")
    private String content;

    @JsonProperty("maxLineSize")
    private int maxLineLength;

    @JsonProperty("fillColor")
    private String fillColor;
}
