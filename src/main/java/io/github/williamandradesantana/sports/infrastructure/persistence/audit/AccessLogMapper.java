package io.github.williamandradesantana.sports.infrastructure.persistence.audit;

import io.github.williamandradesantana.sports.domain.audit.AccessLog;

public class AccessLogMapper {

    public AccessLog toDomain(AccessLogJpaEntity entity) {
        return new AccessLog(
            entity.getId(), entity.getUserId(), entity.getUsername(), entity.getProvider(), entity.getIpAddress(),
            entity.getUserAgent(), entity.isSuccess(), entity.getFailureReason(), entity.getOccurredAt()
        );
    }

    public AccessLogJpaEntity toJpaEntity(AccessLog accessLog) {
        return new AccessLogJpaEntity(
            accessLog.getId(), accessLog.getUserId().orElse(null), accessLog.getUsername(),
            accessLog.getProvider(), accessLog.getIpAddress().orElse(null),
            accessLog.getUserAgent().orElse(null), accessLog.isSuccess(),
            accessLog.getFailureReason().orElse(null), accessLog.getOccurredAt()
        );
    }
}
