package com.biit.infographic.core.providers;

import com.biit.factmanager.client.FactClient;
import com.biit.factmanager.client.SearchParameters;
import com.biit.factmanager.dto.FactDTO;
import com.biit.kafka.events.EventSubject;

import java.time.LocalDateTime;
import java.util.HashMap;
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
        final Map<SearchParameters, Object> filter = new HashMap<>();
        filter.put(SearchParameters.ELEMENT_NAME, formName);
        filter.put(SearchParameters.FACT_TYPE, factType);
        filter.put(SearchParameters.CREATED_BY, createdBy);
        filter.put(SearchParameters.SUBJECT, subject.name());
        filter.put(SearchParameters.APPLICATION, application);

        final List<FactDTO> facts = factClient.get(filter, null);
        return null;
    }
}
