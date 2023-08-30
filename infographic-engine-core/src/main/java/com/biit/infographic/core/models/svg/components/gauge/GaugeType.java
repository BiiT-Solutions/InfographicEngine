package com.biit.infographic.core.models.svg.components.gauge;

public enum GaugeType {
    FIVE_VALUES,
    GRADIENT;

    public static GaugeType getType(String parameter) {
        if (parameter != null) {
            for (GaugeType gaugeType : GaugeType.values()) {
                if (gaugeType.name().equalsIgnoreCase(parameter)) {
                    return gaugeType;
                }
            }
        }
        return GaugeType.GRADIENT;
    }
}
