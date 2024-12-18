package com.biit.infographic.core.svg;

import com.biit.drools.form.DroolsSubmittedForm;
import com.biit.infographic.core.controllers.DroolsResultController;
import com.biit.infographic.core.generators.SvgGenerator;
import com.biit.infographic.core.models.svg.SvgAreaElement;
import com.biit.infographic.core.models.svg.SvgBackground;
import com.biit.infographic.core.models.svg.SvgTemplate;
import com.biit.infographic.core.models.svg.components.SvgCircle;
import com.biit.infographic.core.models.svg.components.SvgCircleSector;
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
@Test(groups = "frustrationOnTeamworking")
public class The5FrustrationsOnTeamworkingTeamTest extends AbstractTestNGSpringContextTests {

    protected static final String OUTPUT_FOLDER = System.getProperty("java.io.tmpdir") + File.separator + "SvgTests";

    private static final String DROOLS_FORM_FILE_PATH = "drools/Frustration On Teamworking Team.json";
    protected static final String TEMPLATE_ID = "five_frustrations_team";

    private static final String TEMPLATE_NAME = "Frustration On Teamworking Team";
    private static final String TEMPLATE_BACKGROUND_COLOR = "ffffffff";

    private static final String FONT = "Montserrat";
    private static final int TITLE_FONT_SIZE = 20;
    private static final int SUBTITLE_FONT_SIZE = 16;
    private static final int PARAGRAPH_FONT_SIZE = 12;
    private static final int PARAGRAPH_WIDTH = 676;

    private static final int CIRCLE_RADIUS = 30;
    private static final String CIRCLE_COLOR = "f20d5eff";
    private static final String CIRCLE_BACKGROUND = "edededff";

    private static final String SUBMIT_TIME = "#FORM%SUBMIT%TIME#";
    private static final String SUBMIT_DATE = "#FORM%SUBMIT%DATE#";

    private static final String FRUSTRATION_1_CIRCLE = "#DROOLS%Frustration On Teamworking Team%Frustration1#";
    private static final String FRUSTRATION_2_CIRCLE = "#DROOLS%Frustration On Teamworking Team%Frustration2#";
    private static final String FRUSTRATION_3_CIRCLE = "#DROOLS%Frustration On Teamworking Team%Frustration3#";
    private static final String FRUSTRATION_4_CIRCLE = "#DROOLS%Frustration On Teamworking Team%Frustration4#";
    private static final String FRUSTRATION_5_CIRCLE = "#DROOLS%Frustration On Teamworking Team%Frustration5#";

    private static final String FRUSTRATION_1_VALUE = "#DROOLS%Frustration On Teamworking Team%Frustration1Total#";
    private static final String FRUSTRATION_2_VALUE = "#DROOLS%Frustration On Teamworking Team%Frustration2Total#";
    private static final String FRUSTRATION_3_VALUE = "#DROOLS%Frustration On Teamworking Team%Frustration3Total#";
    private static final String FRUSTRATION_4_VALUE = "#DROOLS%Frustration On Teamworking Team%Frustration4Total#";
    private static final String FRUSTRATION_5_VALUE = "#DROOLS%Frustration On Teamworking Team%Frustration5Total#";


    @Autowired
    private DroolsResultController droolsResultController;

    private SvgTemplate frustrationOnTeamworking;

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
        svgBackground.setBackgroundColor(TEMPLATE_BACKGROUND_COLOR);
        return svgBackground;
    }


    private List<SvgAreaElement> generateTitle() {
        final List<SvgAreaElement> elements = new ArrayList<>();

        final SvgText title1 = new SvgText(FONT, "De 5 frustraties van teamwork", TITLE_FONT_SIZE, 249, 73);
        elements.add(title1);

        final SvgText title2 = new SvgText(FONT, "Hoe zorg je dat het samenwerken leuk blijft", SUBTITLE_FONT_SIZE, 225, 95);
        title2.setFontWeight(FontWeight.BOLD);
        elements.add(title2);

        return elements;
    }

    private List<SvgAreaElement> generateFrustration1(int hight) {
        final List<SvgAreaElement> elements = new ArrayList<>();

        final SvgText title = new SvgText(FONT, "Frustratie 1: Gebrek aan vertrouwen", TITLE_FONT_SIZE, 222, hight);
        title.setFontWeight(FontWeight.BOLD);
        elements.add(title);

        final SvgCircle circleBackground = new SvgCircle(370, hight + 37, CIRCLE_RADIUS, CIRCLE_BACKGROUND);
        elements.add(circleBackground);

        final SvgCircleSector scoreCircle = new SvgCircleSector(370, hight + 37, CIRCLE_RADIUS);
        scoreCircle.setPercentage(FRUSTRATION_1_CIRCLE);
        scoreCircle.getElementAttributes().setFill(CIRCLE_COLOR);
        scoreCircle.getElementStroke().setStrokeWidth(0);
        elements.add(scoreCircle);

        final SvgText value = new SvgText(FRUSTRATION_1_VALUE, 22, 400, hight + 61);
        value.setTextAlign(TextAlign.CENTER);
        elements.add(value);

        final SvgText paragraph = new SvgText(FONT, The5FrustrationsOnTeamworkingTest.FRUSTRATION_1_DUTCH_TEXT, PARAGRAPH_FONT_SIZE, 62, hight + 111);
        paragraph.setMaxLineWidth(PARAGRAPH_WIDTH);
        paragraph.setTextAlign(TextAlign.JUSTIFY);
        elements.add(paragraph);

        return elements;
    }

    private List<SvgAreaElement> generateFrustration2(int hight) {
        final List<SvgAreaElement> elements = new ArrayList<>();

        final SvgText title = new SvgText(FONT, "Frustratie 2: De angst voor conflicten en confrontatie", TITLE_FONT_SIZE, 136, hight);
        title.setFontWeight(FontWeight.BOLD);
        elements.add(title);

        final SvgCircle circleBackground = new SvgCircle(370, hight + 37, CIRCLE_RADIUS, CIRCLE_BACKGROUND);
        elements.add(circleBackground);

        final SvgCircleSector scoreCircle = new SvgCircleSector(370, hight + 37, CIRCLE_RADIUS);
        scoreCircle.setPercentage(FRUSTRATION_2_CIRCLE);
        scoreCircle.getElementAttributes().setFill(CIRCLE_COLOR);
        scoreCircle.getElementStroke().setStrokeWidth(0);
        elements.add(scoreCircle);

        final SvgText value = new SvgText(FRUSTRATION_2_VALUE, 22, 400, hight + 61);
        value.setTextAlign(TextAlign.CENTER);
        elements.add(value);

        final SvgText paragraph = new SvgText(FONT, The5FrustrationsOnTeamworkingTest.FRUSTRATION_2_DUTCH_TEXT, PARAGRAPH_FONT_SIZE, 62, hight + 111);
        paragraph.setMaxLineWidth(PARAGRAPH_WIDTH);
        paragraph.setTextAlign(TextAlign.JUSTIFY);
        elements.add(paragraph);

        return elements;
    }

    private List<SvgAreaElement> generateFrustration3(int hight) {
        final List<SvgAreaElement> elements = new ArrayList<>();

        final SvgText title = new SvgText(FONT, "Frustratie 3: Gebrek aan betrokkenheid", TITLE_FONT_SIZE, 204, hight);
        title.setFontWeight(FontWeight.BOLD);
        elements.add(title);

        final SvgCircle circleBackground = new SvgCircle(370, hight + 37, CIRCLE_RADIUS, CIRCLE_BACKGROUND);
        elements.add(circleBackground);

        final SvgCircleSector scoreCircle = new SvgCircleSector(370, hight + 37, CIRCLE_RADIUS);
        scoreCircle.setPercentage(FRUSTRATION_3_CIRCLE);
        scoreCircle.getElementAttributes().setFill(CIRCLE_COLOR);
        scoreCircle.getElementStroke().setStrokeWidth(0);
        elements.add(scoreCircle);

        final SvgText value = new SvgText(FRUSTRATION_3_VALUE, 22, 400, hight + 61);
        value.setTextAlign(TextAlign.CENTER);
        elements.add(value);

        final SvgText paragraph = new SvgText(FONT, The5FrustrationsOnTeamworkingTest.FRUSTRATION_3_DUTCH_TEXT, PARAGRAPH_FONT_SIZE, 62, hight + 111);
        paragraph.setMaxLineWidth(PARAGRAPH_WIDTH);
        paragraph.setTextAlign(TextAlign.JUSTIFY);
        elements.add(paragraph);

        return elements;
    }

    private List<SvgAreaElement> generateFrustration4(int hight) {
        final List<SvgAreaElement> elements = new ArrayList<>();

        final SvgText title = new SvgText(FONT, "Frustratie 4: Het ontlopen of afschuiven van verantwoordelijkheid", TITLE_FONT_SIZE, 73, hight);
        title.setFontWeight(FontWeight.BOLD);
        elements.add(title);

        final SvgCircle circleBackground = new SvgCircle(370, hight + 37, CIRCLE_RADIUS, CIRCLE_BACKGROUND);
        elements.add(circleBackground);

        final SvgCircleSector scoreCircle = new SvgCircleSector(370, hight + 37, CIRCLE_RADIUS);
        scoreCircle.setPercentage(FRUSTRATION_4_CIRCLE);
        scoreCircle.getElementAttributes().setFill(CIRCLE_COLOR);
        scoreCircle.getElementStroke().setStrokeWidth(0);
        elements.add(scoreCircle);

        final SvgText value = new SvgText(FRUSTRATION_4_VALUE, 22, 400, hight + 61);
        value.setTextAlign(TextAlign.CENTER);
        elements.add(value);

        final SvgText paragraph = new SvgText(FONT, The5FrustrationsOnTeamworkingTest.FRUSTRATION_4_DUTCH_TEXT, PARAGRAPH_FONT_SIZE, 62, hight + 111);
        paragraph.setMaxLineWidth(PARAGRAPH_WIDTH);
        paragraph.setTextAlign(TextAlign.JUSTIFY);
        elements.add(paragraph);

        return elements;
    }

    private List<SvgAreaElement> generateFrustration5(int hight) {
        final List<SvgAreaElement> elements = new ArrayList<>();

        final SvgText title = new SvgText(FONT, "Frustratie 5: Te weinig aandacht voor resultaten.", TITLE_FONT_SIZE, 160, hight);
        title.setFontWeight(FontWeight.BOLD);
        elements.add(title);

        final SvgCircle circleBackground = new SvgCircle(370, hight + 37, CIRCLE_RADIUS, CIRCLE_BACKGROUND);
        elements.add(circleBackground);

        final SvgCircleSector scoreCircle = new SvgCircleSector(370, hight + 37, CIRCLE_RADIUS);
        scoreCircle.setPercentage(FRUSTRATION_5_CIRCLE);
        scoreCircle.getElementAttributes().setFill(CIRCLE_COLOR);
        scoreCircle.getElementStroke().setStrokeWidth(0);
        elements.add(scoreCircle);

        final SvgText value = new SvgText(FRUSTRATION_5_VALUE, 22, 400, hight + 61);
        value.setTextAlign(TextAlign.CENTER);
        elements.add(value);

        final SvgText paragraph = new SvgText(FONT, The5FrustrationsOnTeamworkingTest.FRUSTRATION_5_DUTCH_TEXT, PARAGRAPH_FONT_SIZE, 62, hight + 111);
        paragraph.setMaxLineWidth(PARAGRAPH_WIDTH);
        paragraph.setTextAlign(TextAlign.JUSTIFY);
        elements.add(paragraph);

        return elements;
    }

    private List<SvgAreaElement> generateFooter() {
        final List<SvgAreaElement> elements = new ArrayList<>();

        final SvgText date = new SvgText(SUBMIT_DATE + " " + SUBMIT_TIME, 14, 737, 1450);
        date.setTextAlign(TextAlign.RIGHT);
        elements.add(date);

        return elements;
    }


    @BeforeClass
    public void prepareFolder() throws IOException {
        Files.createDirectories(Paths.get(OUTPUT_FOLDER));
    }

    @Test
    public void generateFrustrationAtTeamworking() {
        frustrationOnTeamworking = new SvgTemplate();
        frustrationOnTeamworking.setUuid(TEMPLATE_ID);
        frustrationOnTeamworking.getElementAttributes().setWidth(800);
        frustrationOnTeamworking.getElementAttributes().setHeight(1500);
        frustrationOnTeamworking.setSvgBackground(generateBackground());

        frustrationOnTeamworking.addElements(generateTitle());
        frustrationOnTeamworking.addElements(generateFrustration1(152));
        frustrationOnTeamworking.addElements(generateFrustration2(423));
        frustrationOnTeamworking.addElements(generateFrustration3(660));
        frustrationOnTeamworking.addElements(generateFrustration4(896));
        frustrationOnTeamworking.addElements(generateFrustration5(1165));

        frustrationOnTeamworking.addElements(generateFooter());
    }

    @Test(dependsOnMethods = "generateFrustrationAtTeamworking")
    public void executeFrustrationAtTeamworking() throws IOException {
        FontFactory.resetFonts();
        final DroolsSubmittedForm droolsSubmittedForm = DroolsSubmittedForm.getFromJson(FileReader.getResource(DROOLS_FORM_FILE_PATH, StandardCharsets.UTF_8));
        final List<String> svgResults = droolsResultController.execute(droolsSubmittedForm, Collections.singletonList(frustrationOnTeamworking));
        Assert.assertEquals(svgResults.size(), 1);

        try (PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(OUTPUT_FOLDER
                + File.separator + TEMPLATE_NAME + ".svg")), true)) {
            out.println(svgResults.get(0));
        }

        checkContent(svgResults.get(0), TEMPLATE_NAME + ".svg");
    }

    @Test(dependsOnMethods = "executeFrustrationAtTeamworking")
    public void checkSerialization() throws JsonProcessingException {
        //boardingPassTemplate.toJson() is what must be deployed into the infographic docker container
        SvgTemplate svgTemplate1 = SvgTemplate.fromJson(frustrationOnTeamworking.toJson());
        Assert.assertEquals(SvgGenerator.generate(svgTemplate1), SvgGenerator.generate(frustrationOnTeamworking));
    }


    @AfterClass
    public void removeFolder() {
        Assert.assertTrue(deleteDirectory(new File(OUTPUT_FOLDER)));
    }
}
