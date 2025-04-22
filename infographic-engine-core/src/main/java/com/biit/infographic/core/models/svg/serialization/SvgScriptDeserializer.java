package com.biit.infographic.core.models.svg.serialization;

import com.biit.infographic.core.models.svg.components.SvgScript;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.Serial;

public class SvgScriptDeserializer extends SvgAreaElementDeserializer<SvgScript> {

    @Serial
    private static final long serialVersionUID = -5907312035542213150L;

    public SvgScriptDeserializer() {
        super(SvgScript.class);
    }

    @Override
    public void deserialize(SvgScript element, JsonNode jsonObject) throws JsonProcessingException {
        super.deserialize(element, jsonObject);
        element.setScript(DeserializerParser.parseString("script", jsonObject));
    }
}
