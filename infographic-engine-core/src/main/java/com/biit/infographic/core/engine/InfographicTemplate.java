package com.biit.infographic.core.engine;

import com.biit.infographic.core.engine.files.InfographicIndexFile;

public class InfographicTemplate {
    private InfographicIndexFile indexFile;
    private String template;

    public InfographicTemplate() {
    }

    public InfographicIndexFile getIndexFile() {
        return indexFile;
    }

    public void setIndexFile(InfographicIndexFile indexFile) {
        this.indexFile = indexFile;
    }

    public String getTemplate() {
        return template;
    }

    public void setTemplate(String template) {
        this.template = template;
    }
}
