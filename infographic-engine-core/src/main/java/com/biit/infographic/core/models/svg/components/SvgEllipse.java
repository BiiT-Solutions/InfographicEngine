package com.biit.infographic.core.models.svg.components;

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

import com.biit.infographic.core.models.svg.ElementAttributes;
import com.biit.infographic.core.models.svg.ElementType;
import com.biit.infographic.core.models.svg.SvgAreaElement;
import com.biit.infographic.core.models.svg.serialization.SvgEllipseDeserializer;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.util.Collection;
import java.util.Collections;

@JsonDeserialize(using = SvgEllipseDeserializer.class)
@JsonRootName(value = "ellipse")
public class SvgEllipse extends SvgAreaElement {

    public SvgEllipse(ElementAttributes elementAttributes) {
        super(elementAttributes);
        setElementType(ElementType.ELLIPSE);
    }

    public SvgEllipse() {
        this(new ElementAttributes());
    }

    public SvgEllipse(Number xCoordinate, Number yCoordinate, String width, String height, String fill) {
        this(xCoordinate != null ? xCoordinate.longValue() : null, yCoordinate != null ? yCoordinate.longValue() : null, width, height, fill);
    }

    public SvgEllipse(Long xCoordinate, Long yCoordinate, String width, String height, String fill) {
        this(new ElementAttributes(xCoordinate, yCoordinate, width, height, fill));
    }

    @Override
    public Collection<Element> generateSvg(Document doc) {
        final Element ellipse = doc.createElementNS(NAMESPACE, "ellipse");
        ellipse.setAttributeNS(null, "cx", String.valueOf(getElementAttributes().getXCoordinate()));
        ellipse.setAttributeNS(null, "cy", String.valueOf(getElementAttributes().getYCoordinate()));
        if (getElementAttributes().getWidth() != null && getElementAttributes().getWidth() != 0) {
            ellipse.setAttributeNS(null, "rx", String.valueOf(generateRealWidth()));
        }
        if (getElementAttributes().getHeight() != null && getElementAttributes().getHeight() != 0) {
            ellipse.setAttributeNS(null, "ry", String.valueOf(generateRealHeight()));
        }
        elementStroke(ellipse);
        elementAttributes(ellipse);
        return Collections.singletonList(ellipse);
    }
}
