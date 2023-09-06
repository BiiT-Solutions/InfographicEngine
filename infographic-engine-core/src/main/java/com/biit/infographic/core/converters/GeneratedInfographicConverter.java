package com.biit.infographic.core.converters;

import com.biit.infographic.core.converters.models.GeneratedInfographicConverterRequest;
import com.biit.infographic.core.models.GeneratedInfographicDTO;
import com.biit.infographic.persistence.entities.GeneratedInfographic;
import com.biit.server.controller.converters.ElementConverter;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

@Component
public class GeneratedInfographicConverter extends ElementConverter<GeneratedInfographic, GeneratedInfographicDTO,
        GeneratedInfographicConverterRequest> {


    @Override
    protected GeneratedInfographicDTO convertElement(GeneratedInfographicConverterRequest from) {
        final GeneratedInfographicDTO generatedInfographicDTO = new GeneratedInfographicDTO();
        BeanUtils.copyProperties(from.getEntity(), generatedInfographicDTO);
        return generatedInfographicDTO;
    }

    @Override
    public GeneratedInfographic reverse(GeneratedInfographicDTO to) {
        if (to == null) {
            return null;
        }
        final GeneratedInfographic generatedInfographic = new GeneratedInfographic();
        BeanUtils.copyProperties(to, generatedInfographic);
        return generatedInfographic;
    }
}
