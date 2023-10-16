package com.biit.infographic.core.models.svg.components.gradient;

import com.biit.infographic.core.models.svg.ElementType;
import com.biit.infographic.core.models.svg.SvgElement;
import com.biit.infographic.core.models.svg.exceptions.InvalidAttributeException;
import com.biit.infographic.core.models.svg.serialization.SvgGradientStopDeserializer;
import com.biit.infographic.core.models.svg.utils.Color;
import com.biit.infographic.logger.SvgGeneratorLogger;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

@JsonDeserialize(using = SvgGradientStopDeserializer.class)
@JsonRootName(value = "stop")
public class SvgGradientStop extends SvgElement {

    @JsonProperty("color")
    private String color;


    @JsonProperty("opacity")
    private Double opacity;


    @JsonProperty("offset")
    private Double offset;

    public SvgGradientStop() {
        super();
        setElementType(ElementType.GRADIENT_STOP);
    }

    public SvgGradientStop(String color, Double opacity, Double offset) {
        this();
        setColor(color);
        setOpacity(opacity);
        setOffset(offset);
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        color = Color.checkColor(color);
        if (Color.isValidWithoutTransparency(color)) {
            this.color = color;
        } else if (Color.isValidWithTransparency(color)) {
            this.color = color.substring(0, Color.COLOR_WITH_TRANSPARENCY_LENGTH - 1);
            setOpacity(Color.getOpacity(color));
        } else {
            SvgGeneratorLogger.warning(this.getClass(), "Color value '" + color + "' on gradient is invalid and therefore ignored.");
            this.color = color;
        }
    }

    public Double getOpacity() {
        return opacity;
    }

    public void setOpacity(Double opacity) {
        this.opacity = opacity;
    }

    public Double getOffset() {
        return offset;
    }

    public void setOffset(Double offset) {
        this.offset = offset;
    }

    @Override
    public Element generateSvg(Document doc) {
        validateAttributes();
        final Element stop = doc.createElementNS(NAMESPACE, "stop");
        if (getId() != null) {
            stop.setAttributeNS(null, "id", getId());
        }
        stop.setAttributeNS(null, "style", generateStyle(new StringBuilder()).toString());
        stop.setAttributeNS(null, "offset", String.valueOf(getOffset()));
        return stop;
    }

    protected StringBuilder generateStyle(StringBuilder style) {
        if (style == null) {
            style = new StringBuilder();
        }
        if (getColor() != null) {
            style.append("stop-color:");
            style.append(getColor());
            style.append(";");
        }

        style.append("stop-opacity:");
        style.append(getOpacity() != null ? getOpacity() : 1);
        style.append(";");

        return style;
    }

    @Override
    public void validateAttributes() throws InvalidAttributeException {
        if (getOffset() == null) {
            throw new InvalidAttributeException(this.getClass(), "Offset is null on a Stop '" + getId() + "'");
        }
        if (getColor() == null) {
            throw new InvalidAttributeException(this.getClass(), "Color is null on a Stop '" + getId() + "'");
        }
    }
}
