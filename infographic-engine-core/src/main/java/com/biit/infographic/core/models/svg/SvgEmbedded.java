package com.biit.infographic.core.models.svg;

import com.biit.infographic.core.models.svg.components.gauge.GaugeType;
import com.biit.infographic.core.models.svg.exceptions.InvalidAttributeException;
import com.biit.infographic.core.models.svg.serialization.SvgEmbeddedDeserializer;
import com.biit.infographic.logger.SvgGeneratorLogger;
import com.biit.utils.file.FileReader;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
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

    public SvgEmbedded(String resourceName) throws FileNotFoundException {
        this();
        readSvg(resourceName);
    }

    public SvgEmbedded(String resourceName, Long xCoordinate, Long yCoordinate) throws FileNotFoundException {
        this(new ElementAttributes(xCoordinate, yCoordinate, null, null, null));
        readSvg(resourceName);
    }

    public String getSvgCode() {
        return svgCode;
    }

    public void setSvgCode(String svgCode) {
        this.svgCode = svgCode;
    }

    public void readSvg(String resourceName) throws FileNotFoundException {
        this.resourceName = resourceName;
        this.svgCode = FileReader.getResource(resourceName, StandardCharsets.UTF_8).trim();
    }

    public String getResourceName() {
        return resourceName;
    }

    public void setResourceName(String resourceName) throws FileNotFoundException {
        readSvg(resourceName);
    }

    @Override
    public Element generateSvg(Document doc) {
        //Wrap all inner elements in a group.
        final Element container = doc.createElementNS(NAMESPACE, "g");
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
                container.appendChild(node);
            }
        } catch (ParserConfigurationException | IOException | SAXException e) {
            throw new RuntimeException(e);
        }
        return container;
    }

    private double getScale() throws ParserConfigurationException, IOException, SAXException {
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
    }

    @Override
    public void validateAttributes() throws InvalidAttributeException {

    }
}
