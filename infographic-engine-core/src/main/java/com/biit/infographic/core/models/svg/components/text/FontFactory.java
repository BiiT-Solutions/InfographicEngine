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
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public final class FontFactory {
    private static final String FONTS_FOLDER = "fonts";
    private static final String FONTS_REGULAR = "Regular";
    private static final long FONTS_POOL_EXPIRATION_TIME = 60 * 60 * 1000;
    //Font Family --> Font Weight --> Font
    private static Map<String, Map<FontWeight, Font>> fonts;
    //Font Family --> Font Weight --> Font File
    private static Map<String, Map<FontWeight, String>> fontsFiles;

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

                final String fontName = normalizeFonts(font.getFamily(Locale.ENGLISH));
                final FontWeight fontWeight = getFontWeight(fontFile);

                fonts.computeIfAbsent(fontName, k -> new HashMap<>());
                fontsFiles.computeIfAbsent(fontName, k -> new HashMap<>());

                fonts.get(fontName).put(fontWeight, font);
                fontsFiles.get(fontName).put(fontWeight, fontFile);
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
        return fontName.replaceAll(FONTS_REGULAR, "").trim();
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
            if (fontWeight == null) {
                if (fonts.get(fontName) != null) {
                    return fonts.get(fontName).get(FontWeight.NORMAL);
                }
            }
            for (Map.Entry<String, Map<FontWeight, Font>> fontEntry : fonts.entrySet()) {
                if (fontEntry.getKey().startsWith(normalizeFonts(fontName))) {
                    return fontEntry.getValue().get(fontWeight);
                }
            }
        }
        return null;
    }

    public static String getFontFiles(String fontsName, FontWeight fontWeight) {
        if (fontsFiles == null) {
            loadFonts();
        }
        final String[] fontsArray = fontsName.split(",");
        for (String fontName : fontsArray) {
            if (fontWeight == null) {
                if (fonts.get(fontName) != null) {
                    return fontsFiles.get(fontName).get(FontWeight.NORMAL);
                }
            }
            for (Map.Entry<String, Map<FontWeight, String>> fontEntry : fontsFiles.entrySet()) {
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
}
