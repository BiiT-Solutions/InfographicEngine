package com.biit.infographic.core.models.svg.serialization;

import com.biit.infographic.core.models.svg.components.SvgCircleSector;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.Serial;

public class SvgCircleSectorDeserializer extends SvgAreaElementDeserializer<SvgCircleSector> {

    @Serial
    private static final long serialVersionUID = -528026102743433935L;

    public SvgCircleSectorDeserializer() {
        super(SvgCircleSector.class);
    }

    @Override
    public void deserialize(SvgCircleSector element, JsonNode jsonObject) throws JsonProcessingException {
        super.deserialize(element, jsonObject);

        element.setRadius(DeserializerParser.parseLong("radius", jsonObject));
        element.setStartAngle(DeserializerParser.parseLong("startAngle", jsonObject));
        element.setEndAngle(DeserializerParser.parseLong("endAngle", jsonObject));
        element.setPercentage(DeserializerParser.parseString("percentage", jsonObject));
    }
}
