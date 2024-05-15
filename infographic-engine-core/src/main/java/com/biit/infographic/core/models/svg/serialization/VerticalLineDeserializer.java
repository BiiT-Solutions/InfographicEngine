package com.biit.infographic.core.models.svg.serialization;

import com.biit.infographic.core.models.svg.components.path.VerticalLine;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.IOException;

public class VerticalLineDeserializer extends PathElementDeserializer<VerticalLine> {

    public VerticalLineDeserializer() {
        super(VerticalLine.class);
    }

    @Override
    public void deserialize(VerticalLine element, JsonNode jsonObject, DeserializationContext context) throws IOException {
        super.deserialize(element, jsonObject, context);
        element.setY(DeserializerParser.parseLong("y", jsonObject));
    }
}
