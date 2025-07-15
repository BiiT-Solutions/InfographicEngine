package com.biit.infographic.rest.api;

import com.biit.drools.form.DroolsSubmittedForm;
import com.biit.infographic.core.engine.SvgFromDroolsConverter;
import com.biit.infographic.core.exceptions.ElementDoesNotExistsException;
import com.biit.infographic.core.exceptions.InfographicNotFoundException;
import com.biit.infographic.core.generators.SvgGenerator;
import com.biit.infographic.core.models.svg.SvgTemplate;
import com.biit.infographic.core.pdf.PdfController;
import com.biit.infographic.core.providers.GeneratedInfographicProvider;
import com.biit.infographic.persistence.entities.GeneratedInfographic;
import com.biit.infographic.rest.api.model.InfographicSearch;
import com.biit.server.exceptions.BadRequestException;
import com.biit.server.exceptions.NotFoundException;
import com.biit.server.rest.CustomHeaders;
import com.biit.server.rest.SecurityService;
import com.biit.server.utils.exceptions.EmptyPdfBodyException;
import com.biit.server.utils.exceptions.InvalidXmlElementException;
import com.biit.usermanager.client.providers.UserManagerClient;
import com.biit.usermanager.dto.UserDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/svg")
public class SvgServices extends ImageServices {

    private final SvgFromDroolsConverter svgFromDroolsConverter;

    private final GeneratedInfographicProvider generatedInfographicProvider;

    private final PdfController pdfController;

    private final UserManagerClient userManagerClient;

    public SvgServices(SecurityService securityService, SvgFromDroolsConverter svgFromDroolsConverter,
                       GeneratedInfographicProvider generatedInfographicProvider, PdfController pdfController,
                       UserManagerClient userManagerClient) {
        super(securityService);
        this.svgFromDroolsConverter = svgFromDroolsConverter;
        this.generatedInfographicProvider = generatedInfographicProvider;
        this.pdfController = pdfController;
        this.userManagerClient = userManagerClient;
    }


    @PreAuthorize("hasAnyAuthority(@securityService.viewerPrivilege, @securityService.editorPrivilege, @securityService.adminPrivilege)")
    @Operation(summary = "Converts a template to a SVG. No post processing is done.", security = @SecurityRequirement(name = "bearerAuth"))
    @PostMapping(value = "/create", consumes = MediaType.APPLICATION_JSON_VALUE, produces = {MediaType.TEXT_PLAIN_VALUE, MediaType.APPLICATION_JSON_VALUE}
    )
    public String create(@RequestBody SvgTemplate svgTemplate, Authentication authentication, HttpServletResponse response,
                         HttpServletRequest request) {
        return SvgGenerator.generate(svgTemplate);
    }


    @PreAuthorize("hasAnyAuthority(@securityService.viewerPrivilege, @securityService.editorPrivilege, @securityService.adminPrivilege)")
    @Operation(summary = "Generates a SVG from a drools input. The template must be installed on the system",
            security = @SecurityRequirement(name = "bearerAuth"))
    @PostMapping(value = "/create/drools", consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = {MediaType.TEXT_PLAIN_VALUE, MediaType.APPLICATION_JSON_VALUE})
    public List<String> createFromDrools(@RequestHeader(name = CustomHeaders.TIMEZONE_HEADER, required = false) String timeZoneHeader,
                                         @RequestBody DroolsSubmittedForm droolsForm, Authentication authentication, HttpServletResponse response,
                                         HttpServletRequest request) {
        return svgFromDroolsConverter.executeFromTemplates(droolsForm, authentication.getName(), timeZoneHeader, request.getLocale());
    }


    @Operation(summary = "Generates a SVG from a drools input. The template must be on the system.", description = """
            - Locale from infographic is obtained from the 'Accept-Language' header or the locale obtained by the user who has send the form.
            - Timezone is obtained from 'X-Time-Zone' header.
            """, security = @SecurityRequirement(name = "bearerAuth"),
            parameters = {
                    @Parameter(in = ParameterIn.HEADER,
                            name = "Accept-Language",
                            description = "Language requested for the texts",
                            example = "en-EN",
                            schema = @Schema(type = "string"))})
    @PreAuthorize("hasAnyAuthority(@securityService.viewerPrivilege, @securityService.editorPrivilege, @securityService.adminPrivilege)")
    @PostMapping(value = "/create/drools/plain", consumes = MediaType.TEXT_PLAIN_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public List<String> executeDroolsEngineFromText(@RequestHeader(name = CustomHeaders.TIMEZONE_HEADER, required = false) String timeZoneHeader,
                                                    @RequestBody final String droolsFormContent, Authentication authentication,
                                                    HttpServletRequest request) {
        final DroolsSubmittedForm droolsSubmittedForm;
        try {
            droolsSubmittedForm = DroolsSubmittedForm.getFromJson(droolsFormContent);
        } catch (JsonProcessingException ex) {
            throw new BadRequestException(this.getClass(), "Input cannot be converted to drools result.");
        }
        return svgFromDroolsConverter.executeFromTemplates(droolsSubmittedForm, authentication.getName(), timeZoneHeader, request.getLocale());
    }


    @Operation(summary = "Generates one SVG from a drools input. If multiples templates are generated, only the selected one is returned."
            + " The template must be on the system.", description = """
            - Locale from infographic is obtained from the 'Accept-Language' header or the locale obtained by the user who has send the form.
            - Timezone is obtained from 'X-Time-Zone' header.
            """, security = @SecurityRequirement(name = "bearerAuth"),
            parameters = {
                    @Parameter(in = ParameterIn.HEADER,
                            name = "Accept-Language",
                            description = "Language requested for the texts",
                            example = "en-EN",
                            schema = @Schema(type = "string"))})
    @PreAuthorize("hasAnyAuthority(@securityService.viewerPrivilege, @securityService.editorPrivilege, @securityService.adminPrivilege)")
    @PostMapping(value = "/create/drools/plain/page/{index}", consumes = MediaType.TEXT_PLAIN_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public String getFirst(@RequestHeader(name = CustomHeaders.TIMEZONE_HEADER, required = false) String timeZoneHeader,
                           @PathVariable("index") Integer index,
                           @RequestBody final String droolsFormContent,
                           Authentication authentication, HttpServletResponse response, HttpServletRequest request) {
        final DroolsSubmittedForm droolsSubmittedForm;
        try {
            droolsSubmittedForm = DroolsSubmittedForm.getFromJson(droolsFormContent);
        } catch (JsonProcessingException ex) {
            throw new BadRequestException(this.getClass(), "Input cannot be converted to drools result.");
        }
        final List<String> svg = svgFromDroolsConverter.executeFromTemplates(droolsSubmittedForm, authentication.getName(),
                timeZoneHeader, request.getLocale());
        if (svg.isEmpty()) {
            throw new ElementDoesNotExistsException(this.getClass(), "No svg obtained from this input.");
        }
        if (index == null || svg.size() == 1) {
            return svg.get(0);
        }
        try {
            final ContentDisposition contentDisposition = ContentDisposition.builder("attachment")
                    .filename("Infographic.svg").build();
            response.setHeader(HttpHeaders.CONTENT_DISPOSITION, contentDisposition.toString());
            return svg.get(index);
        } catch (ArrayIndexOutOfBoundsException e) {
            throw new BadRequestException(this.getClass(), "No index '" + index
                    + "' available. Total SVG generated are '" + svg.size() + "'.");
        }
    }


    @Operation(summary = "Search results as PDF generated by drools.", description = """
            Parameters:
            - form: the form name.
            - version: the form version.
            - createdBy: who has filled up the form. If no user is selected by default is the authenticated user.
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
    @GetMapping(value = "/find/latest/pdf", produces = {MediaType.APPLICATION_PDF_VALUE, MediaType.APPLICATION_JSON_VALUE})
    public byte[] getLatestAsPdf(
            @Parameter(name = "form", required = false) @RequestParam(value = "form", required = false) String form,
            @Parameter(name = "version", required = false) @RequestParam(value = "version", required = false) Integer version,
            @Parameter(name = "createdBy", required = false) @RequestParam(value = "createdBy", required = false) String createdBy,
            @Parameter(name = "createdByExternalReference", required = false) @RequestParam(value = "createdByExternalReference", required = false)
            String externalReference,
            @Parameter(name = "organization", required = false) @RequestParam(value = "organization", required = false) String organization,
            @Parameter(name = "unit", required = false) @RequestParam(value = "unit", required = false) String unit,
            @RequestHeader(name = CustomHeaders.TIMEZONE_HEADER, required = false) String timeZoneHeader,
            Authentication authentication, HttpServletRequest request, HttpServletResponse response)
            throws InvalidXmlElementException, EmptyPdfBodyException {
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

        Optional<GeneratedInfographic> generatedInfographic = generatedInfographicProvider
                .processLatest(form, version, organization, unit, createdBy, timeZoneHeader, request.getLocale());

        if (generatedInfographic.isEmpty()) {
            //For testing it is possible to have an infographic without a drools form. But for production, this will always must return NOT FOUND.
            generatedInfographic = generatedInfographicProvider.findLatest(form, version, createdBy, organization, unit);
            if (generatedInfographic.isEmpty()) {
                throw new InfographicNotFoundException(this.getClass(), "No infographic found!");
            }
        }

        if (generatedInfographic.get().getSvgContents() == null
                || generatedInfographic.get().getSvgContents().isEmpty()) {
            throw new NotFoundException(this.getClass(), "No infographic found!");
        }

        final byte[] bytes = pdfController.generatePdfFromSvgs(generatedInfographic.get().getSvgContents());
        final ContentDisposition contentDisposition = ContentDisposition.builder("attachment")
                .filename((form != null ? form : "infographic") + ".pdf").build();
        response.setHeader(HttpHeaders.CONTENT_DISPOSITION, contentDisposition.toString());
        return bytes;
    }


    @Operation(summary = "Search results as PDF generated by drools.", description = """
            Received a list of infographics, and the system puts together as one PDF document.
            Only the last version from each infographic is returned.
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
    @PostMapping(value = "/find/latest/pdf", produces = {MediaType.APPLICATION_PDF_VALUE, MediaType.APPLICATION_JSON_VALUE})
    public byte[] getAsPdf(
            @RequestHeader(name = CustomHeaders.TIMEZONE_HEADER, required = false) String timeZoneHeader,
            @RequestBody List<InfographicSearch> infographicSearches,
            Authentication authentication, HttpServletRequest request, HttpServletResponse response)
            throws InvalidXmlElementException, EmptyPdfBodyException {

        final ArrayList<String> svgCodes = new ArrayList<>();

        for (InfographicSearch infographicSearch : infographicSearches) {
            canBeDoneByDifferentUsers(infographicSearch.getCreatedBy(), authentication);
            Optional<GeneratedInfographic> generatedInfographic = generatedInfographicProvider
                    .processLatest(infographicSearch.getForm(), infographicSearch.getVersion(),
                            infographicSearch.getOrganization(), infographicSearch.getUnit(), infographicSearch.getCreatedBy(),
                            timeZoneHeader, request.getLocale());

            if (generatedInfographic.isEmpty()) {
                //For testing it is possible to have an infographic without a drools form. But for production, this will always must return NOT FOUND.
                generatedInfographic = generatedInfographicProvider.findLatest(infographicSearch.getForm(), infographicSearch.getVersion(),
                        infographicSearch.getCreatedBy(), infographicSearch.getOrganization(), infographicSearch.getUnit());
            }

            generatedInfographic.ifPresent(infographic -> svgCodes.addAll(infographic.getSvgContents()));
        }

        if (svgCodes.isEmpty()) {
            throw new InfographicNotFoundException(this.getClass(), "No infographics found!");
        }

        final byte[] bytes = pdfController.generatePdfFromSvgs(svgCodes);
        final ContentDisposition contentDisposition = ContentDisposition.builder("attachment")
                .filename("infographic.pdf").build();
        response.setHeader(HttpHeaders.CONTENT_DISPOSITION, contentDisposition.toString());
        return bytes;
    }
}
