package io.github.williamandradesantana.sports.infrastructure.security.audit;

import jakarta.servlet.http.HttpServletRequest;

public final class RequestMetadataExtractor {

    private RequestMetadataExtractor(){}

    public static String extractIpAddress(HttpServletRequest request) {
        String forwardedFor = request.getHeader("X-Forwarded-For");
        if (forwardedFor != null && !forwardedFor.isBlank())
            return forwardedFor.split(",")[0].trim();

        return request.getRemoteAddr();
    }

    public static String extractUserAgent(HttpServletRequest request) {
        return request.getHeader("User-Agent");
    }
}
