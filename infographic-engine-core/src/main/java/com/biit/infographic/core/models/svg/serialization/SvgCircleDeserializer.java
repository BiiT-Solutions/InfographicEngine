package com.biit.infographic.core.models.svg.serialization;

import com.biit.infographic.core.models.svg.components.SvgCircle;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.Serial;

public class SvgCircleDeserializer extends SvgAreaElementDeserializer<SvgCircle> {

    @Serial
    private static final long serialVersionUID = 5998742055189528150L;

    public SvgCircleDeserializer() {
        super(SvgCircle.class);
    }

    @Override
    public void deserialize(SvgCircle element, JsonNode jsonObject) throws JsonProcessingException {
        super.deserialize(element, jsonObject);
        element.setRadius(DeserializerParser.parseLong("radius", jsonObject));
    }
}
