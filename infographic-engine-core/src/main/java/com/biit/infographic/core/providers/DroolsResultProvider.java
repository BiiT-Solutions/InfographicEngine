package com.biit.infographic.core.providers;


import com.biit.infographic.persistence.entities.DroolsResult;
import com.biit.infographic.persistence.repositories.DroolsResultRepository;
import com.biit.server.providers.ElementProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class DroolsResultProvider extends ElementProvider<DroolsResult, Long, DroolsResultRepository> {

    @Autowired
    public DroolsResultProvider(DroolsResultRepository repository) {
        super(repository);
    }


    public List<DroolsResult> findBy(String name, Integer version, String organization, String createdBy,
                                     LocalDateTime lowerTimeBoundary, LocalDateTime upperTimeBoundary) {
        return getRepository().findBy(name, version, organization, createdBy, lowerTimeBoundary, upperTimeBoundary);
    }

    public Optional<DroolsResult> findLatest(String name, Integer version, String createdBy, String organization) {
        final List<DroolsResult> results = getRepository().findBy(name, version, createdBy, organization);
        if (results.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(results.get(0));
    }

}
