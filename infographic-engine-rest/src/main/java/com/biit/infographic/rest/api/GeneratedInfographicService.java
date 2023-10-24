package com.biit.infographic.rest.api;

import com.biit.infographic.core.controllers.GeneratedInfographicController;
import com.biit.infographic.core.converters.GeneratedInfographicConverter;
import com.biit.infographic.core.converters.models.GeneratedInfographicConverterRequest;
import com.biit.infographic.core.models.GeneratedInfographicDTO;
import com.biit.infographic.core.providers.GeneratedInfographicProvider;
import com.biit.infographic.persistence.entities.GeneratedInfographic;
import com.biit.infographic.persistence.repositories.GeneratedInfographicRepository;
import com.biit.server.rest.BasicServices;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PostFilter;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.Collection;
import java.util.List;

@RestController
@RequestMapping("/infographic")
@SecurityRequirement(name = "bearerAuth")
public class GeneratedInfographicService extends BasicServices<GeneratedInfographic, Long, GeneratedInfographicDTO, GeneratedInfographicRepository,
        GeneratedInfographicProvider, GeneratedInfographicConverterRequest, GeneratedInfographicConverter, GeneratedInfographicController> {

    protected GeneratedInfographicService(GeneratedInfographicController controller) {
        super(controller);
    }

    @PostFilter("hasAnyAuthority(@securityService.adminPrivilege)")
    @Operation(summary = "Gets all", security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<GeneratedInfographicDTO> getAll(HttpServletRequest request) {
        return super.getAll(request);
    }

    @Operation(hidden = true)
    @Override
    public GeneratedInfographicDTO add(@RequestBody GeneratedInfographicDTO dto, Authentication authentication, HttpServletRequest request) {
        throw new UnsupportedOperationException("Method not valid!");
    }

    @Operation(hidden = true)
    @Override
    public List<GeneratedInfographicDTO> add(@RequestBody Collection<GeneratedInfographicDTO> dtos, Authentication authentication, HttpServletRequest request) {
        throw new UnsupportedOperationException("Method not valid!");
    }

    @Operation(hidden = true)
    @Override
    public GeneratedInfographicDTO update(@RequestBody GeneratedInfographicDTO dto, Authentication authentication, HttpServletRequest request) {
        throw new UnsupportedOperationException("Method not valid!");
    }

    @Operation(hidden = true)
    @Override
    public void delete(@RequestBody GeneratedInfographicDTO dto, Authentication authentication, HttpServletRequest request) {
        throw new UnsupportedOperationException("Method not valid!");
    }

    @Operation(hidden = true)
    @Override
    public void delete(Long id, HttpServletRequest request) {
        throw new UnsupportedOperationException("Method not valid!");
    }

    @Operation(summary = "Search infographics.", description = """
            Parameters:
            - form: the form name.
            - version: the form version.
            - createdBy: who has filled up the form.
            - organization: which organization the form belongs to.
            - startDate: filtering forms from this day.
            - endDate: filtering facts to this day.
            """,
            security = @SecurityRequirement(name = "bearerAuth"))
    @PreAuthorize("hasAnyAuthority(@securityService.viewerPrivilege, @securityService.editorPrivilege, @securityService.adminPrivilege)")
    @ResponseStatus(value = HttpStatus.OK)
    @GetMapping(value = "/find", produces = MediaType.APPLICATION_JSON_VALUE)
    public Collection<GeneratedInfographicDTO> getForms(
            HttpServletRequest httpRequest,
            @Parameter(name = "form", required = false) @RequestParam(value = "form", required = false) String form,
            @Parameter(name = "version", required = false) @RequestParam(value = "version", required = false) Integer version,
            @Parameter(name = "createdBy", required = false) @RequestParam(value = "createdBy", required = false) String createdBy,
            @Parameter(name = "organization", required = false) @RequestParam(value = "organization", required = false) Long organization,
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) @Parameter(description = "Forms until the selected date", example = "2023-01-01T00:00:00.00Z")
            @RequestParam(value = "from", required = false) OffsetDateTime from,
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) @Parameter(description = "Forms until the selected date", example = "2023-01-31T23:59:59.99Z")
            @RequestParam(value = "to", required = false) OffsetDateTime to,
            Authentication authentication, HttpServletRequest request) {

        canBeDoneForDifferentUsers(createdBy, authentication);

        return getController().findBy(form, version, organization, createdBy,
                from != null ? LocalDateTime.ofInstant(from.toInstant(), ZoneId.systemDefault()) : null,
                to != null ? LocalDateTime.ofInstant(to.toInstant(), ZoneId.systemDefault()) : null);
    }

    @Operation(summary = "Search results generated by drools.", description = """
            Parameters:
            - form: the form name.
            - version: the form version.
            - createdBy: who has filled up the form.
            - organization: which organization the form belongs to.
            - startDate: filtering forms from this day.
            - endDate: filtering facts to this day.
            """,
            security = @SecurityRequirement(name = "bearerAuth"))
    @PreAuthorize("hasAnyAuthority(@securityService.viewerPrivilege, @securityService.editorPrivilege, @securityService.adminPrivilege)")
    @ResponseStatus(value = HttpStatus.OK)
    @GetMapping(value = "/find/latest", produces = MediaType.APPLICATION_JSON_VALUE)
    public GeneratedInfographicDTO getLatest(
            HttpServletRequest httpRequest,
            @Parameter(name = "form", required = false) @RequestParam(value = "form", required = false) String form,
            @Parameter(name = "version", required = false) @RequestParam(value = "version", required = false) Integer version,
            @Parameter(name = "createdBy", required = false) @RequestParam(value = "createdBy", required = false) String createdBy,
            @Parameter(name = "organization", required = false) @RequestParam(value = "organization", required = false) Long organization,
            Authentication authentication, HttpServletRequest request) {

        canBeDoneForDifferentUsers(createdBy, authentication);

        return getController().findLatest(form, version, organization, createdBy);
    }
}
