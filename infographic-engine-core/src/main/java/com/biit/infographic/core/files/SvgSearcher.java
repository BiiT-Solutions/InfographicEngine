package com.biit.infographic.core.files;

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

import java.io.InputStream;
import java.util.List;

public final class SvgSearcher {

    private static final String SYSTEM_VARIABLE_FILES_LOCATION = "SVG_FOLDER";

    private SvgSearcher() {
        super();
    }

    public static List<String> getFilesOnFolder(String folder) {
        return FileSearcher.getFilesOnFolder(folder, SYSTEM_VARIABLE_FILES_LOCATION);
    }

    public static InputStream getFileAsInputStream(String filePath) {
        return FileSearcher.getFileAsInputStream(filePath, SYSTEM_VARIABLE_FILES_LOCATION);
    }

    public static String getFileAsString(String filePath) {
        return FileSearcher.getFileAsString(filePath, SYSTEM_VARIABLE_FILES_LOCATION);
    }

    public static byte[] getFileAsBytes(String filePath) {
        return FileSearcher.getFileAsBytes(filePath, SYSTEM_VARIABLE_FILES_LOCATION);
    }
}
