package com.biit.infographic.core.models.svg;

import com.biit.infographic.core.models.svg.exceptions.InvalidAttributeException;
import com.biit.infographic.core.models.svg.serialization.SvgElementDeserializer;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import org.w3c.dom.Element;

@JsonDeserialize(using = SvgElementDeserializer.class)

@JsonRootName(value = "element")
@JsonPropertyOrder(alphabetic = true)
@JsonIgnoreProperties(ignoreUnknown = true)
public abstract class SvgElement implements ISvgElement {

    @JsonProperty("id")
    private String id;

    @JsonProperty("elementType")
    private ElementType elementType;

    @JsonIgnore
    private String cssClass;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
        this.cssClass = id;
    }


    public ElementType getElementType() {
        return elementType;
    }

    public void setElementType(ElementType elementType) {
        this.elementType = elementType;
    }

    public void elementAttributes(Element element) throws InvalidAttributeException {
        if (getId() != null) {
            element.setAttribute("id", getId());
        }
        if (getCssClass() != null) {
            element.setAttribute("class", getCssClass());
        }
    }

    public String getCssClass() {
        return cssClass;
    }

    public void setCssClass(String cssClass) {
        this.cssClass = cssClass;
    }

    //Each child must implement and filter invalid attributes.
    public abstract void validateAttributes() throws InvalidAttributeException;

    @Override
    public String toString() {
        if (getId() == null) {
            return super.toString();
        }
        return "SvgElement{"
                + "id='" + id + '\''
                + ", elementType=" + elementType
                + '}';
    }
}
