package com.biit.infographic.core.controllers.kafka;

import com.biit.drools.baseform.core.converters.EventConverter;
import com.biit.drools.baseform.logger.BaseFormDroolsEngineLogger;
import com.biit.drools.form.DroolsSubmittedForm;
import com.biit.kafka.events.KafkaEventTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Map;

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

    public void sendResultEvents(DroolsSubmittedForm response, String executedBy) {
        BaseFormDroolsEngineLogger.debug(this.getClass().getName(), "Preparing for sending events...");
        if (sendTopic != null && !sendTopic.isEmpty()) {
            //Send the complete form as an event.
            kafkaTemplate.send(sendTopic, eventConverter.getEvent(response, executedBy));
            BaseFormDroolsEngineLogger.debug(this.getClass().getName(), "Event with results from '{}' send!", response.getName());

            //Send formVariables as event.
            if (response.getVariablesValue() != null) {
                BaseFormDroolsEngineLogger.debug(this.getClass().getName(), "Variables obtained by drools are:\n{}\n", response.getVariablesValue().entrySet());
                for (Map.Entry<String, Object> variable : response.getVariablesValue().entrySet()) {
                    kafkaTemplate.send(sendTopic, eventConverter.getVariableEvent(variable.getKey(), variable.getValue(), executedBy));
                    BaseFormDroolsEngineLogger.debug(this.getClass().getName(), "Event with variable '{}' and value '{}' send!",
                            variable.getKey(), variable.getValue());
                }
            }
        } else {
            BaseFormDroolsEngineLogger.warning(this.getClass().getName(), "Send topic not defined!");
        }
    }
}
