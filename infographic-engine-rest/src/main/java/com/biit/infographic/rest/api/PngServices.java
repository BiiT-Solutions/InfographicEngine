package com.biit.infographic.rest.api;

import com.biit.infographic.core.generators.PngGenerator;
import com.biit.infographic.core.models.svg.SvgTemplate;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/png")
public class PngServices {

    @PreAuthorize("hasAnyAuthority(@securityService.viewerPrivilege, @securityService.editorPrivilege, @securityService.adminPrivilege)")
    @Operation(summary = "Generates a PNG from a template", security = @SecurityRequirement(name = "bearerAuth"))
    @PostMapping(value = "/create", produces = {MediaType.APPLICATION_OCTET_STREAM_VALUE, MediaType.APPLICATION_JSON_VALUE},
            consumes = MediaType.APPLICATION_JSON_VALUE)
    public byte[] getAll(@RequestBody SvgTemplate svgTemplate, Authentication authentication, HttpServletResponse response,
                         HttpServletRequest request) {
        final ContentDisposition contentDisposition = ContentDisposition.builder("attachment")
                .filename("Infographic.png").build();
        response.setHeader(HttpHeaders.CONTENT_DISPOSITION, contentDisposition.toString());
        return PngGenerator.generate(svgTemplate);
    }
}
