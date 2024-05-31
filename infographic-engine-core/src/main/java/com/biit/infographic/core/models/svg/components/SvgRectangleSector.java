package com.biit.infographic.core.models.svg.components;

import com.biit.infographic.core.exceptions.InvalidParameterException;
import com.biit.infographic.core.models.svg.ElementAttributes;
import com.biit.infographic.core.models.svg.ElementType;
import com.biit.infographic.core.models.svg.SvgAreaElement;
import com.biit.infographic.core.models.svg.components.path.Point;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.util.ArrayList;
import java.util.Collection;

public class SvgRectangleSector extends SvgAreaElement {
    private static final double LATERAL_TO_RADIUS = 0.8;

    private static final int CIRCLE_DEGREES = 360;
    private static final int FIRST_CORNER_DEGREES = 45;
    private static final int SECOND_CORNER_DEGREES = 135;
    private static final int THIRD_CORNER_DEGREES = 225;
    private static final int FORTH_CORNER_DEGREES = 315;

    @JsonProperty("startAngle")
    private Long startAngle;

    @JsonProperty("endAngle")
    private Long endAngle;

    public SvgRectangleSector(ElementAttributes elementAttributes) {
        super(elementAttributes);
        setElementType(ElementType.CIRCLE_SECTOR);
    }

    public SvgRectangleSector() {
        this(new ElementAttributes());
    }

    public SvgRectangleSector(Number xCoordinate, Number yCoordinate, String width, String height, Number percentage) {
        this(xCoordinate != null ? xCoordinate.longValue() : 0, yCoordinate != null ? yCoordinate.longValue() : 0,
                width, height,
                0,
                CIRCLE_DEGREES * percentage.doubleValue());
        if (percentage.doubleValue() < 0 || percentage.doubleValue() > 1) {
            throw new IllegalArgumentException("percentage must be between 0 and 1");
        }
    }

    public SvgRectangleSector(Number xCoordinate, Number yCoordinate, String width, String height, Number startAngle, Number endAngle) {
        this(xCoordinate != null ? xCoordinate.longValue() : 0, yCoordinate != null ? yCoordinate.longValue() : 0,
                width, height,
                startAngle != null ? startAngle.longValue() : 0,
                endAngle != null ? endAngle.longValue() : 0);
    }

    public SvgRectangleSector(Long xCoordinate, Long yCoordinate, String width, String height, Long startAngle, Long endAngle) {
        this(new ElementAttributes(xCoordinate, yCoordinate, width, height, null));
        setStartAngle(startAngle);
        setEndAngle(endAngle);
        getElementStroke().setStrokeWidth(1);
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

        sector.setAttributeNS(null, "d", "M " + createSection());
        elementStroke(sector);
        elementAttributes(sector);

        return elements;
    }


    private String createSection() {
        final StringBuilder path = new StringBuilder();

        //Start at the middle.
        final Point center = new Point(getElementAttributes().getXCoordinate() + (getElementAttributes().getWidth() / 2),
                getElementAttributes().getYCoordinate() - (getElementAttributes().getHeight() / 2));
        path.append(center.getX()).append(" ").append(center.getY()).append(" ");

        //Go to the first intersection of the angle with the square.
        final Point angleZeroIntersection = getIntesectionPoint(getStartAngle());
        path.append(angleZeroIntersection.getX()).append(",").append(angleZeroIntersection.getY()).append(" ");
        //Add needed square corners;
        if (getStartAngle() < FIRST_CORNER_DEGREES && getEndAngle() > FIRST_CORNER_DEGREES) {
            path.append(getElementAttributes().getXCoordinate() + getElementAttributes().getWidth()).append(",")
                    .append(getElementAttributes().getYCoordinate() - getElementAttributes().getHeight()).append(" ");
        }

        if (getStartAngle() < SECOND_CORNER_DEGREES && getEndAngle() > SECOND_CORNER_DEGREES) {
            path.append(getElementAttributes().getXCoordinate() + getElementAttributes().getWidth()).append(",")
                    .append(getElementAttributes().getYCoordinate()).append(" ");
        }

        if (getStartAngle() < THIRD_CORNER_DEGREES && getEndAngle() > THIRD_CORNER_DEGREES) {
            path.append(getElementAttributes().getXCoordinate()).append(",")
                    .append(getElementAttributes().getYCoordinate()).append(" ");
        }

        if (getStartAngle() < FORTH_CORNER_DEGREES && getEndAngle() > FORTH_CORNER_DEGREES) {
            path.append(getElementAttributes().getXCoordinate() + getElementAttributes().getWidth()).append(",")
                    .append(getElementAttributes().getYCoordinate()).append(" ");
        }

        final Point angleIntersection = getIntesectionPoint(getEndAngle());
        path.append(angleIntersection.getX()).append(",").append(angleIntersection.getY()).append(" Z");

        return path.toString();
    }


    private Point getIntesectionPoint(long angleInDegrees) {
        final Point center = new Point(getElementAttributes().getXCoordinate() + (double) getElementAttributes().getWidth() / 2,
                getElementAttributes().getYCoordinate() - (double) getElementAttributes().getHeight() / 2);
        final Point anglePoint = polarToCartesian(center.getX(), center.getY(),
                (long) (Math.max(getElementAttributes().getWidth(), getElementAttributes().getHeight()) * LATERAL_TO_RADIUS),
                angleInDegrees);
        final Point firstLateralPoint = getStartSquareLateral(angleInDegrees);
        final Point lastLateralPoint = getEndSquareLateral(angleInDegrees);

        return calculateIntersectionPoint(center.getX(), center.getY(),
                anglePoint.getX(), anglePoint.getY(),
                firstLateralPoint.getX(), firstLateralPoint.getY(),
                lastLateralPoint.getX(), lastLateralPoint.getY());
    }

    private Point polarToCartesian(long x, long y, long radius, long angleInDegrees) {
        final double angleInRadians = (angleInDegrees - 90) * Math.PI / 180.0;

        return new Point(x + (radius * Math.cos(angleInRadians)),
                y + (radius * Math.sin(angleInRadians)));
    }


    private Point getStartSquareLateral(long angleInDegrees) {
        angleInDegrees = angleInDegrees % CIRCLE_DEGREES;
        if (angleInDegrees < FIRST_CORNER_DEGREES) {
            return new Point(getElementAttributes().getXCoordinate(),
                    getElementAttributes().getYCoordinate() - getElementAttributes().getHeight());
        } else if (angleInDegrees < SECOND_CORNER_DEGREES) {
            return new Point(getElementAttributes().getXCoordinate() + getElementAttributes().getWidth(),
                    getElementAttributes().getYCoordinate() - getElementAttributes().getHeight());
        } else if (angleInDegrees < THIRD_CORNER_DEGREES) {
            return new Point(getElementAttributes().getXCoordinate() + getElementAttributes().getWidth(),
                    getElementAttributes().getYCoordinate());
        } else {
            return new Point(getElementAttributes().getXCoordinate(),
                    getElementAttributes().getYCoordinate());
        }
    }

    private Point getEndSquareLateral(long angleInDegrees) {
        angleInDegrees = angleInDegrees % CIRCLE_DEGREES;
        if (angleInDegrees < FIRST_CORNER_DEGREES) {
            return new Point(getElementAttributes().getXCoordinate() + getElementAttributes().getWidth(),
                    getElementAttributes().getYCoordinate() - getElementAttributes().getHeight());
        } else if (angleInDegrees < SECOND_CORNER_DEGREES) {
            return new Point(getElementAttributes().getXCoordinate() + getElementAttributes().getWidth(),
                    getElementAttributes().getYCoordinate());
        } else if (angleInDegrees < THIRD_CORNER_DEGREES) {
            return new Point(getElementAttributes().getXCoordinate(),
                    getElementAttributes().getYCoordinate());
        } else {
            return new Point(getElementAttributes().getXCoordinate(),
                    getElementAttributes().getYCoordinate() - getElementAttributes().getHeight());
        }
    }


    //https://www.baeldung.com/java-intersection-of-two-lines
    private Point calculateIntersectionPoint(
            double cx1, double cy1,
            double cx2, double cy2,
            double rx1, double ry1,
            double rx2, double ry2) {

        //Get line from circle.
        final double ca = (cy2 - cy1) / (cx2 != cx1 ? cx2 - cx1 : 1);
        final double cb = cy1 - ca * cx1;

        //Get line from rectangle.
        final double ra = (ry2 - ry1) / (rx2 != rx1 ? rx2 - rx1 : 1);
        final double rb = ry1 - ra * rx1;

        if (ca == ra) {
            return null;
        }

        if (cx2 != cx1 && rx2 != rx1) {
            final double x = (rb - cb) / (ca - ra);
            final double y = ca * x + cb;

            return new Point(x, y);
        }
        if (cx2 == cx1 && rx2 == rx1) {
            //Parallel lines
            throw new InvalidParameterException(this.getClass(), "Lines does not intersect!");
        }
        if (cx2 == cx1) {
            return new Point(cx1, ra * cx1 + rb);
        }
        return new Point(rx1, ca * rx1 + cb);
    }


}
