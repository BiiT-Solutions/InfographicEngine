package com.biit.infographic.core.controllers;


import com.biit.drools.form.DroolsSubmittedForm;
import com.biit.infographic.core.converters.DroolsResultConverter;
import com.biit.infographic.core.converters.models.DroolsResultConverterRequest;
import com.biit.infographic.core.exceptions.ElementDoesNotExistsException;
import com.biit.infographic.core.models.DroolsResultDTO;
import com.biit.infographic.core.providers.DroolsResultProvider;
import com.biit.infographic.persistence.entities.DroolsResult;
import com.biit.infographic.persistence.repositories.DroolsResultRepository;
import com.biit.server.controller.BasicElementController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.time.LocalDateTime;
import java.util.List;

@Controller
public class DroolsResultController extends BasicElementController<DroolsResult, DroolsResultDTO, DroolsResultRepository,
        DroolsResultProvider, DroolsResultConverterRequest, DroolsResultConverter> {

    @Autowired
    protected DroolsResultController(DroolsResultProvider provider, DroolsResultConverter converter) {
        super(provider, converter);
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

        //Replace template variables by drools values.

        //Generate SVG.
    }

    public DroolsResultDTO findLatest(String name, Integer version, Long organizationId, String createdBy) {
        return convert(getProvider()
                .findLatest(name, version, createdBy, organizationId)
                .orElseThrow(() -> new ElementDoesNotExistsException(this.getClass(),
                        "No drools result found with name '" + name + "', version '" + version + "', creator '"
                                + createdBy + "' and organization '" + organizationId + "'.")));
    }

    public List<DroolsResultDTO> findBy(String name, Integer version, Long organizationId, String createdBy,
                                        LocalDateTime lowerTimeBoundary, LocalDateTime upperTimeBoundary) {
        return convertAll(getProvider().findBy(name, version, organizationId, createdBy, lowerTimeBoundary, upperTimeBoundary));
    }
}
