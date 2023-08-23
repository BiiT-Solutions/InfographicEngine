package com.biit.infographic.core.controllers;


import com.biit.infographic.core.converters.MyEntityConverter;
import com.biit.infographic.core.converters.models.MyEntityConverterRequest;
import com.biit.infographic.core.exceptions.MyEntityNotFoundException;
import com.biit.infographic.core.providers.MyEntityProvider;
import com.biit.infographic.core.models.MyEntityDTO;
import com.biit.infographic.persistence.entities.MyEntity;
import com.biit.infographic.persistence.repositories.MyEntityRepository;
import com.biit.server.controller.BasicElementController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
public class MyEntityController extends BasicElementController<MyEntity, MyEntityDTO, MyEntityRepository,
        MyEntityProvider, MyEntityConverterRequest, MyEntityConverter> {

    @Autowired
    protected MyEntityController(MyEntityProvider provider, MyEntityConverter converter) {
        super(provider, converter);
    }

    @Override
    protected MyEntityConverterRequest createConverterRequest(MyEntity myEntity) {
        return new MyEntityConverterRequest(myEntity);
    }

    public MyEntityDTO getByName(String name) {
        return getConverter().convert(new MyEntityConverterRequest(getProvider().findByName(name).orElseThrow(() ->
                new MyEntityNotFoundException(this.getClass(),
                        "No MyEntity with name '" + name + "' found on the system."))));
    }
}
