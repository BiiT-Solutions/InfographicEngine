package com.biit.infographic.core.engine;

import com.biit.drools.form.DroolsSubmittedCategory;
import com.biit.drools.form.DroolsSubmittedForm;
import com.biit.drools.form.DroolsSubmittedQuestion;
import com.biit.infographic.core.controllers.DroolsResultController;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;

@SpringBootTest
@Test(groups = "templateFromDrools")
public class TemplateFromDroolsTests extends AbstractTestNGSpringContextTests {

    private final static String APPLICATION_NAME = "Test";
    private final static String FORM_NAME = "TheForm";
    private final static Integer FORM_VERSION = 3;
    private final static Long FORM_ORGANIZATION = 23L;

    private final static String DROOLS_VARIABLE_NAME = "Score";

    @Autowired
    private DroolsResultController droolsResultController;

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

    @BeforeClass
    private void loadSubmittedForm() throws JsonProcessingException {
        droolsSubmittedForm = new DroolsSubmittedForm();
        droolsSubmittedForm.setApplicationName(APPLICATION_NAME);
        droolsSubmittedForm.setTag(FORM_NAME);
        droolsSubmittedForm.setVersion(FORM_VERSION);
        droolsSubmittedForm.setOrganizationId(FORM_ORGANIZATION);

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
        droolsResultController.process(droolsSubmittedForm, null);
    }

}
