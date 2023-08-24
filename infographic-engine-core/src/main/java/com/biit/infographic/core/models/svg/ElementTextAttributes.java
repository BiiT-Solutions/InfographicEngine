package com.biit.infographic.core.models.svg;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;

@JsonRootName(value = "attributes")
public class ElementTextAttributes extends ElementAttributes {

    @JsonProperty("font-family")
    private String fontFamily;

    @JsonProperty("font-size")
    private int fontSize;

}
