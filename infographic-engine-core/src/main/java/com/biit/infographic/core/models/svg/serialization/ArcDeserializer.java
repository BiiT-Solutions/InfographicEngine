package com.biit.infographic.core.models.svg.serialization;

import com.biit.infographic.core.models.svg.components.path.Arc;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.IOException;

public class ArcDeserializer extends PathElementDeserializer<Arc> {

    public ArcDeserializer() {
        super(Arc.class);
    }

    @Override
    public void deserialize(Arc element, JsonNode jsonObject, DeserializationContext context) throws IOException {
        super.deserialize(element, jsonObject, context);
        element.setX(DeserializerParser.parseLong("x", jsonObject));
        element.setY(DeserializerParser.parseLong("y", jsonObject));
    }
}
