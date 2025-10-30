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
import com.biit.infographic.core.models.svg.serialization.ArcDeserializer;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@JsonDeserialize(using = ArcDeserializer.class)
@JsonRootName(value = "arc")
public class Arc extends PathElement {

    @JsonProperty("x")
    private Long x;

    @JsonProperty("y")
    private Long y;

    public Arc() {
        super();
        setElementType(ElementType.ARC);
    }

    public Arc(Number x, Number y) {
        this.x = x != null ? x.longValue() : null;
        this.y = y != null ? y.longValue() : null;
    }

    public Arc(Long x, Long y) {
        this();
        this.x = x;
        this.y = y;
    }

    @Override
    public Long getX() {
        return x;
    }

    public void setX(Long x) {
        this.x = x;
    }

    @Override
    public Long getY() {
        return y;
    }

    public void setY(Long y) {
        this.y = y;
    }

    /**
     *
     * @param previousX
     * @param previousY
     * @return
     */
    @Override
    public String generateCoordinate(Long previousX, Long previousY) {
        return "A "
                + (previousX != null ? getX() - previousX : getX())
                + " "
                + (previousY != null ? getY() - previousY : getY())
                + " 0 0 0 "
                + getX()
                + " "
                + getY()
                + " ";
    }
}
