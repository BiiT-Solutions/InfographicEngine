package com.biit.infographic.core.models.svg.serialization;

import com.biit.infographic.core.models.svg.components.gradient.SvgGradient;
import com.biit.infographic.core.models.svg.components.gradient.SvgGradientStop;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.IOException;

public class SvgGradientDeserializer extends SvgElementDeserializer<SvgGradient> {

    public SvgGradientDeserializer() {
        super(SvgGradient.class);
    }

    @Override
    public void deserialize(SvgGradient element, JsonNode jsonObject, DeserializationContext context) throws IOException {
        super.deserialize(element, jsonObject, context);

        element.setX1Coordinate(DeserializerParser.parseLong("x1", jsonObject));
        element.setY1Coordinate(DeserializerParser.parseLong("y1", jsonObject));
        element.setX2Coordinate(DeserializerParser.parseLong("x2", jsonObject));
        element.setY2Coordinate(DeserializerParser.parseLong("y2", jsonObject));

        final JsonNode stops = jsonObject.get("stops");
        if (stops != null) {
            if (stops.isArray()) {
                for (JsonNode stop : stops) {
                    final SvgGradientStop childElement = ObjectMapperFactory.getObjectMapper()
                            .readValue(stop.toPrettyString(), SvgGradientStop.class);
                    element.addStops(childElement);
                }
            }
        }
    }
}
