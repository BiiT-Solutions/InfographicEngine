package com.biit.infographic.core.svg;

/*-
 * #%L
 * Infographic Engine v2 (Core)
 * %%
 * Copyright (C) 2022 - 2025 BiiT Sourcing Solutions S.L.
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * #L%
 */

import com.biit.infographic.core.generators.SvgGenerator;
import com.biit.infographic.core.models.svg.SvgTemplate;
import com.biit.infographic.core.models.svg.components.bars.SvgHorizontalBar;
import com.biit.infographic.core.models.svg.components.charts.SvgRadarChart;
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
import java.util.Map;


@Test(groups = {"chartTests"})
public class ChartTests extends SvgGeneration {

    @BeforeClass
    public void prepareFolder() throws IOException {
        Files.createDirectories(Paths.get(OUTPUT_FOLDER));
    }


    @Test
    public void horizontalBarsTests() throws IOException {
        SvgTemplate svgTemplate = new SvgTemplate();
        final SvgHorizontalBar horizontalBar = new SvgHorizontalBar(50, 50, 200, 20, 3.5, 5.0);
        horizontalBar.getElementStroke().setStrokeWidth(1d);

        svgTemplate.addElement(horizontalBar);

        try (PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(OUTPUT_FOLDER
                + File.separator + "horizontalBar.svg")), true)) {
            out.println(SvgGenerator.generate(svgTemplate));
        }

        checkContent(SvgGenerator.generate(svgTemplate), "horizontalBar.svg");

        SvgTemplate svgTemplate1 = SvgTemplate.fromJson(svgTemplate.toJson());
        Assert.assertEquals(SvgGenerator.generate(svgTemplate1), SvgGenerator.generate(svgTemplate));
    }

    @Test
    public void radarChart() throws IOException {
        SvgTemplate svgTemplate = new SvgTemplate();

        final SvgRadarChart radarChart = new SvgRadarChart(50, 50, 300);
        radarChart.setData(
                Map.of("Label1", 5d, "Label2", 5d, "Label3", 5d, "Label4", 5d, "Label5", 5d, "Label6", 5d, "Label7", 5d),
                Map.of("Label1", 4.5d, "Label2", 3d, "Label3", 5d, "Label4", 3.5d, "Label5", 4d, "Label6", 3.5d, "Label7", 1.5d)
        );
        radarChart.getElementAttributes().setFill("#ffdddd");
        radarChart.setDrawRadius(true);
        radarChart.setDrawWeb(true);

        svgTemplate.addElement(radarChart);


        try (PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(OUTPUT_FOLDER
                + File.separator + "radarChart.svg")), true)) {
            out.println(SvgGenerator.generate(svgTemplate));
        }

        checkContent(SvgGenerator.generate(svgTemplate), "radarChart.svg");

        SvgTemplate svgTemplate1 = SvgTemplate.fromJson(svgTemplate.toJson());
        Assert.assertEquals(SvgGenerator.generate(svgTemplate1), SvgGenerator.generate(svgTemplate));
    }

    @AfterClass
    public void removeFolder() {
        Assert.assertTrue(deleteDirectory(new File(OUTPUT_FOLDER)));
    }
}
