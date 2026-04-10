package com.tcc.consent_query_service.model.consent.entities;

import com.tcc.consent_query_service.model.consent.valueObjects.ConsentHistoryEntry;

import java.util.Collections;
import java.util.List;

public record ConsentHistory(Long userId, List<ConsentHistoryEntry> events) {

    public static ConsentHistory empty(Long userId) {
        return new ConsentHistory(userId, Collections.emptyList());
    }

    public boolean hasEvents() {
        return events != null && !events.isEmpty();
    }
}