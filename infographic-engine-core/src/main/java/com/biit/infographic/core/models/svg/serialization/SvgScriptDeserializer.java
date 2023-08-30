package com.biit.infographic.core.models.svg.serialization;

import com.biit.infographic.core.models.svg.components.SvgScript;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.IOException;

public class SvgScriptDeserializer extends SvgElementDeserializer<SvgScript> {

    public SvgScriptDeserializer() {
        super(SvgScript.class);
    }

    @Override
    public void deserialize(SvgScript element, JsonNode jsonObject, DeserializationContext context) throws IOException {
        super.deserialize(element, jsonObject, context);
        element.setScript(DeserializerParser.parseString("script", jsonObject));
    }
}
