package com.biit.infographic.core.models.svg.serialization;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

public final class ObjectMapperFactory {

    private static ObjectMapper objectMapper;

    private ObjectMapperFactory() {

    }

    public static ObjectMapper getObjectMapper() {
        if (objectMapper == null) {
            objectMapper = new ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT);
        }
        return objectMapper;
    }


}
