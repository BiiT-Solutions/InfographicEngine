package com.biit.infographic.core.models.svg.serialization;

import com.biit.infographic.core.models.svg.Unit;
import com.biit.infographic.core.models.svg.components.text.FontLengthAdjust;
import com.biit.infographic.core.models.svg.components.text.FontStyle;
import com.biit.infographic.core.models.svg.components.text.FontVariantType;
import com.biit.infographic.core.models.svg.components.text.FontWeight;
import com.biit.infographic.core.models.svg.components.text.LetterCase;
import com.biit.infographic.core.models.svg.components.text.SvgText;
import com.biit.infographic.core.models.svg.components.text.TextAlign;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.Serial;

public class SvgTextDeserializer extends SvgAreaElementDeserializer<SvgText> {

    @Serial
    private static final long serialVersionUID = -1966618220058304890L;

    public SvgTextDeserializer() {
        super(SvgText.class);
    }

    @Override
    public void deserialize(SvgText element, JsonNode jsonObject) throws JsonProcessingException {
        super.deserialize(element, jsonObject);
        element.setText(DeserializerParser.parseString("contentText", jsonObject));
        element.setFontFamily(DeserializerParser.parseString("fontFamily", jsonObject));
        element.setFontSize(DeserializerParser.parseInteger("fontSize", jsonObject));
        if (DeserializerParser.parseString("fontVariant", jsonObject) != null) {
            element.setFontVariant(FontVariantType.getVariant(DeserializerParser.parseString("fontVariant", jsonObject)));
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
        element.setMaxParagraphHeight(DeserializerParser.parseInteger("maxParagraphHeight", jsonObject));
        element.setMinLineSeparation(DeserializerParser.parseInteger("minLineSeparation", jsonObject));
        element.setSamePhraseLineSeparator(DeserializerParser.parseInteger("samePhraseLineSeparator", jsonObject));
        element.setTextAlign(TextAlign.getAlignment(DeserializerParser.parseString("textAlign", jsonObject)));
        element.setLetterCase(LetterCase.getCase(DeserializerParser.parseString("letterCase", jsonObject)));
        if (DeserializerParser.parseString("fontWeight", jsonObject) != null) {
            element.setFontWeight(FontWeight.getWeight(DeserializerParser.parseString("fontWeight", jsonObject)));
        }
        if (DeserializerParser.parseString("fontStyle", jsonObject) != null) {
            element.setFontStyle(FontStyle.getStyle(DeserializerParser.parseString("fontStyle", jsonObject)));
        }
    }
}
