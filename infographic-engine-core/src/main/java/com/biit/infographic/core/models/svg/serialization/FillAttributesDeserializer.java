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

import com.biit.infographic.core.models.svg.ElementType;
import com.biit.infographic.core.models.svg.FillAttributes;
import com.biit.infographic.core.models.svg.exceptions.InvalidAttributeException;
import com.biit.infographic.logger.InfographicEngineLogger;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

public class FillAttributesDeserializer<T extends FillAttributes> extends StdDeserializer<T> {


    public FillAttributesDeserializer() {
        super(FillAttributes.class);
    }

    public FillAttributesDeserializer(Class<T> aClass) {
        super(aClass);
    }

    public void deserialize(T element, JsonNode jsonObject) throws JsonProcessingException {
        element.setFill(DeserializerParser.parseString("fill", jsonObject));
        element.setFillOpacity(DeserializerParser.parseString("fillOpacity", jsonObject));
        element.setHoverFillColor(DeserializerParser.parseString("hoverFillColor", jsonObject));
        element.setHoverFillOpacity(DeserializerParser.parseString("hoverFillOpacity", jsonObject));
    }


    @Override
    public T deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
        final JsonNode jsonObject = jsonParser.getCodec().readTree(jsonParser);

        try {
            ElementType type;

            try {
                type = ElementType.fromString(jsonObject.get("elementType").asText());
                if (type == null) {
                    type = ElementType.ELEMENT_ATTRIBUTES;
                }
            } catch (NullPointerException e) {
                type = ElementType.ELEMENT_ATTRIBUTES;
            }

            final T element = (T) type.getRelatedClass().getDeclaredConstructor().newInstance();
            deserialize(element, jsonObject);
            return element;
        } catch (InvocationTargetException | InstantiationException | IllegalAccessException | NoSuchMethodException
                 | NullPointerException e) {
            InfographicEngineLogger.severe(this.getClass(), "Invalid node:\n" + jsonObject.toPrettyString());
            InfographicEngineLogger.errorMessage(this.getClass(), e);
            throw new InvalidAttributeException(this.getClass(), e);
        }
    }
}
