package com.biit.infographic.core.models.svg;

import com.biit.infographic.core.models.svg.components.gradient.SvgGradient;
import com.biit.infographic.core.models.svg.serialization.ElementAttributesDeserializer;
import com.biit.infographic.core.models.svg.utils.Color;
import com.biit.infographic.logger.SvgGeneratorLogger;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.Objects;

@JsonDeserialize(using = ElementAttributesDeserializer.class)
@JsonRootName(value = "commonAttributes")
public class ElementAttributes {

    @JsonProperty("width")
    private Long width;

    @JsonProperty("widthUnit")
    private Unit widthUnit = Unit.PIXELS;

    @JsonProperty("height")
    private Long height;

    @JsonProperty("heightUnit")
    private Unit heightUnit = Unit.PIXELS;

    @JsonProperty("x")
    private Long xCoordinate;

    @JsonProperty("y")
    private Long yCoordinate;

    @JsonProperty("style")
    private String style;

    @JsonProperty("fill")
    private String fill;

    @JsonProperty("fillOpacity")
    private String fillOpacity;

    @JsonProperty("class")
    private String cssClass;

    @JsonProperty("verticalAlign")
    private VerticalAlignment verticalAlignment;

    public ElementAttributes() {
        super();
    }

    public ElementAttributes(String width, String height) {
        this();
        setWidthValue(width);
        setHeightValue(height);
    }

    public ElementAttributes(String width, String height, String fill) {
        this(width, height);
        setFill(fill);
    }

    public ElementAttributes(Number xCoordinate, Number yCoordinate, String width, String height) {
        this(xCoordinate != null ? xCoordinate.longValue() : null, yCoordinate != null ? yCoordinate.longValue() : null, width, height);
    }

    public ElementAttributes(Long xCoordinate, Long yCoordinate, String width, String height) {
        this(width, height, null);
        setXCoordinate(xCoordinate);
        setYCoordinate(yCoordinate);
    }

    public ElementAttributes(Number xCoordinate, Number yCoordinate, String width, String height, String fill) {
        this(xCoordinate != null ? xCoordinate.longValue() : null, yCoordinate != null ? yCoordinate.longValue() : null, width, height, fill);
    }

    public ElementAttributes(Long xCoordinate, Long yCoordinate, String width, String height, String fill) {
        this(width, height, fill);
        setXCoordinate(xCoordinate);
        setYCoordinate(yCoordinate);
    }

    public ElementAttributes(Number xCoordinate, Number yCoordinate, String width, String height, String fill, Double fillOpacity) {
        this(xCoordinate != null ? xCoordinate.longValue() : null, yCoordinate != null ? yCoordinate.longValue() : null, width, height, fill, fillOpacity);
    }

    public ElementAttributes(Long xCoordinate, Long yCoordinate, String width, String height, String fill, Double fillOpacity) {
        this(width, height, fill);
        setXCoordinate(xCoordinate);
        setYCoordinate(yCoordinate);
        setFillOpacity(fillOpacity);
    }

    @JsonIgnore
    public String getWidthValue() {
        return getWidth() + getWidthUnit().getValue();
    }

    public Long getWidth() {
        return width;
    }

    public void setWidth(Integer width) {
        setWidth(Long.valueOf(width));
    }

    public void setWidth(Long width) {
        this.width = width;
    }

    @JsonIgnore
    public void setWidthValue(String width) {
        this.width = Unit.getValue(width);
        this.widthUnit = Unit.getUnit(width);
    }

    public Long getHeight() {
        return height;
    }

    public void setHeight(Integer height) {
        setHeight(Long.valueOf(height));
    }

    public void setHeight(Long height) {
        this.height = height;
    }

    @JsonIgnore
    public void setHeightValue(String height) {
        this.height = Unit.getValue(height);
        this.heightUnit = Unit.getUnit(height);
    }

    @JsonIgnore
    public String getHeightValue() {
        return getHeight() + getHeightUnit().getValue();
    }

    public Unit getWidthUnit() {
        return widthUnit;
    }

    public void setWidthUnit(Unit widthUnit) {
        this.widthUnit = widthUnit;
    }

    public Unit getHeightUnit() {
        return heightUnit;
    }

    public void setHeightUnit(Unit heightUnit) {
        this.heightUnit = heightUnit;
    }

    @JsonGetter("x")
    public long getXCoordinate() {
        if (xCoordinate == null) {
            return 0;
        }
        return xCoordinate;
    }

    public void setXCoordinate(Number xCoordinate) {
        this.xCoordinate = xCoordinate != null ? xCoordinate.longValue() : null;
    }

    @JsonSetter("x")
    public void setXCoordinate(Long xCoordinate) {
        this.xCoordinate = xCoordinate;
    }

    @JsonGetter("y")
    public long getYCoordinate() {
        if (yCoordinate == null) {
            return 0;
        }
        return yCoordinate;
    }

    public void setYCoordinate(Number yCoordinate) {
        this.yCoordinate = yCoordinate != null ? yCoordinate.longValue() : null;
    }

    @JsonSetter("y")
    public void setYCoordinate(Long yCoordinate) {
        this.yCoordinate = yCoordinate;
    }

    public String getStyle() {
        if (style == null) {
            return "";
        }
        return style;
    }

    public void addStyle(String style) {
        if (this.style == null) {
            this.style = "";
        }
        if (!this.style.isEmpty()) {
            this.style = this.style.concat(";");
        }
        this.style = this.style.concat(style);
    }

    public void setStyle(String style) {
        this.style = style;
    }

    public VerticalAlignment getVerticalAlignment() {
        return verticalAlignment;
    }

    public void setVerticalAlignment(VerticalAlignment verticalAlignment) {
        this.verticalAlignment = verticalAlignment;
    }

    public String getFill() {
        return fill;
    }

    public void setFill(String fill) {
        fill = Color.checkColor(fill);
        if (Color.isDroolsVariable(fill)) {
            this.fill = fill;
        } else if (Color.isValidWithoutTransparency(fill)) {
            this.fill = fill;
        } else if (Color.isValidWithTransparency(fill)) {
            this.fill = fill.substring(0, Color.COLOR_WITH_TRANSPARENCY_LENGTH - 1);
            setFillOpacity(String.valueOf(Color.getOpacity(fill)));
        } else if (Color.isValidName(fill)) {
            this.fill = fill;
            setFillOpacity((String) null);
        } else {
            //Some predefined tags.
            if (Objects.equals("none", fill)) {
                this.fill = fill;
            } else {
                SvgGeneratorLogger.warning(this.getClass(), "Fill value '" + fill + "' is invalid and therefore ignored.");
                this.fill = fill;
            }
        }
    }

    public String getFillOpacity() {
        return fillOpacity;
    }

    public void setFillOpacity(Double fillOpacity) {
        setFillOpacity(String.valueOf(fillOpacity));
    }

    public void setFillOpacity(String fillOpacity) {
        try {
            if (fillOpacity == null || "null".equals(fillOpacity)) {
                this.fillOpacity = null;
            } else if (Color.isDroolsVariable(fillOpacity)) {
                this.fillOpacity = fillOpacity;
            } else if (Double.parseDouble(fillOpacity) < 0 || Double.parseDouble(fillOpacity) > 1) {
                SvgGeneratorLogger.warning(this.getClass(), "Opacity value '" + fillOpacity + "' is invalid and therefore ignored.");
            } else {
                this.fillOpacity = fillOpacity;
            }
        } catch (NumberFormatException e) {
            SvgGeneratorLogger.severe(this.getClass(), "Opacity value '" + fillOpacity + "' is invalid!.");
        }
    }

    public String getCssClass() {
        return cssClass;
    }

    public void setCssClass(String cssClass) {
        this.cssClass = cssClass;
    }
}
