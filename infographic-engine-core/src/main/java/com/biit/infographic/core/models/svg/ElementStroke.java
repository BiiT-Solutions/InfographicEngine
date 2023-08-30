package com.biit.infographic.core.models.svg;

import com.biit.infographic.core.models.svg.components.StrokeLineCap;
import com.biit.infographic.core.models.svg.serialization.ElementStrokeDeserializer;
import com.biit.infographic.core.models.svg.utils.Color;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.List;

@JsonDeserialize(using = ElementStrokeDeserializer.class)
@JsonRootName(value = "border")
public class ElementStroke {

    @JsonProperty("strokeWidth")
    private Double strokeWidth;

    @JsonProperty("strokeColor")
    private String strokeColor;

    @JsonProperty("strokeDash")
    private List<Integer> strokeDash;

    @JsonProperty("strokeLinecap")
    private StrokeLineCap lineCap;

    public double getStrokeWidth() {
        if (strokeWidth == null) {
            return 0.0;
        }
        return strokeWidth;
    }

    public void setStrokeWidth(Double strokeWidth) {
        this.strokeWidth = strokeWidth;
    }

    public String getStrokeColor() {
        if (strokeColor == null) {
            return "black";
        }
        return strokeColor;
    }

    public void setStrokeColor(String strokeColor) {
        this.strokeColor = Color.checkColor(strokeColor);
    }

    public List<Integer> getStrokeDash() {
        return strokeDash;
    }

    public void setStrokeDash(List<Integer> strokeDash) {
        this.strokeDash = strokeDash;
    }

    public StrokeLineCap getLineCap() {
        if (lineCap == null) {
            return StrokeLineCap.BUTT;
        }
        return lineCap;
    }

    public void setLineCap(StrokeLineCap lineCap) {
        this.lineCap = lineCap;
    }
}
