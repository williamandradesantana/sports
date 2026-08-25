package io.github.williamandradesantana.sports.infrastructure.persistence.match;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface OddsJpaRepository extends JpaRepository<OddsJpaEntity, UUID> {
    List<OddsJpaEntity> findByMatchIdOrderByCapturedAtAsc(UUID matchId);
    OddsJpaEntity findFirstByMatchIdOrderByCapturedAtDesc(UUID matchId);
}
