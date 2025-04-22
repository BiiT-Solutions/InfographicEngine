package com.biit.infographic.core.models.svg.serialization;

import com.biit.infographic.core.models.svg.components.gradient.SvgGradientStop;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;

public class SvgGradientStopDeserializer extends SvgElementDeserializer<SvgGradientStop> {

    public SvgGradientStopDeserializer() {
        super(SvgGradientStop.class);
    }

    @Override
    public void deserialize(SvgGradientStop element, JsonNode jsonObject) throws JsonProcessingException {
        super.deserialize(element, jsonObject);
        element.setColor(DeserializerParser.parseString("color", jsonObject));
        element.setOpacity(DeserializerParser.parseDouble("opacity", jsonObject));
        element.setOffset(DeserializerParser.parseDouble("offset", jsonObject));
    }
}
