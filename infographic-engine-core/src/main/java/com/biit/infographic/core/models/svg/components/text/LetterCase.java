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
