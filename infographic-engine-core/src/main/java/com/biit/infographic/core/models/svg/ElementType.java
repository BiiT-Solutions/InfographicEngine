package com.biit.infographic.core.models.svg;

import com.biit.infographic.core.models.svg.components.SvgCircle;
import com.biit.infographic.core.models.svg.components.SvgEllipse;
import com.biit.infographic.core.models.svg.components.SvgImage;
import com.biit.infographic.core.models.svg.components.SvgLine;
import com.biit.infographic.core.models.svg.components.SvgLink;
import com.biit.infographic.core.models.svg.components.SvgPath;
import com.biit.infographic.core.models.svg.components.SvgRectangle;
import com.biit.infographic.core.models.svg.components.SvgScript;
import com.biit.infographic.core.models.svg.components.gauge.SvgGauge;
import com.biit.infographic.core.models.svg.components.gradient.SvgGradient;
import com.biit.infographic.core.models.svg.components.gradient.SvgGradientStop;
import com.biit.infographic.core.models.svg.components.path.Arc;
import com.biit.infographic.core.models.svg.components.path.BezierCurve;
import com.biit.infographic.core.models.svg.components.path.BezierCurveJoin;
import com.biit.infographic.core.models.svg.components.path.BezierCurveQuadratic;
import com.biit.infographic.core.models.svg.components.path.HorizontalLine;
import com.biit.infographic.core.models.svg.components.path.Point;
import com.biit.infographic.core.models.svg.components.path.VerticalLine;
import com.biit.infographic.core.models.svg.components.text.SvgText;

public enum ElementType {
    SVG(SvgTemplate.class),
    NESTED_SVG(SvgTemplate.class),
    EMBEDDED_SVG(SvgEmbedded.class),
    IMAGE(SvgImage.class),
    RECTANGLE(SvgRectangle.class),
    CIRCLE(SvgCircle.class),
    ELLIPSE(SvgEllipse.class),
    LINE(SvgLine.class),
    PATH(SvgPath.class),
    TEXT(SvgText.class),
    SCRIPT(SvgScript.class),
    GAUGE(SvgGauge.class),
    GRADIENT(SvgGradient.class),
    GRADIENT_STOP(SvgGradientStop.class),
    ARC(Arc.class),
    BEZIER_CURVE(BezierCurve.class),
    BEZIER_CURVE_JOIN(BezierCurveJoin.class),
    BEZIER_CURVE_QUADRATIC(BezierCurveQuadratic.class),
    HORIZONTAL_LINE(HorizontalLine.class),
    POINT(Point.class),
    VERTICAL_LINE(VerticalLine.class),
    LINK(SvgLink.class),
    ELEMENT_ATTRIBUTES(ElementAttributes.class);

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
