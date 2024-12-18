package com.biit.infographic.core.engine;

import com.biit.drools.form.DroolsSubmittedCategory;
import com.biit.drools.form.DroolsSubmittedForm;
import com.biit.drools.form.DroolsSubmittedQuestion;
import com.biit.infographic.core.controllers.DroolsResultController;
import com.biit.infographic.core.controllers.GeneratedInfographicController;
import com.biit.infographic.core.models.GeneratedInfographicAsJpegDTO;
import com.biit.infographic.core.models.GeneratedInfographicAsPngDTO;
import com.biit.infographic.core.providers.GeneratedInfographicProvider;
import com.biit.infographic.persistence.entities.GeneratedInfographic;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;

@SpringBootTest
@Test(groups = "templateFromDrools")
public class TemplateFromDroolsTests extends AbstractTestNGSpringContextTests {

    private static final String APPLICATION_NAME = "Test";
    private static final String FORM_NAME = "TheForm";
    private static final Integer FORM_VERSION = 3;
    private static final String FORM_ORGANIZATION = "The Organization";

    private static final String DROOLS_VARIABLE_NAME = "Score";
    private static final String USER = "dummy@user.com";

    @Autowired
    private DroolsResultController droolsResultController;

    @Autowired
    private GeneratedInfographicProvider generatedInfographicProvider;

    @Autowired
    private GeneratedInfographicController generatedInfographicController;

    private DroolsSubmittedForm droolsSubmittedForm;

    private String readDrools(String fileName) {
        try {
            return new String(Files.readAllBytes(Paths.get(getClass().getClassLoader()
                    .getResource("drools" + File.separator + fileName).toURI())));
        } catch (Exception e) {
            Assert.fail("Cannot read resource 'drools/" + fileName + "'.");
        }
        return null;
    }

    private void checkContent(String content, String resourceFile) {
        try {
            Assert.assertEquals(content.trim(), new String(Files.readAllBytes(Paths.get(getClass().getClassLoader()
                    .getResource("results" + File.separator + resourceFile).toURI()))).trim());
        } catch (IOException | URISyntaxException e) {
            Assert.fail();
        }
    }

    @BeforeClass
    private void loadSubmittedForm() throws JsonProcessingException {
        droolsSubmittedForm = new DroolsSubmittedForm();
        droolsSubmittedForm.setApplicationName(APPLICATION_NAME);
        droolsSubmittedForm.setTag(FORM_NAME);
        droolsSubmittedForm.setVersion(FORM_VERSION);
        droolsSubmittedForm.setOrganization(FORM_ORGANIZATION);

        DroolsSubmittedCategory category = new DroolsSubmittedCategory("The Category");
        droolsSubmittedForm.addChild(category);
        DroolsSubmittedQuestion question = new DroolsSubmittedQuestion("The Question");
        question.addAnswer("One");
        question.addAnswer("Two");
        question.addAnswer("Three");
        category.addChild(question);

        droolsSubmittedForm.setVariableValue(DROOLS_VARIABLE_NAME, 42);
    }

    @Test
    public void checkDroolsSubmittedForm() {
        droolsResultController.process(droolsSubmittedForm, null, USER, null, null, null);
    }

    @Test(dependsOnMethods = "checkDroolsSubmittedForm")
    public void readFromDatabase() {
        final GeneratedInfographic generatedInfographic = generatedInfographicProvider.findLatest(FORM_NAME, FORM_VERSION, USER, FORM_ORGANIZATION, null).orElse(null);
        Assert.assertNotNull(generatedInfographic);
        Assert.assertEquals(generatedInfographic.getSvgContents().size(), 2);
        checkContent(generatedInfographic.getSvgContents().get(0), "title.svg");
        checkContent(generatedInfographic.getSvgContents().get(1), "scoringTest.svg");
    }

    @Test(dependsOnMethods = "checkDroolsSubmittedForm")
    public void generatedInfographicAsPngDTO() {
        final GeneratedInfographicAsPngDTO generatedInfographicAsPngDTO = GeneratedInfographicAsPngDTO.from(generatedInfographicController.findLatest(FORM_NAME, FORM_VERSION, FORM_ORGANIZATION, null, USER));
        Assert.assertNotNull(generatedInfographicAsPngDTO.getContents());
    }

    @Test(dependsOnMethods = "checkDroolsSubmittedForm")
    public void generatedInfographicAJpgDTO() {
        final GeneratedInfographicAsJpegDTO generatedInfographicAsPngDTO = GeneratedInfographicAsJpegDTO.from(generatedInfographicController.findLatest(FORM_NAME, FORM_VERSION, FORM_ORGANIZATION, null, USER));
        Assert.assertNotNull(generatedInfographicAsPngDTO.getContents());
    }

}
