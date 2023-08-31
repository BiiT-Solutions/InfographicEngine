package com.biit.infographic.core.files;

import java.util.Map;

/**
 * Indicators, charts, form questions, goals, ...
 */
public interface IParameter {

    String getName();

    Map<String, String> getAttributes();

    String getType();
}
