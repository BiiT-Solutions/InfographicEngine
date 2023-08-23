package com.biit.infographic.core.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonRootName;

@JsonRootName(value = "element")
@JsonPropertyOrder(alphabetic = true)
@JsonIgnoreProperties(ignoreUnknown = true)
public class SvgElementDTO {

    @JsonProperty("id")
    private String id;

    @JsonProperty("type")
    private ElementType elementType;

    @JsonProperty("attributes")
    private ElementAttributes elementAttributes;
}
