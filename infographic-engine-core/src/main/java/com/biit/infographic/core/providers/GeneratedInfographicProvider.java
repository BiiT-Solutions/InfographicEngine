package com.biit.infographic.core.providers;

import com.biit.drools.form.DroolsSubmittedForm;
import com.biit.infographic.persistence.entities.GeneratedInfographic;
import com.biit.infographic.persistence.repositories.GeneratedInfographicRepository;
import com.biit.server.providers.CrudProvider;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class GeneratedInfographicProvider extends CrudProvider<GeneratedInfographic, Long, GeneratedInfographicRepository> {

    public GeneratedInfographicProvider(GeneratedInfographicRepository repository) {
        super(repository);
    }

    public GeneratedInfographic createGeneratedInfographic(DroolsSubmittedForm droolsSubmittedForm, List<String> svgContents, String executedBy) {
        final GeneratedInfographic generatedInfographic = new GeneratedInfographic();
        generatedInfographic.setSvgContents(svgContents);
        generatedInfographic.setOrganizationId(droolsSubmittedForm.getOrganizationId());
        generatedInfographic.setCreatedBy(executedBy);
        generatedInfographic.setFormName(droolsSubmittedForm.getName());
        generatedInfographic.setFormVersion(droolsSubmittedForm.getVersion());
        generatedInfographic.setOrganizationId(droolsSubmittedForm.getOrganizationId());
        return generatedInfographic;
    }

    public List<GeneratedInfographic> findBy(String name, Integer version, Long organizationId, String createdBy,
                                             LocalDateTime lowerTimeBoundary, LocalDateTime upperTimeBoundary) {
        return getRepository().findBy(name, version, organizationId, createdBy, lowerTimeBoundary, upperTimeBoundary);
    }

    public Optional<GeneratedInfographic> findLatest(String name, Integer version, String createdBy, Long organizationId) {
        final List<GeneratedInfographic> results = getRepository().findLatest(name, version, createdBy, organizationId,
                PageRequest.of(0, 1, Sort.Direction.DESC, "createdAt"));
        if (results.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(results.get(0));
    }
}
