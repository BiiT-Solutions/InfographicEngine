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

import com.biit.factmanager.client.FactClient;
import com.biit.factmanager.client.SearchParameters;
import com.biit.factmanager.dto.FactDTO;
import com.biit.kafka.events.EventSubject;

import java.time.LocalDateTime;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

//@Service
public class FactManagerVariableProvider {

    private final FactClient factClient;

    public FactManagerVariableProvider(FactClient factClient) {
        this.factClient = factClient;
    }


    public String getVariableValue(String formName, String variableName, String factType, EventSubject subject, String createdBy,
                                   String application, LocalDateTime submittedAt) {
        final Map<SearchParameters, Object> filter = new EnumMap<>(SearchParameters.class);
        filter.put(SearchParameters.ELEMENT_NAME, formName);
        filter.put(SearchParameters.FACT_TYPE, factType);
        filter.put(SearchParameters.CREATED_BY, createdBy);
        filter.put(SearchParameters.SUBJECT, subject.name());
        filter.put(SearchParameters.APPLICATION, application);

        final List<FactDTO> facts = factClient.get(filter, null);
        return null;
    }
}
