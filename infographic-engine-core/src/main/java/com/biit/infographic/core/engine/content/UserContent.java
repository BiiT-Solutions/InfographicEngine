package com.biit.infographic.core.engine.content;

import com.biit.drools.form.DroolsSubmittedForm;
import com.biit.infographic.core.engine.Parameter;
import com.biit.infographic.core.exceptions.ElementDoesNotExistsException;
import com.biit.infographic.logger.InfographicEngineLogger;
import com.biit.server.security.IAuthenticatedUser;
import com.biit.usermanager.client.provider.UserManagerClient;
import com.biit.utils.pool.BasePool;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.Set;

@Component
public class UserContent {

    private static final String SUBMITTER_PARAMETER = "SUBMITTER";
    private static final String NAME_ATTRIBUTE = "NAME";
    private static final String LASTNAME_ATTRIBUTE = "LASTNAME";

    private static final Long USER_POOL_TIME = (long) (10 * 60 * 1000);

    private final UserManagerClient userManagerClient;
    private final UserPool userPool;

    public UserContent(UserManagerClient userManagerClient) {
        this.userManagerClient = userManagerClient;
        userPool = new UserPool();
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
                    if (Objects.equals(attribute, NAME_ATTRIBUTE)) {
                        parameter.getAttributes().put(attribute, getUserName(droolsSubmittedForm));
                    } else if (Objects.equals(attribute, LASTNAME_ATTRIBUTE)) {
                        parameter.getAttributes().put(attribute, getUserLastname(droolsSubmittedForm));
                    }
                }
            }
        }
    }

    private String getUserName(DroolsSubmittedForm droolsSubmittedForm) {
        final IAuthenticatedUser authenticatedUser = getUser(droolsSubmittedForm.getSubmittedBy());
        if (authenticatedUser != null) {
            return authenticatedUser.getName();
        }
        return null;
    }

    private String getUserLastname(DroolsSubmittedForm droolsSubmittedForm) {
        final IAuthenticatedUser authenticatedUser = getUser(droolsSubmittedForm.getSubmittedBy());
        if (authenticatedUser != null) {
            return authenticatedUser.getLastname();
        }
        return null;
    }

    private IAuthenticatedUser getUser(String username) {
        if (username != null) {
            IAuthenticatedUser user = userPool.getElement(username);
            if (user == null) {
                user = userManagerClient.findByUsername(username).orElse(null);
                if (user == null) {
                    InfographicEngineLogger.severe(this.getClass(), "No user found with username '{}'.", username);
                } else {
                    userPool.addElement(user, username);
                }
            }
            return user;
        } else {
            InfographicEngineLogger.warning(this.getClass(), "No user name provided!");
            return null;
        }
    }


    static class UserPool extends BasePool<String, IAuthenticatedUser> {

        @Override
        public long getExpirationTime() {
            return USER_POOL_TIME;
        }

        @Override
        public boolean isDirty(IAuthenticatedUser element) {
            return false;
        }
    }
}
