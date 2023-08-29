package com.biit.infographic.core.models.svg.components.custom;

import com.biit.infographic.core.models.svg.ElementAttributes;
import com.biit.infographic.core.models.svg.ElementType;
import com.biit.infographic.core.models.svg.SvgElement;
import com.biit.infographic.core.models.svg.SvgUtils;
import com.biit.infographic.core.models.svg.exceptions.InvalidAttributeException;
import com.biit.infographic.logger.InfograpicEngineLogger;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

@JsonRootName(value = "gauge")
public class SvgGauge extends SvgElement {
    private static final int GAUGE_WIDTH = 170;
    private static final int ARROW_AXIS_WIDTH = 10;
    private static final int GAUGE_DEGREES = 180;
    private static final int GAUGE_HEIGHT = 77;

    @JsonProperty("flip")
    private boolean flip = false;

    @JsonProperty("min")
    private double min = 0;

    @JsonProperty("min")
    private double max = 1;

    @JsonProperty("min")
    private double value = (double) 1 / 2;

    @JsonProperty("type")
    private GaugeType type;

    public SvgGauge(ElementAttributes elementAttributes) {
        super(elementAttributes);
        setElementType(ElementType.GAUGE);
        setType(GaugeType.GRADIENT);
    }

    public SvgGauge() {
        this(new ElementAttributes());
    }

    public SvgGauge(Double min, Double max, Double value) {
        this();
        setMin(min);
        setMax(max);
        setValue(value);
    }

    public SvgGauge(Long xCoordinate, Long yCoordinate, Double min, Double max, Double value) {
        this(new ElementAttributes(xCoordinate, yCoordinate, null, null, null));
        setMin(min);
        setMax(max);
        setValue(value);
    }

    public boolean isFlip() {
        return flip;
    }

    public void setFlip(boolean flip) {
        this.flip = flip;
    }

    public double getMin() {
        return min;
    }

    public void setMin(double min) {
        this.min = min;
    }

    public double getMax() {
        return max;
    }

    public void setMax(double max) {
        this.max = max;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public GaugeType getType() {
        return type;
    }

    public void setType(GaugeType type) {
        this.type = type;
    }

    public Element generateArrow(Document doc, Double min, Double max, Double value) {
        final Element arrowContainer = doc.createElementNS(NAMESPACE, "g");
        try {
            final List<Element> children = SvgUtils.getContent(new String(Files.readAllBytes(Paths.get(getClass().getClassLoader()
                    .getResource("gauge" + File.separator + "gauge_arrow.svg").toURI()))));
            for (Element child : children) {
                //Move children from one document to other.
                final Node node = doc.importNode(child, true);
                arrowContainer.appendChild(node);
            }
        } catch (Exception e) {
            InfograpicEngineLogger.errorMessage(this.getClass(), e);
        }

        //Calculate the angle of the arrow.
        if (min == null) {
            min = (double) 0;
        }

        if (max == null) {
            max = (double) 0;
        }
        if (max <= min) {
            max = min + 1;
        }
        //    Check arrow value.
        if (value == null) {
            value = min;
        } else if (value < min) {
            value = min;
        } else if (value > max) {
            value = max;
        }

        arrowContainer.setAttributeNS(null, "transform", " translate("
                + (getXPositionSelector(getElementAttributes().getXCoordinate())) + "," + (getElementAttributes().getYCoordinate() + GAUGE_HEIGHT) + ")"
                + "rotate(" + getAngle(min, max, value) + " 7.7 6.5)");
        //6 5 are the x y of the elector image to mark the rotation point

        return arrowContainer;
    }

    private long getXPositionSelector(long x) {
        return (x + (GAUGE_WIDTH / 2) - (ARROW_AXIS_WIDTH / 2));
    }

    private Double getAngle(Double min, Double max, Double value) {
        final double sum = (GAUGE_DEGREES / (max - min)) * (value - min);
        return GAUGE_DEGREES + sum;
    }


    @Override
    public Element generateSvg(Document doc) {
        final Element gauge = doc.createElementNS(NAMESPACE, "svg");
        gauge.setAttributeNS(null, "x", String.valueOf(getElementAttributes().getXCoordinate()));
        gauge.setAttributeNS(null, "y", String.valueOf(getElementAttributes().getYCoordinate()));

        try {
            final List<Element> children = SvgUtils.getContent(new String(Files.readAllBytes(Paths.get(getClass().getClassLoader()
                    .getResource("gauge" + File.separator + "gauge_gradient.svg").toURI()))).trim());
            for (Element child : children) {
                //Move children from one document to other.
                final Node node = doc.importNode(child, true);
                gauge.appendChild(node);
            }
        } catch (Exception e) {
            InfograpicEngineLogger.errorMessage(this.getClass(), e);
        }

        if (flip) {
            gauge.setAttributeNS(null, "transform", " translate(" + (getElementAttributes().getXCoordinate() + GAUGE_WIDTH) + ","
                    + (getElementAttributes().getYCoordinate()) + ") scale(-1, 1)");
        } else {
            gauge.setAttributeNS(null, "transform", " translate(" + getElementAttributes().getXCoordinate() + ","
                    + getElementAttributes().getYCoordinate() + ")");
        }

        gauge.appendChild(generateArrow(doc, min, max, value));

        elementAttributes(gauge);
        return gauge;
    }

    @Override
    public void validateAttributes() throws InvalidAttributeException {

    }
}
