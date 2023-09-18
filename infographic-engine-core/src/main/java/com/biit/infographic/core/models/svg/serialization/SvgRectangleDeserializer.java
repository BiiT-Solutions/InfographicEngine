package com.biit.infographic.core.models.svg.serialization;

import com.biit.infographic.core.models.svg.components.SvgRectangle;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.IOException;

public class SvgRectangleDeserializer extends SvgAreaElementDeserializer<SvgRectangle> {

    public SvgRectangleDeserializer() {
        super(SvgRectangle.class);
    }

    @Override
    public void deserialize(SvgRectangle element, JsonNode jsonObject, DeserializationContext context) throws IOException {
        super.deserialize(element, jsonObject, context);
        element.setXRadius(DeserializerParser.parseLong("xRadius", jsonObject));
        element.setYRadius(DeserializerParser.parseLong("yRadius", jsonObject));
    }
}
