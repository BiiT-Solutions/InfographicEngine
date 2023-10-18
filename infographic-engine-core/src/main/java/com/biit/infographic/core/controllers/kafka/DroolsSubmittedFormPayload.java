package com.biit.infographic.core.controllers.kafka;

import com.biit.kafka.events.EventPayload;

public class DroolsSubmittedFormPayload implements EventPayload {
    private String json;

    public DroolsSubmittedFormPayload() {

    }

    public String getJson() {
        return json;
    }

    public void setJson(String json) {
        this.json = json;
    }
}
