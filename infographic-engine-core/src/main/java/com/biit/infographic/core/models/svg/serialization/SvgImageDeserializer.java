package com.biit.infographic.core.models.svg.serialization;

import com.biit.infographic.core.models.svg.components.SvgImage;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.IOException;

public class SvgImageDeserializer extends SvgAreaElementDeserializer<SvgImage> {

    public SvgImageDeserializer() {
        super(SvgImage.class);
    }

    @Override
    public void deserialize(SvgImage element, JsonNode jsonObject, DeserializationContext context) throws IOException {
        super.deserialize(element, jsonObject, context);
        element.setContent(DeserializerParser.parseString("content", jsonObject));
        element.setResource(DeserializerParser.parseString("resource", jsonObject));
        element.setResourceAlreadyInBase64(DeserializerParser.parseBoolean("resourceAlreadyInBase64", jsonObject));
    }
}
