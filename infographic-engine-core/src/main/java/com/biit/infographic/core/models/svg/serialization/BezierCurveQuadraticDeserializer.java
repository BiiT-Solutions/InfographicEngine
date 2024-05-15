package com.biit.infographic.core.models.svg.serialization;

import com.biit.infographic.core.models.svg.components.path.BezierCurveQuadratic;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.IOException;

public class BezierCurveQuadraticDeserializer extends PathElementDeserializer<BezierCurveQuadratic> {

    public BezierCurveQuadraticDeserializer() {
        super(BezierCurveQuadratic.class);
    }

    @Override
    public void deserialize(BezierCurveQuadratic element, JsonNode jsonObject, DeserializationContext context) throws IOException {
        super.deserialize(element, jsonObject, context);
        element.setX1(DeserializerParser.parseLong("x1", jsonObject));
        element.setY1(DeserializerParser.parseLong("y1", jsonObject));
        element.setX(DeserializerParser.parseLong("x", jsonObject));
        element.setY(DeserializerParser.parseLong("y", jsonObject));
    }
}
