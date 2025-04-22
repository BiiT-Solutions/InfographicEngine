package com.biit.infographic.core.models.svg.serialization;

import com.biit.infographic.core.models.svg.components.gauge.GaugeType;
import com.biit.infographic.core.models.svg.components.gauge.SvgGauge;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.Serial;

public class SvgGaugeDeserializer extends SvgAreaElementDeserializer<SvgGauge> {

    @Serial
    private static final long serialVersionUID = -4352054756636608234L;

    public SvgGaugeDeserializer() {
        super(SvgGauge.class);
    }

    @Override
    public void deserialize(SvgGauge element, JsonNode jsonObject) throws JsonProcessingException {
        super.deserialize(element, jsonObject);
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
