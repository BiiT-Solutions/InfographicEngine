package com.biit.infographic.core.models.svg.components.gradient;

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

import com.biit.infographic.core.models.svg.ElementType;
import com.biit.infographic.core.models.svg.SvgElement;
import com.biit.infographic.core.models.svg.exceptions.InvalidAttributeException;
import com.biit.infographic.core.models.svg.serialization.SvgGradientDeserializer;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

@JsonDeserialize(using = SvgGradientDeserializer.class)
@JsonRootName(value = "gradient")
public class SvgGradient extends SvgElement {
    public static final String ID_PREFIX = "linearGradient";

    @JsonProperty("stops")
    private List<SvgGradientStop> stops;

    @JsonProperty("x1")
    private Long x1Coordinate;

    @JsonProperty("y1")
    private Long y1Coordinate;

    @JsonProperty("x2")
    private Long x2Coordinate;

    @JsonProperty("y2")
    private Long y2Coordinate;

    public SvgGradient() {
        super();
        setElementType(ElementType.GRADIENT);
    }

    public SvgGradient(Number x1Coordinate, Number y1Coordinate, Number x2Coordinate, Number y2Coordinate, SvgGradientStop... stops) {
        this(x1Coordinate != null ? x1Coordinate.longValue() : null,
                y1Coordinate != null ? y1Coordinate.longValue() : null,
                x2Coordinate != null ? x2Coordinate.longValue() : null,
                y2Coordinate != null ? y2Coordinate.longValue() : null,
                stops);
    }

    public SvgGradient(Long x1Coordinate, Long y1Coordinate, Long x2Coordinate, Long y2Coordinate, SvgGradientStop... stops) {
        this();
        setX1Coordinate(x1Coordinate);
        setY1Coordinate(y1Coordinate);
        setX2Coordinate(x2Coordinate);
        setY2Coordinate(y2Coordinate);
        setStops(Arrays.asList(stops));
    }

    public SvgGradient(SvgGradientStop... stops) {
        this();
        setStops(Arrays.asList(stops));
    }

    public Long getX1Coordinate() {
        return x1Coordinate;
    }

    public void setX1Coordinate(Number x1Coordinate) {
        this.x1Coordinate = x1Coordinate != null ? x1Coordinate.longValue() : null;
    }

    public void setX1Coordinate(Long x1Coordinate) {
        this.x1Coordinate = x1Coordinate;
    }

    public Long getY1Coordinate() {
        return y1Coordinate;
    }

    public void setY1Coordinate(Number y1Coordinate) {
        this.y1Coordinate = y1Coordinate != null ? y1Coordinate.longValue() : null;
    }

    public void setY1Coordinate(Long y1Coordinate) {
        this.y1Coordinate = y1Coordinate;
    }

    public Long getX2Coordinate() {
        return x2Coordinate;
    }

    public void setX2Coordinate(Number x2Coordinate) {
        this.x2Coordinate = x2Coordinate != null ? x2Coordinate.longValue() : null;
    }

    public void setX2Coordinate(Long x2Coordinate) {
        this.x2Coordinate = x2Coordinate;
    }

    public Long getY2Coordinate() {
        return y2Coordinate;
    }

    public void setY2Coordinate(Number y2Coordinate) {
        this.y2Coordinate = y2Coordinate != null ? y2Coordinate.longValue() : null;
    }

    public void setY2Coordinate(Long y2Coordinate) {
        this.y2Coordinate = y2Coordinate;
    }

    public List<SvgGradientStop> getStops() {
        return stops;
    }

    public void setStops(List<SvgGradientStop> stops) {
        this.stops = stops;
    }

    public void addStops(SvgGradientStop stop) {
        if (stops == null) {
            stops = new ArrayList<>();
        }
        stops.add(stop);
    }

    @Override
    public Collection<Element> generateSvg(Document doc) {
        final Element gradient = doc.createElementNS(NAMESPACE, ID_PREFIX);
        if (getId() != null) {
            gradient.setAttributeNS(null, "id", getId());
        }
        if (getX1Coordinate() != null) {
            gradient.setAttributeNS(null, "x1", String.valueOf(getX1Coordinate()));
        }
        if (getY1Coordinate() != null) {
            gradient.setAttributeNS(null, "y1", String.valueOf(getY1Coordinate()));
        }
        if (getX2Coordinate() != null) {
            gradient.setAttributeNS(null, "x2", String.valueOf(getX2Coordinate()));
        }
        if (getY2Coordinate() != null) {
            gradient.setAttributeNS(null, "y2", String.valueOf(getY2Coordinate()));
        }
        // Needed to be shown correctly on browsers: gradientUnits="userSpaceOnUse"
        gradient.setAttributeNS(null, "gradientUnits", "userSpaceOnUse");
        stops.forEach(stop -> {
            final Collection<Element> elements = stop.generateSvg(doc);
            elements.forEach(gradient::appendChild);
        });
        return Collections.singletonList(gradient);
    }

    @Override
    public void validateAttributes() throws InvalidAttributeException {
        //No validation yet!
    }

    public void setDefaultCoordinates(Number x1, Number y1, Number x2, Number y2) {
        if (getX1Coordinate() == null) {
            setX1Coordinate(x1);
        }
        if (getY1Coordinate() == null) {
            setY1Coordinate(y1);
        }
        if (getX2Coordinate() == null) {
            setX2Coordinate(x2);
        }
        if (getY2Coordinate() == null) {
            setY2Coordinate(y2);
        }
    }
}
