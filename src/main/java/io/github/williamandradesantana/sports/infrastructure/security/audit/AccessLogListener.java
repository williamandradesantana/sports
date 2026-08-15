package io.github.williamandradesantana.sports.infrastructure.security.audit;

import io.github.williamandradesantana.sports.application.audit.RecordAccessLogCommand;
import io.github.williamandradesantana.sports.application.audit.RecordAccessLogUseCase;
import io.github.williamandradesantana.sports.domain.user.AuthProvider;
import io.github.williamandradesantana.sports.infrastructure.security.UserDetailsImpl;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.context.event.EventListener;
import org.springframework.security.authentication.event.AbstractAuthenticationFailureEvent;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

public class AccessLogListener {

    private final RecordAccessLogUseCase recordAccessLogUseCase;

    public AccessLogListener(RecordAccessLogUseCase recordAccessLogUseCase) {
        this.recordAccessLogUseCase = recordAccessLogUseCase;
    }

    @EventListener
    public void onSuccess(AuthenticationSuccessEvent event) {
        if (!(event.getAuthentication().getPrincipal() instanceof UserDetailsImpl userDetails))
            return;

        HttpServletRequest request = currentRequest();
        if (request == null) return;

        recordAccessLogUseCase.execute(new RecordAccessLogCommand(
            userDetails.getDomainUser().getId(),
            userDetails.getUsername(),
            AuthProvider.LOCAL.name(),
            RequestMetadataExtractor.extractIpAddress(request),
            RequestMetadataExtractor.extractUserAgent(request),
            true,
            null
        ));
    }

    @EventListener
    public void onFailure(AbstractAuthenticationFailureEvent event) {
        HttpServletRequest request = currentRequest();
        if (request == null) return;

        String attemptedUsername = String.valueOf(event.getAuthentication().getPrincipal());

        recordAccessLogUseCase.execute(new RecordAccessLogCommand(
            null,
            attemptedUsername,
            AuthProvider.LOCAL.name(),
            RequestMetadataExtractor.extractIpAddress(request),
            RequestMetadataExtractor.extractUserAgent(request),
            false,
            event.getException().getMessage()
        ));
    }

    private HttpServletRequest currentRequest() {
        var attributes = RequestContextHolder.getRequestAttributes();
        if (attributes instanceof ServletRequestAttributes servletAttributes) {
            return servletAttributes.getRequest();
        }
        return null;
    }
}
