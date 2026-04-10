package com.tcc.consent_query_service.model.consent.valueObjects;

import com.fasterxml.jackson.databind.ObjectMapper;

public record IssuedBy(String id, String issuer) {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    public static IssuedBy fromJson(String json) {
        if (json == null || json.isBlank() || json.equals("{}") || json.equals("null")) {
            return null;
        }
        String trimmed = json.trim();
        if (trimmed.startsWith("{") && trimmed.endsWith("}")) {
            try {
                return OBJECT_MAPPER.readValue(json, IssuedBy.class);
            } catch (Exception e) {
                return null;
            }
        }
        return null;
    }

    public String type() {
        return issuer;
    }
}