package com.biit.infographic.rest.api;

import com.biit.drools.form.DroolsSubmittedForm;
import com.biit.infographic.core.controllers.DroolsResultController;
import com.biit.infographic.core.exceptions.ElementDoesNotExistsException;
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
import com.fasterxml.jackson.core.JsonProcessingException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
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

    private final DroolsResultController droolsResultController;

    private final GeneratedInfographicProvider generatedInfographicProvider;

    private final PdfController pdfController;

    public SvgServices(SecurityService securityService, DroolsResultController droolsResultController,
                       GeneratedInfographicProvider generatedInfographicProvider, PdfController pdfController) {
        super(securityService);
        this.droolsResultController = droolsResultController;
        this.generatedInfographicProvider = generatedInfographicProvider;
        this.pdfController = pdfController;
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
    public List<String> createFromDrools(@RequestHeader(name = CustomHeaders.TIMEZONE_HEADER, required = false) String timeZoneHeader,
                                         @RequestBody DroolsSubmittedForm droolsForm, Authentication authentication, HttpServletResponse response,
                                         HttpServletRequest request) {
        return droolsResultController.executeFromTemplates(droolsForm, authentication.getName(), timeZoneHeader);
    }


    @PreAuthorize("hasAnyAuthority(@securityService.viewerPrivilege, @securityService.editorPrivilege, @securityService.adminPrivilege)")
    @Operation(summary = "Generates a SVG from a drools input. The template must be on the system.", security = @SecurityRequirement(name = "bearerAuth"))
    @PostMapping(value = "/create/drools/plain", consumes = MediaType.TEXT_PLAIN_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public List<String> executeDroolsEngineFromText(@RequestHeader(name = CustomHeaders.TIMEZONE_HEADER, required = false) String timeZoneHeader,
                                                    @RequestBody final String droolsFormContent, Authentication authentication) {
        final DroolsSubmittedForm droolsSubmittedForm;
        try {
            droolsSubmittedForm = DroolsSubmittedForm.getFromJson(droolsFormContent);
        } catch (JsonProcessingException ex) {
            throw new BadRequestException(this.getClass(), "Input cannot be converted to drools result.");
        }
        return droolsResultController.executeFromTemplates(droolsSubmittedForm, authentication.getName(), timeZoneHeader);
    }


    @PreAuthorize("hasAnyAuthority(@securityService.viewerPrivilege, @securityService.editorPrivilege, @securityService.adminPrivilege)")
    @Operation(summary = "Generates one SVG from a drools input. If multiples templates are generated, only the selected one is returned."
            + " The template must be on the system.", security = @SecurityRequirement(name = "bearerAuth"))
    @PostMapping(value = "/create/drools/plain/page/{index}", consumes = MediaType.TEXT_PLAIN_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public String getFirst(@RequestHeader(name = CustomHeaders.TIMEZONE_HEADER, required = false) String timeZoneHeader,
                           @PathVariable("index") Integer index,
                           @RequestBody final String droolsFormContent,
                           Authentication authentication, HttpServletResponse response) {
        final DroolsSubmittedForm droolsSubmittedForm;
        try {
            droolsSubmittedForm = DroolsSubmittedForm.getFromJson(droolsFormContent);
        } catch (JsonProcessingException ex) {
            throw new BadRequestException(this.getClass(), "Input cannot be converted to drools result.");
        }
        final List<String> svg = droolsResultController.executeFromTemplates(droolsSubmittedForm, authentication.getName(), timeZoneHeader);
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


    @Operation(summary = "Search results as PDF generated by drools.", description = """
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
    @GetMapping(value = "/find/latest/pdf", produces = MediaType.APPLICATION_PDF_VALUE)
    public byte[] getLatestAsPdf(
            @Parameter(name = "form", required = false) @RequestParam(value = "form", required = false) String form,
            @Parameter(name = "version", required = false) @RequestParam(value = "version", required = false) Integer version,
            @Parameter(name = "createdBy", required = false) @RequestParam(value = "createdBy", required = false) String createdBy,
            @Parameter(name = "organization", required = false) @RequestParam(value = "organization", required = false) Long organization,
            Authentication authentication, HttpServletRequest request, HttpServletResponse response)
            throws InvalidXmlElementException, EmptyPdfBodyException {
        if (createdBy == null) {
            createdBy = authentication.getName();
        }
        canBeDoneForDifferentUsers(createdBy, authentication);

        final Optional<GeneratedInfographic> generatedInfographic = generatedInfographicProvider
                .findLatest(form, version, createdBy, organization);

        if (generatedInfographic.isEmpty()) {
            throw new NotFoundException(this.getClass(), "No infographic found!");
        }

        final ContentDisposition contentDisposition = ContentDisposition.builder("attachment")
                .filename((form != null ? form : "infographic") + ".pdf").build();
        response.setHeader(HttpHeaders.CONTENT_DISPOSITION, contentDisposition.toString());

        return pdfController.generatePdfFromSvgs(generatedInfographic.get().getSvgContents());

    }


    @Operation(summary = "Search results as PDF generated by drools.", description = """
            Received a list of infographics, and the system puts together as one PDF document.
            Only the last version from each infographic is returned.
            """,
            security = @SecurityRequirement(name = "bearerAuth"))
    @PreAuthorize("hasAnyAuthority(@securityService.viewerPrivilege, @securityService.editorPrivilege, @securityService.adminPrivilege)")
    @ResponseStatus(value = HttpStatus.OK)
    @PostMapping(value = "/find/latest/pdf", produces = MediaType.APPLICATION_PDF_VALUE)
    public byte[] getAsPdf(
            @RequestBody List<InfographicSearch> infographicSearches,
            Authentication authentication, HttpServletRequest request, HttpServletResponse response)
            throws InvalidXmlElementException, EmptyPdfBodyException {

        final ArrayList<String> svgCodes = new ArrayList<>();

        for (InfographicSearch infographicSearch : infographicSearches) {
            canBeDoneForDifferentUsers(infographicSearch.getCreatedBy(), authentication);
            final Optional<GeneratedInfographic> generatedInfographic = generatedInfographicProvider
                    .findLatest(infographicSearch.getForm(), infographicSearch.getVersion(),
                            infographicSearch.getCreatedBy(), infographicSearch.getOrganization());

            generatedInfographic.ifPresent(infographic -> svgCodes.addAll(infographic.getSvgContents()));
        }

        if (svgCodes.isEmpty()) {
            throw new NotFoundException(this.getClass(), "No infographics found!");
        }

        final ContentDisposition contentDisposition = ContentDisposition.builder("attachment")
                .filename("infographic.pdf").build();
        response.setHeader(HttpHeaders.CONTENT_DISPOSITION, contentDisposition.toString());

        return pdfController.generatePdfFromSvgs(svgCodes);
    }
}
