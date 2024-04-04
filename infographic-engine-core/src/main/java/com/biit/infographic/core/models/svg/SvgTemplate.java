package com.biit.infographic.core.models.svg;

import com.biit.infographic.core.models.svg.components.SvgCircle;
import com.biit.infographic.core.models.svg.components.SvgEllipse;
import com.biit.infographic.core.models.svg.components.SvgLine;
import com.biit.infographic.core.models.svg.components.gradient.SvgGradient;
import com.biit.infographic.core.models.svg.components.text.FontWeight;
import com.biit.infographic.core.models.svg.components.text.SvgText;
import com.biit.infographic.core.models.svg.exceptions.InvalidAttributeException;
import com.biit.infographic.core.models.svg.serialization.ObjectMapperFactory;
import com.biit.infographic.core.models.svg.serialization.SvgTemplateDeserializer;
import com.biit.infographic.logger.InfographicEngineLogger;
import com.biit.infographic.logger.SvgGeneratorLogger;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;

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

    @JsonProperty("embedFonts")
    private Boolean embedFonts;

    public SvgTemplate(ElementAttributes elementAttributes) {
        super(elementAttributes);
        setElementType(ElementType.SVG);
    }

    public SvgTemplate() {
        this(new ElementAttributes());
    }

    public SvgTemplate(Number width, Number height) {
        this(width != null ? width.longValue() : null, height != null ? height.longValue() : null);
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

    public boolean isEmbedFonts() {
        return embedFonts != null ? embedFonts : true;
    }

    public void setEmbedFonts(Boolean embedFonts) {
        this.embedFonts = embedFonts;
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
        if (element.getId() == null) {
            element.setId(element.getElementType().name().toLowerCase() + elements.size());
        }
        elements.add(element);
    }

    public void addElements(List<SvgAreaElement> elements) {
        elements.forEach(this::addElement);
    }

    public SvgElement getElement(String id) {
        if (elements != null) {
            for (SvgElement element : elements) {
                if (Objects.equals(id, element.getId())) {
                    return element;
                }
            }
        }
        return null;
    }

    @Override
    public Collection<Element> generateSvg(Document doc) {
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
                final Collection<Element> elements = svgElement.generateSvg(doc);
                if (elements != null && !elements.isEmpty()) {
                    elements.forEach(svgRoot::appendChild);
                } else {
                    InfographicEngineLogger.warning(this.getClass(), "Element '"
                            + svgElement.getId() + "' cannot be converted to SVG.");
                }
            });
        }

        return Collections.singletonList(svgRoot);
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
                    final Long maxLineLength = ((SvgText) element).getMaxLineLength();
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
            final HashMap<String, HashSet<FontWeight>> embeddedFonts = new HashMap<>();
            for (SvgAreaElement element : elements) {
                if (element.getElementAttributes() != null && element.getElementAttributes().getGradient() != null) {
                    final SvgGradient gradient = element.getElementAttributes().getGradient();
                    gradient.setId(SvgGradient.ID_PREFIX + "_" + element.getElementType().name().toLowerCase() + "_" + ++idCounter);
                    if (element instanceof SvgLine) {
                        gradient.setX1Coordinate(element.getElementAttributes().getXCoordinate());
                        gradient.setY1Coordinate(element.getElementAttributes().getYCoordinate());
                        gradient.setX2Coordinate(((SvgLine) element).getX2Coordinate());
                        gradient.setY2Coordinate(((SvgLine) element).getY2Coordinate());
                    }
                    final Collection<Element> elements = gradient.generateSvg(doc);
                    elements.forEach(defs::appendChild);
                }
                if (element instanceof SvgText && isEmbedFonts()) {
                    if (((SvgText) element).mustEmbedFont()
                            && (embeddedFonts.get(((SvgText) element).getMainFontFamily()) == null
                            || !embeddedFonts.get(((SvgText) element).getMainFontFamily()).contains(((SvgText) element).getFontWeight()))) {
                        final Element fontScript = ((SvgText) element).embeddedFont(doc);
                        if (fontScript != null) {
                            defs.appendChild(fontScript);
                            embeddedFonts.computeIfAbsent(((SvgText) element).getMainFontFamily(), k -> new HashSet<>());
                            embeddedFonts.get(((SvgText) element).getMainFontFamily()).add(((SvgText) element).getFontWeight());
                            SvgGeneratorLogger.info(this.getClass(), "Font '{}' embedded!", ((SvgText) element).getFontFamily());
                            idCounter++;
                        }
                    }
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
            final Collection<Element> background = svgBackground.generateSvg(doc);
            if (background != null && !background.isEmpty()) {
                background.forEach(svgRoot::appendChild);
            }
        }
    }

    public String toJson() throws JsonProcessingException {
        return ObjectMapperFactory.getObjectMapper().writeValueAsString(this);
    }

    public static SvgTemplate fromJson(String json) throws JsonProcessingException {
        return ObjectMapperFactory.getObjectMapper().readValue(json, SvgTemplate.class);
    }
}
