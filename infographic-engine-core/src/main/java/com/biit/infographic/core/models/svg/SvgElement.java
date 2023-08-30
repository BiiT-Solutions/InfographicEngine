package com.biit.infographic.core.models.svg;

import com.biit.infographic.core.models.svg.components.StrokeLineCap;
import com.biit.infographic.core.models.svg.exceptions.InvalidAttributeException;
import com.biit.infographic.core.models.svg.serialization.SvgElementDeserializer;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import org.w3c.dom.Element;

import java.util.stream.Collectors;

@JsonDeserialize(using = SvgElementDeserializer.class)
//@JsonSerialize(using = SvgElementSerializer.class)
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

    @JsonProperty("stroke")
    private ElementStroke elementStroke;

    public SvgElement() {
        setElementAttributes(new ElementAttributes());
        setElementStroke(new ElementStroke());
    }

    public SvgElement(String width, String height, String fill) {
        setElementAttributes(new ElementAttributes(width, height, fill));
        setElementStroke(new ElementStroke());
    }

    public SvgElement(Long xCoordinate, Long yCoordinate, String width, String height, String fill) {
        setElementAttributes(new ElementAttributes(xCoordinate, yCoordinate, width, height, fill));
        setElementStroke(new ElementStroke());
    }

    public SvgElement(ElementAttributes elementAttributes) {
        setElementAttributes(elementAttributes);
        setElementStroke(new ElementStroke());
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

    public ElementStroke getElementStroke() {
        return elementStroke;
    }

    public void setElementStroke(ElementStroke elementStroke) {
        this.elementStroke = elementStroke;
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

    public void elementStroke(Element element) throws InvalidAttributeException {
        if (elementStroke != null) {
            if (elementStroke.getStrokeWidth() > 0.0) {
                element.setAttributeNS(null, "stroke", elementStroke.getStrokeColor());
                element.setAttributeNS(null, "stroke-width", String.valueOf(elementStroke.getStrokeWidth()));
            }
            if (elementStroke.getStrokeDash() != null && !elementStroke.getStrokeDash().isEmpty()) {
                element.setAttributeNS(null, "stroke-dasharray", elementStroke.getStrokeDash().stream().map(String::valueOf)
                        .collect(Collectors.joining(" ")));
            }
            if (elementStroke.getLineCap() != null && elementStroke.getLineCap() != StrokeLineCap.BUTT) {
                element.setAttributeNS(null, "style", "stroke-linecap:" + elementStroke.getLineCap().value());
            }
        }
    }
}
