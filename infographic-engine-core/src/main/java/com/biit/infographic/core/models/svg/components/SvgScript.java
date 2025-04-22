package com.biit.infographic.core.models.svg.components;

import com.biit.infographic.core.models.svg.ElementAttributes;
import com.biit.infographic.core.models.svg.ElementType;
import com.biit.infographic.core.models.svg.SvgAreaElement;
import com.biit.infographic.core.models.svg.exceptions.InvalidAttributeException;
import com.biit.infographic.core.models.svg.serialization.SvgScriptDeserializer;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.util.Collection;
import java.util.Collections;

@JsonDeserialize(using = SvgScriptDeserializer.class)
@JsonRootName(value = "script")
public class SvgScript extends SvgAreaElement {

    @JsonProperty("script")
    private String script;

    public SvgScript(ElementAttributes elementAttributes) {
        super(elementAttributes);
        setElementType(ElementType.SCRIPT);
    }

    public SvgScript() {
        this("");
    }

    public SvgScript(String script) {
        this(new ElementAttributes());
        setScript(script);
    }

    public String getScript() {
        return script;
    }

    public void setScript(String script) {
        this.script = script;
    }

    @Override
    public Collection<Element> generateSvg(Document doc) {
        final Element elementScript = doc.createElementNS(NAMESPACE, "script");
        elementScript.setTextContent(getScript());
        elementAttributes(elementScript);
        return Collections.singletonList(elementScript);
    }

    @Override
    public void validateAttributes() throws InvalidAttributeException {
        super.validateAttributes();
        if (getElementAttributes().getHeight() != null) {
            throw new InvalidAttributeException(this.getClass(), "Image '" + getId() + "' does not have 'height' attribute");
        }
        if (getElementAttributes().getWidth() != null) {
            throw new InvalidAttributeException(this.getClass(), "Image '" + getId() + "' must have 'width' attribute");
        }
    }
}
