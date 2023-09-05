package com.biit.infographic.core.controllers;


import com.biit.drools.form.DroolsSubmittedForm;
import com.biit.infographic.core.controllers.kafka.EventSender;
import com.biit.infographic.core.converters.DroolsResultConverter;
import com.biit.infographic.core.converters.models.DroolsResultConverterRequest;
import com.biit.infographic.core.engine.InfographicTemplate;
import com.biit.infographic.core.engine.InfographicTemplateAndContent;
import com.biit.infographic.core.engine.Parameter;
import com.biit.infographic.core.engine.content.DroolsContent;
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
import com.biit.server.controller.BasicElementController;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Controller
public class DroolsResultController extends BasicElementController<DroolsResult, DroolsResultDTO, DroolsResultRepository,
        DroolsResultProvider, DroolsResultConverterRequest, DroolsResultConverter> {

    private final EventSender eventSender;
    private final GeneratedInfographicProvider generatedInfographicProvider;
    private final InfographicEngineController infographicEngineController;
    private final DroolsContent droolsContent;

    @Autowired
    protected DroolsResultController(DroolsResultProvider provider, DroolsResultConverter converter, EventSender eventSender,
                                     GeneratedInfographicProvider generatedInfographicProvider,
                                     InfographicEngineController infographicEngineController, DroolsContent droolsContent) {
        super(provider, converter);
        this.eventSender = eventSender;
        this.generatedInfographicProvider = generatedInfographicProvider;
        this.infographicEngineController = infographicEngineController;
        this.droolsContent = droolsContent;
    }

    @Override
    protected DroolsResultConverterRequest createConverterRequest(DroolsResult droolsResult) {
        return new DroolsResultConverterRequest(droolsResult);
    }

    /**
     * Gets the drools answer, executes a template and generate a SVG.
     *
     * @param droolsSubmittedForm the answers obtained from base form drool engine.
     * @param executedBy          the owner of the form.
     */
    public void process(DroolsSubmittedForm droolsSubmittedForm, String executedBy) {
        //Get the template for this form.
        final List<InfographicTemplate> templates = infographicEngineController.getTemplates(droolsSubmittedForm);

        //Replace template variables by drools values.
        final Map<InfographicFileElement, Set<Parameter>> values = infographicEngineController.getValues(droolsSubmittedForm,
                infographicEngineController.getParamsFromTemplates(templates));
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

        //Store SVG.
        final GeneratedInfographic generatedInfographic = generatedInfographicProvider.createGeneratedInfographic(droolsSubmittedForm, svgContents, executedBy);
        generatedInfographicProvider.save(generatedInfographic);

        //Send a new event.
        eventSender.sendResultEvents(generatedInfographic, executedBy);
    }

    public DroolsResultDTO findLatest(String name, Integer version, Long organizationId, String createdBy) {
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
