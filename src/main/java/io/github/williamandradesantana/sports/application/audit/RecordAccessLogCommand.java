package io.github.williamandradesantana.sports.application.audit;

import java.util.UUID;

public record RecordAccessLogCommand(
        UUID userId, String username, String provider, String ipAddress,
        String userAgent, boolean success, String failedReason
) {
}
