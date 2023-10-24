package com.biit.infographic.core.models;

import com.biit.server.controllers.models.ElementDTO;

import java.util.List;

public class GeneratedInfographicDTO extends ElementDTO<Long> {

    private String formName;

    private int formVersion;

    private Long organizationId;

    private String createdBy;

    private List<String> svgContents;

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

    @Override
    public String getCreatedBy() {
        return createdBy;
    }

    @Override
    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public List<String> getSvgContents() {
        return svgContents;
    }

    public void setSvgContents(List<String> svgContents) {
        this.svgContents = svgContents;
    }
}
