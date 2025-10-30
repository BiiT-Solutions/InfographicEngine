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

import com.biit.infographic.core.exceptions.ElementDoesNotExistsException;
import com.biit.infographic.logger.InfographicEngineLogger;
import com.biit.utils.file.FileReader;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;

public class InfographicSearcher {

    public static final String JSON_EXTENSION = ".json";
    private static final String SYSTEM_VARIABLE_CONFIG = "INFOGRAPHIC_FOLDER";

    /**
     * Reads the index file from ENV variable or Resources.
     *
     * @param path the path of the infographic folder.
     * @return the content of the index.json
     */
    public String readFile(String path) throws FileNotFoundException {
        try {
            return getInfographicFromEnvironmentalVariable(path);
        } catch (ElementDoesNotExistsException | IOException e) {
            InfographicEngineLogger.debug(this.getClass().getName(),
                    "File not found on global variable folder set by environmental variable for path '{}'.",
                    path);
        }
        try {
            return getInfographicFromDefaultFolder(path);
        } catch (ElementDoesNotExistsException | IOException e) {
            InfographicEngineLogger.debug(this.getClass().getName(),
                    "File not found on global variable folder set by environmental variable for path '{}'.",
                    path);
        }
        try {
            return getInfographicFromResources(path);
        } catch (FileNotFoundException e) {
            InfographicEngineLogger.debug(this.getClass().getName(),
                    "File not found on resources folder for path '{}'.", path);
        }
        throw new FileNotFoundException("File not found on for path '" + path + "'.");
    }

    private String getInfographicFromEnvironmentalVariable(String path) throws IOException {
        if (readEnvironmentVariable() == null) {
            InfographicEngineLogger.info(this.getClass(),
                    "No env variable '{}' defined.", SYSTEM_VARIABLE_CONFIG);
            throw new ElementDoesNotExistsException(this.getClass(), "No env variable '" + SYSTEM_VARIABLE_CONFIG + "' defined.");
        }
        // Load Json file
        final String infographicFilePath = readEnvironmentVariable() + File.separator + path;
        InfographicEngineLogger.debug(this.getClass().getName(),
                "Infographic from environment '{}' file path is '{}' ", readEnvironmentVariable(), infographicFilePath);
        return Files.readString(Paths.get(infographicFilePath), StandardCharsets.UTF_8);
    }

    private String getInfographicFromDefaultFolder(String path) throws IOException {
        // Load Json file
        InfographicEngineLogger.debug(this.getClass().getName(),
                "Infographic file from default folder path '{}'. ", path);
        return Files.readString(Paths.get(path), StandardCharsets.UTF_8);
    }

    private String getInfographicFromResources(String path) throws FileNotFoundException {
        // Load Json file
        if (path.startsWith(File.separator)) {
            path = path.substring(1);
        }
        InfographicEngineLogger.debug(this.getClass().getName(), "Infographic file from resource path '{}'.", path);
        return FileReader.getResource(path, StandardCharsets.UTF_8);
    }

    public static String getInfographicPath(String path, InfographicFileElement definition) {
        if (definition.isFolder() && !path.endsWith(File.separator)) {
            path += File.separator;
        }
        path = path + definition.getJsonFile();

        if (!definition.isFolder()) {
            path = path + "_v" + definition.getJsonVersion() + JSON_EXTENSION;
        }
        return path;
    }


    public static String readEnvironmentVariable() {
        final Map<String, String> env = System.getenv();
        return env.get(SYSTEM_VARIABLE_CONFIG);
    }
}
