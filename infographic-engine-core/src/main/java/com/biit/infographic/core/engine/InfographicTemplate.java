package com.biit.infographic.core.engine;

import com.biit.infographic.core.engine.files.InfographicFileElement;

public class InfographicTemplate {
    private InfographicFileElement indexFile;
    private String template;

    public InfographicTemplate() {
    }

    public InfographicTemplate(InfographicFileElement indexFile, String template) {
        this.indexFile = indexFile;
        this.template = template;
    }

    public InfographicFileElement getIndexFile() {
        return indexFile;
    }

    public void setIndexFile(InfographicFileElement indexFile) {
        this.indexFile = indexFile;
    }

    public String getTemplate() {
        return template;
    }

    public void setTemplate(String template) {
        this.template = template;
    }
}
