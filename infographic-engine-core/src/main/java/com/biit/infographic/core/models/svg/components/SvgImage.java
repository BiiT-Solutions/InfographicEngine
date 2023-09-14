package com.biit.infographic.core.models.svg.components;

import com.biit.infographic.core.models.svg.ElementAttributes;
import com.biit.infographic.core.models.svg.ElementType;
import com.biit.infographic.core.models.svg.SvgElement;
import com.biit.infographic.core.models.svg.exceptions.InvalidAttributeException;
import com.biit.infographic.core.models.svg.serialization.SvgImageDeserializer;
import com.biit.infographic.logger.SvgGeneratorLogger;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import org.apache.commons.io.FileUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.io.File;
import java.io.IOException;
import java.util.Base64;

@JsonDeserialize(using = SvgImageDeserializer.class)
@JsonRootName(value = "image")
public class SvgImage extends SvgElement {
    private static final String BASE_64_PREFIX = "data:image/png;base64,";

    @JsonProperty("content")
    private String content;

    @JsonProperty("href")
    private String href;

    public SvgImage() {
        this(new ElementAttributes());
    }

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

    @JsonIgnore
    public void setFromFile(File inputFile) {
        // load file from /src/test/resources
        try {
            final byte[] fileContent = FileUtils.readFileToByteArray(inputFile);
            setContent(Base64.getEncoder().encodeToString(fileContent));
        } catch (NullPointerException | IOException e) {
            SvgGeneratorLogger.errorMessage(this.getClass(), e);
        }

    }

    @JsonIgnore
    public void setFromResource(String resourcePath) {
        // load file from /src/test/resources
        setFromFile(new File(getClass().getClassLoader().getResource(resourcePath).getFile()));
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
            image.setAttributeNS("http://www.w3.org/1999/xlink", "href", BASE_64_PREFIX + content);
        } else {
            image.setAttributeNS("http://www.w3.org/1999/xlink", "href", content);
            //image.setAttributeNS("xlink:href", content);
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
        if (getElementAttributes().getFill() != null) {
            throw new InvalidAttributeException(this.getClass(), "Image '" + getId() + "' must not have 'fill' attribute");
        }
        if (getElementAttributes().getCssClass() != null) {
            throw new InvalidAttributeException(this.getClass(), "Image '" + getId() + "' must not have 'class' attribute");
        }
    }
}
