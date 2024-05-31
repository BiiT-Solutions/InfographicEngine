package com.biit.infographic.core.models.svg.components;

import com.biit.infographic.core.models.svg.ElementAttributes;
import com.biit.infographic.core.models.svg.ElementType;
import com.biit.infographic.core.models.svg.SvgAreaElement;
import com.biit.infographic.core.models.svg.components.path.Point;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.util.ArrayList;
import java.util.Collection;

public class SvgCircleSector extends SvgAreaElement {
    private static final int CIRCLE_DEGREES = 360;

    @JsonProperty("radius")
    private Long radius;

    @JsonProperty("startAngle")
    private Long startAngle;

    @JsonProperty("endAngle")
    private Long endAngle;

    public SvgCircleSector(ElementAttributes elementAttributes) {
        super(elementAttributes);
        setElementType(ElementType.CIRCLE_SECTOR);
    }

    public SvgCircleSector() {
        this(new ElementAttributes());
    }


    /**
     * Create a sector.
     *
     * @param xCoordinate x for the circle.
     * @param yCoordinate y for the circle.
     * @param radius      radius of the circle.
     * @param percentage  percentage filled up [0, 1]
     */
    public SvgCircleSector(Number xCoordinate, Number yCoordinate, Number radius, Number percentage) {
        this(xCoordinate != null ? xCoordinate.longValue() : 0, yCoordinate != null ? yCoordinate.longValue() : 0,
                radius != null ? radius.longValue() : 0, 0,
                CIRCLE_DEGREES * percentage.doubleValue());
        if (percentage.doubleValue() < 0 || percentage.doubleValue() > 1) {
            throw new IllegalArgumentException("percentage must be between 0 and 1");
        }
    }

    public SvgCircleSector(Number xCoordinate, Number yCoordinate, Number radius, Number startAngle, Number endAngle) {
        this(xCoordinate != null ? xCoordinate.longValue() : 0, yCoordinate != null ? yCoordinate.longValue() : 0,
                radius != null ? radius.longValue() : 0, startAngle != null ? startAngle.longValue() : 0,
                endAngle != null ? endAngle.longValue() : 0);
    }

    public SvgCircleSector(Long xCoordinate, Long yCoordinate, Long radius, Long startAngle, Long endAngle) {
        this(new ElementAttributes(xCoordinate, yCoordinate, null, null, null));
        setRadius(radius);
        setStartAngle(startAngle);
        setEndAngle(endAngle);
        getElementStroke().setStrokeWidth(1);
    }

    public Long getRadius() {
        return radius;
    }

    public void setRadius(Long radius) {
        this.radius = radius;
    }

    public Long getStartAngle() {
        return startAngle;
    }

    public void setStartAngle(Long startAngle) {
        this.startAngle = startAngle;
    }

    public Long getEndAngle() {
        return endAngle;
    }

    public void setEndAngle(Long endAngle) {
        this.endAngle = endAngle;
    }

    @Override
    public Collection<Element> generateSvg(Document doc) {
        validateAttributes();
        final ArrayList<Element> elements = new ArrayList<>();
        final Element sector = doc.createElementNS(NAMESPACE, "path");
        elements.add(sector);

        sector.setAttributeNS(null, "d", "m " + createArc(getElementAttributes().getXCoordinate(),
                getElementAttributes().getYCoordinate(), getRadius(), getStartAngle(), getEndAngle()));
        elementStroke(sector);
        elementAttributes(sector);

        return elements;
    }


    //https://stackoverflow.com/questions/5736398/how-to-calculate-the-svg-path-for-an-arc-of-a-circle
    private String createArc(long x, long y, long radius, long startAngle, long endAngle) {
        final StringBuilder path = new StringBuilder();
        final Point start = polarToCartesian(x, y, radius, endAngle);
        final Point end = polarToCartesian(x, y, radius, startAngle);

        final int largeArcFlag = endAngle - startAngle <= CIRCLE_DEGREES / 2 ? 0 : 1;

        path.append(start.getX()).append(" ").append(start.getY()).append(" ")
                .append("A ").append(radius).append(" ").append(radius).append(" ").append("0 ")
                .append(largeArcFlag).append(" 0 ").append(end.getX()).append(" ").append(end.getY()).append(" ");

        //Close the arc if fill property is there.
        if (getElementAttributes().getFill() != null) {
            path.append("L ").append(x).append(" ").append(y).append(" Z");
        }

        return path.toString();
    }

    private Point polarToCartesian(long x, long y, long radius, long angleInDegrees) {
        final double angleInRadians = (angleInDegrees - 90) * Math.PI / 180.0;

        return new Point(x + (radius * Math.cos(angleInRadians)),
                y + (radius * Math.sin(angleInRadians)));
    }
}
