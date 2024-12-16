package com.biit.infographic.core.engine.content;

import com.biit.drools.form.DroolsSubmittedForm;
import com.biit.infographic.core.engine.Parameter;
import com.biit.infographic.core.exceptions.ElementDoesNotExistsException;
import com.biit.infographic.logger.InfographicEngineLogger;
import org.springframework.stereotype.Component;

import java.time.format.DateTimeFormatter;
import java.util.Objects;
import java.util.Set;

@Component
public class FormContent {

    private static final String FORM_PARAMETER = "SUBMIT";
    private static final String TIME_ATTRIBUTE = "TIME";
    private static final String TIME_ATTRIBUTE_PATTERN = "HH:mm";
    private static final String DATE_ATTRIBUTE = "DATE";
    private static final String DATE_ATTRIBUTE_PATTERN = "dd/MM/yyyy";

    public void setUserVariableValues(Set<Parameter> parameters, DroolsSubmittedForm droolsSubmittedForm)
            throws ElementDoesNotExistsException {
        if (parameters == null) {
            return;
        }
        for (Parameter parameter : parameters) {
            if (Objects.equals(parameter.getName(), FORM_PARAMETER)) {
                InfographicEngineLogger.debug(this.getClass(), "Processing parameter '#{}%{}%{}#'.",
                        parameter.getType(), parameter.getName(), parameter.getAttributes());
                // Search for any variable defined in the parameters
                for (String attribute : parameter.getAttributes().keySet()) {
                    //#FORM%SUBMIT%DATE#
                    try {
                        if (Objects.equals(attribute, TIME_ATTRIBUTE)) {
                            parameter.getAttributes().put(attribute, droolsSubmittedForm.getSubmittedAt()
                                    .format(DateTimeFormatter.ofPattern(TIME_ATTRIBUTE_PATTERN)));
                        } else if (Objects.equals(attribute, DATE_ATTRIBUTE)) {
                            parameter.getAttributes().put(attribute, droolsSubmittedForm.getSubmittedAt()
                                    .format(DateTimeFormatter.ofPattern(DATE_ATTRIBUTE_PATTERN)));
                        }
                    } catch (Exception e) {
                        InfographicEngineLogger.errorMessage(this.getClass(), e);
                    }
                }
            }
        }
    }
}
