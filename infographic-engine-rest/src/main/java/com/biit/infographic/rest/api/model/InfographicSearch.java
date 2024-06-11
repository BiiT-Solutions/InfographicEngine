package com.biit.infographic.rest.api.model;

public class InfographicSearch {
    private String form;
    private Integer version;
    private String createdBy;

    private String organization;

    public InfographicSearch() {
        super();
    }

    public InfographicSearch(String form, Integer version, String createdBy, String organization) {
        this();
        this.form = form;
        this.version = version;
        this.createdBy = createdBy;
        this.organization = organization;
    }

    public String getForm() {
        return form;
    }

    public void setForm(String form) {
        this.form = form;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getOrganization() {
        return organization;
    }

    public void setOrganization(String organization) {
        this.organization = organization;
    }
}
