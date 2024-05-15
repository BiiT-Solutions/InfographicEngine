package com.biit.infographic.core.models.svg.components.path;

import com.biit.infographic.core.models.svg.ElementType;
import com.biit.infographic.core.models.svg.serialization.BezierCurveJoinDeserializer;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@JsonDeserialize(using = BezierCurveJoinDeserializer.class)
@JsonRootName(value = "bezierCurve")
public class BezierCurveJoin extends PathElement {

    @JsonProperty("x")
    private Long x;

    @JsonProperty("y")
    private Long y;

    @JsonProperty("x2")
    private Long x2;

    @JsonProperty("y2")
    private Long y2;

    public BezierCurveJoin() {
        super();
        setElementType(ElementType.BEZIER_CURVE_JOIN);
    }

    public BezierCurveJoin(Number x, Number y) {
        this(x, y, false);
    }

    public BezierCurveJoin(Number x, Number y, boolean relativeCoordinates) {
        this(x != null ? x.longValue() : null, y != null ? y.longValue() : null, relativeCoordinates);
    }

    public BezierCurveJoin(Long x, Long y) {
        this(x, y, false);
    }

    public BezierCurveJoin(Long x, Long y, boolean relativeCoordinates) {
        this();
        this.x = x;
        this.y = y;
        setRelativeCoordinates(relativeCoordinates);
    }

    public BezierCurveJoin(Number x, Number y, Number x2, Number y2) {
        this(x, y, x2, y2, false);
    }

    public BezierCurveJoin(Number x, Number y, Number x2, Number y2, boolean relativeCoordinates) {
        this(x != null ? x.longValue() : null, y != null ? y.longValue() : null,
                x2 != null ? x2.longValue() : null, y2 != null ? y2.longValue() : null, relativeCoordinates);
    }

    public BezierCurveJoin(Long x, Long y, Long x2, Long y2) {
        this(x, y, x2, y2, false);
    }

    public BezierCurveJoin(Long x, Long y, Long x2, Long y2, boolean relativeCoordinates) {
        this();
        this.x = x;
        this.y = y;
        this.x2 = x2;
        this.y2 = y2;
        setRelativeCoordinates(relativeCoordinates);
    }

    @Override
    public Long getX() {
        return x;
    }

    public void setX(Long x) {
        this.x = x;
    }

    @Override
    public Long getY() {
        return y;
    }

    public void setY(Long y) {
        this.y = y;
    }

    public Long getX2() {
        return x2;
    }

    public void setX2(Long x2) {
        this.x2 = x2;
    }

    public Long getY2() {
        return y2;
    }

    public void setY2(Long y2) {
        this.y2 = y2;
    }

    @Override
    public String generateCoordinate(Long previousX, Long previousY) {
        return (isRelativeCoordinates() ? "s " : "S ") + getX2() + " " + getY2() + ", "
                + getX() + " " + getY() + " ";
    }
}
