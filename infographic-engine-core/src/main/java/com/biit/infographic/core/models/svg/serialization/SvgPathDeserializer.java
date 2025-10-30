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

import com.biit.form.log.FormStructureLogger;
import com.biit.infographic.core.models.svg.ElementType;
import com.biit.infographic.core.models.svg.components.SvgPath;
import com.biit.infographic.core.models.svg.components.path.PathElement;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.Serial;
import java.util.ArrayList;
import java.util.List;

public class SvgPathDeserializer extends SvgAreaElementDeserializer<SvgPath> {

    @Serial
    private static final long serialVersionUID = 8854438003454071723L;

    public SvgPathDeserializer() {
        super(SvgPath.class);
    }

    @Override
    public void deserialize(SvgPath element, JsonNode jsonObject) throws JsonProcessingException {
        super.deserialize(element, jsonObject);

        final JsonNode elementsJson = jsonObject.get("elements");
        final List<PathElement> elements = new ArrayList<>();

        if (elementsJson != null) {
            //Handle children one by one.
            if (elementsJson.isArray()) {
                for (JsonNode childNode : elementsJson) {
                    try {
                        final ElementType type = ElementType.fromString(childNode.get("elementType").textValue());
                        if (type != null) {
                            final PathElement childElement = (PathElement) ObjectMapperFactory.getObjectMapper()
                                    .readValue(childNode.toPrettyString(), type.getRelatedClass());
                            elements.add(childElement);
                        }
                    } catch (NullPointerException e) {
                        FormStructureLogger.severe(this.getClass().getName(), "Invalid path element:\n" + jsonObject.toPrettyString());
                        FormStructureLogger.errorMessage(this.getClass().getName(), e);
                        throw new RuntimeException(e);
                    }
                }
            }
        }
        element.setPathElements(elements);
    }
}
