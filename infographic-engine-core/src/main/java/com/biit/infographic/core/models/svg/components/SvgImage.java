package com.biit.infographic.core.models.svg.components;

import com.biit.infographic.core.models.svg.ElementAttributes;
import com.biit.infographic.core.models.svg.ElementType;
import com.biit.infographic.core.models.svg.SvgElement;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class SvgImage extends SvgElement {
    private static final String BASE_64_PREFIX = "data:image/png;base64,";

    private String name;
    private String content;
    private String href;

    private SvgImage() {
        super(new ElementAttributes("100%", "100%"));
        setElementType(ElementType.IMAGE);
    }

    public SvgImage(String name, String content) {
        this();
        setName(name);
        setContent(content);
    }

    public SvgImage(String name, String content, String href) {
        this();
        setName(name);
        setContent(content);
        setHref(href);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
        image.setAttribute("id", name);
        image.setAttributeNS(null, "x", String.valueOf(getElementAttributes().getXCoordinate()));
        image.setAttributeNS(null, "y", String.valueOf(getElementAttributes().getYCoordinate()));
        if (getElementAttributes().getWidth() != null) {
            image.setAttributeNS(null, "width", getElementAttributes().getWidth());
        }
        if (getElementAttributes().getHeight() != null) {
            image.setAttributeNS(null, "height", getElementAttributes().getHeight());
        }
        if (!content.startsWith(BASE_64_PREFIX)) {
            image.setAttribute("xlink:href", BASE_64_PREFIX + content);
        } else {
            image.setAttribute("xlink:href", content);
        }
        if (href != null) {
            image.setAttribute("onclick", "window.location='" + href + "'");
        }
        return image;
    }
}
