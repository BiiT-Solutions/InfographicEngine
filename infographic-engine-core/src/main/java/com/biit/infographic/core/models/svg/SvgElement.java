package com.biit.infographic.core.models.svg;

/*-
 * #%L
 * Infographic Engine v2 (Core)
 * %%
 * Copyright (C) 2022 - 2025 BiiT Sourcing Solutions S.L.
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * #L%
 */

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
        if (getId() != null && !getId().isEmpty()) {
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
