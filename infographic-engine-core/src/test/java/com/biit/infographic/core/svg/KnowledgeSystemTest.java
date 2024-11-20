package com.biit.infographic.core.svg;

import com.biit.drools.form.DroolsSubmittedForm;
import com.biit.infographic.core.controllers.DroolsResultController;
import com.biit.infographic.core.models.svg.SvgTemplate;
import com.biit.infographic.core.models.svg.components.text.FontWeight;
import com.biit.infographic.core.models.svg.components.text.SvgText;
import com.biit.ks.client.TestTextClient;
import com.biit.ks.dto.TextDTO;
import com.biit.ks.dto.TextLanguagesDTO;
import com.biit.ks.models.ITextClient;
import com.biit.server.client.user.AuthenticatedUser;
import com.biit.usermanager.client.providers.AuthenticatedUserProvider;
import com.biit.utils.file.FileReader;
import org.apache.commons.lang3.LocaleUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
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
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;


@SpringBootTest
@Test(groups = "knowledgeSystem")
public class KnowledgeSystemTest extends AbstractTestNGSpringContextTests {
    protected static final String OUTPUT_FOLDER = System.getProperty("java.io.tmpdir") + File.separator + "SvgTests";
    protected static final String TEMPLATE_ID = "ks_test";

    private static final String KNOWLEDGE_SYSTEM_TEXT_TO_REPLACE = "#DROOLS%KnowledgeSystemTest%Content#";
    private static final String KNOWLEDGE_SYSTEM_TEXT_NAME = "id123";

    private static final String DROOLS_FORM_FILE_PATH = "drools/KnowledgeSystemTest.json";

    private final static String TEXT_LA = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Donec tortor sem, pharetra vel ornare quis, cursus sed nibh.";
    private final static String TEXT_ES = "El cliente es muy importante, el cliente será seguido por el cliente. Hasta el torturador, la aljaba o el adorno, el curso pero el nibh.";

    //The one that has submitted the drools json.
    private final static String USER_NAME = "Chuck Norris";


    @Autowired
    private DroolsResultController droolsResultController;

    @Autowired
    private AuthenticatedUserProvider authenticatedUserProvider;

    @Autowired
    private ITextClient textClient;

    private AuthenticatedUser user;

    private SvgTemplate ksTemplate;

    protected boolean deleteDirectory(File directoryToBeDeleted) {
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

    @BeforeClass
    public void setup() {
        final TextDTO content = new TextDTO();
        content.setName(KNOWLEDGE_SYSTEM_TEXT_NAME);
        content.addContent(TextLanguagesDTO.LA, TEXT_LA);
        content.addContent(TextLanguagesDTO.ES, TEXT_ES);
        ((TestTextClient) textClient).setContent(content);
    }

    @BeforeClass
    public void createUser() throws IOException {
        user = (AuthenticatedUser) authenticatedUserProvider.createUser(USER_NAME, USER_NAME, "123456");
        user.setLocale(LocaleUtils.toLocale("es_ES"));
        authenticatedUserProvider.updateUser(user);
    }

    @Test
    public void generateInfographic() throws IOException {
        ksTemplate = new SvgTemplate();
        ksTemplate.setUuid(TEMPLATE_ID);
        ksTemplate.getElementAttributes().setHeight(480L);
        ksTemplate.getElementAttributes().setWidth(640L);

        final SvgText svgText = new SvgText(KNOWLEDGE_SYSTEM_TEXT_TO_REPLACE, 67, FontWeight.BOLD, 50L, 50L);
        svgText.setFontSize(12);
        svgText.setMaxLineWidth(500);
        ksTemplate.addElements(List.of(svgText));
    }


    @Test(dependsOnMethods = "generateInfographic")
    public void executeInfographic() throws IOException {
        final DroolsSubmittedForm droolsSubmittedForm = DroolsSubmittedForm.getFromJson(FileReader.getResource(DROOLS_FORM_FILE_PATH, StandardCharsets.UTF_8));
        final List<String> svgResults = droolsResultController.execute(droolsSubmittedForm, Collections.singletonList(ksTemplate));
        Assert.assertEquals(svgResults.size(), 1);

        try (PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(OUTPUT_FOLDER
                + File.separator + "knowledgeSystemFromDrools.svg")), true)) {
            out.println(svgResults.get(0));
        }

        checkContent(svgResults.get(0), "knowledgeSystemFromDrools.svg");
    }


    @AfterClass
    public void removeFolder() {
        Assert.assertTrue(deleteDirectory(new File(OUTPUT_FOLDER)));
    }

    @AfterClass
    public void removeUser() {
        authenticatedUserProvider.delete(user);
    }
}
