package com.biit.infographic.core.models;

import com.biit.server.controllers.models.ElementDTO;

import java.util.List;

public class GeneratedInfographicAsImageDTO extends ElementDTO<Long> {

    private Long id;

    private String formName;

    private int formVersion;

    private String organization;

    private String createdBy;

    private List<byte[]> contents;

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

    public String getOrganization() {
        return organization;
    }

    public void setOrganization(String organization) {
        this.organization = organization;
    }

    @Override
    public String getCreatedBy() {
        return createdBy;
    }

    @Override
    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public List<byte[]> getContents() {
        return contents;
    }

    public void setContents(List<byte[]> contents) {
        this.contents = contents;
    }

}
