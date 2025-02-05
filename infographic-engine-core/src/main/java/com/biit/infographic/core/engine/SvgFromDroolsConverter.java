package com.biit.infographic.core.engine;

import com.biit.drools.form.DroolsSubmittedForm;
import com.biit.infographic.core.engine.files.InfographicFileElement;
import com.biit.infographic.core.generators.SvgGenerator;
import com.biit.infographic.core.models.svg.SvgTemplate;
import com.biit.infographic.core.models.svg.serialization.ObjectMapperFactory;
import com.biit.infographic.logger.InfographicEngineLogger;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
public class SvgFromDroolsConverter {

    private final InfographicEngine infographicEngine;
    private final ObjectMapper objectMapper;

    public SvgFromDroolsConverter(InfographicEngine infographicEngine, ObjectMapper objectMapper) {
        this.infographicEngine = infographicEngine;
        this.objectMapper = objectMapper;
    }


    public List<String> executeFromTemplates(DroolsSubmittedForm droolsSubmittedForm, String createdBy, String timeZone) {
        //Get the template for this form.
        final List<InfographicTemplate> templates = infographicEngine.getTemplates(droolsSubmittedForm);
        return executeFromTemplates(droolsSubmittedForm, templates, timeZone);
    }


    public List<String> executeFromTemplates(DroolsSubmittedForm droolsSubmittedForm, List<InfographicTemplate> templates, String timeZone) {
        //Replace template variables by drools values.
        final Map<InfographicFileElement, Set<Parameter>> values = infographicEngine.getValues(droolsSubmittedForm,
                infographicEngine.getParamsFromTemplates(templates), timeZone);
        final List<InfographicTemplateAndContent> templateAndContents = infographicEngine.addContentToTemplates(templates, values);

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
}
