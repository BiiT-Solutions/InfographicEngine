package com.biit.infographic.core.models.svg.components;

import com.biit.infographic.core.models.svg.ElementAttributes;
import com.biit.infographic.core.models.svg.ElementType;
import com.biit.infographic.core.models.svg.SvgElement;
import com.biit.infographic.core.models.svg.exceptions.InvalidAttributeException;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.util.List;
import java.util.stream.Collectors;

public class SvgLine extends SvgElement {


    @JsonProperty("x2")
    private Long x2Coordinate;

    @JsonProperty("y2")
    private Long y2Coordinate;

    @JsonProperty("stroke-width")
    private Double strokeWidth;

    @JsonProperty("stroke")
    private String strokeColor;

    @JsonProperty("stroke-dasharray")
    private List<Integer> strokeDash;

    @JsonProperty("stroke-linecap")
    private StrokeLineCap lineCap;

    public SvgLine(ElementAttributes elementAttributes) {
        super(elementAttributes);
        setElementType(ElementType.LINE);
    }

    public SvgLine() {
        this(new ElementAttributes());
    }

    public SvgLine(Long x1Coordinate, Long y1Coordinate, Long x2Coordinate, Long y2Coordinate) {
        this(new ElementAttributes(x1Coordinate, y1Coordinate, null, null));
        setX2Coordinate(x2Coordinate);
        setY2Coordinate(y2Coordinate);
    }

    public Long getX2Coordinate() {
        return x2Coordinate;
    }

    public void setX2Coordinate(Long x2Coordinate) {
        this.x2Coordinate = x2Coordinate;
    }

    public Long getY2Coordinate() {
        return y2Coordinate;
    }

    public void setY2Coordinate(Long y2Coordinate) {
        this.y2Coordinate = y2Coordinate;
    }

    public double getStrokeWidth() {
        if (strokeWidth == null) {
            return 1.0;
        }
        return strokeWidth;
    }

    public void setStrokeWidth(double strokeWidth) {
        this.strokeWidth = strokeWidth;
    }

    public String getStrokeColor() {
        if (strokeColor == null) {
            return "black";
        }
        return strokeColor;
    }

    public void setStrokeColor(String strokeColor) {
        this.strokeColor = strokeColor;
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

    @Override
    public Element generateSvg(Document doc) {
        validateAttributes();
        final Element line = doc.createElementNS(NAMESPACE, "line");
        if (getId() != null) {
            line.setAttribute("id", getId());
        }
        line.setAttributeNS(null, "x1", String.valueOf(getElementAttributes().getXCoordinate()));
        line.setAttributeNS(null, "y1", String.valueOf(getElementAttributes().getYCoordinate()));
        line.setAttributeNS(null, "x2", String.valueOf(getX2Coordinate()));
        line.setAttributeNS(null, "y2", String.valueOf(getY2Coordinate()));
        line.setAttributeNS(null, "stroke", getStrokeColor());
        line.setAttributeNS(null, "stroke-width", String.valueOf(getStrokeWidth()));
        if (strokeDash != null && !strokeDash.isEmpty()) {
            line.setAttributeNS(null, "stroke-dasharray", strokeDash.stream().map(String::valueOf)
                    .collect(Collectors.joining(" ")));
        }
        if (lineCap != null) {
            line.setAttributeNS(null, "style", "stroke-linecap:" + getLineCap().value());
        }
        return line;
    }

    @Override
    public void validateAttributes() throws InvalidAttributeException {
        if (x2Coordinate == null) {
            throw new InvalidAttributeException(this.getClass(), "Invalid x2 on line '" + getId() + "'");
        }
        if (y2Coordinate == null) {
            throw new InvalidAttributeException(this.getClass(), "Invalid y2 on line '" + getId() + "'");
        }
    }
}
