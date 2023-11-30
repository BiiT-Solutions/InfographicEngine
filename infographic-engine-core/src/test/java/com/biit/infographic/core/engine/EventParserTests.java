package com.biit.infographic.core.engine;

import com.biit.drools.form.DroolsSubmittedForm;
import com.biit.kafka.config.ObjectMapperFactory;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;

@Test(groups = "eventParser")
public class EventParserTests {

    private String readEventPayload(String resourceFile) {
        try {
            return new String(Files.readAllBytes(Paths.get(getClass().getClassLoader()
                    .getResource("events" + File.separator + resourceFile).toURI()))).trim();
        } catch (IOException | URISyntaxException e) {
            Assert.fail();
        }
        return null;
    }


    @Test
    public void parseEvent() throws JsonProcessingException {
        final DroolsSubmittedForm droolsSubmittedForm = ObjectMapperFactory.getObjectMapper().readValue(
                readEventPayload("EventPayload.txt"), DroolsSubmittedForm.class);

        Assert.assertEquals(droolsSubmittedForm.getName(), "CADT");
        Assert.assertFalse(droolsSubmittedForm.getFormVariables().isEmpty());
    }
}
