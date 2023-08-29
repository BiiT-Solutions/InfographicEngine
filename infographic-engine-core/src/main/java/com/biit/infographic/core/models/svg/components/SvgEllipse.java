package com.biit.infographic.core.models.svg.components;

import com.biit.infographic.core.models.svg.ElementAttributes;
import com.biit.infographic.core.models.svg.ElementType;
import com.biit.infographic.core.models.svg.SvgElement;
import com.biit.infographic.core.models.svg.exceptions.InvalidAttributeException;
import com.fasterxml.jackson.annotation.JsonRootName;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

@JsonRootName(value = "ellipse")
public class SvgEllipse extends SvgElement {

    public SvgEllipse(ElementAttributes elementAttributes) {
        super(elementAttributes);
        setElementType(ElementType.ELLIPSE);
    }

    public SvgEllipse() {
        this(new ElementAttributes());
    }

    public SvgEllipse(Long xCoordinate, Long yCoordinate, String width, String height, String fill) {
        this(new ElementAttributes(xCoordinate, yCoordinate, width, height, fill));
    }

    @Override
    public Element generateSvg(Document doc) {
        final Element ellipse = doc.createElementNS(NAMESPACE, "ellipse");
        ellipse.setAttributeNS(null, "cx", String.valueOf(getElementAttributes().getXCoordinate()));
        ellipse.setAttributeNS(null, "cy", String.valueOf(getElementAttributes().getYCoordinate()));
        if (getElementAttributes().getWidth() != null && getElementAttributes().getWidth() != 0) {
            ellipse.setAttributeNS(null, "rx", String.valueOf(getElementAttributes().getWidthValue()));
        }
        if (getElementAttributes().getHeight() != null && getElementAttributes().getHeight() != 0) {
            ellipse.setAttributeNS(null, "ry", String.valueOf(getElementAttributes().getHeightValue()));
        }
        elementStroke(ellipse);
        elementAttributes(ellipse);
        return ellipse;
    }

    @Override
    public void validateAttributes() throws InvalidAttributeException {

    }
}
