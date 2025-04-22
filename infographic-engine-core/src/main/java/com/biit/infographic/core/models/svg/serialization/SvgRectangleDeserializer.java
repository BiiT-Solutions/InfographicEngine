package com.biit.infographic.core.models.svg.serialization;

import com.biit.infographic.core.models.svg.components.SvgRectangle;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.Serial;

public class SvgRectangleDeserializer extends SvgAreaElementDeserializer<SvgRectangle> {

    @Serial
    private static final long serialVersionUID = 3515330236581714514L;

    public SvgRectangleDeserializer() {
        super(SvgRectangle.class);
    }

    @Override
    public void deserialize(SvgRectangle element, JsonNode jsonObject) throws JsonProcessingException {
        super.deserialize(element, jsonObject);
        element.setXRadius(DeserializerParser.parseLong("xRadius", jsonObject));
        element.setYRadius(DeserializerParser.parseLong("yRadius", jsonObject));
    }
}
