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
import com.biit.infographic.core.models.svg.ElementStroke;
import com.biit.infographic.core.models.svg.ElementType;
import com.biit.infographic.core.models.svg.SvgAreaElement;
import com.biit.infographic.core.models.svg.clip.SvgClipPath;
import com.biit.infographic.core.models.svg.components.SvgLink;
import com.biit.infographic.core.models.svg.components.gradient.SvgGradient;
import com.biit.infographic.logger.InfographicEngineLogger;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;

public abstract class SvgAreaElementDeserializer<T extends SvgAreaElement> extends SvgElementDeserializer<T> {

    protected SvgAreaElementDeserializer(Class<T> aClass) {
        super(aClass);
    }

    @Override
    public void deserialize(T element, JsonNode jsonObject) throws JsonProcessingException {
        super.deserialize(element, jsonObject);

        if (jsonObject.get("commonAttributes") != null) {
            element.setElementAttributes(ObjectMapperFactory.getObjectMapper().readValue(
                    jsonObject.get("commonAttributes").toPrettyString(), ElementAttributes.class));
        } else {
            InfographicEngineLogger.warning(this.getClass(), "Element with id '{}' has no 'commonAttributes' defined.", element.getId());
        }
        if (jsonObject.get("stroke") != null) {
            element.setElementStroke(ObjectMapperFactory.getObjectMapper().readValue(jsonObject.get("stroke").toPrettyString(), ElementStroke.class));
        }
        if (jsonObject.get("gradient") != null) {
            element.setGradient(ObjectMapperFactory.getObjectMapper().readValue(jsonObject.get("gradient").toPrettyString(), SvgGradient.class));
        }
        if (jsonObject.get("link") != null) {
            element.setLink(ObjectMapperFactory.getObjectMapper().readValue(jsonObject.get("link").toPrettyString(), SvgLink.class));
        }
        if (jsonObject.get("clipPath") != null) {
            final ElementType elementType = ElementType.fromString(jsonObject.get("clipPath").get("elementType").asText());
            element.setClipPath((SvgClipPath) ObjectMapperFactory.getObjectMapper().readValue(jsonObject.get("clipPath").toPrettyString(),
                    elementType.getRelatedClass()));
        }
    }
}
