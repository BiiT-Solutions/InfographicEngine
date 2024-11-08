package com.biit.infographic.core.controllers.kafka;

import com.biit.infographic.core.controllers.kafka.converter.PdfInfographicEventConverter;
import com.biit.infographic.persistence.entities.GeneratedInfographic;
import com.biit.kafka.events.KafkaEventTemplate;
import com.biit.kafka.logger.EventsLogger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class InfographicPdfEventSender {

    @Value("${spring.kafka.send.topic:}")
    private String sendTopic;

    private final PdfInfographicEventConverter pdfInfographicEventConverter;

    private final KafkaEventTemplate kafkaTemplate;

    public InfographicPdfEventSender(PdfInfographicEventConverter pdfInfographicEventConverter, KafkaEventTemplate kafkaTemplate) {
        this.pdfInfographicEventConverter = pdfInfographicEventConverter;

        this.kafkaTemplate = kafkaTemplate;
    }


    public void sendPdfInfographic(GeneratedInfographic generatedInfographic, byte[] pdfData, UUID sessionId, String organization) {
        EventsLogger.debug(this.getClass().getName(), "Preparing for sending events...");
        if (kafkaTemplate != null && sendTopic != null && !sendTopic.isEmpty()) {
            kafkaTemplate.send(sendTopic, pdfInfographicEventConverter.getPdfEvent(pdfData, generatedInfographic, sessionId, organization));
            EventsLogger.debug(this.getClass().getName(), "Event with pdf from '{}' and version '{}' send!",
                    generatedInfographic.getFormName(), generatedInfographic.getFormVersion());
        }
    }
}
