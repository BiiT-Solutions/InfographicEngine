package com.biit.infographic.core.models.svg.serialization;

import com.biit.infographic.core.models.svg.components.SvgLine;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.Serial;

public class SvgLineDeserializer extends SvgAreaElementDeserializer<SvgLine> {

    @Serial
    private static final long serialVersionUID = -2481660135749838504L;

    public SvgLineDeserializer() {
        super(SvgLine.class);
    }

    @Override
    public void deserialize(SvgLine element, JsonNode jsonObject) throws JsonProcessingException {
        element.setX2Coordinate(DeserializerParser.parseLong("x2", jsonObject));
        element.setY2Coordinate(DeserializerParser.parseLong("y2", jsonObject));
        super.deserialize(element, jsonObject);
    }
}
