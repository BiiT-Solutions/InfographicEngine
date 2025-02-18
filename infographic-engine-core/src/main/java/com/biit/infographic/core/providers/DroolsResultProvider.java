package com.biit.infographic.core.providers;


import com.biit.infographic.persistence.entities.DroolsResult;
import com.biit.infographic.persistence.repositories.DroolsResultRepository;
import com.biit.server.providers.ElementProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static com.biit.database.encryption.KeyProperty.getEncryptionKey;

@Service
public class DroolsResultProvider extends ElementProvider<DroolsResult, Long, DroolsResultRepository> {

    @Autowired
    public DroolsResultProvider(DroolsResultRepository repository) {
        super(repository);
    }


    public List<DroolsResult> findBy(String name, Integer version, String organization, String unit, String createdBy,
                                     LocalDateTime lowerTimeBoundary, LocalDateTime upperTimeBoundary) {
        final List<DroolsResult> droolsResults;
        if (getEncryptionKey() != null && !getEncryptionKey().isBlank()) {
            droolsResults = getRepository().findByHash(name, version, organization, unit, createdBy, lowerTimeBoundary, upperTimeBoundary);
        } else {
            droolsResults = getRepository().findBy(name, version, organization, unit, createdBy, lowerTimeBoundary, upperTimeBoundary);
        }
        return droolsResults;
    }


    public Optional<DroolsResult> findLatest(String name, Integer version, String createdBy, String organization, String unit) {
        final List<DroolsResult> results;
        if (getEncryptionKey() != null && !getEncryptionKey().isBlank()) {
            results = getRepository().findByHash(name, version, createdBy, organization, unit);
        } else {
            results = getRepository().findBy(name, version, createdBy, organization, unit);
        }
        if (results.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(results.get(0));
    }

}
