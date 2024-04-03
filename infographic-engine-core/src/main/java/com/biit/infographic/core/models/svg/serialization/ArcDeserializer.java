package com.biit.infographic.core.models.svg.serialization;

import com.biit.infographic.core.models.svg.components.Arc;
import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import java.io.IOException;

public class ArcDeserializer extends StdDeserializer<Arc> {

    public ArcDeserializer() {
        super(Arc.class);
    }

    @Override
    public Arc deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
        final JsonNode jsonObject = jsonParser.getCodec().readTree(jsonParser);

        final Arc arc = new Arc();
        arc.setX(DeserializerParser.parseLong("x", jsonObject));
        arc.setY(DeserializerParser.parseLong("y", jsonObject));
        return arc;
    }
}
