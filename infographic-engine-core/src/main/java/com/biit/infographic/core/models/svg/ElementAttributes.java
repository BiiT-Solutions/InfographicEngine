package com.biit.infographic.core.models.svg;

import com.biit.infographic.core.models.svg.utils.Color;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.fasterxml.jackson.annotation.JsonSetter;

@JsonRootName(value = "attributes")
public class ElementAttributes {

    @JsonProperty("width")
    private Long width;

    private Unit widthUnit = Unit.PIXELS;

    @JsonProperty("height")
    private Long height;

    private Unit heightUnit = Unit.PIXELS;

    @JsonProperty("x")
    private Long xCoordinate;

    @JsonProperty("y")
    private Long yCoordinate;

    @JsonProperty("style")
    private String style;

    @JsonProperty("fill")
    private String fill;

    @JsonProperty("verticalAlign")
    private VerticalAlignment verticalAlignment;

    public ElementAttributes() {
        super();
    }

    public ElementAttributes(String width, String height) {
        this();
        setWidth(width);
        setHeight(height);
    }

    public ElementAttributes(String width, String height, String fill) {
        this(width, height);
        setFill(fill);
    }

    public ElementAttributes(Long xCoordinate, Long yCoordinate, String width, String height) {
        this(width, height, null);
        setXCoordinate(xCoordinate);
        setYCoordinate(yCoordinate);
    }

    public ElementAttributes(Long xCoordinate, Long yCoordinate, String width, String height, String fill) {
        this(width, height, fill);
        setXCoordinate(xCoordinate);
        setYCoordinate(yCoordinate);
    }

    public String getWidthValue() {
        return getWidth() + getWidthUnit().getTag();
    }

    public Long getWidth() {
        return width;
    }

    public void setWidth(String width) {
        if (width == null) {
            this.width = null;
        } else {
            try {
                this.width = Long.parseLong(width);
                this.widthUnit = Unit.PIXELS;
            } catch (NumberFormatException e) {
                if (width.endsWith("%")) {
                    this.width = Long.parseLong(width.replaceAll("%", "").trim());
                    this.widthUnit = Unit.PERCENTAGE;
                }
            }
        }
    }

    public Long getHeight() {
        return height;
    }

    public void setHeight(String height) {
        if (height == null) {
            this.height = null;
        } else {
            try {
                this.height = Long.parseLong(height);
                this.heightUnit = Unit.PIXELS;
            } catch (NumberFormatException e) {
                if (height.endsWith("%")) {
                    this.height = Long.parseLong(height.replaceAll("%", "").trim());
                    this.heightUnit = Unit.PERCENTAGE;
                }
            }
        }
    }

    public String getHeightValue() {
        return getHeight() + getHeightUnit().getTag();
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

    @JsonSetter("y")
    public void setYCoordinate(Long yCoordinate) {
        this.yCoordinate = yCoordinate;
    }

    public String getStyle() {
        return style;
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
        if (fill != null && !fill.startsWith("#")) {
            fill = "#" + fill;
        }
        if (Color.isValidWithoutTransparency(fill)) {
            this.fill = fill;
        }
    }
}
