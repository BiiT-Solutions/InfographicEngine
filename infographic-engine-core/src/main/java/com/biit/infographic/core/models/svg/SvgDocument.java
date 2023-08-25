package com.biit.infographic.core.models.svg;

import com.biit.infographic.core.models.svg.components.SvgCircle;
import com.biit.infographic.core.models.svg.components.SvgEllipse;
import com.biit.infographic.core.models.svg.components.SvgLine;
import com.biit.infographic.core.models.svg.components.text.SvgText;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.util.ArrayList;
import java.util.List;

/**
 * Root class for generating an SVG document.
 * <p>
 * If height and width are not set, will be calculated to fit the content.
 */
@JsonRootName(value = "report")
public class SvgDocument implements ISvgElement {
    public static final long DEFAULT_WIDTH = 256L;
    public static final long DEFAULT_HEIGHT = 256L;

    @JsonProperty("width")
    private Long width;

    @JsonProperty("height")
    private Long height;

    @JsonProperty("background")
    private SvgBackground svgBackground;

    @JsonProperty("type")
    private LayoutType layoutType;

    @JsonProperty("elements")
    private List<SvgElement> elements;

    public SvgDocument() {
        super();
    }

    public SvgDocument(Long width, Long height) {
        this();
        setWidth(width);
        setHeight(height);
    }

    public long getWidth() {
        return width;
    }

    public void setWidth(long width) {
        this.width = width;
    }

    public long getHeight() {
        return height;
    }

    public void setHeight(long height) {
        this.height = height;
    }

    public SvgBackground getSvgBackground() {
        return svgBackground;
    }

    public void setSvgBackground(SvgBackground svgBackground) {
        this.svgBackground = svgBackground;
    }

    public LayoutType getLayoutType() {
        return layoutType;
    }

    public void setLayoutType(LayoutType layoutType) {
        this.layoutType = layoutType;
    }

    public List<SvgElement> getElements() {
        if (elements == null) {
            return new ArrayList<>();
        }
        return elements;
    }

    public void setElements(List<SvgElement> elements) {
        this.elements = elements;
    }

    public void addElement(SvgElement element) {
        if (elements == null) {
            elements = new ArrayList<>();
        }
        elements.add(element);
    }

    @Override
    public Element generateSvg(Document doc) {
        // Get the root element (the 'svg' element).
        final Element svgRoot = doc.getDocumentElement();

        setViewBox(svgRoot);

        svgRoot.setAttributeNS(null, "width", String.valueOf(width != 0 ? width : DEFAULT_WIDTH));
        svgRoot.setAttributeNS(null, "height", String.valueOf(height != 0 ? height : DEFAULT_HEIGHT));

        setSvgBackground(doc, svgRoot);

        if (getElements() != null && !getElements().isEmpty()) {
            elements.forEach(svgElement -> {
                svgElement.validateAttributes();
                svgRoot.appendChild(svgElement.generateSvg(doc));
            });
        }

        return svgRoot;
    }

    private void setViewBox(Element svgRoot) {
        long height = this.height != null ? this.height : 0L;
        long width = this.width != null ? this.width : 0L;
        long x = 0;
        long y = 0;

        if (elements != null) {
            for (SvgElement element : elements) {
                if (element.getElementAttributes().getHeight() != null && element.getElementAttributes().getHeightUnit() == Unit.PIXELS) {
                    height = Math.max(height, element.getElementAttributes().getXCoordinate() + element.getElementAttributes().getHeight());
                }
                if (element.getElementAttributes().getWidth() != null && element.getElementAttributes().getWidthUnit() == Unit.PIXELS) {
                    width = Math.max(width, element.getElementAttributes().getYCoordinate() + element.getElementAttributes().getWidth());
                }
                x = Math.min(x, element.getElementAttributes().getXCoordinate());
                y = Math.min(y, element.getElementAttributes().getYCoordinate());
                if (element instanceof SvgCircle) {
                    height = Math.max(height, ((SvgCircle) element).getRadius() + element.getElementAttributes().getYCoordinate());
                    width = Math.max(width, ((SvgCircle) element).getRadius() + element.getElementAttributes().getXCoordinate());
                    x = Math.min(x, element.getElementAttributes().getXCoordinate() - ((SvgCircle) element).getRadius());
                    y = Math.min(y, element.getElementAttributes().getYCoordinate() - ((SvgCircle) element).getRadius());
                }
                if (element instanceof SvgEllipse) {
                    height = Math.max(height, ((SvgEllipse) element).getYRadius() + element.getElementAttributes().getYCoordinate());
                    width = Math.max(width, ((SvgEllipse) element).getXRadius() + element.getElementAttributes().getXCoordinate());
                    x = Math.min(x, element.getElementAttributes().getXCoordinate() - ((SvgEllipse) element).getYRadius());
                    y = Math.min(y, element.getElementAttributes().getYCoordinate() - ((SvgEllipse) element).getXRadius());
                }
                if (element instanceof SvgLine) {
                    height = Math.max(height, element.getElementAttributes().getYCoordinate());
                    width = Math.max(width, element.getElementAttributes().getXCoordinate());
                    height = Math.max(height, ((SvgLine) element).getY2Coordinate());
                    width = Math.max(width, ((SvgLine) element).getX2Coordinate());
                }
                if (element instanceof SvgText) {
                    height = Math.max(height, element.getElementAttributes().getYCoordinate() + ((SvgText) element).getFontSize());
                    final Integer maxLineLength = ((SvgText) element).getMaxLineLength();
                    width = Math.max(width, element.getElementAttributes().getXCoordinate() + (maxLineLength != null ? maxLineLength : 0));
                }
            }
        }

        //Updated width and height if are not defined.
        if (this.width == null) {
            this.width = Math.abs(x) + width;
        }

        if (this.height == null) {
            this.height = Math.abs(y) + height;
        }

        svgRoot.setAttributeNS(null, "viewBox", x + " " + y + " " + (width + Math.abs(x)) + " " + (height + Math.abs(y)));
    }

    private void setSvgBackground(Document doc, Element svgRoot) {
        if (svgBackground != null) {
            final Element background = svgBackground.generateSvg(doc);
            if (background != null) {
                svgRoot.appendChild(background);
            }
        }
    }
}
