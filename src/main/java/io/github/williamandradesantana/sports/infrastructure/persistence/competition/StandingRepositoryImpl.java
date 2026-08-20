package io.github.williamandradesantana.sports.infrastructure.persistence.competition;

import io.github.williamandradesantana.sports.domain.competition.Standing;
import io.github.williamandradesantana.sports.domain.competition.StandingRepository;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class StandingRepositoryImpl implements StandingRepository {

    private final StandingJpaRepository jpaRepository;
    private final StandingMapper mapper;

    public StandingRepositoryImpl(StandingJpaRepository jpaRepository, StandingMapper mapper) {
        this.jpaRepository = jpaRepository;
        this.mapper = mapper;
    }

    @Override
    public List<Standing> findBySeasonId(UUID seasonId) {
        return jpaRepository.findBySeasonId(seasonId).stream().map(mapper::toDomain).toList();
    }

    @Override
    public Optional<Standing> findBySeasonIdAndTeamId(UUID seasonId, UUID teamId) {
        return jpaRepository.findBySeasonIdAndTeamId(seasonId, teamId).map(mapper::toDomain);
    }

    @Override
    public void save(Standing standing) {
        jpaRepository.save(mapper.toJpaEntity(standing));
    }
}
