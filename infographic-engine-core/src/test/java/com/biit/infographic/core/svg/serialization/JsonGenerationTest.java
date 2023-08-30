package com.biit.infographic.core.svg.serialization;

import com.biit.infographic.core.models.svg.SvgBackground;
import com.biit.infographic.core.models.svg.SvgTemplate;
import com.biit.infographic.core.models.svg.components.SvgRectangle;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;

@Test(groups = "jsonGeneration")
public class JsonGenerationTest {
    private static final String OUTPUT_FOLDER = System.getProperty("java.io.tmpdir") + File.separator + "JsonTests";
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
    }

    @Test
    public void documentDrawRectangleTest() throws IOException {
        SvgTemplate svgTemplate = new SvgTemplate();
        svgTemplate.addElement(new SvgRectangle(SvgTemplate.DEFAULT_WIDTH / 2, SvgTemplate.DEFAULT_HEIGHT / 2,
                String.valueOf(SvgTemplate.DEFAULT_WIDTH / 2), String.valueOf(SvgTemplate.DEFAULT_HEIGHT / 2), "ff0000"));
        String jsonText = generateJson(svgTemplate);

        SvgTemplate svgTemplate1 = objectMapper.readValue(jsonText, SvgTemplate.class);
    }

    @AfterClass(enabled = false)
    public void removeFolder() {
        Assert.assertTrue(deleteDirectory(new File(OUTPUT_FOLDER)));
    }
}
