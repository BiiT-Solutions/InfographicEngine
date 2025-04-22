package com.biit.infographic.core.models.svg.serialization;

import com.biit.infographic.core.models.svg.components.SvgImage;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.Serial;

public class SvgImageDeserializer extends SvgAreaElementDeserializer<SvgImage> {

    @Serial
    private static final long serialVersionUID = 17918111966909245L;

    public SvgImageDeserializer() {
        super(SvgImage.class);
    }

    @Override
    public void deserialize(SvgImage element, JsonNode jsonObject) throws JsonProcessingException {
        super.deserialize(element, jsonObject);
        element.setContent(DeserializerParser.parseString("content", jsonObject));
        element.setResource(DeserializerParser.parseString("resource", jsonObject));
        element.setResourceAlreadyInBase64(DeserializerParser.parseBoolean("resourceAlreadyInBase64", jsonObject));
    }
}
