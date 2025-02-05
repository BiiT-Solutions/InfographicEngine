package com.biit.infographic.core.controllers;


import com.biit.infographic.core.converters.DroolsResultConverter;
import com.biit.infographic.core.converters.models.DroolsResultConverterRequest;
import com.biit.infographic.core.exceptions.FormNotFoundException;
import com.biit.infographic.core.models.DroolsResultDTO;
import com.biit.infographic.core.providers.DroolsResultProvider;
import com.biit.infographic.persistence.entities.DroolsResult;
import com.biit.infographic.persistence.repositories.DroolsResultRepository;
import com.biit.server.controller.ElementController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.time.LocalDateTime;
import java.util.List;

@Controller
public class DroolsResultController extends ElementController<DroolsResult, Long, DroolsResultDTO, DroolsResultRepository,
        DroolsResultProvider, DroolsResultConverterRequest, DroolsResultConverter> {

    @Autowired
    protected DroolsResultController(DroolsResultProvider provider, DroolsResultConverter converter) {
        super(provider, converter);
    }

    @Override
    protected DroolsResultConverterRequest createConverterRequest(DroolsResult droolsResult) {
        return new DroolsResultConverterRequest(droolsResult);
    }


    public DroolsResultDTO findLatest(String name, Integer version, String organization, String unit, String createdBy) {
        return convert(getProvider()
                .findLatest(name, version, createdBy, organization, unit)
                .orElseThrow(() -> new FormNotFoundException(this.getClass(),
                        "No drools result found with name '" + name + "', version '" + version + "', creator '"
                                + createdBy + "' and organization '" + organization + "'.")));
    }


    public List<DroolsResultDTO> findBy(String name, Integer version, String organization, String unit, String createdBy,
                                        LocalDateTime lowerTimeBoundary, LocalDateTime upperTimeBoundary) {
        return convertAll(getProvider().findBy(name, version, organization, unit, createdBy, lowerTimeBoundary, upperTimeBoundary));
    }
}
