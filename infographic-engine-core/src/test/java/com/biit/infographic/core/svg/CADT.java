package com.biit.infographic.core.svg;

import com.biit.infographic.core.generators.SvgGenerator;
import com.biit.infographic.core.models.svg.SvgBackground;
import com.biit.infographic.core.models.svg.SvgElement;
import com.biit.infographic.core.models.svg.SvgTemplate;
import com.biit.infographic.core.models.svg.components.Point;
import com.biit.infographic.core.models.svg.components.StrokeLineCap;
import com.biit.infographic.core.models.svg.components.SvgCircle;
import com.biit.infographic.core.models.svg.components.SvgImage;
import com.biit.infographic.core.models.svg.components.SvgLine;
import com.biit.infographic.core.models.svg.components.SvgPath;
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
    private static final Double DEFAULT_STROKE_WIDTH = 8.33D;
    private static final String BORDER_COLOR = "b49057";
    private static final String UNIVERSAL_COLOR = "919ee1";
    private static final String SOCIETY_COLOR = "7ccadf";
    private static final String VISION_COLOR = "e9a197";
    private static final String STRENGTH_COLOR = "919ee1";
    private static final String STRUCTURE_COLOR = "8bc4ab";
    private static final String INSPIRATION_COLOR = "919ee1";
    private static final String ADAPTABILITY_COLOR = "7ccadf";
    private static final String ACTION_COLOR = "e9a197";
    private static final String MATERIAL_ATTACHMENT_COLOR = "8bc4ab";
    private static final String COMMUNICATION_COLOR = "7ccadf";
    private static final String SELF_AWARE_COLOR = "e9a197";
    private static final String ANALYSIS_COLOR = "8bc4ab";

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
        scoreCircle.getElementStroke().setStrokeWidth(DEFAULT_STROKE_WIDTH);
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

    private List<SvgElement> generateUniversal() {
        final List<SvgElement> universalElements = new ArrayList<>();

        //Line
        final SvgPath path = new SvgPath(UNIVERSAL_COLOR, DEFAULT_STROKE_WIDTH, 2252L, 1542L, new Point(2252L, 1292L),
                new Point(2218L, 1250L), new Point(281L, 1250L));
        universalElements.add(path);

        //Circles
        final SvgCircle scoreCircle1 = new SvgCircle(2205L, 1244L, 46L);
        scoreCircle1.getElementAttributes().setFill(UNIVERSAL_COLOR);
        universalElements.add(scoreCircle1);

        final SvgCircle scoreCircle2 = new SvgCircle(2205L, 1370L, 46L);
        scoreCircle2.getElementAttributes().setFill(UNIVERSAL_COLOR);
        universalElements.add(scoreCircle2);

        final SvgCircle scoreCircle3 = new SvgCircle(2205L, 1495L, 46L);
        scoreCircle3.getElementAttributes().setFill(UNIVERSAL_COLOR);
        universalElements.add(scoreCircle3);

        //Title
        final SvgText title = new SvgText("UNIVERSAL", 67, FontWeight.BOLD, UNIVERSAL_COLOR, 285L, 593L);
        universalElements.add(title);

        final SvgLine titleLine = new SvgLine(UNIVERSAL_COLOR, DEFAULT_STROKE_WIDTH, 281L, 655L, 1152L, 655L);
        universalElements.add(titleLine);

        return universalElements;
    }

    private List<SvgElement> generateSociety() {
        final List<SvgElement> societyElements = new ArrayList<>();

        //Line
        final SvgPath path = new SvgPath(SOCIETY_COLOR, DEFAULT_STROKE_WIDTH, 2420L, 1542L, new Point(2420L, 1292L),
                new Point(2177L, 1048L), new Point(1240L, 1048L));
        societyElements.add(path);

        //Circles
        final SvgCircle scoreCircle1 = new SvgCircle(2372L, 1244L, 46L);
        scoreCircle1.getElementAttributes().setFill(SOCIETY_COLOR);
        societyElements.add(scoreCircle1);

        final SvgCircle scoreCircle2 = new SvgCircle(2372L, 1370L, 46L);
        scoreCircle2.getElementAttributes().setFill(SOCIETY_COLOR);
        societyElements.add(scoreCircle2);

        final SvgCircle scoreCircle3 = new SvgCircle(2372L, 1495L, 46L);
        scoreCircle3.getElementAttributes().setFill(SOCIETY_COLOR);
        societyElements.add(scoreCircle3);

        //Title
        final SvgText title = new SvgText("SOCIETY", 67, FontWeight.BOLD, SOCIETY_COLOR, 1242L, 391L);
        societyElements.add(title);

        final SvgLine titleLine = new SvgLine(SOCIETY_COLOR, DEFAULT_STROKE_WIDTH, 1240L, 453L, 2111L, 453L);
        societyElements.add(titleLine);

        return societyElements;
    }

    private List<SvgElement> generateVision() {
        final List<SvgElement> visionElements = new ArrayList<>();

        //Line
        final SvgPath path = new SvgPath(VISION_COLOR, DEFAULT_STROKE_WIDTH, 2587L, 1542L, new Point(2587L, 1292L),
                new Point(2829L, 1048L), new Point(3767L, 1048L));
        visionElements.add(path);

        //Circles
        final SvgCircle scoreCircle1 = new SvgCircle(2539L, 1244L, 46L);
        scoreCircle1.getElementAttributes().setFill(VISION_COLOR);
        visionElements.add(scoreCircle1);

        final SvgCircle scoreCircle2 = new SvgCircle(2539L, 1370L, 46L);
        scoreCircle2.getElementAttributes().setFill(VISION_COLOR);
        visionElements.add(scoreCircle2);

        final SvgCircle scoreCircle3 = new SvgCircle(2539L, 1495L, 46L);
        scoreCircle3.getElementAttributes().setFill(VISION_COLOR);
        visionElements.add(scoreCircle3);

        //Title
        final SvgText title = new SvgText("VISION", 67, FontWeight.BOLD, VISION_COLOR, 2896L, 391L);
        visionElements.add(title);

        final SvgLine titleLine = new SvgLine(VISION_COLOR, DEFAULT_STROKE_WIDTH, 2896L, 453L, 3766L, 453L);
        visionElements.add(titleLine);

        return visionElements;
    }

    private List<SvgElement> generateStrength() {
        final List<SvgElement> strengthElements = new ArrayList<>();

        //Line
        final SvgPath path = new SvgPath(STRENGTH_COLOR, DEFAULT_STROKE_WIDTH, 2754L, 1542L, new Point(2754L, 1292L),
                new Point(2795L, 1250L), new Point(4725L, 1250L));
        strengthElements.add(path);

        //Circles
        final SvgCircle scoreCircle1 = new SvgCircle(2706L, 1244L, 46L);
        scoreCircle1.getElementAttributes().setFill(STRENGTH_COLOR);
        strengthElements.add(scoreCircle1);

        final SvgCircle scoreCircle2 = new SvgCircle(2706L, 1370L, 46L);
        scoreCircle2.getElementAttributes().setFill(STRENGTH_COLOR);
        strengthElements.add(scoreCircle2);

        final SvgCircle scoreCircle3 = new SvgCircle(2706L, 1495L, 46L);
        scoreCircle3.getElementAttributes().setFill(STRENGTH_COLOR);
        strengthElements.add(scoreCircle3);

        //Title
        final SvgText title = new SvgText("STRENGTH", 67, FontWeight.BOLD, STRENGTH_COLOR, 3856L, 593L);
        strengthElements.add(title);

        final SvgLine titleLine = new SvgLine(STRENGTH_COLOR, DEFAULT_STROKE_WIDTH, 3855L, 653L, 4725L, 653L);
        strengthElements.add(titleLine);

        return strengthElements;
    }

    private List<SvgElement> generateStructure() {
        final List<SvgElement> structureElements = new ArrayList<>();

        //Line
        final SvgLine line1 = new SvgLine(STRUCTURE_COLOR, DEFAULT_STROKE_WIDTH, 2253L, 1694L, 2253L, 1851L);
        structureElements.add(line1);

        final SvgLine line2 = new SvgLine(STRUCTURE_COLOR, DEFAULT_STROKE_WIDTH, 2044L, 1772L, 2253L, 1772L);
        structureElements.add(line2);

        //Circles
        final SvgCircle scoreCircle1 = new SvgCircle(2205L, 1662L, 46L);
        scoreCircle1.getElementAttributes().setFill(STRUCTURE_COLOR);
        structureElements.add(scoreCircle1);

        final SvgCircle scoreCircle2 = new SvgCircle(2205L, 1788L, 46L);
        scoreCircle2.getElementAttributes().setFill(STRUCTURE_COLOR);
        structureElements.add(scoreCircle2);

        //Title
        final SvgText title = new SvgText("STRUCTURE", 67, FontWeight.BOLD, STRUCTURE_COLOR, 680L, 1421L);
        structureElements.add(title);

        final SvgLine titleLine = new SvgLine(STRUCTURE_COLOR, DEFAULT_STROKE_WIDTH, 281L, 1483L, 1151L, 1483L);
        structureElements.add(titleLine);

        final SvgLine titleLineButton = new SvgLine(STRUCTURE_COLOR, DEFAULT_STROKE_WIDTH, 281L, 2187L, 1151L, 2187L);
        structureElements.add(titleLineButton);

        return structureElements;
    }

    private List<SvgElement> generateInspiration() {
        final List<SvgElement> inspirationElements = new ArrayList<>();

        //Line
        final SvgLine line1 = new SvgLine(INSPIRATION_COLOR, DEFAULT_STROKE_WIDTH, 2420L, 1694L, 2420L, 1851L);
        inspirationElements.add(line1);

        final SvgLine line2 = new SvgLine(INSPIRATION_COLOR, DEFAULT_STROKE_WIDTH, 2044L, 1772L, 2420L, 1772L);
        inspirationElements.add(line2);

        //Circles
        final SvgCircle scoreCircle1 = new SvgCircle(2372L, 1662L, 46L);
        scoreCircle1.getElementAttributes().setFill(INSPIRATION_COLOR);
        inspirationElements.add(scoreCircle1);

        final SvgCircle scoreCircle2 = new SvgCircle(2372L, 1788L, 46L);
        scoreCircle2.getElementAttributes().setFill(INSPIRATION_COLOR);
        inspirationElements.add(scoreCircle2);

        //Title
        final SvgText title = new SvgText("INSPIRATION", 67, FontWeight.BOLD, INSPIRATION_COLOR, 1191L, 1421L);
        inspirationElements.add(title);

        final SvgLine titleLine = new SvgLine(INSPIRATION_COLOR, DEFAULT_STROKE_WIDTH, 1162L, 1483L, 2032L, 1483L);
        inspirationElements.add(titleLine);

        final SvgLine titleLineBottom = new SvgLine(INSPIRATION_COLOR, DEFAULT_STROKE_WIDTH, 1162L, 2187L, 2032L, 2187L);
        inspirationElements.add(titleLineBottom);

        return inspirationElements;
    }

    private List<SvgElement> generateAdaptability() {
        final List<SvgElement> adaptabilityElements = new ArrayList<>();

        //Line
        final SvgLine line1 = new SvgLine(ADAPTABILITY_COLOR, DEFAULT_STROKE_WIDTH, 2587L, 1694L, 2587L, 1851L);
        adaptabilityElements.add(line1);

        final SvgLine line2 = new SvgLine(ADAPTABILITY_COLOR, DEFAULT_STROKE_WIDTH, 2587L, 1772L, 2954L, 1772L);
        adaptabilityElements.add(line2);

        //Circles
        final SvgCircle scoreCircle1 = new SvgCircle(2539L, 1662L, 46L);
        scoreCircle1.getElementAttributes().setFill(ADAPTABILITY_COLOR);
        adaptabilityElements.add(scoreCircle1);

        final SvgCircle scoreCircle2 = new SvgCircle(2539L, 1788L, 46L);
        scoreCircle2.getElementAttributes().setFill(ADAPTABILITY_COLOR);
        adaptabilityElements.add(scoreCircle2);

        //Title
        final SvgText title = new SvgText("ADAPTABILITY", 67, FontWeight.BOLD, ADAPTABILITY_COLOR, 3285L, 1421L);
        adaptabilityElements.add(title);

        final SvgLine titleLine = new SvgLine(ADAPTABILITY_COLOR, DEFAULT_STROKE_WIDTH, 2974L, 1483L, 3844L, 1483L);
        adaptabilityElements.add(titleLine);

        final SvgLine titleLineBottom = new SvgLine(ADAPTABILITY_COLOR, DEFAULT_STROKE_WIDTH, 2974L, 2187L, 3844L, 2187L);
        adaptabilityElements.add(titleLineBottom);

        return adaptabilityElements;
    }

    private List<SvgElement> generateAction() {
        final List<SvgElement> actionElements = new ArrayList<>();

        //Line
        final SvgLine line1 = new SvgLine(ACTION_COLOR, DEFAULT_STROKE_WIDTH, 2753L, 1694L, 2753L, 1851L);
        actionElements.add(line1);

        final SvgLine line2 = new SvgLine(ACTION_COLOR, DEFAULT_STROKE_WIDTH, 2754L, 1772L, 2954L, 1772L);
        actionElements.add(line2);

        //Circles
        final SvgCircle scoreCircle1 = new SvgCircle(2706L, 1662L, 46L);
        scoreCircle1.getElementAttributes().setFill(ACTION_COLOR);
        actionElements.add(scoreCircle1);

        final SvgCircle scoreCircle2 = new SvgCircle(2706L, 1788L, 46L);
        scoreCircle2.getElementAttributes().setFill(ACTION_COLOR);
        actionElements.add(scoreCircle2);

        //Title
        final SvgText title = new SvgText("ACTION", 67, FontWeight.BOLD, ACTION_COLOR, 3877L, 1421L);
        actionElements.add(title);

        final SvgLine titleLine = new SvgLine(ACTION_COLOR, DEFAULT_STROKE_WIDTH, 3854L, 1483L, 4725L, 1483L);
        actionElements.add(titleLine);

        final SvgLine titleLineButton = new SvgLine(ACTION_COLOR, DEFAULT_STROKE_WIDTH, 3854L, 2187L, 4725L, 2187L);
        actionElements.add(titleLineButton);

        return actionElements;
    }

    private List<SvgElement> generateMaterialAttachment() {
        final List<SvgElement> materialAttachmentElements = new ArrayList<>();

        //Line
        final SvgPath path = new SvgPath(MATERIAL_ATTACHMENT_COLOR, DEFAULT_STROKE_WIDTH, 2252L, 2002L, new Point(2252L, 2252L),
                new Point(2075L, 2430L), new Point(281L, 2430L));
        materialAttachmentElements.add(path);

        //Circles
        final SvgCircle scoreCircle1 = new SvgCircle(2205L, 1954L, 46L);
        scoreCircle1.getElementAttributes().setFill(MATERIAL_ATTACHMENT_COLOR);
        materialAttachmentElements.add(scoreCircle1);

        final SvgCircle scoreCircle2 = new SvgCircle(2205L, 2080L, 46L);
        scoreCircle2.getElementAttributes().setFill(MATERIAL_ATTACHMENT_COLOR);
        materialAttachmentElements.add(scoreCircle2);

        final SvgCircle scoreCircle3 = new SvgCircle(2205L, 2205L, 46L);
        scoreCircle3.getElementAttributes().setFill(MATERIAL_ATTACHMENT_COLOR);
        materialAttachmentElements.add(scoreCircle3);

        //Title
        final SvgText title = new SvgText("MATERIAL ATTACHMENT", 67, FontWeight.BOLD, MATERIAL_ATTACHMENT_COLOR, 285L, 2365L);
        materialAttachmentElements.add(title);

        final SvgLine titleLine = new SvgLine(MATERIAL_ATTACHMENT_COLOR, DEFAULT_STROKE_WIDTH, 281L, 3016L, 1152L, 3016L);
        materialAttachmentElements.add(titleLine);

        return materialAttachmentElements;
    }

    private List<SvgElement> generateCommunication() {
        final List<SvgElement> communicationElements = new ArrayList<>();

        //Line
        final SvgPath path = new SvgPath(COMMUNICATION_COLOR, DEFAULT_STROKE_WIDTH, 2420L, 2002L, new Point(2420L, 2341L),
                new Point(2111L, 2640L), new Point(1240L, 2640L));
        communicationElements.add(path);

        //Circles
        final SvgCircle scoreCircle1 = new SvgCircle(2372L, 1954L, 46L);
        scoreCircle1.getElementAttributes().setFill(COMMUNICATION_COLOR);
        communicationElements.add(scoreCircle1);

        final SvgCircle scoreCircle2 = new SvgCircle(2372L, 2080L, 46L);
        scoreCircle2.getElementAttributes().setFill(COMMUNICATION_COLOR);
        communicationElements.add(scoreCircle2);

        final SvgCircle scoreCircle3 = new SvgCircle(2372L, 2205L, 46L);
        scoreCircle3.getElementAttributes().setFill(COMMUNICATION_COLOR);
        communicationElements.add(scoreCircle3);

        //Title
        final SvgText title = new SvgText("COMMUNICATION", 67, FontWeight.BOLD, COMMUNICATION_COLOR, 1243L, 2580L);
        communicationElements.add(title);

        final SvgLine titleLine = new SvgLine(COMMUNICATION_COLOR, DEFAULT_STROKE_WIDTH, 1240L, 3241L, 2111L, 3241L);
        communicationElements.add(titleLine);

        return communicationElements;
    }

    private List<SvgElement> generateSelfAware() {
        final List<SvgElement> selfAwareElements = new ArrayList<>();

        //Line
        final SvgPath path = new SvgPath(SELF_AWARE_COLOR, DEFAULT_STROKE_WIDTH, 2587L, 2002L, new Point(2587L, 2341L),
                new Point(2896L, 2640L), new Point(3767L, 2640L));
        selfAwareElements.add(path);

        //Circles
        final SvgCircle scoreCircle1 = new SvgCircle(2539L, 1954L, 46L);
        scoreCircle1.getElementAttributes().setFill(SELF_AWARE_COLOR);
        selfAwareElements.add(scoreCircle1);

        final SvgCircle scoreCircle2 = new SvgCircle(2539L, 2080L, 46L);
        scoreCircle2.getElementAttributes().setFill(SELF_AWARE_COLOR);
        selfAwareElements.add(scoreCircle2);

        final SvgCircle scoreCircle3 = new SvgCircle(2539L, 2205L, 46L);
        scoreCircle3.getElementAttributes().setFill(SELF_AWARE_COLOR);
        selfAwareElements.add(scoreCircle3);

        //Title
        final SvgText title = new SvgText("SELF AWARE", 67, FontWeight.BOLD, SELF_AWARE_COLOR, 2898L, 2580L);
        selfAwareElements.add(title);

        final SvgLine titleLine = new SvgLine(SELF_AWARE_COLOR, DEFAULT_STROKE_WIDTH, 2896L, 3245L, 3766L, 3245L);
        selfAwareElements.add(titleLine);

        return selfAwareElements;
    }

    private List<SvgElement> generateAnalysis() {
        final List<SvgElement> analysisElements = new ArrayList<>();

        //Line
        final SvgPath path = new SvgPath(ANALYSIS_COLOR, DEFAULT_STROKE_WIDTH, 2754L, 2002L, new Point(2754L, 2341L),
                new Point(2931L, 2430L), new Point(4725L, 2430L));
        analysisElements.add(path);

        //Circles
        final SvgCircle scoreCircle1 = new SvgCircle(2706L, 1954L, 46L);
        scoreCircle1.getElementAttributes().setFill(ANALYSIS_COLOR);
        analysisElements.add(scoreCircle1);

        final SvgCircle scoreCircle2 = new SvgCircle(2706L, 2080L, 46L);
        scoreCircle2.getElementAttributes().setFill(ANALYSIS_COLOR);
        analysisElements.add(scoreCircle2);

        final SvgCircle scoreCircle3 = new SvgCircle(2706L, 2205L, 46L);
        scoreCircle3.getElementAttributes().setFill(ANALYSIS_COLOR);
        analysisElements.add(scoreCircle3);

        //Title
        final SvgText title = new SvgText("ANALYSIS", 67, FontWeight.BOLD, ANALYSIS_COLOR, 3856L, 2365L);
        analysisElements.add(title);

        final SvgLine titleLine = new SvgLine(ANALYSIS_COLOR, DEFAULT_STROKE_WIDTH, 3855L, 3016L, 4725L, 3016L);
        analysisElements.add(titleLine);

        return analysisElements;
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
        secondBackground.getElementStroke().setStrokeWidth(DEFAULT_STROKE_WIDTH);
        secondBackground.setXRadius(70L);
        secondBackground.setYRadius(70L);
        svgTemplate.addElement(secondBackground);

        svgTemplate.addElements(generateHeader());
        svgTemplate.addElements(generateUniversal());
        svgTemplate.addElements(generateSociety());
        svgTemplate.addElements(generateVision());
        svgTemplate.addElements(generateStrength());

        svgTemplate.addElements(generateInspiration());
        svgTemplate.addElements(generateStructure());
        svgTemplate.addElements(generateAdaptability());
        svgTemplate.addElements(generateAction());

        svgTemplate.addElements(generateMaterialAttachment());
        svgTemplate.addElements(generateCommunication());
        svgTemplate.addElements(generateSelfAware());
        svgTemplate.addElements(generateAnalysis());


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
