package com.biit.infographic.core.models.svg.serialization;

import com.biit.infographic.core.models.svg.components.path.HorizontalLine;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.IOException;

public class HorizontalLineDeserializer extends PathElementDeserializer<HorizontalLine> {

    public HorizontalLineDeserializer() {
        super(HorizontalLine.class);
    }

    @Override
    public void deserialize(HorizontalLine element, JsonNode jsonObject, DeserializationContext context) throws IOException {
        super.deserialize(element, jsonObject, context);
        element.setX(DeserializerParser.parseLong("x", jsonObject));
    }
}
