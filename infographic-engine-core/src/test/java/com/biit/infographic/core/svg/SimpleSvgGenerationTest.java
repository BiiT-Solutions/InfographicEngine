package com.biit.infographic.core.svg;

import com.biit.infographic.core.models.svg.SvgBackground;
import com.biit.infographic.core.models.svg.SvgDocument;
import com.biit.infographic.core.models.svg.components.SvgCircle;
import com.biit.infographic.core.models.svg.components.SvgRectangle;
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

@Test(groups = {"simpleSvgGenerationTest"})
public class SimpleSvgGenerationTest {
    private static final String OUTPUT_FOLDER = System.getProperty("java.io.tmpdir") + File.separator + "SvgTests";

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
    }

    @Test
    public void documentBackgroundImageTest() throws IOException {
        SvgDocument svgDocument = new SvgDocument();
        svgDocument.setSvgBackground(new SvgBackground().image(readBase64Image("EliseNess.txt")));

        try (PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(OUTPUT_FOLDER
                + File.separator + "documentBackgroundImage.svg")), true)) {
            out.println(SvgGenerator.generate(svgDocument));
        }
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
    }

    @Test(expectedExceptions = InvalidAttributeException.class)
    public void documentDrawInvalidCircleTest() throws IOException {
        SvgDocument svgDocument = new SvgDocument();
        svgDocument.addElement(new SvgCircle(SvgDocument.DEFAULT_WIDTH / 2, SvgDocument.DEFAULT_HEIGHT / 2,
                0L));

        try (PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(OUTPUT_FOLDER
                + File.separator + "documentDrawCircle.svg")), true)) {
            out.println(SvgGenerator.generate(svgDocument));
        }
    }

    @AfterClass(enabled = false)
    public void removeFolder() {
        Assert.assertTrue(deleteDirectory(new File(OUTPUT_FOLDER)));
    }
}
