package com.biit.infographic.rest.api;

import com.biit.infographic.core.controllers.GeneratedInfographicController;
import com.biit.infographic.core.converters.GeneratedInfographicConverter;
import com.biit.infographic.core.converters.models.GeneratedInfographicConverterRequest;
import com.biit.infographic.core.models.GeneratedInfographicAsJpegDTO;
import com.biit.infographic.core.models.GeneratedInfographicAsPngDTO;
import com.biit.infographic.core.models.GeneratedInfographicDTO;
import com.biit.infographic.core.providers.GeneratedInfographicProvider;
import com.biit.infographic.persistence.entities.GeneratedInfographic;
import com.biit.infographic.persistence.repositories.GeneratedInfographicRepository;
import com.biit.server.providers.StorableObjectProvider;
import com.biit.server.rest.CustomHeaders;
import com.biit.server.rest.ElementServices;
import com.biit.usermanager.client.providers.UserManagerClient;
import com.biit.usermanager.dto.UserDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Schema;
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
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

@RestController
@RequestMapping("/infographics")
@SecurityRequirement(name = "bearerAuth")
public class GeneratedInfographicService extends ElementServices<GeneratedInfographic, Long, GeneratedInfographicDTO, GeneratedInfographicRepository,
        GeneratedInfographicProvider, GeneratedInfographicConverterRequest, GeneratedInfographicConverter, GeneratedInfographicController> {

    private final UserManagerClient userManagerClient;
    private final InfographicEngineSecurityService securityService;

    protected GeneratedInfographicService(GeneratedInfographicController controller, UserManagerClient userManagerClient,
                                          InfographicEngineSecurityService securityService) {
        super(controller);
        this.userManagerClient = userManagerClient;
        this.securityService = securityService;
    }

    @Override
    @PostFilter("hasAnyAuthority(@securityService.adminPrivilege)")
    @Operation(summary = "Gets all", security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<GeneratedInfographicDTO> getAll(
            @RequestParam(name = "page", defaultValue = "0") Optional<Integer> page,
            @RequestParam(name = "size", defaultValue = StorableObjectProvider.MAX_PAGE_SIZE + "") Optional<Integer> size,
            Authentication authentication, HttpServletRequest request) {
        return super.getAll(page, size, authentication, request);
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
    public void delete(Long id, Authentication authentication, HttpServletRequest request) {
        throw new UnsupportedOperationException("Method not valid!");
    }

    @Operation(summary = "Search for stored infographics.", description = """
            Parameters:
            - form: the form name.
            - version: the form version.
            - createdBy: who has filled up the form. If no organization is selected by default is the authenticated user.
            - createdByExternalReference: who has filled up the form. Using an external reference for a 3rd party application.
            - organization: which organization the form belongs to.
            - unit: related to a team, department or any other group of users.
            - startDate: filtering forms from this day.
            - endDate: filtering facts to this day.
            """,
            security = @SecurityRequirement(name = "bearerAuth"))
    @PreAuthorize("hasAnyAuthority(@securityService.viewerPrivilege, @securityService.editorPrivilege, @securityService.adminPrivilege,"
            + "@securityService.organizationAdminPrivilege)")
    @ResponseStatus(value = HttpStatus.OK)
    @GetMapping(value = "/find", produces = MediaType.APPLICATION_JSON_VALUE)
    public Collection<GeneratedInfographicDTO> getForms(
            HttpServletRequest httpRequest,
            @Parameter(name = "form", required = false) @RequestParam(value = "form", required = false) String form,
            @Parameter(name = "version", required = false) @RequestParam(value = "version", required = false) Integer version,
            @Parameter(name = "createdBy", required = false) @RequestParam(value = "createdBy", required = false) String createdBy,
            @Parameter(name = "createdByExternalReference", required = false) @RequestParam(value = "createdByExternalReference", required = false)
            String externalReference,
            @Parameter(name = "organization", required = false) @RequestParam(value = "organization", required = false) String organization,
            @Parameter(name = "unit", required = false) @RequestParam(value = "unit", required = false) String unit,
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) @Parameter(description = "Forms until the selected date", example = "2023-01-01T00:00:00.00Z")
            @RequestParam(value = "from", required = false) OffsetDateTime from,
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) @Parameter(description = "Forms until the selected date", example = "2023-01-31T23:59:59.99Z")
            @RequestParam(value = "to", required = false) OffsetDateTime to,
            Authentication authentication, HttpServletRequest request) {

        if (createdBy == null && organization == null) {
            if (externalReference == null) {
                createdBy = authentication.getName();
            } else {
                final Optional<UserDTO> user = userManagerClient.findByExternalReference(externalReference);
                if (user.isPresent()) {
                    createdBy = user.get().getUsername();
                }
            }
        }

        organization = isAllowedOrganization(organization, authentication, securityService.getAdminPrivilege(), securityService.getEditorPrivilege());

        return getController().findBy(form, version, organization, unit, createdBy,
                from != null ? LocalDateTime.ofInstant(from.toInstant(), ZoneId.systemDefault()) : null,
                to != null ? LocalDateTime.ofInstant(to.toInstant(), ZoneId.systemDefault()) : null);
    }

    @Operation(summary = "Search results generated by drools.", description = """
            Parameters:
            - form: the form name.
            - version: the form version. If not set, latest available will be used.
            - createdBy: who has filled up the form. If no organization is selected by default is the authenticated user.
            - createdByExternalReference: who has filled up the form. Using an external reference for a 3rd party application.
            - organization: which organization the form belongs to.
            - unit: related to a team, department or any other group of users.
            - startDate: filtering forms from this day.
            - endDate: filtering facts to this day.
            - Locale from infographic is obtained from the 'Accept-Language' header or the locale obtained by the user who has send the form.
            - Timezone is obtained from 'X-Time-Zone' header.
            """,
            security = @SecurityRequirement(name = "bearerAuth"),
            parameters = {
                    @Parameter(in = ParameterIn.HEADER,
                            name = "Accept-Language",
                            description = "Language requested for the texts",
                            example = "en-EN",
                            schema = @Schema(type = "string"))})
    @PreAuthorize("hasAnyAuthority(@securityService.viewerPrivilege, @securityService.editorPrivilege, @securityService.adminPrivilege,"
            + "@securityService.organizationAdminPrivilege)")
    @ResponseStatus(value = HttpStatus.OK)
    @GetMapping(value = "/find/latest", produces = MediaType.APPLICATION_JSON_VALUE)
    public GeneratedInfographicDTO getLatest(
            @RequestHeader(name = CustomHeaders.TIMEZONE_HEADER, required = false) String timeZoneHeader,
            @Parameter(name = "form", required = false) @RequestParam(value = "form", required = false) String form,
            @Parameter(name = "version", required = false) @RequestParam(value = "version", required = false) Integer version,
            @Parameter(name = "createdBy", required = false) @RequestParam(value = "createdBy", required = false) String createdBy,
            @Parameter(name = "createdByExternalReference", required = false) @RequestParam(value = "createdByExternalReference", required = false)
            String externalReference,
            @Parameter(name = "organization", required = false) @RequestParam(value = "organization", required = false) String organization,
            @Parameter(name = "unit", required = false) @RequestParam(value = "unit", required = false) String unit,
            Authentication authentication, HttpServletRequest request) {

        if (createdBy == null && organization == null) {
            if (externalReference == null) {
                createdBy = authentication.getName();
            } else {
                final Optional<UserDTO> user = userManagerClient.findByExternalReference(externalReference);
                if (user.isPresent()) {
                    createdBy = user.get().getUsername();
                }
            }
        }

        canBeDoneByDifferentUsers(createdBy, authentication);

        organization = isAllowedOrganization(organization, authentication, securityService.getAdminPrivilege(), securityService.getEditorPrivilege());

        return getController().processLatest(form, version, organization, unit, createdBy, timeZoneHeader, request.getLocale());
    }


    @Operation(summary = "Search results generated by drools.", description = """
            Parameters:
            - form: the form name.
            - version: the form version. If not set, latest available will be used.
            - createdBy: who has filled up the form. If no organization is selected by default is the authenticated user.
            - createdByExternalReference: who has filled up the form. Using an external reference for a 3rd party application.
            - organization: which organization the form belongs to.
            - unit: related to a team, department or any other group of users.
            - startDate: filtering forms from this day.
            - endDate: filtering facts to this day.
            - Locale from infographic is obtained from the 'Accept-Language' header or the locale obtained by the user who has send the form.
            - Timezone is obtained from 'X-Time-Zone' header.
            """,
            security = @SecurityRequirement(name = "bearerAuth"),
            parameters = {
                    @Parameter(in = ParameterIn.HEADER,
                            name = "Accept-Language",
                            description = "Language requested for the texts",
                            example = "en-EN",
                            schema = @Schema(type = "string"))})
    @PreAuthorize("hasAnyAuthority(@securityService.viewerPrivilege, @securityService.editorPrivilege, @securityService.adminPrivilege)")
    @ResponseStatus(value = HttpStatus.OK)
    @GetMapping(value = "/find/latest/png", produces = {MediaType.APPLICATION_OCTET_STREAM_VALUE, MediaType.APPLICATION_JSON_VALUE})
    public GeneratedInfographicAsPngDTO getLatestAsPng(
            @RequestHeader(name = CustomHeaders.TIMEZONE_HEADER, required = false) String timeZoneHeader,
            @Parameter(name = "form", required = false) @RequestParam(value = "form", required = false) String form,
            @Parameter(name = "version", required = false) @RequestParam(value = "version", required = false) Integer version,
            @Parameter(name = "createdBy", required = false) @RequestParam(value = "createdBy", required = false) String createdBy,
            @Parameter(name = "createdByExternalReference", required = false) @RequestParam(value = "createdByExternalReference", required = false)
            String externalReference,
            @Parameter(name = "organization", required = false) @RequestParam(value = "organization", required = false) String organization,
            @Parameter(name = "unit", required = false) @RequestParam(value = "unit", required = false) String unit,
            Authentication authentication, HttpServletRequest request) {

        if (createdBy == null && organization == null) {
            if (externalReference == null) {
                createdBy = authentication.getName();
            } else {
                final Optional<UserDTO> user = userManagerClient.findByExternalReference(externalReference);
                if (user.isPresent()) {
                    createdBy = user.get().getUsername();
                }
            }
        }

        canBeDoneByDifferentUsers(createdBy, authentication);

        organization = isAllowedOrganization(organization, authentication, securityService.getAdminPrivilege(), securityService.getEditorPrivilege());

        return GeneratedInfographicAsPngDTO.from(getController().processLatest(form, version, organization, unit, createdBy,
                timeZoneHeader, request.getLocale()));
    }


    @Operation(summary = "Search results generated by drools.", description = """
            Parameters:
            - form: the form name.
            - version: the form version. If not set, latest available will be used.
            - createdBy: who has filled up the form. If no organization is selected by default is the authenticated user.
            - createdByExternalReference: who has filled up the form. Using an external reference for a 3rd party application.
            - organization: which organization the form belongs to.
            - unit: related to a team, department or any other group of users.
            - startDate: filtering forms from this day.
            - endDate: filtering facts to this day.
            - Locale from infographic is obtained from the 'Accept-Language' header or the locale obtained by the user who has send the form.
            - Timezone is obtained from 'X-Time-Zone' header.
            """,
            security = @SecurityRequirement(name = "bearerAuth"),
            parameters = {
                    @Parameter(in = ParameterIn.HEADER,
                            name = "Accept-Language",
                            description = "Language requested for the texts",
                            example = "en-EN",
                            schema = @Schema(type = "string"))})
    @PreAuthorize("hasAnyAuthority(@securityService.viewerPrivilege, @securityService.editorPrivilege, @securityService.adminPrivilege)")
    @ResponseStatus(value = HttpStatus.OK)
    @GetMapping(value = "/find/latest/jpeg", produces = {MediaType.APPLICATION_OCTET_STREAM_VALUE, MediaType.APPLICATION_JSON_VALUE})
    public GeneratedInfographicAsJpegDTO getLatestAsJpeg(
            @RequestHeader(name = CustomHeaders.TIMEZONE_HEADER, required = false) String timeZoneHeader,
            @Parameter(name = "form", required = false) @RequestParam(value = "form", required = false) String form,
            @Parameter(name = "version", required = false) @RequestParam(value = "version", required = false) Integer version,
            @Parameter(name = "createdBy", required = false) @RequestParam(value = "createdBy", required = false) String createdBy,
            @Parameter(name = "createdByExternalReference", required = false) @RequestParam(value = "createdByExternalReference", required = false)
            String externalReference,
            @Parameter(name = "organization", required = false) @RequestParam(value = "organization", required = false) String organization,
            @Parameter(name = "unit", required = false) @RequestParam(value = "unit", required = false) String unit,
            Authentication authentication, HttpServletRequest request) {

        if (createdBy == null && organization == null) {
            if (externalReference == null) {
                createdBy = authentication.getName();
            } else {
                final Optional<UserDTO> user = userManagerClient.findByExternalReference(externalReference);
                if (user.isPresent()) {
                    createdBy = user.get().getUsername();
                }
            }
        }

        canBeDoneByDifferentUsers(createdBy, authentication);

        organization = isAllowedOrganization(organization, authentication, securityService.getAdminPrivilege(), securityService.getEditorPrivilege());

        return GeneratedInfographicAsJpegDTO.from(getController().processLatest(form, version, organization, unit, createdBy,
                timeZoneHeader, request.getLocale()));
    }


    @Operation(summary = "Search results generated by drools.", description = """
            Parameters:
            - form: the form name.
            - version: the form version. If not set, latest available will be used.
            - creators: a list of user who has filled up the form.
            """,
            security = @SecurityRequirement(name = "bearerAuth"))
    @PreAuthorize("hasAnyAuthority(@securityService.viewerPrivilege, @securityService.editorPrivilege, @securityService.adminPrivilege)")
    @ResponseStatus(value = HttpStatus.OK)
    @GetMapping(value = "/find/latest/users", produces = MediaType.APPLICATION_JSON_VALUE)
    public Map<String, GeneratedInfographicDTO> getLatest(
            HttpServletRequest httpRequest,
            @Parameter(name = "form", required = false) @RequestParam(value = "form", required = false) String form,
            @Parameter(name = "version", required = false) @RequestParam(value = "version", required = false) Integer version,
            @Parameter(name = "createdBy", required = false) @RequestParam(value = "createdBy", required = false) Set<String> creators,
            Authentication authentication, HttpServletRequest request) {
        if (creators == null || creators.isEmpty()) {
            return new HashMap<>();
        }
        creators.forEach(creator ->
                canBeDoneByDifferentUsers(creator, authentication));

        return getController().findLatest(form, version, creators);
    }
}
