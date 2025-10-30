package com.biit.infographic.core.providers;

/*-
 * #%L
 * Infographic Engine v2 (Core)
 * %%
 * Copyright (C) 2022 - 2025 BiiT Sourcing Solutions S.L.
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * #L%
 */

import com.biit.drools.form.DroolsSubmittedForm;
import com.biit.infographic.logger.InfographicEngineLogger;
import com.biit.server.security.IAuthenticatedUserProvider;
import com.biit.server.security.model.IAuthenticatedUser;
import com.biit.utils.pool.BasePool;
import jakarta.ws.rs.InternalServerErrorException;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
public class UserProvider extends BasePool<String, IAuthenticatedUser> {

    private static final Long USER_POOL_TIME = (long) (10 * 60 * 1000);

    private final List<IAuthenticatedUserProvider<? extends IAuthenticatedUser>> userServices;

    public UserProvider(List<IAuthenticatedUserProvider<? extends IAuthenticatedUser>> userServices) {
        this.userServices = userServices;
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
            IAuthenticatedUser user = getElement(username);
            if (user == null) {
                if (userServices.isEmpty()) {
                    throw new InternalServerErrorException("User connector is not ready!");
                }
                user = userServices.get(0).findByUsername(username).orElse(null);
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
    public boolean isDirty(IAuthenticatedUser element) {
        return false;
    }
}
