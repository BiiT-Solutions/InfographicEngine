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

import com.biit.infographic.core.models.svg.serialization.FillAttributesDeserializer;
import com.biit.infographic.core.models.svg.utils.Color;
import com.biit.infographic.logger.SvgGeneratorLogger;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.Objects;

@JsonRootName(value = "fillAttributes")
@JsonDeserialize(using = FillAttributesDeserializer.class)
public class FillAttributes {

    @JsonProperty("elementType")
    private ElementType elementType;

    @JsonProperty("fill")
    private String fill;

    @JsonProperty("fillOpacity")
    private String fillOpacity;

    @JsonProperty("hoverFillColor")
    private String hoverFillColor;

    @JsonProperty("hoverFillOpacity")
    private String hoverFillOpacity;

    public ElementType getElementType() {
        return elementType;
    }

    public void setElementType(ElementType elementType) {
        this.elementType = elementType;
    }

    public String getFill() {
        return fill;
    }

    public void setFill(String fill) {
        fill = Color.checkColor(fill);
        if (Color.isDroolsVariable(fill)) {
            this.fill = fill;
        } else if (Color.isValidWithoutTransparency(fill)) {
            this.fill = fill;
        } else if (Color.isValidWithTransparency(fill)) {
            this.fill = fill.substring(0, Color.COLOR_WITH_TRANSPARENCY_LENGTH - 1);
            setFillOpacity(String.valueOf(Color.getOpacity(fill)));
        } else if (Color.isValidName(fill)) {
            this.fill = fill;
            setFillOpacity((String) null);
        } else {
            //Some predefined tags.
            if (Objects.equals("none", fill)) {
                this.fill = fill;
            } else {
                SvgGeneratorLogger.warning(this.getClass(), "Fill value '" + fill + "' is invalid and therefore ignored.");
                this.fill = fill;
            }
        }
    }

    public String getFillOpacity() {
        return fillOpacity;
    }

    public void setFillOpacity(Double fillOpacity) {
        setFillOpacity(String.valueOf(fillOpacity));
    }

    public void setFillOpacity(String fillOpacity) {
        this.fillOpacity = Color.getFillOpacity(fillOpacity);
    }

    public String getHoverFillColor() {
        return hoverFillColor;
    }

    public void setHoverFillColor(String hover) {
        hover = Color.checkColor(hover);
        if (Color.isDroolsVariable(hover)) {
            this.hoverFillColor = hover;
        } else if (Color.isValidWithoutTransparency(hover)) {
            this.hoverFillColor = hover;
        } else if (Color.isValidWithTransparency(hover)) {
            this.hoverFillColor = hover.substring(0, Color.COLOR_WITH_TRANSPARENCY_LENGTH - 1);
            setHoverFillOpacity(String.valueOf(Color.getOpacity(hover)));
        } else if (Color.isValidName(hover)) {
            this.hoverFillColor = hover;
            setHoverFillOpacity((String) null);
        } else {
            //Some predefined tags.
            if (Objects.equals("none", hover)) {
                this.hoverFillColor = hover;
            } else {
                SvgGeneratorLogger.warning(this.getClass(), "hover value '" + hover + "' is invalid and therefore ignored.");
                this.hoverFillColor = hover;
            }
        }
    }

    public void setHoverFillOpacity(Double fillOpacity) {
        setHoverFillOpacity(String.valueOf(fillOpacity));
    }


    public String getHoverFillOpacity() {
        return hoverFillOpacity;
    }

    public void setHoverFillOpacity(String hoverFillOpacity) {
        this.hoverFillOpacity = hoverFillOpacity;
    }
}
