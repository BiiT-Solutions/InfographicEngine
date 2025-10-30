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
import com.biit.infographic.core.models.svg.StrokeAlign;
import com.biit.infographic.core.models.svg.SvgAreaElement;
import com.biit.infographic.core.models.svg.components.path.Point;
import com.biit.infographic.core.models.svg.serialization.SvgCircleSectorDeserializer;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.util.ArrayList;
import java.util.Collection;

@JsonDeserialize(using = SvgCircleSectorDeserializer.class)
@JsonRootName(value = "circleSector")
public class SvgCircleSector extends SvgAreaElement {
    private static final int CIRCLE_DEGREES = 360;

    @JsonProperty("radius")
    private Long radius;

    @JsonProperty("startAngle")
    private Long startAngle;

    @JsonProperty("endAngle")
    private Long endAngle;

    @JsonProperty("percentage")
    private String percentage;

    public SvgCircleSector(ElementAttributes elementAttributes) {
        super(elementAttributes);
        setElementType(ElementType.CIRCLE_SECTOR);
    }

    public SvgCircleSector() {
        this(new ElementAttributes());
    }

    /**
     * Create a sector.
     *
     * @param xCoordinate x for the circle.
     * @param yCoordinate y for the circle.
     * @param radius      radius of the circle.
     */
    public SvgCircleSector(Number xCoordinate, Number yCoordinate, Number radius) {
        this(xCoordinate != null ? xCoordinate.longValue() : 0, yCoordinate != null ? yCoordinate.longValue() : 0,
                radius != null ? radius.longValue() : 0, 0, 0);
    }


    /**
     * Create a sector.
     *
     * @param xCoordinate x for the circle.
     * @param yCoordinate y for the circle.
     * @param radius      radius of the circle.
     * @param percentage  percentage filled up [0, 1]
     */
    public SvgCircleSector(Number xCoordinate, Number yCoordinate, Number radius, Number percentage) {
        this(xCoordinate != null ? xCoordinate.longValue() : 0, yCoordinate != null ? yCoordinate.longValue() : 0,
                radius != null ? radius.longValue() : 0, 0, null);
        if (percentage.doubleValue() < 0 || percentage.doubleValue() > 1) {
            throw new IllegalArgumentException("percentage must be between 0 and 1");
        }
        setPercentage(percentage);
    }

    public SvgCircleSector(Number xCoordinate, Number yCoordinate, Number radius, Number startAngle, Number endAngle) {
        this(xCoordinate != null ? xCoordinate.longValue() : 0, yCoordinate != null ? yCoordinate.longValue() : 0,
                radius != null ? radius.longValue() : 0, startAngle != null ? startAngle.longValue() : 0,
                endAngle != null ? endAngle.longValue() : 0);
    }

    public SvgCircleSector(Long xCoordinate, Long yCoordinate, Long radius, Long startAngle, Long endAngle) {
        this(new ElementAttributes(xCoordinate, yCoordinate, null, null, null));
        setRadius(radius);
        setStartAngle(startAngle);
        setEndAngle(endAngle);
        getElementStroke().setStrokeWidth(1);
    }

    public Long getRadius() {
        return radius;
    }

    public Long getRealRadius() {
        return (long) (radius
                - (getElementStroke().getStrokeAlign() == StrokeAlign.OUTSET ? getElementStroke().getStrokeWidth() : getElementStroke().getStrokeWidth() / 2));
    }

    public void setRadius(Long radius) {
        this.radius = radius;
    }

    public Long getStartAngle() {
        if (startAngle != null) {
            return startAngle;
        }
        return 0L;
    }

    public void setStartAngle(Long startAngle) {
        this.startAngle = startAngle;
    }

    public Long getEndAngle() {
        return endAngle;
    }

    @JsonIgnore
    public Long getCalculatedEndAngle() {
        if (endAngle != null && (endAngle != 0 || percentage == null)) {
            return endAngle;
        }
        try {
            return (long) Math.min(CIRCLE_DEGREES, CIRCLE_DEGREES * Double.parseDouble(percentage.isBlank() ? "0" : percentage));
        } catch (NumberFormatException nfe) {
            return 0L;
        }
    }

    public void setEndAngle(Long endAngle) {
        this.endAngle = endAngle;
    }

    /**
     * @param percentage
     */
    public void setPercentage(String percentage) {
        this.percentage = percentage;
    }

    /**
     * @param percentage percentage filled up [0, 1]
     */
    public void setPercentage(Number percentage) {
        if (percentage == null || percentage.doubleValue() < 0 || percentage.doubleValue() > 1) {
            throw new IllegalArgumentException("percentage must be between 0 and 1");
        }
        this.percentage = String.valueOf(percentage);
    }

    @Override
    public Collection<Element> generateSvg(Document doc) {
        validateAttributes();
        final ArrayList<Element> elements = new ArrayList<>();
        final Element sector;
        //Calculate as a Circle
        if (getCalculatedEndAngle() == CIRCLE_DEGREES) {
            sector = doc.createElementNS(NAMESPACE, "circle");
            sector.setAttributeNS(null, "cx", String.valueOf(getElementAttributes().getXCoordinate() + getRadius()));
            sector.setAttributeNS(null, "cy", String.valueOf(getElementAttributes().getYCoordinate() + getRadius()));
            sector.setAttributeNS(null, "r", String.valueOf(getRealRadius()));
        } else {
            sector = doc.createElementNS(NAMESPACE, "path");
            sector.setAttributeNS(null, "d", "m " + createArc(getElementAttributes().getXCoordinate() + getRadius(),
                    getElementAttributes().getYCoordinate() + getRadius(), getRadius(), getStartAngle(), getCalculatedEndAngle()));
        }
        elements.add(sector);
        elementStroke(sector);
        elementAttributes(sector);

        return elements;
    }


    //https://stackoverflow.com/questions/5736398/how-to-calculate-the-svg-path-for-an-arc-of-a-circle
    private String createArc(long x, long y, long radius, long startAngle, long endAngle) {
        final StringBuilder path = new StringBuilder();
        final Point start = polarToCartesian(x, y, radius, endAngle);
        final Point end = polarToCartesian(x, y, radius, startAngle);

        final int largeArcFlag = endAngle - startAngle <= CIRCLE_DEGREES / 2 ? 0 : 1;

        path.append(start.getX()).append(" ").append(start.getY()).append(" ")
                .append("A ").append(radius).append(" ").append(radius).append(" ").append("0 ")
                .append(largeArcFlag).append(" 0 ").append(end.getX()).append(" ").append(end.getY()).append(" ");

        //Close the arc if fill property is there.
        if (getElementAttributes().getFill() != null) {
            path.append("L ").append(x).append(" ").append(y).append(" Z");
        }

        return path.toString();
    }

    private Point polarToCartesian(long x, long y, long radius, long angleInDegrees) {
        final double angleInRadians = (angleInDegrees - 90) * Math.PI / 180.0;

        return new Point(x + (radius * Math.cos(angleInRadians)),
                y + (radius * Math.sin(angleInRadians)));
    }
}
