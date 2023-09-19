package com.biit.infographic.core.models.svg.serialization;

import com.biit.infographic.core.models.svg.ElementAttributes;
import com.biit.infographic.core.models.svg.Unit;
import com.biit.infographic.core.models.svg.VerticalAlignment;
import com.biit.infographic.core.models.svg.components.gradient.SvgGradient;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import java.io.IOException;

public class ElementAttributesDeserializer extends StdDeserializer<ElementAttributes> {

    protected ElementAttributesDeserializer() {
        super(ElementAttributes.class);
    }

    @Override
    public ElementAttributes deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
        final JsonNode jsonObject = jsonParser.getCodec().readTree(jsonParser);
        final ElementAttributes elementAttributes = new ElementAttributes();

        elementAttributes.setWidth(DeserializerParser.parseLong("width", jsonObject));
        elementAttributes.setWidthUnit(Unit.getUnit(DeserializerParser.parseString("widthUnit", jsonObject)));
        elementAttributes.setHeight(DeserializerParser.parseLong("height", jsonObject));
        elementAttributes.setHeightUnit(Unit.getUnit(DeserializerParser.parseString("heightUnit", jsonObject)));
        elementAttributes.setXCoordinate(DeserializerParser.parseLong("x", jsonObject));
        elementAttributes.setYCoordinate(DeserializerParser.parseLong("y", jsonObject));
        elementAttributes.setStyle(DeserializerParser.parseString("style", jsonObject));
        elementAttributes.setFill(DeserializerParser.parseString("fill", jsonObject));
        elementAttributes.setCssClass(DeserializerParser.parseString("class", jsonObject));
        elementAttributes.setVerticalAlignment(VerticalAlignment.getAlignment(DeserializerParser.parseString("verticalAlign", jsonObject)));

        if (jsonObject.get("gradient") != null) {
            elementAttributes.setGradient(ObjectMapperFactory.getObjectMapper().readValue(jsonObject.get("gradient").toPrettyString(), SvgGradient.class));
        }

        return elementAttributes;
    }
}
