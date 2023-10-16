package com.biit.infographic.core.models.svg;

import com.biit.infographic.core.models.svg.components.StrokeLineCap;
import com.biit.infographic.core.models.svg.exceptions.InvalidAttributeException;
import com.biit.infographic.core.models.svg.serialization.SvgAreaElementDeserializer;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import org.w3c.dom.Element;

import java.util.stream.Collectors;

@JsonDeserialize(using = SvgAreaElementDeserializer.class)
//@JsonSerialize(using = SvgElementSerializer.class)
public abstract class SvgAreaElement extends SvgElement implements ISvgElement {

    @JsonProperty("commonAttributes")
    private ElementAttributes elementAttributes;

    @JsonProperty("stroke")
    private ElementStroke elementStroke;

    public SvgAreaElement() {
        setElementAttributes(new ElementAttributes());
        setElementStroke(new ElementStroke());
    }

    public SvgAreaElement(String width, String height, String fill) {
        setElementAttributes(new ElementAttributes(width, height, fill));
        setElementStroke(new ElementStroke());
    }

    public SvgAreaElement(Long xCoordinate, Long yCoordinate, String width, String height, String fill) {
        setElementAttributes(new ElementAttributes(xCoordinate, yCoordinate, width, height, fill));
        setElementStroke(new ElementStroke());
    }

    public SvgAreaElement(ElementAttributes elementAttributes) {
        setElementAttributes(elementAttributes);
        setElementStroke(new ElementStroke());
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

    /**
     * Stroke is included on the width and must be subtracted.
     *
     * @return a string format including unit if needed.
     */
    protected String generateRealWidth() {
        return (getElementAttributes().getWidth() - getElementStroke().getStrokeWidth()) + getElementAttributes().getWidthUnit().getValue();
    }

    /**
     * Stroke is included on the width and must be subtracted.
     *
     * @return a string format including unit if needed.
     */
    protected String generateRealHeight() {
        return (getElementAttributes().getHeight() - getElementStroke().getStrokeWidth()) + getElementAttributes().getHeightUnit().getValue();
    }

    /**
     * Stroke is included on the X and must be subtracted.
     *
     * @return the calculated coordinate
     */
    protected Double generateRealXCoordinate() {
        return (getElementAttributes().getXCoordinate() + getElementStroke().getStrokeWidth() / 2);
    }

    /**
     * Stroke is included on the y and must be subtracted.
     *
     * @return the calculated coordinate
     */
    protected Double generateRealYCoordinate() {
        return (getElementAttributes().getYCoordinate() + getElementStroke().getStrokeWidth() / 2);
    }

    public void elementAttributes(Element element) throws InvalidAttributeException {
        super.elementAttributes(element);
        if (getElementAttributes().getFill() != null) {
            element.setAttributeNS(null, "fill", getElementAttributes().getFill());
        } else if (getElementAttributes().getGradient() != null) {
            element.setAttributeNS(null, "fill", "url(#" + getElementAttributes().getGradient().getId() + ")");
        }
        if (getElementAttributes().getFillOpacity() != null) {
            element.setAttributeNS(null, "fill-opacity", String.valueOf(getElementAttributes().getFillOpacity()));
        }
        if (elementAttributes.getCssClass() != null) {
            element.setAttribute("class", elementAttributes.getCssClass());
        }
        if (elementAttributes.getWidth() != null) {
            element.setAttributeNS(null, "width", generateRealWidth());
        }
        if (elementAttributes.getHeight() != null) {
            element.setAttributeNS(null, "height", generateRealHeight());
        }
        final String style = generateStyle(new StringBuilder(getElementAttributes().getStyle())).toString();
        if (!style.isBlank()) {
            element.setAttributeNS(null, "style", style);
        }
    }

    public void elementStroke(Element element) throws InvalidAttributeException {
        if (elementStroke != null) {
            if (elementStroke.getStrokeWidth() > 0.0) {
                element.setAttributeNS(null, "stroke", elementStroke.getStrokeColor());
                element.setAttributeNS(null, "stroke-width", String.valueOf(elementStroke.getStrokeWidth()));
                if (elementStroke.getStrokeOpacity() != null) {
                    element.setAttributeNS(null, "stroke-opacity", String.valueOf(elementStroke.getStrokeOpacity()));
                }
            }
            if (elementStroke.getStrokeDash() != null && !elementStroke.getStrokeDash().isEmpty()) {
                element.setAttributeNS(null, "stroke-dasharray", elementStroke.getStrokeDash().stream().map(String::valueOf)
                        .collect(Collectors.joining(" ")));
            }
        }
    }

    protected StringBuilder generateStyle(StringBuilder style) {
        if (style == null) {
            style = new StringBuilder();
        }
        if (elementStroke.getLineCap() != null && elementStroke.getLineCap() != StrokeLineCap.BUTT) {
            style.append("stroke-linecap:");
            style.append(elementStroke.getLineCap().value());
            style.append(";");
        }
        return style;
    }

    @Override
    public void validateAttributes() throws InvalidAttributeException {
        if (elementAttributes.getFill() != null && elementAttributes.getGradient() != null) {
            throw new InvalidAttributeException(this.getClass(), "Cannot define fill color and gradient on '" + getId() + "'");
        }
    }

}
