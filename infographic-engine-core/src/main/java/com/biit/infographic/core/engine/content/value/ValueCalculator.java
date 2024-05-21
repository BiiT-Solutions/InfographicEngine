package com.biit.infographic.core.engine.content.value;

import com.biit.infographic.core.exceptions.InvalidParameterException;
import org.springframework.stereotype.Component;

import java.util.regex.Pattern;

@Component
public class ValueCalculator {

    public static final String ATTRIBUTE_FIELDS_SEPARATION = "|";
    public static final String CONDITION_SEPARATION = "?";

    public static final String VALUE_SEPARATION = ":";

    //#SOMETHING%SOMELEMENT%element==value?ffffff:000000#
    public Condition getCondition(String attribute) {
        if (!attribute.contains(CONDITION_SEPARATION)) {
            return null;
        }
        final String[] actions = attribute.split(Pattern.quote(CONDITION_SEPARATION));
        final ConditionOperation operator = ConditionOperation.getOperator(actions[0]);
        if (actions.length < 2 || operator == null || !actions[1].contains(VALUE_SEPARATION)) {
            return null;
        }
        final String[] condition = actions[0].split(operator.getRepresentation());
        final String[] values = actions[1].split(VALUE_SEPARATION);
        return new Condition(condition[0], operator, condition[1], values[0], values[1]);
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
