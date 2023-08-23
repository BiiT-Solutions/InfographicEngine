package com.biit.infographic.core.models;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonRootName;

@JsonRootName(value = "background")
@JsonPropertyOrder(alphabetic = true)
@JsonIgnoreProperties(ignoreUnknown = true)
public class SvgBackgroundDTO {
    @JsonProperty("backgroundColor")
    private String backgroundColor;

    //As Base64.
    @JsonProperty("image")
    private String image;
}
