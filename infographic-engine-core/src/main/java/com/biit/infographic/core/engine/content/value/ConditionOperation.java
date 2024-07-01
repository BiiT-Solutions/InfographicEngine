package com.biit.infographic.core.engine.content.value;

public enum ConditionOperation {

    EQUALS("=="),
    NOT_EQUALS("!="),
    LESS_EQUALS("<="),
    GREATER_EQUALS(">="),
    LESS("<"),
    GREATER(">"),
    CONTAINS("∋"),
    NOT_CONTAINS("∌");

    private final String representation;

    ConditionOperation(String representation) {
        this.representation = representation;
    }

    public String getRepresentation() {
        return representation;
    }

    public static ConditionOperation getOperator(String conditionTag) {
        for (ConditionOperation conditionOperation : ConditionOperation.values()) {
            if (conditionTag.contains(conditionOperation.getRepresentation())) {
                return conditionOperation;
            }
        }
        return null;
    }
}
