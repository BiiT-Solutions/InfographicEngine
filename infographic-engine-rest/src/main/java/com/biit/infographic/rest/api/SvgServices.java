package com.biit.infographic.rest.api;

import com.biit.infographic.core.models.svg.SvgTemplate;
import com.biit.infographic.core.svg.SvgGenerator;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/svg")
public class SvgServices {

    @PreAuthorize("hasAnyAuthority(@securityService.viewerPrivilege, @securityService.editorPrivilege, @securityService.adminPrivilege)")
    @Operation(summary = "Generates a SVG from a template", security = @SecurityRequirement(name = "bearerAuth"))
    @PostMapping(value = "/create", produces = {MediaType.TEXT_PLAIN_VALUE, MediaType.APPLICATION_JSON_VALUE},
            consumes = MediaType.APPLICATION_JSON_VALUE)
    public String getAll(@RequestBody SvgTemplate svgTemplate, Authentication authentication, HttpServletRequest request) {
        return SvgGenerator.generate(svgTemplate);
    }
}
