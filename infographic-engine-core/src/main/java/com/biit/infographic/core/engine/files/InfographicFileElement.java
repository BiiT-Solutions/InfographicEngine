package com.biit.infographic.core.engine.files;

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
