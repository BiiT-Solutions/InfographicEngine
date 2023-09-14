package com.biit.infographic.core.models.svg.serialization;

import com.biit.infographic.core.models.svg.Unit;
import com.biit.infographic.core.models.svg.components.text.FontLengthAdjust;
import com.biit.infographic.core.models.svg.components.text.FontVariantType;
import com.biit.infographic.core.models.svg.components.text.FontWeight;
import com.biit.infographic.core.models.svg.components.text.SvgText;
import com.biit.infographic.core.models.svg.components.text.TextAlign;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.IOException;

public class SvgTextDeserializer extends SvgElementDeserializer<SvgText> {

    public SvgTextDeserializer() {
        super(SvgText.class);
    }

    @Override
    public void deserialize(SvgText element, JsonNode jsonObject, DeserializationContext context) throws IOException {
        super.deserialize(element, jsonObject, context);
        element.setText(DeserializerParser.parseString("contentText", jsonObject));
        element.setFontFamily(DeserializerParser.parseString("fontFamily", jsonObject));
        element.setFontSize(DeserializerParser.parseInteger("fontSize", jsonObject));
        if (DeserializerParser.parseString("fontVariant", jsonObject) != null) {
            element.setFontVariant(FontVariantType.getVariant(DeserializerParser.parseString("fontVariant", jsonObject)));
        }
        if (DeserializerParser.parseString("fontWeight", jsonObject) != null) {
            element.setFontWeight(FontWeight.getWeight(DeserializerParser.parseString("fontWeight", jsonObject)));
        }
        element.setRotate(DeserializerParser.parseLong("rotate", jsonObject));
        if (DeserializerParser.parseString("lengthAdjust", jsonObject) != null) {
            element.setLengthAdjust(FontLengthAdjust.getLengthAdjust(DeserializerParser.parseString("lengthAdjust", jsonObject)));
        }
        element.setTextLength(DeserializerParser.parseLong("textLength", jsonObject));
        element.setTextLengthUnit(Unit.getUnit(DeserializerParser.parseString("textLengthUnit", jsonObject)));
        element.setDx(DeserializerParser.parseLong("dx", jsonObject));
        element.setDxUnit(Unit.getUnit(DeserializerParser.parseString("dxUnit", jsonObject)));
        element.setDy(DeserializerParser.parseLong("dy", jsonObject));
        element.setDyUnit(Unit.getUnit(DeserializerParser.parseString("dyUnit", jsonObject)));
        element.setMaxLineLength(DeserializerParser.parseInteger("maxLineLength", jsonObject));
        element.setMaxLineWidth(DeserializerParser.parseInteger("maxLineWidth", jsonObject));
        element.setTextAlign(TextAlign.getAlignment(DeserializerParser.parseString("textAlign", jsonObject)));
    }
}
