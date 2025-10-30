package com.biit.infographic.rest.api;

/*-
 * #%L
 * Infographic Engine v2 (Rest)
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

import com.biit.server.rest.SecurityService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.server.MethodNotAllowedException;

import java.util.List;
import java.util.Objects;

public abstract class ImageServices {

    private final SecurityService securityService;

    protected ImageServices(SecurityService securityService) {
        this.securityService = securityService;
    }

    protected void canBeDoneByDifferentUsers(String userName, Authentication authentication) {
        if (userName != null && !Objects.equals(userName, authentication.getName())) {
            final List<String> grantedAuthorities = authentication.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList();
            if (!grantedAuthorities.contains(securityService.getAdminPrivilege()) && !grantedAuthorities.contains(this.securityService.getEditorPrivilege())) {
                throw new MethodNotAllowedException("You are not allowed to search information from other users.", null);
            }
        }
    }
}
