package com.biit.infographic.core.models.svg.components;

import com.biit.infographic.core.models.svg.ElementAttributes;
import com.biit.infographic.core.models.svg.ElementType;
import com.biit.infographic.core.models.svg.SvgAreaElement;
import com.biit.infographic.core.models.svg.exceptions.InvalidAttributeException;
import com.biit.infographic.core.models.svg.serialization.SvgCircleDeserializer;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

@JsonDeserialize(using = SvgCircleDeserializer.class)
@JsonRootName(value = "circle")
public class SvgCircle extends SvgAreaElement {

    @JsonProperty("radius")
    private Long radius;

    public SvgCircle(ElementAttributes elementAttributes) {
        super(elementAttributes);
        setElementType(ElementType.CIRCLE);
    }

    public SvgCircle() {
        this(new ElementAttributes());
    }

    public SvgCircle(String width, String height, String fill) {
        this(new ElementAttributes(width, height, fill));
    }

    public SvgCircle(Long xCoordinate, Long yCoordinate, Long radius) {
        this(new ElementAttributes(xCoordinate, yCoordinate, null, null, null));
        setRadius(radius);
    }

    public Long getRadius() {
        return radius;
    }

    public void setRadius(Long radius) {
        this.radius = radius;
    }

    @Override
    public Element generateSvg(Document doc) {
        validateAttributes();
        final Element circle = doc.createElementNS(NAMESPACE, "circle");
        circle.setAttributeNS(null, "cx", String.valueOf(getElementAttributes().getXCoordinate() + getRadius()));
        circle.setAttributeNS(null, "cy", String.valueOf(getElementAttributes().getYCoordinate() + getRadius()));
        circle.setAttributeNS(null, "r", String.valueOf(radius));
        elementStroke(circle);
        elementAttributes(circle);
        return circle;
    }

    @Override
    public void validateAttributes() throws InvalidAttributeException {
        super.validateAttributes();
        if (radius == null || radius == 0) {
            throw new InvalidAttributeException(this.getClass(), "Invalid radius on circle '" + getId() + "'");
        }
        if (getElementAttributes().getHeight() != null) {
            throw new InvalidAttributeException(this.getClass(), "Circle '" + getId() + "' must not have 'height' attribute");
        }
        if (getElementAttributes().getWidth() != null) {
            throw new InvalidAttributeException(this.getClass(), "Circle '" + getId() + "' must not have 'width' attribute");
        }
    }
}
