package com.biit.infographic.core.controllers.kafka.converter;

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
        droolsResult.setOrganizationId(droolsSubmittedForm.getOrganizationId());
        return droolsResult;
    }

    public DroolsResult getDroolsContent(Event event, DroolsSubmittedForm droolsSubmittedForm) {
        final DroolsResult receivedForm = new DroolsResult();
        receivedForm.setForm(event.getPayload().toString());
        receivedForm.setCreatedBy(event.getCreatedBy());
        receivedForm.setFormName(droolsSubmittedForm.getName());
        receivedForm.setFormVersion(droolsSubmittedForm.getVersion());
        receivedForm.setOrganizationId(droolsSubmittedForm.getOrganizationId());
        return receivedForm;
    }

    public InfographicPayload generatePayload(GeneratedInfographic generatedInfographic) {
        final InfographicPayload infographicPayload = new InfographicPayload();
        BeanUtils.copyProperties(generatedInfographic, infographicPayload);
        return infographicPayload;
    }

    public Event getInfographicEvent(GeneratedInfographic generatedInfographic, String executedBy) {
        final InfographicPayload eventPayload = generatePayload(generatedInfographic);
        final Event event = new Event(eventPayload);
        event.setCreatedBy(executedBy);
        event.setMessageId(UUID.randomUUID());
        event.setSubject(EventSubject.CREATED.toString());
        event.setContentType(MediaType.APPLICATION_XML_VALUE);
        event.setCreatedAt(LocalDateTime.now());
        event.setReplyTo(applicationName);
        event.setTag(generatedInfographic.getFormName());
        event.setCustomProperty(EventCustomProperties.FACT_TYPE, INFOGRAPHIC_VARIABLE_EVENT_TYPE);
        return event;
    }
}
