package com.biit.infographic.core.models.svg.serialization;

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
import com.biit.infographic.core.models.svg.Unit;
import com.biit.infographic.core.models.svg.VerticalAlignment;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.Serial;

public class ElementAttributesDeserializer extends FillAttributesDeserializer<ElementAttributes> {

    @Serial
    private static final long serialVersionUID = 7499501566491992527L;

    protected ElementAttributesDeserializer() {
        super(ElementAttributes.class);
    }

    @Override
    public void deserialize(ElementAttributes element, JsonNode jsonObject) throws JsonProcessingException {
        super.deserialize(element, jsonObject);
        element.setWidth(DeserializerParser.parseLong("width", jsonObject));
        element.setWidthUnit(Unit.getUnit(DeserializerParser.parseString("widthUnit", jsonObject)));
        element.setHeight(DeserializerParser.parseLong("height", jsonObject));
        element.setHeightUnit(Unit.getUnit(DeserializerParser.parseString("heightUnit", jsonObject)));
        element.setXCoordinate(DeserializerParser.parseLong("x", jsonObject));
        element.setYCoordinate(DeserializerParser.parseLong("y", jsonObject));
        element.setStyle(DeserializerParser.parseString("style", jsonObject));
        element.setCssClass(DeserializerParser.parseString("class", jsonObject));
        element.setVerticalAlignment(VerticalAlignment.getAlignment(DeserializerParser.parseString("verticalAlign", jsonObject)));
    }
}
