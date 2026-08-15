package io.github.williamandradesantana.sports.infrastructure.persistence.audit;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.OffsetDateTime;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "tb_access_logs")
public class AccessLogJpaEntity {

    @Id
    private UUID id;

    @Column(name = "user_id")
    private UUID userId;

    @Column(name = "username", nullable = false)
    private String username;

    @Column(name = "provider", nullable = false)
    private String provider;

    @Column(name = "ip_address")
    private String ipAddress;

    @Column(name = "user_agent")
    private String userAgent;

    @Column(name = "success", nullable = false)
    private boolean success;

    @Column(name = "failure_reason")
    private String failureReason;

    @Column(name = "occurred_at", nullable = false)
    private OffsetDateTime occurredAt;

    protected AccessLogJpaEntity(){}

    public AccessLogJpaEntity(UUID id, UUID userId, String username, String provider, String ipAddress, String userAgent, boolean success, String failureReason, OffsetDateTime occurredAt) {
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

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getProvider() {
        return provider;
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public String getUserAgent() {
        return userAgent;
    }

    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getFailureReason() {
        return failureReason;
    }

    public void setFailureReason(String failureReason) {
        this.failureReason = failureReason;
    }

    public OffsetDateTime getOccurredAt() {
        return occurredAt;
    }

    public void setOccurredAt(OffsetDateTime occurredAt) {
        this.occurredAt = occurredAt;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;

        AccessLogJpaEntity that = (AccessLogJpaEntity) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
