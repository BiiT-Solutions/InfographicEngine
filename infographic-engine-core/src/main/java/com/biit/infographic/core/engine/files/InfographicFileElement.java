package com.biit.infographic.core.engine.files;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;

public class InfographicFileElement {

    @JsonProperty("jsonFile")
    private String jsonFile;

    @JsonProperty("jsonVersion")
    private int jsonVersion;

    @JsonProperty("formName")
    @JsonAlias("examinationFormName")
    private String formName;

    @JsonProperty("formVersion")
    @JsonAlias("examinationFormVersion")
    private Long formVersion;

    @JsonProperty("selectable")
    private boolean selectable;

    @JsonProperty("selected")
    private boolean selected;

    @JsonProperty("isFolder")
    @JsonAlias("isGroup")
    private boolean isFolder;

    public String getJsonFile() {
        return jsonFile;
    }

    public void setJsonFile(String jsonFile) {
        this.jsonFile = jsonFile;
    }

    public int getJsonVersion() {
        return jsonVersion;
    }

    public void setJsonVersion(int version) {
        this.jsonVersion = version;
    }

    public String getFormName() {
        return formName;
    }

    public Long getFormVersion() {
        return formVersion;
    }

    public void setFormName(String formName) {
        this.formName = formName;
    }

    public void setFormVersion(Long formVersion) {
        this.formVersion = formVersion;
    }

    public boolean isSelectable() {
        return selectable;
    }

    public void setSelectable(boolean selectable) {
        this.selectable = selectable;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public boolean isFolder() {
        return isFolder;
    }

    public void setFolder(boolean isGroup) {
        this.isFolder = isGroup;
    }

    public String getCompleteName() {
        return getJsonFile() + "_v" + getJsonVersion();
    }

    @Override
    public String toString() {
        return "(" + getCompleteName() + ")";
    }
}
