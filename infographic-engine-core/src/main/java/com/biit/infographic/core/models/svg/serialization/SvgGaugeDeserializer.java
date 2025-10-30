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

import com.biit.infographic.core.models.svg.components.gauge.GaugeType;
import com.biit.infographic.core.models.svg.components.gauge.SvgGauge;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.Serial;

public class SvgGaugeDeserializer extends SvgAreaElementDeserializer<SvgGauge> {

    @Serial
    private static final long serialVersionUID = -4352054756636608234L;

    public SvgGaugeDeserializer() {
        super(SvgGauge.class);
    }

    @Override
    public void deserialize(SvgGauge element, JsonNode jsonObject) throws JsonProcessingException {
        super.deserialize(element, jsonObject);
        element.setFlip(DeserializerParser.parseBoolean("flip", jsonObject));
        element.setMin(DeserializerParser.parseDouble("min", jsonObject));
        element.setMax(DeserializerParser.parseDouble("max", jsonObject));
        element.setValue(DeserializerParser.parseDouble("value", jsonObject));
        element.setType(GaugeType.getType(DeserializerParser.parseString("type", jsonObject)));
        if (jsonObject.get("colors") != null) {
            element.setColors(DeserializerParser.parseList("colors", jsonObject).toArray(new String[0]));
        }
    }
}
