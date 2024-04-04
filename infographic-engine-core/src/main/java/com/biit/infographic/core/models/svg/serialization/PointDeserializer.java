package com.biit.infographic.core.models.svg.serialization;

import com.biit.infographic.core.models.svg.components.Point;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import java.io.IOException;

public class PointDeserializer extends StdDeserializer<Point> {

    public PointDeserializer() {
        super(Point.class);
    }

    @Override
    public Point deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
        final JsonNode jsonObject = jsonParser.getCodec().readTree(jsonParser);

        final Point svgPoint = new Point();
        svgPoint.setX(DeserializerParser.parseLong("x", jsonObject));
        svgPoint.setY(DeserializerParser.parseLong("y", jsonObject));
        return svgPoint;
    }
}
