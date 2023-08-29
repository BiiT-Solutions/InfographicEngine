package com.biit.infographic.core.svg;

import com.biit.infographic.core.models.svg.SvgDocument;
import com.biit.infographic.core.models.svg.components.custom.SvgGauge;
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

@Test(groups = {"customSvgComponentsTest"})
public class CustomSvgComponentsTest {
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
    public void gaugeTest() throws IOException {
        SvgDocument svgDocument = new SvgDocument();
        svgDocument.addElement(new SvgGauge(1.0, 10.0, 3.0) {
        });

        try (PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(OUTPUT_FOLDER
                + File.separator + "documentGauge.svg")), true)) {
            out.println(SvgGenerator.generate(svgDocument));
        }

        checkContent(SvgGenerator.generate(svgDocument), "documentGauge.svg");
    }

    @AfterClass(enabled = false)
    public void removeFolder() {
        Assert.assertTrue(deleteDirectory(new File(OUTPUT_FOLDER)));
    }
}
