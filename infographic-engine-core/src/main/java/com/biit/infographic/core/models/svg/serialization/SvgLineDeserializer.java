package com.biit.infographic.core.models.svg.serialization;

import com.biit.infographic.core.models.svg.components.SvgLine;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.IOException;

public class SvgLineDeserializer extends SvgElementDeserializer<SvgLine> {

    public SvgLineDeserializer() {
        super(SvgLine.class);
    }

    @Override
    public void deserialize(SvgLine element, JsonNode jsonObject, DeserializationContext context) throws IOException {
        super.deserialize(element, jsonObject, context);
        element.setX2Coordinate(DeserializerParser.parseLong("x2", jsonObject));
        element.setY2Coordinate(DeserializerParser.parseLong("y2", jsonObject));
    }
}
