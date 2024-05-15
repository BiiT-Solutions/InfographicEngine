package com.biit.infographic.core.svg;

import com.biit.drools.form.DroolsSubmittedForm;
import com.biit.infographic.core.controllers.DroolsResultController;
import com.biit.infographic.core.generators.SvgGenerator;
import com.biit.infographic.core.models.svg.SvgAreaElement;
import com.biit.infographic.core.models.svg.SvgBackground;
import com.biit.infographic.core.models.svg.SvgTemplate;
import com.biit.infographic.core.models.svg.components.SvgPath;
import com.biit.infographic.core.models.svg.components.path.BezierCurve;
import com.biit.infographic.core.models.svg.components.path.HorizontalLine;
import com.biit.infographic.core.models.svg.components.path.VerticalLine;
import com.biit.infographic.core.models.svg.components.text.FontFactory;
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
@Test(groups = "boardingPass")
public class BoardingPassFromDroolsTest extends AbstractTestNGSpringContextTests {

    protected static final String OUTPUT_FOLDER = System.getProperty("java.io.tmpdir") + File.separator + "SvgTests";

    private final static String DROOLS_FORM_FILE_PATH = "drools/boardingPassTemplate.json";

    @Autowired
    private DroolsResultController droolsResultController;

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
        svgBackground.setBackgroundColor("#f0eeed");
        return svgBackground;
    }

    private List<SvgAreaElement> generatePath() {
        final List<SvgAreaElement> elements = new ArrayList<>();

        final SvgPath path = new SvgPath(43, 35,
                new VerticalLine(3, true),
                new BezierCurve(8, 8, 0, 4, 4, 8, true),
                new HorizontalLine(30, true));

        path.getElementStroke().setStrokeColor("d3d4d4");
        path.getElementStroke().setStrokeWidth(1);

        elements.add(path);
        return elements;
    }

    @BeforeClass
    public void prepareFolder() throws IOException {
        Files.createDirectories(Paths.get(OUTPUT_FOLDER));
    }

    @Test
    public void generateBoardingPass() throws IOException {
        boardingPassTemplate = new SvgTemplate();
        boardingPassTemplate.getElementAttributes().setHeight(319L);
        boardingPassTemplate.getElementAttributes().setWidth(500L);
        boardingPassTemplate.setSvgBackground(generateBackground());

        boardingPassTemplate.addElements(generatePath());
    }

    @Test(dependsOnMethods = "generateBoardingPass")
    public void executeBoardingPass() throws IOException {
        FontFactory.resetFonts();
        final DroolsSubmittedForm droolsSubmittedForm = DroolsSubmittedForm.getFromJson(FileReader.getResource(DROOLS_FORM_FILE_PATH, StandardCharsets.UTF_8));
        final List<String> svgResults = droolsResultController.execute(droolsSubmittedForm, Collections.singletonList(boardingPassTemplate));
        Assert.assertEquals(svgResults.size(), 1);

        try (PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(OUTPUT_FOLDER
                + File.separator + "boardingPassTemplate.svg")), true)) {
            out.println(svgResults.get(0));
        }

        checkContent(svgResults.get(0), "boardingPassTemplate.svg");
    }

    @Test(dependsOnMethods = "generateBoardingPass")
    public void checkSerialization() throws JsonProcessingException {
        //cadtTemplate.toJson() is what must be deployed into the infographic docker container
        SvgTemplate svgTemplate1 = SvgTemplate.fromJson(boardingPassTemplate.toJson());
        Assert.assertEquals(SvgGenerator.generate(svgTemplate1), SvgGenerator.generate(boardingPassTemplate));
    }


    @AfterClass
    public void removeFolder() {
        Assert.assertTrue(deleteDirectory(new File(OUTPUT_FOLDER)));
    }
}
