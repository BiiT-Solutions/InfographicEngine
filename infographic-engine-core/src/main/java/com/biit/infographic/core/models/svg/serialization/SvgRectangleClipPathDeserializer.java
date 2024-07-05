package com.biit.infographic.core.models.svg.serialization;

import com.biit.infographic.core.models.svg.clip.ClipDirection;
import com.biit.infographic.core.models.svg.clip.SvgRectangleClipPath;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.IOException;

public class SvgRectangleClipPathDeserializer extends SvgClipPathDeserializer<SvgRectangleClipPath> {

    protected SvgRectangleClipPathDeserializer() {
        super(SvgRectangleClipPath.class);
    }

    @Override
    public void deserialize(SvgRectangleClipPath element, JsonNode jsonObject, DeserializationContext context) throws IOException {
        super.deserialize(element, jsonObject, context);
        if (jsonObject.get("direction") != null) {
            element.setClipDirection(ClipDirection.fromString(jsonObject.get("direction").asText()));
        }
        if (jsonObject.get("percentage") != null) {
            element.setPercentage(DeserializerParser.parseDouble("percentage", jsonObject));
        }
    }
}
