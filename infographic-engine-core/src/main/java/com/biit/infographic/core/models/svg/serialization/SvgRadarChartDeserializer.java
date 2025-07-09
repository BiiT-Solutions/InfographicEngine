package com.biit.infographic.core.models.svg.serialization;

import com.biit.infographic.core.models.svg.components.charts.SvgRadarChart;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.Serial;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SvgRadarChartDeserializer extends SvgAreaElementDeserializer<SvgRadarChart> {

    @Serial
    private static final long serialVersionUID = 8209387759139307104L;

    public SvgRadarChartDeserializer() {
        super(SvgRadarChart.class);
    }

    @Override
    public void deserialize(SvgRadarChart element, JsonNode jsonObject) throws JsonProcessingException {
        super.deserialize(element, jsonObject);

        element.setFontSize(DeserializerParser.parseInteger("fontSize", jsonObject));
        element.setDrawRadius(DeserializerParser.parseBoolean("drawRadius", jsonObject));
        element.setDrawWeb(DeserializerParser.parseBoolean("drawWeb", jsonObject));
        element.setData(parseListMap("data", jsonObject));
    }


    private static List<Map<String, Double>> parseListMap(String name, JsonNode jsonObject) throws JsonProcessingException {
        final JsonNode valuesJson = jsonObject.get(name);
        if (valuesJson != null) {
            return ObjectMapperFactory.getObjectMapper().readValue(valuesJson.toPrettyString(),
                    new TypeReference<>() {
                    });
        }
        return new ArrayList<>();
    }
}
