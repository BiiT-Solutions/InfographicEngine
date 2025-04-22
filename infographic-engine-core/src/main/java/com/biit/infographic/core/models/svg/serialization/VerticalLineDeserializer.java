package com.biit.infographic.core.models.svg.serialization;

import com.biit.infographic.core.models.svg.components.path.VerticalLine;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.Serial;

public class VerticalLineDeserializer extends PathElementDeserializer<VerticalLine> {

    @Serial
    private static final long serialVersionUID = 2085873833605577167L;

    public VerticalLineDeserializer() {
        super(VerticalLine.class);
    }

    @Override
    public void deserialize(VerticalLine element, JsonNode jsonObject) throws JsonProcessingException {
        super.deserialize(element, jsonObject);
        element.setY(DeserializerParser.parseLong("y", jsonObject));
    }
}
