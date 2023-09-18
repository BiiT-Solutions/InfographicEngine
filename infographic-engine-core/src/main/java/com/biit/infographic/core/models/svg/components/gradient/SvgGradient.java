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

    public SvgGradient() {
        super();
        setElementType(ElementType.GRADIENT);
    }

    public SvgGradient(SvgGradientStop... stops) {
        this();
        setStops(Arrays.asList(stops));
    }


    public List<SvgGradientStop> getStops() {
        return stops;
    }

    public void setStops(List<SvgGradientStop> stops) {
        this.stops = stops;
    }

    public void addStops(SvgGradientStop stop) {
        if (stops.isEmpty()) {
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
        stops.forEach(stop -> gradient.appendChild(stop.generateSvg(doc)));
        return gradient;
    }

    @Override
    public void validateAttributes() throws InvalidAttributeException {

    }
}
