package com.biit.infographic.core.pdf;

import com.biit.infographic.core.generators.SvgGenerator;
import com.biit.infographic.core.models.svg.ElementAttributes;
import com.biit.infographic.core.models.svg.SvgTemplate;
import com.biit.infographic.core.models.svg.components.SvgImage;
import com.biit.infographic.core.models.svg.components.text.SvgText;
import com.biit.server.utils.exceptions.EmptyPdfBodyException;
import com.biit.server.utils.exceptions.InvalidXmlElementException;
import org.apache.commons.io.FileUtils;
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
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;

@SpringBootTest
@Test(groups = "pdfFromSvg")
public class PdfFromSVGTest extends AbstractTestNGSpringContextTests {

    private static final String OUTPUT_FOLDER = System.getProperty("java.io.tmpdir") + File.separator + "SvgTests";

    @Autowired
    private PdfController pdfController;

    protected boolean deleteDirectory(File directoryToBeDeleted) {
        File[] allContents = directoryToBeDeleted.listFiles();
        if (allContents != null) {
            for (File file : allContents) {
                deleteDirectory(file);
            }
        }
        return directoryToBeDeleted.delete();
    }

    protected String readBase64Image(String imageName) {
        try {
            return new String(Files.readAllBytes(Paths.get(getClass().getClassLoader()
                    .getResource("images" + File.separator + imageName).toURI())));
        } catch (Exception e) {
            Assert.fail("Cannot read resource 'images/" + imageName + "'.");
        }
        return null;
    }

    private void checkContent(File file1, String resourceFile) throws URISyntaxException, IOException {
        final File comparedToFile = new File(Paths.get(getClass().getClassLoader()
                .getResource("pdf" + File.separator + resourceFile).toURI()).toString());
        //Not working! Each PDF has some bytes different.
        Assert.assertTrue(FileUtils.contentEquals(file1, comparedToFile));
    }

    @BeforeClass
    public void prepareFolder() throws IOException {
        Files.createDirectories(Paths.get(OUTPUT_FOLDER));
    }

    @Test
    public void generatePdfWithText() throws InvalidXmlElementException, EmptyPdfBodyException, IOException, URISyntaxException {
        SvgTemplate svgTemplate = new SvgTemplate(SvgTemplate.DEFAULT_WIDTH, SvgTemplate.DEFAULT_HEIGHT);
        final SvgText text = new SvgText("This is the first text", 12, SvgTemplate.DEFAULT_WIDTH / 2, SvgTemplate.DEFAULT_HEIGHT / 2);
        text.getElementAttributes().setFill("ff0000");
        text.getElementStroke().setStrokeColor("0000ff");
        text.getElementStroke().setStrokeWidth(0.2);
        text.getElementStroke().setStrokeDash(Arrays.asList(5, 5, 10, 10, 1));
        svgTemplate.addElement(text);

        final String filePath = OUTPUT_FOLDER + File.separator + "documentColoredText.pdf";
        final File destFile = new File(filePath);
        destFile.createNewFile();
        try (FileOutputStream fos = new FileOutputStream(filePath)) {
            fos.write(pdfController.generatePdfFromSVG(svgTemplate));
        }
    }

    @Test
    public void documentImageTest() throws IOException, InvalidXmlElementException, EmptyPdfBodyException, URISyntaxException {
        SvgTemplate svgTemplate = new SvgTemplate();
        svgTemplate.addElement(new SvgImage(new ElementAttributes(SvgTemplate.DEFAULT_WIDTH / 2, SvgTemplate.DEFAULT_HEIGHT / 2,
                String.valueOf(SvgTemplate.DEFAULT_WIDTH / 2), String.valueOf(SvgTemplate.DEFAULT_HEIGHT / 2)), "EliseNess",
                readBase64Image("EliseNess.txt")));

        try (PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(OUTPUT_FOLDER
                + File.separator + "documentImage.svg")), true)) {
            out.println(SvgGenerator.generate(svgTemplate));
        }

        final String filePath = OUTPUT_FOLDER + File.separator + "documentImage.pdf";
        final File destFile = new File(filePath);
        destFile.createNewFile();
        try (FileOutputStream fos = new FileOutputStream(filePath)) {
            fos.write(pdfController.generatePdfFromSVG(svgTemplate));
        }
    }

    @AfterClass
    public void removeFolder() {
        Assert.assertTrue(deleteDirectory(new File(OUTPUT_FOLDER)));
    }
}
