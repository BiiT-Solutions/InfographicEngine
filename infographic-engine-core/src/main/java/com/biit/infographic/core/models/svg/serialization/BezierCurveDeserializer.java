package com.biit.infographic.core.models.svg.serialization;

import com.biit.infographic.core.models.svg.components.path.BezierCurve;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.IOException;

public class BezierCurveDeserializer extends PathElementDeserializer<BezierCurve> {

    public BezierCurveDeserializer() {
        super(BezierCurve.class);
    }

    @Override
    public void deserialize(BezierCurve element, JsonNode jsonObject, DeserializationContext context) throws IOException {
        super.deserialize(element, jsonObject, context);
        element.setX1(DeserializerParser.parseLong("x1", jsonObject));
        element.setY1(DeserializerParser.parseLong("y1", jsonObject));
        element.setX2(DeserializerParser.parseLong("x2", jsonObject));
        element.setY2(DeserializerParser.parseLong("y2", jsonObject));
        element.setX(DeserializerParser.parseLong("x", jsonObject));
        element.setY(DeserializerParser.parseLong("y", jsonObject));
    }
}
