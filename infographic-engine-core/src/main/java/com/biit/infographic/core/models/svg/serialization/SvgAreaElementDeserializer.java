package com.biit.infographic.core.models.svg.serialization;

import com.biit.infographic.core.models.svg.ElementAttributes;
import com.biit.infographic.core.models.svg.ElementStroke;
import com.biit.infographic.core.models.svg.ElementType;
import com.biit.infographic.core.models.svg.SvgAreaElement;
import com.biit.infographic.core.models.svg.clip.SvgClipPath;
import com.biit.infographic.core.models.svg.components.SvgLink;
import com.biit.infographic.core.models.svg.components.gradient.SvgGradient;
import com.biit.infographic.logger.InfographicEngineLogger;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;

public abstract class SvgAreaElementDeserializer<T extends SvgAreaElement> extends SvgElementDeserializer<T> {

    protected SvgAreaElementDeserializer(Class<T> aClass) {
        super(aClass);
    }

    @Override
    public void deserialize(T element, JsonNode jsonObject) throws JsonProcessingException {
        super.deserialize(element, jsonObject);

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
        if (jsonObject.get("clipPath") != null) {
            final ElementType elementType = ElementType.fromString(jsonObject.get("clipPath").get("elementType").asText());
            element.setClipPath((SvgClipPath) ObjectMapperFactory.getObjectMapper().readValue(jsonObject.get("clipPath").toPrettyString(),
                    elementType.getRelatedClass()));
        }
    }
}
