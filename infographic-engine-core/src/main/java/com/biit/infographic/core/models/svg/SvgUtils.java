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

import com.biit.infographic.core.models.svg.exceptions.InvalidCodeException;
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
import java.util.Objects;

public final class SvgUtils {
    private static final List<String> CHILDREN_ALLOWED = Arrays.asList("defs", "path", "g");
    private static final List<String> INKSCAPE_NOT_ALLOWED = Arrays.asList("sodipodi", "inkscape");

    private SvgUtils() {

    }

    public static Document stringToSvg(String svgCode) throws ParserConfigurationException, IOException, SAXException {
        if (svgCode == null) {
            throw new InvalidCodeException(SvgUtils.class, "Provided code is null");
        }
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
        try {
            final NodeList children = stringToSvg(svgCode).getDocumentElement().getChildNodes();
            final List<Element> selectedOnes = new ArrayList<>();
            for (int i = 0; i < children.getLength(); i++) {
                if (children.item(i) != null && (filter == null || filter.isEmpty() || filter.contains(children.item(i).getNodeName()))) {
                    try {
                        final Element child = (Element) children.item(i);
                        if (INKSCAPE_NOT_ALLOWED.stream().noneMatch(s -> child.getNodeName().startsWith(s))) {
                            //Filter empty defs.
                            if (!Objects.equals(child.getNodeName(), "defs") || child.getChildNodes().getLength() > 1
                                    //Defs has empty spaces when embedding svgs
                                    || (child.getChildNodes().getLength() == 1 && !((Element) child.getChildNodes()).getTextContent().trim().isEmpty())) {
                                selectedOnes.add(child);
                            }
                        }
                    } catch (Exception ignored) {
                        //Not a node
                    }
                }
            }
            return selectedOnes;
        } catch (InvalidCodeException e) {
            return new ArrayList<>();
        }
    }
}
