package com.biit.infographic.core.models.svg.clip;

import com.biit.infographic.core.models.svg.ElementAttributes;
import com.biit.infographic.core.models.svg.SvgAreaElement;
import com.biit.infographic.core.models.svg.SvgElement;
import com.biit.infographic.core.models.svg.exceptions.InvalidAttributeException;
import com.biit.infographic.core.models.svg.serialization.SvgClipPathDeserializer;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.util.ArrayList;
import java.util.Collection;

@JsonDeserialize(using = SvgClipPathDeserializer.class)
@JsonRootName(value = "clipPath")
public abstract class SvgClipPath extends SvgElement {
    public static final String ID_PREFIX = "clipPath";

    @JsonIgnore
    private ElementAttributes elementAttributes;

    public ElementAttributes getElementAttributes() {
        return elementAttributes;
    }

    public void setElementAttributes(ElementAttributes elementAttributes) {
        this.elementAttributes = elementAttributes;
    }

    protected abstract SvgAreaElement generateArea();

    @Override
    public Collection<Element> generateSvg(Document doc) {
        validateAttributes();
        final ArrayList<Element> elements = new ArrayList<>();
        final Element clipPath = doc.createElementNS(NAMESPACE, "clipPath");
        clipPath.setAttributeNS(null, "id", getId());
        elements.add(clipPath);
        //Add layout.
        clipPath.appendChild(generateArea().generateSvg(doc).iterator().next());
        return elements;
    }


    @Override
    public void validateAttributes() throws InvalidAttributeException {

    }
}
