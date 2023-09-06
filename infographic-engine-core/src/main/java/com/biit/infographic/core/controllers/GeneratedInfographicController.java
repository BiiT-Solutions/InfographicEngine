package com.biit.infographic.core.controllers;


import com.biit.infographic.core.converters.GeneratedInfographicConverter;
import com.biit.infographic.core.converters.models.GeneratedInfographicConverterRequest;
import com.biit.infographic.core.exceptions.FormNotFoundException;
import com.biit.infographic.core.models.GeneratedInfographicDTO;
import com.biit.infographic.core.providers.GeneratedInfographicProvider;
import com.biit.infographic.persistence.entities.GeneratedInfographic;
import com.biit.infographic.persistence.repositories.GeneratedInfographicRepository;
import com.biit.server.controller.BasicElementController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.time.LocalDateTime;
import java.util.List;

@Controller
public class GeneratedInfographicController extends BasicElementController<GeneratedInfographic, GeneratedInfographicDTO, GeneratedInfographicRepository,
        GeneratedInfographicProvider, GeneratedInfographicConverterRequest, GeneratedInfographicConverter> {


    @Autowired
    protected GeneratedInfographicController(GeneratedInfographicProvider provider, GeneratedInfographicConverter converter) {
        super(provider, converter);
    }

    @Override
    protected GeneratedInfographicConverterRequest createConverterRequest(GeneratedInfographic generatedInfographic) {
        return new GeneratedInfographicConverterRequest(generatedInfographic);
    }


    public GeneratedInfographicDTO findLatest(String name, Integer version, Long organizationId, String createdBy) {
        return convert(getProvider()
                .findLatest(name, version, createdBy, organizationId)
                .orElseThrow(() -> new FormNotFoundException(this.getClass(),
                        "No infographic found with name '" + name + "', version '" + version + "', creator '"
                                + createdBy + "' and organization '" + organizationId + "'.")));
    }

    public List<GeneratedInfographicDTO> findBy(String name, Integer version, Long organizationId, String createdBy,
                                                LocalDateTime lowerTimeBoundary, LocalDateTime upperTimeBoundary) {
        return convertAll(getProvider().findBy(name, version, organizationId, createdBy, lowerTimeBoundary, upperTimeBoundary));
    }
}
