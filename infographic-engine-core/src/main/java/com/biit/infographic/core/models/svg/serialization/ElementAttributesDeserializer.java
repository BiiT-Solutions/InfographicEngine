package com.biit.infographic.core.models.svg.serialization;

import com.biit.infographic.core.models.svg.ElementAttributes;
import com.biit.infographic.core.models.svg.Unit;
import com.biit.infographic.core.models.svg.VerticalAlignment;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.IOException;

public class ElementAttributesDeserializer extends FillAttributesDeserializer<ElementAttributes> {

    protected ElementAttributesDeserializer() {
        super(ElementAttributes.class);
    }

    @Override
    public void deserialize(ElementAttributes element, JsonNode jsonObject, DeserializationContext context) throws IOException {
        super.deserialize(element, jsonObject, context);
        element.setWidth(DeserializerParser.parseLong("width", jsonObject));
        element.setWidthUnit(Unit.getUnit(DeserializerParser.parseString("widthUnit", jsonObject)));
        element.setHeight(DeserializerParser.parseLong("height", jsonObject));
        element.setHeightUnit(Unit.getUnit(DeserializerParser.parseString("heightUnit", jsonObject)));
        element.setXCoordinate(DeserializerParser.parseLong("x", jsonObject));
        element.setYCoordinate(DeserializerParser.parseLong("y", jsonObject));
        element.setStyle(DeserializerParser.parseString("style", jsonObject));
        element.setCssClass(DeserializerParser.parseString("class", jsonObject));
        element.setVerticalAlignment(VerticalAlignment.getAlignment(DeserializerParser.parseString("verticalAlign", jsonObject)));
    }
}
