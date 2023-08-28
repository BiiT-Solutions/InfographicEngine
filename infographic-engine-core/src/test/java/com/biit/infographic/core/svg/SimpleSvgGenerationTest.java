package com.biit.infographic.core.svg;

import com.biit.infographic.core.models.svg.ElementAttributes;
import com.biit.infographic.core.models.svg.SvgBackground;
import com.biit.infographic.core.models.svg.SvgDocument;
import com.biit.infographic.core.models.svg.components.StrokeLineCap;
import com.biit.infographic.core.models.svg.components.SvgCircle;
import com.biit.infographic.core.models.svg.components.SvgEllipse;
import com.biit.infographic.core.models.svg.components.SvgImage;
import com.biit.infographic.core.models.svg.components.SvgLine;
import com.biit.infographic.core.models.svg.components.SvgRectangle;
import com.biit.infographic.core.models.svg.components.SvgScript;
import com.biit.infographic.core.models.svg.components.text.FontLengthAdjust;
import com.biit.infographic.core.models.svg.components.text.FontVariantType;
import com.biit.infographic.core.models.svg.components.text.SvgText;
import com.biit.infographic.core.models.svg.components.text.TextAlign;
import com.biit.infographic.core.models.svg.exceptions.InvalidAttributeException;
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
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;

@Test(groups = {"simpleSvgGenerationTest"})
public class SimpleSvgGenerationTest {
    private static final String OUTPUT_FOLDER = System.getProperty("java.io.tmpdir") + File.separator + "SvgTests";
    private static final String LONG_TEXT = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Integer turpis erat, rutrum et neque sit amet, rhoncus tincidunt felis. Vivamus nibh quam, commodo eget maximus quis, lobortis id dolor. Nullam ac sem bibendum, molestie nibh at, facilisis arcu. Aliquam ullamcorper varius orci quis tempor. Lorem ipsum dolor sit amet, consectetur adipiscing elit. Orci varius natoque penatibus et magnis dis parturient montes, nascetur ridiculus mus. Nam imperdiet magna eget turpis maximus tempor. Suspendisse tincidunt vel elit eu iaculis. Etiam sem risus, sodales in lorem eget, suscipit ultricies arcu. In pellentesque interdum rutrum. Nullam pharetra purus et interdum lacinia. Curabitur malesuada tortor ac tortor laoreet, quis placerat magna hendrerit.";
    private static final String SCRIPT = """
            function getColor() {
                  const R = Math.round(Math.random() * 255)
                    .toString(16)
                    .padStart(2, "0");
                        
                  const G = Math.round(Math.random() * 255)
                    .toString(16)
                    .padStart(2, "0");
                        
                  const B = Math.round(Math.random() * 255)
                    .toString(16)
                    .padStart(2, "0");
                        
                  return `#${R}${G}${B}`;
                }
                
                document.querySelector("circle").addEventListener("click", (e) => {
                    e.target.style.fill = getColor();
                });
            """;

    private String readBase64Image(String imageName) {
        try {
            return new String(Files.readAllBytes(Paths.get(getClass().getClassLoader()
                    .getResource("images" + File.separator + imageName).toURI())));
        } catch (Exception e) {
            Assert.fail("Cannot read resource 'images/" + imageName + "'.");
        }
        return null;
    }

    private boolean deleteDirectory(File directoryToBeDeleted) {
        File[] allContents = directoryToBeDeleted.listFiles();
        if (allContents != null) {
            for (File file : allContents) {
                deleteDirectory(file);
            }
        }
        return directoryToBeDeleted.delete();
    }

    private void checkContent(String content, String resourceFile) {
        try {
            Assert.assertEquals(content.trim(), new String(Files.readAllBytes(Paths.get(getClass().getClassLoader()
                    .getResource("svg" + File.separator + resourceFile).toURI()))).trim());
        } catch (IOException | URISyntaxException e) {
            Assert.fail();
        }
    }

    @BeforeClass
    public void prepareFolder() throws IOException {
        Files.createDirectories(Paths.get(OUTPUT_FOLDER));
    }

    @Test
    public void documentBackgroundColorTest() throws IOException {
        SvgDocument svgDocument = new SvgDocument();
        svgDocument.setSvgBackground(new SvgBackground().backgroundColor("#449911"));

        try (PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(OUTPUT_FOLDER
                + File.separator + "documentBackgroundColor.svg")), true)) {
            out.println(SvgGenerator.generate(svgDocument));
        }
        checkContent(SvgGenerator.generate(svgDocument), "documentBackgroundColor.svg");
    }

    @Test
    public void documentBackgroundImageTest() throws IOException {
        SvgDocument svgDocument = new SvgDocument();
        svgDocument.setSvgBackground(new SvgBackground().image(readBase64Image("EliseNess.txt")));

        try (PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(OUTPUT_FOLDER
                + File.separator + "documentBackgroundImage.svg")), true)) {
            out.println(SvgGenerator.generate(svgDocument));
        }
        checkContent(SvgGenerator.generate(svgDocument), "documentBackgroundImage.svg");
    }

    @Test
    public void documentImageTest() throws IOException {
        SvgDocument svgDocument = new SvgDocument();
        svgDocument.addElement(new SvgImage(new ElementAttributes(SvgDocument.DEFAULT_WIDTH / 2, SvgDocument.DEFAULT_HEIGHT / 2,
                String.valueOf(SvgDocument.DEFAULT_WIDTH / 2), String.valueOf(SvgDocument.DEFAULT_HEIGHT / 2)), "EliseNess",
                readBase64Image("EliseNess.txt")));

        try (PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(OUTPUT_FOLDER
                + File.separator + "documentImage.svg")), true)) {
            out.println(SvgGenerator.generate(svgDocument));
        }
        checkContent(SvgGenerator.generate(svgDocument), "documentImage.svg");
    }

    @Test
    public void documentDrawRectangleTest() throws IOException {
        SvgDocument svgDocument = new SvgDocument();
        svgDocument.addElement(new SvgRectangle(SvgDocument.DEFAULT_WIDTH / 2, SvgDocument.DEFAULT_HEIGHT / 2,
                String.valueOf(SvgDocument.DEFAULT_WIDTH / 2), String.valueOf(SvgDocument.DEFAULT_HEIGHT / 2), "ff0000"));

        try (PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(OUTPUT_FOLDER
                + File.separator + "documentDrawRectangle.svg")), true)) {
            out.println(SvgGenerator.generate(svgDocument));
        }
        checkContent(SvgGenerator.generate(svgDocument), "documentDrawRectangle.svg");
    }

    @Test
    public void documentDrawRectangleWithRadiusTest() throws IOException {
        SvgDocument svgDocument = new SvgDocument();
        final SvgRectangle rectangle = new SvgRectangle(SvgDocument.DEFAULT_WIDTH / 2, SvgDocument.DEFAULT_HEIGHT / 2,
                String.valueOf(SvgDocument.DEFAULT_WIDTH / 2), String.valueOf(SvgDocument.DEFAULT_HEIGHT / 2), "ff0000");
        rectangle.setXRadius(25L);
        rectangle.setXRadius(50L);
        svgDocument.addElement(rectangle);

        try (PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(OUTPUT_FOLDER
                + File.separator + "documentDrawRectangleWithRadius.svg")), true)) {
            out.println(SvgGenerator.generate(svgDocument));
        }
        checkContent(SvgGenerator.generate(svgDocument), "documentDrawRectangleWithRadius.svg");
    }

    @Test(expectedExceptions = InvalidAttributeException.class)
    public void documentDrawInvalidRectangleTest() {
        SvgDocument svgDocument = new SvgDocument();
        svgDocument.addElement(new SvgRectangle(SvgDocument.DEFAULT_WIDTH / 2, SvgDocument.DEFAULT_HEIGHT / 2,
                null, String.valueOf(SvgDocument.DEFAULT_HEIGHT / 2), "ff0000"));

        SvgGenerator.generate(svgDocument);
    }

    @Test
    public void documentDrawCircleTest() throws IOException {
        SvgDocument svgDocument = new SvgDocument();
        svgDocument.addElement(new SvgCircle(SvgDocument.DEFAULT_WIDTH / 2, SvgDocument.DEFAULT_HEIGHT / 2,
                SvgDocument.DEFAULT_WIDTH / 2));

        try (PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(OUTPUT_FOLDER
                + File.separator + "documentDrawCircle.svg")), true)) {
            out.println(SvgGenerator.generate(svgDocument));
        }

        checkContent(SvgGenerator.generate(svgDocument), "documentDrawCircle.svg");
    }

    @Test(expectedExceptions = InvalidAttributeException.class)
    public void documentDrawInvalidCircleTest() throws IOException {
        SvgDocument svgDocument = new SvgDocument();
        svgDocument.addElement(new SvgCircle(SvgDocument.DEFAULT_WIDTH / 2, SvgDocument.DEFAULT_HEIGHT / 2,
                0L));

        SvgGenerator.generate(svgDocument);
    }

    @Test
    public void documentDrawCircleOutsideTest() throws IOException {
        SvgDocument svgDocument = new SvgDocument();
        svgDocument.addElement(new SvgCircle(SvgDocument.DEFAULT_WIDTH / 2 - 100, SvgDocument.DEFAULT_HEIGHT / 2 - 100,
                SvgDocument.DEFAULT_HEIGHT));

        try (PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(OUTPUT_FOLDER
                + File.separator + "documentDrawCircleOutside.svg")), true)) {
            out.println(SvgGenerator.generate(svgDocument));
        }

        checkContent(SvgGenerator.generate(svgDocument), "documentDrawCircleOutside.svg");
    }

    @Test
    public void documentDrawEllipseTest() throws IOException {
        SvgDocument svgDocument = new SvgDocument();
        final SvgEllipse ellipse = new SvgEllipse(SvgDocument.DEFAULT_WIDTH / 2, SvgDocument.DEFAULT_HEIGHT / 2,
                SvgDocument.DEFAULT_WIDTH / 2, SvgDocument.DEFAULT_HEIGHT / 2
                , "ff00ff");
        ellipse.setXRadius(25L);
        ellipse.setYRadius(50L);
        svgDocument.addElement(ellipse);

        try (PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(OUTPUT_FOLDER
                + File.separator + "documentDrawEllipse.svg")), true)) {
            out.println(SvgGenerator.generate(svgDocument));
        }

        checkContent(SvgGenerator.generate(svgDocument), "documentDrawEllipse.svg");
    }

    @Test
    public void documentDrawEllipseOutsideTest() throws IOException {
        SvgDocument svgDocument = new SvgDocument();
        final SvgEllipse ellipse = new SvgEllipse(SvgDocument.DEFAULT_WIDTH, SvgDocument.DEFAULT_HEIGHT,
                SvgDocument.DEFAULT_WIDTH / 2, SvgDocument.DEFAULT_HEIGHT / 2
                , "ff00ff");
        ellipse.setXRadius(25L);
        ellipse.setYRadius(50L);
        svgDocument.addElement(ellipse);

        try (PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(OUTPUT_FOLDER
                + File.separator + "documentDrawEllipseOutside.svg")), true)) {
            out.println(SvgGenerator.generate(svgDocument));
        }

        checkContent(SvgGenerator.generate(svgDocument), "documentDrawEllipseOutside.svg");
    }

    @Test
    public void documentDrawLineTest() throws IOException {
        SvgDocument svgDocument = new SvgDocument();
        final SvgLine line = new SvgLine(50L, 50L, SvgDocument.DEFAULT_WIDTH, SvgDocument.DEFAULT_HEIGHT);
        svgDocument.addElement(line);

        try (PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(OUTPUT_FOLDER
                + File.separator + "documentDrawLine.svg")), true)) {
            out.println(SvgGenerator.generate(svgDocument));
        }

        checkContent(SvgGenerator.generate(svgDocument), "documentDrawLine.svg");
    }

    @Test
    public void documentDrawLineOutsideTest() throws IOException {
        SvgDocument svgDocument = new SvgDocument();
        final SvgLine line = new SvgLine(50L, 50L, SvgDocument.DEFAULT_WIDTH + 100, SvgDocument.DEFAULT_HEIGHT + 50);
        svgDocument.addElement(line);

        try (PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(OUTPUT_FOLDER
                + File.separator + "documentDrawLineOutside.svg")), true)) {
            out.println(SvgGenerator.generate(svgDocument));
        }

        checkContent(SvgGenerator.generate(svgDocument), "documentDrawLineOutside.svg");
    }

    @Test
    public void documentDrawLineStrokeTest() throws IOException {
        SvgDocument svgDocument = new SvgDocument();
        final SvgLine line = new SvgLine(50L, 50L, SvgDocument.DEFAULT_WIDTH, SvgDocument.DEFAULT_HEIGHT);
        line.setLineCap(StrokeLineCap.ROUND);
        line.setStrokeDash(Arrays.asList(5, 5, 10, 10, 1));
        svgDocument.addElement(line);

        try (PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(OUTPUT_FOLDER
                + File.separator + "documentDrawLineStroke.svg")), true)) {
            out.println(SvgGenerator.generate(svgDocument));
        }

        checkContent(SvgGenerator.generate(svgDocument), "documentDrawLineStroke.svg");
    }

    @Test
    public void documentSimpleTextTest() throws IOException {
        SvgDocument svgDocument = new SvgDocument(SvgDocument.DEFAULT_WIDTH, SvgDocument.DEFAULT_HEIGHT);
        final SvgText text = new SvgText("This is the first text", 12, SvgDocument.DEFAULT_WIDTH / 2, SvgDocument.DEFAULT_HEIGHT / 2);
        svgDocument.addElement(text);

        try (PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(OUTPUT_FOLDER
                + File.separator + "documentSimpleText.svg")), true)) {
            out.println(SvgGenerator.generate(svgDocument));
        }

        checkContent(SvgGenerator.generate(svgDocument), "documentSimpleText.svg");
    }

    @Test
    public void documentLongTextLimitedLineTest() throws IOException {
        SvgDocument svgDocument = new SvgDocument(SvgDocument.DEFAULT_WIDTH, SvgDocument.DEFAULT_HEIGHT);
        final SvgText text = new SvgText(LONG_TEXT, 12, SvgDocument.DEFAULT_WIDTH / 2, SvgDocument.DEFAULT_HEIGHT / 2);
        text.setLengthAdjust(FontLengthAdjust.SPACING);
        text.setMaxLineLength(80);
        svgDocument.addElement(text);

        try (PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(OUTPUT_FOLDER
                + File.separator + "documentLongTextLimitedLine.svg")), true)) {
            out.println(SvgGenerator.generate(svgDocument));
        }

        checkContent(SvgGenerator.generate(svgDocument), "documentLongTextLimitedLine.svg");
    }

    @Test
    public void documentLongTextLimitedLineForcingLengthTest() throws IOException {
        SvgDocument svgDocument = new SvgDocument(SvgDocument.DEFAULT_WIDTH, SvgDocument.DEFAULT_HEIGHT);
        final SvgText text = new SvgText(LONG_TEXT, 12, SvgDocument.DEFAULT_WIDTH / 2, SvgDocument.DEFAULT_HEIGHT / 2);
        text.setTextLength("8000");
        text.setLengthAdjust(FontLengthAdjust.SPACING);
        text.setMaxLineLength(80);
        svgDocument.addElement(text);

        try (PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(OUTPUT_FOLDER
                + File.separator + "documentLongTextLimitedLineForcingLength.svg")), true)) {
            out.println(SvgGenerator.generate(svgDocument));
        }

        checkContent(SvgGenerator.generate(svgDocument), "documentLongTextLimitedLineForcingLength.svg");
    }

    @Test
    public void documentLongTextAlignRightTest() throws IOException {
        SvgDocument svgDocument = new SvgDocument(SvgDocument.DEFAULT_WIDTH, SvgDocument.DEFAULT_HEIGHT);
        final SvgText text = new SvgText(LONG_TEXT, 12, SvgDocument.DEFAULT_WIDTH / 2, SvgDocument.DEFAULT_HEIGHT / 2);
        text.setTextAlign(TextAlign.RIGHT);
        text.setMaxLineLength(80);
        svgDocument.addElement(text);

        try (PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(OUTPUT_FOLDER
                + File.separator + "documentLongTextAlignRight.svg")), true)) {
            out.println(SvgGenerator.generate(svgDocument));
        }

        checkContent(SvgGenerator.generate(svgDocument), "documentLongTextAlignRight.svg");
    }

    @Test
    public void documentLongTextJustifyTest() throws IOException {
        SvgDocument svgDocument = new SvgDocument(SvgDocument.DEFAULT_WIDTH, SvgDocument.DEFAULT_HEIGHT);
        final SvgText text = new SvgText(LONG_TEXT, 12, SvgDocument.DEFAULT_WIDTH / 2, SvgDocument.DEFAULT_HEIGHT / 2);
        text.setTextAlign(TextAlign.JUSTIFY);
        text.setMaxLineLength(80);
        svgDocument.addElement(text);

        try (PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(OUTPUT_FOLDER
                + File.separator + "documentLongTextJustify.svg")), true)) {
            out.println(SvgGenerator.generate(svgDocument));
        }

        checkContent(SvgGenerator.generate(svgDocument), "documentLongTextJustify.svg");
    }

    @Test
    public void documentLongTextJustifyByWidthTest() throws IOException {
        SvgDocument svgDocument = new SvgDocument(SvgDocument.DEFAULT_WIDTH, SvgDocument.DEFAULT_HEIGHT);
        final SvgText text = new SvgText(LONG_TEXT, 12, SvgDocument.DEFAULT_WIDTH / 2, SvgDocument.DEFAULT_HEIGHT / 2, 300);
        text.setTextAlign(TextAlign.JUSTIFY);
        text.setMaxLineLength(80);
        svgDocument.addElement(text);

        try (PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(OUTPUT_FOLDER
                + File.separator + "documentLongTextJustifyByWidth.svg")), true)) {
            out.println(SvgGenerator.generate(svgDocument));
        }

        checkContent(SvgGenerator.generate(svgDocument), "documentLongTextJustifyByWidth.svg");
    }

    @Test
    public void documentLongTextJustifyRotatedTest() throws IOException {
        SvgDocument svgDocument = new SvgDocument(SvgDocument.DEFAULT_WIDTH, SvgDocument.DEFAULT_HEIGHT);
        final SvgText text = new SvgText(LONG_TEXT, 12, SvgDocument.DEFAULT_WIDTH / 2, SvgDocument.DEFAULT_HEIGHT / 2);
        text.setTextAlign(TextAlign.JUSTIFY);
        text.setMaxLineLength(80);
        text.setRotate(90);
        svgDocument.addElement(text);

        try (PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(OUTPUT_FOLDER
                + File.separator + "documentLongTextJustifyRotated.svg")), true)) {
            out.println(SvgGenerator.generate(svgDocument));
        }

        checkContent(SvgGenerator.generate(svgDocument), "documentLongTextJustifyRotated.svg");
    }

    @Test
    public void documentSimpleTextVariantTest() throws IOException {
        SvgDocument svgDocument = new SvgDocument(SvgDocument.DEFAULT_WIDTH, SvgDocument.DEFAULT_HEIGHT);
        SvgText text = new SvgText("This is the normal text", 12, SvgDocument.DEFAULT_WIDTH / 2, SvgDocument.DEFAULT_HEIGHT / 2);
        text.setFontVariant(FontVariantType.NORMAL);
        svgDocument.addElement(text);

        text = new SvgText("This is the Small Caps text", 12, SvgDocument.DEFAULT_WIDTH / 2, SvgDocument.DEFAULT_HEIGHT / 2 + 50);
        text.setFontVariant(FontVariantType.SMALL_CAPS);
        svgDocument.addElement(text);

        text = new SvgText("This is the Unicase text", 12, SvgDocument.DEFAULT_WIDTH / 2, SvgDocument.DEFAULT_HEIGHT / 2 + 100);
        text.setFontVariant(FontVariantType.UNICASE);
        svgDocument.addElement(text);

        try (PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(OUTPUT_FOLDER
                + File.separator + "documentSimpleTextVariant.svg")), true)) {
            out.println(SvgGenerator.generate(svgDocument));
        }

        checkContent(SvgGenerator.generate(svgDocument), "documentSimpleTextVariant.svg");
    }

    @Test
    public void documentColoredTest() throws IOException {
        SvgDocument svgDocument = new SvgDocument(SvgDocument.DEFAULT_WIDTH, SvgDocument.DEFAULT_HEIGHT);
        final SvgText text = new SvgText("This is the first text", 12, SvgDocument.DEFAULT_WIDTH / 2, SvgDocument.DEFAULT_HEIGHT / 2);
        text.getElementAttributes().setFill("ff0000");
        text.setStrokeColor("0000ff");
        text.setStrokeWidth(0.2);
        text.setStrokeDash(Arrays.asList(5, 5, 10, 10, 1));
        svgDocument.addElement(text);

        try (PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(OUTPUT_FOLDER
                + File.separator + "documentColoredText.svg")), true)) {
            out.println(SvgGenerator.generate(svgDocument));
        }

        checkContent(SvgGenerator.generate(svgDocument), "documentColoredText.svg");
    }

    @Test
    public void documentScriptTest() throws IOException {
        SvgDocument svgDocument = new SvgDocument();
        svgDocument.addElement(new SvgCircle(SvgDocument.DEFAULT_WIDTH / 2, SvgDocument.DEFAULT_HEIGHT / 2,
                SvgDocument.DEFAULT_WIDTH / 2));

        svgDocument.addElement(new SvgScript(SCRIPT));

        try (PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(OUTPUT_FOLDER
                + File.separator + "documentScript.svg")), true)) {
            out.println(SvgGenerator.generate(svgDocument));
        }

        checkContent(SvgGenerator.generate(svgDocument), "documentScript.svg");
    }

    @AfterClass(enabled = false)
    public void removeFolder() {
        Assert.assertTrue(deleteDirectory(new File(OUTPUT_FOLDER)));
    }
}
