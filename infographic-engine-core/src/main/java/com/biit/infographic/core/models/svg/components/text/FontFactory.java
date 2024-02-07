package com.biit.infographic.core.models.svg.components.text;

import com.biit.infographic.core.files.FontSearcher;
import com.biit.infographic.logger.SvgGeneratorLogger;
import com.biit.utils.pool.BasePool;

import java.awt.Font;
import java.awt.FontFormatException;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Base64;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class FontFactory {
    private static final String FONTS_FOLDER = "fonts";
    private static final String FONTS_REGULAR = "Regular";
    private static final String FONTS_MEDIUM = "Medium";
    private static final long FONTS_POOL_EXPIRATION_TIME = 60 * 60 * 1000;
    private static Map<String, Font> fonts;
    private static Map<String, String> fontsFiles;

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

    private FontFactory() {
        loadFonts();
    }

    private static void loadFonts() {
        try {
            fonts = new HashMap<>();
            fontsFiles = new HashMap<>();
            final List<String> availableFonts = FontSearcher.getFilesOnFolder(FONTS_FOLDER);
            for (String file : availableFonts) {
                loadFont(file);
            }
            SvgGeneratorLogger.debug(FontFactory.class, "Fonts found are '{}'.", fontsFiles);
        } catch (Exception e) {
            SvgGeneratorLogger.errorMessage(FontFactory.class, e);
        }
    }

    private static void loadFont(String fontFile) {
        final InputStream is = FontSearcher.getFileAsInputStream(FONTS_FOLDER + File.separator + fontFile);
        try {
            if (is != null) {
                final Font font = Font.createFont(Font.TRUETYPE_FONT, is);
                //Regular fonts are named regular, but never indexed with regular. Other as Bolds are stored in the name.
                fonts.put(normalizeFonts(font.getFontName()), font);
                fontsFiles.put(normalizeFonts(font.getFontName()), fontFile);
                SvgGeneratorLogger.debug(FontFactory.class, "Font '{}' found and defined as '{}'.", font.getFontName(), normalizeFonts(font.getFontName()));
            } else {
                SvgGeneratorLogger.severe(FontFactory.class, "Font '{}' has some issues and cannot be loaded!", fontFile);
            }
        } catch (FontFormatException | IOException e) {
            SvgGeneratorLogger.errorMessage(FontFactory.class, e);
        }
    }

    private static String normalizeFonts(String fontName) {
        return fontName.replaceAll(FONTS_REGULAR, "").replaceAll(FONTS_MEDIUM, "").trim();
    }

    public static void resetFonts() {
        fonts = null;
    }


    public static Font getFont(String fontName, FontWeight fontWeight) {
        if (fonts == null) {
            loadFonts();
        }
        if (fontWeight == null) {
            return fonts.get(fontName);
        }
        for (Map.Entry<String, Font> fontEntry : fonts.entrySet()) {
            if (fontEntry.getKey().startsWith(fontName) && fontEntry.getKey().toUpperCase().endsWith(fontWeight.name())) {
                return fontEntry.getValue();
            }
        }
        return null;
    }

    public static String getFontFiles(String fontName, FontWeight fontWeight) {
        if (fontsFiles == null) {
            loadFonts();
        }
        if (fontWeight == null) {
            return fontsFiles.get(fontName);
        }
        for (Map.Entry<String, String> fontEntry : fontsFiles.entrySet()) {
            if (fontEntry.getKey().startsWith(fontName) && fontEntry.getKey().toUpperCase().endsWith(fontWeight.name())) {
                return fontEntry.getValue();
            }
        }
        return null;
    }

    public static String encodeFontToBase64(String fontName, FontWeight fontWeight) {
        if (ENCODED_FONTS_POOL.getElement(fontName) == null) {
            final String fontFile = getFontFiles(fontName, fontWeight);
            SvgGeneratorLogger.debug(FontFactory.class, "Encoding font '{}'", FONTS_FOLDER + File.separator + fontFile);
            final byte[] fileContent = FontSearcher.getFileAsBytes(FONTS_FOLDER + File.separator + fontFile);
            ENCODED_FONTS_POOL.addElement(Base64.getEncoder().encodeToString(fileContent), fontName);
        }
        return ENCODED_FONTS_POOL.getElement(fontName);
    }

    public static Map<String, String> getFontsFiles() {
        return Collections.unmodifiableMap(fontsFiles);
    }
}
