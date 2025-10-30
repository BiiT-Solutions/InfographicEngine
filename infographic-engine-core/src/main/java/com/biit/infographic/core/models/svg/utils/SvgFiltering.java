package com.biit.infographic.core.models.svg.utils;

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

import com.biit.infographic.core.generators.SvgGenerator;
import com.biit.infographic.core.models.GeneratedInfographicAsImageDTO;
import com.biit.infographic.logger.InfographicEngineLogger;
import org.apache.batik.anim.dom.SAXSVGDocumentFactory;
import org.apache.batik.util.XMLResourceDescriptor;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.svg.SVGDocument;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public final class SvgFiltering {

    private static final String FONT_FACE_NODE = "^\\s*@font-face[\\S\\s]*$";

    private SvgFiltering() {

    }

    public static String cleanUpCode(String svgCode) {
        return filterEmptyImages(filterEmbeddedFonts(svgCode));
    }

    /**
     * Embedded fonts cannot be used on PDFs, PNGs or JPEG.
     */
    public static String filterEmbeddedFonts(String svgCode) {
        try {
            final SAXSVGDocumentFactory factory = new SAXSVGDocumentFactory(XMLResourceDescriptor.getXMLParserClassName());
            final SVGDocument svgDocument = factory.createSVGDocument("", new ByteArrayInputStream(svgCode.getBytes(StandardCharsets.UTF_8)));
            final NodeList styleList = svgDocument.getElementsByTagName("style");
            final List<Node> stylingToRemove = new ArrayList<>();
            for (int i = 0; i < styleList.getLength(); i++) {
                // To search only "style" desired children
                final Node defsChild = styleList.item(i);
                if (defsChild.getNodeType() == Node.ELEMENT_NODE
                        && defsChild.getNodeName().equalsIgnoreCase("style")
                        && defsChild.getTextContent().matches(FONT_FACE_NODE)) {
                    stylingToRemove.add(defsChild);
                    InfographicEngineLogger.warning(SvgFiltering.class, "Font is removed from svg document!");
                }
            }
            //Remove font-face nodes
            stylingToRemove.forEach(node -> node.getParentNode().removeChild(node));
            return SvgGenerator.convertToString(svgDocument);
        } catch (IOException e) {
            InfographicEngineLogger.severe(GeneratedInfographicAsImageDTO.class, "Cannot remove embedded fonts!");
            return svgCode;
        }
    }

    /**
     * Images not well-formed, causes the PDF to crash.
     *
     * @param svgCode
     * @return
     */
    public static String filterEmptyImages(String svgCode) {
        try {
            final SAXSVGDocumentFactory factory = new SAXSVGDocumentFactory(XMLResourceDescriptor.getXMLParserClassName());
            final SVGDocument svgDocument = factory.createSVGDocument("", new ByteArrayInputStream(svgCode.getBytes(StandardCharsets.UTF_8)));
            final NodeList imageList = svgDocument.getElementsByTagName("image");
            //Image content is on "ns0:href" property
            final List<Node> imageToDelete = new ArrayList<>();
            for (int i = 0; i < imageList.getLength(); i++) {
                // To search only "style" desired children
                final Node defsChild = imageList.item(i);
                if (defsChild.getAttributes().getNamedItemNS("http://www.w3.org/1999/xlink", "href") == null
                        || defsChild.getAttributes().getNamedItemNS("http://www.w3.org/1999/xlink", "href").getNodeValue().isEmpty()) {
                    imageToDelete.add(defsChild);
                    InfographicEngineLogger.warning(SvgFiltering.class, "Image '{}' is removed as is blank.",
                            (defsChild.getAttributes().getNamedItem("id") != null ? defsChild.getAttributes().getNamedItem("id").getNodeValue() : null));
                }
            }
            //Remove images
            imageToDelete.forEach(node -> node.getParentNode().removeChild(node));
            return SvgGenerator.convertToString(svgDocument);
        } catch (IOException e) {
            InfographicEngineLogger.severe(GeneratedInfographicAsImageDTO.class, "Cannot remove embedded fonts!");
            return svgCode;
        }
    }
}
