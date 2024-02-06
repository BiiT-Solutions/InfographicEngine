package com.biit.infographic.core.models.svg;

import org.apache.commons.io.IOUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public final class SvgUtils {
    private static final List<String> CHILDREN_ALLOWED = Arrays.asList("defs", "path", "g");
    private static final List<String> INKSCAPE_NOT_ALLOWED = Arrays.asList("sodipodi", "inkscape");

    private SvgUtils() {

    }

    public static Document stringToSvg(String svgCode) throws ParserConfigurationException, IOException, SAXException {
        return DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(IOUtils.toInputStream(svgCode));
    }

    public static Element stringToElement(String svgCode) throws ParserConfigurationException, IOException, SAXException {
        final NodeList children = stringToSvg(svgCode).getDocumentElement().getChildNodes();
        return (Element) children.item(0);
    }

    public static List<Element> getContent(String svgCode) throws ParserConfigurationException, IOException, SAXException {
        return getContent(svgCode, CHILDREN_ALLOWED);
    }

    public static List<Element> getContent(String svgCode, List<String> filter) throws ParserConfigurationException, IOException, SAXException {
        final NodeList children = stringToSvg(svgCode).getDocumentElement().getChildNodes();
        final List<Element> selectedOnes = new ArrayList<>();
        for (int i = 0; i < children.getLength(); i++) {
            if (children.item(i) != null && (filter == null || filter.isEmpty() || filter.contains(children.item(i).getNodeName()))) {
                try {
                    final Element child = (Element) children.item(i);
                    if (INKSCAPE_NOT_ALLOWED.stream().noneMatch(s -> child.getNodeName().startsWith(s))) {
                        selectedOnes.add(child);
                    }
                } catch (Exception ignored) {
                    //Not a node
                }
            }
        }
        return selectedOnes;
    }
}
