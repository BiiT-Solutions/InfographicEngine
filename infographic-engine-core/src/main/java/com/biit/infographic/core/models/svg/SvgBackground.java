package com.biit.infographic.core.models.svg;


import com.biit.infographic.core.models.svg.components.SvgRectangle;
import com.biit.infographic.core.models.svg.components.SvgImage;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonRootName;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

@JsonRootName(value = "background")
@JsonPropertyOrder(alphabetic = true)
@JsonIgnoreProperties(ignoreUnknown = true)
public class SvgBackground implements ISvgElement {
    @JsonProperty("backgroundColor")
    private String backgroundColor = null;

    //As Base64.
    @JsonProperty("image")
    private String image = null;

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
    public Element generateSvg(Document doc) {
        if (getBackgroundColor() != null) {
            final SvgRectangle rectangle = new SvgRectangle("100%", "100%", backgroundColor);
            return rectangle.generateSvg(doc);
        } else if (getImage() != null) {
            final SvgImage image = new SvgImage("background", getImage());
            return image.generateSvg(doc);
        }
        return null;
    }
}
