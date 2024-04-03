package com.biit.infographic.core.models.svg.components;

import com.biit.infographic.core.files.ImageSearcher;
import com.biit.infographic.core.models.svg.ElementAttributes;
import com.biit.infographic.core.models.svg.ElementType;
import com.biit.infographic.core.models.svg.SvgAreaElement;
import com.biit.infographic.core.models.svg.exceptions.InvalidAttributeException;
import com.biit.infographic.core.models.svg.serialization.SvgImageDeserializer;
import com.biit.infographic.logger.InfographicEngineLogger;
import com.biit.infographic.logger.SvgGeneratorLogger;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.util.Base64;
import java.util.Collection;
import java.util.Collections;

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

    // Resource is already codified on base64 or is an image.
    @JsonProperty("resourceAlreadyInBase64")
    private boolean resourceAlreadyInBase64;

    public SvgImage() {
        this(new ElementAttributes());
    }

    private SvgImage(ElementAttributes elementAttributes) {
        super(elementAttributes);
        setElementType(ElementType.IMAGE);
        resourceAlreadyInBase64 = false;
    }

    public SvgImage(ElementAttributes elementAttributes, String resource) {
        this(elementAttributes);
        setId(resource);
        setResource(resource);
    }

    public SvgImage(ElementAttributes elementAttributes, String resource, boolean resourceAlreadyInBase64) {
        this(elementAttributes);
        setId(resource);
        setResource(resource);
        setResourceAlreadyInBase64(resourceAlreadyInBase64);
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

    public String codifyOnBase64(byte[] content) {
        // load file from /src/test/resources
        try {
            return Base64.getEncoder().encodeToString(content);
        } catch (NullPointerException e) {
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
    public void setFromPath(String resourcePath) {
        // load file from /src/test/resources
        this.content = getFromPath(resourcePath);
    }

    @JsonIgnore
    public String getFromPath(String resourcePath) {
        // load file from /src/test/resources
        if (resourceAlreadyInBase64) {
            return ImageSearcher.getFileAsString(resourcePath);
        }
        return codifyOnBase64(ImageSearcher.getFileAsBytes(resourcePath));
    }

    public String getResource() {
        return resource;
    }

    public void setResource(String resource) {
        this.resource = resource;
    }

    public boolean isResourceAlreadyInBase64() {
        return resourceAlreadyInBase64;
    }

    public void setResourceAlreadyInBase64(boolean resourceAlreadyInBase64) {
        this.resourceAlreadyInBase64 = resourceAlreadyInBase64;
    }

    public String getHref() {
        return href;
    }

    public void setHref(String href) {
        this.href = href;
    }

    @Override
    public Collection<Element> generateSvg(Document doc) {
        String finalContent = content;
        if (resource != null && content == null) {
            finalContent = getFromPath(resource);
        }
        if (finalContent == null || finalContent.isEmpty()) {
            InfographicEngineLogger.warning(this.getClass(), "Image with id '" + getId() + "' is empty!");
            return null;
        }

        final Element image = doc.createElementNS(NAMESPACE, "image");
        image.setAttributeNS(null, "x", String.valueOf(getElementAttributes().getXCoordinate()));
        image.setAttributeNS(null, "y", String.valueOf(getElementAttributes().getYCoordinate()));

        if (!finalContent.startsWith(BASE_64_PREFIX)) {
            image.setAttributeNS("http://www.w3.org/1999/xlink", "href", BASE_64_PREFIX + finalContent);
        } else {
            image.setAttributeNS("http://www.w3.org/1999/xlink", "href", finalContent);
        }

        if (href != null) {
            image.setAttribute("onclick", "window.location='" + href + "'");
        }
        elementAttributes(image);
        return Collections.singletonList(image);
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
