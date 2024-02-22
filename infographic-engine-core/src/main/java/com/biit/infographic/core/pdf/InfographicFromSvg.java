package com.biit.infographic.core.pdf;

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
import org.apache.batik.bridge.svg12.SVG12BridgeContext;
import org.apache.batik.gvt.GraphicsNode;
import org.apache.batik.util.XMLResourceDescriptor;
import org.w3c.dom.svg.SVGDocument;

import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.GraphicsEnvironment;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Set;

public class InfographicFromSvg extends InfographicPdf {

    private final List<String> svgs;

    private final FontMapper fontMapper;

    public InfographicFromSvg(List<String> svgs) {
        this.svgs = svgs;
        this.fontMapper = createFontMapper();
    }

    private GraphicsNode convert(String svgCode) throws IOException {
        //Create org.w3c.dom.svg.SVGDocument
        final SAXSVGDocumentFactory factory = new SAXSVGDocumentFactory(XMLResourceDescriptor.getXMLParserClassName());
        final SVGDocument svgDocument = factory.createSVGDocument("", new ByteArrayInputStream(svgCode.getBytes(StandardCharsets.UTF_8)));

        //Create a org.apache.batik.gvt.GraphicsNode from the org.w3c.dom.svg.SVGDocument
        final UserAgent userAgent = new UserAgentAdapter();
        final DocumentLoader loader = new DocumentLoader(userAgent);

        // Notice, that you should use org.apache.batik.bridge.svg12.SVG12BridgeContext.SVG12BridgeContext for the svg version 1.2
        final BridgeContext context = new SVG12BridgeContext(userAgent, loader);
        context.setDynamicState(BridgeContext.DYNAMIC);

        //Register fonts
        final GraphicsEnvironment graphicsEnvironment = GraphicsEnvironment.getLocalGraphicsEnvironment();
        final Set<Font> loadedFonts = com.biit.infographic.core.models.svg.components.text.FontFactory.getLoadedFonts();
        for (Font font : loadedFonts) {
            graphicsEnvironment.registerFont(font);
//            try {
//                graphicsEnvironment.registerFont(Font.createFont(Font.TRUETYPE_FONT,
//                        new File(com.biit.infographic.core.models.svg.components.text.FontFactory.getFontPath(font.getFamily(), FontWeight.NORMAL))));
//            } catch (FontFormatException e) {
//                throw new RuntimeException(e);
//            }
        }
        loadedFonts.forEach(graphicsEnvironment::registerFont);

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

                final Graphics2D g2d = template.createGraphics(template.getWidth(), template.getHeight(), fontMapper);
                try {
                    graphicsNode.paint(g2d);
                } finally {
                    g2d.dispose();
                }

                final ImgTemplate image = new ImgTemplate(template);
                document.add(image);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    private FontMapper createFontMapper() {
        final DefaultFontMapper fontMapper = new DefaultFontMapper();
        //fontMapper.registerDirectories();
        com.biit.infographic.core.models.svg.components.text.FontFactory.getDefaultFoldersToSearch()
                .forEach(folder -> {
                    fontMapper.insertDirectory(folder);
                    FontFactory.registerDirectory(folder);
                });

//        final Map<String, Map<FontWeight, String>> fonts = com.biit.infographic.core.models.svg.components.text.FontFactory.getFontsPaths();

//        for (Map.Entry<String, Map<FontWeight, String>> fontEntry : fonts.entrySet()) {
//            for (Map.Entry<FontWeight, String> weights : fontEntry.getValue().entrySet()) {
//                FontFactory.register(weights.getValue(), fontEntry.getKey());
//                SvgGeneratorLogger.debug(this.getClass(), "Registered font '{}' on PDF.", fontEntry.getKey());
//            }
//        }

//        final List<String> fonts = FontSearcher.getFilesOnFolderPath(com.biit.infographic.core.models.svg.components.text.FontFactory.FONTS_FOLDER);
//        for (String font : fonts) {
//            try {
//                FontFactory.register(font);
//                SvgGeneratorLogger.debug(this.getClass(), "Registered font '{}' on PDF.", font);
//            } catch (Exception e) {
//                SvgGeneratorLogger.errorMessage(this.getClass(), e);
//            }
//        }
        return fontMapper;
    }
}
