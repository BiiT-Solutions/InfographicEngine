package com.biit.infographic.core.svg;

import com.biit.drools.form.DroolsSubmittedForm;
import com.biit.infographic.core.controllers.DroolsResultController;
import com.biit.infographic.core.generators.SvgGenerator;
import com.biit.infographic.core.models.svg.SvgAreaElement;
import com.biit.infographic.core.models.svg.SvgTemplate;
import com.biit.infographic.core.models.svg.clip.ClipDirection;
import com.biit.infographic.core.models.svg.clip.SvgRectangleClipPath;
import com.biit.infographic.core.models.svg.components.SvgCircle;
import com.biit.infographic.core.models.svg.components.SvgRectangle;
import com.biit.infographic.core.pdf.PdfController;
import com.biit.server.utils.exceptions.EmptyPdfBodyException;
import com.biit.server.utils.exceptions.InvalidXmlElementException;
import com.biit.utils.file.FileReader;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
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
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@SpringBootTest
@Test(groups = "cadt")
public class CadtProfileComparatorLayerFormDroolsTest extends CadtProfileCreatorFormDroolsTest {
    protected static final String OUTPUT_FOLDER = System.getProperty("java.io.tmpdir") + File.separator + "SvgTests";

    private static final String DROOLS_FORM_FILE_PATH = "drools/DroolsSubmittedCadtProfileCreator.json";

    @Autowired
    private DroolsResultController droolsResultController;

    @Autowired
    private PdfController pdfController;


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

        //Show only half-circles.
        elements.forEach(element -> {
            if (element instanceof SvgCircle) {
                element.setClipPath(new SvgRectangleClipPath(0.5, ClipDirection.LEFT_TO_RIGHT));
            }
            if (element instanceof SvgRectangle) {
                element.setClipPath(new SvgRectangleClipPath(0.5, ClipDirection.LEFT_TO_RIGHT));
            }
        });


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
                + File.separator + "cadtProfileComparatorLayerFromDrools.svg")), true)) {
            out.println(svgResults.get(0));
        }

        checkContent(svgResults.get(0), "cadtProfileComparatorLayerFromDrools.svg");

        final String filePath = OUTPUT_FOLDER + File.separator + "cadtProfileComparatorLayerFromDrools.pdf";
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
