package com.biit.infographic.core.svg;

import com.biit.appointment.rest.client.TestAppointmentCenterClient;
import com.biit.drools.form.DroolsSubmittedForm;
import com.biit.infographic.core.engine.SvgFromDroolsConverter;
import com.biit.infographic.core.engine.content.AppointmentContent;
import com.biit.infographic.core.engine.content.value.ValueCalculator;
import com.biit.infographic.core.generators.SvgGenerator;
import com.biit.infographic.core.models.svg.SvgAreaElement;
import com.biit.infographic.core.models.svg.SvgBackground;
import com.biit.infographic.core.models.svg.SvgEmbedded;
import com.biit.infographic.core.models.svg.SvgTemplate;
import com.biit.infographic.core.models.svg.components.SvgCircle;
import com.biit.infographic.core.models.svg.components.SvgPath;
import com.biit.infographic.core.models.svg.components.SvgRectangle;
import com.biit.infographic.core.models.svg.components.path.BezierCurve;
import com.biit.infographic.core.models.svg.components.path.HorizontalLine;
import com.biit.infographic.core.models.svg.components.path.VerticalLine;
import com.biit.infographic.core.models.svg.components.text.FontFactory;
import com.biit.infographic.core.models.svg.components.text.FontWeight;
import com.biit.infographic.core.models.svg.components.text.SvgText;
import com.biit.infographic.core.providers.UserProvider;
import com.biit.usermanager.client.providers.AuthenticatedUserProvider;
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
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@SpringBootTest
@Test(groups = "boardingPass")
public class BoardingPassSuccessTogetherFromDroolsTest extends AbstractTestNGSpringContextTests {

    protected static final String OUTPUT_FOLDER = System.getProperty("java.io.tmpdir") + File.separator + "SvgTests";

    private static final String DROOLS_FORM_FILE_PATH = "drools/boardingPassSuccessTogetherTemplate.json";
    protected static final String TEMPLATE_ID = "default_test";

    //The one that has submitted the drools json.
    private final static String USER_NAME = "Chuck Norris";

    private static final String TEMPLATE_NAME = "Success Together";
    private static final String TEMPLATE_BACKGROUND_COLOR = "f0eeed";

    private static final String TEMPLATE_ENABLED_COLOR = "b49057";
    private static final String TEMPLATE_DISABLED_COLOR = "d3d4d4";
    private static final String TEMPLATE_ENABLED_COLOR_HOVER = "d4c1a1";

    private static final String BUTTON_TEXT_DISABLED_COLOR = "b9b9b9";
    private static final String BUTTON_TEXT_ENABLED_COLOR = "000000";

    private static final String ICON = "images/CADT/Society.svg";
    private static final int ICON_SIZE = 16;

    private static final String TITLE_FONT_FAMILY = "Sofia Sans Extra Condensed";
    private static final int TITLE_FONT_SIZE = 20;
    private static final int BUTTON_FONT_SIZE = 20;
    private static final int SUBTITLE_FONT_SIZE = 16;
    private static final String TEXT_FONT_FAMILY = "Arial";
    private static final int TEXT_FONT_SIZE = 12;
    private static final int TEXT_LINE_SEPARATOR = 2;


    private static final int PATH_WIDTH = 4;
    private static final String FIRST_PATH_COLOR = "#APPOINTMENT%TEMPLATE%" + TEMPLATE_NAME + ValueCalculator.ATTRIBUTE_FIELDS_SEPARATION + "DURATION_TIME*0.25" + ValueCalculator.CONDITION_SEPARATION + TEMPLATE_ENABLED_COLOR + ValueCalculator.VALUE_SEPARATION + TEMPLATE_DISABLED_COLOR + "#";
    private static final String SECOND_PATH_COLOR = "#APPOINTMENT%TEMPLATE%" + TEMPLATE_NAME + ValueCalculator.ATTRIBUTE_FIELDS_SEPARATION + "DURATION_TIME*0.5" + ValueCalculator.CONDITION_SEPARATION + TEMPLATE_ENABLED_COLOR + ValueCalculator.VALUE_SEPARATION + TEMPLATE_DISABLED_COLOR + "#";
    private static final String THIRD_PATH_COLOR = "#APPOINTMENT%TEMPLATE%" + TEMPLATE_NAME + ValueCalculator.ATTRIBUTE_FIELDS_SEPARATION + "DURATION_TIME*0.75" + ValueCalculator.CONDITION_SEPARATION + TEMPLATE_ENABLED_COLOR + ValueCalculator.VALUE_SEPARATION + TEMPLATE_DISABLED_COLOR + "#";
    private static final String FORTH_PATH_COLOR = "#APPOINTMENT%TEMPLATE%" + TEMPLATE_NAME + ValueCalculator.ATTRIBUTE_FIELDS_SEPARATION + "DURATION_TIME*0.9" + ValueCalculator.CONDITION_SEPARATION + TEMPLATE_ENABLED_COLOR + ValueCalculator.VALUE_SEPARATION + TEMPLATE_DISABLED_COLOR + "#";

    private static final String FIRST_CIRCLE_COLOR = "#APPOINTMENT%TEMPLATE%" + TEMPLATE_NAME + ValueCalculator.ATTRIBUTE_FIELDS_SEPARATION + "DURATION_TIME*0" + ValueCalculator.CONDITION_SEPARATION + TEMPLATE_ENABLED_COLOR + ValueCalculator.VALUE_SEPARATION + TEMPLATE_DISABLED_COLOR + "#";
    private static final String SECOND_CIRCLE_COLOR = "#APPOINTMENT%TEMPLATE%" + TEMPLATE_NAME + ValueCalculator.ATTRIBUTE_FIELDS_SEPARATION + "DURATION_TIME*0.30" + ValueCalculator.CONDITION_SEPARATION + TEMPLATE_ENABLED_COLOR + ValueCalculator.VALUE_SEPARATION + TEMPLATE_DISABLED_COLOR + "#";
    private static final String THIRD_CIRCLE_COLOR = "#APPOINTMENT%TEMPLATE%" + TEMPLATE_NAME + ValueCalculator.ATTRIBUTE_FIELDS_SEPARATION + "DURATION_TIME*0.60" + ValueCalculator.CONDITION_SEPARATION + TEMPLATE_ENABLED_COLOR + ValueCalculator.VALUE_SEPARATION + TEMPLATE_DISABLED_COLOR + "#";
    private static final String FORTH_CIRCLE_COLOR = "#APPOINTMENT%TEMPLATE%" + TEMPLATE_NAME + ValueCalculator.ATTRIBUTE_FIELDS_SEPARATION + "DURATION_TIME*1" + ValueCalculator.CONDITION_SEPARATION + TEMPLATE_ENABLED_COLOR + ValueCalculator.VALUE_SEPARATION + TEMPLATE_DISABLED_COLOR + "#";

    private static final String SUBMIT_BUTTON_COLOR = "#APPOINTMENT%TEMPLATE%" + TEMPLATE_NAME + ValueCalculator.ATTRIBUTE_FIELDS_SEPARATION + "DURATION_TIME*0.9" + ValueCalculator.CONDITION_SEPARATION + TEMPLATE_ENABLED_COLOR + ValueCalculator.VALUE_SEPARATION + TEMPLATE_DISABLED_COLOR + "#";
    private static final String SUBMIT_BUTTON_HOVER_OPACITY = "#APPOINTMENT%TEMPLATE%" + TEMPLATE_NAME + ValueCalculator.ATTRIBUTE_FIELDS_SEPARATION + "DURATION_TIME*0.9" + ValueCalculator.CONDITION_SEPARATION + TEMPLATE_ENABLED_COLOR + ValueCalculator.VALUE_SEPARATION + TEMPLATE_ENABLED_COLOR_HOVER + "#";
    private static final String SUBMIT_BUTTON_TEXT_COLOR = "#APPOINTMENT%TEMPLATE%" + TEMPLATE_NAME + ValueCalculator.ATTRIBUTE_FIELDS_SEPARATION + "DURATION_TIME*0.9" + ValueCalculator.CONDITION_SEPARATION + BUTTON_TEXT_ENABLED_COLOR + ValueCalculator.VALUE_SEPARATION + BUTTON_TEXT_DISABLED_COLOR + "#";

    private static final String STARTING_TIME = "#APPOINTMENT%TEMPLATE%" + TEMPLATE_NAME + ValueCalculator.ATTRIBUTE_FIELDS_SEPARATION + "STARTING_TIME_HOUR#";
    private static final String ENDING_TIME = "#APPOINTMENT%TEMPLATE%" + TEMPLATE_NAME + ValueCalculator.ATTRIBUTE_FIELDS_SEPARATION + "ENDING_TIME_HOUR#";

    private static final String BAD_URL = "";
    private static final String GOOD_URL = "https://google.com";
    private static final String BUTTON_URL = "#APPOINTMENT%TEMPLATE%" + TEMPLATE_NAME + ValueCalculator.ATTRIBUTE_FIELDS_SEPARATION + "DURATION_TIME*0.9" + ValueCalculator.CONDITION_SEPARATION + BAD_URL + ValueCalculator.VALUE_SEPARATION + GOOD_URL + "#";

    @Autowired
    private SvgFromDroolsConverter svgFromDroolsConverter;

    @Autowired
    private AuthenticatedUserProvider authenticatedUserProvider;

    @Autowired
    private UserProvider userProvider;

    @Autowired
    private TestAppointmentCenterClient testAppointmentCenterClient;

    @Autowired
    private AppointmentContent appointmentContent;

    private SvgTemplate boardingPassTemplate;

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

    private List<SvgAreaElement> generatePath() {
        final List<SvgAreaElement> elements = new ArrayList<>();

        final SvgPath path = new SvgPath(36, 84,
                new VerticalLine(93),
                new BezierCurve(68, 125, 36, 109, 50, 125),
                new HorizontalLine(292),
                new BezierCurve(324, 157, 310, 125, 324, 140),
                new VerticalLine(174));
        path.getElementStroke().setStrokeColor(FIRST_PATH_COLOR);
        path.getElementStroke().setStrokeWidth(PATH_WIDTH);
        elements.add(path);

        final SvgPath path2 = new SvgPath(324, 180,
                new VerticalLine(196),
                new BezierCurve(292, 228, 324, 214, 310, 228),
                new HorizontalLine(68),
                new BezierCurve(36, 260, 50, 228, 36, 242),
                new VerticalLine(277));
        path2.getElementStroke().setStrokeColor(SECOND_PATH_COLOR);
        path2.getElementStroke().setStrokeWidth(PATH_WIDTH);
        elements.add(path2);

        final SvgPath path3 = new SvgPath(36, 280,
                new VerticalLine(318),
                new BezierCurve(68, 350, 36, 336, 52, 350),
                new HorizontalLine(292),
                new BezierCurve(324, 382, 312, 350, 324, 365),
                new VerticalLine(399));
        path3.getElementStroke().setStrokeColor(THIRD_PATH_COLOR);
        path3.getElementStroke().setStrokeWidth(PATH_WIDTH);
        elements.add(path3);

        return elements;
    }

    private List<SvgAreaElement> generateCircles() {
        final List<SvgAreaElement> elements = new ArrayList<>();

        final SvgCircle svgCircle1 = new SvgCircle(23, 72, 13);
        svgCircle1.getElementAttributes().setFill(FIRST_CIRCLE_COLOR);
        elements.add(svgCircle1);

        final SvgCircle svgCircle2 = new SvgCircle(312, 164, 13);
        svgCircle2.getElementAttributes().setFill(SECOND_CIRCLE_COLOR);
        elements.add(svgCircle2);

        final SvgCircle svgCircle3 = new SvgCircle(23, 267, 13);
        svgCircle3.getElementAttributes().setFill(THIRD_CIRCLE_COLOR);
        elements.add(svgCircle3);

        final SvgCircle svgCircle4 = new SvgCircle(312, 389, 13);
        svgCircle4.getElementAttributes().setFill(FORTH_CIRCLE_COLOR);
        elements.add(svgCircle4);


        return elements;
    }

    private List<SvgAreaElement> generateIcons() {
        final List<SvgAreaElement> elements = new ArrayList<>();

        final SvgEmbedded circleIcon1 = new SvgEmbedded(ICON, 317, 169);
        circleIcon1.getElementAttributes().setWidth(ICON_SIZE);
        circleIcon1.getElementAttributes().setHeight(ICON_SIZE);
        elements.add(circleIcon1);

        final SvgEmbedded circleIcon2 = new SvgEmbedded(ICON, 28, 272);
        circleIcon2.getElementAttributes().setWidth(ICON_SIZE);
        circleIcon2.getElementAttributes().setHeight(ICON_SIZE);
        elements.add(circleIcon2);

        return elements;
    }

    private List<SvgAreaElement> generateStaticTexts() {
        final List<SvgAreaElement> elements = new ArrayList<>();

        final SvgText title = new SvgText(TITLE_FONT_FAMILY, "SAMEN SUCCES VIEREN", TITLE_FONT_SIZE, 24, 29);
        title.setFontWeight(FontWeight.BOLD);
        elements.add(title);

        final SvgText checkIn = new SvgText(TITLE_FONT_FAMILY, "CHECK IN", SUBTITLE_FONT_SIZE, 58, 71);
        checkIn.setFontWeight(FontWeight.BOLD);
        elements.add(checkIn);

        final SvgText paragraph1 = new SvgText(TEXT_FONT_FAMILY, "Een teambuildingactiviteit gericht op het vieren van collectieve successen en het versterken van de teamgeest.",
                TEXT_FONT_SIZE, 64, 155);
        paragraph1.setMaxLineWidth(228);
        paragraph1.setSamePhraseLineSeparator(TEXT_LINE_SEPARATOR);
        elements.add(paragraph1);

        final SvgText paragraph2 = new SvgText(TEXT_FONT_FAMILY, "Reflectie op de gezamenlijke prestaties en het bespreken van lessen die kunnen worden toegepast in de dagelijkse werksituaties.",
                TEXT_FONT_SIZE, 64, 251);
        paragraph2.setMaxLineWidth(228);
        paragraph2.setSamePhraseLineSeparator(TEXT_LINE_SEPARATOR);
        elements.add(paragraph2);

        final SvgText checkOut = new SvgText(TITLE_FONT_FAMILY, "CHECK OUT", SUBTITLE_FONT_SIZE, 246, 388);
        checkOut.setFontWeight(FontWeight.BOLD);
        elements.add(checkOut);

        return elements;
    }

    private List<SvgAreaElement> generateSubmitButton() {
        final List<SvgAreaElement> elements = new ArrayList<>();

        final SvgRectangle button = new SvgRectangle(107, 331, 146, 38, SUBMIT_BUTTON_COLOR);
        button.setXRadius(9);
        button.setYRadius(9);
        button.setLink(BUTTON_URL);
        button.getLink().getFillAttributes().setHoverFillColor(SUBMIT_BUTTON_HOVER_OPACITY);
        elements.add(button);

        final SvgText buttonLabel = new SvgText(TITLE_FONT_FAMILY, "FINISH STEP", BUTTON_FONT_SIZE, 144, 343);
        buttonLabel.getElementAttributes().setFill(SUBMIT_BUTTON_TEXT_COLOR);
        buttonLabel.setFontWeight(FontWeight.BOLD);
        buttonLabel.setLink(BUTTON_URL);
        elements.add(buttonLabel);

        return elements;
    }

    private List<SvgAreaElement> generateTimeTexts() {
        final List<SvgAreaElement> elements = new ArrayList<>();

        final SvgText checkInTime = new SvgText(TEXT_FONT_FAMILY, STARTING_TIME, TEXT_FONT_SIZE, 58, 91);
        elements.add(checkInTime);

        final SvgText checkInTimeUnit = new SvgText(TEXT_FONT_FAMILY, "h", TEXT_FONT_SIZE, 90, 91);
        elements.add(checkInTimeUnit);

        final SvgText checkOutTime = new SvgText(TEXT_FONT_FAMILY, ENDING_TIME, TEXT_FONT_SIZE, 260, 407);
        elements.add(checkOutTime);

        final SvgText checkOutTimeUnit = new SvgText(TEXT_FONT_FAMILY, "h", TEXT_FONT_SIZE, 292, 407);
        elements.add(checkOutTimeUnit);

        return elements;
    }

    @BeforeClass
    public void createUser() throws IOException {
        authenticatedUserProvider.createUser(USER_NAME, USER_NAME, "123456");
    }

    @BeforeClass
    public void prepareFolder() throws IOException {
        Files.createDirectories(Paths.get(OUTPUT_FOLDER));
    }

    @BeforeClass
    public void defineAppointment() {
        ZonedDateTime time = ZonedDateTime.of(LocalDate.of(2024, 5, 16), LocalTime.of(16, 19, 32), ZoneId.of("Europe/Madrid"));

        appointmentContent.setDateToCheck(time);
        testAppointmentCenterClient.setStartTime(time.minusMinutes(110).toLocalDateTime());
        testAppointmentCenterClient.setEndTime(time.plusMinutes(10).toLocalDateTime());
    }

    @Test
    public void generateBoardingPass() {
        boardingPassTemplate = new SvgTemplate();
        boardingPassTemplate.setUuid(TEMPLATE_ID);
        boardingPassTemplate.setId(TEMPLATE_ID);
        boardingPassTemplate.getElementAttributes().setHeight(444);
        boardingPassTemplate.getElementAttributes().setWidth(360);
        boardingPassTemplate.setSvgBackground(generateBackground());

        boardingPassTemplate.addElements(generatePath());
        boardingPassTemplate.addElements(generateCircles());
        boardingPassTemplate.addElements(generateIcons());
        boardingPassTemplate.addElements(generateStaticTexts());
        boardingPassTemplate.addElements(generateSubmitButton());
        boardingPassTemplate.addElements(generateTimeTexts());
    }

    @Test(dependsOnMethods = "generateBoardingPass")
    public void executeBoardingPass() throws IOException {
        FontFactory.resetFonts();
        final DroolsSubmittedForm droolsSubmittedForm = DroolsSubmittedForm.getFromJson(FileReader.getResource(DROOLS_FORM_FILE_PATH, StandardCharsets.UTF_8));
        final List<String> svgResults = svgFromDroolsConverter.execute(droolsSubmittedForm, Collections.singletonList(boardingPassTemplate));
        Assert.assertEquals(svgResults.size(), 1);

        try (PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(OUTPUT_FOLDER
                + File.separator + TEMPLATE_NAME + ".svg")), true)) {
            out.println(svgResults.get(0));
        }

        checkContent(svgResults.get(0), TEMPLATE_NAME + ".svg");
    }

    @Test(dependsOnMethods = "generateBoardingPass")
    public void checkSerialization() throws JsonProcessingException {
        //boardingPassTemplate.toJson() is what must be deployed into the infographic docker container
        SvgTemplate svgTemplate1 = SvgTemplate.fromJson(boardingPassTemplate.toJson());
        Assert.assertEquals(SvgGenerator.generate(svgTemplate1), SvgGenerator.generate(boardingPassTemplate));
    }


    @AfterClass(alwaysRun = true)
    public void removeFolder() {
        Assert.assertTrue(deleteDirectory(new File(OUTPUT_FOLDER)));
    }

    @AfterClass(alwaysRun = true)
    public void removeUser() {
        authenticatedUserProvider.clear();
        userProvider.reset();
    }
}
