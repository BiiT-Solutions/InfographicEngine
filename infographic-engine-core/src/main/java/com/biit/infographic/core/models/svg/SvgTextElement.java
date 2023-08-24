package com.biit.infographic.core.models.svg;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonRootName;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

@JsonRootName(value = "element")
@JsonPropertyOrder(alphabetic = true)
@JsonIgnoreProperties(ignoreUnknown = true)
public class SvgTextElement extends SvgElement {

    @JsonProperty("contentText")
    private String content;

    @JsonProperty("maxLineSize")
    private int maxLineLength;

    @JsonProperty("fillColor")
    private String fillColor;

    @Override
    public Element generateSvg(Document doc) {
        return null;
    }
}
