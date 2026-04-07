package com.tcc.consent_query_service.model.consent.enums;

import lombok.Getter;

import java.util.Arrays;

@Getter
public enum DataCategory {
    PERSONAL_DATA("Dados Pessoais"),
    CONTRACT_DATA("Dados Contratuais"),
    FINANCIAL_DATA("Dados Financeiros");

    private final String description;

    DataCategory(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public static DataCategory of(String value) {
        return Arrays.stream(values())
                .filter(v -> v.description.equalsIgnoreCase(value) || v.name().equalsIgnoreCase(value))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Invalid DataCategory: " + value));
    }
}
