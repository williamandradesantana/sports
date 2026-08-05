package io.github.williamandradesantana.sports.infrastructure.security.oauth2;

import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class OAuth2LoginFailureHandlerTest {

    @Mock
    private HttpServletRequest request;

    @Mock
    private AuthenticationException exception;

    private MockHttpServletResponse response;

    private OAuth2LoginFailureHandler failureHandler;
    private final String authorizedRedirectUrl = "http://localhost:3000/oauth2/redirect";

    @BeforeEach
    void setup() {
        response = new MockHttpServletResponse();
        failureHandler = new OAuth2LoginFailureHandler(authorizedRedirectUrl);
    }

    @Test
    @DisplayName("Test: Should redirect to frontend with authentication_failed error")
    void shouldRedirectToFrontendWithAuthenticationFailedError() throws IOException {
        failureHandler.onAuthenticationFailure(request, response, exception);
        assertEquals(authorizedRedirectUrl + "?error=authentication_failed", response.getRedirectedUrl());
    }
}