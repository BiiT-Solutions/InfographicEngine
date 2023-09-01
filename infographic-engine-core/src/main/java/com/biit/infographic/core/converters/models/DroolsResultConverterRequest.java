package com.biit.infographic.core.converters.models;

import com.biit.infographic.persistence.entities.DroolsResult;
import com.biit.server.converters.models.ConverterRequest;

public class DroolsResultConverterRequest extends ConverterRequest<DroolsResult> {
    public DroolsResultConverterRequest(DroolsResult myEntity) {
        super(myEntity);
    }
}

