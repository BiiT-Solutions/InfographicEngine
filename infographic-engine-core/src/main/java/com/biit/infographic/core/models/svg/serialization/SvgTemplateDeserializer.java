package com.biit.infographic.core.models.svg.serialization;

import com.biit.infographic.core.models.svg.ElementType;
import com.biit.infographic.core.models.svg.LayoutType;
import com.biit.infographic.core.models.svg.SvgBackground;
import com.biit.infographic.core.models.svg.SvgElement;
import com.biit.infographic.core.models.svg.SvgTemplate;
import com.biit.infographic.logger.InfographicEngineLogger;
import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public abstract class SvgTemplateDeserializer extends StdDeserializer<SvgTemplate> {


    public SvgTemplateDeserializer() {
        super(SvgTemplate.class);
    }

    public void deserialize(SvgTemplate element, JsonNode jsonObject, DeserializationContext context) throws IOException {
        element.setSvgBackground(ObjectMapperFactory.getObjectMapper().readValue("background", SvgBackground.class));
        element.setLayoutType(LayoutType.valueOf(DeserializerParser.parseString("type", jsonObject)));

        final List<SvgElement> templateElements = new ArrayList<>();
        final JsonNode elementsJson = jsonObject.get("elements");
        if (elementsJson != null) {
            if (elementsJson.isArray()) {
                for (JsonNode elementJson : elementsJson) {
                    final ElementType type = ElementType.fromString(elementJson.get("type").asText());
                    if (type != null) {
                        templateElements.add((SvgElement) ObjectMapperFactory.getObjectMapper()
                                .readValue(elementJson.toPrettyString(), type.getRelatedClass()));
                    }
                }
            }
        }
        element.setElements(templateElements);
    }


    @Override
    public SvgTemplate deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JacksonException {
        final JsonNode jsonObject = jsonParser.getCodec().readTree(jsonParser);
        try {
            final SvgTemplate svgTemplate = new SvgTemplate();
            deserialize(svgTemplate, jsonObject, deserializationContext);
            return svgTemplate;
        } catch (NullPointerException e) {
            InfographicEngineLogger.severe(this.getClass().getName(), "Invalid node:\n" + jsonObject.toPrettyString());
            InfographicEngineLogger.errorMessage(this.getClass().getName(), e);
            throw new RuntimeException(e);
        }
    }
}
