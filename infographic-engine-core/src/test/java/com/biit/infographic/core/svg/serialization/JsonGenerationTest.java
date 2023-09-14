package com.biit.infographic.core.svg.serialization;

import com.biit.infographic.core.models.svg.SvgBackground;
import com.biit.infographic.core.models.svg.SvgTemplate;
import com.biit.infographic.core.models.svg.components.Point;
import com.biit.infographic.core.models.svg.components.StrokeLineCap;
import com.biit.infographic.core.models.svg.components.SvgCircle;
import com.biit.infographic.core.models.svg.components.SvgEllipse;
import com.biit.infographic.core.models.svg.components.SvgLine;
import com.biit.infographic.core.models.svg.components.SvgPath;
import com.biit.infographic.core.models.svg.components.SvgRectangle;
import com.biit.infographic.core.models.svg.components.SvgScript;
import com.biit.infographic.core.models.svg.components.text.FontLengthAdjust;
import com.biit.infographic.core.models.svg.components.text.FontVariantType;
import com.biit.infographic.core.models.svg.components.text.SvgText;
import com.biit.infographic.core.models.svg.components.text.TextAlign;
import com.biit.infographic.core.generators.SvgGenerator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
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

@Test(groups = "jsonGeneration")
public class JsonGenerationTest {
    private static final String OUTPUT_FOLDER = System.getProperty("java.io.tmpdir") + File.separator + "JsonTests";
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


    private final ObjectMapper objectMapper = new ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT);

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
                    .getResource("json" + File.separator + resourceFile).toURI()))).trim());
        } catch (IOException | URISyntaxException e) {
            Assert.fail();
        }
    }

    private String generateJson(SvgTemplate template) throws JsonProcessingException {
        //return objectMapper.setSerializationInclusion(Include.NON_NULL).enable(DeserializationFeature.UNWRAP_ROOT_VALUE).enable(SerializationFeature.WRAP_ROOT_VALUE).writeValueAsString(template);
        return objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL).writeValueAsString(template);
    }

    private void check(SvgTemplate sourceTemplate, SvgTemplate newTemplate) {
        Assert.assertEquals(SvgGenerator.generate(newTemplate), SvgGenerator.generate(sourceTemplate));
    }

    private String readBase64Image(String imageName) {
        try {
            return new String(Files.readAllBytes(Paths.get(getClass().getClassLoader()
                    .getResource("images" + File.separator + imageName).toURI())));
        } catch (Exception e) {
            Assert.fail("Cannot read resource 'images/" + imageName + "'.");
        }
        return null;
    }

    @BeforeClass
    public void prepareFolder() throws IOException {
        Files.createDirectories(Paths.get(OUTPUT_FOLDER));
    }

    @Test
    public void documentBackgroundColorTest() throws IOException {
        SvgTemplate svgTemplate = new SvgTemplate();
        svgTemplate.setSvgBackground(new SvgBackground().backgroundColor("#449911"));
        String jsonText = generateJson(svgTemplate);

        SvgTemplate svgTemplate1 = objectMapper.readValue(jsonText, SvgTemplate.class);
        check(svgTemplate, svgTemplate1);
    }

    @Test
    public void documentBackgroundImageTest() throws IOException {
        SvgTemplate svgTemplate = new SvgTemplate();
        svgTemplate.setSvgBackground(new SvgBackground().image(readBase64Image("EliseNess.txt")));

        String jsonText = generateJson(svgTemplate);

        SvgTemplate svgTemplate1 = objectMapper.readValue(jsonText, SvgTemplate.class);
        check(svgTemplate, svgTemplate1);
    }

    @Test
    public void documentDrawRectangleTest() throws IOException {
        SvgTemplate svgTemplate = new SvgTemplate();
        svgTemplate.addElement(new SvgRectangle(SvgTemplate.DEFAULT_WIDTH / 2, SvgTemplate.DEFAULT_HEIGHT / 2,
                String.valueOf(SvgTemplate.DEFAULT_WIDTH / 2), String.valueOf(SvgTemplate.DEFAULT_HEIGHT / 2), "ff0000"));
        String jsonText = generateJson(svgTemplate);

        SvgTemplate svgTemplate1 = objectMapper.readValue(jsonText, SvgTemplate.class);
        check(svgTemplate, svgTemplate1);
    }

    @Test
    public void documentDrawRectangleWithRadiusTest() throws IOException {
        SvgTemplate svgTemplate = new SvgTemplate();
        final SvgRectangle rectangle = new SvgRectangle(SvgTemplate.DEFAULT_WIDTH / 2, SvgTemplate.DEFAULT_HEIGHT / 2,
                String.valueOf(SvgTemplate.DEFAULT_WIDTH / 2), String.valueOf(SvgTemplate.DEFAULT_HEIGHT / 2), "ff0000");
        rectangle.setXRadius(25L);
        rectangle.setXRadius(50L);
        svgTemplate.addElement(rectangle);

        String jsonText = generateJson(svgTemplate);

        SvgTemplate svgTemplate1 = objectMapper.readValue(jsonText, SvgTemplate.class);
        check(svgTemplate, svgTemplate1);
    }

    @Test
    public void documentDrawCircleTest() throws IOException {
        SvgTemplate svgTemplate = new SvgTemplate();
        svgTemplate.addElement(new SvgCircle(SvgTemplate.DEFAULT_WIDTH / 2, SvgTemplate.DEFAULT_HEIGHT / 2,
                SvgTemplate.DEFAULT_WIDTH / 2));

        String jsonText = generateJson(svgTemplate);

        SvgTemplate svgTemplate1 = objectMapper.readValue(jsonText, SvgTemplate.class);
        check(svgTemplate, svgTemplate1);
    }

    @Test
    public void documentDrawEllipseTest() throws IOException {
        SvgTemplate svgTemplate = new SvgTemplate();
        final SvgEllipse ellipse = new SvgEllipse(SvgTemplate.DEFAULT_WIDTH / 2, SvgTemplate.DEFAULT_HEIGHT / 2,
                String.valueOf(SvgTemplate.DEFAULT_WIDTH / 4), String.valueOf(SvgTemplate.DEFAULT_HEIGHT / 2)
                , "ff00ff");
        svgTemplate.addElement(ellipse);

        String jsonText = generateJson(svgTemplate);

        SvgTemplate svgTemplate1 = objectMapper.readValue(jsonText, SvgTemplate.class);
        check(svgTemplate, svgTemplate1);
    }

    @Test
    public void documentDrawLineTest() throws IOException {
        SvgTemplate svgTemplate = new SvgTemplate();
        final SvgLine line = new SvgLine(50L, 50L, SvgTemplate.DEFAULT_WIDTH, SvgTemplate.DEFAULT_HEIGHT);
        svgTemplate.addElement(line);

        String jsonText = generateJson(svgTemplate);

        SvgTemplate svgTemplate1 = objectMapper.readValue(jsonText, SvgTemplate.class);
        check(svgTemplate, svgTemplate1);
    }

    @Test
    public void documentDrawLineStrokeTest() throws IOException {
        SvgTemplate svgTemplate = new SvgTemplate();
        final SvgLine line = new SvgLine(50L, 50L, SvgTemplate.DEFAULT_WIDTH, SvgTemplate.DEFAULT_HEIGHT);
        line.getElementStroke().setLineCap(StrokeLineCap.ROUND);
        line.getElementStroke().setStrokeDash(Arrays.asList(5, 5, 10, 10, 1));
        svgTemplate.addElement(line);

        String jsonText = generateJson(svgTemplate);

        SvgTemplate svgTemplate1 = objectMapper.readValue(jsonText, SvgTemplate.class);
        check(svgTemplate, svgTemplate1);
    }

    @Test
    public void documentSimpleTextTest() throws IOException {
        SvgTemplate svgTemplate = new SvgTemplate(SvgTemplate.DEFAULT_WIDTH, SvgTemplate.DEFAULT_HEIGHT);
        final SvgText text = new SvgText("This is the first text", 12, SvgTemplate.DEFAULT_WIDTH / 2, SvgTemplate.DEFAULT_HEIGHT / 2);
        svgTemplate.addElement(text);

        String jsonText = generateJson(svgTemplate);

        SvgTemplate svgTemplate1 = objectMapper.readValue(jsonText, SvgTemplate.class);
        check(svgTemplate, svgTemplate1);
    }

    @Test
    public void documentLongTextLimitedLineTest() throws IOException {
        SvgTemplate svgTemplate = new SvgTemplate(SvgTemplate.DEFAULT_WIDTH, SvgTemplate.DEFAULT_HEIGHT);
        final SvgText text = new SvgText(LONG_TEXT, 12, SvgTemplate.DEFAULT_WIDTH / 2, SvgTemplate.DEFAULT_HEIGHT / 2);
        text.setLengthAdjust(FontLengthAdjust.SPACING);
        text.setMaxLineLength(80);
        svgTemplate.addElement(text);

        String jsonText = generateJson(svgTemplate);

        SvgTemplate svgTemplate1 = objectMapper.readValue(jsonText, SvgTemplate.class);
        check(svgTemplate, svgTemplate1);
    }

    @Test
    public void documentLongTextLimitedLineForcingLengthTest() throws IOException {
        SvgTemplate svgTemplate = new SvgTemplate(SvgTemplate.DEFAULT_WIDTH, SvgTemplate.DEFAULT_HEIGHT);
        final SvgText text = new SvgText(LONG_TEXT, 12, SvgTemplate.DEFAULT_WIDTH / 2, SvgTemplate.DEFAULT_HEIGHT / 2);
        text.setTextLength("8000");
        text.setLengthAdjust(FontLengthAdjust.SPACING);
        text.setMaxLineLength(80);
        svgTemplate.addElement(text);

        String jsonText = generateJson(svgTemplate);

        SvgTemplate svgTemplate1 = objectMapper.readValue(jsonText, SvgTemplate.class);
        check(svgTemplate, svgTemplate1);
    }

    @Test
    public void documentLongTextAlignRightTest() throws IOException {
        SvgTemplate svgTemplate = new SvgTemplate(SvgTemplate.DEFAULT_WIDTH, SvgTemplate.DEFAULT_HEIGHT);
        final SvgText text = new SvgText(LONG_TEXT, 12, SvgTemplate.DEFAULT_WIDTH / 2, SvgTemplate.DEFAULT_HEIGHT / 2);
        text.setTextAlign(TextAlign.RIGHT);
        text.setMaxLineLength(80);
        svgTemplate.addElement(text);

        String jsonText = generateJson(svgTemplate);

        SvgTemplate svgTemplate1 = objectMapper.readValue(jsonText, SvgTemplate.class);
        check(svgTemplate, svgTemplate1);
    }

    @Test
    public void documentLongTextJustifyTest() throws IOException {
        SvgTemplate svgTemplate = new SvgTemplate(SvgTemplate.DEFAULT_WIDTH, SvgTemplate.DEFAULT_HEIGHT);
        final SvgText text = new SvgText(LONG_TEXT, 12, SvgTemplate.DEFAULT_WIDTH / 2, SvgTemplate.DEFAULT_HEIGHT / 2);
        text.setTextAlign(TextAlign.JUSTIFY);
        text.setMaxLineLength(80);
        svgTemplate.addElement(text);

        String jsonText = generateJson(svgTemplate);

        SvgTemplate svgTemplate1 = objectMapper.readValue(jsonText, SvgTemplate.class);
        check(svgTemplate, svgTemplate1);
    }

    @Test
    public void documentLongTextJustifyByWidthTest() throws IOException {
        SvgTemplate svgTemplate = new SvgTemplate(SvgTemplate.DEFAULT_WIDTH, SvgTemplate.DEFAULT_HEIGHT);
        final SvgText text = new SvgText(LONG_TEXT, 12, SvgTemplate.DEFAULT_WIDTH / 2, SvgTemplate.DEFAULT_HEIGHT / 2);
        text.setTextAlign(TextAlign.JUSTIFY);
        text.setMaxLineWidth(300);
        text.setMaxLineLength(80);
        svgTemplate.addElement(text);

        String jsonText = generateJson(svgTemplate);

        SvgTemplate svgTemplate1 = objectMapper.readValue(jsonText, SvgTemplate.class);
        check(svgTemplate, svgTemplate1);
    }

    @Test
    public void documentLongTextJustifyRotatedTest() throws IOException {
        SvgTemplate svgTemplate = new SvgTemplate(SvgTemplate.DEFAULT_WIDTH, SvgTemplate.DEFAULT_HEIGHT);
        final SvgText text = new SvgText(LONG_TEXT, 12, SvgTemplate.DEFAULT_WIDTH / 2, SvgTemplate.DEFAULT_HEIGHT / 2);
        text.setTextAlign(TextAlign.JUSTIFY);
        text.setMaxLineLength(80);
        text.setRotate(90L);
        svgTemplate.addElement(text);

        String jsonText = generateJson(svgTemplate);

        SvgTemplate svgTemplate1 = objectMapper.readValue(jsonText, SvgTemplate.class);
        check(svgTemplate, svgTemplate1);
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

        String jsonText = generateJson(svgTemplate);

        SvgTemplate svgTemplate1 = objectMapper.readValue(jsonText, SvgTemplate.class);
        check(svgTemplate, svgTemplate1);
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

        String jsonText = generateJson(svgTemplate);

        SvgTemplate svgTemplate1 = objectMapper.readValue(jsonText, SvgTemplate.class);
        check(svgTemplate, svgTemplate1);
    }

    @Test
    public void documentScriptTest() throws IOException {
        SvgTemplate svgTemplate = new SvgTemplate();
        svgTemplate.addElement(new SvgCircle(SvgTemplate.DEFAULT_WIDTH / 2, SvgTemplate.DEFAULT_HEIGHT / 2,
                SvgTemplate.DEFAULT_WIDTH / 2));

        svgTemplate.addElement(new SvgScript(SCRIPT));

        String jsonText = generateJson(svgTemplate);

        SvgTemplate svgTemplate1 = objectMapper.readValue(jsonText, SvgTemplate.class);
        check(svgTemplate, svgTemplate1);
    }

    @Test
    public void nestedDocumentsTest() throws JsonProcessingException {
        SvgTemplate svgTemplate = new SvgTemplate();
        svgTemplate.setId("Parent");

        SvgTemplate childDocument1 = new SvgTemplate();
        childDocument1.addElement(new SvgCircle(SvgTemplate.DEFAULT_WIDTH / 2, SvgTemplate.DEFAULT_HEIGHT / 2,
                SvgTemplate.DEFAULT_WIDTH / 2));
        childDocument1.setId("Child1");
        svgTemplate.addElement(childDocument1);

        SvgTemplate childDocument2 = new SvgTemplate();
        childDocument2.addElement(new SvgRectangle(SvgTemplate.DEFAULT_WIDTH / 2, SvgTemplate.DEFAULT_HEIGHT / 2,
                String.valueOf(SvgTemplate.DEFAULT_WIDTH / 2), String.valueOf(SvgTemplate.DEFAULT_HEIGHT / 2), "ff0000"));
        childDocument2.setId("Child2");
        svgTemplate.addElement(childDocument2);

        String jsonText = generateJson(svgTemplate);

        SvgTemplate svgTemplate1 = objectMapper.readValue(jsonText, SvgTemplate.class);
        check(svgTemplate, svgTemplate1);
    }

    @Test
    public void documentDrawPathTest() throws IOException {
        SvgTemplate svgTemplate = new SvgTemplate();
        final SvgPath line = new SvgPath(0L, 0L, new Point(50L, 50L), new Point(100L, 0L), new Point(200L, 150L));
        line.getElementStroke().setLineCap(StrokeLineCap.ROUND);
        line.getElementStroke().setStrokeDash(Arrays.asList(5, 5, 10, 10, 1));
        svgTemplate.addElement(line);

        String jsonText = generateJson(svgTemplate);

        SvgTemplate svgTemplate1 = objectMapper.readValue(jsonText, SvgTemplate.class);
        check(svgTemplate, svgTemplate1);
    }



    @AfterClass(enabled = false)
    public void removeFolder() {
        Assert.assertTrue(deleteDirectory(new File(OUTPUT_FOLDER)));
    }
}
