package com.biit.infographic.core.models.svg.components.text;

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

import com.biit.infographic.core.files.FileSearcher;
import com.biit.infographic.core.files.FontSearcher;
import com.biit.infographic.logger.InfographicEngineLogger;
import com.biit.infographic.logger.SvgGeneratorLogger;
import com.biit.utils.pool.BasePool;
import org.springframework.core.io.ClassPathResource;

import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.GraphicsEnvironment;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Base64;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

public final class FontFactory {
    public static final String FONTS_FOLDER = "fonts";
    private static final String FONTS_REGULAR = "Regular";
    private static final long FONTS_POOL_EXPIRATION_TIME = 60 * 60 * 1000L;
    //Font Family --> Font Weight --> Font
    private static Map<String, Map<FontWeight, Font>> fonts;
    //Font Family --> Font Weight --> Font File
    private static Map<String, Map<FontWeight, String>> fontsFiles;
    //Font Family --> Font Weight --> Font Path
    private static Map<String, Map<FontWeight, String>> fontsPaths;

    private static final BasePool<String, String> ENCODED_FONTS_POOL = new BasePool<>() {
        @Override
        public long getExpirationTime() {
            return FONTS_POOL_EXPIRATION_TIME;
        }

        @Override
        public boolean isDirty(String element) {
            return false;
        }
    };

    private static boolean fontsLoaded = false;

    private FontFactory() {
        loadFonts();
    }

    private static void loadFonts() {
        try {
            fonts = new HashMap<>();
            fontsFiles = new HashMap<>();
            fontsPaths = new HashMap<>();
            final List<String> availableFonts = FontSearcher.getFilesOnFolder(FONTS_FOLDER);
            for (String file : availableFonts) {
                loadFont(file);
            }
            SvgGeneratorLogger.debug(FontFactory.class, "Fonts found are '{}'.", fontsFiles);
            registerFonts();
        } catch (Exception e) {
            SvgGeneratorLogger.errorMessage(FontFactory.class, e);
        }
    }

    public static Set<Font> getLoadedFonts() {
        if (fonts == null) {
            loadFonts();
        }

        final Set<Font> loadedFonts = new HashSet<>();
        for (Map<FontWeight, Font> weights : fonts.values()) {
            loadedFonts.addAll(weights.values());
        }
        return loadedFonts;
    }

    private static void loadFont(String fontFile) {
        final InputStream is = FontSearcher.getFileAsInputStream(FONTS_FOLDER + File.separator + fontFile);
        try {
            if (is != null) {
                final Font font = Font.createFont(Font.TRUETYPE_FONT, is);
                //Regular fonts are named regular, but never indexed with regular. Other as Bolds are stored in the name.

                final String fontName = normalizeFonts(font.getFamily(Locale.ENGLISH));
                final FontWeight fontWeight = getFontWeight(fontFile);

                fonts.computeIfAbsent(fontName, k -> new EnumMap<>(FontWeight.class));
                fontsFiles.computeIfAbsent(fontName, k -> new EnumMap<>(FontWeight.class));
                fontsPaths.computeIfAbsent(fontName, k -> new EnumMap<>(FontWeight.class));

                fonts.get(fontName).put(fontWeight, font);
                fontsFiles.get(fontName).put(fontWeight, fontFile);
                fontsPaths.get(fontName).put(fontWeight, FontSearcher.getFilePath(fontFile, FONTS_FOLDER));
                SvgGeneratorLogger.debug(FontFactory.class, "Font '{}' found and with weight '{}' included.", fontName, fontWeight);
            } else {
                SvgGeneratorLogger.severe(FontFactory.class, "Font '{}' has some issues and cannot be loaded!", fontFile);
            }
        } catch (FontFormatException | IOException e) {
            SvgGeneratorLogger.errorMessage(FontFactory.class, e);
        }
    }

    private static FontWeight getFontWeight(String fileName) {
        if (fileName.toUpperCase().contains("BOLD")) {
            return FontWeight.BOLD;
        }
        return FontWeight.NORMAL;
    }

    private static String normalizeFonts(String fontName) {
        return fontName.replace(FONTS_REGULAR, "").trim();
    }

    public static void resetFonts() {
        fonts = null;
    }


    public static Font getFont(String fontsName, FontWeight fontWeight) {
        if (fonts == null) {
            loadFonts();
        }
        final String[] fontsArray = fontsName.split(",");
        for (String fontName : fontsArray) {
            if (fontWeight == null && fonts.get(fontName) != null) {
                return fonts.get(fontName).get(FontWeight.NORMAL);
            }
            for (Map.Entry<String, Map<FontWeight, Font>> fontEntry : fonts.entrySet()) {
                if (fontEntry.getKey().startsWith(normalizeFonts(fontName))) {
                    return fontEntry.getValue().get(fontWeight);
                }
            }
        }
        return null;
    }

    private static String getFontFiles(String fontsName, FontWeight fontWeight) {
        if (fontsFiles == null) {
            loadFonts();
        }
        final String[] fontsArray = fontsName.split(",");
        for (String fontName : fontsArray) {
            if (fontWeight == null && fonts.get(fontName) != null) {
                    return fontsFiles.get(fontName).get(FontWeight.NORMAL);
            }
            for (Map.Entry<String, Map<FontWeight, String>> fontEntry : fontsFiles.entrySet()) {
                if (fontEntry.getKey().startsWith(normalizeFonts(fontName))) {
                    return fontEntry.getValue().get(fontWeight);
                }
            }
        }
        return null;
    }

    public static String getFontPath(String fontsName, FontWeight fontWeight) {
        if (fontsPaths == null) {
            loadFonts();
        }
        final String[] fontsArray = fontsName.split(",");
        for (String fontName : fontsArray) {
            if (fontWeight == null && fonts.get(fontName) != null) {
                    return fontsPaths.get(fontName).get(FontWeight.NORMAL);
                }
            for (Map.Entry<String, Map<FontWeight, String>> fontEntry : fontsPaths.entrySet()) {
                if (fontEntry.getKey().startsWith(normalizeFonts(fontName))) {
                    return fontEntry.getValue().get(fontWeight);
                }
            }
        }
        return null;
    }

    public static String encodeFontToBase64(String fontName, FontWeight fontWeight) {
        if (ENCODED_FONTS_POOL.getElement(fontName + fontWeight) == null) {
            final String fontFile = getFontFiles(fontName, fontWeight);
            SvgGeneratorLogger.debug(FontFactory.class, "Encoding font '{}'", FONTS_FOLDER + File.separator + fontFile);
            final byte[] fileContent = FontSearcher.getFileAsBytes(FONTS_FOLDER + File.separator + fontFile);
            ENCODED_FONTS_POOL.addElement(Base64.getEncoder().encodeToString(fileContent), fontName + fontWeight);
        }
        return ENCODED_FONTS_POOL.getElement(fontName + fontWeight);
    }

    public static Map<String, Map<FontWeight, Font>> getFonts() {
        return fonts;
    }

    public static Map<String, Map<FontWeight, String>> getFontsPaths() {
        return fontsPaths;
    }

    public static List<String> getDefaultFoldersToSearch() {
        final List<String> fontsFolders = new ArrayList<>();
        final String systemVariablesFilePath = FileSearcher.readEnvironmentVariable(FontSearcher.SYSTEM_VARIABLE_FILES_LOCATION);
        if (systemVariablesFilePath == null) {
            InfographicEngineLogger.warning(FontFactory.class, "No environmental variable '{}' defined!", FontSearcher.SYSTEM_VARIABLE_FILES_LOCATION);
        } else {
            InfographicEngineLogger.debug(FontFactory.class, "Environment variable set as '{}'.", systemVariablesFilePath);
            if (new File(systemVariablesFilePath + File.separator + FONTS_FOLDER).exists()) {
                fontsFolders.add(systemVariablesFilePath + File.separator + FONTS_FOLDER);
                InfographicEngineLogger.debug(FontFactory.class, "Fonts Folder '{}' loaded.", systemVariablesFilePath + File.separator + FONTS_FOLDER);
            } else {
                InfographicEngineLogger.warning(FontFactory.class, "Fonts Folder '{}' not found!", systemVariablesFilePath + File.separator + FONTS_FOLDER);
            }
        }
        try {
            fontsFolders.add(new ClassPathResource(File.separator + FONTS_FOLDER).getFile().getPath());
            InfographicEngineLogger.debug(FontFactory.class, "Resource Folder '{}' loaded.", File.separator + FONTS_FOLDER);
        } catch (IOException e) {
            InfographicEngineLogger.warning(FontFactory.class, "Resource Folder '{}' not found!", File.separator + FONTS_FOLDER);
        }
        return fontsFolders;
    }

    /**
     * PNGTranscoder and PdfTemplate needs that these fonts are registered directly on Java VM.
     */
    private static void registerFonts() {
        if (!fontsLoaded) {
            //Register fonts
            final GraphicsEnvironment graphicsEnvironment = GraphicsEnvironment.getLocalGraphicsEnvironment();
            getLoadedFonts().forEach(graphicsEnvironment::registerFont);
            fontsLoaded = true;
        }
    }
}
