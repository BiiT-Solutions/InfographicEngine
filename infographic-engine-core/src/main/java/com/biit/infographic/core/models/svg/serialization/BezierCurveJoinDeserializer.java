package com.biit.infographic.core.models.svg.serialization;

import com.biit.infographic.core.models.svg.components.path.BezierCurveJoin;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.IOException;

public class BezierCurveJoinDeserializer extends PathElementDeserializer<BezierCurveJoin> {

    public BezierCurveJoinDeserializer() {
        super(BezierCurveJoin.class);
    }

    @Override
    public void deserialize(BezierCurveJoin element, JsonNode jsonObject, DeserializationContext context) throws IOException {
        super.deserialize(element, jsonObject, context);
        element.setX2(DeserializerParser.parseLong("x2", jsonObject));
        element.setY2(DeserializerParser.parseLong("y2", jsonObject));
        element.setX(DeserializerParser.parseLong("x", jsonObject));
        element.setY(DeserializerParser.parseLong("y", jsonObject));
    }
}
