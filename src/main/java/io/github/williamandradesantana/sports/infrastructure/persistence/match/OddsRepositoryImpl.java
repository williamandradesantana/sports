package io.github.williamandradesantana.sports.infrastructure.persistence.match;

import io.github.williamandradesantana.sports.domain.match.Odds;
import io.github.williamandradesantana.sports.domain.match.OddsRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class OddsRepositoryImpl implements OddsRepository {

    private final OddsJpaRepository jpaRepository;
    private final OddsMapper mapper;

    public OddsRepositoryImpl(OddsJpaRepository jpaRepository, OddsMapper mapper) {
        this.jpaRepository = jpaRepository;
        this.mapper = mapper;
    }

    @Override
    public List<Odds> findByMatchId(UUID matchId) {
        return jpaRepository.findByMatchIdOrderByCapturedAtAsc(matchId).stream().map(mapper::toDomain).toList();
    }

    @Override
    public Optional<Odds> findLatestByMatchId(UUID matchId) {
        return Optional.ofNullable(jpaRepository.findFirstByMatchIdOrderByCapturedAtDesc(matchId))
                .map(mapper::toDomain);
    }

    @Override
    public void save(Odds odds) {
        jpaRepository.save(mapper.toJpaEntity(odds));
    }
}
