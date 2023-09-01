package com.biit.infographic.core.providers;


import com.biit.infographic.persistence.entities.DroolsResult;
import com.biit.infographic.persistence.repositories.DroolsResultRepository;
import com.biit.server.providers.CrudProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class DroolsResultProvider extends CrudProvider<DroolsResult, Long, DroolsResultRepository> {

    @Autowired
    public DroolsResultProvider(DroolsResultRepository repository) {
        super(repository);
    }


    public List<DroolsResult> findBy(String name, Integer version, Long organizationId, String createdBy,
                                     LocalDateTime lowerTimeBoundary, LocalDateTime upperTimeBoundary) {
        return getRepository().findBy(name, version, organizationId, createdBy, lowerTimeBoundary, upperTimeBoundary);
    }

    public Optional<DroolsResult> findLatest(String name, Integer version, String createdBy, Long organizationId) {
        final List<DroolsResult> results = getRepository().findLatest(name, version, createdBy, organizationId,
                PageRequest.of(0, 1, Sort.Direction.DESC, "createdAt"));
        if (results.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(results.get(0));
    }

}
