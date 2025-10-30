package com.biit.infographic.core.engine;

/*-
 * #%L
 * Infographic Engine v2 (Core)
 * %%
 * Copyright (C) 2022 - 2025 BiiT Sourcing Solutions S.L.
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * #L%
 */

import com.biit.infographic.core.models.svg.serialization.ObjectMapperFactory;
import com.biit.infographic.logger.InfographicEngineLogger;
import com.biit.utils.file.FileReader;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;

import java.io.IOException;
import java.util.Map;

/**
 * This class represents an Infographic with the Parameters and its values.
 */
public class InfographicTemplateAndContent {

    @JsonProperty("name")
    private String name;

    @JsonProperty("template")
    private String template;

    @JsonProperty("content")
    // key is "#" + type + "%" + name + "%" + key + "#"
    private Map<String, String> content;

    public InfographicTemplateAndContent(String name) {
        this.name = name;
    }

    public InfographicTemplateAndContent(String name, String jsonTemplate, Map<String, String> content) {
        this.name = name;
        this.template = jsonTemplate;
        this.content = content;
    }

    /**
     * Sets the content from a list of keys and a list of values
     *
     * @param content Elements that must be substituted by a text;
     */
    public void setContent(Map<String, String> content) {
        this.content = content;
    }

    public void setTemplateFromResource(String resourceName) throws IOException {
        template = FileReader.readFile(FileReader.getResource(resourceName));
    }

    public void setTemplate(String template) {
        this.template = template;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTemplate() {
        return replaceInvalidChars(template);
    }

    /**
     * Updates the template parameters with the drools content.
     *
     * @return the template updated.
     */
    public String getProcessedTemplate() {
        return replaceInvalidChars(replaceContent(template));
    }

    public Map<String, String> getContent() {
        return content;
    }

    public String toJson() {
        try {
            return ObjectMapperFactory.getObjectMapper().writeValueAsString(this);
        } catch (JsonProcessingException e) {
            InfographicEngineLogger.errorMessage(this.getClass(), e);
        }
        return null;
    }

    private String replaceInvalidChars(String text) {
        if (text == null) {
            return "";
        }
        return text.replace("\n", "").replace("\r", "");
    }

    private String replaceContent(String text) {
        for (Map.Entry<String, String> value : content.entrySet()) {
            text = text.replace(value.getKey(), value.getValue() != null ? value.getValue() : "");
        }
        return text;
    }

    @Override
    public String toString() {
        if (name != null && !name.isEmpty()) {
            return name;
        }
        return super.toString();
    }
}
