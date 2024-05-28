package com.biit.infographic.core.controllers;


import com.biit.drools.form.DroolsSubmittedForm;
import com.biit.infographic.core.controllers.kafka.DroolsEventSender;
import com.biit.infographic.core.converters.DroolsResultConverter;
import com.biit.infographic.core.converters.models.DroolsResultConverterRequest;
import com.biit.infographic.core.engine.InfographicTemplate;
import com.biit.infographic.core.engine.InfographicTemplateAndContent;
import com.biit.infographic.core.engine.Parameter;
import com.biit.infographic.core.engine.files.InfographicFileElement;
import com.biit.infographic.core.exceptions.FormNotFoundException;
import com.biit.infographic.core.generators.SvgGenerator;
import com.biit.infographic.core.models.DroolsResultDTO;
import com.biit.infographic.core.models.svg.SvgTemplate;
import com.biit.infographic.core.models.svg.serialization.ObjectMapperFactory;
import com.biit.infographic.core.providers.DroolsResultProvider;
import com.biit.infographic.core.providers.GeneratedInfographicProvider;
import com.biit.infographic.logger.InfographicEngineLogger;
import com.biit.infographic.persistence.entities.DroolsResult;
import com.biit.infographic.persistence.entities.GeneratedInfographic;
import com.biit.infographic.persistence.repositories.DroolsResultRepository;
import com.biit.server.controller.ElementController;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Controller
public class DroolsResultController extends ElementController<DroolsResult, Long, DroolsResultDTO, DroolsResultRepository,
        DroolsResultProvider, DroolsResultConverterRequest, DroolsResultConverter> {

    private final DroolsEventSender droolsEventSender;
    private final GeneratedInfographicProvider generatedInfographicProvider;
    private final InfographicEngineController infographicEngineController;
    private final ObjectMapper objectMapper;

    @Autowired
    protected DroolsResultController(DroolsResultProvider provider, DroolsResultConverter converter, DroolsEventSender droolsEventSender,
                                     GeneratedInfographicProvider generatedInfographicProvider,
                                     InfographicEngineController infographicEngineController, ObjectMapper objectMapper) {
        super(provider, converter);
        this.droolsEventSender = droolsEventSender;
        this.generatedInfographicProvider = generatedInfographicProvider;
        this.infographicEngineController = infographicEngineController;
        this.objectMapper = objectMapper;
    }

    @Override
    protected DroolsResultConverterRequest createConverterRequest(DroolsResult droolsResult) {
        return new DroolsResultConverterRequest(droolsResult);
    }

    /**
     * Gets the drools answer, executes a template and generate a SVG.
     *
     * @param droolsSubmittedForm the answers obtained from base form drool engine.
     * @param createdBy           the owner of the form.
     */
    public void process(DroolsSubmittedForm droolsSubmittedForm, String formName, String createdBy, String timeZone) {
        //Generate SVG.
        final List<String> svgContents = executeFromTemplates(droolsSubmittedForm, createdBy, timeZone);

        //Store SVG.
        final GeneratedInfographic generatedInfographic = generatedInfographicProvider.createGeneratedInfographic(droolsSubmittedForm, svgContents,
                formName, createdBy);
        generatedInfographicProvider.save(generatedInfographic);

        //Send a new event.
        droolsEventSender.sendResultEvents(generatedInfographic, createdBy);
    }

    public List<String> executeFromTemplates(DroolsSubmittedForm droolsSubmittedForm, String createdBy, String timeZone) {
        //Get the template for this form.
        final List<InfographicTemplate> templates = infographicEngineController.getTemplates(droolsSubmittedForm);
        return executeFromTemplates(droolsSubmittedForm, templates, timeZone);
    }

    public List<String> executeFromTemplates(DroolsSubmittedForm droolsSubmittedForm, List<InfographicTemplate> templates, String timeZone) {
        //Replace template variables by drools values.
        final Map<InfographicFileElement, Set<Parameter>> values = infographicEngineController.getValues(droolsSubmittedForm,
                infographicEngineController.getParamsFromTemplates(templates), timeZone);
        final List<InfographicTemplateAndContent> templateAndContents = infographicEngineController.addContentToTemplates(templates, values);

        //Generate SVG.
        final List<String> svgContents = new ArrayList<>();
        for (InfographicTemplateAndContent infographicTemplateAndContent : templateAndContents) {
            try {
                svgContents.add(SvgGenerator.generate(ObjectMapperFactory.getObjectMapper().readValue(
                        infographicTemplateAndContent.getProcessedTemplate(), SvgTemplate.class)));
            } catch (JsonProcessingException e) {
                InfographicEngineLogger.errorMessage(this.getClass(), e);
                throw new RuntimeException(e);
            }
        }
        return svgContents;
    }

    public List<String> execute(DroolsSubmittedForm droolsSubmittedForm, List<SvgTemplate> svgTemplates) {
        return execute(droolsSubmittedForm, svgTemplates, null);
    }

    public List<String> execute(DroolsSubmittedForm droolsSubmittedForm, List<SvgTemplate> svgTemplates, String timeZone) {
        //Replace template variables by drools values.
        return executeFromTemplates(droolsSubmittedForm, svgTemplates.stream().map(svgTemplate -> {
            try {
                return new InfographicTemplate(null,
                        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL).writeValueAsString(svgTemplate));
            } catch (JsonProcessingException e) {
                InfographicEngineLogger.errorMessage(this.getClass(), e);
                throw new RuntimeException(e);
            }
        }).toList(), timeZone);
    }

    public DroolsResultDTO findLatest(String name, Integer version, Long organizationId, String createdBy, String timeZone) {
        return convert(getProvider()
                .findLatest(name, version, createdBy, organizationId)
                .orElseThrow(() -> new FormNotFoundException(this.getClass(),
                        "No drools result found with name '" + name + "', version '" + version + "', creator '"
                                + createdBy + "' and organization '" + organizationId + "'.")));
    }

    public List<DroolsResultDTO> findBy(String name, Integer version, Long organizationId, String createdBy,
                                        LocalDateTime lowerTimeBoundary, LocalDateTime upperTimeBoundary) {
        return convertAll(getProvider().findBy(name, version, organizationId, createdBy, lowerTimeBoundary, upperTimeBoundary));
    }
}
