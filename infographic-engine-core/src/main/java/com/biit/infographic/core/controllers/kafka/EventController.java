package com.biit.infographic.core.controllers.kafka;

import com.biit.drools.form.DroolsSubmittedForm;
import com.biit.infographic.core.controllers.DroolsResultController;
import com.biit.infographic.core.controllers.kafka.converter.EventConverter;
import com.biit.infographic.core.models.svg.serialization.ObjectMapperFactory;
import com.biit.infographic.logger.EventsLogger;
import com.biit.infographic.persistence.repositories.DroolsResultRepository;
import com.biit.kafka.consumers.EventListener;
import com.biit.kafka.events.Event;
import com.biit.kafka.events.EventCustomProperties;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.stereotype.Controller;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.TimeZone;


@Controller
public class EventController {
    public static final String ALLOWED_FACT_TYPE = "DroolsResultForm";

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
        eventListener.addListener((event, offset, key, partition, topic, timeStamp) ->
                eventHandler(event, key, partition, topic, timeStamp));
    }

    public void eventHandler(Event event, String key, int partition, String topic, long timeStamp) {
        EventsLogger.debug(this.getClass(), "Received event '{}' on topic '{}', key '{}', partition '{}' at '{}'",
                event, topic, key, partition, LocalDateTime.ofInstant(Instant.ofEpochMilli(timeStamp),
                        TimeZone.getDefault().toZoneId()));

        final String createdBy = event.getCustomProperties().get(EventCustomProperties.ISSUER.getTag()) != null
                ? event.getCustomProperties().get(EventCustomProperties.ISSUER.getTag())
                : event.getCreatedBy();

        try {
            if (event.getCustomProperties() != null) {
                if (!Objects.equals(event.getCustomProperty(EventCustomProperties.FACT_TYPE), ALLOWED_FACT_TYPE)) {
                    EventsLogger.debug(this.getClass(), "Event ignored.");
                    return;
                }
            }
            final DroolsSubmittedForm droolsForm = getDroolsForm(event);
            EventsLogger.info(this.getClass(), "Received Drools Result '{}'.", droolsForm.getName());
            droolsResultRepository.save(eventConverter.getDroolsContent(event, droolsForm));
            EventsLogger.debug(this.getClass(), "Drools Result '{}' saved.", droolsForm.getName());
            droolsResultController.process(droolsForm, createdBy);
        } catch (JsonProcessingException e) {
            EventsLogger.severe(this.getClass(), "Event cannot be parsed!!\n" + event);
            EventsLogger.errorMessage(this.getClass(), e);
        } catch (Exception e) {
            EventsLogger.severe(this.getClass(), "Invalid event received!!\n" + event);
            EventsLogger.errorMessage(this.getClass(), e);
        }
    }

    private DroolsSubmittedForm getDroolsForm(Event event) throws JsonProcessingException {
        return ObjectMapperFactory.getObjectMapper().readValue(getDroolsEventPayload(event).getJson(), DroolsSubmittedForm.class);
    }


    private DroolsSubmittedFormPayload getDroolsEventPayload(Event event) {
        return event.getEntity(DroolsSubmittedFormPayload.class);
    }
}
