package com.biit.infographic.core.models.svg.serialization;

import com.biit.infographic.core.models.svg.components.gauge.GaugeType;
import com.biit.infographic.core.models.svg.components.gauge.SvgGauge;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.IOException;

public class SvgGaugeDeserializer extends SvgAreaElementDeserializer<SvgGauge> {

    public SvgGaugeDeserializer() {
        super(SvgGauge.class);
    }

    @Override
    public void deserialize(SvgGauge element, JsonNode jsonObject, DeserializationContext context) throws IOException {
        super.deserialize(element, jsonObject, context);
        element.setFlip(DeserializerParser.parseBoolean("flip", jsonObject));
        element.setMin(DeserializerParser.parseDouble("min", jsonObject));
        element.setMax(DeserializerParser.parseDouble("max", jsonObject));
        element.setValue(DeserializerParser.parseDouble("value", jsonObject));
        element.setType(GaugeType.getType(DeserializerParser.parseString("type", jsonObject)));
        if (jsonObject.get("colors") != null) {
            element.setColors(DeserializerParser.parseList("colors", jsonObject).toArray(new String[0]));
        }
    }
}
