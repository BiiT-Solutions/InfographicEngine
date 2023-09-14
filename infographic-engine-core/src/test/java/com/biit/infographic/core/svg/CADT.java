package com.biit.infographic.core.svg;

import com.biit.infographic.core.generators.SvgGenerator;
import com.biit.infographic.core.models.svg.SvgBackground;
import com.biit.infographic.core.models.svg.SvgElement;
import com.biit.infographic.core.models.svg.SvgTemplate;
import com.biit.infographic.core.models.svg.components.StrokeLineCap;
import com.biit.infographic.core.models.svg.components.SvgCircle;
import com.biit.infographic.core.models.svg.components.SvgImage;
import com.biit.infographic.core.models.svg.components.SvgLine;
import com.biit.infographic.core.models.svg.components.SvgRectangle;
import com.biit.infographic.core.models.svg.components.text.FontWeight;
import com.biit.infographic.core.models.svg.components.text.SvgText;
import org.testng.annotations.AfterClass;
import org.testng.annotations.Test;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

@Test(groups = "cadt")
public class CADT extends SvgGeneration {
    private static String BORDER_COLOR = "b49057";

    private SvgBackground generateBackground() {
        final SvgBackground svgBackground = new SvgBackground();
        svgBackground.setBackgroundColor("#e1dbd6");
        svgBackground.setxRadius(104L);
        svgBackground.setyRadius(104L);
        return svgBackground;
    }


    private List<SvgElement> generateHeader() {
        final List<SvgElement> headerElements = new ArrayList<>();


        //Name background
        final SvgRectangle nameRectangle = new SvgRectangle(1676L, 81L, "1653px", "225px", "ffffff");
        nameRectangle.getElementStroke().setStrokeColor(BORDER_COLOR);
        nameRectangle.getElementStroke().setStrokeWidth(7.07);
        nameRectangle.setXRadius(60L);
        nameRectangle.setYRadius(60L);
        headerElements.add(nameRectangle);

        final SvgText name = new SvgText("MARIA GARCIA", 100, 2137L, 116L);
        name.setFontFamily("Arial-BoldMT, Arial, sans-serif");
        name.setFontWeight(FontWeight.BOLD);
        headerElements.add(name);

        final SvgText position = new SvgText("for big business director", 83, 2062L, 215L);
        position.setFontFamily("Arial-BoldMT, Arial, sans-serif");
        headerElements.add(position);

        //Logo
        final SvgImage logo = new SvgImage();
        logo.setFromResource("images/NHM-Logo.png");
        logo.getElementAttributes().setXCoordinate(1272L);
        logo.getElementAttributes().setYCoordinate(67L);
        logo.getElementAttributes().setWidth(282L);
        logo.getElementAttributes().setHeight(254L);
        headerElements.add(logo);

        //Score circle
        final SvgCircle scoreCircle = new SvgCircle(3540L, 80L, 113L);
        scoreCircle.getElementAttributes().setFill("ffffff");
        scoreCircle.getElementStroke().setStrokeWidth(8.3);
        scoreCircle.getElementStroke().setStrokeColor(BORDER_COLOR);
        headerElements.add(scoreCircle);


        final SvgText score = new SvgText("90", 80, 3610L, 129L);
        score.setFontFamily("Arial-BoldMT, Arial, sans-serif");
        score.setFontWeight(FontWeight.BOLD);
        headerElements.add(score);

        final SvgLine scoreSeparator = new SvgLine(3622L, 192L, 3680L, 192L);
        scoreSeparator.getElementStroke().setStrokeWidth(4D);
        scoreSeparator.getElementStroke().setLineCap(StrokeLineCap.ROUND);
        headerElements.add(scoreSeparator);

        final SvgText scoreTotal = new SvgText("100", 80, 3588L, 205L);
        scoreTotal.setFontFamily("Arial-BoldMT, Arial, sans-serif");
        scoreTotal.setFontWeight(FontWeight.BOLD);
        headerElements.add(scoreTotal);

        return headerElements;
    }


    @Test
    public void generateCADT() throws IOException {
        final SvgTemplate svgTemplate = new SvgTemplate();
        svgTemplate.getElementAttributes().setHeight(3488L);
        svgTemplate.getElementAttributes().setWidth(5038L);
        svgTemplate.setSvgBackground(generateBackground());

        //Add big white rectangle as 2nd background.
        final SvgRectangle secondBackground = new SvgRectangle(163L, 189L, "4680px", "3153px", "ffffff");
        secondBackground.getElementStroke().setStrokeColor(BORDER_COLOR);
        secondBackground.getElementStroke().setStrokeWidth(8.3);
        secondBackground.setXRadius(70L);
        secondBackground.setYRadius(70L);
        svgTemplate.addElement(secondBackground);

        svgTemplate.addElements(generateHeader());

        try (PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(OUTPUT_FOLDER
                + File.separator + "CADT.svg")), true)) {
            out.println(SvgGenerator.generate(svgTemplate));
        }
        //checkContent(SvgGenerator.generate(svgTemplate), "CADT.svg");
    }

    @AfterClass
    public void removeFolder() {
        // Assert.assertTrue(deleteDirectory(new File(OUTPUT_FOLDER)));
    }
}
