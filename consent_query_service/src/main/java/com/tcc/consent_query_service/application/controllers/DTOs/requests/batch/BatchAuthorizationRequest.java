package com.tcc.consent_query_service.application.controllers.DTOs.requests.batch;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BatchAuthorizationRequest {
    @NotEmpty(message = "titularIds must not be empty")
    private List<Long> titularIds;

    @NotBlank(message = "dataCategory is required")
    private String dataCategory;

    @NotBlank(message = "purpose is required")
    private String purpose;
}
