package com.biit.infographic.core.svg;

import com.biit.drools.form.DroolsSubmittedForm;
import com.biit.infographic.core.controllers.DroolsResultController;
import com.biit.infographic.core.engine.content.value.ConditionOperation;
import com.biit.infographic.core.engine.content.value.ValueCalculator;
import com.biit.infographic.core.generators.SvgGenerator;
import com.biit.infographic.core.models.svg.StrokeAlign;
import com.biit.infographic.core.models.svg.SvgAreaElement;
import com.biit.infographic.core.models.svg.SvgTemplate;
import com.biit.infographic.core.models.svg.components.SvgCircle;
import com.biit.infographic.core.models.svg.components.SvgLine;
import com.biit.infographic.core.models.svg.components.SvgRectangle;
import com.biit.infographic.core.models.svg.components.gradient.SvgGradient;
import com.biit.infographic.core.models.svg.components.gradient.SvgGradientStop;
import com.biit.infographic.core.pdf.PdfController;
import com.biit.server.utils.exceptions.EmptyPdfBodyException;
import com.biit.server.utils.exceptions.InvalidXmlElementException;
import com.biit.utils.file.FileReader;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
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
public class CadtProfileLayerFormDroolsTest extends AbstractTestNGSpringContextTests {
    protected static final String OUTPUT_FOLDER = System.getProperty("java.io.tmpdir") + File.separator + "SvgTests";

    private static final long SQUARE_LENGTH = 34;
    private static final long SQUARE_BORDER_LENGTH = 46;

    private static final long BORDER_CIRCLE_RADIUS = 23L;

    private static final Double BORDER_CIRCLE_WIDTH = 2D;
    private static final long CIRCLE_RADIUS = 17L;
    private static final double CIRCLE_STROKE_WIDTH = 2.0D;

    private static final Double LINE_STROKE_WIDTH = 2D;
    private static final Double FULL_ALPHA = 1.0D;

    private static final String DISABLED_COLOR = "d3d4d4";
    private static final String UNSELECTED_COLOR = "ffffff";
    private static final String STRUCTURE_COLOR = "8bc4ab";
    private static final String INSPIRATION_COLOR = "919ee1";
    private static final String ADAPTABILITY_COLOR = "7ccadf";
    private static final String ACTION_COLOR = "e9a197";


    private static final String UNIVERSAL_COLOR = "919ee1";
    private static final String UNIVERSAL_LINE_COLOR = "#DROOLS%CADT_Profile_Creator%/form/profile/receptive" + ConditionOperation.CONTAINS.getRepresentation() + "receptive" + ValueCalculator.CONDITION_SEPARATION + UNIVERSAL_COLOR + ValueCalculator.VALUE_SEPARATION + DISABLED_COLOR + "#";
    private static final String UNIVERSAL_BORDER_COLOR = "#DROOLS%CADT_Profile_Creator%/form/profile/receptive" + ConditionOperation.CONTAINS.getRepresentation() + "receptive" + ValueCalculator.CONDITION_SEPARATION + UNIVERSAL_COLOR + ValueCalculator.VALUE_SEPARATION + DISABLED_COLOR + "#";
    private static final String UNIVERSAL_BACKGROUND_OPACITY = "#DROOLS%CADT_Profile_Creator%/form/profile/receptive" + ConditionOperation.CONTAINS.getRepresentation() + "receptive" + ValueCalculator.CONDITION_SEPARATION + "1" + ValueCalculator.VALUE_SEPARATION + "0" + "#";
    private static final String UNIVERSAL_CIRCLE_COLOR_1 = "#DROOLS%CADT_Profile_Creator%/form/profile/receptive" + ConditionOperation.CONTAINS.getRepresentation() + "receptive" + ValueCalculator.CONDITION_SEPARATION + UNIVERSAL_COLOR + ValueCalculator.VALUE_SEPARATION + UNSELECTED_COLOR + "#";
    private static final String UNIVERSAL_CIRCLE_COLOR_2 = "#DROOLS%CADT_Profile_Creator%/form/profile/receptive" + ConditionOperation.CONTAINS.getRepresentation() + "interpersonal-sensitivity" + ValueCalculator.CONDITION_SEPARATION + UNIVERSAL_COLOR + ValueCalculator.VALUE_SEPARATION + UNSELECTED_COLOR + "#";
    private static final String UNIVERSAL_CIRCLE_COLOR_3 = "#DROOLS%CADT_Profile_Creator%/form/profile/receptive" + ConditionOperation.CONTAINS.getRepresentation() + "multicultural-sensitivity" + ValueCalculator.CONDITION_SEPARATION + UNIVERSAL_COLOR + ValueCalculator.VALUE_SEPARATION + UNSELECTED_COLOR + "#";
    private static final String UNIVERSAL_CIRCLE_BORDER_COLOR_1 = UNIVERSAL_COLOR;
    private static final String UNIVERSAL_CIRCLE_BORDER_COLOR_2 = UNIVERSAL_COLOR;
    private static final String UNIVERSAL_CIRCLE_BORDER_COLOR_3 = UNIVERSAL_COLOR;

    private static final String SOCIETY_COLOR = "7ccadf";
    private static final String SOCIETY_LINE_COLOR = "#DROOLS%CADT_Profile_Creator%/form/profile/innovator" + ConditionOperation.CONTAINS.getRepresentation() + "innovator" + ValueCalculator.CONDITION_SEPARATION + SOCIETY_COLOR + ValueCalculator.VALUE_SEPARATION + DISABLED_COLOR + "#";
    private static final String SOCIETY_BORDER_COLOR = "#DROOLS%CADT_Profile_Creator%/form/profile/innovator" + ConditionOperation.CONTAINS.getRepresentation() + "innovator" + ValueCalculator.CONDITION_SEPARATION + SOCIETY_COLOR + ValueCalculator.VALUE_SEPARATION + DISABLED_COLOR + "#";
    private static final String SOCIETY_BACKGROUND_OPACITY = "#DROOLS%CADT_Profile_Creator%/form/profile/innovator" + ConditionOperation.CONTAINS.getRepresentation() + "innovator" + ValueCalculator.CONDITION_SEPARATION + "1" + ValueCalculator.VALUE_SEPARATION + "0" + "#";
    private static final String SOCIETY_CIRCLE_COLOR_1 = "#DROOLS%CADT_Profile_Creator%/form/profile/innovator" + ConditionOperation.CONTAINS.getRepresentation() + "innovator" + ValueCalculator.CONDITION_SEPARATION + SOCIETY_COLOR + ValueCalculator.VALUE_SEPARATION + UNSELECTED_COLOR + "#";
    private static final String SOCIETY_CIRCLE_COLOR_2 = "#DROOLS%CADT_Profile_Creator%/form/profile/innovator" + ConditionOperation.CONTAINS.getRepresentation() + "cooperation" + ValueCalculator.CONDITION_SEPARATION + SOCIETY_COLOR + ValueCalculator.VALUE_SEPARATION + UNSELECTED_COLOR + "#";
    private static final String SOCIETY_CIRCLE_COLOR_3 = "#DROOLS%CADT_Profile_Creator%/form/profile/innovator" + ConditionOperation.CONTAINS.getRepresentation() + "innovation" + ValueCalculator.CONDITION_SEPARATION + SOCIETY_COLOR + ValueCalculator.VALUE_SEPARATION + UNSELECTED_COLOR + "#";
    private static final String SOCIETY_CIRCLE_BORDER_COLOR_1 = SOCIETY_BORDER_COLOR;
    private static final String SOCIETY_CIRCLE_BORDER_COLOR_2 = SOCIETY_BORDER_COLOR;
    private static final String SOCIETY_CIRCLE_BORDER_COLOR_3 = SOCIETY_BORDER_COLOR;

    private static final String VISION_COLOR = "e9a197";
    private static final String VISION_LINE_COLOR = "#DROOLS%CADT_Profile_Creator%/form/profile/visionary" + ConditionOperation.CONTAINS.getRepresentation() + "visionary" + ValueCalculator.CONDITION_SEPARATION + VISION_COLOR + ValueCalculator.VALUE_SEPARATION + DISABLED_COLOR + "#";
    private static final String VISION_BORDER_COLOR = "#DROOLS%CADT_Profile_Creator%/form/profile/visionary" + ConditionOperation.CONTAINS.getRepresentation() + "visionary" + ValueCalculator.CONDITION_SEPARATION + VISION_COLOR + ValueCalculator.VALUE_SEPARATION + DISABLED_COLOR + "#";
    private static final String VISION_BACKGROUND_OPACITY = "#DROOLS%CADT_Profile_Creator%/form/profile/visionary" + ConditionOperation.CONTAINS.getRepresentation() + "visionary" + ValueCalculator.CONDITION_SEPARATION + "1" + ValueCalculator.VALUE_SEPARATION + "0" + "#";
    private static final String VISION_CIRCLE_COLOR_1 = "#DROOLS%CADT_Profile_Creator%/form/profile/visionary" + ConditionOperation.CONTAINS.getRepresentation() + "visionary" + ValueCalculator.CONDITION_SEPARATION + VISION_COLOR + ValueCalculator.VALUE_SEPARATION + UNSELECTED_COLOR + "#";
    private static final String VISION_CIRCLE_COLOR_2 = "#DROOLS%CADT_Profile_Creator%/form/profile/visionary" + ConditionOperation.CONTAINS.getRepresentation() + "persuasiveness" + ValueCalculator.CONDITION_SEPARATION + VISION_COLOR + ValueCalculator.VALUE_SEPARATION + UNSELECTED_COLOR + "#";
    private static final String VISION_CIRCLE_COLOR_3 = "#DROOLS%CADT_Profile_Creator%/form/profile/visionary" + ConditionOperation.CONTAINS.getRepresentation() + "future" + ValueCalculator.CONDITION_SEPARATION + VISION_COLOR + ValueCalculator.VALUE_SEPARATION + UNSELECTED_COLOR + "#";
    private static final String VISION_CIRCLE_BORDER_COLOR_1 = VISION_BORDER_COLOR;
    private static final String VISION_CIRCLE_BORDER_COLOR_2 = VISION_BORDER_COLOR;
    private static final String VISION_CIRCLE_BORDER_COLOR_3 = VISION_BORDER_COLOR;

    private static final String STRENGTH_COLOR = "919ee1";
    private static final String STRENGTH_LINE_COLOR = "#DROOLS%CADT_Profile_Creator%/form/profile/strategist" + ConditionOperation.CONTAINS.getRepresentation() + "strategist" + ValueCalculator.CONDITION_SEPARATION + STRENGTH_COLOR + ValueCalculator.VALUE_SEPARATION + DISABLED_COLOR + "#";
    private static final String STRENGTH_BORDER_COLOR = "#DROOLS%CADT_Profile_Creator%/form/profile/strategist" + ConditionOperation.CONTAINS.getRepresentation() + "strategist" + ValueCalculator.CONDITION_SEPARATION + STRENGTH_COLOR + ValueCalculator.VALUE_SEPARATION + DISABLED_COLOR + "#";
    private static final String STRENGTH_BACKGROUND_OPACITY = "#DROOLS%CADT_Profile_Creator%/form/profile/strategist" + ConditionOperation.CONTAINS.getRepresentation() + "strategist" + ValueCalculator.CONDITION_SEPARATION + "1" + ValueCalculator.VALUE_SEPARATION + "0" + "#";
    private static final String STRENGTH_CIRCLE_COLOR_1 = "#DROOLS%CADT_Profile_Creator%/form/profile/strategist" + ConditionOperation.CONTAINS.getRepresentation() + "strategist" + ValueCalculator.CONDITION_SEPARATION + STRENGTH_COLOR + ValueCalculator.VALUE_SEPARATION + UNSELECTED_COLOR + "#";
    private static final String STRENGTH_CIRCLE_COLOR_2 = "#DROOLS%CADT_Profile_Creator%/form/profile/strategist" + ConditionOperation.CONTAINS.getRepresentation() + "decisiveness" + ValueCalculator.CONDITION_SEPARATION + STRENGTH_COLOR + ValueCalculator.VALUE_SEPARATION + UNSELECTED_COLOR + "#";
    private static final String STRENGTH_CIRCLE_COLOR_3 = "#DROOLS%CADT_Profile_Creator%/form/profile/strategist" + ConditionOperation.CONTAINS.getRepresentation() + "judgement" + ValueCalculator.CONDITION_SEPARATION + STRENGTH_COLOR + ValueCalculator.VALUE_SEPARATION + UNSELECTED_COLOR + "#";
    private static final String STRENGTH_CIRCLE_BORDER_COLOR_1 = STRENGTH_BORDER_COLOR;
    private static final String STRENGTH_CIRCLE_BORDER_COLOR_2 = STRENGTH_BORDER_COLOR;
    private static final String STRENGTH_CIRCLE_BORDER_COLOR_3 = STRENGTH_BORDER_COLOR;

    private static final String STRUCTURE_CIRCLE_COLOR_1 = "#DROOLS%CADT_Profile_Creator%/form/profile/structure" + ConditionOperation.CONTAINS.getRepresentation() + "discipline" + ValueCalculator.CONDITION_SEPARATION + STRUCTURE_COLOR + ValueCalculator.VALUE_SEPARATION + UNSELECTED_COLOR + "#";
    private static final String STRUCTURE_CIRCLE_COLOR_2 = "#DROOLS%CADT_Profile_Creator%/form/profile/structure" + ConditionOperation.CONTAINS.getRepresentation() + "goal-setting" + ValueCalculator.CONDITION_SEPARATION + STRUCTURE_COLOR + ValueCalculator.VALUE_SEPARATION + UNSELECTED_COLOR + "#";
    private static final String INSPIRATION_CIRCLE_COLOR_1 = "#DROOLS%CADT_Profile_Creator%/form/profile/inspiration" + ConditionOperation.CONTAINS.getRepresentation() + "conscientiousness" + ValueCalculator.CONDITION_SEPARATION + INSPIRATION_COLOR + ValueCalculator.VALUE_SEPARATION + UNSELECTED_COLOR + "#";
    private static final String INSPIRATION_CIRCLE_COLOR_2 = "#DROOLS%CADT_Profile_Creator%/form/profile/inspiration" + ConditionOperation.CONTAINS.getRepresentation() + "engagement" + ValueCalculator.CONDITION_SEPARATION + INSPIRATION_COLOR + ValueCalculator.VALUE_SEPARATION + UNSELECTED_COLOR + "#";
    private static final String ADAPTABILITY_CIRCLE_COLOR_1 = "#DROOLS%CADT_Profile_Creator%/form/profile/adaptability" + ConditionOperation.CONTAINS.getRepresentation() + "building-and-maintaining" + ValueCalculator.CONDITION_SEPARATION + ADAPTABILITY_COLOR + ValueCalculator.VALUE_SEPARATION + UNSELECTED_COLOR + "#";
    private static final String ADAPTABILITY_CIRCLE_COLOR_2 = "#DROOLS%CADT_Profile_Creator%/form/profile/adaptability" + ConditionOperation.CONTAINS.getRepresentation() + "flexibility" + ValueCalculator.CONDITION_SEPARATION + ADAPTABILITY_COLOR + ValueCalculator.VALUE_SEPARATION + UNSELECTED_COLOR + "#";
    private static final String ACTION_CIRCLE_COLOR_1 = "#DROOLS%CADT_Profile_Creator%/form/profile/action" + ConditionOperation.CONTAINS.getRepresentation() + "direction" + ValueCalculator.CONDITION_SEPARATION + ACTION_COLOR + ValueCalculator.VALUE_SEPARATION + UNSELECTED_COLOR + "#";
    private static final String ACTION_CIRCLE_COLOR_2 = "#DROOLS%CADT_Profile_Creator%/form/profile/action" + ConditionOperation.CONTAINS.getRepresentation() + "initiative" + ValueCalculator.CONDITION_SEPARATION + ACTION_COLOR + ValueCalculator.VALUE_SEPARATION + UNSELECTED_COLOR + "#";

    private static final String MATERIAL_ATTACHMENT_COLOR = "8bc4ab";
    private static final String MATERIAL_ATTACHMENT_LINE_COLOR = "#DROOLS%CADT_Profile_Creator%/form/profile/banker" + ConditionOperation.CONTAINS.getRepresentation() + "banker" + ValueCalculator.CONDITION_SEPARATION + MATERIAL_ATTACHMENT_COLOR + ValueCalculator.VALUE_SEPARATION + DISABLED_COLOR + "#";
    private static final String MATERIAL_ATTACHMENT_BORDER_COLOR = "#DROOLS%CADT_Profile_Creator%/form/profile/banker" + ConditionOperation.CONTAINS.getRepresentation() + "banker" + ValueCalculator.CONDITION_SEPARATION + MATERIAL_ATTACHMENT_COLOR + ValueCalculator.VALUE_SEPARATION + DISABLED_COLOR + "#";
    private static final String MATERIAL_ATTACHMENT_BACKGROUND_OPACITY = "#DROOLS%CADT_Profile_Creator%/form/profile/banker" + ConditionOperation.CONTAINS.getRepresentation() + "banker" + ValueCalculator.CONDITION_SEPARATION + "1" + ValueCalculator.VALUE_SEPARATION + "0" + "#";
    private static final String MATERIAL_ATTACHMENT_CIRCLE_COLOR_1 = "#DROOLS%CADT_Profile_Creator%/form/profile/banker" + ConditionOperation.CONTAINS.getRepresentation() + "banker" + ValueCalculator.CONDITION_SEPARATION + MATERIAL_ATTACHMENT_COLOR + ValueCalculator.VALUE_SEPARATION + UNSELECTED_COLOR + "#";
    private static final String MATERIAL_ATTACHMENT_CIRCLE_COLOR_2 = "#DROOLS%CADT_Profile_Creator%/form/profile/banker" + ConditionOperation.CONTAINS.getRepresentation() + "business-minded" + ValueCalculator.CONDITION_SEPARATION + MATERIAL_ATTACHMENT_COLOR + ValueCalculator.VALUE_SEPARATION + UNSELECTED_COLOR + "#";
    private static final String MATERIAL_ATTACHMENT_CIRCLE_COLOR_3 = "#DROOLS%CADT_Profile_Creator%/form/profile/banker" + ConditionOperation.CONTAINS.getRepresentation() + "tenacity" + ValueCalculator.CONDITION_SEPARATION + MATERIAL_ATTACHMENT_COLOR + ValueCalculator.VALUE_SEPARATION + UNSELECTED_COLOR + "#";
    private static final String MATERIAL_ATTACHMENT_CIRCLE_BORDER_COLOR_1 = MATERIAL_ATTACHMENT_BORDER_COLOR;
    private static final String MATERIAL_ATTACHMENT_CIRCLE_BORDER_COLOR_2 = MATERIAL_ATTACHMENT_BORDER_COLOR;
    private static final String MATERIAL_ATTACHMENT_CIRCLE_BORDER_COLOR_3 = MATERIAL_ATTACHMENT_BORDER_COLOR;

    private static final String COMMUNICATION_COLOR = "7ccadf";
    private static final String COMMUNICATION_LINE_COLOR = "#DROOLS%CADT_Profile_Creator%/form/profile/tradesman" + ConditionOperation.CONTAINS.getRepresentation() + "tradesman" + ValueCalculator.CONDITION_SEPARATION + COMMUNICATION_COLOR + ValueCalculator.VALUE_SEPARATION + DISABLED_COLOR + "#";
    private static final String COMMUNICATION_BORDER_COLOR = "#DROOLS%CADT_Profile_Creator%/form/profile/tradesman" + ConditionOperation.CONTAINS.getRepresentation() + "tradesman" + ValueCalculator.CONDITION_SEPARATION + COMMUNICATION_COLOR + ValueCalculator.VALUE_SEPARATION + DISABLED_COLOR + "#";
    private static final String COMMUNICATION_BACKGROUND_OPACITY = "#DROOLS%CADT_Profile_Creator%/form/profile/tradesman" + ConditionOperation.CONTAINS.getRepresentation() + "tradesman" + ValueCalculator.CONDITION_SEPARATION + "1" + ValueCalculator.VALUE_SEPARATION + "0" + "#";
    private static final String COMMUNICATION_CIRCLE_COLOR_1 = "#DROOLS%CADT_Profile_Creator%/form/profile/tradesman" + ConditionOperation.CONTAINS.getRepresentation() + "tradesman" + ValueCalculator.CONDITION_SEPARATION + COMMUNICATION_COLOR + ValueCalculator.VALUE_SEPARATION + UNSELECTED_COLOR + "#";
    private static final String COMMUNICATION_CIRCLE_COLOR_2 = "#DROOLS%CADT_Profile_Creator%/form/profile/tradesman" + ConditionOperation.CONTAINS.getRepresentation() + "communication-skills" + ValueCalculator.CONDITION_SEPARATION + COMMUNICATION_COLOR + ValueCalculator.VALUE_SEPARATION + UNSELECTED_COLOR + "#";
    private static final String COMMUNICATION_CIRCLE_COLOR_3 = "#DROOLS%CADT_Profile_Creator%/form/profile/tradesman" + ConditionOperation.CONTAINS.getRepresentation() + "client-oriented" + ValueCalculator.CONDITION_SEPARATION + COMMUNICATION_COLOR + ValueCalculator.VALUE_SEPARATION + UNSELECTED_COLOR + "#";
    private static final String COMMUNICATION_CIRCLE_BORDER_COLOR_1 = COMMUNICATION_BORDER_COLOR;
    private static final String COMMUNICATION_CIRCLE_BORDER_COLOR_2 = COMMUNICATION_BORDER_COLOR;
    private static final String COMMUNICATION_CIRCLE_BORDER_COLOR_3 = COMMUNICATION_BORDER_COLOR;

    private static final String SELF_AWARE_COLOR = "e9a197";
    private static final String SELF_AWARE_LINE_COLOR = "#DROOLS%CADT_Profile_Creator%/form/profile/leader" + ConditionOperation.CONTAINS.getRepresentation() + "leader" + ValueCalculator.CONDITION_SEPARATION + SELF_AWARE_COLOR + ValueCalculator.VALUE_SEPARATION + DISABLED_COLOR + "#";
    private static final String SELF_AWARE_BORDER_COLOR = "#DROOLS%CADT_Profile_Creator%/form/profile/leader" + ConditionOperation.CONTAINS.getRepresentation() + "leader" + ValueCalculator.CONDITION_SEPARATION + SELF_AWARE_COLOR + ValueCalculator.VALUE_SEPARATION + DISABLED_COLOR + "#";
    private static final String SELF_AWARE_BACKGROUND_OPACITY = "#DROOLS%CADT_Profile_Creator%/form/profile/leader" + ConditionOperation.CONTAINS.getRepresentation() + "leader" + ValueCalculator.CONDITION_SEPARATION + "1" + ValueCalculator.VALUE_SEPARATION + "0" + "#";
    private static final String SELF_AWARE_CIRCLE_COLOR_1 = "#DROOLS%CADT_Profile_Creator%/form/profile/leader" + ConditionOperation.CONTAINS.getRepresentation() + "leader" + ValueCalculator.CONDITION_SEPARATION + SELF_AWARE_COLOR + ValueCalculator.VALUE_SEPARATION + UNSELECTED_COLOR + "#";
    private static final String SELF_AWARE_CIRCLE_COLOR_2 = "#DROOLS%CADT_Profile_Creator%/form/profile/leader" + ConditionOperation.CONTAINS.getRepresentation() + "leadership" + ValueCalculator.CONDITION_SEPARATION + SELF_AWARE_COLOR + ValueCalculator.VALUE_SEPARATION + UNSELECTED_COLOR + "#";
    private static final String SELF_AWARE_CIRCLE_COLOR_3 = "#DROOLS%CADT_Profile_Creator%/form/profile/leader" + ConditionOperation.CONTAINS.getRepresentation() + "independence" + ValueCalculator.CONDITION_SEPARATION + SELF_AWARE_COLOR + ValueCalculator.VALUE_SEPARATION + UNSELECTED_COLOR + "#";
    private static final String SELF_AWARE_CIRCLE_BORDER_COLOR_1 = SELF_AWARE_BORDER_COLOR;
    private static final String SELF_AWARE_CIRCLE_BORDER_COLOR_2 = SELF_AWARE_BORDER_COLOR;
    private static final String SELF_AWARE_CIRCLE_BORDER_COLOR_3 = SELF_AWARE_BORDER_COLOR;

    private static final String ANALYSIS_COLOR = "8bc4ab";
    private static final String ANALYSIS_LINE_COLOR = "#DROOLS%CADT_Profile_Creator%/form/profile/scientist" + ConditionOperation.CONTAINS.getRepresentation() + "scientist" + ValueCalculator.CONDITION_SEPARATION + ANALYSIS_COLOR + ValueCalculator.VALUE_SEPARATION + DISABLED_COLOR + "#";
    private static final String ANALYSIS_BORDER_COLOR = "#DROOLS%CADT_Profile_Creator%/form/profile/scientist" + ConditionOperation.CONTAINS.getRepresentation() + "scientist" + ValueCalculator.CONDITION_SEPARATION + ANALYSIS_COLOR + ValueCalculator.VALUE_SEPARATION + DISABLED_COLOR + "#";
    private static final String ANALYSIS_BACKGROUND_OPACITY = "#DROOLS%CADT_Profile_Creator%/form/profile/scientist" + ConditionOperation.CONTAINS.getRepresentation() + "scientist" + ValueCalculator.CONDITION_SEPARATION + "1" + ValueCalculator.VALUE_SEPARATION + "0" + "#";
    private static final String ANALYSIS_CIRCLE_COLOR_1 = "#DROOLS%CADT_Profile_Creator%/form/profile/scientist" + ConditionOperation.CONTAINS.getRepresentation() + "scientist" + ValueCalculator.CONDITION_SEPARATION + ANALYSIS_COLOR + ValueCalculator.VALUE_SEPARATION + UNSELECTED_COLOR + "#";
    private static final String ANALYSIS_CIRCLE_COLOR_2 = "#DROOLS%CADT_Profile_Creator%/form/profile/scientist" + ConditionOperation.CONTAINS.getRepresentation() + "problem-analysis" + ValueCalculator.CONDITION_SEPARATION + ANALYSIS_COLOR + ValueCalculator.VALUE_SEPARATION + UNSELECTED_COLOR + "#";
    private static final String ANALYSIS_CIRCLE_COLOR_3 = "#DROOLS%CADT_Profile_Creator%/form/profile/scientist" + ConditionOperation.CONTAINS.getRepresentation() + "planification" + ValueCalculator.CONDITION_SEPARATION + ANALYSIS_COLOR + ValueCalculator.VALUE_SEPARATION + UNSELECTED_COLOR + "#";
    private static final String ANALYSIS_CIRCLE_BORDER_COLOR_1 = ANALYSIS_BORDER_COLOR;
    private static final String ANALYSIS_CIRCLE_BORDER_COLOR_2 = ANALYSIS_BORDER_COLOR;
    private static final String ANALYSIS_CIRCLE_BORDER_COLOR_3 = ANALYSIS_BORDER_COLOR;

    private static final String EMPTY_BULLET_COLOR = "ffffff";

    private static final String DROOLS_FORM_FILE_PATH = "drools/DroolsSubmittedCadtProfileCreator.json";

    @Autowired
    private DroolsResultController droolsResultController;

    @Autowired
    private PdfController pdfController;

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

    private List<SvgAreaElement> generateUniversal() {
        final List<SvgAreaElement> universalElements = new ArrayList<>();

        //Line
        final SvgLine scoreLine = new SvgLine(UNIVERSAL_LINE_COLOR, LINE_STROKE_WIDTH, 32, 48, 32, 116);
        universalElements.add(scoreLine);

        //Border
        final SvgCircle border = new SvgCircle(10, 10, BORDER_CIRCLE_RADIUS);
        border.getElementAttributes().setFill(EMPTY_BULLET_COLOR);
        border.getElementAttributes().setFillOpacity(UNIVERSAL_BACKGROUND_OPACITY);
        border.getElementStroke().setStrokeColor(UNIVERSAL_BORDER_COLOR);
        border.getElementStroke().setStrokeWidth(BORDER_CIRCLE_WIDTH);
        border.getElementStroke().setStrokeOpacity(UNIVERSAL_BACKGROUND_OPACITY);
        universalElements.add(border);

        //Circles
        final SvgCircle scoreCircle1 = new SvgCircle(16, 16, CIRCLE_RADIUS);
        scoreCircle1.getElementAttributes().setFill(UNIVERSAL_CIRCLE_COLOR_1);
        scoreCircle1.getElementStroke().setStrokeColor(UNIVERSAL_CIRCLE_BORDER_COLOR_1);
        scoreCircle1.getElementStroke().setStrokeWidth(CIRCLE_STROKE_WIDTH);
        universalElements.add(scoreCircle1);

        final SvgCircle scoreCircle2 = new SvgCircle(16, 61, CIRCLE_RADIUS);
        scoreCircle2.getElementAttributes().setFill(UNIVERSAL_CIRCLE_COLOR_2);
        scoreCircle2.getElementStroke().setStrokeColor(UNIVERSAL_CIRCLE_BORDER_COLOR_2);
        scoreCircle2.getElementStroke().setStrokeWidth(CIRCLE_STROKE_WIDTH);
        universalElements.add(scoreCircle2);

        final SvgCircle scoreCircle3 = new SvgCircle(16, 106, CIRCLE_RADIUS);
        scoreCircle3.getElementAttributes().setFill(UNIVERSAL_CIRCLE_COLOR_3);
        scoreCircle3.getElementStroke().setStrokeColor(UNIVERSAL_CIRCLE_BORDER_COLOR_3);
        scoreCircle3.getElementStroke().setStrokeWidth(CIRCLE_STROKE_WIDTH);
        universalElements.add(scoreCircle3);

        return universalElements;
    }

    private List<SvgAreaElement> generateSociety() {
        final List<SvgAreaElement> societyElements = new ArrayList<>();

        //Line
        final SvgLine scoreLine = new SvgLine(SOCIETY_LINE_COLOR, LINE_STROKE_WIDTH, 92, 48, 92, 116);
        societyElements.add(scoreLine);

        //Border
        final SvgCircle border = new SvgCircle(70, 10, BORDER_CIRCLE_RADIUS);
        border.getElementAttributes().setFill(EMPTY_BULLET_COLOR);
        border.getElementAttributes().setFillOpacity(SOCIETY_BACKGROUND_OPACITY);
        border.getElementStroke().setStrokeColor(SOCIETY_BORDER_COLOR);
        border.getElementStroke().setStrokeOpacity(SOCIETY_BACKGROUND_OPACITY);
        border.getElementStroke().setStrokeWidth(BORDER_CIRCLE_WIDTH);
        societyElements.add(border);

        //Circles
        final SvgCircle scoreCircle1 = new SvgCircle(76, 16, CIRCLE_RADIUS);
        scoreCircle1.getElementAttributes().setFill(SOCIETY_CIRCLE_COLOR_1);
        scoreCircle1.getElementStroke().setStrokeColor(SOCIETY_CIRCLE_BORDER_COLOR_1);
        scoreCircle1.getElementStroke().setStrokeWidth(CIRCLE_STROKE_WIDTH);
        societyElements.add(scoreCircle1);

        final SvgCircle scoreCircle2 = new SvgCircle(76, 61, CIRCLE_RADIUS);
        scoreCircle2.getElementAttributes().setFill(SOCIETY_CIRCLE_COLOR_2);
        scoreCircle2.getElementStroke().setStrokeColor(SOCIETY_CIRCLE_BORDER_COLOR_2);
        scoreCircle2.getElementStroke().setStrokeWidth(CIRCLE_STROKE_WIDTH);
        societyElements.add(scoreCircle2);

        final SvgCircle scoreCircle3 = new SvgCircle(76, 106, CIRCLE_RADIUS);
        scoreCircle3.getElementAttributes().setFill(SOCIETY_CIRCLE_COLOR_3);
        scoreCircle3.getElementStroke().setStrokeColor(SOCIETY_CIRCLE_BORDER_COLOR_3);
        scoreCircle3.getElementStroke().setStrokeWidth(CIRCLE_STROKE_WIDTH);
        societyElements.add(scoreCircle3);

        return societyElements;
    }

    private List<SvgAreaElement> generateVision() {
        final List<SvgAreaElement> visionElements = new ArrayList<>();

        //Line
        final SvgLine scoreLine = new SvgLine(VISION_LINE_COLOR, LINE_STROKE_WIDTH, 152, 48, 152, 116);
        visionElements.add(scoreLine);

        //Border
        final SvgCircle border = new SvgCircle(130, 10, BORDER_CIRCLE_RADIUS);
        border.getElementAttributes().setFill(EMPTY_BULLET_COLOR);
        border.getElementAttributes().setFillOpacity(VISION_BACKGROUND_OPACITY);
        border.getElementStroke().setStrokeColor(VISION_BORDER_COLOR);
        border.getElementStroke().setStrokeOpacity(VISION_BACKGROUND_OPACITY);
        border.getElementStroke().setStrokeWidth(BORDER_CIRCLE_WIDTH);
        visionElements.add(border);

        //Circles
        final SvgCircle scoreCircle1 = new SvgCircle(136, 16, CIRCLE_RADIUS);
        scoreCircle1.getElementAttributes().setFill(VISION_CIRCLE_COLOR_1);
        scoreCircle1.getElementStroke().setStrokeColor(VISION_CIRCLE_BORDER_COLOR_1);
        scoreCircle1.getElementStroke().setStrokeWidth(CIRCLE_STROKE_WIDTH);
        visionElements.add(scoreCircle1);

        final SvgCircle scoreCircle2 = new SvgCircle(136, 61, CIRCLE_RADIUS);
        scoreCircle2.getElementAttributes().setFill(VISION_CIRCLE_COLOR_2);
        scoreCircle2.getElementStroke().setStrokeColor(VISION_CIRCLE_BORDER_COLOR_2);
        scoreCircle2.getElementStroke().setStrokeWidth(CIRCLE_STROKE_WIDTH);
        visionElements.add(scoreCircle2);

        final SvgCircle scoreCircle3 = new SvgCircle(136, 106, CIRCLE_RADIUS);
        scoreCircle3.getElementAttributes().setFill(VISION_CIRCLE_COLOR_3);
        scoreCircle3.getElementStroke().setStrokeColor(VISION_CIRCLE_BORDER_COLOR_3);
        scoreCircle3.getElementStroke().setStrokeWidth(CIRCLE_STROKE_WIDTH);
        visionElements.add(scoreCircle3);

        return visionElements;
    }


    private List<SvgAreaElement> generateStrength() {
        final List<SvgAreaElement> strengthElements = new ArrayList<>();

        //Line
        final SvgLine scoreLine = new SvgLine(STRENGTH_LINE_COLOR, LINE_STROKE_WIDTH, 212, 48, 212, 116);
        strengthElements.add(scoreLine);

        //Border
        final SvgCircle border = new SvgCircle(190, 10, BORDER_CIRCLE_RADIUS);
        border.getElementAttributes().setFill(EMPTY_BULLET_COLOR);
        border.getElementAttributes().setFillOpacity(STRENGTH_BACKGROUND_OPACITY);
        border.getElementStroke().setStrokeColor(STRENGTH_BORDER_COLOR);
        border.getElementStroke().setStrokeOpacity(STRENGTH_BACKGROUND_OPACITY);
        border.getElementStroke().setStrokeWidth(BORDER_CIRCLE_WIDTH);
        strengthElements.add(border);

        //Circles
        final SvgCircle scoreCircle1 = new SvgCircle(196, 16, CIRCLE_RADIUS);
        scoreCircle1.getElementAttributes().setFill(STRENGTH_CIRCLE_COLOR_1);
        scoreCircle1.getElementStroke().setStrokeColor(STRENGTH_CIRCLE_BORDER_COLOR_1);
        scoreCircle1.getElementStroke().setStrokeWidth(CIRCLE_STROKE_WIDTH);
        strengthElements.add(scoreCircle1);

        final SvgCircle scoreCircle2 = new SvgCircle(196, 61, CIRCLE_RADIUS);
        scoreCircle2.getElementAttributes().setFill(STRENGTH_CIRCLE_COLOR_2);
        scoreCircle2.getElementStroke().setStrokeColor(STRENGTH_CIRCLE_BORDER_COLOR_2);
        scoreCircle2.getElementStroke().setStrokeWidth(CIRCLE_STROKE_WIDTH);
        strengthElements.add(scoreCircle2);

        final SvgCircle scoreCircle3 = new SvgCircle(196, 106, CIRCLE_RADIUS);
        scoreCircle3.getElementAttributes().setFill(STRENGTH_CIRCLE_COLOR_3);
        scoreCircle3.getElementStroke().setStrokeColor(STRENGTH_CIRCLE_BORDER_COLOR_3);
        scoreCircle3.getElementStroke().setStrokeWidth(CIRCLE_STROKE_WIDTH);
        strengthElements.add(scoreCircle3);

        return strengthElements;
    }


    private List<SvgAreaElement> generateStructure() {
        final List<SvgAreaElement> structureElements = new ArrayList<>();

        //Line
        final SvgLine line1 = new SvgLine(STRUCTURE_COLOR, LINE_STROKE_WIDTH, 32, 203, 32, 214);
        structureElements.add(line1);

        //Circles
        final SvgRectangle scoreRectangle1 = new SvgRectangle(16, 169, SQUARE_LENGTH, SQUARE_LENGTH);
        scoreRectangle1.getElementAttributes().setFill(STRUCTURE_CIRCLE_COLOR_1);
        scoreRectangle1.getElementStroke().setStrokeColor(STRUCTURE_COLOR);
        scoreRectangle1.getElementStroke().setStrokeWidth(CIRCLE_STROKE_WIDTH);
        structureElements.add(scoreRectangle1);

        final SvgRectangle scoreRectangle2 = new SvgRectangle(16, 214, SQUARE_LENGTH, SQUARE_LENGTH);
        scoreRectangle2.getElementAttributes().setFill(STRUCTURE_CIRCLE_COLOR_2);
        scoreRectangle2.getElementStroke().setStrokeColor(STRUCTURE_COLOR);
        scoreRectangle2.getElementStroke().setStrokeWidth(CIRCLE_STROKE_WIDTH);
        structureElements.add(scoreRectangle2);

        return structureElements;
    }

    private List<SvgAreaElement> generateInspiration() {
        final List<SvgAreaElement> inspirationElements = new ArrayList<>();

        //Line
        final SvgLine line1 = new SvgLine(INSPIRATION_COLOR, LINE_STROKE_WIDTH, 92, 203, 92, 214);
        inspirationElements.add(line1);

        //Circles
        final SvgCircle scoreCircle1 = new SvgCircle(76, 169, CIRCLE_RADIUS);
        scoreCircle1.getElementAttributes().setFill(INSPIRATION_CIRCLE_COLOR_1);
        scoreCircle1.getElementStroke().setStrokeColor(INSPIRATION_COLOR);
        scoreCircle1.getElementStroke().setStrokeWidth(BORDER_CIRCLE_WIDTH);
        inspirationElements.add(scoreCircle1);

        final SvgCircle scoreCircle2 = new SvgCircle(76, 214, CIRCLE_RADIUS);
        scoreCircle2.getElementAttributes().setFill(INSPIRATION_CIRCLE_COLOR_2);
        scoreCircle2.getElementStroke().setStrokeColor(INSPIRATION_COLOR);
        scoreCircle2.getElementStroke().setStrokeWidth(BORDER_CIRCLE_WIDTH);
        inspirationElements.add(scoreCircle2);

        return inspirationElements;
    }

    private List<SvgAreaElement> generateStructureInspiration() {
        final List<SvgAreaElement> structureInspirationElements = new ArrayList<>();

        //Line
        final SvgLine line1 = new SvgLine(null, LINE_STROKE_WIDTH, 33, 208, 93, 208);
        line1.setGradient(new SvgGradient(
                new SvgGradientStop(STRUCTURE_COLOR, FULL_ALPHA, 0.0),
                new SvgGradientStop(INSPIRATION_COLOR, FULL_ALPHA, 1.0)));
        structureInspirationElements.add(line1);

        return structureInspirationElements;
    }

    private List<SvgAreaElement> generateAdaptability() {
        final List<SvgAreaElement> adaptabilityElements = new ArrayList<>();

        //Line
        final SvgLine line1 = new SvgLine(ADAPTABILITY_COLOR, LINE_STROKE_WIDTH, 152, 203, 152, 214);
        adaptabilityElements.add(line1);

        //Circles
        final SvgRectangle scoreRectangle1 = new SvgRectangle(136, 169, SQUARE_LENGTH, SQUARE_LENGTH);
        scoreRectangle1.getElementAttributes().setFill(ADAPTABILITY_CIRCLE_COLOR_1);
        scoreRectangle1.getElementStroke().setStrokeColor(ADAPTABILITY_COLOR);
        scoreRectangle1.getElementStroke().setStrokeWidth(CIRCLE_STROKE_WIDTH);
        adaptabilityElements.add(scoreRectangle1);

        final SvgRectangle scoreRectangle2 = new SvgRectangle(136, 214, SQUARE_LENGTH, SQUARE_LENGTH);
        scoreRectangle2.getElementAttributes().setFill(ADAPTABILITY_CIRCLE_COLOR_2);
        scoreRectangle2.getElementStroke().setStrokeColor(ADAPTABILITY_COLOR);
        scoreRectangle2.getElementStroke().setStrokeWidth(CIRCLE_STROKE_WIDTH);
        adaptabilityElements.add(scoreRectangle2);

        return adaptabilityElements;
    }

    private List<SvgAreaElement> generateAction() {
        final List<SvgAreaElement> actionElements = new ArrayList<>();

        //Line
        final SvgLine line1 = new SvgLine(ACTION_COLOR, LINE_STROKE_WIDTH, 212, 203, 212, 214);
        actionElements.add(line1);

        //Circles
        final SvgCircle scoreCircle1 = new SvgCircle(196, 169, CIRCLE_RADIUS);
        scoreCircle1.getElementAttributes().setFill(ACTION_CIRCLE_COLOR_1);
        scoreCircle1.getElementStroke().setStrokeColor(ACTION_COLOR);
        scoreCircle1.getElementStroke().setStrokeWidth(CIRCLE_STROKE_WIDTH);
        actionElements.add(scoreCircle1);

        final SvgCircle scoreCircle2 = new SvgCircle(196, 214, CIRCLE_RADIUS);
        scoreCircle2.getElementAttributes().setFill(ACTION_CIRCLE_COLOR_2);
        scoreCircle2.getElementStroke().setStrokeColor(ACTION_COLOR);
        scoreCircle2.getElementStroke().setStrokeWidth(CIRCLE_STROKE_WIDTH);
        actionElements.add(scoreCircle2);

        return actionElements;
    }

    private List<SvgAreaElement> generateAdaptabilityAction() {
        final List<SvgAreaElement> adaptabilityActionElements = new ArrayList<>();
        //Line
        final SvgLine line1 = new SvgLine(null, LINE_STROKE_WIDTH, 153, 208, 213, 208);
        line1.setGradient(new SvgGradient(
                new SvgGradientStop(ADAPTABILITY_COLOR, FULL_ALPHA, 0.0),
                new SvgGradientStop(ACTION_COLOR, FULL_ALPHA, 1.0)));
        adaptabilityActionElements.add(line1);
        return adaptabilityActionElements;
    }


    private List<SvgAreaElement> generateMaterialAttachment() {
        final List<SvgAreaElement> materialAttachmentElements = new ArrayList<>();

        //Line
        final SvgLine scoreLine = new SvgLine(MATERIAL_ATTACHMENT_LINE_COLOR, LINE_STROKE_WIDTH, 32, 310, 32, 378);
        materialAttachmentElements.add(scoreLine);

        //Border
        final SvgRectangle border = new SvgRectangle(10, 272, SQUARE_BORDER_LENGTH, SQUARE_BORDER_LENGTH);
        border.getElementAttributes().setFill(EMPTY_BULLET_COLOR);
        border.getElementAttributes().setFillOpacity(MATERIAL_ATTACHMENT_BACKGROUND_OPACITY);
        border.getElementStroke().setStrokeColor(MATERIAL_ATTACHMENT_BORDER_COLOR);
        border.getElementStroke().setStrokeWidth(CIRCLE_STROKE_WIDTH);
        border.getElementStroke().setStrokeOpacity(MATERIAL_ATTACHMENT_BACKGROUND_OPACITY);
        border.getElementStroke().setStrokeAlign(StrokeAlign.OUTSET);
        materialAttachmentElements.add(border);

        //Circles
        final SvgRectangle scoreSquare1 = new SvgRectangle(16, 278, SQUARE_LENGTH, SQUARE_LENGTH);
        scoreSquare1.getElementAttributes().setFill(MATERIAL_ATTACHMENT_CIRCLE_COLOR_1);
        scoreSquare1.getElementStroke().setStrokeColor(MATERIAL_ATTACHMENT_CIRCLE_BORDER_COLOR_1);
        scoreSquare1.getElementStroke().setStrokeWidth(CIRCLE_STROKE_WIDTH);
        materialAttachmentElements.add(scoreSquare1);

        final SvgRectangle scoreSquare2 = new SvgRectangle(16, 323, SQUARE_LENGTH, SQUARE_LENGTH);
        scoreSquare2.getElementAttributes().setFill(MATERIAL_ATTACHMENT_CIRCLE_COLOR_2);
        scoreSquare2.getElementStroke().setStrokeColor(MATERIAL_ATTACHMENT_CIRCLE_BORDER_COLOR_2);
        scoreSquare2.getElementStroke().setStrokeWidth(CIRCLE_STROKE_WIDTH);
        materialAttachmentElements.add(scoreSquare2);

        final SvgRectangle scoreSquare3 = new SvgRectangle(16, 368, SQUARE_LENGTH, SQUARE_LENGTH);
        scoreSquare3.getElementAttributes().setFill(MATERIAL_ATTACHMENT_CIRCLE_COLOR_3);
        scoreSquare3.getElementStroke().setStrokeColor(MATERIAL_ATTACHMENT_CIRCLE_BORDER_COLOR_3);
        scoreSquare3.getElementStroke().setStrokeWidth(CIRCLE_STROKE_WIDTH);
        materialAttachmentElements.add(scoreSquare3);

        return materialAttachmentElements;
    }

    private List<SvgAreaElement> generateCommunication() {
        final List<SvgAreaElement> communicationElements = new ArrayList<>();

        //Line
        final SvgLine scoreLine = new SvgLine(COMMUNICATION_LINE_COLOR, LINE_STROKE_WIDTH, 92, 310, 92, 378);
        communicationElements.add(scoreLine);

        //Border
        final SvgRectangle border = new SvgRectangle(70, 272, SQUARE_BORDER_LENGTH, SQUARE_BORDER_LENGTH);
        border.getElementAttributes().setFill(EMPTY_BULLET_COLOR);
        border.getElementAttributes().setFillOpacity(COMMUNICATION_BACKGROUND_OPACITY);
        border.getElementStroke().setStrokeColor(COMMUNICATION_BORDER_COLOR);
        border.getElementStroke().setStrokeWidth(CIRCLE_STROKE_WIDTH);
        border.getElementStroke().setStrokeOpacity(COMMUNICATION_BACKGROUND_OPACITY);
        border.getElementStroke().setStrokeAlign(StrokeAlign.OUTSET);
        communicationElements.add(border);

        //Circles
        final SvgRectangle scoreSquare1 = new SvgRectangle(76, 278, SQUARE_LENGTH, SQUARE_LENGTH);
        scoreSquare1.getElementAttributes().setFill(COMMUNICATION_CIRCLE_COLOR_1);
        scoreSquare1.getElementStroke().setStrokeColor(COMMUNICATION_CIRCLE_BORDER_COLOR_1);
        scoreSquare1.getElementStroke().setStrokeWidth(CIRCLE_STROKE_WIDTH);
        communicationElements.add(scoreSquare1);

        final SvgRectangle scoreSquare2 = new SvgRectangle(76, 323, SQUARE_LENGTH, SQUARE_LENGTH);
        scoreSquare2.getElementAttributes().setFill(COMMUNICATION_CIRCLE_COLOR_2);
        scoreSquare2.getElementStroke().setStrokeColor(COMMUNICATION_CIRCLE_BORDER_COLOR_2);
        scoreSquare2.getElementStroke().setStrokeWidth(CIRCLE_STROKE_WIDTH);
        communicationElements.add(scoreSquare2);

        final SvgRectangle scoreSquare3 = new SvgRectangle(76, 368, SQUARE_LENGTH, SQUARE_LENGTH);
        scoreSquare3.getElementAttributes().setFill(COMMUNICATION_CIRCLE_COLOR_3);
        scoreSquare3.getElementStroke().setStrokeColor(COMMUNICATION_CIRCLE_BORDER_COLOR_3);
        scoreSquare3.getElementStroke().setStrokeWidth(CIRCLE_STROKE_WIDTH);
        communicationElements.add(scoreSquare3);

        return communicationElements;
    }

    private List<SvgAreaElement> generateSelfAware() {
        final List<SvgAreaElement> selfAwareElements = new ArrayList<>();

        //Line
        final SvgLine scoreLine = new SvgLine(SELF_AWARE_LINE_COLOR, LINE_STROKE_WIDTH, 152, 310, 152, 378);
        selfAwareElements.add(scoreLine);

        //Border
        final SvgRectangle border = new SvgRectangle(130, 272, SQUARE_BORDER_LENGTH, SQUARE_BORDER_LENGTH);
        border.getElementAttributes().setFill(EMPTY_BULLET_COLOR);
        border.getElementAttributes().setFillOpacity(SELF_AWARE_BACKGROUND_OPACITY);
        border.getElementStroke().setStrokeColor(SELF_AWARE_BORDER_COLOR);
        border.getElementStroke().setStrokeWidth(CIRCLE_STROKE_WIDTH);
        border.getElementStroke().setStrokeOpacity(SELF_AWARE_BACKGROUND_OPACITY);
        border.getElementStroke().setStrokeAlign(StrokeAlign.OUTSET);
        selfAwareElements.add(border);

        //Circles
        final SvgRectangle scoreSquare1 = new SvgRectangle(136, 278, SQUARE_LENGTH, SQUARE_LENGTH);
        scoreSquare1.getElementAttributes().setFill(SELF_AWARE_CIRCLE_COLOR_1);
        scoreSquare1.getElementStroke().setStrokeColor(SELF_AWARE_CIRCLE_BORDER_COLOR_1);
        scoreSquare1.getElementStroke().setStrokeWidth(CIRCLE_STROKE_WIDTH);
        selfAwareElements.add(scoreSquare1);

        final SvgRectangle scoreSquare2 = new SvgRectangle(136, 323, SQUARE_LENGTH, SQUARE_LENGTH);
        scoreSquare2.getElementAttributes().setFill(SELF_AWARE_CIRCLE_COLOR_2);
        scoreSquare2.getElementStroke().setStrokeColor(SELF_AWARE_CIRCLE_BORDER_COLOR_2);
        scoreSquare2.getElementStroke().setStrokeWidth(CIRCLE_STROKE_WIDTH);
        selfAwareElements.add(scoreSquare2);

        final SvgRectangle scoreSquare3 = new SvgRectangle(136, 368, SQUARE_LENGTH, SQUARE_LENGTH);
        scoreSquare3.getElementAttributes().setFill(SELF_AWARE_CIRCLE_COLOR_3);
        scoreSquare3.getElementStroke().setStrokeColor(SELF_AWARE_CIRCLE_BORDER_COLOR_3);
        scoreSquare3.getElementStroke().setStrokeWidth(CIRCLE_STROKE_WIDTH);
        selfAwareElements.add(scoreSquare3);

        return selfAwareElements;
    }

    private List<SvgAreaElement> generateAnalysis() {
        final List<SvgAreaElement> analysisElements = new ArrayList<>();

        //Line
        final SvgLine scoreLine = new SvgLine(ANALYSIS_LINE_COLOR, LINE_STROKE_WIDTH, 212, 310, 212, 378);
        analysisElements.add(scoreLine);

        //Border
        final SvgRectangle border = new SvgRectangle(190, 272, SQUARE_BORDER_LENGTH, SQUARE_BORDER_LENGTH);
        border.getElementAttributes().setFill(EMPTY_BULLET_COLOR);
        border.getElementAttributes().setFillOpacity(ANALYSIS_BACKGROUND_OPACITY);
        border.getElementStroke().setStrokeColor(ANALYSIS_BORDER_COLOR);
        border.getElementStroke().setStrokeWidth(CIRCLE_STROKE_WIDTH);
        border.getElementStroke().setStrokeOpacity(ANALYSIS_BACKGROUND_OPACITY);
        border.getElementStroke().setStrokeAlign(StrokeAlign.OUTSET);
        analysisElements.add(border);

        //Circles
        final SvgRectangle scoreSquare1 = new SvgRectangle(196, 278, SQUARE_LENGTH, SQUARE_LENGTH);
        scoreSquare1.getElementAttributes().setFill(ANALYSIS_CIRCLE_COLOR_1);
        scoreSquare1.getElementStroke().setStrokeColor(ANALYSIS_CIRCLE_BORDER_COLOR_1);
        scoreSquare1.getElementStroke().setStrokeWidth(CIRCLE_STROKE_WIDTH);
        analysisElements.add(scoreSquare1);

        final SvgRectangle scoreSquare2 = new SvgRectangle(196, 323, SQUARE_LENGTH, SQUARE_LENGTH);
        scoreSquare2.getElementAttributes().setFill(ANALYSIS_CIRCLE_COLOR_2);
        scoreSquare2.getElementStroke().setStrokeColor(ANALYSIS_CIRCLE_BORDER_COLOR_2);
        scoreSquare2.getElementStroke().setStrokeWidth(CIRCLE_STROKE_WIDTH);
        analysisElements.add(scoreSquare2);

        final SvgRectangle scoreSquare3 = new SvgRectangle(196, 368, SQUARE_LENGTH, SQUARE_LENGTH);
        scoreSquare3.getElementAttributes().setFill(ANALYSIS_CIRCLE_COLOR_3);
        scoreSquare3.getElementStroke().setStrokeColor(ANALYSIS_CIRCLE_BORDER_COLOR_3);
        scoreSquare3.getElementStroke().setStrokeWidth(CIRCLE_STROKE_WIDTH);
        analysisElements.add(scoreSquare3);

        return analysisElements;
    }


    @BeforeClass
    public void prepareFolder() throws IOException {
        Files.createDirectories(Paths.get(OUTPUT_FOLDER));
    }

    private List<SvgAreaElement> generateContent() {
        final List<SvgAreaElement> elements = new ArrayList<>();

        elements.addAll(generateUniversal());
        elements.addAll(generateSociety());
        elements.addAll(generateVision());
        elements.addAll(generateStrength());

        elements.addAll(generateStructureInspiration());
        elements.addAll(generateStructure());
        elements.addAll(generateInspiration());
        elements.addAll(generateAdaptabilityAction());
        elements.addAll(generateAdaptability());
        elements.addAll(generateAction());

        elements.addAll(generateMaterialAttachment());
        elements.addAll(generateCommunication());
        elements.addAll(generateSelfAware());
        elements.addAll(generateAnalysis());
        return elements;
    }

    @Test
    public void generateCADT() throws IOException {
        cadtTemplate = new SvgTemplate();
        cadtTemplate.getElementAttributes().setWidth(246);
        cadtTemplate.getElementAttributes().setHeight(412);
        cadtTemplate.setDocumentSize(false);

        cadtTemplate.addElements(generateContent());
    }

    @Test(dependsOnMethods = "generateCADT")
    public void executeCadt() throws IOException, InvalidXmlElementException, EmptyPdfBodyException {
        final DroolsSubmittedForm droolsSubmittedForm = DroolsSubmittedForm.getFromJson(FileReader.getResource(DROOLS_FORM_FILE_PATH, StandardCharsets.UTF_8));
        final List<String> svgResults = droolsResultController.execute(droolsSubmittedForm, Collections.singletonList(cadtTemplate));
        Assert.assertEquals(svgResults.size(), 1);

        try (PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(OUTPUT_FOLDER
                + File.separator + "cadtProfileCreatorFromDrools.svg")), true)) {
            out.println(svgResults.get(0));
        }

        checkContent(svgResults.get(0), "cadtProfileCreatorFromDrools.svg");

        final String filePath = OUTPUT_FOLDER + File.separator + "cadtProfileCreatorFromDrools.pdf";
        final File destFile = new File(filePath);
        destFile.createNewFile();
        try (FileOutputStream fos = new FileOutputStream(filePath)) {
            fos.write(pdfController.generatePdfFromSvgs(svgResults));
        }
    }

    @Test(dependsOnMethods = "generateCADT")
    public void checkSerialization() throws JsonProcessingException {
        //cadtTemplate.toJson() is what must be deployed into the infographic docker container
        SvgTemplate svgTemplate1 = SvgTemplate.fromJson(cadtTemplate.toJson());
        Assert.assertEquals(SvgGenerator.generate(svgTemplate1), SvgGenerator.generate(cadtTemplate));
    }

    @AfterClass
    public void removeFolder() {
        Assert.assertTrue(deleteDirectory(new File(OUTPUT_FOLDER)));
    }
}
