package com.biit.infographic.core.models.svg.components;

import com.biit.infographic.core.models.svg.ElementAttributes;
import com.biit.infographic.core.models.svg.ElementType;
import com.biit.infographic.core.models.svg.StrokeAlign;
import com.biit.infographic.core.models.svg.SvgAreaElement;
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

    public void setRadius(Number radius) {
        this.radius = radius != null ? radius.longValue() : null;
    }

    public void setRadius(Long radius) {
        this.radius = radius;
    }

    @Override
    public Collection<Element> generateSvg(Document doc) {
        validateAttributes();
        final ArrayList<Element> elements = new ArrayList<>();
        final Element circle = doc.createElementNS(NAMESPACE, "circle");
        elements.add(circle);
        circle.setAttributeNS(null, "cx", String.valueOf(generateRealXCoordinate().longValue()));
        circle.setAttributeNS(null, "cy", String.valueOf(generateRealYCoordinate().longValue()));
        circle.setAttributeNS(null, "r", String.valueOf(getRadius()));
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
        if (getElementAttributes().getHeight() != null) {
            throw new InvalidAttributeException(this.getClass(), "Circle '" + getId() + "' must not have 'height' attribute");
        }
        if (getElementAttributes().getWidth() != null) {
            throw new InvalidAttributeException(this.getClass(), "Circle '" + getId() + "' must not have 'width' attribute");
        }
    }

    /**
     * Stroke is included on the X and must be subtracted.
     *
     * @return the calculated coordinate
     */
    @Override
    protected Double generateRealXCoordinate() {
        return (double) (getElementAttributes().getXCoordinate() + getRadius());
    }

    /**
     * Stroke is included on the y and must be subtracted.
     *
     * @return the calculated coordinate
     */
    @Override
    protected Double generateRealYCoordinate() {
        return (double) (getElementAttributes().getYCoordinate() + getRadius());
    }

    private Collection<Element> createOuterStroke(Document doc) {
        final SvgPath border = new SvgPath(
                generateRealXCoordinate().longValue() - getRadius() - (long) (getElementStroke().getStrokeWidth() / 2),
                generateRealYCoordinate().longValue(),
                new Arc(generateRealXCoordinate().longValue(),
                        generateRealYCoordinate().longValue() + getRadius() + (long) (getElementStroke().getStrokeWidth() / 2)),
                new Arc(generateRealXCoordinate().longValue() + getRadius() + (long) (getElementStroke().getStrokeWidth() / 2),
                        generateRealYCoordinate().longValue()),
                new Arc(generateRealXCoordinate().longValue(),
                        generateRealYCoordinate().longValue() - getRadius() - (long) (getElementStroke().getStrokeWidth() / 2)),
                new Arc(generateRealXCoordinate().longValue() - getRadius() - (long) (getElementStroke().getStrokeWidth() / 2),
                        generateRealYCoordinate().longValue()));
        border.setElementStroke(getElementStroke());
        border.getElementStroke().setLineCap(StrokeLineCap.BUTT);
        return border.generateSvg(doc);
    }
}
