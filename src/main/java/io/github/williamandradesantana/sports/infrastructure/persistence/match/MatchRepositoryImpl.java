package io.github.williamandradesantana.sports.infrastructure.persistence.match;

import io.github.williamandradesantana.sports.domain.match.Match;
import io.github.williamandradesantana.sports.domain.match.MatchRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class MatchRepositoryImpl implements MatchRepository {

    private final MatchJpaRepository jpaRepository;
    private final MatchMapper mapper;

    public MatchRepositoryImpl(MatchJpaRepository jpaRepository, MatchMapper mapper) {
        this.jpaRepository = jpaRepository;
        this.mapper = mapper;
    }

    @Override
    public Optional<Match> findById(UUID id) {
        return jpaRepository.findById(id).map(mapper::toDomain);
    }

    @Override
    public Optional<Match> findByExternalId(Long externalId) {
        return jpaRepository.findByExternalId(externalId).map(mapper::toDomain);
    }

    @Override
    public List<Match> findBySeasonId(UUID seasonId) {
        return jpaRepository.findBySeasonId(seasonId).stream().map(mapper::toDomain).toList();
    }

    @Override
    public List<Match> findByTeamId(UUID teamId) {
        return jpaRepository.findByHomeTeamIdOrAwayTeamId(teamId, teamId).stream().map(mapper::toDomain).toList();
    }

    @Override
    public void save(Match match) {
        jpaRepository.save(mapper.toJpaEntity(match));
    }
}
