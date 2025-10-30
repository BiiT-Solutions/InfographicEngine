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

public enum FontVariantType {
    NORMAL("normal"),

    NONE("none"),

    SMALL_CAPS("small-caps"),

    ALL_SMALL_CAPS("all-small-caps"),

    TITLING_CAPS("titling-caps"),

    PETITE_CAPS("petite-caps"),

    ALL_PETITE_CAPS("all-petite-caps"),

    UNICASE("unicase"),

    SLASHED_ZERO("slashed-zero");


    private final String tag;

    FontVariantType(String tag) {
        this.tag = tag;
    }

    public String getTag() {
        return tag;
    }

    public static FontVariantType getVariant(String parameter) {
        if (parameter != null) {
            for (FontVariantType variantType : FontVariantType.values()) {
                if (variantType.name().equalsIgnoreCase(parameter)) {
                    return variantType;
                }
            }
        }
        return FontVariantType.NONE;
    }
}
