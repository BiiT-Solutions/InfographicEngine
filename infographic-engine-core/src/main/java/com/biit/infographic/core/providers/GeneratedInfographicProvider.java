package com.biit.infographic.core.providers;

import com.biit.drools.form.DroolsSubmittedForm;
import com.biit.infographic.persistence.entities.GeneratedInfographic;
import com.biit.infographic.persistence.repositories.GeneratedInfographicRepository;
import com.biit.server.providers.ElementProvider;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

@Service
public class GeneratedInfographicProvider extends ElementProvider<GeneratedInfographic, Long, GeneratedInfographicRepository> {

    public GeneratedInfographicProvider(GeneratedInfographicRepository repository) {
        super(repository);
    }

    public GeneratedInfographic createGeneratedInfographic(DroolsSubmittedForm droolsSubmittedForm, List<String> svgContents,
                                                           String formName, String createdBy, String organization) {
        final GeneratedInfographic generatedInfographic = new GeneratedInfographic();
        generatedInfographic.setSvgContents(svgContents);
        generatedInfographic.setDroolsSubmittedForm(droolsSubmittedForm.toJson());
        if (organization != null) {
            generatedInfographic.setOrganization(organization);
        } else {
            generatedInfographic.setOrganization(droolsSubmittedForm.getOrganization());
        }
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

    public List<GeneratedInfographic> findBy(String name, Integer version, String organization, String createdBy,
                                             LocalDateTime lowerTimeBoundary, LocalDateTime upperTimeBoundary) {
        return getRepository().findBy(name, version, organization, createdBy, lowerTimeBoundary, upperTimeBoundary);
    }

    public Optional<GeneratedInfographic> findLatest(String name, Integer version, String createdBy, String organization) {
        final List<GeneratedInfographic> results = getRepository().findBy(name, version, createdBy, organization);
        if (results.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(results.get(0));
    }

    public Map<String, GeneratedInfographic> findLatest(String name, Integer version, Set<String> creators) {
        final Map<String, GeneratedInfographic> infographics = new HashMap<>();
        creators.forEach(creator ->
                infographics.put(creator, findLatest(name, version, creator, null).orElse(null)));
        return infographics;
    }
}
