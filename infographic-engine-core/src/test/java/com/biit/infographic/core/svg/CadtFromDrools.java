package com.biit.infographic.core.svg;

import com.biit.drools.form.DroolsSubmittedForm;
import com.biit.infographic.core.controllers.DroolsResultController;
import com.biit.infographic.core.models.svg.SvgAreaElement;
import com.biit.infographic.core.models.svg.SvgBackground;
import com.biit.infographic.core.models.svg.SvgTemplate;
import com.biit.infographic.core.models.svg.components.Point;
import com.biit.infographic.core.models.svg.components.StrokeLineCap;
import com.biit.infographic.core.models.svg.components.SvgCircle;
import com.biit.infographic.core.models.svg.components.SvgImage;
import com.biit.infographic.core.models.svg.components.SvgLine;
import com.biit.infographic.core.models.svg.components.SvgPath;
import com.biit.infographic.core.models.svg.components.SvgRectangle;
import com.biit.infographic.core.models.svg.components.gradient.SvgGradient;
import com.biit.infographic.core.models.svg.components.gradient.SvgGradientStop;
import com.biit.infographic.core.models.svg.components.text.FontWeight;
import com.biit.infographic.core.models.svg.components.text.SvgText;
import com.biit.infographic.core.models.svg.components.text.TextAlign;
import com.biit.utils.file.FileReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@SpringBootTest
@Test(groups = "cadt")
public class CadtFromDrools extends AbstractTestNGSpringContextTests {
    protected static final String OUTPUT_FOLDER = System.getProperty("java.io.tmpdir") + File.separator + "SvgTests";

    private static final String FONT_FAMILY = "Montserrat";
    private static final Double DEFAULT_STROKE_WIDTH = 8.33D;
    private static final Double ALPHA_SECONDARY = 0.5D;
    private static final Double ALPHA_PRIMARY = 0.75D;
    private static final Double FULL_ALPHA = 1.0D;
    private static final String BORDER_COLOR = "b49057";
    private static final String UNIVERSAL_TEXT_1 = "#DROOLS%CADT%UniversalCell1#";
    private static final String UNIVERSAL_TEXT_2 = "#DROOLS%CADT%UniversalCell2#";
    private static final String UNIVERSAL_TEXT_3 = "#DROOLS%CADT%UniversalCell3#";
    private static final String UNIVERSAL_COLOR_1 = "#DROOLS%CADT%UniversalCell1Color#";
    private static final String UNIVERSAL_COLOR_2 = "#DROOLS%CADT%UniversalCell2Color#";
    private static final String UNIVERSAL_COLOR_3 = "#DROOLS%CADT%UniversalCell3Color#";
    private static final String SOCIETY_TEXT_1 = "#DROOLS%CADT%SocietyCell1#";
    private static final String SOCIETY_TEXT_2 = "#DROOLS%CADT%SocietyCell2#";
    private static final String SOCIETY_TEXT_3 = "#DROOLS%CADT%SocietyCell3#";
    private static final String SOCIETY_COLOR_1 = "#DROOLS%CADT%SocietyCell1Color#";
    private static final String SOCIETY_COLOR_2 = "#DROOLS%CADT%SocietyCell2Color#";
    private static final String SOCIETY_COLOR_3 = "#DROOLS%CADT%SocietyCell3Color#";
    private static final String VISION_TEXT_1 = "#DROOLS%CADT%VisionCell1#";
    private static final String VISION_TEXT_2 = "#DROOLS%CADT%VisionCell2#";
    private static final String VISION_TEXT_3 = "#DROOLS%CADT%VisionCell3#";
    private static final String VISION_COLOR_1 = "#DROOLS%CADT%VisionCell1Color#";
    private static final String VISION_COLOR_2 = "#DROOLS%CADT%VisionCell2Color#";
    private static final String VISION_COLOR_3 = "#DROOLS%CADT%VisionCell3Color#";
    private static final String STRENGTH_TEXT_1 = "#DROOLS%CADT%StrengthCell1#";
    private static final String STRENGTH_TEXT_2 = "#DROOLS%CADT%StrengthCell2#";
    private static final String STRENGTH_TEXT_3 = "#DROOLS%CADT%StrengthCell3#";
    private static final String STRENGTH_COLOR_1 = "#DROOLS%CADT%StrengthCell1Color#";
    private static final String STRENGTH_COLOR_2 = "#DROOLS%CADT%StrengthCell2Color#";
    private static final String STRENGTH_COLOR_3 = "#DROOLS%CADT%StrengthCell3Color#";

    private static final String STRUCTURE_TEXT_1 = "#DROOLS%CADT%StructureCell1#";
    private static final String STRUCTURE_TEXT_2 = "#DROOLS%CADT%StructureCell2#";
    private static final String STRUCTURE_TEXT_3 = "#DROOLS%CADT%StructureCell3#";
    private static final String STRUCTURE_COLOR_1 = "#DROOLS%CADT%StructureCell1Color#";
    private static final String STRUCTURE_COLOR_2 = "#DROOLS%CADT%StructureCell2Color#";
    private static final String STRUCTURE_COLOR_3 = "#DROOLS%CADT%StructureCell3Color#";
    private static final String INSPIRATION_TEXT_1 = "#DROOLS%CADT%InspirationCell1#";
    private static final String INSPIRATION_TEXT_2 = "#DROOLS%CADT%InspirationCell2#";
    private static final String INSPIRATION_TEXT_3 = "#DROOLS%CADT%InspirationCell3#";
    private static final String INSPIRATION_COLOR_1 = "#DROOLS%CADT%InspirationCell1Color#";
    private static final String INSPIRATION_COLOR_2 = "#DROOLS%CADT%InspirationCell2Color#";
    private static final String INSPIRATION_COLOR_3 = "#DROOLS%CADT%InspirationCell3Color#";
    private static final String STRUCTURE_INSPIRATION_TEXT = "#DROOLS%CADT%StructureInspirationCell#";
    private static final String ADAPTABILITY_TEXT_1 = "#DROOLS%CADT%AdaptabilityCell1#";
    private static final String ADAPTABILITY_TEXT_2 = "#DROOLS%CADT%AdaptabilityCell2#";
    private static final String ADAPTABILITY_TEXT_3 = "#DROOLS%CADT%AdaptabilityCell3#";
    private static final String ADAPTABILITY_COLOR_1 = "#DROOLS%CADT%AdaptabilityCell1Color#";
    private static final String ADAPTABILITY_COLOR_2 = "#DROOLS%CADT%AdaptabilityCell2Color#";
    private static final String ADAPTABILITY_COLOR_3 = "#DROOLS%CADT%AdaptabilityCell3Color#";
    private static final String ACTION_TEXT_1 = "#DROOLS%CADT%ActionCell1#";
    private static final String ACTION_TEXT_2 = "#DROOLS%CADT%ActionCell2#";
    private static final String ACTION_TEXT_3 = "#DROOLS%CADT%ActionCell3#";
    private static final String ACTION_COLOR_1 = "#DROOLS%CADT%ActionCell1Color#";
    private static final String ACTION_COLOR_2 = "#DROOLS%CADT%ActionCell2Color#";
    private static final String ACTION_COLOR_3 = "#DROOLS%CADT%ActionCell3Color#";
    private static final String ADAPTABILITY_ACTION_TEXT = "#DROOLS%CADT%AdaptabilityActionCell#";

    private static final String MATERIAL_ATTACHMENT_TEXT_1 = "#DROOLS%CADT%MatterialAttachementCell1#";
    private static final String MATERIAL_ATTACHMENT_TEXT_2 = "#DROOLS%CADT%MatterialAttachementCell2#";
    private static final String MATERIAL_ATTACHMENT_TEXT_3 = "#DROOLS%CADT%MatterialAttachementCell3#";
    private static final String MATERIAL_ATTACHMENT_COLOR_1 = "#DROOLS%CADT%MatterialAttachementCell1Color#";
    private static final String MATERIAL_ATTACHMENT_COLOR_2 = "#DROOLS%CADT%MatterialAttachementCell2Color#";
    private static final String MATERIAL_ATTACHMENT_COLOR_3 = "#DROOLS%CADT%MatterialAttachementCell3Color#";
    private static final String COMMUNICATION_TEXT_1 = "#DROOLS%CADT%CommunicationCell1#";
    private static final String COMMUNICATION_TEXT_2 = "#DROOLS%CADT%CommunicationCell2#";
    private static final String COMMUNICATION_TEXT_3 = "#DROOLS%CADT%CommunicationCell3#";
    private static final String COMMUNICATION_COLOR_1 = "#DROOLS%CADT%CommunicationCell1Color#";
    private static final String COMMUNICATION_COLOR_2 = "#DROOLS%CADT%CommunicationCell2Color#";
    private static final String COMMUNICATION_COLOR_3 = "#DROOLS%CADT%CommunicationCell3Color#";
    private static final String SELF_AWARE_TEXT_1 = "#DROOLS%CADT%SelfAwareCell1#";
    private static final String SELF_AWARE_TEXT_2 = "#DROOLS%CADT%SelfAwareCell2#";
    private static final String SELF_AWARE_TEXT_3 = "#DROOLS%CADT%SelfAwareCell3#";
    private static final String SELF_AWARE_COLOR_1 = "#DROOLS%CADT%SelfAwareCell1Color#";
    private static final String SELF_AWARE_COLOR_2 = "#DROOLS%CADT%SelfAwareCell2Color#";
    private static final String SELF_AWARE_COLOR_3 = "#DROOLS%CADT%SelfAwareCell3Color#";
    private static final String ANALYSIS_TEXT_1 = "#DROOLS%CADT%AnalysisCell1#";
    private static final String ANALYSIS_TEXT_2 = "#DROOLS%CADT%AnalysisCell2#";
    private static final String ANALYSIS_TEXT_3 = "#DROOLS%CADT%AnalysisCell3#";
    private static final String ANALYSIS_COLOR_1 = "#DROOLS%CADT%AnalysisCell1Color#";
    private static final String ANALYSIS_COLOR_2 = "#DROOLS%CADT%AnalysisCell2Color#";
    private static final String ANALYSIS_COLOR_3 = "#DROOLS%CADT%AnalysisCell3Color#";

    private static final String UNIVERSAL_CIRCLE_COLOR_1 = "#DROOLS%CADT%UniversalCircle1Color#";
    private static final String UNIVERSAL_CIRCLE_COLOR_2 = "#DROOLS%CADT%UniversalCircle2Color#";
    private static final String UNIVERSAL_CIRCLE_COLOR_3 = "#DROOLS%CADT%UniversalCircle3Color#";
    private static final String SOCIETY_CIRCLE_COLOR_1 = "#DROOLS%CADT%SocietyCircle1Color#";
    private static final String SOCIETY_CIRCLE_COLOR_2 = "#DROOLS%CADT%SocietyCircle2Color#";
    private static final String SOCIETY_CIRCLE_COLOR_3 = "#DROOLS%CADT%SocietyCircle3Color#";
    private static final String VISION_CIRCLE_COLOR_1 = "#DROOLS%CADT%VisionCircle1Color#";
    private static final String VISION_CIRCLE_COLOR_2 = "#DROOLS%CADT%VisionCircle2Color#";
    private static final String VISION_CIRCLE_COLOR_3 = "#DROOLS%CADT%VisionCircle3Color#";
    private static final String STRENGTH_CIRCLE_COLOR_1 = "#DROOLS%CADT%StrengthCircle1Color#";
    private static final String STRENGTH_CIRCLE_COLOR_2 = "#DROOLS%CADT%StrengthCircle2Color#";
    private static final String STRENGTH_CIRCLE_COLOR_3 = "#DROOLS%CADT%StrengthCircle3Color#";

    private static final String STRUCTURE_CIRCLE_COLOR_1 = "#DROOLS%CADT%StructureCircle1Color#";
    private static final String STRUCTURE_CIRCLE_COLOR_2 = "#DROOLS%CADT%StructureCircle2Color#";
    private static final String INSPIRATION_CIRCLE_COLOR_1 = "#DROOLS%CADT%InspirationCircle1Color#";
    private static final String INSPIRATION_CIRCLE_COLOR_2 = "#DROOLS%CADT%InspirationCircle2Color#";
    private static final String ADAPTABILITY_CIRCLE_COLOR_1 = "#DROOLS%CADT%AdaptabilityCircle1Color#";
    private static final String ADAPTABILITY_CIRCLE_COLOR_2 = "#DROOLS%CADT%AdaptabilityCircle2Color#";
    private static final String ACTION_CIRCLE_COLOR_1 = "#DROOLS%CADT%ActionCircle1Color#";
    private static final String ACTION_CIRCLE_COLOR_2 = "#DROOLS%CADT%ActionCircle2Color#";

    private static final String MATERIAL_ATTACHMENT_CIRCLE_COLOR_1 = "#DROOLS%CADT%MaterialAttachmentCircle1Color#";
    private static final String MATERIAL_ATTACHMENT_CIRCLE_COLOR_2 = "#DROOLS%CADT%MaterialAttachmentCircle2Color#";
    private static final String MATERIAL_ATTACHMENT_CIRCLE_COLOR_3 = "#DROOLS%CADT%MaterialAttachmentCircle3Color#";
    private static final String COMMUNICATION_CIRCLE_COLOR_1 = "#DROOLS%CADT%CommunicationCircle1Color#";
    private static final String COMMUNICATION_CIRCLE_COLOR_2 = "#DROOLS%CADT%CommunicationCircle2Color#";
    private static final String COMMUNICATION_CIRCLE_COLOR_3 = "#DROOLS%CADT%CommunicationCircle3Color#";
    private static final String SELF_AWARE_CIRCLE_COLOR_1 = "#DROOLS%CADT%SelfAwareCircle1Color#";
    private static final String SELF_AWARE_CIRCLE_COLOR_2 = "#DROOLS%CADT%SelfAwareCircle2Color#";
    private static final String SELF_AWARE_CIRCLE_COLOR_3 = "#DROOLS%CADT%SelfAwareCircle3Color#";
    private static final String ANALYSIS_CIRCLE_COLOR_1 = "#DROOLS%CADT%AnalysisCircle1Color#";
    private static final String ANALYSIS_CIRCLE_COLOR_2 = "#DROOLS%CADT%AnalysisCircle2Color#";
    private static final String ANALYSIS_CIRCLE_COLOR_3 = "#DROOLS%CADT%AnalysisCircle3Color#";

    private static final int TEXT_PADDING = 25;
    private static final int FONT_SIZE = 42;

    private final static String DROOLS_FORM_FILE_PATH = "drools/DroolsSubmittedCadt.json";

    @Autowired
    private DroolsResultController droolsResultController;

    private SvgTemplate cadtTemplate;

    protected boolean deleteDirectory(File directoryToBeDeleted) {
        File[] allContents = directoryToBeDeleted.listFiles();
        if (allContents != null) {
            for (File file : allContents) {
                deleteDirectory(file);
            }
        }
        return directoryToBeDeleted.delete();
    }

    private void checkContent(String content, String resourceFile) {
        try {
            Assert.assertEquals(content.trim(), new String(Files.readAllBytes(Paths.get(getClass().getClassLoader()
                    .getResource("svg" + File.separator + resourceFile).toURI()))).trim());
        } catch (IOException | URISyntaxException e) {
            Assert.fail();
        }
    }

    private SvgText generateText(String variable, long x, long y, int width, int height) {
        final SvgText text = new SvgText(FONT_FAMILY, variable, FONT_SIZE, x + TEXT_PADDING, y + TEXT_PADDING);
        text.setMaxParagraphHeight(height - TEXT_PADDING * 2);
        text.setMaxLineWidth(width - TEXT_PADDING * 2);
        text.setTextAlign(TextAlign.JUSTIFY);
        return text;
    }

    private SvgBackground generateBackground() {
        final SvgBackground svgBackground = new SvgBackground();
        svgBackground.setBackgroundColor("#e1dbd6");
        svgBackground.setXRadius(104L);
        svgBackground.setYRadius(104L);
        return svgBackground;
    }


    private List<SvgAreaElement> generateHeader() {
        final List<SvgAreaElement> headerElements = new ArrayList<>();


        //Name background
        final SvgRectangle nameRectangle = new SvgRectangle(1676L, 81L, "1653px", "225px", "ffffff");
        nameRectangle.getElementStroke().setStrokeColor(BORDER_COLOR);
        nameRectangle.getElementStroke().setStrokeWidth(7.07);
        nameRectangle.setXRadius(60L);
        nameRectangle.setYRadius(60L);
        headerElements.add(nameRectangle);

        final SvgText name = new SvgText("CADT Results", 100, 2160L, 165L);
        name.setFontFamily("Arial-BoldMT, Arial, sans-serif");
        name.setFontWeight(FontWeight.BOLD);
        headerElements.add(name);

//        final SvgText position = new SvgText("CADT Results", 83, 2248L, 215L);
//        position.setFontFamily("Arial-BoldMT, Arial, sans-serif");
//        headerElements.add(position);

        //Logo
        final SvgImage logo = new SvgImage();
        logo.setFromResource("images/NHM-Logo.png");
        logo.getElementAttributes().setXCoordinate(1272L);
        logo.getElementAttributes().setYCoordinate(67L);
        logo.getElementAttributes().setWidth(282L);
        logo.getElementAttributes().setHeight(254L);
        headerElements.add(logo);

        return headerElements;
    }

    private List<SvgAreaElement> generateScore() {
        final List<SvgAreaElement> scoreElements = new ArrayList<>();
        //Score circle
        final SvgCircle scoreCircle = new SvgCircle(3540L, 80L, 113L);
        scoreCircle.getElementAttributes().setFill("ffffff");
        scoreCircle.getElementStroke().setStrokeWidth(DEFAULT_STROKE_WIDTH);
        scoreCircle.getElementStroke().setStrokeColor(BORDER_COLOR);
        scoreElements.add(scoreCircle);


        final SvgText score = new SvgText("90", 80, 3610L, 129L);
        score.setFontFamily("Arial-BoldMT, Arial, sans-serif");
        score.setFontWeight(FontWeight.BOLD);
        scoreElements.add(score);

        final SvgLine scoreSeparator = new SvgLine(3622L, 192L, 3680L, 192L);
        scoreSeparator.getElementStroke().setStrokeWidth(4D);
        scoreSeparator.getElementStroke().setLineCap(StrokeLineCap.ROUND);
        scoreElements.add(scoreSeparator);

        final SvgText scoreTotal = new SvgText("100", 80, 3588L, 205L);
        scoreTotal.setFontFamily("Arial-BoldMT, Arial, sans-serif");
        scoreTotal.setFontWeight(FontWeight.BOLD);
        scoreElements.add(scoreTotal);

        return scoreElements;
    }

    private List<SvgAreaElement> generateUniversal() {
        final List<SvgAreaElement> universalElements = new ArrayList<>();

        //Line
        final SvgPath path = new SvgPath(UNIVERSAL_CIRCLE_COLOR_1, DEFAULT_STROKE_WIDTH, 2252L, 1542L, new Point(2252L, 1292L),
                new Point(2218L, 1250L), new Point(281L, 1250L));
        universalElements.add(path);

        //Circles
        final SvgCircle scoreCircle1 = new SvgCircle(2205L, 1244L, 46L);
        scoreCircle1.getElementAttributes().setFill(UNIVERSAL_CIRCLE_COLOR_1);
        universalElements.add(scoreCircle1);

        final SvgCircle scoreCircle2 = new SvgCircle(2205L, 1370L, 46L);
        scoreCircle2.getElementAttributes().setFill(UNIVERSAL_CIRCLE_COLOR_2);
        universalElements.add(scoreCircle2);

        final SvgCircle scoreCircle3 = new SvgCircle(2205L, 1495L, 46L);
        scoreCircle3.getElementAttributes().setFill(UNIVERSAL_CIRCLE_COLOR_3);
        universalElements.add(scoreCircle3);

        //Title
        final SvgText title = new SvgText("UNIVERSAL", 67, FontWeight.BOLD, UNIVERSAL_CIRCLE_COLOR_1, 285L, 593L);
        universalElements.add(title);

        final SvgLine titleLine = new SvgLine(UNIVERSAL_CIRCLE_COLOR_1, DEFAULT_STROKE_WIDTH, 281L, 655L, 1152L, 655L);
        universalElements.add(titleLine);

        //Content
        final SvgRectangle content1 = new SvgRectangle(281L, 659L, 871L, 190L, UNIVERSAL_COLOR_1, ALPHA_SECONDARY);
        universalElements.add(content1);
        universalElements.add(generateText(UNIVERSAL_TEXT_1, 281L, 659L, 871, 190));

        final SvgRectangle content2 = new SvgRectangle(281L, 857L, 871L, 190L, UNIVERSAL_COLOR_2, ALPHA_PRIMARY);
        universalElements.add(content2);
        universalElements.add(generateText(UNIVERSAL_TEXT_2, 281L, 857L, 871, 190));

        final SvgRectangle content3 = new SvgRectangle(281L, 1055L, 871L, 190L, UNIVERSAL_COLOR_3, ALPHA_SECONDARY);
        universalElements.add(content3);
        universalElements.add(generateText(UNIVERSAL_TEXT_3, 281L, 1055L, 871, 190));

        return universalElements;
    }

    private List<SvgAreaElement> generateSociety() {
        final List<SvgAreaElement> societyElements = new ArrayList<>();

        //Line
        final SvgPath path = new SvgPath(SOCIETY_CIRCLE_COLOR_1, DEFAULT_STROKE_WIDTH, 2420L, 1542L, new Point(2420L, 1292L),
                new Point(2177L, 1048L), new Point(1240L, 1048L));
        societyElements.add(path);

        //Circles
        final SvgCircle scoreCircle1 = new SvgCircle(2372L, 1244L, 46L);
        scoreCircle1.getElementAttributes().setFill(SOCIETY_CIRCLE_COLOR_1);
        societyElements.add(scoreCircle1);

        final SvgCircle scoreCircle2 = new SvgCircle(2372L, 1370L, 46L);
        scoreCircle2.getElementAttributes().setFill(SOCIETY_CIRCLE_COLOR_2);
        societyElements.add(scoreCircle2);

        final SvgCircle scoreCircle3 = new SvgCircle(2372L, 1495L, 46L);
        scoreCircle3.getElementAttributes().setFill(SOCIETY_CIRCLE_COLOR_3);
        societyElements.add(scoreCircle3);

        //Title
        final SvgText title = new SvgText("SOCIETY", 67, FontWeight.BOLD, SOCIETY_CIRCLE_COLOR_1, 1242L, 391L);
        societyElements.add(title);

        final SvgLine titleLine = new SvgLine(SOCIETY_CIRCLE_COLOR_1, DEFAULT_STROKE_WIDTH, 1240L, 453L, 2111L, 453L);
        societyElements.add(titleLine);

        //Content
        final SvgRectangle content1 = new SvgRectangle(1240L, 457L, 871L, 190L, SOCIETY_COLOR_1, ALPHA_SECONDARY);
        societyElements.add(content1);
        societyElements.add(generateText(SOCIETY_TEXT_1, 1240L, 457L, 871, 190));

        final SvgRectangle content2 = new SvgRectangle(1240L, 655L, 871L, 190L, SOCIETY_COLOR_2, ALPHA_PRIMARY);
        societyElements.add(content2);
        societyElements.add(generateText(SOCIETY_TEXT_2, 1240L, 655L, 871, 190));

        final SvgRectangle content3 = new SvgRectangle(1240L, 852L, 871L, 190L, SOCIETY_COLOR_3, ALPHA_SECONDARY);
        societyElements.add(content3);
        societyElements.add(generateText(SOCIETY_TEXT_3, 1240L, 852L, 871, 190));

        return societyElements;
    }

    private List<SvgAreaElement> generateVision() {
        final List<SvgAreaElement> visionElements = new ArrayList<>();

        //Line
        final SvgPath path = new SvgPath(VISION_CIRCLE_COLOR_1, DEFAULT_STROKE_WIDTH, 2587L, 1542L, new Point(2587L, 1292L),
                new Point(2829L, 1048L), new Point(3767L, 1048L));
        visionElements.add(path);

        //Circles
        final SvgCircle scoreCircle1 = new SvgCircle(2539L, 1244L, 46L);
        scoreCircle1.getElementAttributes().setFill(VISION_CIRCLE_COLOR_1);
        visionElements.add(scoreCircle1);

        final SvgCircle scoreCircle2 = new SvgCircle(2539L, 1370L, 46L);
        scoreCircle2.getElementAttributes().setFill(VISION_CIRCLE_COLOR_2);
        visionElements.add(scoreCircle2);

        final SvgCircle scoreCircle3 = new SvgCircle(2539L, 1495L, 46L);
        scoreCircle3.getElementAttributes().setFill(VISION_CIRCLE_COLOR_3);
        visionElements.add(scoreCircle3);

        //Title
        final SvgText title = new SvgText("VISION", 67, FontWeight.BOLD, VISION_CIRCLE_COLOR_1, 2896L, 391L);
        visionElements.add(title);

        final SvgLine titleLine = new SvgLine(VISION_CIRCLE_COLOR_1, DEFAULT_STROKE_WIDTH, 2896L, 453L, 3766L, 453L);
        visionElements.add(titleLine);

        //Content
        final SvgRectangle content1 = new SvgRectangle(2896L, 457L, 871L, 190L, VISION_COLOR_1, ALPHA_SECONDARY);
        visionElements.add(content1);
        visionElements.add(generateText(VISION_TEXT_1, 2896L, 457L, 871, 190));

        final SvgRectangle content2 = new SvgRectangle(2896L, 655L, 871L, 190L, VISION_COLOR_2, ALPHA_PRIMARY);
        visionElements.add(content2);
        visionElements.add(generateText(VISION_TEXT_2, 2896L, 655L, 871, 190));

        final SvgRectangle content3 = new SvgRectangle(2896L, 852L, 871L, 190L, VISION_COLOR_3, ALPHA_SECONDARY);
        visionElements.add(content3);
        visionElements.add(generateText(VISION_TEXT_3, 2896L, 852L, 871, 190));

        return visionElements;
    }

    private List<SvgAreaElement> generateStrength() {
        final List<SvgAreaElement> strengthElements = new ArrayList<>();

        //Line
        final SvgPath path = new SvgPath(STRENGTH_CIRCLE_COLOR_1, DEFAULT_STROKE_WIDTH, 2754L, 1542L, new Point(2754L, 1292L),
                new Point(2795L, 1250L), new Point(4725L, 1250L));
        strengthElements.add(path);

        //Circles
        final SvgCircle scoreCircle1 = new SvgCircle(2706L, 1244L, 46L);
        scoreCircle1.getElementAttributes().setFill(STRENGTH_CIRCLE_COLOR_1);
        strengthElements.add(scoreCircle1);

        final SvgCircle scoreCircle2 = new SvgCircle(2706L, 1370L, 46L);
        scoreCircle2.getElementAttributes().setFill(STRENGTH_CIRCLE_COLOR_2);
        strengthElements.add(scoreCircle2);

        final SvgCircle scoreCircle3 = new SvgCircle(2706L, 1495L, 46L);
        scoreCircle3.getElementAttributes().setFill(STRENGTH_CIRCLE_COLOR_3);
        strengthElements.add(scoreCircle3);

        //Title
        final SvgText title = new SvgText("STRENGTH", 67, FontWeight.BOLD, STRENGTH_CIRCLE_COLOR_1, 3856L, 593L);
        strengthElements.add(title);

        final SvgLine titleLine = new SvgLine(STRENGTH_CIRCLE_COLOR_1, DEFAULT_STROKE_WIDTH, 3855L, 653L, 4725L, 653L);
        strengthElements.add(titleLine);

        //Content
        final SvgRectangle content1 = new SvgRectangle(3855L, 659L, 871L, 190L, STRENGTH_COLOR_1, ALPHA_SECONDARY);
        strengthElements.add(content1);
        strengthElements.add(generateText(STRENGTH_TEXT_1, 3855L, 659L, 871, 190));

        final SvgRectangle content2 = new SvgRectangle(3855L, 857L, 871L, 190L, STRENGTH_COLOR_2, ALPHA_PRIMARY);
        strengthElements.add(content2);
        strengthElements.add(generateText(STRENGTH_TEXT_2, 3855L, 857L, 871, 190));

        final SvgRectangle content3 = new SvgRectangle(3855L, 1055L, 871L, 190L, STRENGTH_COLOR_3, ALPHA_SECONDARY);
        strengthElements.add(content3);
        strengthElements.add(generateText(STRENGTH_TEXT_3, 3855L, 1055L, 871, 190));

        return strengthElements;
    }

    private List<SvgAreaElement> generateStructure() {
        final List<SvgAreaElement> structureElements = new ArrayList<>();

        //Line
        final SvgLine line1 = new SvgLine(STRUCTURE_CIRCLE_COLOR_1, DEFAULT_STROKE_WIDTH, 2253L, 1694L, 2253L, 1851L);
        structureElements.add(line1);

//        final SvgLine line2 = new SvgLine(STRUCTURE_COLOR, DEFAULT_STROKE_WIDTH, 2044L, 1772L, 2253L, 1772L);
//        structureElements.add(line2);

        //Circles
        final SvgCircle scoreCircle1 = new SvgCircle(2205L, 1662L, 46L);
        scoreCircle1.getElementAttributes().setFill(STRUCTURE_CIRCLE_COLOR_1);
        structureElements.add(scoreCircle1);

        final SvgCircle scoreCircle2 = new SvgCircle(2205L, 1788L, 46L);
        scoreCircle2.getElementAttributes().setFill(STRUCTURE_CIRCLE_COLOR_2);
        structureElements.add(scoreCircle2);

        //Title
        final SvgText title = new SvgText("STRUCTURE", 67, FontWeight.BOLD, STRUCTURE_CIRCLE_COLOR_1, 680L, 1421L);
        structureElements.add(title);

        final SvgLine titleLine = new SvgLine(STRUCTURE_CIRCLE_COLOR_1, DEFAULT_STROKE_WIDTH, 281L, 1483L, 1152L, 1483L);
        structureElements.add(titleLine);

//        final SvgLine titleLineButton = new SvgLine(STRUCTURE_COLOR, DEFAULT_STROKE_WIDTH, 281L, 2187L, 1152L, 2187L);
//        structureElements.add(titleLineButton);

        //Content
        final SvgRectangle content1 = new SvgRectangle(281L, 1487L, 871L, 190L, STRUCTURE_COLOR_1, ALPHA_SECONDARY);
        structureElements.add(content1);
        structureElements.add(generateText(STRUCTURE_TEXT_1, 281L, 1487L, 871, 190));

        final SvgRectangle content2 = new SvgRectangle(281L, 1685L, 871L, 190L, STRUCTURE_COLOR_2, ALPHA_PRIMARY);
        structureElements.add(content2);
        structureElements.add(generateText(STRUCTURE_TEXT_2, 281L, 1685L, 871, 190));

        final SvgRectangle content3 = new SvgRectangle(281L, 1883L, 871L, 190L, STRUCTURE_COLOR_3, ALPHA_SECONDARY);
        structureElements.add(content3);
        structureElements.add(generateText(STRUCTURE_TEXT_3, 281L, 1883L, 871, 190));

        return structureElements;
    }

    private List<SvgAreaElement> generateInspiration() {
        final List<SvgAreaElement> inspirationElements = new ArrayList<>();

        //Line
        final SvgLine line1 = new SvgLine(INSPIRATION_CIRCLE_COLOR_1, DEFAULT_STROKE_WIDTH, 2420L, 1694L, 2420L, 1851L);
        inspirationElements.add(line1);

//        final SvgLine line2 = new SvgLine(INSPIRATION_COLOR, DEFAULT_STROKE_WIDTH, 2044L, 1772L, 2420L, 1772L);
//        inspirationElements.add(line2);

        //Circles
        final SvgCircle scoreCircle1 = new SvgCircle(2372L, 1662L, 46L);
        scoreCircle1.getElementAttributes().setFill(INSPIRATION_CIRCLE_COLOR_1);
        inspirationElements.add(scoreCircle1);

        final SvgCircle scoreCircle2 = new SvgCircle(2372L, 1788L, 46L);
        scoreCircle2.getElementAttributes().setFill(INSPIRATION_CIRCLE_COLOR_2);
        inspirationElements.add(scoreCircle2);

        //Title
        final SvgText title = new SvgText("INSPIRATION", 67, FontWeight.BOLD, INSPIRATION_CIRCLE_COLOR_1, 1191L, 1421L);
        inspirationElements.add(title);

        final SvgLine titleLine = new SvgLine(INSPIRATION_CIRCLE_COLOR_1, DEFAULT_STROKE_WIDTH, 1162L, 1483L, 2033L, 1483L);
        inspirationElements.add(titleLine);

//        final SvgLine titleLineBottom = new SvgLine(INSPIRATION_COLOR, DEFAULT_STROKE_WIDTH, 1162L, 2187L, 2033L, 2187L);
//        inspirationElements.add(titleLineBottom);

        //Content
        final SvgRectangle content1 = new SvgRectangle(1162L, 1487L, 871L, 190L, INSPIRATION_COLOR_1, ALPHA_SECONDARY);
        inspirationElements.add(content1);
        inspirationElements.add(generateText(INSPIRATION_TEXT_1, 1162L, 1487L, 871, 190));

        final SvgRectangle content2 = new SvgRectangle(1162L, 1685L, 871L, 190L, INSPIRATION_COLOR_2, ALPHA_PRIMARY);
        inspirationElements.add(content2);
        inspirationElements.add(generateText(INSPIRATION_TEXT_2, 1162L, 1685L, 871, 190));

        final SvgRectangle content3 = new SvgRectangle(1162L, 1883L, 871L, 190L, INSPIRATION_COLOR_3, ALPHA_SECONDARY);
        inspirationElements.add(content3);
        inspirationElements.add(generateText(INSPIRATION_TEXT_3, 1162L, 1883L, 871, 190));

        return inspirationElements;
    }

    private List<SvgAreaElement> generateStructureInspiration() {
        final List<SvgAreaElement> structureInspirationElements = new ArrayList<>();

        //Title
        final SvgText title = new SvgText("-", 67, FontWeight.BOLD, "272727", 1146L, 1416L);
        structureInspirationElements.add(title);

        //Line
        final SvgLine line1 = new SvgLine(null, DEFAULT_STROKE_WIDTH, 2044L, 1772L, 2420L, 1772L);
        line1.getElementAttributes().setGradient(new SvgGradient(
                new SvgGradientStop(INSPIRATION_COLOR_1, FULL_ALPHA, 0.0),
                new SvgGradientStop(STRUCTURE_COLOR_1, FULL_ALPHA, 0.5),
                new SvgGradientStop(INSPIRATION_COLOR_2, FULL_ALPHA, 1.0)));
        structureInspirationElements.add(line1);

        //Content
        final SvgRectangle content1 = new SvgRectangle(281L, 2083L, 1750L, 100L, null);
        content1.getElementAttributes().setGradient(new SvgGradient(new SvgGradientStop(STRUCTURE_COLOR_1, ALPHA_SECONDARY, 0.0), new SvgGradientStop(INSPIRATION_COLOR_1, ALPHA_SECONDARY, 1.0)));
        structureInspirationElements.add(content1);
        structureInspirationElements.add(generateText(STRUCTURE_INSPIRATION_TEXT, 281L, 2083L, 1750, 100));

        final SvgLine titleLineButton = new SvgLine(null, DEFAULT_STROKE_WIDTH, 281L, 2187L, 2033L, 2187L);
        titleLineButton.getElementAttributes().setGradient(new SvgGradient(new SvgGradientStop(STRUCTURE_CIRCLE_COLOR_1, FULL_ALPHA, 0.0), new SvgGradientStop(INSPIRATION_CIRCLE_COLOR_1, FULL_ALPHA, 1.0)));
        structureInspirationElements.add(titleLineButton);

        return structureInspirationElements;
    }

    private List<SvgAreaElement> generateAdaptability() {
        final List<SvgAreaElement> adaptabilityElements = new ArrayList<>();

        //Line
        final SvgLine line1 = new SvgLine(ADAPTABILITY_CIRCLE_COLOR_1, DEFAULT_STROKE_WIDTH, 2587L, 1694L, 2587L, 1851L);
        adaptabilityElements.add(line1);

//        final SvgLine line2 = new SvgLine(ADAPTABILITY_COLOR, DEFAULT_STROKE_WIDTH, 2587L, 1772L, 2954L, 1772L);
//        adaptabilityElements.add(line2);

        //Circles
        final SvgCircle scoreCircle1 = new SvgCircle(2539L, 1662L, 46L);
        scoreCircle1.getElementAttributes().setFill(ADAPTABILITY_CIRCLE_COLOR_1);
        adaptabilityElements.add(scoreCircle1);

        final SvgCircle scoreCircle2 = new SvgCircle(2539L, 1788L, 46L);
        scoreCircle2.getElementAttributes().setFill(ADAPTABILITY_CIRCLE_COLOR_2);
        adaptabilityElements.add(scoreCircle2);

        //Title
        final SvgText title = new SvgText("ADAPTABILITY", 67, FontWeight.BOLD, ADAPTABILITY_COLOR_1, 3285L, 1421L);
        adaptabilityElements.add(title);

        final SvgLine titleLine = new SvgLine(ADAPTABILITY_COLOR_1, DEFAULT_STROKE_WIDTH, 2974L, 1483L, 3845L, 1483L);
        adaptabilityElements.add(titleLine);

//        final SvgLine titleLineBottom = new SvgLine(ADAPTABILITY_COLOR, DEFAULT_STROKE_WIDTH, 2974L, 2187L, 3845L, 2187L);
//        adaptabilityElements.add(titleLineBottom);

        //Content
        final SvgRectangle content1 = new SvgRectangle(2974L, 1487L, 871L, 190L, ADAPTABILITY_COLOR_1, ALPHA_SECONDARY);
        adaptabilityElements.add(content1);
        adaptabilityElements.add(generateText(ADAPTABILITY_TEXT_1, 2974L, 1487L, 871, 190));

        final SvgRectangle content2 = new SvgRectangle(2974L, 1685L, 871L, 190L, ADAPTABILITY_COLOR_2, ALPHA_PRIMARY);
        adaptabilityElements.add(content2);
        adaptabilityElements.add(generateText(ADAPTABILITY_TEXT_2, 2974L, 1685L, 871, 190));

        final SvgRectangle content3 = new SvgRectangle(2974L, 1883L, 871L, 190L, ADAPTABILITY_COLOR_3, ALPHA_SECONDARY);
        adaptabilityElements.add(content3);
        adaptabilityElements.add(generateText(ADAPTABILITY_TEXT_3, 2974L, 1883L, 871, 190));

        return adaptabilityElements;
    }

    private List<SvgAreaElement> generateAction() {
        final List<SvgAreaElement> actionElements = new ArrayList<>();

        //Line
        final SvgLine line1 = new SvgLine(ACTION_CIRCLE_COLOR_1, DEFAULT_STROKE_WIDTH, 2753L, 1694L, 2753L, 1851L);
        actionElements.add(line1);

//        final SvgLine line2 = new SvgLine(ACTION_COLOR, DEFAULT_STROKE_WIDTH, 2754L, 1772L, 2954L, 1772L);
//        actionElements.add(line2);

        //Circles
        final SvgCircle scoreCircle1 = new SvgCircle(2706L, 1662L, 46L);
        scoreCircle1.getElementAttributes().setFill(ACTION_CIRCLE_COLOR_1);
        actionElements.add(scoreCircle1);

        final SvgCircle scoreCircle2 = new SvgCircle(2706L, 1788L, 46L);
        scoreCircle2.getElementAttributes().setFill(ACTION_CIRCLE_COLOR_2);
        actionElements.add(scoreCircle2);

        //Title
        final SvgText title = new SvgText("ACTION", 67, FontWeight.BOLD, ACTION_COLOR_1, 3877L, 1421L);
        actionElements.add(title);

        final SvgLine titleLine = new SvgLine(ACTION_COLOR_1, DEFAULT_STROKE_WIDTH, 3854L, 1483L, 4725L, 1483L);
        actionElements.add(titleLine);

//        final SvgLine titleLineButton = new SvgLine(ACTION_COLOR, DEFAULT_STROKE_WIDTH, 3854L, 2187L, 4725L, 2187L);
//        actionElements.add(titleLineButton);

        //Content
        final SvgRectangle content1 = new SvgRectangle(3854L, 1487L, 871L, 190L, ACTION_COLOR_1, ALPHA_SECONDARY);
        actionElements.add(content1);
        actionElements.add(generateText(ACTION_TEXT_1, 3854L, 1487L, 871, 190));

        final SvgRectangle content2 = new SvgRectangle(3854L, 1685L, 871L, 190L, ACTION_COLOR_2, ALPHA_PRIMARY);
        actionElements.add(content2);
        actionElements.add(generateText(ACTION_TEXT_2, 3854L, 1685L, 871, 190));

        final SvgRectangle content3 = new SvgRectangle(3854L, 1883L, 871L, 190L, ACTION_COLOR_3, ALPHA_SECONDARY);
        actionElements.add(content3);
        actionElements.add(generateText(ACTION_TEXT_3, 3854L, 1883L, 871, 190));

        return actionElements;
    }

    private List<SvgAreaElement> generateAdaptabilityAction() {
        final List<SvgAreaElement> adaptabilityActionElements = new ArrayList<>();
        //Title
        final SvgText title = new SvgText("-", 67, FontWeight.BOLD, "272727", 3839L, 1416L);
        adaptabilityActionElements.add(title);

        //Line
        final SvgLine line1 = new SvgLine(null, DEFAULT_STROKE_WIDTH, 2587L, 1772L, 2954L, 1772L);
        line1.getElementAttributes().setGradient(new SvgGradient(
                new SvgGradientStop(ADAPTABILITY_CIRCLE_COLOR_1, FULL_ALPHA, 0.0),
                new SvgGradientStop(ACTION_CIRCLE_COLOR_1, FULL_ALPHA, 0.5),
                new SvgGradientStop(ADAPTABILITY_CIRCLE_COLOR_2, FULL_ALPHA, 1.0)));
        adaptabilityActionElements.add(line1);

        //Content
        final SvgRectangle content1 = new SvgRectangle(2974L, 2083L, 1750L, 100L, null);
        content1.getElementAttributes().setGradient(new SvgGradient(new SvgGradientStop(ADAPTABILITY_COLOR_1, ALPHA_SECONDARY, 0.0), new SvgGradientStop(ACTION_COLOR_1, ALPHA_SECONDARY, 1.0)));
        adaptabilityActionElements.add(content1);
        adaptabilityActionElements.add(generateText(ADAPTABILITY_ACTION_TEXT, 2974L, 2083L, 1750, 100));

        final SvgLine titleLineButton = new SvgLine(null, DEFAULT_STROKE_WIDTH, 2974L, 2187L, 4725L, 2187L);
        titleLineButton.getElementAttributes().setGradient(new SvgGradient(new SvgGradientStop(ADAPTABILITY_COLOR_1, FULL_ALPHA, 0.0), new SvgGradientStop(ACTION_COLOR_1, FULL_ALPHA, 1.0)));
        adaptabilityActionElements.add(titleLineButton);

        return adaptabilityActionElements;
    }

    private List<SvgAreaElement> generateMaterialAttachment() {
        final List<SvgAreaElement> materialAttachmentElements = new ArrayList<>();

        //Line
        final SvgPath path = new SvgPath(MATERIAL_ATTACHMENT_CIRCLE_COLOR_1, DEFAULT_STROKE_WIDTH, 2252L, 2002L, new Point(2252L, 2252L),
                new Point(2075L, 2430L), new Point(281L, 2430L));
        materialAttachmentElements.add(path);

        //Circles
        final SvgCircle scoreCircle1 = new SvgCircle(2205L, 1954L, 46L);
        scoreCircle1.getElementAttributes().setFill(MATERIAL_ATTACHMENT_CIRCLE_COLOR_1);
        materialAttachmentElements.add(scoreCircle1);

        final SvgCircle scoreCircle2 = new SvgCircle(2205L, 2080L, 46L);
        scoreCircle2.getElementAttributes().setFill(MATERIAL_ATTACHMENT_CIRCLE_COLOR_2);
        materialAttachmentElements.add(scoreCircle2);

        final SvgCircle scoreCircle3 = new SvgCircle(2205L, 2205L, 46L);
        scoreCircle3.getElementAttributes().setFill(MATERIAL_ATTACHMENT_CIRCLE_COLOR_3);
        materialAttachmentElements.add(scoreCircle3);

        //Title
        final SvgText title = new SvgText("MATERIAL ATTACHMENT", 67, FontWeight.BOLD, MATERIAL_ATTACHMENT_CIRCLE_COLOR_1, 285L, 2365L);
        materialAttachmentElements.add(title);

        final SvgLine titleLine = new SvgLine(MATERIAL_ATTACHMENT_CIRCLE_COLOR_1, DEFAULT_STROKE_WIDTH, 281L, 3016L, 1152L, 3016L);
        materialAttachmentElements.add(titleLine);

        //Content
        final SvgRectangle content1 = new SvgRectangle(281L, 2434L, 871L, 190L, MATERIAL_ATTACHMENT_COLOR_1, ALPHA_SECONDARY);
        materialAttachmentElements.add(content1);
        materialAttachmentElements.add(generateText(MATERIAL_ATTACHMENT_TEXT_1, 281L, 2434L, 871, 190));

        final SvgRectangle content2 = new SvgRectangle(281L, 2632L, 871L, 190L, MATERIAL_ATTACHMENT_COLOR_2, ALPHA_PRIMARY);
        materialAttachmentElements.add(content2);
        materialAttachmentElements.add(generateText(MATERIAL_ATTACHMENT_TEXT_2, 281L, 2632L, 871, 190));

        final SvgRectangle content3 = new SvgRectangle(281L, 2830L, 871L, 190L, MATERIAL_ATTACHMENT_COLOR_3, ALPHA_SECONDARY);
        materialAttachmentElements.add(content3);
        materialAttachmentElements.add(generateText(MATERIAL_ATTACHMENT_TEXT_3, 281L, 2830L, 871, 190));

        return materialAttachmentElements;
    }

    private List<SvgAreaElement> generateCommunication() {
        final List<SvgAreaElement> communicationElements = new ArrayList<>();

        //Line
        final SvgPath path = new SvgPath(COMMUNICATION_CIRCLE_COLOR_1, DEFAULT_STROKE_WIDTH, 2420L, 2002L, new Point(2420L, 2341L),
                new Point(2111L, 2640L), new Point(1240L, 2640L));
        communicationElements.add(path);

        //Circles
        final SvgCircle scoreCircle1 = new SvgCircle(2372L, 1954L, 46L);
        scoreCircle1.getElementAttributes().setFill(COMMUNICATION_CIRCLE_COLOR_1);
        communicationElements.add(scoreCircle1);

        final SvgCircle scoreCircle2 = new SvgCircle(2372L, 2080L, 46L);
        scoreCircle2.getElementAttributes().setFill(COMMUNICATION_CIRCLE_COLOR_2);
        communicationElements.add(scoreCircle2);

        final SvgCircle scoreCircle3 = new SvgCircle(2372L, 2205L, 46L);
        scoreCircle3.getElementAttributes().setFill(COMMUNICATION_CIRCLE_COLOR_3);
        communicationElements.add(scoreCircle3);

        //Title
        final SvgText title = new SvgText("COMMUNICATION", 67, FontWeight.BOLD, COMMUNICATION_CIRCLE_COLOR_1, 1243L, 2580L);
        communicationElements.add(title);

        final SvgLine titleLine = new SvgLine(COMMUNICATION_CIRCLE_COLOR_1, DEFAULT_STROKE_WIDTH, 1240L, 3230L, 2111L, 3230L);
        communicationElements.add(titleLine);

        //Content
        final SvgRectangle content1 = new SvgRectangle(1240L, 2644L, 871L, 190L, COMMUNICATION_COLOR_1, ALPHA_SECONDARY);
        communicationElements.add(content1);
        communicationElements.add(generateText(COMMUNICATION_TEXT_1, 1240L, 2644L, 871, 190));

        final SvgRectangle content2 = new SvgRectangle(1240L, 2842L, 871L, 190L, COMMUNICATION_COLOR_2, ALPHA_PRIMARY);
        communicationElements.add(content2);
        communicationElements.add(generateText(COMMUNICATION_TEXT_2, 1240L, 2842L, 871, 190));

        final SvgRectangle content3 = new SvgRectangle(1240L, 3040L, 871L, 190L, COMMUNICATION_COLOR_3, ALPHA_SECONDARY);
        communicationElements.add(content3);
        communicationElements.add(generateText(COMMUNICATION_TEXT_3, 1240L, 3040L, 871, 190));

        return communicationElements;
    }

    private List<SvgAreaElement> generateSelfAware() {
        final List<SvgAreaElement> selfAwareElements = new ArrayList<>();

        //Line
        final SvgPath path = new SvgPath(SELF_AWARE_CIRCLE_COLOR_1, DEFAULT_STROKE_WIDTH, 2587L, 2002L, new Point(2587L, 2341L),
                new Point(2896L, 2640L), new Point(3767L, 2640L));
        selfAwareElements.add(path);

        //Circles
        final SvgCircle scoreCircle1 = new SvgCircle(2539L, 1954L, 46L);
        scoreCircle1.getElementAttributes().setFill(SELF_AWARE_CIRCLE_COLOR_1);
        selfAwareElements.add(scoreCircle1);

        final SvgCircle scoreCircle2 = new SvgCircle(2539L, 2080L, 46L);
        scoreCircle2.getElementAttributes().setFill(SELF_AWARE_CIRCLE_COLOR_2);
        selfAwareElements.add(scoreCircle2);

        final SvgCircle scoreCircle3 = new SvgCircle(2539L, 2205L, 46L);
        scoreCircle3.getElementAttributes().setFill(SELF_AWARE_CIRCLE_COLOR_3);
        selfAwareElements.add(scoreCircle3);

        //Title
        final SvgText title = new SvgText("SELF AWARE", 67, FontWeight.BOLD, SELF_AWARE_CIRCLE_COLOR_1, 2898L, 2580L);
        selfAwareElements.add(title);

        final SvgLine titleLine = new SvgLine(SELF_AWARE_CIRCLE_COLOR_1, DEFAULT_STROKE_WIDTH, 2896L, 3230L, 3766L, 3230L);
        selfAwareElements.add(titleLine);

        //Content
        final SvgRectangle content1 = new SvgRectangle(2896L, 2644L, 871L, 190L, SELF_AWARE_COLOR_1, ALPHA_SECONDARY);
        selfAwareElements.add(content1);
        selfAwareElements.add(generateText(SELF_AWARE_TEXT_1, 2896L, 2644L, 871, 190));

        final SvgRectangle content2 = new SvgRectangle(2896L, 2842L, 871L, 190L, SELF_AWARE_COLOR_2, ALPHA_PRIMARY);
        selfAwareElements.add(content2);
        selfAwareElements.add(generateText(SELF_AWARE_TEXT_2, 2896L, 2842L, 871, 190));

        final SvgRectangle content3 = new SvgRectangle(2896L, 3040L, 871L, 190L, SELF_AWARE_COLOR_3, ALPHA_SECONDARY);
        selfAwareElements.add(content3);
        selfAwareElements.add(generateText(SELF_AWARE_TEXT_3, 2896L, 3040L, 871, 190));

        return selfAwareElements;
    }

    private List<SvgAreaElement> generateAnalysis() {
        final List<SvgAreaElement> analysisElements = new ArrayList<>();

        //Line
        final SvgPath path = new SvgPath(ANALYSIS_CIRCLE_COLOR_1, DEFAULT_STROKE_WIDTH, 2754L, 2002L, new Point(2754L, 2341L),
                new Point(2931L, 2430L), new Point(4725L, 2430L));
        analysisElements.add(path);

        //Circles
        final SvgCircle scoreCircle1 = new SvgCircle(2706L, 1954L, 46L);
        scoreCircle1.getElementAttributes().setFill(ANALYSIS_CIRCLE_COLOR_1);
        analysisElements.add(scoreCircle1);

        final SvgCircle scoreCircle2 = new SvgCircle(2706L, 2080L, 46L);
        scoreCircle2.getElementAttributes().setFill(ANALYSIS_CIRCLE_COLOR_2);
        analysisElements.add(scoreCircle2);

        final SvgCircle scoreCircle3 = new SvgCircle(2706L, 2205L, 46L);
        scoreCircle3.getElementAttributes().setFill(ANALYSIS_CIRCLE_COLOR_3);
        analysisElements.add(scoreCircle3);

        //Title
        final SvgText title = new SvgText("ANALYSIS", 67, FontWeight.BOLD, ANALYSIS_CIRCLE_COLOR_1, 3856L, 2365L);
        analysisElements.add(title);

        final SvgLine titleLine = new SvgLine(ANALYSIS_CIRCLE_COLOR_1, DEFAULT_STROKE_WIDTH, 3855L, 3016L, 4725L, 3016L);
        analysisElements.add(titleLine);

        //Content
        final SvgRectangle content1 = new SvgRectangle(3855L, 2434L, 871L, 190L, ANALYSIS_COLOR_1, ALPHA_SECONDARY);
        analysisElements.add(content1);
        analysisElements.add(generateText(ANALYSIS_TEXT_1, 3855L, 2434L, 871, 190));

        final SvgRectangle content2 = new SvgRectangle(3855L, 2632L, 871L, 190L, ANALYSIS_COLOR_2, ALPHA_PRIMARY);
        analysisElements.add(content2);
        analysisElements.add(generateText(ANALYSIS_TEXT_2, 3855L, 2632L, 871, 190));

        final SvgRectangle content3 = new SvgRectangle(3855L, 2830L, 871L, 190L, ANALYSIS_COLOR_3, ALPHA_SECONDARY);
        analysisElements.add(content3);
        analysisElements.add(generateText(ANALYSIS_TEXT_3, 3855L, 2830L, 871, 190));

        return analysisElements;
    }

    @BeforeClass
    public void prepareFolder() throws IOException {
        Files.createDirectories(Paths.get(OUTPUT_FOLDER));
    }

    @Test
    public void generateCADT() throws IOException {
        cadtTemplate = new SvgTemplate();
        cadtTemplate.getElementAttributes().setHeight(3488L);
        cadtTemplate.getElementAttributes().setWidth(5038L);
        cadtTemplate.setSvgBackground(generateBackground());

        //Add big white rectangle as 2nd background.
        final SvgRectangle secondBackground = new SvgRectangle(163L, 189L, "4680px", "3153px", "ffffff");
        secondBackground.getElementStroke().setStrokeColor(BORDER_COLOR);
        secondBackground.getElementStroke().setStrokeWidth(DEFAULT_STROKE_WIDTH);
        secondBackground.setXRadius(70L);
        secondBackground.setYRadius(70L);
        cadtTemplate.addElement(secondBackground);

        cadtTemplate.addElements(generateHeader());
        //cadtTemplate.addElements(generateScore());
        cadtTemplate.addElements(generateUniversal());
        cadtTemplate.addElements(generateSociety());
        cadtTemplate.addElements(generateVision());
        cadtTemplate.addElements(generateStrength());

        cadtTemplate.addElements(generateInspiration());
        cadtTemplate.addElements(generateStructure());
        cadtTemplate.addElements(generateStructureInspiration());
        cadtTemplate.addElements(generateAdaptability());
        cadtTemplate.addElements(generateAction());
        cadtTemplate.addElements(generateAdaptabilityAction());

        cadtTemplate.addElements(generateMaterialAttachment());
        cadtTemplate.addElements(generateCommunication());
        cadtTemplate.addElements(generateSelfAware());
        cadtTemplate.addElements(generateAnalysis());

        try (PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(OUTPUT_FOLDER
                + File.separator + "CADT_v1.json")), true)) {
            out.println(cadtTemplate.toJson());
        }
    }

    @Test(dependsOnMethods = "generateCADT")
    public void executeCadt() throws IOException {
        final DroolsSubmittedForm droolsSubmittedForm = DroolsSubmittedForm.getFromJson(FileReader.getResource(DROOLS_FORM_FILE_PATH, StandardCharsets.UTF_8));
        final List<String> svgResults = droolsResultController.execute(droolsSubmittedForm, Collections.singletonList(cadtTemplate));
        Assert.assertEquals(svgResults.size(), 1);

        try (PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(OUTPUT_FOLDER
                + File.separator + "CADTFromDrools.svg")), true)) {
            out.println(svgResults.get(0));
        }

        checkContent(svgResults.get(0), "cadtFromDrools.svg");
    }

    @AfterClass
    public void removeFolder() {
        Assert.assertTrue(deleteDirectory(new File(OUTPUT_FOLDER)));
    }
}
