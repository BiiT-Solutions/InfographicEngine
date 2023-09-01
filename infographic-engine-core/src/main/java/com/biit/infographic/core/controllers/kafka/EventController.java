package com.biit.infographic.core.controllers.kafka;

import com.biit.drools.form.DroolsSubmittedForm;
import com.biit.form.submitted.implementation.SubmittedForm;
import com.biit.infographic.core.controllers.DroolsResultController;
import com.biit.infographic.core.controllers.kafka.converter.EventConverter;
import com.biit.infographic.logger.InfographicEngineLogger;
import com.biit.infographic.persistence.repositories.DroolsResultRepository;
import com.biit.kafka.consumers.EventListener;
import com.biit.kafka.events.Event;
import com.biit.kafka.events.EventCustomProperties;
import org.springframework.stereotype.Controller;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.TimeZone;


@Controller
public class EventController {

    private final EventConverter eventConverter;

    private final DroolsResultRepository droolsResultRepository;

    private final DroolsResultController droolsResultController;


    public EventController(EventListener eventListener,
                           EventConverter eventConverter,
                           DroolsResultRepository droolsResultRepository,
                           DroolsResultController droolsResultController) {
        this.eventConverter = eventConverter;
        this.droolsResultRepository = droolsResultRepository;
        this.droolsResultController = droolsResultController;

        //Listen to topic
        eventListener.addListener((event, offset, key, partition, topic, timeStamp) -> {
            eventHandler(event, key, partition, topic, timeStamp);
        });
    }

    private void eventHandler(Event event, String key, int partition, String topic, long timeStamp) {
        InfographicEngineLogger.debug(this.getClass(), "Received event '{}' on topic '{}', key '{}', partition '{}' at '{}'",
                event, topic, key, partition, LocalDateTime.ofInstant(Instant.ofEpochMilli(timeStamp),
                        TimeZone.getDefault().toZoneId()));

        final String createdBy = event.getCustomProperties().get(EventCustomProperties.ISSUER.getTag()) != null
                ? event.getCustomProperties().get(EventCustomProperties.ISSUER.getTag())
                : event.getCreatedBy();

        try {
            //Event is a FormResult
            final DroolsSubmittedForm droolsForm = getDroolsForm(event);
            InfographicEngineLogger.debug(this.getClass(), "Received Drools Result '{}'.", droolsForm.getName());
            droolsResultRepository.save(eventConverter.getDroolsContent(event, droolsForm));
            InfographicEngineLogger.debug(this.getClass(), "Drools Result '{}' saved.", droolsForm.getName());
            droolsResultController.process(droolsForm, createdBy);
        } catch (Exception e) {
            //Not a FormResult but maybe a SubmittedForm.
            InfographicEngineLogger.severe(this.getClass(), "Invalid event received!!\n" + event);
        }
    }


    private DroolsSubmittedForm getDroolsForm(Event event) {
        return event.getEntity(DroolsSubmittedForm.class);
    }

    private SubmittedForm getSubmittedForm(Event event) {
        return event.getEntity(SubmittedForm.class);
    }

}
