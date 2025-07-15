package com.biit.infographic.rest.api;

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
