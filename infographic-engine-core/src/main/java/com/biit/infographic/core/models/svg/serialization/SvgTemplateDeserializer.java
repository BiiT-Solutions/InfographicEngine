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
import com.biit.infographic.core.models.svg.LayoutType;
import com.biit.infographic.core.models.svg.SvgAreaElement;
import com.biit.infographic.core.models.svg.SvgBackground;
import com.biit.infographic.core.models.svg.SvgTemplate;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.Serial;
import java.util.ArrayList;
import java.util.List;

public class SvgTemplateDeserializer extends SvgAreaElementDeserializer<SvgTemplate> {


    @Serial
    private static final long serialVersionUID = -3528557423415520609L;

    public SvgTemplateDeserializer() {
        super(SvgTemplate.class);
    }

    @Override
    public void deserialize(SvgTemplate element, JsonNode jsonObject) throws JsonProcessingException {
        super.deserialize(element, jsonObject);
        if (jsonObject.get("background") != null) {
            element.setSvgBackground(ObjectMapperFactory.getObjectMapper().readValue(jsonObject.get("background").toPrettyString(), SvgBackground.class));
        }
        element.setLayoutType(LayoutType.getType(DeserializerParser.parseString("type", jsonObject)));
        if (jsonObject.get("embedFonts") != null) {
            element.setEmbedFonts(DeserializerParser.parseBoolean("embedFonts", jsonObject));
        }
        if (jsonObject.get("documentSize") != null) {
            element.setDocumentSize(DeserializerParser.parseBoolean("documentSize", jsonObject));
        }
        if (jsonObject.get("uuid") != null) {
            element.setUuid(DeserializerParser.parseString("uuid", jsonObject));
        }
        if (jsonObject.get("id") != null) {
            element.setId(DeserializerParser.parseString("id", jsonObject));
        }

        final List<SvgAreaElement> templateElements = new ArrayList<>();
        final JsonNode elementsJson = jsonObject.get("elements");
        if (elementsJson != null && elementsJson.isArray()) {
            for (JsonNode elementJson : elementsJson) {
                final ElementType type = ElementType.fromString(elementJson.get("elementType").textValue());
                if (type != null) {
                    final SvgAreaElement childElement = (SvgAreaElement) ObjectMapperFactory.getObjectMapper()
                            .readValue(elementJson.toPrettyString(), type.getRelatedClass());
                    if (childElement.getElementType() == ElementType.SVG) {
                        childElement.setElementType(ElementType.NESTED_SVG);
                    }
                    templateElements.add(childElement);
                }
            }
        }
        element.setElements(templateElements);
    }
}
