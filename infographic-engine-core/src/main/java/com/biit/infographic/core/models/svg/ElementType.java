package com.biit.infographic.core.models.svg;

/*-
 * #%L
 * Infographic Engine v2 (Core)
 * %%
 * Copyright (C) 2022 - 2025 BiiT Sourcing Solutions S.L.
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * #L%
 */

import com.biit.infographic.core.models.svg.clip.SvgClipPath;
import com.biit.infographic.core.models.svg.clip.SvgRectangleClipPath;
import com.biit.infographic.core.models.svg.components.SvgCircle;
import com.biit.infographic.core.models.svg.components.SvgCircleSector;
import com.biit.infographic.core.models.svg.components.SvgEllipse;
import com.biit.infographic.core.models.svg.components.SvgImage;
import com.biit.infographic.core.models.svg.components.SvgLine;
import com.biit.infographic.core.models.svg.components.SvgLink;
import com.biit.infographic.core.models.svg.components.SvgPath;
import com.biit.infographic.core.models.svg.components.SvgRectangle;
import com.biit.infographic.core.models.svg.components.SvgRectangleSector;
import com.biit.infographic.core.models.svg.components.SvgScript;
import com.biit.infographic.core.models.svg.components.bars.SvgHorizontalBar;
import com.biit.infographic.core.models.svg.components.charts.SvgRadarChart;
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
    RECTANGLE_SECTOR(SvgRectangleSector.class),
    CIRCLE(SvgCircle.class),
    CIRCLE_SECTOR(SvgCircleSector.class),
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
    CLIP_PATH(SvgClipPath.class),
    RECTANGLE_CLIP_PATH(SvgRectangleClipPath.class),
    ELEMENT_ATTRIBUTES(ElementAttributes.class),
    HORIZONTAL_BAR(SvgHorizontalBar.class),
    RADAR_CHART(SvgRadarChart.class);

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
