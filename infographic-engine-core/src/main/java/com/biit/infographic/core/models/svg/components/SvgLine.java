package com.biit.infographic.core.models.svg.components;

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

import com.biit.infographic.core.models.svg.ElementAttributes;
import com.biit.infographic.core.models.svg.ElementType;
import com.biit.infographic.core.models.svg.SvgAreaElement;
import com.biit.infographic.core.models.svg.components.gradient.SvgGradient;
import com.biit.infographic.core.models.svg.exceptions.InvalidAttributeException;
import com.biit.infographic.core.models.svg.serialization.SvgLineDeserializer;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.util.Collection;
import java.util.Collections;

@JsonDeserialize(using = SvgLineDeserializer.class)
@JsonRootName(value = "line")
public class SvgLine extends SvgAreaElement {

    @JsonProperty("x2")
    private Long x2Coordinate;

    @JsonProperty("y2")
    private Long y2Coordinate;

    public SvgLine(ElementAttributes elementAttributes) {
        super(elementAttributes);
        setElementType(ElementType.LINE);
        getElementStroke().setStrokeWidth(1.0);
    }

    public SvgLine() {
        this(new ElementAttributes());
    }

    public SvgLine(Number x1Coordinate, Number y1Coordinate, Number x2Coordinate, Number y2Coordinate) {
        this(x1Coordinate != null ? x1Coordinate.longValue() : null, y1Coordinate != null ? y1Coordinate.longValue() : null,
                x2Coordinate != null ? x2Coordinate.longValue() : null, y2Coordinate != null ? y2Coordinate.longValue() : null);
    }

    public SvgLine(Long x1Coordinate, Long y1Coordinate, Long x2Coordinate, Long y2Coordinate) {
        this(new ElementAttributes(x1Coordinate, y1Coordinate, null, null));
        setX2Coordinate(x2Coordinate);
        setY2Coordinate(y2Coordinate);
    }

    public SvgLine(String strokeColor, Double strokeWidth, Number x1Coordinate, Number y1Coordinate, Number x2Coordinate, Number y2Coordinate) {
        this(strokeColor, strokeWidth, x1Coordinate != null ? x1Coordinate.longValue() : null, y1Coordinate != null ? y1Coordinate.longValue() : null,
                x2Coordinate != null ? x2Coordinate.longValue() : null, y2Coordinate != null ? y2Coordinate.longValue() : null);
    }

    public SvgLine(String strokeColor, Double strokeWidth, Long x1Coordinate, Long y1Coordinate, Long x2Coordinate, Long y2Coordinate) {
        this(new ElementAttributes(x1Coordinate, y1Coordinate, null, null));
        getElementStroke().setStrokeColor(strokeColor);
        getElementStroke().setStrokeWidth(strokeWidth);
        setX2Coordinate(x2Coordinate);
        setY2Coordinate(y2Coordinate);
    }

    public Long getX2Coordinate() {
        return x2Coordinate;
    }

    public void setX2Coordinate(Long x2Coordinate) {
        this.x2Coordinate = x2Coordinate;
    }

    public Long getY2Coordinate() {
        return y2Coordinate;
    }

    public void setY2Coordinate(Long y2Coordinate) {
        this.y2Coordinate = y2Coordinate;
    }

    @Override
    public Collection<Element> generateSvg(Document doc) {
        validateAttributes();
        final Element line = doc.createElementNS(NAMESPACE, "line");
        if (getElementStroke() != null && getElementStroke().getStrokeWidth() > 1) {
            line.setAttributeNS(null, "x1", String.valueOf(getElementAttributes().getXCoordinate() + getElementStroke().getStrokeWidth() / 2));
            line.setAttributeNS(null, "y1", String.valueOf(getElementAttributes().getYCoordinate()));
            line.setAttributeNS(null, "x2", String.valueOf(getX2Coordinate() + getElementStroke().getStrokeWidth() / 2));
            line.setAttributeNS(null, "y2", String.valueOf(getY2Coordinate()));
        } else {
            line.setAttributeNS(null, "x1", String.valueOf(getElementAttributes().getXCoordinate()));
            line.setAttributeNS(null, "y1", String.valueOf(getElementAttributes().getYCoordinate()));
            line.setAttributeNS(null, "x2", String.valueOf(getX2Coordinate()));
            line.setAttributeNS(null, "y2", String.valueOf(getY2Coordinate()));
        }
        elementStroke(line);
        elementAttributes(line);
        return Collections.singletonList(line);
    }

    @Override
    public void validateAttributes() throws InvalidAttributeException {
        super.validateAttributes();
        if (x2Coordinate == null) {
            throw new InvalidAttributeException(this.getClass(), "Invalid x2 on line '" + getId() + "'");
        }
        if (y2Coordinate == null) {
            throw new InvalidAttributeException(this.getClass(), "Invalid y2 on line '" + getId() + "'");
        }
        if (getElementAttributes().getHeight() != null) {
            throw new InvalidAttributeException(this.getClass(), "Line '" + getId() + "' must not have 'height' attribute");
        }
        if (getElementAttributes().getWidth() != null) {
            throw new InvalidAttributeException(this.getClass(), "Line '" + getId() + "' must not have 'width' attribute");
        }
    }

    @Override
    protected StringBuilder generateStyle(StringBuilder style) {
        super.generateStyle(style);
        if (style == null) {
            style = new StringBuilder();
        }
        if (getGradient() != null) {
            style.append("fill:url(#");
            style.append(getGradient().getId());
            style.append(");");
        }
        return style;
    }

    @Override
    public void elementStroke(Element element) throws InvalidAttributeException {
        super.elementStroke(element);
        //Lines has the gradient on the stroke, not on the area.
        if (getGradient() != null) {
            element.setAttributeNS(null, "stroke", "url(#" + getGradient().getId() + ")");
        }
    }

    @Override
    public void setGradient(SvgGradient gradient) {
        if (gradient.getX1Coordinate() == null) {
            gradient.setX1Coordinate(getElementAttributes().getXCoordinate());
        }
        if (gradient.getY1Coordinate() == null) {
            gradient.setY1Coordinate(getElementAttributes().getYCoordinate());
        }
        if (gradient.getX2Coordinate() == null) {
            gradient.setX2Coordinate(getX2Coordinate());
        }
        if (gradient.getY2Coordinate() == null) {
            gradient.setY2Coordinate(getY2Coordinate());
        }
        super.setGradient(gradient);
    }

    @Override
    protected void updateClipPath() {
        if (getClipPath() != null) {
            getClipPath().setSourceX(Math.min(getElementAttributes().getXCoordinate(), getX2Coordinate()));
            getClipPath().setSourceY(Math.min(getElementAttributes().getYCoordinate(), getY2Coordinate()));
            getClipPath().setSourceWidth((long) (Math.abs(getElementAttributes().getXCoordinate() - getX2Coordinate())
                    + (getElementStroke() != null ? getElementStroke().getStrokeWidth() : 0)));
            getClipPath().setSourceHeight((long) (Math.abs(getElementAttributes().getYCoordinate() - getY2Coordinate())
                    + (getElementStroke() != null ? getElementStroke().getStrokeWidth() : 0)));
        }
    }
}
