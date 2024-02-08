package com.biit.infographic.core.svg;

import com.biit.infographic.core.files.FontSearcher;
import com.biit.infographic.core.generators.SvgGenerator;
import com.biit.infographic.core.models.svg.SvgTemplate;
import com.biit.infographic.core.models.svg.components.text.FontFactory;
import com.biit.infographic.core.models.svg.components.text.FontLengthAdjust;
import com.biit.infographic.core.models.svg.components.text.FontVariantType;
import com.biit.infographic.core.models.svg.components.text.SvgText;
import com.biit.infographic.core.models.svg.components.text.TextAlign;
import com.biit.utils.file.FileReader;
import org.apache.commons.io.FileUtils;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import uk.org.webcompere.systemstubs.environment.EnvironmentVariables;
import uk.org.webcompere.systemstubs.testng.SystemStub;
import uk.org.webcompere.systemstubs.testng.SystemStubsListener;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;

@Test(groups = {"svgText"})
@Listeners(SystemStubsListener.class)
public class TextSvgGenerationTest extends SvgGeneration {
    private static final String LONG_TEXT = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Integer turpis erat, rutrum et neque sit amet, rhoncus tincidunt felis. Vivamus nibh quam, commodo eget maximus quis, lobortis id dolor. Nullam ac sem bibendum, molestie nibh at, facilisis arcu. Aliquam ullamcorper varius orci quis tempor. Lorem ipsum dolor sit amet, consectetur adipiscing elit. Orci varius natoque penatibus et magnis dis parturient montes, nascetur ridiculus mus. Nam imperdiet magna eget turpis maximus tempor. Suspendisse tincidunt vel elit eu iaculis. Etiam sem risus, sodales in lorem eget, suscipit ultricies arcu. In pellentesque interdum rutrum. Nullam pharetra purus et interdum lacinia. Curabitur malesuada tortor ac tortor laoreet, quis placerat magna hendrerit.";
    private static final String LONG_TEXT_WITH_NEW_LINES = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Integer turpis erat, rutrum et neque sit amet, rhoncus tincidunt felis. Vivamus nibh quam, commodo eget maximus quis, lobortis id dolor." + SvgText.NEW_LINE_SYMBOL + " Nullam ac sem bibendum, molestie nibh at, facilisis arcu. Aliquam ullamcorper varius orci quis tempor. Lorem ipsum dolor sit amet, consectetur adipiscing elit. Orci varius natoque penatibus et magnis dis parturient montes, nascetur ridiculus mus. Nam imperdiet magna eget turpis maximus tempor. Suspendisse tincidunt vel elit eu iaculis. Etiam sem risus, sodales in lorem eget, suscipit ultricies arcu." + SvgText.NEW_LINE_SYMBOL + SvgText.NEW_LINE_SYMBOL + " In pellentesque interdum rutrum. Nullam pharetra purus et interdum lacinia. Curabitur malesuada tortor ac tortor laoreet, quis placerat magna hendrerit.";

    @SystemStub
    private EnvironmentVariables setEnvironment;

    @BeforeClass
    public void prepareFolder() throws IOException {
        Files.createDirectories(Paths.get(OUTPUT_FOLDER));
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
    public void documentSimpleTextTestWithNewLine() throws IOException {
        SvgTemplate svgTemplate = new SvgTemplate(SvgTemplate.DEFAULT_WIDTH, SvgTemplate.DEFAULT_HEIGHT);
        final SvgText text = new SvgText("This is the first text\n with a new line\n\n and a paragraph.", 12, SvgTemplate.DEFAULT_WIDTH / 2, SvgTemplate.DEFAULT_HEIGHT / 2);
        svgTemplate.addElement(text);

        try (PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(OUTPUT_FOLDER
                + File.separator + "documentSimpleTextWithNewLine.svg")), true)) {
            out.println(SvgGenerator.generate(svgTemplate));
        }

        checkContent(SvgGenerator.generate(svgTemplate), "documentSimpleTextWithNewLine.svg");
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

        checkContent(SvgGenerator.generate(svgTemplate), "documentLongTextAlignRight.svg");
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
    public void documentLongTextJustifyWithNewLineTest() throws IOException {
        SvgTemplate svgTemplate = new SvgTemplate(SvgTemplate.DEFAULT_WIDTH, SvgTemplate.DEFAULT_HEIGHT);
        final SvgText text = new SvgText(LONG_TEXT_WITH_NEW_LINES, 12, SvgTemplate.DEFAULT_WIDTH / 2, SvgTemplate.DEFAULT_HEIGHT / 2);
        text.setTextAlign(TextAlign.JUSTIFY);
        text.setMaxLineLength(80);
        svgTemplate.addElement(text);

        try (PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(OUTPUT_FOLDER
                + File.separator + "documentLongTextJustifyWithNewLine.svg")), true)) {
            out.println(SvgGenerator.generate(svgTemplate));
        }

        checkContent(SvgGenerator.generate(svgTemplate), "documentLongTextJustifyWithNewLine.svg");
    }

    @Test
    public void documentLongTextJustifyWithNewLineCustomFontTest() throws IOException {
        SvgTemplate svgTemplate = new SvgTemplate(SvgTemplate.DEFAULT_WIDTH * 3, SvgTemplate.DEFAULT_HEIGHT * 3);
        final SvgText text = new SvgText(LONG_TEXT_WITH_NEW_LINES, 12, SvgTemplate.DEFAULT_WIDTH / 2, SvgTemplate.DEFAULT_HEIGHT / 2);
        text.setTextAlign(TextAlign.JUSTIFY);
        text.setMaxLineLength(80);
        text.setFontFamily("Arial");
        svgTemplate.addElement(text);

        try (PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(OUTPUT_FOLDER
                + File.separator + "documentLongTextJustifyWithNewLineCustomFont.svg")), true)) {
            out.println(SvgGenerator.generate(svgTemplate));
        }

        checkContent(SvgGenerator.generate(svgTemplate), "documentLongTextJustifyWithNewLineCustomFont.svg");
    }

    @Test
    public void documentLongTextJustifyByWidthTest() throws IOException {
        SvgTemplate svgTemplate = new SvgTemplate(SvgTemplate.DEFAULT_WIDTH, SvgTemplate.DEFAULT_HEIGHT);
        final SvgText text = new SvgText(LONG_TEXT, 12, SvgTemplate.DEFAULT_WIDTH / 2, SvgTemplate.DEFAULT_HEIGHT / 2);
        text.setTextAlign(TextAlign.JUSTIFY);
        text.setMaxLineWidth(300);
        svgTemplate.addElement(text);

        try (PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(OUTPUT_FOLDER
                + File.separator + "documentLongTextJustifyByWidth.svg")), true)) {
            out.println(SvgGenerator.generate(svgTemplate));
        }

        checkContent(SvgGenerator.generate(svgTemplate), "documentLongTextJustifyByWidth.svg");
    }

    @Test
    public void documentLongTextJustifyByWidthWithNewLinesTest() throws IOException {
        SvgTemplate svgTemplate = new SvgTemplate(SvgTemplate.DEFAULT_WIDTH, SvgTemplate.DEFAULT_HEIGHT);
        final SvgText text = new SvgText(LONG_TEXT_WITH_NEW_LINES, 12, SvgTemplate.DEFAULT_WIDTH / 2, SvgTemplate.DEFAULT_HEIGHT / 2);
        text.setTextAlign(TextAlign.JUSTIFY);
        text.setMaxLineWidth(300);
        svgTemplate.addElement(text);

        try (PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(OUTPUT_FOLDER
                + File.separator + "documentLongTextJustifyByWidthWithNewLines.svg")), true)) {
            out.println(SvgGenerator.generate(svgTemplate));
        }

        checkContent(SvgGenerator.generate(svgTemplate), "documentLongTextJustifyByWidthWithNewLines.svg");
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
    public void documentParagraphHeightTest() throws IOException {
        SvgTemplate svgTemplate = new SvgTemplate(SvgTemplate.DEFAULT_WIDTH, SvgTemplate.DEFAULT_HEIGHT);
        SvgText text = new SvgText(LONG_TEXT, 8, 0L, 0L);
        text.setFontVariant(FontVariantType.NORMAL);
        text.setTextAlign(TextAlign.JUSTIFY);
        text.setMaxLineWidth(120);
        text.setMaxParagraphHeight(180);
        svgTemplate.addElement(text);


        try (PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(OUTPUT_FOLDER
                + File.separator + "documentParagraphHeight.svg")), true)) {
            out.println(SvgGenerator.generate(svgTemplate));
        }

        checkContent(SvgGenerator.generate(svgTemplate), "documentParagraphHeight.svg");
    }

    @Test
    public void documentOtherParagraphHeightTest() throws IOException {
        SvgTemplate svgTemplate = new SvgTemplate(SvgTemplate.DEFAULT_WIDTH, SvgTemplate.DEFAULT_HEIGHT);
        SvgText text = new SvgText(LONG_TEXT, 8, 0L, 0L);
        text.setFontVariant(FontVariantType.NORMAL);
        text.setTextAlign(TextAlign.JUSTIFY);
        text.setMaxLineWidth(200);
        text.setMaxParagraphHeight(90);
        svgTemplate.addElement(text);


        try (PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(OUTPUT_FOLDER
                + File.separator + "documentOtherParagraphHeight.svg")), true)) {
            out.println(SvgGenerator.generate(svgTemplate));
        }

        checkContent(SvgGenerator.generate(svgTemplate), "documentOtherParagraphHeight.svg");
    }


    @Test
    public void documentMontserratFontTest() throws IOException {
        SvgTemplate svgTemplate = new SvgTemplate(SvgTemplate.DEFAULT_WIDTH, SvgTemplate.DEFAULT_HEIGHT);
        SvgText text = new SvgText("Montserrat", LONG_TEXT, 8, 0L, 0L);
        text.setFontVariant(FontVariantType.NORMAL);
        text.setTextAlign(TextAlign.JUSTIFY);
        text.setMaxLineWidth(200);
        text.setMaxParagraphHeight(90);
        svgTemplate.addElement(text);


        try (PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(OUTPUT_FOLDER
                + File.separator + "documentMontserratFont.svg")), true)) {
            out.println(SvgGenerator.generate(svgTemplate));
        }

        checkContent(SvgGenerator.generate(svgTemplate), "documentMontserratFont.svg");
    }

    @Test
    public void documentMondayDonuts() throws IOException {
        SvgTemplate svgTemplate = new SvgTemplate(SvgTemplate.DEFAULT_WIDTH, SvgTemplate.DEFAULT_HEIGHT);
        SvgText text = new SvgText("Monday Donuts", LONG_TEXT, 8, 0L, 0L);
        text.setFontVariant(FontVariantType.NORMAL);
        text.setTextAlign(TextAlign.JUSTIFY);
        text.setMaxLineWidth(200);
        text.setMaxParagraphHeight(90);
        svgTemplate.addElement(text);


        try (PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(OUTPUT_FOLDER
                + File.separator + "documentMondayDonutsFont.svg")), true)) {
            out.println(SvgGenerator.generate(svgTemplate));
        }

        checkContent(SvgGenerator.generate(svgTemplate), "documentMondayDonutsFont.svg");
    }

    @Test
    public void fontFromEnvVariable() throws IOException {
        FontFactory.resetFonts();
        //Copy font to an external Folder
        File fontToCopy = FileReader.getResource("fonts/others/Milk Mango.ttf");
        File outputDirectory = Files.createTempDirectory("fonts").toFile();
        outputDirectory.deleteOnExit();
        Files.createDirectories(Paths.get(outputDirectory + File.separator + "fonts"));
        File fontCopied = Files.createTempFile(new File(outputDirectory.toPath() + File.separator + "fonts").toPath(), "Milk Mango", ".ttf").toFile();
        fontCopied.deleteOnExit();
        FileUtils.copyFile(fontToCopy, fontCopied);

        //Set the external folder as an ENV variable.
        setEnvironment.set(FontSearcher.SYSTEM_VARIABLE_FILES_LOCATION, outputDirectory.toString());

        //Create SVG and check the embedded font.
        SvgTemplate svgTemplate = new SvgTemplate(SvgTemplate.DEFAULT_WIDTH, SvgTemplate.DEFAULT_HEIGHT);
        SvgText text = new SvgText("Milk Mango", LONG_TEXT, 8, 0L, 0L);
        text.setFontVariant(FontVariantType.NORMAL);
        text.setTextAlign(TextAlign.JUSTIFY);
        text.setMaxLineWidth(200);
        text.setMaxParagraphHeight(90);
        svgTemplate.addElement(text);


        try (PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(OUTPUT_FOLDER
                + File.separator + "documentMilkMangoEnvFont.svg")), true)) {
            out.println(SvgGenerator.generate(svgTemplate));
        }

        checkContent(SvgGenerator.generate(svgTemplate), "documentMilkMangoEnvFont.svg");
    }

    @AfterClass
    public void removeFolder() {
        Assert.assertTrue(deleteDirectory(new File(OUTPUT_FOLDER)));
    }
}
