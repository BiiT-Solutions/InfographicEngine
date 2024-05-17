package com.biit.infographic.core.models.svg.serialization;

import com.biit.infographic.core.models.svg.FillAttributes;
import com.biit.infographic.core.models.svg.components.SvgLink;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.IOException;

public class SvgLinkDeserializer extends SvgElementDeserializer<SvgLink> {

    public SvgLinkDeserializer() {
        super(SvgLink.class);
    }

    @Override
    public void deserialize(SvgLink element, JsonNode jsonObject, DeserializationContext context) throws IOException {
        super.deserialize(element, jsonObject, context);
        if (jsonObject.get("href") != null) {
            element.setHref(DeserializerParser.parseString("href", jsonObject));
        }
        if (jsonObject.get("fillAttributes") != null) {
            element.setFillAttributes(ObjectMapperFactory.getObjectMapper().readValue(jsonObject.get("fillAttributes").toPrettyString(),
                    FillAttributes.class));
        }
    }
}
