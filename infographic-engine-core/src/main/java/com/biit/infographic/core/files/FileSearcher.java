package com.biit.infographic.core.files;

import com.biit.infographic.logger.InfographicEngineLogger;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;

public final class FileSearcher {

    private static final String SYSTEM_VARIABLE_CONFIG = "FILES_FOLDER";

    private FileSearcher() {

    }

    public static String getFileAsString(String filePath) {
        final byte[] content = getFileAsBytes(filePath);
        if (content != null && content.length > 0) {
            return new String(content, StandardCharsets.UTF_8);
        }
        return null;
    }

    public static byte[] getFileAsBytes(String filePath) {
        try {
            return getFileFromResources(filePath);
        } catch (Exception e) {
            InfographicEngineLogger.debug(FileSearcher.class.getName(),
                    "File '{}' not found on resources folder.", filePath);
        }
        try {
            return getFileFromEnvironmentalVariable(filePath);
        } catch (Exception e) {
            InfographicEngineLogger.debug(FileSearcher.class.getName(),
                    "File '{}' not found on resources folder.", filePath);
        }
        return null;
    }

    private static byte[] getFileFromResources(String filePath) throws FileNotFoundException {
        try {
            InfographicEngineLogger.debug(FileSearcher.class.getName(), "Searching for resource file at '{}'.", filePath);
            return FileUtils.readFileToByteArray(new File(FileUtils.class.getClassLoader()
                    .getResource(filePath).getFile()));
        } catch (NullPointerException | IOException e) {
            InfographicEngineLogger.warning(FileSearcher.class.getName(), "No file found at resources '{}'.", filePath);
            throw new FileNotFoundException(FileUtils.class, "No file found at resources '" + filePath + "'.");
        }
    }

    private static byte[] getFileFromEnvironmentalVariable(String filePath) throws FileNotFoundException {
        if (readEnvironmentVariable() == null) {
            InfographicEngineLogger.info(FileSearcher.class.getName(),
                    "No env variable '{}' defined.", SYSTEM_VARIABLE_CONFIG);
            throw new FileNotFoundException(FileUtils.class, "No env variable '{}' defined.");
        }
        final String systemVariablesFilePath = readEnvironmentVariable() + File.separator + filePath;
        try {
            InfographicEngineLogger.debug(FileSearcher.class.getName(),
                    "File path '{}'", systemVariablesFilePath);
            return FileUtils.readFileToByteArray(new File(systemVariablesFilePath));
        } catch (NullPointerException | IOException e) {
            InfographicEngineLogger.debug(FileSearcher.class.getName(), "No file found at '{}'.", systemVariablesFilePath);
            throw new FileNotFoundException(FileUtils.class, "No file found at '" + systemVariablesFilePath + "'.");
        }
    }

    public static String readEnvironmentVariable() {
        final Map<String, String> env = System.getenv();
        return env.get(SYSTEM_VARIABLE_CONFIG);
    }
}
