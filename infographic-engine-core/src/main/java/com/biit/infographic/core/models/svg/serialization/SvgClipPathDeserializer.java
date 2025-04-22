package com.biit.infographic.core.models.svg.serialization;

import com.biit.infographic.core.models.svg.clip.ClipType;
import com.biit.infographic.core.models.svg.clip.SvgClipPath;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.Serial;

public class SvgClipPathDeserializer<T extends SvgClipPath> extends SvgElementDeserializer<T> {

    @Serial
    private static final long serialVersionUID = 8200928948046144676L;

    public SvgClipPathDeserializer() {
        super(null);
    }

    public SvgClipPathDeserializer(Class<T> aClass) {
        super(aClass);
    }

    @Override
    public void deserialize(T element, JsonNode jsonObject) throws JsonProcessingException {
        super.deserialize(element, jsonObject);

        if (jsonObject.get("clipType") != null) {
            element.setClipType(ClipType.fromString(jsonObject.get("clipType").asText()));
        }
        if (jsonObject.get("sourceX") != null) {
            element.setSourceX(DeserializerParser.parseLong("sourceX", jsonObject));
        }
        if (jsonObject.get("sourceY") != null) {
            element.setSourceY(DeserializerParser.parseLong("sourceY", jsonObject));
        }
        if (jsonObject.get("sourceHeight") != null) {
            element.setSourceHeight(DeserializerParser.parseLong("sourceHeight", jsonObject));
        }
        if (jsonObject.get("sourceWidth") != null) {
            element.setSourceWidth(DeserializerParser.parseLong("sourceWidth", jsonObject));
        }

    }
}
