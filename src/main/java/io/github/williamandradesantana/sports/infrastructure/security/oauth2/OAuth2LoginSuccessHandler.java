package io.github.williamandradesantana.sports.infrastructure.security.oauth2;

import io.github.williamandradesantana.sports.application.user.GoogleLoginUseCase;
import io.github.williamandradesantana.sports.application.user.GoogleProfileCommand;
import io.github.williamandradesantana.sports.application.audit.RecordAccessLogCommand;
import io.github.williamandradesantana.sports.application.audit.RecordAccessLogUseCase;
import io.github.williamandradesantana.sports.domain.user.AuthProvider;
import io.github.williamandradesantana.sports.domain.user.User;
import io.github.williamandradesantana.sports.infrastructure.security.audit.RequestMetadataExtractor;
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
    private final RecordAccessLogUseCase recordAccessLogUseCase;
    private final String authorizedRedirectUrl;

    public OAuth2LoginSuccessHandler(GoogleLoginUseCase googleLoginUseCase, RecordAccessLogUseCase recordAccessLogUseCase, String authorizedRedirectUrl) {
        this.googleLoginUseCase = googleLoginUseCase;
        this.recordAccessLogUseCase = recordAccessLogUseCase;
        this.authorizedRedirectUrl = authorizedRedirectUrl;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();

        Boolean emailVerified = oAuth2User.getAttribute("email_verified");
        String email = oAuth2User.getAttribute("email");
        if (emailVerified == null || !emailVerified) {
            recordAccessLogUseCase.execute(new RecordAccessLogCommand(
                null, email != null ? email : "unknown", AuthProvider.GOOGLE.name(),
                RequestMetadataExtractor.extractIpAddress(request),
                RequestMetadataExtractor.extractUserAgent(request),
                false, "Email not verified"
            ));

            getRedirectStrategy().sendRedirect(request, response,
                    UriComponentsBuilder.fromUriString(authorizedRedirectUrl)
                            .queryParam("error", "email_not_verified").build().toUriString());
            return;
        }

        String name = oAuth2User.getAttribute("name");
        User user = googleLoginUseCase.resolveUser(new GoogleProfileCommand(email, name));
        String token = googleLoginUseCase.generateTokenFor(user);

        recordAccessLogUseCase.execute(new RecordAccessLogCommand(
                user.getId(), user.getUsername(), AuthProvider.GOOGLE.name(),
                RequestMetadataExtractor.extractIpAddress(request),
                RequestMetadataExtractor.extractUserAgent(request),
                true, null
        ));

        String targetUrl = UriComponentsBuilder.fromUriString(authorizedRedirectUrl)
                .queryParam("token", token)
                .build().toUriString();

        getRedirectStrategy().sendRedirect(request, response, targetUrl);
    }
}
