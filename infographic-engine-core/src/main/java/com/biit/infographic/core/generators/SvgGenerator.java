package com.biit.infographic.core.generators;

import com.biit.infographic.core.models.svg.ISvgElement;
import com.biit.infographic.core.models.svg.SvgTemplate;
import com.biit.infographic.logger.SvgGeneratorLogger;
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

    public static String generate(SvgTemplate svgTemplate) {
        final DOMImplementation impl = SVGDOMImplementation.getDOMImplementation();
        final Document doc = impl.createDocument(ISvgElement.NAMESPACE, "svg", null);

        svgTemplate.generateSvg(doc);

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
            //java.lang.RuntimeException: Namespace for prefix 'inkscape' has not been declared.
            //Please store as plain SVG.
            transformer.transform(domSource, result);
            return writer.toString();
        } catch (TransformerException ex) {
            SvgGeneratorLogger.errorMessage(SvgGenerator.class, ex);
        }
        return null;
    }
}
