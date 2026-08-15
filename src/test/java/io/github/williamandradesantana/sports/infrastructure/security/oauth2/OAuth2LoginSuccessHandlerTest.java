package io.github.williamandradesantana.sports.infrastructure.security.oauth2;

import io.github.williamandradesantana.sports.application.audit.RecordAccessLogCommand;
import io.github.williamandradesantana.sports.application.audit.RecordAccessLogUseCase;
import io.github.williamandradesantana.sports.application.user.GoogleLoginUseCase;
import io.github.williamandradesantana.sports.application.user.GoogleProfileCommand;
import io.github.williamandradesantana.sports.domain.user.AuthProvider;
import io.github.williamandradesantana.sports.domain.user.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.io.IOException;
import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
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

    @Mock
    private RecordAccessLogUseCase recordAccessLogUseCase;

    private final String authorizedRedirectUrl = "http://localhost:3000/oauth2/redirect";
    private OAuth2LoginSuccessHandler oAuth2LoginSuccessHandler;

    @BeforeEach
    void setup() {
        response = new MockHttpServletResponse();
        oAuth2LoginSuccessHandler = new OAuth2LoginSuccessHandler(
                googleLoginUseCase, recordAccessLogUseCase, authorizedRedirectUrl
        );
    }

    @Test
    @DisplayName("Test: authenticate a user with oauth2 success")
    void test_AuthenticateAUserWithOAuth2Success() throws ServletException, IOException {
       when(authentication.getPrincipal()).thenReturn(oAuth2User);
       when(oAuth2User.getAttribute("email_verified")).thenReturn(true);
       when(oAuth2User.getAttribute("email")).thenReturn("william@email.com");
       when(oAuth2User.getAttribute("name")).thenReturn("william");

       User resolvedUser = new User(
           UUID.randomUUID(), "william", "william", "william@email.com", null,
           AuthProvider.GOOGLE, true, true, true, true,
           Set.of()
       );

       when(googleLoginUseCase.resolveUser(any(GoogleProfileCommand.class))).thenReturn(resolvedUser);
       when(googleLoginUseCase.generateTokenFor(resolvedUser)).thenReturn("jwt-token");

       oAuth2LoginSuccessHandler.onAuthenticationSuccess(request, response, authentication);

       verify(googleLoginUseCase).resolveUser(any(GoogleProfileCommand.class));
       verify(googleLoginUseCase).generateTokenFor(resolvedUser);
       verify(recordAccessLogUseCase).execute(any(RecordAccessLogCommand.class));
       assertEquals(authorizedRedirectUrl + "?token=jwt-token", response.getRedirectedUrl());
    }
}