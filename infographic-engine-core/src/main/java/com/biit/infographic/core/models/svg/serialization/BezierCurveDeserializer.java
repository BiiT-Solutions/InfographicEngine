package com.biit.infographic.core.models.svg.serialization;

import com.biit.infographic.core.models.svg.components.path.BezierCurve;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.Serial;

public class BezierCurveDeserializer extends PathElementDeserializer<BezierCurve> {

    @Serial
    private static final long serialVersionUID = 5315409323459888641L;

    public BezierCurveDeserializer() {
        super(BezierCurve.class);
    }

    @Override
    public void deserialize(BezierCurve element, JsonNode jsonObject) throws JsonProcessingException {
        super.deserialize(element, jsonObject);
        element.setX1(DeserializerParser.parseLong("x1", jsonObject));
        element.setY1(DeserializerParser.parseLong("y1", jsonObject));
        element.setX2(DeserializerParser.parseLong("x2", jsonObject));
        element.setY2(DeserializerParser.parseLong("y2", jsonObject));
        element.setX(DeserializerParser.parseLong("x", jsonObject));
        element.setY(DeserializerParser.parseLong("y", jsonObject));
    }
}
