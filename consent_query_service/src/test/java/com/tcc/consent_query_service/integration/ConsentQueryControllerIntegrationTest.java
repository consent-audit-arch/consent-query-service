package com.tcc.consent_query_service.integration;

import com.tcc.consent_query_service.infrastructure.persistence.entities.ConsentProjectionJpaEntity;
import com.tcc.consent_query_service.infrastructure.persistence.repositories.JPARepository.ConsentProjectionJpaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class ConsentQueryControllerIntegrationTest extends AbstractIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ConsentProjectionJpaRepository projectionRepository;

    @BeforeEach
    void setUp() {
        projectionRepository.deleteAll();
    }

    @Test
    void getConsentsByUserId_shouldReturnAllConsents() throws Exception {
        Long userId = 100L;
        persistConsent(userId, "PERSONAL_DATA", "PROMOTION", "GRANTED");
        persistConsent(userId, "CONTRACT_DATA", "BILLING", "REVOKED");

        mockMvc.perform(get("/api/v1/consent/{userId}", userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId", is(100)))
                .andExpect(jsonPath("$.authorizations", hasSize(2)))
                .andExpect(jsonPath("$.authorizations[0].dataCategory", is("PERSONAL_DATA")))
                .andExpect(jsonPath("$.authorizations[0].status", is("GRANTED")))
                .andExpect(jsonPath("$.authorizations[1].dataCategory", is("CONTRACT_DATA")))
                .andExpect(jsonPath("$.authorizations[1].status", is("REVOKED")));
    }

    @Test
    void getConsentsByUserId_shouldReturnEmptyListWhenNoConsents() throws Exception {
        Long userId = 999L;

        mockMvc.perform(get("/api/v1/consent/{userId}", userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId", is(999)))
                .andExpect(jsonPath("$.authorizations", hasSize(0)));
    }

    @Test
    void getConsentAuthorization_shouldReturnAuthorizationWhenFound() throws Exception {
        Long userId = 100L;
        persistConsent(userId, "PERSONAL_DATA", "PROMOTION", "GRANTED");

        mockMvc.perform(get("/api/v1/consent/{userId}/authorizations", userId)
                        .param("dataCategory", "PERSONAL_DATA")
                        .param("purpose", "PROMOTION"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId", is(100)))
                .andExpect(jsonPath("$.dataCategory", is("PERSONAL_DATA")))
                .andExpect(jsonPath("$.purpose", is("PROMOTION")))
                .andExpect(jsonPath("$.status", is("GRANTED")))
                .andExpect(jsonPath("$.legalBasis", is("CONSENT")));
    }

    @Test
    void getConsentAuthorization_shouldReturn404WhenNotFound() throws Exception {
        Long userId = 999L;

        mockMvc.perform(get("/api/v1/consent/{userId}/authorizations", userId)
                        .param("dataCategory", "PERSONAL_DATA")
                        .param("purpose", "PROMOTION"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error", is("Not Found")))
                .andExpect(jsonPath("$.message", containsString("Consent authorization not found")));
    }

    @Test
    void getConsentAuthorization_shouldReturn400ForInvalidDataCategory() throws Exception {
        Long userId = 100L;

        mockMvc.perform(get("/api/v1/consent/{userId}/authorizations", userId)
                        .param("dataCategory", "INVALID_CATEGORY")
                        .param("purpose", "PROMOTION"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error", is("Bad Request")));
    }

    private void persistConsent(Long userId, String dataCategory, String finality, String status) {
        ConsentProjectionJpaEntity entity = new ConsentProjectionJpaEntity();
        try {
            var fields = ConsentProjectionJpaEntity.class.getDeclaredFields();
            for (var field : fields) {
                field.setAccessible(true);
                switch (field.getName()) {
                    case "id" -> field.set(entity, null);
                    case "userId" -> field.set(entity, userId);
                    case "dataCategory" -> field.set(entity, dataCategory);
                    case "finality" -> field.set(entity, finality);
                    case "status" -> field.set(entity, status);
                    case "legalBasis" -> field.set(entity, "CONSENT");
                    case "grantedAt" -> field.set(entity, LocalDateTime.now());
                    case "revokedAt" -> field.set(entity, status.equals("REVOKED") ? LocalDateTime.now() : null);
                    case "lastEventId" -> field.set(entity, 1L);
                    case "version" -> field.set(entity, 1L);
                    case "updatedAt" -> field.set(entity, LocalDateTime.now());
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        projectionRepository.save(entity);
    }
}
