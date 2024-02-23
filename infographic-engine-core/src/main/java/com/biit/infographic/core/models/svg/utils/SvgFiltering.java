package com.biit.infographic.core.models.svg.utils;

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

    /**
     * Embedded fonts cannot be used on PDFs, PNGs or JPEG.
     */
    public static String filterEmbeddedFonts(String svgCode) {
        try {
            final SAXSVGDocumentFactory factory = new SAXSVGDocumentFactory(XMLResourceDescriptor.getXMLParserClassName());
            final SVGDocument svgDocument = factory.createSVGDocument("", new ByteArrayInputStream(svgCode.getBytes(StandardCharsets.UTF_8)));
            final NodeList styleList = svgDocument.getElementsByTagName("style");
            final List<Node> stylingNodes = new ArrayList<>();
            for (int i = 0; i < styleList.getLength(); i++) {
                // To search only "style" desired children
                final Node defsChild = styleList.item(i);
                if (defsChild.getNodeType() == Node.ELEMENT_NODE
                        && defsChild.getNodeName().equalsIgnoreCase("style")
                        && defsChild.getTextContent().matches(FONT_FACE_NODE)) {
                    stylingNodes.add(defsChild);
                }
            }
            //Remove font-face nodes
            stylingNodes.forEach(node -> node.getParentNode().removeChild(node));
            return SvgGenerator.convertToString(svgDocument);
        } catch (IOException e) {
            InfographicEngineLogger.severe(GeneratedInfographicAsImageDTO.class, "Cannot remove embedded fonts!");
            return svgCode;
        }
    }
}
