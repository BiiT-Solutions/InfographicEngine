package com.biit.infographic.core.models.svg;

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

import com.biit.infographic.core.files.SvgSearcher;
import com.biit.infographic.core.models.svg.exceptions.InvalidAttributeException;
import com.biit.infographic.core.models.svg.exceptions.InvalidCodeException;
import com.biit.infographic.core.models.svg.serialization.SvgEmbeddedDeserializer;
import com.biit.infographic.logger.InfographicEngineLogger;
import com.biit.infographic.logger.SvgGeneratorLogger;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

@JsonDeserialize(using = SvgEmbeddedDeserializer.class)
public class SvgEmbedded extends SvgAreaElement {

    @JsonProperty("resourceName")
    private String resourceName;

    @JsonProperty("svgCode")
    private String svgCode;

    public SvgEmbedded(ElementAttributes elementAttributes) {
        super(elementAttributes);
        setElementType(ElementType.EMBEDDED_SVG);
    }

    public SvgEmbedded() {
        this(new ElementAttributes());
        setElementStroke(new ElementStroke());
        setElementType(ElementType.EMBEDDED_SVG);
    }

    public SvgEmbedded(String resourceName) {
        this();
        readSvg(resourceName);
    }

    public SvgEmbedded(String resourceName, Number xCoordinate, Number yCoordinate) {
        this(resourceName, xCoordinate != null ? xCoordinate.longValue() : null, yCoordinate != null ? yCoordinate.longValue() : null);
    }

    public SvgEmbedded(String resourceName, Long xCoordinate, Long yCoordinate) {
        this(new ElementAttributes(xCoordinate, yCoordinate, null, null, null));
        readSvg(resourceName);
    }

    public String getSvgCode() {
        return svgCode;
    }

    public void setSvgCode(String svgCode) {
        this.svgCode = svgCode;
    }

    public void readSvg(String resourceName) {
        this.resourceName = resourceName;
        this.svgCode = SvgSearcher.getFileAsString(resourceName);
    }

    public String getResourceName() {
        return resourceName;
    }

    public void setResourceName(String resourceName) {
        readSvg(resourceName);
    }

    @Override
    public Collection<Element> generateSvg(Document doc) {
        //Wrap all inner elements in a group.
        final Element container = doc.createElementNS(NAMESPACE, "g");
        //Maybe is a drools variable just updated and has not read the resource after.
        if (svgCode == null && resourceName != null) {
            readSvg(resourceName);
        }
        try {
            container.setAttributeNS(null, "transform", " translate("
                    + (getElementAttributes().getXCoordinate()) + "," + getElementAttributes().getYCoordinate() + ")"
                    + " scale(" + getScale() + ")");

            //Search for inner 'g' that contains the elements of the svg.
            List<Element> children = SvgUtils.getContent(this.svgCode, Collections.singletonList("g"));
            if (children.isEmpty()) {
                //Not in group? takes all children.
                children = SvgUtils.getContent(this.svgCode, null);
            }
            for (Element child : children) {
                //Move children from one document to others.
                final Node node = doc.importNode(child, true);
                //Force fill if set.
                if (node instanceof Element && getElementAttributes().getFill() != null) {
                    ((Element) node).setAttributeNS(null, "fill", getElementAttributes().getFill());
                }
                container.appendChild(node);
            }
        } catch (ParserConfigurationException | IOException | SAXException e) {
            throw new RuntimeException(e);
        } catch (InvalidCodeException e) {
            InfographicEngineLogger.severe(this.getClass(), this + " has not valid svg code to embed.");
            throw e;
        }
        return Collections.singletonList(container);
    }

    /**
     * Scale uses the complete SVG size, not the drawing. If you want something accurate, ensure that the document fits the content.
     *
     * @return the % for scaling.
     * @throws ParserConfigurationException
     * @throws IOException
     * @throws SAXException
     */
    private double getScale() throws ParserConfigurationException, IOException, SAXException {
        try {
            final Document doc = SvgUtils.stringToSvg(svgCode);
            if (getElementAttributes().getWidth() == null && getElementAttributes().getHeight() == null) {
                return 1D;
            }
            Long width;
            try {
                width = Long.parseLong(doc.getDocumentElement().getAttribute("width"));
            } catch (NumberFormatException e) {
                width = getElementAttributes().getWidth();
                SvgGeneratorLogger.debug(this.getClass(), "No width found on Imported SVG");
            }
            Long height;
            try {
                height = Long.parseLong(doc.getDocumentElement().getAttribute("height"));
            } catch (NumberFormatException e) {
                height = getElementAttributes().getHeight();
                SvgGeneratorLogger.debug(this.getClass(), "No width found on Imported SVG");
            }

            final double widthRatio;
            if (getElementAttributes().getWidth() != null && width != null && width != 0) {
                widthRatio = (double) getElementAttributes().getWidth() / width;
            } else {
                widthRatio = 1D;
            }

            final double heightRatio;
            if (getElementAttributes().getHeight() != null && height != null && height != 0) {
                heightRatio = (double) getElementAttributes().getHeight() / height;
            } else {
                heightRatio = 1D;
            }

            return Math.min(heightRatio, widthRatio);
        } catch (InvalidCodeException e) {
            return 1.0;
        }
    }

    @Override
    public void validateAttributes() throws InvalidAttributeException {
        //No validation yet.
    }

    @Override
    public String toString() {
        return "SvgEmbedded{"
                + "id='" + getId() + '\''
                + ", elementType=" + getElementType()
                + ", resourceName='" + resourceName + '\''
                + '}';
    }
}
