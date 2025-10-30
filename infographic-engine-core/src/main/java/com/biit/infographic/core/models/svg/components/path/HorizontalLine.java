package com.biit.infographic.core.models.svg.components.path;

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
import com.biit.infographic.core.models.svg.serialization.HorizontalLineDeserializer;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@JsonDeserialize(using = HorizontalLineDeserializer.class)
@JsonRootName(value = "horizontalLine")
public class HorizontalLine extends PathElement {

    @JsonProperty("x")
    private Long x;

    public HorizontalLine() {
        super();
        setElementType(ElementType.HORIZONTAL_LINE);
    }

    public HorizontalLine(Number x) {
        this(x, false);
    }

    public HorizontalLine(Long x) {
        this(x, false);
    }

    public HorizontalLine(Number x, boolean relativeCoordinates) {
        this(x != null ? x.longValue() : null, relativeCoordinates);
    }

    public HorizontalLine(Long x, boolean relativeCoordinates) {
        this();
        this.x = x;
        setRelativeCoordinates(relativeCoordinates);
    }

    @Override
    public Long getX() {
        return x;
    }

    @Override
    public Long getY() {
        return null;
    }

    public void setX(Long x) {
        this.x = x;
    }

    @Override
    public String generateCoordinate(Long previousX, Long previousY) {
        return (isRelativeCoordinates() ? "h " : "H ") + getX() + " ";
    }
}
