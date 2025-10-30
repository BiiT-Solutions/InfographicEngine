package com.biit.infographic.core.providers;

/*-
 * #%L
 * Infographic Engine v2 (Core)
 * %%
 * Copyright (C) 2022 - 2025 BiiT Sourcing Solutions S.L.
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * #L%
 */

import com.biit.drools.form.DroolsSubmittedForm;
import com.biit.infographic.core.engine.SvgFromDroolsConverter;
import com.biit.infographic.core.exceptions.MalformedTemplateException;
import com.biit.infographic.logger.InfographicEngineLogger;
import com.biit.infographic.persistence.entities.DroolsResult;
import com.biit.infographic.persistence.entities.GeneratedInfographic;
import com.biit.infographic.persistence.repositories.DroolsResultRepository;
import com.biit.infographic.persistence.repositories.GeneratedInfographicRepository;
import com.biit.server.providers.ElementProvider;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import static com.biit.database.encryption.KeyProperty.getEncryptionKey;

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
        final List<GeneratedInfographic> results;
        if (getEncryptionKey() != null && !getEncryptionKey().isBlank()) {
            results = getRepository().findByHash(name, version, organization, unit, createdBy, lowerTimeBoundary, upperTimeBoundary);
        } else {
            results = getRepository().findBy(name, version, organization, unit, createdBy, lowerTimeBoundary, upperTimeBoundary);
        }
        return results;
    }


    public Optional<GeneratedInfographic> findLatest(String formName, Integer formVersion, String createdBy, String organization, String unit) {
        final List<GeneratedInfographic> results;
        if (getEncryptionKey() != null && !getEncryptionKey().isBlank()) {
            results = getRepository().findByHash(formName, formVersion, createdBy, organization, unit);
        } else {
            results = getRepository().findBy(formName, formVersion, createdBy, organization, unit);
        }
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

        final GeneratedInfographic generatedInfographic = createGeneratedInfographic(droolsSubmittedForm, svgContents,
                formName, createdBy, organization, unit);
        if (generatedInfographic == null) {
            return Optional.empty();
        }
        return Optional.of(generatedInfographic);
    }


    /**
     * Gets the latest drools form, and process it.
     */
    public Optional<GeneratedInfographic> processLatest(String formName, Integer formVersion, String organization, String unit, String submittedBy,
                                                        String timeZone, Locale locale) {
        final List<DroolsResult> results;
        if (getEncryptionKey() != null && !getEncryptionKey().isBlank()) {
            results = droolsResultRepository.findByHash(formName, formVersion, submittedBy, organization, unit);
        } else {
            results = droolsResultRepository.findBy(formName, formVersion, submittedBy, organization, unit);
        }
        if (results.isEmpty()) {
            return Optional.empty();
        }
        //Get the latest with higher version!
        Collections.sort(results, Comparator.comparing(DroolsResult::getFormVersion, Comparator.nullsLast(Comparator.naturalOrder()))
                .thenComparing(DroolsResult::getCreatedAt, Comparator.nullsLast(Comparator.naturalOrder())).reversed());
        try {
            final DroolsSubmittedForm droolsSubmittedForm = DroolsSubmittedForm.getFromJson(results.get(0).getForm());
            return process(droolsSubmittedForm, formName, droolsSubmittedForm.getSubmittedBy(), organization, unit, timeZone, locale);
        } catch (JsonProcessingException | MalformedTemplateException | NullPointerException e) {
            InfographicEngineLogger.errorMessage(this.getClass(), e);
            return findLatest(formName, formVersion, submittedBy, organization, unit);
        }
    }
}
