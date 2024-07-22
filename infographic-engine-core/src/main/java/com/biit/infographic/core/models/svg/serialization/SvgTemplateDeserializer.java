package com.biit.infographic.core.models.svg.serialization;

import com.biit.infographic.core.models.svg.ElementType;
import com.biit.infographic.core.models.svg.LayoutType;
import com.biit.infographic.core.models.svg.SvgAreaElement;
import com.biit.infographic.core.models.svg.SvgBackground;
import com.biit.infographic.core.models.svg.SvgTemplate;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SvgTemplateDeserializer extends SvgAreaElementDeserializer<SvgTemplate> {


    public SvgTemplateDeserializer() {
        super(SvgTemplate.class);
    }

    @Override
    public void deserialize(SvgTemplate element, JsonNode jsonObject, DeserializationContext context) throws IOException {
        super.deserialize(element, jsonObject, context);
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

        final List<SvgAreaElement> templateElements = new ArrayList<>();
        final JsonNode elementsJson = jsonObject.get("elements");
        if (elementsJson != null) {
            if (elementsJson.isArray()) {
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
        }
        element.setElements(templateElements);
    }
}
