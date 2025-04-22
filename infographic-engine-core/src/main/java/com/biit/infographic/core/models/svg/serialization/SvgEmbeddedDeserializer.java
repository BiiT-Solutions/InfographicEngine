package com.biit.infographic.core.models.svg.serialization;

import com.biit.infographic.core.models.svg.SvgEmbedded;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.Serial;

public class SvgEmbeddedDeserializer extends SvgAreaElementDeserializer<SvgEmbedded> {

    @Serial
    private static final long serialVersionUID = -5050243057709804770L;

    public SvgEmbeddedDeserializer() {
        super(SvgEmbedded.class);
    }

    @Override
    public void deserialize(SvgEmbedded element, JsonNode jsonObject) throws JsonProcessingException {
        super.deserialize(element, jsonObject);
        element.setResourceName(DeserializerParser.parseString("resourceName", jsonObject));
        element.setSvgCode(DeserializerParser.parseString("svgCode", jsonObject));

    }
}
