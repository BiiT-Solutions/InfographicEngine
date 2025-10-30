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
import com.biit.infographic.core.models.svg.serialization.VerticalLineDeserializer;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@JsonDeserialize(using = VerticalLineDeserializer.class)
@JsonRootName(value = "verticalLine")
public class VerticalLine extends PathElement {

    @JsonProperty("y")
    private Long y;

    public VerticalLine() {
        super();
        setElementType(ElementType.VERTICAL_LINE);
    }

    public VerticalLine(Number y) {
        this(y, false);
    }

    public VerticalLine(Long y) {
        this(y, false);
    }

    public VerticalLine(Number y, boolean relativeCoordinates) {
        this(y != null ? y.longValue() : null, relativeCoordinates);
    }

    public VerticalLine(Long y, boolean relativeCoordinates) {
        this();
        this.y = y;
        setRelativeCoordinates(relativeCoordinates);
    }

    @Override
    public Long getX() {
        return null;
    }

    @Override
    public Long getY() {
        return y;
    }

    public void setY(Long y) {
        this.y = y;
    }

    @Override
    public String generateCoordinate(Long previousX, Long previousY) {
        return (isRelativeCoordinates() ? "v " : "V ") + getY() + " ";
    }

}
