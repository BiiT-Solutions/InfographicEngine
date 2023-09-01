package com.biit.infographic.core.controllers.kafka.converter;

import com.biit.drools.form.DroolsSubmittedForm;
import com.biit.form.result.FormResult;
import com.biit.infographic.persistence.entities.DroolsResult;
import com.biit.kafka.events.Event;
import org.springframework.beans.factory.annotation.Value;

public class EventConverter {

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
}
