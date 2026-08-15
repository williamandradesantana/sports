package io.github.williamandradesantana.sports.domain.audit;

import io.github.williamandradesantana.sports.domain.audit.exceptions.InvalidAccessLogException;

import java.time.OffsetDateTime;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

public class AccessLog {

    private final UUID id;
    private final UUID userId;
    private final String username;
    private final String provider;
    private final String ipAddress;
    private final String userAgent;
    private final boolean success;
    private final String failureReason;
    private final OffsetDateTime occurredAt;

    public AccessLog(UUID id, UUID userId, String username, String provider, String ipAddress, String userAgent, boolean success, String failureReason, OffsetDateTime occurredAt) {
        if (username == null || username.isBlank())
            throw new InvalidAccessLogException("Username cannot be null or blank!");
        if (provider == null || provider.isBlank())
            throw new InvalidAccessLogException("Provider cannot be null or blank!");
        if (occurredAt == null) throw new InvalidAccessLogException("OccurredAt cannot be null or blank!");

        this.id = id;
        this.userId = userId;
        this.username = username;
        this.provider = provider;
        this.ipAddress = ipAddress;
        this.userAgent = userAgent;
        this.success = success;
        this.failureReason = failureReason;
        this.occurredAt = occurredAt;
    }

    public static AccessLog successful(
            UUID userId, String username, String provider, String ipAddress, String userAgent
    ) {
        return new AccessLog(
                UUID.randomUUID(), userId, username, provider, ipAddress,
                userAgent, true, null, OffsetDateTime.now()
        );
    }

    public static AccessLog failed(String username, String provider, String ipAddress,
                                   String userAgent, String failureReason
    ) {
        return new AccessLog(
            UUID.randomUUID(), null, username, provider, ipAddress,
            userAgent, false, failureReason, OffsetDateTime.now()
        );
    }

    public UUID getId() {
        return id;
    }

    public Optional<UUID> getUserId() {
        return Optional.ofNullable(userId);
    }

    public String getUsername() {
        return username;
    }

    public String getProvider() {
        return provider;
    }

    public Optional<String> getIpAddress() {
        return Optional.ofNullable(ipAddress);
    }

    public Optional<String> getUserAgent() {
        return Optional.ofNullable(userAgent);
    }

    public boolean isSuccess() {
        return success;
    }

    public Optional<String> getFailureReason() {
        return Optional.ofNullable(failureReason);
    }

    public OffsetDateTime getOccurredAt() {
        return occurredAt;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;

        AccessLog accessLog = (AccessLog) o;
        return Objects.equals(id, accessLog.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
