package com.biit.infographic.core.models.svg.components;

import com.biit.infographic.core.models.svg.ElementAttributes;
import com.biit.infographic.core.models.svg.ElementType;
import com.biit.infographic.core.models.svg.SvgElement;
import com.biit.infographic.core.models.svg.exceptions.InvalidAttributeException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class SvgCircle extends SvgElement {

    private Long radio;

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

    public SvgCircle(Long xCoordinate, Long yCoordinate, Long radio) {
        this(new ElementAttributes(xCoordinate, yCoordinate, null, null, null));
        setRadio(radio);
    }

    public Long getRadio() {
        return radio;
    }

    public void setRadio(Long radio) {
        this.radio = radio;
    }

    @Override
    public Element generateSvg(Document doc) {
        validateAttributes();
        final Element rectangle = doc.createElementNS(NAMESPACE, "circle");
        rectangle.setAttributeNS(null, "cx", String.valueOf(getElementAttributes().getXCoordinate()));
        rectangle.setAttributeNS(null, "cy", String.valueOf(getElementAttributes().getYCoordinate()));
        rectangle.setAttributeNS(null, "r", String.valueOf(radio));
        if (getElementAttributes().getFill() != null) {
            rectangle.setAttributeNS(null, "fill", getElementAttributes().getFill());
        }
        return rectangle;
    }

    @Override
    public void validateAttributes() throws InvalidAttributeException {
        if (radio == null || radio == 0) {
            throw new InvalidAttributeException(this.getClass(), "Invalid radio on circle '" + getId() + "'");
        }
        if (getElementAttributes().getHeight() != null) {
            throw new InvalidAttributeException(this.getClass(), "Circle '" + getId() + "' must not have height attribute");
        }
        if (getElementAttributes().getWidth() != null) {
            throw new InvalidAttributeException(this.getClass(), "Circle '" + getId() + "' must not have width attribute");
        }
        if (getElementAttributes().getFill() != null) {
            throw new InvalidAttributeException(this.getClass(), "Circle '" + getId() + "' must not have fill attribute");
        }
    }
}
