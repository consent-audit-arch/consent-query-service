package com.tcc.consent_query_service.model.consent.enums;

import lombok.Getter;

import java.util.Arrays;

@Getter
public enum ConsentStatus {
    GRANTED("Granted"),
    REVOKED("Revoked");

    private final String value;

    ConsentStatus(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static ConsentStatus of(String value) {
        return Arrays.stream(values())
                .filter(v -> v.value.equalsIgnoreCase(value) || v.name().equalsIgnoreCase(value))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Invalid ConsentStatus: " + value));
    }
}
