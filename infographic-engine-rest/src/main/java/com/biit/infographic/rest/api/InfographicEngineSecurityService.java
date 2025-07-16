package com.biit.infographic.rest.api;


import com.biit.server.rest.SecurityService;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

@Primary
@Service("securityService")
public class InfographicEngineSecurityService extends SecurityService {

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

    public String getOrganizationAdminPrivilege() {
        return "INFOGRAPHICENGINE_ORGANIZATION_ADMIN";
    }
}
