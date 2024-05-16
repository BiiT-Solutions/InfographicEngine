package com.biit.infographic.core.svg;

import com.biit.infographic.core.generators.SvgGenerator;
import com.biit.infographic.core.models.svg.SvgTemplate;
import com.biit.infographic.core.models.svg.components.SvgCircle;
import com.biit.infographic.core.models.svg.components.SvgScript;
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

public class ActionableSVGTest extends SvgGeneration {
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
                
                document.querySelector("circle").addEventListener("mouseover", (e) => {
                    e.target.style.fill = getColor();
                });
                document.querySelector("circle").addEventListener("mouseout", (e) => {
                    e.target.style.fill = getColor();
                });
            """;

    @BeforeClass
    public void prepareFolder() throws IOException {
        Files.createDirectories(Paths.get(OUTPUT_FOLDER));
    }

    @Test
    public void generateSvgWithButton() throws IOException {
        SvgTemplate svgTemplate = new SvgTemplate(SvgTemplate.DEFAULT_WIDTH, SvgTemplate.DEFAULT_HEIGHT);
        SvgCircle svgCircle = new SvgCircle(SvgTemplate.DEFAULT_WIDTH / 4, SvgTemplate.DEFAULT_HEIGHT / 4,
                SvgTemplate.DEFAULT_WIDTH / 4);
        svgCircle.setLink("http://www.google.es");
        svgTemplate.addElement(svgCircle);

        svgTemplate.addElement(new SvgScript(SCRIPT));

        try (PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(OUTPUT_FOLDER
                + File.separator + "documentWithButton.svg")), true)) {
            out.println(SvgGenerator.generate(svgTemplate));
        }

        checkContent(SvgGenerator.generate(svgTemplate), "documentWithButton.svg");
    }


    @AfterClass
    public void removeFolder() {
        Assert.assertTrue(deleteDirectory(new File(OUTPUT_FOLDER)));
    }
}
