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

import com.biit.infographic.core.models.svg.clip.ClipType;
import com.biit.infographic.core.models.svg.clip.SvgClipPath;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.Serial;

public class SvgClipPathDeserializer<T extends SvgClipPath> extends SvgElementDeserializer<T> {

    @Serial
    private static final long serialVersionUID = 8200928948046144676L;

    public SvgClipPathDeserializer() {
        super(null);
    }

    public SvgClipPathDeserializer(Class<T> aClass) {
        super(aClass);
    }

    @Override
    public void deserialize(T element, JsonNode jsonObject) throws JsonProcessingException {
        super.deserialize(element, jsonObject);

        if (jsonObject.get("clipType") != null) {
            element.setClipType(ClipType.fromString(jsonObject.get("clipType").asText()));
        }
        if (jsonObject.get("sourceX") != null) {
            element.setSourceX(DeserializerParser.parseLong("sourceX", jsonObject));
        }
        if (jsonObject.get("sourceY") != null) {
            element.setSourceY(DeserializerParser.parseLong("sourceY", jsonObject));
        }
        if (jsonObject.get("sourceHeight") != null) {
            element.setSourceHeight(DeserializerParser.parseLong("sourceHeight", jsonObject));
        }
        if (jsonObject.get("sourceWidth") != null) {
            element.setSourceWidth(DeserializerParser.parseLong("sourceWidth", jsonObject));
        }

    }
}
