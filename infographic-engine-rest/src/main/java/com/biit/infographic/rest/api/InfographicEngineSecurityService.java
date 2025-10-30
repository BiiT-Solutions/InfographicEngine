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
import com.biit.server.security.IUserOrganizationProvider;
import com.biit.server.security.model.IUserOrganization;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.List;

@Primary
@Service("securityService")
public class InfographicEngineSecurityService extends SecurityService {

    public InfographicEngineSecurityService(List<IUserOrganizationProvider<? extends IUserOrganization>> userOrganizationProviders) {
        super(userOrganizationProviders);
    }


    @Override
    public String getViewerPrivilege() {
        return "INFOGRAPHICENGINE_VIEWER";
    }

    @Override
    public String getAdminPrivilege() {
        return "INFOGRAPHICENGINE_ADMIN";
    }

    @Override
    public String getEditorPrivilege() {
        return "INFOGRAPHICENGINE_EDITOR";
    }

    @Override
    public String getOrganizationAdminPrivilege() {
        return "INFOGRAPHICENGINE_ORGANIZATION_ADMIN";
    }
}
