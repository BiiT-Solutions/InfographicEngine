package com.biit.infographic.core.models.svg.serialization;

import com.biit.infographic.core.models.svg.clip.SvgClipPath;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.IOException;

public class SvgClipPathDeserializer<T extends SvgClipPath> extends SvgElementDeserializer<T> {

    public SvgClipPathDeserializer(Class<T> aClass) {
        super(aClass);
    }

    @Override
    public void deserialize(T element, JsonNode jsonObject, DeserializationContext context) throws IOException {
        super.deserialize(element, jsonObject, context);
    }
}
