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

import com.biit.infographic.core.controllers.kafka.converter.EventConverter;
import com.biit.infographic.logger.EventsLogger;
import com.biit.infographic.persistence.entities.GeneratedInfographic;
import com.biit.kafka.events.KafkaEventTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@ConditionalOnExpression("${spring.kafka.enabled:false}")
public class InfographicEventSender {

    @Value("${spring.kafka.send.topic:}")
    private String sendTopic;

    private final KafkaEventTemplate kafkaTemplate;

    private final EventConverter eventConverter;

    private InfographicEventSender(EventConverter eventConverter) {
        kafkaTemplate = null;
        this.eventConverter = eventConverter;
    }

    @Autowired(required = false)
    public InfographicEventSender(KafkaEventTemplate kafkaTemplate, EventConverter eventConverter) {
        this.kafkaTemplate = kafkaTemplate;
        this.eventConverter = eventConverter;
    }

    public void sendResultEvents(GeneratedInfographic generatedInfographic, String executedBy, String organization, UUID sessionId,
                                 String unit) {
        EventsLogger.debug(this.getClass().getName(), "Preparing for sending events...");
        if (kafkaTemplate != null && sendTopic != null && !sendTopic.isEmpty()) {
            //Send the complete svg as an event.
            try {
                kafkaTemplate.send(sendTopic, eventConverter.getInfographicEvent(generatedInfographic, executedBy, organization, sessionId, unit));
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
