package com.biit.infographic.core.svg;

import com.biit.infographic.core.generators.SvgGenerator;
import com.biit.infographic.core.models.svg.ElementAttributes;
import com.biit.infographic.core.models.svg.SvgBackground;
import com.biit.infographic.core.models.svg.SvgTemplate;
import com.biit.infographic.core.models.svg.components.Point;
import com.biit.infographic.core.models.svg.components.StrokeLineCap;
import com.biit.infographic.core.models.svg.components.SvgCircle;
import com.biit.infographic.core.models.svg.components.SvgEllipse;
import com.biit.infographic.core.models.svg.components.SvgImage;
import com.biit.infographic.core.models.svg.components.SvgLine;
import com.biit.infographic.core.models.svg.components.SvgPath;
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
public class SimpleSvgGenerationTest extends  SvgGeneration {
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



    @Test
    public void documentBackgroundColorTest() throws IOException {
        SvgTemplate svgTemplate = new SvgTemplate();
        svgTemplate.setSvgBackground(new SvgBackground().backgroundColor("#449911"));

        try (PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(OUTPUT_FOLDER
                + File.separator + "documentBackgroundColor.svg")), true)) {
            out.println(SvgGenerator.generate(svgTemplate));
        }
        checkContent(SvgGenerator.generate(svgTemplate), "documentBackgroundColor.svg");
    }

    @Test
    public void documentBackgroundImageTest() throws IOException {
        SvgTemplate svgTemplate = new SvgTemplate();
        svgTemplate.setSvgBackground(new SvgBackground().image(readBase64Image("EliseNess.txt")));

        try (PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(OUTPUT_FOLDER
                + File.separator + "documentBackgroundImage.svg")), true)) {
            out.println(SvgGenerator.generate(svgTemplate));
        }
        checkContent(SvgGenerator.generate(svgTemplate), "documentBackgroundImage.svg");
    }

    @Test
    public void documentImageTest() throws IOException {
        SvgTemplate svgTemplate = new SvgTemplate();
        svgTemplate.addElement(new SvgImage(new ElementAttributes(SvgTemplate.DEFAULT_WIDTH / 2, SvgTemplate.DEFAULT_HEIGHT / 2,
                String.valueOf(SvgTemplate.DEFAULT_WIDTH / 2), String.valueOf(SvgTemplate.DEFAULT_HEIGHT / 2)), "EliseNess",
                readBase64Image("EliseNess.txt")));

        try (PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(OUTPUT_FOLDER
                + File.separator + "documentImage.svg")), true)) {
            out.println(SvgGenerator.generate(svgTemplate));
        }
        checkContent(SvgGenerator.generate(svgTemplate), "documentImage.svg");
    }

    @Test
    public void documentDrawRectangleTest() throws IOException {
        SvgTemplate svgTemplate = new SvgTemplate();
        svgTemplate.addElement(new SvgRectangle(SvgTemplate.DEFAULT_WIDTH / 2, SvgTemplate.DEFAULT_HEIGHT / 2,
                String.valueOf(SvgTemplate.DEFAULT_WIDTH / 2), String.valueOf(SvgTemplate.DEFAULT_HEIGHT / 2), "ff0000"));

        try (PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(OUTPUT_FOLDER
                + File.separator + "documentDrawRectangle.svg")), true)) {
            out.println(SvgGenerator.generate(svgTemplate));
        }
        checkContent(SvgGenerator.generate(svgTemplate), "documentDrawRectangle.svg");
    }

    @Test
    public void documentDrawRectangleWithRadiusTest() throws IOException {
        SvgTemplate svgTemplate = new SvgTemplate();
        final SvgRectangle rectangle = new SvgRectangle(SvgTemplate.DEFAULT_WIDTH / 2, SvgTemplate.DEFAULT_HEIGHT / 2,
                String.valueOf(SvgTemplate.DEFAULT_WIDTH / 2), String.valueOf(SvgTemplate.DEFAULT_HEIGHT / 2), "ff0000");
        rectangle.setXRadius(25L);
        rectangle.setXRadius(50L);
        svgTemplate.addElement(rectangle);

        try (PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(OUTPUT_FOLDER
                + File.separator + "documentDrawRectangleWithRadius.svg")), true)) {
            out.println(SvgGenerator.generate(svgTemplate));
        }
        checkContent(SvgGenerator.generate(svgTemplate), "documentDrawRectangleWithRadius.svg");
    }

    @Test(expectedExceptions = InvalidAttributeException.class)
    public void documentDrawInvalidRectangleTest() {
        SvgTemplate svgTemplate = new SvgTemplate();
        svgTemplate.addElement(new SvgRectangle(SvgTemplate.DEFAULT_WIDTH / 2, SvgTemplate.DEFAULT_HEIGHT / 2,
                null, String.valueOf(SvgTemplate.DEFAULT_HEIGHT / 2), "ff0000"));

        SvgGenerator.generate(svgTemplate);
    }

    @Test
    public void documentDrawCircleTest() throws IOException {
        SvgTemplate svgTemplate = new SvgTemplate();
        svgTemplate.addElement(new SvgCircle(SvgTemplate.DEFAULT_WIDTH / 2, SvgTemplate.DEFAULT_HEIGHT / 2,
                SvgTemplate.DEFAULT_WIDTH / 2));

        try (PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(OUTPUT_FOLDER
                + File.separator + "documentDrawCircle.svg")), true)) {
            out.println(SvgGenerator.generate(svgTemplate));
        }

        checkContent(SvgGenerator.generate(svgTemplate), "documentDrawCircle.svg");
    }

    @Test(expectedExceptions = InvalidAttributeException.class)
    public void documentDrawInvalidCircleTest() throws IOException {
        SvgTemplate svgTemplate = new SvgTemplate();
        svgTemplate.addElement(new SvgCircle(SvgTemplate.DEFAULT_WIDTH / 2, SvgTemplate.DEFAULT_HEIGHT / 2,
                0L));

        SvgGenerator.generate(svgTemplate);
    }

    @Test
    public void documentDrawCircleOutsideTest() throws IOException {
        SvgTemplate svgTemplate = new SvgTemplate();
        svgTemplate.addElement(new SvgCircle(SvgTemplate.DEFAULT_WIDTH / 2 - 100, SvgTemplate.DEFAULT_HEIGHT / 2 - 100,
                SvgTemplate.DEFAULT_HEIGHT));

        try (PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(OUTPUT_FOLDER
                + File.separator + "documentDrawCircleOutside.svg")), true)) {
            out.println(SvgGenerator.generate(svgTemplate));
        }

        checkContent(SvgGenerator.generate(svgTemplate), "documentDrawCircleOutside.svg");
    }

    @Test
    public void documentDrawEllipseTest() throws IOException {
        SvgTemplate svgTemplate = new SvgTemplate();
        final SvgEllipse ellipse = new SvgEllipse(SvgTemplate.DEFAULT_WIDTH / 2, SvgTemplate.DEFAULT_HEIGHT / 2,
                String.valueOf(SvgTemplate.DEFAULT_WIDTH / 4), String.valueOf(SvgTemplate.DEFAULT_HEIGHT / 2)
                , "ff00ff");
        svgTemplate.addElement(ellipse);

        try (PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(OUTPUT_FOLDER
                + File.separator + "documentDrawEllipse.svg")), true)) {
            out.println(SvgGenerator.generate(svgTemplate));
        }

        checkContent(SvgGenerator.generate(svgTemplate), "documentDrawEllipse.svg");
    }

    @Test
    public void documentDrawEllipseOutsideTest() throws IOException {
        SvgTemplate svgTemplate = new SvgTemplate();
        final SvgEllipse ellipse = new SvgEllipse(SvgTemplate.DEFAULT_WIDTH, SvgTemplate.DEFAULT_HEIGHT,
                String.valueOf(SvgTemplate.DEFAULT_WIDTH / 4), String.valueOf(SvgTemplate.DEFAULT_HEIGHT / 2)
                , "ff00ff");
        svgTemplate.addElement(ellipse);

        try (PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(OUTPUT_FOLDER
                + File.separator + "documentDrawEllipseOutside.svg")), true)) {
            out.println(SvgGenerator.generate(svgTemplate));
        }

        checkContent(SvgGenerator.generate(svgTemplate), "documentDrawEllipseOutside.svg");
    }

    @Test
    public void documentDrawLineTest() throws IOException {
        SvgTemplate svgTemplate = new SvgTemplate();
        final SvgLine line = new SvgLine(50L, 50L, SvgTemplate.DEFAULT_WIDTH, SvgTemplate.DEFAULT_HEIGHT);
        svgTemplate.addElement(line);

        try (PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(OUTPUT_FOLDER
                + File.separator + "documentDrawLine.svg")), true)) {
            out.println(SvgGenerator.generate(svgTemplate));
        }

        checkContent(SvgGenerator.generate(svgTemplate), "documentDrawLine.svg");
    }

    @Test
    public void documentDrawLineOutsideTest() throws IOException {
        SvgTemplate svgTemplate = new SvgTemplate();
        final SvgLine line = new SvgLine(50L, 50L, SvgTemplate.DEFAULT_WIDTH + 100, SvgTemplate.DEFAULT_HEIGHT + 50);
        svgTemplate.addElement(line);

        try (PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(OUTPUT_FOLDER
                + File.separator + "documentDrawLineOutside.svg")), true)) {
            out.println(SvgGenerator.generate(svgTemplate));
        }

        checkContent(SvgGenerator.generate(svgTemplate), "documentDrawLineOutside.svg");
    }

    @Test
    public void documentDrawLineStrokeTest() throws IOException {
        SvgTemplate svgTemplate = new SvgTemplate();
        final SvgLine line = new SvgLine(50L, 50L, SvgTemplate.DEFAULT_WIDTH, SvgTemplate.DEFAULT_HEIGHT);
        line.getElementStroke().setLineCap(StrokeLineCap.ROUND);
        line.getElementStroke().setStrokeDash(Arrays.asList(5, 5, 10, 10, 1));
        svgTemplate.addElement(line);

        try (PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(OUTPUT_FOLDER
                + File.separator + "documentDrawLineStroke.svg")), true)) {
            out.println(SvgGenerator.generate(svgTemplate));
        }

        checkContent(SvgGenerator.generate(svgTemplate), "documentDrawLineStroke.svg");
    }

    @Test
    public void documentSimpleTextTest() throws IOException {
        SvgTemplate svgTemplate = new SvgTemplate(SvgTemplate.DEFAULT_WIDTH, SvgTemplate.DEFAULT_HEIGHT);
        final SvgText text = new SvgText("This is the first text", 12, SvgTemplate.DEFAULT_WIDTH / 2, SvgTemplate.DEFAULT_HEIGHT / 2);
        svgTemplate.addElement(text);

        try (PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(OUTPUT_FOLDER
                + File.separator + "documentSimpleText.svg")), true)) {
            out.println(SvgGenerator.generate(svgTemplate));
        }

        checkContent(SvgGenerator.generate(svgTemplate), "documentSimpleText.svg");
    }

    @Test
    public void documentLongTextLimitedLineTest() throws IOException {
        SvgTemplate svgTemplate = new SvgTemplate(SvgTemplate.DEFAULT_WIDTH, SvgTemplate.DEFAULT_HEIGHT);
        final SvgText text = new SvgText(LONG_TEXT, 12, SvgTemplate.DEFAULT_WIDTH / 2, SvgTemplate.DEFAULT_HEIGHT / 2);
        text.setLengthAdjust(FontLengthAdjust.SPACING);
        text.setMaxLineLength(80);
        svgTemplate.addElement(text);

        try (PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(OUTPUT_FOLDER
                + File.separator + "documentLongTextLimitedLine.svg")), true)) {
            out.println(SvgGenerator.generate(svgTemplate));
        }

        checkContent(SvgGenerator.generate(svgTemplate), "documentLongTextLimitedLine.svg");
    }

    @Test
    public void documentLongTextLimitedLineForcingLengthTest() throws IOException {
        SvgTemplate svgTemplate = new SvgTemplate(SvgTemplate.DEFAULT_WIDTH, SvgTemplate.DEFAULT_HEIGHT);
        final SvgText text = new SvgText(LONG_TEXT, 12, SvgTemplate.DEFAULT_WIDTH / 2, SvgTemplate.DEFAULT_HEIGHT / 2);
        text.setTextLength("8000");
        text.setLengthAdjust(FontLengthAdjust.SPACING);
        text.setMaxLineLength(80);
        svgTemplate.addElement(text);

        try (PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(OUTPUT_FOLDER
                + File.separator + "documentLongTextLimitedLineForcingLength.svg")), true)) {
            out.println(SvgGenerator.generate(svgTemplate));
        }

        checkContent(SvgGenerator.generate(svgTemplate), "documentLongTextLimitedLineForcingLength.svg");
    }

    @Test
    public void documentLongTextAlignRightTest() throws IOException {
        SvgTemplate svgTemplate = new SvgTemplate(SvgTemplate.DEFAULT_WIDTH, SvgTemplate.DEFAULT_HEIGHT);
        final SvgText text = new SvgText(LONG_TEXT, 12, SvgTemplate.DEFAULT_WIDTH / 2, SvgTemplate.DEFAULT_HEIGHT / 2);
        text.setTextAlign(TextAlign.RIGHT);
        text.setMaxLineLength(80);
        svgTemplate.addElement(text);

        try (PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(OUTPUT_FOLDER
                + File.separator + "documentLongTextAlignRight.svg")), true)) {
            out.println(SvgGenerator.generate(svgTemplate));
        }
    }

    @Test
    public void documentLongTextJustifyTest() throws IOException {
        SvgTemplate svgTemplate = new SvgTemplate(SvgTemplate.DEFAULT_WIDTH, SvgTemplate.DEFAULT_HEIGHT);
        final SvgText text = new SvgText(LONG_TEXT, 12, SvgTemplate.DEFAULT_WIDTH / 2, SvgTemplate.DEFAULT_HEIGHT / 2);
        text.setTextAlign(TextAlign.JUSTIFY);
        text.setMaxLineLength(80);
        svgTemplate.addElement(text);

        try (PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(OUTPUT_FOLDER
                + File.separator + "documentLongTextJustify.svg")), true)) {
            out.println(SvgGenerator.generate(svgTemplate));
        }

        checkContent(SvgGenerator.generate(svgTemplate), "documentLongTextJustify.svg");
    }

    @Test
    public void documentLongTextJustifyByWidthTest() throws IOException {
        SvgTemplate svgTemplate = new SvgTemplate(SvgTemplate.DEFAULT_WIDTH, SvgTemplate.DEFAULT_HEIGHT);
        final SvgText text = new SvgText(LONG_TEXT, 12, SvgTemplate.DEFAULT_WIDTH / 2, SvgTemplate.DEFAULT_HEIGHT / 2);
        text.setTextAlign(TextAlign.JUSTIFY);
        text.setMaxLineWidth(300);
        text.setMaxLineLength(80);
        svgTemplate.addElement(text);

        try (PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(OUTPUT_FOLDER
                + File.separator + "documentLongTextJustifyByWidth.svg")), true)) {
            out.println(SvgGenerator.generate(svgTemplate));
        }

        checkContent(SvgGenerator.generate(svgTemplate), "documentLongTextJustifyByWidth.svg");
    }

    @Test
    public void documentLongTextJustifyRotatedTest() throws IOException {
        SvgTemplate svgTemplate = new SvgTemplate(SvgTemplate.DEFAULT_WIDTH, SvgTemplate.DEFAULT_HEIGHT);
        final SvgText text = new SvgText(LONG_TEXT, 12, SvgTemplate.DEFAULT_WIDTH / 2, SvgTemplate.DEFAULT_HEIGHT / 2);
        text.setTextAlign(TextAlign.JUSTIFY);
        text.setMaxLineLength(80);
        text.setRotate(90L);
        svgTemplate.addElement(text);

        try (PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(OUTPUT_FOLDER
                + File.separator + "documentLongTextJustifyRotated.svg")), true)) {
            out.println(SvgGenerator.generate(svgTemplate));
        }

        checkContent(SvgGenerator.generate(svgTemplate), "documentLongTextJustifyRotated.svg");
    }

    @Test
    public void documentSimpleTextVariantTest() throws IOException {
        SvgTemplate svgTemplate = new SvgTemplate(SvgTemplate.DEFAULT_WIDTH, SvgTemplate.DEFAULT_HEIGHT);
        SvgText text = new SvgText("This is the normal text", 12, SvgTemplate.DEFAULT_WIDTH / 2, SvgTemplate.DEFAULT_HEIGHT / 2);
        text.setFontVariant(FontVariantType.NORMAL);
        svgTemplate.addElement(text);

        text = new SvgText("This is the Small Caps text", 12, SvgTemplate.DEFAULT_WIDTH / 2, SvgTemplate.DEFAULT_HEIGHT / 2 + 50);
        text.setFontVariant(FontVariantType.SMALL_CAPS);
        svgTemplate.addElement(text);

        text = new SvgText("This is the Unicase text", 12, SvgTemplate.DEFAULT_WIDTH / 2, SvgTemplate.DEFAULT_HEIGHT / 2 + 100);
        text.setFontVariant(FontVariantType.UNICASE);
        svgTemplate.addElement(text);

        try (PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(OUTPUT_FOLDER
                + File.separator + "documentSimpleTextVariant.svg")), true)) {
            out.println(SvgGenerator.generate(svgTemplate));
        }

        checkContent(SvgGenerator.generate(svgTemplate), "documentSimpleTextVariant.svg");
    }

    @Test
    public void documentColoredTest() throws IOException {
        SvgTemplate svgTemplate = new SvgTemplate(SvgTemplate.DEFAULT_WIDTH, SvgTemplate.DEFAULT_HEIGHT);
        final SvgText text = new SvgText("This is the first text", 12, SvgTemplate.DEFAULT_WIDTH / 2, SvgTemplate.DEFAULT_HEIGHT / 2);
        text.getElementAttributes().setFill("ff0000");
        text.getElementStroke().setStrokeColor("0000ff");
        text.getElementStroke().setStrokeWidth(0.2);
        text.getElementStroke().setStrokeDash(Arrays.asList(5, 5, 10, 10, 1));
        svgTemplate.addElement(text);

        try (PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(OUTPUT_FOLDER
                + File.separator + "documentColoredText.svg")), true)) {
            out.println(SvgGenerator.generate(svgTemplate));
        }

        checkContent(SvgGenerator.generate(svgTemplate), "documentColoredText.svg");
    }

    @Test
    public void documentScriptTest() throws IOException {
        SvgTemplate svgTemplate = new SvgTemplate();
        svgTemplate.addElement(new SvgCircle(SvgTemplate.DEFAULT_WIDTH / 2, SvgTemplate.DEFAULT_HEIGHT / 2,
                SvgTemplate.DEFAULT_WIDTH / 2));

        svgTemplate.addElement(new SvgScript(SCRIPT));

        try (PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(OUTPUT_FOLDER
                + File.separator + "documentScript.svg")), true)) {
            out.println(SvgGenerator.generate(svgTemplate));
        }

        checkContent(SvgGenerator.generate(svgTemplate), "documentScript.svg");
    }

    @Test
    public void nestedDocumentsTest() throws IOException {
        SvgTemplate svgTemplate = new SvgTemplate();

        SvgTemplate childDocument1 = new SvgTemplate();
        childDocument1.addElement(new SvgCircle(SvgTemplate.DEFAULT_WIDTH / 2, SvgTemplate.DEFAULT_HEIGHT / 2,
                SvgTemplate.DEFAULT_WIDTH / 2));
        svgTemplate.addElement(childDocument1);

        SvgTemplate childDocument2 = new SvgTemplate();
        childDocument2.addElement(new SvgRectangle(SvgTemplate.DEFAULT_WIDTH / 2, SvgTemplate.DEFAULT_HEIGHT / 2,
                String.valueOf(SvgTemplate.DEFAULT_WIDTH / 2), String.valueOf(SvgTemplate.DEFAULT_HEIGHT / 2), "ff0000"));
        svgTemplate.addElement(childDocument2);

        try (PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(OUTPUT_FOLDER
                + File.separator + "nestedDocuments.svg")), true)) {
            out.println(SvgGenerator.generate(svgTemplate));
        }

        checkContent(SvgGenerator.generate(svgTemplate), "nestedDocuments.svg");
    }

    @Test
    public void documentDrawPathTest() throws IOException {
        SvgTemplate svgTemplate = new SvgTemplate();
        final SvgPath line = new SvgPath(0L, 0L, new Point(50L, 50L), new Point(100L, 0L), new Point(200L, 150L));
        line.getElementStroke().setLineCap(StrokeLineCap.ROUND);
        line.getElementStroke().setStrokeDash(Arrays.asList(5, 5, 10, 10, 1));
        svgTemplate.addElement(line);

        try (PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(OUTPUT_FOLDER
                + File.separator + "documentDrawPath.svg")), true)) {
            out.println(SvgGenerator.generate(svgTemplate));
        }

        checkContent(SvgGenerator.generate(svgTemplate), "documentDrawPath.svg");
    }

    @Test
    public void documentDrawPathAreaFilledTest() throws IOException {
        SvgTemplate svgTemplate = new SvgTemplate();
        final SvgPath area = new SvgPath(0L, 0L, new Point(50L, 50L), new Point(100L, 0L), new Point(200L, 150L));
        area.getElementStroke().setLineCap(StrokeLineCap.ROUND);
        area.getElementStroke().setStrokeDash(Arrays.asList(5, 5, 10, 10, 1));
        area.getElementAttributes().setFill("ff00ff");
        svgTemplate.addElement(area);

        try (PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(OUTPUT_FOLDER
                + File.separator + "documentDrawPath.svg")), true)) {
            out.println(SvgGenerator.generate(svgTemplate));
        }

        checkContent(SvgGenerator.generate(svgTemplate), "documentDrawPathAreaFilled.svg");
    }
}
