package com.biit.infographic.core.models.svg.components;

import com.biit.infographic.core.models.svg.ElementAttributes;
import com.biit.infographic.core.models.svg.ElementType;
import com.biit.infographic.core.models.svg.SvgElement;
import com.biit.infographic.core.models.svg.exceptions.InvalidAttributeException;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.fasterxml.jackson.annotation.JsonSetter;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

@JsonRootName(value = "rectangle")
public class SvgRectangle extends SvgElement {

    private Long xRadius = 0L;
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

    @JsonGetter("xRadius")
    public Long getXRadius() {
        return xRadius;
    }

    @JsonSetter("xRadius")
    public void setXRadius(Long xRadius) {
        this.xRadius = xRadius;
    }

    @JsonGetter("yRadius")
    public Long getYRadius() {
        return yRadius;
    }

    @JsonSetter("yRadius")
    public void setYRadius(Long yRadius) {
        this.yRadius = yRadius;
    }

    @Override
    public Element generateSvg(Document doc) {
        final Element rectangle = doc.createElementNS(NAMESPACE, "rect");
        rectangle.setAttributeNS(null, "x", String.valueOf(getElementAttributes().getXCoordinate()));
        rectangle.setAttributeNS(null, "y", String.valueOf(getElementAttributes().getYCoordinate()));
        if (xRadius != null && xRadius != 0) {
            rectangle.setAttributeNS(null, "rx", String.valueOf(getXRadius()));
        }
        if (yRadius != null && yRadius != 0) {
            rectangle.setAttributeNS(null, "ry", String.valueOf(getYRadius()));
        }
        elementAttributes(rectangle);
        return rectangle;
    }

    @Override
    public void validateAttributes() throws InvalidAttributeException {
        if (getElementAttributes().getHeight() == null) {
            throw new InvalidAttributeException(this.getClass(), "Rectangle '" + getId() + "' does not have 'height' attribute");
        }
        if (getElementAttributes().getWidth() == null) {
            throw new InvalidAttributeException(this.getClass(), "Rectangle '" + getId() + "' does not have 'width' attribute");
        }
    }
}
