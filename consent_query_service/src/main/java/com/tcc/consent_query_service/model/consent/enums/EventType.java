package com.tcc.consent_query_service.model.consent.enums;

import lombok.Getter;

@Getter
public enum EventType {
    CONSENT_GRANTED("ConsentGranted"),
    CONSENT_REVOKED("ConsentRevoked");

    private final String value;

    EventType(String value) {
        this.value = value;
    }

    public static EventType of(String value) {
        return switch (value) {
            case "ConsentGranted" -> CONSENT_GRANTED;
            case "ConsentRevoked" -> CONSENT_REVOKED;
            default -> throw new IllegalArgumentException("Invalid EventType: " + value);
        };
    }
}