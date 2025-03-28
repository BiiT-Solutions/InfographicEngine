package com.biit.infographic.core.engine;

import com.biit.drools.form.DroolsSubmittedForm;
import com.biit.infographic.core.controllers.kafka.EventController;
import com.biit.infographic.core.providers.DroolsResultProvider;
import com.biit.infographic.core.providers.GeneratedInfographicProvider;
import com.biit.infographic.persistence.entities.DroolsResult;
import com.biit.infographic.persistence.entities.GeneratedInfographic;
import com.biit.kafka.config.ObjectMapperFactory;
import com.biit.kafka.events.Event;
import com.biit.kafka.events.EventCustomProperties;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Optional;

@SpringBootTest
@Test(groups = "eventController")
public class EventControllerTests extends AbstractTestNGSpringContextTests {
    private static String EVENT_CREATOR = "Thor";

    @Autowired
    private EventController eventController;

    @Autowired
    private DroolsResultProvider droolsResultProvider;

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
    public void checkEventParsing() throws JsonProcessingException {
        Event event = new Event();
        event.setPayload(readEventPayload("EventPayload.txt"));
        event.setCustomProperty(EventCustomProperties.FACT_TYPE, EventController.ALLOWED_FACT_TYPE);
        event.setCreatedBy(EVENT_CREATOR);


        eventController.eventHandler(event, "", "", 0, "", System.currentTimeMillis());

        //Ensure that result is stored.
        Optional<DroolsResult> droolsResultOptional = droolsResultProvider.findLatest(null, null, EVENT_CREATOR, null, null);
        Assert.assertTrue(droolsResultOptional.isPresent());
        final DroolsSubmittedForm droolsSubmittedFormStored = ObjectMapperFactory.getObjectMapper().readValue(
                droolsResultOptional.get().getForm(), DroolsSubmittedForm.class);
        Assert.assertEquals(droolsSubmittedFormStored.getName(), "CADT");
        Assert.assertFalse(droolsSubmittedFormStored.getFormVariables().isEmpty());
    }
}
