package com.biit.infographic.core.models.svg.components;

import com.biit.infographic.core.models.svg.ElementAttributes;
import com.biit.infographic.core.models.svg.ElementType;
import com.biit.infographic.core.models.svg.StrokeAlign;
import com.biit.infographic.core.models.svg.SvgAreaElement;
import com.biit.infographic.core.models.svg.components.path.Arc;
import com.biit.infographic.core.models.svg.exceptions.InvalidAttributeException;
import com.biit.infographic.core.models.svg.serialization.SvgCircleDeserializer;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.util.ArrayList;
import java.util.Collection;

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

    public SvgCircle(Number xCoordinate, Number yCoordinate, Number radius) {
        this(xCoordinate != null ? xCoordinate.longValue() : null, yCoordinate != null ? yCoordinate.longValue() : null,
                radius != null ? radius.longValue() : null);
    }

    public SvgCircle(Long xCoordinate, Long yCoordinate, Long radius) {
        this(new ElementAttributes(xCoordinate, yCoordinate, null, null, null));
        setRadius(radius);
    }

    public Long getRadius() {
        return radius;
    }

    public Long getRealRadius() {
        return (long) (radius
                - (getElementStroke().getStrokeAlign() == StrokeAlign.OUTSET ? getElementStroke().getStrokeWidth() : getElementStroke().getStrokeWidth() / 2));
    }

    public void setRadius(Number radius) {
        this.radius = radius != null ? radius.longValue() : null;
    }

    public void setRadius(Long radius) {
        this.radius = radius;
        getElementAttributes().setHeight(radius * 2);
        getElementAttributes().setWidth(radius * 2);
    }

    @Override
    public Collection<Element> generateSvg(Document doc) {
        validateAttributes();
        final ArrayList<Element> elements = new ArrayList<>();
        final Element circle = doc.createElementNS(NAMESPACE, "circle");
        elements.add(circle);
        circle.setAttributeNS(null, "cx", String.valueOf(getElementAttributes().getXCoordinate() + getRadius()));
        circle.setAttributeNS(null, "cy", String.valueOf(getElementAttributes().getYCoordinate() + getRadius()));
        circle.setAttributeNS(null, "r", String.valueOf(getRealRadius()));
        if (getElementStroke() != null && getElementStroke().getStrokeAlign() == StrokeAlign.OUTSET) {
            elements.addAll(createOuterStroke(doc));
        } else {
            elementStroke(circle);
        }
        elementAttributes(circle);
        return elements;
    }

    @Override
    public void validateAttributes() throws InvalidAttributeException {
        super.validateAttributes();
        if (radius == null || radius == 0) {
            throw new InvalidAttributeException(this.getClass(), "Invalid radius on circle '" + getId() + "'");
        }
        getElementAttributes().setHeight(getRadius() * 2);
        getElementAttributes().setWidth(getRadius() * 2);
    }

    private Collection<Element> createOuterStroke(Document doc) {
        final SvgPath border = new SvgPath(getElementAttributes().getXCoordinate() + (long) (getElementStroke().getStrokeWidth() / 2),
                getElementAttributes().getYCoordinate() + getRadius(),
                new Arc(getElementAttributes().getXCoordinate() + getRadius(),
                        getElementAttributes().getYCoordinate() + (2 * getRadius()) - (long) (getElementStroke().getStrokeWidth() / 2)),
                new Arc(getElementAttributes().getXCoordinate() + (2 * getRadius()) - (long) (getElementStroke().getStrokeWidth() / 2),
                        getElementAttributes().getYCoordinate() + getRadius()),
                new Arc(getElementAttributes().getXCoordinate() + getRadius(),
                        getElementAttributes().getYCoordinate() + getElementStroke().getStrokeWidth() / 2),
                new Arc(getElementAttributes().getXCoordinate() + (long) (getElementStroke().getStrokeWidth() / 2),
                        getElementAttributes().getYCoordinate() + getRadius()));
        border.setElementStroke(getElementStroke());
        border.getElementStroke().setLineCap(StrokeLineCap.BUTT);
        if (getElementStroke() != null && getElementStroke().getGradient() != null) {
            border.getElementAttributes().setFill(null);
            border.setGradient(getElementStroke().getGradient());
        }
        return border.generateSvg(doc);
    }
}
