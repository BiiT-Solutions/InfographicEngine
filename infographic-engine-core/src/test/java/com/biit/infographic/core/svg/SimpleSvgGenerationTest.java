package com.biit.infographic.core.svg;

import com.biit.infographic.core.generators.SvgGenerator;
import com.biit.infographic.core.models.svg.ElementAttributes;
import com.biit.infographic.core.models.svg.StrokeAlign;
import com.biit.infographic.core.models.svg.SvgBackground;
import com.biit.infographic.core.models.svg.SvgEmbedded;
import com.biit.infographic.core.models.svg.SvgTemplate;
import com.biit.infographic.core.models.svg.components.StrokeLineCap;
import com.biit.infographic.core.models.svg.components.SvgCircle;
import com.biit.infographic.core.models.svg.components.SvgCircleSector;
import com.biit.infographic.core.models.svg.components.SvgEllipse;
import com.biit.infographic.core.models.svg.components.SvgImage;
import com.biit.infographic.core.models.svg.components.SvgLine;
import com.biit.infographic.core.models.svg.components.SvgPath;
import com.biit.infographic.core.models.svg.components.SvgRectangle;
import com.biit.infographic.core.models.svg.components.SvgRectangleSector;
import com.biit.infographic.core.models.svg.components.SvgScript;
import com.biit.infographic.core.models.svg.components.gradient.SvgGradient;
import com.biit.infographic.core.models.svg.components.gradient.SvgGradientStop;
import com.biit.infographic.core.models.svg.components.path.Arc;
import com.biit.infographic.core.models.svg.components.path.BezierCurve;
import com.biit.infographic.core.models.svg.components.path.HorizontalLine;
import com.biit.infographic.core.models.svg.components.path.Point;
import com.biit.infographic.core.models.svg.components.path.VerticalLine;
import com.biit.infographic.core.models.svg.components.text.SvgText;
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
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;

@Test(groups = {"simpleSvgGenerationTest"})
public class SimpleSvgGenerationTest extends SvgGeneration {
    private static final String OUTPUT_FOLDER = System.getProperty("java.io.tmpdir") + File.separator + "SvgTests";
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


    @BeforeClass
    public void prepareFolder() throws IOException {
        Files.createDirectories(Paths.get(OUTPUT_FOLDER));
    }


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
    public void documentDrawRectanglesWithGradientTest() throws IOException {
        SvgTemplate svgTemplate = new SvgTemplate();
        final SvgRectangle svgRectangle1 = new SvgRectangle(SvgTemplate.DEFAULT_WIDTH / 2, SvgTemplate.DEFAULT_HEIGHT / 2,
                String.valueOf(SvgTemplate.DEFAULT_WIDTH / 2), String.valueOf(SvgTemplate.DEFAULT_HEIGHT / 2), null);
        svgRectangle1.setGradient(new SvgGradient(new SvgGradientStop("#ff0000", 1.0, 0.0), new SvgGradientStop("#0000ff", 1.0, 1.0)));
        svgTemplate.addElement(svgRectangle1);

        final SvgRectangle svgRectangle2 = new SvgRectangle(0L, 0L,
                String.valueOf(SvgTemplate.DEFAULT_WIDTH / 2), String.valueOf(SvgTemplate.DEFAULT_HEIGHT / 2), null);
        svgRectangle2.setGradient(new SvgGradient(new SvgGradientStop("#00ff00", 1.0, 0.0), new SvgGradientStop("#ff00ff", 1.0, 1.0)));
        svgTemplate.addElement(svgRectangle2);

        try (PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(OUTPUT_FOLDER
                + File.separator + "documentDrawRectanglesWithGradient.svg")), true)) {
            out.println(SvgGenerator.generate(svgTemplate));
        }
        checkContent(SvgGenerator.generate(svgTemplate), "documentDrawRectanglesWithGradient.svg");
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
                + File.separator + "documentDrawPathAreaFilled.svg")), true)) {
            out.println(SvgGenerator.generate(svgTemplate));
        }

        checkContent(SvgGenerator.generate(svgTemplate), "documentDrawPathAreaFilled.svg");
    }

    @Test
    public void importExternalSvg() throws IOException {
        SvgTemplate svgTemplate = new SvgTemplate();

        SvgEmbedded svgEmbedded = new SvgEmbedded("images/CADT/Action.svg");
        svgEmbedded.getElementAttributes().setXCoordinate(50L);
        svgEmbedded.getElementAttributes().setYCoordinate(50L);
        svgEmbedded.getElementAttributes().setWidth(55L);
        svgEmbedded.getElementAttributes().setHeight(35L);
        svgTemplate.addElement(svgEmbedded);

        try (PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(OUTPUT_FOLDER
                + File.separator + "documentExternalSvg.svg")), true)) {
            out.println(SvgGenerator.generate(svgTemplate));
        }

        checkContent(SvgGenerator.generate(svgTemplate), "documentExternalSvg.svg");
    }

    @Test
    public void documentDrawPathMixingLinesAndArcs() throws IOException {
        SvgTemplate svgTemplate = new SvgTemplate();
        final SvgPath line = new SvgPath(0L, 0L, new Arc(25L, 25L), new Point(50L, 50L), new Point(100L, 0L),
                new Point(200L, 150L), new Arc(0L, 80L));
        svgTemplate.addElement(line);

        try (PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(OUTPUT_FOLDER
                + File.separator + "documentDrawPathMixingLinesAndArcs.svg")), true)) {
            out.println(SvgGenerator.generate(svgTemplate));
        }

        checkContent(SvgGenerator.generate(svgTemplate), "documentDrawPathMixingLinesAndArcs.svg");
    }

    @Test
    public void documentDrawPathWithBezierCurves() throws IOException {
        SvgTemplate svgTemplate = new SvgTemplate();
        svgTemplate.addElement(new SvgPath(43, 35,
                new VerticalLine(3),
                new BezierCurve(8, 8, 0, 5, 3, 8),
                new HorizontalLine(30)));


        try (PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(OUTPUT_FOLDER
                + File.separator + "documentDrawPathWithBezierCurves.svg")), true)) {
            out.println(SvgGenerator.generate(svgTemplate));
        }

    }

    @Test
    public void documentDrawPathCircle() throws IOException {
        SvgTemplate svgTemplate = new SvgTemplate();
        final SvgPath line = new SvgPath(50L, 50L, new Arc(100L, 100L), new Arc(150L, 50L), new Arc(100L, 0L), new Arc(50L, 50L));
        svgTemplate.addElement(line);

        try (PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(OUTPUT_FOLDER
                + File.separator + "documentDrawPathCircle.svg")), true)) {
            out.println(SvgGenerator.generate(svgTemplate));
        }

        checkContent(SvgGenerator.generate(svgTemplate), "documentDrawPathCircle.svg");
    }

    @Test
    public void outerBorderTest() throws IOException {
        SvgTemplate svgTemplate = new SvgTemplate();
        final SvgCircle svgCircle = new SvgCircle(SvgTemplate.DEFAULT_WIDTH / 8, SvgTemplate.DEFAULT_HEIGHT / 8,
                SvgTemplate.DEFAULT_WIDTH / 8);
        svgCircle.getElementAttributes().setFill("00ff00");
        svgCircle.getElementStroke().setStrokeColor("ff00ff");
        svgCircle.getElementStroke().setStrokeWidth(4D);
        svgCircle.getElementStroke().setStrokeOpacity(0.5);
        svgCircle.getElementStroke().setStrokeAlign(StrokeAlign.OUTSET);
        svgTemplate.addElement(svgCircle);

        final SvgRectangle svgRectangle = new SvgRectangle(3 * SvgTemplate.DEFAULT_WIDTH / 4, SvgTemplate.DEFAULT_HEIGHT / 4,
                SvgTemplate.DEFAULT_WIDTH / 4, SvgTemplate.DEFAULT_WIDTH / 4);
        svgRectangle.getElementAttributes().setFill("00ff00");
        svgRectangle.getElementStroke().setStrokeColor("ff00ff");
        svgRectangle.getElementStroke().setStrokeWidth(4D);
        svgRectangle.getElementStroke().setStrokeOpacity(0.5);
        svgRectangle.getElementStroke().setStrokeAlign(StrokeAlign.OUTSET);
        svgTemplate.addElement(svgRectangle);

        try (PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(OUTPUT_FOLDER
                + File.separator + "outerBorder.svg")), true)) {
            out.println(SvgGenerator.generate(svgTemplate));
        }

        checkContent(SvgGenerator.generate(svgTemplate), "outerBorder.svg");
    }

    @Test
    public void outerBorderWithGradientTest() throws IOException {
        SvgTemplate svgTemplate = new SvgTemplate();

        final SvgCircle svgCircle = new SvgCircle(SvgTemplate.DEFAULT_WIDTH / 8, SvgTemplate.DEFAULT_HEIGHT / 8,
                SvgTemplate.DEFAULT_WIDTH / 8);
        svgCircle.getElementAttributes().setFill("00ff00");
        svgCircle.getElementStroke().setStrokeWidth(4D);
        svgCircle.getElementStroke().setGradient(new SvgGradient(svgCircle.getElementAttributes().getXCoordinate(),
                svgCircle.getElementAttributes().getYCoordinate(), svgCircle.getElementAttributes().getXCoordinate() + svgCircle.getRadius() * 2,
                svgCircle.getElementAttributes().getYCoordinate(), new SvgGradientStop("ff0000", 0.5, 0.0), new SvgGradientStop("0000ff", 0.5, 1.0)));
        svgCircle.getElementStroke().setStrokeAlign(StrokeAlign.OUTSET);
        svgTemplate.addElement(svgCircle);

        final SvgRectangle svgRectangle = new SvgRectangle(3 * SvgTemplate.DEFAULT_WIDTH / 4, SvgTemplate.DEFAULT_HEIGHT / 4,
                SvgTemplate.DEFAULT_WIDTH / 4, SvgTemplate.DEFAULT_WIDTH / 4);
        svgRectangle.getElementAttributes().setFill("00ff00");
        svgRectangle.getElementStroke().setStrokeWidth(4D);
        svgRectangle.getElementStroke().setGradient(
                new SvgGradient(svgRectangle.getElementAttributes().getXCoordinate(),
                        svgRectangle.getElementAttributes().getYCoordinate(), svgRectangle.getElementAttributes().getXCoordinate() + svgRectangle.getElementAttributes().getWidth(),
                        svgRectangle.getElementAttributes().getYCoordinate(),
                        new SvgGradientStop("ff0000", 0.5, 0.0), new SvgGradientStop("0000ff", 0.5, 1.0)));
        svgRectangle.getElementStroke().setStrokeAlign(StrokeAlign.OUTSET);
        svgTemplate.addElement(svgRectangle);

        try (PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(OUTPUT_FOLDER
                + File.separator + "outerBorderWithGradient.svg")), true)) {
            out.println(SvgGenerator.generate(svgTemplate));
        }

        checkContent(SvgGenerator.generate(svgTemplate), "outerBorderWithGradient.svg");
    }

    @Test
    public void documentDrawRectangleWithLinkHoverTest() throws IOException {
        SvgTemplate svgTemplate = new SvgTemplate();
        final SvgRectangle rectangle = new SvgRectangle(SvgTemplate.DEFAULT_WIDTH / 2, SvgTemplate.DEFAULT_HEIGHT / 2,
                String.valueOf(SvgTemplate.DEFAULT_WIDTH / 2), String.valueOf(SvgTemplate.DEFAULT_HEIGHT / 2), "ff0000");
        rectangle.setLink("https://google.es");
        rectangle.getLink().getFillAttributes().setHoverFillColor("00ff00");
        svgTemplate.addElement(rectangle);

        try (PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(OUTPUT_FOLDER
                + File.separator + "documentDrawRectangleWithLinkHover.svg")), true)) {
            out.println(SvgGenerator.generate(svgTemplate));
        }
        checkContent(SvgGenerator.generate(svgTemplate), "documentDrawRectangleWithLinkHover.svg");
    }


    @Test
    public void documentDrawRectangleWithLinkHoverOpacityTest() throws IOException {
        SvgTemplate svgTemplate = new SvgTemplate();
        final SvgRectangle rectangle = new SvgRectangle(SvgTemplate.DEFAULT_WIDTH / 2, SvgTemplate.DEFAULT_HEIGHT / 2,
                String.valueOf(SvgTemplate.DEFAULT_WIDTH / 2), String.valueOf(SvgTemplate.DEFAULT_HEIGHT / 2), "ff0000");
        rectangle.setLink("https://google.es");
        rectangle.getLink().getFillAttributes().setHoverFillOpacity(0.5);
        svgTemplate.addElement(rectangle);

        try (PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(OUTPUT_FOLDER
                + File.separator + "documentDrawRectangleWithLinkHoverOpacity.svg")), true)) {
            out.println(SvgGenerator.generate(svgTemplate));
        }
        checkContent(SvgGenerator.generate(svgTemplate), "documentDrawRectangleWithLinkHoverOpacity.svg");
    }

    @Test
    public void documentDrawCircleSectorTest() throws IOException {
        SvgTemplate svgTemplate = new SvgTemplate();
        final SvgCircleSector svgCircleSector = new SvgCircleSector(SvgTemplate.DEFAULT_WIDTH / 2, SvgTemplate.DEFAULT_HEIGHT / 2, SvgTemplate.DEFAULT_HEIGHT / 2,
                20, 290);
        svgCircleSector.getElementAttributes().setFill("none");
        svgTemplate.addElement(svgCircleSector);

        try (PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(OUTPUT_FOLDER
                + File.separator + "documentDrawCircleSector.svg")), true)) {
            out.println(SvgGenerator.generate(svgTemplate));
        }
        checkContent(SvgGenerator.generate(svgTemplate), "documentDrawCircleSector.svg");
    }

    @Test
    public void documentDrawCircleSectorFilledTest() throws IOException {
        SvgTemplate svgTemplate = new SvgTemplate();
        final SvgCircleSector svgCircleSector = new SvgCircleSector(SvgTemplate.DEFAULT_WIDTH / 2, SvgTemplate.DEFAULT_HEIGHT / 2, SvgTemplate.DEFAULT_HEIGHT / 2,
                20, 290);
        svgCircleSector.getElementAttributes().setFill("red");
        svgTemplate.addElement(svgCircleSector);

        final SvgCircleSector svgCircleSector2 = new SvgCircleSector(SvgTemplate.DEFAULT_WIDTH / 2, SvgTemplate.DEFAULT_HEIGHT / 2, SvgTemplate.DEFAULT_HEIGHT / 2,
                290, 20);
        svgCircleSector2.getElementAttributes().setFill("green");
        svgTemplate.addElement(svgCircleSector2);

        try (PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(OUTPUT_FOLDER
                + File.separator + "documentDrawCircleSectorFilled.svg")), true)) {
            out.println(SvgGenerator.generate(svgTemplate));
        }
        checkContent(SvgGenerator.generate(svgTemplate), "documentDrawCircleSectorFilled.svg");
    }


    @Test
    public void documentDrawCircleSectorPercentageTest() throws IOException {
        SvgTemplate svgTemplate = new SvgTemplate();
        final SvgCircleSector svgCircleSector = new SvgCircleSector(SvgTemplate.DEFAULT_WIDTH / 4, SvgTemplate.DEFAULT_HEIGHT / 4, SvgTemplate.DEFAULT_HEIGHT / 4,
                0.75);
        svgCircleSector.getElementAttributes().setFill("red");
        svgTemplate.addElement(svgCircleSector);

        final SvgCircleSector svgCircleSector2 = new SvgCircleSector(SvgTemplate.DEFAULT_WIDTH * 0.75, SvgTemplate.DEFAULT_HEIGHT / 4, SvgTemplate.DEFAULT_HEIGHT / 4,
                1.0);
        svgCircleSector2.getElementAttributes().setFill("blue");
        svgTemplate.addElement(svgCircleSector2);

        try (PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(OUTPUT_FOLDER
                + File.separator + "documentDrawCircleSectorPercentage.svg")), true)) {
            out.println(SvgGenerator.generate(svgTemplate));
        }
        checkContent(SvgGenerator.generate(svgTemplate), "documentDrawCircleSectorPercentage.svg");
    }


    @Test
    public void documentDrawRectangleSectorFilledTest() throws IOException {
        SvgTemplate svgTemplate = new SvgTemplate();
        final SvgRectangleSector svgRectangleSector = new SvgRectangleSector(0, SvgTemplate.DEFAULT_HEIGHT / 2,
                SvgTemplate.DEFAULT_WIDTH / 2, SvgTemplate.DEFAULT_HEIGHT / 2,
                0, 240);
        svgRectangleSector.getElementAttributes().setFill("red");
        svgTemplate.addElement(svgRectangleSector);

        final SvgRectangleSector svgRectangleSector2 = new SvgRectangleSector(SvgTemplate.DEFAULT_HEIGHT / 2, SvgTemplate.DEFAULT_HEIGHT / 2,
                SvgTemplate.DEFAULT_WIDTH / 2, SvgTemplate.DEFAULT_HEIGHT / 2,
                60, 290);
        svgRectangleSector2.getElementAttributes().setFill("green");
        svgTemplate.addElement(svgRectangleSector2);

        final SvgRectangleSector svgRectangleSector3 = new SvgRectangleSector(0, SvgTemplate.DEFAULT_HEIGHT,
                SvgTemplate.DEFAULT_WIDTH / 2, SvgTemplate.DEFAULT_HEIGHT / 2,
                110, 190);
        svgRectangleSector3.getElementAttributes().setFill("blue");
        svgTemplate.addElement(svgRectangleSector3);

        final SvgRectangleSector svgRectangleSector4 = new SvgRectangleSector(SvgTemplate.DEFAULT_HEIGHT / 2, SvgTemplate.DEFAULT_HEIGHT,
                SvgTemplate.DEFAULT_WIDTH / 2, SvgTemplate.DEFAULT_HEIGHT / 2,
                0, 120);
        svgRectangleSector4.getElementAttributes().setFill("yellow");
        svgTemplate.addElement(svgRectangleSector4);

        final SvgRectangleSector svgRectangleSector5 = new SvgRectangleSector(0, SvgTemplate.DEFAULT_HEIGHT * 1.5,
                SvgTemplate.DEFAULT_WIDTH / 2, SvgTemplate.DEFAULT_HEIGHT / 2,
                0, 350);
        svgRectangleSector5.getElementAttributes().setFill("magenta");
        svgTemplate.addElement(svgRectangleSector5);

        final SvgRectangleSector svgRectangleSector6 = new SvgRectangleSector(SvgTemplate.DEFAULT_HEIGHT / 2, SvgTemplate.DEFAULT_HEIGHT * 1.5,
                SvgTemplate.DEFAULT_WIDTH / 2, SvgTemplate.DEFAULT_HEIGHT / 2,
                0, 360);
        svgRectangleSector6.getElementAttributes().setFill("orange");
        svgTemplate.addElement(svgRectangleSector6);

        try (PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(OUTPUT_FOLDER
                + File.separator + "documentDrawRectangleSectorFilled.svg")), true)) {
            out.println(SvgGenerator.generate(svgTemplate));
        }
        checkContent(SvgGenerator.generate(svgTemplate), "documentDrawRectangleSectorFilled.svg");
    }

    @Test
    public void compareCoordinatesRectangleAndSectionTest() throws IOException {
        SvgTemplate svgTemplate = new SvgTemplate();

        final SvgRectangle rectangle = new SvgRectangle(SvgTemplate.DEFAULT_WIDTH / 2, SvgTemplate.DEFAULT_HEIGHT / 2,
                String.valueOf(SvgTemplate.DEFAULT_WIDTH / 2), String.valueOf(SvgTemplate.DEFAULT_HEIGHT / 2), "yellow");
        svgTemplate.addElement(rectangle);

        final SvgRectangleSector svgRectangleSector = new SvgRectangleSector(SvgTemplate.DEFAULT_WIDTH / 2, SvgTemplate.DEFAULT_HEIGHT / 2,
                SvgTemplate.DEFAULT_WIDTH / 2, SvgTemplate.DEFAULT_HEIGHT / 2,
                0, 240);
        svgRectangleSector.getElementAttributes().setFill("red");
        svgTemplate.addElement(svgRectangleSector);

        try (PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(OUTPUT_FOLDER
                + File.separator + "compareCoordinatesRectangleAndSection.svg")), true)) {
            out.println(SvgGenerator.generate(svgTemplate));
        }
        checkContent(SvgGenerator.generate(svgTemplate), "compareCoordinatesRectangleAndSection.svg");
    }


    @Test
    public void compareCoordinatesCircleAndSectionTest() throws IOException {
        SvgTemplate svgTemplate = new SvgTemplate();

        final SvgCircle circle = new SvgCircle(0, 0,
                SvgTemplate.DEFAULT_WIDTH / 2);
        circle.getElementAttributes().setFill("blue");
        svgTemplate.addElement(circle);

        final SvgCircleSector svgCircleSector = new SvgCircleSector(0, 0,
                SvgTemplate.DEFAULT_HEIGHT / 2, 0.75);
        svgCircleSector.getElementAttributes().setFill("red");
        svgTemplate.addElement(svgCircleSector);

        try (PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(OUTPUT_FOLDER
                + File.separator + "compareCoordinatesRectangleAndSection.svg")), true)) {
            out.println(SvgGenerator.generate(svgTemplate));
        }
        checkContent(SvgGenerator.generate(svgTemplate), "compareCoordinatesRectangleAndSection.svg");
    }


    @AfterClass
    public void removeFolder() {
        Assert.assertTrue(deleteDirectory(new File(OUTPUT_FOLDER)));
    }
}
