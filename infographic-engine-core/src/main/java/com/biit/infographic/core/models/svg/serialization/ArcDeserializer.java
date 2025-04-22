package com.biit.infographic.core.models.svg.serialization;

import com.biit.infographic.core.models.svg.components.path.Arc;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.Serial;

public class ArcDeserializer extends PathElementDeserializer<Arc> {

    @Serial
    private static final long serialVersionUID = 1624426468079442955L;

    public ArcDeserializer() {
        super(Arc.class);
    }

    @Override
    public void deserialize(Arc element, JsonNode jsonObject) throws JsonProcessingException {
        super.deserialize(element, jsonObject);
        element.setX(DeserializerParser.parseLong("x", jsonObject));
        element.setY(DeserializerParser.parseLong("y", jsonObject));
    }
}
