package com.biit.infographic.core.models.svg.components;

import com.biit.infographic.core.exceptions.InvalidParameterException;
import com.biit.infographic.core.models.svg.ElementAttributes;
import com.biit.infographic.core.models.svg.ElementType;
import com.biit.infographic.core.models.svg.StrokeAlign;
import com.biit.infographic.core.models.svg.SvgAreaElement;
import com.biit.infographic.core.models.svg.components.path.Point;
import com.biit.infographic.core.models.svg.serialization.SvgRectangleSectorDeserializer;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.util.ArrayList;
import java.util.Collection;

@JsonDeserialize(using = SvgRectangleSectorDeserializer.class)
@JsonRootName(value = "rectangleSector")
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

    @JsonProperty("percentage")
    private String percentage;

    public SvgRectangleSector(ElementAttributes elementAttributes) {
        super(elementAttributes);
        setElementType(ElementType.RECTANGLE_SECTOR);
    }

    public SvgRectangleSector() {
        this(new ElementAttributes());
    }

    public SvgRectangleSector(Number xCoordinate, Number yCoordinate, Number width, Number height) {
        this(xCoordinate != null ? xCoordinate.longValue() : 0, yCoordinate != null ? yCoordinate.longValue() : 0,
                width, height, 0, 0);
    }

    public SvgRectangleSector(Number xCoordinate, Number yCoordinate, Number width, Number height, Number percentage) {
        this(xCoordinate != null ? xCoordinate.longValue() : 0, yCoordinate != null ? yCoordinate.longValue() : 0,
                width, height, 0, null);
        if (percentage.doubleValue() < 0 || percentage.doubleValue() > 1) {
            throw new IllegalArgumentException("percentage must be between 0 and 1");
        }
        setPercentage(percentage);
    }

    public SvgRectangleSector(Number xCoordinate, Number yCoordinate, Number width, Number height, Number startAngle, Number endAngle) {
        this(xCoordinate != null ? xCoordinate.longValue() : 0, yCoordinate != null ? yCoordinate.longValue() : 0,
                width.longValue(), height.longValue(),
                startAngle != null ? startAngle.longValue() : 0,
                endAngle != null ? endAngle.longValue() : 0);
    }

    public SvgRectangleSector(Long xCoordinate, Long yCoordinate, Long width, Long height, Long startAngle, Long endAngle) {
        this(new ElementAttributes(xCoordinate, yCoordinate, String.valueOf(width), String.valueOf(height), null));
        setStartAngle(startAngle);
        setEndAngle(endAngle);
        getElementStroke().setStrokeWidth(1);
    }

    public Long getStartAngle() {
        if (startAngle != null) {
            return startAngle;
        }
        return 0L;
    }

    public void setStartAngle(Long startAngle) {
        this.startAngle = startAngle;
    }

    public Long getEndAngle() {
        return endAngle;
    }

    @JsonIgnore
    public Long getCalculatedEndAngle() {
        if (endAngle != null && (endAngle != 0 || percentage == null)) {
            return endAngle;
        }
        return (long) (CIRCLE_DEGREES * Double.parseDouble(percentage.isBlank() ? "0" : percentage));
    }

    public void setEndAngle(Long endAngle) {
        this.endAngle = endAngle;
    }

    /**
     * @param percentage
     */
    public void setPercentage(String percentage) {
        this.percentage = percentage;
    }

    /**
     * @param percentage percentage filled up [0, 1]
     */
    public void setPercentage(Number percentage) {
        if (percentage == null || percentage.doubleValue() < 0 || percentage.doubleValue() > 1) {
            throw new IllegalArgumentException("percentage must be between 0 and 1");
        }
        this.percentage = String.valueOf(percentage);
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

    /**
     * Stroke is included on the X and must be subtracted.
     *
     * @return the calculated coordinate
     */
    protected Double generateRealXCoordinate() {
        return (getElementAttributes().getXCoordinate()
                + (getElementStroke().getStrokeAlign() == StrokeAlign.OUTSET ? getElementStroke().getStrokeWidth() : getElementStroke().getStrokeWidth() / 2));
    }

    /**
     * Stroke is included on the y and must be subtracted.
     *
     * @return the calculated coordinate
     */
    protected Double generateRealYCoordinate() {
        return (getElementAttributes().getYCoordinate() + getElementAttributes().getHeight()
                + (getElementStroke().getStrokeAlign() == StrokeAlign.OUTSET ? getElementStroke().getStrokeWidth() : getElementStroke().getStrokeWidth() / 2));
    }


    private String createSection() {
        final StringBuilder path = new StringBuilder();

        //Start at the middle.
        final Point center = new Point(generateRealXCoordinate() + (getElementAttributes().getWidth() / 2),
                generateRealYCoordinate() - (getElementAttributes().getHeight() / 2));
        path.append(center.getX()).append(" ").append(center.getY()).append(" ");

        //Go to the first intersection of the angle with the square.
        final Point angleZeroIntersection = getIntesectionPoint(getStartAngle());
        path.append(angleZeroIntersection.getX()).append(",").append(angleZeroIntersection.getY()).append(" ");
        //Add needed square corners;
        if (getStartAngle() < FIRST_CORNER_DEGREES && getCalculatedEndAngle() > FIRST_CORNER_DEGREES) {
            path.append(generateRealXCoordinate() + getElementAttributes().getWidth()).append(",")
                    .append(generateRealYCoordinate() - getElementAttributes().getHeight()).append(" ");
        }

        if (getStartAngle() < SECOND_CORNER_DEGREES && getCalculatedEndAngle() > SECOND_CORNER_DEGREES) {
            path.append(generateRealXCoordinate() + getElementAttributes().getWidth()).append(",")
                    .append(generateRealYCoordinate()).append(" ");
        }

        if (getStartAngle() < THIRD_CORNER_DEGREES && getCalculatedEndAngle() > THIRD_CORNER_DEGREES) {
            path.append(generateRealXCoordinate()).append(",")
                    .append(generateRealYCoordinate()).append(" ");
        }

        if (getStartAngle() < FORTH_CORNER_DEGREES && getCalculatedEndAngle() > FORTH_CORNER_DEGREES) {
            path.append(generateRealXCoordinate()).append(",")
                    .append(generateRealYCoordinate() - getElementAttributes().getHeight()).append(" ");
        }

        final Point angleIntersection = getIntesectionPoint(getCalculatedEndAngle());
        path.append(angleIntersection.getX()).append(",").append(angleIntersection.getY()).append(" Z");

        return path.toString();
    }


    private Point getIntesectionPoint(long angleInDegrees) {
        final Point center = new Point(generateRealXCoordinate() + (double) getElementAttributes().getWidth() / 2,
                generateRealYCoordinate() - (double) getElementAttributes().getHeight() / 2);
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
            return new Point(generateRealXCoordinate(),
                    generateRealYCoordinate() - getElementAttributes().getHeight());
        } else if (angleInDegrees < SECOND_CORNER_DEGREES) {
            return new Point(generateRealXCoordinate() + getElementAttributes().getWidth(),
                    generateRealYCoordinate() - getElementAttributes().getHeight());
        } else if (angleInDegrees < THIRD_CORNER_DEGREES) {
            return new Point(generateRealXCoordinate() + getElementAttributes().getWidth(),
                    generateRealYCoordinate());
        } else if (angleInDegrees < FORTH_CORNER_DEGREES) {
            return new Point(generateRealXCoordinate(),
                    generateRealYCoordinate());
        }
        return new Point(generateRealXCoordinate(),
                generateRealYCoordinate() - getElementAttributes().getHeight());
    }

    private Point getEndSquareLateral(long angleInDegrees) {
        angleInDegrees = angleInDegrees % CIRCLE_DEGREES;
        if (angleInDegrees < FIRST_CORNER_DEGREES) {
            return new Point(generateRealXCoordinate() + getElementAttributes().getWidth(),
                    generateRealYCoordinate() - getElementAttributes().getHeight());
        } else if (angleInDegrees < SECOND_CORNER_DEGREES) {
            return new Point(generateRealXCoordinate() + getElementAttributes().getWidth(),
                    generateRealYCoordinate());
        } else if (angleInDegrees < THIRD_CORNER_DEGREES) {
            return new Point(generateRealXCoordinate(),
                    generateRealYCoordinate());
        } else if (angleInDegrees < FORTH_CORNER_DEGREES) {
            return new Point(generateRealXCoordinate(),
                    generateRealYCoordinate() - getElementAttributes().getHeight());
        }
        return new Point(generateRealXCoordinate() + getElementAttributes().getWidth(),
                generateRealYCoordinate() - getElementAttributes().getHeight());
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
