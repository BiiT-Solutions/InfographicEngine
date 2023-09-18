package com.biit.infographic.core.models.svg.serialization;

import com.biit.infographic.core.models.svg.components.SvgEllipse;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.IOException;

public class SvgEllipseDeserializer extends SvgAreaElementDeserializer<SvgEllipse> {

    public SvgEllipseDeserializer() {
        super(SvgEllipse.class);
    }

    @Override
    public void deserialize(SvgEllipse element, JsonNode jsonObject, DeserializationContext context) throws IOException {
        super.deserialize(element, jsonObject, context);
    }
}
