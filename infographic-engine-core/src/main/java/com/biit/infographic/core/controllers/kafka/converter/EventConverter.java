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

import com.biit.drools.form.DroolsSubmittedForm;
import com.biit.infographic.persistence.entities.DroolsResult;
import com.biit.infographic.persistence.entities.GeneratedInfographic;
import com.biit.kafka.events.Event;
import com.biit.kafka.events.EventCustomProperties;
import com.biit.kafka.events.EventSubject;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.UUID;

@Component
public class EventConverter {
    private static final String INFOGRAPHIC_VARIABLE_EVENT_TYPE = "Infographic";

    @Value("${spring.application.name:#{null}}")
    private String applicationName;


    public DroolsResult getDroolsContent(DroolsSubmittedForm droolsSubmittedForm, String executedBy) {
        final DroolsResult droolsResult = new DroolsResult();
        droolsResult.setForm(droolsSubmittedForm.generateXML());
        droolsResult.setCreatedBy(executedBy);
        droolsResult.setFormName(droolsSubmittedForm.getName());
        if (droolsSubmittedForm.getVersion() != null) {
            droolsResult.setFormVersion(droolsSubmittedForm.getVersion());
        } else {
            droolsResult.setFormVersion(1);
        }
        droolsResult.setOrganization(droolsSubmittedForm.getOrganization());
        return droolsResult;
    }


    public DroolsResult getDroolsContent(Event event, DroolsSubmittedForm droolsSubmittedForm) {
        final DroolsResult receivedForm = new DroolsResult();
        receivedForm.setForm(event.getPayload());
        receivedForm.setCreatedBy(event.getCreatedBy());
        //As Drools now can execute multiples rules from one form, the rules form name is on the event tag.
        if (event.getTag() != null) {
            receivedForm.setFormName(event.getTag());
        } else {
            receivedForm.setFormName(droolsSubmittedForm.getName());
        }
        receivedForm.setFormVersion(droolsSubmittedForm.getVersion());
        receivedForm.setOrganization(droolsSubmittedForm.getOrganization());
        receivedForm.setUnit(event.getUnit());
        return receivedForm;
    }


    public InfographicPayload generatePayload(GeneratedInfographic generatedInfographic) {
        final InfographicPayload infographicPayload = new InfographicPayload();
        BeanUtils.copyProperties(generatedInfographic, infographicPayload);
        return infographicPayload;
    }


    public Event getInfographicEvent(GeneratedInfographic generatedInfographic, String executedBy, String organization, UUID sessionId, String unit) {
        final InfographicPayload eventPayload = generatePayload(generatedInfographic);
        final Event event = new Event(eventPayload);
        event.setCreatedBy(executedBy);
        event.setMessageId(UUID.randomUUID());
        event.setSubject(EventSubject.CREATED.toString());
        event.setContentType(MediaType.APPLICATION_XML_VALUE);
        event.setCreatedAt(LocalDateTime.now());
        event.setReplyTo(applicationName);
        event.setTag(generatedInfographic.getFormName());
        event.setSessionId(sessionId);
        event.setCustomProperty(EventCustomProperties.FACT_TYPE, INFOGRAPHIC_VARIABLE_EVENT_TYPE);
        event.setOrganization(organization);
        event.setUnit(unit);
        return event;
    }
}
