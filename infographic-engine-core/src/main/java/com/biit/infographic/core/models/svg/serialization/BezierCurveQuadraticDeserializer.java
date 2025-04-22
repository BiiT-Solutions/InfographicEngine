package com.biit.infographic.core.models.svg.serialization;

import com.biit.infographic.core.models.svg.components.path.BezierCurveQuadratic;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.Serial;

public class BezierCurveQuadraticDeserializer extends PathElementDeserializer<BezierCurveQuadratic> {

    @Serial
    private static final long serialVersionUID = -593938018235452900L;

    public BezierCurveQuadraticDeserializer() {
        super(BezierCurveQuadratic.class);
    }

    @Override
    public void deserialize(BezierCurveQuadratic element, JsonNode jsonObject) throws JsonProcessingException {
        super.deserialize(element, jsonObject);
        element.setX1(DeserializerParser.parseLong("x1", jsonObject));
        element.setY1(DeserializerParser.parseLong("y1", jsonObject));
        element.setX(DeserializerParser.parseLong("x", jsonObject));
        element.setY(DeserializerParser.parseLong("y", jsonObject));
    }
}
