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
import com.biit.infographic.core.models.svg.serialization.SvgGradientStopDeserializer;
import com.biit.infographic.core.models.svg.utils.Color;
import com.biit.infographic.logger.SvgGeneratorLogger;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.util.Collection;
import java.util.Collections;

@JsonDeserialize(using = SvgGradientStopDeserializer.class)
@JsonRootName(value = "stop")
public class SvgGradientStop extends SvgElement {

    @JsonProperty("color")
    private String color;


    @JsonProperty("opacity")
    private Double opacity;


    @JsonProperty("offset")
    private Double offset;

    public SvgGradientStop() {
        super();
        setElementType(ElementType.GRADIENT_STOP);
    }

    public SvgGradientStop(String color, Double offset) {
        this();
        setColor(color);
        setOffset(offset);
    }

    public SvgGradientStop(String color, Double opacity, Double offset) {
        this();
        setColor(color);
        setOpacity(opacity);
        setOffset(offset);
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        color = Color.checkColor(color);
        if (Color.isValidWithoutTransparency(color)) {
            this.color = color;
        } else if (Color.isValidWithTransparency(color)) {
            this.color = color.substring(0, Color.COLOR_WITH_TRANSPARENCY_LENGTH - 1);
            setOpacity(Color.getOpacity(color));
        } else if (Color.isValidName(color)) {
            this.color = color;
            setOpacity(null);
        } else {
            SvgGeneratorLogger.warning(this.getClass(), "Color value '" + color + "' on gradient is invalid and therefore ignored.");
            this.color = color;
        }
    }

    public Double getOpacity() {
        return opacity;
    }

    public void setOpacity(Double opacity) {
        this.opacity = opacity;
    }

    public Double getOffset() {
        return offset;
    }

    public void setOffset(Double offset) {
        this.offset = offset;
    }

    @Override
    public Collection<Element> generateSvg(Document doc) {
        validateAttributes();
        final Element stop = doc.createElementNS(NAMESPACE, "stop");
        if (getId() != null) {
            stop.setAttributeNS(null, "id", getId());
        }
        if (getColor() != null) {
            stop.setAttributeNS(null, "stop-color", String.valueOf(getColor()));
        }
        if (getOpacity() != null) {
            stop.setAttributeNS(null, "stop-opacity", String.valueOf(getOpacity()));
        }
        if (getOffset() != null) {
            stop.setAttributeNS(null, "offset", String.valueOf(getOffset()));
        }
        return Collections.singletonList(stop);
    }

    @Override
    public void validateAttributes() throws InvalidAttributeException {
        if (getOffset() == null) {
            throw new InvalidAttributeException(this.getClass(), "Offset is null on a Stop '" + getId() + "'");
        }
        if (getColor() == null) {
            throw new InvalidAttributeException(this.getClass(), "Color is null on a Stop '" + getId() + "'");
        }
    }
}
