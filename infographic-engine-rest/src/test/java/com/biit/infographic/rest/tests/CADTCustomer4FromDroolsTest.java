package com.biit.infographic.rest.tests;

import com.biit.drools.form.DroolsSubmittedForm;
import com.biit.infographic.core.engine.SvgFromDroolsConverter;
import com.biit.infographic.core.generators.SvgGenerator;
import com.biit.infographic.core.models.svg.SvgAreaElement;
import com.biit.infographic.core.models.svg.SvgBackground;
import com.biit.infographic.core.models.svg.SvgEmbedded;
import com.biit.infographic.core.models.svg.SvgTemplate;
import com.biit.infographic.core.models.svg.VerticalAlignment;
import com.biit.infographic.core.models.svg.components.SvgCircle;
import com.biit.infographic.core.models.svg.components.SvgImage;
import com.biit.infographic.core.models.svg.components.SvgLine;
import com.biit.infographic.core.models.svg.components.SvgRectangle;
import com.biit.infographic.core.models.svg.components.gradient.SvgGradient;
import com.biit.infographic.core.models.svg.components.gradient.SvgGradientStop;
import com.biit.infographic.core.models.svg.components.text.FontFactory;
import com.biit.infographic.core.models.svg.components.text.FontWeight;
import com.biit.infographic.core.models.svg.components.text.SvgText;
import com.biit.infographic.core.models.svg.components.text.TextAlign;
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
import java.io.FileNotFoundException;
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
@Test(groups = "cadtCustomer4")
public class CADTCustomer4FromDroolsTest extends AbstractTestNGSpringContextTests {
    protected static final String OUTPUT_FOLDER = System.getProperty("java.io.tmpdir") + File.separator + "SvgTests";

    private static final String DROOLS_FORM_FILE_PATH = "drools/DroolsSubmittedCadtCustomer_4.json";

    private static final String TITLE = "YOUR CADT PROFILE";
    private static final String INTRODUCTION_CONTENT = "The result of the CADT assessment is the personal CADT profile, which is a guide of your actual situation and of the further improvements you can work " +
            "on. The following is labeled so you can easily follow and remember which was your starting point, and it is close to the communication way you are going to " +
            "follow with your practitioner from now on.";


    private static final Double FULL_ALPHA = 1.0D;

    private static final String BORDER_COLOR = "b49057";

    private static final Double DEFAULT_STROKE_WIDTH = 8D;

    private static final int INTRODUCTION_TEXTS_WIDTHS = 2182;


    private static final String FIRST_BULLET_COLOR = "919ee1";
    private static final String SECOND_BULLET_COLOR = "e9a197";
    private static final String THIRD_BULLET_COLOR = "7ccadf";
    private static final String FORTH_BULLET_COLOR = "8bc4ab";
    private static final String EMPTY_BULLET_COLOR = "ffffff";

    private static final String DISABLED_BULLET_COLOR = "d3d4d4";

    private static final String UNIVERSAL_BORDER_COLOR = "#DROOLS%CADT_Customer_4%UniversalBorderColor#";
    private static final String UNIVERSAL_BACKGROUND_OPACITY = "#DROOLS%CADT_Customer_4%UniversalBorderBackgroundOpacity#";
    private static final String UNIVERSAL_COLOR = "#DROOLS%CADT_Customer_4%UniversalColor#";
    private static final String RECEPTIVE_COLOR = "#DROOLS%CADT_Customer_4%ReceptiveColor#";
    private static final String INTERPERSONAL_SENSITIVITY_COLOR = "#DROOLS%CADT_Customer_4%InterpersonalSensitivityColor#";
    private static final String MULTICULTURAL_SENSITIVITY_COLOR = "#DROOLS%CADT_Customer_4%MulticulturalSensitivityColor#";
    private static final String UNIVERSAL_ICON = "images/CADT/Universal.svg";


    private static final String SOCIETY_COLOR = "#DROOLS%CADT_Customer_4%SocietyColor#";
    private static final String SOCIETY_BORDER_COLOR = "#DROOLS%CADT_Customer_4%SocietyBorderColor#";
    private static final String SOCIETY_BACKGROUND_OPACITY = "#DROOLS%CADT_Customer_4%SocietyBorderBackgroundOpacity#";
    private static final String INNOVATOR_COLOR = "#DROOLS%CADT_Customer_4%InnovatorColor#";
    private static final String COOPERATION_COLOR = "#DROOLS%CADT_Customer_4%CooperationColor#";
    private static final String INNOVATION_COLOR = "#DROOLS%CADT_Customer_4%InnovationColor#";
    private static final String SOCIETY_ICON = "images/CADT/Society.svg";


    private static final String VISION_COLOR = "#DROOLS%CADT_Customer_4%VisionColor#";
    private static final String VISION_BORDER_COLOR = "#DROOLS%CADT_Customer_4%VisionBorderColor#";
    private static final String VISION_BACKGROUND_OPACITY = "#DROOLS%CADT_Customer_4%VisionBorderBackgroundOpacity#";
    private static final String VISIONARY_COLOR = "#DROOLS%CADT_Customer_4%VisionaryColor#";
    private static final String PERSUASIVENESS_COLOR = "#DROOLS%CADT_Customer_4%PersuasivenessColor#";
    private static final String FUTURE_COLOR = "#DROOLS%CADT_Customer_4%FutureColor#";
    private static final String VISION_ICON = "images/CADT/Vision.svg";


    private static final String STRENGTH_COLOR = "#DROOLS%CADT_Customer_4%StrengthColor#";
    private static final String STRENGTH_BORDER_COLOR = "#DROOLS%CADT_Customer_4%StrengthBorderColor#";
    private static final String STRENGTH_BACKGROUND_OPACITY = "#DROOLS%CADT_Customer_4%StrengthBorderBackgroundOpacity#";
    private static final String STRATEGIST_COLOR = "#DROOLS%CADT_Customer_4%StrategistColor#";
    private static final String DECISIVENESS_COLOR = "#DROOLS%CADT_Customer_4%DecisivenessColor#";
    private static final String JUDGEMENT_COLOR = "#DROOLS%CADT_Customer_4%JudgementColor#";
    private static final String STRENGTH_ICON = "images/CADT/Strength.svg";


    private static final String STRUCTURE_COLOR = FORTH_BULLET_COLOR;
    private static final String DISCIPLINE_COLOR = "#DROOLS%CADT_Customer_4%DisicplineColor#";
    private static final String GOAL_SETTING_COLOR = "#DROOLS%CADT_Customer_4%GoalSettingColor#";
    private static final String STRUCTURE_ICON = "images/CADT/Structure.svg";

    private static final String INSPIRATION_COLOR = FIRST_BULLET_COLOR;
    private static final String CONSCIENTIOUSNESS_COLOR = "#DROOLS%CADT_Customer_4%ConscientiousnessColor#";
    private static final String ENGAGEMENT_COLOR = "#DROOLS%CADT_Customer_4%EngagementColor#";
    private static final String INSPIRATION_ICON = "images/CADT/Inspiration.svg";


    private static final String ADAPTABILITY_COLOR = THIRD_BULLET_COLOR;
    private static final String BUILDING_AND_MAINTAINING_COLOR = "#DROOLS%CADT_Customer_4%BuildingAndMaintainingColor#";
    private static final String FLEXIBILITY_COLOR = "#DROOLS%CADT_Customer_4%FlexibilityColor#";
    private static final String ADAPTABILITY_ICON = "images/CADT/Adaptability.svg";


    private static final String ACTION_COLOR = SECOND_BULLET_COLOR;
    private static final String DIRECTION_COLOR = "#DROOLS%CADT_Customer_4%DirectionColor#";
    private static final String INITIATIVE_COLOR = "#DROOLS%CADT_Customer_4%InitiativeColor#";
    private static final String ACTION_ICON = "images/CADT/Action.svg";


    private static final String MATERIAL_ATTACHMENT_COLOR = "#DROOLS%CADT_Customer_4%MaterialAttachmentColor#";
    private static final String MATERIAL_ATTACHMENT_BORDER_COLOR = "#DROOLS%CADT_Customer_4%MaterialAttachmentBorderColor#";
    private static final String MATERIAL_ATTACHMENT_BACKGROUND_OPACITY = "#DROOLS%CADT_Customer_4%MaterialAttachmentBorderBackgroundOpacity#";
    private static final String BANKER_COLOR = "#DROOLS%CADT_Customer_4%BankerColor#";
    private static final String BUSINESS_MINDED_COLOR = "#DROOLS%CADT_Customer_4%BusinessMindedColor#";
    private static final String TENACITY_COLOR = "#DROOLS%CADT_Customer_4%TenacityColor#";
    private static final String MATERIAL_ATTACHMENT_ICON = "images/CADT/MaterialAttachment.svg";


    private static final String COMMUNICATION_COLOR = "#DROOLS%CADT_Customer_4%CommunicationColor#";
    private static final String COMMUNICATION_BORDER_COLOR = "#DROOLS%CADT_Customer_4%CommunicationBorderColor#";
    private static final String COMMUNICATION_BACKGROUND_OPACITY = "#DROOLS%CADT_Customer_4%CommunicationBorderBackgroundOpacity#";
    private static final String SALESMAN_COLOR = "#DROOLS%CADT_Customer_4%SalesmanColor#";
    private static final String COMMUNICATION_SKILLS_COLOR = "#DROOLS%CADT_Customer_4%CommunicationSkillsColor#";
    private static final String CLIENT_ORIENTED_COLOR = "#DROOLS%CADT_Customer_4%ClientOrientedColor#";
    private static final String COMMUNICATION_ICON = "images/CADT/Communication.svg";

    private static final String SELF_AWARE_COLOR = "#DROOLS%CADT_Customer_4%SelfAwareColor#";
    private static final String SELF_AWARE_BORDER_COLOR = "#DROOLS%CADT_Customer_4%SelfAwareBorderColor#";
    private static final String SELF_AWARE_BACKGROUND_OPACITY = "#DROOLS%CADT_Customer_4%SelfAwareBorderBackgroundOpacity#";
    private static final String LEADER_COLOR = "#DROOLS%CADT_Customer_4%LeaderColor#";
    private static final String LEADERSHIP_COLOR = "#DROOLS%CADT_Customer_4%LeadershipColor#";
    private static final String INDEPENDENCE_COLOR = "#DROOLS%CADT_Customer_4%IndependenceColor#";
    private static final String SELF_AWARE_ICON = "images/CADT/SelfAware.svg";


    private static final String ANALYSIS_COLOR = "#DROOLS%CADT_Customer_4%AnalysisColor#";
    private static final String ANALYSIS_BORDER_COLOR = "#DROOLS%CADT_Customer_4%AnalysisBorderColor#";
    private static final String ANALYSIS_BACKGROUND_OPACITY = "#DROOLS%CADT_Customer_4%AnalysisBorderBackgroundOpacity#";
    private static final String SCIENTIST_COLOR = "#DROOLS%CADT_Customer_4%ScientistColor#";
    private static final String PROBLEM_ANALYSIS_COLOR = "#DROOLS%CADT_Customer_4%ProblemAnalysisColor#";
    private static final String PLANING_AND_ORGANIZING_COLOR = "#DROOLS%CADT_Customer_4%PlaningAndOrganizationColor#";
    private static final String ANALYSIS_ICON = "images/CADT/Analysis.svg";


    private static final String INTELLECTUAL_PROPERTY_DISCLAIM_COLOR = "d3d4d4ff";

    @Autowired
    private SvgFromDroolsConverter svgFromDroolsConverter;

    private SvgTemplate cadtTemplate;

    private void checkContent(String content, String resourceFile) {
        try {
            Assert.assertEquals(content.trim(), new String(Files.readAllBytes(Paths.get(getClass().getClassLoader()
                    .getResource("svg" + File.separator + resourceFile).toURI()))).trim());
        } catch (IOException | URISyntaxException e) {
            Assert.fail();
        }
    }

    protected boolean deleteDirectory(File directoryToBeDeleted) {
        File[] allContents = directoryToBeDeleted.listFiles();
        if (allContents != null) {
            for (File file : allContents) {
                deleteDirectory(file);
            }
        }
        return directoryToBeDeleted.delete();
    }


    private SvgBackground generateBackground() {
        final SvgBackground svgBackground = new SvgBackground();
        svgBackground.setBackgroundColor("#ffffff");
        return svgBackground;
    }

    private List<SvgAreaElement> generateHeader() {
        final List<SvgAreaElement> elements = new ArrayList<>();


        //Title background
        final SvgRectangle nameRectangle = new SvgRectangle(612L, 188L, "1260L", "127px", "ffffff");
        nameRectangle.getElementStroke().setStrokeColor(BORDER_COLOR);
        nameRectangle.getElementStroke().setStrokeWidth(7.07);
        nameRectangle.setXRadius(25L);
        nameRectangle.setYRadius(30L);
        elements.add(nameRectangle);

        final SvgText title = new SvgText(TITLE, 40, 1052L, 239L);
        title.setFontFamily("Arial, sans-serif");
        title.setFontWeight(FontWeight.BOLD);
        elements.add(title);

        //Logo
        final SvgImage logo = new SvgImage();
        logo.setId("logo");
        logo.setFromPath("images/BIIT-logo.png");
        logo.getElementAttributes().setXCoordinate(2052L);
        logo.getElementAttributes().setYCoordinate(67L);
        logo.getElementAttributes().setWidth(282L);
        logo.getElementAttributes().setHeight(254L);
        elements.add(logo);


        return elements;
    }

    private List<SvgAreaElement> generateIntroduction() {
        final List<SvgAreaElement> elements = new ArrayList<>();

        final SvgText text = new SvgText("Arial", INTRODUCTION_CONTENT, 32, 147L, 408L);
        text.setMaxParagraphHeight(188);
        text.setMaxLineWidth(INTRODUCTION_TEXTS_WIDTHS);
        text.setTextAlign(TextAlign.LEFT);
        elements.add(text);

        return elements;
    }

    private List<SvgAreaElement> generateSelectedExample() {
        final List<SvgAreaElement> elements = new ArrayList<>();

        final SvgText selectedText = new SvgText("Arial", "You are doing fine", 32, 452L, 693L);
        elements.add(selectedText);

        final SvgCircle scoreCircle1 = new SvgCircle(738L, 655L, 47L);
        scoreCircle1.getElementAttributes().setFill(FIRST_BULLET_COLOR);
        elements.add(scoreCircle1);

        final SvgCircle scoreCircle2 = new SvgCircle(856L, 655L, 47L);
        scoreCircle2.getElementAttributes().setFill(SECOND_BULLET_COLOR);
        elements.add(scoreCircle2);

        final SvgCircle scoreCircle3 = new SvgCircle(973L, 655L, 47L);
        scoreCircle3.getElementAttributes().setFill(THIRD_BULLET_COLOR);
        elements.add(scoreCircle3);

        final SvgCircle scoreCircle4 = new SvgCircle(1090L, 655L, 47L);
        scoreCircle4.getElementAttributes().setFill(FORTH_BULLET_COLOR);
        elements.add(scoreCircle4);

        final SvgCircle emptyCircle1 = new SvgCircle(1332L, 655L, 47L);
        emptyCircle1.getElementAttributes().setFill(EMPTY_BULLET_COLOR);
        emptyCircle1.getElementStroke().setStrokeWidth(DEFAULT_STROKE_WIDTH);
        emptyCircle1.getElementStroke().setStrokeColor(FIRST_BULLET_COLOR);
        elements.add(emptyCircle1);

        final SvgCircle emptyCircle2 = new SvgCircle(1449L, 655L, 47L);
        emptyCircle2.getElementAttributes().setFill(EMPTY_BULLET_COLOR);
        emptyCircle2.getElementStroke().setStrokeWidth(DEFAULT_STROKE_WIDTH);
        emptyCircle2.getElementStroke().setStrokeColor(SECOND_BULLET_COLOR);
        elements.add(emptyCircle2);

        final SvgCircle emptyCircle3 = new SvgCircle(1567L, 655L, 47L);
        emptyCircle3.getElementAttributes().setFill(EMPTY_BULLET_COLOR);
        emptyCircle3.getElementStroke().setStrokeWidth(DEFAULT_STROKE_WIDTH);
        emptyCircle3.getElementStroke().setStrokeColor(THIRD_BULLET_COLOR);
        elements.add(emptyCircle3);

        final SvgCircle emptyCircle4 = new SvgCircle(1684L, 655L, 47L);
        emptyCircle4.getElementAttributes().setFill(EMPTY_BULLET_COLOR);
        emptyCircle4.getElementStroke().setStrokeWidth(DEFAULT_STROKE_WIDTH);
        emptyCircle4.getElementStroke().setStrokeColor(FORTH_BULLET_COLOR);
        elements.add(emptyCircle4);

        final SvgText notSelectedText = new SvgText("Arial", "You have room for improvement", 32, 1808L, 693L);
        elements.add(notSelectedText);

        final SvgCircle scoreCircle5 = new SvgCircle(914L, 802L, 47L);
        scoreCircle5.getElementAttributes().setFill(DISABLED_BULLET_COLOR);
        elements.add(scoreCircle5);

        final SvgText attentionText = new SvgText("Arial", "You need to pay attention", 32, 349L, 840L);
        elements.add(attentionText);

        final SvgCircle emptyCircle5 = new SvgCircle(1508L, 802L, 47L);
        emptyCircle5.getElementAttributes().setFill(EMPTY_BULLET_COLOR);
        emptyCircle5.getElementStroke().setStrokeWidth(DEFAULT_STROKE_WIDTH);
        emptyCircle5.getElementStroke().setStrokeColor(DISABLED_BULLET_COLOR);
        elements.add(emptyCircle5);

        final SvgText skippingText = new SvgText("Arial", "You are skipping pitfalls", 32, 1808L, 840L);
        elements.add(skippingText);

        return elements;
    }

    private List<SvgAreaElement> generateUniversalElements() throws FileNotFoundException {
        final List<SvgAreaElement> elements = new ArrayList<>();

        final SvgText groupTitle = new SvgText("Sofia Sans Extra Condensed", "UNIVERSAL", 32, 558L, 1037L);
        groupTitle.setMaxParagraphHeight(59);
        groupTitle.setMaxLineWidth(1248);
        groupTitle.setTextAlign(TextAlign.CENTER);
        groupTitle.getElementAttributes().setVerticalAlignment(VerticalAlignment.MIDDLE);
        groupTitle.setFontWeight(FontWeight.BOLD);
        elements.add(groupTitle);

        final SvgLine titleLine = new SvgLine("000000", 2D, 500L, 1078L, 617L, 1077L);
        elements.add(titleLine);

        final SvgLine groupLine = new SvgLine(UNIVERSAL_COLOR, DEFAULT_STROKE_WIDTH, 558L, 1190L, 558L, 1491L);
        elements.add(groupLine);

        final SvgText scoreTitle1 = new SvgText("Sofia Sans Extra Condensed", "RECEPTIVE", 32, 439L, 1156L);
        scoreTitle1.setMaxParagraphHeight(90);
        scoreTitle1.setMaxLineWidth(180);
        scoreTitle1.setTextAlign(TextAlign.RIGHT);
        scoreTitle1.getElementAttributes().setVerticalAlignment(VerticalAlignment.MIDDLE);
        scoreTitle1.setFontWeight(FontWeight.BOLD);
        elements.add(scoreTitle1);

        final SvgCircle scoreCircle1Border = new SvgCircle(496L, 1103L, 63L);
        scoreCircle1Border.getElementAttributes().setFill(EMPTY_BULLET_COLOR);
        scoreCircle1Border.getElementAttributes().setFillOpacity(UNIVERSAL_BACKGROUND_OPACITY);
        scoreCircle1Border.getElementStroke().setStrokeColor(UNIVERSAL_BORDER_COLOR);
        scoreCircle1Border.getElementStroke().setStrokeWidth(DEFAULT_STROKE_WIDTH);
        elements.add(scoreCircle1Border);

        final SvgCircle scoreCircle1 = new SvgCircle(515L, 1123L, 43L);
        scoreCircle1.getElementAttributes().setFill(RECEPTIVE_COLOR);
        scoreCircle1.getElementStroke().setStrokeColor(UNIVERSAL_COLOR);
        scoreCircle1.getElementStroke().setStrokeWidth(DEFAULT_STROKE_WIDTH);
        elements.add(scoreCircle1);

        SvgEmbedded scoreCircleIcon1 = new SvgEmbedded(UNIVERSAL_ICON, 531L, 1138L);
        scoreCircleIcon1.getElementAttributes().setWidth(55);
        scoreCircleIcon1.getElementAttributes().setHeight(55);
        elements.add(scoreCircleIcon1);

        final SvgText scoreTitle2 = new SvgText("Sofia Sans Extra Condensed", "INTERPERSONAL SENSITIVITY", 32, 430L, 1353L);
        scoreTitle2.setMaxParagraphHeight(90);
        scoreTitle2.setMaxLineWidth(180);
        scoreTitle2.setTextAlign(TextAlign.RIGHT);
        scoreTitle2.getElementAttributes().setVerticalAlignment(VerticalAlignment.MIDDLE);
        scoreTitle2.setFontWeight(FontWeight.NORMAL);
        elements.add(scoreTitle2);

        final SvgCircle scoreCircle2 = new SvgCircle(515L, 1338L, 43L);
        scoreCircle2.getElementAttributes().setFill(INTERPERSONAL_SENSITIVITY_COLOR);
        scoreCircle2.getElementStroke().setStrokeColor(UNIVERSAL_COLOR);
        scoreCircle2.getElementStroke().setStrokeWidth(DEFAULT_STROKE_WIDTH);
        elements.add(scoreCircle2);

        SvgEmbedded scoreCircleIcon2 = new SvgEmbedded(UNIVERSAL_ICON, 531L, 1354L);
        scoreCircleIcon2.getElementAttributes().setWidth(55);
        scoreCircleIcon2.getElementAttributes().setHeight(55);
        elements.add(scoreCircleIcon2);

        final SvgText scoreTitle3 = new SvgText("Sofia Sans Extra Condensed", "MULTICULTURAL SENSITIVITY", 32, 431L, 1504L);
        scoreTitle3.setMaxParagraphHeight(90);
        scoreTitle3.setMaxLineWidth(180);
        scoreTitle3.setTextAlign(TextAlign.RIGHT);
        scoreTitle3.getElementAttributes().setVerticalAlignment(VerticalAlignment.MIDDLE);
        scoreTitle3.setFontWeight(FontWeight.NORMAL);
        elements.add(scoreTitle3);

        final SvgCircle scoreCircle3 = new SvgCircle(515L, 1489L, 43L);
        scoreCircle3.getElementAttributes().setFill(MULTICULTURAL_SENSITIVITY_COLOR);
        scoreCircle3.getElementStroke().setStrokeColor(UNIVERSAL_COLOR);
        scoreCircle3.getElementStroke().setStrokeWidth(DEFAULT_STROKE_WIDTH);
        elements.add(scoreCircle3);

        SvgEmbedded scoreCircleIcon3 = new SvgEmbedded(UNIVERSAL_ICON, 531L, 1504L);
        scoreCircleIcon3.getElementAttributes().setWidth(55);
        scoreCircleIcon3.getElementAttributes().setHeight(55);
        elements.add(scoreCircleIcon3);

        return elements;
    }


    private List<SvgAreaElement> generateSocietyElements() throws FileNotFoundException {
        final List<SvgAreaElement> elements = new ArrayList<>();

        final SvgText groupTitle = new SvgText("Sofia Sans Extra Condensed", "SOCIETY", 32, 994L, 1037L);
        groupTitle.setMaxParagraphHeight(59);
        groupTitle.setMaxLineWidth(1248);
        groupTitle.setTextAlign(TextAlign.CENTER);
        groupTitle.getElementAttributes().setVerticalAlignment(VerticalAlignment.MIDDLE);
        groupTitle.setFontWeight(FontWeight.BOLD);
        elements.add(groupTitle);

        final SvgLine titleLine = new SvgLine("000000", 2D, 936L, 1078L, 1053L, 1077L);
        elements.add(titleLine);

        final SvgLine groupLine = new SvgLine(SOCIETY_COLOR, DEFAULT_STROKE_WIDTH, 994L, 1190L, 994L, 1491L);
        elements.add(groupLine);

        final SvgText scoreTitle1 = new SvgText("Sofia Sans Extra Condensed", "INNOVATOR", 32, 874L, 1156L);
        scoreTitle1.setMaxParagraphHeight(90);
        scoreTitle1.setMaxLineWidth(180);
        scoreTitle1.setTextAlign(TextAlign.RIGHT);
        scoreTitle1.getElementAttributes().setVerticalAlignment(VerticalAlignment.MIDDLE);
        scoreTitle1.setFontWeight(FontWeight.BOLD);
        elements.add(scoreTitle1);

        final SvgCircle scoreCircle1Border = new SvgCircle(931L, 1103L, 63L);
        scoreCircle1Border.getElementAttributes().setFill(EMPTY_BULLET_COLOR);
        scoreCircle1Border.getElementAttributes().setFillOpacity(SOCIETY_BACKGROUND_OPACITY);
        scoreCircle1Border.getElementStroke().setStrokeColor(SOCIETY_BORDER_COLOR);
        scoreCircle1Border.getElementStroke().setStrokeWidth(DEFAULT_STROKE_WIDTH);
        elements.add(scoreCircle1Border);

        final SvgCircle scoreCircle1 = new SvgCircle(951L, 1123L, 43L);
        scoreCircle1.getElementAttributes().setFill(INNOVATOR_COLOR);
        scoreCircle1.getElementStroke().setStrokeColor(SOCIETY_COLOR);
        scoreCircle1.getElementStroke().setStrokeWidth(DEFAULT_STROKE_WIDTH);
        elements.add(scoreCircle1);

        SvgEmbedded scoreCircleIcon1 = new SvgEmbedded(SOCIETY_ICON, 966L, 1138L);
        scoreCircleIcon1.getElementAttributes().setWidth(55);
        scoreCircleIcon1.getElementAttributes().setHeight(55);
        elements.add(scoreCircleIcon1);

        final SvgText scoreTitle2 = new SvgText("Sofia Sans Extra Condensed", "COOPERATION", 32, 871L, 1371L);
        scoreTitle2.setMaxParagraphHeight(90);
        scoreTitle2.setMaxLineWidth(180);
        scoreTitle2.setTextAlign(TextAlign.RIGHT);
        scoreTitle2.getElementAttributes().setVerticalAlignment(VerticalAlignment.MIDDLE);
        scoreTitle2.setFontWeight(FontWeight.NORMAL);
        elements.add(scoreTitle2);

        final SvgCircle scoreCircle2 = new SvgCircle(951L, 1338L, 43L);
        scoreCircle2.getElementAttributes().setFill(COOPERATION_COLOR);
        scoreCircle2.getElementStroke().setStrokeColor(SOCIETY_COLOR);
        scoreCircle2.getElementStroke().setStrokeWidth(DEFAULT_STROKE_WIDTH);
        elements.add(scoreCircle2);

        SvgEmbedded scoreCircleIcon2 = new SvgEmbedded(SOCIETY_ICON, 966L, 1353L);
        scoreCircleIcon2.getElementAttributes().setWidth(55);
        scoreCircleIcon2.getElementAttributes().setHeight(55);
        elements.add(scoreCircleIcon2);

        final SvgText scoreTitle3 = new SvgText("Sofia Sans Extra Condensed", "INNOVATION", 32, 879L, 1522L);
        scoreTitle3.setMaxParagraphHeight(90);
        scoreTitle3.setMaxLineWidth(180);
        scoreTitle3.setTextAlign(TextAlign.RIGHT);
        scoreTitle3.getElementAttributes().setVerticalAlignment(VerticalAlignment.MIDDLE);
        scoreTitle3.setFontWeight(FontWeight.NORMAL);
        elements.add(scoreTitle3);

        final SvgCircle scoreCircle3 = new SvgCircle(951L, 1489L, 43L);
        scoreCircle3.getElementAttributes().setFill(INNOVATION_COLOR);
        scoreCircle3.getElementStroke().setStrokeColor(SOCIETY_COLOR);
        scoreCircle3.getElementStroke().setStrokeWidth(DEFAULT_STROKE_WIDTH);
        elements.add(scoreCircle3);

        SvgEmbedded scoreCircleIcon3 = new SvgEmbedded(SOCIETY_ICON, 966L, 1504L);
        scoreCircleIcon3.getElementAttributes().setWidth(55);
        scoreCircleIcon3.getElementAttributes().setHeight(55);
        elements.add(scoreCircleIcon3);

        return elements;
    }

    private List<SvgAreaElement> generateVisionElements() throws FileNotFoundException {
        final List<SvgAreaElement> elements = new ArrayList<>();

        final SvgText groupTitle = new SvgText("Sofia Sans Extra Condensed", "VISION", 32, 1443L, 1037L);
        groupTitle.setMaxParagraphHeight(59);
        groupTitle.setMaxLineWidth(1248);
        groupTitle.setTextAlign(TextAlign.CENTER);
        groupTitle.getElementAttributes().setVerticalAlignment(VerticalAlignment.MIDDLE);
        groupTitle.setFontWeight(FontWeight.BOLD);
        elements.add(groupTitle);

        final SvgLine titleLine = new SvgLine("000000", 2D, 1384L, 1078L, 1500L, 1077L);
        elements.add(titleLine);

        final SvgLine groupLine = new SvgLine(VISION_COLOR, DEFAULT_STROKE_WIDTH, 1442L, 1190L, 1442L, 1491L);
        elements.add(groupLine);

        final SvgText scoreTitle1 = new SvgText("Sofia Sans Extra Condensed", "VISIONARY", 32, 1643L, 1156L);
        scoreTitle1.setMaxParagraphHeight(90);
        scoreTitle1.setMaxLineWidth(180);
        scoreTitle1.setTextAlign(TextAlign.RIGHT);
        scoreTitle1.getElementAttributes().setVerticalAlignment(VerticalAlignment.MIDDLE);
        scoreTitle1.setFontWeight(FontWeight.BOLD);
        elements.add(scoreTitle1);

        final SvgCircle scoreCircle1Border = new SvgCircle(1379L, 1103L, 63L);
        scoreCircle1Border.getElementAttributes().setFill(EMPTY_BULLET_COLOR);
        scoreCircle1Border.getElementAttributes().setFillOpacity(VISION_BACKGROUND_OPACITY);
        scoreCircle1Border.getElementStroke().setStrokeColor(VISION_BORDER_COLOR);
        scoreCircle1Border.getElementStroke().setStrokeWidth(DEFAULT_STROKE_WIDTH);
        elements.add(scoreCircle1Border);

        final SvgCircle scoreCircle1 = new SvgCircle(1399L, 1123L, 43L);
        scoreCircle1.getElementAttributes().setFill(VISIONARY_COLOR);
        scoreCircle1.getElementStroke().setStrokeColor(VISION_COLOR);
        scoreCircle1.getElementStroke().setStrokeWidth(DEFAULT_STROKE_WIDTH);
        elements.add(scoreCircle1);

        SvgEmbedded scoreCircleIcon1 = new SvgEmbedded(VISION_ICON, 1414L, 1139L);
        scoreCircleIcon1.getElementAttributes().setWidth(55);
        scoreCircleIcon1.getElementAttributes().setHeight(55);
        elements.add(scoreCircleIcon1);

        final SvgText scoreTitle2 = new SvgText("Sofia Sans Extra Condensed", "PERSUASIVENESS", 32, 1691L, 1371L);
        scoreTitle2.setMaxParagraphHeight(90);
        scoreTitle2.setMaxLineWidth(180);
        scoreTitle2.setTextAlign(TextAlign.RIGHT);
        scoreTitle2.getElementAttributes().setVerticalAlignment(VerticalAlignment.MIDDLE);
        scoreTitle2.setFontWeight(FontWeight.NORMAL);
        elements.add(scoreTitle2);

        final SvgCircle scoreCircle2 = new SvgCircle(1399L, 1338L, 43L);
        scoreCircle2.getElementAttributes().setFill(PERSUASIVENESS_COLOR);
        scoreCircle2.getElementStroke().setStrokeColor(VISION_COLOR);
        scoreCircle2.getElementStroke().setStrokeWidth(DEFAULT_STROKE_WIDTH);
        elements.add(scoreCircle2);

        SvgEmbedded scoreCircleIcon2 = new SvgEmbedded(VISION_ICON, 1414L, 1353L);
        scoreCircleIcon2.getElementAttributes().setWidth(55);
        scoreCircleIcon2.getElementAttributes().setHeight(55);
        elements.add(scoreCircleIcon2);

        final SvgText scoreTitle3 = new SvgText("Sofia Sans Extra Condensed", "FUTURE", 32, 1608L, 1522L);
        scoreTitle3.setMaxParagraphHeight(90);
        scoreTitle3.setMaxLineWidth(180);
        scoreTitle3.setTextAlign(TextAlign.RIGHT);
        scoreTitle3.getElementAttributes().setVerticalAlignment(VerticalAlignment.MIDDLE);
        scoreTitle3.setFontWeight(FontWeight.NORMAL);
        elements.add(scoreTitle3);

        final SvgCircle scoreCircle3 = new SvgCircle(1399L, 1489L, 43L);
        scoreCircle3.getElementAttributes().setFill(FUTURE_COLOR);
        scoreCircle3.getElementStroke().setStrokeColor(VISION_COLOR);
        scoreCircle3.getElementStroke().setStrokeWidth(DEFAULT_STROKE_WIDTH);
        elements.add(scoreCircle3);

        SvgEmbedded scoreCircleIcon3 = new SvgEmbedded(VISION_ICON, 1414L, 1505L);
        scoreCircleIcon3.getElementAttributes().setWidth(55);
        scoreCircleIcon3.getElementAttributes().setHeight(55);
        elements.add(scoreCircleIcon3);

        return elements;
    }

    private List<SvgAreaElement> generateStrengthElements() throws FileNotFoundException {
        final List<SvgAreaElement> elements = new ArrayList<>();

        final SvgText groupTitle = new SvgText("Sofia Sans Extra Condensed", "STRENGTH", 32, 1886L, 1037L);
        groupTitle.setMaxParagraphHeight(59);
        groupTitle.setMaxLineWidth(1248);
        groupTitle.setTextAlign(TextAlign.CENTER);
        groupTitle.getElementAttributes().setVerticalAlignment(VerticalAlignment.MIDDLE);
        groupTitle.setFontWeight(FontWeight.BOLD);
        elements.add(groupTitle);

        final SvgLine titleLine = new SvgLine("000000", 2D, 1828L, 1078L, 1944L, 1077L);
        elements.add(titleLine);

        final SvgLine groupLine = new SvgLine(STRENGTH_COLOR, DEFAULT_STROKE_WIDTH, 1885L, 1190L, 1885L, 1491L);
        elements.add(groupLine);

        final SvgText scoreTitle1 = new SvgText("Sofia Sans Extra Condensed", "STRATEGIST", 32, 2095L, 1156L);
        scoreTitle1.setMaxParagraphHeight(90);
        scoreTitle1.setMaxLineWidth(180);
        scoreTitle1.setTextAlign(TextAlign.RIGHT);
        scoreTitle1.getElementAttributes().setVerticalAlignment(VerticalAlignment.MIDDLE);
        scoreTitle1.setFontWeight(FontWeight.BOLD);
        elements.add(scoreTitle1);

        final SvgCircle scoreCircle1Border = new SvgCircle(1822L, 1103L, 63L);
        scoreCircle1Border.getElementAttributes().setFill(EMPTY_BULLET_COLOR);
        scoreCircle1Border.getElementAttributes().setFillOpacity(STRENGTH_BACKGROUND_OPACITY);
        scoreCircle1Border.getElementStroke().setStrokeColor(STRENGTH_BORDER_COLOR);
        scoreCircle1Border.getElementStroke().setStrokeWidth(DEFAULT_STROKE_WIDTH);
        elements.add(scoreCircle1Border);

        final SvgCircle scoreCircle1 = new SvgCircle(1842L, 1123L, 43L);
        scoreCircle1.getElementAttributes().setFill(STRATEGIST_COLOR);
        scoreCircle1.getElementStroke().setStrokeColor(STRENGTH_COLOR);
        scoreCircle1.getElementStroke().setStrokeWidth(DEFAULT_STROKE_WIDTH);
        elements.add(scoreCircle1);

        SvgEmbedded scoreCircleIcon1 = new SvgEmbedded(STRENGTH_ICON, 1857L, 1139L);
        scoreCircleIcon1.getElementAttributes().setWidth(55);
        scoreCircleIcon1.getElementAttributes().setHeight(55);
        elements.add(scoreCircleIcon1);

        final SvgText scoreTitle2 = new SvgText("Sofia Sans Extra Condensed", "DECISIVENESS", 32, 2105L, 1371L);
        scoreTitle2.setMaxParagraphHeight(90);
        scoreTitle2.setMaxLineWidth(180);
        scoreTitle2.setTextAlign(TextAlign.RIGHT);
        scoreTitle2.getElementAttributes().setVerticalAlignment(VerticalAlignment.MIDDLE);
        scoreTitle2.setFontWeight(FontWeight.NORMAL);
        elements.add(scoreTitle2);

        final SvgCircle scoreCircle2 = new SvgCircle(1842L, 1338L, 43L);
        scoreCircle2.getElementAttributes().setFill(DECISIVENESS_COLOR);
        scoreCircle2.getElementStroke().setStrokeColor(STRENGTH_COLOR);
        scoreCircle2.getElementStroke().setStrokeWidth(DEFAULT_STROKE_WIDTH);
        elements.add(scoreCircle2);

        SvgEmbedded scoreCircleIcon2 = new SvgEmbedded(STRENGTH_ICON, 1857L, 1354L);
        scoreCircleIcon2.getElementAttributes().setWidth(55);
        scoreCircleIcon2.getElementAttributes().setHeight(55);
        elements.add(scoreCircleIcon2);

        final SvgText scoreTitle3 = new SvgText("Sofia Sans Extra Condensed", "JUDGEMENT", 32, 2086L, 1520L);
        scoreTitle3.setMaxParagraphHeight(90);
        scoreTitle3.setMaxLineWidth(180);
        scoreTitle3.setTextAlign(TextAlign.RIGHT);
        scoreTitle3.getElementAttributes().setVerticalAlignment(VerticalAlignment.MIDDLE);
        scoreTitle3.setFontWeight(FontWeight.NORMAL);
        elements.add(scoreTitle3);

        final SvgCircle scoreCircle3 = new SvgCircle(1842L, 1489L, 43L);
        scoreCircle3.getElementAttributes().setFill(JUDGEMENT_COLOR);
        scoreCircle3.getElementStroke().setStrokeColor(STRENGTH_COLOR);
        scoreCircle3.getElementStroke().setStrokeWidth(DEFAULT_STROKE_WIDTH);
        elements.add(scoreCircle3);

        SvgEmbedded scoreCircleIcon3 = new SvgEmbedded(STRENGTH_ICON, 1857L, 1505L);
        scoreCircleIcon3.getElementAttributes().setWidth(55);
        scoreCircleIcon3.getElementAttributes().setHeight(55);
        elements.add(scoreCircleIcon3);

        return elements;
    }

    private List<SvgAreaElement> generateStructureInspirationsElements() throws FileNotFoundException {
        final List<SvgAreaElement> elements = new ArrayList<>();

        final SvgText groupTitle = new SvgText("Sofia Sans Extra Condensed", "STRUCTURE", 32, 558L, 1849L);
        groupTitle.setMaxParagraphHeight(59);
        groupTitle.setMaxLineWidth(1248);
        groupTitle.setTextAlign(TextAlign.CENTER);
        groupTitle.getElementAttributes().setVerticalAlignment(VerticalAlignment.MIDDLE);
        groupTitle.setFontWeight(FontWeight.BOLD);
        elements.add(groupTitle);

        final SvgLine titleLine = new SvgLine("000000", 2D, 500L, 1890L, 617L, 1890L);
        elements.add(titleLine);

        final SvgLine groupLine = new SvgLine(STRUCTURE_COLOR, DEFAULT_STROKE_WIDTH, 558L, 2000L, 558L, 2080L);
        elements.add(groupLine);

        final SvgText scoreTitle1 = new SvgText("Sofia Sans Extra Condensed", "DISCIPLINE", 32, 439L, 1952L);
        scoreTitle1.setMaxParagraphHeight(90);
        scoreTitle1.setMaxLineWidth(180);
        scoreTitle1.setTextAlign(TextAlign.RIGHT);
        scoreTitle1.getElementAttributes().setVerticalAlignment(VerticalAlignment.MIDDLE);
        scoreTitle1.setFontWeight(FontWeight.NORMAL);
        elements.add(scoreTitle1);

        final SvgCircle scoreCircle1 = new SvgCircle(515L, 1926L, 43L);
        scoreCircle1.getElementAttributes().setFill(DISCIPLINE_COLOR);
        scoreCircle1.getElementStroke().setStrokeColor(STRUCTURE_COLOR);
        scoreCircle1.getElementStroke().setStrokeWidth(DEFAULT_STROKE_WIDTH);
        elements.add(scoreCircle1);

        SvgEmbedded scoreCircleIcon1 = new SvgEmbedded(STRUCTURE_ICON, 531L, 1946L);
        scoreCircleIcon1.getElementAttributes().setWidth(55);
        scoreCircleIcon1.getElementAttributes().setHeight(55);
        elements.add(scoreCircleIcon1);

        final SvgText scoreTitle2 = new SvgText("Sofia Sans Extra Condensed", "GOAL SETTING", 32, 440L, 2105L);
        scoreTitle2.setMaxParagraphHeight(90);
        scoreTitle2.setMaxLineWidth(180);
        scoreTitle2.setTextAlign(TextAlign.RIGHT);
        scoreTitle2.getElementAttributes().setVerticalAlignment(VerticalAlignment.MIDDLE);
        scoreTitle2.setFontWeight(FontWeight.NORMAL);
        elements.add(scoreTitle2);

        final SvgCircle scoreCircle2 = new SvgCircle(515L, 2077L, 43L);
        scoreCircle2.getElementAttributes().setFill(GOAL_SETTING_COLOR);
        scoreCircle2.getElementStroke().setStrokeColor(STRUCTURE_COLOR);
        scoreCircle2.getElementStroke().setStrokeWidth(DEFAULT_STROKE_WIDTH);
        elements.add(scoreCircle2);

        SvgEmbedded scoreCircleIcon2 = new SvgEmbedded(STRUCTURE_ICON, 531L, 2097L);
        scoreCircleIcon2.getElementAttributes().setWidth(55);
        scoreCircleIcon2.getElementAttributes().setHeight(55);
        elements.add(scoreCircleIcon2);

        final SvgLine gradientLine = new SvgLine(STRUCTURE_COLOR, DEFAULT_STROKE_WIDTH, 555L, 2044L, 994L, 2044L);
        gradientLine.setGradient(new SvgGradient(
                new SvgGradientStop(STRUCTURE_COLOR, FULL_ALPHA, 0.0),
                new SvgGradientStop(INSPIRATION_COLOR, FULL_ALPHA, 1.0)));
        elements.add(gradientLine);


        final SvgText groupTitle2 = new SvgText("Sofia Sans Extra Condensed", "INSPIRATION", 32, 994L, 1849L);
        groupTitle2.setMaxParagraphHeight(59);
        groupTitle2.setMaxLineWidth(1248);
        groupTitle2.setTextAlign(TextAlign.CENTER);
        groupTitle2.getElementAttributes().setVerticalAlignment(VerticalAlignment.MIDDLE);
        groupTitle2.setFontWeight(FontWeight.BOLD);
        elements.add(groupTitle2);

        final SvgLine titleLine2 = new SvgLine("000000", 2D, 937L, 1890L, 1053L, 1890L);
        elements.add(titleLine2);

        final SvgLine groupLine2 = new SvgLine(INSPIRATION_COLOR, DEFAULT_STROKE_WIDTH, 992L, 2000L, 992L, 2080L);
        elements.add(groupLine2);

        final SvgText scoreTitle3 = new SvgText("Sofia Sans Extra Condensed", "CONSCIENTIOUSNESS", 32, 874L, 1952L);
        scoreTitle3.setMaxParagraphHeight(90);
        scoreTitle3.setMaxLineWidth(180);
        scoreTitle3.setTextAlign(TextAlign.RIGHT);
        scoreTitle3.getElementAttributes().setVerticalAlignment(VerticalAlignment.MIDDLE);
        scoreTitle3.setFontWeight(FontWeight.NORMAL);
        elements.add(scoreTitle3);

        final SvgCircle scoreCircle3 = new SvgCircle(948L, 1926L, 43L);
        scoreCircle3.getElementAttributes().setFill(CONSCIENTIOUSNESS_COLOR);
        scoreCircle3.getElementStroke().setStrokeColor(INSPIRATION_COLOR);
        scoreCircle3.getElementStroke().setStrokeWidth(DEFAULT_STROKE_WIDTH);
        elements.add(scoreCircle3);

        SvgEmbedded scoreCircleIcon3 = new SvgEmbedded(INSPIRATION_ICON, 959L, 1941L);
        scoreCircleIcon3.getElementAttributes().setWidth(55);
        scoreCircleIcon3.getElementAttributes().setHeight(55);
        elements.add(scoreCircleIcon3);

        final SvgText scoreTitle4 = new SvgText("Sofia Sans Extra Condensed", "ENGAGEMENT", 32, 874L, 2105L);
        scoreTitle4.setMaxParagraphHeight(90);
        scoreTitle4.setMaxLineWidth(180);
        scoreTitle4.setTextAlign(TextAlign.RIGHT);
        scoreTitle4.getElementAttributes().setVerticalAlignment(VerticalAlignment.MIDDLE);
        scoreTitle4.setFontWeight(FontWeight.NORMAL);
        elements.add(scoreTitle4);

        final SvgCircle scoreCircle4 = new SvgCircle(948L, 2077L, 43L);
        scoreCircle4.getElementAttributes().setFill(ENGAGEMENT_COLOR);
        scoreCircle4.getElementStroke().setStrokeColor(INSPIRATION_COLOR);
        scoreCircle4.getElementStroke().setStrokeWidth(DEFAULT_STROKE_WIDTH);
        elements.add(scoreCircle4);

        SvgEmbedded scoreCircleIcon4 = new SvgEmbedded(INSPIRATION_ICON, 959L, 2092L);
        scoreCircleIcon4.getElementAttributes().setWidth(55);
        scoreCircleIcon4.getElementAttributes().setHeight(55);
        elements.add(scoreCircleIcon4);

        return elements;
    }

    private List<SvgAreaElement> generateAdaptabilityActionElements() throws FileNotFoundException {
        final List<SvgAreaElement> elements = new ArrayList<>();

        final SvgText groupTitle = new SvgText("Sofia Sans Extra Condensed", "ADAPTABILITY", 32, 1442L, 1849L);
        groupTitle.setMaxParagraphHeight(59);
        groupTitle.setMaxLineWidth(1248);
        groupTitle.setTextAlign(TextAlign.CENTER);
        groupTitle.getElementAttributes().setVerticalAlignment(VerticalAlignment.MIDDLE);
        groupTitle.setFontWeight(FontWeight.BOLD);
        elements.add(groupTitle);

        final SvgLine titleLine = new SvgLine("000000", 2D, 1384L, 1890L, 1500L, 1890L);
        elements.add(titleLine);

        final SvgLine groupLine = new SvgLine(ADAPTABILITY_COLOR, DEFAULT_STROKE_WIDTH, 1442L, 2000L, 1442L, 2080L);
        elements.add(groupLine);

        final SvgText scoreTitle1 = new SvgText("Sofia Sans Extra Condensed", "BUILDING AND MAINTAINING", 32, 1538L, 1932L);
        scoreTitle1.setMaxParagraphHeight(90);
        scoreTitle1.setMaxLineWidth(180);
        scoreTitle1.setTextAlign(TextAlign.LEFT);
        scoreTitle1.getElementAttributes().setVerticalAlignment(VerticalAlignment.MIDDLE);
        scoreTitle1.setFontWeight(FontWeight.NORMAL);
        elements.add(scoreTitle1);

        final SvgCircle scoreCircle1 = new SvgCircle(1399L, 1926L, 43L);
        scoreCircle1.getElementAttributes().setFill(BUILDING_AND_MAINTAINING_COLOR);
        scoreCircle1.getElementStroke().setStrokeColor(ADAPTABILITY_COLOR);
        scoreCircle1.getElementStroke().setStrokeWidth(DEFAULT_STROKE_WIDTH);
        elements.add(scoreCircle1);

        SvgEmbedded scoreCircleIcon1 = new SvgEmbedded(ADAPTABILITY_ICON, 1414L, 1941L);
        scoreCircleIcon1.getElementAttributes().setWidth(55);
        scoreCircleIcon1.getElementAttributes().setHeight(55);
        elements.add(scoreCircleIcon1);

        final SvgText scoreTitle2 = new SvgText("Sofia Sans Extra Condensed", "FLEXIBILITY", 32, 1638L, 2105L);
        scoreTitle2.setMaxParagraphHeight(90);
        scoreTitle2.setMaxLineWidth(180);
        scoreTitle2.setTextAlign(TextAlign.RIGHT);
        scoreTitle2.getElementAttributes().setVerticalAlignment(VerticalAlignment.MIDDLE);
        scoreTitle2.setFontWeight(FontWeight.NORMAL);
        elements.add(scoreTitle2);

        final SvgCircle scoreCircle2 = new SvgCircle(1399L, 2077L, 43L);
        scoreCircle2.getElementAttributes().setFill(FLEXIBILITY_COLOR);
        scoreCircle2.getElementStroke().setStrokeColor(ADAPTABILITY_COLOR);
        scoreCircle2.getElementStroke().setStrokeWidth(DEFAULT_STROKE_WIDTH);
        elements.add(scoreCircle2);

        SvgEmbedded scoreCircleIcon2 = new SvgEmbedded(ADAPTABILITY_ICON, 1415L, 2092L);
        scoreCircleIcon2.getElementAttributes().setWidth(55);
        scoreCircleIcon2.getElementAttributes().setHeight(55);
        elements.add(scoreCircleIcon2);

        final SvgLine gradientLine = new SvgLine(ADAPTABILITY_COLOR, DEFAULT_STROKE_WIDTH, 1440L, 2044L, 1885L, 2044L);
        gradientLine.setGradient(new SvgGradient(
                new SvgGradientStop(ADAPTABILITY_COLOR, FULL_ALPHA, 0.0),
                new SvgGradientStop(ACTION_COLOR, FULL_ALPHA, 1.0)));
        elements.add(gradientLine);


        final SvgText groupTitle2 = new SvgText("Sofia Sans Extra Condensed", "ACTION", 32, 1886L, 1849L);
        groupTitle2.setMaxParagraphHeight(59);
        groupTitle2.setMaxLineWidth(1248);
        groupTitle2.setTextAlign(TextAlign.CENTER);
        groupTitle2.getElementAttributes().setVerticalAlignment(VerticalAlignment.MIDDLE);
        groupTitle2.setFontWeight(FontWeight.BOLD);
        elements.add(groupTitle2);

        final SvgLine titleLine2 = new SvgLine("000000", 2D, 1831L, 1890L, 1948L, 1890L);
        elements.add(titleLine2);

        final SvgLine groupLine2 = new SvgLine(ACTION_COLOR, DEFAULT_STROKE_WIDTH, 1885L, 2000L, 1885L, 2080L);
        elements.add(groupLine2);

        final SvgText scoreTitle3 = new SvgText("Sofia Sans Extra Condensed", "DIRECTION", 32, 2074L, 1952L);
        scoreTitle3.setMaxParagraphHeight(90);
        scoreTitle3.setMaxLineWidth(180);
        scoreTitle3.setTextAlign(TextAlign.RIGHT);
        scoreTitle3.getElementAttributes().setVerticalAlignment(VerticalAlignment.MIDDLE);
        scoreTitle3.setFontWeight(FontWeight.NORMAL);
        elements.add(scoreTitle3);

        final SvgCircle scoreCircle3 = new SvgCircle(1842L, 1926L, 43L);
        scoreCircle3.getElementAttributes().setFill(DIRECTION_COLOR);
        scoreCircle3.getElementStroke().setStrokeColor(ACTION_COLOR);
        scoreCircle3.getElementStroke().setStrokeWidth(DEFAULT_STROKE_WIDTH);
        elements.add(scoreCircle3);

        SvgEmbedded scoreCircleIcon3 = new SvgEmbedded(ACTION_ICON, 1857L, 1941L);
        scoreCircleIcon3.getElementAttributes().setWidth(55);
        scoreCircleIcon3.getElementAttributes().setHeight(55);
        elements.add(scoreCircleIcon3);

        final SvgText scoreTitle4 = new SvgText("Sofia Sans Extra Condensed", "INITIATIVE", 32, 2076L, 2105L);
        scoreTitle4.setMaxParagraphHeight(90);
        scoreTitle4.setMaxLineWidth(180);
        scoreTitle4.setTextAlign(TextAlign.RIGHT);
        scoreTitle4.getElementAttributes().setVerticalAlignment(VerticalAlignment.MIDDLE);
        scoreTitle4.setFontWeight(FontWeight.NORMAL);
        elements.add(scoreTitle4);

        final SvgCircle scoreCircle4 = new SvgCircle(1842L, 2077L, 43L);
        scoreCircle4.getElementAttributes().setFill(INITIATIVE_COLOR);
        scoreCircle4.getElementStroke().setStrokeColor(ACTION_COLOR);
        scoreCircle4.getElementStroke().setStrokeWidth(DEFAULT_STROKE_WIDTH);
        elements.add(scoreCircle4);

        SvgEmbedded scoreCircleIcon4 = new SvgEmbedded(ACTION_ICON, 1857L, 2092L);
        scoreCircleIcon4.getElementAttributes().setWidth(55);
        scoreCircleIcon4.getElementAttributes().setHeight(55);
        elements.add(scoreCircleIcon4);

        return elements;
    }

    private List<SvgAreaElement> generateMaterialAttachmentElements() throws FileNotFoundException {
        final List<SvgAreaElement> elements = new ArrayList<>();

        final SvgText groupTitle = new SvgText("Sofia Sans Extra Condensed", "MATERIAL ATTACHMENT", 32, 558L, 2482L);
        groupTitle.setMaxParagraphHeight(59);
        groupTitle.setMaxLineWidth(1248);
        groupTitle.setTextAlign(TextAlign.CENTER);
        groupTitle.getElementAttributes().setVerticalAlignment(VerticalAlignment.MIDDLE);
        groupTitle.setFontWeight(FontWeight.BOLD);
        elements.add(groupTitle);

        final SvgLine titleLine = new SvgLine("000000", 2D, 500L, 2522L, 617L, 2522L);
        elements.add(titleLine);

        final SvgLine groupLine = new SvgLine(MATERIAL_ATTACHMENT_COLOR, DEFAULT_STROKE_WIDTH, 558L, 2637L, 558L, 2957L);
        elements.add(groupLine);

        final SvgText scoreTitle1 = new SvgText("Sofia Sans Extra Condensed", "BANKER", 32, 439L, 2598L);
        scoreTitle1.setMaxParagraphHeight(90);
        scoreTitle1.setMaxLineWidth(180);
        scoreTitle1.setTextAlign(TextAlign.RIGHT);
        scoreTitle1.getElementAttributes().setVerticalAlignment(VerticalAlignment.MIDDLE);
        scoreTitle1.setFontWeight(FontWeight.BOLD);
        elements.add(scoreTitle1);

        final SvgCircle scoreCircle1Border = new SvgCircle(496L, 2545L, 63L);
        scoreCircle1Border.getElementAttributes().setFill(EMPTY_BULLET_COLOR);
        scoreCircle1Border.getElementAttributes().setFillOpacity(MATERIAL_ATTACHMENT_BACKGROUND_OPACITY);
        scoreCircle1Border.getElementStroke().setStrokeColor(MATERIAL_ATTACHMENT_BORDER_COLOR);
        scoreCircle1Border.getElementStroke().setStrokeWidth(DEFAULT_STROKE_WIDTH);
        elements.add(scoreCircle1Border);

        final SvgCircle scoreCircle1 = new SvgCircle(515L, 2565L, 43L);
        scoreCircle1.getElementAttributes().setFill(BANKER_COLOR);
        scoreCircle1.getElementStroke().setStrokeColor(MATERIAL_ATTACHMENT_COLOR);
        scoreCircle1.getElementStroke().setStrokeWidth(DEFAULT_STROKE_WIDTH);
        elements.add(scoreCircle1);

        SvgEmbedded scoreCircleIcon1 = new SvgEmbedded(MATERIAL_ATTACHMENT_ICON, 531L, 2580L);
        scoreCircleIcon1.getElementAttributes().setWidth(55);
        scoreCircleIcon1.getElementAttributes().setHeight(55);
        elements.add(scoreCircleIcon1);

        final SvgText scoreTitle2 = new SvgText("Sofia Sans Extra Condensed", "BUSINESS MINDED", 32, 440L, 2811L);
        scoreTitle2.setMaxParagraphHeight(90);
        scoreTitle2.setMaxLineWidth(180);
        scoreTitle2.setTextAlign(TextAlign.RIGHT);
        scoreTitle2.getElementAttributes().setVerticalAlignment(VerticalAlignment.MIDDLE);
        scoreTitle2.setFontWeight(FontWeight.NORMAL);
        elements.add(scoreTitle2);

        final SvgCircle scoreCircle2 = new SvgCircle(515L, 2777L, 43L);
        scoreCircle2.getElementAttributes().setFill(BUSINESS_MINDED_COLOR);
        scoreCircle2.getElementStroke().setStrokeColor(MATERIAL_ATTACHMENT_COLOR);
        scoreCircle2.getElementStroke().setStrokeWidth(DEFAULT_STROKE_WIDTH);
        elements.add(scoreCircle2);

        SvgEmbedded scoreCircleIcon2 = new SvgEmbedded(MATERIAL_ATTACHMENT_ICON, 531L, 2792L);
        scoreCircleIcon2.getElementAttributes().setWidth(55);
        scoreCircleIcon2.getElementAttributes().setHeight(55);
        elements.add(scoreCircleIcon2);

        final SvgText scoreTitle3 = new SvgText("Sofia Sans Extra Condensed", "TENACITY", 32, 439L, 2964L);
        scoreTitle3.setMaxParagraphHeight(90);
        scoreTitle3.setMaxLineWidth(180);
        scoreTitle3.setTextAlign(TextAlign.RIGHT);
        scoreTitle3.getElementAttributes().setVerticalAlignment(VerticalAlignment.MIDDLE);
        scoreTitle3.setFontWeight(FontWeight.NORMAL);
        elements.add(scoreTitle3);

        final SvgCircle scoreCircle3 = new SvgCircle(515L, 2931L, 43L);
        scoreCircle3.getElementAttributes().setFill(TENACITY_COLOR);
        scoreCircle3.getElementStroke().setStrokeColor(MATERIAL_ATTACHMENT_COLOR);
        scoreCircle3.getElementStroke().setStrokeWidth(DEFAULT_STROKE_WIDTH);
        elements.add(scoreCircle3);

        SvgEmbedded scoreCircleIcon3 = new SvgEmbedded(MATERIAL_ATTACHMENT_ICON, 531L, 2946L);
        scoreCircleIcon3.getElementAttributes().setWidth(55);
        scoreCircleIcon3.getElementAttributes().setHeight(55);
        elements.add(scoreCircleIcon3);

        return elements;
    }

    private List<SvgAreaElement> generateCommunicationElements() throws FileNotFoundException {
        final List<SvgAreaElement> elements = new ArrayList<>();

        final SvgText groupTitle = new SvgText("Sofia Sans Extra Condensed", "COMMUNICATION", 32, 994L, 2482L);
        groupTitle.setMaxParagraphHeight(59);
        groupTitle.setMaxLineWidth(1248);
        groupTitle.setTextAlign(TextAlign.CENTER);
        groupTitle.getElementAttributes().setVerticalAlignment(VerticalAlignment.MIDDLE);
        groupTitle.setFontWeight(FontWeight.BOLD);
        elements.add(groupTitle);

        final SvgLine titleLine = new SvgLine("000000", 2D, 936L, 2522L, 1053L, 2522L);
        elements.add(titleLine);

        final SvgLine groupLine = new SvgLine(COMMUNICATION_COLOR, DEFAULT_STROKE_WIDTH, 994L, 2637L, 994L, 2957L);
        elements.add(groupLine);

        final SvgText scoreTitle1 = new SvgText("Sofia Sans Extra Condensed", "SALESMAN", 32, 875L, 2598L);
        scoreTitle1.setMaxParagraphHeight(90);
        scoreTitle1.setMaxLineWidth(180);
        scoreTitle1.setTextAlign(TextAlign.RIGHT);
        scoreTitle1.getElementAttributes().setVerticalAlignment(VerticalAlignment.MIDDLE);
        scoreTitle1.setFontWeight(FontWeight.BOLD);
        elements.add(scoreTitle1);

        final SvgCircle scoreCircle1Border = new SvgCircle(931L, 2545L, 63L);
        scoreCircle1Border.getElementAttributes().setFill(EMPTY_BULLET_COLOR);
        scoreCircle1Border.getElementAttributes().setFillOpacity(COMMUNICATION_BACKGROUND_OPACITY);
        scoreCircle1Border.getElementStroke().setStrokeColor(COMMUNICATION_BORDER_COLOR);
        scoreCircle1Border.getElementStroke().setStrokeWidth(DEFAULT_STROKE_WIDTH);
        elements.add(scoreCircle1Border);

        final SvgCircle scoreCircle1 = new SvgCircle(951L, 2565L, 43L);
        scoreCircle1.getElementAttributes().setFill(SALESMAN_COLOR);
        scoreCircle1.getElementStroke().setStrokeColor(COMMUNICATION_COLOR);
        scoreCircle1.getElementStroke().setStrokeWidth(DEFAULT_STROKE_WIDTH);
        elements.add(scoreCircle1);

        SvgEmbedded scoreCircleIcon1 = new SvgEmbedded(COMMUNICATION_ICON, 966L, 2576L);
        scoreCircleIcon1.getElementAttributes().setWidth(55);
        scoreCircleIcon1.getElementAttributes().setHeight(55);
        elements.add(scoreCircleIcon1);

        final SvgText scoreTitle2 = new SvgText("Sofia Sans Extra Condensed", "COMMUNICATION SKILLS", 32, 874L, 2792L);
        scoreTitle2.setMaxParagraphHeight(90);
        scoreTitle2.setMaxLineWidth(160);
        scoreTitle2.setTextAlign(TextAlign.RIGHT);
        scoreTitle2.getElementAttributes().setVerticalAlignment(VerticalAlignment.MIDDLE);
        scoreTitle2.setFontWeight(FontWeight.NORMAL);
        elements.add(scoreTitle2);

        final SvgCircle scoreCircle2 = new SvgCircle(951L, 2777L, 43L);
        scoreCircle2.getElementAttributes().setFill(COMMUNICATION_SKILLS_COLOR);
        scoreCircle2.getElementStroke().setStrokeColor(COMMUNICATION_COLOR);
        scoreCircle2.getElementStroke().setStrokeWidth(DEFAULT_STROKE_WIDTH);
        elements.add(scoreCircle2);

        SvgEmbedded scoreCircleIcon2 = new SvgEmbedded(COMMUNICATION_ICON, 966L, 2788L);
        scoreCircleIcon2.getElementAttributes().setWidth(55);
        scoreCircleIcon2.getElementAttributes().setHeight(55);
        elements.add(scoreCircleIcon2);

        final SvgText scoreTitle3 = new SvgText("Sofia Sans Extra Condensed", "CLIENT ORIENTED", 32, 875L, 2964L);
        scoreTitle3.setMaxParagraphHeight(90);
        scoreTitle3.setMaxLineWidth(180);
        scoreTitle3.setTextAlign(TextAlign.RIGHT);
        scoreTitle3.getElementAttributes().setVerticalAlignment(VerticalAlignment.MIDDLE);
        scoreTitle3.setFontWeight(FontWeight.NORMAL);
        elements.add(scoreTitle3);

        final SvgCircle scoreCircle3 = new SvgCircle(951L, 2931L, 43L);
        scoreCircle3.getElementAttributes().setFill(CLIENT_ORIENTED_COLOR);
        scoreCircle3.getElementStroke().setStrokeColor(COMMUNICATION_COLOR);
        scoreCircle3.getElementStroke().setStrokeWidth(DEFAULT_STROKE_WIDTH);
        elements.add(scoreCircle3);

        SvgEmbedded scoreCircleIcon3 = new SvgEmbedded(COMMUNICATION_ICON, 966L, 2942L);
        scoreCircleIcon3.getElementAttributes().setWidth(55);
        scoreCircleIcon3.getElementAttributes().setHeight(55);
        elements.add(scoreCircleIcon3);

        return elements;
    }

    private List<SvgAreaElement> generateSelfAwareElements() throws FileNotFoundException {
        final List<SvgAreaElement> elements = new ArrayList<>();

        final SvgText groupTitle = new SvgText("Sofia Sans Extra Condensed", "SELF AWARE", 32, 1443L, 2482L);
        groupTitle.setMaxParagraphHeight(59);
        groupTitle.setMaxLineWidth(1248);
        groupTitle.setTextAlign(TextAlign.CENTER);
        groupTitle.getElementAttributes().setVerticalAlignment(VerticalAlignment.MIDDLE);
        groupTitle.setFontWeight(FontWeight.BOLD);
        elements.add(groupTitle);

        final SvgLine titleLine = new SvgLine("000000", 2D, 1384L, 2522L, 1500L, 2522L);
        elements.add(titleLine);

        final SvgLine groupLine = new SvgLine(SELF_AWARE_COLOR, DEFAULT_STROKE_WIDTH, 1442L, 2637L, 1442L, 2957L);
        elements.add(groupLine);

        final SvgText scoreTitle1 = new SvgText("Sofia Sans Extra Condensed", "LEADER", 32, 1612L, 2598L);
        scoreTitle1.setMaxParagraphHeight(90);
        scoreTitle1.setMaxLineWidth(180);
        scoreTitle1.setTextAlign(TextAlign.RIGHT);
        scoreTitle1.getElementAttributes().setVerticalAlignment(VerticalAlignment.MIDDLE);
        scoreTitle1.setFontWeight(FontWeight.BOLD);
        elements.add(scoreTitle1);

        final SvgCircle scoreCircle1Border = new SvgCircle(1379L, 2545L, 63L);
        scoreCircle1Border.getElementAttributes().setFill(EMPTY_BULLET_COLOR);
        scoreCircle1Border.getElementAttributes().setFillOpacity(SELF_AWARE_BACKGROUND_OPACITY);
        scoreCircle1Border.getElementStroke().setStrokeColor(SELF_AWARE_BORDER_COLOR);
        scoreCircle1Border.getElementStroke().setStrokeWidth(DEFAULT_STROKE_WIDTH);
        elements.add(scoreCircle1Border);

        final SvgCircle scoreCircle1 = new SvgCircle(1399L, 2565L, 43L);
        scoreCircle1.getElementAttributes().setFill(LEADER_COLOR);
        scoreCircle1.getElementStroke().setStrokeColor(SELF_AWARE_COLOR);
        scoreCircle1.getElementStroke().setStrokeWidth(DEFAULT_STROKE_WIDTH);
        elements.add(scoreCircle1);

        SvgEmbedded scoreCircleIcon1 = new SvgEmbedded(SELF_AWARE_ICON, 1415L, 2581L);
        scoreCircleIcon1.getElementAttributes().setWidth(55);
        scoreCircleIcon1.getElementAttributes().setHeight(55);
        elements.add(scoreCircleIcon1);

        final SvgText scoreTitle2 = new SvgText("Sofia Sans Extra Condensed", "LEADERSHIP", 32, 1645L, 2810L);
        scoreTitle2.setMaxParagraphHeight(90);
        scoreTitle2.setMaxLineWidth(180);
        scoreTitle2.setTextAlign(TextAlign.RIGHT);
        scoreTitle2.getElementAttributes().setVerticalAlignment(VerticalAlignment.MIDDLE);
        scoreTitle2.setFontWeight(FontWeight.NORMAL);
        elements.add(scoreTitle2);

        final SvgCircle scoreCircle2 = new SvgCircle(1399L, 2777L, 43L);
        scoreCircle2.getElementAttributes().setFill(LEADERSHIP_COLOR);
        scoreCircle2.getElementStroke().setStrokeColor(SELF_AWARE_COLOR);
        scoreCircle2.getElementStroke().setStrokeWidth(DEFAULT_STROKE_WIDTH);
        elements.add(scoreCircle2);

        SvgEmbedded scoreCircleIcon2 = new SvgEmbedded(SELF_AWARE_ICON, 1415L, 2793L);
        scoreCircleIcon2.getElementAttributes().setWidth(55);
        scoreCircleIcon2.getElementAttributes().setHeight(55);
        elements.add(scoreCircleIcon2);


        final SvgText scoreTitle3 = new SvgText("Sofia Sans Extra Condensed", "INDEPENDENCE", 32, 1674L, 2964L);
        scoreTitle3.setMaxParagraphHeight(90);
        scoreTitle3.setMaxLineWidth(180);
        scoreTitle3.setTextAlign(TextAlign.RIGHT);
        scoreTitle3.getElementAttributes().setVerticalAlignment(VerticalAlignment.MIDDLE);
        scoreTitle3.setFontWeight(FontWeight.NORMAL);
        elements.add(scoreTitle3);

        final SvgCircle scoreCircle3 = new SvgCircle(1399L, 2931L, 43L);
        scoreCircle3.getElementAttributes().setFill(INDEPENDENCE_COLOR);
        scoreCircle3.getElementStroke().setStrokeColor(SELF_AWARE_COLOR);
        scoreCircle3.getElementStroke().setStrokeWidth(DEFAULT_STROKE_WIDTH);
        elements.add(scoreCircle3);

        SvgEmbedded scoreCircleIcon3 = new SvgEmbedded(SELF_AWARE_ICON, 1415L, 2949L);
        scoreCircleIcon3.getElementAttributes().setWidth(55);
        scoreCircleIcon3.getElementAttributes().setHeight(55);
        elements.add(scoreCircleIcon3);

        return elements;
    }

    private List<SvgAreaElement> generateAnalysisElements() throws FileNotFoundException {
        final List<SvgAreaElement> elements = new ArrayList<>();

        final SvgText groupTitle = new SvgText("Sofia Sans Extra Condensed", "ANALYSIS", 32, 1885L, 2482L);
        groupTitle.setMaxParagraphHeight(59);
        groupTitle.setMaxLineWidth(1248);
        groupTitle.setTextAlign(TextAlign.CENTER);
        groupTitle.getElementAttributes().setVerticalAlignment(VerticalAlignment.MIDDLE);
        groupTitle.setFontWeight(FontWeight.BOLD);
        elements.add(groupTitle);

        final SvgLine titleLine = new SvgLine("000000", 2D, 1828L, 2522L, 1944L, 2522L);
        elements.add(titleLine);

        final SvgLine groupLine = new SvgLine(ANALYSIS_COLOR, DEFAULT_STROKE_WIDTH, 1885L, 2637L, 1885L, 2957L);
        elements.add(groupLine);

        final SvgText scoreTitle1 = new SvgText("Sofia Sans Extra Condensed", "SCIENTIST", 32, 2079L, 2598L);
        scoreTitle1.setMaxParagraphHeight(90);
        scoreTitle1.setMaxLineWidth(180);
        scoreTitle1.setTextAlign(TextAlign.RIGHT);
        scoreTitle1.getElementAttributes().setVerticalAlignment(VerticalAlignment.MIDDLE);
        scoreTitle1.setFontWeight(FontWeight.BOLD);
        elements.add(scoreTitle1);

        final SvgCircle scoreCircle1Border = new SvgCircle(1822L, 2545L, 63L);
        scoreCircle1Border.getElementAttributes().setFill(EMPTY_BULLET_COLOR);
        scoreCircle1Border.getElementAttributes().setFillOpacity(ANALYSIS_BACKGROUND_OPACITY);
        scoreCircle1Border.getElementStroke().setStrokeColor(ANALYSIS_BORDER_COLOR);
        scoreCircle1Border.getElementStroke().setStrokeWidth(DEFAULT_STROKE_WIDTH);
        elements.add(scoreCircle1Border);

        final SvgCircle scoreCircle1 = new SvgCircle(1842L, 2565L, 43L);
        scoreCircle1.getElementAttributes().setFill(SCIENTIST_COLOR);
        scoreCircle1.getElementStroke().setStrokeColor(ANALYSIS_COLOR);
        scoreCircle1.getElementStroke().setStrokeWidth(DEFAULT_STROKE_WIDTH);
        elements.add(scoreCircle1);

        SvgEmbedded scoreCircleIcon1 = new SvgEmbedded(ANALYSIS_ICON, 1858L, 2583L);
        scoreCircleIcon1.getElementAttributes().setWidth(55);
        scoreCircleIcon1.getElementAttributes().setHeight(55);
        elements.add(scoreCircleIcon1);

        final SvgText scoreTitle2 = new SvgText("Sofia Sans Extra Condensed", "PROBLEM ANALYSIS", 32, 2063L, 2792L);
        scoreTitle2.setMaxParagraphHeight(90);
        scoreTitle2.setMaxLineWidth(160);
        scoreTitle2.setTextAlign(TextAlign.RIGHT);
        scoreTitle2.getElementAttributes().setVerticalAlignment(VerticalAlignment.MIDDLE);
        scoreTitle2.setFontWeight(FontWeight.NORMAL);
        elements.add(scoreTitle2);

        final SvgCircle scoreCircle2 = new SvgCircle(1842L, 2777L, 43L);
        scoreCircle2.getElementAttributes().setFill(PROBLEM_ANALYSIS_COLOR);
        scoreCircle2.getElementStroke().setStrokeColor(ANALYSIS_COLOR);
        scoreCircle2.getElementStroke().setStrokeWidth(DEFAULT_STROKE_WIDTH);
        elements.add(scoreCircle2);

        SvgEmbedded scoreCircleIcon2 = new SvgEmbedded(ANALYSIS_ICON, 1858L, 2795L);
        scoreCircleIcon2.getElementAttributes().setWidth(55);
        scoreCircleIcon2.getElementAttributes().setHeight(55);
        elements.add(scoreCircleIcon2);

        final SvgText scoreTitle3 = new SvgText("Sofia Sans Extra Condensed", "PLANING AND ORGANIZING", 32, 1980L, 2945L);
        scoreTitle3.setMaxParagraphHeight(90);
        scoreTitle3.setMaxLineWidth(180);
        scoreTitle3.setTextAlign(TextAlign.LEFT);
        scoreTitle3.getElementAttributes().setVerticalAlignment(VerticalAlignment.MIDDLE);
        scoreTitle3.setFontWeight(FontWeight.NORMAL);
        elements.add(scoreTitle3);

        final SvgCircle scoreCircle3 = new SvgCircle(1842L, 2931L, 43L);
        scoreCircle3.getElementAttributes().setFill(PLANING_AND_ORGANIZING_COLOR);
        scoreCircle3.getElementStroke().setStrokeColor(ANALYSIS_COLOR);
        scoreCircle3.getElementStroke().setStrokeWidth(DEFAULT_STROKE_WIDTH);
        elements.add(scoreCircle3);

        SvgEmbedded scoreCircleIcon3 = new SvgEmbedded(ANALYSIS_ICON, 1858L, 2949L);
        scoreCircleIcon3.getElementAttributes().setWidth(55);
        scoreCircleIcon3.getElementAttributes().setHeight(55);
        elements.add(scoreCircleIcon3);

        return elements;
    }

    private List<SvgAreaElement> generateIntellectualPropertyTexts() {
        final List<SvgAreaElement> elements = new ArrayList<>();

        final SvgText text = new SvgText("Intellectual property disclaim...", 32, 550L, 3350L);
        text.setFontFamily("Arial, sans-serif");
        text.getElementAttributes().setFill(INTELLECTUAL_PROPERTY_DISCLAIM_COLOR);
        elements.add(text);

        return elements;
    }


    @BeforeClass
    public void prepareFolder() throws IOException {
        Files.createDirectories(Paths.get(OUTPUT_FOLDER));
    }

    @Test
    public void generateCADT() throws IOException {
        cadtTemplate = new SvgTemplate();
        cadtTemplate.getElementAttributes().setHeight(3495L);
        cadtTemplate.getElementAttributes().setWidth(2481L);
        cadtTemplate.setSvgBackground(generateBackground());

        cadtTemplate.addElements(generateHeader());
        cadtTemplate.addElements(generateIntroduction());
        cadtTemplate.addElements(generateSelectedExample());

        cadtTemplate.addElements(generateUniversalElements());
        cadtTemplate.addElements(generateSocietyElements());
        cadtTemplate.addElements(generateVisionElements());
        cadtTemplate.addElements(generateStrengthElements());

        cadtTemplate.addElements(generateStructureInspirationsElements());
        cadtTemplate.addElements(generateAdaptabilityActionElements());

        cadtTemplate.addElements(generateMaterialAttachmentElements());
        cadtTemplate.addElements(generateCommunicationElements());
        cadtTemplate.addElements(generateSelfAwareElements());
        cadtTemplate.addElements(generateAnalysisElements());


        cadtTemplate.addElements(generateIntellectualPropertyTexts());
    }

    @Test(dependsOnMethods = "generateCADT")
    public void executeCadt() throws IOException {
        FontFactory.resetFonts();
        final DroolsSubmittedForm droolsSubmittedForm = DroolsSubmittedForm.getFromJson(FileReader.getResource(DROOLS_FORM_FILE_PATH, StandardCharsets.UTF_8));
        final List<String> svgResults = svgFromDroolsConverter.execute(droolsSubmittedForm, Collections.singletonList(cadtTemplate));
        Assert.assertEquals(svgResults.size(), 1);

        try (PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(OUTPUT_FOLDER
                + File.separator + "cadtCustomer4FromDrools.svg")), true)) {
            out.println(svgResults.get(0));
        }

        checkContent(svgResults.get(0), "cadtCustomer4FromDrools.svg");
    }

    @Test(dependsOnMethods = "generateCADT")
    public void checkSerialization() throws JsonProcessingException {
        //Copy JSON code for Infographic docker container from there:
        SvgTemplate svgTemplate4 = SvgTemplate.fromJson(cadtTemplate.toJson());
        Assert.assertEquals(SvgGenerator.generate(svgTemplate4), SvgGenerator.generate(cadtTemplate));
    }

    @AfterClass
    public void removeFolder() {
        Assert.assertTrue(deleteDirectory(new File(OUTPUT_FOLDER)));
    }
}
