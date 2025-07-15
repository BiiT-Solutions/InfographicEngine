package com.biit.infographic.core.providers;

import com.biit.drools.form.DroolsSubmittedForm;
import com.biit.infographic.logger.InfographicEngineLogger;
import com.biit.server.security.IAuthenticatedUserProvider;
import com.biit.server.security.model.IAuthenticatedUser;
import com.biit.usermanager.dto.UserDTO;
import com.biit.utils.pool.BasePool;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class UserProvider extends BasePool<String, UserDTO> {

    private static final Long USER_POOL_TIME = (long) (10 * 60 * 1000);

    private final IAuthenticatedUserProvider<UserDTO> authenticatedUserProvider;

    public UserProvider(IAuthenticatedUserProvider<UserDTO> authenticatedUserProvider) {
        this.authenticatedUserProvider = authenticatedUserProvider;
    }

    public String getUserName(DroolsSubmittedForm droolsSubmittedForm) {
        final IAuthenticatedUser authenticatedUser = getUser(droolsSubmittedForm.getSubmittedBy());
        if (authenticatedUser != null) {
            return authenticatedUser.getName();
        }
        return null;
    }

    public String getUserLastname(DroolsSubmittedForm droolsSubmittedForm) {
        final IAuthenticatedUser authenticatedUser = getUser(droolsSubmittedForm.getSubmittedBy());
        if (authenticatedUser != null) {
            return authenticatedUser.getLastname();
        }
        return null;
    }

    public UUID getUserUUID(DroolsSubmittedForm droolsSubmittedForm) {
        final IAuthenticatedUser authenticatedUser = getUser(droolsSubmittedForm.getSubmittedBy());
        if (authenticatedUser != null) {
            return UUID.fromString(authenticatedUser.getUID());
        }
        return null;
    }

    public IAuthenticatedUser getUser(String username) {
        if (username != null) {
            UserDTO user = getElement(username);
            if (user == null) {
                user = authenticatedUserProvider.findByUsername(username).orElse(null);
                if (user == null) {
                    InfographicEngineLogger.severe(this.getClass(), "No user found with username '{}'.", username);
                } else {
                    addElement(user, username);
                }
            }
            return user;
        } else {
            InfographicEngineLogger.warning(this.getClass(), "No user name provided!");
            return null;
        }
    }

    @Override
    public long getExpirationTime() {
        return USER_POOL_TIME;
    }

    @Override
    public boolean isDirty(UserDTO element) {
        return false;
    }
}
