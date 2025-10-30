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


import com.biit.infographic.core.models.svg.components.SvgImage;
import com.biit.infographic.core.models.svg.components.SvgRectangle;
import com.biit.infographic.core.models.svg.exceptions.InvalidAttributeException;
import com.biit.infographic.core.models.svg.serialization.SvgBackgroundDeserializer;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.util.ArrayList;
import java.util.Collection;

@JsonDeserialize(using = SvgBackgroundDeserializer.class)
@JsonRootName(value = "background")
@JsonPropertyOrder(alphabetic = true)
@JsonIgnoreProperties(ignoreUnknown = true)
public class SvgBackground implements ISvgElement {

    @JsonProperty("backgroundColor")
    private String backgroundColor = null;

    @JsonProperty("xRadius")
    private Long xRadius = 0L;

    @JsonProperty("yRadius")
    private Long yRadius = 0L;

    //As Base64.
    @JsonProperty("image")
    private String image = null;

    public SvgBackground() {
        super();
    }

    public SvgBackground(String backgroundColor) {
        this();
        setBackgroundColor(backgroundColor);
    }

    public String getBackgroundColor() {
        return backgroundColor;
    }

    public void setBackgroundColor(String backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    public SvgBackground backgroundColor(String backgroundColor) {
        setBackgroundColor(backgroundColor);
        return this;
    }

    public Long getXRadius() {
        return xRadius;
    }

    public void setXRadius(Number xRadius) {
        this.xRadius = xRadius != null ? xRadius.longValue() : null;
    }

    public void setXRadius(Long xRadius) {
        this.xRadius = xRadius;
    }

    public Long getYRadius() {
        return yRadius;
    }

    public void setYRadius(Number yRadius) {
        this.yRadius = yRadius != null ? yRadius.longValue() : null;
    }

    public void setYRadius(Long yRadius) {
        this.yRadius = yRadius;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public SvgBackground image(String image) {
        setImage(image);
        return this;
    }

    @Override
    public Collection<Element> generateSvg(Document doc) {
        validateAttributes();
        if (getBackgroundColor() != null) {
            final SvgRectangle rectangle = new SvgRectangle("100%", "100%", backgroundColor);
            rectangle.setXRadius(xRadius);
            rectangle.setYRadius(yRadius);
            return rectangle.generateSvg(doc);
        } else if (getImage() != null) {
            final SvgImage svgImage = new SvgImage(new ElementAttributes("100%", "100%"), "background", getImage());
            return svgImage.generateSvg(doc);
        }
        return new ArrayList<>();
    }

    @Override
    public void validateAttributes() throws InvalidAttributeException {
        //Nothing.
    }
}
