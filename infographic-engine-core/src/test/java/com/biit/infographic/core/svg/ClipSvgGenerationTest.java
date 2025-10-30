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
import com.biit.infographic.core.models.svg.clip.ClipDirection;
import com.biit.infographic.core.models.svg.clip.SvgRectangleClipPath;
import com.biit.infographic.core.models.svg.components.SvgCircle;
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

@Test(groups = {"clipSvgGenerationTest"})
public class ClipSvgGenerationTest extends SvgGeneration {

    @BeforeClass
    public void prepareFolder() throws IOException {
        Files.createDirectories(Paths.get(OUTPUT_FOLDER));
    }


    @Test
    public void documentDrawCircleClipTopToBottomTest33() throws IOException {
        final SvgTemplate svgTemplate = new SvgTemplate();
        svgTemplate.setUuid(TEMPLATE_ID);
        svgTemplate.setId(TEMPLATE_ID);
        final SvgCircle svgCircle = new SvgCircle(0, 0,
                SvgTemplate.DEFAULT_WIDTH / 2);
        svgCircle.setClipPath(new SvgRectangleClipPath(0.33D, ClipDirection.TOP_TO_BOTTOM));
        svgTemplate.addElement(svgCircle);

        try (PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(OUTPUT_FOLDER
                + File.separator + "documentDrawCircleClipTopToBottom33.svg")), true)) {
            out.println(SvgGenerator.generate(svgTemplate));
        }

        checkContent(SvgGenerator.generate(svgTemplate), "documentDrawCircleClipTopToBottom33.svg");
    }

    @Test
    public void documentDrawCircleClipTopToBottomTest66() throws IOException {
        final SvgTemplate svgTemplate = new SvgTemplate();
        svgTemplate.setUuid(TEMPLATE_ID);
        svgTemplate.setId(TEMPLATE_ID);
        final SvgCircle svgCircle = new SvgCircle(0, 0,
                SvgTemplate.DEFAULT_WIDTH / 2);
        svgCircle.setClipPath(new SvgRectangleClipPath(0.66D, ClipDirection.TOP_TO_BOTTOM));
        svgTemplate.addElement(svgCircle);

        try (PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(OUTPUT_FOLDER
                + File.separator + "documentDrawCircleClipTopToBottom66.svg")), true)) {
            out.println(SvgGenerator.generate(svgTemplate));
        }

        checkContent(SvgGenerator.generate(svgTemplate), "documentDrawCircleClipTopToBottom66.svg");
    }

    @Test
    public void documentDrawCircleClipBottomToTopTest33() throws IOException {
        final SvgTemplate svgTemplate = new SvgTemplate();
        svgTemplate.setUuid(TEMPLATE_ID);
        svgTemplate.setId(TEMPLATE_ID);
        final SvgCircle svgCircle = new SvgCircle(0, 0,
                SvgTemplate.DEFAULT_WIDTH / 2);
        svgCircle.setClipPath(new SvgRectangleClipPath(0.33D, ClipDirection.BOTTOM_TO_TOP));
        svgTemplate.addElement(svgCircle);

        try (PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(OUTPUT_FOLDER
                + File.separator + "documentDrawCircleClipBottomToTop33.svg")), true)) {
            out.println(SvgGenerator.generate(svgTemplate));
        }

        checkContent(SvgGenerator.generate(svgTemplate), "documentDrawCircleClipBottomToTop33.svg");
    }

    @Test
    public void documentDrawCircleClipBottomToTopTest66() throws IOException {
        final SvgTemplate svgTemplate = new SvgTemplate();
        svgTemplate.setUuid(TEMPLATE_ID);
        svgTemplate.setId(TEMPLATE_ID);
        final SvgCircle svgCircle = new SvgCircle(0, 0,
                SvgTemplate.DEFAULT_WIDTH / 2);
        svgCircle.setClipPath(new SvgRectangleClipPath(0.66D, ClipDirection.BOTTOM_TO_TOP));
        svgTemplate.addElement(svgCircle);

        try (PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(OUTPUT_FOLDER
                + File.separator + "documentDrawCircleClipBottomToTop66.svg")), true)) {
            out.println(SvgGenerator.generate(svgTemplate));
        }

        checkContent(SvgGenerator.generate(svgTemplate), "documentDrawCircleClipBottomToTop66.svg");
    }

    @Test
    public void documentDrawCircleClipLeftToRightTest33() throws IOException {
        final SvgTemplate svgTemplate = new SvgTemplate();
        svgTemplate.setUuid(TEMPLATE_ID);
        svgTemplate.setId(TEMPLATE_ID);
        final SvgCircle svgCircle = new SvgCircle(0, 0,
                SvgTemplate.DEFAULT_WIDTH / 2);
        svgCircle.setClipPath(new SvgRectangleClipPath(0.33D, ClipDirection.LEFT_TO_RIGHT));
        svgTemplate.addElement(svgCircle);

        try (PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(OUTPUT_FOLDER
                + File.separator + "documentDrawCircleClipLeftToRight33.svg")), true)) {
            out.println(SvgGenerator.generate(svgTemplate));
        }

        checkContent(SvgGenerator.generate(svgTemplate), "documentDrawCircleClipLeftToRight33.svg");
    }

    @Test
    public void documentDrawCircleClipLeftToRightTest66() throws IOException {
        final SvgTemplate svgTemplate = new SvgTemplate();
        svgTemplate.setUuid(TEMPLATE_ID);
        svgTemplate.setId(TEMPLATE_ID);
        final SvgCircle svgCircle = new SvgCircle(0, 0,
                SvgTemplate.DEFAULT_WIDTH / 2);
        svgCircle.setClipPath(new SvgRectangleClipPath(0.66D, ClipDirection.LEFT_TO_RIGHT));
        svgTemplate.addElement(svgCircle);

        try (PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(OUTPUT_FOLDER
                + File.separator + "documentDrawCircleClipLeftToRight66.svg")), true)) {
            out.println(SvgGenerator.generate(svgTemplate));
        }

        checkContent(SvgGenerator.generate(svgTemplate), "documentDrawCircleClipLeftToRight66.svg");
    }

    @Test
    public void documentDrawCircleClipRightToLeftTest33() throws IOException {
        final SvgTemplate svgTemplate = new SvgTemplate();
        svgTemplate.setUuid(TEMPLATE_ID);
        svgTemplate.setId(TEMPLATE_ID);
        final SvgCircle svgCircle = new SvgCircle(0, 0,
                SvgTemplate.DEFAULT_WIDTH / 2);
        svgCircle.setClipPath(new SvgRectangleClipPath(0.33D, ClipDirection.RIGHT_TO_LEFT));
        svgTemplate.addElement(svgCircle);

        try (PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(OUTPUT_FOLDER
                + File.separator + "documentDrawCircleClipRightToLeft33.svg")), true)) {
            out.println(SvgGenerator.generate(svgTemplate));
        }

        checkContent(SvgGenerator.generate(svgTemplate), "documentDrawCircleClipRightToLeft33.svg");
    }

    @Test
    public void documentDrawCircleClipRightToLeftTest66() throws IOException {
        final SvgTemplate svgTemplate = new SvgTemplate();
        svgTemplate.setUuid(TEMPLATE_ID);
        svgTemplate.setId(TEMPLATE_ID);
        final SvgCircle svgCircle = new SvgCircle(0, 0,
                SvgTemplate.DEFAULT_WIDTH / 2);
        svgCircle.setClipPath(new SvgRectangleClipPath(0.66D, ClipDirection.RIGHT_TO_LEFT));
        svgTemplate.addElement(svgCircle);

        try (PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(OUTPUT_FOLDER
                + File.separator + "documentDrawCircleClipRightToLeft66.svg")), true)) {
            out.println(SvgGenerator.generate(svgTemplate));
        }

        checkContent(SvgGenerator.generate(svgTemplate), "documentDrawCircleClipRightToLeft66.svg");
    }

    @AfterClass
    public void removeFolder() {
        Assert.assertTrue(deleteDirectory(new File(OUTPUT_FOLDER)));
    }
}
