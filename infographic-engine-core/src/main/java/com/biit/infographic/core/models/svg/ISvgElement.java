package com.biit.infographic.core.models.svg;

import com.biit.infographic.core.models.svg.exceptions.InvalidAttributeException;
import org.apache.batik.anim.dom.SVGDOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.util.Collection;

public interface ISvgElement {
    String NAMESPACE = SVGDOMImplementation.SVG_NAMESPACE_URI;

    Collection<Element> generateSvg(Document doc);

    void validateAttributes() throws InvalidAttributeException;
}
