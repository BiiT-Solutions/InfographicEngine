package com.biit.infographic.core.engine.content.value;

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

import com.biit.infographic.logger.InfographicEngineLogger;

import java.util.Set;

public class Condition {
    private static final String VALUE_ARRAY_SEPARATOR = " ";
    private final String element;
    private final ConditionOperation operator;
    private final String comparedTo;
    private final String resultIfEquals;
    private final String resultIfNotEquals;

    public Condition(String element, ConditionOperation operator, String comparedTo, String resultIfEquals, String resultIfNotEquals) {
        this.element = element;
        this.operator = operator;
        this.comparedTo = comparedTo;
        this.resultIfEquals = resultIfEquals;
        this.resultIfNotEquals = resultIfNotEquals;
    }

    public String getElement() {
        return element;
    }

    public ConditionOperation getOperator() {
        return operator;
    }

    public String getComparedTo() {
        return comparedTo;
    }

    public String getResultIfEquals() {
        return resultIfEquals;
    }

    public String getResultIfNotEquals() {
        return resultIfNotEquals;
    }

    /**
     * Gets a result if the element has this value.
     *
     * @param value The possible value of the element.
     * @return the chosen value.
     */
    public String getResult(String value) {
        if (value == null) {
            return resultIfNotEquals;
        }
        try {
            return switch (operator) {
                case EQUALS -> value.equalsIgnoreCase(comparedTo) ? resultIfEquals : resultIfNotEquals;
                case NOT_EQUALS -> !value.equalsIgnoreCase(comparedTo) ? resultIfEquals : resultIfNotEquals;
                case LESS ->
                        Double.parseDouble(value) < Double.parseDouble(comparedTo) ? resultIfEquals : resultIfNotEquals;
                case LESS_EQUALS ->
                        Double.parseDouble(value) <= Double.parseDouble(comparedTo) ? resultIfEquals : resultIfNotEquals;
                case GREATER ->
                        Double.parseDouble(value) > Double.parseDouble(comparedTo) ? resultIfEquals : resultIfNotEquals;
                case GREATER_EQUALS ->
                        Double.parseDouble(value) >= Double.parseDouble(comparedTo) ? resultIfEquals : resultIfNotEquals;
                case CONTAINS ->
                        Set.of(value.split(VALUE_ARRAY_SEPARATOR)).contains(comparedTo) ? resultIfEquals : resultIfNotEquals;
                case NOT_CONTAINS ->
                        !Set.of(value.split(VALUE_ARRAY_SEPARATOR)).contains(comparedTo) ? resultIfEquals : resultIfNotEquals;
            };
        } catch (NumberFormatException e) {
            InfographicEngineLogger.errorMessage(this.getClass(), e);
        }
        return resultIfNotEquals;
    }

    @Override
    public String toString() {
        return "Condition{"
                + "element='" + element + '\''
                + ", operator=" + operator
                + ", comparedTo='" + comparedTo + '\''
                + ", resultIfEquals='" + resultIfEquals + '\''
                + ", resultIfNotEquals='" + resultIfNotEquals + '\''
                + '}';
    }
}
