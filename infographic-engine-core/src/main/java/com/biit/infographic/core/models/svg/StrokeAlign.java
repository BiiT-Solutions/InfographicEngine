package com.biit.infographic.core.models.svg;

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

public enum StrokeAlign {
    /**
     * Default.
     */
    CENTER,
    /**
     * Only working with circle and rectangle.
     */
    OUTSET,
    /**
     * No implemented yet.
     */
    INSET;

    public static StrokeAlign get(String parameter) {
        if (parameter != null) {
            for (StrokeAlign strokeLineCap : StrokeAlign.values()) {
                if (strokeLineCap.name().equalsIgnoreCase(parameter)) {
                    return strokeLineCap;
                }
            }
        }
        return StrokeAlign.CENTER;
    }
}
