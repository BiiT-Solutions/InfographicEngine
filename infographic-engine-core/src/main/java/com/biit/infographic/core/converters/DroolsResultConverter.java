package com.biit.infographic.core.converters;

import com.biit.infographic.core.converters.models.DroolsResultConverterRequest;
import com.biit.infographic.core.models.DroolsResultDTO;
import com.biit.infographic.persistence.entities.DroolsResult;
import com.biit.server.controller.converters.ElementConverter;
import org.springframework.beans.BeanUtils;

public class DroolsResultConverter extends ElementConverter<DroolsResult, DroolsResultDTO, DroolsResultConverterRequest> {


    @Override
    protected DroolsResultDTO convertElement(DroolsResultConverterRequest from) {
        final DroolsResultDTO droolsResultDTO = new DroolsResultDTO();
        BeanUtils.copyProperties(from.getEntity(), droolsResultDTO);
        return droolsResultDTO;
    }

    @Override
    public DroolsResult reverse(DroolsResultDTO to) {
        if (to == null) {
            return null;
        }
        final DroolsResult droolsResult = new DroolsResult();
        BeanUtils.copyProperties(to, droolsResult);
        return droolsResult;
    }
}