package com.biit.infographic.core.models.svg.components;

import com.biit.infographic.core.models.svg.ElementAttributes;
import com.biit.infographic.core.models.svg.ElementType;
import com.biit.infographic.core.models.svg.StrokeAlign;
import com.biit.infographic.core.models.svg.SvgAreaElement;
import com.biit.infographic.core.models.svg.exceptions.InvalidAttributeException;
import com.biit.infographic.core.models.svg.serialization.SvgRectangleDeserializer;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.util.ArrayList;
import java.util.Collection;

@JsonDeserialize(using = SvgRectangleDeserializer.class)
@JsonRootName(value = "rectangle")
public class SvgRectangle extends SvgAreaElement {

    @JsonProperty("xRadius")
    private Long xRadius = 0L;

    @JsonProperty("yRadius")
    private Long yRadius = 0L;

    public SvgRectangle(ElementAttributes elementAttributes) {
        super(elementAttributes);
        setElementType(ElementType.RECTANGLE);
    }

    public SvgRectangle() {
        this(new ElementAttributes());
    }

    public SvgRectangle(String width, String height, String fill) {
        this(new ElementAttributes(width, height, fill));
    }

    public SvgRectangle(Number xCoordinate, Number yCoordinate, String width, String height, String fill) {
        this(xCoordinate != null ? xCoordinate.longValue() : null, yCoordinate != null ? yCoordinate.longValue() : null, width, height, fill);
    }

    public SvgRectangle(Long xCoordinate, Long yCoordinate, String width, String height, String fill) {
        this(new ElementAttributes(xCoordinate, yCoordinate, width, height, fill));
    }

    public SvgRectangle(Number xCoordinate, Number yCoordinate, Number width, Number height) {
        this(xCoordinate != null ? xCoordinate.longValue() : null, yCoordinate != null ? yCoordinate.longValue() : null,
                width != null ? width.longValue() : null, height != null ? height.longValue() : null);
    }

    public SvgRectangle(Long xCoordinate, Long yCoordinate, Long width, Long height) {
        this(new ElementAttributes(xCoordinate, yCoordinate, String.valueOf(width), String.valueOf(height)));
    }

    public SvgRectangle(Number xCoordinate, Number yCoordinate, Number width, Number height, String fill) {
        this(xCoordinate != null ? xCoordinate.longValue() : null, yCoordinate != null ? yCoordinate.longValue() : null,
                width != null ? width.longValue() : null, height != null ? height.longValue() : null, fill);
    }

    public SvgRectangle(Long xCoordinate, Long yCoordinate, Long width, Long height, String fill) {
        this(new ElementAttributes(xCoordinate, yCoordinate, String.valueOf(width), String.valueOf(height), fill));
    }

    public SvgRectangle(Number xCoordinate, Number yCoordinate, Number width, Number height, String fill, Double fillOpacity) {
        this(xCoordinate != null ? xCoordinate.longValue() : null, yCoordinate != null ? yCoordinate.longValue() : null,
                width != null ? width.longValue() : null, height != null ? height.longValue() : null, fill, fillOpacity);
    }

    public SvgRectangle(Long xCoordinate, Long yCoordinate, Long width, Long height, String fill, Double fillOpacity) {
        this(new ElementAttributes(xCoordinate, yCoordinate, String.valueOf(width), String.valueOf(height), fill, fillOpacity));
    }

    public Long getXRadius() {
        return xRadius;
    }

    public void setXRadius(Number xRadius) {
        this.xRadius = xRadius != null ? xRadius.longValue() : null;
    }

    public void setXRadius(Long xRadius) {
        this.xRadius = xRadius;
    }

    public Long getYRadius() {
        return yRadius;
    }

    public void setYRadius(Number yRadius) {
        this.yRadius = yRadius != null ? yRadius.longValue() : null;
    }

    public void setYRadius(Long yRadius) {
        this.yRadius = yRadius;
    }

    @Override
    public Collection<Element> generateSvg(Document doc) {
        validateAttributes();
        final ArrayList<Element> elements = new ArrayList<>();
        final Element rectangle = doc.createElementNS(NAMESPACE, "rect");
        elements.add(rectangle);
        rectangle.setAttributeNS(null, "x", String.valueOf(generateRealXCoordinate()));
        rectangle.setAttributeNS(null, "y", String.valueOf(generateRealYCoordinate()));
        if (xRadius != null && xRadius != 0) {
            rectangle.setAttributeNS(null, "rx", String.valueOf(getXRadius()));
        }
        if (yRadius != null && yRadius != 0) {
            rectangle.setAttributeNS(null, "ry", String.valueOf(getYRadius()));
        }
        if (getElementStroke() != null && getElementStroke().getStrokeAlign() == StrokeAlign.OUTSET) {
            elements.addAll(createOuterStroke(doc));
        } else {
            elementStroke(rectangle);
        }
        elementAttributes(rectangle);
        return elements;
    }

    @Override
    public void validateAttributes() throws InvalidAttributeException {
        super.validateAttributes();
        if (getElementAttributes().getHeight() == null) {
            throw new InvalidAttributeException(this.getClass(), "Rectangle '" + getId() + "' does not have 'height' attribute");
        }
        if (getElementAttributes().getWidth() == null) {
            throw new InvalidAttributeException(this.getClass(), "Rectangle '" + getId() + "' does not have 'width' attribute");
        }
    }

    private Collection<Element> createOuterStroke(Document doc) {
        final SvgPath border = new SvgPath(
                (long) (generateRealXCoordinate().longValue() - getElementStroke().getStrokeWidth() / 2),
                (long) (generateRealYCoordinate().longValue() - getElementStroke().getStrokeWidth() / 2),
                new Point((long) (generateRealXCoordinate() - getElementStroke().getStrokeWidth() / 2),
                        (long) (generateRealYCoordinate().longValue() + getElementAttributes().getHeight() - (getElementStroke().getStrokeWidth() * 3) / 2)),
                new Point((long) (generateRealXCoordinate().longValue() + getElementAttributes().getWidth() - (getElementStroke().getStrokeWidth() * 3) / 2),
                        (long) (generateRealYCoordinate().longValue() + getElementAttributes().getHeight() - (getElementStroke().getStrokeWidth() * 3) / 2)),
                new Point((long) (generateRealXCoordinate().longValue() + getElementAttributes().getWidth() - (getElementStroke().getStrokeWidth() * 3) / 2),
                        (long) (generateRealYCoordinate().longValue() - getElementStroke().getStrokeWidth() / 2)),
                new Point((long) (generateRealXCoordinate().longValue() - getElementStroke().getStrokeWidth() / 2),
                        (long) (generateRealYCoordinate().longValue() - getElementStroke().getStrokeWidth() / 2)));
        border.setElementStroke(getElementStroke());
        border.getElementStroke().setLineCap(StrokeLineCap.SQUARE);
        return border.generateSvg(doc);
    }
}
