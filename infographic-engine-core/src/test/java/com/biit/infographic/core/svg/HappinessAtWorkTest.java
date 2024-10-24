package com.biit.infographic.core.svg;

import com.biit.drools.form.DroolsSubmittedForm;
import com.biit.infographic.core.controllers.DroolsResultController;
import com.biit.infographic.core.engine.content.AppointmentContent;
import com.biit.infographic.core.generators.SvgGenerator;
import com.biit.infographic.core.models.svg.SvgAreaElement;
import com.biit.infographic.core.models.svg.SvgBackground;
import com.biit.infographic.core.models.svg.SvgTemplate;
import com.biit.infographic.core.models.svg.components.SvgCircle;
import com.biit.infographic.core.models.svg.components.SvgCircleSector;
import com.biit.infographic.core.models.svg.components.text.FontFactory;
import com.biit.infographic.core.models.svg.components.text.FontWeight;
import com.biit.infographic.core.models.svg.components.text.SvgText;
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
@Test(groups = "happinessAtWork")
public class HappinessAtWorkTest extends AbstractTestNGSpringContextTests {

    protected static final String OUTPUT_FOLDER = System.getProperty("java.io.tmpdir") + File.separator + "SvgTests";

    private static final String DROOLS_FORM_FILE_PATH = "drools/happinessAtWork.json";
    protected static final String TEMPLATE_ID = "happiness_at_work";

    private static final String TEMPLATE_NAME = "Happiness at Work";
    private static final String TEMPLATE_BACKGROUND_COLOR = "ffffffff";

    private static final String FONT = "Montserrat";
    private static final int TITLE_FONT_SIZE = 16;
    private static final int SUBTITLE_FONT_SIZE = 13;
    private static final int PARAGRAPH_FONT_SIZE = 12;
    private static final int PARAGRAPH_WIDTH = 475;

    private static final int CIRCLE_RADIUS = 24;
    private static final String CIRCLE_COLOR = "f20d5eff";
    private static final String CIRCLE_BACKGROUND = "edededff";

    private static final String ENGAGEMENT_CIRCLE = "#DROOLS%engagement%Percentage#";
    private static final String JOB_SATISFACTION_CIRCLE = "#DROOLS%satisfaction%Percentage#";
    private static final String ORGANISATIONAL_AFFECTION_CIRCLE = "#DROOLS%organisational_affection%Percentage#";


    @Autowired
    private DroolsResultController droolsResultController;

    @Autowired
    private AppointmentContent appointmentContent;

    private SvgTemplate happinessAtWorkTemplate;

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

        final SvgText title1 = new SvgText(FONT, "This is the result of the quick scan", TITLE_FONT_SIZE, 74, 73);
        elements.add(title1);

        final SvgText title2 = new SvgText(FONT, "“Happiness at Work”", TITLE_FONT_SIZE, 350, 73);
        title2.setFontWeight(FontWeight.BOLD);
        elements.add(title2);

        final SvgText subTitle = new SvgText(FONT, "Thank you for filling in", SUBTITLE_FONT_SIZE, 226, 103);
        elements.add(subTitle);

        final SvgText paragraph = new SvgText(FONT, "If you want to know more, please contact the BIIT representatives or our partners: Living Story and New Human Management. You can use the flyer to find out who can best unlock the knowledge you are looking for.\n"
                + "Please ask Henny how BIIT Labstation can create data visualizations like heatmaps to show overall results.", PARAGRAPH_FONT_SIZE, 62, 132);
        paragraph.setMaxLineWidth(PARAGRAPH_WIDTH);
        elements.add(paragraph);

        return elements;
    }

    private List<SvgAreaElement> generateEngagement() {
        final List<SvgAreaElement> elements = new ArrayList<>();

        final SvgText title = new SvgText(FONT, "Engagement", TITLE_FONT_SIZE, 245, 231);
        title.setFontWeight(FontWeight.BOLD);
        elements.add(title);

        final SvgCircle circleBackground = new SvgCircle(275, 255, CIRCLE_RADIUS, CIRCLE_BACKGROUND);
        elements.add(circleBackground);

        final SvgCircleSector scoreCircle = new SvgCircleSector(275, 255, CIRCLE_RADIUS);
        scoreCircle.setPercentage(ENGAGEMENT_CIRCLE);
        scoreCircle.getElementAttributes().setFill(CIRCLE_COLOR);
        scoreCircle.getElementStroke().setStrokeWidth(0);
        elements.add(scoreCircle);

        final SvgText paragraph = new SvgText(FONT, "Engagement is a special feeling of energy and motivation related to the ability to feel excited, vibrant, enthusiastic or passionate about work.\nIf you want to know how to get this feeling in your organisation by using innovation and game based learning talk to Karen (Living Story).", PARAGRAPH_FONT_SIZE, 62, 313);
        paragraph.setMaxLineWidth(PARAGRAPH_WIDTH);
        elements.add(paragraph);

        return elements;
    }

    private List<SvgAreaElement> generateJobSatisfaction() {
        final List<SvgAreaElement> elements = new ArrayList<>();

        final SvgText title = new SvgText(FONT, "Job Satisfaction", TITLE_FONT_SIZE, 232, 425);
        title.setFontWeight(FontWeight.BOLD);
        elements.add(title);

        final SvgCircle circleBackground = new SvgCircle(275, 448, CIRCLE_RADIUS, CIRCLE_BACKGROUND);
        elements.add(circleBackground);

        final SvgCircleSector scoreCircle = new SvgCircleSector(275, 448, CIRCLE_RADIUS);
        scoreCircle.setPercentage(JOB_SATISFACTION_CIRCLE);
        scoreCircle.getElementAttributes().setFill(CIRCLE_COLOR);
        scoreCircle.getElementStroke().setStrokeWidth(0);
        elements.add(scoreCircle);

        final SvgText paragraph = new SvgText(FONT, "Having the right natural orientation and skills is key to performing with appropriateness, adequacy, acceptability, suitability or job satisfaction. Laurens, Ellen and Anieke (New Human Management) can inform you about how to use the Competencies Analysis and Development Tool (CADT) for defining job profiles and assessment.", PARAGRAPH_FONT_SIZE, 62, 507);
        paragraph.setMaxLineWidth(PARAGRAPH_WIDTH);
        elements.add(paragraph);

        return elements;
    }


    private List<SvgAreaElement> generateOrganisationalAffection() {
        final List<SvgAreaElement> elements = new ArrayList<>();

        final SvgText title = new SvgText(FONT, "Organisational affection", TITLE_FONT_SIZE, 197, 620);
        title.setFontWeight(FontWeight.BOLD);
        elements.add(title);

        final SvgCircle circleBackground = new SvgCircle(275, 643, CIRCLE_RADIUS, CIRCLE_BACKGROUND);
        elements.add(circleBackground);

        final SvgCircleSector scoreCircle = new SvgCircleSector(275, 643, CIRCLE_RADIUS);
        scoreCircle.setPercentage(ORGANISATIONAL_AFFECTION_CIRCLE);
        scoreCircle.getElementAttributes().setFill(CIRCLE_COLOR);
        scoreCircle.getElementStroke().setStrokeWidth(0);
        elements.add(scoreCircle);

        final SvgText paragraph = new SvgText(FONT, "To analyse the connection between employees and organisation, NHM has developed the Nature Culture Analysis (NCA) assessment, which compares the natural and cultural behaviours of employees and organisation. If you want to know how to improve this connection to increase job performance, ask Laurens, Ellen and Anieke (New Human Management).", PARAGRAPH_FONT_SIZE, 62, 703);
        paragraph.setMaxLineWidth(PARAGRAPH_WIDTH);
        elements.add(paragraph);

        return elements;
    }


    @BeforeClass
    public void prepareFolder() throws IOException {
        Files.createDirectories(Paths.get(OUTPUT_FOLDER));
    }

    @Test
    public void generateHappinessAtWork() {
        happinessAtWorkTemplate = new SvgTemplate();
        happinessAtWorkTemplate.setUuid(TEMPLATE_ID);
        happinessAtWorkTemplate.getElementAttributes().setHeight(842);
        happinessAtWorkTemplate.getElementAttributes().setWidth(596);
        happinessAtWorkTemplate.setSvgBackground(generateBackground());

        happinessAtWorkTemplate.addElements(generateTitle());
        happinessAtWorkTemplate.addElements(generateEngagement());
        happinessAtWorkTemplate.addElements(generateJobSatisfaction());
        happinessAtWorkTemplate.addElements(generateOrganisationalAffection());
    }

    @Test(dependsOnMethods = "generateHappinessAtWork")
    public void executeHappinessAtWork() throws IOException {
        FontFactory.resetFonts();
        final DroolsSubmittedForm droolsSubmittedForm = DroolsSubmittedForm.getFromJson(FileReader.getResource(DROOLS_FORM_FILE_PATH, StandardCharsets.UTF_8));
        final List<String> svgResults = droolsResultController.execute(droolsSubmittedForm, Collections.singletonList(happinessAtWorkTemplate));
        Assert.assertEquals(svgResults.size(), 1);

        try (PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(OUTPUT_FOLDER
                + File.separator + TEMPLATE_NAME + ".svg")), true)) {
            out.println(svgResults.get(0));
        }

        checkContent(svgResults.get(0), TEMPLATE_NAME + ".svg");
    }

    @Test(dependsOnMethods = "executeHappinessAtWork")
    public void checkSerialization() throws JsonProcessingException {
        //boardingPassTemplate.toJson() is what must be deployed into the infographic docker container
        SvgTemplate svgTemplate1 = SvgTemplate.fromJson(happinessAtWorkTemplate.toJson());
        Assert.assertEquals(SvgGenerator.generate(svgTemplate1), SvgGenerator.generate(happinessAtWorkTemplate));
    }


    @AfterClass
    public void removeFolder() {
        Assert.assertTrue(deleteDirectory(new File(OUTPUT_FOLDER)));
    }
}
