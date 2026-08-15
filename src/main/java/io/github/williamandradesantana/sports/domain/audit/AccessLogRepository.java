package io.github.williamandradesantana.sports.domain.audit;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface AccessLogRepository {
    void save(AccessLog accessLog);
    Page<AccessLog> findAll(Pageable pageable);
    Page<AccessLog> findByUsername(String username, Pageable pageable);
}
