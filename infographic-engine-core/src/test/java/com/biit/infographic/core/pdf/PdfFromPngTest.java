package com.biit.infographic.core.pdf;

import com.biit.infographic.core.models.svg.ElementAttributes;
import com.biit.infographic.core.models.svg.SvgTemplate;
import com.biit.infographic.core.models.svg.components.SvgImage;
import com.biit.infographic.core.models.svg.components.text.FontVariantType;
import com.biit.infographic.core.models.svg.components.text.SvgText;
import com.biit.infographic.core.models.svg.components.text.TextAlign;
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

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;

@SpringBootTest
@Test(groups = "pdfFromPng")
public class PdfFromPngTest extends AbstractTestNGSpringContextTests {

    private static final String LONG_TEXT = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Integer turpis erat, rutrum et neque sit amet, rhoncus tincidunt felis. Vivamus nibh quam, commodo eget maximus quis, lobortis id dolor. Nullam ac sem bibendum, molestie nibh at, facilisis arcu. Aliquam ullamcorper varius orci quis tempor. Lorem ipsum dolor sit amet, consectetur adipiscing elit. Orci varius natoque penatibus et magnis dis parturient montes, nascetur ridiculus mus. Nam imperdiet magna eget turpis maximus tempor. Suspendisse tincidunt vel elit eu iaculis. Etiam sem risus, sodales in lorem eget, suscipit ultricies arcu. In pellentesque interdum rutrum. Nullam pharetra purus et interdum lacinia. Curabitur malesuada tortor ac tortor laoreet, quis placerat magna hendrerit.";

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

        final String filePath = OUTPUT_FOLDER + File.separator + "documentColoredTextPng.pdf";
        final File destFile = new File(filePath);
        destFile.createNewFile();
        try (FileOutputStream fos = new FileOutputStream(filePath)) {
            fos.write(pdfController.generatePdfAsPngImage(svgTemplate));
        }
    }

    @Test
    public void documentImageTest() throws IOException, InvalidXmlElementException, EmptyPdfBodyException, URISyntaxException {
        SvgTemplate svgTemplate = new SvgTemplate();
        svgTemplate.addElement(new SvgImage(new ElementAttributes(SvgTemplate.DEFAULT_WIDTH / 2, SvgTemplate.DEFAULT_HEIGHT / 2,
                String.valueOf(SvgTemplate.DEFAULT_WIDTH / 2), String.valueOf(SvgTemplate.DEFAULT_HEIGHT / 2)), "EliseNess",
                readBase64Image("EliseNess.txt")));

        final String filePath = OUTPUT_FOLDER + File.separator + "documentImagePng.pdf";
        final File destFile = new File(filePath);
        destFile.createNewFile();
        try (FileOutputStream fos = new FileOutputStream(filePath)) {
            fos.write(pdfController.generatePdfAsPngImage(svgTemplate));
        }
    }

    @Test
    public void documentMondayDonuts() throws IOException, InvalidXmlElementException, EmptyPdfBodyException {
        SvgTemplate svgTemplate = new SvgTemplate(SvgTemplate.DEFAULT_WIDTH, SvgTemplate.DEFAULT_HEIGHT);
        SvgText text = new SvgText("Monday Donuts", LONG_TEXT, 8, 0L, 0L);
        text.setFontVariant(FontVariantType.NORMAL);
        text.setTextAlign(TextAlign.JUSTIFY);
        text.setMaxLineWidth(200);
        text.setMaxParagraphHeight(90);
        svgTemplate.addElement(text);

        final String filePath = OUTPUT_FOLDER + File.separator + "documentMondayDonutsFontPng.pdf";
        final File destFile = new File(filePath);
        destFile.createNewFile();
        try (FileOutputStream fos = new FileOutputStream(filePath)) {
            fos.write(pdfController.generatePdfAsPngImage(svgTemplate));
        }
    }

    @AfterClass
    public void removeFolder() {
        Assert.assertTrue(deleteDirectory(new File(OUTPUT_FOLDER)));
    }
}
