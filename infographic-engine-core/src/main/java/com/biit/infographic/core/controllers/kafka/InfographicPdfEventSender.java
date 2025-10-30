package com.biit.infographic.core.controllers.kafka;

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

import com.biit.infographic.core.controllers.kafka.converter.PdfInfographicEventConverter;
import com.biit.infographic.persistence.entities.GeneratedInfographic;
import com.biit.kafka.events.KafkaEventTemplate;
import com.biit.kafka.logger.EventsLogger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@ConditionalOnExpression("${spring.kafka.enabled:false}")
public class InfographicPdfEventSender {

    @Value("${spring.kafka.send.pdf.topic:}")
    private String sendTopic;

    private final PdfInfographicEventConverter pdfInfographicEventConverter;

    private final KafkaEventTemplate kafkaTemplate;

    private InfographicPdfEventSender() {
        pdfInfographicEventConverter = null;
        kafkaTemplate = null;
    }

    @Autowired(required = false)
    public InfographicPdfEventSender(PdfInfographicEventConverter pdfInfographicEventConverter, KafkaEventTemplate kafkaTemplate) {
        this.pdfInfographicEventConverter = pdfInfographicEventConverter;
        this.kafkaTemplate = kafkaTemplate;
    }


    public void sendPdfInfographic(GeneratedInfographic generatedInfographic, byte[] pdfData, UUID sessionId,
                                   String organization, String unit) {
        EventsLogger.debug(this.getClass().getName(), "Preparing for sending events...");
        if (kafkaTemplate != null && sendTopic != null && !sendTopic.isEmpty()) {
            kafkaTemplate.send(sendTopic, pdfInfographicEventConverter.getPdfEvent(pdfData, generatedInfographic, sessionId, organization, unit));
            EventsLogger.debug(this.getClass().getName(), "Event with pdf from '{}' and version '{}' send!",
                    generatedInfographic.getFormName(), generatedInfographic.getFormVersion());
        }
    }
}
