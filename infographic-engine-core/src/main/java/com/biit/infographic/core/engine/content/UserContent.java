package com.biit.infographic.core.engine.content;

import com.biit.drools.form.DroolsSubmittedForm;
import com.biit.infographic.core.engine.Parameter;
import com.biit.infographic.core.exceptions.ElementDoesNotExistsException;
import com.biit.infographic.core.providers.UserProvider;
import com.biit.infographic.logger.InfographicEngineLogger;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.Set;

@Component
public class UserContent {

    private static final String SUBMITTER_PARAMETER = "SUBMITTER";
    private static final String NAME_ATTRIBUTE = "NAME";
    private static final String LASTNAME_ATTRIBUTE = "LASTNAME";

    private final UserProvider userProvider;

    public UserContent(UserProvider userProvider) {
        this.userProvider = userProvider;
    }

    public void setUserVariableValues(Set<Parameter> parameters, DroolsSubmittedForm droolsSubmittedForm)
            throws ElementDoesNotExistsException {
        if (parameters == null) {
            return;
        }
        for (Parameter parameter : parameters) {
            if (Objects.equals(parameter.getName(), SUBMITTER_PARAMETER)) {
                InfographicEngineLogger.debug(this.getClass(), "Processing parameter '#{}%{}%{}#'.",
                        parameter.getType(), parameter.getName(), parameter.getAttributes());
                // Search for any variable defined in the parameters
                for (String attribute : parameter.getAttributes().keySet()) {
                    //#USER%SUBMITTER%NAME#
                    try {
                        if (Objects.equals(attribute, NAME_ATTRIBUTE)) {
                            parameter.getAttributes().put(attribute, userProvider.getUserName(droolsSubmittedForm));
                        } else if (Objects.equals(attribute, LASTNAME_ATTRIBUTE)) {
                            parameter.getAttributes().put(attribute, userProvider.getUserLastname(droolsSubmittedForm));
                        }
                    } catch (Exception e) {
                        InfographicEngineLogger.errorMessage(this.getClass(), e);
                    }
                }
            }
        }
    }
}
