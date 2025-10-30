package com.biit.infographic.core.controllers.kafka.converter;

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

import com.biit.infographic.persistence.entities.GeneratedInfographic;
import com.biit.kafka.events.Event;
import com.biit.kafka.events.EventCustomProperties;
import com.biit.kafka.events.EventSubject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.UUID;

@Component
public class PdfInfographicEventConverter {
    private static final String INFOGRAPHIC_EVENT_TYPE = "PdfInfographic";

    @Value("${spring.application.name:#{null}}")
    private String applicationName;

    public PdfFormPayload generatePayload(byte[] pdfInfographic, GeneratedInfographic generatedInfographic) {
        final PdfFormPayload infographicPayload = new PdfFormPayload();
        infographicPayload.setFormName(generatedInfographic.getFormName());
        infographicPayload.setFormVersion(generatedInfographic.getVersion());
        infographicPayload.setPdfContent(pdfInfographic);
        return infographicPayload;
    }

    public Event getPdfEvent(byte[] pdfData, GeneratedInfographic generatedInfographic, UUID sessionId, String organization, String unit) {
        final PdfFormPayload eventPayload = generatePayload(pdfData, generatedInfographic);
        final Event event = new Event(eventPayload);
        event.setCreatedBy(generatedInfographic.getCreatedBy());
        event.setMessageId(UUID.randomUUID());
        event.setSubject(EventSubject.REPORT.toString());
        event.setContentType(MediaType.APPLICATION_OCTET_STREAM_VALUE);
        event.setCreatedAt(LocalDateTime.now());
        event.setReplyTo(applicationName);
        event.setTag(generatedInfographic.getFormName());
        event.setSessionId(sessionId);
        event.setCustomProperty(EventCustomProperties.FACT_TYPE, INFOGRAPHIC_EVENT_TYPE);
        event.setOrganization(organization);
        event.setUnit(unit);
        return event;
    }
}
