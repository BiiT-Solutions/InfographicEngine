package com.biit.infographic.core.providers;

import com.biit.drools.form.DroolsSubmittedForm;
import com.biit.infographic.core.engine.SvgFromDroolsConverter;
import com.biit.infographic.logger.InfographicEngineLogger;
import com.biit.infographic.persistence.entities.DroolsResult;
import com.biit.infographic.persistence.entities.GeneratedInfographic;
import com.biit.infographic.persistence.repositories.DroolsResultRepository;
import com.biit.infographic.persistence.repositories.GeneratedInfographicRepository;
import com.biit.server.providers.ElementProvider;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

@Service
public class GeneratedInfographicProvider extends ElementProvider<GeneratedInfographic, Long, GeneratedInfographicRepository> {

    private final SvgFromDroolsConverter svgFromDroolsConverter;
    private final DroolsResultRepository droolsResultRepository;

    public GeneratedInfographicProvider(GeneratedInfographicRepository repository, SvgFromDroolsConverter svgFromDroolsConverter,
                                        DroolsResultRepository droolsResultRepository) {
        super(repository);
        this.svgFromDroolsConverter = svgFromDroolsConverter;
        this.droolsResultRepository = droolsResultRepository;
    }

    public GeneratedInfographic createGeneratedInfographic(DroolsSubmittedForm droolsSubmittedForm, List<String> svgContents,
                                                           String formName, String createdBy, String organization, String unit) {
        if (droolsSubmittedForm == null || svgContents == null || svgContents.isEmpty()) {
            return null;
        }
        final GeneratedInfographic generatedInfographic = new GeneratedInfographic();
        generatedInfographic.setSvgContents(svgContents);
        generatedInfographic.setDroolsSubmittedForm(droolsSubmittedForm.toJson());
        if (organization != null) {
            generatedInfographic.setOrganization(organization);
        } else {
            generatedInfographic.setOrganization(droolsSubmittedForm.getOrganization());
        }
        generatedInfographic.setUnit(unit);
        generatedInfographic.setCreatedBy(createdBy);
        //As Drools now can execute multiples rules from one form, the rule form name is on the event tag.
        if (formName != null) {
            generatedInfographic.setFormName(formName);
        } else {
            generatedInfographic.setFormName(droolsSubmittedForm.getName());
        }
        generatedInfographic.setFormVersion(droolsSubmittedForm.getVersion() != null ? droolsSubmittedForm.getVersion() : 1);
        return generatedInfographic;
    }


    public List<GeneratedInfographic> findBy(String name, Integer version, String organization, String unit, String createdBy,
                                             LocalDateTime lowerTimeBoundary, LocalDateTime upperTimeBoundary) {
        return getRepository().findBy(name, version, organization, unit, createdBy, lowerTimeBoundary, upperTimeBoundary);
    }


    public Optional<GeneratedInfographic> findLatest(String name, Integer version, String createdBy, String organization, String unit) {
        final List<GeneratedInfographic> results = getRepository().findBy(name, version, createdBy, organization, unit);
        if (results.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(results.get(0));
    }


    public Map<String, GeneratedInfographic> findLatest(String name, Integer version, Set<String> creators) {
        final Map<String, GeneratedInfographic> infographics = new HashMap<>();
        creators.forEach(creator ->
                infographics.put(creator, findLatest(name, version, creator, null, null).orElse(null)));
        return infographics;
    }

    /**
     * Gets the drools answer, executes a template and generate an SVG.
     *
     * @param droolsSubmittedForm the answers obtained from base form drool engine.
     * @param createdBy           the owner of the form.
     */
    public Optional<GeneratedInfographic> process(DroolsSubmittedForm droolsSubmittedForm, String formName, String createdBy, String organization,
                                                  String unit, String timeZone, Locale locale) {
        //Generate SVG.
        final List<String> svgContents = svgFromDroolsConverter.executeFromTemplates(droolsSubmittedForm, createdBy, timeZone, locale);

        //Store SVG.
        final GeneratedInfographic generatedInfographic = createGeneratedInfographic(droolsSubmittedForm, svgContents,
                formName, createdBy, organization, unit);
        if (generatedInfographic == null) {
            return Optional.empty();
        }
        return Optional.of(save(generatedInfographic));
    }


    /**
     * Gets the latest drools form, and process it.
     */
    public Optional<GeneratedInfographic> processLatest(String formName, Integer formVersion, String organization, String unit, String submittedBy,
                                                        String timeZone, Locale locale) {
        final List<DroolsResult> results = droolsResultRepository.findBy(formName, formVersion, submittedBy, organization, unit);
        if (results.isEmpty()) {
            return Optional.empty();
        }
        try {
            final DroolsSubmittedForm droolsSubmittedForm = DroolsSubmittedForm.getFromJson(results.get(0).getForm());
            return process(droolsSubmittedForm, formName, droolsSubmittedForm.getSubmittedBy(), organization, unit, timeZone, locale);
        } catch (JsonProcessingException e) {
            InfographicEngineLogger.errorMessage(this.getClass(), e);
            return findLatest(formName, formVersion, submittedBy, organization, unit);
        }
    }
}
