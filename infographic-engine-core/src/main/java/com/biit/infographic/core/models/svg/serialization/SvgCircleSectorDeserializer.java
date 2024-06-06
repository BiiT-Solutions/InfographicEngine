package com.biit.infographic.core.models.svg.serialization;

import com.biit.infographic.core.models.svg.components.SvgCircleSector;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.IOException;

public class SvgCircleSectorDeserializer extends SvgAreaElementDeserializer<SvgCircleSector> {

    public SvgCircleSectorDeserializer() {
        super(SvgCircleSector.class);
    }

    @Override
    public void deserialize(SvgCircleSector element, JsonNode jsonObject, DeserializationContext context) throws IOException {
        super.deserialize(element, jsonObject, context);

        element.setRadius(DeserializerParser.parseLong("radius", jsonObject));
        element.setStartAngle(DeserializerParser.parseLong("startAngle", jsonObject));
        element.setEndAngle(DeserializerParser.parseLong("endAngle", jsonObject));
        element.setPercentage(DeserializerParser.parseString("percentage", jsonObject));
    }
}
