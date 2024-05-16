package com.biit.infographic.core.models.svg.serialization;

import com.biit.infographic.core.models.svg.ElementAttributes;
import com.biit.infographic.core.models.svg.ElementStroke;
import com.biit.infographic.core.models.svg.SvgAreaElement;
import com.biit.infographic.core.models.svg.components.SvgLink;
import com.biit.infographic.core.models.svg.components.gradient.SvgGradient;
import com.biit.infographic.logger.InfographicEngineLogger;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.IOException;

public abstract class SvgAreaElementDeserializer<T extends SvgAreaElement> extends SvgElementDeserializer<T> {

    public SvgAreaElementDeserializer(Class<T> aClass) {
        super(aClass);
    }

    @Override
    public void deserialize(T element, JsonNode jsonObject, DeserializationContext context) throws IOException {
        super.deserialize(element, jsonObject, context);

        if (jsonObject.get("commonAttributes") != null) {
            element.setElementAttributes(ObjectMapperFactory.getObjectMapper().readValue(
                    jsonObject.get("commonAttributes").toPrettyString(), ElementAttributes.class));
        } else {
            InfographicEngineLogger.warning(this.getClass(), "Element with id '{}' has no 'commonAttributes' defined.", element.getId());
        }
        if (jsonObject.get("stroke") != null) {
            element.setElementStroke(ObjectMapperFactory.getObjectMapper().readValue(jsonObject.get("stroke").toPrettyString(), ElementStroke.class));
        }
        if (jsonObject.get("gradient") != null) {
            element.setGradient(ObjectMapperFactory.getObjectMapper().readValue(jsonObject.get("gradient").toPrettyString(), SvgGradient.class));
        }
        if (jsonObject.get("link") != null) {
            element.setLink(ObjectMapperFactory.getObjectMapper().readValue(jsonObject.get("link").toPrettyString(), SvgLink.class));
        }
    }
}
