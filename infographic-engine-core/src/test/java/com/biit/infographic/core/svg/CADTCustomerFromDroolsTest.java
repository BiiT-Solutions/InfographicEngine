package com.biit.infographic.core.svg;

import com.biit.drools.form.DroolsSubmittedForm;
import com.biit.infographic.core.controllers.DroolsResultController;
import com.biit.infographic.core.generators.SvgGenerator;
import com.biit.infographic.core.models.svg.ElementAttributes;
import com.biit.infographic.core.models.svg.SvgAreaElement;
import com.biit.infographic.core.models.svg.SvgBackground;
import com.biit.infographic.core.models.svg.SvgTemplate;
import com.biit.infographic.core.models.svg.VerticalAlignment;
import com.biit.infographic.core.models.svg.components.SvgCircle;
import com.biit.infographic.core.models.svg.components.SvgImage;
import com.biit.infographic.core.models.svg.components.SvgLine;
import com.biit.infographic.core.models.svg.components.SvgRectangle;
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
@Test(groups = "cadtCustomer")
public class CADTCustomerFromDroolsTest extends AbstractTestNGSpringContextTests {
    protected static final String OUTPUT_FOLDER = System.getProperty("java.io.tmpdir") + File.separator + "SvgTests";

    private static final String TITLE = "NATURAL STRENGTH AND NATURAL POTENTIAL";
    private static final String INTRODUCTION_TITLE = "INTRODUCTION";
    private static final String INTRODUCTION_CONTENT = "The CADT assessment provides you with your natural strength and potential. By choosing your favourites from the 8 archetypes CADT determines what your natural tendencies are, while choosing from the 24 competencies CADT determines what your current behaviours are. Each archetype has associated key competencies. In case they are in tune (i.e. you have chosen them) it means that these competences provide you with energy and that you are able to use them appropriately even under pressure. If they are not in tune (i.e. you have not chosen them) it means that you either do not recognise them in yourself or that you have not developed them yet, but that you are naturally oriented to develop them.";

    private static final String BORDER_COLOR = "b49057";

    private static final Double DEFAULT_STROKE_WIDTH = 8D;

    private static final int GROUP_TEXTS_WIDTHS = 835;
    private static final int INTRODUCTION_TEXTS_WIDTHS = 2182;


    private static final String FIRST_BULLET_COLOR = "919ee1";
    private static final String SECOND_BULLET_COLOR = "e9a197";
    private static final String THIRD_BULLET_COLOR = "7ccadf";
    private static final String FORTH_BULLET_COLOR = "8bc4ab";
    private static final String INTELLECTUAL_PROPERTY_DISCLAIM_COLOR = "d3d4d4ff";

    private static final String FIRST_CHOICE_TITLE = "#DROOLS%CADT%FirstSelectionsTitle#";
    private static final String FIRST_CHOICE_INTRODUCTION = "#DROOLS%CADT%FirstSelectionsIntroduction#";

    private static final String FIRST_CHOICE_CARD = "#DROOLS%CADT%FirstSelectionsCard#";

    private static final String FIRST_GROUP_FIRST_CHOICE_TEXT = "#DROOLS%CADT%FirstGroupFirstSelectionArchetypeText#";
    private static final String FIRST_GROUP_FIRST_CHOICE_CONSIDERATIONS = "#DROOLS%CADT%FirstGroupFirstSelectionConsiderations#";

    private static final String SECOND_GROUP_FIRST_CHOICE_TEXT = "#DROOLS%CADT%SecondGroupFirstSelectionArchetypeText#";
    private static final String SECOND_GROUP_FIRST_CHOICE_CONSIDERATIONS = "#DROOLS%CADT%SecondGroupFirstSelectionConsiderations#";

    private static final String FIRST_GROUP_FIRST_CHOICE_BULLET_TITLE = "#DROOLS%CADT%FirstGroupFirstSelectionTitle#";
    private static final String FIRST_GROUP_FIRST_CHOICE_ARCHETYPE_BULLET_COLOR = "#DROOLS%CADT%FirstGroupFirstSelectionArchetypeColor#";
    private static final String FIRST_GROUP_FIRST_CHOICE_FIRST_COMPETENCE_BULLET_COLOR = "#DROOLS%CADT%FirstGroupFirstSelectionFirstCompetenceColor#";
    private static final String FIRST_GROUP_FIRST_CHOICE_SECOND_COMPETENCE_BULLET_COLOR = "#DROOLS%CADT%FirstGroupFirstSelectionSecondCompetenceColor#";

    private static final String SECOND_GROUP_FIRST_CHOICE_BULLET_TITLE = "#DROOLS%CADT%SecondGroupFirstSelectionTitle#";
    private static final String SECOND_GROUP_FIRST_CHOICE_ARCHETYPE_BULLET_COLOR = "#DROOLS%CADT%SecondGroupFirstSelectionArchetypeColor#";
    private static final String SECOND_GROUP_FIRST_CHOICE_FIRST_COMPETENCE_BULLET_COLOR = "#DROOLS%CADT%SecondGroupFirstSelectionFirstCompetenceColor#";
    private static final String SECOND_GROUP_FIRST_CHOICE_SECOND_COMPETENCE_BULLET_COLOR = "#DROOLS%CADT%SecondGroupFirstSelectionSecondCompetenceColor#";

    private static final String SECOND_CHOICE_TITLE = "#DROOLS%CADT%SecondSelectionsTitle#";

    private static final String SECOND_CHOICE_INTRODUCTION = "#DROOLS%CADT%SecondSelectionsIntroduction#";

    private static final String SECOND_CHOICE_CARD = "#DROOLS%CADT%SecondSelectionsCard#";

    private static final String FIRST_GROUP_SECOND_CHOICE_TEXT = "#DROOLS%CADT%FirstGroupSecondSelectionArchetypeText#";
    private static final String FIRST_GROUP_SECOND_CHOICE_CONSIDERATIONS = "#DROOLS%CADT%FirstGroupSecondSelectionConsiderations#";
    private static final String SECOND_GROUP_SECOND_CHOICE_TEXT = "#DROOLS%CADT%SecondGroupSecondSelectionArchetypeText#";
    private static final String SECOND_GROUP_SECOND_CHOICE_CONSIDERATIONS = "#DROOLS%CADT%SecondGroupSecondSelectionConsiderations#";

    private static final String FIRST_GROUP_SECOND_CHOICE_BULLET_TITLE = "#DROOLS%CADT%FirstGroupSecondSelectionTitle#";
    private static final String FIRST_GROUP_SECOND_CHOICE_ARCHETYPE_BULLET_COLOR = "#DROOLS%CADT%FirstGroupSecondSelectionArchetypeColor#";
    private static final String FIRST_GROUP_SECOND_CHOICE_FIRST_COMPETENCE_BULLET_COLOR = "#DROOLS%CADT%FirstGroupSecondSelectionFirstCompetenceColor#";
    private static final String FIRST_GROUP_SECOND_CHOICE_SECOND_COMPETENCE_BULLET_COLOR = "#DROOLS%CADT%FirstGroupSecondSelectionSecondCompetenceColor#";

    private static final String SECOND_GROUP_SECOND_CHOICE_BULLET_TITLE = "#DROOLS%CADT%SecondGroupSecondSelectionTitle#";
    private static final String SECOND_GROUP_SECOND_CHOICE_ARCHETYPE_BULLET_COLOR = "#DROOLS%CADT%SecondGroupSecondSelectionArchetypeColor#";
    private static final String SECOND_GROUP_SECOND_CHOICE_FIRST_COMPETENCE_BULLET_COLOR = "#DROOLS%CADT%SecondGroupSecondSelectionFirstCompetenceColor#";
    private static final String SECOND_GROUP_SECOND_CHOICE_SECOND_COMPETENCE_BULLET_COLOR = "#DROOLS%CADT%SecondGroupSecondSelectionSecondCompetenceColor#";

    private SvgTemplate cadtTemplate;

    private final static String DROOLS_FORM_FILE_PATH = "drools/DroolsSubmittedCadtCustomer.json";

    @Autowired
    private DroolsResultController droolsResultController;

    protected String readBase64Image(String imageName) {
        try {
            return new String(Files.readAllBytes(Paths.get(getClass().getClassLoader()
                    .getResource("images" + File.separator + imageName).toURI())));
        } catch (Exception e) {
            Assert.fail("Cannot read resource 'images/" + imageName + "'.");
        }
        return null;
    }

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

        final SvgText title = new SvgText(TITLE, 40, 766L, 239L);
        title.setFontFamily("Arial-BoldMT, Arial, sans-serif");
        title.setFontWeight(FontWeight.BOLD);
        elements.add(title);

        //Logo
        final SvgImage logo = new SvgImage();
        logo.setFromPath("images/NHM-Logo.png");
        logo.getElementAttributes().setXCoordinate(2052L);
        logo.getElementAttributes().setYCoordinate(67L);
        logo.getElementAttributes().setWidth(282L);
        logo.getElementAttributes().setHeight(254L);
        elements.add(logo);


        return elements;
    }

    private List<SvgAreaElement> generateIntroduction() {
        final List<SvgAreaElement> elements = new ArrayList<>();
        final SvgText title = new SvgText(INTRODUCTION_TITLE, 40, 1107L, 351L);
        title.setFontFamily("Arial-BoldMT, Arial, sans-serif");
        title.setFontWeight(FontWeight.BOLD);
        elements.add(title);

        final SvgText text = new SvgText("Arial", INTRODUCTION_CONTENT, 32, 147L, 407L);
        text.setMaxParagraphHeight(188);
        text.setMaxLineWidth(INTRODUCTION_TEXTS_WIDTHS);
        text.setTextAlign(TextAlign.JUSTIFY);
        elements.add(text);

        return elements;
    }

    private List<SvgAreaElement> generateSelectedExample() {
        final List<SvgAreaElement> elements = new ArrayList<>();

        final SvgText selectedText = new SvgText("Arial", "selected", 32, 552L, 729L);
        elements.add(selectedText);

        final SvgCircle scoreCircle1 = new SvgCircle(692L, 691L, 47L);
        scoreCircle1.getElementAttributes().setFill(FIRST_BULLET_COLOR);
        elements.add(scoreCircle1);

        final SvgCircle scoreCircle2 = new SvgCircle(809L, 691L, 47L);
        scoreCircle2.getElementAttributes().setFill(SECOND_BULLET_COLOR);
        elements.add(scoreCircle2);

        final SvgCircle scoreCircle3 = new SvgCircle(928L, 691L, 47L);
        scoreCircle3.getElementAttributes().setFill(THIRD_BULLET_COLOR);
        elements.add(scoreCircle3);

        final SvgCircle scoreCircle4 = new SvgCircle(1043L, 691L, 47L);
        scoreCircle4.getElementAttributes().setFill(FORTH_BULLET_COLOR);
        elements.add(scoreCircle4);

        final SvgCircle emptyCircle1 = new SvgCircle(1340L, 691L, 47L);
        emptyCircle1.getElementAttributes().setFill("ffffffff");
        emptyCircle1.getElementStroke().setStrokeWidth(DEFAULT_STROKE_WIDTH);
        emptyCircle1.getElementStroke().setStrokeColor(FIRST_BULLET_COLOR);
        elements.add(emptyCircle1);

        final SvgCircle emptyCircle2 = new SvgCircle(1457L, 691L, 47L);
        emptyCircle2.getElementAttributes().setFill("ffffffff");
        emptyCircle2.getElementStroke().setStrokeWidth(DEFAULT_STROKE_WIDTH);
        emptyCircle2.getElementStroke().setStrokeColor(SECOND_BULLET_COLOR);
        elements.add(emptyCircle2);

        final SvgCircle emptyCircle3 = new SvgCircle(1574L, 691L, 47L);
        emptyCircle3.getElementAttributes().setFill("ffffffff");
        emptyCircle3.getElementStroke().setStrokeWidth(DEFAULT_STROKE_WIDTH);
        emptyCircle3.getElementStroke().setStrokeColor(THIRD_BULLET_COLOR);
        elements.add(emptyCircle3);

        final SvgCircle emptyCircle4 = new SvgCircle(1691L, 691L, 47L);
        emptyCircle4.getElementAttributes().setFill("ffffffff");
        emptyCircle4.getElementStroke().setStrokeWidth(DEFAULT_STROKE_WIDTH);
        emptyCircle4.getElementStroke().setStrokeColor(FORTH_BULLET_COLOR);
        elements.add(emptyCircle4);

        final SvgText notSelectedText = new SvgText("Arial", "not selected", 32, 1813L, 724L);
        elements.add(notSelectedText);

        return elements;
    }

    private List<SvgAreaElement> generatePrimaryValuesTitle() {
        final List<SvgAreaElement> elements = new ArrayList<>();
        final SvgText title = new SvgText("YOUR PRIMARY NATURAL POWER AND NATURAL POTENTIAL", 36, 700L, 893L);
        title.setFontFamily("Arial-BoldMT, Arial, sans-serif");
        title.setFontWeight(FontWeight.BOLD);
        elements.add(title);

        final SvgRectangle rectangle = new SvgRectangle(619L, 948L, "1248px", "59px", "f0edebff");
        elements.add(rectangle);

        final SvgText rectangleText = new SvgText("Arial", FIRST_CHOICE_TITLE, 32, 1243L, 964L);
        rectangleText.setMaxParagraphHeight(59);
        rectangleText.setMaxLineWidth(1248);
        rectangleText.setTextAlign(TextAlign.CENTER);
        rectangleText.getElementAttributes().setVerticalAlignment(VerticalAlignment.MIDDLE);
        elements.add(rectangleText);

        final SvgText paragraphText = new SvgText("Arial", FIRST_CHOICE_INTRODUCTION, 32, 147L, 1035L);
        paragraphText.setMaxParagraphHeight(66);
        paragraphText.setMaxLineWidth(INTRODUCTION_TEXTS_WIDTHS);
        paragraphText.setTextAlign(TextAlign.JUSTIFY);
        elements.add(paragraphText);

        return elements;
    }

    private List<SvgAreaElement> generatePrimaryCard() {
        final List<SvgAreaElement> elements = new ArrayList<>();
        final SvgImage svgImage = new SvgImage(new ElementAttributes(1043L, 1411L, "400", "600"),
                "images/" + FIRST_CHOICE_CARD, true);
        elements.add(svgImage);
        return elements;
    }

    private List<SvgAreaElement> generatePrimaryValuesCircles() {
        final List<SvgAreaElement> elements = new ArrayList<>();

        final SvgText firstGroupTitle = new SvgText("Sofia Sans Extra Condensed", FIRST_GROUP_FIRST_CHOICE_BULLET_TITLE, 32, 1115L, 1171L);
        firstGroupTitle.setMaxParagraphHeight(59);
        firstGroupTitle.setMaxLineWidth(1248);
        firstGroupTitle.setTextAlign(TextAlign.CENTER);
        firstGroupTitle.getElementAttributes().setVerticalAlignment(VerticalAlignment.MIDDLE);
        firstGroupTitle.getElementAttributes().setFill(FIRST_GROUP_FIRST_CHOICE_ARCHETYPE_BULLET_COLOR);
        firstGroupTitle.setFontWeight(FontWeight.BOLD);
        elements.add(firstGroupTitle);

        final SvgLine firstGroupLine = new SvgLine(FIRST_GROUP_FIRST_CHOICE_ARCHETYPE_BULLET_COLOR, DEFAULT_STROKE_WIDTH, 1119L, 1300L, 1119L, 1640L);
        elements.add(firstGroupLine);

        final SvgCircle firstScoreCircleBorder = new SvgCircle(1062L, 1233L, 56L);
        firstScoreCircleBorder.getElementAttributes().setFill("ffffff");
        firstScoreCircleBorder.getElementStroke().setStrokeColor(FIRST_GROUP_FIRST_CHOICE_ARCHETYPE_BULLET_COLOR);
        firstScoreCircleBorder.getElementStroke().setStrokeWidth(DEFAULT_STROKE_WIDTH);
        elements.add(firstScoreCircleBorder);

        final SvgCircle firstScoreCircle1 = new SvgCircle(1075L, 1246L, 43L);
        firstScoreCircle1.getElementAttributes().setFill(FIRST_GROUP_FIRST_CHOICE_ARCHETYPE_BULLET_COLOR);
        firstScoreCircle1.getElementStroke().setStrokeColor(FIRST_GROUP_FIRST_CHOICE_ARCHETYPE_BULLET_COLOR);
        firstScoreCircle1.getElementStroke().setStrokeWidth(DEFAULT_STROKE_WIDTH);
        elements.add(firstScoreCircle1);

        final SvgCircle firstScoreCircle2 = new SvgCircle(1075L, 1437L, 43L);
        firstScoreCircle2.getElementAttributes().setFill(FIRST_GROUP_FIRST_CHOICE_FIRST_COMPETENCE_BULLET_COLOR);
        firstScoreCircle2.getElementStroke().setStrokeColor(FIRST_GROUP_FIRST_CHOICE_ARCHETYPE_BULLET_COLOR);
        firstScoreCircle2.getElementStroke().setStrokeWidth(DEFAULT_STROKE_WIDTH);
        elements.add(firstScoreCircle2);

        final SvgCircle firstScoreCircle3 = new SvgCircle(1075L, 1570L, 43L);
        firstScoreCircle3.getElementAttributes().setFill(FIRST_GROUP_FIRST_CHOICE_SECOND_COMPETENCE_BULLET_COLOR);
        firstScoreCircle3.getElementStroke().setStrokeColor(FIRST_GROUP_FIRST_CHOICE_ARCHETYPE_BULLET_COLOR);
        firstScoreCircle3.getElementStroke().setStrokeWidth(DEFAULT_STROKE_WIDTH);
        elements.add(firstScoreCircle3);

        final SvgText secondGroupTitle = new SvgText("Sofia Sans Extra Condensed", SECOND_GROUP_FIRST_CHOICE_BULLET_TITLE, 32, 1365L, 1171L);
        secondGroupTitle.setMaxParagraphHeight(59);
        secondGroupTitle.setMaxLineWidth(1248);
        secondGroupTitle.setTextAlign(TextAlign.CENTER);
        secondGroupTitle.getElementAttributes().setVerticalAlignment(VerticalAlignment.MIDDLE);
        secondGroupTitle.getElementAttributes().setFill(SECOND_GROUP_FIRST_CHOICE_ARCHETYPE_BULLET_COLOR);
        secondGroupTitle.setFontWeight(FontWeight.BOLD);
        elements.add(secondGroupTitle);

        final SvgLine secondGroupLine = new SvgLine(SECOND_GROUP_FIRST_CHOICE_ARCHETYPE_BULLET_COLOR, DEFAULT_STROKE_WIDTH, 1365L, 1300L, 1365L, 1640L);
        elements.add(secondGroupLine);

        final SvgCircle secondScoreCircleBorder = new SvgCircle(1308L, 1233L, 56L);
        secondScoreCircleBorder.getElementAttributes().setFill("ffffff");
        secondScoreCircleBorder.getElementStroke().setStrokeColor(SECOND_GROUP_FIRST_CHOICE_ARCHETYPE_BULLET_COLOR);
        secondScoreCircleBorder.getElementStroke().setStrokeWidth(DEFAULT_STROKE_WIDTH);
        elements.add(secondScoreCircleBorder);

        final SvgCircle secondScoreCircle1 = new SvgCircle(1321L, 1246L, 43L);
        secondScoreCircle1.getElementAttributes().setFill(SECOND_GROUP_FIRST_CHOICE_ARCHETYPE_BULLET_COLOR);
        secondScoreCircle1.getElementStroke().setStrokeColor(SECOND_GROUP_FIRST_CHOICE_ARCHETYPE_BULLET_COLOR);
        secondScoreCircle1.getElementStroke().setStrokeWidth(DEFAULT_STROKE_WIDTH);
        elements.add(secondScoreCircle1);

        final SvgCircle secondScoreCircle2 = new SvgCircle(1321L, 1437L, 43L);
        secondScoreCircle2.getElementAttributes().setFill(SECOND_GROUP_FIRST_CHOICE_FIRST_COMPETENCE_BULLET_COLOR);
        secondScoreCircle2.getElementStroke().setStrokeColor(SECOND_GROUP_FIRST_CHOICE_ARCHETYPE_BULLET_COLOR);
        secondScoreCircle2.getElementStroke().setStrokeWidth(DEFAULT_STROKE_WIDTH);
        elements.add(secondScoreCircle2);

        final SvgCircle secondScoreCircle3 = new SvgCircle(1321L, 1570L, 43L);
        secondScoreCircle3.getElementAttributes().setFill(SECOND_GROUP_FIRST_CHOICE_SECOND_COMPETENCE_BULLET_COLOR);
        secondScoreCircle3.getElementStroke().setStrokeColor(SECOND_GROUP_FIRST_CHOICE_ARCHETYPE_BULLET_COLOR);
        secondScoreCircle3.getElementStroke().setStrokeWidth(DEFAULT_STROKE_WIDTH);
        elements.add(secondScoreCircle3);


        return elements;
    }

    private List<SvgAreaElement> generateSecondaryCard() {
        final List<SvgAreaElement> elements = new ArrayList<>();
        final SvgImage svgImage = new SvgImage(new ElementAttributes(1043L, 2637L, "400", "600"),
                "images/" + SECOND_CHOICE_CARD, true);
        elements.add(svgImage);
        return elements;
    }

    private List<SvgAreaElement> generateSecondaryValuesCircles() {
        final List<SvgAreaElement> elements = new ArrayList<>();

        final SvgText firstGroupTitle = new SvgText("Sofia Sans Extra Condensed", FIRST_GROUP_SECOND_CHOICE_BULLET_TITLE, 32, 1115L, 2399L);
        firstGroupTitle.setMaxParagraphHeight(59);
        firstGroupTitle.setMaxLineWidth(1248);
        firstGroupTitle.setTextAlign(TextAlign.CENTER);
        firstGroupTitle.getElementAttributes().setVerticalAlignment(VerticalAlignment.MIDDLE);
        firstGroupTitle.getElementAttributes().setFill(FIRST_GROUP_SECOND_CHOICE_ARCHETYPE_BULLET_COLOR);
        firstGroupTitle.setFontWeight(FontWeight.BOLD);
        elements.add(firstGroupTitle);

        final SvgLine firstGroupLine = new SvgLine(FIRST_GROUP_SECOND_CHOICE_ARCHETYPE_BULLET_COLOR, DEFAULT_STROKE_WIDTH, 1119L, 2550L, 1119L, 2850L);
        elements.add(firstGroupLine);

        final SvgCircle firstScoreCircleBorder = new SvgCircle(1062L, 2458L, 56L);
        firstScoreCircleBorder.getElementAttributes().setFill("ffffff");
        firstScoreCircleBorder.getElementStroke().setStrokeColor(FIRST_GROUP_SECOND_CHOICE_ARCHETYPE_BULLET_COLOR);
        firstScoreCircleBorder.getElementStroke().setStrokeWidth(DEFAULT_STROKE_WIDTH);
        elements.add(firstScoreCircleBorder);

        final SvgCircle firstScoreCircle1 = new SvgCircle(1075L, 2471L, 43L);
        firstScoreCircle1.getElementAttributes().setFill(FIRST_GROUP_SECOND_CHOICE_ARCHETYPE_BULLET_COLOR);
        firstScoreCircle1.getElementStroke().setStrokeColor(FIRST_GROUP_SECOND_CHOICE_ARCHETYPE_BULLET_COLOR);
        firstScoreCircle1.getElementStroke().setStrokeWidth(DEFAULT_STROKE_WIDTH);
        elements.add(firstScoreCircle1);

        final SvgCircle firstScoreCircle2 = new SvgCircle(1075L, 2662L, 43L);
        firstScoreCircle2.getElementAttributes().setFill(FIRST_GROUP_SECOND_CHOICE_FIRST_COMPETENCE_BULLET_COLOR);
        firstScoreCircle2.getElementStroke().setStrokeColor(FIRST_GROUP_SECOND_CHOICE_ARCHETYPE_BULLET_COLOR);
        firstScoreCircle2.getElementStroke().setStrokeWidth(DEFAULT_STROKE_WIDTH);
        elements.add(firstScoreCircle2);

        final SvgCircle firstScoreCircle3 = new SvgCircle(1075L, 2791L, 43L);
        firstScoreCircle3.getElementAttributes().setFill(FIRST_GROUP_SECOND_CHOICE_SECOND_COMPETENCE_BULLET_COLOR);
        firstScoreCircle3.getElementStroke().setStrokeColor(FIRST_GROUP_SECOND_CHOICE_ARCHETYPE_BULLET_COLOR);
        firstScoreCircle3.getElementStroke().setStrokeWidth(DEFAULT_STROKE_WIDTH);
        elements.add(firstScoreCircle3);

        final SvgText secondGroupTitle = new SvgText("Sofia Sans Extra Condensed", SECOND_GROUP_SECOND_CHOICE_BULLET_TITLE, 32, 1365L, 2399L);
        secondGroupTitle.setMaxParagraphHeight(59);
        secondGroupTitle.setMaxLineWidth(1248);
        secondGroupTitle.setTextAlign(TextAlign.CENTER);
        secondGroupTitle.getElementAttributes().setVerticalAlignment(VerticalAlignment.MIDDLE);
        secondGroupTitle.getElementAttributes().setFill(SECOND_GROUP_SECOND_CHOICE_ARCHETYPE_BULLET_COLOR);
        secondGroupTitle.setFontWeight(FontWeight.BOLD);
        elements.add(secondGroupTitle);

        final SvgLine secondGroupLine = new SvgLine(SECOND_GROUP_SECOND_CHOICE_ARCHETYPE_BULLET_COLOR, DEFAULT_STROKE_WIDTH, 1365L, 2550L, 1365L, 2850L);
        elements.add(secondGroupLine);

        final SvgCircle secondScoreCircleBorder = new SvgCircle(1308L, 2458L, 56L);
        secondScoreCircleBorder.getElementAttributes().setFill("ffffff");
        secondScoreCircleBorder.getElementStroke().setStrokeColor(SECOND_GROUP_SECOND_CHOICE_ARCHETYPE_BULLET_COLOR);
        secondScoreCircleBorder.getElementStroke().setStrokeWidth(DEFAULT_STROKE_WIDTH);
        elements.add(secondScoreCircleBorder);

        final SvgCircle secondScoreCircle1 = new SvgCircle(1321L, 2471L, 43L);
        secondScoreCircle1.getElementAttributes().setFill(SECOND_GROUP_SECOND_CHOICE_ARCHETYPE_BULLET_COLOR);
        secondScoreCircle1.getElementStroke().setStrokeColor(SECOND_GROUP_SECOND_CHOICE_ARCHETYPE_BULLET_COLOR);
        secondScoreCircle1.getElementStroke().setStrokeWidth(DEFAULT_STROKE_WIDTH);
        elements.add(secondScoreCircle1);

        final SvgCircle secondScoreCircle2 = new SvgCircle(1321L, 2662L, 43L);
        secondScoreCircle2.getElementAttributes().setFill(SECOND_GROUP_SECOND_CHOICE_FIRST_COMPETENCE_BULLET_COLOR);
        secondScoreCircle2.getElementStroke().setStrokeColor(SECOND_GROUP_SECOND_CHOICE_ARCHETYPE_BULLET_COLOR);
        secondScoreCircle2.getElementStroke().setStrokeWidth(DEFAULT_STROKE_WIDTH);
        elements.add(secondScoreCircle2);

        final SvgCircle secondScoreCircle3 = new SvgCircle(1321L, 2791L, 43L);
        secondScoreCircle3.getElementAttributes().setFill(SECOND_GROUP_SECOND_CHOICE_SECOND_COMPETENCE_BULLET_COLOR);
        secondScoreCircle3.getElementStroke().setStrokeColor(SECOND_GROUP_SECOND_CHOICE_ARCHETYPE_BULLET_COLOR);
        secondScoreCircle3.getElementStroke().setStrokeWidth(DEFAULT_STROKE_WIDTH);
        elements.add(secondScoreCircle3);

        return elements;
    }

    private List<SvgAreaElement> generatePrimaryValuesTexts() {
        final List<SvgAreaElement> elements = new ArrayList<>();

        final SvgText paragraphLeftText = new SvgText("Arial", FIRST_GROUP_FIRST_CHOICE_TEXT,
                32, 147L, 1157L);
        paragraphLeftText.setMaxParagraphHeight(246);
        paragraphLeftText.setMaxLineWidth(GROUP_TEXTS_WIDTHS);
        paragraphLeftText.setTextAlign(TextAlign.JUSTIFY);
        elements.add(paragraphLeftText);

        final SvgText paragraphLeftConsiderationsText = new SvgText("Arial", FIRST_GROUP_FIRST_CHOICE_CONSIDERATIONS,
                32, 147L, 1439L);
        paragraphLeftConsiderationsText.setMaxParagraphHeight(400);
        paragraphLeftConsiderationsText.setMaxLineWidth(GROUP_TEXTS_WIDTHS);
        paragraphLeftConsiderationsText.setTextAlign(TextAlign.JUSTIFY);
        elements.add(paragraphLeftConsiderationsText);

        final SvgText paragraphRightText = new SvgText("Arial", SECOND_GROUP_FIRST_CHOICE_TEXT,
                32, 1490L, 1157L);
        paragraphRightText.setMaxParagraphHeight(246);
        paragraphRightText.setMaxLineWidth(GROUP_TEXTS_WIDTHS);
        paragraphRightText.setTextAlign(TextAlign.JUSTIFY);
        elements.add(paragraphRightText);

        final SvgText paragraphRightConsiderationsText = new SvgText("Arial", SECOND_GROUP_FIRST_CHOICE_CONSIDERATIONS,
                32, 1490L, 1439L);
        paragraphRightConsiderationsText.setMaxParagraphHeight(400);
        paragraphRightConsiderationsText.setMaxLineWidth(GROUP_TEXTS_WIDTHS);
        paragraphRightConsiderationsText.setTextAlign(TextAlign.JUSTIFY);
        elements.add(paragraphRightConsiderationsText);

        return elements;
    }

    private List<SvgAreaElement> generateSecondaryValuesTitle() {
        final List<SvgAreaElement> elements = new ArrayList<>();
        final SvgText title = new SvgText("YOUR SECONDARY NATURAL POWER AND NATURAL POTENTIAL", 36, 668L, 2118L);
        title.setFontFamily("Arial-BoldMT, Arial, sans-serif");
        title.setFontWeight(FontWeight.BOLD);
        elements.add(title);

        final SvgRectangle rectangle = new SvgRectangle(619L, 2173L, "1248px", "59px", "f0edebff");
        elements.add(rectangle);

        final SvgText rectangleText = new SvgText("Arial", SECOND_CHOICE_TITLE, 32, 1243L, 2190L);
        rectangleText.setMaxParagraphHeight(59);
        rectangleText.setMaxLineWidth(1248);
        rectangleText.setTextAlign(TextAlign.CENTER);
        rectangleText.getElementAttributes().setVerticalAlignment(VerticalAlignment.MIDDLE);
        elements.add(rectangleText);

        final SvgText paragraphText = new SvgText("Arial", SECOND_CHOICE_INTRODUCTION, 32, 147L, 2260L);
        paragraphText.setMaxParagraphHeight(66);
        paragraphText.setMaxLineWidth(INTRODUCTION_TEXTS_WIDTHS);
        paragraphText.setTextAlign(TextAlign.JUSTIFY);
        elements.add(paragraphText);

        return elements;
    }

    private List<SvgAreaElement> generateSecondaryValuesTexts() {
        final List<SvgAreaElement> elements = new ArrayList<>();

        final SvgText paragraphLeftText = new SvgText("Arial", FIRST_GROUP_SECOND_CHOICE_TEXT,
                32, 147L, 2382L);
        paragraphLeftText.setMaxParagraphHeight(246);
        paragraphLeftText.setMaxLineWidth(800);
        paragraphLeftText.setTextAlign(TextAlign.JUSTIFY);
        elements.add(paragraphLeftText);

        final SvgText paragraphLeftConsiderationsText = new SvgText("Arial", FIRST_GROUP_SECOND_CHOICE_CONSIDERATIONS,
                32, 147L, 2664L);
        paragraphLeftConsiderationsText.setMaxParagraphHeight(400);
        paragraphLeftConsiderationsText.setMaxLineWidth(800);
        paragraphLeftConsiderationsText.setTextAlign(TextAlign.JUSTIFY);
        elements.add(paragraphLeftConsiderationsText);

        final SvgText paragraphRightText = new SvgText("Arial", SECOND_GROUP_SECOND_CHOICE_TEXT,
                32, 1490L, 2382L);
        paragraphRightText.setMaxParagraphHeight(246);
        paragraphRightText.setMaxLineWidth(800);
        paragraphRightText.setTextAlign(TextAlign.JUSTIFY);
        elements.add(paragraphRightText);

        final SvgText paragraphRightConsiderationsText = new SvgText("Arial", SECOND_GROUP_SECOND_CHOICE_CONSIDERATIONS,
                32, 1490L, 2664L);
        paragraphRightConsiderationsText.setMaxParagraphHeight(400);
        paragraphRightConsiderationsText.setMaxLineWidth(800);
        paragraphRightConsiderationsText.setTextAlign(TextAlign.JUSTIFY);
        elements.add(paragraphRightConsiderationsText);

        return elements;
    }

    private List<SvgAreaElement> generateIntellectualPropertyTexts() {
        final List<SvgAreaElement> elements = new ArrayList<>();

        final SvgText text = new SvgText("Intellectual property disclaim...", 32, 550L, 3346L);
        text.setFontFamily("Arial-BoldMT, Arial, sans-serif");
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
        cadtTemplate.addElements(generatePrimaryValuesTitle());
        cadtTemplate.addElements(generatePrimaryCard());
        cadtTemplate.addElements(generatePrimaryValuesCircles());
        cadtTemplate.addElements(generatePrimaryValuesTexts());
        cadtTemplate.addElements(generateSecondaryValuesTitle());
        cadtTemplate.addElements(generateSecondaryCard());
        cadtTemplate.addElements(generateSecondaryValuesCircles());
        cadtTemplate.addElements(generateSecondaryValuesTexts());
        cadtTemplate.addElements(generateIntellectualPropertyTexts());


        try (PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(OUTPUT_FOLDER
                + File.separator + "CADT_Customer.svg")), true)) {
            out.println(SvgGenerator.generate(cadtTemplate));
        }
    }

    @Test(dependsOnMethods = "generateCADT")
    public void executeCadt() throws IOException {
        final DroolsSubmittedForm droolsSubmittedForm = DroolsSubmittedForm.getFromJson(FileReader.getResource(DROOLS_FORM_FILE_PATH, StandardCharsets.UTF_8));
        final List<String> svgResults = droolsResultController.execute(droolsSubmittedForm, Collections.singletonList(cadtTemplate));
        Assert.assertEquals(svgResults.size(), 1);

        try (PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(OUTPUT_FOLDER
                + File.separator + "cadtCustomerFromDrools.svg")), true)) {
            out.println(svgResults.get(0));
        }

        checkContent(svgResults.get(0), "cadtCustomerFromDrools.svg");
    }

    @AfterClass
    public void removeFolder() {
        Assert.assertTrue(deleteDirectory(new File(OUTPUT_FOLDER)));
    }
}
