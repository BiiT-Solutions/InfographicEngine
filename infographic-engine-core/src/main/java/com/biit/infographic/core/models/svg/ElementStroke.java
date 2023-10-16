package com.biit.infographic.core.models.svg;

import com.biit.infographic.core.models.svg.components.StrokeLineCap;
import com.biit.infographic.core.models.svg.serialization.ElementStrokeDeserializer;
import com.biit.infographic.core.models.svg.utils.Color;
import com.biit.infographic.logger.SvgGeneratorLogger;
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

    @JsonProperty("strokeOpacity")
    private Double strokeOpacity;

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
        strokeColor = Color.checkColor(strokeColor);
        if (Color.isValidWithoutTransparency(strokeColor)) {
            this.strokeColor = strokeColor;
        } else if (Color.isValidWithTransparency(strokeColor)) {
            this.strokeColor = strokeColor.substring(0, Color.COLOR_WITH_TRANSPARENCY_LENGTH - 1);
            setStrokeOpacity(Color.getOpacity(strokeColor));
        } else {
            //Some predefined tags.
            SvgGeneratorLogger.warning(this.getClass(), "Stroke color value '" + strokeColor + "' is invalid and therefore ignored.");
            this.strokeColor = strokeColor;
        }
    }

    public Double getStrokeOpacity() {
        return strokeOpacity;
    }

    public void setStrokeOpacity(Double strokeOpacity) {
        this.strokeOpacity = strokeOpacity;
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
