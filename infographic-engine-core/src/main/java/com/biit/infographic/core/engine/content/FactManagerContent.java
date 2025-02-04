package com.biit.infographic.core.engine.content;

import com.biit.drools.form.DroolsSubmittedForm;
import com.biit.infographic.core.engine.Parameter;
import com.biit.infographic.core.providers.FactManagerVariableProvider;
import com.biit.infographic.logger.InfographicEngineLogger;
import com.biit.kafka.events.EventSubject;
import com.biit.ks.dto.exceptions.TextNotFoundException;

import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

//@Component
public class FactManagerContent {

    private static final String FACT_VARIABLE_TAG = "$FactManagerVariable{";
    private static final String FACT_TYPE = "DroolsVariable";
    private static final EventSubject FACT_SUBJECT = EventSubject.CREATED;
    private static final String FACT_APPLICATION = "BaseFormDroolsEngine";
    private final FactManagerVariableProvider factManagerVariableProvider;

    public FactManagerContent(FactManagerVariableProvider factManagerVariableProvider) {
        this.factManagerVariableProvider = factManagerVariableProvider;
    }

    public void setFactManagerVariables(Set<Parameter> parameters, DroolsSubmittedForm droolsSubmittedForm) {
        if (parameters != null) {
            //Search a parameter with the fact variable system tag.
            for (Parameter parameter : parameters) {
                for (Map.Entry<String, String> attribute : parameter.getAttributes().entrySet()) {
                    try {
                        if (attribute.getValue().contains(FACT_VARIABLE_TAG)) {
                            final String kafkaVariableName = extractKafkaValue(attribute.getValue());
                            final String variableValue = factManagerVariableProvider.getVariableValue(
                                    droolsSubmittedForm.getName(), kafkaVariableName, FACT_TYPE, FACT_SUBJECT,
                                    droolsSubmittedForm.getSubmittedBy(), FACT_APPLICATION, droolsSubmittedForm.getSubmittedAt());
                            //Replace FactVariable tag with obtained variable value.
                            attribute.setValue(attribute.getValue().replaceAll(Pattern.quote(FACT_VARIABLE_TAG) + "(.*?)}", variableValue));
                        }
                    } catch (TextNotFoundException e) {
                        InfographicEngineLogger.errorMessage(this.getClass(), e);
                    }
                }
            }
        }
    }

    private String extractKafkaValue(String value) {
        if (value == null || value.isEmpty()) {
            return "";
        }
        final String firstMatch = value.substring(value.indexOf(FACT_VARIABLE_TAG) + FACT_VARIABLE_TAG.length());
        return firstMatch.substring(0, firstMatch.indexOf('}'));
    }
}
