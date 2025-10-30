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
import com.fasterxml.jackson.annotation.JsonProperty;

public abstract class PathElement {

    @JsonProperty("elementType")
    private ElementType elementType;

    @JsonProperty("relativeCoordinates")
    private boolean relativeCoordinates;

    protected PathElement() {
        this.relativeCoordinates = false;
    }

    public ElementType getElementType() {
        return elementType;
    }

    public void setElementType(ElementType elementType) {
        this.elementType = elementType;
    }

    public boolean isRelativeCoordinates() {
        return relativeCoordinates;
    }

    public void setRelativeCoordinates(boolean relativeCoordinates) {
        this.relativeCoordinates = relativeCoordinates;
    }

    public abstract Long getX();

    public abstract Long getY();

    public abstract String generateCoordinate(Long previousX, Long previousY);
}
