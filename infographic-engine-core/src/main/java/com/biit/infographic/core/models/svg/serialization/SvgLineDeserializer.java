package com.biit.infographic.core.models.svg.serialization;

import com.biit.infographic.core.models.svg.components.SvgLine;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.IOException;

public class SvgLineDeserializer extends SvgAreaElementDeserializer<SvgLine> {

    public SvgLineDeserializer() {
        super(SvgLine.class);
    }

    @Override
    public void deserialize(SvgLine element, JsonNode jsonObject, DeserializationContext context) throws IOException {
        element.setX2Coordinate(DeserializerParser.parseLong("x2", jsonObject));
        element.setY2Coordinate(DeserializerParser.parseLong("y2", jsonObject));
        super.deserialize(element, jsonObject, context);
    }
}
