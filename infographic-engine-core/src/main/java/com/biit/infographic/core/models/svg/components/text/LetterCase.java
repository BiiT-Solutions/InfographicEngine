package com.biit.infographic.core.models.svg.components.text;

public enum LetterCase {
    NONE,
    LOWERCASE,
    UPPERCASE,
    CAMELCASE,
    SNAKECASE;

    public static LetterCase getCase(String parameter) {
        if (parameter != null) {
            for (LetterCase letterCase : LetterCase.values()) {
                if (letterCase.name().equalsIgnoreCase(parameter)) {
                    return letterCase;
                }
            }
        }
        return LetterCase.NONE;
    }

    public static String toCamelCase(String text) {
        if (text == null) {
            return null;
        }
        final String[] words = text.split("[\\W_]+");
        final StringBuilder builder = new StringBuilder();
        for (int i = 0; i < words.length; i++) {
            String word = words[i];
            if (i == 0) {
                word = word.isEmpty() ? word : word.toLowerCase();
            } else {
                word = word.isEmpty() ? word : Character.toUpperCase(word.charAt(0)) + word.substring(1).toLowerCase();
            }
            builder.append(word);
        }
        return builder.toString();
    }

    public static String toSnakeCase(String text) {
        if (text == null) {
            return null;
        }
        final String[] words = text.split("[\\W_]+");
        final StringBuilder builder = new StringBuilder();
        for (int i = 0; i < words.length; i++) {
            builder.append(words[i].toLowerCase());
            if (i < words.length - 1) {
                builder.append("_");
            }
        }
        return builder.toString();
    }
}
