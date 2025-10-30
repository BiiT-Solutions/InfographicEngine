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

import com.biit.infographic.core.models.svg.ElementStroke;
import com.biit.infographic.core.models.svg.StrokeAlign;
import com.biit.infographic.core.models.svg.components.StrokeLineCap;
import com.biit.infographic.core.models.svg.components.gradient.SvgGradient;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import java.io.IOException;

public class ElementStrokeDeserializer extends StdDeserializer<ElementStroke> {

    protected ElementStrokeDeserializer() {
        super(ElementStroke.class);
    }

    @Override
    public ElementStroke deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
        final JsonNode jsonObject = jsonParser.getCodec().readTree(jsonParser);
        final ElementStroke elementStroke = new ElementStroke();

        elementStroke.setStrokeWidth(DeserializerParser.parseDouble("strokeWidth", jsonObject));
        elementStroke.setStrokeColor(DeserializerParser.parseString("strokeColor", jsonObject));
        elementStroke.setLineCap(StrokeLineCap.get(DeserializerParser.parseString("strokeLinecap", jsonObject)));
        if (jsonObject.get("strokeDash") != null) {
            elementStroke.setStrokeDash(DeserializerParser.parseList("strokeDash", jsonObject));
        }
        if (jsonObject.get("strokeOpacity") != null) {
            elementStroke.setStrokeOpacity(DeserializerParser.parseString("strokeOpacity", jsonObject));
        }
        if (jsonObject.get("strokeAlign") != null) {
            elementStroke.setStrokeAlign(StrokeAlign.get(DeserializerParser.parseString("strokeAlign", jsonObject)));
        }
        if (jsonObject.get("gradient") != null) {
            elementStroke.setGradient(ObjectMapperFactory.getObjectMapper().readValue(jsonObject.get("gradient").toPrettyString(), SvgGradient.class));
        }

        return elementStroke;
    }
}
