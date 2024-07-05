package com.biit.infographic.core.models.svg.components;

import com.biit.infographic.core.models.svg.ElementAttributes;
import com.biit.infographic.core.models.svg.ElementType;
import com.biit.infographic.core.models.svg.serialization.SvgCircleDeserializer;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@JsonDeserialize(using = SvgCircleDeserializer.class)
@JsonRootName(value = "semicircle")
public class SvgSemiCircle extends SvgCircle {

    public SvgSemiCircle(ElementAttributes elementAttributes) {
        super(elementAttributes);
        setElementType(ElementType.CIRCLE);
    }

    public SvgSemiCircle() {
        this(new ElementAttributes());
    }

    public SvgSemiCircle(String width, String height, String fill) {
        this(new ElementAttributes(width, height, fill));
    }

    public SvgSemiCircle(Number xCoordinate, Number yCoordinate, Number radius) {
        this(xCoordinate != null ? xCoordinate.longValue() : null, yCoordinate != null ? yCoordinate.longValue() : null,
                radius != null ? radius.longValue() : null);
    }

    public SvgSemiCircle(Long xCoordinate, Long yCoordinate, Long radius) {
        this(new ElementAttributes(xCoordinate, yCoordinate, null, null, null));
        setRadius(radius);
    }
}
