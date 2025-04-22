package com.biit.infographic.core.models.svg.serialization;

import com.biit.infographic.core.models.svg.SvgBackground;
import com.biit.infographic.core.models.svg.SvgTemplate;
import com.biit.infographic.logger.InfographicEngineLogger;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import java.io.IOException;
import java.io.Serial;

public class SvgBackgroundDeserializer extends StdDeserializer<SvgBackground> {


    @Serial
    private static final long serialVersionUID = -4408249063967407570L;

    public SvgBackgroundDeserializer() {
        super(SvgTemplate.class);
    }

    public void deserialize(SvgBackground element, JsonNode jsonObject) throws JsonProcessingException {
        element.setBackgroundColor(DeserializerParser.parseString("backgroundColor", jsonObject));
        element.setImage(DeserializerParser.parseString("image", jsonObject));
        element.setXRadius(DeserializerParser.parseLong("xRadius", jsonObject));
        element.setYRadius(DeserializerParser.parseLong("yRadius", jsonObject));
    }


    @Override
    public SvgBackground deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
        final JsonNode jsonObject = jsonParser.getCodec().readTree(jsonParser);
        try {
            final SvgBackground svgTemplate = new SvgBackground();
            deserialize(svgTemplate, jsonObject);
            return svgTemplate;
        } catch (NullPointerException e) {
            InfographicEngineLogger.severe(this.getClass(), "Invalid node:\n" + jsonObject.toPrettyString());
            InfographicEngineLogger.errorMessage(this.getClass(), e);
            throw new RuntimeException(e);
        }
    }
}
