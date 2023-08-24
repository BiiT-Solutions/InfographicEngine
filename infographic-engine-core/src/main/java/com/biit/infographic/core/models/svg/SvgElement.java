package com.biit.infographic.core.models.svg;

import com.biit.infographic.core.models.svg.exceptions.InvalidAttributeException;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonRootName;

@JsonRootName(value = "element")
@JsonPropertyOrder(alphabetic = true)
@JsonIgnoreProperties(ignoreUnknown = true)
public abstract class SvgElement implements ISvgElement {

    @JsonProperty("id")
    private String id;

    @JsonProperty("type")
    private ElementType elementType;

    @JsonProperty("attributes")
    private ElementAttributes elementAttributes;

    public SvgElement() {
        setElementAttributes(new ElementAttributes());
    }

    public SvgElement(String width, String height, String fill) {
        setElementAttributes(new ElementAttributes(width, height, fill));
    }

    public SvgElement(Long xCoordinate, Long yCoordinate, String width, String height, String fill) {
        setElementAttributes(new ElementAttributes(xCoordinate, yCoordinate, width, height, fill));
    }

    public SvgElement(ElementAttributes elementAttributes) {
        setElementAttributes(elementAttributes);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public ElementType getElementType() {
        return elementType;
    }

    public void setElementType(ElementType elementType) {
        this.elementType = elementType;
    }

    public ElementAttributes getElementAttributes() {
        return elementAttributes;
    }

    public void setElementAttributes(ElementAttributes elementAttributes) {
        this.elementAttributes = elementAttributes;
    }

    public void validateAttributes() throws InvalidAttributeException {
        //Each child must implement and filter invalid attributes.
    }
}
