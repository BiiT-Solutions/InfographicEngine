package com.biit.infographic.core.pdf;

import com.biit.infographic.logger.InfographicEngineLogger;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
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
                final float svgImageWidth = (float) graphicsNode.getPrimitiveBounds().getWidth();
                final float svgImageHeight = (float) graphicsNode.getPrimitiveBounds().getHeight();

                final PdfTemplate template = PdfTemplate.createTemplate(writer, svgImageWidth, svgImageHeight);

                final Graphics2D g2d = template.createGraphics(template.getWidth(), template.getHeight(), FONT_MAPPER);
                try {
                    graphicsNode.paint(g2d);
                } finally {
                    g2d.dispose();
                }

                final ImgTemplate image = new ImgTemplate(template);
                if (image.getHeight() > getPageSize().getHeight() || image.getWidth() > getPageSize().getWidth()) {
                    image.scaleToFit(getPageSize().getWidth(), getPageSize().getHeight());
                }
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
