package com.biit.infographic.core.models.svg.serialization;

import com.biit.infographic.core.models.svg.components.SvgRectangleSector;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.IOException;

public class SvgRectangleSectorDeserializer extends SvgAreaElementDeserializer<SvgRectangleSector> {

    public SvgRectangleSectorDeserializer() {
        super(SvgRectangleSector.class);
    }

    @Override
    public void deserialize(SvgRectangleSector element, JsonNode jsonObject, DeserializationContext context) throws IOException {
        super.deserialize(element, jsonObject, context);

        element.setStartAngle(DeserializerParser.parseLong("startAngle", jsonObject));
        element.setEndAngle(DeserializerParser.parseLong("endAngle", jsonObject));
        element.setPercentage(DeserializerParser.parseString("percentage", jsonObject));
    }
}
