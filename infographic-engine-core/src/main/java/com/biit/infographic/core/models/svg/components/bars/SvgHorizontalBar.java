package com.biit.infographic.core.models.svg.components.bars;

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
import com.biit.infographic.core.models.svg.StrokeAlign;
import com.biit.infographic.core.models.svg.components.SvgRectangle;
import com.biit.infographic.core.models.svg.serialization.SvgHorizontalBarDeserializer;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.util.ArrayList;
import java.util.Collection;

@JsonDeserialize(using = SvgHorizontalBarDeserializer.class)
@JsonRootName(value = "horizontal_bar")
public class SvgHorizontalBar extends SvgRectangle {

    @JsonProperty("value")
    private double value = 0;

    @JsonProperty("total")
    private double total = 1;

    public SvgHorizontalBar(ElementAttributes elementAttributes) {
        super(elementAttributes);
        setElementType(ElementType.HORIZONTAL_BAR);
    }

    public SvgHorizontalBar() {
        this(new ElementAttributes());
    }

    public SvgHorizontalBar(Number xCoordinate, Number yCoordinate, Number width, Number height, Number value, Number total) {
        this(xCoordinate != null ? xCoordinate.longValue() : null, yCoordinate != null ? yCoordinate.longValue() : null,
                width != null ? width.longValue() : null, height != null ? height.longValue() : null,
                value != null ? value.doubleValue() : null, total != null ? total.doubleValue() : null);
    }

    public SvgHorizontalBar(Long xCoordinate, Long yCoordinate, Number width, Number height, Double value, Double total) {
        this(new ElementAttributes(xCoordinate, yCoordinate, String.valueOf(width), String.valueOf(height)));
        setValue(value);
        setTotal(total);
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    @Override
    public Collection<Element> generateSvg(Document doc) {
        validateAttributes();
        final ArrayList<Element> elements = new ArrayList<>();

        final Element baseRectangle = doc.createElementNS(NAMESPACE, "rect");
        elements.add(baseRectangle);
        baseRectangle.setAttributeNS(null, "x", String.valueOf(generateRealXCoordinate()));
        baseRectangle.setAttributeNS(null, "y", String.valueOf(generateRealYCoordinate()));
        if (getXRadius() != null && getXRadius() != 0) {
            baseRectangle.setAttributeNS(null, "rx", String.valueOf(getXRadius()));
        }
        if (getYRadius() != null && getYRadius() != 0) {
            baseRectangle.setAttributeNS(null, "ry", String.valueOf(getYRadius()));
        }
        if (getElementStroke() != null && getElementStroke().getStrokeAlign() == StrokeAlign.OUTSET) {
            final Collection<Element> strokes = createOuterStroke(doc);
            elements.addAll(strokes);
        } else {
            elementStroke(baseRectangle);
        }
        baseRectangle.setAttributeNS(null, "fill", Colors.GREY);
        elementAttributes(baseRectangle);

        final Element valueRectangle = doc.createElementNS(NAMESPACE, "rect");
        elements.add(valueRectangle);
        valueRectangle.setAttributeNS(null, "x", String.valueOf(generateRealXCoordinate()));
        valueRectangle.setAttributeNS(null, "y", String.valueOf(generateRealYCoordinate()));
        if (getXRadius() != null && getXRadius() != 0) {
            valueRectangle.setAttributeNS(null, "rx", String.valueOf(getXRadius()));
        }
        if (getYRadius() != null && getYRadius() != 0) {
            valueRectangle.setAttributeNS(null, "ry", String.valueOf(getYRadius()));
        }
        if (getElementStroke() != null && getElementStroke().getStrokeAlign() == StrokeAlign.OUTSET) {
            final Collection<Element> strokes = createOuterStroke(doc);
            elements.addAll(strokes);
        } else {
            elementStroke(valueRectangle);
        }
        valueRectangle.setAttributeNS(null, "fill", Colors.MAGENTA);
        elementAttributes(valueRectangle);


        //Get proportions.
        valueRectangle.setAttributeNS(null, "width",
                getElementAttributes().getWidth() * (value / total) + getElementAttributes().getWidthUnit().getValue());


        return elements;
    }

}
