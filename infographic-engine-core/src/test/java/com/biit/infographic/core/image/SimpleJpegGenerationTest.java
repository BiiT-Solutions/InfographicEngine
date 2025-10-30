package com.biit.infographic.core.image;

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

import com.biit.infographic.core.generators.JpegGenerator;
import com.biit.infographic.core.models.svg.ElementAttributes;
import com.biit.infographic.core.models.svg.SvgBackground;
import com.biit.infographic.core.models.svg.SvgTemplate;
import com.biit.infographic.core.models.svg.components.SvgCircle;
import com.biit.infographic.core.models.svg.components.SvgImage;
import org.apache.batik.transcoder.TranscoderException;
import org.apache.batik.transcoder.TranscoderInput;
import org.apache.batik.transcoder.TranscoderOutput;
import org.apache.batik.transcoder.image.JPEGTranscoder;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;

@Test(groups = {"simpleJpegGenerationTest"})
public class SimpleJpegGenerationTest {
    private static final String OUTPUT_FOLDER = System.getProperty("java.io.tmpdir") + File.separator + "JpegTests";

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
    public void createImage() throws IOException, TranscoderException {
        URL resource = getClass().getClassLoader().getResource("svg/documentDrawCircle.svg");
        String svgUriInput = resource.toString();
        TranscoderInput inputSvgImage = new TranscoderInput(svgUriInput);
        //Step-2: Define OutputStream to PNG Image and attach to TranscoderOutput
        OutputStream outputStream = new FileOutputStream(OUTPUT_FOLDER
                + File.separator + "documentResource.jpg");
        TranscoderOutput outputPngImage = new TranscoderOutput(outputStream);
        // Step-3: Create PNGTranscoder and define hints if required
        JPEGTranscoder jpegTranscoder = new JPEGTranscoder();
        // Step-4: Convert and Write output
        jpegTranscoder.transcode(inputSvgImage, outputPngImage);
        outputStream.flush();
        outputStream.close();
    }

    @Test
    public void documentBackgroundColorTest() throws IOException {
        SvgTemplate svgTemplate = new SvgTemplate();
        svgTemplate.setSvgBackground(new SvgBackground().backgroundColor("#449911"));

        Files.write(Paths.get(OUTPUT_FOLDER
                + File.separator + "documentBackgroundColor.jpg"), JpegGenerator.generate(svgTemplate));
    }

    @Test
    public void documentBackgroundImageTest() throws IOException {
        SvgTemplate svgTemplate = new SvgTemplate();
        svgTemplate.setSvgBackground(new SvgBackground().image(readBase64Image("EliseNess.txt")));

        Files.write(Paths.get(OUTPUT_FOLDER
                + File.separator + "documentBackgroundImage.jpg"), JpegGenerator.generate(svgTemplate));
    }

    @Test
    public void documentDrawCircleTest() throws IOException {
        SvgTemplate svgTemplate = new SvgTemplate();
        svgTemplate.addElement(new SvgCircle(SvgTemplate.DEFAULT_WIDTH / 2, SvgTemplate.DEFAULT_HEIGHT / 2,
                SvgTemplate.DEFAULT_WIDTH / 2));

        Files.write(Paths.get(OUTPUT_FOLDER
                + File.separator + "documentDrawCircle.jpg"), JpegGenerator.generate(svgTemplate));
    }

    @Test
    public void documentImageTest() throws IOException {
        SvgTemplate svgTemplate = new SvgTemplate();
        svgTemplate.addElement(new SvgImage(new ElementAttributes(SvgTemplate.DEFAULT_WIDTH / 2, SvgTemplate.DEFAULT_HEIGHT / 2,
                String.valueOf(SvgTemplate.DEFAULT_WIDTH / 2), String.valueOf(SvgTemplate.DEFAULT_HEIGHT / 2)), "EliseNess",
                readBase64Image("EliseNess.txt")));

        Files.write(Paths.get(OUTPUT_FOLDER
                + File.separator + "documentImage.jpg"), JpegGenerator.generate(svgTemplate));
    }

    @AfterClass
    public void removeFolder() {
        Assert.assertTrue(deleteDirectory(new File(OUTPUT_FOLDER)));
    }

}
