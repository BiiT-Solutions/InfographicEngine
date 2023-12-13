package com.biit.infographic.core.svg;

import com.biit.infographic.core.generators.SvgGenerator;
import com.biit.infographic.core.models.svg.SvgAreaElement;
import com.biit.infographic.core.models.svg.SvgBackground;
import com.biit.infographic.core.models.svg.SvgTemplate;
import com.biit.infographic.core.models.svg.components.SvgRectangle;
import com.biit.infographic.core.models.svg.components.text.FontWeight;
import com.biit.infographic.core.models.svg.components.text.SvgText;
import org.testng.annotations.Test;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

@Test(groups = "cadtCustomer")
public class CADTCustomerTest extends SvgGeneration {

    private static final String TITLE = "NATURAL STRENGTH AND NATURAL POTENTIAL";

    private static final String BORDER_COLOR = "b49057";

    private SvgTemplate cadtTemplate;

    private SvgBackground generateBackground() {
        final SvgBackground svgBackground = new SvgBackground();
        svgBackground.setBackgroundColor("#ffffff");
        return svgBackground;
    }

    private List<SvgAreaElement> generateHeader() {
        final List<SvgAreaElement> headerElements = new ArrayList<>();


        //Title background
        final SvgRectangle nameRectangle = new SvgRectangle(612L, 188L, "1260L", "127px", "ffffff");
        nameRectangle.getElementStroke().setStrokeColor(BORDER_COLOR);
        nameRectangle.getElementStroke().setStrokeWidth(7.07);
        nameRectangle.setXRadius(25L);
        nameRectangle.setYRadius(30L);
        headerElements.add(nameRectangle);

        final SvgText title = new SvgText(TITLE, 30, 2137L, 116L);
        title.setFontFamily("Arial-BoldMT, Arial, sans-serif");
        title.setFontWeight(FontWeight.BOLD);
        headerElements.add(title);


        return headerElements;
    }

    @Test
    public void generateCADT() throws IOException {
        cadtTemplate = new SvgTemplate();
        cadtTemplate.getElementAttributes().setHeight(3495L);
        cadtTemplate.getElementAttributes().setWidth(2481L);
        //cadtTemplate.setSvgBackground(generateBackground());

        cadtTemplate.addElements(generateHeader());


        try (PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(OUTPUT_FOLDER
                + File.separator + "CADT_Customer.svg")), true)) {
            out.println(SvgGenerator.generate(cadtTemplate));
        }
    }
}
