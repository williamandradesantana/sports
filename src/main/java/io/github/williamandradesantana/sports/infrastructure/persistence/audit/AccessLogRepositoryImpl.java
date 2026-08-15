package io.github.williamandradesantana.sports.infrastructure.persistence.audit;

import io.github.williamandradesantana.sports.domain.audit.AccessLog;
import io.github.williamandradesantana.sports.domain.audit.AccessLogRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public class AccessLogRepositoryImpl implements AccessLogRepository {

    private final AccessLogJpaRepository jpaRepository;
    private final AccessLogMapper mapper;

    public AccessLogRepositoryImpl(AccessLogJpaRepository jpaRepository, AccessLogMapper mapper) {
        this.jpaRepository = jpaRepository;
        this.mapper = mapper;
    }

    @Override
    public void save(AccessLog accessLog) {
        jpaRepository.save(mapper.toJpaEntity(accessLog));
    }

    @Override
    public Page<AccessLog> findAll(Pageable pageable) {
        return jpaRepository.findAll(pageable).map(mapper::toDomain);
    }

    @Override
    public Page<AccessLog> findByUsername(String username, Pageable pageable) {
        return jpaRepository.findByUsername(username, pageable).map(mapper::toDomain);
    }
}
