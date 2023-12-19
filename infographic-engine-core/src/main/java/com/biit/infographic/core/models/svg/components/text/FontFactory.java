package com.biit.infographic.core.models.svg.components.text;

import com.biit.infographic.logger.SvgGeneratorLogger;
import com.biit.utils.file.FileReader;
import org.apache.commons.io.FileUtils;

import java.awt.Font;
import java.awt.FontFormatException;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class FontFactory {
    private static final String FONTS_FOLDER = "fonts";
    private static final String FONTS_REGULAR = "Regular";
    private static final String FONTS_EXTENSION = "ttf";
    private static Map<String, Font> fonts;
    private static Map<String, String> fontsFiles;

    private FontFactory() {
        loadFonts();
    }

    private static void loadFonts() {
        try {
            fonts = new HashMap<>();
            fontsFiles = new HashMap<>();
            final List<String> availableFonts = FileReader.getResourceFiles(File.separator + FONTS_FOLDER);
            for (String file : availableFonts) {
                loadFont(file);
            }
        } catch (Exception e) {
            SvgGeneratorLogger.errorMessage(FontFactory.class, e);
        }
    }

    private static void loadFont(String fontFile) {
        final InputStream is = FontFactory.class.getResourceAsStream(File.separator + FONTS_FOLDER + File.separator + fontFile);
        try {
            if (is != null) {
                final Font font = Font.createFont(Font.TRUETYPE_FONT, is);
                //Regular fonts are named regular, but never indexed with regular.
                fonts.put(font.getFontName().replaceAll(FONTS_REGULAR, "").trim(), font);
                fontsFiles.put(font.getFontName().replaceAll(FONTS_REGULAR, "").trim(), fontFile);
                SvgGeneratorLogger.debug(FontFactory.class, "Font '{}' found.", font.getFontName());
            } else {
                SvgGeneratorLogger.severe(FontFactory.class, "Font '{}' has some issues and cannot be loaded!", fontFile);
            }
        } catch (FontFormatException | IOException e) {
            SvgGeneratorLogger.errorMessage(FontFactory.class, e);
        }
    }


    public static Font getFont(String fontName) {
        if (fonts == null) {
            loadFonts();
        }
        return fonts.get(fontName);
    }

    public static String encodeFontToBase64(String fontName) throws IOException {
        final File file = FileReader.getResource(FONTS_FOLDER + File.separator + fontsFiles.get(fontName));
        SvgGeneratorLogger.debug(FontFactory.class, "Encoding font '{}'", file.getAbsolutePath());
        final byte[] fileContent = FileUtils.readFileToByteArray(file);
        return Base64.getEncoder().encodeToString(fileContent);
    }
}
