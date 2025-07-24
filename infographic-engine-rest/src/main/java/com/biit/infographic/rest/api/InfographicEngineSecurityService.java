package com.biit.infographic.rest.api;


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
