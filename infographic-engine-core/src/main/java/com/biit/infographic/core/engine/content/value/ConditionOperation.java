package com.biit.infographic.core.engine.content.value;

public enum ConditionOperation {

    EQUALS("=="),
    LESS_EQUALS("<="),
    GREATER_EQUALS(">="),
    LESS("<"),
    GREATER(">");

    private final String representation;

    ConditionOperation(String representation) {
        this.representation = representation;
    }

    public String getRepresentation() {
        return representation;
    }

    public static ConditionOperation getOperator(String condition) {
        if (condition.contains(ConditionOperation.EQUALS.getRepresentation())) {
            return ConditionOperation.EQUALS;
        }
        if (condition.contains(ConditionOperation.LESS_EQUALS.getRepresentation())) {
            return ConditionOperation.LESS_EQUALS;
        }
        if (condition.contains(ConditionOperation.GREATER_EQUALS.getRepresentation())) {
            return ConditionOperation.GREATER_EQUALS;
        }
        if (condition.contains(ConditionOperation.LESS.getRepresentation())) {
            return ConditionOperation.LESS;
        }
        if (condition.contains(ConditionOperation.GREATER.getRepresentation())) {
            return ConditionOperation.GREATER;
        }
        return null;
    }
}
