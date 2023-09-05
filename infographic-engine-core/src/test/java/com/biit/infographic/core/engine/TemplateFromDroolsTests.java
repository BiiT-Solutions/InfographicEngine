package com.biit.infographic.core.engine;

import com.biit.drools.form.DroolsSubmittedForm;
import com.biit.infographic.core.controllers.DroolsResultController;
import com.biit.infographic.core.models.svg.serialization.ObjectMapperFactory;
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
public class TemplateFromDroolsTests {

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
        droolsSubmittedForm = ObjectMapperFactory.getObjectMapper().readValue(readDrools("droolsSubmittedForm.json"), DroolsSubmittedForm.class);
    }

    @Test
    public void checkDroolsSubmittedForm() {
        Assert.assertEquals(droolsSubmittedForm,1);
    }

}
