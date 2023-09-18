package com.biit.infographic.core.models.svg.serialization;

import com.biit.form.log.FormStructureLogger;
import com.biit.infographic.core.models.svg.components.Point;
import com.biit.infographic.core.models.svg.components.SvgPath;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SvgPathDeserializer extends SvgAreaElementDeserializer<SvgPath> {

    public SvgPathDeserializer() {
        super(SvgPath.class);
    }

    @Override
    public void deserialize(SvgPath element, JsonNode jsonObject, DeserializationContext context) throws IOException {
        super.deserialize(element, jsonObject, context);

        final JsonNode pointsJson = jsonObject.get("points");
        final List<Point> points = new ArrayList<>();

        if (pointsJson != null) {
            //Handle children one by one.
            if (pointsJson.isArray()) {
                for (JsonNode childNode : pointsJson) {
                    try {
                        points.add(new Point(DeserializerParser.parseLong("x", childNode), DeserializerParser.parseLong("y", childNode)));
                    } catch (NullPointerException e) {
                        FormStructureLogger.severe(this.getClass().getName(), "Invalid point:\n" + jsonObject.toPrettyString());
                        FormStructureLogger.errorMessage(this.getClass().getName(), e);
                        throw new RuntimeException(e);
                    }
                }
            }
        }
        element.setPoints(points);
    }
}
