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
import com.biit.infographic.core.models.svg.components.path.PathElement;
import com.biit.infographic.core.models.svg.exceptions.InvalidAttributeException;
import com.biit.infographic.logger.InfographicEngineLogger;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

public abstract class PathElementDeserializer<T extends PathElement> extends StdDeserializer<T> {

    protected PathElementDeserializer(Class<T> vc) {
        super(vc);
    }

    public void deserialize(T element, JsonNode jsonObject) throws JsonProcessingException {
        element.setRelativeCoordinates(DeserializerParser.parseBoolean("relativeCoordinates", jsonObject));
    }


    @Override
    public T deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
        final JsonNode jsonObject = jsonParser.getCodec().readTree(jsonParser);

        try {
            final ElementType type = ElementType.fromString(jsonObject.get("elementType").asText());
            if (type == null) {
                return null;
            }

            final T element = (T) type.getRelatedClass().getDeclaredConstructor().newInstance();
            deserialize(element, jsonObject);
            return element;
        } catch (InvocationTargetException | InstantiationException | IllegalAccessException | NoSuchMethodException
                 | NullPointerException e) {
            InfographicEngineLogger.severe(this.getClass(), "Invalid path node:\n" + jsonObject.toPrettyString());
            InfographicEngineLogger.errorMessage(this.getClass(), e);
            throw new InvalidAttributeException(this.getClass(), e);
        }
    }
}
