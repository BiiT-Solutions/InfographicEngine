package com.biit.infographic.core.models.svg.serialization;

import com.biit.infographic.core.models.svg.components.SvgRectangleSector;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.Serial;

public class SvgRectangleSectorDeserializer extends SvgAreaElementDeserializer<SvgRectangleSector> {

    @Serial
    private static final long serialVersionUID = 8209387759139307104L;

    public SvgRectangleSectorDeserializer() {
        super(SvgRectangleSector.class);
    }

    @Override
    public void deserialize(SvgRectangleSector element, JsonNode jsonObject) throws JsonProcessingException {
        super.deserialize(element, jsonObject);

        element.setStartAngle(DeserializerParser.parseLong("startAngle", jsonObject));
        element.setEndAngle(DeserializerParser.parseLong("endAngle", jsonObject));
        element.setPercentage(DeserializerParser.parseString("percentage", jsonObject));
    }
}
