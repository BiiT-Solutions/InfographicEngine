package com.biit.infographic.core.controllers;


import com.biit.drools.form.DroolsSubmittedForm;
import com.biit.infographic.core.converters.GeneratedInfographicConverter;
import com.biit.infographic.core.converters.models.GeneratedInfographicConverterRequest;
import com.biit.infographic.core.engine.SvgFromDroolsConverter;
import com.biit.infographic.core.exceptions.FormNotFoundException;
import com.biit.infographic.core.models.GeneratedInfographicDTO;
import com.biit.infographic.core.providers.GeneratedInfographicProvider;
import com.biit.infographic.persistence.entities.GeneratedInfographic;
import com.biit.infographic.persistence.repositories.DroolsResultRepository;
import com.biit.infographic.persistence.repositories.GeneratedInfographicRepository;
import com.biit.logger.ExceptionType;
import com.biit.server.controller.ElementController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Controller
public class GeneratedInfographicController extends ElementController<GeneratedInfographic, Long, GeneratedInfographicDTO,
        GeneratedInfographicRepository, GeneratedInfographicProvider, GeneratedInfographicConverterRequest, GeneratedInfographicConverter> {

    private final SvgFromDroolsConverter svgFromDroolsConverter;
    private final GeneratedInfographicProvider generatedInfographicProvider;
    private final DroolsResultRepository droolsResultRepository;

    @Autowired
    protected GeneratedInfographicController(GeneratedInfographicProvider provider, GeneratedInfographicConverter converter,
                                             SvgFromDroolsConverter svgFromDroolsConverter,
                                             GeneratedInfographicProvider generatedInfographicProvider,
                                             DroolsResultRepository droolsResultRepository) {
        super(provider, converter);
        this.svgFromDroolsConverter = svgFromDroolsConverter;
        this.generatedInfographicProvider = generatedInfographicProvider;
        this.droolsResultRepository = droolsResultRepository;
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
    public GeneratedInfographic process(DroolsSubmittedForm droolsSubmittedForm, String formName, String createdBy, String organization,
                                        String unit, String timeZone) {
        //Generate SVG.
        final List<String> svgContents = svgFromDroolsConverter.executeFromTemplates(droolsSubmittedForm, createdBy, timeZone);

        //Store SVG.
        final GeneratedInfographic generatedInfographic = generatedInfographicProvider.createGeneratedInfographic(droolsSubmittedForm, svgContents,
                formName, createdBy, organization, unit);
        return generatedInfographicProvider.save(generatedInfographic);
    }


//    public Optional<GeneratedInfographic> fromDrools(String name, Integer version, String organization, String unit, String createdBy)
//            throws JsonProcessingException {
//        final List<DroolsResult> results = droolsResultRepository.findBy(name, version, createdBy, organization, unit);
//        if (results.isEmpty()) {
//            return Optional.empty();
//        }
//        final DroolsSubmittedForm droolsSubmittedForm = DroolsSubmittedForm.getFromJson(results.get(0).getForm());
//
//
//    }
}
