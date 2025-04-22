package com.biit.infographic.core.models.svg.serialization;

import com.biit.infographic.core.models.svg.clip.ClipDirection;
import com.biit.infographic.core.models.svg.clip.SvgRectangleClipPath;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.Serial;

public class SvgRectangleClipPathDeserializer extends SvgClipPathDeserializer<SvgRectangleClipPath> {

    @Serial
    private static final long serialVersionUID = -4027748067580208847L;

    protected SvgRectangleClipPathDeserializer() {
        super(SvgRectangleClipPath.class);
    }

    @Override
    public void deserialize(SvgRectangleClipPath element, JsonNode jsonObject) throws JsonProcessingException {
        super.deserialize(element, jsonObject);
        if (jsonObject.get("direction") != null) {
            element.setClipDirection(ClipDirection.fromString(jsonObject.get("direction").asText()));
        }
        if (jsonObject.get("percentage") != null) {
            element.setPercentage(DeserializerParser.parseDouble("percentage", jsonObject));
        }
    }
}
