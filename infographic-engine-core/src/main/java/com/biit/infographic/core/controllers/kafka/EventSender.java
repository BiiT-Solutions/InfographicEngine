package com.biit.infographic.core.controllers.kafka;

import com.biit.infographic.core.controllers.kafka.converter.EventConverter;
import com.biit.infographic.logger.EventsLogger;
import com.biit.infographic.persistence.entities.GeneratedInfographic;
import com.biit.kafka.events.KafkaEventTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class EventSender {

    @Value("${spring.kafka.send.topic:}")
    private String sendTopic;

    private final KafkaEventTemplate kafkaTemplate;

    private final EventConverter eventConverter;

    public EventSender(KafkaEventTemplate kafkaTemplate, EventConverter eventConverter) {
        this.kafkaTemplate = kafkaTemplate;
        this.eventConverter = eventConverter;
    }

    public void sendResultEvents(GeneratedInfographic generatedInfographic, String executedBy) {
        EventsLogger.debug(this.getClass().getName(), "Preparing for sending events...");
        if (sendTopic != null && !sendTopic.isEmpty()) {
            //Send the complete svg as an event.
            kafkaTemplate.send(sendTopic, eventConverter.getInfographicEvent(generatedInfographic, executedBy));
            EventsLogger.debug(this.getClass().getName(), "Event with results from '{}' and version '{}' send!",
                    generatedInfographic.getFormName(), generatedInfographic.getFormVersion());
        } else {
            EventsLogger.warning(this.getClass().getName(), "Send topic not defined!");
        }
    }
}
