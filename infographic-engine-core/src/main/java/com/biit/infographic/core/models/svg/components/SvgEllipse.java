package com.biit.infographic.core.models.svg.components;

import com.biit.infographic.core.models.svg.ElementAttributes;
import com.biit.infographic.core.models.svg.ElementType;
import com.biit.infographic.core.models.svg.SvgElement;
import com.biit.infographic.core.models.svg.exceptions.InvalidAttributeException;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonSetter;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class SvgEllipse extends SvgElement {

    private Long xRadius = 0L;
    private Long yRadius = 0L;

    public SvgEllipse(ElementAttributes elementAttributes) {
        super(elementAttributes);
        setElementType(ElementType.ELLIPSE);
    }

    public SvgEllipse() {
        this(new ElementAttributes());
    }

    public SvgEllipse(Long xCoordinate, Long yCoordinate, Long xRadius, Long yRadius, String fill) {
        this(new ElementAttributes(xCoordinate, yCoordinate, null, null, fill));
        setXRadius(xRadius);
        setYRadius(yRadius);
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
        final Element rectangle = doc.createElementNS(NAMESPACE, "ellipse");
        if (getId() != null) {
            rectangle.setAttribute("id", getId());
        }
        rectangle.setAttributeNS(null, "cx", String.valueOf(getElementAttributes().getXCoordinate()));
        rectangle.setAttributeNS(null, "cy", String.valueOf(getElementAttributes().getYCoordinate()));
        if (getElementAttributes().getFill() != null) {
            rectangle.setAttributeNS(null, "fill", getElementAttributes().getFill());
        }
        if (xRadius != null && xRadius != 0) {
            rectangle.setAttributeNS(null, "rx", String.valueOf(getXRadius()));
        }
        if (yRadius != null && yRadius != 0) {
            rectangle.setAttributeNS(null, "ry", String.valueOf(getYRadius()));
        }
        return rectangle;
    }

    @Override
    public void validateAttributes() throws InvalidAttributeException {
        if (getElementAttributes().getHeight() != null) {
            throw new InvalidAttributeException(this.getClass(), "Ellipse '" + getId() + "' must not have height attribute");
        }
        if (getElementAttributes().getWidth() != null) {
            throw new InvalidAttributeException(this.getClass(), "Ellipse '" + getId() + "' must not have width attribute");
        }
        if (getXRadius() == null || getXRadius() == 0) {
            throw new InvalidAttributeException(this.getClass(), "Ellipse '" + getId() + "' does not have xRadius attribute");
        }
        if (getYRadius() == null || getYRadius() == 0) {
            throw new InvalidAttributeException(this.getClass(), "Ellipse '" + getId() + "' does not have yRadius attribute");
        }
    }
}
