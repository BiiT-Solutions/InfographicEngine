package com.biit.infographic.core.converters.models;

import com.biit.server.converters.models.ConverterRequest;
import com.biit.infographic.persistence.entities.MyEntity;

public class MyEntityConverterRequest extends ConverterRequest<MyEntity> {
    public MyEntityConverterRequest(MyEntity myEntity) {
        super(myEntity);
    }
}
