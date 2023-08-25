package com.biit.infographic.core.models.svg;

import com.biit.infographic.core.models.svg.exceptions.InvalidAttributeException;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonRootName;
import org.w3c.dom.Element;

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

    //Each child must implement and filter invalid attributes.
    public abstract void validateAttributes() throws InvalidAttributeException;

    public void elementAttributes(Element element) throws InvalidAttributeException {
        if (getId() != null) {
            element.setAttribute("id", getId());
        }
        if (getElementAttributes().getFill() != null) {
            element.setAttributeNS(null, "fill", getElementAttributes().getFill());
        }
        if (elementAttributes.getCssClass() != null) {
            element.setAttribute("class", elementAttributes.getCssClass());
        }
        if (elementAttributes.getWidth() != null) {
            element.setAttributeNS(null, "width", getElementAttributes().getWidthValue());
        }
        if (elementAttributes.getHeight() != null) {
            element.setAttributeNS(null, "height", getElementAttributes().getHeightValue());
        }
    }
}
