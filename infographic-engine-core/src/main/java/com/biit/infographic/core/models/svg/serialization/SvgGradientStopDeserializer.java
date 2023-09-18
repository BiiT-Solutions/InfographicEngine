package com.biit.infographic.core.models.svg.serialization;

import com.biit.infographic.core.models.svg.components.gradient.SvgGradientStop;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.IOException;

public class SvgGradientStopDeserializer extends SvgElementDeserializer<SvgGradientStop> {

    public SvgGradientStopDeserializer() {
        super(SvgGradientStop.class);
    }

    @Override
    public void deserialize(SvgGradientStop element, JsonNode jsonObject, DeserializationContext context) throws IOException {
        super.deserialize(element, jsonObject, context);
        element.setColor(DeserializerParser.parseString("color", jsonObject));
        element.setOpacity(DeserializerParser.parseDouble("opacity", jsonObject));
        element.setOffset(DeserializerParser.parseDouble("offset", jsonObject));
    }
}
