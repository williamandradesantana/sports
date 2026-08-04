package io.github.williamandradesantana.sports.interfaces.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.williamandradesantana.sports.infrastructure.persistence.PostgresIntegrationTest;
import io.github.williamandradesantana.sports.interfaces.auth.dto.LoginRequest;
import io.github.williamandradesantana.sports.interfaces.auth.dto.RegisterRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class AuthControllerTest extends PostgresIntegrationTest {

    @Autowired
    private MockMvc mockMvc;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    @DisplayName("Test: registering a new user should return 201 without exposing the password")
    void test_RegisteringNewUser_ShouldReturn201() throws Exception {
        RegisterRequest registerRequest = new RegisterRequest("wbs", "William Santana", "password");

        mockMvc.perform(post("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(registerRequest)))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.username").value(registerRequest.username()))
            .andExpect(jsonPath("$.fullName").value(registerRequest.fullName()))
            .andExpect(jsonPath("$.password").doesNotExist());
    }

    @Test
    @DisplayName("Test: registering a duplicate username should return 409")
    void test_RegisteringDuplicateUsername_ShouldReturn409() throws Exception {
        RegisterRequest registerRequest = new RegisterRequest("wbs", "William Santana", "password");
        String body = objectMapper.writeValueAsString(registerRequest);

        mockMvc.perform(
            post("/api/auth/register").contentType(MediaType.APPLICATION_JSON_VALUE).content(body))
            .andExpect(status().isCreated());
        mockMvc.perform(
            post("/api/auth/register").contentType(MediaType.APPLICATION_JSON_VALUE).content(body))
            .andExpect(status().isConflict());
    }

    @Test
    @DisplayName("Test: logging in with valid credentials should return a token")
    void test_LoginWithValidCredentials_ShouldReturnToken() throws Exception {
        registerUser("logintest", "password");
        LoginRequest loginRequest = new LoginRequest("logintest", "password");

        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(loginRequest)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.token").isNotEmpty());
    }

    @Test
    @DisplayName("Test: logging in with wrong password should return 401 with a generic message")
    void test_LoginWithWrongPassword_ShouldReturn401() throws Exception {
        registerUser("wrongpass", "password123");
        LoginRequest loginRequest = new LoginRequest("wrongpass", "incorrect-password");

        mockMvc.perform(post("/api/auth/login")
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .content(objectMapper.writeValueAsString(loginRequest)))
            .andExpect(status().isUnauthorized())
            .andExpect(jsonPath("$.message").value("Bad credentials"));
    }

    @Test
    @DisplayName("Test: logging in with non-existent username should also return 401, not 404")
    void test_LoginWithNonExistentUsername_ShouldReturn401() throws Exception {
        LoginRequest loginRequest = new LoginRequest("does-not-exists", "password");

        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(loginRequest)))
            .andExpect(status().isUnauthorized());
    }

    private void registerUser(String username, String password) throws Exception {
        RegisterRequest registerRequest = new RegisterRequest(username, username, password);
        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(registerRequest)))
                .andExpect(status().isCreated());
    }
}