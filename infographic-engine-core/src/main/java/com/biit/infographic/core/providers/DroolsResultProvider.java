package com.biit.infographic.core.providers;

/*-
 * #%L
 * Infographic Engine v2 (Core)
 * %%
 * Copyright (C) 2022 - 2025 BiiT Sourcing Solutions S.L.
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * #L%
 */


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
