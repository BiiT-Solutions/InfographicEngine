package com.biit.infographic.core.providers;

import com.biit.drools.form.DroolsSubmittedForm;
import com.biit.infographic.persistence.entities.GeneratedInfographic;
import com.biit.infographic.persistence.repositories.GeneratedInfographicRepository;
import com.biit.server.providers.ElementProvider;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class GeneratedInfographicProvider extends ElementProvider<GeneratedInfographic, Long, GeneratedInfographicRepository> {

    public GeneratedInfographicProvider(GeneratedInfographicRepository repository) {
        super(repository);
    }

    public GeneratedInfographic createGeneratedInfographic(DroolsSubmittedForm droolsSubmittedForm, List<String> svgContents,
                                                           String formName, String executedBy) {
        final GeneratedInfographic generatedInfographic = new GeneratedInfographic();
        generatedInfographic.setSvgContents(svgContents);
        generatedInfographic.setDroolsSubmittedForm(droolsSubmittedForm.toJson());
        generatedInfographic.setOrganizationId(droolsSubmittedForm.getOrganizationId());
        generatedInfographic.setCreatedBy(executedBy);
        //As Drools now can execute multiples rules from one form, the rules form name is on the event tag.
        if (formName != null) {
            generatedInfographic.setFormName(formName);
        } else {
            generatedInfographic.setFormName(droolsSubmittedForm.getName());
        }
        generatedInfographic.setFormVersion(droolsSubmittedForm.getVersion() != null ? droolsSubmittedForm.getVersion() : 1);
        generatedInfographic.setOrganizationId(droolsSubmittedForm.getOrganizationId());
        return generatedInfographic;
    }

    public List<GeneratedInfographic> findBy(String name, Integer version, Long organizationId, String createdBy,
                                             LocalDateTime lowerTimeBoundary, LocalDateTime upperTimeBoundary) {
        return getRepository().findBy(name, version, organizationId, createdBy, lowerTimeBoundary, upperTimeBoundary);
    }

    public Optional<GeneratedInfographic> findLatest(String name, Integer version, String createdBy, Long organizationId) {
        final List<GeneratedInfographic> results = getRepository().findBy(name, version, createdBy, organizationId);
        if (results.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(results.get(0));
    }
}
