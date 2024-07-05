package com.biit.infographic.core.models.svg.clip;

import com.biit.infographic.core.models.svg.ElementType;
import com.biit.infographic.core.models.svg.SvgAreaElement;
import com.biit.infographic.core.models.svg.SvgElement;
import com.biit.infographic.core.models.svg.exceptions.InvalidAttributeException;
import com.biit.infographic.core.models.svg.serialization.SvgClipPathDeserializer;
import com.fasterxml.jackson.annotation.JsonProperty;
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

    @JsonProperty("sourceX")
    private Long sourceX;

    @JsonProperty("sourceY")
    private Long sourceY;

    @JsonProperty("sourceHeight")
    private Long sourceHeight;

    @JsonProperty("sourceWidth")
    private Long sourceWidth;

    @JsonProperty("clipType")
    private ClipType clipType;

    protected SvgClipPath(ClipType clipType) {
        this.clipType = clipType;
        setElementType(ElementType.CLIP_PATH);
    }

    protected abstract SvgAreaElement generateArea();

    public Long getSourceX() {
        return sourceX;
    }

    public void setSourceX(Long xCoordinate) {
        this.sourceX = xCoordinate;
    }

    public Long getSourceY() {
        return sourceY;
    }

    public void setSourceY(Long yCoordinate) {
        this.sourceY = yCoordinate;
    }

    public Long getSourceHeight() {
        return sourceHeight;
    }

    public void setSourceHeight(Long height) {
        this.sourceHeight = height;
    }

    public Long getSourceWidth() {
        return sourceWidth;
    }

    public void setSourceWidth(Long width) {
        this.sourceWidth = width;
    }

    public void setClipType(ClipType clipType) {
        this.clipType = clipType;
    }

    public ClipType getClipType() {
        return clipType;
    }

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
