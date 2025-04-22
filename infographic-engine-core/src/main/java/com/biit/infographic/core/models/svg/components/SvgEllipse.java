package com.biit.infographic.core.models.svg.components;

import com.biit.infographic.core.models.svg.ElementAttributes;
import com.biit.infographic.core.models.svg.ElementType;
import com.biit.infographic.core.models.svg.SvgAreaElement;
import com.biit.infographic.core.models.svg.serialization.SvgEllipseDeserializer;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.util.Collection;
import java.util.Collections;

@JsonDeserialize(using = SvgEllipseDeserializer.class)
@JsonRootName(value = "ellipse")
public class SvgEllipse extends SvgAreaElement {

    public SvgEllipse(ElementAttributes elementAttributes) {
        super(elementAttributes);
        setElementType(ElementType.ELLIPSE);
    }

    public SvgEllipse() {
        this(new ElementAttributes());
    }

    public SvgEllipse(Number xCoordinate, Number yCoordinate, String width, String height, String fill) {
        this(xCoordinate != null ? xCoordinate.longValue() : null, yCoordinate != null ? yCoordinate.longValue() : null, width, height, fill);
    }

    public SvgEllipse(Long xCoordinate, Long yCoordinate, String width, String height, String fill) {
        this(new ElementAttributes(xCoordinate, yCoordinate, width, height, fill));
    }

    @Override
    public Collection<Element> generateSvg(Document doc) {
        final Element ellipse = doc.createElementNS(NAMESPACE, "ellipse");
        ellipse.setAttributeNS(null, "cx", String.valueOf(getElementAttributes().getXCoordinate()));
        ellipse.setAttributeNS(null, "cy", String.valueOf(getElementAttributes().getYCoordinate()));
        if (getElementAttributes().getWidth() != null && getElementAttributes().getWidth() != 0) {
            ellipse.setAttributeNS(null, "rx", String.valueOf(generateRealWidth()));
        }
        if (getElementAttributes().getHeight() != null && getElementAttributes().getHeight() != 0) {
            ellipse.setAttributeNS(null, "ry", String.valueOf(generateRealHeight()));
        }
        elementStroke(ellipse);
        elementAttributes(ellipse);
        return Collections.singletonList(ellipse);
    }
}
