package com.biit.infographic.core.models.svg.components.gauge;

import com.biit.infographic.core.models.svg.ElementAttributes;
import com.biit.infographic.core.models.svg.ElementType;
import com.biit.infographic.core.models.svg.SvgElement;
import com.biit.infographic.core.models.svg.SvgUtils;
import com.biit.infographic.core.models.svg.exceptions.InvalidAttributeException;
import com.biit.infographic.core.models.svg.utils.Color;
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
import java.util.Objects;

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

    @JsonProperty("colors")
    private String[] colors = new String[]{"#ff0000", "#ff8000", "#ffd900", "#87aa00", "#678100"};

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

    public String[] getColors() {
        return colors;
    }

    public void setColors(String[] colors) {
        this.colors = colors;
    }

    public Element generateArrow(Document doc, Double min, Double max, Double value) {
        final Element arrowContainer = doc.createElementNS(NAMESPACE, "g");
        try {
            final List<Element> children = SvgUtils.getContent(Files.readString(Paths.get(getClass().getClassLoader()
                    .getResource("gauge" + File.separator + "gauge_arrow.svg").toURI())));
            for (Element child : children) {
                //Move children from one document to others.
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
            final String resouce;
            if (type == GaugeType.FIVE_VALUES) {
                resouce = "gauge" + File.separator + "gauge_five_values.svg";
            } else {
                resouce = "gauge" + File.separator + "gauge_gradient.svg";
            }
            final List<Element> children = SvgUtils.getContent(Files.readString(Paths.get(getClass().getClassLoader()
                    .getResource(resouce).toURI())).trim());
            for (Element child : children) {
                //Move children from one document to others.
                final Node node = doc.importNode(child, true);
                //Replace colors.
                setGradientColors(node);
                gauge.appendChild(node);
            }
        } catch (Exception e) {
            InfograpicEngineLogger.errorMessage(this.getClass(), e);
        }

        gauge.appendChild(generateArrow(doc, min, max, value));

        elementAttributes(gauge);
        return gauge;
    }

    private void setGradientColors(Node child) {
        if (child.getNodeName().equals("defs")) {
            int color = flip ? getColors().length - 1 : 0;
            for (int i = 0; i < child.getChildNodes().getLength(); i++) {
                if (Objects.equals(child.getChildNodes().item(i).getNodeName(), "linearGradient")) {
                    final Node gradient = child.getChildNodes().item(i);
                    for (int j = 0; j < gradient.getChildNodes().getLength(); j++) {
                        if (Objects.equals(gradient.getChildNodes().item(j).getNodeName(), "stop")) {
                            //Replace tag on style with next color.
                            ((Element) gradient.getChildNodes().item(j)).setAttributeNS(null, "style",
                                    ((Element) gradient.getChildNodes().item(j)).getAttributeNS(null, "style")
                                            .replace("CUSTOM_COLOR", Color.checkColor(getColors()[color % getColors().length])));
                            if (flip) {
                                color--;
                                if (color < 0) {
                                    color = getColors().length - 1;
                                }
                            } else {
                                color++;
                                if (color >= getColors().length) {
                                    color = 0;
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    @Override
    public void validateAttributes() throws InvalidAttributeException {

    }
}
