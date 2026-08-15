package io.github.williamandradesantana.sports.application.audit;

import io.github.williamandradesantana.sports.domain.audit.AccessLog;
import io.github.williamandradesantana.sports.domain.audit.AccessLogRepository;

public class RecordAccessLogUseCase {

    private final AccessLogRepository accessLogRepository;

    public RecordAccessLogUseCase(AccessLogRepository accessLogRepository) {
        this.accessLogRepository = accessLogRepository;
    }

    public void execute(RecordAccessLogCommand command) {
        AccessLog accessLog = command.success()
            ? AccessLog.successful(
                command.userId(), command.username(), command.provider(),
                command.ipAddress(), command.userAgent()
            )
            : AccessLog.failed(
                command.username(), command.provider(), command.ipAddress(),
                command.userAgent(), command.failedReason()
            );

        accessLogRepository.save(accessLog);
    }
}
