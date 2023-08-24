package com.biit.infographic.core.models.svg;

import com.biit.infographic.core.models.svg.utils.Color;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.fasterxml.jackson.annotation.JsonSetter;

@JsonRootName(value = "attributes")
public class ElementAttributes {

    @JsonProperty("id")
    private String id;

    @JsonProperty("width")
    private String width;

    @JsonProperty("height")
    private String height;

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

    public ElementAttributes(Long xCoordinate, Long yCoordinate, String width, String height, String fill) {
        this(width, height, fill);
        setXCoordinate(xCoordinate);
        setYCoordinate(yCoordinate);
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getWidth() {
        return width;
    }

    public void setWidth(String width) {
        this.width = width;
    }

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
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
