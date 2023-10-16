package com.biit.infographic.core.models.svg.serialization;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

public final class ObjectMapperFactory {

    private static ObjectMapper objectMapper;

    private ObjectMapperFactory() {

    }

    public static ObjectMapper getObjectMapper() {
        if (objectMapper == null) {
            objectMapper = new ObjectMapper().setSerializationInclusion(JsonInclude.Include.NON_NULL).enable(SerializationFeature.INDENT_OUTPUT);
        }
        return objectMapper;
    }


}
