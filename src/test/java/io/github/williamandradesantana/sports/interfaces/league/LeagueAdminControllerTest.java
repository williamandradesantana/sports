package io.github.williamandradesantana.sports.interfaces.league;

import io.github.williamandradesantana.sports.domain.user.PermissionName;
import io.github.williamandradesantana.sports.infrastructure.persistence.PostgresIntegrationTest;
import io.github.williamandradesantana.sports.infrastructure.security.jwt.JwtService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.options;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class LeagueAdminControllerTest extends PostgresIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JwtService jwtService;

    @Test
    @DisplayName("Test: syncing without a token should return 403")
    void test_SyncingWithoutToken_ShouldReturn403() throws Exception {
        mockMvc.perform(post("/api/v1/admin/leagues/sync").queryParam("externalId", "39"))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("Test: syncing with a non-admin token should return 403")
    void test_SyncingWithNonAdminToken_ShouldReturn403() throws Exception {
        String token = jwtService.generateToken("regular-user", Set.of(PermissionName.COMMON_USER));

        mockMvc.perform(post("/api/v1/admin/leagues/sync")
                .queryParam("externalId", "39")
                .header("Authorization", "Bearer " + token)
                .header("Origin", "http://localhost:3000"))
            .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("Test: preflight request from allowed origin should include CORS headers")
    void test_PreflightFromAllowedOrigin_ShouldIncludeCorsHeaders() throws Exception {
        mockMvc.perform(options("/api/v1/admin/leagues/sync")
                        .header("Origin", "http://localhost:3000")
                        .header("Access-Control-Request-Method", "POST"))
                .andExpect(status().isOk())
                .andExpect(header().string("Access-Control-Allow-Origin", "http://localhost:3000"));
    }

    @Test
    @DisplayName("Test: preflight request from a non-allowed origin should be rejected")
    void test_PreflightFromDisallowedOrigin_ShouldBeRejected() throws Exception {
        mockMvc.perform(options("/api/v1/admin/leagues/sync")
                        .header("Origin", "https://malicious-site.com")
                        .header("Access-Control-Request-Method", "POST"))
                .andExpect(status().isForbidden());
    }
}