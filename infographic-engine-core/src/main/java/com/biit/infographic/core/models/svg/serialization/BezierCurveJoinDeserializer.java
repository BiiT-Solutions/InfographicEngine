package com.biit.infographic.core.models.svg.serialization;

import com.biit.infographic.core.models.svg.components.path.BezierCurveJoin;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.Serial;

public class BezierCurveJoinDeserializer extends PathElementDeserializer<BezierCurveJoin> {

    @Serial
    private static final long serialVersionUID = -1324413259072548418L;

    public BezierCurveJoinDeserializer() {
        super(BezierCurveJoin.class);
    }

    @Override
    public void deserialize(BezierCurveJoin element, JsonNode jsonObject) throws JsonProcessingException {
        super.deserialize(element, jsonObject);
        element.setX2(DeserializerParser.parseLong("x2", jsonObject));
        element.setY2(DeserializerParser.parseLong("y2", jsonObject));
        element.setX(DeserializerParser.parseLong("x", jsonObject));
        element.setY(DeserializerParser.parseLong("y", jsonObject));
    }
}
