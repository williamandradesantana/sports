package io.github.williamandradesantana.sports.infrastructure.persistence.audit;

import io.github.williamandradesantana.sports.domain.audit.AccessLogRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AccessLogPersistenceConfig {

    @Bean
    public AccessLogMapper accessLogMapper() {
        return new AccessLogMapper();
    }

    @Bean
    public AccessLogRepository accessLogRepository(
            AccessLogJpaRepository accessLogJpaRepository, AccessLogMapper accessLogMapper
    ) {
        return new AccessLogRepositoryImpl(accessLogJpaRepository, accessLogMapper);
    }
}
