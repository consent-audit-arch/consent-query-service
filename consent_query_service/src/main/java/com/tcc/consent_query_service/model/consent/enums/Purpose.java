package com.tcc.consent_query_service.model.consent.enums;

import lombok.Getter;

import java.util.Arrays;

@Getter
public enum Purpose {
    PROMOTION("Promoção"),
    BILLING("Cobrança"),
    ANALYTICS("Análise"),
    CUSTOMER_SERVICE("Atendimento");

    private final String description;

    Purpose(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public static Purpose of(String value) {
        return Arrays.stream(values())
                .filter(v -> v.description.equalsIgnoreCase(value) || v.name().equalsIgnoreCase(value))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Invalid Purpose: " + value));
    }
}
