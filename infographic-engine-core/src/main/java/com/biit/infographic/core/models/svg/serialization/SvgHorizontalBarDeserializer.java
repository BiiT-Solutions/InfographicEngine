package com.biit.infographic.core.models.svg.serialization;

import com.biit.infographic.core.models.svg.components.bars.SvgHorizontalBar;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.Serial;

public class SvgHorizontalBarDeserializer extends SvgAreaElementDeserializer<SvgHorizontalBar> {

    @Serial
    private static final long serialVersionUID = 6647656249151126896L;

    public SvgHorizontalBarDeserializer() {
        super(SvgHorizontalBar.class);
    }

    @Override
    public void deserialize(SvgHorizontalBar element, JsonNode jsonObject) throws JsonProcessingException {
        super.deserialize(element, jsonObject);
        element.setTotal(jsonObject.get("total").asDouble());
        element.setValue(jsonObject.get("value").asDouble());
    }
}
