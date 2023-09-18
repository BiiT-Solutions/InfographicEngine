package com.biit.infographic.core.models.svg;

import com.biit.infographic.core.models.svg.components.SvgCircle;
import com.biit.infographic.core.models.svg.components.SvgEllipse;
import com.biit.infographic.core.models.svg.components.SvgLine;
import com.biit.infographic.core.models.svg.components.gradient.SvgGradient;
import com.biit.infographic.core.models.svg.components.text.SvgText;
import com.biit.infographic.core.models.svg.exceptions.InvalidAttributeException;
import com.biit.infographic.core.models.svg.serialization.SvgTemplateDeserializer;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.util.ArrayList;
import java.util.List;

/**
 * Root class for generating an SVG document.
 * <p>
 * If height and width are not set, will be calculated to fit the content.
 */
@JsonDeserialize(using = SvgTemplateDeserializer.class)
@JsonInclude(JsonInclude.Include.NON_ABSENT)
public class SvgTemplate extends SvgAreaElement {
    public static final long DEFAULT_WIDTH = 256L;
    public static final long DEFAULT_HEIGHT = 256L;

    @JsonProperty("background")
    private SvgBackground svgBackground;

    @JsonProperty("type")
    private LayoutType layoutType;

    @JsonProperty("elements")
    private List<SvgAreaElement> elements;

    public SvgTemplate(ElementAttributes elementAttributes) {
        super(elementAttributes);
        setElementType(ElementType.SVG);
    }

    public SvgTemplate() {
        this(new ElementAttributes());
    }

    public SvgTemplate(Long width, Long height) {
        this(new ElementAttributes(String.valueOf(width), String.valueOf(height), null));
        getElementAttributes().setWidthUnit(Unit.PIXELS);
        getElementAttributes().setHeightUnit(Unit.PIXELS);
    }

    @Override
    public void validateAttributes() throws InvalidAttributeException {
        super.validateAttributes();
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

    public List<SvgAreaElement> getElements() {
        if (elements == null) {
            return new ArrayList<>();
        }
        return elements;
    }

    public void setElements(List<SvgAreaElement> elements) {
        this.elements = elements;
    }

    public void addElement(SvgAreaElement element) {
        if (elements == null) {
            elements = new ArrayList<>();
        }
        if (element.getElementType() == ElementType.SVG) {
            element.setElementType(ElementType.NESTED_SVG);
        }
        elements.add(element);
    }

    public void addElements(List<SvgAreaElement> elements) {
        elements.forEach(this::addElement);
    }

    @Override
    public Element generateSvg(Document doc) {
        // Get the root element (the 'svg' element).
        final Element svgRoot;
        if (getElementType() == ElementType.NESTED_SVG) {
            //Nested SVGs does not have document definition and XML headers.
            svgRoot = doc.createElementNS(NAMESPACE, "svg");
        } else {
            //Set default SVG information.
            svgRoot = doc.getDocumentElement();
            defineViewBox(svgRoot);
        }

        svgRoot.setAttributeNS(null, "width", String.valueOf(getElementAttributes().getWidth() != null
                && getElementAttributes().getWidth() != 0 ? getElementAttributes().getWidth() : DEFAULT_WIDTH));
        svgRoot.setAttributeNS(null, "height", String.valueOf(getElementAttributes().getHeight() != null
                && getElementAttributes().getHeight() != 0 ? getElementAttributes().getHeight() : DEFAULT_HEIGHT));

        generateDefs(doc, svgRoot);

        setSvgBackground(doc, svgRoot);

        if (getElements() != null && !getElements().isEmpty()) {
            elements.forEach(svgElement -> {
                svgElement.validateAttributes();
                svgRoot.appendChild(svgElement.generateSvg(doc));
            });
        }

        return svgRoot;
    }

    private void defineViewBox(Element svgRoot) {
        long height = getElementAttributes().getHeight() != null ? getElementAttributes().getHeight() : 0L;
        long width = getElementAttributes().getWidth() != null ? getElementAttributes().getWidth() : 0L;
        long x = 0;
        long y = 0;

        if (elements != null) {
            for (SvgAreaElement element : elements) {
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
//                    x = Math.min(x, element.getElementAttributes().getXCoordinate() - ((SvgCircle) element).getRadius());
//                    y = Math.min(y, element.getElementAttributes().getYCoordinate() - ((SvgCircle) element).getRadius());
                }
                if (element instanceof SvgEllipse) {
                    height = Math.max(height, element.getElementAttributes().getHeight() + element.getElementAttributes().getYCoordinate());
                    width = Math.max(width, element.getElementAttributes().getWidth() + element.getElementAttributes().getXCoordinate());
//                    x = Math.min(x, element.getElementAttributes().getXCoordinate() - element.getElementAttributes().getWidth());
//                    y = Math.min(y, element.getElementAttributes().getYCoordinate() - element.getElementAttributes().getHeight());
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
        if (getElementAttributes().getWidth() == null) {
            getElementAttributes().setWidth(Math.abs(x) + width);
        }

        if (getElementAttributes().getHeight() == null) {
            getElementAttributes().setHeight(Math.abs(y) + height);
        }

        svgRoot.setAttributeNS(null, "viewBox", x + " " + y + " " + getElementAttributes().getWidth()
                + " " + getElementAttributes().getHeight());
    }

    /**
     * Defs must include all gradient definitions.
     *
     * @param doc the Document DOM.
     */
    private void generateDefs(Document doc, Element svgRoot) {
        final Element defs = doc.createElementNS(NAMESPACE, "defs");
        if (elements != null && !elements.isEmpty()) {
            int idCounter = 0;
            for (SvgAreaElement element : elements) {
                if (element.getElementAttributes() != null && element.getElementAttributes().getGradient() != null) {
                    final SvgGradient gradient = element.getElementAttributes().getGradient();
                    gradient.setId(SvgGradient.ID_PREFIX + ++idCounter);
                    defs.appendChild(gradient.generateSvg(doc));
                }
            }

            if (idCounter > 0) {
                svgRoot.appendChild(defs);
            }
        }
    }

    @JsonIgnore
    private void setSvgBackground(Document doc, Element svgRoot) {
        if (svgBackground != null) {
            final Element background = svgBackground.generateSvg(doc);
            if (background != null) {
                svgRoot.appendChild(background);
            }
        }
    }
}
