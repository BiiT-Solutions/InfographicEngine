package com.biit.infographic.core.engine;

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

import com.biit.drools.form.DroolsSubmittedCategory;
import com.biit.drools.form.DroolsSubmittedForm;
import com.biit.drools.form.DroolsSubmittedQuestion;
import com.biit.infographic.core.controllers.GeneratedInfographicController;
import com.biit.infographic.core.converters.GeneratedInfographicConverter;
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
    private GeneratedInfographicProvider generatedInfographicProvider;

    @Autowired
    private GeneratedInfographicController generatedInfographicController;

    @Autowired
    private GeneratedInfographicConverter generatedInfographicConverter;

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
        generatedInfographicProvider.save(generatedInfographicConverter.reverse(generatedInfographicController.process(droolsSubmittedForm, null, USER, null, null, null, null)));
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
