package com.biit.infographic.core.models.svg.components;

import com.biit.infographic.core.models.svg.ElementType;
import com.biit.infographic.core.models.svg.FillAttributes;
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

    @JsonProperty("fillAttributes")
    private FillAttributes fillAttributes;

    public SvgLink() {
        setElementType(ElementType.LINK);
        fillAttributes = new FillAttributes();
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

    public FillAttributes getFillAttributes() {
        return fillAttributes;
    }

    public void setFillAttributes(FillAttributes fillAttributes) {
        this.fillAttributes = fillAttributes;
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


    public Element embeddedStyle(Document doc) {
        //We only embed the first font choice.
        final Element style = doc.createElementNS(NAMESPACE, "style");
        style.setAttributeNS(null, "type", "text/css");
        style.setTextContent(embeddedStyleScript());
        return style;
    }


    private String embeddedStyleScript() {
        final StringBuilder script = new StringBuilder();
        script.append("\n\t\t.").append(getCssClass()).append(" {\n");
        script.append("\t\t}\n");
        if (getFillAttributes() != null && (getFillAttributes().getHoverFillColor() != null || getFillAttributes().getHoverFillOpacity() != null)) {
            script.append("\n\t\t.").append(getCssClass()).append(":hover").append(" {\n");
            if (getFillAttributes().getHoverFillColor() != null) {
                script.append("\t\t\t").append("fill: ").append(getFillAttributes().getHoverFillColor()).append("\n");
            }
            if (getFillAttributes().getHoverFillOpacity() != null) {
                script.append("\t\t\t").append("fill-opacity: ").append(getFillAttributes().getHoverFillOpacity()).append("\n");
            }
            script.append("\t\t}\n");
        }
        return script.toString();
    }
}
