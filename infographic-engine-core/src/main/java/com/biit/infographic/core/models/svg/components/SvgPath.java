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
import java.util.List;

@JsonDeserialize(using = SvgPathDeserializer.class)
@JsonRootName(value = "path")
public class SvgPath extends SvgAreaElement {

    @JsonProperty("points")
    private List<Point> points;

    public SvgPath() {
        this(new ElementAttributes());
    }

    public SvgPath(ElementAttributes elementAttributes) {
        super(elementAttributes);
        setElementType(ElementType.PATH);
        getElementStroke().setStrokeWidth(1.0);
        elementAttributes.setFill("none");
    }

    public SvgPath(Long xCoordinate, Long yCoordinate, Point... points) {
        this(new ElementAttributes(xCoordinate, yCoordinate, null, null, null));
        if (points != null) {
            setPoints(Arrays.asList(points));
        } else {
            setPoints(new ArrayList<>());
        }
    }

    public SvgPath(String strokeColor, Double strokeWidth, Long xCoordinate, Long yCoordinate, Point... points) {
        this(new ElementAttributes(xCoordinate, yCoordinate, null, null, null));
        getElementStroke().setStrokeColor(strokeColor);
        getElementStroke().setStrokeWidth(strokeWidth);
        if (points != null) {
            setPoints(Arrays.asList(points));
        } else {
            setPoints(new ArrayList<>());
        }
    }

    public List<Point> getPoints() {
        if (points == null) {
            return new ArrayList<>();
        }
        return points;
    }

    public void setPoints(List<Point> points) {
        this.points = points;
    }

    @Override
    public void validateAttributes() throws InvalidAttributeException {
        super.validateAttributes();
        if (getElementAttributes().getHeight() != null) {
            throw new InvalidAttributeException(this.getClass(), "Point '" + getId() + "' must not have 'height' attribute");
        }
        if (getElementAttributes().getWidth() != null) {
            throw new InvalidAttributeException(this.getClass(), "Point '" + getId() + "' must not have 'width' attribute");
        }
    }

    @Override
    public Element generateSvg(Document doc) {
        validateAttributes();
        final Element path = doc.createElementNS(NAMESPACE, "path");
        path.setAttributeNS(null, "d", "m " + generateCoordinates());
        elementStroke(path);
        elementAttributes(path);
        return path;
    }

    public String generateCoordinates() {
        final StringBuilder path = new StringBuilder();
        Long previousX = null;
        Long previousY = null;
        //If x, y on attributes are defined, we assume that are the first point.
        path.append(generateCoordinate(null, null, getElementAttributes().getXCoordinate(), getElementAttributes().getYCoordinate()));
        previousX = getElementAttributes().getXCoordinate();
        previousY = getElementAttributes().getYCoordinate();

        for (Point point : getPoints()) {
            path.append(generateCoordinate(previousX, previousY, point.getX(), point.getY()));
            previousX = point.getX();
            previousY = point.getY();
        }
        return path.toString();
    }

    private String generateCoordinate(Long previousX, Long previousY, Long currentX, Long currentY) {
        final StringBuilder coordinate = new StringBuilder();
        if (previousX == null) {
            coordinate.append(currentX);
        } else {
            coordinate.append(currentX - previousX);
        }
        coordinate.append(",");
        if (previousY == null) {
            coordinate.append(currentY);
        } else {
            coordinate.append(currentY - previousY);
        }
        coordinate.append(" ");
        return coordinate.toString();
    }
}
