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

import com.biit.infographic.core.models.svg.components.text.FontFactory;
import com.biit.infographic.logger.InfographicEngineLogger;
import com.biit.infographic.logger.SvgGeneratorLogger;
import com.biit.utils.file.FileReader;
import org.apache.commons.io.FileUtils;
import org.springframework.core.io.ClassPathResource;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

public final class FileSearcher {

    private FileSearcher() {

    }


    public static String getFilePath(String file, String folderName, String systemVariableFilesLocation) {
        final String resourceFolderPath;
        try {
            resourceFolderPath = new ClassPathResource(File.separator + folderName).getFile().getPath();
            final File resourceFolder = new File(resourceFolderPath);
            if (resourceFolder.listFiles() != null) {
                for (File fileListed : Objects.requireNonNull(resourceFolder.listFiles())) {
                    if (Objects.equals(fileListed.getName(), file)) {
                        return fileListed.getAbsolutePath();
                    }
                }
            } else {
                SvgGeneratorLogger.warning(FileSearcher.class, "No files found on '{}'.", resourceFolderPath + File.separator + folderName);
            }
        } catch (java.io.FileNotFoundException e) {
            SvgGeneratorLogger.debug(FileSearcher.class, "Font is not on resource path '{}'.", File.separator + folderName);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        final String systemVariablesFilePath = readEnvironmentVariable(systemVariableFilesLocation);
        final File folder = new File(systemVariablesFilePath + File.separator + folderName);
        if (folder.listFiles() != null) {
            for (File fileListed : Objects.requireNonNull(folder.listFiles())) {
                if (Objects.equals(fileListed.getName(), file)) {
                    return fileListed.getAbsolutePath();
                }
            }
        } else {
            SvgGeneratorLogger.warning(FileSearcher.class, "No files found on '{}'.", systemVariablesFilePath + File.separator + folderName);
        }

        return null;
    }

    public static List<String> getFilesOnFolderPath(String folderName, String systemVariableFilesLocation) {
        final List<String> files = new ArrayList<>();

        final String resourceFolderPath;
        try {
            resourceFolderPath = new ClassPathResource(File.separator + folderName).getFile().getPath();
            final File resourceFolder = new File(resourceFolderPath);
            if (resourceFolder.listFiles() != null) {
                files.addAll(Arrays.stream(Objects.requireNonNull(resourceFolder.listFiles())).map(File::getAbsolutePath).collect(Collectors.toSet()));
            } else {
                SvgGeneratorLogger.warning(FileSearcher.class, "No files found on '{}'.", resourceFolderPath + File.separator + folderName);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        final String systemVariablesFilePath = readEnvironmentVariable(systemVariableFilesLocation);
        final File folder = new File(systemVariablesFilePath + File.separator + folderName);
        if (folder.listFiles() != null) {
            files.addAll(Arrays.stream(Objects.requireNonNull(folder.listFiles())).map(File::getAbsolutePath).collect(Collectors.toSet()));
        } else {
            SvgGeneratorLogger.warning(FileSearcher.class, "No files found on '{}'.", systemVariablesFilePath + File.separator + folderName);
        }

        return files;
    }

    public static List<String> getFilesOnFolder(String folderName, String systemVariableFilesLocation) {
        final List<String> files = new ArrayList<>();
        try {
            files.addAll(FileReader.getResourceFiles(File.separator + (folderName != null ? folderName : "")));
        } catch (Exception e) {
            SvgGeneratorLogger.errorMessage(FontFactory.class, e);
        }
        final String systemVariablesFilePath = readEnvironmentVariable(systemVariableFilesLocation);
        final File folder = new File(systemVariablesFilePath + File.separator + (folderName != null ? folderName : ""));
        SvgGeneratorLogger.info(FileSearcher.class, "Searching fonts on '{}'.", folder.getAbsolutePath());
        if (folder.listFiles() != null) {
            files.addAll(Arrays.stream(Objects.requireNonNull(folder.listFiles())).map(File::getName).collect(Collectors.toSet()));
        } else {
            SvgGeneratorLogger.warning(FileSearcher.class, "No files found on '{}'.", systemVariablesFilePath + File.separator + folderName);
        }

        return files;
    }

    public static InputStream getFileAsInputStream(String filePath, String systemVariableFilesLocation) {
        try {
            return getInputStreamFromResources(filePath);
        } catch (Exception e) {
            InfographicEngineLogger.debug(FileSearcher.class.getName(),
                    "File '{}' not found on resources folder.", filePath);
        }
        try {
            return getInputStreamFromEnvironmentalVariable(filePath, systemVariableFilesLocation);
        } catch (Exception e) {
            InfographicEngineLogger.debug(FileSearcher.class.getName(),
                    "File '{}' not found on resources folder.", filePath);
        }
        return null;
    }

    public static String getFileAsString(String filePath, String systemVariableFilesLocation) {
        final byte[] content = getFileAsBytes(filePath, systemVariableFilesLocation);
        if (content != null && content.length > 0) {
            return new String(content, StandardCharsets.UTF_8);
        }
        return null;
    }

    public static byte[] getFileAsBytes(String filePath, String systemVariableFilesLocation) {
        try {
            return getFileFromResources(filePath);
        } catch (Exception e) {
            InfographicEngineLogger.debug(FileSearcher.class.getName(),
                    "File '{}' not found on resources folder.", filePath);
        }
        try {
            return getFileFromEnvironmentalVariable(filePath, systemVariableFilesLocation);
        } catch (Exception e) {
            InfographicEngineLogger.debug(FileSearcher.class.getName(),
                    "File '{}' not found on resources folder.", filePath);
        }
        return null;
    }

    private static byte[] getFileFromResources(String filePath) throws FileNotFoundException {
        try {
            InfographicEngineLogger.debug(FileSearcher.class.getName(), "Searching for resource file at '{}'.", filePath);
            return FileUtils.readFileToByteArray(FileReader.getResource(filePath));
        } catch (NullPointerException | IOException e) {
            InfographicEngineLogger.warning(FileSearcher.class.getName(), "No file found at resources '{}'.", filePath);
            throw new FileNotFoundException(FileUtils.class, "No file found at resources '" + filePath + "'.");
        }
    }

    private static byte[] getFileFromEnvironmentalVariable(String filePath, String systemVariableFilesLocation) throws FileNotFoundException {
        if (readEnvironmentVariable(systemVariableFilesLocation) == null) {
            InfographicEngineLogger.info(FileSearcher.class.getName(),
                    "No env variable '{}' defined.", systemVariableFilesLocation);
            throw new FileNotFoundException(FileUtils.class, "No env variable '{}' defined.");
        }
        final String systemVariablesFilePath = readEnvironmentVariable(systemVariableFilesLocation) + File.separator + filePath;
        try {
            InfographicEngineLogger.debug(FileSearcher.class.getName(),
                    "File path '{}'", systemVariablesFilePath);
            return FileUtils.readFileToByteArray(new File(systemVariablesFilePath));
        } catch (NullPointerException | IOException e) {
            InfographicEngineLogger.debug(FileSearcher.class.getName(), "No file found at '{}'.", systemVariablesFilePath);
            throw new FileNotFoundException(FileUtils.class, "No file found at '" + systemVariablesFilePath + "'.");
        }
    }

    private static InputStream getInputStreamFromResources(String filePath) throws FileNotFoundException {
        try {
            InfographicEngineLogger.debug(FileSearcher.class.getName(), "Searching for resource file at '{}'.", filePath);
            return FileUtils.openInputStream(FileReader.getResource(filePath));
        } catch (NullPointerException | IOException e) {
            InfographicEngineLogger.warning(FileSearcher.class.getName(), "No file found at resources '{}'.", filePath);
            throw new FileNotFoundException(FileUtils.class, "No file found at resources '" + filePath + "'.");
        }
    }

    private static InputStream getInputStreamFromEnvironmentalVariable(String filePath, String systemVariableFilesLocation) throws FileNotFoundException {
        if (readEnvironmentVariable(systemVariableFilesLocation) == null) {
            InfographicEngineLogger.info(FileSearcher.class.getName(),
                    "No env variable '{}' defined.", systemVariableFilesLocation);
            throw new FileNotFoundException(FileUtils.class, "No env variable '{}' defined.");
        }
        final String systemVariablesFilePath = readEnvironmentVariable(systemVariableFilesLocation) + File.separator + filePath;
        try {
            InfographicEngineLogger.debug(FileSearcher.class.getName(),
                    "File path '{}'", systemVariablesFilePath);
            return FileUtils.openInputStream(new File(systemVariablesFilePath));
        } catch (NullPointerException | IOException e) {
            InfographicEngineLogger.warning(FileSearcher.class.getName(), "No file found at '{}'.", systemVariablesFilePath);
            throw new FileNotFoundException(FileUtils.class, "No file found at '" + systemVariablesFilePath + "'.");
        }
    }

    public static String readEnvironmentVariable(String systemVariableFilesLocation) {
        final Map<String, String> env = System.getenv();
        return env.get(systemVariableFilesLocation);
    }
}
