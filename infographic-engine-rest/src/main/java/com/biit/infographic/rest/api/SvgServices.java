package com.biit.infographic.rest.api;

import com.biit.drools.form.DroolsSubmittedForm;
import com.biit.infographic.core.controllers.DroolsResultController;
import com.biit.infographic.core.generators.SvgGenerator;
import com.biit.infographic.core.models.svg.SvgTemplate;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/svg")
public class SvgServices {

    private final DroolsResultController droolsResultController;

    public SvgServices(DroolsResultController droolsResultController) {
        this.droolsResultController = droolsResultController;
    }

    @PreAuthorize("hasAnyAuthority(@securityService.viewerPrivilege, @securityService.editorPrivilege, @securityService.adminPrivilege)")
    @Operation(summary = "Generates a SVG from a template", security = @SecurityRequirement(name = "bearerAuth"))
    @PostMapping(value = "/create", consumes = MediaType.APPLICATION_JSON_VALUE, produces = {MediaType.TEXT_PLAIN_VALUE, MediaType.APPLICATION_JSON_VALUE}
    )
    public String create(@RequestBody SvgTemplate svgTemplate, Authentication authentication, HttpServletResponse response,
                         HttpServletRequest request) {
//        final ContentDisposition contentDisposition = ContentDisposition.builder("attachment")
//                .filename("Infographic.svg").build();
//        response.setHeader(HttpHeaders.CONTENT_DISPOSITION, contentDisposition.toString());
        return SvgGenerator.generate(svgTemplate);
    }

    @PreAuthorize("hasAnyAuthority(@securityService.viewerPrivilege, @securityService.editorPrivilege, @securityService.adminPrivilege)")
    @Operation(summary = "Generates a SVG from a drools input. The template must be on the system", security = @SecurityRequirement(name = "bearerAuth"))
    @PostMapping(value = "/create/drools", consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = {MediaType.TEXT_PLAIN_VALUE, MediaType.APPLICATION_JSON_VALUE})
    public List<String> createFromDrools(@RequestBody DroolsSubmittedForm droolsForm, Authentication authentication, HttpServletResponse response,
                                         HttpServletRequest request) {
        return droolsResultController.execute(droolsForm);
    }
}
