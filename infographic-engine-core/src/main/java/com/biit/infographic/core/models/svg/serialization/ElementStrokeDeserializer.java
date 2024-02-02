package com.biit.infographic.core.models.svg.serialization;

import com.biit.infographic.core.models.svg.ElementStroke;
import com.biit.infographic.core.models.svg.components.StrokeLineCap;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import java.io.IOException;

public class ElementStrokeDeserializer extends StdDeserializer<ElementStroke> {

    protected ElementStrokeDeserializer() {
        super(ElementStroke.class);
    }

    @Override
    public ElementStroke deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
        final JsonNode jsonObject = jsonParser.getCodec().readTree(jsonParser);
        final ElementStroke elementStroke = new ElementStroke();

        elementStroke.setStrokeWidth(DeserializerParser.parseDouble("strokeWidth", jsonObject));
        elementStroke.setStrokeColor(DeserializerParser.parseString("strokeColor", jsonObject));
        elementStroke.setLineCap(StrokeLineCap.get(DeserializerParser.parseString("strokeLinecap", jsonObject)));
        if (jsonObject.get("strokeDash") != null) {
            elementStroke.setStrokeDash(DeserializerParser.parseList("strokeDash", jsonObject));
        }
        if (jsonObject.get("strokeOpacity") != null) {
            elementStroke.setStrokeOpacity(DeserializerParser.parseDouble("strokeOpacity", jsonObject));
        }

        return elementStroke;
    }
}
