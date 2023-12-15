package com.biit.infographic.rest.api;

import com.biit.drools.form.DroolsSubmittedForm;
import com.biit.infographic.core.controllers.DroolsResultController;
import com.biit.infographic.core.exceptions.ElementDoesNotExistsException;
import com.biit.infographic.core.generators.SvgGenerator;
import com.biit.infographic.core.models.svg.SvgTemplate;
import com.biit.server.exceptions.BadRequestException;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
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
        return droolsResultController.executeFromTemplates(droolsForm);
    }

    @PreAuthorize("hasAnyAuthority(@securityService.viewerPrivilege, @securityService.editorPrivilege, @securityService.adminPrivilege)")
    @Operation(summary = "Generates a SVG from a drools input. The template must be on the system.", security = @SecurityRequirement(name = "bearerAuth"))
    @PostMapping(value = "/create/drools/plain", consumes = MediaType.TEXT_PLAIN_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public List<String> executeDroolsEngineFromText(@RequestBody final String droolsFormContent, Authentication authentication) {
        final DroolsSubmittedForm droolsSubmittedForm;
        try {
            droolsSubmittedForm = DroolsSubmittedForm.getFromJson(droolsFormContent);
        } catch (JsonProcessingException ex) {
            throw new BadRequestException(this.getClass(), "Input cannot be converted to drools result.");
        }
        return droolsResultController.executeFromTemplates(droolsSubmittedForm);
    }

    @PreAuthorize("hasAnyAuthority(@securityService.viewerPrivilege, @securityService.editorPrivilege, @securityService.adminPrivilege)")
    @Operation(summary = "Generates one SVG from a drools input. If multiples templates are generated, only the selected one is returned."
            + " The template must be on the system.", security = @SecurityRequirement(name = "bearerAuth"))
    @PostMapping(value = "/create/drools/plain/page/{index}", consumes = MediaType.TEXT_PLAIN_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public String getFirst(@PathVariable("index") Integer index,
                           @RequestBody final String droolsFormContent,
                           Authentication authentication, HttpServletResponse response) {
        final DroolsSubmittedForm droolsSubmittedForm;
        try {
            droolsSubmittedForm = DroolsSubmittedForm.getFromJson(droolsFormContent);
        } catch (JsonProcessingException ex) {
            throw new BadRequestException(this.getClass(), "Input cannot be converted to drools result.");
        }
        final List<String> svg = droolsResultController.executeFromTemplates(droolsSubmittedForm);
        if (svg.isEmpty()) {
            throw new ElementDoesNotExistsException(this.getClass(), "No svg obtained from this input.");
        }
        final ContentDisposition contentDisposition = ContentDisposition.builder("attachment")
                .filename("Infographic.svg").build();
        response.setHeader(HttpHeaders.CONTENT_DISPOSITION, contentDisposition.toString());
        if (index == null || svg.size() == 1) {
            return svg.get(0);
        }
        try {
            return svg.get(index);
        } catch (ArrayIndexOutOfBoundsException e) {
            throw new BadRequestException(this.getClass(), "No index '" + index
                    + "' available. Total SVG generated are '" + svg.size() + "'.");
        }
    }
}
