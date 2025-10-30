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

import com.biit.infographic.core.models.svg.components.gradient.SvgGradient;
import com.biit.infographic.core.models.svg.components.gradient.SvgGradientStop;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;

public class SvgGradientDeserializer extends SvgElementDeserializer<SvgGradient> {

    public SvgGradientDeserializer() {
        super(SvgGradient.class);
    }

    @Override
    public void deserialize(SvgGradient element, JsonNode jsonObject) throws JsonProcessingException {
        super.deserialize(element, jsonObject);

        element.setX1Coordinate(DeserializerParser.parseLong("x1", jsonObject));
        element.setY1Coordinate(DeserializerParser.parseLong("y1", jsonObject));
        element.setX2Coordinate(DeserializerParser.parseLong("x2", jsonObject));
        element.setY2Coordinate(DeserializerParser.parseLong("y2", jsonObject));

        final JsonNode stops = jsonObject.get("stops");
        if (stops != null && stops.isArray()) {
            for (JsonNode stop : stops) {
                final SvgGradientStop childElement = ObjectMapperFactory.getObjectMapper()
                        .readValue(stop.toPrettyString(), SvgGradientStop.class);
                element.addStops(childElement);
            }
        }
    }
}
