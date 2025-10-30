package com.biit.infographic.core.pdf;

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

import com.biit.infographic.logger.InfographicEngineLogger;
import com.biit.server.utils.pdf.PdfDocument;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.FontFactory;
import com.lowagie.text.ImgTemplate;
import com.lowagie.text.pdf.DefaultFontMapper;
import com.lowagie.text.pdf.FontMapper;
import com.lowagie.text.pdf.PdfTemplate;
import com.lowagie.text.pdf.PdfWriter;
import org.apache.batik.anim.dom.SAXSVGDocumentFactory;
import org.apache.batik.bridge.BridgeContext;
import org.apache.batik.bridge.DocumentLoader;
import org.apache.batik.bridge.GVTBuilder;
import org.apache.batik.bridge.UserAgent;
import org.apache.batik.bridge.UserAgentAdapter;
import org.apache.batik.gvt.GraphicsNode;
import org.apache.batik.util.XMLResourceDescriptor;
import org.w3c.dom.svg.SVGDocument;

import java.awt.Graphics2D;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class InfographicFromSvg extends InfographicPdf {

    private final List<String> svgs;

    private static final FontMapper FONT_MAPPER;

    static {
        FONT_MAPPER = createFontMapper();
    }

    public InfographicFromSvg(List<String> svgs) {
        super();
        this.svgs = svgs;
    }

    private GraphicsNode convert(String svgCode) throws IOException {
        //Create org.w3c.dom.svg.SVGDocument
        final SAXSVGDocumentFactory factory = new SAXSVGDocumentFactory(XMLResourceDescriptor.getXMLParserClassName());
        final SVGDocument svgDocument = factory.createSVGDocument("", new ByteArrayInputStream(svgCode.getBytes(StandardCharsets.UTF_8)));

        //Create a org.apache.batik.gvt.GraphicsNode from the org.w3c.dom.svg.SVGDocument
        final UserAgent userAgent = new UserAgentAdapter();
        final DocumentLoader loader = new DocumentLoader(userAgent);

        // Notice, that you should use org.apache.batik.bridge.svg12.SVG12BridgeContext.SVG12BridgeContext for the svg version 1.2
        final BridgeContext context = new BridgeContext(userAgent, loader);
        context.setDynamicState(BridgeContext.DYNAMIC);

        final GVTBuilder builder = new GVTBuilder();
        return builder.build(context, svgDocument);
    }

    @Override
    protected void createContent(Document document, PdfWriter writer) throws DocumentException {
        this.svgs.forEach(svg -> {
            try {
                final GraphicsNode graphicsNode = convert(svg);
                final double svgImageWidth = graphicsNode.getPrimitiveBounds().getWidth();
                final double svgImageHeight = graphicsNode.getPrimitiveBounds().getHeight()
                        * (svgImageWidth / graphicsNode.getPrimitiveBounds().getWidth());

                final PdfTemplate template = PdfTemplate.createTemplate(writer, (float) svgImageWidth,
                        (float) svgImageHeight);

                final Graphics2D g2d = template.createGraphics(template.getWidth(), template.getHeight(), FONT_MAPPER);
                try {
                    graphicsNode.paint(g2d);
                } finally {
                    g2d.dispose();
                }

                final ImgTemplate image = new ImgTemplate(template);
                if (image.getHeight() > getPageSize().getHeight() - (PdfDocument.DEFAULT_TOP_MARGIN + PdfDocument.DEFAULT_BOTTOM_MARGIN)
                        || image.getWidth() > getPageSize().getWidth() - (PdfDocument.DEFAULT_RIGHT_MARGIN + PdfDocument.DEFAULT_LEFT_MARGIN)) {
                    image.scaleToFit(getPageSize().getWidth() - (PdfDocument.DEFAULT_RIGHT_MARGIN + PdfDocument.DEFAULT_LEFT_MARGIN),
                            getPageSize().getHeight() - (PdfDocument.DEFAULT_TOP_MARGIN + PdfDocument.DEFAULT_BOTTOM_MARGIN));
                }
                image.setAlignment(Element.ALIGN_CENTER);
                document.add(image);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    public static FontMapper createFontMapper() {
        final DefaultFontMapper fontMapper = new DefaultFontMapper();
        com.biit.infographic.core.models.svg.components.text.FontFactory.getDefaultFoldersToSearch()
                .forEach(folder -> {
                    InfographicEngineLogger.info(InfographicFromSvg.class, "Registering folder '{}' for PDF font mapper.", folder);
                    fontMapper.insertDirectory(folder);
                    FontFactory.registerDirectory(folder);
                });
        InfographicEngineLogger.debug(InfographicFromSvg.class, "Registered fonts are '{}'", FontFactory.getRegisteredFonts());
        return fontMapper;
    }
}
