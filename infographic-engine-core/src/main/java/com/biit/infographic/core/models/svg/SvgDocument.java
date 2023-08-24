package com.biit.infographic.core.models.svg;

import com.biit.infographic.core.models.svg.exceptions.InvalidAttributeException;
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

    @JsonProperty("numColumns")
    private int columns;

    @JsonProperty("numRows")
    private int rows;

    @JsonProperty("elements")
    private List<SvgElement> elements;

    @JsonProperty("content")
    private List<SvgElement> content;

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

    public int getColumns() {
        return columns;
    }

    public void setColumns(int columns) {
        this.columns = columns;
    }

    public int getRows() {
        return rows;
    }

    public void setRows(int rows) {
        this.rows = rows;
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

    public List<SvgElement> getContent() {
        if (content == null) {
            return new ArrayList<>();
        }
        return content;
    }

    public void setContent(List<SvgElement> content) {
        this.content = content;
    }

    @Override
    public Element generateSvg(Document doc) {
        // Get the root element (the 'svg' element).
        final Element svgRoot = doc.getDocumentElement();
        svgRoot.setAttributeNS(null, "width", String.valueOf(width));
        svgRoot.setAttributeNS(null, "height", String.valueOf(height));

        setSvgBackground(doc, svgRoot);

        if (getElements() != null && !getElements().isEmpty()) {
            elements.forEach(svgElement -> {
                svgElement.validateAttributes();
                svgRoot.appendChild(svgElement.generateSvg(doc));
            });
        }

        return svgRoot;
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
