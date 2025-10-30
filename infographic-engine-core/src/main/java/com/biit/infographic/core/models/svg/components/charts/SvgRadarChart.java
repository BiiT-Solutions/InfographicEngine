package com.biit.infographic.core.models.svg.components.charts;

/*-
 * #%L
 * Infographic Engine v2 (Core)
 * %%
 * Copyright (C) 2022 - 2025 BiiT Sourcing Solutions S.L.
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * #L%
 */

import com.biit.infographic.core.models.svg.Colors;
import com.biit.infographic.core.models.svg.ElementAttributes;
import com.biit.infographic.core.models.svg.ElementType;
import com.biit.infographic.core.models.svg.SvgAreaElement;
import com.biit.infographic.core.models.svg.components.SvgPath;
import com.biit.infographic.core.models.svg.components.path.Point;
import com.biit.infographic.core.models.svg.components.text.SvgText;
import com.biit.infographic.core.models.svg.components.text.TextAlign;
import com.biit.infographic.core.models.svg.exceptions.InvalidAttributeException;
import com.biit.infographic.core.models.svg.serialization.SvgRadarChartDeserializer;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import static com.biit.infographic.core.models.svg.components.text.SvgText.DEFAULT_FONT_SIZE;

@JsonDeserialize(using = SvgRadarChartDeserializer.class)
@JsonRootName(value = "radar_chart")
public class SvgRadarChart extends SvgAreaElement {

    private static final int LABELS_MARGIN = 10;
    private static final String WEB_COLOR = Colors.WHITE;

    @JsonProperty("fontSize")
    private int fontSize = DEFAULT_FONT_SIZE;

    @JsonProperty("data")
    private List<Map<String, Double>> data;

    @JsonIgnore
    private List<String> labels;

    @JsonProperty("drawRadius")
    private boolean drawRadius = false;

    @JsonProperty("drawWeb")
    private boolean drawWeb = false;

    public SvgRadarChart(ElementAttributes elementAttributes) {
        super(elementAttributes);
        setElementType(ElementType.RADAR_CHART);
    }

    public SvgRadarChart() {
        this(new ElementAttributes());
    }

    public SvgRadarChart(Number xCoordinate, Number yCoordinate, Number length) {
        this(xCoordinate != null ? xCoordinate.longValue() : null, yCoordinate != null ? yCoordinate.longValue() : null,
                length != null ? length.longValue() : null, length != null ? length.longValue() : null);
    }

    public SvgRadarChart(Long xCoordinate, Long yCoordinate, Number width, Number height) {
        this(new ElementAttributes(xCoordinate, yCoordinate, String.valueOf(width), String.valueOf(height)));
    }


    public void setData(Map<String, Double>... data) {
        this.data = Arrays.asList(data);
    }

    public void setData(List<Map<String, Double>> data) {
        this.data = data;
    }

    public int getFontSize() {
        return fontSize;
    }

    public void setFontSize(int fontSize) {
        this.fontSize = fontSize;
    }

    public boolean isDrawRadius() {
        return drawRadius;
    }

    public boolean isDrawWeb() {
        return drawWeb;
    }

    public void setDrawWeb(boolean drawWeb) {
        this.drawWeb = drawWeb;
    }

    public void setDrawRadius(boolean drawRadius) {
        this.drawRadius = drawRadius;
    }

    public List<String> getLabels() {
        if (labels == null) {
            final Set<String> dataLabels = new HashSet<>();
            for (Map<String, Double> map : data) {
                dataLabels.addAll(map.keySet());
            }
            this.labels = new ArrayList<>(dataLabels);
            Collections.sort(this.labels);
        }
        return this.labels;
    }

    public long getMaxValue() {
        long maxValueIndex = 0;
        for (String label : getLabels()) {
            final long labelMaxValue = getMaxValue(label);
            if (labelMaxValue > maxValueIndex) {
                maxValueIndex = labelMaxValue;
            }
        }
        return maxValueIndex;
    }

    public long getMaxValue(String label) {
        long maxValueIndex = 0;
        for (Map<String, Double> map : data) {
            for (Map.Entry<String, Double> entry : map.entrySet()) {
                if (entry.getKey().equals(label) && entry.getValue() > maxValueIndex) {
                    maxValueIndex = entry.getValue().longValue();
                }
            }
        }
        return maxValueIndex;
    }


    @Override
    public Collection<Element> generateSvg(Document doc) {
        validateAttributes();
        final ArrayList<Element> elements = new ArrayList<>();

        final Element background = doc.createElementNS(NAMESPACE, "rect");
        background.setAttributeNS(null, "x", String.valueOf(generateRealXCoordinate()));
        background.setAttributeNS(null, "y", String.valueOf(generateRealYCoordinate()));
        elementAttributes(background);
        elements.add(background);

        elements.addAll(drawGuideLines(doc));


        int color = 0;
        for (Map<String, Double> values : data) {
            final Element value = createArea(doc, values, color % 2 == 0 ? Colors.LIGHT_GREY : Colors.MAGENTA);
            elementStroke(value);
            elements.add(value);
            color++;
        }

        elements.addAll(generateLabels(doc));


        return elements;
    }


    @Override
    public void validateAttributes() throws InvalidAttributeException {
        super.validateAttributes();
        if (getElementAttributes().getHeight() == null) {
            throw new InvalidAttributeException(this.getClass(), "RadarChart '" + getId() + "' does not have 'height' attribute");
        }
        if (getElementAttributes().getWidth() == null) {
            throw new InvalidAttributeException(this.getClass(), "RadarChart '" + getId() + "' does not have 'width' attribute");
        }
        if (data == null || data.isEmpty()) {
            throw new InvalidAttributeException(this.getClass(), "RadarChart '" + getId() + "' does not have 'any data");
        }
    }


    private List<Element> generateLabels(Document doc) {
        final ArrayList<Element> elements = new ArrayList<>();
        for (int i = 0; i < getLabels().size(); i++) {
            final Point coordinate = polarToCartesian(
                    getElementAttributes().getXCoordinate() + getElementAttributes().getWidth() / 2,
                    getElementAttributes().getYCoordinate() + getElementAttributes().getHeight() / 2,
                    (getElementAttributes().getHeight() / 2 - LABELS_MARGIN),
                    (long) (360 / getLabels().size()) * i);
            final SvgText textLabel = new SvgText(getLabels().get(i), getFontSize(), coordinate.getX(), coordinate.getY());
            textLabel.setText(getLabels().get(i));
            textLabel.setTextAlign(TextAlign.CENTER);
            elements.addAll(textLabel.generateSvg(doc));
        }
        return elements;
    }


    private Point polarToCartesian(long x, long y, long radius, long angleInDegrees) {
        final double angleInRadians = (angleInDegrees - 90) * Math.PI / 180.0;

        return new Point(x + (radius * Math.cos(angleInRadians)),
                y + (radius * Math.sin(angleInRadians)));
    }


    private int getLabelIndex(String label) {
        return getLabels().indexOf(label);
    }


    private Element createArea(Document doc, Map<String, Double> values, String color) {
        final Element value = doc.createElementNS(NAMESPACE, "path");

        value.setAttributeNS(null, "d", "M " + createPathArea(values));
        elementStroke(value);
        value.setAttributeNS(null, "fill", color);
        value.setAttributeNS(null, "fill-opacity", "0.6");
        return value;
    }

    private String createPathArea(Map<String, Double> values) {
        final StringBuilder path = new StringBuilder();
        final long maxRadius = (getElementAttributes().getHeight() / 2) - LABELS_MARGIN * 2 - getFontSize();
        final TreeMap<String, Double> sortedLabels = new TreeMap<>(values);
        for (Map.Entry<String, Double> entry : sortedLabels.entrySet()) {
            final Point coordinate = polarToCartesian(
                    getElementAttributes().getXCoordinate() + getElementAttributes().getWidth() / 2,
                    getElementAttributes().getYCoordinate() + getElementAttributes().getHeight() / 2,
                    (long) (maxRadius * (entry.getValue() / getMaxValue(entry.getKey()))),
                    (long) (360 / getLabels().size()) * getLabelIndex(entry.getKey()));
            path.append(coordinate.getX()).append(",").append(coordinate.getY()).append(" ");
        }
        path.append("Z");
        return path.toString();
    }

    private List<Element> drawGuideLines(Document doc) {
        final ArrayList<Element> elements = new ArrayList<>();
        //Radius
        if (drawRadius) {
            for (int i = 0; i < getLabels().size(); i++) {
                final long maxRadius = (getElementAttributes().getHeight() / 2) - LABELS_MARGIN * 2 - getFontSize();

                final SvgPath radius = new SvgPath(WEB_COLOR, 1d,
                        getElementAttributes().getXCoordinate() + getElementAttributes().getWidth() / 2,
                        getElementAttributes().getYCoordinate() + getElementAttributes().getHeight() / 2,
                        polarToCartesian(
                                getElementAttributes().getXCoordinate() + getElementAttributes().getWidth() / 2,
                                getElementAttributes().getYCoordinate() + getElementAttributes().getHeight() / 2,
                                maxRadius,
                                (long) (360 / getLabels().size()) * i)
                );
                elements.addAll(radius.generateSvg(doc));
            }
        }
        if (drawWeb) {
            final long maxRadius = (getElementAttributes().getHeight() / 2) - LABELS_MARGIN * 2 - getFontSize();
            final long maxValue = getMaxValue();
            //Web
            for (int i = 0; i < maxValue - 1; i++) {
                final Element web = doc.createElementNS(NAMESPACE, "path");
                final StringBuilder path = new StringBuilder();
                for (int j = 0; j < getLabels().size(); j++) {
                    final Point coordinate = polarToCartesian(
                            getElementAttributes().getXCoordinate() + getElementAttributes().getWidth() / 2,
                            getElementAttributes().getYCoordinate() + getElementAttributes().getHeight() / 2,
                            (long) (maxRadius * ((double) (i + 1) / maxValue)),
                            (long) (360 / getLabels().size()) * j);
                    path.append(coordinate.getX()).append(",").append(coordinate.getY()).append(" ");
                }
                path.append("Z");
                web.setAttributeNS(null, "d", "M " + path);
                web.setAttributeNS(null, "fill", "none");
                web.setAttributeNS(null, "stroke", WEB_COLOR);
                web.setAttributeNS(null, "stroke-width", "1");
                elements.add(web);
            }
        }

        return elements;
    }

}
