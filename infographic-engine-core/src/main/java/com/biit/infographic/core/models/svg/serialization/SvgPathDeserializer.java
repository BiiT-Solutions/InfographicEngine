package com.biit.infographic.core.models.svg.serialization;

import com.biit.form.log.FormStructureLogger;
import com.biit.infographic.core.models.svg.ElementType;
import com.biit.infographic.core.models.svg.components.SvgPath;
import com.biit.infographic.core.models.svg.components.path.PathElement;
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

        final JsonNode elementsJson = jsonObject.get("elements");
        final List<PathElement> elements = new ArrayList<>();

        if (elementsJson != null) {
            //Handle children one by one.
            if (elementsJson.isArray()) {
                for (JsonNode childNode : elementsJson) {
                    try {
                        final ElementType type = ElementType.fromString(childNode.get("elementType").textValue());
                        if (type != null) {
                            final PathElement childElement = (PathElement) ObjectMapperFactory.getObjectMapper()
                                    .readValue(childNode.toPrettyString(), type.getRelatedClass());
                            elements.add(childElement);
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
