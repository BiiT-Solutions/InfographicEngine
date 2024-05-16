package com.biit.infographic.core.models.svg.components;

import com.biit.infographic.core.models.svg.ElementType;
import com.biit.infographic.core.models.svg.SvgElement;
import com.biit.infographic.core.models.svg.exceptions.InvalidAttributeException;
import com.biit.infographic.core.models.svg.serialization.SvgLinkDeserializer;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.util.Collection;
import java.util.List;

@JsonRootName(value = "link")
@JsonDeserialize(using = SvgLinkDeserializer.class)
public class SvgLink extends SvgElement {

    @JsonProperty("href")
    private String href;

    public SvgLink() {
        setElementType(ElementType.LINK);
    }

    public SvgLink(String href) {
        this();
        this.href = href;
    }

    public String getHref() {
        return href;
    }

    public void setHref(String href) {
        this.href = href;
    }

    @Override
    public Collection<Element> generateSvg(Document doc) {
        return List.of();
    }

    @Override
    public void validateAttributes() throws InvalidAttributeException {
        if (href == null) {
            throw new InvalidAttributeException(this.getClass(), "Link '" + getId() + "' must have 'href' attribute");
        }
    }
}
