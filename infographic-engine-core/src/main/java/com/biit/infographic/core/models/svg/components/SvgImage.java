package com.biit.infographic.core.models.svg.components;

import com.biit.infographic.core.models.svg.ElementAttributes;
import com.biit.infographic.core.models.svg.ElementType;
import com.biit.infographic.core.models.svg.SvgElement;
import com.biit.infographic.core.models.svg.exceptions.InvalidAttributeException;
import com.fasterxml.jackson.annotation.JsonRootName;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

@JsonRootName(value = "image")
public class SvgImage extends SvgElement {
    private static final String BASE_64_PREFIX = "data:image/png;base64,";

    private String content;
    private String href;

    private SvgImage(ElementAttributes elementAttributes) {
        super(elementAttributes);
        setElementType(ElementType.IMAGE);
    }

    public SvgImage(ElementAttributes elementAttributes, String id, String content) {
        this(elementAttributes);
        setId(id);
        setContent(content);
    }

    public SvgImage(ElementAttributes elementAttributes, String id, String content, String href) {
        this(elementAttributes);
        setId(id);
        setContent(content);
        setHref(href);
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getHref() {
        return href;
    }

    public void setHref(String href) {
        this.href = href;
    }

    @Override
    public Element generateSvg(Document doc) {
        final Element image = doc.createElementNS(NAMESPACE, "image");
        image.setAttributeNS(null, "x", String.valueOf(getElementAttributes().getXCoordinate()));
        image.setAttributeNS(null, "y", String.valueOf(getElementAttributes().getYCoordinate()));
        if (!content.startsWith(BASE_64_PREFIX)) {
            image.setAttribute("xlink:href", BASE_64_PREFIX + content);
        } else {
            image.setAttribute("xlink:href", content);
        }
        if (href != null) {
            image.setAttribute("onclick", "window.location='" + href + "'");
        }
        elementAttributes(image);
        return image;
    }

    @Override
    public void validateAttributes() throws InvalidAttributeException {
        if (getElementAttributes().getHeight() == null) {
            throw new InvalidAttributeException(this.getClass(), "Image '" + getId() + "' must have 'height' attribute");
        }
        if (getElementAttributes().getWidth() == null) {
            throw new InvalidAttributeException(this.getClass(), "Image '" + getId() + "' must have 'width' attribute");
        }
    }
}
