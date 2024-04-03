package com.biit.infographic.core.models.svg.components;

import com.biit.infographic.core.models.svg.ElementAttributes;
import com.biit.infographic.core.models.svg.ElementType;
import com.biit.infographic.core.models.svg.SvgAreaElement;
import com.biit.infographic.core.models.svg.exceptions.InvalidAttributeException;
import com.biit.infographic.core.models.svg.serialization.SvgPathDeserializer;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

@JsonDeserialize(using = SvgPathDeserializer.class)
@JsonRootName(value = "path")
public class SvgPath extends SvgAreaElement {

    @JsonProperty("elements")
    private List<PathElement> pathElements;

    public SvgPath() {
        this(new ElementAttributes());
    }

    public SvgPath(ElementAttributes elementAttributes) {
        super(elementAttributes);
        setElementType(ElementType.PATH);
        getElementStroke().setStrokeWidth(1.0);
        elementAttributes.setFill("none");
    }

    public SvgPath(Long xCoordinate, Long yCoordinate, PathElement... pathElements) {
        this(new ElementAttributes(xCoordinate, yCoordinate, null, null, null));
        if (pathElements != null) {
            setPathElements(Arrays.asList(pathElements));
        } else {
            setPathElements(new ArrayList<>());
        }
    }

    public SvgPath(String strokeColor, Double strokeWidth, Long xCoordinate, Long yCoordinate, PathElement... pathElements) {
        this(new ElementAttributes(xCoordinate, yCoordinate, null, null, null));
        getElementStroke().setStrokeColor(strokeColor);
        getElementStroke().setStrokeWidth(strokeWidth);
        if (pathElements != null) {
            setPathElements(Arrays.asList(pathElements));
        } else {
            setPathElements(new ArrayList<>());
        }
    }

    public List<PathElement> getPathElements() {
        if (pathElements == null) {
            return new ArrayList<>();
        }
        return pathElements;
    }

    public void setPathElements(List<PathElement> pathElements) {
        this.pathElements = pathElements;
    }

    @Override
    public void validateAttributes() throws InvalidAttributeException {
        super.validateAttributes();
        if (getElementAttributes().getHeight() != null) {
            throw new InvalidAttributeException(this.getClass(), "Path '" + getId() + "' must not have 'height' attribute");
        }
        if (getElementAttributes().getWidth() != null) {
            throw new InvalidAttributeException(this.getClass(), "Path '" + getId() + "' must not have 'width' attribute");
        }
    }

    @Override
    public Collection<Element> generateSvg(Document doc) {
        validateAttributes();
        final Element path = doc.createElementNS(NAMESPACE, "path");
        path.setAttributeNS(null, "d", "m " + generateCoordinates());
        elementStroke(path);
        elementAttributes(path);
        return Collections.singletonList(path);
    }

    public String generateCoordinates() {
        final StringBuilder path = new StringBuilder();
        Long previousX;
        Long previousY;
        //If x, y on attributes are defined, we assume that are the first point.
        path.append(getElementAttributes().getXCoordinate()).append(" ").append(getElementAttributes().getYCoordinate()).append(" ");
        previousX = getElementAttributes().getXCoordinate();
        previousY = getElementAttributes().getYCoordinate();

        for (PathElement pathElement : getPathElements()) {
            path.append(pathElement.generateCoordinate(previousX, previousY));
            previousX = pathElement.getX();
            previousY = pathElement.getY();
        }
        return path.toString();
    }
}
