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

import com.biit.infographic.core.generators.JpegGenerator;
import com.biit.infographic.core.generators.PngGenerator;
import com.biit.infographic.core.generators.SvgGenerator;
import com.biit.infographic.core.models.svg.SvgBackground;
import com.biit.infographic.core.models.svg.SvgTemplate;
import com.biit.infographic.core.models.svg.utils.SvgFiltering;
import com.biit.infographic.persistence.entities.GeneratedInfographic;
import com.biit.server.utils.exceptions.EmptyPdfBodyException;
import com.biit.server.utils.exceptions.InvalidXmlElementException;
import com.biit.server.utils.pdf.events.FooterEvent;
import org.springframework.stereotype.Controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Controller
public class PdfController {

    private static final String FOOTER_TEXT = "";

    /**
     * Infographics without background can be cut by the PDF generator!
     */
    public byte[] generatePdfFromSvg(GeneratedInfographic generatedInfographic) throws InvalidXmlElementException, EmptyPdfBodyException {
        return generatePdfFromSvgs(generatedInfographic.getSvgContents());
    }

    public byte[] generatePdfFromSvg(SvgTemplate svgTemplate) throws InvalidXmlElementException, EmptyPdfBodyException {
        //Without a background the size of the image later is calculated incorrectly.
        if (svgTemplate.getSvgBackground() == null) {
            svgTemplate.setSvgBackground(new SvgBackground().backgroundColor("#ffffff00"));
        }
        svgTemplate.setEmbedFonts(false);
        return generatePdfFromSvgs(Collections.singletonList(SvgGenerator.generate(svgTemplate)));
    }

    /**
     * The PDF generator can cut infographics without a background!
     */
    public byte[] generatePdfFromSvgs(List<String> svgs) throws InvalidXmlElementException, EmptyPdfBodyException {
        FooterEvent.setFooterText(FOOTER_TEXT);
        //Filter embedded fonts.
        final List<String> filteredSvgs = new ArrayList<>();
        svgs.forEach(svgCode -> filteredSvgs.add(SvgFiltering.cleanUpCode(svgCode)));
        final InfographicFromSvg infographicFromSvg = new InfographicFromSvg(filteredSvgs);
        return infographicFromSvg.generate();
    }

    public byte[] generatePdfAsPngImage(SvgTemplate svgTemplate) throws InvalidXmlElementException, EmptyPdfBodyException {
        svgTemplate.setEmbedFonts(false);
        return generatePdfFromImage(Collections.singletonList(PngGenerator.generate(SvgGenerator.generate(svgTemplate))));
    }

    public byte[] generatePdfAsJpegImage(SvgTemplate svgTemplate) throws InvalidXmlElementException, EmptyPdfBodyException {
        svgTemplate.setEmbedFonts(false);
        return generatePdfFromImage(Collections.singletonList(JpegGenerator.generate(SvgGenerator.generate(svgTemplate))));
    }

    public byte[] generatePdfFromImage(List<byte[]> rawData) throws InvalidXmlElementException, EmptyPdfBodyException {
        FooterEvent.setFooterText(FOOTER_TEXT);
        final InfographicFromImage infographicFromImage = new InfographicFromImage(rawData);
        return infographicFromImage.generate();
    }
}
