package com.biit.infographic.core.models.svg.components.gradient;

import com.biit.infographic.core.models.svg.ElementType;
import com.biit.infographic.core.models.svg.SvgElement;
import com.biit.infographic.core.models.svg.exceptions.InvalidAttributeException;
import com.biit.infographic.core.models.svg.serialization.SvgGradientDeserializer;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@JsonDeserialize(using = SvgGradientDeserializer.class)
@JsonRootName(value = "gradient")
public class SvgGradient extends SvgElement {
    public static final String ID_PREFIX = "linearGradient";

    @JsonProperty("stops")
    private List<SvgGradientStop> stops;

    @JsonProperty("x1")
    private Long x1Coordinate;

    @JsonProperty("y1")
    private Long y1Coordinate;

    @JsonProperty("x2")
    private Long x2Coordinate;

    @JsonProperty("y2")
    private Long y2Coordinate;

    public SvgGradient() {
        super();
        setElementType(ElementType.GRADIENT);
    }

    public SvgGradient(SvgGradientStop... stops) {
        this();
        setStops(Arrays.asList(stops));
    }

    public Long getX1Coordinate() {
        return x1Coordinate;
    }

    public void setX1Coordinate(Long x1Coordinate) {
        this.x1Coordinate = x1Coordinate;
    }

    public Long getY1Coordinate() {
        return y1Coordinate;
    }

    public void setY1Coordinate(Long y1Coordinate) {
        this.y1Coordinate = y1Coordinate;
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

    public List<SvgGradientStop> getStops() {
        return stops;
    }

    public void setStops(List<SvgGradientStop> stops) {
        this.stops = stops;
    }

    public void addStops(SvgGradientStop stop) {
        if (stops == null) {
            stops = new ArrayList<>();
        }
        stops.add(stop);
    }

    @Override
    public Element generateSvg(Document doc) {
        final Element gradient = doc.createElementNS(NAMESPACE, "linearGradient");
        if (getId() != null) {
            gradient.setAttributeNS(null, "id", getId());
        }
        if (getX1Coordinate() != null) {
            gradient.setAttributeNS(null, "x1", String.valueOf(getX1Coordinate()));
        }
        if (getY1Coordinate() != null) {
            gradient.setAttributeNS(null, "y1", String.valueOf(getY1Coordinate()));
        }
        if (getX2Coordinate() != null) {
            gradient.setAttributeNS(null, "x2", String.valueOf(getX2Coordinate()));
        }
        if (getY2Coordinate() != null) {
            gradient.setAttributeNS(null, "y2", String.valueOf(getY2Coordinate()));
        }
        // Needed to be shown correctly on browsers: gradientUnits="userSpaceOnUse"
        gradient.setAttributeNS(null, "gradientUnits", "userSpaceOnUse");
        stops.forEach(stop -> gradient.appendChild(stop.generateSvg(doc)));
        return gradient;
    }

    @Override
    public void validateAttributes() throws InvalidAttributeException {

    }
}
