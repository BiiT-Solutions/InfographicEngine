package com.biit.infographic.core.models.svg.components;

import com.biit.infographic.core.models.svg.ElementAttributes;
import com.biit.infographic.core.models.svg.ElementType;
import com.biit.infographic.core.models.svg.SvgAreaElement;
import com.biit.infographic.core.models.svg.exceptions.InvalidAttributeException;
import com.biit.infographic.core.models.svg.serialization.SvgRectangleDeserializer;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

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

    public SvgRectangle(Long xCoordinate, Long yCoordinate, String width, String height, String fill) {
        this(new ElementAttributes(xCoordinate, yCoordinate, width, height, fill));
    }

    public SvgRectangle(Long xCoordinate, Long yCoordinate, Long width, Long height, String fill) {
        this(new ElementAttributes(xCoordinate, yCoordinate, String.valueOf(width), String.valueOf(height), fill));
    }

    public SvgRectangle(Long xCoordinate, Long yCoordinate, Long width, Long height, String fill, Double fillOpacity) {
        this(new ElementAttributes(xCoordinate, yCoordinate, String.valueOf(width), String.valueOf(height), fill, fillOpacity));
    }

    public Long getXRadius() {
        return xRadius;
    }

    public void setXRadius(Long xRadius) {
        this.xRadius = xRadius;
    }

    public Long getYRadius() {
        return yRadius;
    }

    public void setYRadius(Long yRadius) {
        this.yRadius = yRadius;
    }

    @Override
    public Element generateSvg(Document doc) {
        final Element rectangle = doc.createElementNS(NAMESPACE, "rect");
        rectangle.setAttributeNS(null, "x", String.valueOf(generateRealXCoordinate()));
        rectangle.setAttributeNS(null, "y", String.valueOf(generateRealYCoordinate()));
        if (xRadius != null && xRadius != 0) {
            rectangle.setAttributeNS(null, "rx", String.valueOf(getXRadius()));
        }
        if (yRadius != null && yRadius != 0) {
            rectangle.setAttributeNS(null, "ry", String.valueOf(getYRadius()));
        }
        elementStroke(rectangle);
        elementAttributes(rectangle);
        return rectangle;
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
}
