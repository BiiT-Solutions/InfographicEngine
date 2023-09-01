package com.biit.infographic.core.engine.files;

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

public class FileSearcher {

    private static final String SYSTEM_VARIABLE_CONFIG = "INFOGRAPHIC_FOLDER";

    /**
     * Reads the index file from ENV variable or Resources.
     *
     * @param path the path of the infographic folder.
     * @return the content of the index.json
     */
    public String readFile(String path) {
        try {
            return getInfographicFromEnvironmentalVariable(path);
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
        return null;
    }

    private String getInfographicFromEnvironmentalVariable(String path) throws IOException {
        if (readEnvironmentVariable() == null) {
            InfographicEngineLogger.info(this.getClass(),
                    "No env variable '{}' defined.", SYSTEM_VARIABLE_CONFIG);
            throw new ElementDoesNotExistsException(this.getClass(), "No env variable '{}' defined.");
        }
        // Load Json file
        final String infographicFilePath = readEnvironmentVariable() + File.separator + path;
        InfographicEngineLogger.debug(this.getClass().getName(),
                "Infographic file path: " + infographicFilePath);
        return Files.readString(Paths.get(infographicFilePath), StandardCharsets.UTF_8);
    }

    private String getInfographicFromResources(String path) throws FileNotFoundException {
        // Load Json file
        final String infographicFilePath = File.separator + path;
        InfographicEngineLogger.debug(this.getClass().getName(), "Infographic file path: " + infographicFilePath);
        return FileReader.getResource(infographicFilePath, StandardCharsets.UTF_8);
    }


    public static String readEnvironmentVariable() {
        final Map<String, String> env = System.getenv();
        return env.get(SYSTEM_VARIABLE_CONFIG);
    }
}
