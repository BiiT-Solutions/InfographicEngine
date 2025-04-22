package com.biit.infographic.core.models.svg.serialization;

import com.biit.infographic.core.models.svg.components.path.HorizontalLine;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.Serial;

public class HorizontalLineDeserializer extends PathElementDeserializer<HorizontalLine> {

    @Serial
    private static final long serialVersionUID = 6048008023696459908L;

    public HorizontalLineDeserializer() {
        super(HorizontalLine.class);
    }

    @Override
    public void deserialize(HorizontalLine element, JsonNode jsonObject) throws JsonProcessingException {
        super.deserialize(element, jsonObject);
        element.setX(DeserializerParser.parseLong("x", jsonObject));
    }
}
