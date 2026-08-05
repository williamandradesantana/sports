package io.github.williamandradesantana.sports.infrastructure.security.oauth2;

import io.github.williamandradesantana.sports.application.user.GoogleLoginUseCase;
import io.github.williamandradesantana.sports.application.user.GoogleProfileCommand;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OAuth2LoginSuccessHandlerTest {

    @Mock
    private GoogleLoginUseCase googleLoginUseCase;

    @Mock
    private HttpServletRequest request;

    private MockHttpServletResponse response;

    @Mock
    private Authentication authentication;

    @Mock
    private OAuth2User oAuth2User;

    private final String authorizedRedirectUrl = "http://localhost:3000/oauth2/redirect";
    private OAuth2LoginSuccessHandler oAuth2LoginSuccessHandler;

    @BeforeEach
    void setup() {
        response = new MockHttpServletResponse();
        oAuth2LoginSuccessHandler = new OAuth2LoginSuccessHandler(googleLoginUseCase, authorizedRedirectUrl);
    }

    @Test
    @DisplayName("Test: authenticate a user with oauth2 success")
    void test_AuthenticateAUserWithOAuth2Success() throws ServletException, IOException {
       when(authentication.getPrincipal()).thenReturn(oAuth2User);
       when(oAuth2User.getAttribute("email_verified")).thenReturn(true);
       when(oAuth2User.getAttribute("email")).thenReturn("william@email.com");
       when(oAuth2User.getAttribute("name")).thenReturn("william");

       when(googleLoginUseCase.execute(any(GoogleProfileCommand.class))).thenReturn("jwt-token");

       oAuth2LoginSuccessHandler.onAuthenticationSuccess(request, response, authentication);

       verify(googleLoginUseCase).execute(any(GoogleProfileCommand.class));
       assertEquals(authorizedRedirectUrl + "?token=jwt-token", response.getRedirectedUrl());

    }
}