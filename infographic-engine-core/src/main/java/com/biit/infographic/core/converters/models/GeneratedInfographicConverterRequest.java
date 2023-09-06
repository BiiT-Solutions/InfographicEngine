package com.biit.infographic.core.converters.models;

import com.biit.infographic.persistence.entities.GeneratedInfographic;
import com.biit.server.converters.models.ConverterRequest;

public class GeneratedInfographicConverterRequest extends ConverterRequest<GeneratedInfographic> {
    public GeneratedInfographicConverterRequest(GeneratedInfographic myEntity) {
        super(myEntity);
    }
}

