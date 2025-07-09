package com.biit.infographic.core.svg;

import com.biit.infographic.core.generators.SvgGenerator;
import com.biit.infographic.core.models.svg.SvgTemplate;
import com.biit.infographic.core.models.svg.components.bars.SvgHorizontalBar;
import com.biit.infographic.core.models.svg.components.gauge.GaugeType;
import com.biit.infographic.core.models.svg.components.gauge.SvgGauge;
import com.fasterxml.jackson.core.JsonProcessingException;
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

@Test(groups = {"customSvgComponentsTest"})
public class CustomSvgComponentsTest extends SvgGeneration {

    @BeforeClass
    public void prepareFolder() throws IOException {
        Files.createDirectories(Paths.get(OUTPUT_FOLDER));
    }

    @Test
    public void gaugeTest() throws IOException {
        SvgTemplate svgTemplate = new SvgTemplate();
        svgTemplate.addElement(new SvgGauge(1.0, 10.0, 3.0));

        try (PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(OUTPUT_FOLDER
                + File.separator + "documentGauge.svg")), true)) {
            out.println(SvgGenerator.generate(svgTemplate));
        }

        checkContent(SvgGenerator.generate(svgTemplate), "documentGauge.svg");
    }

    @Test
    public void gaugeDifferentColorsTest() throws IOException {
        SvgTemplate svgTemplate = new SvgTemplate();
        final SvgGauge gauge = new SvgGauge(1.0, 10.0, 3.8);
        gauge.setColors(new String[]{"#000000", "#ff0000"});
        gauge.setFlip(true);
        svgTemplate.addElement(gauge);

        try (PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(OUTPUT_FOLDER
                + File.separator + "documentGaugeDifferentColors.svg")), true)) {
            out.println(SvgGenerator.generate(svgTemplate));
        }

        checkContent(SvgGenerator.generate(svgTemplate), "documentGaugeDifferentColors.svg");
    }

    @Test
    public void gaugeFlipTest() throws IOException {
        SvgTemplate svgTemplate = new SvgTemplate();
        final SvgGauge gauge = new SvgGauge(1.0, 10.0, 3.8);
        gauge.setFlip(true);
        svgTemplate.addElement(gauge);

        try (PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(OUTPUT_FOLDER
                + File.separator + "documentGaugeFlip.svg")), true)) {
            out.println(SvgGenerator.generate(svgTemplate));
        }

        checkContent(SvgGenerator.generate(svgTemplate), "documentGaugeFlip.svg");
    }

    @Test
    public void gauge5ValuesTest() throws IOException {
        SvgTemplate svgTemplate = new SvgTemplate();
        final SvgGauge gauge = new SvgGauge(1.0, 5.0, 4.0);
        gauge.setType(GaugeType.FIVE_VALUES);
        svgTemplate.addElement(gauge);

        try (PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(OUTPUT_FOLDER
                + File.separator + "documentGauge5Values.svg")), true)) {
            out.println(SvgGenerator.generate(svgTemplate));
        }

        checkContent(SvgGenerator.generate(svgTemplate), "documentGauge5Values.svg");
    }

    @Test
    public void gauge5ValuesFlipTest() throws IOException {
        SvgTemplate svgTemplate = new SvgTemplate();
        final SvgGauge gauge = new SvgGauge(1.0, 5.0, 4.0);
        gauge.setType(GaugeType.FIVE_VALUES);
        gauge.setFlip(true);
        svgTemplate.addElement(gauge);

        try (PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(OUTPUT_FOLDER
                + File.separator + "documentGauge5ValuesFlip.svg")), true)) {
            out.println(SvgGenerator.generate(svgTemplate));
        }

        checkContent(SvgGenerator.generate(svgTemplate), "documentGauge5ValuesFlip.svg");
    }

    @AfterClass
    public void removeFolder() {
        Assert.assertTrue(deleteDirectory(new File(OUTPUT_FOLDER)));
    }
}
