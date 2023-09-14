package com.biit.infographic.core.models.svg;

import com.biit.infographic.core.models.svg.components.SvgCircle;
import com.biit.infographic.core.models.svg.components.SvgEllipse;
import com.biit.infographic.core.models.svg.components.SvgImage;
import com.biit.infographic.core.models.svg.components.SvgLine;
import com.biit.infographic.core.models.svg.components.SvgPath;
import com.biit.infographic.core.models.svg.components.SvgRectangle;
import com.biit.infographic.core.models.svg.components.SvgScript;
import com.biit.infographic.core.models.svg.components.gauge.SvgGauge;
import com.biit.infographic.core.models.svg.components.text.SvgText;

public enum ElementType {
    SVG(SvgTemplate.class),
    NESTED_SVG(SvgTemplate.class),
    IMAGE(SvgImage.class),
    RECTANGLE(SvgRectangle.class),
    CIRCLE(SvgCircle.class),
    ELLIPSE(SvgEllipse.class),
    LINE(SvgLine.class),
    PATH(SvgPath.class),
    TEXT(SvgText.class),
    SCRIPT(SvgScript.class),
    GAUGE(SvgGauge.class);

    private final Class<?> relatedClass;

    ElementType(Class<?> relatedClass) {
        this.relatedClass = relatedClass;
    }

    public Class<?> getRelatedClass() {
        return relatedClass;
    }

    public static ElementType fromString(String name) {
        for (ElementType elementType : ElementType.values()) {
            if (elementType.name().equalsIgnoreCase(name)) {
                return elementType;
            }
        }
        return null;
    }
}
