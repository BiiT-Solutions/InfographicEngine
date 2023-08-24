package com.biit.infographic.core.models.svg;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.util.ArrayList;
import java.util.List;

@JsonRootName(value = "report")
public class SvgDocument implements ISvgElement {
    public static final long DEFAULT_WIDTH = 256L;
    public static final long DEFAULT_HEIGHT = 256L;

    @JsonProperty("width")
    private long width = DEFAULT_WIDTH;

    @JsonProperty("height")
    private long height = DEFAULT_HEIGHT;

    @JsonProperty("background")
    private SvgBackground svgBackground;

    @JsonProperty("type")
    private LayoutType layoutType;

    @JsonProperty("elements")
    private List<SvgElement> elements;

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
        svgRoot.setAttributeNS(null, "width", String.valueOf(width));
        svgRoot.setAttributeNS(null, "height", String.valueOf(height));

        setSvgBackground(doc, svgRoot);
        setViewBox(svgRoot);

        if (getElements() != null && !getElements().isEmpty()) {
            elements.forEach(svgElement -> {
                svgElement.validateAttributes();
                svgRoot.appendChild(svgElement.generateSvg(doc));
            });
        }

        return svgRoot;
    }

    private void setViewBox(Element svgRoot) {
        long height = this.height;
        long width = this.width;

        if (elements != null) {
            for (SvgElement element : elements) {
                if (element.getElementAttributes().getHeight() != null && element.getElementAttributes().getHeightUnit() == Unit.PIXELS) {
                    height = Math.max(height, element.getElementAttributes().getXCoordinate() + element.getElementAttributes().getHeight());
                }
                if (element.getElementAttributes().getWidth() != null && element.getElementAttributes().getWidthUnit() == Unit.PIXELS) {
                    width = Math.max(width, element.getElementAttributes().getYCoordinate() + element.getElementAttributes().getWidth());
                }
            }
        }

        svgRoot.setAttributeNS(null, "viewBox", "0 0 " + width + " " + height);
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
