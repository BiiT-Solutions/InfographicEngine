package com.biit.infographic.core.models;

import com.biit.server.controllers.models.ElementDTO;

import java.util.List;

public class GeneratedInfographicDTO extends ElementDTO<Long> {

    private Long id;

    private String formName;

    private int formVersion;

    private Long organizationId;

    private List<String> svgContents;

    private List<String> jsonContents;

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

    public List<String> getJsonContents() {
        return jsonContents;
    }

    public void setJsonContents(List<String> jsonContents) {
        this.jsonContents = jsonContents;
    }

    @Override
    public String toString() {
        return "GeneratedInfographicDTO{"
                + "id=" + id
                + ", formName='" + formName + '\''
                + ", formVersion=" + formVersion
                +
                '}';
    }
}
