package com.biit.infographic.core.controllers.kafka;

import com.biit.infographic.core.controllers.kafka.converter.EventConverter;
import com.biit.infographic.logger.EventsLogger;
import com.biit.infographic.persistence.entities.GeneratedInfographic;
import com.biit.kafka.events.KafkaEventTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class InfographicEventSender {

    @Value("${spring.kafka.send.topic:}")
    private String sendTopic;

    private final KafkaEventTemplate kafkaTemplate;

    private final EventConverter eventConverter;

    public InfographicEventSender(@Autowired(required = false) KafkaEventTemplate kafkaTemplate, EventConverter eventConverter) {
        this.kafkaTemplate = kafkaTemplate;
        this.eventConverter = eventConverter;
    }

    public void sendResultEvents(GeneratedInfographic generatedInfographic, String executedBy, String organization, UUID sessionId) {
        EventsLogger.debug(this.getClass().getName(), "Preparing for sending events...");
        if (kafkaTemplate != null && sendTopic != null && !sendTopic.isEmpty()) {
            //Send the complete svg as an event.
            try {
                kafkaTemplate.send(sendTopic, eventConverter.getInfographicEvent(generatedInfographic, executedBy, organization, sessionId));
                EventsLogger.debug(this.getClass().getName(), "Event with results from '{}' and version '{}' send!",
                        generatedInfographic.getFormName(), generatedInfographic.getFormVersion());
            } catch (Exception e) {
                EventsLogger.errorMessage(this.getClass(), e);
            }
        } else {
            EventsLogger.warning(this.getClass().getName(), "Send topic not defined!");
        }
    }
}
