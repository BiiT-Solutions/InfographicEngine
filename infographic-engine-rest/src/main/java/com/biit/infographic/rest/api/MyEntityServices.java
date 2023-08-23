package com.biit.infographic.rest.api;

import com.biit.server.rest.BasicServices;
import com.biit.infographic.core.controllers.MyEntityController;
import com.biit.infographic.core.converters.MyEntityConverter;
import com.biit.infographic.core.converters.models.MyEntityConverterRequest;
import com.biit.infographic.core.providers.MyEntityProvider;
import com.biit.infographic.core.models.MyEntityDTO;
import com.biit.infographic.persistence.entities.MyEntity;
import com.biit.infographic.persistence.repositories.MyEntityRepository;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/entities")
public class MyEntityServices extends BasicServices<MyEntity, MyEntityDTO, MyEntityRepository,
        MyEntityProvider, MyEntityConverterRequest, MyEntityConverter, MyEntityController> {

    public MyEntityServices(MyEntityController controller) {
        super(controller);
    }
}
