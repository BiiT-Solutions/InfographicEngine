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

public enum Unit {
    PERCENTAGE("%", "%"),
    PIXELS("px", "");

    private final String tag;

    private final String value;

    Unit(String tag, String value) {
        this.tag = tag;
        this.value = value;
    }

    public String getTag() {
        return tag;
    }

    public String getValue() {
        return value;
    }

    public static Long getValue(String parameter) {
        if (parameter == null) {
            return null;
        }
        return Long.parseLong(parameter.replaceAll("[^\\d.]", ""));
    }

    public static Unit getUnit(String parameter) {
        if (parameter != null) {
            for (Unit unit : Unit.values()) {
                if (parameter.endsWith(unit.getTag()) || unit.name().equalsIgnoreCase(parameter)) {
                    return unit;
                }
            }
        }
        return Unit.PIXELS;
    }
}
