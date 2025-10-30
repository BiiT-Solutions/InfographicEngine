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
import com.biit.infographic.core.models.svg.serialization.BezierCurveQuadraticDeserializer;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@JsonDeserialize(using = BezierCurveQuadraticDeserializer.class)
@JsonRootName(value = "bezierCurve")
public class BezierCurveQuadratic extends PathElement {

    @JsonProperty("x")
    private Long x;

    @JsonProperty("y")
    private Long y;

    @JsonProperty("x1")
    private Long x1;

    @JsonProperty("y1")
    private Long y1;

    public BezierCurveQuadratic() {
        super();
        setElementType(ElementType.BEZIER_CURVE_QUADRATIC);

    }

    public BezierCurveQuadratic(Number x, Number y) {
        this(x, y, false);
    }

    public BezierCurveQuadratic(Number x, Number y, boolean relativeCoordinates) {
        this(x != null ? x.longValue() : null, y != null ? y.longValue() : null, relativeCoordinates);
    }

    public BezierCurveQuadratic(Long x, Long y) {
        this(x, y, false);
    }

    public BezierCurveQuadratic(Long x, Long y, boolean relativeCoordinates) {
        this();
        this.x = x;
        this.y = y;
        setRelativeCoordinates(relativeCoordinates);
    }

    public BezierCurveQuadratic(Number x, Number y, Number x1, Number y1, boolean relativeCoordinates) {
        this(x != null ? x.longValue() : null, y != null ? y.longValue() : null,
                x1 != null ? x1.longValue() : null, y1 != null ? y1.longValue() : null,
                relativeCoordinates);
    }

    public BezierCurveQuadratic(Number x, Number y, Number x1, Number y1) {
        this(x, y, x1, y1, false);
    }

    public BezierCurveQuadratic(Long x, Long y, Long x1, Long y1, boolean relativeCoordinates) {
        this();
        this.x = x;
        this.y = y;
        this.x1 = x1;
        this.y1 = y1;
        setRelativeCoordinates(relativeCoordinates);
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

    public Long getX1() {
        return x1;
    }

    public void setX1(Long x1) {
        this.x1 = x1;
    }

    public Long getY1() {
        return y1;
    }

    public void setY1(Long y1) {
        this.y1 = y1;
    }

    @Override
    public String generateCoordinate(Long previousX, Long previousY) {
        return (isRelativeCoordinates() ? "q " : "Q ") + getX1() + " " + getY1() + ", "
                + getX() + " " + getY() + " ";
    }
}
