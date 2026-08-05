package io.github.williamandradesantana.sports.infrastructure.security.oauth2;

import io.github.williamandradesantana.sports.application.user.GoogleLoginUseCase;
import io.github.williamandradesantana.sports.application.user.GoogleProfileCommand;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;

public class OAuth2LoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final GoogleLoginUseCase googleLoginUseCase;
    private final String authorizedRedirectUrl;

    public OAuth2LoginSuccessHandler(GoogleLoginUseCase googleLoginUseCase, String authorizedRedirectUrl) {
        this.googleLoginUseCase = googleLoginUseCase;
        this.authorizedRedirectUrl = authorizedRedirectUrl;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();

        Boolean emailVerified = oAuth2User.getAttribute("email_verified");
        if (emailVerified == null || !emailVerified) {
            getRedirectStrategy()
                .sendRedirect(request, response,
                    UriComponentsBuilder
                        .fromUriString(authorizedRedirectUrl).queryParam("error", "email_not_verified")
                            .build().toUriString()
                );
            return;
        }

        String email = oAuth2User.getAttribute("email");
        String name = oAuth2User.getAttribute("name");

        String token = googleLoginUseCase.execute(new GoogleProfileCommand(email, name));

        String targetUrl = UriComponentsBuilder.fromUriString(authorizedRedirectUrl)
                .queryParam("token", token)
                .build().toUriString();

        getRedirectStrategy().sendRedirect(request, response, targetUrl);
    }
}
