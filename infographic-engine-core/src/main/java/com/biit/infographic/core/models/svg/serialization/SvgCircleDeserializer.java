package com.biit.infographic.core.models.svg.serialization;

import com.biit.infographic.core.models.svg.components.SvgCircle;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.IOException;

public class SvgCircleDeserializer extends SvgAreaElementDeserializer<SvgCircle> {

    public SvgCircleDeserializer() {
        super(SvgCircle.class);
    }

    @Override
    public void deserialize(SvgCircle element, JsonNode jsonObject, DeserializationContext context) throws IOException {
        super.deserialize(element, jsonObject, context);
        element.setRadius(DeserializerParser.parseLong("radius", jsonObject));
    }
}
