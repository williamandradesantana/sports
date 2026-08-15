package io.github.williamandradesantana.sports.application.audit;

import io.github.williamandradesantana.sports.domain.audit.AccessLogRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AccessLogApplicationConfig {

    @Bean
    public RecordAccessLogUseCase recordAccessLogUseCase(AccessLogRepository accessLogRepository) {
        return new RecordAccessLogUseCase(accessLogRepository);
    }
}