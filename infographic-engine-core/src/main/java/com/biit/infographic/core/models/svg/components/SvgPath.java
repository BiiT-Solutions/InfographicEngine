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
import com.biit.infographic.core.models.svg.components.path.PathElement;
import com.biit.infographic.core.models.svg.exceptions.InvalidAttributeException;
import com.biit.infographic.core.models.svg.serialization.SvgPathDeserializer;
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

@JsonDeserialize(using = SvgPathDeserializer.class)
@JsonRootName(value = "path")
public class SvgPath extends SvgAreaElement {

    @JsonProperty("elements")
    private List<PathElement> pathElements;

    public SvgPath() {
        this(new ElementAttributes());
    }

    public SvgPath(ElementAttributes elementAttributes) {
        super(elementAttributes);
        setElementType(ElementType.PATH);
        getElementStroke().setStrokeWidth(1.0);
        elementAttributes.setFill("none");
    }

    public SvgPath(Number xCoordinate, Number yCoordinate, PathElement... pathElements) {
        this(xCoordinate != null ? xCoordinate.longValue() : null, yCoordinate != null ? yCoordinate.longValue() : null, pathElements);
    }

    public SvgPath(Long xCoordinate, Long yCoordinate, PathElement... pathElements) {
        this(new ElementAttributes(xCoordinate, yCoordinate, null, null, null));
        if (pathElements != null) {
            setPathElements(Arrays.asList(pathElements));
        } else {
            setPathElements(new ArrayList<>());
        }
    }

    public SvgPath(String strokeColor, Double strokeWidth, Number xCoordinate, Number yCoordinate, PathElement... pathElements) {
        this(strokeColor, strokeWidth, xCoordinate != null ? xCoordinate.longValue() : null, yCoordinate != null ? yCoordinate.longValue() : null,
                pathElements);
    }

    public SvgPath(String strokeColor, Double strokeWidth, Long xCoordinate, Long yCoordinate, PathElement... pathElements) {
        this(new ElementAttributes(xCoordinate, yCoordinate, null, null, null));
        getElementStroke().setStrokeColor(strokeColor);
        getElementStroke().setStrokeWidth(strokeWidth);
        if (pathElements != null) {
            setPathElements(Arrays.asList(pathElements));
        } else {
            setPathElements(new ArrayList<>());
        }
    }

    public List<PathElement> getPathElements() {
        if (pathElements == null) {
            return new ArrayList<>();
        }
        return pathElements;
    }

    public void setPathElements(List<PathElement> pathElements) {
        this.pathElements = pathElements;
    }

    @Override
    public void validateAttributes() throws InvalidAttributeException {
        super.validateAttributes();
        if (getElementAttributes().getHeight() != null) {
            throw new InvalidAttributeException(this.getClass(), "Path '" + getId() + "' must not have 'height' attribute");
        }
        if (getElementAttributes().getWidth() != null) {
            throw new InvalidAttributeException(this.getClass(), "Path '" + getId() + "' must not have 'width' attribute");
        }
    }

    @Override
    public Collection<Element> generateSvg(Document doc) {
        validateAttributes();
        final Element path = doc.createElementNS(NAMESPACE, "path");
        path.setAttributeNS(null, "d", "m " + generateCoordinates());
        elementStroke(path);
        elementAttributes(path);
        return Collections.singletonList(path);
    }

    public String generateCoordinates() {
        final StringBuilder path = new StringBuilder();
        Long previousX;
        Long previousY;
        //If x, y on attributes are defined, we assume that are the first point.
        path.append(getElementAttributes().getXCoordinate()).append(" ").append(getElementAttributes().getYCoordinate()).append(" ");
        previousX = getElementAttributes().getXCoordinate();
        previousY = getElementAttributes().getYCoordinate();

        for (PathElement pathElement : getPathElements()) {
            path.append(pathElement.generateCoordinate(previousX, previousY));
            previousX = pathElement.getX();
            previousY = pathElement.getY();
        }
        return path.toString();
    }
}
