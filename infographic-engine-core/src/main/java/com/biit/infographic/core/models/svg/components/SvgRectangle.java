package com.biit.infographic.core.models.svg.components;

import com.biit.infographic.core.models.svg.ElementAttributes;
import com.biit.infographic.core.models.svg.ElementType;
import com.biit.infographic.core.models.svg.SvgElement;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class SvgRectangle extends SvgElement {

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

    @Override
    public Element generateSvg(Document doc) {
        final Element rectangle = doc.createElementNS(NAMESPACE, "rect");
        rectangle.setAttributeNS(null, "x", String.valueOf(getElementAttributes().getXCoordinate()));
        rectangle.setAttributeNS(null, "y", String.valueOf(getElementAttributes().getYCoordinate()));
        if (getElementAttributes().getWidth() != null) {
            rectangle.setAttributeNS(null, "width", getElementAttributes().getWidth());
        }
        if (getElementAttributes().getHeight() != null) {
            rectangle.setAttributeNS(null, "height", getElementAttributes().getHeight());
        }
        if (getElementAttributes().getFill() != null) {
            rectangle.setAttributeNS(null, "fill", getElementAttributes().getFill());
        }
        return rectangle;
    }
}
