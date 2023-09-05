package com.biit.infographic.core.providers;

import com.biit.drools.form.DroolsSubmittedForm;
import com.biit.infographic.persistence.entities.GeneratedInfographic;
import com.biit.infographic.persistence.repositories.GeneratedInfographicRepository;
import com.biit.server.providers.CrudProvider;
import org.springframework.stereotype.Service;

import java.util.List;

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
}
