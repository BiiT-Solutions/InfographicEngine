package com.biit.infographic.core.models.svg.serialization;

import com.biit.infographic.core.models.svg.FillAttributes;
import com.biit.infographic.core.models.svg.components.SvgLink;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.Serial;

public class SvgLinkDeserializer extends SvgElementDeserializer<SvgLink> {

    @Serial
    private static final long serialVersionUID = -8845994575352083538L;

    public SvgLinkDeserializer() {
        super(SvgLink.class);
    }

    @Override
    public void deserialize(SvgLink element, JsonNode jsonObject) throws JsonProcessingException {
        super.deserialize(element, jsonObject);
        if (jsonObject.get("href") != null) {
            element.setHref(DeserializerParser.parseString("href", jsonObject));
        }
        if (jsonObject.get("fillAttributes") != null) {
            element.setFillAttributes(ObjectMapperFactory.getObjectMapper().readValue(jsonObject.get("fillAttributes").toPrettyString(),
                    FillAttributes.class));
        }
    }
}
