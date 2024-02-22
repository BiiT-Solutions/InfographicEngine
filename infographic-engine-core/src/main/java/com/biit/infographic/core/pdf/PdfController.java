package com.biit.infographic.core.pdf;

import com.biit.infographic.core.generators.SvgGenerator;
import com.biit.infographic.core.models.svg.SvgBackground;
import com.biit.infographic.core.models.svg.SvgTemplate;
import com.biit.infographic.core.providers.GeneratedInfographicProvider;
import com.biit.infographic.persistence.entities.GeneratedInfographic;
import com.biit.server.utils.exceptions.EmptyPdfBodyException;
import com.biit.server.utils.exceptions.InvalidXmlElementException;
import com.biit.server.utils.pdf.events.FooterEvent;
import org.springframework.stereotype.Controller;

import java.util.Collections;
import java.util.List;

@Controller
public class PdfController {

    private static final String FOOTER_TEXT = "Infographic Engine v2";

    private final GeneratedInfographicProvider generatedInfographicProvider;

    public PdfController(GeneratedInfographicProvider generatedInfographicProvider) {
        this.generatedInfographicProvider = generatedInfographicProvider;
    }

    /**
     * Infographics without background can be cutt by the PDF generator!
     */
    public byte[] generatePdfFromSvg(GeneratedInfographic generatedInfographic) throws InvalidXmlElementException, EmptyPdfBodyException {
        return generatePdfFromSvg(generatedInfographic.getSvgContents());
    }

    public byte[] generatePdfFromSvg(SvgTemplate svgTemplate) throws InvalidXmlElementException, EmptyPdfBodyException {
        //Without a background the size of the image later is calculated incorrectly.
        if (svgTemplate.getSvgBackground() == null) {
            svgTemplate.setSvgBackground(new SvgBackground().backgroundColor("#ffffff00"));
        }
        return generatePdfFromSvg(Collections.singletonList(SvgGenerator.generate(svgTemplate)));
    }

    /**
     * Infographics without background can be cutt by the PDF generator!
     */
    public byte[] generatePdfFromSvg(List<String> svgs) throws InvalidXmlElementException, EmptyPdfBodyException {
        FooterEvent.setFooterText(FOOTER_TEXT);
        final InfographicFromSvg infographicFromSvg = new InfographicFromSvg(svgs);
        return infographicFromSvg.generate();
    }

    public byte[] generatePdfFromImage(List<byte[]> rawData) throws InvalidXmlElementException, EmptyPdfBodyException {
        FooterEvent.setFooterText(FOOTER_TEXT);
        final InfographicFromImage infographicFromImage = new InfographicFromImage(rawData);
        return infographicFromImage.generate();
    }
}
