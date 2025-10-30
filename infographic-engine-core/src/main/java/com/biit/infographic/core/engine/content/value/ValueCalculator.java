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

import com.biit.infographic.core.exceptions.InvalidParameterException;
import com.biit.infographic.core.exceptions.MalformedConditionException;
import org.springframework.stereotype.Component;

import java.util.regex.Pattern;

@Component
public class ValueCalculator {

    public static final String ATTRIBUTE_FIELDS_SEPARATION = "|";
    public static final String CONDITION_SEPARATION = "?";

    public static final String VALUE_SEPARATION = "!";

    //#SOMETHING%SOMELEMENT%element==value?ffffff!000000#
    public Condition getCondition(String attribute) {
        //Has a question mark, but it is not a URL.
        if (!attribute.contains(CONDITION_SEPARATION) || attribute.startsWith("http")) {
            return null;
        }
        final String[] actions = attribute.split(Pattern.quote(CONDITION_SEPARATION));
        final ConditionOperation operator = ConditionOperation.getOperator(actions[0]);
        if (actions.length < 2 || operator == null || !actions[1].contains(VALUE_SEPARATION)) {
            return null;
        }
        final String[] condition = actions[0].split(operator.getRepresentation());
        final String[] values = actions[1].split(Pattern.quote(VALUE_SEPARATION));
        try {
            return new Condition(condition[0], operator, condition[1], values[0], values[1]);
        } catch (ArrayIndexOutOfBoundsException e) {
            throw new MalformedConditionException(this.getClass(), "Invalid condition structure on '" + attribute + "'.");
        }
    }


    public double operationExecution(String operation) {
        //7200/10
        if (operation.contains("/")) {
            return generateFraction(operation);
        }
        if (operation.contains("*")) {
            return generateMultiplication(operation);
        }
        try {
            return Double.parseDouble(operation);
        } catch (Exception e) {
            throw new InvalidParameterException(this.getClass(), "Operation '" + operation + "' is invalid!");
        }
    }

    private double generateFraction(String operation) {
        final String[] parts = operation.split("/");
        if (parts.length == 1) {
            return Double.parseDouble(parts[0]);
        }
        return Double.parseDouble(parts[0]) / Double.parseDouble(parts[1]);
    }

    private double generateMultiplication(String operation) {
        final String[] parts = operation.split("\\*");
        if (parts.length == 1) {
            return Long.parseLong(parts[0]);
        }
        return Double.parseDouble(parts[0]) * Double.parseDouble(parts[1]);
    }

}
