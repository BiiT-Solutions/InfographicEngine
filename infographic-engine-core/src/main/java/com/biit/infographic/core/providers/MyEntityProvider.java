package com.biit.infographic.core.providers;

import com.biit.server.providers.CrudProvider;
import com.biit.infographic.persistence.entities.MyEntity;
import com.biit.infographic.persistence.repositories.MyEntityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class MyEntityProvider extends CrudProvider<MyEntity, Long, MyEntityRepository> {

    @Autowired
    public MyEntityProvider(MyEntityRepository repository) {
        super(repository);
    }

    public Optional<MyEntity> findByName(String name) {
        return getRepository().findByName(name);
    }
}
