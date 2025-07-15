package com.biit.infographic.core.controllers;


import com.biit.drools.form.DroolsSubmittedForm;
import com.biit.infographic.core.converters.GeneratedInfographicConverter;
import com.biit.infographic.core.converters.models.GeneratedInfographicConverterRequest;
import com.biit.infographic.core.exceptions.FormNotFoundException;
import com.biit.infographic.core.models.GeneratedInfographicDTO;
import com.biit.infographic.core.providers.GeneratedInfographicProvider;
import com.biit.infographic.persistence.entities.GeneratedInfographic;
import com.biit.infographic.persistence.repositories.GeneratedInfographicRepository;
import com.biit.logger.ExceptionType;
import com.biit.server.controller.ElementController;
import com.biit.server.security.IUserOrganizationProvider;
import com.biit.server.security.model.IUserOrganization;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Controller
public class GeneratedInfographicController extends ElementController<GeneratedInfographic, Long, GeneratedInfographicDTO,
        GeneratedInfographicRepository, GeneratedInfographicProvider, GeneratedInfographicConverterRequest, GeneratedInfographicConverter> {

    private final GeneratedInfographicProvider generatedInfographicProvider;

    @Autowired
    protected GeneratedInfographicController(GeneratedInfographicProvider provider, GeneratedInfographicConverter converter,
                                             GeneratedInfographicProvider generatedInfographicProvider,
                                             List<IUserOrganizationProvider<? extends IUserOrganization>> userOrganizationProvider) {
        super(provider, converter, userOrganizationProvider);
        this.generatedInfographicProvider = generatedInfographicProvider;
    }

    @Override
    protected GeneratedInfographicConverterRequest createConverterRequest(GeneratedInfographic generatedInfographic) {
        return new GeneratedInfographicConverterRequest(generatedInfographic);
    }


    public GeneratedInfographicDTO findLatest(String name, Integer version, String organization, String unit, String createdBy) {
        return convert(getProvider()
                .findLatest(name, version, createdBy, organization, unit)
                .orElseThrow(() -> new FormNotFoundException(this.getClass(),
                        "No infographic found with name '" + name + "', version '" + version + "', creator '"
                                + createdBy + "' and organization '" + organization + "'.", ExceptionType.DEBUG)));
    }


    public Map<String, GeneratedInfographicDTO> findLatest(String name, Integer version, Set<String> creators) {
        return getProvider().findLatest(name, version, creators).entrySet()
                .stream().collect(Collectors.toMap(Map.Entry::getKey, e -> convert(e.getValue())));
    }


    public List<GeneratedInfographicDTO> findBy(String name, Integer version, String organization, String unit, String createdBy,
                                                LocalDateTime lowerTimeBoundary, LocalDateTime upperTimeBoundary) {
        return convertAll(getProvider().findBy(name, version, organization, unit, createdBy, lowerTimeBoundary, upperTimeBoundary));
    }


    /**
     * Gets the drools answer, executes a template and generate an SVG.
     *
     * @param droolsSubmittedForm the answers obtained from base form drool engine.
     * @param createdBy           the owner of the form.
     */
    public GeneratedInfographicDTO process(DroolsSubmittedForm droolsSubmittedForm, String formName, String createdBy, String organization,
                                           String unit, String timeZone, Locale locale) {
        final Optional<GeneratedInfographic> generatedInfographic = generatedInfographicProvider.process(
                droolsSubmittedForm, formName, createdBy, organization, unit, timeZone, locale);
        return generatedInfographic.map(this::convert).orElse(null);
    }


    /**
     * Gets the latest drools form, and process it.
     */
    public GeneratedInfographicDTO processLatest(String name, Integer version, String organization, String unit, String submittedBy,
                                                 String timeZone, Locale locale) {
        final Optional<GeneratedInfographic> generatedInfographic = generatedInfographicProvider.processLatest(
                name, version, organization, unit, submittedBy, timeZone, locale);
        if (generatedInfographic.isEmpty()) {
            //For testing it is possible to have an infographic without a drools form. But for production this will always must return NOT FOUND.
            return findLatest(name, version, organization, unit, submittedBy);
        }
        return convert(generatedInfographic.get());
    }
}
