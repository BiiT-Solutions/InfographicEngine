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
