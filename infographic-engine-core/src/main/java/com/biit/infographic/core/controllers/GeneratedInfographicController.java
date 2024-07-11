package com.biit.infographic.core.controllers;


import com.biit.infographic.core.converters.GeneratedInfographicConverter;
import com.biit.infographic.core.converters.models.GeneratedInfographicConverterRequest;
import com.biit.infographic.core.exceptions.FormNotFoundException;
import com.biit.infographic.core.models.GeneratedInfographicDTO;
import com.biit.infographic.core.providers.GeneratedInfographicProvider;
import com.biit.infographic.persistence.entities.GeneratedInfographic;
import com.biit.infographic.persistence.repositories.GeneratedInfographicRepository;
import com.biit.logger.ExceptionType;
import com.biit.server.controller.ElementController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Controller
public class GeneratedInfographicController extends ElementController<GeneratedInfographic, Long, GeneratedInfographicDTO,
        GeneratedInfographicRepository, GeneratedInfographicProvider, GeneratedInfographicConverterRequest, GeneratedInfographicConverter> {


    @Autowired
    protected GeneratedInfographicController(GeneratedInfographicProvider provider, GeneratedInfographicConverter converter) {
        super(provider, converter);
    }

    @Override
    protected GeneratedInfographicConverterRequest createConverterRequest(GeneratedInfographic generatedInfographic) {
        return new GeneratedInfographicConverterRequest(generatedInfographic);
    }


    public GeneratedInfographicDTO findLatest(String name, Integer version, String organization, String createdBy) {
        return convert(getProvider()
                .findLatest(name, version, createdBy, organization)
                .orElseThrow(() -> new FormNotFoundException(this.getClass(),
                        "No infographic found with name '" + name + "', version '" + version + "', creator '"
                                + createdBy + "' and organization '" + organization + "'.", ExceptionType.DEBUG)));
    }

    public Map<String, GeneratedInfographicDTO> findLatest(String name, Integer version, Set<String> creators) {
        return getProvider().findLatest(name, version, creators).entrySet()
                .stream().collect(Collectors.toMap(Map.Entry::getKey, e -> convert(e.getValue())));
    }

    public List<GeneratedInfographicDTO> findBy(String name, Integer version, String organization, String createdBy,
                                                LocalDateTime lowerTimeBoundary, LocalDateTime upperTimeBoundary) {
        return convertAll(getProvider().findBy(name, version, organization, createdBy, lowerTimeBoundary, upperTimeBoundary));
    }
}
