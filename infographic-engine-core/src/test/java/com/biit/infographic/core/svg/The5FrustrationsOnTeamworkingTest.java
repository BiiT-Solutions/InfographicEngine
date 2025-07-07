package com.biit.infographic.core.svg;

import com.biit.drools.form.DroolsSubmittedForm;
import com.biit.infographic.core.engine.SvgFromDroolsConverter;
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
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

@SpringBootTest
@Test(groups = "frustrationOnTeamworking")
public class The5FrustrationsOnTeamworkingTest extends AbstractTestNGSpringContextTests {

    protected static final String OUTPUT_FOLDER = System.getProperty("java.io.tmpdir") + File.separator + "SvgTests";

    private static final String DROOLS_FORM_FILE_PATH = "drools/The 5 Frustrations on Teamworking.json";
    protected static final String TEMPLATE_ID = "five_frustrations";

    private static final String TEMPLATE_NAME = "The 5 Frustrations on Teamworking";
    private static final String TEMPLATE_BACKGROUND_COLOR = "ffffffff";

    private static final String FONT = "Montserrat";
    private static final int TITLE_FONT_SIZE = 20;
    private static final int SUBTITLE_FONT_SIZE = 16;
    private static final int PARAGRAPH_FONT_SIZE = 12;

    private static final int CIRCLE_RADIUS = 30;
    private static final String CIRCLE_COLOR = "f20d5eff";
    private static final String CIRCLE_BACKGROUND = "edededff";
    private static final int CIRCLE_TITLE = 16;

    private static final String USER_NAME = "#USER%SUBMITTER%NAME#";
    private static final String USER_LASTNAME = "#USER%SUBMITTER%LASTNAME#";

    private static final String SUBMIT_TIME = "#FORM%SUBMIT%TIME#";
    private static final String SUBMIT_DATE = "#FORM%SUBMIT%DATE#";

    private static final String FRUSTRATION_1_CIRCLE = "#DROOLS%The 5 Frustrations on Teamworking%Frustration1#";
    private static final String FRUSTRATION_2_CIRCLE = "#DROOLS%The 5 Frustrations on Teamworking%Frustration2#";
    private static final String FRUSTRATION_3_CIRCLE = "#DROOLS%The 5 Frustrations on Teamworking%Frustration3#";
    private static final String FRUSTRATION_4_CIRCLE = "#DROOLS%The 5 Frustrations on Teamworking%Frustration4#";
    private static final String FRUSTRATION_5_CIRCLE = "#DROOLS%The 5 Frustrations on Teamworking%Frustration5#";

    private static final String FRUSTRATION_1_VALUE = "#DROOLS%The 5 Frustrations on Teamworking%Frustration1Total#";
    private static final String FRUSTRATION_2_VALUE = "#DROOLS%The 5 Frustrations on Teamworking%Frustration2Total#";
    private static final String FRUSTRATION_3_VALUE = "#DROOLS%The 5 Frustrations on Teamworking%Frustration3Total#";
    private static final String FRUSTRATION_4_VALUE = "#DROOLS%The 5 Frustrations on Teamworking%Frustration4Total#";
    private static final String FRUSTRATION_5_VALUE = "#DROOLS%The 5 Frustrations on Teamworking%Frustration5Total#";

    public static final String TITLE_DUTCH_TEXT = "De 5 frustraties van teamwork";
    public static final String TITLE_ENGLISH_TEXT = "The 5 frustrations of teamwork";

    public static final String SUBTITLE_DUTCH_TEXT = "Hoe zorg je dat samenwerken leuk blijft?";
    public static final String SUBTITLE_ENGLISH_TEXT = "How do you make sure that working together stays fun?";

    public static final String INTRODUCTION_DUTCH_TEXT = "Je hebt de 15 stellingen beantwoord die horen bij de 5 frustraties in teamwork (volgens Patrick Lencioni). Op basis van jouw antwoorden komt daar per frustratie een cijfer uit op een schaal van 0 tot 9. Dat zie je hieronder. Daarnaast zie je een beschrijving per frustratie. Op die manier kun je jouw beoordeling koppelen aan een cijfer. Met andere woorden een hoog cijfer als het eigenlijk geen frustratie is (we doen het goed) en een laag cijfer als jij vindt dat er nog ruimte voor verbetering is.";
    public static final String INTRODUCTION_ENGLISH_TEXT = "You have answered the 15 statements belonging to the 5 frustrations in teamwork (according to Patrick Lencioni). Based on your answers, each frustration is rated on a scale from 0 to 9. You can see this below. In addition, you will see a description per frustration. This way, you can link your rating to a number. In other words, a high number if it is not really a frustration (we do it well) and a low number if you think there is room for improvement.";

    public static final String FRUSTRATION_1_DUTCH_TITLE = "Gebrek aan vertrouwen";
    public static final String FRUSTRATION_1_ENGLISH_TITLE = "Lack of Trust";

    public static final String FRUSTRATION_1_DUTCH_TEXT = "Teams zonder vertrouwen verbergen zwakheden en fouten, en zijn terughoudend in het geven van feedback of vragen om hulp. Dit belemmert samenwerking omdat leden niet zeker zijn van elkaars goede bedoelingen, wat leidt tot een gebrek aan psychologische veiligheid. Openheid en kwetsbaarheid zijn essentieel om dit te overwinnen.";
    public static final String FRUSTRATION_1_ENGLISH_TEXT = "Teams without trust hide weaknesses and mistakes, and are reluctant to give feedback or ask for help. This hinders collaboration because members are unsure of each other's good intentions, leading to a lack of psychological safety. Openness and vulnerability are essential to overcome this.";

    public static final String FRUSTRATION_2_DUTCH_TITLE = "Angst voor conflicten";
    public static final String FRUSTRATION_2_ENGLISH_TITLE = "Fear of conflicts";

    public static final String FRUSTRATION_2_DUTCH_TEXT = "Als teamleden conflicten vermijden, ontstaan saaie vergaderingen en blijven belangrijke onderwerpen onbesproken. Productieve en opbouwende conflicten zijn cruciaal voor groei en het oplossen van problemen. Vertrouwen in elkaar is de basis om deze conflicten aan te durven.";
    public static final String FRUSTRATION_2_ENGLISH_TEXT = "When team members avoid conflict, boring meetings arise and important issues remain undiscussed. Productive and constructive conflicts are crucial for growth and problem solving. Trust in each other is the basis for daring to take on these conflicts.";

    public static final String FRUSTRATION_3_DUTCH_TITLE = "Gebrek aan betrokkenheid";
    public static final String FRUSTRATION_3_ENGLISH_TITLE = "Lack of engagement";

    public static final String FRUSTRATION_3_DUTCH_TEXT = "Onduidelijkheid over doelen en prioriteiten leidt tot een gebrek aan betrokkenheid bij beslissingen. Teams die niet als eenheid optreden, blijven vaak hangen in discussies en missen daadkracht. Actieve deelname en het delen van ideeën versterken de toewijding, zelfs als er niet altijd consensus is.";
    public static final String FRUSTRATION_3_ENGLISH_TEXT = "Lack of clarity on goals and priorities leads to a lack of commitment to decisions. Teams that do not act as a unit often get stuck in discussions and lack decisiveness. Active participation and the sharing of ideas strengthen commitment, even if there is not always consensus.";

    public static final String FRUSTRATION_4_DUTCH_TITLE = "Afschuiven van verantwoordelijkheid";
    public static final String FRUSTRATION_4_ENGLISH_TITLE = "Shifting responsibility";

    public static final String FRUSTRATION_4_DUTCH_TEXT = "Wanneer teamleden elkaar niet aanspreken op hun gedrag of prestaties, wordt middelmatigheid geaccepteerd en worden deadlines gemist. Dit ontstaat vaak uit angst voor ongemakkelijke confrontaties, maar het vermijden hiervan kan relaties juist schaden. Gezonde teams spreken elkaar aan om de kwaliteit hoog te houden.";
    public static final String FRUSTRATION_4_ENGLISH_TEXT = "When team members do not hold each other accountable for their behaviour or performance, mediocrity is accepted and deadlines are missed. This often arises out of fear of uncomfortable confrontations, but avoiding this can actually damage relationships. Healthy teams address each other to keep quality high.";

    public static final String FRUSTRATION_5_DUTCH_TITLE = "Weinig aandacht voor resultaten";
    public static final String FRUSTRATION_5_ENGLISH_TITLE = "Little focus on results";

    public static final String FRUSTRATION_5_DUTCH_TEXT = "Teams die niet gefocust zijn op gezamenlijke resultaten laten zich afleiden door individuele of teamstatus. Dit leidt tot stilstand en het verlies van prestatiegerichte medewerkers. Het samen vaststellen van doelen en resultaten kan helpen om scherpt te blijven en de samenwerking te versterken.";
    public static final String FRUSTRATION_5_ENGLISH_TEXT = "Teams that are not focused on joint results get distracted by individual or team status. This leads to stagnation and the loss of performance-oriented employees. Setting goals and results together can help stay sharp and strengthen cooperation.";

    private static final String ENGLISH_TEXT_COLOR = "000000";
    private static final int DOCUMENT_HEIGHT = 1600;
    private static final int DOCUMENT_WIDTH = 800;
    private static final int MAIN_MARGIN = 55;
    private static final int SECOND_MARGIN = 25;
    private static final int COLUMN_WIDTH = DOCUMENT_WIDTH / 2 - MAIN_MARGIN - SECOND_MARGIN;

    @Autowired
    private SvgFromDroolsConverter svgFromDroolsConverter;

    @Autowired
    private AuthenticatedUserProvider authenticatedUserProvider;

    @Autowired
    private UserProvider userProvider;

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

    @BeforeClass
    public void createUser() throws IOException {
        authenticatedUserProvider.createUser("admin@test.com", UUID.randomUUID().toString(), "Angus", "MacGyver", "123456", null);
    }

    private SvgBackground generateBackground() {
        final SvgBackground svgBackground = new SvgBackground();
        svgBackground.setBackgroundColor(TEMPLATE_BACKGROUND_COLOR);
        return svgBackground;
    }


    private List<SvgAreaElement> generateTitleNL(int margin) {
        final List<SvgAreaElement> elements = new ArrayList<>();

        final SvgText titleNL = new SvgText(FONT, TITLE_DUTCH_TEXT, TITLE_FONT_SIZE, margin + 9, 73);
        titleNL.setFontWeight(FontWeight.BOLD);
        titleNL.setMaxLineWidth(COLUMN_WIDTH);
        elements.add(titleNL);


        final SvgText subtitleNL = new SvgText(FONT, SUBTITLE_DUTCH_TEXT, SUBTITLE_FONT_SIZE, margin + 20, 115);
        subtitleNL.setFontWeight(FontWeight.BOLD);
        subtitleNL.setMaxLineWidth(COLUMN_WIDTH);
        elements.add(subtitleNL);

        final SvgText introductionNL = new SvgText(FONT, INTRODUCTION_DUTCH_TEXT, PARAGRAPH_FONT_SIZE, margin, 160);
        introductionNL.setMaxLineWidth(COLUMN_WIDTH);
        introductionNL.setTextAlign(TextAlign.JUSTIFY);
        introductionNL.setMaxLineWidth(COLUMN_WIDTH);
        elements.add(introductionNL);

        return elements;
    }

    private List<SvgAreaElement> generateTitleEN(int margin) {
        final List<SvgAreaElement> elements = new ArrayList<>();

        final SvgText titleEN = new SvgText(FONT, TITLE_ENGLISH_TEXT, TITLE_FONT_SIZE, margin + 9, 73);
        titleEN.getElementAttributes().setFill(ENGLISH_TEXT_COLOR);
        titleEN.setFontWeight(FontWeight.BOLD);
        titleEN.setMaxLineWidth(COLUMN_WIDTH);
        elements.add(titleEN);

        final SvgText subtitleEN = new SvgText(FONT, SUBTITLE_ENGLISH_TEXT, SUBTITLE_FONT_SIZE, margin + 14, 115);
        subtitleEN.getElementAttributes().setFill(ENGLISH_TEXT_COLOR);
        subtitleEN.setFontWeight(FontWeight.BOLD);
        subtitleEN.setMaxLineWidth(COLUMN_WIDTH);
        elements.add(subtitleEN);

        final SvgText introductionEN = new SvgText(FONT, INTRODUCTION_ENGLISH_TEXT, PARAGRAPH_FONT_SIZE, margin, 160);
        introductionEN.setTextAlign(TextAlign.JUSTIFY);
        introductionEN.setMaxLineWidth(COLUMN_WIDTH);
        introductionEN.getElementAttributes().setFill(ENGLISH_TEXT_COLOR);
        elements.add(introductionEN);

        return elements;
    }

    private List<SvgAreaElement> generateFrustration1NL(int hight, int margin) {
        final List<SvgAreaElement> elements = new ArrayList<>();

        final SvgCircle circleBackground = new SvgCircle(margin + COLUMN_WIDTH / 2 - CIRCLE_RADIUS, hight, CIRCLE_RADIUS, CIRCLE_BACKGROUND);
        elements.add(circleBackground);

        final SvgCircleSector scoreCircle = new SvgCircleSector(margin + COLUMN_WIDTH / 2 - CIRCLE_RADIUS, hight, CIRCLE_RADIUS);
        scoreCircle.setPercentage(FRUSTRATION_1_CIRCLE);
        scoreCircle.getElementAttributes().setFill(CIRCLE_COLOR);
        scoreCircle.getElementStroke().setStrokeWidth(0);
        elements.add(scoreCircle);

        final SvgText value = new SvgText(FRUSTRATION_1_VALUE, 22, margin + COLUMN_WIDTH / 2, hight + 24);
        value.setTextAlign(TextAlign.CENTER);
        elements.add(value);

        final SvgText titleNL = new SvgText(FONT, FRUSTRATION_1_DUTCH_TITLE, CIRCLE_TITLE, margin + COLUMN_WIDTH / 2, hight + 70);
        titleNL.setFontWeight(FontWeight.BOLD);
        titleNL.setTextAlign(TextAlign.CENTER);
        elements.add(titleNL);

        final SvgText paragraphNL = new SvgText(FONT, FRUSTRATION_1_DUTCH_TEXT, PARAGRAPH_FONT_SIZE, margin, hight + 105);
        paragraphNL.setMaxLineWidth(COLUMN_WIDTH);
        paragraphNL.setTextAlign(TextAlign.JUSTIFY);
        elements.add(paragraphNL);

        return elements;
    }

    private List<SvgAreaElement> generateFrustration1EN(int hight, int margin) {
        final List<SvgAreaElement> elements = new ArrayList<>();

        final SvgCircle circleBackground = new SvgCircle(margin + COLUMN_WIDTH / 2 - CIRCLE_RADIUS, hight, CIRCLE_RADIUS, CIRCLE_BACKGROUND);
        elements.add(circleBackground);

        final SvgCircleSector scoreCircle = new SvgCircleSector(margin + COLUMN_WIDTH / 2 - CIRCLE_RADIUS, hight, CIRCLE_RADIUS);
        scoreCircle.setPercentage(FRUSTRATION_1_CIRCLE);
        scoreCircle.getElementAttributes().setFill(CIRCLE_COLOR);
        scoreCircle.getElementStroke().setStrokeWidth(0);
        elements.add(scoreCircle);

        final SvgText value = new SvgText(FRUSTRATION_1_VALUE, 22, margin + COLUMN_WIDTH / 2, hight + 24);
        value.setTextAlign(TextAlign.CENTER);
        elements.add(value);

        final SvgText titleEN = new SvgText(FONT, FRUSTRATION_1_ENGLISH_TITLE, CIRCLE_TITLE, margin + COLUMN_WIDTH / 2, hight + 70);
        titleEN.setFontWeight(FontWeight.BOLD);
        titleEN.getElementAttributes().setFill(ENGLISH_TEXT_COLOR);
        titleEN.setTextAlign(TextAlign.CENTER);
        elements.add(titleEN);

        final SvgText paragraphEN = new SvgText(FONT, FRUSTRATION_1_ENGLISH_TEXT, PARAGRAPH_FONT_SIZE, margin, hight + 105);
        paragraphEN.setMaxLineWidth(COLUMN_WIDTH);
        paragraphEN.setTextAlign(TextAlign.JUSTIFY);
        paragraphEN.getElementAttributes().setFill(ENGLISH_TEXT_COLOR);
        elements.add(paragraphEN);

        return elements;
    }

    private List<SvgAreaElement> generateFrustration2NL(int hight, int margin) {
        final List<SvgAreaElement> elements = new ArrayList<>();

        final SvgCircle circleBackground = new SvgCircle(margin + COLUMN_WIDTH / 2 - CIRCLE_RADIUS, hight, CIRCLE_RADIUS, CIRCLE_BACKGROUND);
        elements.add(circleBackground);

        final SvgCircleSector scoreCircle = new SvgCircleSector(margin + COLUMN_WIDTH / 2 - CIRCLE_RADIUS, hight, CIRCLE_RADIUS);
        scoreCircle.setPercentage(FRUSTRATION_2_CIRCLE);
        scoreCircle.getElementAttributes().setFill(CIRCLE_COLOR);
        scoreCircle.getElementStroke().setStrokeWidth(0);
        elements.add(scoreCircle);

        final SvgText value = new SvgText(FRUSTRATION_2_VALUE, 22, margin + COLUMN_WIDTH / 2, hight + 24);
        value.setTextAlign(TextAlign.CENTER);
        elements.add(value);

        final SvgText titleNL = new SvgText(FONT, FRUSTRATION_2_DUTCH_TITLE, CIRCLE_TITLE, margin + COLUMN_WIDTH / 2, hight + 70);
        titleNL.setFontWeight(FontWeight.BOLD);
        titleNL.setTextAlign(TextAlign.CENTER);
        elements.add(titleNL);

        final SvgText paragraphNL = new SvgText(FONT, FRUSTRATION_2_DUTCH_TEXT, PARAGRAPH_FONT_SIZE, margin, hight + 105);
        paragraphNL.setMaxLineWidth(COLUMN_WIDTH);
        paragraphNL.setTextAlign(TextAlign.JUSTIFY);
        elements.add(paragraphNL);

        return elements;
    }


    private List<SvgAreaElement> generateFrustration2EN(int hight, int margin) {
        final List<SvgAreaElement> elements = new ArrayList<>();

        final SvgCircle circleBackground = new SvgCircle(margin + COLUMN_WIDTH / 2 - CIRCLE_RADIUS, hight, CIRCLE_RADIUS, CIRCLE_BACKGROUND);
        elements.add(circleBackground);

        final SvgCircleSector scoreCircle = new SvgCircleSector(margin + COLUMN_WIDTH / 2 - CIRCLE_RADIUS, hight, CIRCLE_RADIUS);
        scoreCircle.setPercentage(FRUSTRATION_2_CIRCLE);
        scoreCircle.getElementAttributes().setFill(CIRCLE_COLOR);
        scoreCircle.getElementStroke().setStrokeWidth(0);
        elements.add(scoreCircle);

        final SvgText value = new SvgText(FRUSTRATION_2_VALUE, 22, margin + COLUMN_WIDTH / 2, hight + 24);
        value.setTextAlign(TextAlign.CENTER);
        elements.add(value);

        final SvgText titleEN = new SvgText(FONT, FRUSTRATION_2_ENGLISH_TITLE, CIRCLE_TITLE, margin + COLUMN_WIDTH / 2, hight + 70);
        titleEN.setFontWeight(FontWeight.BOLD);
        titleEN.getElementAttributes().setFill(ENGLISH_TEXT_COLOR);
        titleEN.setTextAlign(TextAlign.CENTER);
        elements.add(titleEN);

        final SvgText paragraphEN = new SvgText(FONT, FRUSTRATION_2_ENGLISH_TEXT, PARAGRAPH_FONT_SIZE, margin, hight + 105);
        paragraphEN.setMaxLineWidth(COLUMN_WIDTH);
        paragraphEN.setTextAlign(TextAlign.JUSTIFY);
        paragraphEN.getElementAttributes().setFill(ENGLISH_TEXT_COLOR);
        elements.add(paragraphEN);

        return elements;
    }

    private List<SvgAreaElement> generateFrustration3NL(int hight, int margin) {
        final List<SvgAreaElement> elements = new ArrayList<>();

        final SvgCircle circleBackground = new SvgCircle(margin + COLUMN_WIDTH / 2 - CIRCLE_RADIUS, hight, CIRCLE_RADIUS, CIRCLE_BACKGROUND);
        elements.add(circleBackground);

        final SvgCircleSector scoreCircle = new SvgCircleSector(margin + COLUMN_WIDTH / 2 - CIRCLE_RADIUS, hight, CIRCLE_RADIUS);
        scoreCircle.setPercentage(FRUSTRATION_3_CIRCLE);
        scoreCircle.getElementAttributes().setFill(CIRCLE_COLOR);
        scoreCircle.getElementStroke().setStrokeWidth(0);
        elements.add(scoreCircle);

        final SvgText value = new SvgText(FRUSTRATION_3_VALUE, 22, margin + COLUMN_WIDTH / 2, hight + 24);
        value.setTextAlign(TextAlign.CENTER);
        elements.add(value);

        final SvgText titleNL = new SvgText(FONT, FRUSTRATION_3_DUTCH_TITLE, CIRCLE_TITLE, margin + COLUMN_WIDTH / 2, hight + 70);
        titleNL.setFontWeight(FontWeight.BOLD);
        titleNL.setTextAlign(TextAlign.CENTER);
        elements.add(titleNL);

        final SvgText paragraphNL = new SvgText(FONT, FRUSTRATION_3_DUTCH_TEXT, PARAGRAPH_FONT_SIZE, margin, hight + 105);
        paragraphNL.setMaxLineWidth(COLUMN_WIDTH);
        paragraphNL.setTextAlign(TextAlign.JUSTIFY);
        elements.add(paragraphNL);

        return elements;
    }

    private List<SvgAreaElement> generateFrustration3EN(int hight, int margin) {
        final List<SvgAreaElement> elements = new ArrayList<>();

        final SvgCircle circleBackground = new SvgCircle(margin + COLUMN_WIDTH / 2 - CIRCLE_RADIUS, hight, CIRCLE_RADIUS, CIRCLE_BACKGROUND);
        elements.add(circleBackground);

        final SvgCircleSector scoreCircle = new SvgCircleSector(margin + COLUMN_WIDTH / 2 - CIRCLE_RADIUS, hight, CIRCLE_RADIUS);
        scoreCircle.setPercentage(FRUSTRATION_3_CIRCLE);
        scoreCircle.getElementAttributes().setFill(CIRCLE_COLOR);
        scoreCircle.getElementStroke().setStrokeWidth(0);
        elements.add(scoreCircle);

        final SvgText value = new SvgText(FRUSTRATION_3_VALUE, 22, margin + COLUMN_WIDTH / 2, hight + 24);
        value.setTextAlign(TextAlign.CENTER);
        elements.add(value);

        final SvgText titleEN = new SvgText(FONT, FRUSTRATION_3_ENGLISH_TITLE, CIRCLE_TITLE, margin + COLUMN_WIDTH / 2, hight + 70);
        titleEN.setFontWeight(FontWeight.BOLD);
        titleEN.getElementAttributes().setFill(ENGLISH_TEXT_COLOR);
        titleEN.setTextAlign(TextAlign.CENTER);
        elements.add(titleEN);

        final SvgText paragraphEN = new SvgText(FONT, FRUSTRATION_3_ENGLISH_TEXT, PARAGRAPH_FONT_SIZE, margin, hight + 105);
        paragraphEN.setMaxLineWidth(COLUMN_WIDTH);
        paragraphEN.setTextAlign(TextAlign.JUSTIFY);
        paragraphEN.getElementAttributes().setFill(ENGLISH_TEXT_COLOR);
        elements.add(paragraphEN);

        return elements;
    }

    private List<SvgAreaElement> generateFrustration4NL(int hight, int margin) {
        final List<SvgAreaElement> elements = new ArrayList<>();

        final SvgCircle circleBackground = new SvgCircle(margin + COLUMN_WIDTH / 2 - CIRCLE_RADIUS, hight, CIRCLE_RADIUS, CIRCLE_BACKGROUND);
        elements.add(circleBackground);

        final SvgCircleSector scoreCircle = new SvgCircleSector(margin + COLUMN_WIDTH / 2 - CIRCLE_RADIUS, hight, CIRCLE_RADIUS);
        scoreCircle.setPercentage(FRUSTRATION_4_CIRCLE);
        scoreCircle.getElementAttributes().setFill(CIRCLE_COLOR);
        scoreCircle.getElementStroke().setStrokeWidth(0);
        elements.add(scoreCircle);

        final SvgText value = new SvgText(FRUSTRATION_4_VALUE, 22, margin + COLUMN_WIDTH / 2, hight + 24);
        value.setTextAlign(TextAlign.CENTER);
        elements.add(value);

        final SvgText titleNL = new SvgText(FONT, FRUSTRATION_4_DUTCH_TITLE, CIRCLE_TITLE, margin + COLUMN_WIDTH / 2, hight + 70);
        titleNL.setFontWeight(FontWeight.BOLD);
        titleNL.setTextAlign(TextAlign.CENTER);
        elements.add(titleNL);

        final SvgText paragraphNL = new SvgText(FONT, FRUSTRATION_4_DUTCH_TEXT, PARAGRAPH_FONT_SIZE, margin, hight + 105);
        paragraphNL.setMaxLineWidth(COLUMN_WIDTH);
        paragraphNL.setTextAlign(TextAlign.JUSTIFY);
        elements.add(paragraphNL);

        return elements;
    }

    private List<SvgAreaElement> generateFrustration4EN(int hight, int margin) {
        final List<SvgAreaElement> elements = new ArrayList<>();

        final SvgCircle circleBackground = new SvgCircle(margin + COLUMN_WIDTH / 2 - CIRCLE_RADIUS, hight, CIRCLE_RADIUS, CIRCLE_BACKGROUND);
        elements.add(circleBackground);

        final SvgCircleSector scoreCircle = new SvgCircleSector(margin + COLUMN_WIDTH / 2 - CIRCLE_RADIUS, hight, CIRCLE_RADIUS);
        scoreCircle.setPercentage(FRUSTRATION_4_CIRCLE);
        scoreCircle.getElementAttributes().setFill(CIRCLE_COLOR);
        scoreCircle.getElementStroke().setStrokeWidth(0);
        elements.add(scoreCircle);

        final SvgText value = new SvgText(FRUSTRATION_4_VALUE, 22, margin + COLUMN_WIDTH / 2, hight + 24);
        value.setTextAlign(TextAlign.CENTER);
        elements.add(value);

        final SvgText titleEN = new SvgText(FONT, FRUSTRATION_4_ENGLISH_TITLE, CIRCLE_TITLE, margin + COLUMN_WIDTH / 2, hight + 70);
        titleEN.setFontWeight(FontWeight.BOLD);
        titleEN.getElementAttributes().setFill(ENGLISH_TEXT_COLOR);
        titleEN.setTextAlign(TextAlign.CENTER);
        elements.add(titleEN);

        final SvgText paragraphEN = new SvgText(FONT, FRUSTRATION_4_ENGLISH_TEXT, PARAGRAPH_FONT_SIZE, margin, hight + 105);
        paragraphEN.setMaxLineWidth(COLUMN_WIDTH);
        paragraphEN.setTextAlign(TextAlign.JUSTIFY);
        paragraphEN.getElementAttributes().setFill(ENGLISH_TEXT_COLOR);
        elements.add(paragraphEN);

        return elements;
    }

    private List<SvgAreaElement> generateFrustration5NL(int hight, int margin) {
        final List<SvgAreaElement> elements = new ArrayList<>();

        final SvgCircle circleBackground = new SvgCircle(margin + COLUMN_WIDTH / 2 - CIRCLE_RADIUS, hight, CIRCLE_RADIUS, CIRCLE_BACKGROUND);
        elements.add(circleBackground);

        final SvgCircleSector scoreCircle = new SvgCircleSector(margin + COLUMN_WIDTH / 2 - CIRCLE_RADIUS, hight, CIRCLE_RADIUS);
        scoreCircle.setPercentage(FRUSTRATION_5_CIRCLE);
        scoreCircle.getElementAttributes().setFill(CIRCLE_COLOR);
        scoreCircle.getElementStroke().setStrokeWidth(0);
        elements.add(scoreCircle);

        final SvgText value = new SvgText(FRUSTRATION_5_VALUE, 22, margin + COLUMN_WIDTH / 2, hight + 24);
        value.setTextAlign(TextAlign.CENTER);
        elements.add(value);

        final SvgText titleNL = new SvgText(FONT, FRUSTRATION_5_DUTCH_TITLE, CIRCLE_TITLE, margin + COLUMN_WIDTH / 2, hight + 70);
        titleNL.setFontWeight(FontWeight.BOLD);
        titleNL.setTextAlign(TextAlign.CENTER);
        elements.add(titleNL);

        final SvgText paragraphNL = new SvgText(FONT, FRUSTRATION_5_DUTCH_TEXT, PARAGRAPH_FONT_SIZE, margin, hight + 105);
        paragraphNL.setMaxLineWidth(COLUMN_WIDTH);
        paragraphNL.setTextAlign(TextAlign.JUSTIFY);
        elements.add(paragraphNL);

        return elements;
    }

    private List<SvgAreaElement> generateFrustration5EN(int hight, int margin) {
        final List<SvgAreaElement> elements = new ArrayList<>();

        final SvgCircle circleBackground = new SvgCircle(margin + COLUMN_WIDTH / 2 - CIRCLE_RADIUS, hight, CIRCLE_RADIUS, CIRCLE_BACKGROUND);
        elements.add(circleBackground);

        final SvgCircleSector scoreCircle = new SvgCircleSector(margin + COLUMN_WIDTH / 2 - CIRCLE_RADIUS, hight, CIRCLE_RADIUS);
        scoreCircle.setPercentage(FRUSTRATION_5_CIRCLE);
        scoreCircle.getElementAttributes().setFill(CIRCLE_COLOR);
        scoreCircle.getElementStroke().setStrokeWidth(0);
        elements.add(scoreCircle);

        final SvgText value = new SvgText(FRUSTRATION_5_VALUE, 22, margin + COLUMN_WIDTH / 2, hight + 24);
        value.setTextAlign(TextAlign.CENTER);
        elements.add(value);

        final SvgText titleEN = new SvgText(FONT, FRUSTRATION_5_ENGLISH_TITLE, CIRCLE_TITLE, margin + COLUMN_WIDTH / 2, hight + 70);
        titleEN.setFontWeight(FontWeight.BOLD);
        titleEN.getElementAttributes().setFill(ENGLISH_TEXT_COLOR);
        titleEN.setTextAlign(TextAlign.CENTER);
        elements.add(titleEN);

        final SvgText paragraphEN = new SvgText(FONT, FRUSTRATION_5_ENGLISH_TEXT, PARAGRAPH_FONT_SIZE, margin, hight + 105);
        paragraphEN.setMaxLineWidth(COLUMN_WIDTH);
        paragraphEN.setTextAlign(TextAlign.JUSTIFY);
        paragraphEN.getElementAttributes().setFill(ENGLISH_TEXT_COLOR);
        elements.add(paragraphEN);

        return elements;
    }


    private List<SvgAreaElement> generateFooter() {
        final List<SvgAreaElement> elements = new ArrayList<>();

        final SvgText name = new SvgText(USER_NAME + " " + USER_LASTNAME, 14, 62, DOCUMENT_HEIGHT - 50);
        elements.add(name);

        final SvgText date = new SvgText(SUBMIT_DATE + " " + SUBMIT_TIME, 14, 737, DOCUMENT_HEIGHT - 50);
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
        frustrationOnTeamworking.setId(TEMPLATE_ID);
        frustrationOnTeamworking.getElementAttributes().setWidth(DOCUMENT_WIDTH);
        frustrationOnTeamworking.getElementAttributes().setHeight(DOCUMENT_HEIGHT);
        frustrationOnTeamworking.setSvgBackground(generateBackground());

        frustrationOnTeamworking.addElements(generateTitleNL(MAIN_MARGIN));
        frustrationOnTeamworking.addElements(generateTitleEN(DOCUMENT_WIDTH / 2 + SECOND_MARGIN));
        frustrationOnTeamworking.addElements(generateFrustration1NL(340, MAIN_MARGIN));
        frustrationOnTeamworking.addElements(generateFrustration1EN(340, DOCUMENT_WIDTH / 2 + SECOND_MARGIN));
        frustrationOnTeamworking.addElements(generateFrustration2NL(590, MAIN_MARGIN));
        frustrationOnTeamworking.addElements(generateFrustration2EN(590, DOCUMENT_WIDTH / 2 + SECOND_MARGIN));
        frustrationOnTeamworking.addElements(generateFrustration3NL(825, MAIN_MARGIN));
        frustrationOnTeamworking.addElements(generateFrustration3EN(825, DOCUMENT_WIDTH / 2 + SECOND_MARGIN));
        frustrationOnTeamworking.addElements(generateFrustration4NL(1050, MAIN_MARGIN));
        frustrationOnTeamworking.addElements(generateFrustration4EN(1050, DOCUMENT_WIDTH / 2 + SECOND_MARGIN));
        frustrationOnTeamworking.addElements(generateFrustration5NL(1290, MAIN_MARGIN));
        frustrationOnTeamworking.addElements(generateFrustration5EN(1290, DOCUMENT_WIDTH / 2 + SECOND_MARGIN));

        frustrationOnTeamworking.addElements(generateFooter());
    }

    @Test(dependsOnMethods = "generateFrustrationAtTeamworking")
    public void executeFrustrationAtTeamworking() throws IOException {
        FontFactory.resetFonts();
        final DroolsSubmittedForm droolsSubmittedForm = DroolsSubmittedForm.getFromJson(FileReader.getResource(DROOLS_FORM_FILE_PATH, StandardCharsets.UTF_8));
        final List<String> svgResults = svgFromDroolsConverter.execute(droolsSubmittedForm, Collections.singletonList(frustrationOnTeamworking));
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

    @AfterClass(alwaysRun = true)
    public void removeUser() {
        authenticatedUserProvider.clear();
        userProvider.reset();
    }
}
