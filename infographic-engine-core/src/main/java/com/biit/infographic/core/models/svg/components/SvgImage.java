package com.biit.infographic.core.models.svg.components;

import com.biit.infographic.core.models.svg.ElementAttributes;
import com.biit.infographic.core.models.svg.ElementType;
import com.biit.infographic.core.models.svg.SvgAreaElement;
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
public class SvgImage extends SvgAreaElement {
    private static final String BASE_64_PREFIX = "data:image/png;base64,";

    // To define a content where the image will be included on the json template.
    @JsonProperty("content")
    private String content;

    @JsonProperty("href")
    private String href;

    // To define a resource path where the image will be read after the rules' execution.
    @JsonProperty("resource")
    private String resource;

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
    public String setFromFile(File inputFile) {
        // load file from /src/test/resources
        try {
            final byte[] fileContent = FileUtils.readFileToByteArray(inputFile);
            return Base64.getEncoder().encodeToString(fileContent);
        } catch (NullPointerException | IOException e) {
            SvgGeneratorLogger.errorMessage(this.getClass(), e);
        }
        return null;
    }

    /**
     * Generate a BASE 64 image from a resource. The result is encrusted on the template and cannot be changed by rules.
     *
     * @param resourcePath
     */
    @JsonIgnore
    public void setFromResource(String resourcePath) {
        // load file from /src/test/resources
        this.content = getFromResource(resourcePath);
    }

    @JsonIgnore
    public String getFromResource(String resourcePath) {
        // load file from /src/test/resources
        return setFromFile(new File(getClass().getClassLoader().getResource(resourcePath).getFile()));
    }

    public String getResource() {
        return resource;
    }

    public void setResource(String resource) {
        this.resource = resource;
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

        String finalContent = content;
        if (resource != null && content == null) {
            finalContent = getFromResource(resource);
        }
        if (!finalContent.startsWith(BASE_64_PREFIX)) {
            image.setAttributeNS("http://www.w3.org/1999/xlink", "href", BASE_64_PREFIX + finalContent);
        } else {
            image.setAttributeNS("http://www.w3.org/1999/xlink", "href", finalContent);
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
        super.validateAttributes();
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
