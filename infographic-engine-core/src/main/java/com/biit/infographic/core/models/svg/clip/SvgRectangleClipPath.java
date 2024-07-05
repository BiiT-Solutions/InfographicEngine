package com.biit.infographic.core.models.svg.clip;

import com.biit.infographic.core.models.svg.ElementAttributes;
import com.biit.infographic.core.models.svg.SvgAreaElement;
import com.biit.infographic.core.models.svg.components.SvgRectangle;
import com.biit.infographic.core.models.svg.serialization.SvgRectangleClipPathDeserializer;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@JsonDeserialize(using = SvgRectangleClipPathDeserializer.class)
@JsonRootName(value = "rectangleClipPath")
public class SvgRectangleClipPath extends SvgClipPath {

    @JsonProperty("percentage")
    private double percentage;

    @JsonProperty("direction")
    private ClipDirection clipDirection = ClipDirection.LEFT_TO_RIGHT;

    public SvgRectangleClipPath() {
        super();
    }

    public SvgRectangleClipPath(double percentage, ClipDirection clipDirection) {
        this.percentage = percentage;
        this.clipDirection = clipDirection;
    }

    public double getPercentage() {
        if (percentage > 1) {
            return 1D;
        }
        if (percentage < 0) {
            return 0D;
        }
        return percentage;
    }

    public void setPercentage(double percentage) {
        this.percentage = percentage;
    }

    public ClipDirection getClipDirection() {
        return clipDirection;
    }

    public void setClipDirection(ClipDirection clipDirection) {
        this.clipDirection = clipDirection;
    }

    @Override
    protected SvgAreaElement generateArea() {
        final SvgRectangle area = new SvgRectangle();
        //Obtain rectangle size.
        final ElementAttributes elementAttributes = new ElementAttributes();
        elementAttributes.setHeight(getHeight(getSourceHeight()));
        elementAttributes.setWidth(getWidth(getSourceWidth()));
        elementAttributes.setXCoordinate(getX(getSourceX()));
        elementAttributes.setYCoordinate(getY(getSourceY()));
        area.setElementAttributes(elementAttributes);
        return area;
    }

    private Long getHeight(Long sourceHeight) {
        if (clipDirection == ClipDirection.TOP_TO_BOTTOM || clipDirection == ClipDirection.BOTTOM_TO_TOP) {
            return Math.round(sourceHeight * getPercentage());
        }
        return sourceHeight;
    }

    private Long getWidth(Long sourceWidth) {
        if (clipDirection == ClipDirection.RIGHT_TO_LEFT || clipDirection == ClipDirection.LEFT_TO_RIGHT) {
            return Math.round(sourceWidth * getPercentage());
        }
        return sourceWidth;
    }

    private Long getX(long sourceX) {
        if (clipDirection == ClipDirection.LEFT_TO_RIGHT || clipDirection == ClipDirection.TOP_TO_BOTTOM || clipDirection == ClipDirection.BOTTOM_TO_TOP) {
            return sourceX;
        }
        return Math.round(sourceX + getSourceWidth() - (getSourceWidth() * getPercentage()));
    }

    private Long getY(long sourceY) {
        if (clipDirection == ClipDirection.LEFT_TO_RIGHT || clipDirection == ClipDirection.TOP_TO_BOTTOM || clipDirection == ClipDirection.RIGHT_TO_LEFT) {
            return sourceY;
        }
        //Return percentage missing to Y.
        return Math.round(sourceY + getSourceHeight() - (getSourceHeight() * getPercentage()));
    }
}
