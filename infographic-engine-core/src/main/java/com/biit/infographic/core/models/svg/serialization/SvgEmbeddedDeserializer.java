package com.biit.infographic.core.models.svg.serialization;

import com.biit.infographic.core.models.svg.SvgEmbedded;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.IOException;

public class SvgEmbeddedDeserializer extends SvgAreaElementDeserializer<SvgEmbedded> {

    public SvgEmbeddedDeserializer() {
        super(SvgEmbedded.class);
    }

    @Override
    public void deserialize(SvgEmbedded element, JsonNode jsonObject, DeserializationContext context) throws IOException {
        super.deserialize(element, jsonObject, context);
        element.setResourceName(DeserializerParser.parseString("resourceName", jsonObject));
        element.setSvgCode(DeserializerParser.parseString("svgCode", jsonObject));

    }
}
