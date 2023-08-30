package com.biit.infographic.core.models.svg.serialization;

import com.biit.infographic.core.models.svg.SvgBackground;
import com.biit.infographic.core.models.svg.SvgTemplate;
import com.biit.infographic.logger.InfographicEngineLogger;
import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import java.io.IOException;

public class SvgBackgroundDeserializer extends StdDeserializer<SvgBackground> {


    public SvgBackgroundDeserializer() {
        super(SvgTemplate.class);
    }

    public void deserialize(SvgBackground element, JsonNode jsonObject, DeserializationContext context) throws IOException {
        element.setBackgroundColor(DeserializerParser.parseString("backgroundColor", jsonObject));
        element.setImage(DeserializerParser.parseString("backgroundColor", jsonObject));
    }


    @Override
    public SvgBackground deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JacksonException {
        final JsonNode jsonObject = jsonParser.getCodec().readTree(jsonParser);
        try {
            final SvgBackground svgTemplate = new SvgBackground();
            deserialize(svgTemplate, jsonObject, deserializationContext);
            return svgTemplate;
        } catch (NullPointerException e) {
            InfographicEngineLogger.severe(this.getClass().getName(), "Invalid node:\n" + jsonObject.toPrettyString());
            InfographicEngineLogger.errorMessage(this.getClass().getName(), e);
            throw new RuntimeException(e);
        }
    }
}
