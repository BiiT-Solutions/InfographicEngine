package com.biit.infographic.core.svg;

import com.biit.infographic.core.models.svg.ISvgElement;
import com.biit.infographic.core.models.svg.SvgDocument;
import com.biit.infographic.logger.InfographicEngineLogger;
import org.apache.batik.anim.dom.SVGDOMImplementation;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;

public final class SvgGenerator {
    private static final int XML_INDENTATION = 4;

    private SvgGenerator() {

    }

    public static String generate(SvgDocument svgDocument) {
        final DOMImplementation impl = SVGDOMImplementation.getDOMImplementation();
        final Document doc = impl.createDocument(ISvgElement.NAMESPACE, "svg", null);

        svgDocument.generateSvg(doc);

        return convertToString(doc);
    }

    //method to convert Document to String
    public static String convertToString(Document doc) {
        try {
            final DOMSource domSource = new DOMSource(doc);
            final StringWriter writer = new StringWriter();
            final StreamResult result = new StreamResult(writer);
            final TransformerFactory transformerFactory = TransformerFactory.newInstance();
            transformerFactory.setAttribute("indent-number", XML_INDENTATION);
            final Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.ENCODING, StandardCharsets.UTF_8.name());
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.transform(domSource, result);
            return writer.toString();
        } catch (TransformerException ex) {
            InfographicEngineLogger.errorMessage(SvgGenerator.class, ex);
        }
        return null;
    }
}
