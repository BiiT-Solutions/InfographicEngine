package com.biit.infographic.core.models.svg.serialization;

import com.biit.form.log.FormStructureLogger;
import com.biit.infographic.core.models.svg.components.Arc;
import com.biit.infographic.core.models.svg.components.PathElement;
import com.biit.infographic.core.models.svg.components.Point;
import com.biit.infographic.core.models.svg.components.SvgPath;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class SvgPathDeserializer extends SvgAreaElementDeserializer<SvgPath> {

    public SvgPathDeserializer() {
        super(SvgPath.class);
    }

    @Override
    public void deserialize(SvgPath element, JsonNode jsonObject, DeserializationContext context) throws IOException {
        super.deserialize(element, jsonObject, context);

        final JsonNode elementsJson = jsonObject.get("elements");
        final List<PathElement> elements = new ArrayList<>();

        if (elementsJson != null) {
            //Handle children one by one.
            if (elementsJson.isArray()) {
                for (JsonNode childNode : elementsJson) {
                    try {
                        if (DeserializerParser.parseString("element", childNode) != null
                                && Objects.equals(DeserializerParser.parseString("element", childNode), Arc.ELEMENT_NAME)) {
                            //Is an arc
                            elements.add(new Arc(DeserializerParser.parseLong("x", childNode), DeserializerParser.parseLong("y", childNode)));
                        } else {
                            //Is a point
                            elements.add(new Point(DeserializerParser.parseLong("x", childNode), DeserializerParser.parseLong("y", childNode)));
                        }
                    } catch (NullPointerException e) {
                        FormStructureLogger.severe(this.getClass().getName(), "Invalid path element:\n" + jsonObject.toPrettyString());
                        FormStructureLogger.errorMessage(this.getClass().getName(), e);
                        throw new RuntimeException(e);
                    }
                }
            }
        }
        element.setPathElements(elements);
    }
}
