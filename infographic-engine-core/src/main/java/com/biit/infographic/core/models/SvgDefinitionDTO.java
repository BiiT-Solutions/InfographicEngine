package com.biit.infographic.core.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;

import java.util.List;

@JsonRootName(value = "report")
public class SvgDefinitionDTO {
    @JsonProperty("width")
    private long width;

    @JsonProperty("height")
    private long height;

    @JsonProperty("background")
    private SvgBackgroundDTO svgBackgroundDTO;

    @JsonProperty("type")
    private LayoutType layoutType;

    @JsonProperty("numColumns")
    private int columns;

    @JsonProperty("numRows")
    private int rows;

    @JsonProperty("elements")
    private List<SvgElementDTO> elements;

    @JsonProperty("content")
    private List<SvgElementDTO> content;
}
