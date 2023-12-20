package com.biit.infographic.core.files;

import java.io.InputStream;
import java.util.List;

public final class FontSearcher {

    public static final String SYSTEM_VARIABLE_FILES_LOCATION = "FONTS_FOLDER";

    private FontSearcher() {
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
