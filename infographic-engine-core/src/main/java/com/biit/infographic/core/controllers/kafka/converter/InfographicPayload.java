package com.biit.infographic.core.controllers.kafka.converter;

import com.biit.kafka.events.EventPayload;
import com.biit.server.persistence.entities.Element;

import java.util.List;

public class InfographicPayload extends Element<Long> implements EventPayload {

    private Long id;

    private String formName;

    private int formVersion;

    private Long organizationId;

    private List<String> svgContents;

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    public String getFormName() {
        return formName;
    }

    public void setFormName(String formName) {
        this.formName = formName;
    }

    public int getFormVersion() {
        return formVersion;
    }

    public void setFormVersion(int formVersion) {
        this.formVersion = formVersion;
    }

    public Long getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(Long organizationId) {
        this.organizationId = organizationId;
    }

    public List<String> getSvgContents() {
        return svgContents;
    }

    public void setSvgContents(List<String> svgContents) {
        this.svgContents = svgContents;
    }
}
